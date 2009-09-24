<!-- pestanaDetalleCorreoElectronico.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";

	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),userBean);
	String idEnvio = (String)request.getAttribute("idEnvio");
	String idInstitucion = (String)request.getAttribute("idInstitucion");

	String sAsunto = (String)request.getAttribute("sAsunto");
	if (sAsunto==null) sAsunto="";	
	String sCuerpo = (String)request.getAttribute("sCuerpo");
	if (sCuerpo==null) sCuerpo="";	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<script language="JavaScript">
			function accionGuardar()
			{
				sub();
				EnviosCorreoElectronicoForm.modo.value="Modificar";
				EnviosCorreoElectronicoForm.submit();
			}

			/*function accionVolver()
			{
				EnviosCorreoElectronicoForm.action = "<%=app%>/ENV_DefinirEnvios.do?buscar=true";
				EnviosCorreoElectronicoForm.modo.value="abrir";
				EnviosCorreoElectronicoForm.target = "mainWorkArea";
				EnviosCorreoElectronicoForm.submit();
			}*/

			function refrescarLocal()
			{
				document.location.reload();
			}
		</script>

		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

		<siga:Titulo
			titulo="envios.definirEnvios.correoElectronico.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>

	<body class="detallePestanas">
		<div id="camposRegistro">
			<html:form action="/ENV_Correo_Electronico.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "accion" value = ""/>

				<html:hidden property = "idEnvio" value = "<%=idEnvio%>"/>
				<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>

				<table class="tablaTitulo" align="center" cellspacing="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
							<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%>
						&nbsp;&nbsp;&nbsp;
							<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%>
						</td>
					</tr>
				</table>

<%
				boolean bReadOnly=userBean.getAccessType().equals(SIGAPTConstants.ACCESS_READ) ? true : false;
%>
				<table class="tablaCampos" align="center" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.asunto"/>
						</td>
						<td>
							<html:text property="asunto" value="<%=sAsunto%>" styleClass="boxCombo" size="80" value="<%=sAsunto%>" readonly="<%=bReadOnly%>"/>
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.cuerpo"/>
						</td>
						<td>
							<html:textarea property="cuerpo" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="<%=sCuerpo%>" styleClass="box"  style="width:580" cols="500" rows="25" value="<%=sCuerpo%>" readonly="<%=bReadOnly%>"/>
						</td>
					</tr>
				</table>
			</html:form>

			<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"  />
		</div>

		<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="modo" value="abrirBusquedaModal">
		</html:form>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>