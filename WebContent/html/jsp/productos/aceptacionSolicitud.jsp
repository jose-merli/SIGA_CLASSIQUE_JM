<!DOCTYPE html>
<html>
<head>
<!-- aceptacionSolicitud.jsp -->
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
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "java.util.Vector"%>
<%@ page import = "java.util.Hashtable"%>
<%@ page import = "java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	PysProductosInstitucionAdm pysproductosinstitucion=new PysProductosInstitucionAdm(user);
		
	Integer idInstitucion=(Integer)request.getAttribute("idInstitucion");
	boolean esLetrado=user.isLetrado();
	//String deCertificado =    (String)request.getParameter("deCertificado");	
	//boolean esLetrado=false;
	
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	
	CarroCompra carro = (CarroCompra)request.getSession().getAttribute(CarroCompraAdm.nombreCarro);		

	ArrayList arrayListaArticulosOrdenada = carro.getArrayListaArticulosOrdenada();	

	ScsInscripcionGuardiaAdm inscripcionGuardia=new ScsInscripcionGuardiaAdm(user);
	Hashtable cuentaelegida=inscripcionGuardia.obtenerNumCuenta((Long)carro.getIdPersona(),idInstitucion);
	String numcuenta="";
	String idcuenta="";
	//if(cuentaelegida!=null){
		//numcuenta=(String)cuentaelegida.get("NUMCUENTA");
		//idcuenta=(String)cuentaelegida.get("IDCUENTA");
	//}

	String numero = (String)request.getAttribute("numero");
	String busquedaVolver = (String)request.getSession().getAttribute("CenBusquedaClientesTipo");
 	if (busquedaVolver==null) {
		 busquedaVolver = "volverNo";
	}
	
	String formaPago[] = new String[6];
	formaPago[0] = String.valueOf(idInstitucion);
	formaPago[1] = ""+ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA;
	if (esLetrado)	{		
			formaPago[2] = ""+ClsConstants.TIPO_PAGO_INTERNET;
	}else{
			formaPago[2] = ""+ClsConstants.TIPO_PAGO_SECRETARIA;
	}

