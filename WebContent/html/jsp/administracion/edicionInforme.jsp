<!-- edicionInforme.jsp -->

<!-- CABECERA JSP -->
<%@page import="java.util.ArrayList"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type"
	content="text/html; charset=ISO-8859-1">
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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
		
	<script src="<html:rewrite page="/html/js/jquery.js"/>" type="text/javascript" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
	<script	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
	
</head>
<script type="text/javascript">
jQuery.noConflict();

function inicioPlantillasEnvio() {
	var comboTiposEnvio = document.getElementById('idTipoEnvioDefecto');
	var comboPlantilla = document.getElementById('idPlantillaEnvioDefecto');
	
	if(comboPlantilla.options.length>0&&comboTiposEnvio.value!=''){
	
	jQuery.ajax({ //Comunicación jQuery hacia JSP  
           type: "POST",
           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
           data: "idTipoEnvio="+comboTiposEnvio.value,
           dataType: "json",
           success:  function(json) {
       			var plantillasEnvio = json.plantillasEnvio;
       			var optionComboPlantilla = comboPlantilla.options;
       			//Si tiene opciones el valor es el que iene que estar seleccioando
       			if(optionComboPlantilla){
       				var valueComboPlantilla = optionComboPlantilla[0].value;
       				//vaciamos la listas
					optionComboPlantilla.length = 0;
					jQuery("#idPlantillaEnvioDefecto").append("<option  value=''>&nbsp;</option>");
       				jQuery.each(plantillasEnvio, function(i,item2){
           				var selected = "";
           				if(valueComboPlantilla!='' && valueComboPlantilla==item2.idPlantillaEnvios){
           					selected = "selected";
       					}
                        jQuery("#idPlantillaEnvioDefecto").append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
                    });
       			}
	           			
           },
           error: function(xml,msg){
        	   alert("Error: "+msg);//$("span#ap").text(" Error");
           }
        });

	}else{
		jQuery("#idPlantillaEnvioDefecto").append("<option  value=''>&nbsp;</option>");

	}

}

function inicioTiposIntercambioTelematico() {
	var comboEnviosPermitidos = document.getElementById('comboTipoEnvioPermitidos');
	var tiposEnvio = comboEnviosPermitidos.options;
	var comboTiposIntercambioTelematico = document.getElementById('idTipoIntercambioTelematico');
	
	if(tiposEnvio.options.length>0 && tipoTelematicoSeleccionado(comboEnviosPermitidos)){
	
		jQuery.ajax({ //Comunicación jQuery hacia JSP  
           type: "POST",
           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryTiposIntercambioTelematico",
           data: "idTipoEnvio=6",
           dataType: "json",
           success:  function(json) {
       			var tiposIntercambio = json.tiposIntercambio;
       			var optionComboTiposIntercambioTelematico = comboTiposIntercambioTelematico.options;
       			//Si tiene opciones el valor es el que iene que estar seleccioando
       			if(comboTiposIntercambioTelematico){
       				var valueComboTiposIntercambioTelematico = comboTiposIntercambioTelematico[0].value;
       				//vaciamos la listas
					comboTiposIntercambioTelematico.length = 0;
					jQuery("#idTipoIntercambioTelematico").append("<option  value=''>&nbsp;</option>");
       				jQuery.each(tiposIntercambio, function(i,item2){
           				var selected = "";
           				if(valueComboTiposIntercambioTelematico !='' && valueComboTiposIntercambioTelematico==item2.idTipoIntercambioTelematico){
           					selected = "selected";
       					}
                        jQuery("#idTipoIntercambioTelematico").append("<option "+selected+" value='"+item2.idTipoIntercambioTelematico+"'>"+item2.nombre+"</option>");
                    });
       			}       			
	           			
           },
           error: function(xml,msg){
        	   alert("Error: "+msg);//$("span#ap").text(" Error");
           }
        });

	}else{
		jQuery("#idTipoIntercambioTelematico").append("<option  value=''>&nbsp;</option>");
	}

}

function onChangeTipoenvio() {
	var comboTiposEnvio = document.getElementById('idTipoEnvioDefecto');
	var comboPlantilla = document.getElementById('idPlantillaEnvioDefecto');
	if(comboTiposEnvio.value!=''){			
	jQuery.ajax({ //Comunicación jQuery hacia JSP  
           type: "POST",
           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
           data: "idTipoEnvio="+comboTiposEnvio.value,
           dataType: "json",
           success:  function(json) {
       			var plantillasEnvio = json.plantillasEnvio;
       			var optionComboPlantilla = comboPlantilla.options;
       			
       			optionComboPlantilla.length = 0;
       			jQuery("#idPlantillaEnvioDefecto").append("<option  value=''>&nbsp;</option>");

       				jQuery.each(plantillasEnvio, function(i,item2){
           				var selected = "";
                        jQuery("#idPlantillaEnvioDefecto").append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
                    });


           			
           },
           error: function(xml,msg){
        	   alert("Error: "+msg);//$("span#ap").text(" Error");
           }
        });
	}else{
		comboPlantilla.options.length = 0;

	}
}

