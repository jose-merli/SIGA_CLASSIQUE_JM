<!-- editarGuardiaTurno.jsp -->

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

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaAdm"%>
<%@ page import="com.siga.gratuita.form.DefinirGuardiasTurnosForm"%>
<%@ page import="com.siga.gratuita.action.DefinirGuardiasTurnosAction"%>
<%@ page import="org.redabogacia.sigaservices.app.autogen.model.ScsTiposguardias"%>


<bean:define id="diasASeparar" name="DefinirGuardiasTurnosForm" property="diasASeparar" type="String" />
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	ScsGuardiasTurnoBean beanGuardiasTurno = (ScsGuardiasTurnoBean) ses.getAttribute("DATABACKUPPESTANA");
	DefinirGuardiasTurnosForm miform = (DefinirGuardiasTurnosForm) request.getAttribute("DefinirGuardiasTurnosForm");	
	
	//Modo de las pestanhas anteriores:
	String modoPestanha = request.getAttribute("MODOPESTANA") == null ? "" : (String) request.getAttribute("MODOPESTANA");	
	String modo = modoPestanha.toUpperCase();
	
	Hashtable hash = (Hashtable) beanGuardiasTurno.getOriginalHash();
	String inscripcion = (String) ses.getAttribute("inscripcion");
	String action = (String) request.getAttribute("action");
	ses.removeAttribute("inscripcion");

	//Datos del turno:
	Hashtable hashTurno = (Hashtable) request.getSession().getAttribute("turnoElegido");
	//	Hashtable hashTurno =(Hashtable) request.getAttribute("turnoElegido");	
	String turnoAbreviatura = (String) hashTurno.get("ABREVIATURA");
	String turnoNombre = (String) hashTurno.get("NOMBRE");
	String turnoArea = (String) hashTurno.get("AREA");
	String turnoMateria = (String) hashTurno.get("MATERIA");
	String turnoZona = (String) hashTurno.get("ZONA");
	String turnoSubzona = (String) hashTurno.get("SUBZONA");
	String turnoPartidoJudicial = (String) hashTurno.get("PARTIDOJUDICIAL");

	Vector campos = (Vector) request.getSession().getAttribute("campos");
	String[] dato1 = { usr.getLocation() };
	ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(usr);
	String condicion = " where " + ScsOrdenacionColasBean.C_IDORDENACIONCOLAS + "=" + (String) hash.get("IDORDENACIONCOLAS") + " ";
	Vector vOrdenacion = ordenacion.select(condicion);
	ScsOrdenacionColasBean orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);

	String cOrd = "";
	String[] dato2;

	try {
		ScsGuardiasTurnoAdm guardAdm = new ScsGuardiasTurnoAdm(usr);
		cOrd = guardAdm.getOrdenacionLetradosInscritos(orden.getIdOrdenacionColas().intValue());

		dato2 = new String[4];
		dato2[0] = usr.getLocation();
		dato2[1] = (String) hashTurno.get("IDTURNO");
		dato2[2] = (String) hash.get("IDGUARDIA");
		dato2[3] = cOrd;
	} catch (Exception e) {
	}

	//Datos del Colegiado si procede:
	String entrada = (String) ses.getAttribute("entrada");
	String nombrePestanha = null, numeroPestanha = null;
	try {
		Hashtable datosColegiado = (Hashtable) request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String) datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String) datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e) {
		nombrePestanha = "";
		numeroPestanha = "";
	}

	String botonesAccion = "";
	String classCombo = "boxCombo", estiloText = "box", estiloNumber = "boxConsultaNumber";
	boolean soloLectura = false;
	boolean menuCenso = false;

	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	if (entrada != null && entrada.equals("2")) {
		menuCenso = true;
	} else {
		botonesAccion = "V,";
	}

	//Estilo en modo consulta:
	if (modoPestanha.equalsIgnoreCase("ver")) {
		classCombo = "boxConsulta";
		estiloText = "boxConsulta";
		estiloNumber = "boxConsultaNumber";
		soloLectura = true;
	} else {
		classCombo = "boxCombo";
		estiloText = "box";
		estiloNumber = "boxNumber";
		soloLectura = false;
		botonesAccion += "G,R";
	}

	//Datos de la guardia:
	String descripcionGuardia = "", nombreGuardia = "", descripcionFacturacion = "", descripcionPago = "", numeroLetradosGuardia = "";
	String numeroSustitutosGuardia = "", diasGuardia = "", diasPagados = "", diasSeparacionGuardias = "";
	String tipoDiasGuardia = "", diasPeriodo = "", tipoDiasPeriodo = "", tipoDias = "", semana = "";

	nombreGuardia = (String) hash.get(ScsGuardiasTurnoBean.C_NOMBRE);
	descripcionGuardia = (String) hash.get("DESCRIPCION");
	descripcionFacturacion = (String) hash.get("DESCRIPCIONFACTURACION");
	descripcionPago = (String) hash.get("DESCRIPCIONPAGO");
	numeroLetradosGuardia = (String) hash.get("NUMEROLETRADOSGUARDIA");
	numeroSustitutosGuardia = (String) hash.get("NUMEROSUSTITUTOSGUARDIA");
	diasGuardia = (String) hash.get("DIASGUARDIA");
	diasPagados = (String) hash.get("DIASPAGADOS");
	diasSeparacionGuardias = (String) hash.get("DIASSEPARACIONGUARDIAS");
	String porGrupos = (String) hash.get("PORGRUPOS");
	String rotarComponentes = (String) hash.get("ROTARCOMPONENTES");

	tipoDiasGuardia = (String) hash.get(ScsGuardiasTurnoBean.C_TIPODIASGUARDIA);
	diasPeriodo = (String) hash.get(ScsGuardiasTurnoBean.C_DIASPERIODO);
	tipoDiasPeriodo = (String) hash.get(ScsGuardiasTurnoBean.C_TIPODIASPERIODO);

	String[] datoGuardia = new String[2];
	ArrayList guardiaSel = new ArrayList();

	try {
		datoGuardia[0] = usr.getLocation();
		datoGuardia[1] = (String) hashTurno.get("IDTURNO");
		guardiaSel.add((String) hash.get("IDGUARDIASUSTITUCION"));
	} catch (Exception e) {
	}
	
	// JPT: Obtiene los tipos de guardias
	String idTipoGuardiaSeleccionado = (String) hash.get(ScsGuardiasTurnoBean.C_TIPOGUARDIA);
	List listaTiposGuardias = new ArrayList();
	try {
		listaTiposGuardias = DefinirGuardiasTurnosAction.obtenerListaTiposGuardias(usr);
	} catch (Exception e) {
		listaTiposGuardias = new ArrayList();
	}
	if (listaTiposGuardias==null) listaTiposGuardias = new ArrayList();
	
	// JPT: Obtengo si es vinculada
	String sIdGuardiaPrincipal = (String) hash.get("IDGUARDIAPRINCIPAL");
	boolean bVinculada = (sIdGuardiaPrincipal != null && (sIdGuardiaPrincipal.equals("") || sIdGuardiaPrincipal.equals("-1")));	
	String sVinculada = (bVinculada ? "true" : "false");
	String sNoVinculada = (bVinculada ? "false" : "true");		
