<!-- nuevaAsistencia.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String mensaje = "";

	String nColegiado = request.getAttribute("nColegiado") == null ? "": (String) request.getAttribute("nColegiado");
	String[] dato = { usr.getLocation() };
	String[] datoCom = { usr.getLocation(),"-1"};
	String[] datoJuzg 	= {usr.getLocation(),"-1"};

	AsistenciasForm miForm = (AsistenciasForm) request.getAttribute("miForm");
	ArrayList idTurno = new ArrayList();
	ArrayList idGuardia = new ArrayList();
	ArrayList tAsistencia = new ArrayList();
	ArrayList tAsistenciaColegio = new ArrayList();
	String nTipo = request.getAttribute("tipoAsistencia") == null ? ""
			: (String) request.getAttribute("tipoAsistencia");
	tAsistencia.add(nTipo);

	String fecha = UtilidadesBDAdm.getFechaBD("");

	boolean bEsFichaColegial = request.getParameter("esFichaColegial") != null
			&& ((String) request.getParameter("esFichaColegial"))
					.equals("1") ? true : false;
	boolean bEsClonacion = request.getParameter("esFichaColegial") != null
			&& ((String) request.getParameter("esFichaColegial"))
					.equals("2") ? true : false;
	
	// Datos para precargar
	String turno = request.getParameter("idTurno")!=null?(String)request.getParameter("idTurno"):"";
	String guardia = request.getParameter("idGuardia")!=null?(String)request.getParameter("idGuardia"):"";
	String juzgado = request.getParameter("juzgado")!=null?(String)request.getParameter("juzgado"):"";
	String comisaria = request.getParameter("comisaria")!=null?(String)request.getParameter("comisaria"):"";
	String numeroColegiado = request.getParameter("numeroColegiado")!=null?(String)request.getParameter("numeroColegiado"):"";
	String nombreColegiado = request.getParameter("nombreColegiado")!=null?(String)request.getParameter("nombreColegiado"):"";
	String tipoAsistenciaColegio = request.getParameter("idTipoAsistenciaColegio")!=null?(String)request.getParameter("idTipoAsistenciaColegio"):"";
	String idPersona = request.getParameter("idPersona")!=null?(String)request.getParameter("idPersona"):"";
	fecha = request.getParameter("fechaHora")!=null?(String)request.getParameter("fechaHora"):fecha;

	
	ArrayList comisariaSel = new ArrayList();
	ArrayList juzgadoSel = new ArrayList();
	if (comisaria!=null && !comisaria.equalsIgnoreCase("")){
		comisariaSel.add(0,comisaria);
	}
	
	
	
	if (juzgado!=null && !juzgado.equalsIgnoreCase("")) {
		juzgadoSel.add(0,juzgado);
	}
	if(bEsClonacion){
		if (turno!=null && !turno.equalsIgnoreCase("")) idTurno.add(0,usr.getLocation()+","+turno);
		if (guardia!=null && !guardia.equalsIgnoreCase("")) idGuardia.add(0,usr.getLocation()+","+guardia);
		if (tipoAsistenciaColegio!=null && !tipoAsistenciaColegio.equalsIgnoreCase("")) tAsistenciaColegio.add(0,tipoAsistenciaColegio);
	}
%>
<!-- JSP -->
<script>

	var contador=0;

	function fLoad()
	{
		if("<%=request.getAttribute("mensaje")%>"!="null")
		{
		<% mensaje = (String)request.getAttribute("mensaje"); %>
			alert("<%=mensaje%>");
		}
		<%if(bEsClonacion){%>
			var tmp1 = document.getElementsByName("turnos");
			var tmp2 = tmp1[0]; 
			tmp2.onchange();
			jQuery('#turnos').change();
		<%}%>
	}

	function cargarColegiado()
	{
		<%if(bEsClonacion){%>
			document.getElementById("nomColegiado").value="<%=nombreColegiado%>";
			document.getElementById("colegiado").value="<%=numeroColegiado%>";
			document.getElementById("idPersona").value="<%=idPersona%>";
		<%}%>
		
	}

</script>
<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
</head>

