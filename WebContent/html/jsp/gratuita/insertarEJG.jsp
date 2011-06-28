<!-- insertarEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<!-- JSP -->
<% 

String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	String designaNumero ="", designaAnio="", designaIdTurno="", nColegiado="",asistenciaAnio="",asistenciaNumero="";

	try{
		designaNumero = (String)request.getAttribute("designaNumero");
		designaAnio = (String)request.getAttribute("designaAnio");
		designaIdTurno = (String)request.getAttribute("designaIdTurno");
		nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
		asistenciaNumero = (String)request.getAttribute("asistenciaNumero");
		asistenciaAnio = (String)request.getAttribute("asistenciaAnio");
	}catch(Exception e){}
	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	java.util.ResourceBundle rp=java.util.ResourceBundle.getBundle("SIGA");
	String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	
	
%>

<html>
<!-- HEAD -->
<head>
	<html:javascript formName="DefinirEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">


	<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


	<!--Step 3 -->
	  <!-- defaults for Autocomplete and displaytag -->
	  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
	  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

<script type="text/javascript">	
		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado != null && resultado[2]!=null)
			{
				if (resultado[0]!=null) document.forms[2].idPersona.value=resultado[0];
				if (resultado[2]!=null)document.forms[2].NColegiado.value=resultado[2];
			}
		}

		<!-- Modif Carlos -->
		function busquedaAutomatica ()
		{
			
			document.forms[1].idTurno.value 	= document.forms[2].identificador.value;
			document.forms[1].idGuardia.value 	= document.forms[2].identificador2.value;
			document.forms[1].modo.value		= "buscarPor";
			document.forms[1].action			= "<%=app%>"+"/JGR_MantenimientoAsistencia.do";
			if(document.forms[2].identificador.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
				return false;
			}
			if(document.forms[2].identificador2.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert4' />");
				return false;
			}
			var resultado = ventaModalGeneral(document.forms[1].name,"M");
			if (resultado != null && resultado[0]!=null)
				document.forms[2].NColegiado.value = resultado[0]; 
			else
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert5' />");
		}
		<!-- Fin Modif Carlos -->


		
		function actualizarFecha(){
		  document.forms[1].fechaAperturaEJG.value=DefinirEJGForm.fechaApertura.value;
		}

		function preAccionTurno(){
			document.getElementById("guardias").disabled= true;
			
		}
	</script>
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/>
		</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposMedia" align="center">	
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = ""/>
	</html:form>
	
	<html:form action="/JGR_EJG.do" method="POST" target="submitArea" type="">
	
	<!-- Para seleccion automatica -->
	
	<!-- ************************ //-->
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
	<html:hidden property = "guardiaTurnoIdGuardia" value = ""/>	
	<html:hidden property = "anio" value = ""/>
	<html:hidden property = "idPersona" value = ""/>
	<html:hidden property = "designaNumero" value ="<%=designaNumero%>"/>
	<html:hidden property = "designaAnio" value ="<%=designaAnio%>"/>
	<html:hidden property = "designaIdTurno" value ="<%=designaIdTurno%>"/>
	<html:hidden property = "numero" value = ""/>
	<html:hidden property = "origenApertura" value = "M"/>
	<html:hidden property = "tipoLetrado" value = "M"/>	
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaAperturaEJG" value = "sysdate"/>
	<html:hidden  property="asistenciaAnio" value ="<%=asistenciaAnio%>" />
	<html:hidden  property="asistenciaNumero" value ="<%=asistenciaNumero%>" />
	<html:hidden  property="origen" />
	
	<input type="hidden" name = "flagSalto" value=""/>
	<input type="hidden" name = "flagCompensacion" value=""/>	
	<html:hidden name="DefinirEJGForm" property="NColegiado" value="<%=nColegiado%>"/>	
	<tr><td>			
	
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.expedientesEJG" >
		<table class="tablaCampos" align="center" border ="0">

			<tr>		
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG"/>&nbsp;(*)
				</td>
				<td class="labelText">
					<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG"  parametro="<%=datoTipoOrdinario%>" clase="boxCombo" obligatorio="false"  obligatorioSinTextoSeleccionar="true" />
				</td>	
			</tr>
				
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaApertura"/>&nbsp;(*)
				</td>
				<td class="labelText">		
					<html:text name="DefinirEJGForm" property="fechaApertura" size="10" maxlength="10" styleClass="box"  value="<%=fecha%>" readOnly="true"></html:text>
					<a onClick="showCalendarGeneral(fechaApertura);actualizarFecha();rellenarComboGuardia();" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
				</td>
			</tr>
			
		</table>
		</siga:ConjCampos>	
	
	</td></tr>
	
	<tr><td>
	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloEJG"> 

		<table class="tablaCampos" border="0" >

			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>&nbsp;
				</td>
				<td>
					<html:select styleId="turnos" styleClass="boxCombo" style="width:'320px';" property="idTurno">
						<bean:define id="turnos" name="DefinirEJGForm" property="turnos" type="java.util.Collection" />
						<html:optionsCollection name="turnos" value="idTurno" label="nombre" />
					</html:select>
				</td>
			</tr>
			<tr>		
				<td class="labelText" colspan="3"><siga:Idioma key="gratuita.busquedaEJG.literal.servicioTramitacion"/>&nbsp;
				<html:checkbox property="idTipoTurno"  onclick="activarTipoTurno(this);" /></td>
			</tr>	
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>&nbsp;
				</td>
				<td><html:select styleId="guardias" styleClass="boxCombo" style="width:'240px';" property="idGuardia" disabled="true">
						<bean:define id="guardias" name="DefinirEJGForm" property="guardias" type="java.util.Collection" />
						<html:optionsCollection name="guardias" value="idGuardia" label="nombre" />
					</html:select>
				</td>
			</tr>	
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="4">
					<siga:BusquedaSJCS nombre="DefinirEJGForm" propiedad="buscaLetrado"
	 				   concepto="EJG" operacion="Asignacion" 
					   campoTurno="idTurno" campoGuardia="idGuardia" campoFecha="fechaApertura"
					   campoPersona="idPersona" campoColegiado="numeroColegiado" campoNombreColegiado="nomColegiado"  
					   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
					   diaGuardia="true"
					   modo="nuevo"
					   />				
				</td>
			</tr>
			</table>
			<table>
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
				</td>		
				<td>
					<input type="text" name="numeroColegiado" class="boxConsulta" readOnly value="">
				</td>
				<td class="labelText">
					<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
				</td>
				<td>
					<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" style="width:'240px';">
				</td>			
			</tr>	
			
		</table>
				
	</siga:ConjCampos> 
	</td></tr>
	
<ajax:updateSelectFromField
	baseUrl="/SIGA/JGR_EJG.do?modo=getAjaxTurnos"
	source="idTipoTurno" target="turnos"
	parameters="idTipoTurno={idTipoTurno},idInstitucion={idInstitucion}" 
	/>	
	
<ajax:select
	baseUrl="/SIGA/JGR_EJG.do?modo=getAjaxGuardias"
	source="turnos" target="guardias" 
	parameters="idInstitucion={idInstitucion},idTurno={idTurno}"
	preFunction="preAccionTurno" />
	
	</html:form>
	</table>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 		
		{	
			sub();

			document.forms[1].guardiaTurnoIdTurno.value = document.forms[1].idTurno.value;
			document.forms[1].guardiaTurnoIdGuardia.value = document.forms[1].idGuardia.value;	

			if (validateDefinirEJGForm(document.forms[1])){
				document.forms[1].modo.value="Insertar";
				document.forms[1].submit();			
				window.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			}

		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}

		function activarTipoTurno(valorCheck){
			if(valorCheck){
				document.getElementById('idTipoTurno').value = '1';
			}

			document.getElementById('idTipoTurno').onchange();
		}			
		 
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
