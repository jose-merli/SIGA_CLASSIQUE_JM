<!-- modalNuevo_DefinirCalendarioGuardia.jsp-->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	Hashtable datosHash = new Hashtable();
	String idinstitucionpestanha="", idturnopestanha="", idguardiapestanha="";
	String tipodias="", diasguardia="", num_letrados="", num_sustitutos="";
	//Obtengo del request la hash con los datos iniciales
	if (request.getAttribute("DATOSINICIALES") != null) {
		datosHash = (Hashtable)request.getAttribute("DATOSINICIALES");
		idinstitucionpestanha = (String)datosHash.get("IDINSTITUCIONPESTAÑA");
		idturnopestanha = (String)datosHash.get("IDTURNOPESTAÑA");
		idguardiapestanha = (String)datosHash.get("IDGUARDIAPESTAÑA");
		tipodias = (String)datosHash.get("TIPODIAS");
		diasguardia = (String)datosHash.get("DIASGUARDIA");		
		num_letrados = (String)datosHash.get("NUMEROLETRADOS");
		num_sustitutos = (String)datosHash.get("NUMEROSUSTITUTOS");		
	}
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"type="text/javascript"></script>	
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DefinirCalendarioGuardiaForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulosPeq">
		<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.titulo"/>
	</td>
</tr>
</table>
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposMedia" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "accion" value = ""/>		
		<html:hidden property = "idInstitucionPestanha" value = "<%=idinstitucionpestanha%>"/>
		<html:hidden property = "idTurnoPestanha" value = "<%=idturnopestanha%>"/>
		<html:hidden property = "idGuardiaPestanha" value = "<%=idguardiapestanha%>"/>

	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.tabla">
		<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.letradosGuardia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;
				<%=num_letrados%>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.letradosReserva"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;				
				<%=num_sustitutos%>				
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.diasGuardia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;				
				<%=diasguardia%>				
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.tipoDia"/>
			</td>
			<td class="labelTextValor">
				&nbsp;&nbsp;								
				<%=((String)datosHash.get("TIPODIAS"))%>
			</td>
		</tr>
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
	<td>
		<siga:ConjCampos>		
		<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.fechaDesde"/>:
				&nbsp;(*)&nbsp;&nbsp;&nbsp;
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaDesde" size="10" styleClass="box" value="" readonly="true"></html:text>
				&nbsp;&nbsp;
				<a onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="general.literal.seleccionarFecha"/>'  border="0"></a>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.fechaHasta"/>:
				&nbsp;(*)&nbsp;&nbsp;&nbsp;
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaHasta" size="10" styleClass="box" value="" readonly="true"></html:text>
				&nbsp;&nbsp;
				<a onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="general.literal.seleccionarFecha"/>'  border="0"></a>
			</td>
		</tr>
		<tr>
			<td class="labelText" colspan="2">
				<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.observaciones"/>:
			</td>
		</tr>
		<tr>
			<td class="labelText" colspan="2">
				<html:textarea name="DefinirCalendarioGuardiaForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" property="observaciones" cols="140" rows="5" style="overflow:auto" styleClass="boxCombo" value="" readOnly="false" ></html:textarea>
			</td>
		</tr>
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	</table>
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	<siga:ConjBotonesAccion botones="Y,C" modal="M"/>
	
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">

		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			sub();
		  var fechasOk = false;	
		  if (document.forms[0].fechaDesde.value!="" && document.forms[0].fechaHasta.value!="")
		  	 fechasOk = true;
		  if (fechasOk && ( (compararFecha(document.forms[0].fechaDesde,document.forms[0].fechaHasta)==2) ||
		  					(compararFecha(document.forms[0].fechaDesde,document.forms[0].fechaHasta)==0) )) {
				//Valido por struts las observaciones
				if (validateDefinirCalendarioGuardiaForm(document.DefinirCalendarioGuardiaForm)){
					document.forms[0].modo.value = "insertarCalendario";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
					window.returnValue="MODIFICADO";
				}else{
					fin();
					return false;
				}
			}
			else{ 
				alert('<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.aviso1"/>');
				fin();
				return false;
			}
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>