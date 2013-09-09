<!DOCTYPE html>
<html>
<head>
<!-- resultadoSeriesFacturacion.jsp -->

<!-- VENTANA LISTA DE CABECERAS FIJAS -->
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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	Vector vDatosTab = null;
	vDatosTab = (Vector)request.getAttribute("datosTab");
	request.removeAttribute("datosTab");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="AsignacionConceptosFacturablesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="facturacion.asignacionDeConceptosFacturables.titulo" localizacion="facturacion.busquedaSeriesFacturacion.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function refrescarLocal() {
			parent.buscar();
		}
	</script>
</head>

<body class="tablaCentralCampos">

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

	<!-- Formulario de la lista de detalle multiregistro -->
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
			// INICIO: ZONA DE REGISTROS
			// Aqui se iteran los diferentes registros de la lista
			for (int i=0; i<vDatosTab.size(); i++) {
	   			Hashtable miHash = (Hashtable)vDatosTab.elementAt(i);
	   			String bots = "C,E,B";
	   			if (((String)miHash.get("TIPOSERIE")).equals("G")) {
		   			// No permimitmos borrar si es la genérica.
	   				bots = "C,E";
	   			}
%>
				<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=bots %>" clase="listaNonEdit">
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
			// FIN REGISTRO
			// FIN: ZONA DE REGISTROS
		}
%>
	</siga:Table>
	<!-- FIN: LISTA DE VALORES -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>