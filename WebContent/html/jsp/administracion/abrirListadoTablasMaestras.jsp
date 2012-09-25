<!-- abrirListadoTablasMaestras.jsp -->
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<siga:Titulo titulo="administracion.catalogos.titulo" localizacion="menu.administracion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			var bBuscado=false;
			
			<!-- Funcion asociada a boton buscar -->
			function buscar()
			{
				sub();
				if (listadoTablasMaestrasForm.nombreTablaMaestra.value=="")
				{
					alert("<siga:Idioma key="administracion.catalogos.elegirCatalogo"/>");
					fin();
					bBuscado=false;
				}
				else
				{
					bBuscado=true;
					listadoTablasMaestrasForm.modo.value="buscarInicio";
					listadoTablasMaestrasForm.submit();
				}
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function nuevo() 
			{
				if (!bBuscado)
				{
					alert("<siga:Idioma key="administracion.catalogos.realizarBusqueda"/>");
				}
				else
				{
					listadoTablasMaestrasForm.modo.value="nuevo";
					var resultado=ventaModalGeneral("listadoTablasMaestrasForm","P");
					
					if (resultado=="MODIFICADO")
					{
						buscar();
					}
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body onLoad="ajusteAlto('resultado');">
		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<html:hidden property="actionModal" value=""/>
					<html:hidden property="nombreCampoCodigo"/>
					<html:hidden property="nombreCampoCodigoExt"/>
					<html:hidden property="nombreCampoDescripcion"/>
					<html:hidden property="local"/>
					<html:hidden property="aliasTabla"/>
					<html:hidden property="longitudCodigo"/>
					<html:hidden property="longitudCodigoExt"/>
					<html:hidden property="longitudDescripcion"/>
					<html:hidden property="tipoCodigo"/>
					<html:hidden property="tipoCodigoExt"/>
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>				
						<td class="labelText">
							Seleccionar Catálogo&nbsp;(*)
						</td>				
						<td>
<%
						String sTipoCombo = CenVisibilidad.getNivelInstitucion(userBean.getLocation());
						
						sTipoCombo = sTipoCombo!=null && sTipoCombo.equals("1") ? "tablasMaestrasAdmin" : "tablasMaestras";
%>
							<siga:ComboBD nombre="nombreTablaMaestra" tipo="<%=sTipoCombo%>" obligatorio="true" clase="boxCombo" accion="bBuscado=false;buscar();"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="general.codeext"/>
						</td>
						<td class="labelText">
							<html:text property="codigoBusqueda" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="general.description"/>
						</td>
						<td class="labelText">
							<html:text property="descripcionBusqueda" styleClass="box"/>
						</td>
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
							marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
