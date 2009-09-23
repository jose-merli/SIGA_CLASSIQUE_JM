<!-- modalRegistro_DefinirCalendarioGuardia.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.gratuita.action.DefinirCalendarioGuardiaAction"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
   
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	ArrayList arrayPeriodos = (ArrayList)request.getAttribute("ARRAYPERIODOS");
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<html>

<!-- HEAD -->
<head>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DefinirCalendarioGuardiaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"type="text/javascript"></script>		

	<script>
		function actualizarFechaApertura() {
			var periodo = document.forms[0].periodos;
			var fecha1 = periodo.value.substring(0,periodo.value.indexOf(','));
			document.forms[0].fechaApertura.value = fecha1;			
		}
	</script>

</head>

<body onload="actualizarFechaApertura()">

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.modalRegistro_DefinirCalendarioGuardia.literal.tituloReserva"/>
	</td>
</tr>
</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposMedia" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post">
		<html:hidden property = "modo" value = "modificar"/>
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>

		<html:hidden property = "idInstitucion"/>
		<html:hidden property = "idCalendarioGuardias"/>
		<html:hidden property = "idTurno"/>
		<html:hidden property = "idGuardia"/>
		<html:hidden property = "fechaApertura"/>
		<html:hidden property = "fechaInicio"/>
		<html:hidden property = "fechaFin"/>
		<html:hidden property = "indicePeriodo"/>
		<html:hidden property = "idPersona" value=""/>
		<html:hidden property = "flagSalto" value=""/>
		<html:hidden property = "flagCompensacion" value=""/>		
	
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="gratuita.calendarioGuardias.literal.periodo">		
		<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo" />
			</td>
			<td>
				<html:select name="DefinirCalendarioGuardiaForm" property="periodos" styleClass="boxCombo" size="1" onchange="actualizarFechaApertura()">
					<% if (arrayPeriodos==null || arrayPeriodos.isEmpty()) { %>
						<html:option value="0" key="informes.cartaAsistencia.error"/>
				<% } else { 
						Iterator iter = arrayPeriodos.iterator();
						int i=0;
						while (iter.hasNext()){
							ArrayList arraydiasPeriodo = new ArrayList();
							arraydiasPeriodo = (ArrayList)iter.next();
							String value = arraydiasPeriodo.get(0)+","+arraydiasPeriodo.get(arraydiasPeriodo.size()-1)+ "("+i+")";
							i++;
							%>
					 	    <html:option value="<%=value%>" >
						    <%=UtilidadesString.getMensajeIdioma(usr,"gratuita.calendarioGuardias.literal.periodo")+" "+arraydiasPeriodo.get(0)+" - "+arraydiasPeriodo.get(arraydiasPeriodo.size()-1)%>
							</html:option>
						<% } %>
				<% } %>
				</html:select>
			</td>
		</tr>
		</table>
		</siga:ConjCampos>				
	</td>
	</tr>
	<td>

	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.titulo">		
		<table class="tablaCampos" border="0" width="100%">		
			<tr>
				<td colspan="5">
					<siga:BusquedaSJCS nombre="DefinirCalendarioGuardiaForm" propiedad="buscaLetrado"
		 				   concepto="GUARDIA" operacion="Asignacion" 
						   campoTurno="idTurno" campoGuardia="idGuardia" campoFecha="fechaApertura"
						   campoPersona="idPersona" campoColegiado="NColegiado"  campoNombreColegiado="nomColegiado"
						   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion"
						   modo="nuevo"/>
				</td>
			</tr>					   
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
				</td>		
				<td>
					<input type="text" name="NColegiado" class="boxConsulta" readOnly value="" style="width:'100px';">
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
	
	</td>
	</tr>
	</table>
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
<!-- FIN ******* CAPA DE PRESENTACION ****** -->

		<siga:ConjBotonesAccion botones="Y,C" modal="M"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->

	<script language="JavaScript">
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
				sub();
				var chequeoOk = true;
				var periodo = document.forms[0].periodos;

				//Chequeo que he seleccionado un periodo:
				if (periodo.value=='') {
					chequeoOk = false;
					alert('<siga:Idioma key="gratuita.calendarioGuardias.literal.periodo"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
					fin();
					return false;
				}
				
				//Chequeo que ha seleccionado un valor en el combo:
				if(chequeoOk && document.forms[0].idPersona.value == '') {
					chequeoOk = false;
					alert('<siga:Idioma key="gratuita.modalRegistro_DefinirCalendarioGuardia.literal.seleccione"/>');
					fin();
					return false;
				}

				if (chequeoOk) {												
					var fechaInicio = periodo.value.substring(0,periodo.value.indexOf(','));
					var fechaFin = periodo.value.substring(periodo.value.indexOf(',')+1,periodo.value.indexOf('('));
					var indicePeriodo = periodo.value.substring(periodo.value.indexOf('(')+1,periodo.value.indexOf(')'));
					document.forms[0].fechaInicio.value = fechaInicio;
					document.forms[0].fechaFin.value = fechaFin;
					document.forms[0].indicePeriodo.value = indicePeriodo;
					document.forms[0].modo.value = "insertarGuardiaManual";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
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
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>