<!-- busquedaEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>



<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>


<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGColegioBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="java.util.*"%>



<!-- JSP -->

<%
	String app = request.getContextPath();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean esComision = usr.isComision();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String anio = "", numero = "", numEJG = "", fechaApertura = "", fechaAperturaHasta = "", fechaEstadoDesde = "", fechaEstadoHasta = "", fechaDictamenDesde = "", fechaDictamenHasta = "", fechaLimiteDesde = "", fechaLimiteHasta = "", estado = "", busquedaRealizada = "", nif = "", nombre = "", apellido1 = "", apellido2 = "", idPersona = "", creadoDesde = "";
	String idPersonaDefensa = "";
	// fechaApertura=UtilidadesBDAdm.getFechaBD("");
	String nColegiado = request.getAttribute("nColegiado") == null ? "" : (String) request.getAttribute("nColegiado");
	String nombreColegiado = request.getAttribute("nombreColegiado") == null ? "" : (String) request.getAttribute("nombreColegiado");

	Hashtable miHash = (Hashtable) ses.getAttribute("DATOSFORMULARIO");
	ses.removeAttribute("DATOSFORMULARIO");
	anio = UtilidadesBDAdm.getYearBD("");

	String anio2 = "";

	String calidad = "", procedimiento = "", asunto = "";
	ArrayList juzgado = new ArrayList();
	ArrayList idTurno = new ArrayList(), idGuardia = new ArrayList(), idTipoEJG = new ArrayList(), idTipoEJGColegio = new ArrayList(), idEstado = new ArrayList();
	String cajgAnio="", cajgNumero ="";
	String idRenuncia="";
	ArrayList renunciaSel = new ArrayList();
	String ventanaCajg = request.getParameter("ventanaCajg");
	String calidadidinstitucion="";
	String idcalidad="";
	ArrayList calidadSel = new ArrayList();
	String idInstitucion = usr.getLocation();
	try {

		busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();

		if (busquedaRealizada != null) {
			if (miHash.get(ScsEJGBean.C_ANIO) != null) {
				anio2 = miHash.get(ScsEJGBean.C_ANIO).toString();
				anio = anio2;
			}
			if (miHash.get(ScsEJGBean.C_NUMEJG) != null)
				numEJG = miHash.get(ScsEJGBean.C_NUMEJG).toString();
			if (miHash.get(ScsEJGBean.C_NUMERO) != null)
				numero = miHash.get(ScsEJGBean.C_NUMERO).toString();
			if (miHash.get(ScsEJGBean.C_FECHAAPERTURA) != null) {
				fechaApertura = miHash.get(ScsEJGBean.C_FECHAAPERTURA).toString();
			}
			if (miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG) != null)
				estado = miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString();
			if (miHash.get(ScsEJGBean.C_IDPERSONA) != null)
				idPersona = miHash.get(ScsEJGBean.C_IDPERSONA).toString();
			if (miHash.get(ScsPersonaJGBean.C_NIF) != null)
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();
			if (miHash.get(ScsPersonaJGBean.C_NOMBRE) != null)
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO1) != null)
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO2) != null)
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
			if (miHash.get("CREADODESDE") != null)
				creadoDesde = miHash.get("CREADODESDE").toString();
			if (miHash.get("CALIDAD") != null)
				calidad = miHash.get("CALIDAD").toString();
			if (miHash.get("JUZGADO") != null) {
				juzgado.add(miHash.get("JUZGADO").toString());
			}
			if (miHash.get("PROCEDIMIENTO") != null)
				procedimiento = miHash.get("PROCEDIMIENTO").toString();
			if (miHash.get("ASUNTO") != null)
				asunto = miHash.get("ASUNTO").toString();

			if (miHash.get(ScsTurnoBean.C_IDTURNO) != null) {
				idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());
			}
			if (miHash.get(ScsEJGBean.C_ANIO_CAJG) != null) {
				cajgAnio=miHash.get(ScsEJGBean.C_ANIO_CAJG).toString();
			}
			if (miHash.get(ScsEJGBean.C_NUMERO_CAJG) != null) {
				cajgNumero=miHash.get(ScsEJGBean.C_NUMERO_CAJG).toString();
			}
			if (miHash.get(ScsEJGBean.C_IDRENUNCIA)!= null){
				idRenuncia=miHash.get(ScsEJGBean.C_IDRENUNCIA).toString();
				if (idRenuncia!=null)
					renunciaSel.add(0,idRenuncia);	
			}
			
			if (miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD)!=null&&miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD).equals("")){
				if (miHash.get(ScsEJGBean.C_CALIDADIDINSTITUCION)!=null){
					calidadidinstitucion	=  miHash.get(ScsEJGBean.C_CALIDADIDINSTITUCION).toString();
					idcalidad = miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD).toString();
					calidadSel.add(0,idcalidad+","+calidadidinstitucion);
				}
					
			}
			else{ if (!calidad.equals("")&& calidad!=null){
					calidadSel.add(0,calidad+","+idInstitucion);
				}
				

			} 
			

			
		}
	} catch (Exception e) {
	}
	;

	//String idTurnos = new String("");

	try {
		idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());

		idGuardia.add(miHash.get(ScsGuardiasTurnoBean.C_IDGUARDIA).toString());
		idTipoEJG.add(miHash.get(ScsTipoEJGBean.C_IDTIPOEJG).toString());
		idTipoEJGColegio.add(miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO).toString());
		idEstado.add(miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString());
	} catch (Exception e) {
	}
	;

	java.util.ResourceBundle rp = java.util.ResourceBundle.getBundle("SIGA");
	String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[] = { idordinario, idordinario };

	String[] datos = { usr.getLocation() };
	ArrayList juzgadoSel = new ArrayList();
	String dato[] = { (String) usr.getLocation() };
	String idioma[] = { (String) usr.getLanguage() };
	String accion="";
	String formulario="";
	
	boolean permisoEejg = false;
	if(request.getAttribute("permisoEejg")!=null){
		permisoEejg = Boolean.parseBoolean(request.getAttribute("permisoEejg").toString());
	}
	String idremesa = "";
	if(request.getAttribute("idremesa")!=null)
		idremesa =(String) request.getAttribute("idremesa");
	
	if(esComision && idEstado.isEmpty()){
		idEstado.add("9");
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<script type="text/javascript">	
		function refrescarLocal(){
			
			buscar();
		}		
		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
			if (resultado != null && resultado[2]!=null)
			{
				document.forms[1].idPersona.value=resultado[2];
			}
		}
		function inicio(){
		<% 
		
		 if (busquedaRealizada.equals("1")) {%>
		      buscarPaginador();
		<%}%>
		}
		function seleccionarTodos(pagina) 
		{
				document.forms[1].seleccionarTodos.value = pagina;
				buscar('buscarPor');
				
		}
		
		
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<%if(ventanaCajg.equalsIgnoreCase("0")){%>
		<%accion="/JGR_EJG.do?noReset=true";%>
		<%formulario="DefinirEJGForm";%>
		<siga:TituloExt 
			titulo="gratuita.busquedaEJG.literal.expedientesEJG"
			localizacion="gratuita.busquedaEJG.localizacion"/>
	<%}else if(ventanaCajg.equalsIgnoreCase("1")){%>	
		<%accion="/JGR_E-Comunicaciones_Seleccion.do?noReset=true";%>
		<%formulario="BusquedaCAJG_EJGForm";%>
		<siga:Titulo 
			titulo="gratuita.busquedaEJG.literal.expedientesEJG"
			localizacion="gratuita.busquedaEJG_CAJG.localizacion"/>
	<%}else if(ventanaCajg.equalsIgnoreCase("2")){%>
		<%accion="/JGR_E-Comunicaciones_Gestion.do?noReset=true";%>
		<%formulario="DefinicionRemesas_CAJG_Form";%>
		<siga:Titulo 
			titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa.ExpedientesListos"
			localizacion="gratuita.BusquedaRemesas.añadir.localizacion"/>
	<%} %>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="inicio();ajusteAlto('resultado');" >
