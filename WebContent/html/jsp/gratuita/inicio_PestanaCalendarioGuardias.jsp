<!DOCTYPE html>
<html>
<head>
<!-- inicio_PestanaCalendarioGuardias.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.ScsGuardiasColegiadoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsCabeceraGuardiasBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.Utilidades.paginadores.Paginador"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP -->
<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	String idioma=usr.getLanguage().toUpperCase();

	//Datos de la pantalla anterior
	String idinstitucionpestanha="", idpersonapestanha="", modopestanha="";
	idinstitucionpestanha = (String)request.getAttribute("IDINSTITUCION");
	idpersonapestanha = (String)request.getAttribute("IDPERSONA");	
	modopestanha = request.getSession().getAttribute("modoPestanha")==null?"EDITAR":(String)request.getSession().getAttribute("modoPestanha");
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	String atributoPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(atributoPaginador)!=null){
		hm = (HashMap)ses.getAttribute(atributoPaginador);
	
		if (hm.get("datos")!=null && !hm.get("datos").equals("")) {
			resultado = (Vector)hm.get("datos");
			Paginador paginador = (Paginador)hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());	
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());	
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	
		} else {
			resultado =new Vector();
			paginaSeleccionada = "0";	
			totalRegistros = "0";	
			registrosPorPagina = "0";
		}
		
	} else {
		resultado =new Vector();
		paginaSeleccionada = "0";	
		totalRegistros = "0";	
		registrosPorPagina = "0";
	}	 
	Vector registros = resultado;
	
	//Datos del Colegiado si procede:
	String nombrePestanha = (String)request.getAttribute("NOMBRECOLEGPESTA�A");
	String numeroPestanha = (String)request.getAttribute("NUMEROCOLEGPESTA�A");
	String estadoColegial= (String)request.getAttribute("ESTADOCOLEGIAL");
	String validarVolante= (String)request.getAttribute(ClsConstants.GEN_PARAM_VALIDAR_VOLANTE);
	String action=app+"/JGR_PestanaCalendarioGuardias.do?noReset=true&nColegiado="+numeroPestanha;

	//Almaceno en el request los parametros de la pestanha:
	request.setAttribute("NOMBRECOLEGPESTA�A",nombrePestanha);
	request.setAttribute("NUMEROCOLEGPESTA�A",numeroPestanha);

	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
		
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	String idinstitucion = usr.getLocation();
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:TituloExt titulo="censo.fichaCliente.sjcs.calendarioGuardias.cabecera" localizacion="censo.fichaCliente.sjcs.calendarioGuardias.localizacion" />
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="tablaCentralCampos">
<% //Entrada desde el menu de Censo:
	if (entrada.equalsIgnoreCase("2")) { 
%>
		<table class="tablaTitulo" align="center" cellspacing=0>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.calendarioGuardias.pestana.titulito" />
					&nbsp;&nbsp;
					<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
<%
					if (numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) {
						if (estadoColegial!=null && !estadoColegial.equals("")) {
%> 
							<siga:Idioma key="censo.fichaCliente.literal.colegiado" /> 
							<%= UtilidadesString.mostrarDatoJSP(numeroPestanha)%> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>) 
<%
						} else {
%>
							(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial" />)
<%
						}
					} else { 
%> 
						<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> 
<% 
					} 
%>
				</td>
			</tr>
		</table>
<%
	}
