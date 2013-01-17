<!-- pestanaEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
 
<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean esComision=user.isComision();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	ses.setAttribute("pestanas","1");
	String[] pestanasOcultas = (String[])request.getAttribute("pestanasOcultas");
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/JGR_EJG.do" method="POST">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->
	<!-- TAG DE CONJUNTO DE PESTANAS -->
	
	<% if(esComision){%>
		<siga:PestanasExt 
				pestanaId="EJG" 
				target="mainPestanas"
				parametros="EJG"
				elementoactivo="9"
				procesosinvisibles="<%=pestanasOcultas%>"
				/>
	<%}else{%>
		<siga:PestanasExt 
				pestanaId="EJG" 
				target="mainPestanas"
				parametros="EJG"
				elementoactivo="1"
				procesosinvisibles="<%=pestanasOcultas%>"
				/>
	<%}%>

<!-- FIN: CONJUNTO DE PESTAÑAS (LINEA) -->

	</html:form> 

</div>
	<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas"
			
			>
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
