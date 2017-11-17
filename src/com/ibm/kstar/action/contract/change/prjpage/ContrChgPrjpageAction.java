/**
 * 
 */
package com.ibm.kstar.action.contract.change.prjpage;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.ui.TabMain.Style;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.quot.KstarAftSale;
import com.ibm.kstar.entity.quot.KstarBaseInf;
import com.ibm.kstar.entity.quot.KstarIdm;
import com.ibm.kstar.entity.quot.KstarIdu;
import com.ibm.kstar.entity.quot.KstarPgInf;
import com.ibm.kstar.entity.quot.KstarSngBty;
import com.ibm.kstar.entity.quot.KstarSngClr;
import com.ibm.kstar.entity.quot.KstarSngElec;
import com.ibm.kstar.entity.quot.KstarSngMnt;
import com.ibm.kstar.entity.quot.KstarSngRck;
import com.ibm.kstar.entity.quot.KstarSngUps;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/change/prjpage")
//@Scope("prototype")
public class ContrChgPrjpageAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrChgPrjpage";
 	
	@Autowired
	private IContractService contractService;
	

	@RequestMapping("/prjpage")
	public String prjpages(String contrId,String typ,Model model) {
		String cType = typ; //"0005";
		
		//generate aftsale 
		KstarAftSale aftsale = contractService.getKstarAftSale(contrId,typ);
		if(aftsale==null){
			contractService.saveAftsale(aftsale,contrId, cType);			
		}
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);

