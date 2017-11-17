package com.ibm.kstar.main;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.api.system.permission.entity.VerificationCode;
import com.ibm.kstar.conf.AdminConfiguration;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.session.SessionManager;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.MD5Util;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class MainAction extends BaseAction {

	@Autowired
	ICorePermissionService corePermissionService;

	@Autowired
	ICustomInfoService customInfoService;
	
	@Autowired
	IUserService userService ;

	@Autowired
	IKstarAttachmentService attachmentService;
	
	@Autowired
	AdminConfiguration adminConfiguration;
	
	@Autowired
	protected ILovMemberService lovMemberService;

	@NoRight
	@RequestMapping("/main")
	public String index(Model model){
		
		UserObject user = getUserObject();
		//外部组织
		if("E".equals(user.getOrg().getOptTxt3())){
			CustomInfo customInfo = customInfoService.getCustomInfo(user.getOrg().getOptTxt4());
			if(customInfo==null){
				throw new AnneException("经销商账号未关联客户");
			}
			model.addAttribute("custName",customInfo.getCustomFullName());
			model.addAttribute("custLevel",customInfo.getCustomGradeName());
			model.addAttribute("yearTask",0);
			model.addAttribute("authoArea","ALL");
			model.addAttribute("authoProd","ALL");

			List<KstarAttachment> list = attachmentService.getDocCenter();
			model.addAttribute("docCenter",list);

			return forward("main");
		}else{
			TabMain tabMain = new TabMain();
			tabMain.setInitAll(true);
			tabMain.addTab("合同收款计划明细", "/receipts/contractReceiptList.html");
			tabMain.addTab("核销明细", "/receipts/verificationList.html");
			model.addAttribute("tabMain", tabMain);
			return forward("main2");
		}
	}
	
	@NoRight
	@RequestMapping("/error")
	public String error(HttpServletRequest request){
		return forward("error");
	}

	/**
	 * 找回密码 界面
	 *
	 * @return
	 */
	@NoRight
	@RequestMapping(value = "/findPassword")
	public String findPassword() {
		return forward("findPassword");
	}

	/**
	 * 找回密码
	 *
	 * @param email
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/findPassword", method = RequestMethod.POST)
	public String findPassword(String email) {
		userService.findPassword(email);
		return sendSuccessMessage();
	}

	@NoRight
	@RequestMapping("/resetPassword")
	public String resetPassword(String email, String verificationCode, Model model) {
		model.addAttribute("code", verificationCode);
		model.addAttribute("type", "reset");
		model.addAttribute("email", email);
		return forward("modifyPassword");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPassword(String email, String verificationCode, String newPassword, String confirmPassword) {
		if (StringUtils.isEmpty(newPassword)) {
			throw new AnneException("密码不能为空");
		}
		if (!Objects.equals(newPassword, confirmPassword)) {
			throw new AnneException("新旧密码不相同");
		}
		VerificationCode code = userService.getLastVerificationCode(email, verificationCode);
		if (code == null) {
			return sendErrorMessage("无效的验证码");
		}
		userService.resetPassword(code.getUserId(), newPassword);
		return sendSuccessMessage();
	}

	@RequestMapping("/login")
	public String login() {
		Object user = getUserObject();
		if (user != null) {
			return redirect("/main");
		}
		return forward("login");
	}

	@RequestMapping("/agentLogin")
	public String agentLogin() {
		return forward("login2");
	}

	@ResponseBody
	@RequestMapping(value = "/agentLogin", method = RequestMethod.POST)
	public String agentLogin(String username, String password, String noRight, HttpServletRequest request) {
		return login(username, password, noRight, request);
	}

	@RequestMapping("/noRight")
	public String noRight(String uri, Model model) {
		model.addAttribute("uri", uri);
		return forward("noRight");
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		getSession().invalidate();
		return redirect("/login");
	}

	@LogOperate(module = "登录模块", notes = "用户登录")
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password, String noRight, HttpServletRequest request) {
		//登录
		UserObject user = corePermissionService.login("CRM", username, password);
		if (user == null) {
			throw new AnneException("登录失败，账号或者密码有误");
		}

		String clientIp = ActionUtil.getClientIP(request);

		if (lovMemberService.switchIsOpen(IConstants.JAVA_CODE_LOGIC_SWITCH, IConstants.SWITCH_CODE_LOGIN_IP_LIMIT)) {
			//管理员账号只能在内网登陆
			if ("admin".equalsIgnoreCase(user.getEmployee().getNo()) || adminConfiguration.isAdmin(user.getEmployee().getNo())) {
				if (!clientIp.startsWith("10.0.")//惠州
						&& !clientIp.startsWith("10.1.")//南山
						&& !clientIp.startsWith("10.2.")//光明
						&& !clientIp.startsWith("10.3.")//东莞
						) {
					System.out.println("登陆IP:" + clientIp);
//					throw new AnneException("登录失败，管理员登陆IP["+clientIp+"]不正确");
				}
			}
			
			/*String macAddress = "";
			user.setClientMac(macAddress);
			try {
				macAddress = ActionUtil.GetRemoteMacAddr(clientIp);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		
		if( !(user.getEmployee().getEndDate().getTime() > System.currentTimeMillis() && System.currentTimeMillis() > user.getEmployee().getStartDate().getTime() )){
			throw new AnneException("账号不在可用范围内");
		}
		
		List<LovMember> positions = corePermissionService.getUserPositionList(user.getEmployee().getId());
		if(positions.size() == 0){
			throw new AnneException("登录失败，账号还未分配岗位，请联系管理员分配");
		}
		
		user.setLoginIp(clientIp);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		user.setLoginDate(format.format(new Date()));
		
		user.setSessionId(getSession().getId());
		
		//使用记录的最后一次登录岗位
		User u = userService.get(user.getEmployee().getId());
		LovMember current = null;
		for(LovMember lov : positions){
			if(lov.getId().equals(u.getLastPositionId())){
				user.setPosition(lov);
				current = lov;
			}
		}
		if(current == null){
			user.setPosition(positions.get(0));
		}
		LovMember org = corePermissionService.getOrg(user.getPosition().getId());
		
		if(org == null){
			throw new AnneException("数据异常，未找到用户所属的组织");
		}
		
		user.setOrg(org);
		
		if("ADMIN".equals(user.getPosition().getCode())){
			user.initPermission(null);
		}else{
			user.initPermission(corePermissionService.getPermissionListByPosition("CRM", user.getPosition().getPositionInOrgId()));
		}
		
		getSession().setAttribute("permission",user.getPermissionCodeMap());
		getSession().setAttribute("positions",positions);

		
		getSession().setAttribute("LOGIN_USER", user);
		//设置权限信息
		getSession().setAttribute("permissionCode",user.getPermissionCodeMap());
		getSession().setAttribute("permissionPath",user.getPermissionPathMap());
		getSession().setAttribute("permissionCodePath",user.getPermissionCodePathMap());

		String uuid = null;
		if("yes".equals(request.getParameter("mobile"))){
			uuid = request.getSession().getId();
			SessionManager.getInstance().setMappingSession(uuid, getSession());
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("JSESSION", uuid);
		map.put("positions", positions);
		map.put("user", user);
		return sendSuccessMessage(map);
	}
	
	@NoRight
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/changePosition",method = RequestMethod.POST)
	public String changePosition(String positionId,HttpServletRequest request){
		List<LovMember> positions = (List<LovMember>)getSession().getAttribute("positions");
		UserObject user = getUserObject();
		
		LovMember position = null;
		
		if("admin".equals(user.getEmployee().getNo())){
			position = corePermissionService.getPositionById(positionId);
		}else{
			boolean ok = false;
			for(LovMember lov : positions){
				if(lov.getId().equals(positionId)){
					position = lov;
					ok = true;
					break;
				}
			}
			if(!ok){
				throw new AnneException("未找到切换的岗位");
			}
		}
		
		if(position == null){
			throw new AnneException("未找到切换的岗位");
		}
		
		user.setPosition(position);
		user.setOrg(corePermissionService.getOrg(user.getPosition().getId()));
		
		if("ADMIN".equals(position.getCode())){
			user.initPermission(null);
		}else{
			user.initPermission(corePermissionService.getPermissionListByPosition("CRM", user.getPosition().getPositionInOrgId()));
		}
		
		getSession().setAttribute("permission",user.getPermissionCodeMap());
		getSession().setAttribute("LOGIN_USER", user);
		//设置权限信息
		getSession().setAttribute("permissionCode",user.getPermissionCodeMap());
		getSession().setAttribute("permissionPath",user.getPermissionPathMap());
		getSession().setAttribute("permissionCodePath",user.getPermissionCodePathMap());
		
		userService.changePosition(user.getEmployee().getId(),positionId);
		return sendSuccessMessage();
	}

	@NoRight
	@RequestMapping("/modifyPassword")
	public String modifyPassword() {
		return forward("modifyPassword");
	}

	@RequestMapping("/onlineUsers")
	public String onlineUsers(Model model) {
		/*List<UserObject> list = SessionManager.getOnlineUser();
		model.addAttribute("list",list);*/
		return forward("onlineUsers");
	}

	@ResponseBody
	@RequestMapping(value = "/getOnlineUsers", method = RequestMethod.POST)
	public String getOnlineUsers(Model model, HttpServletRequest request) {
		List<UserObject> list = SessionManager.getOnlineUser();
		List<UserObject> dataList = new ArrayList<UserObject>();
		dataList.addAll(list);

		String qryCondition = request.getParameter("qryCondition");
		String ipAddress = request.getParameter("ipAddress");
		String startTime = request.getParameter("qryCondition");
		String endTime = request.getParameter("qryCondition");
		if(!StringUtil.isNullOrEmpty(qryCondition)){
			qryCondition = qryCondition.trim();
			if(!StringUtil.isNullOrEmpty(qryCondition)){
				for(UserObject userObject : list){
					if(!userObject.getEmployeeName().contains(qryCondition)
							&& !userObject.getPosition().getName().contains(qryCondition)
							&& !userObject.getOrg().getName().contains(qryCondition)
							&& !userObject.getEmployeeNo().contains(qryCondition)){
						dataList.remove(userObject);
					}
				}
			}
		}
		
		if(!StringUtil.isNullOrEmpty(ipAddress)){
			ipAddress = ipAddress.trim();
			for(UserObject userObject : list){
				if(!userObject.getLoginIp().contains(ipAddress)){
					dataList.remove(userObject);
				}
			}
		}
		
		if(!StringUtil.isNullOrEmpty(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date start = format.parse(startTime);
				for(UserObject userObject : list){
					try {
						Date loginDate = format.parse(userObject.getLoginDate());
						if(loginDate.before(start)){
							dataList.remove(userObject);
						}
					} catch (Exception e) {
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if(!StringUtil.isNullOrEmpty(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date end = format.parse(endTime);
				for(UserObject userObject : list){
					try {
						Date loginDate = format.parse(userObject.getLoginDate());
						if(loginDate.after(end)){
							dataList.remove(userObject);
						}
					} catch (Exception e) {
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return sendMessage(JSON.toJSONString(dataList));
	}
	
	@ResponseBody
	@RequestMapping(value="/cutLink",method = RequestMethod.POST)
	public String cutLink(String sessionId){
		List<HttpSession> list = SessionManager.getOnlineSession();
		for(HttpSession session : list){
			if(sessionId.equals(session.getId())){
				session.invalidate();
				break;
			}
		}
		return sendSuccessMessage();
	}
	
	@RequestMapping("/onlineNotice")
	public String onlineNotice(Model model){
		return forward("onlineNotice");
	}
	
	@ResponseBody
	@RequestMapping(value="/onlineNotice",method = RequestMethod.POST)
	public String onlineNotice(String notice,Model model){
		List<HttpSession> sessionList = SessionManager.getOnlineSession();
		for(HttpSession session : sessionList){
			session.setAttribute("NOTICE", notice);
		}
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/getNotice",method = RequestMethod.POST)
	public String getNotice(String notice,Model model){
		HttpSession session = getSession();
		Object o = session.getAttribute("NOTICE");
		if(o!=null){
			session.removeAttribute("NOTICE");
			sendSuccessMessage(o);
		}
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/modifyPasswordSave",method = RequestMethod.POST)
	public String modifyPasswordSave(HttpServletRequest request){
		String oldPassword = request.getParameter("oldPassword") == null ? "" : request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		
		UserObject userObject = getUserObject();
		User user = userService.get(userObject.getEmployee().getId());
		if(newPassword == null){
			throw new AnneException("新密码不能为空");
		}
		if(!newPassword.equals(confirmPassword)){
			throw new AnneException("新密码和确认密码不一致，请重新输入");
		}
		if(user == null){
			throw new AnneException("对不起，该用户不存在");
		}else{
			oldPassword = MD5Util.MD5Encode(oldPassword, "UTF-8");
			
			if(!oldPassword.equals(user.getPassword())){
				throw new AnneException("对不起，您输入的原密码错误，请重新输入");
			}else{
				user.setPassword(MD5Util.MD5Encode(newPassword, "UTF-8"));
				userService.modifyPassword(user);
			}
		}
		return sendSuccessMessage();
	}
	
	@RequestMapping("downloadExcleData")
	public String downloadExcleData(){
		return forward("downloadExcleData");
	}
	
	@RequestMapping("/downLoadDataBySQL")
	public void downLoadDataBySQL(HttpServletRequest request,HttpServletResponse response){
		String exportSql = request.getParameter("exportSql");
		String exportHead = request.getParameter("exportHead");
		
		String str = exportSql.toLowerCase();
		if(str.contains("insert") || str.contains("update") || str.contains("delete")){
			throw new AnneException("对不起，sql不能有insert/update/delete等关键字");
		}
		
		List<Map<String, Object>> list = corePermissionService.downLoadDataBySQL(exportSql);
		
		List<List<Object>> dataList = turnToDataList(list, exportHead);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ExcelUtil.exportExcel(response, dataList, "报表-"+sdf.format(new Date()));
	}

	private List<List<Object>> turnToDataList(List<Map<String, Object>> list, String exportHead) {
		List<List<Object>> dataList = new ArrayList<>();
		if(list != null && !list.isEmpty()){
			List<Object> head = new ArrayList<>();
			if(!StringUtil.isNullOrEmpty(exportHead)){
				Object[] headStrs = exportHead.split(",");
				head = Arrays.asList(headStrs);
			}else{
				head.addAll(list.get(0).keySet());
			}
			dataList.add(head);
			
			for(Map<String, Object> map : list){
				List<Object> data = new ArrayList<>();
				for(Object key : head){
					data.add(map.get(key));
				}
				dataList.add(data);
			}
			
		}
		return dataList;
	}
	
}
