<!-- detallePlantillaEnvios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{	
				sub();
				if(PlantillasEnviosForm.descripcionPlantilla.value=="")
				{
					var mensaje = "<siga:Idioma key="envios.plantillas.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	
					alert (mensaje);
					fin();
					return false;
				}

				if(PlantillasEnviosForm.idTipoEnvio.value=="")
				{
					var mensaje = "<siga:Idioma key="envios.plantillas.literal.tipoenvio"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	
					alert (mensaje);
				fin();
					return false;
				}

				PlantillasEnviosForm.submit();
				
				window.top.returnValue="MODIFICADO";
			}

			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.parent.close();
			}
			
			function refrescarLocal()
			{
				window.parent.close();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="envios.plantillas.literal.titulo"/>
				</td>
			</tr>
		</table>	

			<html:form action="/ENV_DefinirPlantillas.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Insertar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>

				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="envios.plantillas.literal.titulo">
								<table class="tablaCampos" align="center">
									<tr>
										<td class="labelText">
											<siga:Idioma key="envios.plantillas.literal.plantilla"/>&nbsp;(*)
										</td>
										<td>
											<input type="text" name="descripcionPlantilla" value="" maxlength="100" class="boxCombo"/>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="envios.plantillas.literal.tipoenvio"/>&nbsp;(*)
										</td>
										<td>
											<siga:ComboBD nombre="idTipoEnvio" tipo="cmbTipoEnvios" clase="boxCombo" obligatorio="true"/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<siga:ConjBotonesAccion botones="Y,C" modal="P"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>