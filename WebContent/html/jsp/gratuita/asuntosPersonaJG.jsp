<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.PersonaJGForm"%>

<html:html>
<head>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");	
	
	PersonaJGForm form = (PersonaJGForm)request.getAttribute("PersonaJGForm");
	String modoGuardar = form.getModoGuardar();
	String accionGuardar = form.getAccionGuardar();
	
	String action = (String)request.getAttribute("javax.servlet.forward.servlet_path");
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	<script type="text/javascript">
		function reloadPage() {
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(!resultado==""){
				document.forms[0].accionGuardar.value = resultado;
				document.forms[0].modo.value = "<%=modoGuardar%>";
				document.forms[0].submit();
			}
		}

	</script>
</head>

<body onload="reloadPage();">

		<html:form action="<%=action%>" method="POST" target="_self">
			<html:hidden property = "modo" value = "abrirModalAsuntos"/>
			<html:hidden property="ncolegiadoContrario"/>
			<html:hidden property="conceptoE"/>
			<html:hidden property="tituloE"/>
			<html:hidden property="localizacionE"/>
			<html:hidden property="sexo"/> 
			<html:hidden property="hijos"/>
			<html:hidden property="edad"/> 			 
			<html:hidden property="accionE"/>
			<html:hidden property="pantallaE"/>
			<html:hidden property="actionE"/>
			<html:hidden property="idInstitucionJG"/>
			<html:hidden property="idPersonaJG"/>
			<html:hidden property="idInstitucionPER"/>
			<html:hidden property="idPersonaPER"/>
			<html:hidden property="idInstitucionEJG"/>
			<html:hidden property="idTipoEJG"/>
			<html:hidden property="anioEJG"/>
			<html:hidden property="numeroEJG"/>
			<html:hidden property="idInstitucionSOJ"/>
			<html:hidden property="idTipoSOJ"/>
			<html:hidden property="anioSOJ"/>
			<html:hidden property="numeroSOJ"/>
			<html:hidden property="idInstitucionDES"/>
			<html:hidden property="idTurnoDES"/>
			<html:hidden property="anioDES"/>
			<html:hidden property="numeroDES"/>
			<html:hidden property="idInstitucionASI"/>
			<html:hidden property="anioASI"/>
			<html:hidden property="numeroASI"/>
			<html:hidden property="idTipoPersona"/>
			<html:hidden property="tipoId"/>
			<html:hidden property="NIdentificacion"/>
			<html:hidden property="nombre"/>
			<html:hidden property="apellido1"/>
			<html:hidden property="apellido2"/>
			<html:hidden property="direccion"/>
			<html:hidden property="cp"/>
			<html:hidden property="provincia"/>
			<html:hidden property="poblacion"/>
			<html:hidden property="nacionalidad"/>
			<html:hidden property="fechaNac"/>
			<html:hidden property="estadoCivil"/>
			<html:hidden property="regimenConyugal"/>
			<html:hidden property="profesion"/>
			<html:hidden property="minusvalia"/>			
			<html:hidden property="parentesco"/>
			<html:hidden property="observaciones"/>
			<html:hidden property="unidadObservaciones"/>
			<html:hidden property="enCalidadDe"/>
			<html:hidden property="enCalidadDeLibre"/>
			<html:hidden property="calidad"/>
			<html:hidden property="idTipoenCalidad"/>
			<html:hidden property="calidadIdinstitucion"/>			
			<html:hidden property="idProcurador"/>
			<html:hidden property="idRepresentanteJG"/>
			<html:hidden property="idAbogadoContrarioEJG"/>
			<html:hidden property="idPersonaRepresentante"/>
			<html:hidden property="representante"/>
			<html:hidden property="abogadoContrario"/>
			<html:hidden property="abogadoContrarioEJG"/>
			<html:hidden property="idPersonaContrario"/>
			<html:hidden property="solicitante"/>
			<html:hidden property="bienesInmuebles"/>
			<html:hidden property="importeBienesInmuebles"/>
			<html:hidden property="bienesMuebles"/>
			<html:hidden property="importeBienesMuebles"/>
			<html:hidden property="otrosBienes"/>
			<html:hidden property="importeOtrosBienes"/>
			<html:hidden property="ingresosAnuales"/>
			<html:hidden property="importeIngresosAnuales"/>
			<html:hidden property="pantalla"/>
			<html:hidden property="nuevo"/>
			<html:hidden property="chkSolicitaInfoJG"/>
			<html:hidden property="chkPideJG"/>
			<html:hidden property="tipoConoce"/>
			<html:hidden property="tipoGrupoLaboral"/>
			<html:hidden property="numVecesSOJ"/>
			<html:hidden property="nombreObjetoDestino"/>
			<html:hidden property="tipoIngreso"/> 
			<html:hidden property="ncolegiadoRepresentante"/>
			<html:hidden property="modoGuardar"/>
			<html:hidden property="accionGuardar"/>
			<html:hidden property="idioma"/>
			<html:hidden property="nombreAnterior"/>
			<html:hidden property="lNumerosTelefonos"/>
			<html:hidden property="correoElectronico"/>
			<html:hidden property="fax"/>			
			<html:hidden property="existeDomicilio"/>
			<html:hidden property="tipoVia"/>
			<html:hidden property="tipoDir"/>
			<html:hidden property="pisoDir"/>
			<html:hidden property="escaleraDir"/>
			<html:hidden property="puertaDir"/>			
			<html:hidden property="numeroDir"/>			
			<input type="hidden" name="actionModal" value="">
		</html:form>

</body>
</html:html>