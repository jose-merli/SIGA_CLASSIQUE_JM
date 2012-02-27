<!-- consultaDatosGenerales.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.siga.beans.CenInstitucionBean"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	boolean bOcultarHistorico = user.getOcultarHistorico();

	DatosGeneralesForm formulario = (DatosGeneralesForm)request.getAttribute("datosGeneralesForm");
	String modo = formulario.getAccion();

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}

	String mostrarMensaje = (String) request.getSession().getAttribute("MOSTRARMENSAJE");
	String mostrarMensajeNifCif = (String) request.getSession().getAttribute("MOSTRARMENSAJECIFNIF");

	// variables
	ArrayList gruposSel = new ArrayList();	
	String cliente = "";
	String numeroColegiado = "";
	boolean bColegiado = false;
	boolean bfCertificado=true;
	String fechaCertificado = "";
	Vector resultado = null;
    numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
		if (numeroColegiado!=null) {
			bColegiado=true;
		} 
	
	// RGG 24-06-2005 cambio para controlar acceso a datos persona otra institucion que la creadora
	String consultaPersona = (String) request.getAttribute("CenDatosPersonalesOtraInstitucion");
	boolean bConsultaPersona = false;
	
	
	if (!formulario.getIdPersona().equals("")) {
		// modo != nuevo	

		// atributos
		resultado = (Vector) request.getAttribute("CenResultadoDatosGenerales");
	
		String[] arrayGruposSel = formulario.getGrupos();
		if (arrayGruposSel!=null) {
			for (int j=0;j<arrayGruposSel.length;j++) {
				gruposSel.add(arrayGruposSel[j]);
			}
		}
	
		String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
        if (colegiado!=null && !colegiado.equals("")){
		   cliente = UtilidadesString.getMensajeIdioma(user, colegiado);
        }else{
        	cliente = UtilidadesString.getMensajeIdioma(user,"censo.busquedaClientes.literal.sinEstadoColegial");
        }
		
		fechaCertificado = (String) request.getAttribute("CenDatosGeneralesCertificado");
		if (fechaCertificado==null || fechaCertificado.equals("")) {
			fechaCertificado=UtilidadesString.getMensajeIdioma(user, "censo.ConsultaDatosGenerales.mensaje.NoCertificado");
			bfCertificado=false;
		}

	} // del if de nuevo
	
	
	//  tratamiento de readonly
	String estiloCaja = "";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	// caso de accion
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
	
	
	// seleccion de combos
	ArrayList tratamientoSel = new ArrayList();
	ArrayList tipoIdentificacionSel = new ArrayList();
	ArrayList estadoCivilSel = new ArrayList();
	ArrayList idiomaSel = new ArrayList();
	ArrayList caracterSel = new ArrayList();

	String idInstitucion = "";
	String idPersona = "";
	String fotografia = "";
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
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
	estadoColegial = (String) request.getAttribute("ESTADOCOLEGIAL");
	int tipoIdenNIF = ClsConstants.TIPO_IDENTIFICACION_NIF;
    int tipoIdenCIF = ClsConstants.TIPO_IDENTIFICACION_CIF;

	//Toma los de la institucion:
	String [] institucionParam = new String[1];
	if (formulario.getIdInstitucion()!= null && !formulario.getIdInstitucion().equals(""))
   		institucionParam[0] = formulario.getIdInstitucion();
   	else
	   	institucionParam[0] = user.getLocation();
  	
	boolean breadonlyNif = false;
	String estiloCajaNif = "box";
	boolean breadonlyNombreApellidos = false;
	boolean pintaCalendario=true;
	boolean pintaComboSexo=true;
	
	String estiloCajaNombreApellidos = "box";
	String readonlyComboNIFCIF = "false";
	
	idPersona=formulario.getIdPersona();
    String sTipo = (String) request.getAttribute("TIPO");
	
	if (sTipo == null)
		sTipo = "";
		
	if (formulario.getAccion().equals("ver")) {
		breadonlyNif = true;
		estiloCajaNif = "boxConsulta";
		breadonlyNombreApellidos = true;
		estiloCajaNombreApellidos = "boxConsulta";
		readonlyComboNIFCIF = "true";
	} else {//solo miramos si la persona ha sido dada de alta en el colegio por el mismo colegio o por otro cuando es NO COLEGIADO
		if (idPersona.length()>4) {
			if ((!idPersona.substring(0,4).equals(user.getLocation())) && (!bColegiado) && !(sTipo.equals("LETRADO")) ){
			 
				breadonlyNif = true;
				estiloCajaNif = "boxConsulta";
				breadonlyNombreApellidos = true;
				estiloCajaNombreApellidos = "boxConsulta";
				readonlyComboNIFCIF = "true";
				pintaCalendario=false;
				pintaComboSexo=false;
			}   
		} 
	}
	
	boolean bResidente = false;
	String residente = (String) request.getAttribute("RESIDENTE");
	if (bColegiado){//para los COLEGIADOS tiene sentido mirar el check de residente para habilitar y deshabilitar los campos nif, nombre y apellidos
    	if (residente != null && residente.equals(ClsConstants.DB_TRUE))
	    	bResidente = true;
    }else{
	   bResidente = true;
	  //bResidente = true;
	  // Solo se podra modificar el tipo de identificacion si el colegiado es la propia institucion y nos encontramos en dicha institucion o si el  colegiado
		// ha sido dado de alta por la institucion donde nos encontramos.
		
		            CenInstitucionAdm insAdm= new CenInstitucionAdm(user);
					Hashtable hashIns= new Hashtable();
					hashIns.put(CenInstitucionBean.C_IDINSTITUCION,user.getLocation());
					Vector v2 = insAdm.selectByPK(hashIns);
					String idPersonaInstitucionUserBean="";
					if (v2!=null && v2.size()>0) {
						CenInstitucionBean insBean = (CenInstitucionBean) v2.get(0);
						idPersonaInstitucionUserBean=insBean.getIdPersona().toString();
					}	
					
					if (consultaPersona!=null && consultaPersona.equals(ClsConstants.DB_TRUE) && ((!idPersonaInstitucionUserBean.equals(formulario.getIdPersona()) && formulario.getIdPersona().length()<4)) ) {
					  bConsultaPersona=true;
					}
	}
				
	
