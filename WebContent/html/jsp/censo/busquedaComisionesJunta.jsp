<!DOCTYPE html>
<html>
<head>
<!-- busquedaComisionesJunta.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 	 prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 	 prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>
<%@ taglib uri = "c.tld" 			 prefix="c"%>
<%@ taglib uri = "ajaxtags.tld" 	 prefix="ajax" %>

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
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import = "com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	
	HttpSession ses=request.getSession();
	Integer alturaDatosTabla = 155;
	
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getAttribute("buscar");
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String parametro[] = new String[2];
	parametro[0] = (String)usr.getLocation();
	parametro[1] = (String)usr.getLanguage().toUpperCase();
	
	//Variables de busqueda
	String idInstitucionCargo = "-1";
	String cargos = "-1";
	String nombreColegiado = "";
	String numeroColegiado = "";
	
	ArrayList modoSel = new ArrayList();
	ArrayList selCargos = new ArrayList();
	String volverStr = (String)request.getAttribute("volver");
	boolean volver = false;
	if (volverStr != null && volverStr.equals("volver")){
		volver = true;
		idInstitucionCargo = (String)request.getAttribute("idInstitucionCargo");
		nombreColegiado  = (String)request.getAttribute("nombreColegiado");
		numeroColegiado  = (String)request.getAttribute("numColegiado");
		cargos = (String)request.getAttribute("cargos");
		modoSel.add(idInstitucionCargo);
		selCargos.add(cargos);
	}else{
		modoSel.add("-1");
		selCargos.add("-1");
	}
		
%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<siga:TituloExt titulo="censo.comisiones.literal.comisiones" localizacion="censo.comisiones.localizacion"/>
</head>

