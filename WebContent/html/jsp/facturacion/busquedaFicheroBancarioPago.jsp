<!DOCTYPE html>
<html>
<head>
<!-- busquedaFicheroBancarioPago.jsp -->
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
	<siga:Titulo titulo="menu.facturacion.disquetecargos" localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAlto('resultado');">

<%
String app=request.getContextPath();
String action=app+"/FAC_DisqueteCargos.do?noReset=true";
%>
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="/FAC_DisqueteCargos.do?noReset=true" method="POST" target="submitArea" style="display:none">				
		<html:hidden styleId="modo"          				property="modo" 			value = ""/>
		<html:hidden styleId="actionModal"   				property="actionModal" 		value = ""/>
		<html:hidden styleId="idInstitucion" 				property="idInstitucion" />
		<html:hidden property="fechaCargo" />		

		<tr>				
			<td>
				<siga:ConjCampos>			
					<table class="tablaCampos" align="center">
						<tr>						
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioPagos.literal.descripcion" />
							</td>				
							<td>
								<html:text styleId="descripcion" property="descripcion" size="40" styleClass="box" />
							</td>						
						
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
								<siga:Fecha nombreCampo="fechaHasta" campoCargarFechaDesde="fechaDesde"/>
							</td>
						</tr>	
						
						<tr>
							<td class="labelText">
								<bean:message key="facturacion.ficheroBancarioPagos.literal.Origen"/>
							</td>
							<td>
								<html:select styleId="origen" property="origen" styleClass="boxCombo" style="width:265px;" onchange="habilitarSerie();">
									<html:option value=""/>
									<html:option value="1">	Facturacion de serie</html:option>
									<html:option value="2">	Facturas sueltas y renegociaciones </html:option>
								</html:select>	
							</td>
							
							<td class="labelText">
								<bean:message key="censo.facturacion.facturas.literal.Serie"/>
							</td>
							<td id="comboserie" colspan="3" style="display:none;">
								<siga:Select id="idSerieFacturacion" queryId="getSeriesFacturacion" width="200" />
							</td>							
						</tr>						
						
						<tr>
							<td class="labelText">
								<bean:message key="facturacion.ficheroBancarioPagos.literal.banco" />
							</td>
							<td colspan="4">
								<html:select  styleId="codigoBanco" property="codigoBanco" styleClass="boxCombo" style="width:515px;">
									<html:option value=""/>
									<c:forEach items="${listaBancos}" var="banco">
										<html:option value="${banco.codigo}"><c:out value="${banco.nombre}"/></html:option>
									</c:forEach>
								</html:select>
							</td>
						</tr>
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioPagos.literal.nRecibos" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="recibosDesde" property="recibosDesde" size="10" maxlength="6" styleClass="boxNumber" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="recibosHasta" property="recibosHasta" size="10" maxlength="6" styleClass="boxNumber" />
							</td>				
						</tr>	
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioPagos.literal.importeTotalRemesa" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="importesDesde" property="importesDesde" size="10" styleClass="boxNumber" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="importesHasta" property="importesHasta" size="10" styleClass="boxNumber" />
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

	<siga:ConjBotonesBusqueda botones="FG,B"/>


	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		jQuery.noConflict();
		
		jQuery("#recibosDesde").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});
		
		jQuery("#recibosHasta").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});		
	
		function habilitarSerie(){
			if(document.ficheroBancarioPagosForm.origen.value == 1){
				jQuery("#comboserie").show();
			}else{
				jQuery("#comboserie").hide();
			}
		}
		
		function refrescarLocal() {
			//buscar();
		}
	
		function buscar(){
			sub();
			
			if(!isNumero(document.ficheroBancarioPagosForm.recibosDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaFicheroRecibos.errorformato.recibosDesde"/>');
				return false;
			}
			
			if(!isNumero(document.ficheroBancarioPagosForm.recibosHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaFicheroRecibos.errorformato.recibosHasta"/>');
				return false;
			}
			
			if(!validaFloat(document.ficheroBancarioPagosForm.importesDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesDesde"/>');
				return false;
			}
			
			if(!validaFloat(document.ficheroBancarioPagosForm.importesHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesHasta"/>');
				return false;
			}			
			
			if(document.ficheroBancarioPagosForm.recibosHasta.value != "" && document.ficheroBancarioPagosForm.recibosDesde.value != "" && parseInt(document.ficheroBancarioPagosForm.recibosDesde.value) > parseInt(document.ficheroBancarioPagosForm.recibosHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaFicheroRecibos.error.recibos"/>');
				return false;
			}
			
			if(document.ficheroBancarioPagosForm.importesHasta.value != "" && document.ficheroBancarioPagosForm.importesDesde.value != "" && parseFloat(document.ficheroBancarioPagosForm.importesDesde.value) > parseFloat(document.ficheroBancarioPagosForm.importesHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.error.importes"/>');
				return false;
			}			
			
			document.ficheroBancarioPagosForm.modo.value="buscarInit";
			document.ficheroBancarioPagosForm.target="resultado";	
			document.ficheroBancarioPagosForm.submit();	
				
		}
		
		function generarFichero() {	
		    sub();	
			var resultado = ventaModalGeneral(document.all.confirmarFacturacionForm.name,"M");
			fin();
			if (resultado!=undefined) {								
				var resultadoFinal = resultado[0];
				if (resultadoFinal!='') {
					var sms = "<siga:Idioma key='facturacion.ficheroBancarioPagos.mensaje.generacionDisquetesOK' arg0='" + resultadoFinal + "'/>";
					alert(sms);
					buscar();
				}
			}
		}
			
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"> </iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->
	
	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="submitArea">		
		<html:hidden name="confirmarFacturacionForm" property="modo" value = "nuevo"/>
		<html:hidden name="confirmarFacturacionForm" property="actionModal" value = ""/>
	</html:form>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
