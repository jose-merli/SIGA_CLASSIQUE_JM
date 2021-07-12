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
		if(document.forms['FormularioGestion'].cantidadAsunto)
			openDialog('dialogoInsercion',650,400);
		else
			openDialog('dialogoInsercion',600,240);
	}
	
	function openDialog(dialogo,valWidth,valHeight){
		jQuery("#"+dialogo).dialog(
				{
				      height: valHeight,
				      width: valWidth,
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
	function accionInsercion(dialogo){
		
		alert("<siga:Idioma key='messages.enProceso'/>");
		
		
		error = '';
		if(dialogo=='dialogoInsercion'){
			
			
			
			if(document.forms['FormularioGestion'].theFile.value==''){
				error += ""+ '\n';
				
			}
			
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
			
			if(document.forms['FormularioGestion'].cantidadAsunto){
				if(document.forms['FormularioGestion'].cantidadAsunto.value==''){
					error += "<siga:Idioma key='errors.required' arg0='Assumptes. Quantitat'/>"+ '\n';
				}else if(!numerico(document.forms['FormularioGestion'].cantidadAsunto.value)){
					error += "<siga:Idioma key='errors.integer' arg0='Assumptes. Quantitat'/>"+ '\n';
				}
				
			}
			
			if(document.forms['FormularioGestion'].importeAsunto){
				if(document.forms['FormularioGestion'].importeAsunto.value==''){
					error += "<siga:Idioma key='errors.required' arg0='Assumptes. Import total'/>"+ '\n';
				}else if(!validateDecimal(document.forms['FormularioGestion'].importeAsunto.value)){
					error += "<siga:Idioma key='errors.double' arg0='Assumptes. Import total'/>"+ '\n';
				}
				
			}
			if(document.forms['FormularioGestion'].importeDevoluciones){
				if(document.forms['FormularioGestion'].importeDevoluciones.value=='' ){
					error += "<siga:Idioma key='errors.required' arg0='Devolucions. Import total'/>"+ '\n';
				}else if( !validateDecimal(document.forms['FormularioGestion'].importeDevoluciones.value)){
					error += "<siga:Idioma key='errors.double' arg0='Devolucions. Import total'/>"+ '\n';
				}
				
			}
			
			if(document.forms['FormularioGestion'].valorInteres){
				if(document.forms['FormularioGestion'].valorInteres.value=='' ){
					error += "<siga:Idioma key='errors.required' arg0='Interessos. Valor'/>"+ '\n';
				}else if( !validateDecimal(document.forms['FormularioGestion'].valorInteres.value)){
					error += "<siga:Idioma key='errors.double' arg0='Interessos. Valor'/>"+ '\n';
				}
				
			}
			if(document.forms['FormularioGestion'].importeFinal){
				if(document.forms['FormularioGestion'].importeFinal.value=='' ){
					error += "<siga:Idioma key='errors.required' arg0='Import final'/>"+ '\n';
				}else if(!validateDecimal(document.forms['FormularioGestion'].importeFinal.value)){
					error += "<siga:Idioma key='errors.double' arg0='Import final'/>"+ '\n';
				}
				
			}
			if(document.forms['FormularioGestion'].importeAnticipo){
				if(document.forms['FormularioGestion'].importeAnticipo.value=='' ){
					error += "<siga:Idioma key='errors.required' arg0='Bestreta. Import'/>"+ '\n';
				}else if( !validateDecimal(document.forms['FormularioGestion'].importeAnticipo.value)){
					error += "<siga:Idioma key='errors.double' arg0='Bestreta. Import'/>"+ '\n';
				}
				
			}
			if(document.forms['FormularioGestion'].acumuladoTrimetreActual){
				if(document.forms['FormularioGestion'].acumuladoTrimetreActual.value=='' ){
					error += "<siga:Idioma key='errors.required' arg0='Import acumulat del trimestre anterior'/>"+ '\n';
				}else if(!validateDecimal(document.forms['FormularioGestion'].acumuladoTrimetreActual.value)){
					error += "<siga:Idioma key='errors.double' arg0='Import acumulat del trimestre anterior'/>"+ '\n';
				}
				
			}
			if(document.forms['FormularioGestion'].acumuladoTrimetreAnterior){
				if(document.forms['FormularioGestion'].acumuladoTrimetreAnterior.value==''){
					error += "<siga:Idioma key='errors.required' arg0='Import acumulat del trimestre actual'/>"+ '\n';
				}else if( !validateDecimal(document.forms['FormularioGestion'].acumuladoTrimetreAnterior.value)){
					error += "<siga:Idioma key='errors.double' arg0='Import acumulat del trimestre actual'/>"+ '\n';
				}
				
			}
			
			
			if(document.forms['FormularioGestion'].descripcion && document.forms['FormularioGestion'].descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantActuacion.literal.descripcion'/>"+ '\n';
				
			}
			
			if (error=='')
				document.forms['FormularioGestion'].modo.value = "insertaIntercambios";
		}
		
		if (error!=''){
			alert(error);
			fin();
			return false;
		}
		closeDialog(dialogo);
	    document.forms['FormularioGestion'].submit();
	}

			
		</script>
	</head>

	<siga:Titulo titulo="menu.sjcs.ecom.justificacion" localizacion="menu.sjcs.ecomunicaciones.localizacion"/>

<c:set var="botonesBusqueda" value="N,B" />


<body onload="return inicio();">
 
<input type="hidden" id="volverBusqueda" value="${volverBusqueda}" class="inputNotSelect"/>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" target="mainWorkArea" enctype="multipart/form-data" >
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
					<div>
						<siga:Select queryId="getPeriodos" id="idPeriodo"  width="100" />
						</div>
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
						<div>
						<siga:Select queryId="getEstadosJustificacion" id="idEstado"  params="${paramsEstados}" width="180" />
						</div>
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
								<div>
									<siga:Select id="idColegio" queryId="getColegiosDeConsejo"  width="200" />	
								</div>
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
	



		
<div id="dialogoInsercion"  title='<bean:message key="informes.genericos.comunicacion"/>' style=" background-color:white; display:none">

<html:form action="${path}"   name="FormularioGestion"  enctype="multipart/form-data" method="POST"  type ="com.siga.gratuita.form.GestionEconomicaCatalunyaForm"  target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"  />
		<html:hidden property="idIntercambio" />
		<html:hidden property="error"/>
		

<fieldset style="background-color:white;">
	<legend  >
		<siga:Idioma key="comunicaciones.leyenda.informacionIntercambio"/>
	</legend>
  
  		<c:if test="${USRBEAN.location!='3001'}">
			<div class="labelText">
   				<label for="descripcion"  style="width:120px;olor: black"><siga:Idioma key="gratuita.mantActuacion.literal.descripcion"/></label>
   				<html:text property="descripcion" size="35" maxlength="100" />
			</div>	
		</c:if>
  		
		<div class="labelText">
   			<label for="anio"  style="width:120px;color: black"><siga:Idioma key="gratuita.mantActuacion.literal.anio"/></label>
   			<html:text property="anio" size="6" maxlength="4" />
   			<label for="trimestre" style="width:100px;color: black"><siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/></label>
			
			<siga:Select queryId="getPeriodos" id="idPeriodo" width="150" required="true" />
		</div>
		<div class="labelText">
			<label for="file"   style="width:120px;color:black"><siga:Idioma key="administracion.confInterfaz.fichero"/>&nbsp;<siga:Idioma key="menu.sjcs.ecom.certificacion"/></label>&nbsp;<html:file  property="theFile"/>
			</div>

		<c:if test="${USRBEAN.location=='3001'}">
			<div class="labelText">
	   			<label for="cantidadAsunto"  style="width:120px;color: black">Assumptes. Quantitat</label>
	   				<html:text property="cantidadAsunto" size="13" maxlength="6" />
	   			<label for="importeAsunto"  style="width:120px;color: black">Assumptes. Import total</label>
					<html:text property="importeAsunto" size="13" maxlength="13" />
			</div>
			<div class="labelText">
	   			<label for="importeDevoluciones"  style="width:120px;  color: black">Devolucions. Import total</label>
	   			<html:text property="importeDevoluciones" size="13" maxlength="13" />
	   		
				<label for="valorInteres"  style="width:120px;color: black">Interessos. Valor</label>
				<html:text  				property="valorInteres" size="13" maxlength="13" />
			</div>
			<div class="labelText">		
				<label for="importeFinal"  style="width:120px;color: black">Import final</label>
					<html:text property="importeFinal" size="13" maxlength="13" />
			</div>
			
			<div class="labelText">
				<label for="importeAnticipo"  style="width:120px;color: black">Bestreta. Import</label>
					<html:text property="importeAnticipo" size="13" maxlength="13" />
				
			</div>
			
			<div class="labelText">
				<label for="acumuladoTrimetreActual"  style="width:120px;color: black">Import acumulat del trimestre anterior</label>
					<html:text property="acumuladoTrimetreActual" size="13" maxlength="13" />
			
			</div>
			<div class="labelText">
				<label for="acumuladoTrimetreAnterior"  style="width:120px;color: black">Import acumulat del trimestre actual.</label>
					<html:text property="acumuladoTrimetreAnterior" size="13" maxlength="13" />
			</div>
			<div class="labelText">&nbsp;</div>
		</c:if>
			
			
	</fieldset>
		
			
</html:form>
</div>

		
		<!-- FIN: BOTONES BUSQUEDA -->
<iframe name="submitArea"  src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
<script type="text/javascript">

</script>
		
		
</body>

</html>