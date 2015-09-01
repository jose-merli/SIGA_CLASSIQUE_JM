<!DOCTYPE html>
<html>
<head>
<!-- datosSolicitud.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@taglib uri="struts-bean.tld" prefix="bean" %>
<%@taglib uri="struts-html.tld" prefix="html" %>
<%@taglib uri="libreria_SIGA.tld" prefix="siga" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import="com.siga.productos.form.GestionSolicitudesForm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%> 
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<%   	 
	Vector peticion = (Vector) request.getAttribute("peticion");
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	int iCantidadTotal=0;
	double dNetoTotal=0, dIvaTotal=0, dPrecioTotal=0;
	String tipoPeticion = "";
	
	String nombreUsu = (String) request.getAttribute("nombreUsuario");
	String numeroColegiado = (String) request.getAttribute("nColegiado");
	String idPersona = String.valueOf((Long) request.getAttribute("idPersona"));
	String tipoSolicitud = (String) request.getAttribute("tipoSolicitud");
	String sidPeticion = String.valueOf((Long) request.getAttribute("idPeticion"));
	String sIdInstitucion = usrbean.getLocation();
	String siCierroVentanaRefresco = "";
	String fechaEfectiva="";
	boolean letrado=usrbean.isLetrado();

	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarCerrar"));	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	// JPT (14-08-2015): Necesario para que al volver recargue los datos de las fechas
	GestionSolicitudesForm formGestionSolicitudes = (GestionSolicitudesForm) session.getAttribute("GestionSolicitudesForm");
	String fechaDesde="", fechaHasta="";
	if (formGestionSolicitudes != null) {
		fechaDesde = formGestionSolicitudes.getBuscarFechaDesde();
		if (fechaDesde != null) {
			fechaDesde = GstDate.getFormatedDateShort("", fechaDesde);
		}
		
		fechaHasta = formGestionSolicitudes.getBuscarFechaHasta();
		if (fechaHasta!=null) {
			fechaHasta = GstDate.getFormatedDateShort("", fechaHasta);
		}
	}
%>

	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>
	<div id="divImporteAnticipado" title="<siga:Idioma key='pys.solicitudCompra.literal.importeAnticipado'/>" style="display:none">
		<table align="left">
			<tr>
				<td class="labelText" nowrap>
					<siga:Idioma key="pys.mantenimientoServicios.literal.nuevoImporteAnticipado"/>&nbsp;(*)
				</td>
				<td class="labelText">		
					<input type="text" id="textImporteAnticipado" name="textImporteAnticipado" class="box" style="background-color:transparent; color:black; text-align:right" value=""/>&nbsp;&euro;
				</td>
			</tr>		
		
			<tr>
				<td class="labelText" nowrap>
					<siga:Idioma key="pys.mantenimientoServicios.literal.precioSolicitud"/>
				</td>
				<td class="labelText" align="right">
					<input type="text" id="textPrecioSolicitud" name="textPrecioSolicitud" class="boxConsulta" style="background-color:transparent; font-weight:bold; color:black; text-align:right" value="" readOnly="true"/>&nbsp;&euro;	
				</td>
			</tr>						
		</table>			
	</div>	
	
	<div id="divConfirmarSolicitud" title="<siga:Idioma key='pys.mantenimientoServicios.literal.fechaEfectiva'/>" style="display:none">
		<table align="left">
			<tr>
				<td class="labelText" nowrap>
					<siga:Idioma key="pys.mantenimientoServicios.literal.fecha"/>&nbsp;(*)
				</td>
				<td><siga:Fecha nombreCampo="fechaEfectivaDiv" valorInicial="<%=fecha%>" anchoTextField="10"/></td>
			</tr>		
			</tr>						
		</table>			
	</div>		

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
<% 
				if (!tipoSolicitud.equals("BAJA")) {  
%>  
					<siga:Idioma key="pys.gestionSolicitudes.titulo2"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>					
<% 
				} else { 
%>
					<siga:Idioma key="pys.gestionSolicitudes.titulo3"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>					
<% 
				} 
%>
				&nbsp;-&nbsp;
<%				
				if (numeroColegiado != null && !numeroColegiado.equalsIgnoreCase("")) { 
%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroColegiado)%>
<% 
				} else { 
%>
					<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<% 
				} 
