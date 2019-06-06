<!DOCTYPE html>
<html>
<head>
<!-- gestionDevolucionEconomicaCatalunya.jsp -->

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
		buscarDevoluciones();
	}
	
	function buscar() {
		return buscarDevoluciones();
	}
	
	function buscarDevoluciones(pagina,parametros) {
		
        var idInstitucion = document.GestionEconomicaCatalunyaForm.idInstitucion.value;
        var descripcion = document.GestionEconomicaCatalunyaForm.descripcion.value;
        var idFacturacion = '';
        if(document.GestionEconomicaCatalunyaForm.idFacturacion)
        	idFacturacion = document.GestionEconomicaCatalunyaForm.idFacturacion.value;
        var idJustificacion = '';
        if(document.GestionEconomicaCatalunyaForm.idJustificacion)
        	idJustificacion = document.GestionEconomicaCatalunyaForm.idJustificacion.value;
        var idColegio = '';
        if(document.GestionEconomicaCatalunyaForm.idColegio)
        	idColegio = document.GestionEconomicaCatalunyaForm.idColegio.value;
        
        var idEstado = document.GestionEconomicaCatalunyaForm.idEstado.value;
        var data = "idInstitucion="+idInstitucion;
    	data += '&origen=1';
    	if(idJustificacion!='')
        	data += "&idJustificacion="+idJustificacion;
    	
    	
    	
    	
    	if(idEstado!='')
        	data += "&idEstado="+idEstado;
        if(descripcion!='')
        	data += "&descripcion="+descripcion;
        if(idFacturacion!='')
        	data += "&idFacturacion="+idFacturacion;
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
		jQuery("#idJustificacionNew").val("");
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
			document.forms['FormularioGestion'].idJustificacion.value = jQuery('select#idJustificacionNew').val();

			if(document.forms['FormularioGestion'].descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.descripcion'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].idJustificacion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='menu.sjcs.ecom.justificacion'/>";
				
			}
			if (error=='')
				document.FormularioGestion.modo.value = "insertaDevolucion";
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
		alert("<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>");
		document.forms['FormularioGestion'].modo.value = "consultaVistaPreviaDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function borraDevolucion(fila) {
		sub();
		document.forms['FormularioGestion'].modo.value = "borraDevolucion";
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function revalida(fila) {
		sub();
		document.forms['FormularioGestion'].modo.value = "validaDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaLogValidacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErrorValidacionDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();		
	}
	function descargaErrorJustificacionCICAC(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErrorDevolucionCICAC";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();		
	}
	
	function enviaJustificacionCICAC(fila) {
		alertStop("Envia la justificacion an  CICAC. ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaDevolucionCICAC";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaErroresJustificacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErroresDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
	}
	function descargaJustificacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	
	function editaJustificacion(fila) {
		
		sub();
		document.forms['FormularioGestion'].modo.value = "editaDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].target = 'mainWorkArea';
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	function consultaJustificacion(fila) {
		descargaJustificacion(fila);
		
	}
	function finalizaErrores(fila) {
		
		sub();
		document.forms['FormularioGestion'].modo.value = "finalizaErroresDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
		
		
	}
	function enviaRespuestaCICAC_ICA(fila) {
		
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaRespuestaDevolucionCICAC_ICA";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	
	function enviaJustificacionGEN(fila) {
		
		alertStop("Envia la justificacion a la Generalitat. ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaDevolucionGEN";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].submit();
	}
	function adjuntaFicheroError(fila) {
		
		var elem = document.getElementById("fileUpdate");
		elem.click();
		if(elem &&elem.value!=''){
			var idJustificacion = 'idJustificacion_' + fila ;
			var idDevolucion = 'idDevolucion_' + fila ;
			var idInstitucion = 'idInstitucion_' + fila ;
			document.forms['FormularioGestion'].modo.value = "adjuntaFicheroErrorDevolucion";
			document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
			document.forms['FormularioGestion'].idJustificacion.value = document.getElementById(idJustificacion).value;
			document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
			document.forms['FormularioGestion'].pathFile.value = elem.value; 
			document.forms['FormularioGestion'].submit();	
		}
	}
	function insertaErrorGlobal(fila) {
		
		document.forms['FormularioGestion'].modo.value = "insertaErrorGlobalDevolucion";
		var idJustificacion = 'idJustificacion_' + fila ;
		var idDevolucion = 'idDevolucion_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById(idDevolucion).value;
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
			buscarDevoluciones('',document.getElementById("volverBusqueda").value);
		}
		
		
		
	}	
			
		</script>
	</head>
<c:choose> 
	<c:when test="true">
		<siga:Titulo titulo="menu.sjcs.ecom.devolucion" localizacion="menu.sjcs.ecomunicaciones.localizacion"/>
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
						<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
					</td>
					<td  >
						<siga:Select queryId="getEstadosJustificacion" id="idEstado" params="${paramsEstados}" width="180" />
					</td>		
					
					<td></td>		
					<td></td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${USRBEAN.location=='3001'}">
							<td class="labelText">
								<siga:Idioma key="administracion.informes.literal.colegio"/>
								
							</td>
							<td colspan ="5">
								<siga:Select id="idColegio" queryId="getColegiosDeConsejo"  width="400" />	
								
							</td>
						</c:when>
						<c:otherwise>
							<td class="labelText">
								<siga:Idioma key="gratuita.actuacionesDesigna.literal.facturacion"/>
								
							</td>
							<td >
						
								<siga:Select queryId="getFacturacionesSJCS" id="idFacturacion" width="400"  />
							</td>
							<td>
								<siga:Idioma key="menu.sjcs.ecom.justificacion"/>
							</td>
							<td>
								<siga:Select queryId="getJustificacionesCatalunya" id="idJustificacion" width="400"  />
							
							</td>
							<td></td>		
							<td></td>
						</c:otherwise>
					
					</c:choose>
					
					
						
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
		<html:hidden property="idJustificacion"/>
		<html:hidden property="idDevolucion"/>
		<html:hidden property="error"/>
		<html:hidden property="pathFile"/>
		
</html:form>
	
<div id="dialogoInsercion"  title='<bean:message key="menu.sjcs.ecom.devolucion"/>' style="display:none">		
	<div>&nbsp;</div>

  	<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.leyenda.datosJustificacion">
  		
  		<div class="labelText">
   			<label for="descripcion"  style="width:100px;float:left;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/></label>
   			<input type="text" id="descripcionNew" maxlength="100" size="40" />
		</div>
		
		<div class="labelText">
			<label for="idJustificacion" style="width:100px;float:left;color: black"><siga:Idioma key="menu.sjcs.ecom.justificacion"/></label>
			<siga:Select queryId="getJustificacionesCatalunya" id="idJustificacionNew" required="true" width="300" lines="4" />
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