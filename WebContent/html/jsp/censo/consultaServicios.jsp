<!DOCTYPE html>
<html>
<head>
<!-- consultaServicios.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
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
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

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
	
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}

//
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>
<%@page import="com.atos.utils.Row"%>

<!-- HEAD -->


		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
	function solicitar(fila) {
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
    	document.datosFacturacionForm.modo.value = "abrirSolicitud";

		// Abro ventana modal y refresco si necesario
		var resultado = ventaModalGeneral(document.datosFacturacionForm.name,"P");
		if (resultado=='MODIFICADO') {
			document.location.reload();		
		}		
	}
	
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
			document.datosFacturacionForm.modo.value = "abrirPaginaServicio";
			document.datosFacturacionForm.submit();
		}		
	}
	
	function incluirRegBajaLogica(o) {
		if (o.checked) {
			document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "s";
		} else {
			document.datosFacturacionForm.incluirRegistrosConBajaLogica.value = "n";
		}
		document.datosFacturacionForm.modo.value = "abrirServiciosPaginados";		
		document.datosFacturacionForm.submit();
	}
	</script>
	<script>	
		function refrescarLocal() {			
			parent.pulsarId("pestana.fichaCliente.datosServicios","mainPestanas");
		}
	</script>	

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
	   
		 <siga:Titulo 
			titulo="censo.fichaCliente.facturacion.servicios.cabecera"
			localizacion="censo.fichaLetrado.facturacion.localizacion"/>
		
		<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body class="tablaCentralCampos">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<!-- Esto es muy util para el caso de ventanas modales, ya que no
	     vamos a disponer en estos casos de la cabecera de la pagina.
	     Nos serviria para tener conciencia de donde estamos ya que desde una
	     ventana modal no se puede actualizar la barra de titulo y ademas queda detras -->

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

	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->


		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_Facturacion.do" method="POST">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" styleId = "modo" value = "" />

			<!-- para saber si productos o servicios -->
			<html:hidden property = "tipoSolicitud" styleId = "tipoSolicitud"  value = "S" />

			<html:hidden name="datosFacturacionForm" property = "idPersona"  styleId = "idPersona"  value = "<%=idPersona %>" />
			<html:hidden name="datosFacturacionForm" property = "idInstitucion"  styleId = "idInstitucion"  value = "<%=idInstitucion %>" />
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="pos" id="pos" value="S"/>
			<input type="hidden" name="incluirRegistrosConBajaLogica" id="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>"/>
			<input type="hidden" name="accion" id="accion" value="<%=modo%>"/>
		</html:form>


<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="7,6,24,9,6,10,7,7,7,7,10";
		nombresCol="cen.consultaProductos.literal.fecha,cen.consultaProductos.literal.idPeticion,cen.consultaProductos.literal.concepto,cen.consultaProductos.literal.formaPago,cen.consultaProductos.literal.cantidad,cen.consultaProductos.literal.precio,cen.consultaProductos.literal.estadoFactura,cen.consultaProductos.literal.estadoProducto,gratuita.listarTurnos.literal.fechaAlta,censo.consultaDatos.literal.fechaBaja,";
