<!DOCTYPE html>
<html>
<head>
<!-- busquedaProductos.jsp -->
<!-- 
	 Muestra el formulario de busqueda de productos
	 VERSIONES:
		 v2.0 miguel.villegas 1/2/2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();

	// Institucion del usuario de la aplicacion
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion

    // Botones a mostrar
	String botones = "B,N,CON";
	String titulo = "productos.busquedaProductos.literal.busquedaProductos";
	
   //Parametro para la busqueda:
   String [] parametroCombo = {idInstitucion};
%>
	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoProductosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="pys.busquedaProductos.cabecera" localizacion="pys.busquedaProductos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>


<body onLoad="ajusteAlto('resultado');">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->

	<table class="tablaCentralCampos" align="center">
		<tr>				
			<td>
				<siga:ConjCampos leyenda="pys.busquedaProductos.leyenda">
					<table class="tablaCampos" align="center">	
						<html:form action="/PYS_MantenimientoProductos.do" method="POST" target="resultado">
							<html:hidden property = "actionModal" value=""/>
							<html:hidden property = "modo" value = "buscarPor"/>
							<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
							<input type="hidden" name="limpiarFilaSeleccionada" value="">	
												
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProductos.literal.tipo"/>
								</td>	
								<td>
									<siga:ComboBD nombre = "busquedaTipo" tipo="cmbTipoProducto" clase="boxCombo" obligatorio="false" accion="Hijo:busquedaCategoria" ancho="455"/>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProductos.literal.categoria"/>
								</td>	
								<td>										
									<siga:ComboBD nombre = "busquedaCategoria" tipo="cmbProducto" parametro="<%=parametroCombo%>" clase="boxCombo" obligatorio="false"  hijo="t" ancho="200"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProductos.literal.producto"/>
								</td>
								<td>
									<html:text property="busquedaProducto" styleClass="box" size="70" maxlength="100" value=""></html:text>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProductos.literal.formaPago"/>
								</td>	
								<td>										
									<siga:ComboBD nombre = "busquedaPago" tipo="cmbFormaPago" clase="boxCombo" obligatorio="false" ancho="200"/>
								</td>
							</tr>	
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="pys.busquedaProducto.literal.estado"/>
								</td>	
								<td>										
									<select name="busquedaEstado" id="busquedaEstado" class="boxCombo">
										<option value="alta" selected><siga:Idioma key="pys.busquedaProducto.estado.alta"/></option>
										<option value="baja"><siga:Idioma key="pys.busquedaProducto.estado.baja"/></option>
									</select>
								</td>
							</tr>
						</html:form>	
					</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</table>
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_PRODUCTOSYSERVICIOS%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->									
							
	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

    <siga:ConjBotonesBusqueda botones="<%=botones%>" titulo="<%=titulo%>"/>
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		// Funcion asociada a boton nuevo
		function nuevo() {							
			document.forms[0].modo.value='nuevo';
			var resultado = ventaModalGeneral(document.forms[0].name,"G");
			if (resultado=="MODIFICADO") {
				buscar();
			}
		}
	
		// Funcion asociada a boton buscar
		function buscar() {
			sub();		
			document.forms[0].modo.value='buscarPor';
			document.forms[0].target='resultado';				
			document.forms[0].submit();
		}
		
		function consultas() {		
			document.RecuperarConsultasForm.submit();
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"></iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	</body>
</html>