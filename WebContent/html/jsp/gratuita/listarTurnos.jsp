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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole = (Colegio)ses.getAttribute("colegio");
	String elTarget = "mainWorkArea";

	request.getSession().removeAttribute("ocultos");
	request.getSession().removeAttribute("campos");
	FilaExtElement[] elems=null;

	//Datos del Colegiado si procede:
	String nombrePestanha=null, numeroPestanha=null, estadoColegial=null;
	try {
		Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
		estadoColegial = (String)datosColegiado.get("ESTADOCOLEGIAL");
	} catch (Exception e){
		nombrePestanha = "";
		numeroPestanha = "";
	}
		//aalg: se obtiene el modo de acceso para controlar los permisos 
	String accion = String.valueOf(request.getSession().getAttribute("modoPestanha"));
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	String activaSeleccionFila = "true";
	
	if (entrada.equalsIgnoreCase("2")){
		elTarget="mainPestanas";
		activaSeleccionFila = "false";
	}
	String mantTurnos=(String)request.getAttribute("mantTurnos");
	String turnosBajaLogica = (String)request.getAttribute("BAJALOGICATURNOS");
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

<%@page import="com.siga.Utilidades.PaginadorBind"%>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>

			
		
	    			
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<% 
		boolean isEntradaSJCS = entrada.equalsIgnoreCase("1");
		if (!isEntradaSJCS){ %>
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.turnos.cabecera" 
			localizacion="censo.fichaCliente.sjcs.turnos.localizacion"/>
			
			<% } %>
		<!-- FIN: TITULO Y LOCALIZACION -->	

</head>

