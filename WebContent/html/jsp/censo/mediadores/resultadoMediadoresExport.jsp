
<!-- resultadoMediadoresExport.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versi�n inicial
-->

<!-- CABECERA JSP -->
<%@page import="org.redabogacia.sigaservices.app.AppConstants.CEN_MEDIADOR_TIPOEXPORT"%>
<%@page import="org.redabogacia.sigaservices.app.autogen.model.CenMediadorExportfichero"%>
<%@page import="com.siga.censo.mediadores.action.MediadoresExportAction"%>

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
	List<CenMediadorExportfichero> resultado= new ArrayList<CenMediadorExportfichero>();
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute(MediadoresExportAction.DATAPAGINADOR) != null) {
		hm = (HashMap) ses.getAttribute(MediadoresExportAction.DATAPAGINADOR);

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (List<CenMediadorExportfichero>) hm.get("datos");

			PaginadorVector paginador = (PaginadorVector) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new ArrayList<CenMediadorExportfichero>();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new ArrayList<CenMediadorExportfichero>();
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
	<html:javascript formName="MediadoresExportForm" staticJavascript="false" />
	
	</head>
	
	<script type="text/javascript">
		function erroresCarga(fila) {
	    	var idcensowsenvio = document.getElementById('oculto' + fila + '_1');
	    	
	    	document.EdicionRemesaForm.modo.value="erroresCarga";	    	
	    	document.EdicionRemesaForm.idcensowsenvio.value=idcensowsenvio.value;
		   	
			ventaModalGeneral(document.EdicionRemesaForm.name,'P');
		}	
		
		function descargar(fila){						
			var f=document.getElementById("MediadoresExportForm");
			var id = document.getElementById('oculto' + fila + '_1');
			
			f.modo.value = "download";
			f.idmediadorexportfichero.value = id.value;
			f.target="submitArea";
			f.submit();
		}		
		
		function init() {
			var f=document.getElementById("MediadoresExportForm");
			
			if (f && f.idmediadorexportfichero.value > 0) {
				var f=document.getElementById("MediadoresExportForm");
				f.modo.value = "download";							
				f.target="submitArea";
				f.submit();
			}
		}
		
		
	</script>
	
	
	

	<body onload="init()">		
	
	<html:form action="/CEN_ExportMediadoresFichero.do?noReset=true" method="post" target="mainPestanas" style="display:none">
		<html:hidden name="MediadoresFicheroForm" property = "idmediadorexportfichero"/>
		<html:hidden name="MediadoresFicheroForm" property = "modo" value = "ver"/>
	</html:form>
	
	
	<html:form action="/CEN_ExportarMediadores.do?noReset=true" method="post" target="mainPestanas" style="display:none">
		
		<html:hidden name="MediadoresExportForm" property = "modo" value = "insertar"/>
		<html:hidden name="MediadoresExportForm" property = "idmediadorexportfichero"/>
		
		<input type="hidden" name="idcensowsenvio"  id="idcensowsenvio"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados" />
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" />		
	</html:form>
		
		<siga:Table 		   
		   name="listadoIncidencias"
		   border="1"
		   columnNames="censo.mediador.literal.tipoExport,censo.mediador.literal.nombreFichero,censo.mediador.literal.fechaCreacion,"
		   columnSizes="15,50,20">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					CenMediadorExportfichero cenMediadorExportfichero = null;
   					for (int i=0; i<resultado.size(); i++) {
   						cenMediadorExportfichero = (CenMediadorExportfichero)resultado.get(i);
  				   		   		
		   		   		botones = null;
		   		   		String visibleConsulta = null;
		   		   		String size = "&nbsp;";		   		   		
	   		   			
	   		   			visibleConsulta = "true";
	   		   			String tipo = "";
	   		   			FilaExtElement[] elems = new FilaExtElement[1];
	   		   		
	   		   			if (CEN_MEDIADOR_TIPOEXPORT.G.name().equals(cenMediadorExportfichero.getIdtipoexport())) {
	   		   				tipo = "censo.mediador.literal.generado";
	   		   				elems[0]=new FilaExtElement("download", "descargar", "censo.mediador.literal.ficheroXML", SIGAConstants.ACCESS_FULL);
	   		   			} else if (CEN_MEDIADOR_TIPOEXPORT.S.name().equals(cenMediadorExportfichero.getIdtipoexport())) {
	   		   				tipo = "censo.mediador.literal.sincronizado";
	   		   			}	   		   		
	   		   			
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' elementos="<%=elems%>" visibleBorrado="false" visibleEdicion="false" pintarEspacio="no" clase="listaNonEdit" visibleconsulta="true" visibleEdicion="false" botones="C">
		   			<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=cenMediadorExportfichero.getIdmediadorexportfichero()%>">		   			
		   			<td style="text-align: center"><%=UtilidadesString.getMensajeIdioma(usr, tipo)%></td>
					<td style="text-align: center"><%=cenMediadorExportfichero.getNombrefichero()!=null?cenMediadorExportfichero.getNombrefichero():"&nbsp;"%></td>
					<td style="text-align: center"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateLong(usr.getLanguage(), cenMediadorExportfichero.getFechacreacion()))%></td>
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