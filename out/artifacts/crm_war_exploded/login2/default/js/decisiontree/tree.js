
function VerticalTree(){
    // xml数据
    this.data = null;
    
    // 生成树的父容器
    this.container = null;
    
    // 节点最大深度
    this.maxDepth = 0;
    
    // 单个节点生成后高度
    this.nodeHeight = 55;
    
    // 单个节点生成后宽度
    this.nodeWidth = 280;
    
    // 根节点左边距
    this.paddingLeft = 15;
    
    // 根节点上边距
    this.paddingTop = 95;
    
    // 生成HTML后 节点的宽度和高度
    this.nodeOffsetWidth = 126;
    this.nodeOffsetHeight = 45;
    
    // 线的颜色及样式
    this.lineColor = "#E89D40";
    this.lineStyle = "ShortDash";
    
    // 点击节点展开闭合时 滚动条微调参数
    this.adjustHeight = 20;
    
    // 是否给出节点名称提示
    this.showTextInfo = false;
    
    this.defaultLabelSeparator = "";
    
    this.defaultFieldName = "curValue";
    
    this.defaultFieldLabel = "";
    
    this.imageURL = __WAITING_IMG_PATH || "images/";
}

VerticalTree.prototype.round = function(v){
    v = (Math.round((v - 0) * 100)) / 100;
    v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10)) ? v + "0" : v);
    return v;
};

//VerticalTree.prototype.$ = function(id){
//    return document.getElementById(id);
//}
VerticalTree.prototype.customGetById = function(id){
    return document.getElementById(id);
};

VerticalTree.prototype.$C = function(tagName, attrObject){
    var elm = window.document.createElement(tagName);
    if (null != attrObject && typeof(attrObject) == "object") {
        for (var item in attrObject) {
            if (item == "innerHTML" || item == "className") 
                elm[item] = attrObject[item];
            else 
                elm.setAttribute(item, attrObject[item]);
        }
    }
    return elm;
}

VerticalTree.prototype.$A = function(elm, pElm){
    return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
}

VerticalTree.prototype.$R = function(elm, pElm){
    return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
}

VerticalTree.prototype.$T = function(tagName, pElm){
    return pElm ? pElm.getElementsByTagName(tagName) : document.getElementsByTagName(tagName);
}

VerticalTree.prototype.$M = function(att){
    if (att && typeof(att) == "string") {
        var reg = /(^\s*)|(\s*$)/g;
        var a = att.split("=")[0].replace(reg, "");
        var v = att.split("=")[1].replace(reg, "");
    }
    else {
        return [];
    }
    var arr = [];
    var elms = this.$T("*");
    for (var i = 0, iLen = elms.length; i < iLen; i++) {
        if (elms[i].getAttribute(a) == v) {
            arr.push(elms[i]);
        }
    }
    return arr;
}

VerticalTree.prototype.init = function(){
    this.containerRect = this.boundingRect(this.container);
    this.container.className = "treeOutDiv";
    this.bakData = this.data.cloneNode(true);
    this.removeUnExpandNode();
    this.setDepth(this.data, 0);
    this.setPosX();
    this.setPosY();
    this.buildTree();
    this.buildLine();
}

VerticalTree.prototype.removeUnExpandNode = function(){
    var tmpArr = new Array();
    var nodes = this.data.selectNodes(".//TreeNode");
    var nLen = nodes.length;
    for (var i = 0; i < nLen; i++) {
        var node = nodes[i];
        if (node.getAttribute("_opened") == "false") {
            var cLen = node.selectNodes("./TreeNode").length;//alert(cLen);
            for (var k = 0; k < cLen; k++) {
                var id = node.selectNodes("./TreeNode")[k].getAttribute("id");
                tmpArr.push(id);
            }
        }
        if (node.childNodes.length != 0) {
            node.setAttribute("_hasChildNodes", "true");
        }
    }
    var tLen = tmpArr.length;
    for (var j = 0; j < tLen; j++) {
        var curNode = this.data.selectSingleNode(".//TreeNode[@id='" + tmpArr[j] + "']");
        try {
            curNode.removeNode();
        } 
        catch (e) {
        }
    }
}

