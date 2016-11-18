<!DOCTYPE html>
<html>
<head>
<!-- consultaProductos.jsp -->

<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 07-02-2004 creacion
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.PysCompraBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="com.siga.beans.PysProductosSolicitadosBean"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
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
	String action=app+"/CEN_Facturacion.do?noReset=true&bIncluirRegistrosConBajaLogica="+(String)request.getAttribute("bIncluirRegistrosConBajaLogica");
	String modo = (String)ses.getAttribute("MODO");
%>

	<!-- HEAD -->
	<script language="JavaScript">
		function cambiarFechaEfectiva(fila) {
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j = 1;
			var flag = true;
			while (flag) {
				var aux = 'oculto' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
			  	if (oculto == null)  { 
				  flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
			  	j++;
			}
			datos.value = datos.value + "%";
									
	    	document.datosFacturacionForm.modo.value = "editarCambiarFecha";
			// Abro ventana modal y refresco si necesario
			var resultado = ventaModalGeneral(document.datosFacturacionForm.name,"P");
			
			if (resultado=='MODIFICADO') {
				//document.location.reload();
				document.datosFacturacionForm.modo.value = "abrirPaginaProducto";
				document.datosFacturacionForm.submit();			
			}
		}
		
		
		function incluirRegBajaLogica(o) {
			if (o.checked) {
				document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "s";
			} else {
				document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.datosFacturacionForm.modo.value = "abrirProductosPaginados";
			document.datosFacturacionForm.submit();
		}
		
		function refrescarLocal() {
			// document.location.reload();
			// window.location = window.location;
			parent.pulsarId("pestana.fichaCliente.datosProductos","mainPestanas");
		}
	</script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.fichaCliente.facturacion.productos.cabecera" localizacion="censo.fichaLetrado.facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<table class="tablaTitulo" align="center" cellspacing="0">

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_Facturacion.do" method="POST">
			<!-- Campo obligatorio -->
			<html:hidden property="modo" styleId="modo" value="" />
			<!-- para saber si productos o servicios -->
			<input type="hidden" name="pos" id="pos" value="P">
			<html:hidden name="datosFacturacionForm" property="idPersona" styleId="idPersona"  value="<%=idPersona%>" />
			<html:hidden name="datosFacturacionForm" property="idInstitucion" styleId="idInstitucion"  value="<%=idInstitucion%>" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="incluirRegistrosConBajaLogica" id="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
			<input type="hidden" name="accion" id="accion" value="<%=modo%>">
		</html:form>
	
		<tr>
			<td class="titulitosDatos">
	
				<siga:Idioma key="cen.consultaProductos.titulo1"/>&nbsp;&nbsp;
				<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
	
	    		<%if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
				<% } else { %>
			   		<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<% } %>
			</td>
		</tr>
	</table>

	<siga:Table 
	   	name="tablaDatos"
	   	border="1"
	   	columnNames="cen.consultaProductos.literal.fecha,
	   				cen.consultaProductos.literal.idPeticion,
	   				cen.consultaProductos.literal.concepto,
	   				cen.consultaProductos.literal.formaPago,
	   				cen.consultaProductos.literal.nCuenta,
	   				cen.consultaProductos.literal.cantidad,
	   				cen.consultaProductos.literal.precio,
	   				cen.consultaProductos.literal.estadoFactura,
	   				cen.consultaProductos.literal.estadoProducto,
	   				pys.solicitarBaja.literal.fechaEfectiva,"
	   	columnSizes="7,6,17,9,18,6,8,7,7,7,8"
	   	modal="P">
			
<%
		if (resultado == null || resultado.size() == 0) {
%>			
	 		<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		} else {
		
			// recorro el resultado
			for (int i = 0; i < resultado.size(); i++) {
				String cont = String.valueOf(i + 1);
				Row fila = (Row)resultado.elementAt(i);
				Hashtable registro = (Hashtable) fila.getRow(); 
				
				String idPeticion = UtilidadesString.mostrarDatoJSP((String) registro.get(PysProductosSolicitadosBean.C_IDPETICION));
				String idProducto = (String) registro.get(PysProductosSolicitadosBean.C_IDPRODUCTO);
				String idProductoInstitucion = (String) registro.get(PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION);
				String idTipoProducto = (String) registro.get(PysProductosSolicitadosBean.C_IDTIPOPRODUCTO);	
				String cantidad = UtilidadesString.mostrarDatoJSP((String) registro.get(PysProductosSolicitadosBean.C_CANTIDAD));
				String precio = UtilidadesString.mostrarDatoJSP((String) registro.get(PysProductosSolicitadosBean.C_VALOR));
				String aceptado = UtilidadesString.mostrarDatoJSP((String) registro.get(PysProductosSolicitadosBean.C_ACEPTADO)); 
				String nofacturable = (String) registro.get(PysProductosSolicitadosBean.C_NOFACTURABLE);				
				String fecha = (String) registro.get(PysPeticionCompraSuscripcionBean.C_FECHA);			
				
				String concepto = UtilidadesString.mostrarDatoJSP((String) registro.get("CONCEPTO"));				
				String iva = ((String) registro.get("VALORIVA"))==null ? "0" : (String) registro.get("VALORIVA");				
				String idFormaPago = (String) registro.get(PysProductosSolicitadosBean.C_IDFORMAPAGO);				
				String identifCuenta = (String) registro.get(PysProductosSolicitadosBean.C_IDCUENTA);			
				String formaPago = UtilidadesString.mostrarDatoJSP((String) registro.get("FORMAPAGO"));						
				String idCuenta = (String) registro.get("NCUENTA");
				
				String fechaEfectiva = (String) registro.get("FECHAEFEC");				
				String estadoCompra = UtilidadesString.mostrarDatoJSP((String) registro.get("ESTADOCOMPRA"));
				String descripcionEstadoCompra = UtilidadesString.mostrarDatoJSP((String) registro.get("DESCRIPCIONESTADOCOMPRA"));
				String descripcionEstadoProducto = UtilidadesString.mostrarDatoJSP((String) registro.get("DESCRIPCIONESTADOPRODUCTO"));
				
				if (identifCuenta == null || identifCuenta.equals("")) {
					identifCuenta = "0";
				}				
				
				if (fecha == null || fecha.equals("")) {
					fecha = "&nbsp;";
				} else {
					fecha = GstDate.getFormatedDateShort(usr.getLanguage(), fecha);
				}
				
				if (idCuenta == null || idCuenta.equals("")) {
					idCuenta = "&nbsp;";
				} else if (!idCuenta.equals("-")) {					
					idCuenta = UtilidadesString.mostrarIBANConAsteriscos(idCuenta);
				}
				
				if (fechaEfectiva == null || fechaEfectiva.equals("")) {
					fechaEfectiva = "&nbsp;";
				} else {
					fechaEfectiva = GstDate.getFormatedDateShort(usr.getLanguage(), fechaEfectiva);
				}				
									
				//Calculo el precio con iva y lo redondeo:
				double precioDouble = 0;
				if (precio!=null && !precio.trim().equals("") && iva!=null && !iva.trim().equals("")) {
					precioDouble = Double.parseDouble(precio) + (Double.parseDouble(precio) * Double.parseDouble(iva)) / 100;
					precioDouble = UtilidadesNumero.redondea(precioDouble, 2);
				}
				String precioConIVA = String.valueOf(precioDouble);

				FilaExtElement[] elems = new FilaExtElement[1];
				String botones = "";
				if (nofacturable.equals("1") || idFormaPago == null || idFormaPago == "") {
					botones = "";
					idFormaPago = " ";
					formaPago = UtilidadesString.getMensajeIdioma(usr, "pys.estadoPago.noFacturable");  
					descripcionEstadoCompra = UtilidadesString.getMensajeIdioma(usr, "pys.estadoPago.noFacturable");
					
				} else {
					if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado.equals(ClsConstants.PRODUCTO_BAJA)) && modo.equals("editar") && !usr.isLetrado()) {							
						if (estadoCompra.equals("estados.compra.pendiente")) {							
							elems[0] = new FilaExtElement("cambiarFechaEfectiva", "cambiarFechaEfectiva", SIGAConstants.ACCESS_READ);	
							botones = "E";						
						}
					}
				}				
%>	 
			
				<siga:FilaConIconos fila="<%=cont %>" botones="<%=botones%>" modo="<%=modo%>" elementos="<%=elems%>" visibleBorrado="no" visibleConsulta="no" pintarEspacio="no" clase="listaNonEdit">
					<td>
						<!-- campos hidden -->
						<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=idInstitucion%>"/>
						<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idTipoProducto%>"/>
						<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idProducto%>"/>
						<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=idProductoInstitucion%>"/>
						<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=idPersona%>"/>
						<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=idFormaPago%>"/>
						<input type="hidden" id="oculto<%=cont %>_7" name="oculto<%=cont %>_7" value="<%=identifCuenta%>"/>
						<input type="hidden" id="oculto<%=cont %>_8" name="oculto<%=cont %>_8" value="<%=idPeticion%>"/>
						<input type="hidden" id="oculto<%=cont %>_9" name="oculto<%=cont %>_9" value="<%=fechaEfectiva%>"/>
						<%=fecha%>
					</td>

					<td><%=idPeticion%></td>

					<td><%=concepto%></td>

					<td><%=formaPago%></td>
				
					<td><%=idCuenta%></td>
				
					<td align="right"><%=cantidad%></td>
					
					<td align="right"><%=UtilidadesString.formatoImporte(precioConIVA)%>&nbsp;&euro;</td>
					
					<td><%=descripcionEstadoCompra%></td>
					
					<td><%=descripcionEstadoProducto%></td>
					
					<td><%=fechaEfectiva%></td>
				</siga:FilaConIconos>		
<%
			} // del for
		} // del if
%>			

	</siga:Table>
	
<%
	if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						idioma="<%=idioma%>"
						modo="abrirPaginaProducto"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
						distanciaPaginas=""
						action="<%=action%>" />
<%
	}
%>

		<div style="position: absolute; left: 400px; width:140px; bottom: 0px; z-index: 99;">
			<table align="center" border="0" class="botonesSeguido">
				<tr>
					<td class="botonesDetalle">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal" /> 
						<%if (bIncluirBajaLogica) {%>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked> 
						<%} else {%>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);"> 
						<%}%>
 					</td>
				</tr>
			</table>
		</div>

<%
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
 

	if (!busquedaVolver.equals("volverNo")) { 
		if (!usr.isLetrado()) { 
%>
			<siga:ConjBotonesAccion botones="V,BA" idBoton="2" idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"/>
<%
		} else {
%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<%
		}
	} else {
		if (!usr.isLetrado()) { 
%>
			<siga:ConjBotonesAccion botones="BA"  idBoton="2"  idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"/>
<%
		} else { 
%>
			<siga:ConjBotonesAccion botones="botonesDetalle" />
<%
		}
	}
%>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>