<!DOCTYPE html>
<html>
<head>
<!-- consultaServicios.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.beans.PysServiciosInstitucionBean"%>
<%@ page import="com.siga.beans.PysSuscripcionBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
	//Vector obj = (Vector) request.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String idioma = usr.getLanguage().toUpperCase();
	String idPersona = (String) ses.getAttribute("IDPERSONA");
	String idInstitucion = usr.getLocation();
	Hashtable datosColegiado = (Hashtable)ses.getAttribute("DATOSCOLEGIADO");
	String nombrePestanha = "";
	String numeroPestanha = "";
	
	if (datosColegiado != null) {
		nombrePestanha = (String) datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String) datosColegiado.get("NUMEROCOLEGIADO");
	}

	/** PAGINADOR ***/
	Vector resultado = new Vector();
	String paginaSeleccionada = "0";
	String totalRegistros = "0";
	String registrosPorPagina = "0";
	HashMap hm = new HashMap();
	String atributoPaginador = (String) request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador) != null) {
		hm = (HashMap) ses.getAttribute(atributoPaginador);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");
			PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
		}
	}
	
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica"));	
	
	String action = app+"/CEN_Facturacion.do?noReset=true&bIncluirRegistrosConBajaLogica="+(String)request.getAttribute("bIncluirRegistrosConBajaLogica");
	String modo = (String)ses.getAttribute("MODO");
	
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	String sDialogBotonCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.close"));
	String sDialogBotonGuardarCerrar = UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "general.boton.guardarCerrar"));
	String fechaActual = UtilidadesBDAdm.getFechaBD("");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>	

	<siga:Titulo titulo="censo.fichaCliente.facturacion.servicios.cabecera" localizacion="censo.fichaLetrado.facturacion.localizacion"/>		
	
	<style type="text/css">
		.ui-dialog-titlebar-close {
			visibility: hidden;
		}
	</style>	
</head>

<body class="tablaCentralCampos">
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="cen.consultaServicios.titulo1"/>&nbsp;&nbsp;
				<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
	
	    		<%if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
				<% } else { %>
			   		<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<% } %>
			</td>
		</tr>
	</table>
	
	<div id="divFechaAlta" title="<siga:Idioma key='cen.consultaProductos.literal.fechaAlta'/>" style="display:none">
		<table align="left">
			<tr>
				<td class="labelText" style="color:black" nowrap>
					<siga:Idioma key="cen.consultaProductos.literal.fechaAlta"/>&nbsp;(*)
				</td>
				<td><siga:Fecha nombreCampo="fechaAltaDiv" valorInicial="" anchoTextField="12"/></td>
			</tr>		
			</tr>						
		</table>			
	</div>		
	
	<div id="divFechaBaja" title="<siga:Idioma key='censo.consultaDatos.literal.fechaBaja'/>" style="display:none">
		<table align="left">
			<tr>
				<td class="labelText" style="color:black" nowrap>
					<siga:Idioma key="censo.consultaDatos.literal.fechaBaja"/>&nbsp;(*)
				</td>
				<td><siga:Fecha nombreCampo="fechaBajaDiv" valorInicial="" anchoTextField="12"/></td>
			</tr>		
			</tr>						
		</table>			
	</div>		

	<html:form action="/CEN_Facturacion.do" method="POST">
		<html:hidden property = "modo" styleId = "modo" value = "" />
		<!-- para saber si productos o servicios -->
		<html:hidden property = "tipoSolicitud" styleId = "tipoSolicitud"  value = "S" />
		<html:hidden name="datosFacturacionForm" property = "idPersona"  styleId = "idPersona"  value = "<%=idPersona %>" />
		<html:hidden name="datosFacturacionForm" property = "idInstitucion"  styleId = "idInstitucion"  value = "<%=idInstitucion %>" />
		<input type="hidden" name="pos" id="pos" value="S"/>
		<input type="hidden" name="incluirRegistrosConBajaLogica" id="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>"/>
		<input type="hidden" name="accion" id="accion" value="<%=modo%>"/>
		
		<input type="hidden" id="idTipoServicioSel" name="idTipoServicioSel" value="" >
		<input type="hidden" id="idServicioSel" name="idServicioSel" value="" >
		<input type="hidden" id="idServicioInstitucionSel" name="idServicioInstitucionSel" value="" >
		<input type="hidden" id="idPeticionSel" name="idPeticionSel" value="" >
		<input type="hidden" id="fechaEfectiva" name="fechaEfectiva" value="" >
	</html:form>

	<siga:Table 	 
		name="tablaDatos"
		border="1"
		columnNames="cen.consultaProductos.literal.fecha,
		   				cen.consultaProductos.literal.idPeticion,
		   				cen.consultaProductos.literal.concepto,
		   				cen.consultaProductos.literal.formaPago,
		   				cen.consultaProductos.literal.cantidad,
		   				cen.consultaProductos.literal.precio,
		   				cen.consultaProductos.literal.estadoFactura,
		   				cen.consultaProductos.literal.estadoProducto,
		   				cen.consultaProductos.literal.fechaAlta,
		   				censo.consultaDatos.literal.fechaBaja,"
		columnSizes="7,6,22,9,5,10,7,7,7,7,13"
		modal="P">

