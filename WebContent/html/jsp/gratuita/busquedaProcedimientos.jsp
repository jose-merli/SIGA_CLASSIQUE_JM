<!-- busquedaProcedimientos.jsp -->
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
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.procedimientos.cabecera" 
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onload="ajusteAlto('resultado');">

		<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="resultado">

			<html:hidden property = "modo" value = "buscarPor"/>
			<html:hidden property = "actionModal" value = ""/>
			<input type="hidden" name="limpiarFilaSeleccionada" value="">

				<fieldset>
		
				<table width="100%"  align="center">
					<tr>
						<td class="labelText" >
							<siga:Idioma key="gratuita.gruposFacturacion.literal.nombre"/>
						</td>
						<td >
							<html:text name="MantenimientoProcedimientosForm" property="nombre" size="50" maxlength="100" styleClass="box" value=""></html:text>
						</td>	
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
						</td>
						<td>
							<html:text name="MantenimientoProcedimientosForm" property="codigoBusqueda" size="10" maxlength="20" styleClass="box"></html:text>
						</td>
					</tr>
				</table>	
				</fieldset>
			
		</html:form>
		
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.procedimientos.mantenimiento.cabecera1" />

		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0";					 
					class="frameGeneral">
	</iframe>
		

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();			
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
			document.forms[0].modo.value="nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if(resultado=='MODIFICADO') buscar();
		}
		
	</script>
			
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>