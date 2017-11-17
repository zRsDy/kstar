package com.sinosure.exchange.edi.service;

import java.util.List;

import com.sinosure.exchange.edi.po.BuyerCodeApplyInfo;
import com.sinosure.exchange.edi.po.EdiException;
public interface SolEdiProxyWebService {
	/**
	 * A.企业调用此接口，批量进行买家代码申请
	 * B.数据接收成功进入EDI系统后，信保通自动进行数据合规性校验，校验通过后提交中国信保等后续操作。
	 * C.考虑到WebService的事务问题。此接口支持针对同一笔数据的重复调用。美的在申请信息中加入企业的客户ID做为唯一标识。根据该标识来判断是否重复数据(BuyerCodeApplyInfo中的corpSerialNo字段)。如果重复提交业务申请，则系统以第一次提交的数据为准，不接收重复数据。如果业务申请在被中国信保退回以后再次提交，则接收申请数据，发起业务申请。
	 * D.考虑到性能问题，信保通服务端会对客户端的调用做限制。1个小时内最多只能调用此接口50次数。每次传送数据量最多为1000笔。如果客户端调用超出此限制，则此接口抛出 EdiException。
		 异常信息说明：
	    	EDI-EXC-0001：	调用次数超限
	    	EDI-EXC-0002：	单词调用笔数超限
	 * @author wangg, 2011-02-28
	 * @param List<BuyerCodeApplyInfo> buyerCodeApplyInfoList：买家代码申请信息列表
	 * @return 
	 * @throws EdiException
	 */
	public void doEdiBuyerCodeApply(List<BuyerCodeApplyInfo> buyerCodeApplyInfoList) throws EdiException;
	
}
