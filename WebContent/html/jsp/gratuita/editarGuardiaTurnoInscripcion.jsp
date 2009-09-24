<!-- editarGuardiaTurno.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gratuita.form.DefinirGuardiasTurnosForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	ScsGuardiasTurnoBean hashAux = (ScsGuardiasTurnoBean)ses.getAttribute("DATABACKUPPESTANA");
	
	DefinirGuardiasTurnosForm miform = (DefinirGuardiasTurnosForm) request.getAttribute("DefinirGuardiasTurnosForm");
	
	Hashtable hash = (Hashtable)hashAux.getOriginalHash();
	String modo = (String)ses.getAttribute("modo");
	String inscripcion = (String)ses.getAttribute("inscripcion");
	String action = (String)request.getAttribute("action");
	ses.removeAttribute("inscripcion");
	//ses.removeAttribute("modo");   ---LA VARIABLE SE USA EN INCOMPATIBIL GUARDIA, P. EJ.
	Hashtable turno = (Hashtable)ses.getAttribute("turnoElegido");
	//ses.removeAttribute("resultado"); -- no se borra para que luego lo tenga el modificar
	Vector campos = (Vector)request.getSession().getAttribute("campos");
	String[] dato1 = {usr.getLocation()};
	ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(usr);
	String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+(String)hash.get("IDORDENACIONCOLAS")+" ";
	Vector vOrdenacion = ordenacion.select(condicion);
	ScsOrdenacionColasBean orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);

	String cOrd ="";
	String[] dato2;
	try{
		ScsGuardiasTurnoAdm guardAdm = new ScsGuardiasTurnoAdm(usr);
		cOrd = guardAdm.getOrdenacionLetradosInscritos (orden.getIdOrdenacionColas().intValue());
		
		dato2 = new String[4];
		dato2[0] = usr.getLocation();
		dato2[1] = (String)turno.get("IDTURNO");
		dato2[2] = (String)hash.get("IDGUARDIA");
		dato2[3] = cOrd;
	}catch(Exception e){
	}
	//String[] dato2 = {usr.getLocation(),(String)turno.get("IDTURNO"),(String)hash.get("IDGUARDIA"),cOrd};
	
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
	
	String entrada = (String)ses.getAttribute("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "345";
	String botonesAccion = "";
	String altoBotones = "380";
	String classCombo="boxCombo";


	if (entrada!=null && entrada.equals("2")) {
		altoBotones= "450";
		alto = "293";
	} else {
		if(!modo.equalsIgnoreCase("ver")){
			botonesAccion="G,R,V";
		} else {
			botonesAccion="V";
		}
	}
	if(modo.equalsIgnoreCase("ver")){
		classCombo="boxConsulta";
	}
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
 	
<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt 
		titulo="censo.fichaCliente.sjcs.guardias.datosGenerales.cabecera" 
		localizacion="censo.fichaCliente.sjcs.guardias.datosGenerales.localizacion"
	/>

	<html:javascript formName="DefinirGuardiasTurnosForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
</head>

<body>

	
	<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { %>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulos">
		<%
			String titulo = (String) request.getAttribute("titulo");
		%>
			<siga:Idioma key="<%=titulo%>"/>
			
		</td>
	</tr>
	</table>	
	<% } %>
<div id="camposRegistro" class="posicionModalGrande" align="center">

<table  class="tablaCentralCampos"  align="center" border=0 >
<tr>
<td>
		
<!-- Información del Turno que tenemos seleccionado-->
<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno">
	<table border="0" align="center" width="90%">
	<tr>
		<td  class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>
		</td>
		<td  class="labelText" style="text-align:left">
			<input name="abreviatura" type="text" class="boxConsulta" size="30" maxlength="30" value="<%=turno.get("ABREVIATURA")%>" readonly="true"></td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>
		 </td>
		<td colspan="3" class="labelText" style="text-align:left">
			<input name="nombre" type="text" class="boxConsulta" size="80" maxlength="100" value="<%=turno.get("NOMBRE")%>" readonly="true" ></td>
	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>
 </td>
		<td class="labelText" style="text-align:left">
			<input name="area" type="text" class="boxConsulta" size="30" value="<%=turno.get("NOMBRE")%>" readonly="true"></td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>
 </td>
		<td class="labelText" style="text-align:left">
			<input name="materia" type="text" class="boxConsulta" size="30" value="<%=turno.get("MATERIA")%>" readonly="true"></td>
	</tr><tr>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/>
 </td>
		<td class="labelText" style="text-align:left">
			<input name="zona" type="text" class="boxConsulta" size="30" value="<%=turno.get("ZONA")%>" readonly="true"></td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/>
 </td>
		<td class="labelText" style="text-align:left">
			<input name="subzona" type="text" class="boxConsulta" size="30" value="<%=turno.get("SUBZONA")%>" readonly="true"></td>
		<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/>
 </td>
		<td  class="labelTextValor" style="text-align:left">
			<%=(turno.get("PARTIDOJUDICIAL")==null)?"&nbsp;":turno.get("PARTIDOJUDICIAL")%>
<!--			<input name="partidoJudicial" type="text" class="boxConsulta" size="30" value="<%=turno.get("PARTIDOJUDICIAL")%>" readonly="true"> -->
		</td>
	</tr>
	</table>
</siga:ConjCampos>
<!-- Comienzo del formulario con los campos -->

		<table align="center" border ="0" width="100%">

			<html:form action="DefinirGuardiasTurnosAction.do" method="POST" target="_top">
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" name="guardia" value="<%=(String)hash.get("IDGUARDIA")%>">
		<tr>		
			<td>		
				<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.guardia">
				<table align="center" border="0">
				<tr> 
					<td class="labelText"  colspan="2">
						<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>
					</td>
					<td  colspan="2">
						<%if(modo.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirGuardiasTurnosForm" property="nombreGuardia" size="20" maxlength="100" styleClass="boxConsulta" value="<%=(String)hash.get(ScsGuardiasTurnoBean.C_NOMBRE)%>" readonly="true"></html:text>
						<%}else{%>
							<html:text name="DefinirGuardiasTurnosForm" property="nombreGuardia" size="20" maxlength="100" styleClass="box" value="<%=(String)hash.get(ScsGuardiasTurnoBean.C_NOMBRE)%>"></html:text>
						<%}%>
					</td>
					<td class="labelText"  colspan="2">
						<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>
					</td>	
					<td  colspan="2">
						<%String descripcion ="";
						  if(modo.equalsIgnoreCase("ver")){
							try{descripcion=(String)hash.get("DESCRIPCION");}catch(Exception e){}%>
							<html:text name="DefinirGuardiasTurnosForm" property="descripcion" size="50" maxlength="1024" styleClass="boxConsulta" value="<%=descripcion%>" readonly="true"></html:text>
						<%}else{
							try{descripcion=(String)hash.get("DESCRIPCION");}catch(Exception e){}%>
							<html:text name="DefinirGuardiasTurnosForm" property="descripcion" size="50" maxlength="1024" styleClass="box" value="<%=descripcion%>"></html:text>
						<%}%>
					</td>
				</tr>
				<tr>
					<td class="labelText" colspan="4">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionFacturacion"/><br>
						<textarea name="descripcionFacturacion" rows="4" cols="80" <%if(modo.equalsIgnoreCase("ver")){%>readOnly="true" class="boxConsulta"<%}else{%>class="box"<%}%>><%try{%><%=hash.get("DESCRIPCIONFACTURACION")%><%}catch(Exception e){}%></textarea>
					</td>
					<td class="labelText" colspan="4">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionPago"/><br>
						<textarea name="descripcionPago" rows="4" cols="80"<%if(modo.equalsIgnoreCase("ver")){%>readOnly="true" class="boxConsulta"<%}else{%>class="box"<%}%>><%try{%><%=hash.get("DESCRIPCIONPAGO")%><%}catch(Exception e){}%></textarea>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasTurno.literal.letradosGuardia"/>
					</td>
					<td class="labelText">
						<%if(modo.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosGuardia" size="5" maxlength="15" styleClass="boxConsulta" value='<%=(hash.get("NUMEROLETRADOSGUARDIA")).toString()%>' readonly="true"></html:text>
						<%}else{%>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosGuardia" size="5" maxlength="15" styleClass="box" value='<%=(hash.get("NUMEROLETRADOSGUARDIA")).toString()%>'></html:text>
						<%}%>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasTurno.literal.numeroSustitutos"/>
					</td>
					<td class="labelText">
						<%if(modo.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosSustitutos" size="15" maxlength="15" styleClass="boxConsulta" value='<%=(hash.get("NUMEROSUSTITUTOSGUARDIA")).toString()%>' readonly="true"></html:text>
						<%}else{%>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosSustitutos" size="15" maxlength="15" styleClass="box" value='<%=(hash.get("NUMEROSUSTITUTOSGUARDIA")).toString()%>'></html:text>
						<%}%>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardias.literal.duracion"/>
					</td>
					<td class="labelText">
						<%if(modo.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirGuardiasTurnosForm" property="duracion" size="5" maxlength="15" styleClass="boxConsulta" value='<%=(hash.get("DIASGUARDIA")).toString()%>' readonly="true"></html:text>
						<%}else{%>
							<html:text name="DefinirGuardiasTurnosForm" property="duracion" size="5" maxlength="15" styleClass="box" value='<%=(hash.get("DIASGUARDIA")).toString()%>'></html:text>
						<%}%>
					</td>
					<td class="labelText" width="140px"><siga:Idioma key="gratuita.listarGuardiasTurno.literal.diasPagados"/>
					</td>
					<td class="labelText">
					<%if(modo.equalsIgnoreCase("ver")){%><html:text name="DefinirGuardiasTurnosForm" property="diasPagados" size="5" maxlength="15" styleClass="boxConsulta" value='<%=(hash.get("DIASPAGADOS")).toString()%>' readonly="true"></html:text><%}else{%><html:text name="DefinirGuardiasTurnosForm" property="diasPagados" size="15" maxlength="15" styleClass="box" value='<%=(hash.get("DIASPAGADOS")).toString()%>'></html:text><%}%>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardias.literal.tipodia"/>
					</td>
					<td class="labelText">
						<html:text name="DefinirGuardiasTurnosForm" property="tipoDias" size="15" maxlength="15" styleClass="boxConsulta" value='<%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.get("SELECCIONLABORABLES"), (String)hash.get("SELECCIONFESTIVOS"), usr)%>' readonly="true"></html:text>
					</td>
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.maestroGuardias.literal.diasSeparacion"/>
					</td>
					<td class="labelText" colspan="4">
						<%if(modo.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirGuardiasTurnosForm" property="diasSeparacion" size="15" maxlength="15" styleClass="boxConsulta" value='<%=(hash.get("DIASSEPARACIONGUARDIAS")).toString()%>' readonly="true"></html:text>
						<%}else{%>
							<html:text name="DefinirGuardiasTurnosForm" property="diasSeparacion" size="15" maxlength="15" styleClass="box" value='<%=(hash.get("DIASSEPARACIONGUARDIAS")).toString()%>'></html:text>
						<%}%>
					</td>
				</tr>
				</table>
				</siga:ConjCampos>
			</td>
		</tr>
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td>
			<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
			<table border="0" width="100%" align="center">
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% if (!modo.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_1">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_1">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					<% } else {
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
					<% } %>
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.segundoCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% if (!modo.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_2">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_2">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					<% } else {
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
					<% } %>
					</td>
				</tr><tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.terceroCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% if (!modo.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_3">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_3">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					<% } else {
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
					<% } %>
					</td>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.cuartoCriterio"/></td>
					<td class="labelText" style="text-align:right">
					<% if (!modo.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="crit_4">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirGuardiasTurnosForm" property="ord_4">
							<html:option value="A"><siga:Idioma key="gratuita.maestroTurnos.literal.ascendente"/></html:option>
							<html:option value="D"><siga:Idioma key="gratuita.maestroTurnos.literal.descendente"/></html:option>
						</html:select>
					<% } else {
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
					<% } %>
					</td>
				</tr>
			</table>
			</siga:ConjCampos>

					</td>
				</tr>
				</table>
			</td>
		</tr>
		
	</table>
	</html:form>

</td>
</tr>
</table>

	<%if(!modo.equalsIgnoreCase("ver")){%>
		
			<siga:ConjBotonesAccion botones="G,R,V" clase="botonesDetalle"  />
		
	<%} %>

	<%if(inscripcion!=null){%>
		
			<siga:ConjBotonesAccion botones="x,s" clase="botonesDetalle"  />
		
	<%} else { %>
		
			<siga:ConjBotonesAccion botones="<%=botonesAccion %>" clase="botonesDetalle"  />
		
	<%}%>

</div>
	
<script language="JavaScript">
		
		
		function accionCerrar() 
		{		
			window.returnValue="CANCELADO";			
			window.close();
		}

		function accionCancelar() 
		{		
			window.returnValue="CANCELADO";			
			window.close();
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
		
		}
		
		function accionSiguiente() 
		{
			document.forms[0].action = "<%=app%><%=action%>";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			if (document.forms[0].tipoDias.value!=""){
				if (validateDefinirGuardiasTurnosForm(document.DefinirGuardiasTurnosForm)){	
					document.forms[0].target="submitArea";
					document.forms[0].modo.value="modificar";
					document.forms[0].submit();
				}
			}else{
				alert("<siga:Idioma key='gratuita.editarGuardiaTurno.literal.alert1'/>");
			}
		}
		
		function accionVolver() 
		{		
			document.forms[0].action="JGR_DefinirTurnos.do";
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}
		
		
		<!-- Refresco -->
		function refrescarLocal()
		{
			document.forms[0].target = "_parent";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}
	
</script>
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
	
</body>		
</html>