<body class="tablaCentralCampos" onload="mostrarFecha();">
    
	<%
	
		//Entrada desde el menu de Censo:
		if (!isEntradaSJCS) { %>
		<table class="tablaTitulo" align="center" cellspacing=0>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.turnoInscrito.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
						<%if (estadoColegial!=null && !estadoColegial.equals("")){%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
							 <%= UtilidadesString.mostrarDatoJSP(numeroPestanha)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
						 <%}else{%> 
						 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
						 <%}%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
			</table>
			<siga:ConjCampos leyenda="gratuita.busquedaSJCS.literal.filtro">
			<table>
			<tr>
					<td class="labelText"><siga:Idioma key="gratuita.gestionInscripciones.fechaConsulta"/></td>
					<td >
					<siga:Fecha  nombreCampo= "fechaConsulta" postFunction="accionCalendario();"/>
					</td>
				</tr>
				
			</table>
			</siga:ConjCampos>
			
			
			
			
	<% } %>

	<% 	
	    String nC="";
		String tC="";
		String botones="";
		String alto="";
		
		//Entrada desde el menu de  SJCS:
		if (isEntradaSJCS){
			nC="gratuita.definirTurnosIndex.literal.abreviatura,censo.SolicitudIncorporacion.literal.nombre,gratuita.definirTurnosIndex.literal.area,gratuita.definirTurnosIndex.literal.materia,gratuita.definirTurnosIndex.literal.zona,gratuita.definirTurnosIndex.literal.subzona,gratuita.definirTurnosIndex.literal.grupoFacturacion,gratuita.listarTurnos.literal.letradosInscritos,Estado,";
			tC="10,20,10,12,9,8,10,5,5,10";
			botones="C,E,B";
			alto="253";}
		//Entrada desde el menu de Censo:
		else {	
				nC="gratuita.definirTurnosIndex.literal.abreviatura,censo.SolicitudIncorporacion.literal.nombre,gratuita.definirTurnosIndex.literal.materia,gratuita.definirTurnosIndex.literal.subzona,gratuita.listaTurnosLetrados.literal.fechasolicitud,F. Valor,gratuita.altaTurnos.literal.fsbaja,F. Valor Baja,Estado Inscripción,Estado Turno,";
				tC="15,15,7,7,8,8,8,8,8,5,10";
				if(bIncluirBajaLogica)
					botones="";
				else
					botones="C";
				alto="326";}
	%>
		<html:form action="DefinirTurnosAction.do" method="post" target="<%=elTarget%>" style="display:none">			
			<!-- Datos del Colegiado seleccionado -->
			<input type="hidden" id="nombreColegiadoPestanha" name="nombreColegiadoPestanha"  value = "<%=nombrePestanha%>"/>
			<input type="hidden" id="numeroColegiadoPestanha"  name="numeroColegiadoPestanha" value = "<%=numeroPestanha%>"/>
			
			<input type="hidden" id="modo"  name="modo" />
			<!-- campos a pasar -->
			<input type="hidden" id="paso"  name="paso" value="turno"/>
			<input type="hidden" id="idInstitucion"  name="idInstitucion" />
			<input type="hidden" id="idPersona"  name="idPersona" />
			<input type="hidden" id="idTurno"  name="idTurno" />
			<input type="hidden" id="fechaSolicitud" name="fechaSolicitud"/>
			
			<input type="hidden" id="observacionesSolicitud" name="observacionesSolicitud"/>
			<input type="hidden" id="fechaValidacion" name="fechaValidacion"/>
			<input type="hidden" id="observacionesValidacion" name="observacionesValidacion"/>
			<input type="hidden" id="fechaSolicitudBaja" name="fechaSolicitudBaja"/>
			<input type="hidden" id="observacionesBaja" name="observacionesBaja"/>
			<input type="hidden" id="turnosBajaLogica"  name="turnosBajaLogica" value="<%=turnosBajaLogica%>">
			<input type="hidden" id="actionModal"  name="actionModal" value="">		
							
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
			if (isEntradaSJCS){
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
			elems = new FilaExtElement[2];
			elems[0]=null;
			String fechaSolicitud 		= (String) registro.get("FECHASOLICITUD");
			String fechaValidacion 		= (String) registro.get("FECHAVALIDACION");
			String fechaSolicitudBaja 	= (String) registro.get("FECHASOLICITUDBAJA");
			String fechaBaja 			= (String) registro.get("FECHABAJA");
			String fechaDenegacion 		= (String) registro.get("FECHADENEGACION");
			String fechaValor 		= (String) registro.get("FECHAVALOR");
			String fechaValorBaja 		= (String) registro.get("FECHAVALORBAJA");
			
			
			if(fechaSolicitud == null) 		fechaSolicitud 	= "";
			if(fechaValidacion == null) 	fechaValidacion = "";
			if(fechaBaja == null) 			fechaBaja 		= "";
			if(fechaSolicitudBaja == null) 	fechaSolicitudBaja = "";
			if(fechaDenegacion == null) 	fechaDenegacion = "";
			if(fechaValor == null) 	fechaValor = "";
			if(fechaValorBaja == null) 	fechaValorBaja = "";
			String estadoInscripcion = null;
			if(!isEntradaSJCS){
				elems[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);
				estadoInscripcion= "No aplica";
				if(fechaValidacion.equals("")){
					if(fechaSolicitudBaja.equals("")){
						if(fechaDenegacion.equals("")){
							estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.pendiente");
							elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
						}else{
							estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.denegada");
						}
						
					}else{
						if(fechaBaja.equals("")){
							if(fechaDenegacion.equals("")){
								estadoInscripcion  =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.pendiente");
							}else{
								elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
								estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.denegada");
							}
							
						}else{
							estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.confirmada");
							
						}
						
					}
					
				}else{
					
					if(fechaSolicitudBaja.equals("")){
						estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.confirmada");
						elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
					}else{
						if(fechaBaja.equals("")){
							if(fechaDenegacion.equals("")){
								estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.pendiente");
							}else{
								elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
								estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.denegada");
							}
							
						}else{
							estadoInscripcion =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.confirmada");
							
						}
					}
				}
			}
			%>

			<siga:FilaConIconos fila='<%=String.valueOf(i)%>' botones="<%=botones%>" visibleBorrado="<%=String.valueOf(isEntradaSJCS)%>" visibleEdicion="<%=String.valueOf(isEntradaSJCS)%>" pintarEspacio="<%=String.valueOf(isEntradaSJCS)%>" elementos='<%=elems%>' clase="listaNonEdit">
				
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_1' name='oculto<%=String.valueOf(i)%>_1' value='<%=registro.get("IDTURNO")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_2' name='oculto<%=String.valueOf(i)%>_2' value='<%=registro.get("GUARDIAS")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_3' name='oculto<%=String.valueOf(i)%>_3' value='<%=registro.get("VALIDARJUSTIFICACIONES")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_4' name='oculto<%=String.valueOf(i)%>_4' value='<%=registro.get("VALIDARINSCRIPCIONES")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_5' name='oculto<%=String.valueOf(i)%>_5' value='<%=registro.get("DESIGNADIRECTA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_6' name='oculto<%=String.valueOf(i)%>_6' value='<%=registro.get("REPARTOPORPUNTOS")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_7' name='oculto<%=String.valueOf(i)%>_7' value=''/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_8' name='oculto<%=String.valueOf(i)%>_8' value=''/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_9' name='oculto<%=String.valueOf(i)%>_9' value='<%=registro.get("IDORDENACIONCOLAS")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_10' name='oculto<%=String.valueOf(i)%>_10' value='<%=o5%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_11' name='oculto<%=String.valueOf(i)%>_11' value='<%=registro.get("IDAREA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_12' name='oculto<%=String.valueOf(i)%>_12' value='<%=registro.get("IDMATERIA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_13' name='oculto<%=String.valueOf(i)%>_13' value='<%=registro.get("IDZONA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_14' name='oculto<%=String.valueOf(i)%>_14' value='<%=o3%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_15' name='oculto<%=String.valueOf(i)%>_15' value='<%=o4%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_16' name='oculto<%=String.valueOf(i)%>_16' value='<%=o5%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_17' name='oculto<%=String.valueOf(i)%>_17' value='<%=registro.get("IDGRUPOFACTURACION")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_18' name='oculto<%=String.valueOf(i)%>_18' value=''/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_19' name='oculto<%=String.valueOf(i)%>_19' value=''/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_20' name='oculto<%=String.valueOf(i)%>_20' value='<%=registro.get("FECHASOLICITUD")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_21' name='oculto<%=String.valueOf(i)%>_21' value='<%=registro.get("OBSERVACIONESSOLICITUD")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_22' name='oculto<%=String.valueOf(i)%>_22' value='<%=registro.get("FECHAVALIDACION")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_23' name='oculto<%=String.valueOf(i)%>_23' value='<%=registro.get("OBSERVACIONESVALIDACION")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_24' name='oculto<%=String.valueOf(i)%>_24' value='<%=registro.get("FECHASOLICITUDBAJA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_25' name='oculto<%=String.valueOf(i)%>_25' value='<%=registro.get("OBSERVACIONESBAJA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_26' name='oculto<%=String.valueOf(i)%>_26' value='<%=registro.get("IDGRUPOFACTURACION")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_27' name='oculto<%=String.valueOf(i)%>_27' value='<%=registro.get("FECHABAJA")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_28' name='oculto<%=String.valueOf(i)%>_28' value='<%=registro.get("FECHADENEGACION")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_29' name='oculto<%=String.valueOf(i)%>_29' value=''/>
				<input type='hidden' id='oculto<%=String.valueOf(i)%>_30' name='oculto<%=String.valueOf(i)%>_30' value='<%=registro.get("OBSERVACIONESVALBAJA")%>'/>
				
				<%if (!isEntradaSJCS){%>
				<td ><%=registro.get("ABREVIATURA")%>&nbsp;</td>
				<td ><%=((String)registro.get("NOMBRE"))%>&nbsp;</td>
				<td ><%=registro.get("MATERIA")%>&nbsp;</td>
				
				<td ><%if (!((String)registro.get("SUBZONA")).equals("")){%><%=registro.get("SUBZONA")%><%}else{%> &nbsp <%}%>&nbsp;</td>
				<td ><%if (!fechaSolicitud.equals("")){%>
					&nbsp;<%=GstDate.getFormatedDateShort("",fechaSolicitud)%>
					<%}else{%>
					&nbsp;<%}%>
				</td>
					<td ><%if (!fechaValor.equals("")){%>
						&nbsp;<%=GstDate.getFormatedDateShort("",fechaValor)%>
						<%}else{%>
						&nbsp;<%}%>
						
					</td>
					<td ><%if(!fechaSolicitudBaja.equals("")){%>
						<%=GstDate.getFormatedDateShort("",fechaSolicitudBaja)%>
					<% }else{%>
						&nbsp;
					<% }%>
					</td>
					<td >
					<%if (!fechaValorBaja.equals(""))
						{%>
						&nbsp;<%=fechaValorBaja%>
						
					<% }else{%>
						&nbsp;
					<% }%>
					</td>
					
					<td ><%=estadoInscripcion%></td>
					<td  align="center"><%=registro.get("ESTADOLOGICO")%>&nbsp;</td>
				
				<%}else{%>
				<td ><%=registro.get("ABREVIATURA")%>&nbsp;</td>
				<td ><%=((String)registro.get("NOMBRE"))%>&nbsp;</td>
				<td ><%=registro.get("AREA")%>&nbsp;</td>
				<td ><%=registro.get("MATERIA")%>&nbsp;</td>
				<td ><%=registro.get("ZONA")%>&nbsp;</td>
				<td ><%if (!((String)registro.get("SUBZONA")).equals("")){%><%=registro.get("SUBZONA")%><%}else{%> &nbsp <%}%>&nbsp;</td>
				<td ><%=registro.get("GRUPOFACTURACION")%></td>
				<td align="right"><%=registro.get("NLETRADOS")%>&nbsp;</td>
				<td align="center"><%=registro.get("ESTADO")%>&nbsp;</td>
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
		<% if (!isEntradaSJCS){%>
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
								
								action="<%=action%>"  />
			<%}%>
			
			
			<!-- Check para pasar a modo historico donde se muestran los turnos dados de baja -->
			<div style="position:absolute; left:400px;bottom:50px;z-index:2;">
				<table align="center" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.vertodas"/>
							
							<% if (bIncluirBajaLogica) { %>
								<input type="checkbox" id="bajaLogica"  name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked/>
							<% } else { %>
								<input type="checkbox" id="bajaLogica" name="bajaLogica" onclick="incluirRegBajaLogica(this);"/>
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
				<!--aalg: se controlan los permisos según el modo de acceso  -->
			<% if (!busquedaVolver.equals("volverNo")) { 
			if (accion.equals("editar") || (accion.equals("ver") && usr.isLetrado()==true) ){%>
					<siga:ConjBotonesAccion botones="V,bajaEnTodosLosTurnos,L" clase="botonesDetalle"  />
				<%  } else { %>
					<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
				<%  } %>	
			<%  } else { 
				if (accion.equals("editar")|| (accion.equals("ver") && usr.isLetrado()==true)){%>
					<siga:ConjBotonesAccion botones="bajaEnTodosLosTurnos,L" clase="botonesDetalle"  />
			<%  }} %>
		<% } else { %>
			<% if (!busquedaVolver.equals("volverNo")) { 
			%>
					<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />	
			<%  } %>
		<% } %>
		<% } %>

	
	<script>		
	function solicitarbaja(fila) {
	   	document.FormASolicitarBaja.idInstitucion.value = <%=usr.getLocation()%>;
	   	document.FormASolicitarBaja.idPersona.value = <%=request.getSession().getAttribute("idPersonaTurno")%>;
	   
	    var idTurno = 'oculto' + fila + '_' + 1;
		var fsoli = 'oculto' + fila + '_' + 20;
		var osoli = 'oculto' + fila + '_' + 21;
		var fvali = 'oculto' + fila + '_' + 22;
		var ovali = 'oculto' + fila + '_' + 23;
		var fsolbaja = 'oculto' + fila + '_' + 24;
		var obaja = 'oculto' + fila + '_' + 25;
		var fbaja = 'oculto' + fila + '_' + 27;
		var fechaDenegacion = 'oculto' + fila + '_' + 28;
		var observacionesDenegacion = 'oculto' + fila + '_' + 29;
		var oValbaja = 'oculto' + fila + '_' + 30;
	   	document.FormASolicitarBaja.idTurno.value = document.getElementById(idTurno).value;
	   	document.FormASolicitarBaja.fechaSolicitud.value = document.getElementById(fsoli).value;
	   	document.FormASolicitarBaja.observacionesSolicitud.value 	= document.getElementById(osoli).value;
		document.FormASolicitarBaja.fechaValidacion.value 			= document.getElementById(fvali).value;
		document.FormASolicitarBaja.fechaBaja.value 			= document.getElementById(fbaja).value;
		document.FormASolicitarBaja.observacionesValidacion.value 	= document.getElementById(ovali).value;
		document.FormASolicitarBaja.fechaSolicitudBaja.value 		= document.getElementById(fsolbaja).value;
		document.FormASolicitarBaja.observacionesBaja.value 		= document.getElementById(obaja).value;

	   	document.FormASolicitarBaja.fechaDenegacion.value 		= document.getElementById(fechaDenegacion).value;
		document.FormASolicitarBaja.observacionesDenegacion.value 		= document.getElementById(observacionesDenegacion).value;
	   	document.FormASolicitarBaja.observacionesValBaja.value 		= document.getElementById(oValbaja).value;
	   	
	   	
	   	document.FormASolicitarBaja.modo.value = "sbtConsultaTurno";
	    document.FormASolicitarBaja.target = "submitArea";
	   	// document.FormAValidar.submit();
	   	var resultado = ventaModalGeneral(document.FormASolicitarBaja.name,"G");
	   	if (resultado=='MODIFICADO') {
		  	refrescarLocal();
	 	}	   	
	}	
				
	function consultaInscripcion(fila) {
		document.FormAConsultar.idInstitucion.value = <%=usr.getLocation()%>;
		document.FormAConsultar.idPersona.value = <%=request.getSession().getAttribute("idPersonaTurno")%>;
	   
		var idTurno = 'oculto' + fila + '_' + 1;
		var fsoli = 'oculto' + fila + '_' + 20;
		var osoli = 'oculto' + fila + '_' + 21;
		var fvali = 'oculto' + fila + '_' + 22;
		var ovali = 'oculto' + fila + '_' + 23;
		var fsolbaja = 'oculto' + fila + '_' + 24;
		var obaja = 'oculto' + fila + '_' + 25;
		var fbaja = 'oculto' + fila + '_' + 27;
		var fechaDenegacion = 'oculto' + fila + '_' + 28;
		var observacionesDenegacion = 'oculto' + fila + '_' + 29;
		var oValbaja = 'oculto' + fila + '_' + 30;
		document.FormAConsultar.idTurno.value = document.getElementById(idTurno).value;
	   	document.FormAConsultar.fechaSolicitud.value = document.getElementById(fsoli).value;
	   	document.FormAConsultar.observacionesSolicitud.value 	= document.getElementById(osoli).value;
	    document.FormAConsultar.fechaValidacion.value 			= document.getElementById(fvali).value;
		document.FormAConsultar.fechaBaja.value 			= document.getElementById(fbaja).value;
		document.FormAConsultar.observacionesValidacion.value 	= document.getElementById(ovali).value;
		document.FormAConsultar.fechaSolicitudBaja.value 		= document.getElementById(fsolbaja).value;
		document.FormAConsultar.observacionesBaja.value 		= document.getElementById(obaja).value;
		document.FormAConsultar.observacionesValBaja.value 		= document.getElementById(oValbaja).value;
	   	document.FormAConsultar.fechaDenegacion.value 		= document.getElementById(fechaDenegacion).value;

		document.FormAConsultar.observacionesDenegacion.value 		= document.getElementById(observacionesDenegacion).value;		
		
	   	document.FormAConsultar.modo.value = "consultaInscripcion";
	   	var resultado = ventaModalGeneral(document.FormAConsultar.name,"M");				
	}
				
	function darDeBajaEnTodosLosTurnos(mostrarMensaje) {
					/*
					----ANTES-----
					sub();
					if (mostrarMensaje) {
						var mensaje = "<siga:Idioma key="censo.fichaCliente.turnoInscrito.pregunta.bajaEnTodosLosTurnos"/>";
						if(!confirm(mensaje)) {
							fin();
							return;
						}
					}
					----ANTES-----
					*/
					document.FormASolicitarBaja.idInstitucion.value = <%=usr.getLocation()%>;
	   				document.FormASolicitarBaja.idPersona.value = <%=request.getSession().getAttribute("idPersonaTurno")%>;
					document.FormASolicitarBaja.modo.value	= "busquedaTurnosDisponiblesBaja";
					//document.FormASolicitarBaja.modo.value = "comprobarBajaEnTodosLosTurnos";					
					//document.FormASolicitarBaja.target = "submitArea";
					//document.FormASolicitarBaja.submit();
			   		var resultado = ventaModalGeneral(document.FormASolicitarBaja.name,"G");
				    if(resultado == "MODIFICADO") 
				    	refrescarLocal();
					// refrescarLocal();
	}
				
				
	function buscar() {
					document.FormASolicitar.action		 = "<%=app%>/JGR_DefinirTurnosLetradoAction.do?granotm=<%=System.currentTimeMillis()%>";
			 		document.FormASolicitar.modo.value = "buscarPor";
			 		document.FormASolicitar.submit();
	}
					
	function accionSolicitar() {
					document.FormASolicitar.modo.value	= "busquedaTurnosDisponibles";
			   		var resultado = ventaModalGeneral(document.FormASolicitar.name,"G");
				    if(resultado == "MODIFICADO") 
				    	refrescarLocal();
	}
				
	function refrescarLocal(incluirBajaLogica) {			
					document.DefinirTurnosLetradoForm.action = "<%=app%>/JGR_DefinirTurnosLetrado.do";
					if(incluirBajaLogica){
						document.DefinirTurnosLetradoForm.incluirRegistrosConBajaLogica.value = "S";
					}
					document.DefinirTurnosLetradoForm.submit();
	}
		
				

	// Funcion que agrega el concepto de baja logica
	function incluirRegBajaLogica(o) {
					if (o.checked) {
						document.DefinirTurnosLetradoForm.incluirRegistrosConBajaLogica.value = "S";
					} else {
						document.DefinirTurnosLetradoForm.incluirRegistrosConBajaLogica.value = "N";
					}
					document.DefinirTurnosLetradoForm.accion.value = "<%=accion%>";
					document.DefinirTurnosLetradoForm.modo.value = "abrirTurnosLimpiar";
					document.DefinirTurnosLetradoForm.submit();
	}
	
	function mostrarFecha() {		
		if(document.getElementById('fechaConsulta')){
			if(document.DefinirTurnosLetradoForm.fechaConsulta && document.DefinirTurnosLetradoForm.fechaConsulta.value!=''&& document.DefinirTurnosLetradoForm.fechaConsulta.value!='sysdate'){
				document.getElementById('fechaConsulta').value = document.DefinirTurnosLetradoForm.fechaConsulta.value;
			}else{
				fechaActual = getFechaActualDDMMYYYY();
				document.getElementById('fechaConsulta').value = fechaActual;
				document.DefinirTurnosLetradoForm.fechaConsulta.value = fechaActual;
			}
		}
	}
	
	function accionCalendario() {
			// Abrimos el calendario 

			if (document.getElementById('fechaConsulta').value!='') {
				document.DefinirTurnosLetradoForm.fechaConsulta.value =document.getElementById('fechaConsulta').value;
				 document.DefinirTurnosLetradoForm.modo.value = 'abrir';
				 document.DefinirTurnosLetradoForm.submit();
				
		 	}else{
					if(document.DefinirTurnosLetradoForm.fechaConsulta.value==''){
						fechaActual = getFechaActualDDMMYYYY();
						document.getElementById('fechaConsulta').value = fechaActual;
						document.DefinirTurnosLetradoForm.fechaConsulta.value = fechaActual;
						document.DefinirTurnosLetradoForm.modo.value = 'abrirTurnosPaginados';
						document.DefinirTurnosLetradoForm.submit();
					}
			} 
	}
	</script>	
		<html:form action="/JGR_DefinirTurnosLetrado.do" method="post" target="">
			<input type="hidden" name="incluirRegistrosConBajaLogica" value="<%=bIncluirBajaLogica%>">
			<html:hidden property="modo"  styleId="modo"  value="" />
			<html:hidden property="fechaConsulta"  styleId="fechaConsulta"/>
			<html:hidden styleId="accion"  property="accion" />
		</html:form>	
		
		<html:form action="/JGR_AltaTurnosGuardias" name="FormASolicitar" type ="com.siga.gratuita.form.InscripcionTGForm" styleId="FormASolicitar">
			<html:hidden styleId="modo"  property="modo" />
			<html:hidden styleId="idInstitucion"  property="idInstitucion" />
			<html:hidden styleId="idPersona"  property="idPersona"  />
			<html:hidden styleId="idTurno" property="idTurno" />
			<html:hidden styleId="fechaSolicitud"  property="fechaSolicitud" />
			<html:hidden styleId="observacionesSolicitud"  property="observacionesSolicitud" />
			<html:hidden styleId="fechaValidacion" property="fechaValidacion" />
			<html:hidden styleId="observacionesValidacion"  property="observacionesValidacion" />
			<html:hidden styleId="fechaSolicitudBaja"  property="fechaSolicitudBaja"  />
			<html:hidden styleId="observacionesBaja"  property="observacionesBaja" />
			<html:hidden styleId="fechaBaja"  property="fechaBaja" />
			<html:hidden styleId="observacionesValBaja"   property="observacionesValBaja" />
			<html:hidden styleId="observacionesDenegacion" property="observacionesDenegacion" />
			<html:hidden styleId="fechaDenegacion"  property="fechaDenegacion" />
			<html:hidden styleId="estadoPendientes" property="estadoPendientes"/>
			<input type="hidden" name="actionModal" id="actionModal" />
	</html:form>
	<html:form action="/JGR_SolicitarBajaTurno" name="FormASolicitarBaja" type ="com.siga.gratuita.form.InscripcionTGForm" styleId="FormASolicitarBaja">
			<html:hidden styleId="modo"  property="modo"/>
			<html:hidden styleId="idInstitucion"  property="idInstitucion" />
			<html:hidden styleId="idPersona" property="idPersona" />
			<html:hidden styleId="idTurno" property="idTurno" />
			<html:hidden styleId="fechaSolicitud" property="fechaSolicitud" />
			<html:hidden styleId="observacionesSolicitud"  property="observacionesSolicitud" />
			<html:hidden styleId="fechaValidacion"  property="fechaValidacion" />
			<html:hidden styleId="observacionesValidacion"   property="observacionesValidacion" />
			<html:hidden styleId="fechaSolicitudBaja"  property="fechaSolicitudBaja" />
			<html:hidden styleId="observacionesBaja"  property="observacionesBaja" />
			<html:hidden styleId="fechaBaja" property="fechaBaja" />
			<html:hidden styleId="observacionesValBaja" property="observacionesValBaja" />
			<html:hidden styleId="observacionesDenegacion"  property="observacionesDenegacion" />
			<html:hidden styleId="fechaDenegacion"  property="fechaDenegacion" />
			<html:hidden styleId="estadoPendientes" property="estadoPendientes"/>
			<input type="hidden" name="actionModal" id="actionModal"  />
	</html:form>
	
	<html:form action="/JGR_BajaTurnos" name="FormAConsultar" type ="com.siga.gratuita.form.InscripcionTGForm" styleId="FormAConsultar">
			<html:hidden styleId="modo"  property="modo"/>
			<html:hidden styleId="idInstitucion"  property="idInstitucion" />
			<html:hidden styleId="idPersona"  property="idPersona" />
			<html:hidden styleId="idTurno" property="idTurno" />
			<html:hidden styleId="fechaSolicitud"  property="fechaSolicitud" />
			<html:hidden styleId="observacionesSolicitud"  property="observacionesSolicitud" />
			<html:hidden styleId="fechaValidacion"  property="fechaValidacion" />
			<html:hidden styleId="observacionesValidacion"  property="observacionesValidacion" />
			<html:hidden styleId="fechaSolicitudBaja" property="fechaSolicitudBaja" />
			<html:hidden styleId="observacionesBaja"  property="observacionesBaja" />
			<html:hidden styleId="fechaBaja" property="fechaBaja"/>
			<html:hidden styleId="observacionesDenegacion"  property="observacionesDenegacion" />
			<html:hidden styleId="observacionesValBaja"  property="observacionesValBaja" />
			<html:hidden styleId="fechaDenegacion"  property="fechaDenegacion" />
			<html:hidden styleId="estadoPendientes" property="estadoPendientes"/>
			<input type="hidden" name="actionModal" id="actionModal" />
	</html:form>
	
		
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

		</table>		
	</body>
</html>