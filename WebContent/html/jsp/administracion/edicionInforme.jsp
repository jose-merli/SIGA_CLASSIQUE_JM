<!-- edicionInforme.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	
	
</head>

<body onload="cargarListadoArchivos();inicio();">
 
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="administracion.informes.edicion.titulo"/>
		</td>
	</tr>
</table>
<html:form action="/ADM_GestionInformes" >
	<html:hidden property="modo" />
	<html:hidden property="modoInterno" />
	<html:hidden property="claseTipoInforme" />
</html:form>
<html:javascript formName="InformeFormEdicion" staticJavascript="true" />
<html:form action="/ADM_GestionInformes"  name="InformeFormEdicion" type="com.siga.administracion.form.InformeForm" method="POST" target="submitArea" enctype="multipart/form-data">
	<html:hidden property="modo" value="${InformeFormEdicion.modo}"/>
	<html:hidden property="modoInterno" value="${InformeFormEdicion.modoInterno}"/>
	<html:hidden property="idPlantilla" value="${InformeFormEdicion.idPlantilla}"/>
	<html:hidden property="destinatarios" value="${InformeFormEdicion.destinatarios}"/>
	<html:hidden property="claseTipoInforme" value="${InformeFormEdicion.claseTipoInforme}"/>
	<html:hidden property="idConsulta" value="${InformeFormEdicion.idConsulta}"/>
	<html:hidden property="idInstitucionConsulta" value="${InformeFormEdicion.idInstitucionConsulta}"/>
	<input type="hidden" name="location" value="${InformeFormEdicion.usrBean.location}"/>
	<input type="hidden" name="actionModal" />

	
	<bean:define id="location" name="InformeFormEdicion" property="usrBean.location"  />
	
	<table width="100%" border="0">
		<tr>
			<td width="15%"></td>
			<td width="25%"></td>
			<td width="20%"></td>
			<td width="40%"></td>
			
		</tr>
		
		<tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="administracion.informes.literal.tipoInforme"/>(*)
				</td>	
				<td class="labelText">
					<html:select styleClass="boxCombo" style="width:200px;"
						name="InformeFormEdicion" property="idTipoInforme" onchange="onChangeIdTipoInforme();"  >
						<bean:define id="tiposInforme" name="InformeForm"
							property="tiposInforme" type="java.util.Collection" />
							<html:optionsCollection  name="tiposInforme" value="idTipoInforme"
									label="descripcion" />
					</html:select>
					<logic:notEmpty name="InformeForm" property="tiposInforme">
							<logic:iterate name="InformeForm" property="tiposInforme"
								id="tipoInforme" indexId="index">
								<input type="hidden" name="claseTipoInforme_${index}" value="${tipoInforme.clase}">
								<input type="hidden" name="directorioTipoInforme_${index}" value="${tipoInforme.directorio}">
							</logic:iterate>
						</logic:notEmpty>
				</td>
				
				<td class="labelText" >
					<siga:Idioma key="administracion.informes.literal.colegio"/>(*)
				</td>
				<td class="labelText" colspan="3">
					<html:select styleClass="boxCombo" style="width:200px;" name="InformeFormEdicion" property="idInstitucion"  >
						<bean:define id="instituciones" name="InformeForm" property="instituciones" type="java.util.Collection" />
						<html:optionsCollection  name="instituciones" value="idInstitucion" label="abreviatura" />
					</html:select>
					
					<logic:notEmpty name="InformeForm" property="instituciones">
						<logic:iterate name="InformeForm" property="instituciones" id="institucion" indexId="index">
							<input type="hidden" name="institucionId_${index}" value="${institucion.idInstitucion}">
						</logic:iterate>
					</logic:notEmpty>
					
				</td>
			</tr>
			
		</tr>
		<tr>
			<td class="labelText" >
				<siga:Idioma key="administracion.informes.literal.nombre"/>(*)
			</td>
			<td class="labelText">
				<html:text name="InformeFormEdicion"  property="alias"  styleClass="box" onblur="onBlurAlias();"></html:text>
			</td>
			
		
		
			<td class="labelText" rowspan ="2"><siga:Idioma key="administracion.informes.literal.descripcion"/>(*)</td>
			<td rowspan ="2">&nbsp;&nbsp;<html:textarea name="InformeFormEdicion"  property="descripcion"  onchange="cuenta(this,1024)" cols="65" rows="2" style="overflow=auto;width=400;height=80" onkeydown="cuenta(this,1024);" styleClass="boxCombo"  readonly="false"></html:textarea></td>
			
			
		</tr>
		<tr id="ocultarOrden">
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.orden"/>
			</td>
			<td class="labelText">
				<html:text name="InformeFormEdicion"  property="orden"  styleClass="box"></html:text>
					</td>
			
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.directorio"/>(*)
			</td>
			<td class="labelText">
				<html:text name="InformeFormEdicion"  property="directorio"  styleClass="box" ></html:text>
					</td>
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.nombreFisico"/>(*)
			</td>
			<td class="labelText">
				<html:text name="InformeFormEdicion"  property="nombreFisico" styleClass="box"></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.nombreFichGenerado"/>(*)
			</td>
			<td class="labelText">
				<html:text name="InformeFormEdicion"  property="nombreSalida" styleClass="box"></html:text>
					</td>
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.formato"/>
			</td>
			<td class="labelText">
				<html:select name="InformeFormEdicion"  property="tipoFormato"
				styleClass="boxCombo"><html:option value="">
					<siga:Idioma key="general.combo.seleccionar" />
				</html:option>
				<html:option value="W">
					<siga:Idioma key="administracion.informes.formato.word"/>
				</html:option>
				<html:option value="X">
					<siga:Idioma key="administracion.informes.formato.excel"/>
				</html:option>
				<html:option value="P"><siga:Idioma key="administracion.informes.formato.pdf"/></html:option>
			</html:select></td>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.visible"/>(*)
			</td>
			<td class="labelText">
					<html:select property="visible"  name="InformeFormEdicion"  styleClass="boxCombo" >
						<html:option value=""><siga:Idioma key="general.combo.seleccionar"/>
							</html:option>
							<html:option value="S"><siga:Idioma key="general.yes"/>
							</html:option>
							<html:option value="N"><siga:Idioma key="general.no"/></html:option>
					</html:select>							
			</td>
			<td class="labelText" id="ocultarLabelPreseleccionado">
					<siga:Idioma key="administracion.informes.literal.preseleccionado"/>(*)
			</td>
			<td class="labelText" id="ocultarSelectPreseleccionado">
					<html:select property="preseleccionado"  name="InformeFormEdicion"  styleClass="boxCombo" >
						<html:option value=""><siga:Idioma key="general.combo.seleccionar"/>
						</html:option>
						<html:option value="S"><siga:Idioma key="general.yes"/>
						</html:option>
						<html:option value="N"><siga:Idioma key="general.no"/></html:option>
					</html:select>		
			</td>
		</tr>
		<tr  id="ocultarSolicitantes">
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.solicitantes"/>(*)
			</td>
			<td class="labelText">
					<html:select name="InformeFormEdicion"  property="ASolicitantes"  styleClass="boxCombo" >
						<html:option value=""><siga:Idioma key="general.combo.seleccionar"/>
							</html:option>
							<html:option value="S"><siga:Idioma key="general.yes"/>
							</html:option>
							<html:option value="N"><siga:Idioma key="general.no"/></html:option>
					</html:select>		
			</td>
					
			<td class="labelText">
					<siga:Idioma key="administracion.informes.literal.destinatarios.enviarA"/>(*)
			</td>
					
			<td class="labelText">
					<input type="checkbox" name="destinatariosCheck" value="C" ><siga:Idioma key="administracion.informes.destinatarios.colegiados"/>
					<input type="checkbox" name="destinatariosCheck" value="S" ><siga:Idioma key="administracion.informes.destinatarios.solicitantes"/>
					<input type="hidden" name="destinatariosCheck" value="J" >
			</td>
		</tr>
		<tr>
			<td colspan = "4" >&nbsp;</td>
		</tr>
	</table>
