<!DOCTYPE html>
<html>
<head>
<!-- edicionActuacionAsistencia.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax"%>

<%
	// Consulto si tiene configurado la guardia por asistencias(0) o por actuaciones(1)
	String porAsAct = (String) request.getAttribute("porAsAct");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>

	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<script type="text/javascript">
		function preAccionTipoActuacion() {
			//alert("preAccionTipoActuacion");
			if(document.getElementById("tiposActuacion").value=='-1'){
				document.getElementById('tiposCosteFijoActuaciones').options.length = 0;
				return 'cancel';
			}
			document.ActuacionAsistenciaForm.idTipoAsistencia.value = document.ActuacionAsistenciaFormEdicion.idTipoAsistencia.value;
		}
		
		function postAccionTipoActuacion() {	
			//alert("postAccionTipoActuacion");
			document.getElementById('idCosteFijoActuacion').value = document.getElementById('auxIdCosteFijoActuacion').value ;
			document.getElementById('auxIdCosteFijoActuacion').value = '';
			if(document.ActuacionAsistenciaFormEdicion.modo.value =='ver' || document.ActuacionAsistenciaFormEdicion.validada.value==1 ||  document.ActuacionAsistenciaFormEdicion.anulacion.value==1){
				if (jQuery("#idCosteFijoActuacion").length <= 0){
					jQuery("#tiposCosteFijoActuaciones").after('<input type="hidden" value="" id="idCosteFijoActuacion"/>');
				}
				if (jQuery("#tiposCosteFijoActuaciones_txt").lenght <= 0){					
					jQuery("#tiposCosteFijoActuaciones").after('<input type="text" id="tiposCosteFijoActuaciones_txt" readonly class="boxConsulta" value="" style="width:680px;" />');
					jQuery("#tiposCosteFijoActuaciones").hide();
				}
				if (jQuery("#tiposCosteFijoActuaciones").val() != undefined && jQuery("#tiposCosteFijoActuaciones").val() != "-1"){
					jQuery("#tiposCosteFijoActuaciones_txt").val(jQuery("#tiposCosteFijoActuaciones").find("option[value="+jQuery("#tiposCosteFijoActuaciones").val()+"]").text());
				}
				jQuery("#idCosteFijoActuacion").val(jQuery("#tiposCosteFijoActuaciones").val());
				jQuery("#idCosteFijoActuacion").attr("disabled","disabled");
			} else {
				jQuery("#idCosteFijoActuacion").removeAttr("disabled");

			}
		}	
		
		function obtenerJuzgado() { 
		  	if (document.getElementById("codJuzgado").value!=""){
		  		if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").hide();
					jQuery("#asteriscoJuzgado").show();
				}
				document.MantenimientoJuzgadoForm.codigoExt2.value=document.getElementById("codJuzgado").value;
			   	document.MantenimientoJuzgadoForm.submit();			   					
		 	}
		 	else{
		 		document.getElementById("idJuzgado").value=-1;
		 		if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").show();
					if(document.getElementById("idJuzgado").value =="" && document.getElementById("idComisaria").value ==""){
						jQuery("#asteriscoComisaria").show();			
					}else{
						jQuery("#asteriscoJuzgado").hide();
					}
				}
		 	}
		 	
		 	document.getElementById("idComisaria").value="";
			document.getElementById("codComisaria").value="";
		}
		
		function traspasoDatos(resultado) {
			if (resultado[0]==undefined) {
				document.getElementById("idJuzgado").value=-1;
				document.getElementById("codJuzgado").value = "";
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").show();
				}
			} 
			else {
				if(resultado && resultado.length > 0){
					var fin = resultado[0].indexOf(',');
					if (fin != -1) 
						document.getElementById("idJuzgado").value=resultado[0].substring(0,fin);
				}
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").hide();
				}
			}	
		}		
		
		function cambioJuzgado(){	
			var comboJuzgado = document.getElementById("idJuzgado");	
			if(comboJuzgado.value!=""){
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").hide();
					jQuery("#asteriscoJuzgado").show();
				}
				document.getElementById("idComisaria").value="";
				document.getElementById("codComisaria").value="";
			
				jQuery.ajax({
		   			type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado3",
					data: "idCombo="+comboJuzgado.value,
					dataType: "json",
					success: function(json){		
			       		document.getElementById("codJuzgado").value = json.codigoExt2;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicacion: ' + e);
						fin();
					}
				});
			}		
			else{
				document.getElementById("codJuzgado").value = "";
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoComisaria").show();
				
						if(document.getElementById("idJuzgado").value =="" && document.getElementById("idComisaria").value ==""){
							jQuery("#asteriscoJuzgado").show();			
						}else{
							jQuery("#asteriscoJuzgado").hide();
						}
				}
			}
		}		
		
		function obtenerComisaria() { 
			if (document.getElementById("codComisaria").value!=""){
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoJuzgado").hide();
					jQuery("#asteriscoComisaria").show();
				}
				document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.getElementById("codComisaria").value;
				document.MantenimientoComisariaForm.submit();	
			 }
			 else{
					document.getElementById("idComisaria").value=-1;
					if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
						jQuery("#asteriscoJuzgado").show();
						if(document.getElementById("idJuzgado").value =="" && document.getElementById("idComisaria").value ==""){
							jQuery("#asteriscoComisaria").show();			
						}else{
							jQuery("#asteriscoComisaria").hide();
						}
					}
			 }
					
			document.getElementById("idJuzgado").value="";
			document.getElementById("codJuzgado").value="";	
		}
			
		function traspasoDatosComisaria(resultado){
			if (resultado[0]==undefined) {
				document.getElementById("idComisaria").value=-1;
				document.getElementById("codComisaria").value = "";
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoJuzgado").show();
				}
			} 
			else {
				if(resultado && resultado.length > 0){
					var fin = resultado[0].indexOf(',');
					if (fin != -1) 
						document.getElementById("idComisaria").value=resultado[0].substring(0,fin);
				}
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoJuzgado").hide();
				}
			}
		}
		
		function cambioComisaria(){
			var comboComisaria = document.getElementById("idComisaria");
			if(comboComisaria.value!=""){
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoJuzgado").hide();
					jQuery("#asteriscoComisaria").show();
				}
				document.getElementById("idJuzgado").value="";
				document.getElementById("codJuzgado").value="";
			
				jQuery.ajax({ 
		   			type: "POST",
					url: "/SIGA/GEN_Comisarias.do?modo=getAjaxComisaria2",
					data: "idCombo="+comboComisaria.value,
					dataType: "json",
					success: function(json){		
			       		document.getElementById("codComisaria").value = json.codigoExt;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicacion: ' + e);
						fin();
					}
				});
			}
			else{
				document.getElementById("codComisaria").value = "";
				if(document.ActuacionAsistenciaFormEdicion.tipoPcajg.value == 9)  {
					jQuery("#asteriscoJuzgado").show();	
						if(document.getElementById("idJuzgado").value =="" && document.getElementById("idComisaria").value ==""){
							jQuery("#asteriscoComisaria").show();			
						}else{
							jQuery("#asteriscoComisaria").hide();
						}
				}
			}
		}	
		jQuery(function($){
			var defaultValue = jQuery("#nig").val();
			if(defaultValue.length > 19){
				jQuery('#info').show();
				jQuery('#imagenInfo').attr('title',defaultValue) ;
			}else{
				jQuery('#info').hide();
				
			}
			jQuery("#nig").mask("AAAAA AA A AAAA AAAAAAA");
			jQuery("#nig").keyup();	
		});	
	</script>
