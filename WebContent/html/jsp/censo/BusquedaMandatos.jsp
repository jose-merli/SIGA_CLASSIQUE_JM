<!DOCTYPE html>
<html>
<head>
<!-- BusquedaMandatos.jsp -->

<!-- CABECERA JSP -->
<%@ page import="com.siga.beans.ConModuloAdm"%>
<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.MantenimientoMandatosForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConModuloBean"%>

 
<!-- JSP -->
<%
 
	String buscar = (String)request.getAttribute("buscar");

	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en más de una institucion
	// Obtengo el UserBean y en consecuencia la institucion a la que pertenece y su nombre
	
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>


<!-- HEAD -->

	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.mantenimientoMandatos.titulo" localizacion="Censo"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoMandatosForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			// Funcion asociada a boton buscar
			function buscarPaginador() {		
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";
				jQuery("#buscarForm").submit();
				//document.forms[0].submit();	
			}
			
			function buscar(modo) {
				sub();
				if(jQuery('#idChkPendientesFirmar').is(':checked')){
					document.forms[0].chkPendientesFirmar.value="1";
				}else{
					document.forms[0].chkPendientesFirmar.value="0";
				}
				
				document.forms[0].tipoMandato.value=jQuery('#idTipoMandato').val();
				document.forms[0].tipoCliente.value=jQuery('#idTipoCliente').val();

				if(modo)
					document.forms[0].modo.value = modo;
				else
		        	document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				//document.forms[0].submit();
				jQuery("#buscarForm").submit();
			}
			
			function seleccionarTodos(pagina) {
				document.forms[0].seleccionarTodos.value = pagina;
				buscar('buscarPor');					
			}		

			// Funcion asociada a boton limpiar
			function limpiar() {		
				document.forms[0].chkPendientesFirmar.checked=true;
				document.forms[0].nombrePersona.value="";
				document.forms[0].apellido1.value="";
				document.forms[0].apellido2.value="";
				if ($("#numeroColegiado").length != 0)
					document.forms[0].numeroColegiado.value="";
		
				
				document.forms[0].nif.value="";
				document.forms[0].modo.value="abrir";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
			
			function consultas() {		
				document.RecuperarConsultasForm.submit();				
			}
			
			function accionProcesar() {
				
				if(document.getElementById("uploadBox").value != "") {
					sub();
					
					var mensaje = "<siga:Idioma key='censo.mantenimientoMandatos.confirmarCarga'/> ";
						
					//Preguntamos si queremos subir los datos del fichero o no							
					if (confirm(mensaje)) 
					{
						document.forms[0].modo.value="procesarFichero";
						document.forms[0].target="submitArea";	
						var alerta = "<siga:Idioma key='censo.mantenimientoMandatos.procesandoFichero'/> ";
						alert(alerta);
						document.forms[0].submit();	
					} else{
						fin();
					}	
				}else{
					var mensaje = "Debe seleccionar un fichero";
					alert(mensaje);
				}

			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>

<body onLoad="inicio(); ajusteAlto('resultado');">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCampos"  align="center">
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
						<table class="tablaCampos" align="center">
							<html:form styleId="buscarForm" action="/CEN_MantenimientoMandatos.do?noReset=true" method="POST" target="resultado"  enctype="multipart/form-data" >
								<html:hidden name="mantenimientoMandatosForm" property = "modo" value = ""/>
								<html:hidden name="mantenimientoMandatosForm" property="seleccionarTodos" />
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>

								<!-- parametro para colegiados o no -->
								<html:hidden name="mantenimientoMandatosForm" property = "chkPendientesFirmar" />
								<html:hidden name="mantenimientoMandatosForm" property = "tipoMandato" value=""/>


									<tr>
										<td class="labelText"><siga:Idioma key="censo.busquedaClientesAvanzada.literal.tipoColegiacion"/></td>
										<td>
											<select id="idTipoCliente">
												<option></option>
												<option value="C" selected="selected"><siga:Idioma key="censo.colegiacion.colegiado"/></option>
												<option value="N"><siga:Idioma key="censo.colegiacion.noColegiado"/></option>
											</select>
										</td>
										<input type="hidden" name="tipoCliente">
										<td class="labelText">
											<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
										</td>
										<td>
											<html:text name="mantenimientoMandatosForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box" />
										</td>
									</tr>
									
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
									</td>
									<td>
										<html:text name="mantenimientoMandatosForm" property="nif" size="15" styleClass="box"/>
									</td>
	
									<td class="labelText"><siga:Idioma key="censo.busquedaClientes.literal.nombre"/></td>				
									<td>
										<html:text name="mantenimientoMandatosForm" property="nombrePersona" size="30" styleClass="box"/>
									</td>
								</tr>
	
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
									</td>
									<td>
										<html:text name="mantenimientoMandatosForm" property="apellido1" size="30" styleClass="box"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
									</td>
									<td>
										<html:text name="mantenimientoMandatosForm" property="apellido2" size="30" styleClass="box"/>
									</td>
								</tr>
								<tr>
									<td class="labelText"><siga:Idioma key="censo.fichaCliente.bancos.mandatos.fechaFirma"/></td>
									<td><siga:Fecha nombreCampo="fechaFirmaInicio" /> - <siga:Fecha nombreCampo="fechaFirmaFin"/></td>
									<td class="labelText"><siga:Idioma key="gratuita.retenciones.fechaModificacion"/></td>
									<td><siga:Fecha nombreCampo="fechaModInicio"/> - <siga:Fecha nombreCampo="fechaModFin"/></td>
								</tr>
								<tr style="border-top:1px solid black">
									<td class="labelText">Tipo</td>
									<td>
										<select id="idTipoMandato">
											<option></option>
											<option value="0" selected="selected"><siga:Idioma key="pestana.fichaCliente.datosServicios"/></option>
											<option value="1"><siga:Idioma key="pestana.fichaCliente.datosProductos"/></option>
										</select>
									</td>
									
									<td class="labelText"><siga:Idioma key="censo.mantenimientoMandatos.pendieteFirmar"/></td>
									<td>		 
		 								<input type="checkbox" id="idChkPendientesFirmar" checked/>		
									</td>
								</tr>
								<tr>
									<td colspan="4">
								<div id="divSubida" style="padding:2px;">
								<siga:ConjCampos leyenda="Carga por fichero">
									<c:out class="labelText"><siga:Idioma key='censo.mantenimientoMandatos.exportarMandatos'/></c:out><br>
									<html:file name="mantenimientoMandatosForm" styleId="uploadBox" property="fichero" size="90" styleClass="box"  
									accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"/>
									<input type="button" alt="Buscar" name="botonProcesar" onclick="accionProcesar()" class="button" value="Procesar">
								</siga:ConjCampos>
								</div>
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


		<siga:ConjBotonesBusqueda botones="B,CON"  titulo="" />
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->

	</div>	
   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
			id="resultado"
			name="resultado" 
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0";
			class="frameGeneral">
		<!--style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
  		</iframe>

		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
<script>ajusteAlto('resultado');</script>
</body>
</html>