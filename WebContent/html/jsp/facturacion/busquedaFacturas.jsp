<!DOCTYPE html>
<html>
<head>
<!-- busquedaFacturas.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acción sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ConModuloBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.facturacion.form.BusquedaFacturaForm"%>
 
<!-- JSP -->
<%  
	HttpSession ses = request.getSession();	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	BusquedaFacturaForm form = (BusquedaFacturaForm) ses.getAttribute("BusquedaFacturaForm");

	ArrayList serieSeleccionada = new ArrayList();
	ArrayList deudorSeleccionado = new ArrayList();
	ArrayList idEstadoSeleccionado = new ArrayList();
	String buscar = (String) request.getAttribute("botonVolver_Buscar");

	if (buscar != null && buscar.equalsIgnoreCase("true")) { 
		Long idSereFacturacionAnteriorBusqueda = (Long)request.getAttribute("idSereFacturacionAnteriorBusqueda");
		if (idSereFacturacionAnteriorBusqueda != null) {
			String aux = idSereFacturacionAnteriorBusqueda.toString();
			serieSeleccionada.add(aux);
		}
		
		String deudorAux = (String)request.getAttribute("deudor");
		if (deudorAux != null) {
			deudorSeleccionado.add(deudorAux);
		}

		Integer idEstadoAux = (Integer)request.getAttribute("idEstado");
		if (idEstadoAux != null) {
			idEstadoSeleccionado.add(idEstadoAux.toString());
		}

		buscar = "buscarInicio()";
		
	} else { 
		buscar = new String("");
	}

	String sSerieFacturacion[] = new String[1];
	Integer idInstitucion = (Integer)request.getAttribute("idInstitucion");
	if (idInstitucion!= null) 
		sSerieFacturacion[0] = idInstitucion.toString();
	
	String fechaActual = UtilidadesBDAdm.getFechaBD("");
			
	String sNombreTitular = form.getNombreTitular()==null ? "": form.getNombreTitular();
	String sIdentificacionTitular = form.getIdentificacionTitular()==null ? "": form.getIdentificacionTitular();
	
	boolean esConsejo=false;
	int idConsejo = new Integer(user.getLocation()).intValue();
	if(idConsejo==2000 || idConsejo>=3000) {
		esConsejo=true;
	}		
	
	String textoInicialMotivoAnulacion = UtilidadesString.getMensajeIdioma(user,"facturacion.altaAbonos.literal.devolucion");
	int numCaracteresMaximoMotivoanulacion = 255 - textoInicialMotivoAnulacion.length();
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	  	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="facturacion.buscarFactura.cabecera" localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Validaciones en Cliente -->
	<html:javascript formName="BusquedaFacturaForm" staticJavascript="false" />  
