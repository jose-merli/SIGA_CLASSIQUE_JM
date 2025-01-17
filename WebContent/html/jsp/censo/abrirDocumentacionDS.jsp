<!DOCTYPE html>
<html>
<head>
<!-- abrirDocumentacionDS.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 31/01/2005 Versi�n inicial
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
	String idNoColegiado = (String) request.getAttribute("ID_NOCOLEGIADO"); // Obtengo el identificador del NO colegiado de la persona
	String identificadorDS = (String) request.getAttribute("IDENTIFICADORDS");

	// Para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null) {
		busquedaVolver = "volverNo";
	}

	List<DocuShareObjectVO> resultado= new ArrayList<DocuShareObjectVO>();
	String action = (String) request.getAttribute("ACTION"); // Obtengo el action al que pertenece la transaccion (Censo o No Colegiado)
	String modo=(String)request.getSession().getAttribute("accion");
	
%>	

<%@page import="org.redabogacia.sigaservices.app.vo.DocuShareObjectVO"%>

<!-- HEAD -->
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<siga:Titulo 
			titulo="censo.fichaCliente.regtel.cabecera" 
			localizacion="censo.fichaLetrado.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>
	
	<script type="text/javascript">
		function cargarCollection() {			
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado1";	
			document.forms[0].submit();	
		}
	</script>

	<body onload="sub();cargarCollection();ajusteAlto('resultado1')">
	
		<html:form action="<%=action%>" method="post" target="mainWorkArea" style="display:none">
			<input type="hidden" name="modo" value="<%=modo%>">			
			<input type="hidden" name="identificadorDs" value="<%=identificadorDS%>">
			<input type="hidden" name="titleDs" value="">
			<input type="hidden" name="posicionDs" value="">
			<input type="hidden" name="creaCollection" value="false">
			<input type="hidden" name="idPersona" value="<%=idPersona%>">
		</html:form>
		
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" height="32">
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.docushare.literal.titulo1"/> &nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;
				    <%
				    	if (numero != null && !numero.equalsIgnoreCase("")) {
				    %>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
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
							marginwidth="0"					 
							style="position:relative;height:100%;width:100%;">
			</iframe>
			<!-- FIN: IFRAME LISTA RESULTADOS -->
			
			<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>

		<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	</body>
</html>