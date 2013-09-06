<!DOCTYPE html>
<html>
<head>
<!-- pestanaProgrCalendariosCal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>
 
<!-- IMPORTS -->
<!-- JSP -->


	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/displaytag.css'/>" />

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="sjcs.guardias.programacionCalendarios.generados" localizacion="sjcs.guardias.programacionCalendarios.localizacion"/>
	
	<!-- FIN: TITULO Y LOCALIZACION -->

<style type="text/css">
#listadoCalendariosDiv {
	overflow-x: hidden;
}
</style>

<script language="JavaScript">

function refrescarLocal (){

	document.getElementById('idBuscarCalendarios').onclick();
}

function preAccionBuscarCalendarios (){
	sub();
	//document.CalendariosForm.target = 'mainPestanas';
}
function ajustarCabeceraCalendario(){
	if (document.getElementById("listadoCalendarios").clientHeight < document.getElementById("listadoCalendariosDiv").clientHeight) {
		document.getElementById("listadoCalendariosCab").width='100%';
		   
	  } else {
		  document.getElementById("listadoCalendariosCab").width='98.43%';
		   
	  }
}

function postAccionBuscarCalendarios (){
	// document.CalendariosForm.modo.value ="";
	fin();
	ajustarCabeceraCalendario();
	ajusteAltoBotones('divCalendarios');
}


function selectRow(fila) {
	  // alert("fila");
	  //  document.getElementById('filaSelD').value = fila;
	  //  var tabla;
	   // tabla = document.getElementById('listadoCalendarios');
	   // for (var i=0; i<tabla.rows.length; i++) {
	    //  if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
	     // else          tabla.rows[i].className = 'filaTablaImpar';
	  //  }
	   //tabla.rows[fila].className = 'listaNonEditSelected';
	 }



function consultar(fila)
{
	var estado = document.getElementById("estado_"+fila).value;
	var idTurno = document.getElementById("idTurno_"+fila).value;
	var idGuardia = document.getElementById("idGuardia_"+fila).value;
	var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
	document.CalendarioGuardiasForm.idTurno.value = idTurno;
	document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
	document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
	document.CalendarioGuardiasForm.accion.value = 'consultaDesdeProgramacion';
	document.CalendarioGuardiasForm.modo.value="Ver";
	var resultado = ventaModalGeneral(document.CalendarioGuardiasForm.name,"G");
	
	
	
}
function borrar(fila)
{
	
	if(!confirm('<siga:Idioma key="messages.deleteConfirmation"/>'))
		return false;
	sub();

	var estado = document.getElementById("estado_"+fila).value;
	var idTurno = document.getElementById("idTurno_"+fila).value;
	var idGuardia = document.getElementById("idGuardia_"+fila).value;
	var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
	
	
	document.CalendarioGuardiasForm.idTurno.value = idTurno;
	document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
	document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
	document.CalendarioGuardiasForm.accion.value = 'borrarDesdeProgramacion';
	
	document.CalendarioGuardiasForm.modo.value="Borrar";

	var auxTarget = document.CalendarioGuardiasForm.target;
   	document.CalendarioGuardiasForm.target="submitArea";
	document.CalendarioGuardiasForm.submit();
   	document.CalendarioGuardiasForm.target=auxTarget;

	
	
	
	
		
	
}
function editar(fila)
{
	var estado = document.getElementById("estado_"+fila).value;
	var idTurno = document.getElementById("idTurno_"+fila).value;
	var idGuardia = document.getElementById("idGuardia_"+fila).value;
	var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
	
	document.CalendarioGuardiasForm.idTurno.value = idTurno;
	document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
	document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
	document.CalendarioGuardiasForm.accion.value = 'edicionDesdeProgramacion';
	document.CalendarioGuardiasForm.modo.value="Editar";
	var resultado = ventaModalGeneral(document.CalendarioGuardiasForm.name,"G");
	document.getElementById('idBuscarCalendarios').onclick();

}
function pepe(){
	alert("error");
	
}

function descargaLog(fila)
{
    var idTurno = document.getElementById("idTurno_"+fila).value;
	var idGuardia = document.getElementById("idGuardia_"+fila).value;
	var idCalendarioGuardias = document.getElementById("idCalendarioGuardias_"+fila).value;
	document.CalendarioGuardiasForm.idTurno.value = idTurno;
	document.CalendarioGuardiasForm.idGuardia.value = idGuardia;
	document.CalendarioGuardiasForm.idCalendarioGuardias.value = idCalendarioGuardias;
	document.CalendarioGuardiasForm.accion.value = 'descargaDesdeProgramacion';
	document.CalendarioGuardiasForm.target="submitArea";
	document.CalendarioGuardiasForm.modo.value="descargarLog";
	document.CalendarioGuardiasForm.submit();
}


</script>
	
</head>
 
<body>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="calendarioForms" name="calendarioForms" type="java.util.Collection" scope="request" />

