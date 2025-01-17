<!DOCTYPE html>
<html>
<head>
<!-- consultaDatosGenerales.jsp -->

<!-- CABECERA JSP -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.siga.beans.CenInstitucionBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<%  
	// Controles generales
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String idInstitucionAcceso= user.getLocation();
	
	// Datos formulario y sesion
	DatosGeneralesForm formulario = (DatosGeneralesForm)request.getAttribute("datosGeneralesForm");
	String idPersonaSolicitud = formulario.getIdPersona();
	String modo = formulario.getAccion();
	boolean bOcultarHistorico = user.getOcultarHistorico();
	String mostrarMensaje = (String) request.getSession().getAttribute("MOSTRARMENSAJE");
	String mostrarMensajeNifCif = (String) request.getSession().getAttribute("MOSTRARMENSAJECIFNIF");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	
	// Obtencion colegiado
    String numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
    boolean bColegiado;
	if (numeroColegiado!=null) {
		bColegiado=true;
	} else {
		bColegiado=false;
	}
	
	// Obteniendo varias cosas
	ArrayList gruposSel = new ArrayList();	
	String cliente = "";
	boolean bfCertificado=true;
	String fechaCertificado = "";
	Vector resultado = null;
	
	if (!formulario.getIdPersona().equals("")) {
		// Obteniendo atributos varios
		resultado = (Vector) request.getAttribute("CenResultadoDatosGenerales");
		
		// Obteniendo grupos
		String[] arrayGruposSel = formulario.getGrupos();
		if (arrayGruposSel!=null) {
			for (int j=0;j<arrayGruposSel.length;j++) {
				gruposSel.add(arrayGruposSel[j]);
			}
		}
		
		// Obteniendo estado colegial/no colegial
		String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
        if (colegiado!=null && !colegiado.equals("")){
			cliente = UtilidadesString.getMensajeIdioma(user, colegiado);
		}else{
			cliente = UtilidadesString.getMensajeIdioma(user,"censo.busquedaClientes.literal.sinEstadoColegial");
		}
		
        // Obteniendo certificado incorporacion
		fechaCertificado = (String) request.getAttribute("CenDatosGeneralesCertificado");
		if (fechaCertificado==null || fechaCertificado.equals("")) {
			fechaCertificado=UtilidadesString.getMensajeIdioma(user, "censo.ConsultaDatosGenerales.mensaje.NoCertificado");
			bfCertificado=false;
		}
	} 
	
	// Definicion de variables a usar en la JSP
	ArrayList tipoIdentificacionSel = new ArrayList();
	ArrayList estadoCivilSel = new ArrayList();
	ArrayList idiomaSel = new ArrayList();
	ArrayList caracterSel = new ArrayList();
	String idInstitucion = "";
	String idPersona = formulario.getIdPersona();
	String fotografia = "";
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	String apellidos = "";
	String numero = "";
	String nIdentificacion = "";
	String fechaNacimiento = "";
	String fechaAlta = "";
	String nacido = "";
	String estad = "";
	String publicidad = "";
	String guiaJudicial = "";
	String abono = "";
	String cargo = "";
	String comisiones = "";
	String noRevista = "";
	String noRedAbogacia = "";
	String usoFoto = "";
	Boolean descripcion = null;
	String cuentaContable = ""; 
	String sexo = "";
	String idioma = "";
	String estadoCivil = "";
	String caracter = "";
	String tipoi = "";
	String fallecido = "";
	String checkFallecido="";
	String edad= "";
	String estadoColegial = ""; 
	String idTratamiento = "";
	estadoColegial = (String) request.getAttribute("ESTADOCOLEGIAL");
	int tipoIdenNIF = ClsConstants.TIPO_IDENTIFICACION_NIF;
    int tipoIdenCIF = ClsConstants.TIPO_IDENTIFICACION_CIF;
    String paramsTipoIdenJSON = "{\"idtipoidentificacion\":\"-1\"}";
    
	// Obteniendo idinstitucion
	String [] institucionParam = new String[1];
	if (formulario.getIdInstitucion()!= null && !formulario.getIdInstitucion().equals("")) {
   		institucionParam[0] = formulario.getIdInstitucion();
	} else {
	   	institucionParam[0] = user.getLocation();
	}
	
	// Obteniendo Tipo de cliente
    String sTipo = (String) request.getAttribute("TIPO");
	if (sTipo == null)
		sTipo = "";
	String tipoCliente="";
	if (bColegiado) { 
		tipoCliente = ClsConstants.TIPO_CLIENTE_COLEGIADO;
	} else if (sTipo.equalsIgnoreCase("LETRADO")) {
		tipoCliente = ClsConstants.TIPO_CLIENTE_LETRADO;
	} else { 
		tipoCliente = ClsConstants.TIPO_CLIENTE_NOCOLEGIADO;
	}
	
	// Calculando estilos generales en funcion de Ver o Editar
	String estiloCaja; // para los textos
	String readonly;  // para el combo
	boolean breadonly;  // para lo que no es combo y en general indica si es modo lectura o no
	String checkreadonly; // para el check
	if (formulario.getAccion().equals("ver")) {
		// caso de consulta
		estiloCaja = "boxConsulta";
		readonly = "true";
		breadonly = true;
		checkreadonly = " disabled ";
	} else {
		estiloCaja = "box";
		readonly = "false";
		breadonly = false;
		checkreadonly = " ";
	}
	
	// Calculando estilo del Check de Foto
	String checkreadonlyFoto = " ";
	boolean esLetrado = false;
	if (user.isLetrado()) {
		checkreadonlyFoto = " ";
		esLetrado = true;
	} else { 
		if (formulario.getAccion().equals("ver")) {
			checkreadonlyFoto = " disabled ";
		}else{
			checkreadonlyFoto = " ";
		}
	}
	
	// Calculando estilos para controlar edicion de datos generales de persona
	boolean bDatosGeneralesEditables = ((String) request.getAttribute("BDATOSGENERALESEDITABLES")).equals("true") ? true : false;
