<!-- modalDefinirDesignas.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gratuita.form.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String letradoSeleccionado = (String) request.getAttribute("letradoSeleccionado");
	String hayDatos = (String)request.getAttribute("hayDatos");
	String numProcedimiento = (String)request.getAttribute("numProcedimiento");
	
	
	BuscarDesignasForm miform = (BuscarDesignasForm) ses.getAttribute("BuscarDesignasForm");
	String desdeEJG = miform.getDesdeEjg();

	String anioAsistencia = (String)request.getAttribute("anioAsistencia");
	String anioEJG        = (String)request.getAttribute("anioEJG");
	String numeroEjg        = (String)ses.getAttribute("numeroEjg");
	String idTipoEjg        = (String)ses.getAttribute("idTipoEjg");
	String fecha = UtilidadesBDAdm.getFechaBD("");

	// Si venimos desde Asistencia -----------------------------
	String tipoDesignaAsistencia = (String)request.getAttribute("tipoDesignaAsistencia");
	ArrayList elementoSelTipoDesigna = new ArrayList();
	if (tipoDesignaAsistencia != null && !tipoDesignaAsistencia.equals("")) {
		elementoSelTipoDesigna.add(tipoDesignaAsistencia);
	}

	String idPersonaAsi = (String)request.getAttribute("idTurnoAsistencia");	
	String idTurnoAsistencia = (String)request.getAttribute("idTurnoAsistencia");
	String idTurnoEJG        = (String)request.getAttribute("turnoEJG");

	String[] dato = {usr.getLocation()};
	ArrayList elementoSelTurno = new ArrayList();
	String claseComboTurno = "boxCombo";
	String sreadonly="false";
	if (idTurnoAsistencia != null && !idTurnoAsistencia.equals("")) {// Cuando venimos de Asistencias
		elementoSelTurno.add(idTurnoAsistencia);
//		claseComboTurno = "boxConsulta";
//		sreadonly="true";
	}
    else if (idTurnoEJG != null && !idTurnoEJG.equals("")) {// cuando venimos de EJG
		elementoSelTurno.add(idTurnoEJG);
	}
	
	String[] datoJuzgado = new String[2];
	try {
		datoJuzgado[0] = usr.getLocation();
		if (idTurnoAsistencia!=null){
		 datoJuzgado[1] = (idTurnoAsistencia.split(","))[1];
		}else{
		  if (idTurnoEJG!=null){
		     datoJuzgado[1] = (idTurnoEJG.split(","))[1];
		  }else{
		    datoJuzgado[1]="";
		  }	 
		 
		}
	}
	catch(Exception e) {}
	
	String juzgadoAsistencia = (String)request.getAttribute("juzgadoAsistencia");
	String juzgadoEJG = (String)request.getAttribute("idjuzgadoEJG");
	
	ArrayList elementoSelJuzgado = new ArrayList();
	
	if (juzgadoAsistencia != null && !juzgadoAsistencia.equals("")) {
		elementoSelJuzgado.add(juzgadoAsistencia);
		
	}
	
	ArrayList elementoSelSolicitante = new ArrayList();
	elementoSelSolicitante.add("-1");
	
	
	
	String diligenciaAsi = (String)request.getAttribute("diligencia");
	String procedimientoAsi = (String)request.getAttribute("procedimiento");
	String comisariaAsi = (String)request.getAttribute("comisaria");
	
	
	if (juzgadoEJG != null && !juzgadoEJG.equals("")) {
		elementoSelJuzgado.add(juzgadoEJG);
		
	}
	String[] datoSolicitante = new String[5];
	datoSolicitante[0] = usr.getLocation();
	datoSolicitante[1] = usr.getLocation();
	datoSolicitante[2] = anioEJG;
	datoSolicitante[3] = numeroEjg;
	datoSolicitante[4] = idTipoEjg;
	

	String idPersona				 = (String)request.getAttribute("idPersona");
	String nColegiadoAsistencia      = (String)request.getAttribute("nColegiadoAsistencia");
	String nombreColegiadoAsistencia = (String)request.getAttribute("nombreColegiadoAsistencia");
	// -----------------------------------------------------------	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
	<script type="text/javascript" src="<%=app%>/html/js/SIGA.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery-ui.js'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/js/calendarJs.jsp"></script>	

	<html:javascript formName="BuscarDesignasForm" staticJavascript="false" />
	<script type="text/javascript" src="<%=app%>/html/js/validacionStruts.js"></script> 	
