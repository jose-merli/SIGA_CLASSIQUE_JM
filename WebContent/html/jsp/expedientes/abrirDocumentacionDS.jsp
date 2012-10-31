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
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.expedientes.form.ExpDocumentacionForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	String editable = (String)request.getParameter("editable");		
	String soloSeguimiento = (String)request.getParameter("soloSeguimiento");	
	boolean bEditable=false;
	if (soloSeguimiento.equals("true")){
		bEditable=false;
	}else{
		bEditable = editable.equals("1")? true : false;
	}
	
	String botones2 = "V";		
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
	request.removeAttribute("datos");
	
	ExpDocumentacionForm f = (ExpDocumentacionForm)request.getAttribute("ExpDocumentacionForm");	
	
	String identificadorDS = (String) request.getAttribute("IDENTIFICADORDS");

%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.documentacion.cabecera" 
			localizacion="expedientes.auditoria.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>
	
	<script type="text/javascript">
		function cargarCollection() {			
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado1";	
			document.forms[0].submit();	
		}
	</script>

	<body class="detallePestanas" onload="cargarCollection()">	

		<table class="tablaTitulo" align="center" cellspacing="0">
		<html:form action="/EXP_Auditoria_DocumentacionRegTel.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			
			<input type="hidden" name="identificadorDs" value="<%=identificadorDS%>">
			<input type="hidden" name="titleDs" value="">
			<input type="hidden" name="posicionDs" value="">
			
			<tr>
				<td id="titulo" class="titulitosDatos">					
					<%=f.getTituloVentana()%>
					<html:hidden property = "tituloVentana" value = "<%=f.getTituloVentana()%>"/>
				</td>
			</tr>
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>			

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
			
			

		<siga:ConjBotonesAccion botones="<%=botones2%>" clase="botonesDetalle"  />
	<!-- FIN: BOTONES REGISTRO -->

<% if (!busquedaVolver.equals("volverNo")) { %>
		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>
<% } %>	
	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function refrescarLocal()
		{	
			document.location.reload();
		}

	<!-- Asociada al boton Volver -->
		function accionVolver() 
		{				
			<% if (busquedaVolver.equals("AB")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% } else if (busquedaVolver.equals("NB")){ %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<% }  else if (busquedaVolver.equals("Al")){%>
				document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<% } %>
			document.forms[1].submit();	
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>