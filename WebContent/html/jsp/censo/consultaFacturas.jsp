<!-- consultaFacturas.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri =	"struts-tiles.tld" prefix="tiles" %>
<%@taglib uri = "struts-bean.tld" prefix="bean" %>
<%@taglib uri = "struts-html.tld" prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga" %>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.censo.form.FacturasClienteForm"%>
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import="com.siga.beans.FacFacturacionProgramadaBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>

<%
	String app = request.getContextPath(); 
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	HttpSession ses = request.getSession();
	
	Long SidPersona = (Long) request.getAttribute("idPersona");
	if (SidPersona == null){
		SidPersona = (Long) request.getSession().getAttribute("idPersona");
		if (SidPersona == null){
			SidPersona = new Long (request.getParameter("idPersona"));
		}
	}
	
	Integer SidInstitucion = (Integer) request.getAttribute("idInstitucion");
	if(SidInstitucion == null){
		SidInstitucion = (Integer) request.getSession().getAttribute("idInstitucion");
		if(SidInstitucion == null){
			SidInstitucion = new Integer(request.getParameter("idInstitucion"));		
		}	
	}
		
	String nombre = (String) request.getAttribute("nombre");
	if (nombre==null){
		nombre=(String) ses.getAttribute("nombre");
	}	
	
	String numero = (String) request.getAttribute("numero");
	if (numero==null){
		numero=(String) ses.getAttribute("numero");
	}
	
	String accion = (String) request.getAttribute("accion");
	if (accion==null){
		accion=(String) ses.getAttribute("accion");
	}

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
    String sTipo = request.getParameter("tipoCliente");
    String botonesAccion = "";
    
    if (!usr.isLetrado()) { 
		botonesAccion="V";
	}
    
    /** PAGINADOR ***/
	Vector resultado = null;
	String paginaSeleccionada = "";
	String totalRegistros = "";
	String registrosPorPagina = "";
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
	
	boolean bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getSession().getAttribute("bIncluirRegistrosConBajaLogica"));
	String action = app + "/CEN_FacturasCliente.do?noReset=true&bIncluirRegistrosConBajaLogica=" + (String)request.getSession().getAttribute("bIncluirRegistrosConBajaLogica") + 
		"&idInstitucion=" + SidInstitucion.toString() +
		"&idPersona=" + SidPersona.toString() + 
		"&nombre=" + nombre + 
		"&numero=" + numero +
		"&accion="+accion;
	String modo = (String)ses.getAttribute("MODO");
	String idioma = usr.getLanguage().toUpperCase();	
	/** PAGINADOR ***/	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script type="text/javascript">		
		function incluirRegBajaLogica(o) {
			if (o.checked) {
				document.facturasClienteForm.incluirRegistrosConBajaLogica.value = "s";			
			} else {
				document.facturasClienteForm.incluirRegistrosConBajaLogica.value = "n";
			}
			document.facturasClienteForm.modo.value = "abrir";		
			document.facturasClienteForm.submit();
		}
	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->					
	<% if (sTipo!=null && sTipo.equals("LETRADO")) { %>
	 	<siga:Titulo titulo="censo.fichaCliente.facturacion.facturas.cabecera" localizacion="censo.fichaLetrado.facturacion.localizacion"/>
	<% } else { %>
		<siga:TituloExt titulo="censo.fichaCliente.facturacion.facturas.cabecera" localizacion="censo.fichaCliente.facturacion.facturas.localizacion"/>
	<%}%>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">

	<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="censo.facturacion.facturas.literal.Cabecera"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				<% if(!numero.equalsIgnoreCase("")) { %>
					<siga:Idioma key="censo.facturacion.facturas.literal.NColegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<% } else { %>
					<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<%}%>
			</td>
		</tr>
	</table>
		
	<table cellspacing="0" cellpadding="0" width="100%">
		<tr>			
			<html:form action="/CEN_FacturasCliente.do" method="POST" target="_self">
				<html:hidden property = "modo" styleId="modo" value = ""/>
				<!-- RGG: cambio a formularios ligeros -->
				<input type="hidden" id="actionModal"  name="actionModal" value="">
				<input type="hidden" id="incluirRegistrosConBajaLogica"  name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
				<input type="hidden" id="idPersona"  name="idPersona" value="<%=SidPersona.toString()%>">
				<input type="hidden" id="idInstitucion" name="idInstitucion" value="<%=SidInstitucion.toString()%>">
				<input type="hidden" id="accion" name="accion" value="<%=accion%>">
			</html:form>				
			
			<%
				String tamanosCol="";
				String nombresCol="";
				tamanosCol="10,12,20,12,12,12,12,6,";
				nombresCol="censo.facturacion.facturas.literal.Fecha,censo.facturacion.facturas.literal.NumeroFactura,censo.facturacion.facturas.literal.Descripcion,censo.facturacion.facturas.literal.ImporteNeto,censo.facturacion.facturas.literal.ImporteIVA,censo.facturacion.facturas.literal.ImporteTotal,censo.facturacion.facturas.literal.ImportePagado,";
			%>
			
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="<%=nombresCol %>"
			   columnSizes="<%=tamanosCol %>">
			
				<% if ((resultado == null) || (resultado.size() == 0)) { %>
					<tr class="notFound">
   						<td class="titulitos">
   							<siga:Idioma key="messages.noRecordFound"/>
   						</td>
					</tr>	
				<% } else {
						for (int i = 1; i <= resultado.size(); i++) {						
							Row fila = (Row) resultado.get(i-1);
		                    Hashtable factura = fila.getRow();
							if (factura != null) { 
								Integer idInstitucion = UtilidadesHash.getInteger(factura, FacFacturaBean.C_IDINSTITUCION);
								String idFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_IDFACTURA);
								String numFactura = UtilidadesHash.getString(factura, FacFacturaBean.C_NUMEROFACTURA);
								Long idPersona = UtilidadesHash.getLong(factura, FacFacturaBean.C_IDPERSONA);
								String fecha = UtilidadesHash.getString(factura, FacFacturaBean.C_FECHAEMISION);
								fecha = GstDate.getFormatedDateShort("", fecha);
								String serie = UtilidadesHash.getString(factura, FacSerieFacturacionBean.C_NOMBREABREVIADO);

								String descripcion = UtilidadesHash.getString(factura, FacFacturacionProgramadaBean.C_DESCRIPCION);
								Double totalNeto = UtilidadesHash.getDouble(factura, "TOTALNETO");
								Double totalIva = UtilidadesHash.getDouble(factura, "TOTALIVA");
								Double total = UtilidadesHash.getDouble(factura, "TOTAL");
								Double totalPagado = UtilidadesHash.getDouble(factura, "TOTALPAGADO");
   	 			%>
   	 				<siga:FilaConIconos fila='<%=""+i%>' botones="C" visibleEdicion="false" visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit"> 
						<td><!-- Datos ocultos tabla -->
							<input type="hidden" id="oculto<%=i%>_1" name="oculto<%=i%>_1" value="<%=idInstitucion%>">
							<input type="hidden" id="oculto<%=i%>_2" name="oculto<%=i%>_2" value="<%=idFactura%>">
							<input type="hidden" id="oculto<%=i%>_3" name="oculto<%=i%>_3" value="<%=total%>">
							<input type="hidden" id="oculto<%=i%>_4" name="oculto<%=i%>_4" value="<%=totalPagado%>">
							<input type="hidden" id="oculto<%=i%>_5" name="oculto<%=i%>_5" value="<%=accion%>">
							<input type="hidden" id="oculto<%=i%>_6" name="oculto<%=i%>_6" value="<%=idPersona.toString()%>">											
							<%=UtilidadesString.mostrarDatoJSP(fecha)%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(numFactura)%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(descripcion)%></td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalNeto.doubleValue()))%> &euro; </td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalIva.doubleValue()))%> &euro; </td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(total.doubleValue()))%> &euro; </td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(totalPagado.doubleValue()))%> &euro; </td>
					</siga:FilaConIconos>
				<%	
			 				} // if
				 		}  // for  
					} // else
				%>
			</siga:Table>
			
			<% if ( hm.get("datos")!=null && !hm.get("datos").equals("")) { %>  						
				<siga:Paginador totalRegistros="<%=totalRegistros%>" 
					registrosPorPagina="<%=registrosPorPagina%>" 
					paginaSeleccionada="<%=paginaSeleccionada%>" 
					idioma="<%=idioma%>"
					modo="abrirOtra"								
					clase="paginator" 
					divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
					distanciaPaginas=""
					action="<%=action%>" />
	 		<% } %>		
		</tr>
	</table>
	
	<div style="position:absolute; left:300px;bottom:5px;z-index:99;">
		<table align="center" border="0">
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
					<% if (bIncluirBajaLogica) { %>
						<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);" checked>
					<% } else { %>
						<input type="checkbox" name="incluirRegistrosConBajaLogica" onclick="incluirRegBajaLogica(this);">
					<% } %>
				</td>
			</tr>
		</table>
	</div>		

	<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modo='' clase="botonesDetalle"/>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>