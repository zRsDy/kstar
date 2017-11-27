
/*
 *	浏览器类型
 */
_BROWSER_IE = "IE";
_BROWSER_FF = "FF";
_BROWSER_OPERA = "OPERA";
_BROWSER = _BROWSER_IE;
/*
 *	对象类型
 */
_TYPE_NUMBER = "number";
_TYPE_OBJECT = "object";
_TYPE_FUNCTION = "function";
_TYPE_STRING = "string";
_TYPE_BOOLEAN = "boolean";
/*
 *	默认唯一编号名前缀
 */
_UNIQUE_ID_DEFAULT_PREFIX = "default__id";
/*
 *	核心包相对路径
 */
URL_CORE = "./";

__WAITING_IMG_PATH = "../images/";






/*
 *	对象名称：customPublic（全局静态对象）
 *	职责：负责公共函数
 *
 */
var customPublic = {};
/*
 *
 * 函数说明：浏览器识别
 * 返回值：
 *
 */
customPublic.checkBrowser = function(){
    var ua = navigator.userAgent.toUpperCase();
    if (ua.indexOf("MSIE") != -1) {
        _BROWSER = _BROWSER_IE;
    }
    else if (ua.indexOf("FIREFOX") != -1) {
        _BROWSER = _BROWSER_FF;
    }
    else if (ua.indexOf("OPERA") != -1) {
        _BROWSER = _BROWSER_OPERA;
    }
};
customPublic.checkBrowser();
/*
 *
 * 函数说明：执行方法
 * 参数：	string/function:callback	回调方法定义
 any:param					任何类型参数
 * 返回值：	类型不定:returnValue		返回值
 *
 */
customPublic.execCommand = function(callback, param){
    var returnValue = null;
    try {
        switch (typeof(callback)) {
            case _TYPE_STRING:
                returnValue = eval(callback);
                break;
            case _TYPE_FUNCTION:
                returnValue = callback(param);
                break;
            case _TYPE_BOOLEAN:
                returnValue = callback;
                break;
        }
    } 
    catch (e) {
        returnValue = false;
    }
    return returnValue;
};

/*
 *
 * 函数说明：初始化htc类型控件
 * 参数：	Object:obj			htc绑定的HTML对象
 string:flag			检测htc加载完成的特定属性
 string:eventName	加载完成的事件名称
 function:callback	回调方法
 * 返回值：
 *
 */
customPublic.initHTC = function(obj, flag, eventName, callback){
    if (obj == null || flag == null || callback == null) {
        alert("初始化HTC需要的参数为空，请检查");
        return;
    };
    if (obj[flag] != true) {
        obj[eventName] = function(){
            this[eventName] = null;
            customPublic.execCommand(callback);
        };
    }
    else {
        customPublic.execCommand(callback);
    }
};
/*
 *
 * 函数说明：显示等待状态
 * 参数：
 * 返回值：
 *
 */
customPublic.showWaitingLayer = function(){
    var _waitingDivObj = document.getElementById("_waitingDiv");
    if (_waitingDivObj == null) {
        _waitingDivObj = document.createElement("div");
        _waitingDivObj.id = "_waitingDiv";
        with (_waitingDivObj.style) {
            position = "absolute";
            left = "0px";
            top = "0px";
            width = "100%";
            height = "100%";
            zIndex = "999999";
            cursor = "default";
        }
        
        var tableStr = "<TABLE width=\"100%\" height=\"100%\"><TR><TD align=\"center\" valign=\"middle\"><span style=\"border:1px solid #7998B7;width:186px;height:22px;background:#FFFFFF;padding:2px\"><span><img src=\"" + __WAITING_IMG_PATH + "loading_16x16.gif\" /></span><span style=\"font:normal 12px '宋体';color:#2B61BA; height:22px; padding:5px 2px 0px 5px;\">数据加载中,请稍候...</span></span></TD></TR></TABLE>";
        tableStr += _BROWSER == "<div style=\"background:black;" + ("IE" ? "filter:alpha(opacity=0);" : "-moz-opacity:0") + "width:100%;height:100%;position:absolute;left:0;top:0;z-index:10000;\"></div>";
        _waitingDivObj.innerHTML = tableStr;
        document.body.appendChild(_waitingDivObj);
    }
    if (_waitingDivObj != null) {
        _waitingDivObj.style.display = "block";
    }
};
/*
 *
 * 函数说明：隐藏等待状态
 * 参数：
 * 返回值：
 *
 */
