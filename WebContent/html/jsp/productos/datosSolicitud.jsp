<!DOCTYPE html>
<html>
<head>
<!-- datosSolicitud.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@taglib uri = "struts-bean.tld" prefix="bean" %>
<%@taglib uri = "struts-html.tld" prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga" %>

<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysServiciosSolicitadosBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<%   	 
	Vector peticion = (Vector) request.getAttribute("peticion");
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	float ivaTotal = 0;
	double precioTotal = 0;
	String tipoPeticion = "";
	
	String nombreUsu = (String) request.getAttribute("nombreUsuario");
	String numeroColegiado = (String) request.getAttribute("nColegiado");
	String idPersona = String.valueOf((Long) request.getAttribute("idPersona"));
	String tipoSolicitud = (String) request.getAttribute("tipoSolicitud");
	String siCierroVentanaRefresco = ""; // (String) request.getSession().getAttribute("DATABACKUP");
	String fechaEfectiva="";
	boolean letrado=usrbean.isLetrado();
%>

	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
<script language="JavaScript">

	// Refrescar
	function refrescarLocal() { 		
			top.cargaContenidoModal();
	}	

	// Asociada al boton Cerrar
	function accionCerrar() {
		top.cierraConParametros("MODIFICADO");
		
/*		
		<% if ((siCierroVentanaRefresco != null) && (siCierroVentanaRefresco.equalsIgnoreCase("siCierroVentanaRefresco"))) {%>	
			top.cierraConParametros("MODIFICADO");
		<%} else {%>
			top.cierraConParametros();
		<%}%>
*/
	}	
	
	// Asociada al boton Cerrar
	function accionImprimir() { 		
		window.print();
	}	
	
	function confirmarSolicitudCompra (fila) {
	
	  	var productoOservicio = document.getElementById('oculto' + fila + '_1');
	  	sub();
   		// if (!letrado){ 
   			
   			document.forms[0].modo.value = "fechaEfectiva";
			var fecha=ventaModalGeneral(document.forms[0].name, "S");   			
	    	//var fecha = showModalDialog("/SIGA/html/jsp/productos/ventanaFechaEfectiva.jsp","","dialogHeight:200px;dialogWidth:400px;help:no;scroll:no;status:no;");
	    	
	     	window.top.focus();
	     	if( fecha!=null){ 
   	  			document.forms[0].fechaEfectiva.value=fecha;
         	} else {   
         		fin();   
		  		return;
     	 	} 
		//  } 
  
	  	var existePrecioAsociado=document.getElementById('oculto' + fila + '_' + 10);
	  	if (existePrecioAsociado.value == "0") { 
			var mensaje='<siga:Idioma key="messages.pys.gestionSolicitud.errorProductoSinPrecio"/>';
			alert(mensaje);
			fin();
			return(false);
	  	} else {
				// Cogemos la forma de pago
			  var aux = 'oculto' + fila + '_' + 7;
			  var oculto = document.getElementById(aux);
			  if (oculto == null) { 
		  		fin();
			    	return false;
			  }
		    
			  var idFormaPago = oculto.value; 
		
		    //PAGO CON CUENTA BANCARIA
			if (idFormaPago == <%=ClsConstants.TIPO_FORMAPAGO_FACTURA%>){	
				// Cogemos el numero de cuenta
			    aux = 'oculto' + fila + '_' + 9;	   
			    var idCuenta = document.getElementById(aux);	
			    
				<% 
				if (tipoPeticion.equalsIgnoreCase(ClsConstants.TIPO_PETICION_COMPRA_ALTA)){ %>
	
			    if ((idCuenta.value == "null") || (idCuenta.value == null) || (idCuenta.value == "")){
						// Abrimos la ventana para recuperar el numero de cuenta
						document.forms[0].modo.value = "modificarCuenta";				
						var resultado = ventaModalGeneral("GestionSolicitudesForm","P");					
						if (resultado!= undefined && resultado[0]!=undefined){
							aux = 'cuenta' + fila;	
							var nCuenta = document.getElementById(aux);	
							document.GestionSolicitudesForm.idCuenta.value = resultado[0];					
							nCuenta.value=resultado[1];			
						} else {
							fin();
							return false;
						}				
			    }
			   <% } %>
			} //Fin pago con cuenta bancaria
						
			return miSubmitFormConModo (fila, "confirmar");
		}	//Fin de que existe precio
	}

	function denegarSolicitudCompra (fila) {
		var existePrecioAsociado=document.getElementById('oculto' + fila + '_' + 10);
		sub();
		if(confirm("<siga:Idioma key="messages.pys.solicitudCompra.confirmarDenegar"/>")){
	    	if (existePrecioAsociado.value == "0") { 
				var mensaje='<siga:Idioma key="messages.pys.solicitudCompra.errorProductoSinPrecio"/>';
				alert(mensaje);
				fin();
				return(false);
	    	}else{
				return miSubmitFormConModo (fila, "denegar");
			}	
		}else{
			fin();
		}
	}
	
	function miSubmitFormConModo (fila, modo) {
	   var datos;
	   datos = document.getElementById('tablaDatosDinamicosD');
	   datos.value = ""; 
	   preparaDatos(fila,'tablaResultados', datos);
	  
	   document.forms[0].modo.value = modo;
	   document.forms[0].submit();
	}
	
	
	function consultarcert(fila) {   
	  var aux = 'oculto' + fila + '_2'; 
	  var oculto = document.getElementById(aux);
	  aux='oculto' + fila + '_11';
	  var nombre=document.getElementById(aux);
	  aux='oculto' + fila + '_4';
	  var idproducto=document.getElementById(aux);
	  aux='oculto' + fila + '_5';
	  var idproductoInstitucion=document.getElementById(aux);
	  aux='oculto' + fila + '_6';
	  var idTipoproducto=document.getElementById(aux);
	  document.forms[0].idSolicitud.value=oculto.value;
	  document.forms[0].concepto.value=nombre.value;
	  cierraConParametros("@#@#"+document.forms[0].concepto.value+"#@#"+idproducto.value+"#@#"+idproductoInstitucion.value+"#@#"+idTipoproducto.value);  
	}
     
    //Muestra la ventana de anticipo de importes
 	function anticiparImporte (fila) {
 		var aux = 'oculto' + fila + '_6'; 
 		var idTipoClave = document.getElementById(aux).value;

 		var aux = 'oculto' + fila + '_4'; 
 		var idClave = document.getElementById(aux).value;

 		var aux = 'oculto' + fila + '_5'; 
 		var idClaveInstitucion = document.getElementById(aux).value;

 		var aux = 'oculto' + fila + '_2'; 
 		var idPeticion = document.getElementById(aux).value;

 		var productoOservicio = document.getElementById('oculto' + fila + '_1').value;

 		if (productoOservicio=="servicio"){
 			tipo = 'S';
 		}
 		else{
 			tipo = 'P'
 		}
 			
 		//actualizar los datos del formulario de mostrar anticipos
 		document.getElementById("MostrarAnticiposForm").idTipoClave.value = idTipoClave;
 		document.getElementById("MostrarAnticiposForm").idClave.value = idClave;
 		document.getElementById("MostrarAnticiposForm").idClaveInstitucion.value = idClaveInstitucion;
 		document.getElementById("MostrarAnticiposForm").idPeticion.value = idPeticion;
 		document.getElementById("MostrarAnticiposForm").tipo.value = tipo;

 		ventaModalGeneral("MostrarAnticiposForm","P");					

 		refrescarLocal();
   	} 	
