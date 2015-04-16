<!DOCTYPE html>
<html>
<head>
<!-- resultadoSeriesFacturacion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	Vector vDatosTab = (Vector)request.getAttribute("datosTab");
	request.removeAttribute("datosTab");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
	<html:javascript formName="AsignacionConceptosFacturablesForm" staticJavascript="false" />  

	<siga:TituloExt titulo="facturacion.asignacionDeConceptosFacturables.titulo" localizacion="facturacion.busquedaSeriesFacturacion.literal.localizacion"/>
	
	<script>
		function refrescarLocal() {
			parent.buscar();
		}
		
		function solicitarbaja(fila, id) {			
			if(confirm('<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.solicitarBaja"/>')) { 
				sub();				
				if (typeof id == 'undefined')
					id='tabladatos';
				preparaDatos(fila,id);
				
				var auxTarget = document.forms[0].target;
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "solicitarbaja";
				document.forms[0].submit();
				document.forms[0].target = auxTarget;
				fin();
			}			
		}
		
		function solicitaralta(fila, id) {						
			if(confirm('<siga:Idioma key="facturacion.busquedaSeriesFacturacion.literal.solicitarAlta"/>')) { 
				sub();
				if (typeof id == 'undefined')
					id='tabladatos';
				preparaDatos(fila,id);
				
				var auxTarget = document.forms[0].target;
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "solicitaralta";
				document.forms[0].submit();
				document.forms[0].target = auxTarget;
				fin();
			}
		}
	</script>
</head>

<body class="tablaCentralCampos">
	<html:form action="/FAC_AsignacionConceptosFacturables.do" target="mainWorkArea" method="POST" style="display:none">
		<html:hidden property = "modo"  styleId = "modo"  value = "success"/>
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
	</html:form>		
	
	<siga:Table 
	   	name="tabladatos"
		border="1"
   		columnNames="facturacion.resultadoSeriesFacturacion.literal.nombreAbreviado,facturacion.resultadoSeriesFacturacion.literal.descripcion,"
   		columnSizes="30,55,12">

<%
		if (vDatosTab==null || vDatosTab.size()==0)	{
%>
			<tr class="notFound">
	  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<%	
		} else {
			for (int i=0; i<vDatosTab.size(); i++) {
	   			Hashtable miHash = (Hashtable) vDatosTab.get(i);
	   			
	   			String sFechaBaja = UtilidadesHash.getString(miHash, "FECHABAJA");
	   			
	   			FilaExtElement[] elems = new FilaExtElement[1];
	   			
	   			if (sFechaBaja==null || sFechaBaja.equals("")) {
	   				elems[0]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_READ);
	   			} else {
	   				elems[0]=new FilaExtElement("solicitaralta","solicitaralta",SIGAConstants.ACCESS_READ);
	   			}
	   			
%>
				<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' botones='C,E,B' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDSERIEFACTURACION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("USUMODIFICACION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=miHash.get("IDPLANTILLA")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=miHash.get("FECHAMODIFICACION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=UtilidadesString.comaToAnd((String)miHash.get("DESCRIPCION"))%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=UtilidadesString.comaToAnd((String)miHash.get("NOMBREABREVIADO"))%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=miHash.get("IDINSTITUCION")%>">
						<%=miHash.get("NOMBREABREVIADO")%>
					</td>
					<td><%=miHash.get("DESCRIPCION")%></td>
				</siga:FilaConIconos>
<%
			}
		}
%>
	</siga:Table>
		
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>