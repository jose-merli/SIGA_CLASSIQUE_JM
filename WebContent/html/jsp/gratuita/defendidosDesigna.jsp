<!-- defendidosDesigna.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.DefendidosDesignasForm"%>
<%@ page import="com.siga.gratuita.action.PersonaJGAction"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector obj = (Vector)request.getAttribute("resultado");
	request.getSession().setAttribute("resultado",obj);
	String modo = (String) ses.getAttribute("Modo");
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String anio="",numero="", idturno="";
	Hashtable designaActual = (Hashtable)ses.getAttribute("designaActual");
	anio = (String)designaActual.get("ANIO");
	numero = (String)designaActual.get("NUMERO");
	idturno = (String)designaActual.get("IDTURNO");	
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.defendidosDesigna.literal.titulo" 
		localizacion="gratuita.defendidosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>

</head>

<body onLoad="ajusteAlto('resultado');" class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_DefendidosDesignas.do" method="post" target="mainPestanas" style="display:none">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "actionModal" value = ""/>
		</html:form>
		
<iframe align="center" src="<%=app%>/html/jsp/gratuita/listarDefendidosDesignas.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<script language="JavaScript">

			<!-- Asociada al boton Volver -->
		function accionVolver() 
		{	
			document.forms[0].target = "mainWorkArea";	
			document.forms[0].action="JGR_Designas.do";
			document.forms[0].modo.value="volverBusqueda";
			document.forms[0].submit();
		}
		
		function accionNuevo() 
		{	
			var resultado=ventaModalGeneral(document.forms[1].name,"G");
			if (resultado=="MODIFICADO")
			{
				buscar();
			}
		}
		
		function buscar(){
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="";
			document.forms[0].submit();
		}
		
		function refrescarLocal()
		{
			buscar();
		}
</script>		

		<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" modo="<%=modo%>" />

		<html:form action="/JGR_DefendidosDesignasPerJG.do" method="post" target="submitArea">
			<input type="hidden" name="modo" value="abrirPestana">
			<input type="hidden" name="actionModal" value="">
			
			<input type="hidden" name="idInstitucionJG" value="<%=usr.getLocation() %>">
			<input type="hidden" name="idPersonaJG" value="">
	
			<input type="hidden" name="idInstitucionDES" value="<%=usr.getLocation() %>">
			<input type="hidden" name="idTurnoDES" value="<%=idturno %>">
			<input type="hidden" name="anioDES" value="<%=anio %>">
			<input type="hidden" name="numeroDES" value="<%=numero %>">
	
			<input type="hidden" name="conceptoE" value="<%=PersonaJGAction.DESIGNACION_INTERESADO %>">
			<input type="hidden" name="tituloE" value="gratuita.defendidosDesigna.literal.titulo">
			<input type="hidden" name="localizacionE" value="">
			<input type="hidden" name="accionE" value="nuevo">
			<input type="hidden" name="actionE" value="/JGR_DefendidosDesignasPerJG.do">
			<input type="hidden" name="pantallaE" value="M">
		</html:form>	
		
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
		  
		
