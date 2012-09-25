<!-- pagoContabilidad.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	// Obtenemos los datos del pago.
	String idInstitucion 	= (String) String.valueOf(request.getAttribute("idInstitucion"));
	String idFactura 		= (String) request.getAttribute("idFactura");
	String idPagoPorCaja 	= (String) String.valueOf(request.getAttribute("idPagoPorCaja"));
	String numeroFactura 	= (String) request.getAttribute("numeroFactura");
	String importe 			= (String) String.valueOf(request.getAttribute("importe"));
	// Creamos la fecha actual
	java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
	String fecha = formador.format(new Date());
	
%>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script>
			function fLoad()
			{
				document.getElementById('id1').focus();
			}
		</script>
	</head>
	<% 	
		String nC="";
		String tC="";
		String botones="";
	  	nC="facturacion.pagoContabilidad.literal.concepto,facturacion.pagoContabilidad.literal.fecha,facturacion.pagoContabilidad.literal.importe";
		tC="50,20,20";
	%>
<body class="tablaCentralCampos" onLoad="fLoad();">

	<html:form action="/FAC_PagosFacturaPorTarjeta" method="post" style="">
		<input type="hidden" name="modo" value="nuevo"/>
		<html:hidden property = "idInstitucion" value="<%=idInstitucion%>" />
		<html:hidden property = "idFactura" 	value="<%=idFactura%>" />
		<html:hidden property = "idPagoPorCaja" value="<%=idPagoPorCaja%>" />
		<html:hidden property = "fecha" value="<%=fecha%>" />
		<html:hidden property = "numeroFactura" value="<%=numeroFactura%>" />
		<html:hidden property = "datosPagosCajaImportePendiente" value = "<%=importe%>"/>
		<input type="hidden" name="tarjeta" />
		<input type="hidden" name="caducidad" />


			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		
		
		
		<siga:TablaCabecerasFijas 
		   nombre="pago"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   			alto="100%"
		   			ajusteBotonera="true"		
		   
		  >
					<tr class="listaNonEdit"> 
						<td  align="center"><siga:Idioma key="facturacion.pagoContabilidad.literal.pagoPorTarjeta"/><%=numeroFactura%></td>
						<td  align="center"><%=fecha%></td>
						<td  align="center"><%=UtilidadesNumero.formatoCampo(importe)%></td>
					</tr> 
		</siga:TablaCabecerasFijas>

	<div id="tarjeta" style="top:100px; position:absolute; width:100%;">
		<table align="center" border="0">
		<tr>
			<td class="labelText">
<!--			<siga:Idioma key="facturacion.pagoContabilidad.literal.tarjeta"/>:&nbsp;<input type="text" name="pan1" size="4" maxlength="4" value="5020" class="box" /> -->
<!--			<input type="text" name="pan2" size="4" maxlength="4" value="4700" class="box"/> -->
<!--			<input type="text" name="pan3" size="4" maxlength="4" value="0137" class="box"/> -->
<!--			<input type="text" name="pan4" size="4" maxlength="4" value="0055" class="box"/> -->
			<siga:Idioma key="facturacion.pagoContabilidad.literal.tarjeta"/>:&nbsp;(*)&nbsp;
			<input type="text" id="id1" name="pan1" size="4" maxlength="4" value="" class="box" />
			<input type="text" name="pan2" size="4" maxlength="4" value="" class="box"/>
			<input type="text" name="pan3" size="4" maxlength="4" value="" class="box"/>
			<input type="text" name="pan4" size="4" maxlength="4" value="" class="box"/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="labelText">
			<siga:Idioma key="facturacion.pagoContabilidad.literal.caducidad"/>:&nbsp;
			<siga:Idioma key="facturacion.pagoContabilidad.literal.mes"/>:&nbsp;(*)&nbsp;
			<input type="text" name="cad2" size="2" maxlength="2" value="" class="box" />&nbsp;(MM) 
			&nbsp;
			<siga:Idioma key="facturacion.pagoContabilidad.literal.ano"/>:&nbsp;(*)&nbsp;
			<input type="text" name="cad1" size="4" maxlength="4" value="" class="box" />&nbsp;(YYYY) 
			</td>
		</tr>
		</table>
	</div>
	</html:form>



		<siga:ConjBotonesAccion botones="f,v" clase="botonesDetalle"  />	

	</body>
	<script>
	function accionFinalizar()
	{
		
		// Preparamos la tarjeta y la caducidad.
		if(document.forms[0].pan1.value != '' && document.forms[0].pan2.value != '' && 	document.forms[0].pan3.value != '' && document.forms[0].pan4.value != '')
		{ 
			
			document.forms[0].tarjeta.value = document.forms[0].pan1.value + document.forms[0].pan2.value + document.forms[0].pan3.value + document.forms[0].pan4.value;
			if(document.forms[0].tarjeta.value.length < 16)
			{
				alert("<siga:Idioma key='facturacion.pagoContabilidad.literal.alert1'/>");
				document.forms[0].cad1.focus();
				return;
			}
			
		}
		else
		{
			alert("<siga:Idioma key='facturacion.pagoContabilidad.literal.alert1'/>");
			document.forms[0].pan1.focus();
			return;
		}

		if(document.forms[0].cad1.value != '' && document.forms[0].cad2.value !='')
		{
		
			document.forms[0].caducidad.value = document.forms[0].cad1.value + document.forms[0].cad2.value;
			if(document.forms[0].caducidad.value.length < 6)
			{
				alert("<siga:Idioma key='facturacion.pagoContabilidad.literal.alert2'/>");
				document.forms[0].cad1.focus();
				return;
			}
		}
		else
		{
			alert("<siga:Idioma key='facturacion.pagoContabilidad.literal.alert2'/>");
			document.forms[0].cad1.focus();
			return;
		}
		
		document.forms[0].submit();
		
	}

	function accionVolver()
	{
		history.back();
	}
	</script>
</html>
