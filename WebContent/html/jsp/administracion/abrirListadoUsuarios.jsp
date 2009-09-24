<!-- abrirListadoUsuarios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<siga:Titulo 
		titulo="administracion.usuarios.titulo" 
		localizacion="administracion.gestionUsuariosGrupos.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				sub();
				listadoUsuariosForm.modo.value="buscar";
				listadoUsuariosForm.submit();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body onLoad="ajusteAlto('resultado');">
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>				
						<td class="labelText">
							<siga:Idioma key="administracion.usuarios.literal.nombre"/>
						</td>				
						<td>
							<html:text name="listadoUsuariosForm" property="descripcion" size="50" maxlength="50" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="administracion.usuarios.literal.activo"/>
						</td>
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
			

			<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
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
