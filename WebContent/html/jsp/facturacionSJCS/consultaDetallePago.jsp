<!-- consultaDetallePago.jsp -->
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
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoMovimientosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	//recoger de request el vector con los registros resultado
	Vector resultado = (Vector) request.getAttribute("resultado");
	
	Hashtable totales = (Hashtable) request.getAttribute("totales");

	//campos a mostrar en la tabla
	String nombreColegiado ="", importe="", ncolegiado="", idPersona="", irpf="";

	//estado del pago
	String estado = request.getAttribute("estado")==null?"":(String)request.getAttribute("estado");
	boolean abierta = false;
	if (estado.equalsIgnoreCase("abierta"))
		abierta = true;
	
	//Vemos si es nuevo:
	boolean esNuevo = request.getAttribute("NUEVO")==null?false:true;

	//campos ocultos
	String idInstitucion ="", idPago="", estadoPago="", modoOriginal="";
	try
	{
		idInstitucion = (String)request.getAttribute("idInstitucion");
		idPago = (String)request.getAttribute("idPagosJG");
		estadoPago = (String)request.getAttribute("estadoPago");
		modoOriginal = (String)request.getAttribute("modoOriginal");
	}
	catch(Exception e){}

	//resultado del importeTotal

	//el nombre del colegio
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");

	//botones a mostrar
	String botones="";

	//para el importe total de la facturacion
	float total = 0, totalIrpf = 0, totalBrutos = 0, totalRetencion=0, totalTotal=0;
	
	//Importe de Retenciones y Total SJCS:
	String importeTotalSJCS="", importeRetenciones="", importeTotalMovimientoVarios="",importeTotalTotal="";
	// boolean existeMV=false;
	
	String valorFinal ="", valorFinalIrpf ="", valorFinalRetencion="", valorFinalTotal="";
	if(totales!=null){
		valorFinalRetencion = totales.get("totalRetencion")!=null?totales.get("totalRetencion").toString():"";
		valorFinalIrpf = totales.get("totalIrpf")!=null?totales.get("totalIrpf").toString():"";
		valorFinalTotal = totales.get("totalTotal")!=null?totales.get("totalTotal").toString():"";
		valorFinal = totales.get("totalBruto")!=null?totales.get("totalBruto").toString():"";
	}
%>

<html>
<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.pagos.detallePagos" 
		localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DatosDetallePagoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	<script language="JavaScript">	
	
		function refrescarLocal() 
		{
			parent.buscar();
		}
	</script>
</head>

<body class="tablaCentralCampos">

		<!-- TITULO -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
			<html:form action="/FCS_DetallePago.do" method="POST" target="_self">

			<!-- Campo obligatorio -->
			<html:hidden name="DatosDetallePagoForm" property="modo" value = "" />
			<html:hidden name="DatosDetallePagoForm" property="idPago" value = "<%=idPago%>" />
			<html:hidden name="DatosDetallePagoForm" property="idInstitucion" value = "<%=idInstitucion%>" />
			<html:hidden name="DatosDetallePagoForm" property="estadoPago" value = "<%=estadoPago%>" />
			<html:hidden name="DatosDetallePagoForm" property="modoOriginal" value = "<%=modoOriginal%>" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
			
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosPagos.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
			</tr>
		</table>
	
<% if (abierta) { %>
	<br>
	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.factSJCS.error.noExisteDetallePago"/></p>
	<br>
<% } else {
		//Si el estado del pago es mayor o igual a cerrado muestro 2 columnas mas: importe retenciones y el importe sjcs.
		String columnas="", tamanoCol="";
		if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) {
			columnas = "factSJCS.detalleFacturacion.literal.nColegiado,factSJCS.detalleFacturacion.literal.colegiado,factSJCS.datosPagos.literal.importeSJCS,factSJCS.datosPagos.literal.importeMovimientosVarios,factSJCS.datosFacturacion.literal.importeBruto,factSJCS.detalleFacturacion.literal.importeIRPF,factSJCS.datosPagos.literal.importeRetenciones,factSJCS.detalleFacturacion.literal.importe";
			tamanoCol = "9,20,12,12,12,12,12";
		}
		else {
			columnas = "factSJCS.detalleFacturacion.literal.nColegiado,factSJCS.detalleFacturacion.literal.colegiado,factSJCS.datosFacturacion.literal.importeBruto,factSJCS.detalleFacturacion.literal.importeIRPF";
			tamanoCol = "25,25,25,25";
		}
%>
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=columnas%>"
		   tamanoCol="<%=tamanoCol%>"
		   alto="100%"
		   ajuste="90"					   
		   modal="g"
		   scrollModal="true"
		  >
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>
<%	
	} else { 
		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nombreColegiado    = UtilidadesString.mostrarDatoJSP(fila.get("NOMBREPERSONA"));
			ncolegiado         = UtilidadesString.mostrarDatoJSP(fila.get("NCOLEGIADO"));
			
			irpf               = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"),2));
			idPersona          = UtilidadesString.mostrarDatoJSP(fila.get("IDPERSONASJCS"));

			importeRetenciones = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"),2));
			importeTotalSJCS   = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS"),2));
			importeTotalMovimientoVarios = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS"),2));


			float aux = Float.parseFloat(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS")) + Float.parseFloat(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS")) + Float.parseFloat(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"))  + Float.parseFloat(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"));
			importeTotalTotal = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Float(aux)).toString(),2));
			
			// total     += Float.parseFloat(importe);
			//totalBrutos = Float.parseFloat(importeRetenciones) + Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios);
			totalBrutos = Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios);
			if (totalBrutos<0) totalBrutos=0;
			total      += totalBrutos;
			totalIrpf  += Float.parseFloat(irpf);
			totalRetencion  += Float.parseFloat(importeRetenciones);
			if ( Float.parseFloat(importeTotalTotal)<0) importeTotalTotal="0";
			totalTotal  += Float.parseFloat(importeTotalTotal);
