<!-- loadingWindow.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>
<html>
<head>
<% String app = request.getContextPath();
  // String msg="messages.wait";
  String msg="messages.loadingpage";   
%>
<title><siga:Idioma key="<%=msg%>"/></title>
<% if (request.getParameter("msg") != null) {
        msg=request.getParameter("msg");
   }%>
<link rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
</head>
<body style="cursor:wait" onBlur="window.focus();">
<table align="center" border="0" height="100%" width="100%">
<tr><td align="center" class="labelText"><siga:Idioma key="<%=msg%>"/></td></tr>
<tr><td align="center"><img src="<%=app%>/html/imagenes/loading.gif"></td></tr>
</table>
</body>
</html>