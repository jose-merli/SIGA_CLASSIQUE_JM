<!-- calendario.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
%>

<html>

<!-- HEAD -->

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.calendario.literal.calendario" 		
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado');">

	<fieldset>
	<table class="tablaCentralCampos" align="center">

	<html:form action="/CalendarioLaboralAction.do" method="POST" target="resultado" onsubmit="return onSubmitValidaCampoAnio();">
		<input type="hidden" name="modo" value="buscar">	
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idInstitucion" value="<%=usr.getLocation()%>">
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		
	<tr>
	<td class="labelText" width="200">
		<siga:Idioma key="gratuita.calendario.literal.calendario"/>	&nbsp;(*)
	</td>
	<td>
		<html:text name="CalendarioLaboralForm" property="fecha" size="4" maxlength="4" styleClass="box" value=""></html:text>
	</td>	
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="B,N"  titulo="gratuita.calendario.literal.calendario" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	
	<script language="JavaScript">

		function onSubmitValidaCampoAnio () 
		{		
			document.forms[0].fecha.value = trim(document.forms[0].fecha.value);
			var anyo = document.forms[0].fecha.value;
			if (isNaN(anyo) || (anyo=="")) {								
				alert('<siga:Idioma key="gratuita.calendario.literal.fechaIncorrecta"/>');
				return false;
			}
			return true;
		}
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();
			if (onSubmitValidaCampoAnio()) {
				document.forms[0].modo.value = "buscar";
				document.forms[0].submit();
				
			}else{
				fin();
			
			}
		}
	
		function nuevo() 
		{
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

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
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>