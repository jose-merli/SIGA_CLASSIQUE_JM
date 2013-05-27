<!-- comprobantePago.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	// Obtenemos los datos del pago.
	// Error, NºOperacion, Concepto, Fecha, Importe, descripcion
	String error = (String) String.valueOf(request.getAttribute("ERROR"));
	String tarjeta = (String) String.valueOf(request.getAttribute("TARJETA"));
	String caducidad = (String) String.valueOf(request.getAttribute("CADUCIDAD"));
	String fecha = (String) String.valueOf(request.getAttribute("FECHA"));
	String importe = (String) String.valueOf(request.getAttribute("IMPORTE"));
	String numeroFactura = (String) String.valueOf(request.getAttribute("NUMEROFACTURA"));
	String operacion = (String) String.valueOf(request.getAttribute("OPERACION"));
%>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	</head>
	<% 	
		String nC="";
		String tC="";
		String botones="";
	  	nC="facturacion.pagoContabilidad.literal.concepto,facturacion.pagoContabilidad.literal.fecha,facturacion.pagoContabilidad.literal.importe,facturacion.pagoContabilidad.literal.tarjeta,facturacion.pagoContabilidad.literal.caducidad";
		tC="40,15,15,20,10";
	%>
<body class="tablaCentralCampos">
	<html:form action="/FAC_PagosFacturaPorTarjeta" method="post">
		<input type="hidden" name="modo" value="nuevo"/>

			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
		
		<siga:Table 
		   name="pago"
		   border="2"
		   columnNames="<%=nC%>"
		   columnSizes="<%=tC%>">
					<tr class="listaNonEdit"> 
						<td  align="center"><siga:Idioma key="facturacion.pagoContabilidad.literal.pagoPorTarjeta"/><%=numeroFactura%></td>
						<td  align="center"><%=fecha%></td>
						<td  align="center"><%=importe%></td>
						<td  align="center"><%=tarjeta%></td>
						<td  align="center"><%=caducidad%></td>
					</tr> 
		</siga:Table>
	<div id="tarjeta" style="top:100px; position:absolute; width:100%;">
	<center>
		<%if(error!=null && error.equals("SI")){%>
			<font class="titulitos"><siga:Idioma key="facturacion.pagoContabilidad.literal.operacionError"/></font>
		<%}else{%>
		<font class="titulitos" align="center"><siga:Idioma key="facturacion.pagoContabilidad.literal.operacionExito"/></font>
		<%}%>
		<br/><br/>
		<font class="titulos"><siga:Idioma key="facturacion.pagoContabilidad.literal.operacion"/>:&nbsp;<%=operacion%></font>
	</center>
	</div>
		<siga:ConjBotonesAccion botones="I" clase="botonesDetalle"  />	
	</body>
	<script>
	function accionVolver()
	{
		alert("Por implementar");
	}
	function accionVolver()
	{
		alert("Por implementar");
	}
	function accionImprimir()
	{
		window.print();
 	}
	</script>
</html>
