<!-- pestanaProgrCalendariosProg.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
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


	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="sjcs.guardias.programacionCalendarios.programacion" localizacion="sjcs.guardias.programacionCalendarios.localizacion"/>	
	
	<!-- FIN: TITULO Y LOCALIZACION -->

<script language="JavaScript">

function refrescarLocal (){

	document.getElementById('idBuscarProgrCalendarios').onclick();
}

function preAccionBuscarProgrCalendarios (){
	sub();
	//document.ProgrCalendariosForm.target = 'mainPestanas';
}
function postAccionBuscarProgrCalendarios (){
	fin();
	document.ProgrCalendariosForm.modo.value ="";
	ajustarCabeceraTabla();
	ajusteAltoBotones('divProgrCalendarios');
}


function accionNuevaProgrCalendarios(){
	document.ProgrCalendariosForm.modo.value="nuevaProgrCalendarios";
	var resultado = ventaModalGeneral(document.ProgrCalendariosForm.name,"G");
	if (resultado=='MODIFICADO') {
		
		document.getElementById('idBuscarProgrCalendarios').onclick();
		
	}

	
}

function ajustarCabeceraTabla(){
	if (document.getElementById("listadoProgr").clientHeight < document.getElementById("listadoProgrDiv").clientHeight) {
		document.getElementById("listadoProgrCab").width='100%';
		   
	  } else {
		  document.getElementById("listadoProgrCab").width='98.43%';
		   
	  }
}


function consultar(fila)
{
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="consultarProgrCalendarios";
	var resultado = ventaModalGeneral(document.ProgrCalendariosForm.name,"G");
	
	
	
}
function borrar(fila)
{
	if(!confirm("<siga:Idioma key='gratuita.calendarios.programacion.confirmar.borrarProgramacion'/>"))
		return false;
	sub();
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="borrarProgrCalendarios";
	document.ProgrCalendariosForm.submit();
	
	
	
	
		
	
}
function editar(fila)
{
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="editarProgrCalendarios";
	var resultado = ventaModalGeneral(document.ProgrCalendariosForm.name,"G");
	if(resultado=='MODIFICADO'){
		document.getElementById('idBuscarProgrCalendarios').onclick();
	}

	
		
	
}
function adelantarProgrCalendarios(fila)
{	if(!confirm("<siga:Idioma key='gratuita.calendarios.programacion.confirmar.adelantarProgramacion'/>"))
		return false;
	sub();
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="adelantarProgrCalendarios";
	document.ProgrCalendariosForm.submit();
	
	
		
	
}
function reprogramarCalendarios(fila)
{
	if(!confirm("<siga:Idioma key='gratuita.calendarios.programacion.confirmar.reprogramarProgramacion'/>"))
		return false;
	sub();
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="reprogramarCalendarios";
	document.ProgrCalendariosForm.submit();
	
		
	
}
function cancelarGeneracionCalendarios(fila)
{
	
	if(!confirm("<siga:Idioma key='gratuita.calendarios.programacion.confirmar.cancelarProgramacion'/>"))
		return false;
	sub();
	var idProgrCalendario = document.getElementById("idProgrCalendario_"+fila).value;
	document.ProgrCalendariosForm.idProgrCalendario.value = idProgrCalendario;
	document.ProgrCalendariosForm.modo.value="cancelarGeneracionCalendarios";
	document.ProgrCalendariosForm.submit();
	
		
	
}




</script>
	
</head>
 
<body>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="ConjuntoGuardiasForms" name="ConjuntoGuardiasForms" type="java.util.Collection" scope="request" />

<html:form action="${path}"   method="POST" target="submitArea">

