<!-- consultaTurno.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ScsTurnoAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.gratuita.form.DefinirTurnosForm"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
// obtenemos valores
	String  action = (String) request.getAttribute("action");
	String  modo = (String) request.getAttribute("modo");
	String  paso = (String) request.getAttribute("paso");
	String  titulo = (String) request.getAttribute("titulo");
	Integer idinstitucion = (Integer) request.getAttribute("idinstitucion");
	Integer idturno 			= (Integer) request.getAttribute("idturno");
	Integer idpersona 		= (Integer) request.getAttribute("idpersona");
	DefinirTurnosForm miform=(DefinirTurnosForm) request.getAttribute("DefinirTurnosFormOrdenacion");
// obtenesmos el resultado de la conswuta
	Vector obj = (Vector) request.getAttribute("resultado");
	Hashtable hash = (Hashtable)obj.get(0);

// Preparamos los botones de accion
	String botones = "";
	botones = "S,X";
	String[] dato1 = {usr.getLocation()};
	String[] dato2 = {usr.getLocation()};
	
// estilos

String clase="boxConsulta";	

%>

<html>
<head>
	<title>meter el titulo</title>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

<script language="JavaScript">

		//Asociada al boton Volver
		function accionVolver() 
		{		
			
		}

		//Asociada al boton Cancelar
		function accionCancelar() 
		{	
			window.close();	
		}
		
		//Asociada al boton Cancelar
		function accionCerrar() 
		{		
			window.close();
		}

	//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar
		function accionGuardar() 
		{		
		}

		//Asociada al boton Siguiente
		function accionSiguiente() 
		{	sub();
			document.forms[0].target="_self";
			document.forms[0].submit();
		}

</script>
	
</head>

<body>
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="<%=titulo%>"/>
		</td>
	</tr>
	</table>


	<html:form action = "<%=action%>" method="POST" target="_top" enctype="multipart/form-data" onSubmit="return Buscar()">
<% String solBaja = (String) request.getAttribute("solBaja"); 
   if (solBaja==null) solBaja="";
