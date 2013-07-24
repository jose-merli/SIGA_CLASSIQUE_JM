<!DOCTYPE html>
<html>
<head>
<!-- abrirListadoPlantillasEnvios.jsp -->
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
<%@ page import="java.util.ArrayList"%>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	String sDescripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String sIdTipoEnvios = (String)request.getAttribute("idTipoEnvios");
	
	if (sDescripcionPlantilla==null)
	{
		sDescripcionPlantilla="";
	}
	
	if (sIdTipoEnvios==null)
	{
		sIdTipoEnvios="";
	}

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");

	// Para saber donde volver	
	request.getSession().setAttribute("EnvDefinirPlantilla","EDP");
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<siga:Titulo titulo="envios.plantillas.literal.titulo" localizacion="envios.definir.literal.localizacion"/>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
	
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				sub();
				PlantillasEnviosForm.modo.value="buscar";
				PlantillasEnviosForm.submit();
			}

			function nuevo()
			{
				PlantillasEnviosForm.action = "/SIGA/ENV_DefinirPlantillas.do";
				PlantillasEnviosForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("PlantillasEnviosForm","P");

				if (resultado!=undefined) {
					//BNS FALLABA AL DAR DE ALTA UNO NUEVO SIN BUSCAR (tablaDatosDinamicosD no estaba creado)
					if (jQuery("#tablaDatosDinamicosD").length <= 0){
						jQuery('form:first',document).append('<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD" />');
					}
					jQuery("#tablaDatosDinamicosD").val(resultado);
					MyForm.submit();
				}
			}
			
			function refrescarLocal()
			{
				buscar();
			}

			function open()
			{
				if (<%=sDescripcionPlantilla!=""%>)
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
				<form action="/SIGA/ENV_DefinirPlantillas.do" method="POST" target="mainWorkArea" name="MyForm">
					<input type="hidden" name="modo" value="Editar">

				</form>
				
				<html:form action="/ENV_DefinirPlantillas.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<input type="hidden" name="actionModal" value="">
					<input type="hidden" name="limpiarFilaSeleccionada" value="">

					<tr>				
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.plantilla"/>
						</td>				
						<td>
							<html:text value="<%=sDescripcionPlantilla%>" name="PlantillasEnviosForm" property="descripcionPlantilla" size="60" maxlength="100" styleClass="box"/>
						</td>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.tipoenvio"/>
						</td>	
<%
					ArrayList al = new ArrayList();
					al.add(sIdTipoEnvios);
%>
						<td>
							<siga:ComboBD nombre="idTipoEnvios" tipo="cmbTipoEnvios" clase="boxCombo" obligatorio="false" elementoSel="<%=al%>"/>
						</td>
					</tr>
				</html:form>
			</table>
			</fieldset>
			

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
