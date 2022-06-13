<!DOCTYPE html>
<html>
<head>
<!-- busquedaRemesas.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>







<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.ws.form.BusquedaRemesasForm"%>
<%@ page import="com.siga.censo.ws.form.NuevaRemesaForm"%>

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
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>   
	
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	
	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="menu.censo.gestionCensoWS.gestionRemesas" localizacion="censo.ws.gestionremesas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaRemesasForm" staticJavascript="false" />  
		<html:javascript formName="NuevaRemesaForm" staticJavascript="false" />
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
			
</head>

<body onLoad="ajusteAlto('resultado');inicio();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	
	
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		
		
		<table  class="tablaCentralCampos"  align="center">
			<tr>				
				<td>
					<siga:ConjCampos leyenda="menu.censo.gestionCensoWS.gestionRemesas">
						<table class="tablaCampos" align="center">
							<html:form action="/CEN_BusquedaRemesas.do?noReset=true" method="POST" target="resultado">
								<html:hidden name="BusquedaRemesasForm" property = "modo"/>
								<html:hidden property="seleccionarTodos" />
								
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>								
	
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.colegio"/>
									</td>
									<td>
											<html:select property="idColegio" name="BusquedaRemesasForm" styleClass="boxCombo">
												<html:option value="">&nbsp;</html:option>
												<html:optionsCollection name="BusquedaRemesasForm" property="institucionesWS" value="id" label="nombre"></html:optionsCollection>
											</html:select>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.numeroPeticion"/>										
									</td>				
									<td>
										<html:text name="BusquedaRemesasForm" property="numeroPeticion" size="30" styleClass="box"/>
									</td>
								</tr>
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.fechaPeticionDesde"/>
									</td>
									<td>									
										<siga:Fecha  nombreCampo= "fechaPeticionDesde" valorInicial="${BusquedaRemesasForm.fechaPeticionDesde }"/>										
									</td>
								
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.conIncidencia"/>
									</td>
									<td>
										<html:checkbox name="BusquedaRemesasForm" property="conIncidencia"/>
										<html:hidden name="BusquedaRemesasForm" property="conIncidencia" value="false"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.conError"/>
									</td>
									<td>
										<html:checkbox name="BusquedaRemesasForm" property="conError"/>
										<html:hidden name="BusquedaRemesasForm" property="conError" value="false"/>
									</td>
								</tr>		
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.numeroColegiado"/>
									</td>
									<td>
										<html:text name="BusquedaRemesasForm" property="numeroColegiado" size="30" styleClass="box"/>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.nombre"/>										
									</td>				
									<td>
										<html:text name="BusquedaRemesasForm" property="nombre" size="30" styleClass="box"/>
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.primerApellido"/>
									</td>
									<td>
										<html:text name="BusquedaRemesasForm" property="primerApellido" size="30" styleClass="box"/>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.segundoApellido"/>										
									</td>				
									<td>
										<html:text name="BusquedaRemesasForm" property="segundoApellido" size="30" styleClass="box"/>
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.tipoIdentificacion"/>
									</td>
									<td>										
										<html:select property="idTipoIdentificacion" name="BusquedaRemesasForm" styleClass="boxCombo">
											<html:option value="">&nbsp;</html:option>
											<html:optionsCollection name="BusquedaRemesasForm" property="tiposIdentificacion" value="key" label="value"></html:optionsCollection>
										</html:select>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.identificacion"/>										
									</td>				
									<td>
										<html:text name="BusquedaRemesasForm" property="identificacion" size="30" styleClass="box"/>
									</td>
								</tr>
								
								
														
							</html:form>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

		<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
			<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_CENSO%>"/>
			<html:hidden property="modo" value="inicio"/>
			<html:hidden property="accionAnterior" value="${path}"/>
		</html:form>
		
		<html:form action="/CEN_ConfigPerfilColegio" method="POST" target="mainWorkArea">
			<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_CENSO%>"/>
			<html:hidden property="modo" value="ver"/>
			<html:hidden property="accionAnterior" value="${path}"/>
		</html:form>

		<!-- FIN: CAMPOS DE BUSQUEDA-->
	
		<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->
		<%  
			String botones = "AC,N,B,CON,L";
						 
		%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="menu.censo.gestionCensoWS.gestionRemesas" />
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0" 
				marginwidth="0"					 
				class="frameGeneral">
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
		
		
	</div>

	<div id="dialogoActualizaCenso" title='<bean:message key="censo.dialogo.nuevo.carga.webservice"/>' 	style="display: none">

		<div class="labelTextArea">
			<bean:message key="censo.ws.literal.aviso.carga.webservice" />
		</div>
		</br>
		<siga:ConjCampos>

			<html:form action="/CEN_NuevaRemesa.do?noReset=true" method="POST"	target="submitArea" styleId="idActualizaRemForm">
				<html:hidden name="NuevaRemesaForm" property="modo" styleId="idModoAct"/>
				<html:hidden name="NuevaRemesaForm" property="idColegioActualizar" styleId="idColActform"/>
				<div class="labelText">
					<label for="idColegioActualizar" style="width: 140px; float: left; color: black"><bean:message	key="censo.ws.literal.colegio" /></label>

					<html:select property="idColegioActualizar" name="NuevaRemesaForm" styleId="idColAct">
						<html:option value="">
							<siga:Idioma key="general.combo.seleccionar" />
						</html:option>
							<!-- CENSO-297@DTT.JAMARTIN@10/06/2022@INICIO -->
