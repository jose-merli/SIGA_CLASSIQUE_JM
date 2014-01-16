<!-- validacionSIGA.jsp -->
<%@ page contentType="text/javascript" language="java" errorPage=""%>
<%@ page pageEncoding="ISO-8859-1"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>


<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
 
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	// mensajes
	String mensajeCifNif = UtilidadesString.getMensajeIdioma(usrbean, "messages.nif.comprobacion.digitos.error");
	String mensajeNIE = UtilidadesString.getMensajeIdioma(usrbean, "messages.nie.comprobacion.digitos.error");
%>
var mensajeCifNif="<%=mensajeCifNif %>";
var mensajeNIE="<%=mensajeNIE%>";
var mensajetelef1="<siga:Idioma key="certificados.solicitudes.literal.longitudTelef"/>";
var mensajetelef2="<siga:Idioma key="certificados.solicitudes.literal.errorTelef"/>";
var mensajetelef3="<siga:Idioma key="certificados.solicitudes.literal.soloNumeros"/>";

// BEGIN BNS 05/12/2012
// INCIDENCIA INC_07085_SIGA
// FUNCIONES DE VALIDACION DE DOCUMENTO DE IDENTIDAD
var obtenerCaracterVerificacion = false;

function formatearDocumentoIdentidad(campoDocIdentidad, validar, tipoDocIdentidad){
	var bResult = false;
	if (campoDocIdentidad != undefined && campoDocIdentidad.value != undefined && campoDocIdentidad.value != ""){
		var docIdentidad = campoDocIdentidad.value;
		if (tipoDocIdentidad != "" && tipoDocIdentidad != undefined){
			if (tipoDocIdentidad == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
				var obtenerLetra = false;
				var sNif = docIdentidad;
				if (obtenerCaracterVerificacion && !isNaN(sNif[sNif.length - 1])){
					obtenerLetra = true;
				}
				sNif = formatearNIF(sNif);
				if (obtenerLetra){
					var letra = obtenerCaracterVerificacionNif(sNif);
					sNif = sNif.substring(0,8) + letra;
					docIdentidad = sNif;
					bResult = true;
				} else if (validar){
					var nifValido = validarNIF(sNif);
					if (nifValido === -1){
						// NIF con formato correcto pero caracter de verificación no válido
						alert (mensajeCifNif);
					} else if (nifValido === -2){
						// Formato de NIF incorrecto
						alert ("<siga:Idioma key="messages.nif.comprobacion.digitos.erroneo"/>");
					} else {
						docIdentidad = sNif;
						bResult = true;
					}
				} else { 
					docIdentidad = sNif;
					bResult = true;
				}
			} else if (tipoDocIdentidad == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
				var sNie = formatearDocIdentidad(docIdentidad);
				if (validar){
					var nieValido = validarNIF(sNif);
					if (nieValido === -1){
						// NIE con formato correcto pero caracter de verificación no válido
						alert (mensajeNIE);
					} else if (nieValido === -2){
						// Formato de NIE incorrecto
						alert ("<siga:Idioma key="messages.nie.comprobacion.digitos.erroneo"/>");
					} else {
						docIdentidad = sNie;
						bResult = true;
					}
				} else {
					docIdentidad = sNie;
					bResult = true;
				}
			} else if (tipoDocIdentidad == "<%=ClsConstants.TIPO_IDENTIFICACION_CIF%>"){
				var sCif = formatearDocIdentidad(docIdentidad);
				if (validar){
					var cifValido = validarCIF(sCif);
					if (cifValido === -1){
						// CIF con formato correcto pero caracter de verificación no válido
						//TODO: EL MENSAJE NO SE CORRESPONDE CON ERROR DE VALIDACIÓN
						alert (mensajeCifNif);
					} else if (cifValido === -2){
						// Formato de CIF incorrecto
						//TODO: EL MENSAJE NO SE CORRESPONDE CON ERROR DE FORMATO
						alert ("<siga:Idioma key="messages.nifcif.comprobacion.error"/>");
					} else {
						docIdentidad = sCif;
						bResult = true;
					}
				} else {
					docIdentidad = sCif;
					bResult = true;
				}
			} else if (tipoDocIdentidad == "<%=ClsConstants.TIPO_IDENTIFICACION_PASAPORTE%>"){
				docIdentidad = trim(docIdentidad).toUpperCase();
				bResult = true;
			} else if (tipoDocIdentidad == "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>"){
				// NINGÚN CONTROL
				bResult = true;
			}
			campoDocIdentidad.value = docIdentidad;
		} else {
			//TODO: Crear función genérica independiente del tipo si hace falta
		}
	} else {
		// campoDocIdentidad no encontrado o vacío
		// TODO: Mostrar mensaje?
	}
	return bResult;
}

