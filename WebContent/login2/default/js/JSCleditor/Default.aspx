<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
     <link rel="stylesheet" type="text/css" href="jquery.cleditor.css" /> 
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> 
    <script type="text/javascript" src="jquery.cleditor.min.js"></script> 
       <script type="text/javascript">
           $(document).ready(function() {
               $("#input").cleditor({ width: 500, height: 180, useCSS: true })[0].focus();
           });
    </script> 
   
</head>
<body>
    <form id="form1" runat="server">
    <div>
      <textarea id="input" name="input">51控件网 www.51asc.xom</textarea> 
    </div>
    </form>
</body>
</html>
