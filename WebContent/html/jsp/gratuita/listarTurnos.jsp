<!-- listarTurnos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	System.out.println(usr.getLocation());
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole = (Colegio)ses.getAttribute("colegio");
	String elTarget = "mainWorkArea";

	request.getSession().removeAttribute("ocultos");
	request.getSession().removeAttribute("campos");
	FilaExtElement[] elems=new FilaExtElement[1];

	//Datos del Colegiado si procede:
	String nombrePestanha=null, numeroPestanha=null;
	try {
		Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e){
		nombrePestanha = "";
		numeroPestanha = "";
	}

	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	String activaSeleccionFila = "true";
	
	if (entrada.equalsIgnoreCase("2")){
		elTarget="mainPestanas";
		activaSeleccionFila = "false";
	}
	String mantTurnos=(String)request.getAttribute("mantTurnos");
	boolean bMant=true;
	String ajusteB="false";	
	if (mantTurnos==null || !mantTurnos.equals("1")) {
		bMant=false;
		ajusteB="true";
	}

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	Vector resultado = null;
	String idioma = usr.getLanguage().toUpperCase();
	String paginaSeleccionada = "";
	String totalRegistros = "";
	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	String atributoPaginador = "";
	String action = "";
	String modo = "";
	boolean bIncluirBajaLogica = false;
	if (entrada.equalsIgnoreCase("1")){
		resultado = (Vector) request.getAttribute("resultado");
	}else{
		// jbd Añadido paginador e historico segun inc-5679
		atributoPaginador = (String) request
				.getAttribute(ClsConstants.PARAM_PAGINACION);
		if (ses.getAttribute(atributoPaginador) != null) {
			hm = (HashMap) ses.getAttribute(atributoPaginador);
			if (hm.get("datos") != null && !hm.get("datos").equals("")) {
				resultado = (Vector) hm.get("datos");
				PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
				paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
				totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
				registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
	
			} else {
				resultado = new Vector();
				paginaSeleccionada = "0";
				totalRegistros = "0";
				registrosPorPagina = "0";
			}
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";
			totalRegistros = "0";
			registrosPorPagina = "0";
		}
		bIncluirBajaLogica = UtilidadesString.stringToBoolean((String)request.getSession().getAttribute("bIncluirRegistrosConBajaLogica"));
		action=app+"/JGR_DefinirTurnosLetrado.do?noReset=true&bIncluirRegistrosConBajaLogica="+(String)request.getAttribute("bIncluirRegistrosConBajaLogica");
		modo = 	(String)ses.getAttribute("MODO");
	}
	
	String altoLista = entrada.equalsIgnoreCase("1")?"0":"80";

%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<% if (entrada.equalsIgnoreCase("2")){ %>
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.turnos.cabecera" 
			localizacion="censo.fichaCliente.sjcs.turnos.localizacion"/>
			
			<% } %>
		<!-- FIN: TITULO Y LOCALIZACION -->	

</head>

