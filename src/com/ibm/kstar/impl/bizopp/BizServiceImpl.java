package com.ibm.kstar.impl.bizopp;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.*;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.service.IDemoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.web.utils.MathUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.Task;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * ClassName:BizServiceImpl<br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 9:30:25 PM <br/>
 *
 * @author Wutw
 * @see
 * @since JDK 1.7
 */

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizServiceImpl implements IBizBaseService {

    @Autowired
    BaseDao baseDao;

    @Autowired
    ILovMemberService lovMemberService;

    @Autowired
    ProcessStatusService processStatusService;

    @Autowired
    IProcessService processService;

    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;

    @Autowired
    protected ITeamService teamService;

    @Autowired
    IOrgTeamService orgTeamService;

    @Autowired
    ICustomInfoService customInfoService;

    @Autowired
    IPriceHeadService priceHeadService;
    
    @Autowired
    IProductService productService;
    
    @Autowired
    IDemoService demoService;
    
    @Autowired
    IOrderService orderService;
    
    @Autowired
	IBizoppService bizoppService;
    
    @Autowired
	ITaskService taskService;

    @Override
    public <T> void save(T t) {
        baseDao.save(t);
    }

    @Override
    public <T> void update(T t) {
        baseDao.saveOrUpdate(t);
    }

    @Override
    public IPage query(PageCondition condition, Class clazz) throws Exception {
        FilterObject filterObject = condition.getFilterObject();
        filterObject.setClazz(clazz);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        IPage p = baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
        return p;
    }


    @Override
    public IPage queryPrototype(PageCondition condition, String systemPic) throws Exception {
        return baseDao.search("from Prototype where systemPid = ? ", new Object[]{systemPic}, condition.getRows(), condition.getPage());
    }

    @Override
    public <T> T getEntity(String id, T t) {
        return (T) baseDao.get(t.getClass(), id);
    }

    @Override
    public void delete(String id, Class clazz) {
        baseDao.deleteById(clazz, id);
    }

    @Override
    public void startApplyProcess(UserObject user, String id, String applyNumber) {
        String application = "BIZOPP_SPECIAL_PRICE_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        vmap.put("TODO", "TODO");
        //更新流程状态为已发起,记录流程ID
        xflowProcessServiceWrapper.start(model, application, applyNumber + "_" + ProcessConstants.BIZOPP_SPECIAL_PRICE_APPLY_PROC, id, user, vmap);
        processStatusService.updateProcessStatus("SpecialPrice", id, "applyStatus", ProcessConstants.PROCESS_STATUS_Processing);
    }


    @Override
    public void startApplyProcessRebate(UserObject user, String id, String applyNumber) {
        String application = "BIZOPP_REBATE_PRICE_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        Rebate rebate = getRebate(id);
        
        String salesCenter = lovMemberService.getSaleCenter(user.getOrg().getId());
		vmap.put("SalesCenter", salesCenter);

        Employee employee =  ((Employee)CacheData.getInstance().get(rebate.getBusinessExecutive()));
        vmap.put("employeeIdInForm", employee.getId());
        vmap.put("employeeNameInForm", employee.getName());
        
//		DISTRIBUTE_SERIES	"是否分销系列产品：Y-分销系列  N-非分销系列
//		判断方法：特价申请单，配置明细中，产品型号为YDC/YDE开头的"
        String DISTRIBUTE_SERIES = "N";
        Double minApplyRebate = 100D;
        Double minApproveRebate = 100D;
        List<RebateLine> rebateLines = getRebateLine(id, null, null);
        for (RebateLine rl : rebateLines) {
        	if(rl.getSourcePrice()==0||rl.getApplyQty()==0) {
        		throw new AnneException("公开价格与申请数量不能为0！");
        	}
        	if(rl.getApplyRebate() != null){
        		if(rl.getApplyRebate() < minApplyRebate){
        			minApplyRebate = rl.getApplyRebate();
        		}
        	}
        	if(rl.getApproveRebate() != null){
        		// 电池没有批复折扣
        		if(rl.getApproveRebate() < minApproveRebate){
        			minApproveRebate = rl.getApproveRebate();
        		}
        	}
            if (StringUtils.isNotEmpty(rl.getProductModel())) {
                if (rl.getProductModel().startsWith("YDC") || rl.getProductModel().startsWith("YDE")) {
                    DISTRIBUTE_SERIES = "Y";
                    //break;
                }
            }
        }
        vmap.put("minApplyRebate", minApplyRebate.toString());
        vmap.put("minApproveRebate", minApproveRebate.toString());
        vmap.put("DISTRIBUTE_SERIES", DISTRIBUTE_SERIES);
        
        String sql = "select * from sys_t_lov_member a where 1=1 and a.group_Code = 'ORG' and a.opt_Txt3 in('C','B') "
        		+ "and a.leaf_Flag='N' start with a.row_id = ? connect by prior a.parent_Id = a.row_id";
        String orgType = "C";
        List<Map<String, Object>> positions = baseDao.findBySql4Map(sql,new Object[]{user.getOrg().getId()});
        if(positions != null && !positions.isEmpty()){
        	orgType = positions.get(0).get("OPT_TXT3").toString();
        }
        vmap.put("orgType", orgType);
        
        initVMap(vmap, user, id,rebate.getType());
        //更新流程状态为已发起,记录流程ID
        xflowProcessServiceWrapper.start(model, application, applyNumber + "_" + ProcessConstants.BIZOPP_REBATE_PRICE_APPLY_PROC, id, user, vmap);
        processStatusService.updateProcessStatus("Rebate", id, "status", ProcessConstants.PROCESS_STATUS_Processing);
    }

    
    private void initVMap(Map<String, String> vmap,UserObject user, String id,String type){
    	String sql = "select count(1) from CRM_T_REBATE_LINE rl,crm_t_product_basic pb where rl.product_id = pb.c_pid and rl.product_sort_Name  = '非标产品' and rl.rebate_id = ? ";
        BigDecimal count = baseDao.findUniqueBySql(sql,id);
        if(count.intValue() > 0){
        	vmap.put("INCLUDE_NONSTANDARD", "Y");
        }else{
        	vmap.put("INCLUDE_NONSTANDARD", "N");
        }
        String salesCenter = lovMemberService.getSaleCenter(user.getOrg().getId());
		vmap.put("SalesCenter", salesCenter);
        
        
//		SPECIAL_PRICE_TYPE	特价申请单类型：N-常规  I-行业入围
        String SPECIAL_PRICE_TYPE = type;
        vmap.put("SPECIAL_PRICE_TYPE", SPECIAL_PRICE_TYPE);

        /**"当前岗位所属层级    数值：01/02/03/04/05/06
         判断方法：根据当前操作岗位与价格层级表中的各层级岗位进行比对。判断当前操作员岗位属于哪一级。"
         **/
        String POSITION_LEVEL = getPriceLayCompareLine(user.getPosition().getId());
        vmap.put("POSITION_LEVEL", POSITION_LEVEL);


        /**"最高审批层级  数值：01/02/03/04/05/06
         判断方法：特价申请单明细中所有产品行的最高审批层级。每一个产品行的最高审批层级通过当前申请折扣同价格层级表中的层级折扣比对。"
         **/
        if ("常规".equals(type) || "分销".equals(type)) {
            List<Map<String, Object>> maps = getPriceCompareVo(id, user.getOrg().getId());
            String TOP_APPROVE_LEVEL = "";
            int level = 0;

            for (Map<String, Object> map : maps) {
                int tmp_level = 0;
                if (Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER1_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "01";
                }
                if (Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER2_DISCOUNT")))
                        && Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER1_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "02";
                }
                if (Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER3_DISCOUNT")))
                        && Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER2_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "03";
                }
                if (Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER4_DISCOUNT")))
                        && Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER3_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "04";
                }
                if (Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER5_DISCOUNT")))
                        && Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER4_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "05";
                }
                if (/**Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) >= Double.valueOf(String.valueOf(map.get("n_layer6_discount")))
                 && **/Double.valueOf(String.valueOf(map.get("APPLY_REBATE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER5_DISCOUNT")))) {
                    TOP_APPROVE_LEVEL = "06";
                }
                tmp_level = Integer.parseInt(TOP_APPROVE_LEVEL.replaceAll("0", ""));
                if (tmp_level > level) {
                    level = tmp_level;
                }
            }
            vmap.put("TOP_APPROVE_LEVEL", "0" + level);
        }

        /**"是否低于金牌价/批发价 数值：Y/N
         判断方法：1）特价申请单申请明细中的所有行进行判断，只要有一行低于金牌价/批发价，为 Y。
         2）对单行产品数据判断方法：如果申请数量为1，低于金牌价为Y，否则为N;如果申请数量大于1，低于批发价为Y，否则为N."
         **/
        if ("电池".equals(type)) {
            List<Map<String, Object>> maps = getPriceCompareVo(id, user.getOrg().getId());
            String BELOW_GOLD_PRICE = "N";
            for (Map<String, Object> map : maps) {
//                if (Integer.parseInt(String.valueOf(map.get("apply_qty"))) == 1) {
//                    if (Double.valueOf(String.valueOf(map.get("apply_price"))) < Double.valueOf(String.valueOf(map.get("n_layer1_price")))) {
//                        BELOW_GOLD_PRICE = "Y";
//                        break;
//                    }
//                } else {

                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) < Double.valueOf(String.valueOf(map.get("N_LAYER2_NUMBER")))) {
                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER1_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }
                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER2_NUMBER")))
                            && Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) < Double.valueOf(String.valueOf(map.get("N_LAYER3_NUMBER")))) {

                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER2_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }

                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER3_NUMBER")))
                            && Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) < Double.valueOf(String.valueOf(map.get("N_LAYER4_NUMBER")))) {

                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER3_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }

                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER4_NUMBER")))
                            && Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) < Double.valueOf(String.valueOf(map.get("N_LAYER5_NUMBER")))) {

                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER4_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }

                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER5_NUMBER")))
                            && Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) < Double.valueOf(String.valueOf(map.get("N_LAYER6_NUMBER")))) {

                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER5_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }

                    if (Double.valueOf(String.valueOf(map.get("APPLY_QTY"))) >= Double.valueOf(String.valueOf(map.get("N_LAYER6_NUMBER")))) {

                        if (Double.valueOf(String.valueOf(map.get("APPLY_PRICE"))) < Double.valueOf(String.valueOf(map.get("N_LAYER6_PRICE")))) {
                            BELOW_GOLD_PRICE = "Y";
                            break;
                        }
                    }
