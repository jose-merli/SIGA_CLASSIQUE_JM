<!DOCTYPE html>
<html>
<head>
<!-- abrirDocumentacionDS.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versión inicial
-->

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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@page import="org.redabogacia.sigaservices.app.vo.DocuShareObjectVO"%>
<%@page import="org.redabogacia.sigaservices.app.vo.DocuShareObjectVO.DocuShareTipo"%>
<%@page import="java.text.DecimalFormat"%>

<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.censo.form.DatosRegTelForm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	DatosRegTelForm form = (DatosRegTelForm) request.getAttribute("DatosRegTelForm");
		
	List<DocuShareObjectVO> migas = (List<DocuShareObjectVO>)request.getSession().getAttribute("MIGAS_DS");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null) {
		busquedaVolver = "volverNo";
	}

	

	String idioma=usr.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	List<DocuShareObjectVO> resultado= new ArrayList<DocuShareObjectVO>();
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	
	if (ses.getAttribute("DATAPAGINADOR") != null) {
		hm = (HashMap) ses.getAttribute("DATAPAGINADOR");

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (List<DocuShareObjectVO>) hm.get("datos");

			PaginadorVector paginador = (PaginadorVector) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

		} else {
			resultado = new ArrayList<DocuShareObjectVO>();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new ArrayList<DocuShareObjectVO>();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}
	
	String action=app+request.getAttribute("javax.servlet.forward.servlet_path")+"?noReset=true";
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	String botones = "";
	
	Boolean creaCollection = (Boolean)request.getAttribute("CREACOLLECTION");
	
	if (creaCollection == null) {
		creaCollection = Boolean.FALSE;
	}
	
	boolean accionVer = false;
	String accion = (String) request.getSession().getAttribute("accion");
	if (accion != null && accion.equals("ver")) {
		accionVer = true;
	}
	
	
%>	




