<!-- descargasPrevision.jsp -->
<!-- VERSIONES:
	 raul.ggonzalez 26-04-2005 creacion
-->
	 
 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPrevisionesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	String idFacturacion = request.getAttribute("idFacturacion") == null?"":(String)request.getAttribute("idFacturacion");
	String idInstitucion = request.getAttribute("idInstitucion") == null?"":(String)request.getAttribute("idInstitucion");
	String idPago        = request.getAttribute("idPago") == null?"":(String)request.getAttribute("idPago");
	String idPersona     = request.getAttribute("idPersona") == null?"":(String)request.getAttribute("idPersona");
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.previsiones.titulo3"/>
		</td>
	</tr>
	</table>


	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea">
		<html:hidden property="modo" 									value=""/>
		<html:hidden property="tipoDownload" 					value=""/>
		<html:hidden property="idFacturacionDownload" value="<%=idFacturacion%>"/>
		<html:hidden property="idInstitucionDownload" value="<%=idInstitucion%>"/>
		<html:hidden property="idPagoDownload" value="<%=idPago%>"/>
		<html:hidden property="idPersonaDownload" value="<%=idPersona%>"/>

			<siga:ConjCampos leyenda="factSJCS.previsiones.leyenda">
			<table class="tablaCampos" align="center">	
				<tr>		
					<td class="labelText" >
						<siga:Idioma key="factSJCS.previsiones.literal.explicacion"/>
					</td>
				</tr>
				<tr>		
					<td class="labelText" >
					  <input type="button" alt="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" onclick="descargas('O');" class="button" value="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" />
					  &nbsp;<siga:Idioma key="factSJCS.previsiones.literal.ficheroTurnosOficio"/>&nbsp
					</td>
				</tr>
				<tr>		
					<td class="labelText" >
					  <input type="button" alt="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" onclick="descargas('G');" class="button" value="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" />
					  &nbsp;<siga:Idioma key="factSJCS.previsiones.literal.ficheroGuardias"/>&nbsp
					</td>
				</tr>
				<tr>		
					<td class="labelText" >
					  <input type="button" alt="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" onclick="descargas('E');" class="button" value="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" />
					  &nbsp;<siga:Idioma key="factSJCS.previsiones.literal.ficheroExpedientesEJG"/>&nbsp
					</td>
				</tr>
				<tr>		
					<td class="labelText" >
					  <input type="button" alt="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" onclick="descargas('S');" class="button" value="<%=UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download")%>" />
					  &nbsp;<siga:Idioma key="factSJCS.previsiones.literal.ficheroExpedientesSOJ"/>&nbsp
					</td>
				</tr>
			</table>
			</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion botones="C" clase="botonesDetalle" modal="P"/>
		
	<!-- FIN: LISTA DE VALORES -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
	
	//alert ("Idpago: <%=idPago%>");
	//alert ("Idpersona: <%=idPersona%>");

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.close();
		}		

		<!-- Asociada al boton Cerrar -->
		function descargas(tip) 
		{	
			document.forms[0].tipoDownload.value = tip;
			document.forms[0].modo.value = "download";
			document.forms[0].submit();
			
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
