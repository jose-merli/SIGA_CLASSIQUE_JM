<!-- informeJustificacionMasiva.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>	
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.informeJustificacionMasiva.literal.titulo"  localizacion="gratuita.informeJustificacionMasiva.literal.localizacion"/>
	
</head>

<body onLoad="ajusteAlto('resultado');inicio();">
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action = "${path}" method="POST" target="resultado">

		<html:hidden property="modo"/>
		<html:hidden property="idPersona"/>
		<html:hidden property="mostrarTodas"/>
		<html:hidden property="fichaColegial"/>
		
		<c:choose>
			<c:when test="${InformeJustificacionMasivaForm.idPersona!=null&&InformeJustificacionMasivaForm.fichaColegial==true}">
				<table class="tablaTitulo" cellspacing="0">
				<tr>
					<td class="titulitosDatos">
						<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.titulo"/>
						<c:out value="${InformeJustificacionMasivaForm.nombreColegiado}"></c:out>&nbsp;&nbsp;
					    <c:choose>
					    	<c:when test="${InformeJustificacionMasivaForm.numColegiado!=null&&InformeJustificacionMasivaForm.numColegiado!=''}">
					    		<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<c:out value="${InformeJustificacionMasivaForm.numColegiado}"></c:out>
					    		<c:if test="${InformeJustificacionMasivaForm.estadoColegial!=null&&InformeJustificacionMasivaForm.estadoColegial!=''}">
					    			&nbsp;(<c:out value="${InformeJustificacionMasivaForm.estadoColegial}"></c:out>)
					    		</c:if>
					    	</c:when>
					    	<c:otherwise>
					    	<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					    	</c:otherwise>
					    </c:choose>
					</td>
				</tr>
			</table>
			</c:when>
			<c:otherwise>
			
			</c:otherwise>
		</c:choose>
		
		
		
		
		<table width="100%" border="0">

			<tr>
				<td>
				<siga:ConjCampos desplegable="true" oculto="false" leyenda="Datos de Justificacion">
				
					<table>
					
					
					
						<tr>
					    	<c:choose>
					    		<c:when test="${InformeJustificacionMasivaForm.idPersona==null&&InformeJustificacionMasivaForm.fichaColegial==false}">
						    		<td colspan ="2">
							    		<siga:BusquedaPersona tipo="colegiado" campoObligatorio="true" idPersona="idPersona"  >
										</siga:BusquedaPersona>
									</td>
						    	</c:when>
						    	<c:otherwise>
						    		<td class="labelText">
										<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.anio"/>
									</td>
									<td class="labelText">
										<html:text property="anio"  size="4" styleClass="box"  maxlength="4"/>
									</td>
						    	</c:otherwise>
						    </c:choose>
							<td class="labelText" >
								<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.mostrarHistorico"/>
								&nbsp;
							</td>
							<td class="labelText">	
								<input type="checkbox" name="mostrarSoloPendientes" value="on" onclick="onClickMostrarJustificacionesPendientes();">
								&nbsp;
							</td>
						</tr>
						
						
						<tr id="oculto">
							<td colspan ="4">
							
								<table>
									<tr>
								
										<td width="20%">
										</td>
										<td width="18%">
										</td>
										<td width="5%">
										</td>
										<td width="18%">
										</td>
										<td width="5%">
										</td>
										<td width="10">	
										
										</td>	
										<td width="5%">
										</td>
										<td width="14%">	
										
										</td>	
										
				
									</tr>
									<tr>
										<td class="labelText">
			
												<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fecha.Justif"/>&nbsp;<siga:Idioma key="general.literal.desde"/>
			
										</td>
										<td>
											<html:text property="fechaJustificacionDesde"  size="10" styleClass="box" readOnly="true" />
											&nbsp;
											<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaJustificacionDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
									
										</td>
										<td class="labelText">
											<siga:Idioma key="general.literal.hasta"/>
										</td>
										<td>
											<html:text property="fechaJustificacionHasta"  size="10" styleClass="box" readOnly="true" />
												&nbsp;
												<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaJustificacionHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
										</td>
										
										
										<td class="labelText" >
												<siga:Idioma key="gratuita.editarDesigna.literal.estado"/>
										</td>
										<td>	
											<Select name="estado" class="boxCombo">
															<option value=''></option>
															<option value='V' ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
															<option value='F' ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
															<option value='A' ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
											</Select>
										</td>
										<td class="labelText">
												<siga:Idioma key="gratuita.busquedaDesignas.literal.actuacionesValidadas"/>
										</td>
										<td >	
											<Select name="actuacionesPendientes" class="boxCombo">
												<option value='' ></option>
											
												<option value='No'><siga:Idioma key="general.no"/></option>
												<option value='Si' ><siga:Idioma key="general.yes"/></option>
												<option value='SinActuaciones'><siga:Idioma key="gratuita.busquedaDesignas.literal.sinActuaciones"/></option>
											</Select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						
					</table>
				</siga:ConjCampos>
				</td>
				
			</tr>
			
			
			<tr>
				<td>
					<siga:ConjCampos desplegable="true" oculto="true" leyenda="gratuita.busquedaDesignas.literal.datosDesigna">
						<table>
						
							<tr>
								
								<td width="20%">
								</td>
								<td width="18%">
								</td>
								<td width="5%">
								</td>
								<td width="18%">
								</td>
								<td width="5%">
								</td>
								<td width="10">	
								
								</td>	
								<td width="5%">
								</td>
								<td width="14%">	
								
								</td>	
								
		
							</tr>
						
						
							<tr>
								
								<td class="labelText">

									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fechaSalida"/>&nbsp;<siga:Idioma key="general.literal.desde"/>

								</td>
								<td>
									<html:text property="fechaDesde"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
						
								</td>
								<td class="labelText">
									<siga:Idioma key="general.literal.hasta"/>
								</td>
								<td>
									<html:text property="fechaHasta"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
								</td>
							
								
								
								<td class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
								</td>
								<td>	
									<html:text  property="interesadoNombre" size="15" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.apellidos"/>
								</td>
								<td >	
									<html:text  property="interesadoApellidos" size="30" maxlength="100" styleClass="box" ></html:text>
								</td>	
								
		
							</tr>
							
							
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
				
				
				
		</table>
	</html:form>

	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->


	<siga:ConjBotonesBusqueda   botones="B"  titulo=""/>

		<!-- Formularios auxiliares para la busqueda de persona-->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modo" value="abrirBusquedaModal"/>
		
	</html:form>
	

	<iframe align="center" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>	
	
	
	
		<script language="JavaScript">
