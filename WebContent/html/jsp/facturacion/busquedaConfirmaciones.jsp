<!-- busquedaConfirmaciones.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");

	// paraemtro para consultas de estados (Combo)
	String dato[] = new String[1];
	dato[0] = user.getLanguage().toUpperCase();
	ArrayList estadoConfirmacionSel = new ArrayList();
	estadoConfirmacionSel.add(FacEstadoConfirmFactBean.CONFIRM_PENDIENTE.toString());


%>	
	
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="facturacion.confirmarFacturacion.literal.cabecera" 
		localizacion="facturacion.confirmarFacturacion.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- Validaciones en Cliente -->
	<html:javascript formName="confirmarFacturacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>


<body onLoad="ajusteAlto('resultado');">


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

<table class="tablaCentralCampos" align="center">

	<html:form action="/FAC_ConfirmarFacturacion.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		
		<tr><td>
			<siga:ConjCampos leyenda="facturacion.confirmarFacturacion.literal.camposBusqueda">	
			<table class="tablaCampos" align="center">
				<tr>				
					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.estadoConfirmacion"/> (*)</td>
					<td>
						<siga:ComboBD nombre = "estadoConfirmacion" tipo="cmbEstadoConfirmacion"  clase="boxCombo" obligatorio="true" parametro="<%=dato%>" elementoSel="<%=estadoConfirmacionSel%>" />						
					</td>

					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.estadoPDF"/></td>
					<td>
						<siga:ComboBD nombre = "estadoPDF" tipo="cmbEstadoPDF"  clase="boxCombo" obligatorio="false" parametro="<%=dato%>" />						
					</td>
		
				</tr>
				<tr>				
					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.estadoEnvio"/></td>
					<td>
						<siga:ComboBD nombre = "estadoEnvios" tipo="cmbEstadoEnvios"  clase="boxCombo" obligatorio="false" parametro="<%=dato%>"/>						
					</td>

					<td class="labelText"><siga:Idioma key="facturacion.confirmarFacturacion.literal.archivadas"/></td>
					<td >
						<input type="checkbox" value="1" name="archivadas">
					</td>
				</tr>
		
			</table>
		</siga:ConjCampos>	

		</td></tr>

	</html:form>
	
	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B" titulo=""/>

	<!-- FIN: BOTONES BUSQUEDA -->


	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function refrescarLocal() {
			buscar();
		}
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();	
			if (validateConfirmarFacturacionForm(document.confirmarFacturacionForm)){
				setFilaSeleccionadaD('true');
				document.confirmarFacturacionForm.modo.value = "buscar";
				document.confirmarFacturacionForm.submit();
				setFilaSeleccionadaD('false');
			}else{
			
				fin();
				return false;
			
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