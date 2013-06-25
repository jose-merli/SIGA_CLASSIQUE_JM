<!-- pestanasCenSJCSRetIRPFNoColegiado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="struts-html.tld" 	prefix="html"%>
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>

<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@ page import="com.atos.utils.ClsConstants" %>

<!-- JSP -->
<% 
	// Estas dos variables solo se utilizan para el boton volver
	String app = request.getContextPath();
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	
	String sIdInstitucion = (String) request.getAttribute("idInstitucion");
	String sIdPersona = (String) request.getAttribute("idPersona");	
	Vector lista = (Vector) request.getAttribute("resultado");
	
%>

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="censo.fichaCliente.sjcs.retencionesIRPF.cabecera" localizacion="censo.fichaCliente.sjcs.retencionesIRPF.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function accionInformeRetencionesIRPF() {			
			document.RetencionesIRPFForm.modo.value = "dialogoInformeIRPF";
			// Abro ventana modal y refresco si necesario
			var resultado = ventaModalGeneral(document.RetencionesIRPFForm.name,"P");
		}
	</script>
</head>

<body>
	<br>
	<table border="1" cellspacing="0" cellpadding="5" id='cabeceraTabla' width="100%">
		<tr class="tableTitle">
			<td align="center" width="10%">
				<b><siga:Idioma key="gratuita.retencionesIRPF.literal.fDesde"/></b>
			</td>
			<td align="center" width="10%">
				<b><siga:Idioma key="gratuita.retencionesIRPF.literal.fHasta"/></b>
			</td>
			<td align="center" width="10%">
				<b><siga:Idioma key="gratuita.retencionesIRPF.literal.letra"/></b>
			</td>				
			<td align="center" width="60%">
				<b><siga:Idioma key="gratuita.retencionesIRPF.literal.descripcion"/></b>
			</td>
			<td align="center" width="10%">
				<b><siga:Idioma key="gratuita.retencionesIRPF.literal.retencion"/></b>
			</td>	
		</tr>
		
		<% 
			if (lista != null && lista.size()>0) {
				for (int i=0; i<lista.size(); i++) {
					Hashtable datos = (Hashtable) lista.get(i);
					String sLetra = (String) datos.get("LETRA");
					String sDescripcion = (String) datos.get("DESCRIPCION");
					String sRetencion = (String) datos.get("RETENCION");
					
					String claseFila;
					if((i+2)%2==0)
   	 	 				claseFila = "filaTablaPar";
   	 				else
   		 				claseFila = "filaTablaImpar";
		%>			
			<tr class="<%=claseFila%>">
				<td align="left" width="10%">
					-					
				</td>
				<td align="left" width="10%">
					-					
				</td>
				<td align="left" width="10%">
					<%=sLetra%>						
				</td>
				<td align="left" width="60%">
					<%=sDescripcion%>
				</td>
				<td align="right" width="10%">
					<%=sRetencion%>
				</td>		
			</tr>
		<% }} %>				
	</table>
	
	<html:form action="/JGR_PestanaRetencionesIRPF.do" method="post" styleId="RetencionesIRPFForm">
		<input type="hidden" id="modo" name="modo" value=""/>
		<input type="hidden" id="actionModal" name="actionModal" value=""/>
		<input type="hidden" id="idInstitucion" name="idInstitucion" value="<%=sIdInstitucion%>" />
		<input type="hidden" id="idPersona" name="idPersona" value="<%=sIdPersona%>" />
		<input type="hidden" id="desdeFicha" name="desdeFicha" value="1" />
	</html:form>
	
	<siga:ConjBotonesAccion botones="V, IRI" clase="botonesDetalle"  />
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf"%>
	
	<iframe name="submitAreaPestanasCenSJCSRetIRPFNoColegiado" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>