<!-- errorAnotacion.jsp -->
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
<%@ page import="com.siga.beans.ExpDenuncianteBean"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
</head>

<body>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.auditoria.literal.anotacion"/>
			</td>
		</tr>
	</table>		
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/EXP_Auditoria_Seguimiento.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<tr>				
	<td>	
	
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.anotacion">

		<table class="tablaCampos" align="center">
		
	
		<!-- FILA -->
		<tr>
			<td class="labelText" align="center">
				<br>
				<siga:Idioma key="expedientes.auditoria.literal.erroranotacion"/>
			</td>				
		</tr>				
		
		</table>

	</siga:ConjCampos>

	</td>
</tr>

</html:form>

</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="C" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.close();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
