<!-- multidiomaBusqueda.jsp -->

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
<%@ page import="java.util.ArrayList" %>
<% 
	String app=request.getContextPath(); 
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	boolean bCatalogos = UtilidadesString.stringToBoolean((String)request.getAttribute("CATALOGOS_MAESTROS"));
	String botones = "B";
	if (!bCatalogos) botones += ",GR"; // Si es etiquetas
	
	String parametros[] = {usrbean.getLocation(), usrbean.getLocation()}; 
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<% if (bCatalogos) { // Catalogos %>
				<siga:Titulo titulo="administracion.multidioma.catalogosMaestros.titulo" localizacion="administracion.multidioma.etiquetas.localizacion"/>
				
		<% } else { // Etiquetas %>
				<siga:Titulo titulo="administracion.multidioma.etiquetas.titulo" localizacion="administracion.multidioma.etiquetas.localizacion"/>
		<% } %>
		
		<script language="JavaScript">

	 		function buscar()
	 		{
	 			multidiomasForm.target="resultado";
		 		multidiomasForm.modo.value="buscar";
				multidiomasForm.submit();
	 		}
	 		
	 		function generarRecursos()
	 		{
				var mensaje = "<siga:Idioma key="administracion.multidioma.etiquetas.alertGenerarFicheroRecursos"/> ";
				if (confirm(mensaje)) {
					// Abro la ventana de las tuercas:
					multidiomasForm.modo.value="generarFicheroRecursos";
					multidiomasForm.target="submitArea3";
		 			var f = multidiomasForm.name;	
					document.frames.submitArea3.location = '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
					
				} 
	 		}
	 		
		</script>
	</head>
	
	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center" border ="0">
				<html:form action="/ADM_GestionarMultiidiomaEtiquetas.do" method="POST" target="submitArea3">
					
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="esCatalogo" value="<%=bCatalogos%>">
					
					<tr>				
						<td class="labelText" width="40%">
						
							<% if (bCatalogos) { // Catalogos %>
								<siga:Idioma key="administracion.multidioma.catalogosMaestros.literal.entidad"/>
								&nbsp;&nbsp;
		        				<siga:ComboBD nombre="descripcion" tipo="mutidiomaCatalogosMaestros" parametro="<%=parametros%>" obligatorio="true" clase="boxCombo" obligatorioSinTextoSeleccionar="true" />
		        				
							<% } else { // Etiquetas %>
								<siga:Idioma key="administracion.multidioma.etiquetas.literal.descripcion"/>
								&nbsp;&nbsp;
								<input type="text" name="descripcion" size="30" value="" class="box"/>
							<% } %>

						</td>
						<td class="labelText" width="">
							<siga:Idioma key="administracion.multidioma.etiquetas.literal.idioma"/>
						</td>
						<td class="labelText" width="">
							<% 
							   ArrayList eleSel = new ArrayList();
							   eleSel.add(usrbean.getLanguage());
							%>
	        				<siga:ComboBD nombre="idIdioma" tipo="cmbIdioma" obligatorio="true" clase="boxCombo" obligatorioSinTextoSeleccionar="true" elementoSel="<%=eleSel%>"/>
						</td>
						<td class="labelText" width="">
							<siga:Idioma key="administracion.multidioma.etiquetas.literal.idiomaATraducir"/>
						</td>
						<td class="labelText" width="">
	        				<siga:ComboBD nombre="idIdiomaATraducir" tipo="cmbIdioma" obligatorio="true" clase="boxCombo" obligatorioSinTextoSeleccionar="true"/>
						</td>
	       			</tr>
				</html:form>
			</table>
		</fieldset>

		<siga:ConjBotonesBusqueda botones="<%=botones%>" titulo=""/>

		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado" 
		         scrolling="no" frameborder="0" marginheight="0" marginwidth="0"; class="frameGeneral">
		</iframe>

		<iframe name="submitArea3" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
