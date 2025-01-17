<!DOCTYPE html>
<html>
<head>
<!-- facturaPagos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="org.apache.struts.action.ActionMapping"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 
	ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
	String path = actionMapping.getPath();		
	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	String volver = request.getAttribute("volver")==null?"NO":(String)request.getAttribute("volver");
	String botonesAccion = "";

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");

	if (busquedaVolver != null && busquedaVolver.equals("DEV_MANUAL")) {
		volver="SI";
	}

	if (volver!=null && volver.equalsIgnoreCase("SI")){
		botonesAccion = "V";
	}

	String app = request.getContextPath(); 
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	

	Hashtable datos = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	String  idFactura = (String)request.getAttribute("idFactura");
	Integer idInstitucion = (Integer)request.getAttribute("idInstitucion");
	Integer estadoFactura = (Integer)request.getAttribute("estadoFactura");
	String numeroFactura = (String)request.getAttribute("numeroFactura");
	String ultimaFecha = "";
	
	Vector vPagos = null;
	Hashtable hTotales = null;
	Double total        = null;
	Double totalCaja    = null;
	Double totalTarjeta = null;
	Double totalCompensado = null;
	Double totalBanco   = null;
	Double totalAnticipado   = null;
	int formaPagoFactura  = 0;
	
	if (datos != null) {
		vPagos = (Vector) datos.get("PAGOS");
		hTotales = (Hashtable) datos.get("TOTALES");
		
		//Se calcula la fecha de la ultima l�nea
		Hashtable pagoAux = null;
		if (vPagos != null && vPagos.size() > 0) {
			pagoAux = (Hashtable) vPagos.get(vPagos.size()-1);
			ultimaFecha = UtilidadesHash.getString(pagoAux, "FECHA");
		}
	}

	if (hTotales != null) {	
		total        = UtilidadesHash.getDouble(hTotales, "TOTAL_FACTURA");
		totalCaja    = UtilidadesHash.getDouble(hTotales, "TOTALPAGADOSOLOCAJA");
		totalTarjeta = UtilidadesHash.getDouble(hTotales, "TOTALPAGADOPORTARJETA");
		totalBanco   = UtilidadesHash.getDouble(hTotales, "TOTAL_PAGADOPORBANCO");
		totalCompensado   = UtilidadesHash.getDouble(hTotales, "TOTAL_COMPENSADO");
		if (total==null) total = new Double(0.0);
		if (totalCaja==null) totalCaja = new Double(0.0);
		if (totalTarjeta==null) totalTarjeta = new Double(0.0);
		if (totalBanco==null) totalBanco = new Double(0.0);
		if (totalCompensado==null) totalCompensado = new Double(0.0);
		
		totalAnticipado   = UtilidadesHash.getDouble(hTotales, "TOTAL_ANTICIPADO");
		if (totalAnticipado==null) totalAnticipado = new Double(0.0);
		formaPagoFactura   = (UtilidadesHash.getInteger(hTotales, "FORMAPAGOFACTURA")).intValue();
		if (formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_TARJETA) {
			totalTarjeta = new Double(totalTarjeta.doubleValue());
		} else
			if (formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_FACTURA) {
				totalBanco = new Double(totalBanco.doubleValue());
			} else
				if (formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_METALICO) {
					totalCaja = new Double(totalCaja.doubleValue());
			}
	
	}

	
	// Visible botones NuevoPagoTarjeta, NuevoPagoCaja, Renegociar
	boolean mostrarBotonPago = false , mostrarBotonTarjeta = false, mostrarBotonRenegociar = false ;
	
	String modoRegistroBusqueda = (String)request.getAttribute("modoRegistroBusqueda");
	if (modoRegistroBusqueda != null 
		&& !modoRegistroBusqueda.equalsIgnoreCase("ver")) {
		
		if (estadoFactura != null && !user.isLetrado()) {
			
			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) {
				mostrarBotonPago = true;
				mostrarBotonTarjeta = true;
			}

			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)
			      || estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO)
			      || estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA)){
			    mostrarBotonRenegociar = true;
			}
		}
	}
	
	// MAV 7/7/05 a instancias de JG
	if (modoRegistroBusqueda != null 
		&& modoRegistroBusqueda.equalsIgnoreCase("ver")) {
		
		if (estadoFactura != null 
			&& estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA) 
			&& user.isLetrado()){
			mostrarBotonTarjeta = true;
		}
	}
	
 	Double pendiente = new Double(total.doubleValue());
	String mensaje = (String) request.getAttribute("mensaje");	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function refrescarLocal(){ 	
			window.location.reload();			
		}	

		//Asociada al boton Pago por caja 
		function pagoPorCaja() {
			sub();
			document.GestionarFacturaForm.action = "<%=app%>"+"/FAC_PagosFactura.do";	 
			document.GestionarFacturaForm.modo.value = "pagoPorCaja";
			rc = ventaModalGeneral(document.GestionarFacturaForm.name, "P");
			fin();
			if (rc == "MODIFICADO") {
				alert(unescape('<siga:Idioma key="messages.updated.success"/>'), 'success');
				refrescarLocal();
			}
		}	

		//Asociada al boton Pago por tarjeta
		function pagoPorTarjeta(){
			document.forms[0].modo.value = "abrir";
			document.forms[0].action = "<%=app%>"+"/FAC_PagosFacturaPorTarjeta.do";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}	

		//Asociada al boton Renegociar
		function botonRenegociar(){ 
			sub();
			document.GestionarFacturaForm.action = "<%=app%>"+"/FAC_PagosFactura.do";
			document.GestionarFacturaForm.modo.value = "pagoRenegociar";
			rc = ventaModalGeneral(document.GestionarFacturaForm.name, "M");
			fin();
			if (rc == "MODIFICADO") {
				alert(unescape('<siga:Idioma key="messages.updated.success"/>'), 'success');
				refrescarLocal();
			}
		}	
		
		function datosImpresion(fila) {
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			preparaDatos(fila, 'tablaResultados', datos);
			
			document.forms[0].modo.value = "Ver";
			ventaModalGeneral(document.forms[0].name,"P");
		}			
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
 	<% 	if (user.getStrutsTrans().equals("FAC_BusquedaFactura")) {%>
			<siga:Titulo titulo="facturacion.facturas.pagos.cabecera"	localizacion="facturacion.facturas.localizacion"/>
	<% } else if (user.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
			<siga:Titulo titulo="facturacion.facturas.pagos.cabecera"	localizacion="censo.facturacion.facturas.localizacion"/>
	<% }%>		
	<!-- FIN: TITULO Y LOCALIZACION -->	
