<!-- seleccionPlantillasModal.jsp -->
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
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<!-- JSP -->


<html>
<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"
	href='<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>'>
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>

<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery.js'/>"
	type="text/javascript"></script>
</head>
<script>
		function inicioPlantillasEnvio(index) {
				var comboTiposEnvio = document.getElementById('idTipoEnvio_'+index);
				var comboPlantilla = document.getElementById('idPlantillaEnvio_'+index);
				// alert("comboPlantilla"+comboPlantilla.id);
				
				if(comboPlantilla.options.length>0&&comboTiposEnvio.value!=''){
				
				$.ajax({ //Comunicación jQuery hacia JSP  
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
								$("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
		           				$.each(plantillasEnvio, function(i,item2){
			           				var selected = "";
			           				if(valueComboPlantilla!='' && valueComboPlantilla==item2.idPlantillaEnvios){
			           					selected = "selected";
		           					}
			                        $("#idPlantillaEnvio_"+index).append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
			                      //ATENCION EL i+1 el porque tenemos la linea vacia al principio
			                        document.getElementById("idPlantillaEnvio_"+index).options[i+1].setAttribute("acuseRecibo", item2.acuseRecibo);
			                        
			                    });
		           			}
				           			
			           },
			           error: function(xml,msg){
			        	   alert("Error: "+msg);//$("span#ap").text(" Error");
			           }
			        });
			
				}else{
					$("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
					
				}
			
		}
		function onChangeTipoenvio(index) {
			var comboTiposEnvio = document.getElementById('idTipoEnvio_'+index);
			var comboPlantilla = document.getElementById('idPlantillaEnvio_'+index);
			var idPlantillaEnvioDefecto = document.getElementById('idPlantillaEnvioDefecto_'+index).value;
			var idTipoEnvioDefecto = document.getElementById('idTipoEnvioDefecto_'+index).value;
			
			if(comboTiposEnvio.value!=''){			
			$.ajax({ //Comunicación jQuery hacia JSP  
		           type: "POST",
		           url: "/SIGA/ENV_DefinirEnvios.do?modo=getJQueryPlantillasEnvio",
		           data: "idTipoEnvio="+comboTiposEnvio.value,
		           dataType: "json",
		           success:  function(json) {
	           			var plantillasEnvio = json.plantillasEnvio;
	           			var optionComboPlantilla = comboPlantilla.options;
	           			
	           			optionComboPlantilla.length = 0;
	           			$("#idPlantillaEnvio_"+index).append("<option  value=''>&nbsp;</option>");
	           				$.each(plantillasEnvio, function(i,item2){
		           				var selected = "";
		           				if(idPlantillaEnvioDefecto==item2.idPlantillaEnvios&&comboTiposEnvio.value ==idTipoEnvioDefecto){
		           					selected = "selected";
	           					}
		                        $("#idPlantillaEnvio_"+index).append("<option "+selected+" value='"+item2.idPlantillaEnvios+"'>"+item2.nombre+"</option>");
		                        //ATENCION EL i+1 el porque tenemos la linea vacia al principio
		                        document.getElementById("idPlantillaEnvio_"+index).options[i+1].setAttribute("acuseRecibo", item2.acuseRecibo);
		                        
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
		
	</script>
<body>
<bean:define id="asunto" name="asunto"	scope="request" />
<bean:define id="fecha" name="fecha"	scope="request" />
<bean:define id="informeForms" name="informeForms"	type="java.util.Collection" scope="request" />
	<html:form action="/INF_InformesGenericos.do" method="POST"
		target="submitArea">
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
	<html:form action="/ENV_DefinirEnvios.do" method="POST"
		target="submitArea">
		<html:hidden property="modo" value="insertarEnvioGenerico" />
		<html:hidden property="idTipoInforme" />
		<html:hidden property="datosInforme" value="" />
		<html:hidden property="datosEnvios" />
		


		
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos"><siga:Idioma
						key="informes.seleccionPlantillas.titulo" /></td>
			</tr>
		</table>
		
		<table>
			<tr>
				<td width="87" class="labelText">Comunicacion</td>
				<td class="labelTextValor"><html:text name="DefinirEnviosForm"
						property="nombre" value="${asunto}" size="50" maxlength="100" styleClass="box"></html:text>
				</td>

				
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma
						key="envios.definir.literal.fechaprogramada" /></td>
				<td  class="labelTextValor"><html:text name="DefinirEnviosForm"
						property="fechaProgramada" size="10" maxlength="10"
						styleClass="box" readonly="true" value="${fecha}"/> <a href='javascript://'
					onClick="return showCalendarGeneral(fechaProgramada);"><img
						src="<html:rewrite page='/html/imagenes/calendar.gif'/>" border="0"> </a></td>
			</tr>
			<tr>
			<td>
			
			
			</td>
			<td>
			
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

	<siga:TablaCabecerasFijas nombre="tablaDatos" borde="1"
		clase="tableTitle"
		nombreCol="${nombreCol}"
		tamanoCol="${tamanoCol}" alto="100%" modal="G"
		activarFilaSel="true" ajusteBotonera="true">
		
		<c:forEach items="${informeForms}" var="informe" varStatus="status">
			<c:set var="preseleccionado" value="" />
			<c:if test="${informe.preseleccionado=='S'}">
				<c:set var="preseleccionado" value="checked" />
			</c:if>
			<siga:FilaConIconos fila='${status.count}' botones=""
				visibleConsulta="false" visibleEdicion="false" 
				visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">
				<input type="hidden" name="indice" id="${status.count}"/>
				<td><input type="checkbox"	id="${status.count}_${informe.idPlantilla}_${informe.idInstitucion}" name="chkPL" ${preseleccionado} />
				</td>
				<td ><c:out value="${informe.descripcion}" /></td>
				<c:choose>
				
				
				<c:when test="${InformesGenericosForm.enviar=='1'}">	
				<td style="text-align: center"><c:out value="${informe.destinatarios}" /></td>
				
					<td><select style="width:132px;" name="tipoEnvio" id="idTipoEnvio_${status.count}"  onchange="onChangeTipoenvio(${status.count});">
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
				
					<td ><select style="width:202px;" id="idPlantillaEnvio_${status.count}">
							<option value="${idPlantillaDefecto}"><c:out value="${idPlantillaDefecto}" /></option>
					</select>
					</td>
					
					<script>
						inicioPlantillasEnvio(document.getElementsByName("indice")[document.getElementsByName("indice").length-1].id);
					</script>
					</c:when>
					<c:otherwise>
						<td style="text-align: center"><c:out value="${informe.ASolicitantes}" /></td>
					</c:otherwise>
				</c:choose>
			</siga:FilaConIconos>
		</c:forEach>

	</siga:TablaCabecerasFijas>
	<c:choose>
	<c:when test="${InformesGenericosForm.enviar =='1'}">
		<siga:ConjBotonesAccion botones="D,EN,C" ordenar="D,EN,C" modal="P" />
	</c:when>
	<c:otherwise>
	<siga:ConjBotonesAccion botones="D,C" modal="P" />
	</c:otherwise>
	</c:choose>
	
	

	<!-- FIN: CAMPOS -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea"
	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
	style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
<script language="JavaScript">


function accionDownload() 
{	
	sub();
	var oCheck = document.getElementsByName("chkPL");
	var idsInformes = "";
	for (i = 0; i < oCheck.length; i++) {
		if (oCheck[i].checked) {
			ids = oCheck[i].id.split("_");
			idsInformes += ids[1]+","+ids[2]+ "##";	
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


<!-- Asociada al boton Cerrar -->
	function accionCerrar() 
	{			
		window.close();
	}

	function accionEnviar() {
		    
		sub();
		var oCheck = document.getElementsByName("chkPL");
		var idsInformes = "";
		error = "";
		for (i = 0; i < oCheck.length; i++) {
			if (oCheck[i].checked) {
				ids = oCheck[i].id.split("_");
				var idTipoEnvio = document.getElementById("idTipoEnvio_"+ids[0]).value;
				if(idTipoEnvio==''){
					error = 'ko';
					break;
				}
				//meter validacion si esto es vacio
				var objPlantillaEnvio = document.getElementById("idPlantillaEnvio_"+ids[0]);
				var idPlantillaEnvio = objPlantillaEnvio.value;
				if(idPlantillaEnvio==''){
					error = 'ko';
					break;
				}
				var acuseRecibo = objPlantillaEnvio.options[objPlantillaEnvio.selectedIndex].acuseRecibo;

				//meter validacion si esto es vacio
				idsInformes += idTipoEnvio+","+idPlantillaEnvio+","+ids[1]+","+ids[2]+","+acuseRecibo+"##";	
			}
		}
		if(error!=''){
			alert("Debe configurar correctamente todos los envios marcados seleccionando tipo de envio y plantilla de envio");
	    	fin();
	    	return false;
			
		}
			
		if (idsInformes.length > 2)
			idsInformes = idsInformes.substring(0, idsInformes.length - 2);
		if(idsInformes==""){
	    	alert("Debe seleccionar al menos un informe");
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
	// descargarUnica();
	
</script>

</html>