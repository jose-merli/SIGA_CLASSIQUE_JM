<!DOCTYPE html>
<html>
<head>
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
<%@ taglib uri = "c.tld" prefix="c"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.*"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector vDatos = (Vector)request.getAttribute("datos");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";

	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),userBean);
	String idEnvio = (String)request.getAttribute("idEnvio");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String CSV = (String)request.getAttribute("CSV");

	String sCuerpo = (String)request.getAttribute("sCuerpo");
	if (sCuerpo==null) sCuerpo="";	
	
	FilaExtElement[] elems=new FilaExtElement[1];
	elems[0]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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
			
			function download(){
				sub();
				TextoEnviosSMSForm.modo.value="downloadCertificado";
				TextoEnviosSMSForm.submit();
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
							<html:textarea property="cuerpo" onKeyUp="contar(this,151)" onChange="contar(this,151)" onKeyDown="contar(this,151)" value="<%=sCuerpo%>" styleClass="boxCombo" cols="210" rows="10" readonly="<%=bReadOnly%>"/>
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
				
				<%if (CSV!= null && !CSV.equals("")) {%>
					<table border="0" cellpadding="5" cellspacing="0">
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td width="65">&nbsp;</td>			
	  						<td class="labelText" align="right">Descarga Certificado Recepción</td>
							<td> <img id="iconoboton_download" src="/SIGA/html/imagenes/bdownload_off.gif" style="cursor:pointer;" alt="Descargar" name="iconoFila" title="Descargar" border="0" onClick="download()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('download','','/SIGA/html/imagenes/bdownload_on.gif',1)"></td>
						</tr>	
					</table>	
				<% } %>		
				
			</html:form>
<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>			
			<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"  />
		</div>

		<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="modo" value="abrirBusquedaModal">
		</html:form>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>