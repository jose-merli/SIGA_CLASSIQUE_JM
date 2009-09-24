<!-- abrirPestanasPlantillasEnvios.jsp -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>

<%
	String app       = request.getContextPath();
	HttpSession ses  = request.getSession();
	Properties src   = (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Hashtable htDatos = (Hashtable)request.getAttribute("htDatos");

	String idInstitucion        = (String)htDatos.get("idInstitucion");
	String idTipoEnvio          = (String)htDatos.get("idTipoEnvio");
	String idPlantillaEnvios    = (String)htDatos.get("idPlantillaEnvios");
	String sEditable            = (String)htDatos.get("editable");
	String sPlantilla           = (String)htDatos.get("plantilla");
	String descripcionPlantilla = (String)htDatos.get("descripcionPlantilla");
	
	String sModo = "consultar";
	boolean bEditable = sEditable.equals("1") ? true : false;
	if (bEditable)
		sModo="editar";

	String[] lista = new String[2];
	if (idTipoEnvio.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)) {
		lista[0] = "74e";
	} else 
	if (idTipoEnvio.equals(EnvTipoEnviosAdm.K_SMS) || idTipoEnvio.equals(EnvTipoEnviosAdm.K_BUROSMS)) {
		lista[0] = "74c";
		lista[1] = "74b";
	} else {
		lista[0] = "74c";
		lista[1] = "74e";
	} 
	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				document.frames["mainPestanas"].location.href=document.frames["mainPestanas"].location.href;
			}

			function refrescarLocal()
			{
				buscar();
			}
		</script>
	</head>

	<body onload="ajusteAlto('mainPestanas');return activarPestana();">
		<div class="posicionPestanas">
			<html:form action="/ENV_Campos_Plantillas.do" method="POST">
				<html:hidden property="modo" value="<%=sModo%>"/>
				<html:hidden property="hiddenFrame" value="1"/>
				<html:hidden property="actionModal" value=""/>
				
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="idTipoEnvio" value="<%=idTipoEnvio%>"/>
				<html:hidden property="idPlantillaEnvios" value="<%=idPlantillaEnvios%>"/>
				<html:hidden property="descripcionPlantilla" value="<%=descripcionPlantilla%>"/>
				<html:hidden property="editable" value="<%=sEditable%>"/>
				<html:hidden property="plantilla" value="<%=sPlantilla%>"/>

				<siga:PestanasExt pestanaId="PENVIOS" target="mainPestanas" parametros="htDatos" elementoactivo="1" procesosinvisibles="<%=lista%>"/>
			</html:form> 
		</div>

		<iframe src="<%=app%>/html/jsp/general/blank.jsp"
				id="mainPestanas"
				name="mainPestanas" 
				scrolling="no"
				frameborder="0"
				class="framePestanas">
		</iframe>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>