</head>

<body>

	<!-- INICIO: CAMPOS -->
	<html:form action="<%=path%>" method="POST" target="submitArea" style="display:none">
		<html:hidden styleId = "modo" 		property = "modo" 	value = ""/>
		<html:hidden styleId = "idFactura"  property = "idFactura"		value = "<%=idFactura%>"/>
		<html:hidden styleId = "idInstitucion" property = "idInstitucion" value = "<%=String.valueOf(idInstitucion)%>"/>
		<html:hidden styleId = "numeroFactura"  property = "numeroFactura" value = "<%=numeroFactura%>"/>
		<html:hidden styleId = "datosPagosCajaImportePendiente"  property = "datosPagosCajaImportePendiente" value = "<%=String.valueOf(pendiente)%>"/>		
		<input type="hidden" id="ultimaFecha" name="ultimaFecha" value="<%=ultimaFecha%>">
	</html:form>

	<siga:Table 
	   name = "tablaResultados"
	   border  = "2"
	   columnNames="facturacion.pagosFactura.literal.Fecha,
					facturacion.pagosFactura.literal.Accion,
					facturacion.buscarFactura.literal.Estado,
					facturacion.abonosPagos.datosPagoAbono.nCuenta,
					facturacion.pagosFactura.literal.Importe,
					facturacion.pagosFactura.literal.Pendiente,"
	   columnSizes = "8,18,18,30,10,10,6"
	   fixedHeight="70%"
	   modal = "M">
	
