__Tree = null;
__WAITING_IMG_PATH = contextPath+'/RES/default/js/decisiontree/resources/images/';

function init(){
    GetData();
    checkTreeDivSize();
}

function GetData(){
    var p = new HttpRequestParams();
    //p.url = "/irmp-web/RES/default/js/decisiontree/graphicData.xml";
    p.url = contextPath+"/kpi/motivation/"+organCode+"/"+indicatorCode+".htm";
    var request = new HttpRequest(p);
    request.onresult = function(){
        var treeData = this.getNodeValue("Data");
        BuildTree(treeData);
    };
    request.send();
}

function BuildTree(data){
    var tree;
    if (!__Tree) {
        tree = new VerticalTree();
        tree.data = data;
        tree.container = customGetById("treeOutDiv");
        tree.nodeWidth = 150;
        tree.nodeHeight = 50;
        tree.showTextInfo = true;
        //tree.rendererNode = rendererNode;
		tree.onUpdateValue = onUpdateValue;
        tree.init();
        __Tree = tree;
    }
    else {
        tree = __Tree;
		tree.data = data;
		tree.rebuildTree();
    }
    
}

function checkTreeDivSize(){
	   
	    var div = customGetById("treeOutDiv");
	    var oWidth = document.body.offsetWidth - 19;
	    var oHeight = document.body.offsetHeight;
	    if (null != div) {
	        div.style.width = oWidth + "px";
	        div.style.height = oHeight -170 + "px";
	    }
}

function onUpdateValue(el, node, id, value){
	
	
}


window.onload = init;
window.onresize = checkTreeDivSize;
