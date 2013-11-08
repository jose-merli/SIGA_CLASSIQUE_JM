<!DOCTYPE html>
<html>
<head>
<!-- loadingWindow.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri="c.tld" prefix="c"%>


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>


<body style="cursor:wait" onBlur="window.focus();">
<c:set var="msg" value="messages.loadingpage" />
<c:if test="${!empty param.msg && param.msg!='null'}">
	<c:set var="msg" value="${param.msg}" />
</c:if>

<span align="Left" class="labelText"><siga:Idioma key="${msg}"/></span>

<div id="divEspera" title="<siga:Idioma key="${msg}"/>" style="z-index:100;position:absolute;top:45%;left:50%">
	<div style="position:relative;left:-50%">
		<br><img src="<html:rewrite page='/html/imagenes/loadingBar.gif'/>">
	</div
</div>
	
</body>
</html>