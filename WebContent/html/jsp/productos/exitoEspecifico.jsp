<!-- exitoEspecifico.jsp -->
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

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String modal = (String)request.getAttribute("modal");
	
	// claves de registro insertado
	String idInstitucion = (String)request.getAttribute("institucion");
	String idTipoServicios = (String)request.getAttribute("tipoServicio");
	String idServicio = (String)request.getAttribute("servicio");
	String idServiciosInstitucion = (String)request.getAttribute("servicioInstitucion");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
			<%
				if (mensaje!=null){
					%>
						var type = '<siga:Idioma key="<%=mensaje%>"/>';
						alert(type,"success");
					<%
				}
				if (modal!=null){
					if (modal.equalsIgnoreCase("SI_HAY_CONDICION")) {
						%> 
							window.top.returnValue="<%=modal%>"; 
							window.top.close();
						<%
					} else { 
						%>
							window.top.returnValue="MODIFICADO";
						<%
					}
				} else {
					%>	
						document.MantenimientoServiciosForm.submit();
					<%
				}
			%>
		} 
	</script>
</head>
<body onload="reloadPage();">

	<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="modal">
		<html:hidden property="modo" value="editar"/>
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property="idTipoServicios" value="<%=idTipoServicios%>"/>
		<html:hidden property="idServicio" value="<%=idServicio%>"/>
		<html:hidden property="idServiciosInstitucion" value="<%=idServiciosInstitucion%>"/>
	</html:form>

</body>
</html:html>


