<!DOCTYPE html>
<html>
<head>
<!-- operarDatosGenerales.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage=""%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsSOJBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.beans.*"%>
<!-- JSP -->
<% 
String informeUnico =(String) request.getAttribute("informeUnico");
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
		
	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");	
	String accion = (String)ses.getAttribute("accion");	
	String persona = "", fechaApertura="",anio="",numero="", respuesta="",consulta="", estado="", idPersonaJG = "", descripcionTurno = "", descripcionGuardia = "", idTurno="", idGuardia="", ncolegiado="", nombre="",numeroSOJ="",sufijo="";
	String nSolicitaInfoJG="", nSolicitaJG="";
	String dato[] = {(String)usr.getLocation()};
	String anioEJG="",numeroEJG="",tipoEJG="";
	String t_nombreEJG = "", t_apellido1EJG = "", t_apellido2EJG = "", t_anioEJG = "", t_numeroEJG = "", t_tipoEJG="";
	String numColegiadoTram="",nomColegiadoTram="";
	try {
		if (miHash.get(ScsSOJBean.C_IDPERSONA) != null)	persona = miHash.get(ScsSOJBean.C_IDPERSONA).toString();
		anio = miHash.get(ScsSOJBean.C_ANIO).toString();
		fechaApertura = GstDate.getFormatedDateShort("",miHash.get(ScsSOJBean.C_FECHAAPERTURA).toString());
		numero = miHash.get(ScsSOJBean.C_NUMERO).toString();
		respuesta = miHash.get(ScsSOJBean.C_RESPUESTALETRADO).toString();
		consulta = miHash.get(ScsSOJBean.C_DESCRIPCIONCONSULTA).toString();											
		idPersonaJG = miHash.get(ScsSOJBean.C_IDPERSONAJG).toString();
		if (!miHash.get("DESCRIPCIONTURNO").toString().equals("")) descripcionTurno = miHash.get("DESCRIPCIONTURNO").toString();
		if (!miHash.get("DESCRIPCIONGUARDIA").toString().equals("")) descripcionGuardia = miHash.get("DESCRIPCIONGUARDIA").toString();
		ncolegiado = miHash.get("NCOLEGIADO").toString();
		idTurno = miHash.get("IDTURNO").toString();
		idGuardia = miHash.get("IDGUARDIA").toString();
		nombre = miHash.get("NOMBRE") + " " + miHash.get("APELLIDOS1") + " " + miHash.get("APELLIDOS2");
		numeroSOJ = (String)miHash.get("NUMSOJ");
		sufijo = (String)miHash.get("SUFIJO");
		numColegiadoTram=(String)miHash.get("NCOLEGIADO");
		nomColegiadoTram=(String)miHash.get("NOMBRE")+" "+(String)miHash.get("APELLIDOS1")+" "+(String)miHash.get("APELLIDOS2");
		
	} 
	catch (Exception e) {};
	
	if (miHash.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("A")) {
		estado = UtilidadesString.getMensajeIdioma(usr,"gratuita.SOJ.estado.abierto");
	}
	else if (miHash.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("P")){
		estado = UtilidadesString.getMensajeIdioma(usr,"gratuita.SOJ.estado.pendiente");
	} 
	else estado = UtilidadesString.getMensajeIdioma(usr,"gratuita.SOJ.estado.cerrado");
	
	ArrayList idTipoSOJ = new ArrayList(), idTipoSOJColegio = new ArrayList(), idTipoConsulta=new ArrayList(), idTipoRespuesta=new ArrayList(), idTipoConoce=new ArrayList(), idTipoGrupoLaboral=new ArrayList();
	
	try {
		idTipoSOJ.add(miHash.get(ScsSOJBean.C_IDTIPOSOJ).toString());
		idTipoSOJColegio.add(miHash.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString());
		idTipoConsulta.add(miHash.get(ScsSOJBean.C_IDTIPOCONSULTA).toString());
		idTipoRespuesta.add(miHash.get(ScsSOJBean.C_IDTIPORESPUESTA).toString());
		
	} catch (Exception e) {};
%>	
<bean:define id="szEJG" name="DatosGeneralesSOJForm" property="sizeEJG" type="java.lang.String"/>
<%boolean hayEJG=(!szEJG.equals("0"));%>

