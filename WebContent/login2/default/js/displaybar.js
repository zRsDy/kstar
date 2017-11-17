/* frameset */

var displayBar=true;
function switchBar(obj) {
	if (displayBar) {
		//window.top.mainFrame.cols='0,*';
		parent.document.getElementsByTagName("frameset")[1].cols = "0,*";//getElementsByTagName("frameset")[0]是第一个 frameset，getElementsByTagName("frameset")[1]是第二个frameset
		displayBar=false;
		obj.src='../images/displaybar_open.png';
		obj.title='show menu';
	} else {
		//window.top.mainFrame.cols='198,*';
		parent.document.getElementsByTagName("frameset")[1].cols = "198,*";
		displayBar=true;
		obj.src='../images/displaybar_close.png';
		obj.title='hide menu';
	}
	//resizeWindow(); 	
}
