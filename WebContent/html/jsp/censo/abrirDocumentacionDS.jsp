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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.censo.form.DatosColegialesForm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	DatosColegialesForm form = (DatosColegialesForm) request.getAttribute("DatosColegialesForm");
	String nombre = (String) request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero = (String) request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	String idPersona = (String) request.getAttribute("IDPERSONA"); // Obtengo el numero de colegiado de la persona
	
	
	String identificadorDS = (String) request.getAttribute("IDENTIFICADORDS");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null) {
		busquedaVolver = "volverNo";
	}

	

	String idioma=usr.getLanguage().toUpperCase();
	/** PAGINADOR ***/
	List<DocuShareObjectVO> resultado= new ArrayList<DocuShareObjectVO>();
	String paginaSeleccionada ="";
	
	
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	

	
	String action=app+"/CEN_Censo_DocumentacionRegTel.do?noReset=true";
	String modo=(String)request.getSession().getAttribute("accion");
	
	String botones = "C";
	
	
%>	


<%@page import="org.redabogacia.sigaservices.app.vo.DocuShareObjectVO"%><html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.regtel.cabecera" 
			localizacion="censo.fichaCliente.regtel.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>
	
	<script type="text/javascript">
		function cargarCollection() {			
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado1";	
			document.forms[0].submit();	
		}
	</script>

	<body onload="cargarCollection()">
	
		<html:form action="/CEN_Censo_DocumentacionRegTel.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
			<input type="hidden" name="modo" value="<%=modo%>">			
			<input type="hidden" name="identificadorDs" value="<%=identificadorDS%>">
			<input type="hidden" name="titleDs" value="">
			<input type="hidden" name="posicionDs" value="">
			
			<input type="hidden" name="creaCollection" value="false">
			<input type="hidden" name="idPersona" value="<%=idPersona%>">
			
			
		</html:form>
		
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0" height="32">
			<tr>
				<td id="titulitos" class="titulosPeq">
					<siga:Idioma key="censo.docushare.literal.titulo1"/> &nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;
				    <%
				    	if (!numero.equalsIgnoreCase("")) {
				    %>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%
						} else {
					%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%
						}
					%>
				</td>
			</tr>
		</table>
					
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado1"
							name="resultado1"
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
							style="width:100%; height:100%;">
			</iframe>
			<!-- FIN: IFRAME LISTA RESULTADOS -->
			
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>

		<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	</body>
</html>