<%	
		if (resultado==null || resultado.size()==0) { 
%>			
			<tr class="notFound">
				<td class="titulitos">
					<siga:Idioma key="messages.noRecordFound"/>
				</td>
			</tr>
<%	
		} else { 
			for (int i=0; i<resultado.size(); i++) {
				String cont = String.valueOf(i + 1);
				Row fila = (Row)resultado.elementAt(i);
				Hashtable registro = (Hashtable) fila.getRow();
				FilaExtElement[] elems = new FilaExtElement[2];

				String idPeticion = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDPETICION));
				String idServicio = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDSERVICIO));
				String idServiciosInstitucion = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION));
				String idTipoServicios = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDTIPOSERVICIOS));
				String identificadorCuenta = (String) registro.get(PysSuscripcionBean.C_IDCUENTA);				
				String cantidad = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_CANTIDAD));
				String idFormaPago = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDFORMAPAGO));
				String fechaAlta = (String) registro.get(PysSuscripcionBean.C_FECHASUSCRIPCION);
				String fechaBaja = (String) registro.get(PysSuscripcionBean.C_FECHABAJAFACTURACION);
				String fecha = (String) registro.get(PysPeticionCompraSuscripcionBean.C_FECHA);				
				String aceptado = UtilidadesString.mostrarDatoJSP((String) registro.get("ACEPTADO"));
				String formaPago = UtilidadesString.mostrarDatoJSP((String) registro.get("FORMAPAGO"));
				String concepto = UtilidadesString.mostrarDatoJSP((String) registro.get("CONCEPTO"));				
				String UltimaFechaFacturada = (String) registro.get("ULTIMAFECHAFACTURADA");
				
				if (identificadorCuenta==null || identificadorCuenta.equals(""))
					identificadorCuenta = "0";
				
				if (fechaAlta==null || fechaAlta.equals("")) {
					fechaAlta = "&nbsp;";
				} else {
					fechaAlta = GstDate.getFormatedDateShort(usr.getLanguage(), fechaAlta);
				}		
				
				if (fechaBaja==null || fechaBaja.equals("")) {
					fechaBaja = "&nbsp;";
				} else {
					fechaBaja = GstDate.getFormatedDateShort(usr.getLanguage(),fechaBaja);
					
					if (modo.equals("editar") && !usr.isLetrado()) {
						// Solo sale el icono de solicitar baja cuando tiene una fecha de baja
						elems[1]=new FilaExtElement("solicitarbaja", "cambiarFechaBaja", "Cambiar Fecha Baja", SIGAConstants.ACCESS_READ);
					}
				}

				if (fecha==null && fecha.equals("")) {
					fecha = "&nbsp;";
				} else {
					fecha = GstDate.getFormatedDateShort(usr.getLanguage(),fecha);
				}

				boolean bAnticipado= registro.get("SERVICIO_ANTICIPADO")!=null && ((Boolean) registro.get("SERVICIO_ANTICIPADO")).booleanValue();
				String precio = (String) registro.get("VALOR");				
				String iva = ((String) registro.get("VALORIVA"))==null ? "0" : (String) registro.get("VALORIVA");				
				String periodicidad = UtilidadesString.mostrarDatoJSP((String) registro.get("SERVICIO_DESCRIPCION_PERIODICIDAD"));				
				String estadoPago = UtilidadesString.mostrarDatoJSP((String) registro.get("ESTADOPAGO"));
				String estadoServicio = UtilidadesString.mostrarDatoJSP((String) registro.get("ESTADOSERVICIO"));

				//Calculo el precio con iva y lo redondeo:
				double precioDouble = 0;
				String precioConIVA = "0";
				if (precio!=null && !precio.trim().equals("") && iva!=null && !iva.trim().equals("")) {
					precioDouble = Double.parseDouble(precio) + (Double.parseDouble(precio) * Double.parseDouble(iva))  / 100;
					precioDouble = UtilidadesNumero.redondea(precioDouble,2);
				}
			
				//Redondeo:
				precioConIVA = String.valueOf(precioDouble);
				String salidaPrecio = "";
				if (precio==null) {
					salidaPrecio = "-";		
				} else {
					salidaPrecio = UtilidadesNumero.formatoCampo(precioConIVA) + "&nbsp;&euro; / " + periodicidad;	
				}
				
				String botones = "";
				if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado.equals(ClsConstants.PRODUCTO_BAJA)) && modo.equals("editar") && !usr.isLetrado()) {
					botones = "E";
					if (UltimaFechaFacturada==null || UltimaFechaFacturada.equals("")) {
						elems[0]=new FilaExtElement("cambiarFechaEfectiva", "cambiarFechaAlta", "Cambiar Fecha Alta", SIGAConstants.ACCESS_READ);
					}
				}
			
				//Si el modo de pago es domiciliación bancaria se muestra también el botón de consulta
				if (Integer.parseInt(idFormaPago)==20) {
					botones = "C, " + botones;
				}
