<!-- altaTurnos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Colegio cole = (Colegio)ses.getAttribute("colegio");

	String dato[] = {usr.getLocation()};
	// Obtenemos los datos ocultos.
	String IRPF 		= String.valueOf(request.getAttribute("IRPF"));
	String validarinscripciones 	= String.valueOf(request.getAttribute("VALIDARINSCRIPCIONES"));
	Integer guardias    = ((Integer)request.getAttribute("GUARDIAS"));
	String paso 		= String.valueOf(request.getAttribute("paso"));
	String origen 		= String.valueOf(request.getAttribute("origen"));
	Integer idturno 	= ((Integer)request.getAttribute("idturno"));
	Integer idpersona 	= ((Integer)request.getAttribute("IDPERSONA"));
	String idinstitucion = null;
	if((Integer)request.getAttribute("IDINSTITUCION") == null)
		idinstitucion = usr.getLocation();
	else
		idinstitucion = String.valueOf(request.getAttribute("IDINSTITUCION"));

	String titulo = String.valueOf(request.getAttribute("titulo"));
	String action = String.valueOf(request.getAttribute("action"));
	// comprobamos si tenemos fecha de solicitud y observaciones. Si es asi es pq estamos en una validacion.
	String lafechasolicitud = String.valueOf(request.getAttribute("FECHASOLICITUD"));
	String oSolicitud = String.valueOf(request.getAttribute("OBSERVACIONESSOLICITUD"));
	String fValidacion = String.valueOf(request.getAttribute("FECHAVALIDACION"));
	String oValidacion = String.valueOf(request.getAttribute("OBSERVACIONESVALIDACION")).trim();
	String fSolicitudBaja = String.valueOf(request.getAttribute("FECHASOLICITUDBAJA"));
	String oSolicitudBaja = String.valueOf(request.getAttribute("OBSERVACIONESBAJA")).trim();

	if(oSolicitud != null && oSolicitud.equals("null")) oSolicitud = "";
	if(oValidacion != null && oValidacion.equals("null")) oValidacion = "";
	if(oSolicitudBaja != null && oSolicitudBaja.equals("null")) oSolicitudBaja = "";

	if(fValidacion != null && fValidacion.equals("null")) fValidacion = "";
	if(fSolicitudBaja != null && fSolicitudBaja.equals("null")) fSolicitudBaja = "";
	
	String fSolicitud = "";
	if(lafechasolicitud.length()>10)
		fSolicitud = lafechasolicitud.substring(8,10)+"/"+
					lafechasolicitud.substring(5,7)+"/"+
					lafechasolicitud.substring(0,4);
	if(fValidacion.length()>10)
		fValidacion = fValidacion.substring(8,10)+"/"+
					fValidacion.substring(5,7)+"/"+
					fValidacion.substring(0,4);
	if(fSolicitudBaja.length()>10)
		fSolicitudBaja = fSolicitudBaja.substring(8,10)+"/"+
					fSolicitudBaja.substring(5,7)+"/"+
					fSolicitudBaja.substring(0,4);
	// Creamos la fecha actual
	java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
	String fecha = formador.format(new Date());
	// Preparamos los botones de accion
	String botones = String.valueOf(request.getAttribute("botones"));
	//Variables de trabajo
	String idRetencion = "";

