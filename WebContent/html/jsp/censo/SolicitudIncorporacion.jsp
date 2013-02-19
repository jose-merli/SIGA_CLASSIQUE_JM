<!-- SolicitudIncorporacion.jsp -->
<!-- EJEMPLO DE VENTANA DE DETALLE DE UN REGISTRO -->
<!-- Contiene un posible titulo del mantenimiento, ademas de la zona de campos
	 a mantener, que utilizara conjuntos de datos si fuera necesario.
	 Bajo esta zona y sin salirse del tamanho estandar de la ventana existira
	 una zona de botones de acciones sobre el registro.
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri="struts-bean.tld" 	prefix="bean"%>
<%@ taglib uri="struts-html.tld" 	prefix="html"%>
<%@ taglib uri="struts-logic.tld" 	prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<%
	HttpSession ses = request.getSession();
	String fechaSol = (String) request.getAttribute("fechaSol");
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String[] modalidadParam = new String[1];
	modalidadParam[0] = user.getLocation();
	String estiloBox = "box";
	String estiloCombo = "boxCombo";
%>

<html>
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">

	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<html:javascript formName="SolicitudIncorporacionForm"	staticJavascript="false" />

	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera2" localizacion="censo.solicitudIncorporacion.localizacion" />
</head>

<script type="text/javascript">
	function refrescarLocal() {		
		document.getElementById("modo").value="abrirAvanzada";
		document.getElementById("SolicitudIncorporacionForm").setAttribute("target","mainWorkArea");
		document.getElementById("SolicitudIncorporacionForm").submit();
	}
		
	function datosValidos() {
		var errores = "";
	
		if(validaNumeroIdentificacion()) {
	
			if(document.getElementById("tipoSolicitud").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.solicitudDe'/>"+ '\n';
			}
			if(document.getElementById("tipoColegiacion").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.tipoColegiacion'/>"+ '\n';
			}
			if(document.getElementById("tipoModalidadDocumentacion").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.documentacion'/>"+ '\n';
			}
			if(document.getElementById("tipoIdentificacion").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nifcif'/>"+ '\n';
			}
			if(document.getElementById("tipoDon").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.tratamiento'/>" + '\n';
			}
			if(document.getElementById("nombre").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nombre'/>" + '\n';
			}
			if(document.getElementById("apellido1").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.apellido1'/>" + '\n';
			}
			if(document.getElementById("fechaNacimiento").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.fechaNacimiento'/>" + '\n';
			}
			if(document.getElementById("sexo").value=="0"){
				errores += "<siga:Idioma key='errors.required' arg0='censo.consultaDatosGenerales.literal.sexo'/>" + '\n';
			}
			if(document.getElementById("domicilio").value==""){
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
			if(document.getElementById("telefono1").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.telefono1'/>" + '\n';
			}
			if(document.getElementById("mail").value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.email'/>" + '\n';
			}
			
			if (errores != ""){
				alert(errores);
			} else {
				if(validateSolicitudIncorporacionForm(document.getElementById("SolicitudIncorporacionForm"))) {
					return true;
				}
			}
		}
		return false;
	}
	
	function comprobarTipoIdent() {
		// Solo se genera el NIF o CIF de la persona
		if( (document.getElementById("tipoIdentificacion").value == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>" )
				|| (document.getElementById("tipoIdentificacion").value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>" )) {
			document.getElementById("idButtonNif").style.visibility="visible";
		} else {
			document.getElementById("idButtonNif").style.visibility="hidden";
		}
	}	
	
	function validaNumeroIdentificacion() {
		var errorNIE = false;
		var errorNIF = false;
		var valido = true;
	
		if(document.getElementById("tipoIdentificacion").value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			var numero = document.getElementById("NIFCIF").value;
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
		if(document.getElementById("tipoIdentificacion").value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") {
			var dnie = document.getElementById("NIFCIF").value;
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
			alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
		}
		return valido;
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
	
	function generarLetra() {
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
	
	//Asociada al boton Volver -->
	function accionVolver() {		
		window.location = "<html:rewrite page='/html/jsp/censo/SolicitudIncorporacionValidacion.jsp'/>";
	}
	//Asociada al boton Restablecer -->
	function accionRestablecer() {
		if (confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
			document.getElementById("modo").value = "";
			document.getElementById("SolicitudIncorporacionForm").reset();
			cargaPais("");
			document.getElementById("provincia").onchange();
		}
	}
	
	//Asociada al boton Guardar -->
	function accionGuardar() {
		sub();
		if (datosValidos()) {
			document.getElementById("modo").value = "insertar";
			document.getElementById("SolicitudIncorporacionForm").setAttribute("target","submitArea");
			document.getElementById("SolicitudIncorporacionForm").submit();
		} else {
			fin();
			return false;
		}
	}	
	
	var primeravez=true;
	function cargaPais(valor){      		
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
	}	
	
	// --------------------------------------------------------------------------------------------------------------
	//		FILTRO DE POBLACIONES
	// --------------------------------------------------------------------------------------------------------------
	
	var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "messages.general.error"))%>';
	
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
</script>

<body onLoad="cargaPais('');">
	<html:form action="/SIN_SolicitudIncorporacion.do" focus="tipoSolicitud" styleId="SolicitudIncorporacionForm">
		<html:hidden styleId="modo" property="modo" value="" />
		<html:hidden property="editarIdSolicitud" styleId="editarIdSolicitud" value="" />
		<html:hidden property="continuarAprobacion" styleId="continuarAprobacion" value="" />
		<html:hidden property="continuarInsercionColegiado" styleId="continuarInsercionColegiado" value="" />
		<html:hidden property="numeroColegiado" styleId="numeroColegiado" value="" />		

		<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:80px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.solicitudDe'/>" />(*)
								</td>
								<td>
									<siga:ComboBD nombre="tipoSolicitud" tipo="solicitud" ancho="200" clase="boxCombo" obligatorio="true"/>
								</td>
							</tr>
						</table>
					</td>				
					
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:130px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.tipoColegiacion'/>" />(*)
					</td>
					<td>
						<siga:ComboBD nombre="tipoColegiacion" ancho="130" tipo="colegiacion" clase="boxCombo" obligatorio="true" />
					</td>
					
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:170px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.documentacion'/>" />(*)
					</td>	
					<td>
						<siga:ComboBD nombre="tipoModalidadDocumentacion" tipo="modalidadDocumentacion" ancho="170" 
							clase="boxCombo" obligatorio="true" parametro="<%=modalidadParam%>"/>
					</td>
				</tr>
				
				<tr>
					<td style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:158px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.busquedaClientesAvanzada.literal.fechaIncorporacion'/>" />
								</td>
								<td>
									<siga:Fecha nombreCampo="fechaEstadoColegial" valorInicial=""/>
								</td>
							</tr>
						</table>
					</td>
					
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:125px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.fechaSolicitud'/>" />
					</td>
					<td colspan="3" class="boxLabel">
						<input value="<%=fechaSol%>" type="text" id="fechaSolicitud" name="fechaSolicitud" class="boxConsulta" readonly tabindex="-1" />
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos>
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:58px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.nifcif'/>" />&nbsp;(*)
					</td>
					<td colspan="6" style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>									
								<td style="vertical-align: middle;">
									<siga:ComboBD nombre="tipoIdentificacion" tipo="identificacionSolicitud" ancho="100"
										clase="<%=estiloCombo%>" obligatorio="true" accion="comprobarTipoIdent();"/>
								</td>
								<td style="vertical-align: middle;">
									<html:text property="NIFCIF" styleClass="box" size="25" maxlength="20" value=""/>
								</td>
								<td style="vertical-align: middle;">
									<img id="idButtonNif" src="<html:rewrite page='/html/imagenes/comprobar.gif'/>" border="0" onclick="obtenerLetra();" style="cursor:hand;align:left;display:inline;visibility: hidden;">
								</td>
							</tr>
						</table>
					</td>
				</tr>				
				
				<tr>		
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:85px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.tratamiento'/>" />&nbsp;(*)
					</td>
					<td>
						<siga:ComboBD nombre="tipoDon" tipo="tratamiento" ancho="100"
							clase="<%=estiloCombo%>" obligatorio="true" />
					</td>				
					
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:55px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.nombre'/>" />&nbsp;(*)
					</td>
					<td>
						<html:text property="nombre" style="width:170" maxlength="100" styleClass="<%=estiloBox%>" value=""/>
					</td>						
					
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:65px" tabindex="-1" readonly
							value="<siga:Idioma key='censo.consultaDatosGenerales.literal.apellidos'/>" />&nbsp;(*)
					</td>
					<td>
						<html:text property="apellido1" style="width:170" maxlength="100" styleClass="<%=estiloBox%>" value=""/>
					</td>
					<td>
						<html:text property="apellido2" style="width:170" maxlength="100" styleClass="<%=estiloBox%>" value=""/>
					</td>
				</tr>
				
				<tr>
					<td colspan="7" style="padding:0px">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>			
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:140px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.fechaNacimiento'/>" />&nbsp;(*)
								</td>						
								<td>
									<siga:Fecha nombreCampo="fechaNacimiento" valorInicial=""/>
								</td>		
								
								<td style="width:13px">&nbsp;</td>												
											
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:37px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.consultaDatosGenerales.literal.sexo'/>" />&nbsp;(*)
								</td>
								<td>
									<html:select name="SolicitudIncorporacionForm" property="sexo" style="width:75px" styleClass="box" >
								        <html:option value="0" >&nbsp;</html:option>
										<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE%>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
										<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER%>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
									</html:select>	
								</td>
								
								<td style="width:13px">&nbsp;</td>
								
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:81px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.estadoCivil'/>" />
								</td>
								<td>
									<siga:ComboBD nombre="estadoCivil" tipo="estadoCivil" ancho="100" clase="<%=estiloCombo%>" />
								</td>								
								
								<td style="width:13px">&nbsp;</td>
		
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:73px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.naturalDe'/>" />
								</td>
								<td>
									<html:text property="natural" style="width:180" maxlength="100" styleClass="<%=estiloBox%>" value=""/>
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
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:32px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.datosDireccion.literal.pais2'/>" />
								</td>	
								<td>
									<siga:ComboBD nombre="pais" tipo="pais" ancho="420" clase="<%=estiloCombo%>" obligatorio="false" accion="cargaPais(this.value);"/>
								</td>
							</tr>
							
							<tr>
								<td  class="boxLabel">
									<input class="boxLabel" type="text" style="width:64px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.domicilio'/>" />&nbsp;(*)
								</td>									
								<td>
									<html:text styleClass="<%=estiloBox%>" property="domicilio" style="width:420px" maxlength="100" value=""/>
								</td>															
							</tr>						
							
							<tr>								
								<td colspan="2" style="padding:0px">
									<table border="0" cellpadding="5" cellspacing="0">
										<tr id="trEspanol1">
											<td class="boxLabel">
												CP&nbsp;(*)
											</td>
											<td>	
												<html:text property="CP" style="width:50px" styleClass="<%=estiloBox%>" maxlength="5" value=""
													onkeypress="return soloDigitos(event);" onkeydown="onKeyDownCP();" onkeyup="onKeyUpCP();"
													onblur="onBlurCP();" onfocus="onFocusCP();"/>
											</td>					
										
											<td class="boxLabel">
												<input class="boxLabel" type="text" style="width:68px" tabindex="-1" readonly
													value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.poblacion'/>" />&nbsp;(*)
											</td>																		
																	
											<td>
												<html:hidden property="poblacion" value=""/>
												<input class="boxDisabled" id="poblacion_input" type="text" style="width:310px;" value="" maxlength="100" disabled
													onblur="onBlurPoblacionInput();" onkeydown="onKeyDownPoblacionInput(event);" onkeyup="onKeyUpPoblacionInput();" 
													onfocus="onFocusPoblacionInput();" />
												<div id="poblacion_div">
													<select class="box" style="width:310px" id="poblacion_select"
														onblur="onBlurPoblacionSelect();" onchange="onChangePoblacionSelect(true);" onclick="onClickPoblacionSelect();" 
														onkeypress="onKeyPressPoblacionSelect(event);" onkeydown="onKeyDownPoblacionSelect(event);" onmouseover="onMouseDownPoblacionSelect();" onmousedown="onMouseDownPoblacionSelect();">																		
													</select>					
												</div>	
											</td>
										</tr>																			
										
										<tr id="trEspanol2">
											<html:hidden property="provincia" value=""/>
											<td colspan="2" style="height:38px">
												<siga:ToolTip id='idAyudaPoblaciones' imagen="/SIGA/html/imagenes/botonAyuda.gif" texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "censo.SolicitudIncorporacion.ayudaPoblaciones"))%>' />
											</td>  																																								

											<td class="boxLabel">
												<input class="boxLabel" type="text" style="width:68px" tabindex="-1" readonly
													value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.provincia'/>" />											
											</td>
											<td class="boxLabel">
												<input id="provincia_input" class="boxConsulta" type="text" style="width:310px" value="" readonly tabindex="-1" />
											</td>
										</tr>
										
										<tr id="trExtranjero" style="display:none">
											<td class="boxLabel">
												CP&nbsp;(*)
											</td>											
											<td>	
												<html:text property="CPExt" style="width:50px" styleClass="<%=estiloBox%>" maxlength="5" value=""/>
											</td>						
										
											<td class="boxLabel">
												<input class="boxLabel" type="text" style="width:68px" tabindex="-1" readonly
													value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.poblacion'/>" />&nbsp;(*)
											</td>																																													
											<td>
												<html:text styleId="poblacionExt" property="poblacionExt" style="width:310px" maxlength="100" styleClass="<%=estiloBox%>" value="" />
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
								<td  class="boxLabel">
									<input class="boxLabel" type="text" style="width:74px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono1'/>" />&nbsp;(*)
								</td>									
								<td>
									<html:text property="telefono1" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="" />
								</td>
								
								<td  class="boxLabel">
									<input class="boxLabel" type="text" style="width:74px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono2'/>" />
								</td>									
								<td>
									<html:text property="telefono2" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="" />
								</td>
							</tr>
							
							<tr>
								<td  class="boxLabel">
									<input class="boxLabel" type="text" style="width:45px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.fax1'/>" />
								</td>									
								<td>
									<html:text property="fax1" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value="" />
								</td>
								
								<td  class="boxLabel">
									<input class="boxLabel" type="text" style="width:45px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.fax2'/>" />
								</td>
								<td>
									<html:text property="fax2" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value=""/>
								</td>		
							</tr>
							
							<tr>
								<td class="boxLabel">
									<input class="boxLabel" type="text" style="width:45px" tabindex="-1" readonly
										value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.telefono3'/>" />								
								</td>
								<td colspan="3">
									<html:text property="telefono3" style="width:110px" maxlength="15" styleClass="<%=estiloBox%>" value=""/>
								</td>	
							</tr>
							
							<tr>
								<td colspan="4" style="padding:0px;">
									<table border="0" cellpadding="5" cellspacing="0">
										<td  class="boxLabel">
											<input class="boxLabel" type="text" style="width:126px" tabindex="-1" readonly
												value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.email'/>" />&nbsp;(*)		
										</td>
										<td>
											<html:text property="mail" style="width:270px" maxlength="100" styleClass="<%=estiloBox%>" value=""/>
										</td>
									</table>
								</td>
							</tr>				
						</table>
					</td>
				</tr>
				
				<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD" />
				<input type="hidden" name="actionModal" value="" />
			</table>
		</siga:ConjCampos>		


		<siga:ConjCampos>
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>				
					<td class="boxLabel">
						<input class="boxLabel" type="text" style="width:100px" tabindex="-1" readonly
								value="<siga:Idioma key='censo.SolicitudIncorporacion.literal.observaciones'/>" />
					</td>
					<td>
						<textarea class="<%=estiloBox%>" name="observaciones" 
							onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"	
							style="overflow-y:auto;overflow-x:hidden;width:850px;height:50px;resize:none;"
						></textarea>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle" />

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>