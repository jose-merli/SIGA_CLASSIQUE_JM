<!-- validarInscripcion.jsp -->

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

<!-- JSP -->

<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaTurnos.literal.title" /></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />

		
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/jquery.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/jquery.custom.js"/>" type="text/javascript"></script>	

	<script type="text/javascript">	
		function mostrarFechaSolicitud() {
			if(document.getElementById("fechaCheck")) {
				document.getElementById("fechaCheck").style.visibility="hidden";
				if(document.getElementById("calendario_fechaCheck")) { 
					document.getElementById("calendario_fechaCheck").style.visibility="hidden";
				}
			}
			
			if(document.getElementById("fechaSolicitud").value=='' || document.getElementById("fechaSolicitudBaja").value=='') {
				if(document.getElementById("fechaSol")){
					fechaActual = getFechaActualDDMMYYYY();
					document.getElementById("fechaSol").value = fechaActual;
				}					
			}
			
			if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
				document.getElementById('observacionesValidacion').style.display = "block";
			}
	
			if(document.getElementById("fechaCheck") && document.InscripcionTGForm.validarInscripciones.value=='N'&&  (document.InscripcionTGForm.modo.value=='sbtComprobarInsertar'||document.InscripcionTGForm.modo.value=='sbgComprobarInsertar')){
				fechaActual = getFechaActualDDMMYYYY();
				document.getElementById("fechaCheck").value = fechaActual;
				
				if(document.InscripcionTGForm.modo.value=='sbtComprobarInsertar'){
					document.getElementById("observacionesValidacion").value = "<siga:Idioma key='censo.sjcs.turnos.bajaEnTurnoAutomatico.observacion.literal'/>";
				}
				
				if(document.InscripcionTGForm.modo.value=='sbgComprobarInsertar'){
					document.getElementById("observacionesValidacion").value = "<siga:Idioma key='censo.sjcs.turnos.bajaEnGuardiaAutomatico.observacion.literal'/>";
				}				
			}
		}	
		
		function comprobarGuardiaGrupo(checkValidar) {			
			if(document.InscripcionTGForm.porGrupos.value=='1'){
				if(checkValidar.checked){
					document.getElementById("divGuardiaGrupo").style.display = "block";
				} else {
					document.getElementById("divGuardiaGrupo").style.display = "none";
				
				}
			}		
		}
		
		function obtenerFecha(tipoAccion) {			
			if(tipoAccion == 'validar') {				
				if(document.getElementById('validar').checked) {
					// if(document.getElementById('asterisco'))
						// document.getElementById('asterisco').innerHTML = '(*)';
					fechaActual = getFechaActualDDMMYYYY();
					document.getElementById('fechaCheck').value = fechaActual;
					document.InscripcionTGForm.denegar.checked = false;
					document.getElementById("fechaCheck").style.visibility="visible";
					document.getElementById("calendario_fechaCheck").style.visibility="visible";
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
						document.getElementById('observacionesValidacion').style.display = "block";
					}
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar' || document.InscripcionTGForm.modo.value=='sbtComprobarInsertar' || document.InscripcionTGForm.modo.value=='smbtInsertarBaja' || document.InscripcionTGForm.modo.value=='sbgComprobarInsertar'){
						document.InscripcionTGForm.syc[0].style.visibility="visible";
						document.InscripcionTGForm.syc[1].style.visibility="visible";
						document.getElementById("capa1").style.visibility="visible"; 
						document.InscripcionTGForm.syc[0].checked=false;
						document.InscripcionTGForm.syc[1].checked=false;
					}	
				} else {
					// if(document.getElementById('asterisco'))
						// document.getElementById('asterisco').innerHTML = '';
					document.InscripcionTGForm.fechaValidacion.value = "";
					document.getElementById('fechaCheck').value = "";
					document.getElementById("fechaCheck").style.visibility="hidden";
					document.getElementById("calendario_fechaCheck").style.visibility="hidden";
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
						document.getElementById('observacionesValidacion').style.display = "block";
					}
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar' || document.InscripcionTGForm.modo.value=='sbtComprobarInsertar' || document.InscripcionTGForm.modo.value=='smbtInsertarBaja' || document.InscripcionTGForm.modo.value=='sbgComprobarInsertar'){
	
						document.InscripcionTGForm.syc[0].style.visibility="hidden";
						document.InscripcionTGForm.syc[1].style.visibility="hidden";
						document.getElementById("capa1").style.visibility="hidden"; 
						document.InscripcionTGForm.syc[0].checked=false;
						document.InscripcionTGForm.syc[1].checked=false;
					}				
				}
			} else if(tipoAccion == 'denegar') {
				if(document.getElementById('denegar').checked) {
					// if(document.InscripcionTGForm.fechaValidacion.value!='')
						// fechaActual = document.InscripcionTGForm.fechaValidacion.value;
					// else
						fechaActual = getFechaActualDDMMYYYY();
					// if(document.getElementById('asterisco'))
						// document.getElementById('asterisco').innerHTML = '(*)';
					document.getElementById('fechaCheck').value = fechaActual;
					document.getElementById('validar').checked = false;
					document.getElementById("fechaCheck").style.visibility="visible";
					document.getElementById("calendario_fechaCheck").style.visibility="visible";
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbtComprobarValidar'||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar' || document.InscripcionTGForm.modo.value=='sbtComprobarInsertar' || document.InscripcionTGForm.modo.value=='smbtInsertarBaja' || document.InscripcionTGForm.modo.value=='sbgComprobarInsertar'){
	
						document.InscripcionTGForm.syc[0].checked=false;
						document.InscripcionTGForm.syc[1].checked=false;
						document.InscripcionTGForm.syc[0].style.visibility="hidden";
						document.InscripcionTGForm.syc[1].style.visibility="hidden";
						document.getElementById("capa1").style.visibility="hidden"; 
					}		
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
						document.getElementById('observacionesValidacion').style.display = "block";
					}						
				} else { 
					// if(document.getElementById('asterisco'))
						// document.getElementById('asterisco').innerHTML = '';
					document.getElementById('fechaCheck').value = "";
					document.getElementById("fechaCheck").style.visibility="hidden";
					document.getElementById("calendario_fechaCheck").style.visibility="hidden";
					if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
						document.getElementById('observacionesValidacion').style.display = "block";
					}					
				}
			}
				
		}
			
		function accionCancelar() {		
			window.top.close();
		}
			
		/* JPT: Informacion de inscripciones de turno y guardia:
		- sitEditarTelefonosGuardia: Insercion de inscripcion de turno individual (sitInsertar)
		- smitEditarTelefonosGuardia: Insercion de inscripcion de turno masivo (smitInsertar)
		*/
		function accionSiguiente() {	
			//sub();
			
			if(document.getElementById('validar')){
				if(document.getElementById('validar').checked) {
					document.InscripcionTGForm.fechaValidacion.value = document.getElementById('fechaCheck').value;
					// La fecha de validacion no puede ser nula
					if(document.InscripcionTGForm.fechaValidacion.value == "") {
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
						document.getElementById('fechaCheck').focus();
						return false;
					}
						
					/* JPT: Voy a anular las validaciones temporalmente
					
					ATENCION: Las validaciones se hacen en el servidor, al finalizar
					 - CASO TURNO: La fecha de validacion debe ser superior a la fecha de baja del ultimo turno 
					 - CASO GUARDIA: La fecha de validacion debe ser superior a la fecha de baja de la ultima guardia
					                                               y superior o igual que la fecha de validacion del turno
					
					//  La fecha de validacion debe ser superior a la fecha de baja de la inscripcion anterior	
					var fechaBajaTurnoAnterior = document.InscripcionTGForm.fechaBajaTurno.value;
					if(fechaBajaTurnoAnterior!=''){
						fechaBajaTurnoAnterior = fechaBajaTurnoAnterior.substring(8,10) + "/" + fechaBajaTurnoAnterior.substring(5,7) + "/" + fechaBajaTurnoAnterior.substring(0,4);
						var fechaValidacion = document.InscripcionTGForm.fechaValidacion.value;
						var comparaFecha = compararFecha(fechaBajaTurnoAnterior, fechaValidacion);
						
						// return 0: F1 = F2
						// return 1: F1 > F2 
						// return 2: F1 < F2 
						if (comparaFecha < 2) {
							fin();
							error = '<siga:Idioma key="gratuita.gestionInscripciones.error.valida.menor.baja.ia"/>';
							error += ' '+fechaBajaTurnoAnterior;
							alert(error);
							return false;
						}
					}										
					*/
				}
			}
				
			if((document.getElementById('validar')&&document.getElementById('validar').checked)||!document.getElementById('validar')){
				if(document.InscripcionTGForm.porGrupos.value=='1'){
					if(document.InscripcionTGForm.modo.value=='sigInsertar'||document.InscripcionTGForm.modo.value=='vigValidar'){
						if(document.InscripcionTGForm.numeroGrupo.value == "") {
								fin();
								error = "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.porGrupos.numero'/>"+ '\n';
								alert(error);
								return false;
							}
							if(document.InscripcionTGForm.ordenGrupo.value == "") {
								fin();
								error = "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.porGrupos.orden'/>"+ '\n';
								alert(error);
								return false;
							}
					} else {
						if(document.getElementById('mostrarAvisoPorGrupo')){
							alert("<siga:Idioma key='gratuita.guardiasTurno.aviso.porGrupos.insertarEnGrupo'/>");							
						}		
					}			
				}
			}
			document.InscripcionTGForm.submit();
		}
			
		/* JPT: Informacion de inscripciones de turno y guardia: 
		- vitValidar: Insercion de inscripcion de turno por alta pendiente individual
		- vmitValidar: Insercion de inscripcion de turno por alta pendiente masiva
		- sbtComprobarInsertar: Baja de inscripcion de turno individual (puede venir sin validacion)
		- smbtInsertarBaja: Baja de inscripcion de turno masiva (puede venir sin validacion)
		- vbtComprobarValidar: Baja de inscripcion de turno por baja pendiente individual
		- vmbtComprobarValidar: Baja de inscripcion de turno por baja pendiente masiva
		
		- sigInsertar: Insercion de inscripcion de guardia por alta (puede venir sin validacion)
		- vigValidar: Insercion de inscripcion de guardia por alta pendiente individual 
		- vmigValidar: Insercion de inscripcion de guardia por alta pendiente masiva 
		- sbgComprobarInsertar: Baja de inscripcion de guardia por baja (puede venir sin validacion) 
		- vbgComprobarValidar: Baja de inscripcion de guardia por baja pendiente individual
		- vbgComprobarValidar: Baja de inscripcion de guardia por baja pendiente masivo
		*/
		function accionFinalizar() {		
				sub();
				
				// No es obligatoria la fecha en los siguiente casos:
				// - sigInsertar -----------> Alta de guardia
				// - sbgComprobarInsertar --> Baja de guardia
				// - sbtComprobarInsertar --> Baja de turno individual
				// - smbtInsertarBaja ------> Baja de turno masiva
				if(document.InscripcionTGForm.modo.value!='sigInsertar'
						&& document.InscripcionTGForm.modo.value!='sbgComprobarInsertar'
						&& document.InscripcionTGForm.modo.value!='sbtComprobarInsertar'
						&& document.InscripcionTGForm.modo.value!='smbtInsertarBaja' 
						&& document.getElementById('validar').checked==false 
						&& document.getElementById('denegar').checked==false) {
						fin();
						alert("<siga:Idioma key="gratuita.altaTurno.literal.alert1"/>");
						return false;
				}
				
				//En el caso de que no sea necesario la validacion no se hace nada mas
				if(document.getElementById('validar')){
					// Validamos que exista fecha de validacion y ciones de validacion
					
					//cuando se quiere validar habra que diferenciar el alta y la baja
					
					//Caso alta de inscripcion
					if(document.InscripcionTGForm.modo.value=='sigInsertar'
						||document.InscripcionTGForm.modo.value=='vitValidar'
						||document.InscripcionTGForm.modo.value=='vigValidar'
						||document.InscripcionTGForm.modo.value=='vmitValidar'
						||document.InscripcionTGForm.modo.value=='vmigValidar') {	
						
						if(document.getElementById('validar').checked) {						
							document.InscripcionTGForm.fechaValidacion.value = document.getElementById('fechaCheck').value;
							document.InscripcionTGForm.fechaDenegacion.value = "";
							
							// La fecha de validacion no puede ser nula
							if(document.InscripcionTGForm.fechaValidacion.value == "") {
								fin();
								alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
								document.getElementById('fechaCheck').focus();
								return false;
							}																	
							
							/*
							 ATENCION: Las validaciones se hacen en el servidor, al finalizar
							 - CASO TURNO: La fecha de validacion debe ser superior a la fecha de baja del ultimo turno 
							 - CASO GUARDIA: La fecha de validacion debe ser superior a la fecha de baja de la ultima guardia
							                                               y superior o igual que la fecha de validacion del turno                                                              
							 */
						
						} else { 
							if(document.getElementById('denegar').checked) {
								document.InscripcionTGForm.fechaValidacion.value = "";
								document.InscripcionTGForm.fechaDenegacion.value = document.getElementById('fechaCheck').value;
								
								if(document.InscripcionTGForm.fechaDenegacion.value == "") {
									fin();
									alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
									document.getElementById('fechaCheck').focus();
									return false;
								}						
								
								if(document.InscripcionTGForm.observacionesValidacion) {
									document.InscripcionTGForm.observacionesDenegacion.value = document.InscripcionTGForm.observacionesValidacion.value;
									if(document.InscripcionTGForm.observacionesDenegacion.value == "") {
										fin();
										alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
										document.InscripcionTGForm.observacionesValidacion.focus();
										return false;
									}							
								}									
							}
						}			
						
				} else {
					// Este es el caso de las bajas de inscripcion
					// sbtComprobarInsertar
		  			// sbgComprobarInsertar
					// vmbtComprobarValidar
					// vbgComprobarValidar
					// vbtComprobarValidar
					// vmbgComprobarValidar
					if(document.getElementById('validar').checked) {						
						document.InscripcionTGForm.fechaBaja.value = document.getElementById('fechaCheck').value;
						document.InscripcionTGForm.fechaDenegacion.value = "";
						
						if(document.InscripcionTGForm.fechaBaja.value == "") {
							fin();
							alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
							document.getElementById('fechaCheck').focus();
							return false;
						}
						
						// JPT: La fecha de baja debe ser superior o igual a la fecha de validacion de la inscripcion de turno	
						if(document.InscripcionTGForm.fechaValidacionTurno.value!='' ){
							
							/* JPT: Voy a anular las validaciones temporalmente
							
							ATENCION: Las validaciones se hacen en el servidor, al finalizar
							- CASO TURNO: La fecha de baja debe ser superior o igual a la fecha de validacion del turno
						 	                                      y superior o igual a la fecha de NVL(baja,alta) de todas sus guardias 
							- CASO GUARDIA: La fecha de baja debe ser superior o igual a la fecha de validacion de la guardia  
							
							if( document.InscripcionTGForm.modo.value != 'sbtComprobarInsertar' &&
								document.InscripcionTGForm.modo.value != 'vbtComprobarValidar') {
								
								var fechaValidacion = document.InscripcionTGForm.fechaValidacionTurno.value;
								fechaValidacion = fechaValidacion.substring(8,10) + "/" + fechaValidacion.substring(5,7) + "/" + fechaValidacion.substring(0,4);
								var fechaBaja = document.InscripcionTGForm.fechaBaja.value;
								var comparaFecha = compararFecha(fechaValidacion, fechaBaja);
								
								// return 0: F1 = F2
								// return 1: F1 > F2 
								// return 2: F1 < F2 
								if (comparaFecha == 1) {
									fin();
									if(document.InscripcionTGForm.idGuardia.value!=''){
										error = "<siga:Idioma key='gratuita.gestionInscripciones.error.bajaguardia.menor.validaturno'/>";
									} else{
										error = "<siga:Idioma key='gratuita.gestionInscripciones.error.baja.menor.valida'/>";
									}
									error += ' ' + fechaValidacion;								
									// alert("La fecha de baja no puede ser inferior a la fecha de solicitud ni de validacion: "+fechaValidacion);
									alert(error);
									return false;
								}
							} 
							 */							
						}
						
						// JPT: La fecha de baja debe ser superior o igual a la fecha de validacion de la inscripcion de guardia	
						if(document.InscripcionTGForm.idGuardia.value!=''){
							
							/* JPT: Voy a anular las validaciones temporalmente
							
							ATENCION: Las validaciones se hacen en el servidor, al finalizar
							 - CASO GUARDIA: ...
							 
							var fechaValidacion = document.InscripcionTGForm.fechaValidacion.value;
							fechaValidacion = fechaValidacion.substring(8,10) + "/" + fechaValidacion.substring(5,7) + "/" + fechaValidacion.substring(0,4);
							var fechaBaja = document.InscripcionTGForm.fechaBaja.value;
							var comparaFecha = compararFecha(fechaValidacion, fechaBaja);
							
							// return 0: F1 = F2
							// return 1: F1 > F2 
							// return 2: F1 < F2 
							if (comparaFecha == 1) {
								fin();
								error = "<siga:Idioma key='gratuita.gestionInscripciones.error.baja.menor.valida'/>";
								error += ' '+fechaValidacion;								
								// alert("La fecha de baja no puede ser inferior a la fecha de solicitud ni de validacion: "+fechaValidacion);
								alert(error);
								return false;
							}	
							*/
							
							if( (document.InscripcionTGForm.modo.value=='vbgComprobarValidar'
									||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar' 
									||  document.InscripcionTGForm.modo.value=='sbgComprobarInsertar') 
								&& document.InscripcionTGForm.syc[0].checked==true) {
								document.getElementById("tipoActualizacionSyC").value="G";	
								
							} else if( (document.InscripcionTGForm.modo.value=='vbgComprobarValidar'
											||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar'
											||  document.InscripcionTGForm.modo.value=='sbgComprobarInsertar') 
										&& document.InscripcionTGForm.syc[1].checked==true) {
								document.getElementById("tipoActualizacionSyC").value="";
							}
							
						} else {
							if( (document.InscripcionTGForm.modo.value=='vbtComprobarValidar'
								|| document.InscripcionTGForm.modo.value=='vmbtComprobarValidar' 
								|| document.InscripcionTGForm.modo.value=='sbtComprobarInsertar' 
								|| document.InscripcionTGForm.modo.value=='smbtInsertarBaja') 
								&& document.InscripcionTGForm.syc[0].checked==true) {
									document.getElementById("tipoActualizacionSyC").value="T";	
							
							} else if( (document.InscripcionTGForm.modo.value=='vbtComprobarValidar'
								|| document.InscripcionTGForm.modo.value=='vmbtComprobarValidar'
								|| document.InscripcionTGForm.modo.value=='sbtComprobarInsertar'
								|| document.InscripcionTGForm.modo.value=='smbtInsertarBaja') 
								&& document.InscripcionTGForm.syc[1].checked==true) {
									document.getElementById("tipoActualizacionSyC").value="";
							}	
						}
						
						document.InscripcionTGForm.observacionesValBaja.value = document.InscripcionTGForm.observacionesValidacion.value;	
						
						if( (document.InscripcionTGForm.modo.value=='vbgComprobarValidar'
									||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'
									||document.InscripcionTGForm.modo.value=='vmbtComprobarValidar'
									||document.InscripcionTGForm.modo.value=='vmbgComprobarValidar' 
									|| document.InscripcionTGForm.modo.value=='sbtComprobarInsertar' 
									|| document.InscripcionTGForm.modo.value=='smbtInsertarBaja' 
									|| document.InscripcionTGForm.modo.value=='sbgComprobarInsertar') 
								&& document.InscripcionTGForm.syc[0].checked==false 
								&& document.InscripcionTGForm.syc[1].checked==false) {
							fin();
							alert("<siga:Idioma key='gratuita.altaTurnos.literal.defsaltosycompensaciones'/>");							
							return false;
						}			
						
					} else {
						if(document.getElementById('denegar').checked) {
							document.InscripcionTGForm.fechaBaja.value = "";
							document.InscripcionTGForm.fechaDenegacion.value = document.getElementById('fechaCheck').value;
							
							if(document.InscripcionTGForm.fechaDenegacion.value == "") {
								fin();
								alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
								document.getElementById('fechaCheck').focus();
								return false;
							} 
							
							if(document.InscripcionTGForm.observacionesValidacion) {
								document.InscripcionTGForm.observacionesDenegacion.value = document.InscripcionTGForm.observacionesValidacion.value;
								if(document.InscripcionTGForm.observacionesDenegacion.value == "") {
									fin();
									alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
									document.InscripcionTGForm.observacionesValidacion.focus();
									return false;
								}							
							}											
						}
					}
				}					
			}
			if((document.getElementById('validar')&&document.getElementById('validar').checked)||!document.getElementById('validar')){
				if(document.InscripcionTGForm.porGrupos.value=='1'){
					if(document.InscripcionTGForm.modo.value=='sigInsertar'||document.InscripcionTGForm.modo.value=='vigValidar'){
							if(document.InscripcionTGForm.numeroGrupo.value == "") {
								fin();
								error = "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.porGrupos.numero'/>"+ '\n';
								alert(error);
								return false;
							}
							if(document.InscripcionTGForm.ordenGrupo.value == "") {
								fin();
								error = "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.porGrupos.orden'/>"+ '\n';
								alert(error);
								return false;
							}
					} else {
						if(document.getElementById('mostrarAvisoPorGrupo')){
							alert("<siga:Idioma key='gratuita.guardiasTurno.aviso.porGrupos.insertarEnGrupo'/>");
						}		
					}			
				}
			}
			document.InscripcionTGForm.target="submitArea";
			document.InscripcionTGForm.submit();
			window.top.returnValue="MODIFICADO";			
		}	
		
		function refrescarLocal() {			
			fin();
		}
	
	</script>
