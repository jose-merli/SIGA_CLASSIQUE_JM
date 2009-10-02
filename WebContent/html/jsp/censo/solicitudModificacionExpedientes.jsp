<!-- solicitudModificacionExpedientes.jsp -->
<!-- 
	 Edita la solicitud de modificacion de expedientes
	 VERSIONES:
	 miguel.villegas 19-01-2005 
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.beans.CenSolModiFacturacionServicioBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	String nombre = (String)request.getAttribute("nombre");
	String numero = (String)request.getAttribute("numero");
	String idPersona = (String)request.getAttribute("idPersona");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String idInstitucionTipoExpediente = (String)request.getAttribute("idInstitucionTipoExpediente");
	String idTipoExpediente = (String)request.getAttribute("idTipoExpediente");
	String numeroExpediente = (String)request.getAttribute("numeroExpediente");
	String anhoExpediente = (String)request.getAttribute("anhoExpediente");

%>	
<html>

<!-- HEAD -->
	<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="ExpedientesForm" staticJavascript="false"/>  
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
		
	</head>

	<body>
	
		<!-- TITULO -->		
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaExpedientes.titulo"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				     <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>
	
	
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- dentro de esta capa se tienen que situar los diferentes componentes 
		 que se van a mostrar, para que quepen dentro de la ventana.
		 Los elementos que copieis dentro, que tengan el estilo 
		 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
	-->
	<div id="camposRegistro" class="posicionModalPeque" align="center">
	
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
	
		<table  class="tablaCentralCamposPeque"  align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.consultaExpedientes.titulo2">
						<html:form action="/CEN_Expedientes.do" method="POST" target="submitArea">
							<html:hidden property = "modo" value = ""/>
							<html:hidden property = "idPersona" value ="<%=idPersona%>"/>
							<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
							<html:hidden property = "idInstitucionTipoExpediente" value ="<%=idInstitucionTipoExpediente%>"/>
							<html:hidden property = "idTipoExpediente" value ="<%=idTipoExpediente%>"/>
							<html:hidden property = "numeroExpediente" value ="<%=numeroExpediente%>"/>
							<html:hidden property = "fechaExpediente" value ="<%=anhoExpediente%>"/>
					 		<table width="80%">
								<tr>							
									<td width="15%" class="labelText">
										<siga:Idioma key="censo.consultaExpedientes.literal.motivo"/>&nbsp;(*)
									</td>
									<td width="85%" class="labelText">
										<html:textarea cols="50" rows="4" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" property="motivo" styleClass="box"></html:textarea>			   	
									</td>
								</tr>
							</table>	
						</html:form>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
		<!-- FIN: CAMPOS -->
	
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->
	
		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
			 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
			 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
			 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
			 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
			 La propiedad modal dice el tamanho de la ventana (M,P,G)
		-->
	
			<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	
		<!-- FIN: BOTONES REGISTRO -->
	
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
	 
			//Asociada al boton GuardarCerrar
			function accionGuardarCerrar() 
			{		
				sub();
				if (validateExpedientesForm(document.forms[0])) 
				{
					document.forms[0].modo.value="solicitarModificacion";
					document.forms[0].submit();
				}else{
					fin();
					return false;
				}
			}
	
			//Asociada al boton Cerrar
			function accionCerrar() 
			{		
				top.cierraConParametros("NORMAL");
			}
	
			//Asociada al boton Restablecer 
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
	
	
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
	</div>
	
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	</body>
</html>