VerticalTree.prototype.setDepth = function(curNode, depth){
    this.maxDepth = this.maxDepth > depth ? this.maxDepth : depth;
    depth = depth + 1;
    var nodes = curNode.selectNodes("./TreeNode");
    var nLen = nodes.length;
    for (var i = 0; i < nLen; i++) {
        var node = nodes[i];
        node.setAttribute("_depth", depth);
        this.setDepth(node, depth);
    }
}

VerticalTree.prototype.setHeight = function(){
    for (var i = this.maxDepth; i > 0; i--) {
        var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + i + "']");
        var nLen = nodes.length;
        for (var j = 0; j < nLen; j++) {
            var curNode = nodes[j];
            if (curNode.childNodes.length == 0) {
                curNode.setAttribute("_height", this.nodeHeight);
            }
            var pNode = curNode.getParent();
            if (null != pNode && pNode.nodeName != "Tree") {
                var pHeight = pNode.getAttribute("_height");
                var curHeight = curNode.getAttribute("_height");
                if (null == pHeight) {
                    pNode.setAttribute("_height", curHeight);
                }
                else {
                    pNode.setAttribute("_height", parseInt(pHeight) + parseInt(curHeight));
                }
            }
        }
    }
}

VerticalTree.prototype.setPosX = function(){
    for (var i = this.maxDepth; i > 0; i--) {
        var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + i + "']");
        var nLen = nodes.length;
        for (var j = 0; j < nLen; j++) {
            var curNode = nodes[j];
            curNode.setAttribute("_order", j);
            var left = i == 1 ? this.paddingLeft : this.paddingLeft + this.nodeWidth * (i - 1);
            curNode.setAttribute("_posX", left);
        }
    }
}

VerticalTree.prototype.setPosY = function(){
    this.setHeight();
    for (var i = 1; i <= this.maxDepth; i++) {
        var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + i + "']");
        var nLen = nodes.length;
        for (var j = 0; j < nLen; j++) {
            var curNode = nodes[j];
            var height = curNode.getAttribute("_height");
            var pNode, cLen, pPosY, cOrder, preHeight;
            pNode = curNode.getParent();
            if (pNode != null && pNode.nodeName != "Tree") {
                cLen = pNode.childNodes.length;
                pPosY = pNode.getAttribute("_posY");
                cOrder = curNode.getAttribute("_order");
                var pHeight = pNode.getAttribute("_height");
                preHeight = this.getPreviousNodesHeight(i, cOrder, curNode);
                pPreHeight = this.getPreviousNodesHeight(i - 1, pNode.getAttribute("_order"), pNode);
            }
            var top = 0;
            if (i == 1) {
                top = parseInt(this.paddingTop) + parseFloat(height / 2) - parseFloat((this.nodeHeight) / 2);
            }
            else {
                top = parseFloat(pPosY) - parseFloat(pHeight / 2) + parseFloat(preHeight) + parseFloat(height / 2);
            }
            curNode.setAttribute("_posY", parseInt(top));
        }
    }
}

VerticalTree.prototype.getPreviousNodesHeight = function(layer, order, node){
    var pNode = node.getParent();
    var pId = pNode.getAttribute("id");
    var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + layer + "' and @_order<'" + order + "']");
    var height = 0;
    var nLen = nodes.length;
    for (var i = 0; i < nLen; i++) {
        var curNode = nodes[i];
        var curHeight = curNode.getAttribute("_height");
        var curPnode = curNode.getParent();
        var id = curPnode.getAttribute("id");
        if (id == pId) {
            height = parseFloat(height) + parseFloat(curHeight);
        }
    }
    return height;
}

