<!DOCTYPE html>
<html>
<head>
<!-- comprobanteSolicitud.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.GenParametrosAdm"%>
<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import="com.siga.general.Articulo"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%> 
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usrbean = (UsrBean)ses.getAttribute("USRBEAN");
	
	String cer = (String)request.getAttribute("CERTIFICADO_noFactura");
	boolean cer_nofactura  = (cer!=null);
	
	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error = request.getAttribute("error")==null ? "NO" : (String)request.getAttribute("error");

	//Si viene del action mostrarCompra su valor será "mostrarCompra" y sirve para 
	//no mostrar el mensaje de "compra realizada" que se muestra cuando se viene 
	//de los actions finalizarCompra y OkCompraTPV
	String mostrarCompra  = "S".equals(request.getAttribute("mostrarCompra")) ? "S" : "N";

	int iCantidadTotal=0;
	double dNetoTotal=0, dIvaTotal=0, dPrecioTotal=0;
	
	Vector vListaPyS = new Vector();
	String idPeticion="", idPersona="", nombrePersona="", numero="", fecha="";
	//Si no hay error recupero los datos del request:
	if (error.equals("NO")) {
		Hashtable htData = (Hashtable)request.getAttribute("resultados");
		vListaPyS = (Vector)htData.get("vListaPyS");
		idPeticion = String.valueOf((Long)htData.get("idPeticion"));
		idPersona = String.valueOf((Long)htData.get("idPersona"));
		nombrePersona = (String)htData.get("nombrePersona");
		numero = (String)htData.get("numeroColegiado");
		fecha = (String)htData.get("fecha"); 	
	}
	
	Boolean noFact = (Boolean)request.getAttribute("noFacturable");	
	if (noFact == null) {
		noFact = Boolean.FALSE;
	}
	
	Boolean PysFacturado = (Boolean) request.getAttribute("PYS_Facturado");
	if (PysFacturado == null) {
		PysFacturado = Boolean.FALSE;
	}
	
	// Textos del dialog
	String sDialogError= UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "messages.general.error"));
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarCerrar"));	
	
	GenParametrosAdm admParametros = new GenParametrosAdm(usrbean);
	String aprobarSolicitud = admParametros.getValor(usrbean.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<siga:Titulo titulo="pys.solicitudCompra.cabecera" localizacion="pys.solicitudCompra.ruta"/>
</head>

<body onLoad="resultado();">
<%  //INICIO DE LA PARTE DE ERROR O KO
	if (!error.equals("NO")) { %>
		<div id="titulo" style="width:100%; z-index: 5">
			<table align="center" height="20" cellpadding="0" cellspacing="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="messages.solicitudCompra.errorCompra"/>
					</td>
				</tr>
			</table>
		</div>
<%  //FIN DE LA PARTE DE ERROR O KO
	} else { 
	//INICIO DE LA PARTE DEL COMPROBANTE OK
%>
		<div id="divSeleccionSerieFacturacion" title="<siga:Idioma key='facturacion.seleccionSerie.titulo'/>" style="display:none">
			<table align="left">
				<tr>		
					<td class="labelText" nowrap>
						<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.serieFacturacion"/>&nbsp;(*)
					</td>
					<td>
						<select class='box' style='width:270px' id='selectSeleccionSerieFacturacion'>
						</select>
					</td>
				</tr>		
									
				<tr>
					<td class="labelTextValue" colspan="2">
						<siga:Idioma key="pys.gestionSolicitudes.aviso.seriesFacturacionMostradas"/>
					</td>
				</tr>	
			</table>			
		</div>
		
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

		<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
<%
					if (!numero.equalsIgnoreCase("")) {
%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
<%
					}
%>			
				</td>
			</tr>
		</table>
	
		<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="_blank" type=""> 
			<input type="hidden" name="modo" value=""> 
			<input type="hidden" name="actionModal" value=""> 			
		
			<siga:ConjCampos leyenda="pys.solicitudCompra.literal.datosSolicitud">	
				<table class="tablaCampos" align="center">	
					<tr>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.idPeticion"/></td>				
						<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.fechaSolicitud"/></td>				
						<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>	
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.tipoSolicitud"/></td>				
						<td class="labelTextValue"><siga:Idioma key="pys.tipoPeticion.alta"/></td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nombreSolicitante"/></td>				
						<td class="labelTextValue" colspan="5"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>	
					</tr>
				</table>
			</siga:ConjCampos>
		
			<siga:Table 
				name="cabecera"
				border="2"
				columnNames="pys.solicitudCompra.literal.concepto,
							pys.solicitudCompra.literal.formaPago,
							pys.solicitudCompra.literal.nCuenta,
							pys.solicitudCompra.literal.cantidad,
							pys.solicitudCompra.literal.precio,
							pys.solicitudCompra.literal.iva,
							pys.solicitudCompra.literal.estadoPago,
							pys.solicitudCompra.literal.importeAnticipado,"  
				columnSizes="22,14,18,6,9,8,9,9,5">
		
<% 				
				int i = -1;
				if (vListaPyS!=null && vListaPyS.size()>0) {
		 			Enumeration en = vListaPyS.elements();
					while(en.hasMoreElements()){
						i++;
						Hashtable hash = (Hashtable)en.nextElement();
						
						String cuenta = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_CUENTA"));						
						Integer clase = UtilidadesHash.getInteger(hash, "CLASE");
						String letraClase = (clase.intValue() == Articulo.CLASE_PRODUCTO ? "P" : "S");
						String idInstitucion="", idPeticionArticulo="", idArticulo="", idArticuloInstitucion="", idTipoClave="", descripcion="", periodicidad="", idFormaPago="";
						double precio;
						int cantidad;
						if (letraClase.equals("P")) {
							precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();
							idInstitucion = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDINSTITUCION);
							idPeticionArticulo = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPETICION);
							idArticulo = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTO);
							idArticuloInstitucion = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
							idTipoClave = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
							descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO").replaceAll("\r\n", " ").replaceAll("\n\r", " "));
							cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
							idFormaPago = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO);
							
						} else  {
							precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
							idInstitucion = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION);
							idPeticionArticulo = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDPETICION);
							idArticulo = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIO);
							idArticuloInstitucion = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
							idTipoClave = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
							descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO").replaceAll("\r\n", " ").replaceAll("\n\r", " ")) + " " + UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "SERVICIO_DESCRIPCION_PRECIO"));
							periodicidad = " / " + UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "PERIODICIDAD"));
							cantidad = UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_CANTIDAD).intValue();
							idFormaPago = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO);
						}
						
						double iva = UtilidadesHash.getFloat(hash,"VALORIVA").doubleValue();
						
						if (UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO)!=null) {
							iCantidadTotal += cantidad;
							dNetoTotal += cantidad * precio;
							dIvaTotal += UtilidadesNumero.redondea(cantidad * precio * iva / 100, 2);
							dPrecioTotal += UtilidadesNumero.redondea(cantidad * precio * (1 + (iva / 100)), 2);
						}				
		
						//recupera el flag para mostrar/ocultar el botón de anticipar y el importe anticipado
						boolean anticipar = UtilidadesHash.getBoolean(hash, "ANTICIPAR").booleanValue();
						Double aux = UtilidadesHash.getDouble(hash, "IMPORTEANTICIPADO");
						double importeAnticipado = aux != null ? aux.doubleValue() : 0.0;
						
						FilaExtElement[] elementos = null;
						if (letraClase.equals("P") && !PysFacturado && (anticipar || importeAnticipado > 0.0)) {
							elementos = new FilaExtElement[1];								
							elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
						}
