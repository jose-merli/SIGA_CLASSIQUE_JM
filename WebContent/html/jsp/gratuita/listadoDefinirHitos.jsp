<!-- listadoDefinirHitos.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String entrada=(String)request.getSession().getAttribute("entrada");
	String boton ="";
	String acceso=usr.getAccessType();
	String direccion="listadoHitosFacturables.jsp";
	String accion="/JGR_DefinirHitosFacturables.do";
	String letra="F";
	String pagos = (String)request.getSession().getAttribute("pagos");
	if (pagos!=null){
		accion="/JGR_DefinirHitosPago.do";
		direccion="listadoHitosPagos.jsp";
		letra="P";
	}

	//Modo de la pestanha:
	String modopestanha = request.getSession().getAttribute("modo")==null?"":(String)request.getSession().getAttribute("modo");
%>


<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

</head>

<body  onLoad="ajusteAltoBotones('resultado');">

	
	<html:form action="<%=accion%>" method="POST" target="submitArea"  style="display:none">
		<input type="hidden" name="modo" value="">
		<html:hidden property ="actionModal" value = ""/>
	</html:form>
	
	<iframe align="center" src="<%=app%>/html/jsp/gratuita/<%=direccion%>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<% if (entrada.equalsIgnoreCase("1")) {  %>
		<siga:ConjBotonesAccion botones="N,V" modo="<%=modopestanha%>" clase="botonesDetalle"/>
	<% }  %>

	<script language="JavaScript">

		function buscar() 
		{
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value = "abrir";
			document.forms[0].submit();
		}
		
		//Asociada al boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		function refrescarLocal(){
			buscar();
		}
		function accionVolver() 
		{		
			document.forms[1].action="JGR_DefinirTurnos.do";
			document.forms[1].target="mainWorkArea";
			document.forms[1].modo.value="abrirAvanzada";
			document.forms[1].submit();
		}

	</script>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	<html:form action = "/JGR_DefinirTurnos.do" method="POST" target="resultado">
		<html:hidden property = "modo"/>			
	</html:form>
</body>
</html>