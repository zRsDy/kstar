package com.ibm.kstar.api.product;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.IProdDocInforEntity;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductLine;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;

public interface IProductService {

	void startPreSaleProcess(UserObject user,String[] ids);
	
	void startCsaleProcess(UserObject user,String[] ids, String newCsaleStatus, String csaleReason);
	
	void startPublishProcess(UserObject user,String[] ids);
	
	public void endPreSaleProcess(String processId);
	
	IPage query(PageCondition condition) throws Exception;
	
	IPage query2(PageCondition condition,UserObject user) throws Exception;
	
	IPage queryDemandProducts(PageCondition condition) throws Exception;
	
	IPage queryProcess(List<KstarProductWorkFlow> kl, int size,int toPage) throws Exception;
	
	IPage serviceProQuery(PageCondition condition) throws Exception;

	void todo();
	
	void delete(String id);
	
	public KstarProduct queryPureProductById(String id);
	
	public KstarProduct queryProductById(String id);

	KstarProduct getProductById(String id);
	
	public void save(KstarProduct product, UserObject user, String catelogId);
	
	void save(KstarProduct product, UserObject user);
	
	public List<KstarProduct> getList(Condition condition) throws AnneException;
	
	public List<LovMember> queryProModel();
	
	public List<LovMember> queryProModel(Condition condition);

	void updateProduct(KstarProduct product, UserObject user);

	List<LovMember> queryMaterCode(String code);

	KstarProduct queryByMaterCode(String code);
	
	List<LovMember> selectProModel(Condition condition);
	
	List<LovMember> selectMaterCode(Condition condition);
	
	List<LovMember> selectProBrand(Condition condition);
	
	public boolean save(IProdDocInforEntity pdi);

	KstarProductLine getProductLineByProductId(String productId);

	/**
	 * 根据产品查找是否报备目录产品
	 * 是：true
	 * @param productId
	 * @return
	 */
	boolean isReport(String productModel);

	/**
	 * 修改CRM产品类别
	 * @param id
	 * @param crmCategory
	 * @param vmaterCode
	 */
	void updateCrmCategory(String id, String crmCategory, String vmaterCode);

	/**
	 * 获取ERP预定义物料号
	 *
	 * @param product
	 * @return
	 */
	String getErpVmaterCodeFor(KstarProduct product);
}
