<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%
	String app=request.getContextPath();
	
	String idDirecciones=(String)request.getAttribute("idDireccionesPreferentes") ;
	String idpreferente=(String)request.getAttribute("idDireccionesCensoWeb") ;
	String modo=(String)request.getAttribute("modo");
	String control=(String)request.getAttribute("control") ;
	

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="messages.censo.direcciones.errorPreferentes"/>';
			if (confirm(type)) {
			 
			  parent.document.consultaDireccionesSolicForm.idDireccionesPreferentes.value="<%=idDirecciones%>";
			  parent.document.consultaDireccionesSolicForm.idDireccionesCensoWeb.value="<%=idpreferente%>";
			  parent.document.consultaDireccionesSolicForm.modo.value="<%=modo%>";
			  parent.actualizar();
			    
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/CEN_SolicitudDirecciones.do" method="POST" >
		    <html:hidden property = "modo" value = ""/>
		 
			
			
		 </html:form> 	
		

</body>
</html>