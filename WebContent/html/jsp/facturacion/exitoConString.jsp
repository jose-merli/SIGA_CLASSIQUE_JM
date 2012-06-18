<!-- exitoConString.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page language="java" %>
<%@ page contentType = "text/html"  %>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	// SUFIJO = Permite anhadir una cadena de texto al mensaje a mostrar
	String sufijo = (String)request.getAttribute("sufijo");
	// MODAL = caso de ventana modal (cierra ventana despues con retorno "MODIFICADO" siempre)
	// si no queremos cerrar la ventana no ponemos este atributo
	String modal= (String)request.getAttribute("modal");
	// SINREFRESCO = para el caso que no queramos refrescar nada
	String sinrefresco = (String)request.getAttribute("sinrefresco");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	function reloadPage() {

	<%  if (mensaje!=null){%>
		var type = "<%=mensaje%>";
		alert(type);
	<%  } %>
	<%  if (modal!=null){%>
	<%  	if (sinrefresco!=null){%>
				window.top.returnValue=""; 
	<%  	} else { %>
				window.top.returnValue="MODIFICADO"; 
	<%  	} %>
			window.top.close();
	<%  }else{%>	
	<%  	if (sinrefresco==null){%>
				parent.refrescarLocal();
	<%  	} %>
	<%  } %>
	return false;

}
</script>
</head>
<body onload="reloadPage()"></body>
</html:html>