//		tabMain.addTab("界面完成状态", "/inf.html");
		tabMain.addTab("界面完成状态", "/change/prjpage/pginf.html?contrId="+contrId);
		tabMain.addTab("项目基本信息", "/change/prjpage/baseInf.html?contrId="+contrId);
		tabMain.addTab("售前部分", "/change/prjpage/befsale.html?contrId="+contrId);
		tabMain.addTab("售后部分", "/change/prjpage/aftsale.html?contrId="+contrId);
		
		model.addAttribute("tabMain",tabMain);
		
		return forward("prjpage");
	}
	
	@RequestMapping("/pginf")
	public String pginf(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
		String cType = typ; //"0005";
		
		//generate baseinf 
		KstarBaseInf baseinf = contractService.getKstarBaseInf(contrId,typ);
		if(baseinf==null){
			contractService.saveBaseinf(baseinf,contrId,cType);			
		}
		
		//generate idu 
		KstarIdu idu = contractService.getKstarIdu(contrId,typ);
		if(idu==null){
			contractService.saveIdu(idu, contrId, cType);			
		}
		
		//generate idm 
		KstarIdm idm = contractService.getKstarIdm(contrId,typ);
		if(idm==null){
			contractService.saveIdm(idm, contrId,cType);			
		}
		
		//generate sngups 
		KstarSngUps sngups = contractService.getKstarSngUps(contrId,typ);
		if(sngups==null){
			contractService.saveSngUps(sngups, contrId,cType);		
		}
		
		
		//generate sngbty 
		KstarSngBty sngbty = contractService.getKstarSngBty(contrId,typ);
		if(sngbty==null){
			contractService.saveSngbty(sngbty, contrId,cType);			
		}
		
		//generate sngelec 
		KstarSngElec sngelec = contractService.getKstarSngElec(contrId,typ);
		if(sngelec==null){
			contractService.saveSngelec(sngelec, contrId,cType);			
		}
		
		//generate sngclr 
		KstarSngClr sngclr = contractService.getKstarSngClr(contrId,typ);
		if(sngclr==null){
			contractService.saveSngclr(sngclr, contrId,cType);			
		}
		
		//generate sngrck 
		KstarSngRck sngrck = contractService.getKstarSngRck(contrId,typ);
		if(sngrck==null){
			contractService.saveSngrck(sngrck, contrId,cType);	
		}
		
		//generate sngmnt 
		KstarSngMnt sngmnt = contractService.getKstarSngMnt(contrId,typ);
		if(sngmnt==null){
			contractService.saveSngmnt(sngmnt, contrId,cType);	
		}
		
		
		return forward("pginf");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/pginfPage")
	public String pagePginf(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("contrId", request.getParameter("contrId"));
//		condition.setCondition("cType", "0005");
		
		IPage p = contractService.queryPginf(condition);

		return sendSuccessMessage(p);
	}
	
	
	@RequestMapping("/befsale")
	public String befsale(String contrId,Model model) {

		TabMain tabMain1 = new TabMain(Style.top);
		tabMain1.addTab("IDU", "/change/prjpage/idu.html?contrId="+contrId);//z
		tabMain1.addTab("IDM", "/change/prjpage/idm.html?contrId="+contrId);
		tabMain1.addTab("单UPS", "/change/prjpage/sngups.html?contrId="+contrId);
		tabMain1.addTab("单电池", "/change/prjpage/sngbty.html?contrId="+contrId);//z
		tabMain1.addTab("单空调", "/change/prjpage/sngclr.html?contrId="+contrId);
		tabMain1.addTab("单配电", "/change/prjpage/sngelec.html?contrId="+contrId);
		tabMain1.addTab("单机柜", "/change/prjpage/sngrck.html?contrId="+contrId);
		tabMain1.addTab("单监控", "/change/prjpage/sngmnt.html?contrId="+contrId);

		model.addAttribute("tabMain1",tabMain1);
		
		return forward("befsale");
	}
	
	
	@RequestMapping("/idu")
	public String idu(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);
//		String cType = typ; //"0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarIdu idu = contractService.getKstarIdu(contrId,typ);
		if(idu==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("iduInf",idu);
		
		return forward("idu");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/idu",method=RequestMethod.POST)
	public String editIdu(KstarIdu idu){
//		String cType = "0005";
		contractService.updateIdu(idu);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/idm")
	public String idm(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);
//		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarIdm idm = contractService.getKstarIdm(contrId,typ);
		if(idm==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("idmInf",idm);
		
		return forward("idm");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/idm",method=RequestMethod.POST)
	public String editIdm(KstarIdm idm){
//		String cType = "0005";
		contractService.updateIdm(idm);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngmnt")
	public String sngmnt(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
//		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngMnt sngmnt = contractService.getKstarSngMnt(contrId,typ);
		if(sngmnt==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngmntInf",sngmnt);
		
		return forward("sngmnt");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngmnt",method=RequestMethod.POST)
	public String editSngmnt(KstarSngMnt sngmnt){
//		String cType = "0005";
		contractService.updateSngMnt(sngmnt);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngrck")
	public String sngrck(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
//		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngRck sngrck = contractService.getKstarSngRck(contrId,typ);
		if(sngrck==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngrckInf",sngrck);
		
		return forward("sngrck");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngrck",method=RequestMethod.POST)
	public String editSngRck(KstarSngRck sngrck){
//		String cType = "0005";
		contractService.updateSngRck(sngrck);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngelec")
	public String sngelec(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);
//		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngElec sngelec = contractService.getKstarSngElec(contrId,typ);
		if(sngelec==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngelecInf",sngelec);
		
		return forward("sngelec");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngelec",method=RequestMethod.POST)
	public String editSngelec(KstarSngElec sngelec){
//		String cType = "0005";
		contractService.updateSngelec(sngelec);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/sngclr")
	public String sngclr(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);
		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngClr sngclr = contractService.getKstarSngClr(contrId,typ);
		if(sngclr==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngclrInf",sngclr);
		
		return forward("sngclr");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngclr",method=RequestMethod.POST)
	public String editSngclr(KstarSngClr sngclr){
		String cType = "0005";
		contractService.updateSngclr(sngclr);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngups")
	public String sngups(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
//		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngUps sngups = contractService.getKstarSngUps(contrId,typ);
		if(sngups==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngupsInf",sngups);
		
		return forward("sngups");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngups",method=RequestMethod.POST)
	public String editSngups(KstarSngUps sngups){
		String cType = "0005";
		contractService.updateSngups(sngups);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngbty")
	public String sngbty(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);
		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngBty sngbty = contractService.getKstarSngBty(contrId,typ);
		if(sngbty==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("sngbtyInf",sngbty);
		
		return forward("sngbty");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngbty",method=RequestMethod.POST)
	public String editSngbty(KstarSngBty sngbty){
		String cType = "0005";
		contractService.updateSngbty(sngbty);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/baseInf")
	public String baseInf(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到数据");
		}
		KstarBaseInf baseinf = contractService.getKstarBaseInf(contrId,typ);

		model.addAttribute("baseinfInf",baseinf);
		
		return forward("baseInf");
	}
	
	@ResponseBody
	@RequestMapping(value="/baseInf",method=RequestMethod.POST)
	public String editBaseInf(KstarBaseInf baseinf){
		String cType = "0005";
		contractService.updateBaseInf(baseinf);
		return sendSuccessMessage();
	}

		
	
	@RequestMapping("/aftsale")
	public String aftsale(String contrId,String typ,Model model) {
		model.addAttribute("contrId",contrId);	
		String cType = "0005";
		
		if(contrId==null){
			throw new AnneException("没有找到数据");
		}
		KstarAftSale aftsale = contractService.getKstarAftSale(contrId,typ);

		model.addAttribute("aftsaleInf",aftsale);
		
		return forward("aftsale");
	}
	
	@ResponseBody
	@RequestMapping(value="/aftsale",method=RequestMethod.POST)
	public String editAftsale(KstarAftSale aftsale){
		String cType = "0005";
		contractService.updateAftsale(aftsale);
		return sendSuccessMessage();
	}	
		

	
	@ResponseBody
	@RequestMapping(value = "/addPginf", method = RequestMethod.POST)
	public String addPginf(KstarPgInf pginf, HttpServletRequest request) {
		String cType = "0005";
		contractService.savePgInf(pginf,cType);
		return sendSuccessMessage(pginf.getQuotCode());
	}
	
	
	@RequestMapping("/addPginf")
	public String addPginf(Model model){
		return forward("addPginf");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/editPginf",method=RequestMethod.POST)
	public String editPginf(KstarPgInf pginf){
		contractService.updatePgInf(pginf);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editPginf")
	public String editPginf(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarPgInf pginf = contractService.getKstarPgInf(id);
		if(pginf==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("pginfInfo",pginf);
		return forward("addPginf");
	}
	
	@ResponseBody
	@RequestMapping(value="/deletePgInf",method=RequestMethod.POST)
	public String deletePgInf(String id){
		contractService.deletePgInf(id);
		return sendSuccessMessage();
	}
	

	
	
	

}
