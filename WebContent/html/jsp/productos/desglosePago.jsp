<!DOCTYPE html>
<html>
<head>
<!-- desglosePago.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	Integer idInstitucion=Integer.valueOf(user.getLocation());
		
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	
	CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);
	ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	
	
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
		
	double varIvaTotalTarjeta = 0;
	double varPrecioTotalTarjeta = 0;
	
	double varIvaTotalOtro = 0;
	double varPrecioTotalOtro = 0;
	
	String sPrecio;
	String sPeriodicidad;
	
	String pagoConTarjeta = request.getAttribute("PAGOTARJETA")==null?"N":(String)request.getAttribute("PAGOTARJETA");
	boolean visibilidad = true;
	String estilo="box", visibilidadTarjeta="none";
	if (pagoConTarjeta.equals("S")) {
		visibilidad = false;
		visibilidadTarjeta="inline";
		//estilo = "boxConsulta";
	}

	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
 	if (busquedaVolver==null) {
 		busquedaVolver = "volverNo";
	}
	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo titulo="pys.solicitudCompra.cabecera" localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->	
	<script language="JavaScript">			
 		function accionCerrar() {
 			window.top.close();
 		}
 		
	    function validarAnchos() {
	        validarAncho_tarjeta();
	        validarAncho_otro();
	    } 		
	</script>	
</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="mainWorkArea" type="">
  	<html:hidden name="solicitudCompraForm" property="modo" value="" />
  	<html:hidden name="solicitudCompraForm" property="pan" value="" />
  	<html:hidden name="solicitudCompraForm" property="fechaCaducidad" value="" />
  	<input type="hidden" name="pagoConTarjeta" value="<%=pagoConTarjeta%>" />
  
	<table class="tablaTitulo" align="center" cellspacing="0" height="20">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="pys.solicitudCompra.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP((String)request.getAttribute("nombrePersona"))%> &nbsp;&nbsp;		
			</td>
		</tr>
	</table>

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="pys.desgloseCesta.literal.pagoTarjeta"/>
			</td>
		</tr>
	</table>		
					
	<siga:Table
  		name="tarjeta"
  		border="2"
  		columnNames="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva"  
   		columnSizes="50,20,20,10"
   		fixedHeight="42%">
<%				
		boolean tieneArticulo = false;
		for (int i = 0; i < arrayListaArticulosOrdenada.size(); i++) {
			Articulo a = (Articulo) arrayListaArticulosOrdenada.get(i);
			a.getIdFormaPago();													
			if (a.getIdFormaPago() != null && a.getIdFormaPago().intValue() == tarjeta) {			
				double precio = a.getPrecio().doubleValue();
				double iva = a.getValorIva().doubleValue();
				varIvaTotalTarjeta = varIvaTotalTarjeta + UtilidadesNumero.redondea(a.getCantidad() * precio * iva / 100, 2);
				varPrecioTotalTarjeta = varPrecioTotalTarjeta + UtilidadesNumero.redondea(a.getCantidad() * precio * (1 + (iva / 100)), 2);
				
				sPeriodicidad = "";
				sPrecio = "-";
				if(a.getPrecio()!= null) {										
					sPrecio = UtilidadesString.mostrarDatoJSP(a.getPrecio());
					if(a.getClaseArticulo()==Articulo.CLASE_SERVICIO){
						sPeriodicidad = " / " + UtilidadesString.mostrarDatoJSP(a.getPeriodicidad());
					}											
				}
				
				tieneArticulo = true;
%> 											
				<tr class="listaNonEdit">
					<td>
								<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>
								&nbsp;
							<%=UtilidadesString.mostrarDatoJSP(a.getDescripcionPrecio())%>
							</td>					  				
							<td align="right"><%=a.getCantidad()%></td>
							<td align="right"><%=UtilidadesString.formatoImporte(sPrecio)%>&nbsp;&euro;&nbsp;<%=sPeriodicidad%></td>
							<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(iva))%>&nbsp;%</td>					  				
			 	</tr>							
<%						
			}
 		}

		if (!tieneArticulo) {
%>
  			<tr class="notFound">
  				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<%
		}
%>	
  	</siga:Table>		

<%
	varIvaTotalTarjeta = UtilidadesNumero.redondea (varIvaTotalTarjeta, 2);
	varPrecioTotalTarjeta = UtilidadesNumero.redondea (varPrecioTotalTarjeta, 2);
%>
	<table align="center">
		<tr>
			<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>					
			<td class="labelText"><%=UtilidadesString.formatoImporte(varIvaTotalTarjeta)%>&nbsp;&euro;</td>
			<td>&nbsp;&nbsp;</td>
			<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
			<td class="labelText"><%=UtilidadesString.formatoImporte(varPrecioTotalTarjeta)%>&nbsp;&euro;</td>
		</tr>
	 </table>
						 
	<div id="datosTarjeta" style="display:<%=visibilidadTarjeta%>; position:relative;">
		 <table>
			<tr>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nTarjeta"/>&nbsp;(*)</td>
				<td>
					<html:text name="solicitudCompraForm" property="tarjeta1" maxlength="4" size="4" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
					&nbsp;-
					<html:text name="solicitudCompraForm" property="tarjeta2" maxlength="4" size="4" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
					&nbsp;-
					<html:text name="solicitudCompraForm" property="tarjeta3" maxlength="4" size="4" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
					&nbsp;-
					<html:text name="solicitudCompraForm" property="tarjeta4" maxlength="4" size="4" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
				</td>
				<td>&nbsp;&nbsp;</td>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.caducidad"/> (<siga:Idioma key="pys.solicitudCompra.literal.formatoCaducidad"/>)&nbsp;(*)</td>
				<td>
					<html:text name="solicitudCompraForm" property="tarjetaMes" maxlength="2" size="2" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
					&nbsp;/
					<html:text name="solicitudCompraForm" property="tarjetaAnho" maxlength="4" size="4" styleClass="<%=estilo%>" readonly="<%=visibilidad%>" value=""></html:text>
				</td>
			</tr>
		</table>
	</div>
				
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="pys.desgloseCesta.literal.otrosPagos"/>
			</td>
		</tr>
	</table>								
						
	<siga:Table
		name="otro"
		border="2"
		columnNames="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.iva"  
		columnSizes="50,20,20,10"
		fixedHeight="90%">
