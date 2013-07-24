<!DOCTYPE html>
<html>
<head>
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
	
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	Hashtable turno =(Hashtable)request.getSession().getAttribute("turnoElegido");
	String NLETRADOSINSCRITOS =(String)request.getAttribute("NLETRADOSINSCRITOS");
	
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
	String alto = "100%";	
	String entrada = (String) request.getSession().getAttribute("entrada");
	if (entrada.equalsIgnoreCase("2")){
		alto = "281";
	}
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

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
		<html:hidden property = "modo" styleId = "modo" value = ""/>
		
		<input type="hidden" name="guardiaElegida" id="guardiaElegida" value="">
		<input type="hidden" name="guardias" id="guardias" value="">
		<input type="hidden" name="fechaInscripcion"  id="fechaInscripcion" value="">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD">
			<!--input type="hidden" name="filaSelD"-->
			<input type="hidden" name="actionModal" id="actionModal" value="">
		</html:form>	
		
		  <%String nC="gratuita.listarGuardias.literal.guardia,gratuita.listarGuardias.literal.obligatoriedad,gratuita.listarGuardias.literal.tipodia,gratuita.listarGuardias.literal.duracion,gratuita.listarGuardiasTurno.literal.letradosGuardia,gratuita.listarGuardiasTurno.literal.validaJustificacion,gratuita.listarGuardiasTurno.literal.letradosApuntados,";
			String tC="30,10,10,10,10,10,10,10";%>
			
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nC%>"
		   columnSizes="<%=tC%>"
		   fixedHeight="<%=alto%>">
		
		<% if (obj==null || obj.size()==0){%>
				
					<tr class="notFound">
   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
		</tr>
					
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
			     				
				  ScsInscripcionGuardiaAdm InscripcionGuardiaAdm = new ScsInscripcionGuardiaAdm(usr);
		          Vector letradosinscritos = new Vector();
		          String NLetradosInscritos="";
				  letradosinscritos= InscripcionGuardiaAdm.selectGenerico(InscripcionGuardiaAdm.getQueryNumeroColegiadosIncritos((String)hash.get("IDINSTITUCION"),(String)hash.get("IDTURNO"),(String)hash.get("IDGUARDIA"),"sysdate"));
				if( letradosinscritos!=null  ||  letradosinscritos.size()>0){			 
					NLetradosInscritos=(String)(((Hashtable)(letradosinscritos.get(0))).get("NLETRADOSINSCRITOS"));
				
				}						
									
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
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' id='oculto<%=String.valueOf(recordNumber)%>_1'  value='<%=hash.get("IDTURNO")%>'/>
				<input type='hidden' id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=hash.get("IDGUARDIA")%>'/>					
				<input type='hidden'  id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=hash.get("GUARDIAS")%>'/>
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_4' id='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=fechainscripcion%>'/>		  
				
				<td><%=hash.get("GUARDIA")%></td>
				<td>
					<%if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("0")){%>
						<siga:Idioma key="gratuita.altaTurnos_2.literal.obligatorias"/>
					<%}else if(((String)hash.get("OBLIGATORIEDAD")).equalsIgnoreCase("1")){%>
						<siga:Idioma key="gratuita.altaTurnos_2.literal.todasninguna"/><%}else{%><siga:Idioma key="gratuita.altaTurnos_2.literal.aeleccion"/>
					<%}%>
				</td>
				<td><%=UtilidadesString.mostrarDatoJSP(ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr))%></td>
				<td><%=hash.get("DURACION")%>&nbsp;<siga:Idioma key="<%=literalDuracion%>"/></td>
				<td><%=hash.get("NUMEROLETRADOS")%></td>
				<td><%=hash.get("VALIDARJUSTIFICACIONES")%></td>
				<td><%=NLetradosInscritos %></td>			
			</siga:FilaConIconos>	
		
		<%
			recordNumber++;
		}
		%>	
		<%}%>
		</siga:Table>


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
		  
		
