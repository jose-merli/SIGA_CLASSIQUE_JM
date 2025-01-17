<!DOCTYPE html>
<html>
<head>
<!-- busquedaEJG.jsp -->

<!-- CABECERA JSP -->
<%@page import="com.siga.gratuita.form.DefinirEJGForm"%>
<%@page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@page import="org.redabogacia.sigaservices.app.autogen.model.ScsTiporesolucion"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="org.redabogacia.sigaservices.app.util.SIGAReferences"%>
<%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>

<!-- JSP -->
<%
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean esComision = usr.isComision(); 
	boolean esComisionMultiple = usr.getInstitucionesComision()!=null &&usr.getInstitucionesComision().length>1;
	String idInstitucionComision = usr.getIdInstitucionComision().toString();
	HttpSession ses = request.getSession();
	
	
	String accion="", anioActa="", apellido1="", apellido2="", asunto="", busquedaRealizada="", cajgAnio="", cajgNumero="", calidad="", calidadidinstitucion="", formulario="", idcalidad="", idPersona="", idPersonaDefensa="", idRenuncia="", idremesa="", nif="", nombre="", numActa="", numEJG="", procedimiento="", sNig="", prefijoRemesa="", numeroRemesa="", sufijoRemesa=""; 
	String creadoDesde="", fechaApertura="", fechaAperturaHasta="", fechaDictamenDesde="", fechaDictamenHasta="", fechaEstadoDesde="", fechaEstadoHasta="",  fechaLimiteDesde="", fechaLimiteHasta="", fechaPonenteDesde="", fechaPonenteHasta="";
	ArrayList calidadSel=new ArrayList(), idEstado=new ArrayList(), idGuardia=new ArrayList(), idResolucion=new ArrayList(), idTipoDictamen=new ArrayList(), idTipoEJG=new ArrayList(), idTipoEJGColegio=new ArrayList(), idTurno=new ArrayList(), juzgado=new ArrayList(), juzgadoSel = new ArrayList(), renunciaSel=new ArrayList(), vPonente=new ArrayList(), vFundamentoJuridico= new ArrayList(), vTipoRatificacion= new ArrayList(), idPreceptivo = new ArrayList();
	ArrayList idInstitucionSelected = new ArrayList();
	Hashtable miHash = new Hashtable();
	
	boolean permisoEejg = false;
	boolean accesoActa = false;
	
	String anioActual = UtilidadesBDAdm.getYearBD("");
	String anio = anioActual;
	String nColegiado = request.getAttribute("nColegiado") == null ? "" : (String) request.getAttribute("nColegiado");
	String nombreColegiado = request.getAttribute("nombreColegiado") == null ? "" : (String) request.getAttribute("nombreColegiado");
	String ventanaCajg = request.getParameter("ventanaCajg");

	ReadProperties rp=new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	String idordinario = rp.returnProperty("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[] = { idordinario, idordinario };

	String sDictaminado = "I";
	
	if(request.getAttribute("permisoEejg")!=null)
		permisoEejg = Boolean.parseBoolean(request.getAttribute("permisoEejg").toString());	
	if(request.getAttribute("accesoActa")!=null)
		accesoActa = Boolean.parseBoolean(request.getAttribute("accesoActa").toString());
	if(request.getAttribute("idremesa")!=null)
		idremesa = (String) request.getAttribute("idremesa");	
	String idTipoRatificacionEjg = "";
	String tiposResolucionBusqueda = "";
	String paramsInstitucionComision = "{\"idinstitucioncomision\":\""+idInstitucionComision+"\"}";
	String datoPonente[] = {idInstitucionComision,idInstitucionComision};
	
	boolean chkBusquedaExactaSolicitante = true;
	if (ses.getAttribute("DATOSFORMULARIO") instanceof Hashtable) {
		miHash = (Hashtable) ses.getAttribute("DATOSFORMULARIO");
		ses.removeAttribute("DATOSFORMULARIO");    
		
		try {
			busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();
	
			if (busquedaRealizada != null) {
				if (miHash.get(ScsEJGBean.C_ANIO) != null)
					anio = miHash.get(ScsEJGBean.C_ANIO).toString();
				
				if (miHash.get(ScsEJGBean.C_NUMEJG) != null)
					numEJG = miHash.get(ScsEJGBean.C_NUMEJG).toString();										
				
				if (miHash.get(ScsEJGBean.C_IDPERSONA) != null)
					idPersona = miHash.get(ScsEJGBean.C_IDPERSONA).toString();
				
				if (miHash.get(ScsPersonaJGBean.C_NIF) != null)
					nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();
				
				if (miHash.get(ScsPersonaJGBean.C_NOMBRE) != null)
					nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();
				
				if (miHash.get(ScsPersonaJGBean.C_APELLIDO1) != null)
					apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();
				
				if (miHash.get(ScsPersonaJGBean.C_APELLIDO2) != null)
					apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();
				
				if (miHash.get("CREADODESDE") != null)
					creadoDesde = miHash.get("CREADODESDE").toString();
				
				if (miHash.get("CALIDAD") != null)
					calidad = miHash.get("CALIDAD").toString();
				
				if (miHash.get("JUZGADO") != null)
					juzgado.add(miHash.get("JUZGADO").toString());
				
				if (miHash.get("PROCEDIMIENTO") != null)
					procedimiento = miHash.get("PROCEDIMIENTO").toString();
				
				if (miHash.get("ASUNTO") != null)
					asunto = miHash.get("ASUNTO").toString();
				
				if (miHash.get(ScsEJGBean.C_ANIO_CAJG) != null) 
					cajgAnio=miHash.get(ScsEJGBean.C_ANIO_CAJG).toString();
				
				if (miHash.get(ScsEJGBean.C_NUMERO_CAJG) != null) 
					cajgNumero=miHash.get(ScsEJGBean.C_NUMERO_CAJG).toString();
				
				if (miHash.get(ScsEJGBean.C_ANIOACTA) != null) 
					anioActa=miHash.get(ScsEJGBean.C_ANIOACTA).toString();
				
				if (miHash.get("NUMEROACTA") != null) 
					numActa=miHash.get("NUMEROACTA").toString();
				
				if (miHash.get(ScsEJGBean.C_NIG) != null) 
					sNig=miHash.get(ScsEJGBean.C_NIG).toString();
				
				if (miHash.get(ScsEJGBean.C_IDRENUNCIA)!= null) {
					idRenuncia=miHash.get(ScsEJGBean.C_IDRENUNCIA).toString();
					if (idRenuncia!=null)
						renunciaSel.add(0,idRenuncia);	
				}
				
				if (miHash.get("numeroRemesa") != null) 
					numeroRemesa=miHash.get("numeroRemesa").toString();
				if (miHash.get("prefijoRemesa") != null) 
					prefijoRemesa=miHash.get("prefijoRemesa").toString();
				if (miHash.get("sufijoRemesa") != null) 
					sufijoRemesa=miHash.get("sufijoRemesa").toString();
				
				if (miHash.get(ScsEJGBean.C_IDPONENTE)!= null)
					vPonente.add(0, miHash.get(ScsEJGBean.C_IDPONENTE).toString());			
				
				if (miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD)!=null&&miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD).equals("")) {
					if (miHash.get(ScsEJGBean.C_CALIDADIDINSTITUCION)!=null) {
						calidadidinstitucion	=  miHash.get(ScsEJGBean.C_CALIDADIDINSTITUCION).toString();
						idcalidad = miHash.get(ScsEJGBean.C_IDTIPOENCALIDAD).toString();
						calidadSel.add(0,idcalidad+","+calidadidinstitucion);
					}
				} else { 
					if (!calidad.equals("")&& calidad!=null)
						calidadSel.add(0,calidad+","+miHash.get(ScsEJGBean.C_IDINSTITUCION));
				}
				
				if (miHash.get("ESTADOEJG") != null) 
					idEstado.add(miHash.get("ESTADOEJG").toString());
				
				//if (miHash.get("IDTIPORATIFICACIONEJG") != null) 
				//	idResolucion.add(miHash.get("IDTIPORATIFICACIONEJG").toString());
				
				if (miHash.containsKey("IDTIPORATIFICACIONEJG")) {
					idTipoRatificacionEjg = miHash.get("IDTIPORATIFICACIONEJG").toString();
					if (idTipoRatificacionEjg != null && !"".equals(idTipoRatificacionEjg)){
						String[] idTipoRatificacion = idTipoRatificacionEjg.trim().split(",");
						if (idTipoRatificacion.length > 0){
							vTipoRatificacion.add(idTipoRatificacion[0]);
						} else {
							vTipoRatificacion.add("");
						}
					}
				}	
				if (miHash.get("tiposResolucionBusqueda") != null) 
					tiposResolucionBusqueda = (String)miHash.get("tiposResolucionBusqueda");
				
				if (miHash.get("GUARDIATURNO_IDTURNO") != null) {
					String identificadorTurno = miHash.get("GUARDIATURNO_IDTURNO").toString();
					//idTurno.add(identificadorTurno.equals("")? "0": (String)usr.getLocation() + "," + identificadorTurno);
					if (!identificadorTurno.equals("")){
						idTurno.add("{\"idinstitucion\":\""+(String)miHash.get(ScsEJGBean.C_IDINSTITUCION)+"\",\"idturno\":\"" +identificadorTurno+"\"}");
					}
				}
		
				if (miHash.get("GUARDIATURNO_IDGUARDIA") != null)
					idGuardia.add(miHash.get("GUARDIATURNO_IDGUARDIA").toString());
		
				if (miHash.get(ScsTipoEJGBean.C_IDTIPOEJG) != null)
					idTipoEJG.add(miHash.get(ScsTipoEJGBean.C_IDTIPOEJG).toString());
		
				if (miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO) != null)
					idTipoEJGColegio.add(miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO).toString());	
				
				if (miHash.get(ScsEJGBean.C_FECHAAPERTURA) != null)
					fechaApertura = miHash.get(ScsEJGBean.C_FECHAAPERTURA).toString();
				
				if (miHash.get(ScsEJGBean.C_FECHAAPERTURA + "_HASTA") != null)
					fechaAperturaHasta = miHash.get(ScsEJGBean.C_FECHAAPERTURA + "_HASTA").toString();
				
				if (miHash.get("FECHAESTADO") != null)
					fechaEstadoDesde = miHash.get("FECHAESTADO").toString();
				
				if (miHash.get("FECHAESTADO_HASTA") != null)
					fechaEstadoHasta = miHash.get("FECHAESTADO_HASTA").toString();
				
				if (miHash.get(ScsEJGBean.C_FECHALIMITEPRESENTACION) != null)
					fechaLimiteDesde = miHash.get(ScsEJGBean.C_FECHALIMITEPRESENTACION).toString();
				
				if (miHash.get(ScsEJGBean.C_FECHALIMITEPRESENTACION + "_HASTA") != null)
					fechaLimiteHasta = miHash.get(ScsEJGBean.C_FECHALIMITEPRESENTACION + "_HASTA").toString();
				
				if (miHash.get(ScsEJGBean.C_FECHADICTAMEN) != null)
					fechaDictamenDesde = miHash.get(ScsEJGBean.C_FECHADICTAMEN).toString();
				
				if (miHash.get(ScsEJGBean.C_FECHADICTAMEN + "_HASTA") != null)
					fechaDictamenHasta = miHash.get(ScsEJGBean.C_FECHADICTAMEN + "_HASTA").toString();
				
				if (miHash.get(ScsEJGBean.C_FECHAPRESENTACIONPONENTE) != null)
					fechaPonenteDesde = miHash.get(ScsEJGBean.C_FECHAPRESENTACIONPONENTE).toString();
				
				if (miHash.get(ScsEJGBean.C_FECHAPRESENTACIONPONENTE + "_HASTA") != null)
					fechaPonenteHasta = miHash.get(ScsEJGBean.C_FECHAPRESENTACIONPONENTE + "_HASTA").toString();
				
				if (miHash.get(ScsEJGBean.C_IDTIPODICTAMENEJG) != null)
					idTipoDictamen.add(miHash.get(ScsEJGBean.C_IDTIPODICTAMENEJG).toString());	
				
				if (miHash.get("DICTAMINADO") != null)
					sDictaminado = miHash.get("DICTAMINADO").toString();				
								
				if (miHash.containsKey("IDFUNDAMENTOJURIDICO")) {
					String idFundamentoJuridico=miHash.get("IDFUNDAMENTOJURIDICO").toString();
					vFundamentoJuridico.add(idFundamentoJuridico.equals("")? "": idFundamentoJuridico);
				}	
				
				if (miHash.get(ScsEJGBean.C_PRECEPTIVO) != null) 
					idPreceptivo.add(miHash.get(ScsEJGBean.C_PRECEPTIVO).toString());
				if (miHash.get(ScsEJGBean.C_IDINSTITUCION) != null){
					idInstitucionSelected.add(miHash.get(ScsEJGBean.C_IDINSTITUCION).toString());
				}
				if (miHash.get("chkBusquedaExactaSolicitante") != null){
					chkBusquedaExactaSolicitante = (Boolean)miHash.get("chkBusquedaExactaSolicitante");
				}
				
				
				
			} else {
				if (miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG) != null)
					idEstado.add(miHash.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString());
				
				if (miHash.get(ScsTurnoBean.C_IDTURNO) != null){
					idTurno.add("{\"idinstitucion\":\""+(String)miHash.get(ScsEJGBean.C_IDINSTITUCION)+"\",\"idturno\":\"" +miHash.get(ScsTurnoBean.C_IDTURNO).toString()+"\"}");
				}
		
				if (miHash.get(ScsGuardiasTurnoBean.C_IDGUARDIA) != null)
					idGuardia.add(miHash.get(ScsGuardiasTurnoBean.C_IDGUARDIA).toString());
		
				if (miHash.get(ScsTipoEJGBean.C_IDTIPOEJG) != null)
					idTipoEJG.add(miHash.get(ScsTipoEJGBean.C_IDTIPOEJG).toString());
		
				if (miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO) != null)
					idTipoEJGColegio.add(miHash.get(ScsTipoEJGColegioBean.C_IDTIPOEJGCOLEGIO).toString());	
				
				 
			}
		} 
		catch (Exception e) {
			System.out.println("pepe"+e.toString());
		}
	}