// CONTROL DE TIPO	
String tipoCliente="";
if (bColegiado) { 
	tipoCliente=ClsConstants.TIPO_CLIENTE_COLEGIADO;
} else {
	if (sTipo.equalsIgnoreCase("LETRADO")) {
		tipoCliente=ClsConstants.TIPO_CLIENTE_LETRADO;
	} else { 
		tipoCliente=ClsConstants.TIPO_CLIENTE_NOCOLEGIADO;
	}  
}  
String [] caracterParam = new String[1];
caracterParam[0] = tipoCliente;
	
%>
	   	
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.io.File"%>
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<script src="<%=app%>/html/js/jquery.js" type="text/javascript"></script>
		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="datosGeneralesForm" staticJavascript="false" />  
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

		<script>
		
		<!-- Funcion asociada a buscarGrupos() -->
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
			
	function formatearDocumento()
	{
	   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&(document.forms[0].numIdentificacion.value!="")) {
			var sNIF = document.forms[0].numIdentificacion.value;
			document.forms[0].numIdentificacion.value = formateaNIF(sNIF);
	   }else if((document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){		   
	   		var sNIE = document.forms[0].numIdentificacion.value;
			document.forms[0].numIdentificacion.value = formateaNIE(sNIE);
	   }
	}
	
	function generaNumOtro()
	{
		//Ejerciente - Letrado
		
	   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") && (document.forms[0].numIdentificacion.value=="")) {
		   <%if (cliente.equals("") || cliente.equals("No Colegiado")){%>
				generarIdenHistorico();
			<%}%>
			//NIHN (Número de Identificación Histórico para No colegiados) = 'NIHN' + idinstitucion + [0-9]{4}, donde el ultimo numero sera un max+1. Ej. NIHN20400011
		}
	   	
	}	
	$(document).ready(function(){
		$(".box").change(function() {	
		//$("input[type=select][name='tipoIdentificacion']").change(function(){
			if($(this).attr("name")=="tipoIdentificacion"){
				generaNumOtro();
			}
		 });	 
	});
	function generarIdenHistorico()
	{

		$.ajax({ //Comunicación jQuery hacia JSP  
	           type: "POST",
	           url: "/SIGA/CEN_DatosGenerales.do?modo=getIdenHistorico",
	           data: "idInstitucion="+'<%=institucionParam[0]%>',
	           //contentType: "application/json; charset=utf-8",
	           dataType: "json",
	           success:  function(json) {
		           		document.forms[0].numIdentificacion.value=json.numHistorico;
	           },
	           error: function(xml,msg){
	        	   //alert("Error1: "+xml);//$("span#ap").text(" Error");
	        	   alert("Error: "+msg);//$("span#ap").text(" Error");
	           }
	        }); 
	}

	function adaptaTamanoFoto () 
		{
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
  // alert("Salida formateaNIFMio:"+salida);
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
				 	
				 	//añadimos la letra derecha
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
	

	
	
	function validarNIE(a) 
	{
		
		var temp=a.toUpperCase();
		var cadenadni="TRWAGMYFPDXBNJZSQVHLCKE";
 		
 		alert("comp1");
			
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
		
	
	
	function nif(a) 
{
	var a = datosGeneralesForm.numIdentificacion.value;
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

	function mostrarMensaje() 
	{		
		<% if (mostrarMensaje!=null && !(mostrarMensaje.equals(""))) { %>			
			alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.existeEnBBDD"/>");
		<% }else if(mostrarMensajeNifCif!=null && !(mostrarMensajeNifCif.equals(""))){ %>
			alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.existeEnBBDDNIFCIF"/>");
		<%}%>			
	}
		
		
		
		</script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.datosGenerales.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.datosGenerales.cabecera" 
			localizacion="censo.fichaCliente.datosGenerales.localizacion"/>
		<%}%>	
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
	
