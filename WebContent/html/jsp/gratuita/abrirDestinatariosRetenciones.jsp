<!DOCTYPE html>
<html>
<head>
<!-- abrirDestinatariosRetenciones.jsp -->
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
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@page import="java.util.Properties"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo titulo="gratuita.maestros.destinatariosRetenciones.titulo" localizacion="sjcs.maestros.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			
			<!-- Funcion asociada a boton buscar -->
			function buscar()
			{
				document.forms[0].modo.value="buscar";
				document.forms[0].submit();
			}
	
			<!-- Asociada al boton Nuevo -->
			function nuevo() 
			{		
				document.forms[0].modo.value = "nuevo";
				var resultado = ventaModalGeneral(document.forms[0].name,"P");
				if (resultado=='MODIFICADO') {
					buscar();
				}
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/JGR_MantenimientoDestinatariosRetenciones.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="actionModal" value="">
					<tr>				
						<td class="labelText" width="150">
							<siga:Idioma key="gratuita.maestros.destinatariosRetenciones.literal.descripcion"/>
						</td>
						<td class="labelText" width="200">
							<html:text property="nombreBuscado" size="30" styleClass="box"/>
						</td>
						<td class="labelText" width="60%">&nbsp;</td>
					</tr>
				</html:form>
			</table>
			</fieldset>

			<!-- V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log -->
			<siga:ConjBotonesBusqueda botones="B,N" titulo=""/>

			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
					   		scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0"	 
					class="frameGeneral">
		</iframe>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
