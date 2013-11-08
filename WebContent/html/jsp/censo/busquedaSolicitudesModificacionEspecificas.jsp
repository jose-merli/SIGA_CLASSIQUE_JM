<!DOCTYPE html>
<html>
<head>
<!-- busquedaSolicitudesModificacionEspecificas.jsp -->
<!-- 
	 Muestra el formulario de busqueda de solicitudes de modificacion especificas
	 VERSIONES:
	 miguel.villegas 24-01-2005 
-->

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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="org.redabogacia.sigaservices.app.services.gen.SelectDataService" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	// Obtencion del usuario y la institucion
	String idPersona=(String)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	

    // Botones a mostrar
	String botones = "B";
	
	// valores para el combo de tipos de modificaciones	  
	String valoresSolic = "{\""+SelectDataService.IDTIPOMODIFICACION_KEY+"\":[\""+String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES)+","+String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES)+","+
			 String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS)+","+String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV)+","+
			 String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION)+","+String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES)+","+
			 String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXP_FOTO)+"\"]}";
	
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscar()";
	}

			
%>	


	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="SolicitudesModificacionEspecificasForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="censo.busquedaSolicitudesModificacion.cabecera" 
			  localizacion="censo.busquedaSolicitudesModificacion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body onLoad="<%=funcionBuscar %>;ajusteAlto('resultado');">
	
		<div id="camposRegistro" class="posicionBusquedaSolo" align="center">	

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->

			<table class="tablaCentralCampos" align="center">
				<tr>				
					<td>
						<!-- SUBCONJUNTO DE DATOS DE BUSQUEDA -->
						<siga:ConjCampos leyenda="censo.busquedaSolicitudesModificacion.leyenda">
							<table class="tablaCampos" align="center">
								<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="resultado">					
									<html:hidden property = "modo" value = ""/>
									<html:hidden property = "solicitudes" value = ""/>
									<html:hidden property = "textoModificacion" value = ""/>
									<html:hidden property = "idPersona" value ="<%=idPersona%>"/>
									<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
									<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
									
									<!-- INICIOFILAS -->					
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.tipoModificacion"/>
										</td>				
										<td>
											<siga:Select id="tipoModifEspec"
														queryId="getTiposModificacionEspecificas"
														params="<%=valoresSolic%>"/>
										</td>
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.estado"/>&nbsp;&nbsp;
										</td>					
										<td>
											<siga:Select queryId="getEstadosSolicitudMod" id="estadoSolicitudModif" />
										</td>
									</tr>
									<tr>	
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.fechaDesde"/>&nbsp;&nbsp;
										</td>										
										<td>
										<siga:Fecha  nombreCampo= "fechaDesde"/>
											
										</td>					
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.fechaHasta"/>&nbsp;&nbsp;					
										</td>					
										<td>
										<siga:Fecha  nombreCampo= "fechaHasta" campoCargarFechaDesde="fechaDesde"/>
										</td>
									</tr>
								</html:form>	
							</table>				
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
			<!-- FIN: CAMPOS DE BUSQUEDA-->
	
	
			<!-- INICIO: BOTONES BUSQUEDA -->
			<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
			-->
	
	        <siga:ConjBotonesBusqueda botones="<%=botones%>" titulo=""/>
			
			<!-- FIN: BOTONES BUSQUEDA -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">
	
				<!-- Funcion asociada a boton buscar -->
				function buscar() 
				{		
					var indice;
					sub();
					//if (validateSolicitudesModificacionEspecificasForm(document.SolicitudesModificacionEspecificasForm)){ 
					//INC-904 PDM quimtamos la obligatoriedad del campo Tipo Modificación
						document.forms[0].modo.value='buscarPor';
						document.forms[0].textoModificacion.value=document.forms[0].tipoModifEspec.options[document.forms[0].tipoModifEspec.selectedIndex].text;
						document.forms[0].target='resultado';				
						document.forms[0].submit();
					//}	
				}
			
			</script>
			<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	
			<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0"				 
							class="frameGeneral">
			</iframe>
		</div>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