</head>

<body onload="inicio();cambioComisaria();cambioJuzgado();">

	<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean" />
	<input type="hidden" id ="idConsejo" value = "${usrBean.idConsejo}"/>
	<bean:define id="botones" name="botones" scope="request" />
	<bean:define id="tipoPcajg" name="tipoPcajg" scope="request" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />

	<div id="datosModal" style='position:absolute; width:100%; overflow-y:auto'>
	
	<html:form action="${path}">
		<html:hidden property="modo" />
		<html:hidden property="idTipoAsistencia" />
	</html:form>

	<html:javascript formName="ActuacionAsistenciaFormEdicion" staticJavascript="true" />
		
	<html:form action="${path}" name="ActuacionAsistenciaFormEdicion" type="com.siga.gratuita.form.ActuacionAsistenciaForm" method="POST" target="submitArea" enctype="multipart/form-data">
		<html:hidden property="modo" styleId="modo" value="${ActuacionAsistenciaFormEdicion.modo}" />
		<html:hidden property="idInstitucion" styleId="idInstitucion" value="${ActuacionAsistenciaFormEdicion.idInstitucion}" />
		<html:hidden property="anio" styleId="anio" value="${ActuacionAsistenciaFormEdicion.anio}" />
		<html:hidden property="numero" styleId="numero" value="${ActuacionAsistenciaFormEdicion.numero}" />
		<html:hidden property="diaDespues" styleId="diaDespues" value="${ActuacionAsistenciaFormEdicion.diaDespues}" />
		<html:hidden property="anulacion" styleId="anulacion" value="${ActuacionAsistenciaFormEdicion.anulacion}" />
		<html:hidden property="validada" styleId="validada" value="${ActuacionAsistenciaFormEdicion.validada}" />
		<html:hidden property="acuerdoExtrajudicial" styleId="acuerdoExtrajudicial" value="${ActuacionAsistenciaFormEdicion.acuerdoExtrajudicial}" />
		<html:hidden property="numeroDiligenciaAsistencia" styleId="numeroDiligenciaAsistencia" value="${ActuacionAsistenciaFormEdicion.numeroDiligenciaAsistencia}" />
		<html:hidden property="numeroProcedimientoAsistencia" styleId="numeroProcedimientoAsistencia" value="${ActuacionAsistenciaFormEdicion.numeroProcedimientoAsistencia}" />
		<html:hidden property="comisariaAsistencia" styleId="comisariaAsistencia" value="${ActuacionAsistenciaFormEdicion.comisariaAsistencia}" />
		<html:hidden property="juzgadoAsistencia" styleId="juzgadoAsistencia" value="${ActuacionAsistenciaFormEdicion.juzgadoAsistencia}" />
		<html:hidden property="tipoPcajg" styleId="tipoPcajg" value="${ActuacionAsistenciaFormEdicion.tipoPcajg}" />
		<html:hidden property="usuCreacion" styleId="usuCreacion" value="${ActuacionAsistenciaFormEdicion.usuCreacion}" />
		<html:hidden property="fechaCreacion" name="date" styleId="fechaCreacion" value="${ActuacionAsistenciaFormEdicion.fechaCreacion}" />
		<input type="hidden" name="validarJustificaciones" id="validarJustificaciones" value="${asistencia.validarJustificaciones}" />
		<input type="hidden" id="isLetrado" name="isLetrado" value="${usrBean.letrado}" />
		<html:hidden property="idTipoAsistencia" styleId="idTipoAsistencia" value="${ActuacionAsistenciaFormEdicion.idTipoAsistencia}" />
		<html:hidden property="idActuacion" styleId="idActuacion" value="${ActuacionAsistenciaFormEdicion.idActuacion}" />
		<input type="hidden" name="auxIdCosteFijoActuacion" id="auxIdCosteFijoActuacion" value="${ActuacionAsistenciaFormEdicion.idCosteFijoActuacion}" />
		<input type="hidden" name="actionModal" id="actionModal"/>

		<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.dasistencia">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="6%"></td>
					<td width="22%"></td>
					<td width="6%"></td>
					<td width="22%"></td>
					<td width="4%"></td>
					<td width="4%"></td>
					<td width="6%"></td>
					<td width="4%"></td>
					<td width="6%"></td>
					<td width="8%"></td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.turno' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.turno.nombre}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.guardia' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.guardia.nombreGuardia}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.anio' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.anio}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.numero' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.numero}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.fecha' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.fechaHora}"></c:out></td>
				</tr>
			</table>
		</siga:ConjCampos>
			
		<siga:ConjCampos desplegable="true" oculto="false" leyenda='gratuita.mantActuacion.literal.asistido'>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="12%"></td>
					<td width="8%"></td>
					<td width="8%"></td>
					<td width="12%"></td>
					<td width="8%"></td>
					<td width="22%"></td>
					<td width="8%"></td>
					<td width="22%"></td>
				</tr>

				<tr>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.nif' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaJG.NIdentificacion}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.nombre' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaJG.nombre}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.apellidos1' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaJG.apellido1}"></c:out></td>
					
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.apellidos2' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaJG.apellido2}"></c:out></td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos desplegable="true" oculto="false" leyenda="gratuita.busquedaDesignas.literal.letrado">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="12%"></td>
					<td width="8%"></td>
					<td width="8%"></td>
					<td width="12%"></td>
					<td width="8%"></td>
					<td width="22%"></td>
					<td width="8%"></td>
					<td width="22%"></td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.ncolegiado' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaColegiado.colegiado.NColegiado}"></c:out></td>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.nombre' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaColegiado.nombre}"></c:out></td>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.apellidos1' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaColegiado.apellido1}"></c:out></td>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.apellidos2' /></td>
					<td class="labelTextValor"><c:out value="${asistencia.personaColegiado.apellido2}"></c:out></td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos desplegable="true" oculto="false" leyenda="gratuita.mantActuacion.literal.actuacion">
			<table width="100%" border="0" cellpadding="0" cellspacing="2">
				<tr>
					<td width="15%"></td>
					<td width="5%"></td>
					<td width="25%"></td>
					<td width="25%"></td>
					<td width="35%"></td>
				</tr>

				<tr>
					<td class="labelText"><siga:Idioma key='gratuita.mantActuacion.literal.nactuacion' />&nbsp;(*)</td>
					<td class="labelTextValor">
						<c:choose>
							<c:when test="${ActuacionAsistenciaFormEdicion.modo=='nuevo'}">
								<html:text name="ActuacionAsistenciaFormEdicion" styleId="idActuacion" property="idActuacion" size="10" maxlength="10" styleClass="box" />
							</c:when>
							<c:otherwise>
								<c:out value="${ActuacionAsistenciaFormEdicion.idActuacion}" />
								<html:hidden name="ActuacionAsistenciaFormEdicion" property="idActuacion" styleId="idActuacion"/>
							</c:otherwise>
						</c:choose>
					</td>

					<td class="labelText" style="text-align: center;">
						<siga:Idioma key='gratuita.mantActuacion.literal.anulacion' /> 
						<input type="checkbox" id="checkAnulacion" onclick="onclickCheckAnulacion();" />
					</td>

					<td class="labelText">
						<siga:Idioma key='gratuita.mantActuacion.literal.descripcion' />
					</td>
					<td>
						<html:text name="ActuacionAsistenciaFormEdicion" styleId="descripcionBreve" property="descripcionBreve" size="40" styleClass="box" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key='gratuita.mantActuacion.literal.fecha' />&nbsp;(*)
					</td>
					<td colspan="2">
						<c:choose>
							<c:when test="${ActuacionAsistenciaFormEdicion.modo=='ver' ||  ActuacionAsistenciaFormEdicion.validada==1 ||  ActuacionAsistenciaFormEdicion.anulacion==1}">
								<html:text property="fecha" size="10" readonly="true" styleClass="boxConsulta" styleId="fecha" value="${ActuacionAsistenciaFormEdicion.fecha}" />

							</c:when>
							<c:otherwise>
								<siga:Fecha  nombreCampo= "fecha" valorInicial="${ActuacionAsistenciaFormEdicion.fecha}" readOnly="true" postFunction="compruebaDiaDespues('${asistencia.fechaHora}');"/>
							</c:otherwise>
						</c:choose>
					</td>

					<td class="labelText" id="checkDiaDespuesText" style="display:none">
						<siga:Idioma key='gratuita.mantActuacion.literal.diadespues' />
					</td>
					<td>
						<input type="checkbox" id="checkDiaDespues" style="display:none"/>
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.tipoActuacion" />&nbsp;(*)
					</td>
					<td colspan="4" id="tdSelectTiposActuacion">
						<html:select styleClass="boxCombo" style="width:600px;" styleId="tiposActuacion" name="ActuacionAsistenciaFormEdicion" property="idTipoActuacion">
							<bean:define id="tiposActuacion" name="ActuacionAsistenciaForm"  property="tiposActuacion" type="java.util.Collection" />
							<html:optionsCollection name="tiposActuacion" value="idTipoActuacion" label="descripcion" />
						</html:select>
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.Coste" />
					</td>
					<td colspan="4" id="tdSelectTiposCosteFijo">
					<html:select styleClass="boxCombo" style="width:600px;" styleId="tiposCosteFijoActuaciones" value="${ActuacionAsistenciaForm.idCosteFijoActuacion}" name="ActuacionAsistenciaFormEdicion" property="idCosteFijoActuacion" >
						<bean:define id="tipoCosteFijoActuaciones" name="ActuacionAsistenciaForm" property="tipoCosteFijoActuaciones" type="java.util.Collection" />
						<html:optionsCollection name="tipoCosteFijoActuaciones" value="value" label="key" />
					</html:select>
					</td>
				</tr>
				
				<tr>
					<c:choose>
						<c:when test="${tipoPcajg=='2'}">
							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.nasunto' />(*)
							</td>
							<td align="left" colspan="4">
								<html:text name="ActuacionAsistenciaFormEdicion" property="numeroAsunto" styleId="numeroAsunto" size="30" maxlength="15" styleClass="box" />
							</td>
						</c:when>
						
						<c:otherwise>
							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.nasunto' />
							</td>
							<td align="left" colspan="4">
								<html:text name="ActuacionAsistenciaFormEdicion" property="numeroAsunto" styleId="numeroAsunto" size="30" maxlength="20" styleClass="box" />
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
				
				<tr>
					<td class="labelText">
								<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria" />
					<c:choose>
						<c:when test="${tipoPcajg=='9'}">
							<label id="asteriscoComisaria"> (*)</label>
						</c:when>
					</c:choose>
					</td>
					<td>
						<input type="text" id="codComisaria" class="box" size="8" style="margin-top: 2px;" maxlength="10" onBlur="obtenerComisaria();" />
					</td>
					
					<td id="tdSelectComisaria" colspan="3">
						<html:select styleClass="boxCombo" style="width:680px;" name="ActuacionAsistenciaFormEdicion" property="idComisaria" styleId="idComisaria" onchange="cambioComisaria();">
							<bean:define id="comisarias" name="ActuacionAsistenciaForm" property="comisarias" type="java.util.Collection" />
							<html:optionsCollection name="comisarias" value="idComisaria" label="nombre" />
						</html:select>
					</td>
				</tr>
				
				<tr>
					<td class="labelText" style="padding-left: 30px;">o</td>
				</tr>
				
				<tr>
					<td class="labelText">
								<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
					<c:choose>
						<c:when test="${tipoPcajg=='9'}">
								<label id="asteriscoJuzgado"> (*)</label>
						</c:when>
					</c:choose>
					</td>
					<td>
						<input type="text" id="codJuzgado" class="box" size="8" style="margin-top: 2px;" maxlength="10" onBlur="obtenerJuzgado();" />
					</td>					
					<td colspan="3" id="tdSelectJuzgado">
						<html:select styleClass="boxCombo" style="width:680px;" name="ActuacionAsistenciaFormEdicion" styleId="idJuzgado" property="idJuzgado" onchange="cambioJuzgado();">
							<bean:define id="juzgados" name="ActuacionAsistenciaForm" property="juzgados" type="java.util.Collection" />
							<html:optionsCollection name="juzgados" value="idJuzgado" label="nombre" />
						</html:select>
					</td>
				</tr>
				
				<tr>		
					<td class="labelText">
						<siga:Idioma key='gratuita.mantAsistencias.literal.NIG' />
					</td>
					<td colspan="2">
						<html:text name="ActuacionAsistenciaFormEdicion" property="nig"  styleId="nig" styleClass="box" style="size:19;width:200px" />
					</td>
					 			
					<td id="info" style="display:none"><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prision" />
					</td>
					<td colspan="2" id="tdSelectPrision">
						<html:select styleClass="boxCombo" style="width:300px;" name="ActuacionAsistenciaFormEdicion" property="idPrision" styleId="idPrision">
							<bean:define id="prisiones" name="ActuacionAsistenciaForm" property="prisiones" type="java.util.Collection" />
							<html:optionsCollection name="prisiones" value="codigoExt" label="nombre" />
						</html:select>
					</td>
					<td class="labelText">
						<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
					</td>
					<td> 
						<c:choose>
							<c:when test="${ActuacionAsistenciaFormEdicion.modo=='ver' ||  ActuacionAsistenciaFormEdicion.anulacion==1}">
								<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observaciones" property="observaciones"
									style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
									styleClass="boxConsulta"></html:textarea>
							</c:when>
							<c:otherwise>
								<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observaciones" property="observaciones"
									style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
									styleClass="boxCombo"></html:textarea>
							</c:otherwise>
						</c:choose>						
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<c:choose>
			<c:when test="${ActuacionAsistenciaFormEdicion.facturado=='1' ||ActuacionAsistenciaFormEdicion.modo=='ver'}">
				<siga:ConjCampos desplegable="true" oculto="false" leyenda="gratuita.mantActuacion.literal.justificacion">
					<table width="100%" border="0" cellpadding="0" cellspacing="2">
						<tr>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="10%"></td>
							<td width="20%"></td>
							<td width="15%"></td>
							<td width="30%"></td>
						</tr>

						<tr>
							<td class="labelText" valign="top">
								<siga:Idioma key='gratuita.mantActuacion.literal.fecha' />
							</td>
							<td class="labelTextValor">
								<html:text property="fechaJustificacion" size="10" readonly="true" styleId="fechaJustificacion" styleClass="boxConsulta" value="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" />
							</td>
							
							<td colspan="2" class="labelTextValor">
								&nbsp; 
								<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
									<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada' />
								</c:if>
							</td>

							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
							</td>
							<td class="labelTextValor">
								<c:choose>
									<c:when test="${ActuacionAsistenciaFormEdicion.modo=='ver' ||  ActuacionAsistenciaFormEdicion.anulacion==1}">
										<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion" property="observacionesJustificacion"
											style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
											styleClass="boxConsulta"></html:textarea>
									</c:when>
									<c:otherwise>
										<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion" property="observacionesJustificacion"
											style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
											styleClass="boxCombo"></html:textarea>
									</c:otherwise>
								</c:choose>		
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</c:when>

			<c:when test="${usrBean.letrado==false}">
				<siga:ConjCampos leyenda="gratuita.mantActuacion.literal.justificacion">
					<table width="100%" border="0" cellpadding="0" cellspacing="2">
						<tr>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="10%"></td>
							<td width="20%"></td>
							<td width="15%"></td>
							<td width="30%"></td>
						</tr>
						
						<tr>
							<td class="labelText" valign="top">
								<siga:Idioma key='gratuita.mantActuacion.literal.fecha' />
							</td>
							<td class="labelTextValor">
								<siga:Fecha nombreCampo="fechaJustificacion" valorInicial="${ActuacionAsistenciaFormEdicion.fechaJustificacion}" readOnly="true" postFunction="volverJustificacion();"></siga:Fecha>
							</td>

							<td>
								<input type="button"
									alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>"
									id="idButton" onclick="validaJustificacion();" class="button"
									value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
							</td>
							<td class="labelTextValor" align="left" id="tdValidada">&nbsp;
								<c:if test="${ ActuacionAsistenciaFormEdicion.validada==1}">
									<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada' />
								</c:if>
							</td>
							
							<td class="labelText">
								<siga:Idioma key='gratuita.mantActuacion.literal.observaciones' />
							</td>
							<td class="labelTextValor">
								<c:choose>
									<c:when test="${ActuacionAsistenciaFormEdicion.modo=='ver' ||  ActuacionAsistenciaFormEdicion.anulacion==1}">
										<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion" property="observacionesJustificacion" 
											style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
											styleClass="boxConsulta"></html:textarea>
									</c:when>
									<c:otherwise>
										<html:textarea name="ActuacionAsistenciaFormEdicion" styleId="observacionesJustificacion" property="observacionesJustificacion" 
											style="overflow-y:auto; overflow-x:hidden; width:300px; height:45px; resize:none;"
											styleClass="boxCombo"></html:textarea>
									</c:otherwise>
								</c:choose>	
								
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</c:when>
			
			<c:when
				test="${usrBean.letrado==true && asistencia.validarJustificaciones=='N' }">
				<html:hidden name="ActuacionAsistenciaFormEdicion" styleId="fechaJustificacion" property="fechaJustificacion" value="sysdate" />
				<html:hidden name="ActuacionAsistenciaFormEdicion" property="observacionesJustificacion" styleId="observacionesJustificacion" value="Validado automaticamente por configuracion del turno" />
			</c:when>
			
			<c:otherwise>
				<html:hidden name="ActuacionAsistenciaFormEdicion" styleId="fechaJustificacion" property="fechaJustificacion" /> 
				<html:hidden name="ActuacionAsistenciaFormEdicion" property="observacionesJustificacion" />
			</c:otherwise>
		</c:choose>


		<table valign="bottom" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.mantActuacion.literal.mensajeAsunto' /></td>
			</tr>
		</table>			
	</html:form>
	</div>
	<siga:ConjBotonesAccion botones="${botones}" modal="P" />
	
	<ajax:select
			baseUrl="/SIGA${path}.do?modo=getAjaxTipoCosteFijoActuacion"
			source="tiposActuacion" target="tiposCosteFijoActuaciones"
			parameters="idTipoActuacion={tiposActuacion},idTipoAsistencia={idTipoAsistencia},idInstitucion={idInstitucion}"
			preFunction="preAccionTipoActuacion"
			postFunction="postAccionTipoActuacion" />
	
	<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo" value="buscarJuzgado">
		<html:hidden property="codigoExt2" styleId="codigoExt2" value="" />
	</html:form>
	
	<html:form action="/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo" value="buscarComisaria">
		<html:hidden property="codigoExtBusqueda" styleId="codigoExtBusqueda" value="" />
	</html:form>
	
	<html:form action="/JGR_VolantesExpres.do" method="POST" target="submitArea">
		<html:hidden property="fechaGuardia" styleId="fechaGuardia" value="${asistencia.fechaHora}" />
		<html:hidden property="idTurno" styleId="idTurno" value="${asistencia.idTurno}" />
		<html:hidden property="idGuardia" styleId="idGuardia" value="${asistencia.idGuardia}" />
		<html:hidden property="idColegiado" styleId="idColegiado" value="${asistencia.personaColegiado.idPersona}" />
		<html:hidden property="idColegiadoGuardia" styleId="idColegiadoGuardia" value="${asistencia.personaColegiado.idPersona}" />
		<html:hidden property="idTipoAsistenciaColegio" styleId="idTipoAsistenciaColegio" value="${asistencia.idTipoAsistenciaColegio}" />
		<html:hidden property="centroOjuzgado" styleId="centroOjuzgado"/>
	</html:form>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		
	<script type="text/javascript">		
		function inicio() {		
			//Para que se rellene el combo de costes
			document.getElementById("tiposActuacion").onchange();
			document.getElementById("checkDiaDespues").checked = document.ActuacionAsistenciaFormEdicion.diaDespues.value=='S';
			
			var porActuaciones = true; 
<%
			if (porAsAct!=null && porAsAct.equals("0")) { // PorAsistencias=0; PorActuaciones=1
%>
				porActuaciones = false;
<%
			}
%>
			
			// Si tiene marcado el check del dia despues o tiene configurado la guardia por por actuaciones, muestro la casilla del dia despues
			if (document.ActuacionAsistenciaFormEdicion.diaDespues.value=='S' || porActuaciones) {
				jQuery("#checkDiaDespues").show();
				jQuery("#checkDiaDespuesText").show();				
			}
			
			document.getElementById("checkAnulacion").checked = document.ActuacionAsistenciaFormEdicion.anulacion.value=='1';
			if(document.ActuacionAsistenciaFormEdicion.validada!=null && document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
				document.getElementById('fechaJustificacion').className="boxConsulta";
				document.getElementById('fechaJustificacion').readOnly = true;
				if(document.getElementById("tdValidada")){
					document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				}
				jQuery("#fechaJustificacion-datepicker-trigger").hide();
			} else if (document.ActuacionAsistenciaFormEdicion.modo!=null && document.ActuacionAsistenciaFormEdicion.modo.value!='ver' && document.ActuacionAsistenciaFormEdicion.facturado!=null && document.ActuacionAsistenciaFormEdicion.facturado.value!='1' && document.getElementById('isLetrado')!=null && document.getElementById('isLetrado').value=='false') {
				document.getElementById('fechaJustificacion').className="tcal box editable tcalInput";
				document.getElementById('fechaJustificacion').readOnly = false;
				f_tcalInit();
				if(document.getElementById("tdValidada")) {
					document.getElementById("tdValidada").innerHTML = "";
				}
				jQuery("#fechaJustificacion-datepicker-trigger").show();
			}
			
			if(document.ActuacionAsistenciaFormEdicion.modo.value == 'ver' ){
				habilitarCampos(false);			
			} else {
				if(document.getElementById('isLetrado').value=='false') {							
					if(document.ActuacionAsistenciaFormEdicion.anulacion.value=="1") {
						habilitarCampos(false);
						document.getElementById('fechaJustificacion').className="boxConsulta"; 
						jQuery("#fechaJustificacion-datepicker-trigger").hide();
						jQuery("#checkAnulacion").removeAttr("disabled");
					}
					/*mhg - INC_09789_SIGA
					else if(document.ActuacionAsistenciaFormEdicion.validada.value=="1") {
						habilitarCampos(false);						
					}
					*/
				} else {
					if(document.ActuacionAsistenciaFormEdicion.anulacion.value=="1" ||document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
						habilitarCampos(false);
					}
				}
			}
			
			// para VolantesExpress
			var comboJuzgado = document.getElementById("idJuzgado");	
			if(comboJuzgado.value!=""){
				document.VolantesExpressForm.centroOjuzgado.value = "juzgado";
			}
			var comboComisaria = document.getElementById("idComisaria");
			if(comboComisaria.value!=""){
				document.VolantesExpressForm.centroOjuzgado.value = "centro";
			}
		}
		
		function habilitarCampos(isHabilitar) {
			
			if(isHabilitar==true){
				var inputs = document.getElementsByTagName("input");
				for(var i = 0 ; i <inputs.length ; i++) {
					input = inputs[i];
					if(input.type=="checkbox") {
						jQuery.removeAttr(input,"disabled");
					} else if(input.type!="button") {
						if(!input.name.startsWith('fecha')) {
							input.className = "box";
						}
						jQuery.removeAttr(input,"readonly");
					}
				}
				var selects = document.getElementsByTagName("select");
				for(var i = 0 ; i <selects.length ; i++) {
					select = selects[i];
					jQuery.removeAttr(select,"disabled");
					//$(select).removeAttr("disabled"); 
				}
				var textareas = document.getElementsByTagName("textarea");
				for(var i = 0 ; i <textareas.length ; i++) {
					textarea = textareas[i];
					jQuery.removeAttr(textarea,"disabled");
					//$(select).removeAttr("disabled"); 
				}				
			} else {
				var tdTiposActuacion = document.getElementById('tdSelectTiposActuacion');
				var index = document.getElementById("tiposActuacion").selectedIndex;
				var descripcionTipoActuacion;
				if(index && index != -1){
					descripcionTipoActuacion = document.getElementById("tiposActuacion").options[index].text;
				} else {
					descripcionTipoActuacion ="";
				}
				document.getElementById("tiposActuacion").style.display="none";
				var inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionTipoActuacion+'" style="width:600px;" />';
				tdTiposActuacion.innerHTML = tdTiposActuacion.innerHTML + inputStr;
				
				var tdSelectTiposCosteFijo = document.getElementById('tdSelectTiposCosteFijo');
				var descripcionCosteFijo;
				if(jQuery("#tiposCosteFijoActuaciones").find("option:selected").length > 0){
					descripcionCosteFijo = jQuery("#tiposCosteFijoActuaciones").find("option:selected").text();
				} else {
					descripcionCosteFijo ="";
				}
				document.getElementById("tiposCosteFijoActuaciones").style.display="none";
				inputStr = '<input type="text" id="tiposCosteFijoActuaciones_txt" readonly class="boxConsulta" value="'+descripcionCosteFijo+'" style="width:680px;" />'
				tdSelectTiposCosteFijo.innerHTML = tdSelectTiposCosteFijo.innerHTML + inputStr;
		
				var tdSelectComisaria = document.getElementById('tdSelectComisaria');
				index = document.getElementById("idComisaria").selectedIndex;
				var descripcionComisaria;
				if(index && index != -1){
					descripcionComisaria = document.getElementById("idComisaria").options[index].text;
				} else {
					descripcionComisaria ="";
				}
				document.getElementById("idComisaria").style.display="none";
				inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionComisaria+'" style="width:680px;" />'
				tdSelectComisaria.innerHTML = tdSelectComisaria.innerHTML + inputStr;
				
				var tdSelectJuzgado = document.getElementById('tdSelectJuzgado');
				index = document.getElementById("idJuzgado").selectedIndex;
				var descripcionJuzgado;
				if(index && index != -1){
					descripcionJuzgado =document.getElementById("idJuzgado").options[index].text;
				} else {
					descripcionJuzgado ="";
				}
				document.getElementById("idJuzgado").style.display="none";
				inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionJuzgado+'" style="width:680px;" />';
				tdSelectJuzgado.innerHTML = tdSelectJuzgado.innerHTML + inputStr;
				
				var tdSelectPrision = document.getElementById('tdSelectPrision');
				index = document.ActuacionAsistenciaFormEdicion.idPrision.selectedIndex;
				var descripcionPrision;
				if(index && index != -1) {
					descripcionPrision = document.ActuacionAsistenciaFormEdicion.idPrision.options[index].text;
				} else {
					descripcionPrision ="";
				}
				document.getElementById("idPrision").style.display="none";
				inputStr = '<input type="text" readonly class="boxConsulta" value="'+descripcionPrision+'" style="width:300px;"/>';
				tdSelectPrision.innerHTML = tdSelectPrision.innerHTML + inputStr;
				
				var inputs = document.getElementsByTagName("input");
				for(var i = 0 ; i < inputs.length ; i++) {
					var input = inputs[i];
					if(input.type=="checkbox"){
						jQuery.attr(input,"disabled","disabled");
						//$(input).attr("disabled","disabled");
					} else if(input.type!="button"){
						input.className = "boxConsulta";
						//$(input).attr("readonly","readonly");
						jQuery.attr(input,"readonly","readonly");
					}
				}
				
				var selects = document.getElementsByTagName("select");
				for(var i = 0 ; i < selects.length ; i++) {
					var select = selects[i];
					jQuery.attr(select,"disabled","disabled");
					//$(select).attr("disabled","disabled");
				}
				var textareas = document.getElementsByTagName("textarea");
				for (var i = 0 ; i <textareas.length ; i++) {
					var textarea = textareas[i];
					jQuery.attr(textarea,"disabled","disabled")
					//$(textarea).attr("disabled","disabled"); 
				}						
			}		
		}
	
		function refrescarLocal() {
			document.ActuacionAsistenciaForm.target = "_self";
			document.ActuacionAsistenciaForm.modo.value="edicionActuacionAsistencia";
			document.ActuacionAsistenciaForm.submit();
		}			
	
		function accionGuardar() {
			sub();
			if(document.getElementById("tiposActuacion").value== '-1'){
				msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.tipoActuacion'/>";
				alert(msg);
				fin();
				return false;
			}
			if(document.getElementById("tipoPcajg").value=='2' &&  document.ActuacionAsistenciaFormEdicion.numeroAsunto.value== ''){
				msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.nasunto'/>";
				alert(msg);
				fin();
				return false;
			}
			if(document.getElementById("tipoPcajg").value=='9' &&  document.getElementById("idComisaria").value== "" && document.getElementById("idJuzgado").value== ""){
				msg = "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.comisariaJuzgados'/>";
				alert(msg);
				fin();
				return false;
			}
			var nigAux = document.getElementById("nig").value;
			nigAux = ready2ApplyMask(nigAux);
			nigAux = nigAux.toUpperCase();
			
			
			valueNumProcedimiento = document.getElementById("numeroAsunto").value;
			objectConsejo = document.getElementById("idConsejo");
			
			if((objectConsejo && objectConsejo.value ==IDINSTITUCION_CONSEJO_ANDALUZ)){
				var objectAnioProcedimiento =  new Object();
				if(valueNumProcedimiento!=''){ 
					arrayNumProcedimiento = valueNumProcedimiento.split("/");
					if(arrayNumProcedimiento.length<2){
						error = "<siga:Idioma key='gratuita.numProcedimiento.formato' arg0='gratuita.numProcedimiento.formato.numeroanio' />";
						fin();
						alert(error);
						return false;
						
					}
					valueNumProcedimiento = arrayNumProcedimiento[0];
					objectAnioProcedimiento.value = arrayNumProcedimiento[1];
					error = validarFormatosNigNumProc(nigAux,valueNumProcedimiento,objectAnioProcedimiento,'0',objectConsejo);
				}else{
					error = validarFormatosNigNumProc(nigAux,'','','0',objectConsejo);
					
				}
				
				
				
				if(error!=''){
					fin();
					alert(error);
					return false;
					
				}
				formateaNumProcedimiento(valueNumProcedimiento,objectAnioProcedimiento.value,objectConsejo);
				
			}else{
				error = validarFormatosNigNumProc(nigAux,'','','0',objectConsejo);
				if(error!=''){
					fin();
					alert(error);
					return false;
					
				}
			}
			
			
			document.ActuacionAsistenciaFormEdicion.nig.value = nigAux;
			
			
			if(document.getElementById("checkDiaDespues").checked){
				document.ActuacionAsistenciaFormEdicion.diaDespues.value = 'S';
			}else{
				document.ActuacionAsistenciaFormEdicion.diaDespues.value = 'N';
			}
			if(document.getElementById("checkAnulacion").checked){
				document.ActuacionAsistenciaFormEdicion.anulacion.value = '1';
			}else{
				document.ActuacionAsistenciaFormEdicion.anulacion.value = '0';
			}
			if(document.getElementById("validarJustificaciones").value=='N' &&document.getElementById("isLetrado").value=='true' ){
				document.ActuacionAsistenciaFormEdicion.validada.value = "1";
			}
			habilitarCampos(true);
			if (validateActuacionAsistenciaFormEdicion(document.ActuacionAsistenciaFormEdicion)){
				document.ActuacionAsistenciaForm.modo.value = 'edicionActuacionAsistencia';
				document.ActuacionAsistenciaFormEdicion.target = "submitArea";
				document.ActuacionAsistenciaFormEdicion.submit();
			} else {
				fin();
		 	}		
		}
		function accionGuardarCerrar() {
			//solo necesario para nuevoDesdeVolanteExpres
			accionGuardar();
		}
		
		function formateaNumProcedimiento(valueNumProcedimiento,valueAnioProcedimiento,objectConsejo){
			if(objectConsejo && objectConsejo.value==IDINSTITUCION_CONSEJO_ANDALUZ){
				var numProcedimientoArray = valueNumProcedimiento.split('.');
				numProcedimiento = numProcedimientoArray[0];
				if(numProcedimiento && numProcedimiento!=''){
					numProcedimiento = pad(numProcedimiento,5,false);
					finNumProcedimiento = numProcedimientoArray[1]; 
					if(finNumProcedimiento){
						numProcedimiento = numProcedimiento+"."+pad(finNumProcedimiento,2,false);
					}
					document.getElementById("numeroAsunto").value = numProcedimiento+"/"+valueAnioProcedimiento;
				}
				
			}
		}
		
		
		function compruebaDiaDespues(fecha2){
			var fechaAct=document.getElementById("fecha").value;
			var fechaHora=fecha2;
			if (isAfter(fechaAct,fechaHora)){				
<%			
				// Consulto si tiene configurado la guardia por asistencias(0) o por actuaciones(1) y la activo en caso de ser por actuaciones
				if (porAsAct!=null && porAsAct.equals("1")) {
%>					
					document.getElementById("checkDiaDespues").checked = true;
<%
				} else {			
%>					
					document.getElementById("checkDiaDespues").checked = false;
<%
				}
%>			
	
			} else {
			    if (isEquals(fechaHora,fechaAct)){
			    	document.getElementById("checkDiaDespues").checked = false;
				} else {// cuando la fecha de asistencia es igual que la de la actuacion el check del dia despues no se chequea.
					alert("La fecha de actuacion no puede ser anterior a la fecha de la asistencia ("+fechaHora+")");
				  	document.ActuacionAsistenciaFormEdicion.fecha.value = '';
				  	document.getElementById("checkDiaDespues").checked = false;				
				}// fin del if
			}// fin del if
		}
	
		function onclickCheckAnulacion () {		
		}
		
		function validaJustificacion () {
			
			if(document.ActuacionAsistenciaFormEdicion.validada.value=="1"){
				document.getElementById('fechaJustificacion').className="tcal box editable tcalInput";
				document.getElementById('fechaJustificacion').readOnly = false;
				document.ActuacionAsistenciaFormEdicion.validada.value="0";
				document.getElementById("tdValidada").innerHTML = '';
				jQuery("#fechaJustificacion-datepicker-trigger").show();
				document.getElementById('fechaJustificacion').value="";		
				f_tcalInit();
			} else {
	
				document.getElementById('fechaJustificacion').className="boxConsulta";
				document.getElementById('fechaJustificacion').readOnly = true;
				document.ActuacionAsistenciaFormEdicion.validada.value="1"; 
				jQuery("#fechaJustificacion-datepicker-trigger").hide();
				document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				if(document.ActuacionAsistenciaFormEdicion.fechaJustificacion.value==''){
					document.getElementById('fechaJustificacion').value=getFechaActualDDMMYYYY();
					
				}			
			}	
		}
	
		function volverJustificacion () {
			if(document.ActuacionAsistenciaFormEdicion.fechaJustificacion.value!=''){
				//Si alguna vez se abre justificaciones para colegiados, la lineas comentadas de abajo de abajo serviran cuando sea letrado
				//if(document.getElementById('validarJustificaciones').value=='N'){
					//document.ActuacionAsistenciaFormEdicion.validada.value="1";
					//document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				//}
				//else{
					//document.ActuacionAsistenciaFormEdicion.validada.value="0";
					//document.getElementById("tdValidada").innerHTML = '';
						
				//}
				document.ActuacionAsistenciaFormEdicion.validada.value="1";
				document.getElementById("tdValidada").innerHTML = '<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				
			}else{
				document.getElementById("tdValidada").innerHTML = '';
				document.ActuacionAsistenciaFormEdicion.validada.value="0";
			}
			
		}	
		
		// Asociada al boton Volver para Volantes Expres
		function accionVolver() {
			document.VolantesExpressForm.target="mainWorkArea";
			document.VolantesExpressForm.action = "/SIGA/JGR_VolantesExpres.do?origen=ACTUACIONASISTENCIA";
			document.VolantesExpressForm.submit();
		}

		// Asociada al boton Cerrar
		function accionCerrar() {		
			document.ActuacionAsistenciaForm.modo.value = 'abrir';
			window.top.close(); 		
		}
	
		// Asociada al boton Restablecer
		function accionRestablecer() {
			document.ActuacionAsistenciaFormEdicion.reset();
			document.getElementById("checkDiaDespues").checked = document.ActuacionAsistenciaFormEdicion.diaDespues.value=='S';
			document.getElementById("checkAnulacion").checked = document.ActuacionAsistenciaFormEdicion.anulacion.value=='1';
			
			if(document.ActuacionAsistenciaFormEdicion.validada.value != "1"){ 
				inicio();
			}
			cambioComisaria();
			cambioJuzgado();
		}
	</script>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>