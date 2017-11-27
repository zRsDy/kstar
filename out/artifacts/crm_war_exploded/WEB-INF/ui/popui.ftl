<#macro body nav1="" nav2="" type="popup" url="">
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
            .ABC{
                height: 25px;
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

        <!-- <script src="${ctx}/assets/js/dialog/lhgdialog.js"></script> -->
        <script src="${ctx}/assets/js/jquery.validate.min.js"></script>
        <script src="${ctx}/assets/js/jquery-form.min.js"></script>
        <script src="${ctx}/anne/js/ajax.js"></script>
        <script src="${ctx}/anne/js/anne.js"></script>
        <script src="${ctx}/anne/js/echarts.js"></script>


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
            try{
                var api = frameElement.api, opener = api.opener;
            }catch(e){ }
            function close(){
                api.close();
            }
        </script>
        <script type="text/javascript">
            var ctx = '${ctx}';
        </script>
    </head>

    <body>
    <div class="main-container" id="main-container">
        <script type="text/javascript">
            try{ace.settings.check('main-container' , 'fixed')}catch(e){}
        </script>
        <div class="main-container-inner">

            <div class="page-content">
				<#nested>
            </div>

        </div><!-- /.main-container-inner -->
    </div><!-- /.main-container -->
    <script type="text/javascript">
        $('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
            $(this).prev().focus();
        });
        $('[data-rel=tooltip]').tooltip({container:'body'});
        $('[data-rel=popover]').popover({container:'body'});
        $(document).ready(function(){
            $('.panel').click(function(e){
                if($(this).children('p')[0] == e.target){
                    var a = this;
                    $(this).children('table').toggle();
                    $(a).toggleClass('ABC');
                    if($(this).css("display") == 'none'){
                        $(this).attr("_height",$(this).css('height'));
                        $(this).css('height',0);
                    }else{
                        $(this).css('height',$(this).attr("_height"));
                    }
                }
            })
        })

    </script>
    </body>
    </html>
	</#escape>
</#macro>

