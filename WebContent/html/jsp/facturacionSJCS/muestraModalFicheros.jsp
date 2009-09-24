<!-- resumenPagos.jsp -->
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
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.facturacionSJCS.form.ResumenPagosForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	
	

	
%>
<html>

<!-- HEAD -->
<head>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script language="JavaScript">
		
			function cargaPagina() {
					var formulario = "ResumenPagosForm";
					ventaModalGeneral(formulario,"P");
			}
		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="cargaPagina();">

			<html:form action="/JGR_ResumenPagos.do" method="POST">
				<html:hidden name="ResumenPagosForm" property = "facturacion"/>
				<input type="hidden" name="modo" value="onloadDescargaFicheros">
  				<input type="hidden" name="actionModal" value="">
  				<html:hidden name="ResumenPagosForm" property = "idPersona"/>
  				<html:hidden name="ResumenPagosForm" property = "pago"/>	
			</html:form>

</body>
</html>
