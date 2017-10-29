package com.ibm.kstar.impl.system.permission;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.system.permission.user.UserVo;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.api.system.permission.entity.UserPermission;
import com.ibm.kstar.api.system.permission.entity.VerificationCode;
import com.ibm.kstar.api.system.permission.vo.UserEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.MD5Util;
import org.xsnake.web.utils.StringUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    BaseDao baseDao;

    @Autowired
    ICorePermissionService corePermissionService;

    static final String DEFAULT_PASSWORD = "123456";

    private static String getRandomPassword() {
        String words = "1234567890";
        int passwordLenght = 6;
        String password = "";
        Random r = new Random();
        for (int i = 0; i < passwordLenght; i++) {
            int p = r.nextInt(words.length());
            password = password + words.substring(p, p + 1);
        }
        return password;
    }

    @Override
    public void save(User user) throws AnneException {
        UserVo userVo = (UserVo) user;
        userVo.setStartDate(new Date());
        User testUser = baseDao.get(User.class, userVo.getEmployeeId());
        if (testUser != null) {
            throw new AnneException("该员工已经拥有登录账号!");
        }
        userVo.setId(userVo.getEmployeeId());
        String[] roles = userVo.getRoles().split(",");
        for (String role : roles) {
            UserPermission up = new UserPermission();
            up.setMemberId(role);
            up.setUserId(userVo.getEmployeeId());
            up.setType("R");
            baseDao.save(up);
        }
        User u = new User();
        BeanUtils.copyPropertiesIgnoreNull(userVo, u);
        String password = getRandomPassword();
        u.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        baseDao.save(u);
        Employee employee = baseDao.get(Employee.class, u.getId());
        employee.setUserFlag("Y");
        baseDao.update(employee);
        sendPasswordNotice(employee.getEmail(), password);
    }

    static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    private void sendPasswordNotice(final String email, final String password) {
        sendMail(email, "新CRM系统用户初始密码", "新CRM系统已正式上线，访问地址：http://crm.kstar.com:8888\n您可以使用工号或者邮箱登录。初始密码为：" + password + "请妥善保管您的密码。\n建议使用Google浏览器下载地址 http://sw.bos.baidu.com/sw-search-sp/software/7164c4c6bc6e0/ChromeStandalone_58.0.3029.110_Setup.exe");
    }

    public void sendMail(final String email, final String title, final String content) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Transport transport = null;
                try {
                    System.out.println("--------------------sendMain------------------------start");
                    System.out.println("mail:" + email + "  title:" + title);
                    Properties properties = new Properties();
                    properties.setProperty("mail.smtp.auth", "true");
                    properties.setProperty("mail.transport.protocol", "smtp");
                    Session session = Session.getInstance(properties);
                    Message message = new MimeMessage(session);
                    message.setSubject(title);
                    message.setFrom(new InternetAddress("kstarcrm@kstar.com.cn"));
                    message.setText(content);
                    transport = session.getTransport();
                    transport.connect(IConstants.MAIL_SERVICE_ADDR, 25, "kstarcrm@kstar.com.cn", "kstar-5");
                    transport.sendMessage(message, new Address[]{new InternetAddress(email)});
                    System.out.println("--------------------sendMain------------------------end");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (transport != null) {
                        try {
                            transport.close();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        fixedThreadPool.submit(thread);
    }

    @Override
    public void findPassword(String email) {
        User user = baseDao.findUniqueEntity(" select u from Employee e, User u where e.id = u.id and e.email = ?  ", new Object[]{email});
        if (user == null) throw new AnneException("无效的邮箱");
        //        User user = baseDao.get(User.class, employee.getId());
        VerificationCode code = new VerificationCode();
        String verificationCode = getRandomPassword();
        code.setUserId(user.getId());
        code.setEmail(email);
        code.setVerificationCode(verificationCode);
        code.setCreateDt(new Date());
        this.baseDao.save(code);

        sendFindPasswordEmail(code);
    }

    @Override
    public VerificationCode getLastVerificationCode(String email, String verificationCode) {
        //language=HQL
        List<VerificationCode> codes = this.baseDao.findEntity("FROM VerificationCode WHERE email=? and verificationCode=?  and createDt > (current_timestamp - numtodsinterval(5,'minute')) order by createDt desc ", new Object[]{email, verificationCode});
        if (codes.size() >= 1) {
            return codes.get(0);
        }
        return null;
    }

    private void sendFindPasswordEmail(VerificationCode code) {
        //        String service = "http://crm.kstar.com:8888";
        String service = "http://crm.kstar.com:8888";
        sendMail(code.getEmail(), "科士达CRM系统密码找回", "你申请了密码找回,请在5分钟内  访问地址：" + service + "/resetPassword.html?email=" + code.getEmail() + "&verificationCode=" + code.getVerificationCode() + " 进行后续操作。");
    }

    @Override
    public void update(User user) throws AnneException {
        User oldUser = baseDao.get(User.class, user.getId());
        BeanUtils.copyPropertiesIgnoreNull(user, oldUser);
        baseDao.update(oldUser);
        UserVo userVo = ((UserVo) user);
        baseDao.executeHQL(" delete from UserPermission up where up.userId = ? and up.type = 'R' ", new Object[]{oldUser.getId()});
        String[] roles = userVo.getRoles().split(",");
        for (String role : roles) {
            UserPermission up = new UserPermission();
            up.setMemberId(role);
            up.setUserId(oldUser.getId());
            up.setType("R");
            baseDao.save(up);
        }
    }

    public void changePosition(String positionId) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public IPage query(PageCondition condition) throws AnneException {
        String searchKey = condition.getStringCondition("searchKey");
        String positionId = condition.getStringCondition("positionId");
        String orgId = condition.getStringCondition("orgId");
        StringBuffer hql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        hql.append("select distinct u,e from Employee e , User u  where u.id = e.id ");
        if (StringUtil.isNotEmpty(searchKey)) {
            hql.append(" and ( e.name like ? or e.no like ? or e.email like ? )");
            args.add("%" + searchKey + "%");
            args.add("%" + searchKey + "%");
            args.add("%" + searchKey + "%");
        }

        if (StringUtil.isNotEmpty(positionId)) {
            LovMember positon = corePermissionService.getPositionByOrgId(positionId);
            hql.append(" and e.id in (select up.userId from UserPermission up where up.type = 'P' and up.memberId = ? ) ");
            args.add(positon.getId());
        }

        if (StringUtil.isNotEmpty(orgId)) {
            hql.append(" and e.id in (select up.userId from UserPermission up , LovMember p , LovMember o where up.type = 'P' and up.memberId = p.id and p.optTxt1 = o.id and o.path like ? ) ");
            args.add("%" + orgId + "%");
        }

        IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        List<Object[]> list = (List<Object[]>) page.getList();
        List<UserEmployee> l = BeanUtils.castList(UserEmployee.class, list);
        ((PageImpl) page).setList(l);
        return page;
    }

    @Override
    public void delete(String userId) throws AnneException {
        baseDao.deleteById(User.class, userId);
    }

    @Override
    public User get(String userId) throws AnneException {
        return baseDao.get(User.class, userId);
    }

    @Override
    public void changePosition(String userId, String positionId) {
        User user = get(userId);
        user.setLastPositionId(positionId);
        baseDao.update(user);

    }

    @Override
    public void modifyPassword(User user) throws AnneException {
        User oldUser = baseDao.get(User.class, user.getId());
        if (oldUser == null) {
            throw new AnneException("改用户不存在");
        }
        BeanUtils.copyPropertiesIgnoreNull(user, oldUser);
        baseDao.update(oldUser);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IPage queryWithPosition(PageCondition condition) throws AnneException {
        String searchKey = condition.getStringCondition("searchKey");
        String positionId = condition.getStringCondition("positionId");
        StringBuffer hql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        hql.append("select distinct u,e from Employee e , User u , LovMember p , UserPermission up where u.id = e.id and u.startDate < ? and u.endDate > ? and p.groupId = 'POSITION' and up.type = 'P' and up.memberId = p.id and up.userId = u.id ");
        args.add(new Date());
        args.add(new Date());
        if (StringUtil.isNotEmpty(searchKey)) {
            hql.append(" and ( e.name like ? or e.no like ? )");
            args.add("%" + searchKey + "%");
            args.add("%" + searchKey + "%");
        }
        if (StringUtil.isNotEmpty(positionId)) {
            hql.append(" and p.id = ? ");
            args.add(positionId);
        }
        IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        List<Object[]> list = (List<Object[]>) page.getList();
        List<UserEmployee> l = BeanUtils.castList(UserEmployee.class, list);
        ((PageImpl) page).setList(l);
        return page;
    }

    @Override
    public void resetPassword(String userId, String password) throws AnneException {
        User user = baseDao.findUniqueEntity(" select u from User u where u.id=?", new Object[]{userId});
        if (user == null) throw new AnneException("无效的用户");

        user.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        baseDao.update(user);
    }

    public void resetPasswordById(String id) throws AnneException {
        Employee employee = baseDao.findUniqueEntity(" select e from Employee e, User u where e.id = u.id and e.id = ?  ", new Object[]{id});
        if (employee == null) {
            throw new AnneException("无效的邮箱");
        }
        User user = baseDao.get(User.class, employee.getId());
        String password = DEFAULT_PASSWORD;//getRandomPassword();
        user.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        baseDao.update(user);
        sendPasswordNotice(employee.getEmail(), password);
    }


}
