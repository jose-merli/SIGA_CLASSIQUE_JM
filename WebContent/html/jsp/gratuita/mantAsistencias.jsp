<!-- mantAsistencias.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	// Validamos si es una consulta o no.
	String modo = request.getParameter("MODO");
	String accion = (String) request.getSession().getAttribute("accion");

	boolean esFichaColegial = false;

	String sEsFichaColegial = (String) request.getAttribute("esFichaColegial");
	if ((sEsFichaColegial != null)
			&& ((sEsFichaColegial.equalsIgnoreCase("1"))||(sEsFichaColegial.equalsIgnoreCase("true"))  )) {
		esFichaColegial = true;
	}

	String[] dato = {usr.getLocation()};
	// Obtenemos el resultado
	Hashtable hash =  (Hashtable) ((Vector) request.getAttribute("resultado")).get(0);
	String ANIO 				= (String) hash.get("ANIO");
	String NUMERO 				= (String) hash.get("NUMERO");
	String CODIGO 				= (String) hash.get("CODIGO");
	String TURNO 				= (String) hash.get("TURNO");
	String IDTURNO 				= (String) hash.get("IDTURNO");
	String IDGUARDIA			= (String) hash.get("IDGUARDIA");

	String GUARDIA 				= (String) hash.get("GUARDIA");
	String FECHA 				= (String) hash.get("FECHA");
	if(FECHA!= null) FECHA = GstDate.getFormatedDateShort("",FECHA);
	String NIFASISTIDO 			= (String) hash.get("NIFASISTIDO");
	String NOMBREAASISTIDO 		= (String) hash.get("NOMBREAASISTIDO");
	String IDPERSONAJG 			= (String) hash.get("IDPERSONAJG");
	String APELLIDO1ASISTIDO 	= (String) hash.get("APELLIDO1ASISTIDO");
	String APELLIDO2ASISTIDO 	= (String) hash.get("APELLIDO2ASISTIDO");
	String NIFLETRADO 			= (String) hash.get("NIFLETRADO");
	String NUMEROCOLEGIADO		= (String) hash.get("NUMEROCOLEGIADO");
	String NOMBRELETRADO 		= (String) hash.get("NOMBRELETRADO");
	String APELLIDO1LETRADO 	= (String) hash.get("APELLIDO1LETRADO");
	String APELLIDO2LETRADO 	= (String) hash.get("APELLIDO2LETRADO");
	String ANIOEJG 				= (String) hash.get("ANIOEJG");
	String NUMEROEJG 			= (String) hash.get("NUMEROEJG");
	String CODIGO_EJG			= (String) hash.get("CODIGO_EJG");
	String TIPOEJG 				= (String) hash.get("TIPOEJG");
	String IDTIPOEJG 			= (String) hash.get("IDTIPOEJG");
	String FECHAAPERTURA 		= (String) hash.get("FECHAAPERTURA");
	if(FECHAAPERTURA!= null) FECHAAPERTURA = GstDate.getFormatedDateShort("",FECHAAPERTURA);
	String DESIGNA_ANIO 		= (String) hash.get("DESIGNA_ANIO");
	String DESIGNA_NUMERO 		= (String) hash.get("DESIGNA_NUMERO");
	String DESIGNA_TURNO   		= (String) hash.get("DESIGNA_TURNO");
	String NOMBRETURNO 			= (String) hash.get("NOMBRETURNO");
	String DES_DESIGNA_TURNO 	= (String) hash.get("DES_DESIGNA_TURNO");
	String FECHAENTRADA 		= GstDate.getFormatedDateShort("",(String) hash.get("FECHAENTRADA"));
	String TIPOASISTENCIA 		= (String) hash.get("TIPOASISTENCIA");
	String FECHAHORA 			= GstDate.getFormatedDateShort("",(String) hash.get("FECHAHORA"));
	String FECHACIERRE 			= GstDate.getFormatedDateShort("",(String) hash.get("FECHACIERRE"));
	String OBSERVACIONES		= (String) hash.get("OBSERVACIONES");
	String INCIDENCIAS 			= (String) hash.get("INCIDENCIAS");
	String TIPOASISTENCIACOLEGIO = (String) hash.get("TIPOASISTENCIACOLEGIO");
	// Seleccion.
	ArrayList TIPOASISTENCIACOLEGIOSEL = new ArrayList();
	TIPOASISTENCIACOLEGIOSEL.add(TIPOASISTENCIACOLEGIO);
	ArrayList TIPOASISTENCIASEL = new ArrayList();
	TIPOASISTENCIASEL.add(TIPOASISTENCIA);
	// Obtenemos la descripcion del tipoasistencia y tipoasistenciacolegio
	String TIPOASISTENCIASELDESC = "";
	String TIPOASISTENCIACOLEGIODESC = "";
	ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
	String sql = "";
	Vector vect = null;
	if(TIPOASISTENCIA!=null && !TIPOASISTENCIA.equals(""))
	{
		sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma("DESCRIPCION",usr.getLanguage()) + " FROM SCS_TIPOASISTENCIA WHERE IDTIPOASISTENCIA = "+TIPOASISTENCIA;
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if(vect!=null && vect.size()>0)
			TIPOASISTENCIASELDESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
	}
	if(TIPOASISTENCIACOLEGIO!=null && !TIPOASISTENCIACOLEGIO.equals(""))
	{
		sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma("DESCRIPCION",usr.getLanguage()) + " FROM SCS_TIPOASISTENCIACOLEGIO WHERE IDTIPOASISTENCIACOLEGIO = "+TIPOASISTENCIACOLEGIO+" AND IDINSTITUCION = "+usr.getLocation();
		vect = scsAsistenciaAdm.ejecutaSelect(sql);
		if(vect!=null && vect.size()>0)
			TIPOASISTENCIACOLEGIODESC = (String)((Hashtable) vect.get(0)).get("DESCRIPCION");
	}
	
	//FECHA ANULACIÓN
	boolean anulada=false;
	String fechaAnulacion=(String) hash.get("FECHAANULACION");
	if(fechaAnulacion.trim().equals(""))
	{
		Date hoy = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		fechaAnulacion = sd.format(hoy);
	}
	else
	{
		fechaAnulacion = GstDate.getFormatedDateShort("",fechaAnulacion);
		anulada = true;
	}

	String idTurno = (String) hash.get("IDTURNO");
	String[] parametroJuzgado = {usr.getLocation(), idTurno};
	
	// Para cuando creamos la designacion
	idTurno = " ," + idTurno;
	String nombreCompletoLetrado = NOMBRELETRADO + " " + APELLIDO1LETRADO + " " + APELLIDO2LETRADO;
	String juzgado = (String) request.getAttribute("JUZGADO");
	if (juzgado == null) juzgado = new String ("");
	

	ArrayList estadoSel    = new ArrayList();
	ArrayList juzgadoSel   = new ArrayList();
	ArrayList comisariaSel = new ArrayList();

	String numeroDiligenciaAsi    = (String) hash.get(ScsAsistenciasBean.C_NUMERODILIGENCIA);
	String numeroProcedimientoAsi = (String) hash.get(ScsAsistenciasBean.C_NUMEROPROCEDIMIENTO);
	
	// Datos del Juzgado seleccionado:
	String juzgadoAsi            = (String) hash.get(ScsAsistenciasBean.C_JUZGADO);
 	String juzgadoInstitucionAsi = (String) hash.get(ScsAsistenciasBean.C_JUZGADO_IDINSTITUCION);
	if (juzgadoAsi!=null && juzgadoInstitucionAsi!=null)
		juzgadoSel.add(0,juzgadoAsi+","+juzgadoInstitucionAsi);	
	
 	// Datos de la comisaria seleccionado:
	String comisariaAsi            = (String) hash.get(ScsAsistenciasBean.C_COMISARIA);
	String comisariaInstitucionAsi = (String) hash.get(ScsAsistenciasBean.C_COMISARIA_IDINSTITUCION);
	if (comisariaAsi!=null && comisariaInstitucionAsi!=null)
		comisariaSel.add(0,comisariaAsi+","+comisariaInstitucionAsi);

 	// Datos del estadoseleccionado:
	String estadoAsi = (String) hash.get(ScsAsistenciasBean.C_IDESTADOASISTENCIA);
	if (estadoAsi!=null)
		estadoSel.add(0,estadoAsi);
	
	String fechaEstado = (String) hash.get(ScsAsistenciasBean.C_FECHAESTADOASISTENCIA);
	if (fechaEstado != null && !fechaEstado.equals(""))
		fechaEstado = GstDate.getFormatedDateShort("", fechaEstado);
	
	String estilo = "box", readOnly="false", comboSize="400";
	if(modo.equals("ver")){
		estilo = "boxConsulta";
		readOnly = "true";
		comboSize="550";
	}
	
