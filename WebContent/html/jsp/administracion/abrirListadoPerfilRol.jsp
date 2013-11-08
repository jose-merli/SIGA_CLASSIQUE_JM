<!DOCTYPE html>
<html>
<head>
<!-- abrirListadoPerfilRol.jsp -->
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
		titulo="administracion.perfilRol.titulo" 
		localizacion="administracion.gestionUsuariosGrupos.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			//Funcion asociada a boton buscar -->
			function buscar() 
			{
				listadoPerfilRolForm.modo.value="buscar";
				listadoPerfilRolForm.submit();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	</head>

	<body onLoad="ajusteAlto('resultado');buscar()">
	     
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.perfilRol.titulo"/>
			</td>
		</tr>
	</table>		     
			
			<html:form action="/ADM_GestionarPerfilRol.do" method="POST" target="resultado">
				<html:hidden property="modo" value="inicio"/>
				<html:hidden name="listadoPerfilRolForm" property="descripcion" size="50" maxlength="50" styleClass="box"/>
				<html:hidden property="actionModal" value=""/>
			</html:form>

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
