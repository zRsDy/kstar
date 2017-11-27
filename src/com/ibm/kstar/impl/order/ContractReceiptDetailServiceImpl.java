package com.ibm.kstar.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilerRuler;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IContractReceiptDetailService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.order.ContractReceiptDetail;
import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.Receipts;
import com.ibm.kstar.entity.order.VerificationDetail;
import com.ibm.kstar.entity.order.vo.ContractReceiptDetailVO;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractReceiptDetailServiceImpl implements IContractReceiptDetailService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	private ITeamService teamService;
	
	@Override
	public void saveContractReceiptDetail(ContractReceiptDetail contractReceiptDetail,UserObject userObject) throws AnneException {
		// 创建字段
		contractReceiptDetail.setCreatedById(userObject.getEmployee().getId());
		contractReceiptDetail.setCreatedAt(new Date());
		contractReceiptDetail.setCreatedPosId(userObject.getPosition().getId());
		contractReceiptDetail.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		contractReceiptDetail.setUpdatedById(userObject.getEmployee().getId());
		contractReceiptDetail.setUpdatedAt(new Date());
		baseDao.save(contractReceiptDetail);
		teamService.addPosition(contractReceiptDetail.getSalesmanPos(),contractReceiptDetail.getSalesmanId(), 
				IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL,contractReceiptDetail.getId());
	}

	@Override
	public void updateContractReceiptDetail(
			ContractReceiptDetail contractReceiptDetail) throws AnneException {
		ContractReceiptDetail oldContractReceiptDetail = baseDao.get(
				ContractReceiptDetail.class, contractReceiptDetail.getId());
		if (oldContractReceiptDetail == null) {
			throw new AnneException(
					IContractReceiptDetailService.class.getName()
							+ " saveContractReceiptDetail : 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(contractReceiptDetail,
				oldContractReceiptDetail);
		baseDao.update(oldContractReceiptDetail);
	}
	@Override
	public IPage queryContractReceiptDetails(PageCondition condition,String isAgentBoxFlag)
			throws AnneException {
		FilterObject filterObject = condition.getFilterObject(ContractReceiptDetailVO.class);
		String sql = null;
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		sql = hqlObject.getHql();
		
		if(!StringUtil.isNullOrEmpty(isAgentBoxFlag)) {
			sql = sql.substring(0, sql.indexOf("order by"));
			sql = sql +" and contractreceiptdetailvo.custId  in (select lov.optTxt4  from LovMember lov where lov.groupCode = 'ORG' and lov.optTxt3 = 'E') "+
					   " order by contractreceiptdetailvo.updatedAt desc ";
		}
		
		return baseDao.search(sql, hqlObject.getArgs(),
				condition.getRows(), condition.getPage());
	}

	@Override
	public void deleteContractReceiptDetail(String contractReceiptDetailId)
			throws AnneException {
		ContractReceiptDetail contractReceiptDetail  =  baseDao.get(ContractReceiptDetail.class, contractReceiptDetailId);
		if(contractReceiptDetail.getVeriAmount().compareTo(new BigDecimal(0)) > 0 ){
			throw new AnneException(
					IContractReceiptDetailService.class.getName()
							+ " deleteContractReceiptDetail : 已经核销金额大于0，不允许删除");
		}
		baseDao.delete(contractReceiptDetail);
	}

	@Override
	public ContractReceiptDetail getContractReceiptDetail(
			String contractReceiptDetailId) throws AnneException {
		return baseDao
				.get(ContractReceiptDetail.class, contractReceiptDetailId);
	}

	/**
	 * 查找业务实体为空的回款计划
	 * @return
	 */
    private List<Object[]> findDeliveryAndReceiptDetailByBusinessEntityIsNull() {
		List<Object[]> entity = this.baseDao.findEntity("select distinct dh,rd from DeliveryHeader dh,ContractReceiptDetail rd where rd.businessEntity is null and rd.deliveryId=dh.id");
		return entity;
	}

	/**
	 * 查找销售员为空的数据
	 * @return
	 */
	private List<Object[]> findReceiptDetailBySalemanIsNull() {
		List<Object[]> entity = this.baseDao.findEntity("select distinct rd.id,oh.salesmanId,oh.salesmanCode,oh.salesmanName " +
                "from ContractReceiptDetail rd,DeliveryLines dl,OrderLines ol,OrderHeader oh where rd.salesmanId is null and rd.deliveryId=dl.deliveryId and dl.orderId=ol.id and ol.orderId=oh.id and oh.salesmanId is not null");
		return entity;
	}

	@Override
	public void repairBusinessEntityOfReceiptDetail() {
		List<ContractReceiptDetail> updatedReceiptDetail = new ArrayList<>();
		List<Object[]> deliveryAndReceiptDetails = findDeliveryAndReceiptDetailByBusinessEntityIsNull();
		for (Object[] deliveryAndReceiptDetail : deliveryAndReceiptDetails) {
			if (deliveryAndReceiptDetail.length < 2) {
				continue;
			}
			DeliveryHeader delivery = (DeliveryHeader) deliveryAndReceiptDetail[0];
			ContractReceiptDetail receiptDetail = (ContractReceiptDetail) deliveryAndReceiptDetail[1];
			if (delivery != null && receiptDetail != null) {
				receiptDetail.setBusinessEntity(delivery.getBusinessEntity());
				updatedReceiptDetail.add(receiptDetail);
			}
		}
		if (updatedReceiptDetail.size() > 0) {
			this.baseDao.update(updatedReceiptDetail);
		}
	}

	@Override
	public void repairSalemanOfReceiptDetail() {
		List<Object[]> list = findReceiptDetailBySalemanIsNull();
        Set<String> updated = new HashSet<>(list.size());
        for (Object[] objects : list) {
            if (objects.length != 4) {
                continue;
            }
            String receiptDetailId = (String) objects[0];
            if (updated.contains(receiptDetailId)) {
                continue;
            }
            updated.add(receiptDetailId);

            String salesmanId = (String) objects[1];
            String salesmanCode = (String) objects[2];
            String salesmanName = (String) objects[3];
            this.baseDao.executeHQL("update ContractReceiptDetail set salesmanId=?,salesmanCode=?,salesmanName=? where id=?", new Object[]{salesmanId, salesmanCode, salesmanName, receiptDetailId});
        }
	}

	@Override
	public void repairTeamOfReceipt() {
		List<Receipts> list = this.findReceiptsByTeamIsNull();
        if (list.size() == 0) {
            throw new AnneException("没有需要更新的数据");
        }
        for (Receipts receipts : list) {
            teamService.addPosition(receipts.getSalesmanPost(), receipts.getSalesmanId(), IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL, receipts.getId());
        }
    }

    private List<Receipts> findReceiptsByTeamIsNull() {
        List<Receipts> entitys = this.baseDao.findEntity("from Receipts r where r.salesmanPost not in (select t.positionId from Team t where r.id=t.businessId and t.businessType='CONTRACT_RECEIPT_DETAIL')");
        return entitys;
    }

    /**
     * 查询核销日期与收款日期显示在grid上
     */
	@Override
	public void searchGatheringDateAndCheckDate(List<ContractReceiptDetail> contractReceiptDetailList) {
		for(ContractReceiptDetail contractReceiptDetail:contractReceiptDetailList) {
			String vHql = "from VerificationDetail r where r.contrReceDetailId = ?  order by veriDate desc";//核销明细
			List<VerificationDetail> VerificationDetail = this.baseDao.findEntity(vHql,new Object[]{contractReceiptDetail.getId()});
			if(VerificationDetail.size()>0) {
				contractReceiptDetail.setCheckDate(VerificationDetail.get(0).getVeriDate());
				String rHql = "from Receipts r where r.id = ? ";//收款记录
				Receipts receipts = this.baseDao.findUniqueEntity(rHql,new Object[]{VerificationDetail.get(0).getReceiptsId()});
				if(receipts!=null) {
					contractReceiptDetail.setGatheringDate(receipts.getReceiptsDate());
				}
			}
		}
	}

	 /**
     * 查询核销日期与收款日期显示在grid上
     */
	@Override
	public void searchGatheringDateAndCheckDateForVO(List<ContractReceiptDetailVO> contractReceiptDetailList) {
		for(ContractReceiptDetailVO contractReceiptDetail:contractReceiptDetailList) {
			String vHql = "from VerificationDetail r where r.contrReceDetailId = ?  order by veriDate desc";//核销明细
			List<VerificationDetail> VerificationDetail = this.baseDao.findEntity(vHql,new Object[]{contractReceiptDetail.getId()});
			if(VerificationDetail.size()>0) {
				contractReceiptDetail.setCheckDate(VerificationDetail.get(0).getVeriDate());
				String rHql = "from Receipts r where r.id = ? ";//收款记录
				Receipts receipts = this.baseDao.findUniqueEntity(rHql,new Object[]{VerificationDetail.get(0).getReceiptsId()});
				if(receipts!=null) {
					contractReceiptDetail.setGatheringDate(receipts.getReceiptsDate());
				}
			}
		}
	}
}