%>
				&nbsp;-&nbsp;<siga:Idioma key="pys.gestionSolicitudes.literal.idPeticion"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(sidPeticion)%>
			</td> 
		</tr>
	</table>

	<html:form action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">		
		<input type="hidden" name="idSolicitud" value="<%=sidPeticion%>"> 
		<input type="hidden" name="concepto" value="">
		<input type="hidden" name="idProducto"  id="idProducto" value="">
		<input type="hidden" name="idProductoInstitucion"  id="idProductoInstitucion" value="">
		<input type="hidden" name="idTipoProducto"  id="idTipoProducto" value=""> 
		<html:hidden property="modo" value = ""/>
		<html:hidden property="importeAnticipado" value=""/>
		<html:hidden property="idCuenta" value=""/>
		<html:hidden property="idPersona" value="<%=idPersona%>"/>
		<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
		<input type="hidden" name="tipoSolicitud" value="<%=tipoSolicitud%>"/>
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="fechaEfectiva" value="<%=fechaEfectiva%>">
		
		<html:hidden name="GestionSolicitudesForm" property="buscarTipoPeticion"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarIdPeticionCompra"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarEstadoPeticion"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarFechaDesde" value="<%=fechaDesde%>"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarFechaHasta" value="<%=fechaHasta%>"/>
		<html:hidden name="GestionSolicitudesForm" property="facturada"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarNColegiado"/>		
		<html:hidden name="GestionSolicitudesForm" property="buscarNifcif"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarNombre"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarApellido1"/>
		<html:hidden name="GestionSolicitudesForm" property="buscarApellido2"/>
	</html:form>	
	
<%
	String sColumnNames = "pys.gestionSolicitudes.literal.concepto," +
		"pys.gestionSolicitudes.literal.formaPago," +
		"pys.gestionSolicitudes.literal.nCuenta," +
		"pys.gestionSolicitudes.literal.cantidad," +
		"pys.gestionSolicitudes.literal.precio," +
		"pys.gestionSolicitudes.literal.iva," +
		"pys.gestionSolicitudes.literal.estadoPago," +
		"pys.gestionSolicitudes.literal.estadoProducto,";

 	if (tipoSolicitud.equals("BAJA")) {  
 		sColumnNames += "pys.solicitudCompra.literal.solicitudAlta,";		
 	} else {
 		sColumnNames += "pys.solicitudCompra.literal.solicitudBaja,";
 	}
		
	sColumnNames += "pys.solicitarBaja.literal.fechaEfectiva," +
			"pys.gestionSolicitudes.literal.importeAnticipado,";		
%>	
	<siga:Table 
		name="tablaResultados"
		border="2"
		columnNames="<%=sColumnNames%>"
		columnSizes="13,9,17,5,9,6,7,7,6,7,7,7">
