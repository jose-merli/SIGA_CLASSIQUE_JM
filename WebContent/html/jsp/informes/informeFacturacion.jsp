<!DOCTYPE html>
<html>
<head>
<!-- informeFacturacion.jsp -->
<!-- Pantalla con los combos para seleccionar la facturaciona a generar.
	 VERSIONES:
	 pilar.duran creado el  11-04-2008.
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
<%@ page import="java.util.Properties" %>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
	//COMBO PAGOS CERRADOS:
	String comboParams[] = new String[1];
	comboParams[0] = usrbean.getLocation();
	String informeUnico =(String) request.getAttribute("informeUnico");
%>	



<!-- HEAD -->


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
		titulo="factSJCS.informes.informeFacturacion.cabecera" 
		localizacion="factSJCS.informes.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body >
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<siga:ConjCampos leyenda="factSJCS.informes.informeFacturacion.cabecera">					
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCampos" align="center">

	

	<!-- FILA -->
	<tr>				
	<td class="labelText" width="150">
		<siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/>&nbsp;(*)
	</td>				
	<td>
		<siga:ComboBD nombre="idFacturacion" tipo="cmb_FactInformes" parametro="<%=comboParams%>" clase="boxCombo" obligatorio="true" obligatorioSinTextoSeleccionar="true"/>
	</td>
	</tr>

	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	</siga:ConjCampos>		




<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value="<%=usrbean.getLocation()%>"/>
	<html:hidden property="idTipoInforme" value="FACJG"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>



	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM"  />
	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function accionGenerarInforme() 
		{

			idFact = document.getElementById("idFacturacion").value;
			datos = "idFacturacion"+"=="+idFact;
			document.InformesGenericosForm.datosInforme.value=datos;
			if(document.getElementById("informeUnico").value=='1'){
				sub();
				document.InformesGenericosForm.submit();
			}else{
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
				   		
			   	}else {
			   		
				
				}
			}
				
		}

	</script>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>