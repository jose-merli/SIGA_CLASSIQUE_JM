<!DOCTYPE html>
<html>
<head>
<!-- busquedaAreas.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String area="", materia="", busquedaRealizada="";
	try {
		Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
		ses.removeAttribute("DATOSFORMULARIO");
		materia = (String)miHash.get("NOMBREMATERIA");
		area = (String)miHash.get("NOMBREAREA");
		busquedaRealizada = (String)miHash.get("BUSQUEDAREALIZADA");
		
	}catch (Exception e){};

%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="menu.justiciaGratuita.maestros.AreasYMaterias" 
		localizacion="gratuita.areas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');<%if (busquedaRealizada.equals("1")) {%>buscar();<%}%>" >

	<fieldset>

	<table class="tablaCentralCampos" align="center">

	<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
	<html:hidden property = "accion" value = "area"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "actionModal" value = ""/>
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
  		
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaAreas.literal.nombreArea"/>
	</td>
	<td class="labelText">
		<html:text name="DefinirAreasMateriasForm" property="nombreArea" size="30" maxlength="60" styleClass="box" value="<%=area%>"></html:text>
	</td>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaAreas.literal.nombreMateria"/>
	</td>
	<td class="labelText">
		<html:text name="DefinirAreasMateriasForm" property="nombreMateria" size="30" maxlength="60" styleClass="box"  value="<%=materia%>"></html:text>
	</td>
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.busquedaAreas.literal.consultaAreas" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();			
			document.forms[0].target="resultado";
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].submit();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>