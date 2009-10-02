<!-- listarGuardiasTurnos.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
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

<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	Hashtable turno =(Hashtable)request.getSession().getAttribute("turnoElegido");
	Vector ocultos = (Vector)ses.getAttribute("ocultos");
	Vector campos = (Vector)ses.getAttribute("campos");
	String accionTurno = (String)request.getSession().getAttribute("accionTurno");
	String botones="C,E,B";
	if (accionTurno.equalsIgnoreCase("Ver"))botones="C";
	FilaExtElement[] elems=new FilaExtElement[2];
	elems[0]=null;
	elems[1]=null;

	
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
	//Si entrada=2 venimos desde menu censo:
	String alto = "281";


%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.fichaCliente.sjcs.turnos.guardias.cabecera" 
		localizacion="gratuita.turnos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	</script>  

	
	<script>
		function buscar()	{	
			document.forms[0].target="mainPestanas";
			//document.forms[0].action="JGR_ListaGuardiasTurno.do?granotm=<%=System.currentTimeMillis()%>";
			document.forms[0].action="JGR_ListaGuardiasTurno.do";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}
	</script>
	
</head>

<body class="tablaCentralCampos">

<!-- Información del Turno que tenemos seleccionado-->
<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno">
	<table border="0" align="center">
	<tr>
		<td  class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/></td>
		<td  class="labelText" style="text-align:left">
			<input name="abreviatura" type="text" class="boxConsulta" size="35" maxlength="30" value="<%=turno.get("ABREVIATURA")%>" readonly="true">
		</td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/> </td>
		<td colspan="3" class="labelText" style="text-align:left">
			<input name="nombre" type="text" class="boxConsulta" size="60" maxlength="100" value="<%=turno.get("NOMBRE")%>" readonly="true" >
		</td>
	</tr>
	<tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/> </td>
		<td class="labelText" style="text-align:left">
			<input name="area" type="text" class="boxConsulta" size="35" value="<%=turno.get("AREA")%>" readonly="true">
		</td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/> </td>
		<td class="labelText" style="text-align:left">
			<input name="materia" type="text" class="boxConsulta" size="35" value="<%=turno.get("MATERIA")%>" readonly="true">
		</td>

	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/> </td>
		<td class="labelText" style="text-align:left">
			<input name="zona" type="text" class="boxConsulta" size="35" value="<%=turno.get("ZONA")%>" readonly="true">
		</td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/> </td>
		<td class="labelText" style="text-align:left">
			<input name="subzona" type="text" class="boxConsulta" size="35" value="<%=turno.get("SUBZONA")%>" readonly="true">
		</td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/> </td>
		<td  class="labelTextValor" style="text-align:left">
			<%=(turno.get("PARTIDOJUDICIAL")==null)?"&nbsp;":turno.get("PARTIDOJUDICIAL")%>
<!--			<input name="partidoJudicial" type="text" class="boxConsulta" size="30" value="<%=turno.get("PARTIDOJUDICIAL")%>" readonly="true"> -->
		</td>
	</table>