VerticalTree.prototype.buildTree = function(){
    for (var i = 1; i <= this.maxDepth; i++) {
        var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + i + "']");
        var nLen = nodes.length;
        for (var j = 0; j < nLen; j++) {
            var curNode = nodes[j];
            this.createTreeNodeContent(curNode);
        }
    }
}

VerticalTree.prototype.createTreeNodeContent = function(curNode){
    var oThis = this;
    var posX = curNode.getAttribute("_posX");
    var posY = curNode.getAttribute("_posY");
    var id = curNode.getAttribute("id");
    var name = curNode.getAttribute("name");
    var type = curNode.getAttribute("type");
    var maxValue = curNode.getAttribute("maxValue");
    var minValue = curNode.getAttribute("minValue");
    var labelSeparator = curNode.getAttribute("labelSeparator");
    var fieldLabel = curNode.getAttribute("fieldLabel");
    var fieldName = curNode.getAttribute("fieldName");
    var value = curNode.getAttribute(fieldName ? fieldName : this.defaultFieldName);
    var colorDir = curNode.getAttribute("colorDir");
    var blockValueType = curNode.getAttribute("blockValueType");
    
    var treeNode = this.$C("div", {
        className: "nodeCon",
        id: "node_" + id,
        dataID: id
    });
    treeNode.style.left = posX + "px";
    treeNode.style.top = posY + "px";
    this.$A(treeNode, this.container);
    
    var treeNameDiv = this.$C("div", {
        className: "nodeDes",
        innerHTML: ["<nobr>", name, "</nobr>"].join('')
    });
    this.$A(treeNameDiv, treeNode);
    treeNameDiv.onclick = function(){
        oThis.editNodeName(this);
    };
    
    var html = ['<span>', fieldLabel ? fieldLabel : this.defaultFieldLabel, '</span>', '<span>', labelSeparator ? labelSeparator : this.defaultLabelSeparator, '</span>', '<span>', value, '</span>'].join('');
    
    //var html = value;
    if (type != "3") {
        var valueDiv = this.$C("div", {
            className: "nodeValue",
            innerHTML: html
        });
        if (type == "2") {
            valueDiv.innerHTML = html + "%";
            valueDiv.setAttribute("maxValue", maxValue + "%");
            valueDiv.setAttribute("minValue", minValue + "%");
        }
        else 
            if (type == "4" && blockValueType == "1") {
                valueDiv.innerHTML = html;
                valueDiv.setAttribute("maxValue", maxValue);
                valueDiv.setAttribute("minValue", minValue);
            }
            else {
                valueDiv.innerHTML = html;
                valueDiv.setAttribute("maxValue", maxValue);
                valueDiv.setAttribute("minValue", minValue);
            }
        valueDiv.setAttribute("type", type);
        valueDiv.setAttribute("blockValueType", blockValueType);
        this.$A(valueDiv, treeNode);
        
        if (type != "4") {
            var percentDiv = this.$C("div", {
                className: type == "5" ? "" : "nodePer",
                innerHTML: type == "5" ? "<div class='chTextDiv'><input type='text' class='chText' /></div>" : "<div class=\"nodePerValue\"></div>"
            });
            this.$A(percentDiv, treeNode);
        }
        else {
            var blockDiv = this.$C("div", {
                className: "slideBarCon",
                innerHTML: "<div class=\"moveDiv_block\"><div class=\"moveDiv_blockNode\"></div></div>"
            });
            var cDiv = blockDiv.getElementsByTagName("div")[0];
            cDiv.onmousemove = function(){
                oThis.judgePointerPos(curNode, this);
            }
            cDiv.onmousedown = function(){
                oThis.signMoveEvent(this);
            }
            cDiv.onmouseup = function(){
                oThis.stopPointerMove(this);
            }
            this.$A(blockDiv, treeNode);
        }
    }
    
    this.expand(curNode, id, posX, posY);
    
    if (this.showTextInfo) {
        oThis.createNodeDetailLayer(treeNode, curNode);
    }
    
    if (type != "3" && type != "4" && type != "5") {
        if (colorDir == "1") {
            this.refreshNodePercentImg(treeNode);
        }
        else {
            this.refreshNodePercentImgDeorder(treeNode);
        }
    }
    else 
        if (type == "4") {
            this.blockDefPos(treeNode, curNode);
        }
        else 
            if (type == "5") {
                this.addTextEvent(treeNode, curNode, percentDiv);
            }
}

