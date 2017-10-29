Date.prototype.format = function(format) {
	   var o = {
	       "M+": this.getMonth() + 1,
	       // month
	       "d+": this.getDate(),
	       // day
	       "h+": this.getHours(),
	       // hour
	       "m+": this.getMinutes(),
	       // minute
	       "s+": this.getSeconds(),
	       // second
	       "q+": Math.floor((this.getMonth() + 3) / 3),
	       // quarter
	       "S": this.getMilliseconds()
	       // millisecond
	   };
	   if (/(y+)/.test(format) || /(Y+)/.test(format)) {
	       format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	   }
	   for (var k in o) {
	       if (new RegExp("(" + k + ")").test(format)) {
	           format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	       }
	   }
	   return format;
};

function dateFmatter (cellvalue, options, rowObject) { 
	// do something here return new_format_value 
	if(cellvalue != null && cellvalue != ""){
		var timestamp = "";
		var timeV = cellvalue;
		if(!isNaN(timeV) && timeV){//timeV表示日期时间
			timestamp = (new Date(parseFloat(timeV))).format("yyyy-MM-dd");
		}
		return timestamp;
	}else{
		return "";
	}
};

function iniCheck(domId){
	var checkedValue= $("#" + domId).val();
	if(checkedValue == '0'){
		$("#" + domId).removeAttr("checked"); 
	}else if (checkedValue == '1'){
		$("#" + domId).attr("checked","true"); 
	}
}

function changeCheckValue(domId) {
	var checked = $(domId).is(':checked')
	if (checked == true) {
		$(domId).val('1');
		$(domId).attr("checked", "true");
	} else {
		$(domId).val('0');
		$(domId).removeAttr("checked");
	}
}

/**
 *
 * 描述：js实现的map方法
 * @returns {Map}
 */
function Map() {
    var struct = function (key, value) {
        this.key = key;
        this.value = value;
    };
// 添加map键值对
    var put = function (key, value) {
        for (var i = 0; i < this.arr.length; i++) {
            if (this.arr[i].key === key) {
                this.arr[i].value = value;
                return;
            }
        }
        this.arr[this.arr.length] = new struct(key, value);
    };
// 根据key获取value
    var get = function (key) {
        for (var i = 0; i < this.arr.length; i++) {
            if (this.arr[i].key === key) {
                return this.arr[i].value;
            }
        }
        return null;
    };
// 根据key删除
    var remove = function (key) {
        var v;
        for (var i = 0; i < this.arr.length; i++) {
            v = this.arr.pop();
            if (v.key === key) {
                continue;
            }
            this.arr.unshift(v);
        }
    };
// 获取map键值对个数
    var size = function () {
        return this.arr.length;
    };
// 判断map是否为空
    var isEmpty = function () {
        return this.arr.length <= 0;
    };
    this.arr = new Array();
    this.get = get;
    this.put = put;
    this.remove = remove;
    this.size = size;
    this.isEmpty = isEmpty;
}