%>	
<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaTurnos.literal.title"/></title>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<script type="text/javascript">
	
		function mostrarCalendario(numfila){
			var resultado;			
			var tabla;
			tabla = document.getElementById('listadoCalendario');
			resultado = showCalendarGeneral(tabla.rows[numfila].cells[0].all[4]);
		}		

		function obtenerFecha(fecha)
		{
			if(fecha == 'validacion')
			{
				if(document.forms[0].validacion.checked)
				{
					document.forms[0].fechaSolicitudBaja.value = "";
					document.forms[0].baja.checked = false;
					document.forms[0].denegar.checked = false;
					document.forms[0].fechaBaja.value = "";
					document.forms[0].fechaValidacion.value = "<%=fecha%>";
				}
				else
				{
					document.forms[0].fechaValidacion.value = "";
				}
			}
			else if(fecha == 'baja')
			{
				if(document.forms[0].baja.checked)
				{
					document.forms[0].fechaValidacion.value = "";
					document.forms[0].validacion.checked = false;
					document.forms[0].fechaSolicitudBaja.value = "<%=fecha%>";
				}
				else
				{
					document.forms[0].fechaSolicitudBaja.value = "";
				} 
			}
			else if(fecha == 'denegar')
			{
				if(document.forms[0].denegar.checked)
				{
					document.forms[0].fechaSolicitudBaja.value = "<%=fecha%>";
					document.forms[0].fechaValidacion.value = "<%=fecha%>";
					document.forms[0].validacion.checked = false;
					document.forms[0].fechaBaja.value = "<%=fecha%>";
					document.forms[0].validacion.disabled = true;
				}
				else
				{ 
					document.forms[0].fechaBaja.value = "";
					document.forms[0].validacion.disabled = false;
				}
			}
			else if(fecha == 'sbaja')
			{
				if(document.forms[0].confirmacionbaja.checked)
				{
					document.forms[0].fechaBaja.value = "<%=fecha%>";
				}
				else
				{
					document.forms[0].fechaBaja.value = "";
				}
			}
		}
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="<%=titulo%>"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralGrande" 
-->
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="JGR_AltaTurnosGuardias.do" method="post" target="_self">
	<input type="hidden" name="paso" value="2">
	<input type="hidden" name="origen" value="altaTurnos">
	<input type="hidden" name="modo" value="nuevo">
	<input type="hidden" name="idTurno" value="<%=idturno%>">
	<input type="hidden" name="idPersona" value="<%=idpersona%>">
	<input type="hidden" name="idInstitucion" value="<%=idinstitucion%>">
	<input type="hidden" name="guardias" value="<%=guardias%>">
	<input type="hidden" name="validarInscripciones" value="<%=validarinscripciones%>">
	<input type="hidden" name="fechaSolicitud" value="<%=lafechasolicitud%>">
	<input type="hidden" name="idRetencion" />

<% 
	// SOLICITUD DE ALTA
	if (action.equals("/JGR_AltaTurnosGuardias")) { 
%>
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudAlta">
	 
	 <table width="100%" border="0">
		<tr>
		<!-- obtenemos los campos para el alta de turnos -->
			<td width="5%" class="labelText">&nbsp;</td>
			<td width="20%" class="labelText" ><siga:Idioma key="gratuita.altaTurnos.literal.fsolicitud"/>
			</td>
			<td width="55%">			
				<%
				if(!action.equals("/JGR_AltaTurnosGuardias"))
				{
				%>
				<html:text name="AltaTurnosGuardiasForm" property="fechaSolicitudAux" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fSolicitud%>" readOnly="true" ></html:text>
				<%
				}else
				{
				%>
				<html:text name="AltaTurnosGuardiasForm" property="fechaSolicitud" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true" ></html:text>
				<%
				}
				%>
			</td>
			<td width="20%" class="labelText">
			<%if(IRPF.equals("0"))
			{
				String laRetencion = (String)request.getAttribute("retencion");
				idRetencion = (String)request.getAttribute("idretencion");
			%>
				<siga:Idioma key='gratuita.altaTurnos.literal.retencion'/>
			<%
				if(laRetencion!=null){	
			%>
					<html:text name="AltaTurnosGuardiasForm" property="idRetencionSociedad" size="4" maxlength="4" styleClass="box" readOnly="true" value="<%=laRetencion%>"></html:text>
			<%	}else{%>
					<siga:ComboBD nombre="idReten" tipo="tiposirpfsl" estilo="true" clase="boxCombo" obligatorio="false"/>
			<%	
				}
			  }
			%>
			</td>
		</tr>
	 </table>
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"><siga:Idioma key="gratuita.altaTurnos.literal.osolicitud"/>
			</td>
			<td class="labelText">&nbsp;</td>
		</tr>
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"> 
			<%
			if(action.equals("/JGR_AltaTurnosGuardias"))
			{
			%>
				<html:textarea name="AltaTurnosGuardiasForm" property="observacionesSolicitud" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="140" rows="2" style="overflow:auto" styleClass="boxCombo" value="<%=oSolicitud%>" readOnly="false" ></html:textarea>
			<%}else{%>
				<html:textarea name="AltaTurnosGuardiasForm" property="observacionesSolicitud" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="140" rows="2" style="overflow:auto" styleClass="boxConsulta" value="<%=oSolicitud%>" readOnly="true" ></html:textarea></td>
			<%}%>
			<td class="labelText">
			&nbsp;
			</td>
		</tr>
  	 </table>
	</siga:ConjCampos>
<% 
	}
%>

