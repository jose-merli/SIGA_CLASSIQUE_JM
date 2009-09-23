<!-- blank.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% String app=request.getContextPath(); %>

<html>
	<head>
	    <title><siga:Idioma key="index.title"/></title>
	    
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	    
	    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	</head>

<!--	<body class ='tablaCentralCampos' background="/SIGA/html/imagenes/fondo2.gif"> -->
	<body class ='tablaCentralCampos' >
	</body>
</html>