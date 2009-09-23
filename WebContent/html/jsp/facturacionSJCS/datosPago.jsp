<!-- datosPago.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.facturacionSJCS.form.DatosGeneralesPagoForm"%>
<%@ page import = "com.atos.utils.ClsConstants"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.siga.beans.FcsPagosJGBean"%>
<%@ page import = "com.atos.utils.GstDate"%>
<%@ page import = "java.util.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	DatosGeneralesPagoForm formulario = (DatosGeneralesPagoForm)request.getAttribute("formularioPagos");
	String modo = (String)request.getAttribute("modo");
	String accion = (String)request.getAttribute("accion");
	String accionPrevia = accion;

	//NOMBRE INSTITUCION PESTAÑA:
	String nombreInstitucion = request.getAttribute("nombreInstitucion")==null?"":(String)request.getAttribute("nombreInstitucion"); 
	
	//DATOS DEL BEAN PAGOSJG:
	String cantidad="", porcentajeTurnos="0", porcentajeGuardias="0", porcentajeSOJ="0", porcentajeEJG="0";
	String idInstitucion="", idPagosJG="", nombre="", fechaDesde="", fechaHasta="", abreviatura="", idFacturacion="", criterioPagoTurno="";
	String importePagado="";
	
	//DATOS DEL ESTADO:
	String nombreEstado="", idEstadoPagosJG="", fechaEstado="";
	
	//DATOS FACTURACION:
	String idEstadoFacturacion="", nombreFacturacion="", importeFacturado="";

	//COBRO AUTOMATICO
	String cobroAutomatico ="";

	Long importePendienteDePago = null;

	//DATOS CUANDO VENIMOS DE EDITAR/VER:
	if (accion.equalsIgnoreCase("edicion") || accion.equalsIgnoreCase("consulta")) {
		//Datos del Bean:
		FcsPagosJGBean pagosBean = (FcsPagosJGBean)request.getAttribute("PAGOSBEAN");
		idInstitucion = pagosBean.getIdInstitucion().toString();//Propia del registro
		idPagosJG = pagosBean.getIdPagosJG().toString();
		idFacturacion = pagosBean.getIdFacturacion().toString();
		nombre = pagosBean.getNombre()==null?"":pagosBean.getNombre();
		abreviatura = pagosBean.getAbreviatura()==null?"":pagosBean.getAbreviatura();
		fechaDesde = pagosBean.getFechaDesde()==null?"":GstDate.getFormatedDateShort(usrbean.getLanguage(),pagosBean.getFechaDesde());
		fechaHasta = pagosBean.getFechaHasta()==null?"":GstDate.getFormatedDateShort(usrbean.getLanguage(),pagosBean.getFechaHasta());
		criterioPagoTurno = pagosBean.getCriterioPagoTurno()==null?"":pagosBean.getCriterioPagoTurno();
		porcentajeTurnos = pagosBean.getPorcentajeOficio()==null?"":pagosBean.getPorcentajeOficio().toString();
		porcentajeGuardias = pagosBean.getPorcentajeGuardias()==null?"":pagosBean.getPorcentajeGuardias().toString();
		porcentajeEJG = pagosBean.getPorcentajeEJG()==null?"":pagosBean.getPorcentajeEJG().toString();
		porcentajeSOJ = pagosBean.getPorcentajeSOJ()==null?"":pagosBean.getPorcentajeSOJ().toString();
		cantidad = pagosBean.getImporteRepartir()==null?"":pagosBean.getImporteRepartir().toString();
		importePagado = pagosBean.getImportePagado()==null?"":pagosBean.getImportePagado().toString();
		
		//Resto de Datos:
		cobroAutomatico = (String)request.getAttribute("cobroAutomatico");
		nombreEstado = (String)request.getAttribute("nombreEstado");
		fechaEstado = GstDate.getFormatedDateShort(usrbean.getLanguage(),(String)request.getAttribute("fechaEstado"));
		idEstadoPagosJG = request.getAttribute("idEstadoPagosJG")==null?"":(String)request.getAttribute("idEstadoPagosJG");
		idEstadoFacturacion = request.getAttribute("idEstadoFacturacion")==null?"":(String)request.getAttribute("idEstadoFacturacion");
		nombreFacturacion = request.getAttribute("nombreFacturacion")==null?"":(String)request.getAttribute("nombreFacturacion");
		importeFacturado = request.getAttribute("importeFacturado")==null?"":(String)request.getAttribute("importeFacturado");
	} 
	else { //Venimos de Nuevo
		idInstitucion = request.getAttribute("idInstitucionRegistro")==null?"":(String)request.getAttribute("idInstitucionRegistro");//Propia del registro
	}
	
	importePendienteDePago = request.getAttribute("importePendienteDePago")==null?null:(Long)request.getAttribute("importePendienteDePago");
	
	//Accion Previa para ver la modal en modo edicion o consulta:
	if (idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_EJECUTADO) || idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_CERRADO))
		accionPrevia = "consulta";
	
	//VISIBILIDAD:
	boolean b_lectura=false;
	String estilo="";
	String estiloNumber="boxConsultaNumber";
	if (accion.equalsIgnoreCase("nuevo")) {
		nombreEstado="";
		fechaEstado="";
		b_lectura = false;
		estilo = "box";
		estiloNumber="boxNumber";
	} else {
		if (accion.equalsIgnoreCase("edicion")) {			
			estilo = "box";
			if (idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_EJECUTADO) || idEstadoPagosJG.equals(ClsConstants.ESTADO_PAGO_CERRADO)) {
				b_lectura = true;
				estilo = "boxConsulta";
				estiloNumber="boxConsultaNumber";
			}
		} else if (accion.equalsIgnoreCase("consulta")) {
					b_lectura = true;
					estilo = "boxConsulta";
					estiloNumber="boxConsultaNumber";
					
				}
	}
	
	//COMBO FACTURACION:
	String facturacionParams[] = new String[2];
	facturacionParams[0] = usrbean.getLocation();
	facturacionParams[1] = String.valueOf(ClsConstants.ESTADO_FACTURACION_LISTA_CONSEJO);
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.pagos.datosGenerales" 
		localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosGeneralesPagoForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
	<script>
			function  convertirAFormato(n){
			var d = n.replace(/,/,".");
			d = new Number(n);
			d = Number(d.toFixed(2));
			d = d.toLocaleString();
			if(String(d).indexOf(',') < 0){
				d += '.00'; // aqui puede variar segun la cantidad de decimales que desees;
			}	
			return d.replace(".","");	
		}
		
		/*	NOTA:
			El combo me devuelve separado por ~ y en este orden:			
			0.IDFACTURACION
			1.FECHADESDE
			2.FECHAHASTA
			3.TOTALPAGADO
			4.TOTALFACTURADO 
		*/
		function actualizarFechas(obj){
			var seleccionado = obj.options[obj.selectedIndex].value;
			var vseleccionado = seleccionado.split('~');
			
			//IDFACTURACION:
			var id = vseleccionado[0];
			
			//FECHADESDE:
			var fechaDesde = vseleccionado[1];
			
			//FECHAHASTA:
			var fechaHasta = vseleccionado[2];
			
			//TOTALPAGADO:
			var importePagado = vseleccionado[3];
			
			//TOTALFACTURADO:
			var importeFacturado = vseleccionado[4];
			
			//Actualizo los campos:		
			f = document.getElementById("datosGeneralesPagoForm");
			f.idFacturacion.value = id;
			f.fechaDesde.value = fechaDesde;
			f.fechaHasta.value = fechaHasta;			
			f.importePagado.value = importePagado;
			f.importeFacturado.value = importeFacturado;			
		}
		
		//Selecciona por texto el option adecuado segun el nombreFacturacion
		function inicio(){
			f = document.getElementById("datosGeneralesPagoForm").comboFacturacion;
			for (var i=0; i< f.options.length; i++){
				if (f.options[i].text == "<%=nombreFacturacion%>") {
					f.options[i].selected = true;					
					var seleccionado = f.options[i].value;
					var id = seleccionado.substring(0,seleccionado.indexOf(","))
					document.getElementById("datosGeneralesPagoForm").idFacturacion.value = id;
				}
			}
		}
				
	</script>

