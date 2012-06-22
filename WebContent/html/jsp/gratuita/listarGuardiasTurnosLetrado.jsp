<!-- listarGuardiasTurnosLetrado.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>


<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) request.getSession()
			.getAttribute("resultado");
	Hashtable turno = (Hashtable) request.getSession().getAttribute(
			"turnoElegido");
	Vector ocultos = (Vector) ses.getAttribute("ocultos");
	Vector campos = (Vector) ses.getAttribute("campos");
	String accionTurno = (String) request.getSession().getAttribute(
			"accionTurno");
	String botones = "C,E,B";
	if (accionTurno.equalsIgnoreCase("Ver"))
		botones = "C";
	FilaExtElement[] elems = new FilaExtElement[1];
	elems[0] = null;

	//Datos del Colegiado si procede:
	String nombrePestanha = null, numeroPestanha = null;
	try {
		Hashtable datosColegiado = (Hashtable) request.getSession()
				.getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String) datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String) datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e) {
		nombrePestanha = "";
		numeroPestanha = "";
	}

	String alto = "245";
	String idturno = (String) request.getSession().getAttribute(
			"IDTURNOSESION");
	ScsInscripcionTurnoBean inscripcionTurnoSeleccionada = (ScsInscripcionTurnoBean) request
			.getAttribute("inscripcionTurnoSeleccionada");
	String fechaConsultaTurno = (String) request.getSession()
			.getAttribute("fechaConsultaInscripcionTurno");
%>

