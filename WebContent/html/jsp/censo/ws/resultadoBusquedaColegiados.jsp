<!-- resultadoBusquedaColegiados.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versi�n inicial
-->

<!-- CABECERA JSP -->
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>
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
<%@ page import="com.siga.censo.ws.form.EdicionColegiadoForm"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>
<%@ page import="com.siga.censo.ws.action.EdicionRemesasAction"%>




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
	
	if (ses.getAttribute(EdicionRemesasAction.DATAPAGINADOR) != null) {
		hm = (HashMap) ses.getAttribute(EdicionRemesasAction.DATAPAGINADOR);

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
	
	
/* 	boolean accionVer = false;
	String accion = (String) request.getSession().getAttribute("accion");
	if (accion != null && accion.equals("ver")) {
		accionVer = true;
	} */
	
	
%>	


<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	</head>
	
	<script type="text/javascript">
					
			
	</script>

	<body>		
	
	<html:form action="/CEN_EdicionColegiado.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<input type="hidden" name="historico"  id="historico"  value="false">
		
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />
		<input type="hidden" id="verFichaLetrado"  name="verFichaLetrado" value="1">
	</html:form>
	
	<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="generaExcel">	
		<html:hidden property="nombreFichero" value="${EdicionRemesaForm.numeroPeticion}"/>					
	</html:form>
	
		<bean:define name="EdicionRemesaForm" property="accion" id="accion"/>
		
		<siga:Table 		   
		   name="listadoColegiados"
		   border="1"
		   columnNames="censo.ws.literal.numeroColegiado,censo.ws.literal.nombre,censo.ws.literal.primerApellido,censo.ws.literal.segundoApellido,censo.ws.literal.estado,"
		   columnSizes="10,15,15,15,30">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					String botones=null;
   					for (int i=0; i<resultado.size(); i++) {
   						EdicionColegiadoForm edicionColegiadoForm = (EdicionColegiadoForm)resultado.get(i);
  		
		   		   		FilaExtElement[] elems = null;
		   		   		FilaExtElement[] elems2 = null;
		   		   		botones = null;
		   		   		String visibleConsulta = null;
		   		   		String size = "&nbsp;";
		   		   		
		   		   		
	   		   			elems = new FilaExtElement[0];
	   		   			visibleConsulta = "true";
	   		   			
	   		   			if (!edicionColegiadoForm.isCambioSituacion()){
	   		   				botones = "C";
	   		   			}else{
	   		   				elems = new FilaExtElement[1];
	   		   				elems[0]=new FilaExtElement("consultar", "verCambio", "general.boton.consultar", SIGAConstants.ACCESS_FULL);
	   		   				botones = "X";
	   		   				visibleConsulta = "false";
	   		   			}
	   		   		
	   		   		
	   		   			if (!accion.equals("ver")) {		   		   			
	   		   				if (edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.INCIDENCIAS_DATOS_COLEGIADO.getCodigo()
	   		   						|| edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ALTA_COLEGIADO.getCodigo()
	   		   						|| edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ALTA_PERSONA_COLEGIADO.getCodigo()
	   		   						|| edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ERROR_ACTUALIZACION_COLEGIADO.getCodigo()	   		   						   		   					
	   		   					) {
	   		   					
	   		   					
	   		   					if (edicionColegiadoForm.isCambioSituacion()){
	   		   						elems2 = new FilaExtElement[2];
	   		   						elems2[0] = elems[0];
	   		   						elems2[1] = new FilaExtElement("incidencia", "editarCambio", "censo.ws.literal.revisarIncidencias", SIGAConstants.ACCESS_FULL);
	   		   						elems = elems2;
	   		   					}else{
	   		   						elems = new FilaExtElement[1];
	   		   						elems[0]=new FilaExtElement("incidencia", "editar", "censo.ws.literal.revisarIncidencias", SIGAConstants.ACCESS_FULL);	 
	   		   					}
	   		   				} else if (edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.INICIAL_RECIBIDO.getCodigo()
	   		   						|| edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.LISTO_PASO2.getCodigo()
	   		   						|| edicionColegiadoForm.getIdestadocolegiado()==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.BAJA_CALCULADA.getCodigo()) {
	   		   					
	   		   					botones += ",E";
	   		   				}
	   		   			}
	   		   			
	   		   			
	   		   			if (edicionColegiadoForm.getIdpersona() != null) {
	   		   				if (!edicionColegiadoForm.isCambioSituacion()){
	   		   					elems = new FilaExtElement[1];
	   							elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
	   		   				
	   		   				}else{
	   		   					elems2 = new FilaExtElement[2];
		   						elems2[0] = elems[0];
		   						elems2[1] = new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
   		   						elems = elems2;
	   		   				}
		   				}
	   		   			
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' elementos="<%=elems%>" visibleBorrado="false" visibleEdicion="false" visibleConsulta="<%=visibleConsulta%>" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=edicionColegiadoForm.getIdpersona()%>">
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=edicionColegiadoForm.getIdinstitucion()%>">					
					<!-- input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" value="LETRADO" -->
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" id="oculto<%=String.valueOf(i+1)%>_3" value="<%=edicionColegiadoForm.getIdcensodatos()%>">
					<td style="text-align: right;"><%=edicionColegiadoForm.getNcolegiado()!=null?edicionColegiadoForm.getNcolegiado():"&nbsp;"%></td>
					<td><%=edicionColegiadoForm.getNombre()!=null?edicionColegiadoForm.getNombre():"&nbsp;"%></td>
					<td><%=edicionColegiadoForm.getApellido1()!=null?edicionColegiadoForm.getApellido1():"&nbsp;"%></td>
					<td><%=edicionColegiadoForm.getApellido2()!=null?edicionColegiadoForm.getApellido2():"&nbsp;"%></td>										
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
								divStyle="position:absolute; width:100%; height:20; z-index:3;  left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
																	
			
			 <%}%>	
			 
			 <script type="text/javascript">
			 	function editarCambio(fila, id) {
					if (typeof id == 'undefined')
						id='listadoColegiados';
					preparaDatos(fila, id);
				   document.forms[0].modo.value = "editarCambio";
				   document.forms[0].submit();
				 }
			 	function verCambio(fila, id) {
					if (typeof id == 'undefined')
						id='listadoColegiados';
					preparaDatos(fila, id);
				   document.forms[0].modo.value = "verCambio";
				   document.forms[0].submit();
				 }

			 
			 	function accionGenerarExcels() {	
			 		if (confirm('<siga:Idioma key="messages.censo.cargaperiodica.confirmExcel"/>')) {
						document.forms[1].submit();
			 		}
			 	}
			 	
			 	
			 	function accionVolver() {
					document.forms[0].action="./CEN_BusquedaRemesas.do?noReset=true";	
					document.forms[0].modo.value="abrirAvanzada";
					document.forms[0].target="mainWorkArea"; 
					document.forms[0].submit();
				}
			 				 				 	
			 	function informacionLetrado(fila) {
					document.forms[0].action="./CEN_BusquedaClientes.do?noReset=true";
					var oculto3 = document.getElementById("oculto" + fila + "_3");
					
					if (oculto3) {
						oculto3.value = "LETRADO";
					}
					var oculto2 = document.getElementById("oculto" + fila + "_2");
					if (oculto2) {
						oculto2.value = "2000";
					}
					
					selectRow(fila);	
					editar(fila);					
				}
				
			 </script>
			 
			 <% String botonesAction = "V";
			 if ( resultado!=null && resultado.size() > 0){
				 botonesAction += ",GX";
			 }
			 %>
			 
		
	<siga:ConjBotonesAccion botones="<%=botonesAction%>" clase="botonesDetalle"  />
	</body>
</html>