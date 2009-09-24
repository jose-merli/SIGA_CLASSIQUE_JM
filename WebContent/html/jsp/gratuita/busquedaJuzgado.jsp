<!-- busquedaJuzgado.jsp -->

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
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="gratuita.datosJuzgado.cabecera" localizacion="gratuita.datosJuzgado.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	
		<table class="tablaCentralCampos" align="center">
	<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscarPor"/>
		<html:hidden property = "actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">	
						<table class="tablaCampos" border="0" align="left">
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>
								</td>	
								<td >
									<html:text name="MantenimientoJuzgadoForm" property="nombreBusqueda" size="40" styleClass="box"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
								</td>
								<td >
									<siga:ComboBD nombre="idInstitucionJuzgado" tipo="cmbInstitucionPropias" parametro="<%=parametro%>" clase="boxCombo" obligatorio="false" />
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
								<td>
									<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="boxCombo" obligatorio="true" hijo="t"/> 
								</td>									
							</tr>		
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td colspan="3">
									<html:text name="MantenimientoJuzgadoForm" property="codigoExtBusqueda" size="10" styleClass="box"></html:text>
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
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				document.MantenimientoJuzgadoForm.modo.value = "buscarPor";
				document.MantenimientoJuzgadoForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.MantenimientoJuzgadoForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.MantenimientoJuzgadoForm.name,"G");
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