%>



<!-- HEAD -->
 	<link rel="stylesheet" type="text/css" href="<html:rewrite page='/html/dropdownchecklist/smoothness-1.8.13/jquery-ui-1.8.13.custom.css'/>">
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>   
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
		

	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">

    <style>
		table td { vertical-align: center }
		dd { padding-bottom: 15px }
		.ui-dialog-titlebar-close {visibility: hidden;}
    </style>

	   
	 <script type="text/javascript">
	 jQuery.noConflict();
    	    
		function refrescarLocal() {	
			buscar();
		}		
		
		function buscarCliente () {
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
			if (resultado != null && resultado[2]!=null) {
				document.forms[0].idPersona.value=resultado[2];
			}
		}
		
		function inicio(){
		  fin();
		 <% if (busquedaRealizada.equals("1")) {%>
		      buscarPaginador();
		 <%}%>
			//aalg. INC_10622_SIGA. Que aparezca seleccionado todo lo que estaba seleccionado antes de volver
			//Solicitante
		 	var visibleSolicitante = false;
			jQuery("#gratuitabusquedaEJGliteralsolicitante").find('td input').each(function () {
				if (jQuery(this).val() != "" && jQuery(this).attr('type')!='checkbox')
					visibleSolicitante = true;
			});	 	
		 	
		 	if (visibleSolicitante == true)
		 		ocultarDIV('gratuitabusquedaEJGliteralsolicitante');
		 	//Defensa
		 	var visibleDefensa = false;
			jQuery("#gratuitabusquedaEJGliteraldefensa").find('td input').each(function () {
				if (jQuery(this).val() != "")
					visibleDefensa = true;
			});
			if (visibleDefensa == false){
				jQuery("#gratuitabusquedaEJGliteraldefensa").find('td select').each(function () {
					if (jQuery(this).val() != "")
						visibleDefensa = true;
				});
			}
		 	if (visibleDefensa == true)
		 		ocultarDIV('gratuitabusquedaEJGliteraldefensa');
		}
		
		function seleccionarTodos(pagina) {
				document.forms[0].seleccionarTodos.value = pagina;
				buscar('buscarPor');				
		}

		function consultas() {		
			document.RecuperarConsultasForm.submit();			
		}
		
		// Funcion que obtiene el juzgado buscando por codigo externo
		/*
		 function obtenerJuzgado(){ 
			  if (document.forms[0].codigoExtJuzgado.value!=""){
 				   document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
				   document.MantenimientoJuzgadoForm.codigoExt2.value=document.forms[0].codigoExtJuzgado.value;
				   document.MantenimientoJuzgadoForm.submit();		
			 } else
		 		seleccionComboSiga("juzgado",-1);
		}
		*/
		
		function traspasoDatos(resultado){
			if (resultado[0]==undefined) {
				seleccionComboSiga("juzgado",-1);
				document.getElementById("codigoExtJuzgado").value = "";
			} else
				seleccionComboSiga("juzgado",resultado[0]);				 
		}	
	
	function onchangeTipoResolucion() {
		<% if(esComision){%>	
	
		
	
		comboTipoResolucion = document.getElementById('idTipoResolucionEJG');
		var idInstitucion = document.forms['DefinirEJGForm'].idInstitucion.value;
		if(document.getElementById('idFundamentoJuridico')){
			elementsTipoResolucion =  comboTipoResolucion.value;
			var comboFundamentos = document.getElementById('idFundamentoJuridico');
			
			
			var optioncomboFundamentos = comboFundamentos.options;
			//if(elementsTipoResolucion  && jQuery(comboTipoResolucion).val().toString()!='-1'){
			if(elementsTipoResolucion && elementsTipoResolucion.toString()!='-1'){
				if(comboTipoResolucion.value!=""){
					jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
			   			type: "POST",
						url: "/SIGA/GEN_Juzgados.do?modo=getAjaxTiposFundamento",
						data: "idCombo="+elementsTipoResolucion+"##"+idInstitucion,
						dataType: "json",
						success: function(json){		
							var fundamentos = json.fundamentos;
								optioncomboFundamentos.length = 0;
								//jQuery("#idFundamentoJuridico").append("<option  value=''>&nbsp;</option>");
																
								var option = document.createElement("option");
								option.text = "";
								option.value = "";
								comboFundamentos.appendChild(option);

								
		           				jQuery.each(fundamentos, function(i,item2){
		           					seleccionado = '';
		           					if(document.forms[0].idTipoFundamento.value==item2.idFundamento)
		           						seleccionado = 'selected';
			                        //jQuery("#idFundamentoJuridico").append("<option "+seleccionado+" value='"+item2.idFundamento+"'>"+item2.descripcion+"</option>");
									
									var option = document.createElement("option");
									option.text = item2.descripcion;
									option.value = item2.idFundamento;
									option.selected = seleccionado;
									comboFundamentos.appendChild(option);
			                        
			                    });
						},
						error: function(e){
							alert('Error de comunicaci�n:eval(e):' + eval(e));
							fin();
						}
					});
				}
				else{
					optioncomboFundamentos.length = 0;
				}
			}else{
				optioncomboFundamentos.length = 0;
				
			}
		}
		<% }%>
	}	
	jQuery(function($){
		jQuery("#nig").mask("AAAAA AA A AAAA AAAAAAA");
		jQuery("#nig").keyup();	
	});	
	</script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<%
		if(ventanaCajg.equalsIgnoreCase("0")) {
			accion="/JGR_EJG.do?noReset=true";
			formulario="DefinirEJGForm";
	%>
		
		<siga:TituloExt titulo="gratuita.busquedaEJG.literal.expedientesEJG" localizacion="gratuita.busquedaEJG.localizacion"/>
			
	<%
		} else if(ventanaCajg.equalsIgnoreCase("1")) {	
			accion="/JGR_E-Comunicaciones_Seleccion.do?noReset=true";
			formulario="BusquedaCAJG_EJGForm";
	%>
		<siga:Titulo titulo="gratuita.busquedaEJG.literal.expedientesEJG" localizacion="gratuita.busquedaEJG_CAJG.localizacion"/>
		
	<%
		} else if(ventanaCajg.equalsIgnoreCase("2")) {
			accion="/JGR_E-Comunicaciones_Gestion.do?noReset=true";
			formulario="DefinicionRemesas_CAJG_Form";
	%>
		<siga:Titulo titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa.ExpedientesListos" localizacion="gratuita.BusquedaRemesas.a�adir.localizacion"/>
	<%	} else if(ventanaCajg.equalsIgnoreCase("3")) {
			accion="/JGR_E-Comunicaciones_EJGPendientes.do?noReset=true";
			formulario="BusquedaCAJG_EJGForm";
		%>
		<siga:Titulo titulo="menu.sjcs.ecomunicaciones.EJGPteEnvioActualizacion" localizacion="gratuita.busquedaEJG_CAJG.localizacion"/>
	<%	} else if(ventanaCajg.equalsIgnoreCase("4")) {
			accion="/JGR_E-Comunicaciones_InfEconomico.do?noReset=true";
			formulario="DefinicionRemesas_CAJG_Form";
		%>
		<siga:Titulo titulo="menu.sjcs.ecomunicaciones.InfEconomico" localizacion="gratuita.busquedaEJG_CAJG.localizacion"/>
	<% }%>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body id="body" onLoad="inicio();ajusteAlto('resultado');onchangeTipoResolucion();" >
