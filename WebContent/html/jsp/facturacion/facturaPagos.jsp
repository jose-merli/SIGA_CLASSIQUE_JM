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

<!-- JSP -->
<% 
ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
String path = actionMapping.getPath();		
	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
String volver = request.getAttribute("volver")==null?"NO":(String)request.getAttribute("volver");
		String botonesAccion = "";

		// Gestion de Volver
		String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");

		if (busquedaVolver.equals("DEV_MANUAL")) {
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
		if ((modoRegistroBusqueda != null) && (!modoRegistroBusqueda.equalsIgnoreCase("ver"))) {
		
			if (estadoFactura != null) {
				if ((estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)  
						|| ((formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_METALICO)
								&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_ANULADA))
								&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_PAGADA))
								&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA)))
				    ) && !user.isLetrado()){
					mostrarBotonPago = true;
				}
			
				if (((estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA))  
						|| (formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_TARJETA || formaPagoFactura==ClsConstants.TIPO_FORMAPAGO_METALICO
						&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_ANULADA))
						&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_PAGADA))
						&& !(estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA))
				     ))&& !user.isLetrado()){
					mostrarBotonTarjeta = true;
				}

				if ((estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA))
				     && !user.isLetrado() || (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO))){
				    mostrarBotonRenegociar = true;
				}
				if ((estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_DEVUELTA) 
					    ) && !user.isLetrado()){
					mostrarBotonRenegociar = true;
					}				
				
			}
		}
		
		// MAV 7/7/05 a instancias de JG
		if ((modoRegistroBusqueda != null) && (modoRegistroBusqueda.equalsIgnoreCase("ver"))) {
		
			if (estadoFactura != null) {
				if ((estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) && user.isLetrado()){
					mostrarBotonTarjeta = true;
				}
			}
		}
		
	 	Double pendiente = new Double(total.doubleValue());
%>


