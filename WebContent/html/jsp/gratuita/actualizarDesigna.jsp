<!DOCTYPE html>
<%@page import="java.util.ArrayList"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>
<html>
<head>
<!-- actualizarDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<!-- IMPORTS -->

<%@ page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.atos.utils.ClsConstants"%>
<%@page import="com.atos.utils.UsrBean"%>
<%@page import="com.siga.beans.ScsEJGBean"%>
<%@page import="com.siga.gratuita.form.MaestroDesignasForm"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();

	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String dato[] = { (String) usrbean.getLocation() };
%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<script>

	function cambiarJuzgado() {
		var combo = document.getElementById("idJuzgado").value;
		
		if(combo!="-1"){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
						type: "POST",
				url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado3",
				data: "idCombo="+combo,
				dataType: "json",
				success: function(json){		
	    	   		document.getElementById("codigoExtJuzgado").value = json.codigoExt2;      		
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
		else
			document.getElementById("codigoExtJuzgado").value = "";
	}		
	
	function obtenerJuzgado() { 
		var codigo = document.getElementById("codigoExtJuzgado").value;
		
		if(codigo!=""){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
						type: "POST",
				url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado4",
				data: "codigo="+codigo,
				dataType: "json",
				success: function(json){		
					if (json.idJuzgado=="") {
						document.getElementById("idJuzgado").value = "-1";
						document.getElementById("codigoExtJuzgado").value = "";
					} else {
	    	   			document.getElementById("idJuzgado").value = json.idJuzgado;
	    	   			if (document.getElementById("idJuzgado").value=="") {
							document.getElementById("idJuzgado").value = "-1";
							document.getElementById("codigoExtJuzgado").value = "";
						}
					}
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
		else
			document.getElementById("idJuzgado").value = "-1";
	}	
				
	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}		
	
	function formateaNumProcedimiento(valueNumProcedimiento,valueEjisActivo,objectConsejo){
		if(objectConsejo && objectConsejo.value==IDINSTITUCION_CONSEJO_ANDALUZ){
			var numProcedimientoArray = valueNumProcedimiento.split('.');
			numProcedimiento = numProcedimientoArray[0];
			if(numProcedimiento && numProcedimiento!=''){
				numProcedimiento = pad(numProcedimiento,5,false);
				finNumProcedimiento = numProcedimientoArray[1]; 
				if(finNumProcedimiento){
					numProcedimiento = numProcedimiento+"."+pad(finNumProcedimiento,2,false);
				}
				document.getElementById("numeroProcedimiento").value = numProcedimiento;
			}
			
		}else if(valueEjisActivo=='1'){
			if(valueNumProcedimiento!=''){
				numProcedimiento = pad(valueNumProcedimiento,7,false);
				document.getElementById("numeroProcedimiento").value = numProcedimiento;
			}
		
		}
	}
	function accionGuardar() 
	{
		sub();
		var nigAux = document.getElementById("nig").value;
		nigAux = formateaNig(nigAux);
		valueNumProcedimiento = document.getElementById("numeroProcedimiento").value;
		objectConsejo = document.getElementById("idConsejo");
		valueEjisActivo = document.getElementById("ejisActivo").value;
		error = validarFormatosNigNumProc(nigAux,valueNumProcedimiento,document.getElementById("anioProcedimiento"),valueEjisActivo,objectConsejo);
		if(document.getElementById("anioProcedimiento")){
			if(valueNumProcedimiento!='' && document.getElementById("anioProcedimiento").value ==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.anio' />"+"\n";
				
			}
			if(valueNumProcedimiento=='' && document.getElementById("anioProcedimiento").value !=''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.informeJustificacionMasiva.literal.numeroProcedimiento' />"+"\n";
			}
		}
		if(error!=''){
			fin();
			alert(error);
			return false;
		}
		
		formateaNumProcedimiento(valueNumProcedimiento,valueEjisActivo,objectConsejo);
		
		document.MaestroDesignasForm.nig.value = nigAux; 
		document.MaestroDesignasForm.modo.value = 'actualizaDesigna';
		document.MaestroDesignasForm.submit();
	}
	function postAccionCodigoExtJuzgado(){
		if(document.getElementById('idJuzgado').value!=''){
			document.getElementById('idJuzgado').onchange();
		}else{
			// juzgados
			document.getElementById("juzgados").selectedIndex = '0';
			document.getElementById('idJuzgado').onchange();
		}
	}	
	
	function onload(){
		if(document.MaestroDesignasForm.idJuzgado.value!=''){
			document.getElementById('idJuzgado').onchange();
		}
	}
	
	function postAccionJuzgados(){
		cambiarJuzgado();
		
		var idProcedimiento = document.MaestroDesignasForm.procedimiento.value;
		var optionsProcedimientos = document.getElementById("modulos");
		var encontrado;
		// alert("option:"+optionsProcedimientos.length);
		for(var i = 0 ; i <optionsProcedimientos.length ; i++) {
			var option = optionsProcedimientos.options[i].value;
			if(option == idProcedimiento){
				encontrado=i;
				break;
			}
		}
		if (encontrado){
			optionsProcedimientos.selectedIndex=encontrado;
		} else {
			optionsProcedimientos.selectedIndex=0;
		}
	}
	
	
	function pulsa(tabCount){
		
		if(!tabCount){
			tabCount = "0";
		}
		objLink = jQuery("#tab_"+tabCount);
		psts	= document.getElementsByName('linkTabs');
		// eliminamos las clases de las pestañas
		for( i=0; i< psts.length; i++)
		{
			jQuery(psts[i]).removeClass('here');
			
		}
		objLink.addClass('here');
		
		//Ahora las pestañas
		pnl 	= document.getElementById('panel_'+tabCount);
		pnls	= document.getElementById('paneles').getElementsByTagName('div');
		for(j=0; j< pnls.length; j++)
		{
			pnls[j].style.display = 'none';
		}
		
		// Añadimos la clase "actual" a la pestaña activa
		pnl.style.display = 'block';
		
	}
	
	
	
	jQuery(function($){
		var defaultValue = jQuery("#nig").val();
		if(defaultValue.length > 19){
			jQuery('#info').show();
			jQuery('#imagenInfo').attr('title',defaultValue) ;
		}else{
			jQuery('#info').hide();
			
		}
		jQuery("#nig").mask("AAAAA AA A AAAA AAAAAAA");
		jQuery("#nig").keyup();	
		if(document.getElementById("idConsejo") && document.getElementById("idConsejo").value==IDINSTITUCION_CONSEJO_ANDALUZ){
			jQuery("#numeroProcedimiento").mask("99999.99");
			jQuery("#numeroProcedimiento").keyup();	
		}else if(document.getElementById("ejisActivo").value=='1'){
			jQuery("#numeroProcedimiento").mask("9999999");
			jQuery("#numeroProcedimiento").keyup();
			
		}
	});	
</script>
</head>

<body onload="onload();">

<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.actuacionesDesigna.literal.actualizarDesigna" />
		</td>
	</tr>
</table>


<!-- INICIO: CAMPOS DE BUSQUEDA-->

<bean:define id="userBean" name="USRBEAN"  scope="session" />
<c:set var="IDINSTITUCION_CONSEJO_ANDALUZ" value="<%=AppConstants.IDINSTITUCION_CONSEJO_ANDALUZ%>" />
<input type="hidden" id ="idConsejo" value = "${userBean.idConsejo}"/>
<input type="hidden" id ="ejisActivo" value = "${EJIS_ACTIVO}"/>
<html:form action="/JGR_ActualizarInformeJustificacion" method="POST" target="submitArea">
	<html:hidden name="MaestroDesignasForm" property="modo" value="" />
	<html:hidden name="MaestroDesignasForm" property="anio"  />
	<html:hidden name="MaestroDesignasForm" property="numero" />
	<html:hidden name="MaestroDesignasForm" property="fecha" />
	<html:hidden name="MaestroDesignasForm" property="idTurno"/>
	<html:hidden name="MaestroDesignasForm" property="procedimiento"/>
	<html:hidden name="MaestroDesignasForm" property="fichaColegial"/>
	<html:hidden name="MaestroDesignasForm" property="idLetradoDesignado"/>
	<html:hidden name="MaestroDesignasForm" property="idPretension"/>
	
	
	<table class="tablaCentralCampos" height="420" align="center">

		<tr>
			<td valign="top"><siga:ConjCampos
				leyenda="gratuita.busquedaDesignas.literal.turno">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">
					<tr>
						<td width="15%" ></td>
						<td  width="35%"></td>
						<td  width="15%"></td>
						<td  width="35%"></td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma key="facturacion.ano" />/<siga:Idioma key="gratuita.busquedaDesignas.literal.codigo" />
						</td>
						<td class="labelTextValor">
							<c:out value="${MaestroDesignasForm.codigo}"/>
						</td>


						<td class="labelText">
						<siga:Idioma
							key='sjcs.designa.general.letrado' /></td>
						<td>
							<c:out value="${MaestroDesignasForm.letrado}"/>
						</td>
						
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.busquedaSOJ.literal.turno" /></td>
						<td><c:out value="${MaestroDesignasForm.turno}"/></td>
						<td class="labelText"><siga:Idioma
							key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha" />
						</td>
						<!-- JBD 16/2/2009 INC-5682-SIGA -->

						<td><c:out value="${MaestroDesignasForm.fecha}"/>
						</td>

						<!-- JBD 16/2/2009 INC-5682-SIGA -->
					</tr>
				</table>
			</siga:ConjCampos> 
			<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.designa">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">
							<tr>
								<td width="20%" class="labelText"><siga:Idioma
										key='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento' /></td>
								
								<c:choose>
									<c:when	test="${EJIS_ACTIVO=='1'}">
									
										<td width="20%"><html:text
											name="MaestroDesignasForm" property="numeroProcedimiento" styleId="numeroProcedimiento"
											size="7" maxlength="7" styleClass="box" />/<html:text
											name="MaestroDesignasForm" property="anioProcedimiento"
											size="4" maxlength="4" styleClass="box" /></td>
	
									</c:when>
									<c:when	test="${userBean.idConsejo==IDINSTITUCION_CONSEJO_ANDALUZ}">
									
										<td width="20%"><html:text styleId="numeroProcedimiento" 
											name="MaestroDesignasForm" property="numeroProcedimiento"
											size="8" maxlength="8" styleClass="box" />/<html:text
											name="MaestroDesignasForm" property="anioProcedimiento"
											size="4" maxlength="4" styleClass="box" /></td>
	
									</c:when>
									
									<c:otherwise>
										<td width="20%"><html:text name="MaestroDesignasForm"
											property="numeroProcedimiento" maxlength="15" styleClass="box"
											style="width: 100" /></td>
									</c:otherwise>
								</c:choose>

								<td width="10%" class="labelText" width="10%"><siga:Idioma
										key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
								</td>
								<td class="labelText" width="10%"><input type="text"
									name="codigoExtJuzgado" styleId="codigoExtJuzgado" class="box"
									size="8" maxlength="10" onBlur="obtenerJuzgado();" />&nbsp;</td>
								<td width="40%"><html:select styleId="juzgados"
										styleClass="boxCombo" style="width:445px;"
										property="idJuzgado">
										<bean:define id="juzgados" name="MaestroDesignasForm"
											property="juzgados" type="java.util.Collection" />
										<html:optionsCollection name="juzgados" value="idJuzgado"
											label="nombre" />
									</html:select></td>
							</tr>

							<tr>
						<td width="20%"  class="labelText">
							<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo" />
						</td>
						<td colspan="4">
							<html:select styleId="modulos" styleClass="boxCombo" style="width:900px;" property="idProcedimiento" >
								<bean:define id="modulos" name="MaestroDesignasForm" property="modulos" type="java.util.Collection" />
								<html:optionsCollection name="modulos" value="idProcedimiento"	label="nombre" />
							</html:select>
						</td>
					</tr>
					
					<tr>
						
						<td width="20%"  class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/></td>

						<td>
							<html:text name="MaestroDesignasForm" property="nig" styleId="nig"  styleClass="box" style="width:200px" size="19"/>
	
						</td>			
						<td id="info" style="display:none"><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
						</td>
						<td colspan="3">&nbsp;</td>						
					</tr>
					
					<tr><td  colspan="6">&nbsp;</td></tr>
				</table>
			</siga:ConjCampos>
		
 		<siga:ConjCampos leyenda="gratuita.operarEJG.literal.expedienteEJG">
		<div id="panelEJGs" style="display: inline;overflow-x: hidden;">
			 	<bean:define id="ejgs" name="MaestroDesignasForm"	property="ejgs" type="java.util.Collection"/>
	   	<c:choose>
		<c:when test="${empty ejgs}">
			<table class="tablaCampos" align="center" cellpadding="0"
						cellpadding="0" width="100%" border="0">
	
				<tr>
					<td colspan="13" class="titulitos" style="text-align: center"><siga:Idioma
						key="messages.noRecordFound" /></td>
				</tr>
			</table>	
		</c:when>
		
		<c:otherwise>
				<div style="position:relative; left:0px; width:100%; height:30px; top:0px; " id="divid">
					<logic:notEmpty name="MaestroDesignasForm" property="ejgs">
					<table  class="tablaLineaPestanasArriba"  >
						<tr>
						<td></td>
						</tr>
						</table>
						<table id="tabs" class="pest" style="width:100%;border-bottom: 2px;border-bottom-color: black;">
							<tr>
								<logic:iterate name="MaestroDesignasForm" property="ejgs" id="ejg1" indexId="index">
									
										<td class="pestanaTD"   name="pestanas" >
											<a id="tab_${index}" name="linkTabs" href="#" onClick="pulsa('${index}');">
												<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<c:out	value="${ejg1.anio}" />/<c:out	value="${ejg1.numEJG}" />
											</a>
										</td>
								
								</logic:iterate>
								<td width="90%">
								</td>
							</tr>
						</table>
						
						
						
					</logic:notEmpty>
					</div>
						
					<div id="paneles" style="height:280px;overflow-y: scroll; ">
					<logic:notEmpty name="MaestroDesignasForm" property="ejgs">
					<%
						MaestroDesignasForm form = (MaestroDesignasForm) request.getAttribute("MaestroDesignasForm");
											int i = 0;
											List<ScsEJGBean> listadoEjgs = null;
											if (form != null) {
												listadoEjgs = form.getEjgs();
											}
					%>
						<logic:iterate name="MaestroDesignasForm" property="ejgs" id="ejg2" indexId="index2">
							<div id="panel_${index2}"  style="height:400;display: inline;overflow-x: hidden;">
							
								<table class="tablaCampos" align="center" cellpadding="0"
									cellpadding="0" width="100%" border="0" style="height:500;">
								<tr>
										<td colspan="1"   class="labelText" style="width:100px;">	
											<siga:Idioma key='gratuita.operarEJG.literal.interesado'/>
										</td>
										<td colspan="5" class="labelTextValue" style="width:300px;">	
										<c:out		value="${ejg2.tipoLetrado}" />

										</td>
								
								</tr>
								<tr>
									<td class="labelText" style="width:100px;">
										<siga:Idioma key='gratuita.busquedaEJG.literal.fechaApertura'/>
									</td>
									<td  class="labelTextValue" style="width:100px;">
									 	<c:out		value="${ejg2.fechaApertura}" />	
									</td>
									<td class="labelText" style="width:100px;">	
										<siga:Idioma key='gratuita.busquedaEJG.literal.estadoEJG'/>
									</td>
									<td  class="labelTextValue" colspan="3" style="width:200px;">	
											<c:out		value="${ejg2.estadoEjg}" />	
									</td>	
								</tr>							
								<tr>
									<td class="labelText" width="100px">	
										<siga:Idioma key='gratuita.operarEJG.literal.tipo'/>
									</td>
									<td  class="labelTextValue" colspan="5" style="width:300px;">	
										<c:out		value="${ejg2.deTipoEjg}" />	
									</td>
							</tr>
							<tr>
								<td class="labelText" width="15%">
									 <siga:Idioma key='gratuita.busquedaEJG.literal.EJGColegio'/>
								</td>
								<td class="labelTextValue" width="15%">
									<c:out		value="${ejg2.tipoEjgCol}" />	
								</td>
								<td class="labelText" nowrap width="20%">
									<siga:Idioma key='gratuita.operarEJG.literal.fechaPresentacion'/>&nbsp;
								</td>
								<td  class="labelTextValue" width="15%">
									<c:out	value="${ejg2.fechaPresentacion}" />	
								</td>
								<td class="labelText" nowrap width="20%">
												<siga:Idioma key='gratuita.operarEJG.literal.fechaLimitePresentacion'/>
								</td>
								<td  class="labelTextValue" width="15%">
								
								<c:out	value="${ejg2.fechaLimitePresentacion}" />	
								</td>
							</tr>
							<tr>
			
								 <td class="labelText" >	
									<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='gratuita.operarEJG.literal.anio'/> / <siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
								</td>
								<td  class="labelTextValue"  >
								<c:out		value="${ejg2.anioCAJG}" />	/<c:out		value="${ejg2.numeroCAJG}" />
								</td>
								<td class="labelText">	
								<siga:Idioma key='gratuita.operarEJG.literal.origen'/> 

								</td>
								<td  class="labelTextValue">
								
								<c:out		value="${ejg2.descripcionOrigen}" />	
								</td>
								<td class="labelText" >	
								<siga:Idioma key='gratuita.busquedaEJG.dictamen'/>
								</td> 
								<td  class="labelTextValue">
								<c:out		value="${ejg2.descripcionDictamen}" />	
								</td>	
							</td>
							</tr>
								
																
								<tr><td colspan="6">   &nbsp;</td></tr>
								
								</table>
							<%
								if (form != null) {
							%>	
								<table class="tablaCampos" align="center" cellpadding="0"
										cellpadding="0" style="width:100%;" border="0">		
								<tr>
										<td class="labelText" style="width:25%;">
											<siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>
										</td>	
										<td class="labelTextValue"  style="width:25%;">
										<c:choose>
											<c:when test="${ejg2.idTipoRatificacionEJG != null && ejg2.idTipoRatificacionEJG !=''}">
													<%
														ArrayList selTipoRatificacion = new ArrayList();
																							ScsEJGBean ejg = (ScsEJGBean) listadoEjgs.get(i++);
																							selTipoRatificacion.add(ejg.getIdTipoRatificacionEJG() + "," + usrbean.getLocation());
													%>	
												
												<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucionTodos" ancho="200" clase="boxConsulta" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=selTipoRatificacion%>" readonly="true"/>			
											</c:when>
											<c:otherwise>
												-
												<%
												i++;
											%>
											</c:otherwise>
										</c:choose>
										</td>
										<td class="labelText"  style="width:25%;">
											<siga:Idioma key="gratuita.operarRatificacion.literal.fechaResolucionCAJG"/>
										</td>
											
										<td class="labelTextValue"  style="width:25%;">
										<c:choose>
											<c:when test="${ejg2.fechaResolucionCAJG != null && ejg2.fechaResolucionCAJG!=''}">
												<siga:Fecha nombreCampo="fechaResolucionCAJG" valorInicial="${ejg2.fechaResolucionCAJG}" disabled="true" readOnly="true"></siga:Fecha>	
											</c:when>
											<c:otherwise>
												-
											</c:otherwise>
										</c:choose>
										</td>
									</tr>
									<tr><td colspan="6">   &nbsp;</td></tr>
								</table>
								<%
									}
								%>	
								<fieldset>
									<legend>
										<siga:Idioma key="pestana.justiciagratuitadesigna.defensajuridica"/>
									</legend>
									<table class="tablaCampos" align="center" cellpadding="0"
										cellpadding="0" width="100%" border="0">
									
										<tr>
											<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligencia'/></td>
											<td class="labelTextValue">
											<c:out value="${ejg2.numeroDiligencia}" />	
											
											</td> 
											<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.centroDetencion'/></td>
											<td  class="labelTextValue" >	
													<c:out value="${ejg2.descripcionComisaria}" />
											</td>
											
										</tr>
										<tr>
											<td class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/></td>
											<td class="labelTextValue">
											<c:out		value="${ejg2.numeroProcedimiento}" />	
											</td>
											<td class="labelText">	
											 <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
											</td>	 
											<td  class="labelTextValue">	
											<c:out		value="${ejg2.descripcionJuzgado}" />
											</td>	
										</tr>
										<tr>
											<td class="labelText">
												<siga:Idioma key='gratuita.operarEJG.literal.observacionesAsunto'/>
											</td>
											<td  class="labelTextValue">	
											<c:out		value="${ejg2.observaciones}" />
											</td>
											<td class="labelText">
												<siga:Idioma key='gratuita.general.literal.comentariosDelitos'/>
											</td>
											<td   class="labelTextValue">	
											<c:out		value="${ejg2.delitos}" />
											</td>		
										</tr>			
										<tr>
											<td  class="labelText">	
												<siga:Idioma key='gratuita.personaJG.literal.calidad'/>
											</td>		
											
											<td class="labelTextValue">					
													<c:out		value="${ejg2.calidad}" />										
											</td>	
	
											<td class="labelText">	
												<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
											</td>	
											<td class="labelTextValue" >
											<c:out		value="${ejg2.descripcionPretension}" />	
											</td>
										</tr>
										
										</table>
								  </fieldset>
								</div>
							</logic:iterate>
						</logic:notEmpty>	
							
						</div>
						<script type="text/javascript">
							pulsa();
							ajusteAlto('panelEJGs');
						</script>
				  </c:otherwise>
				</c:choose>
				</div>
			</siga:ConjCampos> 
			
			</td>
		</tr>
	</table>
</html:form>

	<ajax:select
		baseUrl="/SIGA/GEN_Juzgados.do?modo=getAjaxModulos"
		source="juzgados" target="modulos" parameters="idJuzgado={idJuzgado},procedimiento={procedimiento},fecha={fecha},fichaColegial={fichaColegial},idPretension={idPretension}"
		postFunction="postAccionJuzgados"
		/>
	
	<html:form action="/JGR_MantenimientoJuzgados" method="POST" target="submitArea">
		<html:hidden property="codigoExt2" value="" />
		<html:hidden property="idJuzgado" value="" />
	</html:form>


<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="C,G" modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->
	<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>

<script>
//a.a();
</script>	
</body>

</html>