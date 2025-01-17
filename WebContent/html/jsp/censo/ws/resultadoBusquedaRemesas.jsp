
<!-- resultadoBusquedaRemesas.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versi�n inicial
-->

<!-- CABECERA JSP -->
<%@page import="com.siga.censo.ws.form.EdicionRemesaForm"%>
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


<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.censo.ws.action.BusquedaRemesasAction"%>
<%@ page import="com.siga.censo.ws.form.BusquedaRemesasForm"%>


<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	

	String idioma=usr.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	List<EdicionRemesaForm> resultado= new ArrayList<EdicionRemesaForm>();
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute(BusquedaRemesasAction.DATAPAGINADOR) != null) {
		hm = (HashMap) ses.getAttribute(BusquedaRemesasAction.DATAPAGINADOR);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (List<EdicionRemesaForm>) hm.get("datos");

			PaginadorVector paginador = (PaginadorVector) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new ArrayList<EdicionRemesaForm>();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new ArrayList<EdicionRemesaForm>();
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
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	</head>
	
	<script type="text/javascript">
		function erroresCarga(fila) {
	    	var idcensowsenvio = document.getElementById('oculto' + fila + '_1');
	    	
	    	document.EdicionRemesaForm.modo.value="erroresCarga";	    	
	    	document.EdicionRemesaForm.idcensowsenvio.value=idcensowsenvio.value;
		   	
			ventaModalGeneral(document.EdicionRemesaForm.name,'P');
		}	
		function refrescarLocal(){
			alert("refrescar local");
		}
	</script>

	<body>		
	
	
	
	<html:form action="/CEN_EdicionRemesas.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="idcensowsenvio"  id="idcensowsenvio"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados" />
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />		
	</html:form>
	
	 
		
		<siga:Table 		   
		   name="listadoRemesas"
		   border="1"
		   columnNames="censo.ws.literal.colegio,censo.carga.tipoenvio.TIPOENVIO,censo.ws.literal.fechaPeticion,censo.ws.literal.estado,"
		   columnSizes="20,15,25,15,15">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					FilaExtElement[] elems = null;
   					
   					for (int i=0; i<resultado.size(); i++) {
   						EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm)resultado.get(i);
  				   		   		
		   		   		botones = null;
		   		   		String visibleConsulta = "true";
		   		   		String size = "&nbsp;";
		   		   		
		   		   		botones = "C,E";
	   		   			
	   		   			String incidencias = "&nbsp;";
	   		   			String porcentaje = "&nbsp;";
	   		   			String tabPorcentaje = "&nbsp;";
	   		   			String tipoEnvio = "";
	   		   			
	   		   			if (edicionRemesaForm.getConerrores() != null && edicionRemesaForm.getConerrores() > 0) {
	   		   				elems = new FilaExtElement[1];
	   		   				elems[0]=new FilaExtElement("error", "erroresCarga", "censo.ws.literal.errorFormato", SIGAConstants.ACCESS_FULL);	   		   				
	   		   				botones = "";
	   		   			} else if (edicionRemesaForm.getIncidencias() != null && edicionRemesaForm.getIncidencias() > 0) {
	   		   				
	   		   				elems = new FilaExtElement[1];
	   		   				elems[0]=new FilaExtElement("incidencia", "editar", "censo.ws.literal.revisarIncidencias", SIGAConstants.ACCESS_FULL);
	   		   				botones = "C";
	   		   				
	   		   				/*incidencias = edicionRemesaForm.getIncidencias() + " / " + edicionRemesaForm.getCountTotalColegiados();
	   		   				int umbral = Integer.valueOf(edicionRemesaForm.getUmbral());
	   		   				elems = new FilaExtElement[1];	
	   		   				int margenUP =umbral+15;*/
	   		   				
	   		   				
	   		   				/*if(edicionRemesaForm.getPorcentajeCalculado()>umbral && edicionRemesaForm.getPorcentajeCalculado()<margenUP ){
		   		   				elems[0]=new FilaExtElement("incidencia", "editar", "censo.ws.literal.revisarIncidencias", SIGAConstants.ACCESS_FULL);
	   		   					tabPorcentaje="orange";
	   		   					botones = "C";
	   		   				}
	   		   				else if(edicionRemesaForm.getPorcentajeCalculado()<=umbral){
		   		   				
		   		   				elems[0]=new FilaExtElement("incidencia", "editar", "censo.ws.literal.revisarIncidencias", SIGAConstants.ACCESS_FULL);
	   		   					tabPorcentaje="green";
	   		   					botones = "C";
	   		   				}else{
	   		   					elems = new FilaExtElement[0];	
	   		   					tabPorcentaje="red";
	   		   					botones = "C";
	   		   				}
	   		   				porcentaje =String.valueOf(edicionRemesaForm.getPorcentajeCalculado())+"%";*/	
	   		   				
	   		   			} else {
	   		   				elems = new FilaExtElement[0];	   		   				
	   		   			}
	   		   			
	   		   			if(AppConstants.ECOM_CEN_MAESESTADOENVIO.PENDIENTE.getCodigo()==edicionRemesaForm.getIdEstadoenvio()){
	   		   				botones =botones+ ",B";
	   		   			} else if (AppConstants.ECOM_CEN_MAESESTADOENVIO.ANALIZANDO.getCodigo() == edicionRemesaForm.getIdEstadoenvio()) {
	   		   				elems = new FilaExtElement[0];
	   		   				botones = "";
	   		   			} else if (AppConstants.ECOM_CEN_MAESESTADOENVIO.ELIMINANDO.getCodigo() == edicionRemesaForm.getIdEstadoenvio()) {
		   		   			elems = new FilaExtElement[0];
	   		   				botones = "";
	   		   			}
	   		   			
	   		   			if(edicionRemesaForm.getTipoEnvio()!=null){
	   		   				tipoEnvio=	AppConstants.ECOM_CEN_TIPO_ENVIO.getDescripcion(edicionRemesaForm.getTipoEnvio());
	   		   			}
	   		   			
	   		   			
	   		   					   		   		
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' elementos="<%=elems%>" visibleBorrado="false" visibleEdicion="false" visibleConsulta="<%=visibleConsulta%>" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=edicionRemesaForm.getIdcensowsenvio()%>">
					<%=BusquedaRemesasForm.getIdinstitucionNombre(edicionRemesaForm.getIdinstitucion().toString())%></td>	
														
					<td style="text-align: center"><siga:Idioma key="<%=tipoEnvio%>"/></td>
					
					<td style="text-align: center"><%=edicionRemesaForm.getFechapeticion()%></td>
							
					<td style="text-align: center"><siga:Idioma key="<%=AppConstants.ECOM_CEN_MAESESTADOENVIO.getDescripcion(edicionRemesaForm.getIdEstadoenvio())%>"/></td>
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
								divStyle="position:absolute; width:100%; height:20; z-index:3; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
																	
			
			 <%}%>	
			 
			
		

	</body>
</html>