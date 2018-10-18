<!DOCTYPE html>
<html>
<head>
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
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
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

	String sAsunto = (String)request.getAttribute("sAsunto");
	if (sAsunto==null) sAsunto="";	
	String sCuerpo = (String)request.getAttribute("sCuerpo");
	if (sCuerpo==null) sCuerpo="";	
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='https://cloud.tinymce.com/stable/tinymce.min.js'/>"></script>
<script type="text/javascript">
	tinyMCE.init({
		selector : "textarea",
		theme : "modern",
		plugins: "pagebreak table save charmap media contextmenu paste directionality noneditable visualchars nonbreaking spellchecker template",
	    toolbar1: "bold italic underline strikethrough | alignleft aligncenter alignright alignjustify fontselect fontsizeselect | bullist numlist | undo redo | forecolor backcolor | charmap nonbreaking",

	    contextmenu: "cut copy paste",
	    menubar: false,
	    statusbar: false,
	    toolbar_item_size: "small",
	});
</script>
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
			function abrirAyuda() {
				
		
				var alto = 580, ancho = 800;
					  
		 		var posX = (screen.width - ancho) / 2;
     		var posY = (screen.height - alto) / 2;
     		var medidasWin = "height=" + alto + ", width=" + ancho + ", top=" + posY + ", left=" + posX;
 			
				w = window.open ("/SIGA/html/jsp/envios/paginaAyuda.jsp",
						   				   "", 
										     "status=0, toolbar=0, location=0, menubar=0, resizable=1," + medidasWin);
		}	
		</script>

		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

		<siga:Titulo
			titulo="envios.definirEnvios.correoElectronico.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>

	<body>
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
						<td class="labelText" width="15%">
							<siga:Idioma key="envios.plantillas.literal.asunto"/>
						</td>
						<td width="70%">
							<html:text property="asunto" value="<%=sAsunto%>" styleClass="box"  style="width:640px"  readonly="<%=bReadOnly%>"/>
						</td>
						<td width="15%" align="left">
			 			 <a href="javascript:abrirAyuda();"><img border=0 src="<html:rewrite page='/html/imagenes/help.gif'/>"  alt="<siga:Idioma key="general.ayuda.normativa"/>"></a>
			 			 
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.cuerpo"/>
						</td>
						<td>
							<html:textarea property="cuerpo" rows="33" value="<%=sCuerpo%>" styleClass="box"  style="width:640px" readonly="<%=bReadOnly%>"/>
						</td>
					</tr>
				</table>
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