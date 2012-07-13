<!-- busquedaComisionesJunta.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "c.tld" prefix="c"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsZonaBean"%>
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="java.util.Properties" %>

<!-- JSP -->

<% 	
	
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	String app=request.getContextPath();

	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String parametro[] = new String[2];
	parametro[0] = (String)usr.getLocation();
	parametro[1] = (String)usr.getLanguage().toUpperCase();
	ArrayList modoSel = new ArrayList();
	modoSel.add("-1");
		
	

%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="censo.comisiones.literal.comisiones" 
		localizacion="censo.comisiones.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<!-- Calendario -->
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>

		<script language="JavaScript">
		jQuery.noConflict();
		var indice = 0;
		//jQuery.noConflict();
		function preAccionBuscarCargos(){

			jQuery("#idButtonL").removeAttr("disabled");
			jQuery("#idButtonB").removeAttr("disabled");
			jQuery("#numeroColegiado").removeAttr("disabled");
			jQuery("#nombreColegiado").removeAttr("disabled");
			jQuery("#cargos").removeAttr("disabled");
			jQuery("#idInstitucionCargo").removeAttr("disabled");
			jQuery("#fechaCargo").removeAttr("disabled");
			jQuery("#invokefechaCargo").show();
			
				
			if(document.getElementById("idInstitucionCargo").value==null || document.getElementById("idInstitucionCargo").value==""){
				alert("<siga:Idioma key='censo.comisiones.colObligatorio'/>");
				return false;
			} 	
			if(document.getElementById("fechaCargo").value==null || document.getElementById("fechaCargo").value==""){
				alert("<siga:Idioma key='censo.comisiones.cargoObligatorio'/>");
				return false;	
			}

		}
		function postAccionBuscarCargos(){
			table = document.getElementById("cargostabla");
			indice= table.rows.length;
			jQuery("#idInsertarCargo").removeAttr("disabled");
			validarAnchoTabla ();
			fin();
			
		}

		function showCalendarGeneral(inputElement){

			var resultado = showModalDialog("<%=app%>/html/jsp/general/calendarGeneral.jsp?valor="+inputElement.value,inputElement,"dialogHeight:275px;dialogWidth:400px;help:no;scroll:no;status:no;");	
			if (resultado) {
				inputElement.value = resultado;
			} 

			document.getElementById('idBuscarCargos').onclick();
			window.top.focus();
			return false;
		}
		
		function limpiarColegiado()
		{
			document.getElementById("idPersona").value = '';
				document.BusquedaComisionesForm.numeroColegiado.value = '';
				document.BusquedaComisionesForm.nombreColegiado.value = '';
				document.getElementById('idBuscarCargos').onclick();
		}
		
		function buscarColegiado()
		{
		
			document.busquedaClientesModalForm.idInstitucion.value=document.getElementById("idInstitucionCargo").value;
			document.busquedaClientesModalForm.idInstitucionCargo.value=document.getElementById("idInstitucionCargo").value;
			
				var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
				if (resultado!=undefined && resultado[0]!=undefined ){
					
					document.getElementById("idPersona").value     = resultado[0];
					document.BusquedaComisionesForm.numeroColegiado.value    = resultado[2];
					document.BusquedaComisionesForm.nombreColegiado.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
					postAccionColegiado();
				}
		}
		function preAccionColegiado()
		{
		
			var insti=document.getElementById("idInstitucionCargo").value;
			 	if(insti== null || insti== "" ){
			 		alert("<siga:Idioma key='censo.comisiones.colObligatorio'/>");
				 	return false;
			 	} 	
			
		}
			
		function postAccionColegiado(){
			

		}

	function accionInsertarRegistroTabla () 
	{
		var validado = validarDatosMinimos (); 
		if(!validado){
			return;
		}
		
	   	jQuery("#idButtonL").attr("disabled","disabled");
	   	jQuery("#idButtonB").attr("disabled","disabled");
	   	jQuery("#numeroColegiado").attr("disabled","disabled");
	   	jQuery("#nombreColegiado").attr("disabled","disabled");
	   	jQuery("#cargos").attr("disabled","disabled");
	   	jQuery("#idInstitucionCargo").attr("disabled","disabled");
	   	jQuery("#fechaCargo").attr("disabled","disabled");
	   	//jQuery('#fechaCargo').datepicker("disable");
	   	jQuery("#invokefechaCargo").hide();
		crearFila();
		jQuery("#idInsertarCargo").attr("disabled","disabled");
	}
	 		
	function validarDatosMinimos () {
		
		if(document.getElementById ("vacio")!=null)
			document.getElementById ("vacio").style.display="none";
		
		return true;
	}
	function crearFila() 
	{  
		table = document.getElementById("cargostabla");
		numFila = indice;
		indice++;
		//if(table.rows.length>0){
	   	tr = table.insertRow();
	   	var fila ="filaTablaPar";
	   	 if((numFila+2)%2==0)
	   	 	 fila = "filaTablaPar";
	   	 else
	   		 fila = "filaTablaImpar";

	   	
		tr.className=fila;	
		tr.id = "fila_" + numFila;
		td = tr.insertCell(0);
		td.innerHTML ='';
		td.setAttribute("width", "10%");
		td = tr.insertCell(1); 
		td.setAttribute("width", "15%");
		td.setAttribute("align", "center");
		td.innerHTML='<siga:ComboBD ancho="140" nombre="cargos_' + numFila + '" id="cargos_' + numFila + '" tipo="cmbCargosJunta" estilo="margin-top:4px;" parametro='<%=parametro%>' clase="boxCombo" accion="comporbarFila(\''+ tr.id +'\');" />';
		td = tr.insertCell(2); 
		td.setAttribute("width", "10%");
		td.setAttribute("align", "center");
		td.innerHTML ='<input type="text" onmousedown="bloquearBuscar(\''+ numFila +'\');" onBlur="comporbarFila(\''+ tr.id +'\');buscarColegiadoN(\''+ numFila +'\');"  id="numeroColegiado_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:5px;" value=""/><input type="hidden" id="idPerson_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:2px;" value=""/>';
		td = tr.insertCell(3); 
		td.setAttribute("width", "35%");
		//td.innerHTML ='<input type="text" id="numeroColegiado_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:2px;" value=""/>';
		td.innerHTML ='<table><tr>' +
        '<td><input type="text" onBlur="comporbarFila(\''+ tr.id +'\');" id="nombreColegiado_' + numFila + '" class="box" style="width:120;margin-top:2px;margin-rigth:1px;" value="" maxlength="35"/>' + " "+'<input type="text" onBlur="comporbarFila(\''+ tr.id +'\');buscarNumColegiadoN(\''+ numFila +'\');" id="apellidosColegiado_' + numFila + '" class="box" style="width:200;margin-top:2px;margin-rigth:1px;" value="" maxlength="40"/></td>' + 
        
        '</tr></table>';
		td = tr.insertCell(4); 
		td.setAttribute("colspan", "2");
		td.setAttribute("align", "center");
		td.className = "";
		td.setAttribute("width", "20%");
		td.innerHTML ='<input type="button" class="button"  name="Buscar_' + numFila + '" id="idButtonB__' + numFila + '" value="<siga:Idioma key="general.boton.search" />"	onClick="comporbarFila(\''+ tr.id +'\');buscarColegiadoNBoton(' + numFila + ');">     <input type="button" class="button" id="idButton_' + numFila + '" name="Limpiar_' + numFila + '" 	value="<siga:Idioma	key="general.boton.clear" />" onClick="limpiarColegiadoN(' + numFila + ');">';
		td = tr.insertCell(5); 
		td.setAttribute("width", "10%");
		td.setAttribute("align", "left");
		td.innerHTML= '<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" onclick="borrarFila( ' + numFila + ',\''+ tr.id +'\')">';

		concatenado = '<table><tr><td>	<img id="iconoboton_consultar' + numFila + '" src="/SIGA/html/imagenes/bconsultar_off.gif" style="cursor:pointer;" title="Consultar" alt="Consultar" name="consultar_' + numFila + '" border="0" onClick=" selectRow(' + numFila + '); consultar(' + numFila + '); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage("consultar_' + numFila + '","","/SIGA/html/imagenes/bconsultar_on.gif",1)">'+
			'<img id="iconoboton_editar' + numFila + '" src="/SIGA/html/imagenes/beditar_off.gif" style="cursor:pointer;" title="Editar" alt="Editar" name="editar_' + numFila + '" border="0" onClick=" selectRow(' + numFila + '); editar(' + numFila + '); " onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage("editar_' + numFila + '","","/SIGA/html/imagenes/beditar_on.gif",1)">'+
			'</td></tr></table>';

			disablebuttons();
			var cargo='cargos_' + numFila + '';
			validarAnchoTabla ();
			document.getElementById (cargo).focus();
	}
	function disablebuttons(){

		
		t = document.getElementById("cargostabla");
		for (i = 0; i < t.rows.length-1; i++) {

			
			var y = t.rows[i].id;
			var x = y.substring(5);
			if(document.getElementById("editaCargo_"+x)!=null){
				document.getElementById("editaCargo_"+x).src="/SIGA/html/imagenes/beditar_disable.gif";
				document.getElementById("borradoLogico_"+x).src="/SIGA/html/imagenes/bborrar_disable.gif";
				
			   	jQuery("#editaCargo_"+x).attr("disabled","disabled");
			   	jQuery("#borradoLogico_"+x).attr("disabled","disabled");
				document.getElementById("borradoLogico_"+x).style.cursor="default";
				document.getElementById("editaCargo_"+x).style.cursor="default";
			}
		}


	}

	function borradoLogicoFila(num, idFila){
         document.datosCVForm.idPerson.value=document.getElementById("idPersona_"+num).value;
         document.datosCVForm.idPersona.value=document.getElementById("idPersona_"+num).value;
         document.datosCVForm.idCV.value=document.getElementById("IDCV_"+num).value;
         document.datosCVForm.idInstitucion.value=document.getElementById("idInstitucion").value;
         document.datosCVForm.idInstitucionCargo.value=document.getElementById("idInstitucionCargo").value;
       
        
         if(borrarFila (num, "fila_"+num))
         	document.getElementById('idBorrar').onclick();

	}

	function buscarColegiadoN(num){
		document.BusquedaComisionesForm.numeroColegiadoN.value=document.getElementById("numeroColegiado_"+num).value;
		document.BusquedaComisionesForm.numeroN.value=num;
		document.getElementById('numeroN').onchange();

		if(document.getElementById("numeroColegiado_"+num).value == null || document.getElementById("numeroColegiado_"+num).value == ""){
			jQuery("#idButtonB_" + num).removeAttr("disabled");
		}
	}
	
	function buscarNumColegiadoN(num){


        document.BusquedaComisionesForm.nombreColegiadoN.value=document.getElementById("nombreColegiado_"+num).value;
        document.BusquedaComisionesForm.apellidosColegiadoN.value=document.getElementById("apellidosColegiado_"+num).value;
        document.BusquedaComisionesForm.numeroN.value=num;
        document.getElementById('numeroN').onchange();
   }
	function buscarColegiadoNBoton(num)
	{

		document.busquedaClientesModalForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucionCargo.value;
		document.busquedaClientesModalForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;
		document.busquedaClientesModalForm.modo.value="abrirBusquedaModal";
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){

				
				
				document.getElementById("idPerson_"+num).value=resultado[0];
				document.getElementById("numeroColegiado_"+num).value= resultado[2];
				document.getElementById("nombreColegiado_"+num).value= resultado[4];
				document.getElementById("apellidosColegiado_"+num).value= resultado[5]+' '+resultado[6];
				
			}else{
				document.getElementById("idPerson_"+num).value = '';
				document.getElementById("numeroColegiado_"+num).value = '';
				document.getElementById("nombreColegiado_"+num).value = '';
				document.getElementById("apellidosColegiado_"+num).value = '';
				
				
			}
			document.BusquedaComisionesForm.numeroColegiadoN.value="";
		 	document.BusquedaComisionesForm.nombreColegiadoN.value="";
	        document.BusquedaComisionesForm.apellidosColegiadoN.value="";
	        document.BusquedaComisionesForm.numeroN.value="";
				
	}

	function bloquearBuscar(numFila){
	   	jQuery("#idButtonB__"+numFila).attr("disabled","disabled");
	}	
	
	function limpiarColegiadoN(num)
	{
			document.getElementById("idPerson_"+num).value = '';
			document.getElementById("numeroColegiado_"+num).value = '';
			document.getElementById("nombreColegiado_"+num).value = '';
			document.getElementById("apellidosColegiado_"+num).value = '';


	}
	function preAccionColegiadoN()
	{
	}
		
	function postAccionColegiadoN(){
		num=document.BusquedaComisionesForm.numeroN.value;
		var idPer=document.BusquedaComisionesForm.idPersonaN.value;
		document.getElementById("idPerson_"+num).value=idPer;
		document.getElementById("nombreColegiado_"+num).value=document.BusquedaComisionesForm.nombreColegiadoN.value;
		document.getElementById("apellidosColegiado_"+num).value=document.BusquedaComisionesForm.apellidosColegiadoN.value;
		document.getElementById("numeroColegiado_"+num).value=document.BusquedaComisionesForm.numeroColegiadoN.value;
		jQuery("#idButtonB__" + num).removeAttr("disabled");
     	multiple=document.BusquedaComisionesForm.multiple.value;
     	if(multiple=="S"){
	  		document.busquedaClientesModalForm.numeroColegiado.value=document.getElementById("numeroColegiado_"+num).value;         	
	  		document.busquedaClientesModalForm.nombrePersona.value=document.getElementById("nombreColegiado_"+num).value;
			document.busquedaClientesModalForm.apellido1.value=document.getElementById("apellidosColegiado_"+num).value;
			document.busquedaClientesModalForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucion.value;
			document.busquedaClientesModalForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;
			document.busquedaClientesModalForm.modo.value="buscarModalResultado";
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");	
			if (resultado!=undefined && resultado[0]!=undefined ){
				
					
				document.getElementById("idPerson_"+num).value=resultado[0];
				document.getElementById("numeroColegiado_"+num).value= resultado[2];
				document.getElementById("nombreColegiado_"+num).value= resultado[4];
				document.getElementById("apellidosColegiado_"+num).value= resultado[5]+' '+resultado[6];
				
			}else{
				document.getElementById("idPerson_"+num).value = '';
				document.getElementById("numeroColegiado_"+num).value = '';
				document.getElementById("nombreColegiado_"+num).value = '';
				document.getElementById("apellidosColegiado_"+num).value = '';
				
									

			}
     	}	

		document.BusquedaComisionesForm.numeroColegiadoN.value="";
	 	document.BusquedaComisionesForm.nombreColegiadoN.value="";
        document.BusquedaComisionesForm.apellidosColegiadoN.value="";
        document.BusquedaComisionesForm.numeroN.value="";
		
	}


	function preAccionBorrarCargo(){		
	}
	
	function postAccionBorrarCargo(){		
	}
	
	function  borrarFila (numFil, idFila) { 
		if (!confirm("<siga:Idioma key='messages.deleteConfirmation'/>")){
			return false;
		}else{
			numFila = table.rows.length;
			t = document.getElementById("cargostabla");
			ulti = t.rows.length;
			for (i = 0; i < t.rows.length; i++) {
				if(ulti==1 || i==(ulti-1)){
					j= table.rows[i].id.split("_")[1]; 
					if(document.getElementById("idPersona_" + j)!=null){
						t.deleteRow (i);
						validarAnchoTabla();
						 return true;
					}else return false;
				}	
				if (t.rows[i].id == idFila) 
				{
					// Guardamos los datos a borrar
					fila = idFila.split("_")[1];
					t.deleteRow (i);
					validarAnchoTabla ();
					return true; 
				}
			}
		}
	} 
	function finalizar(){
		t = document.getElementById("cargostabla");
		ulti = t.rows.length;
		t.deleteRow (ulti); 
				
	}	

	function validarDatosFila (fila)  
	{
		var campo = "";
		var obligatorio = "<siga:Idioma key='censo.comisiones.lineasIncompletas'/>";
		
		var isValidado = true;
		if(document.getElementById("cargos_" + fila)!=null){

			if (!document.getElementById("cargos_" + fila).value) {
				alert (obligatorio);
				return false;
			}
			if (!document.getElementById("nombreColegiado_" + fila).value) {
				alert (obligatorio);
				return false;
			}
			if (!document.getElementById("numeroColegiado_" + fila).value) {
				alert (obligatorio);
				return false;
			}
		}
	

		return isValidado;
	}
	
	function getDatos(idTabla) 
	{	
		table = document.getElementById(idTabla);
		filas = table.rows.length;
		// Datos Dinamicos Asistencias
		var datos = "", accion = "";
		var actualiza="";	
		if(filas!=0){
		for (a = 0; a < filas-1 ; a++) {
			i = table.rows[a].id.split("_")[1];
			var validado = validarDatosFila (i);
						  
			if (!validado) {
				fin();
				return 'cancel';
			}
			if(document.getElementById("idPersona_" + i)!=null){
				if(document.getElementById("editar_" + i)!=null && document.getElementById("editar_" + i).checked){
					
					if(document.getElementById("editar_" + i).disabled==""){
				        ncolegiado = document.getElementById("ncolegiado_" + i).value;
				        if(ncolegiado=="")
				        	datos += "0";
				        else
							datos += ncolegiado;
						datos += ',';
						
						IDCV = document.getElementById("IDCV_" + i).value;
						datos += IDCV;
						datos += ',';
		
						idPersona = document.getElementById("idPersona_" + i).value;
						datos += idPersona;
						datos += ",";
						
						editar = document.getElementById("editar_" + i).checked;
						if(editar)
							datos +="S";
						else
							datos +="N";
						datos += "%%%";
					}
				}
			}else{
				if(actualiza==""){
					actualiza="S";
					datos += "#@#";
				}
				carg = document.getElementById("cargos_" + i).options[document.getElementById("cargos_" + i).selectedIndex].value;
				datos += carg;
				datos += ',';
					
				numero = document.getElementById("numeroColegiado_" + i).value;
				datos += numero;
				datos += ',';
				
				idpersona = document.getElementById("idPerson_" + i).value;
				datos += idpersona;
	
				datos += "%%%";
			}
		}
		i = table.rows[filas-1].id.split("_")[1];
		if(document.getElementById("idPersona_" + i)!=null){
			if(document.getElementById("editar_" + i)!=null && document.getElementById("editar_" + i).checked){

		        ncolegiado = document.getElementById("ncolegiado_" + i).value;
		        if(ncolegiado=="")
		        	datos += "0";
		        else
					datos += ncolegiado;
				datos += ',';
				
				IDCV = document.getElementById("IDCV_" + i).value;
				datos += IDCV;
				datos += ',';

				idPersona = document.getElementById("idPersona_" + i).value;
				datos += idPersona;
				datos += ",";
				
				editar = document.getElementById("editar_" + i).checked;
				if(editar)
					datos +="S";
				else
					datos +="N";
				datos += "%%%";
			}
		

		}
		return datos;
	   } else return null;		
	}

	function preAccionGuardarCargos(){
			
		
		var validado = validarDatosMinimos ();
		if(!validado){
			fin();
			return 'cancel';
		}
		datosCargos = getDatos('cargostabla');

		if(datosCargos=='cancel'  || datosCargos==null){
			fin();
			return 'cancel';
		}

		document.BusquedaComisionesForm.datosCargos.value = datosCargos;
	}

	function postAccionGuardarCargos(){
		document.getElementById('idBuscarCargos').onclick();
		
	}
	
	function comporbarFila(idFila){
		fila = idFila.split("_")[1]
		t = document.getElementById("cargostabla");
		ulti = t.rows.length;
		if (t.rows[ulti-1].id == idFila){
			if((document.getElementById("numeroColegiado_"+fila).value != null    && document.getElementById("numeroColegiado_"+fila).value != "") ||
			   (document.getElementById("nombreColegiado_"+fila).value != null    && document.getElementById("nombreColegiado_"+fila).value != "") ||
			   (document.getElementById("apellidosColegiado_"+fila).value != null && document.getElementById("apellidosColegiado_"+fila).value != "") ){
				crearFila(); 
			}
		}
	}

	function validarAnchoTabla() 
	{
	
		if (document.getElementById("cargostabla").clientHeight < document.getElementById("divCargos").clientHeight) {
			
		
			document.getElementById("tabCargosCabeceras").width='100%';
		}
		else {
			document.getElementById("tabCargosCabeceras").width='98.30%';
		}
	}

			</script>

	</head>


