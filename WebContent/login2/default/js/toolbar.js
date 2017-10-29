    function getTabLabelHtml(index)
    {
      var sHtml="";
      sHtml=sHtml + "<TABLE width='100%' border=0 cellspacing=0 cellpadding=0>";
      sHtml=sHtml + "<TR height=21>";
      sHtml=sHtml + "<TD width=* background='../UICommon/Image/Main/BG.gif'>";
      sHtml=sHtml + "<TABLE border=0 cellspacing=0 cellpadding=0>";
      sHtml=sHtml + "<TR height=21>";
      for (var i=0;i<TabPageNumber;i++)
      {
        if (i==index)
        {
        sHtml=sHtml + "<TD width=21><IMG src='../UICommon/Image/Main/BUTTONLF.gif'></TD>";
        sHtml=sHtml + "<TD background='../UICommon/Image/Main/BUTTONBG.gif' nowrap align=middle valign=center>";
        sHtml=sHtml + "<font face='Courier New, Courier, mono' style=' FONT-SIZE:8pt' color='#ffffff'>";
      //  sHtml=sHtml + eval("TabCaption" + i);
        sHtml=sHtml + "</font>";
        sHtml=sHtml + "</TD>";
        sHtml=sHtml + "<TD width=19><IMG src='../UICommon/Image/Main/BUTTONRT.gif'></TD>";
      }
      else
      {
        sHtml=sHtml + "<TD width=18><IMG src='../UICommon/Image/Main/BUT/downbutlf.gif' style='CURSOR: hand;' onclick='changeTab(" + i + ")'></TD>";
        sHtml=sHtml + "<TD background='../UICommon/Image/Main/BUT/downbutbg.gif' nowrap align=middle valign=center>";
        sHtml=sHtml + "<font style='CURSOR: hand; FONT-SIZE:8pt' face='Courier New, Courier, mono'  color='#cccccc' onclick='changeTab(" + i + ")'>";
       // sHtml=sHtml + eval("TabCaption" + i);
        sHtml=sHtml + "</font>";
        sHtml=sHtml + "</TD>";
        sHtml=sHtml + "<TD width=17><IMG src='../UICommon/Image/Main/BUT/downbutrt.gif' style='CURSOR: hand;' onclick='changeTab(" + i + ")'></TD>";
      }
      }
      sHtml=sHtml + "</TR>";
      sHtml=sHtml + "</TABLE>";
      sHtml=sHtml + "</TD>";
      sHtml=sHtml + "<TD width=42><IMG src='../UICommon/Image/Main/rt.gif'></TD>";
      sHtml=sHtml + "</TR>";
      sHtml=sHtml + "</TABLE>";
      return sHtml;
    }
//-----------------------------------------------------------------------------
//variables and functions for layer
//-----------------------------------------------------------------------------
    isStdBrowser=(document.getElementById)?true:false;
    isIE4=(document.all)?true:false;
    isNS4=(document.layers)?true:false;

    function showObject1(object)
    {
      if (isIE4) object.style.visibility="visible";
      if (isNS4) object.visibility="show";
    }

    function hideObject1(object)
    {
      if (isIE4) object.style.visibility="hidden";
      if (isNS4) object.visibility="hide";
    }

    function getLeft(object)
    {
      return object.offsetLeft;
    }

    function setLeft(object,value)
    {
      if (isStdBrowser)
      {
        object.style.left=value + "px";
      }
      else
      {
        object.left=value;
      }
    }

    function getTop(object)
    {
      return object.offsetTop;
    }

    function setTop(object,value)
    {
      if (isStdBrowser)
      {
        object.style.top=value + "px";
      }
      else
      {
        object.top=value;
      }
    }

    function getWidth(object)
    {
      return object.offsetWidth;
    }

    function setWidth(object,value)
    {
      if (isStdBrowser)
      {
        object.style.width=value + "px";
      }
      else
      {
        object.width=value;
      }
    }

    function getHeight(object)
    {
      return object.offsetHeight;
    }

    function setHeight(object,value)
    {
      if (isStdBrowser)
      {
        object.style.height=value + "px";
      }
      else
      {
        object.height=value;
      }
    }

    function resizeObject(object,left,top,width,height)
    {
      setLeft(object,left);
      setTop(object,top);
      setWidth(object,width);
      setHeight(object,height);
    }

    function getRight(object)
    {
      return getLeft(object) + getWidth(object) - 1;
    }

    function getBottom(object)
    {
      return getTop(object) + getHeight(object) - 1;
    }

    function getClientWidth()
    {
      if (document.all)
      {
        return document.body.clientWidth;
      }
      else
      {
        return window.innerWidth;
      }
    }

    function getClientHeight()
    {
      if (document.all)
      {
        return document.body.clientHeight;
      }
      else
      {
        return window.innerHeight;
      }
    }