</head>

	<script language="JavaScript">
		function inicio(){
			jQuery("#idBotonesAccion").hide();
			jQuery("#idBotonesBusqueda").removeAttr("disabled");
			jQuery("#idBotonesAccion").removeAttr("disabled");
		}

		//Funcion asociada a boton buscar
		function limpiar() {
				document.BusquedaFacturaForm.buscarNumeroFactura.value= "";
				document.BusquedaFacturaForm.buscarFechaDesde.value= "";
				document.BusquedaFacturaForm.buscarFechaHasta.value= "";
				document.BusquedaFacturaForm.buscarIdSerieFacturacion.value= "";
				document.BusquedaFacturaForm.buscarFechaGeneracion.value= "";
				document.BusquedaFacturaForm.buscarIdEstado.value= "";
				document.BusquedaFacturaForm.buscarFormaPago.value= "";
				document.BusquedaFacturaForm.buscarConfirmada.value= "";
				document.BusquedaFacturaForm.buscarContabilizada.value= "";
				if (jQuery("#deudor").exists()) {
					jQuery("#deudor").val("");
				}
				limpiarPersona();							
		}
		
		function buscarInicio() {
			jQuery("#numeroNifTagBusquedaPersonas").val("<%=sIdentificacionTitular%>");
			jQuery("#nombrePersona").val("<%=sNombreTitular%>");
			buscar();
		}

		//Funcion asociada a boton buscar
		function buscar() {					
			jQuery("#nombreTitular").val(jQuery("#nombrePersona").val());
			jQuery("#identificacionTitular").val(jQuery("#numeroNifTagBusquedaPersonas").val());
			
			jQuery("#idBotonesAccion").show();
			try{
				if((validarFecha(document.BusquedaFacturaForm.buscarFechaDesde.value))&&
					(validarFecha(document.BusquedaFacturaForm.buscarFechaGeneracion.value))&&
					(validarFecha(document.BusquedaFacturaForm.buscarFechaHasta.value))){
					// Rango Fechas (desde / hasta)
					sub();
					if (compararFecha (document.BusquedaFacturaForm.buscarFechaDesde, document.BusquedaFacturaForm.buscarFechaHasta) == 1) {
						mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
						alert(mensaje);
						fin();
						return false;
					}
					
					var datosFiltro = "";
					datosFiltro += jQuery("#buscarNumeroFactura").val();
					datosFiltro += jQuery("#buscarFechaDesde").val();
					datosFiltro += jQuery("#buscarFechaHasta").val();
					
					datosFiltro += jQuery("#buscarIdSerieFacturacion").val();
					datosFiltro += jQuery("#buscarFechaGeneracion").val();
					datosFiltro += jQuery("#buscarIdEstado").val();
					
					datosFiltro += jQuery("#buscarFormaPago").val();
					datosFiltro += jQuery("#buscarConfirmada").val();
					datosFiltro += jQuery("#buscarContabilizada").val();
					
					datosFiltro += jQuery("#numeroNifTagBusquedaPersonas").val();
					datosFiltro += jQuery("#nombrePersona").val();								
					if (jQuery("#deudor").exists())
						datosFiltro += jQuery("#deudor").val();
					
					if (datosFiltro == "") {
						var texto = '<siga:Idioma key="errors.filter.required"/>';
						alert(texto);
						fin();
						return false;
					}				
					
					document.BusquedaFacturaForm.target = "resultado";
					document.BusquedaFacturaForm.modo.value = "buscarInit";
					document.BusquedaFacturaForm.submit();
				}else{
					setFocusFormularios();
				}
			}catch(Exception){
				//no hacemos nada
			}
			jQuery("#idBotonesBusqueda").removeAttr("disabled");
			jQuery("#idBotonesAccion").removeAttr("disabled");
			
		}
		
		function consultas() {		
			document.RecuperarConsultasForm.submit();
		}
		
		// Asociada al boton Anular -> abonos masivos
		function accionAnular() {
			if(window.frames.resultado.ObjArray.length>0){
				jQuery("#idBotonesBusqueda").attr("disabled", "disabled");
				jQuery("#idBotonesAccion").attr("disabled", "disabled");
				jQuery("#dialogoAnular").dialog({
  					height: 250,
     				width: 525,
     				modal: true,
     				resizable: false,
     				buttons: {					          	
           				"Guardar y Cerrar": function() {
           					if(window.frames.document.getElementsByName("motivos")[1].value==""){
								var mensaje = '<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.Motivos"/>';
								alert(mensaje);
								jQuery("#idBotonesBusqueda").removeAttr("disabled");
    							jQuery("#idBotonesAccion").removeAttr("disabled");
    							fin();
								return false;
            				}
            	
            				anularFacturas();						            	
            				jQuery(this).dialog( "close" );
            				document.AltaAbonosForm.facturas.value="";
            				document.AltaAbonosForm.fecha.value="";
            				document.AltaAbonosForm.motivos.value="";
           				},
           				"Cerrar": function() {
           	 				jQuery(this).dialog( "close" );
           				}
					},
       				close: function() { 
	        	 		document.AltaAbonosForm.facturas.value="";
			         	document.AltaAbonosForm.fecha.value="";
			         	document.AltaAbonosForm.motivos.value="";
					    jQuery("#motivos").val("");
				        jQuery("#idBotonesBusqueda").removeAttr("disabled");
			    		jQuery("#idBotonesAccion").removeAttr("disabled");
		    		}
			    });
				jQuery(".ui-widget-overlay").css("opacity","0");

			} else {
				fin();
				var mensaje = '<siga:Idioma key="general.message.seleccionar"/>';
				alert(mensaje);
				return false;
			}	
		}
		
		function anularFacturas(){
			sub();
		
			if (window.frames.resultado.ObjArray.length>1000) {
				jQuery("#idBotonesBusqueda").removeAttr("disabled");
				jQuery("#idBotonesAccion").removeAttr("disabled");
				alert ('<siga:Idioma key="facturacion.anulacion.error.anularMilFacturas"/>');
				fin();
				return false;
			}
		
			for (var i=0; i<window.frames.resultado.ObjArray.length; i++) {
				if (document.AltaAbonosForm.facturas.value=="") {
					document.AltaAbonosForm.facturas.value += window.frames.resultado.ObjArray[i];						
				} else {
					document.AltaAbonosForm.facturas.value += ";" +  window.frames.resultado.ObjArray[i];
				}	
			}
		
			//estos dos campos están dentro del jdialog
			document.AltaAbonosForm.motivos.value=window.frames.document.getElementsByName("motivos")[1].value;
			document.AltaAbonosForm.fecha.value=window.frames.document.getElementsByName("fecha")[1].value;
			document.AltaAbonosForm.target = "submitArea";
			document.AltaAbonosForm.modo.value="anularFacturas";
			document.AltaAbonosForm.submit();
			jQuery("#idBotonesBusqueda").removeAttr("disabled");
			jQuery("#idBotonesAccion").removeAttr("disabled");
		}
		function accionDescargarSel(fila) {
			sub();
			document.BusquedaFacturaForm.facturas.value = '';
			numElementosSeleccionados = window.frames.resultado.ObjArray.length;
			if(numElementosSeleccionados==0 ){
				alert("<siga:Idioma key='general.message.seleccionar' />");
				fin();
				return false;
				
			}else if(numElementosSeleccionados>50 ){
				confirmar = "<siga:Idioma key='general.confirmar.demora' arg0='"+numElementosSeleccionados+"'/>";
				if(!confirm(confirmar)){
					fin();
					return false;
				}
			}
			for (var i=0; i<window.frames.resultado.ObjArray.length; i++) {
				
				if (document.BusquedaFacturaForm.facturas.value=="") {
					document.BusquedaFacturaForm.facturas.value += window.frames.resultado.ObjArray[i];						
				} else {
					document.BusquedaFacturaForm.facturas.value += ";" +  window.frames.resultado.ObjArray[i];
				}
		
			}
			document.BusquedaFacturaForm.target = "submitArea";
			document.BusquedaFacturaForm.modo.value="descargaFacturas";
			document.BusquedaFacturaForm.submit();
		
		}
		
		
		function seleccionarTodos(pagina) {
			document.forms[0].seleccionarTodos.value = pagina;
			buscar('buscarPor');				
		}		
		
		function controlCaracteresMotivo(objThis) {
			cuenta(objThis,<%=numCaracteresMaximoMotivoanulacion%>);
		}
	</script>

