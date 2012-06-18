<!-- busquedaLG.jsp-->
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

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Hashtable datosFormulario = new Hashtable();
	String listaguardias="", lugar="", provincia="", zona="", subzona="", partido="", iniciarBusqueda="";
	if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
		datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
		listaguardias = datosFormulario.get("LISTAGUARDIAS")==null?"":(String)datosFormulario.get("LISTAGUARDIAS");
		lugar = datosFormulario.get("LUGAR")==null?"":(String)datosFormulario.get("LUGAR");
		zona = datosFormulario.get("ZONA")==null?"":(String)datosFormulario.get("ZONA");
		subzona = datosFormulario.get("SUBZONA")==null?"":(String)datosFormulario.get("SUBZONA");
		partido = datosFormulario.get("PARTIDO")==null?"":(String)datosFormulario.get("PARTIDO");
		iniciarBusqueda = datosFormulario.get("INICIARBUSQUEDA")==null?"":(String)datosFormulario.get("INICIARBUSQUEDA");		
	}	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaLG.literal.titulo" 
		localizacion="gratuita.busquedaLG.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAlto('resultado');<%if (iniciarBusqueda.equals("SI")) {%> buscar() <% } %>">
	
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCampos" align="center">
	<html:form action = "/JGR_DefinirListaGuardias.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "fechaMod" value = "sysdate"/>
		<html:hidden property = "accion" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<tr>
	<td>
<siga:ConjCampos leyenda="gratuita.turnos.literal.confListadosGuardias">
	
	<table align="center" width="100%">
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaLG.literal.nombre"/>	
	</td>	
	<td class="labelText">	
		<html:text name="DefinirListaGuardiasForm" property="listaGuardias" size="50" maxlength="100" styleClass="box" value="<%=listaguardias%>"></html:text>		
	</td>	
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaLG.literal.lugar"/>
	</td>	
	<td class="labelText">
		<html:text name="DefinirListaGuardiasForm" property="lugar" size="50" maxlength="100" styleClass="box" value="<%=lugar%>"></html:text>		
	</td>	
	</tr>
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaLG.literal.zona"/>	
	</td>	
	<td class="labelText">	
		<html:text name="DefinirListaGuardiasForm" property="zona" size="50" maxlength="60" styleClass="box" value="<%=zona%>"></html:text>		
	</td>	
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaLG.literal.subzona"/>	
	</td>	
	<td class="labelText">	
		<html:text name="DefinirListaGuardiasForm" property="subzona" size="65" maxlength="60" styleClass="box" value="<%=subzona%>"></html:text>		
	</td>	
	</tr>

<!--
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaLG.literal.partido"/>	
	</td>	
	<td class="labelText" colspan="3">	
		<html:text name="DefinirListaGuardiasForm" property="partido" size="185" maxlength="100" styleClass="box" value="<%=partido%>"></html:text>		
	</td>	
	</tr>
-->
	
	
	</table>

</siga:ConjCampos>
	</td>	
	</tr>
	</html:form>
	</table>

	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.busquedaLG.literal.titulo" />
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			document.forms[0].subzona.value = trim(document.forms[0].subzona.value);
			sub();
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].accion.value= "buscarPor";
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].target = 'mainWorkArea';		
			document.forms[0].modo.value = "editar";
			document.forms[0].accion.value= "nuevo";
			document.forms[0].submit();
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