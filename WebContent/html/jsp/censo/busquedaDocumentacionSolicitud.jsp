<!-- busquedaDocumentacionSolicitud.jsp -->
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
<%@ page import="com.siga.censo.form.DocumentacionSolicitudForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
 
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// locales
	DocumentacionSolicitudForm formulario = (DocumentacionSolicitudForm)request.getAttribute("documentacionSolicitudForm");

	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");

	String [] modalidadParam = new String[1];
	modalidadParam[0] = user.getLocation();
	
		
	// datos seleccionados Combo
	ArrayList colegiacionSel = new ArrayList();
	ArrayList tipoSolicitudSel = new ArrayList();
	ArrayList tipoModalidadSel = new ArrayList();
	
	String tipoCol = formulario.getTipoColegiacion();
	if (tipoCol!=null) {
		colegiacionSel.add(tipoCol);
	}
	String tipoSol = formulario.getTipoSolicitud();
	if (tipoSol!=null) {
		tipoSolicitudSel.add(tipoSol);
	}

	String tipoMod = formulario.getTipoModalidad();
	if (tipoMod!=null) {
		tipoModalidadSel.add(tipoMod);
	}
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.documentacionSolicitud.cabecera" 
		localizacion="censo.documentacionSolicitud.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="documentacionSolicitudForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body  onLoad="ajusteAlto('resultado');">



	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.documentacionSolicitud.leyenda">

	<table class="tablaCampos" align="center">

	<html:form action="/CEN_DocumentacionSolicitudes.do" method="POST" target="resultado">
	<html:hidden name="documentacionSolicitudForm" property = "modo" value = ""/>

	<!-- campo con los documentos seleccionados -->
	<html:hidden name="documentacionSolicitudForm" property = "documentosSolicitados" value = ""/>

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.documentacionSolicitud.literal.tipoColegiacion"/>&nbsp;(*)
	</td>				
	<td >
		<siga:ComboBD nombre = "tipoColegiacion"  tipo="colegiacion"  clase="boxCombo" obligatorio="true" elementoSel="<%=colegiacionSel %>"/>						
	</td>

	<td class="labelText">
		<siga:Idioma key="censo.documentacionSolicitud.literal.tipoSolicitud"/>&nbsp;(*)
	</td>				
	<td >
		<siga:ComboBD nombre = "tipoSolicitud" tipo="solicitud"  clase="boxCombo" obligatorio="true" elementoSel="<%=tipoSolicitudSel %>"/>						
	</td>


	<td class="labelText">
		<siga:Idioma key="censo.documentacionSolicitud.literal.modalidad"/>&nbsp;(*)
	</td>				
	<td >
		<siga:ComboBD nombre = "tipoModalidad" tipo="modalidadDocumentacion" clase="boxCombo" obligatorio="true" parametro="<%=modalidadParam%>" elementoSel="<%=tipoModalidadSel%>"/>
	</td>
	</tr>


	</html:form>
	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B"  titulo="censo.documentacionSolicitud.literal.titulo1" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Funcion asociada a boton buscar
		function buscar() 
		{
			sub();		
			if (validateDocumentacionSolicitudForm(document.forms[0])) 
			{
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
			}
			fin();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

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
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
