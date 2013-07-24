<!DOCTYPE html>
<html>
<head>
<!-- busquedaVolantesResultados.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesHash"%>
<%@page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
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
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");

	String idinstitucionSeleccionado = request.getAttribute("IDINSTITUCION")==null?"":(String)request.getAttribute("IDINSTITUCION");
	String idturnoSeleccionado = request.getAttribute("IDTURNO")==null?"":(String)request.getAttribute("IDTURNO");
	String idguardiaSeleccionado = request.getAttribute("IDGUARDIA")==null?"":(String)request.getAttribute("IDGUARDIA");
	String idcalendarioguardiasSeleccionado = request.getAttribute("IDCALENDARIOGUARDIAS")==null?"":(String)request.getAttribute("IDCALENDARIOGUARDIAS");
	String esModificable = request.getAttribute("ESMODIFICABLE")==null?"":(String)request.getAttribute("ESMODIFICABLE");
	
	// Creo el boton Cambiar:
	FilaExtElement[] elems = new FilaExtElement[2];	

%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script>
		function refrescarLocal(){
			parent.buscar();		
		}
	</script>
	
</head>

<body>
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = "modalGuardiaVolantes"/>
		<html:hidden property = "idInstitucion" value = "<%=idinstitucionSeleccionado%>"/>
		<html:hidden property = "idTurno" value = "<%=idturnoSeleccionado%>"/>
		<html:hidden property = "idGuardia" value = "<%=idguardiaSeleccionado%>"/>
		<html:hidden property = "idCalendarioGuardias" value = "<%=idcalendarioguardiasSeleccionado%>"/>		
		<html:hidden property = "reserva" value = "N"/>
		<html:hidden property = "fechaInicio" value = ""/>
		<html:hidden property = "fechaInicioPK" value = ""/>
		<html:hidden property = "fechaFin" value = ""/>
		<html:hidden property = "idPersona" value = ""/>		
			<input type="hidden" name="validarVolantes" value="">
		</html:form>	
		
		
	<!-- INICIO: RESULTADO -->
	<!-- INICIO: BOTONES BUSQUEDA -->	

	<!-- FIN: BOTONES BUSQUEDA -->
		<siga:Table 		   
			   name="listado"
			   border="2"
			   columnNames="gratuita.busquedaVolantesGuardias.literal.val,gratuita.busquedaAsistencias.literal.abrv.fechaValidacion,gratuita.busquedaVolantesGuardias.literal.turno,gratuita.busquedaVolantesGuardias.literal.guardia,censo.busquedaVolantesGuardias.literal.ncol,gratuita.busquedaVolantesGuardias.literal.letrado,gratuita.busquedaVolantesGuardias.literal.FechaInicio,gratuita.busquedaVolantesGuardias.literal.FechaFin,"
			   columnSizes="5,8,15,17,8,17,10,10,10"
			   modal="M">
	<% if ((obj!=null) && !obj.isEmpty()) { %>
				<%
				int recordNumber=1;
				//String fechaInicio="", fechaFin="", fechaPermuta="", idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
				String fechaInicio="", fechaFin="",  idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
				String numerocolegiado="", nombre="", observaciones="", idpersona="", numero="", fechaInicioPermuta="", fechaFinPermuta="";
				String pl = "";
				String fechaInicioPK = "";
				
				while ((recordNumber) <= obj.size()) {	 	
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
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
				fechaInicioPK = ((String)hash.get("FECHA_INICIO_PK")).equals("")?"&nbsp;":(String)hash.get("FECHA_INICIO_PK");
				
				fechaFin = ((String)hash.get("FECHAFIN")).equals("")?"&nbsp;":(String)hash.get("FECHAFIN");
				//fechaPermuta = ((String)hash.get("FECHAPERMUTA")).equals("")?"&nbsp;":(String)hash.get("FECHAPERMUTA");
				idcalendarioguardias = ((String)hash.get("IDCALENDARIOGUARDIAS")).equals("")?"&nbsp;":(String)hash.get("IDCALENDARIOGUARDIAS");
				idturno = ((String)hash.get("IDTURNO")).equals("")?"&nbsp;":(String)hash.get("IDTURNO");
				idguardia = ((String)hash.get("IDGUARDIA")).equals("")?"&nbsp;":(String)hash.get("IDGUARDIA");
				idinstitucion = ((String)hash.get("IDINSTITUCION")).equals("")?"&nbsp;":(String)hash.get("IDINSTITUCION");
				numerocolegiado = ((String)hash.get("NUMEROCOLEGIADO")).equals("")?"&nbsp;":(String)hash.get("NUMEROCOLEGIADO");
				nombre = ((String)hash.get("NOMBRE")).equals("")?"&nbsp;":(String)hash.get("NOMBRE");
				idpersona = ((String)hash.get("IDPERSONA")).equals("")?"&nbsp;":(String)hash.get("IDPERSONA");
				numero = ((String)hash.get("NUMEROPERMUTA")).trim().equals("")?"NINGUNO":(String)hash.get("NUMEROPERMUTA");
				fechaInicioPermuta = ((String)hash.get("FECHAINICIOPERMUTA")).equals("")?"":(String)hash.get("FECHAINICIOPERMUTA");
				fechaFinPermuta = ((String)hash.get("FECHAFINPERMUTA")).equals("")?"":(String)hash.get("FECHAFINPERMUTA");
				String nomTurno = ((String)hash.get("NOMTURNO")).equals("")?"":(String)hash.get("NOMTURNO");
				String nomGuardia = ((String)hash.get("NOMGUARDIA")).equals("")?"":(String)hash.get("NOMGUARDIA");
				String validado = ((String)hash.get("VALIDADO")).equals("")?"":(String)hash.get("VALIDADO");
				String fechaValidacion = UtilidadesHash.getString(hash,"FECHAVALIDACION").equals("")?"&nbsp;":GstDate.getFormatedDateShort("",(String)hash.get("FECHAVALIDACION"));
				int numActuacionesValidadas = 0;
				if (hash!=null && (String)hash.get("ACT_VALIDADAS")!=null &&
					!hash.get("ACT_VALIDADAS").toString().trim().equals("")){
					numActuacionesValidadas = UtilidadesNumero.parseInt((String)hash.get("ACT_VALIDADAS"));
				}

				//PL:
				pl = ((String)hash.get("PL")).equals("")?"":(String)hash.get("PL");

// Antes:
//				elems = new FilaExtElement[2];	
//				//Boton cambiar solo aparece si estamos en Editar, y el pl vale 5:
//				if (numActuacionesValidadas==0 && !esModificable.equals("0") && pl!=null && (pl.equals("5") || pl.equals("1"))) {
//					elems[0]=new FilaExtElement("cambiar","cambiar",SIGAConstants.ACCESS_FULL);	
//					elems[1]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
//				}	

// Ahora:
				elems = new FilaExtElement[2];	
				//Boton cambiar solo aparece si estamos en Editar, y el pl vale 5:
				
				if (numActuacionesValidadas==0 && !esModificable.equals("0") && (pl!=null && pl.equals("5"))) {
					elems[0]=new FilaExtElement("cambiar","cambiar","general.boton.permutar", SIGAConstants.ACCESS_FULL);	
				}	
				if (numActuacionesValidadas==0 && !esModificable.equals("0") && pl!=null && !pl.equals("6"))
					elems[1]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
			
			%>
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C" visibleEdicion="false" visibleConsulta="false" visibleBorrado="false" elementos='<%=elems%>' clase="listaNonEdit" modo="editar" visibleEdicion="no" pintarEspacio="false">
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
					<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_12' value='<%=fechaInicioPK%>' />					
					<!-- checkbox -->
					
					<input type="checkbox" name="chkVal" value="<%=idinstitucion+"@@"+idturno+"@@"+idguardia+"@@"+idcalendarioguardias+"@@"+idpersona+"@@"+fechaInicio+"@@"+fechaInicioPK %>" <%=(validado.equals("1"))?"checked":""%>  <%=(numActuacionesValidadas>0 || esModificable.equals("0"))?"disabled":""%> >
					<input type="checkbox" name="chkValOld" value="<%=idinstitucion+"@@"+idturno+"@@"+idguardia+"@@"+idcalendarioguardias+"@@"+idpersona+"@@"+fechaInicio+"@@"+fechaInicio %>" <%=(validado.equals("1"))?"checked":""%>  disabled style="display:none">
					
						
					
					
				</td>
				<td><%=fechaValidacion%></td>
				<td><%=nomTurno%></td>								
				<td><%=nomGuardia%></td>								
				<td align="center"><%=numerocolegiado%></td>								
				<td><%=nombre%></td>								
				<td align="center">
					<!--fechainicio-->
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta)%>
				</td>
				<td align="center">
					<!--fechaFin-->
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinPermuta)%>
				</td>
				<% 		
				recordNumber++; %>
			</siga:FilaConIconos>

			<% } %>
			
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<html:hidden property = "actionModal" value = "P"/>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<% } %>

			</siga:Table>
		
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
			var fechainicioPermuta = 'oculto' + fila + '_' + 10;
		    var fechafinPermuta = 'oculto' + fila + '_' + 11;
		
			//Datos del elemento seleccionado:
			document.forms[0].idCalendarioGuardias.value = document.getElementById(idcalendario).value;
			document.forms[0].idTurno.value = document.getElementById(idturno).value;
			document.forms[0].idGuardia.value = document.getElementById(idguardia).value;
			document.forms[0].idPersona.value = document.getElementById(idpersona).value;
			/*document.forms[0].fechaInicio.value = document.getElementById(fechainicio).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafin).value;**/
			document.forms[0].fechaInicio.value = document.getElementById(fechainicioPermuta).value;
			document.forms[0].fechaFin.value = document.getElementById(fechafinPermuta).value;
		}
		
		<!-- Funcion asociada a boton cambiar -->
		function cambiar(fila) 
		{		
			//Datos del elemento seleccionado:
			seleccionarFila(fila)			
			
			//Submito
			document.forms[0].action = "<%=app%>/JGR_PestanaCalendarioGuardias.do";
			document.forms[0].validarVolantes.value = "1";
			document.forms[0].modo.value = "buscarPor";
			var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
			if (salida == "MODIFICADO")  {
				refrescarLocal();
			}
			document.forms[0].action = "<%=app%>/JGR_DefinirCalendarioGuardia.do";
		}			
		
		function sustituir(fila) 
		{		
			//Datos del elemento seleccionado:
			seleccionarFila(fila)			

			document.forms[0].action = "<%=app%>/JGR_PestanaCalendarioGuardias.do";
			document.forms[0].modo.value = "sustituir";
			var salida = ventaModalGeneral(document.forms[0].name,"M"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();			
			document.forms[0].action = "<%=app%>/JGR_DefinirCalendarioGuardia.do";
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