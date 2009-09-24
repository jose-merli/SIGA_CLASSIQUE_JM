<!-- modalConfirmar_PestanaCalendarioGuardias.jsp-->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos comunes:
	String idInstitucion = (String)request.getAttribute("IDINSTITUCION");
	String numeroPermuta = (String)request.getAttribute("NUMEROPERMUTA");
	String fechaSolicitud = (String)request.getAttribute("FECHASOLICITUD");
	String anulada = (String)request.getAttribute("ANULADA");

	//Datos del Solicitante:
	Hashtable solicitante = new Hashtable();
	solicitante = (Hashtable)request.getAttribute("SOLICITANTE");
	String fechaInicioSolicitante = UtilidadesHash.getString(solicitante,"FECHAINICIO");
	String fechaFinSolicitante = UtilidadesHash.getString(solicitante,"FECHAFIN");
	String idTurnoSolicitante = UtilidadesHash.getString(solicitante,"IDTURNO");
	String idGuardiaSolicitante = UtilidadesHash.getString(solicitante,"IDGUARDIA");
	String idCalendarioguardiasSolicitante = UtilidadesHash.getString(solicitante,"IDCALENDARIOGUARDIAS");
	String idPersonaSolicitante = UtilidadesHash.getString(solicitante,"IDPERSONA");
	String nombreYApellidosSolicitante = UtilidadesHash.getString(solicitante,"NOMBREYAPELLIDOS");
	String numeroColegiadoSolicitante = UtilidadesHash.getString(solicitante,"NUMEROCOLEGIADO");
	String motivosSolicitante = UtilidadesHash.getString(solicitante,"MOTIVOS");

	//Datos del Confirmador:
	Hashtable confirmador = new Hashtable();
	confirmador = (Hashtable)request.getAttribute("CONFIRMADOR");
	String fechaInicioConfirmador = UtilidadesHash.getString(confirmador,"FECHAINICIO");
	String fechaFinConfirmador = UtilidadesHash.getString(confirmador,"FECHAFIN");
	String idTurnoConfirmador = UtilidadesHash.getString(confirmador,"IDTURNO");
	String idGuardiaConfirmador = UtilidadesHash.getString(confirmador,"IDGUARDIA");
	String idCalendarioguardiasConfirmador = UtilidadesHash.getString(confirmador,"IDCALENDARIOGUARDIAS");
	String idPersonaConfirmador = UtilidadesHash.getString(confirmador,"IDPERSONA");
	String nombreYApellidosConfirmador = UtilidadesHash.getString(confirmador,"NOMBREYAPELLIDOS");
	String numeroColegiadoConfirmador = UtilidadesHash.getString(confirmador,"NUMEROCOLEGIADO");
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="PermutasConfirmadorForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	

	<script>
		//Variable Global que me dice si he pulsado en el radiobutton SI:
		confirmar='SI';
		function setConfirmar(valor) {
			confirmar = valor;
		}
		
		function getConfirmar() {
			return confirmar;
		}
	</script>

</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.titulo"/>
	</td>
</tr>
</table>
	
