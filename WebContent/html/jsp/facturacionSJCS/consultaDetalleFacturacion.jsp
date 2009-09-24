<!-- consultaDetalleFacturacion.jsp //-->
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

	//recoger de request el vector con los registros resultado
	Vector resultado = request.getAttribute("resultado") == null?new Vector():(Vector) request.getAttribute("resultado");
	
	// Vector con los hitos generales
	Hashtable hitos = request.getAttribute("hitos") == null?new Hashtable():(Hashtable) request.getAttribute("hitos");

	//campos a mostrar en la tabla
	String nombreColegiado ="", importe="", ncolegiado="", idPersona="";

	//estado de la factura
	String estado = (String)request.getAttribute("estado");
	boolean abierta = false;
	try{
		if (estado.equalsIgnoreCase("abierta"))abierta = true;
	}catch(Exception e){}

	//campos ocultos
	String idInstitucion ="", idFacturacion="";
	try
	{
		idInstitucion = (String)request.getAttribute("idInstitucion");
		idFacturacion = (String)request.getAttribute("idFacturacion");
	}catch(Exception e){}

	//resultado del importeTotal
	String valorFinal ="";

	//el nombre del colegio
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");

	//botones a mostrar
	String botones="";

	//para el importe total de la facturacion
	double importeTotal=0.0;

	// Para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("SJCSBusquedaFacturacionTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	

%>

<html>
<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DatosDetalleFacturacionForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	<script language="JavaScript">	
	
		function refrescarLocal() 
		{
			parent.buscar();
		}

		function accionDetalleFacturacion() 
		{
			document.forms[0].modo.value="descargaFicheros";
			ventaModalGeneral(document.forms[0].name,"P");
		}
	</script>
	<siga:Titulo 
		titulo="factSJCS.mantenimientoFacturacion.detalleFacturacion" 
		localizacion="factSJCS.mantenimientoFacturacion.localizacion"/>
</head>

<body class="tablaCentralCampos">

	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
		<html:form action="/FCS_DetalleFacturacion.do" method="POST" target="submitArea">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "" />
		<html:hidden property = "idFacturacion" value = "<%=idFacturacion%>" />
		<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="factSJCS.datosFacturacion.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
			</td>
		</tr>
	</table>
		
			<!-- Formulario de la lista de detalle multiregistro -->

<% if (abierta) { %>
	<br>
	<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.factSJCS.error.noExisteDetalleFacturacion"/></p>
	<br>
<% } else {%>
			<siga:TablaCabecerasFijas 
			   nombre="tablaDatos"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="Concepto,factSJCS.detalleFacturacion.literal.importe"
			   tamanoCol="70,30"
			   alto="100%"
			   ajuste="120"					   
			   modal = "g"	
			   scrollModal = "true"		  
			   >
	
				<!-- INICIO: ZONA DE REGISTROS -->
				<!-- Aqui se iteran los diferentes registros de la lista -->
				
	<%	if (resultado==null || resultado.size()==0) { %>			
		 		<br>
		   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 		<br>		
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
					<td class="labelTextNum"><%=UtilidadesNumero.formatoCampo(importeOficio.doubleValue())%>&nbsp;&euro;</td>
				</tr>	
				<tr>
					<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_GUARDIA))%>"/></td>
					<td class="labelTextNum"><%=UtilidadesNumero.formatoCampo(importeGuardia.doubleValue())%>&nbsp;&euro;</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_SOJ))%>"/></td>
					<td class="labelTextNum"><%=UtilidadesNumero.formatoCampo(importeSOJ.doubleValue())%>&nbsp;&euro;</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="<%=(String)hitos.get(String.valueOf(ClsConstants.HITO_GENERAL_EJG))%>"/></td>
					<td class="labelTextNum"><%=UtilidadesNumero.formatoCampo(importeEJG.doubleValue())%>&nbsp;&euro;</td>
				</tr>
	
	<%		} // del for
		} // del if 
		valorFinal = String.valueOf(importeTotal);
		%>			
	
			</siga:TablaCabecerasFijas>

			<div style="position:absolute;left:0px;width:100%;bottom:50px">
			<table border="0" width="100%">
				<tr>
					<td height="220px" colspan="2">
					</td>
				</tr>
				<tr>	
					<td width="70%"  align="right" valign="bottom">
					</td>
					<td width="30%" class="labelTextNum" style="text-align:right">
							<fieldset>
							<table align="right" border="0">
							<tr>
							<td class="labelTextNum"  style="text-align:right">
							<b><siga:Idioma key="factSJCS.datosFacturacion.literal.total"/>&nbsp;&nbsp;<%=UtilidadesNumero.formatoCampo(valorFinal)%> &euro;</B>
							</td>
							</tr>
							</table>
							</fieldset>
					</td>
				</tr>
			</table>
			</div>
<%}%>



			<!-- FIN: LISTA DE VALORES -->

<%
	String bot = "V";
	if (!abierta) {
		bot += ",DF";
	}
%>
		<siga:ConjBotonesAccion botones="<%=bot%>" clase="botonesDetalle"/>
		
		<!-- PARA LA FUNCION VOLVER -->
		<%@ include file="/html/jsp/censo/includeVolver.jspf" %>


	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	</body>
</html>
