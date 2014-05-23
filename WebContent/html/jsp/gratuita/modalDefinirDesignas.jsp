<!DOCTYPE html>
<html>
<head>
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
	
	String letradoSeleccionado = (String) request.getAttribute("letradoSeleccionado");
	String hayDatos = (String)request.getAttribute("hayDatos");
	String numProcedimiento = (String)request.getAttribute("numProcedimiento");
	String anioProcedimiento ="";
	int ejisActivo = 0;
	if (request.getAttribute("EJIS_ACTIVO")!=null){
		ejisActivo = Integer.parseInt(request.getAttribute("EJIS_ACTIVO").toString());
	}	
	if (ejisActivo>0){
		anioProcedimiento = (String)request.getAttribute("anioProcedimiento");
	}
	
	
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
	String idTurnoSeleccionado = "-1";
	if (idTurnoAsistencia != null && !idTurnoAsistencia.equals("")) {// Cuando venimos de Asistencias
		idTurnoSeleccionado = (idTurnoAsistencia.split(","))[1];

	}
    else if (idTurnoEJG != null && !idTurnoEJG.equals("")) {// cuando venimos de EJG
    	idTurnoSeleccionado = (idTurnoEJG.split(","))[1];
		
	}
	elementoSelTurno.add(idTurnoSeleccionado);
	
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	String filtrarModulos = "N";
	if (request.getAttribute("filtrarModulos") != null) {
		filtrarModulos = (String)request.getAttribute("filtrarModulos");
	}
	
	String comboJuzgados = "getJuzgadosJurisdiccionNuevaDesigna";
   	String comboModulos = "getProcedimientosEnVigencia";
   	String comboModulosParentQueryIds = "idjuzgado,fechadesdevigor,fechahastavigor";
   	String comboPretensionesEjis= "getPretensionesNuevaDesignaEjisModulosFiltros";
   	
	

	String fechaVigor = GstDate.getHoyJsp();
	ArrayList elementoSelJuzgado = new ArrayList();
	String idJuzgado = null;
	String juzgadoEJG = (String)request.getAttribute("idjuzgadoEJG");
	if(juzgadoEJG == null || juzgadoEJG.equals("")){
		juzgadoEJG = (String)request.getParameter("idjuzgadoEJG");
	}
	if (juzgadoEJG != null && !juzgadoEJG.equals("")) {
		//El atributo viene de la forma  idJuzgado, idinstitucion por lo que hacemos split
		//request.setAttribute("idjuzgadoEJG", miform.getJuzgadoAsi()+ "," + miform.getJuzgadoInstitucionAsi());
		String idsJuzgadoEJG[] = juzgadoEJG.split(","); 
		idJuzgado = idsJuzgadoEJG[0];
		elementoSelJuzgado.add(0,"{\"idjuzgado\":\""+idJuzgado+"\",\"idinstitucion\":\""+idsJuzgadoEJG[1]+"\",\"fechadesdevigor\":\""+fechaVigor+"\",\"fechahastavigor\":\""+fechaVigor+"\"}");
	
	}
	
	String juzgadoAsistencia = (String)request.getAttribute("juzgadoAsistencia");	
	
	if (juzgadoAsistencia != null && !juzgadoAsistencia.equals("")) {
		//El atributo viene de la forma  idJuzgado, idinstitucion por lo que hacemos split
		//request.setAttribute("juzgadoAsistencia", miform.getJuzgadoAsi()+ "," + miform.getJuzgadoInstitucionAsi());
		String idsJuzgadoAsistencia[] = juzgadoAsistencia.split(","); 
		idJuzgado = idsJuzgadoAsistencia[0];
		elementoSelJuzgado.add(0,"{\"idjuzgado\":\""+idJuzgado+"\",\"idinstitucion\":\""+idsJuzgadoAsistencia[1]+"\",\"fechadesdevigor\":\""+fechaVigor+"\",\"fechahastavigor\":\""+fechaVigor+"\"}");
		
	}
	
	if(idJuzgado==null||idJuzgado.equals("")){
		idJuzgado = "-1";
	}
	String comboPretensiones = "getPretensionesNuevaDesigna";
	String comboPretensionesParentQueryIds = null;
	
	String paramsTurnosJSON = "{\"idturno\":\""+idTurnoSeleccionado+"\"";
	paramsTurnosJSON += ",\"fechadesdevigor\":\""+fechaVigor+"\"";
	paramsTurnosJSON += ",\"fechahastavigor\":\""+fechaVigor+"\"}";
	
	String paramsJuzgadoJSON = "";
	paramsJuzgadoJSON += "{\"idturno\":\""+idTurnoSeleccionado+"\"";
	paramsJuzgadoJSON += ",\"fechadesdevigor\":\""+fechaVigor+"\"";
	paramsJuzgadoJSON += ",\"fechahastavigor\":\""+fechaVigor+"\"}";
	
	String idProcedimientoParamsJSON = "{\"idprocedimiento\":\"-1\"";
	idProcedimientoParamsJSON += ",\"fechadesdevigor\":\""+fechaVigor+"\"";
	idProcedimientoParamsJSON += ",\"fechahastavigor\":\""+fechaVigor+"\"}";
	
	
	String idPretensionParamsJSON ="";
	boolean obligatoriojuzgado=false;
	
	
	if (ejisActivo>0 || pcajgActivo == 4){
		comboPretensiones = comboPretensionesEjis;
		comboPretensionesParentQueryIds = "idjuzgado";
		//obligatoriojuzgado = true;
	} else {
		comboPretensionesParentQueryIds = "";
		idPretensionParamsJSON = "";
	}
	
	String asterisco = "&nbsp(*)&nbsp";
	
	boolean obligatorioProcedimiento = false;
	boolean obligatorioModulo=false;
	boolean obligatorioTipoDesigna=false;
	
	/*
	if (pcajgActivo==2){
		obligatorioProcedimiento = true;
	}else if(pcajgActivo==3){
		obligatorioProcedimiento = true;
	}else if (pcajgActivo==4){
	    obligatorioProcedimiento = true;
		obligatorioModulo=true;
		obligatorioTipoDesigna=true;		
	}else if (pcajgActivo==5){
		obligatoriojuzgado = true;
		obligatorioProcedimiento = true;
	}
	*/
	
	
	
	
	ArrayList elementoSelSolicitante = new ArrayList();
	elementoSelSolicitante.add("-1");
	
	String diligenciaAsi = (String)request.getAttribute("diligencia");
	String procedimientoAsi = (String)request.getAttribute("procedimiento");
	String comisariaAsi = (String)request.getAttribute("comisaria");
	
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



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

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
<%if (ejisActivo>0){%>
	<html:hidden property ="anioProcedimiento" value = "<%=anioProcedimiento%>"/>
<%}%>
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
			<% if (obligatorioTipoDesigna) { %>
		 		<%= asterisco %>
			<% } %>
		</td>
		<td class="labelText" >	
			<siga:ComboBD nombre="tipoDesigna" tipo="tipoDesignaColegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=elementoSelTipoDesigna%>"  ancho="480" />
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
							<siga:Select id="idTurno" queryId="getTurnosDesignacionVisibles" queryParamId="idturno" params="<%=paramsTurnosJSON%>" selectedIds="<%=elementoSelTurno%>" required="true" childrenIds="juzgado"  width="500"/>
						
							
						</td>
					</tr>
				</table>
				<table border="0" width="100%">
					<tr>
						<td width="22%">&nbsp;</td>
						<td width="10%">&nbsp;</td>
						<td width="33%">&nbsp;</td>
						<td width="35%">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4">
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
							<input type="text" name="ncolegiado" class="boxConsulta" readOnly value="" style="width:50px;"/>
							<html:hidden property="colegioOrigen" value=""></html:hidden>
			
						</td>
						<td class="labelText">
							<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
						</td>
						<td >
							<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" style="width:320px;"/>
						</td>			
					</tr>
					
					
				</table>
				
				
				
			</siga:ConjCampos> 
		</td>	
	</tr>
	<tr>
		<td class="labelText" colspan="2"><siga:Idioma key="gratuita.designa.designacionAutomatica"/></td>		
	</tr>
	
	<tr>

		<td colspan= "2"><!-- Busqueda automatica de juzgados--> 						
			<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">
				<table>							
					<tr>
							
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext" />
							<% if (obligatoriojuzgado){ %>
								<%= asterisco %>
						</td>
					
						<td>
							<siga:Select id="juzgado" queryId="<%=comboJuzgados%>" queryParamId="idjuzgado" parentQueryParamIds="idTurno" params="<%=paramsJuzgadoJSON%>" selectedIds="<%=elementoSelJuzgado%>" showSearchBox="true" searchkey="CODIGOEXT2" searchBoxMaxLength="10" searchBoxWidth="8" width="515" required="true" childrenIds="idPretension,idProcedimiento"/>		
						</td>
							<%}else{%>
							</td>
					
						<td>
							<siga:Select id="juzgado" queryId="<%=comboJuzgados%>" queryParamId="idjuzgado" parentQueryParamIds="idTurno" params="<%=paramsJuzgadoJSON%>" selectedIds="<%=elementoSelJuzgado%>" showSearchBox="true" searchkey="CODIGOEXT2" searchBoxMaxLength="10" searchBoxWidth="8" width="515"  childrenIds="idPretension,idProcedimiento"/>		
						</td>
							<%}%>
						 
					</tr>
						
				
				<!-- Juzgado -->		
			</table>
		</siga:ConjCampos> 																
		</td>
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo" />
			<% if (obligatorioModulo) { %>
		 		<%= asterisco %>
		 	</td>
			<td>
				<siga:Select id="idProcedimiento" queryId="<%=comboModulos%>" parentQueryParamIds="<%=comboModulosParentQueryIds%>" params="<%=idProcedimientoParamsJSON%>" required="true"  width="490"/>
			</td>
			<%} else { %>
		 		</td>
			<td>
				<siga:Select id="idProcedimiento" queryId="<%=comboModulos%>" parentQueryParamIds="<%=comboModulosParentQueryIds%>" params="<%=idProcedimientoParamsJSON%>"  width="490"/>
			</td>
			<% } %>
			
	</tr>
	<tr>
		<td class="labelText">	
			<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
			<% if (obligatorioProcedimiento) { %>
		 		<%= asterisco %>
		 		</td>				
				<td >
					<siga:Select id="idPretension" queryId="<%=comboPretensiones %>"  parentQueryParamIds="<%=comboPretensionesParentQueryIds %>"  required="true" width="490" />
				</td>
			<%}else { %>
		 		</td>				
				<td >
					<siga:Select id="idPretension" queryId="<%=comboPretensiones %>"  parentQueryParamIds="<%=comboPretensionesParentQueryIds %>"   width="490" />
				</td>
			<% } %>
		
	</tr>