%>

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<html:form action="/JGR_PestanaCalendarioGuardias.do" method="POST" styleId="PermutasForm" target="submitArea" style="display:none">
		<html:hidden styleId="modo"  property="modo" value="<%=modopestanha%>" />
		<html:hidden styleId="orden"  property="orden" value="FECHA" />
		<html:hidden styleId="idInstitucion" property="idInstitucion" value="<%=idinstitucionpestanha%>" />
		<html:hidden styleId="idPersona"  property="idPersona" value="<%=idpersonapestanha%>" />
		<html:hidden styleId="actionModal"  property="actionModal" value="M" />
		<html:hidden styleId="modoPestanha" property="modoPestanha" value="<%=modopestanha%>" />

		<!-- Datos del Colegiado seleccionado -->
		<html:hidden styleId="nombreColegiadoPestanha" property="nombreColegiadoPestanha" value="<%=nombrePestanha%>" />
		<html:hidden styleId="numeroColegiadoPestanha" property="numeroColegiadoPestanha" value="<%=numeroPestanha%>" />

		<!-- Datos del elemento seleccionado -->
		<html:hidden styleId="idCalendarioGuardias"  property="idCalendarioGuardias" value="" />
		<html:hidden styleId="idTurno"  property="idTurno" value="" />
		<html:hidden styleId="idGuardia"  property="idGuardia" value="" />
		<html:hidden styleId="fechaInicio"  property="fechaInicio" value="" />
		<html:hidden styleId="fechaFin"  property="fechaFin" value="" />
		<html:hidden styleId="reserva" property="reserva" value="" />
	</html:form>
		
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" styleId="DefinirCalendarioGuardiaForm" target="submitArea">
		<html:hidden styleId="usuMod"  property="usuMod" value="<%=usr.getUserName()%>" />
		<html:hidden styleId="modo"  property="modo" value="" />
		<html:hidden styleId="accion" property="accion"  value="" />
		<html:hidden styleId="idCalendarioGuardias" property="idCalendarioGuardias" value="" />
		<html:hidden styleId="idInstitucion"  property="idInstitucion" value="<%=idinstitucion%>" />
		<html:hidden styleId="idTurno"  property="idTurno"value="" />
		<html:hidden styleId="idGuardia"  property="idGuardia" value="" />
		<html:hidden styleId="diasACobrar"  property="diasACobrar" value="" />
		<html:hidden styleId="diasGuardia"  property="diasGuardia" value="" />
		<html:hidden styleId="tipoDias"  property="tipoDias" value="" />
		<html:hidden styleId="idPersona"  property="idPersona" value="<%=idpersonapestanha%>" />
		<html:hidden styleId="actionModal"  property="actionModal" value="" />
		<html:hidden styleId="tablaDatosDinamicosD"  property="tablaDatosDinamicosD" value="" />
		<html:hidden styleId="filaSelD"  property="filaSelD" value="" />
	</html:form>
	
	<%
		String nombreCol = "gratuita.inicio_PestanaCalendarioGuardias.literal.fechaInicio," +
						   "gratuita.inicio_PestanaCalendarioGuardias.literal.fechaFin," +
						   "gratuita.inicio_PestanaCalendarioGuardias.literal.turno," +
						   "gratuita.inicio_PestanaCalendarioGuardias.literal.guardia," +
						   "gratuita.inicio_PestanaCalendarioGuardias.literal.tipodias," +
						   "gratuita.inicio_PestanaCalendarioGuardias.literal.estado,";
		String tamanioCol = "8,8,17,15,15,17,10";
	%>

	<c:if test="${PermutasForm.validaGuardiasColegiado==1}">
		<%nombreCol = "gratuita.busquedaVolantesGuardias.literal.val," + nombreCol; %>
		<%tamanioCol = "5,"+tamanioCol; %>
	</c:if>

	<siga:Table 
		name="listado" 
		border="1" 
		columnNames="<%=nombreCol%>" 
		columnSizes="<%=tamanioCol%>">
						