//-----------------------------------------------------------------------------
//variables and functions for tab UI
//-----------------------------------------------------------------------------
    var TabIndex=0;
    var TabPageNumber=0;
    var objTab;
    var objTabLabel;
    var objTabPanel;

    function tabPanelArray()
    {
      this.length=TabPageNumber;
    }

    function changeTab(index)
    {
     // objTabLabel.innerHTML=getTabLabelHtml(index);
      for (var i=0;i<TabPageNumber;i++)
      {
        if (i==index)
        {
        showObject1(objTabPanel[i]);
      }
      else
      {
        hideObject1(objTabPanel[i]);
      }
      }
      changeToolbar(index)
      TabIndex=index;
    }
    function changeToolbar(index)
    {
      for (var i=0;i<toolBarCount;i++)
      {
        if (i==index)
        {
        showObject1(document.getElementById("Toolbar"+i));
      }
      else
      {
        hideObject1(document.getElementById("Toolbar"+i));
      }
      }
      ToolbarIndex=index;          

    }

    function initTab(PageNumber)
    {
      var i;
      TabPageNumber=PageNumber
      objTabPanel=new tabPanelArray();
      if (isStdBrowser)
      {
        objTab=document.getElementById("Tab");
        objTabLabel=document.getElementById("TabLabel");
      }
      else
      {
        objTab=document.Tab;
        objTabLabel=document.Tab.TabLabel;
      }
      for (i=0;i<TabPageNumber;i++)
      {
        if (isStdBrowser)
        {
        objTabPanel[i]=document.getElementById("TabPanel" + i);
      }
      else
      {
        objTabPanel[i]=eval("document.Tab.TabPanel" + i);
      }
      }
      //changeToolbar(PageNumber);
    }

    function resizeTab(left,top,width,height)
    {
      resizeObject(objTab,left,top,width,height);       

      resizeObject(objTabLabel,0,0,width,getHeight(objTabLabel));
      for (var i=0;i<TabPageNumber;i++)
      {
        resizeObject(objTabPanel[i],0,getBottom(objTabLabel)+1,width ,height-getBottom(objTabLabel));
      }
    }

