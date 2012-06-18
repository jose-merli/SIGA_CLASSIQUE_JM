<!-- busquedaMovimientos.jsp -->
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
<%@ taglib uri="c.tld" prefix="c"%>
<!-- IMPORTS -->

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoMovimientosForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>



<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usr = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String pagoSel = "";
%>	
	
<%  
	//recoger el formulario de sesion
	MantenimientoMovimientosForm formulario = (MantenimientoMovimientosForm)request.getSession().getAttribute("MantenimientoMovimientosForm");

	//para el combo
	String[] dato = {usr.getLocation()};
	
	//Para ver si hay que refrescar la busqueda
	String buscar="";
	try{
		buscar = (String)formulario.getBuscar();
	}catch(Exception e){}
	String funcionBuscar = "";
	if ((buscar!=null)&&(buscar.equalsIgnoreCase("si"))) {
		funcionBuscar = "buscar2();";
	}

	
	//para refrescar el combo
	ArrayList pagoA = new ArrayList();
	try{
		pagoA.add((String)formulario.getPagoAsociado());
	}
	catch(Exception e){
		pagoA.add("0");
	}
		
%>	

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

			function accionPagoAsociado(varPagoAsociado)
			{
				if(varPagoAsociado.value ==''){
					document.getElementById("idCheckHistorico").checked = '';
					document.getElementById("idCheckHistorico").disabled = '';
				}else{
					document.getElementById("idCheckHistorico").checked = 'checked';
					document.getElementById("idCheckHistorico").disabled = 'disabled';
				}
			}
			
		</script>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.datosMovimientos.cabecera" 
		localizacion="factSJCS.datosMovimientos.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoMovimientosForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAlto('resultado');<%=funcionBuscar %>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
<div id="camposRegistro" class="posicionBusquedaSolo" align="center">
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center" border="0">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.datosMovimientos.leyenda">

	<table align="left">

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
    <html:form action="${path}?noReset=true" method="POST" target="submitArea">
		<html:hidden name="MantenimientoMovimientosForm" property = "modo" value = ""/>
		<html:hidden name="MantenimientoMovimientosForm" property = "actionModal" value = ""/>
		<html:hidden name="MantenimientoMovimientosForm" property="checkHistorico" value=""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<input type="hidden" name="botonBuscarPulsado" value="">
		<input type="hidden" name="checkHistoricoMovimiento" value="">
		
	<!-- FILA -->
	<tr>	
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosMovimientos.literal.nifCif"/>
		</td>			
		<td class="labelText">
			<html:text name="MantenimientoMovimientosForm" property="nif" maxlength="20" styleClass="box" readonly="false"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;					
			<siga:Idioma key="factSJCS.datosMovimientos.literal.nColegiado"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<html:text name="MantenimientoMovimientosForm" property="ncolegiado" maxlength="20" styleClass="box" readonly="false"/>
		</td>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosMovimientos.literal.descripcion"/>
		</td>
		<td>
			<html:text name="MantenimientoMovimientosForm" property="nombre" maxlength="100" styleClass="box" readonly="false"/>
		</td>	
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosMovimientos.literal.pago"/>
		</td>
		<td>
			&nbsp;	
	 		<siga:ComboBD nombre = "pagoAsociado" tipo="cmbPagoAsociado" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=pagoA%>" accion="accionPagoAsociado(this);" />
		</td>
		<td></td>
		<!-- Introducción del check de histórico -->
		<td class="labelText">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.checkHistorico"/>		
		</td> 		
		<td class="labelText">
			<input type="checkbox" id="idCheckHistorico" onclick="comprobarCheckHistorico(this);"/>			
		</td> 				
	</tr>
	<!-- FILA -->

	</html:form>

	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->

		<siga:ConjBotonesBusqueda botones="B,N"  titulo=""/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		//Funcion asociada a boton buscar
		function buscar() 
		{		
			sub();
			if(document.getElementById("idCheckHistorico").checked)
				document.forms[0].checkHistorico.value = "true";
			else
				document.forms[0].checkHistorico.value = "false";
				
			
			document.forms[0].botonBuscarPulsado.value="si";
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		//Función para cuando se busque por refresco
		function buscar2() 
		{		
			sub();
			document.forms[0].botonBuscarPulsado.value="no";
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		//Funcion asociada a boton nuevo
		function nuevo() 
		{	
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="submitArea";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			if (resultado=="MODIFICADO")buscar2();
		}
		function refrescarLocal()
		{
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrir";
			document.forms[0].submit();
		}

		function comprobarCheckHistorico(valorCheck){
			if (!valorCheck.checked){
				document.forms[0].checkHistoricoMovimiento.value="false";
			} else document.forms[0].checkHistoricoMovimiento.value="true";
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

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->



<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
</div>
 <iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>  	
<!-- FIN: SUBMIT AREA -->

</body>
</html>
