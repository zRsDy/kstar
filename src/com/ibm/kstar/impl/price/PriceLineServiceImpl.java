package com.ibm.kstar.impl.price;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.price.IPriceLineService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.cache.CacheUtils;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductLine;
import com.ibm.kstar.entity.product.PriceLayCompareLine;
import com.ibm.kstar.entity.product.ProductPriceDiscountVO;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.product.ProductPriceLine;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PriceLineServiceImpl extends BaseServiceImpl implements IPriceLineService {
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IProLineService proLineService;
	
	@Override
	public IPage query(PageCondition condition) throws Exception {
		StringBuilder sb = this.getSql();
		List<Object> args = new ArrayList<Object>();
		this.addQueryCondition(condition, args, sb, "headId", "r.C_PRICE_HEAD_ID", "=");
		this.addQueryCondition(condition, args, sb, "productCode", "b.c_pro_code", "like");
		this.addQueryCondition(condition, args, sb, "productName", "b.c_pro_name", "like");
		this.addQueryCondition(condition, args, sb, "proModel", "b.c_pro_model", "like");
		sb.append(" order by r.c_pid desc");
		return baseDao.searchBySql4Map(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void save(ProductPriceLine doc, UserObject user) {
		checkRateAndNumber(doc);
		if(doc.getLayer1Discount() != null){
			doc.setLayer1Price(doc.getCatalogPrice().multiply(doc.getLayer1Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(doc.getLayer2Discount() != null){
			doc.setLayer2Price(doc.getCatalogPrice().multiply(doc.getLayer2Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(doc.getLayer3Discount() != null){
			doc.setLayer3Price(doc.getCatalogPrice().multiply(doc.getLayer3Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(doc.getLayer4Discount() != null){
			doc.setLayer4Price(doc.getCatalogPrice().multiply(doc.getLayer4Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(doc.getLayer5Discount() != null){
			doc.setLayer5Price(doc.getCatalogPrice().multiply(doc.getLayer5Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		if(doc.getLayer6Discount() != null){
			doc.setLayer6Price(doc.getCatalogPrice().multiply(doc.getLayer6Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		List<ProductPriceLine> lst = baseDao.findEntity("from ProductPriceLine l where l.productID = ? and l.headId = ?", new Object[]{doc.getProductID(),doc.getHeadId()});
		int size = lst.size();
		if(doc.getId() != null){
			if(size > 1 || (size == 1 && !doc.getId().equals(lst.get(0).getId()))){
				throw new AnneException("该产品已建立价格表！");
			}else if(size == 1 && doc.getId().equals(lst.get(0).getId())){
				ProductPriceLine line = lst.get(0);
				BeanUtils.copyProperties(doc, line);
				doc = line;
			}
		}else{
			if(size > 0){
				throw new AnneException("该产品已建立价格表！");
			}
		}
		LovMember lovMember = doc.getLovMember("proPriceSpecify", "0002");
		baseDao.executeHQL("update KstarProduct p set p.priceStatus = ? where p.id = ?",new Object[]{lovMember.getId(), doc.getProductID()});
		if(doc.getId() == null){
			doc.setRecordInfor(false, user);
			baseDao.save(doc);
		}else{
			doc.setRecordInfor(true, user);
			baseDao.update(doc);
		}
	}

	@Override
	public ProductPriceLine queryLpcById(String id) {
		ProductPriceLine kp = baseDao.get(ProductPriceLine.class, id);
		KstarProduct product = baseDao.get(KstarProduct.class, kp.getProductID());
		KstarProductLine proLine = baseDao.get(KstarProductLine.class, product.getProLineID());
		kp.setProductCode(product.getProductCode());
		kp.setProductName(product.getProductName());
		kp.setMaterCode(product.getMaterCode());
		kp.setProDesc(product.getProDesc());
		kp.setUnit(product.getUnit());
		kp.setProModel(product.getProModel());
		if(proLine != null){
			kp.setProLine(proLine.getProLine());
			kp.setCproPowcap(proLine.getCproPowcap());
		}
		kp.setProBrand(product.getProBrand());
		kp.setSaleStatus(product.getSaleStatus());
		return kp;
	}

	@Override
	public List<ProductPriceLine> queryPriceLines(String priceHeadId, String productSortId, String productType) {
		List<String> argList = new ArrayList<String>();
		String hql_from = "select t from ProductPriceLine t";
		String hql_where = " where 1=1";
		if((productSortId != null && !"".equals(productSortId)) || (productType != null && !"".equals(productType))){
			hql_from = hql_from + ",KstarProduct p";
			hql_where = hql_where + " and t.productID = p.id";
			if(productSortId != null && !"".equals(productSortId)){
				hql_from = hql_from + ",KstarProductLine l";
				hql_where = hql_where + " and p.proLineID=l.id and l.id = ?";
				argList.add(productSortId);
			}
			if(productType != null && !"".equals(productType)){
				hql_from = hql_from + ",LovMember m";
				hql_where = hql_where + " and p.crmCategory = m.code and m.id = ?";
				argList.add(productType);
			}
		}
		hql_where = hql_where + " and t.headId = ?";
		argList.add(priceHeadId);
		String hql = hql_from + hql_where;
		List<ProductPriceLine> findEntity = baseDao.findEntity(hql, argList.toArray());
		return findEntity;
	}

	@Override
	public void delete(String id) {
		ProductPriceLine kp = baseDao.get(ProductPriceLine.class, id);
		baseDao.delete(kp);
	}

	@Override
	public void importPriceLine(List<List<Object>> data, UserObject user) {
		int sizeOut = data.size();
		Set<String> proCodeSet = new HashSet<>();
		if(sizeOut > 0){
			String sql = "select p.c_pro_code from" 
					+" (select ROW_ID, GROUP_ID, LEAF_FLAG, START_DATE, END_DATE, OPT_TXT5" 
					+" from SYS_T_LOV_MEMBER start with PARENT_ID in" 
					+" (select distinct row_id from SYS_T_LOV_MEMBER pc,crm_t_product_permission_rel pr where pr.PRODUCT_CATALOG_ID = pc.ROW_ID"
					+" and ( pr.ORG_ID = ? or pr.ORG_ID = ?) and pc.delete_flag = 'N' and pc.parent_id = 'ROOT')"
					+" connect by prior ROW_ID = PARENT_ID) lov,"
					+" CRM_T_PRODUCT_BASIC p"
					+" left join CRM_T_PRODUCT_LINE li"
					+" on p.C_PRO_LINE_ID = li.C_PID"
					+" where lov.LEAF_FLAG = 'Y'"
					+" and lov.GROUP_ID = 'procatalog'"
					+" and p.C_PID = lov.OPT_TXT5"
					+" and lov.START_DATE <= sysdate"
					+" and nvl(lov.END_DATE, sysdate + 1) >= sysdate";
			List<Object> args = new ArrayList<>();
			args.add(user.getPosition().getPositionInOrgId());
			args.add(user.getOrg().getId());
			List<Object[]> objLst = baseDao.findBySql(sql, args.toArray());
			if(objLst != null && objLst.size() > 0){
				for(int i=0; i<objLst.size(); i++){
					Object obj = (Object) objLst.get(i);
					if(obj != null){
						proCodeSet.add(obj.toString().intern());
					}
				}
			}
		}
		Map<String,List<PriceLayCompareLine>> comMap = new HashMap<>();	//缓存价格表头
		Map<String,ProductPriceHead> priceHeadMap = new HashMap<>();	//缓存默认折扣率
		for(int i=0; i<sizeOut; i++){
			List<Object> lstIn = data.get(i);
			int index = 11;
			ProductPriceLine line = new ProductPriceLine();
			if(lstIn.get(0) == null || "".equals(lstIn.get(0).toString().trim())){
				throw new AnneException("价格表名称不能为空！");
			}
			if(lstIn.get(1) == null || "".equals(lstIn.get(1).toString().trim())){
				throw new AnneException("产品编码不能为空！");
			}
			if(!proCodeSet.contains(lstIn.get(1).toString().intern())){
				throw new AnneException("产品编码："+lstIn.get(1).toString()+"，没有权限！");
			}
			String priceHeadStr = lstIn.get(0).toString().intern();
			if(!comMap.containsKey(priceHeadStr)){
				//缓存价格表头
				List<ProductPriceHead> head = baseDao.findEntity("from ProductPriceHead h where h.priceTableName = ?",lstIn.get(0));
				if(head == null || head.size() == 0){
					throw new AnneException("价格表名称"+lstIn.get(0)+"不存在！");
				}
				priceHeadMap.put(priceHeadStr, head.get(0));
				//缓存默认折扣率
				String hql = "select l from PriceLayCompareLine l,PriceLayCompareHeader h where l.headerId = h.id and h.organization = ?";
				List<PriceLayCompareLine> tmpComLst = baseDao.findEntity(hql, head.get(0).getOrganization());
				comMap.put(priceHeadStr, tmpComLst);
			}
			List<KstarProduct> product = baseDao.findEntity("from KstarProduct p where p.productCode = ?",lstIn.get(1));
			if(product == null || product.size() == 0){
				throw new AnneException("产品编码"+lstIn.get(1)+"不存在！");
			}
			
			List<ProductPriceLine> proLineLst = baseDao.findEntity("from ProductPriceLine p where p.productID = ? and headId = ?",new Object[]{product.get(0).getId(),priceHeadMap.get(priceHeadStr).getId()});
			if(proLineLst != null && proLineLst.size() > 0){
				line = proLineLst.get(0);
			}
			line.setHeadId(priceHeadMap.get(priceHeadStr).getId());
			line.setProductID(product.get(0).getId());
			line.setCost(getBigDecimal(lstIn.get(index++),"产品成本"));
			line.setCatalogPrice(getBigDecimal(lstIn.get(index++),"目录价格"));
			if(line.getCatalogPrice() == null){
				throw new AnneException("目录价格不能为空！");
			}
			line.setCrossMargin(getBigDecimal(lstIn.get(index++),"毛利率"));
			line.setStartDate(getDate(lstIn.get(index++),"有效开始日期"));
			if(line.getStartDate() == null){
				throw new AnneException("有效开始日期不能为空！");
			}
			if((new Date()).getTime()-line.getStartDate().getTime()>24*60*60*1000){
				throw new AnneException("有效开始日期不能小于当前日期！");
			}
			line.setEndDate(getDate(lstIn.get(index++),"有效结束日期"));
			if(line.getEndDate() != null){
				if(line.getEndDate().before(line.getStartDate())){
					throw new AnneException("有效结束日期不能小于有效开始日期！");
				}
			}
			index++;
			line.setLayer1Discount(getBigDecimal(lstIn.get(index++),"层一价折扣率"));
			line.setLayer1Number(getLong(lstIn.get(index++),"层一数量级"));
			index++;
			line.setLayer2Discount(getBigDecimal(lstIn.get(index++),"层二价折扣率"));
			line.setLayer2Number(getLong(lstIn.get(index++),"层二数量级"));;
			index++;
			line.setLayer3Discount(getBigDecimal(lstIn.get(index++),"层三价折扣率"));
			line.setLayer3Number(getLong(lstIn.get(index++),"层三数量级"));
			index++;
			line.setLayer4Discount(getBigDecimal(lstIn.get(index++),"层四价折扣率"));
			line.setLayer4Number(getLong(lstIn.get(index++),"层四数量级"));
			index++;
			line.setLayer5Discount(getBigDecimal(lstIn.get(index++),"层五价折扣率"));
			line.setLayer5Number(getLong(lstIn.get(index++),"层五数量级"));
			index++;
			line.setLayer6Discount(getBigDecimal(lstIn.get(index++),"层六价折扣率"));
			line.setLayer6Number(getLong(lstIn.get(index++),"层六数量级"));
			//如果没写，加默认折扣率
			List<PriceLayCompareLine> comLst = comMap.get(priceHeadStr);
			if(comLst != null){
				int comLstSize = comLst.size();
				switch(comLstSize){
					case 6:
						if(line.getLayer6Discount() == null){
							line.setLayer6Discount(comLst.get(5).getDiscountRate());
						}
					case 5:
						if(line.getLayer5Discount() == null){
							line.setLayer5Discount(comLst.get(4).getDiscountRate());
						}
					case 4:
						if(line.getLayer4Discount() == null){
							line.setLayer4Discount(comLst.get(3).getDiscountRate());
						}
					case 3:
						if(line.getLayer3Discount() == null){
							line.setLayer3Discount(comLst.get(2).getDiscountRate());
						}
					case 2:
						if(line.getLayer2Discount() == null){
							line.setLayer2Discount(comLst.get(1).getDiscountRate());
						}
					case 1:
						if(line.getLayer1Discount() == null){
							line.setLayer1Discount(comLst.get(0).getDiscountRate());
						}
				}
			}
			//计算层级价格
			if(line.getLayer1Discount() != null){
				line.setLayer1Price(line.getCatalogPrice().multiply(line.getLayer1Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer1Price(null);
			}
			if(line.getLayer2Discount() != null){
				line.setLayer2Price(line.getCatalogPrice().multiply(line.getLayer2Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer2Price(null);
			}
			if(line.getLayer3Discount() != null){
				line.setLayer3Price(line.getCatalogPrice().multiply(line.getLayer3Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer3Price(null);
			}
			if(line.getLayer4Discount() != null){
				line.setLayer4Price(line.getCatalogPrice().multiply(line.getLayer4Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer4Price(null);
			}
			if(line.getLayer5Discount() != null){
				line.setLayer5Price(line.getCatalogPrice().multiply(line.getLayer5Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer5Price(null);
			}
			if(line.getLayer6Discount() != null){
				line.setLayer6Price(line.getCatalogPrice().multiply(line.getLayer6Discount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				line.setLayer6Price(null);
			}
			checkRateAndNumber(line);
			
			if(line.getId() == null){
				line.setRecordInfor(false, user);
				baseDao.save(line);
			}else{
				line.setRecordInfor(true, user);
				baseDao.update(line);
			}
		}
	}
	
	@Override
	public List<List<Object>> exportPriceLineTemplet(String headId) {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		return lstOut;
	}

	@Override
	public List<List<Object>> exportPriceLine(String headId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		StringBuilder sb = this.getSql();
		sb.append(" and r.C_PRICE_HEAD_ID = ?");
		sb.append(" order by r.c_pid desc");
		List<Object[]> lst = baseDao.findBySql(sb.toString(),headId);
		int index = 0;
		for(Object[] obj : lst){
			List<Object> lstIn = new ArrayList<Object>();
			index = 2;
			for(int i=0; i<14; i++){
				lstIn.add(obj[index++]);
			}
			lstIn.add(sdf.format(obj[index++]));
			if(obj[index] != null){
				lstIn.add(sdf.format(obj[index++]));
			}else{
				index++;
				lstIn.add("");
			}
			for(int i=0; i<18; i++){
				lstIn.add(obj[index++]);
			}
			lstOut.add(lstIn);
		}
		return lstOut;
	}
	
	private StringBuilder getSql(){
		StringBuilder sb = new StringBuilder(" select");
		sb.append(" r.c_pid \"id\",");
		sb.append(" b.c_pid \"proId\",");
		sb.append(" h.C_PRICE_HEAD_NAME \"priceTableName\",");
		sb.append(" b.c_pro_code \"proCode\",");
		sb.append(" b.c_pro_name \"proName\",");
		sb.append(" b.c_mater_code \"materCode\",");
		sb.append(" b.c_pro_desc \"proDesc\",");
		sb.append(" (select m.lov_name from sys_t_lov_member m where m.row_id=b.c_unit) \"proUnit\",");
		sb.append(" b.c_pro_model \"proModel\",");
		sb.append(" l.c_pro_line \"proLine\",");
		sb.append(" b.c_brand \"proBrand\",");
		sb.append(" l.c_pro_powcap \"proPowcap\",");
		sb.append(" (select m.lov_name from sys_t_lov_member m where m.row_id=b.c_pro_sale_status) \"saleStatus\",");
		sb.append(" r.n_pro_cost \"proCost\",");
		sb.append(" r.n_catalog_price \"catelogPrice\",");
		sb.append(" r.n_catalog_rate \"catelogRate\",");
		sb.append(" r.dt_start_date \"startDate\",");
		sb.append(" r.dt_end_date \"endDate\",");
		sb.append(" r.n_layer1_price \"price1\",");
		sb.append(" r.n_layer1_discount \"discount1\",");
		sb.append(" r.n_layer1_number \"number1\",");
		sb.append(" r.n_layer2_price \"price2\",");
		sb.append(" r.n_layer2_discount \"discount2\",");
		sb.append(" r.n_layer2_number \"number2\",");
		sb.append(" r.n_layer3_price \"price3\",");
		sb.append(" r.n_layer3_discount \"discount3\",");
		sb.append(" r.n_layer3_number \"number3\",");
		sb.append(" r.n_layer4_price \"price4\",");
		sb.append(" r.n_layer4_discount \"discount4\",");
		sb.append(" r.n_layer4_number \"number4\",");
		sb.append(" r.n_layer5_price \"price5\",");
		sb.append(" r.n_layer5_discount \"discount5\",");
		sb.append(" r.n_layer5_number \"number5\",");
		sb.append(" r.n_layer6_price \"price6\",");
		sb.append(" r.n_layer6_discount \"discount6\",");
		sb.append(" r.n_layer6_number \"number6\"");
		sb.append(" from CRM_T_PRICE_LINE r");
		sb.append(" left join CRM_T_PRICE_HEAD h on h.c_pid = r.C_PRICE_HEAD_ID");
		sb.append(" left join CRM_T_PRODUCT_BASIC b on r.c_pro_id = b.c_pid");
		sb.append(" left join CRM_T_PRODUCT_LINE l on b.c_pro_line_id = l.c_pid");
		sb.append(" where 1=1");
		return sb;
	}
	
	private Date getDate(Object s, String errorName){
		if(s instanceof Date){
			return (Date) s;
		}
		if(s != null){
			String ss = s.toString();
			if(StringUtil.isNotEmpty(ss)){
				try{
					Date date = null;
					if(ss.contains("/")){
						date = DateUtils.parseDate(ss,new String[]{"yyyy/MM/dd"});
					}else if(ss.contains("-")){
						date = DateUtils.parseDate(ss,new String[]{"yyyy-MM-dd"});
					}
					if(date == null){
						throw new AnneException(errorName+"，格式为yyyy-MM-dd，请核对格式！");
					}
					return date;
				}catch(Exception e){
					throw new AnneException(errorName+"，格式为yyyy-MM-dd，请核对格式！");
				}
			}
		}
		return null;
	}
	
	private BigDecimal getBigDecimal(Object s, String errorName){
		if(s != null){
			if(StringUtil.isNotEmpty(s.toString())){
				try{
					return new BigDecimal(s.toString());
				}catch(Exception e){
					throw new AnneException(errorName+"，只能输入数字！");
				}
			}
		}
		return null;
	}
	
	private Long getLong(Object s, String errorName){
		if(s != null){
			if(StringUtil.isNotEmpty(s.toString())){
				try{
					return new Long(s.toString());
				}catch(Exception e){
					throw new AnneException(errorName+"，只能输入整数！");
				}
			}
		}
		return null;
	}
	
	private void  addHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("价格表名称(导入必填)");
		lstHead.add("产品编码(导入必填)");
		lstHead.add("产品名称");
		lstHead.add("物料号");
		lstHead.add("产品描述");
		lstHead.add("单位");
		lstHead.add("产品型号");
		lstHead.add("产品线");
		lstHead.add("品牌");
		lstHead.add("功率容量");
		lstHead.add("转销状态");
		lstHead.add("产品成本");
		lstHead.add("目录价格(导入必填)");
		lstHead.add("毛利率");
		lstHead.add("有效开始日期(导入必填 ，yyyy-MM-dd或yyyy/mm/dd)");
		lstHead.add("有效结束日期(yyyy-MM-dd或yyyy/mm/dd)");
		lstHead.add("层一价格");
		lstHead.add("层一价折扣率");
		lstHead.add("层一数量");
		lstHead.add("层二价格");
		lstHead.add("层二价折扣率");
		lstHead.add("层二数量");
		lstHead.add("层三价格");
		lstHead.add("层三价折扣率");
		lstHead.add("层三数量");
		lstHead.add("层四价格");
		lstHead.add("层四价折扣率");
		lstHead.add("层四数量");
		lstHead.add("层五价格");
		lstHead.add("层五价折扣率");
		lstHead.add("层五数量");
		lstHead.add("层六价格");
		lstHead.add("层六价折扣率");
		lstHead.add("层六数量");
		lstOut.add(lstHead);
	}
	
	@Override
	public void saveProductPriceLines(ProductPriceDiscountVO discountVO){
		
		List<ProductPriceLine> list = queryPriceLines(discountVO.getHeadId(), null, null);
		Map<String, ProductPriceLine> map = new HashMap<>();
		for (ProductPriceLine productPriceLine : list) {
			map.put(productPriceLine.getProductID(), productPriceLine);
		}
		
		for (ProductPriceLine line : discountVO.getLines()) {
			
			// TODO 验证唯一
			checkUnique(discountVO.getHeadId(), line.getProductID());
			
			if (map.get(line.getProductID())==null) {
				line.setHeadId(discountVO.getHeadId());
				line.setCatalogPrice(new BigDecimal(0));
				baseDao.save(line);
			}
		}
		
	}
	
	/**
	 * 验证产品在批发价里面是否唯一
	 * @param headId
	 * @param productId
	 * @return
	 */
	private void checkUnique(String headId,String productId){
		List<ProductPriceLine> list = baseDao.findEntity("select line from ProductPriceLine line,ProductPriceHead head where line.headId = head.id and head.isWholesale = '1' and head.id <> ? and line.productID = ? ",new Object[]{headId,productId});
		if (list.size() > 0) {
			KstarProduct product = CacheUtils.getProduct(productId);
			throw new AnneException(product.getProductName()+"["+product.getMaterCode()+"]在其他批发价已存在，不能重复维护！");
		}
		list = baseDao.findEntity("from ProductPriceLine line where line.headId = ? and line.productID = ?",new Object[]{headId,productId});
		if (list.size() > 0) {
			KstarProduct product = CacheUtils.getProduct(productId);
			throw new AnneException(product.getProductName()+"["+product.getMaterCode()+"]在当前批发价已存在，不能重复维护！");
		}
	}
	
	

	private void checkRateAndNumber(ProductPriceLine lpc){
		if(compareDiscount(lpc.getLayer1Discount(),lpc.getLayer2Discount()) && compareDiscount(lpc.getLayer2Discount(),lpc.getLayer3Discount())
		   && compareDiscount(lpc.getLayer3Discount(),lpc.getLayer4Discount()) && compareDiscount(lpc.getLayer4Discount(),lpc.getLayer5Discount())
		   && compareDiscount(lpc.getLayer5Discount(),lpc.getLayer6Discount()) && compareNumber(lpc.getLayer1Number(),lpc.getLayer2Number())
		   && compareNumber(lpc.getLayer2Number(),lpc.getLayer3Number()) && compareNumber(lpc.getLayer3Number(),lpc.getLayer4Number())
		   && compareNumber(lpc.getLayer4Number(),lpc.getLayer5Number()) && compareNumber(lpc.getLayer5Number(),lpc.getLayer6Number())){
		}
		else{
			throw new AnneException("折扣率必须递减，层一最高;数量级必须递增，层一最小。");
		}
	}
	
	private boolean compareDiscount(BigDecimal b1,BigDecimal b2){
		if(b1 == null && b2 != null){
			throw new AnneException("折扣率不能跨层！");
		}else if(b1 != null && b2 != null){
			if(b1.compareTo(b2) < 1){
				return false;
			}
		}
		return true;
	}
	
	private boolean compareNumber(Long l1,Long l2){
		if(l1 == null && l2 != null){
			throw new AnneException("数量级不能跨层！");
		}else if(l1 != null && l2 != null){
			if(l1 >= l2){
				return false;
			}
		}
		return true;
	}
}
