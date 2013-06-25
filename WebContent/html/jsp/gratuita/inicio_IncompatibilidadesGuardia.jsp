<!-- inicio_IncompatibilidadesGuardia.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	

	//Modo de las pestanhas anteriores:
	String modoPestanha = request.getAttribute("MODOPESTANA")==null?"":(String)request.getAttribute("MODOPESTANA");	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.IncompatibilidadesGuardia.literal.titulo" 
		localizacion="gratuita.IncompatibilidadesGuardia.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAltoBotones('resultado');refrescarLocal()">
	
	
	<html:form action = "/JGR_IncompatibilidadesGuardia.do" method="POST" target="resultado" style="display:none">
		<html:hidden property = "modo" value="<%=modoPestanha%>" />			
		<html:hidden property = "accion" value="<%=modoPestanha%>" />			
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idInstitucionPestanha" />
		<html:hidden property = "idTurnoPestanha"/>
		<html:hidden property = "idGuardiaPestanha"/>
		<html:hidden property = "actionModal" value = ""/>
	</html:form>
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA ACCION -->

	<% if (modoPestanha.equalsIgnoreCase("EDITAR")) { %>
		<siga:ConjBotonesAccion botones="V"  modo="<%=modoPestanha%>"/>	
	<% } %>
		
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">

		<!-- Funcion asociada la busqueda del refreco -->
		function refrescarLocal() 
		{			
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		

		<!-- Funcion asociada a boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"G"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}
		function accionVolver() 
		{		
			document.forms[1].action="JGR_DefinirTurnos.do";
			document.forms[1].target="mainWorkArea";
			document.forms[1].modo.value="abrirAvanzada";
			document.forms[1].submit();
		}
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	<html:form action = "/JGR_DefinirTurnos.do" method="POST" target="resultado">
		<html:hidden property = "modo"/>			
	</html:form>
</body>
</html>