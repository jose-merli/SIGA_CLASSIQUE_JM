<!-- altaNoColegiado.jsp -->
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
	String cuentaContable = ""; 
	String sexo = "";
	String idioma = "";
	String estadoCivil = "";
	String caracter = "";
	String tipoi = "";
	String fallecido = "";
	String checkFallecido="";
	String edad= "";
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
<style>
.ocultar {display:none}
</style>	
<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="datosGeneralesForm" staticJavascript="false" /> 
			<html:javascript formName="datosGeneralesDireccionForm" staticJavascript="false" /> 
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

		<script>
		
		
			
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
		
		
		
		</script>

	</head>



	<body onload="comprobarTelefonoAsterico();selPaisInicio();" class="tablaCentralCampos">

<%
			tipoIdentificacionSel.add(""+ClsConstants.TIPO_IDENTIFICACION_NIF);
%>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
	<td class="titulitosDatos" >
	<siga:Idioma key="censo.altaDatosGenerales.cabecera"/>
	</td>
	
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

			

	<!------------------------------->
	<!-- Tabla central principal -->
	<!------------------------------->
	<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
		<html:form  action="/CEN_DatosGenerales" method="POST" target="submitArea"  enctype="multipart/form-data">
			<html:hidden  name="datosGeneralesForm" property="modo"/>
			<html:hidden  name="datosGeneralesForm" property="idInstitucion"/>
			<html:hidden  name="datosGeneralesForm" property="idPersona"/>
			<html:hidden  name="datosGeneralesForm" property="accion"/>
			<html:hidden  name="datosGeneralesForm" property="tratamiento" value="1"/>
			<html:hidden name="datosGeneralesForm" property="motivo"/>
			<html:hidden name="datosGeneralesForm" property="abono" value="B" />
			<html:hidden name="datosGeneralesForm" property="cargo" value="B" />
			<html:hidden name="datosGeneralesForm" property="idTipoDireccion" value=""/>
			<html:hidden property="actionModal" value=""/>
			<html:hidden property="tipo" value="<%=sTipo%>"/>
	<tr>

		<!-- FILA 1: Datos Generales -->
		<td>
				<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
				<table  class="tablaCampos"  align="center" cellpadding="0" cellpadding="0">
				<tr>
					<!-- TIPO IDENTIFICACION -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.tipoIdentificacion"/>&nbsp;(*)
					</td>				
					<td>
					<% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="box" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" />						
					<% } else { 
						if (bResidente) { %>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="<%=estiloCajaNif%>" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>"  readonly="<%=readonlyComboNIFCIF%>"/>						
						<% } else {%>
							<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="boxConsulta" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" readonly="true"/>
						<% } %>
					<% } %>
					</td>
					<td> 
					
				 <% if ( formulario.getAccion().equalsIgnoreCase("nuevo")) { %>
				      <html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="<%=estiloCajaNif%>" value="<%=nIdentificacion %>"  onblur="formatearDocumento();"></html:text>		
				      <input type="button" name="idButton" value='<siga:Idioma key="censo.nif.letra.letranif" />' onclick="generarLetra();" style="align:right" class="button">		
				 <% }else{ %>
				     <% if(formulario.getAccion().equalsIgnoreCase("editar")){ %> 
				         <% if(!pintaComboSexo){ %>
							<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onblur="formatearDocumento();"></html:text>
				         <%}else{ %>
				            <%if (bResidente){%>
								<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="<%=estiloCajaNif%>" value="<%=nIdentificacion %>"  onblur="formatearDocumento();"></html:text>		
							<input type="button" name="idButton" value='<siga:Idioma key="censo.nif.letra.letranif" />' onclick="generarLetra();" style="align:right" class="button">
							<% }else{ %>
								<!-- Necesario para el validation.xml -->
								<html:text name="datosGeneralesForm" property="numIdentificacion" styleClass="boxConsulta" value="<%=nIdentificacion %>" />
							<% } %>
					    <% } %>
					<% }else{ %>
						<% if (formulario.getAccion().equalsIgnoreCase("editar")&& !bResidente){%>
							<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onblur="formatearDocumento();"></html:text>
						<% }else{%>
							<html:text name="datosGeneralesForm" property="numIdentificacion" size="20" styleClass="boxConsulta" value="<%=nIdentificacion %>" readonly="true" onblur="formatearDocumento();"></html:text>
						<% } %>
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
				<table class="tablaCampos" align="left" cellpadding="0" cellpadding="0" style="width:400px">
				<tr>
					<!-- IDIOMA -->
					<td class="labelText">
						<siga:Idioma key="censo.consultaDatosGenerales.literal.idioma"/>&nbsp;(*)
					</td>
					<td>
						<siga:ComboBD nombre = "idioma" tipo="cmbIdioma" clase="<%=estiloCaja %>" obligatorio="true" elementoSel="<%=idiomaSel %>"  readonly="<%=readonly %>" obligatorioSinTextoSeleccionar="true" />
					</td>
				</tr>
					
					
	
				
				</table>
				</siga:ConjCampos>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	

<tr>
	<td>
	
		<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" >
			<tr>
				<td>
	
					<siga:ConjCampos leyenda="censo.consultaDirecciones.cabecera">
						<table class="tablaCampos" align="left" cellpadding="0" cellpadding="0" style="width:400px">	
							
							<tr>
								<td class="labelText" width="180px" id="direccionSinAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.direccion" />&nbsp;</td>
								<td class="ocultar" width="180px" id="direccionConAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.direccion" />&nbsp;(*)</td>
								<td><html:textarea cols="70" rows="2"
									name="datosGeneralesForm" property="domicilio"
									onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)"
									styleClass="box"></html:textarea></td>
				
				
								<td class="labelText" width="180px" id="cpSinAsterisco" nowrap><siga:Idioma
									key="censo.datosDireccion.literal.cp" />&nbsp;</td>
								<td class="ocultar" width="180px" id="cpConAsterisco" nowrap><siga:Idioma
									key="censo.datosDireccion.literal.cp" />&nbsp;(*)</td>
								<td><html:text name="datosGeneralesForm"
									property="codigoPostal" maxlength="5" size="5" styleClass="box"></html:text></td>
				
				
							</tr>
		
							<tr>
								<td class="labelText" width="180px" id="paisSinAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.pais2" />&nbsp;</td>
								<td class="ocultar" width="180px" id="paisConAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.pais2" />&nbsp;</td>
								<td colspan = "3"><siga:ComboBD nombre="pais" tipo="pais" clase="boxCombo"
									obligatorio="false" accion="selPais(this.value);" /></td>
				
							</tr>
		
							<tr>
								<td class="labelText" id="provinciaSinAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.provincia" />&nbsp;</td>
								<td class="ocultar" id="provinciaConAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.provincia" />&nbsp;(*)</td>
								<td id="provinciaEspanola"><siga:ComboBD nombre="provincia"
									tipo="provincia" clase="boxCombo" obligatorio="false"
									accion="Hijo:poblacion" /></td>
				
								<td class="labelText" id="poblacionSinAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.poblacion" />&nbsp;</td>
								<td class="ocultar" id="poblacionConAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.poblacion" />&nbsp;(*)</td>
								<td id="poblacionEspanola"><siga:ComboBD nombre="poblacion"
									tipo="poblacion" clase="boxCombo" hijo="t" /></td>
								<td class="ocultar" id="poblacionExtranjera"><html:text
									name="datosGeneralesForm" property="poblacionExt" size="30"
									styleClass="box"></html:text></td>
							</tr>
		
							<tr>
								<td class="labelText" id="telefonoSinAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.telefono1" />&nbsp;</td>
								<td class="ocultar" id="telefonoConAsterisco"><siga:Idioma
									key="censo.datosDireccion.literal.telefono1" />&nbsp;(*)</td>
								<td><html:text name="datosGeneralesForm"
									property="telefono1" maxlength="20" size="10" styleClass="box"></html:text>
									</td>
				
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.telefono2" />&nbsp;</td>
								<td><html:text name="datosGeneralesForm"
									property="telefono2" maxlength="20" size="10" styleClass="box"></html:text></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.movil" />&nbsp;</td>
								<td ><html:text name="datosGeneralesForm" property="movil"
									maxlength="20" size="10" styleClass="box"></html:text></td>
								<td></td>
								<td></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.fax1" />&nbsp;</td>
								<td><html:text name="datosGeneralesForm" property="fax1"
									maxlength="20" size="10" styleClass="box"></html:text></td>
				
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.fax2" />&nbsp;</td>
								<td><html:text name="datosGeneralesForm" property="fax2"
									maxlength="20" size="10" styleClass="box"></html:text></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.correo" />&nbsp;</td>
								<td nowrap="nowrap"><html:text name="datosGeneralesForm"
									property="correoElectronico" maxlength="100" size="50"
									styleClass="box"></html:text></td>
				
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.paginaWeb" />&nbsp;</td>
								<td><html:text name="datosGeneralesForm"
									property="paginaWeb" maxlength="100" size="25" styleClass="box"></html:text></td>
							</tr>
		
							<tr>
								<td class="labelText"><siga:Idioma
									key="censo.datosDireccion.literal.preferente" /></td>
								<td class="labelText"><siga:Idioma
									key="censo.preferente.mail" /><input type="checkbox"
									name="preferenteMail">&nbsp;&nbsp;&nbsp; <siga:Idioma
									key="censo.preferente.correo" /><input type="checkbox"
									name="preferenteCorreo">&nbsp;&nbsp;&nbsp; <siga:Idioma
									key="censo.preferente.fax" /><input type="checkbox"
									name="preferenteFax">&nbsp;&nbsp;&nbsp; <siga:Idioma
									key="censo.preferente.sms" /><input type="checkbox"
									name="preferenteSms"></td>
								<td></td>
								<td></td>
							</tr>
		
	
						</table>
		
					</siga:ConjCampos>	
				</td>
			</tr>
		
		</table>
		
	</td>
</tr>
</table>

	

</html:form>





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

	<siga:ConjBotonesAccion botones="<%=botonesAccion %>" modo="<%=modo%>" idBoton="3"  idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';
		//Asociada al boton Restablecer
		function accionRestablecer() {
			document.forms[0].reset();	
		}

	function validarFormulario() {
		if (trim(document.getElementById("numIdentificacion").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.tipoIdentificacion"/>');
		   	return false;
		}
		if (trim(document.getElementById("tratamiento").value)=="") {
			alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/>');
		   	return false;
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
				
		var rc	= true;
		var tipoIden = document.datosGeneralesForm.tipoIdentificacion.value;
		if ((tipoIden == <%=tipoIdenNIF%>)) {
			rc = validarNIFCIF(tipoIden, document.datosGeneralesForm.numIdentificacion.value);
		} else if((tipoIden == "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>") ) {
			rc=validaNIE(document.datosGeneralesForm.numIdentificacion.value);
		}
		return rc;	
	}


		//Asociada al boton Guardar
		function accionGuardar() {
		
			sub();
			if (validarFormulario() && validarDireccion()) {
				document.datosGeneralesForm.modo.value = "validarNoColegiado";
				document.datosGeneralesForm.submit();
				
			}else{
				fin();
				return false;
			}
		}
		function traspasaDatos(resultado){
			
			document.datosGeneralesForm.nombre.value=resultado[4];
			document.datosGeneralesForm.apellido1.value=resultado[5];
			document.datosGeneralesForm.apellido2.value=resultado[6];
		
		}
		function traspasaDatosCliente(resultado){
			top.cierraConParametros(resultado);
		}
		
		//funciones de direcciones
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
		
		function selPaisInicio() {
			var p = document.getElementById("pais");
			selPais(p.value);
		}	    
		function comprobarTelefonoAsterico() 
		{
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
		 	
		 	//Validamos que haya algun campo metido y en ese caso pasamos las otras validadciones
		 	//si no devolvemos true. Como no hemos metido idTipoDireccion no insertara
		 	
		 	if(trim(document.datosGeneralesForm.telefono1.value)!='' ||
		 	trim(document.datosGeneralesForm.telefono2.value)!='' ||
		 	trim(document.datosGeneralesForm.domicilio.value)!='' ||
		 	trim(document.datosGeneralesForm.codigoPostal.value)!='' ||
		 	trim(document.datosGeneralesForm.poblacion.value)!='' ||
		 	trim(document.datosGeneralesForm.provincia.value)!='' ||
		 	trim(document.datosGeneralesForm.poblacionExt.value)!='' ||
		 	trim(document.datosGeneralesForm.movil.value)!='' ||
		 	trim(document.datosGeneralesForm.fax1.value)!='' ||
		 	trim(document.datosGeneralesForm.fax2.value)!='' ||
		 	document.datosGeneralesForm.preferenteFax.checked||document.datosGeneralesForm.preferenteSms.checked
		 	||document.datosGeneralesForm.preferenteMail.checked||document.datosGeneralesForm.preferenteCorreo.checked){
		 	
		 	
		 	
		 	
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
			   
			   if((document.datosGeneralesForm.preferenteMail.checked) && 
						 (trim(document.datosGeneralesForm.correoElectronico.value)=="")) {
						
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.correo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
		 								 
						 return false;
					}
					if((document.datosGeneralesForm.preferenteCorreo.checked) && 
						 (trim(document.datosGeneralesForm.domicilio.value)=="")) {
						 
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.direccion"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 
						 return false;
					}
					// RGG 25/04/2005
					if((trim(document.datosGeneralesForm.domicilio.value)!="") &&
						(trim(document.datosGeneralesForm.codigoPostal.value)=="")) {
						
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
						(trim(document.datosGeneralesForm.poblacion.value)=="")) {
						
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
					if((document.datosGeneralesForm.preferenteFax.checked) && 
						 (trim(document.datosGeneralesForm.fax1.value)=="")) {
		 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.fax1"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		 				 alert (mensaje);
						 return false;
					}
					// jbd 04/06/2009 opcion sms
					if((document.datosGeneralesForm.preferenteSms.checked) && 
							 (trim(document.datosGeneralesForm.movil.value)=="")) {
							 
			 				 var mensaje = "<siga:Idioma key="censo.datosDireccion.literal.movil"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
			 				 alert (mensaje);
						 return false;
						}
					return true;
			 
			 }else{
			 	return true;
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