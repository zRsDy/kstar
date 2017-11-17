function ajaxPost(options){
	var form = options.form;
	var _success = options.success;
	var _error = options.error;
	$(form).ajaxSubmit({
		type: "post",
		data: options.data,
	    url: $(form).attr('action'),
	    success : function(result){ //以return 方式出来的信息或者异常
	    	if(result.status === "error" && typeof(_error)=="function"){
	    		_error(result.message);
	    	}
	    	if(result.status === "success" && typeof(_success)=="function"){
	    		_success(result.message);
	    	}
	    },
	    error : function(error){ //以throw 方式出来的异常
	    	if(error.status == 400){
				alert("检查spring值注入问题");
			}else if(error.status == 401){
				var data = JSON.parse(error.responseText);
				window.location.href=ctx+data.message;
			}else if(error.status == 403){
				var data = JSON.parse(error.responseText);
				if(typeof(_error)=="function"){
	    			_error(data.message);
		    	}
			}else if(error.status == 404 || error.status == 405){
				alert("请求地址没有找到 或者 请检查网络连接是否正常 ");
			}else if(error.status<500){//客户端异常
				alert("请检查网络连接是否正常");
	    	}else if(error.responseText){//服务端异常
	    		var data = JSON.parse(error.responseText);
	    		if(typeof(_error)=="function"){
	    			_error(data.message);
		    	}else{
		    		alert(data.message);
		    	}
	    	}else{
	    		alert("未知错误!");
	    	}
	    }
	});
}

function ajax(option){
	var data = option.data;
	var type = option.type;
	var url = option.url;
	var async = false===option.async?false:true;
	var _success = option.success;
	var _error = option.error;
	$.ajax({
		url: url,
		type: "post",
		data: data,
		async : async,
		success: function(result){
			if(result.status === "error" && typeof(_error)=="function"){
	    		_error(result.message);
	    	}
	    	if(result.status === "success" && typeof(_success)=="function"){
	    		_success(result.message);
	    	}
		},error:function(error){
			if(error.status == 400){
				alert("检查spring值注入问题");
			}else if(error.status == 401){
				var data = JSON.parse(error.responseText);
				window.location.href=ctx+data.message;
			}else if(error.status == 403){
				var data = JSON.parse(error.responseText);
				if(typeof(_error)=="function"){
	    			_error(data.message);
		    	}
			}else if(error.status == 404 || error.status == 405){
				alert("请求地址没有找到 或者 请检查网络连接是否正常 ");
			}else if(error.status<500){//客户端异常
				alert("请检查网络连接是否正常");
	    	}else if(error.responseText){//服务端异常
	    		var data = JSON.parse(error.responseText);
	    		if(typeof(_error)=="function"){
	    			_error(data.message);
		    	}else{
		    		alert(data.message);
		    	}
	    	}else{
	    		alert("未知错误!");
	    	}
		}
	});
}

function errorTips(errorMessage){
	var errDiv = $("#errorContent");
	if(errDiv.length > 0){  //判断页面是否存在错误输出区域
		var err = $("<article ></article >");	
		if(errDiv){
			errDiv.removeClass("hide");
			var errMessage = "<div class='alert alert-danger fade in'>	<button class='close' data-dismiss='alert'> × </button> <i class='fa-fw fa fa-times'></i> <strong>错误!</strong>"+errorMessage+"</div>";
			err.html(errMessage);
			errDiv.html(err);
		}
	}else{  //将错误信息弹出来显示
		var errorDialog = $("<div class='hide center'><h1> "+errorMessage+" </h1> </div>");
		errorDialog.removeClass('hide').dialog({
			modal: true,
			draggable :false,
			closeOnEscape :true
		});
	}
}