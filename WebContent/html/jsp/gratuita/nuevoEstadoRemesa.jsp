<!-- nuevoEstadoRemesa.jsp -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.GruposClienteClienteForm"%>
<%@ page import="com.siga.censo.form.ActividadProfesionalForm"%>
<%@ page import="com.siga.gratuita.form.EstadosRemesaForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>


<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	EstadosRemesaForm miform = (EstadosRemesaForm)request.getAttribute("EstadosRemesaForm");
	String[] param = {miform.getIdRemesa(), miform.getIdInstitucion()};
	String modo_aux=(String)request.getAttribute("MODO_AUX");
	String idestadoAnterior="";
	String fechaAnterior="";
	if (miform.getIdEstado()!=null){
		idestadoAnterior=miform.getIdEstado();
	}
	if (miform.getFechaEstado()!=null){
		fechaAnterior=miform.getFechaEstado();
	}
	
	String fechaEstado = UtilidadesBDAdm.getFechaBD("");
	
	ArrayList estadoSel = new ArrayList();
	if (modo_aux != null && modo_aux.trim().equals("modificar")) {
		estadoSel.add(idestadoAnterior);
		fechaEstado = fechaAnterior;
	} else {
		estadoSel.add(String.valueOf((Integer.parseInt(idestadoAnterior) + 1)));
	}
	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<html:javascript formName="EstadosRemesaForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>
	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
		<td class="titulitosDatos">	
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.estado"/>
		</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->




		
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.BusquedaRemesas_CAJG.literal.estado">
	<table class="tablaCampos" align="center" border="0">
	<html:form action="/JGR_EstadosRemesa.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden property = "idRemesa" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "modoAnterior" />
		<html:hidden property = "idEstadoAnterior" value="<%=idestadoAnterior  %>" />
		<html:hidden property = "fechaEstadoAnterior" value="<%=fechaAnterior  %>" />
	
	<tr>
		<td width="120" class="labelText">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.fechaEstado"/>&nbsp;(*)
		</td>
		
		<td>
			<siga:Fecha nombreCampo="fechaEstado" valorInicial="<%=fechaEstado%>"></siga:Fecha>
		</td>
	</tr>
	
	<tr>
		<!-- GRUPO CLIENTE -->
		<td width="80" class="labelText">
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.estado"/>
		</td>
		<td align="left">
			
			<siga:ComboBD nombre="idEstado" elementoSel="<%=estadoSel%>" tipo="cmbEstadosRemesa" clase="boxConsulta" obligatorioSinTextoSeleccionar="true" seleccionMultiple="false" readonly="true" parametro="<%=param%>"  />
			
		</td>
	</tr>
	</html:form>
	</table>
	</siga:ConjCampos>
	<!-- FIN: CAMPOS DEL REGISTRO -->


	
	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->
	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
		
		//Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}	
	
		//Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar
		function accionGuardarCerrar() {	
			if (document.forms[0].idEstado.value!='') {			
				document.forms[0].target = "submitArea";
				<%
				if (modo_aux==null || !modo_aux.equals("modificar")){%>
					document.forms[0].modo.value = "Insertar";
				<%}else{
					%>
					document.forms[0].modo.value = "Modificar";
				<%}%>
				var f = document.getElementById("EstadosRemesaForm");				
  				if (f && validateEstadosRemesaForm(f)) {
	        		document.forms[0].submit();
	        	}
			} else
				alert('error...');
	   }	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>