%>
						<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>							
							<td>
								<input type="hidden" id="oculto<%=i+1%>_idArticulo" name="oculto<%=i+1%>_idArticulo" value="<%=idArticulo%>">
							    <input type="hidden" id="oculto<%=i+1%>_idArticuloInstitucion" name="oculto<%=i+1%>_idArticuloInstitucion" value="<%=idArticuloInstitucion%>">
							    <input type="hidden" id="oculto<%=i+1%>_idPeticion"name="oculto<%=i+1%>_idPeticion" value="<%=idPeticionArticulo%>">
							    <input type="hidden" id="oculto<%=i+1%>_idTipoClave" name="oculto<%=i+1%>_idTipoClave" value="<%=idTipoClave%>">
							    <input type="hidden" id="oculto<%=i+1%>_tipo" name="oculto<%=i+1%>_tipo" value="<%=letraClase%>">
							    <input type="hidden" id="oculto<%=i+1%>_idInstitucion" name="oculto<%=i+1%>_idInstitucion" value="<%=idInstitucion%>">
							    <input type="hidden" id="oculto<%=i+1%>_importeAnticipado" name="oculto<%=i+1%>_importeAnticipado" value="<%=importeAnticipado%>">
							    <input type="hidden" id="oculto<%=i+1%>_precioSolicitud" name="oculto<%=i+1%>_precioSolicitud" value="<%=UtilidadesNumero.redondea(cantidad * precio * (1 + (iva / 100)), 2)%>">
			  					<%=descripcion%>						  								
			  				</td>
			  				<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%></td>
			  				<td><%=UtilidadesString.mostrarIBANConAsteriscos(cuenta)%></td>
			  				<td align="right"><%=cantidad%></td>
			  				<td align="right"><%=UtilidadesString.formatoImporte(precio)%>&nbsp;&euro;<%=periodicidad%></td>
			  				<td align="right"><%=UtilidadesNumero.formatoCampo(iva)%>&nbsp;%</td>
			  				<td>	
