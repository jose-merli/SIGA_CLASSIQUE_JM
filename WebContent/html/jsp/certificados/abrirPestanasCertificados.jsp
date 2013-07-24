<!DOCTYPE html>
<html>
<head>
<!-- abrirPestanasCertificados.jsp -->
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
<%@ page import="com.siga.administracion.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	

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


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<siga:Titulo titulo="certificados.mantenimiento.titulo" localizacion="menu.certificados"/>

		<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() {
				window.frames["mainPestanas"].location.href=window.frames["mainPestanas"].location.href;
			}

			function refrescarLocal() {
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
				<html:hidden styleId="modo"  property="modo" value="<%=sModo%>"/>
				<html:hidden styleId="hiddenFrame" property="hiddenFrame" value="1"/>
				<html:hidden styleId="actionModal" property="actionModal" value=""/>
				
				<html:hidden styleId="idInstitucion" property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden styleId="idTipoProducto" property="idTipoProducto"  value="<%=idTipoProducto%>"/>
				<html:hidden styleId="idProducto"  property="idProducto" value="<%=idProducto%>"/>
				<html:hidden styleId="idProductoInstitucion" property="idProductoInstitucion" value="<%=idProductoInstitucion%>"/>
				
				<html:hidden styleId="certificado"  property="certificado" value="<%=sCertificado%>"/>
				<html:hidden styleId="editable"  property="editable" value="<%=sEditable%>"/>
				
				<html:hidden styleId="descripcionCertificado"  property="descripcionCertificado" value="<%=descCertificado%>"/>

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