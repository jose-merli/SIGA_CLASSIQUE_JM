<!DOCTYPE html>
<html>
<head>
<!-- exitoInsercionNoColegiado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String idPersona = (String)request.getAttribute("idPersona");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String nColegiado = (String)request.getAttribute("nColegiado");
	String nif = (String)request.getAttribute("nif");
	String nombre=(String)request.getAttribute("nombre");
	String apellido1=(String)request.getAttribute("apellido1");
	String apellido2=(String)request.getAttribute("apellido2");
		String idDireccion=(String)request.getAttribute("idDireccion");
	
	
	
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	function reloadPage() {
	 
		<% 
		 	if (mensaje!=null){
		%>
			    var type = '<siga:Idioma key="<%=mensaje%>"/>';
			
				var aux = new Array();
				aux[0]="<%=idPersona %>";
				aux[1]="<%=idInstitucion %>";
				aux[2]="<%=nColegiado%>";
				
				aux[3]="<%=nif%>";
				aux[4]="<%=UtilidadesString.cambiarDoblesComillas(nombre) %>";
				aux[5]="<%=UtilidadesString.cambiarDoblesComillas(apellido1) %>";
				aux[6]="<%=UtilidadesString.cambiarDoblesComillas(apellido2) %>";
				<%if(idDireccion!=null){%>
				aux[7]="<%=idDireccion%>";
				<%}else{%>
					aux[7]="";
				<%}%>
				
				aux[8]="Nuevo";
				
				top.cierraConParametros(aux);
	
				alert(type);
	 	<% 	
	 		}
	 	%>
			
	}
	</script>
</head>

<body onload="reloadPage();">


</body>
</html>