<% if (hayEJG){
   anioEJG=(String)miHash.get("EJGANIO");
   numeroEJG=(String)miHash.get("EJGNUMERO");
   tipoEJG=(String)miHash.get("EJGIDTIPOEJG");
       
		ScsEJGAdm EJGadm = new ScsEJGAdm (usr);
		Hashtable hTituloEJG = EJGadm.getTituloPantallaEJG(usr.getLocation(), anioEJG, numeroEJG,tipoEJG);

		if (hTituloEJG != null) {
			t_nombreEJG    = (String)hTituloEJG.get(ScsPersonaJGBean.C_NOMBRE);
			t_apellido1EJG = (String)hTituloEJG.get(ScsPersonaJGBean.C_APELLIDO1);
			t_apellido2EJG = (String)hTituloEJG.get(ScsPersonaJGBean.C_APELLIDO2);
			t_anioEJG      = (String)hTituloEJG.get(ScsEJGBean.C_ANIO);
			t_numeroEJG    = (String)hTituloEJG.get(ScsEJGBean.C_NUMEJG);
			t_tipoEJG   = (String)hTituloEJG.get("TIPOEJG");
		}
  }					
%>
					
<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="DatosGeneralesSOJForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.busquedaSOJ.ldatosGenerales" 
		localizacion="gratuita.busquedaSOJ.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>
		function refrescarLocal(){
			buscar();
		}		
	</script>
	
</head>

