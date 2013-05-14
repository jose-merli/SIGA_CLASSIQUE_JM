<!-- informeFichaCertificados.jsp-->

<!-- Pantalla con los combos para seleccionar los pagos a generar -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String sInstitucion=usrbean.getLocation();		
	boolean esConsejo=false;
	int idConsejo = new Integer(sInstitucion).intValue();
	if (idConsejo == ClsConstants.INSTITUCION_CONSEJOGENERAL || idConsejo >= ClsConstants.INSTITUCION_CONSEJO)
		esConsejo=true;
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>				
		
		<html:javascript formName="mantenimientoInformesForm" staticJavascript="false" />		
		
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo titulo="factSJCS.informes.certificados.cabecera" localizacion="factSJCS.informes.ruta" />
		
		<script language="JavaScript">
			jQuery.noConflict();					
		
			function accionGenerarInforme() {	
				sub();	

				var idInstitucion = <%=sInstitucion%>
				<% if (esConsejo) { %>
					idInstitucion = document.getElementById("colegioPagos").value;
				<% } %>
				var grupoPagos = document.getElementById("grupoPagos").value;
				var idPagoInicial = document.getElementById("idPagoInicial").value;
				var idPagoFinal = document.getElementById("idPagoFinal").value;
												
				var form = document.getElementById("mantenimientoInformesForm");
				form.idInstitucion.value = idInstitucion;
				form.idGrupo.value = grupoPagos;
				form.idPago.value = idPagoInicial;
				form.idPagoFin.value = idPagoFinal;
				
				if (form.idPago.value != "") {
			    	if (validateMantenimientoInformesForm(form)) {
			    		form.modo.value="generarCertificadoPago";
						// con pantalla de espera
						window.frames.submitArea.location='/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName='+form.name+'&msg=messages.factSJCS.procesandoInforme';
					} else {
						fin();
						return false;
					}
		
				} else {
					alert("<siga:Idioma key='informes.sjcs.pagos.literal.sinpago'/>");
					fin();
					return false;
				} 			 
			}
			
			function postAccionTurno () {
				jQuery('#idPagoFinal option').remove();
				jQuery("#idPagoFinal").removeAttr("disabled");
				
				jQuery('#idPagoFinal').append('<option value=""></option>');
				jQuery("#idPagoInicial option").clone().appendTo(jQuery('#idPagoFinal'));
				
				if(jQuery("#idPagoInicial").attr("disabled") == "disabled")
					jQuery("#idPagoFinal").attr("disabled", "disabled");
			}			
		</script>		
	</head>

	<body>
		<siga:ConjCampos leyenda="factSJCS.informes.certificados.cabecera">
			<input type="hidden" id= "idInstitucion" value="<%=sInstitucion%>" />

			<table class="tablaCentralCampos" align="center" border="0">		
				<% if (esConsejo) { %>
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.institucion"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="colegioPagos">
							</select>
						</td>
					</tr>							
				
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.grupo"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="grupoPagos" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.pago" />&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px" id="idPagoInicial" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.pagoFin" />
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="idPagoFinal" disabled>
							</select>											
						</td>
					</tr>																	
					
					<ajax:select
						baseUrl="/SIGA/INF_CertificadoPago.do"
						source="idInstitucion" target="colegioPagos" 
						parameters="idInstitucion={idInstitucion},modo=ajaxObtenerInstituciones"
						executeOnLoad="true"/>			
					
					<ajax:select
						baseUrl="/SIGA/INF_CertificadoPago.do"
						source="colegioPagos" target="grupoPagos" 
						parameters="idInstitucion={colegioPagos},modo=ajaxObtenerTurnos"/>
						
					<ajax:select
						baseUrl="/SIGA/INF_CertificadoPago.do"
						source="grupoPagos" target="idPagoInicial"
						parameters="idInstitucion={colegioPagos},idGrupo={grupoPagos},modo=ajaxObtenerPagos"
						postFunction="postAccionTurno" />							

				<% 	} else { %>				
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.grupo"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="grupoPagos">
							</select>						
						</td>
					</tr>	
					
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.pago" />&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px" id="idPagoInicial" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="110px" style="vertical-align: middle;">
							<siga:Idioma key="informes.sjcs.pagos.literal.pagoFin" />
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="idPagoFinal" disabled>
							</select>											
						</td>
					</tr>													
					
					<ajax:select
						baseUrl="/SIGA/INF_CertificadoPago.do"
						source="idInstitucion" target="grupoPagos" 
						parameters="idInstitucion={idInstitucion},modo=ajaxObtenerTurnos"
						executeOnLoad="true"/>			
												
					<ajax:select
						baseUrl="/SIGA/INF_CertificadoPago.do"
						source="grupoPagos" target="idPagoInicial"
						parameters="idInstitucion={idInstitucion},idGrupo={grupoPagos},modo=ajaxObtenerPagos"
						postFunction="postAccionTurno" />						
				<% } %>
				
				<tr>
					<td class="labelText" colspan="2" style="vertical-align: middle;">
						<siga:Idioma key="informes.sjcs.pagos.literal.mensaje" />
					</td>
				</tr>						
			</table>			
		</siga:ConjCampos>
		
		<html:form action="/INF_FichaFacturacion.do" method="POST" target="submitArea">
			<html:hidden name="mantenimientoInformesForm" property="modo" value="" />
			<html:hidden property="idInstitucion" value="<%=sInstitucion%>"/>
			<html:hidden property="idGrupo" value=""/>
			<html:hidden property="idPago" value=""/>
			<html:hidden property="idPagoFin" value=""/>
		</html:form>

<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
	 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
	 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
-->
		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM" />

		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	</body>
</html>