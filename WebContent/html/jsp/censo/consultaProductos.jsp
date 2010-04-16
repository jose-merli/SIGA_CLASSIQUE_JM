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
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>

<!-- JSP -->
<%

	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");
	
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
	Vector resultado = null;
	String paginaSeleccionada = "";

	String totalRegistros = "";

	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	String atributoPaginador = (String) request
			.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador) != null) {
		hm = (HashMap) ses.getAttribute(atributoPaginador);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");

			PaginadorBind paginador = (PaginadorBind) hm
					.get("paginador");
			paginaSeleccionada = String.valueOf(paginador
					.getPaginaActual());

			totalRegistros = String.valueOf(paginador
					.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador
					.getNumeroRegistrosPorPagina());

		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";

		totalRegistros = "0";

		registrosPorPagina = "0";
	}
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getAttribute("bIncluirRegistrosConBajaLogica"));
	String action=app+"/CEN_Facturacion.do?noReset=true&bIncluirRegistrosConBajaLogica="+(String)request.getAttribute("bIncluirRegistrosConBajaLogica");
	String modo = 	(String)ses.getAttribute("MODO");

%>


<%@page import="com.atos.utils.Row"%>
<html>
<!-- HEAD -->
<head>
	
	<script language="JavaScript">
	function cambiarFechaEfectiva(fila) {
		var datos;
		datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = ""; 
		var j;
		var tabla;
		tabla = document.getElementById('tablaDatos');
		var flag = true;
		j = 1;
		while (flag) {
		  var aux = 'oculto' + fila + '_' + j;
		  var oculto = document.getElementById(aux);
		  if (oculto == null)  { flag = false; }
		  else { datos.value = datos.value + oculto.value + ','; }
		  j++;
		}
		datos.value = datos.value + "%"
								
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
	</script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script>	
		function refrescarLocal() {
			// document.location.reload();
			// window.location = window.location;
			parent.pulsarId("pestana.fichaCliente.datosProductos","mainPestanas");
		}
	</script>	

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
		 <siga:Titulo 
			titulo="censo.fichaCliente.facturacion.productos.cabecera"
			localizacion="censo.fichaLetrado.facturacion.localizacion"/>
		
		
		<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos" onLoad="validarAncho_tablaDatos();">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<!-- Esto es muy util para el caso de ventanas modales, ya que no
	     vamos a disponer en estos casos de la cabecera de la pagina.
	     Nos serviria para tener conciencia de donde estamos ya que desde una
	     ventana modal no se puede actualizar la barra de titulo y ademas queda detras -->

		<table class="tablaTitulo" align="center" cellspacing="0">

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_Facturacion.do" method="POST">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "" />
			<!-- para saber si productos o servicios -->
			<input type="hidden" name="pos" value="P">
			<html:hidden name="datosFacturacionForm" property = "idPersona" value = "<%=idPersona %>" />
			<html:hidden name="datosFacturacionForm" property = "idInstitucion" value = "<%=idInstitucion %>" />
			<!-- RGG: cambio a formularios ligeros -->
			<html:hidden name="datosFacturacionForm"  property ="filaSelD"/>
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
			<input type="hidden" name="accion" value="<%=modo%>">
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

	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->



		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

	<!-- NO HAY FORMULARIO -->
	
	
			

<%
	String tamanosCol = "";
	String nombresCol = "";
	
	tamanosCol = "8,9,12,11,17,6,8,8,7,7,10";
	nombresCol = "cen.consultaProductos.literal.fecha,cen.consultaProductos.literal.idPeticion,cen.consultaProductos.literal.concepto,cen.consultaProductos.literal.formaPago,cen.consultaProductos.literal.nCuenta,cen.consultaProductos.literal.cantidad,cen.consultaProductos.literal.precio,cen.consultaProductos.literal.estadoPago,cen.consultaProductos.literal.estadoProducto,pys.solicitarBaja.literal.fechaEfectiva,";
%>


		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol %>"
		   tamanoCol="<%=tamanosCol %>"
		   alto="350"		   
		   ajuste="80"
		   modal="P"
		   activarFilaSel="true"	
		>

		

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%
	if (resultado == null || resultado.size() == 0) {
			%>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
<%
	} else {
		
			// recorro el resultado
			for (int i = 0; i < resultado.size(); i++) {
				FilaExtElement[] elems = new FilaExtElement[2];
				FilaExtElement[] elems2 = new FilaExtElement[1];
				Row fila = (Row)resultado.elementAt(i);
				Hashtable registro = (Hashtable) fila.getRow(); 
				
				
				
				
				String cont = new Integer(i + 1).toString();

				// calculo de campos
				String fecha = (String) registro.get("FECHA");
				if (fecha == null || fecha.equals("")) {
					fecha = "&nbsp;";
				} else {
					// formateo
					fecha = GstDate.getFormatedDateShort(usr
							.getLanguage(), fecha);
				}
				
				String idPeticion = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("IDPETICION"));
				String concepto = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("CONCEPTO"));
				String formaPago = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("FORMAPAGO"));
				String idFormaPago = (String) registro
						.get("IDFORMAPAGO");
				String idCuenta = UtilidadesString
						.mostrarNumeroCuentaConAsteriscos((String) registro
								.get("NCUENTA"));
				if (idCuenta == null || idCuenta.equals("")) {
					idCuenta = "&nbsp";
				}
				String identifCuenta = (String) registro
						.get("IDCUENTA");
				if (identifCuenta == null || identifCuenta.equals("")) {
					identifCuenta = " ";
				}
				String cantidad = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("CANTIDAD"));
				String precio = UtilidadesString
						.mostrarDatoJSP((String) registro.get("VALOR"));
				String iva = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("PORCENTAJEIVA"));

				//Calculo el precio con iva y lo redondeo:
				double precioDouble = 0;
				String precioConIVA = "0";
				if ((precio != null && !precio.trim().equals(""))
						&& (iva != null && !iva.trim().equals(""))) {
					precioDouble = Double.parseDouble(precio)
							+ (Double.parseDouble(precio) * Double
									.parseDouble(iva)) / 100;
					precioDouble = UtilidadesNumero.redondea(
							precioDouble, 2);
				}
				//Redondeo:
				precioConIVA = String.valueOf(precioDouble);

				String estadoPago = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("ESTADOPAGO"));
				String estadoPago2 = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("ESTADOPAGO"));

				/*estadoPago = UtilidadesProductosServicios.getEstadoPago(estadoPago);*/

				estadoPago = UtilidadesString.getMensajeIdioma(usr,
						estadoPago);

				String tipoPeticion = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("TIPOPETICION"));
				String aceptado = UtilidadesString
						.mostrarDatoJSP((String) registro
								.get("ACEPTADO"));
				String estadoProducto = UtilidadesProductosServicios
						.getEstadoProductoServicio(aceptado);
				estadoProducto = UtilidadesString.getMensajeIdioma(
						usr, estadoProducto);
				String botones = "";

				// solo los que no estan aceptados
				//if (aceptado.equals(ClsConstants.PRODUCTO_PENDIENTE) && modo.equals("editar")) {
				
				
					
					if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado
						.equals(ClsConstants.PRODUCTO_PENDIENTE))
						&& !estadoPago2
								.equals("estados.compra.cobrado")
						&& !estadoPago2
								.equals("estados.compra.pendienteFactura")
						&& !estadoPago2
								.equals("estados.compra.facturado")
						&& modo.equals("editar")) {
					botones = "E";
				}

				String idTipoProducto = (String) registro
						.get("IDTIPOPRODUCTO");
				String idProducto = (String) registro.get("IDPRODUCTO");
				String idProductoInstitucion = (String) registro
						.get("IDPRODUCTOINSTITUCION");
				String nofacturable = (String) registro
						.get("NOFACTURABLE");
				
				if (modo.equals("editar") && !usr.isLetrado()
						
						&& estadoProducto.equals("Aceptado")) {
					elems[1] = new FilaExtElement(
							"cambiarFechaEfectiva",
							"cambiarFechaEfectiva",
							SIGAConstants.ACCESS_READ);
					elems2[0] = new FilaExtElement(
							"cambiarFechaEfectiva",
							"cambiarFechaEfectiva",
							SIGAConstants.ACCESS_READ);
				}
				
				//Fecha Efectiva:
				String fechaEfectiva = (String) registro
						.get("FECHAEFEC");
				if (fechaEfectiva == null || fechaEfectiva.equals("")) {
					fechaEfectiva = "&nbsp;";
				} else {
					// formateo
					fechaEfectiva = GstDate.getFormatedDateShort(
							usr.getLanguage(), fechaEfectiva);
				}
				
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
				 
				 
			
			<%
			if (nofacturable.equals("1") || idFormaPago == null
								|| idFormaPago == "") {
							botones = "";
							idFormaPago = " ";
						}
				 				 						%>	 
			
			<siga:FilaConIconos fila="<%=cont %>" botones="<%=botones%>" modo="<%=modo%>" elementos="<%=elems%>" visibleBorrado="no" visibleConsulta="no" pintarEspacio="no" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idInstitucion  %>"/>
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idTipoProducto  %>"/>
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=idProducto  %>"/>
					<input type="hidden" name="oculto<%=cont %>_4" value="<%=idProductoInstitucion  %>"/>
					<input type="hidden" name="oculto<%=cont %>_5" value="<%=idPersona  %>"/>
					<input type="hidden" name="oculto<%=cont %>_6" value="<%=idFormaPago %>"/>
					<input type="hidden" name="oculto<%=cont %>_7" value="<%=identifCuenta %>"/>
					<input type="hidden" name="oculto<%=cont %>_8" value="<%=idPeticion %>"/>
					<input type="hidden" name="oculto<%=cont %>_9" value="<%=fechaEfectiva %>"/>

					<%=fecha%>
				</td>
				<td>
					<%=idPeticion%>
				</td>
				<td>
					<%=concepto%>
				</td>
				<td>
				<%
					if (nofacturable.equals("1") || formaPago == null
											|| formaPago == "") {
				%>
						No Facturable
					<%
					} else {
				%>
						<%=formaPago%>
					<%
						}
					%>
				</td>
				<td>
					<%=idCuenta%>
				</td>
				<td align="right">
					<%=cantidad%>
				</td>
				<td align="right">
					<%=UtilidadesNumero
											.formatoCampo(precioConIVA)
									+ "&euro;"%>
				</td>
				<td>
				<%
					if (nofacturable.equals("1") || formaPago == null
											|| formaPago == "") {
				%>
						No Facturable
					<%
					} else {
				%>
						<%=estadoPago%>
					<%
						}
					%>					
				</td>
				<td>
					<%=estadoProducto%>
				</td>
				<td>
					<%=fechaEfectiva%>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%
	} // del for
%>
			
	
			<!-- FIN: ZONA DE REGISTROS -->

<%
	} // del if
%>			

		</siga:TablaCabecerasFijas>
		<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="abrirPaginaProducto"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
															

	 <%}%>


		<!-- FIN: LISTA DE VALORES -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	
	
		<div style="position:absolute; left:200px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
						
						<% if (bIncluirBajaLogica) { %>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
						<% } else { %>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);">
						<% } %>
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
	<siga:ConjBotonesAccion botones="V,BA" idBoton="1#2" idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"/>
<%
	} else{
%>
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<%
	}
} else {
	if (!usr.isLetrado()) { 
%>
	<siga:ConjBotonesAccion botones="BA"  idBoton="1#2"  idPersonaBA="<%=idPersona %>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle"/>
<%
	} else{
%>
	<siga:ConjBotonesAccion botones="botonesDetalle" />
<%
	}
}
%>
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	<!-- FIN: BOTONES REGISTRO -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>