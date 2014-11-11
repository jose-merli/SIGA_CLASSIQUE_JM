<!DOCTYPE html>
<html>
<head>
<!-- comprobanteSolicitud.jsp -->
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
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "java.util.*"%>
<%@ page import = "com.siga.general.Articulo"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usrbean=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String cer = (String)request.getAttribute("CERTIFICADO_noFactura");
	
	boolean cer_nofactura  = (cer!=null);
	
		
	//Variable que me indica si vengo de un error. En tal caso muestro el error.
	String error  = request.getAttribute("error")==null?"NO":(String)request.getAttribute("error");

	//Si viene del action mostrarCompra su valor ser� "mostrarCompra" y sirve para 
	//no mostrar el mensaje de "compra realizada" que se muestra cuando se viene 
	//de los actions finalizarCompra y OkCompraTPV
	String mostrarCompra  = "S".equals(request.getAttribute("mostrarCompra"))?"S":"N";

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
	float varIvaTotal = 0;
	double varPrecioTotal = 0;
	
	//Datos del Action:
	Hashtable htData = new Hashtable();
	Vector vListaPyS = new Vector();
	String idPeticion="", idPersona="", nombrePersona="", numero="", fecha="";
	//Si no hay error recupero los datos del request:
	if (error.equals("NO")) {
		htData = (Hashtable)request.getAttribute("resultados");
		vListaPyS = (Vector)htData.get("vListaPyS");
		idPeticion = String.valueOf((Long)htData.get("idPeticion"));
		idPersona=String.valueOf((Long)htData.get("idPersona"));
		nombrePersona=(String)htData.get("nombrePersona");
		numero=(String)htData.get("numeroColegiado");
		fecha = (String)htData.get("fecha"); 	
	}
	
	Boolean noFact = (Boolean)request.getAttribute("noFacturable");
	if(noFact == null){
		noFact = Boolean.FALSE;
	}
	
	// Textos del dialog
	String sDialogError= UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "messages.general.error"));
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarCerrar"));	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="pys.solicitudCompra.cabecera" localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	<script language="JavaScript">		
		var error = '<%=error%>';
		
 		function resultado(){
 			if (error == 'NO') {
	 			f = document.solicitudCompraForm; 
	 			if ("S" != "<%=mostrarCompra%>"){
	 			  if (f.modo.value==""){
	 				var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.compraRealizada"/>";						
					alert(mensaje);	
					f.modo.value="Imprimir";
					//validarAncho_cabecera();
				  }
	 			}
			}
 		}
 		
 		function accionImprimirApaisado(){
			document.solicitudCompraForm.modo.value = "imprimirCompra";
		 	ventaModalGeneralScrollAuto("solicitudCompraForm","G");			
		 	//document.solicitudCompraForm.submit();
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
						
					// Recupera el identificador de la serie de facturacion
					var idSerieFacturacion = json.idSerieFacturacion;		
					
					if (idSerieFacturacion==null || idSerieFacturacion=='') {
						jQuery("#selectSeleccionSerieFacturacion")[0].innerHTML = json.aOptionsSeriesFacturacion[0];
						
						jQuery("#divSeleccionSerieFacturacion").dialog(
							{
								height: 150,
								width: 500,
								modal: true,
								resizable: false,
								buttons: {
									"<%=sDialogBotonGuardarCerrar%>": function() {
										idSerieFacturacion = jQuery("#selectSeleccionSerieFacturacion").val();
										if (idSerieFacturacion==null || idSerieFacturacion=='') {
											alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
											
										} else {
											jQuery(this).dialog("close");
											document.solicitudCompraForm.target = "submitArea";
											document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
											document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;											
											document.solicitudCompraForm.submit();	
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
						document.solicitudCompraForm.target = "submitArea";
						document.solicitudCompraForm.modo.value = "facturacionRapidaCompra";
						document.solicitudCompraForm.tablaDatosDinamicosD.value = idInstitucion.value + ',' + idPeticion.value + ',' + idSerieFacturacion;							
						document.solicitudCompraForm.submit();							
					}							
					
					fin();							
				},
				
				error: function(e){
					alert("<%=sDialogError%>");
					fin();
				}
			});			    
 		}
 		
 		function anticiparImporte (fila) {
 			var aux = 'oculto' + fila + '_idTipoClave'; 
 	 		var idTipoClave = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idArticulo'; 
 	 		var idClave = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idArticuloInstitucion'; 
 	 		var idClaveInstitucion = document.getElementById(aux).value;

 	 		var aux = 'oculto' + fila + '_idPeticion'; 
 	 		var idPeticion = document.getElementById(aux).value;

 	 		var tipo = document.getElementById('oculto' + fila + '_tipo').value;
	
 	 		//actualizar los datos del formulario de mostrar anticipos
 	 		document.getElementById("MostrarAnticiposForm").idTipoClave.value = idTipoClave;
 	 		document.getElementById("MostrarAnticiposForm").idClave.value = idClave;
 	 		document.getElementById("MostrarAnticiposForm").idClaveInstitucion.value = idClaveInstitucion;
 	 		document.getElementById("MostrarAnticiposForm").idPeticion.value = idPeticion;
 	 		document.getElementById("MostrarAnticiposForm").tipo.value = tipo;

 	 		var resultado = ventaModalGeneral("MostrarAnticiposForm","P");		
 	 		if (resultado == "MODIFICADO"){
 	 			refrescarLocal();
 	 		}
 	   	}
 		
 		function refrescarLocal(){
			document.solicitudCompraForm.modo.value = "mostrarCompra";
		 	document.solicitudCompraForm.target="mainWorkArea";			
		 	document.solicitudCompraForm.submit();
 		}
 		
 		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="resultado();">
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
		</table>			
	</div>

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

		<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="pys.solicitudCompra.titulo2"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
				    <%if(!numero.equalsIgnoreCase("")){%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} %>			
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		
		<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="_blank" type=""> 
			<input type="hidden" name="modo" value=""> 
			<input type="hidden" name="actionModal" value=""> 			
		
			<siga:ConjCampos leyenda="pys.solicitudCompra.literal.datosSolicitud">	
				<table class="tablaCampos" align="center">	
					<!-- FILA -->
					<tr>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.idPeticion"/></td>				
						<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(idPeticion)%></td>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.fechaSolicitud"/></td>				
						<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(fecha)%></td>	
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.tipoSolicitud"/></td>				
						<td class="labelTextValue"><siga:Idioma key="pys.tipoPeticion.alta"/></td>
					</tr>
					<!-- FILA -->
					<tr>
						<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nombreSolicitante"/></td>				
						<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>	
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
				columnSizes="20,15,17,8,8,6,10,11,5"
				fixedHeight="83%">
		
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
							descripcion = UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(hash, "DESCRIPCION_ARTICULO"));
							cantidad = UtilidadesHash.getInteger(hash, PysProductosSolicitadosBean.C_CANTIDAD).intValue();
							idFormaPago = UtilidadesHash.getString(hash, PysProductosSolicitadosBean.C_IDFORMAPAGO);
							
						} else  {
							precio = UtilidadesHash.getDouble(hash, "PRECIOSSERVICIOS").doubleValue();
							idInstitucion = UtilidadesHash.getString(hash, PysServiciosSolicitadosBean.C_IDINSTITUCION);
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
		
						//recupera el flag para mostrar/ocultar el bot�n de anticipar y el importe anticipado
						boolean anticipar = UtilidadesHash.getBoolean(hash, "ANTICIPAR").booleanValue();
						Double aux = UtilidadesHash.getDouble(hash, "IMPORTEANTICIPADO");
						double importeAnticipado = aux != null ? aux.doubleValue() : 0.0;
						FilaExtElement[] elementos=new FilaExtElement[1];
						if (anticipar) {
							elementos[0]=new FilaExtElement("anticiparImporte", "anticiparImporte", SIGAConstants.ACCESS_FULL);
						} else {
							elementos = null;
						}
%>
						<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" clase="listaNonEdit" elementos='<%=elementos%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' pintarEspacio='no'>							
							<td>
								<input type="hidden" name="oculto<%=i+1%>_idArticulo" value="<%=idArticulo%>">
							    <input type="hidden" name="oculto<%=i+1%>_idArticuloInstitucion" value="<%=idArticuloInstitucion%>">
							    <input type="hidden" name="oculto<%=i+1%>_idPeticion" value="<%=idPeticionArticulo%>">
							    <input type="hidden" name="oculto<%=i+1%>_idTipoClave" value="<%=idTipoClave%>">
							    <input type="hidden" name="oculto<%=i+1%>_tipo" value="<%=letraClase%>">
							    <input type="hidden" name="oculto<%=i+1%>_idInstitucion" value="<%=idInstitucion%>">
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
			 	} else {
%>
					<tr class="notFound">
			  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>			
<%
			 	}
