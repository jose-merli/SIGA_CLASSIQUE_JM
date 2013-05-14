<!-- genericLoadForm.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String messageName = (String)request.getAttribute("descOperation");
	String urlAction = (String)request.getAttribute("urlAction");
	// JZB 23/12/2004 Se ha anhadido un comportamiento específica para las modales
	String modal= (String)request.getAttribute("modal");
	String retorno= (String) request.getAttribute("retorno"); //Para devolver un valor a la ventana principal
%>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		
<script type="text/javascript">
function reloadPage() {
	var type = '<siga:Idioma key="<%=messageName%>"/>';
	alert(type);   
<%  if (modal!=null&&modal.equals("1")){%>
	<%  if (retorno!=null){%>  window.top.returnValue="<%=retorno%>"; <%}%>	
	window.top.close();
<%  }else{%>	
	//parent.parent.frames[0].location='<%=urlAction%>';
	//parent.parent.frames[1].location='<%=app%>/jsp/general/blank.jsp';
	//parent.parent.buscar();
	top.mainWorkArea.buscar();
<%  };%>
	return false;

}
</script>
</head>
<body onload="reloadPage()"></body>
</html:html>
