<!-- busquedaAsistenciasLetrado.jsp -->
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
<%@ page import="java.util.*"%>

<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String nombreColegiado =  request.getAttribute("nombreColegiado")==null?"":(String)request.getAttribute("nombreColegiado");

	String[] dato = {usr.getLocation()};
	// Comprobamos si existe busqueda anterior
	Hashtable busqueda = (Hashtable) ses.getAttribute("busqueda");
	ses.removeAttribute("busqueda");
	String anio = 			  "";
	String numero =           "";
	String fechaDesde =       "";
	String fechaHasta =       "";
	String idTurno =          "";
	String idGuardia =        "";
	String tAsistencia =      "";
	String tAsistenciaColegio = "";
	String nif =              "";
	String nombre =           "";
	String apellido1 =        "";
	String apellido2 =        "";
   	ArrayList tAsistenciaA = new ArrayList();
   	ArrayList tAsistenciaColegioA = new ArrayList();
   	ArrayList turnosA = new ArrayList();
   	ArrayList guardiasA = new ArrayList();
	boolean buscar = false;
	String esVolver = (String) ses.getAttribute("esVolver");
	if(busqueda!=null && (esVolver!=null && esVolver.equals("1")))
	{
		anio = 			  (String) busqueda.get("anio");              
		numero =           (String) busqueda.get("numero");            
		fechaDesde =       (String) busqueda.get("fechaDesde");        
		fechaHasta =       (String) busqueda.get("fechaHasta");        
		idTurno =          (String) busqueda.get("idTurno");           
		idGuardia =        (String) busqueda.get("idGuardia");         
		nColegiado =       (String) busqueda.get("nColegiado");        
		tAsistencia =      (String) busqueda.get("tAsistencia");       
		tAsistenciaColegio = (String) busqueda.get("tAsistenciaColegio");
		nif =              (String) busqueda.get("nif");               
		nombre =           (String) busqueda.get("nombre");            
		apellido1 =        (String) busqueda.get("apellido1");         
		apellido2 =        (String) busqueda.get("apellido2");    
		// Preparamos la seleccion de los combos
		tAsistenciaA.add(tAsistencia);
		tAsistenciaColegioA.add(tAsistenciaColegio);
		turnosA.add(idTurno);
		guardiasA.add(idGuardia);
		ses.removeAttribute("esVolver");
	}
	if((esVolver!=null && esVolver.equals("1")))
	{
		ses.removeAttribute("esVolver");
		buscar = true;
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt 
		titulo="gratuita.busquedaAsistencias.literal.titulo" 
		localizacion="gratuita.busquedaAsistencias.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">	
	
	function buscarCliente ()
	{
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
		var colegiado = document.getElementById('nColegiado');
		if (resultado != null && resultado[2]!=null)
		{
			colegiado=resultado[2];
		}
		if(colegiado!=null) 
		{
			document.forms[1].colegiado.value = colegiado;
		}
	}
	
	function refrescarLocal()
	{
		buscar();
	}

	function fLoad()
	{
			var tmp1 = document.getElementsByName("turnos");
			var tmp2 = tmp1[0]; 
			tmp2.onchange();
	}

	</script>
	
	
</head>

<body onLoad="ajusteAlto('resultado');">

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>

	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->	
		<html:form action = "/JGR_AsistenciasLetrado.do" method="POST" target="resultado">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "idTurno" value = ""/>	
		<html:hidden property = "idGuardia" value = ""/>	
	
 	<siga:ConjCampos leyenda="gratuita.busquedaAsistencias.literal.titulo">
	<table width="100%" border="0">

		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.anyo"/>
			</td>
			<td>
			<html:text name="AsistenciasForm" property="anio" size="4" maxlength="4" styleClass="box" value="<%=anio%>"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.numero"/>
			</td>
			<td>
				<html:text name="AsistenciasForm" property="numero" size="10" maxlength="10" styleClass="box" value="<%=numero%>"></html:text>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.fechaAsistencia"/>
				&nbsp;&nbsp;
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.entre"/>&nbsp;<html:text name="AsistenciasForm" property="fechaDesde" size="10" styleClass="box" value="<%=fechaDesde%>" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
				&nbsp;&nbsp;
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.y"/>&nbsp;<html:text name="AsistenciasForm" property="fechaHasta" size="10" styleClass="box" value="<%=fechaHasta%>" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno"/>
			</td>
			<td>
				<siga:ComboBD nombre ="turnos" tipo ="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:scsinscripcionguardia" parametro="<%=dato%>" elementoSel="<%=turnosA%>" />
			</td>	
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia"/>
			</td>
			<td>
				<siga:ComboBD nombre = "scsinscripcionguardia" tipo="guardias" clase="boxCombo" hijo="t" parametro="<%=dato%>"  elementoSel="<%=guardiasA%>" />
			</td>	
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.tipoAsistencia"/>
			</td>
			<td>
				<siga:ComboBD nombre="idTipoAsistencia" tipo="scstipoasistencia" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=tAsistenciaA%>" />
			</td>	
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.ncolegiado"/>
			</td>
			<td colspan="3">
 			<%	if(!usr.isLetrado()) { %>
 

				<!-- RGG - SELECCION DE COLEGIADO -->
				<script language="JavaScript">	
					function buscarCliente () 
					{
						var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
						if (resultado != null && resultado[2]!=null)
						{
							document.getElementById('colegiado').value = resultado[2];
						}
						if (resultado != null && resultado[4]!=null && resultado[5]!=null && resultado[6]!=null)
						{
							document.getElementById('nombreMostrado').value = resultado[4] + " " + resultado[5] + " " + resultado[6];
						}
					}		
				</script>
				<script language="JavaScript">	
					function limpiarCliente () 
					{
						document.getElementById('colegiado').value = "";
						document.getElementById('nombreMostrado').value = "";
					}		
				</script>
				<!-- Si la busqueda se realiza por idPersona, el campo numeroLetrado no puede modificarse, en cambio
					 si la busqueda se realiza mediante el campo numeroLetrado se podría modificar por pantalla sin
					 necesidad de seleccionarlo por el botón -->
				<html:text name="AsistenciasForm" property="colegiado" size="10" maxlength="100" styleClass="boxConsulta" value="<%=nColegiado%>" readOnly="true"></html:text>
				<html:text property="nombreMostrado" size="30" maxlength="150" styleClass="boxConsulta" value="" readOnly="true"></html:text>
				&nbsp;
				<!-- Boton para buscar un Colegiado -->
				<input type="button" class="button" name="buscarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>' onClick="buscarCliente();">
				<!-- Si el campo numeroLetrado es de solo lectuta hace falta este botón para limpiar -->
				&nbsp;<input type="button" class="button" name="limpiarColegiado" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.limpiar"/>' onClick="limpiarCliente();">
				<!-- FIN - RGG - SELECCION DE COLEGIADO -->
 			<% } else { %>
				<html:text name="AsistenciasForm" property="colegiado" size="10" maxlength="100" styleClass="boxConsulta" value="<%=nColegiado%>" readOnly="true"></html:text>
				<html:text property="nombreMostrado" size="30" maxlength="150" styleClass="boxConsulta" value="<%=nombreColegiado%>" readOnly="true"></html:text>
				&nbsp;
			<% } %>
			</td>	


			<td class="labelText" >
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.tipoAsistenciaColegio"/>
			</td>
			<td>
				<siga:ComboBD nombre="idTipoAsistenciaColegio" tipo="scstipoasistenciacolegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=tAsistenciaColegioA%>" />
			</td>	
		</tr>
	</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaAsistencias.literal.asistido">
	<table width="100%">
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.nif"/>
		</td>	
		<td class="labelText">
			<html:text name="AsistenciasForm" property="nif" size="10" maxlength="10" styleClass="box" value="<%=nif%>"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.nombre"/>
		</td>
		<td class="labelText">	
			<html:text name="AsistenciasForm" property="nombre" size="15" maxlength="10" styleClass="box" value="<%=nombre%>" ></html:text>
		</td>	
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1"/>
		</td>
		<td class="labelText">	
			<html:text name="AsistenciasForm" property="apellido1" size="15" maxlength="10" styleClass="box" value="<%=apellido1%>" ></html:text>
		</td>	
		<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2"/>
		</td>
		<td>
			<html:text name="AsistenciasForm" property="apellido2" size="15" maxlength="10" styleClass="box" value="<%=apellido2%>" ></html:text>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
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


	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function errorValidacion(){
				var anio = document.getElementById('anio').value;		
				var numero = document.getElementById('numero').value;
				var colegiado = document.getElementById('colegiado').value;
				var error = false;
				
				if ((anio !="") && isNaN(anio)) {
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
						error = true;
				}
				if (!error && (numero !="") && isNaN(numero)) {
						alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorNumero"/>');
						error = true;
				}
				if (!error && (colegiado !="") && isNaN(colegiado)) {
						alert('<siga:Idioma key="FactSJCS.busquedaRetencionesJ.literal.errorNumerico"/>');
						error = true;
				}

				return error;
		}		

		<!-- Funcion asociada a boton buscar -->
		function buscarOld() 
		{
				if (!errorValidacion()) {
					// obtenemos el idturno y el idguardia
					document.forms[1].idGuardia.value 	= document.forms[1].scsinscripcionguardia.value;
					document.forms[1].idTurno.value		= document.forms[1].turnos.value.substr(document.forms[1].turnos.value.indexOf(",")+1);
					document.forms[1].target			= "resultado";
					document.forms[1].modo.value 		= "buscarPor";
					document.forms[1].submit();
				}else{
					fin();
				}
		}		
		function buscar() 
		{ 
				sub();
				if (!errorValidacion()) {
					// obtenemos el idturno y el idguardia
					document.forms[1].idGuardia.value 	= document.forms[1].scsinscripcionguardia.value;
					document.forms[1].idTurno.value		= document.forms[1].turnos.value.substr(document.forms[1].turnos.value.indexOf(",")+1);
					document.forms[1].target			= "resultado";
					document.forms[1].modo.value 		= "buscarInit";
					document.forms[1].submit();
				}else{
					fin();
				}
		}
		function buscarPaginador() 
		{
		
				if (!errorValidacion()) {
					// obtenemos el idturno y el idguardia
					document.forms[1].idGuardia.value 	= document.forms[1].scsinscripcionguardia.value;
					document.forms[1].idTurno.value		= document.forms[1].turnos.value.substr(document.forms[1].turnos.value.indexOf(",")+1);
					document.forms[1].target			= "resultado";
					document.forms[1].modo.value 		= "buscarPor";
					document.forms[1].submit();
				}
		}
		
		
		
		function nuevo()
		{
			document.forms[1].modo.value	= "nuevo";
			//document.forms[1].target="_blank";
			//document.forms[1].submit();
	   		var resultado = ventaModalGeneral(document.forms[1].name,"M");
	   		if(resultado = "MODIFICADO"){buscar();}
		}
		
		function generarCarta() 
		{
		
			document.forms[1].modo.value	= "generarCarta";
	   		var resultado = ventaModalGeneral(document.forms[1].name,"M");
	   		if(resultado = "MODIFICADO"){
	   			buscar();
	   		}
		}
		
	</script>
	<%
		// para el boton volver
		if(buscar) %><script>buscar();</script><%
	%>

	<siga:ConjBotonesBusqueda botones="C,B,N"  titulo="gratuita.busquedaAsistencias.literal.titulo"/>

	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>