/*  String t_nombreEJG = "", t_apellido1EJG = "", t_apellido2EJG = "", t_anioEJG = "", t_numeroEJG = "", t_tipoEJG="";;
						ScsEJGAdm ejgAdm = new ScsEJGAdm (usr);
						Hashtable hTituloEJG = ejgAdm.getTituloPantallaEJG(usr.getLocation(), ANIOEJG, NUMEROEJG,IDTIPOEJG);

						if (hTituloEJG != null) {
							t_nombreEJG    = (String)hTituloEJG.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1EJG = (String)hTituloEJG.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2EJG = (String)hTituloEJG.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anioEJG      = (String)hTituloEJG.get(ScsEJGBean.C_ANIO);
							t_numeroEJG    = (String)hTituloEJG.get(ScsEJGBean.C_NUMEJG);
							t_tipoEJG   = (String)hTituloEJG.get("TIPOEJG");
						}
*/
					
String t_nombreD = "", t_apellido1D = "", t_apellido2D = "";				
if ((DESIGNA_ANIO != null) && (!DESIGNA_ANIO.equals(""))) {
	ScsDesignaAdm admD = new ScsDesignaAdm (usr);
	Hashtable hTituloD = admD.getTituloPantallaDesigna(usr.getLocation(), DESIGNA_ANIO, DESIGNA_NUMERO,DESIGNA_TURNO);
	if (hTituloD != null) {
		t_nombreD    = (String)hTituloD.get(ScsPersonaJGBean.C_NOMBRE);
		t_apellido1D = (String)hTituloD.get(ScsPersonaJGBean.C_APELLIDO1);
		t_apellido2D = (String)hTituloD.get(ScsPersonaJGBean.C_APELLIDO2);
	}	
}





