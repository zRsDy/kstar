
/*
 *  错误类型
 */
_ERROR_TYPE_OPERATION_EXCEPTION = 0;
_ERROR_TYPE_KNOWN_EXCEPTION = 1;
_ERROR_TYPE_UNKNOWN_EXCEPTION = 2;
/*
 *  通讯用XML节点名
 */
_XML_NODE_RESPONSE_ROOT = "Response";
_XML_NODE_RESPONSE_ERROR = "Error";
_XML_NODE_RESPONSE_SUCCESS = "Success";
_XML_NODE_REQUEST_ROOT = "Request";
_XML_NODE_REQUEST_NAME = "Name";
_XML_NODE_REQUEST_VALUE = "Value";
_XML_NODE_REQUEST_PARAM = "Param";

_XML_NODE_RESPONSE_TIMEOUT_ERROR = "TimeoutERROR";
/*
 *  XML节点类型
 */
_XML_NODE_TYPE_ELEMENT = 1;
_XML_NODE_TYPE_ATTRIBUTE = 2;
_XML_NODE_TYPE_TEXT = 3;
_XML_NODE_TYPE_CDATA = 4;
_XML_NODE_TYPE_PROCESSING = 7;
_XML_NODE_TYPE_COMMENT = 8;
_XML_NODE_TYPE_DOCUMENT = 9;
/*
 *  HTTP响应状态
 */
_HTTP_RESPONSE_STATUS_LOCAL_OK = 0;
_HTTP_RESPONSE_STATUS_REMOTE_OK = 200;
/*
 *  HTTP响应解析结果类型
 */
_HTTP_RESPONSE_DATA_TYPE_EXCEPTION = "exception";
_HTTP_RESPONSE_DATA_TYPE_SUCCESS = "success";
_HTTP_RESPONSE_DATA_TYPE_DATA = "data";

//新老框架区别全局变量 2007-8-31
_NEW_OLD_FRAME_MARK = false;

/*
 *  对象名称：XMLHTTP请求参数对象
 *  职责：负责配置XMLHTTP请求参数
 */
function HttpRequestParams(){
    this.url = "";
    this.method = "POST"; //默认请求方式POST
    this.async = true; //默认异步
    this.content = {}; //存放提交数据各个字段
    this.header = {}; //存放http头信息
}

/*
 *	函数说明：设置发送数据
 *	参数：  string:name 		数据字段名
 *	string:value        数据内容
 *	boolean:flag        同名是否覆盖(默认true)
 *	返回值：
 */
HttpRequestParams.prototype.setContent = function(name, value, flag){
    if (false != flag) {
        //默认覆盖
        this.content[name] = value;
    }
    else {
        //不覆盖，追加
        var oldValue = this.content[name];
        if (null == oldValue) {
            //原先没有值
            this.content[name] = value;
        }
        else if (oldValue instanceof Array) {
            //原先已经是数组
            oldValue[oldValue.length] = value;
            this.content[name] = oldValue;
        }
        else {
            //原先是单值
            this.content[name] = [oldValue, value];
        }
    }
}

/*
 *	函数说明：设置xform专用格式发送数据
 *	参数：
 *	XmlNode:dataNode 	XmlNode实例，xform的data数据节点
 *	string:prefix 	    提交字段前缀
 *	返回值：
 */
HttpRequestParams.prototype.setXFormContent = function(dataNode, prefix){
    if ("data" == dataNode.nodeName) {
        var nodes = dataNode.selectNodes("./row/*");
        for (var i = 0, iLen = nodes.length; i < iLen; i++) {
            var name = nodes[i].nodeName;
            var value = nodes[i].text;
            
            //2006-7-19 从data节点上获取保存名，如果没有则用原名
            var rename = dataNode.getAttribute(name);
            if (null != rename) {
                name = rename;
            }
            
            if (null != prefix) {
                name = prefix + "." + name;
            }
            
            this.setContent(name, value, false);
        }
    }
}