</head>

<body onload="mostrarFechaSolicitud();">


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
		<c:if test="${InscripcionTGForm.modo=='sitEditarTelefonosGuardia'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.solicitud.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sbtComprobarInsertar' || InscripcionTGForm.modo=='smbtInsertarBaja'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.solicitudBaja.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sigInsertar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitud.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='sbgComprobarInsertar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.solicitudBaja.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vitValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.validar.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vbtComprobarValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.validarBaja.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vigValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.validar.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vbgComprobarValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.validarBaja.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='smitEditarTelefonosGuardia'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.masiva.solicitud.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vmitValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.masiva.validar.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vmbtComprobarValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.turno.masiva.solicitudBaja.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vmigValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.masiva.solicitud.datos" />
		</c:if>
		<c:if test="${InscripcionTGForm.modo=='vmbgComprobarValidar'}">
			<siga:Idioma key="gratuita.gestionInscripciones.paso.inscripcion.guardia.masiva.solicitudBaja.datos" />
		</c:if>
		
		</td>
	</tr>
</table>


<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
<!-- INICIO: CAMPOS -->
<!-- Zona de campos de busqueda o filtro -->
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}" method="post" target="_self">
	<html:hidden property="fechaSolicitud" styleId="fechaSolicitud"/>
	<html:hidden property="fechaBaja" styleId="fechaBaja"/>
	<html:hidden property="fechaSolicitudBaja" styleId="fechaSolicitudBaja"/>
	<html:hidden property="fechaValidacion" styleId="fechaValidacion"/>
	<html:hidden property="idGuardia" styleId="idGuardia"/>
	<html:hidden property="porGrupos" styleId="porGrupos"/>
	<html:hidden property="modo" styleId="modo"/>
	<html:hidden property="validarInscripciones" styleId="validarInscripciones"/>
	<html:hidden property="fechaBajaTurno" styleId="fechaBajaTurno"/>
	<html:hidden property="fechaValidacionTurno" styleId="fechaValidacionTurno"/>
	<html:hidden property="fechaDenegacion" styleId="fechaDenegacion"/>
	<html:hidden property="observacionesDenegacion" styleId="observacionesDenegacion"/>
	<html:hidden property="observacionesValBaja" styleId="observacionesValBaja"/>
	<html:hidden property="tipoActualizacionSyC" styleId="tipoActualizacionSyC"/>	
	
	<c:if test="${(InscripcionTGForm.fechaSolicitud==null || InscripcionTGForm.fechaSolicitud=='')&& InscripcionTGForm.solicitudAlta==true}">
