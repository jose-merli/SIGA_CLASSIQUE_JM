<!DOCTYPE html>
<%@page import="org.redabogacia.sigaservices.app.util.KeyValue"%>
<%@page import="org.redabogacia.sigaservices.app.autogen.model.ScsTipodictamenejg"%>
<html>
<head>
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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants.PARAMETRO"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	
	Hashtable miHash = (Hashtable)ses.getAttribute("DATABACKUPDICT");
	String accion = (String)ses.getAttribute("accion");
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) accion="ver";
	String modo = (String)request.getAttribute("MODO");
	
	
	String anio= "", numero="", idTipoEJG = "", fechaDictamen = "",dictamen ="",idInstitucion = "",idTipoDictamenEJGSelected = "",idFundamentoCalifSelected = "";

	
	if(miHash!=null){
		if (miHash.get("ANIO")!=null)anio = miHash.get("ANIO").toString();
		if (miHash.get("NUMERO")!=null)	numero = miHash.get("NUMERO").toString();
		if (miHash.get("IDTIPOEJG")!=null)idTipoEJG = miHash.get("IDTIPOEJG").toString();
		if (miHash.get("IDINSTITUCION")!=null)idInstitucion = miHash.get("IDINSTITUCION").toString();
		if (miHash.get("FECHADICTAMEN")!=null)fechaDictamen = GstDate.getFormatedDateShort("",miHash.get("FECHADICTAMEN").toString()).toString();
		if (miHash.get("IDTIPODICTAMENEJG")!=null)idTipoDictamenEJGSelected = miHash.get("IDTIPODICTAMENEJG").toString();
		if (miHash.get("IDFUNDAMENTOCALIF")!=null) idFundamentoCalifSelected = miHash.get("IDFUNDAMENTOCALIF").toString();
		if (miHash.get("DICTAMEN")!=null) dictamen = miHash.get("DICTAMEN").toString();
		
	}
		
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	boolean obligatorioFecha = false;
	boolean obligatorioTipoDictamen = false;
	boolean obligatorioFundamento = false;
	 if (pcajgActivo==2){
		obligatorioFecha = true;
		obligatorioTipoDictamen = true;
		obligatorioFundamento = true;
	}else if (pcajgActivo==3){
		obligatorioFecha = true;
		obligatorioTipoDictamen = true;
		obligatorioFundamento = true;
	}
	String asterisco = "&nbsp;(*)&nbsp;";
	String informeUnico =(String) request.getAttribute("informeUnico");
%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script src="<%=app%>/html/js/SIGA.js?v=${sessionScope.VERSIONJS}" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
	<siga:Titulo titulo="pestana.justiciagratuitaejg.dictamen"
		localizacion="gratuita.busquedaEJG.localizacion" />
</head>
<body>
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">

	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
			<%
				String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG = "";
				ScsEJGAdm adm = new ScsEJGAdm(usr);
				Hashtable hTitulo = adm.getTituloPantallaEJG(idInstitucion,
						anio, numero, idTipoEJG,(String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString()));

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
			 <c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
			- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%>
			<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%></td>
			<td class="titulitosDatos"> 
			<input 	type="button" 
				alt='<siga:Idioma key="gratuita.EJG.InformeCalificacion" />'  
		       	id="idButton"  
		       	onclick="return generarCarta();" 
		       	class="button" 
		       	value='<siga:Idioma key="gratuita.EJG.InformeCalificacion" />'/>
			</td>	
		</tr>
	</table>

	<div id="campos" align="center">
