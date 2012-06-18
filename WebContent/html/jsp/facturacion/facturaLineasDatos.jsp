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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


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
	}
	else {
		if ((bEditarDescripcion != null) && (bEditarDescripcion.booleanValue())) { claseEditarDes    = "box"; readOnlyDes=false; }
		if ((bEditarPrecio != null)      && (bEditarPrecio.booleanValue()))      { claseEditarPrecio = "box"; readOnlyPre=false; }
		if ((bEditarIVA != null)         && (bEditarIVA.booleanValue()))         { claseEditarIVA    = "box"; readOnlyIva=false; }
	}
	
%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="GestionarFacturaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function validarPrecios(){
			document.GestionarFacturaForm.datosLineaPrecio.value=document.GestionarFacturaForm.datosLineaPrecio.value.replace(/,/,".");
			document.GestionarFacturaForm.datosLineaIVA.value=document.GestionarFacturaForm.datosLineaIVA.value.replace(/,/,".");			
			document.GestionarFacturaForm.datosLineaTotalNeto.value=document.GestionarFacturaForm.datosLineaTotalNeto.value.replace(/,/,".");			
			document.GestionarFacturaForm.datosLineaTotalIVA.value=document.GestionarFacturaForm.datosLineaTotalIVA.value.replace(/,/,".");
			document.GestionarFacturaForm.datosLineaTotal.value=document.GestionarFacturaForm.datosLineaTotal.value.replace(/,/,".");						
		}

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {			
			if (validateGestionarFacturaForm(document.GestionarFacturaForm)) {
				validarPrecios();
				document.GestionarFacturaForm.modo.value = "modificar";
				document.GestionarFacturaForm.submit();
				return;			
			}
		}		
		
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
		
		function calculaPrecios () {
			var totalNeto = document.GestionarFacturaForm.datosLineaCantidad.value * document.GestionarFacturaForm.datosLineaPrecio.value.replace(/,/,".");
			document.GestionarFacturaForm.datosLineaTotalNeto.value = parseInt(totalNeto*100) / 100;
			var totalIVA = document.GestionarFacturaForm.datosLineaTotalNeto.value * document.GestionarFacturaForm.datosLineaIVA.value.replace(/,/,".") /100;
			document.GestionarFacturaForm.datosLineaTotalIVA.value = parseInt(totalIVA*100) / 100;
			document.GestionarFacturaForm.datosLineaTotal.value = parseInt((totalNeto + totalIVA)*100) / 100;
			
			//convierto al formato correcto:
			document.GestionarFacturaForm.datosLineaTotalNeto.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotalNeto.value);
			document.GestionarFacturaForm.datosLineaTotalIVA.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotalIVA.value); 
			document.GestionarFacturaForm.datosLineaTotal.value = convertirAFormato(document.GestionarFacturaForm.datosLineaTotal.value);
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
		
		<html:hidden property = "modo" 					value = ""/>
		<table class="tablaCentralCamposMedia">
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.lineasFactura.Datos.Titulo">
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Orden"/></td>
							<td class="labelText">
									<html:text property="datosLineaOrden" styleClass="boxConsulta" value="<%=UtilidadesString.mostrarDatoJSP(linea.getNumeroOrden())%>" readonly = "true"/>
							</td>
						</tr>

						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Descripcion"/></td>
							<td class="labelText" colspan="3">
								<html:textarea property="datosLineaDescripcion" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" cols="75" rows="2" styleClass="<%=claseEditarDes%>" value="<%=linea.getDescripcion()%>" readonly="<%=readOnlyDes%>" style="overflow:hidden"/>
							</td>
						</tr>

						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Cantidad"/></td>
							<td class="labelText">
								<html:text property="datosLineaCantidad" styleClass="boxConsulta" value="<%=UtilidadesString.mostrarDatoJSP(linea.getCantidad())%>" readOnly="true" />
							</td>
						</tr>
					</table>
					</siga:ConjCampos>
					<br>

					<fieldset>
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Precio"/></td>
							<td class="labelText">
								<html:text property="datosLineaPrecio" styleClass="<%=claseEditarPrecio%>" value="<%=UtilidadesNumero.formatoCampo(String.valueOf(linea.getPrecioUnitario()))%>" readonly="<%=readOnlyPre%>" onChange="calculaPrecios();"/>
								&nbsp;&euro;
							</td>
							

							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.TotalNeto"/></td>
							<td class="labelText">
								<html:text property="datosLineaTotalNeto" styleClass="boxConsultaNumber" value="" readonly = "true"/>
								&nbsp;&euro;
							</td>
						</tr>

						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.IVA"/></td>
							<td class="labelText">
								<html:text property="datosLineaIVA" styleClass="<%=claseEditarIVA%>" value="<%=UtilidadesNumero.formatoCampo(String.valueOf(linea.getIva()))%>" readonly="<%=readOnlyIva%>" onChange="calculaPrecios();"/>
							</td>

							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.importeIVA"/></td>
							<td class="labelText">
								<html:text property="datosLineaTotalIVA" styleClass="boxConsultaNumber" value="" readonly = "true"/>
								&nbsp;&euro;
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td><td>&nbsp;</td><td colspan="2"><hr align="left" width="100%" size="1" class="boxConsultaNumber"></td>
						<tr>
						
							<td>&nbsp;</td><td>&nbsp;</td>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Total"/></td>
							<td class="labelText">
								<html:text property="datosLineaTotal" styleClass="boxConsultaNumber" value="" readonly = "true"/>
								&nbsp;&euro;
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td><td>&nbsp;</td>
							<td class="labelText"><siga:Idioma key="facturacion.lineasFactura.literal.Anticipado"/></td>
							<td class="labelText">
									<html:text property="datosLineaAnticipado" styleClass="boxConsultaNumber" value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(linea.getImporteAnticipado().doubleValue()))%>" readonly = "true"/>
									&nbsp;&euro;
							</td>
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