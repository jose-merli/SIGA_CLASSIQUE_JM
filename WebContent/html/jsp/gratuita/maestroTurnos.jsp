<!-- maestroTurnos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsTurnoAdm"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.CenPersonaAdm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasAdm"%>
<%@ page import="com.siga.gratuita.form.DefinirTurnosForm"%>
<%@ page import="com.siga.beans.ScsOrdenacionColasBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String accion = (String)request.getSession().getAttribute("accionTurno");
	Hashtable turno = (Hashtable) ses.getAttribute("turnoElegido");
	String validarInscripciones=(String)turno.get("VALIDARINSCRIPCIONES"); 
	
	String asterisco = "&nbsp(*)&nbsp";
	int pcajgActivo = 0;
    boolean obligatorioCodigoExterno = false;
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute(
				"pcajgActivo").toString());		
	}
	
	if (pcajgActivo == 4){		
		obligatorioCodigoExterno = true;
	}

	DefinirTurnosForm miform = (DefinirTurnosForm) request.getAttribute("DefinirTurnosForm");
	request.setAttribute("BUSQUEDAREALIZADA",(String)request.getSession().getAttribute("BUSQUEDAREALIZADA"));
	String turnosBajaLogica = (String)request.getSession().getAttribute("BAJALOGICATURNOS");
	
	Vector campos = new Vector();
	//Vector ocultos = new Vector();
	Vector vOrdenacion = new Vector();
	CenPersonaBean personaBean = null;
	String where="";
	String[] dato1 = {usr.getLocation()};
	String[] dato2 = {usr.getLocation(),""};
	String[] datos2={usr.getLocation(),usr.getLanguage()};
	String conPestanas="";
	if (!accion.equalsIgnoreCase("nuevo")){
		campos = (Vector)request.getSession().getAttribute("campos");
		//ocultos = (Vector)request.getSession().getAttribute("ocultos");
		//ses.setAttribute("turno",(String)ocultos.get(0));
		where = " where "+ ScsTurnoBean.C_IDTURNO+"="+(String)turno.get("IDTURNO")+" and "+ ScsTurnoBean.C_IDINSTITUCION+"="+usr.getLocation()+" ";
	}
	if ((accion.equalsIgnoreCase("ver"))||accion.equalsIgnoreCase("editar")){
				CenPersonaAdm persona = new CenPersonaAdm(usr);
				String condicion ="";
				if (!((String)turno.get("IDPERSONA_ULTIMO")).equals(""))condicion=" where "+CenPersonaBean.C_IDPERSONA+"="+(String)turno.get("IDPERSONA_ULTIMO")+" ";
				else condicion=" where "+CenPersonaBean.C_IDPERSONA+"=-1";
				Vector vPersona = new Vector();
				vPersona=persona.select(condicion);
				if (vPersona.size()>0)personaBean = (CenPersonaBean)vPersona.get(0);}
	String valida="";
	String valida2="";
	if(accion.equalsIgnoreCase("ver")){
		valida="readOnly='true'";
		valida2="disabled";
	}
	int elPrimero=1;
	String siguienteAccion ="";
	String classTipo="box";
	String classCombo="boxCombo";
	String localiz="";
	if (accion.equalsIgnoreCase("Editar")){
		siguienteAccion="modificar";
	}
	else {
		siguienteAccion="insertar";
	}
	
	if(accion.equalsIgnoreCase("ver")){
		classTipo="boxConsulta";
		classCombo="boxConsulta";
	}
	
	localiz="gratuita.turnos.localizacion";
	
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
	
	String alto = "345";
	ArrayList vTipoTurno = new ArrayList();
%>

