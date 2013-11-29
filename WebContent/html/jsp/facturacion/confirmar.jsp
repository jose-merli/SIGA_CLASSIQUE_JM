<!DOCTYPE html>
<html>
<head>
<!-- confirmar.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		 
	String mensaje = (String)request.getAttribute("mensaje");	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script language="JavaScript">	
		function reloadPage() {	
			if ('<%=mensaje%>'!=null){		
				if(confirm('<siga:Idioma key="<%=mensaje%>"/>')){
					document.forms[0].modo.value='reintentar';
					document.forms[0].submit();
				}				
			}	
			window.top.close();
		}
	</script>
</head>
<body onload="reloadPage()">
	<html:form action="/FAC_Devoluciones.do" method="POST" target="submitArea" >
		<html:hidden property="modo" value="anular"/>
	</html:form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>