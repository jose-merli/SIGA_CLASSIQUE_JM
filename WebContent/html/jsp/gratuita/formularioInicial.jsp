<!-- formularioInicial.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsRetencionesBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.insertarRetencion.literal.retencionesIrpf" 
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
</head>

<body onload="ajusteAlto('resultado');">

	<fieldset>
	<table class="tablaCentralCampos" align="center">

	<html:form action="/SolicitudRetencioAction.do" method="POST" target="resultado">
	<input type="hidden" name="modo" value="inicio">
	<input type="hidden" name="actionModal" value="">
  		
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.descripcion"/>
	</td>
	<td class="labelText">
		<html:text name="SolicitudRetencionForm" property="descripcion" size="60" maxlength="60" styleClass="box" value=""></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.retencion"/>
	</td>
	<td class="labelText">
		<html:text name="SolicitudRetencionForm" property="retencion" size="5" maxlength="5" styleClass="box"  value=""></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.retenciones.letraNifSociedad"/>
	</td>
	<td class="labelText">
		<html:text name="SolicitudRetencionForm" property="letraNifSociedad" size="1" maxlength="1" styleClass="box"  value=""></html:text>
	</td>
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
		
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.retenciones.consultaRetenciones" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			document.forms[0].descripcion.value = trim(document.forms[0].descripcion.value);
			document.forms[0].retencion.value = trim(document.forms[0].retencion.value);
			document.forms[0].letraNifSociedad.value = trim(document.forms[0].letraNifSociedad.value);
			
			var nif = document.forms[0].letraNifSociedad.value;
			var retencion = document.forms[0].retencion.value;
			if (isNaN(nif) || (nif == 0)) {
				if (isNaN(retencion)) {
					alert('<siga:Idioma key="gratuita.formularioInicial.message.errorIRPF"/>');
				}
				else {
					document.forms[0].modo.value = "buscar";
					document.forms[0].submit();
				}
			}
			else alert("Error nif");
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>