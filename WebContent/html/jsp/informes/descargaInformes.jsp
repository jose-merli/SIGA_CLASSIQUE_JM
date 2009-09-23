<!-- descargaInformes.jsp -->
<%-- 
	 VERSIONES:
	 david.sanchezp_ creado el  30-05-2005.
--%>

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.informes.form.MantenimientoInformesForm "%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	String mensaje = "";
	String rutaFichero = request.getAttribute("rutaFicheroPDF")==null?"":(String)request.getAttribute("rutaFicheroPDF");
	String generacionPdfOK = request.getAttribute("generacionPdfOK")==null?"":(String)request.getAttribute("generacionPdfOK");
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

</head>


<body>
<% if (mensaje!=null && !mensaje.equals("")) { %>

	<script>
		alert('<%=mensaje%>');
	</script>
	
<% } else { %>

<div id="camposRegistro" class="posicionBusqueda" align="center">

	<table  class="tablaCentralCampos"  align="center">

	<html:form action="/INF_FichaPago.do" method="POST" target="submitArea">
	<html:hidden name="mantenimientoInformesForm" property = "modo" value = "descarga"/>
	<html:hidden name="mantenimientoInformesForm" property = "rutaFichero" value = "<%=rutaFichero%>"/>

	<tr>
	<td class="labelText">
		<%-- Si todo ha ido bien muestro mensaje de OK y boton de descarga: --%>
		<% String boton = UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"general.boton.download"); %>
		<br>
		<p align="center">
		 <% if  (generacionPdfOK!=null && generacionPdfOK.equalsIgnoreCase("OK")) { %>
		 		<siga:Idioma key="messages.informes.generado"/>
		</p>
		<br>
		<table align="center">
		<tr>
			<td class="tdBotones">
				<input type="button" alt="<%=boton %>"  id="deleteButton" onclick="return descargar();" class="button" value="<%=boton %>">
			</td>
		</tr>
		</table>
		<br>
		<%-- Si no se ha podido generar el informe aviso con un mensaje: --%>
		<% } else { %>
		 		<siga:Idioma key="messages.informes.errorGeneracion"/>
		 		&nbsp;
		 		<br>
		 		<br>
		<% } %>		
	</td>
	</tr>

	</html:form>

	</table>

<script>

	function descargar() {
		document.forms[0].modo.value="descargar";
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