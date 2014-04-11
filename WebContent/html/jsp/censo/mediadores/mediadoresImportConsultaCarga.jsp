<!-- mediadoresImportConsultaCarga.jsp -->
<!DOCTYPE html>
<!-- CABECERA JSP -->
<%@page import="com.siga.beans.ConModuloAdm"%>
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
<%@ page import="com.siga.censo.mediadores.form.MediadoresImportConsultaForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConModuloBean"%>

<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script> 
	
		<script type="text/javascript">
			jQuery.noConflict();
			
			// Funcion asociada a boton limpiar
			function limpiar() {		
				
				var f=document.getElementById("MediadoresImportConsultaForm");
				f.reset();
			}
			
			function generarFichero() {	
				sub();
				var f=document.getElementById("MediadoresImportConsultaForm");
				f.modo.value = "insertar";	
				f.target="resultado";
				f.submit();
			}
			
			
			
			function buscar() {
				sub();
				var f=document.getElementById("MediadoresImportConsultaForm");
				f.modo.value = "buscar";
				f.target="resultado";
				f.submit();
			}
			
			function refrescarLocal(){
				buscar();
			}
			
			
		</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="menu.censo.gestionMediadores.consultaCargas" localizacion="censo.ws.gestionMediadores.importar.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MediadoresImportConsultaForm" staticJavascript="false" />  	  
	
		
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>

<body onLoad="ajusteAlto('resultado');buscar();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<html:form action="/CEN_ConsultaCargaMediadores.do" method="POST" target="resultado">	
			<html:hidden name="MediadoresImportConsultaForm" property = "modo" value = "insertar"/>
		
			<table  class="tablaCentralCampos"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="menu.censo.gestionMediadores.consultaCarga">
							<table class="tablaCampos" align="center">						
								
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>								
	
								<!-- FILA -->
								<tr>			
									<td class="labelText">
										<siga:Idioma key="censo.mediadores.literal.colegio"/>
									</td>
									<td>
										<logic:empty property="instituciones" name="MediadoresImportConsultaForm">
											<html:hidden property="idColegio" name="MediadoresImportConsultaForm"  />
											<html:text property="nombreColegio" name="MediadoresImportConsultaForm" size="70" styleClass="boxComboConsulta" readonly="true"/>
										</logic:empty>
										<logic:notEmpty property="instituciones" name="MediadoresImportConsultaForm">
											<html:select property="idColegio" name="MediadoresImportConsultaForm" styleClass="boxCombo">
												<html:option value="">&nbsp;</html:option>
												<html:optionsCollection name="MediadoresImportConsultaForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
											</html:select>
										</logic:notEmpty>
									</td>						
								</tr>
								
								<tr>								
									<td class="labelText" style="vertical-align:middle" width="160px">
										<siga:Idioma key="censo.mediadores.literal.carga.fechaDesde" />
									</td>
									<td width="100px" style="vertical-align:middle">
										<siga:Fecha nombreCampo="fechaCargaDesde" valorInicial="${MediadoresImportConsultaForm.fechaCargaDesde}" anchoTextField="8"/> 
									</td>
									
									<td class="labelText" style="vertical-align:middle" width="160px">
										<siga:Idioma key="censo.mediadores.literal.carga.fechaHasta" />
									</td>
									<td width="100px" style="vertical-align:middle">
										<siga:Fecha nombreCampo="fechaACargaHasta" valorInicial="${MediadoresImportConsultaForm.fechaCargaHasta}" anchoTextField="8"/> 
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
		

		</html:form>	
		
		
		<siga:ConjBotonesBusqueda botones="B,L"  titulo="menu.censo.gestionMediadores.consultaCargas" />
		
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0" 
				marginwidth="0";					 
				class="frameGeneral">
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	</div>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->



				
</body>
</html>