<!-- abrirEjecucionSancion.jsp -->
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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	boolean nuevo =  (request.getAttribute("nuevo")!=null)?true:false;
	String insertado = UtilidadesString.getMensajeIdioma(userBean, "messages.inserted.success");
	String actualizado = UtilidadesString.getMensajeIdioma(userBean, "messages.updated.success");
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script>
			function abrirModal() {
				<%if (nuevo){%>
					alert('<%=insertado%>');
				<%}else{%>
					alert('<%=actualizado%>');
				<%}%>
				var result=ventaModalGeneral("EjecucionSancionForm","P");
				parent.refrescarLocal();
			}
		</script>

	</head>


<body onLoad="abrirModal();" >

	<html:form action="/EXP_Auditoria_EjecucionSancion.do" method="POST" target="submitArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="">
	</html:form>

</body>

</html>