<!-- HEAD -->
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->

		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>
	
	<script type="text/javascript">
		
		
			function download(fila) {			
				var idDoc = document.getElementById('oculto' + fila + '_1');
				if (idDoc) {			
					accionDownload(idDoc.value);
				}
			}
	
			function consultaCollection(posicion, identificador, title) {
				var objHidd = document.getElementById(identificador);
				if (objHidd) {
					accionConsultaCollection(posicion, objHidd.value, title);
				}
			}
			
			function preguntaCrearCollection() {
				
				if (<%=!accionVer%> && <%=creaCollection%> && confirm('<siga:Idioma key="messages.creaCollection"/>')) {
					parent.document.forms[0].modo.value="buscarPor";
					parent.document.forms[0].target="resultado1";
					parent.document.forms[0].creaCollection.value=true;	
					parent.document.forms[0].submit();
				} else {
					parent.document.forms[0].creaCollection.value=false;
				}
			}
			
	</script>

	<body onload="preguntaCrearCollection()">	
		
		
		<table>
			<tr>
		<% if (migas != null) {
			String separador = " >> ";
			String preSeparador = "";
			String comodin = separador + " . . . ";
			
			DocuShareObjectVO docuShareObjectVO = null;
			String srcImage = app + "/html/imagenes/ico_homepage_ds.png";
			
			for (int i = 0; i < migas.size(); i++) {
				docuShareObjectVO = migas.get(i);
				if ((i < 2 || i > migas.size()-3)) {//solo se muestran los dos primeros y los dos últimos%>
					<td >
						<input type="hidden" name="identificadorDSMigas<%=i%>" value="<%=docuShareObjectVO.getId()%>"/>
						<%=preSeparador%><a href="javascript:consultaCollection(<%=i %>, 'identificadorDSMigas<%=i%>', '<%=docuShareObjectVO.getTitle()%>')">
					<% if (i == 0) { 
						preSeparador = separador;%>
						<img src='<%=srcImage%>' width="70%" border="0"/>	
					<% } else { %>
						<%=UtilidadesString.mostrarDatoJSP(docuShareObjectVO.getTitle())%>						
					<% } %>
						</a>
					  </td>
					<%
				} else if (comodin != null) {
					%><td><%=comodin %></td><%
					comodin = null;
				}
			}
			
		} %>
			</tr>
		</table>
					
		<siga:Table 		   
		   name="contenidoCollection"
		   border="1"
		   columnNames="censo.regtel.literal.descripcion,censo.regtel.literal.fechaModificacion,censo.regtel.literal.tamanio,"
		   columnSizes="60,15,15">
		   
		   	<%
   				if (resultado != null && resultado.size() > 0) {
   					for (int i=0; i<resultado.size(); i++) {
  						DocuShareObjectVO dsObj = (DocuShareObjectVO)resultado.get(i);
  		
		   		   		FilaExtElement[] elems = null;
		   		   		botones = null;
		   		   		String visibleConsulta = null;
		   		   		String size = "&nbsp;";
		   		   		String srcImage = "";
		   		   		
		   		   		if (DocuShareObjectVO.DocuShareTipo.COLLECTION.equals(dsObj.getTipo())) {
		   		   			botones = "C";			
		   		   			elems = new FilaExtElement[0];
		   		   			visibleConsulta = "true";
		   		   			srcImage = app + "/html/imagenes/ico_collection_ds.png";
		   		   		} else if (DocuShareObjectVO.DocuShareTipo.DOCUMENT.equals(dsObj.getTipo())) {
		   		   			botones = "";			
	   		   				elems = new FilaExtElement[1];
	   		   				elems[0] = new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
	   		   				visibleConsulta = "false";
	   		   				size = DecimalFormat.getInstance().format(dsObj.getSizeKB()) + " KB";
	   		   				srcImage = app + "/html/imagenes/ico_document_ds.png";
		   		   		}
		   		   		
		   		   		
		   		   	%>
		   		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' elementos="<%=elems%>" visibleBorrado="false" visibleEdicion="false" visibleConsulta="<%=visibleConsulta%>" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
					
					
					<td>
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=dsObj.getId()%>">
					<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=dsObj.getTitle()%>">
					<img src='<%=srcImage%>'/>&nbsp;<%=dsObj.getTitle()%></td>										
					<td style="text-align: center;"><%=UtilidadesString.formatoFecha(dsObj.getFechaModificacion(), ClsConstants.DATE_FORMAT_MEDIUM_SPANISH)%>&nbsp;</td>
					<td style="text-align: right;"><%=size%></td>
				</siga:FilaConIconos>	
							<% }
		   				   } else { %>
	 							<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
						<% } %>
				
			</siga:Table>
			
			<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	  	  						
				<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:35px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
																	
			
			 <%}%>	
			 
			 <script type="text/javascript">
			 	//sobreescribimos la funcion consultar
			 	function consultar(fila) {
			 		var objHiddId = document.getElementById('oculto' + fila + '_1');
			 		var objHiddDes = document.getElementById('oculto' + fila + '_2');
			 		
					if (objHiddId && objHiddDes) {
						accionConsultaCollection('', objHiddId.value, objHiddDes.value);
					}
					
				}
			 	
			 	function accionDownload(idDoc) {			
			 		parent.document.forms[0].modo.value="download";				
			 		parent.document.forms[0].target="submitArea";
			 		parent.document.forms[0].identificadorDs.value=idDoc;
			 		parent.document.forms[0].submit();
				}

				function accionConsultaCollection(posicion, identificador, title) {					
					parent.document.forms[0].modo.value="ver";
					parent.document.forms[0].target="resultado1";
					parent.document.forms[0].identificadorDs.value=identificador;
					parent.document.forms[0].posicionDs.value=posicion;
					
					parent.document.forms[0].titleDs.value=title;
					parent.document.forms[0].submit();
				}
				
				
			 </script>
		

	</body>
</html>