<%	 
		if ((vPagos != null) && (vPagos.size() > 0)){			
			
			String textoCompensacion = UtilidadesString.getMensajeIdioma(user, "facturacion.pagosFactura.accion.compensacion");
			boolean seleccionado = false;
			
			for (int i = 1; i <= vPagos.size(); i++) { 					
				 Hashtable pago = (Hashtable) vPagos.get(i-1);
				 
				 if (pago != null){ 				 
					String tabla = UtilidadesHash.getString(pago, "TABLA").trim();
					String estado = UtilidadesHash.getString(pago, "ESTADO");
					String fecha = UtilidadesHash.getString(pago, "FECHA");
					Double importe = (Double)pago.get("IMPORTE");
					Double importePte =(Double)pago.get("IMPORTEPENDIENTE");
					String tarjeta = UtilidadesHash.getString(pago, "TARJETA");
					String numAbono = UtilidadesHash.getString(pago, "NUMEROABONO");
					Integer idAbonoCuenta = UtilidadesHash.getInteger(pago, "IDABONO_IDCUENTA");
					Integer idPago = UtilidadesHash.getInteger(pago, "IDPAGO");
					String nombreBanco=UtilidadesHash.getString(pago, "NOMBREBANCO");
					String idFacturaPagos = UtilidadesHash.getString(pago, "IDFACTURA");
					String esAnulacionComision = UtilidadesHash.getString(pago, "ANULACIONCOMISION");					
				 	String medioPago = "";

 					if(tabla.startsWith(textoCompensacion)){
 						//Para que en el comprobante de pago se muestre el abono en observaciones no en medio de pago
 						medioPago=textoCompensacion;
 					}else{
 						medioPago=tabla;
 					}
					FilaExtElement[] elementos=new FilaExtElement[1];
					if ((idPago != null) && (idPago.intValue() > 0)){
  						elementos[0]=new FilaExtElement("datosImpresion","datosImpresion",SIGAConstants.ACCESS_FULL);
	  				}
%>


					<siga:FilaConIconos 
						fila='<%=""+i%>'
						botones=''
						elementos='<%=elementos%>' 
						visibleConsulta='no'
						visibleEdicion="no" 
						visibleBorrado="no"
						pintarEspacio='no' 
						clase="listaNonEdit">
						
<%
						String clase="", claseNumero=""; 
						if ((esAnulacionComision!=null && esAnulacionComision.equals("1") && idFactura!=null && idFacturaPagos!=null && idFactura.equals(idFacturaPagos)) ||
								(vPagos.size()==i && !seleccionado)) {
							seleccionado = true;
							clase="class='listaNonEditSelected' style='font-weight:bold'";
							claseNumero="class='listaNonEditSelected' style='font-weight:bold; text-align:right'";
						}
%>						
						
						<td <%=clase%>>
							<input type="hidden" id="oculto<%=i%>_1" value="<%=idPago%>">
							<input type="hidden" id="oculto<%=i%>_2" value="<%=medioPago%>">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fecha))%>
						</td>
						<td <%=clase%>><%=UtilidadesString.mostrarDatoJSP(tabla)%></td>
						<td <%=clase%>><%=UtilidadesString.mostrarDatoJSP(estado)%></td>
						<td <%=clase%>><%=UtilidadesString.mostrarDatoJSP(nombreBanco)%></td>
						<td align="right" <%=claseNumero%>><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(importe.doubleValue()))%>&nbsp;&euro;</td>
						<td align="right" <%=claseNumero%>><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(importePte.doubleValue()))%>&nbsp;&euro;</td>
<%						
						if (idPago == null || idPago.intValue()<=0){
%>						
							<td>&nbsp;</td>	
<%
						}		
%>		


					</siga:FilaConIconos>
<%	 		 
				} // if
			}  // for
		} else {  
%>
			<tr class="notFound">
  				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		}
%>								
	</siga:Table> 
	
	<div id="totales" style="bottom:70px; left:200px; position:absolute; width:250px">
		<fieldset>
			<table  align="left" border="0" width="100%">
				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalFactura"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.formatoImporte(total)%>&nbsp;&euro;</td>
				</tr>
			</table>
		</fieldset>
	</div>
	
	<div id="totales2" style="bottom:70px; left:580px; position:absolute; width:250px;">
		<fieldset>
			<table  align="left" border="0" width="100%">
				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalAnticipado"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(UtilidadesNumero.redondea(totalAnticipado.toString(),2)))%>&nbsp;&euro;</td>
				</tr>

				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalCaja"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(UtilidadesNumero.redondea(totalCaja.toString(),2)))%>&nbsp;&euro;</td>
				</tr>

				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalTarjeta"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(UtilidadesNumero.redondea(totalTarjeta.toString(),2)))%>&nbsp;&euro;</td>
				</tr>
					
				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalBanco"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(UtilidadesNumero.redondea(totalBanco.toString(),2)))%>&nbsp;&euro;</td>
				</tr>
					
				<tr>				
					<td class="labelText" nowrap><siga:Idioma key="facturacion.pagosFactura.literal.TotalCompensado"/></td>
					<td class="labelTextNum" align="right" nowrap><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(UtilidadesNumero.redondea(totalCompensado.toString(),2)))%>&nbsp;&euro;</td>
				</tr>
			</table>
		</fieldset>			
	</div>
	<%if(mostrarBotonPago||mostrarBotonTarjeta||mostrarBotonRenegociar){ %>
	<div style="position:absolute;left:23px;bottom:35px;">
		<table align="center" border="0" width="25%" >
			<tr>
				<td class="tdBotones">
					<%if(mostrarBotonPago) {%>
						<input type="button" name="pagoCaja" id="idButton" value="<siga:Idioma key="facturacion.pagosFactura.boton.PagoCaja"/>" onclick="return pagoPorCaja();" class="button">
					<%} else { %>
						&nbsp;
					<%}%>
				</td>
				<td class="tdBotones">
					<%if(mostrarBotonRenegociar) {%>
						<input type="button" name="renegociar" id="idButton" value="<siga:Idioma key="facturacion.pagosFactura.boton.Renegociar"/>" onclick="return botonRenegociar();" class="button">
					<%} else { %>
						&nbsp;
					<%}%>
				</td>
			</tr>
		</table>
	</div>			
	<%}%>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='<%=botonesAccion%>' />

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
				
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>