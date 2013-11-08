<!DOCTYPE html>
<html>
<head>
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
		
	Row modificada=new Row();
	Hashtable original= new Hashtable();
	boolean hayOriginal=false;
	
	// Obtencion de los valores originales de la direccion solicitada para modificar
	if (request.getAttribute("container_desc") != null){
		hayOriginal=true;
		Enumeration enumSel = ((Vector)request.getAttribute("container_desc")).elements();
		while (enumSel.hasMoreElements()) {
			original = (Hashtable) enumSel.nextElement();
		} 
	}

	// Obtencion de los valores modifiacdos de la direccion solicitada
	if (request.getAttribute("container") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container")).elements();
		while (enumSel.hasMoreElements()) {
			modificada = (Row) enumSel.nextElement();
		} 
	}	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Asociada al boton Cerrar
		function accionCerrar() { 			
			window.top.close();
		}	
		
		// Asociada a la carga de la pagina
		function originalModificado(estado) { 			
			if (estado!="10"){
				var mensaje='<siga:Idioma key="messages.censo.solicitudesModificacion.advertencia"/>';
				alert(mensaje);
			}
		}
	</script>	
</head>

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
							<table class="tablaCampos" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">							
								<tr>		
									<td class="labelText" width="110px">
										<siga:Idioma key="censo.datosCV.literal.tipo"/>&nbsp;
									</td>
									<td>
										<% if (hayOriginal){ %>
											<html:text property="tipoO" value='<%=String.valueOf(original.get("TIPOAPUNTE"))%>' styleClass="boxConsulta" readOnly="true"/>
										<% } else { %>
											&nbsp;
										<% } %>
										<br>
										<html:text property="tipoM" value='<%=modificada.getString("TIPOAPUNTE")%>' styleClass="boxConsultaRojo" readOnly="true" />
									</td>
									
									<td>
										<% if (hayOriginal){ %>
											<html:text property="tipoCVSubtipo1O" value='<%=String.valueOf(original.get("DESCSUBTIPO1"))%>' styleClass="boxConsulta" readOnly="true" />
										<% } else { %>
											&nbsp;
										<% } %>
										<br>
										<html:text property="tipoCVSubtipo1M" value='<%=modificada.getString("DESCSUBTIPO1")%>' styleClass="boxConsultaRojo" readOnly="true"/>
									</td>
									
									<td>
										<% if (hayOriginal){ %>
											<html:text property="tipoCVSubtipo2O" value='<%=String.valueOf(original.get("DESCSUBTIPO2"))%>' styleClass="boxConsulta" readOnly="true" />
										<% } else { %>
											&nbsp;
										<% } %>
										<br>
										<html:text property="tipoCVSubtipo2M" value='<%=modificada.getString("DESCSUBTIPO2")%>' styleClass="boxConsultaRojo" readOnly="true"/>
									</td>
								</tr>	

								<tr>
									<td class="labelText">
										<siga:Idioma key="censo.datosCV.literal.fechaInicio"/>&nbsp;
									</td>	
									<td>
										<% if (hayOriginal){ %>
											<html:text property="fechaIniO" value='<%=GstDate.getFormatedDateShort("",String.valueOf(original.get(CenDatosCVBean.C_FECHAINICIO)))%>' styleClass="boxConsulta" readOnly="true"/> 
										<% } else { %>
											&nbsp;
										<% } %>		
										<br>									
										<html:text property="fechaIniM" value='<%=GstDate.getFormatedDateShort("",modificada.getString(CenSolicitudModificacionCVBean.C_FECHAINICIO))%>' styleClass="boxConsultaRojo" readOnly="true"/>
									</td>
																
									<td class="labelText">
										<siga:Idioma key="censo.datosCV.literal.fechaFin"/>&nbsp;					
									</td>
									<td>
										<% if (hayOriginal){ %>
											<html:text property="fechaFinO" value='<%=GstDate.getFormatedDateShort("",String.valueOf(original.get(CenDatosCVBean.C_FECHAFIN)))%>' styleClass="boxConsulta" readOnly="true"/>
										<% } else { %>
											&nbsp;
										<% } %>
										<br>
										<html:text property="fechaFinM" value='<%=GstDate.getFormatedDateShort("",modificada.getString(CenSolicitudModificacionCVBean.C_FECHAFIN))%>' styleClass="boxConsultaRojo" readOnly="true"/>
									</td>
								</tr>

				  				<tr>
				   					<td class="labelText">
										<siga:Idioma key="censo.datosCV.literal.descripcion"/>&nbsp;
									</td>				
				   					<td colspan="3">
										<% if (hayOriginal){ %>
											<html:textarea property="descO" 
												style="overflow-y:auto; overflow-x:hidden; width:550px; height:40px; resize:none;"
												value='<%=String.valueOf(original.get(CenDatosCVBean.C_DESCRIPCION))%>' styleClass="boxConsulta" readOnly="true"></html:textarea>
										<% } else { %>
											&nbsp;
										<% } %>
										<br>
										<html:textarea property="descM" 
											style="overflow-y:auto; overflow-x:hidden; width:550px; height:40px; resize:none;"  
											value='<%=modificada.getString(CenSolicitudModificacionCVBean.C_DESCRIPCION)%>' styleClass="boxConsultaRojo" readOnly="true"></html:textarea>
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
										<html:textarea property="motivo" 
											style="overflow-y:auto; overflow-x:hidden; width:600px; height:80px; resize:none;"
											value="<%=modificada.getString(CenSolicitudModificacionCVBean.C_MOTIVO)%>" styleClass="boxConsulta" readOnly="true"></html:textarea>
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