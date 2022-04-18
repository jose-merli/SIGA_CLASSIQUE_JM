<html>
<head>
<!-- listadoDesgloseIntercambiosCatalunya.jsp -->
<!-- CABECERA JSP -->
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
<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>" ></script>
<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>

	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">

<script type="text/javascript">
	if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
		alert(document.getElementById("mensajeSuccess").value,'success');
	}
	
	function refrescarLocal(){
		accionVolver();
		inicio();
	}

	function accionVolver() 
	{		
		document.forms['GestionEconomicaCatalunyaForm'].modo.value="volver";
		document.forms['GestionEconomicaCatalunyaForm'].target = 'mainWorkArea';
		document.forms['GestionEconomicaCatalunyaForm'].submit();
	}
	
	
	function inicio() {
		if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
			alert(document.getElementById("mensajeSuccess").value,'success');
		}
	}
	function consultaVistaPrevia(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "consultaVistaPrevia";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].submit();
		
	}
	
	function revalida(fila) {
		sub();
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].modo.value = "valida";
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();
		
	}
	
	
	function enviaIntercambiosGEN() {
		sub();
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById('idInstitucionIntercambio').value;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById('idIntercambio').value;
		document.forms['FormularioGestion'].modo.value = "enviaIntercambiosGEN";
		document.forms['FormularioGestion'].submit();
	}
	
	
	function enviaIntercambiosCICAC() {
		sub();
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById('idInstitucionIntercambio').value;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById('idIntercambio').value;
		document.forms['FormularioGestion'].modo.value = "enviaIntercambiosCICAC";
		document.forms['FormularioGestion'].submit();
	}
	function enviaRespuestaCICAC_ICA(fila) {
		//alertStop("Envia el respuesta al ICA ");
		sub();
		document.forms['FormularioGestion'].modo.value = "enviaRespuestaCICAC_ICA";
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById('idInstitucionIntercambio').value;
		document.forms['FormularioGestion'].idIntercambio.value = document.getElementById('idIntercambio').value;
		document.forms['FormularioGestion'].submit();
	}
	
	
	
	
	
	
	function descarga(fila) {
		sub();
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descarga";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();
		if(true)
			fin();
	}
	
	function consulta(fila) {
		sub();
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "consulta";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();
		if(true)
			fin();
	}
	
	function adjuntaFicheroError(fila) {
		
		var elem = document.getElementById("fileUpdate");
		elem.click();
		if(elem &&elem.value!=''){
			
			document.forms['FormularioGestion'].modo.value = "adjuntaFicheroError";
			var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
			document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
			document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
			document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
			document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
			document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
			document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		
			document.forms['FormularioGestion'].pathFile.value = elem.value; 
			document.forms['FormularioGestion'].submit();	
		}
	}		
	
	function insertaErrorGlobal(fila) {
		
		document.forms['FormularioGestion'].modo.value = "insertaErrorGlobal";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idInstitucion.value = document.getElementById("idInstitucionIntercambio").value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		
		jQuery("#errorGlobal").val("");
		openDialog('dialogoErrorGlobal');
		
		
	}
	
	
	function descargaLogValidacion(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErrorValidacion";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();		
	}
	
	
	
	function descargaErrores(fila) {
		alert('<siga:Idioma key="facturacion.estadosfac.literal.GenEjecucion"/>');
		document.forms['FormularioGestion'].modo.value = "descargaErrores";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();
		
	}
	
	function finalizaErrores(fila) {
		
		//alertStop("Finaliza errores. Cambia el estado e inserta ecom para revisar finalizados");
		sub();
		document.forms['FormularioGestion'].modo.value = "finalizaErrores";
		var idTipoIntercambio = 'idTipoIntercambio_' + fila ;
		document.forms['FormularioGestion'].idTipoIntercambio.value = document.getElementById(idTipoIntercambio).value;
		document.forms['FormularioGestion'].idDevolucion.value = document.getElementById("idDevolucion").value;
		document.forms['FormularioGestion'].idJustificacion.value = document.getElementById("idJustificacion").value;
		document.forms['FormularioGestion'].idCertificacion.value = document.getElementById("idCertificacion").value;
		document.forms['FormularioGestion'].idCertificacionAnexo.value = document.getElementById("idCertificacionAnexo").value;
		document.forms['FormularioGestion'].idTipoCertificacion.value = document.getElementById("idTipoCertificacion").value;
		document.forms['FormularioGestion'].submit();
		
		
	}
	
	
	
	
	
	
	function openDialog(dialogo){
		jQuery("#"+dialogo).dialog(
				{
				      height: 220,
				      width: 525,
				      modal: true,
				      resizable: false,
				      buttons: {
				    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionInsercion(dialogo); }},
				          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog(dialogo);}}
				      }
				}
			);
			
		jQuery(".ui-widget-overlay").css("opacity","0");
	}
	
	function accionInsercion(dialogo){
		sub();
		error = '';
		if(dialogo=='dialogoErrorGlobal'){
			document.forms['FormularioGestion'].error.value = jQuery("#errorGlobal").val();
			if(document.forms['FormularioGestion'].error.value==''){
				error += "<siga:Idioma key='errors.required' arg0='general.literal.errorGlobal'/>";
				
			}
			if (error=='')
				document.FormularioGestion.modo.value = "insertaErrorGlobal";
		}
		if (error!=''){
			alert(error);
			fin();
			return false;
		}
		
		document.forms['FormularioGestion'].submit();
	}
	function accionGuardar(dialogo){
		sub();
		document.forms['FormularioGestion'].descripcion.value = document.forms['GestionEconomicaCatalunyaForm'].descripcion.value;
		
		document.forms['FormularioGestion'].modo.value = "guardar";
		document.forms['FormularioGestion'].submit();
	}
	function closeDialog(dialogo){
		 jQuery("#"+dialogo).dialog("close"); 
	}
	