<body>
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">

		<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoSOJ="",t_sufijo;
						ScsDefinirSOJAdm adm = new ScsDefinirSOJAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaSOJ(usr.getLocation(), anio, numero,(miHash.get(ScsSOJBean.C_IDTIPOSOJ).toString()));
						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsSOJBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsSOJBean.C_NUMSOJ);
							t_sufijo    = (String)hTitulo.get(ScsSOJBean.C_SUFIJO);
							if (t_sufijo!=null && !t_sufijo.equals("")){
								t_numero =t_numero +"-"+t_sufijo;
							}
							t_tipoSOJ   = (String)hTitulo.get("TIPOSOJ");
						}
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> <%=UtilidadesString.mostrarDatoJSP(t_tipoSOJ)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
			<td>
				<%
					if (!accion.equalsIgnoreCase("ver")) {
				%>
				<table>
					<tr>
						<td>
						<input 	type="button" 
				alt='<siga:Idioma key="general.boton.cartaInteresados" />'  
		       	id="idButton"  
		       	onclick="return generarCarta();" 
		       	class="button" 
		       	value='<siga:Idioma key="gratuita.EJG.botonComunicaciones" />'/>
		       	
						</td>
					</tr>
				</table>
				<%
					}
				%>
				</td>
		</tr>
		</table>

	<table align="center"  width="100%">
	<html:form action="/JGR_PestanaSOJDatosGenerales" method="POST" target="mainPestanas">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersonaJG" value = "<%=idPersonaJG%>"/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idTurno" value = "<%=idTurno%>"/>
	<html:hidden property = "idPersona" value = "<%=persona%>"/>
	<html:hidden property = "idGuardia" value = "<%=idGuardia%>"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>
	<html:hidden property = "idInstitucion" value=""/>
	<html:hidden property = "anioEJG" value=""/>
	<html:hidden property = "numeroEJG" value=""/>
	<html:hidden property = "tipoEJG" value=""/>
	<html:hidden property = "numero" value="<%=numero%>"/>
	<input type="hidden" name = "flagSalto" value=""/>
    <input type="hidden" name = "flagCompensacion" value=""/>	
	<input type="hidden" name="nColegiadoTramitador"  value="<%=numColegiadoTram%>">
	
	<tr>				
	<td width="100%" align="center">
   
	<siga:ConjCampos leyenda="gratuita.operarEJG.literal.ServicioTramitacion">
	<table width="100%" border="0">
		<tr> 
			<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/> / <siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>	
			</td>
	
			<td  class="labelTextValor">
			<% if (sufijo!=null && !sufijo.equals("")){ %>
				<html:text name="DatosGeneralesSOJForm" property="anio" size="4" maxlength="4" styleClass="boxConsulta"  value="<%=anio%>" readonly="true"></html:text> / <html:text name="DatosGeneralesSOJForm" property="numSOJ" size="5" maxlength="10" styleClass="boxConsulta"  value="<%=numeroSOJ%>" readonly="true"></html:text>-<input type="text" class="boxConsulta" value="<%=sufijo%>" readOnly="true" style="width:100px">
			<%}else{%>
		  		<html:text name="DatosGeneralesSOJForm" property="anio" size="4" maxlength="4" styleClass="boxConsulta"  value="<%=anio%>" readonly="true"></html:text> / <html:text name="DatosGeneralesSOJForm" property="numSOJ" size="5" maxlength="10" styleClass="boxConsulta"  value="<%=numeroSOJ%>" readonly="true"></html:text>
			<% } %>
	    	</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
			</td>
			<td>	
				<%if(accion.equals("ver")){%>
					<input type="text" class="boxConsulta" value="<%=descripcionTurno%>" readOnly="true" style="width:300px">
				<%}else{
					ArrayList lista=new ArrayList();
					String cadena= usr.getLocation()+","+idTurno;
					lista.add(cadena);
				%>
					<siga:ComboBD nombre = "identificador" ancho="350" tipo="turnos" clase="boxCombo" obligatorio="false" pestana="t" accion="Hijo:identificador2" elementoSel="<%=lista%>" parametro="<%=dato%>"/>
				<%}%>
			</td>
			<td class="labelText">	
				<siga:Idioma key="gratuita.busquedaSOJ.literal.guardia"/>
			</td>
			<td>	
				<%if(accion.equals("ver")){%>
					<input type="text" class="boxConsulta" value="<%=descripcionGuardia%>" readOnly="true" style="width:200px">					
				<%}else{
					ArrayList lista=new ArrayList();
					String cadena= usr.getLocation()+","+idGuardia;
					lista.add(cadena);
					String[] datos={usr.getLocation(),idTurno};	
				%>
					<siga:ComboBD nombre = "identificador2" ancho="350" tipo="guardias" clase="boxCombo" pestana="t" obligatorio="false" hijo="t" parametro="<%=datos%>" elementoSel="<%=lista%>"/>									
				<%}%>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			<siga:BusquedaSJCS nombre="DatosGeneralesSOJForm" propiedad="buscaLetrado"
		 				   concepto="SOJ" operacion="Asignacion" botones="M"
						   campoTurno="identificador" campoGuardia="identificador2" campoFecha="fechaApertura"
						   campoPersona="idPersona" campoColegiado="NColegiado" campoNombreColegiado="nomColegiado"  
						   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
						   diaGuardia="false"
						   modo="<%=accion%>"
    					   />							   
			</td>
		</tr>
		<tr>
			<td class="labelText">	
				<siga:Idioma key='gratuita.operarEJG.literal.tramitador'/>
			</td>
			<td class="labelTextValue">
				<input type="text" name="NColegiado" class="boxConsulta" readOnly value="<%=numColegiadoTram%>" style="width:'100px';">-<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="<%=nomColegiadoTram%>" style="width:'240px';">
			</td>
		</tr>
	
	</table>	
