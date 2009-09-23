<!-- genericLoadForm2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ page import="com.siga.administracion.SIGAConstants,java.util.*"%>
<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String urlAction = (String)request.getAttribute("urlAction");	
%>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script type="text/jscript" language="JavaScript1.2">
function reloadPage() {
	//top.frames['MainWorkArea'].location='<%=urlAction%>';
	document.forms[0].target="mainWorkArea";	
	if(document.forms[0].modo.value=="Borrar" ) document.forms[0].modo.value="";
	document.forms[0].submit();
 };


</script>
</head>
<body onload="reloadPage()"></body>
<form id="enviar" name="enviar "method="<%=request.getMethod()%>" action="<%=urlAction%>">
<%
			Enumeration params=request.getParameterNames();
	        while(params.hasMoreElements()){
	           String param=(String)params.nextElement(); 
%> 
	          <input type="hidden" name="<%=param%>"  value="<%=request.getParameter(param)%>" />
	          
<%}
		

%>
			<input type="hidden" name="refresh" value=""/>
</form>

</html:html>
