<!DOCTYPE html>
<html>
<head>
<!-- ventanaUsuario.jsp -->

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

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String nombreUsuario = "", nombreInstitucion = "", nifUsuario = "", nombreGrupo = "";
	Hashtable h = userBean!=null?userBean.getDatosUsuario():null;
	if (h != null) {
		nifUsuario        = (String)h.get("NIF_USUARIO");
		nombreUsuario     = (String)h.get("NOMBRE_USUARIO");
		nombreGrupo       = (String)h.get("NOMBRE_GRUPO");
		nombreInstitucion = (String)h.get("NOMBRE_INSTITUCION");
	}
%>


<!-- HEAD -->

<title><siga:Idioma key="general.ventana.cgae"/> - <siga:Idioma key="general.boton.usuario"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Cerrar -->
		function accionCerrar(){ 
			window.top.close();
		}	
	</script>	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32" width="100%">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma key="general.usuario.cabecera"/></td>
		</tr>
	</table>


	<fieldset >
		<table class="tablaCampos" border="0" >	
			<tr>
				<td class="labelText" width="25%"><siga:Idioma key="general.usuario.literal.nombre"/></td>
				<td width="80%"><span class="boxConsulta"><%=UtilidadesString.mostrarDatoJSP(nombreUsuario)%></span></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="general.usuario.literal.dni"/></td>
				<td><span class="boxConsulta"><%=UtilidadesString.mostrarDatoJSP(nifUsuario)%></span></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="general.usuario.literal.grupos"/></td>
				<td><span class="boxConsulta"><%=UtilidadesString.mostrarDatoJSP(nombreGrupo)%></span></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="general.usuario.literal.institucion"/></td>
				<td><span class="boxConsulta"><%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%></span></td>
			</tr>
		</table>
	</fieldset>


	<siga:ConjBotonesAccion botones='C' modo=''  modal="P"/>
	

</body>
</html>