VerticalTree.prototype.expand = function(curNode, id, posX, posY){
    var oThis = this;
    if (curNode.getAttribute("_hasChildNodes") == "true") {
        var imgDiv = this.$C("div", {
            className: "expandDiv",
            nodeID: id
        });
        var imgSrc = "";
        var opened = curNode.getAttribute("_opened");
        if (opened == "false") {
            imgSrc = oThis.imageURL + "expand.gif";
        }
        else {
            imgSrc = oThis.imageURL + "contract.gif";
        }
        imgDiv.innerHTML = "<img src=\"" + imgSrc + "\">";
        with (imgDiv.style) {
            left = parseInt(posX) + parseInt(this.nodeOffsetWidth) - 13 + "px";
            top = parseInt(posY) + parseInt(this.nodeOffsetHeight / 2) - 13 / 2 + "px";
        }
        this.$A(imgDiv, this.container);
        imgDiv.onclick = function(e){
            customPublic.showWaitingLayer();
            var tarNode = oThis.data.selectSingleNode(".//TreeNode[@id='" + this.nodeID + "']");
            var isOpen = tarNode.getAttribute("_opened");
            if (isOpen == "false") {
                tarNode.setAttribute("_opened", "true");
                this.firstChild.src = oThis.imageURL + "contract.gif";
                var treeNode = oThis.bakData.selectSingleNode(".//TreeNode[@id='" + this.nodeID + "']").cloneNode(true);
                oThis.addChildNodes(tarNode, treeNode);
                oThis.onExpand(tarNode, e);
            }
            else {
                tarNode.setAttribute("_opened", "false");
                this.firstChild.src = oThis.imageURL + "expand.gif";
                oThis.onCollapse(tarNode, e);
            }
            var evt = e ? e : event;
            oThis.rebuildTree();
            oThis.adjustScrollBar(this.nodeID, evt.clientY);
            customPublic.hideWaitingLayer();
        }
    }
}

VerticalTree.prototype.addChildNodes = function(pNode, curNode){
    var treeNodes = curNode.selectNodes("./TreeNode");
    var tLen = treeNodes.length;
    for (var i = 0; i < tLen; i++) {
        pNode.appendChild(treeNodes[i]);
        this.addChildNodes(treeNodes[i], treeNodes[i]);
    }
}

VerticalTree.prototype.buildLine = function(){
    var lineDiv = this.$C("div");
    this.$A(lineDiv, this.container);
    lineDiv.className="psline";
    var str = [];
    
    for (var i = 1; i < this.maxDepth + 1; i++) {
        var nodes = this.data.selectNodes(".//TreeNode[@_depth='" + i + "']");
        var nLen = nodes.length;
        for (var j = 0; j < nLen; j++) {
            var curNode = nodes[j];
            var posX = parseInt(parseFloat(curNode.getAttribute("_posX")) + this.nodeOffsetWidth);
            var posY = parseInt(parseFloat(curNode.getAttribute("_posY")) + parseFloat(this.nodeOffsetHeight / 2));
            var posX_E1 = parseInt(posX + parseFloat((this.nodeWidth - this.nodeOffsetWidth) / 2));
            var posY_E1 = parseInt(posY);
            
            if (curNode.childNodes.length != 0) {
                str[str.length] = "<v:Line from='" + posX + "," + posY + "' to='" + posX_E1 + "," + posY_E1 + "' strokecolor='" + this.lineColor + "'><v:Stroke dashstyle='" + this.lineStyle + "' /></v:Line>";
                
                var childNodes = curNode.childNodes;
                var cLen = childNodes.length;
                for (var k = 0; k < cLen; k++) {
                    var cNode = childNodes[k];
                    var cPosX = parseInt(parseFloat(cNode.getAttribute("_posX")) - parseFloat((this.nodeWidth - this.nodeOffsetWidth) / 2));
                    var cPosY = parseInt(parseFloat(cNode.getAttribute("_posY")) + parseFloat(this.nodeOffsetHeight / 2));
                    
                    var cPosX_E = parseInt(cPosX + parseFloat((this.nodeWidth - this.nodeOffsetWidth) / 2));
                    var cPosY_E = cPosY;
                    str[str.length] = "<v:Line from='" + posX_E1 + "," + posY_E1 + "' to='" + cPosX + "," + cPosY + "' strokecolor='" + this.lineColor + "'><v:Stroke dashstyle='" + this.lineStyle + "' /></v:Line>";
                    str[str.length] = "<v:Line from='" + cPosX + "," + cPosY + "' to='" + cPosX_E + "," + cPosY_E + "' strokecolor='" + this.lineColor + "'><v:Stroke dashstyle='" + this.lineStyle + "' /></v:Line>";
                }
            }
        }
    }
    lineDiv.innerHTML = str.join("\r\n");
}

