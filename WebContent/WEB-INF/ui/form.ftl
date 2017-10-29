<#macro test>test</#macro>
<#macro panel id="" title="" class="col-sm-12 col-xs-12" style=''>
<div class="${class}" style = "${style}" id="${id}">
    <div class="widget-box">
        <div class="widget-header">
            <h4 class="smaller">${title}</h4>
        </div>

        <div class="widget-body" style = "${style}">
            <div class="widget-main">
				<#nested>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro radio name class='' tip='' style='' required='' value='' id='' selectValue=''>
<input type="radio"
	<#if id?? && id != ''>
       id='${id}'
	</#if>
       name="${name}"
       value="${value}"
	<#if selectValue?? && selectValue == value> checked="checked" </#if>
	<#if required?? && required!=""> valid-required="${required}" </#if>
       class="ace ${class}"
/>

	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>
</#macro>

<#macro date id name readonly="readonly" placeholder='' class='' tip='' style='' format="yyyy-mm-dd" required='' value=""  onChange="">
<input
	<#if id?? && id!=""> id="${id}" </#if>
                         class=" date-picker ${class}"
                         placeholder='${placeholder}'
                         name="${name}"
                         style="${style}"
                         readonly="${readonly}"
                         type="text"
                         title = "不能为空！"
	<#if required?? && required!=""> valid-required="${required}" </#if>
                         value="${value}"
                         data-date-format="${format}" />
<label for="${id}" class="error red"> </label>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>

<script type="text/javascript">
    $('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
        $(this).prev().focus();
    });
		<#if onChange?? && onChange!="">
        $("#${id}").onChange(function(){
            var onchangeFun = ${onChange};
            onchangeFun();
        })
		</#if>

</script>

</#macro>

<#macro datetime id name readonly="readonly" placeholder='' class='' tip='' style='' format="yyyy-mm-dd hh:mi" required='' value="" >
    <div class="input-group date form_date col-md-5" data-date="" data-date-format="" data-link-field="${id}" data-link-format="yyyy-mm-dd hh:ii">
        <input 
		 <#if id?? && id!=""> id="${id}" </#if>
		 class="form-control ${class}"  
		 placeholder='${placeholder}' 
		 name="${name}" 
		 readonly="${readonly}" 
		 type="text"
		 size="20"
		 <#if required?? && required!=""> valid-required="${required}" </#if> 
		 value="${value}"
		 data-date-format="${format}" />
		 
        <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
		<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		
		<label for="${id}" class="error red"> </label>
		<#if tip?? && tip!=''>
			<span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
		</#if>
    </div>
        
	<script type="text/javascript">
	    $('.form_date').datetimepicker({
	        language: 'zh-CN',
	        weekStart: 0,
			autoclose: 1,
			todayHighlight: 1,
			forceParse: 0,
			minuteStep : 1,
			format:"yyyy-mm-dd hh:ii"});
	</script>
</#macro>

<#macro autocomplete id name code='' multiple="false" url='function(){
	return "${ctx}/lov/member/autocomplete.html?code=${code}";
}' placeholder="${i18n.get('请选择')}" class='' tip='' style='' required='' value='' inputTip='请输入查询内容' idKey='id' onSelect='' onChange=''
formatSelection = ""
formatResult="function(data) {
			return '<div> ' +data.name+ '【' +data.code +'】 </div>';
		}">
<input
	<#if id?? && id!=""> id="${id}" </#if>
                         type="search"
                         name="${name}"
                         class="${class}"
                         placeholder='${placeholder}'
                         style='${style}'
	<#if required?? && required!=""> valid-required="${required}" </#if>
>
<label for="${id}" class="error red"> </label>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>
<script type="text/javascript">
		<#if value?? && value != ''>
			var fr = ${formatResult};
			$('#${id}').attr('title',fr(${value}));
		</#if>
        $('#${id}').bind('change',function(){
        	<#if onChange?? && onChange!=''>
	            var fun = ${onChange};
	            if(typeof(fun) === "function"){
	                fun();
	            }
           </#if>
           fr = ${formatResult};
		   $('#${id}').attr('title',fr($('#${id}').select2('data')));
		   $('#s2id_${id}').attr('title',fr($('#${id}').select2('data')));
        })
		
    $(function($) {
		<#if value?? && value!=''>
            var defaultValue=${value};
            $("#${id}").val(defaultValue['${idKey}']);
		</#if>

		<#if url?index_of('function') gt -1 >
            var _urlFunction = ${url};
            var _url = _urlFunction();
		<#else>
            var _url = "${url}";
		</#if>

        $('#${id}').xAutocomplete({
			<#if value?? && value!=''>
                default:${value},
			</#if>
			<#if idKey?? && idKey!=''>
                idKey:'${idKey}',
			</#if>
			<#if formatResult?? && formatResult!=''>
                formatResult:${formatResult},
			</#if>
			<#if formatSelection?? && formatSelection!=''>
                formatSelection:${formatSelection},
			<#else>
                formatSelection:${formatResult},
			</#if>
			<#if onSelect?? && onSelect!=''>
                onSelect:${onSelect},
			</#if>
			<#if multiple?? && multiple=="true">
                multiple:true,
			</#if>
            url:_url,
            inputTip:"${inputTip}"
        });
    })

    function reload_${id}(newDefaultValue){
		<#if value?? && value!=''>
            var defaultValue;
            if(newDefaultValue){
                defaultValue = newDefaultValue;
            }else{
                defaultValue=${value};
            }
            $("#${id}").val(defaultValue['${idKey}']);
		<#else>
            var defaultValue= newDefaultValue;
            if(defaultValue){
                $("#${id}").val(defaultValue['${idKey}']);
            }
		</#if>

		<#if url?index_of('function') gt -1 >
            var _urlFunction = ${url};
            var _url = _urlFunction();
		<#else>
            var _url = "${url}";
		</#if>

        $('#${id}').xAutocomplete({
			<#if value?? && value!=''>
                default:newDefaultValue? newDefaultValue : ${value},
			</#if>
			<#if idKey?? && idKey!=''>
                idKey:'${idKey}',
			</#if>
			<#if formatResult?? && formatResult!=''>
                formatResult:${formatResult},
			</#if>
			<#if formatSelection?? && formatSelection!=''>
                formatSelection:${formatSelection},
			</#if>
			<#if onSelect?? && onSelect!=''>
                onSelect:${onSelect},
			</#if>
			<#if multiple?? && multiple=="true">
                multiple:true,
			</#if>
            url:_url,
            inputTip:"${inputTip}"
        });
    }