<%-- 						<html:optionsCollection name="BusquedaRemesasForm" 	property="institucionesWS" value="id" label="nombre"></html:optionsCollection> --%>
							<html:optionsCollection name="BusquedaRemesasForm" 	property="instituciones" value="id" label="nombre"></html:optionsCollection>
							<!-- CENSO-297@DTT.JAMARTIN@10/06/2022@FIN -->
					</html:select>

				</div>

			</html:form>

		</siga:ConjCampos>

	</div>



	<div id="dialogoNuevoExcel"  title='<bean:message key="censo.dialogo.nuevoExcel"/>' style="display:none">
			
			<div class="labelTextArea"><bean:message key="censo.ws.literal.aviso.carga.excel"/></div>
			</br>	
		  	<siga:ConjCampos >
		  	
				  	<html:form action="/CEN_NuevaRemesa.do?noReset=true" method="POST" target="submitArea" styleId="idNuevaRemForm" enctype="multipart/form-data">
							<html:hidden name="NuevaRemesaForm" property = "modo" styleId="idModoNuevo"/>
							<html:hidden property="seleccionarTodos" />
					
							<div class="labelText">
								<label for="idColegioInsertar"   style="width:140px;float:left;color: black"><bean:message key="censo.ws.literal.colegio"/></label>
								
								<html:select property="idColegioInsertar" name="NuevaRemesaForm" styleId="idColegioInsertar">
									<html:option value=""><siga:Idioma key="general.combo.seleccionar" /></html:option>
									<html:optionsCollection name="BusquedaRemesasForm" property="institucionesWS" value="id" label="nombre"></html:optionsCollection>
								</html:select>
											
							</div>
							
							</br>
							
							<div class="labelText">
								<label for="fechaExportacion"   style="width:140px;float:left;color: black"><bean:message key="censo.ws.literal.fechaExportacion"/></label>
								<siga:Fecha nombreCampo="fechaExportacion"  atributos='style="background-color: #FFFFFF; color: #000000; width: 100px;"'></siga:Fecha>
							</div>
							
							</br>
							
							<div class="labelText">
								<label for="file"   style="width:140px;float:left;color: black"><bean:message key="censo.ws.literal.fichero"/></label>
								<input type="file" id="file" name="file" size="35" styleClass="box" style="background-color: #FFFFFF;" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"/>
								
							</div>
					
					</html:form>
					
				</siga:ConjCampos>
		
		</div>
	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
		
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			jQuery.noConflict();
			
					
			function refrescarLocal(){
				
				cerrarPopup();
				
			}
			
			function cerrarPopup() {
				if (jQuery("#dialogoNuevoExcel").is(':visible')){
					closeDialog('dialogoNuevoExcel');
				} 
				
				if(jQuery("#dialogoActualizaCenso").is(':visible')) { 
					closeDialog('dialogoActualizaCenso');
				}
			}
			
			// Funcion asociada a boton buscar
			function buscarPaginador() {		
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
			
			function buscar() {				
				sub();
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();					
			}
			
			// Funcion asociada a boton limpiar
			function limpiar() {		
				document.forms[0].modo.value="abrir";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
			
			function inicio() {
				actualizarCenso();
				nuevo();
				cerrarPopup();
				if (document.forms['BusquedaRemesasForm'].modo.value == "abrirAvanzada") {
					buscarPaginador();				
				}
				
			}
			
			function consultas() {		
				document.RecuperarConsultasForm.submit();				
			}
			
			
			function actualizarCenso(){
				
				jQuery("#dialogoActualizaCenso").dialog(
						{
						      height: 300,
						      width: 600,
						      modal: true,
						      resizable: false,						      
						      buttons: {
						    	  "Actualiza": { id: 'Actualiza', text: '<siga:Idioma key="general.boton.actualizar"/>', click: function(){ lanzaActualizacion(); }},
						          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog("dialogoActualizaCenso");}}
						      }
						}
					);
					
				jQuery(".ui-widget-overlay").css("opacity","0");
				
			}
			
			
			
			function nuevo() {
				
				jQuery("#idNuevaRemForm")[0].reset();			
				jQuery("#dialogoNuevoExcel").dialog(
						{
						      height: 300,
						      width: 600,
						      modal: true,
						      resizable: false,						      
						      buttons: {
						    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionInsercion(); }},
						          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog("dialogoNuevoExcel");}}
						      }
						}
					);
					
				jQuery(".ui-widget-overlay").css("opacity","0");
			}
			
			
			function lanzaActualizacion(){
				
				
				jQuery("#idModoAct").val('actualizaWS');
				
				error = '';
				var idcol=jQuery("#idColAct").val();
				if(idcol==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.colegio'/>"+ '\n';
					
				}
				
				if (error!=''){
					alert(error);
					//fin();
					return false;
				} else {
				
					jQuery("#idColActform").val(idcol);
					jQuery("#idActualizaRemForm").submit();	
				}
				
			}
			
			
			function accionInsercion(){
				//sub();
				jQuery("#idModoNuevo").val('insertar');
				var idcolInsertar=jQuery("#idColegioInsertar").val();
				var idFechaExp=jQuery("#fechaExportacion").val();
				var idFile=jQuery("#file").val();
				error = '';
				
				if(idcolInsertar==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.colegio'/>"+ '\n';
					
				}
				
				if(idFechaExp==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.fechaExportacion'/>"+ '\n';
					
				}
				if(idFile==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.fichero'/>"+ '\n';
					
				}
				
				if (error!=''){
					alert(error);
					//fin();
					return false;
				} else {
					
					jQuery("#idNuevaRemForm").submit();
				}
				
			}
			
			function closeDialog(dialogo){
				 jQuery("#"+dialogo).dialog("close"); 
			}
			
		
			
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

</body>
</html>