<!--bean:define id="permisoEejg" scope="request" name="permisoEejg" type="java.lang.Boolean"/-->

<bean:define id="path" name="org.apache.struts.action.mapping.instance"	property="path" scope="request" />
<%
ArrayList tiposResolucion = null;
if(usr.isComision()){
	tiposResolucion = (ArrayList)request.getAttribute("tiposResolucion");

} %>
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="<%=accion %>" method="POST" target="resultado">
		<html:hidden name="<%=formulario%>" property="idPersona" value=""></html:hidden> <!-- 0 -->
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
		<html:hidden property = "descripcionEstado" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<html:hidden property = "numero" value = ""/>
		<html:hidden property = "selDefinitivo" value = ""/>
		<html:hidden property = "idRemesa" value = "<%=idremesa%>"/>
		<input type="hidden" name="volver" value="">
		<html:hidden property="seleccionarTodos" />
		<input type="hidden" id="tablaDatosDinamicosD" />	
		<input type="hidden" id="filaSelD" />		
		<html:hidden property = "idTipoResolucion" />
		<html:hidden property = "idTipoFundamento" />	
		<html:hidden property = "valorBusquedaExactaSolicitante" />
		<html:hidden property = "idNuevoEstado"/>
		<html:hidden property = "observaciones" value = ""/>
				
		
		
		

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.EJG">
	
	
	
		<table align="center" width="100%" border="0" cellpadding="5" cellspacing="0">
		<%if(esComisionMultiple){ %>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
					
				</td>				
				<td colspan="5">
				<siga:Select id="idInstitucionComision" queryParamId="idInstitucion" params="<%=paramsInstitucionComision%>"  queryId="getInstitucionesComision" selectedIds="<%=idInstitucionSelected %>" childrenIds="idTipoEJGColegio,juzgado,calidad,identificador,idTipoDictamenEJG"/>
				</td>
			</tr>			
		<%}else{%>
			<html:hidden property="idInstitucionComision" value="<%=idInstitucionComision%>"/>
		<%}%>
		
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.anyo" />/<siga:Idioma key="gratuita.busquedaEJG.literal.codigo" />
				</td>
				
				<td colspan="5" style="padding:0px" style="vertical-align:middle">
					<table border="0" cellpadding="5" cellspacing="0">
						<tr>
							<td style="vertical-align:middle">
								<html:text name="<%=formulario%>" styleId="anio" styleClass="box" property="anio"  value="<%=anio%>" style="width:40px" maxlength="4"></html:text>
								&nbsp;/&nbsp;
								<html:text name="<%=formulario%>" styleId="numEJG" styleClass="box" property="numEJG" value="<%=numEJG%>" style="width:90px" maxlength="13"> </html:text>
							</td>
						
							<td class="labelText" style="vertical-align:middle">
								<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG" />
							</td>
							<td style="vertical-align:middle">
								<siga:Select id="idTipoEJG" queryId="getTiposEjg" width="200" selectedIds="<%=idTipoEJG %>"/>
							</td>
							
							<td class="labelText" style="vertical-align:middle">
								<siga:Idioma key="gratuita.busquedaEJG.literal.EJGColegio" />
							</td>
							
							<td style="vertical-align:middle">
								<siga:Select id="idTipoEJGColegio" queryId="getTiposEjgColegio" width="200" selectedIds="<%=idTipoEJGColegio %>" />
							</td>				
							
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaSOJ.literal.creadoDesde" />
				</td>
				<td style="vertical-align:middle">
					<select id="creadoDesde" name="creadoDesde" class="box">
						<option value=""></option>
						<option value="A" <%if (creadoDesde.startsWith("A")) {%> selected <%}%>>ASISTENCIA</option>
						<option value="D" <%if (creadoDesde.startsWith("D")) {%> selected <%}%>>DESIGNA</option>
						<option value="S" <%if (creadoDesde.startsWith("S")) {%> selected <%}%>>SOJ</option>
						<option value="M" <%if (creadoDesde.startsWith("M")) {%> selected <%}%>>MANUAL</option>
					</select>
				</td>				
		
				<td class="labelText" style="vertical-align:middle" width="160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaAperturaDesde" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaAperturaDesde" valorInicial="<%=fechaApertura%>" anchoTextField="8"/> 
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaAperturaHasta" valorInicial="<%=fechaAperturaHasta%>" campoCargarFechaDesde="fechaAperturaDesde" anchoTextField="8"/> 
				</td>
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.estadoEJG" />
				</td>
				<td style="vertical-align:middle">
					<% if(esComision){%>
						<siga:Select id="estadoEJG" queryId="getEstadosEjgComision" selectedIds="<%=idEstado%>"  />
					<% }else{ %>
						<siga:Select id="estadoEJG" queryId="getEstadosEjg" selectedIds="<%=idEstado%>" />
					<% } %>
				</td>
				<% if(!ventanaCajg.equalsIgnoreCase("3")) {%>
				<td class="labelText" style="vertical-align:middle" width="160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaEstadoDesde" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaEstadoDesde" valorInicial="<%=fechaEstadoDesde%>" anchoTextField="8" /> 
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaEstadoHasta" valorInicial="<%=fechaEstadoHasta%>" campoCargarFechaDesde="fechaEstadoDesde" anchoTextField="8"/> 
				</td>
				<%}else{%>
				<td colspan ="4" >
					<html:hidden property="fechaEstadoDesde"/>
					<html:hidden property="fechaEstadoHasta"/>
				&nbsp;</td>
				<%}%>
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.resolucion"/>
				</td>				
				<td style="vertical-align:middle">
					<%if(esComision){%>
						<select id="idTipoResolucionEJG" styleClass="boxCombo" multiple="multiple" onchange="onchangeTipoResolucion();" style="width:375px;">
							<% for (int i = 0; i < tiposResolucion.size(); i++) {
								ScsTiporesolucion  resolucion = (ScsTiporesolucion)tiposResolucion.get(i);
								String seleccionado = "";
								if(tiposResolucionBusqueda!=null && !tiposResolucionBusqueda.equals("")){
									List alIds = Arrays.asList(tiposResolucionBusqueda.split(",")) ;
									if(resolucion.getIdtiporesolucion()!=null)
										seleccionado=alIds.contains(resolucion.getIdtiporesolucion().toString())?"selected":"";
									
								}
							%>
								<option <%=seleccionado%> value="<%=resolucion.getIdtiporesolucion()!=null?resolucion.getIdtiporesolucion():"" %>" ><c:out value="<%=resolucion.getDescripcion() %>"/> </option>
							<%} %>	
						</select>
				<%}else{%>
					<siga:Select id="idTipoRatificacionEJG" queryParamId="idtiporesolucion" queryId="getTiposResolucionTodos" selectedIds="<%=vTipoRatificacion%>" width="375" />
				<%}%>	
				</td>
													
				<td class="labelText" style="vertical-align:middle" width="160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaLimiteDesde" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaLimitePresentacionDesde" valorInicial="<%=fechaLimiteDesde%>" anchoTextField="8" /> 
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaLimitePresentacionHasta" valorInicial="<%=fechaLimiteHasta%>" campoCargarFechaDesde="fechaLimitePresentacionDesde" anchoTextField="8" /> 
				</td>			
			</tr>
			
			<%if(esComision){%>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.operarRatificacion.literal.fundamentoJuridico"/>
					</td>
					
					
					<td colspan="4">
						<select style="width:700px;" id="idFundamentoJuridico" class="boxCombo">
								<option value="">&nbsp;</option>
						</select>
					
					</td>
				</tr>
			<%}%>
			
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.dictaminado" />
				</td>
				<td style="padding:0px" style="vertical-align:middle">
					<table border="0" cellpadding="5" cellspacing="0">
						<tr>
							<td style="vertical-align:middle">
								<select id="dictaminado" name="dictaminado" class="boxCombo">
									<option value="S" <%if (sDictaminado.equalsIgnoreCase("S")) {%> selected <%}%>><siga:Idioma key="gratuita.busquedaSOJ.literal.si" /></option>
									<option value="N" <%if (sDictaminado.equalsIgnoreCase("N")) {%> selected <%}%>><siga:Idioma key="gratuita.busquedaSOJ.literal.no" /></option>
									<option value="I" <%if (sDictaminado.equalsIgnoreCase("I")) {%> selected <%}%>><siga:Idioma key="gratuita.busquedaSOJ.literal.indiferente" /></option>				
								</select>
							</td>
					
							<td class="labelText" style="vertical-align:middle">
								<siga:Idioma key="gratuita.busquedaEJG.dictamen"/>
							</td>
							<td style="vertical-align:middle">
								<siga:Select id="idTipoDictamenEJG" queryId="getTiposDictamenEjg" selectedIds="<%=idTipoDictamen%>" width="140" />
							</td>									
						</tr>
					</table>
				</td>
				
				<td class="labelText" style="vertical-align:middle" width="160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.fechaDictamenDesde"  />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaDictamenDesde" valorInicial="<%=fechaDictamenDesde%>" anchoTextField="8"/> 
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.hasta" />
				</td>
				<td width="100px" style="vertical-align:middle"> 
					<siga:Fecha nombreCampo="fechaDictamenHasta" valorInicial="<%=fechaDictamenHasta%>" campoCargarFechaDesde="fechaDictamenDesde" anchoTextField="8"/> 
				</td>
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.ponente"/>
				</td>

				<td style="vertical-align:middle">
					<siga:ComboBD nombre="idPonente" ancho="375" tipo="tipoPonenteAll" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=datoPonente%>" elementoSel="<%=vPonente%>"  />
				</td>
				
				<td class="labelText" style="vertical-align:middle" width="160px">
					<siga:Idioma key="gratuita.busquedaEJG.literal.presentacionPonente.desde" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaPresentacionPonenteDesde" valorInicial="<%=fechaPonenteDesde%>" anchoTextField="8"/>
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaEJG.literal.presentacionPonente.hasta" />
				</td>
				<td width="100px" style="vertical-align:middle">
					<siga:Fecha nombreCampo="fechaPresentacionPonenteHasta" valorInicial="<%=fechaPonenteHasta%>" campoCargarFechaDesde="fechaPresentacionPonenteDesde" anchoTextField="8"/> 
				</td>			
			</tr>				

			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/>&nbsp;<siga:Idioma key='gratuita.operarEJG.literal.anio'/>/<siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
				</td>
				<td style="vertical-align:middle">
					<html:text name="<%=formulario%>" styleId="anioCAJG" styleClass="box" property="anioCAJG" style="width:40px" maxlength="4" value="<%=cajgAnio%>" />
					&nbsp;/&nbsp;
					<html:text name="<%=formulario%>" styleId="numeroCAJG" styleClass="box" property="numeroCAJG" value="<%=cajgNumero%>" size="8" maxlength="10" />
				</td>
							
				<td class="labelText" style="vertical-align:middle" width="140px">
					<siga:Idioma key='gratuita.operarEJG.literal.acta'/>&nbsp;<siga:Idioma key='gratuita.operarEJG.literal.anio'/>/<siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
				</td>
				<td colspan="3" style="vertical-align:middle">
					<html:text name="<%=formulario%>" styleId="anioActa" styleClass="box" property="anioActa" style="width:40px" maxlength="4" value="<%=anioActa%>" />
					&nbsp;/&nbsp;
					<html:text name="<%=formulario%>" styleId="numeroActa" styleClass="box" property="numeroActa" value="<%=numActa%>" size="8" maxlength="10" />
				</td>				
			</tr>
			<tr>
				<td class="labelText" style="vertical-align:middle" width="100px">
					<siga:Idioma key="facturacion.consultaDevolucion.literal.identificador"/>
				</td>

				<td style="vertical-align:middle"  >
					<html:text name="<%=formulario%>" property="prefijoRemesa" size="5" maxlength="10" styleClass="box" style="width:45px" readonly="false" value="<%=prefijoRemesa%>" title="Prefijo"/>
					<html:text name="<%=formulario%>" property="numeroRemesa" size="5" maxlength="10" styleClass="box" style="width:65px" readonly="false" value="<%=numeroRemesa%>" title="N�mero"/>
					<html:text name="<%=formulario%>" property="sufijoRemesa" size="5" maxlength="10" styleClass="box" style="width:45px" readonly="false" value="<%=sufijoRemesa%>"  title="Sufijo"/>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	<div id="divTramitador" style="display: none;">
	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloTramitador" desplegable="true" oculto="false" >
		<table align="center" width="100%" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaEJG.literal.turno" />
				</td>					
				<td>
					<siga:Select id="identificador" queryParamId="idturno" queryId="getTurnos" selectedIds="<%=idTurno%>" width="355" childrenIds="guardiaTurnoIdGuardia" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaEJG.literal.guardia" />
				</td>
				<td>
					<% String guardiaTurnoIdGuardiaParam = ""; if (idTurno != null && idTurno.size() > 0) guardiaTurnoIdGuardiaParam = idTurno.iterator().next().toString(); %>
					<siga:Select id="guardiaTurnoIdGuardia" queryId="getGuardiasDeTurno" params="<%=guardiaTurnoIdGuardiaParam %>" selectedIds="<%=idGuardia%>" width="355" parentQueryParamIds="idturno" />
				</td>
			</tr>
			
			<tr>
				<td colspan="4" style="padding:0px">
					<siga:BusquedaPersona tipo="colegiado" idPersona="idPersona"></siga:BusquedaPersona>
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	</div>
	<%if(!esComision){ %>
		<script language="JavaScript">
			jQuery('#divTramitador').show();
		</script>
	<%} %>
	
	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.defensa"  desplegable="true" oculto="true">
		<table align="center" width="100%" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.personaJG.literal.calidad" />
				</td>
				<td>
					<siga:Select id="calidad" queryId="getTiposCalidades" selectedIds="<%=calidadSel%>" width="130" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key='gratuita.operarEJG.literal.Preceptivo'/>
				</td>
				<td >
					<siga:ComboBD nombre="idPreceptivo" tipo="comboPreceptivo" ancho="200" elementoSel="<%=idPreceptivo%>" clase="boxCombo"/>
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.operarEJG.literal.renuncia" />
				</td>
				<td colspan="2">
					<siga:Select id="idRenuncia" queryId="getRenuncias" selectedIds="<%=renunciaSel%>" width="180" />
				</td>
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.mantAsistencias.literal.juzgado" />
				</td>				
				<td colspan="6">
				
					<siga:Select id="juzgado" queryId="getJuzgados" selectedIds="<%=juzgado%>" width="500" showSearchBox="true" searchkey="CODIGOEXT2" searchBoxMaxLength="10" searchBoxWidth="10" />
				
				</td>
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="informes.cartaAsistencia.procedimiento" />
				</td>
				<td>
					<html:text styleId="procedimiento" name="<%=formulario%>" property="procedimiento" size="14" maxlength="100" styleClass="box" value="<%=procedimiento%>" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="informes.cartaAsistencia.asunto" />
				</td>
				<td>
					<html:text styleId="asunto" name="<%=formulario%>" property="asunto" size="20" maxlength="100" styleClass="box" value="<%=asunto%>" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">	
					<siga:Idioma key="gratuita.mantAsistencias.literal.NIG"/>
				</td>
					
				<td>
					<html:text name="<%=formulario%>" property="nig" styleId="nig" styleClass="box" value="<%=sNig%>" style="size:19;width:200px"/>
				</td>	
			</tr>
		</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.solicitante" desplegable="true" oculto="true">
		<table align="center" width="100%" border="0" cellpadding="5" cellspacing="0">
			<tr>
				<td width="20%"></td>
				<td width="5%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="15%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				
			</tr>
			
			<tr>
				<td class="labelText" style="vertical-align: middle">
					<siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda" /> 
					<html:checkbox styleId="chkBusquedaExactaSolicitante" name="<%=formulario%>" property="chkBusquedaExactaSolicitante" />
				</td>

				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaEJG.literal.nif" />
				</td>
				<td>
					<html:text styleId="nif" name="<%=formulario%>" property="nif" size="10" maxlength="20" styleClass="box" value="<%=nif%>" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaEJG.literal.nombre" /> 
				</td>				
				<td>
					<html:text styleId="nombre" name="<%=formulario%>" property="nombre" size="15" maxlength="100" styleClass="box" value="<%=nombre%>" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido1" /> 
				</td>				
				<td>
					<html:text styleId="apellido1" name="<%=formulario%>" property="apellido1" size="15" maxlength="100" styleClass="box" value="<%=apellido1%>" />
				</td>
				
				<td class="labelText" style="vertical-align:middle">
					<siga:Idioma key="gratuita.busquedaAsistencias.literal.apellido2" /> 
				</td>
				
				<td>
					<html:text styleId="apellido2" name="<%=formulario%>" property="apellido2" size="15" maxlength="100" styleClass="box" value="<%=apellido2%>" />
				</td>
			</tr>
		</table>
	</siga:ConjCampos>
	
	<div id="cambioEstado" title="Cambiar estado" style="display:none">
		<div>
		  	<p class="labelTextValue"><siga:Idioma key='gratuita.busquedaEJG.seleccioneEstado'/></p>
			<siga:Select id="idNuevoEstado" queryId="getEstadosEjg" width="300px"/>
		</div>
		<div>
		  	<p class="labelTextValue"><siga:Idioma key='pestana.justiciagratuitaejg.observaciones'/></p>
			<html:textarea styleId="observaciones" name="<%=formulario%>" property="observaciones" size="60" rows="5" maxlength="4000" styleClass="box" value="" />
		</div>
	</div>

