<!DOCTYPE html>
<html>
<head>
<!-- gestionJustificacionEconomicaCatalunya.jsp -->

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
<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">

<script type="text/javascript">
	
	
	function refrescarLocal(){
		if(jQuery("#dialogoInsercion:visible").length)
			closeDialog('dialogoInsercion');
		if(jQuery("#dialogoErrorGlobal:visible").length)
			closeDialog('dialogoErrorGlobal');
		buscarJustificaciones();
	}
	
	function buscar() {
		return buscarJustificaciones();
	}
	
	function buscarJustificaciones(pagina,parametros) {
		
        var idInstitucion = document.GestionEconomicaCatalunyaForm.idInstitucion.value;
        var idColegio = '';
        if(document.GestionEconomicaCatalunyaForm.idColegio)
        	idColegio = document.GestionEconomicaCatalunyaForm.idColegio.value;
        var descripcion = document.GestionEconomicaCatalunyaForm.descripcion.value;
        var anio = document.GestionEconomicaCatalunyaForm.anio.value;
        var idPeriodo = document.GestionEconomicaCatalunyaForm.idPeriodo.value;
        var idFacturacion = '';
        if(document.GestionEconomicaCatalunyaForm.idFacturacion)
        	idFacturacion = document.GestionEconomicaCatalunyaForm.idFacturacion.value;
        var idEstado = document.GestionEconomicaCatalunyaForm.idEstado.value;
        var fechaDesde = document.GestionEconomicaCatalunyaForm.fechaDesde.value;
        var data = "idInstitucion="+idInstitucion;
        if(idEstado!='')
        	data += "&idEstado="+idEstado;
        if(descripcion!='')
        	data += "&descripcion="+descripcion;
        if(anio!='')
        	data += "&anio="+anio;
        if(idPeriodo!='')
        	data += "&idPeriodo="+idPeriodo;
        if(idFacturacion!='')
        	data += "&idFacturacion="+idFacturacion;
        if(fechaDesde!='')
        	data += "&fechaDesde="+fechaDesde;
        if(idColegio!='')
        	data += "&idColegio="+idColegio;
        if(pagina)
        	data += "&pagina="+pagina;
        if(parametros){
        	data += '&'+parametros;
        	
        }
        sub();
        var accion = document.GestionEconomicaCatalunyaForm.action;
	     jQuery.ajax({
            type: "POST",
            url: accion+"?modo=getAjaxBusqueda",
            data: data,
            success: function(response){
            	console.log(response);
            	fin();
            	jQuery('#divListado').html(response);
	            fin();
            },
            error: function(e){
            	fin();
                alert('Error: ' + e);
            }
        });
    }
  	
  	function nuevo(){
		jQuery("#descripcionNew").val("");
		jQuery("#idPeriodoNew").val("");
		jQuery("#anioNew").val("");
		jQuery("#idFacturacionNew").val("");
		
		openDialog('dialogoInsercion');
	}
	
	function openDialog(dialogo){
		jQuery("#"+dialogo).dialog(
				{
				      height: 300,
				      width: 525,
				      modal: true,
				      resizable: false,
				      buttons: {
				    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionInsercion(dialogo); }},
				          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog(dialogo);}}
				      }
				}
			);
			
		jQuery(".ui-widget-overlay").css("opacity","0");
	}
	
	function accionInsercion(dialogo){
		sub();
		error = '';
		if(dialogo=='dialogoInsercion'){
			document.forms['FormularioGestion'].descripcion.value = jQuery("#descripcionNew").val();  
			document.forms['FormularioGestion'].idPeriodo.value = jQuery('select#idPeriodoNew option:selected').val();
			document.forms['FormularioGestion'].anio.value = jQuery("#anioNew").val();
			document.forms['FormularioGestion'].facturaciones.value = jQuery('select#facturacionesNew').val();
			
			if(document.forms['FormularioGestion'].anio.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.anio'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].idPeriodo.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarioGuardias.literal.periodo'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.descripcion'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].facturaciones.value==''){
				error += "<siga:Idioma key='errors.required' arg0='pestana.justiciagratuita.retenciones'/>";
				
			}
			if (error=='')
				document.FormularioGestion.modo.value = "insertaJustificacion";
		}else if(dialogo=='dialogoErrorGlobal'){
			document.forms['FormularioGestion'].error.value = jQuery("#errorGlobal").val();
			if(document.forms['FormularioGestion'].error.value==''){
				error += "<siga:Idioma key='errors.required' arg0='general.literal.errorGlobal'/>";
				
			}
			if (error=='')
				document.FormularioGestion.modo.value = "insertaErrorGlobal";
		}
		if (error!=''){
			alert(error);
			fin();
			return false;
		}
		document.forms['FormularioGestion'].submit();
	}
	function closeDialog(dialogo){
		 jQuery("#"+dialogo).dialog("close"); 
	}
	
	function consultaVistaPreviaJustificacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "consultaVistaPreviaJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function borraJustificacion(fila) {
		sub();
		document.forms['FormularioGestion'].modo.value = "deleteJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function revalida(fila) {
		sub();
		document.forms['FormularioGestion'].modo.value = "validaJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaLogValidacion(fila) {
		document.forms['FormularioGestion'].modo.value = "descargaErrorValidacionJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();		
	}
	function descargaErrorJustificacionCICAC(fila) {
		document.forms['FormularioGestion'].modo.value = "descargaErrorJustificacionCICAC";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();		
	}
	
	function enviaJustificacionCICAC(fila) {
		//alertStop("Envia la justificacion an  CICAC. ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaJustificacionCICAC";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaErroresJustificacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErroresJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaJustificacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	
	function editaJustificacion(fila) {
		
		sub();
		document.forms['FormularioGestion'].modo.value = "editaJustificacion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].target = 'mainWorkArea';
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	function consultaJustificacion(fila) {
		descargaJustificacion(fila);
		//sub();
		//document.forms['FormularioGestion'].modo.value = "consultaJustificacion";
		//var idJustificacion = 'idJustificacion_' + fila ;
		//var idInstitucion = 'idInstitucion_' + fila ;
		//document.forms['FormularioGestion'].target = 'mainWorkArea';
		//document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		//document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		//document.forms['FormularioGestion'].submit();
	}
	function finalizaErrores(fila) {
		
		//alertStop("Finaliza errores. Cambia el estado e inserta ecom para revisar finalizados");
		sub();
		document.forms['FormularioGestion'].modo.value = "finalizaErrores";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
		
	}
	function enviaRespuestaCICAC_ICA(fila) {
		
		//alertStop("Envia el respuesta al ICA ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaRespuestaCICAC_ICA";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	
	function enviaJustificacionGEN(fila) {
		
		// alertStop("Envia la justificacion a la Generalitat. ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaJustificacionGEN";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	function adjuntaFicheroError(fila) {
		
		var elem = document.getElementById("fileUpdate");
		elem.click();
		if(elem &&elem.value!=''){
			var idJustificacion = 'idJustificacion_' + fila ;
			var idInstitucion = 'idInstitucion_' + fila ;
			document.forms['FormularioGestion'].modo.value = "adjuntaFicheroError";
			document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
			document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
			document.forms['FormularioGestion'].pathFile.value = elem.value; 
			document.forms['FormularioGestion'].submit();	
		}
	}
	function insertaErrorGlobal(fila) {
		
		document.forms['FormularioGestion'].modo.value = "insertaErrorGlobal";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		jQuery("#errorGlobal").val("");
		openDialog('dialogoErrorGlobal');
		
		
	}
		
	function inicio() {
		if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
			alert(document.getElementById("mensajeSuccess").value,'success');
		}
		if(document.getElementById("volverBusqueda") && document.getElementById("volverBusqueda").value!=''){
			buscarJustificaciones('',document.getElementById("volverBusqueda").value);
		}
		
		
		
	}	
			
		</script>
	</head>
<c:choose> 
	<c:when test="true">
		<siga:Titulo titulo="menu.sjcs.ecom.justificacion" localizacion="menu.sjcs.ecomunicaciones.localizacion"/>
	</c:when>
	<c:otherwise>
		<siga:Titulo titulo="menu.sjcs.ecom.certificacion" localizacion="menu.sjcs.ecomunicaciones.localizacion"/>
	</c:otherwise>
</c:choose>
<c:set var="botonesBusqueda" value="N,B" />
<c:if test="${USRBEAN.location=='3001'}">
		<c:set var="botonesBusqueda" value="B" />
</c:if>

<body onload="return inicio();">
 
<input type="hidden" id="volverBusqueda" value="${volverBusqueda}" class="inputNotSelect"/>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" enctype="multipart/form-data" target="mainWorkArea">
		<html:hidden  property="modo"/>
		<html:hidden property="idInstitucion"/>
		<input type="file" id ='fileUpdate' style="display:none"/>
		<siga:ConjCampos leyenda="gratuita.gestionInscripciones.datosSolicitud.leyenda">
			<table width="100%" border="0">
				<tr>
					<td width="150x"></td>
					<td colspan="3" width="330x"></td>
					<td width="150x"></td>
					<td width="330x"></td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/>
					</td>
					<td>
						<html:text property="descripcion" size="25"  styleClass="box"  />
					</td>
					<td class="labelText">
					
						<siga:Idioma key="gratuita.mantActuacion.literal.anio"/>
					</td>
					<td>
						<html:text property="anio" size="10" maxlength="4"  styleClass="box"  />
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/>
					</td>
					<td>
						<siga:Select queryId="getPeriodos" id="idPeriodo" width="100" />
					</td>
				</tr>
				<tr>				
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.fecha"/>&nbsp;<siga:Idioma key="gratuita.mantActuacion.literal.actuacion"/>
						
					</td>
					
					<td>
						<siga:Fecha nombreCampo="fechaDesde"  valorInicial="${SolicitudAceptadaCentralitaForm.fechaDesde}" />							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
					</td>
					<td colspan ='2' >
						<siga:Select queryId="getEstadosJustificacion" id="idEstado" params="${paramsEstados}" width="180" />
					</td>		
					
					<td></td>		
				</tr>
				<tr>
					<c:choose>
						<c:when test="${USRBEAN.location=='3001'}">
							<td class="labelText">
								<siga:Idioma key="administracion.informes.literal.colegio"/>
								
							</td>
							<td colspan ="3">
								<siga:Select id="idColegio" queryId="getColegiosDeConsejo"  width="400" />	
								
							</td>
						</c:when>
						<c:otherwise>
							<td class="labelText">
								<siga:Idioma key="gratuita.actuacionesDesigna.literal.facturacion"/>
								
							</td>
							<td colspan ="3">
						
								<siga:Select queryId="getFacturacionesSJCS" id="idFacturacion" width="400"  />
							</td>
						</c:otherwise>
					
					</c:choose>
					
					<td colspan ="2"></td>
						
				</tr>
			</table>				
		</siga:ConjCampos>


	<siga:ConjBotonesBusqueda botones="${botonesBusqueda}"/>
		<div id="divListado"></div>	
	</html:form>
	
	<html:form action="${path}"   name="FormularioGestion" type ="com.siga.gratuita.form.GestionEconomicaCatalunyaForm"  target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion" value='${GestionEconomicaCatalunyaForm.idInstitucion}' />
		<html:hidden property="descripcion"/>
		<html:hidden property="idPeriodo"/>
		<html:hidden property="idJustificacion"/>
		<html:hidden property="anio"/>
		<html:hidden property="facturaciones"/>
		<html:hidden property="error"/>
		<html:hidden property="pathFile"/>
		
		
		
		
</html:form>
	
		
<div id="dialogoInsercion"  title='<bean:message key="menu.sjcs.ecom.justificacion"/>' style="display:none">
	<div>&nbsp;</div>

  	<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.leyenda.datosJustificacion">
  		
  		<div class="labelText">
   			<label for="descripcion"  style="width:100px;float:left;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/></label>
   			<input type="text" id="descripcionNew" maxlength="100" size="40" />
		</div>
		<div class="labelText">
		
						
		
   			<label for="anio"  style="width:100px;float:left;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.anio"/></label>
   			<input type="text" id="anioNew" maxlength="4" size="6" />
   			<label for="trimestre" style="width:100px;color: black"><siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/></label>
			<siga:Select queryId="getPeriodos" id="idPeriodoNew" width="120" required="true" />
		</div>
		<div class="labelText">
			<label for="facturacion" style="width:100px;float:left;color: black"><siga:Idioma key="pestana.justiciagratuita.retenciones"/></label>
			<siga:Select queryId="getFacturacionesSJCS" id="facturacionesNew" required="true" width="300" multiple="true" lines="4" />
		</div>
   			
			
	</siga:ConjCampos>

</div>

<div id="dialogoErrorGlobal"  title='<siga:Idioma key="general.literal.errorGlobal"/>' style="display:none">
<div>&nbsp;</div>
	<div>
		<div class="labelText">
   			<label for="errorGlobal"  style="width:90px;float:left;color: black"><siga:Idioma key="general.literal.errorGlobal"/></label><textarea  id="errorGlobal" name="errorGlobal"
			                	onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
			                	style="overflow-y:auto; overflow-x:hidden; width:350px; height:70px; resize:none;" 
			                	class="box"></textarea>  
		</div>
	</div>
</div>




		
		<!-- FIN: BOTONES BUSQUEDA -->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
		
		
</body>
</html>