%>

		<siga:Table 	 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol %>"
		   columnSizes="<%=tamanosCol %>"
		   modal="P">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%	
	} else { 


		// recorro el resultado
		for (int i=0;i<resultado.size();i++) 
		{

			FilaExtElement[] elems=new FilaExtElement[2];
			FilaExtElement[] elems2=new FilaExtElement[1];
			elems[0]=new FilaExtElement("solicitar","solicitar",SIGAConstants.ACCESS_READ);  	
			Row fila = (Row)resultado.elementAt(i);
			Hashtable registro = (Hashtable) fila.getRow();
			
			
			String cont = new Integer(i+1).toString();

			// calculo de campos
			String fecha = (String) registro.get("FECHA");
			if (fecha==null && fecha.equals("")) {
				fecha = "&nbsp;";
			} else {
				// formateo
				fecha = GstDate.getFormatedDateShort(usr.getLanguage(),fecha);
			}
			String idPeticion = UtilidadesString.mostrarDatoJSP((String) registro.get("IDPETICION"));
			String idServicio = UtilidadesString.mostrarDatoJSP((String) registro.get("IDSERVICIO"));
			String idServiciosInstitucion = UtilidadesString.mostrarDatoJSP((String) registro.get("IDSERVICIOSINSTITUCION"));
			String idTipoServicios = UtilidadesString.mostrarDatoJSP((String) registro.get("IDTIPOSERVICIOS"));
			String concepto = UtilidadesString.mostrarDatoJSP((String) registro.get("CONCEPTO"));
			String formaPago = UtilidadesString.mostrarDatoJSP((String) registro.get("FORMAPAGO"));
			String idFormaPago = UtilidadesString.mostrarDatoJSP((String) registro.get("IDFORMAPAGO"));
			String identificadorCuenta = (((String) registro.get("IDCUENTA"))==null || ((String) registro.get("IDCUENTA")).equals(""))?"0":(String) registro.get("IDCUENTA");
			String idCuenta = (String) registro.get("NCUENTA");
			
			if (idCuenta == null || idCuenta.equals("") ) {
				idCuenta = "&nbsp";
			} else if (!idCuenta.equals("-")) {
				idCuenta = UtilidadesString.mostrarIBANConAsteriscos(idCuenta);
			}	

			String cantidad = UtilidadesString.mostrarDatoJSP((String) registro.get("CANTIDAD"));
			String precio = (String) registro.get("VALOR");
			//String iva = ((String) registro.get("PORCENTAJEIVA"))==null?"0":(String) registro.get("PORCENTAJEIVA");
			String iva = ((String) registro.get("VALORIVA"))==null?"0":(String) registro.get("VALORIVA");
			String estadoPago = UtilidadesString.mostrarDatoJSP((String) registro.get("ESTADOPAGO"));
			/*estadoPago = UtilidadesProductosServicios.getEstadoPago(estadoPago);*/
			estadoPago = UtilidadesString.getMensajeIdioma(usr, estadoPago);
			String tipoPeticion = UtilidadesString.mostrarDatoJSP((String) registro.get("TIPOPETICION"));
			String aceptado = UtilidadesString.mostrarDatoJSP((String) registro.get("ACEPTADO"));
			String estadoServicio = UtilidadesString.getMensajeIdioma(usr, UtilidadesProductosServicios.getEstadoProductoServicio(aceptado));
			String periodicidad = UtilidadesString.mostrarDatoJSP((String) registro.get("SERVICIO_DESCRIPCION_PERIODICIDAD"));
			// anticipos
			
			boolean bAnticipado= registro.get("SERVICIO_ANTICIPADO")!=null && ((Boolean) registro.get("SERVICIO_ANTICIPADO")).booleanValue();
			boolean boton = false;
			String botones = "";

			//Calculo el precio con iva y lo redondeo:
			double precioDouble = 0;
			String precioConIVA = "0";
			if ( (precio!=null && !precio.trim().equals("")) && (iva!=null && !iva.trim().equals(""))) {
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

// RGG			if ((tipoPeticion.equals(ClsConstants.PRODUCTO_ACEPTADO) || tipoPeticion.equals(ClsConstants.PRODUCTO_PENDIENTE)) && usrbean.isLetrado()) {
// RGG 25-02-2005			if ((tipoPeticion.equals(ClsConstants.PRODUCTO_ACEPTADO) || tipoPeticion.equals(ClsConstants.PRODUCTO_PENDIENTE)) && idPersona.equals(new Long(usrbean.getIdPersona()).toString())) {
			if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado.equals(ClsConstants.PRODUCTO_PENDIENTE))) {
				botones = "E";
			} else {
				if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado.equals(ClsConstants.PRODUCTO_PENDIENTE)) && idFormaPago.equals(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA).toString())) {
					boton = true;
				}

			}
			
			String idTipoProducto = (String) registro.get("IDTIPOPRODUCTO");
			String idProducto = (String) registro.get("IDPRODUCTO");
			String idProductoInstitucion = (String) registro.get("IDPRODUCTOINSTITUCION");
			String estaFacturado = (String) registro.get("ESTAFACTURADO");
            
			if (((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) && !estaFacturado.equals("1")) || 
			(aceptado.equals(ClsConstants.PRODUCTO_BAJA) &&  !estaFacturado.equals("1"))) && 
			modo.equals("editar") && !usr.isLetrado()) 
			{
				elems[1]=new FilaExtElement("cambiarFechaEfectiva","cambiarFechaEfectiva",SIGAConstants.ACCESS_READ);
				elems2[0]=new FilaExtElement("cambiarFechaEfectiva","cambiarFechaEfectiva",SIGAConstants.ACCESS_READ);
			}
						
			//Fecha Efectiva:
			String fechaEfectiva = (String) registro.get("FECHAEFEC");
			if (fechaEfectiva==null || fechaEfectiva.equals("")) {
				fechaEfectiva = "&nbsp;";
			} else {
				// formateo
				fechaEfectiva = GstDate.getFormatedDateShort(usr.getLanguage(),fechaEfectiva);
			}
			
			//Fecha Baja
			String fechaBaja = (String) registro.get("FECHABAJA");
			if (fechaBaja==null || fechaBaja.equals("")) {
				fechaBaja = "&nbsp;";
			} else {
				// formateo
				fechaBaja = GstDate.getFormatedDateShort(usr.getLanguage(),fechaBaja);
			}
			
			//Si el modo de pago es domiciliación bancaria se muestra también el botón de consulta
			if(Integer.parseInt(idFormaPago)==20)
				botones = "C, " + botones;	
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