<% 
		if (peticion != null && peticion.size() > 0) { 
			for (int i = 0; i < peticion.size(); i++) { 
				FilaExtElement[] elementos = new FilaExtElement[2];
				Hashtable productoServicio = (Hashtable) peticion.get(i);

				//Fecha Efectiva:
				fechaEfectiva = (String) productoServicio.get("FECHAEFEC");
				if (fechaEfectiva==null || fechaEfectiva.trim().equals("")) { 
					fechaEfectiva = "&nbsp;";
				} else {
					fechaEfectiva = GstDate.getFormatedDateShort(usrbean.getLanguage(),fechaEfectiva);
				}
									
				if (productoServicio != null){ 
					Long idPeticion, idArticulo, idArticuloInstitucion;
					Integer idInstitucion, idTipoArticulo, idCuenta, idTipoEnvios;
					String estado;
					int cantidad;
					double precio, importeUnitario;
					double iva = 0;
				
					String estadoPago = UtilidadesString.getMensajeIdioma(usrbean,UtilidadesHash.getString(productoServicio, "ESTADOPAGO"));
					String formaPago = UtilidadesHash.getString(productoServicio, "FORMAPAGO");
					Integer idFormaPago = UtilidadesHash.getInteger(productoServicio, PysProductosSolicitadosBean.C_IDFORMAPAGO);									
					String nCuenta = UtilidadesHash.getString(productoServicio, "NCUENTA");
					String concepto = UtilidadesHash.getString(productoServicio, "CONCEPTO");
					String tipoTablaPetion 	= UtilidadesHash.getString(productoServicio, "CONSULTA");
					String peticionRelacionada 	= UtilidadesHash.getString(productoServicio, "IDPETICIONRELACIONADA");
					String productoOServicio = "";
					String aceptado = "";
					String periodicidad = "";
					String existePrecio = "";
					idTipoEnvios = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_IDTIPOENVIOS);
					int indiceBoton = 0;
					//El importeAnticipado será < 0 si no se puede anticipar, en caso contrario contendrá el valor del importe anticipado actual.
					//esta variable se usa tanto para decidir si mostrar o no el botón de "anticipar" como el valor actual del anticipo.
					int anticipar = -1;
					double importeAnticipado = -1;
				
					// Producto
					if (tipoTablaPetion.trim().equalsIgnoreCase("PRODUCTO")) {					  
						productoOServicio = "producto";
						
						if (idTipoEnvios == null) { // No es certificado
							if (fechaEfectiva==null || fechaEfectiva.equals("&nbsp;") || fechaEfectiva.trim().equals("")) { // No tiene fecha efectiva
								elementos[0]=new FilaExtElement("confirmarSolicitudCompra", "confirmarSolicitudCompra", SIGAConstants.ACCESS_FULL);
								elementos[1]=new FilaExtElement("denegarSolicitudCompra", "denegarSolicitudCompra", SIGAConstants.ACCESS_FULL);
							}
							
						} else { //Certificados
						   	if (!idTipoEnvios.equals("")) {						   
						    	elementos=new FilaExtElement[1];
						    	elementos[0]=new FilaExtElement("consultarcert", "consultarcert", SIGAConstants.ACCESS_FULL);
						   	}
						}
				
						idPeticion = UtilidadesHash.getLong (productoServicio, PysProductosSolicitadosBean.C_IDPETICION);
						idInstitucion = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_IDINSTITUCION);
						idTipoArticulo = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
						idArticulo = UtilidadesHash.getLong (productoServicio, PysProductosSolicitadosBean.C_IDPRODUCTO);
						idArticuloInstitucion = UtilidadesHash.getLong (productoServicio, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
						aceptado = UtilidadesHash.getString(productoServicio, PysServiciosSolicitadosBean.C_ACEPTADO);
						estado = UtilidadesProductosServicios.getEstadoProductoServicio(aceptado);
						String aux = UtilidadesHash.getString(productoServicio, "ANTICIPAR");						
						
						if (aux.indexOf("#") != -1){
							String[] arrayAux = aux.split("#");
							anticipar = Integer.parseInt(arrayAux[0]);
							if (arrayAux.length>1) {
								importeAnticipado = Double.parseDouble(aux.split("#")[1]);
							}
							
						} else {
							anticipar = Integer.valueOf(aux).intValue();
						}
				
						idCuenta = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_IDCUENTA);
						cantidad = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
						if (UtilidadesHash.getDouble (productoServicio, PysProductosSolicitadosBean.C_VALOR)!=null){
							precio = UtilidadesHash.getDouble(productoServicio, PysProductosSolicitadosBean.C_VALOR).doubleValue();
							iva = UtilidadesHash.getFloat(productoServicio, "VALORIVA").doubleValue();
							existePrecio="1";
							
						} else {
							precio = 0;
							iva = 0;
							existePrecio="0";
						}
					
					// Servicio
					} else {
						if (fechaEfectiva==null || fechaEfectiva.equals("&nbsp;") || fechaEfectiva.trim().equals("")) { // No tiene fecha efectiva 
							elementos[0]=new FilaExtElement("confirmarSolicitudCompra", "confirmarSolicitudCompra", SIGAConstants.ACCESS_FULL);
							elementos[1]=new FilaExtElement("denegarSolicitudCompra", "denegarSolicitudCompra", SIGAConstants.ACCESS_FULL);
					  	}	
				
						productoOServicio = "servicio";
						idPeticion = UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDPETICION);
						idInstitucion = UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_IDINSTITUCION);
						idTipoArticulo = UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
						idArticulo = UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDSERVICIO);
						idArticuloInstitucion = UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
						aceptado = UtilidadesHash.getString(productoServicio, PysServiciosSolicitadosBean.C_ACEPTADO);										
						estado = UtilidadesProductosServicios.getEstadoProductoServicio(aceptado);
						String aux = UtilidadesHash.getString(productoServicio, "ANTICIPAR");
						if (aux.indexOf("#") != -1){
							anticipar = Integer.parseInt(aux.split("#")[0]);
							importeAnticipado = Double.parseDouble(aux.split("#")[1]);
							
						} else {
							anticipar = Integer.valueOf(aux).intValue();
						}
						idCuenta = UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_IDCUENTA);
						cantidad = UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_CANTIDAD).intValue();
						if (UtilidadesHash.getDouble (productoServicio, "VALOR")!=null){
							precio = UtilidadesHash.getDouble (productoServicio, "VALOR").doubleValue();
							iva = UtilidadesHash.getFloat (productoServicio, "VALORIVA").doubleValue();
							periodicidad = " / " + UtilidadesHash.getString(productoServicio, "SERVICIO_DESCRIPCION_PERIODICIDAD");
							existePrecio="1";							
						} else {
							precio = 0;
							iva = 0;
							periodicidad = " - " ;
							existePrecio="0";
						}
					}
				
					// Calculamos el precio total y el total del iva
					importeUnitario = UtilidadesNumero.redondea(cantidad * precio * (1 + (iva / 100)), 2);
					
					if (idFormaPago!=null) {
						iCantidadTotal += cantidad;
						dNetoTotal += cantidad * precio;
						dIvaTotal += UtilidadesNumero.redondea(cantidad * precio * iva / 100, 2);
						dPrecioTotal += importeUnitario;
					}
				
					tipoPeticion = UtilidadesHash.getString(productoServicio, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
					
					if (tipoTablaPetion.trim().equalsIgnoreCase("PRODUCTO") && // Producto
							idTipoEnvios == null && // No es certificado
							idFormaPago!=null && // Tiene forma de pago 
							aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) && // Esta aceptado 
							(anticipar>0 || importeAnticipado > 0.0)) { // Se puede anticipar o ya tiene anticipo
						elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
					}									
	%>
					<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='' clase='listaNonEdit' elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>
						<td>
							<input type="hidden" id="oculto<%=i+1%>_1" value="<%=productoOServicio%>">
							<input type="hidden" id="oculto<%=i+1%>_2" value="<%=idPeticion%>">
							<input type="hidden" id="oculto<%=i+1%>_3" value="<%=idInstitucion%>">
							<input type="hidden" id="oculto<%=i+1%>_4" value="<%=idArticulo%>">
							<input type="hidden" id="oculto<%=i+1%>_5" value="<%=idArticuloInstitucion%>">
							<input type="hidden" id="oculto<%=i+1%>_6" value="<%=idTipoArticulo%>">
							<input type="hidden" id="oculto<%=i+1%>_7" value="<%=idFormaPago%>">
							<input type="hidden" id="oculto<%=i+1%>_8" value="<%=importeUnitario%>">
							<input type="hidden" id="oculto<%=i+1%>_9" value="<%=idCuenta%>">
							<input type="hidden" id="oculto<%=i+1%>_10" value="<%=existePrecio%>">
							<input type="hidden" id="oculto<%=i+1%>_11" value="<%=concepto%>">
							<input type="hidden" id="oculto<%=i+1%>_12" value="<%=fechaEfectiva%>">
							<input type="hidden" id="oculto<%=i+1%>_13" value="<%=importeAnticipado%>">
							<input type="hidden" id="oculto<%=i+1%>_14" value="<%=importeUnitario%>">
							<input type="hidden" id="oculto<%=i+1%>_15" value="<%=tipoSolicitud%>">
							<%=UtilidadesString.mostrarDatoJSP(concepto.replaceAll("\r\n", " ").replaceAll("\n\r", " "))%>
						</td>
			
						<td>										
