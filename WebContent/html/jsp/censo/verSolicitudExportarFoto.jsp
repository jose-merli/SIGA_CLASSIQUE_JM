<!DOCTYPE html>
<html>
<head>
<!-- verSolicitudExportarFoto.jsp -->
<!-- 
	 Permite mostrar las solicitudes de modificacion de datos generales del cliente
	 VERSIONES:
	 miguel.villegas 27-01-2005 
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
<%@ page import = "com.siga.beans.CenSolicitModifDatosBasicosBean"%>
<%@ page import = "com.siga.beans.CenClienteBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "java.util.ArrayList"%>
<%@ page import = "java.util.Enumeration"%>
<%@ page import = "java.util.Vector"%>
<%@ page import = "java.util.Hashtable"%>
<%@page import  = "com.siga.beans.CenSolicModifExportarFotoBean"%>

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

	// Creo un par de ArrayList con los valores de los codigos de los idiomas a mostrar
	ArrayList idiomaOrig=new ArrayList();	
	idiomaOrig.add(String.valueOf(original.get(CenClienteBean.C_IDLENGUAJE)));	
	ArrayList idiomaDest=new ArrayList();	
	idiomaDest.add(modificada.getString(CenSolicitModifDatosBasicosBean.C_IDLENGUAJE));		
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
		// Asociada al boton Cerrar
		function accionCerrar() { 			
			window.top.close();
		}	
					
		// Asociada a la carga de la pagina 
		function originalModificado(estado){ 			
			if (estado!="10"){
				var mensaje='<siga:Idioma key="messages.censo.solicitudesModificacion.advertencia"/>';
				alert(mensaje);
			}
		}	
	</script>
</head>

<body onLoad="originalModificado(<%=modificada.getString(CenSolicitModifDatosBasicosBean.C_IDESTADOSOLIC)%>)" class="tablaCentralCampos">
	<!-- Barra de titulo actualizable desde los mantenimientos -->

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="titulitosDatos" cellspacing="0">
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
					<siga:ConjCampos leyenda="censo.consultaDatosGenerales.cabecera">
						<table class="tablaCampos" align="center">							
							<tr>		
								<td width="20%" class="labelText">
									<siga:Idioma key="censo.consultaDatosGenerales.literal.usarFoto"/>&nbsp;
								</td>
								<td class="labelTextNormal">
									<% if (String.valueOf(original.get(CenClienteBean.C_EXPORTARFOTO)).equalsIgnoreCase("1")){%>
			   							<siga:Idioma key="general.yes"/>
				   					<% } else { %>
			   							<siga:Idioma key="general.no"/>
				   					<% } %>						   					
				   					<br>
				   					<div class="labelTextRojo">
										<% if (modificada.getString(CenSolicModifExportarFotoBean.C_EXPORTARFOTO).equalsIgnoreCase("1")){%>
				   							<siga:Idioma key="general.yes"/>
					   					<% } else { %>
				   							<siga:Idioma key="general.no"/>
					   					<% } %>
					   				</div>
								</td>
							</tr>	
		   				</table>
					</siga:ConjCampos>
					
					<siga:ConjCampos leyenda="censo.consultaDatosGenerales.literal.motivo">
						<table class="tablaCampos" align="center">														
			  				<tr>
			   					<td width="10%" class="labelText">
									<siga:Idioma key="censo.consultaDatosGenerales.literal.motivo"/>&nbsp;
								</td>											
			   					<td width="90%">
									<html:textarea property="motivo" 
										style="overflow-y:auto; overflow-x:hidden; width:880px; height:80px; resize:none;"
										value="<%=modificada.getString(CenSolicitModifDatosBasicosBean.C_MOTIVO)%>" 
										styleClass="boxConsulta" readOnly="true"></html:textarea>
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
	
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



	<siga:ConjBotonesAccion botones="C" modal="M" clase="botonesDetalle"/>
	
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>