<html:hidden property="idProgrCalendario" value=""/>
<html:hidden property="modo" value=""/>
<input type="hidden" name="actionModal" />

		<table width="100%" border="0">
			<tr>
				<td width="20%"></td>
				<td width="30%"></td>
				<td width="15%"></td>
				<td width="35%"></td>
			</tr>
			<tr>				
			
					<td class="labelText">
					<siga:Idioma key='gratuita.calendarios.programacion.fechaProgramada'/>
					&nbsp;
					<siga:Idioma key='general.literal.desde'/>
					
					</td>
					<td >
					
						<siga:Fecha nombreCampo="fechaProgrDesde"></siga:Fecha>
	
					</td>
		
					<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>
					<td>
						<siga:Fecha nombreCampo="fechaProgrHasta" campoCargarFechaDesde="fechaProgrDesde"></siga:Fecha>
					</td>
					
				</tr>
			<tr>
			<tr>
				<td class="labelText"><siga:Idioma key='gratuita.calendarios.programacion.conjuntoGuardias'/></td>
				<td><html:select styleClass="boxCombo" style="width:320px;"
								property="idConjuntoGuardia"  >
								
						<html:option value="">&nbsp;</html:option>
						<html:optionsCollection name="ConjuntoGuardiasForms" value="idConjuntoGuardia"
									label="descripcion" />
					</html:select>
				</td>
				<td class="labelText"><siga:Idioma key="gratuita.calendarios.programacion.estado"/></td>
				<td><html:select property="estado" styleClass="boxCombo">
			
						<html:option value="">&nbsp;</html:option>
						<html:option value="0"><siga:Idioma key="gratuita.calendarios.programacion.estado.programada"/></html:option>
						<html:option value="1"><siga:Idioma key="gratuita.calendarios.programacion.estado.procesando"/></html:option>
						<html:option value="2"><siga:Idioma key="gratuita.calendarios.programacion.estado.generadaErrores"/></html:option>
						<html:option value="3"><siga:Idioma key="gratuita.calendarios.programacion.estado.generada"/></html:option>
						<html:option value="4"><siga:Idioma key="gratuita.calendarios.programacion.estado.cancelado"/></html:option>
						<html:option value="5"><siga:Idioma key="gratuita.calendarios.programacion.estado.reprogramada"/></html:option>
						
						
				</html:select>
				</td>
				
				
			</tr>
			
			
			<tr>				
					<td class="labelText">
					<siga:Idioma key="gratuita.calendarios.programacion.fechaCalendario"/>
					<siga:Idioma key='general.literal.desde'/>
					</td>
					<td>					
						<siga:Fecha nombreCampo="fechaCalInicio"></siga:Fecha>
	
					</td>
		
					<td class="labelText"><siga:Idioma key='general.literal.hasta'/></td>
					<td>						
						<siga:Fecha nombreCampo="fechaCalFin" campoCargarFechaDesde="fechaCalInicio"></siga:Fecha>
					</td>
					
				</tr>
			<tr>
			
				
		</table>


	</html:form>	
<table class="botonesSeguido" align="center">
<tr>
	<td  style="width:900px;">
		&nbsp;
	</td>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.new"/>"  id="idButton" onclick="return accionNuevaProgrCalendarios();" class="button" name="idButton" value="<siga:Idioma key="general.boton.new"/>">
	</td>
	<td class="tdBotones">
		<input type='button'  id = 'idBuscarProgrCalendarios' name='idButton' class="button" value='Buscar' alt='Buscar' class='busquedaProgrCalendarios'>
	</td>

</tr>
</table>
<div id="divProgrCalendarios" style='position:absolute;width:100%; '>
	<table id='progrCalendarios' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
	</table>
</div>	



<ajax:htmlContent
	baseUrl="/SIGA/JGR_ProgrCalendariosProgr.do?modo=getAjaxBusquedaProgrCalendarios"
	source="idBuscarProgrCalendarios"
	target="divProgrCalendarios"
	preFunction="preAccionBuscarProgrCalendarios"
	postFunction="postAccionBuscarProgrCalendarios"
	parameters="idConjuntoGuardia={idConjuntoGuardia},fechaProgrDesde={fechaProgrDesde},fechaProgrHasta={fechaProgrHasta},fechaCalInicio={fechaCalInicio},fechaCalFin={fechaCalFin},estado={estado}"/>

<!-- FIN: CAMPOS DE BUSQUEDA-->
<!-- Formularios auxiliares -->



			
<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		
				style="display: none"></iframe>

</body>
</html>