%>


<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="gratuita.mantAsistencias.literal.titulo" 
				 localizacion="gratuita.mantAsistencias.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body >

    <table class="tablaTitulo" align="center" cellspacing=0>
		<tr>
			<td class="titulitosDatos">
			
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(usr.getLocation(), ANIO, NUMERO);
					if (hTitulo != null) {
						t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
						t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
						t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						t_anio      = (String)hTitulo.get(ScsAsistenciasBean.C_ANIO);
						t_numero    = (String)hTitulo.get(ScsAsistenciasBean.C_NUMERO);
					}
				%>
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>	



<table class="tablaCentralCampos" height="440">

<%			
	String sAction = esFichaColegial ? "JGR_MantenimientoAsistenciaLetrado.do" : "JGR_MantenimientoAsistencia.do";
%>

<html:form action = "<%=sAction%>" method="POST" target="mainWorkArea">
<html:hidden property = "actionModal" value = ""/>
<html:hidden property = "tipoEJG" value = "<%=TIPOEJG%>"/>
<html:hidden property = "idTipoEJG" value = "<%=IDTIPOEJG%>"/>
<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
<html:hidden property = "anio" 	value = "<%=ANIO%>"/>
<html:hidden property = "numero" value = "<%=NUMERO%>"/>
<html:hidden property = "modo" value = ""/>	
<html:hidden property = "idPersonaJG" value = "<%=IDPERSONAJG%>"/>
<input type="hidden" name="esFichaColegial" value="<%=sEsFichaColegial%>"/>
<input type="hidden" name="estadoAsinteciaAnterior" value="<%=estadoAsi%>" >

<html:hidden property = "ejg_anio"    		value= "<%=ANIOEJG%>"/>
<html:hidden property = "ejg_numero"  		value= "<%=NUMEROEJG%>"/>
<html:hidden property = "ejg_idTipoEJG" 	value= "<%=IDTIPOEJG%>"/>
<html:hidden property = "ejg_idInstitucion" value= "<%=usr.getLocation()%>"/>

<html:hidden property = "designa_anio"    		value= "<%=DESIGNA_ANIO%>"/>
<html:hidden property = "designa_numero"  		value= "<%=DESIGNA_NUMERO%>"/>
<html:hidden property = "designa_turno" 		value= "<%=DESIGNA_TURNO%>"/>
<html:hidden property = "designa_idInstitucion" value= "<%=usr.getLocation()%>"/>

<html:hidden property = "idTurno"    		value= "<%=IDTURNO%>"/>
<html:hidden property = "idGuardia"    		value= "<%=IDGUARDIA%>"/>
<html:hidden property = "nombreColegiado"  	value= "<%=nombreCompletoLetrado%>"/>
<html:hidden property = "numeroColegiado"  	value= "<%=NUMEROCOLEGIADO%>"/>

