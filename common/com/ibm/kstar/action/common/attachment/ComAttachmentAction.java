/**
 *
 */
package com.ibm.kstar.action.common.attachment;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ibm
 */
@Controller
@RequestMapping("/common/attachment")
//@Scope("prototype")
public class ComAttachmentAction extends BaseAction {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CONTRACT_PROCESS_KEY = "contrCommonAttachment";

    @Autowired
    private IKstarAttachmentService attachmentService;
    @Autowired
    private ILovMemberService lovMemberService;

    /**
     * 附件
     *
     * @param id
     * @return
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
    @NoRight
    @RequestMapping("/attachment")
    public String atts(String businessId, String docGroupCode, String title, String businessType, boolean unableAdd, boolean unableDelete, String templateURL, String editURL, Model model,HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("bizId", businessId);
        docGroupCode = (docGroupCode == null ? "ATTACHMENTTYPEGROUP" : docGroupCode);
        model.addAttribute("docGroupCode", docGroupCode);
        model.addAttribute("bizType", businessType);
        if ("special".equals(title)) {
            title = "特殊产品规格要求表";
        }else if("customInfo".equals(title)){
        	title = "客户资料";
        }else{
        	title = "";
        }
        model.addAttribute("title", title);
        model.addAttribute("unableAdd", unableAdd);
        
        String needDocType = request.getParameter("needDocType");
        model.addAttribute("needDocType", needDocType);

        //修复Bug:需求清单-孙志华0804|593|驳回给销售销售可以删除附件存在风险，请设置为：销售只能新增附件，不能删除附件（除了在合同新建状态下可以删除）
        //国内合同角色在任何情况下都能删除附件
        if (IConstants.CONTR_STAND.equals(businessType)) {
            if (unableDelete) {
                List<LovMember> rule = lovMemberService.getRulesByUserId(getUserObject().getEmployee().getId());
                for (LovMember lovMember : rule) {
                    if ("HTROLE01".equals(lovMember.getCode())) {
                        unableDelete = false;
                        break;
                    }
                }
            }
        } else if (IConstants.CONTR_CHANGE.equals(businessType)) {

            //黄奇 2017-08-10 合同变更单,只有国内合同专员才能删除附件
            unableDelete = true;
            List<LovMember> rule = lovMemberService.getRulesByUserId(getUserObject().getEmployee().getId());
            for (LovMember lovMember : rule) {
                if ("HTROLE01".equals(lovMember.getCode())) {
                    unableDelete = false;
                    break;
                }
            }
        }

        model.addAttribute("unableDelete", unableDelete);
        model.addAttribute("templateURL", templateURL);
        model.addAttribute("editURL", editURL);
        return forward("attachmentLst");
    }

    @NoRight
    @RequestMapping("/productAttachment")
    public String productAttachment(Model model) {
        return forward("productAttachmentLst");
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/page")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String bizId = condition.getStringCondition("bizId");
        String businessId = condition.getStringCondition("businessId");
        IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
        if (!"xx".equals(bizId) || (StringUtil.isNotEmpty(businessId) && !"xx".equals(businessId))) {
            p = attachmentService.query(condition);
        }
        return sendSuccessMessage(p);
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/productAttachmentPage")
    public String productAttachmentPage(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        IPage p = attachmentService.queryProductAttachments(condition);
        return sendSuccessMessage(p);
    }

    @RequestMapping("/add")
    public String add(String bizId, String docGroupCode, String bizType, String bizId1, Model model,HttpServletRequest request) {
        KstarAttachment att = new KstarAttachment();
        att.setDocUpdr(getUserObject().getEmployee().getName());
        att.setDtUpdDt(new Date());
        if (StringUtil.isNotEmpty(bizId1)) {
            bizId = bizId1;
        }
        model.addAttribute("bizId", bizId);
        model.addAttribute("docGroupCode", docGroupCode);
        model.addAttribute("bizType", bizType);
        model.addAttribute("attInfo", att);
        
        String needDocType = request.getParameter("needDocType");
        if("true".equals(needDocType)){
        	model.addAttribute("needDocType", true);
        }else{
        	model.addAttribute("needDocType", false);
        }

        return forward("comAttachment");
    }

    
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request) throws InstantiationException, IllegalAccessException, SecurityException {
        KstarAttachment att = ActionUtil.requestToObject(KstarAttachment.class, request);
        if (att == null) {

        }
        if (att.getDocGroupCode() == null || att.getDocGroupCode().equalsIgnoreCase("ATTACHMENTTYPEGROUP")) {
            LovMember tpLov = lovMemberService.getLovMemberByCode("ATTACHMENTTYPEGROUP", "ATT_UNUSE");
            att.setDocTp(tpLov.getId());
        }
        List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
        List<KstarAttachment> attachments = new ArrayList<>();
        if (list.size() > 0) {
            for (IUploadFile up : list) {

                att.setDocFulnm(up.getName());
                att.setDocSuffix(up.getSuffix());
                //			att.setDocFulnm(docFulnm);
                att.setDocPath(up.getRealPath());
                att.setDocUrl(up.getWebPath());

                att.setDocUpdr(getUserObject().getEmployee().getId());
                att.setDtUpdDt(new Date());
                att.setCreatedAt(new Date());
                att.setCreatedById(getUserObject().getEmployee().getId());
                att.setCreatedPosId(getUserObject().getPosition().getId());
                att.setCreatedOrgId(getUserObject().getOrg().getId());
                att.setDocPath(up.getRealPath());

                attachments.add(att);

            }

        }
        attachmentService.saveList(attachments);

        return sendSuccessMessage();
    }

    @NoRight
    @RequestMapping("/addFileList")
    public String addFileList(String bizId, String docGroupCode, String bizType, String bizId1, Model model) {
        KstarAttachment att = new KstarAttachment();
        att.setDocUpdr(getUserObject().getEmployee().getName());
        att.setDtUpdDt(new Date());
        if (StringUtil.isNotEmpty(bizId1)) {
            bizId = bizId1;
        }
        model.addAttribute("bizId", bizId);
        model.addAttribute("docGroupCode", docGroupCode);
        model.addAttribute("bizType", bizType);
        model.addAttribute("attInfo", att);

        return forward("comAttachment");
    }
    
    @ResponseBody
    @RequestMapping(value = "/addFileList", method = RequestMethod.POST)
    public String addFileList(HttpServletRequest request) throws InstantiationException, IllegalAccessException, SecurityException {
        KstarAttachment att = ActionUtil.requestToObject(KstarAttachment.class, request);
        if (att == null) {

        }
        if (att.getDocGroupCode() == null || att.getDocGroupCode().equalsIgnoreCase("ATTACHMENTTYPEGROUP")) {
            LovMember tpLov = lovMemberService.getLovMemberByCode("ATTACHMENTTYPEGROUP", "ATT_UNUSE");
            att.setDocTp(tpLov.getId());
        }
        List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
        List<KstarAttachment> attachments = new ArrayList<>();
        if (list.size() > 0) {
            for (IUploadFile up : list) {
            	KstarAttachment attTemp = new KstarAttachment();
                /*att.setDocFulnm(up.getName());
                att.setDocSuffix(up.getSuffix());
                //			att.setDocFulnm(docFulnm);
                att.setDocPath(up.getRealPath());
                att.setDocUrl(up.getWebPath());
                //att.setDocName();
                att.setDocUpdr(getUserObject().getEmployee().getId());
                att.setDtUpdDt(new Date());
                att.setCreatedAt(new Date());
                att.setCreatedById(getUserObject().getEmployee().getId());
                att.setCreatedPosId(getUserObject().getPosition().getId());
                att.setCreatedOrgId(getUserObject().getOrg().getId());
                att.setDocPath(up.getRealPath());*/