</script>
</#macro>

<#macro number id name placeholder='' class='' tip='' style='' required='' message="必须输入数字！" value='' onChange='' readonly=''>
<input type="text" class=""  multiple="false"
	<#if id?? && id!=""> id="${id}" </#if>
       name="${name}"
       placeholder="${placeholder}"
       style='${style}'
       class="${class}"
       valid-number="${message}"
	<#if required?? && required!=""> valid-required="${required}" </#if>
	<#if readonly?? && readonly!=""> readonly="${readonly}" </#if>
       value="${value}"
/>
<label for="${id}" class="error red"> </label>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>
	<#if onChange?? && onChange!="">
    <script>
        $(document).ready(function(){
            /*$('#${id}').bind('propertychange',function(){
					var fun = ${onChange};
					if(typeof(fun) === "function"){
						fun();
					}
				})
				$('#${id}').bind('input',function(){
					var fun = ${onChange};
					if(typeof(fun) === "function"){
						fun();
					}
				})*/
            $('#${id}').bind('change',function(){
                var fun = ${onChange};
                if(typeof(fun) === "function"){
                    fun();
                }
            })
        });
    </script>
	</#if>
</#macro>

<#macro select name id code='' idKey='id' viewKey='name' viewKey2=''
placeholder="${i18n.get('请选择')}" class='' style='' tip='' required ='' value='' filter1='' filter2=''
multiple="false" readonly="false" defaultValue='{fieldName:"name",fieldValue:"aaaaa"}'
parentId="" childId="" level="" url="function(){ return '${ctx}/lov/member/select.html?code=${code}';}"  onChange='' onLoadSuccess='' errorLabel='true' >
<select <#if id?? && id!=""> id="${id}" </#if>
                             name="${name}"
                             placeholder="${placeholder}"
                             style='${style}'
                             class="${class}" 
	<#if multiple?? && multiple=="true">
                             multiple="multiple"
	</#if>
	<#if required?? && required!=""> valid-required="${required}" </#if>
                             value="${value}"
></select>
<input type="hidden" id="data_${id}"/>
	<#if errorLabel == 'true'>
    <label for="${id}" class="error red"> </label>
    <input type="hidden" id="${id}_hide" />
	</#if>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>

<script type="text/javascript">
    function reload_${id}(defValue,fun){
        var url_${id}_function = ${url};
        var url_${id} = url_${id}_function();
        $("#${id}").select2("readonly", ${readonly});
		<#if parentId?? && parentId!=''>
            var parentCode = $("#${parentId}").val();
            if(!parentCode){
                $('#${id}').html("<option></option>");
                $('#${id}').select2("val", "");
                return;
            }
		</#if>

        if(defValue){
            $('#${id}').val(defValue);
            $('#${id}_hide').val(defValue);
        }

        $('#${id}').fastCode({
			<#if filter1?? && filter1!=''>
                filter1 : "${filter1}",
			</#if>
			<#if filter2?? && filter2!=''>
                filter2 : "${filter2}",
			</#if>
			<#if parentId?? && parentId!=''>
                parentCode : $("#${parentId}").val(),
			</#if>
            onLoadSuccess:function(datas){
				<#if childId?? && childId!=''>
                    reload_${childId}();
				</#if>
				<#if onLoadSuccess?? && onLoadSuccess!=''>
                    var onLoadSuccess = ${onLoadSuccess};
                    onLoadSuccess();
				</#if>
                if(!defValue){
					<#if defaultValue?? && defaultValue!=''>
                        var dv = ${defaultValue!{}};
                        $.each(datas,function(i,item){
                            if(item[[dv['fieldName']]] == dv['fieldValue']){
                                $('#${id}').select2().select2("val", item.id);;
                            }
                        })
					</#if>
                }

                if($('#${id}_hide').val() && $('#${id}_hide').val()!=''){
                    var sv = $('#${id}_hide').val();
                    $('#${id}').select2("val", sv);
                }

                if(typeof(fun) === "function"){
                    fun();
                }
            },
			<#if level?? && level!=''>
                level : "${level}",
			</#if>
            url: url_${id},
            code:"${code}",
            idKey:"${idKey}",
            viewKey:"${viewKey}",
            viewKey2:"${viewKey2}",
            multiple:${multiple}
        });
    }

    $(function($) {
        //初始化，如果父ID为空则正常初始化
        var url_${id}_function = ${url};
        var url_${id} = url_${id}_function();

        $('#${id}').fastCode({
			<#if filter1?? && filter1!=''>
                filter1 : "${filter1}",
			</#if>
			<#if filter2?? && filter2!=''>
                filter2 : "${filter2}",
			</#if>
			<#if parentId?? && parentId!=''>
                parentCode : $("#${parentId}").val(),
			</#if>
			<#if level?? && level!=''>
                level : "${level}",
			</#if>

            onLoadSuccess:function(datas){
				<#if childId?? && childId!=''>
                    reload_${childId}();
				</#if>
				<#if onLoadSuccess?? && onLoadSuccess!=''>
                    var onLoadSuccess = ${onLoadSuccess};
                    onLoadSuccess();
				</#if>
				<#if defaultValue?? && defaultValue!=''>
                    var dv = ${defaultValue!{}};
                    $.each(datas,function(i,item){
                        if(item[[dv['fieldName']]] == dv['fieldValue']){
                            $('#${id}').select2().select2("val", item.id);
                        }
                    })
				</#if>
            },

            url: url_${id},
            code:"${code}",
            idKey:"${idKey}",
            viewKey:"${viewKey}",
            viewKey2:"${viewKey2}",
            multiple:${multiple},
            readonly:${readonly}
        });

		<#if childId?? && childId!=''>
            $('#${id}').change(function(){
                reload_${childId}();
            });
		</#if>

    })
