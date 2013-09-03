<!DOCTYPE html>
<html>
<head>
<!-- SolicitudIncorporacionDatos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri = "c.tld" 					prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm" %>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="utils.system"%>

<%
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");	

	Vector documentos = (Vector) request.getAttribute("datosDocumentacion");
	CenSolicitudIncorporacionBean datosPersonales = (CenSolicitudIncorporacionBean) request.getAttribute("datosPersonales");
	ArrayList idEstadoSolicitud = null;

	String  tipoSolicitud   = (String) request.getAttribute("TipoSolicitud");
	String  tipoColegiacion = (String) request.getAttribute("TipoColegiacion");
	String  tipoTratamiento = (String) request.getAttribute("TipoTratamiento");
	String  tipoEstadoCivil = (String) request.getAttribute("TipoEstadoCivil");
	String  provincia       = (String) request.getAttribute("Provincia");
	String  poblacion       = (String) request.getAttribute("Poblacion");
	String  pais  			= (String) request.getAttribute("Pais");
	String  estadoSolicitud = (String) request.getAttribute("EstadoSolicitud");
	String  editar			= (String) request.getAttribute("Editar");
	String  modoAnterior	= (String) request.getAttribute("ModoAnterior");
	String  modalidadDocumentacion = (String) request.getAttribute("ModalidadDocumentacion");
	
	ArrayList modalidadSel = new ArrayList();
	modalidadSel.add(datosPersonales.getIdModalidadDocumentacion());
	
	if (tipoSolicitud   == null) tipoSolicitud = "";
	if (tipoColegiacion == null) tipoColegiacion = "";
	if (tipoTratamiento == null) tipoTratamiento = "";
	if (tipoEstadoCivil == null) tipoEstadoCivil = "";
	if (provincia       == null) provincia = "";
	if (poblacion       == null) poblacion = "";
	if (pais       		== null) pais = "";
	if (estadoSolicitud == null) estadoSolicitud = "";
	if (editar          == null) editar = "false"; 
	else {
		idEstadoSolicitud = new ArrayList();
		idEstadoSolicitud.add(datosPersonales.getIdEstado().toString());
	}	
	
	String sexo  = datosPersonales.getSexo();
	String ssexo = "";
	if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE)) 
		ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
	else 
		ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer");

	String altoTabla = "80%";		
	if (editar!=null && editar.equalsIgnoreCase("true"))
		altoTabla = "75%";
	
	String idProvincia = "";
	String idPoblacion = "";
	ArrayList selPais = new ArrayList();
	selPais.add(datosPersonales.getIdPais());
	boolean esEspana=datosPersonales.getIdPais().equalsIgnoreCase(ClsConstants.ID_PAIS_ESPANA)||datosPersonales.getIdPais().equalsIgnoreCase("");
	if (esEspana){	
		idPoblacion = datosPersonales.getIdPoblacion();
		idProvincia = datosPersonales.getIdProvincia();
	}
	
	ArrayList selEstadoCiv = new ArrayList();
	ArrayList selIdent = new ArrayList();
	ArrayList selTratamiento = new ArrayList();
	selEstadoCiv.add(datosPersonales.getIdEstadoCivil());
	selIdent.add(datosPersonales.getIdTipoIdentificacion());
	selTratamiento.add(datosPersonales.getIdTratamiento());	
				
	String estiloBox = "box";
	String estiloBoxNumber = "boxNumber";
	String estiloCombo = "box";
	boolean readonly = false;
	String sreadonly = "false";
	boolean scheck = false;
	if (editar.equalsIgnoreCase("false")) {
		estiloBox = "boxConsulta";
		estiloBoxNumber = "boxConsulta";
		estiloCombo = "boxComboConsulta";
		readonly = true;
		sreadonly = "true";
		scheck = true;
	} else {
		estiloBox = "box";
		estiloBoxNumber = "boxNumber";
		estiloCombo = "boxCombo";
		readonly = false;
		sreadonly = "false";
		scheck = false;
	}
	
	String [] modalidadParam = new String[1];
	modalidadParam[0] = user.getLocation();
	
	//Para la cuenta bancaria
	String titular = datosPersonales.getTitular();
	String cbo_Codigo = datosPersonales.getCbo_Codigo();
	String cuentaCodigoSucursal = datosPersonales.getCodigoSucursal();
	String cuentaDigitoControl = datosPersonales.getDigitoControl();
	String cuentaNumeroCuenta = datosPersonales.getNumeroCuenta();
	String abonoCargo = datosPersonales.getAbonoCargo();
	String abonoJG = datosPersonales.getAbonoSJCS();
	boolean cargo = false;
	boolean abono = false;
	boolean abonoSJCS = false;
	if(abonoCargo.equalsIgnoreCase("C")){
		cargo = true;
	}else if(abonoCargo.equalsIgnoreCase("A")){
		abono = true;
	}else if(abonoCargo.equalsIgnoreCase("T")){
		abono = true;
		cargo = true;
	}
	if (abonoJG.equalsIgnoreCase("1")){
		abonoSJCS = true;
	}
	String nColegiado = "";
	if (datosPersonales.getNColegiado()!=null)
		nColegiado= datosPersonales.getNColegiado();
	
	String fechaBaja = "";
	
	if ((titular==null)||(titular.equalsIgnoreCase(""))){
		titular=datosPersonales.getNombre()+" "+datosPersonales.getApellido1()+" "+datosPersonales.getApellido2();
		if (titular.length()>100){
			titular=titular.substring(0,99);
		}
	}
	
	ArrayList selColegiacion = new ArrayList();
	selColegiacion.add(datosPersonales.getIdTipoColegiacion());
	ArrayList selSolicitud = new ArrayList();
	selSolicitud.add(datosPersonales.getIdTipoSolicitud());
	
	boolean residente = datosPersonales.getResidente();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


	
	<html:javascript formName="SolicitudIncorporacionForm" staticJavascript="false" />  	
  	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>
	
	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera3" localizacion="censo.solicitudIncorporacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<script language="JavaScript">

	function editarNColegiado(){
		var numeroCol = jQuery("#numColBox");
		var botNumeroCol = document.getElementById("botonNCol"); 
		if (numeroCol[0].disabled){
			numeroCol.removeAttr('disabled');
			numeroCol.removeClass().addClass('box');			
			botNumeroCol.style.visibility ="hidden";
		}
	}
	
	function restablecerNColegiado(){
		var numeroCol = jQuery("#numColBox");
		var botNumeroCol = document.getElementById("botonNCol");
		<%if(nColegiado==null || nColegiado.equalsIgnoreCase("")){%>
			numeroCol.attr('disabled', 'disabled');
			numeroCol.removeClass().addClass('boxDisabled');
			botNumeroCol.style.visibility ="visible";
		<%}else{%>
			numeroCol.removeClass().addClass('box');
			document.getElementById("numColBox").value=<%=nColegiado%>;
			botNumeroCol.style.visibility ="hidden";
		<%}%>
	}
