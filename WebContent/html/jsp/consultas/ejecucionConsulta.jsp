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
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitiveBind"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- SCRIPTLET -->
<%
	//Controles generales
	String app = request.getContextPath();
	String action = app + "/CON_RecuperarConsultas.do";
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute ("USRBEAN");
	String idioma = user.getLanguage().toUpperCase();
	
	//Algunas variables
	String descripcion = (String) request.getAttribute ("descripcion");	
	request.removeAttribute ("descripcion");	
	String vacio = (String) request.getAttribute ("vacio");	
	
	HashMap hm = (HashMap) ses.getAttribute ("DATABACKUP");
//	PaginadorCaseSensitiveBind paginador = (PaginadorCaseSensitiveBind) hm.get ("paginador");
	PaginadorBind paginador = (PaginadorBind) hm.get ("paginador");
	
	String paginaSeleccionada = String.valueOf (paginador.getPaginaActual());
	String totalRegistros = String.valueOf (paginador.getNumeroTotalRegistros());
	String registrosPorPagina = String.valueOf (paginador.getNumeroRegistrosPorPagina());
%>

<html>

<!-- METATAGS -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">

<head>
	<link id="default" rel="stylesheet" type="text/css"
		  href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Cerrar
		function accionCerrar() 
		{
			top.cierraConParametros ("VACIO");		
		}
		
		function accionDownload() 
		{
			RecuperarConsultasForm.modo.value = "download";
			RecuperarConsultasForm.target = "submitArea";
			RecuperarConsultasForm.submit();
		}
		
		function accionImprimir() 
		{
			window.print();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
</head>

<body onLoad="ajusteAltoMain('resultado','52');">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" height="38">
		<tr>
			<td id="titulo" class="titulitosDatos"><%=descripcion%></td>
		</tr>
	</table>
	
	<!-- IFRAME LISTA RESULTADOS -->
	<iframe align="middle"
			src="<%=app%>/html/jsp/consultas/resultadoEjecucionConsulta.jsp"
			id="resultado" name="resultado"	scrolling="yes"
			frameborder="0" marginheight="0" marginwidth="0";
			class="frameGeneral">
	</iframe>
	
	<siga:ConjBotonesAccion botones="C,D,I" modal="G" />
	
	<siga:Paginador totalRegistros="<%=totalRegistros%>" 
					registrosPorPagina="<%=registrosPorPagina%>" 
					paginaSeleccionada="<%=paginaSeleccionada%>" 
					idioma="<%=idioma%>"
					modo="ejecutarConsulta"								
					clase="paginator" 
					divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 32px; left: 0px"
					distanciaPaginas=""
					action="<%=action%>" />
	
	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>	
	</html:form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
			style="display:none">
	</iframe>
	 
</body>

</html>
