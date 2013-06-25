<!-- resultadoBusquedaDuplicados.jsp -->

<!-- CABECERA JSP -->
<%@page import="org.apache.commons.validator.Form"%>
<%@page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.Utilidades.paginadores.PaginadorCaseSensitive"%>
<%@page import="com.siga.Utilidades.paginadores.Paginador"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="com.atos.utils.ClsConstants"%>
<%@page import="com.atos.utils.UsrBean"%>
<%@page import="com.atos.utils.Row"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.administracion.SIGAConstants"%>
<%@page import="com.siga.censo.form.MantenimientoDuplicadosForm"%>
<%@page import="com.siga.Utilidades.paginadores.PaginadorBind"%>

<%
	/**************/
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
	String valorCheckPersona = "";
	
	String action = app + "/CEN_MantenimientoDuplicados.do?noReset=true";
	int cont=0;
	
	Vector resultado = (Vector)request.getAttribute("resultado");
	int nRegistros = 0;
	if(resultado!=null)nRegistros=resultado.size();
	
	MantenimientoDuplicadosForm form = (MantenimientoDuplicadosForm)request.getAttribute("MantenimientoDuplicadosForm");
	
	String nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2,censo.resultadoDuplicados.institucion,censo.resultadoDuplicados.numeroColegiado";
	String tamanoCol="3,10,10,18,18,18,10,8";
	boolean ocultarColegiaciones=true;
	if(form!=null){
		ocultarColegiaciones = form.getAgruparColegiaciones()!=null&&form.getAgruparColegiaciones().equalsIgnoreCase("s");
	}
	if(ocultarColegiaciones){
		nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2, Número de colegiaciones";
		tamanoCol="3,10,10,20,20,20,12";
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/displaytag.css'/>" />
	<script type="text/javascript">


	function accionExportar(){
		
		document.forms[0].modo.value="exportar";
		document.forms[0].submit();
	}

	function accionCerrar() {		
		 window.open('', '_self', '');
	        window.top.close();
	}

	window.resizeTo("500","500");
	
	</script>

</head>

<body class="tablaCentralCampos" onload="accionExportar();">

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="modo"/>
	<html:hidden property="seleccion" />
	<html:hidden property="seleccionados" />
	<html:hidden property="datosPaginador" />
	<html:hidden property="chkApellidos" />
	<html:hidden property="chkNombreApellidos" />
	<html:hidden property="chkNumColegiado" />
	<html:hidden property="chkIdentificador" />
	<html:hidden property="nifcif" />
	<html:hidden property="nombre" />
	<html:hidden property="numeroColegiado" />
	<html:hidden property="apellido1" />
	<html:hidden property="apellido2" />
	<html:hidden property="agruparColegiaciones" />
	<html:hidden property="tipoConexion" />
	<html:hidden property="sentidoOrdenacion" />
	<html:hidden property="campoOrdenacion" />
	
	<table align="center" border="0" height="100%" width="100%">
		<tr><td align="center">&nbsp;</td></tr>
		<tr><td align="center">&nbsp;</td></tr>
		<tr><td align="center"><img src="<%=app%>/html/imagenes/loading.gif"></td></tr>
		<tr><td align="center">&nbsp;</td></tr>
		<tr><td class="labelText" style="text-align:center">Su descarga se está generando. Espere unos instantes.</td></tr>
	</table>


	<table class="botonesDetalle" align="center">
		<tr>
		<td>&nbsp;</td>
		<td class="tdBotones">
		<input type="button" alt="Cerrar"  id="idButton" onclick="return accionCerrar();" class="button" name="idButton" value="Cerrar">
		</td></tr>
	</table>

</html:form>
	 
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
</body>
</html>