function inicio ()
{
	document.getElementById("mostrarSoloPendientes").checked = "checked";
	document.getElementById("oculto").style.display = "none";
	
	
	
	if(document.InformeJustificacionMasivaForm.idPersona.value!=''){
		var date = new Date();
        
        iYear = date.getFullYear(); 
		document.InformeJustificacionMasivaForm.anio.value = iYear;
		//String anio = UtilidadesBDAdm.getYearBD("");
		buscar();
	}
	
}
function onClickMostrarJustificacionesPendientes ()
{
	document.InformeJustificacionMasivaForm.estado.value='';
	document.InformeJustificacionMasivaForm.actuacionesPendientes.value=''; 
	if(document.getElementById("mostrarSoloPendientes").checked){
		document.getElementById("oculto").style.display = "none";
	}else if(document.InformeJustificacionMasivaForm.fichaColegial.value=='false'){
		document.getElementById("oculto").style.display = "block";
	
	}else{
		document.getElementById("oculto").style.display = "none";
	}
	// buscar();
	
}
function buscar ()
{
	sub();
	/*if(document.getElementById("mostrarSoloPendientes").checked){
		document.InformeJustificacionMasivaForm.mostrarTodas.value ="on";
	}else{
		document.InformeJustificacionMasivaForm.mostrarTodas.value = "off";
	}
	*/ 
	document.InformeJustificacionMasivaForm.mostrarTodas.value =document.getElementById("mostrarSoloPendientes").checked;
	if (document.InformeJustificacionMasivaForm.idPersona.value) {
		document.InformeJustificacionMasivaForm.modo.value = "buscarInit";
		// document.InformeJustificacionMasivaForm.modo.value = "buscar";
		document.InformeJustificacionMasivaForm.submit();
	}else{
		alert ("<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.aviso.letradoRequerido"/>");
		fin();
		return false;
			
	}
}


	</script>
	
	
  </body>
  
</html>