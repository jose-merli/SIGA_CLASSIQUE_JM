<!DOCTYPE html>
<html>
<head>
<!-- seleccionInformes.jsp -->

<!-- 
	 Permite mostrar/editar datos sobre lols precios asociados a los servicios
	 VERSIONES:
	 miguel.villegas 7-2-2005 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- JSP -->

<!-- HEAD -->
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<script>
	jQuery.noConflict();
	
	function inicioPlantillasEnvio(index) {
		var comboTiposEnvio = document.getElementById('idTipoEnvio_'+index);
		var comboPlantilla = document.getElementById('idPlantillaEnvio_'+index);
		var idPlantillaEnvioDefecto = document.getElementById('idPlantillaEnvioDefecto_'+index).value;
		// alert("comboPlantilla"+comboPlantilla.id);
		
		if(comboPlantilla.options.length>0&&comboTiposEnvio.value!=''){
		
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
	           type: "POST",
	           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
	           async: false,
	           data: "idTipoEnvio="+comboTiposEnvio.value+"&idPlantillaEnvioDefecto="+idPlantillaEnvioDefecto,
	           contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	           
	           dataType: "json",
	           success:  function(json) {
           			var plantillasEnvio = json.plantillasEnvio;
           			var optionComboPlantilla = comboPlantilla.options;
           			//Si tiene opciones el valor es el que iene que estar seleccioando
           			if(optionComboPlantilla){
           				var valueComboPlantilla = optionComboPlantilla[0].value;
           				//vaciamos la listas
						optionComboPlantilla.length = 0;
						jQuery("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
						
						jQuery.each(plantillasEnvio, function(i,item2){
							var selected = "";
	           				if(valueComboPlantilla!='' && valueComboPlantilla==item2.idPlantillaEnvios){
	           					selected = "selected";
           					}
	                        jQuery("#idPlantillaEnvio_"+index).append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
	                      //ATENCION EL i+1 el porque tenemos la linea vacia al principio
	                        document.getElementById("idPlantillaEnvio_"+index).options[i+1].setAttribute("acuseRecibo", item2.acuseRecibo);
	                        
	                    });
           			}
		           			
	           	},
	           	error: function(xml,msg){
	        	   alert("Error: "+msg);//$("span#ap").text(" Error");
	           	}
        	});
	
		} else {
			jQuery("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
		}
	}
	
	function onChangeTipoenvio(index) {
		var comboTiposEnvio = document.getElementById('idTipoEnvio_'+index);
		var comboPlantilla = document.getElementById('idPlantillaEnvio_'+index);
		var idPlantillaEnvioDefecto = document.getElementById('idPlantillaEnvioDefecto_'+index).value;
		var idTipoEnvioDefecto = document.getElementById('idTipoEnvioDefecto_'+index).value;
		
		if(comboTiposEnvio.value!=''){			
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
	           type: "POST",
	           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
	           data: "idTipoEnvio="+comboTiposEnvio.value,
	           dataType: "json",
	           success:  function(json) {
           			var plantillasEnvio = json.plantillasEnvio;
           			var optionComboPlantilla = comboPlantilla.options;
           			
           			optionComboPlantilla.length = 0;
           			jQuery("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
           			
           			jQuery.each(plantillasEnvio, function(i,item2){
           				var selected = "";	
          				if((idPlantillaEnvioDefecto!=null)&&(idPlantillaEnvioDefecto!="-1")){
	           				if(idPlantillaEnvioDefecto==item2.idPlantillaEnvios&&comboTiposEnvio.value ==idTipoEnvioDefecto){
	           					selected = "selected";
	          					}
          				}
                       jQuery("#idPlantillaEnvio_"+index).append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
                       //ATENCION EL i+1 el porque tenemos la linea vacia al principio
                       document.getElementById("idPlantillaEnvio_"+index).options[i+1].setAttribute("acuseRecibo", item2.acuseRecibo);
                        
                    });
           			
		           			
	           },
	           error: function(xml,msg){
	        	   alert("Error: "+msg);//$("span#ap").text(" Error");
	           }
	        });
		} else {
			comboPlantilla.options.length = 0;
		}
	}
</script>
	
