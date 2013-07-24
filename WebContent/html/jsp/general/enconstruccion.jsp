<!DOCTYPE html>
<html>
<head>
<!-- enconstruccion.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<% String app = request.getContextPath(); %>




	<title>Página en construcción</title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

</head>

<body>
	<table align="center" border="0" height="100%" width="100%">
		<tr>
			<td valign="middle" align="center">
				<p class="titulitos"><siga:Idioma key="general.mensaje.enconstruccion"/></p>
			</td>
		</tr>
	</table>

</body>
</html>