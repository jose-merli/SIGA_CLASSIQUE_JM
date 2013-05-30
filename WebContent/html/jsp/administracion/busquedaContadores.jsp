<!-- busquedaContadores.jsp -->
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
	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo titulo="administracion.parametrosGenerales.contadores" localizacion="menu.parametrosGenerales.localizacion"/>
		
		<script language="JavaScript">
	 		function buscar()
	 		{
				sub();
				gestionContadoresForm.modo.value="buscar";
				gestionContadoresForm.submit();
	 		}
		</script>
	</head>
	
	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center" border ="0">
				<html:form action="/ADM_Contadores.do" method="POST" target="resultado">
					
					<input type="hidden" name="modo" value="inicio">
					
					
					<tr>				
						
						<td class="labelText" >
							<siga:Idioma key="administracion.parametrosGenerales.literal.modulo"/>
						</td>
	        			<td class="labelText" >
	        				<siga:ComboBD nombre="idModulo" tipo="cmbModuloContador"  clase="boxCombo" />
						</td>
						<td class="labelText" >
							<siga:Idioma key="administracion.parametrosGenerales.literal.codigo"/>
						</td>
						<td class="labelText">
						<html:text property="codigo" styleClass="box" size="20" ></html:text>
						</td>
	       			</tr>	   
					<tr>				
						
						<td class="labelText" >
							<siga:Idioma key="administracion.parametrosGenerales.literal.nombre"/>
						</td>
	        			<td class="labelText" >
	        				<html:text property="nombreContador" styleClass="box" size="30" ></html:text>
						</td>
						<td class="labelText" >
							<siga:Idioma key="administracion.parametrosGenerales.literal.descripcion"/>
						</td>
						<td class="labelText">
						    <html:text property="descripcionContador" styleClass="box" size="30" ></html:text>
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
