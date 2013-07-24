<!DOCTYPE html>
<html>
<head>
<!-- definirGuardiaTurno.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.DefinirGuardiasTurnosForm"%>
<%@ page import="org.redabogacia.sigaservices.app.autogen.model.ScsTiposguardias"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
	
	
	Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
	String modo = (String)ses.getAttribute("modo");
	ses.removeAttribute("modo");
	Vector campos = (Vector)request.getSession().getAttribute("campos");
	Vector ocultos = (Vector)request.getSession().getAttribute("ocultos");
	ses.removeAttribute("resultado"); // se borra esta variable que puede haberse creado a lo largo de la navegacion
	String[] dato1 = {usr.getLocation()};
	String[] dato2 = {usr.getLocation(),(String)turno.get("IDTURNO"),"_","1"};	
	String[] datoGuardia = {usr.getLocation(), (String)turno.get("IDTURNO")};
	
	DefinirGuardiasTurnosForm miform = (DefinirGuardiasTurnosForm) request.getAttribute("DefinirGuardiasTurnosForm");
	List listaTiposGuardias = (List) miform.getTiposGuardias();
	if (listaTiposGuardias==null) listaTiposGuardias = new ArrayList();
%>	





<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<html:javascript formName="DefinirGuardiasTurnosForm" staticJavascript="false" />

	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	 	
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
 	
 	<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
 	<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
 	
<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="Alta Guardias" localizacion="SJCS > Turnos > Guardias > Alta Guardias"/>

	<script>
		function postAccionTurno(){
			accionComboGuardiaPrincipal();
		}
	</script>
</head>

