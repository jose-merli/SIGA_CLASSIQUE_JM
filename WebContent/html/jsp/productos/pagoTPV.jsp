<!-- pagoTPV.jsp -->
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
<%@ page import="com.siga.general.Firma"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import = "com.atos.utils.ClsConstants"%>

<!-- JSP -->
<% String app=request.getContextPath(); %>

<%
	//Recuperamos los datos necesarios:
	String clave = (String)request.getAttribute(ClsConstants.PASSWORD_FIRMA);
	String merchantId = (String)request.getAttribute(ClsConstants.MERCHANTID);
	String acquiredBin = (String)request.getAttribute(ClsConstants.ACQUIREBIN);
	String terminalId = (String)request.getAttribute(ClsConstants.TERMINALID);
	String urlTPV = (String)request.getAttribute(ClsConstants.URL_TPV);
	String operacion = (String)request.getAttribute("OPERACION");
	String importe = (String)request.getAttribute("IMPORTE");
	String pan = (String)request.getAttribute("PAN");
	String fechaCaducidad = (String)request.getAttribute("CADUCIDAD");
	String urlOK = (String)request.getAttribute("URLOK");
	String urlKO = (String)request.getAttribute("URLKO");
	String exponente = (String)request.getAttribute("EXPONENTE");
	String moneda = (String)request.getAttribute("MONEDA");
	String descripcion = (String)request.getAttribute("DESCRIPCION");

	//---------------------------------------------------
	//TRATAMIENTO DE LA FIRMA
	//
	//Calculo la firma:
	String valorFirma = "";
	Firma firma = new Firma();
	firma.setClave(clave);
	firma.setMerchantId(merchantId);
	firma.setAcquirerBin(acquiredBin);
	firma.setTerminalId(terminalId);
	firma.setOperacion(operacion);
	firma.setImporte(importe);
	firma.setMoneda(moneda);
	firma.setExponente(exponente);
	firma.setReferencia("");
	firma.setFirma(firma.calcularFirma());
%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function iniciarTPV(){			
			document.getElementById("tpv").submit();
		}
	</script>	
</head>

<body onload="iniciarTPV()">
<!-- FORMULARIO PARA EL TPV -->
<form name="tpv" id="tpv" action="<%=urlTPV%>" method="POST" enctype="application/x-www-form-urlencoded">
	<input type="hidden" name="MerchantID" value="<%=firma.getMerchantId()%>">
	<input type="hidden" name="AcquirerBIN" value="<%=firma.getAcquirerBin()%>">
	<input type="hidden" name="TerminalID" value="<%=firma.getTerminalId()%>">
	<!-- CAMPOS DEPENDIENTES DE LA OPERACION -->
	<input type="hidden" name="Num_operacion" value="<%=firma.getOperacion()%>">
	<input type="hidden" name="Importe" value="<%=firma.getImporte()%>">
	<input type="hidden" name="PAN" value="<%=pan%>">
	<input type="hidden" name="Caducidad" value="<%=fechaCaducidad%>">
	<!-- FIN CAMPOS DEPENDIENTES DE LA OPERACION -->
	<input type="hidden" name="TipoMoneda" value="<%=firma.getMoneda()%>">
	<input type="hidden" name="Exponente" value="<%=firma.getExponente()%>">
	<input type="hidden" name="URL_OK" value="<%=urlOK%>">
	<input type="hidden" name="URL_NOK" value="<%=urlKO%>">
	<input type="hidden" name="Firma" value="<%=firma.getFirma()%>">
	<input type="hidden" name="Idioma" value="1">
	<input type="hidden" name="Pago_soportado" value="SSL">
	<input type="hidden" name="Pago_elegido" value="SSL">
	<input type="hidden" name="Descripcion" value="<%=descripcion%>">
</form>
</body>
</html>