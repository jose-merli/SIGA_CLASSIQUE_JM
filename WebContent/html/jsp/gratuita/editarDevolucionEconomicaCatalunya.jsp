<html>
<head>
<!-- editarDevolucionEconomicaCatalunya.jsp -->
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
	

		<!-- INICIO: TITULO Y LOCALIZACION -->
		
		

<script type="text/javascript">
	
	function refrescarLocal(){
		
		if(jQuery("#dialogoErrorLineaJustificacion:visible").length)
			closeDialog('dialogoErrorLineaJustificacion');
		
		buscar();
	}
	function buscar() {
		document.forms['GestionEconomicaCatalunyaForm'].modo.value = 'buscaEdicionJustificacion';
		document.forms['GestionEconomicaCatalunyaForm'].submit();
		
	}
	function marcarDesmarcarTodos(o) { 
		var ele = document.getElementsByName("chkActuacion");
		for (i = 0; i < ele.length; i++) {
			if(!ele[i].disabled)
				ele[i].checked = o.checked;
		}
  	 }
	
	
	function openDialog(dialogo){
		jQuery("#"+dialogo).dialog(
				{
				      height: 350,
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
		//sub();
		error = '';
		if(dialogo=='dialogoErrorLineaJustificacion'){
			errorLinea = jQuery("#errorLineaJustificacion").val();
			if(errorLinea==''){
				error += "<siga:Idioma key='errors.required' arg0='Error designa'/>";
				
			}
			if (error=='')
				document.forms['FormularioGestion'].modo.value = "insertaErrorLineaJustificacion";
		
		
			if (error!=''){
				alert(error);
				fin();
				return false;
			}
			var ele = document.getElementsByName('chkActuacion');
			var datosMasivos='';
			for (fila = 0; fila < ele.length; fila++) {
			    if(ele[fila].checked){
			    	filaTabla = fila+1;
			    	var identificadorFila = 'identificador_' + filaTabla ;
					identificador = document.getElementById(identificadorFila).value;
				   	datosMasivos += identificador +"##"; 			
				}			
			}
			
			if(datosMasivos==''){
				alert('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return false;
			}
			
			document.forms['FormularioGestion'].seleccion.value = datosMasivos;
			document.forms['FormularioGestion'].error.value = errorLinea;
			document.forms['FormularioGestion'].submit();
		}
		
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

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="accion"/>
		<html:hidden property="idJustificacion"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="jsonVolver"/>
		

		<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.leyenda.datosJustificacion">
			<table width="100%" border="0">
				<tr>
					<td width="15"></td>
					<td width="35"></td>
					<td width="15"></td>
					<td width="35"></td>
					
				</tr>
				
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.busquedaClientesAvanzada.literal.colegio"/>
					</td>
					<td class="labelTextValor">
						<c:out	value="${justificacion.abreviaturaInstitucion}"/>
							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.justificacion"/>
						
					</td>
					<td class="labelTextValor">
						<c:out	value="${justificacion.descripcion}"/>
							
					</td>
				</tr>
			<tr>
			
					<td class="labelText" >
						<siga:Idioma key="gratuita.mantActuacion.literal.anio"/>
					</td>
					<td class="labelTextValor"> 
						<c:out value="${justificacion.anio}"/>
							
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/>
					</td>
					<td class="labelTextValor">
						<c:out	value="${justificacion.nombrePeriodo}"/>
					</td>
					
				</tr>
				<tr>
	
					<td class="labelText">
						<siga:Idioma key="pestana.justiciagratuita.retenciones"/>
					</td>
					<td class="labelTextValor" >
						<c:out value="${justificacion.descripcionFacturaciones}"/>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
					</td >
					<td colspan="2" class="labelTextValor">
						<c:out value="${justificacion.descripcionEstado}"/>
						
					</td>
				</tr>
				
				
			</table>				
		</siga:ConjCampos>
	<siga:ConjCampos leyenda="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.criterios">
			<table width="100%" border="0">
				<tr>
					<td width="15"></td>
					<td width="35"></td>
					<td width="15"></td>
					<td width="35"></td>
					
					
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="envios.definir.literal.identificador"/>
					</td>
					<td class="labelTextValor">
						<html:text property="edicionId" styleClass="box"  />
							
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.fecha"/>
					</td>
					<td class="labelTextValor">
						<siga:Fecha nombreCampo="edicionFecha" valorInicial="${GestionEconomicaCatalunyaForm.edicionFecha}"  />
					</td>
					<td class="labelText">
						<siga:Idioma key="censo.tipoCliente.colegiado"/>
					</td>
					<td class="labelTextValor" >
						<html:text property="edicionLetrado"  styleClass="box"  />
					</td>
				</tr>
			<tr>
					<td class="labelText" >
						<siga:Idioma key="gratuita.operarEJG.literal.designa"/>
					</td>
					<td class="labelTextValor"> 
						<html:select property="edicionTipoActuacion" styleClass="boxCombo"  >						
								<html:option value="" ></html:option>
								<html:option value="D"><siga:Idioma key="menu.justiciaGratuita.TurnosOficio"/></html:option>
								<html:option value="A"><siga:Idioma key="gratuita.mantActuacion.literal.guardia"/></html:option>
							</html:select>
						
							
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.mantActuacion.literal.fecha"/>&nbsp;<siga:Idioma key="gratuita.mantActuacion.literal.actuacion"/>
					</td>
					<td class="labelTextValor">
						<siga:Fecha nombreCampo="edicionFechaJustificacion" valorInicial="${GestionEconomicaCatalunyaForm.edicionFechaJustificacion}" />
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarEJG.literal.interesado"/>
					</td >
					<td class="labelTextValor">
						<html:text property="edicionJusticiable" styleClass="box"  />
						
					</td>
					
				</tr>
				
				
			</table>				
		</siga:ConjCampos>
	<siga:ConjBotonesBusqueda botones="B"/>
	<c:set var="columnNames" value="envios.definir.literal.identificador,gratuita.mantActuacion.literal.fecha,
		censo.tipoCliente.colegiado,gratuita.mantAsistencias.literal.ejg,gratuita.mantAsistencias.literal.turno,gratuita.operarEJG.literal.designa,gratuita.mantActuacion.literal.fechajustificacion,gratuita.actuacionesDesigna.literal.modulo,gratuita.datoseconomicos.importe,gratuita.operarEJG.literal.interesado,Error" />
	<c:set var="columnSizes" value="13,7,10,6,10,10,5,6,6,10,6" />
	<c:set var="mostrarBotonEdicion" value="no" />
	<c:set var="botonesFila" value="" />
	
	<c:if test="${GestionEconomicaCatalunyaForm.modo=='editaJustificacion'}">
		<c:set var="columnNames" value="<input type='checkbox' name='chkGeneral' id ='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>
		,envios.definir.literal.identificador,gratuita.mantActuacion.literal.fecha,censo.tipoCliente.colegiado
		,gratuita.mantAsistencias.literal.ejg,gratuita.mantAsistencias.literal.turno,gratuita.operarEJG.literal.designa,gratuita.mantActuacion.literal.fechajustificacion,gratuita.actuacionesDesigna.literal.modulo,gratuita.datoseconomicos.importe,gratuita.operarEJG.literal.interesado,Error," />
		<c:set var="columnSizes" value="4,13,7,10,6,10,10,5,6,6,10,6,4" />
		<c:set var="mostrarBotonEdicion" value="no" />
		<c:set var="botonesFila" value="E" />
	</c:if>
		<siga:Table name="listadoInicial" border="1" columnNames="${columnNames}" columnSizes="${columnSizes}">
	   

			<c:choose>
				<c:when test="${empty justificacion.datosJustificacion}">
					<tr class="notFound">
			   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				</c:when>
				<c:otherwise>
					
					<c:forEach items="${justificacion.datosJustificacion}" var="actuacion" varStatus="status">
					
					
						<siga:FilaConIconos	fila='${status.count}'
			  				botones="${botonesFila}" 
			  				pintarEspacio="no"
			  				visibleConsulta="no"
			  				visibleEdicion = "${mostrarBotonEdicion}"
			  				visibleBorrado = "no"
			  				clase="listaNonEdit">
			  				<c:if test="${GestionEconomicaCatalunyaForm.modo=='editaJustificacion'}">
			  					<td >
		 							
		 							<input type="checkbox" id="chkActuacion_${status.count}" name="chkActuacion" />
								</td>
			  				</c:if>
		 					<td>
		 						<input type="hidden"	id="identificador_${status.count}"	value="${actuacion.codActuacion}"/>
								<c:out value="${actuacion.codActuacion}"/>
							</td>
							<td>
								<c:out value="${actuacion.fechaActuacion}"/>
							</td>
							<td>
								<c:out value="${actuacion.abogado}"/>
							</td>
							<td>
								<c:out value="${actuacion.ejg}"/>
							</td>
							<td>
								<c:out value="${actuacion.turno}"/>
							</td>
							<td>
								<c:out value="${actuacion.oficio}"/><c:out value="${actuacion.guardia}"/>
							</td>
							<td>
								<c:out value="${actuacion.fechaJustificacion}"/>
							</td>
							<td>
								<c:out value="${actuacion.modulo}"/>
							</td>
							<td>
								<c:out value="${actuacion.importe}"/>
							</td>
							<td>
								<c:out value="${actuacion.justiciable}"/>
							</td>		
								<td>
								<c:out value="${actuacion.error}"/>
							</td>		
							
						</siga:FilaConIconos>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			
			
			</siga:Table>
			
			
		<table class="botonesDetalle" align="center">
				<tr>
				<td class="tdBotones">
					<input type="button" alt="<siga:Idioma key='general.boton.volver'/>"  id="idButton" onclick="return accionVolver();" class="button" name="idButton" value="<siga:Idioma key='general.boton.volver'/>">
				</td>	
				<td style="width: 900px;">
						&nbsp;
					</td>
					<td class="tdBotones">
					<c:if test="${GestionEconomicaCatalunyaForm.accion=='editaJustificacion'}">
						<input type="button" alt="<siga:Idioma key='general.boton.editarSel'/>"	id="idValidarSolicitudAceptada" onclick="return editar();" class="button"   name="idButton" value="<siga:Idioma key='general.boton.editarSel'/>" />
					</c:if>
					</td>
				</tr>
			</table>

</html:form>
<html:form action="${path}"  name="FormularioGestion" type ="com.siga.gratuita.form.GestionEconomicaCatalunyaForm" target="submitArea">
 	<html:hidden property="modo"/>
	<html:hidden property="idInstitucion" value='${GestionEconomicaCatalunyaForm.idInstitucion}' />
	<html:hidden property="idJustificacion" value='${GestionEconomicaCatalunyaForm.idJustificacion}'/>
	<html:hidden property="seleccion"/>
	<html:hidden property="error"/>
</html:form>
<div id="dialogoErrorLineaJustificacion"  title='<bean:message key="general.literal.errorGlobal"/>' style="display:none">
<div>&nbsp;</div>
	<div>
		<div class="labelText">
   			<label for="errorLineaJustificacion"  style="width:90px;float:left;color: black">Error</label>
   			<textarea  id="errorLineaJustificacion" name="errorLineaJustificacion"
			                	onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
			                	style="overflow-y:auto; overflow-x:hidden; width:350px; height:70px; resize:none;" 
			                	class="box"></textarea>  
		</div>
	</div>
</div>
		


<script type="text/javascript">
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

	function editar(fila){
		if(fila){
			document.getElementById('chkGeneral').checked=false;
			marcarDesmarcarTodos(document.getElementById('chkGeneral'));
			
			var identificadorCheck = 'chkActuacion_' + fila;
			document.getElementById(identificadorCheck).checked=true;
		}
		jQuery("#errorLineaJustificacion").val("");
		openDialog('dialogoErrorLineaJustificacion');
	}
</script>
	<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
</body>
</html>
