<!-- verSolicitudDatosExpediente.jsp -->
<!-- 
	 Permite mostrar las solicitudes de modificacion de expedientes
	 VERSIONES:
	 miguel.villegas 15-02-2005 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenSoliModiDireccionesBean"%>
<%@ page import = "com.siga.beans.CenSolModiFacturacionServicioBean"%>
<%@ page import = "com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import = "com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import = "com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import = "com.siga.beans.CenDireccionesBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.beans.ExpExpedienteBean"%>
<%@ page import = "com.siga.beans.ExpSolicitudBorradoBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<!-- JSP -->
<% 
	// Declaraciones varias
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Hashtable modificada=new Hashtable();
	Hashtable original= new Hashtable();
	
	// Obtencion de los valores originales de la facturacion solicitada para modificar
	if (request.getAttribute("container_desc") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container_desc")).elements();
		while (enumSel.hasMoreElements())
		{
			original = (Hashtable) enumSel.nextElement();
		} 
	}

	// Obtencion de los valores modifiacdos de la facturacion solicitada
	if (request.getAttribute("container") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container")).elements();
		while (enumSel.hasMoreElements())
		{
			modificada = (Hashtable) enumSel.nextElement();
		}
	}
	// RGG 14/03/2007 cambio para dar un tamaño a la letra y en caso de Tiems darle otro
	String fontSize = "13px";
	if (((String)src.get("font.style")).indexOf("Times")!=-1) {
		fontSize="15px";
	}
	
%>

<html>

	<!-- HEAD -->
	<head>

		<style type="text/css">
			.labelTextRojo{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; 	font-weight: bold; 
						margin: auto; color:#6A0000; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; vertical-align: top;}	
						
			.labelTextNormal{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; vertical-align: top; 
						margin: auto; color:#<%=src.get("color.labelText")%>; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; padding-left: 5px;}							
						
			.boxConsultaRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}						  
						
			.boxComboNormal {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						margin: auto; color:#<%=src.get("color.labelText")%>;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent;}

			.boxComboRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}

		</style>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			<!-- Asociada al boton Cerrar -->
			function accionCerrar(){ 			
				window.top.close();
			}	
			
			<!-- Asociada a la carga de la pagina -->
			function originalModificado(estado){ 			
				if (estado!="10"){
					var mensaje='<siga:Idioma key="messages.censo.solicitudesModificacion.advertencia"/>';
					alert(mensaje);
				}
			}
		
		</script>	

	</head>
	<body onLoad="originalModificado(<%=String.valueOf(modificada.get(ExpSolicitudBorradoBean.C_IDESTADOSOLIC))%>)" class="tablaCentralCampos">
	<!-- Barra de titulo actualizable desde los mantenimientos -->

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulos">
					<siga:Idioma key="censo.consultaExpedientes.titulo2"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

		<!-- INICIO: CAMPOS -->
			<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "idPersona" value =""/>
			<html:hidden property = "idInstitucion" value =""/>
			<html:hidden property = "solicitudes"/>
				<table class="tablaCentralCampos" align="center">			
					<tr>				
						<td>
							<siga:ConjCampos leyenda="censo.consultaExpedientes.cabecera">
								<table class="tablaCampos" align="center">
									<!-- FILA -->
									<tr>		
										<td width="20%"class="labelText">
											<siga:Idioma key="censo.consultaExpedientes.literal.nombre"/>&nbsp;
										</td>
										<td width="80%">
											<html:text property="domicilioO" value='<%=String.valueOf(original.get("NOMBRE"))+" "+String.valueOf(original.get("APELLIDOS1"))+" "+String.valueOf(original.get("APELLIDOS2"))%>' styleClass="boxConsulta" readOnly="true" size="60"></html:text>
										</td>						
									</tr>	
									<!-- FILA -->
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.consultaExpedientes.literal.nifcif"/>&nbsp;
										</td>	
										<td>
											<html:text property="codigoPostal" value='<%=String.valueOf(original.get("NIFCIF"))%>' styleClass="boxConsulta" readOnly="true" size="30"></html:text>
										</td>							
									</tr>
									<!-- FILA -->
									<tr>		
										<td class="labelText">
											<siga:Idioma key="censo.consultaExpedientes.literal.tipoExpediente"/>&nbsp;
										</td>
										<td>
											<html:text property="domicilioO" value='<%=String.valueOf(original.get("TIPOEXPEDIENTE"))%>' styleClass="boxConsulta" readOnly="true"></html:text>
										</td>						
									</tr>
									<!-- FILA -->
									<tr>		
										<td class="labelText">
											<siga:Idioma key="censo.consultaExpedientes.literal.nExpediente"/>&nbsp;
										</td>
										<td>
											<html:text property="domicilioO" value='<%=String.valueOf(original.get("ANIOEXPEDIENTE"))+" / "+String.valueOf(original.get("NUMEROEXPEDIENTE"))%>' styleClass="boxConsulta" readOnly="true" size="60">&nbsp;/&nbsp;</html:text>
										</td>
									</tr>
				   				</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->	
			
			<!-- FIN: BOTONES REGISTRO -->	

			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	

			
			<siga:ConjBotonesAccion botones="C" modal="M" clase="botonesDetalle"/>

		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
