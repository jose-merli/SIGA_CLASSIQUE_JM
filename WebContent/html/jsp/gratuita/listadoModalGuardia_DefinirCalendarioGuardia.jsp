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
	String tiene_colegiado= request.getAttribute("TIENE_COLEGIADO")==null?"":(String)request.getAttribute("TIENE_COLEGIADO");
	
	
	String buscarLetrado             = UtilidadesString.getMensajeIdioma(usr, "gratuita.turnos.literal.buscarLetrado");
	String literalNColegiado         = UtilidadesString.getMensajeIdioma(usr, "gratuita.turnos.literal.nColegiado");
	// Creo el boton Cambiar:
	FilaExtElement[] elems = null;
	if (!modoOriginal.equalsIgnoreCase("VER")) {
	    elems = new FilaExtElement[1];	
	}
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<script>
		function refrescarLocal(){
			parent.refrescarGuardias();		
		}
		
		function buscarLetradoEnColaLetrado () 
		{
			s = document.getElementById("buscarLetrado").value;
			if (s) {
				var ele = document.getElementsByName ("numeroColegiadoBusqueda");
				for (i = 0; i < ele.length; i++) {
					var a = ele[i].value.split("_");
					if (a) {
						var fila = a[0];
						var nColegiado = a[1];
					//alert ("Texto: " + s + " Fila: " + fila + " nColegiado: " + nColegiado);
						if(nColegiado == s) {
							selectRowTablaLetrados(eval(fila));
							return;
						}
					}
				}
			}
			selectRowTablaLetrados(-1);
		}
		
		function selectRowTablaLetrados(fila) 
		{
		   var tabla;
		   tabla = document.getElementById('listado');
		   for (var i = 0; i < tabla.rows.length; i++) {
		      if (i % 2 == 0) { 
					tabla.rows[i].className = 'filaTablaPar';
			  } else { 
					tabla.rows[i].className = 'filaTablaImpar';
			  } 
		     
			
		   }
		   if (fila >= 0 && fila < tabla.rows.length) {
			   tabla.rows[fila].className = 'listaNonEditSelected';
			   tabla.rows[fila].scrollIntoView(false);
		   }
		}

		function limpiarTexto(t, limpiar) 
		{
			if (limpiar == 1) {
				t.value= "";
			}
			else {
				if (!t.value) {
					t.value = "<%=literalNColegiado%>";
				}
			}
		}
		function activarBusquedaColegiado(){
			
			<% if ((obj!=null) && !obj.isEmpty()) { %>
				if (<%=tiene_colegiado.equals("0")%> ){
					document.getElementById('tituloTablaLetrados').style.display="block";
	
				}else{
					document.getElementById('tituloTablaLetrados').style.display="none";
				}
			<% } %>
		}
	</script>
	
</head>

<body onload="activarBusquedaColegiado();">
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
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
	<!-- INICIO: RESULTADO -->
	<!-- INICIO: BOTONES BUSQUEDA -->	

	<!-- FIN: BOTONES BUSQUEDA -->
	<% if ((obj!=null) && !obj.isEmpty()) { %>
	<table id='tituloTablaLetrados' border='1' width='100%' cellspacing='0' cellpadding='0' style="border-bottom:none">
		  <tr class = 'tableTitle'>
			
			<td align='center' width='275px'>
				<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado' />
			</td>
			<td align='center' width='174px'>
				<input id="buscarLetrado" type="text" class="box" size="10" value="<%=0%>"  
				onfocus="limpiarTexto(this, 1);" onblur="buscarLetradoEnColaLetrado();limpiarTexto(this, 0);">
				&nbsp;
				<img src="/SIGA/html/imagenes/bconsultar_off.gif" style="cursor:hand;" onClick="buscarLetradoEnColaLetrado();" alt="<%=buscarLetrado%>" >
			</td>
			<td width='550px'>
			&nbsp;
			</td>
		  </tr>
	</table>
		<siga:TablaCabecerasFijas 		   
			   nombre="listado"
			   borde="2"
			   clase="tableTitle"		   
			   nombreCol="gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaInicio,gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaFin,gratuita.listadoModal_DefinirCalendarioGuardia.literal.numeroColegiado,gratuita.listadoModal_DefinirCalendarioGuardia.literal.nombre,gratuita.listadoModal_DefinirCalendarioGuardia.literal.fechaOriginal,"
			   tamanoCol="14,14,18,23,15,16"
		   			alto="100%"

			   modal="M"
		>
				<%
				int recordNumber=1;
				String fechaInicio="",fechaInicioPK="", fechaFin="",  idcalendarioguardias="", idturno="", idguardia="", idinstitucion="";
				String numerocolegiado="", nombre="", observaciones="", idpersona="", numero="", fechaInicioPermuta="", fechaFinPermuta="";
				String pl = "";
				boolean facturada= false;
				int i=0;
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
				//facturada = ((String)hash.get("GUARDIAFACTURADA")).equalsIgnoreCase("false")?false:true;
				//PL:
				pl = ((String)hash.get("PL")).equals("")?"":(String)hash.get("PL");
				elems = new FilaExtElement[2];	
				//Boton cambiar solo aparece si estamos en Editar, y el pl vale 5:
				
				if (!modoOriginal.equalsIgnoreCase("VER") && (pl!=null && pl.equals("5"))) {
					elems[0]=new FilaExtElement("permutar","permutar",SIGAConstants.ACCESS_FULL);	
				}
				// inc7150 // Si la guardia esta facturada no se puede sustituir
				//if (!modoOriginal.equalsIgnoreCase("VER")&& pl!=null && !pl.equals("6"))
				if (!facturada && !modoOriginal.equalsIgnoreCase("VER")&& pl!=null && !pl.equals("6"))
					elems[1]=new FilaExtElement("sustituir","sustituir",SIGAConstants.ACCESS_FULL);
				String numeroColegiadoBusqueda = "" + recordNumber + "_" + numerocolegiado;
				String botones="C";
				String visibleBorrado="";
				
				if (hash.get("PINTARBOTONBORRAR").equals("1")){
					botones+=",B";
					
				}else{
					visibleBorrado="si";
				}	
					

			%>
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos='<%=elems%>' clase="listaNonEdit" modo="<%=modoOriginal%>" visibleEdicion="no" visibleBorrado="<%=visibleBorrado%>" pintarEspacio="no">
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
					<!--< % //Si tengo permuta muestro las fechas de la permuta:
					if (!numero.equals("NINGUNO")) { % >-->
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioPermuta)%>
					<!--< } else { %>
					< %=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)% >
					< % } % >-->
				</td>
				<td align="center">
					<!--< % //Si tengo permuta muestro las fechas de la permuta:
					if (!numero.equals("NINGUNO")) { % >-->
					<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinPermuta)%>
					<!--< % } else { % >
					< %=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)% >
					< % } % >-->
				</td>
				
				<td align="center">
					<input name="numeroColegiadoBusqueda" type="hidden" class="box" size="10" value="<%=numeroColegiadoBusqueda%>" >
					
					<%=numerocolegiado%>
				</td>
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
			</siga:TablaCabecerasFijas>
		<!-- FIN: RESULTADO -->
	<% } else { %>
		<html:hidden property = "actionModal" value = "P"/>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
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

		<!-- Funcion asociada a boton cambiar -->
		function permutar(fila) 
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
		function sustituir(fila) 
		{		
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

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->		
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>