            	attTemp.setBizId(att.getBizId());
            	attTemp.setBizType(att.getBizType());
            	attTemp.setDocGroupCode(att.getDocGroupCode());
            	attTemp.setDocTp(att.getDocTp());
            	attTemp.setNotes(att.getNotes());
            	
                attTemp.setDocFulnm(up.getName());
                attTemp.setDocSuffix(up.getSuffix());
                attTemp.setDocPath(up.getRealPath());
                attTemp.setDocUrl(up.getWebPath());
                attTemp.setDocNm(up.getName());
                attTemp.setDocUpdr(getUserObject().getEmployee().getId());
                attTemp.setDtUpdDt(new Date());
                attTemp.setCreatedAt(new Date());
                attTemp.setCreatedById(getUserObject().getEmployee().getId());
                attTemp.setCreatedPosId(getUserObject().getPosition().getId());
                attTemp.setCreatedOrgId(getUserObject().getOrg().getId());
                attTemp.setDocPath(up.getRealPath());
                
                attachments.add(attTemp);

            }

        }
        attachmentService.saveList(attachments);

        return sendSuccessMessage();
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(KstarAttachment att) {
        attachmentService.update(att);
        return sendSuccessMessage();
    }


    @RequestMapping("/edit")
    public String edit(String id, String docGroupCode, Model model) {
        KstarAttachment att = attachmentService.get(id);
        String bizId = att.getBizId();
        model.addAttribute("attInfo", att);
        model.addAttribute("bizId", bizId);
        model.addAttribute("bizType", att.getBizType());
        model.addAttribute("docGroupCode", docGroupCode);
        return forward("comAttachment");
    }
    
    @RequestMapping("/editList")
    public String editList(String id, String docGroupCode, Model model) {
        String[] ids = id.split(","); 
        KstarAttachment att = attachmentService.get(ids[0]);
        String bizId = att.getBizId();
        model.addAttribute("remark", id);
        model.addAttribute("attInfo", att);
        model.addAttribute("bizId", bizId);
        model.addAttribute("attNameList", false);
        model.addAttribute("bizType", att.getBizType());
        model.addAttribute("docGroupCode", docGroupCode);
        return forward("comAttachment");
    }

    @ResponseBody
    @RequestMapping(value = "/editList", method = RequestMethod.POST)
    public String editList(KstarAttachment att) {
    	String remark = att.getRemark();
    	if(!StringUtil.isNullOrEmpty(remark)){
    		String[] ids = remark.split(","); 
    		for(String id:ids) {
    			 KstarAttachment katt = attachmentService.get(id);
    			 katt.setDocTp(att.getDocTp());
    			 katt.setNotes(att.getNotes());
    			 attachmentService.update(katt);
    		}
    	}
        return sendSuccessMessage();
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
        attachmentService.delete(id);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/updateToAvalid", method = RequestMethod.POST)
    public String updateToAvalid(@RequestParam(value = "ids[]") String[] ids) {
        attachmentService.updateExt1(ids, "1");
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/updateToInvalid", method = RequestMethod.POST)
    public String updateToInvalid(@RequestParam(value = "ids[]") String[] ids) {
        attachmentService.updateExt1(ids, "0");
        return sendSuccessMessage();
    }
    
    @NoRight
    @ResponseBody
    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    public String deleteList(String idsStr, HttpServletRequest request) {
		String[] ids = idsStr.split(","); 
		if(ids.length>1) {
			for(String id:ids) {
				attachmentService.delete(id);
			}
		}
        return sendSuccessMessage();
    }
}