<%
		if ((registros!= null) && (registros.size()>0)) { 
			int recordNumber=1;
			String fechaInicio="", fechaFin="", idcalendarioguardias="", idturno="", idguardia="", reserva="";
			String turno="", guardia="", tipodias="", estado="", nomFacturacion="";
			String validado ;
			String facturado;
			String numActuacionesValidadas;
			while ((recordNumber) <= registros.size()) {	 	
				Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable hash = (Hashtable) fila.getRow();
					
				//Campos ocultos por cada fila del Confirmador:
				//1- IDCALENDARIOGUARDIAS
				//2- IDTURNO
				//3- IDGUARDIA
				//4- FECHAINICIO					
				//5- RESERVA
				//6- FECHA FIN
				
				//Campos visibles por cada fila:
				//1- FECHAINICIO
				//2- FECHAFIN
				//3- TURNO
				//4- GUARDIA
				//5- TIPODIAS
				//6- ESTADO					
				
				 //Datos ocultos:
				idcalendarioguardias = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
				idturno = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDTURNO);
				idguardia = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDGUARDIA);
				fechaInicio = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_INICIO);
				reserva = UtilidadesHash.getString(hash,ScsGuardiasColegiadoBean.C_RESERVA);
				fechaFin = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_FIN);
				validado = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_VALIDADO);
				facturado = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FACTURADO);
				
				numActuacionesValidadas = UtilidadesHash.getString(hash,"ACT_VALIDADAS");
				
				if(numActuacionesValidadas.equals(""))
					numActuacionesValidadas = "0";
				
				//Datos visibles:
				turno = UtilidadesHash.getString(hash,"TURNO");
				guardia = UtilidadesHash.getString(hash,"GUARDIA");
				tipodias = ScsGuardiasTurnoAdm.obtenerTipoDia (
					UtilidadesHash.getString(hash,"SELECCIONLABORABLES"), 
					UtilidadesHash.getString(hash,"SELECCIONFESTIVOS"), 
					usr);
				estado = UtilidadesHash.getString(hash,"ESTADO");
				nomFacturacion = UtilidadesHash.getString(hash,"NOMBREFACTURACION");

				//Botones
				FilaExtElement[] elems=new FilaExtElement[4];
				elems[0]=null; // Permutar
				elems[1]=null; // Confirmar
				elems[2]=null; // Sustituir
				elems[3]=new FilaExtElement("masinformacion","masinformacion",SIGAConstants.ACCESS_FULL);
				
				/*Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
				- RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
				-- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
				-- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
				-- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
				-- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
				-- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia */ 				
				String funcionPermutas = UtilidadesHash.getString(hash, "FUNCIONPERMUTAS");
				String accionSustituir = funcionPermutas.substring(0, 1);
				String accionAnular = funcionPermutas.substring(1, 2);
				String accionBorrar = funcionPermutas.substring(2, 3);
				String accionPermutar = funcionPermutas.substring(3, 4);
				String tieneAsistencias = funcionPermutas.substring(4, 5);				
				
				//SI ES LETRADO
				if (usr.isLetrado()) {
					if (accionPermutar!=null && accionPermutar.equalsIgnoreCase("S")) {
						elems[0]=new FilaExtElement("permutar","permutar",SIGAConstants.ACCESS_FULL);						
					} else if (accionPermutar!=null && accionPermutar.equalsIgnoreCase("P")) {
						elems[1]=new FilaExtElement("confirmar","confirmar",SIGAConstants.ACCESS_FULL);
					}
					
				} else if (!modopestanha.equalsIgnoreCase("VER")) {
					if (accionPermutar!=null && accionPermutar.equalsIgnoreCase("S")) {
						elems[0]=new FilaExtElement("permutar","permutar",SIGAConstants.ACCESS_FULL);						
					} else if (accionPermutar!=null && accionPermutar.equalsIgnoreCase("P")) {
						elems[1]=new FilaExtElement("confirmar","confirmar",SIGAConstants.ACCESS_FULL);
					}
					
					if (accionSustituir!=null && accionSustituir.equalsIgnoreCase("S"))
						elems[2]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
				}
%>
				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>'
					botones="" 
					elementos='<%=elems%>' 
					clase="listaNonEdit"
					visibleEdicion="no" 
					visibleBorrado="no" 
					visibleConsulta="no"
					pintarEspacio="no">
						
					<c:if test="${PermutasForm.validaGuardiasColegiado==1}">
						<td align="center">
							<input type="checkbox" name="chkVal"
								value="<%=idinstitucion+"@@"+idturno+"@@"+idguardia+"@@"+idcalendarioguardias+"@@"+idpersonapestanha+"@@"+fechaInicio+"@@"+fechaInicio %>"
								<%=(validado.equals("1"))?"checked":""%>
								<%=(Integer.valueOf(numActuacionesValidadas)>0 || facturado.equals("1"))?"disabled":""%>>
						</td>
					</c:if>
						
					<td align="center">
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idcalendarioguardias%>' />
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idturno%>' />
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idguardia%>'/>
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=fechaInicio%>' />
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_5' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=reserva%>' />
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_6' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=fechaFin%>' />
						<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_ASI' name='oculto<%=String.valueOf(recordNumber)%>_ASI' value='<%=tieneAsistencias%>' />
						<input type="checkbox" name="chkValOld"
							value="<%=idinstitucion+"@@"+idturno+"@@"+idguardia+"@@"+idcalendarioguardias+"@@"+idpersonapestanha+"@@"+fechaInicio+"@@"+fechaInicio %>"
							<%=(validado.equals("1"))?"checked":""%> disabled
							style="display: none"> 
						<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>
					</td>
				
					<td align="center"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%></td>
					<td align="center"><%=turno%></td>
					<td align="center"><%=guardia%></td>
					<td align="center"><%=tipodias%></td>
					<td align="center">									
