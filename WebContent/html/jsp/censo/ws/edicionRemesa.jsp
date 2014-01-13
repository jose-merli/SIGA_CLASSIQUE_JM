<!DOCTYPE html>
<!-- edicionRemesa.jsp -->

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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.ws.form.EdicionRemesaForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ConModuloBean"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>




<% 

UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
CenInstitucionAdm institucionAdm = new CenInstitucionAdm(userBean);

%> 

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.ws.edicionRemesa.titulo" localizacion="censo.ws.edicionRemesa.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="EdicionRemesaForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			// Funcion asociada a boton buscar
			function buscarPaginador() {	
				sub();
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
			
			function buscar() {
				sub();
				document.forms[0].modo.value="buscarInit";
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
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>

<body onLoad="ajusteAlto('resultado');buscarPaginador()">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		<table  class="tablaCentralCampos"  align="center">
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.ws.gestionremesas.datosRemesa">
						<table class="tablaCampos" align="center">
															
							<!-- FILA -->
							<tr>				
								<td class="labelText">
									<siga:Idioma key="censo.ws.literal.colegio"/>
								</td>
								<td>
									<html:text readonly="true" name="EdicionRemesaForm" property="nombreColegio" styleClass="boxConsulta" size="60"/>										
								</td>

								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="censo.ws.literal.numeroPeticion"/>										
								</td>				
								<td>
									<html:text readonly="true" name="EdicionRemesaForm" property="numeroPeticion" styleClass="boxConsulta" size="20"/>										
								</td>
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="censo.ws.literal.incidenciaGeneral"/>										
								</td>				
								<td width="300">
									<logic:equal property="conerrores" value="1" name="EdicionRemesaForm">		
										<c:forEach items="${EdicionRemesaForm.listaErrores}" var="incidencia" varStatus="i">												
											<c:out value="${incidencia}"/><br/>
										</c:forEach>
									</logic:equal>
									<logic:equal property="conerrores" value="0" name="EdicionRemesaForm">
										<siga:Idioma key="censo.ws.literal.sinIncidencia"/>
									</logic:equal>		
								</td>
												
								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="censo.ws.literal.fechaPeticion"/>
								</td>
								<td>
									<html:text readonly="true" name="EdicionRemesaForm" property="fechapeticion" styleClass="boxConsulta" size="30"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="censo.ws.literal.estado"/>										
								</td>				
								<bean:define id="idEstadoenvio" name="EdicionRemesaForm" property="idEstadoenvio" type="Short"></bean:define>
								<td colspan="3">
									<siga:Idioma key="<%=AppConstants.ECOM_CEN_MAESESTADOENVIO.getDescripcion(idEstadoenvio)%>"/>
								</td>
							</tr>
							
						</table>
					</siga:ConjCampos>
								
					<siga:ConjCampos leyenda="censo.ws.edicionremesas.filtroColegiado">
						<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="POST" target="resultado">
							<table class="tablaCampos" align="center">							
								
								<input type="hidden" name="idcensowsenvio" value="<bean:write name="EdicionRemesaForm" property="idcensowsenvio"/>"/>
								<input type="hidden" name="modo" value="<bean:write name="EdicionRemesaForm" property="modo"/>"/>
								
								
								<html:hidden property="seleccionarTodos" />
								<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.numeroColegiado"/>
									</td>
									<td>
										<html:text name="EdicionRemesaForm" property="numeroColegiado" size="30" styleClass="box"/>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.nombre"/>										
									</td>				
									<td>
										<html:text name="EdicionRemesaForm" property="nombre" styleId="nombre" size="30" styleClass="box"/>										
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.primerApellido"/>
									</td>
									<td>
										<html:text name="EdicionRemesaForm" property="primerApellido" size="30" styleClass="box"/>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.segundoApellido"/>										
									</td>				
									<td>
										<html:text name="EdicionRemesaForm" property="segundoApellido" size="30" styleClass="box"/>
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.tipoIdentificacion"/>
									</td>
									<td>										
										<html:select property="idTipoIdentificacion" name="EdicionRemesaForm" styleClass="boxCombo">
											<html:option value="">&nbsp;</html:option>
											<html:optionsCollection name="EdicionRemesaForm" property="tiposIdentificacion" value="key" label="value"></html:optionsCollection>
										</html:select>
									</td>
	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.identificacion"/>										
									</td>				
									<td>
										<html:text name="EdicionRemesaForm" property="identificacion" size="30" styleClass="box"/>
									</td>
								</tr>
								
								<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.estado"/>
									</td>
									<td>										
										<html:select property="idestadocolegiado" name="EdicionRemesaForm" styleClass="boxCombo">
											<html:option value="">&nbsp;</html:option>
											<html:optionsCollection name="EdicionRemesaForm" property="estadosColegiado" value="key" label="value"></html:optionsCollection>
										</html:select>
									</td>	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.incidencia"/>
									</td>
									<td>										
										<html:select property="idincidencia" name="EdicionRemesaForm" styleClass="boxCombo">
											<html:option value="">&nbsp;</html:option>
											<html:optionsCollection name="EdicionRemesaForm" property="incidenciasColegiado" value="key" label="value"></html:optionsCollection>
										</html:select>
									</td>	
									
								</tr>
								
								
							</table>
						</html:form>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

		<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">			
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
			String botones = "B";
						 
		%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="censo.ws.edicionRemesa.listadoColegiados" />
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