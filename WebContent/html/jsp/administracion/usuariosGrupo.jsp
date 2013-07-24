<!DOCTYPE html>
<html>
<head>
<!-- usuariosGrupo.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties" %>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	String[] parametro = new String[1];
	parametro[0] = ((UsrBean)request.getSession().getAttribute("USRBEAN")).getLocation();
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo 
		titulo="administracion.grupos.asignarUsuariosGrupo.titulo" 
		localizacion="administracion.gestionUsuariosGrupos.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			//Funcion asociada a boton buscar -->
			function buscar() 
			{
				var bBuscar=true;
				
				sub();
				if (asignarUsuariosGrupoForm.nombreBusqueda.value=="" &&
				    asignarUsuariosGrupoForm.idRolBusqueda.value=="" &&
				    asignarUsuariosGrupoForm.idGrupoBusqueda.value=="")
				{
					if (!confirm("<siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.busquedaCostosa"/>"))
					{
						bBuscar=false;
						fin();
					}
				}
				
				if (bBuscar)
				{
					asignarUsuariosGrupoForm.modo.value="buscar";
					asignarUsuariosGrupoForm.submit();
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	</head>
	
	<body onload="ajusteAlto('resultado');">
			<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_AsignarUsuariosGrupo.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">

					<tr>				
						<td class="labelText">
							<siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.nombre"/>
						</td>				
						<td>
							<html:text name="asignarUsuariosGrupoForm" property="nombreBusqueda" size="50" maxlength="50" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.rol"/>
						</td>				
						<td>
							<siga:Select queryId="getRoles" id="idRolBusqueda"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.grupo"/>
						</td>				
						<td>
							<siga:Select queryId="getPerfilesDeInstitucion" id="idGrupoBusqueda"/>
						</td>
					</tr>
				</html:form>
			</table>
			</fieldset>

			<siga:ConjBotonesBusqueda botones="B" titulo=""/>

			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
					   		scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
					class="frameGeneral">
	</iframe>

			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
