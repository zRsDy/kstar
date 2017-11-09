package com.ibm.kstar.impl.order;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IContractReceiptDetailService;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.order.IReceiptsService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.*;
import com.ibm.kstar.entity.order.vo.ReceiptsListVO;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.team.Team;
import com.ibm.kstar.permission.utils.PermissionUtil;

import org.apache.commons.math.util.MathUtils;
import org.datanucleus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ReceiptsServiceImpl implements IReceiptsService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    ILovMemberService lovMemberService;
    @Autowired
    ICustomInfoService customInfoService;
    @Autowired
    protected ITeamService teamService;
    @Autowired
    IOrderService orderService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    IContractService contractService;
    @Autowired
    IDeliveryService deliveryService;
    @Autowired
    ICorePermissionService corePermissionService;
    @Autowired
    private IKstarAttachmentService attachmentService;
    @Autowired
	IContractReceiptDetailService contractReceiptDetailService;

    @Override
    public void saveReceipts(Receipts receipts, UserObject userObject) throws AnneException {
        // 创建字段
        receipts.setCreatedById(userObject.getEmployee().getId());
        receipts.setCreatedAt(new Date());
        receipts.setCreatedPosId(userObject.getPosition().getId());
        receipts.setCreatedOrgId(userObject.getOrg().getId());
        // 更新字段
        receipts.setUpdatedById(userObject.getEmployee().getId());
        receipts.setUpdatedAt(new Date());
        baseDao.save(receipts);

//        		teamService.addPosition(receipts.getSalesmanPost(),receipts.getSalesmanId(),
//        				IConstants.PERMISSION_BUSINESS_TYPE_RECEIPTS,receipts.getId());
    }
    
    private void validateReceiptAmount(Receipts oldReceipts) throws AnneException {
    	if(StringUtils.isEmpty(oldReceipts.getContractCode())){
    		return;
    	}
    	
    	Contract contract = contractService.getContractByNoForContrVer(oldReceipts.getContractCode());
        if (contract == null) {
            return;
        }
        
        BigDecimal sum = new BigDecimal(0);
        String hql = " select sum(vd.n_veri_amount) from crm_t_verification_detail vd where vd.c_contract_id=? and vd.c_receipts_id!=?";
        List<Object[]> objs = baseDao.findBySql(hql, new Object[]{contract.getId(),oldReceipts.getId()});
        if(objs.size()>0&&objs.get(0)!=null){
        	sum = (BigDecimal)((Object)objs.get(0));
        }
        sum = oldReceipts.getAmount().add(sum);
        if(sum.compareTo(new BigDecimal(contract.getTotalAmt()))==1){
        	throw new AnneException("收款单可核销金额不能大于合同未核销金额！");
        }
    }

    @Override
    public void updateReceipts(Receipts receipts, UserObject userObject) throws AnneException {
        Receipts oldReceipts = baseDao.get(Receipts.class, receipts.getId());
        if (oldReceipts == null) {
            throw new AnneException(IReceiptsService.class.getName()
                    + " saveReceipts : 没有找到需要更新的数据");
        }
        if("No".equals(oldReceipts.getErpCust())
        		&&StringUtils.isEmpty(oldReceipts.getCorrectCustName())){
        	receipts.setChangeCustDate(new Date());
        	receipts.setChangeCustMember(userObject.getEmployeeName());
        	receipts.setChangeCustMemberId(userObject.getEmployee().getId());
        }
        BeanUtils.copyPropertiesIgnoreNull(receipts, oldReceipts);

        /* 徐建2017/11/06注释该部分代码
        if (StringUtil.isNotEmpty(oldReceipts.getContractCode())) {
            Contract contract = contractService.getContractByNoForContrVer(oldReceipts.getContractCode());
            if (contract != null) {
                String hql = " from KstarPrjLst k where k.quotCode = ? ";
                List<KstarPrjLst> kstarPrjLsts = baseDao.findEntity(hql, new Object[]{contract.getId()});
                if (kstarPrjLsts == null || kstarPrjLsts.size() <= 0) {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " updateReceipts : 合同可发货金额小于收款金额，不允许按合同自动核销！");
                } else {
                    BigDecimal amount = new BigDecimal(0);
                    for (KstarPrjLst prjLst : kstarPrjLsts) {
                        if (prjLst != null) {
                            BigDecimal notVeriNum = prjLst.getNotVeriNum() == null ? new BigDecimal(0) : new BigDecimal(prjLst.getNotVeriNum());
                            BigDecimal prdPrc = prjLst.getPrdPrc() == null ? new BigDecimal(0) : new BigDecimal(prjLst.getPrdPrc());
                            amount = amount.add(notVeriNum.multiply(prdPrc));
                        }
                    }
                    if (amount.compareTo(oldReceipts.getAmount()) == -1) {
                        throw new AnneException(IReceiptsService.class.getName()
                                + " updateReceipts : 合同可发货金额小于收款金额，不允许按合同自动核销！");
                    }
                }
            }
        }*/
        
        /* 徐建2017/11/06 增加新的校验规则:收款记录转预收款需判断：收款金额+合同已核销金额<合同金额  */
        validateReceiptAmount(oldReceipts);
		if (oldReceipts.getStatus().equals((IConstants.RECEIPT_STATUS_20))
				|| oldReceipts.getStatus().equals((IConstants.RECEIPT_STATUS_30))) {
			if (receipts.getAmount().compareTo(new BigDecimal(0)) == 0) {
				oldReceipts.setStatus(IConstants.RECEIPT_STATUS_40);
			}
		}
        
        oldReceipts.setUpdatedAt(new Date());
        oldReceipts.setUpdatedById(userObject.getEmployee().getId());
        baseDao.update(oldReceipts);
    }

    @Override
    public IPage queryReceipts(PageCondition condition,UserObject userObject) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(Receipts.class);
        filterObject.addOrderBy("receiptsDate", "asc");
        String type = condition.getStringCondition("type");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        String hql = hqlObject.getHql();

        //收款核销按岗位上下级进行权限控制
        String op = condition.getStringCondition("op");
        if ("verification".equals(op)) {
            String as = Receipts.class.getSimpleName().toLowerCase();
            String permissionHQL = " and " + as + ".salesmanId in (";
            //language=HQL
            permissionHQL += " select u.id from LovMember o,LovMember p,User u,UserPermission up " +
                    "where u.id = up.userId and up.type = 'P' and up.memberId=p.id and p.optTxt1=o.id and u.id="+as+".salesmanId";
            permissionHQL += " and " + PermissionUtil.getPermissionHQL(null, "u.id", "p.id", "o.id", as + ".id", userObject, "ReceiptsList");
            permissionHQL += ")";
            int index = hql.indexOf(" order by ");
            if (index >= 0) {
                StringBuilder s = new StringBuilder();
                s.append(hql.substring(0, index))
                        .append(permissionHQL)
                        .append(" order by ")
                        .append(hql.substring(index + 10, hql.length()));
                hql = s.toString();
            } else {
                hql = hql + permissionHQL;
            }
        }

        //是否只显示新建和退回状态的数据
        String onlyNewAndSendback = condition.getStringCondition("onlyNewAndSendback");
        if (Objects.equals(onlyNewAndSendback, "1")) {
            int index = hql.indexOf(" order by ");
            if (index >= 0) {
                StringBuilder s = new StringBuilder();
                s.append(hql.substring(0, index))
                        .append(" and (status=? or status=?) ")
                        .append(" order by ")
                        .append(hql.substring(index + 10, hql.length()));
                hql = s.toString();
            } else {
                hql = hql + " and (status=? or status=?) ";
            }

            hqlObject.addArgs(IConstants.RECEIPT_STATUS_10);
            hqlObject.addArgs(IConstants.RECEIPT_STATUS_R10);
        }

        //是否只显示更新用户名称为空的数据
        String isHaveErpCust = condition.getStringCondition("isHaveErpCust");
        if (Objects.equals(isHaveErpCust, "1")) {
            int index = hql.indexOf(" where 1=1 ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where 1=1 ")
                    .append(" and correctCustName is null ")
                    .append(hql.substring(index + 11, hql.length()));
            hql = s.toString();
        }
        
        //是否只显示更正用户名称是销售人员更正得数据
        String isHaveChange = condition.getStringCondition("isHaveChange");
        if (Objects.equals(isHaveChange, "1")) {
            int index = hql.indexOf(" where 1=1 ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where 1=1 ")
                    .append(" and changeCustMember != null ")
                    .append(hql.substring(index + 11, hql.length()));
            hql = s.toString();
        }
        if (Objects.equals(type, "RECEVICE")) {
            int index = hql.indexOf(" where ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where ")
                    .append(" salesmanId is null and ")
                    .append(" correctCustId is null and ")
                    .append(" erpCust = 'No' and ")
                    .append(" status = '20' and ")
                    .append(hql.substring(index + 7, hql.length()));
            hql = s.toString();
        }

        return baseDao.search(hql, hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public void deleteReceipts(String receiptsId) throws AnneException {

        String[] ids = receiptsId.split(",");
        for (String id : ids) {
            Receipts receipts = baseDao.get(Receipts.class, id);
            if (receipts == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " deleteReceipts : 没有找到该收款单，请刷新列表");
            }
            double veriAmount = receipts.getVeriAmount() == null ? 0 : receipts.getVeriAmount().doubleValue();
            if (IConstants.RECEIPT_STATUS_30.equals(receipts.getStatus())
                    || IConstants.RECEIPT_STATUS_40.equals(receipts.getStatus())
                    || veriAmount > 0
                    || IConstants.YES_Yes.equals(receipts.getErpImp())) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " deleteReceipts : 收款单状态不允许删除");
            }
            baseDao.delete(receipts);
        }
    }

    @Override
    public void cancelReceipts(String receiptsId, UserObject userObject) throws AnneException {
        String[] ids = receiptsId.split(",");
        for (String id : ids) {
            Receipts receipts = baseDao.get(Receipts.class, id);
            if (receipts == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " deleteReceipts : 没有找到该收款单，请刷新列表");
            }
            receipts.setVeriAmount(new BigDecimal(0));
            receipts.setStatus(IConstants.RECEIPT_STATUS_R20);
            receipts.setUpdatedAt(new Date());
            if (userObject != null) {
                receipts.setUpdatedById(userObject.getEmployee().getId());
            }
            baseDao.update(receipts);

            String hql = " from VerificationDetail v where v.receiptsId = ? ";
            List<VerificationDetail> verificationDetails = baseDao.findEntity(hql, new Object[]{receipts.getId()});
            if (verificationDetails != null && verificationDetails.size() > 0) {
                for (VerificationDetail vs : verificationDetails) {
                    //已核销金额
                    BigDecimal amount = vs.getAmount() == null ? new BigDecimal(0) : vs.getAmount();//本次核销金额
                    //获取核销明细对应的回款计划明细
                    ContractReceiptDetail cReceiptDetail = baseDao.get(ContractReceiptDetail.class, vs.getContrReceDetailId());
                    if (cReceiptDetail != null) {
                        //扣除回款计划明细已核销金额
                        cReceiptDetail.setAmount(new BigDecimal(0).subtract(amount));
                        this.updateContractReceiptDetailVeriAmount(cReceiptDetail, userObject);
                    }
                    baseDao.delete(vs);
                }
            }
        }
    }

    @Override
    public Receipts getReceipts(String receiptsId) throws AnneException {
        return baseDao.get(Receipts.class, receiptsId);
    }

    @Override
    public IPage queryContractReceiptDetail(PageCondition condition) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(ContractReceiptDetail.class);
        filterObject.addOrderBy("paymentDate", "asc");
        filterObject.addOrderBy("contractCode", "asc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        StringBuffer sb = new StringBuffer(hqlObject.getHql());
        sb.append(" group by contractreceiptdetail.salesSort ");
        int i = sb.toString().indexOf(" order by");
        String hql = " and contractreceiptdetail.planAmount > contractreceiptdetail.veriAmount ";
        sb.insert(i, hql);

        return baseDao.search(sb.toString(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    /**
     * 保存\更新收款记录
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#saveOrUpdateReceiptsList(com.ibm.kstar.entity.order.ReceiptsListVO)
     */
    @Override
    public void saveOrUpdateReceiptsList(ReceiptsListVO receiptsList, UserObject userObject) throws Exception {
        //收款记录
        List<Map<Object, Object>> allotList = receiptsList.getAllotList();
        for (Map<Object, Object> map : allotList) {
            Receipts receipts = (Receipts) BeanUtils.convertMap(Receipts.class, map);
            Receipts oldReceipts = baseDao.get(Receipts.class, receipts.getId());
            if (oldReceipts == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " saveOrUpdateReceiptsList : 没有找到要更新的数据");
            }
            BeanUtils.copyPropertiesIgnoreNull(receipts, oldReceipts);
            oldReceipts.setStatus(IConstants.RECEIPT_STATUS_20);//已发布
            oldReceipts.setUpdatedById(userObject.getEmployee().getId());
            oldReceipts.setUpdatedAt(new Date());
            baseDao.update(oldReceipts);
        }
    }

    /**
     * TODO 发布收款单.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#publishReceipts(java.lang.String)
     */
    @Override
    public void publishReceipts(String id, UserObject userObject) throws Exception {
        Receipts receipts = baseDao.get(Receipts.class, id);
        if (receipts == null) {
            throw new AnneException(IReceiptsService.class.getName()
                    + " publishReceipts : 没有找到要更新的数据");
        }
        receipts.setStatus(IConstants.RECEIPT_STATUS_20);//已发布
        if(receipts.getAmount().compareTo(new BigDecimal(0))==0){
        	receipts.setStatus(IConstants.RECEIPT_STATUS_40);
        }
        receipts.setUpdatedAt(new Date());
        receipts.setUpdatedById(userObject.getEmployee().getId());

        //		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(),
        //				IConstants.PERMISSION_BUSINESS_TYPE_RECEIPTS,receipts.getId());

        baseDao.update(receipts);
    }

    /**
     * 收款核销
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#receiptsVerification(com.ibm.kstar.entity.order.ReceiptsListVO)
     */
    @Override
    public void receiptsVerification(String id, ReceiptsListVO receiptsList, UserObject userObject) throws Exception {
        //本次总核销金额
        BigDecimal totalVeriAmount = new BigDecimal(0);
        Receipts receipts = baseDao.get(Receipts.class, id);
        if (receipts == null) {
            throw new AnneException("核销失败，没有找到对应的收款单！");
        }
        if (!IConstants.RECEIPT_STATUS_20.equals(receipts.getStatus()) && !IConstants.RECEIPT_STATUS_30.equals(receipts.getStatus())) {
            throw new AnneException("核销失败，收款单状态不支持核销！");
        }
        //		if(receipts.getContractCode() != null && receipts.getContractCode().contains("KSTAR-JH")){
        //			throw new AnneException("核销失败，暂时不允许核销借货合同的收款单！");
        //		}

        //合同收款计划明细
        List<Map<Object, Object>> contractReceiptsList = receiptsList.getContractReceiptsList();
        if (contractReceiptsList != null && contractReceiptsList.size() > 0) {
            for (Map<Object, Object> map : contractReceiptsList) {
                ContractReceiptDetail contractReceiptsDetail = (ContractReceiptDetail) BeanUtils.convertMap(ContractReceiptDetail.class, map);
                //				if(contractReceiptsDetail.getContractCode() != null && contractReceiptsDetail.getContractCode().contains("KSTAR-JH")){
                //					throw new AnneException("核销失败，暂时不允许核销借货合同的回款计划！");
                //				}
                //如果币种不相符
                if (receipts.getCurrency() != null && !receipts.getCurrency().equals(contractReceiptsDetail.getReceiptsType())) {
                    throw new AnneException("核销失败，收款单币种和合同收款计划明细币种不一致！");
                }
                //本次核销金额
                BigDecimal amount = contractReceiptsDetail.getAmount() == null ? new BigDecimal(0) : contractReceiptsDetail.getAmount();
                BigDecimal balance = contractReceiptsDetail.getBalance() == null ? new BigDecimal(0) : contractReceiptsDetail.getBalance();
                if (amount.compareTo(balance) == 1) {
                    throw new AnneException("核销失败，本次核销金额不能大于余额！");
                }
                if (!Objects.equals(contractReceiptsDetail.getBusinessEntity(), receipts.getBusinessId())) {
                    throw new AnneException("核销失败，业务实体不相同。");
                }
            }

            //循环更新合同收款计划
            for (Map<Object, Object> map : contractReceiptsList) {
                ContractReceiptDetail contractReceiptsDetail = (ContractReceiptDetail) BeanUtils.convertMap(ContractReceiptDetail.class, map);
                ContractReceiptDetail oldcContractReceiptDetail = baseDao.get(ContractReceiptDetail.class, contractReceiptsDetail.getId());

                if (oldcContractReceiptDetail == null) {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " receiptsVerification : 核销失败，没有找对应的合同收款计划");
                }
                BigDecimal amount = contractReceiptsDetail.getAmount() == null ? new BigDecimal(0) : contractReceiptsDetail.getAmount();

                //getProductSum(oldcContractReceiptDetail.getId());

                //计算总核销金额
                totalVeriAmount = totalVeriAmount.add(amount);

                BeanUtils.copyPropertiesIgnoreNull(contractReceiptsDetail, oldcContractReceiptDetail);
                //更新合同计划明细核销金额
                this.updateContractReceiptDetailVeriAmount(oldcContractReceiptDetail, userObject);
                //生成核销明细
                this.createVerificationDetail(receipts, oldcContractReceiptDetail);
            }

            //更新收款单核销金额
            this.updateReceiptsVeriAmount(receipts, totalVeriAmount, userObject);

        } else {
            totalVeriAmount = receipts.getNotVeriAmount();

            String custId = "";
            if (StringUtil.isNotEmpty(receipts.getCorrectCustId())) {
                custId = receipts.getCorrectCustId();
            } else if (StringUtil.isNotEmpty(receipts.getPaymentCustomerId())) {
                custId = receipts.getPaymentCustomerId();
            }
            List<Object> args = new ArrayList<>();
            //			StringBuffer hql = new StringBuffer(" from ContractReceiptDetail cr where cr.salesmanId = ? and contractCode not like ? ");
            //			args.add(receipts.getSalesmanId());
            //			StringBuffer hql = new StringBuffer(" from ContractReceiptDetail cr where contractCode not like ? ");
            //			args.add("KSTAR-JH%");
            StringBuffer hql = new StringBuffer(" from ContractReceiptDetail cr where  ");
            hql.append(" cr.businessEntity = ?");
            args.add(receipts.getBusinessId());
            hql.append(" and cr.status != ? ");
            args.add(IConstants.RECEIPT_STATUS_40);
            hql.append(" and cr.status != ? ");
            args.add(IConstants.RECEIPT_STATUS_50);
            hql.append(" and cr.custId = ? ");
            args.add(custId);

            if (StringUtil.isNotEmpty(receipts.getContractCode())) {
                hql.append(" and cr.contractCode = ? ");
                args.add(receipts.getContractCode());
            }
            if (StringUtil.isNotEmpty(receipts.getCurrency())) {
                hql.append(" and cr.receiptsType = ? ");
                args.add(receipts.getCurrency());
            }

            hql.append(" order by paymentDate asc, receiptsPlan asc");
            List<ContractReceiptDetail> contractReceiptDetails = baseDao.findEntity(hql.toString(), args.toArray());
            BigDecimal veriAmount = new BigDecimal(0);
            if (contractReceiptDetails != null && contractReceiptDetails.size() > 0) {
                for (ContractReceiptDetail cr : contractReceiptDetails) {
                    BigDecimal amount = cr.getPlanAmount().subtract(cr.getVeriAmount()); //未核销金额
                    if (amount.doubleValue() <= 0) {
                        continue;
                    }

                    //getProductSum(cr.getId());

                    if (amount.compareTo(totalVeriAmount) >= 0) {
                        //更新合同计划明细核销金额
                        cr.setAmount(totalVeriAmount);
                        this.updateContractReceiptDetailVeriAmount(cr, userObject);
                        this.createVerificationDetail(receipts, cr); //生成核销明细
                        veriAmount = veriAmount.add(totalVeriAmount);
                        totalVeriAmount = totalVeriAmount.subtract(totalVeriAmount);
                    } else {
                        //更新合同计划明细核销金额
                        cr.setAmount(amount);
                        this.updateContractReceiptDetailVeriAmount(cr, userObject);
                        this.createVerificationDetail(receipts, cr); //生成核销明细
                        veriAmount = veriAmount.add(amount);
                        totalVeriAmount = totalVeriAmount.subtract(amount);
                    }
                    if (totalVeriAmount.compareTo(new BigDecimal(0)) <= 0) {
                        break;
                    }
                }
            }
            //更新收款单核销金额
            this.updateReceiptsVeriAmount(receipts, veriAmount, userObject);
        }
    }

    /**
     * createVerificationDetail:生成核销明细. <br/>
     *
     * @param receipts
     * @param contractReceiptsDetail
     * @author liming
     * @since JDK 1.7
     */
    private void createVerificationDetail(Receipts receipts, ContractReceiptDetail contractReceiptsDetail) {
        //核销明细
        String crId = contractReceiptsDetail.getId();

        VerificationDetail verificationDetail = new VerificationDetail();
        BeanUtils.copyPropertiesIgnoreNull(contractReceiptsDetail, verificationDetail);
        verificationDetail.setId(null);
        verificationDetail.setReceiptsId(receipts.getId());
        verificationDetail.setReceiptsCode(receipts.getReceiptsCode());
        verificationDetail.setContrReceDetailId(crId);
        verificationDetail.setSalesmanId(contractReceiptsDetail.getSalesmanId());
        verificationDetail.setSalesmanName(contractReceiptsDetail.getSalesmanName());
        verificationDetail.setVeriDate(new Date());
        verificationDetail.setExplain(contractReceiptsDetail.getExplain());
        //保存核销明细
        baseDao.save(verificationDetail);
    }

    /**
     * 更新收款单核销金额
     * updateReceiptsVeriAmount:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param id
     * @param veriAmount 本次总核销金额
     * @throws AnneException
     * @author liming
     * @since JDK 1.7
     */
    private Receipts updateReceiptsVeriAmount(Receipts oldReceipts, BigDecimal veriAmount, UserObject userObject) throws AnneException {
        if (oldReceipts == null) {
            throw new AnneException(IReceiptsService.class.getName()
                    + " updateReceiptsVeriAmount : 没有找到要更新的数据");
        }
        BigDecimal amount = oldReceipts.getAmount(); //收款金额
        BigDecimal oldVeriAmount = oldReceipts.getVeriAmount() == null ? new BigDecimal(0) : oldReceipts.getVeriAmount();
        BigDecimal totalVeriAmount = veriAmount.add(oldVeriAmount);

        if (totalVeriAmount.compareTo(amount) == 1) {
            throw new AnneException("核销失败，核销金额不能大于收款单未核销金额");
        }
        if (totalVeriAmount.compareTo(amount) == 0) {
            //如果收款合计金额等于已核销金额，则将状态改成已核销
            oldReceipts.setStatus(IConstants.RECEIPT_STATUS_40);
        } else {
            //部分核销
            if (totalVeriAmount.doubleValue() == 0) {
                oldReceipts.setStatus(IConstants.RECEIPT_STATUS_20);
            } else {
                oldReceipts.setStatus(IConstants.RECEIPT_STATUS_30);
            }
        }
        //更新收款金额
        oldReceipts.setVeriAmount(totalVeriAmount);
        oldReceipts.setUpdatedAt(new Date());
        if (userObject != null) {
            oldReceipts.setUpdatedById(userObject.getEmployee().getId());
        }
        baseDao.update(oldReceipts);
        return oldReceipts;
    }

    /**
     * updateContractReceiptDetailVeriAmount:更新合同收款计划明细核销金额. <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @param contractReceiptsDetail
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void updateContractReceiptDetailVeriAmount(ContractReceiptDetail contractReceiptDetail, UserObject userObject) {
        //本次核销金额
        BigDecimal amount = contractReceiptDetail.getAmount() == null ? new BigDecimal(0) : contractReceiptDetail.getAmount();
        //已核销金额
        BigDecimal veriAmount = contractReceiptDetail.getVeriAmount() == null ? new BigDecimal(0) : contractReceiptDetail.getVeriAmount();
        //累计已核销金额
        veriAmount = veriAmount.add(amount);
        BigDecimal planAmount = contractReceiptDetail.getPlanAmount() == null ? new BigDecimal(0) : contractReceiptDetail.getPlanAmount();
        int i = planAmount.compareTo(veriAmount);
        if (i == 0) {
            //如果合同计划金额等于已核销金额，则将状态改成已核销
            contractReceiptDetail.setStatus(IConstants.RECEIPT_STATUS_40);
        } else if (i == 1) {
            //部分核销
            if (veriAmount.doubleValue() == 0) {
                contractReceiptDetail.setStatus(IConstants.RECEIPT_STATUS_20);
            } else {
                contractReceiptDetail.setStatus(IConstants.RECEIPT_STATUS_30);
            }
        } else if (i == -1) {
            throw new AnneException(IReceiptsService.class.getName()
                    + " updateContractReceiptDetailVeriAmount : 核销金额不能超过合同计划收款金额");
        }
        //累计已核销金额
        contractReceiptDetail.setVeriAmount(veriAmount);
        contractReceiptDetail.setUpdatedAt(new Date());
        if (userObject != null) {
            contractReceiptDetail.setUpdatedById(userObject.getEmployee().getId());
        }
        //更新合同收款计划明细
        baseDao.update(contractReceiptDetail);
    }

    @Override
    public void importReceiptsList(List<List<Object>> data, UserObject userObject) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "OPERATION_UNIT");
        //业务实体LOV
        List<LovMember> businessList = lovMemberService.getList(condition);
        Map<String, LovMember> businessMap = new HashMap<String, LovMember>();
        for (LovMember lov : businessList) {
            businessMap.put(lov.getName(), lov);
        }
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "CURRENCY");
        //货币
        List<LovMember> currencyList = lovMemberService.getList(condition);
        Map<String, LovMember> currencyMap = new HashMap<String, LovMember>();
        for (LovMember lov : currencyList) {
            currencyMap.put(lov.getName(), lov);
        }
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "RECEIPT_STATUS");
        //收款状态
        List<LovMember> statusList = lovMemberService.getList(condition);
        Map<String, LovMember> statusMap = new HashMap<String, LovMember>();
        for (LovMember lov : statusList) {
            statusMap.put(lov.getName(), lov);
        }
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "RECEIPTSTYPE");
        //收款状态
        List<LovMember> typeList = lovMemberService.getList(condition);
        Map<String, LovMember> typeMap = new HashMap<String, LovMember>();
        for (LovMember lov : typeList) {
            typeMap.put(lov.getName(), lov);
        }

        if (data == null) {
            return;
        }

        for (List<Object> rowData : data) {

            LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "RECEIPTS");
            String code = "";
            String prefix = "KST-SK-";
            if (lov != null) {
                prefix = lov.getName();
            }
            code = orderService.getSequenceCode("gen_receipts_code", prefix);

            Receipts receipts = new Receipts();
            receipts.setReceiptsCode(code);
            //业务实体
            Object businessName = rowData.get(0);
            if (businessName == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " importReceiptsList : 业务实体不能为空");
            }
            lov = businessMap.get(businessName.toString());
            if (lov == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " importReceiptsList : 没有找到对应业务实体");
            }
            receipts.setBusinessId(lov.getId());//业务实体ID
            receipts.setBusinessCode(lov.getCode());//业务实体名称
            receipts.setBusinessName(lov.getName());//业务实体名称

            //收款日期
            if (rowData.get(1) == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " importReceiptsList : 收款日期不能为空");
            }
            Date receiptsDate = null;
            try {
                receiptsDate = sdf.parse(rowData.get(1).toString());
            } catch (Exception e) {
                try {
                    String date = sdf.format(rowData.get(1));
                    receiptsDate = sdf.parse(date);
                } catch (Exception e1) {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " importReceiptsList : 日期格式应该为 dd/MM/yyyy");
                }
            }
            receipts.setReceiptsDate(receiptsDate);//收款日期

            //收款分类
            Object type = rowData.get(2);
            if (type != null) {
                lov = typeMap.get(type.toString());
                if (lov != null) {
                    receipts.setReceiptsType(lov.getCode());//收款分类
                }
            }
            //付款客户
            String paymentCustomerName = "";
            CustomInfo customInfo = null;
            if (rowData.get(3) != null) {
                paymentCustomerName = rowData.get(3).toString();
                customInfo = this.getCustomInfoByFullName(paymentCustomerName);
                if (customInfo != null) {
                    receipts.setPaymentCustomerId(customInfo.getId());//付款客户ID
                    receipts.setErpCust(IConstants.YES_Yes);//是ERP客户
                } else {
                    receipts.setErpCust(IConstants.NO_No);//否ERP客户
                }
            } else {
                receipts.setErpCust(IConstants.NO_No);//否ERP客户
            }
            receipts.setPaymentCustomerName(paymentCustomerName);

            //更正客户
            String correctCustomerCode = "";
            if (rowData.get(5) != null) {
                correctCustomerCode = rowData.get(5).toString();
                customInfo = customInfoService.getCustomInfoByCode(correctCustomerCode);
                if (customInfo != null) {
                    receipts.setCorrectCustId(customInfo.getId());//更正客户ID
                    receipts.setCorrectCustName(customInfo.getCustomFullName());//更正客户名称
                }
            }
            if (rowData.get(5) == null || customInfo == null) {
                //更正客户
                String correctCustomerName = "";
                if (rowData.get(4) != null) {
                    correctCustomerName = rowData.get(4).toString();
                    customInfo = this.getCustomInfoByFullName(correctCustomerName);
                    if (customInfo != null) {
                        receipts.setCorrectCustId(customInfo.getId());//更正客户ID
                        receipts.setCorrectCustName(customInfo.getCustomFullName());//更正客户名称
                    }
                }
            }

            receipts.setRegion(rowData.get(6) != null ? rowData.get(6).toString() : "");//地区（国家/省）
            receipts.setReceiptsBank(rowData.get(7) != null ? rowData.get(7).toString() : "");//收款银行
            receipts.setBankAccount(rowData.get(8) != null ? rowData.get(8).toString() : "");//收款账号
            //币种
            Object currency = rowData.get(9);
            if (currency == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " importReceiptsList :币种不能为空!");
            } else {
                lov = currencyMap.get(currency.toString());
                if (lov == null) {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " importReceiptsList :没有找打对应的币种");
                } else {
                    receipts.setCurrency(lov.getCode());//币种
                }
            }
            //到账金额
            receipts.setArrivalAmount(rowData.get(10) != null ?
                    new BigDecimal(rowData.get(10).toString()) : new BigDecimal(0));
            //				//手续费
            //				receipts.setPoundage(rowData.get(10) != null ?
            //						new BigDecimal(rowData.get(10).toString()) : new BigDecimal(0));
            //
            //				BigDecimal arrivalAmount = receipts.getArrivalAmount() == null ? new BigDecimal(0) : receipts.getArrivalAmount();
            //				BigDecimal poundage = receipts.getPoundage() == null ? new BigDecimal(0) : receipts.getPoundage();
            //				//合计
            //				receipts.setAmount(arrivalAmount.add(poundage));
            receipts.setAmount(receipts.getArrivalAmount());
            //				//代垫运费收款
            //				receipts.setFreight(rowData.get(11) != null ?
            //						new BigDecimal(rowData.get(11).toString()) : new BigDecimal(0));
            //				//是否预收款
            //				if(rowData.get(12) != null){
            //					String isAdvancesReceived = rowData.get(12).toString();
            //					if(StringUtil.isNotEmpty(isAdvancesReceived) && IConstants.YES_Yes.equals(isAdvancesReceived)){
            //						receipts.setIsAdvancesReceived(IConstants.YES_Yes);
            //					}else{
            //						receipts.setIsAdvancesReceived(IConstants.NO_No);
            //					}
            //				}else{
            //					receipts.setIsAdvancesReceived(IConstants.NO_No);
            //				}
            //				//合同编号
            //				receipts.setContractCode(rowData.get(13) != null ?rowData.get(13).toString(): "");
            //备注
            receipts.setRemarks(rowData.get(11) != null ? rowData.get(11).toString() : "");


            //销售人员
            Object employeeNo = rowData.get(12);
            Employee employee = null;
            if (employeeNo != null) {
                employee = employeeService.getEmployeeByNo(employeeNo.toString());
            }

            // 获取所属销售组织和业务部门
            if ((employee == null && employeeNo == null) || employee != null) {
                Object[] sbte = null;
                if (employee != null) {
                    //销售人员名称
                    String employeeName = rowData.get(13) != null ? rowData.get(13).toString() : "";
                    if (employeeName.equals(employee.getName())) {
                        sbte = getSalesCenterAndBizDept(customInfo, employee);
                    }
                } else {
                    sbte = getSalesCenterAndBizDept(customInfo, employee);
                }
                if (sbte != null) {
                    employee = (Employee) sbte[2];
                    if (employee != null) {

                        receipts.setSalesmanId(employee.getId());
                        receipts.setSalesmanCode(employee.getNo());
                        receipts.setSalesmanName(employee.getName());

                        LovMember saleCenter = (LovMember) sbte[0];
                        receipts.setSalesCenter(saleCenter != null ? saleCenter.getId() : null);

                        LovMember bizDept = (LovMember) sbte[1];
                        receipts.setBizDept(bizDept != null ? bizDept.getId() : null);

                        Team team = (Team) sbte[3];
                        receipts.setSalesmanPost(team != null ? team.getPositionId() : null);

                    }

                }
            }

            if (userObject != null) {
                //负责部门
                receipts.setRespDept(userObject.getOrg().getId());
                //负责人
                receipts.setPicNo(userObject.getEmployeeNo());//负责人员工号
                receipts.setPic(userObject.getEmployeeNo() + "|" + userObject.getEmployeeName());//负责人
                String saleCenterCode = lovMemberService.getSaleCenter(userObject.getOrg().getId());
                lov = lovMemberService.getLovMemberByCode("ORG", saleCenterCode);
                if (lov != null) {
                    receipts.setPicSaleCenter(lov.getId()); //负责人销售中心
                }
            }

            receipts.setErpImp(IConstants.NO_No);
            receipts.setStatus(IConstants.RECEIPT_STATUS_10);//收款状态
            receipts.setIsAdvancesReceived(IConstants.NO_No);
            receipts.setPoundage(new BigDecimal(0));
            receipts.setVeriAmount(new BigDecimal(0));
            receipts.setFreight(new BigDecimal(0));

            this.saveReceipts(receipts, userObject);

        }
    }

    /**
     * 获取所属销售组织和业务部门
     * 先取更正客户,更正客户不存在在取
     * 如果销售人员为空则取客户的销售团队中的第一条
     *
     * @param customInfo 客户
     * @param employee   销售人员
     * @param teamCache  销售团队缓存数据
     * @return 返回一个包含四个元素的数组(第一个元素为销售组织, 第二个元素为业务部门, 第三元素为员工, 第四个元素为销售团队)或null
     */
    private Object[] getSalesCenterAndBizDept(CustomInfo customInfo) {
        if (customInfo == null) {
            return null;
        }
        Team team = teamService.getTeamByBusinessId(customInfo.getId());
        if (team == null) {
            return null;
        }
        LovMember[] salesCenterAndBizDept = getSalesCenterAndBizDept(team);
        if (salesCenterAndBizDept == null) {
            return null;
        }
        Employee employee = employeeService.get(team.getMasterEmployeeId());
        return new Object[]{salesCenterAndBizDept[0], salesCenterAndBizDept[1], employee, team};


    }

    /**
     * @param customInfo
     * @param employee
     * @return 返回一个包含三个个元素的数组(第一个元素为销售组织, 第二个元素为业务部门, 第三元素为员工, 第四个元素为销售团队)或null
     */
    private Object[] getSalesCenterAndBizDept(CustomInfo customInfo, Employee employee) {
        if (customInfo == null) {
            return null;
        }
        if (employee == null) {
            return getSalesCenterAndBizDept(customInfo);
        }

        Team team = null;
        List<Team> teams = teamService.getAllTeamByBusinessId(customInfo.getId());
        String employeeId = employee.getId();
        for (Team t : teams) {
            if (Objects.equals(t.getMasterEmployeeId(), employeeId)) {
                team = t;
                break;
            }
        }

        if (team == null) {
            return null;
        }
        LovMember[] salesCenterAndBizDept = getSalesCenterAndBizDept(team);
        if (salesCenterAndBizDept == null) {
            return null;
        }
        return new Object[]{salesCenterAndBizDept[0], salesCenterAndBizDept[1], employee, team};
    }

    /**
     * 根据销售团队获取营销中心和业务部门
     *
     * @return 返回一个包含两个个元素的数组(第一个元素为销售组织, 第二个元素为业务部门)或null
     */
    private LovMember[] getSalesCenterAndBizDept(Team team) {
        String positionId = team.getPositionId();
        LovMember position = ((LovMember) CacheData.getInstance().get(positionId));
        if (position == null) {
            return null;
        }
        String orgId = position.getOptTxt1();
        LovMember org = null;
        if (orgId != null) {
            org = ((LovMember) CacheData.getInstance().get(orgId));
        }
        if (org == null) {
            return null;
        }
        LovMember salesCenter = getSaleCenter(org.getParentId());
        LovMember bizDept = null;
        if (salesCenter != null) {
            bizDept = getBizDept(org.getParentId(), salesCenter.getId());
        }
        return new LovMember[]{salesCenter, bizDept};
    }

    /**
     * getOrderSalesmanCenter:获取当前销售中心. <br/>
     *
     * @param currentDepId
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getSaleCenter(String currentDepId) {
        return lovMemberService.getSaleCenterLovmember(currentDepId);
    }

    /**
     * 获取业务中心
     *
     * @param oid
     * @param parentId
     * @return
     */
    @Override
    public LovMember getBizDept(String oid, String parentId) {
        return lovMemberService.get(oid);
    }


    @Override
    public void importReceiptPlanList(List<List<Object>> data, UserObject userObject) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "CURRENCY");
        //货币
        List<LovMember> currencyList = lovMemberService.getList(condition);
        Map<String, LovMember> currencyMap = new HashMap<String, LovMember>();
        for (LovMember lov : currencyList) {
            currencyMap.put(lov.getCode(), lov);
        }
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "CONTRACTPAYMENTPLAN");
        //收款阶段
        List<LovMember> stageList = lovMemberService.getList(condition);
        Map<String, LovMember> stageMap = new HashMap<String, LovMember>();
        for (LovMember lov : stageList) {
            stageMap.put(lov.getName(), lov);
        }
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "CONTRACTPAYSEQ");
        //收款计划行
        List<LovMember> planList = lovMemberService.getList(condition);
        Map<String, LovMember> planMap = new HashMap<String, LovMember>();
        for (LovMember lov : planList) {
            planMap.put(lov.getName(), lov);
        }


        //业务部门
        condition = new Condition();
        condition.getFilterObject().addCondition("groupCode", "eq", "SALES_DEPARTMENT");
        condition.getFilterObject().addCondition("level", "eq", "2");
        List<LovMember> bizDeptList = lovMemberService.getList(condition);
        Map<String, LovMember> bizDeptMap = new HashMap<String, LovMember>();
        for (LovMember lov : bizDeptList) {
            bizDeptMap.put(lov.getName(), lov);
        }

        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                List<Object> rowData = data.get(i);
                LovMember lov = new LovMember();
                ContractReceiptDetail detail = new ContractReceiptDetail();

                //付款客户
                Object paymentCustomerName = rowData.get(1);
                ;
                CustomInfo customInfo = null;
                if (paymentCustomerName != null) {
                    customInfo = this.getCustomInfoByFullName(paymentCustomerName.toString());
                    if (customInfo != null) {
                        detail.setCustId(customInfo.getId());//付款客户ID
                        detail.setCustCode(customInfo.getErpCode());
                        detail.setCustName(customInfo.getCustomFullName());
                    }
                }

                //收款阶段
                Object stage = rowData.get(2);
                if (stage != null) {
                    lov = stageMap.get(stage.toString());
                    if (lov != null) {
                        detail.setReceiptsStage(lov.getId());
                    }
                }

                //币种
                Object currency = rowData.get(3);
                if (currency == null) {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " importReceiptsList :币种不能为空!");
                } else {
                    lov = currencyMap.get(currency.toString());
                    if (lov == null) {
                        throw new AnneException(IReceiptsService.class.getName()
                                + " importReceiptsList :没有找打对应的币种");
                    } else {
                        detail.setReceiptsType(lov.getCode());//币种
                    }
                }
                //已核销金额
                Object veriAmount = rowData.get(4);
                if (veriAmount != null) {
                    BigDecimal amount = new BigDecimal(0);
                    try {
                        amount = new BigDecimal(veriAmount.toString());
                    } catch (Exception e) {
                        amount = new BigDecimal(0);
                    }
                    detail.setVeriAmount(amount);//已核销金额
                }
                //合同
                Object contractCode = rowData.get(5);
                if (contractCode != null) {
                    Contract contract = contractService.getContractByNo(contractCode.toString());
                    if (contract != null) {
                        detail.setContractId(contract.getId());//合同ID
                        detail.setContractCode(contract.getContrNo());//合同编号
                    }
                }
                DeliveryHeader deliveryHeader = null;
                //出货单
                Object deliveryCode = rowData.get(6);
                if (deliveryCode != null) {
                    deliveryHeader = deliveryService.getDeliveryHeaderByCode(deliveryCode.toString());
                    if (deliveryHeader != null) {
                        detail.setDeliveryCode(deliveryHeader.getDeliveryCode());//出货单编号
                        detail.setDeliveryId(deliveryHeader.getId());//出货单ID
                    }
                }
                //计划金额
                Object amount = rowData.get(7);
                if (amount != null) {
                    BigDecimal planAmount = new BigDecimal(0);
                    try {
                        planAmount = new BigDecimal(amount.toString());
                    } catch (Exception e) {
                        planAmount = new BigDecimal(0);
                    }
                    detail.setPlanAmount(planAmount);
                }
                //备注
                Object remarks = rowData.get(8);
                if (remarks != null) {
                    detail.setRemarks(remarks.toString());
                }

                //收款计划行
                Object receiptsPlan = rowData.get(9);
                if (receiptsPlan != null) {
                    lov = planMap.get(receiptsPlan.toString());
                    if (lov != null) {
                        detail.setReceiptsPlan(lov.getId());
                    } else {
                        lov = lovMemberService.getLovMemberByCode("CONTRACTPAYSEQ", "01");
                        if (lov != null) {
                            detail.setReceiptsPlan(lov.getId());
                        }
                    }
                }
                //销售人员
                Object employeeNo = rowData.get(10);
                Employee salesman = null;
                if (employeeNo != null) {
                    salesman = employeeService.getEmployeeByNo(employeeNo.toString());
                    if (salesman != null) {
                        //销售人员名称
                        //String employeeName = rowData.get(11) != null ? rowData.get(11).toString() : "";
                        detail.setSalesmanId(salesman.getId());
                        detail.setSalesmanCode(salesman.getNo());
                        detail.setSalesmanName(salesman.getName());
                    } else {
                        throw new AnneException(IReceiptsService.class.getName()
                                + " importReceiptsList : 导入失败，销售人员编号【" + employeeNo + "】没有找到对应的销售员");
                    }
                } else {
                    throw new AnneException(IReceiptsService.class.getName()
                            + " importReceiptsList : 导入失败，销售人员编号不能为空");
                }

                //业务部门
                Object bizDept = rowData.get(12);
                if (bizDept != null) {
                    lov = bizDeptMap.get(bizDept.toString());
                    if (lov != null) {
                        detail.setBizDept(lov.getId());
                    }
                }
                //到期日
                Object paymentDate = rowData.get(13);
                if (paymentDate != null) {
                    Date date = null;
                    try {
                        date = sdf.parse(paymentDate.toString());
                    } catch (Exception e) {
                        try {
                            String d = sdf.format(paymentDate);
                            date = sdf.parse(d);
                        } catch (Exception e1) {
                            throw new AnneException(IReceiptsService.class.getName()
                                    + " importReceiptsList : 日期格式应该为 dd/MM/yyyy");
                        }
                    }
                    detail.setPaymentDate(date);
                }
                if (deliveryHeader != null) {
                    // 创建字段
                    detail.setCreatedById(deliveryHeader.getCreatedById());
                    detail.setCreatedAt(new Date());
                    detail.setCreatedPosId(deliveryHeader.getCreatedPosId());
                    detail.setCreatedOrgId(deliveryHeader.getCreatedOrgId());
                    // 更新字段
                    detail.setUpdatedById(deliveryHeader.getCreatedById());//更新人
                } else {
                    // 创建字段
                    detail.setCreatedById(userObject.getEmployee().getId());
                    detail.setCreatedAt(new Date());
                    detail.setCreatedPosId(userObject.getPosition().getId());
                    detail.setCreatedOrgId(userObject.getOrg().getId());
                    // 更新字段
                    detail.setUpdatedById(userObject.getEmployee().getId());//更新人
                }
                detail.setUpdatedAt(new Date());//更新时间

                baseDao.save(detail);
                if (deliveryHeader != null) {
                    teamService.addPosition(deliveryHeader.getCreatedPosId(), deliveryHeader.getCreatedById(),
                            IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL, detail.getId());
                } else {
                    //销售员添加到销售团队
                    List<LovMember> mems = corePermissionService.getUserPositionList(salesman.getId());
                    if (mems != null) {
                        for (LovMember lovMember : mems) {
                            teamService.addPosition(lovMember.getId(), salesman.getId(),
                                    IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL, detail.getId());
                        }
                    }
                }

            }
        }

    }

    //获取付款单位
    private CustomInfo getCustomInfoByFullName(String fullName) {
        if (StringUtil.isEmpty(fullName)) {
            return null;
        }
        //获取付款单位
        String hql = "from CustomInfo c where c.customFullName = ? ";
        return baseDao.findUniqueEntity(hql, new Object[]{fullName});
    }

    @Override
    public List<Receipts> getReceiptsList(Condition condition) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(Receipts.class);
        filterObject.addOrderBy("updatedAt", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.findEntity(hqlObject.getHql(), hqlObject.getArgs());
    }

    @Override
    public List<List<Object>> getReceiptsImportTemplet() {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("收款组织");
        lstHead.add("收款时间(日/月/年)");
        lstHead.add("收款分类");
        lstHead.add("付款单位名称");
        lstHead.add("更正客户");
        lstHead.add("更正客户编号");
        lstHead.add("国家/省");
        lstHead.add("我司银行名称");
        lstHead.add("我司银行账号");
        lstHead.add("收款币别");
        lstHead.add("到账金额");
        //		lstHead.add("手续费");
        //		lstHead.add("代垫运费收款");
        //		lstHead.add("是否是预收款");
        //		lstHead.add("合同编号");
        lstHead.add("备注");
        lstHead.add("销售人员工号");
        lstHead.add("销售人员名称");
        lstHead.add("销售人员营销中心");
        lstHead.add("销售人员业务部门");
        //		lstHead.add("负责部门");
        //		lstHead.add("负责人员工号");
        //		lstHead.add("负责人");
        lstOut.add(lstHead);

        List<Object> lstIn = new ArrayList<Object>();
        lstIn.add("101-深圳科士达科技股份有限公司");
        lstIn.add("30/01/2017");
        lstIn.add("现金");
        lstIn.add("广州市XXXXXX有限公司");
        lstIn.add("广州市XXXXXX有限公司");
        lstIn.add("XXXXXX");
        lstIn.add("中国");
        lstIn.add("XX银行XXXXXX支行");
        lstIn.add("0192100184775");
        lstIn.add("人民币");
        lstIn.add(10000);
        //		lstIn.add(10);
        //		lstIn.add(10);
        //		lstIn.add("Yes");
        //		lstIn.add("KSTAR-XXXXXXXXXXX");
        lstIn.add("备注-货款");
        lstIn.add("XXXXX");
        lstIn.add("张三");
        lstIn.add("国内营销中心");
        lstIn.add("国内营销销售部");
        //		lstIn.add("商务部");
        //		lstIn.add("XXXXX");
        //		lstIn.add("XXX");
        lstOut.add(lstIn);
        return lstOut;
    }

    @Override
    public List<List<Object>> getExcelData(List<Receipts> receiptsList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("收款组织");
        lstHead.add("收款时间");
        lstHead.add("收款分类");
        lstHead.add("付款客户");
        lstHead.add("是否ERP客户");
        lstHead.add("更正客户");
        lstHead.add("国家/省");
        lstHead.add("我司银行名称");
        lstHead.add("我司银行账号");
        lstHead.add("收款币别");
        lstHead.add("到账金额");
        lstHead.add("手续费");
        lstHead.add("合计");
        lstHead.add("代垫运费收款");
        lstHead.add("已核销金");
        lstHead.add("未核销金额");
        lstHead.add("是否是预收款");
        lstHead.add("合同编号");
        lstHead.add("备注");
        lstHead.add("销售人员编码");
        lstHead.add("销售人员名称");
        lstHead.add("所属营销中心");
        lstHead.add("业务部门");
        lstHead.add("负责部门");
        lstHead.add("负责人");
        lstHead.add("流程状态");
        lstOut.add(lstHead);
        for (Receipts receipts : receiptsList) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(receipts.getBusinessName());
            lstIn.add(sdf.format(receipts.getReceiptsDate()));
            lstIn.add(receipts.getReceiptsTypeLable());
            lstIn.add(receipts.getPaymentCustomerName());
            lstIn.add(receipts.getErpCust());
            lstIn.add(receipts.getCorrectCustName());
            lstIn.add(receipts.getRegion());
            lstIn.add(receipts.getReceiptsBank());
            lstIn.add(receipts.getBankAccount());
            lstIn.add(receipts.getCurrencyLable());
            lstIn.add(receipts.getArrivalAmount());
            lstIn.add(receipts.getPoundage());
            lstIn.add(receipts.getAmount());
            lstIn.add(receipts.getFreight());
            lstIn.add(receipts.getVeriAmount());
            lstIn.add(receipts.getNotVeriAmount());
            lstIn.add(receipts.getIsAdvancesReceived());
            lstIn.add(receipts.getContractCode());
            lstIn.add(receipts.getRemarks());
            lstIn.add(receipts.getSalesmanCode());
            lstIn.add(receipts.getSalesmanName());
            lstIn.add(receipts.getSalesCenterLable());
            lstIn.add(receipts.getBizDeptLable());
            lstIn.add(receipts.getRespDept());
            lstIn.add(receipts.getPic());
            lstIn.add(receipts.getStatusLable());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    @Override
    public IPage queryVerification(PageCondition condition)
            throws AnneException {
        FilterObject filterObject = condition.getFilterObject(VerificationDetail.class);
        filterObject.addOrderBy("veriDate", "desc");


        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    /**
     * TODO 删除核销明细，回写核销金额.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#deleteVerification(java.lang.String)
     */
    @Override
    public void deleteVerification(String verificationId, UserObject userObject) throws AnneException {

        String[] ids = verificationId.split(",");
        for (String id : ids) {
            VerificationDetail verificationDetail = baseDao.get(VerificationDetail.class, id);
            if (verificationDetail == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " deleteVerification : 没有找到该核销明细，请刷新列表");
            }
            //已核销金额
            BigDecimal amount = verificationDetail.getAmount() == null ? new BigDecimal(0) : verificationDetail.getAmount();//本次核销金额
            //获取核销明细对应的回款计划明细
            ContractReceiptDetail cReceiptDetail = baseDao.get(ContractReceiptDetail.class, verificationDetail.getContrReceDetailId());
            if (cReceiptDetail != null) {
                //扣除回款计划明细已核销金额
                cReceiptDetail.setAmount(new BigDecimal(0).subtract(amount));
                this.updateContractReceiptDetailVeriAmount(cReceiptDetail, userObject);
            }
            //扣除收款单已核销金额
            Receipts receipts = baseDao.get(Receipts.class, verificationDetail.getReceiptsId());
            this.updateReceiptsVeriAmount(receipts, new BigDecimal(0).subtract(amount), userObject);

            baseDao.delete(verificationDetail);
        }
    }

    @Override
    public void returnReceipt(String id, UserObject userObject) throws AnneException {
        if (StringUtil.isNotEmpty(id)) {
            Receipts receipts = baseDao.get(Receipts.class, id);
            if (receipts == null) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " returnReceipt : 没有找到该收款单，请刷新列表");
            }
            if ((receipts.getVeriAmount() != null && receipts.getVeriAmount().doubleValue() > 0)
                    || !IConstants.RECEIPT_STATUS_20.equals(receipts.getStatus())) {
                throw new AnneException(IReceiptsService.class.getName()
                        + " returnReceipt : 收款单已存在核销或收款单状态不支持退回，请检查");
            }
            receipts.setStatus(IConstants.RECEIPT_STATUS_R10);//业务退回
            receipts.setUpdatedAt(new Date());
            if (userObject != null) {
                receipts.setUpdatedById(userObject.getEmployee().getId());
            }
            baseDao.update(receipts);
        }
    }

    @Override
    public List<List<Object>> getReceiptPlanImportTemplet() {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();

        lstHead.add("客户编号");
        lstHead.add("客户名称");
        lstHead.add("收款阶段");
        lstHead.add("币种");
        lstHead.add("已核销金额");
        lstHead.add("合同编号");
        lstHead.add("出货申请编码");
        lstHead.add("计划金额");
        lstHead.add("备注");
        lstHead.add("合同收款计划行");
        lstHead.add("销售员工号");
        lstHead.add("销售员名称");
        lstHead.add("业务部门");
        lstHead.add("到期日");
        lstOut.add(lstHead);

        List<Object> lstIn = new ArrayList<Object>();
        lstIn.add("XXXXX");
        lstIn.add("广州市XXXXXX有限公司");
        lstIn.add("发货");
        lstIn.add("CNY");
        lstIn.add(0);
        lstIn.add("KSTAR-XXXXXXXXXXX");
        lstIn.add("KST-CH-XXXXXXXXXXX");
        lstIn.add(0);
        lstIn.add("备注.....");
        lstIn.add("第一笔");
        lstIn.add("XXXXX");
        lstIn.add("张三");
        lstIn.add("XXXXXX");
        lstIn.add("17/2/2017");

        lstOut.add(lstIn);
        return lstOut;
    }

    /**
     * TODO 收款单拆分保存（可选）.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#splitReceiptsSave(java.lang.String, java.math.BigDecimal, java.lang.String)
     */
    @Override
    public void splitReceiptsSave(String id, BigDecimal splitAmount, String splitContractCode, UserObject userObject) {
        Receipts receipts = this.getReceipts(id);
        if (receipts == null) {
            throw new AnneException(IOrderService.class.getName()
                    + " splitReceiptsSave : 拆分失败，没有找到需要拆分的收款单!");
        }
        if (!IConstants.RECEIPT_STATUS_20.equals(receipts.getStatus())) {
            throw new AnneException(IOrderService.class.getName()
                    + " splitReceiptsSave : 拆分失败，收款单状态不允许拆分!");
        }
        if (splitAmount.compareTo(receipts.getArrivalAmount()) >= 0) {
            throw new AnneException(IOrderService.class.getName()
                    + " splitReceiptsSave : 拆分失败，拆分金额不能大于收款金额!");
        }
        receipts.setArrivalAmount(receipts.getArrivalAmount().subtract(splitAmount));
        receipts.setAmount(receipts.getArrivalAmount().add(receipts.getPoundage()));

        receipts.setUpdatedAt(new Date());
        if (userObject != null) {
            receipts.setUpdatedById(userObject.getEmployee().getId());
        }
        baseDao.update(receipts);//更新被拆分收款单
        //保存拆分收款单
        Receipts newReceipts = new Receipts();
        BeanUtils.copyPropertiesIgnoreNull(receipts, newReceipts);
        newReceipts.setReceiptsCode(this.getReceiptsCode(receipts.getReceiptsCode()));
        newReceipts.setId(null);
        newReceipts.setContractCode(splitContractCode);
        newReceipts.setArrivalAmount(splitAmount);
        newReceipts.setPoundage(new BigDecimal(0));
        newReceipts.setAmount(splitAmount);
        newReceipts.setCreatedAt(new Date());
        newReceipts.setUpdatedAt(new Date());
        if (userObject != null) {
            newReceipts.setCreatedById(userObject.getEmployee().getId());
            newReceipts.setUpdatedById(userObject.getEmployee().getId());
            receipts.setCreatedPosId(userObject.getPosition().getId());
            receipts.setCreatedOrgId(userObject.getOrg().getId());
        }
        baseDao.save(newReceipts);
    }

    private String getReceiptsCode(String receiptsCode) {
        if (StringUtil.isEmpty(receiptsCode)) {
            return null;
        }
        int index = receiptsCode.indexOf("-", 16);
        if (index > 0) {
            receiptsCode = receiptsCode.substring(0, index);
        }
        String hql = "select t.receiptsCode from Receipts t where t.receiptsCode like ? ";
        List<String> receiptsCodes = baseDao.findEntity(hql, new Object[]{receiptsCode + "%"});
        List<Integer> codeIntLst = new ArrayList<Integer>();
        if (receiptsCodes == null) {
            return receiptsCode + "-1";
        } else {
            for (String code : receiptsCodes) {
                if (code == null || code == "") {
                    code = "0";
                } else {
                    int i = code.indexOf("-", 16);
                    if (i > 0) {
                        code = code.substring(i + 1, code.length());
                    } else {
                        code = "0";
                    }
                }
                codeIntLst.add(Integer.parseInt(code));
            }
            int maxCode = Collections.max(codeIntLst);
            int n = maxCode + 1;
            return receiptsCode + "-" + String.valueOf(n);
        }
    }


    @Override
    public List<ContractReceiptProduct> getContractReceiptProduct(String detailId) {
        return baseDao.findEntity("from ContractReceiptProduct where detailId = ? ", detailId);
    }

    @Override
    public void saveOrUpdateContractReceiptProduct(ContractReceiptDetail detail, String receiptsId) {

        BigDecimal sum = new BigDecimal(0);

        List<ContractReceiptProduct> list = detail.getItems();
        ContractReceiptProduct contractReceiptProduct = null;
        for (ContractReceiptProduct receiptProduct : list) {

            if (receiptProduct.getQuantityA() == null) {
                continue;
            }

            if (receiptProduct.getQuantityA().doubleValue() <= 0) {
                throw new AnneException("核销数量不能小于等于0");
            }

            if (StringUtil.isNullOrEmpty(receiptProduct.getId())) { //TODO 这里能新增？20170803 Jerry
                receiptProduct.setQuantity(receiptProduct.getQuantityA());
                baseDao.save(receiptProduct);
            } else {
                contractReceiptProduct = baseDao.get(ContractReceiptProduct.class, receiptProduct.getId());
                contractReceiptProduct.setQuantity(contractReceiptProduct.getQuantity().add(receiptProduct.getQuantityA()));
                contractReceiptProduct.setNotes(receiptProduct.getNotes());
                baseDao.update(contractReceiptProduct);
            }

            sum = sum.add(receiptProduct.getQuantityA().multiply(receiptProduct.getPrice()));
            ContractReceiptDetail cpd = baseDao.get(ContractReceiptDetail.class, list.get(0).getDetailId());
            Receipts receipts = baseDao.get(Receipts.class, receiptsId);
            cpd.setAmount(sum);
            this.createVerificationDetail(receipts, cpd);


        }

        if (list.size() > 0) {
            ContractReceiptDetail cpd = baseDao.get(ContractReceiptDetail.class, list.get(0).getDetailId());
            cpd.setVeriAmount(cpd.getVeriAmount().add(sum));
            baseDao.update(cpd);
        }

        //		if (list.size() > 0) {
        //			BigDecimal sum = new BigDecimal(0);
        //			list = getContractReceiptProduct(list.get(0).getDetailId());
        //			for (ContractReceiptProduct receiptProduct : list) {
        //				sum = receiptProduct.getQuantity().multiply(receiptProduct.getPrice()).add(sum);
        //			}
        //			ContractReceiptDetail receiptDetail = baseDao.get(ContractReceiptDetail.class, list.get(0).getDetailId());
        //			if (sum.compareTo(receiptDetail.getVeriAmount()) > 0) {
        //				throw new AnneException("核销产品明细的金额="+sum+"不能大于，收款计划已核销金额"+receiptDetail.getVeriAmount());
        //			}
        //		}

    }

    /**
     * 核销验证合同明细
     *
     * @param detailId
     */
    private BigDecimal getProductSum(String detailId) {
        BigDecimal sum = new BigDecimal(0);
        List<ContractReceiptProduct> list = getContractReceiptProduct(detailId);

        ContractReceiptDetail crd = baseDao.get(ContractReceiptDetail.class, detailId);
        if (crd == null) {
            throw new AnneException("无效的ID");
        }

        if (list.size() < 1 && crd.getContractCode() != null && crd.getContractCode().startsWith("KSTAR-JH")) {
            throw new AnneException("请选择核销产品明细");
        }

        for (ContractReceiptProduct receiptProduct : list) {
            sum = receiptProduct.getQuantity().multiply(receiptProduct.getPrice()).add(sum);
        }
        ContractReceiptDetail receiptDetail = baseDao.get(ContractReceiptDetail.class, detailId);

        if (sum.compareTo(receiptDetail.getVeriAmount()) > 0) {
            throw new AnneException("收款计划已核销金额=" + receiptDetail.getVeriAmount() + ",不能小于核销产品明细的金额=" + sum);
        }

        PageCondition condition = new PageCondition();
        condition.getFilterObject().addCondition("bizId", "=", detailId);
        List<KstarAttachment> files = attachmentService.getAttachmentList(condition);
        if (files.size() == 0 && crd.getContractCode() != null && crd.getContractCode().startsWith("KSTAR-JH")) {
            throw new AnneException("请在核销合同明细里先上传附件后再进行核销收款");
        }
        return sum;
    }


    /**
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IReceiptsService#receiptsVerificationAutoService()
     */
    @Override
    public void receiptsVerificationAutoService() {
        String hql1 = "from Receipts r where r.status = ? and r.contractCode is not null ";
        List<Receipts> receiptsList = baseDao.findEntity(hql1, new Object[]{IConstants.RECEIPT_STATUS_20});

        if (receiptsList == null || receiptsList.size() == 0) {
            return;
        }

        for (Receipts receipts : receiptsList) {
            String customInfoId = getCustomInfoId(receipts);
            if (StringUtils.isEmpty(customInfoId)) {
                continue;
            }
            boolean isAgent = lovMemberService.isAgentForOrg(customInfoId);
            if (isAgent) {
                receiptsVerificationByCustomInfo(receipts);
            } else {
                receiptsVerificationByContract(receipts);
            }
        }
    }

    private String getCustomInfoId(Receipts receipts) {
        String customInfoId = null;
        customInfoId = receipts.getCorrectCustId();
        if (StringUtil.isEmpty(customInfoId)) {
            customInfoId = receipts.getPaymentCustomerId();
        }
        return customInfoId;
    }

    /**
     * 代理商按客户核销
     *
     * @param receipts
     */
    @Override
    public void receiptsVerificationByCustomInfo(Receipts receipts) {
        String customInfoId = getCustomInfoId(receipts);
        if (StringUtils.isEmpty(customInfoId)) {
            return;
        }

        List<Object> args = new ArrayList<>();
        StringBuffer hql = new StringBuffer(" from ContractReceiptDetail cr where 1=1 ");
        hql.append(" and cr.status != ? ");
        args.add(IConstants.RECEIPT_STATUS_40);

        hql.append(" and cr.status != ? ");
        args.add(IConstants.RECEIPT_STATUS_50);

        hql.append(" and cr.custId = ? ");
        args.add(customInfoId);

        if (StringUtil.isNotEmpty(receipts.getCurrency())) {
            hql.append(" and cr.receiptsType = ? ");
            args.add(receipts.getCurrency());
        }
        hql.append(" order by paymentDate asc, receiptsPlan asc");
        List<ContractReceiptDetail> contractReceiptDetails = baseDao.findEntity(hql.toString(), args.toArray());
        if (contractReceiptDetails == null || contractReceiptDetails.size() == 0) {
            return;
        }

        receiptsVerification(receipts, contractReceiptDetails);
    }

    /**
     * 按合同核销
     *
     * @param receipts
     */
    private void receiptsVerificationByContract(Receipts receipts) {

        if (StringUtils.isEmpty(receipts.getContractCode())) {
            return;
        }
        String customInfoId = getCustomInfoId(receipts);
        if (StringUtils.isEmpty(customInfoId)) {
            return;
        }

        List<Object> args = new ArrayList<>();
        StringBuffer hql = new StringBuffer(" from ContractReceiptDetail cr where 1=1 ");
        hql.append(" and cr.status != ? ");
        args.add(IConstants.RECEIPT_STATUS_40);

        hql.append(" and cr.status != ? ");
        args.add(IConstants.RECEIPT_STATUS_50);

        hql.append(" and cr.custId = ? ");
        args.add(customInfoId);

        hql.append(" and cr.contractCode = ? ");
        args.add(receipts.getContractCode());

        if (StringUtil.isNotEmpty(receipts.getCurrency())) {
            hql.append(" and cr.receiptsType = ? ");
            args.add(receipts.getCurrency());
        }
        hql.append(" order by paymentDate asc, receiptsPlan asc");
        List<ContractReceiptDetail> contractReceiptDetails = baseDao.findEntity(hql.toString(), args.toArray());
        if (contractReceiptDetails == null || contractReceiptDetails.size() == 0) {
            return;
        }

        receiptsVerification(receipts, contractReceiptDetails);
    }

    /**
     * 对回款计划进行核销
     *
     * @param receipts
     * @param contractReceiptDetails
     */
    private void receiptsVerification(Receipts receipts, List<ContractReceiptDetail> contractReceiptDetails) {
        //总核销金额
        BigDecimal totalVeriAmount = receipts.getNotVeriAmount();
        BigDecimal veriAmount = new BigDecimal(0);
        for (ContractReceiptDetail cr : contractReceiptDetails) {
        	if(cr.getRemarks() != null && cr.getRemarks().contains("期初")){
        		cr.setSalesSort("Y");
        	}
            BigDecimal amount = cr.getPlanAmount().subtract(cr.getVeriAmount()); //未核销金额
            if (amount.doubleValue() <= 0) {
                continue;
            }
            if (amount.compareTo(totalVeriAmount) >= 0) {
                //更新合同计划明细核销金额
                cr.setAmount(totalVeriAmount);
                this.updateContractReceiptDetailVeriAmount(cr, null);
                //生成核销明细
                this.createVerificationDetail(receipts, cr);
                veriAmount = veriAmount.add(totalVeriAmount);
                totalVeriAmount = totalVeriAmount.subtract(totalVeriAmount);
            } else {
                //更新合同计划明细核销金额
                cr.setAmount(amount);
                this.updateContractReceiptDetailVeriAmount(cr, null);
                //生成核销明细
                this.createVerificationDetail(receipts, cr);
                veriAmount = veriAmount.add(amount);
                totalVeriAmount = totalVeriAmount.subtract(amount);
            }
            if (totalVeriAmount.compareTo(new BigDecimal(0)) <= 0) {
                break;
            }
        }
        //更新收款单核销金额
        this.updateReceiptsVeriAmount(receipts, veriAmount, null);
    }

    /**
     * 回款计划导出
     * @param ids
     * @return
     * @throws AnneException
     */
    @Override
    public List<List<Object>> exportSelectedReceiptLists(PageCondition condition,UserObject user,String[] ids,String typ) throws AnneException {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addHead(lstOut);
        List<ContractReceiptDetail> lines = getSelectedReceiptList(condition, user, ids, typ);
        contractReceiptDetailService.searchGatheringDateAndCheckDate(lines);
        for (ContractReceiptDetail receiptDetail : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(receiptDetail.getBusinessEntityLable());
            lstIn.add(receiptDetail.getCustCode());
            lstIn.add(receiptDetail.getCustName());
            lstIn.add(receiptDetail.getContractCode());
            lstIn.add(receiptDetail.getReceiptsPlanLable());
            lstIn.add(receiptDetail.getReceiptsStageLable());
            lstIn.add(receiptDetail.getDeliveryCode());
            lstIn.add(receiptDetail.getPlanAmount());
            lstIn.add(receiptDetail.getVeriAmount());
            lstIn.add(receiptDetail.getGridBalance());
            lstIn.add(receiptDetail.getReceiptsTypeLable());
            lstIn.add(receiptDetail.getPaymentDate());
            lstIn.add(receiptDetail.getGatheringDate());
            lstIn.add(receiptDetail.getCheckDate());
            lstIn.add(receiptDetail.getSalesmanCode());
            lstIn.add(receiptDetail.getSalesmanName());
            lstIn.add(receiptDetail.getSalesmanPosName());
            lstIn.add(receiptDetail.getRemarks());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    private void addHead(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("业务实体");
        lstHead.add("客户编号");
        lstHead.add("客户名称");
        lstHead.add("合同编号");
        lstHead.add("合同收款计划行");
        lstHead.add("收款阶段");
        lstHead.add("出货申请编号");
        lstHead.add("计划金额");
        lstHead.add("核销金额");
        lstHead.add("未核销金额");
        lstHead.add("币种");
        lstHead.add("到期日");
        lstHead.add("收款日期");
        lstHead.add("核销日期");
        lstHead.add("销售人员编号");
        lstHead.add("销售人员名称");
        lstHead.add("销售人员岗位");
        lstHead.add("备注");
        lstOut.add(lstHead);
    }

    private List<ContractReceiptDetail> getSelectedReceiptList(PageCondition condition,UserObject user,String[] ids,String typ) {
    	FilterObject fo = condition.getFilterObject(ContractReceiptDetail.class);
		HqlObject ho = HqlUtil.getHqlObject(fo);
		String hql_search = ho.getHql();

		String sel_hql = "";
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}

		StringBuffer hql = new StringBuffer(sel_hql);
    	String idsStr = "";
        for (String id : ids) {
            idsStr += "'" + id + "',";
        }
        idsStr = idsStr.substring(0, idsStr.length() - 1);
        if (!StringUtil.isNullOrEmpty(idsStr) && !"''".equals(idsStr)) {
			hql.append(" AND contractreceiptdetail.id in ("+ idsStr +")");
		}
        hql.append(" order by contractreceiptdetail.createdAt desc ");
        return baseDao.findEntity(hql.toString());
    }

	
    /**
     * 校验回款计划是否已核销
     */
    @Override
	public List<ContractReceiptDetail> checkedReceiptsPlan(String contrNo, String deliveryCode) {
		// TODO Auto-generated method stub
    	List<Object> args = new ArrayList<>();
    	String hql = " from ContractReceiptDetail where 1=1 ";
    		hql += " and (contractCode = ? or deliveryCode = ?) ";
    		hql += " and veriAmount != 0 ";
    	args.add(contrNo);
    	args.add(deliveryCode);
    	
		return baseDao.findEntity(hql, args.toArray());
	}

	@Override
	public List<Receipts> queryReceiptsList(PageCondition condition, UserObject userObject) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(Receipts.class);
        filterObject.addOrderBy("receiptsDate", "asc");
        String type = condition.getStringCondition("type");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        String hql = hqlObject.getHql();

        //收款核销按岗位上下级进行权限控制
        String op = condition.getStringCondition("op");
        if ("verification".equals(op)) {
            String as = Receipts.class.getSimpleName().toLowerCase();
            String permissionHQL = " and " + as + ".salesmanId in (";
            //language=HQL
            permissionHQL += " select u.id from LovMember o,LovMember p,User u,UserPermission up " +
                    "where u.id = up.userId and up.type = 'P' and up.memberId=p.id and p.optTxt1=o.id and u.id="+as+".salesmanId";
            permissionHQL += " and " + PermissionUtil.getPermissionHQL(null, "u.id", "p.id", "o.id", as + ".id", userObject, "ReceiptsList");
            permissionHQL += ")";
            int index = hql.indexOf(" order by ");
            if (index >= 0) {
                StringBuilder s = new StringBuilder();
                s.append(hql.substring(0, index))
                        .append(permissionHQL)
                        .append(" order by ")
                        .append(hql.substring(index + 10, hql.length()));
                hql = s.toString();
            } else {
                hql = hql + permissionHQL;
            }
        }

        //是否只显示新建和退回状态的数据
        String onlyNewAndSendback = condition.getStringCondition("onlyNewAndSendback");
        if (Objects.equals(onlyNewAndSendback, "1")) {
            int index = hql.indexOf(" order by ");
            if (index >= 0) {
                StringBuilder s = new StringBuilder();
                s.append(hql.substring(0, index))
                        .append(" and (status=? or status=?) ")
                        .append(" order by ")
                        .append(hql.substring(index + 10, hql.length()));
                hql = s.toString();
            } else {
                hql = hql + " and (status=? or status=?) ";
            }

            hqlObject.addArgs(IConstants.RECEIPT_STATUS_10);
            hqlObject.addArgs(IConstants.RECEIPT_STATUS_R10);
        }

        //是否只显示更新用户名称为空的数据
        String isHaveErpCust = condition.getStringCondition("isHaveErpCust");
        if (Objects.equals(isHaveErpCust, "1")) {
            int index = hql.indexOf(" where 1=1 ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where 1=1 ")
                    .append(" and correctCustName is null ")
                    .append(hql.substring(index + 11, hql.length()));
            hql = s.toString();
        }
        
        //是否只显示更正用户名称是销售人员更正得数据
        String isHaveChange = condition.getStringCondition("isHaveChange");
        if (Objects.equals(isHaveChange, "1")) {
            int index = hql.indexOf(" where 1=1 ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where 1=1 ")
                    .append(" and changeCustMember != null ")
                    .append(hql.substring(index + 11, hql.length()));
            hql = s.toString();
        }
        if (Objects.equals(type, "RECEVICE")) {
            int index = hql.indexOf(" where ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where ")
                    .append(" salesmanId is null and ")
                    .append(" correctCustId is null and ")
                    .append(" erpCust = 'No' and ")
                    .append(" status = '20' and ")
                    .append(hql.substring(index + 7, hql.length()));
            hql = s.toString();
        }

        return baseDao.findEntity(hql, hqlObject.getArgs());
	}

	@Override
	public List<List<Object>> mappageExport(PageCondition condition, UserObject userObject) {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		lstOut.add(buildHead());
		
		List<Receipts> list = queryReceiptsList(condition, userObject);
		if(list != null && list.size() > 0){
			for(Receipts receipts : list){
				List<Object> lstIn = new ArrayList<Object>();
				lstIn.add(receipts.getBusinessName());
				lstIn.add(receipts.getReceiptsCode());
				lstIn.add(receipts.getReceiptsDate());
				lstIn.add(receipts.getVeriAmount());
				lstIn.add(receipts.getNotVeriAmount());
				lstIn.add(receipts.getPaymentCustomerName());
				lstIn.add(receipts.getErpCust());
				lstIn.add(receipts.getCorrectCustName());
				lstIn.add(receipts.getSalesCenterLable());
				lstIn.add(receipts.getBizDeptLable());
				lstIn.add(receipts.getSalesmanName());
				lstIn.add(receipts.getSalesmanPostLabel());
				lstIn.add(receipts.getArrivalAmount());
				lstIn.add(receipts.getCurrency());
				lstIn.add(receipts.getIsAdvancesReceived());
				lstIn.add(receipts.getAmount());
				lstIn.add(receipts.getPoundage());
				lstIn.add(receipts.getFreight());
				lstIn.add(receipts.getContractCode());
				lstIn.add(receipts.getRemarks());
				lstIn.add(receipts.getPic());
				lstIn.add(receipts.getPicSaleCenterLable());
				lstIn.add(receipts.getRespDeptLable());
				lstIn.add(receipts.getErpImpLable());
				lstIn.add(receipts.getReceiptsTypeLable());
				lstIn.add(receipts.getReceiptsBank());
				lstIn.add(receipts.getBankAccount());
				lstIn.add(receipts.getStatusLable());
				lstIn.add(receipts.getCreator());
				lstIn.add(receipts.getCreatedPositionName());
				lstIn.add(receipts.getCreatedAt());
				lstOut.add(lstIn);
			}
		}
		
		return lstOut;
	}

	private List<Object> buildHead() {
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("业务实体名称");
		lstHead.add("收款单号");
		lstHead.add("收款日期");
		lstHead.add("已核销金额");
		lstHead.add("未核销金额");
		lstHead.add("付款客户");
		lstHead.add("是否ERP客户");
		lstHead.add("更正客户名称");
		lstHead.add("营销中心");
		lstHead.add("业务部门");
		lstHead.add("销售人员");
		lstHead.add("销售人员岗位");
		lstHead.add("到账金额");
		lstHead.add("币种");
		lstHead.add("是否预收款");
		lstHead.add("合计");
		lstHead.add("手续费");
		lstHead.add("代垫运费收款");
		lstHead.add("合同编号");
		lstHead.add("备注");
		lstHead.add("负责人");
		lstHead.add("负责人营销中心");
		lstHead.add("负责部门");
		lstHead.add("ERP已导入");
		lstHead.add("收款分类");
		lstHead.add("收款银行");
		lstHead.add("银行账号");
		lstHead.add("状态");
		lstHead.add("创建人");
		lstHead.add("创建人岗位");
		lstHead.add("创建时间");
		return lstHead;
	}

    
}