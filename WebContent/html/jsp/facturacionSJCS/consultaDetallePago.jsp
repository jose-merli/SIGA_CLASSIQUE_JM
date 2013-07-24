<!DOCTYPE html>
<html>
<head>
<!-- consultaDetallePago.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=EUC-JP" >
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
	double total = 0, totalIrpf = 0, totalBrutos = 0, totalRetencion=0, totalTotal=0;
	
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


<!-- HEAD -->

	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
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
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=columnas%>"
		   columnSizes="<%=tamanoCol%>"
		   fixedHeight="380"
		   modal="g"
		   modalScroll="true">
		   
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%	
	} else { 
		for (int cont=1;cont<=resultado.size();cont++) {
			Hashtable fila = (Hashtable) resultado.get(cont-1);

			nombreColegiado    = UtilidadesString.mostrarDatoJSP(fila.get("NOMBREPERSONA"));
			ncolegiado         = UtilidadesString.mostrarDatoJSP(fila.get("NCOLEGIADO"));
			
			irpf               = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(new Double (UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF")),2));
			idPersona          = UtilidadesString.mostrarDatoJSP(fila.get("IDPERSONASJCS"));

			importeRetenciones = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(new Double (UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES")),2));
			importeTotalSJCS   = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(new Double (UtilidadesHash.getString(fila, "TOTALIMPORTESJCS")),2));
			importeTotalMovimientoVarios = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea(new Double (UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS")),2));


			double aux = Double.parseDouble(UtilidadesHash.getString(fila, "TOTALIMPORTESJCS")) + Double.parseDouble(UtilidadesHash.getString(fila, "IMPORTETOTALMOVIMIENTOS")) + Double.parseDouble(UtilidadesHash.getString(fila, "TOTALIMPORTEIRPF"))  + Double.parseDouble(UtilidadesHash.getString(fila, "IMPORTETOTALRETENCIONES"));
			importeTotalTotal = UtilidadesString.mostrarDatoJSP(UtilidadesNumero.redondea((new Double(aux)),2));
			
			// total     += Float.parseFloat(importe);
			//totalBrutos = Float.parseFloat(importeRetenciones) + Float.parseFloat(importeTotalSJCS) + Float.parseFloat(importeTotalMovimientoVarios);
			totalBrutos = Double.parseDouble(importeTotalSJCS) + Double.parseDouble(importeTotalMovimientoVarios);
			if (totalBrutos<0) totalBrutos=0;
			total      += totalBrutos;
			totalIrpf  += Double.parseDouble(irpf);
			totalRetencion  += Double.parseDouble(importeRetenciones);
			if ( Double.parseDouble(importeTotalTotal)<0) importeTotalTotal="0";
			totalTotal  += new Double(importeTotalTotal);
%>

  		<tr class="<%=((cont+1)%2==0?"filaTablaPar":"filaTablaImpar")%>">
				<td><input type="hidden" name="oculto<%=cont%>_1" value="<%=idPersona%>"><%=ncolegiado%></td>
				<td><%=nombreColegiado%></td>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<td align="right"><%=UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(importeTotalSJCS.toString()),2))%>&nbsp;&euro;</td>				
					<td align="right"><%=UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble (importeTotalMovimientoVarios.toString()),2))%>&nbsp;&euro;</td>				
				<% } %>
				<td align="right"><%=UtilidadesString.formatoImporte(totalBrutos)%>&nbsp;&euro;</td>
				<td align="right">
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<%=UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(irpf.toString()),2))%>&nbsp;&euro;
				<% } else { %>
					<siga:Idioma key="factSJCS.datosFacturacion.literal.IRPFSinCalcular"/>
				<% } %>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
					<td align="right"><%=UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(importeRetenciones.toString()),2))%>&nbsp;&euro;</td>
					<td align="right"><%=UtilidadesString.formatoImporte(UtilidadesNumero.redondea((new Double(aux)),2))%>&nbsp;&euro;</td>
				<% } %>
				</td>
  		</tr>

<%		} // del for
	} // del if 

	%>			

	</siga:Table>
	
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
					<%=(totales.get("totalBruto")==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(valorFinal.toString()),2)))%>&nbsp;&euro;
				</td>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
						<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
							<%=(totales.get("totalBruto")==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(valorFinalIrpf.toString()),2)))%>&nbsp;&euro;
						<% } else {	%>
							-
						<% } %>
				</td>
				<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
					<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
						<%=(totales.get("totalBruto")==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(valorFinalRetencion.toString()),2)))%>&nbsp;&euro;
					<% } else {	%>
						-
					<% } %>
				</td>
				<td width="<%=tamA%>%" class="labelTextNum" align="right">
					<% if (Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_EJECUTADO)) { %>
						<%=(totales.get("totalBruto")==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(Double.parseDouble(valorFinalTotal.toString()),2)))%>&nbsp;&euro;
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
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