</html:form>	
<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
					
</iframe>

<c:choose>
<c:when test="${((InformeFormEdicion.idInstitucion=='0'&&InformeFormEdicion.usrBean.location=='2000')||InformeFormEdicion.idInstitucion==InformeFormEdicion.usrBean.location)&&InformeForm.modo!='consultar'}">		
	<siga:ConjBotonesAccion botones="G,C,R" modal="P" />
</c:when>
<c:otherwise>
	<siga:ConjBotonesAccion botones="C" modal="P" />
</c:otherwise>
</c:choose>
	<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>	
<script type="text/javascript">
<!-- Asociada al boton GuardarCerrar -->
function cargarListadoArchivos(){
	
	document.InformeFormEdicion.target = "resultado";
	document.InformeFormEdicion.modoInterno.value = document.InformeForm.modo.value;
	if(document.InformeForm.modo.value=='modificar'||document.InformeForm.modo.value=='consultar'){
		document.InformeFormEdicion.modo.value = "listadoArchivosInforme";
		document.InformeFormEdicion.submit();
		ajusteAlto('resultado');
		
	}else {
		document.InformeFormEdicion.modo.value = "nuevoListadoArchivosInforme";
	}
}
//Cuando hacemos algo en alias lo ponemos en nombre generado, si en este todavia no se ha introducido nada
function onBlurAlias() 
{	
	if(document.InformeFormEdicion.alias.value!=''&&document.InformeFormEdicion.nombreSalida.value==''){
		document.InformeFormEdicion.nombreSalida.value=document.InformeFormEdicion.alias.value;
	}
}
//Cuando la clase del tipo de informe es Personalizable permitimos poner tipo de archivo
function onChangeIdTipoInforme() 
{	
	indiceTipoInforme = document.getElementById("idTipoInforme").selectedIndex;
	idClaseTipoInforme = 'claseTipoInforme_'+indiceTipoInforme;
	idDirectorioTipoInforme = 'directorioTipoInforme_'+indiceTipoInforme;
	claseTipoInforme =  document.getElementById(idClaseTipoInforme).value;
	directorioTipoInforme =  document.getElementById(idDirectorioTipoInforme).value;
	document.getElementById("directorio").disabled = "";
	document.InformeFormEdicion.directorio.value = directorioTipoInforme;
	document.getElementById("directorio").disabled = "disabled";
	if(claseTipoInforme=='P'||claseTipoInforme=='C'){
		document.getElementById("tipoFormato").disabled="";
	}else{
		document.getElementById("tipoFormato").disabled="disabled";	
	}
}
function inicio() 
{	
	var listaDestinatarios = document.InformeFormEdicion.destinatarios.value;
	listaCheck = document.getElementsByName("destinatariosCheck");
	for(var i = 0 ; i <listaDestinatarios.length ; i++) {
		for(var j = 0 ; j <listaCheck.length ; j++) {
			if(listaCheck[j].value == listaDestinatarios.charAt(i)){
				listaCheck[j].checked = "checked";
				break;
			}
	
		}
	}
	if(document.InformeForm.claseTipoInforme.value=='P'||document.InformeForm.claseTipoInforme.value=='C'){
		document.getElementById("tipoFormato").disabled="";
	}else{
		document.getElementById("tipoFormato").disabled="disabled";	
	
	}
	document.getElementById("directorio").disabled="disabled";
	
	if(document.InformeForm.modo.value=='modificar'){
		document.getElementById("idInstitucion").disabled="disabled";
		document.getElementById("idPlantilla").disabled="disabled";
		//document.getElementById("tipoFormato").disabled="disabled";
		document.getElementById("idTipoInforme").disabled="disabled";
		
				
	}else if(document.InformeForm.modo.value=='consultar'){
	
		inputs = document.getElementsByTagName("input");
		for(var i = 0 ; i <inputs.length ; i++) {
			input = inputs[i];
			input.disabled =  "disabled"; 
		}
		selects = document.getElementsByTagName("select");
		for(var i = 0 ; i <selects.length ; i++) {
			select = selects[i];
			select.disabled =  "disabled"; 
		}
		textareas = document.getElementsByTagName("textarea");
		for(var i = 0 ; i <textareas.length ; i++) {
			textarea = textareas[i];
			textarea.disabled =  "disabled"; 
		}
		document.getElementById ("titulo").innerHTML = '<siga:Idioma key='administracion.informes.consulta.titulo'  />';
				
	}else if(document.InformeForm.modo.value=='insertar'){
		document.getElementById ("titulo").innerHTML = '<siga:Idioma key='administracion.informes.nuevo.titulo'  />';
		if(document.getElementById("location").value=='2000'){
			document.getElementById("idInstitucion").disabled="";
			document.getElementById("idPlantilla").disabled="";
			document.getElementById("idTipoInforme").disabled="";
			
			// document.getElementById("tipoFormato").disabled="";
		}else{
			//Mostramos la propia institcion(0 seleciona , 1 pordefecto, 2 intitucion)
			document.getElementById("idInstitucion").selectedIndex = "2";
			document.getElementById("idInstitucion").disabled="disabled";
			document.getElementById("idPlantilla").disabled="";
			document.getElementById("idTipoInforme").disabled="";
			// document.getElementById("tipoFormato").disabled="";
		}
	
	}
	if(document.InformeFormEdicion.idTipoInforme.value=='CON'){
		//document.InformeFormEdicion.idTipoInforme.value=document.InformeForm.idTipoInforme.value;
		//alert("document.getElementById(idTipoInforme)"+document.getElementById("idTipoInforme").disabled);
		gestionarDatosConsultas();
		// document.getElementById("idTipoInforme").disabled = "disabled";
		
	}
}
function accionGuardar() 
{		
	
	document.getElementById("directorio").disabled="";
	document.getElementById("idInstitucion").disabled="";
	document.getElementById("idPlantilla").disabled="";
	document.getElementById("idTipoInforme").disabled="";
	// document.getElementById("tipoFormato").disabled="";
				
	if (document.InformeForm.modo.value=='insertar'){
		indiceTipoInforme = document.getElementById("idTipoInforme").selectedIndex;
		idClaseTipoInforme = 'claseTipoInforme_'+indiceTipoInforme;
		claseTipoInforme =  document.getElementById(idClaseTipoInforme).value;
		document.InformeFormEdicion.claseTipoInforme.value = claseTipoInforme;

		indiceInstitucion = document.getElementById("idInstitucion").selectedIndex;
		idInstitucion = 'institucionId_'+indiceInstitucion;
		document.InformeFormEdicion.idInstitucion.value = document.getElementById(idInstitucion).value;		
	}
	if(document.InformeFormEdicion.idInstitucion.value=='-1')
		document.InformeFormEdicion.idInstitucion.value = '';
	if(document.InformeFormEdicion.idTipoInforme.value=='-1')
		document.InformeFormEdicion.idTipoInforme.value = '';
	if(document.InformeFormEdicion.tipoFormato.value=='-1')
		document.InformeFormEdicion.tipoFormato.value = '';
	if(document.InformeFormEdicion.preseleccionado.value=='-1')
		document.InformeFormEdicion.preseleccionado.value = '';
	if(document.InformeFormEdicion.ASolicitantes.value=='-1')
		document.InformeFormEdicion.ASolicitantes.value = '';
	if(document.InformeFormEdicion.visible.value=='-1')
		document.InformeFormEdicion.visible.value = '';
	
	sub();
	if(document.InformeFormEdicion.idTipoInforme.value!='CON'){ 
		listaDestinatarios = document.getElementsByName("destinatariosCheck");
		var destinatarios ="";
		for(var i = 0 ; i <listaDestinatarios.length ; i++) {
			if(listaDestinatarios[i].checked){
				destinatarios+=listaDestinatarios[i].value
			}	
		}
		if(destinatarios==''){
			error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.destinatarios' />";
			alert(error);
			fin();
			document.getElementById("idInstitucion").disabled="disabled";
			document.getElementById("idPlantilla").disabled="disabled";
			document.getElementById("idTipoInforme").disabled="disabled";
			document.getElementById("directorio").disabled="disabled";
			return false;
		}
		document.InformeFormEdicion.destinatarios.value = destinatarios;
	}else{
		document.InformeFormEdicion.destinatarios.value = 'C';
		document.InformeFormEdicion.preseleccionado.value = 'S';
		document.InformeFormEdicion.ASolicitantes.value = 'S';
	}
	
	document.InformeFormEdicion.modo.value = document.InformeForm.modo.value; 
	document.getElementById("directorio").disabled = "";
	 if (validateInformeFormEdicion(document.InformeFormEdicion)){
		document.InformeFormEdicion.submit();
		document.InformeForm.modo.value="modificar";
		if(formatearFormulario(document.InformeFormEdicion)){
			alert("<siga:Idioma key='administracion.informes.mensaje.aviso.sustituyeEspacios'/>");
		}
		
	 }else{
	 	fin();
 	}
	document.getElementById("idInstitucion").disabled="disabled";
	document.getElementById("idPlantilla").disabled="disabled";
	document.getElementById("idTipoInforme").disabled="disabled";
	// document.getElementById("tipoFormato").disabled="disabled";
	document.getElementById("directorio").disabled = "disabled";
}
function formatearFormulario(formulario)
{
	var existeAlgunEspacio = false;
	var aux = formulario.nombreFisico.value;
	formulario.nombreFisico.value = replaceAll(formulario.nombreFisico.value,' ','_');
	if(!existeAlgunEspacio){
		existeAlgunEspacio = (aux != formulario.nombreFisico.value);
	}
	aux = formulario.nombreSalida.value;
	formulario.nombreSalida.value = replaceAll(formulario.nombreSalida.value,' ','_');
	if(!existeAlgunEspacio){
		existeAlgunEspacio  =  (aux != formulario.nombreSalida.value);
	}
	aux = formulario.directorio.value;
	formulario.directorio.value = replaceAll(formulario.directorio.value,' ','_');
	if(!existeAlgunEspacio){
		existeAlgunEspacio  =  (aux != formulario.directorio.value);
	}
	return existeAlgunEspacio;
}

function refrescarLocal()
{	
	if (document.InformeForm.modo.value=='insertar'){
		window.close();
	}else{
		cargarListadoArchivos();
	}
	
}
<!-- Asociada al boton Cerrar -->
function accionCerrar() 
{		
	// parent.refrescarLocal();
	window.close(); 
	 // document.InformeForm.modo.value = "buscar";
	 // document.InformeForm.submit();
	 // window.close(); 
	
}
function gestionarDatosConsultas() 
{		
	document.getElementsByName("idTipoInforme")[0].disabled =  "disabled";
	document.getElementById("alias").disabled =  "disabled";
	document.getElementById("ocultarSolicitantes").style.display =  "none";
	//document.getElementById("ocultarOrden").style.display =  "none";
	document.getElementById("ocultarLabelPreseleccionado").style.display =  "none";
	document.getElementById("ocultarSelectPreseleccionado").style.display =  "none";
	
	
}
<!-- Asociada al boton Restablecer -->
function accionRestablecer() 
{		
	document.InformeFormEdicion.reset();
}

</script>

</body>

</html>