<body onload="validarAnchoTabla();" >

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<fieldset>
	<table class="tablaCentralCampos" align="center">

	<html:form action="/CEN_GestionarComisiones.do" styleId="BusquedaComisionesForm" method="POST" target="mainWorkArea">
	<html:hidden styleId="modo" property = "modo" value = "inicio"/>
	<html:hidden property="idPersona" value=""/>
	<html:hidden styleId="numeroN" property="numeroN" value=""/>
	<html:hidden styleId="multiple" property="multiple" value=""/>
	<html:hidden styleId="idPersonaN" property="idPersonaN" value=""/>
	<html:hidden styleId="numeroColegiadoN" property="numeroColegiadoN" value=""/>
	<html:hidden styleId="nombreColegiadoN" property="nombreColegiadoN" value=""/>
	<html:hidden styleId="apellidosColegiadoN" property="apellidosColegiadoN" value=""/>	
	<html:hidden styleId="datosCargos" property="datosCargos" value=""/>
	

			
	<html:hidden property="idInstitucion" value="${BusquedaComisionesForm.idInstitucion}"/>
	<!-- FILA -->
	<tr>				
	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.colegio"/> (*)
		
	</td>				
	<td class="labelText">
				<siga:ComboBD ancho="" nombre = "idInstitucionCargo" id="idInstitucionCargo" tipo="cmbNombreColegiosConsejosTodos" parametro="<%=parametro %>" obligatorioSinTextoSeleccionar="true" clase="boxCombo"  elementoSel="<%=modoSel %>" accion="limpiarColegiado()"/>
	</td>
	<td  colspan="2">
		<table><tr>
				<td class="labelText"><siga:Idioma	key="gratuita.volantesExpres.literal.colegiado" /></td>
				<td><html:text styleId="numeroColegiado" property="numeroColegiado" size="4" maxlength="9"	styleClass="box" value=""></html:text></td>
				<td><html:text styleId="nombreColegiado"
									property="nombreColegiado" size="40" maxlength="50"
									styleClass="box" readonly="true" id="nombreCol"></html:text></td>
				<td><!-- Boton buscar --> <input type="button"
									class="button"  name="Buscar" id="idButtonB"
									value="<siga:Idioma
											key="general.boton.search" />"
									onClick="buscarColegiado();"> <!-- Boton limpiar -->
								&nbsp;<input type="button" class="button" id="idButtonL"
									name="Limpia"
									value="<siga:Idioma
											key="general.boton.clear" />"
									onClick="limpiarColegiado();"></td>		
		</tr></table>	
	</td>								
	  </tr>						   				
	<tr>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.fechaCargo"/> (*)
	</td>
	<td class="labelText">
				<siga:Fecha  nombreCampo= "fechaCargo"/>
	</td>
				
	<td colspan="2">
		<table><tr>
			<td class="labelText">
			<siga:Idioma key="censo.busquedaComisiones.literal.cargos"/></td>
			<td class="labelText">
			<siga:ComboBD nombre="cargos" id="cargos" tipo="cmbCargosJunta" parametro="<%=parametro%>" clase="boxCombo" /></td>
			<td></td><td></td></tr></table>
	</td>
	
	
	</tr>
	<input type='button'  id = 'idBorrar' name='idButton' style="display:none" value='Borrar' alt='Borrar' >

 <ajax:updateFieldFromField 
	baseUrl="/SIGA/CEN_GestionarComisiones.do?modo=getAjaxColegiado"
    source="numeroColegiado" 
    target="idPersona,numeroColegiado,nombreColegiado"
	parameters="numeroColegiado={numeroColegiado},idInstitucionCargo={idInstitucionCargo}" 
	preFunction="preAccionColegiado" 
	postFunction="postAccionColegiado"  />

			
