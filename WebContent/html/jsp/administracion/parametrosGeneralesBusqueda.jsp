<!DOCTYPE html>
<html>
<head>
<!-- parametrosGeneralesBusqueda.jsp -->
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

<% 
	String app=request.getContextPath(); 
	String esCGAE = (String)request.getAttribute("esCGAE");
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo titulo="administracion.parametrosGenerales.titulo" localizacion="menu.parametrosGenerales.localizacion"/>
		
		<script language="JavaScript">
	 		function buscar()
	 		{
				var combo = document.getElementById("idModulo");
				var combo = jQuery("#idModulo");
				sub();
				if (combo.val() == '' || combo.val() == '-1') {
					alert("<siga:Idioma key="administracion.parametrosGenerales.literal.modulo"/> <siga:Idioma key="messages.campoObligatorio.error"/>");
					return;
				}
				c = document.getElementById("checkGeneral");
				if (c !=null && c.checked) {
					parametrosGeneralesForm.checkParametrosGenerales.value="1";
				}
				else {
					parametrosGeneralesForm.checkParametrosGenerales.value="0";
				}
					
				parametrosGeneralesForm.modo.value="buscar";
				parametrosGeneralesForm.submit();
	 		}
		</script>
	</head>
	
	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center" border ="0">
				<html:form action="/ADM_ParametrosGenerales.do" method="POST" target="resultado">
					
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="checkParametrosGenerales" value="">
					
					<tr>				
						<td class="labelText" width="10%">&nbsp;</td>
						<td class="labelText" width="15%">
							<siga:Idioma key="administracion.parametrosGenerales.literal.modulo"/>
						</td>
	        			<td class="labelText" width="15%">
	        				<siga:Select queryId="getParametros" id="idModulo" required="true"/>
						</td>

						<td class="labelText">
						<% if ((esCGAE != null)&&(esCGAE.equals("true"))) { %>
							<siga:Idioma key="administracion.parametrosGenerales.literal.checkParametrosGenerales"/>&nbsp;&nbsp;
							<input type=checkbox id="checkGeneral" />
						<% } %>
						</td>
	       			</tr>	   
				</html:form>
			</table>
		</fieldset>

		<siga:ConjBotonesBusqueda botones="B" titulo=""/>

		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado"  scrolling="no"
								frameborder="0" marginheight="0" marginwidth="0"; class="frameGeneral">
		</iframe>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