<body onload="ajusteAltoBotones('resultado');inicio();<%=buscar%>">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
	
	<html:form action="/FAC_BusquedaFactura.do" method="POST" target="resultado">
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden property="modo" value=""/>
		<html:hidden property="seleccionarTodos" value=""/>		
		<html:hidden property="titular" styleId="titular" />										
		<html:hidden property="nombreTitular" styleId="nombreTitular" />
		<html:hidden property="identificacionTitular" styleId="identificacionTitular" />
		<html:hidden property="facturas" styleId="facturas" value="" />
		
	
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.buscarFactura.leyenda">	
						<table class="tablaCampos" align="center"  cellspacing=0 cellpadding=0>
							<tr>				
								<td width="140px" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.NumeroFactura"/></td>
								<td width="230px"><html:text styleClass="box" property="buscarNumeroFactura" styleId="buscarNumeroFactura" maxlength="20"/></td>

								<td width="140px" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaDesde"/></td>
								<td width="140px"><siga:Fecha nombreCampo="buscarFechaDesde" valorInicial="<%=form.getBuscarFechaDesde()%>"/></td>
		
								<td width="120px" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaHasta"/></td>
								<td><siga:Fecha nombreCampo="buscarFechaHasta" valorInicial="<%=form.getBuscarFechaHasta()%>"/></td>
							</tr>
		
							<tr>				
								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.SerieFacturacion"/></td>
								<td><siga:ComboBD nombre="buscarIdSerieFacturacion" tipo="cmbSerieFacturacion" clase="boxCombo" obligatorio="false" parametro="<%=sSerieFacturacion%>" elementoSel="<%=serieSeleccionada%>"/></td>

								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaGeneracion"/></td>
								<td><siga:Fecha nombreCampo="buscarFechaGeneracion" valorInicial="<%=form.getBuscarFechaGeneracion()%>"/></td>

								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Estado"/></td>
								<td><siga:ComboBD nombre="buscarIdEstado" tipo="cmbEstadosFactura"  clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoSeleccionado%>"/></td>
							</tr>
				
							<tr>
								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FormaPago"/></td>
								<td>
									<html:select name="BusquedaFacturaForm" property="buscarFormaPago" styleId="buscarFormaPago" styleClass="boxCombo">
										<html:option value=""></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.TIPO_CARGO_BANCO)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Banco"/></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.TIPO_CARGO_CAJA)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Caja"/></html:option>
									</html:select>
								</td>

								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Confirmada"/></td>
								<td>
									<html:select name="BusquedaFacturaForm" property="buscarConfirmada" styleId="buscarConfirmada" styleClass="boxCombo">
										<html:option value=""></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.DB_TRUE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Sí"/></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.DB_FALSE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.No"/></html:option>
									</html:select>
								</td>

								<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Contabilizada"/></td>
								<td>
									<html:select name="BusquedaFacturaForm" property="buscarContabilizada" styleId="buscarContabilizada" styleClass="boxCombo">
										<html:option value=""></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.DB_TRUE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Sí"/></html:option>
										<html:option value="<%=String.valueOf(ClsConstants.DB_FALSE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.No"/></html:option>
									</html:select>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>	

					<siga:ConjCampos leyenda="facturacion.buscarCliente.leyenda">	
						<table class="tablaCampos" align="center" border="0">
							<tr>