function tipoTelematicoSeleccionado(comboEnviosPermitidos){
	var selected = ""; 
	for (var i = 0; i < comboEnviosPermitidos.options.length; i++) {
		if (comboEnviosPermitidos.options[i].selected) {
			selected = comboEnviosPermitidos.options[i].value.split(',')[1];
			if(selected =='6'){
				return true;
			}
		}
	}

	return false;
}

function onChangeTipoIntercambio() {
	var comboTiposIntercambioTelematico = document.getElementById('idTipoIntercambioTelematico');
	var comboEnviosPermitidos = document.getElementById('comboTipoEnvioPermitidos');

	if(tipoTelematicoSeleccionado(comboEnviosPermitidos)){
	
		jQuery.ajax({ //Comunicación jQuery hacia JSP  
           type: "POST",
           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryTiposIntercambioTelematico",
           data: "idTipoEnvio=6",
           dataType: "json",
           success:  function(json) {
       			var tiposIntercambio = json.tiposIntercambio;
        		jQuery("#idTipoIntercambioTelematico").empty();
        		jQuery("#idTipoIntercambioTelematico").append("<option  value=''>&nbsp;</option>");   
    			jQuery.each(tiposIntercambio, function(i,item2){
              		var selected = "";
                    jQuery("#idTipoIntercambioTelematico").append("<option "+selected+" value='"+item2.idTipoIntercambioTelematico+"'>"+item2.nombre+"</option>");
                });
           },
           error: function(xml,msg){
        	   alert("Error: "+msg);
           }
        });

	}else{
		jQuery("#idTipoIntercambioTelematico").empty();
		jQuery("#idTipoIntercambioTelematico").append("<option  value=''>&nbsp;</option>");
	}
}

