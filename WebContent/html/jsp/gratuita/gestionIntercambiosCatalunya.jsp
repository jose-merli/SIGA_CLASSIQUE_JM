<!DOCTYPE html>
<html>
<head>
<!-- gestionIntercambiosCatalunya.jsp -->

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
		buscarIntercambios();
	}
	
	function buscar() {
		return buscarIntercambios();
	}
	
	function buscarIntercambios(pagina,parametros) {
		
        var idInstitucion = document.GestionEconomicaCatalunyaForm.idInstitucion.value;
        var idColegio = '';
        if(document.GestionEconomicaCatalunyaForm.idColegio)
        	idColegio = document.GestionEconomicaCatalunyaForm.idColegio.value;
        var descripcion = document.GestionEconomicaCatalunyaForm.descripcion.value;
        var anio = document.GestionEconomicaCatalunyaForm.anio.value;
        var idPeriodo = document.GestionEconomicaCatalunyaForm.idPeriodo.value;
        var idEstado = document.GestionEconomicaCatalunyaForm.idEstado.value;
        var data = "idInstitucion="+idInstitucion;
        if(idEstado!='')
        	data += "&idEstado="+idEstado;
        
        if(descripcion!='')
        	data += "&descripcion="+descripcion;
        if(anio!='')
        	data += "&anio="+anio;
        if(idPeriodo!='')
        	data += "&idPeriodo="+idPeriodo;
        
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
		
		openDialog('dialogoInsercion');
	}
	
	function openDialog(dialogo){
		jQuery("#"+dialogo).dialog(
				{
				      height: 270,
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
			document.forms['FormularioGestion'].pathFile.value = jQuery("#theFileNew").val();
			
			if(document.forms['FormularioGestion'].anio.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.anio'/>"+ '\n';
				
			}
			if(!isNumero(document.forms['FormularioGestion'].anio.value)){
				
				error += "<siga:Idioma key='errors.short' arg0='gratuita.mantActuacion.literal.anio'/>"+ '\n';
				document.forms['FormularioGestion'].anio.value ='';
				
			}
			if(document.forms['FormularioGestion'].idPeriodo.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.calendarioGuardias.literal.periodo'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.descripcion'/>"+ '\n';
				
			}
			
			if (error=='')
				document.FormularioGestion.modo.value = "insertaIntercambios";
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
	function editaIntercambio(fila) {
		document.forms['FormularioGestion'].modo.value = "editaIntercambio";
		var idIntercambio = 'idIntercambio_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById(idIntercambio).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].target = 'mainWorkArea';
		document.forms['FormularioGestion'].submit();
		
	}
	
	function borraIntercambio(fila) {
		sub();
		document.forms['FormularioGestion'].modo.value = "borraIntercambio";
		var idIntercambio = 'idIntercambio_' + fila ;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].submit();
		
	}
	
		
	function inicio() {
		if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
			alert(document.getElementById("mensajeSuccess").value,'success');
		}
		if(document.getElementById("volverBusqueda") && document.getElementById("volverBusqueda").value!=''){
			buscarIntercambios('',document.getElementById("volverBusqueda").value);
		}
		
		
		
	}	
	function enviarIntercambiosGEN(fila) {
		sub();
		var idIntercambio = 'idIntercambio_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById(idIntercambio).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].modo.value = "enviarIntercambiosGEN";
		
		document.forms['FormularioGestion'].submit();
	}
	function enviarIntercambiosCICAC(fila) {
		sub();
		var idIntercambio = 'idIntercambio_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById(idIntercambio).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById(idInstitucion).value;
		document.forms['FormularioGestion'].modo.value = "enviarIntercambiosCICAC";
		
		document.forms['FormularioGestion'].submit();
	}
	
			
		</script>
	</head>

	<siga:Titulo titulo="menu.sjcs.ecom.justificacion" localizacion="menu.sjcs.ecomunicaciones.localizacion"/>

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
		
		<siga:ConjCampos leyenda="gratuita.gestionInscripciones.datosSolicitud.leyenda">
			<table width="100%" border="0">
				<tr>
					<td width="10"></td>
					<td width="15"></td>
					<td width="10"></td>
					<td width="15"></td>
					<td width="10"></td>
					<td width="15"></td>
					<td width="10"></td>
					<td width="15"></td>
					<td></td>
					
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/>
					</td>
					<td>
						<html:text property="descripcion" size="25"  styleClass="box"  />
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/>
					</td>
					<td>
						<siga:Select queryId="getPeriodos" id="idPeriodo" width="100" />
					</td>
					<td class="labelText">
					
						<siga:Idioma key="gratuita.mantActuacion.literal.anio"/>
					</td>
					<td>
						<html:text property="anio" size="4" maxlength="4"  styleClass="box"  />
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
					</td>
					<td >
						<siga:Select queryId="getEstadosJustificacion" id="idEstado" params="${paramsEstados}" width="180" />
					</td>		
					<td>
						
					</td>
					</tr>
					<c:if test="${USRBEAN.location=='3001'}">
						<tr>
							<td class="labelText">
								<siga:Idioma key="administracion.informes.literal.colegio"/>
								
							</td>
							<td>
								<siga:Select id="idColegio" queryId="getColegiosDeConsejo"  width="200" />	
								
							</td>
							<td colspan="6">
						
							</td>
							<td>
						
							</td>
							</tr>
					</c:if>
						
			</table>				
		</siga:ConjCampos>


	<siga:ConjBotonesBusqueda botones="${botonesBusqueda}"/>
		<div id="divListado"></div>	
	</html:form>
	
	<html:form action="${path}"   name="FormularioGestion" method="POST" enctype="multipart/form-data" type ="com.siga.gratuita.form.GestionEconomicaCatalunyaForm"  target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"  />
		<html:hidden property="idIntercambio" />
		<html:hidden property="descripcion"/>
		<html:hidden property="idPeriodo"/>
		<html:hidden property="idDevolucion"/>
		<html:hidden property="anio"/>
		<html:hidden property="error"/>
		<html:hidden property="pathFile"/>
		
</html:form>
	
		
<div id="dialogoInsercion"  title='<bean:message key="informes.genericos.comunicacion"/>' style=" background-color:white; display:none">
	
<fieldset style="background-color:white;">
	<legend  >
		<siga:Idioma key="comunicaciones.leyenda.informacionIntercambio"/>
	</legend>
  
  		
  		<div class="labelText">
   			<label for="descripcion"  style="width:100px;float:left;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/></label>
   			<input type="text" id="descripcionNew" maxlength="100" size="35" />
		</div>
		<div class="labelText">
   			<label for="anio"  style="width:100px;float:left;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.anio"/></label>
   			<input type="text" id="anioNew" maxlength="4" size="6" />
   			<label for="trimestre" style="width:100px;color: black"><siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/></label>
			<siga:Select queryId="getPeriodos" id="idPeriodoNew" width="150" required="true" />
		</div>
		<div class="labelText">
			<label for="file"   style="width:100px;float:left;color:black">Fichero</label><input type="file"  id="theFileNew" size="30" />
			</div>
			<div class="labelText"><label style="color:black">Seleccione el fichero generado de la certificacion del trimestre para enviarselo a la Generalitat</label></div>
	</fieldset>

</div>





		
		<!-- FIN: BOTONES BUSQUEDA -->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
		
		
</body>
</html>