/*
		formaPago[1] = "'" + ClsConstants.TIPO_PAGO_INTERNET_SECRETARIA + "'";
		if(esLetrado)	{		
				formaPago[2] = "'" + ClsConstants.TIPO_PAGO_INTERNET + "'";
		}else{
				formaPago[2] = "'" + ClsConstants.TIPO_PAGO_SECRETARIA + "'";
		}
*/		
	int factura = ClsConstants.TIPO_FORMAPAGO_FACTURA;
	int tarjeta = ClsConstants.TIPO_FORMAPAGO_TARJETA;
	float varIvaTotal = 0;
	double varPrecioTotal = 0;	
	String parametroFuncion;
	String formaPagoNombre;
	String sIdCuenta;
	String sFormaPago;
	String sTipoCertificado;
	String sIdTipoEnvios;
	String sIdDireccion;
	String sCuenta;	
	String sCantidad;
	String sPrecio;
	String sPeriodicidad;
	String sIva;
	String desactivado = "readOnly";
	String botones = "V,CC";
	String editar;
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	// Modificacion Precio del producto
	GenParametrosAdm admGen = new GenParametrosAdm(user);
	String sModPrecio = admGen.getValor(user.getLocation(), ClsConstants.MODULO_PRODUCTOS, "MODIFICAR_IMPORTE_UNITARIO_PRODUCTOS", "N");
	boolean bModPrecio = UtilidadesString.stringToBoolean(sModPrecio);
	String aprobarSolicitud=admGen.getValor(user.getLocation(), ClsConstants.MODULO_PRODUCTOS, "APROBAR_SOLICITUD_COMPRA", "S");
	bModPrecio &= !esLetrado;
	
	// Devolucion de texto para mostrar en esta pantalla, al intentar continuar
	String textoError = (String)request.getAttribute("textoError");
	if (textoError == null)
		textoError = "";
	request.setAttribute("textoError", "");
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
		// JPT: Funcion que pone muestra un numero con punto en los millares, coma en el simbolo decimal y dos decimales
		function  convertirAFormato(numero){
			var numeroFormateado = numero.replace(",", ".");
			var numeroNumber = new Number(numeroFormateado);
			
			if (isNaN(numeroNumber)) {
				return "";
			}
			
			numeroNumber = Number(numeroNumber.toFixed(2));
			numeroNumber = numeroNumber.toLocaleString();
			
			//Tratamiento decimales
			if (numeroNumber.indexOf(',') < 0) {
				numeroNumber += ',00'; // Si no tiene decimales le pongo dos ceros
			} else {
				if (numeroNumber.indexOf(',') + 3 > numeroNumber.length){
					numeroNumber += '0'; // Si tiene un decimal le pongo otro decimal
				}
			}
			
			return numeroNumber;	
		}		
	
		function editarCertificado(fila) {
			f = document.solicitudCompraForm;
			tipoEnvio =eval("f.certificado" + fila +"_2");	
			direccion =eval("f.certificado" + fila +"_3");
			
			var resultado = ventaModalGeneral("RemitentesForm","G");					
			if (resultado!= undefined && resultado[0]!=undefined) {
				tipoEnvio.value = resultado[12];
				direccion.value = resultado[11];									
			} else {			
				tipoEnvio.value="";
				direccion.value="";		
			}
			f.modo.value = "guardarCertificado";
			f.submit();				
		}
						
		function consultarCertificado(fila) {
			f = document.solicitudCompraForm;
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var j;
			var tabla;
			tabla = document.getElementById('cabecera');
			var flag = true;
			j = 1;
			while (flag) {
			  	var aux = 'certificado' + fila + '_' + j;
			  	var oculto = document.getElementById(aux);
			  	if (oculto == null)  { 
				  	flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
			  	j++;
			}
			datos.value = datos.value + "%"
		  	f.modo.value = "consultarCertificado";
		 	ventaModalGeneral(document.forms[0].name,"M");	  
		 	f.modo.value = "guardarCertificado";
			f.submit(); 			
		}

		function cuentaUnica() {
			// Mostrando mensaje de error si se ha vuelto a cargar la pagina porque habia un error
			if ("<%=textoError%>") {
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.message.subtotalNegativo"/>";
				alert (mensaje);
			}
			
			f = document.solicitudCompraForm;
			
			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
				nofacturable=eval("f.oculto" + j +"_7");
				if(nofacturable.value=="0"){ //En el caso de que sea facturable
					cuenta=eval("f.oculto" + j +"_5");	
					formaPago=eval("f.oculto" + j +"_6");
					numeroCuenta=eval("f.cuenta" + j);				
					pago=eval("f.formaPago" + j);
					cuentaelegida=eval("f.oculto" + j +"_8");	
					idcuenta=eval("f.oculto" + j +"_9");
					if(pago.value==<%=factura%>){	
						if(cuentaelegida.value!=""){
							cuenta.value=idcuenta.value;						
							numeroCuenta.value=cuentaelegida.value;
							document.getElementById("cuenta"+j).value = idcuenta.value;								
						} 
						
					} else {
					   	jQuery("#cuenta"+j).attr("disabled","disabled");
						document.getElementById("cuenta"+j).style.display ='none';						
					}
				} else {
				   	jQuery("#cuenta"+j).attr("disabled","disabled");
					document.getElementById("cuenta"+j).style.display ='none';
				}
			}
 		}
	
		function obtenerCuentaAccion(fila) {
			f = document.solicitudCompraForm;
			cuenta=eval("f.oculto" + fila +"_5");	
			formaPago=eval("f.oculto" + fila +"_6");
			numeroCuenta=eval("f.cuenta" + fila);				
			pago=eval("f.formaPago" + fila);
			cuentaelegida=eval("f.oculto" + fila +"_8");	
			idcuenta=eval("f.oculto" + fila +"_9");
			if (pago.value==<%=factura%>) {	
			   	jQuery("#cuenta"+fila).removeAttr("disabled");
				document.getElementById("cuenta" + fila).style.display ='block';
				alert("Seleccione una Cuenta Bancaria en N� de Cuenta");
				cuenta.value=idcuenta.value;
				numeroCuenta.value=cuentaelegida.value;	
				
				if (document.getElementById("cuenta"+fila).childNodes.length > 2) {
					document.getElementById("cuenta"+fila).value = 0;
				} else {
					document.getElementById("cuenta"+fila).value = idcuenta.value;
				}
				
			} else {
				jQuery("#cuenta"+fila).attr("disabled","disabled");			
				document.getElementById("cuenta" + fila).style.display ='none';
				cuenta.value="";
				numeroCuenta.value="";								
			}	
			formaPago.value=pago[pago.selectedIndex].text; 					   
 		}
		
		function obtenerCuenta(fila) {
			f = document.solicitudCompraForm;
			cuenta=eval("f.oculto" + fila +"_5");	
			formaPago=eval("f.oculto" + fila +"_6");
			numeroCuenta=eval("f.cuenta" + fila);				
			pago=eval("f.formaPago" + fila);
			cuentaelegida=eval("f.oculto" + fila +"_8");	
			idcuenta=eval("f.oculto" + fila +"_9");
			if (pago.value==<%=factura%>) {	
				jQuery("#cuenta"+fila).removeAttr("disabled");
				document.getElementById("cuenta" + fila).style.display ='block';
				cuenta.value=idcuenta.value;
				numeroCuenta.value=cuentaelegida.value;		
				if (document.getElementById("cuenta"+fila).childNodes.length > 2) {
					document.getElementById("cuenta"+fila).value = 0;
				} else {
					document.getElementById("cuenta"+fila).value = idcuenta.value;
				}				
			} else {
				jQuery("#cuenta"+fila).attr("disabled","disabled");		
				document.getElementById("cuenta" + fila).style.display ='none';
				cuenta.value="";
				numeroCuenta.value="";								
			}	
			formaPago.value=pago[pago.selectedIndex].text; 					   
 		}
		
		function errorValidarCantidad (valor) {
			// No es valido si es nulo
			// No es valido si no tiene longitud
			// No es valido si tiene blancos
			// No es valido si no es numero
			// No es valido si es menor o igual que cero
			// No es valido si tiene algun punto o coma
			if (valor==null || valor.value==null || valor.value=='' || valor.value.indexOf(" ")!=-1)
				return true;
			else {
				var valorFormateado = valor.value;
				valorFormateado = valorFormateado.replace(",", ".");
				if (isNaN(valorFormateado) || eval(valorFormateado)<=0 || valorFormateado.indexOf(".")>-1)
					return true;					
			}
			
			return false;
		}		
		
		function errorValidarPrecio (valor) {
			// No es valido si es nulo
			// No es valido si no tiene longitud
			// No es valido si tiene blancos
			// No es valido si no es numero
			// No es valido si es menor que cero
			// No es valido si tiene varios puntos o comas
			if (valor==null || valor.value==null || valor.value=='' || valor.value.indexOf(" ")!=-1)
				return true;
			else {
				var valorFormateado = valor.value;
				valorFormateado = valorFormateado.replace(",", ".");
				if (isNaN(valorFormateado) || eval(valorFormateado)<0 || (valorFormateado.indexOf(".")!=-1 && valorFormateado.substring(valorFormateado.indexOf(".")+1).length > 2))
					return true;					
			}
			
			return false;
		}		
		
 		function validarCantidadOnBlur(valor) { 		
 			if (errorValidarCantidad(valor)) { 
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.cantidad"/> <siga:Idioma key="messages.campoNoCorrecto.error"/>";
				alert (mensaje);
				valor.focus();
				
			} else {
				calcularImporte();
			}	
		}
 		
 		function validarPrecioOnBlur(valor) {
 			if (errorValidarPrecio(valor)) {
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.precio"/> <siga:Idioma key="messages.campoNoCorrecto.error"/>";
				alert (mensaje);   
				valor.focus();
				
			} else {
				calcularImporte();
			}	
		} 		 	 		
		
		function validarCantidades() {
			f = document.solicitudCompraForm; 	
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				cantidad=eval("f.cantidad" + j);
 				
 				if (errorValidarCantidad(cantidad)) { 
 					nombre = document.getElementById("nombreArticulo" + j);
	 				var mensaje = nombre.value + ": <siga:Idioma key="messages.pys.mantenimientoProductos.errorCantidad"/>";
					alert (mensaje);
 					return false;
				}
	 		}	 		
	 		return true;
	 	}		
		
		function validarPrecios() {
			f = document.solicitudCompraForm; 	
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				precio=eval("f.precio" + j); 				
 				precio.value = precio.value.replace(/,/,".");	
 				
 				tipo = document.getElementById("oculto" + j + "_4");
			    // Si es producto
 				if (tipo.value == <%=Articulo.CLASE_PRODUCTO%>) {
	 				if(errorValidarPrecio(precio)) { 
	 					nombre = document.getElementById("nombreArticulo" + j);
		 				var mensaje = nombre.value + ": <siga:Idioma key="messages.pys.mantenimientoProductos.errorPrecio"/>";
						alert (mensaje);
	 					return false;
					}
					
				// Si es servicio
 				} else {  
					if (precio.value==null || precio.value=="-") { 
		 				var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.errorProductoSinPrecio"/>";
						alert (mensaje);
	 					return false;
					}		
 				}
	 		}	 		
	 		return true;
	 	}
 	
		function obligatorioFormaPago() {
	 		f = document.solicitudCompraForm; 	
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				pago=eval("f.formaPago" + j);
				if (pago.value==null || pago.value=="") { 
 					var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.formaPago"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);	 						
					return false;	 	
				}		
	 		}	 		
	 		return true;
 		}
	 	
	 	function validarFormaPagoANTIGUO(){
	 		f = document.solicitudCompraForm; 
	 		auxTarjeta="N";			
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				pago=eval("f.formaPago" + j);
				if (pago.value==null || pago.value=="") { 				
					var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.formaPago"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);	
					return false;	 	
				}	else if(pago.value==<%=tarjeta%>) {
						auxTarjeta="S";
				}			
	 		}
 			
	 		if (auxTarjeta=="S") {
	 			f.modo.value  = "validarTarjeta"; 		
				var resultado = ventaModalGeneral("solicitudCompraForm","P");
				if (resultado == null) return;
				if (resultado[0] == 0) return;
				var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.conexionTPV"/>";
				alert (mensaje);	 
	 		}
	 		return true;
	 	}	 	   

	 	function validarFormaPago(){	 		
	 		f = document.solicitudCompraForm; 
	 		auxTarjeta="N";	
	
			testigo=0;
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				
				nofacturable=eval("f.oculto" + j +"_7");
				cuentaelegida=eval("f.oculto" + j +"_8");
				cuenta=eval("f.cuenta" + j);
				idcuenta=eval("f.oculto" + j +"_9");
				
				//En el caso de que sea facturable
				if (nofacturable.value=="0") { 
	 				pago=eval("f.formaPago" + j);
	
 					if(pago.value==null || pago.value==""){ 				
 						var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.formaPago"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
						alert (mensaje);	
						return false;	 
						
					} else if(pago.value==<%=tarjeta%>) {
							auxTarjeta="S";
							
					} else if(pago.value==<%=factura%>){
						
						if((testigo==0)&&(cuenta.value==null||cuenta.value=="")){
							obtenerCuentaAccion(j);
							testigo=j;

						} else if(cuenta.value==null||cuenta.value==""){
							//alert("cuenta2="+cuenta.value);
							//alert("cuentaelegida2="+cuentaelegida.value);

							actual=eval("f.cuenta"+j);
							primero=eval("f.cuenta"+testigo)
							actual.value=primero.value;

							actualidcuenta=eval("f.oculto" + j +"_5");
							primeroidcuenta=eval("f.oculto" + testigo +"_5");
							actualidcuenta.value=primeroidcuenta.value;

						}
					}
	   			}	
			}	
 			
			if (testigo!=0) {
				return false;	
			} else {
		 		return true;
		 	}	
	 	}	
	 	
	 	function validarCertificado() {
	 		f = document.solicitudCompraForm; 	 			
	 		salida = true;
 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 				certificado=eval("f.certificado" + j +"_1"); 				
 				if (certificado.value!=null && certificado.value!="") { 			
 					direccion=eval("f.certificado" + j +"_3"); 					
 					if (direccion.value==null || direccion.value=="") {		
 						var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.certificados"/>";
						alert (mensaje);	
						return false;	 	
					}
 					cantidad=eval("f.cantidad" + j); 					
 					if(cantidad.value>1){		
						cantidad.value="1";
						salida = false; 	
					}
				}			
	 		}
 			
			if (!salida) {
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.numeroCertificadosMal"/>";
				alert (mensaje);	
			}
	 		return salida;
	 	}	
	 	
	 	function abrirModal() {
 		 	if(auxTarjeta=="S"){
				f.modo.value  = "validarTarjeta"; 		
				var resultado = ventaModalGeneral("solicitudCompraForm","P");
				if (resultado == null) return;
				if (resultado[0] == 0) return;
				var mensaje = "<siga:Idioma key="messages.pys.solicitudCompra.conexionTPV"/>";
				alert (mensaje);	 
	 		}
	 	}
 		 		
 		function calcularImporte(){ 		
 			f = document.solicitudCompraForm;
 			varIvaTotal=0;
 			varPrecioTotal=0;

 			for (j=1; j < <%=arrayListaArticulosOrdenada.size()%>+1; j++) { 
 			
	 			pago=eval("f.formaPago" + j);
	 			
 				if (pago) {
	 				cantidad=eval("f.cantidad" + j); 		
	 				precio=eval("f.precio" + j);
	 				iva=eval("f.iva" + j);
	 				precio.value = precio.value.replace(/,/,".");
					iva.value = iva.value.replace(/,/,".");
					if (!isNaN(parseFloat(precio.value))) {			
						varIvaTotal = varIvaTotal +  (cantidad.value * parseFloat(precio.value) * (iva.value/100));
						varPrecioTotal = varPrecioTotal + (cantidad.value * (parseFloat(precio.value) * (1 + (iva.value/100))));
					}					
	 			}

 			}
 			f.ivaTotal.value 	= (Math.round(varIvaTotal * 100))/100; 
 			f.precioTotal.value = (Math.round(varPrecioTotal * 100))/100; 
 			
 			f.ivaTotal.value = convertirAFormato(f.ivaTotal.value);
 			f.precioTotal.value = convertirAFormato(f.precioTotal.value); 			
 		}

		//Debe llamarse accionConfirmarCompra
 		function accionConfirmarCompra(){
 			sub();
 			f = document.solicitudCompraForm;
 			if (validarCantidades() && validarPrecios() && validarFormaPago() && validarCertificado()) {							
 				f.target = "mainWorkArea";
				f.modo.value = "confirmarCompra";
				f.submit();
			} else {
				fin();
			}			
 		}
 		
 		function acciondesglosePago(){
 			f = document.solicitudCompraForm; 			
 			if(obligatorioFormaPago()){
 				f.modo.value = "desglosePago"; 				
				var resultado = ventaModalGeneral("solicitudCompraForm","G");			
			}	
 		}
 		
 		function refrescarLocal(){ 	
			f = document.solicitudCompraForm;
 			f.modo.value = "continuar";
 			f.target="mainWorkArea";		
 			f.submit();
		}	
 		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onLoad="cuentaUnica();">

	<table class="tablaTitulo" align="center" cellspacing="0" height="20">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="pys.solicitudCompra.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP((String)request.getAttribute("nombrePersona"))%> &nbsp;&nbsp;		
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="ventana" 				value="aceptacion">
	  	<input type="hidden" name="actionModal" 		value=""> 
	  	<input type="hidden" name="volver" 					value="">
	  	<input type="hidden" name="numeroColegiado" value = "<%=numero%>">
	  	<input type="hidden" name="nombrePersona" 	value = "<%=(String)request.getAttribute("nombrePersona")%>">
	  	<input type="hidden" name="nif" 						value = "<%=(String)request.getAttribute("nif")%>">  
	  	<input type="hidden" name="idInstitucionPresentador" 	value = "<%=(Integer)request.getAttribute("idInstitucionPresentador")%>">
	  	 
	  	<html:hidden name="solicitudCompraForm" property="modo"/>
	  	
	  	<!-- CAMPO PARA SABER SI ESTAMOS TRATANDO CON EL TPV -->
	  	<html:hidden name="solicitudCompraForm" property = "idPersona" value = "<%=String.valueOf((Long)carro.getIdPersona())%>"/>
	  	<html:hidden name="solicitudCompraForm" property = "idInstitucion" value = "<%=String.valueOf(idInstitucion)%>"/>				
				
