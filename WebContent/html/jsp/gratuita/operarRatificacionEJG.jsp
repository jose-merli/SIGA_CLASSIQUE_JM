<!-- operarRatificacionEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUP");
	String accion = (String)ses.getAttribute("accion");
	String dato[] = {(String)usr.getLocation()};
		
	String anio= "", numero="", idTipoEJG = "", observaciones = "",refA="";
	String fechaRatificacion = "", fechaResolucionCAJG= "", fechaNotificacion= "";
	boolean requiereTurnado= false;
	ArrayList vFundamentoJuridico= new ArrayList(), vTipoRatificacion= new ArrayList();
	
	try {
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		if (miHash.containsKey("RATIFICACIONDICTAMEN")) observaciones = miHash.get("RATIFICACIONDICTAMEN").toString();
		if (miHash.containsKey("REFAUTO")) refA = miHash.get("REFAUTO").toString();

		if (miHash.containsKey("FECHARATIFICACION")) fechaRatificacion = GstDate.getFormatedDateShort("",miHash.get("FECHARATIFICACION").toString()).toString();
		if (miHash.containsKey("FECHARESOLUCIONCAJG")) fechaResolucionCAJG = GstDate.getFormatedDateShort("",miHash.get("FECHARESOLUCIONCAJG").toString()).toString();
		if (miHash.containsKey("FECHANOTIFICACION")) fechaNotificacion = GstDate.getFormatedDateShort("",miHash.get("FECHANOTIFICACION").toString()).toString();
		if (miHash.containsKey("TURNADORATIFICACION")){
		 if (!miHash.get("TURNADORATIFICACION").toString().equals("") && !miHash.get("TURNADORATIFICACION").toString().equals("0")){
		  requiereTurnado=true;
		 }
		} 
		

		if (miHash.containsKey("IDFUNDAMENTOJURIDICO") && miHash.get("IDFUNDAMENTOJURIDICO") != "") {
			vFundamentoJuridico.add(miHash.get("IDFUNDAMENTOJURIDICO").toString());
		}
		if (miHash.containsKey("IDTIPORATIFICACIONEJG") && miHash.get("IDTIPORATIFICACIONEJG") != "") {
			vTipoRatificacion.add(miHash.get("IDTIPORATIFICACIONEJG").toString());
		}
	}catch(Exception e){e.printStackTrace();};
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<script type="text/javascript">
		function refrescarLocal()
		{
			document.location.reload();
		}
	</script>
	<siga:Titulo 
		titulo="pestana.justiciagratuitaejg.ratificacion" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
</head>

