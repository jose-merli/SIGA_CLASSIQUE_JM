<!-- consultaPagoImportesTotales.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoMovimientosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	String nombreInstitucion     = (String)request.getAttribute("nombreInstitucion");
	Hashtable datosConcepto      = (Hashtable) request.getAttribute("datosConcepto");
	Hashtable datosFacturacion   = (Hashtable) request.getAttribute("datosFacturacion");
	Hashtable datosPagoRealizado = (Hashtable) request.getAttribute("datosPagoRealizado");
	Hashtable datosPagoActual    = (Hashtable) request.getAttribute("datosPagoActual");
%>

<html>
<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.pagos.importesTotales" 
		localizacion="factSJCS.Pagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DatosDetallePagoForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	<script language="JavaScript">	
	
		function refrescarLocal() 
		{
			parent.buscar();
		}
	</script>
</head>

<body class="tablaCentralCampos">

		<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosPagos.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>

	<table border="1" width="100%" cellspacing='0' cellpadding='0'>
		<tr>
			<td width="25%" class="tableTitle"><siga:Idioma key="factSJCS.Pagos.importesTotales.literal.concepto"/></td>
			<td width="25%" class="tableTitle"><siga:Idioma key="factSJCS.Pagos.importesTotales.literal.facturacion"/></td>
			<td width="25%" class="tableTitle"><siga:Idioma key="factSJCS.Pagos.importesTotales.literal.pagosRealizados"/></td>
			<td width="25%" class="tableTitle"><siga:Idioma key="factSJCS.Pagos.importesTotales.literal.pagoActual"/></td>
		</tr>
		<tr>
			<td class="labelText">
			<siga:Idioma key='<%=(datosConcepto==null?"":UtilidadesHash.getString(datosConcepto, "" + ClsConstants.HITO_GENERAL_TURNO))%>'/>
			</td>
			<td class="labelTextNum">
			<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosFacturacion, FcsFacturacionJGBean.C_IMPORTEOFICIO)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoRealizado==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoRealizado, FcsPagosJGBean.C_IMPORTEOFICIO)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoActual==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoActual, FcsPagosJGBean.C_IMPORTEOFICIO)),2)))%>&nbsp;&euro;
			</td>
		</tr>
		<tr>
			<td class="labelText">
			<siga:Idioma key='<%=(datosConcepto==null?"":UtilidadesHash.getString(datosConcepto, "" + ClsConstants.HITO_GENERAL_GUARDIA))%>'/>
			</td>	
			<td class="labelTextNum">
			<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosFacturacion, FcsPagosJGBean.C_IMPORTEGUARDIA)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoRealizado==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoRealizado, FcsPagosJGBean.C_IMPORTEGUARDIA)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoActual==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoActual, FcsPagosJGBean.C_IMPORTEGUARDIA)),2)))%>&nbsp;&euro;
			</td>
		</tr>
		<tr>
			<td class="labelText">
			<siga:Idioma key='<%=(datosConcepto==null?"":UtilidadesHash.getString(datosConcepto, "" + ClsConstants.HITO_GENERAL_SOJ))%>'/>
			</td>
			<td class="labelTextNum">
			<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosFacturacion, FcsFacturacionJGBean.C_IMPORTESOJ)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoRealizado==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoRealizado, FcsPagosJGBean.C_IMPORTESOJ)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoActual==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoActual, FcsPagosJGBean.C_IMPORTESOJ)),2)))%>&nbsp;&euro;
			</td>
		</tr>
		<tr>
			<td class="labelText">
			<siga:Idioma key='<%=(datosConcepto==null?"":UtilidadesHash.getString(datosConcepto, "" + ClsConstants.HITO_GENERAL_EJG))%>'/>
			</td>
			<td class="labelTextNum">
			<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosFacturacion, FcsFacturacionJGBean.C_IMPORTEEJG)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoRealizado==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoRealizado, FcsPagosJGBean.C_IMPORTEEJG)),2)))%>&nbsp;&euro;
			</td>
			<td class="labelTextNum">
			<%=(datosPagoActual==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoActual, FcsPagosJGBean.C_IMPORTEEJG)),2)))%>&nbsp;&euro;
			</td>
		</tr>
	</table>

	<div style="position:absolute;width:100%;bottom:50px;left:0px;">
	<table border="0" width="100%">
		<tr>
			<td width="25%" class="labelTextNum">&nbsp;</td>
			<td width="25%" class="labelTextNum">
				<B>
						<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;&nbsp;
						<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosFacturacion, FcsFacturacionJGBean.C_IMPORTETOTAL)),2)))%>&nbsp;&euro;
				</B>
			</td>
			<td width="25%" class="labelTextNum">
				<B>
					<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;&nbsp;
					<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoRealizado, FcsPagosJGBean.C_IMPORTEPAGADO)),2)))%>&nbsp;&euro;
				</B>
			</td>
			<td width="25%" class="labelTextNum">
				<B>
					<siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;&nbsp;
					<%=(datosFacturacion==null?"":UtilidadesString.formatoImporte(UtilidadesNumero.redondea(new Double(UtilidadesHash.getString(datosPagoActual, FcsPagosJGBean.C_IMPORTEPAGADO)),2)))%>&nbsp;&euro;
				</B>
			</td>
		</tr>
	</table>
	</div>

		<!-- FIN: LISTA DE VALORES -->

		
	<siga:ConjBotonesAccion botones="V"/>
	
	<!-- FORMULARIO INICIAL -->
	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainWorkArea">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="abrir" />
	</html:form>
	
	
<script language="JavaScript">
	
	//Asociada al boton Volver
	function accionVolver() 
	{		
		var f = document.getElementById("mantenimientoPagoForm");
		f.target = "mainPestanas";
		f.submit();
	}
		
</script>	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>