%>

	<!-- HEAD -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>">
	</script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="datosGeneralesForm" staticJavascript="false" />  
	 	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/censo/posiblesDuplicados.js?v=${sessionScope.VERSIONJS}'/>" type="text/javascript"></script>
	
	
		
	
	<script>
		// Si fechaNacimiento es editable, indica si es valida o no (ver esFechaNacimientoInvalida)
		function comprobarFechaNacimiento(valorFechaNacimiento) {
			if (!jQuery("#fechaNacimiento").is('[readonly]') && esFechaNacimientoInvalida(valorFechaNacimiento)) {
				alert("<siga:Idioma key='errors.date.past' arg0='censo.consultaDatosGenerales.literal.fechaNacimiento'/>");
				return false;
			} else {
				return true;
			}
		}
		
		// Funcion asociada a buscarGrupos()
		function buscarGrupos(){
				document.GruposClienteClienteForm.modo.value="buscar";
				document.GruposClienteClienteForm.modoAnterior.value=document.forms[0].accion.value;				
				document.GruposClienteClienteForm.idPersona.value=document.forms[0].idPersona.value;	
				document.GruposClienteClienteForm.idInstitucion.value=document.forms[0].idInstitucion.value;	
				document.GruposClienteClienteForm.target="resultado";	
				document.GruposClienteClienteForm.submit();	
		}
		
		function refrescarLocal() {
	
				<% if (modo.equalsIgnoreCase("NUEVO")) { %>
				document.forms[0].accion.value="editar";
				<% } %>
				document.forms[0].modo.value="abrir";
				
				document.forms[0].submit();				
				
				//Refresco el iframe de grupos:
				buscarGrupos();				
	
			}
			
		//Funcion que quita blancos a derecha e izquierda de la cadena
		function fTrim(Str)	{				 
			Str = Str.replace(/(^\s*)|(\s*$)/g,"");			
			return Str;
		}
				
		function formatearDocumento(){
	       //Se aplica la funcion ftrim para eliminar blancos
		   document.forms[0].numIdentificacion.value = fTrim(document.forms[0].numIdentificacion.value);
		   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&(document.forms[0].numIdentificacion.value!="")) {
				var sNIF = document.forms[0].numIdentificacion.value;
				var sNIFFormateado = formateaNIF(sNIF);
				
				if(esNIFCorrecto(sNIFFormateado,true)){
					document.forms[0].numIdentificacion.value = sNIFFormateado;				
				}else{
					document.forms[0].numIdentificacion.value = sNIF;
				}
				
		   }else if((document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){		   
		   		var sNIE = document.forms[0].numIdentificacion.value;
		   		var sNIEFormetado = formateaNIE(sNIE);
		   		
				if(esNIECorrecto(sNIEFormetado,true)){
					document.forms[0].numIdentificacion.value = sNIEFormetado;				
				}else{
					document.forms[0].numIdentificacion.value = sNIE;
				}	   		
		   }
		}
		
		function generaNumOtro() {
			//Ejerciente - Letrado
			
		   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") && (document.forms[0].numIdentificacion.value=="")) {
			   <%if (cliente.equals("") || cliente.equals("No Colegiado")){%>
					generarIdenHistorico();
				<%}%>
				//NIHN (N�mero de Identificaci�n Hist�rico para No colegiados) = 'NIHN' + idinstitucion + [0-9]{4}, donde el ultimo numero sera un max+1. Ej. NIHN20400011
			}
		   	
		}	
		
		jQuery(document).ready(function(){
			jQuery(".box").change(function() {	
			//jQuery("input[type=select][name='tipoIdentificacion']").change(function(){
				if(jQuery(this).attr("name")=="tipoIdentificacion"){
					generaNumOtro();
				}
			 });	 
		});
			
		function generarIdenHistorico() {
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
	           type: "POST",
	           url: "/SIGA/CEN_DatosGenerales.do?modo=getIdenHistorico",
	           data: "idInstitucion="+'<%=institucionParam[0]%>',
	           //contentType: "application/json; charset=utf-8",
	           dataType: "json",
	           success:  function(json) {
		           		document.forms[0].numIdentificacion.value=json.numHistorico;
	           },
	           error: function(xml,msg){
	        	   //alert("Error1: "+xml);//jQuery("span#ap").text(" Error");
	        	   alert("Error: "+msg);//jQuery("span#ap").text(" Error");
	           }
	        }); 
		}
	
		function adaptaTamanoFoto () {
			widthMax = 180;
			heightMax = 240;
			foto = document.getElementById ("fotoNueva");
			if (foto) {
				ratio = foto.height / foto.width;
	
				if (foto.width  > widthMax ) {
					foto.width  = widthMax;
					foto.height = widthMax * ratio;
				}
	
				if (foto.height > heightMax) { 
					foto.height = heightMax;
					foto.widtht = heightMax / ratio;
				}
			}
			return;
		}
			
		function formateaNIF(valorX) {
	   		var longitud=9;
	   		var salida='';
	   		valorX = quitarCeros(valorX);
	   		
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
			return salida; 
		}
			
		function generarLetra() {
			var numId = datosGeneralesForm.numIdentificacion.value;
			var tipoIdentificacion = datosGeneralesForm.tipoIdentificacion.value;
		  	var letra='TRWAGMYFPDXBNJZSQVHLCKET';
			if(numId.length==0){
				return false;		
			}if( (tipoIdentificacion == "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")){
				if(isNumero(numId)==true){
					if(numId.length==8){
					 	numero = numId;
					 	numero = numero % 23;
					 	letra=letra.substring(numero,numero+1);
					 	datosGeneralesForm.numIdentificacion.value =numId+letra;
					 		
					}else{
					 	numero = numId.substr(0,numId.length-1);
					 	numero = numero % 23;
					 	let = numId.substr(numId.length-1,1);
					 	letra=letra.substring(numero,numero+1);
		 			 	//if (letra.tolowercase()!=let.tolowercase())
				 		//alert('DNI Erroneo. Ponemos el correcto');
						datosGeneralesForm.numIdentificacion.value = numId.substring(0,8)+letra;
						
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
					 	
					 	//a�adimos la letra derecha
					 	numero = numero + letra;
					 	
						datosGeneralesForm.numIdentificacion.value = numero;
						 	
						 	
						 	
						
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
						datosGeneralesForm.numIdentificacion.value = numero;
					  	
					  	
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
		
		function esNIECorrecto(nie){
			var nieNew=nie.toUpperCase();
		  	var nieAux=nieNew.substring(0,1);
			  
		  	if ((nieAux=="X")||(nieAux=="Y")||(nieAux=="Z")){
				nieAux=str_replace(['X','Y','Z'],['0','1','2'], nieAux);
		     	nieNew = nieAux+nieNew.substring(1);
		     	if(esNIFCorrecto(nieNew,false)){
			 		return true;
			 	}
			  
		  	} else {
	   			return false; 
		  	}
		}
		
		function validarNIE(a) {
			var temp=a.toUpperCase();
			var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
	 		
			//comprobacion de NIEs
			//T
			if (/^[T]{1}/.test(temp))
			{
				
				if (a[8] == /^[T]{1}[A-Z0-9]{8}jQuery/.test(temp))
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
				alert("comp");
				pos = str_replace(['X', 'Y', 'Z'], ['0','1','2'], temp).substring(0, 8) % 23;
				alert("comp1"+a[8]);
				alert("comp10"+cadenadni.substring(pos, pos + 1));
				if (a[8] == cadenadni.substring(pos, pos + 1))
				
				{
					alert("comp2");
					return 3;
				}
				else
				{
					alert("comp3");
					return -3;
				}
			}
			alert("comp4");
			return 0;
		} 		
		
		function nif(a) {
			var a = datosGeneralesForm.numIdentificacion.value;
			var temp=a.toUpperCase();
			var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
		 
			if (temp!==''){
				//si no tiene un formato valido devuelve error
				if ((!/^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}jQuery/.test(temp) && !/^[T]{1}[A-Z0-9]{8}jQuery/.test(temp)) && !/^[0-9]{8}[A-Z]{1}jQuery/.test(temp))
				{
					return 0;
				}
		 
				//comprobacion de NIFs estandar
				if (/^[0-9]{8}[A-Z]{1}jQuery/.test(temp))
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
				for (var i = 1; i < 8; i += 2) {
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
					if (a[8] == /^[T]{1}[A-Z0-9]{8}jQuery/.test(temp))
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
	
		function mostrarMensaje() {		
			<% if (mostrarMensaje!=null && !(mostrarMensaje.equals(""))) { %>			
				alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.existeEnBBDD"/>");
			<% }else if(mostrarMensajeNifCif!=null && !(mostrarMensajeNifCif.equals(""))){ %>
				alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.existeEnBBDDNIFCIF"/>");
			<%}%>			
		}
			
		
		//Funcion encargada de obtener los tratamientos a partir del sexo seleccionado
		function obtenerTratamientos (idSexo,idTratamiento){
			//Limpiamos el combo tratamiento para que no se sumen los resultados
			jQuery("#tratamiento").html("");
	
			jQuery.ajax({  
	           type: "POST",
	           url: "/SIGA/CEN_Censo.do?modo=getTratamientoAPartirDelSexo",
	           data: "idSexo="+idSexo,
	           dataType: "json",
	           success:  function(json) {
	        	   jQuery("#tratamiento").append('<option value="">'+"--Seleccionar"+'</option>');
	           
	        		jQuery.each(json, function(index, value) {
	        			jQuery("#tratamiento").append('<option value='+value.id+'>'+value.descripcion+'</option>');
	        		   });
	        		
	        		// Realizado para que se marque por defecto un valor cada vez que se cambie de sexo
	        		//Si tratamiento trae valor.
	        		
	        		if(idTratamiento != null && idTratamiento !=""){
	        			jQuery('#tratamiento > option[value='+idTratamiento+']').attr('selected', 'selected');
		        		jQuery('#textTratamiento').val(jQuery("#tratamiento option:selected").text());
	        		}else{//Si no trae valor por defecto si es mujer tendr� que salir seleccionado Sra, si es hombre Sr.
	        			
	        			var id_sr = "<%=ClsConstants.ID_SR%>";
	        			var id_sra = "<%=ClsConstants.ID_SRA%>";
	        			var id_sr_sra = "<%=ClsConstants.ID_SR_SRA%>";
	        			
	        			if(idSexo == "<%=ClsConstants.GENERO_HOMBRE%>"){
	        				jQuery('#tratamiento > option[value='+id_sr+']').attr('selected', 'selected');
			        		jQuery('#textTratamiento').val(jQuery("#tratamiento option:selected").text());
	        			}else{
	        				if(idSexo == "<%=ClsConstants.GENERO_MUJER%>"){
	        					jQuery('#tratamiento > option[value='+id_sra+']').attr('selected', 'selected');
				        		jQuery('#textTratamiento').val(jQuery("#tratamiento option:selected").text());
	        				}else{//Neutro
	        					jQuery('#tratamiento > option[value='+id_sr_sra+']').attr('selected', 'selected');
				        		jQuery('#textTratamiento').val(jQuery("#tratamiento option:selected").text());
	        				}
	        			}
	        		}
	     		
	           },
	           error: function(xml,msg){
	        	   alert("Error: "+msg);
	           }
	        }); 
		}
	</script>

	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		<siga:Titulo titulo="censo.fichaCliente.datosGenerales.cabecera" localizacion="censo.fichaLetrado.localizacion"/>
	<% } else { %>
		<siga:TituloExt titulo="censo.fichaCliente.datosGenerales.cabecera" localizacion="censo.fichaCliente.datosGenerales.localizacion"/>
	<% } %>	
	<!-- FIN: TITULO Y LOCALIZACION --> 
</head>


<%	if (!formulario.getAccion().equals("nuevo") && (resultado==null || resultado.size()==0)) { %>			

	<body class="tablaCentralCampos">
			<table class="tdMensaje">
			<tr><td>
				<br><br>
				<siga:Idioma key="messages.noRecordFound"/>
			</td></tr> 
			</table>

		<% if (!busquedaVolver.equals("volverNo")) { %>
				<siga:ConjBotonesAccion botones="V"  clase="botonesDetalle"  />
		<% } %>
	
<% } else { 
	
	if (!formulario.getAccion().equalsIgnoreCase("NUEVO")) {

			// cuando NO es nuevo
			
			// SOLO VA A TENER UN REGISTRO 
			Hashtable registro = (Hashtable) resultado.get(0);

			// calculo de campos
			idInstitucion=formulario.getIdInstitucion();
			if (idInstitucion==null) idInstitucion=""; 
			idPersona = (String) registro.get(CenPersonaBean.C_IDPERSONA);
			if (idPersona==null) idPersona=""; 
			// fotografia con path virtual
			fotografia = (String) registro.get(CenClienteBean.C_FOTOGRAFIA);
			if (fotografia!=null && !fotografia.equals("")) {
				//fotografia = "/"+ClsConstants.PATH_DOMAIN+"/"+ClsConstants.RELATIVE_PATH_FOTOS+"/"+idInstitucion+"/"+fotografia;
				fotografia = File.separatorChar+ClsConstants.PATH_DOMAIN+
								  File.separatorChar+ClsConstants.RELATIVE_PATH_FOTOS+
								  File.separatorChar+idInstitucion+
								  File.separatorChar+fotografia;
			}
			nombre = (String) registro.get(CenPersonaBean.C_NOMBRE);
			if (nombre==null) nombre=""; 
			apellido1 = (String) registro.get(CenPersonaBean.C_APELLIDOS1);
			if (apellido1==null) apellido1=""; 
			apellido2 = (String) registro.get(CenPersonaBean.C_APELLIDOS2);
			if (apellido2==null) apellido2=""; 
			apellidos = apellido1 + apellido2;
			numero = "";
			nIdentificacion = (String) registro.get(CenPersonaBean.C_NIFCIF);
			if (nIdentificacion==null) nIdentificacion=""; 
			fechaNacimiento = registro.get(CenPersonaBean.C_FECHANACIMIENTO)==null?"":GstDate.getFormatedDateShort(user.getLanguage(),(String)registro.get(CenPersonaBean.C_FECHANACIMIENTO));	
			if (fechaNacimiento==null) fechaNacimiento=""; 
			fechaAlta = registro.get(CenClienteBean.C_FECHAALTA)==null?"":GstDate.getFormatedDateShort(user.getLanguage(),(String)registro.get(CenClienteBean.C_FECHAALTA));	
			if (fechaAlta==null) fechaAlta=""; 
			nacido = (String) registro.get(CenPersonaBean.C_NATURALDE);
			if (nacido==null) nacido=""; 
			estad = (String) registro.get(CenPersonaBean.C_NIFCIF);
			if (estad==null) estad=""; 
			publicidad = (String) registro.get(CenClienteBean.C_PUBLICIDAD);
			if (publicidad==null) publicidad=""; 
			guiaJudicial = (String) registro.get(CenClienteBean.C_GUIAJUDICIAL);
			if (guiaJudicial==null) guiaJudicial=""; 
			abono = (String) registro.get(CenClienteBean.C_ABONOSBANCO);
			if (abono==null) abono=""; 
			cargo = (String) registro.get(CenClienteBean.C_CARGOSBANCO);
			if (cargo==null) cargo=""; 
			comisiones = (String) registro.get(CenClienteBean.C_COMISIONES);
			if (comisiones==null) comisiones=""; 
			noRevista = (String) registro.get(CenClienteBean.C_NOENVIARREVISTA);
			if (noRevista==null) noRevista=""; 
			noRedAbogacia = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
			if (noRedAbogacia==null) noRedAbogacia="";
			usoFoto = (String) registro.get(CenClienteBean.C_EXPORTARFOTO);
			if (usoFoto==null) usoFoto="";
		
			descripcion = (Boolean) request.getAttribute("descripcion");

			cuentaContable = (String) registro.get(CenClienteBean.C_ASIENTOCONTABLE);
			if (cuentaContable==null) cuentaContable=""; 
			sexo = (String) registro.get(CenPersonaBean.C_SEXO);
			if (sexo==null) sexo=""; 
			//Si es vacio es que estamos hablando de que el sexo es neutro
			if ("".equalsIgnoreCase(sexo)) sexo=ClsConstants.GENERO_NEUTRO;
			idioma = (String) registro.get(CenClienteBean.C_IDLENGUAJE);
			if (idioma==null) idioma=""; 
			estadoCivil = (String) registro.get(CenPersonaBean.C_IDESTADOCIVIL);
			if (estadoCivil==null) estadoCivil=""; 
			caracter = (String) registro.get(CenClienteBean.C_CARACTER);
			if (caracter==null) caracter=""; 
			tipoi = (String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION);
			if (tipoi==null) tipoi=""; 
			edad = (String) registro.get("EDAD");
			if (edad==null) edad=""; 
	
			// combos
			idTratamiento = (String) registro.get(CenClienteBean.C_IDTRATAMIENTO);
			String idtipoidentificacion = (String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION);
			tipoIdentificacionSel.add(idtipoidentificacion);
			paramsTipoIdenJSON = "{\"idtipoidentificacion\":\""+idtipoidentificacion+"\"}";
			estadoCivilSel.add((String) registro.get(CenPersonaBean.C_IDESTADOCIVIL));
			idiomaSel.add((String) registro.get(CenClienteBean.C_IDLENGUAJE));
			caracterSel.add((String) registro.get(CenClienteBean.C_CARACTER));
			
			fallecido=(String)registro.get(CenPersonaBean.C_FALLECIDO);
			
		} else {
			String idtipoidentificacion = String.valueOf(ClsConstants.TIPO_IDENTIFICACION_NIF);
			tipoIdentificacionSel.add(idtipoidentificacion);
			paramsTipoIdenJSON = "{\"idtipoidentificacion\":\""+idtipoidentificacion+"\"}";
			descripcion = new Boolean(false);
		}
	
	boolean camposReadonly = true;
	if (!breadonly && bDatosGeneralesEditables)
		camposReadonly = false;
	%>

	<body class="tablaCentralCampos" onload="adaptaTamanoFoto();buscarGrupos();mostrarMensaje();obtenerTratamientos ('<%=sexo%>','<%=idTratamiento%>');comprobarDuplicados('<%=idInstitucionAcceso%>', '<%=idPersona%>', '<%=nIdentificacion%>', '<%=nombre.replaceAll("'","@")%>', '<%=apellido1.replaceAll("'","@")%>', '<%=apellido2.replaceAll("'","@")%>', '<%=idInstitucion%>', '<%=numero%>');">


	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
	<td class="titulitosDatos" >
	<%	if (!formulario.getAccion().equals("nuevo")) { %>
				<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1"/>
				 &nbsp; 		
				 <%=UtilidadesString.mostrarDatoJSP(nombre) + " " + UtilidadesString.mostrarDatoJSP(apellido1) + " " + UtilidadesString.mostrarDatoJSP(apellido2) %>
				 &nbsp; 		
				<% if (bColegiado) { %>
					 <%if (estadoColegial!=null && !estadoColegial.equals("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
						 <%= UtilidadesString.mostrarDatoJSP(numeroColegiado)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
					 <%}else{%> 
					 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
					 <%}%>
				<% } else { %>
					<% if (sTipo.equalsIgnoreCase("LETRADO")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.letrado"/>
					<% } else { %>
							<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% }  %>
				<% }  %>
	<% } else { %>
				<siga:Idioma key="censo.altaDatosGenerales.cabecera"/>
	<% }  %>
	</td>
	
	</tr>
	<% 
	boolean isAplicarLOPD = noRedAbogacia != null && !noRedAbogacia.equals("")&& noRedAbogacia.equals(ClsConstants.DB_TRUE);
	if(isAplicarLOPD){
	%>
	<tr align="center" valign="middle">
	
	<td class="titulitosDatos">
	
	<img src="<%=app%>/html/imagenes/blopd_disable.gif" align="middle" border="0" >
	<siga:Idioma key="general.boton.lopd"/>
	<img src="<%=app%>/html/imagenes/blopd_disable.gif" align="middle" border="0" >
	</td>
	</tr>
	<% }%>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

			

	<!------------------------------->
	<!-- Tabla central principal -->
	<!------------------------------->
	<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
		<html:form  action="/CEN_DatosGenerales.do" method="POST" target="mainPestanas"  enctype="multipart/form-data">
			<html:hidden  name="datosGeneralesForm" property="modo"/>
			<html:hidden  name="datosGeneralesForm" property="idInstitucion"/>
			<html:hidden  name="datosGeneralesForm" property="idPersona"/>
			<html:hidden  name="datosGeneralesForm" property="id"/>
			<html:hidden  name="datosGeneralesForm" property="accion"/>
			<html:hidden  name="datosGeneralesForm" property="idTratamiento" value="1"/>
			<html:hidden name="datosGeneralesForm" property="motivo"/>
			<html:hidden name="datosGeneralesForm" property="abono" value="B" />
			<html:hidden name="datosGeneralesForm" property="cargo" value="B" />
			<html:hidden property="actionModal" value=""/>
			<html:hidden property="tipo" value="<%=sTipo%>"/>
			<html:hidden name="datosGeneralesForm" property = "continuarAprobacion" value = ""/>
			<input type="hidden" name="bDatosGeneralesEditables"  id="bDatosGeneralesEditables"  value="<%=bDatosGeneralesEditables%>"/>
			
	<tr>
		<!-- FILA 1: FOTO -->
		<td rowspan="3" valign="top" style="width:200px">
			<siga:ConjCampos leyenda="censo.consultaDatosGenerales.literal.foto">
				<br>
				<% 
					if (fotografia == null || fotografia.equals("")) { 
						fotografia = app + "/html/imagenes/usuarioDesconocido.gif";
					}
				%>

					<div style='height:245px; width:184px; overflow-x:auto; overflow-y:auto'>
						<center>
							<img id="fotoNueva" border="0" src="<%=fotografia%>" >	
						</center>
					</div>
						<br>&nbsp;		

				<% if (!breadonly) { %>
						<html:file name="datosGeneralesForm" property="foto" style="width:200px" styleClass="<%=estiloCaja %>" accept="image/gif,image/jpg" ></html:file>		
						<br>&nbsp;		
				<% } %>
			</siga:ConjCampos>
		</td>

		<!-- FILA 1: Datos Generales -->
		<td>
				<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
				<table class="tablaCampos"  align="center" cellpadding="0" cellpadding="0">
				<tr>
					<!-- CLIENTE -->
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.situacion"/>&nbsp;
					</td>				
					<td  style="width:200px">
						<html:text name="datosGeneralesForm" property="cliente" size="40" styleClass="boxConsulta" readonly="true" value="<%=cliente %>" ></html:text>
					</td>
	                <!-- FALLECIMIENTO -->
					<td class="labelText" style="width:170px">
					<% 
             		  if (fallecido!=null && !fallecido.equals("") && !fallecido.equals("0")){%>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.fallecido"/>&nbsp;
					<%}else{%>
					  &nbsp;
					<%}%>
					</td>		
					<!-- FECHA ALTA -->
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaAlta"/>
					
						
					</td>				
					<td>
						<html:text name="datosGeneralesForm" property="fechaAlta" styleClass="boxConsulta" readonly="true" value="<%=fechaAlta %>" style="width:100px" >
						</html:text>

					</td>
				</tr>
				
				<tr>
					<!-- TIPO IDENTIFICACION -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.tipoIdentificacion"/>&nbsp;(*)
					</td>				
					<td colspan="4">
					<% if (!breadonly && bDatosGeneralesEditables) { %>
					
						<% if(tipoCliente.equals(ClsConstants.TIPO_CLIENTE_NOCOLEGIADO) && !idInstitucion.equals("2000")){%>
							<siga:Select id="tipoIdentificacion" 
										queryId="getTiposIdentificacionSinCIF"
										selectedIds="<%=tipoIdentificacionSel%>"
										required="true"/>
										
						<% } else { %>
								<siga:Select id="tipoIdentificacion" 
										queryId="getTiposIdentificacionSinCIFNiOtros"
										selectedIds="<%=tipoIdentificacionSel%>"
										params="<%=paramsTipoIdenJSON%>" 
										required="true"/>		
										
						<% } %>
						
					<% } else { %>
							<siga:Select id="tipoIdentificacion" 
										queryId="getTiposIdentificacion"
										selectedIds="<%=tipoIdentificacionSel%>"
										required="true"
										readOnly="true"/>
					<% } %>
					&nbsp;
					<% if (!breadonly && bDatosGeneralesEditables) { %>
				      <html:text name="datosGeneralesForm" property="numIdentificacion" size="20"  maxlength="20" styleClass="box" value="<%=nIdentificacion %>" onfocus="generaNumOtro();" onBlur="formatearDocumento();"/>&nbsp;
					<% } else { %>
					  <html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onBlur="formatearDocumento();"/>
					<% } %>
					</td>
					<td align="right">
						<img id="iconoboton_cargando_1"	src="/SIGA/html/imagenes/bloading_on_23.gif"	style="cursor: hand; display: none" alt="Cargando posibles duplicados"> 
						<img id="iconoboton_aviso_1"	src="/SIGA/html/imagenes/warning.png"			style="cursor: hand; display: none" alt="Duplicidades" onClick="accionObtenerDuplicados();"> 
					</td>
				</tr>
				</table>
				</siga:ConjCampos>
		</td>
	</tr>
   
	<!-- FILA 2: Informacion Personal -->
	<tr>
		<td>
				<siga:ConjCampos leyenda="censo.general.literal.informacionOrganizacion">
				<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0">
				<tr>
					<!-- SEXO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>
					</td>				
					<td>
					<% 
						String ssexo = "";
						if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
						if (sexo.equals(ClsConstants.TIPO_SEXO_MUJER)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer");
					%>
					<% if (!breadonly && bDatosGeneralesEditables) { %>
						<!-- option select -->
						<html:select name="datosGeneralesForm" property="sexo" style = "null" styleClass = "<%=estiloCaja %>" value="<%=sexo %>" onchange="obtenerTratamientos(this.value,'')">
						    <html:option value="0" >&nbsp;</html:option>
							<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
							<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
						</html:select>						
					<% } else { %>
						<html:hidden  name="datosGeneralesForm" property="sexo" value="<%=sexo %>"/>
						<html:text name="datosGeneralesForm" property="ssexo" size="20" styleClass="boxConsulta" value="<%=ssexo %>" readonly="true" ></html:text>
					<% } %>
					</td>
					<!-- TRATAMIENTO -->
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/>&nbsp;(*)
					</td>
					<td  style="width:200px">
						<% if (("false").equalsIgnoreCase(readonly)) { %>
							<html:select styleId="tratamiento" styleClass="boxCombo" style="width:190px;" property="tratamiento" >		
							</html:select>
						<% } else { %>
							<html:select styleId="tratamiento" styleClass="boxCombo" style="width:190px; display:none" property="tratamiento" >		
							</html:select>
						    <html:text name="datosGeneralesForm" property="tratamiento" styleId="textTratamiento" size="20" styleClass="boxConsulta" value="" readonly="true" ></html:text>
						<% } %>
					</td>
				</tr>
				<tr>
					<!-- NOMBRE -->
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp;(*)
					</td>
					
					<td>
					<% if (!breadonly && bDatosGeneralesEditables) { %>
						<html:text name="datosGeneralesForm" property="nombre" size="20" maxlength="100" styleClass="box" style='width:190px;' value="<%=nombre %>"  ></html:text>
					<% } else { %>
						<html:text name="datosGeneralesForm" property="nombre" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=nombre %>" readonly="true" ></html:text>
					<% } %>
					</td>
				<tr>
					<!-- APELLIDO1 -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.apellido1"/>&nbsp;(*)
					</td>				
					<td>
					<% if (!breadonly && bDatosGeneralesEditables) { %>
						<html:text name="datosGeneralesForm" property="apellido1" size="20" maxlength="100" styleClass="box" style='width:190px;' value="<%=apellido1 %>" ></html:text>
					<% } else { %>
						<html:text name="datosGeneralesForm" property="apellido1" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=apellido1 %>" readonly="true" ></html:text>
					<% } %>
					</td> 
					
					<!-- APELLIDO2 -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.apellido2"/>
					</td>				
					<td>
					<% if (!breadonly && bDatosGeneralesEditables) { %>
				    	<html:text name="datosGeneralesForm" property="apellido2" size="20" maxlength="100" styleClass="box" style='width:190px;' value="<%=apellido2 %>" ></html:text>
					<% } else { %>
						<html:text name="datosGeneralesForm" property="apellido2" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=apellido2 %>" readonly="true" ></html:text>
					<% } %>
					</td>
					
				
				</tr>
				<tr>
					<!-- FECHA DE NACIMIENTO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaNacimiento"/>&nbsp;
					</td>				
					<td>
<% 
						if (!breadonly && bDatosGeneralesEditables) { 
%>
							<siga:Fecha id="fechaNacimiento" nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>" postFunction="comprobarFechaNacimiento(this.value)"/>
<% 
						} else { 
%>
							<siga:Fecha id="fechaNacimiento" nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>" disabled="true"/>
<% 
						} 
%>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.edad"/>
					</td>				
					<td class="boxConsulta">
						<%=edad%>
					</td>
				
				</tr>
				<tr>
					<!-- LUGAR NACIMIENTO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nacido"/>
					</td>				
					<td>
					<% if (!breadonly && bDatosGeneralesEditables) { %>
						<html:text name="datosGeneralesForm" property="lugarNacimiento" size="20" maxlength="100" styleClass="box" style='width:190px;' value="<%=nacido %>"></html:text>
					<% } else { %>
						<html:text name="datosGeneralesForm" property="lugarNacimiento" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=nacido %>" readonly="true" ></html:text>
					<% } %>
					</td>
					<!-- ESTADO CIVIL -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.estadoCivil"/>
					</td>				
					<td>
						<siga:Select id="estadoCivil"
									queryId="getEstadosCiviles"
									selectedIds="<%=estadoCivilSel%>"
									readOnly="<%=String.valueOf(camposReadonly)%>"/>					
					</td>
				
				
				</tr>
				<tr>
					<% if (!bDatosGeneralesEditables && !esLetrado && !institucionParam[0].equals("2000")) { %> 
					<td class="labelText">
						<img src="<%=app%>/html/imagenes/help.gif" 
							alt="<siga:Idioma key='censo.consultaDatosGenerales.mensaje.noEditable'/>" 
							onclick="alertaNoEditable();"
							style="cursor: hand;"/> 
					</td>
					<td class="labelText">
						<input type="button" class="button" id="mensaje" name="mensaje"  value='Comunicar CGAE' onClick="solicitarModificacionDatos();">
					</td>
					<% } else { %>
					<td class="labelText" colspan="2">
					</td>
					<% } %>
				</tr>
				</table>
				</siga:ConjCampos>
		</td>
	</tr>

	<!-- FILA 3: Informacion Adicional -->
	<tr>
		<td>
			<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
			<tr>
			<td>

				<siga:ConjCampos leyenda="censo.general.literal.informacionAdicional">
				<table class="tablaCampos" align="left" cellpadding="0" cellpadding="0" style="width:450px">
				<tr>
					<!-- IDIOMA -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.idioma"/>&nbsp;(*)
					</td>
					<td>
						<siga:Select id="idioma"
									queryId="getIdiomasInstitucion"
									required="true"
									selectedIds="<%=idiomaSel%>"
									width="100"
									readOnly="<%=readonly%>"/>
					</td>
					<td class="labelText">
					<!-- CUENTA CONTABLE -->
					<% if (!user.isLetrado()) { %>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.cuentaContable"/>
					</td>
					<td>
						<html:text name="datosGeneralesForm" property="cuentaContable" size="20" maxlength="20" styleClass="<%=estiloCaja %>" readonly="<%=breadonly %>" value="<%=cuentaContable %>"></html:text>
					<% } else { %>
						<html:hidden name="datosGeneralesForm" property="cuentaContable" value="<%=cuentaContable %>"></html:hidden>
						&nbsp;
					<% }  %>
					</td>
				</tr>
				
				<tr>
				<td colspan="4">
				<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" > <!-- para alinear -->
				
				<tr>
				
					<td class="labelText" colspan="2">
					<!-- GUIA JUDICIAL -->
					<% if (guiaJudicial.equals(ClsConstants.DB_TRUE)) { %>
						<input type="checkbox" name="guiaJudicial"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="guiaJudicial"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.guiaJudicial"/>
					</td>
					
					<!-- PUBLICIDAD -->
					<td class="labelText" colspan="2">
					<%  if (publicidad.equals(ClsConstants.DB_TRUE)) { 	%>
						<input type="checkbox" name="publicidad"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="publicidad"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.publicidad"/>
					</td>
									
					<!-- COMISIONES -->
					<td class="labelText" colspan="2">
					<% if (comisiones.equals(ClsConstants.DB_TRUE)) { %>
						<input type="checkbox" name="comisiones"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="comisiones"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.comisiones"/>
					</td>
	
				</tr>
				
<% if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_LETRADO)) { %>					
				<tr>
					<td class="labelText" style="height:20px"  colspan="6">	
					<% if (noRevista.equals(ClsConstants.DB_TRUE)) { %>
						<input type="checkbox" name="noRevista"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="noRevista"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="messages.letrados.noRevistaCGAE"/>
					</td>
	
				</tr>
				<tr>
					<td class="labelText" style="height:20px"  colspan="6">
					
					<%  if (noRedAbogacia.equals(ClsConstants.DB_TRUE)) { 	%>
						<input type="checkbox" name="noRedAbogacia"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="noRedAbogacia"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="messages.letrados.noApareceRedAbogacia"/>
				</td>
				</tr>

				
<% } else { %>		
				<tr>
					<td class="labelText" style="height:30px"  colspan="4">			
						<input type="hidden" name="noRevista"  value="<%=(noRevista.equals(ClsConstants.DB_TRUE))?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE %>"  />
					<%  if (noRedAbogacia.equals(ClsConstants.DB_TRUE)) { 	%>
						<input type="checkbox" name="noRedAbogacia"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> checked />
					<% } else { %>
						<input type="checkbox" name="noRedAbogacia"  value="<%=ClsConstants.DB_TRUE %>"  <%=checkreadonly %> />
					<% } %>
						<siga:Idioma key="messages.letrados.lopd"/>
					</td>
					<td class="labelText" colspan="2">
						<input type="hidden" name="noRevista"  value="<%=(noRevista.equals(ClsConstants.DB_TRUE))?ClsConstants.DB_TRUE:ClsConstants.DB_FALSE %>"  />
					<% if (! user.isLetrado()) {
						 if (descripcion.booleanValue()){
					%>
						<input type="checkbox" name="descripcion"  value="<%=ClsConstants.DB_TRUE %>" disabled checked />
					<%   } else { %>
						<input type="checkbox" name="descripcion"  value="<%=ClsConstants.DB_TRUE %>" disabled  />
					<%   } %>
						<siga:Idioma key="censo.consultaDatosGenerales.literal.ver.observaciones"/>
					<% } %>
					</td>	
				</tr>
				
				<tr>
					<td class="labelText" style="height:20px"  colspan="6">
						<%  if (usoFoto.equals(ClsConstants.DB_TRUE)) { 	%>
							<input type="checkbox" name="exportarFoto"  value="<%=ClsConstants.DB_TRUE %>" <%=checkreadonlyFoto %> checked onClick="habilitarBoton()"/>
						<% } else { %>
							<input type="checkbox" name="exportarFoto"  value="<%=ClsConstants.DB_TRUE %>" <%=checkreadonlyFoto %> onClick="habilitarBoton()"/>
						<% } %>
						
						<siga:Idioma key="censo.consultaDatosGenerales.literal.usarFoto"/>
					</td>
					
					<% if (user.isLetrado()) { %>
						<td>
							<input type="button" class="button" id="modificar" name="modificar"  value='Modificar' onClick="solicitarModificacion();" disabled >
						</td>
					<% } %>					
				</tr>				
				
											
							
				
<% } %>					
					</td>
				</table> <!-- para alinear -->
				</tr>	
	
				
				</table>
				</siga:ConjCampos>
			</td>
			<td style="width:300px">
						<!-- GRUPO CLIENTE -->
						<!-- INICIO: IFRAME LISTA RESULTADOS -->
						<siga:ConjCampos>
						<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
										id="resultado"
										name="resultado" 
										scrolling="no"
										frameborder="0"
										marginheight="0"
										marginwidth="0"					 
										style="width:100%; height:100%;">
						</iframe>
						<!-- FIN: IFRAME LISTA RESULTADOS -->
						</siga:ConjCampos>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</html:form>
	</table>

	
	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="resultado" styleId="GruposClienteClienteForm">
		<html:hidden name="GruposClienteClienteForm" property="modo" value="buscar"/>
		<html:hidden name="GruposClienteClienteForm" property="idPersona" />
		<html:hidden name="GruposClienteClienteForm" property="idInstitucion" />
		<html:hidden name="GruposClienteClienteForm" property="modoAnterior" />		
	</html:form>
	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G ,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
 
<% 
	// BOTONES
	String botonesAccion = "";
	if ((busquedaVolver==null)||(!user.isLetrado())) {
		botonesAccion+="V,";
	}
	
	if (new Long(user.getIdPersona()).toString().equals(idPersona) && user.isLetrado()) { 
		botonesAccion+="SM,";
	}
	if (modo.equals("nuevo") || modo.equals("editar")) { 
		botonesAccion+="G,R,";
	}
	if (!user.isLetrado() && !modo.equals("nuevo")) { 
	  
	
	  if (!bColegiado && !sTipo.trim().toUpperCase().equalsIgnoreCase("LETRADO")) { //Para los no colegiados hacemos que aparezca el boton colegiar
	   botonesAccion+="BA,";
	  }
	}
	
	// le quito la coma final
	if (botonesAccion.length()>0) {
		botonesAccion=botonesAccion.substring(0,botonesAccion.length()-1);
	}
	
%>

	<siga:ConjBotonesAccion botones="<%=botonesAccion %>" modo="<%=modo%>" idBoton="3"  idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Muestra alerta de no editables los datos generales
		function alertaNoEditable() {
			alert('<siga:Idioma key="censo.consultaDatosGenerales.mensaje.noEditable"/>', 'info');
		}
		
		// Asociada al boton SolicitarModificacion
		function accionSolicitarModificacion() 	{
			document.forms[0].modo.value="abrirSolicitud";
			ventaModalGeneral(document.forms[0].name,'P');
		}

		// Asociada al boton Restablecer
		function accionRestablecer() {
			document.forms[0].reset();
			//Debemos de restaurar tambi�n el valor del campo creado autom�ticamente
			obtenerTratamientos ('<%=sexo%>','<%=idTratamiento%>');
		}

		function habilitarBoton() {
			if(document.getElementById("modificar") != null){
				if(document.getElementById("modificar").disabled){
					jQuery("#modificar").removeAttr("disabled");
				}else{
				   	jQuery("#modificar").attr("disabled","disabled");
				}
			}
		}

		function solicitarModificacion() {
		    var type = '�Confirma la solicitud de modificaci�n?';
			if (confirm(type)) {
				if(document.forms[0].exportarFoto.checked){
					document.forms[0].exportarFoto.value = "1";
				}else{
					document.forms[0].exportarFoto.value ="0";
				}		

				document.forms[0].target="submitArea";
				document.forms[0].modo.value="solicitarModificacion";
				document.forms[0].submit();	
			}			
		}
		
		function solicitarModificacionDatos() {
			document.SolicitudesModificacionForm.target="submitArea";
			ventaModalGeneral(document.SolicitudesModificacionForm.name,'G');
			document.SolicitudesModificacionForm.submit();	
		}
		
		function validarFormulario() {
			if (validateDatosGeneralesForm(document.forms[0])) {
				var rc = true;
				
				<% if (bDatosGeneralesEditables) { %>
					var tipoIden = document.datosGeneralesForm.tipoIdentificacion.value;
					
					if (tipoIden == <%=tipoIdenNIF%>) {
						rc = validarNIFCIF(tipoIden, document.datosGeneralesForm.numIdentificacion.value);
						if (rc == false) {
							alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
						}
						
					} else if (tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") {
						rc = validaNIE(document.datosGeneralesForm.numIdentificacion.value);
						
					} else if (tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_PASAPORTE%>" && document.forms[0].numIdentificacion.value=="") {
						alert('<siga:Idioma key="messages.pasaporte.comprobacion.error"/>');
						rc = false;
						
					} else if (tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>" && document.forms[0].numIdentificacion.value=="") {	
						alert('<siga:Idioma key="messages.otro.comprobacion.error"/>');
						rc = false;
						
					} else if (document.forms[0].tipoIdentificacion.value== "" && document.forms[0].numIdentificacion.value=="") {
						if (document.forms[0].cliente.valyue== "Letrado" || document.forms[0].cliente.value=="Ejerciente") {
							alert('<siga:Idioma key="messages.tipoIdenNumIden.comprobacion.error"/>');
							rc = false;
						}
					}
				
				<% } %>
				
				if (rc) {					
					if (!comprobarFechaNacimiento(jQuery("#fechaNacimiento").val())) {
						rc = false;
					}
				}
				
				return rc;
				
			} else {			
			  	return false;
			}
		}
	
		// Asociada al boton Guardar
		function accionGuardar() {		
			sub();
			
			//Se eliminan los espacios en el num. identificacion
			document.forms[0].numIdentificacion.value = fTrim(document.forms[0].numIdentificacion.value);			
			
			<%if (cliente.equals("") || cliente.equals("No Colegiado")){%>
				if((document.forms[0].tipoIdentificacion.value== "") && (document.forms[0].numIdentificacion.value==""))
					alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.aviso"/>");
			<%}%>

				if (validarFormulario()	&&	TestFileType(document.forms[0].foto.value, ['GIF', 'JPG', 'PNG', 'JPEG'])) {
				<%	if (!formulario.getAccion().equals("nuevo")) { %>
					var datos;
					<% if (!bOcultarHistorico) { %>
							datos = showModalDialog("<%=app%>/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
							window.top.focus();
						<% } else { %>
								datos = new Array();
								datos[0] = 1;
								datos[1] = "";
						<% } %>
					
						if (datos[0] == 1) { // Boton Guardar
							document.forms[0].motivo.value = datos[1];
							document.forms[0].target="submitArea";
							document.forms[0].modo.value="modificar";
							document.forms[0].submit();	
						}else{
							fin();
							return false;
						
						}
						
				<%	} else { %>
				
						document.forms[0].target="submitArea";
						document.forms[0].modo.value="insertar";
						document.forms[0].submit();	
				<%	}  %>
				}else{
					fin();
				}
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<%	} // else de "no hay datos" %>

<html:form action="/CEN_ModificacionDatos.do" method="POST" target="mainWorkArea"  style="display:none">
		<input type="hidden" name="modo" value="solicitarModificacionDatos">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idPersona" value = "<%=idPersonaSolicitud%>"/>
</html:form>


<%@ include file="/html/jsp/censo/includeMantenimientoDuplicados.jspf"%>
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>