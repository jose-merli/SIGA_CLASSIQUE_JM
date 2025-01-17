<!DOCTYPE html>
<html>
<head>
<!-- pestanasCenSJCS.jsp -->
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	//Para saber que venimos desde los menus de Censo a SJCS:
	ses.setAttribute("entrada","2");
	
	try {
		Long idPersona = new Long(request.getParameter("idPersona"));
		String accion = (String)request.getParameter("accion");
		request.getSession().setAttribute("modoPestanha",accion);
		request.setAttribute("error",accion);
		ses.setAttribute("idPersonaTurno",idPersona.toString());
	} catch(Exception e) {
	}
%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<script type="text/javascript">
			//Funcion asociada a boton buscar -->
			function buscar() {
				window.frames["mainPestanas"].location.href=window.frames["mainPestanas"].location.href;
			}

			function refrescarLocal() {
				buscar();
			}
	
</script>
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/DefinirTurnosAction.do" method="POST" style="display:none">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = "editar"/>
	</html:form> 

<%
	//Preparo los parametros a pasar al siguiente nivel de pestanhas:
	Hashtable datosPestanha = new Hashtable();
	datosPestanha.put("idPersonaPestanha",request.getParameter("idPersona"));
	datosPestanha.put("idInstitucionPestanha",request.getParameter("idInstitucion"));
	//aalg: paso la accion ver o editar al siguiente nivel de pestanhas
	datosPestanha.put("accion", request.getParameter("accion"));
	//Meto la hash en la request:
	request.setAttribute("datosCliente",datosPestanha);
%>
	
<!-- INICIO: CONJUNTO DE PESTA�AS (LINEA) -->
<!-- Esto pinta una linea de pestanhas, pero primero es necesario
	 crear los diferentes arrays que contienen los elementos de la linea.
	 Estos arrays deben medir igual para representar las etiquetas a mostrar,
	 los action a ejecutar y el target destino.
	 Contiene un par�metro que indica la numeracion del conjunto para su manipulaci�n
	 y una atributo visible, para configuirar qu� conjunto estara inicialmente visible
	 dentro de la logica de funcionamiento de las pestanhas 
-->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<siga:PestanasExt 
			pestanaId="LETTURNO" 
			target="mainPestanas"
			elementoactivo="1"
			parametros="datosCliente"
	/>

<!-- FIN: CONJUNTO DE PESTA�AS (LINEA) -->
<!-- Del mismo modo se pueden configurar m�s lineas de pestanhas iterando conjuntos de pestanhas -->


	

		<!-- INICIO: IFRAME GESTION PRINCIPAL -->
		<!--	 <%=app%>/html/jsp/gratuita/maestroTurnos.jsp -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas"
			>
	</iframe>
	

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