VerticalTree.prototype.rebuildTree = function(){
    this.container.innerHTML = "";
    this.removeAttributes();
    this.removeUnExpandNode();
    this.setDepth(this.data, 0);
    this.setPosX();
    this.setPosY();
    this.buildTree();
    this.buildLine();
}

VerticalTree.prototype.removeAttributes = function(){
    var nodes = this.data.selectNodes(".//TreeNode");
    var nLen = nodes.length;
    for (var i = 0; i < nLen; i++) {
        var curNode = nodes[i];
        curNode.removeAttribute("_depth");
        curNode.removeAttribute("_posX");
        curNode.removeAttribute("_posY");
        curNode.removeAttribute("_height");
        curNode.removeAttribute("_order");
    }
}

VerticalTree.prototype.adjustScrollBar = function(id, Y){
    var node = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");
    var newY = Math.floor(node.getAttribute("_posY"));
    var depth = Math.floor(node.getAttribute("_depth"));
    var marge = newY - Y;
    var scrollTop = this.containerRect.top + marge + (this.maxDepth - depth) * 20;
    this.container.scrollTop = scrollTop;
}

VerticalTree.prototype.getTreeData = function(){
    return this.data;
}

VerticalTree.prototype.getTreeHTML = function(){
    return this.container.innerHTML;
}

VerticalTree.prototype.editNodeName = function(obj){
    var oThis = this;
    if (obj.firstChild.nodeName.toUpperCase() != "INPUT") {
    	//去掉点击修改文件功能
        //obj.setAttribute("showText", obj.innerText);
        //obj.innerHTML = "<input type=\"input\" onselect=\"\" onselectstart=\"\" value=\"" + obj.getAttribute("showText") + "\" class=\"nodeDesInput\" />";
        obj.firstChild.focus();
        var ipt = obj.getElementsByTagName("input")[0];
        if (null != ipt) {
            ipt.onblur = function(){
                oThis.refreshNodeName(this);
            }
        }
    }
}

VerticalTree.prototype.refreshNodeName = function(obj){
    var div = obj.parentNode;
    div.setAttribute("showText", obj.value);
    div.innerHTML = "<nobr>" + obj.value + "</nobr>";
}

VerticalTree.prototype.createNodeDetailLayer = function(obj, curNode){
    var oThis = this;
    obj.onmousemove = function(e){
        var evt = e ? e : event;
        var eX = evt.clientX;
        var eY = evt.clientY;
        oThis.showDetailTextLayer(this.firstChild, curNode, eX, eY);
    }
    obj.onmouseout = function(){
        oThis.hideDetailTextLayer();
    };
}

