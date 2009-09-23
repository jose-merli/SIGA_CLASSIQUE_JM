<!-- descargarFichero.jsp -->

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

<!-- IMPORTS -->


<!-- JSP -->
<%  
	String app=request.getContextPath();
	String rutaFichero = (String)request.getAttribute("rutaFichero");
	String nombreFichero = (String)request.getAttribute("nombreFichero");
	String borrarFichero = (String)request.getAttribute("borrarFichero");
	
%>	

<%@page import="java.util.Properties"%>
<html>

<!-- HEAD -->
<head>

	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script>
		function init(){
			document.forms[0].submit();
		}
	</script>

</head>


<body onload="init();">

	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea22">
		<html:hidden property = "accion" value = "descargarFichero"/>
		<html:hidden property = "nombreFichero" value = "<%=nombreFichero%>"/>
		<html:hidden property = "rutaFichero" value = "<%=rutaFichero%>"/>
		<%if(borrarFichero!=null){%>
            <html:hidden property = "borrarFichero" value = "<%=borrarFichero%>"/>
        <%}%>    
	</html:form>
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea22" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	<script>
		fin(window.parent.parent.document);
	</script>
</body>

</html>