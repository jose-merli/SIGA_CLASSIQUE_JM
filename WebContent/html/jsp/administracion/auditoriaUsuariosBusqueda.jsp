<!DOCTYPE html>
<html>
<head>
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

	

	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
		
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
	 				document.auditoriaUsuariosForm.nombre.value = "";
	 				jQuery("#nombre").attr("disabled","disabled");
		 			
				}
				else {
					jQuery("#nombre").removeAttr("disabled");
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
	        				<siga:Select queryId="getTiposCambio" id="tipoAccion"/> 
						</td>
	       			</tr>
	       			<tr>
	       				<td class="labelText">
	       					<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaDesde"/>
	       				</td>
						<td>
							<siga:Fecha  nombreCampo= "fechaDesde"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="administracion.auditoriaUsuarios.literal.fechaHasta"/>
						</td>
						<td>
							<siga:Fecha  nombreCampo= "fechaHasta" campoCargarFechaDesde="fechaDesde"/>
						</td>
	       			</tr>
				</html:form>
			</table>
		</fieldset>

		<siga:ConjBotonesBusqueda botones="B" titulo=""/>

		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado"  
		         scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral">
		</iframe>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