<!--bean:define id="permisoEejg" scope="request" name="permisoEejg" type="java.lang.Boolean"/-->

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	

	<html:form action="<%=accion %>" method="POST" target="resultado">
		<html:hidden name="<%=formulario%>" property="idPersona" value=""></html:hidden> <!-- 0 -->
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
		<html:hidden property = "descripcionEstado" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden property = "numero" value = ""/>
		<html:hidden property = "selDefinitivo" value = ""/>
		<html:hidden property = "idRemesa" value = "<%=idremesa%>"/>
		<input type="hidden" name="volver" value="">
		<html:hidden property="seleccionarTodos" />
	
		

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.EJG">

		<table align="center" width="100%" border="0" >
			<tr>
				<td class="labelText" width="16%">
					<siga:Idioma key="gratuita.busquedaEJG.literal.anyo" />/<siga:Idioma key="gratuita.busquedaEJG.literal.codigo" /></td>
				<td width="15%">
					<html:text name="<%=formulario%>" styleClass="box" property="anio"  value="<%=anio%>" style="width:40" maxlength="4"></html:text>
					&nbsp;/&nbsp;<html:text name="<%=formulario%>" styleClass="box" property="numEJG" value="<%=numEJG%>" size="8" maxlength="10"> </html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG" />
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG" parametro="<%=datoTipoOrdinario%>"clase="boxCombo" obligatorio="false" elementoSel="<%=idTipoEJG%>" ancho="200"/>
				</td>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.EJGColegio" />
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="idTipoEJGColegio" tipo="tipoEJGColegio" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idTipoEJGColegio%>" ancho="200"/>
				</td>
			</tr>
		</table>
		
		<table align="center" width="100%" border="0" >
			<tr>
				<td class="labelText" style="width:15%"><siga:Idioma
					key="gratuita.busquedaSOJ.literal.creadoDesde" />
				</td>
				<td colspan="3" class="labelText">
					<select name="creadoDesde" class="box">
						<option value=""></option>
						<option value="A" <%if (creadoDesde.startsWith("A")) {%> selected <%}%>>ASISTENCIA</option>
						<option value="D" <%if (creadoDesde.startsWith("D")) {%> selected <%}%>>DESIGNA</option>
						<option value="S" <%if (creadoDesde.startsWith("S")) {%> selected <%}%>>SOJ</option>
						<option value="M" <%if (creadoDesde.startsWith("M")) {%> selected <%}%>>MANUAL</option>
					</select>
				</td>
				
		
				<td class="labelText" style="left;width:160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaAperturaDesde" />
				</td>
				<td >
					<siga:Fecha nombreCampo="fechaAperturaDesde" valorInicial="<%=fechaApertura%>" /> 
					<a onClick="return showCalendarGeneral(fechaAperturaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
				<td class="labelText"><siga:Idioma
					key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td >
					<siga:Fecha nombreCampo="fechaAperturaHasta" valorInicial="<%=fechaAperturaHasta%>" /> 
					<a onClick="return showCalendarGeneral(fechaAperturaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.estadoEJG" />
				</td>
				<% if(esComision){%>
					<td colspan="3" class="labelText">
						<siga:ComboBD nombre="estadoEJG" tipo="estadosEJGComision" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idEstado%>" obligatorioSinTextoSeleccionar="true"/>
					</td>
				<% }else{ %>
					<td colspan="3" class="labelText">
						<siga:ComboBD nombre="estadoEJG" tipo="estadosEJG" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idEstado%>" />
					</td>
				<% } %>
				<td class="labelText" style="width:160">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaEstadoDesde" />
				</td>
				<td>
					<siga:Fecha nombreCampo="fechaEstadoDesde" valorInicial="<%=fechaEstadoDesde%>" /> 
					<a onClick="return showCalendarGeneral(fechaEstadoDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td>
					<siga:Fecha nombreCampo="fechaEstadoHasta" valorInicial="<%=fechaEstadoHasta%>" /> 
					<a onClick="return showCalendarGeneral(fechaEstadoHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma
					key="gratuita.busquedaEJG.literal.dictaminado" />
				</td>
				<td class="labelText" style="align:left">
					<select name="dictaminado" class="box">
						<option value="S" ><siga:Idioma key="gratuita.busquedaSOJ.literal.si" /></option>
						<option value="N"><siga:Idioma key="gratuita.busquedaSOJ.literal.no" /></option>
						<option value="I" selected><siga:Idioma key="gratuita.busquedaSOJ.literal.indiferente" /></option>				
					</select>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.dictamen"/>
				</td>
				<td > 
					<siga:ComboBD nombre="idTipoDictamenEJG" tipo="dictamenEJG" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" obligatorio="false" ancho="140" />
				</td>
				<td class="labelText" style="width:160">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaDictamenDesde" /></td>
				<td>
					<siga:Fecha nombreCampo="fechaDictamenDesde" valorInicial="<%=fechaDictamenDesde%>" /> 
					<a onClick="return showCalendarGeneral(fechaDictamenDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9" >
					</a>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td> 
					<siga:Fecha nombreCampo="fechaDictamenHasta" valorInicial="<%=fechaDictamenHasta%>" /> 
					<a onClick="return showCalendarGeneral(fechaDictamenHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
			</tr>

			<tr>
				<td class="labelText" >
					<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> 
					<siga:Idioma key='gratuita.operarEJG.literal.anio'/>/<siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
				</td>
				<td class="labelText" >
					<html:text name="<%=formulario%>" styleClass="box" property="anioCAJG"  style="width:40" maxlength="4" value="<%=cajgAnio%>"></html:text>&nbsp;/&nbsp;
					<html:text name="<%=formulario%>" styleClass="box" property="numeroCAJG" value="<%=cajgNumero%>" size="8" maxlength="10">
					</html:text>
				</td>
				
				<td class="labelText">
					<siga:Idioma key="Resolución"/>
				</td>
				<td > 
					<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="resolucionEJG" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=idioma%>" ancho="140" />
				</td>
				
				
				<td class="labelText" style="text-align: left;width:160"><siga:Idioma
					key="gratuita.busquedaEJG.literal.fechaLimiteDesde" />
				</td>

				<td>
					<siga:Fecha nombreCampo="fechaLimitePresentacionDesde" valorInicial="<%=fechaLimiteDesde%>" /> 
					<a onClick="return showCalendarGeneral(fechaLimitePresentacionDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
				<td class="labelText"><siga:Idioma
					key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td><siga:Fecha nombreCampo="fechaLimitePresentacionHasta" valorInicial="<%=fechaLimiteHasta%>" /> 
					<a onClick="return showCalendarGeneral(fechaLimitePresentacionHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
						<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>" border="0" valign="bottom" anchoTextField="9">
					</a>
				</td>
			</tr>
			</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloTramitador" desplegable="true" oculto="false">
		<table border="0">
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.turno" />
					&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
					<siga:ComboBD nombre="identificador" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:guardiaTurnoIdGuardia" parametro="<%=dato%>" elementoSel="<%=idTurno%>" ancho="355" />
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.guardia" />
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="guardiaTurnoIdGuardia" tipo="guardias" clase="boxCombo" obligatorio="false" hijo="t" elementoSel="<%=idGuardia%>" ancho="355" />
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<siga:BusquedaPersona tipo="colegiado" idPersona="idPersona"></siga:BusquedaPersona>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.defensa"  desplegable="true" oculto="true">
		<table border="0" align="center" width="100%">
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.personaJG.literal.calidad" />
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="calidad" tipo="ComboCalidades" ancho="130" clase="boxCombo" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=calidadSel%>" hijo="t" readonly="false"/>
				</td>
				<td>&nbsp</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.operarEJG.literal.renuncia" />
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="idRenuncia" tipo="comboRenuncia" ancho="130" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=renunciaSel%>"  readonly="false"/>
					&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
					&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado" />
				</td>
				<td class="labelText" colspan="2">
					<input type="text" name="codigoExtJuzgado" class="box" size="7" style="margin-top: 3px;" maxlength="10" onBlur="obtenerJuzgado();" />
				</td>
				<td class="labelText" colspan="2">
					<siga:ComboBD nombre="juzgado" tipo="comboJuzgados" ancho="500" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" hijo="t" elementoSel="<%=juzgado%>" parametro="<%=datos%>" />
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="informes.cartaAsistencia.procedimiento" />
				</td>
				<td class="labelText">
					<html:text name="<%=formulario%>" property="procedimiento" size="14" maxlength="100" styleClass="box" value="<%=procedimiento%>"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="informes.cartaAsistencia.asunto" />
				</td>
				<td class="labelText" colspan="2">
					<html:text name="<%=formulario%>" property="asunto" size="100" maxlength="100" styleClass="box" value="<%=asunto%>"></html:text>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.solicitante" desplegable="true" oculto="true">
		<table align="center" width="100%">
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.nif" />
				</td>
				<td class="labelText">
					<html:text name="<%=formulario%>" property="nif" size="10" maxlength="20" styleClass="box" value="<%=nif%>"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.nombre" /> <html:text name="<%=formulario%>" property="nombre" size="26" maxlength="100" styleClass="box" value="<%=nombre%>"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1" /> <html:text name="<%=formulario%>" property="apellido1" size="26" maxlength="100" styleClass="box" value="<%=apellido1%>"></html:text>
				</td>
				<td class="labelText"><siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2" /> <html:text name="<%=formulario%>" property="apellido2" size="26" maxlength="100" styleClass="box" value="<%=apellido2%>"></html:text>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>