/*aalg:funci�n para controlar si el n�mero de colegiado elegido se puede asignar o ya 
 * ha sido utilizado. Inc. 47 */
 
	function existeNColegiado(){
		if ($("#numColBox").length!=0 && $("#numColBox").val()!=""){
			jQuery.ajax({ //Comunicacion jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_MantenimientoSolicitudesIncorporacion.do?modo=getAjaxExisteColegiado",
				dataType: "json",
				data: "nColegiado="+$("#numColBox").val(),
				
				success: function(json){
					var mensaje = json.mensaje;
					if (mensaje != null && mensaje != ""){
						alert(mensaje);
						$("#numColBox").val("");
					}

					fin();
				},
				error: function(e){
					alert('Error de comunicaci�n: ' + e);
					fin();
				}
			});
		}
	}
	
	function validaAbonoSJCS(){
		if (document.SolicitudIncorporacionForm.abonoSJCS.checked) {
			if (!document.SolicitudIncorporacionForm.cuentaAbono.checked) {
				var mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.cuentaSJCS"/>";
				alert (mensaje);
				document.SolicitudIncorporacionForm.abonoSJCS.checked = false;
				return false;
			}
		}
	}

	function datosValidos(){
		var errores = "";

		if(validaNumeroIdentificacion()){

			if(document.getElementById("tipoSolicitud").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.solicitudDe'/>"+ '\n';
			}
			if(document.getElementById("tipoColegiacion").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.tipoColegiacion'/>"+ '\n';
			}
			if(document.getElementById("tipoModalidadDocumentacion").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.documentacion'/>"+ '\n';
			}
			if(document.SolicitudIncorporacionForm.tipoIdentificacion.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nifcif'/>"+ '\n';
			}
			if(document.SolicitudIncorporacionForm.tipoDon.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.tratamiento'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.nombre.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nombre'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.apellido1.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.apellido1'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.fechaNacimiento.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.fechaNacimiento'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.sexo.value=="0"){
				errores += "<siga:Idioma key='errors.required' arg0='censo.consultaDatosGenerales.literal.sexo'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.domicilio.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.domicilio'/>" + '\n';
			}			
			if (document.SolicitudIncorporacionForm.pais.value == "" || document.SolicitudIncorporacionForm.pais.value == "<%=ClsConstants.ID_PAIS_ESPANA%>") {
				if(document.SolicitudIncorporacionForm.CP.value==""){
					errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.codigoPostal'/>" + '\n';
				}			
				else if (!validaCP(document.SolicitudIncorporacionForm.CP.value)) {
					errores += "<siga:Idioma key='errors.invalid' arg0='censo.SolicitudIncorporacion.literal.codigoPostal'/>" + '\n';
				}
					   		
		   		if (document.SolicitudIncorporacionForm.poblacion.value == "") {
		   			errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.poblacion'/>" + '\n';
		       	}
		   		
		    } else {
				if(document.SolicitudIncorporacionForm.CPExt.value==""){
					errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.codigoPostal'/>" + '\n';
				}		    	
				else {
					document.SolicitudIncorporacionForm.CP.value=document.SolicitudIncorporacionForm.CPExt.value;
				}
				
		   		if (document.SolicitudIncorporacionForm.poblacionExt.value == "") {
		   			errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.poblacion'/>" + '\n';
		       	}
			}
			if(document.SolicitudIncorporacionForm.telefono1.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.telefono1'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.mail.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.email'/>" + '\n';
			}
			
			if(!calcularDigito()){
				fin();
				return false;
			}		
			
			if (errores != ""){
				alert(errores);
			}else{
				if(validateSolicitudIncorporacionForm(document.SolicitudIncorporacionForm))
					return true;
			}
		}
		return false;
	}

	function comprobarTipoIdent(){
		<%if(!readonly){%>
			// Solo se genera el NIF o CIF de la persona
			if((SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")||
				(SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>")){
				document.getElementById("idButtonNif").style.visibility="visible";
			}	else{
				document.getElementById("idButtonNif").style.visibility="hidden";
			}
		<%}%>
	}	

	function cambioModalidad(){		
		var selMod = document.getElementById("tipoModalidadDocumentacion");
		var strMod = <%=datosPersonales.getIdModalidadDocumentacion()%>;
		var selSol = document.getElementById("tipoSolicitud");
		var strSol = <%=datosPersonales.getIdTipoSolicitud()%>;
		var selCol = document.getElementById("tipoColegiacion");
		var strCol = <%=datosPersonales.getIdTipoColegiacion()%>;
		if((selMod.value!=strMod)||(selCol.value!=strCol)||(selSol.value!=strSol)){
			if(!confirm('<siga:Idioma key="messages.confirm.cambioModalidad"/>')){
				for (var i=0; i<selMod.options.length; i++) {
					if (selMod.options[i].value == strMod) {
						selMod.selectedIndex = i;
					}
				}
				for (i=0; i<selSol.options.length; i++) {
					if (selSol.options[i].value == strSol) {
						selSol.selectedIndex = i;
					}
				}
				for (i=0; i<selCol.options.length; i++) {
					if (selCol.options[i].value == strCol) {
						selCol.selectedIndex = i;
					}
				}
				return false;
			}
		}
		return true;	
	}
	
	function obtenerLetra(){
		if (generarLetra()) {
			var tipoIdentificacion = document.getElementById("tipoIdentificacion").value;
			if(tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")
				alert("<siga:Idioma key='messages.nifcif.comprobacion.correcto'/>");
			else
				if(tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>")
					alert("<siga:Idioma key='messages.nie.comprobacion.correcto'/>");
		}

	}

	function generarLetra(){
		var numId = document.getElementById("NIFCIF").value;
		var tipoIdentificacion = document.getElementById("tipoIdentificacion").value;
	  	var letra='TRWAGMYFPDXBNJZSQVHLCKET';
		if(numId.length==0) {
			return false;		
		}
		if(tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			if(numId.length==8){
				if(isNumero(numId)==true){
				 	numero = numId;
				 	numero = numero % 23;
				 	letra=letra.substring(numero,numero+1);
				 	document.getElementById("NIFCIF").value = numId+letra;
				}
				else
					return validaNumeroIdentificacion(tipoIdentificacion, numId);					
			} 
			else
				return validaNumeroIdentificacion(tipoIdentificacion, numId);
		} 
		else
			if((tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){
				if(numId.length==8){
					var dnie = document.getElementById("NIFCIF").value;
					letIni = numId.substring(0,1);
					primeraLetra = letIni;
					if  (letIni.toUpperCase()=='Y')
			 			letIni = '1';
			 		else {
			 			if  (letIni.toUpperCase()=='Z')
			 				letIni = '2';
			 			else
			 				letIni = '0';
			 		}
			 
					num = letIni+numId.substring(1,8);
					if(primeraLetra.match('[X|Y|Z]') && isNumero(num)){
						var posicion = num % 23;
						letras='TRWAGMYFPDXBNJZSQVHLCKET';
						var letra=letras.substring(posicion,posicion+1);
						numero = dnie + letra;
						document.getElementById("NIFCIF").value = numero;
					} 
					else 
						return validaNumeroIdentificacion(tipoIdentificacion, numId);					
				} 
				else
					return validaNumeroIdentificacion(tipoIdentificacion, numId);
			}
						
		// Caso1: Se han realizado las modificaciones necesarias sin encontrar errores 
		// Caso2: no es nif ni nie no hay generacion de letra	
		return true;
	}	

	function validaNumeroIdentificacion(){
		var errorNIE = false;
		var errorNIF = false;
		var valido = true;

		if(SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			var numero = SolicitudIncorporacionForm.NIFCIF.value;
			if(numero.length==9){
				letIn = numero.substring(8,9);
				num = numero.substring(0,8);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letIn) {
					errorNIF=true;
				}
			}else{
				errorNIF=true;
			}
		}
		if(SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
			var dnie = SolicitudIncorporacionForm.NIFCIF.value;
			if(dnie.length==9){
				letIni = dnie.substring(0,1);
				primera=letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni + dnie.substring(1,8);
				letFin = dnie.substring(8,9);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (!primera.match('[X|Y|Z]')||letra!=letFin) {
					errorNIE=true;
				}
			}else{
				errorNIE=true;
			}
		}
		if (errorNIF){
			valido = false;
			alert("<siga:Idioma key="messages.nif.comprobacion.digitos.error"/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key="messages.nie.comprobacion.digitos.error"/>");
		}
		return valido;
	}

	function cargarChecksCuenta(){
		document.SolicitudIncorporacionForm.cuentaCargo.checked = <%=cargo%>;
		document.SolicitudIncorporacionForm.cuentaAbono.checked = <%=abono%>;
		document.SolicitudIncorporacionForm.abonoSJCS.checked = <%=abonoSJCS%>;
		document.SolicitudIncorporacionForm.residente.checked = <%= residente%>;
	}

	// Funciones para obtener el d�gito de control de la Cuenta
	function obtenerDigito(valor){	
	  valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
	  control = 0;
	  for (var i=0; i<=9; i++)
	    control += parseInt(valor.charAt(i)) * valores[i];		  
	  control = 11 - (control % 11);
	  if (control == 11) control = 0;
	  else if (control == 10) control = 1;
	  return control;
	}
	
	function numerico(valor){
		cad = valor.toString();
		for (var i=0; i<cad.length; i++) {
			var caracter = cad.charAt(i);
			if (caracter<"0"||caracter>"9"){					
				return false;
			}
		}
		return true;
	}
	
	function calcularDigito(){
		mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";
	
		f = document.SolicitudIncorporacionForm;		
		if (f.cbo_Codigo.value    == ""  && f.codigoSucursal.value == "" && f.digitoControl.value == ""  && f.numeroCuenta.value   == "" ){ 
			 return true;
		}
		else{
			if(f.banco.value==""){
				alert(mensaje);
				return false;
			}
			if(f.cbo_Codigo.value.length != 4 || f.codigoSucursal.value.length != 4 || f.digitoControl.value.length != 2 || f.numeroCuenta.value.length != 10){
				alert(mensaje);
				return false;
			}else{
				if(!numerico(f.cbo_Codigo.value) || !numerico(f.codigoSucursal.value) || !numerico(f.digitoControl.value) || !numerico(f.numeroCuenta.value)){
					alert(mensaje);
					return false;
				}
				else {
				  if(f.digitoControl.value != obtenerDigito("00" + f.cbo_Codigo.value + f.codigoSucursal.value) + "" + obtenerDigito(f.numeroCuenta.value)){
						alert(mensaje);
						return false;
					}
				}
			}
		}
		
		return true;  
	}	
	
	function quitarBotonesAlta(){
		<%if(!modoAnterior.equalsIgnoreCase("Editar")){%>
			if(document.getElementById("tdBotonSolicitudPlanProfesional")){
				jQuery("#tdBotonSolicitudPlanProfesional").attr('disabled', 'disabled');
			}
			if(document.getElementById("tdBotonSolicitudSeguroUniversal")){
				jQuery("#tdBotonSolicitudSeguroUniversal").attr('disabled', 'disabled');
			}
		<%}%>
	}	

	function accionVolver(){		
		document.forms[0].action="./CEN_SolicitudesIncorporacion.do";	
		document.forms[0].target="mainWorkArea";
		document.forms[0].submit();
	}
 
	function accionRestablecer(){
		if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
			document.SolicitudIncorporacionForm.reset();
			cargaPais(<%=datosPersonales.getIdPais()%>);
			cargarChecksCuenta();
			comprobarTipoIdent();
			restablecerNColegiado();
		}
	}
	
	function refrescarLocal(){
		document.forms[0].modo.value = "abrir";
		document.forms[0].target = "mainPestanas";
		document.forms[0].submit();
		
	}

	function accionGuardar(){
		sub();
		if(cambioModalidad()){			
			if(datosValidos()){
				var datos;
				var size=0;
				<% if (documentos!=null) { %>
					size=<%=documentos.size()%>;
				<% } %>
				 
				if (size>0) {  
					datos = document.getElementById('tablaDatosDinamicosD');
					datos.value = ""; 
					var j, fila;
					for (fila = 1; fila < size+1; fila++) {
						var tabla;
						tabla = document.getElementById('documentoAPresentar');
						var flag = true;
						j = 1;
						while (flag) {
							var aux = 'oculto' + fila + '_' + j;
							var oculto = document.getElementById(aux);
							if (oculto == null)  
								flag = false;
							else 
								datos.value = datos.value + oculto.value + ',';
							j++;
						}
						datos.value = datos.value + "%";
						datos.value = datos.value + (tabla.rows[fila].cells)[0].children[j-2].checked + ',';
						datos.value = datos.value + "#";
					}
				}								
				document.SolicitudIncorporacionForm.modo.value = "Modificar";
				document.SolicitudIncorporacionForm.target = "submitArea";
				document.SolicitudIncorporacionForm.submit();
			}else{
				fin();
				return false;
			}
		}else{
			fin();
			return false;
		}
	}

	function accionCerrar(){	
		window.top.close();
		//top.cierraConParametros("MODIFICADO");
	}
	
	function accionSolicitarAltaAlterMutua(){
		document.AlterMutuaForm.modo.value="nuevo";
		
		document.AlterMutuaForm.domicilio.value  = document.SolicitudIncorporacionForm.domicilio.value;
		document.AlterMutuaForm.poblacion.value  = document.SolicitudIncorporacionForm.poblacion.text;
		document.AlterMutuaForm.codigoPostal.value  = document.SolicitudIncorporacionForm.CP.value;
		document.AlterMutuaForm.telefono1.value  = document.SolicitudIncorporacionForm.telefono1.value;
		document.AlterMutuaForm.telefono2.value  = document.SolicitudIncorporacionForm.telefono2.value;
		document.AlterMutuaForm.movil.value  = document.SolicitudIncorporacionForm.telefono3.value;
		document.AlterMutuaForm.fax.value  = document.SolicitudIncorporacionForm.fax1.value;
		document.AlterMutuaForm.correoElectronico.value  = document.SolicitudIncorporacionForm.mail.value;
		
		document.AlterMutuaForm.cboCodigo.value  = document.SolicitudIncorporacionForm.banco.value;
		document.AlterMutuaForm.codigoSucursal.value  = document.SolicitudIncorporacionForm.codigoSucursal.value;
		document.AlterMutuaForm.digitoControl.value  = document.SolicitudIncorporacionForm.digitoControl.value;
		document.AlterMutuaForm.numeroCuenta.value  = document.SolicitudIncorporacionForm.numeroCuenta.value;
		
		document.AlterMutuaForm.identificador.value = document.getElementById('numeroIdentificacionBBDD').value;
		document.AlterMutuaForm.tipoIdentificacion.value = document.SolicitudIncorporacionForm.tipoIdentificacion.options[document.SolicitudIncorporacionForm.tipoIdentificacion.selectedIndex].text;
		document.AlterMutuaForm.nombre.value = document.SolicitudIncorporacionForm.nombre.value;
		document.AlterMutuaForm.apellidos.value = document.SolicitudIncorporacionForm.apellido1.value+" "+document.SolicitudIncorporacionForm.apellido2.value;
		document.AlterMutuaForm.fechaNacimiento.value = document.SolicitudIncorporacionForm.fechaNacimiento.value;
		document.AlterMutuaForm.sexo.value = document.SolicitudIncorporacionForm.sexo.options[document.SolicitudIncorporacionForm.sexo.selectedIndex].text;
		document.AlterMutuaForm.estadoCivil.value  =  document.SolicitudIncorporacionForm.estadoCivil.options[document.SolicitudIncorporacionForm.estadoCivil.selectedIndex].text;
		
		//document.AlterMutuaForm.target.value = "mainWorkArea";
		
		document.AlterMutuaForm.submit();
/*		
   		var resultado = ventaModalGeneral(document.AlterMutuaForm.name,"G",'<siga:Idioma key="censo.mutualidad.aviso.espera" />');
   		
	    if(resultado && resultado.length){
	    	
	    	actualizaDatosMutualidad(idTipoSolicitud,resultado);
	    }
*/
	}
	
	function accionSolicitarAltaMutualidad(idTipoSolicitud){
		
		document.MutualidadForm.modo.value="nuevo";
		document.MutualidadForm.tipoIdentificacion.value = document.SolicitudIncorporacionForm.tipoIdentificacion.options[document.SolicitudIncorporacionForm.tipoIdentificacion.selectedIndex].text;
		document.MutualidadForm.idTipoIdentificacion.value = document.SolicitudIncorporacionForm.tipoIdentificacion.options[document.SolicitudIncorporacionForm.tipoIdentificacion.selectedIndex].value;

		//document.MutualidadForm.numeroIdentificacion.value = document.SolicitudIncorporacionForm.NIFCIF.value;
		document.MutualidadForm.numeroIdentificacion.value = document.getElementById('numeroIdentificacionBBDD').value;
		
		document.MutualidadForm.sexo.value = document.SolicitudIncorporacionForm.sexo.options[document.SolicitudIncorporacionForm.sexo.selectedIndex].text;
		document.MutualidadForm.idSexo.value = document.SolicitudIncorporacionForm.sexo.options[document.SolicitudIncorporacionForm.sexo.selectedIndex].value;
		
		document.MutualidadForm.tratamiento.value = document.SolicitudIncorporacionForm.tipoDon.options[document.SolicitudIncorporacionForm.tipoDon.selectedIndex].text;
		document.MutualidadForm.idTratamiento.value = document.SolicitudIncorporacionForm.tipoDon.options[document.SolicitudIncorporacionForm.tipoDon.selectedIndex].value;
		
		document.MutualidadForm.nombre.value = document.SolicitudIncorporacionForm.nombre.value;
		document.MutualidadForm.apellido1.value = document.SolicitudIncorporacionForm.apellido1.value;
		document.MutualidadForm.apellido2.value = document.SolicitudIncorporacionForm.apellido2.value;
		document.MutualidadForm.naturalDe.value  = document.SolicitudIncorporacionForm.natural.value;
		//document.MutualidadForm.fechaNacimiento.value  = document.SolicitudIncorporacionForm.fechaNacimiento.value;
		document.MutualidadForm.fechaNacimiento.value  = document.getElementById('fechaNacimientoBBDD').value;
		
		
		document.MutualidadForm.estadoCivil.value  =  document.SolicitudIncorporacionForm.estadoCivil.options[document.SolicitudIncorporacionForm.estadoCivil.selectedIndex].text;
		document.MutualidadForm.idEstadoCivil.value  =  document.SolicitudIncorporacionForm.estadoCivil.options[document.SolicitudIncorporacionForm.estadoCivil.selectedIndex].value;
		
		
		document.MutualidadForm.idPais.value  = document.SolicitudIncorporacionForm.pais.value;
		document.MutualidadForm.idProvincia.value  = document.SolicitudIncorporacionForm.provincia.value;
		document.MutualidadForm.idPoblacion.value  = document.SolicitudIncorporacionForm.poblacion.value;
		document.MutualidadForm.poblacionExtranjera.value  = document.SolicitudIncorporacionForm.poblacionExt.value;
		document.MutualidadForm.domicilio.value  = document.SolicitudIncorporacionForm.domicilio.value;
		document.MutualidadForm.codigoPostal.value  = document.SolicitudIncorporacionForm.CP.value;
		document.MutualidadForm.telef1.value  = document.SolicitudIncorporacionForm.telefono1.value;
		document.MutualidadForm.telef2.value  = document.SolicitudIncorporacionForm.telefono2.value;
		document.MutualidadForm.movil.value  = document.SolicitudIncorporacionForm.telefono3.value;
		document.MutualidadForm.fax1.value  = document.SolicitudIncorporacionForm.fax1.value;
		document.MutualidadForm.fax2.value  = document.SolicitudIncorporacionForm.fax2.value;
		document.MutualidadForm.correoElectronico.value  = document.SolicitudIncorporacionForm.mail.value;
		
		document.MutualidadForm.titular.value  = document.SolicitudIncorporacionForm.titular.value;
		document.MutualidadForm.idBanco.value  = document.SolicitudIncorporacionForm.banco.value;
		document.MutualidadForm.cboCodigo.value  = document.SolicitudIncorporacionForm.banco.value;
		document.MutualidadForm.codigoSucursal.value  = document.SolicitudIncorporacionForm.codigoSucursal.value;
		document.MutualidadForm.digitoControl.value  = document.SolicitudIncorporacionForm.digitoControl.value;
		document.MutualidadForm.numeroCuenta.value  = document.SolicitudIncorporacionForm.numeroCuenta.value;
		
		document.MutualidadForm.idTipoSolicitud.value  = idTipoSolicitud;
		
		document.MutualidadForm.idSolicitudIncorporacion.value  = document.SolicitudIncorporacionForm.editarIdSolicitud.value;
		
   		var resultado = ventaModalGeneral(document.MutualidadForm.name,"G",'<siga:Idioma	key="censo.mutualidad.aviso.espera" />');
   		
   		
	    if(resultado && resultado.length){
	    	
	    	actualizaDatosMutualidad(idTipoSolicitud,resultado);
	    }
	}
	
	function actualizaDatosMutualidad(idTipoSolicitud,resultado){
		document.MutualidadForm.idSolicitud.value = resultado[0];
		document.MutualidadForm.idSolicitudAceptada.value = resultado[1];
		document.MutualidadForm.idEstado.value = resultado[3];
		if(idTipoSolicitud=='P'){
			if(resultado[1]!='0'&&resultado[1]!='')
				document.getElementById("tdIdSolicitudPlanProfesional").innerHTML = resultado[1];
			else
				document.getElementById("tdIdSolicitudPlanProfesional").innerHTML = resultado[0];
	    	document.getElementById("tdEstadoSolicitudPlanProfesional").innerHTML = resultado[2];
	    	jQuery("#tdBotonSolicitudPlanProfesional").hide();
	    	jQuery("#tdBotonEstadoSolicitudPlanProfesional").show();
	    	jQuery("#tdTextoNSolicitudPlanProfesional").show();
	    	
		}else{
			if(resultado[1]!='0'&&resultado[1]!='')
				document.getElementById("tdIdSolicitudSeguroUniversal").innerHTML = resultado[1];
			else
				document.getElementById("tdIdSolicitudSeguroUniversal").innerHTML = resultado[0];
	    	document.getElementById("tdEstadoSolicitudSeguroUniversal").innerHTML = resultado[2];
	    	jQuery("#tdBotonSolicitudSeguroUniversal").hide();
	    	jQuery("#tdBotonEstadoSolicitudSeguroUniversal").show();
	    	jQuery("#tdTextoNSolicitudSeguroUniversal").show();
		}
	}
	
	function accionComprobarEstadoMutualidad(idTipoSolicitud){
		 if(idTipoSolicitud=='P'){
			 if(document.MutualidadForm.idSolicitud.value==''){
				document.MutualidadForm.idSolicitud.value = document.SolicitudIncorporacionForm.idSolicitudPlanProfesional.value;
				document.MutualidadForm.idSolicitudAceptada.value =document.SolicitudIncorporacionForm.idSolicitudAceptadaPlanProfesional.value;
			 }
		}else{
			if(document.MutualidadForm.idSolicitud.value==''){
				document.MutualidadForm.idSolicitud.value = document.SolicitudIncorporacionForm.idSolicitudSeguroUniversal.value;
				document.MutualidadForm.idSolicitudAceptada.value =document.SolicitudIncorporacionForm.idSolicitudAceptadaSeguroUniversal.value;
			}
		} 
		 
		document.MutualidadForm.modo.value = "actualizaEstado";
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'<siga:Idioma	key="censo.mutualidad.aviso.espera" />');
		if(resultado){
			document.MutualidadForm.idEstado.value = resultado[0];
			document.MutualidadForm.estado.value = resultado[1];	
			if(idTipoSolicitud=='P'){
		    	document.getElementById("tdEstadoSolicitudPlanProfesional").innerHTML = resultado[1];
			}else{
		    	document.getElementById("tdEstadoSolicitudSeguroUniversal").innerHTML = resultado[1];
		    	
			}
			var ruta = resultado[2];
			if(ruta && ruta.length>0 && confirm('�Desea descargar el documento asociado a la solicitud?')){
				
				var formu=document.createElement("<form method='POST' name='descargar'  action='/SIGA/ServletDescargaFichero.svrl' target='submitArea'>");
				formu.appendChild(document.createElement("<input type='hidden' name='rutaFichero'   value='' />"));
				formu.appendChild(document.createElement("<input type='hidden' name='nombreFichero'   value='' />"));
				formu.appendChild(document.createElement("<input type='hidden' name='accion'   value='' />"));
				document.appendChild(formu);
				formu.rutaFichero.value=ruta;
				formu.nombreFichero.value='Solicitud.pdf';
				formu.accion.value = "";
				formu.submit();				
			}			
		}
	}
	
	function accionComprobarEstadoMutualista(){
		document.MutualidadForm.idSolicitudAceptada.value =document.SolicitudIncorporacionForm.idSolicitudAceptadaPlanProfesional.value;
		document.MutualidadForm.modo.value = "actualizaEstadoMutualista";
		document.MutualidadForm.numeroIdentificacion.value = document.getElementById('numeroIdentificacionBBDD').value;
		document.MutualidadForm.fechaNacimiento.value  = document.getElementById('fechaNacimientoBBDD').value;
		var resultado = ventaModalGeneral(document.MutualidadForm.name,"0",'<siga:Idioma	key="censo.mutualidad.aviso.espera" />');
	}

	quitarBotonesAlta();
	
	var primeravez=true;
	function cargaPais(valor){      		
		<%if(!readonly){%>
			if (valor!=null && valor!="" && valor!=<%=ClsConstants.ID_PAIS_ESPANA%>) {
				if (primeravez) {
					primeravez=false;
					cargaInicial (20, 20, 73);
				}
				else {
					document.getElementById("CPExt").value="";
					document.getElementById("poblacionExt").value="";
				}
				jQuery("#trEspanol1").hide();
				jQuery("#trEspanol2").hide();
				jQuery("#trExtranjero").show();				
				
	       } else {
				if (primeravez) {
					primeravez=false;
					cargaInicial (20, 20, 73);
				}
				else {
					document.getElementById("CP").value="";
					cambiaCodigoPostal();					
				}
				jQuery("#trEspanol1").show();
				jQuery("#trEspanol2").show();
				jQuery("#trExtranjero").hide();
	       }
			
		<%}else{%>
			if (valor!=null && valor!="" && valor!=<%=ClsConstants.ID_PAIS_ESPANA%>) {
				jQuery("#trEspanol1").hide();
				jQuery("#trEspanol2").hide();
				jQuery("#trExtranjero").show();				
				
	       } else {
	    	   	jQuery("#trEspanol1").show();
				jQuery("#trEspanol2").show();
				jQuery("#trExtranjero").hide();	
	       }			
		<%}%>
	}	
	
	// --------------------------------------------------------------------------------------------------------------
	//		BANCOS
	// --------------------------------------------------------------------------------------------------------------
	
	var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "messages.general.error"))%>';
	
	function cargarBancos(){
		var idBanco = SolicitudIncorporacionForm.cbo_Codigo.value;	
		if (idBanco!=undefined&&idBanco!="") {
			jQuery.ajax({ //Comunicacion jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBanco",
				data: "idBanco="+idBanco,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){		
					var txtBanco = json.banco.nombre;
					document.getElementById("banco").value=txtBanco;
					fin();
				},
				error: function(e){
					alert(mensajeGeneralError);
					fin();
				}
			});
		} else {
			document.getElementById("banco").value="";
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------
	//		FILTRO DE POBLACIONES
	// --------------------------------------------------------------------------------------------------------------
	
	var bControl=false;	
	var bControlKey=false;
	var bFocoCP=false;
	var bFocoPoblacionInput=false;	
	var cteControl="controlFiltro";
	var controlCP="";
	var controlFiltro=cteControl; 
	var controlKeyCP="";	
	var cteSeleccionar="<siga:Idioma key='general.combo.seleccionar'/>";	
	var msgControl="";	
	var numLineasSelectPoblaciones=20; // Siempre debe ser menor o igual que numMaximoSelectPoblaciones
	var numMaximoSelectPoblaciones=20; // Siempre debe ser mayor o igual que numLineasSelectPoblaciones
	var numMinLetrasFiltro=3;
	var numPoblaciones=0;
	var tiempoEspera=500;
	var txtTabulador="";
	var valorGuiones="";	
	
	// Funcion para cargar el autocomplete 
	function cargaInicial (numLineasSelectPoblacionesAux, numMaximoSelectPoblacionesAux, numeroGuiones) {		
		//msgControl=msgControl+"cargaInicial("+numLineasSelectPoblacionesAux+","+numMaximoSelectPoblacionesAux+","+numeroGuiones+")\n";alert(msgControl);
		
		if (numLineasSelectPoblacionesAux>0) {
			numLineasSelectPoblaciones=numLineasSelectPoblacionesAux;
		}
		
		if (numMaximoSelectPoblacionesAux>0) {
			numMaximoSelectPoblaciones=numMaximoSelectPoblacionesAux;
		}
		
		if (numeroGuiones>0) {
			valorGuiones="";
			while(valorGuiones.length<numeroGuiones) {
				valorGuiones=valorGuiones+"-";
			}
		}
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		var elementoPoblacionDiv 	= jQuery("#poblacion_div");
		
		var pos = elementoPoblacionSelect.offset();             
		elementoPoblacionSelect.css("position", "absolute");             
		elementoPoblacionSelect.css("zIndex", 9999);             
		elementoPoblacionSelect.offset(pos);
		elementoPoblacionDiv.hide(); 
	}	
	
	// Cambia el codigo postal
	function cambiaCodigoPostal() {
		//msgControl=msgControl+"cambiaCodigoPostal()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar		
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		var elementoPoblacionDiv 	= jQuery("#poblacion_div");
		var elementoPoblacionHidden = document.getElementById("poblacion");
		var elementoPoblacionInput 	= document.getElementById("poblacion_input");		
		var elementoProvinciaHidden = document.getElementById("provincia");
		var elementoProvinciaInput	= document.getElementById("provincia_input");			
		
		elementoPoblacionSelect[0].innerHTML="";
		elementoPoblacionDiv.hide();
		
		elementoPoblacionHidden.value="";
		elementoPoblacionInput.value="";
		
		elementoProvinciaHidden.value="";
		elementoProvinciaInput.value="";
					
		controlCP=cteControl;
		controlFiltro=cteControl;
		numPoblaciones=0;
		
		elementoPoblacionInput = jQuery("#poblacion_input");
		if (!elementoPoblacionInput[0].disabled) {
			elementoPoblacionInput.attr('disabled', 'disabled');
			elementoPoblacionInput.removeClass().addClass('boxDisabled');
		}
	}
	
	// Control del codigo postal
	function validaCP(codigoPostal){
		//msgControl=msgControl+"validaCP("+codigoPostal+")\n";alert(msgControl);
		
		if (codigoPostal.length!=5) {
			return false;
		}
		
		var strProvincia = codigoPostal.substring(0, 2);
		var intProvincia = parseInt(strProvincia,10);			
		var tresUltimosDigitos = codigoPostal.substring(2);
		
		if (tresUltimosDigitos=="000" || intProvincia<1 || intProvincia>52) {
			return false;
		}
		
		return true;
	}		
	
	// Se realiza cuando pierde el foco el campo codigo postal
	function onBlurCP() {
		//msgControl=msgControl+"onBlurCP()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar		
		var elementoPoblacionDiv = jQuery("#poblacion_div");
		
		bFocoCP=false;
		
		if (bControl) {
			bControl=false;
			
		} else {						
			elementoPoblacionDiv.hide();
		}
	}
	
	// Se realiza cuando obtiene el foco el campo codigo postal
	function onFocusCP() {
		//msgControl=msgControl+"onFocusCP()\n";alert(msgControl);
		
		bFocoCP=true;
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput = document.getElementById("poblacion_input");	
		var elementoCP = document.getElementById("CP");
		
		if (!validaCP(elementoCP.value)) {
			cambiaCodigoPostal();
			
		} else {
			if (elementoPoblacionInput.value=="") {
				actualizarPoblacionesCP(true);
			}
		}
	}	
	
	// Se realiza despues de cargar la letra del cp
	function onKeyUpCP() {
		//msgControl=msgControl+"onKeyUpCP()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar	
		var elementoCP = document.getElementById("CP");
		
		// Control que comprueba si ha modificado el codigo postal
		if (controlKeyCP!=elementoCP.value) {
			if (!validaCP(elementoCP.value)) {
				cambiaCodigoPostal();
				
			} else { 
				actualizarPoblacionesCP(true);
			}
		}
	}		
	
	// Se realiza antes de cargar la letra del cp
	function onKeyDownCP() {
		//msgControl=msgControl+"onKeyDownCP()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar	
		var elementoCP = document.getElementById("CP");
		
		controlKeyCP = elementoCP.value;	
	}	
	
	// Selecciona un elemento del desplegable
	function onChangePoblacionSelect(editaPoblacionInput) {
		//msgControl=msgControl+"onChangePoblacionSelect("+editaPoblacionInput+")\n";alert(msgControl);	
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		var elementoPoblacionHidden = document.getElementById("poblacion");
		var elementoPoblacionInput 	= document.getElementById("poblacion_input");				
		var elementoProvinciaHidden = document.getElementById("provincia");
		var elementoProvinciaInput 	= document.getElementById("provincia_input");
		var elementoCP 				= document.getElementById("CP");
		
		// Vamos a mostrar el elemento seleccionado del desplegable en el filtro
		var poblacionSelected=elementoPoblacionSelect[0].selectedIndex;
		
		// 0: Si ha seleccionado el elemento "--Seleccionar"
		if (poblacionSelected<0) { // -1: Si no hay nada seleccionado
			if (editaPoblacionInput) {
				elementoPoblacionInput.value="";
			}
			elementoPoblacionHidden.value="";
			
			if (elementoCP.value=="") {
				elementoProvinciaHidden.value="";
				elementoProvinciaInput.value="";
			}
			
		} else {					
			// Obtengo el valor de la poblacion
			var valorPoblacionSelect = elementoPoblacionSelect.val(); 
			if (valorPoblacionSelect!="") { // Seleccion y guiones tienen valor ""
				var arrayDatos = valorPoblacionSelect.split("#"); // Otras poblaciones tienen valor "IdPoblacion#IdProvincia#NombreProvincia"
				elementoPoblacionHidden.value=arrayDatos[0]; // IdPoblacion
				elementoProvinciaHidden.value=arrayDatos[1]; // IdProvincia
				elementoProvinciaInput.value=arrayDatos[2]; // NombreProvincia
				
				if (editaPoblacionInput) {
					var nombrePoblacion = elementoPoblacionSelect[0].options[poblacionSelected].text;
				
					// Este control evita perder el foco, si el nombre es identico
					if (nombrePoblacion!=elementoPoblacionInput.value) {
						elementoPoblacionInput.value = nombrePoblacion; // NombrePoblacion
					}
				}
			}
			else {
				if (editaPoblacionInput) {
					elementoPoblacionInput.value="";
				}
				elementoPoblacionHidden.value="";
				
				if (elementoCP.value=="") {
					elementoProvinciaHidden.value="";
					elementoProvinciaInput.value="";
				}
			}
		}
	}
	
	// Pulsa una opcion del desplegable
	function onClickPoblacionSelect() {
		//msgControl=msgControl+"onClickPoblacionSelect()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput = document.getElementById("poblacion_input");
		
		bControl=true; // Inhabilita onFocusPoblacionInput()
		elementoPoblacionInput.focus(); //Invoca onBlurPoblacionSelect() + onFocusPoblacionInput()		 
	}
	
	// Este metodo es necesario para cuando va a realizar una accion en el desplegable sin tener el foco
	function onMouseDownPoblacionSelect() {
		//msgControl=msgControl+"onMouseDownPoblacionSelect()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		
		// Para hacer esto tiene que tener el foco poblacion_input
		if (bFocoPoblacionInput || bFocoCP) {				
			bControl=true; // Inhabilita onBlurPoblacionInput() o onBlurCP()
			elementoPoblacionSelect.focus(); //Invoca onBlurPoblacionInput() o onBlurCP()
		}
	}		
	
	// Se realiza cuando abandona el foco del desplegable
	function onBlurPoblacionSelect() {	
		//msgControl=msgControl+"onBlurPoblacionSelect()\n";alert(msgControl);		
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionDiv = jQuery("#poblacion_div");
		
		// Oculto el desplegable y recalculo el valor del filtro
		elementoPoblacionDiv.hide();
		onChangePoblacionSelect(true);
	}	
	
	// Se realiza cuando pulsa una letra en el desplegable
	function onKeyDownPoblacionSelect(e) {
		var tecla = (document.all) ? e.keyCode : e.which;
		//msgControl=msgControl+"onKeyDownPoblacionSelect("+tecla+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput 	= document.getElementById("poblacion_input");
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		
		switch(tecla) {		
	  		case 8: //BACKSPACE: Anulamos la accion del retroceso en el desplegable
	  			e.returnValue = false; 
	  			e.cancelBubble = true; 
	  			return false;		  		
	  			break;
		
			case 13: //INTRO: Paso el foco al filtro
				bControl=true; // Inhabilita onFocusPoblacionInput()				
				elementoPoblacionInput.focus(); //Invoca onBlurPoblacionSelect() + onFocusPoblacionInput()
				break;
				
			case 27: //ESC: Paso el foco al filtro
				elementoPoblacionSelect.val([]); // Elimino los elementos seleccionados del desplegable
				
				bControl=true; // Inhabilita onFocusPoblacionInput()				
				elementoPoblacionInput.focus(); //Invoca onBlurPoblacionSelect() + onFocusPoblacionInput()
				break;							
  		}		
	}
	
	// Necesario para firefox
	function onKeyPressPoblacionSelect(e) {
		var tecla = (document.all) ? e.keyCode : e.which; 
		//msgControl=msgControl+"onKeyPressPoblacionSelect("+tecla+")\n";alert(msgControl);
		
		switch(tecla) {			
		  	case 8: //BACKSPACE: Anulamos la accion del retroceso en el desplegable
		  		e.returnValue = false; 
		  		e.cancelBubble = true; 
		  		return false;		  		
  				break;
  		}		
	}									
	
	// Se realiza cuando obtiene el foco del filtro
	function onFocusPoblacionInput() {
		//msgControl=msgControl+"onFocusPoblacionInput()\n";alert(msgControl);
		bFocoPoblacionInput=true;
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput = document.getElementById("poblacion_input");
		
		// Un control y actualizo el desplegable segun el contenido del filtro
		if (bControl) {
			bControl=false;
			
		} else {
			actualizarPoblacionesCPPoblacion(elementoPoblacionInput.value);
		}
	}			
		
	// Se realiza cuando abandona el foco del filtro
	function onBlurPoblacionInput() {
		//msgControl=msgControl+"onBlurPoblacionInput()\n";alert(msgControl);
		bFocoPoblacionInput=false;
		
		// Un control, ocultro el filtro y calculo el valor final del filtro
		if (bControl) {
			bControl=false;
			
		} else {
			// Obtiene los elementos que vamos a utilizar
			var elementoPoblacionDiv = jQuery("#poblacion_div");
			
			elementoPoblacionDiv.hide();
			onChangePoblacionSelect(true);						
		}
	}
	
	// Se realiza cuando termina de cargar la letra en el filtro
	function onKeyUpPoblacionInput() {
		//msgControl=msgControl+"onKeyUpPoblacionInput()\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput = document.getElementById("poblacion_input");
		
		var valorFiltro = elementoPoblacionInput.value;
		
		// Un control y actualizo el desplegable segun el contenido del filtro
		if (bControlKey) {
			bControlKey=false;
			setTimeout(function(){controlarPoblacion(valorFiltro);},tiempoEspera,"Javascript");
		}
	}	
	
	// Sea realiza antes de cargar la letra en el filtro
	function onKeyDownPoblacionInput(e) {		
		var tecla = (document.all) ? e.keyCode : e.which;
		//msgControl=msgControl+"onKeyDownPoblacionInput("+tecla+")\n";alert(msgControl);
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput 	= document.getElementById("poblacion_input");
		var elementoPoblacionDiv 	= jQuery("#poblacion_div");
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		
		txtTabulador="";
		
		switch(tecla) {		  				
  			case 9: //TABULADOR
  				txtTabulador=elementoPoblacionInput.value;
  				elementoPoblacionDiv.hide();   				
  				break;  
  				
			case 13: //INTRO: Paso el foco al filtro
				elementoPoblacionDiv.hide();
				break;  				
  				
			case 27: //ESC: Paso el foco al filtro
				elementoPoblacionSelect.val([]); // Elimino los elementos seleccionados del desplegable
				elementoPoblacionDiv.hide();
				elementoPoblacionInput.value="";
				break;  
				
			case 38: //CURSOR ARRIBA
			case 40: //CURSOR ABAJO
				//actualizarPoblacionesCPPoblacion(elementoPoblacionInput.value); // No esta claro que sea necesario
				if (numPoblaciones>1) {						
					bControl=true;  // Inhabilita onBlurPoblacionInput()					
					
					// Esto es util, para evitar KeyDown en el desplegable
					setTimeout(function(){elementoPoblacionDiv.show();elementoPoblacionSelect.focus();},10,"Javascript");
					//elementoPoblacionDiv.show();
					//elementoPoblacionSelect.focus(); //Invoca onBlurPoblacionInput()
				}
  				break;					
  				
  			default:
  				bControlKey=true; // Inhabilita onKeyUpPoblacionInput() 
  				break;				
  		}
	}		
	
	// Controla el estado tras un tiempo de espera
	function controlarPoblacion(filtroAntiguo) {
		//msgControl=msgControl+"controlPoblacion("+filtroAntiguo+")\n";alert(msgControl);

		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionInput = document.getElementById("poblacion_input");
		
		if (filtroAntiguo==elementoPoblacionInput.value || filtroAntiguo==txtTabulador) {
			actualizarPoblacionesCPPoblacion(filtroAntiguo);
		}
	}			
			 	
	// Actualizo el desplegable segun el contenido del filtro y del codigo postal
	function actualizarPoblacionesCPPoblacion(valorPoblacion){
		//msgControl=msgControl+"actualizarPoblacionesCPPoblacion("+valorPoblacion+")\n";alert(msgControl);			
		
		// Obtiene los elementos que vamos a utilizar
		var elementoPoblacionDiv 	= jQuery("#poblacion_div");
		var elementoPoblacionSelect = jQuery("#poblacion_select");
		var elementoPoblacionHidden = document.getElementById("poblacion");
		var elementoCP 				= document.getElementById("CP");				
		var valorCP 				= elementoCP.value;
		
		if (!validaCP(valorCP)) {
			bControl=true; // Inhabilita onBlurPoblacionInput()
			elementoCP.focus(); //Invoca onBlurPoblacionInput()
			
		} else {		
			// Si no tiene filtro, lanzo la busqueda por codigo postal
			if (valorPoblacion=="") {
				elementoPoblacionSelect.val([]); // Elimino los elementos seleccionados del desplegable
				actualizarPoblacionesCP(false);
	
			} else {		
				// Controlo una longitud minima de caracteres
				if (valorPoblacion.length>=numMinLetrasFiltro) {	 
									
					// Control de repeticion
					if (controlFiltro!=valorPoblacion) {			
						controlFiltro=valorPoblacion;
				 		
						jQuery.ajax({ 
							type: "POST",
							url: "/SIGA/CEN_Poblaciones.do?modo=getAjaxPoblacionesCPFiltro",				
							data: "valorCP="+valorCP+"&valorPoblacion="+valorPoblacion+"&valorGuiones="+valorGuiones+"&numMaxPoblaciones="+numMaximoSelectPoblaciones,
							dataType: "json",
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							success: function(json){					
									
								// Con esta comparacion y por hacer AJAX, nos evitamos hacer codigo
								if (valorPoblacion==controlFiltro) {					
									
									// Recupera el numero de poblaciones
									numPoblaciones = json.numPoblaciones;		
									
									// Vuelvo a obtener el objeto
									elementoPoblacionDiv = jQuery("#poblacion_div");
									
									// Pinta el desplegable con el contenido devuelto por ajax
									var htmlFinal=elementoPoblacionDiv[0].innerHTML;
									htmlFinal.replace("\"","'");
									htmlFinal=htmlFinal.substring(0, htmlFinal.indexOf('>')+1);
									elementoPoblacionDiv[0].innerHTML=htmlFinal+json.htmlPoblaciones[0]+"</SELECT>";
									
									// Vuelvo a obtener el objeto
									elementoPoblacionSelect = jQuery("#poblacion_select");
			    	        	    
						   	    	// Si solo tiene el elemento de seleccion, oculta el desplegable
					        		if (numPoblaciones<2) {
					        			elementoPoblacionDiv.hide();
				        	
					        		} else {    	    		      
					       				// Cargo el numero de lineas que tiene que mostrar el desplegable
			    	        			if (numPoblaciones>numLineasSelectPoblaciones) {
			    	        				elementoPoblacionSelect[0].size=numLineasSelectPoblaciones;
			    	        			} else {
			    	        				elementoPoblacionSelect[0].size=numPoblaciones;
			    	        			}
					    			
			    	        			// Si solo hay un elemento, quitando el de seleccion, lo selecciono
					    				if (numPoblaciones==2) {
					    					elementoPoblacionSelect[0].options[1].selected=true;
					    				}
					    				
					    				// Control de si se debe mostrar el desplegable
				    					if (bFocoPoblacionInput) {
				    						elementoPoblacionDiv.show();
				    					} else {
				    						elementoPoblacionDiv.hide();
				    					}
			    					}   		    	
					        		
						   	    	// Actualizo los datos seleccionados
									onChangePoblacionSelect(!bFocoPoblacionInput);
								
								} else { // valorPoblacion!=controlFiltro
									if (numPoblaciones>1 && bFocoPoblacionInput) {
										elementoPoblacionDiv.show();	
									} else {
										elementoPoblacionDiv.hide();
									}		
								}				    	       		       				       				    		
								fin();
							},
							error: function(e){
								alert(mensajeGeneralError);
								fin();
							}
						});
						
					// Si no ha cambiado el filtro, lo muestro cuando tenga elementos
					} else { // controlFiltro==valorPoblacion
						if (numPoblaciones>1 && bFocoPoblacionInput) {
							elementoPoblacionDiv.show();	
						} else {
							elementoPoblacionDiv.hide();
						}		
					}
					
				} else { // valorPoblacion.length<numMinLetrasFiltro	 
					elementoPoblacionDiv.hide();
					elementoPoblacionSelect[0].innerHTML="";							
					elementoPoblacionHidden.value="";
					
					controlFiltro=valorPoblacion;
					numPoblaciones=0;
	
					if (elementoCP.value=="") {
						var elementoProvinciaHidden = document.getElementById("provincia");
						var elementoProvinciaInput	= document.getElementById("provincia_input");
	
						elementoProvinciaHidden.value="";
						elementoProvinciaInput.value="";
					}												
				}	
			}
		}
	}	
	
	// Actualizo el desplegable segun el contenido del codigo postal
	function actualizarPoblacionesCP(bCP){
		//msgControl=msgControl+"actualizarPoblacionesCP("+bCP+")\n";alert(msgControl);

		var valorCP 				= document.getElementById("CP").value;	
		var elementoPoblacionInput 	= jQuery("#poblacion_input");
		var elementoPoblacionDiv 	= jQuery("#poblacion_div");	

		// Control de repeticion
		if ((bCP&&controlCP!=valorCP)||(!bCP&&controlFiltro!="")) {
			
			if (bCP)
				controlCP=valorCP;
			else
				controlFiltro="";
			
			if (elementoPoblacionInput[0].disabled) {
				elementoPoblacionInput.removeAttr('disabled');
				elementoPoblacionInput.removeClass().addClass('box');
			}
	 		
			jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/CEN_Poblaciones.do?modo=getAjaxPoblacionesCP",				
				data: "valorCP="+valorCP,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){							
						
					// Recupera el numero de poblaciones
					numPoblaciones = json.numPoblaciones;		
					
					// Vuelvo a obtener el objeto
					elementoPoblacionDiv = jQuery("#poblacion_div");
					
					// Pinta el desplegable con el contenido devuelto por ajax
					var htmlFinal=elementoPoblacionDiv[0].innerHTML;
					htmlFinal.replace("\"","'");
					htmlFinal=htmlFinal.substring(0, htmlFinal.indexOf('>')+1);
					elementoPoblacionDiv[0].innerHTML=htmlFinal+json.htmlPoblaciones[0]+"</SELECT>";
					
					// Vuelvo a obtener el objeto
					var elementoPoblacionSelect = jQuery("#poblacion_select");												
					elementoPoblacionInput = document.getElementById("poblacion_input");
					
					document.getElementById("provincia").value = json.idProvinciaCp;
					document.getElementById("provincia_input").value = json.nombreProvinciaCp;						
   	        	    
		   	    	// Si solo tiene el elemento de seleccion, oculta el desplegable
	        		if (numPoblaciones<2) {
	        			elementoPoblacionDiv.hide();
        		        		
	       			} else {
	       				// Si viene por introducir un codigo postal debe seleccionarse
	       				// Si tengo estoy en el filtro y borro el nombre de la poblacion ... no debe seleccionarse, porque si solo tiene una no puedo borrar
	       				if (numPoblaciones==2 && bCP) {
	    					elementoPoblacionSelect[0].options[1].selected=true;	
	    					elementoPoblacionSelect[0].size=numPoblaciones;
	    					
    						elementoPoblacionInput.value=elementoPoblacionSelect[0].options[elementoPoblacionSelect[0].selectedIndex].text;
    						elementoPoblacionDiv.hide();
		        		
		       			} else {    	    		      
		       				// Cargo el numero de lineas que tiene que mostrar el desplegable
	   	        			if (numPoblaciones>numLineasSelectPoblaciones) {
	   	        				elementoPoblacionSelect[0].size=numLineasSelectPoblaciones;
	   	        			} else {
	   	        				elementoPoblacionSelect[0].size=numPoblaciones;
	   	        			}
		    				
		       				if (bFocoCP || bFocoPoblacionInput) {
	   							elementoPoblacionDiv.show();
		       				} else {
		       					elementoPoblacionDiv.hide();
		       				}
	   					}		       						       				
	       			}
		   	    	
	        		// Actualizo los datos seleccionados
					onChangePoblacionSelect(false);
					fin();
				},
				error: function(e){
					alert(mensajeGeneralError);
					fin();
				}
			});
		
		// Si no ha cambiado el filtro, lo muestro cuando tenga elementos
		} else {
			if (numPoblaciones>1 && (bFocoCP || bFocoPoblacionInput)) {
				elementoPoblacionDiv.show();	
			} else {
				elementoPoblacionDiv.hide();
			}		
		} 
	} 		
	
	jQuery.noConflict();
	jQuery(document).ready(function() {
		cargaPais(<%=datosPersonales.getIdPais() %>);
  	});	
