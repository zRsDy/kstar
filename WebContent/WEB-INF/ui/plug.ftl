<#macro tree id datas='' moveEdit = 'false' onMove = "function(){}"
onClick='' ondblClick='' checkbox='true' async='true' expandAll='false' onComplete ="function(){}" viewName="function(data){
	return data.name ;
}"
 asyncUrl='' isParent="function(data){return data.leafFlag == 'N';}" dataFilter = "undefined" chkboxType="{ 'Y' : 'ps',  'N' : 'ps' }" onRightClick='' onCheckClick=''  onCheck='function(event, treeId, treeNode){}'
 >  
	<ul id="${id}" class="ztree"></ul>
	<script type="text/javascript">
		var curDragNodes;
		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
		}
		
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
			var onMove = ${onMove};
			onMove(treeNodes,targetNode,moveType);
			//moveType 有三种情况，上移，下移，移出
		}
	
		var setting_${id} ={
			<#if moveEdit == 'true'>
			edit: {
				drag: {
					autoExpandTrigger: true
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			</#if>
			data: {
				simpleData: {
					enable: true
				}
			}
		}; 
		setting_${id}.data.simpleData.idKey='id';
		setting_${id}.data.simpleData.pIdKey='parentId';
		setting_${id}.data.simpleData.rootPId='0';
		
		var dataFilter_${id} = ${dataFilter};
		
		setting_${id}.callback = {
			<#if onCheck?? && onCheck!=''>
				onCheck : ${onCheck},
			</#if>
			<#if onClick?? && onClick!=''>
				onClick: ${onClick},
			</#if>
			
			<#if ondblClick?? && ondblClick!=''>
				onDblClick: ${ondblClick},
			</#if>
			
			<#if onRightClick?? && onRightClick!=''>
				onRightClick: ${onRightClick},
			<#else>
				onRightClick:function(event, treeId, treeNode) {
				   	var treeObj = $.fn.zTree.getZTreeObj(treeId);
					var nodes = treeObj.getNodes();
					if (nodes.length>0) {
						treeObj.cancelSelectedNode();
					}
					selectedData = null;
				},
			</#if>
			<#if moveEdit == 'true'>
				beforeDrag: beforeDrag,
				onDrop: onDrop,
			</#if>
			onAsyncSuccess: function(event, treeId, treeNode, msg) {
			  	var treeObj = $.fn.zTree.getZTreeObj(treeId);
			  	$("span[treenode_check]").click(function(){
					<#if onCheckClick?? && onCheckClick!=''>
					var onCheckClick = ${onCheckClick};
					onCheckClick();
					</#if>
				})
				<#if onComplete?? && onComplete!=''>
				var onComplete = ${onComplete};
				onComplete();
				</#if>
			}
		}
		if(${checkbox}){
			setting_${id}.check = { enable : true };
			setting_${id}.check.chkboxType = ${chkboxType};
		}
		<#if async?? && async=='true'>
			setting_${id}.async = { enable : true };
			setting_${id}.async.url = "${asyncUrl}";
			setting_${id}.async.dataFilter= ajaxDataFilter_${id};
			setting_${id}.async.autoParam = ["id"];
			function ajaxDataFilter_${id}(treeId, parentNode, responseData) {
				var result = responseData.message;
				var isParentFunction = ${isParent};
				var viewName = ${viewName}; 
				for(var i =0; i < result.length; i++) {
			        result[i].isParent = isParentFunction(result[i]);
				    if(dataFilter_${id}){
						result[i] = dataFilter_${id}(result[i]);
					}
					result[i].name = viewName(result[i]); 
					result[i].lv = result[i].level; 
			    }
			    return responseData.message;
			};
			$(function($) {
				$.fn.zTree.init($("#${id}"), setting_${id});
			});
		<#else>
			var datas_${id} = ${datas};
			$(function($) {
				$.fn.zTree.init($("#${id}"), setting_${id}, datas_${id});
			});
		</#if>
		
		function reload_${id}(nodeId){
			var treeObj = $.fn.zTree.getZTreeObj("${id}");
			var node = treeObj.getNodeByParam("id",nodeId,null);
			treeObj.reAsyncChildNodes(node, "refresh");
		}
		
		function deletAndreload_${id}(nodeId){
			var treeObj = $.fn.zTree.getZTreeObj("${id}");
			var node = treeObj.getNodeByParam("id",nodeId,null);
			if(node==null){
				return;
			}
				
			var nextNode = node.getPreNode();
			if(nextNode==null){
				nextNode = node.getNextNode();
			}
			if(nextNode==null){
				nextNode = node.getParentNode();
			}
			treeObj.removeNode(node);
			if(nextNode!=null){
				treeObj.selectNode(nextNode);
				$('#parentId').val(nextNode.id);
				reload_t1();
			}
		} 
		
	</script>
</#macro>

<#macro showMore id="" area=".showMore">
	<a href="#" id="showMoreBtn_${id}">更多</a>
	<a href="#" id="hideMoreBtn_${id}" class="hide">收起</a>
	<script type="text/javascript">
		$("#showMoreBtn_${id}").click(function(){
			$("${area}").removeClass("hide");
			$("#hideMoreBtn_${id}").removeClass("hide");
			$("${area}").show();
			$("#hideMoreBtn_${id}").show();
			$("#showMoreBtn_${id}").hide();
		});
		
		$("#hideMoreBtn_${id}").click(function(){
			$("${area}").hide();
			$("#showMoreBtn_${id}").show();
			$("#hideMoreBtn_${id}").hide();
		});
	</script>
</#macro>

<#macro tabs tabMain id="" >
<style>

</style>

	<div id="${tabMain.id}" class="tabbable tabs-${tabMain.style}">
		<ul class="nav nav-tabs" >
			<#list tabMain.list as tab>
				<li class="">
					<a data-toggle="tab" href="#${tab.id}"
					<#if !tabMain.initAll>
						onclick='showPage_${tabMain.id}("${tab.id}","${ctx}${tab.url}")'
					</#if>
					>
						<i class="${(tab.styleClass)!'pink icon-dashboard bigger-110'}"></i>
						${i18n.get('${(tab.title)!}')}
					</a>
				</li>
			</#list>
		</ul>
	
			<#list tabMain.list as tab>
				<div style="padding: 3px 3px 3px 3px;border: 0px solid #c5d0dc;" id="${tab.id}" class="tab-pane">
					
				</div>
			</#list>
	</div>

	<script type="text/javascript">

        $("#${tabMain.id}").tabs();
			<#if tabMain.initAll>
			function reload_${id}(name) {
				<#list tabMain.list as tab>
					if (name == "${tab.title}") {
                        $('#${tab.id}').load("${ctx}${tab.url}", function (response, status, xhr) {
                            if (status === 'error') {
                                $('#' + tabId).html('页面加载失败!!');
                            }
                        });
                    }
				</#list>
            }
            $(function () {
				<#list tabMain.list as tab>
				$("#${tab.id}").load("${ctx}${tab.url}");
				</#list>
            });

			<#else>
				<#list tabMain.list as tab>
					<#if tab_index == 0>
				showPage_${tabMain.id}("${tab.id}", "${ctx}${tab.url}");
					</#if>
				</#list>
			
			var loadimg = "<img src ='${ctx}/assets/img/loading.gif' /> ";
			function showPage_${tabMain.id}(tabId, url) {
                $('#' + tabId).tab('show');
                $('#' + tabId).html(loadimg + ' 页面加载中，请稍后...');
                $('#' + tabId).load(url, function (response, status, xhr) {
                    if (status === 'error') {
                        $('#' + tabId).html('页面加载失败!!');
                    }
                });
            }
			</#if>
	</script>
</#macro>


<#macro treeDesign id datas='' moveEdit = 'false' onMove = "function(){}"
onClick='' ondblClick='' checkbox='true' async='true' expandAll='false' onComplete ="function(){}" viewName="function(data){
	return data.name ;
}"
 asyncUrl='' isParent="function(data){return data.leafFlag == 'N';}" dataFilter = "undefined" chkboxType="{ 'Y' : 'ps',  'N' : 'ps' }" onRightClick='' onCheckClick=''  onCheck='function(event, treeId, treeNode){}'
 >  
	<ul id="${id}" class="ztree"></ul>
	<script type="text/javascript">
		var curDragNodes;
		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
		}
		
		function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
			var onMove = ${onMove};
			onMove(treeNodes,targetNode,moveType);
			//moveType 有三种情况，上移，下移，移出
		}
	
		var setting_${id} ={
			<#if moveEdit == 'true'>
			edit: {
				drag: {
					autoExpandTrigger: true
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			</#if>
			data: {
				simpleData: {
					enable: true
				}
			}
		}; 
		setting_${id}.data.simpleData.idKey='id';
		setting_${id}.data.simpleData.pIdKey='parentId';
		setting_${id}.data.simpleData.rootPId='0';
		
		var dataFilter_${id} = ${dataFilter};
		
		setting_${id}.callback = {
			<#if onCheck?? && onCheck!=''>
				onCheck : ${onCheck},
			</#if>
			<#if onClick?? && onClick!=''>
				onClick: ${onClick},
			</#if>
			
			<#if ondblClick?? && ondblClick!=''>
				onDblClick: ${ondblClick},
			</#if>
			
			<#if onRightClick?? && onRightClick!=''>
				onRightClick: ${onRightClick},
			<#else>
				onRightClick:function(event, treeId, treeNode) {
				   	var treeObj = $.fn.zTree.getZTreeObj(treeId);
					var nodes = treeObj.getNodes();
					if (nodes.length>0) {
						treeObj.cancelSelectedNode();
					}
					selectedData = null;
				},
			</#if>
			<#if moveEdit == 'true'>
				beforeDrag: beforeDrag,
				onDrop: onDrop,
			</#if>
			onAsyncSuccess: function(event, treeId, treeNode, msg) {
			  	var treeObj = $.fn.zTree.getZTreeObj(treeId);
			  	$("span[treenode_check]").click(function(){
					<#if onCheckClick?? && onCheckClick!=''>
					var onCheckClick = ${onCheckClick};
					onCheckClick();
					</#if>
				})
				<#if onComplete?? && onComplete!=''>
				var onComplete = ${onComplete};
				onComplete();
				</#if>
			}
		}
		if(${checkbox}){
			setting_${id}.check = { enable : true };
			setting_${id}.check.chkboxType = ${chkboxType};
		}
		<#if async?? && async=='true'>
			setting_${id}.async = { enable : true };
			setting_${id}.async.url = "${asyncUrl}";
			setting_${id}.async.dataFilter= ajaxDataFilter_${id};
			setting_${id}.async.autoParam = ["id"];
			function ajaxDataFilter_${id}(treeId, parentNode, responseData) {
				var result = responseData.message;
				var isParentFunction = ${isParent};
				var viewName = ${viewName}; 
				for(var i =0; i < result.length; i++) {
			        result[i].isParent = isParentFunction(result[i]);
				    if(dataFilter_${id}){
						result[i] = dataFilter_${id}(result[i]);
					}
					result[i].name = viewName(result[i]); 
					result[i].lv = result[i].level; 
			    }
			    return responseData.message;
			};
			$(function($) {
				$.fn.zTree.init($("#${id}"), setting_${id});
			});
		<#else>
			var result = ${datas};
			for(var i =0; i < result.length; i++) {
				var isParentFunction = ${isParent};
				var viewName = ${viewName}; 
			    result[i].isParent = isParentFunction(result[i]);
			    if(dataFilter_${id}){
					result[i] = dataFilter_${id}(result[i]);
				}
				result[i].name = viewName(result[i]); 
				result[i].lv = result[i].level; 
		    }
			$(function($) {
				$.fn.zTree.init($("#${id}"), setting_${id}, result);
			});
		</#if>
		
		function reload_${id}(nodeId){
			var treeObj = $.fn.zTree.getZTreeObj("${id}");
			var node = treeObj.getNodeByParam("id",nodeId,null);
			treeObj.reAsyncChildNodes(node, "refresh");
		}
		
		function deletAndreload_${id}(nodeId){
			var treeObj = $.fn.zTree.getZTreeObj("${id}");
			var node = treeObj.getNodeByParam("id",nodeId,null);
			if(node==null){
				return;
			}
				
			var nextNode = node.getPreNode();
			if(nextNode==null){
				nextNode = node.getNextNode();
			}
			if(nextNode==null){
				nextNode = node.getParentNode();
			}
			treeObj.removeNode(node);
			if(nextNode!=null){
				treeObj.selectNode(nextNode);
				$('#parentId').val(nextNode.id);
				reload_t1();
			}
		} 
		
	</script>
</#macro>