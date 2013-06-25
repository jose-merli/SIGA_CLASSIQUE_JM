<!-- datosGrupoFacturacion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();	
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String accion = request.getAttribute("accion").toString();
	String  nombre="", porcentajeInicio="", porcentajeExtrajudicial="", estilo="", idGrupoFacturacion="";

	try {
		Hashtable miHash = (Hashtable) request.getAttribute("DATABACKUP");		
		idGrupoFacturacion = (String) miHash.get("IDGRUPOFACTURACION");
		nombre = (String) miHash.get("NOMBRE");
		porcentajeInicio = (String) miHash.get("PORCENTAJEINICIO");
		porcentajeExtrajudicial = (String) miHash.get("PORCENTAJEEXTRAJUDICIAL");
	} catch (Exception e){};
		
	if (accion.equalsIgnoreCase("ver"))	estilo = "boxConsulta";
	else estilo = "box";
	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<html:javascript formName="DefinirGrupoFacturacionForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="gratuita.gruposFacturacion.cabecera"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">
	
	<html:form action="/JGR_MantenimientoGruposFacturacion.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "idGrupoFacturacion" value = "<%=idGrupoFacturacion%>"/>

	<tr>		
	<td>	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.gruposFacturacion.leyenda">
	<table class="tablaCampos" align="center">
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.gruposFacturacion.literal.nombre"/>
	</td>				
	<td class="labelText">
		<html:text name="DefinirGrupoFacturacionForm" property="nombre" size="20" styleClass="<%=estilo%>" value="<%=nombre%>"></html:text>
	</td>
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.gruposFacturacion.literal.porcentajeInicio"/>
	</td>				
	<td class="labelText" colspan="3">
		<html:text name="DefinirGrupoFacturacionForm" property="porcentajeInicio" size="3" styleClass="<%=estilo%>" value="<%=porcentajeInicio%>"></html:text>&nbsp;&nbsp;%
	</td>	
	</tr>

	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.gruposFacturacion.literal.porcentajeExtrajudicial"/>
	</td>				
	<td class="labelText" colspan="3">
		<html:text name="DefinirGrupoFacturacionForm" property="porcentajeExtrajudicial" size="3" styleClass="<%=estilo%>" value="<%=porcentajeExtrajudicial%>"></html:text>&nbsp;&nbsp;%
	</td>	
	</tr>	
	</table>
	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>

	<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  modo="<%=accion%>"/>

	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			if (validateDefinirGrupoFacturacionForm(document.DefinirGrupoFacturacionForm)){
		        document.forms[0].target = "submitArea";
				document.forms[0].modo.value = '<%=accion%>';
	        	document.forms[0].submit();
			}	        
		}				
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");
		}
	</script>
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe></body>
</html>