</script>
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
	<html:hidden styleId="modo" property="modo"  value="${InformeFormEdicion.modo}"/>
	<html:hidden styleId="modoInterno" property="modoInterno" value="${InformeFormEdicion.modoInterno}"/>
	<html:hidden styleId="idPlantilla"  property="idPlantilla" value="${InformeFormEdicion.idPlantilla}"/>
	<html:hidden styleId="destinatarios"  property="destinatarios"  value="${InformeFormEdicion.destinatarios}"/>
	<html:hidden styleId="claseTipoInforme" property="claseTipoInforme"  value="${InformeFormEdicion.claseTipoInforme}"/>
	<html:hidden styleId="idConsulta" property="idConsulta" value="${InformeFormEdicion.idConsulta}"/>
	<html:hidden styleId="idInstitucionConsulta" property="idInstitucionConsulta" value="${InformeFormEdicion.idInstitucionConsulta}"/>
	<html:hidden styleId="idTiposEnvio" property="idTiposEnvio"/>
	<html:hidden styleId="idTipoEnvio" property="idTipoEnvio"/>
	<html:hidden styleId="idPlantillaEnvio" property="idPlantillaEnvio"/>
	<html:hidden styleId="idTipoIntercambioTelem" property="idTipoIntercambioTelem"/>
	
	<input type="hidden"  id="location"  name="location" value="${InformeFormEdicion.usrBean.location}"/>
	<input type="hidden"  id="actionModal" name="actionModal" />

	
	<bean:define id="location" name="InformeFormEdicion" property="usrBean.location"  />
	<bean:define id="comboTipoEnvio" name="comboTipoEnvio"  scope="request"/>
	<bean:define id="idTipoEnvioDef" name="idTipoEnvioDef"  scope="request"/>
	<bean:define id="idPlantillaEnvioDef" name="idPlantillaEnvioDef"  scope="request"/>
	<bean:define id="idTipoIntercambioTelem" name="idTipoIntercambioTelem"  scope="request"/>
	<bean:define id="intercambioTelematico" name="intercambioTelematico"  scope="request"/>
	<input type="hidden" id="comboTipoEnvioHidden" value="${comboTipoEnvio}" />
	<%  String[] parametrosComboEnvios = (String[])request.getAttribute("parametrosComboEnvios"); 
		ArrayList idTipoEnvioSeleccionado = (ArrayList)request.getAttribute("idTipoEnvioSeleccionado");
		ArrayList idPlantillaEnvioSeleccionado = (ArrayList)request.getAttribute("idPlantillaEnvioSeleccionado");
	
	%>
	





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

						<bean:define id="tiposInforme" name="InformeForm" property="tiposInforme" type="java.util.Collection" />
						<html:optionsCollection  name="tiposInforme" value="idTipoInforme"	label="descripcion" />

					</html:select>
					<logic:notEmpty name="InformeForm" property="tiposInforme">
						<c:set var="consulta" value="0" />
						<logic:iterate name="InformeForm" property="tiposInforme" id="tipoInforme" indexId="index">
							<c:if test="${tipoInforme.idTipoInforme == 'CON'}">
								<c:set var="consulta" value="1" />
							</c:if> 
							<c:choose>
								<c:when test="${consulta == '1' && tipoInforme.idTipoInforme != 'CON'}">

									<input type="hidden" name="claseTipoInforme_${index-1}" value="${tipoInforme.clase}">
									<input type="hidden" name="directorioTipoInforme_${index-1}" value="${tipoInforme.directorio}">
								</c:when>
								<c:when test="${consulta == '0' && tipoInforme.idTipoInforme != 'CON'}">
									<input type="hidden" name="claseTipoInforme_${index}" value="${tipoInforme.clase}">
									<input type="hidden" name="directorioTipoInforme_${index}" value="${tipoInforme.directorio}">
								</c:when>
							</c:choose>								
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
				<html:text name="InformeFormEdicion"  property="alias"  styleClass="box" maxlength="50" onblur="onBlurAlias();"></html:text>
			</td>
			
		
		
			<td class="labelText" rowspan ="2"><siga:Idioma key="administracion.informes.literal.descripcion"/>(*)</td>
			<td rowspan ="2">&nbsp;&nbsp;<html:textarea name="InformeFormEdicion"  property="descripcion"  onchange="cuenta(this,1024)" cols="65" rows="2" style="overflow=auto;width=400;height=80" onkeydown="cuenta(this,1024);" styleClass="boxCombo"  readonly="false"></html:textarea></td>
			
			
		</tr>
		<tr id="ocultarOrden">
			<td class="labelText">
				<siga:Idioma key="administracion.informes.literal.orden"/>(*)
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
				<html:option value="E">
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
							<html:option value="S"><siga:Idioma key="general.yes"/>
							</html:option>
							<html:option value="N"><siga:Idioma key="general.no"/></html:option>
					</html:select>		
			</td>
					
			<td class="labelText">
					<siga:Idioma key="administracion.informes.literal.destinatarios.enviarA"/>(*)
			</td>
					
			<td class="labelText">
					<input type="radio" name="destinatariosCheck" value="C" ><siga:Idioma key="administracion.informes.destinatarios.colegiados"/>
					<input type="radio" name="destinatariosCheck" value="S" ><siga:Idioma key="administracion.informes.destinatarios.solicitantes"/>
					<input type="radio" name="destinatariosCheck" value="P" ><siga:Idioma key="administracion.informes.destinatarios.procurador"/>
					<input type="radio" name="destinatariosCheck" value="J" ><siga:Idioma key="administracion.informes.destinatarios.juzgado"/>
			</td>
		</tr>
			<tr id="trEnvios">

				<td colspan="4">
					<table>
						<tr>
							<td width="15%"></td>
							<td width="25%"></td>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="15%"></td>
							<td width="15%"></td>

						</tr>
						<tr>
							<td class="labelText"><siga:Idioma
									key="administracion.informes.literal.tipoEnviosPermitidos" /> </td>
							<td>
							<siga:ComboBD nombre = "comboTipoEnvioPermitidos" tipo="${comboTipoEnvio}"
							 clase="box" filasMostrar="7"
							 parametro="${parametrosComboEnvios}"
							seleccionMultiple="true" 
							elementoSel="<%=idTipoEnvioSeleccionado%>" obligatorio="true"
							accion="accionComboTipoEnvio(this.selectedIndex);"
							
							
							/>
							
							</td>
							
							
						
							<td class="labelText"><siga:Idioma
									key="administracion.informes.literal.tipoEnvioDefecto" /></td>
							<td><select  style="width:132px;" id="idTipoEnvioDefecto" onchange="onChangeTipoenvio();">
									<option  value=""></option>
									<c:forEach items="${tipoEnviosBeans}" var="tipoEnvio">
										<c:set var="envioSeleccionado" value="" />
										<c:if test="${idTipoEnvioDef==tipoEnvio.idTipoEnvios}">
											<c:set var="envioSeleccionado" value="selected" />
										</c:if>
										<option ${envioSeleccionado} value="${tipoEnvio.idTipoEnvios}" ><c:out value="${tipoEnvio.nombre}"/> </option>
									</c:forEach>
							
									</select>
							</td>


							<td class="labelText"><siga:Idioma
									key="administracion.informes.literal.plantillaEnvioDefecto" /></td>

							<td><select style="width:202px;" id="idPlantillaEnvioDefecto">
									
										<option value="${idPlantillaEnvioDef}"><c:out value="${idPlantillaEnvioDef}" /></option>
									
								</select>
								<script type="text/javascript">
									inicioPlantillasEnvio();
								</script>	
							</td>
							
						</tr>





							<td colspan = "2"></td>
							<td class="labelText"><siga:Idioma
									key="administracion.informes.literal.tipointercambio" /></td>

							<td colspan = "3"><select style="width:460px;" id="idTipoIntercambioTelematico">
									<option value="${idTipoIntercambioTelem}"><c:out value="${idTipoIntercambioTelem}" /></option>
								</select>
								<script type="text/javascript">
								inicioTiposIntercambioTelematico();
								</script>								
							</td>						
						<tr>
						
						</tr>
					</table>
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

		
	if(document.InformeForm.modo.value=='modificar' || document.InformeForm.modo.value=='modificarTelematico' || document.InformeForm.modo.value=='consultar'){

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
		document.InformeFormEdicion.nombreFisico.value=document.InformeFormEdicion.alias.value;
		if(formatearFormulario(document.InformeFormEdicion)){
			alert("<siga:Idioma key='administracion.informes.mensaje.aviso.sustituyeEspacios'/>");
		}		
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
	
	if(document.InformeFormEdicion.idTipoInforme.value=='CON'){
		gestionarDatosConsultas();
		
	}
	
	// if(document.getElementById("idTipoInforme").value!='EJG' || document.getElementById("comboTipoEnvioHidden").value=='cmbTipoEnviosInst')
		// document.getElementById("trEnvios").style.display =  "none";
	// else
		//document.getElementById("trEnvios").style.display =  "block";
	
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



		if(${intercambioTelematico} == '0'){
			document.getElementById("idInstitucion").disabled="disabled";
			document.getElementById("idPlantilla").disabled="disabled";
			//document.getElementById("tipoFormato").disabled="disabled";
			document.getElementById("idTipoInforme").disabled="disabled";

		}else if (${intercambioTelematico} == '1'){
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
			jQuery('#idTipoEnvioDefecto').removeAttr("disabled");
			jQuery('#idPlantillaEnvioDefecto').removeAttr("disabled");
			jQuery('#modo').removeAttr("disabled");
			jQuery('#idButton').removeAttr("disabled");
			jQuery('#idButtonGuardar').removeAttr("disabled");
			jQuery('#modoInterno').removeAttr("disabled");
		}	

		if(document.InformeFormEdicion.idInstitucion.value == '0'){
			document.getElementById("idTipoEnvioDefecto").disabled="disabled";
			document.getElementById("idPlantillaEnvioDefecto").disabled="disabled";
		}
		

	}else if(document.InformeForm.modo.value=='consultar'){
	
		inputs = document.getElementsByTagName("input");
		for(var i = 0 ; i <inputs.length ; i++) {
			input = inputs[i];



			input.disabled =  "disabled"; 
		}
		jQuery('#idButton').removeAttr("disabled");
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
	}else{
		var tiposInforme = document.getElementById("idTipoInforme").options;
		for ( var i = 1; i < tiposInforme.length; i++) {
			var tipoInforme = tiposInforme[i];
			
			if(tipoInforme.value=="CON"){
				document.getElementById("idTipoInforme").remove(i);
				break;
				
			}
		}

	}


}


