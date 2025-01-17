<!DOCTYPE html>
<html>
<head>
<!-- datosPagoCajaAbono.jsp -->
<!-- 
	 Muestra el formulario de pago por caja
	 VERSIONES:
		 miguel.villegas 17/03/2005
-->

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
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	// Datos del cliente a visualizar
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	
	String importePendiente=(String)request.getAttribute("PAGOPENDIENTE"); // Obtengo el importe pendiente
	String idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el importe pendiente
	
	String campo = UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"facturacion.abonosPagos.datosPagoAbono.importePago");
	String[] campos = {campo};
	String mensajeInvalido = UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"errors.invalid", campos );

%>


<!-- HEAD -->


		
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="AbonosPagosForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			var datos = new Array();
			window.top.close();
		}	

		<!-- Asociada al boton Pagar -->
		function accionRealizarPago() {
			sub();				
			if (validateAbonosPagosForm(document.AbonosPagosForm)){
				document.forms[0].modo.value="realizarPagoCaja";
				document.forms[0].importe.value=document.forms[0].importe.value.replace(/,/,".");
				var importe = document.forms[0].importe.value
				
				if (isNaN(importe) || parseFloat(importe) == 0){
					alert('<%=mensajeInvalido%>');
					fin();
					return ;					
				}	
				else if (eval(parseFloat(importe)+">"+document.forms[0].pagoPendiente.value)){
					var mensaje='<siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.errorCantidad" />';
					alert(mensaje);
					fin();
					return ;
				}
				else{
					document.forms[0].submit();				
				}
			} else {
				fin();
			}
		}		
		
		function importeFocus() {
			document.getElementById("importe").focus();
		}
		
	</script>	
</head>

<body onload="importeFocus()">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.abonoCaja"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_AbonosPagos.do" method="POST" target="submitArea">
		<html:hidden property ="modo" value = "pagarCaja"/>
		<html:hidden property="idAbono" value="<%=idAbono%>"/>
		<html:hidden property="idFactura" value="<%=idFactura%>"/>
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property="pagoPendiente" value="<%=importePendiente%>"/>
		<html:hidden property="numeroCuenta" value=""/>
		<table  class="tablaCentralCamposPeque"  align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="facturacion.abonosPagos.datosPagoAbono.datosPago">
						<table class="tablaCampos" align="center">	
							<tr>
								<td class="labelText" width="30%">
									<siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.importePago"/>
								</td>
								<td width="70%" class="nonEdit">
									<html:text property="importe" size="11" maxlength="11" styleClass="box" value=""></html:text>&nbsp;&euro;
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>
	<siga:ConjBotonesAccion botones='C,RP' modo=''  modal="P"/>
	<!-- FIN: CAMPOS -->
</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	<script>
	capturarEnter();
	
	function capturarEnter() {
		document.getElementById("importe").onkeypress = submitConTeclaEnter;
	}
	
	function submitConTeclaEnter(){			
		var keycode;
		if (window.event)  {
			keycode = window.event.keyCode;
		}
		if (keycode == 13) {
			if(checkSubmit()){
				document.getElementById("importe").blur();
				accionRealizarPago();
			}
		}
	}
	</script>
</body>
</html>