</html:form>

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>	
	<html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea21">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
		<html:hidden property = "nombreObjetoDestino" value=""/>
	</html:form>
		
	<html:form action="/JGR_UnidadFamiliarEEJG"  method="post" target="submitArea21">
		<html:hidden property="modo"/>
		<html:hidden property="datosInforme"/>
	</html:form>
	
	<html:form action="/CON_RecuperarConsultas" method="POST" target="mainWorkArea">
		<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_SJCS%>"/>
		<html:hidden property="modo" value="inicio"/>
		<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>	

<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
<!-- INICIO: BOTONES BUSQUEDA -->	
	<%if(ventanaCajg.equalsIgnoreCase("0")){%>
		<%if(!esComision){%>
			<%if(permisoEejg){ %>
				<siga:ConjBotonesBusqueda botones="L,C,N,B,DEE, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
			<%}else{ %>
				<siga:ConjBotonesBusqueda botones="L,C,N,B, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
			<%} %>
		<%}else{ %>
			<%if(permisoEejg){ 
				if(accesoActa){	%>
					<siga:ConjBotonesBusqueda botones="L,C,B,DEE,ES, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
			
				<%}else{%>
					<siga:ConjBotonesBusqueda botones="L,C,B,DEE,ES, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
			
				<%}
			}else{
				if(accesoActa){	%>
					<siga:ConjBotonesBusqueda botones="L,C,B,ES, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
				<%}else{ %>
				<siga:ConjBotonesBusqueda botones="L,C,B,ES, CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
				<%} %>
			<%} 
		}%>
	<%}else if(ventanaCajg.equalsIgnoreCase("1")){%> <!-- Antiguo busquedaEJG_Cajg -->
		<siga:ConjBotonesBusqueda botones="L,le,B,ces,CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
	<%}else if(ventanaCajg.equalsIgnoreCase("2")){%> <!-- Antiguo busquedaEJG_Listos -->
		<siga:ConjBotonesBusqueda botones="L,B,ar,CON"  titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa.ExpedientesListos" />
	<%}else if(ventanaCajg.equalsIgnoreCase("3")){%> <!-- Antiguo busquedaEJG_Cajg -->
		<siga:ConjBotonesBusqueda botones="L,le,B,CON"  titulo="gratuita.busquedaEJG.literal.expedientesEJG" />
	<%}else if(ventanaCajg.equalsIgnoreCase("4")){%> <!-- Antiguo busquedaEJG_Cajg -->
		<siga:ConjBotonesBusqueda botones="L,B,ar,CON"  titulo="gratuita.BusquedaRemesas_CAJG.literal.Remesa.ExpedientesListos" />
	<%}%>
