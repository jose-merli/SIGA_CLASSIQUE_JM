<!DOCTYPE html>
<html>
<head>
<!-- inicioDelitosEJG.jsp --> 

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   	prefix="html"%>
<%@ taglib uri = "struts-logic.tld"   	prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoEJGForm"%>
<%@ page import="com.siga.gratuita.form.ContrariosEjgForm"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsDelitoBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants.PARAMETRO"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>
<%@page import="com.siga.ws.CajgConfiguracion"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String profile[]=usr.getProfile();
	

	
	String modopestanha = request.getSession().getAttribute("accion")==null?"":(String)request.getSession().getAttribute("accion");
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) modopestanha="ver";
	
	ArrayList estadoSel    = new ArrayList();
	ArrayList juzgadoSel   = new ArrayList();
	ArrayList comisariaSel = new ArrayList();
	ArrayList pretensionesSel = new ArrayList();
	ArrayList calidadSel = new ArrayList();
	ArrayList preceptivoSel = new ArrayList();
	ArrayList situacionSel = new ArrayList();
	ArrayList renunciaSel = new ArrayList();

	//ScsEJGAdm adm = new ScsEJGAdm (usr);
	
	String OBSERVACIONES = "", DELITOS = "", PROCURADOR = "", PROCURADORNECESARIO ="",idProcurador="", idInstitucionProcurador="", idcalidad="", FECHAPROCURADOR="",  
		   procuradorNombreCompleto = "", procuradorNumColegiado = "", procuradorSel = "",idTurno = "", nombreCompleto="";	
   	String numeroDiligenciaAsi    = "",numeroProcedimientoAsi = "", anioProcedimientoAsi = "", juzgadoAsi="", juzgadoInstitucionAsi="", comisariaAsi="", comisariaInstitucionAsi="";
   	String idPretension    = "", pretension="", idPreceptivo="", idSituacion="", numeroDesignaProc="";
   	
   	String idRenuncia="", nig = "";
   	String calidadidinstitucion="";
	Hashtable hash = (Hashtable)ses.getAttribute("DATABACKUP");
	
	String anio = hash.get("ANIO").toString();
	String numero = hash.get("NUMERO").toString();
	String idTipoEJG = hash.get("IDTIPOEJG").toString();
	String idInstitucion = hash.get("IDINSTITUCION").toString();
	//Hashtable hash = (Hashtable) request.getSession().getAttribute("DATABACKUP");

	try {
		if (hash.containsKey("PROCURADORNECESARIO")) PROCURADORNECESARIO					  			=  hash.get("PROCURADORNECESARIO").toString();

		if (hash.containsKey("IDPRETENSION")) idPretension			  				=  hash.get("IDPRETENSION").toString(); 		
		if (hash.containsKey("IDPRECEPTIVO")) idPreceptivo			  				=  hash.get("IDPRECEPTIVO").toString();
		if (hash.containsKey("IDSITUACION")) idSituacion			  				=  hash.get("IDSITUACION").toString();
		if (hash.containsKey("IDRENUNCIA")) idRenuncia			  				=  hash.get("IDRENUNCIA").toString(); 	
		
		if (hash.containsKey("IDTURNO")) idTurno					  			=  hash.get("IDTURNO").toString(); 		
//		if (hash.containsKey("CALIDAD")) calidad					  			=  hash.get("CALIDAD").toString();
		if (hash.containsKey("IDTIPOENCALIDAD")) idcalidad					  			=  hash.get("IDTIPOENCALIDAD").toString();
		if (hash.containsKey(ScsEJGBean.C_CALIDADIDINSTITUCION)) calidadidinstitucion	=  hash.get(ScsEJGBean.C_CALIDADIDINSTITUCION).toString();
		if (hash.containsKey(ScsEJGBean.C_NIG)) nig	=  									hash.get(ScsEJGBean.C_NIG).toString();
		
		if (hash.containsKey(ScsEJGBean.C_NUMERODILIGENCIA)) numeroDiligenciaAsi					  			=  hash.get(ScsEJGBean.C_NUMERODILIGENCIA).toString();
		if (hash.containsKey(ScsEJGBean.C_NUMEROPROCEDIMIENTO)) numeroProcedimientoAsi					  			=  hash.get(ScsEJGBean.C_NUMEROPROCEDIMIENTO).toString();
		if (hash.containsKey(ScsEJGBean.C_ANIOPROCEDIMIENTO)) anioProcedimientoAsi					  			=  hash.get(ScsEJGBean.C_ANIOPROCEDIMIENTO).toString();
	
	// Datos pretensiones seleccionado:
		if (hash.containsKey(ScsEJGBean.C_IDPRETENSION)) idPretension					  		=  hash.get(ScsEJGBean.C_IDPRETENSION).toString();

	// Datos del Juzgado seleccionado:
		if (hash.containsKey(ScsEJGBean.C_JUZGADO)) juzgadoAsi					  			=  hash.get(ScsEJGBean.C_JUZGADO).toString();
		if (hash.containsKey(ScsEJGBean.C_JUZGADOIDINSTITUCION)) juzgadoInstitucionAsi		=  hash.get(ScsEJGBean.C_JUZGADOIDINSTITUCION).toString();
 	
 	// Datos de la comisaria seleccionado:
		if (hash.containsKey(ScsEJGBean.C_COMISARIA)) comisariaAsi					  			=  hash.get(ScsEJGBean.C_COMISARIA).toString();
		if (hash.containsKey(ScsEJGBean.C_COMISARIAIDINSTITUCION)) comisariaInstitucionAsi 		=  hash.get(ScsEJGBean.C_COMISARIAIDINSTITUCION).toString();
		
 		if (hash.containsKey("NUMERODESIGNAPROC")) numeroDesignaProc  =  hash.get("NUMERODESIGNAPROC").toString();

	 	if (hash.containsKey("OBSERVACIONES")) OBSERVACIONES =  hash.get("OBSERVACIONES").toString();
	 	if (hash.containsKey("DELITOS")) DELITOS =  hash.get("DELITOS").toString();
	 	//if (hash.containsKey("PROCURADOR")) PROCURADOR =  hash.get("PROCURADOR").toString();
	 	
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

	String[] parametroJuzgado = {idInstitucion, idTurno};
	String[] datosCom={idInstitucion, "-1"};	
	String[] datos={idInstitucion,idTurno};
	String[] datosJuz={idInstitucion,idTurno,"-1"};

	if (idPretension!=null && idInstitucion!=null)
		pretensionesSel.add(0,idPretension+","+idInstitucion);	
	
	if (idcalidad!=null)
		calidadSel.add(0,idcalidad+","+idInstitucion);	

	if (juzgadoAsi!=null && juzgadoInstitucionAsi!=null){
		juzgadoSel.add(0,juzgadoAsi+","+juzgadoInstitucionAsi);	
		if(!juzgadoAsi.equals(""))
			datosJuz[2] = juzgadoAsi;
	}
	
	if (comisariaAsi!=null && comisariaInstitucionAsi!=null){
		comisariaSel.add(0,comisariaAsi+","+comisariaInstitucionAsi);
		if(!comisariaAsi.equals(""))
			datosCom[1] = comisariaAsi;
	}
	
	if (idPreceptivo!=null)
		preceptivoSel.add(0,idPreceptivo);	
	
	if (idSituacion!=null)
		situacionSel.add(0,idSituacion);
	
	if (idRenuncia!=null)
		renunciaSel.add(0,idRenuncia);	
	

	String estilo = "box", readOnly="false", estiloCombo="boxCombo";
		
	String[] datos2={idInstitucion,usr.getLanguage()};	
	String maximaLongitud = "20";
		
	String[] paramPretension = {idInstitucion, "-1"};
	
	if( idPretension != null && (!idPretension.equals("")))
		paramPretension[1]= idPretension;
	
	String asterisco = "&nbsp;(*)&nbsp;";

	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	
	int ejisActivo = 0;
	if (request.getAttribute("EJIS_ACTIVO")!=null){
		ejisActivo = Integer.parseInt(request.getAttribute("EJIS_ACTIVO").toString());
	}		
	
	boolean obligatorioPreceptivo = false;
	boolean obligatorioNumProcedimiento = false;
	boolean obligatorioProcurador = false;
	boolean validarProcedimiento = false;
	boolean obligatorioPretension = false;
	boolean obligatorioSituacion = false;
	boolean obligatoriojuzgado=false;
	boolean obligatorioAsunto=false;
	boolean obligatorioNumDesignaProcurador=false;
	
	if (pcajgActivo==1){
		
	}else if (pcajgActivo==2){
		obligatorioPreceptivo = true;
		obligatorioPretension = true;
		maximaLongitud = "15";		
		obligatorioNumDesignaProcurador = true;
	}else if (pcajgActivo==3){
		obligatorioPreceptivo = true;
		obligatorioPretension = true;
	}else if (pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_CANTABRIA||pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA){
		validarProcedimiento = true;
		obligatorioPreceptivo = true;
		
		obligatorioPretension = true;
	}else if (pcajgActivo==5){
		validarProcedimiento = true;
		obligatorioPretension = true;
		obligatoriojuzgado = true;/*Se modifica para que sea obligatorio el juzgado para pcajg=5*/
	}else if (pcajgActivo==6){
		obligatorioAsunto = true;
	}	
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="utils.system"%>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="gratuita.EJG.delitosFaltas" localizacion="gratuita.busquedaEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<bean:define id="ProcuradoresDESVector" name="ProcuradoresDES" scope="request" />
	
	<style>
		.literalRelacion{display:inline-block;width:100px;padding:0px;padding-left:10px;}
		.literalNombreProc{display:inline-block;width:67px;padding:0px}
		.literalProc{display:inline-block;width:110px;padding:0px}
		.ncol{display:inline-block;width:70px;padding:0px}
		.designacion{display:inline-block;width:160px;padding:0px}
		.nombreProc{display:inline-block;width:220px;padding:0px}
		.botonera{display:inline-block;width:100px;padding:0px;text-align:right}
		.toggleButton{display:inline-block;width:67px;padding:0px}
		.botonDesplegar{cursor:pointer;width:16px;display:inline;padding:0;margin:0}
	</style>	
	<script>
			function obtenerJuzgado() { 
			  	if (document.getElementById("codigoExtJuzgado").value!=""){
				 	document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="DOSFRAMES";	
				 	document.MantenimientoJuzgadoForm.codigoExt2.value=document.getElementById("codigoExtJuzgado").value;
				 	document.MantenimientoJuzgadoForm.submit();
			 	}
			 	else
			 		seleccionComboSiga("juzgado",-1);	
			}
			
			function traspasoDatos(resultado){
				if (resultado[0]==undefined) {
					seleccionComboSiga("juzgado",-1);
					document.getElementById("codigoExtJuzgado").value = "";
				} 
				else
					seleccionComboSiga("juzgado",resultado[0]);				 
			}		
			
		function cambiarJuzgado(comboJuzgado) {
			if(comboJuzgado.value!=""){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
		   			type: "POST",
					url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado2",
					data: "idCombo="+comboJuzgado.value,
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
		
		    function obtenerComisaria() { 
			  if (document.getElementById("codigoExtComisaria").value!=""){
				   document.MantenimientoComisariaForm.nombreObjetoDestino.value="DOSFRAMES";	
				   document.MantenimientoComisariaForm.codigoExtBusqueda.value=document.getElementById("codigoExtComisaria").value;
				   document.MantenimientoComisariaForm.submit();	  
				}			  
				else
			 		seleccionComboSiga("comisaria",-1);
			 }
			
			function traspasoDatosComisaria(resultado) {
				if (resultado[0]==undefined) {
					seleccionComboSiga("comisaria",-1);
					document.getElementById("codigoExtComisaria").value = "";
				} 
				else
					seleccionComboSiga("comisaria",resultado[0]);	
			}
	
		function cambiarComisaria(comboComisaria) {
			if(comboComisaria.value!=""){
				jQuery.ajax({ //Comunicación jQuery hacia JSP  
		   			type: "POST",
					url: "/SIGA/GEN_Comisarias.do?modo=getAjaxComisaria",
					data: "idCombo="+comboComisaria.value,
					dataType: "json",
					success: function(json){		
			       		document.getElementById("codigoExtComisaria").value = json.codigoExt;      		
						fin();
					},
					error: function(e){
						alert('Error de comunicación: ' + e);
						fin();
					}
				});
			}
			else
				document.getElementById("codigoExtComisaria").value = "";
		}	
		
		function calcularAltura() {			
			if (jQuery('table.botonesDetalle').exists()) {
				var tablaBotones = jQuery('table.botonesDetalle')[0];						
				var tablaDatos = jQuery('#divResultados')[0];
				
				var posTablaBotones = tablaBotones.offsetTop;
				var posTablaDatos = tablaDatos.offsetTop;
				
				jQuery('#divResultados').height(posTablaBotones - posTablaDatos);
			}		
		}		
		jQuery(function($){
			var defaultValue = jQuery("#nig2").val();
			if(defaultValue.length > 19){
				jQuery('#info').show();
				jQuery('#imagenInfo').attr('title',defaultValue) ;
			}else{
				jQuery('#info').hide();
				
			}
			jQuery("#nig2").mask("AAAAA AA A AAAA AAAAAAA");
			jQuery("#nig2").keyup();	
			if(document.getElementById("idConsejo") && document.getElementById("idConsejo").value==IDINSTITUCION_CONSEJO_ANDALUZ){
				//jQuery("#numeroProcedimiento2").mask("99999.99");
				//jQuery("#numeroProcedimiento2").keyup();	
			}else if(document.getElementById("ejisActivo").value=='1'){
				jQuery("#numeroProcedimiento2").mask("9999999");
				jQuery("#numeroProcedimiento2").keyup();
				
			}
			<%if (pcajgActivo==2) { %>
			jQuery("#numDesignaProc").mask("999999");
			<%}%>
		});	
		
		function mostrarProcuradoresDES(){
			jQuery('.contenedorProcOtros').show();		
			jQuery("#botonToggleProc").html("<img src=\"<html:rewrite page='/html/imagenes/iconoOcultar.gif'/>\" onclick=\"ocultarProcuradoresDES();\" class=\"botonDesplegar\"/>");
		}
		
		function ocultarProcuradoresDES(){
			jQuery('.contenedorProcOtros').hide();
			jQuery("#botonToggleProc").html("<img src=\"<html:rewrite page='/html/imagenes/iconoDesplegar.gif'/>\" onclick=\"mostrarProcuradoresDES();\" class=\"botonDesplegar\"/>");

		}		
		
		
		
	</script>	
</head>

<body onload="cargarListados();calcularAltura();">
<c:set var="IDINSTITUCION_CONSEJO_ANDALUZ" value="<%=AppConstants.IDINSTITUCION_CONSEJO_ANDALUZ%>" />
<bean:define id="usrBean" name="USRBEAN" scope="session" type="com.atos.utils.UsrBean" />
<input type="hidden" id ="ejisActivo" value = "${EJIS_ACTIVO}"/>
<input type="hidden" id ="idConsejo" value = "${usrBean.idConsejo}"/>
<table class="tablaTitulo" cellspacing="0">

	<html:form action = "/JGR_DelitosEJG.do" method="POST" target="resultado" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>			
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoEJGForm" property="anio" />
		<html:hidden name="pestanaDelitoEJGForm" property="numero" />
		<html:hidden name="pestanaDelitoEJGForm" property="idTipoEJG" />
		<html:hidden name="pestanaDelitoEJGForm" property="idInstitucion" />
		<html:hidden name="pestanaDelitoEJGForm"  styleId="jsonVolver" property = "jsonVolver"  />
	</html:form>
	
	<html:form action = "/JGR_MantenimientoEJG.do" method="POST" target="submitArea"  style="display:none">
		<input type="hidden" name="modo"        value="buscarJuzgado"> 
		<html:hidden property = "idTurnoEJG" value = "<%=idTurno%>"/>
		<html:hidden property = "idTipoEJG" value = "<%=idTipoEJG%>"/>		
		<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>
		<html:hidden property = "numero" value = "<%=numero%>"/>
		<html:hidden property = "anio" value = "<%=anio%>"/>		
		<html:hidden property = "idProcurador" value="<%=idProcurador%>"/>
		<html:hidden property = "idInstitucionProcurador" value="<%=idInstitucionProcurador%>"/>
	    <html:hidden property = "idTipoenCalidad" value="<%=idcalidad%>"/>
		<html:hidden property = "observaciones" value=""/>
		<html:hidden property = "comisaria" value="<%=OBSERVACIONES%>"/>
		<html:hidden property = "juzgado" value=""/>
		<html:hidden property = "numeroDilegencia" value="<%=numeroDiligenciaAsi%>"/>
		<html:hidden property = "numeroProcedimiento" value="<%=numeroProcedimientoAsi%>"/>		
		<html:hidden property = "anioProcedimiento" value="<%=anioProcedimientoAsi%>"/>		
		<html:hidden property = "delitos" value="<%=DELITOS%>"/>
		<html:hidden property = "pretension" value=""/>
		<html:hidden property = "idPreceptivo" value="<%=idPreceptivo%>"/>
		<html:hidden property = "idSituacion" value="<%=idSituacion%>"/>
		<html:hidden property = "fechaProc" value=""/>
		<html:hidden property = "numeroDesignaProc" value="<%=numeroDesignaProc%>"/>
		<html:hidden property = "idRenuncia" value="<%=idRenuncia%>"/>
		<html:hidden property = "NIG" value="<%=nig%>"/>
		<html:hidden property = "actualizaProcuradores" value="0"/>
	</html:form>
	
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type=""  style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo"        value="abrirBusquedaProcuradorModal">
	</html:form>
		
	<html:form action="/JGR_ContrariosEjg.do" method="POST" target="resultado1" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>			
		<!-- Datos de la pestanha -->
		<html:hidden name="ContrariosEjgForm" property="anio" value = "<%=anio%>" />
		<html:hidden name="ContrariosEjgForm" property="numero" value = "<%=numero%>" />
		<html:hidden name="ContrariosEjgForm" property="idTipoEJG" value = "<%=idTipoEJG%>"/>
		<html:hidden name="ContrariosEjgForm" property="idInstitucion" value = "<%=idInstitucion%>"/>
	</html:form>
	
	<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
	
	<html:form action="/JGR_MantenimientoComisarias.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarComisaria">
		<html:hidden property = "codigoExtBusqueda" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>	

	<tr>
<%  
		String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
		ScsEJGAdm adm = new ScsEJGAdm (usr);
						
		Hashtable hTitulo = adm.getTituloPantallaEJG(idInstitucion, anio, numero,idTipoEJG,(String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString()));
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
			<c:out value="${PREFIJOEXPEDIENTECAJG}" />&nbsp;<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
			- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
		</td>
	</tr>
</table>	
		
<table class="tablaCentralCampos" align="center">
	<tr>
		<td>
			<siga:ConjCampos leyenda="pestana.justiciagratuitadesigna.defensajuridica">
				<table width="100%" border="0">
					<tr>
						<td class="labelText" width="105px">
							<siga:Idioma key='gratuita.operarEJG.literal.Preceptivo'/>
<%
								if (obligatorioPreceptivo) {
%>
									<%=asterisco%> 
<%
								}
%>		
					    </td> 

						<td colspan="2">
<%
							if(modopestanha.equals("editar")) {
%>						 
								<siga:ComboBD nombre="preceptivo2" tipo="comboPreceptivo" ancho="250" clase="<%=estiloCombo%>" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=preceptivoSel%>"  readonly="false"/> 
<%
							} else { 
%>
								<siga:ComboBD nombre="preceptivo2" tipo="comboPreceptivo" ancho="250" clase="boxConsulta" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=preceptivoSel%>"  readonly="true"/>          	   
<%
							}
%>							
						</td>
						 
						<td>
<%
							if(modopestanha.equals("editar")) {
%>	
							    <siga:ComboBD nombre="renuncia" tipo="comboRenuncia" ancho="200" clase="<%=estiloCombo%>" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=renunciaSel%>"  readonly="false"/>
<%
							} else { 
%> 
							   <siga:ComboBD nombre="renuncia" tipo="comboRenuncia" ancho="200" clase="boxConsulta" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=renunciaSel%>"  readonly="true"/>          	   
<%
							}
%> 
						</td>	
										
						<td class="labelText">
						   <siga:Idioma key='gratuita.operarEJG.literal.situacion'/>
<%
							if (obligatorioSituacion) {
%>
								<%=asterisco%> 
<%
							}
%>							   
					    </td> 
					    
					    <td>
<%
							if(modopestanha.equals("editar")) {
%>						 
								<siga:ComboBD nombre="situacion" tipo="comboSituacion" ancho="200" clase="<%=estiloCombo%>" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=situacionSel%>"  readonly="false"/> 
<%
							} else { 
%>
								<siga:ComboBD nombre="situacion" tipo="comboSituacion" ancho="200" clase="boxConsulta" filasMostrar="1"  seleccionMultiple="false" obligatorio="false"  parametro="<%=datos%>" elementoSel="<%=situacionSel%>"  readonly="true"/>          	   
<%	
							}
%>						    
					    </td>												
					 </tr>
					 
					<tr>
						<td class="labelText">
							<siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligenciasolo'/>
						</td>
						<td>
<%
							if(modopestanha.equals("editar")) {
%>
								<input name="numeroDilegencia2" size="10" maxlength="<%=maximaLongitud%>" type="text" value="<%=numeroDiligenciaAsi%>" class="<%=estilo%>" />
<%
							} else {
%>
								<input name="numeroDilegencia2" size="10" maxlength="<%=maximaLongitud%>" type="text" value="<%=numeroDiligenciaAsi%>" class="boxConsulta" />
<%
							}
%>								
						</td> 
						
						<td class="labelText">
							<siga:Idioma key='gratuita.mantAsistencias.literal.c.Detencion'/>
						</td>
						
						<td colspan="3">
<%
							if(modopestanha.equals("editar")) {
%>
							 	<input type="text" name="codigoExtComisaria" class="box" size="3"  style="margin-top:3px;" maxlength="10" onBlur="obtenerComisaria();"/>
								<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" ancho="500" obligatorio="false" parametro="<%=datosCom%>" elementoSel="<%=comisariaSel%>" clase="<%=estilo%>" hijo="t" readonly="false" accion="parent.cambiarComisaria(this);"/>
<%
							} else {
%>
								<siga:ComboBD nombre="comisaria" tipo="comboComisariasTurno" ancho="500" obligatorio="false" parametro="<%=datosCom%>" elementoSel="<%=comisariaSel%>" clase="boxConsulta" hijo="t" readonly="true"/>
<%
							}
%>							
						</td>
					 </tr>
					 
					<tr>
						<td class="labelText">
							<siga:Idioma key='gratuita.mantAsistencias.literal.numeroProced'/>
<%
							if (obligatorioNumProcedimiento) {
%>
								<%= asterisco %> 
<%
							}
%>
						</td>
						<td> 
						<c:choose>
							<c:when	test="${EJIS_ACTIVO=='1'}">
										
								<%
								if(modopestanha.equals("editar")) {
								%>	
								 	<input name="numeroProcedimiento2" id="numeroProcedimiento2" size="7" maxlength="7" type="text" value="<%=numeroProcedimientoAsi%>" class="<%=estilo%>" />/
								 	<input name="anioProcedimiento2" size="4" maxlength="4" type="text" value="<%=anioProcedimientoAsi%>" class="<%=estilo%>" />
								<%
								} else { 
								%>
								 	<input name="numeroProcedimiento2" id="numeroProcedimiento2"  size="7" maxlength="7" type="text" value="<%=numeroProcedimientoAsi%>" class="boxConsulta" />/
								 	<input name="anioProcedimiento2" name="anioProcedimiento2" size="4" maxlength="4" type="text" value="<%=anioProcedimientoAsi%>" class="boxConsulta" />
								<%
								}
								%>		
		
							</c:when>
							<c:when	test="${usrBean.idConsejo==IDINSTITUCION_CONSEJO_ANDALUZ}">
										
								<%
								if(modopestanha.equals("editar")) {
								%>	
								 	<input name="numeroProcedimiento2" id="numeroProcedimiento2" size="7" maxlength="8" type="text" value="<%=numeroProcedimientoAsi%>" class="<%=estilo%>" />/
								 	<input name="anioProcedimiento2" size="4" maxlength="4" type="text" value="<%=anioProcedimientoAsi%>" class="<%=estilo%>" />
								<%
								} else { 
								%>
								 	<input name="numeroProcedimiento2"id="numeroProcedimiento2"  size="7" maxlength="8" type="text" value="<%=numeroProcedimientoAsi%>" class="boxConsulta" />/
								 	<input name="anioProcedimiento2" size="4" maxlength="4" type="text" value="<%=anioProcedimientoAsi%>" class="boxConsulta" />
								<%
								}
								%>	
		
							</c:when>
									
							<c:otherwise>
								<%
								if(modopestanha.equals("editar")) {
								%>
									<input name="numeroProcedimiento2" size="10" type="text" value="<%=numeroProcedimientoAsi%>" class="<%=estilo%>" maxlength="<%=maximaLongitud%>"/>
								<%
								} else {
								%>
									<input name="numeroProcedimiento2" size="10" type="text" value="<%=numeroProcedimientoAsi%>" class="boxConsulta"/>
								<%
								}
								%>	
							</c:otherwise>
						</c:choose>
						
						</td>
								
						<td class="labelText">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
<% 
							if (obligatoriojuzgado) { 
%>
								<%=asterisco%>
<%
							}
%>
						</td>
							 
<%
						if (ejisActivo>0 || pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_CANTABRIA||pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA) {
%>																		
							<td colspan="3">	
<%
								if(modopestanha.equals("editar")) {
%>
							 	  	<input type="text" name="codigoExtJuzgado" class="box" size="3"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />							 	  
							 	  	<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosEJG" ancho="500" clase="<%=estiloCombo%>" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="false" accion="Hijo:pretensiones2; parent.cambiarJuzgado(this);" />           	   
<%
								} else { 
%>
									<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosEJG" ancho="500" clase="boxConsulta" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="true"/>           	   
<%
								}
%>							
							</td>						
<%
						} else {
%>		

							<td colspan="3">	
<%
								if(modopestanha.equals("editar")) {
%>
							 	  	<input type="text" name="codigoExtJuzgado" class="box" size="3"  style="margin-top:3px;" maxlength="10" onBlur="obtenerJuzgado();" />							 	  
							 	  	<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosEJG" ancho="500" clase="<%=estiloCombo%>" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="false" accion="parent.cambiarJuzgado(this);" />           	   
<%
								} else { 
%>
									<siga:ComboBD nombre="juzgado" tipo="comboJuzgadosEJG" ancho="500" clase="boxConsulta" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=datosJuz%>" elementoSel="<%=juzgadoSel%>" hijo="t" readonly="true"/>           	   
<%
								}
%>							
							</td>						
						
<%
						}
%>									
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key='gratuita.operarEJG.literal.observacionesAsunto'/>
<% 
							if (obligatorioAsunto){ 
%>
									<%=asterisco%>
<%
							}
%>
						</td>
						<td colspan="2">	
<%
							if(modopestanha.equals("editar")) { 
%>
								<html:textarea property="observaciones2" styleClass="box" 
									onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)"
									style="overflow-y:auto; overflow-x:hidden; width:250px; height:30px; resize:none;"    
									value="<%=OBSERVACIONES%>"></html:textarea>
<%
							} else {
%>
								<html:textarea property="observaciones2" styleClass="boxConsulta"
									style="overflow-y:auto; overflow-x:hidden; width:250px; height:30px; resize:none;"
									value="<%=OBSERVACIONES%>"></html:textarea>
<%
							}
%>							
						</td>
						
						<td class="labelText">
							<siga:Idioma key='gratuita.general.literal.comentariosDelitos'/>
						</td>
						<td colspan="2">	
<%
							if(modopestanha.equals("editar")) { 
%>
								<html:textarea name="DefinirMantenimientoEJGForm" property="delitos2" styleClass="box"
									onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)"
									style="overflow-y:auto; overflow-x:hidden; width:350px; height:30px; resize:none;"   
									value="<%=DELITOS%>"></html:textarea>							
<%
							} else {
%>
								<html:textarea name="DefinirMantenimientoEJGForm" property="delitos2" styleClass="boxConsulta"
								style="overflow-y:auto; overflow-x:hidden; width:350px; height:30px; resize:none;"  
								value="<%=DELITOS%>"></html:textarea>							
<%
							}
%>							
						</td>		
					</tr>	
							
					<tr>
						<td class="labelText">	
							<siga:Idioma key='gratuita.personaJG.literal.calidad'/>&nbsp;(*)
						</td>
						<td colspan="2">			
<%
							if(modopestanha.equals("editar")) {
%>
								<siga:ComboBD nombre="calidad2" tipo="ComboCalidades" ancho="250" clase="<%=estiloCombo%>" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="true" obligatorioSinTextoSeleccionar="true" parametro="<%=datos2%>" elementoSel="<%=calidadSel%>" hijo="t" readonly="false"/>           	   
<%
							} else {
%>
								<siga:ComboBD nombre="calidad2" tipo="ComboCalidades" ancho="250" clase="boxConsulta" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=calidadSel%>" hijo="t" readonly="true"/>           	   
<%
							}
%>
						</td>
							
						<td class="labelText">	
							<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>							
<%
							if (obligatorioPretension) {
%>
								<%=asterisco%> 
<%
							}
%>	
						</td>	
<%
						if (ejisActivo>0 || pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_CANTABRIA||pcajgActivo==CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA) {
%>							
							<td colspan="2">
<%
								if (modopestanha.equals("editar")) {
%>
									<siga:ComboBD nombre="pretensiones2" tipo="comboPretensionesEjis" ancho="350" clase="<%=estiloCombo%>" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=paramPretension%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="false"/>           	   
<%
								} else {
%>
									<siga:ComboBD nombre="pretensiones2" tipo="comboPretensionesEjis" ancho="350" clase="boxConsulta" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=paramPretension%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
<%
								}
%>								
							</td>
<%
						} else { 
%>	
							<td colspan="2">
<%
								if (modopestanha.equals("editar")) { 
%>
									<siga:ComboBD nombre="pretensiones2" tipo="comboPretensiones" ancho="350" clase="<%=estiloCombo%>" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=paramPretension%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="false"/>           	   
<%
								} else { 
%>
									<siga:ComboBD nombre="pretensiones2" tipo="comboPretensiones" ancho="350" clase="boxConsulta" filasMostrar="1" pestana="2" seleccionMultiple="false" obligatorio="false"  parametro="<%=paramPretension%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
<%
								}
%>							
							</td>				
<% 
						} 
%>								
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
						</td>
						<td > 
<%
							if(modopestanha.equals("editar")) {
%>
							 	<input id="nig2" name="nig2"  type="text" value="<%=nig%>" class="<%=estilo%>" style="size:19;width:200px"/>
<%
							} else {
%>
								<input id="nig2" name="nig2"  type="text" value="<%=nig%>" class="boxConsulta" style="size:19;width:200px"/>
<%
							}
%>						
						</td>
						
									
						<td id="info" style="display:none"><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
						</td>
						
						<td colspan="3">&nbsp;</td>
					</tr>
					
					<tr>
						<td colspan="6"> 
						
				<siga:ConjCampos leyenda="gratuita.datosProcurador.literal.procurador">
					<table  width="100%" border="0">
						<tr>

							<td class="labelText">
	<%--										<html:hidden name = "DefinirMantenimientoEJGForm" property = "procurador" value="<%=procuradorSel%>"/> --%>
								<siga:Idioma key="gratuita.busquedaSOJ.literal.numeroColegidado"/>
	<%
								if (obligatorioProcurador) {
	%>
									<%=asterisco%> 
	<%
								}
	%>
							</td>
							<td>
								<input type="text" name="nColegiadoProcurador" id="nColegiadoProcurador" size="5" maxlength="100" class="boxConsulta" readOnly value="<%=procuradorNumColegiado%>"/>
							</td>
							
							<td  class="labelText" >
								<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
							</td>
							<td>
								<input type="text" name="nombreCompleto" id="nombreCompleto" size="30" maxlength="100" class="boxConsulta" readOnly value="<%=procuradorNombreCompleto%>"/>
							</td>
							
							<td class="labelText">
								<siga:Idioma key='gratuita.operarEJG.literal.numDesigProc'/>
							</td>
							<td>
	<%
								if (modopestanha.equals("ver")) {
	%>
		                        	<input type="text" class="boxConsulta" value="99" readOnly>
	<%
								} else {
	%>
								  	<input type="text" id="numDesignaProc" name="numDesignaProc" class="box" size="5" maxlength="20" value="<%=numeroDesignaProc%>">
	<%
								}
	%>
	                           </td>
	                           
	                           <td class="labelText">	
	                            	<siga:Idioma key='gratuita.operarEJG.literal.fechaDesigProc'/>
	                           </td>
	                           <td>
	<%   
								if (modopestanha.equals("ver")) {
	%>
	                             	<input type="text" class="boxConsulta" value="<%=FECHAPROCURADOR%>" readOnly>
	<%	
								} else { 
	%>
	                            		<siga:Fecha nombreCampo="fechaProc1" valorInicial="<%=FECHAPROCURADOR%>" readOnly="true" />
	<%
								}
	%>
	                           </td>
	                           
							<td>
	<%
								if(modopestanha.equals("editar")) {
	%>
									<html:button property='idButton' onclick="return buscarProcurador();" styleClass="button"><siga:Idioma key="general.boton.search"/></html:button>
	<%
								}
	%>
							</td>
							<td>
	<%
								if(modopestanha.equals("editar")) {
	%>
									<html:button property='idButton' onclick="return limpiarProcurador();" styleClass="button"><siga:Idioma key="general.boton.clear"/></html:button> 
	<%
								}
	%>
							</td>
							
						</tr> 
						
						<logic:notEmpty name="ProcuradoresDES" scope="request">						
							<tr>
								<td colspan="10">
									<logic:iterate name="ProcuradoresDES" id="procurador" scope="request" indexId="index"> 
										<logic:equal name="index" value="0">
											<div class="contenedorPrimerProc">
												<span class="labelText literalRelacion">Relacionados</span>
												<span id="botonToggleProc" class="toggleButton"><img src="<html:rewrite page='/html/imagenes/iconoDesplegar.gif'/>" onclick="mostrarProcuradoresDES();" class="botonDesplegar"/></span>
											</logic:equal>
										<logic:notEqual name="index" value="0">
											<div class="contenedorProcOtros" style="display:none">
												<span class="labelText literalRelacion">&nbsp;</span>
												<span class="toggleButton">&nbsp;</span>
										</logic:notEqual>
											<span class="labelText literalNombreProc"><siga:Idioma key='gratuita.busquedaSOJ.literal.nombre'/></span>
											<span class="labelTextValue nombreProc">${procurador.NOMBRE}&nbsp;${procurador.APELLIDOS1}&nbsp; ${procurador.APELLIDOS2}</span>
											<span class="labelText literalProc"><siga:Idioma key='gratuita.busquedaSOJ.literal.numeroColegidado'/></span>
											<span class="labelTextValue ncol">${procurador.NCOLEGIADO}</span>
											<span class="labelText literalProc"><siga:Idioma key='gratuita.busquedaDesignas.literal.Designacion'/></span>
											<span class="labelTextValue designacion">${procurador.ANIO}/${procurador.CODIGO}</span>
										</div>
									</logic:iterate>
									<script type="text/javascript">
										if (jQuery('.contenedorProcOtros')[0]) {
											jQuery("#botonToggleProc").show();
										}else{
											//jQuery("#botonToggleProc").hide();
										}
									</script>
								  </td>
								</tr>	
							</logic:notEmpty>									
						</table>
						</siga:ConjCampos>		
						</td>			
					</tr>					
				</table>
			</siga:ConjCampos>
	  	</td>
	</tr>
</table>



<div id="divResultados">
	<div style="position:relative;height:50%;width:100%;">
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
			id="resultado"
			name="resultado" 
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0"					 				
			style="position:relative;height:100%;width:100%;"></iframe>
	</div>	
		
	<div style="position:relative;height:50%;width:100%;">
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
			id="resultado1"
			name="resultado1" 
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0"					 
			style="position:relative;height:100%;width:100%;"></iframe>	
	</div>
</div>
<%
	if(modopestanha.equals("editar")) {
%>
		<siga:ConjBotonesAccion botones="G,V"  clase="botonesDetalle" modo="<%=modopestanha%>"/> 
<%
	} else {
%>
		<siga:ConjBotonesAccion botones="V"   clase="botonesDetalle" modo="<%=modopestanha%>"/> 
<%
	}
%>
		
<!-- INICIO: SCRIPTS BOTONES ACCION -->
<script language="JavaScript">
	
	jQuery.noConflict();
	
	function refrescarLocal() {
		parent.pulsarId("pestana.justiciagratuita.EJG.delitos","mainPestanas"); 
	}
	
	function cargarListados(){
		document.forms[0].target = 'resultado';		
		document.forms[0].modo.value = "buscar";
		document.forms[0].submit();
		 
			
		document.forms[3].target = 'resultado1';		
		document.forms[3].modo.value = "";
		document.forms[3].submit();
	}
	
	
	
		// Funcion asociada a boton Nuevo 
		/*function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var salida = ventaModalGeneral(document.forms[0].name,"P"); 			
			if (salida == "MODIFICADO") 
				refrescarLocal();
		}*/
		
	// Asociada al boton Volver 
	function accionVolver() {
		if(document.pestanaDelitoEJGForm.jsonVolver && document.pestanaDelitoEJGForm.jsonVolver.value!=''){
			
			jSonVolverValue = document.pestanaDelitoEJGForm.jsonVolver.value;
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
			document.forms[0].idInstitucion.value="<%=idInstitucion%>";
			document.forms[0].action="./JGR_EJG.do";	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
	}
		
	function accionGuardar(){		
		sub();
		var observaciones = document.getElementById("observaciones").value;
<%
		if (pcajgActivo>0) {
%>
			var error = "";
	   		if (<%=obligatorioPreceptivo%> && document.getElementById("preceptivo2").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.Preceptivo'/>"+ '\n';
					
			if (<%=obligatorioSituacion%> && document.getElementById("situacion").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.situacion'/>"+ '\n';
					
			if (<%=obligatorioNumProcedimiento%> && document.getElementById("numeroProcedimiento2").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.mantAsistencias.literal.numeroProced'/>"+ '\n';
				
			if ( <%=obligatorioPretension%> && document.getElementById("pretensiones2").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.pretensiones'/>"+ '\n';
				
			if (<%=obligatorioProcurador%> && document.getElementById("nColegiadoProcurador").value=="")
				error += "<siga:Idioma key='errors.required' arg0='gratuita.datosProcurador.literal.procurador'/>"+ '\n';
				
			if (<%=obligatorioNumDesignaProcurador%> && document.getElementById("numDesignaProc").value=="" && document.getElementById("idProcurador").value != null && document.getElementById("idProcurador").value != '')
				error += "<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.numDesigProc'/>"+ '\n';
					
			if(!document.getElementById("idConsejo") || document.getElementById("idConsejo").value !=IDINSTITUCION_CONSEJO_ANDALUZ || document.getElementById("ejisActivo").value=='0'){
				if (<%=validarProcedimiento%>) {
					if(!validaProcedimiento(document.getElementById("numeroProcedimiento2").value))
						error += "<siga:Idioma key='gratuita.procedimientos.numero.formato'/>"+ '\n';
				}
			}				
			if(document.getElementById("calidad2").value=="")
				  error += "<siga:Idioma key='gratuita.personaJG.literal.mensajecalidad'/>"+ '\n';

			if(<%=obligatoriojuzgado%> && document.getElementById("juzgado").value=="")										
				error += "<siga:Idioma key='gratuita.editarDesigna.juzgado'/>"+ '\n';

			if(<%=obligatorioAsunto%> && document.getElementById("observaciones2").value=="")										
				error += "<siga:Idioma key='gratuita.editarDesigna.asunto'/>"+ '\n';
				
			if(error!=""){
				alert(error);
				fin();
				return false;
			}
<%
		}
%> 		 	
		var nigAux = document.getElementById("nig2").value;


		nigAux = ready2ApplyMask(nigAux);
		nigAux = nigAux.toUpperCase();


		valueNumProcedimiento = document.getElementById("numeroProcedimiento2").value;
		objectConsejo = document.getElementById("idConsejo");
		valueEjisActivo = document.getElementById("ejisActivo").value;
		if((objectConsejo && objectConsejo.value ==IDINSTITUCION_CONSEJO_ANDALUZ) || valueEjisActivo=='1'){
			error = validarFormatosNigNumProc(nigAux,valueNumProcedimiento,document.getElementById("anioProcedimiento2"),valueEjisActivo,objectConsejo);
			if(valueNumProcedimiento!='' && document.getElementById("anioProcedimiento2").value ==''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.operarEJG.literal.anio' />"+"\n";
				
			}
			if(valueNumProcedimiento=='' && document.getElementById("anioProcedimiento2").value !=''){
				error += "<siga:Idioma key='errors.required' arg0='gratuita.informeJustificacionMasiva.literal.numeroProcedimiento' />"+"\n";
			}
			if(error!=''){
				fin();
				alert(error);
				return false;
				
			}
			formateaNumProcedimiento(valueNumProcedimiento,valueEjisActivo,objectConsejo);
		}


		if (observaciones.length <= 1024) {
			document.DefinirMantenimientoEJGForm.modo.value = "modificarDefensa";
			document.DefinirMantenimientoEJGForm.target = "submitArea";				
			document.DefinirMantenimientoEJGForm.idProcurador.value = document.getElementById("idProcurador").value	;
			document.DefinirMantenimientoEJGForm.idInstitucionProcurador.value = document.getElementById("idInstitucionProcurador").value	;
			document.DefinirMantenimientoEJGForm.idPreceptivo.value = document.getElementById("preceptivo2").value	;
			document.DefinirMantenimientoEJGForm.idSituacion.value = document.getElementById("situacion").value	;				
			document.DefinirMantenimientoEJGForm.idTipoenCalidad.value = document.getElementById("calidad2").value	;
			document.DefinirMantenimientoEJGForm.comisaria.value = document.getElementById("comisaria").value	;
			document.DefinirMantenimientoEJGForm.juzgado.value = document.getElementById("juzgado").value	;				
			document.DefinirMantenimientoEJGForm.numeroDilegencia.value = document.getElementById("numeroDilegencia2").value	;
			document.DefinirMantenimientoEJGForm.numeroProcedimiento.value = document.getElementById("numeroProcedimiento2").value;
			if(document.getElementById("anioProcedimiento2"))
				document.DefinirMantenimientoEJGForm.anioProcedimiento.value = document.getElementById("anioProcedimiento2").value;
 		
			document.DefinirMantenimientoEJGForm.observaciones.value = document.getElementById("observaciones2").value;
			document.DefinirMantenimientoEJGForm.delitos.value = document.getElementById("delitos2").value;
			document.DefinirMantenimientoEJGForm.pretension.value = document.getElementById("pretensiones2").value;
			document.DefinirMantenimientoEJGForm.fechaProc.value = document.getElementById("fechaProc1").value;				
			document.DefinirMantenimientoEJGForm.idRenuncia.value = document.getElementById("renuncia").value;
			document.DefinirMantenimientoEJGForm.numeroDesignaProc.value = document.getElementById("numDesignaProc").value;
			document.DefinirMantenimientoEJGForm.NIG.value = nigAux;
			

											
               
			if (document.DefinirMantenimientoEJGForm.fechaProc.value!="" &&	document.getElementById("nombreCompleto").value==""){
				 alert('<siga:Idioma key="gratuita.operarEJG.message.fechaDesigProc"/>');
				 fin();
				 return false;
			}
			if(document.getElementById('info'))
				jQuery('#info').hide();
			
			if(document.getElementById("idProcurador").value != null && document.getElementById("idProcurador").value != '' && '${ProcuradoresDESVector}' != null && '${ProcuradoresDESVector}' != ''){
				var type = "<siga:Idioma key="gratuita.cambiosProcuradoresDesigna.actualizar.procuradores"/>";
				if(confirm(type)){				
					document.DefinirMantenimientoEJGForm.actualizaProcuradores.value = "1";
				} else{
					document.DefinirMantenimientoEJGForm.actualizaProcuradores.value = "0";
				}
			}			
			
			document.DefinirMantenimientoEJGForm.submit();
			
		} else  {
			alert('<siga:Idioma key="gratuita.operarEJG.message.lontigudObservaciones"/>');	
			fin();
			return false;
		}
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
				document.getElementById("numeroProcedimiento2").value = numProcedimiento;
			}
			
		}else if(valueEjisActivo=='1'){
			if(valueNumProcedimiento!=''){
				numProcedimiento = pad(valueNumProcedimiento,7,false);
				document.getElementById("numeroProcedimiento2").value = numProcedimiento;
			}
		
		}
	}
	function limpiarProcurador() {
		document.getElementById("nombreCompleto").value = '';
		document.getElementById("nColegiadoProcurador").value     = '';
		document.getElementById("idProcurador").value = '';
		document.getElementById("idInstitucionProcurador").value = '';
		document.getElementById("fechaProc1").value = '';
		document.getElementById("numDesignaProc").value = '';
	}
		
	
		// Valida el numero de procedimiento (n/aaaa)
		function validaProcedimiento (strValue) {
			var objRegExp  = /^([0-9]+\/[0-9]{4})?$/;
			return objRegExp.test(strValue);
		}				

			
	function buscarProcurador() {
		var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
		if((resultado != undefined) && (resultado[0] != undefined) && (resultado[1] != undefined)) {
			var procurador = resultado[0].split(",");
			document.all.DefinirMantenimientoEJGForm.idProcurador.value = procurador[0];
			document.all.DefinirMantenimientoEJGForm.idInstitucionProcurador.value = procurador[1];
			document.getElementById("nombreCompleto").value = resultado[1];
			document.getElementById("nColegiadoProcurador").value = resultado[2];
			//alert ("Procurador: " + document.all.DefinirMantenimientoEJGForm.procurador.value + ", Nombre: " + document.all.DefinirMantenimientoEJGForm.nombreCompleto.value + ", Ncolegiado: " + document.all.DefinirMantenimientoEJGForm.nColegiadoProcurador.value);
		}	
	}				
</script>
<!-- FIN: SCRIPTS BOTONES ACCION -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>