<html:form action="${path}"   method="POST" target="submitArea">

<html:hidden styleId="modo"  property="modo" value=""/>
<input type="hidden" name="actionModal"  id="actionModal" />

		<table width="100%" border="0">
			<tr>
				<td width="20%"></td>
				<td width="30%"></td>
				<td width="15%"></td>
				<td width="35%"></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.calendarios.programacion.turno" /></td>
				<td><html:select styleId="turnos" styleClass="boxCombo" style="width:320px;"
					property="idTurnoCalendario">
					<bean:define id="turnos" name="DefinirCalendarioGuardiaForm"
						property="turnos" type="java.util.Collection" />
					<html:optionsCollection name="turnos" value="idTurno"
						label="nombre" />
					</html:select>
				</td>
				<td class="labelText"><siga:Idioma
					key="gratuita.calendarios.programacion.guardia" /></td>
				<td><html:select styleId="guardias" styleClass="boxCombo" style="width:320px;"
					property="idGuardiaCalendario" >
					<bean:define id="guardias" name="DefinirCalendarioGuardiaForm"
						property="guardias" type="java.util.Collection" />
					<html:optionsCollection name="guardias" value="idGuardia"
						label="nombre" />
				</html:select></td>
			</tr>
			
			
			
			<tr>
				
				<td class="labelText"><siga:Idioma key="gratuita.calendarios.programacion.estado"/></td>
				<td><html:select property="estado" styleId="estado" styleClass="boxCombo">
						
						<html:option value="">&nbsp;</html:option>
						<html:option value="0"><siga:Idioma key="gratuita.calendarios.programacion.estado.programada"/></html:option>
						<html:option value="1"><siga:Idioma key="gratuita.calendarios.programacion.estado.procesando"/></html:option>
						<html:option value="2"><siga:Idioma key="gratuita.calendarios.programacion.estado.generadaErrores"/></html:option>
						<html:option value="3"><siga:Idioma key="gratuita.calendarios.programacion.estado.generada"/></html:option>
						<html:option value="4"><siga:Idioma key="gratuita.calendarios.programacion.estado.cancelado"/></html:option>
						<html:option value="5"><siga:Idioma key="gratuita.calendarios.programacion.estado.pendienteManual"/></html:option>
						
						
						
				</html:select>
				</td>
				<td class="labelText"></td>
				<td>
				</td>
				
				
			</tr>
			
			
			<tr>				
					<td class="labelText"><siga:Idioma key="gratuita.calendarios.programacion.fechaCalendario"/>
					
					<siga:Idioma key='general.literal.desde'/></td>
					<td>					
						<siga:Fecha nombreCampo="fechaInicio"></siga:Fecha>
	
					</td>
		
					<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>
					<td>
						<siga:Fecha nombreCampo="fechaFin" campoCargarFechaDesde="fechaInicio"></siga:Fecha>
					</td>
					
				</tr>
			<tr>
			
				
		</table>
	<ajax:select
	baseUrl="/SIGA${path}.do?modo=getAjaxGuardias"
	source="turnos" target="guardias" parameters="idTurnoCalendario={idTurnoCalendario}"
	/>

	</html:form>	
	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" name="CalendarioGuardiasForm" type="com.siga.gratuita.form.DefinirCalendarioGuardiaForm" method="post" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idTurno" value = ""/>
		<html:hidden property = "idGuardia" value = ""/>
		<html:hidden property = "idCalendarioGuardias" value = ""/>
		<html:hidden property = "accion" value = ""/>
		<html:hidden property = "modoPestanha" value = ""/>

		<input type="hidden" name="actionModal" value="">
		
	</html:form>	
		
<table class="botonesSeguido" align="center">
<tr>
	<td  style="width:900px;">
		&nbsp;
	</td>
	
	<td class="tdBotones">
		<input type='button'  id = 'idBuscarCalendarios' name='idButton' class="button" value='Buscar' alt='Buscar' class='busquedaProgrCalendarios'>
	</td>

</tr>
</table>
<div id="divCalendarios" style='position:absolute;width:100%;overflow-x: hidden;'>
	<table id='calendarios' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed;overflow-x: hidden'>
	</table>
</div>	



<ajax:htmlContent
	baseUrl="/SIGA/JGR_ProgrCalendariosCal.do?modo=getAjaxBusquedaCalendarios"
	source="idBuscarCalendarios"
	target="divCalendarios"
	preFunction="preAccionBuscarCalendarios"
	postFunction="postAccionBuscarCalendarios"
	errorFunction="pepe"
	parameters="idTurnoCalendario={idTurnoCalendario},idGuardiaCalendario={idGuardiaCalendario},fechaInicio={fechaInicio},fechaFin={fechaFin},estado={estado}"/>

<!-- FIN: CAMPOS DE BUSQUEDA-->
<!-- Formularios auxiliares -->



			
<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		
				style="display: none"></iframe>

</body>
</html>