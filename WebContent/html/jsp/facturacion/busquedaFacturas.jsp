<!DOCTYPE html>
<html>
<head>
<!-- busquedaFacturas.jsp -->
<!-- EJEMPLO DE VENTANA DE BUSQUEDA -->
<!-- Contiene la zona de campos de busqueda o filtro y la barra  botones de
	 busqueda, que ademas contiene el titulo de la busqueda o lista de resultados.
	 No tiene botones de acci�n sobre los registros debido a que nisiquiera
	 necesita boton volver, ya que esta pagina representa UNA BSQUEDA PRINCIPAL
-->

<!-- CABECERA JSP -->
<%@page import="com.siga.beans.ConModuloBean"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.facturacion.form.BusquedaFacturaForm"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");

	String nombreClienteAnterior = "";
	Long idPersonaAnterior = new Long(-1);

	// Si el cliente es un letrado, le establezco a el como parte de la busqueda
	if (user.isLetrado()) {
		idPersonaAnterior=new Long(user.getIdPersona());
		CenPersonaAdm admPersona=new CenPersonaAdm(user);
		nombreClienteAnterior=admPersona.obtenerNombreApellidos(idPersonaAnterior.toString());
	}

	ArrayList serieSeleccionada = new ArrayList();
	ArrayList deudorSeleccionado = new ArrayList();
	ArrayList idEstadoSeleccionado = new ArrayList();
	String buscar = (String) request.getAttribute("botonVolver_Buscar");

	if ((buscar != null) && (buscar.equalsIgnoreCase("true"))) { 
		nombreClienteAnterior = (String)request.getAttribute("nombreClienteAnteriorBusqueda");
		idPersonaAnterior = (Long)request.getAttribute("idPersonaAnteriorBusqueda");
		
		if (idPersonaAnterior == null) idPersonaAnterior = new Long(-1);
		Long idSereFacturacionAnteriorBusqueda = (Long)request.getAttribute("idSereFacturacionAnteriorBusqueda");
		
		if (idSereFacturacionAnteriorBusqueda != null) {
			String aux = idSereFacturacionAnteriorBusqueda.toString();
			serieSeleccionada.add(aux);
		}
		
		String deudorAux = (String)request.getAttribute("deudor");
		Integer idEstadoAux = (Integer)request.getAttribute("idEstado");
		
		
		if (deudorAux != null) {
			String aux1 = deudorAux;
			deudorSeleccionado.add(aux1);
		}

		if (idEstadoAux != null) {
			String aux2 = idEstadoAux.toString();
			idEstadoSeleccionado.add(aux2);
		}

		buscar = "buscarPaginador()";
	}
	else buscar = new String("");

	String sSerieFacturacion[] = new String[1];
	Integer idInstitucion = (Integer)request.getAttribute("idInstitucion");
	if (idInstitucion!= null) 
		sSerieFacturacion[0] = idInstitucion.toString();
	
	String fechaActual = UtilidadesBDAdm.getFechaBD("");
	
%>	
	
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>

	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>	  	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="facturacion.buscarFactura.cabecera" 
		localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Validaciones en Cliente -->
	<html:javascript formName="BusquedaFacturaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>


