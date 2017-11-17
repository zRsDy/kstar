package com.ibm.kstar.api.product;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.CatelogMatchBean;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import java.util.List;

public interface ICatelogMatchService {

    IPage query(PageCondition condition, String directID) throws Exception;

    public void save(CatelogMatchBean product, UserObject user);

    public CatelogMatchBean queryById(String id);

    public List<LovMember> queryByDirectID(String directID);

    public List<LovMember> queryProCategory();

    public List<LovMember> queryProModel(String search);

    void preDefineInCatelog(List<String> productId);

    void delete(String id);
}
