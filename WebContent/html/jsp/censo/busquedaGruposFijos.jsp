<!-- busquedaGruposFijos.jsp -->

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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>	
	
<html>

<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="censo.datosGruposFijos.cabecera" localizacion="censo.GruposFijos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/CEN_MantenimientoGruposFijos.do" method="POST" target="resultado">
		<html:hidden styleId="modo" property = "modo" value = "buscarPor"/>
		<html:hidden styleId="accionModal" property = "actionModal" value = ""/>
		<input type="hidden" id="limpiarFilaSeleccionada" name="limpiarFilaSeleccionada" value=""/>
	
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.datosGruposFijos.literal.GruposFijos">	
						<table class="tablaCampos" border="0" align="left" width="100">
							<tr>				
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>
								</td>
								<td>
									<html:text name="MantenimientoGruposFijosForm" property="nombre" size="100" styleClass="box"></html:text>
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
		
		<siga:ConjBotonesBusqueda botones="B,N" titulo="censo.datosGruposFijos.literal.GruposFijos"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				document.MantenimientoGruposFijosForm.modo.value = "buscarInit";
				document.MantenimientoGruposFijosForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.MantenimientoGruposFijosForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.MantenimientoGruposFijosForm.name,"M");
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