<%
						String descripcion = "";
						if (validado.equals("1")) {							
							switch (Integer.parseInt(estado)) {
								case 1: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado1"; break;
								case 2: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado2"; break;
								case 3: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado3"; break;
								case 4: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado4"; break;
								case 5: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado5"; break;
								case 6: descripcion="gratuita.inicio_PestanaCalendarioGuardias.literal.estado6"; break;
								default: descripcion=""; break;
							}
						} else {
							// jbd // Comprobar parametro VALIDAR_VOLANTE, si es S deber�a poner No validada, si es N deberia poner Anulada
							if(validarVolante.equalsIgnoreCase("N")){
								descripcion = "gratuita.inicio_PestanaCalendarioGuardias.literal.anulada";
							}else if(validarVolante.equalsIgnoreCase("S")){
								descripcion = "gratuita.inicio_PestanaCalendarioGuardias.literal.noValidada";
							}
						}
						
						if (!descripcion.equals("")) {
							if (descripcion.equals("gratuita.inicio_PestanaCalendarioGuardias.literal.estado6")) {
%>
								<siga:Idioma key="<%=descripcion%>" /><%= " - " + nomFacturacion %>
<% 
							} else {
%>
								<siga:Idioma key="<%=descripcion%>" />
<% 
							}
						} 
%>
					</td>
				</siga:FilaConIconos>				
