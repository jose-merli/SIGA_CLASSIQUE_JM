<!DOCTYPE html>
<html>
<head>
<!-- facturaLineasDatos.jsp -->

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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacLineaFacturaBean"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 

	FacLineaFacturaBean linea  = (FacLineaFacturaBean) request.getSession().getAttribute("DATABACKUP");
	String modo                = (String) request.getAttribute("modo");
	Boolean bEditarDescripcion = (Boolean) request.getAttribute("editarDescripcion");
	Boolean bEditarPrecio      = (Boolean) request.getAttribute("editarPrecio");
	Boolean bEditarIVA         = (Boolean) request.getAttribute("editarIVA");

	boolean readOnly = false;
	boolean readOnlyDes = true;
	boolean readOnlyPre = true;
	boolean readOnlyIva = true;
	// Hay que preparar un readOnly por caja.
	String claseEditarDes    = "boxConsulta";
	String claseEditarPrecio = "boxConsultaNumber";
	String claseEditarIVA    = "boxConsultaNumber";

	String botones = "C,Y";
	if ((modo != null) && (modo.equalsIgnoreCase("ver"))) {
		botones = "C";
		readOnly = true;
	} else {
		if ((bEditarDescripcion != null) && (bEditarDescripcion.booleanValue())) { claseEditarDes    = "box"; readOnlyDes=false; }
		if ((bEditarPrecio != null)      && (bEditarPrecio.booleanValue()))      { claseEditarPrecio = "box"; readOnlyPre=false; }
		if ((bEditarIVA != null)         && (bEditarIVA.booleanValue()))         { claseEditarIVA    = "box"; readOnlyIva=false; }
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="GestionarFacturaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		// JPT: Funcion que quita todos los puntos de millar y pone los decimales correspondientes
		function formatNumber(valorNumero) {
			if (!valorNumero) {
				valorNumero = 0;
				
			} else {
				valorNumero = valorNumero.replace(",", ".");
				
				while (valorNumero.toString().indexOf(".", 0) > 0 && valorNumero.toString().length - valorNumero.toString().indexOf(".", 0) > 3) {
					valorNumero = valorNumero.replace(".", "");
				}
			
				if (valorNumero.toString().indexOf(".", 0) == -1) {
					valorNumero += ".00";
					
				} else if (valorNumero.toString().charAt(valorNumero.toString().length -1) == ".") {
					valorNumero += "00";
					
				} else if (valorNumero.toString().charAt(valorNumero.toString().length -2) == ".") {
					valorNumero += "0";
				}				
			}
			
			return valorNumero;
		}
	
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
	
		function validarPrecios(){
			document.GestionarFacturaForm.datosLineaPrecio.value=formatNumber(document.GestionarFacturaForm.datosLineaPrecio.value);
			document.GestionarFacturaForm.datosLineaIVA.value=formatNumber(document.GestionarFacturaForm.datosLineaIVA.value);		
			document.GestionarFacturaForm.datosLineaTotalNeto.value=formatNumber(document.GestionarFacturaForm.datosLineaTotalNeto.value);			
			document.GestionarFacturaForm.datosLineaTotalIVA.value=formatNumber(document.GestionarFacturaForm.datosLineaTotalIVA.value);
			document.GestionarFacturaForm.datosLineaTotal.value=formatNumber(document.GestionarFacturaForm.datosLineaTotal.value);					
		}	

		// Asociada al boton Volver
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {			
			if (validateGestionarFacturaForm(document.GestionarFacturaForm)) {
				validarPrecios();
				document.GestionarFacturaForm.modo.value = "modificar";
				document.GestionarFacturaForm.submit();
				return;			
			}
		}		
		
		function calculaPrecios () {
			if (validateGestionarFacturaForm(document.GestionarFacturaForm)) {
				var precio = formatNumber(document.GestionarFacturaForm.datosLineaPrecio.value);
				var cantidad = document.GestionarFacturaForm.datosLineaCantidad.value;
				
				if (isNaN(precio) || isNaN(cantidad)) {
					document.GestionarFacturaForm.datosLineaTotalNeto.value = "";
					document.GestionarFacturaForm.datosLineaTotalIVA.value = "";
					document.GestionarFacturaForm.datosLineaTotal.value = "";
				
				} else {						
					var calculoTotalNeto = eval(precio) * eval(cantidad);
					calculoTotalNeto =  Math.abs(calculoTotalNeto * 100) / 100;
					document.GestionarFacturaForm.datosLineaTotalNeto.value = calculoTotalNeto;
					document.GestionarFacturaForm.datosLineaTotalNeto.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotalNeto.value);
					
					var iva = formatNumber(document.GestionarFacturaForm.datosLineaIVA.value);
					
					if (isNaN(iva)) {
						document.GestionarFacturaForm.datosLineaTotalIVA.value = "";
						document.GestionarFacturaForm.datosLineaTotal.value = "";
					
					} else {
						var calculoTotalIva = calculoTotalNeto * eval(iva) / 100;
						calculoTotalIva =  Math.abs(calculoTotalIva * 100) / 100;
						document.GestionarFacturaForm.datosLineaTotalIVA.value = calculoTotalIva;
						document.GestionarFacturaForm.datosLineaTotalIVA.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotalIVA.value);
						
						var calculoTotal = calculoTotalNeto + calculoTotalIva;
						document.GestionarFacturaForm.datosLineaTotal.value = calculoTotal;
						document.GestionarFacturaForm.datosLineaTotal.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotal.value);
					}
				}
			}
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.lineasFactura.Datos.Titulo"/></td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<div id="camposRegistro" class="posicionModalMedia" align="center">
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<html:form action="/FAC_LineasFactura.do" method="POST" target="submitArea">
			<html:hidden property="modo" value = ""/>
			<table class="tablaCentralCamposMedia">
				<tr>
					<td>
						<siga:ConjCampos leyenda="facturacion.lineasFactura.Datos.Titulo">
							<table class="tablaCampos" border="0">
								<tr>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Orden"/></td>
									<td class="labelText"><html:text property="datosLineaOrden" styleClass="boxConsulta" value="<%=UtilidadesString.mostrarDatoJSP(linea.getNumeroOrden())%>" readonly="true"/></td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Descripcion"/></td>
									<td class="labelText">
										<html:textarea property="datosLineaDescripcion" styleClass="<%=claseEditarDes%>"  readonly="<%=readOnlyDes%>"
											value="<%=linea.getDescripcion()%>" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
											style="overflow-y:auto; overflow-x:hidden; width:550px; height:50px; resize:none;"/>
									</td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Cantidad"/></td>
									<td class="labelText"><html:text property="datosLineaCantidad" styleClass="boxConsulta" value="<%=UtilidadesString.mostrarDatoJSP(linea.getCantidad())%>" readOnly="true" /></td>
								</tr>
							</table>					
						</siga:ConjCampos>
					</td>
				</tr>
				
				<tr>
					<td>					
						<fieldset>
							<table class="tablaCampos" border="0">
								<tr>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Precio"/></td>
									<td class="labelText">
										<html:text property="datosLineaPrecio" styleClass="<%=claseEditarPrecio%>" readonly="<%=readOnlyPre%>" style="text-align:right" maxlength="13"
											value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(linea.getPrecioUnitario().doubleValue()))%>" onChange="calculaPrecios();"/>&nbsp;&euro;
									</td>

									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.TotalNeto"/></td>
									<td class="labelText"><html:text property="datosLineaTotalNeto" styleClass="boxConsultaNumber" value="" readonly="true"/>&nbsp;&euro;</td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.IVA"/></td>
									<td class="labelText">
										<html:text property="datosLineaIVA" styleClass="<%=claseEditarIVA%>" readonly="<%=readOnlyIva%>" style="text-align:right" maxlength="5" 										
											value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(linea.getIva().doubleValue()))%>" onChange="calculaPrecios();"/>&nbsp;%
									</td>

									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.importeIVA"/></td>
									<td class="labelText"><html:text property="datosLineaTotalIVA" styleClass="boxConsultaNumber" value="" readonly="true"/>&nbsp;&euro;</td>
								</tr>

								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td colspan="2"><hr size="1"></td>
								</tr>
								
								<tr>						
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Total"/></td>
									<td class="labelText"><html:text property="datosLineaTotal" styleClass="boxConsultaNumber" value="" readonly = "true"/>&nbsp;&euro;</td>
								</tr>

								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Anticipado"/></td>
									<td class="labelText"><html:text property="datosLineaAnticipado" styleClass="boxConsultaNumber" value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(linea.getImporteAnticipado().doubleValue()))%>" readonly = "true"/>&nbsp;&euro;</td>
								</tr>
							</table>
						</fieldset>					
					</td>
				</tr>
			</table>
		</html:form>
		<!-- FIN: CAMPOS -->
	
		<script>			
			calculaPrecios();
		</script>

		<siga:ConjBotonesAccion botones='<%=botones%>' modo='' modal="M" />
	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>