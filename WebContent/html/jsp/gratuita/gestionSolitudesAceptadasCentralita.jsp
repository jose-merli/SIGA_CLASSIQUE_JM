<!DOCTYPE html>
<html>
<head>
<!-- gestionSolitudesAceptadasCentralita.jsp -->

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


<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>

	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
		

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<siga:TituloExt titulo="menu.sjcs.solicitudesAceptadasCentralita" localizacion="sjcs.solicitudaceptadacentralita.localizacion"/>
		

<script type="text/javascript">
	jQuery(function(){
		jQuery("#idJuzgado").on("change", function(){
			return onChangeJuzgado(jQuery(this).val());
		});
	});
	
	jQuery(function(){
		jQuery("#idComisaria").on("change", function(){
			return onChangeComisaria(jQuery(this).val());
		});
	});
	
	function onChangeComisaria(valor){
		jQuery("#idJuzgado").val("");
		jQuery("#idJuzgado_searchBox").val("");
	}
	function onChangeJuzgado(valor){
		jQuery("#idComisaria").val("");
		jQuery("#idComisaria_searchBox").val("");
		
	}	
		
	function limpiarColegiado(){
		document.SolicitudAceptadaCentralitaForm.idPersona.value = '';
		document.SolicitudAceptadaCentralitaForm.colegiadoNumero.value = '';
		document.SolicitudAceptadaCentralitaForm.colegiadoNombre.value = '';
	}
	
	function buscarColegiado(){
		var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
		if (resultado!=undefined && resultado[0]!=undefined ){
			
			document.SolicitudAceptadaCentralitaForm.idPersona.value       = resultado[0];
			document.SolicitudAceptadaCentralitaForm.colegiadoNumero.value    = resultado[2];
			document.SolicitudAceptadaCentralitaForm.colegiadoNombre.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
		}
	}
	
	function refrescarLocal(){
		buscarSolicitudesAceptadas();
	}
	
	function onChangeColegiado(){
		var colegiadoNumero = document.getElementById("colegiadoNumero").value;
		var idInstitucion = document.SolicitudAceptadaCentralitaForm.idInstitucion.value;
		if(colegiadoNumero!=""){
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
				type: "POST",
				url: "/SIGA/JGR_GestionSolicitudesAceptadasCentralita.do?modo=getColegiadoAjax",
				data: "colegiadoNumero="+colegiadoNumero+"&idInstitucion="+idInstitucion,
				dataType: "json",
				success: function(json){		
	    	   		document.SolicitudAceptadaCentralitaForm.idPersona.value       = json.idPersona;
					document.SolicitudAceptadaCentralitaForm.colegiadoNumero.value    = json.colegiadoNumero;
					document.SolicitudAceptadaCentralitaForm.colegiadoNombre.value   = json.colegiadoNombre;
					fin();
				},
				error: function(e){
					alert('Error de comunicaci�n: ' + e);
					fin();
				}
			});
		}
		else{
			document.SolicitudAceptadaCentralitaForm.idPersona.value       = "";
			document.SolicitudAceptadaCentralitaForm.colegiadoNumero.value    = "";
			document.SolicitudAceptadaCentralitaForm.colegiadoNombre.value   = "";
		}
		
	}
	function buscar() {
		return buscarSolicitudesAceptadas();
	}
	function buscarSolicitudesAceptadas() {
		
		
		
		
        var idInstitucion = document.SolicitudAceptadaCentralitaForm.idInstitucion.value;
        var idSolicitudAceptada = document.SolicitudAceptadaCentralitaForm.idSolicitudAceptada.value;
        var idEstado = document.SolicitudAceptadaCentralitaForm.idEstado.value;
        var idTurno = document.SolicitudAceptadaCentralitaForm.idTurno.value;
        var idGuardia = document.SolicitudAceptadaCentralitaForm.idGuardia.value;
        var fechaDesde = document.SolicitudAceptadaCentralitaForm.fechaDesde.value;
        var fechaHasta = document.SolicitudAceptadaCentralitaForm.fechaHasta.value;
        var idComisaria = document.SolicitudAceptadaCentralitaForm.idComisaria.value;
        var idJuzgado = document.SolicitudAceptadaCentralitaForm.idJuzgado.value;
        var idPersona = document.SolicitudAceptadaCentralitaForm.idPersona.value;
        //Voy a pasar tambien el numero de colegiado para al volver de la edicion este seteado en el formulario de busqeuda y podamos poner los datos correctos
        var colegiadoNumero =  document.SolicitudAceptadaCentralitaForm.colegiadoNumero.value
        
        
        var data = "idInstitucion="+idInstitucion;
        if(idEstado=='-1'){
        	error = "<siga:Idioma key='errors.required' arg0='estado'/>"+ '\n';
        	alert(error);
        	return;
        }else
        	data += "&idEstado="+idEstado;

        if(idSolicitudAceptada!='')
        	data += "&idSolicitudAceptada="+idSolicitudAceptada;
        
        if(idTurno!='')
        	data += "&idTurno="+idTurno;
        if(idGuardia!='')
        	data += "&idGuardia="+idGuardia;
        if(fechaDesde!='')
        	data += "&fechaDesde="+fechaDesde;
        if(fechaHasta!='')
        	data += "&fechaHasta="+fechaHasta;
        if(idComisaria!='')
        	data += "&idComisaria="+idComisaria;
        if(idJuzgado!='')
        	data += "&idJuzgado="+idJuzgado;
        if(idPersona!='')
        	data += "&idPersona="+idPersona;
        if(colegiadoNumero!='')
        	data += "&colegiadoNumero="+colegiadoNumero;
        
        
        if(idEstado=='0'){
        	jQuery("#idValidarSolicitudAceptada").show();
        	jQuery("#idDenegarSolicitudAceptada").show();
        	jQuery("#idActivarSolicitudAceptadaDenegada").hide();
        }else if(idEstado=='1'){
        	jQuery("#idValidarSolicitudAceptada").hide();
        	jQuery("#idDenegarSolicitudAceptada").hide();
        	jQuery("#idActivarSolicitudAceptadaDenegada").hide();
        }else if(idEstado=='2'){
        	jQuery("#idValidarSolicitudAceptada").hide();
        	jQuery("#idDenegarSolicitudAceptada").hide();
        	jQuery("#idActivarSolicitudAceptadaDenegada").show();

        }
        sub();
	     jQuery.ajax({
            type: "POST",
            url: "/SIGA/JGR_GestionSolicitudesAceptadasCentralita.do?modo=getAjaxBusqueda",
            data: data,
            success: function(response){
                jQuery('#divListado').html(response);
	                fin();
            },
            error: function(e){
            	fin();
                alert('Error: ' + e);
            }
        });
    }
	
	function validar(fila) {				
		var idSolicitudAceptada = 'idSolicitudAceptada_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.FormAValidar.target = 'mainWorkArea';
		document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
	   	document.FormAValidar.idSolicitudAceptada.value = document.getElementById(idSolicitudAceptada).value;
	 	document.FormAValidar.modo.value = "editarSolicitudAceptada";
	 	document.FormAValidar.submit();
	 }
	
	function denegar(fila) {		
		var idSolicitudAceptada = 'idSolicitudAceptada_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
	   	document.FormAValidar.idSolicitudAceptada.value = document.getElementById(idSolicitudAceptada).value;
	 	document.FormAValidar.modo.value = "denegarSolicitudAceptada";
	 	document.FormAValidar.submit();
	 }
	function activar(fila) {				
		var idSolicitudAceptada = 'idSolicitudAceptada_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
	   	document.FormAValidar.idSolicitudAceptada.value = document.getElementById(idSolicitudAceptada).value;
	 	document.FormAValidar.modo.value = "activarSolicitudAceptadaDenegada";
	 	document.FormAValidar.submit();
	 }
	function marcarDesmarcarTodos(o) { 
		var ele = document.getElementsByName("chkSolicitud");
		for (i = 0; i < ele.length; i++) {
			if(!ele[i].disabled)
				ele[i].checked = o.checked;
		}
  	 }
  	function accionMasiva(modoAccionMasiva) {
		var ele = document.getElementsByName('chkSolicitud');
		
		var datosMasivos='';
		var estadoFinal="";
		for (fila = 0; fila < ele.length; fila++) {
		    if(ele[fila].checked){
		    	filaTabla = fila+1;
		    	var idSolicitudAceptadaFila = 'idSolicitudAceptada_' + filaTabla ;
				var idInstitucionFila = 'idInstitucion_' + filaTabla ;
				idSolicitudAceptada = document.getElementById(idSolicitudAceptadaFila).value;
			   	idInstitucion = document.getElementById(idInstitucionFila).value;
			   	datosMasivos = datosMasivos +","+ idInstitucion+"##"+idSolicitudAceptada+"##"; 			
			}			
		}

		if(datosMasivos==''){
			alert('<siga:Idioma key="general.message.seleccionar"/>');
			return false;
		}
		datosMasivos=datosMasivos.substring(1);
		document.FormAValidar.datosMasivos.value=datosMasivos;
		document.FormAValidar.modo.value=modoAccionMasiva;
		document.FormAValidar.submit();		
	}
  	
  	

	function consultaInscripcion(fila) {
		var idSolicitudAceptada = 'idSolicitudAceptada_' + fila ;
		var idInstitucion = 'idInstitucion_' + fila ;
		document.FormAValidar.target = 'mainWorkArea';
		document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
	   	document.FormAValidar.idSolicitudAceptada.value = document.getElementById(idSolicitudAceptada).value;
	 	document.FormAValidar.modo.value = "consultarSolicitudAceptada";
	 	document.FormAValidar.submit();																							 
	}
		
	function inicio() {
		if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
			alert(document.getElementById("mensajeSuccess").value,'success');
		}
		//Ponemos el colegiado si hubiera
		onChangeColegiado();
		buscarSolicitudesAceptadas();
		
	}	
			
		</script>
	</head>

	<body onload="return inicio();">
		<c:set var="parametrosComboComisaria" value="{\"idcomisaria\":\"-1\"}"/>
		<input type="hidden" id="mensajeSuccess" value="${mensajeSuccess}"/>
			
		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
		<html:form action="${path}"  method="POST" target="mainWorkArea">
			<html:hidden property="modo"/>
			<html:hidden property="idInstitucion"/>
			

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
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.idAvisoCentralita"/>
						</td>
						<td>
						
							<html:text property="idSolicitudAceptada" size="10" maxlength="10" styleClass="box"  />
								
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
						</td>
						<td>
						
							<html:select property="idEstado" styleClass="boxCombo" onchange="return buscarSolicitudesAceptadas();" >						
								<html:option value="-1"><siga:Idioma key="general.combo.seleccionar"/></html:option>
								<html:option value="0" ><siga:Idioma key="gratuita.gestionInscripciones.estado.pendiente"/></html:option>
								<html:option value="1"><siga:Idioma key="gratuita.gestionInscripciones.estado.confirmada"/></html:option>
								<html:option value="2"><siga:Idioma key="gratuita.gestionInscripciones.estado.denegada"/></html:option>
							</html:select>
						</td>
												
						<td class="labelText" >
							
						</td>
						<td >
							
						</td>	
					</tr>
			
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno"/>
						</td>
						<td colspan="3">
							<siga:Select queryId="getTurnos" id="idTurno" queryParamId="idturno" childrenIds="idGuardia" selectedIds="${idTurnoSelected}"  width="300" />
							
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia"/>
						</td>
						<td>
							<siga:Select queryId="getGuardiasDeTurno" id="idGuardia"  parentQueryParamIds="idturno" params="${paramsGuardiasDeTurno}" selectedIds="${idGuardiaSelected}" width="300" />
						</td>
					</tr>
			
					<tr>				
						<td class="labelText">
							<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.fechaLlamada"/>&nbsp;<siga:Idioma key="general.literal.desde"/>
							
							
						</td>
						
						<td colspan="3">
							<siga:Fecha nombreCampo="fechaDesde"  valorInicial="${SolicitudAceptadaCentralitaForm.fechaDesde}" />							
						</td>
		
						<td class="labelText">
							<siga:Idioma key="sjcs.solicitudaceptadacentralita.literal.fechaLlamada"/>&nbsp;<siga:Idioma key="general.literal.hasta"/>
						</td>
						<td>
							<siga:Fecha nombreCampo="fechaHasta" valorInicial="${SolicitudAceptadaCentralitaForm.fechaHasta}" />
						</td>					
					</tr>
					
					<tr>
					
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria"/>
						</td>
												
						<td id="tdSelectComisaria" colspan="5">
							<siga:Select id="idComisaria" 
										queryId="getComisariasDeInstitucion" 
										params="${parametrosComboComisaria}"
										showSearchBox="true"
										searchkey="CODIGOEXT"
										searchBoxMaxLength="10"
										searchBoxWidth="8"
										width="420"
										selectedIds="${idComisariaSelected}"
										/>
						
						
							
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado"/>
						</td>
											
						<td id="tdSelectJuzgado" colspan = "5">
						
							<siga:Select id="idJuzgado" 
										queryParamId="idjuzgado"
										queryId="getJuzgados"
										showSearchBox="true"
										searchkey="CODIGOEXT2"
										searchBoxMaxLength="10"
										searchBoxWidth="8"
										width="420"
										selectedIds="${idJuzgadoSelected}"
										/>
						
							
						</td>
						
					
					</tr>
					
					<tr>
						<td colspan="6">
							<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.titulo">
								<table align="left">
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.seleccionColegiadoJG.literal.colegiado"/>
											
										</td>
										<td>
											<html:hidden property="idPersona"/>
											<html:text styleId="colegiadoNumero" property="colegiadoNumero" size="4" maxlength="9" styleClass="box" onchange="return onChangeColegiado();" />
										</td>
										
										<td>
											<html:text styleId="colegiadoNombre" property="colegiadoNombre" size="50" maxlength="50" styleClass="box" readonly="true" />
										</td>
										<td>
											<!-- Boton buscar --> 
											<input type="button" class="button" id="idButton" name="Buscar" value="<siga:Idioma key='general.boton.search'/>" onClick="buscarColegiado();" /> 
											<!-- Boton limpiar -->
											&nbsp;<input type="button" class="button" id="idButton" name="Limpiar" value="<siga:Idioma key='general.boton.clear'/>" onClick="limpiarColegiado();" />
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>				
			</siga:ConjCampos>
	
			<siga:ConjBotonesBusqueda botones="B"  titulo="gratuita.busquedaAsistencias.literal.titulo"/>
			
			
			<div id="divListado"></div>	

			<table class="botonesDetalle" align="center">
				<tr>
					<td style="width: 900px;">
						&nbsp;
					</td>
					
					<td class="tdBotones">
						<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.denegarSolicitud'/>"  id="idDenegarSolicitudAceptada" onclick="return accionMasiva('denegarSolicitudesAceptadas');" class="button" style="display: none" name="idButton"   value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.denegarSolicitud'/>">
					</td>
					<td class="tdBotones">
						<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.activarSolicitud'/>"  id="idActivarSolicitudAceptadaDenegada" onclick="return accionMasiva('activarSolicitudesAceptadasDenegadas');" style="display: none" class="button" name="idButton"    value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.activarSolicitud'/>">
					</td>
					<td class="tdBotones">
						<input type="button" alt="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>"	id="idValidarSolicitudAceptada" onclick="return accionMasiva('guardarSolicitudesAceptadas');" class="button" style="display: none"  name="idButton" value="<siga:Idioma key='sjcs.solicitudaceptadacentralita.boton.validarSolicitud'/>" />
					</td>
					
				</tr>
			</table>
		</html:form>
		
		<!-- FIN: CAMPOS DE BUSQUEDA-->
		<!-- Formularios auxiliares -->
		<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="" style="display:none">
			<input type="hidden" name="actionModal" value="" />
			<input type="hidden" name="modo" value="abrirBusquedaModal" />
		</html:form>
		
		<html:form action="${path}"  name="FormAValidar" type ="com.siga.gratuita.form.SolicitudAceptadaCentralitaForm" target="submitArea">
			<html:hidden property="modo"/>
			<html:hidden property="idInstitucion" />
			<html:hidden property="idSolicitudAceptada" />
			<html:hidden property="datosMasivos" />
			
			
		</html:form>
		
				
		<!-- FIN: BOTONES BUSQUEDA -->
		
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
		
		
	</body>
</html>