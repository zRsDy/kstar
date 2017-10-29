
//-----------------------------------------------------------------------------
//JS基础构件 - 修改备注,nodeCode,reportDate
//-----------------------------------------------------------------------------
     function rptModifyComment(reportId){
    	 rId=reportId;
         var url=contextPath + "/report/showComment.htm?reportId=" + reportId + '&_ts=' + (new Date()).getTime();
		openContentWindow(url, "showComment_dialog");
		$("#showComment_dialog").dialog({
			autoOpen : false,
			height : 'auto',
			width : 'auto',
			modal : true,
			title : '上传',
			hide : "explode",
			buttons : {
				'提交' : function() {
					var theform = document.forms['showCommentForm'];

					$.ajax({
						type : "POST",
						url : theform.action,
						data : $(theform).serialize(),
						beforeSend : function(){
							var content = $("#content").val();
							var cArr = content.match(/[^x00-xff]/ig);
							var len = content.length + (cArr == null ? 0 : cArr.length);
							if(len>240){
								alert("留言内容不能大于120个汉字!");
								return false;
							}
							return true;
						},
						success : function(data) {
							refreshPage();
						},
						error : function() {
							alert("保存失败，请刷新页面");
						}
					});
				},
				'取消' : function() {
					$("#showComment_dialog").dialog("close");
				}
			}
		});
		$("#showComment_dialog").dialog("open");
     }

//-----------------------------------------------------------------------------
//JS基础构件 - 查询历史报表
//-----------------------------------------------------------------------------
     function rptShowHistory(reportId,nodeCode,reportDate){
        var url=contextPath + "/report/showHistory.htm?reportId="+ reportId + "&nodeCode=" + nodeCode + "&reportDate=" + reportDate;
        url += '&_ts=' + (new Date()).getTime();
        js_callpage(url);
     }

//-----------------------------------------------------------------------------
//Ajax 基础构件 - 删除收藏夹报表
//-----------------------------------------------------------------------------
   function rptDeleteFavorite(reportId){
        var ajax = rptAjaxpObject();
        if (ajax == null) {
            alert("rptAjaxpObject is null");
            return;
        }
        
        ajax.onreadystatechange = function () {
            if (ajax.readyState == 4) {
                if (ajax.status == 200) {
                    var text = ajax.responseText;
                    ajax.abort();
                    ajax = null;
                    alert("删除收藏夹报表成功！");
                }else{
                    var text=ajax.status;
                    ajax.abort();
                    ajax = null;
                    alert('error:Expcetion occured on communication with server, please try later. code:' + text);
                } 
            }    
        }; 

        try{
        	var strUrl = contextPath +  "/report/deleteFavorite.htm?reportId=" + reportId ;
            ajax.open("post", strUrl, true);
            ajax.send(null);
        }catch(err){
            ajax.abort();
            ajax=null;
        }
    };

//-----------------------------------------------------------------------------
//Ajax 基础构件 - 加入报表收藏夹
//-----------------------------------------------------------------------------
   function rptAddFavorite(reportId){
        var ajax = rptAjaxpObject();
        if (ajax == null) {
            alert("rptAjaxpObject is null");
            return;
        }
        
        ajax.onreadystatechange = function () {
            if (ajax.readyState == 4) {
                if (ajax.status == 200) {
                    var text = ajax.responseText;
                    ajax.abort();
                    ajax = null;
                    switch(text) {
                    case "exist":
                        alert("该报表已经存在于我的收藏夹中！");
                        break;
                    case "fail":
                        alert("加入报表收藏夹失败！");
                        break;
                    default:
                        alert("加入报表收藏夹成功！");
                    }
                }else{
                    var text=ajax.status;
                    ajax.abort();
                    ajax = null;
                    alert('error:Expcetion occured on communication with server, please try later. code:' + text);
                } 
            }    
        }; 

        try{
        	var strUrl = contextPath +  "/report/addFavorite.htm?reportId=" + reportId ;
            ajax.open("post", strUrl, true);
            ajax.send(null);
        }catch(err){
            ajax.abort();
            ajax=null;
        }
    };
 
//-----------------------------------------------------------------------------
//Ajax 基础构件 - 更新报表审计状态
//-----------------------------------------------------------------------------
   function rptUpdateAudit(reportId, reportNode, reportDate, status){
        var ajax = rptAjaxpObject();
        if (ajax == null) {
            alert("rptAjaxpObject is null");
            return;
        }
        ajax.onreadystatechange = function () {
            if (ajax.readyState == 4) {
                if (ajax.status == 200) {
                    var text = ajax.responseText;
                    ajax.abort();
                    ajax = null;
                    var strMessage = "";
                    if("0" == status){
                    	strMessage += "未审批 " + reportDate;
                    	strMessage += "<input type='button' value='' class='buexamine1'" ;
                    	strMessage += "onclick=\"rptUpdateAudit('" + reportId + "','" + reportNode + "','" + reportDate + "', '1')\" title='已审批'/>";
                    }else{
                    	strMessage += "已审批 " + reportDate;
                    	strMessage += "<input type='button' value='' class='buexamine2'" ;
                    	strMessage += "onclick=\"rptUpdateAudit('" + reportId + "','" + reportNode + "','" + reportDate + "', '0')\" title='未审批'/>";
                    }
                    if(document.getElementById("audit_"+reportId) !=null){
                    	document.getElementById("audit_"+reportId).innerHTML = strMessage;
                    }                    
                }else{
                    var text=ajax.status;
                    ajax.abort();
                    ajax = null;
                    alert('error:Expcetion occured on communication with server, please try later. code:' + text);
                } 
            }    
        }; 
        try{
        	var strUrl = contextPath +  "/report/updateAudit.htm?reportId=" + reportId + "&reportNode=" + reportNode+ "&reportDate=" + reportDate+ "&status=" + status ;
        	ajax.open("post", strUrl, true);
            ajax.send(null);
        }catch(err){
            ajax.abort();
            ajax=null;
        }
    };
 
//-----------------------------------------------------------------------------
//Ajax 基础构件 --Ajax 对象
//-----------------------------------------------------------------------------
    function rptAjaxpObject(){
        try {
            return new XMLHttpRequest();
        }catch (e) {}
        try {
            return new ActiveXObject('Msxml2.XMLHTTP');
        }catch (e) {}
        try {
            return new ActiveXObject('Microsoft.XMLHTTP');
        }catch (e) {}
        return null;
    };


  //-----------------------------------------------------------------------------
  //JS基础构件 --基础函数
  //-----------------------------------------------------------------------------
	 function js_callpage(htmlurl){
	    var newwin=window.open(htmlurl,'','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,top=1,left=1,width=1024,height=680');
	    return false;
	 }
	
	 function MM_openBrWindow(theURL,winName,features) { //v2.0
	    window.open(theURL,winName,'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,top=20,left=20,width=960,height=640');
	 }
		
	 function js_changepage(htmlurl){ 
		//window.open(htmlurl);
	    window.location.href=htmlurl;
	 }
	 
  
	 function SetWinHeight(obj)
	{
	    var win=obj;
	    if (document.getElementById)
	    {
	        if (win && !window.opera)
	        {
	            if (win.contentDocument && win.contentDocument.body.offsetHeight)
	
	                win.height = win.contentDocument.body.offsetHeight;
	            else if(win.Document && win.Document.body.scrollHeight)
	                win.height = win.Document.body.scrollHeight;
	        }
	    }
	} 
	 
	 