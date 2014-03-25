<!-- mediadoresImportCarga.jsp -->
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
<%@ page import="com.siga.censo.mediadores.form.MediadoresImportForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConModuloBean"%>


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
				
				//document.forms[0].nombrePersona.value="";
				
				document.forms[0].modo.value="abrir";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
			
			function accionAceptar() {
				sub();
				var f=document.getElementById("MediadoresImportForm");				
				
				if (f && f.idColegio && !f.idColegio.value) {					
  					var campo = "<siga:Idioma key="censo.mediadores.literal.colegio"/>";
  					var msg = "<siga:Idioma key="errors.required"  arg0='" + campo + "'/>";					
					alert(msg);
  					fin();
					return false;
  				}
				
				if (f && f.file && !f.file.value) {					
  					var campo = "<siga:Idioma key="censo.mediadores.literal.fichero"/>";
  					var msg = "<siga:Idioma key="errors.required"  arg0='" + campo + "'/>";					
					alert(msg);
  					fin();
					return false;
  				}
				
				
				f.modo.value = "insertar";
				//para obligar a volver a cargar el fichero si esta ha ido mal
				//me quedo con los valores que quiero conservar
				var idcol = f.idColegio.value
				f.submit();
				//reseteo y cargo de nuevo los valores que quiero quedarme
				f.reset();
				f.idColegio.value=idcol;
				return true;
			}
			
		</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="menu.censo.gestionMediadores.importar" localizacion="censo.ws.gestionMediadores.importar.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MediadoresImportForm" staticJavascript="false" />  	  	
		
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>

<body onLoad="ajusteAlto('resultado');">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<html:form action="/CEN_CargaMediadores.do" method="POST" target="resultado" enctype="multipart/form-data">
		
		
			<table  class="tablaCentralCampos"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="menu.censo.gestionMediadores.importar">
							<table class="tablaCampos" align="center">								
								<html:hidden name="MediadoresImportForm" property = "modo" value = "insertar"/>
								
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>								
	
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.mediadores.literal.colegio"/>
									</td>
									<td>
										<logic:empty property="instituciones" name="MediadoresImportForm">
											<html:hidden property="idColegio" name="MediadoresImportForm"  />
											<html:text property="nombreColegio" name="MediadoresImportForm" size="70" styleClass="boxComboConsulta" readonly="true"/>
										</logic:empty>
										<logic:notEmpty property="instituciones" name="MediadoresImportForm">
											<html:select property="idColegio" name="MediadoresImportForm" styleClass="boxCombo">
												<html:option value="">&nbsp;</html:option>
												<html:optionsCollection name="MediadoresImportForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
											</html:select>
										</logic:notEmpty>
									</td>
												
									<td class="labelText">
										<siga:Idioma key="censo.mediadores.literal.fichero"/>
									</td>				
									<td>	
										<html:file property="file" name="MediadoresImportForm" size="50" styleClass="box" accept=".csv"></html:file>
									</td>						
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
		</html:form>

		<siga:ConjBotonesAccion botones="A" clase="botonesSeguido"/>
		
		
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