<body onload="ajusteAltoBotones('resultado');inicio();<%=buscar%>">
<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
<table class="tablaCentralCampos" align="center">

	<html:form action="/FAC_BusquedaFactura.do" method="POST" target="resultado">
		<html:hidden property = "modo" 						value = ""/>
		<html:hidden property = "buscarIdPersona" value = "<%=idPersonaAnterior.toString()%>"/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden property="seleccionarTodos" value=""/>
		<tr><td>
			<siga:ConjCampos leyenda="facturacion.buscarFactura.leyenda">	
			<table class="tablaCampos" align="center"  cellspacing=0 cellpadding=0>
				<tr>				
					<td width="140" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.NumeroFactura"/></td>
					<td width="230"><html:text styleClass="box" property="buscarNumeroFactura" maxlength="20" /></td>

					<td width="140" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaDesde"/></td>
					<td width="140">
						<!--html:text styleClass="box" property="buscarFechaDesde" size="8" maxlength="10" readonly="true"/-->
						<siga:Fecha nombreCampo="buscarFechaDesde" />
					</td>
		
					<td width="120" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaHasta"/></td>
					<td>
						<!--html:text styleClass="box" property="buscarFechaHasta" size="8" maxlength="10" readonly="true"/-->
						<siga:Fecha nombreCampo="buscarFechaHasta" campoCargarFechaDesde="buscarFechaDesde" />
					</td>
				</tr>
		
				<tr>				
					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.SerieFacturacion"/></td>
					<td><siga:ComboBD nombre="buscarIdSerieFacturacion" tipo="cmbSerieFacturacion" 
					clase="boxCombo" obligatorio="false" parametro="<%=sSerieFacturacion%>" elementoSel="<%=serieSeleccionada%>" /></td>

					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaGeneracion"/></td>
					<td>
						<!--html:text styleClass="box" property="buscarFechaGeneracion" size="8" maxlength="10" readonly="true"/-->
						<siga:Fecha nombreCampo="buscarFechaGeneracion" />
					</td>

					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Estado"/></td>
					<td><siga:ComboBD nombre="buscarIdEstado" tipo="cmbEstadosFactura"  clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoSeleccionado%>"/>
					</td>
				</tr>
				
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FormaPago"/></td>
					<td><html:select name="BusquedaFacturaForm" property="buscarFormaPago" styleClass="boxCombo">
								<html:option value=""></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.TIPO_CARGO_BANCO)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Banco"/></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.TIPO_CARGO_CAJA)%>"><siga:Idioma key="facturacion.buscarFactura.literal.Caja"/></html:option>
							</html:select>
					</td>

					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Confirmada"/></td>
					<td><html:select name="BusquedaFacturaForm" property="buscarConfirmada" styleClass="boxCombo">
								<html:option value=""></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.DB_TRUE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.S�"/></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.DB_FALSE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.No"/></html:option>
							</html:select>
					</td>

					<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Contabilizada"/></td>
					<td><html:select name="BusquedaFacturaForm" property="buscarContabilizada" styleClass="boxCombo">
								<html:option value=""></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.DB_TRUE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.S�"/></html:option>
								<html:option value="<%=String.valueOf(ClsConstants.DB_FALSE)%>"><siga:Idioma key="facturacion.buscarFactura.literal.No"/></html:option>
							</html:select>
					</td>

				</tr>
			</table>
		</siga:ConjCampos>	

			<siga:ConjCampos leyenda="facturacion.buscarCliente.leyenda">	
			<table class="tablaCampos" align="center" border="0">
				<tr>
					<td class="labelText" width="140"><siga:Idioma key="Colegiado Suscrito"/>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td  width="150"> 
					<% if (!user.isLetrado()) { %>
						<html:button property="idButton"  onclick="return buscarCliente();" styleClass="button"><siga:Idioma key="general.boton.search"/> </html:button>
						<html:button property="idButton" onclick="return limpiarCliente();" styleClass="button"><siga:Idioma key="general.boton.clear"/> </html:button>
					<% } %>
					</td>
					<td class="labelText" width="200"> 
					<input type="text" name="buscarNombreCliente" value="<%=UtilidadesString.mostrarDatoJSP(nombreClienteAnterior)%>" size="50" class="boxConsulta" readOnly="true"/>
					</td>
					
					<td class="labelText" ><siga:Idioma key="facturacion.buscarFactura.literal.Deudor"/>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td colspan="2"> 
					<% if (!user.isLetrado()) { %>
						<siga:ComboBD nombre="deudor" tipo="cmbDeudores"  ancho="160" clase="boxCombo" obligatorio="false" elementoSel="<%=deudorSeleccionado%>"/>
					<% } %>
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
		
		<siga:ConjBotonesBusqueda botones="B,L,CON" titulo="facturacion.buscarFactura.cabecera"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	<!-- FORMULARIO PARA RECOGER LOS DATOS DE LA BUSQUEDA -->
