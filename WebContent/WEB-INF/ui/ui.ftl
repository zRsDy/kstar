
<#macro body nav1="" nav2="" type="general" url="">
<#escape x as x?html>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>Kstar CRM</title>

		<meta name="description" content="Static &amp; Dynamic Tables" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!-- basic styles -->

		<link href="${ctx}/assets/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="${ctx}/assets/css/font-awesome.min.css" />

		<!--[if IE 7]>
		  <link rel="stylesheet" href="${ctx}/assets/css/font-awesome-ie7.min.css" />
		<![endif]-->

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="${ctx}/assets/css/jquery-ui-1.10.3.full.min.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/datepicker.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/ui.jqgrid.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/jquery.gritter.css" />
		<!-- fonts -->

		<!-- ace styles -->
		<link rel="stylesheet" href="${ctx}/assets/css/ace.min.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/ace-rtl.min.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/ace-skins.min.css" />
		<link rel="stylesheet" href="${ctx}/assets/js/select2/select2.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/zTreeStyle/zTreeStyle.css" />
		<link rel="stylesheet" href="${ctx}/assets/css/my-styles.css" />
		
		<style>
			.ui-icon-seek-first:before {
			    content: "<<";
			}
			.ui-icon-seek-prev:before {
			    content: "<";
			}
			.ui-icon-seek-next:before {
			    content: ">";
			}
			.ui-icon-seek-end:before {
			    content: ">>";
			}
			
		</style>
		
		<!--[if lte IE 8]>
			<link rel="stylesheet" href="${ctx}/assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- page specific plugin scripts -->

		<!--[if lte IE 8]>
		  <script src="${ctx}/assets/js/excanvas.min.js"></script>
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->

		<script src="${ctx}/assets/js/ace-extra.min.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lt IE 9]>
		<script src="${ctx}/assets/js/html5shiv.js"></script>
		<script src="${ctx}/assets/js/respond.min.js"></script>
		<![endif]-->
		
		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctx}/assets/js/jquery-2.0.3.min.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->
		
		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${ctx}/assets/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		

		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='${ctx}/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="${ctx}/assets/js/bootstrap.min.js"></script>
		<script src="${ctx}/assets/js/typeahead-bs2.min.js"></script>

		<!-- page specific plugin scripts -->
		
		<script src="${ctx}/assets/js/jquery-ui-1.10.3.full.min.js"></script>
		<script src="${ctx}/assets/js/date-time/bootstrap-datepicker.min.js"></script>
		<script src="${ctx}/assets/js/jqGrid/jquery.jqGrid.min.js"></script>
		<script src="${ctx}/assets/js/jqGrid/i18n/grid.locale-en.js"></script>

		<!-- ace scripts -->

		<script src="${ctx}/assets/js/ace-elements.min.js"></script>
		<script src="${ctx}/assets/js/ace.min.js"></script>
		
		<script src="${ctx}/assets/js/dialog/lhgdialog.js"></script>
		<script src="${ctx}/assets/js/jquery.validate.min.js"></script>
		<script src="${ctx}/assets/js/jquery-form.min.js"></script>
		<script src="${ctx}/anne/js/ajax.js"></script>
		<script src="${ctx}/anne/js/anne.js"></script>
		
		<script src="${ctx}/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script src="${ctx}/assets/js/bootbox.min.js"></script>
		<script src="${ctx}/assets/js/jquery.easy-pie-chart.min.js"></script>
		<script src="${ctx}/assets/js/jquery.gritter.min.js"></script>
		<script src="${ctx}/assets/js/spin.min.js"></script>
		<script src="${ctx}/assets/js/select2/select2.min.js"></script>
		<script src="${ctx}/assets/js/jquery.ztree.all.min.js"></script>
        <script src="${ctx}/anne/js/echarts.js"></script>
		
		<!-- ace scripts -->
		
		<script src="${ctx}/assets/js/fuelux/fuelux.spinner.min.js"></script>
		
		<script type="text/javascript">
			var ctx = '${ctx}';
		</script>
	</head>

	<body>
		<div class="sysNotice"></div>
			<@navbar />
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			<div class="main-container-inner">
			<a class="menu-toggler" id="menu-toggler" href="#">
				<span class="menu-text"></span>
			</a>
			<script>
			$(document).ready(function(){
				var height = $(window).height()-50;
				$("#sidebar").css({height:height,overflow:'auto'});
			});
			</script>
			<@m.menu url="${url}"/>
			<div class="main-content">  <!-- main-content -->
				<div class="page-content" id="page-content">
					<#nested>
				</div>
			</div>		<!-- main-content -->
			</div><!-- /.main-container-inner -->
				<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
					<i class="icon-double-angle-up icon-only bigger-110"></i>
				</a>
		</div><!-- /.main-container -->
		<script type="text/javascript">
			$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
				$(this).prev().focus();
			});
			$('[data-rel=tooltip]').tooltip({container:'body'});
			$('[data-rel=popover]').popover({container:'body'});
			
			function changePosition(positionId,positionName){
				parent.confirm("你确定要切换岗位吗?切换后，页面将被重置刷新。", function(result) {
						if(result) {
							ajax({
								type:"post",
								url : '${ctx}/changePosition.html?positionId='+positionId,
								success : function(result) {
									location.href='main.html';
								},
								error : function(result) {
									exalert(e,result);
								}
							});
						}
					});
			}
			
			$(function(){
				if($('#ace-settings-add-container').prop('checked')){
					$('#ace-settings-add-container').trigger('click');
				}
			});
			
			function modifyPassword(){
				parent.dialog2("修改密码","${ctx}/modifyPassword.html",'',window);
			}
			
			function changeLanguage(lang){
				ajax({
					type:"get",
					url : '${ctx}/language/change.html?language='+lang,
					success : function(result) {
						alert("切换成功!");
						location.href='main.html';
					},
					error : function(result) {
						exalert(e,result);
					}
				});
			}
			/*
			function showflowList(){  
		        $.ajax({  
		            type: "POST",  
		            url: "${ctx}/flow/page.html",  
		            dataType: "json",  
		            success: function(data) {
		            	if(data.message.count > 0){
		            		$('#myTaskCount').html(data.message.count);
		            		var html = "<li class='dropdown-header'> <a href='#' onclick=addTab('待办任务','${ctx}/flow/list.html')>待办流程</a> </li> ";
			                $.each(data.message.list,function(i,item){
			                	if(i<10){
			                		var url = "${ctx}/flow/process.html?taskId="+item.id+"&id="+item.businessKey;
			                		var m = "parent.dialog('处理流程','"+url+"',window)";
			                		var c = "onclick="+m;
			                		html = html + "<li><a href='#' "+c+" >"+item.title+"</a></li>";	
		                		}
			                })
			                $('#taskList').html(html);
		            	}else{
		            		$('#myTaskCount').html('');
		            		var html = "<li class='dropdown-header'> <a href='#' onclick=addTab('待办任务','${ctx}/flow/list.html')>待办流程</a> </li> ";
		            		$('#taskList').html(html);
		            	}
		            }
		        });  
			}
			*/
			
			function showflowList(){  
		        $.ajax({  
		            type: "POST",  
		            url: "${ctx}/flow/crmOrPdmPage.html",  
		            dataType: "json",  
		            success: function(data) {
			                debugger;
		            	if(data.message.count > 0){
		            		$('#myTaskCount').html(data.message.count);
		            		var html = "<li class='dropdown-header'> <a href='#' onclick=addTab('待办任务','${ctx}/flow/list.html')>待办流程</a> </li> ";
			                $.each(data.message.list,function(i,item){
			                	if(i<10){
			                		if(item.hasOwnProperty("activityType")){
				                		var url = "${ctx}/flow/process.html?taskId="+item.id+"&id="+item.businessKey;
				                		var m = "parent.dialog('处理流程','"+url+"',window)";
				                		var c = "onclick="+m;
				                		html = html + "<li><a href='#' "+c+" >"+item.title+"</a></li>";
				                	}else{
				                		var url = "${ctx}/flow/pdmProcess.html?procId="+item.procId+"&id="+item.rowid+"&formNo="+item.formNo;
				                		var m = "parent.dialog('处理非标PDM流程','"+url+"',window)";
				                		var c = "onclick="+m;
				                		var title = item.name+' - '+item.procName+' - '+item.statusName
				                		html = html + "<li><a href='#' "+c+" >"+title+"</a></li>";
				                	}
		                		}
			                })
			                $('#taskList').html(html);
		            	}else{
		            		$('#myTaskCount').html('');
		            		var html = "<li class='dropdown-header'> <a href='#' onclick=addTab('待办任务','${ctx}/flow/list.html')>待办流程</a> </li> ";
		            		$('#taskList').html(html);
		            	}
		            }
		        });  
			}
			
			
			function showNotice(){  
		        $.ajax({  
		            type: "POST",  
		            url: "${ctx}/getNotice.html",  
		            dataType: "json",  
		            success: function(data) {
		            	if(data.message){
			            	var html = "<div class='alert alert-block alert-success'><button type='button' class='close' data-dismiss='alert'><i class='icon-remove'></i></button><strong class='green noticeContent'>"+data.message+"</strong></div>";
			            	$(".sysNotice").html(html);
		            	}else{
		            		$(".sysNotice").html("");
		            	}
		            }
		        });
			}
		showflowList();
   		setInterval('showflowList()',60000);
   		setInterval('showNotice()',60000);
		</script>
	</body>
