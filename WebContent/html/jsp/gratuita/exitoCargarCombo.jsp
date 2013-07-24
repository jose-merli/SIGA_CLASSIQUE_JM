<!DOCTYPE html>
<html>
<head>
<!-- exitoCargarCombo.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
	
	Vector v = (Vector)request.getAttribute("resultado");
%>

	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script>
	function reloadPage()
	{
		var obj = parent.document.getElementById("comboDiaGuardia");
		// Borro los valores
		elemens = obj.options.length;
		for (var i=0; i<elemens; i++) { 
		  obj.removeChild(obj.firstChild); 
		} 
		parent.document.ncolegiado = new Array();
		// lo cargo
		var elem;
		<%
		for (int i=0;v!=null&&i<v.size();i++) {
			Hashtable ht= (Hashtable) v.get(i);
		%>
		  elem = new Option("<%=(String)ht.get("DESCRIPCION")%>","<%=(String)ht.get("ID")%>");
		  obj.options[<%=i%>] = elem;
		  parent.document.ncolegiado[<%=i%>] = '<%=(String)ht.get("NCOLEGIADO")%>'
		<% 
		}
		%>
		if (obj.options.length>0) {
			obj.options[0].selected=true;
			obj.onchange();
		}
	}
	</script>

</head>
<body onload="reloadPage()">
</body>
</html>