<ajax:updateFieldFromField  
	baseUrl="/SIGA/CEN_GestionarComisiones.do?modo=getAjaxColegiadoIndividual"
    source="numeroN" 
    target="idPersonaN,numeroColegiadoN,nombreColegiadoN,apellidosColegiadoN,numeroN,multiple"
	parameters="numeroColegiadoN={numeroColegiadoN},nombreColegiadoN={nombreColegiadoN},apellidosColegiadoN={apellidosColegiadoN},idInstitucionCargo={idInstitucionCargo},numeroN={numeroN}" 
	preFunction="preAccionColegiadoN" 
	postFunction="postAccionColegiadoN"  /> 
	
	</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
<table style="width: 100%;">
<tr> 
<td class="titulitos">
<siga:Idioma key="censo.comisiones.literal.consultarComisiones"/>
</td>
<td class="tdBotones">
<input type='button'  id = 'idInsertarCargo' class="button" style="display:block" name='idButton' value='<siga:Idioma key="general.boton.new"/>' alt='<siga:Idioma key="general.boton.new"/>' onclick="accionInsertarRegistroTabla();">
</td>
<td class="tdBotones">
<input type="button" alt="Buscar" id="idBuscarCargos"  name='idButton' class="button" value="Buscar"  class='busquedaAsistencias'>
<input type="button" alt='<siga:Idioma key="general.boton.guardar"/>' style="display:none"
			name='idButton' id="idGuardarCargos"  class="button" 
			value='<siga:Idioma key="general.boton.guardar"/>'>