<body onload="cargarColegiado();">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.nuevaAsistencia.literal.titulo"/>
		</td>
	</tr>
	</table>

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action = "/JGR_MantenimientoAsistencia.do" method="POST">
	
	<input type="hidden" name="actionModal" value="">
	<html:hidden property = "modo" value="buscar"/>	
	<html:hidden property = "idTurno" />	
	<html:hidden property = "idGuardia" />
	<html:hidden property = "idPersona" />
	<input type="hidden" name = "flagSalto" value=""/>
	<input type="hidden" name = "flagCompensacion" value=""/>
	<input type="hidden" name = "checkSalto" value=""/>
	<input type="hidden" name = "checkCompensacion" value=""/>	
	<input type="hidden" name = "esFichaColegial" value="<%=bEsFichaColegial%>"/>
		

	<table  class="tablaCentralCamposMedia"  align="center" border="0">
	<tr>
		<td width="30%"></td>
		<td width="20%"></td>
		<td width="10%"></td>
		<td width="40%"></td>
		
	</tr>
	
		<tr>
			<td class="labelText" width="220">
				<siga:Idioma key="gratuita.nuevaAsistencia.literal.turno"/>&nbsp;(*)
			</td>	
			<td colspan="3" width="480">
				<%if (bEsFichaColegial) {%>
					<siga:ComboBD nombre="turnos" tipo="turnosLetradoAsistencia" clase="boxCombo"  ancho="480"  obligatorio="false" accion="Hijo:guardias;"  parametro="<%=dato%>" ElementoSel="<%=idTurno%>" />
				<%} else {%>
					<siga:ComboBD nombre="turnos" tipo="turnosAsistencia" clase="boxCombo" obligatorio="false"  ancho="480" accion="Hijo:guardias;"  parametro="<%=dato%>" elementoSel="<%=idTurno%>" />
				<%}%>
			</td>	
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.nuevaAsistencia.literal.guardia"/>&nbsp;(*)
			</td>	
			<td colspan="3">
				<siga:ComboBD nombre="guardias" tipo="guardias" clase="boxCombo" hijo="t" ancho="480"  accion="parent.rellenarComboLetrado();" parametro="<%=dato%>" elementoSel="<%=idGuardia%>" />
			</td>	
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key='gratuita.nuevaAsistencia.literal.tasistenciacolegio'/>&nbsp;(*)
			</td>	
			<td colspan="3">
				<siga:ComboBD nombre="idTipoAsistenciaColegio" tipo="scstipoasistenciacolegio" estilo="true"  clase="boxCombo" ancho="480" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=tAsistenciaColegio%>" />
			</td>	
		</tr>
		

<%if(bEsClonacion){%>
		<tr>
			<td class="labelText" style="vertical-align:text-top;text-align: left">
				<siga:Idioma key="gratuita.mantAsistencias.literal.centroDetencion"/>
			</td>
			<td colspan="3">
				<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" clase="boxCombo" ancho="480" obligatorio="false" parametro="<%=datoCom%>" elementoSel="<%=comisariaSel%>"/>
			</td>
		</tr>
		<tr>
			<td class="labelText" style="vertical-align:text-top;text-align: left">
				<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
			</td>
			<td colspan="3">
				<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosTurno" clase="boxCombo" ancho="480" obligatorio="false" parametro="<%=datoJuzg%>" elementoSel="<%=juzgadoSel%>"/>
			</td>
		</tr>
<%}%>
		
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.busquedaAsistencias.literal.fechaAsistencia'/>&nbsp;(*)
			</td>	
			<td >
			 <siga:Datepicker nombreCampo="fechaHora" valorInicial="<%=fecha%>" postFunction="rellenarComboLetrado();"></siga:Datepicker>
			</td>
			<td   class="labelText">
				<siga:Idioma key='gratuita.nuevaAsistencia.literal.hora'/>&nbsp;
			</td>
			<td>
				<html:text property="horaAsistencia" size="2" maxlength="2" styleClass="box" value="" style="text-align:center"></html:text>:<html:text 
					property="minutoAsistencia"  size="2" maxlength="2" styleClass="box" value="" style="text-align:center"></html:text>
				
			</td>
		</tr>
		
		<tr style="display:none">
			<td class="labelText">
				<siga:Idioma key='gratuita.nuevaAsistencia.literal.tasistencia'/>&nbsp;(*)
			</td>	
			<td colspan="3">
				<siga:ComboBD nombre="idTipoAsistencia" tipo="scstipoasistencia" estilo="true" clase="boxCombo" ancho="480" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  ElementoSel="<%=tAsistencia%>" />
			</td>	
		</tr>
