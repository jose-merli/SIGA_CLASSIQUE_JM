<!DOCTYPE html>
<html>
<head>
<!-- listadoModalGuardia_DefinirCalendarioGuardia.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP --> 
<%
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");	
	String fechaDesde = request.getAttribute("FECHADESDE")==null?"":(String)request.getAttribute("FECHADESDE");
	String fechaHasta = request.getAttribute("FECHAHASTA")==null?"":(String)request.getAttribute("FECHAHASTA");
	String idinstitucionSeleccionado = request.getAttribute("IDINSTITUCION")==null?"":(String)request.getAttribute("IDINSTITUCION");
	String idturnoSeleccionado = request.getAttribute("IDTURNO")==null?"":(String)request.getAttribute("IDTURNO");
	String idguardiaSeleccionado = request.getAttribute("IDGUARDIA")==null?"":(String)request.getAttribute("IDGUARDIA");
	String idcalendarioguardiasSeleccionado = request.getAttribute("IDCALENDARIOGUARDIAS")==null?"":(String)request.getAttribute("IDCALENDARIOGUARDIAS");
	String diasguardia = request.getAttribute("DIASGUARDIA")==null?"":(String)request.getAttribute("DIASGUARDIA");
	String diasacobrar = request.getAttribute("DIASACOBRAR")==null?"":(String)request.getAttribute("DIASACOBRAR");
	String tipodias = request.getAttribute("TIPODIAS")==null?"":(String)request.getAttribute("TIPODIAS");
	String numeroLetrados = request.getAttribute("NUMEROLETRADOS")==null?"":(String)request.getAttribute("NUMEROLETRADOS");
	String numeroSustitutos = request.getAttribute("NUMEROSUSTITUTOS")==null?"":(String)request.getAttribute("NUMEROSUSTITUTOS");
	String modoOriginal = request.getAttribute("MODOORIGINAL")==null?"":(String)request.getAttribute("MODOORIGINAL");
	String tiene_colegiado= request.getAttribute("TIENE_COLEGIADO")==null?"":(String)request.getAttribute("TIENE_COLEGIADO");
	
	String buscarLetrado             = UtilidadesString.getMensajeIdioma(usr, "gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado         = UtilidadesString.getMensajeIdioma(usr, "gratuita.turnos.literal.nColegiado");
	// Creo el boton Cambiar:
	FilaExtElement[] elems = null;
	if (!modoOriginal.equalsIgnoreCase("VER")) {
	    elems = new FilaExtElement[1];	
	}
%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script>
		function refrescarLocal(){
			parent.buscar();		
		}
		
		function buscarLetradoEnColaLetrado() {
			var valorBusqueda = document.getElementById("buscarLetrado").value;
			if (valorBusqueda) {
				var inputBusqColegiado = jQuery("#listado_BodyDiv tbody tr td input#numeroColegiadoBusqueda[value$='_" + valorBusqueda + "']");
				
				if (inputBusqColegiado.exists()){
					var inputBusqColegiado_fila = inputBusqColegiado.val().split("_")[0];
					selectRow(parseInt(inputBusqColegiado_fila), "listado");

				} else
					selectRow(-1, "listado");
			} else
				selectRow(-1, "listado");
		}
		
		function selectRowTablaLetrados(fila) {
			var tablaDatos = jQuery("#listado_BodyDiv tbody");
			var numTotalElementos = tablaDatos.children().length;

			for (var i=0; i<numTotalElementos; i++) {
				if (i % 2 == 0) { 
					tablaDatos.find("tr:eq(" + i + ")").attr("class", 'filaTablaPar');
				} else {
					tablaDatos.find("tr:eq(" + i + ")").attr("class", 'filaTablaImpar');
				}
		   	}
		   
		   	if (fila>=0 && fila<numTotalElementos) {
		   		tablaDatos.find("tr:eq(" + fila + ")").attr("class", 'listaNonEditSelected');
		   	}
		}		

		function limpiarTexto(t, limpiar) {
			if (limpiar == 1) {
				t.value= "";
				
			} else {
				if (!t.value) {
					t.value = "<%=literalNColegiado%>";
				}
			}
		}
		
		function activarBusquedaColegiado() {
			
			<% if ((obj!=null) && !obj.isEmpty()) { %>
				if (<%=tiene_colegiado.equals("0")%> ){
					document.getElementById('tituloTablaLetrados').style.display="block";
	
				}else{
					document.getElementById('tituloTablaLetrados').style.display="none";
				}
			<% } %>
		}

		function desactivarBotonGenerar() {
			<% if ((obj!=null) && !obj.isEmpty()) { %>
				var framePadre = window.parent;
				var botones = framePadre.document.getElementsByName("idButton");
				if(botones.length>4){
					botones(3).style.display="none";
				}
			<%}%>
		}
	</script>
	
</head>

<body onload="activarBusquedaColegiado();desactivarBotonGenerar();">
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = "modalGuardia"/>
		<html:hidden property = "fechaDesde" value = "<%=fechaDesde%>"/>
		<html:hidden property = "fechaHasta" value = "<%=fechaHasta%>"/>
		<html:hidden property = "observaciones" value = ""/>	
		<html:hidden property = "idInstitucion" value = "<%=idinstitucionSeleccionado%>"/>
		<html:hidden property = "idTurno" value = "<%=idturnoSeleccionado%>"/>
		<html:hidden property = "idGuardia" value = "<%=idguardiaSeleccionado%>"/>
		<html:hidden property = "idCalendarioGuardias" value = "<%=idcalendarioguardiasSeleccionado%>"/>		
		<html:hidden property = "diasGuardia" value = "<%=diasguardia%>"/>
		<html:hidden property = "diasACobrar" value = "<%=diasacobrar%>"/>
		<html:hidden property = "tipoDias" value = "<%=tipodias%>"/>				
		<html:hidden property = "numeroLetrados" value = "<%=numeroLetrados%>"/>		
		<html:hidden property = "numeroSustitutos" value = "<%=numeroSustitutos%>"/>		
		<html:hidden property = "reserva" value = "N"/>
		<html:hidden property = "fechaInicio" value = ""/>
		<html:hidden property = "fechaFin" value = ""/>
		<html:hidden property = "idPersona" value = ""/>		
	</html:form>			
		
	<!-- INICIO: RESULTADO -->
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<!-- FIN: BOTONES BUSQUEDA -->
	
<%
	if ((obj!=null) && !obj.isEmpty()) { 
%>			

		<table id='tituloTablaLetrados' border='1' width='100%' cellspacing='0' cellpadding='0' style="border-bottom:none">
			<tr class = 'tableTitle'>
				<td align='center' width='275px'>
					<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado' />
				</td>
				<td align='center' width='174px'>
					<input id="buscarLetrado" type="text" class="box" size="10" value="<%=0%>" onfocus="limpiarTexto(this, 1);" onblur="buscarLetradoEnColaLetrado();limpiarTexto(this, 0);">
					&nbsp;
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" >
				</td>
				<td width='550px'>
					&nbsp;
				</td>
		  	</tr>
		</table>
<% 
	}
%>		

	<siga:Table 		   
		name="listado"
		border="2"
		columnNames="gratuita.listadoModal_DefinirCalendarioGuardia.literal.validada,
			gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaInicio,
			gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaFin,
			gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado,
			gratuita.listadoModal_DefinirCalendarioGuardia.literal.nombre,
			gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaOriginal,
			gratuita.guardiasTurno.literal.porGrupos.orden,"
		columnSizes="7,10,10,10,28,10,10,15"
		modal="M">
<%
		if ((obj!=null) && !obj.isEmpty()) { 
			int recordNumber=1;
			String fechaInicio="", fechaInicioPK="", fechaFin="", idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
			String numerocolegiado="", nombre="", observaciones="", idpersona="", numero="", fechaInicioPermuta="", fechaFinPermuta="";
			String orden="", validado="", activarCheck="";
			int i=0;
			while ((recordNumber) <= obj.size()) {	 	
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);

			/* 
				Campos ocultos por cada fila:      
				1- IDCALENDARIOGUARDIAS
				2- IDTURNO
				3- IDGUARDIA
				4- IDINSTITUCION
				5- OBSERVACIONES
				6- IDPERSONA
				7- FECHAINICIO
				8- NUMERO PERMUTA
				9- FECHA FIN
			
				Campos visibles por cada fila:
				1- FECHAINICIO
				2- FECHAFIN
				3- N� COLEGIADO
				4- NOMBRE
				5- FECHA PERMUTA
				6- GRUPO
				7- ORDEN
			*/
				fechaInicio = ((String)hash.get("FECHAINICIO")).equals("")?"&nbsp;":(String)hash.get("FECHAINICIO");
				fechaFin = ((String)hash.get("FECHAFIN")).equals("")?"&nbsp;":(String)hash.get("FECHAFIN");
				fechaInicioPK = ((String)hash.get("FECHA_INICIO_PK")).equals("")?"&nbsp;":(String)hash.get("FECHA_INICIO_PK");
				idcalendarioguardias = ((String)hash.get("IDCALENDARIOGUARDIAS")).equals("")?"&nbsp;":(String)hash.get("IDCALENDARIOGUARDIAS");
				idturno = ((String)hash.get("IDTURNO")).equals("")?"&nbsp;":(String)hash.get("IDTURNO");
				idguardia = ((String)hash.get("IDGUARDIA")).equals("")?"&nbsp;":(String)hash.get("IDGUARDIA");
				idinstitucion = ((String)hash.get("IDINSTITUCION")).equals("")?"&nbsp;":(String)hash.get("IDINSTITUCION");
				numerocolegiado = ((String)hash.get("NUMEROCOLEGIADO")).equals("")?"&nbsp;":(String)hash.get("NUMEROCOLEGIADO");
				nombre = ((String)hash.get("NOMBRE")).equals("")?"&nbsp;":(String)hash.get("NOMBRE");
				observaciones = ((String)hash.get("OBSERVACIONES")).equals("")?"&nbsp;":(String)hash.get("OBSERVACIONES");
				idpersona = ((String)hash.get("IDPERSONA")).equals("")?"&nbsp;":(String)hash.get("IDPERSONA");
				numero = ((String)hash.get("NUMEROPERMUTA")).equals("")?"NINGUNO":(String)hash.get("NUMEROPERMUTA");
				fechaInicioPermuta = ((String)hash.get("FECHAINICIOPERMUTA")).equals("")?"":(String)hash.get("FECHAINICIOPERMUTA");
				fechaFinPermuta = ((String)hash.get("FECHAFINPERMUTA")).equals("")?"":(String)hash.get("FECHAFINPERMUTA");
				orden = (hash.get("ORDEN")==null||((String)hash.get("ORDEN")).equals(""))?"&nbsp;":(String)hash.get("ORDEN");
				validado = ((String)hash.get("VALIDADO")).equals("")?"":(String)hash.get("VALIDADO");
				
				/* Obtiene las acciones de la guardia
				 * @return String[7]
					 * 0 - P_SUSTITUIR: 'N'=NoSustituible; 'S'=Sustituible
					 * 1 - P_ANULAR: 'N'=NoAnulable; 'S'=Anulable
					 * 2 - P_BORRAR: 'N'=NoBorrable; 'S'=Borrable
					 * 3 - P_PERMUTAR: 'N'=NoPermutable(PendienteSolicitante); 'P'=NoPermutable(PendienteConfirmador); 'S'=Permutable
					 * 4 - P_ASISTENCIA: 'N'=SinAsistencias; 'S'=ConAsistencias
					 * 5 - P_CODRETORNO: Devuelve 0 en caso de que la ejecucion haya sido OK, en caso de error devuelve el codigo de error Oracle correspondiente.
					 * 6 - P_DATOSERROR: Devuelve null en caso de que la ejecucion haya sido OK, en caso de error devuelve el mensaje de error Oracle correspondiente.*/
				String[] accionesGuardia = (String[])hash.get("ACCIONESGUARDIA");
				
				String numeroColegiadoBusqueda="" + recordNumber + "_" + numerocolegiado;
				
				if (validado.equals("1"))
					activarCheck="checked";
				else
					activarCheck="";
				
				elems = new FilaExtElement[3];				
				String botones="C", visibleBorrado="si";
				
				if (!modoOriginal.equalsIgnoreCase("VER")) {
					
					if (accionesGuardia[0]!=null && accionesGuardia[0].equalsIgnoreCase("S")) { // 0 - P_SUSTITUIR: 'N'=NoSustituible; 'S'=Sustituible
						elems[1]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
					}
					
					if (accionesGuardia[1]!=null && accionesGuardia[1].equalsIgnoreCase("S")) { // 1 - P_ANULAR: 'N'=NoAnulable; 'S'=Anulable
						elems[2]=new FilaExtElement("denegar", "anular","anular", SIGAConstants.ACCESS_FULL);
					}
					
					if (accionesGuardia[2]!=null && accionesGuardia[2].equalsIgnoreCase("S")) { // 2 - P_BORRAR: 'N'=NoBorrable; 'S'=Borrable
						botones+=",B";
						visibleBorrado="";
					}
					
					if (accionesGuardia[3]!=null && accionesGuardia[3].equalsIgnoreCase("S")) { // 3 - P_PERMUTAR: 'N'=NoPermutable; 'S'=Permutable
						elems[0]=new FilaExtElement("permutar","permutar",SIGAConstants.ACCESS_FULL);
					}
				}
%>
	       		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit" modo="<%=modoOriginal%>" visibleEdicion="no" visibleBorrado="<%=visibleBorrado%>" pintarEspacio="no">
					<td align="center">
						<input type=checkbox id="checkValidada_<%=String.valueOf(recordNumber)%>" value="<%=validado%>" <%=activarCheck%> disabled="disabled"/>
					</td>
					
					<td align="center">
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idcalendarioguardias%>' >
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idturno%>' >
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idguardia%>' >
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idinstitucion%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=observaciones%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=idpersona%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_7' value='<%=fechaInicio%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_8' value='<%=numero%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_9' value='<%=fechaFin%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_10' value='<%=fechaInicioPermuta%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_11' value='<%=fechaFinPermuta%>' />
						<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_ASI' value='<%=accionesGuardia[4]%>' />
						
						<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta)%>
					</td>
					
					<td align="center">
						<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinPermuta)%>
					</td>
				
					<td align="center">
						<input id="numeroColegiadoBusqueda" name="numeroColegiadoBusqueda" type="hidden" class="box" size="10" value="<%=numeroColegiadoBusqueda%>" >
						<%=numerocolegiado%>
					</td>
					
					<td align="center"><%=nombre%></td>	
												
					<td align="center">
