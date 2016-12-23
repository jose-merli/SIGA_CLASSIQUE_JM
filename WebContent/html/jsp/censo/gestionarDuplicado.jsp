<!DOCTYPE html>
<html>
<head>
<!-- gestionarDuplicado.jsp -->

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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	
	String idInstitucionLocation = usrbean.getLocation();
	
	Hashtable datos = (Hashtable)request.getAttribute("datos");
	String idPersona0 =	((CenPersonaBean) ((ArrayList) datos.get("datosPersonales")).get(0)).getIdPersona().toString();
	String idPersona1 =	((CenPersonaBean) ((ArrayList) datos.get("datosPersonales")).get(1)).getIdPersona().toString();
	
	ArrayList informacionColegiacionesAmbas = ((ArrayList) datos.get("informacionColegiacionesAmbas"));
	
	Hashtable  Hashtable0 = (Hashtable) informacionColegiacionesAmbas.get(0);
	Hashtable  Hashtable1 = (Hashtable) informacionColegiacionesAmbas.get(1);
	
	String estadoColegiaciones0 = (String) Hashtable0.get("colegiacion");
	String estadoColegiaciones1 =  (String) Hashtable1.get("colegiacion");
	
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">




	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<style>
			.ocultar {display:none}
		</style>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
		
		
		<siga:Titulo 
			titulo="censo.busquedaDuplicados.titulo.fusion" 
			localizacion="censo.busquedaDuplicados.localizacion"/>
			
		<siga:TituloExt titulo="censo.busquedaDuplicados.titulo" localizacion="censo.busquedaDuplicados.titulo.fusion"/>
</head>

