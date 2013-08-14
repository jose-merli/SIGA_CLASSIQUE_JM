<!-- busquedaRemesas.jsp -->
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
<%@ page import="com.siga.censo.ws.form.BusquedaRemesasForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConModuloBean"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.ws.gestionremesas.titulo" localizacion="censo.ws.gestionremesas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaRemesasForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			// Funcion asociada a boton buscar
			function buscarPaginador() {		
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
			
			function buscar() {				
				sub();
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();					
			}
			
			// Funcion asociada a boton limpiar
			function limpiar() {		
				
				//document.forms[0].nombrePersona.value="";
				
				document.forms[0].modo.value="abrir";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();	
			}
			
			function inicio() {
				<%if (request.getAttribute("buscar") != null) {%>
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
				<%}%>
				
			}
			
			function consultas() {		
				document.RecuperarConsultasForm.submit();				
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
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
					<siga:ConjCampos leyenda="censo.ws.gestionremesas.titulo">
						<table class="tablaCampos" align="center">
							<html:form action="/CEN_BusquedaRemesas.do?noReset=true" method="POST" target="resultado">
								<html:hidden name="BusquedaRemesasForm" property = "modo" value = ""/>
								<html:hidden property="seleccionarTodos" />
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>								
	
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.colegio"/>
									</td>
									<td>
										<logic:empty property="instituciones" name="BusquedaRemesasForm">
											<html:hidden property="idColegio" name="BusquedaRemesasForm"  />
											<html:text property="nombreColegio" name="BusquedaRemesasForm" size="70" styleClass="boxComboConsulta" readonly="true"/>
										</logic:empty>
										<logic:notEmpty property="instituciones" name="BusquedaRemesasForm">
											<html:select property="idColegio" name="BusquedaRemesasForm" styleClass="boxCombo">
												<html:option value="">&nbsp;</html:option>
												<html:optionsCollection name="BusquedaRemesasForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
											</html:select>
										</logic:notEmpty>
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
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.fechaPeticionHasta"/>										
									</td>				
									<td>
										<siga:Fecha  nombreCampo= "fechaPeticionHasta" valorInicial="${BusquedaRemesasForm.fechaPeticionHasta }"/>
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
										<html:text name="BusquedaRemesasForm" property="idTipoIdentificacion" size="30" styleClass="box"/>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.identificacion"/>										
									</td>				
									<td>
										<html:text name="BusquedaRemesasForm" property="identificacion" size="30" styleClass="box"/>
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.incidencias"/>
									</td>
									<td colspan="3">																				
										<html:select property="idIncidencias" styleClass="boxCombo">
											<html:option value=""/>
											<option value="1"><siga:Idioma key="censo.ws.incidencia.conIncidencias"/></option>								
											<option value="2"><siga:Idioma key="censo.ws.incidencia.conIncidencias.general"/></option>											
											<option value="3"><siga:Idioma key="censo.ws.incidencia.conIncidencias.particular"/></option>
										</html:select>
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

		<!-- FIN: CAMPOS DE BUSQUEDA-->
	
		<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->
		<%  
			String botones = "B,CON,L";
						 
		%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="censo.ws.gestionremesas.titulo" />
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
			<!--style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
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