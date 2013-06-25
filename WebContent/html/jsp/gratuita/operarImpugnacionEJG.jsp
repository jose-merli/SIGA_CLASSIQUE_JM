<!-- operarImpugnacionEJG.jsp -->
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
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");

	Hashtable miHash = (Hashtable) ses.getAttribute("DATABACKUP");
	String accion = (String) ses.getAttribute("accion");
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) accion="ver";
	String dato[] = { (String) usr.getLocation() };

	String anio = "", numero = "", idTipoEJG = "", observaciones = "";
	String fechaRatificacion = "", fechaResolucionCAJG = "", fechaNotificacion = "";
	String fechaAuto = "";
	//Nuevas cadenas para las nueas cajas de la ventana
	String fechaPublicacion = "", observacionImpugnacion ="",numeroResolucion="", anioResolucion="",bisResolucion ="";
	boolean requiereTurnado = false, requiereBis = false;
	ArrayList vTipoSentidoAuto = new ArrayList(), vTipoResolAuto = new ArrayList();

	try {
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();

		if (miHash.containsKey("TURNADORATIFICACION"))
			requiereTurnado = !miHash.get("TURNADORATIFICACION")
					.toString().equals("0");
		if (miHash.containsKey(ScsEJGBean.C_IDTIPOSENTIDOAUTO)
				&& miHash.get(ScsEJGBean.C_IDTIPOSENTIDOAUTO) != "")
			vTipoSentidoAuto.add(miHash.get(
					ScsEJGBean.C_IDTIPOSENTIDOAUTO).toString());
		
		if (miHash.containsKey(ScsEJGBean.C_IDTIPORESOLAUTO)
				&& miHash.get(ScsEJGBean.C_IDTIPORESOLAUTO) != "")
			vTipoResolAuto.add(miHash.get(ScsEJGBean.C_IDTIPORESOLAUTO)
					.toString());

		if (miHash.containsKey("FECHAAUTO"))
			fechaAuto = GstDate.getFormatedDateShort("",
					miHash.get("FECHAAUTO").toString()).toString();

		//Crear nuevos campos en la pestaña de impugnaciones del EJG
		if (miHash.containsKey("FECHAPUBLICACION"))
			fechaPublicacion = GstDate.getFormatedDateShort("",
					miHash.get("FECHAPUBLICACION").toString()).toString();
				
		if (miHash.containsKey("OBSERVACIONIMPUGNACION"))
			observacionImpugnacion = miHash.get("OBSERVACIONIMPUGNACION").toString();
		
		if (miHash.containsKey("NUMERORESOLUCION"))
			numeroResolucion = miHash.get("NUMERORESOLUCION").toString();
		
		if (miHash.containsKey("ANIORESOLUCION"))
			anioResolucion = miHash.get("ANIORESOLUCION").toString();
		
		if (miHash.containsKey("BISRESOLUCION"))			
			requiereBis = !miHash.get("BISRESOLUCION").toString().equals("0");			
	} catch (Exception e) {
		e.printStackTrace();
	}
	;
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>	
	<script type="text/javascript">
		function refrescarLocal()
		{
			document.location.reload();
		}
	</script>
	<siga:Titulo 
		titulo="pestana.justiciagratuitaejg.impugnacion" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
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
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>
	

	<siga:ConjCampos leyenda="gratuita.dictamenEJG.literal.datosImpugnación">
	
	<table align="center"  width="100%" >
	
	<html:form action="/JGR_ImpugnacionEJG" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>

	<tr>		
	<td>			
	
	<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.operarRatificacion.literal.fechaAuto"/>&nbsp;(*)
			</td>
			<td class="labelText">
				<%if (accion.equalsIgnoreCase("ver")) {%>
					<siga:Fecha nombreCampo="fechaAuto" valorInicial="<%=fechaAuto%>" disabled="true" readonly="true"></siga:Fecha>
				<%}else{%>
					<siga:Fecha nombreCampo="fechaAuto" valorInicial="<%=fechaAuto%>" readonly="true"></siga:Fecha>
				<%}%>
			</td>
			<td class="labelText">
				<siga:Idioma key="gratuita.EJG.literal.autoResolutorio"/>
			</td>	
			<td class="labelText">
				<%if (accion.equalsIgnoreCase("ver")) {%>
					<siga:ComboBD nombre="idTipoResolAuto" tipo="idTipoResolAuto" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=vTipoResolAuto%>" readOnly="true"/>
				<%}else{%>
					<siga:ComboBD nombre="idTipoResolAuto" tipo="idTipoResolAuto" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" elementoSel="<%=vTipoResolAuto%>"/>
				<%}%>
			</td>
		</tr>

		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.EJG.literal.sentidoAuto" />
			</td>
			<td class="labelText" colspan="3">
			<%if (accion.equalsIgnoreCase("ver")) {%> 
				<siga:ComboBD
					nombre="idTipoSentidoAuto" tipo="idTipoSentidoAuto"
					clase="boxConsulta" filasMostrar="1" seleccionMultiple="false"
					obligatorio="false" elementoSel="<%=vTipoSentidoAuto%>" ancho="700"
					readOnly="true"/> 
			<%}else{%> 
				<siga:ComboBD
					nombre="idTipoSentidoAuto" tipo="idTipoSentidoAuto"
					clase="boxCombo" filasMostrar="1" seleccionMultiple="false"
					obligatorio="false" elementoSel="<%=vTipoSentidoAuto%>" ancho="700"/> 
			<%}%>
			</td>
			
			
		</tr>
												
		<!-- Inicio Código Crear nuevos campos en la pestaña de impugnaciones del EJG -->
		
		<tr>
			<td class="labelText"  width="300">	
				<siga:Idioma key='gratuita.operarRatificacion.literal.numeroResolucion'/> 
			</td>
	   		<td class="labelText">	
				<% if (accion.equalsIgnoreCase("ver")) {%>
			  		<html:text name="DefinirEJGForm"  property="numeroResolucion"  onkeypress="javascript:return soloDigitos(event)"  onblur="habilitarBisResolucion();" size="11" maxlength="11"  styleClass="boxConsulta"  value="<%=numeroResolucion%>" readonly="true"></html:text> / 
                	<html:text name="DefinirEJGForm"  property="anioResolucion"    onkeypress="javascript:return soloDigitos(event)"  onblur="habilitarBisResolucion();" size="4" maxlength="4"    styleClass="boxConsulta"  value="<%=anioResolucion%>" readonly="true"></html:text>
                	<siga:Idioma key="gratuita.operarRatificacion.literal.bisResolucion"/>&nbsp;&nbsp;
                	<input type="Checkbox" name="bisResolucion"  <%=(requiereBis?"checked":"")%> disabled>
                	
				<%} else {%>
					<html:text name="DefinirEJGForm"  property="numeroResolucion" onkeypress="javascript:return soloDigitos(event)" onblur="habilitarBisResolucion();" size="11" maxlength="11"  styleClass="boxNumber"  value="<%=numeroResolucion%>" ></html:text> / 
                	<html:text name="DefinirEJGForm"  property="anioResolucion"   onkeypress="javascript:return soloDigitos(event)" onblur="habilitarBisResolucion();" size="4" maxlength="4"    styleClass="boxNumber"  value="<%=anioResolucion%>"></html:text>
                	<siga:Idioma key="gratuita.operarRatificacion.literal.bisResolucion"/>&nbsp;&nbsp;
                	<input type="Checkbox" name="bisResolucion"  id="bisResolucion"  <%=(requiereBis?"checked":"")%>>
				<%}%>
	  		</td>
	  		<td class="labelText">
				<siga:Idioma key="gratuita.operarRatificacion.literal.fechaPublicacion"/>
			</td>
			<td class="labelText">			
				<%if (accion.equalsIgnoreCase("ver")) {%>
					<siga:Fecha nombreCampo="fechaPublicacion" valorInicial="<%=fechaPublicacion%>" disabled="true" readonly="true"></siga:Fecha>
					
				<%}else{%>
					<siga:Fecha nombreCampo="fechaPublicacion" valorInicial="<%=fechaPublicacion%>" readonly="true"></siga:Fecha>
					
				<%}%>
			</td>
	  		
	  	</tr>


		
		<tr>
			<td class="labelText" colspan="1">
				<siga:Idioma key="gratuita.operarRatificacion.literal.observaciones"/>
			</td>
			<td class="labelText" colspan="4">	
				<%if (accion.equalsIgnoreCase("ver")) {%>	
					<textarea name="observacionImpugnacion" class="boxConsulta" style="width:770px" rows="18" readOnly="true"><%=observacionImpugnacion%></textarea>
				<%} else {%>
					<textarea name="observacionImpugnacion" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" class="box" style="width:770px" rows="18"><%=observacionImpugnacion%></textarea>
				<%}%>
			</td>		
		</tr>
		<!-- Fin Código Crear nuevos campos en la pestaña de impugnaciones del EJG -->

		<tr>
			<td class="labelText" colspan="4">
				<siga:Idioma key="gratuita.operarRatificacion.literal.requiereTurnado" />&nbsp;&nbsp;
				<input type="Checkbox" name="turnadoRatificacion" 
					<%=(accion.equalsIgnoreCase("ver")?"disabled":"")%> 
					<%=(requiereTurnado?"checked":"")%>>
			</td>
		</tr>
				
	</html:form>
	</table>

	</siga:ConjCampos>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
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
			var fecha  = document.forms[0].fechaAuto.value;
			if(fecha==''){
				alert('<siga:Idioma key="gratuita.operarRatificacion.literal.fechaAuto"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
				fin();
				return false;
			}
			document.forms[0].submit();
		}

		//Método que habilita o deshabilita el check de Bis 
		function habilitarBisResolucion()
		{

			if (document.forms[0].numeroResolucion.value!="" || document.forms[0].anioResolucion.value!="") 
				{
				jQuery("#bisResolucion").removeAttr("disabled");				
				} 
				else
				{
					jQuery("#bisResolucion").attr("disabled","disabled");	
				}
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