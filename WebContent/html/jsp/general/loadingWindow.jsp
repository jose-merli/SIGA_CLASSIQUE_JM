<!-- loadingWindow.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri="c.tld" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
</head>


<body style="cursor:wait" onBlur="window.focus();">
<c:set var="msg" value="messages.loadingpage" />
<c:if test="${!empty param.msg && param.msg!='null'}">
	<c:set var="msg" value="${param.msg}" />
</c:if>

<table align="center" border="0" height="100%" width="100%">
<tr><td align="center" class="labelText"><siga:Idioma key="${msg}"/></td></tr>
<tr><td align="center"><img src="<html:rewrite page='/html/imagenes/loading.gif'/>"></td></tr>
</table>
</body>
</html>