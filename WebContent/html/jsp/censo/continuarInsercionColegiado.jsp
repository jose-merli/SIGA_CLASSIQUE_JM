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
<%@ page import="com.atos.utils.*"%>

<html:html>
<head>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");	
	String mensaje=(String)request.getAttribute("msj");
	
	

%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">
		function reloadPage() {
		    var type = '<%=mensaje%>';
			if (confirm(type)) {
			    parent.document.forms[0].continuarInsercionColegiado.value="1";
				parent.document.forms[0].submit();
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST" target="_self">
			<html:hidden property = "modo" value = "Modificar"/>
			<html:hidden property = "continuarInsercionColegiado" value = ""/>
		
			
			
			
		</html:form>

</body>
</html:html>