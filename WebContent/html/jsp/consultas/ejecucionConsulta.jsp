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
	PaginadorBind paginador = (PaginadorBind) hm.get ("paginador");
	String paginaSeleccionada = String.valueOf (paginador.getPaginaActual());
	String totalRegistros = String.valueOf (paginador.getNumeroTotalRegistros());
	String registrosPorPagina = String.valueOf (paginador.getNumeroRegistrosPorPagina());
	
	String tipoConsulta = (String) request.getAttribute ("tipoConsulta");
%>



<!-- METATAGS -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Cerrar

		
		function accionDownload() 
		{
			sub();
			document.forms[0].modo.value = "download";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
			fin();
		}
		
		function accionImprimir() 
		{			
			
			window.print();
		}
		
		function accionVolver() 
		{		
			var formu=document.RecuperarConsultasForm;
			formu.action=formu.action+"?noReset=true&buscar=true";
			if(parent.document.getElementById("accionAnterior")&&parent.document.getElementById("accionAnterior").value!=""){
				formu.accionAnterior.value=parent.document.getElementById("accionAnterior").value;
				formu.idModulo.value=parent.document.getElementById("idModulo").value;
				formu.modo.value="inicio";
			}else{
				formu.modo.value="abrir";
			}
			
			formu.target='mainWorkArea';
			formu.submit();				
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
			frameborder="0" marginheight="0" marginwidth="0"
			class="frameGeneral">
	</iframe>
	
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center" style="bottom: 20px;">
		<tr>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.volver"/>' name='idButton' id="idButton" onclick="return accionVolver();" class="button" value='<siga:Idioma key="general.boton.volver"/>'>
			</td>
			<td  style="width:900px;">
			&nbsp;
			</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.imprimir"/>' name='idButton' id="idButton" onclick="return accionImprimir();" class="button" value='<siga:Idioma key="general.boton.imprimir"/>'>
			</td>
			<td class="tdBotones">
				<input type="button" alt='<siga:Idioma key="general.boton.download"/>' name='idButton' id="idButton" onclick="return accionDownload();" class="button" value='<siga:Idioma key="general.boton.download"/>'>
			</td>
		</tr>
	</table>
	
	<siga:Paginador totalRegistros="<%=totalRegistros%>" 
					registrosPorPagina="<%=registrosPorPagina%>" 
					paginaSeleccionada="<%=paginaSeleccionada%>" 
					idioma="<%=idioma%>"
					modo="ejecutarConsulta"								
					clase="paginator" 
					divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 48px; left: 0px"
					distanciaPaginas=""
					action="<%=action%>" />
	
		
		<html:form action="/CON_RecuperarConsultasDinamicas.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior" value = "ejecucion"/>
			<html:hidden property = "idModulo"/>
		</html:form>

	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"	style="display:none" />
	 
</body>

</html>
