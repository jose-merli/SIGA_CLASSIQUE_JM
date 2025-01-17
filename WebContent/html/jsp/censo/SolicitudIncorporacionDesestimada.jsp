<!DOCTYPE html>
<html>
<head>
<!-- SolicitudIncorporacionDesestimada.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld"     prefix="siga"     %>

<%@ page import="com.siga.beans.CenSolicitudIncorporacionBean" %>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudInstituBean" %>
<%@ page import="com.siga.beans.CenDocumentacionSolicitudBean" %>

<% 
	String app = request.getContextPath(); 
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


	<title><siga:Idioma key="censo.SolicitudIncorporacionValidacion.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>

<Table>
	<TR><TD class="labelText" align='center'><siga:Idioma key="messages.censo.solicitudIncorporacion.errorSolicitudDesestimada"/></TD></TR>
</table>

<br><br><br><br><br>

<siga:ConjBotonesAccion botones="V"  />

<script>
//Asociada al boton Volver -->
function accionVolver() 
{		
	window.location = "<%=app%>/html/jsp/censo/SolicitudIncorporacionValidacion.jsp";
}
</script>


</Body>
</html>