%>

  		<tr class="<%=((cont+1)%2==0?"filaTablaPar":"filaTablaImpar")%>">
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idPersona%>"><%=ncolegiado%></td>
				<td><%=nombreColegiado%></td>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<td align="right"><%=UtilidadesString.formatoImporte(importeTotalSJCS)%>&nbsp;&euro;</td>				
					<td align="right"><%=UtilidadesString.formatoImporte(importeTotalMovimientoVarios)%>&nbsp;&euro;</td>				
				<% } %>
				<td align="right"><%=UtilidadesString.formatoImporte(totalBrutos)%>&nbsp;&euro;</td>
				<td align="right">
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<%=UtilidadesString.formatoImporte(irpf)%>&nbsp;&euro;
				<% } else { %>
					<siga:Idioma key="factSJCS.datosFacturacion.literal.IRPFSinCalcular"/>
				<% } %>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<td align="right"><%=UtilidadesString.formatoImporte(importeRetenciones)%>&nbsp;&euro;</td>
					<td align="right"><%=UtilidadesString.formatoImporte(importeTotalTotal)%>&nbsp;&euro;</td>
				<% } %>
				</td>
  		</tr>

<%		} // del for
	} // del if 

	%>			

	</siga:TablaCabecerasFijas>
	
	<!-- Totales -->
	<%int tamIni = 50, tamA = 25; // 4 columnas
		if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { 
			//tamIni = 72; // 7 columnas
			//tamA = 14;
			tamIni = 50; // 7 columnas
			tamA = 14;
		}
	%>
<div style="position:absolute;width:100%;bottom:30px;left:0px;">
		<table border="0" width="100%">
			<tr>
				<td width="<%=tamIni%>%" class="labelTextNum">&nbsp;</td>
				<td align="right" class="tableTitle">
					<b><siga:Idioma key="factSJCS.datosFacturacion.literal.totalBruto"/></b>
				</td>
				<td style="align:right" class="tableTitle">
					<b><siga:Idioma key="factSJCS.detalleFacturacion.literal.totalIRPF"/></b>
				</td>
				<td style="align:right" class="tableTitle">
					<b><siga:Idioma key="factSJCS.detalleFacturacion.literal.totalRetenciones"/></b>
				</td>
				<td style="align:right" class="tableTitle">
					<b><siga:Idioma key="factSJCS.detalleFacturacion.literal.total"/></b>
				</td>
			</tr>
			<tr >
				
		      <td width="<%=tamIni%>%"></td>

				<td width="<%=tamA%>%" class="labelTextNum" align="right">
					<%=UtilidadesString.formatoImporte(valorFinal)%>&nbsp;&euro;
				</td>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
						<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
							<%=UtilidadesString.formatoImporte(valorFinalIrpf)%>&nbsp;&euro;
						<% } else {	%>
							-
						<% } %>
				</td>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
					<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
						<%=UtilidadesString.formatoImporte(valorFinalRetencion)%>&nbsp;&euro;
					<% } else {	%>
						-
					<% } %>
				</td>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
					<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
						<%=UtilidadesString.formatoImporte(valorFinalTotal)%>&nbsp;&euro;
					<% } else {	%>
						-
					<% } %>
				</td>
				<% } %>
			</tr>
		</table>
	</div>

<% } //Fin del else para cuando el estado es distinto de abierto %>


		<!-- FIN: LISTA DE VALORES -->
<%
	String bot = "V";
	if (!abierta && !esNuevo) {
//		bot += ",DPA";
		bot += ",dLetrado";
		bot += ",dConcepto";
	}
%>
		
	<siga:ConjBotonesAccion botones="<%=bot%>" clase="botonesDetalle"/>
	
	<!-- FORMULARIO INICIAL -->
	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
	</html:form>
	
	
<script language="JavaScript">
	
	//Asociada al boton Volver
	function accionVolver() 
	{		
		var f = document.getElementById("mantenimientoPagoForm");
		f.target = "mainPestanas";
		f.submit();
	}
		
	function accionDetalleLetrado() 
	{	
		var f = document.getElementById("DatosDetallePagoForm");
		f.modo.value = "detalleLetrado";
		f.submit();

		// Con pantalla de espera
		// document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.wait'; 
	}

	function accionDetalleConcepto() 
	{		
		var f = document.getElementById("DatosDetallePagoForm");
		f.modo.value = "detalleConcepto";
		ventaModalGeneral(f.name,"P");
		
		// f.submit();
		// Con pantalla de espera
		// document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.wait'; 
	}
		
</script>	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
