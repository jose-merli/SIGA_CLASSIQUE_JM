<!-- informeFichaColegiados.jsp -->
<!-- VERSIONES:
	 raul.ggonzalez 30-05-2005 creacion
-->

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
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

	//COMBO PAGOS CERRADOS:
	String comboParams[] = new String[2];
	comboParams[0] = usrbean.getLocation();
	comboParams[1] = ClsConstants.ESTADO_PAGO_EJECUTADO;

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoInformesForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.informes.colegiados.cabecera" 
		localizacion="factSJCS.informes.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body>


	<siga:ConjCampos leyenda="factSJCS.informes.colegiados.cabecera">		


	<table class="tablaCampos" align="center">

	<html:form action="/INF_CartaPago.do" method="POST" target="submitArea">
	<html:hidden name="mantenimientoInformesForm" property = "modo" value = ""/>

	<!-- FILA -->
	<tr>				
	<td class="labelText" width="150">
		<siga:Idioma key="factSJCS.datosPagos.literal.pago"/>&nbsp;(*)
	</td>				
	<td>
		<siga:ComboBD nombre="idPago" tipo="cmb_PagosCerrados" parametro="<%=comboParams%>" clase="boxCombo" obligatorio="false"/>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.idioma"/>&nbsp;(*)
	</td>				
	<td>
		<siga:ComboBD nombre="idioma" tipo="cmbIdioma"  clase="boxCombo" obligatorio="false" />
	</td>
	</tr>



	</html:form>

	</table>
	</siga:ConjCampos>		


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function accionGenerarInforme() 
		{		
				var f=document.getElementById("mantenimientoInformesForm");
    			var fname = document.getElementById("mantenimientoInformesForm").name;
    			sub();
  			    if (validateMantenimientoInformesForm(f)) {
					f.modo.value="generarColegiadopago";
					// con pantalla de espera
				    document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoInforme';
				}else{
				
					fin();
					return false;
				} 
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->


	<!-- INICIO: IFRAME LISTA RESULTADOS -->
<!--	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					style="position:absolute; width:964; height:400; z-index:2; top: 72px; left: 0px">
	</iframe> -->
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