<%
							if(idFormaPago==null) { 
%>
								<siga:Idioma key="pys.mantenimientoProductos.literal.noFacturable"/>
<%
							} else {
%>
								<%=UtilidadesString.mostrarDatoJSP(formaPago)%>
<%
							}
%>
						</td> 

						<td><%=UtilidadesString.mostrarIBANConAsteriscos(nCuenta)%></td>
			 
						<td align="right"><%=cantidad%></td> 
<% 
						if (UtilidadesHash.getDouble (productoServicio, "VALOR")!=null) { 
%>
							<td align="right">
								<%=UtilidadesString.formatoImporte(precio)%>&nbsp;&euro;
<%	
								if (!periodicidad.equals("")) { 
%>							 
									<%=UtilidadesString.mostrarDatoJSP(periodicidad)%>
<%
								}
%>					
							</td>
							
							<td align="right"><%=UtilidadesNumero.formatoCampo(iva)%> %</td> 
<% 
						} else { 
%>
							<td>-</td>
							<td>-</td> 
<% 
						} 
%>						
						<td><siga:Idioma key="<%=estadoPago%>"/></td>			
			
						<td>
<% 
							if(idFormaPago!=null) {  
%>									
								<siga:Idioma key="<%=estado%>"/>
<% 
							} else { 
%>
								<siga:Idioma key="pys.mantenimientoProductos.literal.noFacturable"/>
<% 
							} 
