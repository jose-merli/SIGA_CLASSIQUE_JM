<!-- busquedaPrision.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
%>	
	
<html>

<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="gratuita.datosPrision.cabecera" localizacion="gratuita.datosPrision.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">
	
		<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_MantenimientoPrisiones.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscarPor"/>
		<html:hidden property = "actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.prision">	
						<table class="tablaCampos" border="0" align="left" width="100">
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>
								</td>	
								<td >
									<html:text name="MantenimientoPrisionForm" property="nombreBusqueda" size="40" styleClass="box"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
								</td>
								<td >
									<siga:ComboBD nombre="idInstitucionPrision" tipo="cmbInstitucionPropias" parametro="<%=parametro%>" clase="boxCombo" obligatorio="false" />
								</td>
							</tr>
							<tr>				
								<td class="labelText">
									<siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>
								</td>
								<td >
									<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="boxCombo" obligatorio="false" accion="Hijo:idPoblacion"/>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>
								</td>
								<td >
									<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="true" hijo="t"/> 
								</td>									
							</tr>								
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td colspan="3">
									<html:text name="MantenimientoPrisionForm" property="codigoExtBusqueda" size="10" maxlength="10" styleClass="box"></html:text>
								</td>
							</tr>								
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
	</html:form>
		</table>
	

	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo="gratuita.mantenimientoTablasMaestra.literal.prision"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				document.MantenimientoPrisionForm.modo.value = "buscarPor";
				document.MantenimientoPrisionForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.MantenimientoPrisionForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.MantenimientoPrisionForm.name,"M");
			if (resultado) {
				buscar();
			}
		}
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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