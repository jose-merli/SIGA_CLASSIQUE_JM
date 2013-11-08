<!DOCTYPE html>
<html>
<head>
<!-- retencionesIRPFIndex.jsp -->
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
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	//Datos del Colegiado si procede:
	String nombrePestanha = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
	String numeroPestanha = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "0";
	if (entrada!=null && entrada.equals("2"))
		alto = "16";
%>
	


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script language="JavaScript" type="text/javascript">
		function refrescarLocal() {
			document.forms[0].submit();
		}
	</script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt
		titulo="censo.fichaCliente.sjcs.retencionesIRPF.cabecera"
		localizacion="censo.fichaCliente.sjcs.retencionesIRPF.localizacion" />
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');refrescarLocal();">
    
	</table>
	<br>
	
	<html:form action = "/JGR_PestanaRetencionesIRPF" method="POST" target="resultado" enctype="multipart/form-data" style="display:none">
		<input type="hidden" name="modo" value="buscarPor">
	</html:form>
	
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
	<!-- SUBMIT AREA -->
	<iframe name="submitArea" style="display:none">
	</iframe>
	
</body>
</html>