</siga:ConjCampos>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.expedientesSOJ">

	<table align="center" width="100%" border="0">
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoSOJ"/>
	</td>				
	<td class="labelTextValue">
		<siga:ComboBD nombre="idTipoSOJ" tipo="tipoSOJ" clase="boxConsulta" ancho="350" obligatorio="false" parametro="<%=dato%>" pestana="t" elementoSel="<%=idTipoSOJ%>" readonly="true"/>
	</td>	
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.SOJColegio"/>
	</td>				
	<td class="labelTextValue">
		<siga:ComboBD nombre="idTipoSOJColegio" tipo="tipoSOJColegio" clase="boxConsulta" obligatorio="false" parametro="<%=dato%>" pestana="t" elementoSel="<%=idTipoSOJColegio%>" readonly="true"/>
	</td>	
	</tr>
	
	
	
	<tr>	
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.estadoSOJ"/>
	</td>
	<td class="labelTextValue">
	<%if (accion.equalsIgnoreCase("ver")){%><%if (estado.startsWith("A")) {%><siga:Idioma key="gratuita.SOJ.estado.abierto"/><%} else if (estado.startsWith("P")){%><siga:Idioma key="gratuita.SOJ.estado.pendiente"/><%} else {%><siga:Idioma key="gratuita.SOJ.estado.cerrado"/><%}%>
	<%} else {%>
		<select name="estadoSOJ" class="boxCombo">
			<option value="A" <%if (estado.startsWith("A")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.abierto"/></option>
			<option value="P" <%if (estado.startsWith("P")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.pendiente"/></option>
			<option value="C" <%if (estado.startsWith("C")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.cerrado"/></option>
		</select>
	<%}%>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.fechaApertura"/>
	</td>				
	<td class="labelTextValue">
		<input type="text" name="fechaApertura" class="boxConsulta" value="<%=fechaApertura%>" readonly="true" size="10">
	</td>
	</tr>
	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoConsulta"/>
	</td>				
	<td class="labelTextValue">
	<%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "tipoConsulta" tipo="cmbTipoConsulta" clase="boxConsulta"  ancho="300" readonly="true" parametro="<%=dato%>" elementoSel="<%=idTipoConsulta%>" />		
	<%} else {%>	
	   <siga:ComboBD nombre = "tipoConsulta" tipo="cmbTipoConsulta" clase="boxCombo"  parametro="<%=dato%>" elementoSel="<%=idTipoConsulta%>" />		
	<%}%>				
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoRespuesta"/>
	</td>				
	<td class="labelTextValue">
	  <%if (accion.equalsIgnoreCase("ver")){%>
		<siga:ComboBD nombre = "tipoRespuesta" tipo="cmbTipoRespuesta" clase="boxConsulta" ancho="300" readonly="true" parametro="<%=dato%>"  elementoSel="<%=idTipoRespuesta%>" />	
	  <%} else {%>	
	    <siga:ComboBD nombre = "tipoRespuesta" tipo="cmbTipoRespuesta" clase="boxCombo" parametro="<%=dato%>"  elementoSel="<%=idTipoRespuesta%>" />	
	  <%}%>		
	</td>
	</tr>
	
	<tr>
	<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.operarSOJ.literal.descripcionConsulta"/>	
	</td>
	<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.operarSOJ.literal.respuestaLetrado"/>	
	</td>
	</tr>
	
	<tr>
	<%int rows= (hayEJG?6:10);%>
	<td class="labelText"  colspan="2">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<textarea name="descripcionConsulta" cols="70" rows="<%=rows%>" readonly="true" class="boxConsulta" style="overflow-y:auto; overflow-x:hidden; width:470px; height:200px; resize:none;"><%=consulta%></textarea>
		<%} else {%>
			<textarea name="descripcionConsulta" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" cols="70" rows="<%=rows%>" class="box" style="overflow-y:auto; overflow-x:hidden; width:470px; height:200px; resize:none;"><%=consulta%></textarea>
		<%}%>
	</td>
	<td class="labelText"  colspan="2">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<textarea name="respuestaLetrado" cols="70" rows="<%=rows%>" readonly="true" class="boxConsulta" style="overflow-y:auto; overflow-x:hidden; width:450px; height:200px; resize:none;"><%=respuesta%></textarea>
		<%} else {%>
			<textarea name="respuestaLetrado" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" cols="70" rows="<%=rows%>" class="box" style="overflow-y:auto; overflow-x:hidden; width:450px; height:200px; resize:none;"><%=respuesta%></textarea>
		<%}%>
	</td>
	</tr>
	
	</table>
	</siga:ConjCampos>
	</td>
	</tr>
	<%if(hayEJG){%>					
	<tr>
	  <td>
		<siga:ConjCampos leyenda="gratuita.mantAsistencias.literal.relacionado">
		  
		  <table class="tablaCampos" align="center" width="100%">
			<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarEJG.literal.EJG"/>
			</td>
			<td class="labelText" >
				<siga:Idioma key="gratuita.operarEJG.literal.tipo"/>
			</td>
			<td class="labelText">
				<label class="boxConsulta"><bean:write name="DatosGeneralesSOJForm" property="descTipoEJG"/></label>
			</td>
			<td class="labelTextValue">
			<%=UtilidadesString.mostrarDatoJSP(t_anioEJG)%>/<%=UtilidadesString.mostrarDatoJSP(t_numeroEJG)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombreEJG)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1EJG)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2EJG)%>
			</td>
			<!--<td class="labelText">
				<siga:Idioma key="gratuita.operarEJG.literal.anio"/>
			</td>
			<td class="labelText">
				<label class="boxConsulta"><bean:write name="DatosGeneralesSOJForm" property="anioEJG"/></label>
			</td>
			<td class="labelText">	
				<siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
			</td>
			<td class="labelText">
				<label class="boxConsulta"><bean:write name="DatosGeneralesSOJForm" property="codigoEJG"/></label>
			</td>-->
			<td class="labelText" style="display:none">
				<siga:Idioma key="gratuita.operarEJG.literal.numer"/>
			</td>
			
			<td class="labelText" style="display:none">
				<label class="boxConsulta"><bean:write name="DatosGeneralesSOJForm" property="numeroEJG"/></label>
			</td>
			
			<!--<td class="labelText">
				<siga:Idioma key="gratuita.operarEJG.literal.fecha"/>
			</td>
			<td class="labelText">
 				<bean:define id="sFECHAAPERTURA" name="DatosGeneralesSOJForm" property="fechaAperturaEJG" type="java.lang.String"/>
				<label class="boxConsulta">< %=GstDate.getFormatedDateShort("",sFECHAAPERTURA)%></label>
			</td>-->
			<%	if (accion.equalsIgnoreCase("ver")) {  %>
				<td class="labelText" style="text-align:right">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="consultarEJG" border="0" onclick="consultarEJGFuncion('ver')">
				</td>
			<% } else { %>
				<td class="labelText" style="text-align:right">
					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarDesigna'/>" name="consultarEJG" border="0" onclick="consultarEJGFuncion('ver')">
					<img src="<%=app%>/html/imagenes/beditar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.EditarDesigna'/>" name="" border="0" onclick="consultarEJGFuncion('<%=accion%>')">
					<img src="<%=app%>/html/imagenes/bborrar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.boton.BorrarDesigna'/>" name="" border="0" onclick="borrarRelacionConEJG()">
				</td>
			<% } %>
			
			
		    </tr>
		  </table>
		</siga:ConjCampos>
	  </td>
	</tr>
	<%}%>					
	</html:form>
