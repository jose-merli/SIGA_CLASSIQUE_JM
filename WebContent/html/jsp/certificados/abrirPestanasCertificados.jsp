<!-- abrirPestanasCertificados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Hashtable htDatos = (Hashtable)request.getAttribute("htDatos");
	
	String idInstitucion=(String)htDatos.get("idInstitucion");
	String idTipoProducto=(String)htDatos.get("idTipoProducto");
	String idProducto=(String)htDatos.get("idProducto");
	String idProductoInstitucion=(String)htDatos.get("idProductoInstitucion");
	
	String sEditable = (String)htDatos.get("editable");
	String sCertificado = (String)htDatos.get("certificado");
	
	String descCertificado = (String)htDatos.get("descripcionCertificado");
	
	boolean bEditable = sEditable.equals("1") ? true : false;
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<siga:Titulo titulo="certificados.mantenimiento.titulo" localizacion="menu.certificados"/>

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
			<html:form action="/CER_Plantillas.do" method="POST">
<%
			String sModo="";
			
			if (bEditable)
			{
				sModo="editar";
			}
				
			else
			{
				sModo="consultar";
			}	
%>
				<html:hidden property="modo" value="<%=sModo%>"/>
				<html:hidden property="hiddenFrame" value="1"/>
				<html:hidden property="actionModal" value=""/>
				
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="idTipoProducto" value="<%=idTipoProducto%>"/>
				<html:hidden property="idProducto" value="<%=idProducto%>"/>
				<html:hidden property="idProductoInstitucion" value="<%=idProductoInstitucion%>"/>
				
				<html:hidden property="certificado" value="<%=sCertificado%>"/>
				<html:hidden property="editable" value="<%=sEditable%>"/>
				
				<html:hidden property="descripcionCertificado" value="<%=descCertificado%>"/>

				<siga:PestanasExt pestanaId="CERTIFIC" target="mainPestanas" parametros="htDatos" elementoactivo="1"/>
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