</html:form>
	
<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
	<input type="hidden" name="modo" value="buscarJuzgado">
	<html:hidden property="codigoExt2" value="" />
	<html:hidden property="nombreObjetoDestino" value="" />
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
			var error = "";
			<%if (pcajgActivo>0) { %>
				
			
				if (<%=obligatorioTipoDesigna%> && document.getElementById("tipoDesigna").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.editarDesigna.literal.tipo'/>"+ '\n';

				if(<%=obligatoriojuzgado%> && document.getElementById("juzgado").value=="")										
					error += "<siga:Idioma key='gratuita.editarDesigna.juzgado'/>"+ '\n';
			
				if (<%=obligatorioModulo%> && document.getElementById("idProcedimiento").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.modulo'/>"+ '\n';
				
				if (<%=obligatorioProcedimiento%> && document.getElementById("idPretension").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.pretensiones'/>"+ '\n';
			
			
				
	 		<%} %>
			
			
			if(document.forms[1].colegioOrigen.value==0)
				document.forms[1].colegioOrigen.value="";
			
			if(document.forms[1].idTurno.selectedIndex < 1){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.busquedaSOJ.literal.turno'/>"+ '\n';
			
			}
			if(document.forms[1].fechaAperturaInicio.value.length < 1){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.busquedaDesignas.literal.fechaApertura'/>"+ '\n';
			
			}
			
			if(error!=""){
				alert(error);
				fin();
				return false;
			}
			if (document.forms[1].ncolegiado.value || confirm("<siga:Idioma key='messages.designa.confirmacion.seleccionAutomaticaLetrado' />")){
				document.forms[1].modo.value="insertar";
				document.forms[1].submit();
			}else{
				fin();
				return false;
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
			seleccionComboSiga("juzgado",-1);
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
