<!DOCTYPE html>
<html>
<head>
<!-- designarArticulo27.jsp -->
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
<%@ page import="com.siga.censo.form.BusquedaCensoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%> 
<%@ page import="com.siga.beans.CenTipoDireccionBean"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.siga.beans.CenInstitucionBean"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	DatosGeneralesForm formulario = (DatosGeneralesForm)request.getAttribute("datosGeneralesForm");
	String modo = "nuevo";

	// variables
	Vector vTipos    = (Vector)request.getAttribute("vTipos");
	Hashtable hTiposDir    = (Hashtable)request.getAttribute("TipoDirecciones");	
	ArrayList gruposSel = new ArrayList();	
	String cliente = "";
	String numeroColegiado = "";
	boolean bColegiado = false;
	boolean bfCertificado=true;
	String fechaCertificado = "";
	
	//  tratamiento de readonly
	String estiloCaja = "box";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	
	// seleccion de combos
	ArrayList tratamientoSel = new ArrayList();
	ArrayList tipoIdentificacionSel = new ArrayList();
	ArrayList estadoCivilSel = new ArrayList();
	ArrayList idiomaSel = new ArrayList();
	ArrayList caracterSel = new ArrayList();
	
	//Para el Combo de Turnos
	String dato[] = new String[2];

	boolean bLoadSelected = false;
	String idInstitucion = "";
	String idInstitucionActual = user.getLocation();
	String idInstitucionSeleccionada = "";
	String idPersona = "";
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	String nIdentificacion = "";
	String fechaNacimiento = "";
	String fechaAlta = "";
	String nacido = "";
	String abono = "";
	String cargo = "";
	String sexo = "";
	String idioma = "";
	String estadoCivil = "";
	String idDireccionSeleccionada = "";
	if (formulario.getNumIdentificacion() != null && !"".equals(formulario.getNumIdentificacion())){
		nIdentificacion = formulario.getNumIdentificacion();
		if (formulario.getDatos() != null && formulario.getDatos().containsKey("idInstitucionOrigen")){
			idInstitucionSeleccionada = formulario.getDatos().get("idInstitucionOrigen").toString();		
		}
		if (formulario.getIdDireccion() != null && !"".equals(formulario.getIdDireccion())){
			idDireccionSeleccionada = formulario.getIdDireccion();
		}
		bLoadSelected = true;
	}	
	int tipoIdenNIF = ClsConstants.TIPO_IDENTIFICACION_NIF;
    int tipoIdenCIF = ClsConstants.TIPO_IDENTIFICACION_CIF;

	//Toma los de la institucion:
	String [] institucionParam = new String[1];
   	institucionParam[0] = user.getLocation();
   	
   	ArrayList institucionSel = new ArrayList();
   	institucionSel.add(0,idInstitucionActual);
  	
	String estiloCajaNif = "box";
	String estiloCajaNombreApellidos = "box";
	String readonlyComboNIFCIF = "false";
	
    String sTipo = (String) request.getAttribute("TIPO");
	
	if (sTipo == null)
		sTipo = "";	
	
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
<style>
.ocultar {display:none}
</style>	

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<%@page import="java.util.List"%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!--Step 2 -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>	


		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="datosGeneralesForm" staticJavascript="false" /> 
			<html:javascript formName="datosGeneralesDireccionForm" staticJavascript="false" /> 
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

		<script>
		jQuery.noConflict();
		var bLoadSelected = false;
		function inicio(){
			if(document.getElementById('idButtonGuardar'))
				jQuery("#idButtonGuardar").attr("disabled","disabled");
	
			jQuery("#idButtonB").removeAttr("disabled");
		}
		
		function buscarDesignados (){

			if ((document.getElementById('colegiadoen').value 			== null || document.getElementById('colegiadoen').value == "") &&
				(document.datosGeneralesForm.numIdentificacion.value 	== null || document.datosGeneralesForm.numIdentificacion.value == "") &&	
				(document.datosGeneralesForm.nombre.value 				== null || document.datosGeneralesForm.nombre.value == "") &&
				(document.datosGeneralesForm.nColegiado.value 			== null || document.datosGeneralesForm.nColegiado.value == "") &&
				(document.datosGeneralesForm.apellido2.value 			== null || document.datosGeneralesForm.apellido2.value == "") &&
				(document.datosGeneralesForm.apellido1.value 			== null || document.datosGeneralesForm.apellido1.value == "")){
						
				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.camposVacios"/>" ;
		 		 alert (mensaje);			 
				 return false;
			}
						
			busquedaCensoModalForm.modo.value="buscarTodosArt27Modal";
			if(document.datosGeneralesForm.nColegiado.value != null && document.datosGeneralesForm.nColegiado.value != ""){
				busquedaCensoModalForm.numeroColegiado.value=document.datosGeneralesForm.nColegiado.value;
			}else{
				busquedaCensoModalForm.numeroColegiado.value="";
			}
			
			if(document.datosGeneralesForm.apellido2.value != null && document.datosGeneralesForm.apellido2.value != ""){
				busquedaCensoModalForm.apellido2.value=document.datosGeneralesForm.apellido2.value;
			}else{
				busquedaCensoModalForm.apellido2.value="";
			}

			if(document.datosGeneralesForm.apellido1.value != null && document.datosGeneralesForm.apellido1.value != ""){
				busquedaCensoModalForm.apellido1.value=document.datosGeneralesForm.apellido1.value;
			}else{
				busquedaCensoModalForm.apellido1.value="";
			}

			if(document.datosGeneralesForm.nombre.value != null && document.datosGeneralesForm.nombre.value != ""){
				busquedaCensoModalForm.nombre.value=document.datosGeneralesForm.nombre.value;
			}else{
				busquedaCensoModalForm.nombre.value="";
			}

			if(document.datosGeneralesForm.idPersona.value != null && document.datosGeneralesForm.idPersona.value != ""){
				busquedaCensoModalForm.idPersona.value=document.datosGeneralesForm.idPersona.value;
			}else{
				busquedaCensoModalForm.idPersona.value="";
			}

			if(document.datosGeneralesForm.idInstitucion.value != null && document.datosGeneralesForm.idInstitucion.value != ""){
				busquedaCensoModalForm.idInstitucion.value=document.datosGeneralesForm.idInstitucion.value;
			}else{
				busquedaCensoModalForm.idInstitucion.value="";
			}

			if(document.datosGeneralesForm.numIdentificacion.value != null && document.datosGeneralesForm.numIdentificacion.value != ""){		
				busquedaCensoModalForm.nif.value=document.datosGeneralesForm.numIdentificacion.value;			
			}else{
				busquedaCensoModalForm.nif.value="";
			}			

			if(document.getElementById('colegiadoen').value != null && document.getElementById('colegiadoen').value != ""){		
				busquedaCensoModalForm.colegiadoen.value = document.getElementById('colegiadoen').value;			
			}else{
				busquedaCensoModalForm.colegiadoen.value = "";
			}
			
			var resultado = ventaModalGeneral("busquedaCensoModalForm","G");	
			if (resultado!=undefined && resultado[0]!=undefined ){				
				ponerIconoIdentPersona(true);
		      	document.getElementById("textomod").className="ocultar";
		      	document.getElementById("textomod").style.display="none";
		      	if(document.getElementById('idButtonGuardar'))
			      	jQuery("#idButtonGuardar").removeAttr("disabled");
		      	jQuery("#idButtonB").attr("disabled","disabled");
				document.getElementById("tdadicional").style.display="block";
				document.getElementById("tddireccion").style.display="block";		      	
				datosGeneralesForm.idPersona.value       = resultado[0];
				datosGeneralesForm.idInstitucion.value   = resultado[1];				

				//Datos Generales
				datosGeneralesForm.numIdentificacion.value = resultado[3];
				document.busquedaCensoModalForm.nif.value= datosGeneralesForm.numIdentificacion.value;
				datosGeneralesForm.colegiadoen.value = resultado[20];
				datosGeneralesForm.nColegiado.value   =  resultado[2];
				datosGeneralesForm.tratamiento.value=resultado[15];		
				datosGeneralesForm.nombre.value          = resultado[4];
				datosGeneralesForm.apellido1.value       = resultado[5];
				datosGeneralesForm.apellido2.value = resultado[6]; 
				datosGeneralesForm.tipoIdentificacion.value = resultado[21]; 

				//Informacion adicional
				datosGeneralesForm.sexo.value=resultado[14];
				datosGeneralesForm.lugarNacimiento.value=resultado[18];
				datosGeneralesForm.fechaNacimiento.value=resultado[19];

				//Datos direcciones
				if(resultado[1] != "<%=idInstitucionActual%>"){
					
					limpiarDireccion();
					jQuery("#direcciones").append("<option selected value='-1'>-- Nueva</option>");
					jQuery("#direcciones").attr("disabled","disabled");
					document.getElementById("correoElectronico").value 	= resultado[13];
					document.getElementById("fax1").value 	= resultado[16];
					document.getElementById("telefono1").value 			= resultado[12];
					document.getElementById("domicilio").value 			= resultado[7];
					document.getElementById("domicilio").value 			= resultado[11].replace(/\r\n|\r|\n/g, " ");
					document.getElementById("pais").value 				= resultado[10];
					selPais(resultado[10]);
			
					if (resultado[10] != "" && resultado[10] != idEspana) {
						datosGeneralesForm.poblacionExt.value=resultado[8];
					}else{
						document.getElementById("provincia").value = resultado[9];
						poblacionSeleccionada = resultado[8];
						document.busquedaCensoModalForm.poblacionValue.value = resultado[8];
						document.getElementById("provincia").onchange();
						//window.setTimeout("recargarComboHijo()",500,"Javascript");	
						//document.getElementById("poblacion").value = resultado[8];		
					}
					document.getElementById("checkTipoDireccion_3").checked = "checked";
					
				}else{
					limpiarDireccion();
					datosGeneralesForm.idPersona.onchange();
				}

				// SE DESHABILITAN LOS DATOS GENERALES
					jQuery("#idButtonB").removeAttr("disabled");
					
					jQuery("#tipoIdentificacion").attr("disabled","disabled");
					jQuery("#numIdentificacion").attr("disabled","disabled");
					jQuery("#idInstitucion").attr("disabled","disabled");
					jQuery("#colegiadoen").attr("disabled","disabled");
					jQuery("#nColegiado").attr("disabled","disabled");
					jQuery("#nombre").attr("disabled","disabled");
					jQuery("#apellido1").attr("disabled","disabled");
					jQuery("#apellido2").attr("disabled","disabled");
					// SE DESHABILITAN LOS DATOS DE INFORMACIÓN ADICIONAL
					jQuery("#sexo").attr("disabled","disabled");
					jQuery("#estadoCivil").attr("disabled","disabled");
					jQuery("#lugarNacimiento").attr("disabled","disabled");
					jQuery("#fechaNacimiento").attr("disabled","disabled");
				
			}							
		}		

	function limpiarCliente (){
		document.getElementById('tipoIdentificacion').value = "10";
		document.getElementById('numIdentificacion').value = "";
		document.getElementById('colegiadoen').value = '<%=idInstitucionActual%>';
		document.getElementById('nColegiado').value = "";
		document.getElementById('tratamiento').value = "";
		document.getElementById('nombre').value = "";
		document.getElementById('apellido1').value = "";
		document.getElementById('apellido2').value = "";
		jQuery("#tipoIdentificacion").removeAttr("disabled");
		jQuery("#numIdentificacion").removeAttr("disabled");
		jQuery("#idInstitucion").removeAttr("disabled");
		jQuery("#colegiadoen").removeAttr("disabled");
		jQuery("#nColegiado").removeAttr("disabled");
		jQuery("#nombre").removeAttr("disabled");
		jQuery("#apellido1").removeAttr("disabled");
		jQuery("#apellido2").removeAttr("disabled");
		// SE DESHABILITAN LOS DATOS DE INFORMACIÓN ADICIONAL
		jQuery("#sexo").removeAttr("disabled");
		jQuery("#estadoCivil").removeAttr("disabled");
		jQuery("#lugarNacimiento").removeAttr("disabled");
		jQuery("#fechaNacimiento").removeAttr("disabled");
		jQuery("#tratamiento").removeAttr("disabled");

		ponerIconoIdentPersona(false);
	}	
			
	function formatearDocumento() {
	   if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&&(document.forms[0].numIdentificacion.value!="")) {
			var sNIF = document.forms[0].numIdentificacion.value;
			document.forms[0].numIdentificacion.value = formateaNIF(sNIF);
	   }else if((document.forms[0].tipoIdentificacion.value == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ){
	   		var sNIE = document.forms[0].numIdentificacion.value;
			document.forms[0].numIdentificacion.value = formateaNIE(sNIE);
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
					alert ("<siga:Idioma key="messages.nif.comprobacion.digitos.error"/>");
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
    		}
    		
    		
			if (valorX==null) {
				//salida = relleno("0",longitud);
			} else {
				
				if (valorX.length==0) {			 
					//salida = relleno("0",longitud);
					
				} else{
				   if (valorX.length > longitud) {
					// mayor
					alert ("<siga:Idioma key="messages.nie.comprobacion.digitos.error"/>");
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
	
	function validarNIE(a) {
		var a = trim(datosGeneralesForm.numIdentificacion.value);
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

	function bloquearBuscar(){
	   	jQuery("#idButtonB").attr("disabled","disabled");
	}	
		
	function rellenarComboIden(){
		if(nif(datosGeneralesForm.numIdentificacion.value) == 1){
			datosGeneralesForm.tipoIdentificacion.value = "10";
		}else if(validarNIE(datosGeneralesForm.numIdentificacion.value) == 3){
			datosGeneralesForm.tipoIdentificacion.value = "40";
		}else{
			datosGeneralesForm.tipoIdentificacion.value = "50";
		}

		if(datosGeneralesForm.numIdentificacion.value == null || datosGeneralesForm.numIdentificacion.value == ""){
			jQuery("#idButtonB").removeAttr("disabled");
		}
	}
	
	function nif(a) {
		
		var a = trim(datosGeneralesForm.numIdentificacion.value);
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

	function bloquearDireccion(){

		//Datos direccion
		jQuery("#domicilio").attr("disabled","disabled");
	   	jQuery("#codigoPostal").attr("disabled","disabled");
	   	jQuery("#pais").attr("disabled","disabled");
	   	jQuery("#provincia").attr("disabled","disabled");
	   	jQuery("#poblacion").attr("disabled","disabled");
	   	jQuery("#poblacionFrame").contents().find("#poblacionSel").attr("disabled","disabled");	   	
	   	jQuery("#movil").attr("disabled","disabled");
	   	jQuery("#telefono1").attr("disabled","disabled");
	   	jQuery("#telefono2").attr("disabled","disabled");
	   	jQuery("#fax1").attr("disabled","disabled");
	   	jQuery("#fax2").attr("disabled","disabled");
	   	jQuery("#correoElectronico").attr("disabled","disabled");
	   	jQuery("#paginaWeb").attr("disabled","disabled");
	   	jQuery("#poblacionExt").attr("disabled","disabled");
	   	
		//Preferencia
		jQuery("#preferenteMail").attr("disabled","disabled");
	   	jQuery("#preferenteCorreo").attr("disabled","disabled");
	   	jQuery("#preferenteFax").attr("disabled","disabled");
	   	jQuery("#preferenteSms").attr("disabled","disabled");

		//Tipo Direccion
		jQuery("#checkTipoDireccion_1").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_2").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_3").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_4").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_5").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_6").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_7").attr("disabled","disabled");
	   	jQuery("#checkTipoDireccion_8").attr("disabled","disabled");

	}	
	  	
	
	function desBloquearDireccion(){

		//Datos direccion
		jQuery("#domicilio").removeAttr("disabled");
	   	jQuery("#codigoPostal").removeAttr("disabled");
	   	
	   	jQuery("#pais").removeAttr("disabled");
	   	jQuery("#provincia").removeAttr("disabled");
	   	jQuery("#poblacion").removeAttr("disabled");
	   	jQuery("#poblacionFrame").contents().find("#poblacionSel").removeAttr("disabled"); 
	   	jQuery("#poblacionExt").removeAttr("disabled");
	   	cargarProvincias(datosGeneralesForm.pais);	

	   	jQuery("#movil").removeAttr("disabled");
	   	jQuery("#telefono1").removeAttr("disabled");
	   	jQuery("#telefono2").removeAttr("disabled");
	   	jQuery("#fax1").removeAttr("disabled");
	   	jQuery("#fax2").removeAttr("disabled");
	   	jQuery("#correoElectronico").removeAttr("disabled");
	   	jQuery("#paginaWeb").removeAttr("disabled");
	   	
		//Preferencia
		jQuery("#preferenteMail").removeAttr("disabled");
	   	jQuery("#preferenteCorreo").removeAttr("disabled");
	   	jQuery("#preferenteFax").removeAttr("disabled");
	   	jQuery("#preferenteSms").removeAttr("disabled");

		//Tipo Direccion
		jQuery("#checkTipoDireccion_1").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_2").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_3").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_4").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_5").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_6").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_7").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_8").removeAttr("disabled");

	}		

	function limpiarDireccion(){

		//Combo Direccioens
		document.getElementById("direcciones").value 		= "-1";
		jQuery("#direcciones").attr("disabled","disabled");
		 
		//Datos direccion
		document.getElementById("domicilio").value 			= "";
		document.getElementById("codigoPostal").value 		= "";
		document.getElementById("pais").value 				= "";
		document.getElementById("provincia").value 			= "";
		document.getElementById("provincia").onchange();
		document.getElementById("poblacionExt").value 		= "";
		document.getElementById("poblacion").value 			= "";
		document.getElementById("movil").value 				= "";
		document.getElementById("telefono1").value 			= "";
		document.getElementById("telefono2").value 			= "";
		document.getElementById("fax1").value 				= "";
		document.getElementById("fax2").value 				= "";
		document.getElementById("correoElectronico").value 	= "";
		document.getElementById("paginaWeb").value 			= "";

		//Preferencia
		document.datosGeneralesForm.preferenteMail.checked 		= "";
		document.datosGeneralesForm.preferenteCorreo.checked 	= "";
		document.datosGeneralesForm.preferenteFax.checked  		= "";
		document.datosGeneralesForm.preferenteSms.checked  		= "";

		//Tipo Direccion
		document.getElementById("checkTipoDireccion_1").checked = "";
		document.getElementById("checkTipoDireccion_2").checked = "";
		document.getElementById("checkTipoDireccion_3").checked = "";
		document.getElementById("checkTipoDireccion_4").checked = "";
		document.getElementById("checkTipoDireccion_5").checked = "";
		document.getElementById("checkTipoDireccion_6").checked = "";
		document.getElementById("checkTipoDireccion_7").checked = "";
		document.getElementById("checkTipoDireccion_8").checked = "";

		//Datos direccion
		jQuery("#domicilio").removeAttr("disabled");
	   	jQuery("#codigoPostal").removeAttr("disabled");
	   	jQuery("#pais").removeAttr("disabled");
	   	jQuery("#provincia").removeAttr("disabled");
	   	jQuery("#poblacion").removeAttr("disabled");
	   	jQuery("#poblacionFrame").contents().find("#poblacionSel").removeAttr("disabled");
	   	jQuery("#movil").removeAttr("disabled");
	   	jQuery("#telefono1").removeAttr("disabled");
	   	jQuery("#telefono2").removeAttr("disabled");
	   	jQuery("#fax1").removeAttr("disabled");
	   	jQuery("#fax2").removeAttr("disabled");
	   	jQuery("#correoElectronico").removeAttr("disabled");
	   	jQuery("#paginaWeb").removeAttr("disabled");
	   	jQuery("#poblacionExt").removeAttr("disabled");
	   	
		//Preferencia
		jQuery("#preferenteMail").removeAttr("disabled");
	   	jQuery("#preferenteCorreo").removeAttr("disabled");
	   	jQuery("#preferenteFax").removeAttr("disabled");
	   	jQuery("#preferenteSms").removeAttr("disabled");

		//Tipo Direccion
		jQuery("#checkTipoDireccion_1").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_2").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_3").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_4").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_5").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_6").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_7").removeAttr("disabled");
	   	jQuery("#checkTipoDireccion_8").removeAttr("disabled");
	}

	var poblacionSeleccionada;

	function postAccionDirecciones() {	
		restablecerComboPoblacion();
		document.getElementById("correoElectronico").value 	= document.busquedaCensoModalForm.mail.value;
		document.getElementById("telefono1").value 			= document.busquedaCensoModalForm.telefono.value;
		document.getElementById("domicilio").value 			= document.busquedaCensoModalForm.direccion.value.replace(/\r\n|\r|\n/g, " ");
		document.getElementById("codigoPostal").value 		= document.busquedaCensoModalForm.codPostal.value;
		selPais(document.datosGeneralesForm.pais.value);

		if (document.datosGeneralesForm.pais.value != "" && document.datosGeneralesForm.pais.value != idEspana) {
			datosGeneralesForm.poblacionExt.value=document.datosGeneralesForm.poblacionExt.value;
		}else{
			poblacionSeleccionada = document.busquedaCensoModalForm.poblacionValue.value;
			document.getElementById("provincia").onchange();
			jQuery("#poblacion").attr("disabled","disabled");
		}

		if(document.datosGeneralesForm.preferente.value != null && document.datosGeneralesForm.preferente.value != ""){
			
			if (document.datosGeneralesForm.preferente.value.indexOf("E") >= 0)
				document.datosGeneralesForm.preferenteMail.checked = "checked";
			if (document.datosGeneralesForm.preferente.value.indexOf("C") >= 0)
				document.datosGeneralesForm.preferenteCorreo.checked  = "checked";
			if (document.datosGeneralesForm.preferente.value.indexOf("F") >= 0)
				document.datosGeneralesForm.preferenteFax.checked  = "checked";
			if (document.datosGeneralesForm.preferente.value.indexOf("S") >= 0)
				document.datosGeneralesForm.preferenteSms.checked  = "checked";
		}else{
			document.datosGeneralesForm.preferenteMail.checked = "";
			document.datosGeneralesForm.preferenteCorreo.checked  = "";
			document.datosGeneralesForm.preferenteFax.checked  = "";
			document.datosGeneralesForm.preferenteSms.checked  = "";
		}	

		if(document.busquedaCensoModalForm.tipoDireccion.value != null && document.busquedaCensoModalForm.tipoDireccion.value != ""){
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("1") >= 0)
				document.getElementById("checkTipoDireccion_1").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("2") >= 0)
				document.getElementById("checkTipoDireccion_2").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("3") >= 0)
				document.getElementById("checkTipoDireccion_3").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("4") >= 0)
				document.getElementById("checkTipoDireccion_4").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("5") >= 0)
				document.getElementById("checkTipoDireccion_5").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("6") >= 0)
				document.getElementById("checkTipoDireccion_6").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("7") >= 0)
				document.getElementById("checkTipoDireccion_7").checked = "checked";
			if (document.busquedaCensoModalForm.tipoDireccion.value.indexOf("8") >= 0)
				document.getElementById("checkTipoDireccion_8").checked = "checked";			
		}else{
			document.getElementById("checkTipoDireccion_1").checked = "";
			document.getElementById("checkTipoDireccion_2").checked = "";
			document.getElementById("checkTipoDireccion_3").checked = "";
			document.getElementById("checkTipoDireccion_4").checked = "";
			document.getElementById("checkTipoDireccion_5").checked = "";
			document.getElementById("checkTipoDireccion_6").checked = "";
			document.getElementById("checkTipoDireccion_7").checked = "";
			document.getElementById("checkTipoDireccion_8").checked = "";	
		}
		
		if(document.datosGeneralesForm.direcciones.value == "-1"){
	      	document.getElementById("textomod").className="ocultar";
	      	document.getElementById("textomod").style.display="none";	  
	      	
			//Se DESbloquean los datos de direccion. Solo se podrán mdficar en la ficha del letrado.
			desBloquearDireccion();
		}else{
			document.getElementById("textomod").className="labelText";
			document.getElementById("textomod").style.display="block";
			
			//Se bloquean los datos de direccion. Solo se podrán mdficar en la ficha del letrado.
			bloquearDireccion();
		}			

	}

	function preAccionBusquedaNIF(){
		limpiarDireccion();
	}
	
	function postAccionBusquedaNIF(){		
		if(document.busquedaCensoModalForm.existeNIF.value != null && document.busquedaCensoModalForm.existeNIF.value == "S"){
			if (bLoadSelected){				
				datosGeneralesForm.numIdentificacion.onchange();
			} else{
				if(confirm('El nº de identificación ya se encuentra registrado, Pulse "Aceptar" para recuperar y sustituir los datos actuales o "Cancelar" para modificarlo')) {
					datosGeneralesForm.numIdentificacion.onchange();
				}else{
	
					//Aparecen los menus de abajo y se deja el NIF introducido
					if(document.getElementById('idButtonGuardar'))
						jQuery("#idButtonGuardar").removeAttr("disabled");
					document.getElementById("tdadicional").style.display="block";
					document.getElementById("tddireccion").style.display="block";
					jQuery("#idButtonB").attr("disabled","disabled");
					jQuery("#sexo").removeAttr("disabled");
					jQuery("#estadoCivil").removeAttr("disabled");
					jQuery("#lugarNacimiento").removeAttr("disabled");
					jQuery("#fechaNacimiento").removeAttr("disabled");
					document.busquedaCensoModalForm.existeNIF.value = "";
				}
			}
			
		}else{				
			if(datosGeneralesForm.idPersona.value != null && datosGeneralesForm.idPersona.value != ""){ //EXISTE LA PERSONA
				if(document.busquedaCensoModalForm.multiple.value != null && document.busquedaCensoModalForm.multiple.value != "S"){ //UNICO REGISTRO
					ponerIconoIdentPersona(true);
					formatearDocumento();
					document.getElementById("tipoIdentificacion").value = document.busquedaCensoModalForm.idTipoIdentificacion.value;

					//Datos direcciones
					if(datosGeneralesForm.idInstitucion.value != "<%=idInstitucionActual%>"){
						jQuery("#direcciones").append("<option selected value='-1'>-- Nueva</option>");
						jQuery("#direcciones").attr("disabled","disabled");
						document.getElementById("correoElectronico").value 	= document.busquedaCensoModalForm.mail.value;
						document.getElementById("telefono1").value 			= document.busquedaCensoModalForm.telefono.value;
						document.getElementById("codigoPostal").value 		= document.busquedaCensoModalForm.codPostal.value;
						//QUitamos los salto de linea a la dirección
						document.getElementById("domicilio").value 			= document.busquedaCensoModalForm.direccion.value.replace(/\r\n|\r|\n/g, " ");
						
						selPais(datosGeneralesForm.pais.value);	
						if (datosGeneralesForm.pais.value != "" && datosGeneralesForm.pais.value != idEspana){
							datosGeneralesForm.poblacionExt.value=datosGeneralesForm.poblacionExt.value;
							
						}else{
							poblacionSeleccionada = document.busquedaCensoModalForm.poblacionValue.value;
							document.getElementById("provincia").onchange();
							//window.setTimeout("recargarComboHijo()",500,"Javascript");	
							document.getElementById("poblacion").value = datosGeneralesForm.poblacion.value;		
						}
						document.getElementById("checkTipoDireccion_3").checked = "checked";
			
					}else{
						limpiarDireccion();
						datosGeneralesForm.idPersona.onchange();
					}
			
					if(datosGeneralesForm.numIdentificacion.value != null && datosGeneralesForm.numIdentificacion.value != ""){		
						//SE DESHABILITAN LOS DATOS DE IDENTIFICACIÓN 
						jQuery("#tipoIdentificacion").attr("disabled","disabled");
						jQuery("#numIdentificacion").attr("disabled","disabled");
						jQuery("#idInstitucion").attr("disabled","disabled");
						jQuery("#colegiadoen").attr("disabled","disabled");
						jQuery("#nColegiado").attr("disabled","disabled");
						jQuery("#nombre").attr("disabled","disabled");
						jQuery("#apellido1").attr("disabled","disabled");
						jQuery("#apellido2").attr("disabled","disabled");
					}
	
					//Aparecen los menus de abajo
					if(document.getElementById('idButtonGuardar'))
						jQuery("#idButtonGuardar").removeAttr("disabled");
					document.getElementById("tdadicional").style.display="block";
					document.getElementById("tddireccion").style.display="block";
					jQuery("#idButtonB").attr("disabled","disabled");
					
					if(datosGeneralesForm.numIdentificacion.value != null && datosGeneralesForm.numIdentificacion.value != ""){	
						// SE DESHABILITAN LOS DATOS DE INFORMACIÓN ADICIONAL
						jQuery("#sexo").attr("disabled","disabled");
						jQuery("#estadoCivil").attr("disabled","disabled");
						jQuery("#lugarNacimiento").attr("disabled","disabled");
						jQuery("#fechaNacimiento").attr("disabled","disabled");
					}
					
				}else{ //SE ABRE VENTANA MODAL AL SER BUSQUEDA MULTIPLE
					limpiarDireccion();
					buscarDesignados ();
				}				
			}else{ //NO EXISTE LA PERSONA
				ponerIconoIdentPersona(false);
				limpiarDireccion();
				if(document.busquedaCensoModalForm.textoAlerta.value != null && document.busquedaCensoModalForm.textoAlerta.value != ""){
					alert(document.busquedaCensoModalForm.textoAlerta.value);
				}
	
				
				if(datosGeneralesForm.numIdentificacion.value != null && datosGeneralesForm.numIdentificacion.value != ""){
					//Aparecen los menus de abajo
					jQuery("#idButtonB").attr("disabled","disabled");
					jQuery("#idButtonGuardar").removeAttr("disabled");

					document.getElementById("tdadicional").style.display="block";
					document.getElementById("tddireccion").style.display="block";
	
				}else{
					limpiarCliente();
				}	

				datosGeneralesForm.nColegiado.value="";  
				datosGeneralesForm.colegiadoen.value="";  
				jQuery("#colegiadoen").attr("disabled","disabled");
				jQuery("#nColegiado").attr("disabled","disabled");
					
			}
	
			//Reseteamos el texto de alerta
			document.busquedaCensoModalForm.textoAlerta.value="";
		}
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

	function postAccionComboDirecciones(){
		if (bLoadSelected){			
			bLoadSelected = false;
			<%if (!"".equals(idDireccionSeleccionada)){%>		
			jQuery("#direcciones").val("<%=idDireccionSeleccionada%>");
			jQuery("#direcciones").change();
			<%}%>
		}
		if (jQuery("#direcciones option").length > 1){
			bloquearDireccion();
		}
		document.busquedaCensoModalForm.poblacionValue.value = "";
		document.getElementById("direcciones").onchange();
	}
	
	function loadSelected(){
		bLoadSelected = true;		
		jQuery("#colegiadoen").val("<%="".equals(idInstitucionSeleccionada)?idInstitucionActual:idInstitucionSeleccionada%>");
		jQuery("#colegiadoen").change();
		datosGeneralesForm.numIdentificacion.onchange();
	}
		</script>

	</head>

	<body onload="comprobarTelefonoAsterico();selPaisInicio();inicio();<%if (bLoadSelected){%>loadSelected();<%}%>" class="tablaCentralCampos">

<%
			tipoIdentificacionSel.add(""+ClsConstants.TIPO_IDENTIFICACION_NIF);
%>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td class="titulitosDatos" >
				<siga:Idioma key="censo.designarArticulo27.cabecera"/>
			</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!------------------------------->
	<!-- Tabla central principal -->
	<!------------------------------->
	<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
		<html:form  action="/CEN_DatosGenerales" method="POST" target="submitArea"  enctype="multipart/form-data">
			<html:hidden  name="datosGeneralesForm" property="modo"  styleId="modo"/>
			<html:hidden  name="datosGeneralesForm" property="idInstitucion" styleId="idInstitucion"/>
			<html:hidden  name="datosGeneralesForm" property="idPersona"  styleId="idPersona"/>
			<html:hidden  name="datosGeneralesForm" property="accion" styleId="accion"/>
			<html:hidden name="datosGeneralesForm" property="motivo" styleId="motivo"/>
			<html:hidden name="datosGeneralesForm" property="abono"  styleId="abono" value="B" />
			<html:hidden name="datosGeneralesForm" property="cargo" styleId="cargo" value="B" />
			<html:hidden name="datosGeneralesForm" property="idTipoDireccion" styleId="idTipoDireccion" value=""/>
			<html:hidden name="datosGeneralesForm" property="preferente" styleId="preferente" />
			<html:hidden property="actionModal" value=""/>
			<html:hidden property="tipo" value="<%=sTipo%>"/>
 
	<!-- FILA 2: Informacion Personal -->
	<tr>
		<td>
			<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
			<table class="tablaCampos" align="center" >
				<tr>
					<!-- TIPO IDENTIFICACION -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nIdentificacion"/>&nbsp;(*)
					</td>				
					<td colspan="3">
  				        <html:text name="datosGeneralesForm" property="numIdentificacion" size="20" maxlength="20" styleClass="<%=estiloCajaNif%>" value="<%=nIdentificacion%>" onblur="rellenarComboIden()" onmousedown="bloquearBuscar()"></html:text>
  				        <siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="box" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" />
  				        <img id="info_existe" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>		
					</td>
				</tr>
				<tr>
					<!-- COLEGIADO EN -->
					<td class="labelText">
						<siga:Idioma key="pys.solicitudCompra.literal.colegiadoen"/>&nbsp;
					</td>
					<td>
						<siga:ComboBD nombre = "colegiadoen" tipo="cmbColegiosAbreviados"  clase="<%=estiloCaja %>" estilo='width:160px;' obligatorio="true" readonly="<%=readonly %>" elementoSel="<%=institucionSel%>"/>						
					</td>
					<td class="labelText">
						<siga:Idioma key="censo.busquedaClientes.literal.nColegiado"/>&nbsp;
					</td>
					
					<!-- Nº COLEGIADO -->
					<td>
				       <html:text name="datosGeneralesForm" styleId="nColegiado" property="nColegiado" size="10" maxlength="20" styleClass="<%=estiloCajaNombreApellidos %>"  value="<%=nombre %>"  ></html:text>
					</td>
					<td>
						<!-- Boton para buscar un Colegiado -->
						<input type="button" class="button" id="idButtonB" name="buscarColegiado"  value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>'  onClick="buscarDesignados();" disabled="">
					</td>
				</tr>				
				<tr>
					
					<!-- NOMBRE -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp;(*)
					</td>
					<td>
				       <html:text name="datosGeneralesForm" styleId="nombre" property="nombre" size="20" maxlength="80" styleClass="<%=estiloCajaNombreApellidos %>" style='width:160px;' value="<%=nombre %>"  ></html:text>
					</td>

					<!-- APELLIDOS -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.apellidos"/>&nbsp;(*)
					</td>
					
					<td>
					<!-- APELLIDO 1 -->	
				      <html:text name="datosGeneralesForm" styleId="apellido1" property="apellido1" size="20" maxlength="80" styleClass="<%=estiloCajaNombreApellidos %>" style='width:160px;' value="<%=apellido1 %>" ></html:text>
					<!-- APELLIDO2 -->
					  <html:text name="datosGeneralesForm" styleId="apellido2" property="apellido2" size="20" maxlength="80" styleClass="<%=estiloCajaNombreApellidos %>" style='width:160px;' value="<%=apellido2 %>" ></html:text>
					</td>
				</tr>
				</table>
			</siga:ConjCampos>
		</td>
	</tr>

	<!-- FILA 3: Informacion Adicional -->
	<tr>
		<td id ="tdadicional" style="display:none">
			<siga:ConjCampos leyenda="censo.general.literal.informacionAdicional">
				<table class="tablaCampos" align="left">
					<tr>
						<!-- FECHA DE NACIMIENTO -->
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaNacimiento"/>&nbsp;
						</td>				
						<td>
							<siga:Fecha nombreCampo="fechaNacimiento" valorInicial="<%=fechaNacimiento%>"></siga:Fecha>
						</td>
					
						<!-- LUGAR NACIMIENTO -->
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.nacido"/>
						</td>	
						<td>			
							<html:text name="datosGeneralesForm"  property="lugarNacimiento"  styleId="lugarNacimiento" size="20" maxlength="100" styleClass="box" style='width:190px;' value="<%=nacido %>" ></html:text>
						</td>
					</tr>
					<tr>
						<!-- ESTADO CIVIL -->
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.estadoCivil"/>
						</td>				
						<td>
							<siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="<%=estiloCajaNombreApellidos %>" obligatorio="false" elementoSel="<%=estadoCivilSel %>" readonly="<%=readonlyComboNIFCIF %>"/>							
						</td>
					
						<!-- SEXO -->
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>&nbsp;(*)
						</td>				
						<td>
						<% 
							String ssexo = "";
							if (sexo.equals(ClsConstants.TIPO_SEXO_HOMBRE)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
							if (sexo.equals(ClsConstants.TIPO_SEXO_MUJER)) ssexo = UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer"); %>
							<!-- option select -->
							<html:select name="datosGeneralesForm" property="sexo" styleId="sexo" style = "null" styleClass = "<%=estiloCaja %>" value="<%=sexo %>"  readonly="<%=breadonly %>" >
							    <html:option value="0" >&nbsp;</html:option>
								<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE %>" ><siga:Idioma key="censo.sexo.hombre"/></html:option>
								<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER %>" ><siga:Idioma key="censo.sexo.mujer"/></html:option>
							</html:select>						
						</td>
					</tr>
					<tr>		
						<!-- TRATAMIENTO -->
						<td class="labelText" style="width:170px">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/>&nbsp;(*)
						</td>
						<td  style="width:200px">
							<siga:ComboBD nombre = "tratamiento" tipo="cmbTratamiento"  clase="<%=estiloCaja %>" obligatorio="true" readonly="<%=readonly %>" />						
						</td>
					
						<!-- IDIOMA -->
						<td class="labelText">
							<siga:Idioma key="censo.consultaDatosGenerales.literal.idiomacomunicaciones"/>&nbsp;(*)
						</td>
						<td>
							<siga:ComboBD nombre = "idioma" tipo="cmbIdiomaInstitucion" parametro="<%=institucionParam%>" clase="<%=estiloCaja %>" obligatorio="true" elementoSel="<%=idiomaSel %>"  readonly="<%=readonly %>" obligatorioSinTextoSeleccionar="true" />
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
		</td>
	</tr>
	<tr>
		<td id ="tddireccion" style="display:none">
			<siga:ConjCampos leyenda="censo.consultaDirecciones.cabecera">
				<table class="tablaCampos" align="left" >	

					<!-- COMBO DIRECCIONES -->
					<tr>
						<td class="labelText">
							Direcciones&nbsp;
						</td>
						<td colspan="5">
						  <html:select styleId="direcciones" styleClass="boxCombo"  property="idDireccion">
						  <bean:define id="direcciones" name="datosGeneralesForm" property = "direcciones" type="java.util.Collection" />
							    <html:optionsCollection name="direcciones" value="idDireccion" label="nombre" />
						  </html:select>
						</td>
					</tr>
					
					<!-- TIPO DIRECCION -->
					<tr>
						<td class="labelText"><siga:Idioma key="censo.datosDireccion.literal.tipoDireccion"/>&nbsp(*)</td>
						<td class="labelText" colspan="10">
							<table align="left" border="0" style="width:900px;">
				         		<tr>
				                	<td  class="labelText" align="left">
										<%	
										    String valorCheck="";
											
										    if ( (vTipos != null) && (vTipos.size() > 0) ) {
						                 		for (int i = 1; i <= vTipos.size(); i++) {
							                        String activarCheck=""; 
								                    CenTipoDireccionBean recurso = (CenTipoDireccionBean) vTipos.get(i-1);
						                 			Integer idTipoDireccion1     = (Integer) recurso.getIdTipoDireccion();
						                 			String descripcion         = (String) recurso.getDescripcion();
													
												  	if (modo.equals("editar")||modo.equals("ver")) {	
														valorCheck =(String)hTiposDir.get(recurso.getIdTipoDireccion());
														if (valorCheck.equals("S")){
														 	activarCheck="checked";
														}
												  	}
										 %>   		
											<%=UtilidadesString.mostrarDatoJSP(descripcion)%> 
					          				<input type=checkbox id="checkTipoDireccion_<%=idTipoDireccion1%>" value="<%=idTipoDireccion1%>" <%=activarCheck%> onclick="comprobarTelefonoAsterico()"/>
			 			               		&nbsp;&nbsp;
							                <%  }
						                 	}%>
						               </td>
					               <td class="labelText"></td>
			                 	</tr>
			             </table>	
					 </td>
					</tr>	
					
					<tr><td>&nbsp;</td></tr> 
					
					<!-- DIRECCION -->						
					<tr>
						<td class="labelText" id="direccionSinAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.direccion" />&nbsp;
						</td>
						<td class="ocultar" id="direccionConAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.direccion" />&nbsp;(*)
						</td>
						<td>
							<html:textarea cols="70" rows="2" name="datosGeneralesForm" property="domicilio" styleId="domicilio"
							onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="box"></html:textarea>
						</td>
						<td class="labelText" id="cpSinAsterisco" nowrap>
							<siga:Idioma key="censo.datosDireccion.literal.cp" />&nbsp;
						</td>
						<td class="ocultar" id="cpConAsterisco" nowrap><siga:Idioma
							key="censo.datosDireccion.literal.cp" />&nbsp;(*)
						</td>
						<td>
							<html:text name="datosGeneralesForm" styleId="codigoPostal" property="codigoPostal" maxlength="5" size="5" styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText" id="paisSinAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.pais2" />&nbsp;
						</td>
						<td class="ocultar" id="paisConAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.pais2" />&nbsp;
						</td>
						<td colspan = "3">							
							<html:select name="datosGeneralesForm" styleId="pais" styleClass="boxCombo"  property="pais" onchange="cargarProvincias(this);">
									<html:option value="">&nbsp;</html:option>
								    <html:optionsCollection name="datosGeneralesForm" property="paises" value="idNombre" label="nombre" />
							</html:select>
						</td>
					</tr>

					<tr>
						<td class="labelText" id="provinciaSinAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.provincia" />&nbsp;
						</td>
						<td class="ocultar" id="provinciaConAsterisco">
							<siga:Idioma key="censo.datosDireccion.literal.provincia" />&nbsp;(*)
						</td>
						<td id="provinciaEspanola">
							<html:select name="datosGeneralesForm" styleId="provincia" styleClass="boxCombo"  property="provincia" onchange="cargarPoblaciones(this);" style="wdth:150">
									<html:option value="">&nbsp;</html:option>
								    <html:optionsCollection name="datosGeneralesForm" property="provincias" value="idNombre" label="nombre" />
							</html:select>							
						</td>
						<td class="labelText" id="poblacionSinAsterisco">
							<siga:Idioma
							key="censo.datosDireccion.literal.poblacion" />&nbsp;
						</td>
						<td class="ocultar" id="poblacionConAsterisco">
							<siga:Idioma
							key="censo.datosDireccion.literal.poblacion" />&nbsp;(*)
						</td>
						<td id="poblacionEspanola">
							<html:select name="datosGeneralesForm" styleId="poblacion" styleClass="boxCombo"  property="poblacion" style="width:150">
									<html:option value="-1"><siga:Idioma key="general.combo.seleccionar"/></html:option>
							</html:select>							
						</td>
						<td class="ocultar" id="poblacionExtranjera">
							<html:text
							name="datosGeneralesForm" property="poblacionExt" styleId="poblacionExt" size="25"
							styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText" id="telefonoSinAsterisco"><siga:Idioma
							key="censo.datosDireccion.literal.telefono1" />&nbsp;</td>
						<td class="ocultar" id="telefonoConAsterisco"><siga:Idioma
							key="censo.datosDireccion.literal.telefono1" />&nbsp;(*)</td>
						<td>
							<html:text name="datosGeneralesForm"
							property="telefono1" styleId="telefono1" maxlength="20" size="10" styleClass="box"></html:text>
						</td>
						<td class="labelText">
							<siga:Idioma
							key="censo.datosDireccion.literal.telefono2" />&nbsp;
						</td>
						<td><html:text name="datosGeneralesForm"
							property="telefono2" styleId="telefono2" maxlength="20" size="10" styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.datosDireccion.literal.movil" />&nbsp;
						</td>
						<td ><html:text name="datosGeneralesForm" property="movil" styleId="movil"
							maxlength="20" size="10" styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText">
							<siga:Idioma
							key="censo.datosDireccion.literal.fax1" />&nbsp;
						</td>
						<td>
							<html:text name="datosGeneralesForm" property="fax1" styleId="fax1"
							maxlength="20" size="10" styleClass="box"></html:text>
						</td>
						<td class="labelText">
							<siga:Idioma
							key="censo.datosDireccion.literal.fax2" />&nbsp;
						</td>
						<td>
							<html:text name="datosGeneralesForm" property="fax2" styleId="fax2"
							maxlength="20" size="10" styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText"><siga:Idioma
							key="censo.datosDireccion.literal.correo" />&nbsp;
						</td>
						<td nowrap="nowrap"><html:text name="datosGeneralesForm" styleId="correoElectronico"
							property="correoElectronico" maxlength="100" size="50"
							styleClass="box"></html:text>
						</td>
		
						<td class="labelText"><siga:Idioma
							key="censo.datosDireccion.literal.paginaWeb" />&nbsp;
						</td>
						<td>
							<html:text name="datosGeneralesForm"
							property="paginaWeb" styleId="paginaWeb" maxlength="100" size="25" styleClass="box"></html:text>
						</td>
					</tr>

					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.datosDireccion.literal.preferente" />
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.preferente.mail" /><input type="checkbox"  name="preferenteMail" id="preferenteMail">&nbsp;&nbsp;&nbsp; 
							<siga:Idioma key="censo.preferente.correo" /><input type="checkbox"	name="preferenteCorreo" id="preferenteCorreo">&nbsp;&nbsp;&nbsp;
							<siga:Idioma key="censo.preferente.fax" /><input type="checkbox" name="preferenteFax" id="preferenteFax">&nbsp;&nbsp;&nbsp; 
							<siga:Idioma key="censo.preferente.sms" /><input type="checkbox" name="preferenteSms" id="preferenteSms">
						</td>
					</tr>
				</table>
			</siga:ConjCampos>	
	  	</td>
	</tr>
	<tr align="center">
		<td align="center" id="textomod" class="ocultar" style="display:none">
			(*)&nbsp;<siga:Idioma key="gratuita.modificardireccion.literal"/>
		</td>
	</tr>
</table>

</html:form>

<html:form action="/CEN_BusquedaCensoModal" method="POST" target="_self">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaCensoModal">
	<input type="hidden" name="clientes"	value="1">
	<input type="hidden" name="permitirAniadirNuevo" value="S">
	<html:hidden  name="busquedaCensoModalForm" property="multiple"/>
	<html:hidden  name="busquedaCensoModalForm" property="textoAlerta"/>
	<html:hidden  name="busquedaCensoModalForm" property="nombre"/>
	<html:hidden  name="busquedaCensoModalForm" property="apellido1"/>
	<html:hidden  name="busquedaCensoModalForm" property="apellido2"/>	
	<html:hidden  name="busquedaCensoModalForm" property="numeroColegiado"/>
	<html:hidden  name="busquedaCensoModalForm" property="idPersona"/>
	<html:hidden  name="busquedaCensoModalForm" property="idInstitucion"/>
	<html:hidden  name="busquedaCensoModalForm" property="nif"/>
	<html:hidden  name="busquedaCensoModalForm" property="idTipoIdentificacion"/>
	<html:hidden  name="busquedaCensoModalForm" property="direccion"/>
	<html:hidden  name="busquedaCensoModalForm" property="codPostal"/>
	<html:hidden  name="busquedaCensoModalForm" property="pais"/>
	<html:hidden  name="busquedaCensoModalForm" property="provincia"/>
	<html:hidden  name="busquedaCensoModalForm" property="poblacion"/>
	<html:hidden  name="busquedaCensoModalForm" property="poblacionExt"/>
	<html:hidden  name="busquedaCensoModalForm" property="telefono"/>
	<html:hidden  name="busquedaCensoModalForm" property="telefono2"/>
	<html:hidden  name="busquedaCensoModalForm" property="movil"/>
	<html:hidden  name="busquedaCensoModalForm" property="fax1" />
	<html:hidden  name="busquedaCensoModalForm" property="fax2"/>
	<html:hidden  name="busquedaCensoModalForm" property="mail" />
	<html:hidden  name="busquedaCensoModalForm" property="paginaWeb"/>
	<html:hidden  name="busquedaCensoModalForm" property="colegiadoen"/>	
	<html:hidden  name="busquedaCensoModalForm" property="preferente"/>
	<html:hidden  name="busquedaCensoModalForm" property="tipoDireccion"/>	
	<html:hidden  name="busquedaCensoModalForm" property="direcciones"/>	
	<html:hidden  name="busquedaCensoModalForm" property="idDireccion"/>
	<html:hidden  name="busquedaCensoModalForm" property="fechaNacimiento"/>
	<html:hidden  name="busquedaCensoModalForm" property="lugarNacimiento"/>
	<html:hidden  name="busquedaCensoModalForm" property="estadoCivil"/>
	<html:hidden  name="busquedaCensoModalForm" property="tratamiento"/>
	<html:hidden  name="busquedaCensoModalForm" property="idioma"/>
	<html:hidden  name="busquedaCensoModalForm" property="existeNIF"/>
	<input type="hidden" id="poblacionValue" value=""/>
</html:form>

<ajax:updateSelectFromField
	baseUrl="/SIGA/CEN_BusquedaCensoModal.do?modo=getAjaxComboDirecciones"
	source="idPersona" target="direcciones" parameters="idInstitucion={idInstitucion},idPersona={idPersona}"
	postFunction="postAccionComboDirecciones"
 />

<ajax:updateFieldFromSelect
	baseUrl="/SIGA/CEN_BusquedaCensoModal.do?modo=getAjaxDirecciones"
	source="direcciones" target="fax1,fax2,mail,paginaWeb,movil,telefono,telefono2,poblacionValue,provincia,pais,direccion,preferente,tipoDireccion,codPostal,poblacionExt,idDireccion"
	parameters="idInstitucion={idInstitucion},idPersona={idPersona},idDireccion={idDireccion}"  postFunction="postAccionDirecciones" 
/>

<ajax:updateFieldFromSelect
	baseUrl="/SIGA/CEN_BusquedaCensoModal.do?modo=getAjaxBusquedaNIF"
	source="numIdentificacion" target="existeNIF,multiple,textoAlerta,idPersona,colegiadoen,nColegiado,apellido1,apellido2,nombre,numIdentificacion,idTipoIdentificacion,idInstitucion,fax1,mail,telefono,poblacion,poblacionExt,provincia,pais,direccion,codPostal,sexo,fechaNacimiento,lugarNacimiento,estadoCivil,tratamiento,idioma"
	parameters="tipoIdentificacion={tipoIdentificacion},numIdentificacion={numIdentificacion},colegiadoen={colegiadoen},nColegiado={nColegiado},apellido1={apellido1},apellido2={apellido2},nombre={nombre},existeNIF={existeNIF}"
	postFunction="postAccionBusquedaNIF"
	preFunction="preAccionBusquedaNIF" 
/>

<!-- FIN: CAMPOS -->

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

	<siga:ConjBotonesAccion botones="<%=botonesAccion %>" modo="<%=modo%>" idBoton="1#2" idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';

		//Asociada al boton Restablecer
		function accionRestablecer() {			
			document.busquedaCensoModalForm.poblacionValue.value = "";
			restablecerComboPoblacion();
			limpiarDireccion();
			limpiarCliente();   
			
			//NUEVAS MEJORAS
			jQuery("#idButtonB").removeAttr("disabled");
			document.getElementById("tdadicional").style.display="none";
			document.getElementById("tddireccion").style.display="none";
			document.getElementById("textomod").style.display="none";
			if(document.getElementById('idButtonGuardar'))
				jQuery("#idButtonGuardar").attr("disabled","disabled");

			//Datos direccion
			jQuery("#domicilio").removeAttr("disabled");
		   	jQuery("#codigoPostal").removeAttr("disabled");
		   	jQuery("#pais").removeAttr("disabled");
		   	jQuery("#provincia").removeAttr("disabled");
		   	jQuery("#poblacion").removeAttr("disabled");
		   	jQuery("#poblacionFrame").contents().find("#poblacionSel").removeAttr("disabled");
		   	jQuery("#movil").removeAttr("disabled");
		   	jQuery("#telefono1").removeAttr("disabled");
		   	jQuery("#telefono2").removeAttr("disabled");
		   	jQuery("#fax1").removeAttr("disabled");
		   	jQuery("#fax2").removeAttr("disabled");
		   	jQuery("#correoElectronico").removeAttr("disabled");
		   	jQuery("#paginaWeb").removeAttr("disabled");
		   	jQuery("#poblacionExt").removeAttr("disabled");
		   	
			//Preferencia
			jQuery("#preferenteMail").removeAttr("disabled");
		   	jQuery("#preferenteCorreo").removeAttr("disabled");
		   	jQuery("#preferenteFax").removeAttr("disabled");
		   	jQuery("#preferenteSms").removeAttr("disabled");

			//Tipo Direccion
			jQuery("#checkTipoDireccion_1").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_2").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_3").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_4").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_5").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_6").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_7").removeAttr("disabled");
		   	jQuery("#checkTipoDireccion_8").removeAttr("disabled");
		}
	
	function validarFormulario() {
		var rc	= true;
		if (trim(document.getElementById("numIdentificacion").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.nIdentificacion"/>');
		   	return false;
		   	//generaNumOtro();
		}

		if (trim(document.getElementById("nombre").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>');
		   	return false;
		}
		if (trim(document.getElementById("apellido1").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.apellido1"/>');
		   	return false;
		}

		if (datosGeneralesForm.sexo.value=='0'){
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>');
		   	return false;
		}
		
		if (trim(document.getElementById("tratamiento").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/>');
		   	return false;
		}

		if (trim(document.getElementById("idioma").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.idiomacomunicaciones"/>');
		   	return false;
		}		
		
		var tipoIden = document.datosGeneralesForm.tipoIdentificacion.value;
		var dev = -1;
		if ((tipoIden == <%=tipoIdenNIF%>)) {
			dev = nif(datosGeneralesForm.numIdentificacion.value);
			if(dev == 1){ //DNI Correcto
				rc = true;
				
			}else{// DNI incorrecto
				if(document.datosGeneralesForm.numIdentificacion.disabled == true){ //Viene de busqueda
					datosGeneralesForm.tipoIdentificacion.value = "50";
					rc = true;

				}else{//Letrado escrito manualmente
					alert ("<siga:Idioma key="messages.nif.comprobacion.digitos.error"/>");
					rc = false;
				}
			}

		} else if((tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ) {
			rc=validaNIE(document.datosGeneralesForm.numIdentificacion.value);
		}

		return rc;	
	}

	//Asociada al boton Guardar
		function accionGuardar() {	

		if (validarFormulario()) {
				if (validarDireccion()) {
					document.busquedaCensoModalForm.numeroColegiado.value    =document.datosGeneralesForm.nColegiado.value;
					document.busquedaCensoModalForm.colegiadoen.value    =datosGeneralesForm.colegiadoen.value;
					document.busquedaCensoModalForm.apellido2.value    =document.datosGeneralesForm.apellido2.value;
					document.busquedaCensoModalForm.apellido1.value    =document.datosGeneralesForm.apellido1.value;
					document.busquedaCensoModalForm.nombre.value       =document.datosGeneralesForm.nombre.value;
					document.busquedaCensoModalForm.idInstitucion.value=document.datosGeneralesForm.idInstitucion.value;
					document.busquedaCensoModalForm.nif.value          =document.datosGeneralesForm.numIdentificacion.value;
					document.busquedaCensoModalForm.tratamiento.value  =document.datosGeneralesForm.tratamiento.value;
					document.busquedaCensoModalForm.idioma.value       =document.datosGeneralesForm.idioma.value;					
					document.busquedaCensoModalForm.direccion.value    =document.datosGeneralesForm.domicilio.value;
					document.busquedaCensoModalForm.codPostal.value    =document.datosGeneralesForm.codigoPostal.value;
					document.busquedaCensoModalForm.pais.value         =document.datosGeneralesForm.pais.value;
					document.busquedaCensoModalForm.provincia.value    =document.datosGeneralesForm.provincia.value;
					document.busquedaCensoModalForm.poblacion.value    =jQuery('#poblacion').val();
					document.busquedaCensoModalForm.poblacionExt.value =document.datosGeneralesForm.poblacionExt.value;
					document.busquedaCensoModalForm.telefono.value     =document.datosGeneralesForm.telefono1.value;
					document.busquedaCensoModalForm.telefono2.value    =document.datosGeneralesForm.telefono2.value;
					document.busquedaCensoModalForm.movil.value        =document.datosGeneralesForm.movil.value;
					document.busquedaCensoModalForm.fax1.value         =document.datosGeneralesForm.fax1.value;
					document.busquedaCensoModalForm.fax2.value         =document.datosGeneralesForm.fax2.value;
					document.busquedaCensoModalForm.mail.value         =document.datosGeneralesForm.correoElectronico.value;
					document.busquedaCensoModalForm.paginaWeb.value    =document.datosGeneralesForm.paginaWeb.value;
					document.busquedaCensoModalForm.direcciones.value  =document.datosGeneralesForm.direcciones.value;
					document.busquedaCensoModalForm.idDireccion.value  =document.datosGeneralesForm.idDireccion.value;


					//SI ES DE NUESTRO COLEGIO NO SE TOCA DIRECCIONES
					if(document.datosGeneralesForm.idDireccion.value == "-1" || document.datosGeneralesForm.idDireccion.value == "" || datosGeneralesForm.idInstitucion.value != "<%=idInstitucionActual%>"){
						//Rellenando preferencias
						var preferencia = "";
						if (document.datosGeneralesForm.preferenteMail.checked)
							preferencia += "E";
						if (document.datosGeneralesForm.preferenteCorreo.checked)
							preferencia += "C";
						if (document.datosGeneralesForm.preferenteFax.checked)
							preferencia += "F";
						if (document.datosGeneralesForm.preferenteSms.checked)
							preferencia += "S";
	
						//Rellenando tipoDireccion
						var tipoDir = "";
						if (document.getElementById("checkTipoDireccion_1").checked)
							tipoDir +="1,";
						if (document.getElementById("checkTipoDireccion_2").checked)
							tipoDir +="2,";
						if (document.getElementById("checkTipoDireccion_3").checked)
							tipoDir +="3,";
						if (document.getElementById("checkTipoDireccion_4").checked)
							tipoDir +="4,";
						if (document.getElementById("checkTipoDireccion_5").checked)
							tipoDir +="5,";
						if (document.getElementById("checkTipoDireccion_6").checked)
							tipoDir +="6,";
						if (document.getElementById("checkTipoDireccion_7").checked)
							tipoDir +="7,";
						if (document.getElementById("checkTipoDireccion_8").checked)
							tipoDir +="8";

						 //Se valida que se seleccione un tipoDireccion
						if (tipoDir == ""){
						     var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.tipoDireccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
			 				 alert (mensaje);
			 				 fin();
							 return false;
						}					
	
						document.busquedaCensoModalForm.preferente.value = preferencia;
						document.busquedaCensoModalForm.tipoDireccion.value = tipoDir;
					}else{
						if(document.datosGeneralesForm.idDireccion.value == null){
						     var mensaje = "Seleccione una direccion";
			 				 alert (mensaje);
			 				 fin();
							 return false;
						}
					}
					
					if(document.datosGeneralesForm.idPersona.value!= null && document.datosGeneralesForm.idPersona.value!=""){	
						document.busquedaCensoModalForm.idPersona.value    =document.datosGeneralesForm.idPersona.value;					
						document.busquedaCensoModalForm.modo.value = "insertarNoColegiadoArticulo27";
						document.busquedaCensoModalForm.submit();
						window.top.returnValue="MODIFICADO";
					}else{
						document.datosGeneralesForm.preferente.value = preferencia;
						document.datosGeneralesForm.idTipoDireccion.value = tipoDir;
						document.datosGeneralesForm.modo.value = "validarNoColegiadoArt27";
						document.datosGeneralesForm.submit();
					}
				}else{
					fin();
					return false;
				}			
			}else{
				fin();
				return false;
			}
		}
		
		function traspasaDatos(resultado){			
			document.busquedaCensoModalForm.existeNIF.value = resultado[0];
			postAccionBusquedaNIF();		
		}
		
		function traspasaDatosCliente(resultado){			
			top.cierraConParametros(resultado);
		}
	
		//funciones de direcciones
		function selPais(valor) {
		   if (valor!="" && valor!=idEspana) {
		   		document.getElementById("poblacion").value="";
		   		document.getElementById("provincia").value="";
				jQuery("#provincia").attr("disabled","disabled");
				document.getElementById("poblacionEspanola").className="ocultar";
				document.getElementById("poblacionExtranjera").className="";
	       } else {
		   		document.getElementById("poblacionExt").value="";
				jQuery("#provincia").removeAttr("disabled");
				document.getElementById("poblacionEspanola").className="";
				document.getElementById("poblacionExtranjera").className="ocultar";
	       }
	    }
		
		function selPaisInicio() {
			var p = document.getElementById("pais");
			selPais(p.value);
		}	    
		
		function comprobarTelefonoAsterico(){
			
			document.getElementById("direccionSinAsterisco").className="labelText";
			document.getElementById("direccionConAsterisco").className="ocultar";
			document.getElementById("cpSinAsterisco").className="labelText";
			document.getElementById("cpConAsterisco").className="ocultar";
			document.getElementById("provinciaSinAsterisco").className="labelText";
			document.getElementById("provinciaConAsterisco").className="ocultar";
			document.getElementById("poblacionSinAsterisco").className="labelText";
			document.getElementById("poblacionConAsterisco").className="ocultar";
			document.getElementById("paisSinAsterisco").className="labelText";
			document.getElementById("paisConAsterisco").className="ocultar";
	    } 
	
		 function validarDireccion() {

			//Solo se aplica la validacion cuando la direccion del colegiado no es propia del colegio o es una nueva direccion
		 	if(document.datosGeneralesForm.idDireccion.value == "-1" || document.datosGeneralesForm.idDireccion.value == "" || datosGeneralesForm.idInstitucion.value != "<%=idInstitucionActual%>"){

		 		//Validamos que haya algun campo metido y en ese caso pasamos las otras validadciones si no devolvemos true. Como no hemos metido idTipoDireccion no insertara
			 	if(trim(document.datosGeneralesForm.telefono1.value)!='' ||	trim(document.datosGeneralesForm.telefono2.value)!='' || trim(document.datosGeneralesForm.domicilio.value)!='' ||
			 			trim(document.datosGeneralesForm.codigoPostal.value)!='' ||	trim(document.datosGeneralesForm.poblacion.value)!='' || trim(document.datosGeneralesForm.provincia.value)!='' ||
			 			trim(document.datosGeneralesForm.poblacionExt.value)!='' ||	trim(document.datosGeneralesForm.movil.value)!='' || trim(document.datosGeneralesForm.fax1.value)!='' ||
			 			trim(document.datosGeneralesForm.fax2.value)!='' ||	document.datosGeneralesForm.preferenteFax.checked||document.datosGeneralesForm.preferenteSms.checked ||
			 			document.datosGeneralesForm.preferenteMail.checked||document.datosGeneralesForm.preferenteCorreo.checked){
			 	
				 	document.datosGeneralesForm.telefono1.value=eliminarBlancos(trim(document.datosGeneralesForm.telefono1.value));
				  	document.datosGeneralesForm.telefono2.value=eliminarBlancos(trim(document.datosGeneralesForm.telefono2.value));
					// Validamos los errores ///////////
					// Campos Tipo de Direccion obligatorio -> no se usa validacion Struts pq es multiple seleccion
					
					
		           	document.datosGeneralesForm.idTipoDireccion.value=<%=ClsConstants.TIPO_DIRECCION_PUBLICA%>;
				  
				   
					// VALIDAMOS QUE SOLO HAYAN INTRODUCIDO EN EL COMBO DE TIPO DE DIRECCION DIRECCION DE GUARDIA.
					// SI ES ASI SOLO REALIZAMOS LA VALIDACION DEL CAMPO TELEFONO 1. EN CASO CONTRARIO SE REALIZAN
					// EL RESTO DE VALIDACIONES TAMBIEN NECESARIAS PARA LOS OTROS TIPOS DE DIRECCION
											
					//VALIDACIONES GENERALES A TODOS LOS TIPOS DE DIRECCION SELECCIONADOS
				  
				   if (!validateDatosGeneralesDireccionForm(document.datosGeneralesForm)){
						return false;
					}
				   
				   if((document.datosGeneralesForm.preferenteMail.checked) &&  (trim(document.datosGeneralesForm.correoElectronico.value)=="")) {						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);	 								 
						 return false;
					}
	
					if((document.datosGeneralesForm.preferenteCorreo.checked) && (trim(document.datosGeneralesForm.domicilio.value)=="")) {					 
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);					 
						 return false;
					}
	
					// RGG 25/04/2005
					if((trim(document.datosGeneralesForm.domicilio.value)!="") &&(trim(document.datosGeneralesForm.codigoPostal.value)=="")) {						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.cp"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);	 				 
						 return false;
					}
	
					if((trim(document.datosGeneralesForm.domicilio.value)!="") && (trim(document.datosGeneralesForm.pais.value)==idEspana ||trim(document.datosGeneralesForm.pais.value)=="" ) && 
							(trim(document.datosGeneralesForm.provincia.value)=="")) {						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.provincia"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);	 				
						 return false;
					}
	
					if((trim(document.datosGeneralesForm.domicilio.value)!="") && (trim(document.datosGeneralesForm.pais.value)==idEspana ||trim(document.datosGeneralesForm.pais.value)=="") && 
							(jQuery('#poblacion').val()=="")) {						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);	 				
						 return false;
					}
						
					if((trim(document.datosGeneralesForm.domicilio.value)!="") && (trim(document.datosGeneralesForm.pais.value)!=idEspana && trim(document.datosGeneralesForm.pais.value)!="") && 
							(trim(document.datosGeneralesForm.poblacionExt.value)=="")) {						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.poblacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);	 				
						 return false;
					}
	
					if((document.datosGeneralesForm.preferenteFax.checked) &&  (trim(document.datosGeneralesForm.fax1.value)=="")) {
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 return false;
					}
	
	
					// jbd 04/06/2009 opcion sms
					if((document.datosGeneralesForm.preferenteSms.checked) && (trim(document.datosGeneralesForm.movil.value)=="")) {							 
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 return false;
					}
					return true;
				 
				 }else{
				 	return true;
				 }
		 		
		 	} else {
		 		return true; //No se hace validacion para las direcciones propias del colegio
		 	}
		 }	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	<script>
	var defaultOptionPoblacion = "";
		jQuery(document).ready(function(){
			jQuery('#poblacion option:gt(0)').remove();
			defaultOptionPoblacion = jQuery('#poblacion').html();
		});
		var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "messages.general.error"))%>';
		capturarEnter();
		
		function capturarEnter(){
			document.datosGeneralesForm.colegiadoen.onkeypress = submitConTeclaEnter;
			document.datosGeneralesForm.nColegiado.onkeypress = submitConTeclaEnter;
			document.datosGeneralesForm.apellido2.onkeypress = submitConTeclaEnter;
			document.datosGeneralesForm.apellido1.onkeypress = submitConTeclaEnter;
			document.datosGeneralesForm.nombre.onkeypress = submitConTeclaEnter;
		}
		
		function submitConTeclaEnter(){			
			var keycode;
			if (window.event)  {
				keycode = window.event.keyCode;
			}
			if (keycode == 13) {
				buscarDesignados();
				return false;
			}
		}
		
		function cargarProvincias(comboPais){
			var idPais = jQuery(comboPais).val();
			if (idPais == idEspana || idPais == ""){
				restablecerComboPoblacion();
				jQuery("#provincia").removeAttr("disabled");
// 				jQuery("#provincia").val("-1");
				jQuery("#poblacionEspanola").show();
				jQuery("#poblacionExtranjera").hide();
			} else {
				jQuery("#provincia").attr("disabled", "disabled");
				jQuery("#provincia").val("-1");
				jQuery("#poblacionEspanola").hide();
				jQuery("#poblacionExtranjera").show();
			}
		}
		
		var html_idPoblacion = "poblacion";
		var comboPoblacionHTML = '<select name="'+html_idPoblacion+'" class="boxCombo" id="'+html_idPoblacion+'" style="width:150"></select>';
		
		function cargarPoblaciones(comboProvincia){
			var idProvincia = jQuery("#provincia").val();			
			jQuery("#poblacionEspanola").show();
			jQuery("#poblacionExtranjera").hide();
			if (idProvincia != "-1" && idProvincia != "" && idProvincia != undefined){
				if (document.getElementById("direcciones").value != "-1" && document.busquedaCensoModalForm.poblacionValue.value != "" && document.busquedaCensoModalForm.poblacionValue.value != "-1" && document.busquedaCensoModalForm.poblacionValue.value != undefined){
					// Cambiamos el select por un input text con el nombre de la población seleccionada															
					jQuery.ajax({ 
						type: "POST",
						url: "/SIGA/CEN_Poblaciones.do?modo=getAjaxNombreDePoblacion",				
						data: "valorProvincia="+idProvincia+"&valorPoblacion="+document.busquedaCensoModalForm.poblacionValue.value,
						dataType: "json"}).done(function(json){
							var nombrePoblacion = json.nombrePoblacion;	
							
							document.getElementById("poblacionEspanola").innerHTML = "<input type='text' id='poblacion_display' disabled='disabled' value='"+nombrePoblacion+"'/><input type='hidden' name='poblacion' id='poblacion' value='"+document.busquedaCensoModalForm.poblacionValue.value+"'/>";
							if (nombrePoblacion == "Error B.D.")
								recargarPoblacionesDeProvincia(html_idPoblacion, idProvincia);
						}).fail(function( jqXHR, textStatus, errorThrown){
							recargarPoblacionesDeProvincia(html_idPoblacion, idProvincia);
						});
				} else {
					recargarPoblacionesDeProvincia(html_idPoblacion, idProvincia);
				}
			}
		}
		
		function recargarPoblacionesDeProvincia(html_idPoblacion, idProvincia){
			// Volvemos a poner el combo si lo hemos quitado					
			restablecerComboPoblacion();
			jQuery("#"+html_idPoblacion).attr("disabled","disabled");
			// Cargamos las poblaciones de la provincia
			jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/CEN_Poblaciones.do?modo=getAjaxPoblacionesDeProvincia",				
				data: "valorProvincia="+idProvincia,
				cache: "true",
				timeout: "5000",
				dataType: "html"}).done(function(data){							
					if (jQuery("#"+html_idPoblacion).length > 0){
						document.getElementById("poblacionEspanola").innerHTML = comboPoblacionHTML;
					}							
					jQuery("#"+html_idPoblacion).html(data);
					jQuery("#"+html_idPoblacion).val("-1");
					if (document.busquedaCensoModalForm.poblacionValue.value != "" && document.busquedaCensoModalForm.poblacionValue.value != "-1" && document.busquedaCensoModalForm.poblacionValue.value != undefined){
						jQuery("#"+html_idPoblacion).val(document.busquedaCensoModalForm.poblacionValue.value);
					}
				}).fail(function( jqXHR, textStatus, errorThrown){
					alert(mensajeGeneralError);
				}).always(function(){if (jQuery("#"+html_idPoblacion).val() == -1) jQuery("#"+html_idPoblacion).removeAttr("disabled");});
		}
		
		function restablecerComboPoblacion(){
			if (jQuery("#poblacion_display").length > 0){				
				document.getElementById("poblacionEspanola").innerHTML = comboPoblacionHTML;
				comboPoblaciones = jQuery("#"+html_idPoblacion);
				comboPoblaciones.html(defaultOptionPoblacion);
				comboPoblaciones.val("-1");
			}
		}
	</script>
</body>
</html>