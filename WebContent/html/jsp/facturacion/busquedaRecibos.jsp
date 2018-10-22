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
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacion.form.DevolucionesManualesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.ArrayList"%>

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
	
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(user, "general.boton.guardarCerrar"));
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">

	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>

	<siga:Titulo titulo="facturacion.devolucionManual.titulo" localizacion="facturacion.devolucionManual.localizacion"/>

	<html:javascript formName="DevolucionesManualesForm" staticJavascript="false" />  
</head>

<body onload="ajusteAltoBotones('resultado'); <%=funcionBuscar%>">
	<html:form action="/FAC_DevolucionesManual.do?noReset=true" method="POST" target="resultado">
		<html:hidden property="modo" value = ""/>
		<html:hidden property="recibos"/>
		<html:hidden property="fechaDevolucion" />
		<html:hidden property="aplicarComisiones" />
		<html:hidden property="seleccionarTodos" />
		<html:hidden property="titular" styleId="titular" />										
		<html:hidden property="nombreTitular" styleId="nombreTitular" />
		<html:hidden property="identificacionTitular" styleId="identificacionTitular" />
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	    <input type="hidden" name="actionModal" value="">
	    
		<div id="divDatosDevolucionManual" title="<siga:Idioma key='facturacion.devolucionManual.datosDevolucion'/>" style="display:none">
			<table align="left">
				<tr>		
					<td class="labelText" nowrap style="color:black">
						<siga:Idioma key="facturacion.devolucionManual.fechaDevolucion"/>&nbsp;(*)
					</td>
					<td>
						<siga:Fecha nombreCampo="dialogFechaDevolucion" valorInicial="" />
					</td>
				</tr>		
				
				<tr>
					<td class="labelText" style="color:black">
						<siga:Idioma key="facturacion.devolucionManual.aplicarComisiones"/>
					</td>
					<td>
						<input type="checkbox" id="dialogAplicarComisiones">
					</td>
				</tr>			
			</table>			
		</div>		    
		
		<siga:ConjCampos leyenda="facturacion.devolucionManual.criterios">	
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.devolucionManual.numeroFactura"/></td>
					<td><html:text styleClass="box" property="numeroFactura" styleId="numeroFactura" maxlength="12" /></td>
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
					<td><html:text styleClass="box" property="numeroRecibo" styleId="numeroRecibo" maxlength="12" /></td>
				
					<td class="labelText" width="80px"><siga:Idioma key="facturacion.devolucionManual.numeroRemesa"/></td>
					<td><html:text styleClass="box" property="numeroRemesa" styleId="numeroRemesa" size='10' maxlength="12" /></td>

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
	
	<script language="JavaScript">		
	
		function buscarPaginador() {		
			jQuery("#numeroNifTagBusquedaPersonas").val("<%=sIdentificacionTitular%>");
			jQuery("#nombrePersona").val("<%=sNombreTitular%>");
			document.DevolucionesManualesForm.modo.value="buscar";
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
			//sub();		
			mensaje();
			
			jQuery("#nombreTitular").val(jQuery("#nombrePersona").val());
			jQuery("#identificacionTitular").val(jQuery("#numeroNifTagBusquedaPersonas").val());
			
			// Rango Fechas (desde / hasta)
			if (compararFecha (document.DevolucionesManualesForm.fechaCargoDesde, document.DevolucionesManualesForm.fechaCargoHasta) == 1) {
				var texto = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
				alert(texto);
				fin();
				return false;
			}
			
			var datosFiltro = "";
			datosFiltro += jQuery("#numeroFactura").val();
			datosFiltro += jQuery("#numeroNifTagBusquedaPersonas").val();
			datosFiltro += jQuery("#nombrePersona").val();
			if (jQuery("#destinatario").exists())
				datosFiltro += jQuery("#destinatario").val();
			datosFiltro += jQuery("#numeroRecibo").val();
			datosFiltro += jQuery("#numeroRemesa").val();
			datosFiltro += jQuery("#fechaCargoDesde").val();
			datosFiltro += jQuery("#fechaCargoHasta").val();
			
			if (datosFiltro == "") {
				var texto = '<siga:Idioma key="errors.filter.required"/>';
				alert(texto);
				fin();
				return false;
			}
			
			if (modo) {
				document.DevolucionesManualesForm.modo.value = modo;
				
			} else {
				/*var checkTodos = jQuery("#resultado").contents().find("#chkGeneral");
				if (!checkTodos.exists() || !checkTodos[0].checked) {
					document.DevolucionesManualesForm.seleccionarTodos.value = "";
				}*/
				document.DevolucionesManualesForm.modo.value = "BuscarInicio";
			}
			
			document.DevolucionesManualesForm.recibos.value="";
			document.DevolucionesManualesForm.target="resultado";	
			document.DevolucionesManualesForm.submit();
		}
		
		function seleccionarTodos(pagina) {
			document.DevolucionesManualesForm.seleccionarTodos.value = pagina;
			buscar('buscar');				
		}	
	
		// Asociada al boton ProcesarDevoluciones
		function accionProcesarDevoluciones() {		
			//sub();
			
			var numeroFacturasSeleccionadas;
			
			try{
				numeroFacturasSeleccionadas =  jQuery("#resultado").contents().find("#registrosSeleccionadosPaginador").val();
			}catch(e){
				numeroFacturasSeleccionadas = sessionStorage.getItem("registrosSeleccionadosPaginador");
			}
			
			
			if (numeroFacturasSeleccionadas==null || numeroFacturasSeleccionadas == 0) {
				var mensaje2 = '<siga:Idioma key="messages.fact.error.noRecibos"/>';
				alert(mensaje2);
				//fin();
				return false;
				
			} else if (numeroFacturasSeleccionadas>1000) {
				alert ('<siga:Idioma key="facturacion.devolucionManual.error.devolverMilFacturas"/>');
				//fin();
				return false;
			}
			
			jQuery("#dialogFechaDevolucion").val("");
	        jQuery("#dialogAplicarComisiones").prop('checked', false);
			
	        //fin();
			jQuery("#divDatosDevolucionManual").dialog({
				height: 180,
				width: 400,
				modal: true,
				resizable: false,
				buttons: {
					"<%=sDialogBotonGuardarCerrar%>": function() {
						sub();
						var dialogFechaDevolucion = jQuery("#dialogFechaDevolucion");
						
						if (!dialogFechaDevolucion.exists() || dialogFechaDevolucion.val()=="") {
							var mensaje = "<siga:Idioma key='errors.required' arg0='facturacion.devolucionManual.fechaDevolucion'/>";
							alert(mensaje);
							fin();
							return false;
						}					
						
						var datosFacturasSeleccionadas =  jQuery("#resultado").contents().find("#registrosSeleccionados").val();
						document.DevolucionesManualesForm.recibos.value = datosFacturasSeleccionadas;
						document.DevolucionesManualesForm.fechaDevolucion.value = dialogFechaDevolucion.val();
						if (jQuery("#dialogAplicarComisiones").is(':checked')) {
							document.DevolucionesManualesForm.aplicarComisiones.value = "<%=ClsConstants.DB_TRUE%>";
						} else {
							document.DevolucionesManualesForm.aplicarComisiones.value = "<%=ClsConstants.DB_FALSE%>";
						}
						
						document.DevolucionesManualesForm.target = "submitArea";
						document.DevolucionesManualesForm.modo.value = "insertar";																				
						document.DevolucionesManualesForm.submit();
						
						window.setTimeout("fin()",5000,"Javascript");
					},
					"<%=sDialogBotonCerrar%>": function() {
						cerrarDialog();
					}
				}
			});
			jQuery(".ui-widget-overlay").css("opacity","0");	
		}	
		
		function cerrarDialog(){
			if (jQuery("#divDatosDevolucionManual").hasClass('ui-dialog-content')) {
			 jQuery("#divDatosDevolucionManual").dialog("close"); 
			}
		}
	</script>
			
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>