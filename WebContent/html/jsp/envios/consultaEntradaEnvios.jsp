<!--- consultaEntradaEnvios.jsp -->
<!-- 
	 VERSIONES:
	 Carlos Ruano Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 	prefix="html"%>
<%@ taglib uri = "c.tld" 			prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.text.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="java.io.File" %>
<!-- JSP -->

<%@page import="java.io.File"%>
<html>

<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery.js"/>" ></script>
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery.custom.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="envios.bandejaentrada.titulo"	localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body class="tablaCentralCampos" onload="inicio()">
	
	<html:form action="/ENV_EntradaEnvios.do" method="POST" target="mainWorkArea" style="display:none">		
		<input type="hidden" id="actionModal" name="actionModal" value="">	
	    <html:hidden styleId = "modo" 					property = "modo"/>
	    <html:hidden styleId = "idEnvio" 				property = "idEnvio"  				value="${entradaEnvio.idEnvio}"/>
	    <html:hidden styleId = "idInstitucion"			property = "idInstitucion"  		value="${entradaEnvio.idInstitucion}"/>
	   	<html:hidden styleId = "anioEJGSel" 			property = "anioEJGSel" />
	    <html:hidden styleId = "numeroEJGSel" 			property = "numeroEJGSel"			value="${entradaEnvio.numeroEJGSel}"/>
	    <html:hidden styleId = "numeroDesignaSel" 		property = "numeroDesignaSel" 		value="${entradaEnvio.numeroDesignaSel}"/>
	    <html:hidden styleId = "idTurnoDesignaNew" 		property = "idTurnoDesignaNew"/>
	    <html:hidden styleId = "idTurnoDesignaSel" 		property = "idTurnoDesignaSel"/>
	    <html:hidden styleId = "caso" 					property = "caso"  					value="${entradaEnvio.caso}"/>
	    <html:hidden styleId = "botonesDetalle" 		property = "botonesDetalle"  		value="${entradaEnvio.botonesDetalle}"/>
	    <html:hidden styleId = "idTipoEJGSel" 			property = "idTipoEJGSel" 			value="${entradaEnvio.idTipoEJGSel}"/>
	    <html:hidden styleId = "descripcionSolicitante"	property = "descripcionSolicitante"/>
	   	<html:hidden styleId = "anioDesignaSel" 		property = "anioDesignaSel" />
	   	<html:hidden styleId = "anioDesignaNew" 		property = "anioDesignaNew" />
	    <html:hidden styleId = "numeroDesignaNew" 		property = "numeroDesignaNew" />
	    <html:hidden styleId = "abogadoDesignaSel" 		property = "abogadoDesignaSel" />
	    <html:hidden styleId = "codSelDesigna" 			property = "codSelDesigna" />
	    <html:hidden styleId = "numEJGSel" 				property = "numEJGSel" />
	    <html:hidden styleId = "anioEJGNew" 			property = "anioEJGNew" />
	    <html:hidden styleId = "numeroEJGNew" 			property = "numeroEJGNew" />
	    <html:hidden styleId = "idTipoEJGNew" 			property = "idTipoEJGNew"/>	    
	    <html:hidden styleId = "fechaApertura" 			property = "fechaApertura"/>
	</html:form>

	<table class="tablaCentralCampos" align="center">
		<tr>
			<td width="5%"></td>
			<td width="90%"></td>
			<td width="5%"></td>
		</tr>
		<tr>
			<td colspan="3">
			
			<siga:ConjCampos leyenda="Datos entrada">
					<table class="tablaCampos" align="center">
						<tr>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="8%"></td>
							<td width="8%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
						</tr>
						<tr>
							<td class="labelText">Asunto</td>
							<td class="labelTextValor" colspan="4"><c:out
									value="${entradaEnvio.asunto}" /></td>
							<td class="labelText"><bean:message
									key="envios.definir.literal.remitente" /></td>
							<td class="labelTextValor" colspan="4"><c:out
									value="${entradaEnvio.descripcionRemitente}" /></td>
						</tr>
						<tr>
							<td class="labelText"><bean:message
									key="envios.definir.literal.estado" /></td>
							<td class="labelTextValor"><c:out
									value="${entradaEnvio.descripcionEstado}" />
							</td>
							<td class="labelText">ID. Msj.</td>
							<td class="labelTextValor"><c:out
									value="${entradaEnvio.identificador}" /></td>
							<td class="labelText"><bean:message
									key="envios.definir.literal.fechaeentrada" /></td>
							<td class="labelTextValor" ><c:out
									value="${entradaEnvio.fechaPeticion}" /></td>
						
							<td class="labelText"><bean:message
									key="envios.definir.literal.fecharespuesta" /></td>
							<td class="labelTextValor"><c:out
									value="${entradaEnvio.fechaRespuesta}" /></td>
							
							<td class="labelText">ID. Msj. Relacionado</td>
							<td class="labelTextValor">
								<c:if test="${entradaEnvio.idEnvioRelacionado !=null && entradaEnvio.idEnvioRelacionado!=''}">
									<c:out value="${entradaEnvio.idEnvioRelacionado}" />
									<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_off.gif"
										style="cursor: hand;" alt="Consultar" name="consultar_1"
										border="0" onClick="consultarEnvioRelacionado();"> 
									<img id="iconoboton_editar1" src="/SIGA/html/imagenes/beditar_off.gif"
										style="cursor: hand;" alt="Editar" name="editar_1"
										border="0" onClick="editarEnvioRelacionado();">
								</c:if>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
				</td>
		</tr>
		<tr>
			<td colspan="3" >
			<siga:ConjCampos leyenda="Informacion Intercambio">
				<table>
					${intercambio}
				</table>
			</siga:ConjCampos>
			</td>
		</tr>
		<c:choose>
		<c:when test="${entradaEnvio.idTipoIntercambioTelematico=='06'}">
			<c:choose>
				<c:when test="${entradaEnvio.idEstado=='1' || entradaEnvio.idEstado=='2' || entradaEnvio.idEstado=='4'}">
				<!-- Para los estados SIN_LEER, LEIDO y PTE. ENVIAR -->
				<tr>
					<td colspan="3">
						<siga:ConjCampos leyenda="Acciones">
							<table class="tablaCampos" align="center">
								<tr>
									<td width="10%"></td>
									<td width="20%"></td>
									<td width="20%"></td>
									<td width="20%"></td>
									<td width="20%"></td>
									<td width="10%"></td>
								</tr>
		
							<!-- Esto hay que ocultarlo cuando ya se proceso el ejg o la designa desde la informacion de intercmabio. Esto es cuando el idcomcola de la tabla ecom_soldesignaprovisional es nulo -->
		
								<tr>
									<td colspan="6" class="labelText">
										<input type="radio" id="radioAccionEJG" name='radioAccion' value="0" onclick="accionRadio()"/>EJG y designación
									</td>
								</tr>
								
								<tr>
									<td></td>
									<td colspan="5" class="labelTextValor">
										<i>
										Seleccione "Nuevo EJG" para abrir Expediente JG con los datos de la solicitud y pulse en Guardar. 
										
										O bien, seleccione un  EJG existente y pulse Guardar.
										</i>
									</td>
								</tr>							
		
								<tr>
									<td></td>
									<td  class="labelText">
										<input type="checkbox" id="nuevoEJG" name="nuevoEJG" onclick="accionNuevoEJG()"/>Nuevo EJG 
									</td>
									
									<c:if test="${entradaEnvio.anioEJGNew != null && entradaEnvio.numeroEJGNew != null}">								
										<td class="labelTextValor">
											<c:out value="${entradaEnvio.anioEJGNew}" />/<c:out value="${entradaEnvio.numEJGNew}" />
										</td>	
									</c:if>								
									<td  class="labelText">o Seleccionar EJG</td>
									<td>
										<html:text styleId="anioEJGSelText" property="anioEJGSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.anioEJGSel}" disabled="true" readonly="true"></html:text>
										 /
										<html:text styleId="numeroEJGSelText" property="numeroEJGSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.numEJGSel}" disabled="true" readonly="true"></html:text>
									</td>
									<td  class="labelText">
										<input type="button" class="button" id="buscarEJG" name="buscarEJG" value='Buscar' onClick="buscarEJG();">
									</td>
									<td></td>
								</tr>
								<c:if test="${entradaEnvio.caso=='1' && entradaEnvio.idEstado=='4'}">
									<tr>
										<td colspan="1" ></td>
										<td colspan="5" >
										<siga:ConjCampos leyenda="Datos respuesta">
											<table class="tablaCampos" align="center">
												<tr>
													<td></td>
													<td  class="labelText">EJG
													</td>
													<td  class="labelTextValor"><c:out value="${entradaEnvio.anioEJGSel}" />/<c:out value="${entradaEnvio.numEJGSel}" /></td>
													<td class="labelTextValor"><c:out value="${entradaEnvio.descripcionSolicitante}"/></td>
													<td>
														<img id="iconoboton_editarEJG1" src="/SIGA/html/imagenes/beditar_off.gif"
															style="cursor: hand;" alt="Editar" name="consultar_1"
															border="0" onClick="editarEJG();"> 
														<img id="iconoboton_borrarEJG1" src="/SIGA/html/imagenes/bborrar_off.gif"
															style="cursor: hand;" alt="Borrar" name="consultar_1"
															border="0" onClick="borrarRelacionEJG();">
													</td>
													<td></td>
												</tr>		
												<c:choose>
												<c:when test="${entradaEnvio.anioDesignaSel != null}">									
													<tr>
														<td></td>
														<td  class="labelText">DESIGNACION
														</td>
														<td class="labelTextValor"><c:out value="${entradaEnvio.anioDesignaSel}"/>/<c:out value="${entradaEnvio.codSelDesigna}"/></td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.descripcionSolicitante}"/></td>
															<td>
															<img id="iconoboton_editarDesigna1" src="/SIGA/html/imagenes/beditar_off.gif"
																				style="cursor: hand;" alt="Editar" name="consultar_1"
																				border="0" onClick="editarDesignacion();"> 
														
														</td>
														<td></td>
													</tr>
													<c:if test="${entradaEnvio.abogadoDesignaSel != null}">
														<tr>
															<td></td>
															<td  class="labelText">
															</td>
															<td  class="labelText">Abogado</td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.abogadoDesignaSel}"/></td>
															<td></td>
															<td></td>
														</tr>
													</c:if>	
													<c:if test="${entradaEnvio.procuradorDesignaSel != null}">
														<tr>
															<td></td>
															<td  class="labelText">
															</td>
															<td  class="labelText">Procurador</td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.procuradorDesignaSel}"/></td>
															<td></td>
															<td></td>
														</tr>
													</c:if>	
												</c:when>
												<c:when test="${empty entradaEnvio.ejgSelDesignas}">
													<tr>
														<td></td>
														<td class="labelTextValor" colspan="6">Necesita crear una designación. Pulse editar EJG para coninuar el proceso.</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr>
														<td></td>
														<td class="labelTextValor" colspan="6">Seleccione una designacion y pulse guardar para continuar el proceso y el combo de designaciones.</td>
														<td colspan="2">
															<select id="designacionesRelacionadas"/>
															<script type="text/javascript">
																cargarComboDesignasRelacionadas();
																function cargarComboDesignasRelacionadas() {
																	var designas = ${entradaEnvio.ejgSelDesignas};
																	jQuery("#designacionesRelacionadas").append("<option  value=''>&nbsp;</option>");
																   	jQuery.each(designas, function(i,item2){
																          jQuery("#designacionesRelacionadas").append("<option value='"+item2.value+"'>"+item2.descripcion+"</option>");
																    });
															 	}
															</script>
														</td>
													</tr>
												</c:otherwise>
												</c:choose>
											</table>
										</siga:ConjCampos>
									</td>
								</c:if>
								<tr>
									<td colspan="6" class="labelText"><input type="radio" id='radioAccionDesigna' name='radioAccion' value="1" onclick="accionRadio()"/>Excepción: Designación sin EJG</td> 
								</tr>
								
								<tr>
									<td></td>
									<td colspan="5" class="labelTextValor">
										<i>
										Para el caso de una designación SIN EJG asociado, pulse en "Nueva designación" para abrir una nueva con los datos de la solicitud y pulse Guardar.
		
										O bien, seleccione una designación existente y pulse Guardar.
										</i>
									</td>
								</tr>	
								
								<tr>
									<td></td>
									<td  class="labelText">
										<input type="checkbox" id="nuevaDesigna" name="nuevaDesigna" value="1">Nueva designación </input>
									</td>
									<c:if test="${entradaEnvio.anioDesignaNew != null && entradaEnvio.numeroDesignaNew != null}">								
										<td class="labelTextValor">
											<c:out value="${entradaEnvio.anioDesignaNew}" />/<c:out value="${entradaEnvio.codNewDesigna}" />
										</td>	
									</c:if>									
									<td  class="labelText">o Seleccionar Designación</input></td>
									<td>
									 	<html:text styleId="anioDesignaSelText" property="anioDesignaSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.anioDesignaSel}" disabled="true" readonly="true"></html:text>
									 	/
										<html:text styleId="numeroDesignaSelText" property="numeroDesignaSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.codSelDesigna}" disabled="true" readonly="true"></html:text>
									</td>
									<td class="labelText">
										<input type="button" class="button" id="buscarDesignacion" name="buscarDesignacion" value='Buscar' onClick="buscarDesignacion();">
									</td>
									
								</tr>
								
								<c:if test="${entradaEnvio.caso=='2' && entradaEnvio.idEstado=='4'}">
									<tr>
										<td colspan="1" ></td>
										<td colspan="5" >
										<siga:ConjCampos leyenda="Datos respuesta">
											<table class="tablaCampos" align="center">
												<c:if test="${entradaEnvio.anioDesignaSel != null}">									
													<tr>
														<td></td>
														<td  class="labelText">DESIGNACION
														</td>
														<td class="labelTextValor"><c:out value="${entradaEnvio.anioDesignaSel}"/>/<c:out value="${entradaEnvio.codSelDesigna}"/></td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.descripcionSolicitante}"/></td>
															<td>
															<img id="iconoboton_editarDesigna2" src="/SIGA/html/imagenes/beditar_off.gif"
																style="cursor: hand;" alt="Editar" name="consultar_1"
																border="0" onClick="editarDesignacion();"> 
															<img id="iconoboton_borrarDesigna2" src="/SIGA/html/imagenes/bborrar_off.gif"
																style="cursor: hand;" alt="Borrar" name="consultar_1"
																border="0" onClick="borrarRelacionDesignacion();">
														</td>
														<td></td>
													</tr>
													<c:if test="${entradaEnvio.abogadoDesignaSel != null}">
														<tr>
															<td></td>
															<td  class="labelText">
															</td>
															<td  class="labelText">Abogado</td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.abogadoDesignaSel}"/></td>
															<td></td>
															<td></td>
														</tr>
													</c:if>	
													<c:if test="${entradaEnvio.procuradorDesignaSel != null}">
														<tr>
															<td></td>
															<td  class="labelText">
															</td>
															<td  class="labelText">Procurador</td>
															<td class="labelTextValor"><c:out value="${entradaEnvio.procuradorDesignaSel}"/></td>
															<td></td>
															<td></td>
														</tr>
													</c:if>	
												</c:if>
											</table>
										</siga:ConjCampos>
									</td>
								</c:if>
								<!-- si el estado es RECIBIDO SE LE PERMITE SELECCIONAR -->
		
							</table>
						</siga:ConjCampos></td>
					</tr>
				</c:when>
				<c:when test="${entradaEnvio.idEstado=='3'}">		
					<!-- Para el estado PROCESANDO -->
					<table align="center">	
						<tr></tr><tr></tr><tr></tr><tr></tr>
						<tr></tr><tr></tr><tr></tr><tr></tr>
						<tr>
							<td class="labelText">
								<i>
								SE ESTÁ PROCENSANDO LA SOLICITUD, INTÉNTELO DE NUEVO EN UNOS MINUTOS.
								</i>
							</td>
						</tr>
					</table>	
				</c:when>
				<c:otherwise>	
					<!-- Para el estado FINALIZADO -->
					<table align="center">	
						<tr></tr><tr></tr><tr></tr><tr></tr>
						<tr></tr><tr></tr><tr></tr><tr></tr>
						<tr>
							<td class="labelText">
								<i>
									FINALIZADO
								</i>
							</td>
						</tr>
					</table>					
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${entradaEnvio.idTipoIntercambioTelematico=='05' && (entradaEnvio.idEnvioRelacionado==null || entradaEnvio.idEnvioRelacionado=='')}">		
			<tr>
				<td colspan="3">
					<c:choose>
						<c:when test="${entradaEnvio.idEstado=='1' || entradaEnvio.idEstado=='2'}">	
							<siga:ConjCampos leyenda="Acciones">
								<table class="tablaCampos" align="center">
									<tr>
										<td width="20%"></td>
										<td width="20%"></td>
										<td width="20%"></td>
										<td width="20%"></td>
										<td width="10%"></td>
									</tr>
			
								<!-- Esto hay que ocultarlo cuando ya se proceso el ejg o la designa desde la informacion de intercmabio. Esto es cuando el idcomcola de la tabla ecom_soldesignaprovisional es nulo -->
			
									<tr>
										<td colspan="5" class="labelTextValor">
											<i>
											Seleccione "Nuevo EJG" para abrir Expediente JG con los datos de la solicitud y pulse en Guardar. 
											
											O bien, seleccione un  EJG existente y pulse Guardar.
											</i>
										</td>
									</tr>							
			
									<tr>
										<td  class="labelText">
											<input type="checkbox" id="nuevoEJG" name="nuevoEJG" onclick="accionNuevoEJG()"/>Nuevo EJG 
										</td>
										
										<c:if test="${entradaEnvio.anioEJGNew != null && entradaEnvio.numeroEJGNew != null}">								
											<td class="labelTextValor">
												<c:out value="${entradaEnvio.anioEJGNew}" />/<c:out value="${entradaEnvio.numEJGNew}" />
											</td>	
										</c:if>								
										<td  class="labelText">o Seleccionar EJG</td>
										<td>
											<html:text styleId="anioEJGSelText" property="anioEJGSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.anioEJGSel}" disabled="true" readonly="true"></html:text>
											 /
											<html:text styleId="numeroEJGSelText" property="numeroEJGSelText" size="7" maxlength="10" styleClass="box" style="margin-top: 3px;" value="${entradaEnvio.numEJGSel}" disabled="true" readonly="true"></html:text>
										</td>
										<td  class="labelText">
											<input type="button" class="button" id="buscarEJG" name="buscarEJG" value='Buscar' onClick="buscarEJG();">
										</td>
										<td></td>
									</tr>
								</table>
							</siga:ConjCampos>
						</c:when>
						<c:when test="${entradaEnvio.idEstado=='3'}">								
							<!-- Para el estado PROCESANDO -->
							<table align="center">	
								<tr></tr><tr></tr><tr></tr><tr></tr>
								<tr></tr><tr></tr><tr></tr><tr></tr>
								<tr>
									<td class="labelText">
										<i>
										SE ESTÁ PROCENSANDO LA RESPUESTA, INTÉNTELO DE NUEVO EN UNOS MINUTOS.
										</i>
									</td>
								</tr>
							</table>	
						</c:when>
						<c:when test="${entradaEnvio.idEstado=='5'}">
							<siga:ConjCampos leyenda="Datos respuesta">
								<table class="tablaCampos" align="center">
									<tr>
										<td class="labelText">EJG
										</td>
										<td  class="labelTextValor"><c:out value="${entradaEnvio.anioEJGSel}" />/<c:out value="${entradaEnvio.numEJGSel}" /></td>
										<td class="labelTextValor"><c:out value="${entradaEnvio.descripcionSolicitante}"/></td>
										<td></td>
										<td></td>
									</tr>
								</table>
							</siga:ConjCampos>
						</c:when>					
					</c:choose>
				</td>
			</tr>
		</c:when>	
	</c:choose>
