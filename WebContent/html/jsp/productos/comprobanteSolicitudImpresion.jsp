<!DOCTYPE html>
<html>
<head>
<!-- comprobanteSolicitudImpresion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import = "com.siga.beans.PysServiciosInstitucionBean"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import = "java.util.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	//UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error  = request.getAttribute("error")==null?"NO":(String)request.getAttribute("error");

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
	float varIvaTotal = 0;
	double varPrecioTotal = 0;
	
	//Datos del Action:
	Hashtable htData = new Hashtable();
	Vector vListaPyS = new Vector();
	String idPeticion="", nombrePersona="", numero="", fecha="";
	//Si no hay error recupero los datos del request:
	if (error.equals("NO")) {
		htData = (Hashtable)request.getAttribute("resultados");
		vListaPyS = (Vector)htData.get("vListaPyS");
		idPeticion = String.valueOf((Long)htData.get("idPeticion"));
		nombrePersona=(String)htData.get("nombrePersona");
		numero=(String)htData.get("numeroColegiado");
		fecha = (String)htData.get("fecha"); 	
	}
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	<script language="JavaScript">		
 		
 		function accionImprimir(){
	 		window.print();
 		}
 		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="accionImprimir();">

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
			    <%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%} %>			
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
		columnSizes="20,15,17,8,8,6,10,11"
		fixedHeight="-1"
		modal = "G">
		
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
				String idPeticionArticulo="", idArticulo="", idArticuloInstitucion="", idTipoClave="", descripcion="", periodicidad="", idFormaPago="";
				double precio;
				int cantidad;
				if (letraClase.equals("P")) {
					precio = UtilidadesHash.getDouble(hash, PysProductosSolicitadosBean.C_VALOR).doubleValue();														
					idPeticionArticulo = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPETICION);
					idArticulo = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTO);
					idArticuloInstitucion = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
					idTipoClave = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);
					descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"));
					cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
					idFormaPago = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO);
					
				} else  {
					precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
					idPeticionArticulo = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDPETICION);
					idArticulo = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIO);
					idArticuloInstitucion = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
					idTipoClave = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
					descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO")) + " " + UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "SERVICIO_DESCRIPCION_PRECIO"));
					periodicidad = " / " + UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "PERIODICIDAD"));
					cantidad = UtilidadesHash.getInteger(hash, PysServiciosSolicitadosBean.C_CANTIDAD).intValue();
					idFormaPago = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO);
				}
				
				float iva = UtilidadesHash.getFloat(hash,"VALORIVA").floatValue();
				

				if((UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDFORMAPAGO))!=null){
					varIvaTotal = varIvaTotal +  (cantidad * ((float)(precio / 100)) * iva);
					varPrecioTotal = varPrecioTotal + (cantidad * (precio * (1 + (iva / 100))));
				}

				//recupera el flag para mostrar/ocultar el botón de anticipar y el importe anticipado
				boolean anticipar = UtilidadesHash.getBoolean(hash, "ANTICIPAR").booleanValue();
				Double aux = UtilidadesHash.getDouble(hash, "IMPORTEANTICIPADO");
				double importeAnticipado = aux != null ? aux.doubleValue() : 0.0;
%>
				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>							
					<td>
						<input type="hidden" name="oculto<%=i+1%>_idArticulo" value="<%=idArticulo%>">
					    <input type="hidden" name="oculto<%=i+1%>_idArticuloInstitucion" value="<%=idArticuloInstitucion%>">
					    <input type="hidden" name="oculto<%=i+1%>_idPeticion" value="<%=idPeticionArticulo%>">
					    <input type="hidden" name="oculto<%=i+1%>_idTipoClave" value="<%=idTipoClave%>">
					    <input type="hidden" name="oculto<%=i+1%>_tipo" value="<%=letraClase%>">
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
							if(estado==tarjeta) { 
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
	 	} else {
%>
			<tr class="notFound">
	  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>			
<%
	 	}
%>
	</siga:Table>		

<%
	varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
	varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2); 
%>
	<table width="280px" align="center">
		<tr>
			<td>
				<fieldset>
					<table>						
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>
							<td class="labelTextValue" >					
								<input type='text' name='ivaTotal' value="<%=UtilidadesString.formatoImporte(varIvaTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
							</td>
						</tr>
						
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
							<td class="labelTextValue">
								<input type='text' name='precioTotal' value="<%=UtilidadesString.formatoImporte(varPrecioTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20">
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	
	<table width="100%" align="center">
		<tr>
			<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.servicios.precioServicios"/></td>
		</tr>
	</table>
					
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>