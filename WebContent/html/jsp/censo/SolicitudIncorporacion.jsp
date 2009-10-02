<!-- SolicitudIncorporacion.jsp -->
<!-- EJEMPLO DE VENTANA DE DETALLE DE UN REGISTRO -->
<!-- Contiene un posible titulo del mantenimiento, ademas de la zona de campos
	 a mantener, que utilizara conjuntos de datos si fuera necesario.
	 Bajo esta zona y sin salirse del tamanho estandar de la ventana existira
	 una zona de botones de acciones sobre el registro.
-->

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

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm" %>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.ArrayList" %>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
   
    String fechaSol = (String) request.getAttribute("fechaSol");
    int tipoIdenNIF = ClsConstants.TIPO_IDENTIFICACION_NIF;
    int tipoIdenCIF = ClsConstants.TIPO_IDENTIFICACION_CIF;
	int tipoIdenNIE = ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE;
	
	ArrayList selTipoIdentificacion = new ArrayList();
	selTipoIdentificacion.add (""+tipoIdenNIF);

	String pais="";
	ArrayList idPais=new ArrayList();
//  Lo dejamos en blanco para Cataluña (pero se inserta españa si en blanco)
//	idPais.add(ClsConstants.ID_PAIS_ESPANA);

	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String [] modalidadParam = new String[1];
	modalidadParam[0] = user.getLocation();

	
%>

<style>
.ocultar {display:none}
</style>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<script language="JavaScript" src="<%=app%>/html/js/validation.js" type="text/jscript"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<html:javascript formName="SolicitudIncorporacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
    <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera2" 
							 localizacion="censo.solicitudIncorporacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