<%@page import="org.apache.struts.action.ActionMapping"%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		function refrescarLocal(){ 		
			document.location.reload();
		}

		//Asociada al boton Pago por caja -->
		function pagoPorCaja() {
			sub();
			document.GestionarFacturaForm.action = "<%=app%>"+"/FAC_PagosFactura.do";	 
			document.GestionarFacturaForm.modo.value = "pagoPorCaja";
			rc = ventaModalGeneral(document.GestionarFacturaForm.name, "P");
			fin();
			if (rc == "MODIFICADO") refrescarLocal();
		}	

		//Asociada al boton Pago por tarjeta -->
		function pagoPorTarjeta(){
			document.forms[0].modo.value = "abrir";
			document.forms[0].action = "<%=app%>"+"/FAC_PagosFacturaPorTarjeta.do";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}	

		//Asociada al boton Renegociar -->
		function botonRenegociar(){ 
			sub();
			document.GestionarFacturaForm.action = "<%=app%>"+"/FAC_PagosFactura.do";
			document.GestionarFacturaForm.modo.value = "pagoRenegociar";
			rc = ventaModalGeneral(document.GestionarFacturaForm.name, "M");
			fin();
			if (rc == "MODIFICADO") refrescarLocal();
		}	
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
		 	<% 	if (usr.getStrutsTrans().equals("FAC_BusquedaFactura")) {%>
					<siga:Titulo titulo="facturacion.facturas.pagos.cabecera"	localizacion="facturacion.facturas.localizacion"/>
			<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
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

			   columnSizes = "8,15,15,27,8,8,9"
			   fixedHeight="320"
			   modal = "M"
			   modalScroll="true">
	
			<%	 
				if ((vPagos != null) && (vPagos.size() > 0)){
			
					for (int i = 1; i <= vPagos.size(); i++) { 
					
					 Hashtable pago = (Hashtable) vPagos.get(i-1);
					 if (pago != null){ 
					 
							String tabla = UtilidadesHash.getString(pago, "TABLA").trim();
							String estado = UtilidadesHash.getString(pago, "ESTADO");
							String fecha = UtilidadesHash.getString(pago, "FECHA");
							Double importe = UtilidadesHash.getDouble(pago, "IMPORTE");
							String tarjeta = UtilidadesHash.getString(pago, "TARJETA");
							String numAbono = UtilidadesHash.getString(pago, "NUMEROABONO");
							Integer idAbonoCuenta = UtilidadesHash.getInteger(pago, "IDABONO_IDCUENTA");
							Integer idPago = UtilidadesHash.getInteger(pago, "IDPAGO");
							String nombreBanco=UtilidadesHash.getString(pago, "NOMBREBANCO");
	
						 	String medioPago = "";
						 	Double devolucion = new Double(0);
	
							if (tabla.equals("EMISIÓN FACTURA")) { 
								pendiente	= new Double(total.doubleValue());
								importe = new Double(0);
								if (pendiente.doubleValue() < 0.0) {
									pendiente = new Double(0.0);
								}
							}
							else {
								if ((tabla.length() > 10) && ("DEVOLUCIÓN".equals(tabla.substring(0,10)) || "RENEGOCIACIÓN".equals(tabla.substring(0,13)))) {
									pendiente = new Double (importe.doubleValue());
									importe = new Double(0);
									if (pendiente.doubleValue() < 0.0) {
										pendiente = new Double(0.0);
									}
								} else {
									pendiente = new Double (pendiente.doubleValue() - importe.doubleValue() + devolucion.doubleValue());
									if (pendiente.doubleValue() < 0.0) {
										pendiente = new Double(0.0);
									}									
								}
							}
							
							Double auxPendiente = new Double (UtilidadesNumero.redondea(pendiente.doubleValue(),2));
							
							String botones="";
							if ((idPago != null) && (idPago.intValue() > 0)){
								botones = "C";
							}

						%>


							<siga:FilaConIconos fila='<%=""+i%>' botones="<%=botones%>" visibleEdicion="false" visibleBorrado="false" clase="listaNonEdit"  >
							<td><input type="hidden" id="oculto<%=i%>_1" value="<%=idPago%>">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fecha))%></td>
							<td><%=UtilidadesString.mostrarDatoJSP(tabla)%></td>
							<td><%=UtilidadesString.mostrarDatoJSP(estado)%></td>
							<td><%=UtilidadesString.mostrarDatoJSP(nombreBanco)%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importe.doubleValue()))%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(auxPendiente.doubleValue()))%></td>
							</siga:FilaConIconos>
							 		
		<%	 		 } // if
			 	 }  // for  %>
				<% } // if  %>
			</siga:Table> 
	
	<div id="totales" style="bottom:70px; left:200px; position:absolute; width:295px">
			<fieldset>
			<table  align="left" border="0" width="295">
				<tr>				
					<td width="150"  class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalFactura"/></td>
					<td width="145" class="labelTextNum" align="left"><%=UtilidadesString.formatoImporte(total)%> &euro;</td>
				</tr>
			</table>
			</fieldset>
	</div>
	
	<div id="totales2" style="bottom:70px; left:580px; position:absolute; width:295px;">
			<fieldset>

			<table  align="left" border="0" width="270px">

				<tr>				
					<td width="150px" class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalAnticipado"/></td>
					<td width="145px" class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(totalAnticipado.toString(),2)))%> &euro;</td>
				</tr>

				<tr>				
					<td width="150px" class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalCaja"/></td>
					<td width="145px" class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(totalCaja.toString(),2)))%> &euro;</td>
				</tr>

				<tr>				
					<td width="150px" class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalTarjeta"/></td>
					<td width="145px" class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(totalTarjeta.toString(),2)))%> &euro;</td>
				</tr>
					
				<tr>				
					<td width="150px" class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalBanco"/></td>
					<td width="145px" class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(totalBanco.toString(),2)))%> &euro;</td>
				</tr>
					
				<tr>				
					<td width="150px" class="labelText"><siga:Idioma key="facturacion.pagosFactura.literal.TotalCompensado"/></td>
					<td width="180px" class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(totalCompensado.toString(),2)))%> &euro;</td>
				</tr>
			</table>
			</fieldset>
			
	</div>
	
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
						<%if(mostrarBotonTarjeta) {%>
							<input type="button" name="pagoTarjeta" id="idButton" value="<siga:Idioma key="facturacion.pagosFactura.boton.PagoTarjeta"/>" onclick="return pagoPorTarjeta();" class="button">
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


	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='<%=botonesAccion%>' />

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
	
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
				
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>