VerticalTree.prototype.showDetailTextLayer = function(textObj, curNode, eX, eY){
    var layer = this.customGetById("detailTextLayer");
    if (null == layer) {
        layer = this.$C("div", {
            id: "detailTextLayer"
        });
        this.$A(layer);
    }
    if (textObj.firstChild.nodeName.toUpperCase() == "INPUT") {
        this.hideDetailTextLayer();
        return;
    }
    
    var html = this.rendererNode(curNode.getAttribute(this.defaultFieldName), textObj, curNode);
    layer.innerHTML = textObj.nextSibling == null ? textObj.innerText : textObj.innerText + html;
    layer.style.display = "";
    var lW = layer.offsetWidth;
    var lH = layer.offsetHeight;
    layer.style.left = (eX + 10 + lW > this.containerRect.left + this.container.offsetWidth ? eX - 5 - lW : eX + 10) + "px";
    layer.style.top = (eY + 10 + lH > this.containerRect.top + this.container.offsetHeight ? eY - lH : eY + 10) + "px";
}

VerticalTree.prototype.hideDetailTextLayer = function(){
    var layer = this.customGetById("detailTextLayer");
    layer.style.display = "none";
}

VerticalTree.prototype.refreshNodePercentImg = function(obj){
    var nodeType = obj.firstChild.nextSibling.getAttribute("type");
    var maxValue = obj.firstChild.nextSibling.getAttribute("maxValue");
    var minValue = obj.firstChild.nextSibling.getAttribute("minValue");
	var nodeValue = nodeType == 2 ? obj.firstChild.nextSibling.innerText:obj.firstChild.nextSibling.lastChild.innerHTML;
    nodeValue = nodeType == 1 ? nodeValue : nodeValue.substring(0, nodeValue.indexOf("%"));
    nodeValue = nodeType == 1 ? parseFloat(nodeValue / (maxValue - minValue)) : parseFloat(parseFloat(nodeValue) / 100);
    var nodeShow = obj.lastChild.firstChild;
    var maxWidth = parseFloat(nodeShow.offsetWidth);
    var xValue = 0 - (100 - maxWidth * nodeValue);
    xValue = xValue > 0 ? 0 : xValue;
    var posX = xValue + "px";
    var posY = this.getPosY(nodeValue);
    var posStr = String(posX) + " " + String(posY);
    nodeShow.style.backgroundPosition = posStr;
}

VerticalTree.prototype.refreshNodePercentImgDeorder = function(obj){
    var nodeType = obj.firstChild.nextSibling.getAttribute("type");
    var maxValue = obj.firstChild.nextSibling.getAttribute("maxValue");
    var minValue = obj.firstChild.nextSibling.getAttribute("minValue");
    
    var nodeValue = nodeType == 2 ? obj.firstChild.nextSibling.innerText:obj.firstChild.nextSibling.lastChild.innerHTML;
    nodeValue = nodeType == 1 ? nodeValue : nodeValue.substring(0, nodeValue.indexOf("%"));
    nodeValue = nodeType == 1 ? parseFloat(nodeValue / (maxValue - minValue)) : parseFloat(parseFloat(nodeValue) / 100);
    var nodeShow = obj.lastChild.firstChild;
    
    var maxWidth = parseFloat(nodeShow.offsetWidth);
    var xValue = 0 - (100 - maxWidth * nodeValue);
    xValue = xValue > 0 ? 0 : xValue;
    var posX = xValue + "px";
    var posY = this.getPosYDeorder(nodeValue);
    
    var posStr = String(posX) + " " + String(posY);
    nodeShow.style.backgroundPosition = posStr;
}

VerticalTree.prototype.getPosY = function(nodeValue){
    var posY = "0px";
    if (nodeValue <= 0.25) {
        posY = "0px";
    }
    else 
        if (nodeValue > 0.25 && nodeValue <= 0.5) {
            posY = "-7px";
        }
        else 
            if (nodeValue > 0.5 && nodeValue <= 0.75) {
                posY = "-14px";
            }
            else 
                if (nodeValue > 0.75) {
                    posY = "-21px";
                }
    return posY;
}

