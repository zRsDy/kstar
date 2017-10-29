package com.ibm.kstar.impl.contract;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.order.OrderHeader;
import org.codehaus.jackson.mrbean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.quot.KstarMemInfo;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.permission.utils.PermissionUtil;


/**
 * @author Joe
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrChangeServiceImpl implements IContrChangeService {


    @Autowired
    ICorePermissionService corePermissionService;

    @Autowired
    BaseDao baseDao;

    @Autowired
    private IContractService contractService;
    @Autowired
    private ILovMemberService lovMemberService;
    @Autowired
    IProcessService processService;
    @Autowired
    IProLovService proLovService;
    @Autowired
    protected ITeamService teamService;
    @Autowired
    protected IOrgTeamService orgTeamService;
    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;
    @Autowired
    IOrderService orderService;
    @Autowired
    private ICustomInfoService customerService;


    @Override
    public IPage query(PageCondition condition) {


        FilterObject filterObject = condition.getFilterObject(ContrChange.class);
        filterObject.addOrderBy("createdAt", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());

        //		StringBuffer hql = new StringBuffer(" from ContrChange ");
        //		return baseDao.search(hql.toString(),condition.getRows(), condition.getPage());
    }

    @Override
    public void save(ContrChange contrChange, UserObject userObject) throws AnneException {

        // 创建字段
        contrChange.setRecordInfor(false, userObject);
        contrChange.setSubmiteTime(new Date());
        baseDao.save(contrChange);
        Contract contract = contractService.get(contrChange.getContrId());
        //		updateContractValidFlag(contract.getId());
        //		contractService.copyLovMemberByMemo(contract.getId(), contrChange.getId());
        contractService.copyPrjlstById(contract.getId(), contrChange.getId(), IConstants.CONTR_CHANGE);
        updatePricInfor(contrChange.getId());
        contractService.copyAttachmentById(contract.getId(), contrChange.getId(), IConstants.CONTR_CHANGE);
        //		contractService.copyMemById(contract.getId(), contrChange.getId(), IConstants.CONTR_CHANGE);

        //		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(),
        //				IConstants.CONTR_CHANGE,contrChange.getId());
        //
        //		orgTeamService.configPrimaryOrg(contrChange.getId(), IConstants.CONTR_CHANGE, userObject.getOrg().getId());

        String sourceBizType = contractService.getContrBusinessTypeById(contract.getId());
        String targetBizType = IConstants.CONTR_CHANGE;
        //复制权限信息
        teamService.copyPositionNoAuth(contract.getId(), sourceBizType, contrChange.getId(), targetBizType, userObject.getEmployee().getId());
        orgTeamService.copyPrimaryOrg(contract.getId(), sourceBizType, contrChange.getId(), targetBizType, userObject.getEmployee().getId());

    }

    private void updateContractValidFlag(String contrId) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update crm_t_contr_basic set C_IS_CHANGE='1' where C_ID ='" + contrId + "'");
        baseDao.executeSQL(updateSql.toString());
    }

    /*
     * 生成变更单的工程清单后将原合同中的价格信息复制到新价格字段信息中
     *
     *
     */
    private void updatePricInfor(String changeId) {
        ContrChange contrChg = get(changeId);
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("quotCode", "=", changeId);
        List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
        for (KstarPrjLst prl : plist) {
            //			prl.setCType("CONTR_CHANGE");
            prl.setBuzCode(contrChg.getChangeNo());
            //			prl.setPrdTmpPrc(prl.getPrdPrc());
            //			Double finlPrc = prl.getApprovePrc();
            //			if(finlPrc == null ){
            //				finlPrc = prl.getPrdPrc();
            //			}
            //			prl.setPrdPrc(finlPrc);

            prl.setNewAmt(prl.getAmt());
            //			prl.setNewPrdPrc(finlPrc);
            prl.setNewPrdPrc(prl.getPrdPrc());
            prl.setNewTtlUnt(prl.getTtlUnt());
            //			prl.setNewAvgTtl(prl.getAvgTtl());
            baseDao.update(prl);
        }
    }

    /*
     * 生成新合同后将变更单中新价格字段信息复制到合同旧价格字段信息中
     *
     */
    private List<KstarPrjLst> updatePricInforNewContr(String contrId, String typ, String contrNo) {
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("quotCode", "=", contrId);
        List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
        List<KstarPrjLst> chgedList = new ArrayList();
        for (KstarPrjLst prl : plist) {
            if (prl.getMaterCode() != null && prl.getLineNum() != null) {
                chgedList.add(prl);
            }
            //			if(prl.getUpdtFlag()!=null){
            if (prl.getNewAmt() != null) {
                if (prl.getNewAmt() == 0) {
                    prl.setCType(typ);
                    prl.setBuzCode(contrNo);
                    prl.setAmt(prl.getNewAmt());
                    prl.setPrdPrc(prl.getNewPrdPrc());
                    prl.setTtlUnt(prl.getNewTtlUnt());

                    prl.setNewAmt(0.0);
                    prl.setNewPrdPrc(0.0);
                    prl.setPrdTmpPrc(0.0);
                    prl.setNewTtlUnt(0.0);
                    // 框架协议，新数量为0不做删除
                    if (prl.getAmt() == null) {
                        baseDao.save(prl);
                        continue;
                    }
                    if (prl.getAmt() == 0) {
                        baseDao.save(prl);
                        continue;
                    }

                    baseDao.delete(prl);
                } else {
                    prl.setCType(typ);
                    prl.setBuzCode(contrNo);
                    prl.setAmt(prl.getNewAmt());
                    //					if(prl.getApprovePrc() == null){
                    //						prl.setPrdPrc(prl.getNewPrdPrc());
                    //					}else{
                    //						prl.setPrdPrc(prl.getPrdTmpPrc());
                    //						prl.setApprovePrc(prl.getNewPrdPrc());
                    //					}
                    //					if(prl.getPrdSprc()!=null){
                    //						prl.setApplyDiscount(prl.getPrdPrc()/prl.getPrdSprc());
                    //						prl.setApproveDiscount(prl.getApprovePrc()/prl.getPrdSprc());
                    //					}
                    prl.setPrdPrc(prl.getNewPrdPrc());
                    prl.setTtlUnt(prl.getNewTtlUnt());
                    //					prl.setAvgTtl(prl.getNewAvgTtl());
                    prl.setNewAmt(0.0);
                    prl.setNewPrdPrc(0.0);
                    prl.setPrdTmpPrc(0.0);
                    prl.setNewTtlUnt(0.0);
                    //					prl.setNewAvgTtl(0.0);
                    //					prl.setUpdatType(null);
                    //					prl.setUpdtFlag(null);
                    baseDao.saveOrUpdate(prl);
                }
            }
            //			}else{
            //				prl.setCType(typ);
            //				prl.setBuzCode(contrNo);
            //				baseDao.saveOrUpdate(prl);
            //			}
        }
        return chgedList;
    }

    /*
     * 生成新合同后将变更单中新价格字段信息复制到旧合同的旧价格字段信息中
     *
     */
    private List<KstarPrjLst> updatePricInforOldContr(String contrId, String chgId, String typ, String contrNo) {
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("quotCode", "=", contrId);
        List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
        Condition condition1 = new Condition();
        condition1.getFilterObject().addCondition("quotCode", "=", chgId);
        List<KstarPrjLst> chglist = contractService.getPrjlstList(condition1);
        List<KstarPrjLst> chgedList = new ArrayList();
        for (KstarPrjLst prj : chglist) {
            if (prj.getLineNum() == null || prj.getMaterCode() == null) {
                continue;
            }
            String flg = "N";
            for (KstarPrjLst prl : plist) {
                if (prl.getLineNum() == null || prl.getMaterCode() == null) {
                    continue;
                }
                if (prj.getLineNum().equalsIgnoreCase(prl.getLineNum())) {
                    if (prj.getUpdtFlag() != null) {
                        chgedList.add(prj);
                        if (prj.getNewAmt() == 0) {
                            lovMemberService.delete(prl.getLvId());
                            baseDao.delete(prl);
                            flg = "Y";
                            break;
                        } else {
                            //							prl.setCType(typ);
                            //							prl.setBuzCode(contrNo);
                            prl.setAmt(prj.getNewAmt());
                            prl.setPrdPrc(prj.getNewPrdPrc());
                            prl.setTtlUnt(prj.getNewTtlUnt());
                            prl.setAvgTtl(prj.getNewAvgTtl());
                            prl.setNewAmt(0.0);
                            prl.setNewPrdPrc(0.0);
                            prl.setNewTtlUnt(0.0);
                            prl.setNewAvgTtl(0.0);
                            prl.setUpdatType(null);
                            prl.setUpdtFlag(null);
                            baseDao.update(prl);
                            flg = "Y";
                            break;
                        }
                    }
                } else {
                    flg = "N";
                    continue;
                }
            }
            if (flg == "N") {
                KstarPrjLst prl = new KstarPrjLst();
                BeanUtils.copyPropertiesIgnoreNull(prj, prl);
                prl.setId(null);
                prl.setQuotCode(contrId);
                prl.setLineNum(prj.getLineNum());
                prl.setCType(typ);
                prl.setBuzCode(contrNo);
                prl.setAmt(prj.getNewAmt());
                prl.setPrdPrc(prj.getNewPrdPrc());
                prl.setTtlUnt(prj.getNewTtlUnt());
                prl.setAvgTtl(prj.getNewAvgTtl());
                prl.setNewAmt(0.0);
                prl.setNewPrdPrc(0.0);
                prl.setNewTtlUnt(0.0);
                prl.setNewAvgTtl(0.0);
                prl.setUpdatType(null);
                prl.setUpdtFlag(null);
                baseDao.save(prl);
            }

        }
        return chgedList;
    }

    @Override
    public ContrChange get(String id) throws AnneException {
        if (id == null) {
            return null;
        }
        return baseDao.findUniqueEntity(" from ContrChange where id = ? ", id);
    }

    @Override
    public void update(ContrChange contrChange) throws AnneException {
        ContrChange old = baseDao.get(ContrChange.class, contrChange.getId());
        if (old == null) {
            throw new AnneException(IContrChangeService.class.getName() + " saveQuot : 没有找到要更新的数据");
        }

        if (!contrChange.getId().equals(old.getId())) {
            throw new AnneException("合同单ID不能被修改");
        }

        BeanUtils.copyPropertiesIgnoreNull(contrChange, old);
        old.setSubmiteTime(new Date());
        baseDao.update(old);
        Long count = baseDao.findUniqueEntity("select count(*) from ContrChange where C_ID = ? ", contrChange.getId());
        if (count > 1) {
            throw new AnneException("变更单ID已经存在!");
        }

        Contract contract = contrChange.getContract();
        if (contract != null) {
            this.updateOrder(contrChange);
        }

    }


    public void deletePrjLstByQid(String qId) throws AnneException {
        List<Object> args = new ArrayList<Object>();
        args.add(qId);
        baseDao.executeHQL(" delete KstarPrjLst pl where pl.quotCode = ? ", args.toArray());
    }


    public void deleteAllPrjLstLovMemByQid(String qId) throws AnneException {
        List<Object> args = new ArrayList<Object>();
        args.add(qId);
        baseDao.executeHQL(" delete LovMember l where l.groupCode = 'PRJLSTPRDCAT' and l.memo = ?  ", args.toArray());
    }

    public void delete(String id) throws AnneException {
        baseDao.executeHQL(" delete ContrChange  where id = ? ", new Object[]{id});
    }

    @Override
    public String getContracChangetNumber() throws AnneException {
        //		return baseDao.get(KstarMemInfo.class);
        String connum = "";
        @SuppressWarnings("deprecation")

        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_contract_change_num(?)}";
        //	        CallableStatement sta = conn.prepareCall(sql);
        //	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
        //            sta.setInt(2, -1);
        //	        sta.execute();
        //	        connum = sta.getString(1);
        //
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //        }
        Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];
        return connum;
    }


    /**
     * 变更完成，签订，合同版本升级
     */
    @Override
    public void signUpContract(UserObject user, String changeId) throws AnneException {
        ContrChange old = baseDao.get(ContrChange.class, changeId);
        if (old == null) {
            throw new AnneException(IContrChangeService.class.getName() + " saveQuot : 没有找到要更新的数据");
        }
        //		BeanUtils.copyPropertiesIgnoreNull(contract, old);

        //old.setCreateTime(new Time(0));
        old.setActSignDate(new Date());
        LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");    // 10  已签订
        old.setChangeStat(statLov.getId());
        baseDao.update(old);
        Long count = baseDao.findUniqueEntity("select count(*) from Contract where id = ? ", changeId);
        if (count > 1) {
            throw new AnneException("合同单ID已经存在!");
        }
        Contract oldContr = contractService.get(old.getContrId());
        if (oldContr == null) {
            throw new AnneException(IContrChangeService.class.getName() + "  合同变更签订出错 : 没有找到要更新原合同数据");
        }
        LovMember chgTypeLov = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "08"); // 变更类型为 "废弃" 时修改原合同为 "废弃",且不生成新合同
        if (old.getChangeType().equalsIgnoreCase(chgTypeLov.getId())) {

            LovMember contrDisuseLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");//09	已废弃
            oldContr.setContrStat(contrDisuseLov.getId());
            oldContr.setIsValid("0");
            oldContr.setTotalAmt(0d);
            baseDao.update(oldContr);

            List<KstarPrjLst> kstarPrjLsts = baseDao.findEntity("from KstarPrjLst where quotCode = ? ", oldContr.getId());
            for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {
                kstarPrjLst.setAmt(0d);
                kstarPrjLst.setTtlUnt(0d);
                kstarPrjLst.setNotVeriNum(0d);
            }
            baseDao.update(kstarPrjLsts);

            orderService.updateOrderLinesByContractChange(oldContr.getId(), kstarPrjLsts, user, old.getChangeType());

        } else {
            /*Contract nContr = new Contract();
//			contractService.copyContract(nContr, oldContr, user);
			BeanUtils.copyProperties(oldContr, nContr);
			nContr.setId(null);
			String oldVer=oldContr.getContrVer();
			Integer newVer = Integer.parseInt(oldVer)+1;		
			nContr.setContrVer(newVer.toString());
			nContr.setTotalAmt(old.getTotalAmt());
			nContr.setPayWay(old.getPayWay());
			nContr.setCustCode(old.getCustCode());
			nContr.setBussEnity(old.getBussEnity());
			nContr.setDelivDate(old.getDelivDate());
//			nContr.setIsValid("1"); 
			baseDao.save(nContr);
			String bizType= contractService.getContrBusinessTypeById(nContr.getId());

			contractService.copyLovMemberByMemo(old.getId(), nContr.getId());

			Condition condition = new Condition();
			condition.getFilterObject().addCondition("quotCode", "=", nContr.getId());
			List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
			updatePricInforNewContr(nContr.getId(),bizType,nContr.getContrNo());
			
//			orderService.up

			String sourceBizType=IConstants.CONTR_CHANGE;
			String targetBizType=contractService.getContrBusinessTypeById(nContr.getId());
			//复制权限信息
			teamService.copyPositionNoAuth(old.getId(),sourceBizType,nContr.getId(),targetBizType,user.getEmployee().getId());
			orgTeamService.copyPrimaryOrg(old.getId(),sourceBizType,nContr.getId(),targetBizType,user.getEmployee().getId());
				

            contractService.copyAddrById(oldContr.getId(), nContr.getId(), bizType);
            contractService.copyAttachmentById(oldContr.getId(), nContr.getId(), bizType);
            contractService.copyPayPlanById(oldContr.getId(), nContr.getId());
//			copyLovMemberByMemo(old.getId(), contract.getId()); 
			contractService.copyPrjdtl(oldContr.getId(),nContr.getId(),bizType);*/

            // ---------复制原合同作为备份--------------

            Contract nContr = new Contract();
            //			contractService.copyContract(nContr, oldContr, user);
            BeanUtils.copyProperties(oldContr, nContr);
            nContr.setId(null);
            nContr.setIsValid("0"); // 失效
            //			String oldVer=oldContr.getContrVer();
            //			Integer newVer = Integer.parseInt(oldVer)+1;
            //			nContr.setContrVer(newVer.toString());
            //			nContr.setTotalAmt(old.getTotalAmt());
            //			nContr.setPayWay(old.getPayWay());
            //			nContr.setCustCode(old.getCustCode());
            //			nContr.setBussEnity(old.getBussEnity());
            //			nContr.setDelivDate(old.getDelivDate());
            baseDao.save(nContr);
            //			contractService.copyLovMemberByMemo(oldContr.getId(), nContr.getId());
            contractService.copyPrjlstById(oldContr.getId(), nContr.getId(), "");
            String bizType = contractService.getContrBusinessTypeById(oldContr.getId());

            //			contractService.copyLovMemberByMemo(old.getId(), nContr.getId());

            //			Condition condition = new Condition();
            //			condition.getFilterObject().addCondition("quotCode", "=", nContr.getId());
            //			List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
            //			updatePricInforNewContr(nContr.getId(),bizType,nContr.getContrNo());

            //			orderService.up

            String sourceBizType = IConstants.CONTR_CHANGE;
            //			String targetBizType=contractService.getContrBusinessTypeById(nContr.getId());
            //复制权限信息
            teamService.copyPositionNoAuth(oldContr.getId(), bizType, nContr.getId(), bizType, user.getEmployee().getId());
            orgTeamService.copyPrimaryOrg(oldContr.getId(), bizType, nContr.getId(), bizType, user.getEmployee().getId());


            contractService.copyAddrById(oldContr.getId(), nContr.getId(), bizType);
            contractService.copyAttachmentById(oldContr.getId(), nContr.getId(), bizType);
            contractService.copyPayPlanById(oldContr.getId(), nContr.getId());
            //			copyLovMemberByMemo(old.getId(), contract.getId());
            contractService.copyPrjdtl(oldContr.getId(), nContr.getId(), bizType);


            // ---------更新原版本合同的数据信息并升版 ---------
            String oldVer = oldContr.getContrVer();
            Integer newVer = Integer.parseInt(oldVer) + 1;
            oldContr.setContrVer(newVer.toString());
            oldContr.setTotalAmt(old.getTotalAmt());
            oldContr.setPayWay(old.getPayWay());
            //			oldContr.setCustCode(old.getCustCode());
            oldContr.setBussEnity(old.getBussEnity());
            oldContr.setDelivDate(old.getDelivDate());
            if (!StringUtil.isNullOrEmpty(old.getCurrency())) {
                oldContr.setCurrency(old.getCurrency());
            }
            if (!StringUtil.isNullOrEmpty(old.getIsSealFirst())) {
                oldContr.setIsSealFirst(old.getIsSealFirst());
            }

            oldContr.setIsChange("1"); // 变更
            oldContr.setIsValid("1"); // 有效
            contractService.update(oldContr, null);
            //			deleteAllPrjLstLovMemByQid(oldContr.getId());

            //将变更合并到合同中。
            mergeChange(old, oldContr);
            //            deletePrjLstByQid(oldContr.getId());
            //            contractService.copyPrjlstById(old.getId(), oldContr.getId(), "");

            //contractService.copyLovMemberByMemo(old.getId(), oldContr.getId());
            List<KstarPrjLst> ordChgedList = updatePricInforNewContr(oldContr.getId(), bizType, oldContr.getContrNo());
            //			List<KstarPrjLst>  chgedList = updatePricInforOldContr(oldContr.getId(),old.getId(),bizType,oldContr.getContrNo());
            //更新合同的权限信息
            teamService.deletePosition(oldContr.getId(), bizType);
            orgTeamService.deleteOrg(oldContr.getId(), bizType);
            teamService.copyPositionNoAuth(old.getId(), sourceBizType, oldContr.getId(), bizType, user.getEmployee().getId());
            orgTeamService.copyPrimaryOrg(old.getId(), sourceBizType, oldContr.getId(), bizType, user.getEmployee().getId());

            // ------------- 更新订单信息 -------------
            orderService.updateOrderLinesByContractChange(oldContr.getId(), ordChgedList, user, old.getChangeType());

            //20170626修复状态BUG
            long c = baseDao.findUniqueEntity(" select count(*) from KstarPrjLst l where l.quotCode = ? and l.updatType = '新增' ", oldContr.getId());
            if (c > 0) {
                LovMember lovType = lovMemberService.get(oldContr.getContrType());

                if ("CONTR_STAND-0102".equals(lovType.getCode()) ||
                        "CONTR_STAND-0202".equals(lovType.getCode())) {
                    // 框架合同 - 已签订
                    LovMember t = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");
                    oldContr.setContrStat(t.getId());
                } else if ("CONTR_STAND-0101".equals(lovType.getCode()) ||
                        "CONTR_STAND-0103".equals(lovType.getCode()) ||
                        "CONTR_STAND-0201".equals(lovType.getCode())) {
                    LovMember t = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");
                    oldContr.setContrStat(t.getId());
                } else {
                    LovMember t = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "12");
                    oldContr.setContrStat(t.getId());
                }
                baseDao.update(oldContr);
            }
        }


        //修改数量后需要根据可下单数量来更合同状态。可下单数量为0，则更新为已下单，否则更新为待下单
        OrderHeader orderHeader = orderService.queryOrderHeaderBySourceId(oldContr.getId());
        if (orderHeader != null) {
            contractService.updateContractStatByAmt(orderHeader.getId(), oldContr.getId());
        }
    }

    private void mergeChange(ContrChange theNew, Contract theOld) {
        List<KstarPrjLst> oldPrjList = baseDao.findEntity("from KstarPrjLst where quotCode = ? ", theOld.getId());
        List<KstarPrjLst> newPrjList = baseDao.findEntity("from KstarPrjLst where quotCode = ? ", theNew.getId());
        Map<String, KstarPrjLst> oldPrjMap = new HashMap<>();
        for (KstarPrjLst oldPrj : oldPrjList) {
            oldPrjMap.put(oldPrj.getLineNum(), oldPrj);
        }

        List<KstarPrjLst> updated = new ArrayList<>();
        List<KstarPrjLst> added = new ArrayList<>();
        for (KstarPrjLst newPrj : newPrjList) {
            String lineNum = newPrj.getLineNum();
            if (oldPrjMap.containsKey(lineNum)) {
                KstarPrjLst oldPrj = oldPrjMap.get(lineNum);
                BeanUtils.copyProperties(newPrj, oldPrj, new String[]{"id", "quotCode"});
                updated.add(oldPrj);
            } else {
                KstarPrjLst newPrj_clone = new KstarPrjLst();
                BeanUtils.copyProperties(newPrj, newPrj_clone, new String[]{"id", "quotCode"});
                newPrj_clone.setQuotCode(theOld.getId());
                added.add(newPrj_clone);
            }
        }

        List<KstarPrjLst> deleted = new ArrayList<>();
        deleted.addAll(oldPrjList);
        deleted.removeAll(updated);

        baseDao.save(added);
        baseDao.update(updated);
        for (KstarPrjLst prj : deleted) {
            baseDao.delete(prj);
        }
    }

    private void copyMemFromContr(String oldId, String newId) {
        Condition condition = new Condition();
        condition.getFilterObject().addCondition("quotCode", "=", oldId);
        List<KstarMemInfo> memList = contractService.getMemList(condition);
        for (KstarMemInfo mem : memList) {
            KstarMemInfo newObj = new KstarMemInfo();
            BeanUtils.copyProperties(mem, newObj);
            newObj.setId(null);
            newObj.setQuotCode(newId);
            //			newObj.setCType("CONTR_CHANGE");
            baseDao.save(newObj);
        }
    }

    @Override
    public void startContrChgTrialProcess(UserObject user, String contrId, String typ) {

        String businessId = contrId; //StringUtil.getUUID();
        ContrChange chg = get(contrId);
        String tolAmt = chg.getTotalAmtDesc();
        String application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_TRIAL_PROC, typ);
        String model = lovMemberService.getFlowCodeByAppCode(application);
        String bizNo = chg.getChangeNo();
        Contract contr = contractService.get(chg.getContrId());
        String salesCenter = lovMemberService.getSaleCenter(contr.getMarketDept());//lovMemberService.getSaleCenter(user.getOrg().getId());
        Date now = new Date();
        SimpleDateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String ti = contractService.getTitleByAppName(application);
        String title = ti + " - " + bizNo + " - " + user.getEmployee().getName() + " - " + dtFmt.format(now);
        Map<String, String> varmap = new HashMap<>();
        varmap.put("title", title);//"合同申请初审流程 - "+user.getEmployee().getName()+" - "+dtFmt.format(now));
        varmap.put("app", application);
        varmap.put("typ", typ);
        varmap.put("SalesCenter", salesCenter);
        varmap.put("Contract_TotalAmt", tolAmt);
        if (contr != null) {
            varmap.put("employeeIdInForm", contr.getOrderer());//下单商务专员
            varmap.put("employeeNameInForm", contr.getOrdererName());
        }

        varmap.put("CONTRACTTYPE", contr.getContrTypeName());

        CustomInfo customInfo = customerService.getCustomInfo(contr.getCustCode());
        if (customInfo != null) {
            varmap.put("customCategorySubName", customInfo.getCustomCategorySubName());
        }

        //		processService.start(model, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
        xflowProcessServiceWrapper.start(model, businessId, user, varmap);
        // 合同评审状态
        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
        //		updateContrChgReviewStatus(contrId, lov.getId());
        updateContrChgTrialStatus(contrId, lov.getId());
        // 合同状态更新
        LovMember stdLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "02");// 02	待初审

        updateContrChgStatus(contrId, stdLov.getId());

    }

    @Override
    public void updateContrChgTrialStatus(String id, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update CRM_T_CONTR_CHANGE set C_TRIAL_STAT='" + status + "' where C_ID ='" + id + "'");
        baseDao.executeSQL(updateSql.toString());
    }

    @Override
    public void updateContrChgReviewStatus(String id, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update CRM_T_CONTR_CHANGE set C_REVIEW_STAT='" + status + "' where C_ID ='" + id + "'");
        baseDao.executeSQL(updateSql.toString());
    }

    @Override
    public void updateContrChgStatus(String id, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update CRM_T_CONTR_CHANGE set C_CHANGE_STAT='" + status + "' where C_ID ='" + id + "'");
        baseDao.executeSQL(updateSql.toString());
    }

    @Override
    public KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException {

        List<Object> args = new ArrayList<Object>();
        args.add(qid);
        args.add(lvId);
        String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? ", args.toArray());

        KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);

        return prjLst;
    }

    @Override
    public KstarPrjLst getKstarPrjLstById(String id) throws AnneException {

        KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, id);

        return prjLst;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IPage queryPrjLst(PageCondition condition) {

        //		FilterObject filterObject = condition.getFilterObject(PrjLstDtl.class);
        //		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        //		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());


        //		String quotCode = condition.getStringCondition("qid");
        String quotCode = condition.getStringCondition("contrId");
        //		String parentId = condition.getStringCondition("parentId");
        String searchKey = condition.getStringCondition("searchKey");

        StringBuffer hql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        //StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0003' and quotCode = ? ");
        //hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0003' and lst.quotCode = ? ");
        //hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.leafFlag = 'Y' and lov.id = lst.lvId and lst.CType = '0003' and lov.memo = ? and lst.quotCode = ? ");
        //		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0005' and lov.memo = ? and lst.quotCode = ? ");
        //		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lov.memo = ? and lst.quotCode = ? ");
        hql.append("select lst from KstarPrjLst lst where lst.quotCode = ? ");
        args.add(quotCode);
        //		args.add(quotCode);

        //		if (parentId!=null){
        //			hql.append(" and lov.path like ? ");
        //			args.add("%"+parentId+"%");
        //		}
        if (searchKey != null) {
            hql.append("  and ( lst.prdNm like ? or lst.prdTyp like ? ) ");
            args.add("%" + searchKey + "%");
            args.add("%" + searchKey + "%");
        }

        //		hql.append(" order by lov.codePath ");
        hql.append(" order by lst.prdCtgid, CAST(lst.lineNum as integer) ");
        IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        //		List<Object[]> list = (List<Object[]>) page.getList();
        //		((PageImpl)page).setList(BeanUtils.castList(PrjLstDtl.class,list));
        return page;


    }

    @Override
    public void updatePrjLst(KstarPrjLst prjLst) throws AnneException {

        //		lovMemberService.update(lovMember);
        //		String lvId = lovMember.getId();

        //		String currQuotId = prjLst.getQuotCode();
        //
        //		List<Object> args = new ArrayList<Object>();
        //		args.add(currQuotId);
        //		args.add(lvId);
        //		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? ",args.toArray());
        String prjLstId = prjLst.getId();

        KstarPrjLst oldPrjLst = baseDao.get(KstarPrjLst.class, prjLstId);

        if (oldPrjLst == null) {
            throw new AnneException(IQuotService.class.getName() + " savePrjLst : 没有找到要更新的数据");
        }

        //		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
        //			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
        //			prjLst.setTtlUnt(tmpttlUnt);
        //		}

        if ((prjLst.getNewAmt() != null) && (prjLst.getNewPrdPrc() != null)) {
            Double tmpttlUnt = prjLst.getNewAmt() * prjLst.getNewPrdPrc();
            prjLst.setNewTtlUnt(tmpttlUnt);
            prjLst.setNotVeriNum(prjLst.getNewAmt() - (prjLst.getVeriedNum() == null ? 0 : prjLst.getVeriedNum()));
        }

        BeanUtils.copyPropertiesIgnoreNull(prjLst, oldPrjLst);
        oldPrjLst.setId(prjLstId);
        baseDao.update(oldPrjLst);


        Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where id = ? ", prjLstId);
        if (count > 1) {
            throw new AnneException("ID已经存在!");
        }

        // 根据用户需求暂时取消， 2017.4.20
        //		updateAvgttl(lovMember, prjLst);

        //		Double totalAmt =(double) 0.0;
        //		if((Double)prjLst.getNewPrdPrc()!=null){
        //			//总金额
        ////			totalAmt = getTotalamount(prjLst.getQuotCode());
        //			totalAmt = getNewTotalamount(prjLst.getQuotCode());
        //
        //		}
        //
        //		ContrChange contrChg = get(prjLst.getQuotCode());
        //		contrChg.setTotalAmt(totalAmt);
        //		update(contrChg);
    }

    @Override
    public void savePrjLst(KstarPrjLst prjLst, String cType) throws AnneException {
        if (StringUtil.isEmpty(prjLst.getQuotCode())) {
            throw new AnneException("报价单编号不能为空");
        }
        prjLst.setCType(cType);

        //		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
        //			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
        //			prjLst.setTtlUnt(tmpttlUnt);
        //		}
        if ((prjLst.getNewAmt() != null) && (prjLst.getNewPrdPrc() != null)) {
            Double tmpttlUnt = prjLst.getNewAmt() * prjLst.getNewPrdPrc();
            prjLst.setNewTtlUnt(tmpttlUnt);
        }


        baseDao.save(prjLst);
    }

    //	@Override
    //	public KstarPrjLst getPrjLst(String quotCode,String orgPrdNm) throws AnneException {
    //
    //		List<Object> args = new ArrayList<Object>();
    //		args.add(quotCode);
    //		args.add(orgPrdNm);
    //		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and prdNm = ? ",args.toArray());
    //
    //		KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);
    //
    //		return prjLst;
    //
    //	}


    @Override
    public void deletePrjLst(String quotCode) throws AnneException {
        List<Object> args = new ArrayList<Object>();
        args.add(quotCode);

        baseDao.executeHQL(" delete KstarPrjLst pl where pl.id = ? ", args.toArray());
    }

    @Override
    public void updateAvgttl(LovMember lovMember, KstarPrjLst prjLst) throws AnneException {


        String hql1 = "select l from KstarPrjLst p,LovMember l where p.quotCode = ? and l.groupCode = 'PRJLSTPRDCAT' and p.lvId = l.id and p.quotCode = l.memo order by l.level ";
        String hql2 = "select p from KstarPrjLst p,LovMember l where p.quotCode = ? and l.groupCode = 'PRJLSTPRDCAT' and p.lvId = l.id and p.quotCode = l.memo ";

        String currQuotId = prjLst.getQuotCode();

        List<LovMember> vlist = baseDao.findEntity(hql1, currQuotId);
        List<KstarPrjLst> plist = baseDao.findEntity(hql2, currQuotId);

        Map<String, LovMember> lvmap = new HashMap<String, LovMember>();
        Map<String, KstarPrjLst> prdmap = new HashMap<String, KstarPrjLst>();
        Map<String, KstarPrjLst> stprdmap = new HashMap<String, KstarPrjLst>();

        String rootPrjlvid = "";
        //全部单(标准)产品定价总和
        Double prjstdprd_prdSprc = 0.0;


        for (KstarPrjLst p : plist) {
            prdmap.put(p.getLvId(), p);
            //add standard product
            if (p.getPrdCtg() != null) {
                if (p.getPrdCtg().equals("标准产品")) {
                    stprdmap.put(p.getLvId(), p);
                }

                if (p.getPrdCtg().equals("非标产品")) {
                    stprdmap.put(p.getLvId(), p);
                }

                if (p.getPrdCtg().equals("外购产品")) {
                    stprdmap.put(p.getLvId(), p);
                }
            }
        }

        for (LovMember lov : vlist) {
            lvmap.put(lov.getId(), lov);
            // root
            if (lov.getParentId() != null) {
                if ((lov.getParentId()).equals("ROOT")) {
                    rootPrjlvid = lov.getId();
                }
            }
        }


        Iterator it1 = stprdmap.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry entry1 = (Map.Entry) it1.next();
            String key1 = (entry1.getKey()).toString();
            KstarPrjLst stdprd1 = (KstarPrjLst) (entry1.getValue());

            //全部单(标准)产品定价总和
            //			prjstdprd_prdSprc = prjstdprd_prdSprc + stdprd1.getPrdSprc();
            prjstdprd_prdSprc = prjstdprd_prdSprc + stdprd1.getNewPrdPrc();
        }


        Iterator it = stprdmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (entry.getKey()).toString();
            KstarPrjLst stdprd = (KstarPrjLst) (entry.getValue());

            //标准产品平均总单价
            Double avgTtl = 0.0;
            //项目类服务产品报价总和
            Double prjserprd_ttlunt = 0.0;
            //项目类商务产品报价总和
            Double prjbizprd_ttlunt = 0.0;

            // 单产品行汇总金额
            //			Double std_hhTtl = stdprd.getTtlUnt();
            Double std_hhTtl = stdprd.getNewTtlUnt();


            for (LovMember lov : vlist) {
                KstarPrjLst np = prdmap.get(lov.getId());

                // 单产品行汇总金额
                if (lov.getParentId() != null) {
                    if (lov.getParentId().equals(stdprd.getLvId())) {
                        //						std_hhTtl = std_hhTtl + np.getTtlUnt();
                        std_hhTtl = std_hhTtl + np.getNewTtlUnt();
                    }
                }

                //项目类服务产品报价总和
                if (lov.getParentId() != null) {
                    if (np.getPrdCtg() != null) {
                        if (lov.getParentId().equals(rootPrjlvid)) {
                            if (np.getPrdCtg().equals("服务产品")) {
                                //									 prjserprd_ttlunt = prjserprd_ttlunt + np.getTtlUnt();
                                prjserprd_ttlunt = prjserprd_ttlunt + np.getNewTtlUnt();
                            }
                        }
                    }
                }


                //项目类商务产品报价总和
                if (lov.getParentId() != null) {
                    if (np.getPrdCtg() != null) {
                        if (lov.getParentId().equals(rootPrjlvid)) {
                            if (np.getPrdCtg().equals("商务产品")) {
                                //									 prjbizprd_ttlunt = prjbizprd_ttlunt + np.getTtlUnt();
                                prjbizprd_ttlunt = prjbizprd_ttlunt + np.getNewTtlUnt();
                            }
                        }
                    }
                }

            }

            // 单产品行汇总金额
            //			stdprd.setHhTtl(std_hhTtl);
            stdprd.setNewHhTtl(std_hhTtl);

            //			if ((prjstdprd_prdSprc>0)&&(stdprd.getAmt()>0)) {
            if ((prjstdprd_prdSprc > 0) && (stdprd.getNewAmt() > 0)) {

                //标准产品平均总单价 = 【单产品行汇总金额 + （项目类服务产品报价总和 + 项目类商务产品报价总和） * 定价 / (全部单产品定价总和)】/【数量】
                //				avgTtl = (stdprd.getTtlUnt() + (prjserprd_ttlunt+prjbizprd_ttlunt)* stdprd.getPrdSprc()/prjstdprd_prdSprc)/stdprd.getAmt() ;
                avgTtl = (stdprd.getNewHhTtl() + (prjserprd_ttlunt + prjbizprd_ttlunt) * stdprd.getNewPrdPrc() / prjstdprd_prdSprc) / stdprd.getNewAmt();

            }
            //标准产品平均总单价
            //			stdprd.setAvgTtl(avgTtl);
            stdprd.setNewAvgTtl(avgTtl);

            //更新标准产品
            baseDao.update(stdprd);
        }

    }

    @Override
    public void updateAllAvgttlByQcode(String quotcode) throws AnneException {

        String currQuotId = quotcode;

        List<Object> args = new ArrayList<Object>();
        args.add(currQuotId);

        StringBuffer hql = new StringBuffer(" from KstarPrjLst where prdCtg = '标准产品' and quotCode = ? ");

        List<KstarPrjLst> prjlsts = baseDao.findEntity(hql.toString(), args.toArray());


        for (KstarPrjLst theprjlst : prjlsts) {

            LovMember thelovmember = baseDao.get(LovMember.class, theprjlst.getLvId());
            //theprjlst.setAvgTtl(getAvgttl(thelovmember, theprjlst));
            updateAvgttl(thelovmember, theprjlst);

            //updatePrjLst(thelovmember,theprjlst);
        }

    }

    @Override
    public KstarPrjLst getKstarPrjLst(String qid, String lvId, String ctype) throws AnneException {

        List<Object> args = new ArrayList<Object>();
        args.add(qid);
        args.add(lvId);
        args.add(ctype);
        String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? and CType = ? ", args.toArray());

        KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);

        return prjLst;
    }

    @Override
    public Double getTotalamount(String qid) throws AnneException {

        String hql = " select sum(p.ttlUnt) from KstarPrjLst p where p.quotCode = ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(qid);

        Double theamount = baseDao.findUniqueEntity(hql, args.toArray());

        return theamount;
    }

    public Double getNewTotalamount(String qid) throws AnneException {

        String hql = " select sum(p.newTtlUnt) from KstarPrjLst p where p.quotCode = ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(qid);

        Double theamount = baseDao.findUniqueEntity(hql, args.toArray());

        return theamount;
    }

    @Override
    public KstarQuot getKstarQuot(String quotID) throws AnneException {
        return baseDao.get(KstarQuot.class, quotID);
    }

    @Override
    public void updateQuot(KstarQuot quot) throws AnneException {
        KstarQuot oldQuot = baseDao.get(KstarQuot.class, quot.getId());
        if (oldQuot == null) {
            throw new AnneException(IQuotService.class.getName() + " saveQuot : 没有找到要更新的数据");
        }

        if (!quot.getId().equals(oldQuot.getId())) {
            throw new AnneException("报价单ID不能被修改");
        }

        BeanUtils.copyPropertiesIgnoreNull(quot, oldQuot);
        //oldQuot.setCreateTime(new Date());
        baseDao.update(oldQuot);
        Long count = baseDao.findUniqueEntity("select count(*) from KstarQuot where id = ? ", quot.getId());
        if (count > 1) {
            throw new AnneException("报价单ID已经存在!");
        }
    }

    @Override
    public String addlovroot(String qid, String ctype, String groupId) throws AnneException {

        LovMember lovMember = new LovMember();
        KstarPrjLst prjLst = new KstarPrjLst();
        prjLst.setQuotCode(qid);

        if (StringUtil.isEmpty(prjLst.getQuotCode())) {
            throw new AnneException("找不到该变更单！");
        } else {
            lovMember.setMemo(prjLst.getQuotCode());
        }

        lovMember.setCode(StringUtil.getUUID());
        lovMember.setName(IConstants.CONTR_PRJLST_ROOT_NAME);
        lovMember.setGroupId(groupId);
        lovMember.setLeafFlag("N");


        proLovService.saveCatelog(lovMember);

        prjLst.setPrdNm(IConstants.CONTR_PRJLST_ROOT_NAME);
        prjLst.setLvId(lovMember.getId());
        savePrjLst(prjLst, ctype);

        return lovMember.getId();

    }


    @Override
    public String Checklovroot(String qid) throws AnneException {
        String result = "Y";
        String hql = " select count(*) from LovMember l where l.parentId = 'ROOT' and l.memo = ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(qid);

        Long count = baseDao.findUniqueEntity(hql, args.toArray());
        if (count > 0) {
            result = "N";
        }

        return result;
    }

    /*
     * 根据原合同Id 获取变更单列表
     *
     */
    @Override
    public List<ContrChange> queryChangeListByContrId(String contrId) throws AnneException {
        String hql = "from ContrChange t where t.contrId= ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(contrId);
        return baseDao.findEntity(hql, args.toArray());

    }

    public void cancelChange(String contrId, String typ, UserObject user) throws AnneException {
        //		changeService.startContrChgTrialProcess(getUserObject(), contrId,typ);
        ContrChange contrChg = get(contrId);
        String chgSta = lovMemberService.get(contrChg.getChangeStat()).getCode();
        String application = "";
        String model = "";
        if (chgSta.equalsIgnoreCase("02")) { // 待初审
            application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_TRIAL_PROC, typ);
            model = lovMemberService.getFlowCodeByAppCode(application);
            processService.closeByBusinessKey(model, contrId, user.getParticipant(), "变更单取消");
        } else if (chgSta.equalsIgnoreCase("03") || chgSta.equalsIgnoreCase("06")) {  // 待评审

            //			application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_PRESALE_PROC,typ);
            ////			model = lovMemberService.getFlowCodeByAppCode(application);
            //			ProcessInstance pi = processService.getByBusinessKey(application, contrId);
            //			if(pi!=null){
            //				processService.close(pi.getId(), getUserObject().getParticipant(), "变更单取消");
            //			}
            List<KstarPrjEvl> prjevlLst = contractService.queryContrPrjevlListByContrId(contrId);
            if (prjevlLst.size() > 0) {
                for (KstarPrjEvl evl : prjevlLst) {
                    ProcessInstance pi = processService.get(evl.getEvlRs());
                    if (pi != null) {
                        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁
                        evl.setEvlSt(lov.getId());
                        evl.setEvlMm(user.getPosition().getName());
                        evl.setEvlSg("变更单取消");
                        baseDao.update(evl);
                        processService.close(pi.getId(), user.getParticipant(), "变更单取消");
                    }
                }
            } else {
                // 返回修改，没有变更评审
                application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_TRIAL_PROC, typ);
                model = lovMemberService.getFlowCodeByAppCode(application);
                //processService.closeByBusinessKey(model, contrId, user.getParticipant(), "变更单取消");
            }
        }

        // 合同状态更新
        LovMember stdLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "11");// 11	已取消
        //		changeService.updateContrChgStatus(contrId, stdLov.getId());
        contrChg.setChangeStat(stdLov.getId());
        contrChg.setIsValid("0");

    }


    /**
     * 提交初审，变更类型包含作废，工程清单的新数量必须都等于0！
     *
     * @param contrChange
     */
    @Override
    public void validataNewAmt(ContrChange contrChange) {
        if (!StringUtil.isNullOrEmpty(contrChange.getChangeType())) {
            String[] ids = contrChange.getChangeType().split(",");
            for (String id : ids) {
                LovMember lov = ((LovMember) CacheData.getInstance().get(id));
                if ("08".equals(lov.getCode())) {
                    List<KstarPrjLst> prjLsts = baseDao.findEntity("select p from KstarPrjLst p where p.quotCode = ? ", contrChange.getId());
                    for (KstarPrjLst kstarPrjLst : prjLsts) {
                        if (kstarPrjLst.getNewAmt() != 0d) {
                            throw new AnneException("申请作废合同时需将产品行新数量修改为0");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveLines(String listStr, String contrId, String typ, UserObject userObject) {
        List<KstarPrjLst> list = JSON.parseArray(listStr, KstarPrjLst.class);

        validata(contrId, list);

        for (KstarPrjLst prjLst : list) {
            prjLst.setQuotCode(contrId);
            prjLst.setCType(typ);
            if (prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")) {
                savePrjLstDtl(prjLst, typ, contrId);
            } else {
                updateChgPrjlst(prjLst, userObject);
            }
        }

        Double totalAmt = (double) 0.0;
        totalAmt = this.getNewTotalamount(contrId);

        ContrChange contrChg = this.get(contrId);
        contrChg.setTotalAmt(totalAmt);
        this.update(contrChg);
    }

    /**
     * 修改商务专员
     *
     * @param contrChg
     */
    public void updateOrder(ContrChange contrChg) {

        Contract contract = contrChg.getContract();
        Employee ordEmp = ((Employee) CacheData.getInstance().get(contract.getOrderer()));
        if (ordEmp == null) {
            throw new AnneException("商务专员不存在");
        } else {

            boolean isVaild = corePermissionService.isTradeCommisioner(ordEmp.getId());
            if (!isVaild) {
                throw new AnneException("商务专员错误,选择的员工不是商务专员");
            }

            if (ordEmp.getEndDate() != null) {
                if (!new Date().before(ordEmp.getEndDate())) {
                    throw new AnneException("商务专员无效");
                }
            }
        }

        this.baseDao.executeHQL("update Contract set orderer=?,orderOrgId=? where id=?", new Object[]{contract.getOrderer(), contract.getOrderOrgId(), contract.getId()});
    }

    //黄奇于2017-08-08修复Bug。
    //Bug编号:611 |Bug文件:需求清单-孙志华0804
    private void validata(String contrId, List<KstarPrjLst> list) {
        ContrChange change = baseDao.get(ContrChange.class, contrId);
        LovMember lv09 = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "09");//09	发货后变更
        Map<String, OrderHeader> orderHeaderMap = new HashMap<>();
        // 验证变更后的数据
        if (change.getChangeType().toLowerCase().contains(lv09.getId().toLowerCase())) {
            for (KstarPrjLst prj : list) {
                if ("1".equals(prj.getUpdtFlag()) || "2".equals(prj.getUpdtFlag()) || "3".equals(prj.getUpdtFlag())) {
                    String orderCode = prj.getNotes();
                    if (StringUtil.isNullOrEmpty(orderCode)) {
                        throw new AnneException("变更类型包含 [发货后变更]，请在明细行备注变更的 <b>订单编号</b>");
                    } else {
                        PageCondition condition = new PageCondition();
                        condition.setCondition("orderCode", orderCode);
                        condition.setRows(1);

                        OrderHeader orderHeader = orderHeaderMap.get(orderCode);
                        if (orderHeader != null) {
                            continue;
                        }
                        orderHeader = orderService.queryOrderHeaderByCode(orderCode);
                        if (orderHeader == null) {
                            throw new AnneException("订单:" + orderCode + "不存在");
                        }
                        orderHeaderMap.put(orderCode, orderHeader);
                        ContrChange contrChg = this.get(contrId);
                        if (!Objects.equals(contrChg.getContract().getContrNo(), orderHeader.getSourceCode())) {
                            throw new AnneException("订单:" + orderCode + "对应的合同编号与当前合同变更对应的合同编号不一致");
                        }
                    }
                }
            }
        }
    }

    private void savePrjLstDtl(KstarPrjLst prjLst, String typ, String contrId) {

        String cType = typ;
        //
        //		if(StringUtil.isEmpty(contrId)){
        //			throw new AnneException("找不到对应的变更单");
        //		}else{
        //			lovMember.setMemo(contrId);
        //		}
        //
        //
        //		if(StringUtil.isEmpty(lovMember.getParentId())){
        //			String parentId = contractService.getLovememroot(contrId);
        //			lovMember.setParentId(parentId);
        //		}
        //
        //		Date now = new Date();
        //		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //
        //		int r = contractService.buildRandom(2);
        //		String codeStr = dtFmt.format(now) + r;
        //		lovMember.setCode(codeStr);
        //		lovMember.setName(prjLst.getPrdNm());
        //		lovMember.setLeafFlag("Y");
        //
        //		if(prjLst.getPrdCtg().equals("标准产品")){
        //			lovMember.setLeafFlag("N");
        //		}
        //
        //		proLovService.saveCatelog(lovMember);
        //		prjLst.setLvId(lovMember.getId());
        prjLst.setUpdtFlag("1");
        prjLst.setUpdatType("新增");

        ContrChange contrChg = this.get(contrId);
        prjLst.setBuzCode(contrChg.getChangeNo());
        prjLst.setVeriedNum(0.0);
        prjLst.setNotVeriNum(prjLst.getAmt());
        prjLst.setLineNum(contractService.getLineNum(contrId));
        this.savePrjLst(prjLst, cType);
    }


    private void updateChgPrjlst(KstarPrjLst prjLst, UserObject userObject) {

        //		if( prjLst.getPrdSprc()==null || StringUtil.isEmpty(prjLst.getPrdSprc().toString()) ){
        //			prjLst.setPrdSprc(0.0);
        //		}
        //		if( prjLst.getNewPrdPrc()==null || StringUtil.isEmpty(prjLst.getNewPrdPrc().toString())){
        //			prjLst.setNewPrdPrc(0.0);
        //		}
        //		if( prjLst.getNewAmt()==null || StringUtil.isEmpty(prjLst.getNewAmt().toString())){
        //			prjLst.setNewAmt(0.0);
        //		}

        //		if(prjLst.getOrdNo()!= null){
        ContrChange contrChg = this.get(prjLst.getQuotCode());
        Contract contr = contractService.get(contrChg.getContrId());
        OrderQuantityVo ordQtyVo = orderService.getContractOrderQty(contr.getContrNo(), prjLst.getLineNum());
        if (ordQtyVo != null) {
            double ordBillNum = ordQtyVo.getInvoiceQty();// 订单行开票数量
            double ordDelivNum = ordQtyVo.getDeliveryQty(); // 订单行发运数量
            if (prjLst.getNewAmt() < ordBillNum) {
                throw new AnneException("变更数量小于已开票数量，无法变更");
            } else if (prjLst.getNewAmt() < ordDelivNum) {
                throw new AnneException("变更数量小于已发货数量，无法变更");
            }
        }
        //		}
        //		prjLst.setUpdtFlag("1");
        //		if(prjLst.getUpdatType()==null){
        //			prjLst.setUpdatType("更新");
        //		}else{
        //			if(prjLst.getUpdatType().equalsIgnoreCase("新增")){
        //				prjLst.setUpdatType("新增");
        //			}else{
        //				if(prjLst.getNewAmt()==null){
        //					prjLst.setUpdatType("更新");
        //				}else{
        //					if(prjLst.getNewAmt()==0){
        //						prjLst.setUpdatType("删除");
        //					}else{
        //						prjLst.setUpdatType("更新");
        //					}
        //				}
        //			}
        //		}

        //		LovMember lovMember = lovMemberService.get(lovId);
        //		lovMember.setName(prjLst.getPrdNm());

        this.updatePrjLst(prjLst);

        // 更新订单行的暂挂状态为YES
        // TODO 要将service 更新为用合同ID+行号修改
        orderService.updateContractOrderLinePending(prjLst.getQuotCode(), prjLst.getLineNum(), userObject);

        //		return sendSuccessMessage();
    }

    @Override
    public List<List<Object>> exportContractsHead(PageCondition condition, String typ, String[] ids) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addHead(lstOut);
        List<ContrChange> headerLst = getContractList(condition, typ, ids);
        int i = 1;
        for (ContrChange con : headerLst) {


            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(i);
            lstIn.add(con.getChangeNo());
            lstIn.add(con.getContrNo());
            lstIn.add(con.getContrName());
            lstIn.add(con.getSubmiteTime());
            lstIn.add(con.getCustName());
            lstIn.add(con.getContrCreatorName());
            lstIn.add(con.getContrOrgName());
            lstIn.add(con.getMarketDeptName());
            lstIn.add(con.getChangeStatDesc());
            lstIn.add(con.getChangeTypeDesc());
            lstIn.add(con.getChangeReason());
            lstIn.add(con.getChangeCont());
            lstIn.add(con.getOrdererName());

            if ("1".equals(con.getIsValid())) {
                lstIn.add("是");
            } else {
                lstIn.add("否");
            }

            lstOut.add(lstIn);
            i++;
        }
        return lstOut;
    }

    /**
     * 合同变更导出
     *
     * @param condition
     * @param typ
     * @param ids
     * @return
     */
    public List<ContrChange> getContractList(PageCondition condition, String typ, String[] ids) {


        String idsStr = "";
        if (ids.length > 0) {
            for (String id : ids) {
                idsStr += "'" + id + "',";
            }
            idsStr = idsStr.substring(0, idsStr.length() - 1);
        }

        UserObject user = (UserObject) condition.getCondition("userObject");

        FilterObject fo = condition.getFilterObject(ContrChange.class);
        HqlObject ho = HqlUtil.getHqlObject(fo);
        String hql_search = ho.getHql();

        String sel_hql = "";
        if (hql_search.indexOf("order by") > 0) {
            sel_hql = hql_search.substring(0, hql_search.indexOf("order by") - 1);
        }

        StringBuffer hql = new StringBuffer(sel_hql);

        if (!StringUtil.isNullOrEmpty(idsStr) && !"''".equals(idsStr)) {
            hql.append(" AND contrchange.id in (" + idsStr + ")");
        }

        relevanceHql(condition, hql, ho, user);

        return baseDao.findEntity(hql.toString(), ho.getArgs());
    }

    private void addHead(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("序号");
        lstHead.add("变更编号");
        lstHead.add("原合同号");
        lstHead.add("原合同名称");
        lstHead.add("申请时间");
        lstHead.add("客户名称");
        lstHead.add("销售人员");
        lstHead.add("销售部门");
        lstHead.add("营销部门");
        lstHead.add("变更状态");
        lstHead.add("变更类型");
        lstHead.add("变更原因");
        lstHead.add("变更内容");
        lstHead.add("下单商务专员");
        lstHead.add("有效标识");
        lstOut.add(lstHead);
    }

    private void relevanceHql(PageCondition condition, StringBuffer hql, HqlObject ho, UserObject user) {


        //模糊查询
        String searchKey = condition.getStringCondition("searchKey");
        if (!StringUtil.isNullOrEmpty(searchKey)) {
            hql.append(" AND ( (contrchange.changeNo like '%" + searchKey + "%' ) or ( contrchange.contrName like '%" + searchKey + "%'))");
        }

        String perHql = PermissionUtil.getPermissionHQL(null, "contrchange.createdById", "contrchange.createdPosId", "contrchange.createdOrgId", "contrchange.id", user, IConstants.CONTR_CHANGE);
        hql.append(" AND " + perHql);
        hql.append(" order by contrchange.submiteTime desc  ");

    }
}
