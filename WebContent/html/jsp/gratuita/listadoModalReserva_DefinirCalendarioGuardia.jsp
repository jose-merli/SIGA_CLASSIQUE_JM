<!-- listadoModalReserva_DefinirCalendarioGuardia.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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
	
	// Creo los botones Subir, Bajar y Cambiar
	FilaExtElement[] elems = null;
	if (!modoOriginal.equalsIgnoreCase("VER")) {
	    elems = new FilaExtElement[1];	
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script>
		function refrescarLocal(){
			parent.refrescarReservas();				
		}
	</script>
			
</head>

<body>
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = "modalReserva"/>
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
		<html:hidden property = "reserva" value = "S"/>
		<html:hidden property = "fechaInicio" value = ""/>
		<html:hidden property = "fechaFin" value = ""/>
		<html:hidden property = "idPersona" value = ""/>		
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
	<!-- INICIO: RESULTADO -->
	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesBusqueda botones="N"  titulo="gratuita.listadoModal_DefinirCalendarioGuardia.literal.colegiadosReserva" modo="<%=modoOriginal%>" />
	<!-- FIN: BOTONES BUSQUEDA -->
	<% if ((obj!= null) && (obj.size()>0)) { %>
		<siga:Table 		   
			   name="listado"
			   border="2"
			   columnNames="gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaInicio,gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaFin,gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado,gratuita.listadoModal_DefinirCalendarioGuardia.literal.nombre,gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaPermuta,"
			   columnSizes="14,14,20,23,15,14"
			   modal="P">
				<%
				int recordNumber=1;
				String fechaInicio="", fechaFin="", fechaPermuta="", idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
				String numerocolegiado="", nombre="", observaciones="", idpersona="", numero="", fechaInicioPermuta="", fechaFinPermuta="";
				String pl = "";
				while ((recordNumber) <= obj.size())
				{	 	Hashtable hash = (Hashtable)obj.get(recordNumber-1);
				%>
			<!-- Campos ocultos por cada fila:      
				1- IDCALENDARIOGUARDIAS
				2- IDTURNO
				3- IDGUARDIA
				4- IDINSTITUCION
				5- OBSERVACIONES
				6- IDPERSONA
				7- FECHAINICIO
				8- NUMERO PERMUTA
				9- FECHA FIN
			-->
			<!-- Campos visibles por cada fila:
				1- FECHAINICIO
				2- FECHAFIN
				3- Nº COLEGIADO
				4- NOMBRE
				5- FECHA PERMUTA
			-->
			<%
				fechaInicio = ((String)hash.get("FECHAINICIO")).equals("")?"&nbsp;":(String)hash.get("FECHAINICIO");
				fechaFin = ((String)hash.get("FECHAFIN")).equals("")?"&nbsp;":(String)hash.get("FECHAFIN");
				fechaPermuta = ((String)hash.get("FECHAPERMUTA")).equals("")?"&nbsp;":(String)hash.get("FECHAPERMUTA");
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
				//PL:
				pl = ((String)hash.get("PL")).equals("")?"":(String)hash.get("PL");
				//Boton cambiar solo aparece si estamos en Editar, y el pl vale 5:
				if (!modoOriginal.equalsIgnoreCase("VER") && (pl!=null && pl.equals("5"))) {
				    elems = new FilaExtElement[1];	
					elems[0]=new FilaExtElement("cambiar","cambiar",SIGAConstants.ACCESS_FULL);	
				}

			%>
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,B" elementos='<%=elems%>' clase="listaNonEdit" modo="<%=modoOriginal%>" visibleEdicion="no" >
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
					<% //Si tengo permuta muestro las fechas de la permuta:
					if (!numero.equals("NINGUNO")) { %>
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta)%>
					<% } else { %>
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>
					<% } %>
				</td>
				<td align="center">
					<% //Si tengo permuta muestro las fechas de la permuta:
					if (!numero.equals("NINGUNO")) { %>
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinPermuta)%>
					<% } else { %>
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%>
					<% } %>
				</td>
				<td align="center"><%=numerocolegiado%></td>
				<td align="center"><%=nombre%></td>								
				<td align="center">
					<% //Si tengo permuta muestro las fechas de la permuta:
					if (!numero.equals("NINGUNO")) { %>
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>
					<% } else { %>
					&nbsp;
					<% } %>
				</td>
			</siga:FilaConIconos>
				<% 		recordNumber++; %>
				<% } %>
			</siga:Table>
		<!-- FIN: RESULTADO -->
	<% } else { %>
					
	 		<div class="notFound">
	 		<html:hidden property = "actionModal" value = "P"/>
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
	<% } %>

		
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		//Guardo los campos seleccionados
		function seleccionarFila(fila){
		    var idcalendario = 'oculto' + fila + '_' + 1;
		    var idturno = 'oculto' + fila + '_' + 2;
		    var idguardia = 'oculto' + fila + '_' + 3;
		    var idpersona = 'oculto' + fila + '_' + 6;		    
		    var fechainicio = 'oculto' + fila + '_' + 7;
		    var fechafin = 'oculto' + fila + '_' + 9; 
		
			//Datos del elemento seleccionado:
			document.forms[0].idCalendarioGuardias.value = document.getElementById(idcalendario).value;
			document.forms[0].idTurno.value = document.getElementById(idturno).value;
			document.forms[0].idGuardia.value = document.getElementById(idguardia).value;
			document.forms[0].idPersona.value = document.getElementById(idpersona).value;
			document.forms[0].fechaInicio.value = document.getElementById(fechainicio).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafin).value;
		}

		<!-- Funcion asociada a boton cambiar -->
		function cambiar(fila) 
		{		
			//Datos del elemento seleccionado:
			seleccionarFila(fila)			
			
			//Submito
			document.forms[0].action = "<%=app%>/JGR_PestanaCalendarioGuardias.do";
			document.forms[0].modo.value = "buscarPor";
			var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
			if (salida == "MODIFICADO")  {
				refrescarLocal();
			}
			document.forms[0].action = "<%=app%>/JGR_DefinirCalendarioGuardia.do";
		}

		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].accion.value = "modalReserva";
			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>