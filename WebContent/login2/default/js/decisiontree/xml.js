
/*
 *  对象名称：XmlReader对象
 *  职责：负责XML文档级操作
 *
 */
function XmlReader(xmlstr){
    this.xmldom = null;
    this.parser = null;
    if (window.ActiveXObject) {
        var service = ["MSXML2.DOMDOCUMENT", "Microsoft.DOMDOCUMENT", "MSXML.DOMDOCUMENT", "MSXML3.DOMDOCUMENT"];
        for (var i = 0, iLen = service.length; i < iLen; i++) {
            try {
                this.xmldom = new ActiveXObject(service[i]);
            } 
            catch (ex) {
            }
        }
        if (null == this.xmldom) {
            alert("您浏览器未安装任何XML解析器");
        }
    }
    else if (window.DOMParser) {
        this.parser = new DOMParser();
    }
    if (xmlstr != null) {
        this.loadXML(xmlstr);
    }
}

/*
 *	函数说明：创建新节点
 *	参数：	string:name 		新节点名
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlReader.prototype.createElement = function(name){
    var node = this.xmldom.createElement(name);
    var xmlNode = new XmlNode(node);
    return xmlNode;
}

/*
 *	函数说明：创建CDATA节点
 *	参数：	string:data 		CDATA节点内容
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlReader.prototype.createCDATA = function(data){
    data = String(data).convertCDATA();
    
    var xmlNode = null;
    if (window.ActiveXObject) {
        var node = this.xmldom.createCDATASection(data);
        xmlNode = new XmlNode(node);
    }
    else if (window.DOMParser) {
        var tempReader = new XmlReader("<root><![CDATA[" + data + "]]></root>");
        xmlNode = new XmlNode(tempReader.documentElement.firstChild);
    }
    return xmlNode;
};

/*
 *	函数说明：创建带有CDATA子节点的节点
 *	参数：
 *	string:name 		新节点名
 *	string:data 		CDATA节点内容
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlReader.prototype.createElementCDATA = function(name, data){
    var xmlNode = this.createElement(name);
    var cdataNode = this.createCDATA(data);
    xmlNode.appendChild(cdataNode);
    return xmlNode;
};

/*
 *	函数说明：从字符串加载文档
 *	参数：	string:xmlstr 		xml文档字符串
 *	返回值：
 */
XmlReader.prototype.loadXML = function(xmlstr){
    if (window.ActiveXObject) {
        this.xmldom.async = false;
        this.xmldom.loadXML(xmlstr);
    }
    else if (window.DOMParser) {
        this.xmldom = this.parser.parseFromString(xmlstr, "text/xml");
    }
    this.documentElement = this.xmldom.documentElement;
};

/*
 *	函数说明：从文件地址加载文档
 *	参数：
 *	string:url 		    xml文档地址
 *	boolean:asynce      是否异步加载
 *	返回值：
 */
XmlReader.prototype.load = function(url, async){
    if (window.ActiveXObject) {
        var thisObj = this;
        this.xmldom.async = async;
        this.xmldom.onreadystatechange = function(){
            if (thisObj.xmldom.readyState == 4) {
                var onloadType = typeof(thisObj.onLoad);
                try {
                    if (onloadType == "function") {
                        thisObj.onLoad();
                    }
                    else if (onloadType == "string") {
                        eval(thisObj.onLoad);
                    }
                } 
                catch (e) {
                
                }
            }
        };
        this.xmldom.load(url);
    }
    else if (window.DOMParser) {
        this.xmldom.parseFromString(xmlstr, "text/xml");
    }
    this.documentElement = this.xmldom.documentElement;
};

/*
 *	函数说明：获取解析错误
 *	参数：
 *	返回值：parseError:parseError       parseError对象
 *
 */
XmlReader.prototype.getParseError = function(){
    var parseError = null;
    if (window.ActiveXObject) {
        var tempParseError = this.xmldom.parseError;
        if (0 != tempParseError.errorCode) {
            parseError = tempParseError;
        }
    }
    else if (window.DOMParser) {
    
    }
    return parseError;
}