/*
 *	函数说明：清除发送数据
 *	参数：	string:name 		数据字段名
 *	返回值：
 */
HttpRequestParams.prototype.clearContent = function(name){
    delete this.content[name];
}

/*
 *	函数说明：清除所有发送数据
 *	参数：
 *	返回值：
 */
HttpRequestParams.prototype.clearAllContent = function(){
    this.content = {};
}

/*
 *	函数说明：设置请求头信息
 *	参数：
 *	string:name 		头信息字段名
 *	string:value        头信息内容
 *	返回值：
 */
HttpRequestParams.prototype.setHeader = function(name, value){
    this.header[name] = value;
}





/*
 *  对象名称：XMLHTTP请求对象
 *  职责：负责发起XMLHTTP请求并接收响应数据
 
 *
 */
function HttpRequest(paramsInstance){
    this.value = "";
    
    this.xmlhttp = new XmlHttp();
    this.xmldom = new XmlReader();
    
    this.params = paramsInstance;
    
    //存入队列
    HttpRequests.add(this);
}

/*
 *	函数说明：获取响应数据源代码
 *	参数：
 *	返回值：string:result       响应数据源代码
 */
HttpRequest.prototype.getResponseText = function(){
    var result = this.value;
    return result;
}

/*
 *	函数说明：获取响应数据XML文档对象
 *	参数：
 *	返回值：XmlReader:xmlReader       XML文档对象
 */
HttpRequest.prototype.getResponseXml = function(){
    var xmlReader = this.xmldom;
    return xmlReader;
}

/*
 *	函数说明：获取响应数据XML文档指定节点对象值
 *	参数：	string:name             指定节点名
 *	返回值：any:value               根据节点内容类型不同而定
 */
HttpRequest.prototype.getNodeValue = function(name){
    var value = null;
    if (this.xmldom.documentElement == null) {
		return value;
    }
    var documentElement = new XmlNode(this.xmldom.documentElement);
    // 登录超时处理 2008-11-15
    if (documentElement.selectSingleNode(".//" + _XML_NODE_RESPONSE_TIMEOUT_ERROR) != null) {
        try {
            var errorNode = documentElement.selectSingleNode(".//" + _XML_NODE_RESPONSE_TIMEOUT_ERROR);
            var timeoutNode = errorNode.selectSingleNode(".//timeout");
            var errorInfo = timeoutNode.getCDATA(".");
            ShowPageError(errorInfo, "timeout", true);
        } 
        catch (e) {
        };
        return null;
    }
    var responseNode = documentElement.selectSingleNode("/" + _XML_NODE_RESPONSE_ROOT);
    //2007-8-30
    if (null == responseNode) {
        var node = documentElement.selectSingleNode("/" + name);
    }
    else {
        var node = documentElement.selectSingleNode("/" + _XML_NODE_RESPONSE_ROOT + "/" + name);
    }
    if (null != node) {
        var data = null;
        var datas = node.selectNodes("node()");
        for (var i = 0, iLen = datas.length; i < iLen; i++) {
            var tempData = datas[i];
            switch (tempData.nodeType) {
                case _XML_NODE_TYPE_TEXT:
                    if ("" != tempData.nodeValue.replace(/\s*/g, "")) {
                        data = tempData;
                    }
                    break;
                case _XML_NODE_TYPE_ELEMENT:
                case _XML_NODE_TYPE_CDATA:
                    data = tempData;
                    break;
            }
            if (null != data) {
                break;
            }
        }
        
        if (null != data) {
            //2006-7-1 返回复制节点，以便清除整个原始文档
            data = data.cloneNode(true);
            switch (data.nodeType) {
                case _XML_NODE_TYPE_ELEMENT:
                    value = data;
                    break;
                case _XML_NODE_TYPE_TEXT:
                case _XML_NODE_TYPE_CDATA:
                    value = data.nodeValue;
                    break;
            }
        }
    }
    return value;
};