<%
								try {
									int estado = Integer.parseInt(idFormaPago); 
									if(estado == ClsConstants.TIPO_FORMAPAGO_TARJETA) { 
%>
										<siga:Idioma key="pys.estadoPago.pagado"/>
<%
									} else { 
%>
										<siga:Idioma key="pys.estadoPago.pendiente"/>
<%
									} 
								} catch(Exception e) { %>
										<siga:Idioma key="pys.estadoPago.noFacturable"/>	
<%
								}
%>
					  		</td>
					  		<td align="right"><%=UtilidadesString.formatoImporte(importeAnticipado)%>&nbsp;&euro;</td>
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
					}	
					
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
<%
										if (noFact) { 
%>
											<siga:ToolTip id='ayudaNoFacturable' imagen='/SIGA/html/imagenes/botonAyuda.gif' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean,"messages.pys.solicitudCompra.compraNoFacturable"))%>' />
<%
										}
%>
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
					</tr>						

<%					
			 	} else {
%>
					<tr class="notFound">
			  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>			
<%
			 	}
%>
			</siga:Table>
		</html:form>
	
		<!-- Este formulario se emplea para abrir la ventana modal de modificación de los anticipos-->
		<html:form styleId="MostrarAnticiposForm" action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">
		    <input type="hidden" name="idTipoClave" value="">
			<input type="hidden" name="idClave" value=""> 
			<input type="hidden" name="idClaveInstitucion" value=""> 
			<input type="hidden" name="idPeticion" value=""> 
			<input type="hidden" name="idPersona" value="<%=idPersona%>"> 
			<input type="hidden" name="tipo" value=""> 
			<input type="hidden" name="totalAnticipado" value=""/>
			<input type="hidden" name="precioSolicitud" value=""/>
			<input type="hidden" name="nuevoImporteAnticipado" value=""/>			
			<input type="hidden" name="modo" value="guardarAnticipos"> 
			<input type="hidden" name="actionModal" value="">
			<html:hidden styleId = "tablaDatosDinamicosD"  property = "tablaDatosDinamicosD" value=""/>
		</html:form>	
		
<% 
		if (usrbean.isLetrado() || cer_nofactura || !aprobarSolicitud.equals("S")) { 
%>
			<siga:ConjBotonesAccion botones="IA" clase="botonesDetalle"/>			
<% 
		} else { 
%>
			<siga:ConjBotonesAccion botones="FR,IA" clase="botonesDetalle"/>			
<% 
		}	  
	} //FIN DE LA PARTE DEL COMPROBANTE OK 