<% 
		boolean tieneBotones = false;
		for (int i = 0; !tieneBotones && i < arrayListaArticulosOrdenada.size(); i++) {
			Articulo a = (Articulo) arrayListaArticulosOrdenada.get(i);
			if (a.getTipoCertificado() != null && !String.valueOf(a.getTipoCertificado()).equals("")) {	
				tieneBotones = true;
			}			
		}

		String nombrecol1;
		String tamanoCol1;
		int var=1;

		if (!user.isLetrado()&&aprobarSolicitud.equals("S")) {			
			nombrecol1="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.periodicidad,pys.solicitudCompra.literal.iva,pys.solicitudCompra.literal.fechaEfectiva";  
			tamanoCol1="13,16,19,8,8,7,7,13";
			
		} else {
		   	nombrecol1="pys.solicitudCompra.literal.concepto,pys.solicitudCompra.literal.formaPago,pys.solicitudCompra.literal.nCuenta,pys.solicitudCompra.literal.cantidad,pys.solicitudCompra.literal.precio,pys.solicitudCompra.literal.periodicidad,pys.solicitudCompra.literal.iva";	  				
		   	tamanoCol1="13,16,19,8,8,7,7";
		}
		
		if (tieneBotones) {
			nombrecol1 += ",";
			tamanoCol1 += ",10";
		}
