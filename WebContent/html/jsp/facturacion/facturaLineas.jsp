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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.Vector"%>


<!-- JSP -->
<% 
		String volver = request.getAttribute("volver")==null?"NO":(String)request.getAttribute("volver");
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
		
		double sumaTotal = 0, sumaAnticipado = 0;
%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		function refrescarLocal(){ 		
			document.location.reload();
		}

	</script>	
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="facturacion.facturas.lineas.cabecera" 
		localizacion="facturacion.facturas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>     

<body>

		<html:form action="/FAC_LineasFactura.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property="modo" value = ""/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "2"
			   clase  = "tableTitle"
			   nombreCol="facturacion.lineasFactura.literal.Orden,
			   						facturacion.lineasFactura.literal.Descripcion,
			   						facturacion.lineasFactura.literal.Cantidad,
			   						facturacion.lineasFactura.literal.Precio,
			   						facturacion.lineasFactura.literal.IVA,
										facturacion.lineasFactura.literal.Total,
			   						facturacion.lineasFactura.literal.Anticipado,"
			   tamanoCol = "9,40,7,9,7,9,9,10"
			   alto="100%"
			   ajuste="100"		
			   modal="M">
	
			<%	 
				if ((vLineas != null) && (vLineas.size() > 0)) {
								 
					for (int i = 1; i <= vLineas.size(); i++) { 
					
					 FacLineaFacturaBean linea = (FacLineaFacturaBean) vLineas.get(i-1);
					 if (linea != null) { 
							Double importe = linea.getPrecioUnitario();
							Integer cantidad = linea.getCantidad();
							Float iva = linea.getIva();
							Double anticipado = linea.getImporteAnticipado();
							
							double aux = cantidad.intValue() * importe.doubleValue() * (1 +  iva.floatValue() / 100);
							Double total = new Double (UtilidadesNumero.redondea(aux ,2));
							
							sumaTotal = sumaTotal + total.doubleValue();
							sumaAnticipado = sumaAnticipado + anticipado.doubleValue();
						%>

							<siga:FilaConIconos fila='<%=""+i%>' botones="<%=botones%>" visibleBorrado="false" clase="listaNonEdit"> 
							<td><!-- Datos ocultos tabla -->
									<input type="hidden" id="oculto<%=i%>_1" value="<%=String.valueOf(linea.getIdInstitucion())%>">
									<input type="hidden" id="oculto<%=i%>_2" value="<%=linea.getIdFactura()%>">
									<input type="hidden" id="oculto<%=i%>_3" value="<%=String.valueOf(linea.getNumeroLinea())%>">
									<%=UtilidadesString.mostrarDatoJSP(linea.getNumeroOrden())%></td>
							<td><%=UtilidadesString.mostrarDatoJSP(linea.getDescripcion())%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(cantidad)%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importe.doubleValue()))%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(iva.doubleValue()))%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(total.doubleValue()))%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(anticipado.doubleValue()))%></td>
							</siga:FilaConIconos>
							 		
		<%	 		 } // if
			 	 }  // for  %>
<% 				} // if  %>

			</siga:TablaCabecerasFijas>

	<div id="totales" style="bottom:50px; height:50px; left:0; position:absolute; width:100%;">
			<table align="right" width=100%>
				<tr>			
					<td class="labelTextNum" width="9%">&nbsp;</td>
					<td class="labelTextNum" width="40%">&nbsp;</td>
					<td class="labelTextNum" width="7%">&nbsp;</td>
					<td class="labelTextNum" width="9%">&nbsp;</td>
					<td class="labelTextNum" width="7%">&nbsp;</td>
					<td class="labelTextNum" width="9%"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(sumaTotal,2)))%>&nbsp;&euro;</td>
					<td class="labelTextNum" width="9%"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(UtilidadesNumero.redondea(sumaAnticipado,2)))%>&nbsp;&euro;</td>
					<td class="labelTextNum" width="10%">&nbsp;</td>
				</tr>
			</table>
	</div>		

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