function accionGuardar() 
{
	
	//alert('a ver?'+document.getElementById("comboTipoEnvioPermitidos").options.length);
	var tiposEnvioSeleccionados = "";
	if(document.InformeFormEdicion.idTipoInforme.value!='CON'){
	
	
		var tiposEnvio = document.getElementById("comboTipoEnvioPermitidos").options;
		
		for ( var i = 1; i < tiposEnvio.length; i++) {
			var tipoEnvio = tiposEnvio[i];
			if(tipoEnvio.selected){
				tiposEnvioSeleccionados += tipoEnvio.value+'##';
				
			}
			
		}
		if(tiposEnvioSeleccionados==''){
			error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.tipoEnviosPermitidos' />";	
			
			alert(error);
			return false;	
		
		}
	}else{
		
		if(document.getElementById("alias").value.length>50){
			document.getElementById("alias").value = document.getElementById("alias").value.substring(0,50);
		}
		document.getElementById("alias").disabled="";
		
	}

	document.getElementById("directorio").disabled="";
	document.getElementById("idInstitucion").disabled="";
	document.getElementById("idPlantilla").disabled="";
	

	document.getElementById("idTipoInforme").disabled="";
	// document.getElementById("tipoFormato").disabled="";
	
	jQuery('#idTiposEnvio').removeAttr("disabled");
	jQuery('#idTipoIntercambioTelem').removeAttr("disabled");
	jQuery('#idTipoEnvio').removeAttr("disabled");
	jQuery('#idPlantillaEnvio').removeAttr("disabled");
	
	
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

	//Validación del campo Nombre Fichero Generado
	//Sólo se permite carac. Alfanumércos y guión bajo y alto
	
	if(!(validarAlfaNumericoYGuiones(document.InformeFormEdicion.nombreSalida.value)))
	{
		error = "<siga:Idioma key='administracion.informes.mensaje.aviso.caracteresAlfaNumericos'/>";
		alert(error);
		fin();
		return false;
	}
	document.InformeFormEdicion.idTiposEnvio.value = tiposEnvioSeleccionados;
	document.InformeFormEdicion.idTipoEnvio.value = document.getElementById("idTipoEnvioDefecto").value;
	document.InformeFormEdicion.idPlantillaEnvio.value = document.getElementById("idPlantillaEnvioDefecto").value;
	document.InformeFormEdicion.idTipoIntercambioTelem.value = document.getElementById("idTipoIntercambioTelematico").value;
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
	if(document.InformeFormEdicion.idTipoInforme.value=='CON')
		document.getElementById("alias").disabled="disabled";
	document.getElementById("idTipoInforme").disabled="disabled";
	// document.getElementById("tipoFormato").disabled="disabled";
	document.getElementById("directorio").disabled = "disabled";
	document.getElementById("idTiposEnvio").disabled="disabled";
	document.getElementById("idTipoIntercambioTelem").disabled="disabled";
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
		window.top.close();
	}else{
		cargarListadoArchivos();
	}
	
}
<!-- Asociada al boton Cerrar -->
function accionCerrar() 
{		
	// parent.refrescarLocal();
	window.top.close(); 
	 // document.InformeForm.modo.value = "buscar";
	 // document.InformeForm.submit();
	 // window.top.close(); 
	
}
function gestionarDatosConsultas() 
{		
	document.getElementsByName("idTipoInforme")[0].disabled =  "disabled";
	document.getElementById("alias").disabled =  "disabled";
	document.getElementById("ocultarSolicitantes").style.display =  "none";
	//document.getElementById("ocultarOrden").style.display =  "none";
	document.getElementById("ocultarLabelPreseleccionado").style.display =  "none";
	document.getElementById("ocultarSelectPreseleccionado").style.display =  "none";
	document.getElementById("trEnvios").style.display =  "none";
	
	
	
}