<%@page import="com.siga.Utilidades.UtilidadesHash"%>
<html>
	<!-- HEAD -->
	<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp" />
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css" />
	
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt
		titulo="censo.fichaCliente.sjcs.turnos.guardias.cabecera"
		localizacion="censo.fichaCliente.sjcs.turnos.guardias.localizacion" />
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

	<table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos"><siga:Idioma
					key="censo.consultaDatosGenerales.literal.titulo1" />&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				<%
					if (numeroPestanha != null && !numeroPestanha.equalsIgnoreCase("")) {
				%> <siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
				<%
					} else {
				%> <siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> <%
 					}
 				%>
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
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.abreviatura" /></td>
					<td class="labelText" style="text-align: left"><input
						name="abreviatura" type="text" class="boxConsulta" size="20"
						maxlength="30" value="<%=turno.get("ABREVIATURA")%>"
						readonly="true"></td>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="censo.SolicitudIncorporacion.literal.nombre" /></td>
					<td colspan="3" class="labelText" style="text-align: left"><input
						name="nombre" type="text" class="boxConsulta" size="75"
						maxlength="100" value="<%=turno.get("NOMBRE")%>" readonly="true">
					</td>
				</tr>
				<tr>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.area" /></td>
					<td class="labelText" style="text-align: left"><input
						name="area" type="text" class="boxConsulta" size="30"
						value="<%=turno.get("AREA")%>" readonly="true"></td>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.materia" /></td>
					<td class="labelText" style="text-align: left"><input
						name="materia" type="text" class="boxConsulta" size="30"
						value="<%=turno.get("MATERIA")%>" readonly="true"></td>
				</tr>
				<tr>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.zona" /></td>
					<td class="labelText" style="text-align: left"><input
						name="zona" type="text" class="boxConsulta" size="30"
						value="<%=turno.get("ZONA")%>" readonly="true"></td>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.subzona" /></td>
					<td class="labelText" style="text-align: left"><input
						name="subzona" type="text" class="boxConsulta" size="30"
						value="<%=turno.get("SUBZONA")%>" readonly="true"></td>
					<td class="labelText" style="text-align: left"><siga:Idioma
							key="gratuita.definirTurnosIndex.literal.partidoJudicial" /></td>
					<td class="labelTextValor" style="text-align: left"><%=(turno.get("PARTIDOJUDICIAL") == null) ? "&nbsp;"
							: turno.get("PARTIDOJUDICIAL")%></td>
				</tr>
			</table>
		</siga:ConjCampos>
		<siga:ConjCampos leyenda="gratuita.busquedaSJCS.literal.filtro">
			<table border="0" align="center">
				<tr>
					<td class="labelText"><siga:Idioma
							key="gratuita.gestionInscripciones.fechaConsulta" /></td>
					<td width="75%"><html:text property="fechaConsulta"
							name="DefinirGuardiasTurnosForm" value="<%=fechaConsultaTurno%>"
							size="10" maxlength="10" styleClass="box" readonly="true"></html:text>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<!-- INICIO: LISTA DE VALORES -->
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

	<!-- Formulario de la lista de detalle multiregistro -->

	<siga:TablaCabecerasFijas nombre="tablaDatos" borde="1"
		clase="tableTitle"
		nombreCol="gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardias.literal.fechainscripcion,Fecha Valor,Fecha Solicitud Baja,gratuita.listarGuardiasTurno.literal.fechaBaja,Estado,"
		tamanoCol="15,8,10,6,7,8,8,8,6,6" alto="<%=alto%>"
		ajusteBotonera="true">
		<%
			if (obj == null || obj.size() == 0) {
		%>

		<br>
		<p class="titulitos" style="text-align: center">
			<siga:Idioma key="messages.noRecordFound" />
		</p>
		<br>
		<%
			} else {
					// consultamos si el colegiado esta dado de baja
					String idper = (String) request.getSession().getAttribute(
							"idPersonaTurno");
					boolean estaDeBaja = false;
					if (usr.isLetrado() && idper != null) {
						CenColegiadoAdm cenColegiadoAdm = new CenColegiadoAdm(
								usr);
						Hashtable hashtable = cenColegiadoAdm
								.getEstadoColegial(Long.valueOf(idper),
										Integer.valueOf(usr.getLocation()));
						if (hashtable == null) {
							estaDeBaja = true;
						} else if (!hashtable
								.get("IDESTADO")
								.equals(String
										.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))) {
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
					String tiposguardias = "";
					Boolean isPermitidoInscripcionGuardia = (Boolean) request
							.getAttribute("isPermitidoInscripcionGuardia");
					while ((recordNumber) <= obj.size()) {
						Hashtable htGuardia = (Hashtable) obj
								.get(recordNumber - 1);
						tiposguardias = (String) htGuardia.get("GUARDIAS");
						obligatoriedad = (String) htGuardia
								.get("OBLIGATORIEDAD");
						Hashtable htInscripcion = (Hashtable) htGuardia
								.get("INSCRIPCIONGUARDIA");
						if (htInscripcion != null) {
							fechaSolicitud = (String) htInscripcion
									.get("FECHASUSCRIPCION");
							fechaValidacion = (String) htInscripcion
									.get("FECHAVALIDACION");
							fechaSolicitudBaja = (String) htInscripcion
									.get("FECHASOLICITUDBAJA");
							fechaBaja = (String) htInscripcion.get("FECHABAJA");
							fechaDenegacion = (String) htInscripcion
									.get("FECHADENEGACION");
							fechaValor = (String) htInscripcion
									.get("FECHAVALOR");

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
							if (fechaValidacion.equals("")) {
								if (fechaSolicitudBaja.equals("")) {
									if (fechaDenegacion.equals("")) {
										estado = UtilidadesString
												.getMensajeIdioma(usr,
														"gratuita.gestionInscripciones.estado.alta.pendiente");
										// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
									} else {
										estado = UtilidadesString
												.getMensajeIdioma(usr,
														"gratuita.gestionInscripciones.estado.alta.denegada");
									}
								} else {
									if (fechaBaja.equals("")) {
										if (fechaDenegacion.equals("")) {
											estado = UtilidadesString
													.getMensajeIdioma(usr,
															"gratuita.gestionInscripciones.estado.baja.pendiente");
										} else {
											// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
											estado = UtilidadesString
													.getMensajeIdioma(usr,
															"gratuita.gestionInscripciones.estado.baja.denegada");
										}
									} else {
										estado = UtilidadesString
												.getMensajeIdioma(usr,
														"gratuita.gestionInscripciones.estado.baja.confirmada");
									}
								}
							} else {
								if (fechaSolicitudBaja.equals("")) {
									estado = UtilidadesString
											.getMensajeIdioma(usr,
													"gratuita.gestionInscripciones.estado.alta.confirmada");
									// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
								} else {
									if (fechaBaja.equals("")) {
										if (fechaDenegacion.equals("")) {
											estado = UtilidadesString
													.getMensajeIdioma(usr,
															"gratuita.gestionInscripciones.estado.baja.pendiente");
										} else {
											// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
											estado = UtilidadesString
													.getMensajeIdioma(usr,
															"gratuita.gestionInscripciones.estado.baja.denegada");
										}
									} else {
										estado = UtilidadesString
												.getMensajeIdioma(usr,
														"gratuita.gestionInscripciones.estado.baja.confirmada");
									}
								}
							}
						}
						elems[0] = null;

						if (isPermitidoInscripcionGuardia) {
							//compobamos que el turno este validado y no este en baja
							//if(inscripcionTurnoSeleccionada!=null && inscripcionTurnoSeleccionada.getFechaValidacion()!=null && !inscripcionTurnoSeleccionada.getFechaValidacion().equalsIgnoreCase("")){
							//&& (inscripcionTurnoSeleccionada.getFechaBaja()==null || inscripcionTurnoSeleccionada.getFechaBaja().equals(""))){

							if (obligatoriedad.equals("2")
									|| obligatoriedad.equals("1")) {
								if (!estaDeBaja) {
									if (fechaSolicitud.equals("")) {
										elems[0] = new FilaExtElement(
												"solicitaralta",
												"solicitaralta",
												SIGAConstants.ACCESS_FULL);
									} else {

										if (!fechaDenegacion.equals("")) {
											if (fechaSolicitudBaja.equals(""))
												elems[0] = new FilaExtElement(
														"solicitaralta",
														"solicitaralta",
														SIGAConstants.ACCESS_FULL);
											else
												elems[0] = new FilaExtElement(
														"solicitarbaja",
														"solicitarbaja",
														SIGAConstants.ACCESS_FULL);
										} else {
											if (!fechaBaja.equals("")) {
												request.getSession()
														.setAttribute(
																"esActualizacion",
																fechaSolicitud);
												//elems[0]=new FilaExtElement("solicitaralta","solicitaralta",SIGAConstants.ACCESS_FULL);
											} else {
												if (fechaSolicitudBaja
														.equals("")) {
													elems[0] = new FilaExtElement(
															"solicitarbaja",
															"solicitarbaja",
															SIGAConstants.ACCESS_FULL);
												}
											}
										}
									}
								} else {
									if (!fechaSolicitud.equals("")
											&& fechaSolicitudBaja.equals("")
											&& fechaBaja.equals("")) {
										elems[0] = new FilaExtElement(
												"solicitarbaja",
												"solicitarbaja",
												SIGAConstants.ACCESS_FULL);
									}
								}

							}
							//}
						}
						String tipoDia = (String) htGuardia
								.get("TIPODIASGUARDIA");
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
		<siga:FilaConIconos pintarEspacio="false" visibleEdicion="false"
			visibleBorrado="false" fila='<%=String.valueOf(recordNumber)%>'
			botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit">

			<input type='hidden' name='validaInscripciones' value='<%=htGuardia.get("VALIDARINSCRIPCIONES")%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=htGuardia.get("IDTURNO")%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=htGuardia.get("IDGUARDIA")%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_22' name='oculto<%=String.valueOf(recordNumber)%>_22' value='<%=htGuardia.get("PORGRUPOS")%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=htGuardia.get("GUARDIAS")%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=fechaSolicitud%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_44' name='oculto<%=String.valueOf(recordNumber)%>_44' value='<%=fechaValidacion%>'>
			<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_55' name='oculto<%=String.valueOf(recordNumber)%>_55' value='<%=idper%>'>
			<td><%=htGuardia.get("GUARDIA")%></td>
			<td>
				<%
					if (((String) htGuardia.get("OBLIGATORIEDAD"))
											.equalsIgnoreCase("0")) {
				%> <siga:Idioma key="gratuita.altaTurnos_2.literal.obligatorias" />
				<%
					} else if (((String) htGuardia
											.get("OBLIGATORIEDAD"))
											.equalsIgnoreCase("1")) {
				%> <siga:Idioma key="gratuita.altaTurnos_2.literal.todasninguna" />
				<%
					} else {
				%> <siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion" /> <%
					}
				%>
			</td>
			<td><%=ScsGuardiasTurnoAdm.obtenerTipoDia(
									(String) htGuardia
											.get("SELECCIONLABORABLES"),
									(String) htGuardia.get("SELECCIONFESTIVOS"),
									usr)%></td>
			<td><%=htGuardia.get("DURACION")%>&nbsp;<siga:Idioma
					key="<%=literalDuracion%>" /></td>
			<td>&nbsp;<%=GstDate.getFormatedDateShort("",
									fechaSolicitud)%></td>
			<td>&nbsp;<%=GstDate.getFormatedDateShort("",
									fechaValor)%></td>
			<td>&nbsp;<%=GstDate.getFormatedDateShort("",
									fechaSolicitudBaja)%></td>
			<td>&nbsp;<%=GstDate.getFormatedDateShort("",
									fechaBaja)%>
			</td>
			<td>&nbsp;<%=estado%>
			</td>
		</siga:FilaConIconos>

		<%
			recordNumber++;
					}
		%>
		<%
			}
		%>
	</siga:TablaCabecerasFijas>
	<html:form action="/JGR_InscribirseEnGuardia" name="FormASolicitar" styleId="FormASolicitar"
		type="com.siga.gratuita.form.InscripcionTGForm">
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
	<html:form action="/JGR_BajaEnGuardia" name="FormASolicitarBaja" styleId="FormASolicitarBaja"
		type="com.siga.gratuita.form.InscripcionTGForm">
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
			var idTurno 				= 'oculto' + fila + '_' + 1;
			var idGuardia 				= 'oculto' + fila + '_' + 2;
			var porGrupos 				= 'oculto' + fila + '_' + 22;
			var idPersona 				= 'oculto' + fila + '_' + 55;
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
		
		function solicitarbaja(fila) {
			var idTurno 				= 'oculto' + fila + '_' + 1;
			var idGuardia 				= 'oculto' + fila + '_' + 2;
			var fechaInscripcion		= 'oculto' + fila + '_' + 4;
			var fechaValidacion		= 'oculto' + fila + '_' + 44;
			var idPersona 				= 'oculto' + fila + '_' + 55;
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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->


</body>
</html>


