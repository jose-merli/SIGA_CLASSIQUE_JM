<!-- permisosAplicacion.jsp -->
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

<% String app=request.getContextPath(); %>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<siga:Titulo titulo="administracion.permisos.titulo" localizacion="menu.administracion"/>
		
		<script language="JavaScript">
	 		function buscar()
	 		{
				var combo = document.getElementById("idPerfil");
				sub();				
				if (combo.selectedIndex==0)
				{
					alert("<siga:Idioma key="administracion.permisos.mensaje.especificarGrupo"/>");
					fin();
					return false;
				}
				configurarPermisosAplicacionForm.modo.value="buscar";
				configurarPermisosAplicacionForm.submit();

	 		}
		</script>
	</head>
	
	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_ConfigurarPermisosAplicacion.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					
					<tr>				
						<td class="labelText">
							<siga:Idioma key="administracion.permisos.literal.grupo"/>&nbsp;(*)
						</td>
	        			<td class="labelText">
							<% String[] institucion={((UsrBean)request.getSession().getAttribute("USRBEAN")).getLocation()};%>
	        				<siga:ComboBD nombre="idPerfil" tipo="perfil" obligatorio="true" clase="boxCombo" parametro="<%=institucion%>"/>
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

			<!--<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>-->

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
