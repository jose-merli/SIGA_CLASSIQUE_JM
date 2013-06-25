<!-- inicio_DefinirCalendarioGuardia.jsp -->
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
	

	//Modo de la pestanha:
	//mhg - INC_10635_SIGA
	//String modopestanha = request.getAttribute("modoPestanha")==null?"":(String)request.getAttribute("modoPestanha");
	String modopestanha = request.getSession().getAttribute("modo")==null ? "" : 
	    (String)request.getSession().getAttribute("modo");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.listado_DefinirCalendarioGuardia.literal.titulo" 
		localizacion="gratuita.listado_DefinirCalendarioGuardia.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAltoBotones('resultado');refrescarLocal()">
	
	
	<html:form action = "/JGR_DefinirCalendarioGuardia.do" method="POST" target="resultado" style="display:none">
		<html:hidden property = "modo" value = ""/>			
		<html:hidden property = "accion" value = ""/>			
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "idInstitucionPestanha"/>
		<html:hidden property = "idTurnoPestanha"/>
		<html:hidden property = "idGuardiaPestanha"/>
		<html:hidden property = "modoPestanha" value = "<%=modopestanha%>"/>
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
	<!-- FIN: IFRAME LISTA RESULTADOS -->

		<siga:ConjBotonesAccion botones="N" modo="<%=modopestanha%>"/>	
		
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function refrescarLocal() 
		{
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		

		<!-- Funcion asociada a boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].modo.value = "modalNuevoCalendario";
			var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->
	
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>