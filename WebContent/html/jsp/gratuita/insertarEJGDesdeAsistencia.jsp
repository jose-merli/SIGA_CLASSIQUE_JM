<!-- insertarEJGDesdeAsistencia.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String idInstitucion = "", 
         anioAsistencia = "", 
         numeroAsistencia = "", 
         idPersonaJG = "", 
         nColegiado="",
		 idGuardiaAsistencia="",
		 idTurnoAsistencia="",
		 nombreColegiado="",
		 numeroColegiado="";
		 

	anioAsistencia   = (String) request.getAttribute("anioAsistencia");
	numeroAsistencia = (String) request.getAttribute("nmeroAsistencia");
	idInstitucion    = (String) request.getAttribute("idInstitucionAsistencia");
	idPersonaJG      = (String) request.getAttribute("idPersonaJGAsistencia");
	
	idTurnoAsistencia      = (String) request.getAttribute("idTurnoAsistencia");
	ArrayList elementoSelTurno = new ArrayList();
	
	if (idTurnoAsistencia != null && !idTurnoAsistencia.equals("")) {
		elementoSelTurno.add(idTurnoAsistencia);
		
	}
	idGuardiaAsistencia      = (String) request.getAttribute("idGuardiaAsistencia");
	ArrayList elementoSelGuardia = new ArrayList();
	
	if (idGuardiaAsistencia != null && !idGuardiaAsistencia.equals("")) {
		elementoSelGuardia.add(idGuardiaAsistencia);
	}
    nombreColegiado      = (String) request.getAttribute("nombreColegiado");
	numeroColegiado      = (String) request.getAttribute("nColegiadoAsistencia");
	String dato[] = {idInstitucion};

	try {
		nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	} 
	catch (Exception e)	{
	}
	
	String fecha = UtilidadesBDAdm.getFechaBD("");

	String diligenciaAsi = (String)request.getAttribute("diligencia");
	String procedimientoAsi = (String)request.getAttribute("procedimiento");
	String comisariaAsi = (String)request.getAttribute("comisaria");
	String juzgadoAsi = (String)request.getAttribute("juzgado");

    String[] datoGuardia = new String[2];
	try {
		datoGuardia[0] = usr.getLocation();
		if (idTurnoAsistencia!=null){
		 datoGuardia[1] = (idTurnoAsistencia.split(","))[1];
		}
		
	}
	catch(Exception e) {}
	
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
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
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
		
	/*	function busquedaAutomatica ()
		{
			var idTurno = document.forms[2].identificador.value;
			var idGuardia = document.forms[2].identificador2.value;
			var posicion = 0;
					
			if ((idTurno != "") && ( idGuardia !=""))
			{
				// Se recorre hasta encontrar el separador, que es ","									
				posicion = idTurno.indexOf(',') + 1;
				// El substring que queda a partir de ahí es el identificador del turno 
				document.forms[1].guardiaTurnoIdTurno.value = idTurno.substring(posicion);
				document.forms[1].guardiaTurnoIdGuardia.value = idGuardia;
				document.forms[1].target="submitArea";
				document.forms[1].modo.value="AbrirAvanzada";
				document.forms[2].tipoLetrado.value="P";
				document.forms[1].submit();
			}
			else alert("Seleccione turno y guardia");
			
		}
	*/
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
	
	<html:form action="/JGR_EJG" method="POST" target="submitArea" type="">
		<!-- Para seleccion automatica -->
	<html:hidden property = "idTurno" value="" />
	<html:hidden property = "idGuardia" value=""  />	
	
	<!-- ********************************************** //-->
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
	<html:hidden property = "guardiaTurnoIdGuardia" value = ""/>	
	<html:hidden property = "anio" value = ""/>
	<html:hidden property = "numero" value = ""/>
	<html:hidden property = "idPersona" value = ""/>

	<html:hidden property = "asistenciaNumero" value = "<%=numeroAsistencia%>"/>
	<html:hidden property = "idPersonaJG" value = "<%=idPersonaJG%>"/>
	<html:hidden property = "asistenciaAnio" value = "<%=anioAsistencia%>"/>
	<html:hidden property = "origenApertura" value = "M"/>
	<html:hidden property = "tipoLetrado" value = "M"/>	
	<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaAperturaEJG" value = "sysdate"/>
	<input type="hidden" name = "flagSalto" value=""/>
	<input type="hidden" name = "flagCompensacion" value=""/>	
	
	<html:hidden property = "procedimiento"     value="<%=procedimientoAsi%>"/>
	<html:hidden property = "diligencia"     	value="<%=diligenciaAsi%>"/>

	<html:hidden property = "comisaria"     	value="<%=comisariaAsi%>"/>	
	<html:hidden property = "juzgado"     		value="<%=juzgadoAsi%>"/>	
	
	<tr>		
	<td>			
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.expedientesEJG">
	<table class="tablaCampos" align="center">
	
	<tr>		
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG"/>&nbsp;(*)
		</td>
		<td class="labelText" colspan="2">
			<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG"  parametro="<%=datoTipoOrdinario%>" clase="boxCombo" obligatorio="false" obligatorioSinTextoSeleccionar="true"/>
		</td>	
	</tr>
		
	<tr>