</script>

<body>

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<% if (!tipoSolicitud.equals("BAJA")) {  %>  
					<siga:Idioma key="pys.gestionSolicitudes.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>&nbsp;&nbsp;
				<% } else { %>
					<siga:Idioma key="pys.gestionSolicitudes.titulo3"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>&nbsp;&nbsp;
				<% } %>
				
				<% if ((numeroColegiado != null) && (!numeroColegiado.equalsIgnoreCase(""))) { %>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroColegiado)%>
				<% } else { %>
					<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<% } %>
			</td> 
		</tr>
	</table>

	<html:form action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">		
		<input type="hidden" name="idSolicitud" value=""> 
		<input type="hidden" name="concepto" value=""> 
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "importeAnticipado" value = ""/>
		<html:hidden property = "idCuenta" value = ""/>
		<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
		
		<!-- RGG: cambio a formularios ligeros -->
		
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="fechaEfectiva" value="<%=fechaEfectiva%>">
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
		name = "tablaResultados"
		border  = "2"
		columnNames = "<%=sColumnNames%>"
		columnSizes = "17,9,15,5,7,6,7,7,6,7,7,7">

	<% if ((peticion != null) && (peticion.size() > 0)){ 
			for (int i = 0; i < peticion.size(); i++) { 
				FilaExtElement[] elementos=new FilaExtElement[2];
				 Hashtable productoServicio = (Hashtable) peticion.get(i);

				//Fecha Efectiva:
				fechaEfectiva = (String) productoServicio.get("FECHAEFEC");
				if (fechaEfectiva==null || fechaEfectiva.equals("")) {
					fechaEfectiva = "&nbsp;";
				} else {
					// formateo
					fechaEfectiva = GstDate.getFormatedDateShort(usrbean.getLanguage(),fechaEfectiva);
				}
									
				if (productoServicio != null){ 
					Long idPeticion, idArticulo, idArticuloInstitucion;
					Integer idInstitucion,idTipoArticulo, idCuenta, idTipoEnvios;
					String estado;
					int cantidad;
					double precio, importeUnitario;
					float iva = 0;
				
					//String estadoPago 			= UtilidadesProductosServicios.getEstadoPago(UtilidadesHash.getString(productoServicio, "ESTADOPAGO"));
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
					//El importeAnticipado será < 0 si no se puede anticipar, en caso contrario
					//contendrá el valor del importe anticipado actual.
					//esta variable se usa tanto para decidir si mostrar o no el botón de "anticipar"
					//como el valor actual del anticipo.
					int anticipar = -1;
					double importeAnticipado = 0.0;
				
					// Producto
					if (tipoTablaPetion.trim().equalsIgnoreCase("PRODUCTO")) {					  
						productoOServicio = "producto";
						
						if(idTipoEnvios == null){	
							elementos[0]=new FilaExtElement("confirmarSolicitudCompra", "confirmarSolicitudCompra", SIGAConstants.ACCESS_FULL);
							elementos[1]=new FilaExtElement("denegarSolicitudCompra", "denegarSolicitudCompra", SIGAConstants.ACCESS_FULL);
							
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
							anticipar = Integer.parseInt(aux.split("#")[0]);
							importeAnticipado = Double.parseDouble(aux.split("#")[1]);
							
						} else {
							anticipar = Integer.valueOf(aux).intValue();
						}
				
						idCuenta = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_IDCUENTA);
						cantidad = UtilidadesHash.getInteger (productoServicio, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
						if (UtilidadesHash.getDouble (productoServicio, PysProductosSolicitadosBean.C_VALOR)!=null){
							precio = UtilidadesHash.getDouble (productoServicio, PysProductosSolicitadosBean.C_VALOR).doubleValue();
							//iva = UtilidadesHash.getFloat (productoServicio, PysProductosSolicitadosBean.C_PORCENTAJEIVA).floatValue();
							iva = UtilidadesHash.getFloat (productoServicio, "VALORIVA").floatValue();
							existePrecio="1";
							
						} else {
							precio = 0;
							iva = 0;
							existePrecio="0";
						}
					
					// Servicio
					} else {
						if (fechaEfectiva==null||fechaEfectiva.equals("&nbsp;")||fechaEfectiva.equals(" ")||fechaEfectiva.equals("")) { 
							elementos[0]=new FilaExtElement("confirmarSolicitudCompra", "confirmarSolicitudCompra", SIGAConstants.ACCESS_FULL);
							elementos[1]=new FilaExtElement("denegarSolicitudCompra", "denegarSolicitudCompra", SIGAConstants.ACCESS_FULL);
					  	}	
				
						productoOServicio = "servicio";
						idPeticion 			= UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDPETICION);
						idInstitucion 	= UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_IDINSTITUCION);
						idTipoArticulo 	= UtilidadesHash.getInteger (productoServicio, PysServiciosSolicitadosBean.C_IDTIPOSERVICIOS);
						idArticulo 			= UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDSERVICIO);
						idArticuloInstitucion = UtilidadesHash.getLong (productoServicio, PysServiciosSolicitadosBean.C_IDSERVICIOSINSTITUCION);
						aceptado = UtilidadesHash.getString(productoServicio, PysServiciosSolicitadosBean.C_ACEPTADO);										
						estado 					= UtilidadesProductosServicios.getEstadoProductoServicio(aceptado);
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
							iva = UtilidadesHash.getFloat (productoServicio, "PORCENTAJEIVA").floatValue();
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
					importeUnitario = (cantidad * (precio * (1 + (iva / 100))));
					
					if(idFormaPago!=null){
						ivaTotal = ivaTotal +  (cantidad * ((float)(precio / 100)) * iva);
						precioTotal = precioTotal + importeUnitario;
					}
				
					tipoPeticion = UtilidadesHash.getString(productoServicio, PysPeticionCompraSuscripcionBean.C_TIPOPETICION);
					
					if ((idTipoEnvios == null) && (!aceptado.equalsIgnoreCase(ClsConstants.PRODUCTO_PENDIENTE))) {	
						//Comprueba si debe añadir el botón de anticipar importe
						if(anticipar > 0){
							elementos=new FilaExtElement[2];
							elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
						}
						else{
						   elementos = null;
						}
					}									
	%>

		<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='' clase='listaNonEdit' elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>

			<!-- Datos ocultos tabla -->
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

				<%=UtilidadesString.mostrarDatoJSP(concepto)%>
			</td>
			
			<td>										
				<%if(idFormaPago==null){ %>
					No Facturable
				<%}else{%>
					<%=UtilidadesString.mostrarDatoJSP(formaPago)%>
				<%}%>
			</td> 

			<td><%=UtilidadesString.mostrarIBANConAsteriscos(nCuenta)%></td>
			 
			<td align="right"><%=cantidad%></td> 
		
			<% if (UtilidadesHash.getDouble (productoServicio, "VALOR")!=null){ %>
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
			<% }else{ %>
				<td>- </td>
				<td>- </td> 
			<% } %>
							
			<td><siga:Idioma key="<%=estadoPago%>"/></td>			
			
			<td>
				<% if(idFormaPago!=null){ %>									
					<siga:Idioma key="<%=estado%>"/>
				<% } else { %>
					No Facturable
				<% } %>
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
			
			<%if (elementos == null){ %>
				<td></td>
			<%} %>
		</siga:FilaConIconos>
							 		
		<%	 		} // if
				}  // for  
				 	 
			} else {	 	 
		%>
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		<%	}  %>
	</siga:Table>

	<div id="camposRegistro2" style="position:absolute; left:500px;width:200px; height:80; z-index:2; bottom: 30px;" align="center">
		<% if (tipoPeticion.equalsIgnoreCase(ClsConstants.TIPO_PETICION_COMPRA_ALTA)) {							
				ivaTotal = UtilidadesNumero.redondea (ivaTotal, 2);
				precioTotal = UtilidadesNumero.redondea (precioTotal, 2);
		%>
			<fieldset>
				<table>
					<tr>
						<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.totalIVA"/></td>
						<td class="labelTextNum" align="right"><%=UtilidadesString.formatoImporte(ivaTotal)%> &euro;</td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.total"/></td>
						<td class="labelTextNum" align="right"><%=UtilidadesString.formatoImporte(precioTotal)%> &euro;</td>
					</tr>
				</table>
			</fieldset>
		<%}%>
	</div>

	<siga:ConjBotonesAccion botones="C,I" modo=''  modal="G" clase="botonesDetalle"/>

	<!-- Este formulario se emplea para abrir la ventana modal de modificación de los anticipos-->
	<html:form styleId="MostrarAnticiposForm" action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">
	    <input type="hidden" name="idTipoClave" value="">
		<input type="hidden" name="idClave" value=""> 
		<input type="hidden" name="idClaveInstitucion" value=""> 
		<input type="hidden" name="idPeticion" value=""> 
		<input type="hidden" name="idPersona" value="<%=idPersona%>"> 
		<input type="hidden" name="tipo" value=""> 
		<input type="hidden" name="modo" value="mostrarAnticipos"> 
		<input type="hidden" name="actionModal" value="">
	</html:form>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>