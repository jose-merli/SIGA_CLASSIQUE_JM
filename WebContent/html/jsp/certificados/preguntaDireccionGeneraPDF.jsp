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
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String PREG_PDF_idsParaGenerarFicherosPDF = (String)request.getAttribute("PREG_PDF_idsParaGenerarFicherosPDF");
	String PREG_existe=(String)request.getAttribute("PREG_PDF_existe");


%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="messages.envios.noExistePersonaOrigen"/>';
			if (confirm(type)) {
				document.forms[0].submit();
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/CER_GestionSolicitudes.do" method="POST" target="_self">
			<html:hidden property = "modo" value = "generarPDF"/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<input type="hidden" name="idsTemp" value="">
			<input type="hidden" name="idPeticion" value="">
			<input type="hidden" name="idProducto" value="">
			<input type="hidden" name="idTipoProducto" value="">
			<input type="hidden" name="idProductoInstitucion" value="">
		
			<html:hidden property = "paraConsejo" value = "1"/>

			<html:hidden property = "idsParaGenerarFicherosPDF" value = "<%=PREG_PDF_idsParaGenerarFicherosPDF %>"/>
			<html:hidden property = "PREG_existe" value = "<%=PREG_existe %>"/>
			
		</html:form>

</body>
</html:html>