<% if (boton) { %>

			<siga:FilaConIconos fila="<%=cont %>"  botones="<%=botones %>" modo="<%=modo %>" elementos="<%=elems%>"  visibleEdicion="no" visibleBorrado="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="false" >

				

				<td>
					<!-- campos hidden  -->
				<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=identificadorCuenta %>">
				<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
				<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idTipoServicios %>">
				<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=idServicio %>">
				<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=idServiciosInstitucion %>">
				<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=idPeticion %>">
				<input type="hidden" id="oculto<%=cont %>_7" name="oculto<%=cont %>_7" value="<%=idPersona %>">
				<input type="hidden" id="oculto<%=cont %>_8" name="oculto<%=cont %>_8" value="<%=idFormaPago %>">
				<input type="hidden" id="oculto<%=cont %>_9" name="oculto<%=cont %>_9" value="<%=identificadorCuenta %>">
				<input type="hidden" id="oculto<%=cont %>_10" name="oculto<%=cont %>_10" value="<%=idPeticion %>"/>

				<input type="hidden" id="oculto<%=cont %>_11" name="oculto<%=cont %>_11" value="<%=precio %>">
				<input type="hidden" id="oculto<%=cont %>_12" name="oculto<%=cont %>_12" value="<%=iva %>"/>
				<input type="hidden" id="oculto<%=cont %>_13" name="oculto<%=cont %>_13" value="<%=fechaEfectiva %>"/>
				<input type="hidden" id="oculto<%=cont %>_14" name="oculto<%=cont %>_14" value="<%=fechaBaja %>"/>
					<%=fecha %>
				</td>
				<td>
					<%=idPeticion %>
				</td>
				<td>
					<%=concepto %>
				</td>
				<td>
					<%=formaPago %>
					<% if (bAnticipado) { %>
						<b><siga:Idioma key="censo.servicios.literal.marcaAnticipado"/></b>
					<% } %>
					
				</td>
				<td align="right">
					<%=cantidad %>
				</td>
				<td>
					<%=salidaPrecio %>
				</td>
				<td>
					<%=estadoPago %>
				</td>
				<td>
					<%=estadoServicio %>
				</td>
				<td>
					<%=fechaEfectiva%>
				</td>
				<td>
					<%=fechaBaja%>
				</td>
			</siga:FilaConIconos>		

<%  } else {   %>			
			<siga:FilaConIconos fila="<%=cont %>" botones="<%=botones %>" elementos="<%=elems2%>" modo="<%=modo %>" visibleBorrado="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="false">
			
				<td>
					<!-- campos hidden  -->
					<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=identificadorCuenta %>">
					<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
					<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idTipoServicios %>">
					<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=idServicio %>">
					<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=idServiciosInstitucion %>">
					<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=idPeticion %>">
					<input type="hidden" id="oculto<%=cont %>_7" name="oculto<%=cont %>_7" value="<%=idPersona %>">
					<input type="hidden" id="oculto<%=cont %>_8" name="oculto<%=cont %>_8" value="<%=idFormaPago %>">
					<input type="hidden" id="oculto<%=cont %>_9" name="oculto<%=cont %>_9" value="<%=identificadorCuenta %>">
					<input type="hidden" id="oculto<%=cont %>_10" name="oculto<%=cont %>_10" value="<%=idPeticion %>"/>
	
					<input type="hidden" id="oculto<%=cont %>_11" name="oculto<%=cont %>_11" value="<%=precio %>">
					<input type="hidden" id="oculto<%=cont %>_12" name="oculto<%=cont %>_12" value="<%=iva %>"/>
					<input type="hidden" id="oculto<%=cont %>_13" name="oculto<%=cont %>_13" value="<%=fechaEfectiva %>"/>
					<input type="hidden" id="oculto<%=cont %>_14" name="oculto<%=cont %>_14" value="<%=fechaBaja %>"/>
					<%=fecha %>
				</td>
				<td>
					<%=idPeticion %>
				</td>
				<td>
					<%=concepto %>
				</td>
				<td>
					<%=formaPago %> 
					<% if (bAnticipado) { %>
						<b><siga:Idioma key="censo.servicios.literal.marcaAnticipado"/></b>
					<% } %>
				</td>
				<td align="right">
					<%=cantidad %>
				</td>
				<td>
					<%=salidaPrecio %>
				</td>
				<td>
					<%=estadoPago %>
				</td>
				<td>
					<%=estadoServicio %>
				</td>
				<td>
					<%=fechaEfectiva%>
				</td>
				<td>
					<%=fechaBaja%>
				</td>
			</siga:FilaConIconos>		

<%  } %>			


			<!-- FIN REGISTRO -->
<%		} // del for %>			


<%	} // del if %>
			

		</siga:Table>
		
		<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  
	  						
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="abrirPaginaServicio"								
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
	
	
		<div style="position:absolute; left:200px;bottom:5px;z-index:99;">
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
					<td class="labelText">&nbsp;&nbsp;
						<siga:Idioma key="censo.servicios.literal.mensajeAnticipado"/>
					</td>
				</tr>
			</table>
		</div>
	

<% if (!busquedaVolver.equals("volverNo")) { %>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>
<% } else {%>
		<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>
<% } %>
	<!-- FIN: BOTONES REGISTRO -->

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
