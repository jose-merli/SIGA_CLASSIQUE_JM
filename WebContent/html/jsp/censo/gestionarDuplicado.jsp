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
	
	boolean existeColegiacionesIguales = false;
	boolean existeColegiacionesDiferentes = false;
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
											<siga:Idioma key="censo.gestionarDuplicado.patron.identificacion" /> | <siga:Idioma key="censo.gestionarDuplicado.patron.ultimaModificacion" />
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.busquedaDuplicados.patron.apellido1" /> <siga:Idioma key="censo.busquedaDuplicados.patron.apellido2" />, <siga:Idioma key="censo.busquedaDuplicados.patron.nombre" />
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<b><siga:Idioma key="censo.gestionarDuplicado.patron.modificarDatos" /></b>
										</td>
									</tr>
									
									<tr>
										<!-- Aqui se meten los radio buttons para seleccionar el colegiado base -->
										<td class="labelText">
											<b><siga:Idioma key="censo.gestionarDuplicado.patron.seleccionDestino" /></b>
											<br>
											<siga:Idioma key="censo.gestionarDuplicado.patron.datosGenerales" />
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
											<c:set var="tipoSociedad" value='<%=ClsConstants.TIPO_CLIENTE_INSTITUCION%>'/>
											<c:if test="${datosPersona.tipoCliente==tipoSociedad}">
												<font color="red"><i><b>(SOCIEDAD)</b></i></font>
											</c:if>
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
											<c:set var="tipoLetrado" value='<%=ClsConstants.TIPO_CLIENTE_LETRADO%>'/>
											<c:set var="tipoNoColegiado" value='<%=ClsConstants.TIPO_CLIENTE_NOCOLEGIADO%>'/>
											<c:set var="tipoSociedad" value='<%=ClsConstants.TIPO_CLIENTE_INSTITUCION%>'/>
											<c:choose>
												<c:when test="${datosPersona.tipoCliente==tipoLetrado}">
													<img id="iconoboton_informacionLetrado1" src="/SIGA/html/imagenes/binformacionLetrado_off.gif" 
														 style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
														 title="Acceso a ficha" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',1); " >
												</c:when>
												<c:when test="${datosPersona.tipoCliente==tipoNoColegiado}">
													<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_on.gif" 
														 style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
														 title="consultar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}',''); " >
													<img id="iconoboton_editar10" src="/SIGA/html/imagenes/beditar_off.gif" 
														 style="cursor:pointer;" alt="Editar" class="botonesIcoTabla" name="iconoFila"
														 title="Editar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',0); " >
												</c:when>
												<c:when test="${datosPersona.tipoCliente==tipoSociedad}">
													<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_on.gif" 
														 style="cursor:pointer;" alt="Información letrado" class="botonesIcoTabla" name="iconoFila"
														 title="consultar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}',''); " >
													<img id="iconoboton_editar10" src="/SIGA/html/imagenes/beditar_off.gif" 
														 style="cursor:pointer;" alt="Editar" class="botonesIcoTabla" name="iconoFila"
														 title="Editar" border="0" onClick="informacionLetrado('${datosPersona.idPersona}','<%=idInstitucionLocation%>',0); " >
												</c:when>
												<c:otherwise>
													<i><b><siga:Idioma key="censo.gestionarDuplicado.patron.noDisponible" /></b></i>
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
			
		<!-- INI Datos en GENERAL -->
			<div id="divGeneral" name="divScroll" style="overflow-y:scroll; height:550px;">
				
				<siga:ConjCampos leyenda="Otros datos" desplegable="true" oculto="true" postFunction="pulsarOtrosDatos()">
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
											<siga:Idioma key="censo.gestionarDuplicado.patron.sexo"/> | <siga:Idioma key="censo.gestionarDuplicado.patron.estadoCivil"/>
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
												<i>[<strike><siga:Idioma key="censo.gestionarDuplicado.patron.fechaNacimiento"/></strike>]</i>
											</c:if>
											 | <c:out value="${datosPersona.naturalDe}"/>
											<c:if test="${datosPersona.naturalDe==''}">
												<i>[<strike><siga:Idioma key="censo.gestionarDuplicado.patron.lugarNacimiento"/></strike>]</i>
											</c:if>
										</td>
									</tr>
									
									<tr>
										<td class="labelTextValue">
											<c:if test="${datosPersona.fallecido=='1'}">
												<b><c:out value="(Fallecido)"/></b> &nbsp;
											</c:if>
											<c:out value="${datosPersona.sexoStr}"/>
											<c:if test="${datosPersona.sexoStr==null}">
												<i>[<strike><siga:Idioma key="censo.gestionarDuplicado.patron.sexo"/></strike>]</i>
											</c:if>
											 | <c:out value="${datosPersona.idEstadoCivilStr}"/>
											<c:if test="${datosPersona.idEstadoCivilStr==null}">
												<i>[<strike><siga:Idioma key="censo.gestionarDuplicado.patron.estadoCivil"/></strike>]</i>
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
											<siga:Idioma key="censo.gestionarDuplicado.patron.sancionesYCertificados"/>
										</td>
									</tr>
																								
									<tr>
										<td class="labelText"> 
											<siga:Idioma key="censo.consultaDatosGenerales.literal.tratamiento"/> 
											| <siga:Idioma key="censo.consultaDatosGenerales.literal.idioma"/>
	 									</td>
									</tr>
									
									<tr>
										<td class="labelText"> 
											Otros datos generales
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
												<font color="red"><i><b><siga:Idioma key="messages.letrados.noRevistaCGAE"/></b></i></font> |
											</c:if>
											<c:if test="${datosCliente.noAparacerRedAbogacia=='1'}">
												<!-- Este mensaje es diferente entre CGAE y Colegios -->
												<font color="red"><i><b><siga:Idioma key="messages.letrados.noApareceRedAbogacia"/></b></i></font>
											</c:if>
										</td>
									</tr>
								</table>
							</td>
							</c:forEach>
						</tr>
					</table>
				</siga:ConjCampos>
		<!-- FIN Datos en GENERAL -->
					
		<!-- INI Colegiaciones diferentes -->
				
				<c:choose>
				<c:when test="${empty datos.datosColegiales}">
					<c:set var="existeColegiacionesDiferentes" value="false"/>
				</c:when>
				<c:otherwise>		
					<c:set var="existeColegiacionesDiferentes" value="true"/>
				<br/>
				<siga:ConjCampos leyenda="Colegiaciones diferentes" desplegable="true" oculto="true" postFunction="pulsarColegiacionesDiferentes()">
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
										
										| <b><c:out value="${datosCol.datosColegiacion.numCol}"/></b>
										
										| <c:out value="${datosCol.datosColegiacion.fechaIncorporacion}"/>
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<c:choose>
											<c:when test="${datosCol.datosColegiacion.situacionResidente=='1'}">
												<c:out value="Residente"/>
											</c:when>
											<c:when test="${datosCol.datosColegiacion.situacionResidente=='0'}">
												<c:out value="No Residente"/>
											</c:when>
										</c:choose>
										
										<c:if test="${datosCol.datosColegiacion.comunitario=='1'}">
											<b><c:out value="Inscrito"/></b> &nbsp;
										</c:if>
										
										<c:choose>
											<c:when test="${datosCol.estadoColegiacion != null}">
											| <c:out value="${datosCol.estadoColegiacion.row.DESCRIPCION}"/>
												desde <c:out value="${datosCol.estadoColegiacion.row.FECHAESTADO_SPANISH}"/>
											</c:when>
											<c:when test="${datosCol.datosColegiacion.comunitario != null}">
												<i>[<strike>estado colegial</strike>]</i>
											</c:when>
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
				</c:otherwise>		
				</c:choose>
		<!-- FIN Colegiaciones diferentes -->
				
		<!-- INI Colegiaciones iguales -->
				<c:choose>
				<c:when test="${empty datos.datosColegialesIguales}">
					<c:set var="existeColegiacionesIguales" value="false"/>
				</c:when>
				<c:otherwise>		
					<c:set var="existeColegiacionesIguales" value="true"/>
				<c:forEach items="${datos.datosColegialesIguales}" var="datosColUnica" varStatus="status">
				
				<c:if test="${datosColUnica.fechaProduccion!=''}">
					<c:set var="institucionColegiacion" value="${datosColUnica.institucionColegiacion} (autogestión)"/>
				</c:if>
				<c:if test="${datosColUnica.fechaProduccion==''}">
					<c:set var="institucionColegiacion" value="${datosColUnica.institucionColegiacion}"/>
				</c:if>
				
				<br/>
				<siga:ConjCampos leyenda="Coincidencia - ${institucionColegiacion}" desplegable="true" oculto="true" postFunction="pulsarColegiacionesIguales()">
				
		<!-- INI Datos colegiacion comun -->
					<table width="100%">
						<tr>
							<td width="26%"> 
								<table>
									<tr>
										<td class="labelText">
											Colegio | Num. col. | Fecha Inc. | Resid. e Inscr.
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datosColUnica.datosColegiacion}" var="datosCol" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelText" colspan="3">
											${institucionColegiacion} | 
											
											<b><c:out value="${datosCol.numCol}"/></b> |
											
											<c:out value="${datosCol.fechaIncorporacion}"/> |
											
											<c:choose>
												<c:when test="${datosCol.situacionResidente=='1'}">
													<c:out value="Residente"/>
												</c:when>
												<c:when test="${datosCol.situacionResidente=='0'}">
													<c:out value="No Residente"/>
												</c:when>
											</c:choose>
											
											<c:if test="${datosCol.comunitario=='1'}">
												<b><c:out value="Inscrito"/></b> &nbsp;
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
											<siga:Idioma key="censo.gestionarDuplicado.patron.historicoEstados"/> 
											(OJO: se suman los estados de ambas colegiaciones)
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datosColUnica.historicoEstadosColegiacion}" var="datosCol" varStatus="status">
							<td width="37%">
								<table width="100%">
									<c:forEach items="${datosCol}" var="histEstados"  varStatus="status">
									<tr>
										<td class="labelText" width="50%">
											<c:out value="${histEstados.row.DESCRIPCION}"/>
										</td>
										<td class="labelText" width="50%">
											desde <c:out value="${histEstados.row.FECHAESTADO_SPANISH}"/>
										</td>
									</tr>															
									</c:forEach>
								</table>
							</td>
							</c:forEach>
						</tr>
						
						<tr>
							<td width="26%"> 
								<table>
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.gestionarDuplicado.patron.ultimaActualizacionExterna"/>
										</td>
									</tr>
								</table>
							</td>
							<c:forEach items="${datosColUnica.datosCenso}" var="datosCenso" varStatus="status">
							<td width="37%">
								<table width="100%">
									<tr>
									<c:if test="${datosCenso.situacionEjercicio!=null}">
										<td class="labelText" width="50%">
											<c:out value="${datosCenso.situacionEjercicio}"/> 
										</td>
										<td class="labelText" width="50%">
											a día <c:out value="${datosCenso.fechaSituacion}"/>
										</td>
									</c:if>
									<c:if test="${datosCenso.situacionEjercicio==null}">
										<td class="labelText" colspan="2" width="100%">
											-
										</td>
									</c:if>
									</tr>
									<tr>
										<td class="labelText" width="50%">
											<c:out value="${datosCenso.estadoCenso}"/>
										</td>
										<td class="labelText" width="50%">
											<c:out value="${datosCenso.fechaCenso}"/>
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
											Otros datos colegiales
										</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datosColUnica.datosCliente}" var="datosCliente" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelText"> 
											Tratamiento '<c:out value="${datosCliente.idTratamientoStr}"/>' | 
											Idioma '<c:out value="${datosCliente.idLenguajeStr}"/>' | 
									
											Asiento contable '<c:out value="${datosCliente.asientoContable}"/>' | 
											<c:if test="${datosCliente.fotografia!=''}">
												<i><b>Con foto</b></i> | 
											</c:if>
											<c:if test="${datosCliente.guiaJudicial=='1'}">
												<i><b><siga:Idioma key="censo.consultaDatosGenerales.literal.guiaJudicial"/></b></i> | 
											</c:if>
											<c:if test="${datosCliente.publicidad=='1'}">
												<i><b><siga:Idioma key="censo.consultaDatosGenerales.literal.publicidad"/></b></i> | 
											</c:if>
											<c:if test="${datosCliente.comisiones=='1'}">
												<i><b><siga:Idioma key="censo.consultaDatosGenerales.literal.comisiones"/></b></i> | 
											</c:if>
											<c:if test="${datosCliente.noAparacerRedAbogacia=='1'}">
												<!-- Este mensaje es diferente entre CGAE y Colegios -->
												<i><b><siga:Idioma key="messages.letrados.lopd"/></b></i> 
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
											&nbsp;
	 									</td>
									</tr>
								</table>
							</td>
							
							<c:forEach items="${datosColUnica.datosColegiacion}" var="datosCol" varStatus="status">
							<td width="37%">
								<table>
									<tr>
										<td class="labelText" colspan="3">
											<c:if test="${datosCol.jubilacionCuota=='1'}">
												<siga:Idioma key="censo.consultaDatosGenerales.literal.jubilacion"/> |
											</c:if>
											
											<c:if test="${datosCol.identificadorDS!=null && datosCol.identificadorDS!=''}">
												Con acceso a Docushare |
											</c:if>
											
											<c:out value="${datosCol.idTipoSeguroStr}"/>
											<c:if test="${datosCol.NMutualista!=null && datosCol.NMutualista!=''}">
												&nbsp;Número asegurado: <c:out value="${datosCol.NMutualista}"/> |
											</c:if>
										</td>
									</tr>
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
				</c:otherwise>		
				</c:choose>		
		<!-- FIN Colegiaciones iguales -->
				
		<!-- INI Direcciones en GENERAL -->
				<br/>
				<siga:ConjCampos leyenda="censo.fusionDuplicados.direcciones.cabecera" desplegable="true" oculto="true" postFunction="pulsarDirecciones()">
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
		<!-- FIN Direcciones en GENERAL -->
				
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
			
			var pulsadoOtrosDatos = false;
			var pulsadoColegiacionesIguales = false;
			var pulsadoColegiacionesDiferentes = false;
			var pulsadoDirecciones = false;
			
			function pulsarOtrosDatos() {
				pulsadoOtrosDatos = true;
			}
			function pulsarColegiacionesIguales() {
				pulsadoColegiacionesIguales = true;
			}
			function pulsarColegiacionesDiferentes() {
				pulsadoColegiacionesDiferentes = true;
			}
			function pulsarDirecciones() {
				pulsadoDirecciones = true;
			}
		
			function accionAceptar(){
				
				if (!pulsadoOtrosDatos || (!pulsadoColegiacionesIguales && <%=existeColegiacionesIguales%>) || (!pulsadoColegiacionesDiferentes && <%=existeColegiacionesDiferentes%>) || !pulsadoDirecciones) {
					alert("<siga:Idioma key="messages.error.censo.mantenimientoDuplicados.reviseDatos"/>");
					return
				}
				if (seleccionado){
					<%if(idPersona1.equalsIgnoreCase(idPersona0)){%>
						alert("<siga:Idioma key="censo.fusionDuplicados.error.mismaPersona"/>");
					<%}else{%>
						sub();
						if(document.forms[0].checkDireccion){
							document.forms[0].listaDirecciones.value = getSelectedCheckboxValue(document.forms[0].checkDireccion, true);
							document.forms[0].listaDireccionesNoSeleccionadas.value = getSelectedCheckboxValue(document.forms[0].checkDireccion, false);
						}
						if(confirm("Se van a combinar los datos a una sola persona.")){
							alert("<siga:Idioma key="messages.error.censo.mantenimientoDuplicados.espera"/>");
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
			//Es letrado
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