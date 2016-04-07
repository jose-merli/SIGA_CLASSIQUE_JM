<!DOCTYPE html>
<html>
<head>
<!-- comprobanteSolicitudImpresion.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import="com.siga.general.Articulo"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%> 
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<% 
	String app = request.getContextPath();

	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error  = request.getAttribute("error")==null?"NO":(String)request.getAttribute("error");

	int iCantidadTotal=0;
	double dNetoTotal=0, dIvaTotal=0, dPrecioTotal=0;
	
	//Datos del Action:
	Vector vListaPyS = new Vector();
	String idPeticion="", nombrePersona="", numero="", fecha="";
	//Si no hay error recupero los datos del request:
	if (error.equals("NO")) {
		Hashtable htData = (Hashtable)request.getAttribute("resultados");
		vListaPyS = (Vector)htData.get("vListaPyS");
		idPeticion = String.valueOf((Long)htData.get("idPeticion"));
		nombrePersona=(String)htData.get("nombrePersona");
		numero=(String)htData.get("numeroColegiado");
		fecha = (String)htData.get("fecha"); 	
	}
%>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<siga:Titulo titulo="pys.solicitudCompra.cabecera" localizacion="pys.solicitudCompra.ruta"/>
</head>

<body onLoad="accionImprimir();">

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%>&nbsp;&nbsp;
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

	<siga:ConjCampos leyenda="pys.solicitudCompra.literal.datosSolicitud">	
		<table class="tablaCampos" align="center">	
			<tr>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.idPeticion"/></td>				
				<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.fechaSolicitud"/></td>				
				<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>	
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nombreSolicitante"/></td>				
				<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>	
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.tipoSolicitud"/></td>				
				<td class="labelTextValue"><siga:Idioma key="pys.solicitudCompra.cabecera"/></td>
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
					pys.solicitudCompra.literal.importeAnticipado"  
		columnSizes="20,14,17,6,9,8,10,11"
		fixedHeight="-1">
		
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
				String descripcion="", periodicidad="", idFormaPago="";
				double precio;
				int cantidad;
				if (letraClase.equals("P")) {
					precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();														
					descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO").replaceAll("\r\n", " ").replaceAll("\n\r", " "));
					cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
					idFormaPago = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO);
					
				} else  {
					precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
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
%>
				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>							
					<td><%=descripcion%></td>
	  				<td><%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_FORMAPAGO"))%></td>
	  				<td><%=UtilidadesString.mostrarIBANConAsteriscos(cuenta)%></td>
	  				<td align="right"><%=cantidad%></td>
	  				<td align="right"><%=UtilidadesString.formatoImporte(precio)%>&nbsp;&euro;<%=periodicidad%></td>
	  				<td align="right"><%=UtilidadesNumero.formatoCampo(iva)%>&nbsp;%</td>
	  				<td>	
<%
						try {
							int estado = Integer.parseInt(idFormaPago); 
							if(estado==ClsConstants.TIPO_FORMAPAGO_TARJETA) { 
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
				</siga:FilaConIconos>
<%		
			}				

			dIvaTotal = UtilidadesNumero.redondea (dIvaTotal, 2);
			dPrecioTotal = UtilidadesNumero.redondea (dPrecioTotal, 2);
%>
			<tr class="listaNonEditSelected" style="height:30px">
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td colspan="3" align="center">
					<table border="0" cellpadding="5" cellspacing="0">
						<tr class="listaNonEditSelected">
							<td class="labelText" style="color:black; text-align:center; border:0px; vertical-align:middle">
								<siga:Idioma key="facturacion.lineasFactura.literal.Total"/>																	
							</td>
						</tr>
					
						<tr class="listaNonEditSelected">
							<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.cantidad"/></td>
							<td class="labelText" style="border:0px">
								<input type="text" name="cantidadTotal" value="<%=iCantidadTotal%>" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
							</td>
						</tr>							
					
						<tr class="listaNonEditSelected">
							<td class="labelText" style="color:black; text-align:right; border:0px" nowrap>
								<siga:Idioma key="pys.solicitudCompra.literal.totalImporteNeto"/>									
							</td>
							<td class="labelText" style="border:0px">
								<input type="text" name="netoTotal" value="<%=UtilidadesString.formatoImporte(dNetoTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
							</td>
						</tr>						
						
						<tr class="listaNonEditSelected">
							<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.iva"/></td>
							<td class="labelText" style="border:0px">
								<input type="text" name="ivaTotal" value="<%=UtilidadesString.formatoImporte(dIvaTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
							</td>
						</tr>
						
						<tr class="listaNonEditSelected" border="0">
							<td class="labelText" style="color:black; text-align:right; border:0px" nowrap><siga:Idioma key="pys.solicitudCompra.literal.totalImporte"/></td>
							<td class="labelText" style="border:0px">
								<input type="text" name="precioTotal" value="<%=UtilidadesString.formatoImporte(dPrecioTotal)%> &euro;" style="background-color:transparent; font-weight:bold; color:black; text-align:left" class="boxConsultaNumber" readOnly="true" size="13">
							</td>
						</tr>
					</table>
				</td>	
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