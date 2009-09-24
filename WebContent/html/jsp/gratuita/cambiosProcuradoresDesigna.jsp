<!-- cambiosProcuradoresDesigna.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getAttribute("resultado");
	request.getSession().setAttribute("resultado",obj);
	String accion = (String)request.getAttribute("accion");
	String boton="";
	String modo = (String) ses.getAttribute("Modo");
	
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.defendidosDesigna.literal.titulo" 
		localizacion="gratuita.defendidosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	

</head>

<body onLoad="ajusteAltoBotones('resultado');" class="tablaCentralCampos">
	<html:form action="JGR_CambioProcuradorDesigna.do" method="POST" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
	</html:form>	

	<iframe align="center" src="<%=app%>/html/jsp/gratuita/listarCambiosProcuradoresDesigna.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
		<siga:ConjBotonesAccion botones="N,V" clase="botonesDetalle" modo="<%=modo%>" />
<!-- INICIO: SUBMIT AREA -->

<script language="JavaScript">

	
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionNuevo() 
		{	
			document.forms[0].target="submitArea";
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') buscar();
		}
		
		function buscar()
		{
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
		
		function refrescarLocal()
		{
			buscar();
		}
		
</script>		

<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
		  
		