<% } else { %>


	<body class="tablaCentralCampos" onload="adaptaTamanoFoto();buscarGrupos();mostrarMensaje();">

<%		if (!formulario.getAccion().equalsIgnoreCase("NUEVO")) {
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
			tratamientoSel.add((String) registro.get(CenClienteBean.C_IDTRATAMIENTO));
			tipoIdentificacionSel.add((String) registro.get(CenPersonaBean.C_IDTIPOIDENTIFICACION));
			estadoCivilSel.add((String) registro.get(CenPersonaBean.C_IDESTADOCIVIL));
			idiomaSel.add((String) registro.get(CenClienteBean.C_IDLENGUAJE));
			caracterSel.add((String) registro.get(CenClienteBean.C_CARACTER));
			
			fallecido=(String)registro.get(CenPersonaBean.C_FALLECIDO);
			/*if (fallecido==null || fallecido.equals("0")){
			  fallecido=ClsConstants.DB_FALSE;
			  checkFallecido="";
			
			
			}else { 
			  fallecido = (String) registro.get(CenPersonaBean.C_FALLECIDO);
			  checkFallecido="checked";
			
			}*/
			
		}
		else {
			tipoIdentificacionSel.add(""+ClsConstants.TIPO_IDENTIFICACION_NIF);
			descripcion = new Boolean(false);
		}
%>

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
						<html:file name="datosGeneralesForm" property="foto" size="8" styleClass="<%=estiloCaja %>" accept="image/gif,image/jpg" ></html:file>		
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
					<% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="box" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" />						
					<% } else { 
						if (bResidente) { %>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="<%=estiloCajaNif%>" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>"  readonly="<%=readonlyComboNIFCIF%>"/>						
						<% } else {%>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="boxConsulta" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" readonly="true"/>
						<% } %>
					<% } %>
					&nbsp;
				 <% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>
				      <html:text name="datosGeneralesForm" property="numIdentificacion" size="15" styleClass="<%=estiloCajaNif%>" value="<%=nIdentificacion %>" onfocus="generaNumOtro();" onBlur="formatearDocumento();"></html:text>&nbsp;
				      <input type="button" name="idButton" value='<siga:Idioma key="censo.nif.letra.letranif" />' onclick="generarLetra();" style="align:right" class="button">
				 <% }else{ %>
				     <% if(formulario.getAccion().equalsIgnoreCase("editar")){ %> 
				         <% if(!pintaComboSexo){ %>
							<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onBlur="formatearDocumento();"></html:text>
				         <%}else{ %>
				            <%if (bResidente){%>
									<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="<%=estiloCajaNif%>" value="<%=nIdentificacion %>"  onBlur="formatearDocumento();"></html:text>&nbsp;
								    <input type="button" name="idButton" value='<siga:Idioma key="censo.nif.letra.letranif" />' onclick="generarLetra();" style="align:right" class="button">
							<% }else{ %>
								<!-- Necesario para el validation.xml -->
								<html:text name="datosGeneralesForm" property="numIdentificacion" styleClass="boxConsulta" value="<%=nIdentificacion %>" />
							<% } %>
					    <% } %>
					<% }else{ %>
						<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onBlur="formatearDocumento();"></html:text>
					<% } %>
				 <% } %>
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
					<!-- TRATAMIENTO -->
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/>&nbsp;(*)
					</td>
					<td  style="width:200px">
						<siga:ComboBD nombre = "tratamiento" tipo="cmbTratamiento" clase="<%=estiloCaja %>" obligatorio="true" elementoSel="<%=tratamientoSel %>" readonly="<%=readonly %>" />						
					</td>
					<td class="labelText" style="width:170px">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp;(*)
					</td>
					
					<!-- NOMBRE -->
					<td>
				<% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>	
				       <html:text name="datosGeneralesForm" property="nombre" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=nombre %>"  ></html:text>
				<%}else{
				       if (!bResidente || bConsultaPersona) { %>
							<html:text name="datosGeneralesForm" property="nombre" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=nombre %>" readonly="true" ></html:text>
					<% } else { %>
							<html:text name="datosGeneralesForm" property="nombre" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=nombre %>" readonly="<%=breadonlyNombreApellidos %>" ></html:text>
					<% } 
				  } %>
					</td>
				</tr>
				<tr>
					<!-- APELLIDO1 -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.apellido1"/>&nbsp;(*)
					</td>				
					<td>
				<% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>	
				      <html:text name="datosGeneralesForm" property="apellido1" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=apellido1 %>" ></html:text>
			    <% }else{
				       if (!bResidente || bConsultaPersona) { %>
							<html:text name="datosGeneralesForm" property="apellido1" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=apellido1 %>" readonly="true" ></html:text>
					<% } else { %>
							<html:text name="datosGeneralesForm" property="apellido1" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=apellido1 %>" readonly="<%=breadonlyNombreApellidos %>" ></html:text>
					<% }
				   }  %>
					</td> 
					
					<!-- APELLIDO2 -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.apellido2"/>
					</td>				
					<td>
				 <% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>		
				    <html:text name="datosGeneralesForm" property="apellido2" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=apellido2 %>" ></html:text>
				 <% }else{
				       if (!bResidente || bConsultaPersona) { %>
							<html:text name="datosGeneralesForm" property="apellido2" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=apellido2 %>" readonly="true" ></html:text>
					<% } else { %>
							<html:text name="datosGeneralesForm" property="apellido2" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=apellido2 %>" readonly="<%=breadonlyNombreApellidos %>" ></html:text>
					<% } 
					} %>
					</td>
				</tr>
				<tr>
					<!-- FECHA DE NACIMIENTO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaNacimiento"/>&nbsp;
					</td>				
					<td>
					<% if (bConsultaPersona || !pintaCalendario) { %>
							<html:text name="datosGeneralesForm" property="fechaNacimiento" styleClass="boxConsulta" readonly="true" style='width:80px;' value="<%=fechaNacimiento %>" >
							</html:text>
					<% } else { %>
							<html:text name="datosGeneralesForm" property="fechaNacimiento" styleClass="<%=estiloCaja %>" readonly="true" style='width:80px;' value="<%=fechaNacimiento %>" >
							</html:text>
					<% }  %>
					<% if (!breadonly && !bConsultaPersona && pintaCalendario) { %>
							<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimiento);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
					<% } %>
					</td>
				
					<!-- LUGAR NACIMIENTO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nacido"/>
					</td>				
					<td>
					<% if (bConsultaPersona) { %>
							<html:text name="datosGeneralesForm" property="lugarNacimiento" size="20" maxlength="100" styleClass="boxConsulta" style='width:190px;' value="<%=nacido %>" readonly="true" ></html:text>
					<% } else { %>
							<html:text name="datosGeneralesForm" property="lugarNacimiento" size="20" maxlength="100" styleClass="<%=estiloCajaNombreApellidos %>" style='width:190px;' value="<%=nacido %>" readonly="<%=breadonlyNombreApellidos%>" ></html:text>
					<% }  %>
					</td>
				</tr>
				<tr>
					<!-- ESTADO CIVIL -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.estadoCivil"/>
					</td>				
					<td>
					<% if (bConsultaPersona) { %>
							<siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="boxConsulta" obligatorio="false" elementoSel="<%=estadoCivilSel %>" readonly="true"/>						
					<% } else { %>
							<siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="<%=estiloCajaNombreApellidos %>" obligatorio="false" elementoSel="<%=estadoCivilSel %>" readonly="<%=readonlyComboNIFCIF %>"/>						
					<% }  %>
					</td>
				
					<!-- SEXO -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>&nbsp;(*)
					</td>				
					<td>
					<% 
							String ssexo = "";
							if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
							if (sexo.equals(ClsConstants.TIPO_SEXO_MUJER)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer");
						if (breadonly || bConsultaPersona) { 
							
							if (bConsultaPersona || !pintaComboSexo) { 
					%>
								<!-- MAV 7/9/2005 Incorporo atributo ssexo para solventar incidencia -->
								<html:hidden  name="datosGeneralesForm" property="sexo" value="<%=sexo %>"/>
								<html:text name="datosGeneralesForm" property="ssexo" size="20" styleClass="boxConsulta" value="<%=ssexo %>" readonly="true" ></html:text>
					<% 		} else { %>
								<html:hidden  name="datosGeneralesForm" property="sexo" value="<%=sexo %>"/>
								<html:text name="datosGeneralesForm" property="ssexo" size="20" styleClass="<%=estiloCaja %>" value="<%=ssexo %>" readonly="<%=breadonly %>" ></html:text>
					<% 		} %>
					<% } else { 
					       if(pintaComboSexo){%>
							<!-- option select -->
							<html:select name="datosGeneralesForm" property="sexo" style = "null" styleClass = "<%=estiloCaja %>" value="<%=sexo %>"  readonly="<%=breadonly %>" >
							    <html:option value="0" >&nbsp;</html:option>
								<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
								<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
							</html:select>						
						<%}else{%>	
								<html:hidden  name="datosGeneralesForm" property="sexo" value="<%=sexo %>"/>
								<html:text name="datosGeneralesForm" property="ssexo" size="20" styleClass="boxConsulta" value="<%=ssexo %>" readonly="true" ></html:text>
					<% } %>
					<% } %>
					</td>
				
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.edad"/>
					</td>				
					<td class="boxConsulta">
						<%=edad%>
					</td>
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
						<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="<%=estiloCaja %>" obligatorio="true" elementoSel="<%=idiomaSel %>"  readonly="<%=readonly %>" obligatorioSinTextoSeleccionar="true" />
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
					<!-- CARACTER -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.caracter"/>&nbsp;
					</td>
					<td colspan="3">
						<siga:ComboBD nombre = "caracter" tipo="cmbCaracter" clase="boxConsulta" obligatorio="true" parametro="<%=caracterParam %>" elementoSel="<%=caracterSel %>" readonly="true" obligatorioSinTextoSeleccionar="true" />
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
							<input type="checkbox" name="exportarFoto"  value="<%=ClsConstants.DB_TRUE %>"  checked onchange="habilitarBoton()"/>
						<% } else { %>
							<input type="checkbox" name="exportarFoto"  value="<%=ClsConstants.DB_TRUE %>"  onchange="habilitarBoton()"/>
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
						<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
										id="resultado"
										name="resultado" 
										scrolling="no"
										frameborder="1"
										marginheight="0"
										marginwidth="0";					 
										style="width:100%; height:100%;">
						</iframe>
						<!-- FIN: IFRAME LISTA RESULTADOS -->
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</html:form>
	</table>

	
	<html:form action="/CEN_GruposFijosClientes.do" method="POST" target="resultado">
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
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
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

		<!-- Asociada al boton SolicitarModificacion -->
		function accionSolicitarModificacion() 	{
			document.forms[0].modo.value="abrirSolicitud";
			ventaModalGeneral(document.forms[0].name,'P');
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() {
			document.forms[0].reset();	
		}

		function habilitarBoton() {
			if(document.getElementById("modificar") != null){
				if(document.getElementById("modificar").disabled){
					document.getElementById("modificar").disabled = false;
				}
			}
		}

		function solicitarModificacion() {
		    var type = '¿Confirma la solicitud de modificación?';
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
		

	function validarFormulario() {

		
		if (datosGeneralesForm.sexo.value=='0'){
			alert ('<siga:Idioma key="censo.fichaCliente.literal.sexo"/>');
		   	return false;
		}
		if (validateDatosGeneralesForm(document.forms[0]))
		{
			var rc	= true;
			var tipoIden = document.datosGeneralesForm.tipoIdentificacion.value;
			
			if ((tipoIden == <%=tipoIdenNIF%>))
			{
				rc = validarNIFCIF(tipoIden, document.datosGeneralesForm.numIdentificacion.value);
			}
			else if((tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") )
			{
				rc=validaNIE(document.datosGeneralesForm.numIdentificacion.value);
			}
			else if((tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_PASAPORTE%>") && (document.forms[0].numIdentificacion.value=="") )
			{
				alert ('<siga:Idioma key="messages.pasaporte.comprobacion.error"/>');
				rc=false;
			}
			else if((tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_OTRO%>") && (document.forms[0].numIdentificacion.value=="") )
			{	
				alert ('<siga:Idioma key="messages.otro.comprobacion.error"/>');
				rc=false;
			}
			else if((document.forms[0].tipoIdentificacion.value== "") && (document.forms[0].numIdentificacion.value==""))
			{
				if((document.forms[0].cliente.valyue== "Letrado") || (document.forms[0].cliente.value=="Ejerciente"))
				{
					alert('<siga:Idioma key="messages.tipoIdenNumIden.comprobacion.error"/>');
					rc=false;
				}
			}
			return rc;	
		}
		else
		{			
		  return false;
		}
	}
		<!-- Asociada al boton Guardar -->
		function accionGuardar() {		
			sub();

			<%if (cliente.equals("") || cliente.equals("No Colegiado")){%>
				if((document.forms[0].tipoIdentificacion.value== "") && (document.forms[0].numIdentificacion.value==""))
					alert ("<siga:Idioma key="messages.tipoIdenti.comprobacion.aviso"/>");
			<%}%>

				if (validarFormulario()	&&	TestFileType(document.forms[0].foto.value, ['GIF', 'JPG', 'PNG', 'JPEG'])) {
				<%	if (!formulario.getAccion().equals("nuevo")) { %>
					<% if (!bOcultarHistorico) { %>
							var datos = showModalDialog("<%=app%>/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
						<% } else { %>
								var datos = new Array();
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


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>