</script>

<script>
    $(document).ready(function(){
        $('#${id}').bind('change',function(){
            $('label[for="${id}"]').hide();
			<#if onChange?? && onChange!="">
                var fun = ${onChange};
                if(typeof(fun) === "function"){
                    fun();
                }
			</#if>
        })
    });
</script>



</#macro>

<#macro input name id placeholder='' class='' style='' maxlength='' tip='' required='' dblclick='' value='' type="text" readonly='' disabled=''>
<input type="${type}"
	<#if id?? && id!=""> id="${id}" </#if>
       placeholder="${placeholder}"
       style="${style}"
       class="${class}"
       name="${name}"
	<#if readonly?? && readonly!=""> readonly="${readonly}" </#if>
	<#if maxlength?? && maxlength!=""> maxlength="${maxlength}" </#if>
	<#if required?? && required!=""> valid-required="${required}" </#if>
	<#if disabled?? && disabled!=""> disabled="${disabled}" </#if>
       value="${value}"
/>
<label for="${id}" class="error red"> </label>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>

	<#if dblclick?? && dblclick!="">
    <script>
        $(document).ready(function(){

            $('#${id}').bind('dblclick',function(){
                var fun = ${dblclick};
                fun();
            })
        });
    </script>
	</#if>

</#macro>

<#macro textArea name id placeholder='' class='' maxlength='' style='' tip='' required='' width="50" height="5" value='' readonly=''>
<textarea
	<#if id?? && id!=""> id="${id}" </#if>
                         cols="${width}"
                         rows="${height}"
                         placeholder="${placeholder}"
                         style="${style}"
                         class="${class}"
                         name="${name}"
	<#if maxlength?? && maxlength!=""> maxlength="${maxlength}" </#if>
	<#if required?? && required!=""> valid-required="${required}" </#if>
	<#if readonly?? && readonly!=""> readonly="${readonly}" </#if>
>${value}</textarea>
<label for="${id}" class="error red"> </label>
	<#if tip?? && tip!=''>
    <span class="help-button" data-rel="popover" data-trigger="hover" data-placement="right" data-content="${tip}" title="提示">?</span>
	</#if>
</#macro>



<#macro form id='' class='' action='' success='undefined' error='undefined' before='undefined' ajaxPost="true" autoClose="true" upload="false"  postData='undefined'>
<form class="${class}" id="form_${id}" action='${action}'  method="POST" role="form" autocomplete="off"
	<#if upload =="true">
      enctype='multipart/form-data'
	</#if>
>
	<#nested>
