<!-- busquedaAlerta.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
-->

<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.*,com.siga.expedientes.form.BusquedaAlertaForm,java.util.*"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));
	String idinstitucion = userBean.getLocation();
	String[] parametro = new String[2];
	parametro[0] = idinstitucion;
	parametro[1] = idinstitucion;
	
	Object[] aPerfiles = new Object[2];
	aPerfiles[0] = userBean.getProfile();
	aPerfiles[1] = userBean.getProfile();
	

	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String) request.getParameter("buscar");
	String funcionBuscar = "";
	if (buscar != null) {
		funcionBuscar = "buscarPag()";
	}
	String modoBuscar = "";

	ArrayList vTipoExp = new ArrayList();
	ArrayList vFase = new ArrayList();
	ArrayList vEstado = new ArrayList();
	try {
		BusquedaAlertaForm form = (BusquedaAlertaForm) session.getAttribute("busquedaAlertaForm");
		if (form == null) {
			modoBuscar = "buscarIni";
			vTipoExp.add("");
			vFase.add("");
			vEstado.add("");
		} else {
			modoBuscar = "buscar";
			vTipoExp.add(form.getComboTipoExpediente());
			vFase.add(form.getComboTipoExpediente()+","+form.getIdFase());
			vEstado.add(form.getComboTipoExpediente()+","+form.getIdFase() +","+ form.getIdEstado());
		}
	} catch (Exception e) {
		vTipoExp.add("");
		vFase.add("");
		vEstado.add("");
	}
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script>

			function valoresCombo() {
			var faseSeleccionada='<%=(String)vFase.get(0)%>';
			var estadoSeleccionado='<%=(String)vEstado.get(0)%>';
				document.forms[0].getElementById("comboTipoExpediente").onChange();
				//document.forms[0].getElementById("comboFases").selectedIndex = faseSeleccionada;
				document.forms[0].getElementById("comboFases").onChange();
				//document.forms[0].getElementById("comboEstados").selectedIndex = estadoSeleccionado;
			}
			
			function recargarCombos() {
				document.forms[0].comboTipoExpediente.onchange();
			//	var faseSeleccionada='<%=(String)vFase.get(0)%>';

			}
			
		</script>
		<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo titulo="expedientes.alertas.cabecera" localizacion="expedientes.literal.localizacion" />
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	</head>
	<style>
		.ocultoexp {
			display: none
		}

		.visibleexp {
			display: inline
		}
	</style>
	<body onload="ajusteAlto('resultado');recargarCombos();<%=funcionBuscar%>">
		<html:javascript formName="busquedaAlertaForm" staticJavascript="false" />
		<html:form action="/EXP_Consultas.do?noReset=false" method="POST" target="resultado">
			<html:hidden property="modo" value="" />
			<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<fieldset>
				<table class="tablaCampos" align="center"">
					<!-- FILA -->
					<tr>
						<td class="labelText" width="100">
							<siga:Idioma key="expedientes.auditoria.literal.tipo" />&nbsp;(*)
						</td>
						<td>
							<siga:ComboBD nombre="comboTipoExpediente" tipo="cmbTipoExpedienteLocaloGeneralPermisos" elementoSel="<%=vTipoExp%>" clase="boxCombo" obligatorio="false" parametrosIn="<%=aPerfiles%>" parametro="<%=parametro%>" accion="Hijo:comboFases" />
						</td>
						<td class="labelText" width="100">
							<siga:Idioma key="expedientes.auditoria.literal.nexpediente" />
						</td>
						<td>
							<html:text name="busquedaAlertaForm" property="anioExpediente" style="width:40px;" maxlength="4" styleClass="box"></html:text>&nbsp;/&nbsp;<html:text name="busquedaAlertaForm" property="numeroExpediente" style="width:60px;" maxlength="6" styleClass="box"></html:text>
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.fase" />
						</td>
						<td>
							<siga:ComboBD nombre="comboFases" tipo="cmbFases" clase="boxCombo" obligatorio="false" accion="Hijo:comboEstados" elementoSel="<%=vFase%>"  hijo="t" />
						</td>
						<td class="labelText"> 
							<siga:Idioma key="expedientes.auditoria.literal.estado" />
						</td>
						<td>
							<siga:ComboBD nombre="comboEstados" tipo="cmbEstados" clase="boxCombo" obligatorio="true" elementoSel="<%=vEstado%>"  hijo="t" />
						</td>
						<td width="100"></td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.fechainicial" />
						</td>
						<td>
							<html:text name="busquedaAlertaForm" property="fechaDesde" maxlength="10" size="10" styleClass="box" readonly="true">
							</html:text> 
							<a href='javascript://' onClick="return showCalendarGeneral(fechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
						</td>
						<td class="labelText">
							<siga:Idioma key="expedientes.auditoria.literal.fechafinal" />
						</td>
						<td>
							<html:text name="busquedaAlertaForm" property="fechaHasta" maxlength="10" size="10" styleClass="box" readonly="true">
							</html:text>
							<a href='javascript://' onClick="return showCalendarGeneral(fechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
						</td>
						<td></td>
					</tr>
				</table>
			</fieldset>
		</html:form>

		<!-- FIN: CAMPOS DE BUSQUEDA-->

		<!-- INICIO: BOTONES BUSQUEDA -->
		<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
		-->
		<siga:ConjBotonesBusqueda botones="B" />
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			//<!-- Funcion asociada a boton buscar -->
			function buscarPag() {
				sub();
				if (!validateBusquedaAlertaForm(document.busquedaAlertaForm)){
					fin();
					return false;
				}		
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}

			//<!-- Funcion asociada a boton buscar -->
			function buscar() {
				sub();
				if (!validateBusquedaAlertaForm(document.busquedaAlertaForm)){
					fin();
					return false;
				}	
				var numero = document.getElementById('numeroExpediente').value;
				var anio = document.getElementById('anioExpediente').value;
				
				if (((numero !="") && isNaN(numero)) || ((anio !="") && isNaN(anio))) {
					alert('<siga:Idioma key="expedientes.busquedaExpedientes.literal.errorNumeroExpediente"/>');
				}
								
				document.forms[0].modo.value="buscarIni";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
		<iframe align="middle" src="<%=app%>/html/jsp/general/blank.jsp" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral">
		</iframe>

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
