<!-- gestionInscripcionesTG.jsp -->

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

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>

<%
	HttpSession ses=request.getSession();
	Integer alturaDatosTabla = 256;
%>

<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


  		<!-- defaults for Autocomplete and displaytag -->
  		<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  		<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<siga:Titulo titulo="gratuita.gestionInscripciones.menu"	localizacion="gratuita.gestionInscripciones.localizacion.literal" />

		<script type="text/javascript">
	
	
			function actualizarResultados(){	
			}
	
			function limpiarColegiado(){
				document.InscripcionTGForm.idPersona.value = '';
				document.InscripcionTGForm.colegiadoNumero.value = '';
				document.InscripcionTGForm.colegiadoNombre.value = '';
				actualizarResultados();
			}
			
			function buscarColegiado(){
				var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
				if (resultado!=undefined && resultado[0]!=undefined ){
					
					document.InscripcionTGForm.idPersona.value       = resultado[0];
					document.InscripcionTGForm.colegiadoNumero.value    = resultado[2];
					document.InscripcionTGForm.colegiadoNombre.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
				}
			}
			
			var isRefrescar = false;
			function refrescarLocal(){
				isRefrescar = true;
				document.getElementById('idBusqueda').onclick();			
			}
	
	
			function preAccionBusqueda(){	
				document.getElementById("tipoBusqueda").value =  document.InscripcionTGForm.tipo.value;
				if(document.InscripcionTGForm.clase[0].checked) 
					document.getElementById("claseBusqueda").value = 'T';
				else
					document.getElementById("claseBusqueda").value = 'G';
				
				document.getElementById("estadoBusqueda").value = document.InscripcionTGForm.estado.value;
				if(!isRefrescar){
					if(document.InscripcionTGForm.idPersona.value==''){
						if(!confirm('<siga:Idioma key="messages.general.aviso.sinletradoseleccionado"/>')) {
								return 'cancel';
						 }
						
					}
				}
				sub();
				isRefrescar = false;
			}
			
			function postAccionBusqueda(){
				// validarAnchoTabla();
				fin();
				// cambiarCabeceraTabla();
		
				var estadoBusqueda = document.getElementById("estadoBusqueda").value;
				if(estadoBusqueda=='S'){
					jQuery("#bAccion").attr("disabled","disabled");
					document.getElementById("bAccion").style.display ='none';
					ocultaBotonesFila();
					
				}else if(estadoBusqueda=='P'){
					document.getElementById("bAccion").style.display ='block';
					jQuery("#bAccion").removeAttr("disabled");
					document.getElementById("bAccion").value = '<siga:Idioma key="general.boton.validarSolicitud"/>';
					document.getElementById("bAccion").alt.value = '<siga:Idioma key="general.boton.validarSolicitud"/>';
					
				}
				else if(estadoBusqueda=='C'){
					document.getElementById("bAccion").style.display ='block';
					jQuery("#bAccion").removeAttr("disabled");
					document.getElementById("bAccion").value = '<siga:Idioma key="general.boton.cambiarFechaEfectiva"/>';
					document.getElementById("bAccion").alt.value = '<siga:Idioma key="general.boton.cambiarFechaEfectiva"/>';
				
				}else if(estadoBusqueda=='D'){
					document.getElementById("bAccion").style.display ='none';
					ocultaBotonesFila();
					jQuery("#bAccion").attr("disabled","disabled");					
				
				}
			}
	
			function ocultaBotonesFila(){
				
				//aalg: INC_10633_SIGA
				jQuery("#tabInscripcionesCabeceras").find('img').hide();
			}
			
			function preAccionColegiado(){
				sub();
			}

			function postAccionColegiado(){
				fin();		
			}
	
			function calcularAltura() {		
				var altura = document.getElementById("divInscripcionesDatos").offsetParent.offsetHeight;
				document.getElementById("divInscripcionesDatos").style.height=altura-<%=alturaDatosTabla%>;
				
				validarAnchoTabla();
			}				
	
			function validarAnchoTabla() {
				if (document.getElementById("inscripciones").clientHeight < document.getElementById("divInscripcionesDatos").clientHeight) {
					document.getElementById("tabInscripcionesCabeceras").width='100%';
				}
				else {
					document.getElementById("tabInscripcionesCabeceras").width='98.30%';
				}
			}	
	
			function validar(fila) {				
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var idTurno = 'idTurno_' + fila;
			   	var fechaSolicitud	= 'fechaSolicitud_' + fila ;
			   	var observacionesSolicitud	= 'observacionesSolicitud_' + fila ;
			   	var idGuardia = 'idGuardia_' + fila;
			   	
			   	document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
			   	document.FormAValidar.idPersona.value = document.getElementById(idPersona).value;
			   	document.FormAValidar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAValidar.fechaSolicitud.value = document.getElementById(fechaSolicitud).value;
			   	document.FormAValidar.observacionesSolicitud.value = document.getElementById(observacionesSolicitud).value;
			   	
			   	if(document.getElementById(idGuardia)){
			   		document.FormAValidar.idGuardia.value = document.getElementById(idGuardia).value;
			   		// document.FormAValidar.modo.value = "guardiaInscripcion";
			   		document.FormAValidar.modo.value = "vigConsultaTurno";
			   		
			   	}else{
				   	// document.FormAValidar.modo.value = "turnoInscripcion";
				   	document.FormAValidar.modo.value = "vitConsultaTurno";
			   	}
			   				   	
			   	var resultado = ventaModalGeneral(document.FormAValidar.name,"G");
			    if(resultado == "MODIFICADO") 
			    	refrescarLocal();
			 }
	 
			function validarBaja(fila) {
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var idTurno = 'idTurno_' + fila;
			   	var fechaSolicitud	= 'fechaSolicitud_' + fila ;
			   	var fechaSolicitudBaja	= 'fechaSolicitudBaja_' + fila ;
			   	var observacionesBaja	= 'observacionesBaja_' + fila ;
			   	var idGuardia = 'idGuardia_' + fila;
			   	var fechaValidacion	= 'fechaValidacion_' + fila ;
			   	
			   	document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
			   	document.FormAValidar.idPersona.value = document.getElementById(idPersona).value;
			   	document.FormAValidar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAValidar.fechaSolicitud.value = document.getElementById(fechaSolicitud).value;
				document.FormAValidar.fechaSolicitudBaja.value = document.getElementById(fechaSolicitudBaja).value;
				document.FormAValidar.observacionesBaja.value = document.getElementById(observacionesBaja).value;
				document.FormAValidar.fechaValidacion.value = document.getElementById(fechaValidacion).value;
				if(document.getElementById(idGuardia)){
			   		document.FormAValidar.idGuardia.value = document.getElementById(idGuardia).value;
			   		document.FormAValidar.modo.value = "vbgConsultaTurno";
			   		//guardiaInscripcionBaja
			   	}else{
				   	document.FormAValidar.modo.value = "vbtConsultaTurno";
				   	//turnoInscripcionBaja
			   	}			   	
			
				var resultado = ventaModalGeneral(document.FormAValidar.name,"G");
			   	if(resultado == "MODIFICADO") 
			   		refrescarLocal();
			}
			
			function cambiarFechaEfectivaValidacion(fila) {				
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var idTurno = 'idTurno_' + fila;
			   	var fechaSolicitud	= 'fechaSolicitud_' + fila ;
			   	var fechaValidacion = 'fechaValidacion_' + fila ;
			   	var observacionesValidacion = 'observacionesValidacion_' + fila ;
			   	var validarInscripciones = 'validarInscripciones_' + fila ;
			   	var tipoGuardias = 'tipoGuardias_' + fila ;
			   	var idGuardia = 'idGuardia_' + fila;
			   	
			   	document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
			   	document.FormAValidar.idPersona.value = document.getElementById(idPersona).value;
			   	document.FormAValidar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAValidar.fechaSolicitud.value = document.getElementById(fechaSolicitud).value;
			   	if(document.getElementById(idGuardia))
			   		document.FormAValidar.idGuardia.value = document.getElementById(idGuardia).value;
			   	else
			   		document.FormAValidar.idGuardia.value = "";
			   	
			   	document.FormAValidar.validarInscripciones.value = document.getElementById(validarInscripciones).value;
			   	document.FormAValidar.tipoGuardias.value = document.getElementById(tipoGuardias).value;
			   	
			   	document.FormAValidar.fechaValidacion.value = document.getElementById(fechaValidacion).value;
			    document.FormAValidar.observacionesValidacion.value = document.getElementById(observacionesValidacion).value;
			   	
			   	document.FormAValidar.modo.value = "editarFechaValidacion";
		
				// Abro ventana modal y refresco si necesario
				var resultado = ventaModalGeneral(document.FormAValidar.name,"P");
				if (resultado=='MODIFICADO') {
					  	refrescarLocal();
				}		
			}
			
			function cambiarFechaEfectivaBaja(fila) {
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var idTurno = 'idTurno_' + fila;
			   	var fechaSolicitud	= 'fechaSolicitud_' + fila ;
			   	var fechaSolicitudBaja	= 'fechaSolicitudBaja_' + fila ;
			   	var fechaBaja	= 'fechaBaja_' + fila ;
			   	var fechaValidacion = 'fechaValidacion_' + fila ;
			   	
			   	var oValbaja = 'observacionesValBaja_' + fila;
			   	var idGuardia = 'idGuardia_' + fila;
			   	
			   	var validarInscripciones = 'validarInscripciones_' + fila ;
			   	var tipoGuardias = 'tipoGuardias_' + fila ;
			   	
			   	document.FormAValidar.idInstitucion.value = document.getElementById(idInstitucion).value;
			   	document.FormAValidar.idPersona.value = document.getElementById(idPersona).value;
			   	document.FormAValidar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAValidar.fechaSolicitud.value = document.getElementById(fechaSolicitud).value;
			   	document.FormAValidar.fechaSolicitudBaja.value = document.getElementById(fechaSolicitudBaja).value;
			   	document.FormAValidar.fechaBaja.value = document.getElementById(fechaBaja).value;
			   	document.FormAValidar.observacionesValBaja.value 		= document.getElementById(oValbaja).value;
			  	document.FormAValidar.fechaValidacion.value = document.getElementById(fechaValidacion).value;
			   	
			   	if(document.getElementById(idGuardia))
			   		document.FormAValidar.idGuardia.value = document.getElementById(idGuardia).value;
			   	else
			   		document.FormAValidar.idGuardia.value = "";
			   	
			   	document.FormAValidar.validarInscripciones.value = document.getElementById(validarInscripciones).value;
			   	document.FormAValidar.tipoGuardias.value = document.getElementById(tipoGuardias).value;
			   	
				document.FormAValidar.modo.value = "editarFechaBaja";
		
				var resultado = ventaModalGeneral(document.FormAValidar.name,"P");
				if (resultado=='MODIFICADO') {
					refrescarLocal(true);
				}
			}
	
			function cambiarCabeceraTabla() {
				if(document.InscripcionTGForm.tipo.value=='A'){
					document.getElementById('columnaFechaSolicitud').innerHTML="Fecha Sol";
					document.getElementById('columnaFechaValidacion').innerHTML="Fecha Val";
				
				}else{
					document.getElementById('columnaFechaSolicitud').innerHTML="Fecha Sol Baja";
					document.getElementById('columnaFechaValidacion').innerHTML="Fecha Baja";
				}
				if(document.InscripcionTGForm.clase.value=='T'){
					document.getElementById('columnaGuardia').innerHTML="?¿?";
				}else{
					document.getElementById('columnaGuardia').innerHTML='<siga:Idioma key="gratuita.listarTurnos.literal.guardias"/>';
				}
			}
	
			function marcarDesmarcarTodos(o) { 
				var ele = document.getElementsByName("chkInscripcion");
				for (i = 0; i < ele.length; i++) {
					if(!ele[i].disabled)
						ele[i].checked = o.checked;
				}
		   }
			
		   function accionValidarSolicitudes() {
				var ele = document.getElementsByName('chkInscripcion');
				
				var datos='';
				var estadoFinal="";
				for (fila = 0; fila < ele.length; fila++) {
				    if(ele[fila].checked){
						var idInstitucion = 'idInstitucion_' + fila ;
					   	var idPersona = 'idPersona_' + fila ;
					   	var idTurno = 'idTurno_' + fila;
					   	var idGuardia = 'idGuardia_' + fila;
					   	var fechaSolicitud	= 'fechaSolicitud_' + fila ;
					   	var validarInscripciones = 'validarInscripciones_' + fila ;
					   	var tipoGuardias = 'tipoGuardias_' + fila ;
						var fechaValidacion	= 'fechaValidacion_' + fila ;			   	
						var fechaSolicitudBaja	= 'fechaSolicitudBaja_' + fila ;
						var fechabaja = 'fechaBaja_' + fila;
						
						var estadoActual = document.getElementById('estado_' + fila).value;
						
						if (estadoFinal == "") {
							estadoFinal=estadoActual;
						} else {
							if (estadoFinal != estadoActual) {
								alert('<siga:Idioma key="gratuita.gestionInscripciones.error.diferentesestados"/>');
								return false;
							}
						}
											
					   	
					   	idInstitucion = document.getElementById(idInstitucion).value;
					   	
					   	idPersona = document.getElementById(idPersona).value;
					   	
					   	idTurno = document.getElementById(idTurno).value;
					   	
					   	if(document.getElementById(idGuardia)&&document.getElementById(idGuardia).value!='')
					   		idGuardia = document.getElementById(idGuardia).value;
					   	else
					   		idGuardia = "-";					   	
					   	
					   	fechaSolicitud = document.getElementById(fechaSolicitud).value;
					   	
					   	validarInscripciones = document.getElementById(validarInscripciones).value;
					   	
					   	tipoGuardias = document.getElementById(tipoGuardias).value;
					   		
						if(document.getElementById(fechaValidacion) && document.getElementById(fechaValidacion).value!='')
					   		fechaValidacion = document.getElementById(fechaValidacion).value;
					   	else
					   		fechaValidacion = "-";
					   	
					   	if(document.getElementById(fechaSolicitudBaja) && document.getElementById(fechaSolicitudBaja).value!='')
					   		fechaSolicitudBaja = document.getElementById(fechaSolicitudBaja).value;
					   	else
					   		fechaSolicitudBaja = "-";
					   	
					   	if(document.getElementById(fechabaja) && document.getElementById(fechabaja).value!='')
					   		fechabaja = document.getElementById(fechabaja).value;
					   	else
					   		fechabaja = "-";		    
				    
						datos = datos +","+ idInstitucion+"##"+
						  idPersona+"##"+
						  idTurno+"##"+
						  idGuardia+"##"+
						  fechaSolicitud+"##"+				  
						  validarInscripciones+"##"+
						  tipoGuardias+"##"+
						  fechaValidacion+"##"+
						  fechaSolicitudBaja+"##"+
						  fechabaja+"##"; 			
					}			
				}
		
				if(datos==''){
					alert('<siga:Idioma key="general.message.seleccionar"/>');
					return false;
				}
				datos=datos.substring(1);
				document.FormAValidar.turnosSel.value=datos;
				var tipoBusqueda = document.getElementById("tipoBusqueda").value;
				var claseBusqueda = document.getElementById("claseBusqueda").value;
				var estadoBusqueda = document.getElementById("estadoBusqueda").value;
				
				document.FormAValidar.tipo.value=tipoBusqueda;
				document.FormAValidar.clase.value=claseBusqueda;
				document.FormAValidar.estado.value=estadoBusqueda;
				document.FormAValidar.modo.value="solicitudesMasivas";
				var tamanioVentana ='G';
				if(estadoBusqueda=='C'){
					tamanioVentana = 'P';
				}
				
				var resultado = ventaModalGeneral(document.FormAValidar.name,tamanioVentana);
				if (resultado=='MODIFICADO') {
					refrescarLocal();
				}				
			}
   
			function consultaInscripcion(fila) {
			    var idInstitucion = 'idInstitucion_' + fila ;
			    var idPersona = 'idPersona_' + fila ;
				var idTurno = 'idTurno_' + fila ;
				var fsoli = 'fechaSolicitud_' + fila;
				var osoli = 'observacionesSolicitud_' + fila;
				var fvali = 'fechaValidacion_' + fila;
				var ovali = 'observacionesValidacion_' + fila;
				var fsolbaja = 'fechaSolicitudBaja_' + fila;
				var obaja = 'observacionesBaja_' + fila;
				var fbaja = 'fechaBaja_' + fila;
				var oValbaja = 'observacionesValBaja_' + fila;
				var oDenegacion = 'observacionesDenegacion_' + fila;
				var fDenegacion = 'fechaDenegacion_' + fila;
				document.FormAConsultar.idInstitucion.value = document.getElementById(idInstitucion).value;
				document.FormAConsultar.idPersona.value = document.getElementById(idPersona).value;
				document.FormAConsultar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAConsultar.fechaSolicitud.value = document.getElementById(fsoli).value;
			   	document.FormAConsultar.observacionesSolicitud.value 	= document.getElementById(osoli).value;
			    document.FormAConsultar.fechaValidacion.value 			= document.getElementById(fvali).value;
				document.FormAConsultar.fechaBaja.value 			= document.getElementById(fbaja).value;
				document.FormAConsultar.observacionesValidacion.value 	= document.getElementById(ovali).value;
				document.FormAConsultar.fechaSolicitudBaja.value 		= document.getElementById(fsolbaja).value;
				document.FormAConsultar.observacionesBaja.value 		= document.getElementById(obaja).value;
				document.FormAConsultar.observacionesValBaja.value 		= document.getElementById(oValbaja).value;
				document.FormAConsultar.observacionesDenegacion.value 		= document.getElementById(oDenegacion).value;
			   	document.FormAConsultar.fechaDenegacion.value 		= document.getElementById(fDenegacion).value;
			   	document.FormAConsultar.modo.value = "consultaInscripcion";
			   	document.FormAConsultar.target.value = "consultaInscripcion";
			   	var resultado = ventaModalGeneral(document.FormAConsultar.name,"G");																										 
			}
		</script>
	</head>

	<body>
		<input type="hidden" name="tipoBusqueda">
		<input type="hidden" name="claseBusqueda">
		<input type="hidden" name="estadoBusqueda">
		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>

		<html:form action="${path}"  method="POST" target="mainWorkArea">
			<html:hidden property="modo"/>
			<html:hidden property="idPersona"/>
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
							<siga:Idioma key="gratuita.gestionInscripciones.tipo.literal"/>
						</td>
						<td>
							<html:select property="tipo" styleClass="boxCombo" >
								<html:option value="A"><siga:Idioma key="gratuita.gestionInscripciones.tipo.alta"/></html:option>
								<html:option value="B"><siga:Idioma key="gratuita.gestionInscripciones.tipo.baja"/></html:option>							
							</html:select>
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.estado"/>
						</td>
						<td>
							<html:select property="estado" styleClass="boxCombo">						
								<html:option value="S"><siga:Idioma key="general.combo.seleccionar"/></html:option>
								<html:option value="P"><siga:Idioma key="gratuita.gestionInscripciones.estado.pendiente"/></html:option>
								<html:option value="C"><siga:Idioma key="gratuita.gestionInscripciones.estado.confirmada"/></html:option>
								<html:option value="D"><siga:Idioma key="gratuita.gestionInscripciones.estado.denegada"/></html:option>
							</html:select>
						</td>
												
						<td class="boxConsulta">
							<html:radio property="clase" value="T"/>
							<siga:Idioma key="gratuita.gestionInscripciones.turno.literal"/>
						</td>
							
						<td class="boxConsulta">
							<html:radio property="clase" value="G"/>
							<siga:Idioma key="gratuita.gestionInscripciones.guardia.literal"/>
						</td>
					</tr>
			
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.turno.literal" />
						</td>
						<td colspan = "3">
							<html:select styleId="turnosInscripcion" styleClass="boxCombo" style="width:320px;" property="idTurno">
								<bean:define id="turnosInscripcion" name="InscripcionTGForm" property="turnosInscripcion" type="java.util.Collection" />
								<html:optionsCollection name="turnosInscripcion" value="idTurno" label="nombre" />
							</html:select>
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.guardia.literal" />
						</td>
						<td>
							<html:select styleId="guardiasInscripcion" styleClass="boxCombo" style="width:320px;" property="idGuardia" >
								<bean:define id="guardiasInscripcion" name="InscripcionTGForm" property="guardiasInscripcion" type="java.util.Collection" />
								<html:optionsCollection name="guardiasInscripcion" value="idGuardia" label="nombre" />
							</html:select>
						</td>
					</tr>
			
					<tr>				
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.fechaDesde.literal"/>
						</td>
						<td colspan = "3">
							<siga:Fecha nombreCampo="fechaDesde"  />							
						</td>
		
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.fechaHasta.literal"/>
						</td>
						<td>
							<siga:Fecha nombreCampo="fechaHasta" campoCargarFechaDesde="fechaDesde" />
						</td>					
					</tr>
			
					<tr>
						<td colspan="6">
							<siga:ConjCampos leyenda="gratuita.gestionInscripciones.letrado.literal">
								<table align="left">
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.gestionInscripciones.colegiado.literal" /> (*)
										</td>
										<td>
											<html:text styleId="colegiadoNumero" property="colegiadoNumero" size="4" maxlength="9" styleClass="box" />
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
	
			<table class="botonesSeguido" align="center">
				<tr>
					<td class="titulitos">
					</td>
					<td class="tdBotones">
						<input type='button'  id = 'idBusqueda' name='idButton' value='Buscar' alt='Buscar' class='button' />
					</td>
				</tr>
			</table>

			<ajax:select
				baseUrl="/SIGA${path}.do?modo=getAjaxGuardias"
				source="turnosInscripcion" target="guardiasInscripcion" parameters="idTurno={idTurno}"
				/>
				
			<ajax:updateFieldFromField 
				baseUrl="/SIGA${path}.do?modo=getAjaxColegiado"
			    source="colegiadoNumero" target="idPersona,colegiadoNumero,colegiadoNombre"
				parameters="colegiadoNumero={colegiadoNumero}" 
				preFunction="preAccionColegiado"
				postFunction="postAccionColegiado"
				/>

			<div id="divInscripciones"></div>	

			<table class="botonesDetalle" align="center">
				<tr>
					<td style="width: 900px;">
						&nbsp;
					</td>
					<td class="tdBotones">
						<input type="button" alt="<siga:Idioma key='general.boton.validarSolicitud'/>"	id="bAccion" onclick="return accionValidarSolicitudes();" class="button" name="idButton" value="<siga:Idioma key='general.boton.validarSolicitud'/>" />
					</td>
				</tr>
			</table>
		</html:form>

		<ajax:htmlContent
			baseUrl="/SIGA${path}.do?modo=getAjaxBusqueda"
			source="idBusqueda"
			target="divInscripciones"
			preFunction="preAccionBusqueda"
			postFunction="postAccionBusqueda"
			parameters="idTurno={idTurno},idGuardia={idGuardia},idPersona={idPersona},fechaDesde={fechaDesde},fechaHasta={fechaHasta},tipo={tipo},clase={clase},estado={estado}}"/>

		<!-- FIN: CAMPOS DE BUSQUEDA-->
		<!-- Formularios auxiliares -->
		<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="" style="display:none">
			<input type="hidden" name="actionModal" value="" />
			<input type="hidden" name="modo" value="abrirBusquedaModal" />
		</html:form>
		
		<html:form action="${path}"  name="FormAValidar" type ="com.siga.gratuita.form.InscripcionTGForm">
			<html:hidden property="modo"/>
			<html:hidden property="idInstitucion" />
			<html:hidden property="idPersona" />
			<html:hidden property="idTurno" />
			<html:hidden property="idGuardia" />
			<html:hidden property="fechaSolicitud" />
			<html:hidden property="observacionesSolicitud" />
			<html:hidden property="fechaValidacion" />
			<html:hidden property="observacionesValidacion" />
			<html:hidden property="fechaSolicitudBaja" />
			<html:hidden property="observacionesBaja" />
			<html:hidden property="fechaBaja" />
			<html:hidden property="observacionesValBaja" />
			<html:hidden property="estadoPendientes" />
			<html:hidden property="validarInscripciones" />
			<html:hidden property="tipoGuardias" />
			<html:hidden property="turnosSel" />
			<html:hidden property="tipo" />
			<html:hidden property="clase" />
			<html:hidden property="estado" />
			<input type="hidden" name="actionModal" />
		</html:form>
		
		<html:form action="/JGR_BajaTurnos"  name="FormAConsultar" type ="com.siga.gratuita.form.InscripcionTGForm">
			<html:hidden property="modo"/>
			<html:hidden property="idInstitucion" />
			<html:hidden property="idPersona" />
			<html:hidden property="idTurno" />
			<html:hidden property="fechaSolicitud" />
			<html:hidden property="observacionesSolicitud" />
			<html:hidden property="fechaValidacion" />
			<html:hidden property="observacionesValidacion" />
			<html:hidden property="fechaSolicitudBaja" />
			<html:hidden property="observacionesBaja" />
			<html:hidden property="observacionesValBaja" />
			<html:hidden property="fechaBaja" />
			<html:hidden property="observacionesDenegacion" />
			<html:hidden property="fechaDenegacion" />
			<html:hidden property="estadoPendientes"/>
			<input type="hidden" name="actionModal" />
		</html:form>		
		<!-- FIN: BOTONES BUSQUEDA -->
		
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
	</body>
</html>