<!DOCTYPE html>
<html>
<head>
<!-- datosPagoBancoAbono.jsp -->
<!-- 
	 Muestra el formulario de pago por banco
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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	// Datos del cliente a visualizar
	String idAbono=(String)request.getAttribute("IDABONO"); // Obtengo el identificador del abono
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el identificador de la institucion	
	String importePendiente=(String)request.getAttribute("PAGOPENDIENTE"); // Obtengo el importe pendiente	
	String idPersona=(String)request.getAttribute("IDPERSONA"); // Obtengo el identifiador de persona
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

		//Asociada al boton Volver -->
		function accionCerrar(){ 
			var datos = new Array();
			window.top.close();
		}	
		
		//Asociada al boton Pagar -->
		function accionRealizarPago() {
			
			document.forms[0].importe.value=document.forms[0].importe.value.replace(".","");
			if (validateAbonosPagosForm(document.AbonosPagosForm)){
				document.forms[0].modo.value="realizarPagoBanco";
				document.forms[0].importe.value=document.forms[0].importe.value.replace(/,/,".");
				document.forms[0].submit();
			}				

		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.abonoBanco"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_AbonosPagos.do" method="POST">
		<html:hidden property ="modo" value = ""/>
		<html:hidden property="idAbono" value="<%=idAbono%>"/>
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property="pagoPendiente" value="<%=importePendiente%>"/>
		<table  class="tablaCentralCamposPeque"  align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="facturacion.abonosPagos.datosPagoAbono.datosPago">
						<table class="tablaCampos" align="center">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.importePago"/>
								</td>
								<td class="labelText">
									<html:text property="importe" size="11" maxlength="11" styleClass="boxConsultaNumber" value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(importePendiente))%>" readonly="true"></html:text>&nbsp;&euro;
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.nCuenta"/>
								</td>
								<td>
								<bean:define id="listaCuentas" name="listaCuentas" scope="request"/>
								<bean:define id="idCuentaSel" name="idCuentaSel" scope="request"/>
								<html:select styleId="comboCuentas" property="numeroCuenta" value="${idCuentaSel}" styleClass="boxCombo" style="width:200px;" >
								<html:option value=""><c:out value=""/></html:option>
								<c:forEach items="${listaCuentas}" var="cuentasCmb">
									<html:option value="${cuentasCmb.ID}"><c:out value="${cuentasCmb.DESCRIPCION}"/></html:option>
								</c:forEach>
								</html:select>							
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

</body>
</html>
