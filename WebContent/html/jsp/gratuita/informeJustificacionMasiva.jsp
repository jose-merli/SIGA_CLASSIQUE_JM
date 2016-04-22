<!DOCTYPE html>
<html>
<head>
<!-- informeJustificacionMasiva.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>   
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
</head>

<body onLoad="inicio();">
	<bean:define id="mensajeBusqueda" name="MENSAJE_DOCRESOLUCION" scope="request" />
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
	<html:form action="${path}" method="POST" target="resultado">

		<html:hidden property="modo" />
		<html:hidden property="idPersona" />
		<html:hidden property="mostrarTodas" />
		<html:hidden property="activarRestriccionesFicha" />
		<html:hidden property="fichaColegial" />

		<c:choose>
			<c:when
				test="${InformeJustificacionMasivaForm.idPersona!=null&&InformeJustificacionMasivaForm.fichaColegial==true}">
				<table class="tablaTitulo" cellspacing="0">
					<tr>
						<td class="titulitosDatos"><siga:Idioma
								key="gratuita.informeJustificacionMasiva.literal.titulo" /> <c:out
								value="${InformeJustificacionMasivaForm.nombreColegiado}"></c:out>&nbsp;&nbsp;
							<c:choose>
								<c:when
									test="${InformeJustificacionMasivaForm.estadoColegial!=null&&InformeJustificacionMasivaForm.estadoColegial!=''}">
									<siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;&nbsp;<c:out value="${InformeJustificacionMasivaForm.numColegiado}"></c:out>
									<c:if test="${InformeJustificacionMasivaForm.estadoColegial!=null&&InformeJustificacionMasivaForm.estadoColegial!=''}">
					    				&nbsp;(<c:out value="${InformeJustificacionMasivaForm.estadoColegial}"></c:out>)
					    			</c:if>
								</c:when>
								<c:otherwise>
						 		(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial" />)
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
				<td><siga:ConjCampos desplegable="true" oculto="false"
						postFunction="ajustarDivListadoResultados();"
						leyenda="gratuita.informeJustificacionMasiva.leyenda.datosJustificacion">
						<table>
							<c:choose>
								<c:when test="${InformeJustificacionMasivaForm.idPersona==null&&InformeJustificacionMasivaForm.fichaColegial==false}">
									<tr>
										<td colspan="4"><siga:BusquedaPersona tipo="colegiado"
												campoObligatorio="true" idPersona="idPersona">
											</siga:BusquedaPersona></td>
									</tr>
									<tr>
										<td class="labelText"><siga:Idioma
												key="gratuita.informeJustificacionMasiva.literal.mostrarHistorico" />
											&nbsp;</td>
										<td class="labelText"><input type="checkbox"
											id="mostrarSoloPendientes" name="mostrarSoloPendientes"
											value="on"
											onclick="onClickMostrarJustificacionesPendientes();">
											&nbsp;</td>

										<td class="labelText"><siga:Idioma
												key="gratuita.informeJustificacionMasiva.activarRestricciones.literal" />&nbsp;
											<input type="checkbox" name="activarRestricciones" value="on">
										</td>
										<td>&nbsp;</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td class="labelText"><siga:Idioma
												key="gratuita.informeJustificacionMasiva.literal.anio" /></td>
										<td class="labelText"><html:text property="anio" size="4"
												styleClass="box" maxlength="4" /></td>
										<td class="labelText"><siga:Idioma
												key="gratuita.informeJustificacionMasiva.literal.mostrarHistorico" />
											&nbsp;</td>
										<td class="labelText"><input type="checkbox"
											id="mostrarSoloPendientes" name="mostrarSoloPendientes"
											value="on"
											onclick="onClickMostrarJustificacionesPendientes();">
											&nbsp;</td>
									</tr>
								</c:otherwise>
							</c:choose>

							<tr id="oculto">
								<td colspan="4">
									<table>
										<tr>
											<td width="20%"></td>
											<td width="18%"></td>
											<td width="5%"></td>
											<td width="18%"></td>
											<td width="5%"></td>
											<td width="10"></td>
											<td width="5%"></td>
											<td width="14%"></td>
										</tr>
										<tr>
											<td class="labelText"><siga:Idioma
													key="gratuita.informeJustificacionMasiva.literal.fecha.Justif" />&nbsp;<siga:Idioma
													key="general.literal.desde" /></td>
											<td>
											<siga:Fecha  nombreCampo= "fechaJustificacionDesde"/>
											</td>
											<td class="labelText"><siga:Idioma
													key="general.literal.hasta" /></td>
											<td>
											<siga:Fecha  nombreCampo= "fechaJustificacionHasta"/>
											</td>
											<td class="labelText"><siga:Idioma
													key="gratuita.editarDesigna.literal.estado" /></td>
											<td><Select name="estado" class="boxCombo">
													<option value=''></option>
													<option value='V'>
														<siga:Idioma key="gratuita.estadoDesignacion.activo" />
													</option>
													<option value='F'>
														<siga:Idioma key="gratuita.estadoDesignacion.finalizado" />
													</option>
													<option value='A'>
														<siga:Idioma key="gratuita.estadoDesignacion.anulado" />
													</option>
											</Select></td>
											<td class="labelText"><siga:Idioma
													key="gratuita.busquedaDesignas.literal.actuacionesValidadas" />
											</td>
											<td><Select name="actuacionesPendientes"
												class="boxCombo">
													<option value=''></option>

													<option value='No'>
														<siga:Idioma key="general.no" />
													</option>
													<option value='Si'>
														<siga:Idioma key="general.yes" />
													</option>
													<option value='SinActuaciones'>
														<siga:Idioma
															key="gratuita.busquedaDesignas.literal.sinActuaciones" />
													</option>
											</Select></td>
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
					<siga:ConjCampos desplegable="true" oculto="true"
						postFunction="ajustarDivListadoResultados();"
						leyenda="gratuita.busquedaDesignas.literal.datosDesigna">
						<table>
							<tr>
								<td class="labelText" nowrap>
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fechaSalida"/>&nbsp;<siga:Idioma key="general.literal.desde"/></td>
								<td nowrap>
									<siga:Fecha  nombreCampo= "fechaDesde"/>
								</td>
								
								<td class="labelText" nowrap>
									<siga:Idioma key="general.literal.hasta" />
								</td>								
								<td nowrap>
									<siga:Fecha  nombreCampo= "fechaHasta"/>
								</td>
								
								<td class="labelText" nowrap>
									<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
								</td>
								<td>
									<html:text property="interesadoNombre" size="15" maxlength="100" styleClass="box"/>
								</td>
								
								<td class="labelText" nowrap>
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.apellidos"/>
								</td>
								<td>
									<html:text property="interesadoApellidos" size="30" maxlength="100" styleClass="box"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText" nowrap>
									<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/>/<siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>
								</td>
								<td>	
									<html:text name="InformeJustificacionMasivaForm" property="anioDesgina" styleId="anioDesgina" style="width:40px" maxlength="4" styleClass="box"/>
									&nbsp;/&nbsp;
									<html:text name="InformeJustificacionMasivaForm" property="codigoDesigna" styleId="codigoDesigna" style="width:50px" maxlength="10" styleClass="box"/> 
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos desplegable="true" oculto="true"
						postFunction="ajustarDivListadoResultados();"
						leyenda="gratuita.busquedaEJG.literal.EJG">
						<table>
							<tr>
								<td class="labelText" nowrap>
									<siga:Idioma key="gratuita.busquedaEJG.literal.anyo" />/<siga:Idioma key="gratuita.busquedaEJG.literal.codigo" />
								</td>
								<td>	
									<html:text name="InformeJustificacionMasivaForm" property="anioEJG" styleId="anioEJG" style="width:40px" maxlength="4" styleClass="box"/>
									&nbsp;/&nbsp;
									<html:text name="InformeJustificacionMasivaForm" property="codigoEJG" styleId="codigoEJG" style="width:90px" maxlength="13" styleClass="box"/> 
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>
	
	<!-- Formularios auxiliares para la busqueda de persona-->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value="" />
		<html:hidden property="modo" value="abrirBusquedaModal" />
	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<siga:ConjBotonesBusqueda botones="B" titulo="${mensajeBusqueda}" />
	
	<iframe align="center"
		src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
		id="resultado" name="resultado" scrolling="no" frameborder="0"
		marginheight="0" marginwidth="0" ;	
					class="frameGeneral">
	</iframe>

	<iframe name="submitArea"
		src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
		style="display: none"></iframe>


	<script language="JavaScript">
		function inicio () {
			var siga ="SIGA";
			var tit ="<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.titulo"/>";
			setTitulo(siga, tit);
			if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true'){
				var loc ="<siga:Idioma key="censo.fichaCliente.sjcs.informeJustificacionMasiva.localizacion"/>";
				setLocalizacion(loc);	
			}else{
				var loc ="<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.localizacion"/>";
				setLocalizacion(loc);			
			}	
						
			document.getElementById("mostrarSoloPendientes").checked = "checked";
			document.getElementById("oculto").style.display = "none";	
			
			if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true'){
				buscar();
			}			
		}
		
		function onClickMostrarJustificacionesPendientes () {
			document.InformeJustificacionMasivaForm.estado.value='';
			document.InformeJustificacionMasivaForm.actuacionesPendientes.value=''; 
			if(document.getElementById("mostrarSoloPendientes").checked){
				document.getElementById("oculto").style.display = "none";
			} else if(document.InformeJustificacionMasivaForm.fichaColegial.value=='false'){
				document.getElementById("oculto").style.display = "block";
			} else{
				document.getElementById("oculto").style.display = "none";				
			}
			ajustarAltoResultado();
			 
		}
		
		function ajustarDivListadoResultados() {
			documentResultado = window.frames['resultado'];
			try {
				documentResultado.ajusteDivListado();
		  	} catch(err) {
		  		//peta porque todavia no se ha pintado la ventana hija
		  	}
		}
		
		function ajustarAltoResultado() {	
			ajusteAltoBotonesPaginador('resultado');
			ajusteAlto('resultado');
			ajusteAlto('mainWorkarea');
			ajustarDivListadoResultados();
		}
		
		function buscar () {
			sub();
			var	error ='';
			document.InformeJustificacionMasivaForm.mostrarTodas.value =document.getElementById("mostrarSoloPendientes").checked;
			if(document.getElementById("activarRestricciones"))
				document.InformeJustificacionMasivaForm.activarRestriccionesFicha.value =document.getElementById("activarRestricciones").checked;
			
			if(document.InformeJustificacionMasivaForm.anio){
				var objRegExp  = /^([0-9]{4})?$/;
				if(!objRegExp.test(document.InformeJustificacionMasivaForm.anio.value)){
					error += "<siga:Idioma key='errors.formato' arg0='gratuita.informeJustificacionMasiva.literal.anio'/>"+ '\n';				
				}
			}
			if(document.InformeJustificacionMasivaForm.anioDesgina){
				var objRegExp  = /^([0-9]{4})?$/;
				if(!objRegExp.test(document.InformeJustificacionMasivaForm.anioDesgina.value)){
					error += "<siga:Idioma key='errors.formato' arg0='gratuita.busquedaSOJ.literal.anyo.designa'/>"+ '\n';				
				}
			}	
			var mostrarMensajeUnaVezDesigna=false;
			var codigoDesigna = jQuery("#codigoDesigna").val();
			if (codigoDesigna!="" && codigoDesigna!="*") {
				var codigoDesignas = codigoDesigna.split(',');			
				if (codigoDesignas.length<2){
					codigoDesignas = codigoDesigna.split('-');
				}	
				for (var i = 0; i < codigoDesignas.length; i++) {
					if ((codigoDesignas[i]=="" || !isNumero(codigoDesignas[i])) && !mostrarMensajeUnaVezDesigna) {
						error += "<siga:Idioma key='errors.invalid' arg0='gratuita.busquedaSOJ.literal.codigo'/>"+ '\n';
						mostrarMensajeUnaVezDesigna=true;
					}
				}
			}
			
			if(document.InformeJustificacionMasivaForm.anioEJG){
				var objRegExp  = /^([0-9]{4})?$/;
				if(!objRegExp.test(document.InformeJustificacionMasivaForm.anioEJG.value)){
					error += "<siga:Idioma key='errors.formato' arg0='gratuita.busquedaSOJ.literal.anyo.ejg'/>"+ '\n';				
				}
			}	
			
			
			var mostrarMensajeUnaVezEJG=false;
			var codigoEJG = jQuery("#codigoEJG").val();
			if (codigoEJG!="" && codigoEJG!="*") {
				var codigoEJGs = codigoEJG.split(',');			
				if (codigoEJGs.length<2){
					codigoEJGs = codigoEJG.split('-');
				}	
				for (var i = 0; i < codigoEJGs.length; i++) {
					if ((codigoEJGs[i]=="" || !isNumero(codigoEJGs[i])) && !mostrarMensajeUnaVezEJG) {
						error += "<siga:Idioma key='errors.invalid' arg0='gratuita.busquedaEJG.literal.codigo'/>"+ '\n'; 
						mostrarMensajeUnaVezEJG=true;
					}
				}
			}
			
			if (!document.InformeJustificacionMasivaForm.idPersona.value) {
				error += "<siga:Idioma key='gratuita.informeJustificacionMasiva.mensaje.aviso.letradoRequerido'/>"+ '\n';
				
			} 
			if(error!=''){
				alert (error);
				fin();
				return false;
			}
			
			document.InformeJustificacionMasivaForm.modo.value = "buscarInit";
			document.InformeJustificacionMasivaForm.submit();
		}
	</script>
</body>

</html>