<tr>
<td valign="top">	
	<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.titulo">
	<table width="100%" border="0" style="table-layout:fixed">
		<tr>
		    <td class="labelText" width="12%">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.anio'/> / <siga:Idioma key='gratuita.mantAsistencias.literal.numero'/>
			</td>
			<td class="labelTextValor" width="17%">	
				<%=ANIO%> / <%=NUMERO%>
			</td>
			<td class="labelText" style="width:70px">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.turno'/>
			</td>
			<td class="labelTextValor"  style="width:200px">	
				<%=NOMBRETURNO%>
			</td>
			<td class="labelText"  style="width:70px">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.guardia'/>
			</td>
			<td class="labelTextValor"  style="width:200px">	
				<%=GUARDIA%>
			</td>
			
		</tr>
	</table>
	<table width="100%" border="0" style="table-layout:fixed">
		<tr>
			<td width="50%">	
			<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.asistido">
				<table width="100%" border="0">
					<tr>
						<td class="labelText" width="20%">	
							<siga:Idioma key='gratuita.mantAsistencias.literal.nif'/>
						</td>
						<td class="labelTextValor" width="80%">	
							<%=NIFASISTIDO%>
						</td>
					</tr>
					<tr>
						<td class="labelText">	
							<siga:Idioma key='gratuita.mantAsistencias.literal.nombre'/>
						</td>
						<td class="labelTextValor">	
							<%=NOMBREAASISTIDO%>&nbsp;<%=APELLIDO1ASISTIDO%>&nbsp;<%=APELLIDO2ASISTIDO%>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			</td>
			<td width="50%">	
			<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.letrado">
				<table  width="100%" border="0">
					<tr>
						<td class="labelText" width="20%">	
							<siga:Idioma key='gratuita.mantAsistencias.literal.numero'/>
						</td>
						<td class="labelTextValor" width="80%">	
							<%=NUMEROCOLEGIADO%>
						</td>
					</tr>
					<tr>
						<td class="labelText">	
							<siga:Idioma key='gratuita.mantAsistencias.literal.nombre'/>
						</td>
						<td class="labelTextValor">	
							<%=NOMBRELETRADO%>&nbsp;<%=APELLIDO1LETRADO%>&nbsp;<%=APELLIDO2LETRADO%>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
			</td>
		</tr>
	</table>
	<table width="100%" border="0" style="table-layout:fixed">
		<tr>
			<td class="labelText" width="14%">
				<siga:Idioma key='gratuita.mantAsistencias.literal.tipo'/>
			</td>
			<td class="labelTextValor" width="65%">	
			<% if(modo.equals("ver")){%>
				<%=TIPOASISTENCIASELDESC%>
			<%}else{%>
				<siga:ComboBD ancho="600" nombre="idTipoAsistencia" tipo="scstipoasistencia" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorioSinTextoSeleccionar="true"  elementoSel="<%=TIPOASISTENCIASEL%>"/>
			<%}%>
			</td>	
		</tr>

		<tr>
			<td class="labelText" width="14%">
				<siga:Idioma key='gratuita.mantAsistencias.literal.tasiscolegio'/>
			</td>
			<td class="labelTextValor" width="65%">	
			<% if(modo.equals("ver")){%>
				<%=TIPOASISTENCIACOLEGIODESC%>
			<%}else{%>
				<siga:ComboBD ancho="600" nombre="idTipoAsistenciaColegio" tipo="scstipoasistenciacolegio" estilo="true" clase="boxCombo" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorioSinTextoSeleccionar="true" obligatorio="false" elementoSel="<%=TIPOASISTENCIACOLEGIOSEL%>" />
			<%}%>
			</td>	
		</tr>
	</table>

	<table width="100%" border="0" style="table-layout:fixed">
		<tr>
			<!-- Busqueda automatica de juzgados-->
			<td colspan="4">
	<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.centroDetencion"> 
		   <table width="100%">
		   	<tr>
		   		<td class="labelText" style="vertical-align:text-top;"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligencia'/>
		   		&nbsp;
				<input name="numeroDilegencia" type="text" value="<%=numeroDiligenciaAsi%>" class="<%=estilo%>" maxLength="20" />
			</td> 
			<% if(!modo.equals("ver")){%>
			<td class="labelText" style="vertical-align:text-top;text-align: right">
			<siga:Idioma key='gratuita.mantenimientoTablasMaestra.literal.codigoext'/>
			&nbsp;
			<input type="text" name="codigoExtComisaria" class="box" size="8"  maxlength="10" onBlur="obtenerComisaria();" />
			<%}%>
			<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" ancho="420" obligatorio="false" parametro="<%=parametroJuzgado%>" elementoSel="<%=comisariaSel%>" clase="<%=estilo%>" readonly="<%=readOnly%>"/>
			</td>
			</tr>
		</table>
	</siga:ConjCampos> 
		</td>
<!------------------>
		
		</tr>
		<tr>
			<!-- Busqueda automatica de juzgados-->
			<td colspan="4">
		<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado"> 

		   <table width="100%">
		   	<tr>
		   	<td class="labelText" style="vertical-align:text-top;"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/>
		   	&nbsp;
		   	<input name="numeroProcedimiento" maxlength="20" type="text" value="<%=numeroProcedimientoAsi%>" class="<%=estilo%>"/>
			</td>
			<% if(!modo.equals("ver")){%>	
			<td class="labelText" style="vertical-align:text-top;text-align: right">	
			   <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
			   &nbsp;
			   <input type="text" name="codigoExtJuzgado" class="box" size="8" maxlength="10" onBlur="obtenerJuzgado();"/>
			<%}%>
			<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosTurno" ancho="420" obligatorio="false" parametro="<%=parametroJuzgado%>" elementoSel="<%=juzgadoSel%>" clase="<%=estilo%>" readonly="<%=readOnly%>" />
			</td>   
				
				</tr>
				</table>
		</siga:ConjCampos> 
		</td>