<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudAlta">
	 
	 <table width="100%" border="0">
	 	<tr>
		 	<td width="25%"></td>
		 	<td width="15%"></td>
		 	<td width="25%"></td>
		 	<td width="35%"></td>
	 	
	 	</tr>
		<tr>
		<!-- obtenemos los campos para el alta de turnos -->
			<td class="labelText">
				<siga:Idioma
					key="gratuita.altaTurnos.literal.fsolicitud" />
				</td>
			<td class="labelText">
				<input type="text" name="fechaSol" class="boxConsulta" readOnly="true"/>
			</td>
			<c:choose>
			<c:when test="${InscripcionTGForm.irpf=='0'}">
				<td colspan="2">
				<table>
					<tr>
						<td class="labelText" style="align:right">
							<siga:Idioma key='gratuita.altaTurnos.literal.retencion' />
						</td>
		
						<td align="left">
							<logic:notEmpty name="InscripcionTGForm" property="retenciones">
								<html:select property="idRetencion" styleClass="boxCombo" styleId="idRetencion">
									<logic:iterate name="InscripcionTGForm" property="retenciones"
										id="retencion" indexId="index">										
											<html:option value="${retencion.idRetencion}">
												<c:out value="${retencion.descripcion}"></c:out>
											</html:option>
										</logic:iterate>
								</html:select>								
							</logic:notEmpty>
						</td>
					</tr>
				</table>
				</td>
			</c:when>
			<c:otherwise>
				<td class="labelText">&nbsp;</td>
				<td class="labelText">&nbsp;</td>
			</c:otherwise>
			</c:choose>
		</tr>
		<tr>			
			<td class="labelText">
				<siga:Idioma key="gratuita.altaTurnos.literal.osolicitud"/>
			</td>
			<td colspan="3">
				<html:textarea name="InscripcionTGForm" property="observacionesSolicitud" styleId="observacionesSolicitud"
					onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="false"></html:textarea>
			</td>
		</tr>
	 </table>
	 
	</siga:ConjCampos>