<script>

		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		
		//refrescarLocal -->
		function refrescarLocal() 
		{		
			document.SolicitudIncorporacionForm.modo.value="abrirAvanzada";
			document.SolicitudIncorporacionForm.target="mainWorkArea";
			document.SolicitudIncorporacionForm.submit();
		}
		
		function formatearDocumento()
	{
	   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&(document.forms[0].NIFCIF.value!="")) {
			var sNIF = document.forms[0].NIFCIF.value;
			document.forms[0].NIFCIF.value = formateaNIF(sNIF);
	   }else if((document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>")){
	   
	   		var sNIE = document.forms[0].NIFCIF.value;
			document.forms[0].NIFCIF.value = formateaNIE(sNIE);
	   }
	   	
}
		
		
	    function selPais(valor) {                                                                   
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
			   	document.getElementById("provincia").disabled=true;
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				document.getElementById("provincia").disabled=false;
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
	       }
	    }
		
		function validarTelef(numero){
		 alert(numero.value);
		}
		
		function comprobarApellido2Asterisco(){
		 
		  var checkNIF = false;
			if (document.SolicitudIncorporacionForm.tipoIdentificacion)
			
			if (document.SolicitudIncorporacionForm.tipoIdentificacion.options) {
			
			  for(i = 0; i < document.SolicitudIncorporacionForm.tipoIdentificacion.options.length; i++){  			
				if(document.SolicitudIncorporacionForm.tipoIdentificacion.options[i].selected) {
			
					if(document.SolicitudIncorporacionForm.tipoIdentificacion.options[i].value == 10)
						checkNIF = true;
				}
 			  }
			}
			
			if(checkNIF) {
				document.getElementById("apellidoSinAsterisco").className="ocultar";
				document.getElementById("apellidoConAsterisco").className="labelText";
			}
			else {
				document.getElementById("apellidoSinAsterisco").className="labelText";
				document.getElementById("apellidoConAsterisco").className="ocultar";
	    	}
		}
		
		function formateaNIF(valorX) {
    		var longitud=9;
    		var salida='';
    		
    		if(isNumero(valorX)==true)
    			longitud=8;
    		
    		
    		if(valorX.length==8 && isNumero(valorX)==true)
    			return valorX;
    		
			if (valorX==null) {
				salida = relleno("0",longitud);  
			} else {
				
				if (valorX.length==0) {
				 
					
						salida = relleno("0",longitud);  
					
				} else{
				   if (valorX.length > longitud) {
					// mayor
					
					alert ("<siga:Idioma key="messages.nif.comprobacion.digitos.erroneo"/>");
					return valorX;
				   }else{
				     if (valorX.length < longitud) {//menor
					 
					   salida = relleno('0',longitud - valorX.length) + valorX; 
					  
					   
					 }else{//igual
					  
					   salida = valorX;
					 }
				   }
				}   
				  
			}
  // alert("Salida formateaNIFMio:"+salida);
	return salida; 
	
}
	function formateaNIE(valorX) {
		
    		var longitud=8;
    		var salida='';
    		
    		if( isNumero(valorX)==true){
    			if(valorX.length==8)
    				return valorX;
    		}else{
    			if(valorX.length==9)
    				return valorX;
    			//else
    				//longitud=9;
    		
    		}
    		
    		
			if (valorX==null) {
				//salida = relleno("0",longitud); 
				 
			} else {
				
				if (valorX.length==0) {
				 
					
						//salida = relleno("0",longitud);
				  
					
				} else{
				   if (valorX.length > longitud) {
					// mayor
					
					alert ("<siga:Idioma key="messages.nie.comprobacion.digitos.erroneo"/>");
					return valorX;  
				   }else{
				     if (valorX.length < longitud) {//menor
					   valorX1 = valorX.substring(1);
					   salida = valorX.substring(0,1)+relleno('0',longitud - valorX1.length-1) + valorX1; 
					   
					   
					 }else{//igual
					  
					   salida = valorX;
					 }
				   }
				}   
				  
			}
  // alert("Salida formateaNIFMio:"+salida);
	return salida; 
	
}

	
		
		
		
		
		
	function generarLetra() {
		var numId = SolicitudIncorporacionForm.NIFCIF.value;
		var tipoIdentificacion = SolicitudIncorporacionForm.tipoIdentificacion.value;
	  	var letra='TRWAGMYFPDXBNJZSQVHLCKET';
		if( (tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")){
		if(numId.length==0){
		
			return false;
		
		}if(isNumero(numId)==true){
				if(numId.length==8){
				 	numero = numId;
				 	numero = numero % 23;
				 	letra=letra.substring(numero,numero+1);
				 	SolicitudIncorporacionForm.NIFCIF.value =numId+letra;
				 		
				}else{
				 	numero = numId.substr(0,numId.length-1);
				 	numero = numero % 23;
				 	let = numId.substr(numId.length-1,1);
				 	letra=letra.substring(numero,numero+1);
		 			//if (letra.tolowercase()!=let.tolowercase())
				 	//alert('DNI Erroneo. Ponemos el correcto');
					SolicitudIncorporacionForm.NIFCIF.value = numId.substring(0,8)+letra;
					
				 } 	
				 	
			}else{
				rc = validarNIFCIF(tipoIdentificacion, numId);
				 
				if(rc==true)
					alert('<siga:Idioma key="messages.nifcif.comprobacion.correcto"/>');
				
			}
		} else	if((tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){
			if(numId.length==8){
				if(isNumero(numId)==true){
					numero = numId;
					//Buscamo la letra derecha. Aplicamos algoritmo de letra derecha
					var numero_23 = numero % 23;
					letra=letra.substring(numero_23,numero_23+1);
					//Sustituimos primer numero por letra
					primerNumero = numId.substring(0,1);
				 	if(primerNumero==0)
				 		primerNumero = 'X';
				 	else if  (primerNumero==1)
				 		primerNumero = 'Y';
				 	else if  (primerNumero==2)
				 		primerNumero = 'Z';
				 	
				 	numero = primerNumero+numId.substring(1);
				 	
				 	//añadimos la letra derecha
				 	numero = numero + letra;
				 	
					SolicitudIncorporacionForm.NIFCIF.value = numero;
					 	
					 	
					 	
					
				}else{
					//Miramos que solo sea letra el primer caracter. si no lo es no hacemos nada
					if(isNumero(numId.substring(1))==false){
						return false;
					
					}
					//Sustituimos primera letra por numero para aplicar algoritmo para buscar la letra derecha
					
				  		primeraLetra = numId.substring(0,1);
				    	if(primeraLetra.toUpperCase()=='X')
				 		primeraLetra = '0';
				 	else if  (primeraLetra.toUpperCase()=='Y')
				 		primeraLetra = '1';
				 	else if  (primeraLetra.toUpperCase()=='Z')
				 		primeraLetra = '2';
				 	else{
				 		//Si no es X o Y o Z no hacemos nada 
				 		return false;
				 	}
				 	//Sistituimos la letra por el numero
				 	numero = primeraLetra+numId.substring(1);
				 	//Aplicamos algoritmo de letra derecha
				 	var numero_23 = numero % 23;
					letra=letra.substring(numero_23,numero_23+1);
					
					//Le ponemos la letra derecha al alfanumerico inicial					
					numero = numId +letra;
					SolicitudIncorporacionForm.NIFCIF.value = numero;
				  	
				  	
				}	
			}else{
				rc = validaNIE(numId);
				 
				if(rc==true)
					alert('<siga:Idioma key="messages.nifcif.comprobacion.correcto"/>');
			}
			
		}else {
			//Si no es nif ni nie no hay generacion de letra
			return true;
		}
	}
	
	function validarNIE(a) 
	{
		var temp=a.toUpperCase();
		var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 
 
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
		return 0;
	} 		
		
	
	
	function nif(a) 
{
	var a = SolicitudIncorporacionForm.tipoIdentificacion.value;
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
			alert("comp");
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
		

		
			
</script>
			
</head>

<body onload="comprobarApellido2Asterisco()">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->


	<html:form action = "/SIN_SolicitudIncorporacion.do" focus = "tipoSolicitud" >

	<html:hidden property = "modo" value = ""/>


	<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">

	<!-- SUBCONJUNTO DE DATOS -->
	<table align="center">

		<tr>				
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.solicitudDe"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoSolicitud" tipo="solicitud" clase="boxCombo" obligatorio="true"/></td>
		
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tipoColegiacion"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoColegiacion" tipo="colegiacion" clase="boxCombo"  obligatorio="true"/></td>

<!-- -->	<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaSolicitud"/></td>
			<td><input value="<%=fechaSol%>" type="text" name="fechaSolicitud" class="boxConsulta" readonly="true"></td>
		</tr>

		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tipoIdentificacion"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoIdentificacion" tipo="identificacionSolicitud" clase="boxCombo" obligatorio="true" ElementoSel="<%=selTipoIdentificacion%>" accion="comprobarApellido2Asterisco();"/> </td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif"/>&nbsp;(*)</td>
			<td><input name="NIFCIF" type="text" class="box" size="20" maxlength="20" onBlur="formatearDocumento();"></td>
			<td colspan="2"><input type="button" name="idButton" value='<siga:Idioma key="censo.nif.letra.letranif" />' onclick="generarLetra();" style="align:right" class="button" onBlur="formatearDocumento();"></td> 
		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>&nbsp;(*)</td>
			<td>
				<html:select name="SolicitudIncorporacionForm" property="sexo" style = "null" styleClass = "box" readonly="false" >
			        <html:option value="0" >&nbsp;</html:option>
					<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE%>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
					<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER%>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
				</html:select>									
			</td>
			
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tratamiento"/>&nbsp;(*)</td>
			<td colspan="3">	<siga:ComboBD nombre="tipoDon" tipo="tratamiento" clase="boxCombo"  obligatorio="true"/></td>			
		</tr>
	
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>&nbsp;(*)</td>
			<td><input type="text" name="nombre" maxlength="100" class="box"></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido1"/>&nbsp;(*)</td>
			<td><input type="text" name="apellido1"  maxlength="100" class="box"></td>

			<td class="labelText" id="apellidoSinAsterisco"><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/>&nbsp;</td>
			<td class="ocultar" id="apellidoConAsterisco"><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/>&nbsp;(*)</td>
			<td ><input type="text" name="apellido2"  maxlength="100" class="box"></td>
		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.estadoCivil"/></td>
			<td><siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="boxCombo"/></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaNacimiento"/>&nbsp;(*)</td>
<!-- -->	<td><input type="text" name="fechaNacimiento" maxlength="10" class="box" readonly="true"><a href='javascript://'onClick="return showCalendarGeneral(fechaNacimiento);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.naturalDe"/></td>
			<td><input type="text" name="natural" maxlength="100" class="box"></td>
		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.domicilio"/>&nbsp;(*)</td>
			<td><input type="text" name="domicilio" maxlength="100" class="box"></td>

			<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.pais2"/>&nbsp;</td>
			<td colspan="3">
				<siga:ComboBD nombre="pais" tipo="pais" clase="boxCombo" obligatorio="false" elementoSel="<%=idPais%>" accion="selPais(this.value);"/>
			</td>

		</tr>

		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.codigoPostal"/>&nbsp;(*)</td>
			<td><input type="text" name="CP" class="box" maxlength="5"></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>&nbsp;(*)</td>
			<td colspan="3"><siga:ComboBD nombre = "provincia" tipo="provincia" clase="boxCombo" obligatorio="true" accion="Hijo:poblacion"/></td>

		</tr>
		
		<tr>
			<td class="labelText">&nbsp;</td>
			<td>&nbsp;</td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/> &nbsp;(*)</td>
			<td id="poblacionEspanola" colspan="3">
				<siga:ComboBD nombre = "poblacion" tipo="poblacion" clase="boxCombo" obligatorio="true" hijo="t" ancho="335"/> 
			</td>				
			<td class="ocultar" class="ocultar"  colspan="3" id="poblacionExtranjera">
					<input type="text" name="poblacionExt" value='' size="30" maxlength="100" class="boxCombo"></input>
			</td>

		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono1"/>&nbsp;(*)</td>
			<td><input type="text" name="telefono1" maxlength="20" class="box" ></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono2"/></td>
			<td><input type="text" name="telefono2" maxlength="20" class="box" ></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono3"/></td>
			<td><input type="text" name="telefono3" maxlength="20" class="box" ></td>

		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax1"/></td>
			<td><input type="text" name="fax1" maxlength="20" class="box"></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax2"/></td>
			<td><input type="text" name="fax2" maxlength="20" class="box"></td>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>&nbsp;(*)</td>
			<td><input type="text" name="mail" maxlength="100" class="box"></td>
		</tr>
		
		<tr>
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.observaciones"/></td>
			<td colspan="3"><textarea cols="120" rows="4" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" name="observaciones" style="overflow:hidden" class="box"></textarea></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.documentacion"/>&nbsp;(*)</td>
			<td colspan="">
				<siga:ComboBD nombre = "tipoModalidadDocumentacion" tipo="modalidadDocumentacion" clase="boxCombo" obligatorio="true" parametro="<%=modalidadParam%>" />
			</td>
		</tr>

	</table>

	</siga:ConjCampos>

	</html:form>

	<!-- FIN: CAMPOS DEL REGISTRO -->


	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->

		<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton Volver -->
		function accionVolver() 
		{		
			window.location = "<%=app%>/html/jsp/censo/SolicitudIncorporacionValidacion.jsp";
		}
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.SolicitudIncorporacionForm.modo.value="";
				document.SolicitudIncorporacionForm.reset();
				document.SolicitudIncorporacionForm.provincia.onchange();
			}
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{   sub();
		
			var numeroATratar=trim(SolicitudIncorporacionForm.telefono1.value);
			SolicitudIncorporacionForm.telefono1.value=eliminarBlancos(numeroATratar);
			
			var numeroATratar2=trim(SolicitudIncorporacionForm.telefono2.value);
			SolicitudIncorporacionForm.telefono2.value=eliminarBlancos(numeroATratar2);
			
			if (validateSolicitudIncorporacionForm(document.SolicitudIncorporacionForm)) 
			{  
				if(isAfter(SolicitudIncorporacionForm.fechaNacimiento.value,SolicitudIncorporacionForm.fechaSolicitud.value)) { 
					alert('<siga:Idioma key="censo.SolicitudIncorporacion.literal.nacimiento"/>');
					fin(); 
			     	return false;
				}			
			    if (document.SolicitudIncorporacionForm.sexo.value=='0'){
		          alert ('<siga:Idioma key="censo.fichaCliente.literal.sexo"/>');
		          fin();
     		      return false;
     		    }
				if (trim(SolicitudIncorporacionForm.pais.value)=="" || SolicitudIncorporacionForm.pais.value==idEspana) {
			   		if (document.SolicitudIncorporacionForm.provincia.value == "") {
		     	    	alert('<siga:Idioma key="messages.campoObligatorio.error"/> <siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>');
		     	    	fin();
			     	    return false;
			       	}
			   		if (document.SolicitudIncorporacionForm.poblacion.value == "") {
		     	    	alert('<siga:Idioma key="messages.campoObligatorio.error"/> <siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>');
		     	    	fin();
			     	    return false;
			       	}
			    } else {
			   		if (document.SolicitudIncorporacionForm.poblacionExt.value == "") {
			   		
		     	    	alert('<siga:Idioma key="messages.campoObligatorio.error"/> <siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>');
		     	    	fin();
			     	    return false;
			       	}
 			    }				       	

				var rc	= true;
				var tipoIden = document.SolicitudIncorporacionForm.tipoIdentificacion.value;
				if ((tipoIden == <%=tipoIdenNIF%>)){
					rc = validarNIFCIF(tipoIden, document.SolicitudIncorporacionForm.NIFCIF.value);
					
					
					//Valido si todo ha ido bien y es tipo NIF que el segundo apellido es obligatorio:
					if (rc && (tipoIden == <%=tipoIdenNIF%>) && (trim(document.SolicitudIncorporacionForm.apellido2.value)=='')){
						alert('<siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
						fin();
						return false;
					}
				}else if(tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
					rc=validaNIE(document.SolicitudIncorporacionForm.NIFCIF.value);
					
					
					
				}
			
				if (rc) {
					document.SolicitudIncorporacionForm.modo.value="insertar";
					document.SolicitudIncorporacionForm.target="submitArea";
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
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>