<body onload="calcularAltura();" >

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
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
		
		<siga:ConjCampos leyenda="Filtros de búsqueda">
			<table class="tablaCentralCampos" align="center">
	
				<!-- FILA -->
				<tr>				
					<td class="labelText">
						<siga:Idioma key="censo.busquedaClientes.literal.colegio"/> (*)						
					</td>				
					<td class="labelText">
						<siga:Select id="idInstitucionCargo" queryId="getNombreColegiosConsejoTodos" required="true" selectedIds="<%=modoSel %>"/>
						<script type="text/javascript">
						jQuery(function(){
							jQuery("#idInstitucionCargo").on("change",function(){limpiarColegiado();});
						});
						</script>						
					</td>
					<td>
						<table>
							<tr>
								<td class="labelText">
									<siga:Idioma	key="gratuita.volantesExpres.literal.colegiado" />
								</td>
								<td>
									<html:text styleId="numeroColegiado" property="numeroColegiado" size="4" maxlength="9"	styleClass="box" value="<%=numeroColegiado%>" />
								</td>
								<td>
									<html:text styleId="nombreColegiado" property="nombreColegiado" size="40" maxlength="50" styleClass="box" readonly="true" id="nombreCol" value="<%=nombreColegiado%>"/>
								</td>
								<td><!-- Boton buscar --> 
									<input type="button" class="button"  name="Buscar" id="idButtonB" value="<siga:Idioma key='general.boton.search' />" onClick="buscarColegiado();" /> 
									<!-- Boton limpiar -->
									&nbsp;<input type="button" class="button" id="idButtonL" name="Limpia" value="<siga:Idioma key='general.boton.clear' />" onClick="limpiarColegiado();" />
								</td>		
							</tr>
						</table>	
					</td>								
	  			</tr>						   				
	
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.busquedaComisiones.literal.fechaCargo"/> (*)
					</td>
					<td class="labelText">
						<siga:Fecha nombreCampo="fechaCargo" valorInicial='<%=UtilidadesBDAdm.getFechaBD("")%>'/>
					</td>
				
					<td>
						<table>
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.busquedaComisiones.literal.cargos"/>
								</td>
								<td class="labelText">
									<siga:Select id="cargos" queryId="getCenTiposCVsubtipo2IdTipoCvJuntasGobierno" selectedIds="<%=selCargos%>" />
								</td>
								<td></td>
								<td><input type='button'  id = 'idBorrar' name='idButton' style="display:none" value='Borrar' alt='Borrar' ></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<div id="fechasParaInsertar" style="display:none">
		<siga:ConjCampos leyenda="Fechas para insertar" desplegable="true">
			<table width="100%" align="center" >
				<tr>
					<td style="text-align: center; width: 10%;"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaInicio" /></td>
					<td style="text-align: right; width: 60%; vertical-align: middle" colspan="3" rowspan="2" id="mensajeFechaFinCargos">La Fecha de Fin sólo se usa para dar de baja a los cargos existentes. <br>Para añadirla en un nuevo cargo, primero pulse en Guardar.</td>
					<td style="text-align: center; width: 20%;"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaFin" /></td>
					<td style="text-align: center; width: 10%;">&nbsp;</td>
				</tr>
				<tr>
					<td style="text-align: center; width: 10%;"><siga:Fecha nombreCampo="fechaInicioCargos"/></td>
					<td style="text-align: center; width: 20%;"><siga:Fecha nombreCampo="fechaFinCargos"/></td>
					<td style="text-align: center; width: 10%;">&nbsp;</td>
				</tr>
			</table>
		</siga:ConjCampos>
		</div>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	
	<!-- INICIO: BOTONES BUSQUEDA -->
			
		<table class="botonesSeguido" id="idBotonesBusqueda" align="center" >
			<tr> 
				<td class="titulitos">
					<siga:Idioma key="censo.comisiones.literal.consultarComisiones"/>
				</td>
				<td class="tdBotones">
					<input type="button"  id="idInsertarCargo" class="button" style="display:block" name="idButton" value="<siga:Idioma key='general.boton.new'/>" alt="<siga:Idioma key='general.boton.new'/>" onclick="accionInsertarRegistroTabla();" />
				</td>
				<td class="tdBotones">
					<input type="button" alt="Buscar" id="idBuscarCargos"  name="idButton" class="button" value="Buscar"  class="busquedaAsistencias" />
					<input type="button" alt="<siga:Idioma key='general.boton.guardar'/>" style="display:none" name="idButton" id="idGuardarCargos"  class="button" value="<siga:Idioma key='general.boton.guardar'/>" />
				</td>
			</tr>	 
		</table>	
		
	<!-- FIN: BOTONES BUSQUEDA -->
		
		<div>
			<table id="tabCargosCabeceras" name="tabCargosCabeceras" width='100%' cellspacing='0' cellpadding='0'
				class='fixedHeaderTable dataScroll'
				style='table-layout: fixed; border-spacing: 0px;'>
				<thead class='Cabeceras' style='text-align: center;'>
					<tr class='tableTitle'>
						<th style="text-align: center; width: 10%;"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaInicio" /></th>
						<th style="text-align: center; width: 15%" id="cargo"><siga:Idioma key="censo.datosCV.literal.cargo" /></th>
						<th style="text-align: center; width: 10%"><siga:Idioma key="censo.busquedaClientes.literal.nColegiado" /></th>
						<th style="text-align: center; width: 35%"><siga:Idioma key="censo.busquedaClientes.literal.nombre" /></th>
						<th style="text-align: center; width: 20%"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaFin" /></th>
						<th style="text-align: center; width: 10%">&nbsp;</th>
					</tr>
				</thead>
			</table>
		</div>
		
		<div id="divCargos" style="height:595px;position:absolute;width:100%;overflow-y:auto" >
			
			<div id="vacio">
			</div>
		</div>
			
		<div id="divBorrar">
		</div>
		<!-- INICIO: BOTONES BUSQUEDA -->
	</html:form>		

	<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="" style="display:none" scope="request">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<html:hidden property="idInstitucion" /> 
		<html:hidden property="idInstitucionCargo" /> 
		<html:hidden property="nombrePersona" />
		<html:hidden property="apellido1" />
		<html:hidden property="numeroColegiado" />	
	</html:form>

	<html:form action="/CEN_DatosCV.do" method="POST">
		<html:hidden property = "modo" 							value=""/>
		<input type='hidden' name="mantenimiento" 				value=""/>	
		<input type='hidden' name="accion" 						value=""/>
		<input type="hidden" name="nombreUsuario" 				value=""/>
		<input type="hidden" name="numeroUsuario" 				value=""/>
		<html:hidden property = "numcolegiado" 					value=""/>
		<html:hidden property = "nombre" 						value=""/>		
		<html:hidden property="idPersona" 						value=""/> 
		<html:hidden property="idPerson" 						value=""/> 	
		<html:hidden property="idCV" 							value=""/> 
		<html:hidden property="idInstitucion" 					value=""/> 
		<html:hidden property="idInstitucionCargo" 				value=""/> 
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" 				value="">
		<input type="hidden" name="incluirRegistrosConBajaLogica" value="">
	</html:form>

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<siga:ConjBotonesAccion botones="G" />
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>