</head>

	<script language="JavaScript">	

		function buscarCliente () 
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			var colegiado = document.getElementById('ncolegiado');
			var colegioOrigen = document.getElementById('colegioOrigen');			
			var manual = document.getElementById('manual');
			var idPersona = document.getElementById('idPersona');
			manual.value="0";
			if (resultado != null && resultado[2]!=null)
			{
				colegiado.value=resultado[2];
				idPersona.value=resultado[0];
				colegioOrigen.value=resultado[4];
			}
		}
		
		/* function buscarClienteAutomatica ()
		{
			if(document.forms[1].idTurno.selectedIndex > 0){
				document.forms[1].target="submitArea";
				document.forms[1].modo.value="abrirAvanzada";
				document.forms[1].submit();
			}else{
				alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1' />");
			}
		}*/ 
	
		function cargarCliente ()
		{
			<% if (nColegiadoAsistencia != null && !nColegiadoAsistencia.equals("")) { %>
				document.forms[1].ncolegiado.value="<%=nColegiadoAsistencia%>";
			//document.forms[1].colegiadoSJCS.value="< %=nColegiadoAsistencia%>";
				document.forms[1].nomColegiado.value="<%=nombreColegiadoAsistencia%>";
			<% } %>
			
//			document.getElementById("idTurno").onchange(); 
		}
		function refrescarLocal() 
		{		
			fin();
		}
		
	   function obtenerJuzgado() { 
			if (document.forms[1].codigoExtJuzgado.value!=""){	
 				document.MantenimientoJuzgadoForm.codigoExt2.value=document.forms[1].codigoExtJuzgado.value;
				document.MantenimientoJuzgadoForm.submit();	
			 }
		}
		
		function traspasoDatos(resultado) {
			seleccionComboSiga("juzgado",resultado[0]);
		}
		
	function cambiarJuzgado(comboJuzgado) {
		if(comboJuzgado.value!=""){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
	   			type: "POST",
				url: "/SIGA/JGR_MantenimientoJuzgados.do?modo=getAjaxJuzgado2",
				data: "idCombo="+comboJuzgado.value,
				dataType: "json",
				success: function(json){		
		       		document.getElementById("codigoExtJuzgado").value = json.codigoExt2;      		
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
	}		
	</script>

<body onload="cargarCliente()">

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulos">
			<siga:Idioma key="gratuita.busquedaDesignas.literal.nuevaDesigna"/>
		</td>
	</tr>
</table>

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>

<!-- INICIO --> 
<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposMedia"  align="center" border="0" width="100%">

	<html:javascript formName="BuscarDesignasForm" staticJavascript="false" />
	<html:form action="/JGR_Designas.do" method="POST" target="submitArea" >
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden property = "idPersona" value="<%=idPersona %>" />
	<html:hidden property = "manual" value = "0"/>
	
	<html:hidden property ="anioAsistencia" value = "<%=anioAsistencia%>"/>
	<html:hidden property ="anioEjg" value = "<%=anioEJG%>"/>
	<html:hidden property ="numProcedimiento" value = "<%=numProcedimiento%>"/>
	
	<html:hidden property = "diligencia"     value="<%=diligenciaAsi%>"/>
	<html:hidden property = "procedimiento"     value="<%=procedimientoAsi%>"/>
	<html:hidden property = "comisaria"     value="<%=comisariaAsi%>"/>
   <tr>
		<td class="labelText"  width="25%">
			<siga:Idioma key="gratuita.busquedaDesignas.literal.fechaApertura"/>&nbsp;(*)
		</td>
		<td class="labelText"  >
			<siga:Fecha nombreCampo="fechaAperturaInicio"   valorInicial="<%=fecha%>" posicionX="400" posicionY="10"/>				
		</td>
	</tr>
		
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaDesignas.literal.tipoDesigna"/>
		</td>
		<td class="labelText" >	
			<siga:ComboBD nombre="tipoDesigna" tipo="tipoDesignaColegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=elementoSelTipoDesigna%>" />
		</td>	
	</tr>	
	<%if (anioEJG!=null && !anioEJG.equals("")){%>
	<tr>
		<td class="labelText">
  		 <siga:Idioma key="gratuita.busquedaEJG.literal.solicitante"/>&nbsp;(*)
		</td>
		<td class="labelText">		
		 <siga:ComboBD nombre = "idSolicitante" tipo="cmbSolicitante" clase="boxCombo" ancho="500" obligatorioSinTextoSeleccionar="true" parametro="<%=datoSolicitante%>" elementoSel="<%=elementoSelSolicitante%>"/>		
		</td>
		
	</tr>
	<%}%>
	
	<tr>
		<td colspan="2">	
		<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.titulo"> 
		<table border="0" width="100%">
			<tr>
				<td class="labelText" width="20%">
					<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>&nbsp;(*)
				</td>
				<td class="labelText" colspan="3">
					<siga:ComboBD nombre="idTurno" tipo="turnosDesignacionAlta" estilo="true" ancho="480" clase="<%=claseComboTurno%>" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=elementoSelTurno%>" accion="Hijo:juzgado" readOnly="<%=sreadonly%>" ancho="500"/>
				</td>
			</tr>
			<tr>

		   <td class="labelText" colspan="4">	
<!-- Busqueda automatica de juzgados-->
		<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">
			 <table width="100%" >
			   	<tr>
					<td class="labelText" width="10%">	
					   <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
					</td>	 
					<td class="labelText" width="10%" >	
					   <input type="text" name="codigoExtJuzgado" class="box" size="8" maxlength="10" onChange="obtenerJuzgado();"/>&nbsp;
					</td>	 
					<td> &nbsp;</td>	
					<td>
					   <siga:ComboBD nombre="juzgado" tipo="comboJuzgadosTurno" ancho="460" estilo="true"  clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  parametro="<%=datoJuzgado%>" elementoSel="<%=elementoSelJuzgado%>" hijo="t" accion="parent.cambiarJuzgado(this);"/>
					</td>   
				</tr>
			</table>
		</siga:ConjCampos> 
<!------------------>
		
		</td>
	</tr>
	</table>
	<table border="0" width="100%">
		<tr>
			<td colspan="5">
				<html:hidden property="flagSalto" value=""></html:hidden>
				<html:hidden property="flagCompensacion" value=""></html:hidden>
					<siga:BusquedaSJCS	propiedad="seleccionLetrado" botones="M,D"
						concepto="Designacion" operacion="Asignacion" nombre="BuscarDesignasForm" campoTurno="idTurno" campoFecha="fechaAperturaInicio"
						campoPersona="idPersona" campoColegiado="ncolegiado" campoNombreColegiado="nomColegiado" mostrarNColegiado="true" campoColegio="colegioOrigen" 
						mostrarNombreColegiado="true" campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" modo="nuevo"
					/>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
			</td>		
			<td>
				<input type="text" name="ncolegiado" class="boxConsulta" readOnly value="" style="width:'100px';">
				<html:hidden property="colegioOrigen" value=""></html:hidden>

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
	<tr>
		<td class="labelText" colspan="3">	
			<siga:Idioma key="gratuita.designa.designacionAutomatica"/>
		</td>		
	</tr>
<!--
	<tr>
		<td class="labelText">	
			<siga:Idioma key="gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado"/>
		</td>
		<td class="labelText">
				<html:text name="BuscarDesignasForm" property="ncolegiado" size="10" maxlength="10" styleClass="box" value=""></html:text>
		</td>
		<td class="labelText">	
			<input type="button" class="button" name="buscarColegiado" value="Buscar Colegiado" onClick="buscarCliente();"> 
		</td>	
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;
		</td>
		<td class="labelText">	
			<input type="button" class="button" name="seleccionAutomatica" value="Selección Automática" onClick="buscarClienteAutomatica();"> 
		</td>	
	</tr>
	<tr>
		<td colspan="3">
			&nbsp;
		</td>
	</tr>
-->	

	</html:form>
	
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
	</html:form>	
	
	</table>

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="Y,R,C" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	 
			sub();
			if(document.forms[1].colegioOrigen.value==0)
				document.forms[1].colegioOrigen.value="";
			if((document.forms[1].idTurno.selectedIndex < 1)//||(document.forms[1].ncolegiado.value.length < 1)
			   ||(document.forms[1].fechaAperturaInicio.value.length < 1)||(document.forms[1].idSolicitante && document.forms[1].idSolicitante.value=="")){
				alert("Debe rellenar todos los campos");
				fin();
				return false;
			}else{
				
				if (document.forms[1].ncolegiado.value || confirm("<siga:Idioma key='messages.designa.confirmacion.seleccionAutomaticaLetrado' />")){
					document.forms[1].modo.value="insertar";
					document.forms[1].submit();
				}else{
					fin();
					return false;
				}				
			}
		}

				
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Funcion asociada a boton buscar -->

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
