<!-- detalleSolicitudCertificado.jsp -->
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
	
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) request.getAttribute("solicitud");
	CenInstitucionBean beanInstitucionOrigen = (CenInstitucionBean) request.getAttribute("institucionOrigen");
	CenInstitucionBean beanInstitucionDestino = (CenInstitucionBean) request.getAttribute("institucionDestino");
	CenInstitucionBean beanInstitucionColegiacion = (CenInstitucionBean) request.getAttribute("institucionColegiacion");
	String modificarSolicitud = (String) request.getAttribute("modificarSolicitud");
	String idEstadoSolicitud = (String) request.getAttribute("idEstadoSolicitud");
	String tipoCertificado = (String) request.getAttribute("tipoCertificado");

	boolean isSolicitudColegio = beanSolicitud.getIdInstitucion_Sol().intValue()!=2000 && !String.valueOf(beanSolicitud.getIdInstitucion_Sol()).substring(0,2).equals("30"); 
	String comboInstituciones = "cmbInstitucionesAbreviadas";
	String comboInstitucionesDest = "cmbInstitucionesAbreviadas";
	String consultaOrigen, consultaDestino = "";

	if (institucion == 2000){ // General
		consultaOrigen = "cmbColegiosAbreviados";
		consultaDestino = "cmbColegiosAbreviados";
	}else if (institucion > 3000){  // Consejo
		consultaOrigen = "cmbColegiosConsejo";
		consultaDestino = "cmbColegiosConsejoRelacionados";
	}else{ // Colegio
		consultaOrigen = "cmbColegiosConsejo";
		consultaDestino = "cmbColegiosAbreviados";
	}
	String[] parametros = new String[]{beanSolicitud.getIdInstitucion_Sol().toString(),beanSolicitud.getIdInstitucion_Sol().toString()};
		
	ArrayList idInstitucionPresentador = new ArrayList();
	if (beanInstitucionOrigen != null) {
		idInstitucionPresentador.add(beanInstitucionOrigen.getIdInstitucion().toString());
	}
	
	ArrayList idInstitucionColegiacion = new ArrayList();
	if (beanInstitucionColegiacion != null) {
		idInstitucionColegiacion.add(beanInstitucionColegiacion.getIdInstitucion().toString());
	}
	
	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(beanSolicitud.getMetodoSolicitud());

	ArrayList idInstitucionDestino = new ArrayList();
	if (beanInstitucionDestino != null) {
		idInstitucionDestino.add(beanInstitucionDestino.getIdInstitucion().toString());
	}

	String codigo = (String) request.getAttribute("codigo");
	String sanciones = (String) request.getAttribute("sanciones");

	String numSolicitud = beanSolicitud.getIdSolicitud().toString();
	Long idCompra = beanSolicitud.getIdPeticionProducto();
	String sIdCompra = "";
	if (idCompra != null) {
		sIdCompra = idCompra.toString();
	}

	String sAbreviaturaInstitucionOrigen = "";
	String sAbreviaturaInstitucionDestino = "";

	if (beanInstitucionOrigen != null) {
		sAbreviaturaInstitucionOrigen = beanInstitucionOrigen.getAbreviatura();
	}

	if (beanInstitucionDestino != null) {
		sAbreviaturaInstitucionDestino = beanInstitucionDestino.getAbreviatura();
	}
	String deshabilitaCobro = "";
	String deshabilitaDescarga = "";
	if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND)) {
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "";

	} else if (idEstadoSolicitud
			.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_APROBADO)) {
		deshabilitaDescarga = "";
		deshabilitaCobro = "";
	} else if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO)
			|| idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO)
			|| idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO)) {
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "disabled";
	}
	String botones = "";
	if (idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_DENEGADO) 
		|| idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_ANULADO) 
		|| modo.equalsIgnoreCase("ver")) {
		botones = "C";
	} else {
		botones = "Y,C";
	}
	
	String tipoBox="box";
	String stLectura="false";
	boolean modoEditar=true;
	String deshabilitaInfo="";
	String deshabilitaChecks="";
	if(modo.equalsIgnoreCase("ver")){
		tipoBox="boxConsulta";
		stLectura="true";
		modoEditar=false;
		deshabilitaDescarga = "disabled";
		deshabilitaCobro = "disabled";
		deshabilitaInfo = "disabled";
		deshabilitaChecks = "disabled";
		modificarSolicitud="0";
	}

	String nombreUltimoUsuMod = (String) request
			.getAttribute("nombreUltimoUsuMod");
	if (nombreUltimoUsuMod == null)
		nombreUltimoUsuMod = new String("");