<%if (bEsFichaColegial) {%>
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.nuevaAsistencia.literal.ncolegiado"/>
			</td>	
			<td colspan="3">
				<html:text name="AsistenciasForm" property="colegiado" size="10" maxlength="10" styleClass="boxConsulta" value="<%=nColegiado%>" readOnly="true"></html:text>			
			</td>
		</tr>
<%} else {%>
		<tr>
			<!--<html:hidden name="AsistenciasForm" property="colegiado" value="< %=nColegiado%>" ></html:hidden>			-->
			<td colspan="4" width="700">
				<siga:ConjCampos
					leyenda="gratuita.seleccionColegiadoJG.literal.titulo">
					<table width="100%" border="0">
						<tr>
							<td colspan="5"><siga:BusquedaSJCS nombre="AsistenciasForm"
								propiedad="buscaLetrado" concepto="asistencia"
								operacion="Asignacion" campoTurno="turnos"
								campoGuardia="guardias" campoFecha="fechaHora"
								campoPersona="idPersona" campoColegiado="colegiado"
								campoFlagSalto="flagSalto"
								campoFlagCompensacion="flagCompensacion" campoSalto="salto"
								campoCompensacion="compensacion"
								campoNombreColegiado="nomColegiado" diaGuardia="true"
								modo="editar" />
							</td>
						</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key='gratuita.nuevaAsistencia.literal.ncolegiado' />
						</td>
						<td>
							<input type="text" name="colegiado" class="boxConsulta" readOnly value="<%=numeroColegiado %>" style="width:'100px';">
						</td>
						<td class="labelText">
							<siga:Idioma key='censo.colegiacion.colegiado' />
						</td>
						<td colspan="2">
							<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="<%=nombreColegiado %>" style="width:'240px';">
						</td>
					</tr>
				</table>
				</siga:ConjCampos>
			</td>
		</tr>
<%}%>
		
	</table>
	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">


		function rellenarComboLetrado(){
			//Cuando es de ficha colegial no crea las funciones javascript que si genera el tag siga:BusquedaSJCS
			fin();
			<%if(bEsFichaColegial){%>
				return true;
			<%}else if(bEsClonacion){%>
				if (contador<=0){
					contador++;
					return true;
				}else{
					rellenarComboGuardia();
				}
			<%}else{%>
				rellenarComboGuardia();
			<%}%>
		}
		// Funcion asociada a boton limpiar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
		function refrescarLocal() 
		{		
			fin();
		}
		//Funcion asociada a boton Guardar -->
		function accionGuardarCerrar() 
		{	
		
			sub();	
			if(document.forms[1].turnos.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
				fin();
				return false;
			}
			if(document.forms[1].guardias.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert4' />");
				fin();
				return false;
			}
			/* jbd Tras la incidencia 6502 el tipo asistencia desaparece y el tipo asistencia colegio pasa a ser obligatorio
			if(document.forms[1].idTipoAsistencia.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert6' />");
				fin();
				return false;
			}*/
			if(document.forms[1].idTipoAsistenciaColegio.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert8' />");
				fin();
				return false;
			}
			if(document.forms[1].fechaHora.value == "")
			{
				alert("<siga:Idioma key='gratuita.busquedaAsistencias.literal.fechaAsistencia' />"+" "+"<siga:Idioma key='messages.campoObligatorio.error' />");
				fin();
				return false;
			}
			//Para la validacion no tengo en cuenta si empieza por 0 y tiene 2 digitos (tanto hora como minuto)
			var horas = trim(document.forms[1].horaAsistencia.value);
			var minutos = trim(document.forms[1].minutoAsistencia.value);
			
			

			if (horas.length==1) {
				document.forms[1].horaAsistencia.value = "0" + horas;
			}
			if (minutos.length==1) {
				document.forms[1].minutoAsistencia.value = "0" + minutos;
			}
			if (horas!="" && (horas>23 || horas<0)) {
				alert("<siga:Idioma key='messages.general.error.hora'/>");
				fin();
				return false;
			}
			if (minutos!="" && (minutos>59 || minutos<0)) {
				alert("<siga:Idioma key='messages.general.error.hora'/>");
				fin();
				return false;
			}
			valor = trim(document.forms[1].horaAsistencia.value);
            if (valor!="" && !isNumero(valor)) {
            	alert ("<siga:Idioma key='messages.general.error.hora'/>");
            	fin();
            	return false;
			}
			valor = trim(document.forms[1].minutoAsistencia.value);
            if (valor!="" && !isNumero(valor)) {
            	alert ("<siga:Idioma key='messages.general.error.hora'/>");
            	fin();
            	return false;
			}
			
			
			
			
			
			
			if(document.forms[1].colegiado.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert7' />");
				fin();
				return false;
			}
			
			if(<%=bEsFichaColegial%>==false&&<%=bEsClonacion%>==false)
			{
				if(document.forms[1].salto.checked)
					document.forms[1].checkSalto.value ="1";
				else
					document.forms[1].checkSalto.value ="0";
				if(document.forms[1].compensacion.checked)
					document.forms[1].checkCompensacion.value ="1";
				else
					document.forms[1].checkCompensacion.value ="0";	
				
				document.forms[1].action="JGR_MantenimientoAsistencia.do";
			}
			
			else
			{
				document.forms[1].action="JGR_MantenimientoAsistenciaLetrado.do";
			}
			
			document.forms[1].idTurno.value = document.forms[1].turnos.value;
			document.forms[1].idGuardia.value = document.forms[1].guardias.value;
			document.forms[1].modo.value = "insertar";
			document.forms[1].target = "submitArea";
			
			document.forms[1].submit();

			return true;
		}

		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			var colegiado = document.getElementById('nColegiado');
			if (resultado != null && resultado[2]!=null)
			{
				colegiado=resultado[2];
			}
			document.forms[1].colegiado.value = colegiado;
		}
		
		function abrirGuardias ()
		{
			document.forms[1].idTurno.value 	= document.forms[1].turnos.value;
			document.forms[1].idGuardia.value 	= document.forms[1].guardias.value;
			document.forms[1].modo.value		= "buscarPor";
			if(document.forms[1].turnos.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
				return false;
			}
			if(document.forms[1].guardias.value == "")
			{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert4' />");
				return false;
			}
			var resultado = ventaModalGeneral(document.forms[1].name,"M");
			if (resultado != null && resultado[0]!=null)
				document.forms[1].colegiado.value = resultado[0]; 
			else
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert5' />");
			return true;
		}

		fLoad();

		cargarColegiado();
		

	</script>
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesAccion botones="y,c" modal="M"  />	
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>