%>   
	<input type="hidden" name="solBaja" value="<%=solBaja%>"/>
	<input type="hidden" name="modo" value="<%=modo%>"/>
	<input type="hidden" name="paso" value="<%=paso%>"/>
	<input type="hidden" name="guardias" value="<%=(String)hash.get("GUARDIAS")%>"/>
	<input type="hidden" name="idPersona" value="<%=idpersona%>"/>
	<input type="hidden" name="idInstitucion" value="<%=idinstitucion%>"/>
	<input type="hidden" name="idTurno" value="<%=idturno%>"/>
	<input type="hidden" name="observacionesSolicitud" value="<%=(String)hash.get("OBSERVACIONESSOLICITUD")%>"/>
	<input type="hidden" name="fechaSolicitud" value="<%=(String)hash.get("FECHASOLICITUD")%>"/>
	<input type="hidden" name="observacionesBaja" value="<%=(String)hash.get("OBSERVACIONESBAJA")%>"/>
	<input type="hidden" name="fechaSolicitudBaja" value="<%=(String)hash.get("FECHASOLICITUDBAJA")%>"/>
	<input type="hidden" name="observacionesValidacion" value="<%=(String)hash.get("OBSERVACIONESVALIDACION")%>"/>
	<input type="hidden" name="fechaValidacion" value="<%=(String)hash.get("FECHAVALIDACION")%>"/>
	<input type="hidden" name="validarInscripciones" value="<%=(String)hash.get("VALIDARINSCRIPCIONES")%>"/>

	<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.datosGenerales">
		
	<table width="100%" border=1>
	<tr>
		<td class="labelText" style="text-align:left">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>
		</td>
		<td class="labelText" style="text-align:left">
			<input name="abreviatura" type="text" class="boxConsulta" size="25" maxlength="25" value="<%=(String)hash.get("ABREVIATURA")%>" readonly>
		</td>
		<td class="labelText" style="text-align:left">
			<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/> 
		</td>
		<td class="labelText" style="text-align:left">
			<input name="nombre" type="text" class="boxConsulta" size="50" maxlength="100" value="<%=(String)hash.get("NOMBRE")%>" readonly>
		</td>
	</tr>
	<tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>
		 </td>
		<td class="labelText" style="text-align:left">
			<input name="area" type="text" class="boxConsulta" value="<%=(String)hash.get("AREA")%>" readonly>
		</td>
		<td class="labelText" style="text-align:left">
		<siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/> 
		</td>
		<td class="labelText" style="text-align:left">
			<input name="materia" type="text" class="boxConsulta" value="<%=(String)hash.get("MATERIA")%>" readonly size=30>
		</td>
	</tr>
	<tr>
		<td class="labelText" style="text-align:left">
			<siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/> 
		</td>
		<td class="labelText"  style="text-align:left">
			<input name="zona" type="text" class="boxConsulta" value="<%=(String)hash.get("ZONA")%>" readonly>
		</td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/> </td>
		<td class="labelText" style="text-align:left">
			<input name="subzona" type="text" class="boxConsulta" value="<%=(String)hash.get("SUBZONA")%>" readonly>
		</td>
	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/> </td>
		<td colspan="3" class="labelText" style="text-align:left">
			<input name="partidojudicial" type="text" class="boxConsulta" value="<%=(String)hash.get("PARTIDOS")%>" readonly size=30>
		</td>
	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidaPresupuestaria"/> </td>
		<td colspan="3" class="labelText" style="text-align:left">
			<input name="partidaPresupuestaria" type="text" style="width: 500" class="boxConsulta" value="<%=(String)hash.get("PARTIDAPRESUPUESTARIA")%>" readonly>
		</td>
	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/> </td>
		<td colspan="3" class="labelText" style="text-align:left">
			<input name="grupoFacturacion" type="text" style="width: 500" class="boxConsulta" value="<%=(String)hash.get("GRUPOFACTURACION")%>" readonly>
		</td>
	</tr>
	<tr>
		<td class="labelText" style="text-align:left">
		<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>
		</td>		
		<td class="labelText" style="text-align:left">
			<textarea name="descripcion" class="boxConsulta" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="4" cols="50" readonly><%=(String)hash.get("DESCRIPCION")%></textarea>
		</td>		
		<td class="labelText" style="text-align:left">
			<siga:Idioma key="gratuita.maestroTurnos.literal.requisitosAcceso"/>
		</td>		
		<td class="labelText" style="text-align:left">
			<textarea onKeyDown="cuenta(this)" name="requisitos" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="4" cols="50" readonly class="boxConsulta"><%=(String)hash.get("REQUISITOS")%></textarea>
		</td>
	</tr>
	</table>
		</siga:ConjCampos>
		<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.guardias">
		<table width="100%" border="0" align="center">
		<tr>
		<%if(((String)hash.get("GUARDIAS")).equals("0")){%>
			<td class="labelText" style="text-align:center">
				<INPUT NAME="gu" TYPE=RADIO VALUE="0" checked readonly> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.obligatorias"/> 
			</td>
			<%}
			else if(((String)hash.get("GUARDIAS")).equals("1")){
			%>
			<td class="labelText"  style="text-align:center">
				<INPUT NAME="gu" TYPE=RADIO VALUE="1" checked readonly > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.todasNinguna"/>
			</td>
			<%}
			else if(((String)hash.get("GUARDIAS")).equals("2")){
			%>
			<td class="labelText" style="text-align:center">
				<INPUT NAME="gu" TYPE=RADIO VALUE="2" checked readonly > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.elegir"/>
			</td>
			<%}%>
		</tr>
		</table>
		</siga:ConjCampos>
		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.configuracion">
		<table border="0" align="center" width="100%">
			<tr>
				<td class="labelText" style="text-align:left">
					<siga:Idioma key="gratuita.maestroTurnos.literal.validarJustificaciones"/>
					<% if(((String)hash.get("VALIDARJUSTIFICACIONES")).equals("S")){
						%>
					<INPUT NAME="validarJustificaciones" TYPE=CHECKBOX disabled checked>
					<%}else{%>
					<INPUT NAME="validarJustificaciones" TYPE=CHECKBOX disabled>
					<%}%>
				</td>
				<td class="labelText" style="text-align:left">
					<siga:Idioma key="gratuita.maestroTurnos.literal.validarInscripciones"/>
						<% if(((String)hash.get("VALIDARINSCRIPCIONES")).equals("S")){
						%>
						<INPUT NAME="validacionInscripciones" TYPE=CHECKBOX disabled checked>
						<%}else{%>
						<INPUT NAME="validacionInscripciones" TYPE=CHECKBOX disabled>
						<%}%>
				</td>
			</tr>
		</table>
		</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
			<table border="0" width="100%" align="center">
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% 
						String valor1 = "";
						if (miform.getCrit_1().equalsIgnoreCase("0")) {
							valor1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
						} else
						if (miform.getCrit_1().equalsIgnoreCase("1")) {
							valor1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
						} else
						if (miform.getCrit_1().equalsIgnoreCase("2")) {
							valor1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
						} else
						if (miform.getCrit_1().equalsIgnoreCase("3")) {
							valor1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
						} else
						if (miform.getCrit_1().equalsIgnoreCase("4")) {
							valor1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
						} 

						String orden1 = "";
						if (miform.getOrd_1().equalsIgnoreCase("A")) {
							orden1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
						} else
						if (miform.getOrd_1().equalsIgnoreCase("D")) {
							orden1=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
						}
					%>
						<input type="text" name="crit_1" class="boxConsulta" value="<%=valor1%>"/>&nbsp;<input type="text" name="ord_1" class="boxConsulta" value="<%=orden1%>"/>
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.segundoCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% 
						String valor2 = "";
						if (miform.getCrit_2().equalsIgnoreCase("0")) {
							valor2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
						} else
						if (miform.getCrit_2().equalsIgnoreCase("1")) {
							valor2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
						} else
						if (miform.getCrit_2().equalsIgnoreCase("2")) {
							valor2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
						} else
						if (miform.getCrit_2().equalsIgnoreCase("3")) {
							valor2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
						} else
						if (miform.getCrit_2().equalsIgnoreCase("4")) {
							valor2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
						} 

						String orden2 = "";
						if (miform.getOrd_2().equalsIgnoreCase("A")) {
							orden2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
						} else
						if (miform.getOrd_2().equalsIgnoreCase("D")) {
							orden2=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
						}
					%>
						<input type="text" name="crit_2" class="boxConsulta" value="<%=valor2%>"/>&nbsp;<input type="text" name="ord_2" class="boxConsulta" value="<%=orden2%>"/>
					</td>
				</tr><tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.terceroCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% 
						String valor3 = "";
						if (miform.getCrit_3().equalsIgnoreCase("0")) {
							valor3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
						} else
						if (miform.getCrit_3().equalsIgnoreCase("1")) {
							valor3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
						} else
						if (miform.getCrit_3().equalsIgnoreCase("2")) {
							valor3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
						} else
						if (miform.getCrit_3().equalsIgnoreCase("3")) {
							valor3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
						} else
						if (miform.getCrit_3().equalsIgnoreCase("4")) {
							valor3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
						} 

						String orden3 = "";
						if (miform.getOrd_3().equalsIgnoreCase("A")) {
							orden3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
						} else
						if (miform.getOrd_3().equalsIgnoreCase("D")) {
							orden3=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
						}
					%>
						<input type="text" name="crit_3" class="boxConsulta" value="<%=valor3%>"/>&nbsp;<input type="text" name="ord_3" class="boxConsulta" value="<%=orden3%>"/>
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.cuartoCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% 
						String valor4 = "";
						if (miform.getCrit_4().equalsIgnoreCase("0")) {
							valor4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.sinDefinir");
						} else
						if (miform.getCrit_4().equalsIgnoreCase("1")) {
							valor4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.alfabetico");
						} else
						if (miform.getCrit_4().equalsIgnoreCase("2")) {
							valor4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.antiguedad");
						} else
						if (miform.getCrit_4().equalsIgnoreCase("3")) {
							valor4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.edad");
						} else
						if (miform.getCrit_4().equalsIgnoreCase("4")) {
							valor4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.cola");
						} 

						String orden4 = "";
						if (miform.getOrd_4().equalsIgnoreCase("A")) {
							orden4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.ascendente");
						} else
						if (miform.getOrd_4().equalsIgnoreCase("D")) {
							orden4=UtilidadesString.getMensajeIdioma(usr, "gratuita.maestroTurnos.literal.descendente");
						}
					%>
						<input type="text" name="crit_4" class="boxConsulta" value="<%=valor4%>"/>&nbsp;<input type="text" name="ord_4" class="boxConsulta" value="<%=orden4%>"/>
					</td>
				</tr>
			</table>
			</siga:ConjCampos>
	</html:form>

	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />	


</body>

</html>
	
	