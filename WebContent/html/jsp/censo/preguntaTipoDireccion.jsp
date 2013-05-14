<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ page import="com.atos.utils.*"%>

<%@page import="com.siga.censo.form.DireccionesForm;"%>
<html:html>
<head>
<%
	String app=request.getContextPath();
	
	String idDirecciones=(String)request.getAttribute("idDireccionesCensoWeb") ;
	String modo=(String)request.getAttribute("modo");
	String modificarPre=(String)request.getAttribute("idDireccionesPreferentes") ;
	String control=(String)request.getAttribute("control") ;



%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="messages.censo.direcciones.errorCensoWeb"/>';
			if (confirm(type)) {
			 
			  parent.document.consultaDireccionesForm.idDireccionesCensoWeb.value="<%=idDirecciones%>";
			  parent.document.consultaDireccionesForm.modificarPreferencias.value="<%=modificarPre%>";
			  parent.document.consultaDireccionesForm.control.value="<%=control%>";
			  parent.document.consultaDireccionesForm.modo.value="<%=modo%>";
			  parent.actualizarcenso();
			    
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/CEN_ConsultasDirecciones.do" method="POST" >
		    <html:hidden property = "modo" value = ""/>
		 
			
			
		 </html:form> 	
		

</body>
</html:html>