</siga:ConjCampos>
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="DefinirGuardiasTurnosAction.do" method="post" target="" 	 style="display:none">
		
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = ""/>
		
		<input type="hidden" name="guardiaElegida" value="">
		<input type="hidden" name="guardias" value="">
		<input type="hidden" name="fechaInscripcion" value="">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		  <%

			String nC="";
			String tC="";
				nC="gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardiasTurno.literal.letradosGuardia,gratuita.listarGuardiasTurno.literal.validaJustificacion,gratuita.listarGuardiasTurno.literal.letradosApuntados,";
				tC="30,10,10,10,10,10,10,10";
			%>
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   alto="<%=alto%>"
		   ajusteBotonera="true"
		  >
		
		<% if (obj==null || obj.size()==0){%>
				
					<br>
			   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
					<br>
					
		<%}else{
			// consultamos si el colegiado esta dado de baja
			String idper = (String)request.getSession().getAttribute("idPersonaTurno");
			boolean estaDeBaja = false;
			if(usr.isLetrado() && idper != null)			
			{
				CenColegiadoAdm cenColegiadoAdm = new CenColegiadoAdm(usr);
				Hashtable hashtable = cenColegiadoAdm.getEstadoColegial(Long.valueOf(idper),Integer.valueOf(usr.getLocation()));
				if(hashtable==null)
					estaDeBaja = true;
				else
				if(!hashtable.get("IDESTADO").equals(String.valueOf(ClsConstants.ESTADO_COLEGIAL_EJERCIENTE))){
					estaDeBaja = true;
				}	
			}
		  
	    	int recordNumber=1;
	    	String obligatoriedad 	= "";
	    	String fechainscripcion = "";
	    	String fechabaja 		= "";
	    	String tiposGuardias	= "";
			while ((recordNumber) <= obj.size())
			{	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);

				Vector inscrG = null;
				String consulta="";
				String fI = "";
				String fB = "";
					consulta="select count(DISTINCT IDPERSONA) LETRADOSINSCRITOS from scs_inscripcionguardia"+
								" where idinstitucion="+(String)hash.get("IDINSTITUCION")+
								" and idturno = "+hash.get("IDTURNO")+
								" and idguardia ="+hash.get("IDGUARDIA")+
								" and FECHABAJA IS NULL ";
								
					ScsInscripcionGuardiaAdm inscripcion = new ScsInscripcionGuardiaAdm(usr);
					inscrG = (Vector)inscripcion.ejecutaSelect(consulta);
					
				String tipoDia = (String)hash.get("TIPODIASGUARDIA");
				String literalDuracion ="gratuita.altaTurnos_2.literal.dias";
				if(tipoDia.equalsIgnoreCase("D"))
					literalDuracion = "gratuita.altaTurnos_2.literal.dias";
				else if(tipoDia.equalsIgnoreCase("S"))
						literalDuracion = "gratuita.altaTurnos_2.literal.semanas";
					 else if(tipoDia.equalsIgnoreCase("M"))
							literalDuracion = "gratuita.altaTurnos_2.literal.meses";
						  else if(tipoDia.equalsIgnoreCase("Q"))
								 literalDuracion = "gratuita.altaTurnos_2.literal.quincenas";	
				%>
		  <siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>"  elementos='<%=elems%>' clase="listaNonEdit">
				<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("IDTURNO")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGUARDIA")%>'><%=hash.get("GUARDIA")%><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("GUARDIAS")%>'><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=fechainscripcion%>'></td>
				<td><%if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("0")){%><siga:Idioma key="gratuita.altaTurnos_2.literal.obligatorias"/><%}else if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("1")){%><siga:Idioma key="gratuita.altaTurnos_2.literal.todasninguna"/><%}else{%><siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion"/><%}%></td>
				<td>
				<%=UtilidadesString.mostrarDatoJSP(ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr))%>
				</td>
				<td><%=hash.get("DURACION")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>"/></td>
				<td><%=hash.get("NUMEROLETRADOS")%></td>
				<td><%=hash.get("VALIDARJUSTIFICACIONES")%></td>
				<td><%=((Hashtable)inscrG.get(0)).get("LETRADOSINSCRITOS")%></td>
			</siga:FilaConIconos>	
		
		<%
			recordNumber++;
		}
		%>	
		<%}%>
		</siga:TablaCabecerasFijas>


<!-- FIN: LISTA DE VALORES -->
		
	
		<% if(accionTurno.equalsIgnoreCase("Editar")){ %>
			<siga:ConjBotonesAccion botones="N,V"  />
		<% } else { %>
			<siga:ConjBotonesAccion botones="V" />
		<% } %>
<script language="JavaScript">
	
	
		function accionNuevo() 
		{	
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"G");
			if (resultado=='MODIFICADO') 
					buscar();
		}
		function accionVolver() 
		{		
			document.forms[0].action="JGR_DefinirTurnos.do";
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}

		// botones alta y baja
		function solicitaralta(fila) 
		{
			var guardia 				= 'oculto' + fila + '_' + 2;
			var guardias 				= 'oculto' + fila + '_' + 3;
			document.forms[0].guardiaElegida.value = document.getElementById(guardia).value;
			document.forms[0].guardias.value = document.getElementById(guardias).value;
			document.forms[0].action = "<%=app%>/JGR_InscribirseEnGuardia.do";
			document.forms[0].modo.value = "editar";
			var resultado = ventaModalGeneral(document.forms[0].name,"G");
			buscar();
		}

		function solicitarbaja(fila) 
		{
			var guardia 				= 'oculto' + fila + '_' + 2;
			var guardias 				= 'oculto' + fila + '_' + 3;
			var fechaInscripcion		= 'oculto' + fila + '_' + 4;
			document.forms[0].guardiaElegida.value = document.getElementById(guardia).value;
			document.forms[0].guardias.value = document.getElementById(guardias).value;
			document.forms[0].fechaInscripcion.value = document.getElementById(fechaInscripcion).value;
			document.forms[0].action = "<%=app%>/JGR_BajaEnGuardia.do";
			document.forms[0].modo.value = "editar";
			var resultado = ventaModalGeneral(document.forms[0].name,"G");
			buscar();
		}

		function refrescarLocal(){
			buscar();
		}


</script>	
	
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>
		  
		
