<!-- pestanaProgrCalendariosConf.jsp -->
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

<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>

<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="sjcs.guardias.programacionCalendarios.configuracion" localizacion="sjcs.guardias.programacionCalendarios.localizacion"/>	
	
	<!-- FIN: TITULO Y LOCALIZACION -->

<script language="JavaScript">
function accionNuevoConjuntoGuardias(){
	sub();
	if(document.getElementById('descripcionNuevoConjuntoGuardias').value==''){
		error = "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.nombreNuevoConjuntoGuardias'/>";
		alert(error);
		fin();
		return;
	}
	document.ConjuntoGuardiasForm.modo.value = 'insertarConjuntoGuardias';
	document.ConfConjuntoGuardiasForm.modo.value = '';
	document.ConjuntoGuardiasForm.descripcion.value = document.getElementById('descripcionNuevoConjuntoGuardias').value;

	var checksConfiguracion = document.getElementsByName("chkConfiguracion");
	var guardiasInsertar='';

	for (i = 1; i <= checksConfiguracion.length; i++) {
  	  	//var checkConfiguracion = checksConfiguracion[i];
  	  	var checkConfiguracion = document.getElementById("checkConfiguracion_"+i);
  		var orden = document.getElementById("orden_"+i).value;
		// Si estan marcados deberemos actulizar elementosInsertar o, si ha cambiado el orden actualizar elementosModificar
  		if(checkConfiguracion.checked && orden==''){
  			error = "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.orden'/>";
  			alert(error);
  			fin();
  	  		return false;
  			
  	  	}
	}
	/*
	
	for (i = 1; i <= checksConfiguracion.length; i++) {
  	  	//var checkConfiguracion = checksConfiguracion[i];
  	  	var checkConfiguracion = document.getElementById("checkConfiguracion_"+i);
  		var orden = document.getElementById("orden_"+i).value;
		// Si estan marcados deberemos actulizar elementosInsertar o, si ha cambiado el orden actualizar elementosModificar
  		if(checkConfiguracion.checked && orden==''){
  			error = "<siga:Idioma key='errors.required' arg0='gratuita.calendarios.programacion.orden'/>";
  			alert(error);
  			fin();
  	  		return false;
  			
  	  	}
	}
	*/ 
	
	
  	for (i = 1; i <= checksConfiguracion.length; i++) {
  		var checkConfiguracion = document.getElementById("checkConfiguracion_"+i); 
  	  	var idTurno = document.getElementById("idTurno_"+i).value;
  		var idGuardia = document.getElementById("idGuardia_"+i).value;
		// Si estan marcados deberemos actulizar elementosInsertar o, si ha cambiado el orden actualizar elementosModificar
  		if(checkConfiguracion.checked){
  			var orden = document.getElementById("orden_"+i).value;
  			if(orden!='')
  	  			guardiasInsertar += "idTurno=="+idTurno+",idGuardia=="+idGuardia+",orden=="+orden+"##";

  	  	  	

  	  	}
  		
  		
  	
  	  
	}
  	document.ConjuntoGuardiasForm.guardiasInsertar.value = guardiasInsertar;

	document.ConjuntoGuardiasForm.submit();
		
	
}
function refrescarLocal (){

	
	if(document.ConfConjuntoGuardiasForm.modo.value=='insertarConfiguracionConjuntoGuardias'){
		onChangeConjuntoGuardia();
	}else if(!document.ConfConjuntoGuardiasForm.modo.value){
		document.ConfConjuntoGuardiasForm.modo.value = 'actualizarConfiguracion';
		document.ConfConjuntoGuardiasForm.target = "mainPestanas";
		document.ConfConjuntoGuardiasForm.submit();
	}
	
}
function onChangeConjuntoGuardia (){
	document.getElementById('idBuscarConfConjuntoGuardias').onclick();
}
function preAccionBuscarConfConjuntoGuardias (){
	sub();
	if(!document.ConfConjuntoGuardiasForm.idConjuntoGuardia || document.ConfConjuntoGuardiasForm.idConjuntoGuardia.value==''){
		fin();
		return 'cancel';
	}
	
}
function postAccionBuscarConfConjuntoGuardias (){
	fin();
	ajustarCabeceraTabla();
	ajusteAltoBotones('divConfConjuntoGuardias');
}
function checkTodos(chkGeneral){

 	var ele = document.getElementsByName("chkConfiguracion");
  	for (i = 0; i < ele.length; i++) {
  	  	var obj = ele[i]; 
  	  obj.checked = chkGeneral.checked;
	}
		
}