%> 

	<script language="JavaScript">		
		var error = '<%=error%>';
		
 		function resultado(){
 			fin(); // Finaliza el bloqueo de la accion accionFacturacionRapida()
 			if (error == 'NO') {
	 			f = document.solicitudCompraForm; 
	 			if ("S" != "<%=mostrarCompra%>"){
					if (f.modo.value==""){
	 					var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.compraRealizada"/>";						
						alert(mensaje);	
						f.modo.value="Imprimir";
				  	}
	 			}
			}
 		}
 		
 		function accionImprimirApaisado(){
 			window.open("/SIGA/PYS_GenerarSolicitudes.do?modo=imprimirCompra", '_blank');
		 	//ventaModalGeneralScrollAuto("solicitudCompraForm","G");			
 		}
 		
 		function accionFacturacionRapida(){
 			sub();
		 	var tableCabecera = document.getElementById("cabecera");
		 	var filas = tableCabecera.rows.length;
		 	var numColumnas = tableCabecera.rows[0].cells.length - 1;
		 	
		 	for (var a = 1; a < filas ; a++) {
		 		tableCabecera.rows[a].cells[numColumnas].innerHTML = "";
		 	}

			var divAsistencias = document.getElementById("divAsistencias");
			if(divAsistencias) 
				 divAsistencias.innerHTML="";
						
		    var idInstitucion = document.getElementById('oculto1_idInstitucion');
		    var idPeticion = document.getElementById('oculto1_idPeticion');
		    
		    jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/PYS_GenerarSolicitudes.do?modo=getAjaxSeleccionSerieFacturacion",				
				data: "idInstitucion=" + idInstitucion.value + "&idPeticion=" + idPeticion.value,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){
					fin(); // finaliza el sub de accionFacturacionRapida()
						
					// Recupera el identificador de la serie de facturacion
					var idSerieFacturacion = json.idSerieFacturacion;		
					
					if (idSerieFacturacion==null || idSerieFacturacion=='') {
						//jQuery("#selectSeleccionSerieFacturacion")[0].innerHTML = json.aOptionsSeriesFacturacion[0];
						jQuery("#selectSeleccionSerieFacturacion").find("option").detach();
						jQuery("#selectSeleccionSerieFacturacion").append(json.aOptionsSeriesFacturacion[0]);
						
						jQuery("#divSeleccionSerieFacturacion").dialog(
							{
								height: 220,
								width: 550,
								modal: true,
								resizable: false,
								buttons: {
									"<%=sDialogBotonGuardarCerrar%>": function() {
										idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
										if (idSerieFacturacion==null || idSerieFacturacion=='') {
											alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
											
										} else {
											jQuery(this).dialog("close");
											sub(); // Inicia el bloqueo hasta que haya realizado la accion
											document.solicitudCompraForm.target = "submitArea";
											document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
											document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;											
											document.solicitudCompraForm.submit();	
											ocultarIconosAnticipos();
										}
									},
									"<%=sDialogBotonCerrar%>": function() {
										jQuery(this).dialog("close");
									}
								}
							}
						);
						jQuery(".ui-widget-overlay").css("opacity","0");														
						
					} else {
						sub(); // Inicia el bloqueo hasta que haya realizado la accion
						document.solicitudCompraForm.target = "submitArea";
						document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
						document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;							
						document.solicitudCompraForm.submit();
						ocultarIconosAnticipos();
					}													
				},
				
				error: function(e){
					fin(); // finaliza el sub de accionFacturacionRapida()
					alert("<%=sDialogError%>");					
				}
			});			    
 		}
 		
 		function ocultarIconosAnticipos() {
 			// Oculta todos los iconos de anticipos al facturar
 			jQuery("[id^=iconoboton_anticiparImporte]").hide();
 		}
 		
 		function anticiparImporte(fila){ 	
 			jQuery("#textImporteAnticipado").val(jQuery("#oculto" + fila + "_importeAnticipado").val());
 			jQuery("#textPrecioSolicitud").val(jQuery("#oculto" + fila + "_precioSolicitud").val());
 			
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
			formAnticipos.idTipoClave.value = jQuery("#oculto" + fila + "_idTipoClave").val();
			formAnticipos.idClave.value = jQuery("#oculto" + fila + "_idArticulo").val();
			formAnticipos.idClaveInstitucion.value = jQuery("#oculto" + fila + "_idArticuloInstitucion").val();
			formAnticipos.idPeticion.value = jQuery("#oculto" + fila + "_idPeticion").val();
			formAnticipos.tipo.value = jQuery("#oculto" + fila + "_tipo").val();
			formAnticipos.totalAnticipado.value = jQuery("#oculto" + fila + "_importeAnticipado").val();
			formAnticipos.precioSolicitud.value = jQuery("#textPrecioSolicitud").val();
			formAnticipos.nuevoImporteAnticipado.value = jQuery("#textImporteAnticipado").val();
			formAnticipos.modo.value = "guardarAnticipos";
			formAnticipos.submit();
		}
 		
 		function refrescarLocal(){
			document.solicitudCompraForm.modo.value = "mostrarCompra";
		 	document.solicitudCompraForm.target="mainWorkArea";			
		 	document.solicitudCompraForm.submit();
 		}
	</script>	

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>