function accionComboTipoEnvio(index) {
	var envioDefectoSeleccionado = document.getElementById("idTipoEnvioDefecto").value;
	document.getElementById("idTipoEnvioDefecto").options.length = 0;

	var tiposEnvio = document.getElementById("comboTipoEnvioPermitidos").options;
	var findDefecto = false;
	jQuery("#idTipoEnvioDefecto").append("<option  value=''>&nbsp;</option>");
	for ( var i = 0; i < tiposEnvio.length; i++) {
		var optionTipoEnvio =tiposEnvio[i];
		if(optionTipoEnvio.selected){
			var idTipoEnvio = optionTipoEnvio.value.split(',')[1];
			if(!findDefecto && idTipoEnvio==envioDefectoSeleccionado){
				findDefecto = true;
			}
			jQuery("#idTipoEnvioDefecto").append("<option  value='"+idTipoEnvio+"'>"+optionTipoEnvio.text+"</option>");
		}
	}
	document.getElementById("idTipoEnvioDefecto").value = envioDefectoSeleccionado;
	if(!findDefecto){
		onChangeTipoenvio();

	}


	if(index != -1)
		onChangeTipoIntercambio();

}

<!-- Asociada al boton Restablecer -->
function accionRestablecer() 
{		
	document.InformeFormEdicion.reset();
}

jQuery(document).ready(function () {

	accionComboTipoEnvio(-1);
});

 //a.b();

</script>

</body>

</html>