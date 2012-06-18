<!-- loadingWindowOpener.jsp -->
<%@ page contentType="text/html" language="java"%>
<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>
<html>
<head>
<% String app = request.getContextPath();
   String formName=request.getParameter("formName");
   String msg=(request.getParameter("msg") == null) ? "" : "?msg="+request.getParameter("msg");
%>
<link rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script language="javascript">
   	var val;
   	function openLoadingWindow() {
		try {
    		val = showModalDialog('<%=app%>/html/jsp/general/loadingWindow.jsp<%=msg%>','', 'unadorned:yes;resizable:no;status:no;dialogWidth:200px;dialogHeight:200px;help:no;');
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