%>

<html>

<!-- HEAD -->
<head>	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<% if (entrada != null && entrada.equals("2")) { %>
		<siga:TituloExt titulo="censo.fichaCliente.sjcs.guardias.datosGenerales.cabecera" localizacion="censo.fichaCliente.sjcs.guardias.datosGenerales.localizacion" />
	<% } else { %>
		<siga:TituloExt titulo="censo.fichaCliente.sjcs.guardias.datosGenerales.cabecera" localizacion="gratuita.DatosGeneralesGuardia.literal.localizacion" />
	<% } %>
	
	<html:javascript formName="DefinirGuardiasTurnosForm" staticJavascript="false" />	
	
	<script>	
		var modo = "<%=modo%>"; 
	
		function postAccionTurno(){			
			accionComboGuardiaPrincipal();
		}		
	</script>
</head>

<body class="tablaCentralCampos" onload="modificarVarios(); init();">

	<%
		//Entrada desde el menu de Censo:
		if (entrada != null && entrada.equals("2")) {
	%>
		<table class="tablaTitulo" align="center" cellspacing=0>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1" />&nbsp;&nbsp; <%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
					<%	if (numeroPestanha != null && !numeroPestanha.equalsIgnoreCase("")) { %>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<%	} else { %> 
						<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> 
					<%	} %>
				</td>
			</tr>
		</table>
	<% } %>

	<!-- TURNO: Información del Turno que tenemos seleccionado -->
	<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno" desplegable="true" oculto="<%=sVinculada%>">
		<table border="0" align="center" width="100%">
			<tr>
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura" />
				</td>
				<td class="labelText" style="text-align: left">
					<input name="abreviatura" type="text" class="boxConsulta" size="30" maxlength="30" value="<%=UtilidadesString.mostrarDatoJSP(turnoAbreviatura)%>" readonly="true">
				</td>
				
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre" />
				</td>
				<td colspan="3" class="labelText" style="text-align: left">
					<input name="nombre" type="text" class="boxConsulta" size="50" maxlength="100" value="<%=UtilidadesString.mostrarDatoJSP(turnoNombre)%>" readonly="true">
				</td>
			</tr>

			<tr>
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.area" />
				</td>
				<td class="labelText" style="text-align: left">
					<input name="area" type="text" class="boxConsulta" size="25" value="<%=UtilidadesString.mostrarDatoJSP(turnoArea)%>" readonly="true">
				</td>
				
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia" />
				</td>
				<td class="labelText" style="text-align: left">
					<input name="materia" type="text" class="boxConsulta" size="25" value="<%=UtilidadesString.mostrarDatoJSP(turnoMateria)%>" readonly="true">
				</td>
			</tr>

			<tr>
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.zona" />
				</td>
				<td class="labelText" style="text-align: left">
					<input name="zona" type="text" class="boxConsulta" size="30" value="<%=UtilidadesString.mostrarDatoJSP(turnoZona)%>" readonly="true">
				</td>
				
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona" />
				</td>
				<td class="labelText" style="text-align: left">
					<input name="subzona" type="text" class="boxConsulta" size="25" value="<%=UtilidadesString.mostrarDatoJSP(turnoSubzona)%>" readonly="true">
				</td>
				
				<td class="labelText" style="text-align: left">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial" />
				</td>
				<td class="labelTextValor" style="text-align: left">
					<%=UtilidadesString.mostrarDatoJSP(turnoPartidoJudicial)%>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>


	<html:form action="DefinirGuardiasTurnosAction.do" method="POST" target="_top">
		<html:hidden property="modo" value="" />
		<html:hidden property="idInstitucionPestanha" />
		<html:hidden property="idTurnoPestanha" />
		<html:hidden property="idGuardiaPestanha" />
		<html:hidden name="DefinirGuardiasTurnosForm" property="letradosSustitutos" value='0' />
		<html:hidden name="DefinirGuardiasTurnosForm" property="diasPagados" value='1' />
		<input type="hidden" name="guardia" value="<%=(String)hash.get("IDGUARDIA")%>">

		<!-- GUARDIA -->
		<!-- gratuita.listarGuardias.literal.guardia -->
		<siga:ConjCampos leyenda="gratuita.guardiasTurno.literal.datosgenerales" desplegable="true" oculto="false">			
			<table align="center" border="0" width="100%">
				<tr>
					<td class="labelText" style="width: 100px;">
						<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre" />&nbsp;(*)
					</td>
					<td>
						<html:text name="DefinirGuardiasTurnosForm" styleId="nombreGuardia"
							property="nombreGuardia" size="40" maxlength="30"
							styleClass="<%=estiloText%>" value="<%=nombreGuardia%>"
							readonly="<%=soloLectura%>" />
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion" />&nbsp;(*)
					</td>
					<td>
						<html:text name="DefinirGuardiasTurnosForm"
							property="descripcion"  styleId="descripcion"  size="40" maxlength="1024"
							styleClass="<%=estiloText%>" value="<%=descripcionGuardia%>"
							readonly="<%=soloLectura%>"/>
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionFacturacion" />
					</td>
					<td>
						<html:textarea name="DefinirGuardiasTurnosForm" styleId="descripcionFacturacion" property="descripcionFacturacion"
							onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" 
							style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;" styleClass="<%=estiloText%>"							
							readOnly="<%=soloLectura%>" value="<%=descripcionFacturacion%>" />
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionPago" />
					</td>
					<td>
						<html:textarea name="DefinirGuardiasTurnosForm" styleId="descripcionPago" property="descripcionPago"
							onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" 	 							
							style="overflow-y:auto; overflow-x:hidden; width:300px; height:50px; resize:none;" styleClass="<%=estiloText%>"
							readOnly="<%=soloLectura%>" value="<%=descripcionPago%>" />
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.turno.guardia.literal.deSustitucion" />
					</td>
					<td class="labelText">
						<%if (hash.get("IDGUARDIASUSTITUCION") != null && !((String) hash.get("IDGUARDIASUSTITUCION")).equals("")) {%> 
							<siga:ComboBD nombre="guardiaDeSustitucion" tipo="guardias"
								clase="boxConsulta" readOnly="true"
								parametro="<%=datoGuardia%>" elementoSel="<%=guardiaSel%>"
								ancho="300" /> 
						<% } %>
					</td>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.turno.guardia.literal.tipoGuardia" />
					</td>
					<td>
						<% if (soloLectura) {
							String textoTipoGuardia = "";
							if (idTipoGuardiaSeleccionado!=null && !idTipoGuardiaSeleccionado.equals("")) {
								for (int i=0; i<listaTiposGuardias.size(); i++) {
									ScsTiposguardias objTipoGuardia = (ScsTiposguardias) listaTiposGuardias.get(i);
									if (objTipoGuardia.getIdtipoguardia() != null && idTipoGuardiaSeleccionado.equals(objTipoGuardia.getIdtipoguardia().toString())) {
										textoTipoGuardia = UtilidadesMultidioma.getDatoMaestroIdioma(objTipoGuardia.getDescripcion(), usr);
									}
								}
							}%>
								<input type="text" name="tiposGuardias" class="boxConsulta" value="<%=textoTipoGuardia%>" readOnly="true" />			
							
						<% } else { %>
							<html:select styleId="tiposGuardias" styleClass="boxCombo" style="width:150px;" property="idTipoGuardiaSeleccionado"  value="<%=idTipoGuardiaSeleccionado%>"> 		
								<html:option value="">&nbsp;</html:option>	
								<%for (int i=0; i<listaTiposGuardias.size(); i++) {
									ScsTiposguardias objTipoGuardia = (ScsTiposguardias) listaTiposGuardias.get(i);
								%>							
									<html:option value="<%=UtilidadesString.mostrarDatoJSP(objTipoGuardia.getIdtipoguardia())%>"> <%=UtilidadesMultidioma.getDatoMaestroIdioma(objTipoGuardia.getDescripcion(), usr)%></html:option> 
								<%}%>					
							</html:select>
						<% } %>							
					</td>
				</tr>
				
				<logic:empty name="DefinirGuardiasTurnosForm" property="guardiasVinculadas">
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.guardiasTurno.literal.turnoPrincipal" />
						</td>
						<td>
							<html:select styleId="turnosPrincipales" styleClass="boxCombo" style="width:200px;" property="idTurnoPrincipal">
								<bean:define id="turnosPrincipales" name="DefinirGuardiasTurnosForm" property="turnosPrincipales" type="java.util.Collection" />
								<html:optionsCollection name="turnosPrincipales" value="idTurno" label="nombre" />
							</html:select>							
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.guardiasTurno.literal.guardiaPrincipal" />
						</td>
						<td>
							<html:select styleId="guardiasPrincipales" styleClass="boxCombo" style="width:180px;" property="idGuardiaPrincipal" onchange="accionComboGuardiaPrincipal();">								
								<bean:define id="guardiasPrincipales" name="DefinirGuardiasTurnosForm" property="guardiasPrincipales" type="java.util.Collection" />
								<html:optionsCollection name="guardiasPrincipales" value="idGuardia" label="nombre" /> 
							</html:select>
						</td>
					</tr>
				</logic:empty>
				
				<logic:notEmpty name="DefinirGuardiasTurnosForm" property="guardiasVinculadas">
					<tr>
						<td colspan="4">
							<div style="position: relative; height: 100%; width: 100%; overflow-y: auto">
								<table class="tablaCampos" border='1' align='center' width='100%' height="10" cellspacing='0' cellpadding='0' style='table-layout: fixed'>
									<tr class='tableTitle'>
										<td align='center' width='50%'>
											<siga:Idioma key="gratuita.guardiasTurno.literal.turnoVinculado" />
										</td>
										<td align='center' width='50%'>
											<siga:Idioma key="gratuita.guardiasTurno.literal.guardiaVinculada" />
										</td>
									</tr>
									
									<logic:iterate name="DefinirGuardiasTurnosForm" property="guardiasVinculadas" id="guardiaVinculada" indexId="index">
										<tr class="filaTablaImpar">
											<td align='left' width='50%'>
												<c:out value="${guardiaVinculada.turno.abreviatura}" />
											</td>
											<td align='left' width='50%'>
												<c:out value="${guardiaVinculada.nombre}" />
											</td>
										</tr>
									</logic:iterate>
								</table>
							</div>
						</td>
					</tr>
				</logic:notEmpty>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.guardiasTurno.literal.configuracioncola" desplegable="<%=sVinculada%>" oculto="<%=sNoVinculada%>">
			<table align="center" border="0" width="100%">
				<tr>
					<td class="labelText" style="text-align: left" width="15%">
						<input type=checkbox id="idPorGrupos" name="porGrupos" onClick="comprobarPorGrupos();" value=1 <%=(porGrupos.equals("1"))?"checked":""%> />
						<label for="idPorGrupos">
							<siga:Idioma key="gratuita.guardiasTurno.literal.porgrupos" />
						</label>
					</td>
					<td class="labelText" style="text-align: right" width="30%">
						<div id="textoLetrado" style="display: none">
							<siga:Idioma key="gratuita.listarGuardiasTurno.literal.letradosGuardia" />
							&nbsp;(*)
						</div>
						<div id="textoGrupos" style="display: none">
							<siga:Idioma key="gratuita.listarGuardiasTurno.literal.letradosGrupo" />&nbsp;(*)
						</div> 
						&nbsp;&nbsp;
					</td>
					<td>
						<html:text name="DefinirGuardiasTurnosForm" property="letradosGuardia" size="6" maxlength="6" styleClass="<%=estiloNumber%>" value='<%=numeroLetradosGuardia%>' readonly="<%=soloLectura%>" />
					</td>
					<td class="labelText" style="text-align: left" width="40%">
						<div id="divRotacion">
							<input type=checkbox id="idRotacion" name="rotarComponentes" value=1 <%=(rotarComponentes.equals("1"))?"checked":""%> /> 
							<label for="idRotacion">
								<siga:Idioma key="gratuita.guardiasTurno.literal.rotarcomponentes" />
							</label>
						</div>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<!-- PESOS ORDENACION -->
		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion" desplegable="<%=sVinculada%>" oculto="<%=sNoVinculada%>">
			<table border="0" width="100%" align="center">
				<tr>
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio" />
					</td>
					<td class="labelText" style="text-align: right">
						<% if (!modoPestanha.equalsIgnoreCase("ver")) {%> 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_1">
								<html:option value="0">
									<siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir" />
								</html:option>
								<html:option value="1">
									<siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico" />
								</html:option>
								<html:option value="2">
									<siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad" />
								</html:option>
								<html:option value="3">
									<siga:Idioma key="gratuita.maestroTurnos.literal.edad" />
								</html:option>
								<html:option value="4">
									<siga:Idioma key="gratuita.maestroTurnos.literal.cola" />
								</html:option>
							</html:select> 
							&nbsp; 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_1">
								<html:option value="A">
									<siga:Idioma key="gratuita.maestroTurnos.literal.ascendente" />
								</html:option>
								<html:option value="D">
									<siga:Idioma key="gratuita.maestroTurnos.literal.descendente" />
								</html:option>
							</html:select> 
							
						<% } else {
								String valor1 = "";
								if (miform.getCrit_1().equalsIgnoreCase("0")) {
									valor1 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
								} else if (miform.getCrit_1().equalsIgnoreCase("1")) {
									valor1 = UtilidadesString.getMensajeIdioma(usr,	"gratuita.maestroTurnos.literal.alfabetico");
								} else if (miform.getCrit_1().equalsIgnoreCase("2")) {
									valor1 = UtilidadesString.getMensajeIdioma(usr,	"gratuita.maestroTurnos.literal.antiguedad");
								} else if (miform.getCrit_1().equalsIgnoreCase("3")) {
									valor1 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
								} else if (miform.getCrit_1().equalsIgnoreCase("4")) {
									valor1 = UtilidadesString.getMensajeIdioma(usr,	"gratuita.maestroTurnos.literal.cola");
								}
								String orden1 = "";
								if (miform.getOrd_1().equalsIgnoreCase("A")) {
									orden1 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
								} else if (miform.getOrd_1().equalsIgnoreCase("D")) {
									orden1 = UtilidadesString.getMensajeIdioma(usr,	"gratuita.maestroTurnos.literal.descendente");
								}
						%> 
							<input type="text" name="crit_1" class="boxConsulta" value="<%=valor1%>"  readOnly="true" />&nbsp;<input type="text" name="ord_1" class="boxConsulta" value="<%=orden1%>" readOnly="true" /> 
						<% } %>
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.segundoCriterio" />
					</td>
					<td class="labelText" style="text-align: right">
						<%if (!modoPestanha.equalsIgnoreCase("ver")) {%> 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_2">
								<html:option value="0">
									<siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir" />
								</html:option>
								<html:option value="1">
									<siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico" />
								</html:option>
								<html:option value="2">
									<siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad" />
								</html:option>
								<html:option value="3">
									<siga:Idioma key="gratuita.maestroTurnos.literal.edad" />
								</html:option>
								<html:option value="4">
									<siga:Idioma key="gratuita.maestroTurnos.literal.cola" />
								</html:option>
							</html:select> 
							&nbsp; 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_2">
								<html:option value="A">
									<siga:Idioma key="gratuita.maestroTurnos.literal.ascendente" />
								</html:option>
								<html:option value="D">
									<siga:Idioma key="gratuita.maestroTurnos.literal.descendente" />
								</html:option>
							</html:select>
							 
						<% } else {
								String valor2 = "";
								if (miform.getCrit_2().equalsIgnoreCase("0")) {
									valor2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
								} else if (miform.getCrit_2().equalsIgnoreCase("1")) {
									valor2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
								} else if (miform.getCrit_2().equalsIgnoreCase("2")) {
									valor2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
								} else if (miform.getCrit_2().equalsIgnoreCase("3")) {
									valor2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
								} else if (miform.getCrit_2().equalsIgnoreCase("4")) {
									valor2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
								}
								String orden2 = "";
								if (miform.getOrd_2().equalsIgnoreCase("A")) {
									orden2 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
								} else if (miform.getOrd_2().equalsIgnoreCase("D")) {
									orden2 = UtilidadesString.getMensajeIdioma(usr,	"gratuita.maestroTurnos.literal.descendente");
								}
						%> 
							<input type="text" name="crit_2" class="boxConsulta" value="<%=valor2%>" readOnly="true" />&nbsp;<input type="text" name="ord_2" class="boxConsulta" value="<%=orden2%>" readOnly="true" /> 
						<% } %>
					</td>
				</tr>

				<tr>
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.terceroCriterio" />
					</td>
					<td class="labelText" style="text-align: right">
						<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_3">
								<html:option value="0">
									<siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir" />
								</html:option>
								<html:option value="1">
									<siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico" />
								</html:option>
								<html:option value="2">
									<siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad" />
								</html:option>
								<html:option value="3">
									<siga:Idioma key="gratuita.maestroTurnos.literal.edad" />
								</html:option>
								<html:option value="4">
									<siga:Idioma key="gratuita.maestroTurnos.literal.cola" />
								</html:option>
							</html:select> 
							&nbsp; 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_3">
								<html:option value="A">
									<siga:Idioma key="gratuita.maestroTurnos.literal.ascendente" />
								</html:option>
								<html:option value="D">
									<siga:Idioma key="gratuita.maestroTurnos.literal.descendente" />
								</html:option>
							</html:select> 
						<% } else {
								String valor3 = "";
								if (miform.getCrit_3().equalsIgnoreCase("0")) {
									valor3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
								} else if (miform.getCrit_3().equalsIgnoreCase("1")) {
									valor3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
								} else if (miform.getCrit_3().equalsIgnoreCase("2")) {
									valor3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
								} else if (miform.getCrit_3().equalsIgnoreCase("3")) {
									valor3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
								} else if (miform.getCrit_3().equalsIgnoreCase("4")) {
									valor3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
								}
								String orden3 = "";
								if (miform.getOrd_3().equalsIgnoreCase("A")) {
									orden3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
								} else if (miform.getOrd_3().equalsIgnoreCase("D")) {
									orden3 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
								}
						%> 
							<input type="text" name="crit_3" class="boxConsulta" value="<%=valor3%>" readOnly="true" />&nbsp;<input type="text" name="ord_3" class="boxConsulta" value="<%=orden3%>" readOnly="true" /> 
						<% } %>
					</td>
					
					<td class="labelText" style="text-align: left">
						<siga:Idioma key="gratuita.maestroTurnos.literal.cuartoCriterio" />
					</td>
					<td class="labelText" style="text-align: right">
						<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_4">
								<html:option value="0">
									<siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir" />
								</html:option>
								<html:option value="1">
									<siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico" />
								</html:option>
								<html:option value="2">
									<siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad" />
								</html:option>
								<html:option value="3">
									<siga:Idioma key="gratuita.maestroTurnos.literal.edad" />
								</html:option>
								<html:option value="4">
									<siga:Idioma key="gratuita.maestroTurnos.literal.cola" />
								</html:option>
							</html:select> 
							&nbsp; 
							<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_4">
								<html:option value="A">
									<siga:Idioma key="gratuita.maestroTurnos.literal.ascendente" />
								</html:option>
								<html:option value="D">
									<siga:Idioma key="gratuita.maestroTurnos.literal.descendente" />
								</html:option>
							</html:select> 
						<% } else {
								String valor4 = "";
								if (miform.getCrit_4().equalsIgnoreCase("0")) {
									valor4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
								} else if (miform.getCrit_4().equalsIgnoreCase("1")) {
									valor4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
								} else if (miform.getCrit_4().equalsIgnoreCase("2")) {
									valor4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
								} else if (miform.getCrit_4().equalsIgnoreCase("3")) {
									valor4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
								} else if (miform.getCrit_4().equalsIgnoreCase("4")) {
									valor4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
								}
								String orden4 = "";
								if (miform.getOrd_4().equalsIgnoreCase("A")) {
									orden4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
								} else if (miform.getOrd_4().equalsIgnoreCase("D")) {
									orden4 = UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
								}
						%> 
							<input type="text" name="crit_4" class="boxConsulta" value="<%=valor4%>" readOnly="true" />&nbsp;<input type="text" name="ord_4" class="boxConsulta" value="<%=orden4%>" readOnly="true" /> 
						<% } %>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.guardiasTurno.literal.configuracioncalendarios" desplegable="<%=sVinculada%>" oculto="<%=sNoVinculada%>">
			<table align="center" border="0" width="100%">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardias.literal.duracion" />&nbsp;(*)
					</td>
					<td>
						<html:text name="DefinirGuardiasTurnosForm" property="duracion" size="4" maxlength="4" styleClass="<%=estiloNumber%>" value='<%=diasGuardia%>' readonly="<%=soloLectura%>" /> &nbsp; 
						<% if (modoPestanha.equalsIgnoreCase("ver")) { %> 
							<html:text name="DefinirGuardiasTurnosForm" property="tipoDiasGuardia" readOnly="true" styleClass="boxConsulta" value="<%=DefinirGuardiasTurnosAction.obtenerTipoDiaPeriodo(tipoDiasGuardia, usr)%>" />
						<% } else { %> 
							<html:select name="DefinirGuardiasTurnosForm" property="tipoDiasGuardia" size="1" value="<%=tipoDiasGuardia%>" styleClass="boxCombo">
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES%>" key="gratuita.combo.literal.diasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_SEMANAS_NATURALES%>" key="gratuita.combo.literal.semanasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_QUINCENAS_NATURALES%>" key="gratuita.combo.literal.quincenasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_MESES_NATURALES%>" key="gratuita.combo.literal.mesesNaturales" />
							</html:select> 
						<% } %>
					</td>
					
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.maestroGuardias.literal.diasSeparacion" />&nbsp;(*)
						&nbsp;&nbsp; 
						<html:text name="DefinirGuardiasTurnosForm" property="diasSeparacion" size="2" maxlength="2" styleClass="<%=estiloNumber%>" value='<%=diasSeparacionGuardias%>' readonly="<%=soloLectura%>" />
					</td>
				</tr>

				<tr>
					<td class="labelText">
						<html:checkbox name="DefinirGuardiasTurnosForm" property="checkDiasPeriodo" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarDiasPeriodo()" disabled="<%=soloLectura%>" />
						&nbsp; <siga:Idioma key="gratuita.calendarioGuardias.literal.periodo" />
					</td>
					<td>
						<html:text name="DefinirGuardiasTurnosForm" property="diasPeriodo" styleId="diasPeriodo" size="4" maxlength="4" styleClass="<%=estiloNumber%>" value="<%=diasPeriodo%>" readonly="<%=soloLectura%>" /> 
						&nbsp; 
						<% if (modoPestanha.equalsIgnoreCase("ver")) { %> 
							<html:text name="DefinirGuardiasTurnosForm" property="tipoDiasPeriodo" styleId="tipoDiasPeriodo" readOnly="true" styleClass="boxConsulta" value="<%=DefinirGuardiasTurnosAction.obtenerTipoDiaPeriodo(tipoDiasPeriodo, usr)%>" />
						<% } else { %> 
							<html:select name="DefinirGuardiasTurnosForm" property="tipoDiasPeriodo" styleId="tipoDiasPeriodo" size="1" value="<%=tipoDiasPeriodo%>" styleClass="boxCombo">
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES%>" key="gratuita.combo.literal.diasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_SEMANAS_NATURALES%>" key="gratuita.combo.literal.semanasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_QUINCENAS_NATURALES%>" key="gratuita.combo.literal.quincenasNaturales" />
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_MESES_NATURALES%>" key="gratuita.combo.literal.mesesNaturales" />
							</html:select> 
						<% } %>
					</td>
					
					<td rowspan="2" class="labelText" valign="center" align="middle">
						<html:checkbox name="DefinirGuardiasTurnosForm" property="hayDiasASeparar" value="<%=ClsConstants.DB_TRUE%>" disabled="true" styleId="hayDiasASeparar" /> 
						<siga:Idioma key="gratuita.guardiasTurno.literal.separarGuardia" /> <%=diasASeparar%>
					</td>
					<td>
						<IMG border=0 src="<%=app+"/html/imagenes/"%>help.gif" alt="<siga:Idioma key="gratuita.guardiasTurno.ayuda.separarGuardia"/>">
					</td>
				</tr>

				<tr>
					<td colspan="4">
						<table>
							<tr>
								<td class="labelText" rowspan="2" style="width: 96px; padding-left: 7px;">
									<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.tipodias" />
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.combo.literal.laborables" />
								</td>
								
								<td>
									<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
										<html:button property="boton" onclick="marcarTodosLaborables()" styleId="btnMarcarTodosLaborables" styleClass="button btnMarcarDesmarcar" disabled="<%=soloLectura%>">
											<siga:Idioma key="general.boton.marcarTodos" />
										</html:button> 
									<% } %>
								</td>
								<td class="labelText" style="width: 255px;">
									<siga:Idioma key="gratuita.checkbox.literal.Lunes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesLunes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
									 
									<siga:Idioma key="gratuita.checkbox.literal.Martes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesMartes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
									 
									<siga:Idioma key="gratuita.checkbox.literal.Miercoles" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesMiercoles" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
									 
									<siga:Idioma key="gratuita.checkbox.literal.Jueves" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesJueves" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Viernes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesViernes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Sabado" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesSabado" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
								</td>
								
								<td>
									<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
										<html:button property="boton" styleId="btnDesmarcarTodosLaborables" onclick="desmarcarTodosLaborables()" styleClass="button btnMarcarDesmarcar" disabled="<%=soloLectura%>">
											<siga:Idioma key="general.boton.desmarcarTodos" />
										</html:button> 
									<% } %>
								</td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma
										key="gratuita.combo.literal.festivos" />
								</td>
								<td>
									<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
										<html:button property="boton" onclick="marcarTodosFestivos()" styleId="btnMarcarTodosFestivos" styleClass="button btnMarcarDesmarcar" disabled="<%=soloLectura%>"> 
											<siga:Idioma key="general.boton.marcarTodos" />
										</html:button> 
									<% } %>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.checkbox.literal.Lunes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosLunes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Martes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosMartes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Miercoles" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosMiercoles" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
									 
									<siga:Idioma key="gratuita.checkbox.literal.Jueves" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosJueves" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
									 
									<siga:Idioma key="gratuita.checkbox.literal.Viernes" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosViernes" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Sabado" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosSabado" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" /> 
									
									<siga:Idioma key="gratuita.checkbox.literal.Domingo" /> 
									<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosDomingo" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=soloLectura%>" onclick="modificarSeleccionDias()" />
								</td>
								
								<td>
									<% if (!modoPestanha.equalsIgnoreCase("ver")) { %> 
										<html:button property="boton" onclick="desmarcarTodosFestivos()" styleClass="button btnMarcarDesmarcar" styleId="btnDesmarcarTodosFestivos" disabled="<%=soloLectura%>"> 
											<siga:Idioma key="general.boton.desmarcarTodos" />
										</html:button> 
									<% } %>
								</td>
							</tr>
						</table>
					</td>

					<td class="labelText" width="12%">
						<div id="labelSeleccionDias">
							<siga:Idioma key="gratuita.guardiasTurno.literal.seleccionBase" />
							:
						</div>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		
		<ajax:select
			baseUrl="/SIGA/DefinirGuardiasTurnosAction.do?modo=getAjaxGuardias"
			source="turnosPrincipales" target="guardiasPrincipales"
			parameters="idTurnoPrincipal={idTurnoPrincipal}"
			postFunction="postAccionTurno" />
			
	</html:form>

	<% if (!menuCenso) { %>
		<siga:ConjBotonesAccion botones="<%=botonesAccion%>" />
	<% } %>

	<!-------------------- INICIO: Funciones JavaScript -------------------->
	<script language="JavaScript">

		function init() {
			var modo="<%=modoPestanha%>";		
			if (modo=="VER"){
				jQuery("#turnosPrincipales").attr("disabled","disabled");
				jQuery("#guardiasPrincipales").attr("disabled","disabled");		
			}
			<%if(soloLectura){%>
				jQuery("input").not("*[type=button]").not("*.boxConsulta").not("*.boxConsultaNumber").attr("disabled","disabled");
				jQuery("select").attr("readonly","readonly");		
			<%}%>
			jQuery("#hayDiasASeparar").attr("disabled","disabled");
			//aalg: INC_08301_SIGA
			<% String idguardia=(String) hash.get("IDGUARDIA");
			   if(idguardia==""){%>
				jQuery("#turnosPrincipales").removeAttr("disabled");
				jQuery("#guardiasPrincipales").removeAttr("disabled");
			<%}else{%>
				if (jQuery("#turnosPrincipales").val()==-1){
					jQuery("#turnosPrincipales").parent().parent().hide();			
				}
					
				jQuery("#turnosPrincipales").attr("disabled","disabled");
				jQuery("#guardiasPrincipales").attr("disabled","disabled");
			<%}%>
		}

		//Modifica los checks desde el onload
		function modificarVarios () {
			modificarDiasPeriodo ();
			modificarSeleccionDias ();
		} //modificarVarios ()
	
		//Habilita o no el input y combo de dias de periodo
		function modificarDiasPeriodo () {
			var deshabilitar = (document.getElementById("idGuardiaPrincipal") && 
					!(document.getElementById("idGuardiaPrincipal").value=='' || document.getElementById("idGuardiaPrincipal").value=='-1'));
			
			if (document.DefinirGuardiasTurnosForm.checkDiasPeriodo.checked && !deshabilitar){
				jQuery("#diasPeriodo").removeAttr("disabled");
				jQuery("#tipoDiasPeriodo").removeAttr("disabled");
	
			} else {
				document.DefinirGuardiasTurnosForm.diasPeriodo.value = "0";
				jQuery("#diasPeriodo").attr("disabled","disabled");
				document.DefinirGuardiasTurnosForm.tipoDiasPeriodo.value = "";
				jQuery("#tipoDiasPeriodo").attr("disabled","disabled");
			}
		} //modificarDiasPeriodo ()
	
		// Marca todos los checkBox de la seleccion de laborables
		function marcarTodosLaborables () {
			document.DefinirGuardiasTurnosForm.seleccionLaborablesLunes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesMartes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesMiercoles.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesJueves.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesViernes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesSabado.checked = true;
			
			modificarSeleccionDias ();
		} //marcarTodosLaborables ()
	
		// Desmarca todos los checkBox de la seleccion de laborables
		function desmarcarTodosLaborables () {
			document.DefinirGuardiasTurnosForm.seleccionLaborablesLunes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesMartes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesMiercoles.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesJueves.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesViernes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionLaborablesSabado.checked = false;
			
			modificarSeleccionDias ();
		} //desmarcarTodosLaborables ()
	
		// Marca todos los checkBox de la seleccion de festivos
		function marcarTodosFestivos () {
			document.DefinirGuardiasTurnosForm.seleccionFestivosLunes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosMartes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosMiercoles.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosJueves.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosViernes.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosSabado.checked = true;
			document.DefinirGuardiasTurnosForm.seleccionFestivosDomingo.checked = true;
			
			modificarSeleccionDias ();
		} //marcarTodosFestivos ()
	
		// Desmarca todos los checkBox de la seleccion de festivos
		function desmarcarTodosFestivos () {
			document.DefinirGuardiasTurnosForm.seleccionFestivosLunes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosMartes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosMiercoles.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosJueves.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosViernes.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosSabado.checked = false;
			document.DefinirGuardiasTurnosForm.seleccionFestivosDomingo.checked = false;
			
			modificarSeleccionDias ();
		} //desmarcarTodosFestivos ()
	
		// Muestra la seleccion de dias en texto
		//(PRESENTE TAMBIEN EN definirGuardiaTurno.jsp)
		function modificarSeleccionDias () {
			seleccionBase = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionBase")%>";
			seleccionLaborables = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionLaborables")%>";
			seleccionFestivos = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.guardiasTurno.literal.seleccionFestivos")%>";
			L = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Lunes")%>";
			M = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Martes")%>";
			X = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Miercoles")%>";
			J = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Jueves")%>";
			V = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Viernes")%>";
			S = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Sabado")%>";
			D = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.checkbox.literal.Domingo")%>";
			
	        seleccion = "";
	        
	        //escribiendo los laborables
	        selTemp = "";
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesLunes.checked == true)		selTemp += L;
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesMartes.checked == true)		selTemp += M;
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesMiercoles.checked == true)	selTemp += X;
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesJueves.checked == true)		selTemp += J;
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesViernes.checked == true)		selTemp += V;
	        if (document.DefinirGuardiasTurnosForm.seleccionLaborablesSabado.checked == true)		selTemp += S;
	        
	        if (selTemp == L+M+X+J+V)			seleccion += seleccionLaborables + " " + L + "-" + V;
	        else if (selTemp == L+M+X+J+V+S)	seleccion += seleccionLaborables + " " + L + "-" + S;
	        else if (! selTemp == "")			seleccion += seleccionLaborables + " " + selTemp;
	        
	        //escribiendo los festivos
	        selTemp = "";
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosLunes.checked == true)		selTemp += L;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosMartes.checked == true)		selTemp += M;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosMiercoles.checked == true)	selTemp += X;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosJueves.checked == true)		selTemp += J;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosViernes.checked == true)	selTemp += V;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosSabado.checked == true)		selTemp += S;
	        if (document.DefinirGuardiasTurnosForm.seleccionFestivosDomingo.checked == true)	selTemp += D;
	        
	        if (seleccion != "" && selTemp != "") seleccion += ", ";
	        
	        if (selTemp == L+M+X+J+V)			seleccion += seleccionFestivos + " " + L + "-" + V;
	        else if (selTemp == L+M+X+J+V+S)	seleccion += seleccionFestivos + " " + L + "-" + S;
	        else if (selTemp == L+M+X+J+V+S+D)	seleccion += seleccionFestivos + " " + L + "-" + D;
	        else if (! selTemp == "")			seleccion += seleccionFestivos + " " + selTemp;
	        
	        //mostrando en pantalla
	        document.getElementById ("labelSeleccionDias").innerHTML = seleccionBase + ": " + seleccion;
		} //modificarSeleccionDias ()
	
		// Asociada al boton Restablecer
		function accionRestablecer() 	{
			jQuery("#mainWorkArea", window.top.document).contents().find(".framePestanas").contents().find("#pestana\\.justiciagratuitaguardia\\.datosgenerales").click();
		}

		// Asociada al boton Guardar
		function accionGuardar() {	
			sub();
			if(document.getElementById("idTurnoPrincipal")&&document.getElementById("idTurnoPrincipal").value !="-1" && document.getElementById("idTurnoPrincipal").value !=""){
					error = '';
					if(document.getElementById("idGuardiaPrincipal").value =="-1" || document.getElementById("idGuardiaPrincipal").value ==""){
						error += "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.guardiaPrincipal'/>"+ '\n';				
					}
					
					if(document.getElementById("nombreGuardia").value ==""){
						error += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nombre'/>"+ '\n';
					}
					
					if(document.getElementById("descripcion").value ==""){
						error += "<siga:Idioma key='errors.required' arg0='gratuita.maestroTurnos.literal.descripcion'/>"+ '\n';							
					}
					
					if(error ==''){
						document.DefinirGuardiasTurnosForm.modo.value = "modificar";
				       	document.DefinirGuardiasTurnosForm.target = "submitArea";
				       	jQuery("form[name=DefinirGuardiasTurnosForm]")
						.find("input:disabled, textarea:disabled, select:disabled").removeAttr("disabled");
						document.DefinirGuardiasTurnosForm.submit();
						
					}else{
						fin();
						alert(error);
					 	return false;					
					}
					
				}else{		
					if (validateDefinirGuardiasTurnosForm(document.DefinirGuardiasTurnosForm)) {
						if (document.DefinirGuardiasTurnosForm.duracion.value==0) {
							alert('<siga:Idioma key="gratuita.maestroTurnos.literal.cero"/>');
						} else { 
							document.forms[0].target="submitArea";
							document.forms[0].modo.value="modificar";
							document.forms[0].submit();
						}
					}
			}
			
			comprobarPorGrupos();
		}

		// Asociada al boton Volver
		function accionVolver() 	{
			document.forms[0].action="JGR_DefinirTurnos.do";
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}
	
		// Refresco
		function refrescarLocal() {
			document.forms[0].target = "_parent";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}
	
		function accionComboGuardiaPrincipal(){
			if(document.getElementById("idGuardiaPrincipal")){
				var deshabilitar = !(document.getElementById("idGuardiaPrincipal").value==''||document.getElementById("idGuardiaPrincipal").value=='-1');
				if (deshabilitar){
					jQuery("form[name=DefinirGuardiasTurnosForm]")
					.find("input, textarea, select")
					.not("input[type=hidden],input.inputDisabled,#turnosPrincipales,#guardiasPrincipales,#nombreGuardia,#descripcion,#descripcionFacturacion,#descripcionPago")
					.attr("disabled","disabled");				
					jQuery("input.btnMarcarDesmarcar").hide();			
				}else{
					jQuery("form[name=DefinirGuardiasTurnosForm]")
					.find("input, textarea, select").not("input.inputDisabled").removeAttr("disabled");
					jQuery("input.btnMarcarDesmarcar").show();
				}
			}
		}

		function comprobarPorGrupos(){
			if(document.getElementById("idPorGrupos").checked) {
				document.getElementById("textoLetrado").style.display = "none";
				document.getElementById("textoGrupos").style.display = "inline-block";
				document.getElementById("divRotacion").style.display = "inline-block";
			} else {
				document.getElementById("textoLetrado").style.display = "inline-block";
				document.getElementById("textoGrupos").style.display = "none";
				document.getElementById("divRotacion").style.display = "none";
			}
		}
	
		accionComboGuardiaPrincipal();
		comprobarPorGrupos();
	</script>
	<!-------------------- FIN: Funciones JavaScript -------------------->

	<!-------------------- INICIO: SUBMIT AREA -------------------->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
	<!-------------------- FIN: SUBMIT AREA -------------------->
</body>
</html>