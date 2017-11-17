
	function changePeriod() {
		alert("切换报告期/分支机构");
         var url="com.ibm.riskintegration.report.changePeriod.flow";
         url += '?_ts=' + (new Date()).getTime();
         
	     //var temp = window.showModalDialog(url,"1234",'dialogHeight:108px;dialogWidth:332px;center:yes;status:no;location:no;');
	     
	     //if(temp == '0'){
	     //	changePeriodRefresh();
	     //}
	     
	     //showModal(url,null, changePeriodRefresh, 640,160,240,30,"切换报告期/分支机构");
	}

	function changePeriodRefresh() {
	  if(parent){
	     if(parent.frames["topFrame"]){
	  		parent.frames["topFrame"].changePeriodRefresh();
	     }
	  }
	  parent.frames["mainFrame"].document.location.reload();
	}

	
	function retrieveComment2(reportId){
	     var myAjax = new Ajax("com.ibm.riskintegration.report.rifreportcommentbiz.retrieveReportComment.biz");	     
	     myAjax.addParam('reportId',reportId);
	     myAjax.submit ();

	     var strComment = myAjax.getValue('/root/data/rifreportcomment/content');
	     	     
         if(typeof(strComment)=="undefined" || strComment ==null || strComment == ""){
             strComment = "";
		 }
	     if(document.getElementById("comment_"+reportId) !=null){
            document.getElementById("comment_"+reportId).innerHTML = strComment;
         }

         var strTemp = myAjax.getValue('/root/data/rifreportcomment/docfilelink');
		 var strDownload = "<a href='com.ibm.riskintegration.report.docManager.flow?documentId=" + myAjax.getValue('/root/data/rifreportcomment/docfilelink') + "'>(附件：" + myAjax.getValue('/root/data/rifreportcomment/docfilename') + ")</a>" ;
         	     
         if(typeof(strDownload)=="undefined" || strTemp ==null || strTemp == ""){
             strDownload = "";
		 }
	     if(document.getElementById("download_"+reportId) !=null){
            document.getElementById("download_"+reportId).innerHTML = strDownload;
         }
		
         strTemp = myAjax.getValue('/root/data/rifreportcomment/imgfilelink');
	     var strImage = "<img src='com.ibm.riskintegration.report.docManager.flow?documentId=" + myAjax.getValue('/root/data/rifreportcomment/imgfilelink') + "'/>" ;
	     
         if(typeof(strDownload)=="undefined" || strTemp ==null || strTemp == ""){
             strImage = "";
		 }
	     if(document.getElementById("image_"+reportId) !=null){          
            document.getElementById("image_"+reportId).innerHTML = strImage;
         }
    }
	 
	function retrieveComment(reportId){
	     var myAjax = new Ajax("com.ibm.riskintegration.report.rifreportcommentbiz.retrieveReportComment.biz");	     
	     myAjax.addParam('reportId',reportId);
	     myAjax.submit ();

	     var strComment = myAjax.getValue('/root/data/rifreportcomment/content');
	     	     
         if(typeof(strComment)=="undefined" || strComment ==null || strComment == ""){
             strComment = "";
		 }
	     if(document.getElementById("comment_"+reportId) !=null){
            document.getElementById("comment_"+reportId).innerHTML = strComment;
         }

         var strDocumentIds = myAjax.getValues('/root/data/rifreportcomment/docFiles/documentId');
         var strDocumentNames = myAjax.getValues('/root/data/rifreportcomment/docFiles/documentName');
         var strDownload = "";
         
         for(var k=0; k<strDocumentIds.length; k++){
		    strDownload += "<a href='com.ibm.riskintegration.report.docManager.flow?documentId=" + strDocumentIds[k] + "'>[附件：" + strDocumentNames[k] + "]</a><br>" ;
         }
         
         if(typeof(strDownload)=="undefined" || strDownload ==null || strDownload == ""){
             strDownload = "";
		 }
	     if(document.getElementById("download_"+reportId) !=null){
            document.getElementById("download_"+reportId).innerHTML = strDownload;
         }
		
		
         strDocumentIds = myAjax.getValues('/root/data/rifreportcomment/imgFiles/documentId');
         strDocumentNames = myAjax.getValues('/root/data/rifreportcomment/imgFiles/documentName');
         strDownload = "";
         
         for(var k=0; k<strDocumentIds.length; k++){
		    strDownload += "<img width='480' src='com.ibm.riskintegration.report.docManager.flow?documentId=" +strDocumentIds[k] + "'/><br> " ;
         }
         
         if(typeof(strDownload)=="undefined" || strDownload ==null || strDownload == ""){
             strDownload = "";
		 }
		 
	     if(document.getElementById("image_"+reportId) !=null){          
            document.getElementById("image_"+reportId).innerHTML = strDownload;
         }
    }
     
     function showComment(reportId){
         var url="com.ibm.riskintegration.report.showComment.flow?_eosFlowAction=start&reportId="+reportId
         url += '&_ts=' + (new Date()).getTime();
         js_callpage(url);         
     }
     
     function showFavorite(reportId){
         var url="com.ibm.riskintegration.report.showHistory.flow?reportId="+reportId
         url += '&_ts=' + (new Date()).getTime();
         js_callpage(url);
         
         //js_callpage("com.ibm.riskintegration.report.showReportFavorite.flow?_eosFlowAction=start&reportId="+reportId);
         
         // 显示对话框,参数依次为,
         // 对话框内的页面url
         // 传给对话框的参数
         // 宽度, 高度 左坐标 上坐标
         // 对话框标题
         //showModal ("com.ibm.riskintegration.report.showHistory.flow?_eosFlowAction=start&reportId="+reportId,reportId, null, 960,720,100,100,"查看历史数据");
     }
     
	 
	function show(strs,editable){
	    var risk = strs;
	    var arr = new Array();
	    arr = risk.split("@@");
	    var arrA =  new Array(); 
	    for(var k=0;k<arr.length;k++){
	       arrA = arr[k].split("_@");
	
	       if(arrA[0] !=null && arrA[0] !=""){
				if(document.getElementById("iframe_"+arrA[0]) !=null){					
				   document.getElementById("iframe_"+arrA[0]).src = arrA[1];
				}
		   }
	
	       var strComment = "";
	       if('Y' == editable){
	       }
	       
            strComment = strComment + '<a class="edit" title="文字文件上传" href="#" onclick="modifyComment(' + arrA[0] + ')"></a>';
            strComment = strComment + '<a class="look" title="查看历史数据" href="#" onclick="showHistory(' + arrA[0] + ')"></a>';
            strComment = strComment + '<a class="collect" title="加入收藏" href="#" onclick="addFavorite(' + arrA[0] + ')"></a>';
            
			if(document.getElementById("tools_"+arrA[0]) !=null){
			   document.getElementById("tools_"+arrA[0]).innerHTML = strComment;
			}
	    }
	}
	
 
//==============================

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
