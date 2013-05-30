<!-- exitoDevolucionManual.jsp -->
<!-- CABECERA JSP -->
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
<%@ page import="com.siga.informes.form.MantenimientoInformesForm "%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String generacionOK = (String)request.getAttribute("generacionOK");
	String rutaFichero = (String)request.getAttribute("rutaFichero");
	String borrarFichero = (String)request.getAttribute("borrarFichero");
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script>
	function init(){
		alert('<siga:Idioma key="facturacion.pagoContabilidad.literal.operacionExito"/>');
		document.forms[0].modo.value="descargar";
		//document.forms[0].submit();
		<%if(generacionOK!=null){%>
			window.top.returnValue="MODIFICADO"; 
			window.top.close();
		<%}%>
	}
	</script>

</head>


<body onload="init();">
	<html:form action="/INF_FichaPago.do" method="POST" target="submitArea">
	<html:hidden name="mantenimientoInformesForm" property = "modo" value = "download"/>
	<html:hidden name="mantenimientoInformesForm" property = "rutaFichero" value = "<%=rutaFichero%>"/>
	<%if(borrarFichero!=null){%>
		<html:hidden property = "borrarFichero" value = "<%=borrarFichero%>"/>
	<%}%>
	</html:form>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>