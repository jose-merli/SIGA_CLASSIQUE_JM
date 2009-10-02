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
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// Obtencion del usuario y la institucion
	String idPersona=(String)request.getAttribute("IDPERSONA"); // Obtengo el identificador de la persona
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	

    // Botones a mostrar
	String botones = "B";
	
	// valores para el combo de tipos de modificaciones
	String valoresSolic[] = {String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES),String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DIRECCIONES),
							 String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_CUENTAS_BANCARIAS),String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV),
							 String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION),String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES)};   

	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscar()";
	}

			
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		

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
									<input type="hidden" name="limpiarFilaSeleccionada" value="">
									
									<!-- INICIOFILAS -->					
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.tipoModificacion"/>
										</td>				
										<td>
											<siga:ComboBD nombre = "tipoModifEspec" tipo="tipoModifEspec" clase="boxCombo" obligatorio="false" parametro="<%=valoresSolic%>"/>
										</td>
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.estado"/>&nbsp;&nbsp;
										</td>					
										<td>
											<siga:ComboBD nombre ="estadoSolicitudModif" tipo="EstadoSolicitudModif" clase="boxCombo" obligatorio="false"/>
										</td>
									</tr>
									<tr>	
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.fechaDesde"/>&nbsp;&nbsp;
										</td>										
										<td>
											<html:text property="fechaDesde" size="10" styleClass="box" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif"   border="0">
											</a>						
											
										</td>					
										<td class="labelText">
											<siga:Idioma key="censo.busquedaSolicitudesModificacion.literal.fechaHasta"/>&nbsp;&nbsp;					
										</td>					
										<td>
											<html:text property="fechaHasta" size="10" styleClass="box" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif"  border="0">
											</a>						
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
