<!DOCTYPE html>
<html>
<head>
<!-- busquedaRecibos.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacion.form.DevolucionesManualesForm"%>

<%  
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	DevolucionesManualesForm form = (DevolucionesManualesForm) request.getSession().getAttribute("DevolucionesManualesForm");
	
	String sTitular = form.getTitular()==null ? "": form.getTitular();
	String sNombreTitular = form.getNombreTitular()==null ? "": form.getNombreTitular();
	String sIdentificacionTitular = form.getIdentificacionTitular()==null ? "": form.getIdentificacionTitular();
	
	boolean esConsejo=false;
	int idConsejo = new Integer(user.getLocation()).intValue();
	ArrayList deudorSeleccionado = new ArrayList();
	if(idConsejo==2000 || idConsejo>=3000) {
		esConsejo=true;
		
		String idDeudor = form.getDestinatario();
		if (idDeudor != null) {
			deudorSeleccionado.add(idDeudor);
		}		
	}	
	
	String buscar = (String)request.getAttribute("buscar");
	String funcionBuscar = "";
	if (buscar!=null && buscar.equals("1")) {
		funcionBuscar = "buscarPaginador()";
	}
	if (form.getHayMotivos().equals("0")) {
		funcionBuscar = "mensaje()";
	}
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>

	<siga:Titulo titulo="facturacion.devolucionManual.titulo" localizacion="facturacion.devolucionManual.localizacion"/>

	<html:javascript formName="DevolucionesManualesForm" staticJavascript="false" />  
</head>

<script language="JavaScript">

	function buscarPaginador() {		
		jQuery("#numeroNifTagBusquedaPersonas").val("<%=sIdentificacionTitular%>");
		jQuery("#nombrePersona").val("<%=sNombreTitular%>");
		document.DevolucionesManualesForm.modo.value="BuscarInicio";
		document.DevolucionesManualesForm.target="resultado";	
		document.DevolucionesManualesForm.submit();	
	}
	
	function mensaje() {
<% 		
		if (form.getHayMotivos().equals("0")) { 
%>
			// NO HAY MOTIVOS
			var m = '<siga:Idioma key="messages.fact.error.motivosNoCargados"/>';
			alert(m);
			return false;
<% 		
		} 
%>	
	}
	
	// Funcion asociada a boton buscarCliente
	function refrescarLocal() {
		buscar();
	}

	// Funcion asociada a boton buscar
	function buscar(modo) {
		sub();		
		mensaje();
		
		jQuery("#nombreTitular").val(jQuery("#nombrePersona").val());
		jQuery("#identificacionTitular").val(jQuery("#numeroNifTagBusquedaPersonas").val());
		
		// Rango Fechas (desde / hasta)
		if (compararFecha (document.DevolucionesManualesForm.fechaCargoDesde, document.DevolucionesManualesForm.fechaCargoHasta) == 1) {
			mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
			alert(mensaje);
			return false;
		}
		
		if (validateDevolucionesManualesForm(document.DevolucionesManualesForm)){
			if (modo) {
				document.DevolucionesManualesForm.modo.value = modo;
				
			} else {
				var checkTodos = jQuery("#resultado").contents().find("#chkGeneral");
				if (!checkTodos.exists() || !checkTodos[0].checked) {
					document.DevolucionesManualesForm.seleccionarTodos.value = "";
				}
				document.DevolucionesManualesForm.modo.value = "BuscarInicio";
			}
			
			document.DevolucionesManualesForm.target="resultado";	
			document.DevolucionesManualesForm.submit();
		}
	}
	
	function seleccionarTodos(pagina) {
		document.DevolucionesManualesForm.seleccionarTodos.value = pagina;
		buscar('buscar');				
	}		

	// Asociada al boton ProcesarDevoluciones
	function accionProcesarDevoluciones() {
		var numeroFacturasSeleccionadas =  jQuery("#resultado").contents().find("#registrosSeleccionadosPaginador").val();
		
		if (numeroFacturasSeleccionadas==null || numeroFacturasSeleccionadas == 0) {
			var mensaje2 = '<siga:Idioma key="messages.fact.error.noRecibos"/>';
			alert(mensaje2);
			return false;
			
		} else if (numeroFacturasSeleccionadas.length>1000) {
			alert ('<siga:Idioma key="facturacion.devolucionManual.error.devolverMilFacturas"/>');
			return false;
		}
		
		var datosFacturasSeleccionadas =  jQuery("#resultado").contents().find("#registrosSeleccionados").val();
		
		document.DevolucionesManualesForm.recibos.value = datosFacturasSeleccionadas;
		var aux = document.DevolucionesManualesForm.modo.value;
		document.DevolucionesManualesForm.modo.value = "modificar";
		var datos = ventaModalGeneral("DevolucionesManualesForm","P");
		if (datos != null && datos == "MODIFICADO") {
  	 		refrescarLocal();
	  	}
		document.DevolucionesManualesForm.modo.value = aux;		
	}	