</td>

</tr>	 
</table>	

		
	<!-- FIN: BOTONES BUSQUEDA -->
	
	
	<div>		
	<table id='tabCargosCabeceras' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='10%'><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaInicio"/></td>
			<td id='cargo' align='center' width='15%'><siga:Idioma key="censo.datosCV.literal.cargo"/></td>
			<td align='center' width='10%'><siga:Idioma key="censo.busquedaClientes.literal.nColegiado"/></td>
			<td align='center' width='35%'><siga:Idioma key="censo.busquedaClientes.literal.nombre"/></td>
			<td align='center' width='20%'></td>
			<td align='center' width='10%'>
			
			</td>
		</tr>
	</table>
	</div>
	<div id="divCargos" style='height:530px;position:absolute;width:100%;  overflow-y:auto' >
	<table id='cargostabla' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		
	</table>
	<div id="vacio"></div>
	</div>	
	<div id="divBorrar"></div>
			<!-- INICIO: BOTONES BUSQUEDA -->
</html:form>
			



<ajax:htmlContent
	baseUrl="/SIGA/CEN_DatosCV.do?modo=getAjaxBorrarCargo"
	source="idBorrar"
	target="divBorrar"
	preFunction="preAccionBorrarCargo"
	postFunction="postAccionBorrarCargo"
	parameters="idPersona={idPersona},idPerson={idPerson},idCV={idCV},idInstitucion={idInstitucion},idInstitucionCargo={idInstitucionCargo}"/>


