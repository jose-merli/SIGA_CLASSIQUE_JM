<!-- consultaDatosRegistrales.jsp -->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenNoColegiadoBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.DatosRegistralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<!-- JSP -->
<% 
	String app = null;
	HttpSession ses = null;
	Properties src = null;
	UsrBean user = null;
	DatosRegistralesForm formulario = null;
	boolean bOcultarHistorico = false;
	String modo = null;
	
	String estiloCaja="", estiloCombo="";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	
	String numero=(String)request.getAttribute("numero");
	String nombre=(String)request.getAttribute("nombrePersona");
	String SSPP=(String)request.getAttribute("SSPP");
	String botones = "V";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	ArrayList actividadSel = new ArrayList();
	try{
		
		app=request.getContextPath();
		ses=request.getSession();
		src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
		user = (UsrBean) ses.getAttribute("USRBEAN");
		bOcultarHistorico = user.getOcultarHistorico();
		formulario = (DatosRegistralesForm)request.getAttribute("DatosRegistralesForm");
		String nif=formulario.getNumIdentificacion();
		modo = (String)request.getAttribute("accion");
		
		
		
	//  tratamiento de readonly
		estiloCaja = "";
		readonly = "false";  // para el combo
		breadonly = false;  // para lo que no es combo
		checkreadonly = " "; // para el check
		boolean breadonlyNif = false;
		String estiloCajaNif = "box";
		String readonlyComboNIFCIF = "false";
		// caso de accion 
		if (modo.equalsIgnoreCase("VER")) {
			// caso de consulta
			estiloCaja = "boxConsulta";
			estiloCombo = "boxConsulta";
			readonly = "true";
			breadonly = true;
			checkreadonly = " disabled ";
			
		} else {
			estiloCaja = "box";
			estiloCombo = "boxCombo";
			readonly = "false";
			breadonly = false;
			checkreadonly = " ";
			
		}
		
		
		// Gestion de Volver
		
		if (busquedaVolver == null) {
			busquedaVolver = "volverNo";
		}
		
		if (!busquedaVolver.equals("volverNo")) { 
			botones="V,G,R";
		}
		
	}
	
	catch (Exception e){
		e.printStackTrace();
	}
	
%>	