// END BNS 05/12/2012

function ValidaCIF (cif)
{   
    var pares = 0; 
    var impares = 0; 
    var suma; 
    var ultima; 
    var unumero; 
    var uletra = new Array("J", "A", "B", "C", "D", "E", "F", "G", "H", "I"); 
    var xxx; 
     
    
    cif = cif.toUpperCase(); 
     
    var regular =/^[ABCDEFGHKLMNPQS]\d\d\d\d\d\d\d[0-9,A-J]$/g; 
    if (!regular.exec(cif)) {
	 	alert(mensajeCifNif);
	 	return false; 
	}
          
    ultima = cif.substr(8,1); 
 
    for (var cont = 1 ; cont < 7 ; cont ++){ 
         xxx = (2 * parseInt(cif.substr(cont++,1))).toString() + 0; 
         impares += parseInt(xxx.substr(0,1)) + parseInt(xxx.substr(1,1)); 
         pares += parseInt(cif.substr(cont,1)); 
    } 
     xxx = (2 * parseInt(cif.substr(cont,1))).toString(); 
     impares += parseInt(xxx.substr(0,1)) + parseInt(0 + xxx.substr(1,1)); 
      
     suma = (pares + impares).toString(); 
     unumero = parseInt(suma.substr(suma.length - 1, 1)); 
     unumero = (10 - unumero).toString(); 
     if(unumero == 10) unumero = 0; 
      
     if ((ultima == unumero) || (ultima == uletra[unumero])) {
         return true; 
	 } else {
		 alert(mensajeCifNif);
         return false; 
     }
} 

  function esNIFCorrecto(nif, validaNIFCIF){
  
	letras = new Array();
  letras[0]  = "T";		  letras[1]  = "R";		  letras[2]  = "W";		  letras[3]  = "A";		  letras[4]  = "G";
  letras[5]  = "M";		  letras[6]  = "Y";		  letras[7]  = "F";		  letras[8]  = "P";		  letras[9]  = "D";
  letras[10] = "X";			letras[11] = "B";		  letras[12] = "N";		  letras[13] = "J";		  letras[14] = "Z";
  letras[15] = "S";		  letras[16] = "Q";		  letras[17] = "V";		  letras[18] = "H";		  letras[19] = "L";
  letras[20] = "C";			letras[21] = "K";		  letras[22] = "E";		

  txtError = "";
  ok = true;
	
  dni=nif.substring(0,nif.length-1);
  dni = quitarCeros(dni);
  
  // RGG quito esto porque se traga 1-R
  //dni=parseInt(dni);
  
  if (isNaN(dni)) {
   	ok = false;
  } else { 

	letra=nif.charAt(nif.length-1);
  	letraCorrecta = letras[ dni % 23];
	
	
  	if (dni > 99999999){ 
    	ok = false;
  	} 
  	else 
 	if(letra < "A" || letra > "z"){
	    ok = false;
	} 
	else 
	if(letra.toUpperCase() != letraCorrecta.toUpperCase()) {
    	ok = false;
	}
  }

  if (ok) {
    return true;
  } else {
   if (validaNIFCIF){ 
	//alert (mensajeCifNif);
	;
   }else{
    alert (mensajeNIE);
   }	
   return false;
  }
}
function quitarCeros(valorX) {
	if (valorX.length>1) {
		var aux = valorX.substring(0,1);	
		while (aux=='0') {
			valorX = valorX.substring(1,valorX.length);
			aux = valorX.substring(0,1);	
		}
	}
	return valorX;
	
}
function formateaNIF(valorX) {
    var longitud=9;
	var salida='';
			if (valorX==null) {
			 
				
				salida = relleno("0",longitud);  
				
			} else {
				
				if (valorX.length==0) {
				 
					
						salida = relleno("0",longitud);  
					
				} else{
				   if (valorX.length > longitud) {
					// mayor
					
					salida = valorX.substring(valorX.length - longitud, valorX.length);  
				   }else{
				     if (valorX.length < longitud) {//menor
					 
					   salida = relleno('0',longitud - valorX.length) + valorX; 
					  
					   
					 }else{//igual
					  
					   salida = valorX;
					 }
				   }
				}   
				  
			}
   
	return salida; 
	
}

