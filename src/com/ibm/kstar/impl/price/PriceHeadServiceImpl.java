package com.ibm.kstar.impl.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceDiscount;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.product.ProductPriceLine;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PriceHeadServiceImpl extends BaseServiceImpl implements IPriceHeadService {

	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ICustomInfoService customService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	protected ITeamService teamService;

	@Autowired
	ILovMemberService lovMemberService;
	
	@Override
	public IPage query(PageCondition condition,UserObject user) throws Exception {
		List<Object> args = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select p from ProductPriceHead p,LovMember m"
				+ " where p.organization = m.id");
		String phql = PermissionUtil.getPermissionHQL(null, "p.createdById", "p.createdPosId", "p.createdOrgId", "p.id", user, ProcessConstants.PRODUCT_PRICE_HEAD);
		sb.append(" and ").append(phql).append(" ");
		this.addQueryCondition(condition, args, sb, "priceTableName", "p.priceTableName", "like");
		this.addQueryCondition(condition, args, sb, "organization", "m.name", "like");
		this.addQueryCondition(condition, args, sb, "startDate", "p.startDate", ">=");
		this.addQueryCondition(condition, args, sb, "endDate", "p.endDate", "<=");
		this.addQueryCondition(condition, args, sb, "clientPrice", "p.clientPrice", "=");
		this.addQueryCondition(condition, args, sb, "commonPrice", "p.commonPrice", "=");
		sb.append(" order by p.id desc");
		IPage page = baseDao.search(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<ProductPriceHead> list = (List<ProductPriceHead>) page.getList();
		for(ProductPriceHead head : list){
			if(head.getCreater() != null){
				Employee creator = employeeService.get(head.getCreater());
				if(creator != null){
					head.setCreaterName(creator.getName());
				}
			}
			String client = head.getClientId();
			if(StringUtil.isNotEmpty(client)){
				CustomInfo custom = customService.getCustomInfo(client);
				if(custom != null){
					head.setClientName(custom.getCustomFullName());
				}
			}
		}
		return page;
	}

	@Override
	public void save(ProductPriceHead head, UserObject user) {
		List<Object> args = new ArrayList<>();
		String hql = "from ProductPriceHead h where h.priceTableName = ?";
		args.add(head.getPriceTableName());
		if(head.getId() != null){
			hql += " and h.id != ?";
			args.add(head.getId());
		}
		List<ProductPriceHead> headLst = baseDao.findEntity(hql,args.toArray());
		if(headLst.size() > 0){
			throw new AnneException("该价格表名称已被用！");
		}
		LovMember lovOrg = head.getLovMember(head.getOrganization());
		if("A".equals(lovOrg.getOptTxt3())){
			if("1".equals(head.getCommonPrice())){
				hql = "from ProductPriceHead h where h.commonPrice = '1' and h.organization = ?";
				args.clear();
				args.add(head.getOrganization());
				if(head.getId() != null){
					hql += " and h.id != ?";
					args.add(head.getId());
				}
				headLst = baseDao.findEntity(hql,args.toArray());
				if(headLst.size() > 0){
					throw new AnneException("同一营销中心只能有一个统一标准价格！");
				}
			}
		}else{
			head.setCommonPrice("");
		}
		if(head.getId() == null){
			head.setRecordInfor(false, user);
			baseDao.save(head);
			saveOrUpdateProductPriceDiscount(head);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.PRODUCT_PRICE_HEAD,head.getId());
		}else if(head.getId() != null){
			ProductPriceHead oldHead = baseDao.get(ProductPriceHead.class, head.getId());
			BeanUtils.copyPropertiesIgnoreNull(head,oldHead);
			head.setRecordInfor(true, user);
			baseDao.update(oldHead);
			saveOrUpdateProductPriceDiscount(head);
		}
	}

	/**
	 * 保存批发信息
	 * @param head
	 */
	private void saveOrUpdateProductPriceDiscount(ProductPriceHead head){
		// 是客户价目表
		if("1".equals(head.getClientPrice())){
			return;
		}
		
		List<ProductPriceDiscount> discounts = head.getDiscounts();
		
		for (ProductPriceDiscount discount : discounts) {
			
			if (StringUtil.isNullOrEmpty(discount.getId())) {
				// 保存
				if ("1".equals(head.getIsWholesale())) {
					if (ProductPriceDiscount.WHOLESALE_TYPE.equals(discount.getType()) ) {
						// && (discount.getPrive() != null || discount.getQuantity() != null)
						validataDiscount(discount);
						discount.setHeadId(head.getId());
						baseDao.save(discount);
					}
				}
				if ("1".equals(head.getIsDiscount())) {
					if (ProductPriceDiscount.DISCOUNT_TYPE.equals(discount.getType()) ) {
						// && (discount.getRetio() != null || discount.getSum() != null)
						discount.setHeadId(head.getId());
						baseDao.save(discount);
					}
				}
			}else{
				// 更新
				ProductPriceDiscount discount2 = baseDao.get(ProductPriceDiscount.class, discount.getId());
				if (ProductPriceDiscount.WHOLESALE_TYPE.equals(discount.getType())){
					validataDiscount(discount);
					discount2.setPrive(discount.getPrive());
					discount2.setQuantity(discount.getQuantity());
					discount2.setRetio(discount.getRetio());
				}
				if (ProductPriceDiscount.DISCOUNT_TYPE.equals(discount.getType())){
					discount2.setRetio(discount.getRetio());
					discount2.setSum(discount.getSum());
				}
				baseDao.update(discount2);
			}
		}
	}
	
	
	private void validataDiscount(ProductPriceDiscount discount){
		if (discount.getRetio() != null && discount.getPrive() != null) {
			throw new AnneException("第"+discount.getOrderNo()+"行，折扣率和价格只能维护一个值！");
		}
		if (discount.getRetio() != null && discount.getQuantity() == null) {
			throw new AnneException("第"+discount.getOrderNo()+"行，请维护数量！");
		}
		if (discount.getPrive() != null && discount.getQuantity() == null) {
			throw new AnneException("第"+discount.getOrderNo()+"行，请维护数量！");
		}
		if (discount.getQuantity() != null && (discount.getPrive() == null && discount.getRetio() == null)) {
			throw new AnneException("第"+discount.getOrderNo()+"行，请维护折扣率或价格！");
		}
		
	}
	
	
	@Override
	public List<ProductPriceDiscount> getProductPriceDiscount(String headId){
		return baseDao.findEntity("from ProductPriceDiscount where headId = ? ",headId);
	}
	
	
	@Override
	public ProductPriceHead queryLpcById(String id) {
		ProductPriceHead kp = baseDao.get(ProductPriceHead.class, id);
		if(kp != null && kp.getCreater() != null){
			Employee emp = employeeService.get(kp.getCreater());
			if(emp != null ){
				kp.setCreaterName(emp.getName());				
			}
		}
		return kp;
	}

	@Override
	public void delete(String id) {
		ProductPriceHead kp = baseDao.get(ProductPriceHead.class, id);
		List<ProductPriceLine> lineLst = baseDao.findEntity("from ProductPriceLine p where p.headId = ?",kp.getId());
		baseDao.deleteAll(lineLst);
		baseDao.delete(kp);
	}

	@Override
	public List<Map<String,Object>> queryPrice(String customId, String orgId, String searchKey){
//		String hql = "select p from ProductPriceHead p"
//					+" left join (from LovMember m start with m.id=? connect by prior parentId = id) m1 on p.organization = m1.id"
//					+" where ((p.clientPrice = '1' and p.clientId = ?) or m1.id is not null)";
		String sql = "select" 
				+" p.c_pid \"id\",p.c_price_head_name \"priceTableName\",p.c_client_price \"clientPrice\""
				+" ,c.c_custom_full_name \"clientName\",m2.lov_name \"currencyName\",p.dt_start_date \"startDate\""
				+" ,p.dt_end_date \"endDate\",m1.lov_name \"organization\",p.c_desc \"comments\"" 
				+" from CRM_T_PRICE_HEAD p"
				+" left join (select row_id,lov_name from sys_t_lov_member start with row_id=? connect by prior parent_id = row_id) m1 on p.c_organization = m1.row_id"
				+" left join crm_t_custom_info c on p.c_client_id = c.c_pid"
				+" left join sys_t_lov_member m2 on p.c_currency = m2.row_id"
				+" where (((p.c_client_price = '1' and p.c_client_id = ?) or p.c_client_id = null ) or m1.row_id is not null)";
		customId = customId==null? "":customId;
		orgId = orgId==null? "":orgId;
		List<Object> args = new ArrayList<Object>();
		args.add(orgId);
		args.add(customId);
		if(StringUtil.isNotEmpty(searchKey)){
			sql += " and p.c_price_head_name like ?";
			args.add("%"+searchKey.trim()+"%");
		}
		return baseDao.findBySql4Map(sql, args.toArray());
	}

	@Override
	public ProductPriceHead getDefaultPriceHead(String orgId) {
		String saleCenter = lovMemberService.getSaleCenter(orgId);
		LovMember lovMember = lovMemberService.getLovMemberByCode("ORG", saleCenter);
		if(lovMember == null){
			return null;
		}
		ProductPriceHead productPriceHead = baseDao.findUniqueEntity("from ProductPriceHead where organization = ? and commonPrice = '1' ",lovMember.getId());
		return productPriceHead;
	}

	@Override
	public ProductPriceHead getCustomerPriceHead(String clientId) {
		ProductPriceHead productPriceHead = baseDao.findUniqueEntity("from ProductPriceHead where clientPrice = ? and clientId = ? " +
				"and ((startDate < sysdate and endDate >= sysdate) or (endDate is null))  ",new Object[]{"1",clientId});
		return productPriceHead;
	}

	/**
	 * 根据客户id，产品id获取价格。
	 * @param belongOperator
	 * @param productId
	 * @return
	 */
	@Override
	public Double getCustomerProductPrice(String belongOperator, String productId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select l.catalogPrice from ProductPriceHead h , ProductPriceLine l ");
		sb.append(" where h.id = l.headId and l.productID = ? and h.clientId = ? and h.clientPrice = '1' ");
		sb.append(" and (h.endDate is null or (h.startDate < sysdate and h.endDate > sysdate)) ");
		BigDecimal price =  baseDao.findUniqueEntity(sb.toString(),new Object[]{productId,belongOperator});
		return price.doubleValue();
	}
}