%>
				<siga:FilaConIconos fila="<%=cont%>" botones="<%=botones%>" elementos="<%=elems%>" modo="<%=modo%>" visibleBorrado="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="false">
			
					<td>
						<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=identificadorCuenta%>">
						<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idInstitucion%>">
						<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idTipoServicios%>">
						<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=idServicio%>">
						<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=idServiciosInstitucion%>">
						<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=idPeticion%>">
						<input type="hidden" id="oculto<%=cont %>_7" name="oculto<%=cont %>_7" value="<%=idPersona%>">
						<input type="hidden" id="oculto<%=cont %>_8" name="oculto<%=cont %>_8" value="<%=idFormaPago%>">
						<input type="hidden" id="oculto<%=cont %>_9" name="oculto<%=cont %>_9" value="<%=fechaAlta%>"/>
						<input type="hidden" id="oculto<%=cont %>_10" name="oculto<%=cont %>_10" value="<%=UltimaFechaFacturada%>"/>
						<input type="hidden" id="oculto<%=cont %>_11" name="oculto<%=cont %>_11" value="<%=fechaBaja%>"/>
						<%=fecha%>
					</td>
					
					<td><%=idPeticion%></td>
					<td><%=concepto%></td>
					<td>
						<%=formaPago%> 
						<% if (bAnticipado) { %>
							<b><siga:Idioma key="censo.servicios.literal.marcaAnticipado"/></b>
						<% } %>
					</td>
					<td align="right"><%=cantidad%></td>
					<td><%=salidaPrecio%></td>
					<td><%=estadoPago%></td>
					<td><%=estadoServicio%></td>
					<td><%=fechaAlta%></td>
					<td><%=fechaBaja%></td>
				</siga:FilaConIconos>				
<%		
			} // del for
		} // del if 
%>			
	</siga:Table>
		
<%
	if (hm.get("datos")!=null && !hm.get("datos").equals("")) { 
%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						idioma="<%=idioma%>"
						modo="abrirPaginaServicio"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
						distanciaPaginas=""
						action="<%=action%>" />
<%
	}