function relleno(caracter, longitud){
   var salida='';
    for (var cont = 0 ; cont < longitud ; cont ++){ 
	  salida += caracter;
	}
	
	return salida;
}



function compararFecha (dato1, dato2)
{
	String1 = "";
	String2 = "";
	if (typeof dato1 == "object")
		String1 = String1 + dato1.value;
	else
		String1 = String1 + dato1;
	
	if (typeof dato2 == "object")
		String2 = String2 + dato2.value;
	else
		String2 = String2 + dato2;
	
	if ((typeof String1 == "undefined") || (typeof String2 == "undefined")) 
		return -1;

	if ((String1 == "undefined") || (String2 == "undefined")) 
		return -1;

	if ((String1 == "") || (String2 == "")) 
		return -1;

	if (String1.substring(1,2)=="/") {
		String1="0"+String1
	}
	if (String1.substring(4,5)=="/"){
		String1=String1.substring(0,3)+"0"+String1.substring(3,9)
	}
	
	if (String2.substring(1,2)=="/") {
		String2="0"+String2
	}
	if (String2.substring(4,5)=="/"){
		String2=String2.substring(0,3)+"0"+String2.substring(3,9)
	}

		dia1 = String1.substring(0,2);
		mes1 = String1.substring(3,5);
		anyo1= String1.substring(6,10);

		dia2 = String2.substring(0,2);
		mes2 = String2.substring(3,5);
		anyo2= String2.substring(6,10);
	
	if (dia1 == "08") // parseInt("08") == 10 base octogonal
		dia1 = "8";
	if (dia1 == '09') // parseInt("09") == 11 base octogonal
		dia1 = "9";
	if (mes1 == "08") // parseInt("08") == 10 base octogonal
		mes1 = "8";
	if (mes1 == "09") // parseInt("09") == 11 base octogonal
		mes1 = "9";
	if (dia2 == "08") // parseInt("08") == 10 base octogonal
		dia2 = "8";
	if (dia2 == '09') // parseInt("09") == 11 base octogonal
		dia2 = "9";
	if (mes2 == "08") // parseInt("08") == 10 base octogonal
		mes2 = "8";
	if (mes2 == "09") // parseInt("09") == 11 base octogonal
		mes2 = "9";
	
	dia1 = parseInt(dia1);
	dia2 = parseInt(dia2);
	mes1 = parseInt(mes1);
	mes2 = parseInt(mes2);
	anyo1= parseInt(anyo1);
	anyo2= parseInt(anyo2);
	
	if ((anyo1 == anyo2) && (mes1 == mes2) && (dia1 == dia2))
	{
		return 0; 	// F1 = F2
	} 

	if (anyo1 > anyo2)
	{
		return 1; // F1 > F2 
	}
	
	if ((anyo1 == anyo2) && (mes1 > mes2))
	{
		return 1; // F1 > F2 
	}
	
	if ((anyo1 == anyo2) && (mes1 == mes2) && (dia1 > dia2))
	{
		return 1; // F1 > F2 
	} 

	return 2; // F1 < F2 
}

