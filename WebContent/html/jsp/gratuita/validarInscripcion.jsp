<!-- validarInscripcion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

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
<link id="default" rel="stylesheet" type="text/css"
	href='<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>'>
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>

<script type="text/javascript">
	
	function mostrarFechaSolicitud()
	{
		if(document.InscripcionTGForm.fechaSolicitud.value==''||document.InscripcionTGForm.fechaSolicitudBaja.value=='')
		{
				if(document.getElementById("fechaSol")){
					fechaActual = getFechaActualDDMMYYYY();
					document.getElementById("fechaSol").value = fechaActual;
				}
				
		}
		if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
			document.getElementById('observacionesValidacion').style.display = "block";
		}
		
	}
	
	
	function obtenerFecha(tipoAccion)
	{
		
		if(tipoAccion == 'validar')
		{
			
			if(document.getElementById('validar').checked)
			{
				// if(document.getElementById('asterisco'))
					// document.getElementById('asterisco').innerText = '(*)';
				fechaActual = getFechaActualDDMMYYYY();
				document.getElementById('fechaCheck').value = fechaActual;
				document.InscripcionTGForm.denegar.checked = false;
				document.getElementById("calendarioTd").style.visibility="visible";
				if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
					document.getElementById('observacionesValidacion').style.display = "block";
				}
			}
			else
			{
				// if(document.getElementById('asterisco'))
					// document.getElementById('asterisco').innerText = '';
				document.getElementById('fechaCheck').value = "";
				document.getElementById("calendarioTd").style.visibility="hidden";
				if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
					document.getElementById('observacionesValidacion').style.display = "block";
				}
				
			}
		}else if(tipoAccion == 'denegar')
		{
			if(document.getElementById('denegar').checked)
			{
				// if(document.InscripcionTGForm.fechaValidacion.value!='')
					// fechaActual = document.InscripcionTGForm.fechaValidacion.value;
				// else
					fechaActual = getFechaActualDDMMYYYY();
				// if(document.getElementById('asterisco'))
					// document.getElementById('asterisco').innerText = '(*)';
				document.getElementById('fechaCheck').value = fechaActual;
				document.getElementById('validar').checked = false;
				document.getElementById("calendarioTd").style.visibility="visible";
				
				if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
					document.getElementById('observacionesValidacion').style.display = "block";
			}
					
			}
			else
			{ 
				// if(document.getElementById('asterisco'))
					// document.getElementById('asterisco').innerText = '';
				document.getElementById('fechaCheck').value = "";
				document.getElementById("calendarioTd").style.visibility="hidden";
				if(document.InscripcionTGForm.modo.value=='vbgComprobarValidar'||document.InscripcionTGForm.modo.value=='vbtComprobarValidar'){
					document.getElementById('observacionesValidacion').style.display = "block";
				}
				
			}
		}
			
	}
		
		function accionCancelar() 
		{		
			window.close();
		}
		function accionSiguiente() 
		{	
			sub();
			if(document.getElementById('validar')){
				if(document.getElementById('validar').checked)
				{
					document.InscripcionTGForm.fechaValidacion.value = document.getElementById('fechaCheck').value;
					// La fecha de validacion no puede ser nula
					if(document.InscripcionTGForm.fechaValidacion.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
						document.getElementById('fechaCheck').focus();
						return false;
					}
					//La fecha de validacion no puede ser inferior a la fecha de baja de la inscripcion anterior	
					var fechaBajaTurnoAnterior = document.InscripcionTGForm.fechaBajaTurno.value;
					if(fechaBajaTurnoAnterior!=''){
						fechaBajaTurnoAnterior = fechaBajaTurnoAnterior.substring(8,10)+"/"+fechaBajaTurnoAnterior.substring(5,7)+"/"+fechaBajaTurnoAnterior.substring(0,4);
						var fechaValidacion = document.InscripcionTGForm.fechaValidacion.value;
						var comparaFecha = compararFecha(fechaBajaTurnoAnterior,fechaValidacion);
						// return 0; 	// F1 = F2
						// return 1; // F1 > F2 
						// return 2; // F1 < F2 
						if (comparaFecha==1) {
							fin();
							error = '<siga:Idioma key="gratuita.gestionInscripciones.error.valida.menor.baja.ia"/>';
							error += ' '+fechaBajaTurnoAnterior;
							alert(error);
							return false;
						}
					}	
					
				
				}
			}
			document.InscripcionTGForm.submit();
		}
		
	function accionFinalizar() 
		{		
			sub();
			
			// no es obligatoria la fecha en los siguiente casos
			//sigInsertar -->insercion de guardia
			//sbgComprobarInsertar --> insercion de la baja de guaadia
			//sbtComprobarInsertar --> insercion de la baja de turno
			if(document.InscripcionTGForm.modo.value!='sigInsertar'&&document.InscripcionTGForm.modo.value!='sbgComprobarInsertar'&&document.InscripcionTGForm.modo.value!='sbtComprobarInsertar' && document.getElementById('validar').checked==false && document.getElementById('denegar').checked==false){
					fin();
					alert("<siga:Idioma key="gratuita.altaTurno.literal.alert1"/>");
					return false;
			}
			//En el caso de que no sea necesario la validacion no se hace nada mas
			if(document.getElementById('validar')){
				// Validamos que exista fecha de validacion y ciones de validacion
				
				//cuando se quiere validar habra que diferenciar el alta y la baja
				//Caso inscripcion
				if(document.InscripcionTGForm.modo.value=='sigInsertar'
					||document.InscripcionTGForm.modo.value=='vitValidar'
					||document.InscripcionTGForm.modo.value=='vigValidar'
					||document.InscripcionTGForm.modo.value=='vmitValidar'
					||document.InscripcionTGForm.modo.value=='vmigValidar'){
					
				if(document.getElementById('validar').checked)
				{
					
					document.InscripcionTGForm.fechaValidacion.value = document.getElementById('fechaCheck').value;
					// La fecha de validacion no puede ser nula
					if(document.InscripcionTGForm.fechaValidacion.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
						document.getElementById('fechaCheck').focus();
						return false;
					}
					//Las observaciones no `pueden ser nulas
					/*if(document.InscripcionTGForm.observacionesValidacion){
						if(document.InscripcionTGForm.observacionesValidacion.value == "")
						{
							fin();
							alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
							document.InscripcionTGForm.observacionesValidacion.focus();
							return false;
						}
					}*/
					
					
					//ATENCION: Existen validaciones adicionales en servidor
					//	CASO TURNO: La fecha de validacion no puede ser inferior a la fecha de baja de la inscripcion anterior
					//	CASO GUARDIA: La fecha de validacion de guardia no puede ser inferior a la fecha de validacion de la inscripcion al turno activo
					
					
					document.InscripcionTGForm.fechaBaja.value = "";
					
					
				}else if(document.getElementById('denegar').checked){
				//denegacion de alta
					if(document.InscripcionTGForm.observacionesValidacion){
						if(document.InscripcionTGForm.observacionesValidacion.value == "")
						{
							fin();
							alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
							document.InscripcionTGForm.observacionesValidacion.focus();
							return false;
						}
					}
					document.InscripcionTGForm.fechaDenegacion.value = document.getElementById('fechaCheck').value;
					document.InscripcionTGForm.observacionesDenegacion.value = document.InscripcionTGForm.observacionesValidacion.value;
				
				}
					
					
					
					
			}else{
			// Este es el caso de las bajas de inscripcion
			// sbtComprobarInsertar
  			// sbgComprobarInsertar
			// vmbtComprobarValidar
			// vbgComprobarValidar
			// vmbgComprobarValidar
			
			
				if(document.getElementById('validar').checked)
				{
					document.InscripcionTGForm.fechaBaja.value = document.getElementById('fechaCheck').value;
					
					if(document.InscripcionTGForm.fechaBaja.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
						document.getElementById('fechaCheck').focus();
						return false;
					}
					if(document.InscripcionTGForm.fechaValidacionTurno.value!=''){
						var fechaValidacion = document.InscripcionTGForm.fechaValidacionTurno.value;
						fechaValidacion = fechaValidacion.substring(8,10)+"/"+fechaValidacion.substring(5,7)+"/"+fechaValidacion.substring(0,4);
						var fechaBaja = document.InscripcionTGForm.fechaBaja.value;
						var comparaFecha = compararFecha(fechaValidacion,fechaBaja);
						// return 0; 	// F1 = F2
						// return 1; // F1 > F2 
						// return 2; // F1 < F2 
						if (comparaFecha==1) {
							fin();
							if(document.InscripcionTGForm.idGuardia.value!=''){
								error = "<siga:Idioma key='gratuita.gestionInscripciones.error.bajaguardia.menor.validaturno'/>";
							}else{
								error = "<siga:Idioma key='gratuita.gestionInscripciones.error.baja.menor.valida'/>";
							}
							error += ' '+fechaValidacion;
							
							// alert("La fecha de baja no puede ser inferior a la fecha de solicitud ni de validacion: "+fechaValidacion);
							alert(error);
							return false;
						}
					}
					//validaciones de guardia
					if(document.InscripcionTGForm.idGuardia.value!=''){
						var fechaValidacion = document.InscripcionTGForm.fechaValidacion.value;
						fechaValidacion = fechaValidacion.substring(8,10)+"/"+fechaValidacion.substring(5,7)+"/"+fechaValidacion.substring(0,4);
						var fechaBaja = document.InscripcionTGForm.fechaBaja.value;
						var comparaFecha = compararFecha(fechaValidacion,fechaBaja);
						// return 0; 	// F1 = F2
						// return 1; // F1 > F2 
						// return 2; // F1 < F2 
						if (comparaFecha==1) {
							fin();
							error = "<siga:Idioma key='gratuita.gestionInscripciones.error.baja.menor.valida'/>";
							error += ' '+fechaValidacion;
							
							// alert("La fecha de baja no puede ser inferior a la fecha de solicitud ni de validacion: "+fechaValidacion);
							alert(error);
							return false;
						}
					
					
					}
					
					document.InscripcionTGForm.observacionesValBaja.value = document.InscripcionTGForm.observacionesValidacion.value;
					
					
					
				}else if(document.getElementById('denegar').checked){
					if(document.InscripcionTGForm.observacionesValidacion){
						if(document.InscripcionTGForm.observacionesValidacion.value == "")
						{
							fin();
							alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
							document.InscripcionTGForm.observacionesValidacion.focus();
							return false;
						}
						document.InscripcionTGForm.observacionesDenegacion.value = document.InscripcionTGForm.observacionesValidacion.value;
					}
					document.InscripcionTGForm.fechaDenegacion.value = document.getElementById('fechaCheck').value;
				
				}
			}
				
		}
		document.InscripcionTGForm.target="submitArea";
		document.InscripcionTGForm.submit();
		window.returnValue="MODIFICADO";			
	}
	
	
	
	
	function refrescarLocal(){
		
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
		<c:if test="${InscripcionTGForm.modo=='sbtComprobarInsertar'}">
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
	<html:hidden property="fechaSolicitud" />
	<html:hidden property="fechaBaja" />
	<html:hidden property="fechaSolicitudBaja" />
	<html:hidden property="fechaValidacion" />
	<html:hidden property="idGuardia" />
	<html:hidden property="modo" />
	<html:hidden property="validarInscripciones" />
	<html:hidden property="fechaBajaTurno" />
	<html:hidden property="fechaValidacionTurno" />
	<html:hidden property="fechaDenegacion" />
	<html:hidden property="observacionesDenegacion" />
	<html:hidden property="observacionesValBaja" />
	
	
	
	
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
						<td class="labelText" style="align:right"><siga:Idioma
							key='gratuita.altaTurnos.literal.retencion' /></td>
		
						<td align="left"><logic:notEmpty name="InscripcionTGForm"
							property="retenciones">
							<html:select property="idRetencion" styleClass="boxCombo">
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
			
			<td class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.osolicitud"/>
			</td>
			<td colspan="3"><html:textarea name="InscripcionTGForm" property="observacionesSolicitud"  onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="false"></html:textarea>
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
			<td class="labelText"><siga:Idioma
				key="gratuita.altaTurnos.literal.fsolicitud" /></td>
			<td class="labelText">
				<input type="text" name="fechaSol" class="boxConsulta" readOnly="true"/>
			</td>
			<td class="labelText">&nbsp;</td>
			<td class="labelText">&nbsp;</td>

		</tr>
		<tr>
			
			<td class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.mbaja"/>
			</td>
			<td colspan="3"> 
				<html:textarea name="InscripcionTGForm" property="observacionesBaja"  onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="false"></html:textarea>
			</td>
		</tr>
	 </table>
	 
	</siga:ConjCampos>	

</c:if>

<c:if test="${InscripcionTGForm.usrBean.letrado==false}">
	<c:if test="${(InscripcionTGForm.validarInscripciones=='S'&&(InscripcionTGForm.validacionAlta==true||InscripcionTGForm.validacionBaja==true))||InscripcionTGForm.masivo==true}">
		<c:if test="${InscripcionTGForm.solicitudAlta==false && InscripcionTGForm.validacionAlta==true&& InscripcionTGForm.masivo==false}">
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
					<td class="labelText"><c:out value="${InscripcionTGForm.fechaSolicitudJsp}">
					</c:out> 
					
					</td>
		
						<td class="labelText">&nbsp;</td>
						<td class="labelText">&nbsp;</td>
		
				</tr>
				<tr>
					
					<td class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.osolicitud"/>
					</td>
					<td colspan="3"> 
						<html:textarea name="InscripcionTGForm" property="observacionesSolicitud"  onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" disabled="true"></html:textarea>
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
					<td class="labelText"><siga:Idioma
						key="gratuita.altaTurnos.literal.fsolicitud" /></td>
					<td class="labelText"><c:out value="${InscripcionTGForm.fechaSolicitudBajaJsp}"></c:out>
					</td>
					<td class="labelText">&nbsp;</td>
					<td class="labelText">&nbsp;</td>
		
				</tr>
				<tr>
					
					<td class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.mbaja"/>
					</td>
					<td colspan="3"> 
						<html:textarea name="InscripcionTGForm" property="observacionesBaja"  onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" disabled="true"></html:textarea>
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
					<td class="labelText"><siga:Idioma
						key="gratuita.altaTurnos.literal.fvalidacion" /></td>
					<td><input type="text" id="fechaCheck" size="10"
						maxlength="10" class="box" readOnly="true" value="">&nbsp;&nbsp;<a
						id="calendarioTd" style="visibility: hidden"
						onClick="showCalendarGeneral(fechaCheck);"
						onMouseOut="MM_swapImgRestore();"
						onMouseOver="MM_swapImage('Calendario','','<html:rewrite page='/html/imagenes/calendar.gif'/>',1);"><img
						src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
						alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
						border="0"></a></td>
					<td colspan="2" align="left">
					
						<table>
							
							<tr>
								<td class="labelText"><siga:Idioma
									key="gratuita.altaTurnos.literal.validacion" /></td>
					
								<c:choose>
									<c:when test="${InscripcionTGForm.modo=='vigValidar'||InscripcionTGForm.modo=='vitValidar'||InscripcionTGForm.modo=='vbgComprobarValidar'||InscripcionTGForm.modo=='vbtComprobarValidar'||InscripcionTGForm.modo=='vmitValidar'||InscripcionTGForm.modo=='vmigValidar'||InscripcionTGForm.modo=='vmbtComprobarValidar'||InscripcionTGForm.modo=='vmbgComprobarValidar'}">
										<td><input type="checkbox" name="validar" value="no" onClick="obtenerFecha('validar')">
										</td>
										<td class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.denegar" />
										</td>
										<td><input type="checkbox" name="denegar" value="no" onClick="obtenerFecha('denegar');document.getElementById('calendarioTd').style.visibility='hidden';">
										</td>
									</c:when>
									
									
									<c:when test="${InscripcionTGForm.modo=='sbgComprobarInsertar'||InscripcionTGForm.modo=='sbtComprobarInsertar'}">
										<td>
											<input type="checkbox" name="validar" value="no" onClick="obtenerFecha('validar');">
										</td>
										<td>&nbsp;
										</td>
										<td>
											<input type="checkbox" name="denegar" value="no" style="visibility: hidden" >
										</td>
									</c:when>
									
				
									<c:otherwise>
										<td>
											<input type="checkbox" name="validar" value="no" onClick="obtenerFecha('validar')">
										</td>
										<td>&nbsp;</td>
										<td>
										<input type="checkbox" name="denegar" value="no" style="visibility: hidden" >
										</td>
									</c:otherwise>
								</c:choose>
								</tr>
								</table>
					</td>
	
				</tr>
				<c:if test="${InscripcionTGForm.modo=='vigValidar'||InscripcionTGForm.modo=='vitValidar'||InscripcionTGForm.modo=='vbgComprobarValidar'||InscripcionTGForm.modo=='vbtComprobarValidar'||
					InscripcionTGForm.modo=='vmitValidar'||InscripcionTGForm.modo=='vmigValidar'||
					InscripcionTGForm.modo=='sitEditarTelefonosGuardia'||InscripcionTGForm.modo=='sigInsertar'||InscripcionTGForm.modo=='smitEditarTelefonosGuardia'||InscripcionTGForm.modo=='sbgComprobarInsertar'||InscripcionTGForm.modo=='sbtComprobarInsertar'||InscripcionTGForm.modo=='vmbtComprobarValidar'||InscripcionTGForm.modo=='vmbgComprobarValidar'  }">
						<tr id="observacionesValidacion">
							<td class="labelText"><siga:Idioma
								key="gratuita.altaTurnos.literal.ovalidacion" />
								</td>
							<td colspan="3"><html:textarea  name="InscripcionTGForm" property="observacionesValidacion"  onChange="cuenta(this,1024)" cols="65" rows="2" onkeydown="cuenta(this,1024);" styleClass="box" style="overflow:auto;width=400;height=80" readOnly="false"></html:textarea>
							</td>
							
						</tr>
				</c:if>
				
				
			</table>
		
		</siga:ConjCampos>
	
	
	</c:if>
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

		
</script>
</body>

</html>