/*
 *	函数说明：发起XMLHTTP请求
 *	参数：	string:content          发送数据内容
 *	返回值：
 */
HttpRequest.prototype.send = function(unShowWaitingLayer){
    var oThis = this;
    if (null == unShowWaitingLayer) {
        unShowWaitingLayer = false;
    }
    
    try {
        if (!unShowWaitingLayer == true) {
            customPublic.showWaitingLayer();
        }
        
        this.xmlhttp.onreadystatechange = function(){
            if (4 == oThis.xmlhttp.readyState) {
                var response = {};
                response.responseText = oThis.xmlhttp.responseText;
                response.responseXML = oThis.xmlhttp.responseXML;
                response.status = oThis.xmlhttp.status;
                response.statusText = oThis.xmlhttp.statusText;
                setTimeout(function(){
                    oThis.abort();
                    
                    if (!unShowWaitingLayer == true) {
                        customPublic.hideWaitingLayer();
                    }
                    oThis.onload(response);
                }, 100);
            }
        };
        this.xmlhttp.open(this.params.method, this.params.url, this.params.async);
        
        this.packageContent();
        this.setCustomRequestHeader();
        this.xmlhttp.send(this.requestBody);
    } 
    catch (e) {
        customPublic.hideWaitingLayer();
        
        //throw e;
        var tempParserResult = {};
        tempParserResult.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
        tempParserResult.type = 1;
        tempParserResult.msg = e.description;
        tempParserResult.description = e.description;
        tempParserResult.source = "";
        
        this.onexception(tempParserResult);
    }
};

/*
 *	函数说明：对发送数据进行封装
 *	参数：
 *	返回值：    XmlReader:contentXml    XmlReader实例
 */
HttpRequest.prototype.packageContent = function(){
    var contentStr = "";
    var contentXml = new XmlReader("<" + _XML_NODE_REQUEST_ROOT + "/>");
    var contentXmlRoot = new XmlNode(contentXml.documentElement);
    
    function setParamNode(name, value){
        var tempNameNode = contentXml.createElement(_XML_NODE_REQUEST_NAME);
        var tempCDATANode = contentXml.createCDATA(name);
        tempNameNode.appendChild(tempCDATANode);
        
        var tempValueNode = contentXml.createElement(_XML_NODE_REQUEST_VALUE);
        var tempCDATANode = contentXml.createCDATA(value);
        tempValueNode.appendChild(tempCDATANode);
        
        var tempParamNode = contentXml.createElement(_XML_NODE_REQUEST_PARAM);
        tempParamNode.appendChild(tempNameNode);
        tempParamNode.appendChild(tempValueNode);
        
        contentXmlRoot.appendChild(tempParamNode);
        
    }
    
    for (var name in this.params.content) {
        var value = this.params.content[name];
        if (value == null) {
            continue;
        }
        
        if (value instanceof Array) {
            for (var i = 0, iLen = value.length; i < iLen; i++) {
                setParamNode(name, value[i]);
            }
        }
        else {
            setParamNode(name, value);
        }
    }
    contentStr = contentXml.toXml();
    this.xmlhttp.setRequestHeader("Content-Length", contentStr.length);
    this.requestBody = contentStr;
};

/*
 *	函数说明：设置自定义请求头信息
 *	参数：
 *	返回值：
 */
HttpRequest.prototype.setCustomRequestHeader = function(){
    this.xmlhttp.setRequestHeader("REQUEST-TYPE", "xmlhttp");
    this.xmlhttp.setRequestHeader("REFERER", this.params.url);
    for (var item in this.params.header) { //设置自定义http头信息
        var itemValue = String(this.params.header[item]);
        if (itemValue != "") {
            this.xmlhttp.setRequestHeader(item, itemValue);
        }
    }
    this.xmlhttp.setRequestHeader("CONTENT-TYPE", "text/xml");
    this.xmlhttp.setRequestHeader("CONTENT-TYPE", "application/octet-stream");
};

