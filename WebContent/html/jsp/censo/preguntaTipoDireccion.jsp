<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ page import="com.atos.utils.*"%>

<%@page import="com.siga.censo.form.DireccionesForm;"%>

<%
	String app=request.getContextPath();
	
	String idDireccionesCensoWeb=(String)request.getAttribute("idDireccionesCensoWeb") ;
	String idDireccionesFacturacion=(String)request.getAttribute("idDireccionesFacturacion") ;
	String modo=(String)request.getAttribute("modo");
	String modificarPre=(String)request.getAttribute("idDireccionesPreferentes") ;
	String control=(String)request.getAttribute("control") ;

	String recurso = "";
	if(idDireccionesCensoWeb!=null && !idDireccionesCensoWeb.equals("")&&idDireccionesFacturacion!=null && !idDireccionesFacturacion.equals("")){
		recurso = "messages.censo.direcciones.errorCensoWebyFacturacion";
	}else if(idDireccionesCensoWeb!=null && !idDireccionesCensoWeb.equals("")){
		recurso = "messages.censo.direcciones.errorCensoWeb";
	}else if(idDireccionesFacturacion!=null && !idDireccionesFacturacion.equals("")){
		recurso = "messages.censo.direcciones.errorFacturacion";
	}

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="<%=recurso%>"/>';
			if (confirm(type)) {
				parent.document.getElementById("idDireccionesCensoWeb").value="<%=idDireccionesCensoWeb%>";
				parent.document.getElementById("idDireccionesFacturacion").value="<%=idDireccionesFacturacion%>";
				parent.document.getElementById("modificarPreferencias").value="<%=modificarPre%>";
			  	parent.document.getElementById("control").value="<%=control%>";
			  	parent.document.getElementById("modo").value="<%=modo%>";
			  	<%
				if(idDireccionesCensoWeb!=null && !idDireccionesCensoWeb.equals("")&&idDireccionesFacturacion!=null && !idDireccionesFacturacion.equals("")){ %>
					parent.actualizarCensoFacturacion();
				<%}else if(idDireccionesCensoWeb!=null && !idDireccionesCensoWeb.equals("")){%>
					parent.actualizarCenso();
				<%}else if(idDireccionesFacturacion!=null && !idDireccionesFacturacion.equals("")){%>
					parent.actualizarFacturacion();
				<%}%>
			  	
			  	
			  	
			} 
		}
	</script>
</head>

<body onload="reloadPage();">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>

	<html:form action = "${path}" method="POST" >
	    <html:hidden property = "modo" value = ""/>
	 </html:form> 	

</body>
</html>