package com.ibm.kstar.action.bizopp.change;


import java.util.List;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.entity.product.ProductPriceHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.StringUtil;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.bizopp.BizOppChange;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * Created by wangchao on 2017/3/17.
 */
@Controller
@RequestMapping(value = "/change")
public class BizChangeAction extends BaseAction {

    @Autowired
    IBizoppService bizoppService;

    @Autowired
    ILovMemberService lovMemberService;

    @Autowired
    IPriceHeadService priceHeadService;

    @NoRight
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public String changeView(String id,String type, Model model) {
        BizOppChange boc = null;
        if(StringUtil.isNotEmpty(type)){
            if("changeId".equals(type)) {
                boc = bizoppService.getBizOppChangeById(id);
                model.addAttribute("processing", "processing");
                model.addAttribute("boc", boc);

                BusinessOpportunity bu = bizoppService.getBizOppEntity(boc.getSourceId());
                model.addAttribute("entity",bu);
                
                initModel(boc.getId(), "00", model);
                
                model.addAttribute("userType", "");
                
                return forward("change");
            }
        }

        List<BizOppChange> has = bizoppService.getBizOppChange(id,ProcessConstants.PROCESS_STATUS_Completed);

        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
        if (productPriceHead != null) {
            model.addAttribute("priceTableId", productPriceHead.getId());
        } else {
            throw new AnneException("默认价格表不能为空！");
        }

        if (has.size() > 0) {
            boc = has.get(0);
            model.addAttribute("processing", "processing");
            model.addAttribute("boc", boc);
            model.addAttribute("entity", bizoppService.getBizOppEntity(boc.getSourceId()));
            
            initModel(boc.getId(), "00", model);
            
        } else {
            //id 商机id
            BusinessOpportunity bu = bizoppService.getBizOppEntity(id);

            if(bu == null){
                throw new AnneException("商机ID不存在！");
            }
            
            BizOppChange bocNew = new BizOppChange();
            bocNew.setSourceId(bu.getId());
            bocNew.setAuditStatus(ProcessConstants.PROCESS_STATUS_Pending);
            
            bocNew.setOpportunityName(bu.getOpportunityName());
            bocNew.setBizOppAddress(bu.getBizOppAddress());
            bocNew.setAddress(bu.getAddress());
            bocNew.setClientId(bu.getClientId());
            bocNew.setBidUnit(bu.getBidUnit());
            bocNew.setOpportunityStep(bu.getSaleStage());
            bocNew.setSuccessRate(bu.getSuccessRate());
            bocNew.setPlanFinDate(bu.getPlanFinDate());
            bocNew.setProjectProgress(bu.getProjectProgress());
            bocNew.setCompetitiveBrands(bu.getCompetitiveBrands());
            bocNew.setBizOppAddressName(bu.getBizOppAddressName());
            bocNew.setClientName(bu.getClientName());
            bocNew.setLayer2(bu.getLayer2());
            bocNew.setLayer3(bu.getLayer3());
            bocNew.setLayer4(bu.getLayer4());
            bocNew.setBidNo(bu.getBidNo());

            bocNew.setOpportunityName2(bu.getOpportunityName());
            bocNew.setBizOppAddress2(bu.getBizOppAddress());
            bocNew.setAddress2(bu.getAddress());
            bocNew.setClientId2(bu.getClientId());
            bocNew.setBidUnit2(bu.getBidUnit());
            bocNew.setOpportunityStep2(bu.getSaleStage());
            bocNew.setSuccessRate2(bu.getSuccessRate());
            bocNew.setPlanFinDate2(bu.getPlanFinDate());
            bocNew.setProjectProgress2(bu.getProjectProgress());
            bocNew.setCompetitiveBrands2(bu.getCompetitiveBrands());
            bocNew.setBizOppAddressName2(bu.getBizOppAddressName());
            bocNew.setClientName2(bu.getClientName());
            bocNew.setLayer22(bu.getLayer2());
            bocNew.setLayer32(bu.getLayer3());
            bocNew.setLayer42(bu.getLayer4());
            bocNew.setBidNo2(bu.getBidNo());
            
            model.addAttribute("boc", bocNew);

            model.addAttribute("entity", bu);
            
            initModel(bu.getId(), bu.getStatus(), model);
            
        }
        
        model.addAttribute("userType", getUserObject().getEmployee().getFlag());
        
        return forward("change");
    }

    
    private void initModel(String id,String status,Model model){
    	TabMain tabMainInfo1 = new TabMain();
        TabMain tabMainInfo2 = new TabMain();
        
        tabMainInfo1.setInitAll(false);
        tabMainInfo2.setInitAll(false);
        
        if (hasPermission("P03BizOppoT1ConfigPage")) {
        	tabMainInfo2.addTab("商机配置", "/change/mainInfo.html?id=" + id + "&status="+status);
        }
        //数据中心1展示“集成商”标签页，光伏2展示“决策链”、“竞争分析”标签。
        if (hasPermission("P03BizOppoT2IntegratorPage")) {
        	tabMainInfo1.addTab("授权单位", "/change/integrator.html?id=" + id);
        }
        
        model.addAttribute("tabMainInfo1",tabMainInfo1);
        model.addAttribute("tabMainInfo2",tabMainInfo2);
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/startChangeProcess", method = RequestMethod.POST)
    public String startChangeProcess(String id, String number) {
        bizoppService.startChangeProcess(id, number, getUserObject());
        return sendSuccessMessage();
    }
    
    /**
     * 保存提交商机变更
     * @param boc
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUpdate",method = RequestMethod.POST)
    public String saveUpdate(@RequestBody BizOppChange boc){

        String stepSource = boc.getOpportunityStep();
        String stepNow = boc.getOpportunityStep2();
//        OPPORTUNITY_STAGES
        if(StringUtil.isNotEmpty(stepSource)&&StringUtil.isNotEmpty(stepNow)) {
            LovMember lovMemberSource = lovMemberService.get(stepSource);
            LovMember lovMemberNow = lovMemberService.get(stepNow);
            if(lovMemberSource!= null && lovMemberNow != null){
                Integer codeSource = lovMemberSource.getNo()==null?0:lovMemberSource.getNo();
                Integer codeNow = lovMemberNow.getNo()==null?0:lovMemberNow.getNo();

                if(codeNow < codeSource){
                    throw new AnneException("商机阶段不能变更为当前阶段之前的状态！");
                }
            }
        }

        boc.fillInit(getUserObject());
        BusinessOpportunity bu = bizoppService.getBizOppEntity(boc.getSourceId());

        if(StringUtil.isEmpty(boc.getId())){
            boc.setId(null);
        }
        bizoppService.saveChange(boc,bu.getNumber(),getUserObject());

        return sendSuccessMessage();
    }

    @NoRight
    @RequestMapping("/mainInfo")
	public String bizoppMainInfo(String id, Model model) {
		model.addAttribute("id", id);
		return forward("mainInfo");
	}
    
    @NoRight
    @RequestMapping("/integrator")
	public String bizoppIntegrator(String id, Model model) {
		model.addAttribute("id", id);
		return forward("integrator");
	}

}