<% 
	if ( action.equals("/JGR_ValidarTurnos") || (action.equals("/JGR_AltaTurnosGuardias")  && validarinscripciones.equals("S")) && !usr.isLetrado())
   { 
	// RGG 21-02-2006 SOLO PARA VALIDACION CUANDO SE VALIDAN COSAS Y SOLO PARA EL AGENTE
%>
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.validacionAlta">
	 <table width="100%" border="0">
		<!-- bloqueado -->
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td  width="20%" class="labelText" colspan="2"><siga:Idioma key="gratuita.altaTurnos.literal.fvalidacion"/>
			</td>
			<td  width="15%" >
			<html:text name="AltaTurnosGuardiasForm" property="fechaValidacion" size="10" maxlength="10" styleClass="boxConsulta" readOnly="true" value=""></html:text>
			</td>
			<td class="labelText" width="15%" >
			<siga:Idioma key="gratuita.altaTurnos.literal.validacion"/>
			</td>
			<td  width="15%" >
		    <input type="checkbox" name="validacion" value="no" onClick="obtenerFecha('validacion')">
			</td>
			<td  width="15%" class="labelText">
				<siga:Idioma key="gratuita.altaTurnos.literal.denegar"/>
			</td>
			<td  width="15%" >
		    	<input type="checkbox" name="denegar" value="no" onClick="obtenerFecha('denegar');">
			</td> 
		</tr>
	 </table>
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"><siga:Idioma key="gratuita.altaTurnos.literal.ovalidacion"/>&nbsp;(*)
			</td>
			<td class="labelText">&nbsp;</td>
		</tr>
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
				<td class="labelText" colspan="2">
				<html:textarea name="AltaTurnosGuardiasForm" property="observacionesValidacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="140" rows="2" style="overflow:auto" styleClass="boxCombo" value="<%=oValidacion%>"></html:textarea>
			</td>
			<td class="labelText">&nbsp;</td>
		</tr>
  	 </table>
	</siga:ConjCampos>
<% 
	} else {
		if (validarinscripciones.equals("S")) {
%>
	<!-- fecha actual de validación -->
	<html:hidden property="fechaValidacion" value="" />
<% 
		} else {
%>
	<!-- fecha actual de validación -->
	<html:hidden property="fechaValidacion" value="<%=fecha%>" />
<% 
		}
	}
%>

<% if (action.equals("/JGR_SolicitarBajaTurno") || action.equals("/JGR_BajaTurnos")) { 
	// RGG 21-02-2006 SOLO PARA LAS BAJAS
%>
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudBaja">
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td width="20%" class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.fsolBaja"/>
			</td>
			<td  width="75%">
				<%if((action.equals("/JGR_SolicitarBajaTurno") || action.equals("/JGR_BajaTurnos"))  && fSolicitudBaja.equals(""))
				{%>
					<html:text name="AltaTurnosGuardiasForm" property="fechaSolicitudBaja" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true" ></html:text>
				<%}else{%>
					<html:text name="AltaTurnosGuardiasForm" property="fechaSolicitudBaja" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fSolicitudBaja%>" readOnly="true" ></html:text>
				<%}%>
			</td>
		</tr>
	 </table>
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"><siga:Idioma key="gratuita.altaTurnos.literal.mbaja"/>
			</td>
			<td class="labelText">&nbsp;</td>
		</tr>
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"> 
				<%
				if(action.equals("/JGR_SolicitarBajaTurno"))
				{
				%>
					<html:textarea name="BajaTurnosForm" property="observacionesBaja" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="140" rows="2" style="overflow:auto" styleClass="box" value="<%=oSolicitudBaja%>" readOnly="false" ></html:textarea>
				<%}
				else
				{
				%>
					<html:textarea name="BajaTurnosForm" property="observacionesBaja" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" cols="140" rows="2" style="overflow:auto" styleClass="boxConsulta" value="<%=oSolicitudBaja%>" readOnly="true" ></html:textarea>
				<%}%>
			<td class="labelText">&nbsp;</td>
		</tr>
  	 </table>
	</siga:ConjCampos>
<% 
	} else {
%>
	<html:hidden property="fechaSolicitudBaja" value=""></html:hidden>
	<html:hidden property="baja" value=""></html:hidden>
<% 
	} 
%>


