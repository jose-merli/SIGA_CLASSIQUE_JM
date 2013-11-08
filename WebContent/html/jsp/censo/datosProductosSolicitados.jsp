<!DOCTYPE html>
<html>
<head>
<!-- datosProductosSolicitados.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	boolean bOcultarHistorico = usrbean.getOcultarHistorico();

	ArrayList formaPagoSel = new ArrayList();
	ArrayList nCuentaSel = new ArrayList();

	String formaPagoParam[] = new String[4];
	String nCuentaParam[] = new String[2];

	DatosFacturacionForm formulario = (DatosFacturacionForm)request.getAttribute("datosFacturacionForm");
	nCuentaParam[0] = formulario.getIdPersona();
	nCuentaParam[1] = formulario.getIdInstitucion();

	formaPagoSel.add((String)request.getAttribute("CenDatosIdFormaPagoSel"));
	nCuentaSel.add((String)request.getAttribute("CenDatosIdCuentaSel"));
	String idPeticion = (String)request.getAttribute("CenDatosIdPeticion");
	
	String pagoBancario = new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA).toString();
	
	String numeroColegiado = "";
	boolean bColegiado = false;
	String colegiado = (String) request.getAttribute("CenDatosGeneralesColegiado");
	String cliente = UtilidadesString.getMensajeIdioma(usrbean, colegiado);

	numeroColegiado = (String) request.getAttribute("CenDatosGeneralesNoColegiado");
	if (numeroColegiado!=null) {
		bColegiado=true;
	} 

	String nombreApellidos = (String) request.getAttribute("CenDatosGeneralesNombreApellidos");
	if (nombreApellidos==null) {
		nombreApellidos="";
	} 

	Vector auxFormaPago = (Vector) request.getAttribute("CenDatosFormaPagoProductos");
	for (int k=0;k<auxFormaPago.size();k++) {
		formaPagoParam[k] = (String)auxFormaPago.get(k);
	}

	String busc = "";

	busc = UtilidadesString.getMensajeIdioma(usrbean,"cen.consultaProductos.titulo1");
	busc += "&nbsp;" + nombreApellidos + "&nbsp;";
	if (bColegiado) { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.colegiado") + " " + numeroColegiado;
	} else { 
		busc += UtilidadesString.getMensajeIdioma(usrbean,"censo.fichaCliente.literal.NoColegiado");
	}  

%>	
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>


<!-- HEAD -->


		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosFacturacionForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

	<script>	
		function verCuentas() {
			if (document.forms[0].formaPago.value == <%=pagoBancario %>) 
			{
				// es pago bancario
				document.forms[0].nCuenta.disabled = false;
			} else {
				// NO es pago bancario
				document.forms[0].nCuenta.disabled = true;
			}
		}
	</script>	
</head>

<body onLoad="verCuentas();">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<%=busc %>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/CEN_Facturacion.do" method="POST" target="submitArea">
	<html:hidden name="datosFacturacionForm"  property = "modo" value = ""/>
	<html:hidden name="datosFacturacionForm"  property = "idInstitucion" />
	<html:hidden name="datosFacturacionForm"  property = "idPersona" />

	<!-- datos modificacion -->
	<html:hidden  name="datosFacturacionForm" property="motivo"/>
	<input type="hidden" name="idFormaPagoCuenta" value="<%=(String)request.getAttribute("CenDatosIdFormaPagoSel")%>" >
	<input type="hidden" name="idTipoProductoSel" value="<%=formaPagoParam[1]%>" >
	<input type="hidden" name="idProductoSel" value="<%=formaPagoParam[2]%>" >
	<input type="hidden" name="idProductoInstitucionSel" value="<%=formaPagoParam[3]%>" >
	<input type="hidden" name="idPeticionSel" value="<%=idPeticion%>" >

	<!-- FILA -->
	<tr>				
	<td>

	<siga:ConjCampos leyenda="cen.consultaProductos.leyenda">

	<table class="tablaCampos" align="center">

	<tr>				
	<td class="labelText">
		<siga:Idioma key="cen.consultaProductos.literal.formaPago"/>
	</td>				
	<td>
		<siga:ComboBD accion="verCuentas();" nombre = "formaPago" tipo="cmbFormaPagoProductoClave" clase="box" obligatorio="true" elementoSel="<%=formaPagoSel %>"  parametro="<%=formaPagoParam %>" obligatorioSinTextoSeleccionar="true"/>
	</td>

	</tr>				

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="cen.consultaProductos.literal.nCuenta"/>
	</td>				
	<td>
		<siga:ComboBD nombre = "nCuenta" tipo="cuentaCargo" clase="box" obligatorio="true" elementoSel="<%=nCuentaSel %>" parametro="<%=nCuentaParam %>" obligatorioSinTextoSeleccionar="true"/>
	</td>

	</tr>

	</table>

	</siga:ConjCampos>


	</td>
	</tr>

	</html:form>
	
	</table>



	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

 
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
		  	sub();
			var banco = false;
		    var pagoDomiciliado = "<%=ClsConstants.TIPO_FORMAPAGO_FACTURA%>";
			if (document.forms[0].formaPago.options[document.forms[0].formaPago.selectedIndex].value == pagoDomiciliado)
				banco = true;

			//Chequeo que si selecciona domiciliacion bancaria debe seleccionar una cuenta:
			if (banco && document.forms[0].nCuenta.options[document.forms[0].nCuenta.selectedIndex].text == ''){
				alert("<siga:Idioma key='pys.solicitudCompra.message.nCuenta'/>");
				fin();
				return false;
			}
			else {
				<% if (!bOcultarHistorico) { %>
						var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
						window.top.focus();
				<% } else { %>
						var datos = new Array();
						datos[0] = 1;
						datos[1] = "";
				<% } %>
				if (datos[0] == 1) { // Boton Guardar
					document.forms[0].motivo.value = datos[1];
					document.forms[0].modo.value="modificarProducto";
					document.forms[0].submit();
				}else{
					fin();
					return false;
				}
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
