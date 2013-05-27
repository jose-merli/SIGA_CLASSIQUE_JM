<!-- busquedaMaestroPJ.jsp-->
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
	String poblacion = "", partido="", provincia="", idprovincia="", iniciarBusqueda="";
	int indice_combo = 0;
	if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
		datosFormulario = (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
		partido = datosFormulario.get("PARTIDOJUDICIAL")==null?"":(String)datosFormulario.get("PARTIDOJUDICIAL");

		iniciarBusqueda = datosFormulario.get("INICIARBUSQUEDA")==null?"":(String)datosFormulario.get("INICIARBUSQUEDA");		
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="MantenimientoMaestroPJForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="gratuita.busquedaMaestroPJ.literal.titulo" localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="ajusteAlto('resultado');<%if (iniciarBusqueda.equals("SI")) {%> buscar() <% } %>">
	
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<fieldset>
	<table class="tablaCentralCampos">
	<html:form action = "/JGR_MantenimientoMaestroPJ.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "idPartido" value = ""/>

	<tr>
	
	<td class="labelText"  width="150">	
		<siga:Idioma key="gratuita.busquedaMaestroPJ.literal.partido"/>	
	</td>	
	<td class="labelTextValor" width="100">	
		<html:text name="MantenimientoMaestroPJForm" property="partidoJudicial" size="30" styleClass="box" value="<%=partido%>"></html:text>		
	</td>
	<td class="labelText" width="60%">	
		&nbsp;
	</td>
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<% if (usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) { %>	
	<siga:ConjBotonesBusqueda botones="B"  titulo="gratuita.busquedaPJ.literal.titulo" />
	<% } else { %>
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.busquedaPJ.literal.titulo" />
	<% } %>
	<!-- FIN: BOTONES BUSQUEDA -->


	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			if (validateMantenimientoMaestroPJForm(document.MantenimientoMaestroPJForm)){
				document.forms[0].modo.value = "buscarPor";
				document.forms[0].submit();
			}
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].poblacion.value= "";
			document.forms[0].partidoJudicial.value= "";
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].target = 'mainWorkArea';		
			document.forms[0].modo.value = "nuevo";
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

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>