</table>
<siga:ConjBotonesAccion botones="${entradaEnvio.botonesDetalle}" clase="botonesDetalle" />

	<!-- formulario para consultar/editar información del envío relacionado -->
	<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
	    <html:hidden styleId = "modo" 			property = "modo"/>
		<html:hidden styleId = "hiddenFrame"  	property = "hiddenFrame"  	value = "1"/>
		<html:hidden styleId = "idEnvio"		property = "idEnvio" 		value="${entradaEnvio.idEnvioRelacionado}"/>
		<html:hidden styleId = "idTipoEnvio"  	property = "idTipoEnvio"/>
		<html:hidden styleId = "datosInforme"  	property = "datosInforme" 	value='idInstitucion==${entradaEnvio.idInstitucion}##anio==${entradaEnvio.anioDesignaSel}##idTurno==${entradaEnvio.idTurnoDesignaSel}##numero==${entradaEnvio.numeroDesignaSel}##idTipoInforme==OFICI%%%'/>
		<html:hidden styleId = "idEstadoEnvio" 	property = "idEstado"/>
		<input type="hidden" id="actionModal"  name="actionModal" value="">
	</html:form>


	<!-- formulario para buscar Tipo SJCS -->
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
	</html:form>	

	<!-- formulario para editar EJG -->	
	<html:form action="/JGR_EJG" method="POST" target="mainWorkArea">
		<html:hidden styleId = "modo" 			property = "modo"  />
		<html:hidden styleId = "anio" 			property = "anio"  			value="${entradaEnvio.anioEJGSel}"/>
		<html:hidden styleId = "numero" 		property = "numero"  		value="${entradaEnvio.numeroEJGSel}"/>
		<html:hidden styleId = "idTipoEJG" 		property = "idTipoEJG" 		value="${entradaEnvio.idTipoEJGSel}"/>
		<html:hidden styleId = "idInstitucion" 	property = "idInstitucion" 	value="${entradaEnvio.idInstitucion}"/>
	</html:form>	

	<!-- Designas -->		
	<html:form action = "/JGR_MantenimientoDesignas.do" method="POST" target="mainWorkArea">
		<html:hidden styleId = "modo"  		property ="modo" />
		<html:hidden styleId = "anio"  		property ="anio" 		value="${entradaEnvio.anioDesignaSel}"/>
		<html:hidden styleId = "numero"		property ="numero" 		value="${entradaEnvio.numeroDesignaSel}"/>
		<html:hidden styleId = "idTurno" 	property ="idTurno" 	value="${entradaEnvio.idTurnoDesignaSel}"/>
		<html:hidden styleId = "desdeEjg"	property ="desdeEjg"	value= "si"/>
	</html:form>		

	<html:form action = "/JGR_Designas.do" method="POST" target="resultado">
		<html:hidden styleId = "modo" 				property = "modo" value = "inicio"/>
		<html:hidden styleId = "actionModal" 		property ="actionModal" value = ""/>
		<html:hidden styleId = "ncolegiado" 		property="ncolegiado" name="BuscarDesignasForm" value=""/>
		<html:hidden styleId = "seleccionarTodos" 	property="seleccionarTodos" />
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
	</html:form>		

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		
		<script language="JavaScript">		
			function refrescarLocal(){			
				document.forms['EntradaEnviosForm'].modo.value = 'ver';
				document.forms['EntradaEnviosForm'].submit();		
			}	

			function accionComunicar(){			
				document.forms['DefinirEnviosForm'].idTipoEnvio.value='6';
				document.forms['DefinirEnviosForm'].idEnvio.value=${entradaEnvio.idEnvio};
				document.forms['DefinirEnviosForm'].target="submitArea";	   	
				document.forms['DefinirEnviosForm'].modo.value='respuestaTelematica';
				document.forms['DefinirEnviosForm'].submit();
			}

			function inicio(){
				//Se filtra por tipo de intercambio
				if (${entradaEnvio.idTipoIntercambioTelematico=='06'}){ 
					if (${entradaEnvio.idEstado=='1' || entradaEnvio.idEstado=='2'}){
						document.getElementById("radioAccionEJG").checked = "checked";
						document.getElementById("nuevoEJG").checked = "checked";
						accionRadio();
	
						//Se creo anteriormente
						if(${entradaEnvio.anioEJGNew != null}){
							document.getElementById("nuevoEJG").disabled = "disabled";
							jQuery('#nuevoEJG').removeAttr("checked");
						} 
	
						if(${entradaEnvio.anioDesignaNew != null}){
							document.getElementById("nuevaDesigna").disabled = "disabled";
							jQuery('#nuevaDesigna').removeAttr("checked");
						}					
						
					} else if(${entradaEnvio.idEstado=='4'}){
						if(${entradaEnvio.caso=='1'}){
							document.getElementById("radioAccionEJG").checked = "checked";
							document.getElementById("radioAccionDesigna").disabled = "disabled";
							if(${entradaEnvio.anioEJGNew != null}){
								document.getElementById("nuevoEJG").checked = "checked";
							}
	
							//Se actualizan los checks
							accionRadio();
	
							//Si ya se ha seleccionado un EJG, se deshabilita la busqueda
							if(${entradaEnvio.anioEJGSel != null}){
								document.getElementById("buscarEJG").disabled = "disabled";
								document.getElementById("nuevoEJG").disabled = "disabled";
								jQuery('#nuevoEJG').removeAttr("checked");
							}
	
						}else if(${entradaEnvio.caso=='2'}){
							document.getElementById("radioAccionDesigna").checked = "checked";
							document.getElementById("radioAccionEJG").disabled = "disabled";
							if(${entradaEnvio.anioDesignaNew != null}){
								document.getElementById("nuevaDesigna").checked = "checked";
							}
	
							//Se actualizan los checks
							accionRadio();
	
							//Si ya se ha seleccionado una designa, se deshabilita la busqueda
							if(${entradaEnvio.anioDesignaSel != null}){
								document.getElementById("buscarDesignacion").disabled = "disabled";
								document.getElementById("nuevaDesigna").disabled = "disabled";
								jQuery('#nuevaDesigna').removeAttr("checked");
							}						
						}
					}
					
				}else{
					if (${entradaEnvio.idEstado=='1' || entradaEnvio.idEstado=='2'}){
						if(${entradaEnvio.idEnvioRelacionado==null || entradaEnvio.idEnvioRelacionado==''}){
							document.getElementById("nuevoEJG").checked = "checked";
						}
					}
				}
			}
			
			function accionGuardar(){
				//Se filtra por tipo de intercambio
				if (${entradaEnvio.idTipoIntercambioTelematico=='06'}){ 
					if(document.getElementById("radioAccionEJG").checked){ //CASO 1
						document.forms['EntradaEnviosForm'].caso.value = "1";
						if(document.getElementById("nuevoEJG").checked){ //Camino 1
							alert("La petición de Nuevo EJG se está procesando, vuelva a acceder en breves minutos");
							document.forms['EntradaEnviosForm'].modo.value = 'procesar';
							document.forms['EntradaEnviosForm'].submit();
						}else{	//Camino 2-> avisar si no se ha seleccionado ningun EJG(campos vacios)									
							if(document.getElementById("anioEJGSelText").value != null && document.getElementById("anioEJGSelText").value != ''){
								//Se comprueba si hay combo de designas o no
								if(document.getElementById("designacionesRelacionadas") && document.getElementById("designacionesRelacionadas").value != null && document.getElementById("designacionesRelacionadas").value != ''){
									var designaSel = document.getElementById("designacionesRelacionadas").value.split(",");
									document.forms['EntradaEnviosForm'].anioDesignaSel.value = designaSel[0];
									document.forms['EntradaEnviosForm'].idInstitucion.value = designaSel[1];
									document.forms['EntradaEnviosForm'].idTurnoDesignaSel.value = designaSel[2];
									document.forms['EntradaEnviosForm'].numeroDesignaSel.value = designaSel[3];
								}
								document.forms['EntradaEnviosForm'].anioEJGSel.value = document.getElementById("anioEJGSelText").value;
								document.forms['EntradaEnviosForm'].numEJGSel.value = document.getElementById("numeroEJGSelText").value;
								document.forms['EntradaEnviosForm'].modo.value = 'modificar';
								document.forms['EntradaEnviosForm'].submit();
	
							}else{
								alert("Debe seleccionar un EJG para poder procesar la solicitud");	
							}
						}
	
					}else if(document.getElementById("radioAccionDesigna").checked){ //CASO 2
						document.forms['EntradaEnviosForm'].caso.value = "2";									
						if(document.getElementById("nuevaDesigna").checked){//Camino 3		
							document.forms['BuscarDesignasForm'].modo.value = "nuevo";
							var resultado=ventaModalGeneral(document.forms['BuscarDesignasForm'].name,"M");
							if (resultado!=null && resultado[0]=='MODIFICADO'){
								document.forms['EntradaEnviosForm'].numeroDesignaNew.value = resultado[1];
								document.forms['EntradaEnviosForm'].idTurnoDesignaNew.value = resultado[2];
								document.forms['EntradaEnviosForm'].idInstitucion.value = resultado[3];
								document.forms['EntradaEnviosForm'].anioDesignaNew.value = resultado[4];
								document.forms['EntradaEnviosForm'].abogadoDesignaSel.value = resultado[5];
								document.forms['EntradaEnviosForm'].fechaApertura.value = resultado[6];
								alert("La petición de Nueva Designación se está procesando, vuelva a acceder en breves minutos");
								document.forms['EntradaEnviosForm'].modo.value = 'procesar';
								document.forms['EntradaEnviosForm'].submit();
							}
							
						}else{	//Camino 4  -> avisar si no se ha seleccionado ninguna designa(campos vacios)									
							if(document.getElementById("anioDesignaSelText").value != null && document.getElementById("anioDesignaSelText").value != ''){
								document.forms['EntradaEnviosForm'].modo.value = 'modificar';
								document.forms['EntradaEnviosForm'].anioDesignaSel.value = document.getElementById("anioDesignaSelText").value;
								document.forms['EntradaEnviosForm'].codSelDesigna.value = document.getElementById("numeroDesignaSelText").value;	
								document.forms['EntradaEnviosForm'].submit();
							}else{
								alert("Debe seleccionar una designación para poder procesar la solicitud");	
							}
						}
					}

				} else if (${entradaEnvio.idTipoIntercambioTelematico=='05'}){
					if(document.getElementById("nuevoEJG").checked){ //Camino 1
						alert("La petición de Nuevo EJG se está procesando, vuelva a acceder en breves minutos");
						document.forms['EntradaEnviosForm'].modo.value = 'procesar';
						document.forms['EntradaEnviosForm'].submit();
					}else{										
						if(document.getElementById("anioEJGSelText").value != null && document.getElementById("anioEJGSelText").value != ''){
							document.forms['EntradaEnviosForm'].anioEJGSel.value = document.getElementById("anioEJGSelText").value;
							document.forms['EntradaEnviosForm'].numEJGSel.value = document.getElementById("numeroEJGSelText").value;
							document.forms['EntradaEnviosForm'].modo.value = 'modificar';
							document.forms['EntradaEnviosForm'].submit();

						}else{
							alert("Debe seleccionar un EJG para poder procesar la solicitud");	
						}
					}
				} 
			}

			function accionDownload (){			
				document.forms['EntradaEnviosForm'].modo.value = 'descargar';
				document.forms['EntradaEnviosForm'].submit();			
			}

			function accionVolver (){			
				history.back();
			}

			function accionRadio(){			
				if(document.getElementById("radioAccionDesigna").checked){
					document.getElementById("nuevoEJG").disabled = "disabled";
					document.getElementById("anioEJGSelText").disabled = "disabled";
					document.getElementById("numeroEJGSelText").disabled = "disabled";
					document.getElementById("anioEJGSelText").value="";
					document.getElementById("numeroEJGSelText").value="";
					document.getElementById("buscarEJG").disabled = "disabled";
					jQuery('#nuevaDesigna').removeAttr("disabled");
					jQuery('#nuevoEJG').removeAttr("checked");
					jQuery('#anioDesignaSelText').removeAttr("disabled");
					jQuery('#numeroDesignaSelText').removeAttr("disabled");
					jQuery('#buscarDesignacion').removeAttr("disabled");

				}else if(document.getElementById("radioAccionEJG").checked){
					document.getElementById("nuevaDesigna").disabled = "disabled";
					document.getElementById("anioDesignaSelText").disabled = "disabled";
					document.getElementById("numeroDesignaSelText").disabled = "disabled";
					document.getElementById("anioDesignaSelText").value="";
					document.getElementById("numeroDesignaSelText").value="";					
					document.getElementById("buscarDesignacion").disabled = "disabled";
					jQuery('#nuevoEJG').removeAttr("disabled");
					jQuery('#nuevaDesigna').removeAttr("checked");					
					jQuery('#anioEJGSelText').removeAttr("disabled");
					jQuery('#numeroEJGSelText').removeAttr("disabled");
					jQuery('#buscarEJG').removeAttr("disabled");

				}

				//Por si ya se selecciono anteriormente nueva EJG/designa
				if (${entradaEnvio.idTipoIntercambioTelematico=='06' && entradaEnvio.idEstado=='2'}){
					if(${entradaEnvio.anioEJGNew != null}){
						document.getElementById("nuevoEJG").disabled = "disabled";
						jQuery('#nuevoEJG').removeAttr("checked");
					} 

					if(${entradaEnvio.anioDesignaNew != null}){
						document.getElementById("nuevaDesigna").disabled = "disabled";
						jQuery('#nuevaDesigna').removeAttr("checked");
					}		
				}				
			}

			function accionNuevoEJG(){			
				if(document.getElementById("nuevoEJG").checked){
					document.getElementById("anioEJGSelText").disabled = "disabled";
					document.getElementById("numeroEJGSelText").disabled = "disabled";
					document.getElementById("anioEJGSelText").value="";
					document.getElementById("numeroEJGSelText").value="";
				}
			}			
			
			function buscarDesignacion(){			
				document.BusquedaPorTipoSJCSForm.tipo.value="DESIGNA";
				var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	
				if (resultado != null && resultado.length >= 4) {
					document.forms['EntradaEnviosForm'].idInstitucion.value=resultado[0];
					document.getElementById("anioDesignaSelText").value = resultado[1]; 
					document.forms['EntradaEnviosForm'].numeroDesignaSel.value=resultado[2];
					document.forms['EntradaEnviosForm'].idTurnoDesignaSel.value=resultado[3];
					document.getElementById("numeroDesignaSelText").value=resultado[4];
					jQuery('#nuevaDesigna').removeAttr("checked");	
				}
			}			
		
			function buscarEJG(){			
				document.BusquedaPorTipoSJCSForm.tipo.value="EJG";
				var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	
				if (resultado != null && resultado.length >= 4) {
					document.forms['EntradaEnviosForm'].idInstitucion.value=resultado[0];
					document.getElementById("anioEJGSelText").value=resultado[1];
					document.forms['EntradaEnviosForm'].numeroEJGSel.value=resultado[2];
					document.forms['EntradaEnviosForm'].idTipoEJGSel.value=resultado[3];
					document.getElementById("numeroEJGSelText").value=resultado[4];
					jQuery('#nuevoEJG').removeAttr("checked");
				}			
			}

			function editarEJG(){
				document.forms['DefinirEJGForm'].modo.value='editar';
				document.forms['DefinirEJGForm'].submit();
			}

			function borrarRelacionEJG(){
				if(("${entradaEnvio.anioEJGNew}" != null && "${entradaEnvio.anioEJGNew}" != '') &&
				   ("${entradaEnvio.anioEJGNew}" == "${entradaEnvio.anioEJGSel}" && "${entradaEnvio.numeroEJGNew}" == "${entradaEnvio.numeroEJGSel}")){
					document.forms['EntradaEnviosForm'].modo.value='abrirModalOpcionBorrado';
					var resultado=ventaModalGeneral(document.forms['EntradaEnviosForm'].name,"XS");
					if(resultado=='BORRAR_RELACION'){ //Solamente se borra la relacion con el EJG
						document.forms['EntradaEnviosForm'].modo.value = 'borrarRelacionEJG';
						document.forms['EntradaEnviosForm'].submit();
						
					} else if(resultado=='BORRAR_TODO'){ //Se borra tanto relación como EJG 				
						document.forms['EntradaEnviosForm'].anioEJGNew.value = "${entradaEnvio.anioEJGNew}";
						document.forms['EntradaEnviosForm'].idTipoEJGNew.value = "${entradaEnvio.idTipoEJGNew}";
						document.forms['EntradaEnviosForm'].numeroEJGNew.value = "${entradaEnvio.numeroEJGNew}";
						document.forms['EntradaEnviosForm'].modo.value = 'borrarRelacionEJG';
						document.forms['EntradaEnviosForm'].submit();
					}
				}else{
					var type = '¿Desea eliminar la relación con el EJG (no se borrará el registro del EJG, solamente la relación)?';
					if(confirm(type)){
						document.forms['EntradaEnviosForm'].modo.value = 'borrarRelacionEJG';
						document.forms['EntradaEnviosForm'].submit();
					}
				}
			}			

			function consultarEnvioRelacionado(){
				document.forms['DefinirEnviosForm'].modo.value='verRelacionEnvio';
				document.forms['DefinirEnviosForm'].submit();
			}

			function editarEnvioRelacionado(){
				document.forms['DefinirEnviosForm'].modo.value='editarRelacionEnvio';
				document.forms['DefinirEnviosForm'].submit();
			}

			function editarDesignacion(){
				document.forms['MaestroDesignasForm'].modo.value='editar';
				document.forms['MaestroDesignasForm'].submit();
			}

			function borrarRelacionDesignacion(){
				if(("${entradaEnvio.anioDesignaNew}" != null && "${entradaEnvio.anioDesignaNew}" != '') &&
				   ("${entradaEnvio.anioDesignaNew}" == "${entradaEnvio.anioDesignaSel}" && "${entradaEnvio.numeroDesignaNew}" == "${entradaEnvio.numeroDesignaSel}")){
					document.forms['EntradaEnviosForm'].modo.value='abrirModalOpcionBorrado';
					var resultado=ventaModalGeneral(document.forms['EntradaEnviosForm'].name,"XS");
					if(resultado=='BORRAR_RELACION'){ //Solamente se borra la relacion con la designa
						document.forms['EntradaEnviosForm'].anioDesignaNew.value = "";
						document.forms['EntradaEnviosForm'].modo.value = 'borrarRelacionDesigna';
						document.forms['EntradaEnviosForm'].submit();
						
					} else if(resultado=='BORRAR_TODO'){ //Se borra tanto relación como designa 				
						document.forms['EntradaEnviosForm'].anioDesignaNew.value = "${entradaEnvio.anioDesignaNew}";
						document.forms['EntradaEnviosForm'].idTurnoDesignaNew.value = "${entradaEnvio.idTurnoDesignaNew}";
						document.forms['EntradaEnviosForm'].numeroDesignaNew.value = "${entradaEnvio.numeroDesignaNew}";
						document.forms['EntradaEnviosForm'].modo.value = 'borrarRelacionDesigna';
						document.forms['EntradaEnviosForm'].submit();
					}
				}else{
					var type = '¿Desea eliminar la relación con la designación (no se borrará el registro de la designación, solamente la relación)?';
					if(confirm(type)){				
						document.forms['EntradaEnviosForm'].modo.value='borrarRelacionDesigna';
						document.forms['EntradaEnviosForm'].submit();
					}
				}
				
			}			

		</script>
		
	</body>
</html>