<body >
	<bean:define id="asunto" name="asunto"	scope="request" />
	<bean:define id="fecha" name="fecha"	scope="request" />
	<bean:define id="informeForms" name="informeForms"	type="java.util.Collection" scope="request" />
	
	<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
		<html:hidden property="modo" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="idInforme" />
		<html:hidden property="idTipoInforme" />
		<html:hidden property="datosInforme" />
		<html:hidden property="descargar" value= "1"/>
		<html:hidden property="enviar"/>
		<html:hidden property="tipoPersonas" />
	</html:form>
	
	<c:if test="${InformesGenericosForm.enviar=='1' }">
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="submitArea">
			<html:hidden property="modo" value="insertarEnvioGenerico" />
			<html:hidden property="idTipoInforme" />
			<html:hidden property="datosInforme" value="" />
			<html:hidden property="datosEnvios" />
		
			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" height="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="informes.seleccionPlantillas.titulo" />
					</td>
				</tr>
			</table>
		
			<table>
				<tr>
					<td width="25%"> </td>
					<td width="75%"> </td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="informes.genericos.comunicacion" />
					</td>
					<td class="labelTextValor">
						<html:text name="DefinirEnviosForm" property="nombre" value="${asunto}" size="50" maxlength="50" styleClass="box" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="envios.definir.literal.fechaprogramada" />
					</td>
					<td class="labelTextValor">
						<siga:Fecha nombreCampo="fechaProgramada" valorInicial="${fecha}" posicionX="30"  posicionY="30"></siga:Fecha>
					</td>
				</tr>
			</table>
		</html:form>
	</c:if>
	
	<c:set var="nombreCol" value="" />
	<c:set var="tamanoCol" value="" />

	<c:choose> 
		<c:when test="${InformesGenericosForm.enviar=='1'}">
			<c:set var="nombreCol" value="infomes.seleccionPlantillas.literal.sel,Informe,Dest.,envios.definir.literal.tipoenvio, envios.plantillas.literal.plantilla" />
			<c:set var="tamanoCol" value="5,40,5,20,30" />
		</c:when>
		<c:otherwise>
			<c:set var="nombreCol" value="infomes.seleccionPlantillas.literal.sel,Informe,administracion.informes.literal.solicitantes" />
			<c:set var="tamanoCol" value="5,75,20" />
		</c:otherwise>
	</c:choose>

	<siga:Table 
		name="tablaDatos" 
		border="1"
		columnNames="${nombreCol}"
		columnSizes="${tamanoCol}" 
		modal="G"
		modalScroll="true">
		
		<c:forEach items="${informeForms}" var="informe" varStatus="status">
			<c:set var="preseleccionado" value="" />
			<c:if test="${informe.preseleccionado=='S'}">
				<c:set var="preseleccionado" value="checked" />		
			</c:if>
			<siga:FilaConIconos fila='${status.count}' botones=""
				visibleConsulta="false" visibleEdicion="false" 
				visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">
				
				<td>
					<input type="hidden" name="indice" id="${status.count}"/>
					<c:if test="${InformesGenericosForm.enviar=='1'}">	
						<input type="checkbox"	id="${status.count}_${informe.idPlantilla}_${informe.idInstitucion}_${informe.idTipoInforme}" name="chkPL" ${preseleccionado} onclick='cargarComboTipoEnvios(${status.count});onclickchkPL();' />
					</c:if>
					<c:if test="${InformesGenericosForm.enviar!='1'}">	
						<input type="checkbox"	id="${status.count}_${informe.idPlantilla}_${informe.idInstitucion}_${informe.idTipoInforme}" name="chkPL" ${preseleccionado} onclick='onclickchkPL()' />
					</c:if>
				</td>
				<td>
					<label for="${status.count}_${informe.idPlantilla}_${informe.idInstitucion}_${informe.idTipoInforme}"><c:out value="${informe.descripcion}" /></label>
				</td>
				<c:choose>
					<c:when test="${InformesGenericosForm.enviar=='1'}">	
						<td style="text-align: center">
							<c:out value="${informe.destinatarios}" />
						</td>				
						<td>
							<c:set var="disabledCombo" value="" />
							<c:if test="${informe.preseleccionado!='S'}">
								<c:set var="disabledCombo" value="disabled" />
							</c:if>
							<select style="width:132px;" name="tipoEnvio" id="idTipoEnvio_${status.count}" ${disabledCombo} onchange="onChangeTipoenvio(${status.count});actCheck(${status.count})" >
								<option value=""></option>
								<c:set var="idPlantillaDefecto" value="" />
								<c:set var="idTipoEnvioDefecto" value="" />
								<c:forEach items="${informe.tiposEnvioPermitidos}" var="tipoEnvio">
									<c:set var="envioSeleccionado" value="" />
									<c:if test="${tipoEnvio.defecto=='1'}">
										<c:set var="idTipoEnvioDefecto" value="${tipoEnvio.idTipoEnvios}" />
										<c:set var="idPlantillaDefecto" value="${tipoEnvio.idPlantillaDefecto}" />
										<c:set var="envioSeleccionado" value="selected" />
									</c:if>
									<option ${envioSeleccionado} value="${tipoEnvio.idTipoEnvios}" ><c:out value="${tipoEnvio.nombre}"/> </option>
								</c:forEach>
							</select>
							<input type="hidden" id="idTipoEnvioDefecto_${status.count}"  value="${idTipoEnvioDefecto}" />
							<input type="hidden" id="idPlantillaEnvioDefecto_${status.count}"  value="${idPlantillaDefecto}" />
						</td>
				
						<td>
							<select style="width:202px;" id="idPlantillaEnvio_${status.count}" ${disabledCombo}>
								<option value="${idPlantillaDefecto}"><c:out value="${idPlantillaDefecto}" /></option>
							</select>
						</td>
					
						
					</c:when>
					<c:otherwise>
						<td style="text-align: center"><c:out value="${informe.ASolicitantes}" /></td>
					</c:otherwise>
				</c:choose>
			</siga:FilaConIconos>
		</c:forEach>
	</siga:Table>
	
	<c:choose>	
		<c:when test="${InformesGenericosForm.enviar =='1'}">
			<table class="botonesDetalle" id="idBotonesAccion"  align="center">
			<tr>
				<td  style="width:900px;">
				&nbsp;
				</td>
				<td class="tdBotones">
				<input type="button" alt="Descargar"  id="botonDescargar" onclick="return accionDownload();" class="button" name="idButton" value="Descargar">
				</td>
				<td class="tdBotones">
				<input type="button" alt="Enviar"  id="botonEnviar" onclick="return accionEnviar();" class="button" name="idButton" value="Enviar">
				</td>
				<td class="tdBotones">
				<input type="button" alt="Cerrar"  id="idButton" onclick="return accionCerrar();" class="button" name="idButton" value="Cerrar">
				</td>
			</tr>
			</table>
		</c:when>
		<c:otherwise>
			<table class="botonesDetalle" id="idBotonesAccion"  align="center">
				<tr>
				<td  style="width:900px;">
				&nbsp;
				</td>
				<td class="tdBotones">
				<input type="button" alt="Descargar"  id="botonDescargar" onclick="return accionDownload();" class="button" name="idButton" value="Descargar">
				</td>
				
				<td class="tdBotones">
				<input type="button" alt="Cerrar"  id="idButton" onclick="return accionCerrar();" class="button" name="idButton" value="Cerrar">
				</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
	<!-- FIN: CAMPOS -->
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>