%>
						</td>
			
						<td><%=peticionRelacionada%></td>
			 
						<td><%=fechaEfectiva%></td> 
			
						<td align="right">
<%	
							if (importeAnticipado >= 0) { 
%>									
								<%=UtilidadesString.formatoImporte(importeAnticipado)%>&nbsp;&euro;															
<%	
							} else { 
%>
								&nbsp;
<%	
							} 
%>
						</td> 
<%
							if (elementos == null || elementos.length <= 0){ 
%>
								<td></td>
<%
							} else {
								boolean pintarCelda = true;
								int l = 0;
								while (pintarCelda && l < elementos.length){
									if (elementos[l] != null) {
										pintarCelda = false;
									}
									l++;
								}
								
								if (pintarCelda) {
%>
									<td></td>
<%
								}
							}
%>
					</siga:FilaConIconos>
<%	 	
				} // if
			}  // for
				
			if (tipoPeticion.equalsIgnoreCase(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {	
				dIvaTotal = UtilidadesNumero.redondea (dIvaTotal, 2);
				dPrecioTotal = UtilidadesNumero.redondea (dPrecioTotal, 2);		
%>
				<tr id="total1" class="listaNonEditSelected" style="height:30px">
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="3" align="center">
						<table border="0" cellpadding="5" cellspacing="0">
							<tr id="total2" class="listaNonEditSelected">
								<td class="labelText" style="color:black; text-align:center; border:0px; vertical-align:middle">
									<siga:Idioma key="facturacion.lineasFactura.literal.Total"/>																	
								</td>
								<td style="border:0px">
									<siga:ToolTip id='ayudaTotal' imagen='/SIGA/html/imagenes/botonAyuda.gif' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean,"messages.servicios.precioServicios"))%>' />
								</td>									
							</tr>
						
							<tr id="total3" class="listaNonEditSelected">
								<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.cantidad"/></td>
								<td class="labelText" style="border:0px">
									<input type="text" name="cantidadTotal" value="<%=iCantidadTotal%>" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
								</td>
							</tr>							
						
							<tr id="total4" class="listaNonEditSelected">
								<td class="labelText" style="color:black; text-align:right; border:0px" nowrap>
									<siga:Idioma key="pys.solicitudCompra.literal.totalImporteNeto"/>									
								</td>
								<td class="labelText" style="border:0px">
									<input type="text" name="netoTotal" value="<%=UtilidadesString.formatoImporte(dNetoTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
								</td>
							</tr>						
							
							<tr id="total5" class="listaNonEditSelected">
								<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.iva"/></td>
								<td class="labelText" style="border:0px">
									<input type="text" name="ivaTotal" value="<%=UtilidadesString.formatoImporte(dIvaTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
								</td>
							</tr>
							
							<tr id="total6" class="listaNonEditSelected" border="0">
								<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.totalImporte"/></td>
								<td class="labelText" style="border:0px">
									<input type="text" name="precioTotal" value="<%=UtilidadesString.formatoImporte(dPrecioTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
								</td>
							</tr>
						</table>
					</td>		
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>		
				</tr>							
<%					 	 
			}
		} else {	 	 
%>
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%	
		}  