<c:set var="comboDisabled" value="" />
<c:set var="classCombo" value="boxCombo" />
<%if (accion.equalsIgnoreCase("ver")) {%>
<c:set var="comboDisabled" value="disabled" />
<c:set var="classCombo" value="boxComboConsulta" />
<%}%>

	<table align="center" width="100%" height="430"
		class="tablaCentralCampos">
		<tr>
			<td valign="top"><siga:ConjCampos
				leyenda="gratuita.dictamenEJG.literal.datosDictamen">

				<table align="center" width="100%" border="0">

					<html:form action="/JGR_DictamenEJG" method="POST"
						target="submitArea">
						<html:hidden property="modo" value="Modificar" />
						<html:hidden property="idInstitucion"	value="<%=idInstitucion%>" />
						<html:hidden property="idTipoEJG" value="<%=idTipoEJG%>" />
						<html:hidden property="anio" value="<%=anio%>" />
						<html:hidden property="numero" value="<%=numero%>" />
						<html:hidden styleId="jsonVolver" property = "jsonVolver"  />

						<tr>
							<td width="5%"></td>
							<td width="25%"></td>
							<td width="35%"></td>
							<td width="10%"></td>
							<td width="10%"></td>
							<td width="15%"></td>
						</tr>

						<tr>
							<td></td>
							<td class="labelText"><siga:Idioma key="gratuita.busquedaContabilidad.literal.fecha" />
								<%if (obligatorioFecha) {%>
									<%=asterisco%> 
								<%}%>	
							</td>
							<td class="labelText">
								<%if(accion.equalsIgnoreCase("ver")){%> 
									<siga:Fecha nombreCampo="fechaDictamen" readOnly="true" disabled="true"  valorInicial="<%=fechaDictamen%>"></siga:Fecha>
									
								<%}else{%> 
									<siga:Fecha nombreCampo="fechaDictamen" readOnly="true"  valorInicial="<%=fechaDictamen%>"></siga:Fecha>
									
								<%}%>
							</td>
							<td class="labelText"><siga:Idioma key="gratuita.operarDictamen.literal.tipoDictamen" />
								<%if (obligatorioTipoDictamen) {%>
									<%=asterisco%> 
								<%}%>
							</td>
							<td class="labelText" style="vertical-align: rigth">
								 
									<select id="idTipoDictamenEJG" name="idTipoDictamenEJG" ${comboDisabled} onchange="onChangeDictamenEJG(this.value);"  class="${classCombo}">
									<option  value=""></option>
									<% 
									List<KeyValue> dictamenEjgList = (List<KeyValue>) request.getAttribute("dictamenEjgList");
									for (KeyValue scsTipodictamenejg : dictamenEjgList) {
										String dictamenEJGSeleccionado = "";
										if(scsTipodictamenejg.getKey().toString().equals(idTipoDictamenEJGSelected))
											dictamenEJGSeleccionado = "selected";
										%>
										<option <%=dictamenEJGSeleccionado%> value="<%=scsTipodictamenejg.getKey()%>" ><%=scsTipodictamenejg.getValue()%></option>
										
									<%} %>
								</select> 
								
							</td>
							<td></td>
						</tr>
						<tr>
							<td></td>
							<td class="labelText"><siga:Idioma key="gratuita.operarDictamen.literal.fundamentoclf" />
								<%if (obligatorioFecha) {%>
									<%=asterisco%> 
								<%}%>
							</td>
							<td class="labelText" colspan="3">
								<select id="idFundamentoCalif" name="idFundamentoCalif" style="width:815px;" ${comboDisabled}  class="${classCombo}"/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td></td>
							<td class="labelText"><siga:Idioma key="gratuita.operarDictamen.literal.dictamen" /></td>
							<td class="labelText" colspan="4">
							<%if (accion.equalsIgnoreCase("ver")) {%> 
								<textarea name="dictamen"
									class="boxConsulta" style="width: 815px" rows="20"
									readOnly><%=dictamen%></textarea> 
							<%} else {%> 
								<textarea
									name="dictamen" class="box" style="width: 815px" 
									rows="20"><%=dictamen%></textarea>
							<%}%>
							</td>
							<td></td>
						</tr>

					</html:form>
				</table>

			</siga:ConjCampos></td>
		</tr>
	</table>
	</div>
	
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value='EJGCA'/>
	<html:hidden property="enviar" value = "0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
	
	
	<% 
	if(accion!=null && !accion.equalsIgnoreCase("ver")){ %>
	<table align="left">
		<tr>
			<td align="left">
			</td>
		</tr>
	</table>
	<%}%>

				
	<!-- Pintamos el boton de borrar solo cuando lo vamos a permitir -->
	<c:if test="${requestScope.isBorrable==false}">
		<siga:ConjBotonesAccion botones="V,R,G" modo="<%=accion%>"clase="botonesDetalle" />
	</c:if>
	<c:if test="${requestScope.isBorrable==true}">
		<siga:ConjBotonesAccion botones="V,R,G,BD" modo="<%=accion%>"clase="botonesDetalle" />
	 </c:if>
	 
	<!-- INICIO: SCRIPTS BOTONES --> 
	<script
		language="JavaScript">	
	function refrescarLocal()
	{
		document.forms[0].modo.value="abrir";
		document.forms[0].target="mainPestanas";		   	
		document.forms[0].submit();
	}
		//Asociada al boton Restablecer
	function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
				
		//Asociada al boton Volver
		function accionVolver()
		{
			if(document.forms[0].jsonVolver && document.forms[0].jsonVolver.value!=''){
				
				jSonVolverValue = document.forms[0].jsonVolver.value;
				jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
				var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
				nombreFormulario = jSonVolverObject.nombreformulario;
				if(nombreFormulario != ''){
					parent.document.forms[nombreFormulario].idRemesa.value =  jSonVolverObject.idremesa;
					parent.document.forms[nombreFormulario].idinstitucion.value = jSonVolverObject.idinstitucion;
					parent.document.forms[nombreFormulario].modo.value="editar";
					parent.document.forms[nombreFormulario].target = "mainWorkArea";
					parent.document.forms[nombreFormulario].submit();
					
				}
			}else{
				document.forms[0].idInstitucion.value = "<%=usr.getLocation()%>";
				document.forms[0].action="./JGR_EJG.do";	
				document.forms[0].modo.value="buscar";
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit();
			}

		}
		
		//Asociada al boton Cerrar
		function accionGuardar()
		{
			sub();
			<%if (pcajgActivo>0){%>
				var error = "";
		   		if (<%=obligatorioFundamento%> && document.getElementById("idFundamentoCalif").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.operarDictamen.literal.fundamentoclf'/>"+ '\n';
				if (<%=obligatorioTipoDictamen%> && document.getElementById("idTipoDictamenEJG").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.operarDictamen.literal.tipoDictamen'/>"+ '\n';
				if (<%=obligatorioFecha%> && document.getElementById("fechaDictamen").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.busquedaContabilidad.literal.fecha'/>"+ '\n';						
				if(error!=""){
					alert(error);
					fin();
					return false;
				}
		 	<%}%>

			document.forms[0].submit();

		}
		
		function limpiaFormulario(){

			jQuery('input[type=text], textarea, select').val('');
			jQuery('#idFundamentoCalifFrame').contents().find('input[type=text], textarea, select').val('');
		}
		
		function borrarDictamen(){
			sub();
			if(confirm("<siga:Idioma key='gratuita.operarDictamen.confirmBorrarDictamen'/>")){
				limpiaFormulario();
				document.forms[0].submit();
			}else{
				fin();
			}
		}
		
		function generarCarta()
		{
		sub();
	
		var anio  = <%=anio%>;
		var idTipo  = <%=idTipoEJG%>;
		var numero = <%=numero%>;
		
		var datos = "idTipoEJG==" +idTipo+"##anio=="+anio +"##numero==" +numero+"%%%";
		
		
		document.InformesGenericosForm.datosInforme.value=datos;
		
		
			document.InformesGenericosForm.datosInforme.value=datos;
			if(document.getElementById("informeUnico").value=='1'){
				document.InformesGenericosForm.submit();
			}else{
			
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
				  fin(); 		
			   	} 
			   	else {
			   		fin();
			   	}
			}
			

		}
		
		function onChangeDictamenEJG(idTipoDictamenEJG){
			//Limpiamos el combo tratamiento para que no se sumen los resultados
			jQuery("#idFundamentoCalif").html("");
	
			jQuery.ajax({  
	           type: "POST",
	           url: "/SIGA/JGR_DictamenEJG.do?modo=getFundamentosDictamen",
	           data: "idTipoDictamenEJG="+idTipoDictamenEJG,
	           dataType: "json",
	           success:  function(json) {
	        	   jQuery("#idFundamentoCalif").append('<option value="">'+" "+'</option>');
	           
	        		jQuery.each(json, function(index, value) {
	        			seleccionado = '';
       					if('<%=idFundamentoCalifSelected%>'==value.id)
       						seleccionado = 'selected';
	        			
	        			jQuery("#idFundamentoCalif").append('<option '+seleccionado+' value='+value.id+'>'+value.descripcion+'</option>');
	        		   });
	        		
	
	     		
	           },
	           error: function(xml,msg){
	        	   alert("Error: "+msg);
	           }
	        }); 
		}
		onChangeDictamenEJG('<%=idTipoDictamenEJGSelected%>');
		jQuery('#idFundamentoCalif option[value=<%=idFundamentoCalifSelected%>]').attr('selected','selected');
		
		
	</script> <!-- FIN: SCRIPTS BOTONES --> <!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA --> <!-- Obligatoria en todas las páginas--> <iframe
		name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe> <!-- FIN: SUBMIT AREA -->
</body>

</html>