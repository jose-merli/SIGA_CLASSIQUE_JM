<!DOCTYPE html>
<html>
<head>
<!-- listarGuardiasTurnosLetrado.jsp -->

<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<%
	HttpSession ses = request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getSession().getAttribute("resultado");
	Hashtable turno = (Hashtable) request.getSession().getAttribute("turnoElegido");
	Vector ocultos = (Vector) ses.getAttribute("ocultos");
	Vector campos = (Vector) ses.getAttribute("campos");
	String accion = String.valueOf(request.getSession().getAttribute("modoPestanha"));
	String accionTurno = (String) request.getSession().getAttribute("accionTurno");
	String botones = "C,E,B";
	if (accionTurno.equalsIgnoreCase("Ver"))
		botones = "C";
	FilaExtElement[] elems = new FilaExtElement[1];
	elems[0] = null;

	//Datos del Colegiado si procede:
	String nombrePestanha = null, numeroPestanha = null;
	try {
		Hashtable datosColegiado = (Hashtable) request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String) datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String) datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e) {
		nombrePestanha = "";
		numeroPestanha = "";
	}

	String alto = "245";
	String idturno = (String) request.getSession().getAttribute("IDTURNOSESION");
	ScsInscripcionTurnoBean inscripcionTurnoSeleccionada = (ScsInscripcionTurnoBean) request.getAttribute("inscripcionTurnoSeleccionada");
	String fechaConsultaTurno = (String) request.getSession().getAttribute("fechaConsultaInscripcionTurno");
	
	String botonTodasNinguna = "";
%>


	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt titulo="censo.fichaCliente.sjcs.turnos.guardias.cabecera" localizacion="censo.fichaCliente.sjcs.turnos.guardias.localizacion" />
		<!-- FIN: TITULO Y LOCALIZACION -->
	
		<!-- SCRIPTS LOCALES -->
	
		<script type="text/javascript">
			function buscar() {	
				document.forms[0].target="mainPestanas";
				document.forms[0].action="JGR_ListarGuardiasTurnosLetrado.do?granotm=<%=System.currentTimeMillis()%>";
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].submit();
			}
		</script>
</head>