function validaNIE(nie){
 
 
  var nieNew=nie.toUpperCase();
  var nieAux=nieNew.substring(0,1);
  
  
  if ((nieAux=="X")||(nieAux=="Y")||(nieAux=="Z")){
     nieAux=str_replace(['X','Y','Z'],['0','1','2'], nieAux);
     nieNew = nieAux+nieNew.substring(1);
     if(esNIFCorrecto(nieNew,false)){
	  return true;
	 }
  }else{
   alert(mensajeNIE);
   return false; 
  }
  
}

function str_replace(search, replace, subject) {
    // http://kevin.vanzonneveld.net
    // +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   improved by: Gabriel Paderni
    // +   improved by: Philip Peterson
    // +   improved by: Simon Willison (http://simonwillison.net)
    // +    revised by: Jonas Raoni Soares Silva (http://www.jsfromhell.com)
    // +   bugfixed by: Anton Ongson
    // +      input by: Onno Marsman
    // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +    tweaked by: Onno Marsman
    // *     example 1: str_replace(' ', '.', 'Kevin van Zonneveld');
    // *     returns 1: 'Kevin.van.Zonneveld'
    // *     example 2: str_replace(['{name}', 'l'], ['hello', 'm'], '{name}, lars');
    // *     returns 2: 'hemmo, mars'
 
    var f = search, r = replace, s = subject;
    var ra = r instanceof Array, sa = s instanceof Array, f = [].concat(f), r = [].concat(r), i = (s = [].concat(s)).length;
 
    while (j = 0, i--) {
        if (s[i]) {
            while (s[i] = s[i].split(f[j]).join(ra ? r[j] || "" : r[0]), ++j in f){};
        }
    };
 
    return sa ? s : s[0];
} 			  		




function validarNIFCIF (tipo, num) {
	/*if (tipo == <%=ClsConstants.TIPO_IDENTIFICACION_CIF %>)   return ValidaCIF (num);			 com.atos.utils.ClsConstants.TIPO_IDENTIFICACION_CIF*/
	if (tipo == <%=ClsConstants.TIPO_IDENTIFICACION_NIF %>){// com.atos.utils.ClsConstants.TIPO_IDENTIFICACION_NIF o com.atos.utils.ClsConstants.TIPO_IDENTIFICACION_CIF
	   if (!esNIFCorrecto(num,true)){
	   	 return ValidaCIF (num);
	   }else{ 
	     return true;
	   }
	}   
	if (tipo == <%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE %>)   return validaNIE (num);	// com.atos.utils.ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE
	if (tipo == <%=ClsConstants.TIPO_IDENTIFICACION_PASAPORTE %>)   return true;	// com.atos.utils.ClsConstants.TIPO_IDENTIFICACION_PASAPORTE
	return false;
}

function validarTelefono (numero){
 
 if (numero!=""){

 var longitud=numero.length;

 
 var telefono=numero;
 
 var valido=true;
 
  if (longitud < 9 || longitud > 14){//Es erroneo los numeros telefonicos cuya longitud es menor que 9 y mayor de 14, ademas tampoco son validos
                                     // los numeros de longitud 9 con un (+) delante.
									 
   
    return false;
  }else{
  
    var checkear="0123456789";
	
	if (telefono.substring(0,1)=="+"){
	  if (longitud==9){
	    
		return false;
	  }else{
	 	   telefono=telefono.substring(1);
	  }	   
	}
	
	
	
	for (i = 0; i < telefono.length; i++){
		ch = telefono.charAt(i);
		
	  for (j = 0; j < checkear.length; j++){
	   
			if (ch != checkear.charAt(j)){
			  if (j == checkear.length-1)
				{
				  
					valido = false;
					break;
					
				}
			}else{
			   valido=true;
			   break;
			}
	  }
	  		
		if (!valido)
		{
		 
		  return (false);
		}
	      
	}  
}
 return (true);
} 
}