</script>

<body onload="ajusteAltoBotones('resultado'); <%=funcionBuscar%>">
	<html:form action="/FAC_DevolucionesManual.do?noReset=true" method="POST" target="resultado">
		<html:hidden property="modo" value = ""/>
		<html:hidden property="recibos"/>
		<html:hidden property="facturas" value=""/>		
		<html:hidden property="fechaDevolucion"/>		
		<html:hidden property="banco"/>		
		<html:hidden property="seleccionarTodos" />
		<html:hidden property="titular" styleId="titular" />										
		<html:hidden property="nombreTitular" styleId="nombreTitular" />
		<html:hidden property="identificacionTitular" styleId="identificacionTitular" />
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	    <input type="hidden" name="actionModal" value="">
		
		<siga:ConjCampos leyenda="facturacion.devolucionManual.criterios">	
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.devolucionManual.numeroFactura"/></td>
					<td><html:text styleClass="box" property="numeroFactura" maxlength="12" /></td>
<%
					if (esConsejo) {
%>	
							<td colspan="6">
								<siga:BusquedaPersona tipo="personas" idPersona="titular"></siga:BusquedaPersona>								
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
							<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Deudor"/>&nbsp;</td>
							<td colspan="6"><siga:ComboBD nombre="destinatario" tipo="cmbDeudores"  ancho="170" clase="boxCombo" obligatorio="false" elementoSel="<%=deudorSeleccionado%>" /></td>
<%
					} else { 
%>
						<td colspan="6">
							<siga:BusquedaPersona tipo="colegiado"  idPersona="titular"></siga:BusquedaPersona>							
						</td>
<% 	
					}
%>
				</tr>

				<tr>							
					<td class="labelText" width="80px"><siga:Idioma key="facturacion.devolucionManual.numeroRecibo"/></td>
					<td><html:text styleClass="box" property="numeroRecibo" maxlength="12" /></td>
				
					<td class="labelText" width="80px"><siga:Idioma key="facturacion.devolucionManual.numeroRemesa"/></td>
					<td><html:text styleClass="box" property="numeroRemesa" size='10' maxlength="12" /></td>

					<td class="labelText"  width="170px"><siga:Idioma key="facturacion.devolucionManual.fechaCargoDesde"/></td>
					<td nowrap><siga:Fecha  nombreCampo="fechaCargoDesde" valorInicial="<%=form.getFechaCargoDesde()%>"/></td>

					<td class="labelText" width="60px"><siga:Idioma key="facturacion.devolucionManual.fechaCargoHasta"/></td>
					<td nowrap><siga:Fecha  nombreCampo="fechaCargoHasta" valorInicial="<%=form.getFechaCargoHasta()%>"/></td>
				</tr>
			</table>
		</siga:ConjCampos>	
	</html:form>

	<!-- BOTONES: B Buscar -->
	<siga:ConjBotonesBusqueda botones="B" />

	<!-- FORMULARIO PARA RECOGER LOS DATOS DE LA BUSQUEDA -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  		<input type="hidden" name="actionModal" value="">
  		<input type="hidden" name="modo" value="abrirBusquedaModal">
  		<input type="hidden" name="clientes" value="1">
 	</html:form>

	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"></iframe>
 	
	<!-- BOTONES ACCION: PD Procesar devoluciones -->	 
	<siga:ConjBotonesAccion botones="PD" />
			
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>