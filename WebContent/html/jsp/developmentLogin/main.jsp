<!-- main.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	ses.removeAttribute(SIGAConstants.STYLESHEET_REF);
%>
	
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	</head>

	<body>
		<iframe src="<%=app%>/html/jsp/developmentLogin/login.jsp"
		
				id="mainWorkArea"
				name="mainWorkArea"
				scrolling="no"
				frameborder="0"
				class="posicionPrincipal">
		</iframe>
	</body>
</html>
