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
<% 
	String app=request.getContextPath();
	boolean abonosSJCS = false;
	String sjcs = request.getParameter("sjcs");
	if ((sjcs!=null) && (sjcs.equals("1"))){
		abonosSJCS = true;
	}
%>	

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<% if(abonosSJCS){ %>
		<siga:Titulo titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" localizacion="factSJCS.Pagos.localizacion"/>
	<% } else { %>
		<siga:Titulo titulo="facturacion.ficheroBancarioAbonos.literal.cabecera" localizacion="facturacion.localizacion"/>
	<% } %>
	<!-- FIN: TITULO Y LOCALIZACION -->
		<style type="text/css">
		.ui-dialog-titlebar-close {
				  visibility: hidden;
				}
	</style>
	
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
		<% if (abonosSJCS){ %>
			<input type="hidden" name="sjcs" value="<%=sjcs%>">
		<%} else { %>
			<input type="hidden" name="sjcs" value="">
			<html:hidden property="listaSufijoProp"/>
		<%} %>

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
								<html:text styleId="abonosDesde" property="abonosDesde" size="10" maxlength="10" styleClass="boxNumber" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="abonosHasta" property="abonosHasta" size="10" maxlength="10" styleClass="boxNumber" />
							</td>				
						</tr>	
						
						<tr>	
							<td class="labelText" >
								<bean:message key="facturacion.ficheroBancarioPagos.literal.importeTotalRemesa" />&nbsp;<bean:message key="general.etiqueta.desde" /> 
							</td>				
							<td>
								<html:text styleId="importesDesde" property="importesDesde" size="10" maxlength="10" styleClass="boxNumber" />
							</td>
				
							<td class="labelText">
								<bean:message key="general.etiqueta.hasta" /> 
							</td>
							<td>
								<html:text styleId="importesHasta" property="importesHasta" size="10" maxlength="10" styleClass="boxNumber" />
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

	<siga:ConjBotonesBusqueda botones="FA,B"/>


	<!-- FIN: BOTONES BUSQUEDA -->
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		jQuery.noConflict();
		
		function refrescarLocal() {
			//buscar();
		}		
		
		jQuery("#abonosDesde").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});
		
		jQuery("#abonosHasta").keypress(function (e) {
			if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57))    
				return false;
		});	
	
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
			
			if(!validaFloat(document.ficheroBancarioAbonosForm.importesDesde.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesDesde"/>');
				return false;
			}
			
			if(!validaFloat(document.ficheroBancarioAbonosForm.importesHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.errorformato.importesHasta"/>');
				return false;
			}			
			
			if(document.ficheroBancarioAbonosForm.abonosHasta.value != "" && document.ficheroBancarioAbonosForm.abonosDesde.value != "" && parseInt(document.ficheroBancarioAbonosForm.abonosDesde.value) > parseInt(document.ficheroBancarioAbonosForm.abonosHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.error.abonos"/>');
				return false;
			}
			
			if(document.ficheroBancarioAbonosForm.importesHasta.value != "" && document.ficheroBancarioAbonosForm.importesDesde.value != "" && parseFloat(document.ficheroBancarioAbonosForm.importesDesde.value) > parseFloat(document.ficheroBancarioAbonosForm.importesHasta.value)){
				fin();
				alert('<siga:Idioma key="facturacion.busquedaEnvioAbonos.error.importes"/>');
				return false;
			}			
			
			document.ficheroBancarioAbonosForm.modo.value="buscarInit";
			document.ficheroBancarioAbonosForm.target="resultado";	
			document.ficheroBancarioAbonosForm.submit();	
				
		}
		
		function generarFichero() {

			if(!confirm('<siga:Idioma key="facturacion.ficheroBancarioAbonos.literal.confirmarFicheroAbonos"/>')) {
				return false;
			}
			
			<% if(!abonosSJCS){%>
			jQuery("#dialogoConfig").dialog(
					{
					      height: 270,
					      width: 525,
					      modal: true,
					      resizable: false,
					      buttons: {					          	
					            "Guardar y Cerrar": function() {
					            	configurarFichNoSJCS();
					            	jQuery( this ).dialog( "close" );
					            }
					          }
					    }
				);
				jQuery(".ui-widget-overlay").css("opacity","0");
				
			<%} else { %>

				document.all.ficheroBancarioAbonosForm.modo.value = "generarFichero";
				document.all.ficheroBancarioAbonosForm.target = 'submitArea';
				var f = document.all.ficheroBancarioAbonosForm.name;	
				window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioAbonos.mensaje.generandoFicheros';
			<% } %>
			
		}
		
		function configurarFichNoSJCS(){
			
			var sufijo = jQuery("#comboSufijos");				
			var propositoSEPA = jQuery("#comboPropositosSEPA");
			var propositoOtros = jQuery("#comboPropositosOtros");
			
			if(sufijo.val()<1) {
				mensaje = "<siga:Idioma key='facturacion.sufijos.message.errorCuentaBancariaSufijoNOSJCS'/>";
				alert(mensaje);
				fin();
				return false;
			}
				
			if(propositoSEPA.val()<1) {
				mensaje = "<siga:Idioma key='facturacion.propositos.message.errorPropSEPA'/>";
				alert(mensaje);
				fin();
				return false;
			}
			
			if(propositoOtros.val()<1) {
				mensaje = "<siga:Idioma key='facturacion.propositos.message.errorPropOtros'/>";
				alert(mensaje);
				fin();
				return false;
			}
			
			document.ficheroBancarioAbonosForm.listaSufijoProp.value=sufijo.val()+"#"+propositoSEPA.val()+"#"+propositoOtros.val();
			document.all.ficheroBancarioAbonosForm.modo.value = "generarFichero";
			document.all.ficheroBancarioAbonosForm.target = 'submitArea';
			var f = document.all.ficheroBancarioAbonosForm.name;	
			window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioAbonos.mensaje.generandoFicheros';

			
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
	<div id="dialogoConfig" title="Nuevo Fichero de Abonos" style="display:none">
	<div>
		<siga:ConjCampos>
			<table class="tablaCampos" colspan=2>
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.sufijos.literal.sufijo"/>
					</td>
					<td>
					<bean:define id="listaSufijos" name="listaSufijos" scope="request"/> 
					<html:select styleId="comboSufijos" property="idSufijo" value="" styleClass="boxCombo" style="width:150px;">
					<html:option value=""><c:out value=""/></html:option>
					<c:forEach items="${listaSufijos}" var="sufijoCmb">
						<html:option value="${sufijoCmb.idSufijo}"><c:out value="${sufijoCmb.sufijo.trim().length()>0?sufijoCmb.sufijo:'   '} ${sufijoCmb.concepto}"/></html:option>
					</c:forEach>
					</html:select>	
					</td>
				</tr> 
				<tr>		
					<td class="labelText">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.SEPA"/>
					</td>
					<td>
						<bean:define id="listaPropositosSEPA" name="listaPropositosSEPA" scope="request"/>
						<html:select styleId="comboPropositosSEPA" property="idpropSEPA" value="" styleClass="boxCombo" style="width:150px;" >
						<html:option value=""><c:out value=""/></html:option>
						<c:forEach items="${listaPropositosSEPA}" var="propSEPACmb">
							<html:option value="${propSEPACmb.idProposito}"><c:out value="${propSEPACmb.codigo.trim().length()>0?propSEPACmb.codigo:'   '} ${propSEPACmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					
					</td>
				</tr>
				<tr>
					<td class="labelText"> 
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.otras"/>
					</td>
					<td>
						<bean:define id="listaPropositosOtros" name="listaPropositosOtros" scope="request"/>
						<html:select styleId="comboPropositosOtros" property="idpropOtros" value="" styleClass="boxCombo" style="width:150px;" >
						<html:option value=""><c:out value=""/></html:option>
						<c:forEach items="${listaPropositosOtros}" var="propOtrosCmb">
							<html:option value="${propOtrosCmb.idProposito}"><c:out value="${propOtrosCmb.codigo.trim().length()>0?propOtrosCmb.codigo:'   '} ${propOtrosCmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</div>
	</div>
</body>
</html>
