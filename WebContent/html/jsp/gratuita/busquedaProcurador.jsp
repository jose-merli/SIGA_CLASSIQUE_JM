<!DOCTYPE html>
<html>
<head>
<!-- busquedaProcurador.jsp -->

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
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
%>	
	


<!-- HEAD -->

	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="gratuita.datosProcurador.cabecera" localizacion="gratuita.datosProcurador.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">

	<html:form action="/JGR_MantenimientoProcuradores.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.datosProcurador.literal.procurador">	
						<table class="tablaCampos" border="0" align="left" width="100">
							<tr>	
							   <td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codProcurador"/>
									&nbsp;
									<html:text name="MantenimientoProcuradorForm" property="codProcurador" size="10" styleClass="box"></html:text>
								</td>			
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>
									&nbsp;
									<html:text name="MantenimientoProcuradorForm" property="nombre" size="20" styleClass="box"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.apellido1"/>
									&nbsp;
									<html:text name="MantenimientoProcuradorForm" property="apellido1" size="20" styleClass="box"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.apellido2"/>
									&nbsp;
									<html:text name="MantenimientoProcuradorForm" property="apellido2" size="20" styleClass="box"></html:text>
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
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo="gratuita.datosProcurador.literal.procurador"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
		       /* if (MantenimientoProcuradorForm.codProcurador.value !="" && isNaN(MantenimientoProcuradorForm.codProcurador.value)) {
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
						return true;
				}*/
				sub();
				document.MantenimientoProcuradorForm.modo.value = "buscarPor";
				document.MantenimientoProcuradorForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.MantenimientoProcuradorForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.MantenimientoProcuradorForm.name,"M");
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