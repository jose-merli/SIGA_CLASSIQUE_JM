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
    String fechaEstadoColegial = "";
    int tipoIdenNIF = ClsConstants.TIPO_IDENTIFICACION_NIF;
    int tipoIdenCIF = ClsConstants.TIPO_IDENTIFICACION_CIF;
	int tipoIdenNIE = ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE;
	
	ArrayList selTipoIdentificacion = new ArrayList();
	selTipoIdentificacion.add (""+tipoIdenNIF);

	String pais="";
	ArrayList idPais=new ArrayList();

	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String [] modalidadParam = new String[1];
	modalidadParam[0] = user.getLocation();
	
	String estiloBox = "box";
	String estiloCombo = "boxCombo";
	String readonly = "false";
	boolean residente = true;

	
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

	function refrescarLocal() 
	{		
		document.SolicitudIncorporacionForm.modo.value="abrirAvanzada";
		document.SolicitudIncorporacionForm.target="mainWorkArea";
		document.SolicitudIncorporacionForm.submit();
	}

	function cargaPais(valor) {      
	   if (valor!=null && valor!="" && valor!=<%=ClsConstants.ID_PAIS_ESPANA%>) {
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
		
	function datosValidos(){
		var errores = "";
	
		if(validaNumeroIdentificacion()){
	
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
			if (document.SolicitudIncorporacionForm.pais.value == "" || document.SolicitudIncorporacionForm.pais.value == "<%=ClsConstants.ID_PAIS_ESPANA%>") {
		   		if (document.SolicitudIncorporacionForm.provincia.value == "") {
		   			errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.provincia'/>" + '\n';
		       	}
		   		if (document.SolicitudIncorporacionForm.poblacion.value == "") {
		   			errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.poblacion'/>" + '\n';
		       	}
		    } else {
		   		if (document.SolicitudIncorporacionForm.poblacionExt.value == "") {
		   			errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.poblacion'/>" + '\n';
		       	}
			}
			if(document.SolicitudIncorporacionForm.domicilio.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.domicilio'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.CP.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.codigoPostal'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.telefono1.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.telefono1'/>" + '\n';
			}
			if(document.SolicitudIncorporacionForm.mail.value==""){
				errores += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.email'/>" + '\n';
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
		// Solo se genera el NIF o CIF de la persona
		if((SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")||
			(SolicitudIncorporacionForm.tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>")){
			document.getElementById("idButtonNif").style.visibility="visible";
		}	else{
			document.getElementById("idButtonNif").style.visibility="hidden";
		}
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
			alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
		}
		return valido;
	}

	function obtenerLetra(){
		generarLetra();
		/*if (!generarLetra()){
			validaNumeroIdentificacion
		}*/
	}

	function generarLetra() {
		var numId = SolicitudIncorporacionForm.NIFCIF.value;
		var tipoIdentificacion = SolicitudIncorporacionForm.tipoIdentificacion.value;
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
				 	SolicitudIncorporacionForm.NIFCIF.value =numId+letra;
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
				var dnie = SolicitudIncorporacionForm.NIFCIF.value;
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
					SolicitudIncorporacionForm.NIFCIF.value = numero;
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
			
</script>
			
</head>

<body >

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->


	<html:form action = "/SIN_SolicitudIncorporacion.do" focus = "tipoSolicitud" >

	<html:hidden property = "modo" value = ""/>

	<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">

	<table width="100%" border="0" >

		<html:hidden property = "editarIdSolicitud" value = ""/>
		<html:hidden property = "continuarAprobacion" value = ""/>
		<html:hidden property = "continuarInsercionColegiado" value = ""/>
		<html:hidden property = "numeroColegiado" value = ""/>


		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.solicitudDe"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoSolicitud" tipo="solicitud" clase="boxCombo" obligatorio="true"/></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.tipoColegiacion"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoColegiacion" ancho="100" tipo="colegiacion" clase="boxCombo" obligatorio="true"/></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.documentacion"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre = "tipoModalidadDocumentacion" tipo="modalidadDocumentacion" clase="boxCombo" obligatorio="true" parametro="<%=modalidadParam%>"/></td>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacion"/></td>
			<td>
				<siga:Fecha nombreCampo="fechaEstadoColegial"></siga:Fecha>
				<a href='javascript://'onClick="return showCalendarGeneral(fechaEstadoColegial);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
			</td>

			
			<td class="labelText"><siga:Idioma key="censo.consultaDatosColegiacion.literal.residente"/></td>
			<td><input type="checkbox" name="residente" checked></td>
		
			<td class="labelText" width="19%"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaSolicitud"/></td>
			<td><input value="<%=fechaSol%>" type="text" name="fechaSolicitud" class="boxConsulta" readonly="true"></td>

		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" width="10%"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif"/>&nbsp;(*)</td>
				<td>
					<siga:ComboBD nombre = "tipoIdentificacion" tipo="identificacionSolicitud"  ancho="80" clase="<%=estiloCombo%>" obligatorio="true" accion="comprobarTipoIdent();"/>
					<html:text property="NIFCIF" value="" styleClass="box" size="8" maxlength="20"></html:text>
					<img id="idButtonNif" src="<%=app%>/html/imagenes/comprobar.gif" border="0" onclick="obtenerLetra();" style="cursor:hand;align:left" style="display:inline;visibility: hidden;">
				</td>

			
			<td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>&nbsp;(*)
			</td>
			<td>
			<html:select name="SolicitudIncorporacionForm" property="sexo" style = "null"  styleClass = "box" >
		        <html:option value="0" >&nbsp;</html:option>
				<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE%>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
				<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER%>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
			</html:select>	
			</td>
			
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tratamiento"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre="tipoDon" tipo="tratamiento" clase="<%=estiloCombo%>" obligatorio="true"/></td>
		</tr>
		<tr>
		
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>&nbsp;(*)</td>
			<td><html:text property="nombre" value="" style="width:180" maxlength="120" styleClass="<%=estiloBox%>"   ></html:text></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido1"/>&nbsp;(*)</td>
			<td><html:text property="apellido1"  value="" style="width:180" maxlength="100" styleClass="<%=estiloBox%>"  ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/></td>
			<td><html:text property="apellido2"  value="" style="width:180" maxlength="100" styleClass="<%=estiloBox%>"   ></html:text></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaNacimiento"/>&nbsp;(*)</td>
			<td>
				<siga:Fecha nombreCampo="fechaNacimiento" valorInicial=""></siga:Fecha>
				<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimiento);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
			</td>

			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.naturalDe"/></td>
			<td><html:text property="natural" style="width:180" maxlength="100" styleClass="<%=estiloBox%>" value=""></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.estadoCivil"/></td>
			<td><siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="<%=estiloCombo%>"/></td>
		</tr>
		<tr>
			
		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.datosDireccion.literal.pais2"/></td>
			<td colspan="2"><siga:ComboBD nombre="pais" tipo="pais" ancho="300" clase="<%=estiloCombo%>" obligatorio="false" accion="cargaPais(this.value);"/></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>&nbsp;(*)</td>
			<td colspan="2"><siga:ComboBD nombre="provincia" tipo="provincia" clase="<%=estiloCombo%>" obligatorio="true" accion="Hijo:poblacion"/></td>

		</tr>
		<tr>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>&nbsp;(*)</td>
			<td id="poblacionEspanola" colspan="2">
				<siga:ComboBD nombre="poblacion" tipo="poblacion" clase="<%=estiloCombo%>" obligatorio="true" hijo="t" ancho="300"/>
			</td> 
			
			<td class="ocultar" colspan="2" id="poblacionExtranjera">
				<html:text property="poblacionExt" style="width:300" maxlength="100" styleClass="<%=estiloBox%>"></html:text>
			</td>
				
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.domicilio"/>&nbsp;(*)</td>
			<td><html:text property="domicilio" value="" size="30" maxlength="100" styleClass="<%=estiloBox%>"></html:text></td>
			
			<td class="labelText" >CP&nbsp;(*)
			<html:text property="CP" value="" styleClass="<%=estiloBox%>" size="5" maxlength="5"></html:text></td>
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono1"/>&nbsp;(*)</td>
			<td><html:text property="telefono1" maxlength="20" styleClass="<%=estiloBox%>" value="" ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono2"/></td>
			<td><html:text property="telefono2" maxlength="20" styleClass="<%=estiloBox%>" value=""></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono3"/></td>
			<td><html:text property="telefono3" maxlength="20" styleClass="<%=estiloBox%>" value=""></html:text></td>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax1"/></td>
			<td><html:text property="fax1" maxlength="20" styleClass="<%=estiloBox%>"></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax2"/></td>
			<td><html:text property="fax2" maxlength="20" styleClass="<%=estiloBox%>"></html:text></td>
					
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>&nbsp;(*)</td>
			<td><html:text property="mail" value="" maxlength="100" styleClass="<%=estiloBox%>"></html:text></td>
		</tr>
		
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">

	</table>
	
	</siga:ConjCampos>
	

	<siga:ConjCampos>
		<table>
			<tr>

				<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.observaciones"/></td>
				<td><textarea cols="120" rows="2" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" name="observaciones" style="overflow:hidden" class="box" ></textarea></td>

			</tr>
		</table>
	</siga:ConjCampos>
	<!-- TABLA -->
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
				cargaPais("");
				document.SolicitudIncorporacionForm.provincia.onchange();
			}
		}

		
		//Asociada al boton Guardar -->
	function accionGuardar(){
			sub();
			if(datosValidos()){
				document.SolicitudIncorporacionForm.modo.value = "insertar";
				document.SolicitudIncorporacionForm.target = "submitArea";
				document.SolicitudIncorporacionForm.submit();
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