</html:form>
	
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea21">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	<html:form action="/JGR_UnidadFamiliarEEJG"  method="post" target="submitArea21">
		<html:hidden property="modo"/>
		<html:hidden property="datosInforme"/>
		
	
</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<%if(ventanaCajg.equalsIgnoreCase("0")){%>
	<%if(!esComision){%>
		<%if(permisoEejg){ %>
			<siga:ConjBotonesBusqueda botones="C,N,B,DEE"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
		<%}else{ %>
			<siga:ConjBotonesBusqueda botones="C,N,B"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
		<%} %>
	<%}else{ %>
		<%if(permisoEejg){ %>
			<siga:ConjBotonesBusqueda botones="C,B,DEE"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
		<%}else{ %>
			<siga:ConjBotonesBusqueda botones="C,B"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
		<%} %>
	<%} %>
	<%}else if(ventanaCajg.equalsIgnoreCase("1")){%> <!-- Antiguo busquedaEJG_Cajg -->
		<siga:ConjBotonesBusqueda botones="le,B"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
	<%}else if(ventanaCajg.equalsIgnoreCase("2")){%> <!-- Antiguo busquedaEJG_Listos -->
		<siga:ConjBotonesBusqueda botones="B,ar"  titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa.ExpedientesListos" />
	<%}%>
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function ocultarConjunto(b) {
			if (b == 1){
				
				document.getElementById("fieldset1").style.display='none';
				//document.getElementById("anio 1").value='(-)Expediente Justicia Gratuita';
				document.getElementById("fieldset2").style.display='block';
				//document.getElementById("anio 3").value='(+)Expediente Justicia Gratuita';
			}
			else {
				
				document.getElementById("fieldset1").style.display='block';
				//document.getElementById("anio 1").value='(-)Expediente Justicia Gratuita';
				document.getElementById("fieldset2").style.display='none';
				//document.getElementById("anio 3").value='(+)Expediente Justicia Gratuita';
			
			}
		}
	
		// Funcion que obtiene el juzgado buscando por codigo externo	
		 function obtenerJuzgado(){ 
			  if (document.forms[1].codigoExtJuzgado.value!=""){
  				   document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[1].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.submit();		
			 }
		}
	
		//<!-- Funcion asociada a boton buscar -->
		function buscarPaginador(){		
			
				document.forms[1].modo.value = "buscarPor";				
				<%if(ventanaCajg.equalsIgnoreCase("2")){%>
					document.forms[1].modo.value = "buscarListos";
					document.forms[1].idRemesa.value=<%=idremesa%>;
					//document.forms[1].submit();
				<%}%>
				
				/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
				con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
				el combo hijo de guardias
				*/
				var id = document.forms[1].identificador.value;
				<%if (!esComision){%>
					document.forms[1].descripcionEstado.value = document.forms[1].estadoEJG[document.forms[1].estadoEJG.selectedIndex].text;
				<% } %>
	
	
	
				var posicion = 0;
				/* Se recorre hasta encontrar el separador, que es ","*/									
				posicion = id.indexOf(',') + 1;
				/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
				document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
				if (isNaN(document.forms[1].anio.value)) {
					alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
				}else if (isNaN(document.forms[1].idPersona.value)) {
					alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
				}else document.forms[1].submit();
			
		}		
		
		function buscar(){ 
				
				if ( !validarObjetoAnio(document.getElementById("anio")) ){
					alert("<siga:Idioma key='fecha.error.anio'/>");
					return false;
				}
				if ( !validarObjetoAnio(document.getElementById("anioCAJG")) ){
					alert("<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='fecha.error.anio'/>");
					return false;
				}
	
				if((validarFecha(document.forms[1].fechaAperturaHasta.value))&&
				   (validarFecha(document.forms[1].fechaAperturaDesde.value))&&
				   (validarFecha(document.forms[1].fechaEstadoHasta.value))&&
				   (validarFecha(document.forms[1].fechaEstadoDesde.value))&&
				   (validarFecha(document.forms[1].fechaDictamenHasta.value))&&
				   (validarFecha(document.forms[1].fechaDictamenDesde.value))&&
				   (validarFecha(document.forms[1].fechaLimitePresentacionHasta.value))&&
				   (validarFecha(document.forms[1].fechaLimitePresentacionDesde.value))){
				
					sub();
					document.forms[1].modo.value = "buscarInit";
					
					<%if(ventanaCajg.equalsIgnoreCase("2")){%>
						document.forms[1].modo.value = "buscarListosInicio";
						document.forms[1].idRemesa.value=<%=idremesa%>;
						//document.forms[1].submit();				
					<%}%>
					
					/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
					con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
					el combo hijo de guardias
					*/
					var id = document.forms[1].identificador.value;
					<%if (!esComision){%>
						document.forms[1].descripcionEstado.value = document.forms[1].estadoEJG[document.forms[1].estadoEJG.selectedIndex].text;
					<%}%>
		
					var posicion = 0;
					/* Se recorre hasta encontrar el separador, que es ","*/									
					posicion = id.indexOf(',') + 1;
					/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
					document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
					if (isNaN(document.forms[1].anio.value)) {
						fin();
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
					}
					/*else if (isNaN(document.forms[1].numEJG.value)) {
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
					}  Podemos hacer la busqueda por este campo con comodines*/
					else if (isNaN(document.forms[1].idPersona.value)) {
						fin();
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
					}
					else document.forms[1].submit();
				}else{
					setFocusFormularios();
				}
			
		}		
		
		//<!-- Funcion asociada a boton limpiar -->
		function limpiar(){		
			document.forms[1].reset();
		}
		
		function traspasoDatos(resultado){
		 seleccionComboSiga("juzgado",resultado[0]);
		}	
		
		//<!-- Funcion asociada a boton Nuevo -->
		function nuevo(){
			document.forms[1].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
			//if(resultado[0]=='MODIFICADO') buscar();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinirEJGForm){
					numero.value        = resultado[1];
					idTipoEJG.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}
		function generarCarta() {
   			if(window.frames.resultado.ObjArray){
 				window.frames.resultado.accionGenerarCarta();
			}

		} 	
		function descargaEejg(){
			<%if(ventanaCajg.equalsIgnoreCase("0")){%>
				if(window.frames.resultado.ObjArray){
					sub();
			   	 	datos =  window.frames.resultado.getDatosSeleccionados();
			    	if (datos.length==0){
			    		alert("<siga:Idioma key='general.message.seleccionar'/>");
				    	fin();
				    	return false;
					}
				
				
					document.DefinirUnidadFamiliarEJGForm.datosInforme.value = datos;
		   			document.DefinirUnidadFamiliarEJGForm.modo.value = "descargaMultiplesEejg";
					document.DefinirUnidadFamiliarEJGForm.submit();
				
 					// window.frames.resultado.accionDescargaEejg();
				}
			<%}%>
		}
		function aniadirARemesa(){
			<%if(ventanaCajg.equalsIgnoreCase("2")){%>
				if(document.frames.resultado.document.<%=formulario%>) {
					var datos1 = document.frames.resultado.document.<%=formulario%>.selDefinitivo;
					if(datos1.value) {
				    	document.forms[1].selDefinitivo.value=datos1.value;
				    	document.forms[1].idRemesa.value=<%=idremesa%>;
				    	document.forms[1].target="mainWorkArea";
						document.forms[1].modo.value = "aniadirARemesa";
						document.forms[1].submit();
					}
				}
			<%}%>
		}

		function accionVolver()
		{
			<%if(ventanaCajg.equalsIgnoreCase("2")){%>
					document.forms[1].action="./JGR_E-Comunicaciones_Gestion.do";	
					document.forms[1].modo.value="editar";
					document.forms[1].volver.value="SI";
					document.forms[1].idRemesa.value=<%=idremesa%>;
					document.forms[1].target="mainWorkArea"; 
					document.forms[1].submit(); 
			
					<%}%>
		}
		
		//<!-- Accion de la busqueda CAJG -->
		function accionListoParaEnviar() 
		{
			try{
			    var dat = "";
			    var datos1 = document.frames.resultado.document.<%=formulario%>.selDefinitivo;
			    document.forms[1].selDefinitivo.value=datos1.value;
			    
			  	document.forms[1].modo.value = "listosParaComision";
			  	var f = document.forms[1].name;	
			  	//Para quien se encargue del marrón de la ruedecita, con mucho cariño para ....
			  	//(JTA)Saludos tambien de mi parte. si quieres ponerlo aqui mira en accionGenerarCarta() de listadoEJG.jspi
							//document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=bot que pasa';
	
			    document.forms[1].submit();
			}
			catch (e){
			alert('<siga:Idioma key="messages.cajg.error.listos"/>');
			}
		}

		

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

	<%if(ventanaCajg.equalsIgnoreCase("2")){%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<%} %>
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea21" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>