<body class="tablaCentralCampos" onload="mostrarFecha();">

	<table class="tablaTitulo" align="center" cellspacing="0" border="0">
		<tr>
			<td class="titulitosDatos">
				<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1" />
				&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>
						
				<% if (numeroPestanha != null && !numeroPestanha.equalsIgnoreCase("")) { %>
					&nbsp;&nbsp;<siga:Idioma key="censo.fichaCliente.literal.colegiado" />
					&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					
				<% } else { %>
					&nbsp;&nbsp;<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> 
				<%}%>
			</td>
		</tr>
	</table>

	<!-- Información del Turno que tenemos seleccionado-->
	<html:form action="DefinirGuardiasTurnosAction.do" method="post" target="">

		<!-- Campo obligatorio -->
		<html:hidden styleId="modo"  property="modo" value="" />
		<input type="hidden" name="guardiaElegida" id="guardiaElegida" value="" />
		<input type="hidden" name="guardias" id="guardias" value="" />
		<input type="hidden" name="fechaInscripcion" id="fechaInscripcion" value="" />
		<input type="hidden" name="actionModal" id="actionModal" value="" />
		<html:hidden styleId="validarInscripciones"   property="validarInscripciones" name="DefinirGuardiasTurnosForm" />
		<input type="hidden" id="fechaSolicitudTurno" name="fechaSolicitudTurno" value="${inscripcionTurnoSeleccionada.fechaSolicitud}" />
		
		<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno">
			<table border="0" align="center">
				<tr>
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura" />
					</td>
					<td class="labelText" style="text-align: left">
						<input name="abreviatura" type="text" class="boxConsulta" size="20"
							maxlength="30" value="<%=turno.get("ABREVIATURA")%>" readonly="true">
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre" />
					</td>
					<td colspan="3" class="labelText" style="text-align: left">
						<input name="nombre" type="text" class="boxConsulta" size="75"
							maxlength="100" value="<%=turno.get("NOMBRE")%>" readonly="true">
					</td>
				</tr>
				
				<tr>
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.area" />
					</td>
					<td class="labelText" style="text-align: left">
						<input name="area" type="text" class="boxConsulta" size="30"
							value="<%=turno.get("AREA")%>" readonly="true">
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia" />
					</td>
					<td class="labelText" style="text-align: left">
						<input name="materia" type="text" class="boxConsulta" size="30"
							value="<%=turno.get("MATERIA")%>" readonly="true">
					</td>
				</tr>
				
				<tr>
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.zona" />
					</td>
					<td class="labelText" style="text-align: left">
						<input name="zona" type="text" class="boxConsulta" size="30"
							value="<%=turno.get("ZONA")%>" readonly="true">
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona" />
					</td>
					<td class="labelText" style="text-align: left">
						<input name="subzona" type="text" class="boxConsulta" size="30"
							value="<%=turno.get("SUBZONA")%>" readonly="true">
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial" />
					</td>
					<td class="labelTextValor" style="text-align: left">
						<%=(turno.get("PARTIDOJUDICIAL") == null) ? "&nbsp;" : turno.get("PARTIDOJUDICIAL")%>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<siga:ConjCampos leyenda="gratuita.busquedaSJCS.literal.filtro">
			<table border="0" align="left">
				<tr>
					<td width="75px">&nbsp;</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.gestionInscripciones.fechaConsulta" />
					</td>
					<td>
						<html:text property="fechaConsulta"
							name="DefinirGuardiasTurnosForm" value="<%=fechaConsultaTurno%>"
							size="10" maxlength="10" styleClass="boxConsulta" readonly="true" />
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->
	<!-- Formulario de la lista de detalle multiregistro -->
	
	<siga:Table 
		name="tablaDatos" 
		border="1"
		columnNames="gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardias.literal.fechainscripcion,Fecha Valor,Fecha Solicitud Baja,gratuita.listarGuardiasTurno.literal.fechaBaja,Estado,"
		columnSizes="20,12,10,8,8,8,10,8,8,8">
		
		<% if (obj == null || obj.size() == 0) { %>
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
 			
		<% } else {
			// consultamos si el colegiado esta dado de baja
			String idper = (String) request.getSession().getAttribute("idPersonaTurno");
			boolean estaDeBaja = false;
			if (usr.isLetrado() && idper != null) {
				CenColegiadoAdm cenColegiadoAdm = new CenColegiadoAdm(usr);
				Hashtable hashtable = cenColegiadoAdm.getEstadoColegial(Long.valueOf(idper), Integer.valueOf(usr.getLocation()));
				if (hashtable == null) {
					estaDeBaja = true;
					
				} else if (!hashtable.get("IDESTADO").equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))) {
					estaDeBaja = true;
				}
			}

			int recordNumber = 1;
			String obligatoriedad = "";
			String fechaSolicitud = "";
			String fechaValidacion = "";
			String fechaSolicitudBaja = "";
			String fechaBaja = "";
			String fechaDenegacion = "";
			String fechaValor = "";
			Boolean isPermitidoInscripcionGuardia = (Boolean) request.getAttribute("isPermitidoInscripcionGuardia");
			while ((recordNumber) <= obj.size()) {
				Hashtable htGuardia = (Hashtable) obj.get(recordNumber - 1);
				obligatoriedad = (String) htGuardia.get("OBLIGATORIEDAD");
				Hashtable htInscripcion = (Hashtable) htGuardia.get("INSCRIPCIONGUARDIA");
				
				if (htInscripcion != null) {
					fechaSolicitud = (String) htInscripcion.get("FECHASUSCRIPCION");
					fechaValidacion = (String) htInscripcion.get("FECHAVALIDACION");
					fechaSolicitudBaja = (String) htInscripcion.get("FECHASOLICITUDBAJA");
					fechaBaja = (String) htInscripcion.get("FECHABAJA");
					fechaDenegacion = (String) htInscripcion.get("FECHADENEGACION");
					fechaValor = (String) htInscripcion.get("FECHAVALOR");

					if (fechaSolicitud == null)
						fechaSolicitud = "";
					
					if (fechaValidacion == null)
						fechaValidacion = "";
					
					if (fechaBaja == null)
						fechaBaja = "";
					
					if (fechaSolicitudBaja == null)
						fechaSolicitudBaja = "";
					
					if (fechaDenegacion == null)
						fechaDenegacion = "";
					
					if (fechaValor == null)
						fechaValor = "";
					
				} else {
					fechaSolicitud = "";
					fechaValidacion = "";
					fechaBaja = "";
					fechaSolicitudBaja = "";
					fechaDenegacion = "";
					fechaValor = "";
				}
				
				String estado = "No aplica";
				
				if (fechaSolicitud.equals("")) {
					estado = "";
					
				} else {
					if (fechaBaja.equals("")) {				
						if (fechaValidacion.equals("")) {
							if (fechaSolicitudBaja.equals("")) {
								if (fechaDenegacion.equals("")) {
									estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.estado.alta.pendiente");
									
								} else {
									estado = UtilidadesString.getMensajeIdioma(usr, "gratuita.gestionInscripciones.estado.alta.denegada");
								}
								
							} else {
								if (fechaBaja.equals("")) {
									if (fechaDenegacion.equals("")) {
										estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.pendiente");
										
									} else {
										estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.denegada");
									}
									
								} else {
									estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.confirmada");
								}
							}
							
						} else {
							if (fechaSolicitudBaja.equals("")) {
								estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.alta.confirmada");
								
							} else {
								if (fechaDenegacion.equals("")) {
									estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.pendiente");
									
								} else {
									estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.denegada");
								}
							}
						}
						
					} else {
						estado = UtilidadesString.getMensajeIdioma(usr,	"gratuita.gestionInscripciones.estado.baja.confirmada");
					}
				}
				
				elems[0] = null;
				
				String literalValidar="";

				if (isPermitidoInscripcionGuardia) {
					
					/* DATOS DE INSCRIPCION DE GUARDIA:
						- fechaSolicitud
						- fechaValidacion						
						- fechaSolicitudBaja
						- fechaBaja
						- fechaDenegacion
						- obligatoriedad
						
						DATOS DE INSCRIPCION DE TURNO:
						- inscripcionTurnoSeleccionada.getFechaSolicitud()
						- inscripcionTurnoSeleccionada.getFechaValidacion()
						- inscripcionTurnoSeleccionada.getFechaSolicitudBaja()
						- inscripcionTurnoSeleccionada.getFechaBaja()						
						- inscripcionTurnoSeleccionada.getFechaDenegacion()
					*/
										
					boolean estadoPendienteValidacionTurno = false;
					boolean estadoBajaTurno = false;
					
					/* ESTADO PENDIENTE TURNO SI SE CUMPLE LOS SIGUIENTES CRITERIOS:
						- TIENE INSCRIPCION DE TURNO
						- TIENE FECHA DE SOLICITUD
						- NO TIENE FECHA DE VALIDACION
						- NO TIENE FECHA DE DENEGACION
					*/
					if (
						inscripcionTurnoSeleccionada != null && 
						inscripcionTurnoSeleccionada.getFechaSolicitud() != null   && !inscripcionTurnoSeleccionada.getFechaSolicitud().equals("") &&
						(inscripcionTurnoSeleccionada.getFechaValidacion() == null || inscripcionTurnoSeleccionada.getFechaValidacion().equals("")) &&
						(inscripcionTurnoSeleccionada.getFechaDenegacion() == null || inscripcionTurnoSeleccionada.getFechaDenegacion().equals(""))
					) {
						estadoPendienteValidacionTurno = true;
						literalValidar="gratuita.altaTurnos.literal.validarTurno";
					}
					
					
					/* ESTADO BAJA TURNO SI CUMPLE LOS SIGUIENTES CRITERIOS:
						- NO TIENE INSCRIPCION DE TURNO
						- NO TIENE FECHA DE SOLICITUD
						
						o
						
						ESTADO BAJA TURNO SI SE CUMPLE LOS SIGUIENTES CRITERIOS:
						- TIENE INSCRIPCION DE TURNO
						- TIENE FECHA DE SOLICITUD
						- NO TIENE FECHA DE VALIDACION
						- TIENE FECHA DE DENEGACION
						
						O
						
						ESTADO BAJA TURNO SI SE CUMPLE LOS SIGUIENTES CRITERIOS:
						- TIENE INSCRIPCION DE TURNO
						- TIENE FECHA DE SOLICITUD
						- TIENE FECHA DE VALIDACION
						- TIENE FECHA DE SOLICITUD DE BAJA
						- TIENE FECHA DE BAJA
					*/
					
					if (
						inscripcionTurnoSeleccionada == null ||
						inscripcionTurnoSeleccionada.getFechaSolicitud() == null || inscripcionTurnoSeleccionada.getFechaSolicitud().equals("")
					) {
						estadoBajaTurno = true;
						
						
					} else if (
						inscripcionTurnoSeleccionada != null && 
						inscripcionTurnoSeleccionada.getFechaSolicitud() != null   && !inscripcionTurnoSeleccionada.getFechaSolicitud().equals("") &&
						(inscripcionTurnoSeleccionada.getFechaValidacion() == null || inscripcionTurnoSeleccionada.getFechaValidacion().equals("")) &&
						inscripcionTurnoSeleccionada.getFechaDenegacion() != null  && !inscripcionTurnoSeleccionada.getFechaDenegacion().equals("")
					) {
						estadoBajaTurno = true;
					
						
					} else if (
						inscripcionTurnoSeleccionada!=null && 
						inscripcionTurnoSeleccionada.getFechaSolicitud() != null     && !inscripcionTurnoSeleccionada.getFechaSolicitud().equals("") &&
						inscripcionTurnoSeleccionada.getFechaValidacion() != null    && !inscripcionTurnoSeleccionada.getFechaValidacion().equals("") &&
						inscripcionTurnoSeleccionada.getFechaSolicitudBaja() != null && !inscripcionTurnoSeleccionada.getFechaSolicitudBaja().equals("") &&						
						inscripcionTurnoSeleccionada.getFechaBaja() != null          && !inscripcionTurnoSeleccionada.getFechaBaja().equals("")
					) {
						estadoBajaTurno = true;
					}
					
					// COMPRUEBO QUE NO TENGA QUE VALIDAR EL TURNO, NI QUE ESTE DE BAJA
					if (!estadoPendienteValidacionTurno && !estadoBajaTurno && (accion.equals("editar") || (accion.equals("ver") && usr.isLetrado()))) {

						// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA" O "ELECCION" (LAS OBLIGATORIAS NO ENTRAN)
						if (obligatoriedad.equals("2") || obligatoriedad.equals("1")) {
							
							// COMPRUEBO QUE EL COLEGIADO SEA EJERCIENTE
							if (!estaDeBaja) {
								
								// COMPRUEBO QUE NO TIENE FECHA DE SOLICITUD (NO HAY INSCRIPCION DE GUARDIA)
								if (fechaSolicitud.equals("")) {
									
									// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA"
									if (obligatoriedad.equals("1")) {
										botonTodasNinguna = "SA";
									
									} else {
										elems[0] = new FilaExtElement("solicitaralta", "solicitaralta", SIGAConstants.ACCESS_FULL);
									}
									
								// COMPRUEBO QUE TIENE FECHA DE SOLICITUD
								} else {
									
									// COMPRUEBO QUE NO TIENE FECHA DE BAJA
									if (fechaBaja.equals("")) {
									
										// COMPRUEBO QUE NO TIENE FECHA DE VALIDACION 
										if (fechaValidacion.equals("")) {
											
											// COMPRUEBO QUE NO TIENE FECHA DE DENEGACION <===> ALTA PENDIENTE
											if (fechaDenegacion.equals("")) {
												// NO HACEMOS NADA
												
											// COMPRUEBO QUE TIENE FECHA DE DENEGACION <===> ALTA DENEGADA
											} else {
												
												// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA"
												if (obligatoriedad.equals("1")) {
													botonTodasNinguna = "SA";
												
												} else {
													elems[0] = new FilaExtElement("solicitaralta", "solicitaralta", SIGAConstants.ACCESS_FULL);
												}
											} // IF FECHA DENEGACION
	
										// COMPRUEBO QUE TIENE FECHA DE SOLICITUD Y FECHA DE VALIDACION
										} else {
											
											// COMPRUEBO QUE TIENE FECHA DE SOLICITUD, FECHA DE VALIDACION, PERO NO FECHA DE SOLICITUD DE BAJA <===> ALTA CONFIRMADA
											if (fechaSolicitudBaja.equals("")) {
														
												// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA"
												if (obligatoriedad.equals("1")) {
													botonTodasNinguna = "SB";
												
												} else {
													elems[0] = new FilaExtElement("solicitarbaja", "solicitarbaja", SIGAConstants.ACCESS_FULL);
												}
												
											// COMPRUEBO QUE TIENE FECHA DE SOLICITUD, FECHA DE VALIDACION Y FECHA DE SOLICITUD DE BAJA
											} else {
												
												// COMPRUEBO QUE NO TIENE FECHA DE DENEGACION <===> BAJA PENDIENTE
												if (fechaDenegacion.equals("")) {
													// NO HACEMOS NADA
											
												// COMPRUEBO QUE TIENE FECHA DE DENEGACION <===> BAJA DENEGADA
												} else {
													
													// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA"
													if (obligatoriedad.equals("1")) {
														botonTodasNinguna = "SB";
													
													} else {
														elems[0] = new FilaExtElement("solicitarbaja", "solicitarbaja", SIGAConstants.ACCESS_FULL);
													}
												} // IF FECHA DENEGACION													
											} // IF FECHA SOLICITUD BAJA										
										} // IF FECHA VALIDACION
										
									} else {
										request.getSession().setAttribute("esActualizacion", fechaSolicitud);
										if (obligatoriedad.equals("1")) {
											botonTodasNinguna = "SA";
										
										} else {
											elems[0] = new FilaExtElement("solicitaralta", "solicitaralta", SIGAConstants.ACCESS_FULL);
										}										
									} // IF FECHA BAJA
								} // IF FECHA SOLICITUD
								
							// COMPRUEBO QUE EL COLEGIADO NO ES EJERCIENTE
							} else {
								
								// COMPRUEBO QUE TIENE FECHA DE SOLICITUD Y FECHA DE VALIDACION, PERO NO TENGO FECHA DE SOLICITUD DE BAJA, NI FECHA DE BAJA  <===> ALTA CONFIRMADA
								if (
									!fechaSolicitud.equals("") && 
									!fechaValidacion.equals("") &&
									fechaSolicitudBaja.equals("") &&
									fechaBaja.equals("")									
								) {
									
									// COMPRUEBO SI ES UN TURNO CONFIGURADO CON GUARDIAS "TODAS O NINGUNA"
									if (obligatoriedad.equals("1")) {
										botonTodasNinguna = "SB";
									
									} else {
										elems[0] = new FilaExtElement("solicitarbaja", "solicitarbaja", SIGAConstants.ACCESS_FULL);
									}
								}
								
							} // IF ESTADO DE BAJA
						} // IF OBLIGATORIEEDAD
					} // IF ESTADO PENDIENTE Y BAJA DEL TURNO
				} // IF isPermitidoInscripcionGuardia

					
				String tipoDia = (String) htGuardia.get("TIPODIASGUARDIA");
				String literalDuracion = "gratuita.altaTurnos_2.literal.dias";
				
				if (tipoDia.equalsIgnoreCase("D"))
					literalDuracion = "gratuita.altaTurnos_2.literal.dias";
				
				else if (tipoDia.equalsIgnoreCase("S"))
					literalDuracion = "gratuita.altaTurnos_2.literal.semanas";
				
				else if (tipoDia.equalsIgnoreCase("M"))
					literalDuracion = "gratuita.altaTurnos_2.literal.meses";
				
				else if (tipoDia.equalsIgnoreCase("Q"))
					literalDuracion = "gratuita.altaTurnos_2.literal.quincenas";
		%>
		
			<siga:FilaConIconos pintarEspacio="false" visibleEdicion="false" visibleBorrado="false" fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit">				
				
				<td>
					<input type='hidden' name='validaInscripciones' value='<%=htGuardia.get("VALIDARINSCRIPCIONES")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=htGuardia.get("IDTURNO")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=htGuardia.get("IDGUARDIA")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_22' name='oculto<%=String.valueOf(recordNumber)%>_22' value='<%=htGuardia.get("PORGRUPOS")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=htGuardia.get("GUARDIAS")%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=fechaSolicitud%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_44' name='oculto<%=String.valueOf(recordNumber)%>_44' value='<%=fechaValidacion%>'>
					<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_55' name='oculto<%=String.valueOf(recordNumber)%>_55' value='<%=idper%>'>
					
					<%=htGuardia.get("GUARDIA")%>
				</td>
				
				<td>
					<% if (((String) htGuardia.get("OBLIGATORIEDAD")).equalsIgnoreCase("0")) { %> 
						<siga:Idioma key="gratuita.altaTurnos_2.literal.obligatorias" />
						
					<% } else if (((String) htGuardia.get("OBLIGATORIEDAD")).equalsIgnoreCase("1")) { %> 
						<siga:Idioma key="gratuita.altaTurnos_2.literal.todasninguna" />
						
					<% } else { %> 
						<siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion" /> 
					<% } %>
				</td>
				
				<td>
					<%=ScsGuardiasTurnoAdm.obtenerTipoDia((String) htGuardia.get("SELECCIONLABORABLES"), (String) htGuardia.get("SELECCIONFESTIVOS"), usr)%>
				</td>
				
				<td>
					<%=htGuardia.get("DURACION")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>" />
				</td>
				
				<td>
					&nbsp;<%=GstDate.getFormatedDateShort("", fechaSolicitud)%><siga:Idioma key="<%=literalValidar%>"/>
				</td>
				
				<td>
					&nbsp;<%=GstDate.getFormatedDateShort("", fechaValor)%>
				</td>
				
				<td>
					&nbsp;<%=GstDate.getFormatedDateShort("", fechaSolicitudBaja)%>
				</td>
				
				<td>
					&nbsp;<%=GstDate.getFormatedDateShort("", fechaBaja)%>
				</td>
				
				<td>
					&nbsp;<%=estado%>
				</td>
			</siga:FilaConIconos>

		<% 		recordNumber++;
			} // WHILE
		} // ELSE 
		%>		
	</siga:Table>
	
	<% if (!botonTodasNinguna.equals("")) { %>
		<siga:ConjBotonesAccion botones="<%=botonTodasNinguna%>" clase="botonesDetalle"  />
	<% } %>	
	
	<html:form action="/JGR_InscribirseEnGuardia" name="FormASolicitar" styleId="FormASolicitar" type="com.siga.gratuita.form.InscripcionTGForm">
		<html:hidden property="modo" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="idPersona" />
		<html:hidden property="idTurno" />
		<html:hidden property="idGuardia" />
		<html:hidden property="porGrupos" />
		<html:hidden property="fechaSolicitud" />
		<html:hidden property="observacionesSolicitud" />
		<html:hidden property="fechaValidacion" />
		<html:hidden property="observacionesValidacion" />
		<html:hidden property="fechaSolicitudBaja" />
		<html:hidden property="observacionesBaja" />
		<html:hidden property="fechaBaja" />
		<html:hidden property="fechaSolicitudTurno" />
		<input type="hidden" name="actionModal" />
	</html:form>
	
	<html:form action="/JGR_BajaEnGuardia" name="FormASolicitarBaja" styleId="FormASolicitarBaja" type="com.siga.gratuita.form.InscripcionTGForm">
		<html:hidden property="modo" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="idPersona" />
		<html:hidden property="idTurno" />
		<html:hidden property="idGuardia" />
		<html:hidden property="fechaSolicitud" />
		<html:hidden property="observacionesSolicitud" />
		<html:hidden property="fechaValidacion" />
		<html:hidden property="observacionesValidacion" />
		<html:hidden property="fechaSolicitudBaja" />
		<html:hidden property="observacionesBaja" />
		<html:hidden property="fechaBaja" />
		<input type="hidden" name="actionModal" />
	</html:form>
	<!-- FIN: LISTA DE VALORES -->

	<script type="text/javascript" >		
		function accionNuevo() {	
			document.forms[0].target="mainPestanas";
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}

		// botones alta y baja
		function solicitaralta(fila) {
			var idTurno = 'oculto' + fila + '_' + 1;
			var idGuardia = 'oculto' + fila + '_' + 2;
			var porGrupos = 'oculto' + fila + '_' + 22;
			var idPersona = 'oculto' + fila + '_' + 55;
			document.FormASolicitar.idTurno.value = document.getElementById(idTurno).value;
			document.FormASolicitar.idGuardia.value = document.getElementById(idGuardia).value;
			document.FormASolicitar.porGrupos.value = document.getElementById(porGrupos).value;
			document.FormASolicitar.idPersona.value = document.getElementById(idPersona).value;
			document.FormASolicitar.idInstitucion.value = <%=usr.getLocation()%>;
			document.FormASolicitar.fechaSolicitud.value = "";
			document.FormASolicitar.fechaSolicitudTurno.value = document.getElementById('fechaSolicitudTurno').value;
			document.FormASolicitar.modo.value = "sigConsultaTurno";			
			var resultado = ventaModalGeneral(document.FormASolicitar.name,"G");
			buscar();
		}
		
		// Funciona a la que llama al pulsar el boton de solicitar alta
		function solicitarAlta() {
			// Como es por guardias todas o ninguna y tiene almenos una guardia para dar de alta, solicitamos el alta de la primera, que conllevara el alta de todas las guardias del turno
			solicitaralta(1);
		}
		
		function solicitarbaja(fila) {
			var idTurno = 'oculto' + fila + '_' + 1;
			var idGuardia = 'oculto' + fila + '_' + 2;
			var fechaInscripcion = 'oculto' + fila + '_' + 4;
			var fechaValidacion = 'oculto' + fila + '_' + 44;
			var idPersona = 'oculto' + fila + '_' + 55;
			document.FormASolicitarBaja.idTurno.value = document.getElementById(idTurno).value;
			document.FormASolicitarBaja.idGuardia.value = document.getElementById(idGuardia).value;
			document.FormASolicitarBaja.idPersona.value = document.getElementById(idPersona).value;
			document.FormASolicitarBaja.idInstitucion.value = <%=usr.getLocation()%>;
			document.FormASolicitarBaja.fechaSolicitud.value = document.getElementById(fechaInscripcion).value;
			document.FormASolicitarBaja.fechaValidacion.value = document.getElementById(fechaValidacion).value;
			document.FormASolicitarBaja.modo.value = "sbgConsultaTurno";
			var resultado = ventaModalGeneral(document.FormASolicitarBaja.name,"G");
			if(resultado) {
				buscar();
			}			
		}
		
		// Funciona a la que llama al pulsar el boton de solicitar baja
		function solicitarBaja() {
			// Como es por guardias todas o ninguna y tiene almenos una guardia para dar de baja, solicitamos la baja de la primera, que conllevara la baja de todas las guardias del turno
			solicitarbaja(1);
		}
		
		function mostrarFecha()	{		
			if(document.getElementById('fechaConsulta')){
				if(document.DefinirGuardiasTurnosForm.fechaConsulta && document.DefinirGuardiasTurnosForm.fechaConsulta.value!=''&& document.DefinirGuardiasTurnosForm.fechaConsulta.value!='sysdate'){
					document.getElementById('fechaConsulta').value = document.DefinirGuardiasTurnosForm.fechaConsulta.value;
				}else{
					fechaActual = getFechaActualDDMMYYYY();
					document.getElementById('fechaConsulta').value = fechaActual;
					document.DefinirGuardiasTurnosForm.fechaConsulta.value = fechaActual;
				}
			}
		}
		
		function refrescarLocal() {
			buscar();
		}
	</script>


	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>