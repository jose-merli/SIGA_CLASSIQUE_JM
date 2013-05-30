<!-- definirGuardia.jsp -->
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
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaAdm"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	ScsGuardiasTurnoBean hash = (ScsGuardiasTurnoBean)((Vector)ses.getAttribute("resultado")).get(0);
	String modo = (String)ses.getAttribute("modo");
	ses.removeAttribute("modo");
	//ses.removeAttribute("resultado"); -- no se borra para que luego lo tenga el modificar
	Vector camposTurno = (Vector)ses.getAttribute("camposTurno");
	String[] dato1 = {usr.getLocation()};
	String[] dato2 = {usr.getLocation(),(String)camposTurno.get(9)};
	ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(usr);
	String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+(String)camposTurno.get(10)+" ";
	Vector vOrdenacion = ordenacion.select(condicion);
	ScsOrdenacionColasBean orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
 	
<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="%%RECURSO%%" localizacion="%%RECURSO%%"/>

</head>

<body class="tablaCentralCampos">

<!-- Información del Turno que tenemos seleccionado-->

		<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.turno">
		<table>
			<tr>
				<td width="10%" class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>:</td>
				<td width="20%" class="labelText" style="text-align:left">
					<input name="abreviaturaT" type="text" class="box" size="30" maxlength="30" value="<%=camposTurno.get(0)%>">
				</td>
				<td class="labelText" width="10%" style="text-align:left"><siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>: </td>
				<td width="50%" class="labelText" style="text-align:left">
					<input name="nombreT" type="text" class="box" size="100" maxlength="100" value="<%=camposTurno.get(1)%>"  >
				</td>
			</tr>
			<tr>
				<td class="labelText" width="15%" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>: </td>
				<td class="labelText" width="30%" style="text-align:left">
					<input name="areaT" type="text" class="box" size="30" value="<%=camposTurno.get(2)%>" >
				</td>
				<td class="labelText" width="15%" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/>: </td>
				<td class="labelText" width="30%" style="text-align:left">
					<input name="materiaT" type="text" class="box" size="30" value="<%=camposTurno.get(3)%>" >
				</td>
			</tr><tr>
				<td class="labelText" width="15%" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/>: </td>
				<td class="labelText" width="30%" style="text-align:left">
					<input name="zonaT" type="text" class="box" size="30" value="<%=camposTurno.get(4)%>" >
				</td>
				<td class="labelText" width="15%" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/>: </td>
				<td class="labelText" width="30%" style="text-align:left">
					<input name="subzonaT" type="text" class="box" size="30" value="<%=camposTurno.get(5)%>" >
				</td>
			</tr><tr>
				<td class="labelText" width="15%" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/>: </td>
				<td colspan="3" class="labelText" style="text-align:left">
					<input name="partidoJudicialT" type="text" class="box" size="30" value="<%=camposTurno.get(6)%>" >
				</td>
			</tr>
		</table>
		</siga:ConjCampos>