<body>
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
						ScsEJGAdm adm = new ScsEJGAdm (usr);
							
						Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero,idTipoEJG);

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsEJGBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsEJGBean.C_NUMEJG);
							t_tipoEJG   = (String)hTitulo.get("TIPOEJG");
						}
					
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
			<td>
				<%
					if (!accion.equalsIgnoreCase("ver")) {
				%>
				<table>
					<tr>
						<td><siga:InformeSimple
							idInstitucion="<%=usr.getLocation()%>"
							recurso="gratuita.EJG.botonComunicaciones" idTipoInforme="EJG"
							formularioDatos="DefinirEJGForm" /></td>
					</tr>
				</table>
				<%
					}
				%>
			</td>
		</tr>
	</table>


	<siga:ConjCampos leyenda="gratuita.dictamenEJG.literal.datosRatificacion">
	
	<table align="center"  width="100%" border="0">
	
	<html:form action="/JGR_RatificacionEJG" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>
	
	<!-- FILA -->
	<tr>
	<td class="labelText" width="150">
		<siga:Idioma key="gratuita.operarRatificacion.literal.fechaResolucionCAJG"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirEJGForm" property="fechaResolucionCAJG" size="10" styleClass="boxConsulta" value="<%=fechaResolucionCAJG%>" disabled="false" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirEJGForm" property="fechaResolucionCAJG" size="10" styleClass="box" value="<%=fechaResolucionCAJG%>" disabled="false" readonly="true"></html:text>
			&nbsp;
			<a onClick="return showCalendarGeneral(fechaResolucionCAJG);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
			<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
			</a>
		<%}%>
	</td>
	</tr>
	<tr>
	<td class="labelText">
	  <siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>
	</td>
	<td  colspan="2">
		
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucion" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vTipoRatificacion%>" readOnly="true"/>
		<%} else {%>
			<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucion" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vTipoRatificacion%>"/>
		<%}%>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarRatificacion.literal.fundamentoJuridico"/>
	</td>
	<td colspan="3">
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ComboBD nombre="idFundamentoJuridico"  ancho="790" tipo="tipoFundamentos" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vFundamentoJuridico%>" readOnly="true"/>
		<%} else {%>
			<siga:ComboBD nombre="idFundamentoJuridico" ancho="790" tipo="tipoFundamentos" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vFundamentoJuridico%>"/>
		<%}%>
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.operarRatificacion.literal.fechaNotificacion"/>
	</td>
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirEJGForm" property="fechaNotificacion" size="10" styleClass="boxConsulta" value="<%=fechaNotificacion%>" disabled="false" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirEJGForm" property="fechaNotificacion" size="10" styleClass="box" value="<%=fechaNotificacion%>" disabled="false" readonly="true"></html:text>
			&nbsp;
			<a onClick="return showCalendarGeneral(fechaNotificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
			<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
			</a>
		<%}%>
	</td>
	<td class="labelText">
			<siga:Idioma key="gratuita.EJG.resolucion.refAuto"/>
			<%if (accion.equalsIgnoreCase("ver")){%>
				<html:text name="DefinirEJGForm" property="refAuto" size="10" styleClass="boxConsulta" value="<%=refA%>" readonly="false" disabled="false"></html:text>
			<%} else {%>
				<html:text name="DefinirEJGForm" property="refAuto" size="10" styleClass="box" value="<%=refA%>" readonly="false" disabled="false"></html:text>
			<%}%>
		</td>
	</tr>
	<tr>
		<td class="labelText">
		<siga:Idioma key="gratuita.operarRatificacion.literal.fechaRatificacion"/>
	</td>	
	<td>
		<%if (accion.equalsIgnoreCase("ver")){%>
			<html:text name="DefinirEJGForm" property="fechaRatificacion" size="10" styleClass="boxConsulta" value="<%=fechaRatificacion%>" disabled="false" readonly="true"></html:text>
		<%} else {%>
			<html:text name="DefinirEJGForm" property="fechaRatificacion" size="10" styleClass="box" value="<%=fechaRatificacion%>" disabled="false" readonly="true"></html:text>
			&nbsp;
			<a onClick="return showCalendarGeneral(fechaRatificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
			<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
			</a>
		<%}%>
	</td>	
	</tr>
	<tr>
	<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.operarRatificacion.literal.requiereTurnado"/>&nbsp;&nbsp;
		<%if (accion.equalsIgnoreCase("ver")){%>
			<input type="Checkbox" name="turnadoRatificacion" <%=(requiereTurnado?"checked":"")%> disabled>
		<%} else {%>
			<input type="Checkbox" name="turnadoRatificacion" <%=(requiereTurnado?"checked":"")%>>
		<%}%>
		
	</td>
	</tr>	
	<tr>
	<td class="labelText" colspan="1">
		<siga:Idioma key="gratuita.operarRatificacion.literal.observacionRatificacion"/>
	</td>
	<td class="labelText" colspan="3">	
		<%if (accion.equalsIgnoreCase("ver")) {%>	
			<textarea name="ratificacionDictamen" class="boxConsulta" style="width:770px" rows="18" readOnly="true"><%=observaciones%></textarea>
		<%} else {%>
			<textarea name="ratificacionDictamen" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" class="box" style="width:770px" rows="18"><%=observaciones%></textarea>
		<%}%>
	</td>		
	</tr>

	</html:form>
	</table>
	
	</siga:ConjCampos>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<siga:ConjBotonesAccion botones="V,R,G" modo="<%=accion%>"/>

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
				
		//Asociada al boton Volver
		function accionVolver()
		{
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 

		}
		
		//Asociada al boton Cerrar
		function accionGuardar()
		{ 
			sub();
			document.forms[0].submit();

		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>