</script>

<body class="tablaCentralCampos" onLoad="cargarChecksCuenta(); comprobarTipoIdent(); cargarBancos();">
	<bean:define id="isPosibilidadSolicitudAlta" name="isPosibilidadSolicitudAlta"  scope="request" />
	<bean:define id="mostrarSolicitudAlta" name="mostrarSolicitudAlta"  scope="request" />
	<bean:define id="motivoSolicitudAlta" name="motivoSolicitudAlta"  scope="request" />

	<html:form action="/CEN_MantenimientoSolicitudesIncorporacion.do" method="POST" target="mainWorkArea">
		<html:hidden property="idSolicitudPlanProfesional"/>
		<html:hidden property="idSolicitudAceptadaSeguroUniversal"/>
		<html:hidden property="idSolicitudAceptadaPlanProfesional"/>
		<html:hidden property="idSolicitudSeguroUniversal"/>
		<input type="hidden" id="numeroIdentificacionBBDD" value ="<%=datosPersonales.getNumeroIdentificador()%>" /> 
		<input type="hidden" id="fechaNacimientoBBDD" value ="<%=datosPersonales.getFechaNacimiento()%>" />	
		
		<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="labelText" >
					<center>
							<siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.numeroSolicitud"/>:
							&nbsp;&nbsp;
							<% if(datosPersonales.getIdSolicitud()!=null)
									out.print(UtilidadesString.mostrarDatoJSP(datosPersonales.getIdSolicitud()));
							%>
							&nbsp;&nbsp;
							<siga:Idioma key="messages.censo.solicitudIncorporacion.guardarCodigo"/>	
					</center>
				</td>
			</tr>
		</table>
		
		<siga:ConjCampos>
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacionDatos.literal.estado'/>
					</td>
					<%if (readonly || datosPersonales.getIdEstado().intValue()==ClsConstants.ESTADO_SOLICITUD_APROBADA) {%>
						<td class="labelTextValue">
							<%=UtilidadesString.mostrarDatoJSP(estadoSolicitud)%>
							<html:hidden property="editarEstadoSolicitud" value="<%=datosPersonales.getIdEstado().toString()%>"  />
						</td>
					<%} else {%>
						<td>
							<siga:ComboBD nombre = "editarEstadoSolicitud" tipo="estadoSolicitud" clase="<%=estiloCombo%>" elementoSel="<%=idEstadoSolicitud%>" obligatorioSinTextoSeleccionar="true" />
						</td>
					<%}%>	
					
					<td style="width:10px">&nbsp;</td>
					
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacionDatos.literal.fechaEstado'/>
					</td>
					<td class="labelText">
						<html:text property="fechaEstado" size="10" styleClass="boxConsulta" tabindex="-1" readonly="true"
							value="<%=datosPersonales.getFechaEstado()%>"/>
					</td>	
					
					<td style="width:10px">&nbsp;</td>
					
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.observaciones'/>
					</td>

					<td rowspan="2">
						<textarea class="<%=estiloBox%>" name="observaciones" 
							<%if (readonly) {%> 
								readonly
							<%} else {%>
								onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
							<%}%>	
							style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"
						><%=datosPersonales.getObservaciones()%></textarea>
					</td>
				</tr>
				
				<tr>
					<td colspan="3"> &nbsp;</td>
					
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.fechaSolicitud'/>
					</td>
					<td class="labelText">
						<html:text property="fechaSolicitud" size="10" styleClass="boxConsulta" tabindex="-1" readonly="true"
							value="<%=datosPersonales.getFechaSolicitud()%>"/>
					</td>												
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">
			<table border="0" cellpadding="5" cellspacing="0">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "editarIdSolicitud" value = "<%=datosPersonales.getIdSolicitud().toString()%>"/>
				<html:hidden property = "continuarAprobacion" value = ""/>
				<html:hidden property = "continuarInsercionColegiado" value = ""/>
		
				<tr>
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.solicitudDe'/>&nbsp;(*)
								</td>
								<%if(readonly){ %>
									<td class="labelTextValue">
										<%=UtilidadesString.mostrarDatoJSP(tipoSolicitud)%>
									</td>
								<%}else{%>
									<td>
										<siga:ComboBD nombre="tipoSolicitud" tipo="solicitud" ancho="200" 
											clase="boxCombo" elementoSel="<%=selSolicitud%>" obligatorio="true"/>
									</td>
								<%}%>
							</tr>
						</table>
					</td>
					
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.tipoColegiacion'/>&nbsp;(*)
								</td>
								<%if(readonly){ %>
									<td class="labelTextValue">
										<%=UtilidadesString.mostrarDatoJSP(tipoColegiacion)%>
									</td>
								<%}else{%>
									<td>
										<siga:ComboBD nombre="tipoColegiacion" tipo="colegiacion" ancho="130" 
											clase="boxCombo" elementoSel="<%=selColegiacion%>" obligatorio="true"/>
									</td>
								<%}%>
							</tr>
						</table>
					</td>									

					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.documentacion'/>&nbsp;(*)
					</td>						
					<%if(readonly){ %>
						<td class="labelTextValue">
							<%=UtilidadesString.mostrarDatoJSP(modalidadDocumentacion)%>
						</td>
					<%}else{%>
						<td>
							<siga:ComboBD nombre="tipoModalidadDocumentacion" tipo="modalidadDocumentacion" ancho="170" 
								clase="boxCombo" obligatorio="true" elementoSel="<%=modalidadSel%>" parametro="<%=modalidadParam%>"/>
						</td>
					<%}%>
				</tr>

				<tr>
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key='censo.busquedaClientesAvanzada.literal.fechaIncorporacion'/>
								</td>
								<%if(readonly){%>
									<td class="labelTextValue">
										<%=UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaEstadoColegial())%>
									</td>
								<%}else{%>
									<td>
										<siga:Fecha nombreCampo="fechaEstadoColegial" valorInicial="<%=datosPersonales.getFechaEstadoColegial() %>" />
									</td>
								<%}%>
							</tr>
						</table>
					</td>
					
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>						
								<td class="labelText">
									<siga:Idioma key='censo.consultaDatosColegiacion.literal.residente'/>
								</td>
								<td>
									<html:checkbox property="residente" disabled="<%=readonly%>"/>
								</td>
							</tr>
						</table>
					</td>									
				
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacionDatos.literal.nColegiado'/>
					</td>
					<td>
						<%if(!readonly && (nColegiado==null || nColegiado.equalsIgnoreCase(""))){%>
							<html:text styleId="numColBox" property="numeroColegiado" style="vertical-align:middle;width:100" maxlength="20" styleClass="boxDisabled" disabled="true" onchange="existeNColegiado()"/>
							<img id="botonNCol" src="<html:rewrite page='/html/imagenes/candado.gif'/>" border="0" onclick="editarNColegiado()" style="cursor:hand;align:left;display:inline;" title="<siga:Idioma key='censo.SolicitudIncorporacion.message.desbloqueoNcolegiado'/>">
						<%}else{%>
							<html:text styleId="numColBox" property="numeroColegiado" style="vertical-align:middle;width:100" maxlength="20" styleClass="<%=estiloBox%>" value="<%=nColegiado%>"  readOnly="<%=readonly%>" onchange="existeNColegiado()"/>
							<img id="botonNCol" src="<html:rewrite page='/html/imagenes/candado.gif'/>" border="0" onclick="editarNColegiado()" style="cursor:hand;align:left;visibility:hidden;display:inline;" title="<siga:Idioma key='censo.SolicitudIncorporacion.message.desbloqueoNcolegiado'/>">
						<%}%>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos>
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.nifcif'/>&nbsp;(*)
					</td>
					<td colspan="6" style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>									
								<%if(readonly){%>	
									<td class="labelTextValue">						
										<siga:ComboBD nombre="tipoIdentificacion" tipo="cmbTipoIdentificacion" ancho="100" 
											clase="<%=estiloCombo%>" elementoSel="<%=selIdent%>" 
											readOnly="<%=sreadonly%>" obligatorio="true"/>	
										<%=UtilidadesString.mostrarDatoJSP(datosPersonales.getNumeroIdentificador())%>											
									</td>	
								<%}else{%>
									<td style="vertical-align: middle;">
										<siga:ComboBD nombre = "tipoIdentificacion" tipo="identificacionSolicitud" ancho="100"
											clase="<%=estiloCombo%>" elementoSel="<%=selIdent%>" 
											readOnly="<%=sreadonly%>" obligatorio="true" accion="comprobarTipoIdent();"/>
									</td>
									<td style="vertical-align: middle;">
										<html:text property="NIFCIF" styleClass="box" size="25" maxlength="20" value="<%=datosPersonales.getNumeroIdentificador() %>"/>
									</td>
									<td style="vertical-align: middle;">
										<img id="idButtonNif" src="<html:rewrite page='/html/imagenes/comprobar.gif'/>" border="0" onclick="obtenerLetra();" style="cursor:hand;align:left;display:inline;visibility:hidden;">
									</td>
								<%}%>
							</tr>
						</table>
					</td>
				</tr>
				
				<tr>		
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.tratamiento'/>&nbsp;(*)
					</td>
					
					<%if(readonly){%>
						<td class="labelTextValue">
							<%=UtilidadesString.mostrarDatoJSP(tipoTratamiento)%>
						</td>
					<%}else{%>
						<td>
							<siga:ComboBD nombre="tipoDon" tipo="tratamiento" ancho="100"
								clase="<%=estiloCombo%>" elementoSel="<%=selTratamiento %>" 
								readOnly="<%=sreadonly%>"  obligatorio="true" />
						</td>
					<%}%>						
					
					<td class="labelText">
						<siga:Idioma key='censo.SolicitudIncorporacion.literal.nombre'/>&nbsp;(*)
					</td>
					<td>
						<html:text property="nombre" style="width:170" maxlength="100" value="<%=datosPersonales.getNombre()%>" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>"/>
					</td>		
					
					<td class="labelText">
						<siga:Idioma key='censo.consultaDatosGenerales.literal.apellidos'/>&nbsp;(*)
					</td>
					<td>
						<html:text property="apellido1"  style="width:170" maxlength="100" value="<%=datosPersonales.getApellido1()%>" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"/>
					</td>
					<td>
						<html:text property="apellido2"  style="width:170" maxlength="100" value="<%=datosPersonales.getApellido2()%>" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"/>
					</td>
				</tr>
				
				<tr>
					<td colspan="7" style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>			
								<td class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.fechaNacimiento'/>&nbsp;(*)
								</td>						
								<%if(readonly){%>
									<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaNacimiento())%></td>
								<%}else{%>
									<td>
										<siga:Fecha nombreCampo="fechaNacimiento" valorInicial="<%=datosPersonales.getFechaNacimiento() %>" />
									</td>
								<%}%>				
								
								<td style="width:13px">&nbsp;</td>												
											
								<td class="labelText">
									<siga:Idioma key='censo.consultaDatosGenerales.literal.sexo'/>&nbsp;(*)
								</td>
								<%if (readonly){%>
									<td class="labelTextValue">
										<%if(datosPersonales.getSexo()!=null)out.print(UtilidadesString.mostrarDatoJSP(ssexo));%>
									</td>
								<%}else{%>
									<td>
										<html:select name="SolicitudIncorporacionForm" property="sexo" value="<%=datosPersonales.getSexo()%>" 
											style="width:75px" styleClass="box" >
									        <html:option value="0" >&nbsp;</html:option>
											<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE%>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
											<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER%>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
										</html:select>	
									</td>
								<%}%>
								
								<td style="width:13px">&nbsp;</td>
								
								<td class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.estadoCivil'/>
								</td>
								
								<%if(readonly){%>
									<td class="labelTextValue">
										<%=UtilidadesString.mostrarDatoJSP(tipoEstadoCivil)%>
									</td>											
								<%}else{%>
									<td>
										<siga:ComboBD nombre="estadoCivil" tipo="estadoCivil" ancho="100"
											clase="<%=estiloCombo%>" readOnly="<%=sreadonly%>" elementoSel="<%=selEstadoCiv%>"/>
									</td>
								<%}%>									
								
								<td style="width:13px">&nbsp;</td>
		
								<td class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.naturalDe'/>
								</td>
								<td>
									<html:text property="natural" style="width:180" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" value="<%= datosPersonales.getNaturalDe() %>"/>
								</td>
							</tr>
						</table>
					</td>						
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>				
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key='censo.datosDireccion.literal.pais2'/>
								</td>	
															
								<%if(readonly){%>
									<td class="labelTextValue">
										<%=UtilidadesString.mostrarDatoJSP(pais)%>
									</td>
								<%}else{%>
									<td>
										<siga:ComboBD nombre="pais" tipo="pais" ancho="420" clase="<%=estiloCombo%>" obligatorio="false" elementoSel="<%=selPais%>" readOnly="<%=sreadonly%>" accion="cargaPais(this.value);"/>
									</td>
								<%}%>
							</tr>
							
							<tr>
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.domicilio'/>&nbsp;(*)
								</td>									
								<td>
									<html:text styleClass="<%=estiloBox%>" property="domicilio" readOnly="<%=readonly%>"
										 style="width:420px" maxlength="100" value="<%=datosPersonales.getDomicilio()%>"/>
								</td>															
							</tr>						
							
							<tr>
								<td colspan="2" style="padding:0px">
									<table border="0" cellpadding="5" cellspacing="0">
										<tr id="trEspanol1">
											<td class="labelText">
												CP&nbsp;(*)
											</td>
											<td>	
												<%if(readonly){%>
													<input type="text" value="<%=datosPersonales.getCodigoPostal()%>" style="width:50px" class="boxConsulta" readonly />
												<%}else{%>
													<html:text property="CP" style="width:50px" styleClass="<%=estiloBox%>" maxlength="5" value="<%=datosPersonales.getCodigoPostal()%>" readOnly="<%=readonly%>"
														onkeypress="return soloDigitos(event);" onkeydown="onKeyDownCP();" onkeyup="onKeyUpCP();"
														onblur="onBlurCP();" onfocus="onFocusCP();"/>
												<%}%>
											</td>					
										
											<td class="labelText">
												<siga:Idioma key='censo.SolicitudIncorporacion.literal.poblacion'/>&nbsp;(*)
											</td>																		
																	
											<td>
												<%if(readonly){%>
													<input type="text" value="<%=poblacion%>" style="width:310px;" class="boxConsulta" readonly />
												<%}else{%>
													<html:hidden property="poblacion" value="<%=idPoblacion%>"/>
													<input class="box" id="poblacion_input" type="text" style="width:310px;" value="<%=poblacion%>" maxlength="100"
														onblur="onBlurPoblacionInput();" onkeydown="onKeyDownPoblacionInput(event);" onkeyup="onKeyUpPoblacionInput();" 
														onfocus="onFocusPoblacionInput();" />
													<div id="poblacion_div">
														<select class="box" style="width:310px" id="poblacion_select"
															onblur="onBlurPoblacionSelect();" onchange="onChangePoblacionSelect(true);" onclick="onClickPoblacionSelect();" 
															onkeypress="onKeyPressPoblacionSelect(event);" onkeydown="onKeyDownPoblacionSelect(event);" onmouseover="onMouseDownPoblacionSelect();" onmousedown="onMouseDownPoblacionSelect();">																		
														</select>					
													</div>	
												<%}%>
											</td>
										</tr>
										
										<tr id="trEspanol2" style="height:38px">
											<html:hidden property="provincia" value="<%=idProvincia%>"/>
											<td colspan="2">
												<siga:ToolTip id='idAyudaPoblaciones' imagen="/SIGA/html/imagenes/botonAyuda.gif" texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "censo.SolicitudIncorporacion.ayudaPoblaciones"))%>' />
											</td>  																																								

											<td class="labelText">
												<siga:Idioma key='censo.SolicitudIncorporacion.literal.provincia'/>									
											</td>
											<td class="labelText">
												<input id="provincia_input" class="boxConsulta" type="text" style="width:310px"  
													value="<%=provincia%>" readonly tabindex="-1" />
											</td>
										</tr>
										
										<tr id="trExtranjero" style="display:none">
											<td class="labelText">
												CP&nbsp;(*)
											</td>											
											<td>	
												<html:text property="CPExt" style="width:50px" styleClass="<%=estiloBox%>" maxlength="5" value="<%=datosPersonales.getCodigoPostal()%>" readOnly="<%=readonly%>"/>
											</td>						
										
											<td class="labelText">
												<siga:Idioma key='censo.SolicitudIncorporacion.literal.poblacion'/>&nbsp;(*)
											</td>																																													
											<td>
												<html:text styleId="poblacionExt" property="poblacionExt" style="width:310px" maxlength="100" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getPoblacionExtranjera()%>" readOnly="<%=readonly%>"/>
											</td>
										</tr>								
									</table>
								</td>			
							</tr>
						</table>
					</td>
					
					<td>&nbsp;</td>
					
					<td style="vertical-align: middle;">							
						<table>
							<tr>
								<td style="height:80px;" class="linea">&nbsp;</td>
							</tr>								
						</table>
					</td>

					<td>
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono1'/>&nbsp;(*)
								</td>									
								<td>
									<html:text property="telefono1" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getTelefono1()%>" readOnly="<%=readonly%>"/>
								</td>
								
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono2'/>
								</td>									
								<td>
									<html:text property="telefono2" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getTelefono2()%>" readOnly="<%=readonly%>"/>
								</td>
							</tr>
							
							<tr>
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.fax1'/>
								</td>									
								<td>
									<html:text property="fax1" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getFax1()%>" readOnly="<%=readonly%>"/>
								</td>
								
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.fax2'/>
								</td>
								<td>
									<html:text property="fax2" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getFax2()%>" readOnly="<%=readonly%>"/>
								</td>		
							</tr>
							
							<tr>
								<td  class="labelText">
									<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono3'/>						
								</td>
								<td colspan="3">
									<html:text property="telefono3" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getMovil()%>" readOnly="<%=readonly%>"/>
								</td>	
							</tr>
							
							<tr>
								<td colspan="4" style="padding:0px;">
									<table border="0" cellpadding="5" cellspacing="0">
										<td  class="labelText">
											<siga:Idioma key='censo.SolicitudIncorporacion.literal.email'/>&nbsp;(*)		
										</td>
										<td>
											<html:text property="mail" style="width:270px" maxlength="100" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getCorreoElectronico()%>" readOnly="<%=readonly%>"/>
										</td>
									</table>
								</td>
							</tr>				
						</table>
					</td>
				</tr>
				
				<!-- RGG: cambio a formularios ligeros -->
				<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD" />
				<input type="hidden" name="actionModal" value="" />
			</table>
		</siga:ConjCampos>
			
		<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera">
			<table border="0" cellpadding="5" cellspacing="0">	
				<tr>		
					<td class="labelText">
						<siga:Idioma key='censo.datosCuentaBancaria.literal.titular'/>
					</td>																
					<td>
						<html:text property="titular" value="<%=titular%>" size="50" styleClass="<%=estiloBox%>" maxlength="100" readOnly="<%=readonly%>"/>
					</td>
												
					<td style="width:20px">&nbsp;</td>
				
					<td class="labelText">
						<siga:Idioma key='censo.tipoCuenta.cargo'/>
					</td>
					
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td>
									<html:checkbox property="cuentaCargo"  disabled="<%=readonly%>"/>
								</td>	
								
								<td style="width:20px">&nbsp;</td>								
																					
								<td class="labelText">
									<siga:Idioma key='censo.tipoCuenta.abono'/>
								</td>									
								<td>
									<html:checkbox property="cuentaAbono" disabled="<%=readonly%>" onClick="validaAbonoSJCS()"/>
								</td>
								
								<td style="width:20px">&nbsp;</td>
								
								<td class="labelText">
									<siga:Idioma key='censo.datosCuentaBancaria.literal.abonoSJCS'/>
								</td>									
								<td>
									<html:checkbox property="abonoSJCS" disabled="<%=readonly%>" onClick="validaAbonoSJCS()" />
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr>					
					<td class="labelText">
						Cuenta
					</td>						
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>								
								<td>
									<html:text size="4"  maxlength="4" property="cbo_Codigo"     value="<%=cbo_Codigo%>" 				styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" onChange="cargarBancos();"/>
									- <html:text size="4"  maxlength="4" property="codigoSucursal" value="<%=cuentaCodigoSucursal%>" 	styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"/>
									- <html:text size="2"  maxlength="2" property="digitoControl"  value="<%=cuentaDigitoControl%>" 	styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"/>
									- <html:text size="10" maxlength="10" property="numeroCuenta"  value="<%=cuentaNumeroCuenta%>" 		styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"/>
								</td>
							</tr>
						</table>
					</td>
					
					<td style="width:20px">&nbsp;</td>
					
					<td class="labelText">
						<siga:Idioma key='censo.datosCuentaBancaria.literal.banco'/>
					</td>
					<td>
						<input id="banco" type="text" style="width:400px;" class="boxConsulta" 
							readonly tabindex="-1" />
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<c:if test="${ModoAnterior=='Editar'||ModoAnterior=='VER'||ModoAnterior=='editar' }">
			<c:choose>
				<c:when test="${mostrarSolicitudAlta==true}">
					<siga:ConjCampos>
						<c:choose>
							<c:when test="${isPosibilidadSolicitudAlta==true }">
								<table class="tablaCampos" align="left">		
									<tr>
										<td class="labelText" >
											<html:button property="idButton" onclick="return accionSolicitarAltaAlterMutua();" styleClass="button">
												<siga:Idioma key="general.boton.solicitarCompra" />
											</html:button>
											<siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.planProfesional"/>
										</td>
										
										<c:choose>
											<c:when test="${SolicitudIncorporacionForm.idSolicitudPlanProfesional==null||SolicitudIncorporacionForm.idSolicitudPlanProfesional==''}">
												<td id="tdBotonSolicitudPlanProfesional" >
													<%if(modoAnterior.equalsIgnoreCase("Editar")){ %>
														<html:button property="idButton" onclick="return accionSolicitarAltaMutualidad('P');" styleClass="button">
															<siga:Idioma key="general.boton.solicitarCompra" />
														</html:button>
													<%}else{ %>
														<html:button property="idButton" styleClass="button">
															<siga:Idioma key="general.boton.solicitarCompra" />
														</html:button>
													<%} %>
												</td>
												
												<td id="tdTextoNSolicitudPlanProfesional" style="display:none" class="labelText" >N�&nbsp;Solicitud:</td>
												<td id="tdIdSolicitudPlanProfesional" class="labelTextValor">&nbsp;</td> 
												<td id="tdEstadoSolicitudPlanProfesional" class="labelTextValor" style="color:blue;">&nbsp;</td>
												<td id="tdBotonEstadoSolicitudPlanProfesional" style="display:none" > 
													<html:button property="idButton" onclick="return accionComprobarEstadoMutualidad('P');" styleClass="button">Comprobar Estado</html:button>
												</td>
							
											</c:when>
											
											<c:otherwise>
												<td class="labelText" >N�&nbsp;Solicitud:</td>
												<td class="labelTextValor">						
													<c:choose>
														<c:when test="${SolicitudIncorporacionForm.idSolicitudAceptadaPlanProfesional!=null}">
															<c:out value="${SolicitudIncorporacionForm.idSolicitudAceptadaPlanProfesional}" />
														</c:when>
														
														<c:otherwise>
															<c:out value="${SolicitudIncorporacionForm.idSolicitudPlanProfesional}" />
														</c:otherwise>
													</c:choose>						
												</td>
						
												<td id="tdEstadoSolicitudPlanProfesional" class="labelTextValor" style="color:blue;"><c:out value="${SolicitudIncorporacionForm.estadoSolicitudPlanProfesional}" /></td>
												<td id="tdBotonEstadoSolicitudPlanProfesional">
													<html:button property="idButton" onclick="return accionComprobarEstadoMutualidad('P');" styleClass="button"> Comprobar Estado</html:button>
												</td>
											</c:otherwise>
										</c:choose>
									</tr>
					
									<tr>
										<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.seguroAccidentes"/></td>
					
										<c:choose>
											<c:when test="${SolicitudIncorporacionForm.idSolicitudSeguroUniversal==null||SolicitudIncorporacionForm.idSolicitudSeguroUniversal==''}">
												<td id="tdBotonSolicitudSeguroUniversal">
													<%if(modoAnterior.equalsIgnoreCase("Editar")){ %>
														<html:button id="botonSolicitarAltaSeguro" property="idButton"onclick="return accionSolicitarAltaMutualidad('S');" styleClass="button">
															<siga:Idioma key="general.boton.solicitarCompra" />
														</html:button>
													<%}else{ %>
														<html:button property="idButton" styleClass="button">
															<siga:Idioma key="general.boton.solicitarCompra" />
														</html:button>
													<%} %>
												</td>
												<td id="tdTextoNSolicitudSeguroUniversal"  style="display:none" class="labelText" >N�&nbsp;Solicitud:</td>
												<td id="tdIdSolicitudSeguroUniversal" class="labelTextValor">&nbsp;</td>
												<td id="tdEstadoSolicitudSeguroUniversal" class="labelTextValor" style="color:blue;">&nbsp;</td>
												<td id="tdBotonEstadoSolicitudSeguroUniversal" style="display:none">
													<html:button property="idButton" onclick="return accionComprobarEstadoMutualidad('S');" styleClass="button">Comprobar Estado</html:button>
												</td>
												<td colspan="2">&nbsp;</td>
											</c:when>
										
											<c:otherwise>
												<td class="labelText" >N�&nbsp;Solicitud:</td>
												<td class="labelTextValor" >
													<c:choose>
														<c:when test="${SolicitudIncorporacionForm.idSolicitudAceptadaSeguroUniversal!=null}">
															<c:out value="${SolicitudIncorporacionForm.idSolicitudAceptadaSeguroUniversal}" />
														</c:when>
														
														<c:otherwise>
															<c:out value="${SolicitudIncorporacionForm.idSolicitudSeguroUniversal}" />
														</c:otherwise>
													</c:choose>						
												</td>
												
												<td id="tdEstadoSolicitudSeguroUniversal" class="labelTextValor" style="color:blue;"><c:out value="${SolicitudIncorporacionForm.estadoSolicitudSeguroUniversal}" /></td>
												<td id="tdBotonEstadoSolicitudSeguroUniversal">
													<html:button property="idButton" onclick="return accionComprobarEstadoMutualidad('S');" styleClass="button">Comprobar Estado</html:button>
												</td>
												<td colspan="2">&nbsp;</td>
											</c:otherwise>
										</c:choose>
									</tr>
								</table>
							</c:when>
			
							<c:otherwise>
								<table>
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.planProfesional"/>
										</td>
										<td class="labelTextValor" style="color:red;"><c:out value="${motivoSolicitudAlta}" /></td>			
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.SolicitudIncorporacionDatos.mutualidad.literal.seguroAccidentes"/>
										</td>
										<td class="labelTextValor" style="color:red;"><c:out value="${motivoSolicitudAlta}" /></td>
									</tr>
								</table>
							</c:otherwise>
						</c:choose>
					</siga:ConjCampos>	
				</c:when>
			
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:if>
		
		<siga:Table 		   
		   name="documentosAPresentar"
		   border="1"
		   columnNames="censo.SolicitudIncorporacionDatos.literal.estado,censo.SolicitudIncorporacionDatos.literal.documento"
		   columnSizes="10,90">
		