<body class="tablaCentralCampos">
    <table class="tablaTitulo" align="center" cellspacing=0>
	<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { %>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.turnoInscrito.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
	<% } %>

	<% 	
	    String nC="";
		String tC="";
		String botones="";
		String alto="";
		
		//Entrada desde el menu de  SJCS:
		if (entrada.equalsIgnoreCase("1")){
			nC="gratuita.definirTurnosIndex.literal.abreviatura,censo.SolicitudIncorporacion.literal.nombre,gratuita.definirTurnosIndex.literal.area,gratuita.definirTurnosIndex.literal.materia,gratuita.definirTurnosIndex.literal.zona,gratuita.definirTurnosIndex.literal.subzona,gratuita.definirTurnosIndex.literal.grupoFacturacion,gratuita.listarTurnos.literal.letradosInscritos,";
			tC="10,20,10,14,10,10,10,5,10";
			botones="C,E,B";
			alto="253";}
		//Entrada desde el menu de Censo:
		else {	
				nC="gratuita.definirTurnosIndex.literal.abreviatura,censo.SolicitudIncorporacion.literal.nombre,gratuita.definirTurnosIndex.literal.area,gratuita.definirTurnosIndex.literal.materia,gratuita.definirTurnosIndex.literal.zona,gratuita.definirTurnosIndex.literal.subzona,gratuita.listarTurnos.literal.fechaAlta,gratuita.listarTurnos.literal.fechaValidacion,gratuita.listarGuardiasTurno.literal.fechaBaja,";
				tC="9,17,8,9,9,8,9,9,9,13";
				botones="C";
				alto="326";}
	%>
		<html:form action="DefinirTurnosAction.do" method="post" target="<%=elTarget%>" style="display:none">
		
			<!-- Datos del Colegiado seleccionado -->
			<input type="hidden" name = "nombreColegiadoPestanha" value = "<%=nombrePestanha%>"/>
			<input type="hidden" name = "numeroColegiadoPestanha" value = "<%=numeroPestanha%>"/>
			
			<input type="hidden" name="modo" />
			<!-- campos a pasar -->
			<input type="hidden" name="paso" value="turno"/>
			<input type="hidden" name="idInstitucion" />
			<input type="hidden" name="idPersona" />
			<input type="hidden" name="idTurno" />
			<input type="hidden" name="fechaSolicitud"/>
			<input type="hidden" name="observacionesSolicitud"/>
			<input type="hidden" name="fechaValidacion"/>
			<input type="hidden" name="observacionesValidacion"/>
			<input type="hidden" name="fechaSolicitudBaja"/>
			<input type="hidden" name="observacionesBaja"/>
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">

		</html:form>	
		
		
	<siga:TablaCabecerasFijas 
		   nombre="listarTurnos"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   ajuste="<%= altoLista %>"
		   ajusteBotonera="true"		
		   ajusteBotonera="<%=ajusteB%>"  
		   activarFilaSel="<%=activaSeleccionFila%>">
	
	<%if (resultado == null || resultado.size() == 0) {%>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%} else {
	
		for (int j = 0; j < resultado.size(); j++) {
			int i=j+1;
			Hashtable registro;
			if (entrada.equalsIgnoreCase("1")){
				registro = (Hashtable)resultado.get(j);
			}else{
				Row fila = (Row)resultado.elementAt(j);
				registro = (Hashtable) fila.getRow();
			}
			String o1,o2,o3,o4,o5="";
			
			try{
				if(((String)registro.get("DESCRIPCION")).equals(""))o1="-";
				else o1=(String)registro.get("DESCRIPCION");
			}catch(Exception e){o1="-";}
			try {
				if(((String)registro.get("REQUISITOS")).equals(""))o2="-";
				else o2=(String)registro.get("REQUISITOS");
			}catch(Exception e){o2="-";}
			
			try {
				if(((String)registro.get("IDSUBZONA")).equals(""))o3="-1";
				else o3=(String)registro.get("IDSUBZONA");
			}catch(Exception e){o3="-1";}
			
			try {
				if(((String)registro.get("IDPARTIDAPRESUPUESTARIA")).equals(""))o4="-1";
				else o4=(String)registro.get("IDPARTIDAPRESUPUESTARIA");
			}catch(Exception e){o4="-1";}
			
			try {
				if(((String)registro.get("IDPERSONAULTIMO")).equals(""))o5="-1";
				else o5=(String)registro.get("IDPERSONAULTIMO");
			}catch(Exception e){o5="-1";}
				
			//obtenemos la fecha de validacion
			elems[0]=null;
			String fechaSolicitud 		= (String) registro.get("FECHASOLICITUD");
			String fechaValidacion 		= (String) registro.get("FECHAVALIDACION");
			String fechaSolicitudBaja 	= (String) registro.get("FECHASOLICITUDBAJA");
			String fechaBaja 			= (String) registro.get("FECHABAJA");
			
			if(fechaSolicitud == null) 		fechaSolicitud 	= "";
			if(fechaValidacion == null) 	fechaValidacion = "";
			if(fechaBaja == null) 			fechaBaja 		= "";
			if(fechaSolicitudBaja == null) 	fechaSolicitudBaja = "";

				if(!fechaSolicitud.equals("") && !fechaValidacion.equals("") && fechaSolicitudBaja.equals("") && fechaBaja.equals(""))
				{
					elems[0]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
				}
			%>

			<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit">
				<td ><input type='hidden' name='oculto<%=String.valueOf(i)%>_1' value='<%=registro.get("IDTURNO")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_2' value='<%=registro.get("GUARDIAS")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_3' value='<%=registro.get("VALIDARJUSTIFICACIONES")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_4' value='<%=registro.get("VALIDARINSCRIPCIONES")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_5' value='<%=registro.get("DESIGNADIRECTA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_6' value='<%=registro.get("REPARTOPORPUNTOS")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_7' value='<%=o1%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_8' value='<%=o2%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_9' value='<%=registro.get("IDORDENACIONCOLAS")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_10' value='<%=o5%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_11' value='<%=registro.get("IDAREA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_12' value='<%=registro.get("IDMATERIA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_13' value='<%=registro.get("IDZONA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_14' value='<%=o3%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_15' value='<%=o4%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_16' value='<%=o5%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_17' value='<%=registro.get("IDGRUPOFACTURACION")%>'><%if (entrada.equals("2")){%><input type='hidden' name='oculto<%=String.valueOf(i)%>_18' value='<%=registro.get("PARTIDAPRESUPUESTARIA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_19' value='<%=registro.get("GRUPOFACTURACION")%>'><%}%><input type='hidden' name='oculto<%=String.valueOf(i)%>_20' value='<%=registro.get("FECHASOLICITUD")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_21' value='<%=registro.get("OBSERVACIONESSOLICITUD")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_22' value='<%=registro.get("FECHAVALIDACION")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_23' value='<%=registro.get("OBSERVACIONESVALIDACION")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_24' value='<%=registro.get("FECHASOLICITUDBAJA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_25' value='<%=registro.get("OBSERVACIONESBAJA")%>'><input type='hidden' name='oculto<%=String.valueOf(i)%>_26' value='<%=registro.get("IDGRUPOFACTURACION")%>'><%=registro.get("ABREVIATURA")%>&nbsp;</td>
				<td ><%=((String)registro.get("NOMBRE"))%>&nbsp;</td>
				<td ><%=registro.get("AREA")%>&nbsp;</td>
				<td ><%=registro.get("MATERIA")%>&nbsp;</td>
				<td ><%=registro.get("ZONA")%>&nbsp;</td>
				<td ><%if (!((String)registro.get("SUBZONA")).equals("")){%><%=registro.get("SUBZONA")%><%}else{%> &nbsp <%}%>&nbsp;</td>
				<% if (entrada.equalsIgnoreCase("1")) { %>
					<td ><%=registro.get("GRUPOFACTURACION")%></td>
				<% } else { %>
					<td ><%if (registro.get("FECHASOLICITUD")!=null){%>&nbsp;<%=GstDate.getFormatedDateShort("",(String)registro.get("FECHASOLICITUD"))%><%}else{%>&nbsp;<%}%>&nbsp;</td>
					<td ><%if (registro.get("FECHAVALIDACION")!=null){%>&nbsp;<%=GstDate.getFormatedDateShort("",(String)registro.get("FECHAVALIDACION"))%><%}else{%>&nbsp;<%}%>&nbsp;</td>
					<td ><%if (registro.get("FECHABAJA")!=null){%>&nbsp;<%=GstDate.getFormatedDateShort("",(String)registro.get("FECHABAJA"))%><%}else{%>&nbsp;<%}%>&nbsp;</td>
				<% } %>
				<% if (entrada.equalsIgnoreCase("1")){%>
			  		<td align="right"><%=registro.get("NLETRADOS")%>&nbsp;</td>
			  	<%} %>
				</siga:FilaConIconos>
			<!-- FIN REGISTRO -->
<%
	} // del for
%>
			<!-- FIN: ZONA DE REGISTROS -->
<%
	} // del if