</form>
<script type="text/javascript">
    $(function($) {
        $(":input").blur(function(){
        	try{
            	this.value = this.value.trim();
            }catch(e){}
        });
        $('.submit').mouseover(function(){
            $(this).focus();
        });
        var form_${id}_validator = {};
        var form_${id}_successFunction = ${success};
        var form_${id}_errorFunction = ${error};
        var form_${id}_before = ${before};
        var form_${id}_postData = ${postData};
        var form_${id}_data;
        //搜索
        $('#form_${id}').click(function(fe){
            var submit = false;
            $.each(fe.target.classList,function(i,cls){
                if(cls == 'submit'){
                    submit = true;
                }
            })
            if(!submit && fe.target.parentNode !=null && fe.target.parentNode.type == 'button'){
                $.each(fe.target.parentNode.classList,function(i,cls){
                    if(cls == 'submit'){
                        submit = true;
                    }
                })
            }

            if(!submit){
                return;
            }

            if(typeof(form_${id}_before) == 'function' ){
                var result = form_${id}_before(fe);
                if(result == false){
                    return false;
                }
            }

            if(typeof(form_${id}_postData) == 'function' ){
                var result = form_${id}_postData(fe);
                if(result){
                    form_${id}_data = result;
                }
            }

            form_${id}_validator.rules = {};
            form_${id}_validator.messages={};
            var inputs = $("form input");
            $.each(inputs,function(i,e){
                var rule = {};
                var message={};
                if($(e).attr("valid-required") != undefined){
                    rule.required = true;
                    message.required = $(e).attr("valid-required");
                }
                if($(e).attr("valid-number") !=undefined){
                    rule.number = true;
                    message.number =  $(e).attr("valid-number");
                }
                form_${id}_validator.rules[e.name] = rule;
                form_${id}_validator.messages[e.name] = message;
            })

            var textareas = $("form textarea");
            $.each(textareas,function(i,e){
                var rule = {};
                var message={};
                if($(e).attr("valid-required") != undefined){
                    rule.required = true;
                    message.required = $(e).attr("valid-required");
                }
                if($(e).attr("valid-number") !=undefined){
                    rule.number = true;
                    message.number =  $(e).attr("valid-number");
                }
                form_${id}_validator.rules[e.name] = rule;
                form_${id}_validator.messages[e.name] = message;
            })

            var selects = $("form select");
            $.each(selects,function(i,e){
                if($(e).attr("valid-required") != undefined){
                    form_${id}_validator.rules[e.name] = { required : true };
                    form_${id}_validator.messages[e.name] = { required : $(e).attr("valid-required") };
                }
            })

            form_${id}_validator.errorPlacement = function(error, element) {
                error.insertAfter(element.parent());
            }
			<#if ajaxPost=="true">
                form_${id}_validator.submitHandler = function(form){
                    $(fe.target).attr("disabled",true);
                    $(fe.target.parentNode).attr("disabled",true);
                    ajaxPost({
                        form : form,
						<#if postData?? && postData!=''>
                            data : form_${id}_data,
						</#if>
                        success : function(result) {
                            $(fe.target).removeAttr("disabled");
                            $(fe.target.parentNode).removeAttr("disabled");
                            if(typeof(form_${id}_successFunction) == 'function' ){
                                try{
                                    form_${id}_successFunction(result);
                                }catch(e){}
								<#if autoClose =="true">
                                    if(frameElement && frameElement.api){ //判断是否是弹出模式的
                                        frameElement.api.close();
                                    }
								</#if>
                            }
                        },
                        error : function(result) {
                            $(fe.target).removeAttr("disabled");
                            $(fe.target.parentNode).removeAttr("disabled");
                            if(typeof(form_${id}_errorFunction) == 'function' ){
                                form_${id}_errorFunction(result);
								<#if autoClose =="true">
                                    if(frameElement && frameElement.api){ //判断是否是弹出模式的
                                        frameElement.api.close();
                                    }
								</#if>
                            }else{
                                xalert(result);
                            }
                        }
                    });
                    return false;
                };
			</#if>
            $(this).validate(form_${id}_validator);
            $(this).submit();
        });
    })
</script>
</#macro>


<#macro table id  colModel url='' postData="{}" title='' style='' rowNum='10',rowNumList = '[]', height='' ondblClickRow='' onSelectRow='' onComplete='' search='true' data='' cellEdit = 'false'  grouping = 'false' groupingView = "{}"
checkbox='false' addURL='' editURL='' deleteURL='' importURL='' importTempletURL='#' exportURL='' class='' buttons='' hidden='false' shrinkToFit='false' searchClick=''
lineEditURL='' onLineEditBefore='' onLineEditAfter='' treeGrid='false' loadonce='false' widthFunction='function(){ return $("#${id}_row_div").width(); }' autowidth='true' forceFit='true' checkAllFunction=''
datatype='json' footerrow='false' gridComplete='' rownumbers = 'false'>

<div class="row popupMenu" id="popupMenu_${id}">
    <ul id="menu_${id}" class="ui-menu ui-widget ui-widget-content ui-corner-all" role="menu" tabindex="0" aria-activedescendant="ui-id-5">
        <li class="ui-state-disabled ui-menu-item" role="presentation" aria-disabled="true">
            <a href="#"  class="ui-corner-all" tabindex="-1" id="detail_btn_${id}" role="menuitem">详情</a>
        </li>
    </ul>
</div>

<div id="searchDiv_${id}" class="row <#if search=='true'>hide</#if>" >
    <table class="col-xs-12">
        <tr>
            <td>
                <form class="form-horizontal" id="search_form_${id}" role="form" style="${style}">
					<#nested>
                    <button type="reset" style="display:none" id="reset_seache_${id}"></button>
                </form>
            </td>
        </tr>
    </table>
</div>
<div class="row" >
    <div class="col-xs-12" id="${id}_row_div" >
        <table id="${id}" class="col-xs-12" style='width:100%'></table>
        <div id="grid-pager_${id}"></div>
    </div>
</div>
<style>
    .ui-jqgrid .ui-jqgrid-title {
        float: left;
        margin: 0px;
        width: 100%;
    }
    .ui-jqgrid-sortable {
        padding-left: 4px;
        font-size: 11px;
        color: #777;
        font-weight: bold;
    }

    .ui-jqgrid-view > .ui-jqgrid-titlebar {
        height: 36px;
        line-height: 24px;
        color: #FFF;
        background: #307ecc;
        padding: 0;
        font-size: 15px;
    }

    .popupMenu{
        position:absolute;
        border:1px solid;
        display:none;
        z-index:999;
        background-color: white;
    }
    .ui-widget-content{
        border-color:#e1e1e1;
    }

