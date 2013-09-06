<!-- resultadoHistorico.jsp -->

<!-- CABECERA JSP -->
<%@page import="com.siga.censo.ws.action.EdicionColegiadoAction"%>
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>


<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>
<%@ page import="com.siga.censo.ws.action.EdicionRemesasAction"%>
<%@ page import="com.siga.censo.ws.form.EdicionColegiadoForm"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>





<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	
	CenInstitucionAdm institucionAdm = new CenInstitucionAdm(usr);
	
			
	

	String idioma=usr.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	List<EdicionColegiadoForm> resultado= new ArrayList<EdicionColegiadoForm>();
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute(EdicionColegiadoAction.DATAPAGINADOR) != null) {
		hm = (HashMap) ses.getAttribute(EdicionColegiadoAction.DATAPAGINADOR);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (List<EdicionColegiadoForm>) hm.get("datos");

			PaginadorVector paginador = (PaginadorVector) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new ArrayList<EdicionColegiadoForm>();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new ArrayList<EdicionColegiadoForm>();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}
	
	String action=app+request.getAttribute("javax.servlet.forward.servlet_path")+"?noReset=true";
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	String botones = "";
	
	
	boolean accionVer = false;
	String accion = (String) request.getSession().getAttribute("accion");
	if (accion != null && accion.equals("ver")) {
		accionVer = true;
	}
	
	
%>	


<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	</head>
	
	<script type="text/javascript">
					
			
	</script>

	<body>		
	
	<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<input type="hidden" name="idcensodatos"  id="idcensodatos"  value="${EdicionColegiadoForm.idcensodatos}">
		<input type="hidden" name="historico"  id="historico"  value="true">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
	</html:form>
	
		
		<siga:Table 		   
		   name="listadoColegiados"
		   border="1"
		   columnNames="censo.ws.literal.fechaCambio,censo.ws.literal.incidencias,censo.ws.literal.estado,"
		   columnSizes="15,40,30">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					for (int i=0; i<resultado.size(); i++) {
   						EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm)resultado.get(i);
  		
		   		   		FilaExtElement[] elems = null;
		   		   		botones = null;
		   		   		String visibleConsulta = null;
		   		   		String size = "&nbsp;";
		   		   		
	   		   			botones = "C";			
	   		   			elems = new FilaExtElement[0];
	   		   			visibleConsulta = "true";		   		   		
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' elementos="<%=elems%>" visibleBorrado="false" visibleEdicion="false" visibleConsulta="<%=visibleConsulta%>" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=String.valueOf(edicionColegiadoForm.getIdcensodatos())%>">
					<%=edicionColegiadoForm.getFechaCambio()!=null?edicionColegiadoForm.getFechaCambio():"&nbsp;"%></td>
					<td>
					<% if (edicionColegiadoForm.getIncidencias() != null &&  edicionColegiadoForm.getIncidencias().size() > 0) { 
						for (String incidencia : edicionColegiadoForm.getIncidencias()) {%> 
							<siga:Idioma key="<%=incidencia%>"/><br/>
						<%}
						
					} else {%>
						&nbsp;
					<%} %>
					</td>
					<td><siga:Idioma key="<%=AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.getDescripcion(edicionColegiadoForm.getIdestadocolegiado())%>"/></td>
					
				</siga:FilaConIconos>	
							<% }
		   				   } else { %>
	 							<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
						<% } %>
				
			</siga:Table>
			
			<%if ( resultado!=null && resultado.size() > 0){%>
	  	  						
				<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:55px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
																	
			
			 <%}%>	
			 
			 <script type="text/javascript">
			 	
			 	
			 	function accionVolver() {
					document.forms[0].action="./CEN_BusquedaRemesas.do?noReset=true&buscar=true";	
					document.forms[0].modo.value="abrirAvanzada";
					document.forms[0].target="mainWorkArea"; 
					document.forms[0].submit();
				}
			 	
				
				
			 </script>
		
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	</body>
</html>