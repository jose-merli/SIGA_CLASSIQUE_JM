<!DOCTYPE html>
<html>
<head>
<!-- detalleSolicitudOriginal.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	int institucion = Integer.parseInt(idInstitucion);
	
	String modo=(String)request.getAttribute("modo");
	
	
	CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) request.getAttribute("solicitud");
	// CerSolicitudCertificadosBean beanSolOriginal = (CerSolicitudCertificadosBean) request.getAttribute("solOriginal");
	CenInstitucionBean beanInstitucionOrigen = (CenInstitucionBean) request.getAttribute("institucionOrigen");
	CenInstitucionBean beanInstitucionDestino = (CenInstitucionBean) request.getAttribute("institucionDestino");
	CenInstitucionBean beanInstitucionColegiacion = (CenInstitucionBean) request.getAttribute("institucionColegiacion");
	String idEstadoSolicitud = (String) request.getAttribute("idEstadoSolicitud");
	String tipoCertificado = (String) request.getAttribute("tipoCertificado");

	String consultaOrigen, consultaDestino = "";

	if (institucion == 2000){ // General
		consultaOrigen = "getColegiosAbreviados";
		consultaDestino = "getColegiosAbreviados";
	}else if (institucion > 3000){  // Consejo
		consultaOrigen = "getColegiosDeConsejo";
		consultaDestino = "getColegiosDeConsejo";
	}else{ // Colegio
		consultaOrigen = "getColegiosDeConsejo";
		consultaDestino = "getColegiosAbreviados";
	}
	
	ArrayList idInstitucionPresentador = new ArrayList();
	if(modo.equalsIgnoreCase("nuevo") && ClsConstants.esColegio(idInstitucion)){
		idInstitucionPresentador.add(idInstitucion);
	} else if (beanInstitucionOrigen != null) {
		idInstitucionPresentador.add(beanInstitucionOrigen.getIdInstitucion().toString());
	}
	
	ArrayList idInstitucionColegiacion = new ArrayList();
	if (beanInstitucionColegiacion != null) {
		idInstitucionColegiacion.add(beanInstitucionColegiacion.getIdInstitucion().toString());
	}
	

	ArrayList idInstitucionDestino = new ArrayList();
	if (beanInstitucionDestino != null) {
		idInstitucionDestino.add(beanInstitucionDestino.getIdInstitucion().toString());
	}


	String numSolicitud = "", idProducto = "",idProductoInstitucion = "",idTipoProducto = "";
	String idInstitucionCertificado = "", idPeticion = "", idsTemp = "", sIdCompra = "";
	String institucionFinal = "", idPersona = "";
	String estadoInc = "", estadoIncOld = "",  residente = "", residenteOld = "";
	String aceptaCesionTxt = "", aceptMut = "", aceptaCesionTxtOrig = "";
	String fechaSolicitud = "", idInstitucionSolicitud ="", fechaSolOrig = "";
	
	String idInstitucionSolOrig = "", idTipoProductoOrig = "", idProductoOrig = "";
	String idProductoInstOrig = "", idtipoCertSel = "", idtipoCertSelOrig = "";
	
	ArrayList tipoCertSel =new ArrayList();
	ArrayList tipoCertSelOrig = new ArrayList();
	String numSolicitudCol = "", nombreInteresado = "", apellido1Interesado = "", apellido2Interesado = "", nidInteresado = "";
	String fechaNacInteresado = "", telInteresado = "", movilInteresado = "", faxInteresado = "", emailInteresado = "";
	String paisInteresado = "", domicilioInteresado = "", cpostalInteresado = "", provInteresado = "", poblInteresado = "";
	String anioLicenciatura = "";
	String numSolicitudColOld = "", nombreInteresadoOld = "", apellido1InteresadoOld = "", apellido2InteresadoOld = "", nidInteresadoOld = "";
	String fechaNacInteresadoOld = "", telInteresadoOld = "", movilInteresadoOld = "", faxInteresadoOld = "", emailInteresadoOld = "";
	String paisInteresadoOld = "", domicilioInteresadoOld = "", cpostalInteresadoOld = "", provInteresadoOld = "", poblInteresadoOld = "";
	
	
	if (beanSolicitud != null){
		if(beanSolicitud.getIdPeticionProducto() != null) {
			sIdCompra = beanSolicitud.getIdPeticionProducto().toString();
		}

		if(beanSolicitud.getIdSolicitud() != null) {
			numSolicitud = beanSolicitud.getIdSolicitud().toString();
		}
		
		if(beanSolicitud.getIdPeticionProducto()!=null){
			idPeticion =  beanSolicitud.getIdPeticionProducto().toString();
		}
		if(beanSolicitud.getIdInstitucion()!=null)
			idInstitucionCertificado = beanSolicitud.getIdInstitucion().toString();
		
		idPersona = beanSolicitud.getIdPersona_Des().toString();
		
		institucionFinal = idInstitucionCertificado; //Revisar

		aceptaCesionTxt = beanSolicitud.getAceptaCesionMutualidad().equals("1")?"SI":"NO";
		aceptMut = beanSolicitud.getAceptaCesionMutualidad();

		fechaSolicitud = beanSolicitud.getFechaSolicitud();
		idInstitucionSolicitud = beanSolicitud.getIdInstitucion_Sol().toString();
		idTipoProducto = beanSolicitud.getPpn_IdTipoProducto().toString();
		idProducto = beanSolicitud.getPpn_IdProducto().toString();
		idProductoInstitucion = beanSolicitud.getPpn_IdProductoInstitucion().toString();
		idtipoCertSel = idInstitucion + "_" +  idTipoProducto + "_" + idProducto  + "_" + idProductoInstitucion;
		tipoCertSel.add(idtipoCertSel);
		
		idsTemp = fechaSolicitud + "||" + numSolicitud + "||" + tipoCertificado + "||" + idTipoProducto + "||" + idProducto + "||" + idProductoInstitucion + "||" + idInstitucionCertificado + "||" + idPersona + "||" + institucionFinal;
	}
	
	/*if (beanSolOriginal != null){
		aceptaCesionTxtOrig = beanSolOriginal.getAceptaCesionMutualidad().equals("1")?"SI":"NO";
		fechaSolOrig = beanSolOriginal.getFechaSolicitud();
		idInstitucionSolOrig = beanSolOriginal.getIdInstitucion_Sol().toString();
		idTipoProductoOrig = beanSolOriginal.getPpn_IdTipoProducto().toString();
		idProductoOrig = beanSolOriginal.getPpn_IdProducto().toString();
		idProductoInstOrig = beanSolOriginal.getPpn_IdProductoInstitucion().toString();
		idtipoCertSelOrig = idInstitucion + "_" +  idTipoProductoOrig + "_" + idProductoOrig  + "_" + idProductoInstOrig;
		tipoCertSelOrig.add(idtipoCertSelOrig);
	}*/
		
		//Datos Interesado
		numSolicitudCol = (String) request.getAttribute("numSolicitudCol");
		nombreInteresado = (String) request.getAttribute("nombreInteresado");
		apellido1Interesado = (String) request.getAttribute("apellido1Interesado");
		apellido2Interesado = (String) request.getAttribute("apellido2Interesado");
		nidInteresado = (String) request.getAttribute("nidInteresado");
		fechaNacInteresado = (String) request.getAttribute("fechaNacInteresado");
		telInteresado = (String) request.getAttribute("telInteresado");
		movilInteresado = (String) request.getAttribute("movilInteresado");
		faxInteresado = (String) request.getAttribute("faxInteresado");
		emailInteresado = (String) request.getAttribute("emailInteresado");
		paisInteresado = (String) request.getAttribute("paisInteresado");
		domicilioInteresado = (String) request.getAttribute("domicilioInteresado");
		cpostalInteresado = (String) request.getAttribute("cpostalInteresado");
		provInteresado = (String) request.getAttribute("provInteresado");
		poblInteresado = (String) request.getAttribute("poblInteresado");
		anioLicenciatura = (String) request.getAttribute("anioLicenciatura");
		
		if(request.getAttribute("estadoInc").equals("10"))
			estadoInc = "No Ejerciente";
		else
			estadoInc = "Ejerciente";
		if(request.getAttribute("residenteInc").equals("SI"))	
			residente = "Residente";
		else
			residente = "No residente";
		
		numSolicitudColOld = (String) request.getAttribute("numSolicitudColOld");
		nombreInteresadoOld = (String) request.getAttribute("nombreInteresadoOld");
		apellido1InteresadoOld = (String) request.getAttribute("apellido1InteresadoOld");
		apellido2InteresadoOld = (String) request.getAttribute("apellido2InteresadoOld");
		nidInteresadoOld = (String) request.getAttribute("nidInteresadoOld");
		fechaNacInteresadoOld = (String) request.getAttribute("fechaNacInteresadoOld");
		telInteresadoOld = (String) request.getAttribute("telInteresadoOld");
		movilInteresadoOld = (String) request.getAttribute("movilInteresadoOld");
		faxInteresadoOld = (String) request.getAttribute("faxInteresadoOld");
		emailInteresadoOld = (String) request.getAttribute("emailInteresadoOld");
		paisInteresadoOld = (String) request.getAttribute("paisInteresadoOld");
		domicilioInteresadoOld = (String) request.getAttribute("domicilioInteresadoOld");
		cpostalInteresadoOld = (String) request.getAttribute("cpostalInteresadoOld");
		provInteresadoOld = (String) request.getAttribute("provInteresadoOld");
		poblInteresadoOld = (String) request.getAttribute("poblInteresadoOld");
		if(request.getAttribute("estadoIncOld").equals("10"))
			estadoIncOld = "No Ejerciente";
		else
			estadoIncOld = "Ejerciente";
		
		if(request.getAttribute("residenteIncOld").equals("SI"))	
			residenteOld = "Residente";
		else
			residenteOld = "No residente";

	// Las siguientes variables controlaran que se pueda o no Facturar y Anular. Ademas, cuando el control de facturas este activo, el campo 'Siguiente estado' para 'Aprobado' siempre sera 'Finalizado'
	String controlFacturasSII = (String) request.getAttribute("controlFacturasSII");
	String hayFacturacionHoy = (String) request.getAttribute("hayFacturacionHoy");
	String facturado = (String) request.getAttribute("facturado");

	String botones = "";
	//Modo consulta
	botones = "V,DLNIF";
		
	String tipoCombo="boxConsulta";
	String tipoComboRojo="boxConsultaRojoN";

	String idInstitucionSol = UtilidadesString.createJsonString("idinstitucion", idInstitucionSolicitud);
	String idInstSolOriginal = 	UtilidadesString.createJsonString("idinstitucion", idInstitucionSolOrig);
	String paramInstitucion[] = { idInstitucion };

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<!-- Incluido jquery en siga.js -->
	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

	<style type="text/css">
		.ui-dialog-titlebar-close {
			  visibility: hidden;
		}
		td{
			padding-top: .3em;
			height: 27px;
		}
	</style>
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	jQuery.noConflict();
		function refrescarLocal() {
			sub();
			CerSolicitudOriginalForm.target="mainPestanas";
			CerSolicitudOriginalForm.modo.value="editar";
			CerSolicitudOriginalForm.submit();
		}
		// Asociada al boton Cerrar
		function accionVolver() 
		{		
			sub();
			CerSolicitudOriginalForm.action = "/SIGA/CER_GestionSolicitudes.do";
			CerSolicitudOriginalForm.target = "mainWorkArea";
			CerSolicitudOriginalForm.modo.value="volver";
			CerSolicitudOriginalForm.submit();
		}
		function descargarDocumento() {
				sub();
				CerSolicitudOriginalForm.descargarCertificado.value="1";
				CerSolicitudOriginalForm.modo.value="descargar";
				CerSolicitudOriginalForm.submit();
		}
	</script>
		
	<style>
        .colizq{clear:left;float:left;}
       
        .colsiguientes{float:left;display:block;}
	</style>	
		
		
