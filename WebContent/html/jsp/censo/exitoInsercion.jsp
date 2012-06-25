<!-- exitoInsercion.jsp -->
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
	String idPersona = (String)request.getAttribute("idPersona");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String tipo = (String)request.getAttribute("tipo");
	String error=(String)request.getAttribute("error");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
		function reloadPage() {
			<% 
				if (mensaje!=null) {
					if (error!=null && error.equals("ERROR")){
						String contadorSugeridoSP=(String)request.getAttribute("contadorSugeridoSP");
						String contadorSugeridoSJ=(String)request.getAttribute("contadorSugeridoSJ");
						
						%>
							var type = '<siga:Idioma key="<%=mensaje%>"/>';
							alert(type,"error");
						<%
							if (contadorSugeridoSP!=null){
								%>
			  						window.parent.document.datosGeneralesForm.contadorNumRegSP.value='<%=contadorSugeridoSP%>';
								<%
							} else {
								if (contadorSugeridoSJ!=null){
									%>
			  							window.parent.document.datosGeneralesForm.contadorNumRegSJ.value='<%=contadorSugeridoSJ%>';
									<%
								}
			 				} 
					} else {
						String estilo="notice";
						if(mensaje.contains("error")){
							estilo="error";
						}else if(mensaje.contains("success")){
							estilo="success";
						}
						%>
							var type = '<siga:Idioma key="<%=mensaje%>"/>';
							alert(type,"<%=estilo%>");
							document.forms[0].submit();
		 				<%
		 			}
				}
			%>
		}
	</script>
</head>

<body onload="reloadPage();">

<html:form action="/CEN_BusquedaClientes.do?noReset=true" method="POST" target="mainWorkArea" type="">
  <input type="hidden" name="modo" value="recargarEditar">
  <input type="hidden" name="idPersona" value="<%=idPersona%>">
  <input type="hidden" name="idInstitucion" value="<%=idInstitucion%>">
  <input type="hidden" name="tipo" value="<%=tipo%>">
</html:form>

</body>
</html:html>