%>

		<siga:Table 
			name="cabecera"
			border="2"
			columnNames="<%=nombrecol1%>"
			columnSizes="<%=tamanoCol1%>"
			fixedHeight="83%">

<% 					
			if(arrayListaArticulosOrdenada == null || arrayListaArticulosOrdenada.size()<1 ) { 	
				botones = "V"; 
%> 	
			  	<tr class="notFound">
	   				<td class="titulitos">
	   					<siga:Idioma key="messages.noRecordFound"/>
	   				</td>
				</tr>
				 						
<% 
			} else {
				botones = "V,CC";
				int fila; 	 								
				for (int i = 0; i < arrayListaArticulosOrdenada.size(); i++) {
					Articulo a = (Articulo) arrayListaArticulosOrdenada.get(i);							

					ArrayList sIdFormaPago = new ArrayList();	
					sPrecio = "-";
					sIva = "-";		
					desactivado = "readOnly";					

					double precio=0;							
					float iva=0;

					sIdCuenta = "";								
					if (a.getIdCuenta()!= null) {
						sIdCuenta = String.valueOf((Integer)a.getIdCuenta());
					}

					sFormaPago = "";							
					if (a.getFormaPago()!= null) {
						sFormaPago = (String)a.getFormaPago();
					}

					sIdTipoEnvios = "";								
					if (a.getIdTipoEnvios()!= null) {
						sIdTipoEnvios = String.valueOf((Integer)a.getIdTipoEnvios());
					}

					sIdDireccion = "";								
					if (a.getIdDireccion()!= null) {
						sIdDireccion = String.valueOf((Long)a.getIdDireccion());
					}
	
					if (a.getIdFormaPago()!= null) {
						sIdFormaPago.add(String.valueOf((Integer)a.getIdFormaPago()));
					}

					sCuenta = "";								
					if (a.getNumeroCuenta()!= null) {
						sCuenta = a.getNumeroCuenta();
					}
							
					sCantidad = Integer.toString(a.getCantidad());						
					sPeriodicidad = UtilidadesString.mostrarDatoJSP(a.getPeriodicidad());
					if (a.getPrecio()!= null) {
						precio = (double)a.getPrecio().doubleValue();
						sPrecio = a.getPrecio().toString();
						desactivado = "";
					}

					if (a.getValorIva()!=null) {
						iva = (float)a.getValorIva().floatValue();
						sIva = a.getValorIva().toString();
					}
						
					Vector datos=pysproductosinstitucion.obtenerInfoProducto(
						String.valueOf(idInstitucion),
						String.valueOf((Integer)a.getIdTipo()),
						String.valueOf((Long)a.getIdArticulo()),
						String.valueOf((Long)a.getIdArticuloInstitucion()));   							

					String nofacturable="";
					try{
						nofacturable=(String)((Row)datos.get(0)).getRow().get(PysProductosInstitucionBean.C_NOFACTURABLE);
					}catch(Exception e){
						nofacturable="0";
					}

					if (datos == null || (datos!=null && nofacturable.equals(DB_FALSE))) {
						varIvaTotal = varIvaTotal + (a.getCantidad() * ((float) precio * iva / 100));
						varPrecioTotal = varPrecioTotal + (a.getCantidad() * ((float) precio * (1 + iva / 100)));
					}

									
					fila=i+1;	
					parametroFuncion = "obtenerCuenta(" + fila + ")";
					formaPagoNombre = "formaPago" + fila; 	
					
					FilaExtElement[] elems=new FilaExtElement[2];
					sTipoCertificado = "";
					if (a.getTipoCertificado() != null && !String.valueOf(a.getTipoCertificado()).equals("")) {	
						sTipoCertificado = String.valueOf(a.getTipoCertificado());		 
						elems[0]=new FilaExtElement("editarCertificado","editarCertificado",SIGAConstants.ACCESS_READ);
						elems[1]=new FilaExtElement("consultarCertificado","consultarCertificado",SIGAConstants.ACCESS_READ);  
					}
%> 				
					<siga:FilaConIconos fila='<%=String.valueOf(fila)%>' botones='' visibleBorrado="false" visibleConsulta="false" visibleEdicion="false" pintarEspacio="no" elementos='<%=elems%>' clase="listaNonEdit">
						<td title='<%=String.valueOf((Integer)a.getIdTipo())%>-<%=String.valueOf((Long)a.getIdArticulo())%>-<%=String.valueOf((Long)a.getIdArticuloInstitucion())%>'> 													
							<input type='hidden' name='oculto<%=String.valueOf(fila)%>_1' value='<%=String.valueOf((Integer)a.getIdTipo())%>'>	 							
							<input type='hidden' name='oculto<%=String.valueOf(fila)%>_2' value='<%=String.valueOf((Long)a.getIdArticulo())%>'>	
							<input type='hidden' name='oculto<%=String.valueOf(fila)%>_3' value='<%=String.valueOf((Long)a.getIdArticuloInstitucion())%>'>	
							<input type='hidden' name='oculto<%=String.valueOf(fila)%>_4' value='<%=String.valueOf(a.getClaseArticulo())%>'>																
			  				<input type='hidden' name='oculto<%=String.valueOf(fila)%>_5' value='<%=sIdCuenta%>'>
		  					<input type='hidden' name='oculto<%=String.valueOf(fila)%>_6' value='<%=sFormaPago%>'>
		  					<input type='hidden' name='oculto<%=String.valueOf(fila)%>_7' value='<%=nofacturable%>'>
		  					<input type='hidden' name='oculto<%=String.valueOf(fila)%>_8' value='<%=numcuenta%>'>				  					
		  					<input type='hidden' name='oculto<%=String.valueOf(fila)%>_9' value='<%=idcuenta%>'>
		  								  									  					
		  					<input type='hidden' name='certificado<%=String.valueOf(fila)%>_1' value='<%=sTipoCertificado%>'>
		  					<input type='hidden' name='certificado<%=String.valueOf(fila)%>_2' value='<%=sIdTipoEnvios%>'>
		  					<input type='hidden' name='certificado<%=String.valueOf(fila)%>_3' value='<%=sIdDireccion%>'>
			  					
		  					<input type='hidden' name='nombreArticulo<%=String.valueOf(fila)%>' value='<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>'>
				  					
		  					<%=UtilidadesString.mostrarDatoJSP(a.getIdArticuloInstitucionDescripcion())%>
		  					&nbsp;
		  					<%=UtilidadesString.mostrarDatoJSP(a.getDescripcionPrecio())%>
		  				</td>
		  				
		  				<td>	
<%
							formaPago[3] = String.valueOf((Integer)a.getIdTipo());
							formaPago[4] = String.valueOf((Long)a.getIdArticulo());
							formaPago[5] = String.valueOf((Long)a.getIdArticuloInstitucion());	
							
							String tamanoFormaPago = tieneBotones ? "150" : "160";

							if (datos != null) {
								if(nofacturable.equals(DB_FALSE)){
									if (a.getClaseArticulo() == Articulo.CLASE_PRODUCTO) {
%>									
										<siga:ComboBD nombre="<%=formaPagoNombre%>" tipo="cmbFormaPagoProducto" clase="boxCombo"  parametro="<%=formaPago%>" elementoSel ="<%=var%>" accion="<%=parametroFuncion%>" ancho="<%=tamanoFormaPago%>"/>
<%	
									} else {	
%>
										<siga:ComboBD nombre="<%=formaPagoNombre%>" tipo="cmbFormaPagoServicio" clase="boxCombo"  parametro="<%=formaPago%>" elementoSel ="<%=var%>" accion="<%=parametroFuncion%>" ancho="<%=tamanoFormaPago%>"/>	

<%								
									}
								} else{
%>
								   <siga:Idioma key="estados.compra.noFacturable"/>&nbsp;
										
<%								}	
							}
%>				
		  				</td>
		  				
						<td align="center">								
<%							
							String tamanoCuentaCargo = tieneBotones ? "180" : "200";
							String parametro[] = new String[2];
   						 	parametro[0] = carro.getIdPersona().toString();
   						 	parametro[1] = idInstitucion.toString(); 
   						 	String n = "cuenta"+String.valueOf(fila);
   						 	if(sIdCuenta!= null && !sIdCuenta.equals("")){
   						 		ArrayList cuentaSel   = new ArrayList();
								cuentaSel.add(sIdCuenta);
%>	
								<siga:ComboBD nombre="<%=n%>" tipo="cuentaCargo" clase="boxCombo" parametro="<%=parametro%>" ancho="<%=tamanoCuentaCargo%>" readonly="false" elementoSel="<%=cuentaSel%>" />
<%
							} else {
	   							if(cuentaelegida.get("IDCUENTA")!=null && !cuentaelegida.get("IDCUENTA").equals("")){ 
	   						 		ArrayList cuentaSel = new ArrayList();
  									cuentaSel.add(cuentaelegida.get("IDCUENTA"));
%>	
   						 			<siga:ComboBD nombre="<%=n%>" tipo="cuentaCargo" clase="boxCombo" parametro="<%=parametro%>" ancho="<%=tamanoCuentaCargo%>" readonly="false" elementoSel="<%=cuentaSel%>"/>
<%
								} else { 
%>
   						 			<siga:ComboBD nombre="<%=n%>" tipo="cuentaCargo" clase="boxCombo" parametro="<%=parametro%>" ancho="<%=tamanoCuentaCargo%>" readonly="false" />
<%
								} 
							}
%>
						</td>
						
		  				<td align="center">
							<input type='text' name='cantidad<%=String.valueOf(fila)%>' value="<%=sCantidad%>" maxlength="5" class="box" styleClass="box" style="text-align:right;" size="3" <%=desactivado%> onBlur="validarCantidadOnBlur(this)">
		  				</td>
		  				
		  				<td align="center">
							<% // Producto
							   if (a.getClaseArticulo() == Articulo.CLASE_PRODUCTO) {%><% if (bModPrecio) {%>
									<input type='text' name='precio<%=String.valueOf(fila)%>' value="<%=UtilidadesNumero.formatoCampo(sPrecio)%>" class="boxNumber" size="6" onBlur="validarPrecioOnBlur(this)">
								<%} else { %>
									<input type='text' name='precio<%=String.valueOf(fila)%>' value="<%=UtilidadesNumero.formatoCampo(sPrecio)%>" class=listaNonEdit readOnly=true style="border:none; background-color:transparent; width:70px; text-align:right;">
								<%}  %>
							<% }  // Servicio
							   else { %>
									 <input type='text' name='precio<%=String.valueOf(fila)%>' value="<%=UtilidadesNumero.formatoCampo(sPrecio)%>" class=listaNonEdit readOnly=true style="border:none; background-color:transparent; width:70px; text-align:right;">
							<% } %>
             
		  				</td>
		  				
		  				<td>
		  					<%=sPeriodicidad%>
		  				</td>
		  				
		  				<td align="center">
		  					<input type='text' name='iva<%=String.valueOf(fila)%>' value="<%=UtilidadesNumero.formatoCampo(sIva)%>" class=listaNonEdit style="text-align:right;" readOnly=true style="border:none; background-color:transparent" size="2">% 
		  				</td>
		  				
<%
						if (!user.isLetrado()&& aprobarSolicitud.equals("S")) { 			  				
							String fechaEfectiva="fechaEfectivaCompra" +fila;				
%>
			  				<td class="labelText">
								<siga:Fecha nombreCampo="<%=fechaEfectiva%>" valorInicial="<%=fecha%>" readOnly="true" anchoTextField="9"></siga:Fecha>									
							</td>
<% 
						}

						if (tieneBotones) {
							if (elems.length <= 0) { 
%>
								<td></td>
<%
							} else {
								boolean pintarCelda = true;
								int l = 0;
								while (pintarCelda && l < elems.length){
									if (elems[l] != null)
										pintarCelda = false;
									l++;
								}
								if (pintarCelda){
%>
									<td></td>
<%	
								}
							}
						}
%>
					</siga:FilaConIconos>
<%		
				} // for
	 		} // else