<!-- Comienzo del formulario con los campos -->

		<table align="center" width="95%">

		<html:form action="DefinirGuardiasTurnosAction.do" method="POST" target="resultado">

		<html:hidden property = "modo" value = ""/>

		<tr>		
			<td>		
				<siga:ConjCampos leyenda="gratuita.listarGuardias.literal.guardia">
				<table align="center" width="90%" border="0">
				<tr>	
					<td class="labelText">
						<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/>
					</td>				
					<td class="labelText">
						<html:text name="DefinirGuardiasTurnosForm" property="nombre" size="15" maxlength="15" styleClass="box" value="<%=hash.getNombre()%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>
					</td>	
					<td class="labelText">
							<html:text name="DefinirGuardiasTurnosForm" property="descripcion" size="100" maxlength="100" styleClass="box" value="<%=hash.getDescripcion()%>"></html:text>
					</td>
				</tr>
				<tr>
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionFacturacion"/><br>
						<textarea name="descripcionFacturacion" rows="4" cols="50" <%if(modo.equalsIgnoreCase("ver")){%> class="box"<%}%>><%=hash.getDescripcionFacturacion()%></textarea>
					</td>
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.maestroGuardias.literal.descripcionPago"/><br>
						<textarea name="descripcionPago" rows="4" cols="50"<%if(modo.equalsIgnoreCase("ver")){%> class="box"<%}%>><%=hash.getDescripcionPago()%></textarea>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria"/>
							<siga:ComboBD nombre="partidaPresupuestaria" tipo="partidaPresupuestaria" clase="boxCombo"   estilo="true" obligatorio="false" parametro="<%=dato1%>"/>
					</td>
					<td class="labelText" colspan="3">
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasTurno.literal.letradosGuardia"/>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosGuardia" size="15" maxlength="15" styleClass="box" value="<%=(hash.getNumeroLetradosGuardia()).toString()%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasTurno.literal.numeroSustitutos"/>
							<html:text name="DefinirGuardiasTurnosForm" property="letradosSustitutos" size="15" maxlength="15" styleClass="box" value="<%=(hash.getNumeroSustitutosGuardia()).toString()%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardias.literal.duracion"/>
							<html:text name="DefinirGuardiasTurnosForm" property="duracion" size="15" maxlength="15" styleClass="box" value="<%=(hash.getDiasGuardia()).toString()%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardiasTurno.literal.diasPagados"/>
							<html:text name="DefinirGuardiasTurnosForm" property="diasPagados" size="15" maxlength="15" styleClass="box" value="<%=(hash.getDiasPagados()).toString()%>"></html:text>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.listarGuardias.literal.tipodia"/>
							<html:text name="DefinirGuardiasTurnosForm" property="tipoDia" size="15" maxlength="15" styleClass="box" value="<%=ScsGuardiasTurnoAdm.obtenerTipoDia((String)hash.getSeleccionLaborables(), (String)hash.getSeleccionFestivos(), usr)%>"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.maestroGuardias.literal.diasSeparacion"/>
							<html:text name="DefinirGuardiasTurnosForm" property="diasSeparacion" size="15" maxlength="15" styleClass="box" value="<%=(hash.getDiasSeparacionGuardia()).toString()%>"></html:text>
					</td>
					<td class="labelText" colspan="2">
						<siga:Idioma key="gratuita.maestroGuardias.literal.requiereValidacion"/>
							<INPUT NAME="requiereValidacion" TYPE=RADIO VALUE="0">
					</td>
				</tr>
				</table>
				</siga:ConjCampos>
		</td>
		</tr>
		<tr>
			<td>		
				<table>
				<tr>
					<td>
						<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
						<table align="center" width="100%" align="center">
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="alfabetico" size="15" maxlength="15" styleClass="box" value="<%=(orden.getAlfabeticoApellidos()).toString()%>"></html:text>
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroTurnos.literal.edad"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="edad" size="15" maxlength="15" styleClass="box" value="<%=(orden.getFechaNacimiento()).toString()%>"></html:text>
							</td>
						</tr>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="nColeciado" size="15" maxlength="15" styleClass="box" value="<%=(orden.getNumeroColegiado()).toString()%>"></html:text>
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroTurnos.literal.cola"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="antiguedadCola" size="15" maxlength="15" styleClass="box" value="<%=(orden.getAntiguedadCola()).toString()%>"></html:text>
							</td>
						</tr>
						</table>
						</siga:ConjCampos>
					</td>
					<td>
						<siga:ConjCampos leyenda="gratuita.maestroGuardias.literal.doblarImporte">
						<table>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroGuardias.literal.asistencias"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="asistencias" size="15" maxlength="15" styleClass="box" value=""></html:text>
							</td>
						</tr>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.maestroGuardias.literal.actuaciones"/>
							</td>
							<td class="labelText">
									<html:text name="DefinirGuardiasTurnosForm" property="actuaciones" size="15" maxlength="15" styleClass="box" value=""></html:text>
							</td>
						</tr>
						</table>
						</siga:ConjCampos>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.maestroTurnos.literal.ultimoLetrado"/>
					<siga:ComboBD nombre="ultimoLetrado" tipo="letradoInscrito" clase="boxCombo"  parametro="<%=dato2%>"/>
			</td>
		</tr>
	</table>
	</html:form>
	<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle"  />
	<script language="JavaScript">
		
		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			history.back();
		}
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
		
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		
			sub();
			document.forms[0].modo.value="modificar";
			document.forms[0].submit();
		}
		
	
	</scrip>
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>		
</html>