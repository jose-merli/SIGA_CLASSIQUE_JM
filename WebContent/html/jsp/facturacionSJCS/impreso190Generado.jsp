<!DOCTYPE html>
<html>
<head>
<!-- impreso190Generado.jsp -->
<% 
//	 VERSIONES:
//	 raul.ggonzalez 06-04-2005 creacion
%>

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
<%@ page import="com.siga.facturacionSJCS.form.GenerarImpreso190Form"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	// locales
	GenerarImpreso190Form formulario = (GenerarImpreso190Form)request.getAttribute("generarImpreso190Form");
	String mensaje = (String)request.getAttribute("mensaje");
	String logError = (String)request.getAttribute("logError");
			
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

</head>



	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<% if (mensaje!=null && !mensaje.equals("") && logError!=null && logError.equals("NO")) { %>
<body>
	<script>
		alert('<%=mensaje%>');
	</script>
	
<% } else { %>
<body onload="descargar();">



<div id="camposRegistro" class="posicionBusqueda" align="center">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">

	<html:form action="/FCS_GenerarImpreso190.do" method="POST" target="submitArea">
	<html:hidden name="generarImpreso190Form" property = "modo" value = ""/>
	<html:hidden name="generarImpreso190Form" property = "borrarFichero" value = "true"/>

	<html:hidden name="generarImpreso190Form" property="anio"/>
	<html:hidden name="generarImpreso190Form" property="nombreFichero"/>
	<html:hidden name="generarImpreso190Form" property="codigoProvincia"/>						
	<html:hidden name="generarImpreso190Form" property="soporte"/>
	<html:hidden name="generarImpreso190Form" property="telefonoContacto"/>
	<html:hidden name="generarImpreso190Form" property="nombreContacto"/>
	<html:hidden name="generarImpreso190Form" property="apellido1Contacto"/>
	<html:hidden name="generarImpreso190Form" property="apellido2Contacto"/>

	<tr>
	<td class="labelText">

<% String boton = UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"general.boton.download"); %>

		<br>
		<br>
		<br>
		<br>
		<p align="center">
			<% if (mensaje!=null && !mensaje.equals("") && logError!=null && logError.equals("SI")) { %>
			<siga:Idioma key="<%=mensaje%>"/>
			<% } else { %>
			<siga:Idioma key="messages.factSJCS.generadoImpreso190"/>
			<% } %>
		</p>
		<br>
		<table align="center">
		<tr>
		
			<td class="tdBotones">
				<!--input type="button" alt="<%=boton%>"  id="deleteButton" onclick="return descargar();" class="button" value="<%=boton%>"-->
			</td>
		</tr>
		</table>
		<!--p align="center"><siga:Idioma key="messages.factSJCS.notaImpreso190"/></p-->
						
	</td>
	</tr>

	</html:form>

	</table>

<script>
	function descargar() {
		document.forms[0].modo.value="download";
		document.forms[0].submit();
	}
</script>

</div>	
<% } %>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>