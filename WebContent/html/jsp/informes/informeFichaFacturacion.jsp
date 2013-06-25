<!-- informeFichaFacturacion.jsp -->
<!-- Pantalla con los combos para seleccionar la facturaciona a generar.
	 VERSIONES:
	 david.sanchezp_ creado el  30-05-2005.
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
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	//COMBO PAGOS CERRADOS:
	String comboParams[] = new String[1];
	comboParams[0] = usrbean.getLocation();
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoInformesFacturacionesForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.informes.facturacion.cabecera" 
		localizacion="factSJCS.informes.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body >

	<siga:ConjCampos leyenda="factSJCS.informes.facturacion.cabecera">					
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCampos" align="center">
	<html:form action="/INF_FichaFacturacion.do" method="POST" target="submitArea">
	<html:hidden name="mantenimientoInformesForm" property = "modo" value = ""/>

	<!-- FILA -->
	<tr>				
	<td class="labelText" width="150">
		<siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/>&nbsp;(*)
	</td>				
	<td>
		<siga:ComboBD nombre="idFacturacion" tipo="cmb_FactInformes" parametro="<%=comboParams%>" clase="boxCombo" obligatorio="false"/>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="factSJCS.datosPagos.literal.idioma"/>
	</td>				
	<td>
		<siga:ComboBD nombre="idioma" tipo="cmbIdioma"  clase="boxCombo" obligatorio="false" />
	</td>
	</tr>

	</html:form>
	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	</siga:ConjCampos>		

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM"  />
	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function accionGenerarInforme() 
		{		
				sub();
				var f=document.getElementById("mantenimientoInformesForm");
    			var fname = document.getElementById("mantenimientoInformesForm").name;
  			    if (validateMantenimientoInformesFacturacionesForm(f)) {
					f.modo.value="generarFichaFacturacion";
					// con pantalla de espera
				    window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoInforme';
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
					class="frameGeneral">
	</iframe> -->
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>