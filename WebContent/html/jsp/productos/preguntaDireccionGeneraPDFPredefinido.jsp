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
	String PREG_PDF_idInstitucion = (String)request.getAttribute("PREG_PDF_idInstitucion");
	String PREG_PDF_idInstitucionPresentador = (String)request.getAttribute("PREG_PDF_idInstitucionPresentador");
	String PREG_PDF_idPersona = (String)request.getAttribute("PREG_PDF_idPersona");
	String PREG_PDF_idBoton = (String)request.getAttribute("PREG_PDF_idBoton");
	String PREG_PDF_idPantilla = (String)request.getAttribute("PREG_PDF_idPantilla");
	String PREG_existe=(String)request.getAttribute("PREG_PDF_existe");

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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

		<html:form action="/PYS_CompraPredefinida.do" method="POST" target="_self">
			<html:hidden property = "modo" value = "insertar"/>
		
			<html:hidden property = "paraConsejo" value = "1"/>
	
			<html:hidden property = "idInstitucion" value = "<%=PREG_PDF_idInstitucion %>"/>
			<html:hidden property = "idInstitucionPresentador" value = "<%=PREG_PDF_idInstitucionPresentador %>"/>
			<html:hidden property = "idPersona" value = "<%=PREG_PDF_idPersona %>"/>
			<html:hidden property = "idBoton" value = "<%=PREG_PDF_idBoton %>"/>
			<html:hidden property = "idPlantilla" value = "<%=PREG_PDF_idPantilla %>"/>

			<html:hidden property = "PREG_existe" value = "<%=PREG_existe %>"/>
			
		</html:form>

</body>
</html:html>