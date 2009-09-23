<!-- inicioDelitosEJG.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri ="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"  prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"   prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoEJGForm"%>
<%@ page import="com.siga.gratuita.form.ContrariosEjgForm"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsDelitoBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String modopestanha = request.getSession().getAttribute("accion")==null?"":(String)request.getSession().getAttribute("accion");
	
	ArrayList estadoSel    = new ArrayList();
	ArrayList juzgadoSel   = new ArrayList();
	ArrayList comisariaSel = new ArrayList();
	ArrayList pretensionesSel = new ArrayList();

	//ScsEJGAdm adm = new ScsEJGAdm (usr);
	
	String OBSERVACIONES = "", DELITOS = "", PROCURADOR = "", PROCURADORNECESARIO ="",idProcurador="", idInstitucionProcurador="", calidad="", FECHAPROCURADOR="",  
		   procuradorNombreCompleto = "", procuradorNumColegiado = "", procuradorSel = "",idTurno = "", nombreCompleto="";	
   	String numeroDiligenciaAsi    = "",numeroProcedimientoAsi = "", juzgadoAsi="", juzgadoInstitucionAsi="", comisariaAsi="", comisariaInstitucionAsi="";
   	String idPretension    = "", idPretensionInstitucion="",pretension="";
   	
	Hashtable hash = (Hashtable)ses.getAttribute("DATABACKUP");
	
	String anio = hash.get("ANIO").toString();
	String numero = hash.get("NUMERO").toString();
	String idTipoEJG = hash.get("IDTIPOEJG").toString();
	
	//Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");

	try {
		if (hash.containsKey("PROCURADORNECESARIO")) PROCURADORNECESARIO					  			=  hash.get("PROCURADORNECESARIO").toString();

		if (hash.containsKey("IDPRETENSION")) idPretension			  				=  hash.get("IDPRETENSION").toString(); 		

		if (hash.containsKey("IDTURNO")) idTurno					  			=  hash.get("IDTURNO").toString(); 		
		if (hash.containsKey("CALIDAD")) calidad					  			=  hash.get("CALIDAD").toString();
		if (hash.containsKey(ScsEJGBean.C_NUMERODILIGENCIA)) numeroDiligenciaAsi					  			=  hash.get(ScsEJGBean.C_NUMERODILIGENCIA).toString();
		if (hash.containsKey(ScsEJGBean.C_NUMEROPROCEDIMIENTO)) numeroProcedimientoAsi					  			=  hash.get(ScsEJGBean.C_NUMEROPROCEDIMIENTO).toString();
	
	// Datos pretensiones seleccionado:
		if (hash.containsKey(ScsEJGBean.C_IDPRETENSION)) idPretension					  		=  hash.get(ScsEJGBean.C_IDPRETENSION).toString();
		if (hash.containsKey(ScsEJGBean.C_IDPRETENSIONINSTITUCION)) idPretensionInstitucion		=  hash.get(ScsEJGBean.C_IDPRETENSIONINSTITUCION).toString();

	// Datos del Juzgado seleccionado:
		if (hash.containsKey(ScsEJGBean.C_JUZGADO)) juzgadoAsi					  			=  hash.get(ScsEJGBean.C_JUZGADO).toString();
		if (hash.containsKey(ScsEJGBean.C_JUZGADOIDINSTITUCION)) juzgadoInstitucionAsi		=  hash.get(ScsEJGBean.C_JUZGADOIDINSTITUCION).toString();
 	
 	// Datos de la comisaria seleccionado:
		if (hash.containsKey(ScsEJGBean.C_COMISARIA)) comisariaAsi					  			=  hash.get(ScsEJGBean.C_COMISARIA).toString();
		if (hash.containsKey(ScsEJGBean.C_COMISARIAIDINSTITUCION)) comisariaInstitucionAsi 		=  hash.get(ScsEJGBean.C_COMISARIAIDINSTITUCION).toString();
		
 		if (hash.containsKey("PROCURADOR")) PROCURADOR 					  =  hash.get("PROCURADOR").toString();

	 	if (hash.containsKey("OBSERVACIONES")) OBSERVACIONES =  hash.get("OBSERVACIONES").toString();
	 	if (hash.containsKey("DELITOS")) DELITOS =  hash.get("DELITOS").toString();
	 	if (hash.containsKey("PROCURADOR")) PROCURADOR =  hash.get("PROCURADOR").toString();
	 	
	 	// Datos del procurador seleccionado:
	 	if (hash.containsKey("IDPROCURADOR")) idProcurador =  hash.get("IDPROCURADOR").toString();
	 	if (hash.containsKey("IDINSTITUCION_PROC")) idInstitucionProcurador =  hash.get("IDINSTITUCION_PROC").toString();
		if (hash.containsKey("IDPROCURADOR") && hash.containsKey("IDINSTITUCION_PROC"));
			procuradorSel = idProcurador + "," + idInstitucionProcurador;
		if (hash.containsKey("PROCURADOR_NUM_COLEGIADO")&& hash.get("PROCURADOR_NUM_COLEGIADO")!=null)
			procuradorNumColegiado   = (String)hash.get("PROCURADOR_NUM_COLEGIADO");
		if (hash.containsKey("PROCURADOR_NOMBRE_COMPLETO") && hash.get("PROCURADOR_NOMBRE_COMPLETO")!=null)
			procuradorNombreCompleto = (String)hash.get("PROCURADOR_NOMBRE_COMPLETO");
			
		if (hash.containsKey("FECHA_DES_PROC")) FECHAPROCURADOR =  GstDate.getFormatedDateShort("",hash.get("FECHA_DES_PROC").toString());	
	}
  catch (Exception e) {};

	String[] parametroJuzgado = {usr.getLocation(), idTurno};

	if (idPretension!=null && idPretensionInstitucion!=null)
		pretensionesSel.add(0,idPretension+","+idPretensionInstitucion);	

	if (juzgadoAsi!=null && juzgadoInstitucionAsi!=null)
		juzgadoSel.add(0,juzgadoAsi+","+juzgadoInstitucionAsi);	
	
	if (comisariaAsi!=null && comisariaInstitucionAsi!=null)
		comisariaSel.add(0,comisariaAsi+","+comisariaInstitucionAsi);

	String estilo = "box", readOnly="false", estiloCombo="boxCombo";
	String[] datos={usr.getLocation(),idTurno};		
	String[] datos2={usr.getLocation(),usr.getLanguage()};	
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<html>