<body onload="activarSustitucion(document.DefinirGuardiasTurnosForm.checkGuardiaDeSustitucion); modificarVarios();">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr><td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.listarGuardias.literal.guardia"/>
		</td></tr>
	</table>
	
	<div id="camposRegistro" class="posicionModalGrande" align="center">
	
		<!-- Información del Turno que tenemos seleccionado-->
		<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno">
			<table border="0" align="center" class="tablaCampos">
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>: </td>
					<td class="labelText" style="text-align:left">
						<input name="abreviatura" type="text" class="boxConsulta" size="30" maxlength="30" value="<%=turno.get("ABREVIATURA")%>" readonly="true">
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>: </td>
					<td class="labelText" style="text-align:left"  colspan="3">
						<input name="nombre" type="text" class="boxConsulta" size="30" maxlength="100" value="<%=turno.get("NOMBRE")%>" readonly="true" >
					</td>
				</tr>
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>: </td>
					<td class="labelText" style="text-align:left">
						<input name="area" type="text" class="boxConsulta" size="30" value="<%=turno.get("AREA")%>" readonly="true">
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>: </td>
					<td class="labelText" style="text-align:left" colspan="3">
						<input name="materia" type="text" class="boxConsulta" size="30" value="<%=turno.get("MATERIA")%>" readonly="true">
					</td>
				</tr>
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/>: </td>
					<td class="labelText" style="text-align:left">
						<input name="zona" type="text" class="boxConsulta" size="30" value="<%=turno.get("ZONA")%>" readonly="true">
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/>: </td>
					<td class="labelText" style="text-align:left">
						<input name="subzona" type="text" class="boxConsulta" size="30" value="<%=turno.get("SUBZONA")%>" readonly="true">
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/>: </td>
					<td  class="labelText" style="text-align:left">
						<input name="partidoJudicial" type="text" class="boxConsulta" size="30" value="<%=turno.get("PARTIDOJUDICIAL")%>" readonly="true">
					</td>
				</tr>	
			</table>
		</siga:ConjCampos>
	
		<!-- Comienzo del formulario con los campos -->
		<html:javascript formName="DefinirGuardiasTurnosForm" staticJavascript="false" />
		<html:form action="DefinirGuardiasTurnosAction.do" method="POST" target="mainPestanas">
			<html:hidden name="DefinirGuardiasTurnosForm" property="modo"  value='0'></html:hidden>
			<html:hidden name="DefinirGuardiasTurnosForm" property="letradosSustitutos"  value='0'></html:hidden>
			<html:hidden name="DefinirGuardiasTurnosForm" property="diasPagados"  value='1'></html:hidden>		
		
			<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.guardia">
				<table align="center" border="0" width="100%">
					<tr>
						<td width="10%"></td>
						<td width="10%"></td>
						<td width="10%"></td>
						<td width="10%"></td>
						<td width="10%"></td>
						<td width="10%"></td>
					</tr>
				
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.turno.guardia.literal.deSustitucion"/>
						</td>
						<td class="labelText">
							<html:checkbox name="DefinirGuardiasTurnosForm" property="checkGuardiaDeSustitucion"  styleId="checkGuardiaDeSustitucion" onclick="activarSustitucion(this);" value="true" />
							<siga:ComboBD nombre="guardiaDeSustitucion" tipo="guardias" clase="boxCombo" parametro="<%=datoGuardia%>" obligatorioSinTextoSeleccionar="si" ancho="" accion="setTexto();"/>
						</td>
																	
						<td class="labelText">
							<siga:Idioma key="gratuita.guardiasTurno.literal.turnoPrincipal"/>
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
							<html:select styleClass="boxCombo" style="width:180px;" name="DefinirGuardiasTurnosForm" styleId="guardiasPrincipales"  property="idGuardiaPrincipal"  onchange="accionComboGuardiaPrincipal();" > 					
								<bean:define id="guardiasPrincipales" name="DefinirGuardiasTurnosForm" property="guardiasPrincipales" type="java.util.Collection" />
								<html:optionsCollection name="guardiasPrincipales" value="idGuardia" label="nombre" />
							</html:select>
						</td>					
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.turno.guardia.literal.tipoGuardia" />
						</td>
						<td>
							<html:select styleId="tiposGuardias" styleClass="boxCombo" style="width:150px;" property="idTipoGuardiaSeleccionado" > 		
								<html:option value="">&nbsp;</html:option>	
								<%for (int i=0; i<listaTiposGuardias.size(); i++) {
									ScsTiposguardias objTipoGuardia = (ScsTiposguardias) listaTiposGuardias.get(i);
								%>
									<html:option value="<%=UtilidadesString.mostrarDatoJSP(objTipoGuardia.getIdtipoguardia())%>"> <%=UtilidadesMultidioma.getDatoMaestroIdioma(objTipoGuardia.getDescripcion(), usr)%></html:option> 
								<%}%>					
							</html:select>
						</td> 
					</tr>
					
					<tr>
						<td class="labelText" style="width:100px;">
							<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>&nbsp;(*)
						</td>
						<td>
							<html:text name="DefinirGuardiasTurnosForm" styleId="guardia" property="guardia" size="40" maxlength="30" styleClass="box" value="" />
						</td>
						<td class="labelText">
							<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>&nbsp;(*)
						</td>
						<td colspan="3">
							<html:text name="DefinirGuardiasTurnosForm" styleId="descripcion" property="descripcion" size="40" maxlength="1024" styleClass="box" value="" />
						</td>
						
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="gratuita.maestroGuardias.literal.descripcionFacturacion"/></td>
						<td>
							<textarea id="descripcionFacturacion" name="descripcionFacturacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)"  rows="3" cols="80" class="box" ></textarea>
						</td>
						<td class="labelText"><siga:Idioma key="gratuita.maestroGuardias.literal.descripcionPago"/></td>
						<td colspan="3">
							<textarea id="descripcionPago" name="descripcionPago" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)"  rows="3" cols="80" class="box"></textarea>
						</td>
						
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.listarGuardiasTurno.literal.letradosGuardia"/>&nbsp;(*)
							</td>
						<td>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosGuardia" size="7" maxlength="6" styleClass="box" value='' readonly="false"></html:text>
						</td>
						<td class="labelText" colspan="2">
							<siga:Idioma key="gratuita.maestroGuardias.literal.diasSeparacion"/>&nbsp;(*)
							</td>
						<td >
							<html:text name="DefinirGuardiasTurnosForm" property="diasSeparacion" size="3" maxlength="2" styleClass="box" value='' readonly="false"></html:text>
						</td>
						
					</tr>
		
					<tr>
						<td class="labelText"><siga:Idioma key="gratuita.listarGuardias.literal.duracion"/>&nbsp;(*)</td>
						<td>
							<html:text name="DefinirGuardiasTurnosForm" styleId="duracion" property="duracion" size="5" maxlength="4" styleClass="box" value='' readonly="false"></html:text>
							&nbsp;
							<html:select name="DefinirGuardiasTurnosForm" styleId="tipoDiasGuardia" property="tipoDiasGuardia" size="1" value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES%>" styleClass="boxCombo">
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES%>" key="gratuita.combo.literal.diasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_SEMANAS_NATURALES%>" key="gratuita.combo.literal.semanasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_QUINCENAS_NATURALES%>" key="gratuita.combo.literal.quincenasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_MESES_NATURALES%>" key="gratuita.combo.literal.mesesNaturales"/>
							</html:select>
						</td>
						<td class="labelText">
							<html:checkbox name="DefinirGuardiasTurnosForm" styleId="checkDiasPeriodo" property="checkDiasPeriodo" onclick="modificarDiasPeriodo()" />
							&nbsp;
							<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo" />
						</td>
						<td colspan="3">
							<html:text name="DefinirGuardiasTurnosForm" styleId="diasPeriodo" property="diasPeriodo" size="5" maxlength="4" styleClass="box" readonly="false"></html:text>
							&nbsp;
							<html:select name="DefinirGuardiasTurnosForm" styleId="tipoDiasPeriodo" property="tipoDiasPeriodo" size="1" styleClass="boxCombo">
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_DIAS_NATURALES%>" key="gratuita.combo.literal.diasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_SEMANAS_NATURALES%>" key="gratuita.combo.literal.semanasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_QUINCENAS_NATURALES%>" key="gratuita.combo.literal.quincenasNaturales"/>
								<html:option value="<%=ClsConstants.TIPO_PERIODO_DIAS_GUARDIA_MESES_NATURALES%>" key="gratuita.combo.literal.mesesNaturales"/>
							</html:select>
						</td>		
					</tr>
			
					<tr>
						<td colspan="5">	
					
							<!-- Inicio: tabla para los dias de la semana -->
							<table BORDER="0">
								<tr>
									<td class="labelText" rowspan="2" style="width:96px;padding-left:7px;">
										<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.tipodias"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="gratuita.combo.literal.laborables"/>
									</td>
									<td>
										<html:button property="boton" onclick="marcarTodosLaborables()" styleClass="button">
											<siga:Idioma key="general.boton.marcarTodos"/>
										</html:button>
									</td>
									<td class="labelText" style="width:255px;">	
										<siga:Idioma key="gratuita.checkbox.literal.Lunes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesLunes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Martes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesMartes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Miercoles"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesMiercoles" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Jueves"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesJueves" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Viernes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesViernes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Sabado"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionLaborablesSabado" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
									</td>
									<td>
										<html:button property="boton" onclick="desmarcarTodosLaborables()" styleClass="button">
											<siga:Idioma key="general.boton.desmarcarTodos"/>
										</html:button>
									</td>
								</tr>
						
								<tr>
									<td class="labelText">
										<siga:Idioma key="gratuita.combo.literal.festivos"/>
									</td>
									<td>
										<html:button property="boton" onclick="marcarTodosFestivos()" styleClass="button">
											<siga:Idioma key="general.boton.marcarTodos"/>
										</html:button>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="gratuita.checkbox.literal.Lunes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosLunes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Martes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosMartes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Miercoles"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosMiercoles" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Jueves"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosJueves" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Viernes"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosViernes" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Sabado"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosSabado" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
										<siga:Idioma key="gratuita.checkbox.literal.Domingo"/>
											<html:checkbox name="DefinirGuardiasTurnosForm" property="seleccionFestivosDomingo" value="<%=ClsConstants.DB_TRUE%>" onclick="modificarSeleccionDias()" />
									</td>
									<td>
										<html:button property="boton" onclick="desmarcarTodosFestivos()" styleClass="button">
											<siga:Idioma key="general.boton.desmarcarTodos"/>
										</html:button>
									</td>
								</tr>
							</table>
							<!-- Fin: tabla para los dias de la semana -->			
						</td>
						<td class="labelText">
							<div id="labelSeleccionDias"><siga:Idioma key="gratuita.guardiasTurno.literal.seleccionBase"/>:</div>
						</td>
					</tr>		
				</table>
			</siga:ConjCampos>
		
			<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
				<table border="0" width="100%" align="center">
					<tr>
						<td width="8%"></td>
						<td width="3%"></td>
						<td width="3%"></td>
						<td width="8%"></td>
						<td width="3%"></td>
						<td width="10%"></td>
					</tr>
					
					<tr>
						<td class="labelText" style="text-align:left" ><siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio"/></td>
						<td class="labelText" style="text-align:left" >
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="crit_1">
								<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
								<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
								<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
								<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
								<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
							</html:select>
						</td>
						<td>
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="ord_1">
								<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
								<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
							</html:select>
						</td>
						<td class="labelText" style="text-align:left" ><siga:Idioma key="gratuita.maestroTurnos.literal.segundoCriterio"/></td>
						<td class="labelText" style="text-align:left" >
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="crit_2">
								<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
								<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
								<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
								<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
								<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
							</html:select>
							</td>
						<td>
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="ord_2">
								<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
								<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
							</html:select>
						</td>
					</tr>
					
					<tr>
						<td class="labelText" style="text-align:left" ><siga:Idioma key="gratuita.maestroTurnos.literal.terceroCriterio"/></td>
						<td class="labelText" style="text-align:left" >
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="crit_3">
								<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
								<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
								<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
								<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
								<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
							</html:select>
							</td>
						<td>
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="ord_3">
								<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
								<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
							</html:select>
						</td>
						<td class="labelText" style="text-align:left" ><siga:Idioma key="gratuita.maestroTurnos.literal.cuartoCriterio"/></td>
						<td class="labelText" style="text-align:left"  >
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="crit_4">
								<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
								<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
								<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
								<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
								<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
							</html:select>
							</td>
						<td >
							<html:select styleClass="boxCombo" name="DefinirGuardiasTurnosForm" property="ord_4">
								<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
								<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
							</html:select>
						</td>
					</tr>
				</table>	
			</siga:ConjCampos>

			<ajax:select
				baseUrl="/SIGA/DefinirGuardiasTurnosAction.do?modo=getAjaxGuardias"
				source="turnosPrincipales" target="guardiasPrincipales" parameters="idTurnoPrincipal={idTurnoPrincipal}"
				postFunction="postAccionTurno"
			/>
		</html:form>
	
		<siga:ConjBotonesAccion botones="G,R,C" modal="G" />
	</div>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">
		
		//Modifica los checks desde el onload
		function modificarVarios () {
			modificarDiasPeriodo ();
			modificarSeleccionDias ();
		} //modificarVarios ()
		
		//Habilita o no el input y combo de dias de periodo
		function modificarDiasPeriodo () {			
			if (document.DefinirGuardiasTurnosForm.checkDiasPeriodo.checked) {
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
		//(PRESENTE TAMBIEN EN editarGuardiaTurno.jsp)
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
		
		// Asociada al boton Guardar
		function accionGuardar() {			
			sub();
			if(document.getElementById("idTurnoPrincipal").value !="-1" && document.getElementById("idTurnoPrincipal").value !=""){
				error = '';
				if(document.getElementById("idGuardiaPrincipal").value =="-1" || document.getElementById("idGuardiaPrincipal").value ==""){
					error += "<siga:Idioma key='errors.required' arg0='gratuita.guardiasTurno.literal.guardiaPrincipal'/>"+ '\n';				
				}
				
				if(document.getElementById("guardia").value ==""){
					error += "<siga:Idioma key='errors.required' arg0='censo.SolicitudIncorporacion.literal.nombre'/>"+ '\n';
				}
				
				if(document.getElementById("descripcion").value ==""){
					error += "<siga:Idioma key='errors.required' arg0='gratuita.maestroTurnos.literal.descripcion'/>"+ '\n';
						
				}
				
				if(error ==''){
					document.DefinirGuardiasTurnosForm.modo.value = "insertar";
			       	document.DefinirGuardiasTurnosForm.target = "submitArea";
					document.DefinirGuardiasTurnosForm.submit();
					
				}else{
					fin();
					alert(error);
				 	return false;				
				}
				
			}else{
							
				if (document.DefinirGuardiasTurnosForm.checkGuardiaDeSustitucion.checked || validateDefinirGuardiasTurnosForm(document.DefinirGuardiasTurnosForm)){
					if (!document.DefinirGuardiasTurnosForm.checkGuardiaDeSustitucion.checked && document.DefinirGuardiasTurnosForm.duracion.value==0) {
						alert('<siga:Idioma key="gratuita.maestroTurnos.literal.cero"/>');
						fin();
				 		return false;

					} else { 					
			        	document.DefinirGuardiasTurnosForm.modo.value = "insertar";
			        	document.DefinirGuardiasTurnosForm.target = "submitArea";
						document.DefinirGuardiasTurnosForm.submit();
					}
					
				} else {
					fin();
				 	return false;
				
				}
			}
		}

		// Refresco
		function refrescarLocal() {
			parent.buscar()
		}

		// Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}

		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		function accionComboGuardiaPrincipal() {
			var deshabilitar = document.getElementById("idGuardiaPrincipal").value==''||document.getElementById("idGuardiaPrincipal").value=='-1';
			for (i = 0; i < document.DefinirGuardiasTurnosForm.all.length; i++) {
				if(document.DefinirGuardiasTurnosForm.all[i].name && document.DefinirGuardiasTurnosForm.all[i].type != "hidden") {
					document.DefinirGuardiasTurnosForm.all[i].disabled = !deshabilitar;
				}
			}
			jQuery("#turnosPrincipales").removeAttr("disabled");
			jQuery("#guardiasPrincipales").removeAttr("disabled");
			jQuery("#guardia").removeAttr("disabled");
			jQuery("#descripcion").removeAttr("disabled");
			jQuery("#descripcionFacturacion").removeAttr("disabled");
			jQuery("#descripcionPago").removeAttr("disabled");
		}
		
		function activarSustitucion (o){	
			document.getElementById("idTurnoPrincipal").value ="-1";
			document.getElementById("idTurnoPrincipal").onchange();
			for (i = 0; i < document.DefinirGuardiasTurnosForm.all.length; i++) {
				if(document.DefinirGuardiasTurnosForm.all[i].name && document.DefinirGuardiasTurnosForm.all[i].type != "hidden") {
					document.DefinirGuardiasTurnosForm.all[i].disabled = o.checked;
				}
			}
			jQuery("#checkGuardiaDeSustitucion").removeAttr("disabled");
			if(!o.checked)
				jQuery("#checkGuardiaDeSustitucion").attr("disabled","disabled");
			else
				jQuery("#checkGuardiaDeSustitucion").removeAttr("disabled");
			jQuery("#guardia").removeAttr("disabled");
			jQuery("#descripcion").removeAttr("disabled");

			if (o.checked) {
				setTexto();
			}
			else {
				document.DefinirGuardiasTurnosForm.guardia.value = "";
				document.DefinirGuardiasTurnosForm.descripcion.value = "";
			}
		}

		function setTexto () {
			i = document.DefinirGuardiasTurnosForm.guardiaDeSustitucion.selectedIndex;
			t = document.DefinirGuardiasTurnosForm.guardiaDeSustitucion.options[i].text;
			aux = " (<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.turno.guardia.mensaje.nombreSustitucion")%>)";
			l = aux.length;
			document.DefinirGuardiasTurnosForm.guardia.value = t.substring(0,30-l) + aux;
			document.DefinirGuardiasTurnosForm.descripcion.value = "<%=UtilidadesString.getMensajeIdioma(usr, "gratuita.turno.guardia.mensaje.descripcion")%>'" + t + "'";
		}	
	</script>		
  </body>		
</html>