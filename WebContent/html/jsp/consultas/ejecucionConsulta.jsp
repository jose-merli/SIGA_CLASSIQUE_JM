<!DOCTYPE html>
<html>
<head>
<!-- ejecucionConsulta.jsp -->

<!-- METATAGS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="struts-html.tld"	prefix="html"%>
<%@ taglib uri="struts-bean.tld"	prefix="bean"%>
<%@ taglib uri="libreria_SIGA.tld"	prefix="siga"%>

<!-- IMPORTS FOR SCRIPTS -->
<%@ page import="java.util.HashMap"%>
<%@ page import="java.lang.Long"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.atos.utils.Row"%>

<!-- SCRIPTLET -->
<%
	//Controles generales
	String app = request.getContextPath();
	String action = (String)request.getAttribute("javax.servlet.forward.request_uri");
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute ("USRBEAN");
	String idioma = user.getLanguage().toUpperCase();
	
	//Algunas variables
	String descripcion = (String) request.getAttribute ("descripcion");	
	request.removeAttribute ("descripcion");	
	String vacio = (String) request.getAttribute ("vacio");	
	
	HashMap hm = (HashMap) ses.getAttribute ("DATABACKUP");
	PaginadorBind paginador = (PaginadorBind) hm.get ("paginador");
	String paginaSeleccionada = String.valueOf (paginador.getPaginaActual());
	String totalRegistros = String.valueOf (paginador.getNumeroTotalRegistros());
	String registrosPorPagina = String.valueOf (paginador.getNumeroRegistrosPorPagina());
	
	String tipoConsulta = (String) request.getAttribute ("tipoConsulta");
	
	String tipoacceso = user.getAccessType();

	//Variables para tomar los datos
	Vector datos = (Vector) ((HashMap) ses.getAttribute("DATABACKUP")).get("datos");
	String[] cabeceras = (String[]) ((HashMap) ses.getAttribute("DATABACKUP")).get("cabeceras");
%>

<!-- METATAGS -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onload="ajusteAltoPaginador('resultado')">

	<script language="JavaScript">
		jQuery(document).ready(function () {
			parent.document.getElementById("idButtonDescargar").style.display =  'block';
			parent.document.getElementById("idButtonImprimir").style.display =  'block';
		});		
	</script>	

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" height="25">
		<tr>
			<td id="titulo" class="titulitosDatos"><%=descripcion%></td>
		</tr>
	</table>
	
<%
	String nombresCol = "";	
	for (int j = 0; j < cabeceras.length; j++) {
		if(j == cabeceras.length-1)
			nombresCol += UtilidadesString.mostrarDatoJSP(cabeceras[j]);
		else
			nombresCol += UtilidadesString.mostrarDatoJSP(cabeceras[j]) + ",";
	}	
	
%>	
	
	<siga:Table 
   	      name="tablaDatos"
   		  border="1"
		  columnNames="<%=nombresCol%>">
	   		     		    		  
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
			<% if (datos==null || datos.size()==0) { %>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
				
			<% } else {  
			
					for (int i = 0; i < datos.size(); i++) {
						Row fila = (Row) datos.elementAt(i);
			%>
						
						  	<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones="" visibleEdicion="no" visibleConsulta="no" visibleBorrado="no" pintarEspacio="no" clase="listaNonEdit">
								<% for (int k = 0; k < cabeceras.length; k++) { %>
									<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(cabeceras[k]))%></td>
								<% } %>
							</siga:FilaConIconos>				
						
					<% } 
				} %>
			
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>	
	
	<% if (action!=null && action.equals("/SIGA/CON_RecuperarConsultasDinamicas.do")) { %>

	<siga:Paginador totalRegistros="<%=totalRegistros%>" 
					registrosPorPagina="<%=registrosPorPagina%>" 
					paginaSeleccionada="<%=paginaSeleccionada%>" 
					idioma="<%=idioma%>"
					modo="ejecutarConsulta"								
					clase="paginator" 
					divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:35px; left: 0px"
					distanciaPaginas=""
					action="<%=action%>" />
					
	<% } else {  %>	
		
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
					registrosPorPagina="<%=registrosPorPagina%>" 
					paginaSeleccionada="<%=paginaSeleccionada%>" 
					idioma="<%=idioma%>"
					modo="ejecutarConsulta"								
					clase="paginator" 
					divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:35px; left: 0px"
					distanciaPaginas=""
					action="<%=action%>" />
	<% } %>
		
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"	style="display:none" />
	 
</body>

</html>
