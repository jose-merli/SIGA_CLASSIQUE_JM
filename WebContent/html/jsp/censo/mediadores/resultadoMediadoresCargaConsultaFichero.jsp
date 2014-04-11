
<!-- resultadoMediadoresCargaConsultaFichero.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versión inicial
-->

<!-- CABECERA JSP -->

<%@page import="com.siga.censo.mediadores.action.ImportarMediadoresConsultaFicheroAction"%>
<%@page import="org.redabogacia.sigaservices.app.autogen.model.CenMediadorCsv"%>

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
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>





<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	
	CenInstitucionAdm institucionAdm = new CenInstitucionAdm(usr);
	
			
	

	String idioma=usr.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	List<CenMediadorCsv> resultado= new ArrayList<CenMediadorCsv>();
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute(ImportarMediadoresConsultaFicheroAction.DATAPAGINADOR) != null) {
		hm = (HashMap) ses.getAttribute(ImportarMediadoresConsultaFicheroAction.DATAPAGINADOR);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (List<CenMediadorCsv>) hm.get("datos");

			PaginadorVector paginador = (PaginadorVector) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new ArrayList<CenMediadorCsv>();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new ArrayList<CenMediadorCsv>();
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
			
		
	</script>

	<body>		
	
	
	
	<html:form action="/CEN_ConsultaCargaMediadores.do?noReset=true" method="post" target="mainPestanas" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="idcensowsenvio"  id="idcensowsenvio"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados" />
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />		
	</html:form>
		
		<siga:Table 		   
		   name="listadoIncidencias"
		   border="1"		   
		   columnNames="censo.mediador.literal.fila,censo.mediador.literal.ncolegio,censo.mediador.literal.identificacion,censo.mediador.literal.nombre,censo.mediador.literal.apellido1,censo.mediador.literal.apellido2"
		   columnSizes="5,12,12,20,20,20">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					CenMediadorCsv cenMediadorCsv = null;
   					for (int i=0; i<resultado.size(); i++) {
   						cenMediadorCsv = (CenMediadorCsv)resultado.get(i);
  				   		   		
		   		   		botones = null;
		   		   		String visibleConsulta = null;
		   		   		String size = "&nbsp;";
		   		   		
	   		   			
	   		   			visibleConsulta = "true";		   		   		
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' visibleBorrado="false" visibleEdicion="false" pintarEspacio="no" clase="listaNonEdit" visibleconsulta="false" visibleEdicion="false" botones="">		   		
		   			<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=cenMediadorCsv.getIdmediadorficherocsv()%>">		   			
		   			
		   			<td style="text-align: center"><%=cenMediadorCsv.getFilacsv()%></td>
					<td style="text-align: right"><%=cenMediadorCsv.getNcolegiado()!=null?cenMediadorCsv.getNcolegiado():"&nbsp;"%></td>										
					<td style="text-align: center"><%=cenMediadorCsv.getIdentificacion()!=null?cenMediadorCsv.getIdentificacion():"&nbsp;"%></td>					
					<td><%=cenMediadorCsv.getNombre()!=null?cenMediadorCsv.getNombre():"&nbsp;"%></td>					
					<td><%=cenMediadorCsv.getApellido1()!=null?cenMediadorCsv.getApellido1():"&nbsp"%></td>
					<td><%=cenMediadorCsv.getApellido2()!=null?cenMediadorCsv.getApellido2():"&nbsp;"%></td>					
					
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