<% 
			if (documentos != null && documentos.size()>0) {
				for (int i = 0; i < documentos.size(); i++){ 
					Vector v = (Vector) documentos.get(i);
					if (v != null) {
						CenDocumentacionSolicitudInstituBean documento = (CenDocumentacionSolicitudInstituBean) v.get(0); 
						String estado = (String) v.get(1);
%>
				   		<tr class="listaNonEdit">
				   			<td align="center">
				   				<input type="hidden" id="oculto<%=(i+1)%>_1" value="<%=documento.getDocumentacionSolicitud().getIdDocumentacion()%>" />
				   				<input type="checkbox" <%if (editar.equalsIgnoreCase("false")) { out.print(" disabled "); } %><%if(estado.equalsIgnoreCase("true"))out.print("checked");%> />
				   			</td>
				   			<td class="labelTextValor">
				   				<%if(documento.getDocumentacionSolicitud().getDescripcion()!=null)out.print(UtilidadesString.mostrarDatoJSP(documento.getDocumentacionSolicitud().getDescripcion()));%>
				   			</td>
				   		</tr>
<%			
					} // if
			   	} // for
			} else {
%>
		 		<tr class="notFound">
					<td class="titulitos">
						<siga:Idioma key="messages.noRecordFound"/>
					</td>
				</tr>
<%
			}
%>
		</siga:Table>
	
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<%if (editar!=null && !editar.equalsIgnoreCase("false")) {%>
			<siga:ConjBotonesAccion botones="G,V" clase="botonesDetalle" />
		<%}else{%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		<%}%>
	
	<!-- FIN: BOTONES REGISTRO -->
	</html:form>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>