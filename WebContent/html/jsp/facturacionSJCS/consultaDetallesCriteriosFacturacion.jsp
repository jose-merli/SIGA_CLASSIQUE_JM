<!DOCTYPE html>
<html>
<head>
<!-- consultaDetallesCriteriosFacturacion.jsp -->

<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getSession().getAttribute("vHito");
	request.getSession().removeAttribute("vHito");
	
	String sRegularizacion = (String) request.getParameter("regularizacion");
	String modo = (String) request.getParameter("modo");
	String idFacturacion = (String) request.getParameter("idFacturacion");
	String idInstitucion = (String) request.getParameter("idInstitucion");
	String estado = (String)ses.getAttribute("estado");
	
	//resultado del importeTotal
	String valorFinal ="";

	//para el importe total de la facturacion
	double importeTotal=0.0;
	
	// Vector con los hitos generales
	Hashtable hitos = request.getSession().getAttribute("hitos") == null?new Hashtable():(Hashtable) request.getSession().getAttribute("hitos");
	
	//recoger de request el vector con los registros resultado
	Vector resultado = request.getSession().getAttribute("resultado") == null?new Vector():(Vector) request.getSession().getAttribute("resultado");
	
	//estado de la factura
	boolean hayDetalle = false;
	
	try {
		if (!modo.equals("nuevo")){
			hayDetalle = ((String)request.getSession().getAttribute("hayDetalle")).equals ("1");
		}
		
	} catch(Exception e) {
		hayDetalle = false;
	}
	
	String prevision = ((String)request.getSession().getAttribute("prevision"));
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		function refrescarLocal(){ 
			parent.buscar();
		}
	</script>
</head>

<body class="tablaCentralCampos">

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="resultado10" style="display:none">
		<html:hidden property = "modo" value = ""/>
	</html:form>	
		
	<!-- Formulario de la lista de detalle multiregistro -->		
<% 
	if (!hayDetalle) { 
%>
		<br>
			<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.factSJCS.error.noExisteDetalleFacturacion"/></p>
		<br>
<% 
	} else {
%>		
		<siga:Table 
			name="tablaDatos"
			border="1"
			columnNames="Concepto,factSJCS.detalleFacturacion.literal.importe"
			columnSizes="70,30"
			modal = "g"	
			modalScroll= "true">
	
			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
						
<%	
			if (resultado==null || resultado.size()==0) { 
%>			
		 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%	
			} else { 
				for (int cont=1;cont<=resultado.size();cont++) {
					FcsFacturacionJGBean fila = (FcsFacturacionJGBean) resultado.get(cont-1);
		
					Double importeOficio =fila.getImporteOficio() == null?new Double(0.0):fila.getImporteOficio();
					Double importeGuardia =fila.getImporteGuardia()== null?new Double(0.0):fila.getImporteGuardia();
					Double importeSOJ =fila.getImporteSOJ()== null?new Double(0.0):fila.getImporteSOJ();
					Double importeEJG =fila.getImporteEJG()== null?new Double(0.0):fila.getImporteEJG();
					importeTotal = fila.getImporteTotal() != null?fila.getImporteTotal().doubleValue():0.0;
%>
			  		<tr>
			  			<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_TURNO))%>"/></td>
						<td class="labelTextNum" style="text-align:right"><%=UtilidadesString.formatoImporte(importeOficio.doubleValue())%>&nbsp;&euro;</td>
					</tr>
						
					<tr>
						<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_GUARDIA))%>"/></td>
						<td class="labelTextNum" style="text-align:right"><%=UtilidadesString.formatoImporte(importeGuardia.doubleValue())%>&nbsp;&euro;</td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_SOJ))%>"/></td>
						<td class="labelTextNum" style="text-align:right"><%=UtilidadesString.formatoImporte(importeSOJ.doubleValue())%>&nbsp;&euro;</td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_EJG))%>"/></td>
						<td class="labelTextNum" style="text-align:right"><%=UtilidadesString.formatoImporte(importeEJG.doubleValue())%>&nbsp;&euro;</td>
					</tr>			
<%		
				} // for
			} // else 
				
			valorFinal = String.valueOf(importeTotal);
%>			
		</siga:Table>
		
		<div style="position:absolute; width:30%; bottom:0px; right:0px;">
			<fieldset>
				<table width="100%">
					<tr>
						<td class="labelTextNum" align="right">
							<b><siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;&nbsp;<%=UtilidadesString.formatoImporte(valorFinal)%> &euro;</b>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
<%
	}
%>
		
	<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>