<!-- mediadoresExportFicheroPestanas.jsp -->
<!DOCTYPE html>
<html>
<head>
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
 
<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	int elementoActivo = 1;
%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	

</head>
 
<body onload="ajusteAlto('subPestanas');return activarPestana();">

<div class="posicionPestanas">

	
		<siga:PestanasExt 
				pestanaId="MED_FICHER" 
				target="subPestanas"
				parametros="exportFicheroParam"
				elementoactivo="<%=elementoActivo%>"
	/>
	


</div>
	
	
	<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="subPestanas"
			name="subPestanas"
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0";
			class="framePestanas"
			>
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->



</body>
</html>