</table>

	<%if(hayEJG){%>		
	<siga:ConjBotonesAccion botones="V,G,R"  modo="<%=accion%>" clase="botonesDetalle"/>
	<%}else{%>	
	<siga:ConjBotonesAccion botones="V,CE,re,G,R"  modo="<%=accion%>" clase="botonesDetalle"/>
	<%}%>	
	
	
	<!-- formulario para buscar Tipo SJCS -->
	<html:form action="/JGR_BusquedaPorTipoSJCS.do" method="POST" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrir">
		<input type="hidden" name="tipo"        value="">
	</html:form>
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property="idTipoInforme" value='SOJ'/>
	<html:hidden property="enviar" value = "0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>

<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
	function generarCarta()
	{
	sub();
	var idInst = document.DatosGeneralesSOJForm.idInstitucion.value;
	var idTipo = document.DatosGeneralesSOJForm.idTipoSOJ.value;
	var anio = document.DatosGeneralesSOJForm.anio.value;
	var numero = document.DatosGeneralesSOJForm.numero.value;
	datos = "idinstitucion=="+idInst + "##idtipo==" +idTipo+"##anio=="+anio +"##numero==" +numero+"%%%";
	
	
	
		document.InformesGenericosForm.datosInforme.value=datos;
		if(document.getElementById("informeUnico").value=='1'){
			document.InformesGenericosForm.submit();
		}else{
		
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			  fin(); 		
		   	} 
		   	else {
		   		fin();
		   	}
		}
		

	}
		
	
		function accionCrearEJG() 
		{	
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");

			if (resultado){
				if(resultado[0]=="MODIFICADO"){
				with(document.DefinirEJGForm){
					numero.value        = resultado[1];
					idTipoEJG.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "editar";
			   		submit();
				}
			}
		   }	
		}
	
		function relacionarConEJG() 
		{
			document.BusquedaPorTipoSJCSForm.tipo.value="EJG";
			var resultado = ventaModalGeneral("BusquedaPorTipoSJCSForm","G");	

			if (resultado != null && resultado.length >= 4)
			{
				document.forms[0].idInstitucion.value = resultado[0];
				document.forms[0].anioEJG.value       = resultado[1];
				document.forms[0].numeroEJG.value     = resultado[2];
				document.forms[0].tipoEJG.value       = resultado[3];
				document.forms[0].modo.value          = "relacionarConEJG";
				document.forms[0].target              = "submitArea";
				document.forms[0].submit();
			}
		}
	<!-- Asociada al boton Consultar EJG -->
		function consultarEJGFuncion(_modo)
		{
			with(document.DefinirEJGForm){
				idTipoEJG.value     = '<bean:write name="DatosGeneralesSOJForm" property="tipoEJG"/>';
				anio.value          = '<bean:write name="DatosGeneralesSOJForm" property="anioEJG"/>';
				numero.value        = '<bean:write name="DatosGeneralesSOJForm" property="numeroEJG"/>';
				fechaApertura.value = '<bean:write name="DatosGeneralesSOJForm" property="fechaAperturaEJG"/>';
				modo.value          = _modo;
		   		submit();
			}
	 	}

		function borrarRelacionConEJG() 
		{
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>"))
			{
				document.forms[0].idInstitucion.value = "<%=usr.getLocation()%>";
				document.forms[0].tipoEJG.value       = '<bean:write name="DatosGeneralesSOJForm" property="tipoEJG"/>';
				document.forms[0].anioEJG.value       = '<bean:write name="DatosGeneralesSOJForm" property="anioEJG"/>';
				document.forms[0].numeroEJG.value     = '<bean:write name="DatosGeneralesSOJForm" property="numeroEJG"/>';
				document.forms[0].modo.value          = "borrarRelacionConEJG";
				document.forms[0].target              = "submitArea";
				document.forms[0].submit();
			}
		}

		function refrescarLocal() {
			document.location.reload();
		}
	
		//Asociada al boton Volver -->
		function accionVolver() {	
			document.forms[0].action="./JGR_ExpedientesSOJ.do";	
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
		function buscar() {		
			document.forms[0].modo.value = "abrir";
			document.forms[0].submit();
		}

		// Asociada al boton Restablecer -->
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		// Asociada al boton Guardar -->
		function accionGuardar()	{
			sub();
			var descripcionConsulta = document.forms[0].descripcionConsulta.value;
			var respuestaLetrado = document.forms[0].respuestaLetrado.value;
			if (descripcionConsulta.length <= 4000) {
				if (respuestaLetrado.length <= 4000) {
					//if (validateDatosGeneralesSOJForm(document.forms[0])){
					    var datosTurno =  document.forms[0].identificador.value.split(",");
						if (datosTurno!=""){
						  document.forms[0].idTurno.value=datosTurno[1];
						}else{
						 document.forms[0].idTurno.value="";
						}
						document.forms[0].idGuardia.value=document.forms[0].identificador2.value;
						document.forms[0].target="submitArea";
						document.forms[0].modo.value="Modificar";
						document.forms[0].submit();
					//}
				}
				else{
					 alert('<siga:Idioma key="gratuita.operarDatosGeneralesSOJ.message.respuestaLetrado"/>');
					 fin();
					 return false;
				}	
			}
			else{ 
				alert('<siga:Idioma key="gratuita.operarDatosGeneralesSOJ.message.longitudCosulta"/>');
				fin();
				return false;
			}
		}			
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
			

	<html:form action="/JGR_EJG.do" method="post" target="mainWorkArea">
		<input type="hidden" name="idTipoEJG"/>
		<input type="hidden" name="anio"/>
		<input type="hidden" name="numero"/>
		<input type="hidden" name="fechaApertura"/>
		<input type="hidden" name="idInstitucion" value="<%=usr.getLocation()%>"/>
		<input type="hidden" name="origen" value="SOJ"/>
		<input type="hidden" name="modo" />
		<input type="hidden" name="modoActualSOJParaVolver" value="<%=accion%>"/>
	</html:form>

	</body>
</html>