</style>
<script type="text/javascript">
    var rowNumList_${id} = ${rowNumList};
    if(rowNumList_${id}.length <= 0 ){
        rowNumList_${id} = [5,10,15,20,25,30,40,50,100];
    }

    var selected_${id} = undefined;
    var selectedList_${id} = [];

    var caption_${id} = "<div style='padding-top: 4px; padding-left: 20px;'> <table> "+
            "<tr id='buttons_tr_${id}'> "+
            "<td><span class='hide' id='loading_${id}'><img src='${ctx}/assets/img/loading.gif' /></span>${title}</td> "+
            "<td>&nbsp;&nbsp;</td> "+
            "<td id='buttons_add_${id}' <#if !addURL?? || addURL==''>style='display:none;'</#if>> <#if addURL != ''> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='add_${id}'> <i class='icon-save bigger-110'></i>${i18n.get('新增')} </button> </span> </#if> </td> "+
            "<td id='buttons_edit_${id}' <#if !editURL?? || editURL==''>style='display:none;'</#if>> <#if editURL != ''> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='edit_${id}'> <i class='icon-edit bigger-110'></i>${i18n.get('修改')} </button> </span> </#if> </td> "+
            "<td id='buttons_delete_${id}' <#if !deleteURL?? || deleteURL==''>style='display:none;'</#if>> <#if deleteURL != ''> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='delete_${id}'> <i class='icon-trash bigger-110'></i>${i18n.get('删除')} </button> </span> </#if> </td> "+
            "<td id='buttons_import_${id}'> <#if importURL?? && importURL!=''> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='import_${id}'> <i class='icon-trash bigger-110'></i>${i18n.get('导入')}</button> </span> </#if> </td>  "+
            "<td id='buttons_export_${id}'> <#if exportURL?? && exportURL!=''> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='export_${id}'> <i class='icon-trash bigger-110'></i>${i18n.get('导出')} </button> </span> </#if> </td>  "+
				<#if search?? && search=='true'>
                "<td id='buttons_search_${id}'> <span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='msearch_${id}'> <i class='icon-trash bigger-110'></i> ${i18n.get('查询 ')}</button> </span> </td>  "+
				</#if>
            "</tr> </table> </div>";

    $(document).ready(function() {
        var dialog_${id};
        var colModel = ${colModel};
        var createdByName = { name : 'createdByName', label : '', hidden : true,sortable:false};
        var createdAt = { name : 'createdAt', label : '', hidden : true,sortable:false,formatter:function(cellvalue, options){ return formatDate(cellvalue) }};
        var updatedByName = { name : 'updatedByName', label : '', hidden : true,sortable:false};
        var updatedAt = { name : 'updatedAt', label : '', hidden : true,sortable:false,formatter:function(cellvalue, options){ return formatDate(cellvalue) }};
        colModel.push(createdByName);
        colModel.push(createdAt);
        colModel.push(updatedByName);
        colModel.push(updatedAt);
        var ${id}_widthFunction = ${widthFunction};
        var ${id}_width = ${id}_widthFunction();
        var grid_selector = "#${id}";

        $("#menu_${id}").menu();
        $(document.body).on('click',function(event){
            var target =event.srcElement ? event.srcElement :event.target;

            if($(target).hasClass("edit-cell")|| $(target).is("select") 
            	|| $(target).parents().hasClass("datepicker datepicker-dropdown dropdown-menu")
            	|| $(target).parents().hasClass("datepicker-days") 
            	|| $(target).parents().hasClass("datepicker-months") 
            	|| $(target).parents().hasClass("datepicker-years") 
            	||　$(target).hasClass("month")
            	||　$(target).hasClass("year")
            	|| ($(target).is("input")&&$(target).closest("td").attr("role")=="gridcell")){
                return;
            }else{
                $(grid_selector).trigger('blur')
            }
        });

        $(grid_selector).bind("contextmenu",function(e){
            return false;
        });

        $(window).click(function(){
            $('.popupMenu').hide();
        })

        $("#popupMenu_${id}").mouseleave(function(e){
            if(e.currentTarget == e.target){
                $('.popupMenu').hide();
            }
        });

        $("#detail_btn_${id}").click(function(){
            if(selected_${id}){
                var detailHtml = "";
                detailHtml = detailHtml + "\n创建人：" + selected_${id}.createdByName;
                detailHtml = detailHtml + "\n创建时间：" + selected_${id}.createdAt;
                detailHtml = detailHtml + "\n最后更新人：" + selected_${id}.updatedByName;
                detailHtml = detailHtml + "\n最后更新时间：" + selected_${id}.updatedAt;
                alert(detailHtml);
            }
        });

        $(grid_selector).blur(function(){
            var item = {};
            var editId = null;
            $('#${id} input[type=\'text\']').each(function(i,o){
                var itemId = o.id;
                if(itemId && itemId != ''){
                    //var objs = itemId.split("_");
                    //if(objs.length == 2){
                    //	var myid = objs[0];
                    //	editId = myid;
                    //	var myname = objs[1];
                    //	item[myname] = o.value;
                    //}
                    var myname = itemId.substring(itemId.lastIndexOf('_')+1);
                    editId = itemId.substring(0,itemId.lastIndexOf('_'));

                    console.info('editId:'+editId);
                    console.info('myname1:'+myname );
                    item[myname] = o.value;
                }
            });

            $('#${id} input[type=\'checkbox\']').each(function(i,o){
                var itemId = o.id;
                if(itemId && itemId != ''){
                    var objs = itemId.split("_");
                    if(objs[0] && objs[0] == 'jqg'){
                        return;
                    }
                    //if(objs.length == 2){
                    //	var myid = objs[0];
                    //	editId = myid;
                    //	var myname = objs[1];
                    //	var checked = $('#'+itemId).is(':checked')
                    //	if(checked){
                    //		item[myname] = $('#'+itemId).val();
                    //	}else{
                    //		item[myname] = $('#'+itemId).attr('offval');
                    //	}
                    //}

                    var myname= itemId.substring(itemId.lastIndexOf('_')+1);
                    editId = itemId.substring(0,itemId.lastIndexOf('_'));
                    console.info('editId:'+editId);
                    console.info('myname2:'+myname);
                    var checked = $('#'+itemId).is(':checked')
                    if(checked){
                        item[myname] = $('#'+itemId).val();
                    }else{
                        item[myname] = $('#'+itemId).attr('offval');
                    }
                }
            });

            $('#${id} select').each(function(i,o){
                var itemId = o.id;
                if(itemId && itemId != ''){
                    //var objs = itemId.split("_");
                    //if(objs.length == 2){
                    //	var myid = objs[0];
                    //	editId = myid;
                    //	var myname = objs[1];
                    //	item[myname] = $('#'+itemId).children('option:selected').text();
                    //}
                    var myname = itemId.substring(itemId.lastIndexOf('_')+1);
                    editId = itemId.substring(0,itemId.lastIndexOf('_'));

                    console.info('editId:'+editId);
                    console.info('myname3:'+myname);
                    item[myname] = $('#'+itemId).children('option:selected').text();
                }
            });
            if(editId){
                var _item = $(grid_selector).jqGrid('getRowData',editId);
                $(grid_selector).jqGrid('restoreRow',editId);
                $(grid_selector).jqGrid('setRowData',editId,_item);
                $(grid_selector).jqGrid('setRowData',editId,item);
                var lineItem = $(grid_selector).jqGrid('getRowData',editId);
                if('${lineEditURL}' != ''){
                    var option = {};
                    option.data = lineItem;
                    option.type = 'post';
                    option.url = '${lineEditURL}';
                    option.success = function(){
						<#if onLineEditAfter?? && onLineEditAfter!=''>
                            var onLineEditAfterFun = ${onLineEditAfter};
                            onLineEditAfterFun(editId);
						<#else>
                            alert('${i18n.get('保存成功！ ')}');
						</#if>
                    };
					<#if onLineEditBefore?? && onLineEditBefore!=''>
                        var onLineEditBeforeFun = ${onLineEditBefore};
                        onLineEditBeforeFun();
					</#if>
                    ajax(option);
                }else{
					<#if onLineEditAfter?? && onLineEditAfter!=''>
                        var onLineEditAfterFun = ${onLineEditAfter};
                        onLineEditAfterFun(editId,lineItem);
					</#if>
                }
            }
        })
        var pager_selector = "#grid-pager_${id}";
        var userButtonHandlers = {};

        var postData = ${postData};
        var formDatas = $('#search_form_${id}').serializeArray();
        $.each(formDatas,function(index,item){
            postData[item.name] = item.value;
        })
        if(typeof(gridcount) == "undefined"){
        	gridcount = 0;
			gridcount++;
        }
					
        $(grid_selector).jqGrid({
            postData :postData,
            cellEdit : ${cellEdit},
            cellsubmit:'',
            datatype : '${datatype}',
			<#if url?? && url!=''>
                url : '${url}',
			</#if>
            colModel : colModel,
            viewrecords : false,
			<#if data?? && data!=''>
                data : ${data},
			</#if>
            rowNum : ${rowNum},
            loadonce:${loadonce},
			<#if treeGrid?? && treeGrid == 'true'>
                treeGrid:true,
                ExpandColumn:"prjlst.prdNm",
                treedatatype:"local",
                treeGridModel:"nested",
                treeReader:{
                    "left_field":"lft",
                    "right_field":"rgt",
                    "level_field":"level",
                    "leaf_field":"isLeaf",
                    "icon_field":"icon"
                },
			</#if>
            rowList : rowNumList_${id} ,
            pager : pager_selector,
            loadtext:"数据加载中，请稍后……",
            pgtext:"当前第{0}页，共{1}页",
            multiboxonly : false,
            caption : caption_${id},
            autowidth : ${autowidth},
            shrinkToFit:${shrinkToFit},
            rownumbers:${rownumbers},
            grouping:${grouping},
            groupingView :${groupingView},
            forceFit:${forceFit},
            width:${id}_width,
            altRows: true,
            multiselect : ${checkbox!'false'},
			<#if footerrow?? && footerrow!=''>
                footerrow : ${footerrow},
			</#if>
            userDataOnFooter: true,
            loadError:function(xhr,status,error){
                var data = JSON.parse(xhr.responseText);
                if(xhr.status == 401){
                    window.location.href=ctx+data.message;
                }else{
                    xalert(data.message);
                }
            },

            onRightClickRow :function(rowid,iRow,iCol,e){
                $("#popupMenu_${id}").show().css({
                    'top':e.pageY+'px',
                    'left':e.pageX+'px'
                });
                e.preventDefault();
            },

            ondblClickRow : function(rowid,iRow,iCol,e){
                selected_${id} = $(grid_selector).jqGrid('getRowData',rowid);
                $(grid_selector).editRow(rowid, true);
				<#if ondblClickRow?? && ondblClickRow!= ''>
                    var ondblClickRow = ${ondblClickRow!};
                    if(typeof(ondblClickRow) === 'function'){
                        ondblClickRow(rowid,iRow,iCol,e);
                    }
				<#else>
                    $('#edit_${id}').trigger('click');
				</#if>
            },
            onSelectRow : function(rowid,status){
                selected_${id} = $(grid_selector).jqGrid('getRowData',rowid);
				<#if onSelectRow?? && onSelectRow!=''>
                    var userFunction = ${onSelectRow} ;
                    if(typeof(userFunction) == 'function' ){
                        userFunction(selected_${id},status);
                    }
				</#if>
                if(status){
                    var flag = true;
                    $.each(selectedList_${id},function(i,item){
                        if(item && selected_${id}.id === item.id){
                            flag = false;
                            return;
                        }
                    });
                    if(flag){
                        selectedList_${id}.push(selected_${id});
                    }
                }else{
                    $.each(selectedList_${id},function(i,item){
                        if(item && selected_${id}.id === item.id){
                            selectedList_${id}.splice(i,1);
                        }
                    });
                }
            },onPaging:function(pgButton){
                selected_${id} = undefined;
            },
            jsonReader : {
                root : "message.list",
                page : "message.pageCurrent",
                total : "message.pageCount",
                records : "message.count",
                repeatitems : true
            },
            gridComplete : function(){
				<#if gridComplete?? && gridComplete!= ''>
                    var gridComplete = ${gridComplete!};
                    if(typeof(gridComplete) === 'function'){
                        gridComplete();
                    }
				<#else>

				</#if>
				
            },
            loadComplete : function(xhr){
                var len = 10;
				var minheight = len * 26 + 3;
                var list;
				<#if datatype == 'json'>
                    list = xhr.message.list;
                    if(list != undefined && list.length > len){
                        len = list.length;
                    }
				</#if>
                var height= len * 26 + 3;
                if(list != undefined && list){
                	if(list.length>10){
                		height= $(window).height()-40-60;
                		if(height<minheight){
                			height = minheight;
                		}
               		}
                }
               	
                //alert($(window).height()+":"+height);
				<#if height?? && height!=''>
                    height = '${height}';
				</#if>
                $(grid_selector).jqGrid('setGridHeight',height);
                $('#search_${id}').hide();
                var postData = $(grid_selector).jqGrid('getGridParam','postData');
                postData['reload'] = "N";
                $(grid_selector).jqGrid('setGridParam',{postData:postData});
				<#if onComplete?? && onComplete!=''>
                    var onCompleteFunction = ${onComplete};
                    if(typeof(onCompleteFunction) == 'function'){
                        onCompleteFunction(xhr.message.list);
                    }
				</#if>
                selectedList_${id} = [];

                if(typeof(onComplete_${id}) === 'function' ){
                    onComplete_${id}();
                }

            }
        });

        $('#cb_${id}').click(function(){
			<#if checkAllFunction != "" >
                var checkAllFunction = ${checkAllFunction};
                checkAllFunction($('#cb_${id}').prop('checked'));
			</#if>
            if($('#cb_${id}').prop('checked')){
                selectedList_${id} = [];
                var ids = $('#${id}').jqGrid('getDataIDs');
                $.each(ids,function(i,id){
                    var row = $('#${id}').jqGrid('getRowData',id);
                    selectedList_${id}.push(row);
                })
            }else{
                selectedList_${id} = [];
            }
        });

		<#if buttons?? && buttons!=''>
            var buttons = ${buttons};
            $.each(buttons,function(i,e){
                if(e.own){
                    var html = "<td><span class='input-icon'> <button type='button' class='btn btn-info btn-xs btn_${id}' id='"+e.id+"'> <i class='"+e.icon+" bigger-110'></i> "+e.label+" </button> </span></td>";
                    if(e.position){
                        $("#buttons_"+e.position+"_${id}").append(html);
                    }else{
                        $("#buttons_tr_${id}").append(html);
                    }
                    userButtonHandlers['handler_'+e.id] = e.handler;
                }
            })
		</#if>

		<#if buttons?? && buttons!=''>
            $.each(buttons,function(i,e){
                if(e.own){
                    $("#"+e.id).click(function(e){
                        var fun = userButtonHandlers['handler_'+$(this).attr('id')];
                        fun(e,selected_${id});
                    })
                }
            });
		</#if>
        //初始化按钮
		<#if addURL?? && addURL!="">
            //新增
            $('#add_${id}').click(function(){
                var url = ${addURL};
                var dialog = parent.dialog('${title}',url,window);
            });
		</#if>

		<#if exportURL?? && exportURL!="">
            //新增
            $('#export_${id}').click(function(){
                var qs = "?";
                var args = getQueryString();
                var formDatas = $('#search_form_${id}').serializeArray();
                $.each(args,function(index,item){
                    qs = qs + item.name +'='+ item.value + '&';
                })
                $.each(formDatas,function(index,item){
                    qs = qs + item.name +'='+ item.value+ '&';
                })
                window.location.href = ${exportURL} + qs;
            });
		</#if>

		<#if editURL?? && editURL!="">
			<#if editURL?index_of("?")!=-1>
				<#assign unionFlag = "&" />
			<#else>
				<#assign unionFlag = "?" />
			</#if>
            //修改
            $('#edit_${id}').click(function(e){
                if(!selected_${id}){
                    exalert(e,"没有选中任何数据!");
                    return;
                }
                var url = ${editURL}+'${unionFlag}'+'id='+selected_${id}.id;
                var dialog = parent.dialog('${title}',url,window);
            });
		</#if>
        //删除

		<#if deleteURL?? && deleteURL!="">
            $('#delete_${id}').click(function(e){
                if(!selected_${id}){
                    exalert(e,"没有选中任何数据!");
                    return;
                }
                bootbox.confirm("你确定要删除该条数据吗?", function(result) {
                    if(result) {
                        var delid = $("#${id}").jqGrid('getGridParam','selrow');
                        //$("#${id}").jqGrid("delRowData", delid);

                        $('#loading_${id}').removeClass('hide');
                        $('#loading_${id}').show();
                        ajax({
                            type:"post",
                            url : ${deleteURL},
                            data: selected_${id},
                            success : function(result) {
                                reload_${id}();
                                $('#loading_${id}').hide();
                            },
                            error : function(result) {
                                if(result == "id is null"){
                                    $("#${id}").jqGrid("delRowData", delid);
                                }else {
                                    exalert(e, result);
                                }
                                $('#loading_${id}').hide();
                            }
                        });
                    }
                });
            });
		</#if>

        $('#msearch_${id}').click(function(e){
            $( "#searchDiv_${id}" ).removeClass('hide').dialog({
                modal: true,
                title: "查询",
                title_html: true,
                width:1000,
                height:500,
                position:'top',
                buttons: [
                    {
                        text: "重置",
                        type:"reset",
                        "class" : "btn btn-xs",
                        click: function() {
                            $('#reset_seache_${id}').trigger('click');
                        }
                    },{
                        text: "查询",
                        "class" : "btn btn-xs",
                        click: function() {
                            $( this ).dialog( "close" );
                            reload_${id}();
                        }
                    }
                ]
            });
        });

		<#if importURL?? && importURL!="">
            $('#import_${id}').click(function(e){
                bootbox.dialog({
                    message: "<form method='POST' enctype='multipart/form-data' id='importFrm_${id}' action=''> <div><input id='file_${id}' name='file' type='file'/></div> <div><a href=\"${importTempletURL!'#'}\">模板下载XLS格式</a></div> </form>",
                    buttons:
                            {
                                'import' :
                                        {
                                            'label' : '<i class="icon-ok"></i>上传',
                                            'className' : 'btn-sm btn-success',
                                            'callback': function(e) {
                                                if($('#file_${id}').val()){
                                                    $('#loading_${id}').removeClass('hide');
                                                    $('#loading_${id}').show();
                                                    var action=${importURL};
                                                    $('#importFrm_${id}').attr('action',action);
                                                    ajaxPost({
                                                        form : $('#importFrm_${id}'),
                                                        success : function(result) {
                                                            reload_${id}();
                                                            $('#loading_${id}').hide();
                                                        },
                                                        error : function(result) {
                                                            xalert(result);
                                                            $('#loading_${id}').hide();
                                                        }
                                                    });
                                                }else{
                                                    xalert(e,'没有选择文件');
                                                }
                                            }
                                        },
                                'cancel' :
                                        {
                                            'label' : '取消!',
                                            'className' : 'btn-sm',
                                            'callback': function() {
                                                //do noting
                                            }
                                        }
                            }
                });
            });
		</#if>

		<#if hidden?? && hidden!=''>
            if('${hidden}' == 'true' && $("#gbox_${id}").is(":hidden")){
                $("#gbox_${id}").hide();
            }else{
                $("#gbox_${id}").show();
            }
		</#if>


        //navButtons
        jQuery(grid_selector).jqGrid('navGrid',pager_selector,
                { 	//navbar options
                    edit: false,
                    editicon : 'icon-pencil blue',
                    add: false,
                    addicon : 'icon-plus-sign purple',
                    del: false,
                    delicon : 'icon-trash red',
                    search: ${search},
                    searchicon : 'icon-search orange',
                },
                { },
                { },
                { },
                {
                    //search form
                    recreateForm: true,
                    afterShowSearch: function(e){
                        var form = $(e[0]);
                        form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                        style_search_form(form);
                    },
                    afterRedraw: function(){
                        style_search_filters($(this));
                    }
                    ,
                    multipleSearch: true
                }
        )

        function style_search_filters(form) {
            form.find('.delete-rule').val('X');
            form.find('.add-rule').addClass('btn btn-xs btn-primary');
            form.find('.add-group').addClass('btn btn-xs btn-success');
            form.find('.delete-group').addClass('btn btn-xs btn-danger');
        }
        function style_search_form(form) {
            var dialog = form.closest('.ui-jqdialog');
            var buttons = dialog.find('.EditTable')
            buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'icon-retweet');
            buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'icon-comment-alt');
            buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'icon-search');
        }

        var search_form_${id}_validator = {};

        //搜索
        $('#search_form_${id}').click(function(){
			<#if searchClick?? && searchClick!="">
                var searchClick = ${searchClick};
                searchClick();
			</#if>
            search_form_${id}_validator.rules = {};
            search_form_${id}_validator.messages={};
            var inputs = $("form input");
            $.each(inputs,function(i,e){
                if($(e).attr("valid-required") != undefined){
                    search_form_${id}_validator.rules[e.name] = { required : true };
                    search_form_${id}_validator.messages[e.name] = { required : $(e).attr("valid-required") };
                }
            })
            var selects = $("form select");
            $.each(selects,function(i,e){
                if($(e).attr("valid-required") != undefined){
                    search_form_${id}_validator.rules[e.name] = { required : true };
                    search_form_${id}_validator.messages[e.name] = { required : $(e).attr("valid-required") };
                }
            })
            search_form_${id}_validator.submitHandler = function(form){
                reload_${id}();
                return false;
            };
            $(this).validate(search_form_${id}_validator);
        });

    });

    function reload_${id}(data){
        selected_${id} = undefined;
        var grid_selector = "#${id}";
		<#if datatype == 'json'>
            var postData = $(grid_selector).jqGrid('getGridParam','postData');
            var formDatas = $('#search_form_${id}').serializeArray();
            $.each(formDatas,function(index,item){
                postData[item.name] = item.value;
            })
            postData['reload'] = "Y";
            $(grid_selector).jqGrid('setGridParam',{postData:postData}).trigger('reloadGrid');
		<#else>
            $(grid_selector).jqGrid('clearGridData');
            $(grid_selector).jqGrid('setGridParam',{data:data}).trigger("reloadGrid")
		</#if>
    }
    $(window).resize(function(){
        $("#${id}").setGridWidth($(window).width()-20);
    });
	

    function getValuesByCell(cellvalue,groupCode) {
        if(cellvalue==null || cellvalue== "undefined"|| cellvalue==""){
            return "";
        }
        var str = "";
        $.ajax({
            type: "post",
            async:false,
            url: "${ctx}/lov/member/getValuesByCell.html",
            data: "value=" + cellvalue+"&groupCode="+groupCode,
            success: function (data) {
                str= data.message;
            }
        });

        return str;
    }

</script>

</#macro>