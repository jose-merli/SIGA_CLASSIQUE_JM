<!DOCTYPE html>
<html>
<head>
<!-- volantesExpress.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>

	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.volantesExpres.literal.titulo"	localizacion="gratuita.volantesExpres.literal.localizacion" />

	<script type="text/javascript">
		jQuery.noConflict();
	
		function init() {
		   	jQuery("#colegiadosSustituidos").attr("disabled","disabled");
		   	jQuery("#colegiadosGuardia").attr("disabled","disabled");
		   	jQuery("#turnos").attr("disabled","disabled");
		   	jQuery("#guardias").attr("disabled","disabled");
			document.getElementById('fechaGuardia').value="${VolantesExpressForm.fechaGuardia}";
		   	accionCalendario();
		   	
			if (document.VolantesExpressForm.centroOjuzgado != "" && document.VolantesExpressForm.centroOjuzgado.value == "juzgado") {
				jQuery("#lugar_juzgado").prop("checked", true);
				document.VolantesExpressForm.lugar_centro.onclick();
			} else {
				jQuery("#lugar_centro").prop("checked", true);
				document.VolantesExpressForm.lugar_centro.onclick();
			}
		}
		
		function accionCalendario() {
			// Abrimos el calendario			
			
			if (document.getElementById('fechaGuardia').value!='') {
				document.VolantesExpressForm.turnos.value= '';
				document.VolantesExpressForm.guardias.value= '';
				document.VolantesExpressForm.colegiadosGuardia.value= '';
				document.VolantesExpressForm.colegiadosSustituidos.value= '';
				document.VolantesExpressForm.idTurno.value = '';
			    document.VolantesExpressForm.idGuardia.value = '';
				document.VolantesExpressForm.idColegiadoGuardia.value = document.getElementById('idColegiadoGuardia').value;
				document.VolantesExpressForm.idColegiadoSustituido.value = '';
				document.VolantesExpressForm.fechaGuardia.value =  document.getElementById('fechaGuardia').value;
				document.VolantesExpressForm.idTipoAsistenciaColegio.value =  document.getElementById('idTipoAsistenciaColegio').value;
				document.getElementById('fechaGuardia').onchange();
				
		 	}else{
				//Si da a reset no viene nada por lo que actualizamos. si viene con fecha
				//es que ha cerrado desde el aspa, lo dejamos como estuviera(no hacemos nada) 		 		
		 		 
				if(document.getElementById('fechaGuardia').value=='')
					document.getElementById('fechaGuardia').onchange();
				
			}
		}
		
		function  postAccionFechaGuardia(){
			if("${VolantesExpressForm.idTurno}" != '') {
				document.getElementById('turnos').value="${VolantesExpressForm.idTurno}";
				document.getElementById('turnos').onchange();
			}
		}
	
		function preAccionTurno(){
			jQuery("#guardias").attr("disabled","disabled");
		}
		
		function postAccionTurno(){
			if ("${VolantesExpressForm.idGuardia}" && "${VolantesExpressForm.idGuardia}" != "") {
				document.getElementById('guardias').value="${VolantesExpressForm.idGuardia}";
				document.getElementById('guardias').onchange();
			}
		}	
		
		function preAccionGuardia(){
		}
		
		function postAccionGuardia(){
			
			if((document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')){
				//Ahora mismo si hay seleccionado un letrado no se va a borrar aunque haya cambio de guardia 
				if(document.VolantesExpressForm.idColegiado&&document.VolantesExpressForm.idColegiado.value!=''){
					postAccionColegiado();
				}else{
					var optionsColegiadoGuardia = document.getElementById("colegiadosGuardia");
					var encontrado;
					if(optionsColegiadoGuardia.length==2){
						optionsColegiadoGuardia.selectedIndex=1;
						document.getElementById('colegiadosGuardia').onchange();
					}
				}
				
				// para que no se pueda cambiar la fecha a mano, que luego es mas dificil controlar que datos traer o no (cuando vuelve de actuacion)
				jQuery("#fechaGuardia").attr("disabled","disabled");
			}
			rellenaTipoAsistencia();
			
		}
		
		function preAccionColegiadoGuardia(){
			sub();
		}
		
		function postAccionColegiadoGuardia(){
			fin();
			if (document.VolantesExpressForm.idColegiadoGuardia != null && document.VolantesExpressForm.idColegiadoGuardia.value != "") {
				idColegiadoGuardia = document.VolantesExpressForm.idColegiadoGuardia.value;
			}
			
			if (idColegiadoGuardia!="-1"){
				document.VolantesExpressForm.idColegiadoSustituido.value = '-1';
				jQuery("#colegiadosSustituidos").attr("disabled","disabled");
			}else{
				jQuery("#colegiadosSustituidos").removeAttr("disabled");
	 		}
			
			actualizarResultados();
		}
		
		function preAccionColegiado(){
		// 	document.getElementById("buscaColegiadoManual").value = "true";
		}
	
		function postAccionColegiado(){
			if(document.getElementById("buscaColegiadoManual").value == "true"){
				document.getElementById("buscaColegiadoManual").value = "false";
				document.getElementById('fechaGuardia').onchange();
				
			
			}else{
				//Comprobamos que esta en el combo
				var idColegiado = document.VolantesExpressForm.idColegiado.value;
				var optionsColegiadoGuardia = document.getElementById("colegiadosGuardia");
				var encontrado;
				for(var i = 0 ; i <optionsColegiadoGuardia.length ; i++) {
					var option = optionsColegiadoGuardia.options[i].value;
					if(option === idColegiado){
						encontrado=i;
					}
				}
				if (encontrado){
					optionsColegiadoGuardia.selectedIndex=encontrado;
				} else {
					optionsColegiadoGuardia.selectedIndex=0;
				}
				actualizarResultados();
			}
		}
		
		function rellenaTipoAsistencia() {
			sub();
			var idGuardia = document.getElementById('guardias').value;
			var idTurno = document.getElementById('turnos').value;
			var comboTipoAsistenciaColegio = document.getElementById('idTipoAsistenciaColegio');
			var optionTipoAsistenciaColegio = comboTipoAsistenciaColegio.options;
			if(idGuardia!='' &&idGuardia!='-1' && idTurno!=''){
				var txtSelect = '--<siga:Idioma key="general.boton.seleccionar"/>';
				jQuery.ajax({   
			           type: "POST",
			           url: "/SIGA/GEN_Juzgados.do?modo=getAjaxTiposAsistencia",
			           data: "idGuardia="+idGuardia+"&idTurno="+idTurno,
			           dataType: "json",
			           success:  function(json) {
			        	    optionTipoAsistenciaColegio.length = 0;
							jQuery("#idTipoAsistenciaColegio").append("<option  value=''>"+txtSelect+"</option>");
							var tiposAsistenciaColegio = json.tiposAsistenciaColegio;
		         			jQuery.each(tiposAsistenciaColegio, function(i,tipoAsistenciaColegio){
		                        jQuery("#idTipoAsistenciaColegio").append("<option value='"+tipoAsistenciaColegio.idTipoAsistenciaColegio+"'>"+tipoAsistenciaColegio.descripcion+"</option>");    
		                    });
		         			document.getElementById('idTipoAsistenciaColegio').value="${VolantesExpressForm.idTipoAsistenciaColegio}";
		    				document.getElementById('idTipoAsistenciaColegio').onchange();
			           },
			           error: function(xml,msg){
			        	   alert("Error: "+msg+xml);
			           }
			   	});
			}else{
				optionTipoAsistenciaColegio.length = 0;
				
			}
			fin();
			if ("${VolantesExpressForm.idColegiado}" && "${VolantesExpressForm.idColegiado}" != "") {
				document.VolantesExpressForm.idColegiado.value = "${VolantesExpressForm.idColegiado}";
			}
		}
		
		function actualizarResultados(){
			if ((document.VolantesExpressForm.fechaGuardia && document.VolantesExpressForm.fechaGuardia.value != '')&&
				(document.VolantesExpressForm.idTurno && document.VolantesExpressForm.idTurno.value != ''&& document.VolantesExpressForm.idTurno.value != '-1')&&
			    (document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')&&
				(document.VolantesExpressForm.idTipoAsistenciaColegio && document.VolantesExpressForm.idTipoAsistenciaColegio.value != ''&& document.VolantesExpressForm.idTipoAsistenciaColegio.value != '-1')&&
				(document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '')){
				document.getElementById('idBuscarAsistencias').onclick();
			}else{
				table = document.getElementById('asistencias');
				if(table){			
					for(var i = table.rows.length - 1; i > 0; i--)
					{
						table.deleteRow(i);
					}
				}
				var divAsistencias = document.getElementById("divAsistencias");
				if(divAsistencias) 
					 divAsistencias.innerHTML="";
			}
		}
		
		function limpiarColegiado(){
			document.VolantesExpressForm.idColegiado.value = '';
			document.VolantesExpressForm.numeroColegiado.value = '';
			document.VolantesExpressForm.nombreColegiado.value = '';
			actualizarResultados();
		}
		
		function buscarColegiado(){
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				
				document.VolantesExpressForm.idColegiado.value       = resultado[0];
				document.VolantesExpressForm.numeroColegiado.value    = resultado[2];
				document.VolantesExpressForm.nombreColegiado.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
				postAccionColegiado();
			}
		}
	
		function refrescarLocal(){
		}
	
		function validarDatosMinimos () {
		
			if (!(document.VolantesExpressForm.fechaGuardia && document.VolantesExpressForm.fechaGuardia.value != '')) {
				var campo = '<siga:Idioma key="gratuita.volantesExpres.literal.GuardiaDia"/>';
	 				var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
	 				alert (msg);				
				return false;
			}
			if (!(document.VolantesExpressForm.fechaJustificacion && document.VolantesExpressForm.fechaJustificacion.value != '')) {
				var campo = '<siga:Idioma key="gratuita.volantesExpres.literal.fechaJustificacion"/>';
	 				var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
	 				alert (msg);				
				return false;
			}
			
			if (!(document.VolantesExpressForm.idTipoAsistenciaColegio && document.VolantesExpressForm.idTipoAsistenciaColegio.value != ''&& document.VolantesExpressForm.idTipoAsistenciaColegio.value != '-1')) {
				alert ('<siga:Idioma key="gratuita.volantesExpres.literal.tipoAsistenciaColegio"/>');
				return false;
			}
			if (!(document.VolantesExpressForm.idTurno && document.VolantesExpressForm.idTurno.value != ''&& document.VolantesExpressForm.idTurno.value != '-1')) {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarTurno"/>');
				return false;
			}
			if (!(document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')) {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarGuardia"/>');
				return false;
			}
			if (!((document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '')||
					(document.VolantesExpressForm.idColegiadoGuardia && document.VolantesExpressForm.idColegiadoGuardia.value!= ''&& document.VolantesExpressForm.idColegiadoGuardia.value!= '-1'))) {
				alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarColegiado"/>');
				return false;
			}
			
			
			return true;
		}
		
		function accionInsertarRegistroTabla () {
			var validado = validarDatosMinimos (); 
			if(!validado){
				return;
			}
			
			crearFila();
		} 
		
		function preAccionGuardarAsistencias(){
			var texto = '';
			if((document.VolantesExpressForm.idColegiadoSustituido && document.VolantesExpressForm.idColegiadoSustituido.value != ''&& document.VolantesExpressForm.idColegiadoSustituido.value != '-1')){
				texto = '<siga:Idioma key="gratuita.volantesExpres.msg.confirmaSustitucion"/>';
			}else if(document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '' && (!document.VolantesExpressForm.idColegiadoGuardia||document.VolantesExpressForm.idColegiadoGuardia==null ||document.VolantesExpressForm.idColegiadoGuardia.value=='-1')){
				texto = '<siga:Idioma key="gratuita.volantesExpres.msg.confirmaRefuerzo"/>';
			
			}else if(document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '' && (!document.VolantesExpressForm.idColegiadoGuardia||document.VolantesExpressForm.idColegiadoGuardia==null ||document.VolantesExpressForm.idColegiadoGuardia.value=='')){
				//Si queremos meter algun aviso cuando vaya a crear la cabecera de guardias		
			}
			
			if(texto!=''){
				if (!confirm(texto)){
					return 'cancel';
				}
			}
		
			sub();
			var validado = validarDatosMinimos ();
			if(!validado){
				fin();
				return 'cancel';
			}
			
			datosAsistencias = getDatos('asistencias');
			if(datosAsistencias=='cancel'){
				fin();
				return 'cancel';
			}
			
			document.VolantesExpressForm.datosAsistencias.value = datosAsistencias;
		}

		function postAccionGuardarAsistencias(){
			fin();
		}

		function preAccionBuscarAsistencias(){
			sub();
			document.getElementById("idTipoAsistenciaColegioSelected").value = document.VolantesExpressForm.idTipoAsistenciaColegio.value;
		}
		
		
		function postAccionBuscarAsistencias(){
			var idTipoAsistenciaColegioSelected = document.getElementById("idTipoAsistenciaColegioSelected").value;
			jQuery("#idTipoAsistenciaColegio option[value='" + idTipoAsistenciaColegioSelected + "']").attr("selected", "selected");
			fin();
		}
		
		function getDatos(idTabla) {	
			table = document.getElementById(idTabla);
			filas = table.rows.length;
			// Datos Dinamicos Asistencias
			var datos = "", accion = "";
				
			for (a = 0; a < filas ; a++) {
				i = table.rows[a].id.split("_")[1];
				 
				var validado = validarDatosFila (i);
							  
				if (!validado) {
					fin();
					return 'cancel';
				}
		              
				claveAnio = document.getElementById("claveAnio_" + i).value;
				if(claveAnio=='-1')
					claveAnio ="";
				
				datos += 'claveAnio='+claveAnio;
				datos += ',';
				claveNumero = document.getElementById("claveNumero_" + i).value;
				if(claveNumero=='-1')
					claveNumero ="";
				datos += 'claveNumero='+claveNumero;
				datos += ',';
				
				claveIdInstitucion = document.getElementById("claveIdInstitucion_" + i).value;
				if(claveIdInstitucion=='-1')
					claveIdInstitucion ="";
				datos += 'claveIdInstitucion='+claveIdInstitucion;
				datos += ',';
				
				
				hora = document.getElementById("hora_" + i).value;
				datos += 'hora='+hora;
				datos += ',';
				
				minuto = document.getElementById("minuto_" + i).value;
				datos += 'minuto='+minuto;
				datos += ',';
				
				if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") {
			    	lugar = document.getElementById("comisaria_"+ i).value;
					datos += 'comisaria='+lugar;
					datos += ',';
					datos += 'juzgado=';
					datos += ',';
				}else{
					lugar = document.getElementById("juzgado_"+ i).value;
					datos += 'comisaria=';
					datos += ',';
					datos += 'juzgado='+lugar;
					datos += ',';
				}
				
				
				idPersona = document.getElementById("idPersona_" + i).value;
				datos += 'idPersona='+idPersona;
				datos += ',';
				
				dni = document.getElementById("dni_" + i).value;
				dni = replaceAll(dni,',','~');
	               dni = replaceAll(dni,'=','¬');
				datos += 'dni='+dni;
				datos += ',';
				
				nombre = document.getElementById("nombre_" + i).value;
				nombre = replaceAll(nombre,',','~');
	               nombre = replaceAll(nombre,'=','¬');
				datos += 'nombre='+nombre;
				datos += ',';
				
				apellido1 = document.getElementById("apellido1_" + i).value;
				apellido1 = replaceAll(apellido1,',','~');
	               apellido1 = replaceAll(apellido1,'=','¬');
				datos += 'apellido1='+apellido1;
				datos += ',';
				
				apellido2 = document.getElementById("apellido2_" + i).value;
				apellido2 = replaceAll(apellido2,',','~');
	               apellido2 = replaceAll(apellido2,'=','¬');
				datos += 'apellido2='+apellido2;
				datos += ',';
				
				if (!jQuery("#comboSexo_"+i).is(':disabled') && document.getElementById("tipoPcajg").value=="9"){
					sexo =jQuery("#comboSexo_"+i).val();
					datos += 'sexo='+sexo;
					datos += ',';
				}else{
					datos += 'sexo=';
					datos += ',';
				}
				
				
	
	             diligencia = document.getElementById("diligencia_" + i).value;
	             diligencia = replaceAll(diligencia,',','~');
	             diligencia = replaceAll(diligencia,'=','¬');
	             
	             datos += 'diligencia='+diligencia;
	             datos += ',';
				observaciones = '';
				if(document.getElementById("observaciones_" + i)){		
					observaciones = document.getElementById("observaciones_" + i).value;
					observaciones = replaceAll(observaciones,',','~');
					observaciones = replaceAll(observaciones,'=','¬');
				}
				datos += 'observaciones='+observaciones;
				datos += ',';
	
				delitosImputados = '';
				if (document.getElementById("delitosImputados_" + i))
					delitosImputados = document.getElementById("delitosImputados_" + i).value;
				datos += 'delitosImputados='+delitosImputados;
				datos += ',';
				
				idDelito = document.getElementById("idDelito_" + i).value;
				if(idDelito=='-1')
					idDelito ="";
				datos += 'idDelito='+idDelito;
				datos += ',';
				
				
				ejgNumero = document.getElementById("ejgNumero_" + i).value;
				if(ejgNumero=='-1')
					ejgNumero ="";
				datos += 'ejgNumero='+ejgNumero;
				datos += ',';
				
				ejgAnio = document.getElementById("ejgAnio_" + i).value;
				if(ejgAnio=='-1')
					ejgAnio ="";
				datos += 'ejgAnio='+ejgAnio;
				datos += ',';
				
				ejgTipo = document.getElementById("ejgTipo_" + i).value;
				if(ejgTipo=='-1')
					ejgTipo ="";
				datos += 'ejgTipo='+ejgTipo;
				datos += "%%%";
				
				
			}
			return datos;
		}
	
		function crearFila () { 
			table = document.getElementById("asistencias");
			if(table.rows.length>0){
				numFila = table.rows.length;
				tr = table.insertRow(numFila);
				tr.id = "fila_" + numFila;
				
				if(numFila%2==0){
					
					tr.className  = "listaNonEdit filaTablaImpar";
				}else{
					tr.className  = "listaNonEdit filaTablaPar";
				}
				tr.style.textAlign = 'center';
				td = tr.insertCell(0);
				td.setAttribute("align", "center");
				td.innerHTML = '<input type="hidden" id="claveAnio_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="claveNumero_' + numFila + '" value="">' + 
			                  '<input type="hidden" id="claveIdInstitucion_' + numFila + '" value="">' +
			                  '<input type="hidden" id="designaNumero_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejgNumero_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejgAnio_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="ejgTipo_' + numFila + '" value=""> ' +
			                  '<input type="hidden" id="delitosImputados_' + numFila + '" value="">' + 
							  '<input type="text" id="hora_'   + numFila + '" class="box" style="width:20px;margin-top:4px;text-align:center;" maxLength="2" value="" onBlur="validaHora(this);" />&nbsp;' + 
			 				  '<input type="text" id="minuto_' + numFila + '" class="box" style="width:20px;margin-top:4px;text-align:center;" maxLength="2" value="" onBlur="validaMinuto(this);" />';
			
				// centro detencion	/ Juzgado
				td = tr.insertCell(1); 
				td.className = "";
				//Centro detencion
				if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") {
					aux = '<input type="text" id="codComisaria_' + numFila + '" class="box" size="8"  style="width:15%;margin-top:4px;" maxlength="10" onBlur="obtenerComisaria(' + numFila + ');" />&nbsp;'+ 			
						'<select class="box" id="comisaria_' + numFila + '" style="width:75%;" name="comisaria_' + numFila + '" onchange="cambiarComisaria(' + numFila + ');"></select>';
				}
				// Juzgado
				else {
					aux = '<input type="text" id="codJuzgado_' + numFila + '" class="box" size="8" style="width:15%;margin-top:4px;" maxlength="10" onBlur="obtenerJuzgado(' + numFila + ');"/>&nbsp;' +
						'<select class="box" id="juzgado_' + numFila + '" style="width:75%;margin-top:4px;" name="juzgado_' + numFila + '" onchange="cambiarJuzgado(' + numFila + ');"></select>';
				}
				td.innerHTML = aux;
				
				// asistido (dni - nombre apellido1 apellido2)
				td = tr.insertCell(2); 
				td.className = "";
				var filaDinamica= '<input type="text" id="dni_' + numFila + '" class="box" style="width:18%;margin-top:4px;" value="" maxlength="20" onBlur="obtenerPersona(' + numFila + ');" />'+
								'&nbsp;&nbsp;-&nbsp;&nbsp' +
								'<input type="text" id="nombre_' + numFila + '" class="box" style="width:18%;margin-top:4px;" value="" maxlength="80"/>&nbsp;' + 
								'<input type="text" id="apellido1_' + numFila + '" class="box" style="width:18%;margin-top:4px;" value="" maxlength="80"/>&nbsp;' +
								'<input type="text" id="apellido2_' + numFila + '" class="box" style="width:18%;margin-top:4px;" value="" maxlength="80"/>&nbsp;';
				if (document.VolantesExpressForm.tipoPcajg.value =="9"){
					filaDinamica = filaDinamica + 
								'<select id="comboSexo_' + numFila + '" styleClass="box" style="width:8%;margin-top:4px;" name="comboSexo_'+numFila+'">' +
									'<option value="" selected ="selected">--Sexo</option>'+
									'<option value="H"><siga:Idioma key="censo.sexo.hombre"/></option>'+
									'<option value="M"><siga:Idioma key="censo.sexo.mujer"/></option>'+
									'<option value="N"><siga:Idioma key="censo.sexo.nc"/></option>'+
								'</select>';
	            }
				filaDinamica = filaDinamica + 
								'<img id="info_existe_' + numFila + '" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>'+
                				'<input type="hidden" id="idPersona_' + numFila + '" class="box" style="width:4%;margin-top:4px;" value=""/>';		                
				td.innerHTML = filaDinamica;
				        
				// numero diligencia (num diligencia / anio)
				td = tr.insertCell(3); 
				td.className = "";
				td.innerHTML ='<input type="text" id="diligencia_' + numFila + '" class="box" maxlength="20" style="width:90%;margin-top:4px;" value=""/> ';
		
				// Delitos
				td = tr.insertCell(4); 
				td.className = "";
				aux = '';
				if(document.VolantesExpressForm.delito && document.VolantesExpressForm.delito.value=='true'){
					aux = '<select class="boxCombo" id="idDelito_' + numFila + '" style="width:90%;margin-top:4px;" name="idDelito_' + numFila + '" />';
				}else{
					aux = '<input type="text" id="observaciones_' + numFila + '" class="box" style="width:90%;margin-top:4px;" value=""/>'+
						'<input type="hidden" id="idDelito_' + numFila + '" value="">';
				}
				td.innerHTML = aux;
				
				// boton borrar
				td = tr.insertCell(5); 
				td.setAttribute("align", "left");
				td.className = "";			
				concatenado = '<table><tr><td style="border: none"><img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" onclick="borrarFila(\''+ tr.id +'\')"></td></tr></table>';
				td.innerHTML = concatenado;
				tr.scrollIntoView(false);
				
				if (document.VolantesExpressForm.lugar[0].checked && 
				    document.VolantesExpressForm.lugar[0].value == "centro") {
					var selectPrimeraComisaria = document.getElementById("comisaria_0"); 
					var auxComisaria = 'comisaria_'+numFila;
				    var comisariaActual = document.getElementById(auxComisaria);
			
				    for (i = 0 ; i<selectPrimeraComisaria.options.length ; i++) {
		   				var option = selectPrimeraComisaria.options[i];
		   				var optionNueva = document.createElement('option');
		   				optionNueva.text = option.text;
		            	optionNueva.value = option.value;
		     			comisariaActual.add(optionNueva);
		    		}
	    		}else{
	    		
		    		var selectPrimerJuzgado = document.getElementById("juzgado_0"); 
					var auxJuzgado = 'juzgado_'+numFila;
				    var juzgadoActual = document.getElementById(auxJuzgado);
			
				    for (i = 0 ; i<selectPrimerJuzgado.options.length ; i++) {
		   				var option = selectPrimerJuzgado.options[i];
		   				var optionNueva = document.createElement('option');
		   				optionNueva.text = option.text;
		            	optionNueva.value = option.value;
		     			juzgadoActual.add(optionNueva);
		    		}
	    		}
	    		if(document.VolantesExpressForm.delito && document.VolantesExpressForm.delito.value=='true'){
		    		var selectPrimerDelito = document.getElementById("idDelito_0"); 
					var auxDelito = 'idDelito_'+numFila;
				    var delitoActual = document.getElementById(auxDelito);
			
				    for (i = 0 ; i<selectPrimerDelito.options.length ; i++) {
		   				var option = selectPrimerDelito.options[i];
		   				var optionNueva = document.createElement('option');
		   				optionNueva.text = option.text;
		            	optionNueva.value = option.value;
		     			delitoActual.add(optionNueva);
		    		}
	    		}
				validarAnchoTabla();
				
			}else{
				document.getElementById('idPrimeraAsistencia').onclick();
				
			}
			
		}
			
		function validarAnchoTabla () {
			if (document.getElementById("asistencias").clientHeight < document.getElementById("divAsistencias").clientHeight) {
				document.getElementById("tabAsistenciasCabeceras").width='100%';
			} else {
				document.getElementById("tabAsistenciasCabeceras").width='98.30%';
			}
		}

		function validarDatosFila (fila) {
			var campo = "";
			var obligatorio = "<siga:Idioma key='messages.campoObligatorio.error'/>";
			
			var isValidado = true;
			
			if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") {
			
			    if (document.getElementById("comisaria_"+fila).value=='-1' || document.getElementById("comisaria_"+fila).value=='') {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.centroDetencion'/>" ;
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			    //alert("diligencia"+(document.getElementById("diligencia_"+fila).value));
			    if (document.getElementById("tipoPcajg").value=="2" && (document.getElementById("diligencia_"+fila).value=='-1' || document.getElementById("diligencia_"+fila).value=='')) {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.numeroDiligencia'/>" ;
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			}else{
			
				if (document.getElementById("juzgado_"+fila).value=='-1' ||document.getElementById("juzgado_"+fila).value=='') {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.juzgado'/>";
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			    if (document.getElementById("tipoPcajg").value=="2" && (document.getElementById("diligencia_"+fila).value=='-1' || document.getElementById("diligencia_"+fila).value=='')) {
					campo = "<siga:Idioma key='gratuita.volantesExpres.procedimiento'/>" ;
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			    valueNumProcedimiento = document.getElementById("diligencia_"+fila).value;
				objectConsejo = document.getElementById("idConsejo");
				
				if((objectConsejo && objectConsejo.value ==IDINSTITUCION_CONSEJO_ANDALUZ)){
					var objectAnioProcedimiento =  new Object();
					error ='';
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
						error = validarFormatosNigNumProc('',valueNumProcedimiento,objectAnioProcedimiento,'0',objectConsejo);
					}
					if(error!=''){
						fin();
						alert(error);
						return false;
						
					}
					formateaNumProcedimiento(fila,valueNumProcedimiento,objectAnioProcedimiento.value,objectConsejo);
				}
			}
			if (!document.getElementById("nombre_" + fila).value) {
				campo = "<siga:Idioma key='gratuita.volantesExpres.literal.asistido'/>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}
			if (!document.getElementById("apellido1_" + fila).value) {
				campo = "<siga:Idioma key='gratuita.volantesExpres.literal.apellido1'/>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}
		
			if(document.getElementById("tipoPcajg").value=="2" && document.VolantesExpressForm.delito && document.VolantesExpressForm.delito.value=='true'){
				if (document.getElementById("idDelito_" + fila).value=='-1' || document.getElementById("idDelito_" + fila).value=='') {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.delitos'/>";
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			 }
			
			if(document.getElementById("tipoPcajg").value=="9" && jQuery("#comboSexo_"+fila).val() == ""){
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.sexo'/>";
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
	
			return isValidado;
		}
		
		function formateaNumProcedimiento(fila,valueNumProcedimiento,valueAnioProcedimiento,objectConsejo){
			if(objectConsejo && objectConsejo.value==IDINSTITUCION_CONSEJO_ANDALUZ){
				var numProcedimientoArray = valueNumProcedimiento.split('.');
				numProcedimiento = numProcedimientoArray[0];
				if(numProcedimiento && numProcedimiento!=''){
					numProcedimiento = pad(numProcedimiento,5,false);
					finNumProcedimiento = numProcedimientoArray[1]; 
					if(finNumProcedimiento){
						numProcedimiento = numProcedimiento+"."+pad(finNumProcedimiento,2,false);
					}
					document.getElementById("diligencia_"+fila).value = numProcedimiento+"/"+valueAnioProcedimiento;
				}
			}
		}
		
		function cambiarComisaria(fila) {
			var combo = document.getElementById("comisaria_"+fila).value;
			
			if(combo!="-1"){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
	  				type: "POST",
					url: "/SIGA/GEN_Comisarias.do?modo=getAjaxComisaria2",
					data: "idCombo="+combo,
					dataType: "json",
					success: function(json){		
		    	   		document.getElementById("codComisaria_"+fila).value = json.codigoExt;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById("codComisaria_"+fila).value = "";
		}		
		
		function obtenerComisaria(fila) { 
			var txtCodigoComisaria="codComisaria_"+fila;
			var txtComboComisaria="comisaria_"+fila;		
			var codigo = document.getElementById(txtCodigoComisaria).value;
			
			if(codigo!=""){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
	  				type: "POST",
					url: "/SIGA/GEN_Comisarias.do?modo=getAjaxComisaria3",
					data: "codigo="+codigo,
					dataType: "json",
					success: function(json){		
						if (json.idComisaria=="") {
							document.getElementById(txtComboComisaria).value = "-1";
							document.getElementById(txtCodigoComisaria).value = "";
						} else {
		    	   			document.getElementById(txtComboComisaria).value = json.idComisaria;    
							if (document.getElementById(txtComboComisaria).value=="") {
								document.getElementById(txtComboComisaria).value = "-1";
								document.getElementById(txtCodigoComisaria).value = "";
		    	   			}
						}
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById(txtComboComisaria).value = "-1";
		}	
		
		function cambiarJuzgado(fila) {
			var combo = document.getElementById("juzgado_"+fila).value;
			
			if(combo!="-1"){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
	  					type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado3",
					data: "idCombo="+combo,
					dataType: "json",
					success: function(json){		
		    	   		document.getElementById("codJuzgado_"+fila).value = json.codigoExt2;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById("codJuzgado_"+fila).value = "";
		}		
		
		function obtenerJuzgado(fila) { 
			var txtCodigoJuzgado="codJuzgado_"+fila;
			var txtComboJuzgado="juzgado_"+fila;
			var codigo = document.getElementById(txtCodigoJuzgado).value;
			
			if(codigo!=""){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
	  					type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado4",
					data: "codigo="+codigo,
					dataType: "json",
					success: function(json){		
						if (json.idJuzgado=="") {
							document.getElementById(txtComboJuzgado).value = "-1";
							document.getElementById(txtCodigoJuzgado).value = "";
						} else {
		    	   			document.getElementById(txtComboJuzgado).value = json.idJuzgado;
		    	   			if (document.getElementById(txtComboJuzgado).value=="") {
								document.getElementById(txtComboJuzgado).value = "-1";
								document.getElementById(txtCodigoJuzgado).value = "";
		    	   			}
						}
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById(txtComboJuzgado).value = "-1";
		}				
	</script>

</head>

<body onload="init();">
<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean" />
<input type="hidden" id ="idConsejo" value = "${usrBean.idConsejo}"/>
<bean:define id="fechaJustificacion" name="VolantesExpressForm" property="fechaJustificacion" type="String" />
<!-- INICIO: CAMPOS DE BUSQUEDA-->
<input type="hidden" id = "idTipoAsistenciaColegioSelected" value = ""/>
<html:form action="/JGR_VolantesExpres" method="POST"
	target="mainWorkArea">
	<html:hidden property="tipoPcajg" styleId="tipoPcajg" />
	<html:hidden property="modo" styleId="modo" value=""/>
	<html:hidden property="idColegiado" styleId="idColegiado" />
	<html:hidden property="idInstitucion" styleId="idInstitucion" />
	<html:hidden property="datosAsistencias" styleId="datosAsistencias" />
	<html:hidden property="idTipoAsistencia" styleId="idTipoAsistencia" />
	<html:hidden property="delito" styleId="delito" />
	<html:hidden property="msgError" styleId="msgError" value=""/>
	<html:hidden property="msgAviso" styleId="msgAviso" value=""/>
	<html:hidden property="centroOjuzgado" styleId="centroOjuzgado"/>
	<input type="hidden" name="buscaColegiadoManual"  id="buscaColegiadoManual" value="false"/>

	<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.cabeceraVolante">
		<table width="100%" border="0">			
			<tr>
				<td width="17%"></td>
				<td width="11%"></td>
				<td width="8%"></td>
				<td width="27%"></td>
				<td width="10%"></td>
				<td width="27%"></td>
			</tr>
			
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.volantesExpres.literal.GuardiaDia" />&nbsp;(*)
				</td>
				<td>
					<siga:Fecha styleId="fechaGuardia" nombreCampo="fechaGuardia" postFunction="accionCalendario();"/>
				</td>

				<td class="labelText">
					<siga:Idioma key="gratuita.volantesExpres.literal.turno" />&nbsp;(*)
				</td>
				<td>
					<html:select styleId="turnos" styleClass="boxCombo" style="width:100%;" property="idTurno">
						<bean:define id="turnos" name="VolantesExpressForm" property="turnos" type="java.util.Collection" />
						<html:optionsCollection name="turnos" value="idTurno" label="nombre" />
					</html:select>
				</td>
				
				<td class="labelText">
					<siga:Idioma key="gratuita.volantesExpres.literal.guardia" />&nbsp;(*)
				</td>
				<td>
					<html:select styleId="guardias" styleClass="boxCombo" style="width:100%;" property="idGuardia" >
						<bean:define id="guardias" name="VolantesExpressForm" property="guardias" type="java.util.Collection" />
						<html:optionsCollection name="guardias" value="idGuardia" label="nombre" />
					</html:select>
				</td>
			</tr>

			<tr>
				<td class="labelText" nowrap>
					<siga:Idioma key="gratuita.volantesExpres.literal.tipoAsistenciaColegio" />&nbsp;(*)
				</td>
				<td colspan="3">
					<select id="idTipoAsistenciaColegio"  name="idTipoAsistenciaColegio" style="width:100%;" class="boxCombo" onchange="actualizarResultados();">
						<option  value="-1">--<siga:Idioma key="general.boton.seleccionar"/></option>
					</select>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.volantesExpres.literal.Lugar" />&nbsp;(*)
				</td>
				<td class="boxConsulta">
					<label><input type="radio" style="margin-top:4px;" id="lugar_centro" name="lugar" property="lugar" value="centro" 
						onclick="cambiarDiligenciaProcedimiento();cambiarCentroDetencionJuzgado();actualizarResultados();">
					<siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion"/></label>
		
					<label><input type="radio" style="margin-top:4px;" id="lugar_juzgado" name="lugar" property="lugar" value="juzgado" 
						onclick="cambiarDiligenciaProcedimiento();cambiarCentroDetencionJuzgado();actualizarResultados();">
					<siga:Idioma key="gratuita.volantesExpres.literal.juzgado"/></label>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.letradosGuardia">
		<table width="100%" border="0">
			<tr>
				<td class="labelText" width="17%">
					<siga:Idioma key="gratuita.volantesExpres.literal.colegiado" />&nbsp;
				</td>
				<td colspan="3" width="46%">
					<html:select styleId="colegiadosGuardia" styleClass="boxCombo" style="width:100%;margin-top:-2px;" property="idColegiadoGuardia">
						<bean:define id="colegiadosGuardia" name="VolantesExpressForm" property="colegiadosGuardia" type="java.util.Collection" />
						<html:optionsCollection name="colegiadosGuardia" value="idPersona" label="nombre" />
					</html:select>
				</td>
				<td colspan="2" width="37%">
					&nbsp;
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.letrado">
		<table width="100%" border="0">
			<tr>
				<td class="labelText" width="17%">
					<siga:Idioma key="gratuita.volantesExpres.literal.colegiado" />&nbsp;(*)
				</td>
				<td colspan="2" width="30%">
					<html:text styleId="numeroColegiado" property="numeroColegiado" style="width:15%;" maxlength="9" styleClass="box" />
					<html:text styleId="nombreColegiado" property="nombreColegiado" style="width:70%;" maxlength="50" styleClass="box" readonly="true" />
				</td>
				<td width="16%">
					<input type="button" style="margin-top:-2px;" class="button" id="idButton" name="Buscar" value="<siga:Idioma key="general.boton.search" />" onClick="buscarColegiado();">
					<input type="button" style="margin-top:-2px;" class="button" id="idButton" name="Limpiar" value="<siga:Idioma key="general.boton.clear" />" onClick="limpiarColegiado();">
				</td>

				<td class="labelText" width="10%">
					<siga:Idioma key="gratuita.volantesExpres.literal.sustitudoDe" />
				</td>
				<td width="27%">
					<html:select styleId="colegiadosSustituidos" styleClass="boxCombo" style="width:85%;" property="idColegiadoSustituido">
						<bean:define id="colegiadosSustituidos" name="VolantesExpressForm" property="colegiadosSustituidos" type="java.util.Collection" />
						<html:optionsCollection name="colegiadosSustituidos" value="idPersona" label="nombre" />
					</html:select>
					<img src="/SIGA/html/imagenes/botonAyuda.gif"
						style="cursor: hand;"
						alt="<siga:Idioma key="gratuita.volantesExpres.ayudaSustitutos"/>"
						name="" border="0" onclick="mostrarAyuda();"/>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.registroVolante">
		<table width="100%" border="0">			
			<tr>
				<td colspan="5" class="labelText" width="73%">
					<siga:Idioma key="gratuita.volantesExpres.mensaje.indicaFormaIdentificacion" />&nbsp;
				</td>
				<td class="labelText" width="13%">
					<siga:Idioma key="gratuita.volantesExpres.literal.fechaJustificacion" />
				</td>
				<td width="14%">
					<siga:Fecha nombreCampo="fechaJustificacion" valorInicial="${VolantesExpressForm.fechaJustificacion}" />
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<ajax:updateSelectFromField
		baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxTurnos"
		source="fechaGuardia" target="turnos"
		parameters="fechaGuardia={fechaGuardia},idColegiado={idColegiado}"
		postFunction="postAccionFechaGuardia"
		/>
		
	<ajax:select
		baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxGuardias"
		source="turnos" target="guardias" parameters="fechaGuardia={fechaGuardia},idTurno={idTurno},idColegiado={idColegiado}"
		preFunction="preAccionTurno"
		postFunction="postAccionTurno" />
	
	<ajax:updateMultipleSelectFromSelect
		baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxColegiados"
		source="guardias" target="colegiadosGuardia,colegiadosSustituidos"
		parameters="fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia}" 
		postFunction="postAccionGuardia"/>

	<ajax:updateFieldFromField 
		baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxColegiado"
	    source="numeroColegiado" target="idColegiado,numeroColegiado,nombreColegiado"
		parameters="numeroColegiado={numeroColegiado}" preFunction="preAccionColegiado" postFunction="postAccionColegiado"  />
		
	<ajax:updateFieldFromSelect  
		baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxColegiadoGuardia"
	    source="colegiadosGuardia" target="idColegiado,numeroColegiado,nombreColegiado"
		parameters="idColegiadoGuardia={idColegiadoGuardia}" preFunction="preAccionColegiadoGuardia" postFunction="postAccionColegiadoGuardia"/>

		<table id='tabAsistenciasCabeceras'	name ='tabAsistenciasCabeceras' class='fixedHeaderTable dataScroll'  width="100%" style='table-layout: fixed;border:1;'>

			<thead class='Cabeceras' style='text-align: center;border:0;' >
				<tr class='tableTitle'>
					<th style='text-align: center; width: 6%;'>
						<siga:Idioma key="gratuita.volantesExpres.literal.hora" />
					</th>
					<th id='centroDetencionJuzgado' style='text-align: center; width: 19%;'>
						<siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion" />
					</th>
					<th style='text-align: center; width: 40%;'>
						<siga:Idioma key="gratuita.volantesExpres.literal.asistido" />
					</th>
					<th id='diligenciaProcedimiento' style='text-align: center; width: 8%;'>
						<siga:Idioma key="gratuita.volantesExpres.literal.numeroDiligencia" />
					</th>
					<th style='text-align: center; width: 12%;'>
					<c:if test="${VolantesExpressForm.delito==true}">
						<siga:Idioma key="gratuita.volantesExpres.literal.delitos" />
					</c:if>
					<c:if test="${VolantesExpressForm.delito==false}">
						<siga:Idioma key="gratuita.volantesExpres.literal.observaciones" />
					</c:if>
					</th>
					<th style='text-align: center; width: 15%;'>
						<input type='button' id='idBuscarAsistencias' name='idButton' style="display: none" 
							value='Buscar' alt='Buscar' class='busquedaAsistencias'>
						<input type='button' id='idInsertarAsistencia' class="button" name='idButton' 
							value='<siga:Idioma key="general.boton.insertar"/>' alt='<siga:Idioma key="general.boton.insertar"/>'
							onclick="accionInsertarRegistroTabla();">
					</th>
				</tr>
			</thead>
			
			<tbody>
			</tbody>

		</table>
		
				
		<div id="divAsistencias" style='height:300px;position:absolute;width:100%; overflow-y:auto'>
			<table id='asistencias' class="fixedHeaderTable dataScroll" id='asistencias' style='table-layout:fixed'>
			</table>
		</div>	


	<!-- INICIO: BOTONES BUSQUEDA -->
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td style="width: 900px;">&nbsp;</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.new"/>' name='idButton' id="idButton" onclick="return accionNuevo();" class="button" value='<siga:Idioma key="general.boton.new"/>'>
				<input type='button'  id = 'idPrimeraAsistencia' name='idButton' style="display:none" class='primeraAsistencia'>
			</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.guardar"/>' name='idButton' id="idGuardarAsistencias"  class="button" value='<siga:Idioma key="general.boton.guardar"/>'>
			</td>
		</tr>
	</table>	
</html:form>

<ajax:htmlContent
	baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxBusquedaAsistencias"
	source="idBuscarAsistencias"
	target="divAsistencias"
	preFunction="preAccionBuscarAsistencias"
	postFunction="postAccionBuscarAsistencias"
	parameters="fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia},idColegiado={idColegiado},idTipoAsistenciaColegio={idTipoAsistenciaColegio},lugar={lugar},idColegiadoSustituido={idColegiadoSustituido}"/>

<ajax:htmlContent
	baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxPrimeraAsistencia"
	source="idPrimeraAsistencia"
	target="divAsistencias"
	preFunction="preAccionBuscarAsistencias"
	postFunction="postAccionBuscarAsistencias"
	parameters="lugar={lugar}"/>


<ajax:htmlContent
	baseUrl="/SIGA/JGR_VolantesExpres.do?modo=getAjaxGuardarAsistencias"
	source="idGuardarAsistencias"
	target="divAsistencias"
	preFunction="preAccionGuardarAsistencias"
	postFunction="postAccionGuardarAsistencias"
	parameters="datosAsistencias={datosAsistencias},fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia},idColegiado={idColegiado},idColegiadoGuardia={idColegiadoGuardia},idTipoAsistenciaColegio={idTipoAsistenciaColegio},lugar={lugar},idColegiadoSustituido={idColegiadoSustituido},fechaJustificacion={fechaJustificacion}"/>

	<!-- FIN: CAMPOS DE BUSQUEDA-->
	<!-- Formularios auxiliares -->
	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>

	<html:form action="/JGR_AsistidoAsistencia" method="POST" target="submitArea" type="" style="display:none">
		<input type="hidden" name="modo" value="buscarNIF">
		<input type="hidden" name="conceptoE" value="PersonaJG">
		<input type="hidden" name="NIdentificacion" value="">
		<input type="hidden" name="nombreObjetoDestino" value="">
	</html:form>

	<html:form action="/JGR_MantenimientoJuzgados" method="POST"
		target="submitArea">
		<input type="hidden" name="modo" value="buscarJuzgado">
		<input type="hidden" name="codigoExt2" value="">
		<input type="hidden" name="nombreObjetoDestino" value="">
	</html:form>

	<html:form action="/JGR_MantenimientoComisarias" method="POST"
		target="submitArea">
		<input type="hidden" name="modo" value="buscarComisaria">
		<input type="hidden" name="codigoExtBusqueda" value="">
		<input type="hidden" name="nombreObjetoDestino" value="">
	</html:form>

	<html:form action="/JGR_ValidarVolantesGuardias" method="POST"
		target="resultado">
		<input type="hidden" name="modo" value="">
		<input type="hidden" name="datosValidar" value="">
		<input type="hidden" name="datosBorrar" value="">
		<input type="hidden" name="desde" value="">
	</html:form>
	<html:form action="/JGR_EJG" method="post" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="" />
		<input type="hidden" name="numEJG" />
		<input type="hidden" name="idTipoEJG" />
		<input type="hidden" name="anio" />
		<input type="hidden" name="numero" />
		<input type="hidden" name="fechaApertura" />
		<input type="hidden" name="idInstitucion"
			value="${VolantesExpressForm.idInstitucion}" />
		<input type="hidden" name="origen" value="A" />
		<input type="hidden" name="modo" />
		<input type="hidden" name="asistenciaAnio" />
		<input type="hidden" name="asistenciaNumero" />
	</html:form>

	<html:form action="/JGR_ActuacionesAsistencia" method="post"
		target="submitArea" style="display:none">
		<input type="hidden" name="modo" value="nuevo" />
		<html:hidden name="ActuacionAsistenciaForm" property="anio" />
		<html:hidden name="ActuacionAsistenciaForm" property="numero" />
		<html:hidden name="ActuacionAsistenciaForm" property="fichaColegial" />
		<html:hidden name="ActuacionAsistenciaForm" property="modoPestanha" />
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
		<html:hidden name="ActuacionAsistenciaForm" property="idInstitucion" />
	</html:form>
	<!-- FIN: BOTONES BUSQUEDA -->

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>

<script>
	function setAnchoAsistencias(){
		tablaAsistencias = document.getElementById("asistencias");
		var posTablaAsistencias = findPosY(tablaAsistencias);
		tablaBotonera = document.getElementById("tablaBotonesDetalle");
		var posTablaBotonera = findPosY(tablaBotonera);
		document.getElementById("divAsistencias").style.height=posTablaBotonera-posTablaAsistencias;
	}
	
	function accionNuevo(){
		jQuery("#fechaGuardia").attr("disabled","");
		document.VolantesExpressForm.modo.value = "";
		document.VolantesExpressForm.submit();
	}
			
	function obtenerPersona (fila) {
		o = document.getElementById ("dni_" + fila);
		o.value = o.value.toUpperCase ();

		if (o.value != "") {
			document.PersonaJGForm.NIdentificacion.value = o.value;
			document.PersonaJGForm.nombreObjetoDestino.value = fila;
			document.PersonaJGForm.submit ();
		}				
	} //obtenerPersona ()
		
	// Para la busqueda por dni
	function traspasoDatos (resultado, bNuevo, fila) {
		// Porque todas las funciones se llaman igual, las separamos
		if (!(bNuevo && fila)) 
			return traspasoDatosJuzgado (resultado);
		
		if (resultado && resultado.length > 7 && resultado[3] != "" && (bNuevo == 1)) { // Existe
			document.getElementById("dni_" + fila).value = resultado[2];
			document.getElementById("nombre_" + fila).value = resultado[3];
			document.getElementById("apellido1_" + fila).value = resultado[4];
			document.getElementById("apellido2_" + fila).value = resultado[5];
			if(resultado[19] != null && resultado[19] != "" && document.getElementById("comboSexo_" + fila)){
				document.getElementById("comboSexo_" + fila).value = resultado[19];
				jQuery("#comboSexo_" + fila).prop("disabled", true);
			}
			
			document.getElementById("idPersona_" + fila).value = resultado[1];
			ponerIconoIdentPersona (fila, true);
		}
		else {	// Nuevo
			document.getElementById("idPersona_" + fila).value = "nuevo";
			document.getElementById("nombre_" + fila).value = "";
			document.getElementById("apellido1_" + fila).value = "";
			document.getElementById("apellido2_" + fila).value = "";
			jQuery("#comboSexo_"+fila+ " option:eq('')").prop('selected', true)
			jQuery("#comboSexo_" + fila).prop("disabled", false);
			ponerIconoIdentPersona (fila, false);
		}
	} //traspasoDatos ()
		
	function ponerIconoIdentPersona (fila, encontrado) {
		if (encontrado) {
			document.getElementById("info_existe_" + fila).src = "/SIGA/html/imagenes/encontrado.gif";
			document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esYaExistentePersonaJG'/>";
			jQuery("#nombre_" + fila).attr("disabled","disabled");
			jQuery("#apellido1_" + fila).attr("disabled","disabled");
			jQuery("#apellido2_" + fila).attr("disabled","disabled");
		}
		else {
			document.getElementById("info_existe_" + fila).src = "/SIGA/html/imagenes/nuevo.gif";
			document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esNuevaPersonaJG'/>";
			jQuery("#nombre_" + fila).removeAttr("disabled");
			jQuery("#apellido1_" + fila).removeAttr("disabled");
			jQuery("#apellido2_" + fila).removeAttr("disabled");
			
		}
	} //ponerIconoIdentPersona ()
	
	function accionCrearEJG(anioAsistencia,numeroAsistencia,idInstitucion,fila) {	
		document.DefinirEJGForm.modo.value = "nuevo";
		document.DefinirEJGForm.asistenciaAnio.value = anioAsistencia;
		document.DefinirEJGForm.asistenciaNumero.value = numeroAsistencia;
		document.DefinirEJGForm.origen.value = "A";
		document.DefinirEJGForm.idInstitucion.value = idInstitucion;
		var resultado=ventaModalGeneral(document.DefinirEJGForm.name,"M");
		if (resultado){
			if(resultado=="MODIFICADO"){
				actualizarResultados();
			}
	   }	
	}
	
	function accionNuevaActuacion(anioAsistencia,numeroAsistencia,idInstitucion) {	
		document.ActuacionAsistenciaForm.modo.value = "nuevoDesdeVolanteExpress";
		document.ActuacionAsistenciaForm.anio.value = anioAsistencia;
		document.ActuacionAsistenciaForm.numero.value = numeroAsistencia;
		document.ActuacionAsistenciaForm.idInstitucion.value = idInstitucion;
		document.ActuacionAsistenciaForm.target = "mainWorkArea";
		document.ActuacionAsistenciaForm.submit();
	}
	
	function accionConsultaAsistencia(anioAsistencia,numeroAsistencia,idInstitucion) {	
		document.ActuacionAsistenciaForm.modo.value = "consultarAsistencia";
		document.ActuacionAsistenciaForm.anio.value = anioAsistencia;
		document.ActuacionAsistenciaForm.numero.value = numeroAsistencia;
		document.ActuacionAsistenciaForm.idInstitucion.value = idInstitucion;
		var resultado=ventaModalGeneral(document.ActuacionAsistenciaForm.name,"M");
		if(true){
			document.ActuacionAsistenciaForm.modo.value = 'abrir';
		}
	}
	
	function  borrarFila (idFila) { 
		if (!confirm('<siga:Idioma key="gratuita.volantesExpres.mensaje.eliminarAsistencia"/>'))
			return;
		t = document.getElementById("asistencias");
		for (i = 0; i < t.rows.length; i++) {
			if (t.rows[i].id == idFila) 
			{
				// Guardamos los datos a borrar
				fila = idFila.split("_")[1];
				error = '';
				numeroEjg = document.getElementById("ejgNumero_" + fila).value;
				if(numeroEjg &&numeroEjg!='-1'){
					error = '<siga:Idioma key="gratuita.volantesExpres.mensaje.borrar.ejgAsociado"/>';
				}
				designaNumero = document.getElementById("designaNumero_" + fila).value;
				if(designaNumero && designaNumero!='-1'){
					error += '\n'+'<siga:Idioma key="gratuita.volantesExpres.mensaje.borrar.designaAsociada"/>';
				}
				if(error!=''){
					alert(error);
					return;
				}
				t.deleteRow (i); 
				validarAnchoTabla();
				return;
			}
		}
	} 
	
	function validaHora (o) {
		var horas = trim(o.value);
		var mensajehoras = "<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>";
		
           
		
		if (horas.length==1) {
			o.value = "0" + horas;
		}
		if (horas!="" && (horas>23 || horas<0 || !isNumero(horas))) {
			alert(mensajehoras);
			o.focus();
			return false;
		}
	}
	
	function validaMinuto (o) {
		var minutos = trim(o.value);
		var mensajeminutos="<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>";
	
		/*if (minutos.length==0) {
			o.value = "00";
		}*/
		if (minutos.length==1) {
			o.value = "0" + minutos;
		}

		if (minutos!="" && (minutos>59 || minutos<0 || !isNumero(minutos) )) {
			alert(mensajeminutos);
			o.focus();
			return false;
		}
	}
	
	function mostrarAyuda() {
		alert('<siga:Idioma key="gratuita.volantesExpres.ayudaSustitutos"/>');
	}
	
	function cambiarDiligenciaProcedimiento(){ 
		td = document.getElementById("diligenciaProcedimiento");
		if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") 
			td.innerHTML = '<siga:Idioma key="gratuita.volantesExpres.literal.numeroDiligencia"/>';
		else
			td.innerHTML = '<siga:Idioma key="gratuita.volantesExpres.procedimiento"/>';
	}
	
	function cambiarCentroDetencionJuzgado(){
		td = document.getElementById("centroDetencionJuzgado");
		if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") 
			td.innerHTML = '<siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion"/>';
		else
			td.innerHTML = '<siga:Idioma key="gratuita.volantesExpres.literal.juzgado"/>';
			
	}
	setAnchoAsistencias();
</script>
</html>