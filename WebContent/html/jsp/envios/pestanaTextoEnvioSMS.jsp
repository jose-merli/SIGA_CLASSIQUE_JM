<!-- pestanaTextoEnvioSMS.jsp -->
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

	String sCuerpo = (String)request.getAttribute("sCuerpo");
	if (sCuerpo==null) sCuerpo="";	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<script language="JavaScript" >
			function accionGuardar()
			{
				sub();
				TextoEnviosSMSForm.modo.value="Modificar";
				TextoEnviosSMSForm.submit();
			}

			/*function accionVolver()
			{
				TextoEnviosSMSForm.action = "<%=app%>/ENV_DefinirEnvios.do?buscar=true";
				TextoEnviosSMSForm.modo.value="abrir";
				TextoEnviosSMSForm.target = "mainWorkArea";
				TextoEnviosSMSForm.submit();
			}*/

			function contar(input, tam){
				cuenta(input, tam);
				document.getElementById('textSize').value = input.value.length;
			}

			function refrescarLocal()
			{
				document.location.reload();
			}
		</script>

		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

		<siga:Titulo
			titulo="pestana.envios.textoSMS" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>

	<body class="detallePestanas">
		<div id="camposRegistro">
			<html:form action="/ENV_TextoEnvio_SMS.do" method="POST" target="submitArea">
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
				<table class="tablaCampos">
					<tr>
						<td class="labelText" width = "5%" align="right">
							<siga:Idioma key="envios.plantillas.literal.cuerpo"/>
						</td>
						<td width = "30%">
							<html:textarea property="cuerpo" onKeyUp="contar(this,151)" onChange="contar(this,151)" onKeyDown="contar(this,151)" value="<%=sCuerpo%>" styleClass="boxCombo" cols="210" rows="10"  value="<%=sCuerpo%>" readonly="<%=bReadOnly%>"/>
						</td>
						<td width = "65%">
						</td>
					</tr>
					
					<tr align="left">
						<td>
						</td>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.caracteresUsados"/>: 
							<input type="text" name="textSize" readonly size="3" value="<%=sCuerpo.length()%>"/>
						</td>
						<td>
						</td>
					</tr>
					
					<tr align="left">
						<td>
						</td>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.textoAyudaSms"/>
						</td>
						<td>
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