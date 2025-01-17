<!DOCTYPE html>
<html>
<head>
<!-- nuevaContabilidad.jsp -->
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
	
	
%>	

<!-- HEAD -->

	<title>Modificacion Retencion IRPF</title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script type="text/javascript">		
	
	function confirmar()
	{
		<%
		String confirmar = (String) request.getAttribute("confirmar");
		String fechaDesde = (String) request.getAttribute("fechaDesde");
		String fechaHasta = (String) request.getAttribute("fechaHasta");
		if(confirmar!=null && confirmar.equals("ok"))
		{%>
			document.forms[0].fechaDesde.value = "<%=fechaDesde%>";
			document.forms[0].fechaHasta.value = "<%=fechaHasta%>";
			var type = '<siga:Idioma key="facturacion.pagoContabilidad.literal.mensaje"/>';
			
			if(confirm(type))
			{	
				document.forms[0].esConfirmacion.value = "1";
				accionGuardarCerrar(); 
				
			}
			else
			{
//				document.forms[0].esConfirmacion.value = "0";
                fin(parent.document); 
				window.top.close();
			}
		<%}%>
	}
		
	</script>
</head>

<body onLoad="confirmar();">
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.nuevaContabilidad.literal.titulo"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
<div id="camposRegistro" class="posicionModalPeque" align="center">
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="/FAC_Contabilidad" method="post" target="submitArea">
	<input type="hidden" name="modo" value="insertar">
	<input type="hidden" name="esConfirmacion" />
	<tr>
	<td>
	 <table width="100%" border="0">
		<tr>
			<td width="30%" class="labelText">
				<siga:Idioma key="gratuita.nuevaContabilidad.literal.fDesde"/>:&nbsp;(*)
			</td>
			<td>
				<siga:Fecha nombreCampo="fechaDesde"/>		
			</td>
		</tr>
		<tr>
			<td width="30%" class="labelText">
				<siga:Idioma key="gratuita.nuevaContabilidad.literal.fHasta"/>:&nbsp;(*)
			</td>
			<td>
				<siga:Fecha nombreCampo="fechaHasta"/>
			</td>
		</tr>
	</table>
   </td>
   </tr>
   	</html:form>
		<div style="position:absolute;top:348px;left: 0px">
			<siga:ConjBotonesAccion botones="<%=botones%>" clase="" modal="P"/>	
		</div>
	</table>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() 
		{
			window.top.close();
		}

		function accionCancelar() 
		{		
			window.top.close();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script>
	<!-- Asociada al boton GuardarCerrar -->
	function accionGuardarCerrar() 
	{ 
		
		if (compararFecha(document.forms[0].fechaDesde,document.forms[0].fechaHasta)==1) {
			mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
			alert(mensaje);
			fin();
			
		}else{
			sub(parent.document);
			document.forms[0].submit();
//			var f = document.forms[0].name;	
//			document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.nuevaContabilidad.mensaje.generandoFicheros';
		}
		
		
	}
</script>
</body>
</html>
