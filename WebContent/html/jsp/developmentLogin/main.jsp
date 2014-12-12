<!DOCTYPE html>
<html style="background-color:#ced9e6;">
<head>
<!-- main.jsp -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="org.redabogacia.sigaservices.app.util.PropertyReader"%>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>
<%@ page import="java.util.Properties"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	ses.removeAttribute(SIGAConstants.STYLESHEET_REF);
	Properties props = PropertyReader.getProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	String cssPath = props.getProperty(SIGAConstants.STYLESHEET_PATH);
	ses.setAttribute(SIGAConstants.STYLESHEET_SKIN, cssPath + "/skin5/stylesheet.css");
%>
	

	
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
		<meta http-equiv="Cache-Control" content="no-cache">
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>">
		</script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script type="text/javascript">
		
			function growl(msg,type){
				//$.noticeRemove($('.notice-item-wrapper'), 400);
				jQuery.noticeAdd({
					text: msg,
					type: type
				});
			}
		
		</script>
	</head>

	<body>
		<iframe src="<%=app%>/html/jsp/developmentLogin/login.jsp"
		
				id="mainWorkArea"
				name="mainWorkArea"
				scrolling="no"
				frameborder="0"
				height="600px"
				class="posicionPrincipal">
		</iframe>
	</body>
</html>