<% 
   if ((action.equals("/JGR_BajaTurnos") || action.equals("/JGR_SolicitarBajaTurno"))  && validarinscripciones.equals("S") && !usr.isLetrado())
   { 
	// RGG 21-02-2006 SOLO PARA VALIDACION CUANDO SE VALIDAN COSAS Y SOLO PARA EL AGENTE
%>

<% 
//   if (action.equals("/JGR_BajaTurnos") && validarinscripciones.equals("S") && !usr.isLetrado()) { 
	// RGG 21-02-2006 SOLO PARA VALIDACION DE BAJAS CUANDO HAY QUE VALIDAR Y SOLO PARA AGENTES
	
%>
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.validacionBaja">
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td  width="20%" class="labelText"><siga:Idioma key="gratuita.altaTurnos.literal.fvalBaja"/>
			</td>
			<td width="15%">
			<html:text name="BajaTurnosForm" property="fechaBaja" size="10" maxlength="10" styleClass="boxConsulta" value="" readOnly="true" ></html:text>
			</td>
			<td width="20%" class="labelText">
			<siga:Idioma key="gratuita.altaTurnos.literal.validacion"/>
			</td>
			<td width="40%">
		    <input type="checkbox" name="confirmacionbaja" value="no" onClick="obtenerFecha('sbaja')">
			</td>
		</tr>
  	 </table>
	</siga:ConjCampos>
<% 
		} else {

	 		if ((action.equals("/JGR_BajaTurnos") || action.equals("/JGR_SolicitarBajaTurno"))  && !validarinscripciones.equals("S")) {
%>
 	<!-- fecha actual de validación --> 
 	<html:hidden property="fechaBaja" value="<%=fecha%>" />
<% 
			} else {
%>
	<!-- fecha actual de validación -->
	<html:hidden property="fechaBaja" value="" />
<% 
			} 
		}
%>
   	</html:form>

	<siga:ConjBotonesAccion botones="<%=botones%>" />	

	<!-- FIN: CAMPOS -->
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->


	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() 
		{
			location.back();
		}

		function accionCerrar() 
		{		
			window.close();
		}
		
		function accionCancelar() 
		{		
			window.close();
		}
		
		<!-- Asociada al boton Siguiente -->
		function accionSiguiente() 
		{	
           sub();
			// Validamos que exista fecha de validacion y observaciones de validacion
			if(document.forms[0].validacion && (document.forms[0].validacion.checked || document.forms[0].denegar.checked))
			{
				if(document.forms[0].fechaValidacion.value == "")
				{  fin();
					alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
					document.forms[0].fechaValidacion.focus();
					return false;
				}
				if(document.forms[0].observacionesValidacion.value == "")
				{   fin();
					alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
					document.forms[0].observacionesValidacion.focus();
					return false;
				}
			}
			// Si existe retencion, validamos que no sea 0
			if(document.forms[0].idReten)
			{
				if(document.forms[0].idReten.options[document.forms[0].idReten.selectedIndex].value == "")
				{   fin();
					alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertretencion'/>");
					return false;
				}
				document.forms[0].idRetencion.value =  document.forms[0].idReten.options[document.forms[0].idReten.selectedIndex].value;
			}
			if(document.forms[0].idRetencionSociedad)
			{
				document.forms[0].idRetencion.value = "<%=idRetencion%>";
			}
			document.forms[0].modo.value="ver";
			document.forms[0].paso.value="guardia";
			document.forms[0].submit();
		}

		function accionFinalizar() 
		{		
			sub();
			if(document.forms[0].validacion && document.forms[0].baja && 
				!document.forms[0].validacion.checked && !document.forms[0].denegar.checked && !document.forms[0].baja.checked)
			{
					fin();
					alert("<siga:Idioma key="gratuita.altaTurno.literal.alert1"/>");
					return false;
			}
			else
			{
				// Validamos que exista fecha de validacion y observaciones de validacion
				if(document.forms[0].validacion && (document.forms[0].validacion.checked || document.forms[0].denegar.checked))
				{
					if(document.forms[0].fechaValidacion.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alerFeVa'/>");
						document.forms[0].fechaValidacion.focus();
						return false;
					}
					if(document.forms[0].observacionesValidacion.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObVa'/>");
						document.forms[0].observacionesValidacion.focus();
						return false;
					}
				}
				// Validamos que exista fecha de baja y observaciones de baja
				if(document.forms[0].baja && document.forms[0].baja.checked)
				{
					if(document.forms[0].observacionesBaja.value == "")
					{
						fin();
						alert("<siga:Idioma key='gratuita.altaTurnos.literal.alertObBa'/>");
						document.forms[0].observacionesBaja.focus();
						return false;
					}
				}
			}			
			document.forms[0].action="<%=app%>/JGR_ValidarTurnos.do";
			document.forms[0].action="<%=app%><%=action%>.do";
			document.forms[0].target="submitArea";
			document.forms[0].modo.value="modificar";
			document.forms[0].submit();
			window.returnValue="MODIFICADO";			
		}
		
		function deshabilitarValidacion()
		{
			var f = document.forms[0];
			if(f.denegar.checked == true)
			{
				f.validacion.checked = false;
				f.validacion.disabled = true;
				document.forms[0].fechaValidacion.value = "";
			}
			else
			{
				f.validacion.disabled = false;
				
			}
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