</c:if>
<c:if test="${InscripcionTGForm.solicitudBaja==true}">
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudBaja">
	 <table width="100%" border="0">
	 	<tr>
		 	<td width="25%"></td>
		 	<td width="15%"></td>
		 	<td width="25%"></td>
		 	<td width="35%"></td>	 	
	 	</tr>
		<tr>
		<!-- obtenemos los campos para el alta de turnos -->
			<td class="labelText">
				<siga:Idioma key="gratuita.altaTurnos.literal.fsolicitud" />
			</td>
			<td class="labelText">
				<input type="text" name="fechaSol" id="fechaSol" class="boxConsulta" readOnly="true"/>
			</td>
			<td class="labelText">&nbsp;</td>
			<td class="labelText">&nbsp;</td>
		</tr>
		<tr>			
			<td class="labelText">
				<siga:Idioma key="gratuita.altaTurnos.literal.mbaja"/>
			</td>
			<td colspan="3"> 
				<html:textarea name="InscripcionTGForm" property="observacionesBaja" styleId="observacionesBaja" 
					onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="false"></html:textarea>
			</td>
		</tr>
	 </table>
	 
	</siga:ConjCampos>	

</c:if>

	<c:if test="${InscripcionTGForm.usrBean.letrado==false}">
		<c:if
			test="${(InscripcionTGForm.validarInscripciones=='S'&&(InscripcionTGForm.validacionAlta==true||InscripcionTGForm.validacionBaja==true))||InscripcionTGForm.masivo==true}">
			<c:if
				test="${InscripcionTGForm.solicitudAlta==false && InscripcionTGForm.validacionAlta==true&& InscripcionTGForm.masivo==false}">
				<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudAlta">

					<table width="100%" border="0">
						<tr>
							<td width="25%"></td>
							<td width="15%"></td>
							<td width="25%"></td>
							<td width="35%"></td>
						</tr>
						<tr>
							<!-- obtenemos los campos para el alta de turnos -->
							<td class="labelText">
								<siga:Idioma
								key="gratuita.altaTurnos.literal.fsolicitud" />
							</td>
							<td class="labelText">
								<c:out value="${InscripcionTGForm.fechaSolicitudJsp}"></c:out>
							</td>
							<td class="labelText">&nbsp;</td>
							<td class="labelText">&nbsp;</td>
						</tr>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.altaTurnos.literal.osolicitud" />
							</td>
							<td colspan="3">
								<html:textarea name="InscripcionTGForm" property="observacionesSolicitud" styleId="observacionesSolicitud" 
									onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" 
									styleClass="box" style="overflow:auto;width=400;height=80"
									disabled="true"></html:textarea>
							</td>
						</tr>
					</table>

				</siga:ConjCampos>
			</c:if>
			<c:if test="${InscripcionTGForm.solicitudBaja==false && InscripcionTGForm.validacionBaja==true&& InscripcionTGForm.masivo==false}">
				<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudBaja">
					<table width="100%" border="0">
						<tr>
							<td width="25%"></td>
							<td width="15%"></td>
							<td width="25%"></td>
							<td width="35%"></td>
						</tr>
						<tr>
							<!-- obtenemos los campos para el alta de turnos -->
							<td class="labelText">
								<siga:Idioma key="gratuita.altaTurnos.literal.fsolicitud" />
							</td>
							<td class="labelText">
								<c:out value="${InscripcionTGForm.fechaSolicitudBajaJsp}"></c:out>
							</td>
							<td class="labelText">&nbsp;</td>
							<td class="labelText">&nbsp;</td>
						</tr>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.altaTurnos.literal.mbaja" />
							</td>
							<td colspan="3">
								<html:textarea name="InscripcionTGForm" styleId="observacionesBaja"
									property="observacionesBaja" onChange="cuenta(this,1024)"
									cols="65" rows="2" onkeydown="cuenta(this,1024);"
									styleClass="box" style="overflow:auto;width=400;height=80"
									disabled="true"></html:textarea>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>

			</c:if>

			<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.validacion">
				<table width="100%" border="0">
					<tr>
						<td width="25%"></td>
						<td width="15%"></td>
						<td width="25%"></td>
						<td width="35%"></td>
					</tr>

					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.altaTurnos.literal.fvalidacion" />
						</td>
						<td>
							<siga:Fecha nombreCampo="fechaCheck" readOnly="true"></siga:Fecha>
							
						</td>
						<td colspan="2" align="left">

						<table>

							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.altaTurnos.literal.validacion" />
								</td>

								<c:choose>
									<c:when
										test="${InscripcionTGForm.modo=='vigValidar'||InscripcionTGForm.modo=='vitValidar'||InscripcionTGForm.modo=='vmitValidar'||InscripcionTGForm.modo=='vmigValidar'}">
										<td>
											<input type="checkbox" id="validar" name="validar" value="no"
												onClick="obtenerFecha('validar');comprobarGuardiaGrupo(this);">
										</td>
										<td class="labelText">
											<siga:Idioma key="gratuita.altaTurnos.literal.denegar" />
										</td>
										<td>
											<input type="checkbox" id="denegar" name="denegar" value="no"
												onClick="obtenerFecha('denegar');document.getElementById('divGuardiaGrupo').style.display = 'none';" />
										</td>
										<td>&nbsp;</td>
									</c:when>
									
									<c:when
										test="${InscripcionTGForm.modo=='vbgComprobarValidar'||InscripcionTGForm.modo=='vbtComprobarValidar'||InscripcionTGForm.modo=='vmbtComprobarValidar'||InscripcionTGForm.modo=='vmbgComprobarValidar'}">
										<td>
											<input type="checkbox" id="validar" name="validar" value="no"
												onClick="obtenerFecha('validar');comprobarGuardiaGrupo(this);">
										</td>
										<td class="labelText">
											<siga:Idioma key="gratuita.altaTurnos.literal.denegar" />
										</td>
										<td>
											<input type="checkbox" name="denegar" value="no"
												onClick="obtenerFecha('denegar');document.getElementById('divGuardiaGrupo').style.display = 'none';" />
										</td>
										<td class="labelText">
											<div id="capa1" style="visibility: hidden">								
												<siga:Idioma key="gratuita.altaTurnos.literal.actualizarSyC" />:
												<!-- td><input type="checkbox" name="syc" value="no" style="visibility: hidden"></td> -->
												<input type="Radio" name="syc" value="s"  style="visibility: hidden"> si 
												<input type="Radio" name="syc" value="n"  style="visibility: hidden"> no 
											</div>
										</td>	
									</c:when>
									
									<c:when
										test="${InscripcionTGForm.modo=='sbgComprobarInsertar'||InscripcionTGForm.modo=='sbtComprobarInsertar'||InscripcionTGForm.modo=='smbtInsertarBaja'}">
										<td>
											<input type="checkbox" id="validar" name="validar" value="no"
												onClick="obtenerFecha('validar');comprobarGuardiaGrupo(this);">
										</td>
										<td>
											<input type="checkbox" id="denegar" name="denegar" value="no" style="visibility: hidden" />
										</td>
										<td>&nbsp;</td>	
										<td class="labelText">
											<div id="capa1" style="visibility: hidden">								
												<siga:Idioma key="gratuita.altaTurnos.literal.actualizarSyC" />:
												<!-- td><input type="checkbox" name="syc" value="no" style="visibility: hidden"></td> -->
												<input type="Radio" name="syc" value="s"  style="visibility: hidden"> si 
												<input type="Radio" name="syc" value="n"  style="visibility: hidden"> no 
											</div>
										</td>	
									</c:when>
									
									<c:otherwise>
										<td>
											<input type="checkbox" id="validar" name="validar" value="no" onClick="obtenerFecha('validar');comprobarGuardiaGrupo(this);">
										</td>
										<td>&nbsp;</td>
										<td>
											<input type="checkbox" id="denegar" name="denegar" value="no" style="visibility: hidden"></td>
										<td>&nbsp;</td>	
									</c:otherwise>
								</c:choose>
							</tr>
						</table>
						</td>

					</tr>
					<c:if
						test="${InscripcionTGForm.modo=='vigValidar'||InscripcionTGForm.modo=='vitValidar'||InscripcionTGForm.modo=='vbgComprobarValidar'||InscripcionTGForm.modo=='vbtComprobarValidar'||
					InscripcionTGForm.modo=='vmitValidar'||InscripcionTGForm.modo=='vmigValidar'||
					InscripcionTGForm.modo=='sitEditarTelefonosGuardia'||InscripcionTGForm.modo=='sigInsertar'||InscripcionTGForm.modo=='smitEditarTelefonosGuardia'||InscripcionTGForm.modo=='sbgComprobarInsertar'||InscripcionTGForm.modo=='sbtComprobarInsertar'||InscripcionTGForm.modo=='smbtInsertarBaja'||InscripcionTGForm.modo=='vmbtComprobarValidar'||InscripcionTGForm.modo=='vmbgComprobarValidar'  }">
						<tr id="observacionesValidacion">
							<td class="labelText">
								<siga:Idioma key="gratuita.altaTurnos.literal.ovalidacion" />
							</td>
							<td colspan="3">
								<html:textarea name="InscripcionTGForm" property="observacionesValidacion" styleId="observacionesValidacion"
									onChange="cuenta(this,1024)"
									cols="65" rows="2" onkeydown="cuenta(this,1024);"
									styleClass="box" style="overflow:auto;width=400;height=80"
									readOnly="false">
								</html:textarea>
							</td>
						</tr>
					</c:if>
				</table>
			</siga:ConjCampos>

		</c:if>
		<!-- principio baja con validacion automatica  -->
				<c:if
			test="${InscripcionTGForm.validarInscripciones=='N'&&InscripcionTGForm.validacionBaja==true && (InscripcionTGForm.modo=='sbtComprobarInsertar' || InscripcionTGForm.modo=='sbgComprobarInsertar')}">

			<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.validacion">
				
					<table width="100%" border="0">
				 	<tr>
					 	<td width="25%"></td>
					 	<td width="15%"></td>
					 	<td width="15%"></td>
					 	<td width="35%"></td>
				 	
				 	</tr>
					<tr>
					<!-- obtenemos los campos para el alta de turnos -->
						<td class="labelText">
							<siga:Idioma key="gratuita.altaTurnos.literal.fvalidacion" />
						</td>
						<td class="labelText">
							<input type="text" id="fechaCheck" name="fechaCheck" class="boxConsulta" readOnly="true"/>							
						</td>
						<td class="labelText">
							<siga:Idioma key="gratuita.altaTurnos.literal.validacion" />
						</td>
						<td class="labelText">
							<input type="checkbox" id="validar" name="validar" value="si" checked="checked" disabled="true" />
						</td>			
					</tr>
					<tr>						
						<td class="labelText">
							<siga:Idioma key="gratuita.altaTurnos.literal.validacion"/>
						</td>
						<td colspan="3"> 
							<html:textarea name="InscripcionTGForm" property="observacionesValidacion" styleId="observacionesValidacion"
								onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="true">
							</html:textarea>
						</td>
						<td class="labelText">&nbsp;</td>
						<td class="labelText">&nbsp;</td>
					</tr>
				 </table>			

			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.saltosycompensaciones">
				
					<table width="100%" border="0">
				 	<tr>
					 	<td width="35%"></td>
					 	<td width="15%"></td>
					 	<td width="10%"></td>
					 	<td width="40%"></td>				 	
				 	</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.altaTurnos.literal.actualizarSyC" />:
						</td>
						<td class="labelText" align="right">					
							<input type="Radio" name="syc" value="s"> si
							<input type="Radio" name="syc" value="n"> no  
						</td>
						<td class="labelText"  align="left">
						</td>					
						<td class="labelText">&nbsp;</td>
					</tr>
				 </table>			

			</siga:ConjCampos>
		</c:if>
		<!-- fin baja con validacion automatica-->
		
		<div id="divGuardiaGrupo" style="display: none"><c:choose>

			<c:when
				test="${InscripcionTGForm.porGrupos=='1'&&(InscripcionTGForm.modo=='vigValidar'||InscripcionTGForm.modo=='sigInsertar') }">
				<siga:ConjCampos leyenda="Guardia de grupo">
					<table width="100%" border="0">
						<tr>
							<td width="25%"></td>
							<td width="15%"></td>
							<td width="60%"></td>
						</tr>
						
						<tr>
							<!-- obtenemos los campos para el alta de turnos -->
							<td class="labelText">
								<siga:Idioma key="gratuita.guardiasTurno.literal.porGrupos.numero" />
							</td>
							<td class="labelText">
								<html:text property="numeroGrupo" styleId="numeroGrupo" size="6" maxlength="6" styleClass="box" />
							</td>
							<td rowspan="2" align="center">
								<div style="height: 140px; width: 100%; overflow-y: auto">
	
									<table id='tabInscripcionesCabeceras' border='1' width='100%'
										cellspacing='0' cellpadding='0'>
										<tr class='tableTitle'>
											<td align='center' width='8%'>
												<siga:Idioma key="gratuita.guardiasTurno.literal.porGrupos.numero" />
											</td>
											<td align='center' width='8%'>
												<siga:Idioma key="gratuita.guardiasTurno.literal.porGrupos.orden" />
											</td>
											<td align='center' width='8%'>
												<siga:Idioma key="gratuita.listaTurnosLetrados.literal.numeroletrado" />
											</td>
											<td align='center' width='13%'>
												<siga:Idioma key="gratuita.listaTurnosLetrados.literal.nombreletrado" />
											</td>		
										</tr>
		
										<logic:notEmpty name="InscripcionTGForm"
											property="gruposGuardiaLetrado">
											<logic:iterate name="InscripcionTGForm"
												property="gruposGuardiaLetrado" id="grupoGuardiaLetrado"
												type="com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion"
												indexId="index">
												<tr class="<%=((index+1)%2==0?"filaTablaPar":"filaTablaImpar")%>">
													<td align='center' width='8%'>
														<c:choose>
															<c:when test="${grupoGuardiaLetrado.numeroGrupo!=null&&grupoGuardiaLetrado.numeroGrupo!=''}">
																<c:out	value="${grupoGuardiaLetrado.numeroGrupo}" />
															</c:when>
															<c:otherwise>&nbsp;</c:otherwise>
														</c:choose>													
													</td>
													<td align='center' width='8%'>
														<c:choose>
															<c:when test="${grupoGuardiaLetrado.ordenGrupo!=null&&grupoGuardiaLetrado.ordenGrupo!=''}">
																<c:out	value="${grupoGuardiaLetrado.ordenGrupo}" />
															</c:when>
															<c:otherwise>&nbsp;</c:otherwise>
														</c:choose>					
													</td>
													<td align='center' width='8%'>
														<c:out value="${grupoGuardiaLetrado.persona.nombre}" />
													</td>
													<td align='center' width='13%'>
														<c:out value="${grupoGuardiaLetrado.persona.colegiado.NColegiado}"/>
													</td>		
												</tr>
											</logic:iterate>
										</logic:notEmpty>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<!-- obtenemos los campos para el alta de turnos -->
							<td class="labelText">
								<siga:Idioma key="gratuita.guardiasTurno.literal.porGrupos.orden" />
							</td>
							<td class="labelText">
								<html:text property="ordenGrupo" styleId="ordenGrupo" size="6" maxlength="6" styleClass="box" />
							</td>
						</tr>
					</table>
					
				</siga:ConjCampos> 
				</c:when>
				<c:when 
					test="${InscripcionTGForm.porGrupos=='1'&&(InscripcionTGForm.modo=='vitValidar'||InscripcionTGForm.modo=='vmitValidar'||InscripcionTGForm.modo=='vmigValidar'||InscripcionTGForm.modo=='sitEditarTelefonosGuardia'||InscripcionTGForm.modo=='smitEditarTelefonosGuardia' ) }">
					<input type="hidden" name="mostrarAvisoPorGrupo" id="mostrarAvisoPorGrupo" />
				</c:when>			
			</c:choose>
		</div>
	</c:if>

</html:form>

	<c:choose>
		<c:when test="${InscripcionTGForm.modo!='sitEditarTelefonosGuardia'&&InscripcionTGForm.modo!='smitEditarTelefonosGuardia'}">
			<siga:ConjBotonesAccion botones="X,F" ordenar="false" />
		</c:when>		

		<c:otherwise>
			<siga:ConjBotonesAccion botones="X,S" ordenar="false" />
		</c:otherwise>
	</c:choose>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea"
	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

<script>
		if(document.getElementById("divGuardiaGrupo")!=null && document.InscripcionTGForm.validarInscripciones.value=='N')
			document.getElementById("divGuardiaGrupo").style.display = "block";
</script>

</body>
</html>