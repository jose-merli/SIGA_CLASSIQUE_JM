<!DOCTYPE html>
<html>
<head>
<!-- busquedaDevoluciones.jsp -->
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
	<siga:Titulo titulo="facturacion.consultaDevolucion.cabecera" localizacion="facturacion.datosDevoluciones.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="init();ajusteAlto('resultado');">
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/FAC_Devoluciones.do?noReset=true" method="POST" target="mainWorkArea" style="display:none" >
		<html:hidden styleId="modo"          				property="modo" 			value = ""/>
		<html:hidden styleId="actionModal"   				property="actionModal" 		value = ""/>
		<html:hidden styleId="idInstitucion" 				property="idInstitucion" />

		<tr>				
			<td>
				<siga:ConjCampos>			
					<table class="tablaCampos" align="center">
						<tr>
							<td class="labelText">
								<bean:message key="facturacion.busquedaDevolucion.tipodevolucion" />
							</td>

							<td>
								<html:select styleId="tipoDevolucion" property="tipoDevolucion" styleClass="boxCombo">
									<html:option value="0">	Discos bancarios </html:option>
									<html:option value="1">	Manuales </html:option>
								</html:select>	
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
								<siga:Fecha nombreCampo="fechaHasta"/>
							</td>
						</tr>	
						
						<tr>
							<td class="labelText">
								<bean:message key="facturacion.cuentasBancarias.banco" />
							</td>
							<td colspan="3">
								<html:select  styleId="codigoBanco" property="codigoBanco" styleClass="boxCombo" style="width:400px;">
									<html:option value=""/>
									<c:forEach items="${listaBancos}" var="banco">
										<html:option value="${banco.codigo}"><c:out value="${banco.nombre}"/></html:option>
									</c:forEach>
								</html:select>
							</td>
						</tr>
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.consultaDevolucion.literal.comisiones" />
							</td>
							<td>
								<html:select styleId="comision" property="comision" styleClass="boxCombo">
									<html:option value=""/>
									<html:option value="1">	<siga:Idioma key="general.boton.no" />	</html:option>
									<html:option value="2">	<siga:Idioma key="general.boton.yes" />	</html:option>
								</html:select>	
							</td>
												
							<td class="labelText" >
								<bean:message key="facturacion.consultaDevolucion.literal.nFacturas" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="facturasDesde" property="facturasDesde" size="10" maxlength="10" styleClass="boxNumber" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="facturasHasta" property="facturasHasta" size="10" maxlength="10" styleClass="boxNumber" />
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

	<siga:ConjBotonesBusqueda botones="PNF,B"/>


	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		jQuery.noConflict();
		
		jQuery("#facturasDesde").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});
		
		jQuery("#facturasHasta").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});
		
		function init(){
			<% 	if (request.getAttribute("buscar") != null && request.getAttribute("buscar").equals("true")) { %>
				buscar();
			<% 	}  %>
		}
	
		function buscar(){
			sub();
			
			if(!isNumero(document.DevolucionesForm.facturasDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaDevolucion.errorformato.facturasdesde"/>');
				return false;
			}
			
			if(!isNumero(document.DevolucionesForm.facturasHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaDevolucion.errorformato.facturashasta"/>');
				return false;
			}
			
			if(document.DevolucionesForm.facturasHasta.value != "" && document.DevolucionesForm.facturasDesde.value != "" && parseInt(document.DevolucionesForm.facturasDesde.value) > parseInt(document.DevolucionesForm.facturasHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaDevolucion.error.facturas"/>');
				return false;
			}
			
			document.DevolucionesForm.modo.value="buscarInit";
			document.DevolucionesForm.target="resultado";	
			document.DevolucionesForm.submit();	
				
		}
		
		// Funcion asociada a boton procesarNuevoFichero
		function procesarNuevoFichero() {
			document.forms[0].modo.value='nuevo';							
			var resultado = ventaModalGeneral("DevolucionesForm","M");
			if (resultado=="MODIFICADO") {
				refrescarLocal();
			}
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