</head>

<body>

<!-- ******* BOTONES Y CAMPOS ****** -->
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosPagos.titulo1"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos" cellpadding="0" cellspacing="0" align="center">

	<html:form action="/FCS_DatosGeneralesPago.do?noReset=true" method="POST" target="submitArea">
	<html:hidden name="datosGeneralesPagoForm" property="modo" value="<%=modo%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idFacturacion" value="<%=idFacturacion%>" />
	<html:hidden name="datosGeneralesPagoForm" property="idEstadoPagosJG" value="<%=idEstadoPagosJG%>" />
	<html:hidden name="datosGeneralesPagoForm" property="cobroAutomatico" value="<%=cobroAutomatico%>" />
	<html:hidden name="datosGeneralesPagoForm" property="actionModal" value=""/>
	<html:hidden name="datosGeneralesPagoForm" property="accionPrevia" value="<%=accionPrevia%>"/>
	
	<tr>				
	<td>
	<siga:ConjCampos leyenda="factSJCS.datosPagos.leyenda1">
	<table class="tablaCampos" align="center" border="0">

	<!-- FILA -->
	<tr>				
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.nombre"/>&nbsp;(*)
	</td>				
	<td class="labelText">
		<html:text name="datosGeneralesPagoForm" property="nombre" size="45" styleClass="<%=estilo%>" readonly="<%=b_lectura%>" value="<%=nombre%>" />
	</td>
	<td class="labelText" colspan="2">
		<siga:Idioma key="factSJCS.datosPagos.literal.abreviatura"/>&nbsp;(*)
		&nbsp;
		<html:text name="datosGeneralesPagoForm" property="abreviatura" size="45" styleClass="<%=estilo%>" readonly="<%=b_lectura%>"  value="<%=abreviatura%>" />
	</td>
	</tr>

	<!-- FILA -->
	<tr>				
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.importe"/>&nbsp;(*)
	</td>
	<td class="labelText">
		<html:text name="datosGeneralesPagoForm" property="importeRepartir" size="20" maxlength="11" styleClass="<%=estiloNumber%>" readonly="<%=b_lectura%>"  value="<%=UtilidadesNumero.formatoCampo(cantidad)%>" />
		&nbsp;&euro;
	</td>
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.estado"/>
		&nbsp;
		<html:text name="datosGeneralesPagoForm" property="nombreEstado" value='<%=nombreEstado%>' styleClass="boxConsulta" readOnly="true"></html:text>	
	</td>
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.fechaEstado"/>
		&nbsp;
		<html:text name="datosGeneralesPagoForm" property="fechaEstado" value='<%=fechaEstado%>' styleClass="boxConsulta" readOnly="true"></html:text>	
	</td>
	</tr>

	<!-- FILA -->
	<!-- DATOS DEL TOTAL PAGADO -->
	<tr>				
	<td class="labelText" colspan="1">
		<siga:Idioma key="factSJCS.datosFacturacion.literal.totalPagado"/>
	</td>
	<td class="labelText" colspan="3">
		<html:text name="datosGeneralesPagoForm" property="importePagado" size="20" styleClass="boxConsultaNumber" readonly="<%=b_lectura%>"  value="<%=UtilidadesNumero.formatoCampo(importePagado)%>" />
		&nbsp;&euro;
	</td>
	</tr>

	<!-- FILA -->
	<!-- DATOS DE LA FACTURACION -->
	<tr>				
	<td colspan="4">
		<siga:ConjCampos leyenda="factSJCS.datosGenerales.cabecera">
		<table class="tablaCampos" align="center" border="0">
		<tr>
		<td class="labelText" >
			<siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/>&nbsp;(*)
		</td>
		<td class="labelText">
			<% if (accion.equalsIgnoreCase("nuevo")) { %>
				<siga:ComboBD nombre="comboFacturacion" tipo="cmb_facturacionPagosTodos" estilo="width:400" parametro="<%=facturacionParams %>" clase="boxCombo" obligatorio="false" accion="actualizarFechas(this);"  />
			<% } else { %>
				<html:text name="datosGeneralesPagoForm" property="comboFacturacion" size="35" value='<%=nombreFacturacion%>' styleClass="boxConsulta" readOnly="true"></html:text>
			<% } %>
		</td>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.fechaInicio"/>
		</td>
		<td class="labelText">
			<html:text name="datosGeneralesPagoForm" property="fechaDesde" value='<%=fechaDesde%>' size="10" styleClass="boxConsulta" readOnly="true"></html:text>
		</td>
		</tr>

		<tr>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.importeFacturado"/>
		</td>
		<td class="labelText">
			<html:text name="datosGeneralesPagoForm" property="importeFacturado" value="<%=UtilidadesNumero.formatoCampo(importeFacturado)%>" size="20" styleClass="boxConsultaNumber" readOnly="true"></html:text>&nbsp;&euro;
		</td>				
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosPagos.literal.fechaFin"/>
		</td>
		<td class="labelText">
			<html:text name="datosGeneralesPagoForm" property="fechaHasta" value='<%=fechaHasta%>' size="10" styleClass="boxConsulta" readOnly="true"></html:text>
		</td>				
		</tr>
		</table>
		</siga:ConjCampos>
	</td>
	</tr>
	
	<!-- FILA -->
	<tr>				
	<td class="labelText" colspan="2">
		<table cellspacing="0" cellpadding="0" width="100%">
		<tr>
		<td>
		<siga:ConjCampos leyenda="factSJCS.datosPagos.leyendaPorcentaje">
			<table class="tablaCampos" align="center" border="0">
			<!-- FILA -->
			<tr>				
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosPagos.literal.porcentajeTurnos"/>
				</td>				
				<td class="labelText">
					<html:text name="datosGeneralesPagoForm" property="porcentajeOficio" size="3" styleClass="<%=estiloNumber%>" readonly="<%=b_lectura%>"  value="<%=porcentajeTurnos%>" />
					&nbsp;
				    &#37;
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosPagos.literal.porcentajeGuardias"/>
				</td>				
				<td class="labelText">
					<html:text name="datosGeneralesPagoForm" property="porcentajeGuardias" size="3" styleClass="<%=estiloNumber%>" readonly="<%=b_lectura%>" value="<%=porcentajeGuardias%>" />
					&nbsp;
				    &#37;
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosPagos.literal.porcentajeEJG"/>
				</td>				
				<td class="labelText">
					<html:text name="datosGeneralesPagoForm" property="porcentajeEJG" size="3" styleClass="<%=estiloNumber%>" readonly="<%=b_lectura%>" value="<%=porcentajeEJG%>" />
					&nbsp;
				    &#37;
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosPagos.literal.porcentajeSOJ"/>
				</td>				
				<td class="labelText">
					<html:text name="datosGeneralesPagoForm" property="porcentajeSOJ" size="3" styleClass="<%=estiloNumber%>" readonly="<%=b_lectura%>" value="<%=porcentajeSOJ%>" />
					&nbsp;
				    &#37;
					&nbsp;
				</td>
			</tr>
			</table>
		</siga:ConjCampos>
		</td>
		</tr>
		</table>
	</td>
	
	<td class="labelText" colspan="2" align="center">
	<html:hidden name="datosGeneralesPagoForm" property="criterioPagoTurno" value="<%=ClsConstants.CRITERIOS_PAGO_FACTURACION%>" />

	</td>
	</tr>
	<!-- FILA -->	
	
	<tr>
	<td>
		&nbsp;
	</td>
	</tr>
	<!-- FILA -->	
	</table>
	</siga:ConjCampos>

	</html:form>

	</table>
	<!-- FIN: CAMPOS-->

	<%
		String botones = "V";
		if (accion.equalsIgnoreCase("nuevo")) 
			botones = "V,G,R";
		else {
			if (accion.equalsIgnoreCase("edicion")) {
				if (idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_ABIERTO))) botones += ",EF,G,R";
				if (idEstadoPagosJG.equals(String.valueOf(ClsConstants.ESTADO_PAGO_EJECUTADO))) botones += ",CP";
			}
			botones += ",VC";
		}
	%>


	<siga:ConjBotonesAccion botones="<%=botones%>"/>

	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="subPestanas">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
			<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<html:hidden name="mantenimientoPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	</html:form>
	
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">		
		//Validador de porcentajes. Suma <= 100
		function validarPorcentajes(f){
			if (f.porcentajeOficio.value!="" && f.porcentajeGuardias.value!="" && f.porcentajeEJG.value!="" && f.porcentajeSOJ.value!="") {
				var suma = parseInt(f.porcentajeOficio.value) + parseInt(f.porcentajeGuardias.value) + parseInt(f.porcentajeEJG.value) + parseInt(f.porcentajeSOJ.value);
				return (suma <= 100);
			} else
				return false;
		}
	
		// Asociada al boton Volver
		function accionVolver() 
		{		
			var f = document.getElementById("mantenimientoPagoForm");
			f.target = "mainPestanas";
			f.submit();
		}
		
		//Convierte a formato java los campos de tipo precio
		function actualizarCamposPrecio(){
			document.forms[0].importeRepartir.value=document.forms[0].importeRepartir.value.replace(/,/,".");
			document.forms[0].importePagado.value=document.forms[0].importePagado.value.replace(/,/,".");
			document.forms[0].importeFacturado.value=document.forms[0].importeFacturado.value.replace(/,/,".");
		}
		
		function formatearCamposprecio(){
			document.forms[0].importeRepartir.value = convertirAFormato(document.forms[0].importeRepartir.value);
			document.forms[0].importePagado.value = convertirAFormato(document.forms[0].importePagado.value);
			document.forms[0].importeFacturado.value = convertirAFormato(document.forms[0].importeFacturado.value);								
		}
	
		function accionEjecutaFacturacion() 
		{		
				//Convierte a formato java los campos de tipo precio
				actualizarCamposPrecio();
				
				var f=document.getElementById("datosGeneralesPagoForm");
				f.modo.value = "ejecutarPago";

				var fname = document.getElementById("datosGeneralesPagoForm").name;
				// con pantalla de espera
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoFacturacion';				
		}
		
		function accionGuardar() 
		{						
			var f=document.getElementById("datosGeneralesPagoForm");
			f.importeRepartir.value = f.importeRepartir.value.replace(/,/,".");
			f.target = "submitArea";
			sub();				
			if (validateDatosGeneralesPagoForm(f)) {			
				//Calculo lo que le queda por pagar:
				var importeFacturado = 0;
				var importePagado = 0;				
				var total = 0;
				if (f.importeFacturado.value!='' && f.importePagado.value!='') {
					var importeFacturado = parseFloat(f.importeFacturado.value.replace(/,/,"."));
					var importePagado = parseFloat(f.importePagado.value.replace(/,/,"."));				
					var total = importeFacturado - importePagado;
				}
				
				//Miro si el importe a repartir excede del pendiente a pagar:
				if ((f.importeRepartir.value!='') && (parseFloat(f.importeRepartir.value) > total)) {
				    if (confirm ("<siga:Idioma key='messages.factSJCS.error.pagoExcedido'/>") == false){
					    fin();
					   	return false;
				   	}
				} 			
				
				//Convierte a formato java los campos de tipo precio
				actualizarCamposPrecio();
				
				//if (validarPorcentajes(f)){
					f.submit();
				//}else{
				//   var type = '<siga:Idioma key="messages.factSJCS.error.porcentajeExcedido"/>';
				//	confirm(type);
				//}	
					
				//Dejoel formato con los 2 decimales y la coma:
				formatearCamposprecio();
			}else{
			
				fin();
				return false;
			
			}
		}
		
		function accionRestablecer() 
		{		
			document.getElementById("datosGeneralesPagoForm").reset();		
		}
		
		function accionCerrarPago() 
		{		
			//Convierte a formato java los campos de tipo precio
			actualizarCamposPrecio();
		
			var f=document.getElementById("datosGeneralesPagoForm");
			<%if (cobroAutomatico.equalsIgnoreCase("si")){%>
				f.modo.value = "cerrarPago";
			<%}else{%>
				f.modo.value = "mostrarColegiadosAPagar";
			<%}%>

			var fname = document.getElementById("datosGeneralesPagoForm").name;
			// con pantalla de espera
			<%if (cobroAutomatico.equalsIgnoreCase("si")){%>
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoPago';
			<%}else{%>
				f.target="submitArea";
				var resultado=ventaModalGeneral(fname,"G");
				if(resultado=='MODIFICADO') refrescarLocal();
			<%}%>
			
			//Dejoel formato con los 2 decimales y la coma:
			formatearCamposprecio();
		}
		
		function accionDefinirCriterio(){
			var f=document.getElementById("datosGeneralesPagoForm");
			f.modo.value = "abrirModal";		
			var salida = ventaModalGeneral(f.name,"M"); 			
			f.modo.value = "modificarPago";
		}
		
		function accionVisualizarCriterios(){
			var f=document.getElementById("datosGeneralesPagoForm");
			f.modo.value = "abrirModal";		
			var salida = ventaModalGeneral(f.name,"M"); 			
			f.modo.value = "modificarPago";
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->
	
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>