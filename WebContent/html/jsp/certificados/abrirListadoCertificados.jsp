<!-- abrirListadoCertificados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	
	String sCertificado = (String)request.getAttribute("certificado");
	
	if (sCertificado==null)
	{
		sCertificado="";
	}

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<siga:Titulo titulo="certificados.mantenimiento.titulo" localizacion="menu.certificados"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				sub();
				MantenimientoCertificadosForm.modo.value="buscar";
				MantenimientoCertificadosForm.submit();
			}
			
			function open()
			{
				if (<%=sCertificado!=""%>)
				{
					buscar();
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body onLoad="ajusteAlto('resultado');open();">
			<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/CER_MantenimientoCertificados.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>				
						<td class="labelText" width="40">
							<siga:Idioma key="certificados.mantenimiento.literal.certificado"/>
						</td>				
						<td>
							<html:text value="<%=sCertificado%>" name="MantenimientoCertificadosForm" property="certificado" size="50" maxlength="50" styleClass="box"/>
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
							marginwidth="0";					 
							class="frameGeneral">
			</iframe>

			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