<!-- FIN: BOTONES BUSQUEDA -->	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function ocultarConjunto(b) {
			if (b == 1){
				
				document.getElementById("fieldset1").style.display='none';
				//document.getElementById("anio 1").value='(-)Expediente Justicia Gratuita';
				document.getElementById("fieldset2").style.display='block';
				//document.getElementById("anio 3").value='(+)Expediente Justicia Gratuita';
				
			} else {				
				document.getElementById("fieldset1").style.display='block';
				//document.getElementById("anio 1").value='(-)Expediente Justicia Gratuita';
				document.getElementById("fieldset2").style.display='none';
				//document.getElementById("anio 3").value='(+)Expediente Justicia Gratuita';			
			}
		}		
	
	//<!-- Funcion asociada a boton buscar -->
	function buscarPaginador(){		
		
			document.forms[0].modo.value = "buscarPor";				
			<%if(ventanaCajg.equalsIgnoreCase("2")||ventanaCajg.equalsIgnoreCase("4")){%>
				document.forms[0].modo.value = "buscarListos";
				document.forms[0].idRemesa.value=<%=idremesa%>;
			<%}%>
			
			/* El idenficiador est� compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos s�lo
			con el turno, ya que la instituci�n se recoger� del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias
			*/
			<%if (!esComision){%>
				document.forms[0].descripcionEstado.value = document.forms[0].estadoEJG[document.forms[0].estadoEJG.selectedIndex].text;
			<% } %>

			document.forms[0].guardiaTurnoIdTurno.value = document.forms[0].identificador.value;
			if (isNaN(document.forms[0].anio.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
			}else if (isNaN(document.forms[0].idPersona.value)) {
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
			}else{
				
			
				document.forms[0].submit();
			}
		}		
		
	function buscar(){ 
		
		var numeroEJG = document.forms[0].numEJG.value;
		numEJGS = numeroEJG.split(',');
		
		if(numEJGS.length>1){
			for ( var i = 0; i < numEJGS.length; i++) {
				if(!isNumero(numEJGS[i])){
					error = "<siga:Idioma key='errors.integer' arg0='gratuita.busquedaEJG.literal.codigo'/>";
					alert(error);
					return;
				}
			}
		}else{
		
			numEJGS = numeroEJG.split('-');
			if(numEJGS.length>1){
				for ( var i = 0; i < numEJGS.length; i++) {
					if(!isNumero(numEJGS[i])){
						error = "<siga:Idioma key='errors.integer' arg0='gratuita.busquedaEJG.literal.codigo'/>";
						alert(error);
						return;
					}
				}
			}
		}
		
		if (document.getElementById('chkBusquedaExactaSolicitante').checked){
	    	document.forms[0].valorBusquedaExactaSolicitante.value="1";
	    }else{
			document.forms[0].valorBusquedaExactaSolicitante.value="0";
		}
			
			
		var tipoResolObj = document.getElementById("idTipoResolucionEJG");//jQuery("#idTipoResolucionEJG").val();
		
		 		 
		if(tipoResolObj){
			
			var tipoResol = new Array();
			var i;
			var count = 0;
			for (i=0; i < tipoResolObj.options.length; i++) { 
				if (tipoResolObj.options[i].selected) {
					tipoResol[count] = tipoResolObj.options[i].value;
					count++; 
				} 
			}
						
			
			if(true){
				if(tipoResol.toString().substring(0,1)==','){
					tipoResol = tipoResol.toString().substring(1);
				}
				document.forms[0].idTipoResolucion.value = tipoResol;
			}
		}else{
			document.forms[0].idTipoResolucion.value = "";
				
		}
		var nigAux = document.getElementById("nig").value;
		nigAux = ready2ApplyMask(nigAux);
		nigAux = nigAux.toUpperCase();
		if(!validarNig(nigAux)){	
			alert("<siga:Idioma key='gratuita.nig.formato'/>");
			return false;
		}
		document.forms[0].nig.value = nigAux; 
		
			
		if(document.getElementById("idFundamentoJuridico"))
			document.forms[0].idTipoFundamento.value = document.getElementById("idFundamentoJuridico").value;
			
			
			
		if ( document.getElementById("anio") && !validarObjetoAnio(document.getElementById("anio")) ){
			alert("<siga:Idioma key='fecha.error.anio'/>");
			return false;
		}
		if ( document.getElementById("anioCAJG") && !validarObjetoAnio(document.getElementById("anioCAJG")) ){
			alert("<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='fecha.error.anio'/>");
			return false;
		}
		if((validarFecha(document.forms[0].fechaAperturaHasta.value))&&
			   (validarFecha(document.forms[0].fechaAperturaDesde.value))&&
			   (validarFecha(document.forms[0].fechaEstadoHasta.value))&&
			   (validarFecha(document.forms[0].fechaEstadoDesde.value))&&
			   (validarFecha(document.forms[0].fechaDictamenHasta.value))&&
			   (validarFecha(document.forms[0].fechaDictamenDesde.value))&&
			   (validarFecha(document.forms[0].fechaLimitePresentacionHasta.value))&&
			   (validarFecha(document.forms[0].fechaLimitePresentacionDesde.value))){
				
			sub();
			document.forms[0].modo.value = "buscarInit";
					
			<%if(ventanaCajg.equalsIgnoreCase("2")||ventanaCajg.equalsIgnoreCase("4")){%>
					document.forms[0].modo.value = "buscarListosInicio";
					document.forms[0].idRemesa.value=<%=idremesa%>;			
			<%}%>
						
			<%if (!esComision){%>
					document.forms[0].descripcionEstado.value = document.forms[0].estadoEJG[document.forms[0].estadoEJG.selectedIndex].text;
			<%}%>

			document.forms[0].guardiaTurnoIdTurno.value = document.forms[0].identificador.value;
			if (isNaN(document.forms[0].anio.value)) {
				fin();
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
			}else if (isNaN(document.forms[0].idPersona.value)) {
				fin();
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorIdPersona"/>');
			}else{ 
				filtroSeleccionado = false;	
				var inputs = document.getElementsByTagName('input');
				for(var i = 0; i < inputs.length; i++) {
				    if(inputs[i].value != "" && inputs[i].type.toLowerCase() != 'checkbox' && inputs[i].type.toLowerCase() != 'button' && inputs[i].readonly != 'readonly') {
				    	filtroSeleccionado = true;
				    }
				}
				
				if(!filtroSeleccionado){
					var selects = document.getElementsByTagName('select');
					
					for(var i = 0; i < inputs.length; i++) {
					    if(selects[i].value != "" && selects[i].name != 'dictaminado' && selects[i].id() != 'idFundamentoJuridico' && selects[i].id() != 'idTipoResolucionEJG') {
					    	filtroSeleccionado = true;
					    }
					}

				}
				
				/*jQuery("#body").find('td input').each(function () {
					if (jQuery(this).val() != "" && jQuery(this).attr('type')!='checkbox' && jQuery(this).attr('type')!='button' && jQuery(this).attr('readonly')!='readonly'){
						filtroSeleccionado = true;
					}
				});	
				if(!filtroSeleccionado){
					jQuery("#body").find('td select').each(function () {
						if (jQuery(this).val() != "" && jQuery(this).attr("name")!='dictaminado' && jQuery(this).attr("id")!='idFundamentoJuridico'&& jQuery(this).attr("id")!='idTipoResolucionEJG'){

							filtroSeleccionado = true;
						}
					});
				}*/
				if(!filtroSeleccionado){
					if(document.getElementById("dictaminado").value!='I'){
						filtroSeleccionado = true;
					}
					if(document.getElementById("idFundamentoJuridico") && document.getElementById("idFundamentoJuridico").value!=''){
						filtroSeleccionado = true;
					}
					if(document.getElementById("idTipoResolucionEJG") && document.getElementById("idTipoResolucionEJG").value!=''){
						filtroSeleccionado = true;
					}
						
				}
				if(!filtroSeleccionado){
					alert('<siga:Idioma key="errors.filter.required"/>');
					fin();
					return false;
				}else{
					document.forms[0].submit();
				}
			}
		}
		jQuery("#nig").keyup();
	}		
		
		//<!-- Funcion asociada a boton limpiar -->
		function limpiar(){		
			document.forms[0].reset();
		}
		
		//<!-- Funcion asociada a boton Nuevo -->
		function nuevo(){
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"M");
			//if(resultado[0]=='MODIFICADO') buscar();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinirEJGForm){
					numero.value        = resultado[1];
					idTipoEJG.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}
		function generarCarta() {
   			if(window.frames.resultado.ObjArray){   				
 				window.frames.resultado.accionGenerarCarta();
			}
			else {
				alert("<siga:Idioma key='general.message.seleccionar'/>");
				fin();
			}			
		} 	
		
		function accionEditarSel() {
   			if(window.frames.resultado.ObjArray){
 				window.frames.resultado.accionEditarSeleccionados();
			}
		} 
		
		function descargaEejg(){
			<%if(ventanaCajg.equalsIgnoreCase("0")){%>				
				if(window.frames.resultado.ObjArray){
					sub();
			   	 	datos =  window.frames.resultado.getDatosSeleccionados();
			    	if (datos.length==0){
			    		alert("<siga:Idioma key='general.message.seleccionar'/>");
				    	fin();
				    	return false;
					}
				
					document.DefinirUnidadFamiliarEJGForm.datosInforme.value = datos;
		   			document.DefinirUnidadFamiliarEJGForm.modo.value = "descargaMultiplesEejg";
					document.DefinirUnidadFamiliarEJGForm.submit();
 					// window.frames.resultado.accionDescargaEejg();
				}
				else {
					alert("<siga:Idioma key='general.message.seleccionar'/>");
				    fin();
				}
			<%}%>
		}
		
		function aniadirARemesa(){
			<%if(ventanaCajg.equalsIgnoreCase("2")||ventanaCajg.equalsIgnoreCase("4")){%>
				if( window.frames.resultado.document.<%=formulario%>) {
					var datos1 =  window.frames.resultado.document.<%=formulario%>.selDefinitivo;
					if(datos1.value) {
				    	document.forms[0].selDefinitivo.value=datos1.value;
				    	document.forms[0].idRemesa.value=<%=idremesa%>;
				    	document.forms[0].target="mainWorkArea";
						document.forms[0].modo.value = "aniadirARemesa";
						document.forms[0].submit();
					}
				}
			<%}%>
		}

		function accionVolver(){
			<%if(ventanaCajg.equalsIgnoreCase("2")){%>
				document.forms[0].action="./JGR_E-Comunicaciones_Gestion.do";	
				document.forms[0].modo.value="editar";
				document.forms[0].volver.value="SI";
				document.forms[0].idRemesa.value=<%=idremesa%>;
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit(); 
			<%}else if(ventanaCajg.equalsIgnoreCase("4")){%>
				document.forms[0].action="./JGR_E-Comunicaciones_InfEconomico.do";	
				document.forms[0].modo.value="editar";
				document.forms[0].volver.value="SI";
				document.forms[0].idRemesa.value=<%=idremesa%>;
				document.forms[0].target="mainWorkArea"; 
				document.forms[0].submit();
			
			
			<%}%>
			
		}
		
		//<!-- Accion de la busqueda CAJG -->
		function accionListoParaEnviar() {
			if(confirm("<siga:Idioma key='messages.cajg.confirmarListoRemitir'/>")){
				try{
				    var dat = "";
				    var datos1 = window.frames.resultado.document.<%=formulario%>.selDefinitivo;
				    document.forms[0].selDefinitivo.value=datos1.value;
				    
				  	document.forms[0].modo.value = "listosParaComision";
				  	var f = document.forms[0].name;	
				  	//Para quien se encargue del marr�n de la ruedecita, con mucho cari�o para ....
				  	//(JTA)Saludos tambien de mi parte. si quieres ponerlo aqui mira en accionGenerarCarta() de listadoEJG.jspi
		
				    document.forms[0].submit();
				}
				catch (e){
					alert('<siga:Idioma key="messages.cajg.error.listos"/>');
				}
			}
		}
		<% if(chkBusquedaExactaSolicitante){%>
			document.getElementById("chkBusquedaExactaSolicitante").checked="checked";
		<% }else{%>
			document.getElementById("chkBusquedaExactaSolicitante").checked="";
		<% }%>
		
		function limpiar() {
			
			try{
				if (jQuery("#idInstitucionComision").exists()) { // idInstitucionComision
					jQuery("#idInstitucionComision").val("");			
				}
			}catch (e){
				if (document.getElementById("idInstitucionComision") != null) { // idInstitucionComision
					document.getElementById("idInstitucionComision").value="";		
				}
			}
			
			jQuery("#anio").val("<%=anioActual%>"); // anio=Actual
			jQuery("#numEJG").val(""); // numEJG

					
			 // idTipoEJGColegio
			try{
				jQuery("#idTipoEJG").val(""); // idTipoEJG
				jQuery("#idTipoEJGColegio").val("");
				jQuery("#creadoDesde").val(""); // creadoDesde
				jQuery("#estadoEJG").val(""); // estadoEJG
				
			}catch(e){

				document.getElementById("idTipoEJG").value="";
				document.getElementById("idTipoEJGColegio").value="";
				document.getElementById("creadoDesde").value="";
				document.getElementById("estadoEJG").value="";
				
			}
			jQuery("#fechaAperturaDesde").val(""); // fechaAperturaDesde
			jQuery("#fechaAperturaHasta").val(""); // fechaAperturaHasta
			jQuery("#fechaEstadoDesde").val(""); // fechaEstadoDesde
			jQuery("#fechaEstadoHasta").val(""); // fechaEstadoHasta
			
			try{

				if (jQuery("#ddcl-idTipoResolucionEJG-ddw").exists()) { // idTipoResolucionEJG
					var opcionIndiferente = jQuery("#ddcl-idTipoResolucionEJG-i0");
					if (opcionIndiferente.is(':checked')) {
						opcionIndiferente.click(); // Desmarco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
					} else {
						opcionIndiferente.click(); // Marco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
						opcionIndiferente.click(); // Desmarco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
					}
				}
			}catch (e)
			{
				if (jQueryTop("#ddcl-idTipoResolucionEJG-ddw") != null) { // idTipoResolucionEJG
					var opcionIndiferente = document.getElementById("#ddcl-idTipoResolucionEJG-i0");
					if (opcionIndiferente.is(':checked')) {
						opcionIndiferente.click(); // Desmarco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
					} else {
						opcionIndiferente.click(); // Marco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
						opcionIndiferente.click(); // Desmarco todas ... invoca una llamada ajax para cargar los fundamentos juridicos
					}
				}
			} 
						
			try{
				if (jQuery("#idTipoRatificacionEJG").exists()) { // idTipoRatificacionEJG
					jQuery("#idTipoRatificacionEJG").val("");
				}
				
			}
			catch(e){
				if (document.getElementById("idTipoRatificacionEJG") != null) { // idTipoRatificacionEJG
					document.getElementById("idTipoRatificacionEJG").value="";
				}
				
			}

			try{
				if (jQuery("#idFundamentoJuridico").exists()) { // idFundamentoJuridico
					jQuery("#idFundamentoJuridico").val("");
				}

			}catch(e){
				if (document.getElementById("idFundamentoJuridico") != null) { // idTipoRatificacionEJG
					document.getElementById("idFundamentoJuridico").value="";
				}
			}
			
			try{
				
				jQuery("#idTipoDictamenEJG").val(""); // idTipoDictamenEJG
				jQuery("#identificador").val(""); // idturno
				jQuery("#idPonente").val(""); // idPonente
				jQuery("#idPreceptivo").val(""); // idPreceptivo
				jQuery("#idRenuncia").val(""); // idRenuncia	
				jQuery("#guardiaTurnoIdGuardia").val(""); // guardiaTurnoIdGuardia	
				jQuery("#calidad").val(""); // calidad
				jQuery("#juzgado").val(""); // juzgado
				jQuery("#dictaminado").val("I"); // dictaminado=Indiferente	
				
			}
			catch(e){
				
				document.getElementById("idTipoDictamenEJG").value=""; // idTipoDictamenEJG
				document.getElementById("identificador").value=""; // idturno
				document.getElementById("idPonente").value=""; // idPonente
				document.getElementById("idPreceptivo").value=""; // idPreceptivo
				document.getElementById("idRenuncia").value=""; // idRenuncia	
				document.getElementById("guardiaTurnoIdGuardia").value=""; // guardiaTurnoIdGuardia	
				document.getElementById("calidad").value=""; // calidad
				document.getElementById("juzgado").value=""; // juzgado
				document.getElementById("dictaminado").value="I";
			}
			
			jQuery("#fechaLimitePresentacionDesde").val(""); // fechaLimitePresentacionDesde
			jQuery("#fechaLimitePresentacionHasta").val(""); // fechaLimitePresentacionHasta
			jQuery("#fechaDictamenDesde").val(""); // fechaDictamenDesde
			jQuery("#fechaDictamenHasta").val(""); // fechaDictamenHasta
			jQuery("#fechaPresentacionPonenteDesde").val(""); // fechaPresentacionPonenteDesde
			jQuery("#fechaPresentacionPonenteHasta").val(""); // fechaPresentacionPonenteHasta
			
			
			jQuery("#anioCAJG").val(""); // anioCAJG
			jQuery("#numeroCAJG").val(""); // numeroCAJG
			jQuery("#anioActa").val(""); // anioActa
			jQuery("#numeroActa").val(""); // numeroActa
	
			jQuery("#juzgado_searchBox").val(""); // juzgado
	
			limpiarPersona();
			
			jQuery("#procedimiento").val(""); // procedimiento
			jQuery("#asunto").val(""); // asunto
			jQuery("#nig").val(""); // nig
			
			jQuery("#chkBusquedaExactaSolicitante").prop('checked', true); // chkBusquedaExactaSolicitante=Checked
			jQuery("#nif").val(""); // nif
			jQuery("#nombre").val(""); // nombre
			jQuery("#apellido1").val(""); // apellido1
			jQuery("#apellido2").val(""); // apellido2
	}				
		
	function cambiarEstado(){
		
		try{
		    var datos1 = window.frames.resultado.document.<%=formulario%>.selDefinitivo;
		    
		    if(datos1.value!=""){
		    	jQuery("#cambioEstado").dialog(
						{
						      height: 350,
						      width: 525,
						      modal: true,
						      resizable: false,
						      buttons: {
						          	"Cambiar": function() {
						        	  accionCambiarSeleccionados();
						        	  jQuery( this ).dialog( "close" );
						        	  limpiarCambioMasivo();
						            },
						            "Cerrar": function() {
						              jQuery( this ).dialog( "close" );
						              limpiarCambioMasivo();
						            }
						          }
						    }
					);
					jQuery(".ui-widget-overlay").css("opacity","0");
		    }else{
		    	alert("<siga:Idioma key='general.message.seleccionar'/>");
		    }
		}
		catch (e){
			alert("<siga:Idioma key='general.message.seleccionar'/>");
		}
	}
	
	function limpiarCambioMasivo(){
		jQuery("#idNuevoEstado").val('');
		jQuery("#observaciones").val('');
	}
	
	function accionCambiarSeleccionados(){
		if(confirm("<siga:Idioma key='messages.cajg.confirmarCambioEstado'/>")){		
			var datos1 = window.frames.resultado.document.<%=formulario%>.selDefinitivo;
			document.forms[0].idNuevoEstado.value=jQuery("#idNuevoEstado").val();
			document.forms[0].observaciones.value=jQuery("#observaciones").val();
			document.forms[0].selDefinitivo.value=datos1.value;
			document.forms[0].modo.value = "listosCambiarEstado";
			document.forms[0].submit();
		}	
	}
	
	</script>
<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="resultado" name="resultado" scrolling="no" frameborder="0" marginheight="0" marginwidth="0" class="frameGeneral"></iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

	<%if(ventanaCajg.equalsIgnoreCase("2") || ventanaCajg.equalsIgnoreCase("4")){%>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<%} %>
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<iframe name="submitArea21" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>