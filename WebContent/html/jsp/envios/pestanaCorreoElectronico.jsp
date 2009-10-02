<!-- pestanaCorreoElectronico.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoEnvio = (String)request.getAttribute("idTipoEnvio");
	String sIdPlantillaEnvios = (String)request.getAttribute("idPlantillaEnvios");
	
	String sAsunto=(String)request.getAttribute("asunto");
	if (sAsunto==null) sAsunto="";	
	String sCuerpo=(String)request.getAttribute("cuerpo");
	if (sCuerpo==null) sCuerpo="";	
	
	String plantilla = (String)request.getAttribute("plantilla");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String idTipoEnvios = (String)request.getAttribute("idTipoEnvios");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton Volver -->
			function accionVolver()
			{
				PlantillasEnviosCorreoElectronicoForm.target="mainWorkArea";
				PlantillasEnviosCorreoElectronicoForm.action = "/SIGA/ENV_DefinirPlantillas.do?noreset=true";
				PlantillasEnviosCorreoElectronicoForm.modo.value="abrirConParametros";
				PlantillasEnviosCorreoElectronicoForm.submit();
			}
	
			function accionGuardar()
			{
				sub();
				PlantillasEnviosCorreoElectronicoForm.target="submitArea";
				PlantillasEnviosCorreoElectronicoForm.action = "/SIGA/ENV_Campos_Correoe.do";
				PlantillasEnviosCorreoElectronicoForm.modo.value="Modificar";
				PlantillasEnviosCorreoElectronicoForm.submit();
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirTiposPlantillas.correoElectronico.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
		/>
	</head>

	<body>

				<table class="tablaTitulo" align="center" cellspacing="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
							<siga:Idioma key="envios.plantillas.literal.plantilla"/>:&nbsp;<%=plantilla%>
						</td>
					</tr>
				</table>
	
		<html:form action="/ENV_Campos_Correoe.do" method="POST">
			<html:hidden property="modo" value=""/>

			<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
			<html:hidden property="idTipoEnvio" value="<%=sIdTipoEnvio%>"/>
			<html:hidden property="idPlantillaEnvios" value="<%=sIdPlantillaEnvios%>"/>
			
			<html:hidden property="descripcionPlantilla" value="<%=descripcionPlantilla%>"/>
			<html:hidden property="idTipoEnvios" value="<%=idTipoEnvios%>"/>



<%
				boolean bReadOnly=bEditable ? false : true;
%>
				<table class="tablaCampos" align="center">
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
							<html:textarea property="cuerpo" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" value="<%=sCuerpo%>" styleClass="boxCombo" cols="210" rows="25"  value="<%=sCuerpo%>" readonly="<%=bReadOnly%>"/>
						</td>
					</tr>
				</table>
		</html:form>

<%
		String sBotones2 = bEditable ? "V,G" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>