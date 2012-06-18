<!-- calculoFIRMATPV.jsp -->
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
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
		
	String[] parametros = new String[9];
	String salida = request.getParameter("salida")==null?"":request.getParameter("salida");
	String importe = request.getParameter("importe")==null?"":request.getParameter("importe");
	String operacion = request.getParameter("operacion")==null?"":request.getParameter("operacion");

	//Calculo la ruta del dominio del servidor:
	StringBuffer ruta = request.getRequestURL();
	String sRuta = ruta.substring(0,ruta.lastIndexOf(("SIGA")))+"SIGA/SIGATPVControl.svrl";

		
	try {
			ClsLogging.writeFileLog("------------------------",5);
			ClsLogging.writeFileLog("Simulacion del TPV...",5);
			ClsLogging.writeFileLog("------------------------\n",5);
			
			ClsLogging.writeFileLog("Entrada de datos:",5); 
			ClsLogging.writeFileLog("Clave_encriptacion, merchantid, acquirerbin, terminalid, nºoperacion, importe, tipomoneda, exponente y referencia que en las compras debe ser \"\".\n",5); 
			
			if (importe.equals("") || operacion.equals(""))
				salida="";
			else {
				Firma firma1 = new Firma();
				firma1.setClave("11439044");			
				firma1.setMerchantId("111950028");
				firma1.setAcquirerBin("0000554052");
				firma1.setTerminalId("00000003");
				firma1.setOperacion(operacion);
				firma1.setImporte(importe);
				firma1.setMoneda("978");
				firma1.setExponente("2");
				firma1.setReferencia("22");	
				firma1.setFirma(firma1.calcularFirma());
				salida = firma1.getFirma();
			}
			ClsLogging.writeFileLog("FIRMA="+salida,5);			
			ClsLogging.writeFileLog("------------------------",5);
		} catch (Exception e) {
			salida="";
		}		

%>
<html>

<!-- HEAD -->
<head>

	<title>
		Prueba del TPV: FIRMA y SERVLET
	</title>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<script language="JavaScript">
		function calcular(){		
			alert("Calculo la firma...");
			//Eliminamos blancos:
			document.firma.importe.value = document.firma.importe.value.replace(/ $/,"").replace(/^ /,"").replace(/[ ]+/g,"");
			document.firma.operacion.value = document.firma.operacion.value.replace(/ $/,"").replace(/^ /,"").replace(/[ ]+/g,"");
			document.firma.action = document.location.href;			
			document.firma.submit();
		}

		function probarServlet() {
			alert("Prueba del Servlet del TPV...");
			//Eliminamos blancos:
			document.firma.importe.value = document.firma.importe.value.replace(/ $/,"").replace(/^ /,"").replace(/[ ]+/g,"");
			document.firma.operacion.value = document.firma.operacion.value.replace(/ $/,"").replace(/^ /,"").replace(/[ ]+/g,"");
			document.pruebaServlet.Firma.value = document.pruebaServlet.Firma.value.replace(/ $/,"").replace(/^ /,"").replace(/[ ]+/g,"");
			if (document.pruebaServlet.Firma.value == "")
				alert("Firma vacia. Debe calcularla primero.");
			else
				document.pruebaServlet.submit();
		} 		
	</script>	

</head>

<body bgcolor="#CFCFCF">

		<table align="center" width="80%">
			<tr>
			<form id="firma" name="firma" action="" onsubmit="calcular()">
				<td class="labelText">
					Datos necesarios para calcular la firma:
				</td>
			</tr>
			<tr>
				<td class="labelText">
					IMPORTE:&nbsp;<input type="text" size="60" name="importe" value="<%=importe%>" class="box">
				</td>
			</tr>
			<tr>
				<td class="labelText">
					NUM. OPERACION:&nbsp;<input type="text" size="60" name="operacion" value="<%=operacion%>" class="box">
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<input type="submit" value="Calcular Firma" class="box">
				</td>
			</tr>
		</table>
		
		<br>
		<br>
		
		<table align="center" width="80%">
			<tr>
				<td class="labelText">
					Cálculo de la firma:
				</td>
			</tr>
			<tr>
				<td class="labelText" nowrap>
					FIRMA:&nbsp;<input type="text" size="100" name="salida" value="<%=salida%>" class="box">
				</td>
			</tr>
		</table>
		</form>
		
		<br>
		<br>
		
		<table align="center" width="80%">
			<tr>
				<td class="labelText">
					<b>Prueba del Servlet SIGATPVControl:</b>
					<br>
					Al Submitir este formulario llamamos al Servlet de SIGA que actualizaria en nuestra Base de Datos la Compra.
					<br>
					<u>Este servlet solo se invoca en las compras validadas por CECA.</u>
					
					<FORM id="pruebaServlet" name="pruebaServlet" ACTION="<%=sRuta%>" METHOD="POST" ENCTYPE="application/x-www-form-urlencoded">
					<!-- CAMPOS OCULTOS -->
					<INPUT type="hidden" NAME="MerchantID" VALUE="111950028">
					<INPUT type="hidden" NAME="AcquirerBIN" VALUE="0000554052">
					<INPUT type="hidden" NAME="TerminalID" VALUE="00000003">
					<!-- CAMPOS DEPENDIENTES DE LA OPERACION -->
					<INPUT type="hidden" NAME="Num_operacion" VALUE="<%=operacion%>">
					<INPUT type="hidden" NAME="Importe" VALUE="<%=importe%>">
					<!-- FIN CAMPOS DEPENDIENTES DE LA OPERACION -->
					<INPUT type="hidden" NAME="Tipo_moneda" VALUE="978">
					<INPUT type="hidden" NAME="Exponente" VALUE="2">
					<INPUT type="hidden" NAME="Referencia" VALUE="22">
					<!--PARA PROBAR LO QUE MANDA EL TPV-->					 
					<INPUT type="hidden" NAME="Firma" VALUE="<%=salida%>">
					<INPUT type="hidden" NAME="Num_aut" VALUE="13">
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<!-- BOTON -->
					<INPUT type="button" VALUE="Probar Servlet TPV" onclick="probarServlet()" class="box">
					</FORM>
				</td>
			</tr>
		</table>	

</body>
</html>
