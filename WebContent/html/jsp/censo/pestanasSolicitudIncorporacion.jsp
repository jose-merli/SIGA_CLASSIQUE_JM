<!DOCTYPE html>
<html>
<head>
<!-- pestanasSolicitudIncorporacion.jsp -->
<!-- CABECERA JSP -->
<%@page import="java.util.ArrayList"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
 
<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	String param = request.getParameter("tablaDatosDinamicosD");
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<html:form action="/CEN_SolicitudesIncorporacion" method="POST">

	<html:hidden property="modo" />
	<siga:PestanasExt 
			pestanaId="SOLINC" 
			target="mainPestanas"
			parametros="SOLINC"
			elementoactivo="1"
	/>

</html:form>

	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas"
			>
	</iframe>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>


</body>
</html>