/*
 *	函数说明：以文本方式输出对象信息
 *	参数：
 *	返回值：
 */
XmlReader.prototype.toString = function(){
    var str = [];
    str[str.length] = "[XmlReader 对象]";
    str[str.length] = "xml:" + this.toXml();
    return str.join("\r\n");
}

/*
 *	函数说明：以文本方式输出XML文档
 *	参数：
 *	返回值：
 */
XmlReader.prototype.toXml = function(){
    var str = "";
    if (window.ActiveXObject) {
        str = this.xmldom.xml;
    }
    else if (window.DOMParser) {
        var xmlSerializer = new XMLSerializer();
        str = xmlSerializer.serializeToString(this.xmldom.documentElement);
    }
    else {
    }
    return str;
};

XmlReader.prototype.onLoad = function(){

};

/*
 *  对象名称：XmlNode对象
 *  职责：负责XML节点级操作
 *
 */
function XmlNode(node){
    if (null != node) {
        this.node = node;
        this.nodeName = this.node.nodeName;
        this.nodeType = this.node.nodeType;
        this.nodeValue = this.node.nodeValue;
        this.text = this.node.text;
        this.firstChild = this.node.firstChild;
        this.lastChild = this.node.lastChild;
        this.childNodes = this.node.childNodes;
        this.attributes = this.node.attributes;
    }
}

/*
 *	函数说明：获取节点属性
 *	参数：	string:attrName		属性名
 *	返回值：string:attrValue    属性值
 */
XmlNode.prototype.getAttribute = function(attrName){
    if ("1" != this.nodeType) {
        return;
    }
    var attrValue = this.node.getAttribute(attrName);
    return attrValue;
};

/*
 *	函数说明：设置节点属性
 *	参数：
 *	string:attrName		属性名
 *	string:attrValue    属性值
 *	string:format       属性存放格式(0默认/1表示CDATA形式)
 *	返回值：
 */
XmlNode.prototype.setAttribute = function(attrName, attrValue, format){
    if ("1" != this.nodeType) {
        return;
    }
    if (null == attrValue) {//属性值为空，视作删除该属性
        if (1 == format) {
            this.removeCDATA(attrName);
        }
        else {
            this.removeAttribute(attrName);
        }
    }
    else {
        if (1 == format) {
            this.setCDATA(attrName, attrValue);
        }
        else {
            this.node.setAttribute(attrName, attrValue);
        }
    }
};

/*
 *	函数说明：删除节点属性
 *	参数：	string:attrName		属性名
 *	返回值：
 */
XmlNode.prototype.removeAttribute = function(attrName){
    if ("1" != this.nodeType) {
        return;
    }
    this.node.removeAttribute(attrName);
}

/*
 *	函数说明：获取节点CDATA格式属性
 *	参数：	string:attrName		属性名
 *	返回值：string:attrValue    属性值
 */
XmlNode.prototype.getCDATA = function(attrName){
    var attrValue;
    var attrNode = this.selectSingleNode(attrName + "/node()");
    if (null != attrNode) {
        attrValue = attrNode.nodeValue.revertCDATA();
    }
    return attrValue;
}

/*
 *	函数说明：设置节点CDATA格式属性
 *	参数：string:attrName		属性名
 *	string:attrValue    属性值
 *	返回值：
 */
XmlNode.prototype.setCDATA = function(attrName, attrValue){
    var oldNode = this.selectSingleNode(attrName);
    if (null == oldNode) {
        var xmlReader = new XmlReader("<xml/>");
        var newNode = xmlReader.createElementCDATA(attrName, attrValue);
        this.appendChild(newNode);
    }
    else {
        var cdataNode = oldNode.selectSingleNode("node()");
        cdataNode.removeNode();
        
        var xmlReader = new XmlReader("<xml/>");
        var cdataNode = xmlReader.createCDATA(attrValue);
        oldNode.appendChild(cdataNode);
    }
}