%>

<html>
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp">

<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"
	type="text/javascript"></script>

<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">
		
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}

			function copiarSanciones() 
			{		
				SolicitudesCertificadosForm.modo.value="copiarSanciones";
				SolicitudesCertificadosForm.submit();
			}
			
			function copiarHistorico() 
			{		
				SolicitudesCertificadosForm.modo.value="copiarHistorico";
				SolicitudesCertificadosForm.submit();
			}
			function historicoObservaciones()
			{
				SolicitudesCertificadosForm.modo.value="historicoObservaciones";
				SolicitudesCertificadosForm.submit();
			} 
			
			function accionGuardarCerrar() 
			{	
				sub();
				if (SolicitudesCertificadosForm.fechaSolicitud.value==""){
				      alert("Debe introducir una fecha de solicitud");
				      fin();
					  return false;
				    }
				var idInstitucion = SolicitudesCertificadosForm.idInstitucion.value;
				if (SolicitudesCertificadosForm.idInstitucionOrigen.value==""){
			      alert("<siga:Idioma key="messages.certificado.error.noExisteColegioOrigen"/>");
			      fin();
				  return false;
			    }
			  if (SolicitudesCertificadosForm.idInstitucionDestino){	
			  <% if (tipoCertificado.equals("C")){%> 
			       if (SolicitudesCertificadosForm.checkCobro.checked){
				       
				         SolicitudesCertificadosForm.idInstitucionDestino.value="";
					   
				 
				  }else{
				  	
				  	if(idInstitucion==2000 || idInstitucion.substring(0,2)==30){
					    if (SolicitudesCertificadosForm.idInstitucionDestino.value==""){
					      alert("<siga:Idioma key="messages.certificado.error.noExisteColegioFacturable"/>");
						  fin();
					 	  return false;
					    }
				    }
				  }
			  <%}%>	    	
			  }
			  		  	
				SolicitudesCertificadosForm.modo.value="modificar";
				SolicitudesCertificadosForm.submit();
			}
			
			function validarCheckDescarga()
			{
				if (!SolicitudesCertificadosForm.checkDescarga.checked)	
				{
					SolicitudesCertificadosForm.fechaDescarga.value="";
					
				}
				else
				{
					var fFecha = new Date();
					var dia=fFecha.getDate();
					var mes=fFecha.getMonth()+1;
					var yea=fFecha.getYear();
					if (dia<10) dia="0"+dia;
					if (mes<10) mes="0"+mes;
									
					SolicitudesCertificadosForm.fechaDescarga.value=dia+"/"+mes+"/"+yea;
				}
			}
			
			function validarCheckCobro()
			{
				if (!SolicitudesCertificadosForm.checkCobro.checked)	
				{
					SolicitudesCertificadosForm.fechaCobro.value="";
				    <% if (tipoCertificado.equals("C")){%>
				       jQuery("#idInstitucionDestino").removeAttr("disabled");
  				    <%}%>	  
				}
				else
				{
				    <% if (tipoCertificado.equals("C")){%>
				       SolicitudesCertificadosForm.idInstitucionDestino.value="";
					   jQuery("#idInstitucionDestino").attr("disabled","disabled");
  				    <%}%>	  
/*					var fFecha = new Date();
					var dia=fFecha.getDate();
					var mes=fFecha.getMonth()+1;
					var yea=fFecha.getYear();
					if (dia<10) dia="0"+dia;
					if (mes<10) mes="0"+mes;
					SolicitudesCertificadosForm.fechaCobro.value=dia+"/"+mes+"/"+yea;
*/					

					SolicitudesCertificadosForm.fechaCobro.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
				}
			}
			
			function fijarFechaEntregaInfo () 
			{
				if (SolicitudesCertificadosForm.checkInfoAdjunta.checked) {
					SolicitudesCertificadosForm.fechaEntregaInfo.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
				}
				else {
					SolicitudesCertificadosForm.fechaEntregaInfo.value="";
				}
			}
			
			function revisarCheck()
			{
				if (SolicitudesCertificadosForm.idInstitucionDestino)
				if (!SolicitudesCertificadosForm.checkCobro.checked)	
				{
				    <% if (tipoCertificado.equals("C")){%>
				       jQuery("#idInstitucionDestino").removeAttr("disabled");
 				    <%}%>	  
				}
				else
				{
				    <% if (tipoCertificado.equals("C")){%>
					   jQuery("#idInstitucionDestino").attr("disabled","disabled");
  				    <%}%>	  
				}
			}
			
		</script>
