<!-- partidosJudiciales.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.siga.beans.CenPartidoJudicialAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>

<%   
	String app=request.getContextPath();
 	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
 
	String idinstitucion = request.getParameter("idinstitucion");
	String idzona = request.getParameter("idzona");
	String idsubzona = request.getParameter("idsubzona");

	CenPartidoJudicialAdm admPJ = new CenPartidoJudicialAdm(usr);
	String v = admPJ.getPartidos(idinstitucion,idzona,idsubzona);
%>


<html>
	<head>
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	</head>

	<body class="BodyIframe">
		<font class="labelTextValor"><%=v %></font>
	</body>
</html>