//-----------------------------------------------------------------------------
//variables and functions for toolbar and tab UI
//-----------------------------------------------------------------------------
    function resizeWindow_SingleToolbar()
    {
      var objToolbar;
      var ToolbarHeight=28;
      var targetWidth=200;
      var targetHeight=150;
      if (getClientWidth()>targetWidth)
      {
        targetWidth=getClientWidth();
      }
      if (getClientHeight()>targetHeight)
      {
        targetHeight=getClientHeight();
      }
      if (isStdBrowser)
      {
        objToolbar=document.getElementById("Toolbar");
      }
      else
      {
        objToolbar=document.Toolbar;
      }
      if (objTab) resizeTab(0,0,targetWidth,targetHeight-getHeight(objToolbar));
      resizeObject(objToolbar,0,targetHeight-getHeight(objToolbar),targetWidth,getHeight(objToolbar));
    }
    function resizeWindow_MultiToolbar()
    {
      //  alert(toolBarCount);
      //    var toolBarCount=1;
      var objToolbar;
      var ToolbarHeight=28;
      var targetWidth=200;
      var targetHeight=150;
      if (getClientWidth()>targetWidth)
      {
        targetWidth=getClientWidth();
      }
      if (getClientHeight()>targetHeight)
      {
        targetHeight=getClientHeight();
      }
      if (objTab)
        resizeTab(0,0,targetWidth,targetHeight);

      for (i=0;i<toolBarCount;i++){

        if (isStdBrowser)
        {
        objToolbar=document.getElementById('Toolbar'+i);
      }
      else
      {
        objToolbar=document.Toolbar;
      }
      resizeObject(objToolbar,0,getHeight(document.getElementById("TabPanel"+i ))-getHeight(objToolbar),targetWidth,getHeight(objToolbar));
      }
    }
    function resizeWindow()
    {
      //  alert(toolBarCount);
      //    var toolBarCount=1;
      var objToolbar;
      var ToolbarHeight=28;
      var targetWidth=20;//200;
      var targetHeight=20;//150;
      if (getClientWidth()>targetWidth)
      {
        targetWidth=getClientWidth();
      }
      if (getClientHeight()>targetHeight)
      {
        targetHeight=getClientHeight();
      }
      if (objTab) resizeTab(0,0,targetWidth,targetHeight-0);

      for (i=0;i<toolBarCount;i++){
        if (isStdBrowser)
        {
         objToolbar=document.getElementById('Toolbar'+i);
        }
        else
        {
        objToolbar=document.Toolbar;
        }
      resizeObject(objToolbar,0,targetHeight-getHeight(objToolbar),targetWidth,getHeight(objToolbar));
      }
    }
    function writetoggle_xsl(i){
      //alert(i);
      document.write('<A  href="javascript:toggle_xsl(div_'+i+',pic_'+i+');"><span style="FONT-SIZE: 10pt; COLOR: aliceblue; color:red" id="pic_'+i+'">-</span></A>');
    }
    function toggle_xsl(layerID, ImageBt){
      //resourceBox = new Cookie(layerID.id);
      //resourceBox.path = "/";
      var collapse = /collapse*/g;
      if(layerID.style.display != "none"){
        layerID.style.display = "none";
        ImageBt.innerText='+';
      }
      else{
        layerID.style.display = "block";
        ImageBt.innerText='-';
      }
      }
      function verify(c,vc){
        if (verify.arguments.length < 2) return;
        var notNullMsg="this field can't be empty!";
        var notNumberMsg="this field must be number value!";
        var errorHtml="<span class='xslError'>r</span>";
        var space="&nbsp;";
        switch (vc.innerText){
          case 'R':{
            if (isWhitespace(c.innerText)){
              c.innerHTML+=errorHtml;
            }
            break;
          }
        case 'D':{
          if (!isFloat(c.innerText)){
            c.innerHTML+=errorHtml;
          }
          break;
        }case 'O':{
          c.innerHTML+=space;
        }
      default:{
      }
        }
      }
      function verify2(c1,c2,vc){
        if (verify2.arguments.length < 3) return;
        var errorHtml="<span class='xslError'>r</span>";
        var space="&nbsp;";
        switch (vc.innerText){
          case 'R':{
            if (isWhitespace(c1.innerText) && isWhitespace(c2.innerText)){
              c1.innerHTML+=errorHtml;
            }else{
              c1.innerHTML+=space;
            }
            break;
          }
        case 'D':{
          if (!isFloat(c1.innerText) && isWhitespace(c2.innerText)){
            c1.innerHTML+=errorHtml;
          }else {
            c1.innerHTML+=space;
          }
          break;
        }
        case 'O':{
          c1.innerHTML+=space;
          break;
        }
        default:{
        }
        }
      }
      function changeOverClass_Click(object)
            {
              object.className="ClickableContent";
      }
      function changeOverClass(object)
      {
        object.className="SelectedContent";
      }
      function changeOutClass(object)
      {
        object.className="Content";
      }