<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DefinirTurnosFormNuevo" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	  	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	 	
	
 	
<script language="JavaScript">

	var validarNew='';
	function refrescarLocal()
	{	document.location=document.location;
		/*document.forms[0].modo.value="abrir";
		document.forms[0].submit();*/
	}
	
		//Asociada al boton Volver -->
		function accionVolver() 
		{		
			document.forms[0].action="JGR_DefinirTurnos.do";
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].submit();
		}

		//Asociada al boton Cancelar -->
		function accionCancelar() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{
		   	
		   sub();

		   
		   if (document.forms[0].validacionInscripcion.checked){
		     
		       validarNew="S";
		    }else{
			   
			   validarNew="N";
			}
			
			<%
				String mensaje = UtilidadesString.getMensajeIdioma(usr, "messages.subzona.obligatoria");
			%>
			var mensaje="<%=mensaje%>";
			var f=document.getElementById("DefinirTurnosForm");
		
			if (<%=obligatorioCodigoExterno%> && document.forms[0].codigoExterno.value.length<1) {
				<%
				String mensajecodigoexterno = UtilidadesString.getMensajeIdioma(usr, "messages.codigoExterno.obligatoria");
				%>
				var error = "<%= mensajecodigoexterno%>";
				alert(error);
				fin();
				return false;	
				}			
			

			if (document.forms[0].subzona.value=="") {
				alert(mensaje);
				fin();
				return false;
			} else {
				if (document.forms[0].grupoFacturacion.value==''){
					alert('<siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
					fin();
					return false;
				}else {
						if (document.forms[0].validarOld.value=="S" && (document.forms[0].validarOld.value!=validarNew)){// Si el check de validarInscripcion estaba activado y se desactiva
						  men = '<siga:Idioma key="gratuita.definirTurnosIndex.literal.validarInscripcion"/>';
						  if (confirm(men)) {
    					   document.forms[0].validarAltas.value="1";
    					  }else{
						    document.forms[0].validarAltas.value="0";
						  }
						 }					
						document.forms[0].target="submitArea";
						document.forms[0].modo.value="modificar";
						document.forms[0].submit();
						
				 }
				}
		}

		//Asociada al boton Siguiente -->
		function accionSiguiente() 
		{		
			document.forms[0].target="_self";
			document.forms[0].action="JGR_AltaTurnosGuardias.do";
			document.forms[0].modo.value="nuevo";
			document.forms[0].submit();
		}


</script>
<!-- script de salto a partidos -->
<script language="JavaScript">
		function mostrarPartido(obj)
		{
			if (document.partidosJud) {
				document.partidosJud.idinstitucion.value="<%=usr.getLocation()%>";
				document.partidosJud.idzona.value=document.forms[0].zona.value;
				document.partidosJud.idsubzona.value=obj.value;
				document.partidosJud.submit();
			}
			return true;
		}

		function mostrarPartidoVer()
		{
			if (document.partidosJud) {
				document.partidosJud.idinstitucion.value="<%=usr.getLocation()%>";
				document.partidosJud.idzona.value="<%=(String)turno.get("IDZONA")%>";
				document.partidosJud.idsubzona.value="<%=(String)turno.get("IDSUBZONA")%>";
				document.partidosJud.submit();
			}
			return true;
		}
</script>

	<siga:Titulo
		titulo="censo.fichaCliente.sjcs.turnos.datosGenerales.cabecera" 
		localizacion="<%=localiz%>"/>

	<script language="JavaScript">
	
		function recargarCombos()
		{   
			<%if(accion.equalsIgnoreCase("Editar")){%>
				var tmp1 = document.getElementsByName("area");
				var tmp2 = tmp1[0];			 
				tmp2.onchange();
				var tmp3 = document.getElementsByName("zona");
				var tmp4 = tmp3[0];
				tmp4.onchange();
			<%}%>
		}
		
	</script>

</head>

<body onLoad="recargarCombos();<%if (accion.equalsIgnoreCase("ver")){%>mostrarPartidoVer();<%}%>" class="tablaCentralCampos">
	
	<html:form action = "/DefinirTurnosAction.do" method="POST" target="submitArea" enctype="multipart/form-data">
	<input type="hidden" name="paso" value="0">
	<input type="hidden" name="modo" value="modificar">
	<input type="hidden" name="validarAltas" value="0">
	<input type="hidden" name="validarOld" value="<%=validarInscripciones%>">
	<input type="hidden" name="turnosBajaLogica" value="<%=turnosBajaLogica%>">
	
		<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.estado">
		<%if (accion.equalsIgnoreCase("editar")){%>

			<table width="30%" border="0" align="left" class="labelText" >
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT NAME="visibilidad" TYPE=RADIO VALUE="1" <% if((((String)turno.get("VISIBILIDAD")).equalsIgnoreCase("1"))||(accion.equalsIgnoreCase("nuevo"))){ %> checked <% } %> > <siga:Idioma key="gratuita.maestroTurnos.literal.bajaLogica.alta"/> 
				</td>

				<td>
					<INPUT NAME="visibilidad" TYPE=RADIO VALUE="0" <% if((((String)turno.get("VISIBILIDAD")).equalsIgnoreCase("0"))){ %> checked <% } %>> <siga:Idioma key="gratuita.maestroTurnos.literal.bajaLogica.baja"/>
				</td>
			</table>
			<%}else{
				String estadoVisibilidad = "";
				if(((String)turno.get("VISIBILIDAD")).equalsIgnoreCase("1")){
					estadoVisibilidad="ALTA";
				}else{
					estadoVisibilidad="BAJA";
				}
					%>
			<td style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text name="DefinirTurnosForm" property="visibilidad" size="50" maxlength="50" styleClass="boxConsulta" value='<%=estadoVisibilidad%>' readOnly="true"></html:text></td>
			<%}%>
		</siga:ConjCampos>

		<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.datosGenerales">
		<table width="100%" border="0" align="center" >
			<tr>

			<td class="labelText">
				<siga:Idioma key="gratuita.definirTurnosIndex.literal.abreviatura"/>&nbsp;(*)
			</td>
			<td>
				<input name="abreviatura" type="text" class="<%=classTipo%>" maxlength="30" <% if (!accion.equalsIgnoreCase("nuevo")){%>value='<%=(String)turno.get("ABREVIATURA")%>'<%}%> <%=valida%> style="width:300" >
			</td>
			<td class="labelText">
				<siga:Idioma key="censo.SolicitudIncorporacion.literal.nombre"/> &nbsp;(*)</td>
			<td>
				<input name="nombre" type="text" class="<%=classTipo%>" maxlength="100" <% if (!accion.equalsIgnoreCase("nuevo")){%>value="<%=(String)turno.get("NOMBRE")%>"<%}%> <%=valida%> style="width:300" >
			</td>
		</tr>
		<tr>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.area"/>  &nbsp;(*) </td>
			<td style="text-align:left">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="DefinirTurnosForm" property="area" size="50" maxlength="50" styleClass="boxConsulta" value='<%=(String)turno.get("AREA")%>' readOnly="true"></html:text>
				<%}else {
					ArrayList vArea = new ArrayList();
						try {
							 
							if ((String)turno.get("IDAREA") == "-1") { 
								vArea.add(usr.getLocation());
								vArea.add("0");
							}
							else {
								vArea.add(usr.getLocation());
								vArea.add((String)turno.get("IDAREA")); 
							}
						} catch (Exception e) {
							vArea.add("0");
						}
					%>
					<siga:ComboBD nombre="area" tipo="area" pestana="t" estilo="true" clase="boxCombo" parametro="<%=dato1%>" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vArea%>" obligatorio="false" accion="Hijo:materia"/>
					<%if  (accion.equals("nuevo")) {%>
						<script>document.forms[0].area[0].text="Seleccionar                                                  ";
								document.forms[0].area[0].value="-1";
						</script>
					<%}%>
				<%}%>
						
			</td>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.materia"/> &nbsp;(*)</td>
			<td style="text-align:left">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirTurnosForm" property="materia" size="50" maxlength="15" styleClass="boxConsulta" value='<%=(String)turno.get("MATERIA")%>' readOnly="true"></html:text>
						<%}else{ArrayList vMateria = new ArrayList();
						try {
							 
							if ((String)turno.get("IDMATERIA") == "-1") { 
								vMateria.add("0");
							}
							else {
								vMateria.add((String)turno.get("IDMATERIA")); 
							}
						} catch (Exception e) {
							vMateria.add("0");
						}
						%>
						<siga:ComboBD nombre="materia" pestana="t" tipo="materia" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vMateria%>" hijo="t"/>
						<%}%>
			</td>
		</tr><tr>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.zona"/> &nbsp;(*)</td>
			<td style="text-align:left">
					<%if (accion.equalsIgnoreCase("ver")){%>
						<html:text name="DefinirTurnosForm" property="zona" size="50" maxlength="15" styleClass="boxConsulta" value='<%=(String)turno.get("ZONA")%>' readOnly="true"></html:text>
					<%}else{
						ArrayList vZona = new ArrayList();
						try {
							 
							if ((String)turno.get("IDZONA") == "-1") { 
								vZona.add(usr.getLocation()+",0");
							}
							else {
								vZona.add(usr.getLocation()+","+(String)turno.get("IDZONA")); 
							}
						} catch (Exception e) {
							vZona.add(usr.getLocation()+",0");
						}
					%>
					<siga:ComboBD nombre="zona" pestana="t" tipo="zona" estilo="true" clase="boxCombo" parametro="<%=dato1%>" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vZona%>" obligatorio="true" accion="Hijo:subzona"/>
						<%}%>
			</td>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.subzona"/> &nbsp;(*)</td>
			<td style="text-align:left">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirTurnosForm" property="subzona" size="50" maxlength="15" styleClass="boxConsulta" value='<%=(String)turno.get("SUBZONA")%>' readOnly="true"></html:text>
						<%}else{
								ArrayList vSubzona = new ArrayList();
								try
								{
									vSubzona.add((String)turno.get("IDSUBZONA"));
									
								} catch (Exception e) {
									vSubzona.add("");
								}
								
							%>
							<siga:ComboBD nombre="subzona" pestana="t" accion="parent.mostrarPartido(this)" tipo="subzona" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false"  elementoSel="<%=vSubzona%>" obligatorio="true" hijo="t"/>
						<%}%>	
			</td>
		</tr>
		<!-- línea de partidos judiciales -->
		<tr>		
			<td class="labelText" >
				<siga:Idioma key="gratuita.definirTurnosIndex.literal.partidoJudicial"/>
			</td>
			<td >
				<iframe ID="partidosjudiciales" name="partidosjudiciales"  src="<%=app%>/html/jsp/general/blank.jsp" WIDTH="400"  HEIGHT="35"  FRAMEBORDER="0"  MARGINWIDTH="0"  MARGINHEIGHT="1"  SCROLLING="no"></iframe>
			</td>
			
			 <td class="labelText" >			 
			 
				<siga:Idioma key="gratuita.maestroTurnos.literal.codigoExt"/>
				<%
				if (obligatorioCodigoExterno) {
				%>
				<%=asterisco%> 
				<%
 					}
 				%>
		  </td>
		  <td >
		  <%if (accion.equalsIgnoreCase("ver")){%>
		    <html:text name="DefinirTurnosForm" property="codigoExterno"  size="10" maxlength="10"  styleClass="boxConsulta" readOnly="true" value='<%=(String)turno.get("CODIGOEXT")%>' ></html:text>			
		<%}else{%>				
			<html:text name="DefinirTurnosForm" property="codigoExterno"  size="10" maxlength="10"  styleClass="Box" readOnly="false" value='<%=(String)turno.get("CODIGOEXT")%>' ></html:text>
		<%}%>	
		  </td>
			
	   </tr>
	   
	  
		
		<tr>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.partidaPresupuestaria"/> &nbsp;(*)</td>
			<td  style="text-align:left">
					<%if (accion.equalsIgnoreCase("editar")){%>
						<%ArrayList vPartida = new ArrayList();
						try {
							String partidaP =(String)turno.get("IDPARTIDAPRESUPUESTARIA");
							if (partidaP == "") { 
								vPartida.add("0");
							}
							else {
								vPartida.add((String)turno.get("IDPARTIDAPRESUPUESTARIA")); 
							}
						} catch (Exception e) {
							vPartida.add("0");
						}				
						%>
						<siga:ComboBD nombre="partidaPresupuestaria" pestana="t" tipo="partidaPresupuestaria" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vPartida%>" estilo="true" obligatorio="true" parametro="<%=dato1%>"/>
							<%}else if (accion.equalsIgnoreCase("nuevo")){%>
						<siga:ComboBD nombre="partidaPresupuestaria" pestana="t" tipo="partidaPresupuestaria" clase="boxCombo" estilo="true" obligatorio="true" parametro="<%=dato1%>"/>
					<%}else{%>
						<html:text name="DefinirTurnosForm" property="partidaPresupuestaria" size="45" maxlength="15" styleClass="boxConsulta" value='<%=(String)turno.get("PARTIDAPRESUPUESTARIA")%>' readOnly="true"></html:text>
					<%}%>
			</td>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/>&nbsp;(*)</td>
			<td style="text-align:left">
					<%	if (accion.equalsIgnoreCase("editar")){
						ArrayList vGrupo = new ArrayList();
						try {
							String grupo =(String)turno.get("IDGRUPOFACTURACION");
							if (grupo == "") { 
								vGrupo.add("0");
							}
							else {
								vGrupo.add((String)turno.get("IDGRUPOFACTURACION")); 
							}
						} catch (Exception e) {
							vGrupo.add("0");
						}				
						%>
						<siga:ComboBD nombre="grupoFacturacion" pestana="t" tipo="grupoFacturacion" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vGrupo%>" estilo="true" obligatorio="false" parametro="<%=dato1%>"/>
					<%}else if (accion.equalsIgnoreCase("nuevo")){%>
						<siga:ComboBD nombre="grupoFacturacion" pestana="t" tipo="grupoFacturacion" clase="boxCombo" estilo="true" obligatorio="false" parametro="<%=dato1%>"/>
					<%}else{%>
						<html:text name="DefinirTurnosForm" property="grupoFacturacion" size="15" maxlength="15" styleClass="boxConsulta" value='<%=(String)turno.get("GRUPOFACTURACION")%>' readOnly="true"></html:text>
					<%}%>
		
			</td>
		</tr>
		
		<tr>
			<td class="labelText">
			<siga:Idioma key="gratuita.maestroTurnos.literal.descripcion"/>&nbsp;(*)
			</td>
			<td>
				<textarea class="<%=classTipo%>" style="width:300" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  scroll="none"  name="descripcion" rows="3" <%=valida%>><% if (!accion.equalsIgnoreCase("nuevo")){%><%=(String)turno.get("DESCRIPCION")%><%}%></textarea>
			</td>
			<td class="labelText">
			<siga:Idioma key="gratuita.maestroTurnos.literal.requisitosAcceso"/>&nbsp;(*)
			</td>
			<td>
				<textarea class="<%=classTipo%>" style="width:300"  onKeyDown="cuenta(this,1023)" onChange="cuenta(this,1023)" name="requisitos" rows="3" <%=valida%>><% if (!accion.equalsIgnoreCase("nuevo")){%><%=(String)turno.get("REQUISITOS")%><%}%></textarea>
			</td>
	
		</tr>	
		
		<tr>
			<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.tipoturno"/>&nbsp;
			</td>
			<td style="text-align:left">
				<%if (accion.equalsIgnoreCase("ver")){%>
					<html:text name="DefinirTurnosForm" property="idTipoTurno" size="50" maxlength="50" styleClass="boxConsulta" value='<%=(String)turno.get("TIPOTURNO")%>' readOnly="true"></html:text>
				<%}else {
					try {
						 
						if ((String)turno.get("IDTIPOTURNO") == "-1") { 
							vTipoTurno.add(usr.getLocation());
							vTipoTurno.add("0");
						}
						else {
							vTipoTurno.add(usr.getLocation());
							vTipoTurno.add((String)turno.get("IDTIPOTURNO")); 
						}
					} catch (Exception e) {
						vTipoTurno.add("0");
					}%>
					<siga:ComboBD nombre="idTipoTurno" tipo="tipoTurno" clase="boxCombo" estilo="true" obligatorio="false" elementoSel='<%=vTipoTurno%>' parametro="<%=datos2%>"/>
				<%}%>
			</td>
		</tr>			
		</table>	
		</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.listarTurnos.literal.guardias">

				<table width="80%" border="0" align="center" class="labelText" >
				<td  width="30%" style="text-align:center">
					<% if (accion.equalsIgnoreCase("nuevo")){ %> 
						<INPUT NAME="guardias" TYPE=RADIO VALUE="0" checked> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.obligatorias"/> 
						</td>
						<td  width="30%" style="text-align:center">
						<INPUT NAME="guardias" TYPE=RADIO VALUE="1" > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.todasNinguna"/>
						</td>
						<td  width="30%" style="text-align:center">
						<INPUT NAME="guardias" TYPE=RADIO VALUE="2" > <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.elegir"/>
					<%} else {%>
						<INPUT NAME="guardias" TYPE=RADIO VALUE="0" <% if((((String)turno.get("GUARDIAS")).equalsIgnoreCase("0"))||(accion.equalsIgnoreCase("nuevo"))){ %> checked <% } %> <%=valida2%>> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.obligatorias"/> 
						</td>
						<td  width="30%" style="text-align:center">
						<INPUT NAME="guardias" TYPE=RADIO VALUE="1" <% if(((String)turno.get("GUARDIAS")).equalsIgnoreCase("1")){ %> checked <% } %> <%=valida2%>> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.todasNinguna"/>
						</td>
						<td  width="30%" style="text-align:center">
						<INPUT NAME="guardias" TYPE=RADIO VALUE="2" <% if(((String)turno.get("GUARDIAS")).equalsIgnoreCase("2")){ %> checked <% } %> <%=valida2%>> <siga:Idioma key="gratuita.maestroTurnos.literal.guardias.elegir"/>
					<% } %>
				</td>
			</table>
			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.configuracion">
			<table align="center" width="100%">
				<tr>
					<td class="labelText" style="text-align:left">
						<INPUT NAME="validarJustificaciones" TYPE=CHECKBOX <% if ((!accion.equalsIgnoreCase("nuevo"))&&(((String)turno.get("VALIDARJUSTIFICACIONES")).equalsIgnoreCase("S"))){%> checked<%}%> <%=valida2%>>
						<siga:Idioma key="gratuita.maestroTurnos.literal.validarJustificaciones"/>
					</td>
					<td class="labelText" style="text-align:left" >
						<INPUT NAME="validacionInscripcion" TYPE=CHECKBOX   <% if ((!accion.equalsIgnoreCase("nuevo"))&&(((String)turno.get("VALIDARINSCRIPCIONES")).equalsIgnoreCase("S"))){%> checked<%}%> <%=valida2%>>
						<siga:Idioma key="gratuita.maestroTurnos.literal.validarInscripciones"/> 
					</td>
					<td class="labelText" >
						<input type="checkbox" name="activarRestriccionActuacion" value="1" <% if (UtilidadesString.stringToBoolean((String)turno.get("ACTIVARRETRICCIONACREDIT"))) { %> checked <% } %> <%=valida2%> > 
						<siga:Idioma key="gratuita.maestroTurnos.literal.activarRestriccion"/>
					</td>
				</tr>
				<tr>
					<td class="labelText" >
						<input type="checkbox" name="activarAsistenciasLetrado" value="1" <% if (UtilidadesString.stringToBoolean((String)turno.get("LETRADOASISTENCIAS"))) { %> checked <% } %> <%=valida2%> > 
						<siga:Idioma key="gratuita.maestroTurnos.literal.aniadirAsistenciasLetrado"/>
					</td>
					<td class="labelText" >
						<input type="checkbox" name="activarActuacionesLetrado" value="1" <% if (UtilidadesString.stringToBoolean((String)turno.get("LETRADOACTUACIONES"))) { %> checked <% } %> <%=valida2%> > 
						<siga:Idioma key="gratuita.maestroTurnos.literal.aniadirActuacionesLetrado"/>
					</td>
				</tr>
				
			</table>
			</siga:ConjCampos>
			<siga:ConjCampos leyenda="gratuita.maestroTurnos.literal.pesosOrdenacion">
			<table border="0" width="100%" align="center">
				<tr>
					<td class="labelText" style="text-align:left"><siga:Idioma key="gratuita.maestroTurnos.literal.primerCriterio"/></td>
					<td style="text-align:right">
					<% if (!accion.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_1">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_1">
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
					<td style="text-align:right">
					<% if (!accion.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_2">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_2">
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
					<td style="text-align:right">
					<% if (!accion.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_3">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_3">
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
					<td style="text-align:right">
					<% if (!accion.equalsIgnoreCase("ver")) {%>
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="crit_4">
							<html:option value="0"><siga:Idioma key="gratuita.maestroTurnos.literal.sinDefinir"/></html:option>
							<html:option value="1"><siga:Idioma key="gratuita.maestroTurnos.literal.alfabetico"/></html:option>
							<html:option value="2"><siga:Idioma key="gratuita.maestroTurnos.literal.antiguedad"/></html:option>
							<html:option value="3"><siga:Idioma key="gratuita.maestroTurnos.literal.edad"/></html:option>
							<html:option value="4"><siga:Idioma key="gratuita.maestroTurnos.literal.cola"/></html:option>
						</html:select>
						&nbsp;
						<html:select styleClass="<%=classCombo %>" name="DefinirTurnosForm" property="ord_4">
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
			
	</html:form>
	<%
	if (!accion.equalsIgnoreCase("ver"))
	{%>
			<siga:ConjBotonesAccion botones="G,R,V" clase="botonesDetalle"/>
	<%}
	else
	{ 
		if(campos!=null && campos.size() == 7)
		{
		%>
				<siga:ConjBotonesAccion botones="S" clase="botonesDetalle"  />
		<%
		}else{
			%>
					<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		<%}
	}%>

<!-- formulario de partidos judiciales -->
<form name="partidosJud" action="<%=app%>/html/jsp/gratuita/partidosJudiciales.jsp" target="partidosjudiciales" method="POST">
	<input type=hidden name="idinstitucion">
	<input type=hidden name="idzona">
	<input type=hidden name="idsubzona">
	
	
</form>

</body>


<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</html>
	
	