<%
								if (esConsejo) {
%>	
										<td><siga:BusquedaPersona tipo="personas" idPersona="titular"/></td>
										<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Deudor"/>&nbsp;</td>
										<td><siga:ComboBD nombre="deudor" tipo="cmbDeudores" ancho="170" clase="boxCombo" obligatorio="false" elementoSel="<%=deudorSeleccionado%>"/></td>
<%
								} else { 
%>
									<td><siga:BusquedaPersona tipo="colegiado" idPersona="titular"/></td>
<% 	
								}
%>
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
	<siga:ConjBotonesBusqueda botones="B,L,CON" titulo="facturacion.buscarFactura.cabecera"/>
	<!-- FIN: BOTONES BUSQUEDA -->

	<!-- FORMULARIO PARA RECOGER LOS DATOS DE LA BUSQUEDA -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  		<input type="hidden" name="actionModal" value="">
  		<input type="hidden" name="modo" value="abrirBusquedaModal">
  		<input type="hidden" name="clientes" value="1">
 	</html:form>
 	
 	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_FACTURACION%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>
	
	<html:form action="/FAC_AltaAbonos.do" method="POST" target="mainWorkArea">
		<html:hidden property="facturas" value=""/>
		<html:hidden property="motivos" value=""/>
		<html:hidden property="fecha" value="<%=fechaActual%>"/>
		<html:hidden property="modo" value=""/>
	</html:form>

	<div id="dialogoAnular" title="<siga:Idioma key='facturacion.altaAbonos.literal.cabeceraDevolucion'/>" style="display:none">
		<siga:ConjCampos leyenda="facturacion.altaAbonos.literal.datosAbono">
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/></td>	
					<td><html:text styleId="fecha" property="fecha" size="10" maxlength="10"  readonly="true" value="<%=fechaActual%>"/></td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.altaAbonos.literal.motivos"/>&nbsp;(*)</td>
					<td><html:textarea styleId="motivos" property="motivos" onkeydown="controlCaracteresMotivo(this)" onchange="controlCaracteresMotivo(this)" rows="4" value="" style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;"/></td>
				</tr>
			</table>
		</siga:ConjCampos>
	</div>

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>
	
	<!-- BOTONES ACCION: an: "Anular" -->	 
	<siga:ConjBotonesAccion botones="an,dse"/>
	<!-- FIN: BOTONES ACCION -->
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>