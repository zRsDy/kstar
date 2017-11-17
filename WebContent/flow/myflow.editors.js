(function($){
var myflow = $.myflow;

$.extend(true, myflow.editors, {
	
	sqlInputEditor : function(id,hidden){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			var input = $('<input id="'+id+'" style="width:97%;"/>').val(props[_k].value).change(function(){
				debugger;
				props[_k].value = $(this).val();
				if(_k == 'text'){
					$(_r).trigger("textchange", [$(this).val(), _src])
				}
			});
			input.appendTo('#'+_div);
			var sql = $(input).val().replace(/\\'/g, "@@");
			sql = sql.replace(/'/g, "\\'");
			sql = sql.replace(/@@/g, "\\'");
			$(input).val(sql);
			if(hidden){
				input.parent().parent().parent().hide();
			}
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
				if(_k == 'text'){
					$(_r).trigger("textchange", [$(this).val(), _src])
				}
			});
		}
	},
	
	hiddenInputEditor : function(id,hidden){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			var input = $('<input id="'+id+'" style="width:97%;"/>').val(props[_k].value).change(function(){
				props[_k].value = $(this).val();
				if(_k == 'text'){
					$(_r).trigger("textchange", [$(this).val(), _src])
				}
			});
			input.appendTo('#'+_div);
			if(hidden){
				input.parent().parent().parent().hide();
			}
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
				if(_k == 'text'){
					$(_r).trigger("textchange", [$(this).val(), _src])
				}
			});
		}
	},
	hiddenProcessInputEditor : function(id,name){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			var input = $('<input id="'+id+'" style="width:97%;"/>').val(props[_k].value).change(function(){
				props[_k].value = $(this).val();
			});
			input.appendTo('#'+_div);
//			input.parent().parent().parent().hide();
			$('#'+id).val($('#'+name).val());
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		}
	}
	
});

})(jQuery);