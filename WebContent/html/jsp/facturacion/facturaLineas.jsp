<!DOCTYPE html>
<html>
<head>
<!-- facturaLineas.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->

<%@ page import="com.siga.beans.FacLineaFacturaBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.apache.struts.action.ActionMapping"%>

<!-- JSP -->
<% 
	ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
	String path = actionMapping.getPath();	
	String volver = request.getAttribute("volver")==null?"NO":(String)request.getAttribute("volver");

	// Obtencion del tipo de acceso sobre la pestanha del usuario de la aplicacion
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String botonesAccion = "";

	// Gestion de Volver
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");

	if (busquedaVolver.equals("DEV_MANUAL")) {
		volver="SI";
	}

	if (volver!=null && volver.equalsIgnoreCase("SI")){
		botonesAccion = "V";
	}
		
	String app = request.getContextPath(); 
	
	Vector vLineas    = (Vector) request.getSession().getAttribute("DATABACKUP");
	
	String botones = "C";


	Boolean modificar = (Boolean)request.getAttribute("modificar");
	if (modificar.booleanValue()) {
		botones = botones + ",E";
	}

	String modoRegistroBusqueda = (String)request.getAttribute("modoRegistroBusqueda");
	if ((modoRegistroBusqueda != null) && (modoRegistroBusqueda.equalsIgnoreCase("ver"))) {
		botones = "C";
	}	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function refrescarLocal(){ 		
			document.location.reload();
		}
	</script>	
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
 	<% 	if (usr.getStrutsTrans().equals("FAC_BusquedaFactura")) {%>
			<siga:Titulo titulo="facturacion.facturas.lineas.cabecera"	localizacion="facturacion.facturas.localizacion"/>
	<% } else if (usr.getStrutsTrans().equals("CEN_BusquedaClientesColegiados")) {%>
			<siga:Titulo titulo="facturacion.facturas.lineas.cabecera"	localizacion="censo.facturacion.facturas.localizacion"/>
	<% }%>			
	<!-- FIN: TITULO Y LOCALIZACION -->	
</head>     

<body>
	<html:form action="<%=path%>" method="POST" target="submitArea" style="display:none">
		<html:hidden styleId="modo"  property="modo" value = ""/>
	</html:form>
		
	<siga:Table 
	   name = "tablaResultados"
	   border  = "2"
	   columnNames="facturacion.lineasFactura.literal.Descripcion,
					facturacion.lineasFactura.literal.Cantidad,
					facturacion.lineasFactura.literal.precioUnitario,
					facturacion.lineasFactura.literal.importeNeto,
					facturacion.lineasFactura.literal.IVA,
					facturacion.lineasFactura.literal.importeIVA,
					facturacion.lineasFactura.literal.importeTotal,
					facturacion.lineasFactura.literal.Anticipado,"
	   columnSizes = "26,8,10,10,6,10,10,10,10"
	   modal="M">
	
<%	 
		double dSumaTotal=0, dSumaAnticipado=0, dSumaImporteNeto=0, dSumaImporteIVA=0;

		if ((vLineas != null) && (vLineas.size() > 0)) {								 
			for (int i = 1; i <= vLineas.size(); i++) { 
				 FacLineaFacturaBean linea = (FacLineaFacturaBean) vLineas.get(i-1);
				 if (linea != null) {
					int iCantidad = linea.getCantidad().intValue();
					double dPrecioUnitario = linea.getPrecioUnitario();					
					double dImporteNeto =  dPrecioUnitario * iCantidad;
					double dIVA = linea.getIva().doubleValue();
					double dImporteIVA = dImporteNeto * dIVA / 100;
					double dImporteTotal = dImporteIVA + dImporteNeto;
					double dAnticipado = linea.getImporteAnticipado().doubleValue();
					
					dSumaTotal = dSumaTotal + dImporteTotal;
					dSumaAnticipado = dSumaAnticipado + dAnticipado;
					dSumaImporteNeto = dSumaImporteNeto + dImporteNeto;
					dSumaImporteIVA = dSumaImporteIVA + dImporteIVA;
%>

					<siga:FilaConIconos fila='<%=""+i%>' botones="<%=botones%>" visibleBorrado="false" clase="listaNonEdit"> 
						<td><!-- Datos ocultos tabla -->
							<input type="hidden" id="oculto<%=i%>_1" value="<%=String.valueOf(linea.getIdInstitucion())%>">
							<input type="hidden" id="oculto<%=i%>_2" value="<%=linea.getIdFactura()%>">
							<input type="hidden" id="oculto<%=i%>_3" value="<%=String.valueOf(linea.getNumeroLinea())%>">
							<%=UtilidadesString.mostrarDatoJSP(linea.getDescripcion())%>
						</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(iCantidad)%></td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dPrecioUnitario))%>&nbsp;&euro;</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dImporteNeto))%>&nbsp;&euro;</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dIVA))%>&nbsp;%</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dImporteIVA))%>&nbsp;&euro;</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dImporteTotal))%>&nbsp;&euro;</td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dAnticipado))%>&nbsp;&euro;</td>
					</siga:FilaConIconos>							 		
<%	 		 
				} // if
			 }  // for
%>
			<tr class="listaNonEditSelected" style="height:30px">
				<td><b><siga:Idioma key="facturacion.lineasFactura.literal.Total"/></b></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td align="right"><b><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dSumaImporteNeto))%>&nbsp;&euro;</b></td>
				<td>&nbsp;</td>
				<td align="right"><b><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dSumaImporteIVA))%>&nbsp;&euro;</b></td>
				<td align="right"><b><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dSumaTotal))%>&nbsp;&euro;</b></td>
				<td align="right"><b><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(dSumaAnticipado))%>&nbsp;&euro;</b></td>
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
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion clase="botonesDetalle" botones='<%=botonesAccion %>' modo=''/>

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>