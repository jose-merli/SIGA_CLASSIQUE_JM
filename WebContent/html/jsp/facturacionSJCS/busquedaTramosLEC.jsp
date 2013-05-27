<!-- busquedaTramosLEC.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="FactSJCS.mantTramosLEC.cabecera" 
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->	
	<siga:ConjCampos leyenda="factSJCS.mantTramosLEC.leyenda">
	<table align="left">

	<html:form action="/FCS_MantenimientoLEC.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "actionModal" value = ""/>
  		
	<tr>
	<td class="labelText" width="150">
		<siga:Idioma key="FactSJCS.mantTramosLEC.literal.nombre"/>
	</td>
	<td class="labelText">
		<html:text name="MantenimientoProcedimientosForm" property="retencion" size="10" maxlength="3" styleClass="box" value=""></html:text>
	</td>	
	</tr>
	
	</html:form>
	
	</table>
	</siga:ConjCampos>
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="FactSJCS.mantTramosLEC.cabecera" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			var retencion = document.forms[0].retencion.value;
			if (!isNaN(retencion)) {
				document.forms[0].modo.value = "buscarPor";
				document.forms[0].submit();
			} else alert("El campo Retencion debe ser numérico");
		}		
		
		<!-- Funcion asociada a boton Nuevo -->		
		function nuevo()
		{
			document.forms[0].modo.value="nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		
	</script>

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>