<% 
				recordNumber++; 
	 		} // while 
		} else { 
%>
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		}
%>
	</siga:Table>

	<div style="position:absolute; left:400px;bottom:0px;z-index:99;">
		<table align="center" class="botonesSeguido">
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.ordenacion" />:	&nbsp; 
					<!-- Combo de ordenacion--> 
					<select id="orden" name="orden" onchange="refrescarLocalThis(this)" class="boxCombo">
						<option value="">&nbsp;</option>
						<option value="FECHA"><siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha" /></option>
						<option value="TURNO"><siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.guardia" /></option>
					</select>
				</td>
			</tr>
		</table>
	</div>

	<html:form action="/JGR_ValidarVolantesGuardias.do" method="POST" target="submitArea" styleId="ValidarVolantesGuardiasForm">
		<html:hidden name="ValidarVolantesGuardiasForm" property="modo" value="" />
		<html:hidden name="ValidarVolantesGuardiasForm" property="datosValidar" value="" />
		<html:hidden name="ValidarVolantesGuardiasForm" property="datosBorrar" value="" />
	</html:form>

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		// Funcion asociada la busqueda del refresco
		function refrescarLocal() {			
			document.forms[0].target = "_self";		
			document.forms[0].orden.value = document.getElementById('orden').value;
			document.forms[0].modo.value = "abrir";
			document.forms[0].submit();
		}
	
		function refrescarLocalThis(objeto) {			
			document.forms[0].target = "_self";		
			document.forms[0].orden.value = objeto.value;
			document.forms[0].modo.value = "abrir";
			document.forms[0].submit();
		}		

		function accionGuardar() {		
			sub();
			var datosvalidar="";
			var datosborrar="";
			var checks = document.getElementsByName("chkVal");
			var checksOld = document.getElementsByName("chkValOld");
			if (checks.type != 'checkbox') {
				for (var i = 0; i < checks.length; i++){
					if (checks[i].disabled==false && checks[i].checked==true) {
						if (checksOld[i].checked==false) {
							datosvalidar += checks[i].value + "##";		
						}
					}
					if (checks[i].disabled==false && checks[i].checked==false) {
						if (checksOld[i].checked==true) {
							datosborrar += checks[i].value + "##";
						}		
					}
				}	
			}
			if (datosvalidar.length>2) {
				datosvalidar=datosvalidar.substring(0,datosvalidar.length-2);
			}
			if (datosborrar.length>2) {
				datosborrar=datosborrar.substring(0,datosborrar.length-2);
			}
			if (trim(datosvalidar)!="" || trim(datosborrar)!="") {
				document.ValidarVolantesGuardiasForm.datosValidar.value=datosvalidar;
				document.ValidarVolantesGuardiasForm.datosBorrar.value=datosborrar;
				document.ValidarVolantesGuardiasForm.modo.value="insertar";
				document.ValidarVolantesGuardiasForm.submit();			
			} else {
				fin();
			}
		}
	
		//Guardo los campos seleccionados
		function seleccionarFila(fila) {
		    var idcalendario = 'oculto' + fila + '_' + 1;
		    var idturno = 'oculto' + fila + '_' + 2;
		    var idguardia = 'oculto' + fila + '_' + 3;
		    var fechainicio = 'oculto' + fila + '_' + 4;
		    var reserva = 'oculto' + fila + '_' + 5;
		    var fechafin = 'oculto' + fila + '_' + 6;
		
			//Datos del elemento seleccionado:
			document.forms[0].idCalendarioGuardias.value = document.getElementById(idcalendario).value;
			document.forms[0].idTurno.value = document.getElementById(idturno).value;
			document.forms[0].idGuardia.value = document.getElementById(idguardia).value;
			document.forms[0].fechaInicio.value = document.getElementById(fechainicio).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafin).value;
			document.forms[0].reserva.value = document.getElementById(reserva).value;		
		}

		// Funcion asociada al boton Cambiar
		function permutar(fila) {		
			//Datos del elemento seleccionado:
			seleccionarFila(fila);
			
			var tieneAsistencias = 'oculto' + fila + '_ASI';
			var valorTieneAsistencias = document.getElementById(tieneAsistencias).value;
			var confirmado = true;
			
			if (valorTieneAsistencias == 'S') {
				confirmado = false;
				if (confirm("<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias'/>")) {
					confirmado = true;
				}
			}			
			
			if (confirmado == true)	{			
				document.forms[0].modo.value = "buscarPor";
				var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
				if (salida == "MODIFICADO"){
					refrescarLocal();
				}
			}
		}

		// Funcion asociada al boton Cambiar
		function confirmar(fila) {		
			//Datos del elemento seleccionado:
			seleccionarFila(fila);

			document.forms[0].modo.value = "abrirAvanzada";
			var salida = ventaModalGeneral(document.forms[0].name,"G"); 			
			if (salida == "MODIFICADO") {
				refrescarLocal();			
			}
		}
	
		function sustituir(fila) {		
			//Datos del elemento seleccionado:
			seleccionarFila(fila);
			
			var tieneAsistencias = 'oculto' + fila + '_ASI';
			var valorTieneAsistencias = document.getElementById(tieneAsistencias).value;
			var confirmado = true;
			
			if (valorTieneAsistencias == 'S') {
				confirmado = false;
				if (confirm("<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias'/>")) {
					confirmado = true;
				}
			}			
			
			if (confirmado == true)	{			
				document.forms[0].modo.value = "sustituir";
				var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
				if (salida == "MODIFICADO") {
					refrescarLocal();			
				}
			}
		}
	
		function masinformacion(fila) {
			selectRow(fila);
		
			consultar2(fila, document.forms[1]);
			document.forms[1].modo.value = "ver";
			document.forms[1].accion.value = "modalConsultaCenso";
			document.forms[1].submit();
			var salida = ventaModalGeneral(document.forms[1].name,"G"); 			
			if (salida == "MODIFICADO") {
				window.top.close();	
			}
		}
	
		function consultar2(fila, formulario) {
			var datos = jQuery(formulario).find("#tablaDatosDinamicosD");
			if (datos != undefined)
				preparaDatos(fila, "listado", datos[0]);
			else
				preparaDatos(fila, "listado");
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<%
	if ( hm.get("datos")!=null && !hm.get("datos").equals("")) {
%>
	<siga:Paginador totalRegistros="<%=totalRegistros%>"
		registrosPorPagina="<%=registrosPorPagina%>"
		paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
		modo="buscarInit" clase="paginator"
		divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
		distanciaPaginas="" action="<%=action%>" />
<%
	}

	String botones = "";
	if (!busquedaVolver.equals("volverNo")) { 
		botones = ""; 
	} 
%>
		
	<c:if test="${PermutasForm.validaGuardiasColegiado==1}">
<% 	
		if (botones.equals("V")) 
			botones = "G";
		else
			botones = "G";
%>
	</c:if>
		
	<siga:ConjBotonesAccion botones="<%=botones.toString()%>" clase="botonesDetalle" />

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	<%@ include file="/html/jsp/censo/includeVolver.jspf"%>
</body>
</html>