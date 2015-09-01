<!DOCTYPE html>
<html>
<head>
<!-- datosSolicitudImpresion.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@taglib uri="struts-tiles.tld" prefix="tiles" %>
<%@taglib uri="struts-bean.tld" prefix="bean" %>
<%@taglib uri="struts-html.tld" prefix="html" %>
<%@taglib uri="libreria_SIGA.tld" prefix="siga" %>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%> 
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<%   	 
	String app = request.getContextPath();
	Vector peticion = (Vector) request.getAttribute("peticion");
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	int iCantidadTotal=0;
	double dNetoTotal=0, dIvaTotal=0, dPrecioTotal=0;
	String tipoPeticion = "";
	String nombreUsu = (String) request.getAttribute("nombreUsuario");
	String numeroColegiado = (String) request.getAttribute("nColegiado");
	String tipoSolicitud = (String) request.getAttribute("tipoSolicitud");
	String sidPeticion = String.valueOf((Long) request.getAttribute("idPeticion"));
	String fechaEfectiva="";
%>

	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
</head>

<body onLoad="accionImprimir();">
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
			"pys.gestionSolicitudes.literal.importeAnticipado";		
%>	
	<siga:Table 
		name="tablaResultados"
		border="2"
		columnNames="<%=sColumnNames%>"
		columnSizes="13,9,17,5,9,6,7,7,6,7,7"
		fixedHeight="-1">
<% 
		if (peticion != null && peticion.size() > 0) { 
			for (int i = 0; i < peticion.size(); i++) { 
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
	%>
					<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='' clase='listaNonEdit' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>
						<td><%=UtilidadesString.mostrarDatoJSP(concepto.replaceAll("\r\n", " ").replaceAll("\n\r", " "))%></td>
			
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
	
	<div id="divEspera" title="Espere por favor" style="z-index:100;position:absolute;display:none;top:45%;left:50%">
		<div style="position:relative;left:-50%">
			<br><img src="<%=app%>/html/imagenes/loadingBar.gif"/>
		</div>
	</div>

	<script language="JavaScript">
		var bloqueado=false;
		
		function mainSub(msg){
			if(!bloqueado){
				if (typeof msg == "undefined")
					msg = "";
				try{
					jQuery.blockUI({
						message: '<div id="barraBloqueante"><span class="labelText">'+msg+'</span><br><img src="<%=app%>/html/imagenes/loadingBar.gif"/></div>', 
						css:{border:0, background:'transparent'},
						overlayCSS: {backgroundColor:'#FFF', opacity: .0} 
					});
				} catch(e){
					jQuery("#divEspera").show();
				}
				bloqueado=true;
			}
		}
	
		function mainFin(){
			if(bloqueado){
				try{
					jQuery.unblockUI();
				} catch(e){
				}
				jQuery("#divEspera").hide();
				bloqueado=false; 
			} 
		}
		
		function accionImprimir(){
 			window.print();
		}
	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>