<body class="tablaCentralCampos">
	
	<html:form action="/CEN_MantenimientoDuplicados.do" method="POST" target="mainWorkArea">
		<html:hidden property = "idPersonaOrigen" value = ""/>
		<html:hidden property = "idPersonaDestino" value = ""/>
		<html:hidden property = "idInstOrigen" value = ""/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "listaDirecciones" value=""/>
		<html:hidden property = "listaDireccionesNoSeleccionadas" value=""/>
		<html:hidden property = "listaEstados" value=""/>
		<html:hidden property = "listaEstadosNoSeleccionados" value=""/>
		<html:hidden property = "tablaDatosDinamicosD" value=""/>
		<html:hidden property = "filaSelD" value=""/>
		<input type="hidden" id="verFichaLetrado"  name="verFichaLetrado" value="">
		<input type="hidden" id="volver"  name="volver" value="MD">
		
	
		<bean:define id="data" name="datos" scope="request"/>
	
		<c:choose>
			<c:when test="${empty data}">
			<div class="notFound">
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
			</div>
			</c:when>
			
			<c:otherwise>		
			<div id="divPersona" name="divScroll" style="overflow-y:scroll; height:150px;">
				<siga:ConjCampos leyenda="censo.fusionDuplicados.datosPersonales.titulo">
					<table width="100%" style="vertical-align:top"> 
						<tr>
						
							<!-- Etiquetas de campos -->
							<td width="26%">
								<table>
									<tr>
										<td class="labelText">
											Identificación | Última Modificación
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											Apellido 1 Apellido 2, Nombre
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<b>>> Modificar datos >></b>
										</td>
									</tr>
									
									<tr>
										<!-- Aqui se meten los radio buttons para seleccionar el colegiado base -->
										<td class="labelText">
											<b>Seleccione cuál será el destino</b>
											<br>
											(datos generales que se conservarán)
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datos.datosPersonales}" var="datosPersona" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosPersona.NIFCIF}"/>
											| <c:out value="${datosPersona.fechaMod}"/>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosPersona.apellido1}"/>&nbsp;
											<c:out value="${datosPersona.apellido2}"/>,&nbsp; 
											<c:out value="${datosPersona.nombre}"/>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:choose>
												<c:when test="${status.count == 1}">
													<c:set var="estado" value="<%=estadoColegiaciones0%>" />
													<c:choose>
															<c:when test="${estado=='L'}">
																<img id="iconoboton_informacionLetrado1" src="/SIGA/html/imagenes/binformacionLetrado_off.gif" 
																	style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
																	 title="Acceso a ficha" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',1); " >
															</c:when>
															<c:when test="${estado=='NC'}">
																	<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_on.gif" 
																style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
																 title="consultar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}',''); " >
															<img id="iconoboton_editar10" src="/SIGA/html/imagenes/beditar_off.gif" 
																style="cursor:pointer;" alt="Editar" class="botonesIcoTabla" name="iconoFila"
																 title="Editar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',0); " >
															</c:when>
															<c:when test="${estado=='C'}">
																No hay colegiaciones
															</c:when>
															
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:set var="estado" value="<%=estadoColegiaciones1%>" />
													<c:choose>
														<c:when test="${estado=='L'}">
															<img id="iconoboton_informacionLetrado1" src="/SIGA/html/imagenes/binformacionLetrado_off.gif" 
																	style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
																	 title="Acceso a ficha" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',1); " >
														</c:when>
														<c:when test="${estado=='NC'}">
															<img id="iconoboton_consultar2" src="/SIGA/html/imagenes/bconsultar_on.gif" 
																style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
																 title="Consultar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}',''); " >
															<img id="iconoboton_editar11" src="/SIGA/html/imagenes/beditar_off.gif" 
																style="cursor:pointer;" alt="Editar" class="botonesIcoTabla" name="iconoFila"
																 title="editar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',0); " >
															</c:when>
														<c:when test="${estado=='C'}">
																No hay colegiaciones
															</c:when>
															
													</c:choose>
												</c:otherwise>
											</c:choose>
											
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<input name="idPersonaSel" value="${datosPersona.idPersona}" type="radio" onclick="seleccionar('${datosPersona.idPersona}');"/>
										</td>
									</tr>
								</table>
							</td>
							</c:forEach>
						
						</tr>
					</table>
				</siga:ConjCampos>
			</div>
			
			<hr style="color:black;"></hr>
			
			<!-- INI Otros datos -->
			<div id="divGeneral" name="divScroll" style="overflow-y:scroll; height:550px;">
				
				<siga:ConjCampos leyenda="Otros datos">
					<table width="100%">
						<tr>
							<td width="26%">
								<table>
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.fusionDuplicados.datosPersonales.fechaNacimiento"/>&nbsp;|&nbsp;
											<siga:Idioma key="censo.fusionDuplicados.datosPersonales.lugarNacimiento"/>
										</td>
									</tr>

									<tr>
										<td class="labelText">
											Sexo | Estado civil
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datos.datosPersonales}" var="datosPersona" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosPersona.fechaNacimiento}"/>
											<c:if test="${datosPersona.fechaNacimiento==''}">
												<i>[<strike>fecha de nacimiento</strike>]</i>
											</c:if>
											 | <c:out value="${datosPersona.naturalDe}"/>
											<c:if test="${datosPersona.naturalDe==''}">
												<i>[<strike>lugar de nacimiento</strike>]</i>
											</c:if>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:if test="${datosPersona.fallecido=='1'}">
												<b><c:out value="(Fallecido)"/></b> &nbsp;
											</c:if>
											<c:out value="${datosPersona.sexoStr}"/>
											<c:if test="${datosPersona.sexoStr==''}">
												<i>[<strike>sexo</strike>]</i>
											</c:if>
											 | <c:out value="${datosPersona.idEstadoCivilStr}"/>
											<c:if test="${datosPersona.idEstadoCivilStr==null}">
												<i>[<strike>estado civil</strike>]</i>
											</c:if>
										</td>
									</tr>
								</table>
							</td>
							</c:forEach>
						
						</tr>
						<tr>
							<td width="26%">
								<table>
									<tr>
										<td class="labelText">
											Sanciones y Certificados
										</td>
									</tr>
																								
									<tr>
										<td class="labelText"> 
											Tratamiento 
											| Idioma
	 									</td>
									</tr>
									
									<tr>
										<td class="labelText"> 
											Otros checks
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datos.datosClienteCGAE}" var="datosCliente" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelText">
											<c:out value="${datosCliente.sanciones}"/> sanciones y 
											<c:out value="${datosCliente.certificados}"/> certificados
										</td>
									</tr>
																								
									<tr>
										<td class="labelText"> 
											<c:out value="${datosCliente.idTratamientoStr}"/>
											<c:if test="${datosCliente.idTratamientoStr==''}">
												<i>[<strike>tratamiento</strike>]</i>
											</c:if>
											| <c:out value="${datosCliente.idLenguajeStr}"/>
											<c:if test="${datosCliente.idLenguajeStr==''}">
												<i>[<strike>idioma</strike>]</i>
											</c:if>
	 									</td>
									</tr>
									
									<tr>
										<td class="labelText"> 
											<!-- Este campo solo se muestra en CGAE -->
											<c:if test="${datosCliente.noEnviarRevista=='1'}">
												<i><b><siga:Idioma key="messages.letrados.noRevistaCGAE"/></b></i> |
											</c:if>
											<c:if test="${datosCliente.noAparacerRedAbogacia=='1'}">
												<!-- Este mensaje es diferente entre CGAE y Colegios -->
												<i><b><siga:Idioma key="messages.letrados.noApareceRedAbogacia"/></b></i>
											</c:if>
										</td>
									</tr>
								</table>
							</td>
							</c:forEach>
						</tr>
					</table>
				</siga:ConjCampos>
				<!-- FIN Otros datos -->
				
				<!-- INI Colegiaciones iguales -->
				<c:forEach items="${datos.datosColegialesIguales}" var="datosColUnica" varStatus="status">
				
				<c:if test="${datosColUnica.fechaProduccion!=''}">
					<c:set var="institucionColegiacion" value="${datosColUnica.institucionColegiacion} (autogestión)"/>
				</c:if>
				<c:if test="${datosColUnica.fechaProduccion==''}">
					<c:set var="institucionColegiacion" value="${datosColUnica.institucionColegiacion}"/>
				</c:if>
				
				<br/>
				<siga:ConjCampos leyenda="Coincidencia - ${institucionColegiacion}">
				
					<!-- INI Datos colegiacion comun -->
					<table width="100%">
					<tr>
						<td width="26%"> 
							<table>
								<tr>
									<td class="labelText">
										Num. col. | Fecha Inc. | Inscr. y Resid. 
									</td>
								</tr>
							</table>
						</td>
						
						<c:forEach items="${datosColUnica.datosColegiacion}" var="datosCol" varStatus="status">
						<td width="37%">
							<table>
								<tr>
									<td class="labelText" colspan="3">
										<c:out value="${datosCol.NColegiado}"/><c:out value="${datosCol.NComunitario}"/>
										| <c:out value="${datosCol.fechaIncorporacion}"/>
								
										<c:if test="${datosCol.comunitario=='1'}">
										| <b><c:out value="Inscrito"/></b> &nbsp;
										</c:if>
										
										|
										<c:choose>
											<c:when test="${datosCol.situacionResidente=='1'}">
												<c:out value="Residente"/>
											</c:when>
											<c:otherwise>
												<c:out value="No Residente"/>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</table>
						</td>
						</c:forEach>
					</tr>
								
					<tr>
						<td width="26%"> 
							<table>
								<tr>
									<td class="labelText">
										Histórico de estados 
									</td>
								</tr>
							</table>
						</td>
						
						<c:forEach items="${datosColUnica.historicoEstadosColegiacion}" var="datosCol" varStatus="status">
						<td width="37%">
							<table>
								<c:forEach items="${datosCol}" var="histEstados"  varStatus="status">
								<tr>
									<td class="labelTextValue">
										<input type="checkBox" name="checkEstado" id="${histEstados.row.IDPERSONA}" value="${histEstados.row.IDINSTITUCION}&&${histEstados.row.IDPERSONA}&&${histEstados.row.FECHAESTADO}" checked/>
									</td>
									<td class="labelText">
										<c:out value="${histEstados.row.DESCRIPCION}"/>
									</td>
									<td class="labelText">
										desde <c:out value="${histEstados.row.FECHAESTADO_SPANISH}"/>
									</td>
								</tr>															
								</c:forEach>
							</table>
						</td>
						</c:forEach>
					</tr>
					</table>
					<!-- FIN Datos colegiacion comun -->

					<!-- INI Conjunto de direcciones de cada colegiacion comun -->
					<siga:ConjCampos leyenda="Direcciones de colegio">
						<table width="100%">
						<tr>
							<!-- Etiquetas de campos -->
							<td width="26%">
								<table>
									<tr>
										<td class="labelText">
											Selección | Tipo
										</td>
									</tr>
									<tr>
										<td class="labelText">
											Domicilio 
										</td>
									</tr>
									<tr>
										<td class="labelText">
											Población | Provincia | País
										</td>
									</tr>
									<tr>
										<td class="labelText">
											Correo | Teléfonos
										</td>
									</tr>
									<tr>
										<td class="labelText">
											Última modificación
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<b>Prevalecerán los tipos y las preferencias de envío de la persona destino</b>
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datosColUnica.direcciones}" var="datosColDir" varStatus="status">
							<td width="37%">
								<table>
									<c:forEach items="${datosColDir}" var="datosDir"  varStatus="status">
									<tr>
										<td class="labelTextValue">
											<input type="checkBox" name="checkDireccion" id="${datosDir.IDPERSONA}" value="${datosDir.IDINSTITUCION}&&${datosDir.IDPERSONA}&&${datosDir.IDDIRECCION}" checked/>
											<c:out value="${datosDir.TIPOSDIRECCION}"/>
											<c:if test="${datosDir.TIPOSDIRECCION==''}">
												<i>[<strike>tipos</strike>]</i>
											</c:if>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosDir.DOMICILIO}"/>
											<c:if test="${datosDir.DOMICILIO==''}">
												<i>[<strike>domicilio</strike>]</i>
											</c:if>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosDir.POBLACION}"/>
											<c:if test="${datosDir.POBLACION==''}">
												<i>[<strike>población</strike>]</i>
											</c:if>
											 | <c:out value="${datosDir.PROVINCIA}"/>
											<c:if test="${datosDir.PROVINCIA==''}">
												<i>[<strike>provincia</strike>]</i>
											</c:if>
											 | <c:out value="${datosDir.PAIS}"/>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosDir.CORREOELECTRONICO}"/>
											<c:if test="${datosDir.CORREOELECTRONICO==''}">
												<i>[<strike>correo electrónico</strike>]</i>
											</c:if>
											<c:if test="${datosDir.TELEFONO1!=''}">
											| <c:out value="${datosDir.TELEFONO1}"/>
											</c:if>
											<c:if test="${datosDir.TELEFONO2!=''}">
											| <c:out value="${datosDir.TELEFONO2}"/>
											</c:if>
											<c:if test="${datosDir.MOVIL!=''}">
											| <c:out value="${datosDir.MOVIL}"/>
											</c:if>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:out value="${datosDir.FECHAMODIFICACION}"/>
										</td>
									</tr>
									
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>															
									</c:forEach>
									
								</table>
							</td>
							</c:forEach>
							
						</tr>
						</table>
					</siga:ConjCampos>
					<!-- FIN Conjunto de direcciones de cada colegiacion comun -->

				</siga:ConjCampos>
				</c:forEach>
				<!-- FIN Colegiaciones iguales -->
					
				<!-- INI Colegiaciones diferentes -->
				<siga:ConjCampos leyenda="censo.fusionDuplicados.colegiaciones.titulo">
 					<table width="100%">
					<tr>
						<!-- Etiquetas de campos -->
 						<td width="26%">
							<table>
								<tr>
									<td class="labelText"> 
										<siga:Idioma key="Colegio"/> 
										| Num. col. 
										| Fecha Inc. <!-- <siga:Idioma key="Fecha Incorporación"/> -->
 									</td>
								</tr>
								
								<tr>
									<td class="labelText"> 
										Situación colegial
									</td>
								</tr>
								
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
						
						<c:forEach items="${datos.datosColegiales}" var="datosCliente" varStatus="status">
 						<td width="37%">
							<table>
								<c:forEach items="${datosCliente}" var="datosCol"  varStatus="status">
								<tr>
									<td class="labelText">
										<c:out value="${datosCol.datosColegio.institucionColegiacion}"/> 
										<c:if test="${datosCol.datosColegio.fechaProduccion!=null && datosCol.datosColegio.fechaProduccion!=''}">
											<b>(autogestión)</b>
										</c:if>
										
										<c:if test="${datosCol.datosColegiacion.NColegiado!=null}">
											| <c:out value="${datosCol.datosColegiacion.NColegiado}"/><c:out value="${datosCol.datosColegiacion.NComunitario}"/>
										</c:if>
										
										| <c:out value="${datosCol.datosColegiacion.fechaIncorporacion}"/>
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<c:choose>
										<c:when test="${datosCol.datosColegiacion.NColegiado!=null}">
											<c:if test="${datosCol.datosColegiacion.comunitario!=null && datosCol.datosColegiacion.comunitario=='1'}">
												<b><c:out value="Inscrito"/></b>
												&nbsp;
											</c:if>
											
											<c:choose>
											<c:when test="${datosCol.datosColegiacion.situacionResidente!=null && datosCol.datosColegiacion.situacionResidente=='1'}">
												<c:out value="Residente"/>
											</c:when>
											
											<c:otherwise>
												<c:out value="No Residente"/>
											</c:otherwise>
											</c:choose>
											
											<c:choose>
											<c:when test="${datosCol.estadoColegiacion != null}">
											| <c:out value="${datosCol.estadoColegiacion.row.DESCRIPCION}"/>
												desde <c:out value="${datosCol.estadoColegiacion.row.FECHAESTADO_SPANISH}"/>
											</c:when>
											
											<c:otherwise>
												<i>[<strike>estado colegial</strike>]</i>
											</c:otherwise>
											</c:choose>
										</c:when>
										
										<c:otherwise>
											<i>No Colegiado</i>
										</c:otherwise>
										</c:choose>
									</td>
								</tr>
								
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>															
								</c:forEach>
							</table>
						</td>
						</c:forEach>
						
					</tr>
					</table>
				</siga:ConjCampos>
				<!-- FIN Colegiaciones diferentes -->
				
				<siga:ConjCampos leyenda="censo.fusionDuplicados.direcciones.cabecera">
				<table width="100%">				
					<tr>
						<!-- Etiquetas de campos -->
						<td width="26%">
							<table>
								<tr>
									<td class="labelText">
										Selección | Tipo
									</td>
								</tr>
								<tr>
									<td class="labelText">
										Domicilio 
									</td>
								</tr>
								<tr>
									<td class="labelText">
										Población | Provincia | País
									</td>
								</tr>
								<tr>
									<td class="labelText">
										Correo | Teléfonos
									</td>
								</tr>
								<tr>
									<td class="labelText">
										Última modif. | Colegio Origen
									</td>
								</tr>
								<tr>
									<td class="labelText">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<b>Prevalecerán los tipos y las preferencias de envío de la persona destino</b>
									</td>
								</tr>
							</table>
						</td>
						
						<c:forEach items="${datos.datosDirecciones}" var="datosCliente"  varStatus="status">
						<td width="37%">
							<table>
								<c:forEach items="${datosCliente}" var="datosDir"  varStatus="status">
								<tr>
									<td class="labelTextValue">
										<input type="checkBox" name="checkDireccion" id="${datosDir.IDPERSONA}" value="${datosDir.IDINSTITUCION}&&${datosDir.IDPERSONA}&&${datosDir.IDDIRECCION}" checked/>
										<c:out value="${datosDir.TIPOSDIRECCION}"/>
										<c:if test="${datosDir.TIPOSDIRECCION==''}">
											<i>[<strike>tipos</strike>]</i>
										</c:if>
									</td>
								</tr>
								
								<tr>
									<td class="labelTextValue">
										<c:out value="${datosDir.DOMICILIO}"/>
										<c:if test="${datosDir.DOMICILIO==''}">
											<i>[<strike>domicilio</strike>]</i>
										</c:if>
									</td>
								</tr>
								
								<tr>
									<td class="labelTextValue">
										<c:out value="${datosDir.POBLACION}"/>
										<c:if test="${datosDir.POBLACION==''}">
											<i>[<strike>población</strike>]</i>
										</c:if>
										 | <c:out value="${datosDir.PROVINCIA}"/>
										<c:if test="${datosDir.PROVINCIA==''}">
											<i>[<strike>provincia</strike>]</i>
										</c:if>
										 | <c:out value="${datosDir.PAIS}"/>
									</td>
								</tr>
								
								<tr>
									<td class="labelTextValue">
										<c:out value="${datosDir.CORREOELECTRONICO}"/>
										<c:if test="${datosDir.CORREOELECTRONICO==''}">
											<i>[<strike>correo electrónico</strike>]</i>
										</c:if>
										<c:if test="${datosDir.TELEFONO1!=''}">
										| <c:out value="${datosDir.TELEFONO1}"/>
										</c:if>
										<c:if test="${datosDir.TELEFONO2!=''}">
										| <c:out value="${datosDir.TELEFONO2}"/>
										</c:if>
										<c:if test="${datosDir.MOVIL!=''}">
										| <c:out value="${datosDir.MOVIL}"/>
										</c:if>
									</td>
								</tr>
								
								<tr>
									<td class="labelTextValue">
										<c:out value="${datosDir.FECHAMODIFICACION}"/>
										 | <c:out value="${datosDir.COLEGIOORIGEN}"/>
										<c:if test="${datosDir.COLEGIOORIGEN==''}">
											<i>[<strike>colegio origen</strike>]</i>
										</c:if>
									</td>
								</tr>
								
								<tr>
									<td class="labelTextValue">
										&nbsp;
									</td>
								</tr>
								</c:forEach>
							</table>
						</td>
						</c:forEach>
						
					</tr>
				</table>
				</siga:ConjCampos>
				
			</div>
			
			</c:otherwise>
		</c:choose>
		
		<siga:ConjBotonesAccion botones="A,V" clase="botonesDetalle" />
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			function accionVolver(){	
				document.forms[0].action = "/SIGA/CEN_MantenimientoDuplicados.do" + "?noReset=true&buscar=true";
				document.forms[0].modo.value = "abrirConParametros";
				document.forms[0].submit();
			}

			// jbd // helpers para recorrer radios y checks
			var incluido;
			var seleccionado = false;
		
			function getSelectedRadio(buttonGroup) {
			   // returns the array number of the selected radio button or -1 if no button is selected
			   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
			      for (var i=0; i<buttonGroup.length; i++) {
			         if (buttonGroup[i].checked) {
			            return i
			         }
			      }
			   
			   } else {
			      if (buttonGroup.checked) { 
			    	  return 0; 			    	 
			      } // if the one button is checked, return zero
			   }
			   // if we get to this point, no radio button is selected
			   return -1;
			}
		
			function getSelectedRadioValue(buttonGroup) {
			   // returns the value of the selected radio button or "" if no button is selected
			   var i = getSelectedRadio(buttonGroup);
			   if (i == -1) {
			      return "";
			      
			   } else {
			      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
			         return buttonGroup[i].value;
			      
			      } else { // The button group is just the one button, and it is checked
			         return buttonGroup.value;
			      }
			   }
			}
		
			function getSelectedCheckbox(buttonGroup) {
			   // Go through all the check boxes. return an array of all the ones
			   // that are selected (their position numbers). if no boxes were checked,
			   // returned array will be empty (length will be zero)
			   var retArr = new Array();
			   var lastElement = 0;
			   if (buttonGroup[0]) { // if the button group is an array (one check box is not an array)
			      for (var i=0; i<buttonGroup.length; i++) {
			         if (buttonGroup[i].checked && buttonGroup[i].disabled==false) {
			            retArr.length = lastElement;
			            retArr[lastElement] = i;
			            lastElement++;
			         }
			      }
			   
			   } else { // There is only one check box (it's not an array)
			      if (buttonGroup.checked && buttonGroup.disabled==false) { // if the one check box is checked
			         retArr.length = lastElement;
			         retArr[lastElement] = 0; // return zero as the only array value
			      }
			   }
			   return retArr;
			}
			function getNotSelectedCheckbox(buttonGroup) {
				   // Go through all the check boxes. return an array of all the ones
				   // that are selected (their position numbers). if no boxes were checked,
				   // returned array will be empty (length will be zero)
				   var retArr = new Array();
				   var lastElement = 0;
				   if (buttonGroup[0]) { // if the button group is an array (one check box is not an array)
				      for (var i=0; i<buttonGroup.length; i++) {
				         if (!buttonGroup[i].checked && buttonGroup[i].disabled==false) {
				            retArr.length = lastElement;
				            retArr[lastElement] = i;
				            lastElement++;
				         }
				      }
				   
				   } else { // There is only one check box (it's not an array)
				      if (!buttonGroup.checked && buttonGroup.disabled==false) { // if the one check box is checked
				         retArr.length = lastElement;
				         retArr[lastElement] = 0; // return zero as the only array value
				      }
				   }
				   return retArr;
				}

			function getSelectedCheckboxValue(buttonGroup, selected) {
			   // return an array of values selected in the check box group. if no boxes
			   // were checked, returned array will be empty (length will be zero)
			   var retArr = new Array(); // set up empty array for the return values
			   var selectedItems;
			   if (selected) {
			      selectedItems = getSelectedCheckbox(buttonGroup);
			   } else {
				  selectedItems = getNotSelectedCheckbox(buttonGroup);
			   }
			   if (selectedItems.length != 0) { // if there was something selected
			      retArr.length = selectedItems.length;
			      for (var i=0; i<selectedItems.length; i++) {
			         if (buttonGroup[selectedItems[i]]) { // Make sure it's an array
			            retArr[i] = buttonGroup[selectedItems[i]].value;
			         } else { // It's not an array (there's just one check box and it's selected)
			            retArr[i] = buttonGroup.value;// return that value
			         }
			      }
			   }
			   return retArr;
			}

			function seleccionarChecks(buttonGroup, id){
	
				if (buttonGroup[0]) { // Cogemos el grupo de checks
				      for (var i=0; i<buttonGroup.length; i++) {
					      if(buttonGroup[i].id==id){
					      		buttonGroup[i].checked=true;
					      		buttonGroup[i].disabled=true;
					      }else{
					    	  	buttonGroup[i].disabled=false;
					      }
				      }
			   } else {
				   if(buttonGroup.id==id){
			      		buttonGroup.checked=true;
			      		buttonGroup.disabled=true;
			      }else{
			    	  	buttonGroup.checked=false;
			    	  	buttonGroup.disabled=false;
			      }
			   }
			}
			// jbd // fin helpers
			
			function accionAceptar(){
				if (seleccionado){
					<%if(idPersona1.equalsIgnoreCase(idPersona0)){%>
						alert("<siga:Idioma key="censo.fusionDuplicados.error.mismaPersona"/>");
					<%}else{%>
						sub();
						if(document.forms[0].checkDireccion){
							document.forms[0].listaDirecciones.value = getSelectedCheckboxValue(document.forms[0].checkDireccion, true);
							document.forms[0].listaDireccionesNoSeleccionadas.value = getSelectedCheckboxValue(document.forms[0].checkDireccion, false);
						}
						if(document.forms[0].checkEstado){
							document.forms[0].listaEstados.value = getSelectedCheckboxValue(document.forms[0].checkEstado, true);
							document.forms[0].listaEstadosNoSeleccionados.value = getSelectedCheckboxValue(document.forms[0].checkEstado, false);
						}
						if(confirm("Se van a combinar los datos a una sola persona.")){
							alert("Este proceso puede durar varios minutos. Por favor, espere...");
							document.forms[0].modo.value = "aceptar";
							document.forms[0].target="submitArea";	
						 	document.forms[0].submit();
						}else{
							fin();
						}
					 <%}%>
				}else{
					alert("<siga:Idioma key="censo.fusionDuplicados.error.seleccionePersona"/>");
				}
			}

			function seleccionar(idD){
				document.forms[0].idPersonaDestino.value = idD;
				if (idD == <%=idPersona1%>) {
					document.forms[0].idPersonaOrigen.value = <%=idPersona0%>;
				} else {
					document.forms[0].idPersonaOrigen.value = <%=idPersona1%>;
				}
				if(document.forms[0].checkDireccion!=null){
					seleccionarChecks(document.forms[0].checkDireccion, idD);
				}
				if(document.forms[0].checkEstado!=null){
					seleccionarChecks(document.forms[0].checkEstado, idD);
				}
				seleccionado=true;
			}
			
		</script>	
	</html:form>
	
	<script language="JavaScript">
	
	function informacionLetrado(idPersona,idIntitucion,verFichaLetrado) {
	    var idInst = idIntitucion;			          		
	    var idPers = idPersona;		    
	    var idLetrado =idLetrado;			    
		
	    document.forms[0].filaSelD.value = 1;
	
		if(idIntitucion != null && idIntitucion !=""){
			document.forms[0].tablaDatosDinamicosD.value=idPers + ',' + idInst + '%';
			document.forms[0].verFichaLetrado.value=verFichaLetrado;
			document.forms[0].modo.value="editar";
		}else{
			//Es no colegiado y el idIntitucion será de donde estés logeado.
			document.forms[0].tablaDatosDinamicosD.value=idPers + ',' + <%=idInstitucionLocation%> + '%';	
			document.forms[0].modo.value="ver";
		}
		
	   	document.forms[0].submit();		   	
	}
	</script>	
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>