customPublic.hideWaitingLayer = function(){
    var _waitingDivObj = document.getElementById("_waitingDiv");
    if (_waitingDivObj != null) {
        setTimeout(function(){
            _waitingDivObj.style.display = "none";
        }, 100);
    }
};
/*
 *
 * 函数说明：写入窗口标题
 * 参数：
 * 返回值：
 *
 */
customPublic.writeTitle = function(){
    if (null != window.dialogArguments) {
        var title = window.dialogArguments.title;
        if (null != title) {
            var blank = new Array(100).join("　");
            document.write("<title>" + title + blank + "</title>");
        }
    }
};
customPublic.writeTitle();

/*
 *	对象名称：UniqueID（全局静态对象）
 *	职责：负责生成对象唯一编号（为了兼容FF）
 *
 */
var UniqueID = {};
UniqueID.key = 0;
/*
 *	函数说明：创建一个唯一编号
 *	参数：	string:prefix		唯一编号名称前缀
 *	返回值：string:uniqueID     唯一编号
 *
 */
UniqueID.generator = function(prefix){
    var uid = String(prefix || _UNIQUE_ID_DEFAULT_PREFIX) + String(this.key);
    this.key++;
    return uid;
};




/*
 *	对象名称：customCache（全局静态对象）
 *	职责：负责管理页面上全局数据信息
 *
 */
var customCache = {};
customCache.Variables = new Collection();
customCache.XmlIslands = new Collection();

/*
 *	对象名称：Collection
 *	职责：负责存取集合成员
 *
 */
function Collection(){
    this.items = {};
}

/*
 *	函数说明：添加成员
 *	参数：	string:id       成员id
 any:item        集合成员
 *	返回值：
 *
 */
Collection.prototype.add = function(id, item){
    this.items[id] = item;
};
/*
 *	函数说明：删除成员
 *	参数：	string:id		要删除的成员id
 *	返回值：
 *
 */
Collection.prototype.del = function(id){
    delete this.items[id];
};
/*
 *	函数说明：清空所有成员
 *	参数：
 *	返回值：
 *
 */
Collection.prototype.clear = function(){
    this.items = {};
};
/*
 *	函数说明：获取成员
 *	参数：	string:id       要获取的成员id
 *	返回值：
 */
Collection.prototype.get = function(id){
    return this.items[id];
};
/*
 *	函数说明：原型继承
 *	参数：	function:Class		将被继承的类
 *	返回值：
 *
 */
Collection.prototype.inherit = function(Class){
    var inheritClass = new Class();
    for (var item in inheritClass) {
        this[item] = inheritClass[item];
    }
};

/*
 *	对象名称：Cookie（全局静态对象）
 *	职责：负责管理页面上cookie数据
 *
 */
var Cookie = {};
/*
 *	函数说明：写入cookie
 *	参数：	string:name     cookie名
 string:value    cookie值
 *	返回值：
 *
 */
Cookie.setValue = function(name, value, expires, path){
    if (null == expires) {
        var exp = new Date();
        exp.setTime(exp.getTime() + (365 * 24 * 60 * 60 * 1000));
        expires = exp.toGMTString();
    }
    if (null == path) {
        path = "/";
    }
    window.document.cookie = name + "=" + escape(value) + "; expires=" + expires + ";path=" + path;
};
/*
 *	函数说明：读取cookie
 *	参数：	string:name     cookie名
 *	返回值：string:
 */
Cookie.getValue = function(name){
    var value = null;
    var arr = window.document.cookie.split(";");
    for (var i = 0, iLen = arr.length; i < iLen; i++) {
        var separator = arr[i].indexOf("=");
        var curName = arr[i].substring(0, separator).replace(/^ /gi, "");
        var curValue = arr[i].substring(separator + 1);
        if (name == curName) {
            value = unescape(curValue);
        }
    }
    return value;
};
/*
 *	函数说明：删除cookie
 *	参数：	string:name     cookie名
 string:path     cookie路径
 *	返回值：
 */
Cookie.del = function(name, path){
    var expires = new Date(0).toGMTString();
    var cval = "";
    
    this.setValue(name, cval, expires, path);
};


/*
 *	对象名称：Query（全局静态对象）
 *	职责：负责获取当前页面地址参数
 *
 */
var customQueryObject = {};
customQueryObject.items = {};
/*
 *	函数说明：获取参数值
 *	参数：	string:name             根据名称获取参数值
 boolean:decode          是否需要解码
 *	返回值：string:value			参数值
 */
