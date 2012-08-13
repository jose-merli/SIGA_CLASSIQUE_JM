<!-- loadingWindowOpener.jsp -->
<%@ page contentType="text/html" language="java"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%> 
<%@ page import="com.atos.utils.UsrBean"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>
<html>
<head>
<% String app = request.getContextPath();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
   	String formName=request.getParameter("formName");
//   String msg=(request.getParameter("msg") == null) ? "" : "?msg="+request.getParameter("msg");
   	String msg="";
   	if(request.getParameter("msg")!=null){
	   msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),request.getParameter("msg")));
   	}
   
%>
<link rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>		
		<script src="<html:rewrite page='/html/js/jquery.js'/>" type="text/javascript"></script>		
		<script src="<html:rewrite page='/html/js/jquery.custom.js'/>" type="text/javascript"></script>	
		
<script language="javascript">
   	var val;
   	function openLoadingWindow() {
		try {
    		sub("<%=msg%>");
	       	<% if (formName != null) {%>
	           parent.document.<%=formName%>.submit();
	       	<% } %>
	       	window.top.focus();
	    	return;
    	} catch(e) {
    		alert("Error javascript:"+e.name + " - "+e.message + " - document:"+document.location + " - document parent :"+parent.document.location);
    	}
    }
</script>
</head>
<body onLoad="openLoadingWindow();">
</body>
</html>