<%			
		boolean tieneArticulo = false;
		for (int i = 0; i < arrayListaArticulosOrdenada.size(); i++) {
			Articulo a = (Articulo) arrayListaArticulosOrdenada.get(i);		
			a.getIdFormaPago();							
			if (a.getIdFormaPago() == null || a.getIdFormaPago().intValue() != tarjeta) {						
			
				double precio = a.getPrecio().doubleValue();
				double iva = a.getValorIva().doubleValue();
				if(a.getIdFormaPago() != null){
					varIvaTotalOtro = varIvaTotalOtro + UtilidadesNumero.redondea(a.getCantidad() * precio * iva / 100, 2);
					varPrecioTotalOtro = varPrecioTotalOtro + UtilidadesNumero.redondea(a.getCantidad() * precio * (1 + (iva / 100)), 2);
				}
				
				sPeriodicidad = "";
				sPrecio = "-";
				if(a.getPrecio()!= null) {										
					sPrecio = UtilidadesString.mostrarDatoJSP(a.getPrecio());
					if(a.getClaseArticulo()==Articulo.CLASE_SERVICIO){
						sPeriodicidad = " / " + UtilidadesString.mostrarDatoJSP(a.getPeriodicidad());
					}											
				}
				
				tieneArticulo = true;
	%> 											
				<tr class="listaNonEdit">
					<td>
	  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>
	  					&nbsp;
 							<%=UtilidadesString.mostrarDatoJSP(a.getDescripcionPrecio())%>
	  				</td>					  				
	  				<td align="right">
	  					<%=a.getCantidad()%>
	  				</td>
	  				<td align="right">
	  					<%=UtilidadesString.formatoImporte(sPrecio)%>&nbsp;&euro;&nbsp;<%=sPeriodicidad%>
	  				</td>
	  				<td align="right">
	  					<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(iva))%>&nbsp;%  
	  				</td>					  				
 				</tr>								
<%							
			}
		}

		if (!tieneArticulo) {
%>
	  		<tr class="notFound">
	  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<%
		}
%>
	</siga:Table>					

	<div style="position:absolute;bottom:30px;width:100%; height:60; z-index:2;" align="center">
<%
		varIvaTotalOtro = UtilidadesNumero.redondea (varIvaTotalOtro, 2);
		varPrecioTotalOtro = UtilidadesNumero.redondea (varPrecioTotalOtro, 2);
%>
		<table>
			<tr>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>					
				<td class="labelText"><%=UtilidadesString.formatoImporte(varIvaTotalOtro)%>&nbsp;&euro;</td>
				<td>&nbsp;&nbsp;</td>
				<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
				<td class="labelText"><%=UtilidadesString.formatoImporte(varPrecioTotalOtro)%>&nbsp;&euro;</td>
			</tr>
		</table>
	</div>		
		
	<siga:ConjBotonesAccion botones="V,FC" clase="botonesDetalle"/>
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
	
	<script>
			function pagoTarjeta() {
				return (document.forms[0].pagoConTarjeta.value == "S");
			}
			
			function validarDatos() {
		 		var f = document.forms[0];
				var valido = true;

		 		if (f.tarjeta1.value!="" && f.tarjeta2.value!="" && f.tarjeta3.value!="" && f.tarjeta4.value!="") {
	 				if (f.tarjetaAnho.value=="" || f.tarjetaMes.value=="") {
				 		alert('<siga:Idioma key="pys.desgloseCesta.literal.avisoFechaTarjeta"/>');
		 				valido = false;		
	 				}
		 		} else {
			 		alert('<siga:Idioma key="pys.desgloseCesta.literal.avisoTarjeta"/>');
		 			valido = false;
		 		}
				return valido;
			}
	
	 		function accionfinalizarCompra() {
				sub();
		 		var f = document.forms[0];
	 			if (pagoTarjeta()) {
	 				if (validarDatos()) {
		 				var pan = f.tarjeta1.value + f.tarjeta2.value + f.tarjeta3.value + f.tarjeta4.value;
		 				var caducidad = f.tarjetaAnho.value + f.tarjetaMes.value;
		 			    f.pan.value = pan;
		 				f.fechaCaducidad.value = caducidad;
			 			f.modo.value = "pagarConTarjeta";
			 			f.submit();	 		
					
		 			}else{
					  fin();
					} 
	 			} else {
		 			f.modo.value = "finalizarCompra";
		 			f.submit();	 								
	 			} 
			}
			
			function accionVolver() {
				f = document.solicitudCompraForm;
				f.action = f.action + "?noReset=true";
				f.modo.value = "abrirAlVolverConf";	
				f.submit();
			}			
 	</script>			
</html:form>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>