<!--	
	<td class="labelText">
		<siga:Idioma key="gratuita.insertarSOJ.literal.letradoTramitador"/>
	</td>	
	<td class="labelText">
		<% if (!usr.isLetrado() ) {%>
			<html:text name="DefinirEJGForm" property="NColegiado" size="10" maxlength="10" styleClass="box" value="<%=nColegiado%>"></html:text>
		<% } else { %>
			<html:text name="DefinirEJGForm" property="NColegiado" size="10" maxlength="10" styleClass="boxConsulta" value="<%=nColegiado%>" readOnly="true"></html:text>			
		<% } %>
	</td>
-->	
	<html:hidden name="DefinirEJGForm" property="NColegiado" value="<%=nColegiado%>"></html:hidden>			
	<!--<td class="labelText">	
		<input type="button" class="button" name="buscarColegiado" value="Buscar Colegiado" onClick="buscarCliente();" <%if(usr.isLetrado()){%>disabled<%}%> > 
	</td>
	<td class="labelText">
		<input type="button" class="button" name="buscarColegiado" value="Seleccion Automatica" onClick="busquedaAutomatica();" <%if(usr.isLetrado()){%>disabled<%}%> > 
	</td>//-->
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaApertura"/>&nbsp;(*)
	</td>
	<td class="labelText">		
		<html:text name="DefinirEJGForm" property="fechaApertura" size="10" maxlength="10" styleClass="box"  value="<%=fecha%>" readOnly="true"></html:text>
		<a onClick="return showCalendarGeneral(fechaApertura);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0" valign="bottom"></a>
	</td>
<!--<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.insertarSOJ.literal.demandante"/>
		<input type="radio" name="calidad" value="D" checked>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<siga:Idioma key="gratuita.insertarSOJ.literal.demandado"/>
		<input type="radio" name="calidad" value="O">
	</td>-->
	</tr>		
	</table>
	</siga:ConjCampos>	
	</td>
	</tr>
	<tr><td>
	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloEJG"> 
		<table>
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>&nbsp;
				</td>
				<td class="labelText" colspan="4">
					<siga:ComboBD nombre = "identificador" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:identificador2" ancho="550" parametro="<%=dato%>"/>
				</td>
			</tr>
			<tr>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>&nbsp;
				</td>
				<td class="labelText" colspan="4">
					<siga:ComboBD nombre = "identificador2" tipo="guardias" clase="boxCombo"  accion="parent.rellenarComboGuardia();" obligatorio="false" hijo="t"/>
				</td>
			</tr>	
			<tr>
				<td colspan="5">
					<siga:BusquedaSJCS nombre="DefinirEJGForm" propiedad="buscaLetrado"
				 				   concepto="EJG" operacion="Asignacion" 
								   campoTurno="identificador" campoGuardia="identificador2" campoFecha="fechaApertura"
								   campoPersona="idPersona" campoColegiado="numeroColegiado" campoNombreColegiado="nomColegiado"  
								   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
								   diaGuardia="true"
								   modo="nuevo"
		    					   />
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
				</td>		
				<td>
					<input type="text" name="numeroColegiado" class="boxConsulta" readOnly value="" style="width:'100px';">
				</td>
				<td class="labelText">
					<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
				</td>
				<td colspan="2">
					<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" style="width:'240px';">
				</td>			
			</tr>	
		</table>		    					   
		</siga:ConjCampos> 
	</td></tr>
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
			document.forms[2].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 		
		{	
			/* El identificador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			var id = document.forms[1].identificador.value;
			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/
			sub();									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[1].guardiaTurnoIdTurno.value = id.substring(posicion);
			document.forms[1].guardiaTurnoIdGuardia.value = document.forms[1].identificador2.value;
			if (validateDefinirEJGForm(document.forms[1])){
				document.forms[1].submit();
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
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