<script language="JavaScript">
	
	function actCheck(index){
		var idTipoEnvio = document.getElementById("idTipoEnvio_"+index).value;
		if(idTipoEnvio==''){
			var oCheck = document.getElementsByName("chkPL");
			oCheck[index-1].checked='';
			document.getElementById("idTipoEnvio_"+index).disabled = true;
		}
	}
	
	function accionDownload() {	
		sub();
		var oCheck = document.getElementsByName("chkPL");
		var idsInformes = "";
		for (i = 0; i < oCheck.length; i++) {
			if (oCheck[i].checked) {
				ids = oCheck[i].id.split("_");
				idsInformes += ids[1]+","+ids[2]+ "##";
				//Esto se hace para cuando hay mas de un informe. Como solo dejamos descergar cuando ha seleccioando un unico informe
				//seteamos el idtipoinforme de ualquiera de ellos al formulario global
				document.InformesGenericosForm.idTipoInforme.value = ids[3];
			}
		}
		
		if (idsInformes.length > 2)
			idsInformes = idsInformes.substring(0, idsInformes.length - 2);
		
	    if(idsInformes==""){
	    	alert("Debe seleccionar al menos un informe");
	    	fin();

	    	return false;
	    }
	    
	    
	    document.InformesGenericosForm.enviar.value = '0';
		document.InformesGenericosForm.idInforme.value = idsInformes;
		document.InformesGenericosForm.modo.value = "download";
		document.InformesGenericosForm.submit();
	}


	// Asociada al boton Cerrar
	function accionCerrar() {			
		window.top.close();
	}

	function accionEnviar() {
		sub();
		var oCheck = document.getElementsByName("chkPL");
		var idsInformes = "";
		var error = "";
		var haSeleccionadoInformes = false;
		
		if (DefinirEnviosForm.nombre.value=='') {
			error = "<siga:Idioma key='errors.required' arg0='informes.genericos.comunicacion'/>\n";
		}
		
		if (DefinirEnviosForm.nombre.value.lenght > 50) {
			error += "<siga:Idioma key='errors.required' arg0='informes.genericos.comunicacion'/>\n";
		}
		
		if (DefinirEnviosForm.fechaProgramada.value == "") {   			
			error += "<siga:Idioma key='envios.definir.literal.fechaprogramada'/> <siga:Idioma key='messages.campoObligatorio.error'/>\n";
		}		
		
		for (i = 0; i < oCheck.length; i++) {
			if (oCheck[i].checked) {
				haSeleccionadoInformes = true;
				
				ids = oCheck[i].id.split("_");
				//Esto se hace para cuando hay mas de un informe. Como solo dejamos descergar cuando ha seleccioando un unico informe
				//seteamos el idtipoinforme de ualquiera de ellos al formulario global
				document.InformesGenericosForm.idTipoInforme.value = ids[3];
				
				var idTipoEnvio = document.getElementById("idTipoEnvio_"+ids[0]).value;
				if(idTipoEnvio==''){
					error += "Debe configurar correctamente todos los envios marcados seleccionando tipo de envio y plantilla de envio\n";
					break;
				}
				//meter validacion si esto es vacio
				var objPlantillaEnvio = document.getElementById("idPlantillaEnvio_"+ids[0]);
				var idPlantillaEnvio = objPlantillaEnvio.value;
				if(idPlantillaEnvio==''){
					error += "Debe configurar correctamente todos los envios marcados seleccionando tipo de envio y plantilla de envio\n";
					break;
				}
				//var acuseRecibo = objPlantillaEnvio.options[objPlantillaEnvio.selectedIndex].acuseRecibo;
				var acuseRecibo = jQuery('#idPlantillaEnvio_'+ids[0]+' option:selected').attr('acuseRecibo');
				//meter validacion si esto es vacio
				idsInformes += idTipoEnvio+","+idPlantillaEnvio+","+ids[1]+","+ids[2]+","+acuseRecibo+"##";									
			}
		}
		
		if (idsInformes.length > 2)
			idsInformes = idsInformes.substring(0, idsInformes.length - 2);
		
		if (!haSeleccionadoInformes) {
			error += "Debe seleccionar al menos un informe\n";
	    }		
		
		if (error!='') {
			alert(error);
	    	fin();
	    	return false;
		}
		
		
	    document.DefinirEnviosForm.datosInforme.value = document.InformesGenericosForm.datosInforme.value ;
	    document.DefinirEnviosForm.idTipoInforme.value = document.InformesGenericosForm.idTipoInforme.value ;
	    document.DefinirEnviosForm.datosEnvios.value = idsInformes;
	   	document.DefinirEnviosForm.submit();
	}
	
	function descargarUnica(){
		if(document.InformesGenericosForm.enviar.value=='0'){
			var oCheck = document.getElementsByName("chkPL");
			var idsInformes = "";
			oCheck[0].checked='checked';
			accionDownload();
		}
	}
	function iniciarPlantillasEnvio(){
		var indices = document.getElementsByName("indice");
		for (i = 0; i < indices.length; i++) {
			
			inicioPlantillasEnvio(indices[i].id);	
			
		}
		
	}
	
	function cargarComboTipoEnvios(index){
		var oCheck = document.getElementsByName("chkPL");
		var comboTiposEnvio = document.getElementById('idTipoEnvio_'+index);
 		if (oCheck[index-1].checked){
			ids = oCheck[index-1].id.split("_");
			document.getElementById("idTipoEnvio_"+index).disabled = false;
			document.getElementById("idPlantillaEnvio_"+index).disabled = false;
			//Recupero los datos de los combos del registro
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
		           type: "POST",
		           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryTiposEnvioPermitido",
		           data: "idPlantilla="+ids[1]+"&idInstitucion="+ids[2],
				   contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		           dataType: "json",
		           success:  function(json) {
	           			var tiposEnvioPermitidos = json.tiposEnvioPermitidos;
	           			var optionComboTiposEnvio = comboTiposEnvio.options;
	           			//Si tiene opciones el valor es el que iene que estar seleccioando
	           			if(optionComboTiposEnvio){
	           				var valueComboTiposEnvio = optionComboTiposEnvio[0].value;
	           				//vaciamos la listas
							optionComboTiposEnvio.length = 0;
							jQuery("#idTipoEnvio_"+index).append("<option  value=''>&nbsp;</option>");
	           				jQuery.each(tiposEnvioPermitidos, function(i,item2){
 		           				var selected = "";
								//Si el tipo de envio es el envio por defecto se selecciona su valor
	 		           			if(item2.defecto=="1"){
		           					selected = "selected";
		           					jQuery("#idTipoEnvioDefecto_"+index).val(item2.idTipoEnvios);
		           					if((item2.idPlantillaDefecto!=null)&&(item2.idPlantillaDefecto!=""))
		           						jQuery("#idPlantillaEnvioDefecto_"+index).val(item2.idPlantillaDefecto);
		           					else
		           						jQuery("#idPlantillaEnvioDefecto_"+index).val("");
	           					}
 		           				 		           				
		                        jQuery("#idTipoEnvio_"+index).append("<option "+selected+" value='"+item2.idTipoEnvios+"'>"+item2.nombre+"</option>");
		                        //Se carga la combo de plantillas disponibles
		                        onChangeTipoenvio(index); 		                     	
		                    });
	           			}
			           			
		           	},
		           error: function(xml,msg){
		        	   alert("Error: "+msg);//$("span#ap").text(" Error");
		           }     	
		    });
		}else{
 			var optionComboTiposEnvio = comboTiposEnvio.options;
 			optionComboTiposEnvio.length = 0;
			jQuery("#idTipoEnvio_"+index).append("<option  value=''>&nbsp;</option>");
 			document.getElementById("idTipoEnvio_"+index).disabled = true;
			var comboPlantilla = document.getElementById('idPlantillaEnvio_'+index);
			var optionComboPlantilla = comboPlantilla.options;
			optionComboPlantilla.length = 0;
			jQuery("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
			document.getElementById("idPlantillaEnvio_"+index).disabled = true;
		}	
	}
	
	
	function onclickchkPL() {
		var oCheck = document.getElementsByName("chkPL");
		var idTiposInforme = "";
		var isPrimerRegistro = true;
		var habilitarDescargayEnvio = true;
		var botonesDisabled = '';
		for (i = 0; i < oCheck.length; i++) {
			if (oCheck[i].checked) {
				ids = oCheck[i].id.split("_");
				if(isPrimerRegistro){
					idTiposInforme = ids[3];
					isPrimerRegistro = false;
				}
				if(idTiposInforme!=ids[3]){
					botonesDisabled = 'disabled'
					break;
				}
			}
		}
	 	document.getElementById("botonDescargar").disabled = botonesDisabled;
	 	if(document.getElementById("botonEnviar"))
	 		document.getElementById("botonEnviar").disabled = botonesDisabled;

	}
	
	if(document.getElementById("botonEnviar"))
		iniciarPlantillasEnvio();
	onclickchkPL();
	function refrescarLocal() {}
	
	
	// descargarUnica();
</script>
</html>