/*
 *	函数说明：删除节点CDATA格式属性
 *	参数：	string:attrName		属性名
 *	返回值：
 */
XmlNode.prototype.removeCDATA = function(attrName){
    var attrNode = this.selectSingleNode(attrName);
    if (null != attrNode) {
        attrNode.removeNode(true);
    }
}

/*
 *	函数说明：获取副本节点
 *	参数：	boolean:deep		是否复制子节点
 *	返回值：
 */
XmlNode.prototype.cloneNode = function(deep){
    return new XmlNode(this.node.cloneNode(deep));
}

/*
 *	函数说明：获取父节点
 *	参数：
 *	返回值：
 */
XmlNode.prototype.getParent = function(){
    var xmlNode = null;
    if (null != this.node.parentNode) {
        xmlNode = new XmlNode(this.node.parentNode);
    }
    return xmlNode;
}

/*
 *	函数说明：删除节点
 *	参数：
 *	返回值：
 */
XmlNode.prototype.removeNode = function(){
    var parentNode = this.node.parentNode;
    if (null != parentNode) {
        parentNode.removeChild(this.node);
    }
}

/*
 *	函数说明：返回表明对象是否有子对象的值。
 *	参数：
 *	返回值：boolean:
 */
XmlNode.prototype.hasChildNodes = function(){
    return this.node.hasChildNodes();
}

/*
 *	函数说明：获取节点
 *	参数：
 *	返回值：array:xmlNodes      XmlNode实例数组
 */
XmlNode.prototype.getChildNodes = function(){
    var xmlNodes = [];
    if (this.childNodes) {
        var childNodes = this.childNodes;
        for (var i = 0; i < childNodes.length; i++) {
            xmlNodes.push(new XmlNode(childNodes[i]));
        }
    }
    return xmlNodes;
}

/*
 *	函数说明：添加子节点
 *	参数：	XmlNode:xmlNode		XmlNode实例
 *	返回值：
 */
XmlNode.prototype.appendChild = function(xmlNode){
    this.node.appendChild(xmlNode.node);
    
    this.nodeValue = this.node.nodeValue;
    this.text = this.node.text;
    this.firstChild = this.node.firstChild;
    this.lastChild = this.node.lastChild;
    this.childNodes = this.node.childNodes;
}

/*
 *	函数说明：查询单个节点
 *	参数：	string:xpath		xpath
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlNode.prototype.selectSingleNode = function(xpath){
    var xmlNode = null;
    if (window.ActiveXObject) {
        var node = this.node.selectSingleNode(xpath);
        if (null != node) {
            xmlNode = new XmlNode(node);
        }
    }
    else {
        var ownerDocument = null;
        if (_XML_NODE_TYPE_DOCUMENT == this.nodeType) {
            ownerDocument = this.node;
        }
        else {
            ownerDocument = this.node.ownerDocument;
        }
        var xPathResult = ownerDocument.evaluate(xpath, this.node, ownerDocument.createNSResolver(ownerDocument.documentElement), 9, null);
        if (xPathResult && xPathResult.singleNodeValue) {
            xmlNode = new XmlNode(xPathResult.singleNodeValue);
        }
    }
    return xmlNode;
}

/*
 *	函数说明：查询多个节点
 *	参数：	string:xpath		xpath
 *	返回值：array:xmlNodes      XmlNode实例数组
 */
