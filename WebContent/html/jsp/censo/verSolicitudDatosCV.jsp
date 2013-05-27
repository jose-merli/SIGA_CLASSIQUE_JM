<!-- verSolicitudDatosCV.jsp -->
<!-- 
	 Permite mostrar las solicitudes de modificacion de datos en el CV
	 VERSIONES:
	 miguel.villegas 26-01-2005 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenSolicitudModificacionCVBean"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<!-- JSP -->
<% 
	// Declaraciones varias
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Row modificada=new Row();
	Hashtable original= new Hashtable();
	boolean hayOriginal=false;
	
	// Obtencion de los valores originales de la direccion solicitada para modificar
	if (request.getAttribute("container_desc") != null){
		hayOriginal=true;
		Enumeration enumSel = ((Vector)request.getAttribute("container_desc")).elements();
		while (enumSel.hasMoreElements())
		{
			original = (Hashtable) enumSel.nextElement();
		} 
	}

	// Obtencion de los valores modifiacdos de la direccion solicitada
	if (request.getAttribute("container") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container")).elements();
		while (enumSel.hasMoreElements())
		{
			modificada = (Row) enumSel.nextElement();
		} 
	}	
	
	// RGG 14/03/2007 cambio para dar un tamaño a la letra y en caso de Tiems darle otro
	String fontSize = "13px";
	if (((String)src.get("font.style")).indexOf("Times")!=-1) {
		fontSize="15px";
	}
	
%>

<html>

	<!-- HEAD -->
	<head>

		<style type="text/css">
			.labelTextRojo{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; 	font-weight: bold; 
						margin: auto; color:#6A0000; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; vertical-align: top;}	
						
			.labelTextNormal{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; vertical-align: top; 
						margin: auto; color:#<%=src.get("color.labelText")%>; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; padding-left: 5px;}							
						
			.boxConsultaRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}						  
						
			.boxComboNormal {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						margin: auto; color:#<%=src.get("color.labelText")%>;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent;}

			.boxComboRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}

		</style>


		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			<!-- Asociada al boton Cerrar -->
			function accionCerrar(){ 			
				window.top.close();
			}	
			
			<!-- Asociada a la carga de la pagina -->
			function originalModificado(estado){ 			
				if (estado!="10"){
					var mensaje='<siga:Idioma key="messages.censo.solicitudesModificacion.advertencia"/>';
					alert(mensaje);
				}
			}
		
		</script>	

	</head  class="tablaCentralCampos">
	<body onLoad="originalModificado(<%=modificada.getString(CenSolicitudModificacionCVBean.C_IDESTADOSOLIC)%>)">
	<!-- Barra de titulo actualizable desde los mantenimientos -->

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulos">
					<siga:Idioma key="censo.solicitudModificacion.literal.titulo"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

		<!-- INICIO: CAMPOS -->
	
			<table class="tablaCentralCampos" align="center">			
				<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property = "idPersona" value =""/>
					<html:hidden property = "idInstitucion" value =""/>
					<html:hidden property = "solicitudes"/>
					<tr>				
						<td>
							<siga:ConjCampos leyenda="censo.consultaDatosCV.cabecera">
								<table class="tablaCampos" align="center">							
									<!-- FILA -->
									<tr>		
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCV.literal.tipo"/>&nbsp;
										</td>
										<td width="20%">
											<% if (hayOriginal){ %>
												<html:text property="tipoO" value='<%=String.valueOf(original.get("TIPOAPUNTE"))%>' styleClass="boxConsulta" readOnly="true"></html:text> <br>
											<% } else { %>
												&nbsp;<br>
											<% } %>
											<html:text property="tipoM" value='<%=modificada.getString("TIPOAPUNTE")%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>
										<td width="15%">
											<% if (hayOriginal){ %>
												<html:text property="tipoCVSubtipo1O" value='<%=String.valueOf(original.get("DESCSUBTIPO1"))%>' styleClass="boxConsulta" readOnly="true"></html:text> <br>
											<% } else { %>
												&nbsp;<br>
											<% } %>
											<html:text property="tipoCVSubtipo1M" value='<%=modificada.getString("DESCSUBTIPO1")%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>
										<td width="15%">
											<% if (hayOriginal){ %>
												<html:text property="tipoCVSubtipo2O" value='<%=String.valueOf(original.get("DESCSUBTIPO2"))%>' styleClass="boxConsulta" readOnly="true"></html:text> <br>
											<% } else { %>
												&nbsp;<br>
											<% } %>
											<html:text property="tipoCVSubtipo2M" value='<%=modificada.getString("DESCSUBTIPO2")%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>
									</tr>	
								</table>	
								<!-- FILA -->
								<table class="tablaCampos" align="center">																
									<tr>
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCV.literal.fechaInicio"/>&nbsp;
										</td>	
										<td width="35%">
											<% if (hayOriginal){ %>
												<html:text property="fechaIniO" value='<%=GstDate.getFormatedDateShort("",String.valueOf(original.get(CenDatosCVBean.C_FECHAINICIO)))%>' styleClass="boxConsulta" readOnly="true"></html:text> <br>
											<% } else { %>
												&nbsp;<br>
											<% } %>											
											<html:text property="fechaIniM" value='<%=GstDate.getFormatedDateShort("",modificada.getString(CenSolicitudModificacionCVBean.C_FECHAINICIO))%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>							
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCV.literal.fechaFin"/>&nbsp;					
										</td>
										<td>
											<% if (hayOriginal){ %>
												<html:text property="fechaFinO" value='<%=GstDate.getFormatedDateShort("",String.valueOf(original.get(CenDatosCVBean.C_FECHAFIN)))%>' styleClass="boxConsulta" readOnly="true"></html:text> <br>
											<% } else { %>
												&nbsp;<br>
											<% } %>
											<html:text property="fechaFinM" value='<%=GstDate.getFormatedDateShort("",modificada.getString(CenSolicitudModificacionCVBean.C_FECHAFIN))%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>
									</tr>
					   				<!-- FILA -->
					  				<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCV.literal.descripcion"/>&nbsp;
										</td>				
					   					<td colspan=3>
											<% if (hayOriginal){ %>
												<html:textarea property="descO" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value='<%=String.valueOf(original.get(CenDatosCVBean.C_DESCRIPCION))%>' styleClass="boxConsulta" readOnly="true" size="70"></html:textarea><br>
											<% } else { %>
												&nbsp;<br>
											<% } %>
											<html:textarea property="descM" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  value='<%=modificada.getString(CenSolicitudModificacionCVBean.C_DESCRIPCION)%>' styleClass="boxConsultaRojo" readOnly="true" size="70"></html:textarea>
										</td>
					  				</tr>
				   				</table>
							</siga:ConjCampos>
							<!-- MOTIVO -->
							<siga:ConjCampos leyenda="censo.datosCuentaBancaria.literal.motivo">
								<table class="tablaCampos" align="center">														
					  				<tr>
					   					<td width="10%" class="labelText">
											<siga:Idioma key="censo.datosCV.literal.motivo"/>&nbsp;
										</td>											
					   					<td width="90%">
											<html:textarea property="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" value="<%=modificada.getString(CenSolicitudModificacionCVBean.C_MOTIVO)%>" styleClass="boxConsulta" readOnly="true" size="80"  rows="5"></html:textarea>
										</td>		   					
					  				</tr>		  			 			 
				   				</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>
			</table>
			<!-- FIN: CAMPOS -->


			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->	
			
			<siga:ConjBotonesAccion botones="C" modal="M" clase="botonesDetalle"/>
			
			<!-- FIN: BOTONES REGISTRO -->	

			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>