<!-- busquedaEJG_CAJG.jsp -->
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
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTipoEJGColegioBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->

<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String anio="", numero="",numEJG="", fechaApertura="", estado="", busquedaRealizada="", nif="", nombre="", apellido1="", apellido2="", idPersona="", creadoDesde="";
	String idPersonaDefensa="";

	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String nombreColegiado =  request.getAttribute("nombreColegiado")==null?"":(String)request.getAttribute("nombreColegiado");

	Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
	ses.removeAttribute("DATOSFORMULARIO");
	anio = UtilidadesBDAdm.getYearBD("");

	String anio2="";
	
	String calidad="", procedimiento="", asunto="";
	ArrayList juzgado=new ArrayList();
  ArrayList idTurno = new ArrayList(), idGuardia = new ArrayList(), idTipoEJG = new ArrayList(), idTipoEJGColegio = new ArrayList(), idEstado = new ArrayList();
	try {
		
		busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();
		
		if (busquedaRealizada!=null){
			if (miHash.get(ScsEJGBean.C_ANIO)!=null)
				anio2=miHash.get(ScsEJGBean.C_ANIO).toString();
			if (miHash.get(ScsEJGBean.C_NUMEJG)!=null)
				numEJG = miHash.get(ScsEJGBean.C_NUMEJG).toString();
			if (miHash.get(ScsEJGBean.C_NUMERO)!=null)
				numero = miHash.get(ScsEJGBean.C_NUMERO).toString();
			if (miHash.get(ScsEJGBean.C_FECHAAPERTURA)!=null)
				fechaApertura = miHash.get(ScsEJGBean.C_FECHAAPERTURA).toString();
			if (miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG)!=null)
				estado = miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString();
			if (miHash.get(ScsEJGBean.C_IDPERSONA)!=null)
				idPersona = miHash.get(ScsEJGBean.C_IDPERSONA).toString();	
			if (miHash.get(ScsPersonaJGBean.C_NIF)!=null)
				nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();	
			if (miHash.get(ScsPersonaJGBean.C_NOMBRE)!=null)
				nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();	
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO1)!=null)
				apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();	
			if (miHash.get(ScsPersonaJGBean.C_APELLIDO2)!=null)
				apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
			if (miHash.get("CREADODESDE")!=null)
				creadoDesde = miHash.get("CREADODESDE").toString();
			if (miHash.get("CALIDAD")!=null)	
				calidad = miHash.get("CALIDAD").toString();
			if (miHash.get("JUZGADO")!=null){
				juzgado.add(miHash.get("JUZGADO").toString());
				
			}	
			if (miHash.get("PROCEDIMIENTO")!=null)
				procedimiento = miHash.get("PROCEDIMIENTO").toString();
			if (miHash.get("ASUNTO")!=null)
				asunto = miHash.get("ASUNTO").toString();		

			if (miHash.get(ScsTurnoBean.C_IDTURNO)!=null){
				idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());
					
			}	
		}
	}catch (Exception e){					};
	
	//String idTurnos = new String("");

	
	try {
		idTurno.add(miHash.get(ScsTurnoBean.C_IDTURNO).toString());
		
		idGuardia.add(miHash.get(ScsGuardiasTurnoBean.C_IDGUARDIA).toString());
		idTipoEJG.add(miHash.get(ScsTipoEJGBean.C_IDTIPOEJG).toString());
		idTipoEJGColegio.add(miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO).toString());
		idEstado.add(miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString());
	} catch (Exception e) {};
	
	java.util.ResourceBundle rp=java.util.ResourceBundle.getBundle("SIGA");
    String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
    String datoTipoOrdinario[]={idordinario,idordinario};	
	String[] datos={usr.getLocation()};	
	ArrayList juzgadoSel   = new ArrayList();
	String dato[] = {(String)usr.getLocation()};
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script type="text/javascript">	
		
		function refrescarLocal() {
			
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
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.literal.expedientesEJG"
		localizacion="gratuita.busquedaEJG_CAJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="inicio();ajusteAlto('resultado');" >

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="/JGR_E-Comunicaciones_Seleccion.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
		<html:hidden property = "descripcionEstado" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden name="BusquedaCAJG_EJGForm" property="idPersona" value=""></html:hidden>
		<html:hidden property = "numero" value = ""/>
		<html:hidden property = "selDefinitivo" value = ""/>
		

		

	<fieldset name="fieldset2" id="fieldset2" style="display:none">
	<legend>
		<span class="boxConsulta" onclick="ocultarConjunto(0);">
			<siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/>
		</span>
	</legend>
	</fieldset>

	<fieldset name="fieldset1" id="fieldset1">
	<legend>
		<span  class="boxConsulta" onclick="ocultarConjunto(1);">
			<siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/>
		</span>
	</legend>
	
	<table align="center" width="100%" border="0">
	<tr>	
	<td class="labelText" width="110">
		<siga:Idioma key="gratuita.busquedaEJG.literal.anyo"/>	
	</td>
	<td class="labelText">	
		<html:text name="BusquedaCAJG_EJGForm" property="anio" size="4" maxlength="4" styleClass="box"  value="<%=anio%>"></html:text>
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.codigo"/>
	</td>
	<td class="labelText">		
		<html:text name="BusquedaCAJG_EJGForm" property="numEJG" size="10" maxlength="10" styleClass="box"  value="<%=numEJG%>"></html:text>
	</td>
	</tr>
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.estadoEJG"/>
	</td>
	<td class="labelText">
		<siga:ComboBD nombre="estadoEJG" tipo="estadosEJG" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idEstado%>"/>
	</td>
	
		<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.dictaminado"/>	
	</td>
	<td class="labelText"> 
		<html:select styleClass="boxCombo" property="dictaminado" value="I">
			<html:option value="S"><siga:Idioma key="gratuita.busquedaSOJ.literal.si"/></html:option>
			<html:option value="N"><siga:Idioma key="gratuita.busquedaSOJ.literal.no"/></html:option>
			<html:option value="I"><siga:Idioma key="gratuita.busquedaSOJ.literal.indiferente"/></html:option>
		</html:select>
	</td>
	<td class="labelText" nowrap>  
		<siga:Idioma key="gratuita.busquedaSOJ.literal.creadoDesde"/>
	</td>
	<td class="labelText"> 
		<select name="creadoDesde" class="box">
			<option value=""></option>
			<option value="A" <%if (creadoDesde.startsWith("A")) {%>selected<%}%>>ASISTENCIA</option>
			<option value="D" <%if (creadoDesde.startsWith("D")) {%>selected<%}%>>DESIGNA</option>
			<option value="S" <%if (creadoDesde.startsWith("S")) {%>selected<%}%>>SOJ</option>
			<option value="M" <%if (creadoDesde.startsWith("M")) {%>selected<%}%>>MANUAL</option>
		</select>
	</td>
	
	</tr>
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG"/>
	</td>	
	<td class="labelText">	
		<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG" clase="boxCombo" obligatorio="false" parametro="<%=datoTipoOrdinario%>" elementoSel="<%=idTipoEJG%>"/>
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.EJGColegio"/>
	</td>
	<td class="labelText" colspan="3">
		<siga:ComboBD nombre="idTipoEJGColegio" tipo="tipoEJGColegio" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idTipoEJGColegio%>"/>
	</td>	
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>
	</td>
	<td class="labelText" >
		<siga:ComboBD nombre = "identificador" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:guardiaTurnoIdGuardia" parametro="<%=dato%>" elementoSel="<%=idTurno%>" ancho="260" />
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>
	</td>
	<td class="labelText" colspan="3">
		<siga:ComboBD nombre = "guardiaTurnoIdGuardia" tipo="guardias" clase="boxCombo" obligatorio="false" hijo="t" elementoSel="<%=idGuardia%>" ancho="320" />
	</td>	
	</tr>
	
	<tr>
	<td class="labelText" nowrap>
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaAperturaDesde"/>
	</td>
	<td class="labelText" >
		<html:text name="BusquedaCAJG_EJGForm" property="fechaAperturaDesde" size="10" maxlength="10" styleClass="box"  value="<%=fechaApertura%>" readOnly="true"></html:text>
		<a onClick="return showCalendarGeneral(fechaAperturaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
	</td>
	<td class="labelText" nowrap>
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaAperturaHasta"/>
	</td>
	<td class="labelText" colspan="2" >
		<html:text name="BusquedaCAJG_EJGForm" property="fechaAperturaHasta" size="10" maxlength="10" styleClass="box"  value="<%=fechaApertura%>" readOnly="true"></html:text>
		<a onClick="return showCalendarGeneral(fechaAperturaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
	</td>
	</tr>
	
	<tr>
	<td class="labelText" nowrap>
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaLimiteDesde"/>
	</td>
	<td class="labelText" >
		<html:text name="BusquedaCAJG_EJGForm" property="fechaLimitePresentacionDesde" size="10" maxlength="10" styleClass="box"  value="<%=fechaApertura%>" readOnly="true"></html:text>
		<a onClick="return showCalendarGeneral(fechaLimitePresentacionDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
	</td>
	<td class="labelText" nowrap>
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaLimiteHasta"/>
	</td>
	<td class="labelText" colspan="2" >
		<html:text name="BusquedaCAJG_EJGForm" property="fechaLimitePresentacionHasta" size="10" maxlength="10" styleClass="box"  value="<%=fechaApertura%>" readOnly="true"></html:text>
		<a onClick="return showCalendarGeneral(fechaLimitePresentacionHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
	</td>
	</tr>

	</table>

	<siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="idPersona">
	</siga:BusquedaPersona>

	</fieldset>
	
	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.defensa">
		<table  border="0" align="center" width="100%">
		<tr>
		<td class="labelText" colspan="1">
			<siga:Idioma key="gratuita.personaJG.literal.calidad"/>
		</td>
		<td class="labelText" colspan="1">
			<Select name="calidad" class="boxCombo">
				<%if(!calidad.equals("")){%>
					<%if(calidad.equals("D")){%>			
						<option value=""></option>
						<option value="D" selected><siga:Idioma key="gratuita.personaJG.calidad.literal.demandante"/></option>
						<option value="O"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandado"/></option>
					<%}%>
					<%if(calidad.equals("O")){%>
						<option value=""></option>
						<option value="D"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandante"/></option>
						<option value="O" selected><siga:Idioma key="gratuita.personaJG.calidad.literal.demandado"/></option>
					<%}%>
				<%}else{%>	
					<option value="" selected></option>
					<option value="D"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandante"/></option>
					<option value="O"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandado"/></option>
				<%}%>					
			</Select>		
		</td> 
		
		<td class="labelText" >
			<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado"/>
		</td>
		
		<td class="labelText" >
	
			<input type="text" name="codigoExtJuzgado" class="box" size="8"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />
			<siga:ComboBD nombre="juzgado" tipo="comboJuzgados" ancho="500" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  hijo="t" elementoSel="<%=juzgado%>" parametro="<%=datos%>"/>           	   
		</td>
		</tr><tr>
		<td class="labelText">
			<siga:Idioma key="informes.cartaAsistencia.procedimiento"/>
		</td>
		<td class="labelText">
			<html:text name="BusquedaCAJG_EJGForm" property="procedimiento" size="15" maxlength="100" styleClass="box"  value="<%=procedimiento%>"></html:text>
		</td>
		<td class="labelText">	
			<siga:Idioma key="informes.cartaAsistencia.asunto"/>
		</td>	
		<td class="labelText">
			<html:text name="BusquedaCAJG_EJGForm" property="asunto" size="15" maxlength="100" styleClass="box"  value="<%=asunto%>"></html:text>
		</td>
		</tr>
		</table>
	</siga:ConjCampos>
	
	
	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.solicitante">
	<table  align="center" width="100%">
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.nif"/>		
	</td>
	<td class="labelText">
		<html:text name="BusquedaCAJG_EJGForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.nombre"/>
	</td>
	<td class="labelText">
		<html:text name="BusquedaCAJG_EJGForm" property="nombre" size="15" maxlength="100" styleClass="box"  value="<%=nombre%>"></html:text>
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaEJG.literal.apellido1"/>
	</td>		
	<td class="labelText">
		<html:text name="BusquedaCAJG_EJGForm" property="apellido1" size="15" maxlength="100" styleClass="box"  value="<%=apellido1%>"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.apellido2"/>
	</td>
	<td class="labelText">
		<html:text name="BusquedaCAJG_EJGForm" property="apellido2" size="15" maxlength="100" styleClass="box"  value="<%=apellido2%>"></html:text>
	</td>	
	</tr>	
	</table>
	</siga:ConjCampos>
	</html:form>
	
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="le,B"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

	
	function ocultarConjunto(b) 
{
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
		 function obtenerJuzgado() 
			{ 
			  if (document.forms[1].codigoExtJuzgado.value!=""){

  				   document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[1].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.submit();		
				   
				
			 }
			}
	//		
	
	

		<!-- Funcion asociada a boton buscar -->
		
		function buscarPaginador() 
		{
			document.forms[1].modo.value = "buscarPor";
			
			/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			document.forms[1].descripcionEstado.value = document.forms[1].estadoEJG[document.forms[1].estadoEJG.selectedIndex].text;



			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
			if (isNaN(document.forms[1].anio.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
			}
			/*else if (isNaN(document.forms[1].numEJG.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
			}  Podemos hacer la busqueda por este campo con comodines*/
			else if (isNaN(document.forms[1].idPersona.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
			}
			else document.forms[1].submit();
		}		
		
		function buscar() 
		{
			sub();
			document.forms[1].modo.value = "buscarInit";
			
			/* El idenficiador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			document.forms[1].descripcionEstado.value = document.forms[1].estadoEJG[document.forms[1].estadoEJG.selectedIndex].text;



			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
			if (isNaN(document.forms[1].anio.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
				fin();
				return false;
			}
			/*else if (isNaN(document.forms[1].numEJG.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
			}  Podemos hacer la busqueda por este campo con comodines*/
			else if (isNaN(document.forms[1].idPersona.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
				fin();
				return false;
			}
			else 
				document.forms[1].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[1].reset();
		}
		function traspasoDatos(resultado){
		   seleccionComboSiga("juzgado",resultado[0]);
		}	
		<!-- Funcion asociada a boton ListoParaEnviar -->
		
		function getElementsByAttr(attr){
var all = document.frames.resultado.document.getElementsByTagName('*');
var elements = [];
for(var i = 0; i < all.length; i++){
if(all[i].getAttribute(attr))elements.push(all[i]);
}
return elements;
} 


		 
		 
		function accionListoParaEnviar() 
		{
			try{
			    var dat = "";
			    var datos1 = document.frames.resultado.document.BusquedaCAJG_EJGForm.selDefinitivo;
			    document.forms[1].selDefinitivo.value=datos1.value;
			    
			  	document.forms[1].modo.value = "listosParaComision";
			  	var f = document.forms[1].name;	
			  	//Para quien se encargue del marrón de la ruedecita, con mucho cariño para ....
				//document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=bot que pasa';
	
			    document.forms[1].submit();
			}
			catch (e){
			
			alert('<siga:Idioma key="messages.cajg.error.listos"/>');
				
			}
		    
		  
			
		}
		function accionCerrar() 
		{		
			window.close();
		}
		
		function traspasoDatos(resultado){
		 seleccionComboSiga("juzgado",resultado[0]);
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

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<iframe name="submitArea1" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>