<!------------------>
			
			
		</tr>
	</table>
	
	<table width="100%" border="0" style="table-layout:fixed">
		<tr>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.busquedaAsistencias.literal.fechaAsistencia'/>
			</td>
			<td class="labelTextValor">	
				<%=FECHAHORA%>
			</td>

			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantAsistencias.literal.fcierre'/>
			</td>
			<td class="labelTextValor" >	
			<% if(modo.equals("editar")){%>
				<html:text name="AsistenciasForm" property="fechaCierre" size="8" maxlength="10" styleClass="box" value="<%=FECHACIERRE%>" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaCierre);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
			<%}else{%>
				<%=FECHACIERRE%>
			<%}%>
			</td>
			
			<td class="labelText"  >
				<siga:Idioma key="gratuita.mantAsistencias.literal.estado"/>
			</td>
			<td class="labelTextValor">
				<siga:ComboBD nombre="estadoAsintecia" tipo="cmbEstadosAsistencia" obligatorio="false" accion="" elementoSel="<%=estadoSel%>" clase="<%=estilo%>" readonly="<%=readOnly%>" obligatorioSinTextoSeleccionar="si"/>
			</td>
			<td class="labelText" >
				<siga:Idioma key="gratuita.mantAsistencias.literal.fechaEstado"/>
			</td>
			<td class="labelTextValor" >
				<html:textarea name="AsistenciasForm" property="fechaEstadoAsistencia" styleClass="boxConsulta" value="<%=fechaEstado%>" readOnly="true"></html:textarea>
			</td>
			
		</tr>
		<tr align="center">
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantAsistencias.literal.observaciones'/>
			</td>
			<td class="labelTextValor" colspan="3">
			<% if(modo.equals("ver")){%>
				<html:textarea name="DefinirPermutaGuardiasForm" property="observaciones" cols="70" rows="3" style="overflow:auto" styleClass="boxComboConsulta" value="<%=OBSERVACIONES%>" readonly="true"></html:textarea>
			<%}else{%>
				<html:textarea name="DefinirPermutaGuardiasForm" property="observaciones" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="70" rows="3" style="overflow:auto" styleClass="boxCombo" value="<%=OBSERVACIONES%>" readOnly="false"></html:textarea>
			<%}%>
			</td>
			<td class="labelText" >	
				<siga:Idioma key='gratuita.mantAsistencias.literal.incidencias'/>
			</td>
			<td class="labelTextValor" colspan="3">
			<% if(modo.equals("ver")){%>
				<html:textarea name="DefinirPermutaGuardiasForm" property="incidencias" cols="70" rows="3" style="overflow:auto" styleClass="boxComboConsulta" value="<%=INCIDENCIAS%>" readOnly="true"></html:textarea>
			<%}else{%>
				<html:textarea name="DefinirPermutaGuardiasForm" property="incidencias" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="70" rows="3" style="overflow:auto" styleClass="boxCombo" value="<%=INCIDENCIAS%>" readOnly="false"></html:textarea>
			<%}%>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>