%>  			
	  	</siga:Table>
				
		<div id="camposRegistro1" style="position:absolute; width:100%; height:70px; z-index:2; bottom:70px; left:0px" align="center">

<%
			varIvaTotal = UtilidadesNumero.redondea (varIvaTotal, 2);
			varPrecioTotal = UtilidadesNumero.redondea (varPrecioTotal, 2);		
%>		
			<div id="camposRegistro2" style="width:240px" align="center">	
				<fieldset>
					<table>
						<tr>
							<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.totalIVA"/></td>
							<td class="labelTextValue"><input type='text' name='ivaTotal' value="<%=UtilidadesString.formatoImporte(varIvaTotal)%>" class="boxConsultaNumber" readOnly=true size="12">&nbsp;&euro;</td>
						</tr>
						
						<tr>
							<td class="labelText" nowrap><siga:Idioma key="pys.solicitudCompra.literal.total"/></td>
							<td class="labelTextValue"><input type='text' name='precioTotal' value="<%=UtilidadesString.formatoImporte(varPrecioTotal)%>" class="boxConsultaNumber" readOnly=true size="12">&nbsp;&euro;</td>
						</tr>
					</table>
				</fieldset>
			</div>

			<table width="100%" align="center">
				<tr>
					<td class="labelTextCentro" colspan="2" align="center">
						<siga:Idioma key="messages.servicios.precioServicios"/>
					</td>
				</tr>
			</table>
		</div>		
	</html:form>

	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/> 			
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<html:form action="/PYS_SolicitarDireccion.do" 	method="POST" target="submitArea" type="">  	
	  	<input type="hidden" name="idInstitucion" 	value="<%=String.valueOf(idInstitucion)%>">
	  	<input type="hidden" name="idPersona" 		value="<%=String.valueOf((Long)carro.getIdPersona())%>">
	  	<input type="hidden" name="idTipoEnvio" 	value="">
	  	<input type="hidden" name="actionModal" 	value="">
	  	<input type="hidden" name="modo" 			value="buscar">
	</html:form>

	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>

<script>
	function accionVolver() {
		if (validarCantidades() && validarPrecios()) {
			f = document.solicitudCompraForm;
			f.action = f.action + "?noReset=true";
			f.modo.value = "abrirAlVolver";	
			f.submit();
		}	 			
	}
</script>