customQueryObject.get = function(name, decode){
    var str = this.items[name];
    if (true == decode) {
        str = unescape(str);
    }
    return str;
};
/*
 *	函数说明：获取参数值
 *	参数：	string:name             根据名称获取参数值
 string:value			参数值
 *	返回值：
 */
customQueryObject.customSet = function(name, value){
    this.items[name] = value;
};

/*
 *	函数说明：解析地址参数
 *	参数：	string:queryString      地址参数
 *	返回值：
 *
 */
customQueryObject.parse = function(queryString){
    var query = queryString.split("&");
    for (var i = 0, iLen = query.length; i < iLen; i++) {
        var str = query[i];
        var name = str.split("=")[0];
        var value = str.split("=")[1];
        this.customSet(name, value);
    }
    
};
/*
 *	函数说明：初始化
 *	参数：
 *	返回值：
 *
 */
customQueryObject.init = function(){
    var queryString = window.location.search.substring(1);
    this.parse(queryString);
};
customQueryObject.init();




///*
// *	函数说明：扩展数组，增加数组项
// *	参数：	any:item		将被增加的项
// *	返回值：
// *     此方法非常糟烂
// */
//Array.prototype.push = function(item){
//    this[this.length] = item;
//};
///*
// *	函数说明：扩展字符串，转化特殊字符为实体
// *	参数：
// *	返回值：string:str      转换后的字符串
// */
String.prototype.convertEntity = function(){
    var str = this;
    str = str.replace(/\&/g, "&amp;");
    str = str.replace(/\"/g, "&quot;");
    str = str.replace(/\</g, "&lt;");
    str = str.replace(/\>/g, "&gt;");
    return str;
};
/*
 *	函数说明：扩展字符串，还原实体
 *	参数：
 *	返回值：string:str      转换后的字符串
 */
String.prototype.revertEntity = function(){
    var str = this;
    str = str.replace(/&quot;/g, "\"");
    str = str.replace(/&lt;/g, "\<");
    str = str.replace(/&gt;/g, "\>");
    str = str.replace(/&amp;/g, "\&");
    return str;
};
/*
 *	函数说明：扩展字符串，转化CDATA为实体
 *	参数：
 *	返回值：string:str      转换后的字符串
 */
String.prototype.convertCDATA = function(){
    var str = this;
    str = str.replace(/\<\!\[CDATA\[/g, "&lt;![CDATA[");
    str = str.replace(/\]\]>/g, "]]&gt;");
    return str;
};
/*
 *	函数说明：扩展字符串，还原CDATA
 *	参数：
 *	返回值：string:str      转换后的字符串
 */
String.prototype.revertCDATA = function(){
    var str = this;
    str = str.replace(/&lt;\!\[CDATA\[/g, "<![CDATA[");
    str = str.replace(/\]\]&gt;/g, "]]>");
    return str;
};
/*
 *	函数说明：根据给定字符串裁减原字符串
 *	参数：	string:trimStr      要裁减的字符串
 *	返回值：string:str      裁减后的字符串
 */
String.prototype.trim = function(trimStr){
    var str = this;
    if (0 == str.indexOf(trimStr)) {
        str = str.substring(trimStr.length);
    }
    return str;
};
/*
 *	函数说明：按字节，从起始位置到终止位置截取
 *	参数：	number:startB       起始字节位置
 number:endB         终止字节位置
 *	返回值：string:str          截取后的字符串
 *	补充说明：当起始位置落在双字节字符中间时，强制成该字符右侧；当终止位置落在双字节字符中间时，强制成该字符左侧
 *
 */
String.prototype.substringB = function(startB, endB){
    var str = this;
    
    var start, end;
    var iByte = 0;
    for (var i = 0; i < str.length; i++) {
    
        if (iByte >= startB && null == start) {
            start = i;
        }
        if (iByte > endB && null == end) {
            end = i - 1;
        }
        else if (iByte == endB && null == end) {
            end = i;
        }
        
        var chr = str.charAt(i);
        if (true == /[^\u0000-\u00FF]/.test(chr)) {
            iByte += 2;
        }
        else {
            iByte++;
        }
    }
    return str.substring(start, end);
};
/*
 *	函数说明：按字节，从起始位置开始截取指定字节数
 *	参数：	number:startB       起始字节位置
 number:lenB         截取字节数
 *	返回值：string:str          截取后的字符串
 *	补充说明：当起始位置落在双字节字符中间时，强制成该字符右侧；当终止位置落在双字节字符中间时，强制成该字符左侧
 *
 */
String.prototype.substrB = function(startB, lenB){
    var str = this;
    return str.substringB(startB, startB + lenB);
};
/*
 *	函数说明：扩展日期，获取四位数年份
 *	参数：
 *	返回值：
 */
Date.prototype.getFullYear = function(){
    var year = this.getYear();
    if (year < 1000) {
        year += 1900;
    }
    return year;
};

/*
 *	函数说明：将变量值转换成字符串
 *	参数：	any:value       变量
 *	返回值：
 */
function convertToString(value){
    var str = "";
    switch (typeof(value)) {
        case "number":
        case "boolean":
        case "function":
            str = value.toString();
            break;
        case "object":
            if (null == value) {
                str = "null";
            }
            else {
                if (null != value.toString) {
                    str = value.toString();
                }
                else {
                    str = "[object]";
                }
            }
            break;
        case "string":
            str = value;
            break;
        case "undefined":
            str = "";
            break;
    }
    return str;
    
}

/*
 *	函数说明：重新封装alert
 *	参数：	string:info     简要信息
 string:detail   详细信息
 *	返回值：
 */
function Alert(info, detail){
    info = convertToString(info);
    detail = convertToString(detail);
    
    var maxWords = 100;
    var params = {};
    params.type = "alert";
    params.info = info;
    params.detail = detail;
    if ("" == detail && maxWords < info.length) {
        params.info = info.substring(0, maxWords) + "...";
        params.detail = info;
    }
    params.title = "";
    window.showModalDialog(URL_CORE + '_info.htm', params, 'dialogwidth:280px; dialogheight:150px; status:no; help:no;resizable:yes;unadorned:yes');
}

/*
 *	函数说明：重新封装confirm
 *	参数：	string:info             简要信息
 string:detail           详细信息
 *	返回值：boolean:returnValue     用户选择确定/取消
 *
 */
function Confirm(info, detail){
    info = convertToString(info);
    detail = convertToString(detail);
    
    var maxWords = 100;
    var params = {};
    params.type = "confirm";
    params.info = info;
    params.detail = detail;
    if ("" == detail && maxWords < info.length) {
        params.info = info.substring(0, maxWords) + "...";
        params.detail = info;
    }
    params.title = "";
    var returnValue = window.showModalDialog(URL_CORE + '_info.htm', params, 'dialogwidth:280px; dialogheight:150px; status:no; help:no;resizable:yes;unadorned:yes');
    return returnValue;
}

/*
 *	函数说明：重新封装prompt
 *	参数：	string:info             简要信息
 string:defaultValue     默认值
 string:title            标题
 boolean:protect         是否保护
 *	返回值：string:returnValue      用户输入的文字
 */
function Prompt(info, defaultValue, title, protect){
    info = convertToString(info);
    defaultValue = convertToString(defaultValue);
    title = convertToString(title);
    
    var params = {};
    params.info = info;
    params.defaultValue = defaultValue;
    params.title = title;
    params.protect = protect;
    var returnValue = window.showModalDialog(URL_CORE + '_prompt.htm', params, 'dialogwidth:280px; dialogheight:150px; status:no; help:no;resizable:no;unadorned:yes');
    return returnValue;
}

/*
 *	函数说明：捕获页面js报错
 *	参数：
 *	返回值：
 */
function onError(msg, url, line){
    alert(msg, "错误:" + msg + "\r\n行:" + line + "\r\n地址:" + url);
    event.returnValue = true;
}

//    window._alert = window.alert;
//    window._confirm = window.confirm;
//    window._prompt = window.prompt;
//    window.alert = Alert;
//    window.confirm = Confirm;
//    window.prompt = Prompt;
//    window.onerror = onError;

//    document.oncontextmenu = function(eventObj){
//        if(null==eventObj){
//            eventObj = window.event;
//        }
//        var srcElement = Event.getSrcElement(eventObj);
//        var tagName = srcElement.tagName.toLowerCase();
//        if("input"!=tagName && "textarea"!=tagName){
//            event.returnValue = false;            
//        }
//        return false;
//    }

/*
 *	函数说明：常用方法缩写
 *	参数：
 *	返回值：
 *
 */
//$ = function(id){
//    return document.getElementById(id);
//};
customGetById = function(id){
return document.getElementById(id);
};