function ajustarCabeceraTabla(){
	if (document.all.listadoConf.clientHeight < document.all.listadoConfDiv.clientHeight) {
		   document.all.listadoConfCab.width='100%';
		   
	  } else {
		   document.all.listadoConfCab.width='98.43%';
		   
	  }
}
function accionGuardar(){
	sub();
	if(!document.ConfConjuntoGuardiasForm.idConjuntoGuardia || document.ConfConjuntoGuardiasForm.idConjuntoGuardia.value==''){
		
		alert("<siga:Idioma key='gratuita.calendarios.programacion.error.conjuntoNoExistente'/>");
		fin();
		return false;
	}
		
	var checksConfiguracion = document.getElementsByName("chkConfiguracion");
	var guardiasBorrar='';
	var guardiasInsertar='';
	
  	for (i = 1; i <= checksConfiguracion.length; i++) {
  		var checkConfiguracion = document.getElementById("checkConfiguracion_"+i); 
  	  	var idTurno = document.getElementById("idTurno_"+i).value;
  		var idGuardia = document.getElementById("idGuardia_"+i).value;
  		var ordenOld = document.getElementById("ordenOld_"+i).value;
  		var checkConfiguracionOld = document.getElementById("checkConfiguracionOld_"+i);
		//Solo se van a actualizar los que esten marcados. Si tienen orden se insertaran, si no se borraran
  		//
  		if(checkConfiguracion.checked){
  			var orden = document.getElementById("orden_"+i).value;
  			
			if(ordenOld.value==''){
				guardiasInsertar += "idTurno=="+idTurno+",idGuardia=="+idGuardia+",orden=="+orden+"##";
			}else{
				if(orden==''){
					guardiasBorrar += "idTurno=="+idTurno+",idGuardia=="+idGuardia+",orden=="+ordenOld+"##";
				}else if(ordenOld!=orden){
					guardiasInsertar += "idTurno=="+idTurno+",idGuardia=="+idGuardia+",orden=="+orden+"##";
					guardiasBorrar += "idTurno=="+idTurno+",idGuardia=="+idGuardia+",orden=="+ordenOld+"##";
  	  	  		}
			}

  	  	}
  	  
	}
  	document.ConfConjuntoGuardiasForm.guardiasInsertar.value = guardiasInsertar;
  	document.ConfConjuntoGuardiasForm.guardiasBorrar.value = guardiasBorrar;
  	document.ConfConjuntoGuardiasForm.modo.value = "insertarConfiguracionConjuntoGuardias";
  	document.ConfConjuntoGuardiasForm.submit();
	
	

	
}
function accionBorrar(){
	
	if(!confirm("<siga:Idioma key='gratuita.calendarios.programacion.confirmar.borrarConjunto'/>"))
		return false;
	sub();
	document.ConjuntoGuardiasForm.idConjuntoGuardia.value = document.ConfConjuntoGuardiasForm.idConjuntoGuardia.value;
	document.ConjuntoGuardiasForm.modo.value = 'borrarConjuntoGuardias';
  	document.ConjuntoGuardiasForm.submit();
	
	

	
}
function onClickMostrarSoloGuardiasConfiguradas() {
	document.ConfConjuntoGuardiasForm.mostrarSoloGuardiasConfiguradas.value = document.getElementById('mostrarGuardiasConfiguradas').checked;
	onChangeConjuntoGuardia();
} 
function onclickCheckConfiguracion(fila) {
	if(document.getElementById("checkConfiguracion_"+fila).checked){
		document.getElementById("orden_"+fila).disabled = '';
	}
	else{
		var mensaje = "<siga:Idioma key='gratuita.calendarios.confirma.restaurarValor'/>";
		if(confirm(mensaje)) {
			document.getElementById("orden_"+fila).value = document.getElementById("ordenOld_"+fila).value;
			document.getElementById("orden_"+fila).disabled = 'disabled';					
			
		}
		else {
			
			document.getElementById("checkConfiguracion_"+fila).checked ='checked';
		}


		
		
	}
	
	
}



