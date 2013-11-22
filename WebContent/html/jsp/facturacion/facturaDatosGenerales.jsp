<!DOCTYPE html>
<html>
<head>
<!-- facturaDatosGenerales.jsp -->

<!-- Modificado por miguel.villegas  12/04/2005  Inserción botón imprimir -->

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
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.beans.FacAbonoBean"%>
<%@ page import="org.apache.struts.action.ActionMapping"%>

<!-- JSP -->
<% 		
	ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
	String path = actionMapping.getPath();
	String volver = (String)request.getAttribute("volver");
		
	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String facturaPDF = "factura.pdf";
	
	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver.equals("DEV_MANUAL")) {
		volver="SI";
	}

	String modoRegistroBusqueda = (String)request.getAttribute("modoRegistroBusqueda");
	String botones = "";
	String claseEditarCampos = "box";
	
	if (volver!=null && volver.trim().equalsIgnoreCase("SI")){
		if (modoRegistroBusqueda!=null && modoRegistroBusqueda.equalsIgnoreCase("ver")) {
			botones = "V";
			claseEditarCampos = "boxConsulta";			
		} else {				
			botones = "V,G,R";
		}
		
	} else {
		if (modoRegistroBusqueda!=null && modoRegistroBusqueda.equalsIgnoreCase("ver")) {
			botones = "";
			claseEditarCampos = "boxConsulta";			
		} else {
			botones = "G,R";
		}			
	}	
	
	String numFactura = "";
	String fecha = "";
	String estado = "";
	String numColegiado = "";
	String nifcif = "";
	String nifcifDeudor = "";
	String nombreColegiado = "";
	String nombreDeudor = "";
	String serie = "";
	String fechaGeneracion = "";
	String fechaConfirmacion = "";
	String formaPago = "facturacion.buscarFactura.literal.Banco";
	String cuenta = "";
	String codEntidad = "";
	String observaciones = "";
	String observinforme = "";
	Double pagosAnticipado = null;
	Double pagosCompensado = null;
	Double pagosPorCaja = null;
	Double pagosSoloCaja = null;
	Double pagosSoloTarjeta = null;
	Double pagosPorBanco = null;
	Double totalFactura = null;
	Double totalPagos = null;
	Double pedientePagar = null;		
	Double totalesImporteNeto = null;
	Double totalesImporteIVA = null;
	Double totalesTotalFactura = null;
	double pagosPorCajaFinal = 0;
	
	Hashtable factura = (Hashtable) request.getSession().getAttribute("DATABACKUP");
	String abonoAnulacion = null;
	if (factura != null) {
		abonoAnulacion = UtilidadesHash.getString(factura, FacAbonoBean.C_NUMEROABONO);
		numFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_NUMEROFACTURA);
		fecha = UtilidadesHash.getString(factura, FacFacturaBean.C_FECHAEMISION);
		estado = UtilidadesHash.getString(factura, "DESCRIPCION_ESTADO");
		numColegiado = UtilidadesHash.getString(factura, CenColegiadoBean.C_NCOLEGIADO);
		nifcif = UtilidadesHash.getString(factura, CenPersonaBean.C_NIFCIF);
		nifcifDeudor = UtilidadesHash.getString(factura, "NIFCIFDEUDOR");
		nombreColegiado = UtilidadesHash.getString(factura, CenPersonaBean.C_NOMBRE) + " " +
						 UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS1) + " " +
						 UtilidadesHash.getString(factura, CenPersonaBean.C_APELLIDOS2);
		serie = UtilidadesHash.getString(factura, FacSerieFacturacionBean.C_DESCRIPCION);
		fechaGeneracion = UtilidadesHash.getString(factura, FacFacturacionProgramadaBean.C_FECHAREALGENERACION);
		fechaConfirmacion = UtilidadesHash.getString(factura, FacFacturacionProgramadaBean.C_FECHACONFIRMACION);
		Integer idCuenta = UtilidadesHash.getInteger(factura, FacFacturaBean.C_IDCUENTA);
		Integer idCuentaDeudor = UtilidadesHash.getInteger(factura, FacFacturaBean.C_IDCUENTADEUDOR);
		nombreDeudor = UtilidadesHash.getString(factura, "PERSONADEUDOR");
		
		/*if (idCuenta != null) {
			formaPago = "facturacion.buscarFactura.literal.Banco";
		}
		else 
			formaPago = "facturacion.buscarFactura.literal.Caja";*/
			
	 	if (nifcifDeudor!=null && !nifcifDeudor.equals("")) {
			if (idCuentaDeudor != null && !idCuentaDeudor.equals("")) {//si existe idCuentaDeudor siempre se factura por domiciliacion bancaria		   
				formaPago = "facturacion.buscarFactura.literal.Banco";
		   	} else {// se le factura al cliente mediante pago en metalico		  
			 	formaPago = "facturacion.buscarFactura.literal.Caja";
		   	}	
			
	 	} else {   
		   	if (idCuenta!=null && !idCuenta.equals("")){		     
		     	formaPago = "facturacion.buscarFactura.literal.Banco";
		   	} else {		   
		     formaPago = "facturacion.buscarFactura.literal.Caja";
		   	}
	 	}
               
		/*if (idCuentaDeudor != null) {//si existe idCuentaDeudor siempre se factura por domiciliacion bancaria
			formaPago = "facturacion.buscarFactura.literal.Banco";
		}
		else// se le factura al cliente mediante pago en metalico
			formaPago = "facturacion.buscarFactura.literal.Caja";	*/
			
		cuenta = UtilidadesHash.getString(factura, CenCuentasBancariasBean.C_NUMEROCUENTA);
		codEntidad= UtilidadesHash.getString(factura, CenCuentasBancariasBean.C_CBO_CODIGO);
		
		if(idCuentaDeudor!=null){
		 cuenta = UtilidadesHash.getString(factura, "NUMEROCUENTADEUDOR");
		 codEntidad= UtilidadesHash.getString(factura, "CODIGOENTIDADDEUDOR");		
		}
		
		observaciones = UtilidadesHash.getString(factura, FacFacturaBean.C_OBSERVACIONES);
		observinforme = UtilidadesHash.getString(factura, FacFacturaBean.C_OBSERVINFORME);
		
		pagosAnticipado = UtilidadesHash.getDouble(factura, "TOTAL_ANTICIPADO");
		pagosCompensado = UtilidadesHash.getDouble(factura, "TOTAL_COMPENSADO");
		pagosPorCaja = UtilidadesHash.getDouble(factura, "TOTAL_PAGADOPORCAJA"); 
		pagosSoloCaja = UtilidadesHash.getDouble(factura, "TOTAL_PAGADOSOLOCAJA"); 
		pagosSoloTarjeta = UtilidadesHash.getDouble(factura, "TOTAL_PAGADOSOLOTARJETA"); 
		pagosPorBanco = UtilidadesHash.getDouble(factura, "TOTAL_PAGADOPORBANCO");
		totalFactura = UtilidadesHash.getDouble(factura, "TOTAL_FACTURA");
		totalPagos = UtilidadesHash.getDouble(factura, "TOTAL_PAGADO");
		pedientePagar = UtilidadesHash.getDouble(factura, "TOTAL_PENDIENTEPAGAR");
		// RGG
		if (pedientePagar.doubleValue()<0) pedientePagar = new Double(0);
		totalesImporteNeto = UtilidadesHash.getDouble(factura, "TOTAL_NETO");
		totalesImporteIVA = UtilidadesHash.getDouble(factura, "TOTAL_IVA");
		totalesTotalFactura = totalFactura;

		// RGG no entiendo por que se hace esto.
		// RGG 11/01/2007 Se hace porque los compensados estan apuntados como por cja, de modo que en el desglose se restan
		// RGG 11/01/2007 Dinalmente se quita porque la funcion totales solo caja ya lo resta
		//if ((pagosPorCaja != null) && (pagosCompensado != null)) {
		//	pagosPorCajaFinal = pagosSoloCaja.doubleValue() - pagosCompensado.doubleValue();
		//}
	}

	// RGG 11/01/2007 cambio para ver si se muestra mensaje de que se ha realizado abono
	boolean mostrarMensajeAnticipado = false;
	// RGG 11/01/2007 Como cuando se confirma se actualizan los importes anticipados
	// nunca se va  dar la condicion de mostrar el mensaje. Lo comento
	//if (totalPagos.doubleValue() > totalFactura.doubleValue()) {
		// se ha anticipado de más
	//	if (!fechaConfirmacion.trim().equals("")) {
			// esta confirmada
	//		mostrarMensajeAnticipado = true;
	//	}
	//}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		// Asociada al boton Reset
		function accionRestablecer(){ 
			GestionarFacturaForm.reset();
		}	
	
		// Asociada al boton Guardar
		function accionGuardar() {
			sub();			
			GestionarFacturaForm.modo.value = "modificar";
			GestionarFacturaForm.submit();
		}		
		
		// Asociada al boton Guardar
		function accionImprimir() {
			alert ("Codificar");		
			var alto = 500, ancho = 800;					  
	 		var posX = (screen.width - ancho) / 2;
     		var posY = (screen.height - alto) / 2;
     		var medidasWin = "height=" + alto + ", width=" + ancho + ", top=" + posY + ", left=" + posX; 			
			w = window.open ("html/pdf/<%=facturaPDF%>", "", "status=0, toolbar=0, location=0, menubar=0, resizable=1," + medidasWin);
		}		
		
		function abrirPDF () {
			GestionarFacturaForm.modo.value = "imprimirFactura";
			GestionarFacturaForm.submit();
		}
		
		function mensaje ()	{
			<% if (mostrarMensajeAnticipado) { %>
				var men='<siga:Idioma key="messages.fact.mayorAnticipado"/>';
				alert(men);
			<% } %>
		}
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
 	<% 	if (usr.getStrutsTrans().equals("FAC_BusquedaFactura")) {%>
			<siga:Titulo titulo="facturacion.facturas.datosGenerales.cabecera"	localizacion="facturacion.facturas.localizacion"/>
	<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
			<siga:Titulo titulo="facturacion.facturas.datosGenerales.cabecera"	localizacion="censo.facturacion.facturas.localizacion"/>
	<% }%>		
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos" onload="mensaje();">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->	
	<html:form action="<%=path%>" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		
		<table class="tablaCampos" border="0">
			<% if (abonoAnulacion!=null && !abonoAnulacion.equals("null")) { %>
				<tr>
					<td class="titulitosDatos">
						<siga:Idioma key="facturacion.literal.avisoAbonoAnulacion"/>&nbsp;<%=abonoAnulacion%>
					</td>
				</tr>
			<% } %>

			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.datosFactura.leyenda">	
						<table class="tablaCampos" border="0">														
							<tr>
								<td  width="15%" class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.NumeroFactura"/></td>
								<td  width="15%" class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(numFactura)%></td>

								<td  width="15%"  class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Fecha"/></td>
								<td  width="15%" class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fecha))%></td>

								<td  width="15%" class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Estado"/></td>
								<td  width="15%" class="labelTextValor"><siga:Idioma key="<%=estado%>"/></td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Cliente"/></td>
								<td class="labelTextValor" ><%=UtilidadesString.mostrarDatoJSP(nifcif)%> - <%=UtilidadesString.mostrarDatoJSP(nombreColegiado)%></td>
								
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Deudor"/></td>
								<td class="labelTextValor" colspan="3"><%=UtilidadesString.mostrarDatoJSP(nifcifDeudor)%> - <%=UtilidadesString.mostrarDatoJSP(nombreDeudor)%></td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.SerieFacturacion"/></td>
								<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(serie)%></td>

								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.FechaGeneracion"/></td>
								<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaGeneracion))%></td>

								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.FechaConfirmacion"/></td>
								<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaConfirmacion))%></td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.FormaPago"/></td>
								<td class="labelTextValor"><siga:Idioma key="<%=formaPago%>"/></td>

								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.NumeroCuenta"/></td>
								<td class="labelTextValor" colspan="3"><%=codEntidad%><%=UtilidadesString.mostrarNumeroCuentaConAsteriscos(cuenta)%></td>
							</tr>
							
							<tr>
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Observaciones"/></td>
								<td colspan="2">
									<html:textarea property="datosGeneralesObservaciones" 
										onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" 
										style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"
										styleClass="<%=claseEditarCampos%>" value="<%=observaciones%>"></html:textarea>
								</td>
								
								<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.Observinforme"/></td>
								<td colspan="2">
									<html:textarea property="datosGeneralesObservinforme" 
										onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" 
										style="overflow-y:auto; overflow-x:hidden; width:350px; height:45px; resize:none;"
										styleClass="<%=claseEditarCampos%>" value="<%=observinforme%>"></html:textarea>
								</td>
							</tr>							
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
							
			<tr width="100%">
				<td>
					<html:button property="idButton" onclick="abrirPDF();" styleClass="button"><siga:Idioma key="facturacion.datosFactura.boton.DescargarFacturaPDF"/> </html:button>
				</td>
			</tr>
			
			<tr>		
				<td>
					<table border="0" width="100%">
						<tr>
							<td width="33%">
								<table>
									<tr>
										<td> <!-- Pagos -->
											<siga:ConjCampos leyenda="facturacion.datosFactura.literal.Pagos">	
												<table class="tablaCampos" border="0">	
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PagosAnticipado"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(pagosAnticipado.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
						
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PagosPorCaja"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesNumero.formatoCampo(pagosSoloCaja.doubleValue())%>&nbsp;&euro;</td>
													</tr>
													
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PagosPorTarjeta"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesNumero.formatoCampo(pagosSoloTarjeta.doubleValue())%>&nbsp;&euro;</td>
													</tr>
						
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PagosPorBanco"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(pagosPorBanco.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
													
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PagosCompensado"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(pagosCompensado.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
												</table>
											</siga:ConjCampos>
										</td>
									</tr>
								</table>
							</td>
							
							<td width="33%">
								<table>
									<tr>
										<td align="center">
											<table class="tablaCampos" border="0">	
												<tr>
													<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.TotalFactura"/></td>
													<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalFactura.doubleValue()))%>&nbsp;&euro;</td>
												</tr>
						
												<tr>
													<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.TotalPagos"/></td>
													<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalPagos.doubleValue()))%>&nbsp;&euro;</td>
												</tr>
						
												<tr>
													<td class="labelTextValor" colspan="2"><hr size="1"></td>
												</tr>
							
												<tr>
													<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.PedientePagar"/></td>
													<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(pedientePagar.doubleValue()))%>&nbsp;&euro;</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
					
							<td width="33%">
								<table>
									<tr>
										<td align="right"> <!-- Totales -->
											<siga:ConjCampos leyenda="facturacion.datosFactura.literal.Totales">	
												<table class="tablaCampos" border="0">	
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.TotalesImporteNeto"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalesImporteNeto.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
						
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.TotalesImporteIVA"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalesImporteIVA.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
						
													<tr>
														<td class="labelTextValor" colspan="2"><hr size="1"></td>
													</tr>
							
													<tr>
														<td class="labelText"><siga:Idioma key="facturacion.datosFactura.literal.TotalesTotalFactura"/></td>
														<td class="labelTextNum" align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalesTotalFactura.doubleValue()))%>&nbsp;&euro;</td>
													</tr>
												</table>
											</siga:ConjCampos>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>					
				</td>
			</tr>
			
		</html:form>
	</table>
	<!-- FIN: CAMPOS -->
	
	<siga:ConjBotonesAccion botones='<%=botones%>' modo=''/>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>