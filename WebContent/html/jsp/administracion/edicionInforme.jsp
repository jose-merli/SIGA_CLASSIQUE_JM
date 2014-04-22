<!DOCTYPE html>
<html>
<head>
<!-- edicionInforme.jsp -->

<!-- CABECERA JSP -->
<%@page import="java.util.ArrayList"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>



	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js --> 
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
		
		<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
		<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
		
	</head>

	<script type="text/javascript">
		jQuery.noConflict();
		
		function inicioPlantillasEnvio() {
			var comboTiposEnvio = document.getElementById('idTipoEnvioDefecto');
			var comboPlantilla = document.getElementById('idPlantillaEnvioDefecto');			
			var plantillaDefectoSel ="${idPlantillaEnvioDef}";
			
			if(comboPlantilla.options.length>0 && comboTiposEnvio.value!=''){
			
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
		           type: "POST",
		           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
		           data: "idTipoEnvio="+comboTiposEnvio.value+"&idPlantillaEnvioDefecto="+plantillaDefectoSel,
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
		       				jQuery("#idPlantillaEnvioDefecto").change();
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
			
			if(${mostrarTipoIntercambio} == '1'){
			
				var comboEnviosPermitidos = jQuery('#comboTipoEnvioPermitidos');
				var comboTiposIntercambioTelematico = document.getElementById('idTipoIntercambioTelematico');
				
				if(jQuery('#comboTipoEnvioPermitidos').find("option").length>0 && jQuery('#comboTipoEnvioPermitidos').val() == "6"){
				
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
		}
		
		function onChangeTipoenvio() {
			var comboTiposEnvio = document.getElementById('idTipoEnvioDefecto');
			var comboPlantilla = document.getElementById('idPlantillaEnvioDefecto');
			if(comboTiposEnvio.value!='' && comboTiposEnvio.value != "undefined"){			
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
		
		function onChangeTipoIntercambio() {
			
			if(${mostrarTipoIntercambio} == '1'){
			
				var comboTiposIntercambioTelematico = document.getElementById('idTipoIntercambioTelematico');
				var comboEnviosPermitidos = jQuery('#comboTipoEnvioPermitidos');
			
				if(jQuery('#comboTipoEnvioPermitidos').val() == "6"){
				
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
		}
		
		
	</script>
	
	<style>
		
		.colIzq{clear:left;}
		
		.col50{width:49%;float:left;display:block;}
		.col25{width:24%;float:left;display:block;}
		.col33{width:32%;float:left;display:block;}
		
		.col90px {width:90px; min-height:24px;float:left;display:block;}
		.col120px{width:120px;min-height:24px;float:left;display:block;}
		.col180px{width:180px;min-height:24px;float:left;display:block;}
		.col220px{width:220px;min-height:24px;float:left;display:block;}
		.col300px{width:300px;min-height:24px;float:left;display:block;}
		

	</style>

<body onload="cargarListadoArchivos();inicio();ajusteAlto('resultado');">
		
	<%
	
	String modo = request.getAttribute("modo")==null ? "" : (String)request.getAttribute("modo");
	String estiloCombo = "boxCombo";
	String estiloTexto = "box";
	
	if(modo.equalsIgnoreCase("ver")){
		estiloCombo = "boxComboConsulta";
		estiloTexto = "boxConsulta";
	}
	
	%>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="administracion.informes.edicion.titulo"/>
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
		<html:hidden styleId="claseTipoInforme" property="claseTipoInforme"  value="${InformeFormEdicion.claseTipoInforme}"/>
		<html:hidden styleId="idConsulta" property="idConsulta" value="${InformeFormEdicion.idConsulta}"/>
		<html:hidden styleId="idInstitucionConsulta" property="idInstitucionConsulta" value="${InformeFormEdicion.idInstitucionConsulta}"/>
		<html:hidden styleId="idTiposEnvio" property="idTiposEnvio"/>
		<html:hidden styleId="idTipoEnvio" property="idTipoEnvio"/>
		<html:hidden styleId="idPlantillaEnvio" property="idPlantillaEnvio"/>
		<html:hidden styleId="idTipoIntercambioTelem" property="idTipoIntercambioTelem"/>
		
		<input type="hidden"  id="location"  name="location" value="${InformeFormEdicion.usrBean.location}"/>
		
		<bean:define id="location" name="InformeFormEdicion" property="usrBean.location"  />
		<bean:define id="comboTipoEnvio" name="comboTipoEnvio"  scope="request"/>
		<bean:define id="idTipoEnvioDef" name="idTipoEnvioDef"  scope="request"/>
		<bean:define id="idPlantillaEnvioDef" name="idPlantillaEnvioDef"  scope="request"/>
		<bean:define id="idTipoIntercambioTelem" name="idTipoIntercambioTelem"  scope="request"/>
		<bean:define id="intercambioTelematico" name="intercambioTelematico"  scope="request"/>
		<bean:define id="mostrarTipoIntercambio" name="mostrarTipoIntercambio"  scope="request"/>
		<input type="hidden" id="comboTipoEnvioHidden" value="${comboTipoEnvio}" />
		<%  String[] parametrosComboEnvios = (String[])request.getAttribute("parametrosComboEnvios"); 
			ArrayList idTipoEnvioSeleccionado = (ArrayList)request.getAttribute("idTipoEnvioSeleccionado");
			ArrayList idPlantillaEnvioSeleccionado = (ArrayList)request.getAttribute("idPlantillaEnvioSeleccionado");	
		%>
		<siga:ConjCampos leyenda="Datos informe">
		
		
		<div class="colIzq col50">
			<div class="conIzq col120px labelText"><siga:Idioma key="administracion.informes.literal.tipoInforme"/>(*)</div>
			<div class="col300px labelTextValue">
				<html:select styleClass="<%=estiloCombo%>" style="width:100%;" styleId="idTipoInforme"
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
			</div>
			<div class="colIzq col120px labelText">
				<siga:Idioma key="administracion.informes.literal.nombre"/> (*)
			</div>
			<div class="col180px labelTextValue">
				<html:text name="InformeFormEdicion"  property="alias" styleId="alias" styleClass="<%=estiloTexto%>" maxlength="50" onblur="onBlurAlias();" />
			</div>	
			<div class="colIzq col120px labelText">
				<siga:Idioma key="administracion.informes.literal.directorio"/>
			</div>
			<div class="col180px labelText">
				<html:text name="InformeFormEdicion"  property="directorio" styleId="directorio" styleClass="<%=estiloTexto%>" />
			</div>		
			<div class="colIzq col120px labelText">
				<siga:Idioma key="administracion.informes.literal.nombreFisico"/> (*)
			</div>	
			<div class="col180px labelTextValue" title="<siga:Idioma key='administracion.informes.mensaje.aviso.caracteresAlfaNumericos'/>">
				<html:text 	name="InformeFormEdicion"  property="nombreFisico" styleId="nombreFisico" styleClass="<%=estiloTexto%>"/>
			</div>
		</div>
		
		<div class="col50">
			<div class="colIzq col120px labelText">
				<siga:Idioma key="administracion.informes.literal.colegio"/> (*)
			</div>				
			<div class="col300px labelTextValue">
				<html:select styleClass="<%=estiloCombo%>" style="width:100%" styleId="idInstitucion" name="InformeFormEdicion" property="idInstitucion"  >
					<bean:define id="instituciones" name="InformeForm" property="instituciones" type="java.util.Collection" />
					<html:optionsCollection  name="instituciones" value="idInstitucion" label="abreviatura" />
				</html:select>
				
				<logic:notEmpty name="InformeForm" property="instituciones">
					<logic:iterate name="InformeForm" property="instituciones" id="institucion" indexId="index">
						<input type="hidden" name="institucionId_${index}" id="institucionId_${index}" value="${institucion.idInstitucion}">
					</logic:iterate>
				</logic:notEmpty>					
			</div>
			<div class="colIzq col120px labelText">
				<siga:Idioma key="administracion.informes.literal.descripcion"/> (*)
			</div>
			<div class="col300px labelTextValue">
				<html:textarea name="InformeFormEdicion" property="descripcion" styleId="descripcion"
					onchange="cuenta(this,1000)" onkeydown="cuenta(this,1000);"
					style="overflow-y:auto; overflow-x:hidden; width:100%; height:80px; resize:none;"
					styleClass="<%=estiloTexto%>" readonly="false"/>
			</div>	
		</div>
		
				
		
		
		
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="Configuración informe">
		<div class="colIzq col220px labelText">
			<siga:Idioma key="administracion.informes.literal.nombreFichGenerado"/> (*)
		</div>
		<div class="col180px labelTextValue"  title="<siga:Idioma key='administracion.informes.mensaje.aviso.caracteresAlfaNumericos'/>">
			<html:text name="InformeFormEdicion" styleId="nombreSalida" property="nombreSalida" styleClass="<%=estiloTexto%>"/>
		</div>
		<div class="col180px labelText">
			<siga:Idioma key="administracion.informes.literal.preseleccionado"/> (*)
		</div>
		<div class="col90px labelTextValue">
			<html:select property="preseleccionado" styleId="preseleccionado" name="InformeFormEdicion" style="width:50" styleClass="<%=estiloCombo%>">
				<html:option value="S"><siga:Idioma key="general.yes"/>	</html:option>
				<html:option value="N"><siga:Idioma key="general.no"/></html:option>
			</html:select>	
		</div>
		<div class="col90px labelText">
			<siga:Idioma key="administracion.informes.literal.visible"/> (*)
		</div>
		<div class="col90px labelTextValue">
			<html:select property="visible" name="InformeFormEdicion" styleId="visible" styleClass="<%=estiloCombo%>">
						<html:option value="S"><siga:Idioma key="general.yes"/></html:option>
						<html:option value="N"><siga:Idioma key="general.no"/></html:option>
					</html:select>	
		</div>
		
		<div>
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.generarinformesindireccion"/> (*)
			</div>
			<div class="col180px labelTextValue">
				<html:select property="generarInformeSinDireccion" styleId="generarInformeSinDireccion" name="InformeFormEdicion"  styleClass="<%=estiloCombo%>" >
					<html:option value="S"><siga:Idioma key="general.yes"/>
					</html:option>
					<html:option value="N"><siga:Idioma key="general.no"/></html:option>
				</html:select>		
			</div>
		</div>	
		<div class="col180px labelText divOrden">
			<siga:Idioma key="administracion.informes.literal.orden"/> (*)
		</div>
		<div class="col90px labelTextValue divOrden">
			<html:text name="InformeFormEdicion" property="orden" styleId="orden" styleClass="<%=estiloTexto%>" style="width:100%" onkeypress="return soloDigitos(event)"/>
		</div>
		<div class="col90px labelText" id="literalFormatoTD">
			<siga:Idioma key="administracion.informes.literal.formato"/> (*)
		</div>
		<div class="col90px labelTextValue" id="formatoTD">
			<html:select name="InformeFormEdicion"  property="tipoFormato" styleId="tipoFormato" styleClass="<%=estiloCombo%>">
				<html:option value="-1"><siga:Idioma key="general.combo.seleccionar" />	</html:option>
				<html:option value="W"><siga:Idioma key="administracion.informes.formato.word"/></html:option>
				<html:option value="E"><siga:Idioma key="administracion.informes.formato.excel"/></html:option>
				<html:option value="P"><siga:Idioma key="administracion.informes.formato.pdf"/></html:option>
			</html:select>
		</div>
			
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="Configuración Envío">
		
		<div class="col50">
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.tipoEnviosPermitidos" /> (*)
			</div>
			<div class="col220px labelTextValue">
				<%
				String tagSelectComboTipoEnvio = "getTiposEnvio";
				if ("cmbTipoEnviosInst".equals(comboTipoEnvio)){
					tagSelectComboTipoEnvio = "getTiposEnvioInst";
				} else if ("cmbTipoEnviosInstNotTelematico".equals(comboTipoEnvio)){
					tagSelectComboTipoEnvio = "getTiposEnvioInstNotTelematico";
				} else if ("cmbTipoEnviosInstSmsNotTelematico".equals(comboTipoEnvio)){
					tagSelectComboTipoEnvio = "getTiposEnvioInstSmsNotTelematico";
				} else if ("cmbTipoEnviosSoloSms".equals(comboTipoEnvio)){
					tagSelectComboTipoEnvio = "getTiposEnviosSoloSms";
				}
				ArrayList<String> tagSelectidTipoEnvioSeleccionado = new ArrayList<String>();
				Iterator<String> iteraIdsTipoEnvioSelect = idTipoEnvioSeleccionado.iterator();
				while (iteraIdsTipoEnvioSelect.hasNext()){
					String sIdTipoEnvioSeleccionado = iteraIdsTipoEnvioSelect.next();
					if (sIdTipoEnvioSeleccionado != null && sIdTipoEnvioSeleccionado.indexOf(",") >= 0){
						String[] idDescompuesto = sIdTipoEnvioSeleccionado.split(",");
						tagSelectidTipoEnvioSeleccionado.add(idDescompuesto[idDescompuesto.length - 1]);
					}
				}
				%>
				<siga:Select id="comboTipoEnvioPermitidos"
					queryId="<%=tagSelectComboTipoEnvio%>"
					selectedIds="<%=tagSelectidTipoEnvioSeleccionado%>"
					multiple="true" 
					lines="6"/>
			</div>
			
		</div>
		
		<div class="col50">
		
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.tipoEnvioDefecto" />
			</div>
			<div class="col220px labelTextValue">
				<select id="idTipoEnvioDefecto" onchange="onChangeTipoenvio();" style="width:150px;" class="<%=estiloCombo%>">
					<option  value=""></option>
					<c:forEach items="${tipoEnviosBeans}" var="tipoEnvio">
						<c:set var="envioSeleccionado" value="" />
						<c:if test="${idTipoEnvioDef==tipoEnvio.idTipoEnvios}">
							<c:set var="envioSeleccionado" value="selected" />
						</c:if>
						<option ${envioSeleccionado} value="${tipoEnvio.idTipoEnvios}" ><c:out value="${tipoEnvio.nombre}"/> </option>
					</c:forEach>
				</select>
			</div>
			
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.destinatarios.enviarA"/> (*)
			</div>
			<div class="col180px labelTextValue">
				<html:select property="destinatarios" styleId="destinatarios" name="InformeFormEdicion"  styleClass="<%=estiloCombo%>" >
					<html:option value="C"><siga:Idioma key="administracion.informes.destinatarios.colegiados"/></html:option>
					<html:option value="S"><siga:Idioma key="administracion.informes.destinatarios.solicitantes"/></html:option>
					<html:option value="P"><siga:Idioma key="administracion.informes.destinatarios.procurador"/></html:option>
					<html:option value="J"><siga:Idioma key="administracion.informes.destinatarios.juzgado"/></html:option>
					<html:option value="X"><siga:Idioma key="administracion.informes.destinatarios.contrarios"/></html:option>							
				</html:select>	
			</div>
			
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.solicitantes"/> (*)
			</div>
			<div class="col120px labelTextValue">
				<html:select name="InformeFormEdicion" styleId="ASolicitantes" property="ASolicitantes"  styleClass="<%=estiloCombo%>" >
					<html:option value="S"><siga:Idioma key="general.yes"/></html:option>
					<html:option value="N"><siga:Idioma key="general.no"/></html:option>
				</html:select>	
			</div>
			
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.desdoblarcontrarios"/> (*)
			</div>
			<div class="col120px labelTextValue">
				<html:select name="InformeFormEdicion" styleId="aContrarios" property="aContrarios" styleClass="<%=estiloCombo%>">
					<html:option value="S"><siga:Idioma key="general.yes"/></html:option>
					<html:option value="N"><siga:Idioma key="general.no"/></html:option>
				</html:select>	
			</div>
		</div>
			
		
		<div>
			<div class="colIzq col220px labelText">
				<siga:Idioma key="administracion.informes.literal.plantillaEnvioDefecto" />
			</div>
			<div class="col220px labelTextValue">
				<select id="idPlantillaEnvioDefecto" class="<%=estiloCombo%>" style="width:400px;">									
					<option value="${idPlantillaEnvioDef}"><c:out value="${idPlantillaEnvioDef}" /></option>									
				</select>
				<script type="text/javascript">
					inicioPlantillasEnvio();
				</script>	
			</div>
		</div>
			
		
		<c:choose>
			<c:when test="${mostrarTipoIntercambio == '1'}">
			<div>
				<div class="colIzq col220px labelText">
						<siga:Idioma key="administracion.informes.literal.tipointercambio" />
				</div>
				<div class="col220px labelTextValue">
						<select id="idTipoIntercambioTelematico" style="width:400px;" class="<%=estiloCombo%>">
							<option value="${idTipoIntercambioTelem}"><c:out value="${idTipoIntercambioTelem}" /></option>
						</select>
						<script type="text/javascript">
							inicioTiposIntercambioTelematico();
						</script>								
				</div>
			</div>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="idTipoIntercambioTelematico">								
			</c:otherwise>
		</c:choose>
					
		</siga:ConjCampos>
		
		
			
			
			<br>
			

			<logic:equal name="InformeForm" property="modo" value="insertar">
				<div id="textomod" class="labelTextValue">
					<td colspan = "6">
						<b>(*)&nbsp;<siga:Idioma key="administracion.informes.literal.avisoAltaPlantillas" /></b>
					</td>
				</div>
			</logic:equal>

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
			<siga:ConjBotonesAccion botones="V,G,R"/>
		</c:when>
		<c:otherwise>
			<siga:ConjBotonesAccion botones="V"/>
		</c:otherwise>
	</c:choose>

	<iframe name="submitArea"
		src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		style="display: none">
	</iframe>	

	<script type="text/javascript">
		jQuery(document).ready(function(){
			if(document.InformeFormEdicion.idTipoInforme.value!='CON' && document.InformeFormEdicion.idTipoInforme.value!='REJG'){
				jQuery("#literalFormatoTD").html("");
				jQuery("#formatoTD").html("");
			}
		});
				
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
			if(document.InformeFormEdicion.descripcion.value==''){
				document.InformeFormEdicion.descripcion.value=document.InformeFormEdicion.alias.value;
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
			document.InformeFormEdicion.directorio.value = directorioTipoInforme;
			if (document.getElementById("tipoFormato") != undefined){
				if(claseTipoInforme=='P'||claseTipoInforme=='C'||document.InformeFormEdicion.idTipoInforme.value=='REJG'){
					campoEscritura('#tipoFormato');
				}else{
					campoSoloLectura('#tipoFormato');
				}
			}
			if(document.InformeFormEdicion.idTipoInforme.value=='CON'){
				gestionarDatosConsultas();
			}
		}
		
		function inicio() {			

			campoSoloLectura('#directorio');
			if (document.getElementById("tipoFormato") != undefined){
				if(document.InformeForm.claseTipoInforme.value=='P'||document.InformeForm.claseTipoInforme.value=='C'||document.InformeFormEdicion.idTipoInforme.value=='REJG'){
					campoEscritura('#tipoFormato');
				}else{
					campoSoloLectura('#tipoFormato');	
				}
			}
			
			if(document.InformeForm.modo.value=='modificar'){
				// No se pueden modificar la institucion y tipo de informe 
				campoSoloLectura('#idInstitucion');
				campoSoloLectura('#idTipoInforme');
				if (${intercambioTelematico} == '1'){
					soloLectura();
					document.getElementById ("titulo").innerHTML = '<siga:Idioma key='administracion.informes.consulta.titulo'  />';
					// En envíos telematicos solo se podra editar algunos campos
					campoEscritura('#idTipoEnvioDefecto');
					campoEscritura('#idPlantillaEnvioDefecto');
				}	

				campoSoloLectura('#idTipoIntercambioTelematico');
		
				// Para los informes por defecto no tiene sentido el tipo envio y plantilla por defecto
				if(document.InformeFormEdicion.idInstitucion.value == '0'){
					campoSoloLectura("#idTipoEnvioDefecto");
					campoSoloLectura("#idPlantillaEnvioDefecto");
				}
				
				// Solo en cgae se puede configurar el envio telematico
				if(document.getElementById("location").value=='2000'){
					campoEscritura('#idTipoIntercambioTelematico');
				}
				
			}else if(document.InformeForm.modo.value=='consultar'){
				// EN modo consulta no se puede editar nada
				soloLectura();
				document.getElementById ("titulo").innerHTML = '<siga:Idioma key='administracion.informes.consulta.titulo'  />';

			}else if(document.InformeForm.modo.value=='insertar'){
				document.getElementById ("titulo").innerHTML = '<siga:Idioma key='administracion.informes.nuevo.titulo'  />';
				if(document.getElementById("location").value=='2000'){
					campoEscritura('#idInstitucion');
					campoEscritura('#idPlantilla');
					campoEscritura('#idTipoInforme');
					campoEscritura('#idTipoIntercambioTelematico');
				}else{
					//Mostramos la propia institcion(0 seleciona , 1 pordefecto, 2 intitucion)
					document.getElementById("idInstitucion").selectedIndex = "2";
					campoSoloLectura("#idInstitucion");
					campoSoloLectura('#idTipoIntercambioTelematico');
					campoEscritura("#idPlantilla");					
					campoEscritura("#idTipoInforme");
				}
				document.InformeFormEdicion.visible.value="S";
			}
			if(document.InformeFormEdicion.idTipoInforme.value=='CON'){
				gestionarDatosConsultas();
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
		
		// Asociada al boton Guardar
		function accionGuardar() {			
			//alert('a ver?'+document.getElementById("comboTipoEnvioPermitidos").options.length);
			var tiposEnvioSeleccionados = "";
			if(document.InformeFormEdicion.idTipoInforme.value!='CON'){
				var tiposEnvio = document.getElementById("comboTipoEnvioPermitidos").options;
				
				for ( var i = 0; i < tiposEnvio.length; i++) {
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
				
			} else {				
				if(document.InformeFormEdicion.tipoFormato.value=='-1' || document.InformeFormEdicion.tipoFormato.value==''){
					error = "<siga:Idioma key='errors.required' arg0='administracion.informes.literal.formato' />";
					alert(error);
					fin();		
					return false;
				}
				
				if(document.getElementById("alias").value.length>50){
					document.getElementById("alias").value = document.getElementById("alias").value.substring(0,50);
				}
				campoEscritura("#alias");						
			}	
			

			campoEscritura('#idTiposEnvio');
			campoEscritura('#idTipoIntercambioTelem');
			campoEscritura('#idTipoEnvio');
			campoEscritura('#idPlantillaEnvio');			
			
			if (document.InformeForm.modo.value=='insertar'){
				indiceTipoInforme = document.getElementById("idTipoInforme").selectedIndex;
				idClaseTipoInforme = 'claseTipoInforme_'+indiceTipoInforme;
				if (document.getElementById(idClaseTipoInforme) != undefined)
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
			
			if(document.InformeFormEdicion.tipoFormato != undefined && document.InformeFormEdicion.tipoFormato.value=='-1')
				document.InformeFormEdicion.tipoFormato.value = '';
			
			if(document.InformeFormEdicion.preseleccionado.value=='-1')
				document.InformeFormEdicion.preseleccionado.value = '';
			
			if(document.InformeFormEdicion.ASolicitantes.value=='-1')
				document.InformeFormEdicion.ASolicitantes.value = '';
			
			if(document.InformeFormEdicion.visible.value=='-1')
				document.InformeFormEdicion.visible.value = '';
		
			//Validación del campo Nombre Fichero Generado
			//Sólo se permite carac. Alfanumércos y guión bajo y alto
			
			if(!(validarAlfaNumericoYGuiones(document.InformeFormEdicion.nombreSalida.value)&&validarAlfaNumericoYGuiones(document.InformeFormEdicion.nombreFisico.value)))
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
			if(document.InformeFormEdicion.idTipoInforme.value=='CON'){ 				
				document.InformeFormEdicion.preseleccionado.value = 'S';
				document.InformeFormEdicion.ASolicitantes.value = 'S';
				document.InformeFormEdicion.aContrarios.value = 'N';				
			}
			
			document.InformeFormEdicion.modo.value = document.InformeForm.modo.value; 
		
			if (validateInformeFormEdicion(document.InformeFormEdicion)){
				if (document.InformeForm.modo.value=='insertar'){
					document.InformeFormEdicion.target="mainWorkArea";
				}else{
					document.InformeFormEdicion.target="submitArea";
				}
				document.InformeFormEdicion.submit();
				jQuery('#textomod').hide();
				document.InformeForm.modo.value="modificar";
				if(formatearFormulario(document.InformeFormEdicion)){
					alert("<siga:Idioma key='administracion.informes.mensaje.aviso.sustituyeEspacios'/>");
				}
			} else {
			 	fin();
		 	}
			
			campoSoloLectura('#idPlantilla');
			if(document.InformeFormEdicion.idTipoInforme.value=='CON'){
				campoSoloLectura('#alias');
			}	
			campoSoloLectura('#idTiposEnvio');
			campoSoloLectura('#idTipoIntercambioTelem');
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
			cargarListadoArchivos();
		}
		
		function gestionarDatosConsultas() 
		{		
			jQuery('#idTipoInforme').attr('disabled','disabled'); //document.getElementsByName("idTipoInforme")[0].disabled =  'disabled';
			jQuery('#alias').attr('disabled','disabled'); //document.getElementById("alias").disabled =  'disabled';
			document.getElementById("ocultarSolicitantes").style.display =  "none";
			//document.getElementById("ocultarOrden").style.display =  "none";
			document.getElementById("ocultarLabelPreseleccionado").style.display =  "none";
			document.getElementById("ocultarSelectPreseleccionado").style.display =  "none";
			document.getElementById("trEnvios").style.display =  "none";
		}
		
		function accionComboTipoEnvio(index) {
			var envioDefectoSeleccionado = document.getElementById("idTipoEnvioDefecto").value;
			document.getElementById("idTipoEnvioDefecto").options.length = 0;
		
			var findDefecto = false;
			jQuery("#idTipoEnvioDefecto").html("<option  value=''>&nbsp;</option>");
			jQuery("#comboTipoEnvioPermitidos").find("option").each(function(){
				if (jQuery(this).is(":selected")){
					var idTipoEnvio = jQuery(this).val();
					if(!findDefecto && idTipoEnvio==envioDefectoSeleccionado){
						findDefecto = true;
					}
					var optionHtml = "<option  value='"+idTipoEnvio+"'";
					if (envioDefectoSeleccionado == idTipoEnvio){
						optionHtml += " selected ";
					}
					optionHtml += ">"+jQuery(this).text()+"</option>";
					jQuery("#idTipoEnvioDefecto").append(optionHtml);
				}
			});
			
			if(!findDefecto){
				onChangeTipoenvio();
			}
		
		
			if(index != -1)
				onChangeTipoIntercambio();
		
		}
		
		// Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			refrescarLocal();
		}
		
		jQuery(document).ready(function () {
			// Quitamos la primera opcion de la lista de envíos
			jQuery('#comboTipoEnvioPermitidos option[value=""]').remove();
			// Añadimos un listener para el cambio de tipo de envio 
			jQuery("#comboTipoEnvioPermitidos").on("change", function(){accionComboTipoEnvio();});
			accionComboTipoEnvio(-1);
		});	
		
		// Asociada al boton Volver
		function accionVolver(){		
			document.InformeForm.target="mainWorkArea";
			document.InformeForm.modo.value = "volver";
			document.InformeForm.submit();
		}
		
		function refrescarLocal(){
			if (document.InformeForm.modo.value=='insertar'){
				document.InformeFormEdicion.reset();
			}else{
				document.InformeFormEdicion.modo.value = "editar";
				document.InformeFormEdicion.target = "mainWorkArea";
				document.InformeFormEdicion.submit();
			}
			//actualizaEstilos();
		}
		
		function soloLectura(){

			jQuery.fn.reverse = [].reverse;
			
			jQuery('select').not('#comboTipoEnvioPermitidos').not("#idPlantillaEnvioDefecto").each(function(){
				jQuery(this).after(jQuery(this).find("option:selected").text());
				jQuery(this).hide();
			});
			
			jQuery('input').attr('readOnly','readOnly');
			jQuery('textArea').attr('readOnly','readOnly');
			jQuery('#comboTipoEnvioPermitidos').hide();
			jQuery('#comboTipoEnvioPermitidos option').reverse().each(function() {
				if(jQuery(this).is(':selected')){
					jQuery('#comboTipoEnvioPermitidos').after(jQuery(this).text()+'<br>');
					console.log(jQuery(this).text());
				}
			});

			jQuery("#idPlantillaEnvioDefecto").change(function(){
				jQuery("#idPlantillaEnvioDefecto").after(jQuery("#idPlantillaEnvioDefecto").find(":selected").text());
				jQuery("#idPlantillaEnvioDefecto").hide();
			});
			
		}
		
		function campoSoloLectura(campo){
			jQuery(campo).attr('readOnly','readOnly');
			if(jQuery(campo).is('select')){			
				jQuery(campo).after(jQuery(campo).find("option:selected").text());
				jQuery(campo).hide();
			}else{
				jQuery(campo).removeClass('box');
				jQuery(campo).addClass('boxConsulta');
			}
		}
		
		function campoEscritura(campo){
			jQuery(campo).removeAttr('readOnly');
			if(jQuery(campo).is("select")){
				jQuery(campo).next().remove();
				jQuery(campo).show();
				jQuery(campo).removeClass('boxComboConsulta');
				jQuery(campo).addClass('boxCombo');
			}else{
				jQuery(campo).removeClass('boxConsulta');
				jQuery(campo).addClass('box');
			}
		}
		
	</script>
</body>
</html>