%>
	</siga:Table>

	<siga:ConjBotonesAccion botones="V,I" modo=''  modal="G" clase="botonesDetalle"/>
	
	<!-- Este formulario se emplea para abrir la ventana modal de modificación de los anticipos-->
	<html:form styleId="MostrarAnticiposForm" action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">
	    <input type="hidden" name="idTipoClave" value="">
		<input type="hidden" name="idClave" value=""> 
		<input type="hidden" name="idClaveInstitucion" value=""> 
		<input type="hidden" name="idPeticion"> 
		<input type="hidden" name="idPersona" value="<%=idPersona%>"> 
		<input type="hidden" name="tipo" value=""> 
		<input type="hidden" name="totalAnticipado" value=""/>
		<input type="hidden" name="precioSolicitud" value=""/>
		<input type="hidden" name="nuevoImporteAnticipado" value=""/>	
		<input type="hidden" name="modo" value="guardarAnticipos"> 
		<input type="hidden" name="actionModal" value="">
	</html:form>	
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<script language="JavaScript">
		// Refrescar
		function refrescarLocal() { 		
			document.forms[0].modo.value = "Editar";
			document.forms[0].target = "mainWorkArea";
			document.forms[0].submit();
		}	
		
		// Asociada al boton Imprimir
		function accionImprimir() { 		
			var url = "/SIGA/PYS_GestionarSolicitudes.do?modo=editarImpresion&idInstitucion=<%=sIdInstitucion%>&idPersona=<%=idPersona%>&idPeticion=<%=sidPeticion%>&tipoSolicitud=<%=tipoSolicitud%>";			
			window.open(url, '_blank');
		}	
		
		function confirmarSolicitudCompra (fila) {
		  	var fecha = null;
		  	
 	 		jQuery("#divConfirmarSolicitud").dialog(
				{
					height: 175,
					width: 400,
					modal: true,
					resizable: false,
					buttons: {
						"<%=sDialogBotonGuardarCerrar%>": function() {
							if (jQuery("#fechaEfectivaDiv").val()=="") {
								var msg="<siga:Idioma key="messages.servicios.fechaEfectivaObligatoria"/>";
								alert(msg);
								return false;							
							}
							jQuery(this).dialog("close");
							fecha = jQuery("#fechaEfectivaDiv").val();
							postConfirmarSolicitudCompra (fila, fecha);
						},
						"<%=sDialogBotonCerrar%>": function() {
							jQuery(this).dialog("close");
						}
					}
				}
			);
			jQuery(".ui-widget-overlay").css("opacity","0");	 				
			jQuery("[id^=total]").addClass("listaNonEditSelected"); // Actualiza el color de los totales		  	
		}		
		
		function postConfirmarSolicitudCompra (fila, fecha) {
			sub();
	     	document.forms[0].fechaEfectiva.value = fecha;
		  	var existePrecioAsociado = jQuery("#oculto" + fila + "_10");
		  	if (existePrecioAsociado.val() == "0") { 
				var mensaje='<siga:Idioma key="messages.pys.gestionSolicitud.errorProductoSinPrecio"/>';
				alert(mensaje);
				fin();
				return(false);
				
		  	} else {
				return miSubmitFormConModo (fila, "confirmar");
			}
		}
	
		function denegarSolicitudCompra (fila) {
			sub();			
			if (confirm("<siga:Idioma key="messages.pys.solicitudCompra.confirmarDenegar"/>")) {
				var existePrecioAsociado = jQuery("#oculto" + fila + "_10");
		    	if (existePrecioAsociado.val() == "0") { 
					var mensaje='<siga:Idioma key="messages.pys.solicitudCompra.errorProductoSinPrecio"/>';
					alert(mensaje);
					fin();
					return(false);
					
		    	} else {
					return miSubmitFormConModo (fila, "denegar");
				}	
			} else {
				fin();
			}
			jQuery("[id^=total]").addClass("listaNonEditSelected"); // Actualiza el color de los totales
		}
		
		function miSubmitFormConModo (fila, modo) {
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j = 1;
			var flag = true;
			while (flag) {
				var dato = jQuery("#oculto" + fila + "_" + j);
			  	if (dato.exists())  {
			  		datos.value = datos.value + dato.val() + ','; 				  
				} else { 
					flag = false; 					
				}
			  	j++;
			}
			datos.value = datos.value + "%";		  
		   	document.forms[0].modo.value = modo;
		   	document.forms[0].submit();
		}
		
		function consultarcert(fila) {   
		  	var idPeticion = jQuery("#oculto" + fila + "_2");
		  	var idArticulo = jQuery("#oculto" + fila + "_4");
		  	var idArticuloInstitucion = jQuery("#oculto" + fila + "_5");
		  	var idTipoArticulo = jQuery("#oculto" + fila + "_6");
		  	var concepto = jQuery("#oculto" + fila + "_11");
		  	
		  	document.forms[0].idSolicitud.value = idPeticion.val();		  	
		  	document.forms[0].idProducto.value = idArticulo.val();
		  	document.forms[0].idProductoInstitucion.value = idArticuloInstitucion.val();
		  	document.forms[0].idTipoProducto.value = idTipoArticulo.val();
		  	document.forms[0].concepto.value = concepto.val();
	 		
	 		document.forms[0].action = "/SIGA/CER_GestionSolicitudes.do?buscar=true";
     		document.forms[0].target = "mainWorkArea";
     		document.forms[0].modo.value = "abrir";
     		document.forms[0].submit();
		}
		
		function anticiparImporte(fila){ 	
 			jQuery("#textImporteAnticipado").val(jQuery("#oculto" + fila + "_13").val());
 			jQuery("#textPrecioSolicitud").val(jQuery("#oculto" + fila + "_14").val());
 			
 	 		jQuery("#divImporteAnticipado").dialog(
				{
					height: 200,
					width: 500,
					modal: true,
					resizable: false,
					buttons: {
						"<%=sDialogBotonGuardarCerrar%>": function() {
							if (!validarImporteAnticipado()){
								return;
							}
							jQuery(this).dialog("close");
							guardarImporteAnticipado(fila);
						},
						"<%=sDialogBotonCerrar%>": function() {
							jQuery(this).dialog("close");
						}
					}
				}
			);
			jQuery(".ui-widget-overlay").css("opacity","0");	 				
			jQuery("[id^=total]").addClass("listaNonEditSelected"); // Actualiza el color de los totales
 		} 	
	 		
		function validarImporteAnticipado(){
			var vImporteAnticipado = jQuery("#textImporteAnticipado").val();			
			vImporteAnticipado = vImporteAnticipado.replace(/,/,".");
			jQuery("#textImporteAnticipado").val(vImporteAnticipado);
			var valorImporteAnticipado = parseFloat(vImporteAnticipado);
			
			var vPrecioSolicitud = jQuery("#textPrecioSolicitud").val();
			vPrecioSolicitud = vPrecioSolicitud.replace(/,/,".");
			jQuery("#textPrecioSolicitud").val(vPrecioSolicitud);
			var valorPrecioSolicitud = parseFloat(vPrecioSolicitud);
		
			if (isNaN(valorImporteAnticipado) || valorImporteAnticipado < 0 ){
				alert('<siga:Idioma key="messages.pys.solicitudCompra.errorAnticiparImporteNoValido"/>');
				return false;
			}
			
			if (valorPrecioSolicitud < valorImporteAnticipado){
				alert('<siga:Idioma key="messages.pys.solicitudCompra.errorAnticiparImporteSuperior"/>');
				return false;
			}
			
			return true;
		} 	 		
	 		
		function guardarImporteAnticipado(fila) {	 
			var formAnticipos = document.getElementById("MostrarAnticiposForm");
			formAnticipos.idTipoClave.value = jQuery("#oculto" + fila + "_6").val();
			formAnticipos.idClave.value = jQuery("#oculto" + fila + "_4").val();
			formAnticipos.idClaveInstitucion.value = jQuery("#oculto" + fila + "_5").val();
			formAnticipos.idPeticion.value = jQuery("#oculto" + fila + "_2").val();			
			if (jQuery("#oculto" + fila + "_1").val()=="servicio")
				formAnticipos.tipo.value = "S";
			else
				formAnticipos.tipo.value = "P";
			formAnticipos.totalAnticipado.value = jQuery("#oculto" + fila + "_13").val();
			formAnticipos.precioSolicitud.value = jQuery("#textPrecioSolicitud").val();
			formAnticipos.nuevoImporteAnticipado.value = jQuery("#textImporteAnticipado").val();
			formAnticipos.modo.value = "guardarAnticipos";
			formAnticipos.submit();
		}	
	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>