<ajax:htmlContent
	baseUrl="/SIGA/CEN_GestionarComisiones.do?modo=getAjaxBusquedaCargos"
	source="idBuscarCargos"
	target="divCargos"
	preFunction="preAccionBuscarCargos"
	postFunction="postAccionBuscarCargos"
	parameters="idInstitucionCargo={idInstitucionCargo},fechaCargo={fechaCargo},numeroColegiado={numeroColegiado},idPersona={idPersona},cargos={cargos},mantenimiento={mantenimiento}"/>


<ajax:htmlContent
	baseUrl="/SIGA/CEN_GestionarComisiones.do?modo=getAjaxGuardarCargos"
	source="idGuardarCargos"
	target="divCargos"
	preFunction="preAccionGuardarCargos"
	postFunction="postAccionGuardarCargos"
	parameters="datosCargos={datosCargos}"/>


	<html:form action="/CEN_BusquedaClientesModal" method="POST"
	target="mainWorkArea" type="" style="display:none" scope="request">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
	<html:hidden property="idInstitucion" /> 
	<html:hidden property="idInstitucionCargo" /> 
	<html:hidden property="nombrePersona" />
	<html:hidden property="apellido1" />
	<html:hidden property="numeroColegiado" />	
	</html:form>


	<html:form action="/CEN_DatosCV.do" method="POST">
	<html:hidden property = "modo" value = ""/>
	<input type='hidden' name="mantenimiento" 	value=""/>	
	<input type='hidden' name="accion" 				value=""/>
	<input type="hidden" name="nombreUsuario" value=""/>
	<input type="hidden" name="numeroUsuario" value=""/>
	<html:hidden property = "numcolegiado" value = ""/>
	<html:hidden property = "nombre" value = ""/>		
	<html:hidden property="idPersona" 			value=""/> 
	<html:hidden property="idPerson" 			value=""/> 	
	<html:hidden property="idCV" 						value=""/> 
	<html:hidden property="idInstitucion" 	value=""/> 
	<html:hidden property="idInstitucionCargo" 	value=""/> 
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="">

	</html:form>

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->

		function  consultar (idFila) 
		{ 
			//document.BusquedaComisionesForm.modo.value = "editar";
			document.datosCVForm.mantenimiento.value="S";

			document.datosCVForm.nombreUsuario.value=document.getElementById("namecolegiado_" + idFila).value;
			document.datosCVForm.numeroUsuario.value=document.getElementById("ncolegiado_" + idFila).value;			
			document.datosCVForm.idPersona.value=document.getElementById("idPersona_" + idFila).value; 	
			document.datosCVForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucion.value; 	
			document.datosCVForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;	
			document.datosCVForm.idCV.value=document.getElementById("IDCV_" + idFila).value; 	
			 document.datosCVForm.modo.value = "verModal";
			  var rc = ventaModalGeneral(document.datosCVForm.name, "M");
			  if (rc != null) { 
		 	 	if (rc == "MODIFICADO") {
		 	 		
		  		}
			  }
		}

		function  editarCargo (idFila) 
		{ 
			document.datosCVForm.mantenimiento.value="S";
			document.datosCVForm.nombreUsuario.value=document.getElementById("namecolegiado_" + idFila).value;
			document.datosCVForm.numeroUsuario.value=document.getElementById("ncolegiado_" + idFila).value;
			document.datosCVForm.idPersona.value=document.getElementById("idPersona_" + idFila).value; 	 	
			document.datosCVForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucion.value; 
			document.datosCVForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;	
			document.datosCVForm.idCV.value=document.getElementById("IDCV_" + idFila).value; 	
			document.datosCVForm.modo.value = "editarModal";
			  var rc = ventaModalGeneral(document.datosCVForm.name, "M");
				document.getElementById('idBuscarCargos').onclick();

			  
		}
				
		function accionGuardar(){
	         document.getElementById('idGuardarCargos').onclick();
			//documentResultado =window.frames['resultado'];
			//documentResultado.finalizar();
			
		}	
		
		//-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{	
				
			document.forms[0].reset();
		}
		
		//Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
		
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].submit();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->



		<siga:ConjBotonesAccion botones="G" />
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>