%>
			</siga:Table>
		
			<div id="camposRegistro1" style="position:absolute; width:100%; height:70px; z-index:2; bottom:70px; left:0px" align="center">

<%
				varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
				varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2);
%>

				<div id="camposRegistro2" style="width:240px" align="center">	
					<fieldset>		
						<table>
							<tr>
								<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>
								<td class="labelTextValue">	<input type='text' name='ivaTotal' value="<%=UtilidadesString.formatoImporte(varIvaTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20"></td>
							</tr>
							<tr>
								<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
								<td class="labelTextValue"><input type='text' name='precioTotal' value="<%=UtilidadesString.formatoImporte(varPrecioTotal)%>&nbsp;&euro;" class="boxConsultaNumber" readOnly=true size="20"></td>
							</tr>
						</table>
					</fieldset>
				</div>
			
				<table width="100%" align="center">
					<tr>
						<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.servicios.precioServicios"/></td>
					</tr>
					<!-- mhg - Mostramos el siguiente mensaje si tenemos productos no facturables -->
					<%if (noFact){ %>
					<tr>
						<td class="labelTextCentro" colspan="2" align="center"><siga:Idioma key="messages.pys.solicitudCompra.compraNoFacturable"/></td>	
					</tr>
					<%}%>
				</table>
			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			</div>
		</html:form>
	
		<!-- Este formulario se emplea para abrir la ventana modal de modificaci�n de los anticipos-->
		<html:form styleId="MostrarAnticiposForm" action="/PYS_GestionarSolicitudes.do" method="POST" target="submitArea" style="display:none">
		    <input type="hidden" name="idTipoClave" value="">
			<input type="hidden" name="idClave" value=""> 
			<input type="hidden" name="idClaveInstitucion" value=""> 
			<input type="hidden" name="idPeticion" value=""> 
			<input type="hidden" name="idPersona" value="<%=idPersona%>"> 
			<input type="hidden" name="tipo" value=""> 
			<input type="hidden" name="modo" value="mostrarAnticipos"> 
			<input type="hidden" name="actionModal" value="">
			<html:hidden styleId = "tablaDatosDinamicosD"  property = "tablaDatosDinamicosD" value=""/>
		</html:form>	
		
<% 
		if (usrbean.isLetrado() || cer_nofactura) { 
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

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>