</script>
</head>


<body onload="inicio();">
	<c:set var="displaynone" value="display:none" />
	<c:set var="estiloText" value="boxConsulta" />
	<c:set var="estiloSelect" value="boxComboConsulta" />
	<input type="hidden" id="mensajeSuccess" value="${mensajeSuccess}"/>
	<input type="file" id ='fileUpdate' style="display:none"/>
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	
	<input type="hidden" id="idInstitucionIntercambio" value="${intercambio.idInstitucion}" />
	<input type="hidden" id="idIntercambio" value="${intercambio.idIntercambio}" />
	
	<html:form action="${path}"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="accion"/>
		<html:hidden property="idIntercambio"/>
		<html:hidden property="idInstitucion" value="${USRBEAN.location}" />
		<html:hidden property="jsonVolver"/>

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	

		<siga:ConjCampos leyenda="comunicaciones.leyenda.informacionIntercambio">
			<table width="100%" border="0">
				
				
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.busquedaClientesAvanzada.literal.colegio"/>
					</td>
					<td class="labelTextValor">
						<c:out	value="${intercambio.abreviaturaInstitucion}"/>
							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.justificacion"/>
						
					</td>
					
					<td class="labelTextValor">
					<c:choose>
						<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001'}">
							<c:out	value="${intercambio.descripcion}"/>
						</c:when>
						<c:otherwise>
							<html:text property="descripcion" value="${intercambio.descripcion}" size="25"   styleClass="box"  />
						</c:otherwise>
					</c:choose>
						
							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/>
					</td>
					<td class="labelTextValor">
						<c:out	value="${intercambio.nombrePeriodo}"/>
					</td>
						<td class="labelText" >
						<siga:Idioma key="gratuita.mantActuacion.literal.anio"/>
					</td>
					<td class="labelTextValor"> 
						<c:out value="${intercambio.anio}"/>
							
					</td>
					<td width='20%'>&nbsp;</td>
				</tr>
				<tr>
					<td  class="labelText" ><siga:Idioma key="gratuita.mantAsistencias.literal.estado"/></td>
					
					<td  class="labelText">
						<c:choose>
							<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001' }">
								<c:out		value="${intercambio.descripcionEstadoConsell}"/>
							</c:when>
							<c:otherwise>
								<c:out		value="${intercambio.descripcionEstadoIca}"/>
							</c:otherwise>
						</c:choose>
					
					</td>
					<td colspan="6"></td>
					
				</tr>
				
				
				
			</table>				
		</siga:ConjCampos>

	
</html:form>