<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  <input type="hidden" name="actionModal" value="">
  <input type="hidden" name="modo" value="abrirBusquedaModal">
  <input type="hidden" name="clientes" value="1">
 
 </html:form>
 <html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
	<html:hidden property="idModulo" value="<%=com.siga.beans.ConModuloBean.IDMODULO_FACTURACION%>"/>
	<html:hidden property="modo" value="inicio"/>
	<html:hidden property="accionAnterior" value="${path}"/>

</html:form>
<html:form action="/FAC_AltaAbonos.do" method="POST" target="mainWorkArea">
	<html:hidden property="facturas" value=""/>
	<html:hidden property="motivos" value=""/>
	<html:hidden property="fecha" value="<%=fechaActual%>"/>
	<html:hidden property="modo" value=""/>
</html:form>	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		function inicio(){
			jQuery("#idBotonesAccion").hide();
		}
		
		//Funcion asociada a boton buscarCliente -->
		function buscarCliente() 
		{
			var datos = new Array();
			datos[0] = "1000"; 						// idpersona
			datos[1] = "2032"; 						// idInstitucion
			datos[2] = "nColegiado"; 			// Numero Colegiado
			datos[3] = "nifcif"; 					// NIF
			datos[4] = "Nombre"; 					// Nombre
			datos[5] = "Apellido1"; 			// Apellido1
			datos[6] = "Apellido2"; 			// Apellido2

			var datos = ventaModalGeneral("busquedaClientesModalForm","G");

			if ((datos == null) ||(typeof datos[0] == "undefined"))  {
			
				return false;
			}
			
			document.BusquedaFacturaForm.buscarIdPersona.value 		 = datos[0];

			document.BusquedaFacturaForm.buscarNombreCliente.value = datos[4] + " " + datos[5] + " " + datos[6];
		}

		
		
		//Funcion asociada a boton buscar -->
		function limpiarCliente() 
		{
				document.BusquedaFacturaForm.buscarIdPersona.value="-1";
				document.BusquedaFacturaForm.buscarNombreCliente.value= "";
		}

		//Funcion asociada a boton buscar -->
		function limpiar() 
		{
				document.BusquedaFacturaForm.buscarIdPersona.value="<%=idPersonaAnterior%>";
				document.BusquedaFacturaForm.buscarNumeroFactura.value= "";
				document.BusquedaFacturaForm.buscarFechaDesde.value= "";
				document.BusquedaFacturaForm.buscarFechaHasta.value= "";
				document.BusquedaFacturaForm.buscarNombreCliente.value= "<%=nombreClienteAnterior%>";
				document.BusquedaFacturaForm.buscarIdSerieFacturacion.value= "";
				document.BusquedaFacturaForm.buscarFechaGeneracion.value= "";
				document.BusquedaFacturaForm.buscarIdEstado.value= "";
				document.BusquedaFacturaForm.buscarFormaPago.value= "";
				document.BusquedaFacturaForm.buscarConfirmada.value= "";
				document.BusquedaFacturaForm.buscarContabilizada.value= "";
				document.BusquedaFacturaForm.deudor.value= "";
		}

		//Funcion asociada a boton buscar -->
		function buscar() 
		{		
			jQuery("#idBotonesAccion").show();
			if((validarFecha(document.BusquedaFacturaForm.buscarFechaDesde.value))&&
				(validarFecha(document.BusquedaFacturaForm.buscarFechaGeneracion.value))&&
				(validarFecha(document.BusquedaFacturaForm.buscarFechaHasta.value))){
				// Rango Fechas (desde / hasta)
				sub();
				if (compararFecha (document.BusquedaFacturaForm.buscarFechaDesde, document.BusquedaFacturaForm.buscarFechaHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					fin();
					return false;
					
				}
				document.BusquedaFacturaForm.modo.value = "buscarInit";
				document.BusquedaFacturaForm.submit();
			}else{
				setFocusFormularios();
			}		
		}
		function buscarPaginador() 
		{		
				
				// Rango Fechas (desde / hasta)
				if (compararFecha (document.BusquedaFacturaForm.buscarFechaDesde, document.BusquedaFacturaForm.buscarFechaHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					return false;
				}
				document.BusquedaFacturaForm.modo.value = "buscarPor";
				document.BusquedaFacturaForm.submit();
		}
		function consultas() 
		{		
			document.RecuperarConsultasForm.submit();
			
		}
		
		// Asociada al boton Anular -> abonos masivos
		function accionAnular() {
			if(window.frames.resultado.ObjArray.length>0){
				jQuery("#idButton").attr("disabled", true);
				jQuery("#dialogoAnular").dialog(
						{
						      height: 250,
						      width: 525,
						      modal: true,
						      resizable: false,
						      buttons: {					          	
						            "Guardar y Cerrar": function() {
						            
						            if(window.frames.document.getElementsByName("motivos")[1].value==""){
						            	fin();
										var mensaje = '<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.Motivos"/>';
										alert(mensaje);
										return false;
						            }
						            	
						            anularFacturas();						            	
						            jQuery( this ).dialog( "close" );
						            document.AltaAbonosForm.facturas.value="";
						            document.AltaAbonosForm.fecha.value="";
						            document.AltaAbonosForm.motivos.value="";
						            
						            },
						            "Cerrar": function() {
							              jQuery( this ).dialog( "close" );
							              document.AltaAbonosForm.facturas.value="";
								          document.AltaAbonosForm.fecha.value="";
								          document.AltaAbonosForm.motivos.value="";
							            }
						          }
						    }
					);
				jQuery(".ui-widget-overlay").css("opacity","0");
			}else{
				fin();
				var mensaje = '<siga:Idioma key="general.message.seleccionar"/>';
				alert(mensaje);
				return false;
			}	
		
		}
		
	function anularFacturas(){
		
		sub();
		 
		
		if (window.frames.resultado.ObjArray.length>1000) {
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
		//estos dos campos est�n dentro del jdialog
		document.AltaAbonosForm.motivos.value=window.frames.document.getElementsByName("motivos")[1].value;
		document.AltaAbonosForm.fecha.value=window.frames.document.getElementsByName("fecha")[1].value;
		document.AltaAbonosForm.target = "submitArea";
		document.AltaAbonosForm.modo.value="anularFacturas";
		document.AltaAbonosForm.submit();
		fin();

	}
	
	function seleccionarTodos(pagina) {
		document.forms[0].seleccionarTodos.value = pagina;
		buscar('buscarPor');				
	}
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->


	<div id="dialogoAnular" title="<siga:Idioma key='facturacion.altaAbonos.literal.cabeceraDevolucion'/>" style="display:none">
	<div>
		<siga:ConjCampos leyenda="facturacion.altaAbonos.literal.datosAbono">
			<table>
			<tr>
			<td class="labelText">
					<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>
				</td>	
				<td >
					<html:text id="fecha" property="fecha" size="10" maxlength="10"  readOnly="true" value="<%=fechaActual%>"></html:text>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="facturacion.altaAbonos.literal.motivos"/>&nbsp;(*)
				</td>
				<td >
					<html:textarea id="motivos" property="motivos" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" rows="4" size="100" value="" style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;"></html:textarea>
				</td>
			</tr>
			</table>
		</siga:ConjCampos>
	</div>
	</div>

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
	<!-- BOTONES ACCION: an: "Anular" -->	 
	<siga:ConjBotonesAccion botones="an"/>
	<!-- FIN: BOTONES ACCION -->		
<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>