<%			
	/////////////////////////
	// DCG 
	boolean hayEJG = false;
	boolean hayDesigna = false;
	
	if ((ANIOEJG   != null) && (!ANIOEJG.equals("")))    hayEJG |= true;
	if ((NUMEROEJG != null) && (!NUMEROEJG.equals("")))  hayEJG |= true;
	if ((DESIGNA_ANIO   != null) && (!DESIGNA_ANIO.equals("")))    hayDesigna |= true;
	if ((DESIGNA_NUMERO != null) && (!DESIGNA_NUMERO.equals("")))  hayDesigna |= true;

	if (hayDesigna || hayEJG) { %>
	<table width="100%">
	<tr>
	<td>
	<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.relacionado">
	<%	if (hayEJG) { %>
		<fieldset>
		<table width="100%" border="0" >
		<tr>
			<td class="labelText"  width="100">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.ejg'/>
			</td>
			<td class="labelText" width="100">	
			
				<siga:Idioma key='gratuita.mantAsistencias.literal.tipo'/>
			</td>
			<td class="labelTextValor" width="200">	
			<% if(TIPOEJG!=null && TIPOEJG.length()>70) TIPOEJG = TIPOEJG.substring(0,69); 	%>
				<%=TIPOEJG%>
			</td>
			<%// Recuperamos el nombre del interesado usando el mismo metodo que nos devuelve la cabecera de los EJG
				String nombreIntEJG = "", apellido1IntEJG = "", apellido2IntEJG = "";
				ScsEJGAdm admEJG = new ScsEJGAdm(usr);

				Hashtable nombreInteresadoEJG = admEJG.getTituloPantallaEJG(usr.getLocation(),
						ANIOEJG, NUMEROEJG, IDTIPOEJG);

				if (hTitulo != null) {
					nombreIntEJG = (String) nombreInteresadoEJG.get(ScsPersonaJGBean.C_NOMBRE);
					apellido1IntEJG = (String) nombreInteresadoEJG
							.get(ScsPersonaJGBean.C_APELLIDO1);
					apellido2IntEJG = (String) nombreInteresadoEJG
							.get(ScsPersonaJGBean.C_APELLIDO2);

				}
			%>
			<td class="labelTextValue" width="300">	
				
				<%=UtilidadesString.mostrarDatoJSP(ANIOEJG)%>/<%=UtilidadesString.mostrarDatoJSP(CODIGO_EJG)%>
					- <%=UtilidadesString.mostrarDatoJSP(nombreIntEJG)%> <%=UtilidadesString.mostrarDatoJSP(apellido1IntEJG)%> <%=UtilidadesString.mostrarDatoJSP(apellido2IntEJG)%>
			</td>
			<!--<td class="labelTextValor" width="50">	
				< %=ANIOEJG%>
			</td>-->
			<td class="labelText" style="display:none">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.numero'/>
			</td>
			<td class="labelTextValor" style="display:none">	
				<%=NUMEROEJG%>
			</td>
			
			<!--<td class="labelTextValor" width="100">	
				< %=CODIGO_EJG%>
			</td>-->			
			
			<!--<td class="labelTextValor" width="200">	
				< %=TIPOEJG%>
			</td>-->
			<!--<td class="labelText">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.fecha'/>
			</td>-->
			<!--<td class="labelTextValor" width="90">	
				< %=FECHAAPERTURA%>
			</td>-->
<%		if (!esFichaColegial){ 
			if (modo.equalsIgnoreCase("ver")) {
%>
				<td  align="right">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarEJG'/>" name="" border="0" onclick="consultarEJGFuncion('ver')">
				</td>
			<% } else { %>
				<td  align="right">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarEJG'/>" name="" border="0" onclick="consultarEJGFuncion('ver')">
					<img src="<%=app%>/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.EditarEJG'/>" name="" border="0" onclick="consultarEJGFuncion('<%=modo%>')">
					<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.BorrarEJG'/>" name="" border="0" onclick="borrarRelacionConEJG()">
				</td>
			<% } %>
<%		}%>
		</tr>
		</table>
		</fieldset>
	<% } %>
	<% if (hayDesigna) { %>
		<fieldset>
		<table width="100%" border="0" style="table-layout:fixed">
		<tr >
			<td class="labelText"  width="100">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.designa'/>
			</td>
			<td class="labelText" width="100">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.turno'/>
			</td>
			<td class="labelTextValor"  width="400">	
				<%=DES_DESIGNA_TURNO%>
			</td>
			<td class="labelTextValue" >
			
			<% // Recuperamos el nombre del interesado usando el mismo metodo que nos devuelve la cabecera de las designaciones
				String nombreIntDes = "", apellido1IntDes = "", apellido2IntDes = "";
				ScsDesignaAdm admDes = new ScsDesignaAdm(usr);
				Hashtable nombreInteresadoDesigna = admDes.getTituloPantallaDesigna(usr.getLocation(),
						DESIGNA_ANIO, DESIGNA_NUMERO, DESIGNA_TURNO);
	
				if (hTitulo != null) {
					nombreIntDes = (String) nombreInteresadoDesigna.get(ScsPersonaJGBean.C_NOMBRE);
					apellido1IntDes = (String) nombreInteresadoDesigna
							.get(ScsPersonaJGBean.C_APELLIDO1);
					apellido2IntDes = (String) nombreInteresadoDesigna
							.get(ScsPersonaJGBean.C_APELLIDO2);
				}
			%>
				<%=UtilidadesString.mostrarDatoJSP(DESIGNA_ANIO)%>/<%=UtilidadesString.mostrarDatoJSP(CODIGO)%> - <%=UtilidadesString.mostrarDatoJSP(nombreIntDes)%> <%=UtilidadesString.mostrarDatoJSP(apellido1IntDes)%> <%=UtilidadesString.mostrarDatoJSP(apellido2IntDes)%>
					
					<!--<input type="text"  class="boxConsulta" value="< %=DESIGNA_ANIO%>" readOnly="true">/<input type="text"  class="boxConsulta" value="< %=DESIGNA_CODIGO%>" readOnly="true">-->
			</td>
			<!--<td class="labelText">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.anio'/>
			</td>
			<td class="labelTextValor" width="50">	
				< %=DESIGNA_ANIO%>
			</td>-->
		   <td class="labelText"  style="display:none" width="0">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.numero'/>
			</td>
			<td class="labelTextValor"  style="display:none" width="0">	
				<%=DESIGNA_NUMERO%>
			</td>
			<!--<td class="labelText" >	
				<siga:Idioma key='gratuita.mantAsistencias.literal.numero'/>
			</td>-->
			<!--<td class="labelTextValor" width="100">	
				< %=CODIGO%>
			</td>-->
			
			<!--<td class="labelText">	
				<siga:Idioma key='gratuita.mantAsistencias.literal.fecha'/>
			</td>
			<td class="labelTextValor" width="90">	
				< %=FECHAENTRADA%>
			</td>-->

<%		if (!esFichaColegial){ 
			if (modo.equalsIgnoreCase("ver")) { 
%>
			<td align="right">
				<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="" border="0" onclick="consultarDesignaFuncion('ver')">
			</td>
			<% } else { %>
				<td  align="right">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="" border="0" onclick="consultarDesignaFuncion('ver')">
					<img src="<%=app%>/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.EditarDesigna'/>" name="" border="0" onclick="consultarDesignaFuncion('<%=modo%>')">
					<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.BorrarDesigna'/>" name="" border="0" onclick="borrarRelacionConDesigna()">
				</td>
			<% } %>
<%		}%>

		</tr>
		</table>
		</fieldset>
		<% } %>
	</siga:ConjCampos>
	</td>
	</tr>
	</table>

	<% } %>
	

</td>
</tr>
	</html:form>

