<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>邮政储蓄银行管理会计系统</title>
		<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${res}/easyui/js/jquery-easyui-1.4/themes/ui-sunny/easyui.css">
		<link rel="stylesheet" type="text/css" href="${res}/easyui/js/jquery-easyui-1.4/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${res}/easyui/js/jquery-easyui-1.4/themes/color.css">
		<link rel="stylesheet" type="text/css" href="${res}/easyui/css/base.css">
		<link rel="stylesheet" type="text/css" href="${res}/easyui/css/main.css">
		<!-- ** Javascript ** -->
		<script type="text/javascript" src="${res}/easyui/js/commons/jquery.min.js"></script>
		<script type="text/javascript" src="${res}/easyui/js/commons/jquery-ui.js"></script>
		<script type="text/javascript" src="${res}/easyui/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${res}/easyui/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${res}/anne/js/main.js"></script>
		<script type="text/javascript" src="${res}/easyui/js/jquery-easyui-1.4/plugins/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${res}/easyui/js/jquery-easyui-1.4/plugins/jquery-form.min.js"></script>
		<script type="text/javascript" src="${res}/anne/js/ajax.js"></script>
		<script type="text/javascript" src="${res}/anne/js/anne.js"></script>
		<script type="text/javascript">
			$(function() {
				main.init();
			});
		</script>
	</head>
	
	<body>
		<body class="easyui-layout">
		<div class="ui-header"
			data-options="region:'north',split:true,border:false"
			style="height: 40px; overflow: hidden;">
			<div class="ui-login">
				<div class="ui-login-info">
					<a class="logout-btn" href="logout.html">退出</a>
				</div>
			</div>
		</div>
		<!-- 树形菜单 -->
		<div data-options="region:'west',split:true,title:'目录'" style="width: 200px;">
			<div id="tree-box" class="easyui-accordion" data-options="fit:true,border:false">
				<div title="多维经营考核指标">
					<a class="menu-item" name="指标定义" href="list.html" >
						<img src="${res}/easyui/images/hxb/menu_icon.gif" />指标定义
					</a>
				
					<a class="menu-item" name="基础指标口径定义" href="list.html" >
						<img src="${res}/easyui/images/hxb/menu_icon.gif" />基础指标口径定义
					</a>
				
					<a class="menu-item" name="衍生指标定义" href="list.html" >
						<img src="${res}/easyui/images/hxb/menu_icon.gif" />衍生指标定义
					</a>
				
					<a class="menu-item" name="指标灵活查询" href="list.html" >
						<img src="${res}/easyui/images/hxb/menu_icon.gif" />指标灵活查询
					</a>
				</div>
			</div>
		</div>

		<!-- 中间内容页面 -->
		<div data-options="region:'center'">
			<div class="easyui-tabs" id="tab-box" data-options="fit:true,border:false">
				<div title="主页" style="padding: 20px; overflow: hidden;">
					<div style="margin-top: 20px;">
						
					</div>
				</div>
			</div>
		</div>
		
		<div data-options="region:'south'" style="height: 30px">
			
		</div>
		
	</body>
	</body>
</html>
