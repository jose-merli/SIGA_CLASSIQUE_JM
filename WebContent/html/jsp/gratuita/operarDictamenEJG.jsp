<!-- operarDictamenEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>

<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUP");
	String accion = (String)ses.getAttribute("accion");
	String modo = (String)request.getAttribute("MODO");
	String dato[] = {(String)usr.getLocation()};
		
	String anio= "", numero="", idTipoEJG = "", dictamen = "", fechaDictamen = "";
	ArrayList vIntFDict = new ArrayList();
	ArrayList vIntFCalf = new ArrayList();
	Object obj=null;
	try {
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		if (miHash.containsKey("DICTAMEN")) dictamen = miHash.get("DICTAMEN").toString();
		if (miHash.containsKey("FECHADICTAMEN")) fechaDictamen = GstDate.getFormatedDateShort("",miHash.get("FECHADICTAMEN").toString()).toString();
	}catch(Exception e){};
	
	if (miHash.containsKey("IDTIPODICTAMENEJG")){
		try {
			obj=miHash.get("IDTIPODICTAMENEJG");
			vIntFDict.add(obj.equals("")? "0":obj.toString());
		} catch (Exception e) {}
	}
	if (miHash.containsKey("IDFUNDAMENTOCALIF")){
		try {
			obj=miHash.get("IDFUNDAMENTOCALIF");
			vIntFCalf.add(obj.equals("")? "0":obj.toString());
		} catch (Exception e) {		
		}
	}
%>

<html>
<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
<script type="text/javascript">
		function refrescarLocal()
		{
			document.location.reload();
		}
	</script>
<siga:Titulo titulo="pestana.justiciagratuitaejg.dictamen"
	localizacion="gratuita.busquedaEJG.localizacion" />
</head>
<body>
<tr>
	<td width="100%" align="center">

	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
			<%
				String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";
				;
				ScsEJGAdm adm = new ScsEJGAdm(usr);

				Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(),
						anio, numero, idTipoEJG);

				if (hTitulo != null) {
					t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
					t_apellido1 = (String) hTitulo
							.get(ScsPersonaJGBean.C_APELLIDO1);
					t_apellido2 = (String) hTitulo
							.get(ScsPersonaJGBean.C_APELLIDO2);
					t_anio = (String) hTitulo.get(ScsEJGBean.C_ANIO);
					t_numero = (String) hTitulo.get(ScsEJGBean.C_NUMEJG);
					t_tipoEJG = (String) hTitulo.get("TIPOEJG");
				}
			%>
			 <%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
			- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%>
			<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%></td>
			<td> <siga:InformeSimple
			idInstitucion="<%=usr.getLocation()%>"
			recurso="gratuita.EJG.InformeCalificacion" idInforme="EJGCA"
			idTipoInforme="EJGCA" formularioDatos="DefinirDictamenEJGForm" /></td>	
		</tr>
	</table>

	<div id="campos" align="center">

	<table align="center" width="100%" height="430"
		class="tablaCentralCampos">
		<tr>
			<td valign="top"><siga:ConjCampos
				leyenda="gratuita.dictamenEJG.literal.datosDictamen">

				<table align="center" width="100%" border="0">

					<html:form action="/JGR_DictamenEJG" method="POST"
						target="submitArea">
						<html:hidden property="modo" value="Modificar" />
						<html:hidden property="idInstitucion"
							value="<%=usr.getLocation()%>" />
						<html:hidden property="idTipoEJG" value="<%=idTipoEJG%>" />
						<html:hidden property="anio" value="<%=anio%>" />
						<html:hidden property="numero" value="<%=numero%>" />

						<tr>
							<td><!-- FILA -->
						<tr>
							<td class="labelText"><siga:Idioma
								key="gratuita.busquedaContabilidad.literal.fecha" /></td>
							<td class="labelText">
							<%
								if (accion.equalsIgnoreCase("ver")) {
							%> <html:text
								name="DefinirDictamenEJGForm" property="fechaDictamen" size="10"
								styleClass="boxConsulta" value="<%=fechaDictamen%>"
								readonly="true" disabled="false"></html:text> <%
 	} else {
 %> <html:text
								name="DefinirDictamenEJGForm" property="fechaDictamen" size="10"
								styleClass="box" value="<%=fechaDictamen%>" readonly="true"
								disabled="false"></html:text>&nbsp;&nbsp;<a
								onClick="return showCalendarGeneral(fechaDictamen);"
								onMouseOut="MM_swapImgRestore();"
								onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img
								src="<%=app%>/html/imagenes/calendar.gif"
								alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"
								border="0"></a> <%
 	}
 %>
							</td>
							<td class="labelText"><siga:Idioma
								key="gratuita.operarDictamen.literal.tipoDictamen" /></td>
							<td class="labelText">
							<%
								if (accion.equalsIgnoreCase("ver")) {
							%> <siga:ComboBD
								nombre="idTipoDictamenEJG" tipo="dictamenEJG"
								clase="boxConsulta" filasMostrar="1" seleccionMultiple="false"
								obligatorio="false" parametro="<%=dato%>"
								elementoSel="<%=vIntFDict%>" readOnly="true" /> <%
 	} else {
 %> <siga:ComboBD
								nombre="idTipoDictamenEJG" tipo="dictamenEJG" clase="boxCombo"
								filasMostrar="1" seleccionMultiple="false" obligatorio="false"
								parametro="<%=dato%>" elementoSel="<%=vIntFDict%>" /> <%
 	}
 %>
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma
								key="gratuita.operarDictamen.literal.fundamentoclf" /></td>


							<td class="labelText" colspan="3">
							<%
								if (accion.equalsIgnoreCase("ver")) {
							%> <siga:ComboBD
								nombre="idFundamentoCalificacion" ancho="815"
								tipo="tipoFundamentos1" parametro="<%=dato%>"
								clase="boxConsulta" filasMostrar="1" seleccionMultiple="false"
								obligatorio="false" elementoSel="<%=vIntFCalf%>" readOnly="true" />
							<%
								} else {
							%> <siga:ComboBD nombre="idFundamentoCalificacion"
								ancho="815" tipo="tipoFundamentos1" parametro="<%=dato%>"
								clase="boxCombo" filasMostrar="1" seleccionMultiple="false"
								obligatorio="false" elementoSel="<%=vIntFCalf%>" /> <%
 	}
 %>
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma
								key="gratuita.operarDictamen.literal.dictamen" /></td>
							<td class="labelText" colspan="4">
							<%
								if (accion.equalsIgnoreCase("ver")) {
							%> <textarea name="dictamen"
								class="boxConsulta" style="width: 815px" rows="20"
								readOnly="true"><%=dictamen%></textarea> <%
 	} else {
 %> <textarea
								name="dictamen" class="box" style="width: 815px" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="20"><%=dictamen%></textarea>
							<%
								}
							%>
							</td>
						</tr>

					</html:form>
				</table>

			</siga:ConjCampos></td>
		</tr>
	</table>
	<% 
	if(accion!=null && !accion.equalsIgnoreCase("ver")){ %>
	<table align="left">
		<tr>
			<td align="left">
			</td>
		</tr>
	</table>
	<%}%>
	<!-- FIN: CAMPOS DEL REGISTRO --> <!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="V,R,G" modo="<%=accion%>"
		clase="botonesDetalle" /> <!-- INICIO: SCRIPTS BOTONES --> <script
		language="JavaScript">	
	
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
	</script> <!-- FIN: SCRIPTS BOTONES --> <!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA --> <!-- Obligatoria en todas las páginas--> <iframe
		name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe> <!-- FIN: SUBMIT AREA -->
</body>

</html>