VerticalTree.prototype.getPosYDeorder = function(nodeValue){
    var posY = "0px";
    if (nodeValue <= 0.25) {
        posY = "-21px";
    }
    else 
        if (nodeValue > 0.25 && nodeValue <= 0.5) {
            posY = "-14px";
        }
        else 
            if (nodeValue > 0.5 && nodeValue <= 0.75) {
                posY = "-7px";
            }
            else 
                if (nodeValue > 0.75) {
                    posY = "0px";
                }
    return posY;
}

VerticalTree.prototype.blockDefPos = function(obj, curNode){
    var infoObj = obj.firstChild.nextSibling;
    var block = infoObj.nextSibling.firstChild.firstChild;
    var maxValue = infoObj.getAttribute("maxValue");
    var minValue = infoObj.getAttribute("minValue");
    var valueType = infoObj.getAttribute("type");
    var defValue = infoObj.lastChild.innerHTML;
    var posX;
    if (valueType == 1) {
        posX = 100 * (defValue / (maxValue - minValue));
    }
    else {
        posX = 100 * (parseFloat(defValue) / (parseFloat(maxValue) - parseFloat(minValue)));
    }
    with (block.style) {
        position = "relative";
        left = posX + "px";
        top = "1px";
        zIndex = "1000";
    }
    var id = curNode.getAttribute("id");
    this.onUpdateValue(obj, curNode, id, defValue);
}

VerticalTree.prototype.signMoveEvent = function(obj){
    obj.__isDrag = "true";
    var block = obj.getElementsByTagName("div")[0];
    block.setCapture();
    obj.rect = this.boundingRect(obj);
    this.onTreeNodeMoveStart(obj);
}

VerticalTree.prototype.judgePointerPos = function(curNode, obj, eventObj){
    var __tmpValue = null;
    var infoDiv = obj.parentNode.parentNode.firstChild.nextSibling;
    var maxValue = infoDiv.getAttribute("maxValue");
    var minValue = infoDiv.getAttribute("minValue");
    var valueType = infoDiv.getAttribute("type");
    var valueTypeBlock = infoDiv.getAttribute("blockValueType");
    var oThis = obj.firstChild;
    if (null == eventObj) {
        eventObj = window.event;
    };
    
    var curX = event.clientX;
    var curY = event.clientY;
    if (obj.__isDrag == "true") {
        var blockX = curX - obj.rect.left;
        if (blockX < 0) {
            blockX = 0;
        };
        if (blockX > 100) {
            blockX = 100;
        };
        with (oThis.style) {
            position = "relative";
            left = blockX - 5 + "px";
            top = "1px";
            zIndex = "1000";
        }
        //__tmpValue = valueType == 1 ? parseInt((blockX/100) * (maxValue - minValue)) : (valueTypeBlock == "1" && valueType == "4" ) ? parseInt((blockX/100) * (maxValue - minValue)) : parseInt((blockX/100) * (parseFloat(maxValue) - parseFloat(minValue))) + "%";
        var refreshValue = null;
        if (valueType == 1) {
            refreshValue = parseInt((blockX / 100) * (maxValue - minValue));
            __tmpValue = refreshValue;
        }
        else 
            if (valueTypeBlock == "1" && valueType == "4") {
                refreshValue = parseInt((blockX / 100) * (maxValue - minValue));
                __tmpValue = refreshValue;
            }
            else {
                refreshValue = parseInt((blockX / 100) * (parseFloat(maxValue) - parseFloat(minValue)));
                __tmpValue = refreshValue + "%";
            }
        
        infoDiv.lastChild.innerHTML = __tmpValue;
        obj.__tmpValue = __tmpValue;
        this.onTreeNodeMove(obj);
        
        var node = obj.parentNode.parentNode;
        var treeId = node.getAttribute("id");
        var id = treeId.substring(treeId.indexOf("_") + 1);
        //this.refreshXMLValue(id, this.defaultFieldName, refreshValue);
        this.onUpdateValue(obj, curNode, id, refreshValue);
    }
}