<!-- HEAD -->
<head>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	


	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="gratuita.EJG.delitosFaltas" 
		localizacion="gratuita.busquedaEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onload="refrescarLocal()">
	
		<table class="tablaTitulo" cellspacing="0" heigth="38">

	<html:form action = "/JGR_DelitosEJG.do" method="POST" target="resultado" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>			
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoEJGForm" property="anio" />
		<html:hidden name="pestanaDelitoEJGForm" property="numero" />
		<html:hidden name="pestanaDelitoEJGForm" property="idTipoEJG" />
	</html:form>
	<html:form action = "/JGR_MantenimientoEJG.do" method="POST" target="submitArea"  style="display:none">
		<input type="hidden" name="modo"        value="buscarJuzgado"> 
		<html:hidden property = "idTurnoEJG" value = "<%=idTurno%>"/>
		<html:hidden property = "idTipoEJG" value = "<%=idTipoEJG%>"/>		
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "numero" value = "<%=numero%>"/>
		<html:hidden property = "anio" value = "<%=anio%>"/>		
		<html:hidden property = "procurador" value="<%=procuradorSel%>"/>
		<html:hidden property = "calidad" value="<%=calidad%>"/>
		<html:hidden property = "observaciones" value=""/>
		<html:hidden property = "comisaria" value="<%=OBSERVACIONES%>"/>
		<html:hidden property = "juzgado" value=""/>
		<html:hidden property = "numeroDilegencia" value="<%=numeroDiligenciaAsi%>"/>
		<html:hidden property = "numeroProcedimiento" value="<%=numeroProcedimientoAsi%>"/>		
		<html:hidden property = "delitos" value="<%=DELITOS%>"/>
		<html:hidden property = "pretension" value=""/>
		<html:hidden property = "fechaProc" value=""/>
	</html:form>
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type=""  style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrirBusquedaProcuradorModal">
	</html:form>	
	<html:form action = "/JGR_ContrariosEjg.do" method="POST" target="resultado1" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>			
		<!-- Datos de la pestanha -->
		<html:hidden name="ContrariosEjgForm" property="anio" value = "<%=anio%>" />
		<html:hidden name="ContrariosEjgForm" property="numero" value = "<%=numero%>" />
		<html:hidden name="ContrariosEjgForm" property="idTipoEJG" value = "<%=idTipoEJG%>"/>
	</html:form>
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	<html:form action = "/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarComisaria">
		<html:hidden property = "codigoExtBusqueda" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>	

		<tr>
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
			<td id="titulo" class="titulitosDatos">
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
		</table>	
	<table class="tablaCentralCampos" align="center">
		  <tr><td>
			<siga:ConjCampos leyenda="pestana.justiciagratuitadesigna.defensajuridica">
				<table width="100%" style="table-layout:fixed" border=0>
					<tr>
						<td colspan="3" class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligenciasolo'/></td>
							<td colspan="2" width="100">
							<%if(modopestanha.equals("editar")){%>
							<input name="numeroDilegencia2" size="10" maxlength="20" type="text" value="<%=numeroDiligenciaAsi%>" class="<%=estilo%>" />
							<%}else{%>
							<input name="numeroDilegencia2" size="10" maxlength="20" type="text" value="<%=numeroDiligenciaAsi%>" class="boxConsulta" />
							<%}%>								
						</td> 
						<td colspan="3" class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.c.Detencion'/></td>
							<td colspan="15">
							<%if(modopestanha.equals("editar")){%>
							 	<input type="text" name="codigoExtComisaria" class="box" size="3"  style="margin-top:3px;" maxlength="10"/>
								<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" ancho="585" obligatorio="false" parametro="<%=datos%>" elementoSel="<%=comisariaSel%>" clase="<%=estilo%>" hijo="t" readonly="false"/>
							<%}else{%>
									<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" ancho="625" obligatorio="false" parametro="<%=datos%>" elementoSel="<%=comisariaSel%>" clase="boxConsulta" hijo="t" readonly="true"/>
							<%}%>							
						</td>
					 </tr>
					<tr>
						<td colspan="3" class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroProced'/></td>
						<td colspan="2"> 
							<%if(modopestanha.equals("editar")){%>
							 	<input name="numeroProcedimiento2" size="10" type="text" value="<%=numeroProcedimientoAsi%>" class="<%=estilo%>" maxlength="20"/>
							<%}else{%>
								<input name="numeroProcedimiento2" size="10" type="text" value="<%=numeroProcedimientoAsi%>" class="boxConsulta"/>
							<%}%>						
						</td>
						<td colspan="3" class="labelText"><siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/></td>	 
						<td colspan="15">	
							<%if(modopestanha.equals("editar")){%>
							 	  <input type="text" name="codigoExtJuzgado" class="box" size="3"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />
							 	  <siga:ComboBD nombre="juzgado" tipo="comboJuzgadosTurno" ancho="585" clase="<%=estiloCombo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="false"/>           	   
							<%}else{%>
									<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosTurno" ancho="625" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="true"/>           	   
							<%}%>							
						</td>	
					</tr>
					<tr>
						<td colspan="3" class="labelText">
							<siga:Idioma key='gratuita.operarEJG.literal.observacionesAsunto'/>
						</td>
						<td colspan="8">	
							<%if(modopestanha.equals("editar")){%>
								<html:textarea property="observaciones2" size="7" cols="60" rows="2" style="overflow:auto; width:330px;"  styleClass="box" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" value="<%=OBSERVACIONES%>"></html:textarea>
							<%}else{%>
								<html:textarea property="observaciones2" size="7" cols="60" rows="2" style="overflow:auto width:330px;" styleClass="boxConsulta" value="<%=OBSERVACIONES%>"></html:textarea>
							<%}%>							
						</td>
						<td colspan="3" class="labelText">
							<siga:Idioma key='gratuita.general.literal.comentariosDelitos'/>
						</td>
						<td colspan="9">	
							<%if(modopestanha.equals("editar")){%>
									<html:textarea name="DefinirMantenimientoEJGForm" size="7" property="delitos2" cols="60" rows="2" style="overflow:auto; width:370px;" styleClass="box" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" value="<%=DELITOS%>"></html:textarea>							
							<%}else{%>
								<html:textarea  name="DefinirMantenimientoEJGForm" size="7" property="delitos2" cols="60" rows="2" style="overflow:auto; width:370px;" styleClass="boxConsulta" value="<%=DELITOS%>"></html:textarea>							
							<%}%>							
						</td>		
					</tr>			
					<tr>
						<td colspan="3" class="labelText">	
							<siga:Idioma key='gratuita.personaJG.literal.calidad'/>
						</td>
							<td colspan="8">			
							<%if(modopestanha.equals("editar")){%>
								<html:select styleClass="boxCombo" property="calidad2" style="width=150" value="<%=calidad %>"readOnly="false">
									<html:option value="D"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandante"/></html:option>
									<html:option value="O"><siga:Idioma key="gratuita.personaJG.calidad.literal.demandado"/></html:option>
								</html:select>
							<%}else{
								if(calidad.equals("D")){	%>
									<html:textarea name="DefinirMantenimientoEJGForm" property="calidad2" cols="60" rows="1" style="overflow:auto" styleClass="boxConsulta" value="Demandante"></html:textarea>							
								<%}else{%>
									<html:textarea name="DefinirMantenimientoEJGForm" property="calidad2" cols="60" rows="1" style="overflow:auto" styleClass="boxConsulta" value="Demandado"></html:textarea>							
							<%  }
							}%>						
						
						</td>	
						<td colspan="3" class="labelText">	
							<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
						</td>	
						<td  colspan="9">
							<%if(modopestanha.equals("editar")){%>
								<siga:ComboBD nombre="pretensiones2" tipo="comboPretensiones" ancho="370" clase="<%=estiloCombo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="false"/>           	   
							<%}else{%>
								<siga:ComboBD nombre="pretensiones2" tipo="comboPretensiones" ancho="370" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
							<%}%>
						</td>
					</tr>
					<tr>
						<td colspan="23"> 
							<siga:ConjCampos leyenda="gratuita.datosProcurador.literal.procurador">
								<table  width="100%" border="0">
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
										</td>
										<td >
											<html:hidden name = "DefinirMantenimientoEJGForm" property = "procurador" value="<%=procuradorSel%>"/>
											<input type="text" name="nColegiadoProcurador" id="nColegiadoProcurador" size="5" maxlength="100" class="boxConsulta" readOnly="true" value="<%=procuradorNumColegiado%>"/>
										</td>
										<td  class="labelText" >
											<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
										</td>
										<td >
											<input type="text" name="nombreCompleto" id="nombreCompleto" size="40" maxlength="100" class="boxConsulta" readOnly="true" value="<%=procuradorNombreCompleto%>"/>
										</td>
										<td class="labelText">
				                             <siga:Idioma key='gratuita.operarEJG.literal.fechaDesigProc'/>
			                            </td>
			                            <td>	
			                             <%   if (modopestanha.equals("ver")) {%>
				                             <input type="text" class="boxConsulta" value="<%=FECHAPROCURADOR%>" readOnly="true">
			                             <%	} else { %>

											  <input type="text" name="fechaProc1" class="box" size="10" value="<%=FECHAPROCURADOR%>" readOnly="true">&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaProc1);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
			                             <%}%>
			                            </td>
										<td>
											<%if(modopestanha.equals("editar")){%>
												<html:button property='idButton' onclick="return buscarProcurador();" styleClass="button"><siga:Idioma key="general.boton.search"/></html:button>
				 
												
												
											<%}%>
										</td>
										<td >
											<%if(modopestanha.equals("editar")){%>
												
												<html:button property='idButton' onclick="return limpiarProcurador();" styleClass="button"><siga:Idioma key="general.boton.clear"/></html:button> 
												
												
											<%}%>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>				
						</td>			
					</tr>
				</table>
			</siga:ConjCampos>
		  </td></tr>
		</table>

	<%if(modopestanha.equals("editar")){%>
		<siga:ConjBotonesAccion botones="G"  modo="editar" clase="botonesSeguido"/> 
	<%}else{%>
		<siga:ConjBotonesAccion botones=""  modo="editar" clase="botonesSeguido"/> 
	<%}%>


	<!-- INICIO: IFRAME LISTA RESULTADOS -->