<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onLoad="revisarCheck();">

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma
			key="certificados.solicitudes.ventanaEdicion.titulo" /></td>
	</tr>
</table>

<div id="camposRegistro" class="posicionModalGrande" align="center">

<table class="tablaCentralCamposGrande" align="center">
	<html:form action="/CER_GestionSolicitudes.do" method="POST"
		target="submitArea">
		<html:hidden property="modo" value="" />
		<html:hidden property="idInstitucion" value="<%=beanSolicitud.getIdInstitucion().toString() %>" />
		<html:hidden property="idInstitucionSolicitud" value="<%=String.valueOf(beanSolicitud.getIdInstitucion_Sol()) %>" />
		<html:hidden property="buscarIdPeticionCompra" value="<%=sIdCompra %>" />
		
			
		<html:hidden property="idSolicitud" value="<%=numSolicitud%>" />
		<tr>
			<td><siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.datosSolicitud">
				<table class="tablaCampos" align="center">
					<tr>
						<td class="labelTextValue" ><b><siga:Idioma key="certificados.solicitudes.literal.numeroSolicitud" /></b>
						&nbsp;&nbsp;&nbsp;<%=numSolicitud%></td>
						<% if (sIdCompra != null) { %>
							<td class="labelTextvalue"><b><siga:Idioma key="certificados.solicitudes.literal.idSolicitudCompra" /></b>
							&nbsp;&nbsp;&nbsp;<%=sIdCompra%></td>
						<%}else{%>
							<td>&nbsp;</td>
						<% } %>						
						<td class="labelText" ><siga:Idioma key="certificados.solicitudes.literal.numeroCertificado" /></td>
						<td class="labelTextValor"><%=codigo%></td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.fechaSolicitud" /></td>
						<td class="labelTextValor">
							<%if (modificarSolicitud.equals("1") && idEstadoSolicitud.equals("1")) {%> 
								<%if(modoEditar){
									SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
									Date date = new Date(beanSolicitud.getFechaSolicitud());
									String fechaSol = sdf.format(date);	%>
									<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSol%>" />&nbsp
									
								<%} else {%>
									<html:text	name="SolicitudesCertificadosForm" style="width:80px"
										property="fechaSolicitud" styleClass="boxConsulta" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaSolicitud()) %>">
									</html:text>
								<%}%>
							<%} else {%>
									<html:text name="SolicitudesCertificadosForm" style="width:80px"
										property="fechaSolicitud" styleClass="boxConsulta" readonly="true"
										value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaSolicitud()) %>">
									</html:text>
							<%}%>
						</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.colegioOrigen" />&nbsp;(*)
						</td>
						<td class="labelTextValor">
						<%if (modificarSolicitud.equals("1")) { %> 
							<siga:ComboBD
								nombre="idInstitucionOrigen" tipo="<%=consultaOrigen%>" obligatorioSinTextoSeleccionar="true"
								elementoSel="<%=idInstitucionPresentador%>" parametro="<%=parametros%>" clase="<%=tipoBox%>"
								filasMostrar="1" readonly="<%=stLectura%>" /> 
						<%} else {%> 						
							<siga:ComboBD
								nombre="idInstitucionOrigen" tipo="<%=consultaOrigen%>" obligatorioSinTextoSeleccionar="true"
								elementoSel="<%=idInstitucionPresentador%>" parametro="<%=parametros%>" clase="boxConsulta"
								filasMostrar="1" readonly="true" /> 
						<%}%>
						</td>
						<td class="labelText">
						<%if (tipoCertificado.equals("C")) {%> 
							<siga:Idioma key="certificados.solicitudes.literal.facturableA" /> 
						<%} else {%> 
							<siga:Idioma key="certificados.solicitudes.literal.colegioDestino" /> 
						<%}%>
						</td>
						<td class="labelTextValor">
						<%if (modificarSolicitud.equals("1")
							  && !(idEstadoSolicitud.equals(CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO))) {%>
							<siga:ComboBD nombre="idInstitucionDestino"
								tipo="<%=consultaDestino%>" elementoSel="<%=idInstitucionDestino%>" parametro="<%=parametros%>" 
								clase="<%=tipoBox%>" filasMostrar="1" readonly="<%=stLectura%>" /> 
						<%} else {%> 				
							<siga:ComboBD
								nombre="idInstitucionDestino" tipo="<%=consultaDestino%>"
								elementoSel="<%=idInstitucionDestino%>" parametro="<%=parametros%>"  clase="boxConsulta"
								filasMostrar="1" readonly="true" /> 
						<%}%>
						</td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.metodoSolicitud"/></td>
						<td>
							<siga:ComboBD nombre="metodoSolicitud" tipo="comboMetodoSolicitud" obligatorio="false" parametro="<%=parametros%>" ElementoSel="<%=aMetodoSol%>" 
								clase="<%=tipoBox%>" readonly="<%=stLectura%>"/>
						</td>						
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="pys.solicitudCompra.literal.colegiadoen"/>
						</td>	
						<td class="labelTextValor">
						<%if (modificarSolicitud.equals("1")) { %> 
							<siga:ComboBD
								nombre="idInstitucionColegiacion" tipo="<%=consultaOrigen%>" obligatorioSinTextoSeleccionar="true"
								elementoSel="<%=idInstitucionColegiacion%>" parametro="<%=parametros%>" clase="<%=tipoBox%>"
								filasMostrar="1" readonly="<%=stLectura%>" /> 
						<%} else {%> 						
							<siga:ComboBD
								nombre="idInstitucionColegiacion" tipo="<%=consultaOrigen%>" obligatorioSinTextoSeleccionar="true"
								elementoSel="<%=idInstitucionColegiacion%>" parametro="<%=parametros%>" clase="boxConsulta"
								filasMostrar="1" readonly="true" /> 
						<%}%>
						</td>
						<td class="labelTextValor" colspan="4">
							<%if (modificarSolicitud.equals("1")) { %> 
								<i><siga:Idioma key="pys.solicitudCompra.literal.indicacion"/></i>
							<%} else {%> 						
								&nbsp;
							<%}%>							
						</td>
					</tr>					
					<tr>
						<td class="labelText">
							<siga:Idioma key="certificados.solicitudes.literal.descripcion"/>
						</td>				
						<td colspan="3">
							<html:textarea property="valor" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="<%=beanSolicitud.getDescripcion()%>" styleClass="boxConsulta" cols="100" rows="4" value="<%=beanSolicitud.getDescripcion()%>" readonly="true"/>
						</td>
					</tr>
				</table>
			</siga:ConjCampos> 
			<siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.gestionSolicitud">
				<table class="tablaCampos" align="center" border="0">
					<tr>
						<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.fechaEmision" /></td>
						<td>
						<%if (modificarSolicitud.equals("1")) {%> 
							<%if(modoEditar){
									String fechaEm ="";									
									if(!beanSolicitud.getFechaEmisionCertificado().equals("")){
										SimpleDateFormat sdf = new SimpleDateFormat(UtilidadesFecha.FORMATO_FECHA_ES);
										Date date = new Date(beanSolicitud.getFechaEmisionCertificado());
										fechaEm = sdf.format(date);
									}%>
								<siga:Fecha nombreCampo="fechaEmision" valorInicial="<%=fechaEm%>" />&nbsp
							<%} else {%>
								<html:text
									name="SolicitudesCertificadosForm" style="width:80px"
									property="fechaEmision" styleClass="<%=tipoBox %>" readonly="true"
									value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEmisionCertificado()) %>">
								</html:text>
							<%}%>
						<%} else {%>
							<html:text name="SolicitudesCertificadosForm" style="width:80px" property="fechaEmision" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEmisionCertificado()) %>">
							</html:text> 
						<%}%>
						</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.descargado" /></td>
						<td>
							<input type=checkbox name="checkDescarga" onclick="validarCheckDescarga();" <%=deshabilitaDescarga%> <%=(beanSolicitud.getFechaDescarga()!=null && !beanSolicitud.getFechaDescarga().trim().equals(""))?"checked":"" %>>
							&nbsp;&nbsp;
							<html:text name="SolicitudesCertificadosForm" property="fechaDescarga" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaDescarga()) %>"
								size="10"></html:text>
						</td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.cobrado" /></td>
						<td>
							<input type=checkbox name="checkCobro" onclick="validarCheckCobro();" <%=deshabilitaCobro%>
								<%=(beanSolicitud.getFechaCobro()!=null && !beanSolicitud.getFechaCobro().trim().equals(""))?"checked":"" %>>
							&nbsp;&nbsp;
							<html:text name="SolicitudesCertificadosForm"
								property="fechaCobro" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaCobro()) %>"
								size="10"></html:text>
						</td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.literal.enviado" /></td>
						<td>
							<input type=checkbox name="checkEnvio" disabled <%=(beanSolicitud.getFechaEnvio()!=null && !beanSolicitud.getFechaEnvio().trim().equals(""))?"checked":"" %>>
						&nbsp;&nbsp;
							<html:text name="SolicitudesCertificadosForm"
								property="fechaEnvio" styleClass="boxConsulta" readonly="true"
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEnvio()) %>"
								size="10"></html:text>
						</td>
						<td class="labelText"><siga:Idioma
							key="certificados.solicitudes.literal.entregadaInfoAdjunta" /></td>
						<td>
							<input type=checkbox name="checkInfoAdjunta" onclick="fijarFechaEntregaInfo();" <%=deshabilitaInfo%>
								<%=(beanSolicitud.getFechaEntregaInfo()!=null && !beanSolicitud.getFechaEntregaInfo().trim().equals(""))?"checked":"" %>>
						&nbsp;&nbsp;
							<html:text name="SolicitudesCertificadosForm"
								property="fechaEntregaInfo" styleClass="boxConsulta" readonly="true" 
								value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaEntregaInfo()) %>"
							size="10"></html:text></td> 
					</tr>
					<tr>
						<td class="labelText" colspan="4">
							<siga:Idioma key="certificados.solicitudes.literal.ultimoUsuMod" />
						&nbsp;&nbsp; 
							<span class="boxConsulta"><%=nombreUltimoUsuMod%></span>
						</td>
						<td class="labelText" colspan="4">
							<siga:Idioma key="certificados.solicitudes.literal.ultimaFechaMod" />
						&nbsp;&nbsp; 
							<span class="boxConsulta"> <%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(userBean.getLanguage(), beanSolicitud.getFechaMod()))%></span>
						</td>
					</tr>
				</table>
			</siga:ConjCampos> 
			<siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.sanciones">
				<table class="tablaCampos" align="center" border="0" cellspacing="0">
					<tr>
						<td width="25%">&nbsp;</td>
						<td width="25">&nbsp;</td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.textoSanciones" /></td>
						<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.comentario" /></td>
					</tr>
					<tr>
						<td><siga:ConjCampos leyenda="certificados.solicitudes.ventanaEdicion.estadosColegiales">
							<table class="tablaCampos" align="center" border="0" cellspacing="0">
								<tr>
									<td class="labelText"><siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirLiteratura" />
									<html:checkbox property="incluirLiteratura" disabled="<%=!modoEditar%>"/></td>
									<td></td>
								</tr>
								<tr>
									<td class="tdBotones">
									<% if (modificarSolicitud.equals("1")) { %> 
									<input type="button" width="200"
										alt="<siga:Idioma key="certificados.solicitudes.literal.copiarHistorico"/>"
										id="enviarSel" onclick="return copiarHistorico();"
										name="idButton" class="button"
										value="<siga:Idioma key="certificados.solicitudes.literal.copiarHistorico"/>">
									<%} else { %> 
									&nbsp; 
									<% } %>
									</td>
								</tr>
								<tr>
									<td class="tdBotones">
									<%
										if (modificarSolicitud.equals("1")) {
									%> <input type="button"
										alt="<siga:Idioma key="certificados.solicitudes.literal.historicoObservaciones"/>"
										id="enviarSel" onclick="return historicoObservaciones();"
										name="idButton" class="button" width="100"
										value="<siga:Idioma key="certificados.solicitudes.literal.CopiarHistObserv"/>">
									<%
										} else {
									%> &nbsp; <%
 	}
 %>
									</td>

								</tr>
							</table>
						</siga:ConjCampos></td>
						<td>&nbsp;</td>
						<td rowspan="2">
						<%
							if (modificarSolicitud.equals("1")) {
						%> <html:textarea
							property="textoSanciones" value="" styleClass="box"
							onKeyDown="cuenta(this,3500)" cols="130" rows="7"
							value="<%=sanciones%>" readonly="false" /> <%
 	} else {
 %> <html:textarea
							property="textoSanciones" value="" styleClass="boxConsulta"
							onKeyDown="cuenta(this,3500)" cols="130" rows="7"
							value="<%=sanciones%>" readonly="true" /> <%
 	}
 %>
						</td>
						<td rowspan="2">
						<%
							if (modificarSolicitud.equals("1")) {
						%> <html:textarea
							property="comentario" value="" styleClass="box"
							onKeyDown="cuenta(this,4000)" cols="130" rows="7"
							value="<%=beanSolicitud.getComentario()%>" readonly="false" /> <%
 	} else {
 %>
						<html:textarea property="comentario" value=""
							styleClass="boxConsulta" onKeyDown="cuenta(this,4000)" cols="130"
							rows="7" value="<%=beanSolicitud.getComentario()%>"
							readonly="true" /> <%
 	}
 %>
						</td>

					</tr>

					<%if(!isSolicitudColegio){ %>
					<tr>
					
					
						<td class="tdBotones">
						<%	
							if (modificarSolicitud.equals("1")) {
						%> <input type="button"
							alt="<siga:Idioma key="certificados.solicitudes.literal.copiarSanciones"/>"
							id="enviarSel" onclick="return copiarSanciones();"
							name="idButton" class="button"
							value="<siga:Idioma key="certificados.solicitudes.literal.copiarSanciones"/>">
						<%
							} else {
						%> &nbsp; <%
 	}
 %>
						</td>
						<td>&nbsp;</td>


					</tr>
					<%}%>
					<tr>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>

						<td>
						<table>
							<tr>
								<td class="labelText">
								<siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirDeudas" />
								</td>
								<td><html:checkbox property="incluirDeudas" disabled="<%=!modoEditar%>"/></td>
							</tr>
						</table>
						</td>
						<td>&nbsp;</td>
						<td>
						<table>
							<tr>

								<td class="labelText">
									<siga:Idioma key="certificados.solicitudes.ventanaEdicion.incluirSanciones" />
								</td>
								<td><html:checkbox property="incluirSanciones" disabled="<%=!modoEditar%>"/></td>
							</tr>
						</table>
						</td>
						<td></td>
					</tr>

				</table>



			</siga:ConjCampos></td>
	</html:form>

	
</table>

<siga:ConjBotonesAccion botones="<%=botones%>" modal="G" /></div>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
</body>
</html>