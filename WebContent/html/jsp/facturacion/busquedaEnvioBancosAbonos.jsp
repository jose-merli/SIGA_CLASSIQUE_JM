<!DOCTYPE html>
<html>
<head>
<!-- busquedaEnvioBancosAbonos.jsp -->
<!-- 
	 VERSIONES:
	 Carlos Ruano Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*, java.util.*"%>

<!-- JSP -->

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAlto('resultado');">
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="/FAC_EnvioAbonosABanco.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">		
		<html:hidden styleId="modo"          				property="modo" 			value = ""/>
		<html:hidden styleId="actionModal"   				property="actionModal" 		value = ""/>
		<html:hidden styleId="idInstitucion" 				property="idInstitucion" />

		<tr>				
			<td>
				<siga:ConjCampos>			
					<table class="tablaCampos" align="center">
						<tr>
							<td class="labelText" >
								<bean:message key="general.etiqueta.fecha" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<siga:Fecha nombreCampo="fechaDesde"/>
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<siga:Fecha nombreCampo="fechaHasta"/>
							</td>
						</tr>	
						
						<tr>
							<td class="labelText">
								<bean:message key="facturacion.cuentasBancarias.banco" />
							</td>
							<td colspan="4">
								<html:select  styleId="codigoBanco" property="codigoBanco" styleClass="boxCombo" style="width:510px;">
									<html:option value=""/>
									<c:forEach items="${listaBancos}" var="banco">
										<html:option value="${banco.codigo}"><c:out value="${banco.nombre}"/></html:option>
									</c:forEach>
								</html:select>
							</td>
						</tr>
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioAbonos.literal.nAbonos" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="abonosDesde" property="abonosDesde" size="10" maxlength="10" styleClass="box" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="abonosHasta" property="abonosHasta" size="10" maxlength="10" styleClass="box" />
							</td>				
						</tr>	
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioPagos.literal.importeTotalRemesa" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="importesDesde" property="importesDesde" size="10" maxlength="10" styleClass="box" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="importesHasta" property="importesHasta" size="10" maxlength="10" styleClass="box" />
							</td>				
						</tr>	
					</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</html:form>
	</table>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

	<siga:ConjBotonesBusqueda botones="B"/>


	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		function buscar(){
			sub();
			
			if(!isNumero(document.ficheroBancarioAbonosForm.abonosDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.abonosDesde"/>');
				return false;
			}
			
			if(!isNumero(document.ficheroBancarioAbonosForm.abonosHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.abonosHasta"/>');
				return false;
			}
			
			if(!isNumero(document.ficheroBancarioAbonosForm.importesDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesDesde"/>');
				return false;
			}
			
			if(!isNumero(document.ficheroBancarioAbonosForm.importesHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesHasta"/>');
				return false;
			}			
			
			if(document.ficheroBancarioAbonosForm.abonosHasta.value != "" && document.ficheroBancarioAbonosForm.abonosDesde.value != "" && document.ficheroBancarioAbonosForm.abonosDesde.value > document.ficheroBancarioAbonosForm.abonosHasta.value){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.error.abonos"/>');
				return false;
			}
			
			if(document.ficheroBancarioAbonosForm.importesHasta.value != "" && document.ficheroBancarioAbonosForm.importesDesde.value != "" && document.ficheroBancarioAbonosForm.importesDesde.value > document.ficheroBancarioAbonosForm.importesHasta.value){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.error.importes"/>');
				return false;
			}			
			
			document.ficheroBancarioAbonosForm.modo.value="buscarInit";
			document.ficheroBancarioAbonosForm.target="resultado";	
			document.ficheroBancarioAbonosForm.submit();	
				
		}
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