VerticalTree.prototype.stopPointerMove = function(obj){
    var oThis = this;
    if (obj.__isDrag == "true") {
        obj.__isDrag = "false";
        if (obj.__tmpValue == null) {
            return;
        };
        var block = obj.getElementsByTagName("div")[0];
        block.releaseCapture();
        try {
            oThis.hideDetailTextLayer();
        } 
        catch (e) {
        }
        this.onTreeNodeMoveEnd(obj);
    }
}

VerticalTree.prototype.boundingRect = function(elm){
    var absTop = 0;
    var absLeft = 0;
    var tempObj = elm;
    while (tempObj != document.body) {
        absTop += tempObj.offsetTop - tempObj.offsetParent.scrollTop;
        absLeft += tempObj.offsetLeft - tempObj.offsetParent.scrollLeft;
        tempObj = tempObj.offsetParent;
    }
    return {
        top: absTop,
        left: absLeft
    };
}

VerticalTree.prototype.refreshXMLValue = function(id, attribute, value){
    var node = this.data.selectSingleNode(".//TreeNode[@id='" + id + "']");
    var nodeBak = this.bakData.selectSingleNode(".//TreeNode[@id='" + id + "']");
    node.setAttribute(attribute, value);
    if (null != nodeBak) {
        nodeBak.setAttribute(attribute, value);
    }
}

VerticalTree.prototype.addTextEvent = function(treeNode, curNode, elm){
    var ipt = this.$T("input", elm)[0], oThis = this;
    oThis.setNodeValue(treeNode, curNode, ipt);
    ipt.onkeyup = ipt.onpaste = function(e){
        if (event.keyCode == 13 || event.keyCode == 10) {
            oThis.setNodeValue(treeNode, curNode, ipt);
            var evt = e ? e : event;
            var eX = evt.clientX;
            var eY = evt.clientY;
            ipt.value = oThis.round(ipt.value);
            oThis.showDetailTextLayer(treeNode.firstChild, curNode, eX, eY);
        }
    }
    ipt.onblur = function(){
        oThis.setNodeValue(treeNode, curNode, ipt);
    }
}

VerticalTree.prototype.setNodeValue = function(treeNode, curNode, ipt){
    if (!ipt.value) {
        ipt.value = curNode.getAttribute(this.defaultFieldName);
    }
    if (!/(^\-{0,1}\d+\.{0,1}\d+$)|(^\-{0,1}\d+$)/.test(ipt.value)) {
        var o = ipt.value.match(/(\-{0,1}\d+\.{0,1}\d+)|(\-{0,1}\d+)/);
        var lValue = o ? o[0] : "";
        ipt.value = lValue;
        return;
    }
    if (ipt.value != "") 
        this.updateValue(treeNode, curNode, ipt.value);
}

VerticalTree.prototype.updateValue = function(treeNode, curNode, v){
    var value = this.round(v);
    var id = treeNode.getAttribute("dataID");
    //this.refreshXMLValue(id, this.defaultFieldName, value);
    var div = this.$T("div", treeNode)[1];
    var beforeValue = this.onBeforeUpdateValue(treeNode, curNode, id, value);
    div.lastChild.innerHTML = beforeValue;
    this.onUpdateValue(treeNode, curNode, id, beforeValue);
}

VerticalTree.prototype.onExpand = function(node, e){

}

VerticalTree.prototype.onCollapse = function(node, e){

}

VerticalTree.prototype.onBeforeUpdateValue = function(el, node, id, value){
    return value;
}

VerticalTree.prototype.onUpdateValue = function(el, node, id, value){
}

VerticalTree.prototype.onTreeNodeMoveStart = function(obj){

};

VerticalTree.prototype.onTreeNodeMove = function(obj){

};

VerticalTree.prototype.onTreeNodeMoveEnd = function(obj){

};

VerticalTree.prototype.rendererNode = function(value, textObj, curNode){
    return ["<br/> [ ", value, " ] "].join('');
}