/*
 *	函数说明：加载数据完成，对结果进行处理
 *	参数：	Object:response     该对象各属性值继承自xmlhttp对象
 *	返回值：
 */
HttpRequest.prototype.onload = function(response){
    this.value = response.responseText;
    
    //远程200本地0才允许
    var httpStatus = response.status;
    var httpStatusText = response.statusText;
    if (httpStatus != _HTTP_RESPONSE_STATUS_LOCAL_OK && httpStatus != _HTTP_RESPONSE_STATUS_REMOTE_OK) {
        var param = {};
        param.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
        param.type = 1;
        param.source = this.value;
        //param.msg = "HTTP " + httpStatus + " 错误\r\n" + httpStatusText;
        param.msg = "HTTP " + httpStatus + " 错误\r\n";
        param.description = "请求远程地址\"" + this.params.url + "\"出错";
        param.code = httpStatus;
        new Message_Exception(param, this);
        this.returnValue = false;
        return;
    }
    
    var responseParser = new HTTP_Response_Parser(this.value);
    
    //将通过解析后的xmlReader赋予xmldom
    this.xmldom = responseParser.xmlReader;
    
    this.ondata();
    this.onresult();
    this.returnValue = true;
    
    //2006-7-1 清除原始文档
    this.xmldom.loadXML("");
};
HttpRequest.prototype.ondata = HttpRequest.prototype.onresult = HttpRequest.prototype.onsuccess = HttpRequest.prototype.onexception = function(){

};
/*
 *	函数说明：终止XMLHTTP请求
 *	参数：
 *	返回值：
 */
HttpRequest.prototype.abort = function(){
    this.xmlhttp.abort();
};

/*
 *  对象名称：HTTP_Response_Parser对象
 *  职责：负责分析处理后台响应数据
 *
 *  成功信息格式
 *  <Response>
 *      <Success>
 *          <type>1</type>
 *          <msg><![CDATA[ ]]></msg>
 *          <description><![CDATA[ ]]></description>
 *      </Success>
 *  </Response>
 *
 *  错误信息格式
 *  <Response>
 *      <Error>
 *          <type>1</type>
 *          <relogin>1</relogin>
 *          <msg><![CDATA[ ]]></msg>
 *          <description><![CDATA[ ]]></description>
 *      </Error>
 *  </Response>
 */
function HTTP_Response_Parser(responseText){
    this.source = responseText;
    this.xmlReader = new XmlReader(responseText);
    this.init();
}

/*
 *	函数说明：初始化实例
 *	参数：
 *	返回值：
 */
HTTP_Response_Parser.prototype.init = function(){
    this.result = {};
    var parseError = this.xmlReader.getParseError();
    if (null != parseError) {
        this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
        this.result.source = this.source;
        this.result.msg = "数据出错在第" + parseError.line + "行第" + parseError.linepos + "字符";
        this.result.description = parseError.reason;
    }
}

/*
 *  对象名称：Message_Exception对象
 *  职责：负责处理异常信息
 *
 */
function Message_Exception(param, request){
    request.ondata();
    
    var errorCode = param.code;
    var ed = new HttpError();
    var erDes = ed.get(errorCode);
    alert(erDes);
    
    request.onexception();
}


/*
 *  对象名称：XmlHttp对象
 *  职责：负责XmlHttp对象创建
 *
 */
function XmlHttp(){
    if (window.ActiveXObject) {
        var service = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP", "MSXML.XMLHTTP", "MSXML3.XMLHTTP"];
        for (var i = 0, iLen = service.length; i < iLen; i++) {
            try {
                return new ActiveXObject(service[i]);
            } 
            catch (ex) {
            }
        }
        alert("您的浏览器不支持XMLHTTP");
    }
    else if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    }
    else {
        alert("您的浏览器不支持XMLHTTP");
        return null;
    }
}