<script language="JavaScript">
	jQuery.noConflict();
	var indice = 0;

	
	jQuery(document).ready(function () {
		 <% if (volver) {  %>
		 document.getElementById('idBuscarCargos').onclick();	
		 <% } %>
	});	
		
	
	function preAccionBuscarCargos(){
		jQuery("#idButtonL").removeAttr("disabled");
		jQuery("#idButtonB").removeAttr("disabled");
		jQuery("#numeroColegiado").removeAttr("disabled");
		jQuery("#nombreColegiado").removeAttr("disabled");
		jQuery("#cargos").removeAttr("disabled");
		jQuery("#idInstitucionCargo").removeAttr("disabled");
		jQuery("#fechaCargo").removeAttr("disabled");
		jQuery("#fechaCargo-datepicker-trigger").removeAttr("disabled");
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
		jQuery("#fechasParaInsertar").show();
		validarAnchoTabla ();
		fin();
		
	}
	
	function limpiarColegiado(){
		document.getElementById("idPersona").value = '';
			document.BusquedaComisionesForm.numeroColegiado.value = '';
			document.BusquedaComisionesForm.nombreColegiado.value = '';
			document.getElementById('idBuscarCargos').onclick();
	}
	
	function buscarColegiado(){
	
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
		
	function preAccionColegiado(){		
		var insti=document.getElementById("idInstitucionCargo").value;
	 	if(insti== null || insti== "" ){
	 		alert("<siga:Idioma key='censo.comisiones.colObligatorio'/>");
		 	return false;
	 	} 				
	}
			
	function postAccionColegiado(){
	}

	function accionInsertarRegistroTabla () {
		//validando que exista fecha de inicio, que se usara en los nuevos registros
		if(document.getElementById("fechaInicioCargos").value==null || document.getElementById("fechaInicioCargos").value==""){
			alert("<siga:Idioma key='censo.comisiones.inicioObligatorio'/>");
			return false;	
		}		
		
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
	   	jQuery('#fechaCargo-datepicker-trigger').attr("disabled","disabled");
	   	jQuery("#invokefechaCargo").hide();
		crearFila();
		jQuery("#idInsertarCargo").attr("disabled","disabled");
	}
	 		
	function validarDatosMinimos () {		
		if(document.getElementById("idInstitucionCargo").value==null || document.getElementById("idInstitucionCargo").value==""){
			alert("<siga:Idioma key='censo.comisiones.colObligatorio'/>");
			return false;
		} 	
		
		if(document.getElementById ("vacio")!=null)
			document.getElementById ("vacio").style.display="none";
		
		return true;
	}
	
	function crearFila() { 
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
		td.setAttribute("width", "15%");
		td.setAttribute("align", "center");
		td.innerHTML ='<input type="hidden" id="fechaInicio_' + numFila + '" value="' + document.getElementById("fechaInicioCargos").value + '"/>';
		td.innerHTML+=document.getElementById("fechaInicioCargos").value;
		td.setAttribute("width", "10%");
		
		td = tr.insertCell(1); 
		td.setAttribute("width", "15%");
		td.setAttribute("align", "center");
		td.innerHTML='<siga:ComboBD ancho="140" nombre="cargos_' + numFila + '" id="cargos_' + numFila + '" tipo="cmbCargosJunta" estilo="margin-top:4px;" parametro='<%=parametro%>' clase="boxCombo" />';
		
		td = tr.insertCell(2); 
		td.setAttribute("width", "10%");
		td.setAttribute("align", "center");
		td.innerHTML ='<input type="text" onmousedown="bloquearBuscar(\''+ numFila +'\');" onChange="buscarColegiadoN(\''+ numFila +'\');"  id="numeroColegiado_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:5px;" value=""/>';
		td.innerHTML+='<input type="hidden" id="idPerson_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:2px;" value=""/>';
		
		td = tr.insertCell(3); 
		td.setAttribute("width", "35%");
		//td.innerHTML ='<input type="text" id="numeroColegiado_' + numFila + '" class="box" size="4" maxlength="9" style="width:70;margin-top:2px;" value=""/>';
		td.innerHTML ='<table><tr>';
		td.innerHTML+='<td><input type="text" id="nombreColegiado_' + numFila + '" class="box" style="width:120;margin-top:2px;margin-rigth:1px;" value="" maxlength="35"/>' + ' ';
		td.innerHTML+='<input type="text" onChange="buscarNumColegiadoN(\''+ numFila +'\');" id="apellidosColegiado_' + numFila + '" class="box" style="width:200;margin-top:2px;margin-rigth:1px;" value="" maxlength="40"/></td>';         
		td.innerHTML+='</tr></table>';
        
		td = tr.insertCell(4); 	
		//td.setAttribute("colspan", "2");
		td.setAttribute("align", "center");
		td.className = "";
		td.setAttribute("width", "20%");
		td.innerHTML ='<input type="button" class="button" style="margin:4px;" name="Buscar_'  + numFila +  '" id="idButtonB__' + numFila + '" value="<siga:Idioma key="general.boton.search" />" onClick="buscarColegiadoNBoton(' + numFila + ');">';
		td.innerHTML+='<input type="button" class="button" style="margin:4px;" name="Limpiar_' + numFila + '"  id="idButton_'   + numFila + '" value="<siga:Idioma key="general.boton.clear" />"  onClick="limpiarColegiadoN(' + numFila + ');">';
		
		td = tr.insertCell(5); 
		td.setAttribute("width", "10%");
		td.setAttribute("align", "center");
		td.innerHTML= '<img id="iconoboton_consultar1"  src="/SIGA/html/imagenes/bconsultar_disable.gif" alt="<siga:Idioma key='general.boton.consultar'/>" title="<siga:Idioma key='general.boton.consultar'/>" name="consultar_1" border="0"> ';
		td.innerHTML+='<img id="iconoboton_editar1"  src="/SIGA/html/imagenes/beditar_disable.gif" alt="<siga:Idioma key='general.boton.editar'/>" title="<siga:Idioma key='general.boton.editar'/>" name="editar_1" border="0"> ';
		td.innerHTML+='<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="borrar_1" border="0" onclick="borrarFila( ' + numFila + ',\''+ tr.id +'\')">';

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

	function finalizarFila(num){
		if (jQuery("#editar_"+num)[0].checked) {
			//validando que exista fecha de fin
			if (!jQuery("#fechaFinCargos")[0].value) {
				jQuery("#editar_"+num)[0].checked = false;
				alert ("<siga:Idioma key='censo.comisiones.finObligatorio'/>");
				return false;
			}
			
			jQuery("#textoFinalizar_"+num)[0].style.display="none";
			jQuery("#fechaFinalizar_"+num)[0].innerHTML=jQuery("#fechaFinCargos")[0].value;
			jQuery("#fechaFinalizar_"+num)[0].style.display="inline";
			jQuery("#fechaFin_"+num)[0].value=jQuery("#fechaFinCargos")[0].value;
		} else {
			jQuery("#textoFinalizar_"+num)[0].style.display="inline";
			jQuery("#fechaFinalizar_"+num)[0].innerHTML="";
			jQuery("#fechaFinalizar_"+num)[0].style.display="none";
			jQuery("#fechaFin_"+num)[0].value="";
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
	
	function buscarColegiadoNBoton(num){
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
        
        if (document.getElementById("idPerson_"+num).value!="")
        	comprobarFila("fila_" + num);
	}

	function bloquearBuscar(numFila){
	   	jQuery("#idButtonB__"+numFila).attr("disabled","disabled");
	}	
	
	function limpiarColegiadoN(num)	{
		document.getElementById("idPerson_"+num).value = '';
		document.getElementById("numeroColegiado_"+num).value = '';
		document.getElementById("nombreColegiado_"+num).value = '';
		document.getElementById("apellidosColegiado_"+num).value = '';
	}
	
	function preAccionColegiadoN(){
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
        
        if (document.getElementById("idPerson_"+num).value!="")
        	comprobarFila("fila_" + num);
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

	function validarDatosFila (fila) {
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
	
	// getDatos(): A partir de la tabla que contiene en HTML todos los datos, los va recogiendo en una cadena, que se entregara al Servidor para que la procese
	// En vez de pasar los datos en un formulario, los va concatenando en una cadena: La persona que construyo esta página, se salto la asignatura de estructuras y la de aplicaciones Web en la Universidad, o bien es fisic@, quimic@ o biolog@ 
	function getDatos(idTabla)
	{
		// obteniendo la tabla HTML a partir del ID pasado
		table = document.getElementById(idTabla);
		numeroFilas = table.rows.length;
		if (numeroFilas==0)
			return null;
		
		var datos = "";
		
		// obteniendo y cargando los datos de cada fila
		var yaTerminamosRegistrosAntiguosEmpezamosConRegistrosNuevos = "";
		for (var a = 0; a < numeroFilas-1 ; a++) { //hasta la penultima fila, pq la ultima fila nunca esta rellena (siempre se crea otra nueva cuando se rellena una)
			i = table.rows[a].id.split("_")[1];
			
			// comprobando que cada fila tiene todos los datos necesarios rellenos
			var validado = validarDatosFila (i);
			if (!validado) {
				fin();
				return 'cancel';
			}
			
			if(yaTerminamosRegistrosAntiguosEmpezamosConRegistrosNuevos=="" && document.getElementById("idPersona_" + i) != null) { //hay idPersona: registros existentes que se actualizan
				// comprobando que se edita el cargo-persona existente
				if(document.getElementById("editar_" + i) != null && document.getElementById("editar_" + i).checked && document.getElementById("editar_" + i).disabled == "") {
			        ncolegiado = document.getElementById("ncolegiado_" + i).value;
			        if(ncolegiado=="")
			        	datos += "0";
			        else
						datos += ncolegiado;
					datos += ',';
					
					IDCV = document.getElementById("IDCV_" + i).value;
					datos += IDCV;
					datos += ',';
	
					numero = document.getElementById("fechaFin_" + i).value;
					datos += numero;
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
			} else { //no hay idPersona: altas de registros
				if (yaTerminamosRegistrosAntiguosEmpezamosConRegistrosNuevos==""){
					datos += "#@#"; //separador de registros existentes que se actualizan de los registros nuevos a insertar
					yaTerminamosRegistrosAntiguosEmpezamosConRegistrosNuevos = "S";
				}
				
				carg = document.getElementById("cargos_" + i).options[document.getElementById("cargos_" + i).selectedIndex].value;
				datos += carg;
				datos += ',';
					
				numero = document.getElementById("fechaInicio_" + i).value;
				datos += numero;
				datos += ',';
				
				idpersona = document.getElementById("idPerson_" + i).value;
				datos += idpersona;
	
				datos += "%%%";
			}
		}
		
		return datos;
	} //getDatos()

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
	
	function comprobarFila(idFila){
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
	
	function calcularAltura() {			
		if (jQuery('table.botonesDetalle').exists()) {
			var tablaBotones = jQuery('table.botonesDetalle')[0];						
			var tablaDatos = jQuery('#divCargos')[0];
			
			var posTablaBotones = tablaBotones.offsetTop;
			var posTablaDatos = tablaDatos.offsetTop;
			
			jQuery('#divCargos').height(posTablaBotones - posTablaDatos);
		}
	}			

	function validarAnchoTabla() {
		if (document.getElementById("cargostabla").clientHeight < document.getElementById("divCargos").clientHeight) {
			document.getElementById("tabCargosCabeceras").width='100%';
		}
		else {
			document.getElementById("tabCargosCabeceras").width='98.30%';
		}
	}
	
	// Funcion asociada a boton buscar 
	function  consultar (idFila) { 
		//document.BusquedaComisionesForm.modo.value = "editar";
		document.datosCVForm.mantenimiento.value="S";

		document.datosCVForm.nombreUsuario.value=document.getElementById("namecolegiado_" + idFila).value;
		document.datosCVForm.numeroUsuario.value=document.getElementById("ncolegiado_" + idFila).value;			
		document.datosCVForm.idPersona.value=document.getElementById("idPersona_" + idFila).value; 	
		document.datosCVForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucion.value; 	
		document.datosCVForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;	
		document.datosCVForm.idCV.value=document.getElementById("IDCV_" + idFila).value; 	
		document.datosCVForm.modo.value = "verModal";
		document.datosCVForm.submit();
	}

	function  editarCargo (idFila) { 
		document.datosCVForm.mantenimiento.value="S";
		document.datosCVForm.nombreUsuario.value=document.getElementById("namecolegiado_" + idFila).value;
		document.datosCVForm.numeroUsuario.value=document.getElementById("ncolegiado_" + idFila).value;
		document.datosCVForm.idPersona.value=document.getElementById("idPersona_" + idFila).value; 	 	
		document.datosCVForm.idInstitucion.value=document.BusquedaComisionesForm.idInstitucion.value; 
		document.datosCVForm.idInstitucionCargo.value=document.BusquedaComisionesForm.idInstitucionCargo.value;	
		document.datosCVForm.idCV.value=document.getElementById("IDCV_" + idFila).value; 	
		document.datosCVForm.modo.value = "editarModal";
		document.getElementById('idBuscarCargos').onclick();
		document.datosCVForm.submit();
	}
			
	function accionGuardar(){
         document.getElementById('idGuardarCargos').onclick();
		//documentResultado =window.frames['resultado'];
		//documentResultado.finalizar();		
	}	
	
	// Funcion asociada a boton limpiar -->
	function limpiar() {				
		document.forms[0].reset();
	}
	
	//Funcion asociada a boton Nuevo -->
	function nuevo() {			
		document.forms[0].target="mainWorkArea";
		document.forms[0].modo.value = "nuevo";
		document.forms[0].submit();
	}	
</script>

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