<table border="0" width="100%"  height="52%">
<tr >

<td width="50%" > 


<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral"
					style="width:100%; height:100%;"	>
	</iframe>

</td>
<td width="50%" >
<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado1"
					name="resultado1" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral"
					style="width:100%; height:100%;">
	</iframe>
</td>		
</tr>
</table>
	
	
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<siga:ConjBotonesAccion botones="V"  clase="botonesDetalle" modo="<%=modopestanha%>"/>	
		
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function refrescarLocal() 
		{ 
			document.forms[0].target = 'resultado';		
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
				
		    document.forms[3].target = 'resultado1';		
			document.forms[3].modo.value = "";
			document.forms[3].submit();
       }
		<!-- Funcion asociada a boton Nuevo -->
		/*function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}*/
		
		<!-- Asociada al boton Volver -->
		function accionVolver()
		{
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		function accionGuardar(){
			sub();
			var observaciones = document.getElementById("observaciones").value;
			if (observaciones.length <= 1024) {
				document.DefinirMantenimientoEJGForm.modo.value = "modificarDefensa";
				document.DefinirMantenimientoEJGForm.target = "submitArea";
				
				document.DefinirMantenimientoEJGForm.procurador.value				=	document.getElementById("procurador").value	;
				document.DefinirMantenimientoEJGForm.calidad.value					=	document.getElementById("calidad2").value	;
				document.DefinirMantenimientoEJGForm.comisaria.value				=	document.getElementById("comisaria").value	;
				document.DefinirMantenimientoEJGForm.juzgado.value					=	document.getElementById("juzgado").value	;				
				document.DefinirMantenimientoEJGForm.numeroDilegencia.value			=	document.getElementById("numeroDilegencia2").value	;
				document.DefinirMantenimientoEJGForm.numeroProcedimiento.value		=	document.getElementById("numeroProcedimiento2").value;					
				document.DefinirMantenimientoEJGForm.observaciones.value			=	document.getElementById("observaciones2").value;
				document.DefinirMantenimientoEJGForm.delitos.value					=	document.getElementById("delitos2").value;
				document.DefinirMantenimientoEJGForm.pretension.value				=	document.getElementById("pretensiones2").value;		
				document.DefinirMantenimientoEJGForm.fechaProc.value				=	document.getElementById("fechaProc1").value;				

//				alert("observaciones->"+document.DefinirMantenimientoEJGForm.observaciones.value+"<observaciones2->"+document.getElementById("observaciones").value);							
//				alert("Procedimiento->"+document.DefinirMantenimientoEJGForm.numeroProcedimiento.value+"<Procedimiento2->"+document.getElementById("numeroProcedimiento").value);											
//				alert("Diligencia->"+document.DefinirMantenimientoEJGForm.numeroDilegencia.value+"<Diligencia->"+document.getElementById("numeroDilegencia").value);											
//				alert("procurador->"+document.DefinirMantenimientoEJGForm.procurador.value+"<procurador2->"+document.getElementById("procurador").value);											
//				alert("Pretensiones->"+document.DefinirMantenimientoEJGForm.pretensiones.value+"<pretensiones22->"+document.getElementById("pretensiones2").value);											
               
                if (document.DefinirMantenimientoEJGForm.fechaProc.value!="" &&	document.getElementById("nombreCompleto").value==""){
				 alert('<siga:Idioma key="gratuita.operarEJG.message.fechaDesigProc"/>');
				 fin();
				 return false;
				}
				document.DefinirMantenimientoEJGForm.submit();
			}
			else  {
				alert('<siga:Idioma key="gratuita.operarEJG.message.lontigudObservaciones"/>');	
				fin();
				return false;
			}
		}
		function limpiarProcurador()
		{
			document.getElementById("nombreCompleto").value = '';
			document.getElementById("nColegiadoProcurador").value     = '';
			document.getElementById("procurador").value = '';
			document.getElementById("fechaProc1").value = '';
			
			
				
		
		
		}	
		function buscarProcurador() 
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			if((resultado != undefined) && (resultado[0] != undefined) && (resultado[1] != undefined)) {
				document.all.DefinirMantenimientoEJGForm.procurador.value     = resultado[0];
				document.getElementById("nombreCompleto").value = resultado[1];
				document.getElementById("nColegiadoProcurador").value     = resultado[2];
				//alert ("Procurador: " + document.all.DefinirMantenimientoEJGForm.procurador.value + ", Nombre: " + document.all.DefinirMantenimientoEJGForm.nombreCompleto.value + ", Ncolegiado: " + document.all.DefinirMantenimientoEJGForm.nColegiadoProcurador.value);
			}	
		}
		
		 function obtenerJuzgado() 
			{ 
			  if (document.getElementById("codigoExtJuzgado").value!=""){
/*				if(document.forms[2].identificador.selectedIndex <= 0 ){
					alert("<siga:Idioma key='gratuita.nuevaAsistencia.mensaje.alert1'/>");
					return;
				}else{	*/
				 document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="DOSFRAMES";	
				   document.MantenimientoJuzgadoForm.codigoExt.value=document.getElementById("codigoExtJuzgado").value;
				   document.MantenimientoJuzgadoForm.submit();		
				   				   
				   
				//}
			 }
			}
	
	    function obtenerComisaria() 
		 { 
			  if (document.getElementById("codigoExtComisaria").value!=""){
				   document.MantenimientoComisariaForm.nombreObjetoDestino.value="DOSFRAMES";	
				   document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.getElementById("codigoExtComisaria").value;
				   document.MantenimientoComisariaForm.submit();	
				  
				}
			  
		 }
		function traspasoDatos(resultado){
		  seleccionComboSiga("juzgado",resultado[0]);
		}	
		function traspasoDatosComisaria(resultado){
		 seleccionComboSiga("comisaria",resultado[0]);
		}		
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->
	

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>