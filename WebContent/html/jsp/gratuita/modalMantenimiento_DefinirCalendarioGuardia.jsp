<!-- modalMantenimiento_DefinirCalendarioGuardia.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.gratuita.action.DefinirGuardiasTurnosAction"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	Hashtable datosHash = new Hashtable();
	String idinstitucion="", idturno="", idguardia="", idcalendarioguardias="";
	String tipodias="", diasguardia="", num_letrados="", num_sustitutos="", diasacobrar="";
	String fechaDesde="", fechaHasta="", observaciones="", modo="";

	String diasSeparacionGuardias="";
	String tipoDiasGuardia="";
	String sTipoDiasGuardia="";

	//Obtengo del request la hash con los datos iniciales
	if (request.getAttribute("DATOSINICIALES") != null) {
		datosHash = (Hashtable)request.getAttribute("DATOSINICIALES");
		idcalendarioguardias = (String)datosHash.get("IDCALENDARIOGUARDIAS");
		idinstitucion = (String)datosHash.get("IDINSTITUCION");
		idturno = (String)datosHash.get("IDTURNO");
		idguardia = (String)datosHash.get("IDGUARDIA");
		tipodias = (String)datosHash.get("TIPODIAS");
		diasguardia = (String)datosHash.get("DIASGUARDIA");
		diasacobrar = (String)datosHash.get("DIASACOBRAR");		
		num_letrados = (String)datosHash.get("NUMEROLETRADOS");
		num_sustitutos = (String)datosHash.get("NUMEROSUSTITUTOS");
		fechaDesde = (String)datosHash.get("FECHADESDE");
		fechaHasta = (String)datosHash.get("FECHAHASTA");
		observaciones = (String)datosHash.get("OBSERVACIONES");
		modo = (String)datosHash.get("modo");

		diasSeparacionGuardias = (String)datosHash.get("DIASSEPARACIONGUARDIAS");
		tipoDiasGuardia = (String)datosHash.get("TIPODIASGUARDIA");
		sTipoDiasGuardia = DefinirGuardiasTurnosAction.obtenerTipoDiaPeriodo(tipoDiasGuardia, usr);
	}
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"type="text/javascript"></script>		
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DefinirCalendarioGuardiaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<script>

		//Busqueda de colegiados de guardias			
		function refrescarGuardias() {
			sub();
			document.forms[0].reserva.value="N";			
			document.forms[0].accion.value="guardias";	
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="guardias";
			document.forms[0].submit();
		}

		//Busqueda de colegiados de guardias y reservas 	
		function refrescarLocal() {
			refrescarGuardias()
		}
		
	</script>
</head>

<body onload="ajusteAlto('guardias');">

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
		<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.titulo"/>
	</td>
</tr>
</table>
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCampos" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="submitArea">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "<%=modo%>"/>
		<html:hidden property = "modoOriginal" value = "<%=modo%>"/>
		<html:hidden property = "accion" value = ""/>		
		<html:hidden property = "idCalendarioGuardias" value = "<%=idcalendarioguardias%>"/>
		<html:hidden property = "idInstitucion" value = "<%=idinstitucion%>"/>
		<html:hidden property = "idTurno" value = "<%=idturno%>"/>
		<html:hidden property = "idGuardia" value = "<%=idguardia%>"/>
		<html:hidden property = "diasACobrar" value = "<%=diasacobrar%>"/>
		<html:hidden property = "diasGuardia" value = "<%=diasguardia%>"/>		
		<html:hidden property = "tipoDias" value = "<%=tipodias%>"/>
		<html:hidden property = "reserva" value = "N"/>
		<html:hidden property = "numeroLetrados" value = "<%=num_letrados%>"/>
		<html:hidden property = "numeroSustitutos" value = "<%=num_sustitutos%>"/>	
		<html:hidden property = "actionModal" value = ""/>
					

	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.tabla">
		<table align="left" cellspacing="0" cellpadding="0">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.letradosGuardia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;
				<%=num_letrados%>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.diasGuardia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;				
				<%=diasguardia%>&nbsp;<%=sTipoDiasGuardia%>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.diasSeparacionGuardias"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;				
				<%=diasSeparacionGuardias%>				
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.tipoDia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;								
				<%=((String)datosHash.get("TIPODIAS"))%>
			</td>
			<td>
				&nbsp;
			</td>
		</tr>
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
	<td>
		<siga:ConjCampos>
		<table align="center" width="100%">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.fechaDesde"/>
			</td>
			<td class="labelTextValor">
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaDesde" size="10" styleClass="boxConsulta" value="<%=fechaDesde%>" readonly="true"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.fechaHasta"/>
			</td>
			<td class="labelTextValor">
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaHasta" size="10" styleClass="boxConsulta" value="<%=fechaHasta%>" readonly="true"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.observaciones"/>
			</td>
			<td class="labelTextValor">
				<% if (modo.equalsIgnoreCase("VER")) { %>
					<html:textarea name="DefinirCalendarioGuardiaForm" property="observaciones" cols="60" rows="2" style="overflow:auto" styleClass="boxConsulta" value="<%=observaciones%>" readOnly="true" ></html:textarea>
				<% } else { %>
					<html:textarea name="DefinirCalendarioGuardiaForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" property="observaciones" cols="60" rows="2" style="overflow:auto" styleClass="boxCombo" value="<%=observaciones%>" readOnly="false" ></html:textarea>
				<% } %>
			</td>
		</tr>
		</table>
		</siga:ConjCampos>
	</td>
	</tr>
	</table>
	
	<siga:ConjCampos leyenda="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.criterios">	
			<table class="tablaCampos" align="left"  cellspacing=0 cellpadding=0>
				<tr>				
					<td width="120" class="labelText"><siga:Idioma key="gratuita.guardiasColegiadoAsistencia.literal.ncolegiado"/></td>
					<td width="5"><html:text name="DefinirCalendarioGuardiaForm" styleClass="box" property="buscarColegiado" maxlength="20" size="5" /></td>

					<td width="140" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaDesde"/></td>
					<td width="140"><html:text name="DefinirCalendarioGuardiaForm" styleClass="box" property="buscarFechaDesde" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaDesde);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
		
					<td width="120" class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.FechaHasta"/></td>
					<td><html:text name="DefinirCalendarioGuardiaForm" styleClass="box" property="buscarFechaHasta" size="8" maxlength="10" readonly="true"/><a href='javascript://'onClick="return showCalendarGeneral(buscarFechaHasta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a></td>
					<td>
					<html:button property="idButton" onclick="return refrescarGuardias();" styleClass="button"><siga:Idioma key="general.boton.search"/> </html:button>
					</td>
				</tr>
			
			</table>
			
	</siga:ConjCampos>	
	
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	
	<siga:ConjBotonesAccion botones="GC,Y,C,NL" clase="botonesSeguido" modal="G" modo="<%=modo%>" />