</script>
	
</head>
 
<body onload="onChangeConjuntoGuardia();">

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="ConjuntoGuardiasForms" name="ConjuntoGuardiasForms" type="java.util.Collection" scope="request" />

<html:form action="${path}"  method="POST" target="submitArea">
<html:hidden property="modo" value=""/>
<html:hidden property="guardiasBorrar" value=""/>
<html:hidden property="guardiasInsertar" value=""/>
<html:hidden property="mostrarSoloGuardiasConfiguradas" value=""/>


	

		<table width="100%" border="0">
			<tr>
				<td width="20%"></td>
				<td width="30%"></td>
				<td width="50%"></td>
				
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="gratuita.calendarios.programacion.conjuntoGuardias"/></td>
				<td colspan = "2"><html:select styleClass="boxCombo" style="width:320px;"
								property="idConjuntoGuardia" onchange="onChangeConjuntoGuardia();" >
						<html:optionsCollection name="ConjuntoGuardiasForms" value="idConjuntoGuardia"
									label="descripcion" />
					</html:select>
				</td>
				
				
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="gratuita.calendarios.programacion.nombreNuevoConjuntoGuardias"/> </td>
				<td>
					<input type="text" styleClass="box" style="width:320px;" id="descripcionNuevoConjuntoGuardias"/>
				</td>
				<td class="labelText">
				<input type="checkbox" id='mostrarGuardiasConfiguradas' onclick="onClickMostrarSoloGuardiasConfiguradas();" /> 
					&nbsp;
					<siga:Idioma key="gratuita.calendarios.programacion.mostrarSoloConfiguradas"/>
				</td>
			</tr>
				
		</table>
<table class="botonesSeguido" align="center">
	<tr>
		<td class="titulitos"></td>
				<td class="tdBotones">
					
				<input type='button'  id = 'idBuscarConfConjuntoGuardias' name='idButton' style="display:none" value='Buscar' alt='Buscar' class='busquedaConfConjuntoGuardias'>
				</td>
			</tr>
	</table>

<div id="divConfConjuntoGuardias" style=''position:absolute;width:100%; '>
	<table id='confConjuntoGuardias' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
	</table>
</div>	

</html:form>

<table class="botonesDetalle" align="center">
<tr>
	<td  style="width:900px;">
		&nbsp;
	</td>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.new"/>"  id="idButton" onclick="return accionNuevoConjuntoGuardias();" class="button" name="idButton" value="<siga:Idioma key="general.boton.new"/>">
	</td>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.borrar"/>"  id="idButton" onclick="return accionBorrar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.borrar"/>">
	</td>
	<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key="general.boton.guardar"/>" onclick="return accionGuardar();" class="button" name="idButton"  value="<siga:Idioma key="general.boton.guardar"/>">
	</td>

</tr>
</table>

<ajax:htmlContent
	baseUrl="/SIGA/JGR_ProgrCalendariosConf.do?modo=getAjaxBusquedaConfConjuntoGuardias"
	source="idBuscarConfConjuntoGuardias"
	target="divConfConjuntoGuardias"
	preFunction="preAccionBuscarConfConjuntoGuardias"
	postFunction="postAccionBuscarConfConjuntoGuardias"
	parameters="idConjuntoGuardia={idConjuntoGuardia},mostrarSoloGuardiasConfiguradas={mostrarSoloGuardiasConfiguradas}"/>

<!-- FIN: CAMPOS DE BUSQUEDA-->
<!-- Formularios auxiliares -->
<html:form action="/JGR_ConjuntoGuardiaProgrCalendarios" method="POST"
	target="submitArea" type="" style="display:none">
	<html:hidden property="modo"/>
	<html:hidden property="descripcion"/>
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idConjuntoGuardia"/>
	<html:hidden property="guardiasInsertar"/>
	
	
	
</html:form>


			
<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		
				style="display: none"></iframe>

</body>
</html>