</html>
</#escape>
</#macro>


<#macro navbar>
<div class="navbar navbar-default" id="navbar"
<#if !LOGIN_USER.inner>
style="background-color: lightseagreen;"
</#if>
>
	<script type="text/javascript">
		try{ace.settings.check('navbar' , 'fixed')}catch(e){}
	</script>

	<div class="navbar-container" id="navbar-container">
		
		<div class="navbar-header pull-left">
			<a href="#" class="navbar-brand">
				<small>
					<i class="icon-leaf"></i>
					
					<#if !LOGIN_USER.inner>
					<img src='${ctx}/assets/img2/head1.png' style="padding-top: 10px;height: 30px;" />
					<#else>
					<img src='${ctx}/assets/img2/head.png' style="padding-top: 10px;height: 30px;" />
					</#if>
				</small>
			</a><!-- /.brand -->
		</div>
		
		<!-- /.navbar-header -->
		<div class="navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<li>
					<a data-toggle="dropdown" class="dropdown-toggle" href="#" 
						<#if !LOGIN_USER.inner>
							style="background-color: lightseagreen;"
						<#else>
							style= "background-color: #438eb9;"
						</#if>
					>
						<i class="icon-bell-alt"></i>
						<span class="badge badge-important" id="myTaskCount"></span>
					</a>

					<ul class="pull-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close" id="taskList" style="width: 400px;">
						
					</ul>
				</li>
				
				<li class="light-blue" >
					
					<a data-toggle="dropdown" href="#" class="dropdown-toggle" 
						
						<#if !LOGIN_USER.inner>
							style="background-color: lightseagreen;"
						<#else>
							style= "background-color: #438eb9;"
						</#if>

					>
							${(LOGIN_USER.employee.name)!}
						<i class="icon-caret-down"></i>
					</a>

					<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li>
								<#list positions as position>
										<a href="#" onclick="changePosition('${position.id}','position.name')"
										<#if LOGIN_USER.position?? && LOGIN_USER.position.id == position.id>
											<#if !LOGIN_USER.inner>
												style="background-color: lightseagreen;"
											<#else>
												style= "background-color: #438eb9;"
											</#if>
										</#if>
										 >
											<#if LOGIN_USER.position?? && LOGIN_USER.position.id == position.id>
											<img src='${ctx}/assets/img2/user.png' style="padding-top: 0px;height: 18px;" />
											</#if>
											${position.name}
										</a>
								</#list>
							</li>
							
							<li>
								<#if LOGIN_USER.employee.no == 'admin'>
									<@form.select style="width: 160px;" placeholder="选择其他岗位" id="cpositionId" name="cpositionId" code="POSITION" errorLabel='false'
									onChange="function(){
										changePosition($('#cpositionId').val());
									}"
									 />
								</#if>
							</li>
						
						<li class="divider"></li>



						<li>
							<a href="#"  onclick="modifyPassword()">
								<i class="icon-user"></i>
								修改密码
							</a>
							<a href="${ctx}/logout.html">
								<i class="icon-off"></i>
								退出系统
							</a>
						</li>
						<li class="divider"></li>
						
						<li>
							<a href="#"  onclick="changeLanguage('zh')">
								中文
							</a>
							<a href="#"  onclick="changeLanguage('en')">
								English
							</a>
						</li>
						
						
					</ul>
				</li>
			</ul><!-- /.ace-nav -->
		</div><!-- /.navbar-header -->
		
		<div class="navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav"><li style=" color: white;">
 			<img src='${ctx}/assets/img2/user.png' style="padding-top: 0px;height: 18px;" />   ${(LOGIN_USER.position.name)!}  &nbsp;&nbsp;&nbsp;&nbsp; </li></ul>
		</div>
		
	</div><!-- /.container -->
</div>
</#macro>

