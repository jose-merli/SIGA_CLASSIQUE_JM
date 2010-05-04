<!-- SolicitudIncorporacionDatos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="utils.system"%>
<%@ page import="com.siga.censo.form.SolicitudIncorporacionForm" %>

<%
	String app=request.getContextPath();
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
		ssexo = com.siga.Utilidades.UtilidadesString.getMensajeIdioma(user, "censo.sexo.hombre");
	else 
		ssexo = com.siga.Utilidades.UtilidadesString.getMensajeIdioma(user, "censo.sexo.mujer");

	String altoTabla = "80%";		
	if (editar!=null && editar.equalsIgnoreCase("true"))
		altoTabla = "75%";
	
	ArrayList selPais = new ArrayList();
	ArrayList selProvincia = new ArrayList();
	ArrayList selPoblacion = new ArrayList();
	ArrayList selIdent = new ArrayList();
	ArrayList selTratamiento = new ArrayList();
	ArrayList selEstadoCiv = new ArrayList();
	selPais.add(datosPersonales.getIdPais());
	boolean esEspana=datosPersonales.getIdPais().equalsIgnoreCase(ClsConstants.ID_PAIS_ESPANA)||datosPersonales.getIdPais().equalsIgnoreCase("");
	if (esEspana){	
		selProvincia.add(datosPersonales.getIdProvincia());
		selPoblacion.add(datosPersonales.getIdPoblacion());
	}
	selEstadoCiv.add(datosPersonales.getIdEstadoCivil());
	selTratamiento.add(datosPersonales.getIdTratamiento());
	selIdent.add(datosPersonales.getIdTipoIdentificacion());
				
	String estiloBox = "box";
	String estiloBoxNumber = "boxNumber";
	String estiloCombo = "box";
	boolean readonly = false;
	String sreadonly = "false";
	boolean scheck = false;
	if (editar.equalsIgnoreCase("false")) {
		estiloBox = "boxConsulta";
		estiloBoxNumber = "boxConsultaNumber";
		estiloCombo = "boxConsulta";
		readonly = true;
		sreadonly = "true";
		scheck = true;
	} else {
		estiloBox = "box";
		estiloBoxNumber = "boxNumber";
		estiloCombo = "box";
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
	
	ArrayList listaBancos = new ArrayList();
	listaBancos.add(String.valueOf(cbo_Codigo));
	String fechaBaja = "";
	
	if ((titular==null)||(titular.equalsIgnoreCase(""))){
		titular=datosPersonales.getNombre()+" "+datosPersonales.getApellido1()+" "+datosPersonales.getApellido2();
	}
	
	ArrayList selColegiacion = new ArrayList();
	selColegiacion.add(datosPersonales.getIdTipoColegiacion());
	ArrayList selSolicitud = new ArrayList();
	selSolicitud.add(datosPersonales.getIdTipoSolicitud());
	
	boolean residente = datosPersonales.getResidente();


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

	<style>
		.ocultar {display:none}
	</style>	


<html>

<head>
	<html:javascript formName="SolicitudIncorporacionForm" staticJavascript="false" />  
	
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="censo.solicitudIncorporacion.cabecera3" 
							 localizacion="censo.solicitudIncorporacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">
		function editarNColegiado(){
			var numeroCol = document.getElementById("numColBox");
			var botNumeroCol = document.getElementById("botonNCol"); 
			if (numeroCol.disabled){
				numeroCol.className="box";
				numeroCol.disabled=false;
				botNumeroCol.style.visibility ="hidden";
			}
		}
		
		function restablecerNColegiado(){
			var numeroCol = document.getElementById("numColBox");
			var botNumeroCol = document.getElementById("botonNCol");
			<%if(nColegiado==null || nColegiado.equalsIgnoreCase("")){%>
				numeroCol.className = "boxDisabled";
				numeroCol.disabled = true;
				botNumeroCol.style.visibility ="visible";
			<%}else{%>
				numeroCol.className = "box";
				numeroCol.value=<%=nColegiado%>;
				botNumeroCol.style.visibility ="hidden";
			<%}%>
		}



		function validaAbonoSJCS() {
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
				//if(!confirm("Es necesario modificar la documentaci�n. Se perderan los documentos actuales.")){
				if(!confirm("Ha realizado cambios que afectan a la documentaci�n requerida.\nAl guardar se cargar� la nueva lista de documentos.")){
					for (i=0; i<selMod.options.length; i++) {
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

		function cargaPais(valor) {      
			<%if(!readonly){%>
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
					recargar();
		       }
			<%}%>
		}
	 
		function recargar(){
			<%if (esEspana){ %>
			var tmp1 = document.getElementsByName("provincia");
			if (tmp1){
				var tmp2 = tmp1[0];
				if (tmp2) {
					tmp2.onchange();
				} 
			}
			<%}%>
		}
	</script>

</head>

<body  class="tablaCentralCampos" onLoad="cargaPais(<%=datosPersonales.getIdPais() %>);cargarChecksCuenta();ajusteAlto('documentos');comprobarTipoIdent();">

	<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST">
	
	<table align="center" width="100%">
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
		<center>
		<table>
			<tr>
			<td>
				<table>
				<tr>
				<td class="labelText" style="font-size:1.1em;"><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.estado"/></td>
				<%if (readonly || datosPersonales.getIdEstado().intValue()==ClsConstants.ESTADO_SOLICITUD_APROBADA) {%>
					<td class="labelTextValor" >
						<%=UtilidadesString.mostrarDatoJSP(estadoSolicitud)%>
						<html:hidden property="editarEstadoSolicitud" value="<%=datosPersonales.getIdEstado().toString()%>"  />
					</td>
				<%} else {%>
					<td ><siga:ComboBD nombre = "editarEstadoSolicitud" tipo="estadoSolicitud" clase="<%=estiloCombo%>" elementoSel="<%=idEstadoSolicitud%>" obligatorioSinTextoSeleccionar="true" /></td>
				<%}%>
				
				<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.fechaEstado"/></td>
				<td><html:text property="fechaSolicitud" styleClass="boxConsulta"  readOnly="true" value="<%=datosPersonales.getFechaEstado()%>"></html:text></td>
				<tr>
				<td colspan="2"></td>
				<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaSolicitud"/></td>
				<td><html:text property="fechaSolicitud" styleClass="boxConsulta"  readOnly="true" value="<%=datosPersonales.getFechaSolicitud()%>"></html:text></td>
				
				<tr>
				
				</tr>
				</table>
				<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.observaciones"/></td>
				<%if(readonly){ %>
					<td><textarea cols="120" rows="2" name="observaciones" style="overflow:hidden" class="boxConsulta" readonly><%=datosPersonales.getObservaciones()%></textarea></td>
				<%}else{%>
					<td><textarea cols="120" rows="2" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" name="observaciones" style="overflow:hidden" class="box" ><%=datosPersonales.getObservaciones()%></textarea></td>
				<%}%>
			</td>
			</tr>
		</table>
		</center>
	<!-- TABLA -->
	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="censo.SolicitudIncorporacionDatos.titulo">

	<table width="100%" border="0" >

		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "editarIdSolicitud" value = "<%=datosPersonales.getIdSolicitud().toString()%>"/>
		<html:hidden property = "continuarAprobacion" value = ""/>
		<html:hidden property = "continuarInsercionColegiado" value = ""/>


		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.solicitudDe"/>&nbsp;(*)</td>
			<!-- <td width="27%"><siga:ComboBD nombre = "tipoSolicitud" tipo="solicitud" clase="boxCombo" obligatorio="true"/></td> -->
			<%if(readonly){ %>
				<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(tipoSolicitud)%></td>
			<%}else{%>
				<td><siga:ComboBD nombre = "tipoSolicitud" tipo="solicitud" clase="boxCombo" elementoSel="<%=selSolicitud%>" obligatorio="true"/></td>
			<%}%>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.tipoColegiacion"/>&nbsp;(*)</td>
			<%if(readonly){ %>
				<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(tipoColegiacion)%></td>
			<%}else{%>
				<td><siga:ComboBD nombre = "tipoColegiacion" ancho="100" tipo="colegiacion" clase="boxCombo" elementoSel="<%=selColegiacion%>" obligatorio="true"/></td>
			<%}%>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.documentacion"/></td>
			<%if(readonly){ %>
				<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(modalidadDocumentacion)%></td>
			<%}else{%>
				<td><siga:ComboBD nombre = "tipoModalidadDocumentacion" tipo="modalidadDocumentacion" clase="boxCombo" obligatorio="true" elementoSel="<%=modalidadSel%>" parametro="<%=modalidadParam%>"/></td>
			<%}%>
		</tr>
		<tr>
			<td class="labelText"><siga:Idioma key="censo.busquedaClientesAvanzada.literal.fechaIncorporacion"/></td>
			<%if(readonly){%>
				<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaEstadoColegial())%></td>
			<%}else{%>
				<td>
					<siga:Fecha nombreCampo="fechaEstadoColegial" valorInicial="<%=datosPersonales.getFechaEstadoColegial() %>"></siga:Fecha>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaEstadoColegial);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
				</td>
			<%}%>
			
			<td class="labelText"><siga:Idioma key="censo.consultaDatosColegiacion.literal.residente"/></td>
			<td><html:checkbox property="residente" disabled="<%=readonly%>"/></td>
		
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.nColegiado"/></td>
			<td>
			<%if(!readonly && (nColegiado==null || nColegiado.equalsIgnoreCase(""))){%>
				<html:text styleId="numColBox" property="numeroColegiado" style="width:100" maxlength="20" styleClass="boxDisabled" disabled="true"></html:text>
				<img id="botonNCol" src="<%=app%>/html/imagenes/candado.gif" border="0" onclick="editarNColegiado()" style="cursor:hand;align:left" style="display:inline;" title="Desbloquea el N�mero de Colegiado para hacerlo editable">
			<%}else{%>
				<html:text styleId="numColBox" property="numeroColegiado" style="width:100" maxlength="20" styleClass="<%=estiloBox%>" value="<%=nColegiado%>"  readOnly="<%=readonly%>" ></html:text>
				<img id="botonNCol" src="<%=app%>/html/imagenes/candado.gif" border="0" onclick="editarNColegiado()" style="cursor:hand;align:left" style="visibility:hidden;display:inline;" title="Desbloquea el N�mero de Colegiado para hacerlo editable">
			<%}%>
			</td>

		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" width="10%"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nifcif"/>&nbsp;(*)</td>
			<%if(readonly){%>
				<td class="labelTextValor">
				<siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacion"  ancho="80" clase="<%=estiloCombo%>" elementoSel="<%=selIdent%>" readOnly="<%=sreadonly%>" obligatorio="true"/>
				<%if(datosPersonales.getNumeroIdentificador()!=null)out.print(datosPersonales.getNumeroIdentificador());%>
				</td>
			<%}else{%>
				<td>
					<siga:ComboBD nombre = "tipoIdentificacion" tipo="identificacionSolicitud"  ancho="80" clase="<%=estiloCombo%>" elementoSel="<%=selIdent%>" readOnly="<%=sreadonly%>" obligatorio="true" accion="comprobarTipoIdent();"/>
					<html:text property="NIFCIF" styleClass="box" size="8" maxlength="20" value="<%=datosPersonales.getNumeroIdentificador() %>"></html:text>
					<img id="idButtonNif" src="<%=app%>/html/imagenes/comprobar.gif" border="0" onclick="obtenerLetra();" style="cursor:hand;align:left" style="display:inline;visibility: hidden;">
				</td>
			<%}%>
			
			<td class="labelText"><siga:Idioma key="censo.consultaDatosGenerales.literal.sexo"/>&nbsp;(*)</td>
			<%if (readonly){%>
				<td class="labelTextValor">
					<%if(datosPersonales.getSexo()!=null)out.print(UtilidadesString.mostrarDatoJSP(ssexo));%>
				</td>
			<%}else{%>
				<td>
					<html:select name="SolicitudIncorporacionForm" property="sexo" style = "null"  value="<%=datosPersonales.getSexo()%>"   styleClass = "box" >
				        <html:option value="0" >&nbsp;</html:option>
						<html:option value="<%=ClsConstants.TIPO_SEXO_HOMBRE%>"><siga:Idioma key="censo.sexo.hombre"/></html:option>
						<html:option value="<%=ClsConstants.TIPO_SEXO_MUJER%>"><siga:Idioma key="censo.sexo.mujer"/></html:option>
					</html:select>	
				</td>
			<%}%>
			
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.tratamiento"/>&nbsp;(*)</td>
			<td><siga:ComboBD nombre="tipoDon" tipo="tratamiento" clase="<%=estiloCombo%>" readOnly="<%=sreadonly%>"  obligatorio="true" elementoSel="<%=selTratamiento %>"/></td>
		</tr>
		<tr>
		
			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>&nbsp;(*)</td>
			<td><html:text property="nombre" style="width:180" maxlength="120" value="<%=datosPersonales.getNombre()%>" styleClass="<%=estiloBox%>"  readOnly="<%=readonly%>" ></html:text></td>

			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido1"/>&nbsp;(*)</td>
			<td><html:text property="apellido1"  style="width:180" maxlength="100" value="<%=datosPersonales.getApellido1()%>" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" ></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.apellido2"/></td>
			<td><html:text property="apellido2"  style="width:180" maxlength="100" value="<%=datosPersonales.getApellido2()%>" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"  ></html:text></td>
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fechaNacimiento"/>&nbsp;(*)</td>
			<%if(readonly){%>
				<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(datosPersonales.getFechaNacimiento())%></td>
			<%}else{%>
				<td>
					<siga:Fecha nombreCampo="fechaNacimiento" valorInicial="<%=datosPersonales.getFechaNacimiento() %>"></siga:Fecha>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaNacimiento);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
				</td>
			<%}%>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.naturalDe"/></td>
			<td><html:text property="natural" style="width:180" maxlength="100" styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" value="<%= datosPersonales.getNaturalDe() %>"></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.estadoCivil"/></td>
			<td><siga:ComboBD nombre = "estadoCivil" tipo="estadoCivil" clase="<%=estiloCombo%>" readOnly="<%=sreadonly%>" elementoSel="<%=selEstadoCiv%>" /></td>
		</tr>
		<tr>
			
		</tr>
	</table>
	</siga:ConjCampos>
	<siga:ConjCampos>
	<table>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.datosDireccion.literal.pais2"/></td>
			<%if(readonly){%>
				<td colspan="2" class="labelTextValor"><%=pais%></td>
			<%}else{%>
				<td colspan="2"><siga:ComboBD nombre="pais" tipo="pais" ancho="300" clase="<%=estiloCombo%>" obligatorio="false" elementoSel="<%=selPais%>" readOnly="<%=sreadonly%>" accion="cargaPais(this.value);"/></td>
			<%}%>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>&nbsp;(*)</td>
			
			<%if(readonly && !esEspana){%>
				<td colspan="2" class="labelTextValor"><%=provincia%></td>
			<%}else{%>
				<td colspan="2"><siga:ComboBD nombre="provincia" tipo="provincia" clase="<%=estiloCombo%>" elementoSel="<%=selProvincia %>" readOnly="<%=sreadonly%>" obligatorio="true" accion="Hijo:poblacion"/></td>
			<%}%>
		</tr>
		<tr>

			<td class="labelText"><siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>&nbsp;(*)</td>
			<td id="poblacionEspanola" colspan="2">
			<%if(readonly){%>
				<input type="text" value="<%=poblacion%>" size="30" maxlength="100" class="boxConsulta" readonly></input>
			<%}else{%>
				<siga:ComboBD nombre="poblacion" tipo="poblacion" clase="<%=estiloCombo%>" elementoSel="<%=selPoblacion %>" readOnly="<%=sreadonly%>" obligatorio="true" hijo="t" ancho="300"/>
			<%}%>
			</td> 
			
			<td class="ocultar" colspan="2" id="poblacionExtranjera">
				<html:text property="poblacionExt" style="width:300" maxlength="100" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getPoblacionExtranjera()%>" readOnly="<%=readonly%>"></html:text>
			</td>
				
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.domicilio"/>&nbsp;(*)</td>
			<td><html:text property="domicilio" size="30" maxlength="100" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getDomicilio()%>" readOnly="<%=readonly%>"></html:text></td>
			
			<td class="labelText" >CP&nbsp;(*)
			<html:text property="CP" styleClass="<%=estiloBox%>" size="5" maxlength="5" value="<%=datosPersonales.getCodigoPostal()%>" readOnly="<%=readonly%>"></html:text></td>
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono1"/>&nbsp;(*)</td>
			<td><html:text property="telefono1" maxlength="20" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getTelefono1()%>" readOnly="<%=readonly%>"></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono2"/></td>
			<td><html:text property="telefono2" maxlength="20" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getTelefono2()%>" readOnly="<%=readonly%>"></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.telefono3"/></td>
			<td><html:text property="telefono3" maxlength="20" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getMovil()%>" readOnly="<%=readonly%>"></html:text></td>
			
		</tr>
		<tr>
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax1"/></td>
			<td><html:text property="fax1" maxlength="20" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getFax1()%>" readOnly="<%=readonly%>"></html:text></td>
			
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.fax2"/></td>
			<td><html:text property="fax2" maxlength="20" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getFax2()%>" readOnly="<%=readonly%>"></html:text></td>
					
			<td class="labelText" ><siga:Idioma key="censo.SolicitudIncorporacion.literal.email"/>&nbsp;(*)</td>
			<td><html:text property="mail" maxlength="100" styleClass="<%=estiloBox%>" value="<%=datosPersonales.getCorreoElectronico()%>" readOnly="<%=readonly%>"></html:text></td>
		</tr>
		
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">

	</table>
	
	</siga:ConjCampos>
		
	<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera">
		<table class="tablaCampos" align="left" border="0" style="width:80%">	
			<tr align="left">		
				<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/></td>
				<td class="labelText">
					<html:text property="titular" value="<%=titular%>" size="50" styleClass="<%=estiloBox%>" maxlength="100" readOnly="<%=readonly%>"></html:text>
				</td>
				<td colspan="2">
					<table>
					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.tipoCuenta.cargo"/>
							<html:checkbox property="cuentaCargo"  disabled="<%=readonly%>"/>
						</td>	
						<td class="labelText">
							<siga:Idioma key="censo.tipoCuenta.abono"/>
							<html:checkbox property="cuentaAbono" disabled="<%=readonly%>" onClick="validaAbonoSJCS()"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/>
							<html:checkbox property="abonoSJCS" disabled="<%=readonly%>" onClick="validaAbonoSJCS()" />
						</td>
					</tr>
					</table>
				</td>
			<tr>

		
			<!-- FILA -->
			<tr>					
				<td class="labelText" nowrap>Cuenta</td>	
				<td class="labelText">
				      <html:text size="4"  maxlength="4" property="cbo_Codigo"     value="<%=cbo_Codigo%>" 				styleClass="<%=estiloBox%>" readOnly="<%=readonly%>" onChange="document.SolicitudIncorporacionForm.banco.value=document.SolicitudIncorporacionForm.cbo_Codigo.value"></html:text>
					- <html:text size="4"  maxlength="4" property="codigoSucursal" value="<%=cuentaCodigoSucursal%>" 	styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
					- <html:text size="2"  maxlength="2" property="digitoControl"  value="<%=cuentaDigitoControl%>" 	styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text>
					- <html:text size="10" maxlength="10" property="numeroCuenta"  value="<%=cuentaNumeroCuenta%>" 		styleClass="<%=estiloBox%>" readOnly="<%=readonly%>"></html:text></td>
				
				<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
				<td class="labelText">
					<siga:ComboBD nombre="banco" ancho="450" tipo="cmbBancos" clase="boxCombo" elementoSel="<%=listaBancos%>" readOnly="<%=sreadonly%>" accion="document.SolicitudIncorporacionForm.cbo_Codigo.value=document.SolicitudIncorporacionForm.banco.value"/>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	
	
	<siga:TablaCabecerasFijas nombre="documentoAPresentar" 
			borde="1" 
			estilo="" 
			clase="tableTitle" 
			nombreCol="censo.SolicitudIncorporacionDatos.literal.estado,censo.SolicitudIncorporacionDatos.literal.documento" 
			tamanoCol="10,90"
			ajusteBotonera="true"
			ajuste="20"
			>
			
		<% if (documentos != null) {
			 for (int i = 0; i < documentos.size(); i++){ 
			 		Vector v = (Vector) documentos.get(i);
			 		if (v != null) {
			 	  	CenDocumentacionSolicitudInstituBean documento = (CenDocumentacionSolicitudInstituBean) v.get(0); 
						String estado = (String) v.get(1);
		%>
		   <tr class="listaNonEdit">
		   		<td align="center">
		   			<input type="hidden" id="oculto<%=(i+1)%>_1" value="<%=documento.getDocumentacionSolicitud().getIdDocumentacion()%>">
		   			<input type="checkbox" <%if (editar.equalsIgnoreCase("false")) { out.print(" disabled "); } %><%if(estado.equalsIgnoreCase("true"))out.print("checked");%>>
		   		</td>
		   		<td>
		   			<%if(documento.getDocumentacionSolicitud().getDescripcion()!=null)out.print(UtilidadesString.mostrarDatoJSP(documento.getDocumentacionSolicitud().getDescripcion()));%>
		   		</td>
		   </tr>
	  <%			} // if
	   		} // for
	    }else{%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" >
	   		 	<!-- PENDIENTE No tiene documentos adjuntos. Pendiente de Validar-->
	   		 	<siga:Idioma key="censo.SolicitudIncorporacionDatos.literal.aviso"/>
	   		 </p>
	 		<br>
	  <%}%>
	</siga:TablaCabecerasFijas>
	

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<%if (editar!=null && !editar.equalsIgnoreCase("false")) {%>
		<siga:ConjBotonesAccion botones="G,R,C" clase="botonesDetalle" />
	<%}else if (modoAnterior.equalsIgnoreCase("VER")) {%>
		<siga:ConjBotonesAccion botones="C" clase="botonesDetalle" />
	<%}else{%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<%}%>
	<!-- FIN: BOTONES REGISTRO -->

	</html:form>

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	

	function accionVolver() {		
		window.location = "<%=app%>/html/jsp/censo/SolicitudIncorporacionValidacion.jsp";
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
							if (oculto == null)  { flag = false; }
							else { datos.value = datos.value + oculto.value + ','; }
							j++;
						}
						datos.value = datos.value + "%"
						datos.value = datos.value + (tabla.rows[fila].cells)[0].all[j-2].checked + ',';
						datos.value = datos.value + "#"
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
		//window.close();
		top.cierraConParametros("MODIFICADO");
	}
	

	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</body>
</html>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