<siga:Table name="listadoInicial" border="1"
	columnNames="administracion.informes.literal.tipointercambio,gratuita.mantAsistencias.literal.estado,"
	columnSizes="25,25,25,25">
					
	<c:choose>
		<c:when test="${empty listadoDesgloseIntercambios}">
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${listadoDesgloseIntercambios}"
				var="justificacion" varStatus="status">
				<bean:define id="elementosFila" name="justificacion"
					property="elementosFila" type="com.siga.tlds.FilaExtElement[]" />
				<siga:FilaConIconos fila='${status.count}' botones=""
					pintarEspacio="no" visibleConsulta="no" visibleEdicion="no"
					visibleBorrado="no"  elementos="${elementosFila}"
					clase="listaNonEdit">
					
					<td align='left'>
						<input type="hidden" id="idTipoIntercambio_${status.count}"
							value="${justificacion.idTipoIntercambio}" class="inputNotSelect"/>
						
						<c:if test="${justificacion.idJustificacion!=null}">
							<input type="hidden" id="idJustificacion" value="${justificacion.idJustificacion}" class="inputNotSelect"/>
						</c:if>
						<c:if test="${justificacion.idDevolucion!=null}">
							<input type="hidden" id="idDevolucion" value="${justificacion.idDevolucion}" class="inputNotSelect"/>
						</c:if>
						
						<c:if test="${justificacion.idCertificacion!=null}">
							<input type="hidden" id="idCertificacion" value="${justificacion.idCertificacion}" class="inputNotSelect"/>
						</c:if>
						<c:if test="${justificacion.idCertificacionAnexo!=null}">
							<input type="hidden" id="idCertificacionAnexo" value="${justificacion.idCertificacionAnexo}" class="inputNotSelect"/>
						</c:if>
						<c:if test="${justificacion.idTipoCertificacion!=null}">
							<input type="hidden" id="idTipoCertificacion" value="${justificacion.idDevolucion}" class="inputNotSelect"/>
						</c:if>
						
						<c:out	value="${justificacion.descripcionTipoIntercambio}"/>
						</td>
										
					<td align='left'>
						<c:choose>
							<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001' }">
								<c:out		value="${justificacion.descripcionEstadoConsell}"/>
							</c:when>
							<c:otherwise>
								<c:out		value="${justificacion.descripcionEstadoIca}"/>
							</c:otherwise>
						</c:choose>
					</td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</siga:Table>

<table class="botonesDetalle" id="idBotonesAccion"  align="center">
	<tr>
		<td class="tdBotones">
		<input type="button" alt="<siga:Idioma key='general.boton.volver'/>"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="<siga:Idioma key='general.boton.volver'/>">
		</td>
		<td class="tdBotones" style="width:900px;">
		&nbsp;
		</td>

		
		<c:choose>
			<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion!='3001' && GestionEconomicaCatalunyaForm.idEstado=='14' }">
				<td class="tdBotones">
					<input type="button" alt="<siga:Idioma key='intercambios.boton.enviar.consejo'/>"  id="idButton" onclick="return enviaIntercambiosCICAC();" class="button" name="idButton" value="<siga:Idioma key='intercambios.boton.enviar.consejo'/>">
				</td>
			</c:when>
			<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001' && GestionEconomicaCatalunyaForm.idEstado=='30' }">
				<td class="tdBotones">
					<input type="button" alt="<siga:Idioma key='intercambios.boton.enviar.comision'/>"  id="idButton" onclick="return enviaIntercambiosGEN();" class="button" name="idButton" value="<siga:Idioma key='intercambios.boton.enviar.comision'/>">
				</td>
			</c:when>
			<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001' && (GestionEconomicaCatalunyaForm.idEstado=='87' || GestionEconomicaCatalunyaForm.idEstado=='90' )  }">
				<td class="tdBotones">
					<input type="button" alt="<siga:Idioma key='intercambios.boton.enviar.ica'/>"  id="idButton" onclick="return enviaRespuestaCICAC_ICA();" class="button" name="idButton" value="<siga:Idioma key='intercambios.boton.enviar.ica'/>">
				</td>
			</c:when>
		</c:choose>
		<c:if test="${GestionEconomicaCatalunyaForm.idInstitucion!='3001'}">
			<td class="tdBotones">
				<input type="button" alt="Guardar"  id="idButton" onclick="return accionGuardar();" class="button" name="idButton" value="Guardar">
			</td>
		</c:if>
	</tr>
</table>
<html:form action="${path}" name="FormularioGestion" type ="com.siga.gratuita.form.GestionEconomicaCatalunyaForm"  target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idTipoIntercambio" />
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idIntercambio"  value="${GestionEconomicaCatalunyaForm.idIntercambio}"/>
		<html:hidden property="idJustificacion"/>
		<html:hidden property="idDevolucion"/>
		
		<html:hidden property="idCertificacion"/>
		<html:hidden property="idCertificacionAnexo"/>
		<html:hidden property="idTipoCertificacion"/>
		<html:hidden property="descripcion"/>
		<html:hidden property="error"/>
		<html:hidden property="pathFile"/>
</html:form>

<div id="dialogoErrorGlobal"  title='<siga:Idioma key="general.literal.errorGlobal"/>' style="display:none">
<div>&nbsp;</div>
	<div>
		<div class="labelText">
   			<label for="errorGlobal"  style="width:90px;float:left;color: black"><siga:Idioma key="general.literal.errorGlobal"/></label><textarea  id="errorGlobal" name="errorGlobal"
			                	onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
			                	style="overflow-y:auto; overflow-x:hidden; width:350px; height:120px; resize:none;" 
			                	class="box"></textarea>  
		</div>
	</div>
</div>


	<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
	
</body>
</html>