XmlNode.prototype.selectNodes = function(xpath){
    var xmlNodes = [];
    if (window.ActiveXObject) {
        var nodes = this.node.selectNodes(xpath);
        for (var i = 0, iLen = nodes.length; i < iLen; i++) {
            xmlNodes[xmlNodes.length] = new XmlNode(nodes[i]);
        }
    }
    else {
        var ownerDocument = null;
        if (_XML_NODE_TYPE_DOCUMENT == this.nodeType) {
            ownerDocument = this.node;
        }
        else {
            ownerDocument = this.node.ownerDocument;
        }
        var xPathResult = ownerDocument.evaluate(xpath, this.node, ownerDocument.createNSResolver(ownerDocument.documentElement), XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
        if (xPathResult) {
            var oNode = xPathResult.iterateNext();
            while (oNode) {
                xmlNodes[xmlNodes.length] = new XmlNode(oNode);
                oNode = xPathResult.iterateNext();
            }
        }
    }
    return xmlNodes;
}

/*
 *	函数说明：比较
 *	参数：	XmlNode:xmlNode     XmlNode实例
 *	返回值：boolean:flag        是否相等
 */
XmlNode.prototype.equals = function(xmlNode){
    var flag = false;
    if (null != xmlNode && this.node == xmlNode.node) {
        flag = true;
    }
    return flag;
}

/*
 *	函数说明：获取第一个子节点
 *	参数：
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlNode.prototype.getFirstChild = function(){
    var xmlNode = null;
    if (null != this.firstChild) {
        xmlNode = new XmlNode(this.firstChild);
    }
    return xmlNode;
}

/*
 *	函数说明：获取最后一个子节点
 *	参数：
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlNode.prototype.getLastChild = function(){
    var xmlNode = null;
    if (null != this.lastChild) {
        xmlNode = new XmlNode(this.lastChild);
    }
    return xmlNode;
}

/*
 *	函数说明：交换子节点
 *	参数：
 *	XmlNode:newChild        XmlNode实例
 *	XmlNode:oldChild        XmlNode实例
 *	返回值：
 */
XmlNode.prototype.replaceChild = function(newChild, oldChild){
    var oldParentNode = oldChild.getParent();
    if (null != oldParentNode && oldParentNode.equals(this)) {
        try {
            this.node.replaceChild(newChild.node, oldChild.node);
        } 
        catch (e) {
        
        }
    }
}

/*
 *	函数说明：交换节点
 *	参数：	XmlNode:xmlNode     XmlNode实例
 *	返回值：
 */
XmlNode.prototype.swapNode = function(xmlNode){
    var parentNode = this.getParent();
    if (null != parentNode) {
        parentNode.replaceChild(xmlNode, this)
    }
}

/*
 *	函数说明：获取前一个兄弟节点
 *	参数：
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlNode.prototype.getPrevSibling = function(){
    var xmlNode = null;
    if (null != this.node.previousSibling) {
        xmlNode = new XmlNode(this.node.previousSibling);
    }
    return xmlNode;
}

/*
 *	函数说明：获取后一个兄弟节点
 *	参数：
 *	返回值：XmlNode:xmlNode     XmlNode实例
 */
XmlNode.prototype.getNextSibling = function(){
    var xmlNode = null;
    if (null != this.node.nextSibling) {
        xmlNode = new XmlNode(this.node.nextSibling);
    }
    return xmlNode;
}

/*
 *	函数说明：以文本方式输出对象信息
 *	参数：
 *	返回值：
 */
XmlNode.prototype.toString = function(){
    var str = [];
    str[str.length] = "[XmlNode 对象]";
    str[str.length] = "nodeName:" + this.nodeName;
    str[str.length] = "nodeType:" + this.nodeType;
    str[str.length] = "nodeValue:" + this.nodeValue;
    str[str.length] = "xml:" + this.toXml();
    return str.join("\r\n");
}

/*
 *	函数说明：以文本方式输出XML节点
 *	参数：
 *	返回值：
 */
XmlNode.prototype.toXml = function(){
    var str = "";
    if (window.ActiveXObject) {
        str = this.node.xml;
    }
    else if (window.DOMParser) {
        var xmlSerializer = new XMLSerializer();
        str = xmlSerializer.serializeToString(this.xmldom.documentElement);
    }
    else {
    }
    return str;
}
