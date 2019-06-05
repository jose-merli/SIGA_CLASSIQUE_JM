<!DOCTYPE html>
<!-- edicionColegiado.jsp -->

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
<%@ page import="com.siga.censo.ws.form.EdicionColegiadoForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ConModuloBean"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<c:if test="${!EdicionColegiadoForm.historico}">
		<siga:Titulo titulo="censo.ws.gestioncolegiado.titulo" localizacion="censo.ws.gestioncolegiado.localizacion"/>
	</c:if>
	
	<c:if test="${EdicionColegiadoForm.historico}">
		<siga:Titulo titulo="censo.ws.gestioncolegiado.historico.titulo" localizacion="censo.ws.gestioncolegiado.historico.localizacion"/>
	</c:if>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="EdicionColegiadoForm" staticJavascript="false" />  
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
		
		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		
		jQuery(function(){
			jQuery("#codigopaisextranj").on("change", function(){
				selPais(jQuery(this).val());
			});
			
		});
	
		function buscar() {
			if (!${EdicionColegiadoForm.historico}) {
				sub();
				document.forms[0].modo.value="buscarInit";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
			}
		}
		
		function inicio() {
			
		}
		
		
		
		
	 	
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->	
</head>



<body onLoad="ajusteAlto('resultado');buscar();inicio();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	
	
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		
		<html:form action="/CEN_EdicionColegiado.do?noReset=false" method="POST" target="resultado">	
		<c:set var="htmlTextReadOnly" value="false" />
		<c:set var="htmlTextClass" value="box" />		
		<c:set var="comboBDClass" value="boxCombo" />
		<c:set var="comboProvincia" value="block" />
		<c:set var="textProvincia" value="none" />
		
		<c:if test="${EdicionColegiadoForm.accion!='editarCambio' || EdicionColegiadoForm.historico}">
			<c:set var="htmlTextReadOnly" value="true" />
			<c:set var="htmlTextClass" value="boxConsulta" />
			<c:set var="comboBDClass" value="boxComboConsulta" />
			<c:set var="comboProvincia" value="none" />
			<c:set var="textProvincia" value="block" />
		</c:if>
		
		<html:hidden name="EdicionColegiadoForm" property="incidenciaNumeroColegiadoDuplicadoRevisada" value="false"/>
		<html:hidden name="EdicionColegiadoForm" property="incidenciaPoblacionNoEncontradaRevisada" value="false"/>
		<html:hidden name="EdicionColegiadoForm" property="incidenciaInscritoRevisada" value="false"/>
		
					
			<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.datosColegiado">			
				<table class="tablaCampos" align="center">							
					
					<input type="hidden" name="modo" value="<bean:write name="EdicionColegiadoForm" property="modo"/>"/>
					<input type="hidden" name="idcensodatos" value="${EdicionColegiadoForm.idcensodatos}"/>
					
					<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
					
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.numColegiado"/>
						</td>
						
						<td>
							<html:text name="EdicionColegiadoForm" property="ncolegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.nombre"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="nombre" styleId="nombreColegiado" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.primerApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido1" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>						
						</td>
						
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.segundoApellido"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="apellido2" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.tipoIdentificacion"/>
						</td>
						<td>							
							<html:select property="idcensotipoidentificacion" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">	
								<html:option value=""></html:option>							
								<html:optionsCollection name="EdicionColegiadoForm" property="tiposIdentificacion" value="key" label="value"></html:optionsCollection>
							</html:select>						
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.identificacion"/>
						</td>
						<td>
							<html:text name="EdicionColegiadoForm" property="numdocumento" size="30" styleClass="${htmlTextClass}" readonly="${htmlTextReadOnly}"/>
						</td>						
					</tr>
					
									
					<!-- FILA -->
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.situacionEjer"/>
						</td>
						<td>						
							
							<html:select property="idecomcensosituacionejer" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">
								<html:option value=""></html:option>								
								<html:optionsCollection name="EdicionColegiadoForm" property="situacionesEjerciente" value="key" label="value"></html:optionsCollection>
							</html:select>							
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.fechasituacion"/>
						</td>
						<td>
							<siga:Fecha  nombreCampo= "fechasituacion" valorInicial="${EdicionColegiadoForm.fechasituacion}" disabled="${htmlTextReadOnly}"/>							
						</td>						
					</tr>
					
					<!-- FILA -->
					<tr>				
						<td class="labelText">
							<siga:Idioma key="censo.ws.literal.motivocambio"/>
						</td>
						
						<td>
							<html:select property="idmotivocambio" name="EdicionColegiadoForm" styleClass="boxCombo" disabled="${htmlTextReadOnly}">
								<html:option value=""></html:option>								
								<html:optionsCollection name="EdicionColegiadoForm" property="motivosCambioSituacion" value="key" label="value"></html:optionsCollection>
							</html:select>	
													
						</td>
						
										
					</tr>				
					
				</table>			
			</siga:ConjCampos>
			
	
			
			<bean:define id="idestadocolegiado" name="EdicionColegiadoForm" property="idestadocolegiado" type="Short"></bean:define>
			
			<siga:ConjCampos leyenda="censo.ws.literal.estado">								
				<table class="tablaCampos" align="center">											
					<tr class="labelTextValue">
						<td>&nbsp;&nbsp;<siga:Idioma key="<%=AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.getDescripcion(idestadocolegiado)%>"/></td>
					</tr>					
				</table>
			</siga:ConjCampos>
			
			<c:choose>
				<c:when test="${EdicionColegiadoForm.incidencias!=null}">				
					<siga:ConjCampos leyenda="censo.ws.edicioncolegiado.incidencias">								
						<table class="tablaCampos" align="center">
							<c:forEach items="${EdicionColegiadoForm.incidencias}" var="incidencia" varStatus="i">							
								<tr class="labelTextValue">									
									<td id="${i.index}">&nbsp;*&nbsp;<siga:Idioma key="${incidencia}"/></td>									
								</tr>
							</c:forEach>
						</table>
					</siga:ConjCampos>
				</c:when>		
			</c:choose>
			
			
					
		</html:form>
		
		<c:choose>
			<c:when test="${!EdicionColegiadoForm.historico && EdicionColegiadoForm.accion=='editarCambio'}">
				<siga:ConjBotonesAccion botones="G, DESC" titulo="censo.ws.gestioncolegiado.historicoCambios" clase="botonesSeguido"/>		
			</c:when>
			<c:when test="${!EdicionColegiadoForm.historico && EdicionColegiadoForm.accion!='editarCambio'}">
				<siga:ConjBotonesAccion botones="" titulo="censo.ws.gestioncolegiado.historicoCambios" clase="botonesSeguido"/>
			</c:when>
		</c:choose>
			
		<!-- formulario para el bot�n volver -->		
		<c:choose>
			<c:when test="${EdicionColegiadoForm.historico}">
				<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<input type="hidden" name="historico"  id="historico"  value="false">
					<input type="hidden" name="accion"  id="accion"  value="${EdicionColegiadoForm.accionPadre}">
					<input type="hidden" name="modo"  id="modo"  value="${EdicionColegiadoForm.accionPadre}">
					<html:hidden property="idcensodatosPadre" value="${EdicionColegiadoForm.idcensodatosPadre }"/>
				</html:form>		
			</c:when>
			<c:otherwise>
				<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="POST" target="mainWorkArea">
					<html:hidden property="volver" value="true"/>
					<html:hidden property="modo" value="${EdicionRemesaForm.accion}"/>
				</html:form>
			</c:otherwise>
		</c:choose>

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0"					 
				class="frameGeneral">
			<!--style="position:absolute; width:964; height:297; z-index:2; top: 177px; left: 0px"> -->
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	</div>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<script type="text/javascript">
			 				 	
			 	function accionVolver() {
					document.forms[1].submit();
				}
			 	
			 	function accionGuardar() {
			 		var guardar = true;
			 		document.forms[0].incidenciaNumeroColegiadoDuplicadoRevisada.value = "false";
			 		document.forms[0].incidenciaPoblacionNoEncontradaRevisada.value = "false";
			 		document.forms[0].incidenciaInscritoRevisada.value = "false";
			 		
			 		if (${EdicionColegiadoForm.incidenciaNumeroColegiadoDuplicado} && '${EdicionColegiadoForm.ncolegiado}' == document.forms[0].ncolegiado.value) {
			 			guardar = confirm('<siga:Idioma key="censo.ws.ncolegiadoduplicado.confirmacion"/>');
			 			if (guardar) {
			 				document.forms[0].incidenciaNumeroColegiadoDuplicadoRevisada.value = "true";
			 			}
			 		}
			 		
			 		if (${EdicionColegiadoForm.incidenciaPoblacionNoEncontrada} && document.forms[0].descripcionpoblacion.value && document.forms[0].codigopoblacion.value) {
			 			
			 			if (confirm('<siga:Idioma key="censo.ws.poblacion.confirmacion"/>')) {
			 				document.forms[0].incidenciaPoblacionNoEncontradaRevisada.value = "true";
			 			}
			 		}
			 		
					if (${EdicionColegiadoForm.incidenciaInscrito} && '${EdicionColegiadoForm.idecomcensosituacionejer}' == document.forms[0].idecomcensosituacionejer.value) {
			 			
			 			if (confirm('<siga:Idioma key="censo.ws.inscrito.confirmacion"/>')) {
			 				document.forms[0].incidenciaInscritoRevisada.value = "true";
			 			}
			 		}
			 		
			 		if (guardar) {
				 		sub();
						document.forms[0].modo.value="modificar";
						document.forms[0].target="mainWorkArea";	
						document.forms[0].submit();
			 		}
			 	}
			 	
			 	function accionArchivar() {
			 		if (confirm('<siga:Idioma key="censo.ws.archivar.confirmacion"/>')) {
			 			sub();
						document.forms[0].modo.value="archivar";
						document.forms[0].target="mainWorkArea";	
						document.forms[0].submit();
			 		}
			 	}
			 	
							
				
				
			 </script>
			 
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>

</body>
</html>