function eliminarBlancos(numeroATratar){
  var longitud=numeroATratar.length;
 
  for (i=0; i < longitud; i++){ 
   	numeroATratar = numeroATratar.replace(' ',''); 
			  
  } 	
  
  return numeroATratar;
}

  

// RGG 12/09/2007 tratamiento de extensiones de fichero
var mensajeFichero="<siga:Idioma key="messages.ficheros.tipoFicheroErroneo"/>";
var mensajeFichero2="<siga:Idioma key="messages.ficheros.tipoFicheroErroneo2"/>";
function TestFileType( fileName, fileTypes ) {
	fileName = fileName.toUpperCase();
	if (!fileName) return true;
	dots = fileName.split(".")
	fileType = dots[dots.length-1];
	var variable = fileTypes.join(".").indexOf(fileType) != -1;
	if (variable == false)
		alert(mensajeFichero+ "\n\n" + (fileTypes.join(" .")) + "\n\n" +mensajeFichero2);
	return variable;
}
function isFicheroPermitido( fileName, fileTypes ) {
	fileName = fileName.toUpperCase();
	if (!fileName){ 
		return false;
	}
	dots = fileName.split(".")
	fileType = dots[dots.length-1];
	var variable = fileTypes.join(".").indexOf(fileType) == -1;
	return variable;
}
function getDigitoControl(valor){	
	  valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
	  control = 0;
	  for (i=0; i<=9; i++)
	  	control += parseInt(valor.charAt(i)) * valores[i];		  
	  	control = 11 - (control % 11);
		if (control == 11) control = 0;
		else if (control == 10) control = 1;
		return control;
	
	}
	
	function getDigitosControl(codigoBanco,sucursal,numeroCuenta){
		if(!isNumero(codigoBanco)||!isNumero(sucursal)||!isNumero(numeroCuenta))
			return -3;
		digitocontrol =  getDigitoControl('00'+codigoBanco+sucursal)+''+getDigitoControl(numeroCuenta);	
	  	return digitocontrol;
	  
	}
	function validarDigitoControl(codigoBanco,sucursal,digitoControl,numeroCuenta){
		 
		if (codigoBanco    == ''  || sucursal == '' || numeroCuenta == ''  || digitoControl   == '' ) 
			 return -1;
		else if(codigoBanco.length != 4 || sucursal.length != 4 ||digitoControl.length != 2 || numeroCuenta.length != 10)
			return -2;
		else if(!isNumero(codigoBanco)||!isNumero(sucursal)||!isNumero(digitoControl)||!isNumero(numeroCuenta))
			return -3;
		else if(digitoControl != getDigitosControl(codigoBanco,sucursal,numeroCuenta))
			return -4;
		else	
			return 0;

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
	
	// Funciones para obtener el d¡gito de control de la Cuenta
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
	
	function validarIBAN(iban) {
		var newIban = iban.toUpperCase(),		
		modulo = function(divident, divisor){
			var m = 0;
		  	for (var i = 0; i < divident.length; ++i)
		    	  m = (m * 10 + parseInt(divident.charAt(i))) % divisor;
		  	return m;
		};
		 
		if (newIban.search(/^[A-Z]{2}/gi) < 0) {
			return false;
		}
		 
		newIban = newIban.substring(4) + newIban.substring(0, 4);
		 
		newIban = newIban.replace(/[A-Z]/g, function (match) {
			return match.charCodeAt(0) - 55;
		});
		 
		return parseInt(modulo(newIban, 97), 10) === 1;
	}
	
	function calcularDigitoCuentaBancariaEspañola(cuenta){
		mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";
		if(!numerico(cuenta)){
			alert(mensaje);
			return false;
		
		} else {
			if(cuenta.substring(8,10) != obtenerDigito("00" + cuenta.substring(0,4) + cuenta.substring(4,8)) + "" + obtenerDigito(cuenta.substring(10))){
				alert(mensaje);
				return false;
			}
		}
		
		return true;  
	}	

  