<% //Si tengo permuta muestro las fechas de la permuta:
						if (!numero.equals("NINGUNO")) { 
%>
							<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>
<% 
						} else { 
%>
							&nbsp;
<% 
						} 
%>
					</td>
					
					<td align="center"><%=orden%></td>		
				</siga:FilaConIconos>
<% 		
				recordNumber++;
			} 
	
		} else { 
%>
	 		<tr class="notFound">
				<td class="titulitos">
					<html:hidden property = "actionModal" value = "P"/>
					<siga:Idioma key="messages.noRecordFound"/>
				</td>
			</tr>
<% 
		}
%>
	</siga:Table>
		
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Guardo los campos seleccionados
		function seleccionarFila(fila) {
		    var idcalendario = 'oculto' + fila + '_' + 1;
		    var idturno = 'oculto' + fila + '_' + 2;
		    var idguardia = 'oculto' + fila + '_' + 3;
		    var idpersona = 'oculto' + fila + '_' + 6;		    
		    //var fechainicio = 'oculto' + fila + '_' + 7;
		    //var fechafin = 'oculto' + fila + '_' + 9;
			var fechainicioPermuta = 'oculto' + fila + '_' + 10;
		    var fechafinPermuta = 'oculto' + fila + '_' + 11;
	
			//Datos del elemento seleccionado:
			document.forms[0].idCalendarioGuardias.value = document.getElementById(idcalendario).value;
			document.forms[0].idTurno.value = document.getElementById(idturno).value;
			document.forms[0].idGuardia.value = document.getElementById(idguardia).value;
			document.forms[0].idPersona.value = document.getElementById(idpersona).value;
			document.forms[0].fechaInicio.value = document.getElementById(fechainicioPermuta).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafinPermuta).value;
			/*document.forms[0].fechaInicio.value = document.getElementById(fechainicio).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafin).value;*/
		}
		
		// Funcion asociada a boton cambiar
		function permutar(fila) {		
			//Datos del elemento seleccionado:
			seleccionarFila(fila);
		
			var tieneAsistencias = 'oculto' + fila + '_ASI';
			var valorTieneAsistencias = document.getElementById(tieneAsistencias).value;
			var confirmado = true;
			
			if (valorTieneAsistencias == 'S') {
				confirmado = false;
				if (confirm("<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias'/>")) {
					confirmado = true;
				}
			}
			
			if (confirmado == true)	{						
				document.forms[0].action = "<%=app%>/JGR_PestanaCalendarioGuardias.do";
				document.forms[0].modo.value = "buscarPor";
				var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
				if (salida == "MODIFICADO")  {
					refrescarLocal();
				}
				document.forms[0].action = "<%=app%>/JGR_DefinirCalendarioGuardia.do";
			}
		}			
		
		function sustituir(fila) {
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
				//Datos del elemento seleccionado:
				seleccionarFila(fila)			
	            document.forms[0].action = "<%=app%>/JGR_PestanaCalendarioGuardias.do";
				document.forms[0].modo.value = "sustituir";
				//document.forms[0].target = "_blank";
				//document.forms[0].submit();
				var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
				if (salida == "MODIFICADO") 
					refrescarLocal();	
				document.forms[0].action = "<%=app%>/JGR_DefinirCalendarioGuardia.do";
			}
		}
		
		function anular(fila) {
			var tieneAsistencias = 'oculto' + fila + '_ASI';
			var valorTieneAsistencias = document.getElementById(tieneAsistencias).value;
			var confirmado = true;
			
			if (valorTieneAsistencias == 'S') {
				confirmado = false;
				if (confirm("<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.anular.tieneAsistencias'/>")) {
					confirmado = true;
				}
			}
			
			if (confirmado == true)	{			
				//Datos del elemento seleccionado:
				seleccionarFila(fila);	
				
				document.forms[0].modo.value = "anular";
				var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
				
				if (salida == "MODIFICADO") { 
					refrescarLocal();
				}
			}
		}
		
		 function consultar(fila, id) {
				if (typeof id == 'undefined')
					id='listado';
				preparaDatos(fila,id);
			   document.forms[0].modo.value = "Ver";
			   ventaModalGeneral(document.forms[0].name, "G");
			 }
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>	
</html>