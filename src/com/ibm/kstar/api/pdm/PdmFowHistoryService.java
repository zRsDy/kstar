package com.ibm.kstar.api.pdm;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月22日 09:47
 * @LastModifier 黄奇
 */
public interface PdmFowHistoryService {
    IPage query(PageCondition condition,String no);
}
