<!-- listarGuardiasLetrado.jsp -->

<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->

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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="java.util.*"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");

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
	
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	String botones="C";
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt titulo="censo.fichaCliente.sjcs.guardias.cabecera" localizacion="censo.fichaCliente.sjcs.guardias.localizacion" />
		<!-- FIN: TITULO Y LOCALIZACION -->
	</head>

	<body class="tablaCentralCampos" onload="mostrarFecha();">
		<bean:define id="bIncluirBajaLogica" property="bajaLogica" name="DefinirGuardiasLetradoForm"></bean:define>
	
		<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { 
			if(bIncluirBajaLogica.equals("S"))
		%>
			<table class="tablaTitulo" cellspacing="0">
				<tr>
					<td class="titulitosDatos">
						<siga:Idioma key="censo.fichaCliente.guardiasInscrito.pestana.titulito" />&nbsp;&nbsp;
						<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
						<% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<%if (estadoColegial!=null && !estadoColegial.equals("")){%> 
								<siga:Idioma key="censo.fichaCliente.literal.colegiado" /> 
								<%= UtilidadesString.mostrarDatoJSP(numeroPestanha)  %>
								&nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>) <%}else{%>
								(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial" />)
							<%}%> 
						<% } else { %> 
							<siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> 
						<% } %>
					</td>
				</tr>	
			</table>
		<% } %>

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="DefinirGuardiasTurnosAction.do" method="post" target="mainPestanas">
			<html:hidden property="modo" styleId="modo" value="" />
			<!-- Datos del Colegiado seleccionado -->
			<html:hidden  property="nombreColegiadoPestanha"  styleId="nombreColegiadoPestanha" value="<%=nombrePestanha%>" />
			<html:hidden property="numeroColegiadoPestanha"  styleId="numeroColegiadoPestanha" value="<%=numeroPestanha%>" />
			<html:hidden property="actionModal" styleId="actionModal" value="M" />
			<html:hidden property="bajaLogica" styleId="bajaLogica"  value="N" />

			<table>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasLetrado.literal.textoInscripcion" />
					</td>
				</tr>
			</table>
		
			<siga:ConjCampos leyenda="gratuita.busquedaSJCS.literal.filtro">
				<table>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.fechaConsulta" />
						</td>
						<td>
							<siga:Fecha  nombreCampo= "fechaConsulta" postFunction="accionCalendario();"/>						
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
		</html:form>
		
		<siga:TablaCabecerasFijas 
			nombre="tablaDatos" 
			borde="1"
			clase="tableTitle"
			nombreCol="gratuita.listarGuardias.literal.turno,gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardias.literal.fechainscripcion,Fecha Valor,Fecha Solicitud Baja,gratuita.listarGuardiasTurno.literal.fechaBaja,Estado,"
			tamanoCol="15,15,10,8,6,8,8,8,8,7,8" 
			alto="100%"
			ajuste="10">
			
			<% if (obj.size()>0) { 
	    		int recordNumber=1;
				while ((recordNumber) <= obj.size()) {	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					String obligatoriedad 		= (String)hash.get("OBLIGATORIEDAD");
					String tipoDia = (String)hash.get("TIPODIASGUARDIA");
					
					String fechaSolicitud 		= (String) hash.get("FECHAINSCRIPCION");
					String fechaValidacion 		= (String) hash.get("FECHAVALIDACION");
					String fechaSolicitudBaja 	= (String) hash.get("FECHASOLICITUDBAJA");
					String fechaBaja 			= (String) hash.get("FECHABAJA");
					String fechaDenegacion 		= (String) hash.get("FECHADENEGACION");
					String fechaValor 		= (String) hash.get("FECHAVALOR");
					
					if(fechaSolicitud == null) 		fechaSolicitud 	= "";
					if(fechaValidacion == null) 	fechaValidacion = "";
					if(fechaBaja == null) 			fechaBaja 		= "";
					if(fechaSolicitudBaja == null) 	fechaSolicitudBaja = "";
					if(fechaDenegacion == null) 	fechaDenegacion = "";
					if(fechaValor == null) 	fechaValor = "";				
	
					String literalValidar="";
					FilaExtElement[] elems = elems = new FilaExtElement[1];
					elems[0]=new FilaExtElement("consultaInscripcion","consultaInscripcion",SIGAConstants.ACCESS_FULL);				
					
					if ((fechaBaja==null || fechaBaja.equals(""))&&(hash.get("VALIDACIONTURNO")==null || ((String)hash.get("VALIDACIONTURNO")).equals(""))){
					  literalValidar="gratuita.altaTurnos.literal.validarTurno";
					  
					}				
					String literalDuracion ="gratuita.altaTurnos_2.literal.dias";
					if(tipoDia.equalsIgnoreCase("D"))
						literalDuracion = "gratuita.altaTurnos_2.literal.dias";
					else if(tipoDia.equalsIgnoreCase("S"))
							literalDuracion = "gratuita.altaTurnos_2.literal.semanas";
						 else if(tipoDia.equalsIgnoreCase("M"))
								literalDuracion = "gratuita.altaTurnos_2.literal.meses";
							  else if(tipoDia.equalsIgnoreCase("Q"))
									 literalDuracion = "gratuita.altaTurnos_2.literal.quincenas";				
					String estado = "No aplica";
					
					if(fechaValidacion.equals("")){
						if(fechaSolicitudBaja.equals("")){
							if(fechaDenegacion.equals("")){
								estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.pendiente");
								// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
							}else{
								estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.denegada");
							}
							
						}else{
							if(fechaBaja.equals("")){
								if(fechaDenegacion.equals("")){
									estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.pendiente");
								}else{
									// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
									estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.denegada");
								}							
							}else{
								estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.confirmada");							
							}						
						}
						
					}else{					
						if(fechaSolicitudBaja.equals("")){
							estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.alta.confirmada");;
							// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
						}else{
							if(fechaBaja.equals("")){
								if(fechaDenegacion.equals("")){
									estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.pendiente");
								}else{
									// elems[1]=new FilaExtElement("solicitarbaja","solicitarbaja",SIGAConstants.ACCESS_FULL);
									estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.denegada");
								}						
							}else{
								estado =UtilidadesString.getMensajeIdioma(usr,"gratuita.gestionInscripciones.estado.baja.confirmada");							
							}						
						}				
					}%>
					
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>'
						botones="<%=botones%>" clase="listaNonEdit" visibleBorrado="false"
						visibleEdicion="false" pintarEspacio="false" elementos='<%=elems%>'>
			
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDTURNO")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGUARDIA")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("IDPERSONA")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=hash.get("FECHAVALIDACION")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_5' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=hash.get("FECHAINSCRIPCION")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_6' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=hash.get("FECHABAJA")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_7' name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=hash.get("FECHASOLICITUDBAJA")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_8' name='oculto<%=String.valueOf(recordNumber)%>_8' value='<%=hash.get("OBSERVACIONESVALIDACION")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_9' name='oculto<%=String.valueOf(recordNumber)%>_9' value='<%=hash.get("OBSERVACIONESSUSCRIPCION")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_10' name='oculto<%=String.valueOf(recordNumber)%>_10' value='<%=hash.get("OBSERVACIONESBAJA")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_11' name='oculto<%=String.valueOf(recordNumber)%>_11' value='<%=hash.get("FECHADENEGACION")%>'>
						<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_12' name='oculto<%=String.valueOf(recordNumber)%>_12' value='<%=hash.get("OBSERVACIONESDENEGACION")%>'>
			
						<td><%=hash.get("TURNO")%></td>
						<td><%=hash.get("GUARDIA")%></td>
						<td>
							<%if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("0")){%>Obligatorias<%}else if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("1")){%>
								Todas o Ninguna
							<%}else{%>
								A elección
							<%}%>
						</td>
						<td><%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr)%></td>
						<td><%=hash.get("DURACION")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>"/></td>
						<td>&nbsp;<%=GstDate.getFormatedDateShort("",fechaSolicitud)%><siga:Idioma key="<%=literalValidar%>"/></td>
						<td>&nbsp;<%=GstDate.getFormatedDateShort("",fechaValor)%></td>
						<td>&nbsp;<%=GstDate.getFormatedDateShort("",fechaSolicitudBaja)%></td>
						<td>&nbsp;<%=GstDate.getFormatedDateShort("",fechaBaja)%></td>
						<td>&nbsp;<%=estado%></td>
					</siga:FilaConIconos>
					<%recordNumber++;%>
				<%}%>
				
			<% } else { %>
				<br>
				<p class="titulitos" style="text-align: center">
					<siga:Idioma key="messages.noRecordFound" />
				</p>
				<br>
			<% } %>
		</siga:TablaCabecerasFijas>

		<!-- Check para pasar a modo historico donde se muestran los turnos dados de baja -->
		<div style="position: absolute; left: 400px; bottom: 25px; z-index: 2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.gestionInscripciones.vertodas" /> 
						<% if (bIncluirBajaLogica.equals("S")) { %>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked> 
						<% } else { %>
							<input type="checkbox" name="bajaLogica" onclick="incluirRegBajaLogica(this);"> 
						<% } %>
					</td>
				</tr>
			</table>
		</div>

		<html:form action="/JGR_ListarGuardiasLetrado.do" method="post" target="">
			<input type="hidden" name="actionModal" value="" />
			<html:hidden name="DefinirGuardiasLetradoForm" property="bajaLogica" />
			<html:hidden name="DefinirGuardiasLetradoForm" property="fechaConsulta" />
			<html:hidden name="DefinirGuardiasLetradoForm" property="modo" />
		</html:form>

		<html:form action="/JGR_BajaTurnos" name="FormAConsultar" styleId="FormAConsultar" type="com.siga.gratuita.form.InscripcionTGForm">
			<html:hidden property="modo" />
			<html:hidden property="idInstitucion" />
			<html:hidden property="idPersona" />
			<html:hidden property="idTurno" />
			<html:hidden property="fechaSolicitud" />
			<html:hidden property="observacionesSolicitud" />
			<html:hidden property="fechaValidacion" />
			<html:hidden property="observacionesValidacion" />
			<html:hidden property="fechaSolicitudBaja" />
			<html:hidden property="observacionesBaja" />
			<html:hidden property="fechaBaja" />
			<html:hidden property="observacionesDenegacion" />
			<html:hidden property="fechaDenegacion" />
			<html:hidden property="estadoPendientes" />
			<input type="hidden" name="actionModal" />
		</html:form>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
		
			//Guardo los campos seleccionados
			function seleccionarFila(fila){
		    	var idturno = 'oculto' + fila + '_' + 1;
			    var idguardia = 'oculto' + fila + '_' + 2;
			    var idpersona = 'oculto' + fila + '_' + 3;
			    
				//Datos del elemento seleccionado:
				document.forms[0].idTurno.value = document.getElementById(idturno).value;
				document.forms[0].idGuardia.value = document.getElementById(idguardia).value;				
			}
		
			function sustituir(fila) {		
				//Datos del elemento seleccionado:
				//seleccionarFila(fila)			
				selectRow(fila); 
				consultar2(fila, document.forms[0]);
				document.forms[0].modo.value = "sustituir";
				//document.forms[0].target = "_blank";
				//document.forms[0].submit();
				var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
				if (salida == "MODIFICADO") 
					refrescarLocal();			
			}
		
			function consultar2(fila, formulario) {		
				 var datos;
				 datos = formulario.tablaDatosDinamicosD;
				 datos.value = ""; 
				 var i, j;
				 for (i = 0; i < 7; i++) {
				      var tabla;
				      tabla = document.getElementById('tablaDatos');
				      if (i == 0) {
				        var flag = true;
				        j = 1;
				        while (flag) {
				          var aux = 'oculto' + fila + '_' + j;
				          var oculto = document.getElementById(aux);
				          if (oculto == null)  { flag = false; }
				          else { datos.value = datos.value + oculto.value + ','; }
				          j++;
				        }
				        datos.value = datos.value + "%"
				      } else { j = 2; }
				      if ((tabla.rows[fila].cells)[i].innerHTML == "")
				        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
				      else
				        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
				 }		   
			 }
 
	 		function refrescarLocal(incluirBajaLogica) {	
				if(incluirBajaLogica){
					document.DefinirGuardiasLetradoForm.bajaLogica.value = "S";
				}		
				document.DefinirGuardiasLetradoForm.target = "_self";	
				//document.DefinirGuardiasLetradoForm.action="JGR_ListarGuardiasLetrado.do";	
				//document.DefinirGuardiasLetradoForm.modo.value = "abrirAvanzada";
				document.DefinirGuardiasLetradoForm.submit();
			}
 		
			function incluirRegBajaLogica(o) {
				if (o.checked) {
					document.DefinirGuardiasLetradoForm.bajaLogica.value = "S";
				} else {
					document.DefinirGuardiasLetradoForm.bajaLogica.value = "N";
				}
				// document.DefinirGuardiasLetradoForm.modo.value = "abrirAvanzada";
				document.DefinirGuardiasLetradoForm.submit();
			}
		
			function consultaInscripcion(fila) {
				document.FormAConsultar.idInstitucion.value = <%=usr.getLocation()%>;
			   
				var idTurno 				= 'oculto' + fila + '_' + 1;
				var fsoli = 'oculto' + fila + '_' + 5;
				var osoli = 'oculto' + fila + '_' + 9;
				
				var fvali = 'oculto' + fila + '_' + 4;
				var ovali = 'oculto' + fila + '_' + 8;
				
				var fsolbaja = 'oculto' + fila + '_' + 7;
				var obaja = 'oculto' + fila + '_' + 10;
				
				var fbaja = 'oculto' + fila + '_' + 6;
				var fechaDenegacion = 'oculto' + fila + '_' + 11;
				var observacionesDenegacion = 'oculto' + fila + '_' + 12;
				document.FormAConsultar.idTurno.value = document.getElementById(idTurno).value;
			   	document.FormAConsultar.fechaSolicitud.value = document.getElementById(fsoli).value;
			   	document.FormAConsultar.observacionesSolicitud.value 	= document.getElementById(osoli).value;
			    document.FormAConsultar.fechaValidacion.value 			= document.getElementById(fvali).value;
				document.FormAConsultar.fechaBaja.value 			= document.getElementById(fbaja).value;
				document.FormAConsultar.observacionesValidacion.value 	= document.getElementById(ovali).value;
				document.FormAConsultar.fechaSolicitudBaja.value 		= document.getElementById(fsolbaja).value;
				document.FormAConsultar.observacionesBaja.value 		= document.getElementById(obaja).value;
				document.FormAConsultar.fechaDenegacion.value 		= document.getElementById(fechaDenegacion).value;
				document.FormAConsultar.observacionesDenegacion.value 		= document.getElementById(observacionesDenegacion).value;
				
				
			   	document.FormAConsultar.modo.value = "consultaInscripcion";
			   	var resultado = ventaModalGeneral(document.FormAConsultar.name,"M");				 
			}		
		
			function accionCalendario() {
				// Abrimos el calendario 
				if (document.getElementById('fechaConsulta').value!='') {
					 
					 document.DefinirGuardiasLetradoForm.fechaConsulta.value = document.getElementById('fechaConsulta').value;
					 document.DefinirGuardiasLetradoForm.modo.value = 'abrirGuardias';
					 document.DefinirGuardiasLetradoForm.submit();
					
			 	}else{
						if(document.DefinirGuardiasLetradoForm.fechaConsulta.value==''){
							fechaActual = getFechaActualDDMMYYYY();
							document.getElementById('fechaConsulta').value = fechaActual;
							document.DefinirGuardiasLetradoForm.fechaConsulta.value = fechaActual;
							document.DefinirGuardiasLetradoForm.modo.value = 'abrirGuardias';
							document.DefinirGuardiasLetradoForm.submit();
						}
				} 
			}
		
			function mostrarFecha() {		
				if(document.getElementById('fechaConsulta')){
					if(document.DefinirGuardiasLetradoForm.fechaConsulta && document.DefinirGuardiasLetradoForm.fechaConsulta.value!=''&& document.DefinirGuardiasLetradoForm.fechaConsulta.value!='sysdate'){
						document.getElementById('fechaConsulta').value = document.DefinirGuardiasLetradoForm.fechaConsulta.value;
					}else{
						fechaActual = getFechaActualDDMMYYYY();
						document.getElementById('fechaConsulta').value = fechaActual;
						document.DefinirGuardiasLetradoForm.fechaConsulta.value = fechaActual;
					}
				}
			}	
		</script>
		<!-- FIN: LISTA DE VALORES -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->

		<% if (!busquedaVolver.equals("volverNo")) { %>
			<siga:ConjBotonesAccion botones="V" />
		<% } else { %>
			<siga:ConjBotonesAccion botones="" />
		<% } %>

		<%@ include file="/html/jsp/censo/includeVolver.jspf"%>
	</body>
</html>