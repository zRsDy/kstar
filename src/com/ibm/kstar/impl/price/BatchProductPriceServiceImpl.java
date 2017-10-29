package com.ibm.kstar.impl.price;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.IProcessService;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.price.IBatchProductPriceService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.price.IPriceLineService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.price.BatchProductPrice;
import com.ibm.kstar.entity.product.ProductPriceLine;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BatchProductPriceServiceImpl extends BaseServiceImpl implements IBatchProductPriceService {
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IProcessService processService;
	
	@Autowired
	IPriceLineService priceLineService;
	
	@Autowired
	IPriceHeadService priceHeadService;
	
	@Autowired
	IProLineService proLineService;
	@Autowired
	protected ITeamService teamService;

	@Override
	public IPage queryBatchProductPrices(PageCondition condition, UserObject user) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("select p.c_pid \"id\",h.c_price_head_name \"priceTableName\",mo.lov_name \"organizationName\""
		+" ,p.c_product_type \"productType\",mt.lov_name \"productTypeName\",l.c_pro_category \"productSort\""
		+" ,p.c_adjust_type \"adjustType\",ma.lov_name \"adjustTypeName\",p.c_adjust_value \"adjustValue\""
		+" ,p.c_adjust_status \"adjustStatus\",ms.lov_name \"adjustStatusName\",p.c_adjust_date \"adjustDate\""
		+" ,mo1.lov_name \"createOrgName\",e.name \"createrName\",p.c_create_date \"createDate\""
		+" from crm_t_batch_product_price p"
		+" left join CRM_T_PRICE_HEAD h on p.c_price_head_id=h.c_pid"
		+" left join CRM_T_PRODUCT_LINE l on p.c_product_sort_id=l.c_pid"
		+" left join SYS_T_LOV_MEMBER mo on mo.row_id = h.c_organization"
		+" left join SYS_T_LOV_MEMBER mt on mt.row_id = p.c_product_type"
		+" left join SYS_T_LOV_MEMBER ma on ma.row_id = p.c_adjust_type"
		+" left join SYS_T_LOV_MEMBER ms on ms.row_id = p.c_adjust_status"
		+" left join SYS_T_LOV_MEMBER mo1 on mo1.row_id = p.c_create_org"
		+" left join SYS_T_PERMISSION_EMPLOYEE e on e.row_id = p.c_creater"
		+" where 1=1");
		
		String phql = PermissionUtil.getPermissionSQL(null, "p.c_created_by_id", "p.c_created_pos_id", "p.c_created_org_id", "p.c_pid", user, ProcessConstants.BATCH_PRODUCT_PRICE);
		sb.append(" and ").append(phql).append(" ");
		
		this.addQueryCondition(condition, args, sb, "priceTableName", "h.c_price_head_name", "like");
		this.addQueryCondition(condition, args, sb, "organization", "mo.lov_name", "like");
		sb.append(" order by p.c_pid desc");
		return baseDao.searchBySql4Map(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveOrUpdateBatchProductPrice(BatchProductPrice batchProductPrice, UserObject user) {
		if(batchProductPrice.getId() == null){
			batchProductPrice.setRecordInfor(false, user);
			baseDao.save(batchProductPrice);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.BATCH_PRODUCT_PRICE,batchProductPrice.getId());
		}else{
			batchProductPrice.setRecordInfor(true, user);
			baseDao.update(batchProductPrice);
		}
	}

	@Override
	public BatchProductPrice queryBatchProductPrice(String id) {
		return baseDao.get(BatchProductPrice.class, id);
	}
	
	@Override
	public BatchProductPrice query(String id) {
		 BatchProductPrice batchProductPrice = baseDao.get(BatchProductPrice.class, id);
		 batchProductPrice.setPriceHead(priceHeadService.queryLpcById(batchProductPrice.getPriceHeadId()));
		 batchProductPrice.setProductSort(proLineService.query(batchProductPrice.getProductSortId()));
		 return batchProductPrice;
	}

	@Override
	public void deleteBatchProductPrice(BatchProductPrice batchProductPrice) {
		baseDao.delete(batchProductPrice);
	}

	@Override
	public void submitBatchProductPrice(UserObject user,String id) {
		BatchProductPrice batchProductPrice = baseDao.get(BatchProductPrice.class, id);
		batchProductPrice.setAdjustDate(new Date());
		batchProductPrice.setAdjustStatus("已提交");
		baseDao.update(batchProductPrice);
		List<ProductPriceLine> queryPriceLines = priceLineService.queryPriceLines(batchProductPrice.getPriceHeadId(), batchProductPrice.getProductSortId(), batchProductPrice.getProductType());
		for(ProductPriceLine line : queryPriceLines){
			if(line.getCatalogPrice() != null){
				if("金额".equals(batchProductPrice.getAdjustTypeName())){
					line.setCatalogPrice(line.getCatalogPrice().add(batchProductPrice.getAdjustValue()));
				}else{
					line.setCatalogPrice(line.getCatalogPrice().add(line.getCatalogPrice().multiply(batchProductPrice.getAdjustValue().divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP))).setScale(2,BigDecimal.ROUND_HALF_UP));
				}
			}
			if(line.getCatalogPrice() != null && line.getLayer1Discount() != null){
				line.setLayer1Price(line.getCatalogPrice().multiply(line.getLayer1Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(line.getCatalogPrice() != null && line.getLayer2Discount() != null){
				line.setLayer2Price(line.getCatalogPrice().multiply(line.getLayer2Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(line.getCatalogPrice() != null && line.getLayer3Discount() != null){
				line.setLayer3Price(line.getCatalogPrice().multiply(line.getLayer3Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(line.getCatalogPrice() != null && line.getLayer4Discount() != null){
				line.setLayer4Price(line.getCatalogPrice().multiply(line.getLayer4Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(line.getCatalogPrice() != null && line.getLayer5Discount() != null){
				line.setLayer5Price(line.getCatalogPrice().multiply(line.getLayer5Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
			if(line.getCatalogPrice() != null && line.getLayer6Discount() != null){
				line.setLayer6Price(line.getCatalogPrice().multiply(line.getLayer6Discount()).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}
		baseDao.update(queryPriceLines);
	}
	
}