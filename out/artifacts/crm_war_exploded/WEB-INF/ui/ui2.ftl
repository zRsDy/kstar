<#macro body nav1="" nav2="" type="general" url="" >
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

		<!--[if IE]> -->
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${ctx}/assets/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
		</script>
		<!--[endif]-->

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
		
		<!-- ace scripts -->
		
		<script src="${ctx}/assets/js/fuelux/fuelux.spinner.min.js"></script>
		
		<script type="text/javascript">
			var ctx = '${ctx}';
		</script>
	</head>

	<body>
			<@navbar /> 
							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-add-container">
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			
				<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
					<i class="icon-double-angle-up icon-only bigger-110"></i>
				</a>
				<#nested>
		</div><!-- /.main-container -->
		<script type="text/javascript">
			$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
				$(this).prev().focus();
			});
			$('[data-rel=tooltip]').tooltip({container:'body'});
			$('[data-rel=popover]').popover({container:'body'});
			
			$(function(){
				if(!$('#ace-settings-add-container').prop('checked')){
					$('#ace-settings-add-container').trigger('click');
				}
			});
			
			function modifyPassword(){
				parent.dialog2("修改密码","${ctx}/modifyPassword.html",'',window);
			}
			
			var currentParent;
			var currentParent2;
			var currentDatas;
			var currentDatas1;
			var currentDatas2;
				
			
			function dialog(title,url,win){
				currentParent = win;
				currentParent2  = win;
				currentDatas = null;
				currentDatas1 = null;
				currentDatas2 = null;
				var dialog = $.dialog({
					title: title,
					width: document.body.scrollWidth || document.documentElement.scrollWidth, 
					height: document.body.scrollHeight || document.documentElement.scrollHeight, 
					lock: false,
					content:'url:' + url,
					frm:false,
					autoOpen:false,
					fixed:true
				});
				$('.ui_max').trigger('click');
			};
			
			function dialog2(title,url,datas,win,width,height){
				currentParent = win;
				currentDatas = datas;
				currentDatas1 = null;
				currentDatas2 = null;
				var dialog = $.dialog({
					title: title,
					width: width?width:1000 , 
					height: height?height:500, 
					lock: true, 
					content:'url:' + url,
					frm:false,
					fixed:true,
					left:"30%",
					top:"20%"
				});
			};
		</script>
	</body>
</html>
</#escape>
</#macro>


<#macro navbar>
<div class="navbar navbar-default" id="navbar">
	<script type="text/javascript">
		try{ace.settings.check('navbar' , 'fixed')}catch(e){}
	</script>

	<div class="navbar-container" id="navbar-container">
		
		<div class="navbar-header pull-left">
			<a href="#" class="navbar-brand">
				<small>
					<i class="icon-leaf"></i>
					<img src='${ctx}/assets/img2/head.png' style="padding-top: 10px;height: 30px;" />
				</small>
			</a><!-- /.brand -->
		</div>
		
		<div class="navbar-header pull-left">
			<ul class="nav navbar-nav">
				<li class="dropdown active">
						<a class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="0" data-close-others="false" href="#">
						Home
						<i class="fa fa-angle-down"></i>
					</a>
				</li>
				<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="0" data-close-others="false" href="#">
						市场管理
						<i class="fa fa-angle-down"></i>
					</a>
					<ul class="dropdown-menu">
						<li><a href="${ctx}/custom/baseinfo/list.html">客户信息管理</a></li>
						<li><a href="${ctx}/custom/adjust/list.html">评估调整</a></li>
						<li><a href="${ctx}/bizopp/list.html">商机信息管理</a></li>
						<li><a href="${ctx}/spePrice/index.html">特价申请信息管理</a></li>
						<li><a href="${ctx}/gift/list.html">资料与礼品库管理</a></li>
						<li><a href="${ctx}/giftApply/list.html">资料与礼品申请</a></li>
						<li><a href="${ctx}/activityApply/activity.html">市场活动申请</a></li>
						<li><a href="${ctx}/activityApply/train.html">培训申请</a></li>
						<li><a href="${ctx}/bid/index.html">投标授权申请信息管理</a></li>
						<li><a href="${ctx}/supportApply/index.html">售前技术支持申请信息</a></li>
						<li><a href="${ctx}/custom/event/list.html">来访接待</a></li>
						
						
					</ul>
				</li>
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="0" data-close-others="false" href="#">
						商务管理
						<i class="fa fa-angle-down"></i>
					</a>
					<ul class="dropdown-menu">
						<li><a href="${ctx}/orderForecast/list.html">下单预测</a></li>
						<li><a href="${ctx}/orderForecast/listGather.html">下单预测汇总</a></li>
						<li><a href="${ctx}/serviceApply/list.html">服务申请</a></li>
						<li><a href="${ctx}/order/list.html">订单查询</a></li>
						<li><a href="${ctx}/delivery/list.html">出货申请</a></li>
						<li><a href="${ctx}/importSale/list.html">引入销量</a></li>
						<li><a href="${ctx}/rebatePolicy/list.html">返利政策</a></li>
						<li><a href="${ctx}/rebateProduct/list.html">返利产品</a></li>
						<li><a href="${ctx}/rebateDetail/list.html">返利明细</a></li>
						<li><a href="${ctx}/rebateSettle/list.html">返利结算</a></li>
						<li><a href="${ctx}/accounts/list.html">账期申请</a></li>
						<li><a href="${ctx}/statement/list.html">对账申请</a></li>
					</ul>
				</li>						
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-delay="0" data-close-others="false" href="#">
						服务申请
						<i class="fa fa-angle-down"></i>
					</a>
					<ul class="dropdown-menu">
						<li><a href="${ctx}/serviceApply/list.html">客户服务管理</a></li>
					</ul>
				</li>
			</ul>
		</div>
		
		<!-- /.navbar-header -->
		<div class="navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<li class="light-blue">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle">
							${(LOGIN_USER.employee.name)!}
						<i class="icon-caret-down"></i>
					</a>

					<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
						<#list positions as position>
							<li>
								<a href="#" 
								<#if LOGIN_USER.position?? && LOGIN_USER.position.id == position.id>
									style="background-color: #438eb9;"
								</#if>
								 >
									<#if LOGIN_USER.position?? && LOGIN_USER.position.id == position.id>
									<img src='${ctx}/assets/img2/user.png' style="padding-top: 0px;height: 18px;" />
									</#if>
									${position.name}
								</a>
							</li>
						</#list>
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