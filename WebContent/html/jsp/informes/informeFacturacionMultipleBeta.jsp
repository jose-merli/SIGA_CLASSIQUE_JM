<!-- informeFacturacionMultipleBeta.jsp -->

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
	String informeUnico=(String)request.getAttribute("informeUnico");	
	String optionsInstituciones="";
	boolean esConsejo=false;
	int idConsejo = new Integer(sInstitucion).intValue();
	if (idConsejo == ClsConstants.INSTITUCION_CONSEJOGENERAL || idConsejo >= ClsConstants.INSTITUCION_CONSEJO) {
		esConsejo=true;
		optionsInstituciones=(String)request.getAttribute("optionsInstituciones");
	}
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>		
	
		<!-- Titulo y localizacion -->
		<siga:Titulo titulo="menu.justiciaGratuita.informes.informeMultipleNuevo" localizacion="factSJCS.informes.ruta" />
		
		<!-- Scripts de botones -->
		<script type="text/javascript">
			jQuery.noConflict();
		
			// Funcion asociada a boton Generar Informe
			function accionGenerarInforme() {
				var idFactIni = document.getElementById("idFacturacionInicio").value;
				var idFactFin = document.getElementById("idFacturacionFin").value;
				var idInstitucion = <%=sInstitucion%>
				<% if (esConsejo) { %>
					idInstitucion = document.getElementById("colegioFacturacion").value;
				<% } %>
					
				
				if (idFactIni != "") {					
					if (idFactFin == "")
						idFactFin=idFactIni;						
					
					var grupoFact =  document.getElementById("grupoFacturacion").value;
					var datos = "idFacturacionIni" + "==" + idFactIni + "##" + "idFacturacionFin" + "==" + idFactFin + "##"+ "grupoFacturacion" + "==" + grupoFact + "##"+ "idInstitucion" + "==" + idInstitucion;
					
					document.InformesGenericosForm.datosInforme.value=datos;
					
					if(document.getElementById("informeUnico").value=='1'){
						sub();
						document.InformesGenericosForm.submit();
						
					} else {
						var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
						/*
						if (arrayResultado==undefined||arrayResultado[0]==undefined){
						   		
					   	}else {
					   						
						}
						*/
					}
					
				} else {
					alert("<siga:Idioma key='factSJCS.informes.informeMultiple.sinfacturacion'/>");
					fin();
					return false;
				} 
			}
			
			function postAccionTurno () {
				jQuery('#idFacturacionFin option').remove();
				jQuery("#idFacturacionFin").removeAttr("disabled");
				
				jQuery('#idFacturacionFin').append('<option value=""></option>');
				jQuery("#idFacturacionInicio option").clone().appendTo(jQuery('#idFacturacionFin'));
				
				if(jQuery("#idFacturacionInicio").attr("disabled") == "disabled")
					jQuery("#idFacturacionFin").attr("disabled", "disabled");
			}
		</script>				
	</head>

	<body>
		<!-- Campos -->
		<siga:ConjCampos leyenda="menu.justiciaGratuita.informes.informeMultipleNuevo">
			<input type="hidden" id= "informeUnico" value="<%=informeUnico%>" />	
			<input type="hidden" id= "idInstitucion" value="<%=sInstitucion%>" />		

			<table class="tablaCampos" align="center" border="0">
			
				<% if (esConsejo) { %>
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.institucion"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="colegioFacturacion">
								<%=optionsInstituciones%>
							</select>
						</td>
					</tr>							
				
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.grupo"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="grupoFacturacion" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.factInicial" />&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px" id="idFacturacionInicio" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.factFinal" />
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="idFacturacionFin" disabled>
							</select>											
						</td>
					</tr>															
						
					<ajax:select
						baseUrl="/SIGA/INF_InformesMultiplesBeta.do"
						source="idInstitucion" target="colegioFacturacion" 
						parameters="idInstitucion={idInstitucion},modo=ajaxObtenerInstituciones"
						executeOnLoad="true"/>					
					
					<ajax:select
						baseUrl="/SIGA/INF_InformesMultiplesBeta.do"
						source="colegioFacturacion" target="grupoFacturacion" 
						parameters="idInstitucion={colegioFacturacion},modo=ajaxObtenerTurnos"/>
						
					<ajax:select
						baseUrl="/SIGA/INF_InformesMultiplesBeta.do"
						source="grupoFacturacion" target="idFacturacionInicio"
						parameters="idInstitucion={colegioFacturacion},idGrupo={grupoFacturacion},modo=ajaxObtenerFacturas"
						postFunction="postAccionTurno" />							

				<% 	} else { %>
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.grupo"/>&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="grupoFacturacion">
							</select>						
						</td>
					</tr>	
					
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.factInicial" />&nbsp;(*)
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px" id="idFacturacionInicio" disabled>
							</select>						
						</td>
					</tr>
					
					<tr>
						<td class="labelText" width="140px" style="vertical-align: middle;">
							<siga:Idioma key="factSJCS.informes.informeMultiple.factFinal" />
						</td>
						<td style="vertical-align: middle;">
							<select styleClass="boxCombo" style="width:800px;" id="idFacturacionFin" disabled>
							</select>											
						</td>
					</tr>							
					
					<ajax:select
						baseUrl="/SIGA/INF_InformesMultiplesBeta.do"
						source="idInstitucion" target="grupoFacturacion" 
						parameters="idInstitucion={idInstitucion},modo=ajaxObtenerTurnos"
						executeOnLoad="true"/>			
												
					<ajax:select
						baseUrl="/SIGA/INF_InformesMultiplesBeta.do"
						source="grupoFacturacion" target="idFacturacionInicio"
						parameters="idInstitucion={idInstitucion},idGrupo={grupoFacturacion},modo=ajaxObtenerFacturas"
						postFunction="postAccionTurno" />								
				<% } %>			
				
				<tr>
					<td class="labelText" colspan="2" style="vertical-align: middle;">
						<siga:Idioma key="factSJCS.informes.informeMultiple.mensaje" />
					</td>
				</tr>				
			</table>
		</siga:ConjCampos>

		<!-- Formularios -->
		<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
			<html:hidden property="idInstitucion" value="<%=sInstitucion%>"/>
			<html:hidden property="idTipoInforme" value="FACJ2"/>
			<html:hidden property="enviar" value="0"/>
			<html:hidden property="descargar" value="1"/>
			<html:hidden property="datosInforme"/>
			<html:hidden property="modo" value = "preSeleccionInformes"/>
			<input type='hidden' name='actionModal' />
		</html:form>
		
		<!-- Formulario para la edicion del envio -->
		<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
			<input type="hidden" name="modo" value="">
			<input type="hidden" name="tablaDatosDinamicosD" value="">
		</form>

		<!-- Botones -->
		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM" />

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>