//                }
                vmap.put("BELOW_GOLD_PRICE", BELOW_GOLD_PRICE);
            }
        }
    }
    
    
    public List<Map<String, Object>> getPriceCompareVo(String rebateId, String orgId) {

        StringBuilder sb = new StringBuilder();
        sb.append("   select rl.product_id,                                                    ");
        sb.append("           rl.productname,                                                  ");
        sb.append("           nvl(l.n_layer1_price,0)  n_layer1_price,                         ");
        sb.append("           nvl(l.n_layer1_discount,1)  n_layer1_discount,                   ");
        sb.append("           nvl(l.n_layer1_number,0)  n_layer1_number,                       ");
        sb.append("           nvl(l.n_layer2_price,0)  n_layer2_price,                         ");
        sb.append("           nvl(l.n_layer2_discount,1)  n_layer2_discount,                   ");
        sb.append("           nvl(l.n_layer2_number,0) n_layer2_number,                        ");
        sb.append("           nvl(l.n_layer3_price,0) n_layer3_price,                          ");
        sb.append("           nvl(l.n_layer3_discount,1) n_layer3_discount,                    ");
        sb.append("           nvl(l.n_layer3_number,0) n_layer3_number,                        ");
        sb.append("           nvl(l.n_layer4_price,0) n_layer4_price,                          ");
        sb.append("           nvl(l.n_layer4_discount,1) n_layer4_discount,                    ");
        sb.append("           nvl(l.n_layer4_number,0) n_layer4_number,                        ");
        sb.append("           nvl(l.n_layer5_price,0) n_layer5_price,                          ");
        sb.append("           nvl(l.n_layer5_discount,1) n_layer5_discount,                    ");
        sb.append("           nvl(l.n_layer5_number,0) n_layer5_number,                        ");
        sb.append("           nvl(l.n_layer6_price,0) n_layer6_price,                          ");
        sb.append("           nvl(l.n_layer6_discount,1) n_layer6_discount,                    ");
        sb.append("           nvl(l.n_layer6_number,0) n_layer6_number,                        ");
        sb.append("           nvl(rl.apply_qty,0) apply_qty,                                   ");
        sb.append("           nvl(rl.apply_rebate,1) apply_rebate,                             ");
        sb.append("           nvl(rl.apply_price,0)  apply_price                               ");
        sb.append("   from crm_t_price_head h, crm_t_price_line l, crm_t_rebate_line rl        ");
        sb.append("   where l.c_price_head_id = h.c_pid                                        ");
        sb.append("   and l.c_pro_id = rl.product_id                                           ");
        sb.append("  and h.c_common_price = '1'                                                ");
        sb.append("   and rl.rebate_id = ?                                                     ");
        sb.append("   and h.c_organization = (select s.lov_code as code                        ");
        sb.append("   from sys_t_lov_member s                                                  ");
        sb.append("  where s.group_code = 'ORG'                                                ");
        sb.append("  and s.opt_txt3 = 'A'                                                      ");
        sb.append("   start with s.row_id = ?                                                  ");
        sb.append("   connect by prior s.parent_id = s.row_id                                  ");
        sb.append("   and rownum = 1)                                                          ");

        List<Map<String, Object>> mp = baseDao.findBySql4Map(sb.toString(), new Object[]{rebateId, orgId});

        return mp;
    }
    @Override
    public void startAuthProcess(UserObject userObject, String id, String bidNumber) {
    	Bid b = bizoppService.getBidEntity(id);
        String application = "BIZOPP_BID_AUTHORITY_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String calesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", calesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        Bid bid = getEntity(id, new Bid());
        CustomInfo customInfo = customInfoService.getCustomInfo(bid.getTerminalClient());
        vmap.put("CustomerClass", customInfo.getCustomClassName());
        //更新流程状态为已发起,记录流程ID
        //xflowProcessServiceWrapper.start(model, application, bidNumber + "_" + ProcessConstants.BIZOPP_BID_AUTHORITY_APPLY_PROC, id, userObject, vmap);
        xflowProcessServiceWrapper.start(
        		model, 
        		application, 
        		b.getPrjName()+"_"+b.getCreatedById()+"_"+b.getBidEnterprise()+"_"+b.getCreatedAt()+b.getTerminalClient(), 
        		id, 
        		userObject, 
        		vmap);
        processStatusService.updateProcessStatus("Bid", id, "status", ProcessConstants.PROCESS_STATUS_Processing);
    }


    public String getPriceLayCompareLine(String positionId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select lov.lov_code ");
        sb.append(" from crm_t_team t, CRM_T_PRICE_LAYER_COMLINE l, sys_t_lov_member lov ");
        sb.append(" where l.c_pid = t.business_id ");
        sb.append(" and lov.row_id = l.c_layer_line_name ");
        sb.append(" and lov.group_code = 'LAYER_COMP_NAME' ");
        sb.append(" and t.position_id = ? ");
        sb.append(" and rownum = 1 ");
        String lov_code = baseDao.findUniqueBySql(sb.toString(), positionId);

        return lov_code;
    }



    @Override
    public void startNewAuthProcess(UserObject userObject, String id, String bidNumber) {
    	Bid entity = this.getEntity(id, new Bid());
        String application = "BIZOPP_BID_AUTHORITY_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String calesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", calesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        Bid bid = getEntity(id, new Bid());
        String Type = "";
        //授权及承诺函（报备类）  授权及承诺函（非报备类）  日常业务类  售前类  售后类  报价类  财务类  物流类  特约经销商

        if("授权及承诺函（报备类）".equals(bid.getSltType())){
            Type = "1";
        } if("授权及承诺函（非报备类）".equals(bid.getSltType())){
            Type = "2";
        } if("售前类".equals(bid.getSltType())){
            Type = "3";
        } if("售后类".equals(bid.getSltType())){
            Type = "4";
        } if("报价类".equals(bid.getSltType())){
            Type = "5";
        } if("财务类".equals(bid.getSltType())){
            Type = "6";
        } if("物流类".equals(bid.getSltType())){
            Type = "7";
        } if("特约经销商".equals(bid.getSltType())){
            Type = "8";
        } if("日常业务类".equals(bid.getSltType())){
            Type = "9";
        }
        vmap.put("Type",Type);

        CustomInfo customInfo = customInfoService.getCustomInfo(bid.getTerminalClient());
        if (customInfo != null) {
            vmap.put("CustomerClass", customInfo.getCustomClassName());
        }

        //更新流程状态为已发起,记录流程ID
        //xflowProcessServiceWrapper.start(model, application, bidNumber + "_" + ProcessConstants.BIZOPP_BID_AUTHORITY_APPLY_PROC, id, userObject, vmap);
        xflowProcessServiceWrapper.start(
    		  model, 
    		  application, 
    		  entity.getPrjName()+"_"+entity.getCreatedByIdName()+"_"+entity.getCreatedOrgIdName()+"_"+entity.getCreatedAt()+"_"+entity.getTerminalClientName(),
    		  id, 
    		  userObject, 
    		  vmap);
        processStatusService.updateProcessStatus("Bid", id, "status", ProcessConstants.PROCESS_STATUS_Processing);
    }

    @Override
    public void startModelProcess(UserObject userObject, String id, String orderNumber) {
        String application = "BIZOPP_PROTO_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String calesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", calesCenter);
        //更新流程状态为已发起,记录流程ID
        xflowProcessServiceWrapper.start(model, application, orderNumber + "_" + ProcessConstants.BIZOPP_PROTO_APPLY_PROC, id, userObject, vmap);
        processStatusService.updateProcessStatus("PrototypeApplyReturn", id, "status", ProcessConstants.PROCESS_STATUS_Processing);
    }

    @Override
    public void saveSpecialPrice(SpecialPrice entity, UserObject userObject) {
        baseDao.save(entity);
        teamService.addPosition(
                userObject.getPosition().getId(),
                userObject.getEmployee().getId(),
                "SpecialPrice",
                entity.getId());
        orgTeamService.configPrimaryOrg(entity.getId(), "SpecialPrice", userObject.getOrg().getId());

    }

    @Override
    public void savePrototypeApplyReturn(PrototypeApplyReturn entity, UserObject userObject) {
        baseDao.save(entity);
        teamService.addPosition(
                userObject.getPosition().getId(),
                userObject.getEmployee().getId(),
                "PrototypeApplyReturn",
                entity.getId());
        orgTeamService.configPrimaryOrg(entity.getId(), "PrototypeApplyReturn", userObject.getOrg().getId());
//		startModelProcess(userObject,entity.getId(),entity.getNumber());
    }

    @Override
    public void saveBid(Bid entity, UserObject userObject) {
        entity.setCreatedById(userObject.getEmployee().getId());
        entity.setCreatedAt(new Date());
        entity.setCreatedPosId(userObject.getPosition().getId());
        entity.setCreatedOrgId(userObject.getOrg().getId());
        entity.setUpdatedById(userObject.getEmployee().getId());
        entity.setUpdatedAt(new Date());

        baseDao.save(entity);
//		startAuthProcess(userObject,entity.getId(),entity.getBidNumber());

        teamService.addPosition(
                userObject.getPosition().getId(),
                userObject.getEmployee().getId(),
                "bid",
                entity.getId());

        String bizOppId = entity.getBizOppId();
        if (StringUtils.isEmpty(bizOppId)) {
            return;
        }
        List<Integrator> integrators = baseDao.findEntity(" from Integrator where bizOppId = ? ", new String[]{bizOppId});

        for (Integrator integrator : integrators) {
            BidAuthUnit temp = new BidAuthUnit();
            temp.setBidId(entity.getId());
            temp.setBizOppId(bizOppId);
            temp.setFromId(integrator.getId());
            temp.setIntegrator(integrator.getIntegrator());

            // 创建字段
            temp.setCreatedById(userObject.getEmployee().getId());
            temp.setCreatedAt(new Date());
            temp.setCreatedPosId(userObject.getPosition().getId());
            temp.setCreatedOrgId(userObject.getOrg().getId());
            // 更新字段
            temp.setUpdatedById(userObject.getEmployee().getId());
            temp.setUpdatedAt(new Date());
            baseDao.save(temp);
        }

    }

    @Override
    public void changeStatus(String id, String status) {
        baseDao.executeHQL("update PrototypeApplyReturn set prototypeStatus = ? where id = ?", new Object[]{status, id});
    }

    @Override
    public IPage querySpecialPriceLine(PageCondition condition, String spId) {

        return baseDao.search("from SpecialPriceLine where spePriceId = ? ", new Object[]{spId}, condition.getRows(), condition.getPage());
    }

    @Override
    public SpecialPriceLine getSpecialPriceLine(String id) {
        return baseDao.findUniqueEntity("from SpecialPriceLine where id = ?", id);
    }

    @Override
    public void editSpecialPriceLine(SpecialPriceLine spl) {
        baseDao.update(spl);
    }

    @Override
    public void saveSpecialPriceLine(SpecialPriceLine specialPriceLine) {
        baseDao.save(specialPriceLine);
    }

    @Override
    public void updateSpecialPriceLine(SpecialPriceLine line) {
        baseDao.update(line);
    }

    @Override
    public Rebate getRebate(String id) {
        return baseDao.findUniqueEntity("from Rebate where id = ? ", id);
    }

    @Override
    public void saveRebate(Rebate entity, UserObject userObject) {
        baseDao.save(entity);

        teamService.addPosition( userObject.getPosition().getId(), userObject.getEmployee().getId(), "Rebate", entity.getId());
        orgTeamService.configPrimaryOrg(entity.getId(), "Rebate", userObject.getOrg().getId());

        if ("常规".equals(entity.getType())) {
            saveRival(entity);

            List<RebateLine> t1List_tmp = JSON.parseArray(entity.getT1List_str(), RebateLine.class);
            List<RebateLine> t1List = new ArrayList<>();
            for (RebateLine rebateLine : t1List_tmp) {
                rebateLine.setRebateId(entity.getId());
                rebateLine.setProductType("1");
                
                // 报备类产品必须关联商机
                validataReport(rebateLine,userObject);
                
                //splitRow(t1List, rebateLine);//满足条件拆分
                t1List.add(rebateLine);
            }

            /*List<RebateLine> t3List_tmp = JSON.parseArray(entity.getT3List_str(), RebateLine.class);
            List<RebateLine> t3List = new ArrayList<>();
            for (RebateLine rebateLine : t3List_tmp) {
                rebateLine.setRebateId(entity.getId());
                rebateLine.setProductType("3");
                
                // 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
                
                //splitRow(t3List, rebateLine);//满足条件拆分
                t3List.add(rebateLine);
            }*/

            baseDao.save(t1List);//标准
            //baseDao.save(t3List);//非标

        } else if ("电池".equals(entity.getType())) {
            saveRival(entity);
            List<RebateLine> t2List_tmp = JSON.parseArray(entity.getT2List_str(), RebateLine.class);
            List<RebateLine> t2List = new ArrayList<>();
            for (RebateLine rebateLine : t2List_tmp) {
                rebateLine.setRebateId(entity.getId());
                rebateLine.setProductType("2");
                
                // 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
                
               // splitRow(t2List, rebateLine);//满足条件拆分
                t2List.add(rebateLine);
            }

            baseDao.save(t2List);//电池

        } else if ("分销".equals(entity.getType())) {
            saveRival(entity);
            List<RebateLine> t5List_tmp = JSON.parseArray(entity.getT5List_str(), RebateLine.class);
            List<RebateLine> t5List = new ArrayList<>();
            for (RebateLine rebateLine : t5List_tmp) {
                rebateLine.setRebateId(entity.getId());
                rebateLine.setProductType("5");
                
                // 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
                
               // splitRow(t5List, rebateLine);//满足条件拆分
                t5List.add(rebateLine);
            }
            baseDao.save(t5List);//分销

        } else {
            List<RebateLine> t4List = JSON.parseArray(entity.getT4List_str(), RebateLine.class);
            for (RebateLine rebateLine : t4List) {
                //获取客户价格表价格
                if (rebateLine.getSourcePrice() == null || rebateLine.getSourcePrice() == 0d) {
                    Double price = priceHeadService.getCustomerProductPrice(entity.getBelongOperator(), rebateLine.getProductId());
                    if (price == null || price == 0d) {
                        throw new AnneException("终端客户价格为空！");
                    }
                    rebateLine.setSourcePrice(price);
                    rebateLine.setAmount(price * rebateLine.getApplyQty());
                }
                rebateLine.setRebateId(entity.getId());
                rebateLine.setProductType("4");
                
                // 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
                
                //splitRow(t4List, rebateLine);//满足条件拆分
            }
            baseDao.save(t4List);
        }
    }

    
	private void validataReport(RebateLine rebateLine, UserObject userObject) {

        String saleCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        //国内光伏代理商的情况：全部机型走特价都需要关联商机
        if (IConstants.CONTR_ORG_GNGFORG_B1_STR.equals(saleCenter)) {
            if (StringUtil.isNullOrEmpty(rebateLine.getBizId())) {
                throw new AnneException("国内光伏代理申请特价的产品需要关联商机," + rebateLine.getProductModel() + "没有关联商机。");
            }
        }

        if (productService.isReport(rebateLine.getProductModel())) {
			if (StringUtil.isNullOrEmpty(rebateLine.getBizId())) {
				throw new AnneException("报备类产品请选择关联的商机");
			}
		}

		if (!match(rebateLine.getBizId(), rebateLine.getProductModel())) {
			throw new AnneException("产品型号[" + rebateLine.getProductModel() + "]在选择的商机配置里不存在产品");
		}
	}
    
    
    public void saveRival(Rebate entity) {
        List<RebateRival> rivalList = JSON.parseArray(entity.getRivalList_str(), RebateRival.class);
        for (RebateRival rival : rivalList) {
            rival.setRebateId(entity.getId());
        }
        baseDao.save(rivalList);
    }

    public void splitRow(List<RebateLine> t1List, RebateLine rebateLine) {
        if (StringUtil.isNotEmpty(rebateLine.getIsBiz())) {
            if (rebateLine.getApplyQty() > rebateLine.getBizQty()) {
                RebateLine rl = new RebateLine();//新行数量设置为：大于商机配置数量的部分
                BeanUtils.copyProperties(rebateLine, rl);

                rebateLine.setApplyQty(rebateLine.getBizQty());// 原行数量设置为商机配置数量
                rebateLine.setAmount(rebateLine.getBizQty() * rebateLine.getApprovePrice());
                rebateLine.setApplyAmount(rebateLine.getBizQty() * rebateLine.getApplyPrice());

                rl.setApplyQty(rl.getApplyQty() - rl.getBizQty());
                rl.setAmount(rl.getApplyQty() * rl.getApprovePrice());
                rl.setApplyAmount(rl.getApplyQty() * rl.getApplyPrice());
                rl.setBizName("KSTAR-SJ-XN01 | 虚拟商机");
                rl.setBizId("KSTAR-SJ-XN01");
                rl.setIsBiz(rl.getIsBiz() + "-split");
                t1List.add(rl);

            }
        }
    }

    public boolean match(String bizId, String productModel) {
    	if (StringUtil.isNullOrEmpty(bizId)) {
			return true;
		}
        if (!"KSTAR-SJ-XN01".equals(bizId)) {
            List<ProductDetail> list = baseDao.findEntity("from ProductDetail where bizOppId = ? and productModel = ? ", new Object[]{bizId, productModel});
            if (list == null || list.size() == 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void updateRebate(Rebate entity, ProcessForm form, UserObject userObject,String newProcessType,boolean newProcessTypeUpdateFlag) {
        Rebate old = baseDao.get(Rebate.class, entity.getId());
        if (old == null) {
            throw new AnneException(this.getClass().getName() + " saveQuot : 没有找到要更新的数据");
        }
        if (!entity.getId().equals(old.getId())) {
            throw new AnneException("ID不能被修改");
        }
        BeanUtils.copyPropertiesIgnoreNull(entity, old);
        if(StringUtil.isNullOrEmpty(newProcessType)) {
        	 baseDao.update(old);
             saveOrUpdateRebateLine(entity, old, userObject);
        }else if(newProcessTypeUpdateFlag) {
        	baseDao.update(old);
            saveOrUpdateRebateLine(entity, old, userObject);
        }
        if(form == null){
			return ;
		}
		
		if(StringUtil.isEmpty(form.getTaskId())){
			return ;
		}
		
		Task t = taskService.getTask(form.getTaskId()); 
		if(t == null){
			throw new AnneException("任务已经不存在");
		}
		
		if(StringUtil.isEmpty(form.getComment())){
			throw new AnneException("意见建议不能为空");
		}
        demoService.todoProcess(form);
    }

    public void saveOrUpdaterival(Rebate entity) {
        List<RebateRival> rivalList = JSON.parseArray(entity.getRivalList_str(), RebateRival.class);
        for (RebateRival rebateRival : rivalList) {
            if (StringUtil.isEmpty(rebateRival.getId())) {
                rebateRival.setId(null);
                rebateRival.setRebateId(entity.getId());
                baseDao.save(rebateRival);
            } else {
                baseDao.update(rebateRival);
            }
        }
    }

    public void saveOrUpdate(Rebate entity, RebateLine rebateLine, String productType,UserObject userObject) {
        if (StringUtil.isEmpty(rebateLine.getId())) {
            rebateLine.setId(null);
            rebateLine.setRebateId(entity.getId());
            rebateLine.setProductType(productType);

            /*if (rebateLine.getBizQty() != null && rebateLine.getBizQty() != 0) {
                if (rebateLine.getApplyQty() > rebateLine.getBizQty()) {
                    RebateLine rl = new RebateLine();//新行数量设置为：大于商机配置数量的部分
                    BeanUtils.copyProperties(rebateLine, rl);

                    rebateLine.setApplyQty(rebateLine.getBizQty());// 原行数量设置为商机配置数量
                    rebateLine.setAmount(rebateLine.getBizQty() * rebateLine.getApprovePrice());
                    rebateLine.setApplyAmount(rebateLine.getBizQty() * rebateLine.getApplyPrice());

                    rl.setApplyQty(rl.getApplyQty() - rl.getBizQty());
                    rl.setAmount(rl.getApplyQty() * rl.getApprovePrice());
                    rl.setApplyAmount(rl.getApplyQty() * rl.getApplyPrice());
                    rl.setBizName("KSTAR-SJ-XN01 | 虚拟商机");
                    rl.setBizId("KSTAR-SJ-XN01");
                    rl.setIsBiz(rl.getIsBiz() + "-split");

                    baseDao.save(rl);
                }
            }*/
            baseDao.save(rebateLine);
        } else {
        	baseDao.save(rebateLine);
        }
    }

    /**
     * 获取特价申请行
     *
     * @return
     */
    @Override
    public List<RebateLine> getRebateLine(String headerId, String lineId, String productId) {
        List<Object> args = new ArrayList<>();
        StringBuilder hql = new StringBuilder();
        hql.append("from RebateLine where 1=1 ");
        if (StringUtil.isNotEmpty(headerId)) {
            hql.append(" and rebateId = ? ");
            args.add(headerId);
        }
        if (StringUtil.isNotEmpty(lineId)) {
            hql.append(" and id = ? ");
            args.add(lineId);
        }
        if (StringUtil.isNotEmpty(productId)) {
            hql.append(" and productId = ? ");
            args.add(productId);
        }

        return baseDao.findEntity(hql.toString(), args.toArray());
    }
    
    public RebateLine getRebateLine(String lineId){
    	return baseDao.get(RebateLine.class, lineId);
    }
    
    
    /**
     * 得到商机已转特价的数量
     * @return
     */
    @Override
    public Double getRebateLineApplyQtyDif(String isBizId){
    	
    	if (StringUtil.isNullOrEmpty(isBizId)) {
			return 0d;
		}
    	
    	Double amt = 0d;
    	
    	List<RebateLine> list = baseDao.findEntity("from RebateLine where isBiz = ? ",isBizId);
    	for (RebateLine rebateLine : list) {
			amt = MathUtils.add(amt, rebateLine.getApplyQty());
		}
    	
    	return amt;
    }
    
    
    
    /* ---------------- 特价变更 ---------------- */
    
    @Override
    public RebateChange getRebateChangeById(String id){
    	return baseDao.get(RebateChange.class, id);
    }
    
    /**
     * 根据特价ID获取最近的变更信息
     * @param rebateId
     * @return
     */
    @Override
    public RebateChange getRebateChangeByRebateId(String rebateId){
    	List<RebateChange> list = baseDao.findEntity("from RebateChange where rebateId = ? order by updatedAt desc",rebateId);
    	if (list.size() > 0) {
			return list.get(0);
		}
    	return null;
    }
    
    /**
     * 根据特价ID获取最近的变更列表
     * @param rebateId
     * @return
     */
    @Override
    public List<RebateChange> getRebateChanges(String rebateId){
    	ArrayList<Object> args = new ArrayList<>();
        args.add(rebateId);
        args.add(ProcessConstants.PROCESS_STATUS_Destroyed);
    	return baseDao.findEntity("from RebateChange where rebateId = ? and status != ? order by updatedAt desc",args.toArray());
    }
    
    
	@Override
	public void updateRebate(RebateChange entity, ProcessForm form, UserObject userObject,String saveOrUpdate,boolean newProcessTypeUpdateFlag) {
		
		if ("save".equals(saveOrUpdate)) {
			entity.setId(null);
			//获取订单变更记录总数
	        int count = this.countByChange(entity.getRebateId());
	        entity.setVersion(count+1);
	        
			baseDao.save(entity);
			//保存产品列表
			saveOrUpdateRebateLine(entity,saveOrUpdate,userObject);
			
			teamService.addPosition(userObject.getPosition().getId(), userObject.getEmployee().getId(), "Rebate", entity.getId());
			orgTeamService.configPrimaryOrg(entity.getId(), "Rebate", userObject.getOrg().getId());
		}else if(newProcessTypeUpdateFlag){
			baseDao.update(entity);
			saveOrUpdateRebateLine(entity,saveOrUpdate,userObject);
		}
		
		if (form != null && !StringUtil.isNullOrEmpty(form.getTaskId())) {
			demoService.todoProcess(form);
		}else{
			orderService.updateOrderLinePendingBySP(entity.getNo(), null, IConstants.YES_Yes, userObject);
			// 发起流程
			startApplyProcessRebateChange(userObject, entity.getId(), entity.getNo());
		}
	}
    
	
	private int countByChange(String rebateId) {
		String str = "from RebateChange t where ";
      			str += " t.rebateId = '"+rebateId+"' ";
      			str += " and t.status != '"+ProcessConstants.PROCESS_STATUS_Destroyed+"' ";
		List<RebateChange> list = baseDao.findEntity(str);
		return list.size();
	}

	private void saveRebateLine(RebateChange entity,String type,String list,String saveOrUpdate,UserObject userObject){
    	List<RebateLine> t5List_tmp = JSON.parseArray(list, RebateLine.class);
		List<RebateLine> t5List = new ArrayList<>();
		for (RebateLine rebateLine : t5List_tmp) {
			rebateLine.setRebateId(entity.getId());
			rebateLine.setProductType(type);
			
			// 报备类产品必须关联商机
			validataReport(rebateLine, userObject);

            //            splitRow(t5List, rebateLine);// 满足条件拆分
            if ("save".equals(saveOrUpdate)) {
                rebateLine.setOldId(rebateLine.getId());
                rebateLine.setId(null);
                baseDao.save(rebateLine);
            } else {
                if (StringUtil.isNullOrEmpty(rebateLine.getId())) {
                    baseDao.save(rebateLine);
                } else {
                    baseDao.update(rebateLine);
                }
            }
            validataQty(entity, rebateLine, userObject);

        }
    }
    
	/**
	 * 验证变更数量是否小于下单数量
	 * @param entity
	 * @param rebateLine
	 */
	private void validataQty(RebateChange entity,RebateLine rebateLine,UserObject userObject){
		if (!StringUtil.isNullOrEmpty(rebateLine.getOldId())) {
			OrderQuantityVo quantityVo = orderService.getRebateOrderQty(entity.getNo(), rebateLine.getOldId());
			if (rebateLine.getApplyQty() < quantityVo.getProQty()) {
				throw new AnneException("产品型号[ " + rebateLine.getProductModel() + " ] 变更数量不能小于订单下单数量 "+quantityVo.getProQty());
			}
		}
	}
	
	
	/**
	 * 特价变更开启工作流
	 * @param user
	 * @param id
	 * @param applyNumber
	 */
	private void startApplyProcessRebateChange(UserObject user, String id, String applyNumber) {
		String application = "BIZOPP_REBATE_PRICE_CHANGE_PROC";
		String model = lovMemberService.getFlowCodeByAppCode(application);
		Map<String, String> vmap = new HashMap<>();
		
		RebateChange change = baseDao.get(RebateChange.class, id);
		
//		Rebate rebate = this.getRebate(change.getRebateId());
		
        Employee employee =  ((Employee)CacheData.getInstance().get(change.getBusinessExecutive()));
        vmap.put("employeeIdInForm", employee.getId());
        vmap.put("employeeNameInForm", employee.getName());
        
//		DISTRIBUTE_SERIES	"是否分销系列产品：Y-分销系列  N-非分销系列
//		判断方法：特价申请单，配置明细中，产品型号为YDC/YDE开头的"
        String DISTRIBUTE_SERIES = "N";
        Double minApplyRebate = 100D;
        Double minApproveRebate = 100D;
        List<RebateLine> rebateLines = getRebateLine(change.getRebateId(), null, null);
        for (RebateLine rl : rebateLines) {
        	if(rl.getApplyRebate() != null){
        		if(rl.getApplyRebate() < minApplyRebate){
        			minApplyRebate = rl.getApplyRebate();
        		}
        	}
        	if(rl.getApproveRebate() != null){
        		// 电池没有批复折扣
        		if(rl.getApproveRebate() < minApproveRebate){
        			minApproveRebate = rl.getApproveRebate();
        		}
        	}
            if (rl.getProductModel() != null && (rl.getProductModel().startsWith("YDC") || rl.getProductModel().startsWith("YDE"))) {
                DISTRIBUTE_SERIES = "Y";
                //break;
            }
        }
        vmap.put("minApplyRebate", minApplyRebate.toString());
        vmap.put("minApproveRebate", minApproveRebate.toString());
        vmap.put("DISTRIBUTE_SERIES", DISTRIBUTE_SERIES);
		
		initVMap(vmap, user, id,change.getType());
		
		// 更新流程状态为已发起,记录流程ID
		xflowProcessServiceWrapper.start(model, application, applyNumber + "_" + ProcessConstants.BIZOPP_REBATE_PRICE_CHANGE_PROC, id, user, vmap);
		processStatusService.updateProcessStatus("RebateChange", id, "status", ProcessConstants.PROCESS_STATUS_Processing);
	}
    
    
    @Override
	public void updatePriceChangeFlowCallBack(String id){
		RebateChange change = baseDao.get(RebateChange.class, id);
		Rebate rebate = baseDao.get(Rebate.class, change.getRebateId());
		
        rebate.setVersion(change.getVersion());
        
		rebate.setName(change.getName());
		rebate.setAgentContact(change.getAgentContact());
		rebate.setPhone(change.getPhone());
		rebate.setBusinessExecutive(change.getBusinessExecutive());
		baseDao.update(rebate);
		
		// 变更前的数据
		List<RebateLine> oldList = getRebateLine(rebate.getId(), null, null);
		Map<String, RebateLine> oldMap = new HashMap<>();
		for (RebateLine rebateLine : oldList) {
			oldMap.put(rebateLine.getId(), rebateLine);
		}
		
		// 变更后的数据
		List<RebateLine> changeList = getRebateLine(change.getId(), null, null);
		Map<String, RebateLine> changeMap = new HashMap<>();
		for (RebateLine rebateLine : changeList) {
			if (StringUtil.isNullOrEmpty(rebateLine.getOldId())) {
				// 新增
				RebateLine oldline = new RebateLine();
				BeanUtils.copyProperties(rebateLine, oldline);
				oldline.setRebateId(rebate.getId());
				oldline.setOldId(null);
				oldline.setId(null);
				baseDao.save(oldline);
				continue;
			}
			changeMap.put(rebateLine.getOldId(), rebateLine);
		}
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserObject userObject = (UserObject) request.getSession().getAttribute("LOGIN_USER");
        orderService.updateOrderLinePendingBySP(rebate.getNo(), null, IConstants.NO_No, userObject);
        
		// 删除，修改
		for (String key : oldMap.keySet()) {
			if (changeMap.get(key) == null) {
				baseDao.delete(oldMap.get(key));
			}else{
				RebateLine oldline = oldMap.get(key);
				RebateLine line = changeMap.get(key);
				oldline.setApplyQty(line.getApplyQty());
				oldline.setSourcePrice(line.getSourcePrice());
				oldline.setApplyRebate(line.getApplyRebate());
				oldline.setApproveRebate(line.getApproveRebate());
				oldline.setApprovePrice(line.getApprovePrice());
				oldline.setApplyPrice(line.getApplyPrice());
				oldline.setAmount(line.getAmount());
				oldline.setBizId(line.getBizId());
				oldline.setBizName(line.getBizName());
				oldline.setCatalogPrice(line.getCatalogPrice());
				baseDao.update(oldline);
			}
		}
		
	}

    /**
     * 特价查询列表
     */
	@Override
	public PageImpl queryByBizName(PageCondition condition, Class t, String bizName,String bizId) throws Exception {
		IPage p = query(condition, t);
		List<Rebate> RebateList = (List<com.ibm.kstar.entity.bizopp.Rebate>) p.getList();
		List<Rebate> RebateListAfter = new ArrayList();
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" b ");
		sb.append(" from ");
		sb.append(" BusinessOpportunity b ");
		if(!StringUtil.isNullOrEmpty(bizId)) {
			sb.append(" where b.number like  ? ");
			args.add("%"+bizId+"%");
		}else if(!StringUtil.isNullOrEmpty(bizName)) {
			sb.append(" where b.opportunityName like  ? ");
			args.add("%"+bizName+"%");
		}
		
		List<BusinessOpportunity> boList = baseDao.findEntity(sb.toString(),args.toArray());
		
		StringBuffer rbl = new StringBuffer();
		rbl.append(" select ");
		rbl.append(" b ");
		rbl.append(" from ");
		rbl.append(" RebateLine b ");
		rbl.append(" where b.rebateId = ? ");
		if(boList.size()>0) {
			for(BusinessOpportunity bo:boList) {
				String boId = bo.getId();
				for(Rebate rebate:RebateList) {
					args.set(0, rebate.getId());
					List<RebateLine> rebateLineList = baseDao.findEntity(rbl.toString(),args.toArray());
					if(rebateLineList.size()>0) {
						for(RebateLine rebateLine:rebateLineList) {
							String rebateLineBizId = rebateLine.getBizId();
							if(boId.equals(rebateLineBizId)) {
								RebateListAfter.add(rebate);
								break;
							}
						}
					}
				}
			}	
		}
		int rows = condition.getRows();
		int count = 1;
		if(RebateListAfter.size()%rows>1) {
			count=RebateListAfter.size()%rows;
		}
		PageImpl page = new PageImpl(RebateListAfter,condition.getPage(),condition.getRows(),count); 
		return page;
	}
	
	 /**
     * 特价查询列表
     */
	@Override
	public PageImpl queryRebateChangeByBizName(PageCondition condition, Class t, String bizName,String bizId) throws Exception {
		IPage p = query(condition, t);
		List<RebateChange> RebateChangeList = (List<com.ibm.kstar.entity.bizopp.RebateChange>) p.getList();
		List<RebateChange> RebateListAfter = new ArrayList();
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" b ");
		sb.append(" from ");
		sb.append(" BusinessOpportunity b ");
		if(!StringUtil.isNullOrEmpty(bizId)) {
			sb.append(" where b.number like  ? ");
			args.add("%"+bizId+"%");
		}else if(!StringUtil.isNullOrEmpty(bizName)) {
			sb.append(" where b.opportunityName like  ? ");
			args.add("%"+bizName+"%");
		}
		
		List<BusinessOpportunity> boList = baseDao.findEntity(sb.toString(),args.toArray());
		
		StringBuffer rbl = new StringBuffer();
		rbl.append(" select ");
		rbl.append(" b ");
		rbl.append(" from ");
		rbl.append(" RebateLine b ");
		rbl.append(" where b.rebateId = ? ");
		if(boList.size()>0) {
			for(BusinessOpportunity bo:boList) {
				String boId = bo.getId();
				for(RebateChange rebate:RebateChangeList) {
					args.set(0, rebate.getId());
					List<RebateLine> rebateLineList = baseDao.findEntity(rbl.toString(),args.toArray());
					if(rebateLineList.size()>0) {
						for(RebateLine rebateLine:rebateLineList) {
							String rebateLineBizId = rebateLine.getBizId();
							if(boId.equals(rebateLineBizId)) {
								RebateListAfter.add(rebate);
								break;
							}
						}
					}
				}
			}	
		}
		int rows = condition.getRows();
		int count = 1;
		if(RebateListAfter.size()%rows>1) {
			count=RebateListAfter.size()%rows;
		}
		PageImpl page = new PageImpl(RebateListAfter,condition.getPage(),condition.getRows(),count); 
		return page;
	}
	
	@Override
	public void saveCopyRebatChange(Rebate entity){
		RebateChange rbChange = new RebateChange();
		BeanUtils.copyPropertiesIgnoreNull(entity, rbChange);
		rbChange.setId(null);
		rbChange.setRebateId(entity.getId());
		rbChange.setStatus(ProcessConstants.PROCESS_STATUS_Completed);
		rbChange.setStartDate(new Date());
		rbChange.setEndDate(DateUtil.getDateAfter(new Date(),60));
		baseDao.save(rbChange);
		
		List<RebateLine> oldList = getRebateLine(entity.getId(), null, null);
		for(RebateLine oldline:oldList){
			RebateLine newline = new RebateLine();
			BeanUtils.copyPropertiesIgnoreNull(oldline, newline);
			newline.setRebateId(rbChange.getId());
			newline.setOldId(null);
			newline.setId(null);
			baseDao.save(newline);
		}
	}
    
	private void saveOrUpdateRebateLine(RebateChange entity,String saveOrUpdate,UserObject userObject) {
		if ("常规".equals(entity.getType())) {

			saveRebateLine(entity, "1", entity.getT1List_str(),saveOrUpdate,userObject);// 标准
			//saveRebateLine(entity, "3", entity.getT3List_str(),saveOrUpdate,userObject);// 非标

		} else if ("电池".equals(entity.getType())) {
			
			saveRebateLine(entity, "2", entity.getT2List_str(),saveOrUpdate,userObject);// 电池
			
		} else if ("分销".equals(entity.getType())) {
			
			saveRebateLine(entity, "5", entity.getT5List_str(),saveOrUpdate,userObject);// 分销
			
		} else {
			List<RebateLine> t4List = JSON.parseArray(entity.getT4List_str(), RebateLine.class);
			for (RebateLine rebateLine : t4List) {
				// 获取客户价格表价格
				if (rebateLine.getSourcePrice() == null || rebateLine.getSourcePrice() == 0d) {
					Double price = priceHeadService.getCustomerProductPrice(entity.getBelongOperator(), rebateLine.getProductId());
					if (price == null || price == 0d) {
						throw new AnneException("终端客户价格为空！");
					}
					rebateLine.setSourcePrice(price);
					rebateLine.setAmount(price * rebateLine.getApplyQty());
				}
				
				rebateLine.setRebateId(entity.getId());
				rebateLine.setProductType("4");

				// 报备类产品必须关联商机
				validataReport(rebateLine, userObject);

                //				splitRow(t4List, rebateLine);// 满足条件拆分
                if ("save".equals(saveOrUpdate)) {
					rebateLine.setOldId(rebateLine.getId());
					rebateLine.setId(null);
					baseDao.save(rebateLine);
				}else{
					if (StringUtil.isNullOrEmpty(rebateLine.getId())) {
						baseDao.save(rebateLine);
					}else{
						baseDao.update(rebateLine);
					}
				}
				validataQty(entity, rebateLine,userObject);
			}
		}
	}
	
	private void saveOrUpdateRebateLine(Rebate entity,Rebate old,UserObject userObject) {
		if ("常规".equals(old.getType())) {
            saveOrUpdaterival(entity);
            List<RebateLine> t1List = JSON.parseArray(entity.getT1List_str(), RebateLine.class);
            for (RebateLine rebateLine : t1List) {
            	// 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
            }
            for (RebateLine rebateLine : t1List) {
            	if(!StringUtil.isNullOrEmpty(rebateLine.getId())) {
            		RebateLine  oldRebateLine  = baseDao.get(RebateLine.class, rebateLine.getId());
            		baseDao.delete(oldRebateLine);
            	}
                saveOrUpdate(entity, rebateLine, "1",userObject);
            }
           /* List<RebateLine> t3List = JSON.parseArray(entity.getT3List_str(), RebateLine.class);
            for (RebateLine rebateLine : t3List) {
                saveOrUpdate(entity, rebateLine, "3",userObject);
            }*/

        } else if ("电池".equals(old.getType())) {
            saveOrUpdaterival(entity);
            List<RebateLine> t2List = JSON.parseArray(entity.getT2List_str(), RebateLine.class);
            for (RebateLine rebateLine : t2List) {
            	// 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
            }
            for (RebateLine rebateLine : t2List) {
            	if(!StringUtil.isNullOrEmpty(rebateLine.getId())) {
            		RebateLine  oldRebateLine  = baseDao.get(RebateLine.class, rebateLine.getId());
            		baseDao.delete(oldRebateLine);
            	}
                saveOrUpdate(entity, rebateLine, "2",userObject);
            }
        } else if ("分销".equals(old.getType())) {
            saveOrUpdaterival(entity);
            List<RebateLine> t5List = JSON.parseArray(entity.getT5List_str(), RebateLine.class);
            for (RebateLine rebateLine : t5List) {
            	// 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
            }
            for (RebateLine rebateLine : t5List) {
            	if(!StringUtil.isNullOrEmpty(rebateLine.getId())) {
            		RebateLine  oldRebateLine  = baseDao.get(RebateLine.class, rebateLine.getId());
            		baseDao.delete(oldRebateLine);
            	}
                saveOrUpdate(entity, rebateLine, "5",userObject);
            }
        } else {
            List<RebateLine> t4List = JSON.parseArray(entity.getT4List_str(), RebateLine.class);
            for (RebateLine rebateLine : t4List) {
            	// 报备类产品必须关联商机
                validataReport(rebateLine, userObject);
            }
            for (RebateLine rebateLine : t4List) {
            	if(!StringUtil.isNullOrEmpty(rebateLine.getId())) {
            		RebateLine  oldRebateLine  = baseDao.get(RebateLine.class, rebateLine.getId());
            		baseDao.delete(oldRebateLine);
            	}
                saveOrUpdate(entity, rebateLine, "4",userObject);
            }
        }
	}
	
	@Override
    public void deleteRebateChange(String RebateChangeId, UserObject userObject) {
        RebateChange rebateChange = baseDao.get(RebateChange.class, RebateChangeId);

        String hql = " from RebateLine line where line.rebateId = ?";
        List<RebateLine> lines = baseDao.findEntity(hql, new Object[]{RebateChangeId});
        if (lines != null) {
            for (RebateLine line : lines) {
                //删除订单行
                baseDao.delete(line);
            }
        }
        if (rebateChange.getStatus().equals("Rejected")) {
        	String application = "BIZOPP_REBATE_PRICE_CHANGE_PROC";
    		String model = lovMemberService.getFlowCodeByAppCode(application);
            processService.closeByBusinessKey(model, rebateChange.getId(), userObject.getParticipant(), "特价变更删除");
        }

        //删除订单头
        baseDao.delete(rebateChange);

    }
	
	/**
	 * 点击销毁按钮，同时删除对应特价变更单据
	 */
	@Override
    public void destoryRebateChange(String entityName, String businessId,
                                    String processStatusColumn, Object processStatusValue) {
        /*RebateChange rebateChange = baseDao.get(RebateChange.class, RebateChangeId);

        String hql = " from RebateLine line where line.rebateId = ?";
        List<RebateLine> lines = baseDao.findEntity(hql, new Object[]{RebateChangeId});
        if (lines != null) {
            for (RebateLine line : lines) {
                //删除订单行
                baseDao.delete(line);
            }
        }
        //删除订单头
        baseDao.delete(rebateChange);*/
        String hql = "update " + entityName + " set " + processStatusColumn + " = ?  where id = ? ";
        ArrayList<Object> args = new ArrayList<>();
        args.add(processStatusValue);
        args.add(businessId);

        baseDao.executeHQL(hql,args.toArray());

    }

    @Override
    public Rebate getRebateByNo(String rebateNo) {
        //language=HQL
        List<Rebate> entitys = this.baseDao.findEntity("from Rebate where no=?", new Object[]{rebateNo});
        if (entitys.size() > 0) {
            if (entitys.size() > 1) {
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error("找到单据编号为{}的重复特价单", rebateNo);
            }
            return entitys.get(0);
        }
        return null;
    }
}
