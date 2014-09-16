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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesProductosServicios"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.PysSuscripcionBean"%>
<%@ page import="com.siga.beans.PysPeticionCompraSuscripcionBean"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP -->
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
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
		function solicitar(fila) {
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
			datos.value = datos.value + "%"
	    	document.datosFacturacionForm.modo.value = "abrirSolicitud";
	
			// Abro ventana modal y refresco si necesario
			var resultado = ventaModalGeneral(document.datosFacturacionForm.name,"P");
			if (resultado=='MODIFICADO') {
				document.location.reload();		
			}		
		}
	
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
		
		function refrescarLocal() {			
			parent.pulsarId("pestana.fichaCliente.datosServicios","mainPestanas");
		}
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.fichaCliente.facturacion.servicios.cabecera" localizacion="censo.fichaLetrado.facturacion.localizacion"/>		
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
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

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
		   				gratuita.listarTurnos.literal.fechaAlta,
		   				censo.consultaDatos.literal.fechaBaja,"
		columnSizes="8,6,22,9,5,10,7,7,8,8,10"
		modal="P">

		<!-- INICIO: ZONA DE REGISTROS -->
		<!-- Aqui se iteran los diferentes registros de la lista -->
			
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

				String idPeticion = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDPETICION));
				// PysSuscripcionBean.C_IDINSTITUCION
				String idServicio = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDSERVICIO));
				String idServiciosInstitucion = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDSERVICIOSINSTITUCION));
				String idTipoServicios = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDTIPOSERVICIOS));
				String identificadorCuenta = (String) registro.get(PysSuscripcionBean.C_IDCUENTA);				
				String cantidad = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_CANTIDAD));
				String idFormaPago = UtilidadesString.mostrarDatoJSP((String) registro.get(PysSuscripcionBean.C_IDFORMAPAGO));
				String fechaEfectiva = (String) registro.get(PysSuscripcionBean.C_FECHASUSCRIPCION);
				String fechaBaja = (String) registro.get(PysSuscripcionBean.C_FECHABAJA);
				// PysPeticionCompraSuscripcionBean.C_TIPOPETICION
				String fecha = (String) registro.get(PysPeticionCompraSuscripcionBean.C_FECHA);
				String aceptado = UtilidadesString.mostrarDatoJSP((String) registro.get("ACEPTADO"));
				String formaPago = UtilidadesString.mostrarDatoJSP((String) registro.get("FORMAPAGO"));
				String concepto = UtilidadesString.mostrarDatoJSP((String) registro.get("CONCEPTO"));				
				// SOLICITARBAJA
				String estaFacturado = (String) registro.get("ESTAFACTURADO");
				// NCUENTA
				
				if (identificadorCuenta==null || identificadorCuenta.equals(""))
					identificadorCuenta = "0";
				
				if (fechaEfectiva==null || fechaEfectiva.equals("")) {
					fechaEfectiva = "&nbsp;";
				} else {
					fechaEfectiva = GstDate.getFormatedDateShort(usr.getLanguage(), fechaEfectiva);
				}		
				
				if (fechaBaja==null || fechaBaja.equals("")) {
					fechaBaja = "&nbsp;";
				} else {
					fechaBaja = GstDate.getFormatedDateShort(usr.getLanguage(),fechaBaja);
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

				FilaExtElement[] elems=new FilaExtElement[1];
				String botones = "";
				if ((aceptado.equals(ClsConstants.PRODUCTO_ACEPTADO) || aceptado.equals(ClsConstants.PRODUCTO_BAJA)) && modo.equals("editar") && !usr.isLetrado()) {
					botones = "E";
					if (!estaFacturado.equals("1")) {
						elems[0]=new FilaExtElement("cambiarFechaEfectiva","cambiarFechaEfectiva",SIGAConstants.ACCESS_READ);
					}
				}
			
				//Si el modo de pago es domiciliación bancaria se muestra también el botón de consulta
				if (Integer.parseInt(idFormaPago)==20) {
					botones = "C, " + botones;
				}
%>
				<!-- REGISTRO  -->
				<!-- Esto es un ejemplo de dos columnas de datos, lo que significa que la lista contiene realmente 3 columnas: Las de datos mas la de botones de acción sobre los registos  -->
	
				<siga:FilaConIconos fila="<%=cont%>" botones="<%=botones%>" elementos="<%=elems%>" modo="<%=modo%>" visibleBorrado="no" visibleConsulta="no" clase="listaNonEdit" pintarEspacio="false">
			
					<td>
						<!-- campos hidden  -->
						<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=identificadorCuenta%>">
						<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idInstitucion%>">
						<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idTipoServicios%>">
						<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=idServicio%>">
						<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=idServiciosInstitucion%>">
						<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=idPeticion%>">
						<input type="hidden" id="oculto<%=cont %>_7" name="oculto<%=cont %>_7" value="<%=idPersona%>">
						<input type="hidden" id="oculto<%=cont %>_8" name="oculto<%=cont %>_8" value="<%=idFormaPago%>">
						<input type="hidden" id="oculto<%=cont %>_13" name="oculto<%=cont %>_9" value="<%=fechaEfectiva%>"/>
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
					<td><%=fechaEfectiva%></td>
					<td><%=fechaBaja%></td>
				</siga:FilaConIconos>				

				<!-- FIN REGISTRO -->
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