</table>
	
	<html:form action = "/JGR_Designas.do" method="POST" target="submitArea">
		<html:hidden property = "actionModal" value= ""/>
		<html:hidden property = "modo" value= "ver"/>
		<html:hidden property = "numeroAsistencia" value="<%=NUMERO%>"/>
		<html:hidden property = "anioAsistencia"   value="<%=ANIO%>"/>
		<html:hidden property = "desdeAsistencia"  value= "si"/>
		<html:hidden property = "juzgadoAsi"       value= "<%=juzgadoAsi%>"/>
		<html:hidden property = "juzgadoInstitucionAsi"  value= "<%=juzgadoInstitucionAsi%>"/>
		
		<html:hidden property = "idTurno"     value="<%=idTurno%>"/>
		<html:hidden property = "juzgado"     value="<%=juzgado%>"/>
		<html:hidden property = "ncolegiado"  value="<%=NUMEROCOLEGIADO%>"/>
		<html:hidden property = "nombre"      value="<%=nombreCompletoLetrado%>"/>
		
		<html:hidden property = "diligencia"     	value="<%=numeroDiligenciaAsi%>"/>
		<html:hidden property = "procedimiento"     value="<%=numeroProcedimientoAsi%>"/>
		<html:hidden property = "comisaria"     	value="<%=comisariaAsi%>"/>
		
		<html:hidden property = "numero"     			value=""/>
		<html:hidden property = "idInstitucion"     	value=""/>
		<html:hidden property = "anio"     				value=""/>

		
	</html:form>		
	
	<html:form action = "/JGR_MantenimientoDesignas.do" method="POST" target="mainWorkArea">
		<html:hidden property = "modo"			value= "<%=modo%>"/>
		<html:hidden property = "anio"   		value= "<%=DESIGNA_ANIO%>"/>
		<html:hidden property = "numero"  		value= "<%=DESIGNA_NUMERO%>"/>
		<html:hidden property = "idTurno" 		value= "<%=DESIGNA_TURNO%>"/>
		<html:hidden property = "idInstitucion" value= "<%=usr.getLocation()%>"/>
		<html:hidden property = "desdeEjg"		value= "si"/>
	</html:form>		
	
	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea">
		<html:hidden property = "modo" 			value= "<%=modo%>"/>
		<html:hidden property = "anio"    		value= "<%=ANIOEJG%>"/>
		<html:hidden property = "numero"  		value= "<%=NUMEROEJG%>"/>
		<html:hidden property = "idTipoEJG" 	value= "<%=IDTIPOEJG%>"/>
		<html:hidden property = "idInstitucion" value= "<%=usr.getLocation()%>"/>
		<html:hidden property = "desdeEjg"		value= "si"/>
	</html:form>		

	<!-- formulario para buscar Tipo SJCS -->
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
	</html:form>
	
	 <html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		
	</html:form>	
	<html:form action = "/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarComisaria">
		<html:hidden property = "codigoExtBusqueda" value=""/>
	</html:form>
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Consultar Designa -->
		function consultarDesignaFuncion(modo) {
		   	document.forms[2].modo.value = modo;
		   	document.forms[2].submit();
	 	}

		<!-- Asociada al boton Consultar EJG -->
		function consultarEJGFuncion(modo) {
		   	document.forms[3].modo.value = modo;
		   	document.forms[3].submit();
	 	}
	
		<!-- Asociada al boton Abrir -->
		function accionCrearEJG()
		{	
			document.forms[0].modo.value = "editar";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
//			refrescarLocal();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinirEJGForm){
					numero.value        = resultado[1];
					idTipoEJG.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "editar";
//					target.value		= "mainWorkArea";
			   		submit();
				}
			}
		}

		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		function accionRestablecer() 
		{		
			parent.buscar();
		}
		
		function refrescarLocal()
		{
			parent.buscar();
		}

		function accionGuardar() 
		{	sub();	
			var fecha = "<%=FECHAHORA%>";
			var fechaCierre = document.forms[0].fechaCierre.value;
			var fi = fecha.substring(6,10)+fecha.substring(3,5)+fecha.substring(0,2);
			var ff = fechaCierre.substring(6,10)+fechaCierre.substring(3,5)+fechaCierre.substring(0,2);
			if(fechaCierre != "" && fi>ff)
			{
				alert("<siga:Idioma key='gratuita.mantAsistencias.mensaje.alert1'/>");
				fin();
				return false;
			}
			if(document.forms[0].idTipoAsistencia.value == "")
			{
				alert("<siga:Idioma key='gratuita.mantAsistencias.mensaje.alert2'/>");
				fin();
				return false;
			}
			/*if(document.forms[0].idTipoAsistenciaColegio.value == "")
			{
				alert("<siga:Idioma key='gratuita.mantAsistencias.mensaje.alert3'/>");
				return false;
			}*/
			
			document.forms[0].modo.value = "modificar";
			document.forms[0].target = "submitArea";
			document.forms[0].submit();
		}

		function accionVolver()
		{
			<%
			// indicamos que es boton volver
			ses.setAttribute("esVolver","1");
			%>
<%
			String sAction2 = esFichaColegial ? "JGR_AsistenciasLetrado.do" : "JGR_Asistencia.do";
%>
			document.forms[0].action = "<%=sAction2%>";
			document.forms[0].modo.value= "abrir";
			document.forms[0].submit();
		}
		
		function accionCrearDesignacion()
		{
			document.forms[1].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
//			refrescarLocal();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.BuscarDesignasForm){
					numero.value        = resultado[1];
					idTurno.value     = resultado[2];
					anio.value          = resultado[4];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}
		
		function relacionarConEJG() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="EJG";
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	
           
			if (resultado != null && resultado.length >= 4)
			{  
				document.forms[0].ejg_idInstitucion.value=resultado[0];
				document.forms[0].ejg_anio.value=resultado[1];
				document.forms[0].ejg_numero.value=resultado[2];
				document.forms[0].ejg_idTipoEJG.value=resultado[3];

				document.forms[0].modo.value= "relacionarConEJG";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
				
			}
		}
		
		function borrarRelacionConEJG() 
		{
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[0].modo.value="borrarRelacionConEJG";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		function relacionarConDesigna() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="DESIGNA";
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	

   
			if (resultado!= null  && resultado.length >= 4)
			{   document.forms[0].designa_idInstitucion.value=resultado[0];
				document.forms[0].designa_anio.value=resultado[1];
				document.forms[0].designa_numero.value=resultado[2];
				document.forms[0].designa_turno.value=resultado[3];

				document.forms[0].modo.value= "relacionarConDesigna";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		function borrarRelacionConDesigna() 
		{
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[0].modo.value="borrarRelacionConDesigna";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
			}
		}
		
		 function obtenerJuzgado() 
			{ 
			 if (document.forms[0].codigoExtJuzgado.value!=""){
				if(document.forms[0].idTurno.selectedIndex <= 0 ){
					alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1'/>");
					return;
				}else{		
 			       document.MantenimientoJuzgadoForm.codigoExt.value=document.forms[0].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.submit();	
				}
			 }
			}
		function traspasoDatos(resultado){
		 //seleccionComboSiga("juzgado",resultado[0]);
		 document.forms[0].juzgado.value=resultado[0];
		
		}
		// Funcion que obtiene la comisaria buscando por codigo externo	
			 function obtenerComisaria() 
			 { 
				  if (document.forms[0].codigoExtComisaria.value!=""){
					if(document.forms[0].idTurno.selectedIndex <= 0 ){
						alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1'/>");
						return;
					}else{	
					   document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.forms[0].codigoExtComisaria.value;
					   document.MantenimientoComisariaForm.submit();		
					}
				  }
			 }
		//			
		function traspasoDatosComisaria(resultado){
//		 seleccionComboSiga("comisaria",resultado[0]);
		 document.forms[0].comisaria.value=resultado[0];
		}	
		
	</script>

	
<!-- BOTONES DE CREACION DE DESIGNA O EJG -->
<%	String botonesDesignaEJG = "";

	if(accion != null && accion.equalsIgnoreCase("modificar")){
 		botonesDesignaEJG = esFichaColegial ? "g,r" : "g,r,v";
	}else{
 		botonesDesignaEJG = esFichaColegial ? "" : "v";
	}

	if (!esFichaColegial) {
		/////////////////////////
		// DCG Si estan los campos a nulos o sin datos mostramos el boton de desgina
		boolean condicion = true;
		if ((modo != null) && ((modo.equalsIgnoreCase("consulta")) || (modo.equalsIgnoreCase("ver")))) condicion &= false;
		else if ((DESIGNA_ANIO   != null) && (!DESIGNA_ANIO.equals("")))    condicion &= false;
		else if ((DESIGNA_NUMERO != null) && (!DESIGNA_NUMERO.equals("")))  condicion &= false;
		if ((IDPERSONAJG != null && !IDPERSONAJG.equals("")) && condicion && accion != null && accion.equalsIgnoreCase("modificar")) {
			botonesDesignaEJG += ",cd,rd";
		}
		/////////////////////////
		
		/////////////////////////
		// DCG Si estan los campos a nulos o sin datos mostramos el boton de EJG
		condicion = true;
		if ((modo != null) && ((modo.equalsIgnoreCase("consulta")) || (modo.equalsIgnoreCase("ver")))) condicion &= false;
		else if ((ANIOEJG   != null) && (!ANIOEJG.equals("")))    condicion &= false;
		else if ((NUMEROEJG != null) && (!NUMEROEJG.equals("")))  condicion &= false;
		/////////////////////////

		if ((IDPERSONAJG != null && !IDPERSONAJG.equals("")) && condicion && accion != null && accion.equalsIgnoreCase("modificar")) { 
			botonesDesignaEJG += ",ce,re";
		}
	}
%>	

<%
		// String sClasePestanas = esFichaColegial ? "botonesDetalle3" : "botonesDetalle";
		String sClasePestanas = "botonesDetalle";
%>
	<!-- INICIO: BOTONES BUSQUEDA -->	
		<siga:ConjBotonesAccion botones="<%=botonesDesignaEJG%>" clase="<%=sClasePestanas%>"/>
	<!-- FIN: BOTONES BUSQUEDA -->

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>