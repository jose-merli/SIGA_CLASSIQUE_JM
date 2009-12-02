<!-- volantesExpress.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ taglib uri="c.tld" prefix="c"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />



<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:TituloExt titulo="gratuita.volantesExpres.literal.titulo"	localizacion="gratuita.volantesExpres.literal.localizacion" />

<script type="text/javascript">
		function accionCalendario() 
		{
			// Abrimos el calendario 
			
			var resultado = showModalDialog("<html:rewrite page='/html/jsp/general/calendarGeneral.jsp'/>?valor="+document.VolantesExpressForm.fechaGuardia.value,document.VolantesExpressForm.fechaGuardia,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");
			if (resultado) {
				
				 document.VolantesExpressForm.turnos.value= '';
				 document.VolantesExpressForm.guardias.value= '';
				 document.VolantesExpressForm.colegiadosGuardia.value= '';
				 document.VolantesExpressForm.colegiadosSustituidos.value= '';
				 document.VolantesExpressForm.idTurno.value = '';
			     document.VolantesExpressForm.idGuardia.value = '';
			     document.VolantesExpressForm.idColegiado.value = '';
				 document.VolantesExpressForm.idColegiadoGuardia.value = '';
				 document.VolantesExpressForm.idColegiadoSustituido.value = '';
				
				 document.VolantesExpressForm.fechaGuardia.value = resultado;
				 document.getElementById('fechaGuardia').onchange();
				
		 	}else{
		 		 document.VolantesExpressForm.turnos.value= '';
				 document.VolantesExpressForm.guardias.value= '';
				 document.VolantesExpressForm.colegiadosGuardia.value= '';
				 document.VolantesExpressForm.colegiadosSustituidos.value= '';
				 document.VolantesExpressForm.idTurno.value = '';
			     document.VolantesExpressForm.idGuardia.value = '';
			     document.VolantesExpressForm.idColegiado.value = '';
				 document.VolantesExpressForm.idColegiadoGuardia.value = '';
				 document.VolantesExpressForm.idColegiadoSustituido.value = '';
				
				 document.VolantesExpressForm.fechaGuardia.value = '';
				 document.getElementById('fechaGuardia').onchange();
			} 
		}
		
	function postAccionColegiadoGuardia(){
		idColegiadoGuardia = document.VolantesExpressForm.idColegiadoGuardia.value;
		
		if (idColegiadoGuardia!="-1"){
			document.VolantesExpressForm.idColegiadoSustituido.value = '-1';
			document.getElementById("colegiadosSustituidos").disabled= true;
		}else{
     		document.getElementById("colegiadosSustituidos").disabled= false;
		}
		actualizarResultados();
	}
	function postAccionColegiado(){
		//Comprobamos que esta en el combo
		var idColegiado = document.VolantesExpressForm.idColegiado.value;
		var optionsColegiadoGuardia = document.getElementById("colegiadosGuardia");
		var encontrado;
		for(var i = 0 ; i <optionsColegiadoGuardia.length ; i++) {
			var option = optionsColegiadoGuardia.options[i].value;
			if(option === idColegiado){
				encontrado=i;
			}
		}
		if (encontrado){
			optionsColegiadoGuardia.selectedIndex=encontrado;
		} else {
			optionsColegiadoGuardia.selectedIndex=0;
		}
		postAccionColegiadoGuardia();
	}
	function postAccionTurno(){
		if((document.VolantesExpressForm.idTurno && document.VolantesExpressForm.idTurno.value != ''&& document.VolantesExpressForm.idTurno.value != '-1')){
			if(document.VolantesExpressForm.idColegiado){
				postAccionColegiado();
			}else{
				actualizarResultados();
			}
		}
					
	}	
	function postAccionGuardia(){
		if((document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')){
			if(document.VolantesExpressForm.idColegiado){
				postAccionColegiado();
			}else{
				actualizarResultados();
			}
		}
					
	}
	function actualizarResultados(){
		
		if((document.VolantesExpressForm.fechaGuardia && document.VolantesExpressForm.fechaGuardia.value != '')&&
		(document.VolantesExpressForm.idTurno && document.VolantesExpressForm.idTurno.value != ''&& document.VolantesExpressForm.idTurno.value != '-1')&&
	    (document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')&&
		(document.VolantesExpressForm.idTipoAsistenciaColegio && document.VolantesExpressForm.idTipoAsistenciaColegio.value != ''&& document.VolantesExpressForm.idTipoAsistenciaColegio.value != '-1')&&
		((document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '')||
		(document.VolantesExpressForm.idColegiadoGuardia && document.VolantesExpressForm.idColegiadoGuardia.value!= ''&& document.VolantesExpressForm.idColegiadoGuardia.value!= '-1'))){
			document.getElementById('idBuscarAsistencias').onclick();
		}else{
			table = document.getElementById('asistencias');
			if(table){			
				for(var i = table.rows.length - 1; i > 0; i--)
				{
					table.deleteRow(i);
				}
			}
			var divAsistencias = document.getElementById("divAsistencias");
			if(divAsistencias) 
				 divAsistencias.innerHTML="";
			
		}
	}
	function limpiarColegiado(){
		document.VolantesExpressForm.idColegiado.value = '';
		document.VolantesExpressForm.numeroColegiado.value = '';
		document.VolantesExpressForm.nombreColegiado.value = '';
		actualizarResultados();
	}
	function buscarColegiado(){
		var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
		if (resultado!=undefined && resultado[0]!=undefined ){
			
			document.VolantesExpressForm.idColegiado.value       = resultado[0];
			document.VolantesExpressForm.numeroColegiado.value    = resultado[2];
			document.VolantesExpressForm.nombreColegiado.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
			postAccionColegiado();
		}
	}
	
	function refrescarLocal(){
			
	}
	function validarDatosMinimos () {
	
		if (!(document.VolantesExpressForm.fechaGuardia && document.VolantesExpressForm.fechaGuardia.value != '')) {
			var campo = '<siga:Idioma key="gratuita.volantesExpres.literal.GuardiaDia"/>';
 				var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
 				alert (msg);				
			return false;
		}
		
		if (!(document.VolantesExpressForm.idTipoAsistenciaColegio && document.VolantesExpressForm.idTipoAsistenciaColegio.value != ''&& document.VolantesExpressForm.idTipoAsistenciaColegio.value != '-1')) {
			alert ('<siga:Idioma key="gratuita.volantesExpres.literal.tipoAsistenciaColegio"/>');
			return false;
		}
		if (!(document.VolantesExpressForm.idTurno && document.VolantesExpressForm.idTurno.value != ''&& document.VolantesExpressForm.idTurno.value != '-1')) {
			alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarTurno"/>');
			return false;
		}
		if (!(document.VolantesExpressForm.idGuardia && document.VolantesExpressForm.idGuardia.value != ''&& document.VolantesExpressForm.idGuardia.value != '-1')) {
			alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarGuardia"/>');
			return false;
		}
		if (!((document.VolantesExpressForm.idColegiado && document.VolantesExpressForm.idColegiado.value != '')||
				(document.VolantesExpressForm.idColegiadoGuardia && document.VolantesExpressForm.idColegiadoGuardia.value!= ''&& document.VolantesExpressForm.idColegiadoGuardia.value!= '-1'))) {
			alert ('<siga:Idioma key="gratuita.volantesExpres.mensaje.seleccionarColegiado"/>');
			return false;
		}
		return true;
	}
	function accionInsertarRegistroTabla () 
	{
		var validado = validarDatosMinimos (); 
		if(!validado){
			return;
		}
		
		crearFila();
	} 
	function preAccionGuardarAsistencias(){
		sub();
		var validado = validarDatosMinimos ();
		if(!validado){
			fin();
			return 'cancel';
		}
		datosAsistencias = getDatos('asistencias');
		if(!datosAsistencias){
			fin();
			return false;
		}
		document.VolantesExpressForm.datosAsistencias.value = datosAsistencias;
	}

	function postAccionGuardarAsistencias(){
		fin();
	}

	function preAccionBuscarAsistencias(){
		sub();
	}
	function postAccionBuscarAsistencias(){
		fin();
		
	}
	function getDatos(idTabla) 
	{	
		table = document.getElementById(idTabla);
		filas = table.rows.length;
		// Datos Dinamicos Asistencias
		var datos = "", accion = "";
			
		for (i = 0; i < filas ; i++) {
			 
			var validado = validarDatosFila (i);
			  
			if (!validado) {
				fin();
				return false;
			}
	              
			claveAnio = document.getElementById("claveAnio_" + i).value;
			datos += 'claveAnio='+claveAnio;
			datos += ',';
			claveNumero = document.getElementById("claveNumero_" + i).value;
			datos += 'claveNumero='+claveNumero;
			datos += ',';
			claveIdInstitucion = document.getElementById("claveIdInstitucion_" + i).value;
			datos += 'claveIdInstitucion='+claveIdInstitucion;
			datos += ',';
			
			
			hora = document.getElementById("hora_" + i).value;
			datos += 'hora='+hora;
			datos += ',';
			
			minuto = document.getElementById("minuto_" + i).value;
			datos += 'minuto='+minuto;
			datos += ',';
			
			if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") {
		    	lugar = document.getElementById("comisaria_"+ i).value;
				datos += 'comisaria='+lugar;
				datos += ',';
				datos += 'juzgado=';
				datos += ',';
			}else{
				lugar = document.getElementById("juzgado_"+ i).value;
				datos += 'comisaria=';
				datos += ',';
				datos += 'juzgado='+lugar;
				datos += ',';
			}
			
			
			idPersona = document.getElementById("idPersona_" + i).value;
			datos += 'idPersona='+idPersona;
			datos += ',';
			
			dni = document.getElementById("dni_" + i).value;
			dni = replaceAll(dni,',','~');
               dni = replaceAll(dni,'=','¬');
			datos += 'dni='+dni;
			datos += ',';
			
			nombre = document.getElementById("nombre_" + i).value;
			nombre = replaceAll(nombre,',','~');
               nombre = replaceAll(nombre,'=','¬');
			datos += 'nombre='+nombre;
			datos += ',';
			
			apellido1 = document.getElementById("apellido1_" + i).value;
			apellido1 = replaceAll(apellido1,',','~');
               apellido1 = replaceAll(apellido1,'=','¬');
			datos += 'apellido1='+apellido1;
			datos += ',';
			
			apellido2 = document.getElementById("apellido2_" + i).value;
			apellido2 = replaceAll(apellido2,',','~');
               apellido2 = replaceAll(apellido2,'=','¬');
			datos += 'apellido2='+apellido2;
			datos += ',';
			
			

               diligencia = document.getElementById("diligencia_" + i).value;
               diligencia = replaceAll(diligencia,',','~');
               diligencia = replaceAll(diligencia,'=','¬');
               
               datos += 'diligencia='+diligencia;
               datos += ',';
							
			observaciones = document.getElementById("observaciones_" + i).value;
			observaciones = replaceAll(observaciones,',','~');
			observaciones = replaceAll(observaciones,'=','¬');
			datos += 'observaciones='+observaciones;
			datos += ',';
			
			idDelito = document.getElementById("idDelito_" + i).value;
			datos += 'idDelito='+idDelito;
			datos += ',';
			
			ejgNumero = document.getElementById("ejgNumero_" + i).value;
			datos += 'ejgNumero='+ejgNumero;
			datos += ',';
			
			ejgAnio = document.getElementById("ejgAnio_" + i).value;
			datos += 'ejgAnio='+ejgAnio;
			datos += ',';
			
			ejgTipo = document.getElementById("ejgTipo_" + i).value;
			datos += 'ejgTipo='+ejgTipo;
			datos += "%%%";
			
		}
		return datos;
			
	}
		
	function crearFila () 
		{ 
		table = document.getElementById("asistencias");
		numFila = table.rows.length;
		tr = table.insertRow();
		tr.id = "fila_" + numFila;
		tr.align = "center";

		td = tr.insertCell();
		td.setAttribute("width", "6%");
		td.setAttribute("align", "center");
		td.className = "";
		td.innerText="";
		td.innerHTML = '<input type="hidden" id="claveAnio_' + numFila + '" value=""> ' +
		                  '<input type="hidden" id="claveNumero_' + numFila + '" value="">' + 
		                  '<input type="hidden" id="claveIdInstitucion_' + numFila + '" value="">' +
		                  '<input type="hidden" id="numero_designa_' + numFila + '" value=""> ' +
		                  '<input type="hidden" id="ejgNumero_' + numFila + '" value=""> ' +
		                  '<input type="hidden" id="ejgAnio_' + numFila + '" value=""> ' +
		                  '<input type="hidden" id="ejgTipo_' + numFila + '" value=""> ' +
						  '<input type="text" id="hora_'   + numFila + '" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="" onBlur="validaHora(this);" /> : ' + 
		 				  '<input type="text" id="minuto_' + numFila + '" class="box" style="width:21;margin-top:3px;text-align:center;" maxLength="2" value="" onBlur="validaMinuto(this);" />';

		
		// centro detencion	/ Juzgado
		td = tr.insertCell(); 
		td.setAttribute("width", "27%");
		td.className = "";
		td.innerText="";
		aux = '';
		//Centro detencion
		if (document.VolantesExpressForm.lugar[0].checked && 
		    document.VolantesExpressForm.lugar[0].value == "centro") {

			aux = '<input type="text" id="codComisaria_' + numFila + '" class="box" size="8"  style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerComisaria(' + numFila + ');" /> ' +
				  '<iframe ID="comisaria_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=comisaria_' + numFila + '&tipo=comboComisariasTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros='+document.VolantesExpressForm.idTurno.value+'&id='+ document.VolantesExpressForm.idInstitucion.value +'&'+ document.VolantesExpressForm.idTurno.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
			      '<input type="hidden" id="comisaria_' + numFila + '" value="">';
		}
		// Juzgado
		else {
			aux = '<input type="text" id="codJuzgado_' + numFila + '" class="box" size="8" style="width:21;margin-top:3px;margin-left:3px;" maxlength="10" onBlur="obtenerJuzgado(' + numFila + ');"/> ' +
				  '<iframe ID="juzgado_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=juzgado_' + numFila + '&tipo=comboJuzgadosTurno&clase=boxCombo&estilo=width:230px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros='+document.VolantesExpressForm.idTurno.value+'&id='+ document.VolantesExpressForm.idInstitucion.value +'&'+ document.VolantesExpressForm.idTurno.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
			      '<input type="hidden" id="juzgado_' + numFila + '" value="">';
		}
		
		td.innerHTML = aux;
		

		// asistido (dni - nombre apellido1 apellido2)
		td = tr.insertCell(); 
		td.setAttribute("width", "36%");
		td.className = "";
		td.innerText="";
		td.innerHTML ='<input type="text" id="dni_' + numFila + '" class="box" style="width:70;margin-top:3px;" value="" maxlength="20" onBlur="obtenerPersona(' + numFila + ');" /> - ' +
		                 '<input type="text" id="nombre_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>&nbsp;' + 
		                 '<input type="text" id="apellido1_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>&nbsp;' +
		                 '<input type="text" id="apellido2_' + numFila + '" class="box" style="width:80;margin-top:3px;" value="" maxlength="80"/>' +
		                 '<input type="hidden" id="idPersona_' + numFila + '" class="box" value=""/>' +
		                 '<img id="info_existe_' + numFila + '" src="/SIGA/html/imagenes/nuevo.gif" alt="<siga:Idioma key="gratuita.volantesExpres.mensaje.esNuevaPersonaJG"/>"/>';
	
		// numero diligencia (num diligencia / anio)
		td = tr.insertCell(); 
		td.setAttribute("width", "9%");
		td.className = "";
		td.innerText="";
		td.innerHTML ='<input type="text" id="diligencia_' + numFila + '" class="box" maxlength="20" style="width:70;margin-top:3px;" value=""/> ';

		// delitos
		
		td = tr.insertCell(); 
		td.setAttribute("width", "14%");
		td.className = "";
		td.innerText="";
		//if(isDelitosVE.booleanValue()%>){
		if(document.VolantesExpressForm.delitos && document.VolantesExpressForm.delitos.value=='true'){
			aux = '';
			// Delitos
			aux =  '<iframe ID="idDelito_' + numFila + 'Frame" SRC="/SIGA/html/jsp/general/comboAnidado.jsp?nombre=idDelito_' + numFila + '&tipo=comboDelitos&clase=boxCombo&estilo=width:215px;&ancho=null&obligatorio=false&elementoSel=[]&seleccionMultiple=false&parametros='+ document.VolantesExpressForm.idInstitucion.value +'&id='+ document.VolantesExpressForm.idInstitucion.value +'&accion=&filasMostrar=1&obligatorioSinTextoSeleccionar=false&readonly=false&pestana=" WIDTH="230" HEIGHT="21" FRAMEBORDER="0" MARGINWIDTH="2" MARGINHEIGHT="1" SCROLLING="NO"></iframe> ' + 
				      '<input type="hidden" id="idDelito_' + numFila + '" value="">'+
				      '<input type="hidden" id="observaciones_' + numFila + '" value="">';
			td.innerHTML = aux;
		}else{
			td.innerHTML ='<input type="text" id="observaciones_' + numFila + '" class="box" style="width:130;margin-top:3px;" value=""/>'+
			'<input type="hidden" id="idDelito_' + numFila + '" value="">';
		}
		
		// boton borrar
		td = tr.insertCell(); 
		td.setAttribute("width", "8%");
		td.className = "";
		td.innerText="";

		
		concatenado = '<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" onclick="borrarFila(\''+ td.id +'\')">';
		td.innerHTML = concatenado;
		tr.scrollIntoView(false);
			
	} //crearFila ()

		function validarDatosFila (fila)  
		{
			var campo = "";
			var obligatorio = "<siga:Idioma key='messages.campoObligatorio.error'/>";
			
			
			if (document.VolantesExpressForm.lugar[0].checked && document.VolantesExpressForm.lugar[0].value == "centro") {
			    
			    if (!document.getElementById("comisaria_"+fila).value) {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.centroDetencion'/>" ;
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			    
			}else{
			
				if (!document.getElementById("juzgado_"+fila).value) {
					campo = "<siga:Idioma key='gratuita.volantesExpres.literal.juzgado'/>";
					alert ("'"+ campo + "' " + obligatorio);
					return false;
				}
			
			}
			
			
			
			if (!document.getElementById("nombre_" + fila).value) {
				campo = "<siga:Idioma key='gratuita.volantesExpres.literal.asistido'/>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}

			if (!document.getElementById("apellido1_" + fila).value) {
				campo = "<siga:Idioma key='gratuita.volantesExpres.literal.apellido1'/>";
				alert ("'"+ campo + "' " + obligatorio);
				return false;
			}

			return true;
		}
		
		
		
	</script>

</head>

<body>

<bean:define id="fechaJustificacion" name="VolantesExpressForm" property="fechaJustificacion" type="String" />
<!-- INICIO: CAMPOS DE BUSQUEDA-->
<html:form action="/JGR_SaltosYCompensaciones" method="POST"
	target="mainWorkArea">

	<html:hidden property="modo" />
	<html:hidden property="idColegiado" />
	<html:hidden property="idInstitucion" />
	<html:hidden property="datosAsistencias" />
	<html:hidden property="idTipoAsistencia" />
	<html:hidden property="delitos" />
	<html:hidden property="msgError" value=""/>
	<html:hidden property="msgAviso" value=""/>

	<siga:ConjCampos
		leyenda="gratuita.volantesExpres.literal.cabeceraVolante">

		<table width="100%" border="0">
			<tr>
				<td width="20%"></td>
				<td width="30%"></td>
				<td width="15%"></td>
				<td width="35%"></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.literal.GuardiaDia" />&nbsp;(*)</td>
				<td><html:text styleId="fechaGuardia" property="fechaGuardia"
					size="10" readonly="true" styleClass="box" />&nbsp; <a
					href='javascript:accionCalendario();//' id="iconoCalendarioA"
					onMouseOut="MM_swapImgRestore();"
					onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif',1);"/>"><img
					id="iconoCalendario"
					src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
					alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
					border="0" align="bottom"></a></td>

				<td class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.literal.Lugar" />&nbsp;(*)</td>
				<td>
				<table>
					<tr>
						<td class="boxConsulta">
							<html:radio property="lugar" value="centro" onclick="actualizarResultados();"></html:radio>
							<siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion"/>
						</td>
					
						<td class="boxConsulta">
							<html:radio property="lugar" value="juzgado" onclick="actualizarResultados();"></html:radio>
							<siga:Idioma key="gratuita.volantesExpres.literal.juzgado"/>
						</td>
					
						
				</table>
				</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.literal.turno" />&nbsp;(*)</td>
				<td><html:select styleId="turnos" style="width:320px;"
					property="idTurno">
					<bean:define id="turnos" name="VolantesExpressForm"
						property="turnos" type="java.util.Collection" />
					<html:optionsCollection name="turnos" value="idTurno"
						label="nombre" />
				</html:select></td>
				<td class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.literal.guardia" />&nbsp;(*)</td>
				<td><html:select styleId="guardias" style="width:320px;"
					property="idGuardia" >
					<bean:define id="guardias" name="VolantesExpressForm"
						property="guardias" type="java.util.Collection" />
					<html:optionsCollection name="guardias" value="idGuardia"
						label="nombre" />
				</html:select></td>
			</tr>


			<tr>
				<td colspan="2">
					<siga:ConjCampos
						leyenda="gratuita.volantesExpres.literal.letradosGuardia">
						<table>
							<tr>
								<td class="labelText"><siga:Idioma
									key="gratuita.volantesExpres.literal.colegiado" />&nbsp;</td>
								<td class="labelText"><html:select
									styleId="colegiadosGuardia" style="width:420px;"
									property="idColegiadoGuardia"
									>
									<bean:define id="colegiadosGuardia" name="VolantesExpressForm"
										property="colegiadosGuardia" type="java.util.Collection" />
									<html:optionsCollection name="colegiadosGuardia"
										value="idPersona" label="nombre" />
								</html:select></td>
							</tr>
	
						</table>
					</siga:ConjCampos>
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>

			<tr>

				<td colspan="4">
					<table width="100%">
						<tr>
							<td width="650px">
								<siga:ConjCampos leyenda="gratuita.volantesExpres.literal.letrado">
			
									<table align="left">
										<tr>
											<td class="labelText"><siga:Idioma
												key="gratuita.volantesExpres.literal.colegiado" /> (*)</td>
											<td><html:text styleId="numeroColegiado"
												property="numeroColegiado" size="4" maxlength="9"
												styleClass="box"></html:text></td>
											<td><html:text styleId="nombreColegiado"
												property="nombreColegiado" size="50" maxlength="50"
												styleClass="box" readonly="true"></html:text></td>
											<td><!-- Boton buscar --> <input type="button"
												class="button" id="idButton" name="Buscar"
												value="<siga:Idioma
												key="general.boton.search" />" onClick="buscarColegiado();">
											 <!-- Boton limpiar -->
											&nbsp;<input type="button" class="button" id="idButton"
												name="Limpiar" value="<siga:Idioma
												key="general.boton.clear" />" onClick="limpiarColegiado();">
											</td>
										</tr>
								
									</table>
			
								</siga:ConjCampos>
							</td>
							<td class="labelText" width="100px" style="text-align: right">
								
								<html:checkbox	property="sustituto"></html:checkbox>
								<siga:Idioma	key="gratuita.volantesExpres.literal.sustitudoDe" />
							</td>
							<td >
								<html:select styleId="colegiadosSustituidos" style="width:200px;"
									property="idColegiadoSustituido">
									<bean:define id="colegiadosGuardia" name="VolantesExpressForm"
										property="colegiadosGuardia" type="java.util.Collection" />
									<html:optionsCollection name="colegiadosGuardia" value="idPersona"
										label="nombre"  />
								</html:select>
							</td>
							
						</tr>
					</table>
				</td>
			</tr>

			<tr>

				<td class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.literal.tipoAsistenciaColegio" />&nbsp;(*)
				</td>
				<td colspan="3"><html:select style="width:720px;"
					property="idTipoAsistenciaColegio" onchange="actualizarResultados();">
					<bean:define id="tiposAsistenciaColegio" name="VolantesExpressForm"
						property="tiposAsistenciaColegio" type="java.util.Collection" />
					<html:optionsCollection name="tiposAsistenciaColegio"
						value="idTipoAsistenciaColegio" label="descripcion" />
				</html:select> </td>
			</tr>

			<tr>
				<td colspan="3" width="1000px">
				<p class="labelText"><siga:Idioma
					key="gratuita.volantesExpres.mensaje.indicaFormaIdentificacion" />
				</p>
				</td>
				<td class="labelText" style="text-align: right"><siga:Idioma
					key="gratuita.volantesExpres.literal.fechaJustificacion" /> <siga:Fecha
					nombreCampo="fechaJustificacion"
					valorInicial="${VolantesExpressForm.fechaJustificacion}" /> &nbsp;<a
					onClick="return showCalendarGeneral(fechaJustificacion);"
					onMouseOut="MM_swapImgRestore();"
					onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img
					src="<html:rewrite page='/html/imagenes/calendar.gif'/>"
					alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
					border="0"></a></td>
			</tr>
		</table>
	</siga:ConjCampos>

<ajax:updateSelectFromField
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxTurnos"
	source="fechaGuardia" target="turnos"
	parameters="fechaGuardia={fechaGuardia}" />
<ajax:select
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxGuardias"
	source="turnos" target="guardias" parameters="fechaGuardia={fechaGuardia},idTurno={idTurno}"
	postFunction="postAccionTurno" />
<ajax:select
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxColegiados"
	source="guardias" target="colegiadosGuardia,colegiadosSustituidos"
	parameters="fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia}" 
	postFunction="postAccionGuardia"/>
<ajax:updateFieldFromField 
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxColegiado"
    source="numeroColegiado" target="idColegiado,numeroColegiado,nombreColegiado"
	parameters="numeroColegiado={numeroColegiado}" postFunction="postAccionColegiado"  />
<ajax:updateFieldFromSelect  
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxColegiadoGuardia"
    source="colegiadosGuardia" target="idColegiado,numeroColegiado,nombreColegiado"
	parameters="idColegiadoGuardia={idColegiadoGuardia}" postFunction="postAccionColegiadoGuardia"/>


			

<div>		
	
	<table id='tabAsistenciasCabeceras' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='6%'><siga:Idioma key="gratuita.volantesExpres.literal.hora"/></td>
			<td align='center' width='27%'><siga:Idioma key="gratuita.volantesExpres.literal.centroDetencion"/>/<siga:Idioma key="gratuita.volantesExpres.literal.juzgado"/></td>
			<td align='center' width='36%'><siga:Idioma key="gratuita.volantesExpres.literal.asistido"/></td>
			<td align='center' width='9%'><siga:Idioma key="gratuita.volantesExpres.literal.numeroDiligencia"/></td>
			<td align='center' width='14%'>
				<c:if test="${VolantesExpressForm.delitos==true}">
					<siga:Idioma key="gratuita.volantesExpres.literal.delitos"/>
				</c:if>
				<c:if test="${VolantesExpressForm.delitos==false}">
					<siga:Idioma key="gratuita.volantesExpres.literal.observaciones"/>
				</c:if>
			</td>
			<td align='center' width='8%'>
				<input type='button'  id = 'idBuscarAsistencias' name='idButton' style="display:none" value='Buscar' alt='Buscar' class='busquedaAsistencias'>
				<input type='button'  id = 'idInsertarAsistencia' class="button" name='idButton' value='<siga:Idioma key="general.boton.insertar"/>' alt='<siga:Idioma key="general.boton.insertar"/>' onclick="accionInsertarRegistroTabla();">
	
			</td>
		</tr>
	</table>
</div>
<div id="divAsistencias">

</div>
<div>
<table id='asistencias' border='1' width='100%' cellspacing='0' cellpadding='0'>
	
</table>
</div>	



			<!-- INICIO: BOTONES BUSQUEDA -->
<table class="botonesDetalle" align="center">
	<tr>
		<td style="width: 900px;">&nbsp;</td>
		<td class="tdBotones"><input type="button" alt='<siga:Idioma key="general.boton.new"/>'
			name='idButton' id="idButton" onclick="return accionNuevo();" class="button"
			value='<siga:Idioma key="general.boton.new"/>'></td>
		<td class="tdBotones"><input type="button" alt='<siga:Idioma key="general.boton.guardar"/>'
			name='idButton' id="idGuardarAsistencias"  class="button" 
			value='<siga:Idioma key="general.boton.guardar"/>'></td>
	</tr>
</table>	

</html:form>

<ajax:htmlContent
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxBusquedaAsistencias"
	source="idBuscarAsistencias"
	target="divAsistencias"
	preFunction="preAccionBuscarAsistencias"
	postFunction="postAccionBuscarAsistencias"
	parameters="fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia},idColegiado={idColegiado},idColegiadoGuardia={idColegiadoGuardia},idTipoAsistenciaColegio={idTipoAsistenciaColegio},lugar={lugar},idColegiadoSustituido={idColegiadoSustituido}"/>

<ajax:htmlContent
	baseUrl="/SIGA/JGR_SaltosYCompensaciones.do?modo=getAjaxGuardarAsistencias"
	source="idGuardarAsistencias"
	target="divAsistencias"
	preFunction="preAccionGuardarAsistencias"
	postFunction="postAccionGuardarAsistencias"
	parameters="datosAsistencias={datosAsistencias},fechaGuardia={fechaGuardia},idTurno={idTurno},idGuardia={idGuardia},idColegiado={idColegiado},idColegiadoGuardia={idColegiadoGuardia},idTipoAsistenciaColegio={idTipoAsistenciaColegio},lugar={lugar},idColegiadoSustituido={idColegiadoSustituido},fechaJustificacion={fechaJustificacion}"/>



			<!-- FIN: CAMPOS DE BUSQUEDA-->
<!-- Formularios auxiliares -->
<html:form action="/CEN_BusquedaClientesModal" method="POST"
	target="mainWorkArea" type="" style="display:none">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
</html:form>

			<html:form action="/JGR_AsistidoAsistencia" method="POST"
				target="submitArea" type="" style="display:none">
				<input type="hidden" name="modo" value="buscarNIF">
				<input type="hidden" name="conceptoE" value="PersonaJG">
				<input type="hidden" name="NIdentificacion" value="">
				<input type="hidden" name="nombreObjetoDestino" value="">
			</html:form>

			<html:form action="/JGR_MantenimientoJuzgados" method="POST"
				target="submitArea">
				<input type="hidden" name="modo" value="buscarJuzgado">
				<input type="hidden" name="codigoExt" value="">
				<input type="hidden" name="nombreObjetoDestino" value="">
			</html:form>

			<html:form action="/JGR_MantenimientoComisarias" method="POST"
				target="submitArea">
				<input type="hidden" name="modo" value="buscarComisaria">
				<input type="hidden" name="codigoExtBusqueda" value="">
				<input type="hidden" name="nombreObjetoDestino" value="">
			</html:form>

			<html:form action="/JGR_ValidarVolantesGuardias" method="POST"
				target="resultado">
				<input type="hidden" name="modo" value="">
				<input type="hidden" name="datosValidar" value="">
				<input type="hidden" name="datosBorrar" value="">
				<input type="hidden" name="desde" value="">
			</html:form>
			<html:form action="/JGR_EJG" method="post" target="mainWorkArea">
				<input type="hidden" name="actionModal" value="" />
				<input type="hidden" name="numEJG" />
				<input type="hidden" name="idTipoEJG" />
				<input type="hidden" name="anio" />
				<input type="hidden" name="numero" />
				<input type="hidden" name="fechaApertura" />
				<input type="hidden" name="idInstitucion"
					value="${VolantesExpressForm.idInstitucion}" />
				<input type="hidden" name="origen" value="A" />
				<input type="hidden" name="modo" />
				<input type="hidden" name="asistenciaAnio" />
				<input type="hidden" name="asistenciaNumero" />
			</html:form>

			<html:form action="/JGR_ActuacionesAsistencia" method="post"
				target="submitArea" style="display:none">
				<input type="hidden" name="modo" value="nuevo" />
				<html:hidden name="AsistenciasForm" property="anio" />
				<html:hidden name="AsistenciasForm" property="numero" />
				<html:hidden name="AsistenciasForm" property="esFichaColegial" />
				<html:hidden name="AsistenciasForm" property="modoPestanha" />
				<!-- RGG: cambio a formularios ligeros -->
				<input type="hidden" name="tablaDatosDinamicosD">
				<input type="hidden" name="actionModal" value="">
			</html:form>



<!-- FIN: BOTONES BUSQUEDA -->


			<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>
</body>

<script>

	function accionNuevo(fila){
		// document.VolantesExpressForm.modo = 'inicio';
		document.VolantesExpressForm.submit();
		
	
	}
	function obtenerComisaria(fila) 
		{
			if (document.getElementById("codComisaria_"+fila).value!=""){
				seleccionComboSiga ("comisaria_"+fila, "");
				document.MantenimientoComisariaForm.codigoExtBusqueda.value = document.getElementById("codComisaria_"+fila).value;
				document.MantenimientoComisariaForm.nombreObjetoDestino.value = "comisaria_" + fila;
				document.MantenimientoComisariaForm.submit();	
			}
		}
	function obtenerJuzgado(fila) 
		{ 
			if (document.getElementById("codJuzgado_"+fila).value!=""){
				seleccionComboSiga ("juzgado_"+fila, "");
				document.MantenimientoJuzgadoForm.codigoExt.value = document.getElementById("codJuzgado_"+fila).value;
				document.MantenimientoJuzgadoForm.nombreObjetoDestino.value = "juzgado_" + fila;
				document.MantenimientoJuzgadoForm.submit();	
			}
		}
		
		function obtenerPersona (fila) 
		{
			o = document.getElementById ("dni_" + fila);
			o.value = o.value.toUpperCase ();

			if (o.value != "") {
				document.PersonaJGForm.NIdentificacion.value = o.value;
				document.PersonaJGForm.nombreObjetoDestino.value = fila;
				document.PersonaJGForm.submit ();
			}				
		} //obtenerPersona ()
		
		function traspasoDatosJuzgado (resultado)
		{
			if (resultado.length > 1) {
				seleccionComboSiga (resultado[1], resultado[0]);
				document.getElementById(resultado[1]).value = resultado[0];
			}
		} //traspasoDatosJuzgado ()
		function traspasoDatosComisaria (resultado)
		{
			traspasoDatosJuzgado (resultado);
		} //traspasoDatosComisaria ()
		
		// Para la busqueda por dni
		function traspasoDatos (resultado, bNuevo, fila) 
		{
			// Porque todas las funciones se llaman igual, las separamos
			if (!(bNuevo && fila)) 
				return traspasoDatosJuzgado (resultado);
			
			if (resultado && resultado.length > 7 && (bNuevo == 1)) { // Existe
				document.getElementById("dni_" + fila).value = resultado[2];
				document.getElementById("nombre_" + fila).value = resultado[3];
				document.getElementById("apellido1_" + fila).value = resultado[4];
				document.getElementById("apellido2_" + fila).value = resultado[5];
				
				document.getElementById("idPersona_" + fila).value = resultado[1];
				ponerIconoIdentPersona (fila, true);
			}
			else {	// Nuevo
				document.getElementById("idPersona_" + fila).value = "nuevo";
				ponerIconoIdentPersona (fila, false);
			}
		} //traspasoDatos ()
		
		function ponerIconoIdentPersona (fila, encontrado)
		{
			if (encontrado) {
				document.getElementById("info_existe_" + fila).src = "/SIGA/html/imagenes/encontrado.gif";
				document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esYaExistentePersonaJG'/>";
				document.getElementById("nombre_" + fila).disabled = true;
				document.getElementById("apellido1_" + fila).disabled = true;
				document.getElementById("apellido2_" + fila).disabled = true;
			}
			else {
				document.getElementById("info_existe_" + fila).src = "/SIGA/html/imagenes/nuevo.gif";
				document.getElementById("info_existe_" + fila).alt = "<siga:Idioma key='gratuita.volantesExpres.mensaje.esNuevaPersonaJG'/>";
				document.getElementById("nombre_" + fila).disabled = false;
				document.getElementById("apellido1_" + fila).disabled = false;
				document.getElementById("apellido2_" + fila).disabled = false;
			}
		} //ponerIconoIdentPersona ()
		function accionCrearEJG(anioAsistencia,numeroAsistencia,idInstitucion,fila) 
		{	
			document.DefinirEJGForm.modo.value = "nuevo";
			document.DefinirEJGForm.asistenciaAnio.value = anioAsistencia;
			document.DefinirEJGForm.asistenciaNumero.value = numeroAsistencia;
			document.DefinirEJGForm.origen.value = "A";
			document.DefinirEJGForm.idInstitucion.value = idInstitucion;
			var resultado=ventaModalGeneral(document.DefinirEJGForm.name,"M");
			if (resultado){
				if(resultado=="MODIFICADO"){
					actualizarResultados();
				}
		   }	
		}
		function accionNuevaActuacion(anioAsistencia,numeroAsistencia,idInstitucion) 
		{	
			document.AsistenciasForm.modo.value = "nuevo";
			document.AsistenciasForm.anio.value = anioAsistencia;
			document.AsistenciasForm.numero.value = numeroAsistencia;
			var resultado=ventaModalGeneral(document.AsistenciasForm.name,"G");

			if (resultado){
				if(resultado=="MODIFICADO"){
					
				}
		   }	
		}
		
	function  borrarFila (fila) 
	{ 
		if (!confirm('<siga:Idioma key="gratuita.volantesExpres.mensaje.eliminarAsistencia"/>'))
			return;
		t = document.getElementById("asistencias"); 
		error = '';
		numeroEjg = document.getElementById("ejgNumero_" + fila).value;
		if(numeroEjg &&numeroEjg!=''){
			error = '<siga:Idioma key="gratuita.volantesExpres.mensaje.borrar.ejgAsociado"/>';
		}
		numeroDesigna = document.getElementById("designaNumero_" + fila).value;
		if(numeroDesigna && numeroDesigna!=''){
			error += '\n'+'<siga:Idioma key="gratuita.volantesExpres.mensaje.borrar.designaAsociada"/>';
		}
		if(error!=''){
			alert(error);
			return;
		}
		t.deleteRow (fila); 
			
	} 
		
		
		function validaHora (o) 
		{
			
			var horas = trim(o.value);
			var mensajehoras = "<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>";
			
            
			
			if (horas.length==1) {
				o.value = "0" + horas;
			}
			if (horas!="" && (horas>23 || horas<0 || !isNumero(horas))) {
				alert(mensajehoras);
				o.focus();
				return false;
			}
		}

		function validaMinuto (o) 
		{
			var minutos = trim(o.value);
			var mensajeminutos="<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>";
		
			/*if (minutos.length==0) {
				o.value = "00";
			}*/
			if (minutos.length==1) {
				o.value = "0" + minutos;
			}

			if (minutos!="" && (minutos>59 || minutos<0 || !isNumero(minutos) )) {
				alert(mensajeminutos);
				o.focus();
				return false;
			}
		}

</script>

</html>