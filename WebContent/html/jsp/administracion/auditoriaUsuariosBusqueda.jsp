<!-- auditoriaUsuariosBusqueda.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Hashtable" %>
<% 
	String app=request.getContextPath();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String nombreUsuario = "";
	Hashtable h = userBean.getDatosUsuario ();
	if (h != null) {
		nombreUsuario = (String)h.get("NOMBRE_USUARIO");
	}
%>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<siga:Titulo titulo="administracion.auditoriaUsuarios.titulo" localizacion="menu.auditoriaUsuarios.localizacion"/>
		
		<script language="JavaScript">
		<!--
			<!-- Funcion asociada a boton buscar -->
	 		function buscar()
	 		{
				// Rango Fechas (desde / hasta)
				sub();
				if (compararFecha (document.auditoriaUsuariosForm.fechaDesde, document.auditoriaUsuariosForm.fechaHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					return false;
				}
				auditoriaUsuariosForm.modo.value = "buscarInicio";
				auditoriaUsuariosForm.submit();
	 		}
	 		
	 		function cambiarUsuario (o) {
	 			if (o.checked) {
		 			document.auditoriaUsuariosForm.nombre.disabled = true;
		 			document.auditoriaUsuariosForm.nombre.value = "";
				}
				else {
		 			document.auditoriaUsuariosForm.nombre.disabled = false;
				}
	 		}
	 	-->
		</script>
	</head>
	
	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center" border ="0">
				<html:form action="/ADM_AuditoriaUsuarios.do" method="POST" target="resultado">
					
					<input type="hidden" name="modo" value="">
					
					<tr>				
						<td class="labelText" width="">
							<siga:Idioma key="administracion.auditoriaUsuarios.literal.nombre"/>
						</td>
						<td class="boxConsulta">
							<html:text styleClass="box" property="nombre" size="30" maxlength="50"  />
						</td>
						<td class="labelText" colspan="2">
							<siga:Idioma key="administracion.auditoriaUsuarios.literal.usuarioAutomatico"/>&nbsp;
		  					<input type="checkbox" name="usuarioAutomatico" onclick="cambiarUsuario(this);">
						</td>
					</tr>
					<tr>
						<td class="labelText" >
							<siga:Idioma key="administracion.auditoriaUsuarios.literal.tipoAccion"/>
						</td>
	        			<td >
	        				<siga:ComboBD nombre="tipoAccion" tipo="cmbCambioHistorico" clase="boxCombo" />
						</td>
	       			</tr>
	       			<tr>
	       				<td class="labelText">
	       					<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaDesde"/>
	       				</td>
						<td>
							<html:text styleClass="box" property="fechaDesde" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(fechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
						</td>
						<td class="labelText">
							<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaHasta"/>
						</td>
						<td>
							<html:text styleClass="box" property="fechaHasta" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(fechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
						</td>
	       			</tr>
				</html:form>
			</table>
		</fieldset>

		<siga:ConjBotonesBusqueda botones="B" titulo=""/>

		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado"  
		         scrolling="no" frameborder="0" marginheight="0" marginwidth="0"; class="frameGeneral">
		</iframe>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
