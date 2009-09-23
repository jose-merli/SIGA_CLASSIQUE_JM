<!-- pestanaTextoSMS.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoEnvio = (String)request.getAttribute("idTipoEnvio");
	String sIdPlantillaEnvios = (String)request.getAttribute("idPlantillaEnvios");
	
	String sCuerpo=(String)request.getAttribute("cuerpo");
	if (sCuerpo==null) sCuerpo="";	
	
	String plantilla = (String)request.getAttribute("plantilla");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String idTipoEnvios = (String)request.getAttribute("idTipoEnvios");
	int escrito=0;
	
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
				TextoSMSForm.target="mainWorkArea";
				TextoSMSForm.action = "/SIGA/ENV_DefinirPlantillas.do?noreset=true";
				TextoSMSForm.modo.value="abrirConParametros";
				TextoSMSForm.submit();
			}
	
			function accionGuardar()
			{
				sub();
				TextoSMSForm.target="submitArea";
				TextoSMSForm.action = "/SIGA/ENV_Texto_SMS.do";
				TextoSMSForm.modo.value="Modificar";
				TextoSMSForm.submit();
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

			function contar(input, tam){
				cuenta(input, tam);
				document.getElementById('textSize').value = input.value.length;
			}
				
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="pestana.envios.textoSMS" 
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
	
		<html:form action="/ENV_Texto_SMS.do" method="POST">
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

<%
		String sBotones2 = bEditable ? "V,G" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>