%>		
		</siga:TablaCabecerasFijas>
		<% if (entrada.equalsIgnoreCase("2")){%>
			<!-- Si hay datos se muestra el paginador -->
			<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
				<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="abrirTurnosPaginados"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
			<%}%>
			
			
			<!-- Check para pasar a modo historico donde se muestran los turnos dados de baja -->
			<div style="position:absolute; left:400px;bottom:50px;z-index:2;">
				<table align="center" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="censo.consultaRegistrosBajaLogica.literal"/>
							
							<% if (bIncluirBajaLogica) { %>
								<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked>
							<% } else { %>
								<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);">
							<% } %>
						</td>
							
					</tr>
				</table>
			</div>
		<%} %>
		
		
		<%
		// consultamos si el colegiado esta dado de baja
		String idper = (String)request.getSession().getAttribute("idPersonaTurno");
		boolean estaDeBaja = false;
		// Modificado 12/7/05 por MAV a instancia JG (se busca que cuando accedes con un letrado no ejerciente no puedas darte de alta en una guardia)
		//if(usr.isLetrado() && idper != null)
		if(idper != null)
		{
			CenColegiadoAdm cenColegiadoAdm = new CenColegiadoAdm(usr);
			Hashtable hashtable = cenColegiadoAdm.getEstadoColegial(Long.valueOf(idper),Integer.valueOf(usr.getLocation()));
			if(hashtable==null){
				estaDeBaja = true;
			}	
			else{
				// Modificado 12/7/05 por MAV 
				//if(hashtable.get("IDESTADO").equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_BAJACOLEGIAL))) estaDeBaja = true;
				if(!hashtable.get("IDESTADO").equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))){
					estaDeBaja = true;
				}	
			}
		} %>
		
		<% 	if (!bMant) { %>
		<% if (!estaDeBaja) { %>
			<% if (!busquedaVolver.equals("volverNo")) { %>
					<siga:ConjBotonesAccion botones="V,bajaEnTodosLosTurnos,L" clase="botonesDetalle"  />	
			<%  } else { %>
					<siga:ConjBotonesAccion botones="bajaEnTodosLosTurnos,L" clase="botonesDetalle"  />	
			<%  } %>
		<% } else { %>
			<% if (!busquedaVolver.equals("volverNo")) { 
			%>
					<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />	
			<%  } %>
		<% } %>
		<% } %>

	
		<script>
				//Valida el Alta en un Turno:
				function validaralta(fila) 
				{
					  var turno 				= 'oculto' + fila + '_' + 1;
					  var fsoli 				    = 'oculto' + fila + '_' + 20;
					  var osoli 				= 'oculto' + fila + '_' + 21;
					  document.forms[0].idInstitucion.value 	= <%=usr.getLocation()%>;
					  document.forms[0].idPersona.value 		= <%=request.getSession().getAttribute("idPersonaTurno")%>;
					  document.forms[0].idTurno.value 			= document.getElementById(turno).value;
					  document.forms[0].fechaSolicitud.value = document.getElementById(fsoli).value;
					  document.forms[0].observacionesSolicitud.value = document.getElementById(osoli).value;
					  document.forms[0].action		= "<%=app%>/JGR_ValidarTurnos.do";
					  document.forms[0].modo.value = "Ver";
		
					  var resultado = ventaModalGeneral(document.forms[0].name,"G");
				      if(resultado == "MODIFICADO") 
				      	refrescarLocal();
				      document.forms[0].action= "<%=app%>/DefinirTurnosAction.do";
				 }
				 
				//Valida la Baja en un Turno:
				function validar(fila) 
				{
					  var turno = 'oculto' + fila + '_' + 1;
					  var fsoli = 'oculto' + fila + '_' + 20;
					  var osoli = 'oculto' + fila + '_' + 21;
					  var fvali = 'oculto' + fila + '_' + 22;
					  var ovali = 'oculto' + fila + '_' + 23;
					  var fbaja = 'oculto' + fila + '_' + 24;
					  var obaja = 'oculto' + fila + '_' + 25;
					  document.forms[0].idInstitucion.value 	= <%=usr.getLocation()%>;
					  document.forms[0].idPersona.value 		= <%=request.getSession().getAttribute("idPersonaTurno")%>;
					  document.forms[0].idTurno.value 			= document.getElementById(turno).value;
					  document.forms[0].fechaSolicitud.value = document.getElementById(fsoli).value;
					  document.forms[0].observacionesSolicitud.value = document.getElementById(osoli).value;
					  document.forms[0].fechaValidacion.value = document.getElementById(fvali).value;
					  document.forms[0].observacionesValidacion.value = document.getElementById(ovali).value;
					  document.forms[0].fechaSolicitudBaja.value = document.getElementById(fbaja).value;
					  document.forms[0].observacionesBaja.value = document.getElementById(obaja).value;
					  document.forms[0].paso.value = "turno";
					  document.forms[0].action		= "<%=app%>/JGR_BajaTurnos.do";
					  document.forms[0].modo.value = "Ver";
		
					  var resultado = ventaModalGeneral(document.forms[0].name,"G");
				      if(resultado == "MODIFICADO")
				      	 refrescarLocal(); 
				      document.forms[0].action		= "<%=app%>/DefinirTurnosAction.do";				    
				 }
				 
				function solicitarbaja(fila) 
				{
				  var turno = 'oculto' + fila + '_' + 1;
				  var fsoli = 'oculto' + fila + '_' + 20;
				  var osoli = 'oculto' + fila + '_' + 21;
				  var fvali = 'oculto' + fila + '_' + 22;
				  var ovali = 'oculto' + fila + '_' + 23;
				  var fbaja = 'oculto' + fila + '_' + 24;
				  var obaja = 'oculto' + fila + '_' + 25;
				  document.BajaTurnosForm.idInstitucion.value 			= <%=usr.getLocation()%>;
				  document.BajaTurnosForm.idPersona.value 				= <%=request.getSession().getAttribute("idPersonaTurno")%>;
				  document.BajaTurnosForm.idTurno.value 					= document.getElementById(turno).value;
				  document.BajaTurnosForm.fechaSolicitud.value 			= document.getElementById(fsoli).value;
				  document.BajaTurnosForm.observacionesSolicitud.value 	= document.getElementById(osoli).value;
				  document.BajaTurnosForm.fechaValidacion.value 			= document.getElementById(fvali).value;
				  document.BajaTurnosForm.observacionesValidacion.value 	= document.getElementById(ovali).value;
				  document.BajaTurnosForm.fechaSolicitudBaja.value 		= document.getElementById(fbaja).value;
				  document.BajaTurnosForm.observacionesBaja.value 		= document.getElementById(obaja).value;
				  document.BajaTurnosForm.paso.value 						= "turnoS";
				  //document.forms[1].action   						= "<%=app%>/JGR_SolicitarBajaTurno.do";
				  document.BajaTurnosForm.modo.value 						= "Ver";

				  document.BajaTurnosForm.confirmacion.value = "0";
				  document.BajaTurnosForm.target = "submitArea";
				  document.BajaTurnosForm.submit();
	
/*				  var resultado = ventaModalGeneral(document.BajaTurnosForm.name,"G");
			      if(resultado == "MODIFICADO") 
			      	 refrescarLocal(); 
			      //document.forms[1].action		= "<%=app%>/DefinirTurnosAction.do";				    
*/
				}
				
				function darDeBajaEnTodosLosTurnos(mostrarMensaje) 
				{
					if (mostrarMensaje) {
						var mensaje = "<siga:Idioma key="censo.fichaCliente.turnoInscrito.pregunta.bajaEnTodosLosTurnos"/>";
						if(!confirm(mensaje)) {
                           
							return;
						}
						
					}
					
					document.BajaTurnosForm.modo.value = "bajaEnTodosLosTurnos";
					document.BajaTurnosForm.target = "submitArea";
					document.BajaTurnosForm.submit();
				}
				
				 function buscar()
				 {
					document.forms[0].action		 = "<%=app%>/JGR_DefinirTurnosLetradoAction.do?granotm=<%=System.currentTimeMillis()%>";
			 		document.forms[0].modo.value = "buscarPor";
			 		document.forms[0].submit();
				 }
					
				function accionSolicitar()
				{
					document.forms[0].action		= "<%=app%>/JGR_AltaTurnosGuardias.do";
					document.forms[0].modo.value	= "";
			   		var resultado = ventaModalGeneral(document.forms[0].name,"G");
				    if(resultado == "MODIFICADO") 
				    	refrescarLocal();
					document.forms[0].action		= "<%=app%>/DefinirTurnosAction.do";
				}
				
				function refrescarLocal()
				{
					document.forms[0].action = "<%=app%>/JGR_DefinirTurnosLetrado.do";
					document.forms[0].submit();
				}
		
				function preguntaConfirmacion(mensajeConfirmacion)
				{
					document.BajaTurnosForm.confirmacion.value = "1";

					if (mensajeConfirmacion != null && mensajeConfirmacion != "") {
						if(!confirm(mensajeConfirmacion)) {	
							return
						}
						
					} 

					if (document.BajaTurnosForm.modo.value == "bajaEnTodosLosTurnos") {
					  	darDeBajaEnTodosLosTurnos(false);
					}
					else {
					    var resultado = ventaModalGeneral(document.BajaTurnosForm.name, "G");
					    if(resultado == "MODIFICADO") {
					      refrescarLocal(); 
					    }
					}
				}

				// Funcion que agrega el concepto de baja logica
				function incluirRegBajaLogica(o) {
					if (o.checked) {
						document.DefinirTurnosLetradoForm.incluirRegistrosConBajaLogica.value = "S";
					} else {
						document.DefinirTurnosLetradoForm.incluirRegistrosConBajaLogica.value = "N";
					}
					document.DefinirTurnosLetradoForm.modo.value = "abrirTurnosLimpiar";
					document.DefinirTurnosLetradoForm.submit();
				}
				
				</script>	


		
		<html:form action="/JGR_SolicitarBajaTurno.do" method="post" target="">
			<!-- indica que estamos en solicitud de baja -->
			<input type="hidden" name="solBaja" value="1" />
		
			<input type="hidden" name="actionModal" value="" /> 
			<html:hidden name="BajaTurnosForm" property="modo" /> 
			<!-- campos a pasar -->
			<html:hidden name="BajaTurnosForm" property="paso" value="turno"/>
			<html:hidden name="BajaTurnosForm" property="idInstitucion" />
			<html:hidden name="BajaTurnosForm" property="idPersona" />
			<html:hidden name="BajaTurnosForm" property="idTurno" />
			<html:hidden name="BajaTurnosForm" property="fechaSolicitud"/>
			<html:hidden name="BajaTurnosForm" property="observacionesSolicitud"/>
			<html:hidden name="BajaTurnosForm" property="fechaValidacion"/>
			<html:hidden name="BajaTurnosForm" property="observacionesValidacion"/>
			<html:hidden name="BajaTurnosForm" property="fechaSolicitudBaja"/>
			<html:hidden name="BajaTurnosForm" property="observacionesBaja"/>
			<html:hidden name="BajaTurnosForm" property="confirmacion"/>
		</html:form>	
			
		<html:form action="/JGR_DefinirTurnosLetrado.do" method="post" target="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
			<html:hidden name="DefinirTurnosLetradoForm" property="modo" />
		</html:form>	

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

		</table>		
	</body>
</html>