<!DOCTYPE html>
<html>
<head>
<!-- verSolicitudDirecciones.jsp -->
<!-- 
	 Permite mostrar las solicitudes de modificacion de direcciones
	 VERSIONES:
	 miguel.villegas 25-01-2005 
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
<%@ page import = "com.siga.beans.CenSoliModiDireccionesBean"%>
<%@ page import = "com.siga.beans.CenDireccionesBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>

<!-- JSP -->
<% 
	// Declaraciones varias
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Row modificada=new Row();
	Hashtable original= new Hashtable();
	
	// Obtencion de los valores originales de la direccion solicitada para modificar
	if (request.getAttribute("container_desc") != null){
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
	
	String paisOriginal="";
	String paisModificado="";
	String poblacionOriginal="";
	String poblacionModificado="";
	if (original!=null){
		 paisOriginal=String.valueOf(original.get("PAIS"));
		paisModificado=modificada.getString("PAIS");
		poblacionOriginal=String.valueOf(original.get("POBLACION"));
		poblacionModificado=modificada.getString("POBLACION");
		if (!String.valueOf(original.get("IDPAIS")).equals(ClsConstants.ID_PAIS_ESPANA)){
			poblacionOriginal=String.valueOf(original.get("POBLACIONEXTRANJERA"));
		}	
		if (!modificada.getString("IDPAIS").equals(ClsConstants.ID_PAIS_ESPANA)){
			poblacionModificado=modificada.getString("POBLACIONEXTRANJERA");
		}	
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		var idEspana='<%=ClsConstants.ID_PAIS_ESPANA%>';

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

<body onLoad="originalModificado(<%=modificada.getString(CenSoliModiDireccionesBean.C_IDESTADOSOLIC)%>)">
<!-- Barra de titulo actualizable desde los mantenimientos -->

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulos">
				<siga:Idioma key="censo.solicitudModificacion.literal.titulo"/>
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	<div id="camposRegistro" class="posicionModalGrande" align="center">

		<!-- INICIO: CAMPOS -->
		<table class="tablaCentralCamposGrande" align="center">			
		
			<% if (original==null) { %>
		 		<tr class="notFound">
		   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
				
			<% } else { %>	 	
				<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property = "idPersona" value =""/>
					<html:hidden property = "idInstitucion" value =""/>
					<html:hidden property = "solicitudes"/>
					<tr>				
						<td>
							<siga:ConjCampos leyenda="censo.consultaDirecciones.cabecera">
								<table class="tablaCampos" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>		
										<td class="labelText" width="100px" rowspan="2">
											<siga:Idioma key="censo.datosDireccion.literal.direccion"/>&nbsp;
										</td>
										<td>
											<html:textarea property="domicilioO" 
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:40px; resize:none;"
												value="<%=String.valueOf(original.get(CenDireccionesBean.C_DOMICILIO))%>" 
												styleClass="boxConsulta" readOnly="true"></html:textarea>
										</td>
																
										<td class="labelText" width="100px" rowspan="2">
											<siga:Idioma key="censo.datosDireccion.literal.cp"/>&nbsp;
										</td>	
										<td>
											<html:text property="codigoPostal" value="<%=String.valueOf(original.get(CenDireccionesBean.C_CODIGOPOSTAL))%>" styleClass="boxConsulta" readOnly="true" />																						
										</td>							
									</tr>	
									
									<tr>
										<td>
											<html:textarea property="domicilioM"  
												style="overflow-y:auto; overflow-x:hidden; width:350px; height:40px; resize:none;" 
												value="<%=modificada.getString(CenSoliModiDireccionesBean.C_DOMICILIO)%>" 
												styleClass="boxConsultaRojo" readOnly="true"></html:textarea>
										</td>
										<td>
											<html:text property="codigoPostal" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_CODIGOPOSTAL)%>" styleClass="boxConsultaRojo" readOnly="true" />
										</td>
									</tr>
									
									<tr>		
										<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.pais2"/>
										</td>
										<td>
											<html:text property="paisO" value="<%=paisOriginal%>" styleClass="boxConsulta" readOnly="true" size="40" /> 
											<br>
											<html:text property="paisM" value="<%=paisModificado%>" styleClass="boxConsultaRojo" readOnly="true" size="40" />
										</td>						
									</tr>	
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.provincia"/>&nbsp;					
										</td>
										<td>
											<html:text property="provinciaO" value='<%=String.valueOf(original.get("PROVINCIA"))%>' styleClass="boxConsulta" readOnly="true" />
											<br>
											<html:text property="provinciaM" value='<%=modificada.getString("PROVINCIA")%>' styleClass="boxConsultaRojo" readOnly="true" />											
										</td>
										
										<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>&nbsp;
										</td>
										<td>									
											<html:text property="poblacionO" value='<%=poblacionOriginal%>' styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="poblacionM" value='<%=poblacionModificado%>' styleClass="boxConsultaRojo" readOnly="true"/>
										</td>
									</tr>
									
					  				<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.telefono1"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="telef1O" value="<%=String.valueOf(original.get(CenDireccionesBean.C_TELEFONO1))%>" styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="telef1M" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_TELEFONO1)%>" styleClass="boxConsultaRojo" readOnly="true"/>
					   					</td>			   	
					   					
					   					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.telefono2"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="telef2O" value="<%=String.valueOf(original.get(CenDireccionesBean.C_TELEFONO2))%>" styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="telef2M" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_TELEFONO2)%>" styleClass="boxConsultaRojo" readOnly="true"/>
					   					</td>	
					  				</tr>
					  				
									<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.movil"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="movilO" value="<%=String.valueOf(original.get(CenDireccionesBean.C_MOVIL))%>" styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="movilM" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_MOVIL)%>" styleClass="boxConsultaRojo" readOnly="true"/>
					   					</td>	
					   					
					   					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.fax1"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="fax1O" value="<%=String.valueOf(original.get(CenDireccionesBean.C_FAX1))%>" styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="fax1M" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_FAX1)%>" styleClass="boxConsultaRojo" readOnly="true"/>
										</td>
					 				</tr>
					 				
					  				<tr>
					  				 	<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.fax2"/>&nbsp;
										</td>				
					   					<td colspan="3">
											<html:text property="fax2O" value="<%=String.valueOf(original.get(CenDireccionesBean.C_FAX2))%>" styleClass="boxConsulta" readOnly="true"/>
											<br>
											<html:text property="fax2M" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_FAX2)%>" styleClass="boxConsultaRojo" readOnly="true"/>
					   	 		   		</td>
					  				</tr>
					  				
					 				<tr>	
					   					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.correo"/>&nbsp;										
										</td>				
					   					<td>
											<html:text property="mailO" value="<%=String.valueOf(original.get(CenDireccionesBean.C_CORREOELECTRONICO))%>" styleClass="boxConsulta" readOnly="true" style="width:350px;"/>
											<br>
											<html:text property="mailM" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_CORREOELECTRONICO)%>" styleClass="boxConsultaRojo" readOnly="true" style="width:350px;"/>
					   			   		</td>	
					   			   		
					  					<td class="labelText">
											<siga:Idioma key="censo.datosDireccion.literal.paginaWeb"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="webO" value="<%=String.valueOf(original.get(CenDireccionesBean.C_PAGINAWEB))%>" styleClass="boxConsulta" readOnly="true" style="width:350px;"/>
											<br>
											<html:text property="webM" value="<%=modificada.getString(CenSoliModiDireccionesBean.C_PAGINAWEB)%>" styleClass="boxConsultaRojo" readOnly="true" style="width:350px;"/>
					  				 	</td>			   	
					  				</tr>

					  			 	 <tr>
							  			<td class="labelText">
							  				<siga:Idioma key="censo.datosDireccion.literal.preferente"/>&nbsp;
							  			</td>
									  	<td colspan="3">
										  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
										  		<tr>
								   					<td class="labelTextNormal">
														<% if (String.valueOf(original.get(CenDireccionesBean.C_PREFERENTE)).indexOf("E")!=-1){%>
									   						<siga:Idioma key="censo.preferente.mail"/>
									   					<% } %>
									   					<br>
									   					<div class="labelTextRojo">
															<% if (modificada.getString(CenSoliModiDireccionesBean.C_PREFERENTE).indexOf("E")!=-1){%>
										   						<siga:Idioma key="censo.preferente.mail"/>
										   					<% } %>
										   				</div>
								  					</td>
								  					
								   					<td class="labelTextNormal">
														<% if (String.valueOf(original.get(CenDireccionesBean.C_PREFERENTE)).indexOf("C")!=-1){	%>
								   							<siga:Idioma key="censo.preferente.correo"/>
									   					<% } %>
									   					<br>
									   					<div class="labelTextRojo">									   					
															<% if (modificada.getString(CenSoliModiDireccionesBean.C_PREFERENTE).indexOf("C")!=-1){%>
										   						<siga:Idioma key="censo.preferente.correo"/>
										   					<% } %>
										   				</div>
								  					</td>
								  					
								   					<td class="labelTextNormal">
														<% if (String.valueOf(original.get(CenDireccionesBean.C_PREFERENTE)).indexOf("F")!=-1){%>
								   							<siga:Idioma key="censo.preferente.fax"/>
									   					<% } %>
									   					<br>
									   					<div class="labelTextRojo">
															<% if (modificada.getString(CenSoliModiDireccionesBean.C_PREFERENTE).indexOf("F")!=-1){%>
										   						<siga:Idioma key="censo.preferente.fax"/>
										   					<% } %>
										   				</div>
					  								</td>
										  		</tr>
										  	</table>
									  	</td>
					  				</tr>					  
				   				</table>
							</siga:ConjCampos>
							
							<siga:ConjCampos leyenda="censo.datosDireccion.literal.motivo">
								<table class="tablaCampos" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">														
					  				<tr>
					   					<td class="labelText" width="10%">
											<siga:Idioma key="censo.datosDireccion.literal.motivo"/>&nbsp;
										</td>											
					   					<td>
											<html:textarea property="motivo"
												style="overflow-y:auto; overflow-x:hidden; width:880px; height:80px; resize:none;"
												value="<%=modificada.getString(CenSoliModiDireccionesBean.C_MOTIVO)%>" 
												styleClass="boxConsulta" readOnly="true"></html:textarea>
										</td>		   					
					  				</tr>		  			 			 
				   				</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>
			<%} %>
		</table>
		<!-- FIN: CAMPOS -->

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->			
		<siga:ConjBotonesAccion botones="C" modal="G"/>
		<!-- FIN: BOTONES REGISTRO -->	
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>