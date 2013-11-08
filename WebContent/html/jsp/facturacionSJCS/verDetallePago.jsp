<!DOCTYPE html>
<html>
<head>
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.facturacionSJCS.form.DatosDetallePagoForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	

	//recoger parametros
	Hashtable totales = (Hashtable) request.getAttribute("valoresTotalPagos"); 
	String nombreInstitucion = (String) request.getAttribute("nombreInstitucion"); 

	double totalRepartir = new Double((String)totales.get("totalRepartir")).doubleValue();
	double totalVarios = new Double((String)totales.get("movimientos")).doubleValue();
	double totalCalcular = new Double((String)totales.get("turnos")).doubleValue();
	double totalRetenciones = new Double((String)totales.get("retenciones")).doubleValue();
	
	totalCalcular += new Double((String)totales.get("guardias")).doubleValue();
	totalCalcular += new Double((String)totales.get("ejg")).doubleValue();
	totalCalcular += new Double((String)totales.get("soj")).doubleValue();
	boolean bMensaje = false;
	if (totalCalcular<=totalRepartir && (totalCalcular+totalVarios)>totalRepartir) {
		bMensaje = true;
	}
	
	//Estado del PAgo:
	String estadoPago = (String)request.getAttribute("estadoPago");
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
 	
	<!--TITULO Y LOCALIZACION -->

		
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.datosPagos.titulo1"/><%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
		</td>
	</tr>
	</table>


	<!-- INICIO: CAMPOS -->
	
	<table  class="tablaCentralCamposMedia" >

	<tr>
	<td>
		<siga:ConjCampos leyenda="factSJCS.datosFacturacion.literal.totales">
		<table width="100%"   cellpadding="0"   cellspacing="0">
			<tr>
			<td>
				<table width="400px" cellpadding="0"   cellspacing="0">
					<tr>
						<td class="labelText" width="200">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalTurnos"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("turnos")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalGuardias"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("guardias")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalSOJ"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("soj")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalEJG"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("ejg")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalMovimientosVarios"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("movimientos")%> &euro;
						</td>
					</tr>
					<% if(Integer.parseInt(estadoPago) >= Integer.parseInt(ClsConstants.ESTADO_PAGO_CERRADO)) { %>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosPagos.literal.totalRetenciones"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("retenciones")%> &euro;
						</td>
					</tr>
					<% } %>
				</table>
			</td>
			</tr>
			<tr>
			<td align="left">
				<siga:ConjCampos leyenda="">
				<table width="400"  cellpadding="0"   cellspacing="0">
					<tr>
						<td class="labelText" width="200">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("total")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalIRPF"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("totalIRPF")%> &euro;
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="factSJCS.datosFacturacion.literal.totalRepartir"/>
						</td>
						<td class="labelText" align="right">
							<%=(String)totales.get("totalRepartir")%> &euro;
						</td>
					</tr>
				</table>
				</siga:ConjCampos>
			</td>
			</tr>
			<% if (bMensaje) {  %>
			<tr>
				<td class="labelText" >
					<siga:Idioma key="messages.factSJCS.pagoSuperiorCantidad"/>
				</td>
			</tr>
			<% }  %>
		</table>
    </siga:ConjCampos>	
	</td>
	</tr>

	</table>
	<!-- INICIO: BOTONES REGISTRO -->
	
		<siga:ConjBotonesAccion botones="C" modal="M"/>

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
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