<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCampos" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_PestanaCalendarioGuardias.do" method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "modificar"/>
		
		<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property = "numero" value = "<%=numeroPermuta%>"/>
		<html:hidden property = "fechaSolicitud" value = "<%=fechaSolicitud%>"/>	
		<html:hidden property = "anulada" value = "<%=anulada%>"/>					
		
		<html:hidden property = "idTurnoSolicitante" value = "<%=idTurnoSolicitante%>"/>
		<html:hidden property = "idGuardiaSolicitante" value = "<%=idGuardiaSolicitante%>"/>
		<html:hidden property = "idCalendarioGuardiasSolicitante" value = "<%=idCalendarioguardiasSolicitante%>"/>
		<html:hidden property = "idPersonaSolicitante" value = "<%=idPersonaSolicitante%>"/>
		<html:hidden property = "fechaInicioSolicitante" value = "<%=fechaInicioSolicitante%>"/>
		<html:hidden property = "fechaFinSolicitante" value = "<%=fechaFinSolicitante%>"/>

		<html:hidden property = "idTurnoConfirmador" value = "<%=idTurnoConfirmador%>"/>
		<html:hidden property = "idGuardiaConfirmador" value = "<%=idGuardiaConfirmador%>"/>
		<html:hidden property = "idCalendarioGuardiasConfirmador" value = "<%=idCalendarioguardiasConfirmador%>"/>
		<html:hidden property = "idPersonaConfirmador" value = "<%=idPersonaConfirmador%>"/>
		<html:hidden property = "fechaInicioConfirmador" value = "<%=fechaInicioConfirmador%>"/>
		<html:hidden property = "fechaFinConfirmador" value = "<%=fechaFinConfirmador%>"/>
		
	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
		<td class="labelText">
			<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.texto1"/>:
		</td>
	</tr>
	<tr>
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.solicitante">
		<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.numero"/>:			
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="numeroColegiadoSolicitante" size="10" maxlength="20" styleClass="box" value="<%=numeroColegiadoSolicitante%>" readOnly="true"></html:text>
			</td>		
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.nombre"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="nombreSolicitante" size="70" maxlength="300" styleClass="box" value="<%=nombreYApellidosSolicitante%>" readOnly="true"></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.fechainicio"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="fechaInicioSolicitanteParseada" size="10" maxlength="10" styleClass="box" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioSolicitante)%>" readOnly="true"></html:text>
			</td>		
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.fechafin"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="fechaFinSolicitanteParseada" size="10" maxlength="10" styleClass="box" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinSolicitante)%>" readOnly="true"></html:text>
			</td>
		</tr>		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.motivos"/>:
			</td>
			<td class="labelText" colspan="3">
				<html:textarea name="PermutasForm" property="motivosSolicitante" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" cols="194" rows="3" style="overflow:auto" styleClass="boxCombo" value="<%=motivosSolicitante%>" readOnly="true" ></html:textarea>
			</td>		
		</tr>		
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.texto2"/>:
		</td>
	</tr>
	<tr>
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.confirmador">
		<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.numero"/>:			
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="numeroColegiadoConfirmador" size="10" maxlength="20" styleClass="box" value="<%=numeroColegiadoConfirmador%>" readOnly="true"></html:text>
			</td>		
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.nombre"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="nombreConfirmador" size="70" maxlength="300" styleClass="box" value="<%=nombreYApellidosConfirmador%>" readOnly="true"></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.fechainicio"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="fechaInicioConfirmadorParseada" size="10" maxlength="10" styleClass="box" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioConfirmador)%>" readOnly="true"></html:text>
			</td>		
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.fechafin"/>:
			</td>
			<td class="labelText">
				<html:text name="PermutasForm" property="fechaFinConfirmadorParseada" size="10" maxlength="10" styleClass="box" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinConfirmador)%>" readOnly="true"></html:text>
			</td>
		</tr>		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.motivos"/>:
			</td>
			<td class="labelText" colspan="3">
				<html:textarea name="PermutasForm" property="motivosConfirmador" cols="194" rows="3" style="overflow:auto" styleClass="boxCombo" value="" readOnly="false" ></html:textarea>
			</td>		
		</tr>		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.confirmar"/>:
			</td>
			<td class="labelText" colspan="3">
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.si"/>:
				&nbsp;<input type="radio" name="confirmar" value="0" checked onclick="setConfirmar('SI')" />
				<siga:Idioma key="gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.no"/>:
				&nbsp;<input type="radio" name="confirmar" value="1" onclick="setConfirmar('NO')" />				
			</td>		
		</tr>		
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>	
	</table>
	

	
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,C" modal="G"/>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		sub();
				//Valido el tamanho del textarea motivosSolicitud
				if (validatePermutasConfirmadorForm(document.PermutasForm)){
					//Si esta marcado el campo Confirmar como SI modificamos.					
					if (getConfirmar() == 'SI')
						document.forms[0].modo.value = "modificar";
					//Si esta marcado como NO borramos el registro
					else
						document.forms[0].modo.value = "borrar";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
				}else{
					fin();
					return false;
				}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>