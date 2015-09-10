<!DOCTYPE html>
<html>
<head>
<!-- abrirListadoUsuarios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties" %>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	

	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo 
		titulo="administracion.usuarios.titulo" 
		localizacion="administracion.gestionUsuariosGrupos.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			//Funcion asociada a boton buscar
			function buscar() 
			{
				
				var bBuscar=true;
				
				sub();
				if (listadoUsuariosForm.descripcion.value=="" &&
					listadoUsuariosForm.idRolBusqueda.value=="" &&
					listadoUsuariosForm.idGrupoBusqueda.value=="" &&
					listadoUsuariosForm.NIF.value=="")
				{
					if (!confirm("<siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.busquedaCostosa"/>"))
					{
						bBuscar=false;
						fin();
					}
				}
				
				if (bBuscar)
				{
					listadoUsuariosForm.modo.value="buscar";
					listadoUsuariosForm.submit();
				}
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body onLoad="ajusteAlto('resultado');">

			<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="">
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>	
						<td class="labelText"><siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.nombre"/></td>				
						<td><html:text name="listadoUsuariosForm" property="descripcion" size="40" maxlength="50" styleClass="box"/></td>
						
						<td class="labelText"><siga:Idioma key="administracion.usuarios.literal.NIF"/></td>				
						<td colspan="3"><html:text name="listadoUsuariosForm" property="NIF" size="15" maxlength="10" styleClass="box"/></td>
					</tr>
					<tr>									
						<td class="labelText"><siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.rol"/></td>				
						<td><siga:Select queryId="getRoles" id="idRolBusqueda"/></td>
						
						<td class="labelText"><siga:Idioma key="administracion.grupos.asignarUsuariosGrupo.literal.grupo"/></td>				
						<td><siga:Select queryId="getPerfilesDeInstitucion" id="idGrupoBusqueda"/></td>

						<td class="labelText"><siga:Idioma key="administracion.usuarios.literal.activo"/></td>
						<td>
							<html:select name="listadoUsuariosForm" property="activoConsulta" styleClass="boxCombo">
								<option value=""></option>
								<option value="S"><siga:Idioma key="general.boton.yes"/></option>
								<option value="N"><siga:Idioma key="general.boton.no"/></option>
							</html:select>
						</td>
					</tr>
				</html:form>
			</table>
			</fieldset>
			

			<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
			<siga:ConjBotonesBusqueda botones="B" titulo=""/>

			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
					   		scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0"					 
					class="frameGeneral">
			</iframe>

			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
