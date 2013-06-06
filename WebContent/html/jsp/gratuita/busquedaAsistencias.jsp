<!-- busquedaAsistencias.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.*"%>

<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	// parametro
	GenParametrosAdm admPar = new GenParametrosAdm(usr);
	String validarVolantes = admPar.getValor(usr.getLocation(),"SCS","VALIDAR_VOLANTE","N");	
	
	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String nombreColegiado =  request.getAttribute("nombreColegiado")==null?"":(String)request.getAttribute("nombreColegiado");

	String[] dato = {usr.getLocation()};
	// Comprobamos si existe busqueda anterior
	Hashtable busqueda = (Hashtable) ses.getAttribute("DATOSFORMULARIO");
	//ses.removeAttribute("DATOSFORMULARIO");
    Hashtable datosBusqueda = (Hashtable) ses.getAttribute("busqueda");
	//ses.removeAttribute("busqueda");
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
	String actuacionValidada =        "";	
	String asunto	=		  "";
	String procedimiento =	  "";
	String nig	=		  	"";
	String juzgado=			  "";
	String comisariaAsi="";
	String tipoActuacion =	  "";
	String comisariaInstitucionAsi="";
	String numeroColegiado = "";
	ArrayList tipoActuacionSel   = new ArrayList();
	ArrayList juzgadoActu   = new ArrayList();
	ArrayList acreditacion   = new ArrayList();	
	
	ArrayList estadoSel    = new ArrayList();
	ArrayList actuacionesPendientesSel = new ArrayList();
	String estado="",actuacionesPendientes="";
    String busquedaRealizada= "";
	
   	ArrayList tAsistenciaA = new ArrayList();
   	ArrayList tAsistenciaColegioA = new ArrayList();
   	ArrayList turnosA = new ArrayList();
   	ArrayList guardiasA = new ArrayList();
	ArrayList comisariaSel = new ArrayList();
	boolean buscar = false;
	String esVolver = (String) ses.getAttribute("esVolver");
	anio = UtilidadesBDAdm.getYearBD("");
    if (busqueda!=null && (busqueda.get("BUSQUEDAREALIZADA")!=null ||!busqueda.get("BUSQUEDAREALIZADA").toString().equals("") )){
		busquedaRealizada = busqueda.get("BUSQUEDAREALIZADA").toString();
	}
	// inc6845 // fechaDesde=UtilidadesBDAdm.getFechaBD("");
	fechaDesde="";
	
	//if((busquedaRealizada!=null) && (!busquedaRealizada.equals("")) ) 
	if(busqueda!=null && (esVolver!=null && esVolver.equals("1")) && datosBusqueda!=null)
	{ 
	  
		comisariaAsi            = (String) busqueda.get(ScsAsistenciasBean.C_COMISARIA);
		comisariaInstitucionAsi = (String) busqueda.get(ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION);
		anio = 			  (String) datosBusqueda.get("anio");              
		numero =           UtilidadesHash.getString(datosBusqueda,"numero");   
		
		         
		fechaDesde =       (String) datosBusqueda.get("fechaDesde");        
		fechaHasta =       (String) datosBusqueda.get("fechaHasta");        
		idTurno =          (String) datosBusqueda.get("idTurno");           
		idGuardia =        (String) datosBusqueda.get("idGuardia");         
		nColegiado =       (String) datosBusqueda.get("nColegiado");    
		tAsistencia =      (String) datosBusqueda.get("tAsistencia");       
		tAsistenciaColegio = (String) datosBusqueda.get("tAsistenciaColegio");
		nif =              (String) datosBusqueda.get("nif");               
		nombre =           (String) datosBusqueda.get("nombre");            
		apellido1 =        (String) datosBusqueda.get("apellido1");         
		apellido2 =        (String) datosBusqueda.get("apellido2"); 
		estado =		   (String) datosBusqueda.get("ESTADO");
		actuacionesPendientes = (String) datosBusqueda.get("actuacionesPendientes");   
		asunto=				(String) datosBusqueda.get("asunto");
		procedimiento=		(String) datosBusqueda.get("procedimiento");
		nig=				(String) datosBusqueda.get("nig");
		juzgado=			(String) datosBusqueda.get("JUZGADO");
		comisariaAsi=		(String) datosBusqueda.get(ScsAsistenciasBean.C_COMISARIA);
		comisariaInstitucionAsi=		(String) datosBusqueda.get(ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION);
		tipoActuacion= 		(String) datosBusqueda.get("tipoActuacion");
		numeroColegiado = (String)datosBusqueda.get("numeroColegiado");
		// Preparamos la seleccion de los combos
		tAsistenciaA.add(tAsistencia);
		tAsistenciaColegioA.add(tAsistenciaColegio);
		turnosA.add(usr.getLocation()+","+idTurno);
		guardiasA.add(idGuardia);
		ses.removeAttribute("esVolver");
	}
	
	String[] parametroTipoActuacion = {usr.getLocation(), tAsistenciaColegio}; 
	
	if (tipoActuacion!=null )
		tipoActuacionSel.add(0,tipoActuacion);
	if (juzgado!=null )
		juzgadoActu.add(0,juzgado);
	if (estado!=null )
		estadoSel.add(0,estado);
	if (actuacionesPendientes!=null )
		actuacionesPendientesSel.add(0,actuacionesPendientes);
	if (comisariaAsi!=null && comisariaInstitucionAsi!=null)
		comisariaSel.add(0,comisariaAsi+","+comisariaInstitucionAsi);
	if((esVolver!=null && esVolver.equals("1"))&&(busqueda!=null))
	{
		ses.removeAttribute("esVolver");
		buscar = true;
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<%=app%>/html/js/validation.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/validacionStruts.js"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt 
		titulo="gratuita.busquedaAsistencias.literal.titulo" 
		localizacion="gratuita.busquedaAsistencias.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">	
	
	function buscarCliente () {
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
		var colegiado = document.getElementById('nColegiado');
		if (resultado != null && resultado[2]!=null) {
			colegiado=resultado[2];
		}
		if(colegiado!=null) {
			document.forms[0].colegiado.value = colegiado;
		}
	}
	
	function refrescarLocal() {
		buscar();
	}

	function fLoad() {
		var tmp1 = document.getElementsByName("turnos");
		var tmp2 = tmp1[0]; 
		tmp2.onchange();
	}
	function inicio(){
		// Ajustamos el numero del colegiado y simulamos la busqueda
		<%if(!nColegiado.equalsIgnoreCase("")){%>
			 document.getElementById("numeroNifTagBusquedaPersonas").value =<%= numeroColegiado%>;
			 obtenerPersonas();
		<%}%>
		// El turno se carga solo pero tenemos que forzar el onChange para cargar sus guardias 
		fLoad();
		// Leemos las actuaciones pendientes para seleccionar en el combo
		<%if(actuacionesPendientes.equalsIgnoreCase("NO")){%>
			document.getElementById("actuacionesPendientes").selectedIndex=1;
		<%}else if(actuacionesPendientes.equalsIgnoreCase("SI")){%>
			document.getElementById("actuacionesPendientes").selectedIndex=2; 
		<%}else if(actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES")){%>
			document.getElementById("actuacionesPendientes").selectedIndex=3;
		<%}else{%>
			document.getElementById("actuacionesPendientes").selectedIndex=0;
		<%}%>
		<%if (busquedaRealizada.equals("1") && (esVolver!=null && esVolver.equals("1"))) {%>
		      buscarPaginador();
		<%}%>
		}

		function obtenerJuzgado() { 
			 if (document.getElementById("codigoExtJuzgadoActu").value!=""){
  				document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
				document.MantenimientoJuzgadoForm.codigoExt2.value=document.getElementById("codigoExtJuzgadoActu").value;
				document.MantenimientoJuzgadoForm.submit();		
			 }
			 else
		 		seleccionComboSiga("juzgado",-1);
		}		
			
		function traspasoDatos(resultado){
			if (resultado[0]==undefined) {
				seleccionComboSiga("juzgado",-1);
				document.getElementById("codigoExtJuzgadoActu").value = "";
			} 
			else
				seleccionComboSiga("juzgado",resultado[0]);
		}
		
		function cambiarJuzgado(comboJuzgado) {
			if(comboJuzgado.value!=""){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
		   			type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado2",
					data: "idCombo="+comboJuzgado.value,
					dataType: "json",
					success: function(json){		
			       		document.getElementById("codigoExtJuzgadoActu").value = json.codigoExt2;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById("codigoExtJuzgadoActu").value = "";
		}			
	</script>
</head>

<body onLoad="ajusteAlto('resultado');inicio();">

	<html:form action = "/JGR_Asistencia.do" method="POST" target="resultado">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property = "modo" value = ""/>	
		<html:hidden property = "idTurno" value = ""/>	
		<html:hidden property = "idGuardia" value = ""/>	
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden name="AsistenciasForm" property="colegiado" value=""/>
	
	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
 	<siga:ConjCampos leyenda="gratuita.busquedaAsistencias.literal.titulo">
	<table width="100%" border="0">

		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.anyo"/> / <siga:Idioma key="gratuita.busquedaAsistencias.literal.numero"/>
			</td>
			<td>
				<html:text name="AsistenciasForm" property="anio"  style="width:40" maxlength="4" styleClass="box" value="<%=anio%>"></html:text> / <html:text name="AsistenciasForm" property="numero" size="10" maxlength="10" styleClass="box" value="<%=numero%>"></html:text>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.fechaAsistencia"/>&nbsp; <siga:Idioma key="gratuita.busquedaAsistencias.literal.entre"/>
			</td>
			<td>
				<siga:Fecha nombreCampo="fechaDesde" valorInicial="<%=fechaDesde%>" />
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.y"/>
			</td>
			<td>	
				<siga:Fecha nombreCampo="fechaHasta" valorInicial="<%=fechaHasta%>" campoCargarFechaDesde="fechaDesde"/>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.turno"/>
			</td>
			<td colspan="3">
				<siga:ComboBD nombre ="turnos" tipo ="turnosDesignacion" clase="boxCombo" ancho="300" obligatorio="false" accion="Hijo:scsinscripcionguardia" parametro="<%=dato%>" elementoSel="<%=turnosA%>" />
			</td>	
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.guardia"/>
			</td>
			<td colspan="3">
				<siga:ComboBD nombre = "scsinscripcionguardia" tipo="guardias" clase="boxCombo" hijo="t" parametro="<%=dato%>"  elementoSel="<%=guardiasA%>" ancho="200"/>
			</td>	
		</tr>
		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
			</td>
			<td colspan="3">	
				<siga:ComboBD nombre="estado" tipo="cmbEstadosAsistencia" obligatorio="false" accion="" elementoSel="<%=estadoSel%>" clase="boxCombo" obligatorioSinTextoSeleccionar="no"/>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.actuacionesValidadas"/>
			</td>	
			<td colspan="3">	
				<Select name="actuacionesPendientes" id="actuacionesPendientes" class="boxCombo">
						<option value=''  selected ></option>
						<option value='No' ><siga:Idioma key="general.no"/></option>
						<option value='Si' ><siga:Idioma key="general.yes"/></option>
						<option value='SinActuaciones' ><siga:Idioma key="gratuita.busquedaAsistencias.literal.sinActuaciones"/></option>
				</Select>
			</td>
		</tr>
		<tr >
			<td class="labelText" >
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.tipoAsistenciaColegio"/>
			</td>
			<td colspan="7">
				<siga:ComboBD ancho="660" nombre="idTipoAsistenciaColegio" tipo="scstipoasistenciacolegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=tAsistenciaColegioA%>" />
			</td>	
		</tr>		
		<tr><td colspan="8">
			<siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="colegiado">
			</siga:BusquedaPersona>
		</td></tr>
		<tr style="display: none">

			<td class="labelText">
				<siga:Idioma key="gratuita.busquedaAsistencias.literal.tipoAsistencia"/>
			</td>
			<td  colspan="7">
				<siga:ComboBD  ancho="660" nombre="idTipoAsistencia" tipo="scstipoasistencia" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=tAsistenciaA%>" />
			</td>	
		</tr>

	</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaAsistencias.literal.asistido" desplegable="true" oculto="true">
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
			<html:text name="AsistenciasForm" property="nombre" size="15" maxlength="100" styleClass="box" value="<%=nombre%>" ></html:text>
		</td>	
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1"/>
		</td>
		<td class="labelText">	
			<html:text name="AsistenciasForm" property="apellido1" size="15" maxlength="100" styleClass="box" value="<%=apellido1%>" ></html:text>
		</td>	
		<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2"/>
		</td>
		<td>
			<html:text name="AsistenciasForm" property="apellido2" size="15" maxlength="100" styleClass="box" value="<%=apellido2%>" ></html:text>
		</td>
	</tr>
	</table>
	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="pestana.justiciagratuitadesigna.actuaciones" desplegable="true" oculto="true">
		<table  border="0" align="center" width="100%">
			<tr>
				<td class="labelText" colspan="1">	
					<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.comisaria"/>
				</td>	
				<td class="labelText" colspan="3">
					<siga:ComboBD nombre="comisaria" tipo="comboComisarias" ancho="740" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=comisariaSel%>" clase="boxCombo"/>
				</td>
			</tr>
			<tr>
				<td class="labelText" colspan="1">
					<siga:Idioma key="informes.cartaOficio.tipoActuacionLista"/>
				</td>
				<td class="labelText" colspan="3">
				<siga:ComboBD ancho="740" nombre="tipoActuacion" tipo="comboTipoActuacionesGenerico" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorioSinTextoSeleccionar="no" elementoSel="<%=tipoActuacionSel%>" parametro="<%=parametroTipoActuacion%>" />
				</td>
			</tr>
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado"/>
				</td>
				<td class="labelText" colspan="3">
					<input type="text" name="codigoExtJuzgadoActu" class="box" size="5"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />
					<siga:ComboBD nombre="juzgado" tipo="comboJuzgados" ancho="680" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  hijo="t" elementoSel="<%=juzgadoActu%>" parametro="<%=dato%>" accion="parent.cambiarJuzgado(this);" />           	   
				</td>
			</tr>
			<tr>
				<table border="0" align="center" width="100%">
					<td class="labelText" width="125">
						<siga:Idioma key="informes.cartaAsistencia.procedimiento" />
					</td>
					<td class="labelText">
						<html:text name="AsistenciasForm" property="procedimiento" size="14" maxlength="100" styleClass="box" value="<%=procedimiento%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="informes.cartaAsistencia.asunto" />
					</td>
					<td class="labelText">
						<html:text name="AsistenciasForm" property="asunto" size="20" maxlength="40" styleClass="box" value="<%=asunto%>"></html:text>
					</td>
					<td class="labelText" >	
						<siga:Idioma key="gratuita.mantAsistencias.literal.NIG"/>
					</td>	
					<td class="labelText" >
						<html:text name="AsistenciasForm" property="nig2" size="15" maxlength="50" styleClass="box" value="<%=nig%>"></html:text>
					</td>	
				</table>			
			</tr>
		</table>
	</siga:ConjCampos>	
	
	
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>

	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->
<% if (validarVolantes.equals("S")) { %>
		<siga:ConjBotonesBusqueda botones="VOL,C,B,N"  titulo="gratuita.busquedaAsistencias.literal.titulo"/>
<% } else { %>
		<siga:ConjBotonesBusqueda botones="C,B,N"  titulo="gratuita.busquedaAsistencias.literal.titulo"/>
<% } %>

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
		function buscar() 
		{ 

			document.forms[0].asunto.value = trim(document.forms[0].asunto.value);
			document.forms[0].numero.value = trim(document.forms[0].numero.value);

			if ( !validarObjetoAnio(document.getElementById("anio")) ){
				alert("<siga:Idioma key="fecha.error.anio"/>");
				return false;
			}

			if((validarFecha(document.forms[0].fechaDesde.value))&&
				(validarFecha(document.forms[0].fechaHasta.value))){
				sub();
				if (!errorValidacion()) {
					// obtenemos el idturno y el idguardia
					document.forms[0].idGuardia.value 	= document.forms[0].scsinscripcionguardia.value;
					document.forms[0].idTurno.value		= document.forms[0].turnos.value.substr(document.forms[0].turnos.value.indexOf(",")+1);
					document.forms[0].target			= "resultado";
					document.forms[0].modo.value 		= "buscarInit";
					document.forms[0].submit();
				}else{
					fin();
				}
			}else{
				setFocusFormularios();
			}
		}
		function buscarPaginador() 
		{
		
				if (!errorValidacion()) {
					// obtenemos el idturno y el idguardia
					document.forms[0].idGuardia.value 	= document.forms[0].scsinscripcionguardia.value;
					document.forms[0].idTurno.value		= document.forms[0].turnos.value.substr(document.forms[0].turnos.value.indexOf(",")+1);
					document.forms[0].target			= "resultado";
					document.forms[0].modo.value 		= "buscarPor";
					document.forms[0].submit();
				}
		}				
		
		function nuevo()
		{
			document.forms[0].modo.value = "nuevo";
	   		var resultado = ventaModalGeneral(document.forms[1].name,"M");
	   		if(resultado == "MODIFICADO") {
				document.forms[0].anio.value   = "";
				document.forms[0].numero.value = "";
				document.forms[0].modo.value   = "editar";
				document.forms[0].target       = "mainWorkArea";
				document.forms[0].submit();
	   		}
		}
		
		function generarCarta() 
		{
			document.forms[0].modo.value	= "generarCarta";
	   		var resultado = ventaModalGeneral(document.forms[0].name,"M");
	   		if(resultado = "MODIFICADO"){
	   			buscar();
	   		}
		}		
		function validarVolante() 
		{
	   		var resultado = ventaModalGeneral(document.ValidarVolantesGuardiasForm.name,"G");
		}
		
	</script>
	<%
		// para el boton volver
		if(buscar) %><script>buscarPaginador();</script>
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<!-- FIN: BOTONES BUSQUEDA -->

	<html:form action = "/JGR_ValidarVolantesGuardias.do" method="POST" target="resultado">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrir">
	</html:form>
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>