<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
  <input type="hidden" name="actionModal" value="">
  <input type="hidden" name="modo" value="abrirBusquedaModal">
  <input type="hidden" name="clientes" value="1">
 
 </html:form>
 

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		<!-- Funcion asociada a boton buscarCliente -->
		function buscarCliente() 
		{
			var datos = new Array();
			datos[0] = "1000"; 						// idpersona
			datos[1] = "2032"; 						// idInstitucion
			datos[2] = "nColegiado"; 			// Numero Colegiado
			datos[3] = "nifcif"; 					// NIF
			datos[4] = "Nombre"; 					// Nombre
			datos[5] = "Apellido1"; 			// Apellido1
			datos[6] = "Apellido2"; 			// Apellido2

			var datos = ventaModalGeneral("busquedaClientesModalForm","G");
			if ((datos == null) ||(typeof datos[0] == "undefined"))  {
			
				return false;
			}
			
			document.DefinirCalendarioGuardiaForm.buscarIdPersona.value 		 = datos[0];

			
		}

		
		
		

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
						
				// Rango Fechas (desde / hasta)
				if (compararFecha (document.DefinirCalendarioGuardiaForm.buscarFechaDesde, document.DefinirCalendarioGuardiaForm.buscarFechaHasta) == 1) {
					mensaje = '<siga:Idioma key="messages.fechas.rangoFechas"/>'
					alert(mensaje);
					return false;
				}
				document.BusquedaFacturaForm.modo.value = "buscarInit";
				document.BusquedaFacturaForm.submit();
		}
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		<!-- Funcion que devuelve true si alguna de las fechas del calendario es anterior a la actual
		function esFechaAnteriorHoy(){
			var f_desde = document.forms[0].fechaDesde.value; //DD/MM/YYYY
			
			var mes_1 = parseInt(f_desde.substring(3,5),10)-1;
			var f_1 = new Date(f_desde.substring(6,10), mes_1, f_desde.substring(0,2));
			
			calDate=new Date();
			calDate2=new Date(calDate.getFullYear(), calDate.getMonth(), calDate.getDate());
			
			if (f_1.getTime() < calDate2.getTime())
				return true;
			else 
				return false;
		}
	
		<!-- Asociada al boton GenerarCalendario -->
		function accionGenerarCalendario() {
			// Creo Calendario de Titulares y despues el de Reservas
			document.forms[0].modo.value = "insertarCalendarioAutomaticamente";
			document.forms[0].target = "submitAreaPrincipal";							
			var fname = document.getElementById("DefinirCalendarioGuardiaForm").name;
			sub();
			//Control de la fecha de generacion:
			if (esFechaAnteriorHoy()){
				if (confirm('<siga:Idioma key="general.aviso.generarCalendarioFechasAnteriorHoy"/>'))
					document.frames.submitAreaPrincipal.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.gratuita.generandoCalendario';						
				else{
					fin();
					return false;
				}
			} else				
				document.frames.submitAreaPrincipal.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.gratuita.generandoCalendario';			
		}
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
			//Valido por struts las observaciones
			if (validateDefinirCalendarioGuardiaForm(document.DefinirCalendarioGuardiaForm)){
					document.forms[0].modo.value = "modificar";
					document.forms[0].target = "submitAreaPrincipal";							
					document.forms[0].submit();	
					window.returnValue="MODIFICADO";
			}
			else{ 
				alert('<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.aviso1"/>');
				fin();
				return false;
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

		<!-- Funcion asociada a boton Nuevo -->
		function accionNuevoLetrado() {		
			document.forms[0].modo.value = "modalNuevaGuardia";
			var salida = ventaModalGeneral(document.forms[0].name,"M");	
			if (salida == "MODIFICADO") 
				refrescarGuardias();
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="guardias"
					name="guardias" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitAreaPrincipal" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>