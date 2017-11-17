(function($) {
	$.fn.extend({
		submitForm : function(option) {
			this.validate({
				rules : option.rules,
				messages : option.messages,
				submitHandler : function(form) {
					ajaxPost({
						form : form,
						success : function(result) {
							option.success(result);
						},
						error : function(result) {
							option.error(result);
						}
					});
					return false;
				},
				errorPlacement : function(error, element) {
					error.insertAfter(element.parent());
				}
			});
		},
		
		select2x : function(option) {
			var select = this;
			var value = select.attr("value");
			if(!value || value==""){
				value = select.val();
			}
			$.ajax({
				url : option.select.url,
				dataType : 'json',
				data : option.select.data,
				type : 'POST',
				success : function(datas) {
					$('#data_'+select.attr('id')).val(JSON.stringify(datas.message));
					select.empty();
					select.append("<option value=''></option>");
					$.each(datas.message, function(index, data) {
						
						var displayName = data[option.select.name];
						if (data[option.select.name2]) {
							displayName = displayName + " | " + data[option.select.name2];
						}
						if (data[option.select.name3]) {
							displayName = displayName + " | " + data[option.select.name3];
						}
						if(option.select.multiple){
							if(value && value != ''){
								var ids = value.split(",");
								var flag = false;
								$.each(ids,function(ii,dd){
									if (dd === data[option.select.id]) {
										flag = true;
									}  
								})
								if (flag) {
									select.append("<option data='"+JSON.stringify(data)+"' selected='true' value='" + data[option.select.id] + "'>" + displayName + "</option>");
								} else {
									select.append("<option data='"+JSON.stringify(data)+"' value='" + data[option.select.id] + "'>" + displayName + "</option>");
								}
								
							}else{
								select.append("<option data='"+JSON.stringify(data)+"' value='" + data[option.select.id] + "'>" + displayName + "</option>");
							}
							
						}else{
							if (value === data[option.select.id]) {
								select.append("<option data='"+JSON.stringify(data)+"' selected='true' value='" + data[option.select.id] + "'>" + displayName + "</option>");
							} else {
								select.append("<option data='"+JSON.stringify(data)+"' value='" + data[option.select.id] + "'>" + displayName + "</option>");
							}
						}
					});
					if (option && option.select2) {
						select.select2(option.select2);
					} else {
						select.select2();
					}
					if (typeof (option.change) === "function") {
						option.change();
					}
					if (typeof (option.onLoadSuccess) === "function") {
						option.onLoadSuccess(datas.message);
					}
				}
			});
		},
		fastCode : function(option) {
			var data = {};
			data.type = option.type;
			data.code = option.code;
			data.filter1 = option.filter1;
			data.filter2 = option.filter2;
			data.parentCode = option.parentCode;
			data.level = option.level;
			this.select2x({
				select : {
					type:"POST",
					url : option.url,
					data : data,
					id : option.idKey,
					name : option.viewKey,
					name2 :option.viewKey2,
					multiple :option.multiple
				},
				select2 : {
					width: 'resolve',
					readonly:option.readonly,
					allowClear : true
				},
				change : option.change,
				onLoadSuccess : option.onLoadSuccess
			});
		},
		
		xAutocomplete : function(option) {
			var url = option.url;
			var inputTip = option.inputTip;
			var defaultValue = option.value;
			this.select2({
				width: 'resolve',
				theme: "classic",
				formatInputTooShort : function(input, min) {
					return inputTip;  
				},
				minimumInputLength : 1,
				allowClear : true,
				multiple: option.multiple,
				ajax : {
					type : "POST",
					url : url,
					dataType : "json",
					data : function(term, page) {
						return {
							search : term
						};
					},
					results : function(data, page) {
						var rs =[];
						$.each(data.message,function(i,d){
							rs[i] = d;
							rs[i].id = d[option.idKey] || d.id;
						});
						return {
							results : rs
						};
					},
					
					quietMillis : 300
				},
				formatResult : function(data) {
					if(option.formatResult && typeof(option.formatResult) == "function"){
						return option.formatResult(data);
					}
					return "<div> " +data.name+ "【" +data.code +"】 </div>";
				},
				formatSelection : function(data) {
					if(option.onSelect && typeof(option.onSelect) == "function"){
						option.onSelect(data);
					}
					if(option.formatSelection && typeof(option.formatSelection) == "function"){
						return option.formatSelection(data);
					}
					if(option.formatResult && typeof(option.formatResult) == "function"){
						return option.formatResult(data);
					}
					return "<div> " +data.name+ "【" +data.code +"】 </div>";
				},
				initSelection : function(element, callback) {
					if (option && option["default"])
						callback(option["default"]);
				}
			});
		}
	})
})(jQuery);

$(document).click(function(){
	try{
		$.gritter.removeAll();
	}catch(e){
		
	}
})

function xalert(content){
	$.gritter.add({
		title: '操作提示',
		text: content,
		class_name: 'gritter-info gritter-center gritter-light' 
	});
}

function exalert(e,content){
	$.gritter.add({
		title: "操作提示",
		text: content,
		class_name: 'gritter-info gritter-center gritter-light' 
	});
	if (e.stopPropagation) {
		e.stopPropagation();
	}else{ 
		e.cancelBubble = true;
	}
}


function formatDate(time){
	if(!time){
		return "";
	}
	if(isNaN(time)){
		return time;
	}
    var datetime = new Date(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

function formatDate_date(time){
	if(!time){
		return "";
	}
	if(isNaN(time)){
		return time;
	}
    var datetime = new Date(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
   
    return year + "-" + month + "-" + date;
}

function getQueryString() {
	var qs=window.location.search.length>0?window.location.search.substring(1):"",
	args=[];
	if(qs==""){
		return args;
	}
	items=qs.split("&");
	len=items.length;
	name=null;
	value=null;
	for(var i=0;i<len;i++){
		var item=items[i].split("=");
		name=decodeURIComponent(item[0]);
		value=decodeURIComponent(item[1]);
		var o = {name : name,value : value};
		args[i] = o;
	}
	return args;
}
