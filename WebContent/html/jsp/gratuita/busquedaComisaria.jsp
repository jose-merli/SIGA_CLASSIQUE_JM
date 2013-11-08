<!DOCTYPE html>
<html>
<head>
<!-- busquedaComisaria.jsp -->

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
		
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
%>	
	


<!-- HEAD -->

	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="gratuita.datosComisaria.cabecera" localizacion="gratuita.datosComisaria.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/JGR_MantenimientoComisarias.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscarPor"/>
		<html:hidden property = "actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.comisaria">	
						<table class="tablaCampos" border="0" align="left" width="100">
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>
								</td>	
								<td class="labelText">
									<html:text name="MantenimientoComisariaForm" property="nombreBusqueda" size="40" styleClass="box"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idInstitucionComisaria" tipo="cmbInstitucionPropias" parametro="<%=parametro%>" clase="boxCombo" obligatorio="false" />
								</td>
							</tr>
							<tr>				
								<td class="labelText">
									<siga:Idioma key="censo.SolicitudIncorporacion.literal.provincia"/>
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="boxCombo" obligatorio="false" accion="Hijo:idPoblacion"/>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.SolicitudIncorporacion.literal.poblacion"/>
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="true" hijo="t"/> 
								</td>	
								</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td class="labelText" colspan="3">
									<html:text name="MantenimientoComisariaForm" property="codigoExtBusqueda" size="10" styleClass="box"></html:text>
								</td>								
							</tr>	
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
	
	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo="gratuita.mantenimientoTablasMaestra.literal.comisaria"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				document.MantenimientoComisariaForm.modo.value = "buscarPor";
				document.MantenimientoComisariaForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.MantenimientoComisariaForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.MantenimientoComisariaForm.name,"M");
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