%>
	
	<div style="position:absolute; left:350px;bottom:0px;z-index:99;">
		<table align="center" border="0">
			<tr>
				<td class="botonesSeguido">
					<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
						
					<% if (bIncluirBajaLogica) { %>
						<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
					<% } else { %>
						<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);">
					<% } %>
					<siga:Idioma key="censo.servicios.literal.mensajeAnticipado"/>
				</td>
			</tr>
		</table>
	</div>
	
	<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
	<script language="JavaScript">
	
		function incluirRegBajaLogica(o) {
			if (o.checked) {
				document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "s";
			} else {
				document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.datosFacturacionForm.modo.value = "abrirServiciosPaginados";		
			document.datosFacturacionForm.submit();
		}
		
		function refrescarLocal() {			
			parent.pulsarId("pestana.fichaCliente.datosServicios","mainPestanas");
		}
		
		function cambiarFechaAlta(fila) {
			var fechaAlta = jQuery("#oculto" + fila + "_9").val();			
			if (fechaAlta==null || fechaAlta.length!=10) {
				fechaAlta = "<%=fechaActual%>";
			}
			jQuery("#fechaAltaDiv").val(fechaAlta);			
			
 	 		jQuery("#divFechaAlta").dialog(
				{
					height: 180,
					width: 300,
					modal: true,
					resizable: false,
					buttons: {
						"<%=sDialogBotonGuardarCerrar%>": function() {
							fechaAlta = jQuery("#fechaAltaDiv").val();
							
							if (fechaAlta == "") {
								var mensaje = "<siga:Idioma key='errors.required' arg0='cen.consultaProductos.literal.fechaAlta'/>";
								alert(mensaje);
								return false;							
							}
							
							var idTipoServicioSel = jQuery("#oculto" + fila + "_3").val();	
							var idServicioSel = jQuery("#oculto" + fila + "_4").val();	
							var idServicioInstitucionSel = jQuery("#oculto" + fila + "_5").val();	
							var idPeticionSel = jQuery("#oculto" + fila + "_6").val();
							
							jQuery("#idTipoServicioSel").val(idTipoServicioSel);
							jQuery("#idServicioSel").val(idServicioSel);
							jQuery("#idServicioInstitucionSel").val(idServicioInstitucionSel);
							jQuery("#idPeticionSel").val(idPeticionSel);
							jQuery("#fechaEfectiva").val(fechaAlta);
							
							document.datosFacturacionForm.target = "submitArea";
							document.datosFacturacionForm.modo.value = "grabarCambiarFecha";
							document.datosFacturacionForm.submit();
							
							jQuery(this).dialog("close");
						},
						
						"<%=sDialogBotonCerrar%>": function() {
							jQuery(this).dialog("close");
						}
					}
				}
			);
			jQuery(".ui-widget-overlay").css("opacity","0");				
		}		
		
		function cambiarFechaBaja(fila) {
			var ultimaFechaFacturada = jQuery("#oculto" + fila + "_10").val();
			var fechaBaja = jQuery("#oculto" + fila + "_11").val();			
			if (fechaBaja==null || fechaBaja.length!=10) {
				fechaBaja = "<%=fechaActual%>";
			}
			jQuery("#fechaBajaDiv").val(fechaBaja);			
			
 	 		jQuery("#divFechaBaja").dialog(
				{
					height: 180,
					width: 300,
					modal: true,
					resizable: false,
					buttons: {
						"<%=sDialogBotonGuardarCerrar%>": function() {
							fechaBaja = jQuery("#fechaBajaDiv").val();
							
							if (fechaBaja == "") {
								var mensaje = "<siga:Idioma key='errors.required' arg0='censo.consultaDatos.literal.fechaBaja'/>";
								alert(mensaje);
								return false;							
							}
											
							if (ultimaFechaFacturada!=null && ultimaFechaFacturada!='' && compararFecha(fechaBaja, ultimaFechaFacturada) == 2) {
								var mensaje = '<siga:Idioma key="fecha.error.superiorIgualA"/> ' + ultimaFechaFacturada;
								alert(mensaje);
								return 0;
							}	
							
							var idTipoServicioSel = jQuery("#oculto" + fila + "_3").val();	
							var idServicioSel = jQuery("#oculto" + fila + "_4").val();	
							var idServicioInstitucionSel = jQuery("#oculto" + fila + "_5").val();	
							var idPeticionSel = jQuery("#oculto" + fila + "_6").val();
							
							jQuery("#idTipoServicioSel").val(idTipoServicioSel);
							jQuery("#idServicioSel").val(idServicioSel);
							jQuery("#idServicioInstitucionSel").val(idServicioInstitucionSel);
							jQuery("#idPeticionSel").val(idPeticionSel);
							jQuery("#fechaEfectiva").val(fechaBaja);
							
							document.datosFacturacionForm.target = "submitArea";
							document.datosFacturacionForm.modo.value = "grabarCambiarFecha";
							document.datosFacturacionForm.submit();							
							
							jQuery(this).dialog("close");
						},
						
						"<%=sDialogBotonCerrar%>": function() {
							jQuery(this).dialog("close");
						}
					}
				}
			);
			jQuery(".ui-widget-overlay").css("opacity","0");				
		}
	</script>		
</body>
</html>