/*
 *	对象名称：HttpRequests（全局静态对象）
 *	职责：负责所有http请求连接
 *
 */
var HttpRequests = {};
HttpRequests.items = [];

/*
 *	函数说明：终止所有请求连接
 *	参数：
 *	返回值：
 */
HttpRequests.closeAll = function(){
    for (var i = 0, iLen = this.items.length; i < iLen; i++) {
        this.items[i].abort();
    }
};

/*
 *	函数说明：添加一个请求连接
 *	参数：
 *	返回值：
 */
HttpRequests.add = function(request){
    this.items[this.items.length] = request;
};

/*
 *	对象名称：HttpError（全局静态对象）
 *	职责：http请求连接错误信息
 *
 */
HttpError = function(){
    this.item = [];
    this.description = [];
    this.addError();
}

/*
 *	函数说明：添加一个错误信息
 *	参数：
 *	返回值：
 */
HttpError.prototype.push = function(code, des){
    this.item[this.item.length] = code;
    this.description[this.description.length] = des;
}

/*
 *	函数说明：根据错误代码，取出错误信息描述
 *	参数：
 *	返回值：
 */
HttpError.prototype.get = function(code){
    var index = -1;
    for (var i = 0, iLen = this.item.length; i < iLen; i++) {
        if (this.item[i] == code) {
            index = i;
            break;
        }
    }
    return index == -1 ? "网络繁忙或网络不可用" : this.description[index];
}

/*
 *	函数说明：加入错误信息
 *	参数：
 *	返回值：
 */
HttpError.prototype.addError = function(){
    this.push("400", "请求无效");
    this.push("401.1", "未授权：登录失败");
    this.push("401.2", "未授权：服务器配置问题导致登录失败");
    this.push("401.3", "ACL 禁止访问资源");
    this.push("401.4", "未授权：授权被筛选器拒绝");
    this.push("401.5", "未授权：ISAPI 或 CGI 授权失败");
    this.push("403", "禁止访问");
    this.push("403", "对 Internet服务管理器 (HTML) 的访问仅限于 Localhost");
    this.push("403.1", "禁止访问：禁止可执行访问");
    this.push("403.2", "禁止访问：禁止读访问");
    this.push("403.3", "禁止访问：禁止写访问");
    this.push("403.4", "禁止访问：要求 SSL");
    this.push("403.5", "禁止访问：要求 SSL 128");
    this.push("403.6", "禁止访问：IP 地址被拒绝");
    this.push("403.7", "禁止访问：要求客户证书");
    this.push("403.8", "禁止访问：禁止站点访问");
    this.push("403.9", "禁止访问：连接的用户过多");
    this.push("403.10", "禁止访问：配置无效");
    this.push("403.11", "禁止访问：密码更改");
    this.push("403.12", "禁止访问：映射器拒绝访问");
    this.push("403.13", "禁止访问：客户证书已被吊销");
    this.push("403.15", "禁止访问：客户访问许可过多");
    this.push("403.16", "禁止访问：客户证书不可信或者无效");
    this.push("403.17", "禁止访问：客户证书已经到期或者尚未生效");
    this.push("404", "未找到：在对应地址中找不到可用站点");
    this.push("404.0", "没有找到文件或目录");
    this.push("404.1", "无法找到 Web 站点,无法找到文件");
    this.push("405", "资源被禁止");
    this.push("406", "无法接受");
    this.push("407", "要求代理身份验证");
    this.push("410", "永远不可用");
    this.push("412", "先决条件失败");
    this.push("414", "请求", "URI 太长");
    this.push("500", "内部服务器错误");
    this.push("500.100", "内部服务器错误,ASP 错误");
    this.push("500-11", "服务器关闭");
    this.push("500-12", "应用程序重新启动");
    this.push("500-13", "服务器太忙");
    this.push("500-14", "应用程序无效");
    this.push("500-15", "不允许请求 global.asa");
    this.push("501", "未实现");
    this.push("502", "网关错误");
}