<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onLoad="ajusteAltoBotones('mainWorkArea');" height="100%">

	<div id="camposRegistro" style="height:750px; overflow-x:hidden; overflow-y: scroll">
	<html:form action="/CER_SolicitudOriginal" method="POST" target="submitArea">
		<html:hidden property="modo" value="" />
		<html:hidden property="idInstitucion" styleId="idInstitucion" value="<%=idInstitucion%>" />
		<html:hidden property="idInstitucionSolicitud" value="<%=idInstitucionSolicitud%>" />
		<html:hidden property="buscarIdPeticionCompra" value="<%=sIdCompra %>" />
			
		<html:hidden property="idSolicitud" styleId="idSolicitud" value="<%=numSolicitud%>" />
		<html:hidden property="idTipoProducto" styleId="idTipoProducto" value="<%=idTipoProducto%>" />
		<html:hidden property="idProducto" styleId="idProducto" value="<%=idProducto%>" />
		<html:hidden property="idProductoInstitucion" styleId="idProductoInstitucion" value="<%=idProductoInstitucion%>" />
		<html:hidden property="tipoCertificado" value="<%=tipoCertificado%>" />
		<html:hidden property="idsTemp" value="<%=idsTemp%>"/>
		<html:hidden property="idSerieSeleccionada" styleId="idSerieSeleccionada" />
		<html:hidden property="regenerar" value="" />
		<input type="hidden" id="idPeticion" name="idPeticion" value="<%=idPeticion%>">	
		<input type="hidden" id="descargarCertificado" name="descargarCertificado" value="">	
		
		<html:hidden property="aceptaCesionMutualidad" value="<%=aceptMut%>" />
		<tr>
			<td> <siga:ConjCampos leyenda="gratuita.busquedaEJG.interesado">
				<table class="tablaCampos" align="left" style="border: none; width: auto" cellspacing="0">
				<tr>
					<td>
						<table class="tablaCampos" align="center"  style="border: none; width: auto;">
							<tr>
								<td colspan="1">&nbsp;</td>
								<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.datosCertifOriginal"/>&nbsp;</td>
							</tr>
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="certificados.solicitudes.literal.numSolicitudColegiacion" /></td>
								<% if (numSolicitudCol.equals(numSolicitudColOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(numSolicitudCol)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(numSolicitudCol)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.busquedaClientes.literal.nombre" /></td>
								<% if (nombreInteresado.equals(nombreInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(nombreInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(nombreInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido1" /></td>
								<% if (apellido1Interesado.equals(apellido1InteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(apellido1Interesado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(apellido1Interesado)%></td>
								<% } %> 
							</tr>
		
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.busquedaDuplicados.patron.apellido2"/></td>
								<% if (apellido2Interesado.equals(apellido2InteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(apellido2Interesado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(apellido2Interesado)%></td>
								<% } %>		
								
							</tr>
							
							<tr>
								<td class="labelText" id="td_gcob1" style="width: 165px"><siga:Idioma key="censo.busquedaDuplicados.patron.numeroIdentificacion" /></td>
								<% if (nidInteresado.equals(nidInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(nidInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(nidInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="informes.solicitudAsistencia.nacimiento" /></td>
								<% if (fechaNacInteresado.equals(fechaNacInteresadoOld)) { %>
									<td class="labelText" width="200px"><%=UtilidadesString.mostrarDatoJSP(fechaNacInteresado)%></td>
								<%}else{%>
									<td class="labelText" width="200px" style="font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(fechaNacInteresado)%></td>
								<% } %> 
							</tr>
						
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.busquedaClientesAvanzada.literal.telefono" /></td>
								<% if (telInteresado.equals(telInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(telInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(telInteresado)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.datosDireccion.literal.movil" /></td>
								<% if (movilInteresado.equals(movilInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(movilInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(movilInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.ws.literal.fax" /></td>
								<% if (faxInteresado.equals(faxInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(faxInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(faxInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.datosDireccion.literal.correo" /></td>
								<% if (emailInteresado.equals(emailInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(emailInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(emailInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.datosDireccion.literal.pais" /></td>
								<% if (paisInteresado.equals(paisInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(paisInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(paisInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.ws.literal.domicilio" /></td>
								<% if (domicilioInteresado.equals(domicilioInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(domicilioInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(domicilioInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="certificados.solicitudes.literal.postalYProvincia" /></td>
								<% if (cpostalInteresado.equals(cpostalInteresadoOld) && provInteresado.equals(provInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(cpostalInteresado) + " " + UtilidadesString.mostrarDatoJSP(provInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(cpostalInteresado) + " " + UtilidadesString.mostrarDatoJSP(provInteresado)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.datosDireccion.literal.poblacion" /></td>
								<% if (poblInteresado.equals(poblInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(poblInteresado)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(poblInteresado)%></td>
								<% } %> 
							</tr>
							<tr>
								<td class="labelText" style="width: 165px"><siga:Idioma key="censo.datosDireccion.literal.anioLicenciatura" /></td>
								<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(anioLicenciatura)%></td> 
							</tr>
							
						</table>
					</td>
					<td style="vertical-align: middle;">							
						<table>
							<tr>
								<td style="height:500px;" class="linea">&nbsp;</td>
							</tr>
						</table>
					</td>
					<td>
						<table class="tablaCampos" align="center" style="border: none; width: auto;">
							<tr>
								<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.datosExistentes"/>&nbsp;</td>
								<td colspan="2">&nbsp;</td>
							</tr>
							
							<tr>
								<% if (numSolicitudCol.equals(numSolicitudColOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(numSolicitudColOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(numSolicitudColOld)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<% if (nombreInteresado.equals(nombreInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(nombreInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(nombreInteresadoOld)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<% if (apellido1Interesado.equals(apellido1InteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(apellido1InteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(apellido1InteresadoOld)%></td>
								<% } %> 
							</tr>
		
							<tr>
								<% if (apellido2Interesado.equals(apellido2InteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(apellido2InteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(apellido2InteresadoOld)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<% if (nidInteresado.equals(nidInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(nidInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(nidInteresadoOld)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<% if (fechaNacInteresado.equals(fechaNacInteresadoOld)) { %>
									<td class="labelText" width="200px"><%=UtilidadesString.mostrarDatoJSP(fechaNacInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" width="200px" style="font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(fechaNacInteresadoOld)%></td>
								<% } %> 
							</tr>
						
							<tr>
								<% if (telInteresado.equals(telInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(telInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(telInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (movilInteresado.equals(movilInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(movilInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(movilInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (faxInteresado.equals(faxInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(faxInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(faxInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (emailInteresado.equals(emailInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(emailInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(emailInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (paisInteresado.equals(paisInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(paisInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(paisInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (domicilioInteresado.equals(domicilioInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(domicilioInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(domicilioInteresadoOld)%></td>
								<% } %> 
							</tr>
							
							<tr>
								<% if (cpostalInteresado.equals(cpostalInteresadoOld) && provInteresado.equals(provInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(cpostalInteresadoOld) + " " + UtilidadesString.mostrarDatoJSP(provInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(cpostalInteresadoOld) + " " + UtilidadesString.mostrarDatoJSP(provInteresadoOld)%></td>
								<% } %> 					
							</tr>
							
							<tr>
								<% if (poblInteresado.equals(poblInteresadoOld)) { %>
									<td class="labelText" style="min-width: 200px"><%=UtilidadesString.mostrarDatoJSP(poblInteresadoOld)%></td>
								<%}else{%>
									<td class="labelText" style="min-width: 200px; font-weight: bold; color: red;"><%=UtilidadesString.mostrarDatoJSP(poblInteresadoOld)%></td>
								<% } %> 
							</tr>
						</table>
					</td>
				</tr>
				</table>
			</siga:ConjCampos>			
			
			</td>							
		</tr>
	</html:form>	
	</div>
	
	<siga:ConjBotonesAccion botones="<%=botones%>" ordenar="true"/>
	
	<% String busquedaVolver = null; /* la vuelta no se trata de forma generica */ %>
	<%@ include file="/html/jsp/censo/includeMantenimientoDuplicados.jspf"%>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>

</body>
</html>