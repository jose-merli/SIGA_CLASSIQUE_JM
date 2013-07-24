<!DOCTYPE html>
<html>
<head>
<!-- editarRetencionIRPF.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>


<!-- JSP -->
<%  
	String botones = "y,X";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	
	Vector obj 			= (Vector) request.getAttribute("resultado");
	Hashtable hash 		= (Hashtable)obj.get(0);
	
	String fechaFin = (String)hash.get("FECHAFIN");

	if(fechaFin == null || fechaFin.equalsIgnoreCase("null"))
		fechaFin = "";
	else
		fechaFin 	= GstDate.getFormatedDateShort("",fechaFin);
	
	String fechaInicio 	= GstDate.getFormatedDateShort("",(String)hash.get("FECHAINICIO"));
	String idRetencion 	= (String)hash.get("IDRETENCION");
	
%>	

<!-- HEAD -->

	<title>Modificacion Retencion IRPF</title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function mostrarCalendario(numfila){
			var resultado;			
			var tabla;
			tabla = document.getElementById('listadoCalendario');
			resultado = showCalendarGeneral(tabla.rows[numfila].cells[0].all[4]);
		}		
	</script>
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.editarRetencionesIRPF.literal.titulo"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<fieldset>
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="/JGR_PestanaRetencionesIRPF" method="post" target="_self">
	<input type="hidden" name="modo" id="modo" value="modificar">
	<tr>
	<td>
	 <table width="100%" border="0">
		<tr>
			<td width="35%" class="labelText" colspan="2">
				<siga:Idioma key="gratuita.altaRetencionIRPF.literal.fDesde"/>
			</td>
			<td>
				<html:text name="RetencionesIRPFForm" property="fechaInicio" size="10" maxlength="10" styleClass="boxConsulta" readOnly="true" value="<%=fechaInicio%>"></html:text>
			</td>
		</tr>
		<tr>
			<td width="35%" class="labelText" colspan="2">
				<siga:Idioma key="gratuita.altaRetencionIRPF.literal.fHasta"/>
			</td>
			<td>
				<html:text name="RetencionesIRPFForm" property="fechaFin" size="10" maxlength="10" styleClass="boxConsulta" readOnly="true" value="<%=fechaFin%>">></html:text>
			</td>
		</tr>
		<tr>
			<td width="35%" class="labelText" colspan="2">
				<siga:Idioma key="gratuita.altaRetencionIRPF.literal.retencion"/>
			</td>
			<td>
			<%
				ArrayList seleccion = new ArrayList();
				seleccion.add(idRetencion);
			%>
				<siga:ComboBD nombre="idRetencion" tipo="tiposirpf" estilo="true" clase="boxCombo" obligatorio="false" elementoSel="<%=seleccion %>" />
			</td>
		</tr>
	</table>
   </td>
   </tr>
   	</html:form>
	</table>
	</fieldset>

	<siga:ConjBotonesAccion botones="<%=botones%>"   modal="P"/>	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() {
		}

		function accionCancelar() {		
			window.top.close();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script>
	<!-- Asociada al boton GuardarCerrar -->
	function accionGuardarCerrar() {
		sub();
		if(document.getElementById("idRetencion").value == "") {
			alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert3'/>");
			fin();
			return false;
		}
		document.forms[0].setAttribute("action","<%=app%>/JGR_PestanaRetencionesIRPF.do");
		document.forms[0].submit();
	}
</script>
</body>
</html>
