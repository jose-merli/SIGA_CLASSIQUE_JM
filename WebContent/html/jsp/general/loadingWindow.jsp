<!-- loadingWindow.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri="c.tld" prefix="c"%>
<html>
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>


<body style="cursor:wait" onBlur="window.focus();">
<c:set var="msg" value="messages.loadingpage" />
<c:if test="${!empty param.msg && param.msg!='null'}">
	<c:set var="msg" value="${param.msg}" />
</c:if>

<table align="center" border="0" height="100%" width="100%">
<tr><td align="center" class="labelText"><siga:Idioma key="${msg}"/></td></tr>
<tr><td align="center"><img src="<html:rewrite page='/html/imagenes/loadingBar.gif'/>"></td></tr>
</table>
</body>
</html>