<!-- SolicitudIncorporacionValidacion.jsp -->
<!-- CABECERA JSP -->
<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CenSolicitudIncorporacionBean"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudBean"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<% 
	String app = request.getContextPath(); 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	Boolean errorCodigo = (Boolean)request.getAttribute("errorCodigo");
%>

<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">

<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp" />
<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css" />

<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->
<!--siga:Titulo titulo="censo.solicitudIncorporacion.localizacion" 
 		         localizacion="censo.solicitudIncorporacion.localizacion"/-->
<siga:TituloExt titulo="censo.solicitudIncorporacion.titulo" localizacion="censo.solicitudIncorporacion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

<script language="JavaScript">
		function validaClave () {
			sub();
			
			if (SolicitudIncorporacionForm.clave.value=="") {
				var mensaje = "<siga:Idioma key='censo.SolicitudIncorporacion.codigo'/> <siga:Idioma key='messages.campoObligatorio.error'/>";
				alert (mensaje);
				fin();
			 	return false;
  			}  						
			
			if (isNaN(document.SolicitudIncorporacionForm.clave.value)) {
				var mensaje = "<siga:Idioma key='censo.SolicitudIncorporacion.codigo'/> <siga:Idioma key='messages.campoNumerico.error'/>";
				alert (mensaje);
				fin();
			 	return false;
  			}
  			
			document.SolicitudIncorporacionForm.modo.value="VER";
			document.SolicitudIncorporacionForm.submit();
	 	}
	</script>
</head>

<body>
	<html:form action="/SIN_SolicitudIncorporacion.do" focus="clave">
		<html:hidden property="esModal" value="N" />
		<br/>
		<br/>
		<br/>
		<br/>

		<table style="width: 964;" align="center" cellspacing=0>

			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.SolicitudIncorporacion.cabecera4" />
				</td>
			</tr>
			<tr>
				<td class="labelTextValor">
					<br> <siga:Idioma key="censo.SolicitudIncorporacion.frase1" /> <br>
				</td>
			</tr>
			<tr>
				<td class="labelTextValor">
					<html:submit styleClass="button" onclick="modo.value='nuevo'">
						<siga:Idioma key="general.boton.new" />
					</html:submit> 
					<br><br>
				</td>
			</tr>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.SolicitudIncorporacion.cabecera5" />
				</td>
			</tr>
			<tr>
				<td class="labelTextValor">
					<br> <siga:Idioma key="censo.SolicitudIncorporacion.frase2" /> <br>
				</td>
			</tr>
			<tr>
				<td class="labelTextValor">
					<input type="password" class="box" name="clave" maxlength="10"> <html:hidden property="modo" value="" /> 
					<input type="button" class="button" id="idButton" onClick="validaClave();" value='<siga:Idioma key="general.boton.consultar" />' /> 
					<br><br>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html>

<script>
	if (<%=errorCodigo%>) {
		var mensaje = "<siga:Idioma key='messages.censo.solicitudIncorporacion.errorNoExisteSolicitud'/>";
		alert(mensaje);
	}
</script>