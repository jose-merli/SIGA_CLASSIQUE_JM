<!-- loadingWindowPru.jsp -->
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<html>
<head>
<% String app = request.getContextPath(); %>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	
</head>
<body class="tableCabecera" onload="window.top.close()">
</body>
</html>