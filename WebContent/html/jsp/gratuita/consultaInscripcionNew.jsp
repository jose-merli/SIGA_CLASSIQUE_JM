<!-- validarInscripcion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- JSP -->

<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaTurnos.literal.title" /></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>

<script type="text/javascript">
	
	
		
		function accionCerrar() 
		{		
			window.top.close();
		}
		
	</script>
</head>

<body>


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.gestionInscripciones.consulta.titulo" />
		</td>
	</tr>
</table>


<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
<!-- INICIO: CAMPOS -->
<!-- Zona de campos de busqueda o filtro -->



	<table  class="tablaCampos" id='inscripciones' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		
		
		
		<tr class="tableTitle">
			
			<td width="8%">Designa</td>
			<td width="8%">EJG</td>
			
			<td width="12%">Juzgado</td>
			<td width="8%">F. Salida</td>
			<td width="8%">Asunto</td>
			<td width="15%">Cliente</td>
			<td width="4%">Cat</td>
			<td width=4%">N.Act</td>
			<td width="15%">Acreditaciones</td>
			<td width="3%"><input type="checkbox"/></td>
			<td colspan="2" width="6%">Botones</td>
			
			<td width="4%">Baja</td>
		</tr>
		<tr class="filaTablaImpar">
		
			<td rowspan="2">2010/0000</td>
			<td rowspan="2">2010/00000</td>
			<td rowspan="2">Jz. Intruccion Nº 1 </td>
			<td rowspan="2">00/00/2010</td>
			<td rowspan="2">Sentencia 0</td>
			<td rowspan="2">Miguel Requena</td>
			<td rowspan="2">MRR</td>
			<td rowspan="2">&nbsp;</td>
			<td><input type="checkbox"/>Acred 0.1(11%)</td>
			<td><input type="checkbox"/></td>
			<td rowspan="2" colspan="2">
				
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
				<img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
			
			</td>
			<td rowspan="2" ><input type="checkbox"/></td>
			
		</tr>
		<tr class="filaTablaImpar">
			
			<td><input type="checkbox"/>Acred 0.2(22%)</td>
			<td><input type="checkbox"/></td>
			
			
		</tr>
		
		
		
		
		
		
		
		
		<tr class="filaTablaPar">
			
			<td rowspan="3">2010/00001</td>
			<td rowspan="3">2010/00001</td>
			<td rowspan="3">Jz. Intruccion Nº 1 </td>
			<td rowspan="3">01/01/2010</td>
			<td rowspan="3">Sentencia 2</td>
			<td rowspan="3">David Casino</td>
			<td rowspan="3">PTE</td>
			<td rowspan="3"> 1</td>
			
			<td>25/10/2010:Acr-ini(30%) </td>
			<td ><input type="checkbox"/></td>
			<td rowspan="3" colspan="2">
				
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
				<img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
			</td>
			
			
			<td rowspan="3"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaPar">
			
			<td><input type="checkbox"/>Acre Fin(70%)</td>
			<td><input type="checkbox"/></td>
		
		</tr>
		<tr class="filaTablaPar">
     
			<td><input type="checkbox"/>Otra Acred(080%)</td>
			<td><input type="checkbox"/></td>

		</tr>
		
		<tr class="filaTablaImpar">
			
			<td rowspan="2">2010/00001</td>
			<td rowspan="2">2010/00001</td>
			<td rowspan="2">Jz. Intruccion Nº 1 </td>
			<td rowspan="2">01/01/2010</td>
			<td rowspan="2">Sentencia 2</td>
			<td rowspan="2">Pedro Ximenez</td>
			<td>JTA</td>
			<td> 1</td>
			<td>25/10/2010:Acr-ini(30%) </td>
			<td><input type="checkbox"/></td>
			
			<td>
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
			
			</td>
			<td rowspan="2">
				<img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
			
			</td>
			
			<td rowspan="2"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaImpar">
			<td>JTA</td>
			<td>2</td>
			<td>25/10/2010:Acre Fin(70%) </td>
			<td><input type="checkbox"/></td>
			<td>
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
			
			</td>
			
		
		</tr>
		
		
		
		<!-- 
		<tr class="filaTablaPar">
			
			<td rowspan="5">2010/00001</td>
			<td rowspan="5">2010/00001</td>
			<td rowspan="5">Jz. Intruccion Nº 1 </td>
			<td rowspan="5">01/01/2010</td>
			<td rowspan="5">Sentencia 1</td>
			<td rowspan="5">Juan Perez</td>
			<td rowspan="5">JTA</td>
			<td rowspan="3"> 1.1</td>
			<td><input type="checkbox"/>Acred 1.1.1(11%)</td>
			<td rowspan="3">
				<img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
			
			</td>
			<td rowspan="3"><input type="checkbox"/></td>
			<td rowspan="3"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaPar">
     
			
			
			
			
			
			
			
			
			<td><input type="checkbox"/>Acred 1.1.2(11%)</td>
		
		</tr>
		<tr class="filaTablaPar">
     
			
			
			
			
			
			
			
			
			<td><input type="checkbox"/>Acred 1.1.3(11%)</td>

		</tr>
		<tr class="filaTablaPar">
		
			<td rowspan="2"> 1.2</td>
			<td><input type="checkbox"/>Acred 1.2.1(11%)</td>
			<td rowspan="2"><img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)"></td>
				<td rowspan="2"><input type="checkbox"/></td>
				<td rowspan="2"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaPar">
			<td><input type="checkbox"/>Acred 1.2.2(11%)</td>
			
		</tr>
		
		
		
		<tr class="filaTablaImpar">
		
			<td rowspan="5">2010/0002</td>
			<td rowspan="5">2010/00002</td>
			<td rowspan="5">Jz. Intruccion Nº 2 </td>
			<td rowspan="5">02/02/2010</td>
			<td rowspan="5">Sentencia 2</td>
			<td rowspan="5">Pedro Ximenez</td>
			<td rowspan="5">LTC</td>
			<td rowspan="2"> 2.1</td>
			<td><input type="checkbox"/>Acred 2.1.1(11%)</td>
			<td rowspan="2">
				<img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)">
			
			</td>
			<td rowspan="2"><input type="checkbox"/></td>
			<td rowspan="2"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaImpar">
			
			<td><input type="checkbox"/>Acred 2.1.2(11%)</td>
			
		</tr>
		
		<tr class="filaTablaImpar">
			
			<td rowspan="3"> 2.2</td>
			<td><input type="checkbox"/>Acred 2.2.1(11%)</td>
			<td rowspan="3"><img  src="/SIGA/html/imagenes/icono+.gif" style="cursor:hand;" alt="<siga:Idioma key="gratuita.volantesExpres.nuevaActuacion"/>" name="" border="0" >
				<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="Editar" name="editar_1" border="0" onClick=" selectRow(1); editar(1); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('editar_1','','/SIGA/html/imagenes/beditar_on.gif',1)"></td>
				<td rowspan="3"><input type="checkbox"/></td>
				<td rowspan="3"><input type="checkbox"/></td>
		</tr>
		<tr class="filaTablaImpar">
     
			
			<td><input type="checkbox"/>Acredn 2.2.2(11%)</td>
			
		</tr>
		<tr class="filaTablaImpar">
     
			
			<td><input type="checkbox"/>Acred 2.2.3(11%)</td>
			
		</tr>
		 -->
		
		
<!-- FIN: SUBMIT AREA -->

</body>
</html>
