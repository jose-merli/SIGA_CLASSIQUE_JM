<!DOCTYPE html>
<html>
<head>
<!-- busquedaConfirmaciones.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	// paraemtro para consultas de estados (Combo)
	String dato[] = new String[1];
	dato[0] = user.getLanguage().toUpperCase();
	ArrayList estadoConfirmacionSel = new ArrayList();
	boolean volver = false;
	
	if (request.getAttribute("volver") != null && request.getAttribute("volver").equals("s")) {
		volver = true;
		if(request.getAttribute("estadoConfirmacion") != null && !((String)request.getAttribute("estadoConfirmacion")).equals("")){
			estadoConfirmacionSel.add((String)request.getAttribute("estadoConfirmacion"));
		}else{
			estadoConfirmacionSel.add(FacEstadoConfirmFactBean.GENERADA.toString());
		}		
	}else{
		//La primera vez que se carga la página
		//estadoConfirmacionSel.add(FacEstadoConfirmFactBean.GENERADA.toString()); //SE QUITA EL VALOR POR DEFECTO DEL COMBO.	
	}
%>	
	


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="facturacion.confirmarFacturacion.literal.cabecera" 	localizacion="facturacion.confirmarFacturacion.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Validaciones en Cliente -->
	<html:javascript formName="confirmarFacturacion1Form" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>


<body onLoad="ajusteAlto('resultado');">


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

<table class="tablaCentralCampos" align="center">

	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property="actionModal" value=""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		
		<tr><td>
			<siga:ConjCampos leyenda="facturacion.confirmarFacturacion.literal.camposBusqueda">	
			<table class="tablaCampos" align="center">
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.estado"/></td>
					<td>
						<siga:ComboBD nombre = "estadoConfirmacion" tipo="cmbEstadoConfirmacion" ancho="100" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" accion="accionEstado()" elementoSel="<%=estadoConfirmacionSel%>" />
					</td>
					
					<td  class="labelText" width="190px"><siga:Idioma key="facturacion.mantenimnientoFacturacion.literal.fPrevistaGeneracion"/>&nbsp;<siga:Idioma key="general.literal.desde"/>&nbsp;(*)</td>
					<td>
						<siga:Fecha styleId="fechaDesdePrevistaGeneracion" nombreCampo="fechaDesdePrevistaGeneracion" valorInicial="${confirmarFacturacionForm.fechaDesdePrevistaGeneracion}" anchoTextField="8"/>
					</td>
					<td class="labelText" width="80px"><siga:Idioma key="general.literal.hasta"/></td>
					<td>
						<siga:Fecha styleId="fechaHastaPrevistaGeneracion" nombreCampo="fechaHastaPrevistaGeneracion" anchoTextField="8"/>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.estadoTraspaso"/></td>
					<td>
						<siga:ComboBD nombre="estadoTraspaso" tipo="cmbEstadoTraspaso" ancho="100" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" />
					</td>
					
					<td  class="labelText" width="150px"><siga:Idioma key="facturacion.confirmarFacturacion.literal.fechaRealGeneracion"/>&nbsp;<siga:Idioma key="general.literal.desde"/></td>
					<td>
						<siga:Fecha styleId="fechaDesdeGeneracion" nombreCampo="fechaDesdeGeneracion" anchoTextField="8" />
					</td>
					<td class="labelText" width="40px"><siga:Idioma key="general.literal.hasta"/></td>
					<td>
						<siga:Fecha styleId="fechaHastaGeneracion" nombreCampo="fechaHastaGeneracion"  anchoTextField="8"/>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.estadoPDF"/></td>
					<td>
						<siga:ComboBD nombre = "estadoPDF" tipo="cmbEstadoPDF"  clase="boxCombo" ancho="100" obligatorio="false" parametro="<%=dato%>" />						
					</td>
					
					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.fechaConfirmacion"/>&nbsp;<siga:Idioma key="general.literal.desde"/></td>
					<td>
						<siga:Fecha styleId="fechaDesdeConfirmacion" nombreCampo="fechaDesdeConfirmacion" anchoTextField="8"/>
					</td>
					<td class="labelText"><siga:Idioma key="general.literal.hasta"/></td>
					<td>
						<siga:Fecha styleId="fechaHastaConfirmacion" nombreCampo="fechaHastaConfirmacion" anchoTextField="8"/>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.estadoEnvio"/></td>
					<td>
						<siga:ComboBD nombre = "estadoEnvios" tipo="cmbEstadoEnvios"  ancho="100"  clase="boxCombo" obligatorio="false" parametro="<%=dato%>"/>						
					</td>
					
					<td class="labelText" colspan="2"><siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.archivadas"/>&nbsp;<input type="checkbox" value="1" name="archivadas">
					</td>
					
					<td colspan="4">&nbsp;</td>
				</tr>
			</table>
		</siga:ConjCampos>	

		</td></tr>

	</html:form>
	
	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo=""/>

	<!-- FIN: BOTONES BUSQUEDA -->


	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		jQuery(document).ready(function () {		
			accionEstado();
			<%	if(volver) { %>	
				buscar();
			<% } %>
		});		
		
		function refrescarLocal() {
			buscar();
		}
		
		// Funcion asociada a boton buscar
		function buscar() {
			sub();	
			if (validateConfirmarFacturacion1Form(document.confirmarFacturacionForm)){
				setFilaSeleccionadaD('true');
				document.confirmarFacturacionForm.modo.value = "buscarInit";
				document.confirmarFacturacionForm.submit();
				setFilaSeleccionadaD('false');
				
			}else{			
				fin();
				return false;
			}
		}
		
		// Asociada al boton Nuevo
		function nuevo() {		
			document.confirmarFacturacionForm.target="mainWorkArea";
			document.confirmarFacturacionForm.modo.value = "nuevo";
			document.confirmarFacturacionForm.submit();
		}		

		function accionEstado() {
			if(jQuery("#estadoConfirmacion").val() == 18 || jQuery("#estadoConfirmacion").val() == 19|| jQuery("#estadoConfirmacion").val() == 20){
				jQuery('#fechaRealGeneracionConAsterisco').hide();
				jQuery('#fechaRealGeneracionSinAsterisco').show();
				jQuery("#fechaDesdeGeneracion").val(null);
				jQuery("#fechaHastaGeneracion").val(null);
				jQuery("#fechaDesdeConfirmacion").val(null);
				jQuery("#fechaHastaConfirmacion").val(null);
			} else{
				jQuery('#fechaRealGeneracionSinAsterisco').hide();
				jQuery('#fechaRealGeneracionConAsterisco').show();
			}
		}
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"				 
					class="frameGeneral">
	</iframe>
			
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>