<html>

	<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
		<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
		<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!--Step 2 -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>	
		<script src="<%=app%>/html/js/jquery.js" type="text/javascript"></script>
		
		<!--Step 3 -->
		  <!-- defaults for Autocomplete and displaytag -->
		  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
		  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />


		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DatosRegistralesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="pestana.fichaCliente.datosRegistrales" 
			localizacion="censo.fichaCliente.datosRegistrales.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

		<script>
		jQuery.noConflict();
		function buscarGrupos(){
			document.ActividadProfesionalForm.modo.value="buscar";
			document.ActividadProfesionalForm.modoAnterior.value=document.forms[0].accion.value;				
			document.ActividadProfesionalForm.idPersona.value=document.forms[0].idPersona.value;	
			document.ActividadProfesionalForm.idInstitucion.value=document.forms[0].idInstitucion.value;
			document.ActividadProfesionalForm.target="resultado";	
			document.ActividadProfesionalForm.submit();	
		}

		function rellenarComboIden(){
			if(DatosRegistralesForm.numIdentificacion.value != null && DatosRegistralesForm.numIdentificacion.value != ""){
				if(nif(DatosRegistralesForm.numIdentificacion.value) == 1){
					DatosRegistralesForm.tipoIdentificacion.value = "10";
				}else if(validarNIE(DatosRegistralesForm.numIdentificacion.value) == 3){
					DatosRegistralesForm.tipoIdentificacion.value = "40";
				}else{
					DatosRegistralesForm.tipoIdentificacion.value = "50";
				}
			}
		}

		function nif(a) {
			
			var a = trim(DatosRegistralesForm.numIdentificacion.value);
			var temp=a.toUpperCase();
			var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
		 
			if (temp!==''){
				//si no tiene un formato valido devuelve error
				if ((!/^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$/.test(temp) && !/^[T]{1}[A-Z0-9]{8}$/.test(temp)) && !/^[0-9]{8}[A-Z]{1}$/.test(temp))
				{
					return 0;
				}
		 
				//comprobacion de NIFs estandar
				if (/^[0-9]{8}[A-Z]{1}$/.test(temp))
				{
					posicion = a.substring(8,0) % 23;
					letra = cadenadni.charAt(posicion);
					var letradni=temp.charAt(8);
					if (letra == letradni)
					{
					   	return 1;
					}
					else
					{
						return -1;
					}
				}
		 
				//algoritmo para comprobacion de codigos tipo CIF
				suma = parseInt(a[2])+parseInt(a[4])+parseInt(a[6]);
				for (i = 1; i < 8; i += 2)
				{
					temp1 = 2 * parseInt(a[i]);
					temp1 += '';
					temp1 = temp1.substring(0,1);
					temp2 = 2 * parseInt(a[i]);
					temp2 += '';
					temp2 = temp2.substring(1,2);
					if (temp2 == '')
					{
						temp2 = '0';
					}
					
					suma += (parseInt(temp1) + parseInt(temp2));
				}
					
				suma += '';
				n = 10 - parseInt(suma.substring(suma.length-1, suma.length));
		 
				//comprobacion de NIFs especiales (se calculan como CIFs)
				if (/^[KLM]{1}/.test(temp))
				{
					if (a[8] == String.fromCharCode(64 + n))
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
		 
				//comprobacion de CIFs
				if (/^[ABCDEFGHJNPQRSUVW]{1}/.test(temp))
				{
					temp = n + '';
					if (a[8] == String.fromCharCode(64 + n) || a[8] == parseInt(temp.substring(temp.length-1, temp.length)))
					{
						return 2;
					}
					else
					{
						return -2;
					}
				}
		 
				//comprobacion de NIEs
				//T
				if (/^[T]{1}/.test(temp))
				{
					if (a[8] == /^[T]{1}[A-Z0-9]{8}$/.test(temp))
					{
						return 3;
					}
					else
					{
						return -3;
					}
				}
		 
				//XYZ
				if (/^[XYZ]{1}/.test(temp))
				{
					pos = str_replace(['X', 'Y', 'Z'], ['0','1','2'], temp).substring(0, 8) % 23;
					if (a[8] == cadenadni.substring(pos, pos + 1))
					{
						return 3;
					}
					else
					{
						return -3;
					}
				}
			}
	 
			return 0;
		}

		function validarNIE(a) {
			var a = trim(DatosRegistralesForm.numIdentificacion.value);
			if(a.length==9){
				letIni = a.substring(0,1);
				primera=letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni + a.substring(1,8);
				letFin = a.substring(8,9);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (!primera.match('[X|Y|Z]')||letra!=letFin) {
					return -3;
				}else{
					return 3;
				}
			}else{
				return -3;
			}
		} 	

		function obtenerNif() {

			if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&& (document.forms[0].numIdentificacion.value!="") ) {
			     var sNIF = document.forms[0].numIdentificacion.value;
			     document.forms[0].numIdentificacion.value = formateaNIF(sNIF);
			} 

		   	var nif = (document.forms[0].numIdentificacion.value);
				
			if (nif!="") {
				
				if (!validaNumeroIdentificacion2()) {
					return false;
				}
				alert(nif);
				//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y éste se encarga de todo :)
			  	document.forms[0].modo.value="buscarNIF";
				document.forms[0].target="submitArea";						
				document.forms[0].submit();	
			}			
		}
			
		function validaNumeroIdentificacion2 (){
			document.forms[0].numIdentificacion.value = (document.forms[0].numIdentificacion.value).toUpperCase();
			var a = (document.forms[0].numIdentificacion.value);
			if (document.forms[0].tipoIdentificacion!=undefined && (document.forms[0].tipoIdentificacion.value==<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>||document.forms[0].tipoIdentificacion.value==<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>)) {
			    if (!validarNIFCIF(document.forms[0].tipoIdentificacion.value,a)) {
					document.forms[0].numIdentificacion.focus();
					return false;
				}
			}
			return true;
		}

		function validaNumeroIdentificacion(){
			var valido = true;

			// Si no se pusieron nombre y apellidos, entonces no se inserta notario
			if(DatosRegistralesForm.nombre.value == "" && DatosRegistralesForm.apellido1.value == "") {
				DatosRegistralesForm.numIdentificacion.value = "";
				DatosRegistralesForm.tipoIdentificacion.value = "";
				
			} // Si se dejo en blanco el N. Identificacion
			else if(DatosRegistralesForm.numIdentificacion.value == "") {
				DatosRegistralesForm.tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
				generarIdenHistorico();
				alert ("<siga:Idioma key='messages.nota.automatico'/>");
				
			} // Si se dejo en blanco el Tipo de identificacion
			else if (DatosRegistralesForm.tipoIdentificacion.value == ""){
				if(document.DatosRegistralesForm.numIdentificacion.disabled == true){//Viene de busqueda
					DatosRegistralesForm.tipoIdentificacion.value = "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>";
				}
			}
			else if(DatosRegistralesForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
				var errorNIF = false;
				var numero = DatosRegistralesForm.numIdentificacion.value;
				
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
				
				if (errorNIF){
					// ERROR NIF
					if(document.DatosRegistralesForm.numIdentificacion.disabled == true){ //Viene de busqueda
						DatosRegistralesForm.tipoIdentificacion.value = "50";
						valido = true;
					}else{
						valido = false;
						alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
					}
				}
			}
			if(DatosRegistralesForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
				var errorNIE = false;
				var dnie = DatosRegistralesForm.numIdentificacion.value;

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

				if (errorNIE){
					// ERROR NIE
					if(document.DatosRegistralesForm.numIdentificacion.disabled == true){ //Viene de busqueda
						DatosRegistralesForm.tipoIdentificacion.value = "50";
						valido = true;
					}else{
						valido = false;
						alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
					}
				}

			}
			
			return valido;
		}

		function obtenerLetra(){
			generarLetra();
		}

		function generarLetra() {
			var numId = DatosRegistralesForm.numIdentificacion.value;
			var tipoIdentificacion = DatosRegistralesForm.tipoIdentificacion.value;
		  	var letra='TRWAGMYFPDXBNJZSQVHLCKET';
			if(numId.length==0){
				return false;		
			}
			if( (tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")){
				if(numId.length==8){
					if(isNumero(numId)==true){
					 	numero = numId;
					 	numero = numero % 23;
					 	letra=letra.substring(numero,numero+1);
					 	DatosRegistralesForm.numIdentificacion.value =numId+letra;
					}else{
						return validaNumeroIdentificacion(tipoIdentificacion, numId);
					}
				}else{
					rc = validaNumeroIdentificacion(tipoIdentificacion, numId);
					if(rc==false){
					    return rc;
					}	
				}
			} else	if((tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){
				if(numId.length==8){
					var dnie = DatosRegistralesForm.numIdentificacion.value;
					letIni = numId.substring(0,1);
					primeraLetra = letIni;
					if  (letIni.toUpperCase()=='Y')
				 		letIni = '1';
				 	else if  (letIni.toUpperCase()=='Z')
				 		letIni = '2';
				 	else{
				 		letIni = '0';
				 	}
					num = letIni+numId.substring(1,8);
					if(primeraLetra.match('[X|Y|Z]') && isNumero(num)){
						var posicion = num % 23;
						letras='TRWAGMYFPDXBNJZSQVHLCKET';
						var letra=letras.substring(posicion,posicion+1);
						numero = dnie + letra;
						DatosRegistralesForm.numIdentificacion.value = numero;
					}else{
						return validaNumeroIdentificacion(tipoIdentificacion, numId);
					}	
				}else{
					rc = validaNumeroIdentificacion(tipoIdentificacion, numId);
					if(rc==undefined){
						return rc;
					}else if(rc==false){
						return rc;
					}	
				}					
			}
			// Caso1: Se han realizado las modificaciones necesarias sin encontrar errores 
			// Caso2: no es nif ni nie no hay generacion de letra

			return true;
		}

		function postAccionBusquedaNIF(){
			if(document.DatosRegistralesForm.idPersonaNotario.value != null && document.DatosRegistralesForm.idPersonaNotario.value != "" && document.DatosRegistralesForm.idPersonaNotario.value != "null"){
				ponerIconoIdentPersona(true);		
				document.DatosRegistralesForm.tipoIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.numIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.nombre.disabled="disabled";      
				document.DatosRegistralesForm.apellido1.disabled="disabled";   
				document.DatosRegistralesForm.apellido2.disabled="disabled"; 
				
			}else{
				//No se ha encontrado esa persona
				ponerIconoIdentPersona(false);
				document.DatosRegistralesForm.tipoIdentificacion.disabled="";
				document.DatosRegistralesForm.numIdentificacion.disabled="";
				document.DatosRegistralesForm.nombre.disabled="";      
				document.DatosRegistralesForm.apellido1.disabled="";   
				document.DatosRegistralesForm.apellido2.disabled=""; 
				document.DatosRegistralesForm.idPersonaNotario.value = "";
			}
		}

		function generaNumOtro()
		{
			if((DatosRegistralesForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") && (DatosRegistralesForm.numIdentificacion.value=="")) {
				generarIdenHistorico();
			} else if ((DatosRegistralesForm.tipoIdentificacion.value != "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") && (DatosRegistralesForm.numIdentificacion.value.substr(0,4)=="NOTA")) {
				// Quitar el Num. de identificacion automatico de notario (NOTA) si se cambia el tipo de identificacion a distinto de Otro
				DatosRegistralesForm.numIdentificacion.value="";
				DatosRegistralesForm.numIdentificacion.disabled = "";
			}
		}
		jQuery(document).ready(function(){
			jQuery(".box").change(function() {
				if(jQuery(this).attr("name")=="tipoIdentificacion"){
					generaNumOtro();
				}
			});	 
		});
		function generarIdenHistorico()
		{
			jQuery.ajax({
				type: "POST",
				url: "/SIGA/CEN_DatosGenerales.do?modo=getIdNotario",
				data: "idInstitucion="+DatosRegistralesForm.idInstitucion.value,
				dataType: "json",
				success:  function(json) {
					DatosRegistralesForm.numIdentificacion.value=json.numHistorico;
					DatosRegistralesForm.numIdentificacion.disabled = "disabled";
				},
				error: function(xml,msg){
					alert("Error: "+msg);
				}
			});
		}
		
		function buscarNotario(){
			if ((document.DatosRegistralesForm.nombre.value 		== null || document.DatosRegistralesForm.nombre.value == "") &&
				(document.DatosRegistralesForm.apellido2.value 		== null || document.DatosRegistralesForm.apellido2.value == "") &&
				(document.DatosRegistralesForm.apellido1.value 		== null || document.DatosRegistralesForm.apellido1.value == "")){
						
				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.camposVacios"/>" ;
		 		 alert (mensaje);			 
				 return false;
			}
			
			if(document.DatosRegistralesForm.apellido2.value != null && document.DatosRegistralesForm.apellido2.value != ""){
				document.DatosRegistralesForm.apellido2.value=document.DatosRegistralesForm.apellido2.value;
			}else{
				document.DatosRegistralesForm.apellido2.value="";
			}

			if(document.DatosRegistralesForm.apellido1.value != null && document.DatosRegistralesForm.apellido1.value != ""){
				document.DatosRegistralesForm.apellido1.value=document.DatosRegistralesForm.apellido1.value;
			}else{
				document.DatosRegistralesForm.apellido1.value="";
			}

			if(document.DatosRegistralesForm.nombre.value != null && document.DatosRegistralesForm.nombre.value != ""){
				document.DatosRegistralesForm.nombre.value=document.DatosRegistralesForm.nombre.value;
			}else{
				document.DatosRegistralesForm.nombre.value="";
			}
						
			DatosRegistralesForm.modo.value="buscarNotarioInit";
			
			var resultado = ventaModalGeneral("DatosRegistralesForm","G");	
			if (resultado!=undefined && resultado[0]!=undefined ){			
				ponerIconoIdentPersona(true);
				document.DatosRegistralesForm.idPersonaNotario.value       = resultado[0];

				//Datos Generales
				document.DatosRegistralesForm.numIdentificacion.value = resultado[1];
				document.DatosRegistralesForm.nombre.value          = resultado[2];
				document.DatosRegistralesForm.apellido1.value       = resultado[3];
				document.DatosRegistralesForm.apellido2.value = resultado[4]; 

				document.DatosRegistralesForm.tipoIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.numIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.nombre.disabled="disabled";      
				document.DatosRegistralesForm.apellido1.disabled="disabled";   
				document.DatosRegistralesForm.apellido2.disabled="disabled"; 
				document.getElementById("textoEdicion").style.display="block";
			}							
		}	

		function limpiarCliente (){
			document.getElementById('tipoIdentificacion').value = "10";
			document.getElementById('numIdentificacion').value = "";
			document.getElementById('nombre').value = "";
			document.getElementById('apellido1').value = "";
			document.getElementById('apellido2').value = "";
			document.DatosRegistralesForm.idPersonaNotario.value ="";
			document.DatosRegistralesForm.nombre.disabled="";
			document.DatosRegistralesForm.apellido1.disabled="";
			document.DatosRegistralesForm.apellido2.disabled="";
			document.forms[0].tipoIdentificacion.disabled="";
			document.forms[0].numIdentificacion.disabled="";
			ponerIconoIdentPersona(false);
			document.getElementById("textoEdicion").style.display="none";
		}

		function ponerIconoIdentPersona (encontrado){
			if (encontrado) {
				document.getElementById("info_existe").src = "/SIGA/html/imagenes/encontrado.gif";
				document.getElementById("info_existe").alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esYaExistentePersonaJG'/>";
			}else {
				document.getElementById("info_existe").src = "/SIGA/html/imagenes/nuevo.gif";
				document.getElementById("info_existe").alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esNuevaPersonaJG'/>";
			}
		} //ponerIconoIdentPersona ()
		
		function inicio(){
			if(document.getElementById('numIdentificacion').value != null && document.getElementById('numIdentificacion').value != ""){
				document.DatosRegistralesForm.tipoIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.numIdentificacion.disabled="disabled";
				document.DatosRegistralesForm.nombre.disabled="disabled";      
				document.DatosRegistralesForm.apellido1.disabled="disabled";   
				document.DatosRegistralesForm.apellido2.disabled="disabled"; 
				ponerIconoIdentPersona(true);
				document.getElementById("textoEdicion").style.display="block";
			}else{
				document.DatosRegistralesForm.tipoIdentificacion.disabled="";
				document.DatosRegistralesForm.numIdentificacion.disabled="";
				document.DatosRegistralesForm.nombre.disabled="";      
				document.DatosRegistralesForm.apellido1.disabled="";   
				document.DatosRegistralesForm.apellido2.disabled=""; 
				ponerIconoIdentPersona(false);
			}
			
		}
		
	</script>
</head>

<body class="tablaCentralCampos" onload="inicio();buscarGrupos();">


<html:form  action="/CEN_DatosRegistrales.do" method="POST" target="mainPestanas" enctype="multipart/form-data">
<input type="hidden" name="actionModal" value="">
<html:hidden  name="DatosRegistralesForm" property="idPersonaNotario"/>
<html:hidden  name="DatosRegistralesForm" property="modo"/>
<html:hidden  name="DatosRegistralesForm" property="idInstitucion" value="<%=user.getLocation()%>"/>
<html:hidden  name="DatosRegistralesForm" property="idPersona"/>
<html:hidden  name="DatosRegistralesForm" property="accion"/>


		<table class="tablaCentralCampos" align="center" cellspacing=0>
		
			
			<tr>				
				<td class="titulitosDatos">
					
					<siga:Idioma key="censo.consultaDatosRegistrales.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
	    			<%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
			  			 <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>		
				</td>					
			</tr>
			
		</table>

			<!-- CAMPOS DEL REGISTRO -->
			<table class="tablaCentralCampos" align="center">									

				<tr>				
					<td width="100%" align="center">

						<!-- DATOS DE CONSTITUCION -->
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.titulo2">
							<table CELLSPACING="3" border="0">
								<tr >
									<td class="labelText" width="142px">
									<!-- FECHA CONSTITUCION -->
										<siga:Idioma key="censo.general.literal.FechaConstitucion"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td width="63%">
										
										<html:text name="DatosRegistralesForm" property="fechaConstitucion" size="10" styleClass="<%=estiloCaja %>" readonly="true"></html:text>
											&nbsp;
										<% if (!breadonly) { %>
										<a href='javascript://'onClick="return showCalendarGeneral(fechaConstitucion);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
										<% } %>
									</td>
									<!-- FECHA FIN -->
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.fecha_fin"/>&nbsp;
									</td>
									<td colspan="0">
										<html:text name="DatosRegistralesForm" property="fecha_fin" size="10" styleClass="<%=estiloCaja %>" readonly="true"></html:text>
											&nbsp;
										<% if (!breadonly) { %>
										<a href='javascript://'onClick="return showCalendarGeneral(fecha_fin);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
										<% } %>
									</td>
								</tr>
								<tr CELLSPACING="3">
									<td class="labelText">
										<!-- RESEÑA -->
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.Resenia"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td colspan="3">
										<html:text name="DatosRegistralesForm" property="resena"  size="131" maxlength="1500" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
									</td>
								</tr>
								<tr>
								
									<td class="labelText" >
										
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.objSocial"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td colspan="1">
										<html:textarea cols="50"  rows="12" name="DatosRegistralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" property="objetoSocial" style="width:550px;" styleClass="<%=estiloCaja%>"></html:textarea>
									</td>
										<td  style="width:300px" colspan="2">
										
										<!-- INICIO: IFRAME LISTA RESULTADOS -->
											<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
															id="resultado"
															name="resultado" 
															scrolling="no"
															frameborder="1"
															marginheight="0"
															marginwidth="0";					 
															style="width:90%; height:100%;">
											</iframe>
											<!-- FIN: IFRAME LISTA RESULTADOS -->
										</td>
									</tr>
							</table>															
						</siga:ConjCampos>

					</td>
				</tr>
			
			</table>
			<!-- CAMPOS DEL SEGURO-->
			<table class="tablaCentralCampos" align="center">
				<tr>				
					<td width="100%" align="center">
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.seguro">
							<table border="0">
								<td class="labelText"  width="142">
									<siga:Idioma key="censo.consultaDatosRegistrales.literal.Poliza"/>&nbsp;
								</td>
								<td >
									<html:text name="DatosRegistralesForm" property="noPoliza"  size="20" maxlength="20" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosRegistrales.literal.Compania"/>&nbsp;
								</td>
								<td >
									<html:text name="DatosRegistralesForm" property="companiaSeg"  size="70" maxlength="100" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
								</td>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>	
			</table>

			<!-- CAMPOS DEL NOTARIO-->
			<table class="tablaCentralCampos"  border=0 >
				<tr>				
					<td>

						<!-- DATOS DEL NOTARIO -->
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.titulo3">
							<table  border=0>
								<tr>
									<td id="obligatoriotipo" class="labelText" >
									
										<!-- NUMERO IDENTIFICACION NIF/CIF -->
										<siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif"/>
										
									</td>
										<%ArrayList tipoIdentificacionSel = new ArrayList();
										  tipoIdentificacionSel.add(request.getAttribute("tipoident"));
										 %>									
									<td>
										<html:text name="DatosRegistralesForm" property="numIdentificacion" size="10" maxlength="20"  styleClass="<%=estiloCaja %>" onblur="rellenarComboIden()"></html:text>
  				        				<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF"  ancho="110" clase="<%=estiloCaja%>" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" />
  				        				<img id="info_existe" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>
									</td>
									
									<% if (!modo.equalsIgnoreCase("VER")) { %>
									
										<td colspan ="5"></td>
										
										<td>
											<!-- Boton para buscar un Colegiado -->
											<input type="button" class="button" id="idButton" name="buscar"  value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>'  onClick="buscarNotario();">
										</td>
									
									<% } else { %>
										<td colspan ="6"></td>
									<% } %>
									
								</tr>
								<tr>
									<td id="obligatorionombre" class="labelText">
										<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp (*)
									</td>
									<td>
										<html:text name="DatosRegistralesForm" property="nombre" size="25" maxlength="80" styleClass="<%=estiloCaja %>">
										</html:text>
									</td>

									<!-- APELLIDOS -->
									<td id="obligatorioapellido" class="labelText">
										<siga:Idioma key="censo.consultaDatosGenerales.literal.apellidos"/>&nbsp;(*)
									</td>
									
									<td>
										<html:text name="DatosRegistralesForm" property="apellido1" size="25" maxlength="80" styleClass="<%=estiloCaja %>"></html:text>
										<html:text name="DatosRegistralesForm" property="apellido2" size="25" maxlength="80" styleClass="<%=estiloCaja %>"></html:text>
									</td>
									
									<% if (!modo.equalsIgnoreCase("VER")) { %>
										<td colspan="3" width="100"></td>
										<td>
											<!-- Boton para limpiar formulario -->
											<input type="button" class="button" id="idButton" name="limpiar" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.limpiar"/>' onClick="limpiarCliente();">
										</td>
									
									<% } else { %>
										<td colspan ="4"></td>
									<% } %>
									
								</tr>
								<tr>
									<td id="textoEdicion" class="labelText" colspan="5" style="display:none">
										(*)&nbsp;<siga:Idioma key="censo.consultaDatosRegistrales.texto.edicionNotario"/>
									</td>
								</tr>
							</table>
																						
						</siga:ConjCampos>

					</td>
				</tr>
			</table>
		</html:form>

<siga:ConjBotonesAccion botones="<%=botones%>"    modo='<%=modo%>' clase="botonesDetalle"/>	
	<html:form action="/CEN_ActividadProfesional.do" method="POST" target="resultado">
		<html:hidden name="ActividadProfesionalForm" property="modo" value="buscar"/>
		<html:hidden name="ActividadProfesionalForm" property="idPersona" />
		<html:hidden name="ActividadProfesionalForm" property="idInstitucion" />
		<html:hidden name="ActividadProfesionalForm" property="modoAnterior" />
	</html:form>

<ajax:updateFieldFromSelect
	baseUrl="/SIGA/CEN_DatosRegistrales.do?modo=getAjaxBusquedaNIF"
	source="numIdentificacion" target="idPersonaNotario,apellido1,apellido2,nombre,numIdentificacion"
	parameters="tipoIdentificacion={tipoIdentificacion},numIdentificacion={numIdentificacion},apellido1={apellido1},apellido2={apellido2},nombre={nombre}"
	postFunction="postAccionBusquedaNIF" 
/>

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();	
			deshabilitarCamposAbajo();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() {
			sub();
			if(validaNumeroIdentificacion()){
				if (<%=SSPP%>=="1" && document.forms[0].resena.value!="" && document.forms[0].fechaConstitucion.value!="" && document.forms[0].objetoSocial.value!="") {
					if (document.forms[0].tipoIdentificacion.value=="" || document.forms[0].numIdentificacion.value=="" || document.forms[0].nombre.value=="" || document.forms[0].apellido1.value==""){ 
						alert ("Introduzca los campos obligatorios ");
						fin();
						return false;
					}else{
						document.forms[0].numIdentificacion.value = (document.forms[0].numIdentificacion.value).toUpperCase();
						//Se habilitan lo campos para poder enviar correctamente su contenido
						document.DatosRegistralesForm.tipoIdentificacion.disabled="";
						document.DatosRegistralesForm.numIdentificacion.disabled="";
						document.DatosRegistralesForm.nombre.disabled="";      
						document.DatosRegistralesForm.apellido1.disabled="";   
						document.DatosRegistralesForm.apellido2.disabled="";
					  	document.forms[0].modo.value="modificarRegistrales";		
						document.forms[0].submit();
					}
				}else{
					if (<%=SSPP%>=="0"){
						if (document.forms[0].tipoIdentificacion.value=="" || document.forms[0].numIdentificacion.value=="" || document.forms[0].nombre.value=="" || document.forms[0].apellido1.value==""){ 
							alert ("Introduzca los campos obligatorios ");
							fin();
							return false;
						}else{
							document.forms[0].numIdentificacion.value = (document.forms[0].numIdentificacion.value).toUpperCase();
							//Se habilitan lo campos para poder enviar correctamente su contenido
							document.DatosRegistralesForm.tipoIdentificacion.disabled="";
							document.DatosRegistralesForm.numIdentificacion.disabled="";
							document.DatosRegistralesForm.nombre.disabled="";      
							document.DatosRegistralesForm.apellido1.disabled="";   
							document.DatosRegistralesForm.apellido2.disabled=""; 
						  	document.forms[0].modo.value="modificarRegistrales";						
							document.forms[0].submit();	
						}
					}else{
						alert ("Introduzca los campos obligatorios ");
						fin();
						return false;	
					}
				}
			} else {
				fin();
				return false;
			}
		}
		
		function refrescarLocal() {		
			document.forms[0].modo.value="abrir";
			deshabilitarCamposAbajo();
			document.forms[0].submit();	
		}


		function deshabilitarCamposAbajo(){
			if (document.forms[0].tipoIdentificacion.value!="" && document.forms[0].numIdentificacion.value!="" && document.forms[0].nombre.value!="" && document.forms[0].apellido1.value!=""){
				document.DatosRegistralesForm.nombre.disabled=true;
				document.DatosRegistralesForm.apellido1.disabled=true;
				document.DatosRegistralesForm.apellido2.disabled=true;
				document.forms[0].tipoIdentificacion.disabled=true;
				document.forms[0].numIdentificacion.disabled=true;
			}else{
				document.DatosRegistralesForm.nombre.disabled=false;
				document.DatosRegistralesForm.apellido1.disabled=false;
				document.DatosRegistralesForm.apellido2.disabled=false;
				document.forms[0].tipoIdentificacion.disabled=false;
				document.forms[0].numIdentificacion.disabled=false;
			}
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	<script>
		capturarEnter();
		
		function capturarEnter(){
			document.DatosRegistralesForm.apellido2.onkeypress = submitConTeclaEnter;
			document.DatosRegistralesForm.apellido1.onkeypress = submitConTeclaEnter;
			document.DatosRegistralesForm.nombre.onkeypress = submitConTeclaEnter;
		}
		
		function submitConTeclaEnter(){			
			var keycode;
			if (window.event)  {
				keycode = window.event.keyCode;
			}
			if (keycode == 13) {
				buscarNotario();
				return false;
			}
		}
		
	</script>
	</body>
</html>
