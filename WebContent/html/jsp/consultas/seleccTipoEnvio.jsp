<!DOCTYPE html>
<html>
<head>
<!-- seleccTipoEnvio.jsp --->
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
<%@ page import="com.siga.beans.ConConsultaBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Vector datos = (Vector)request.getAttribute("datos");
	String errorTipo = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.tipoEnvio");
		
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="NuevoExpedienteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			
</head>

<body>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.plantillas.literal.tipoenvio"/>
			</td>
		</tr>
	</table>		
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="mainWorkArea">			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>	
			<html:hidden property = "actionModal" value = ""/>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="envios.plantillas.literal.tipoenvio">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

			<td class="labelText" width="30%">
				<siga:Idioma key="envios.plantillas.literal.tipoenvio"/>
			</td>				
			<td>		
				<siga:ComboBD nombre = "tipoEnvio" tipo="cmbTipoEnvios" clase="boxCombo" obligatorio="true"/>										
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

		<siga:ConjBotonesAccion botones="A,X" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton GuardarCerrar -->
		function accionAceptar() 
		{		
			if (document.forms[0].tipoEnvio.value==""){
				alert('<%=errorTipo%>');	
				return;		
			}
			top.cierraConParametros(document.forms[0].tipoEnvio.value);
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCancelar() 
		{		
			top.cierraConParametros("VACIO");
		}		
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>