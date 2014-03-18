<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>
<!-- editarDesigna.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<%
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

	Hashtable resultado = (Hashtable) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");

	String modo = (String) ses.getAttribute("Modo");
	String modoAction=(String) ses.getAttribute("ModoAction");
	String idInstitucionLocation = usr.getLocation();
	String[] dato = { usr.getLocation() };

	String tipo = "", estado = "", fecha = "", procurador = "", asunto = "", observaciones = "", delitos = "", fechaAnulacion = "", fechaEstado = "";
	String fechaApertura = "", fechaOficioJuzgado = "", fechaRecepcionColegio = "", modo1="";
	
	//Hastable letrado= new Hastable();
	ScsDesignaAdm clase = new ScsDesignaAdm(usr);

	String nombre_letrado;
	String nume_colegiado;
	String nume_colegiadoOrigen;
	String datosColegiales;
	String institucionOrigen;
	String idInstitucionOrigen;
	Hashtable letrado = clase.obtenerLetradoDesigna((String) resultado.get("IDINSTITUCION"), (String) resultado.get("IDTURNO"),	(String) resultado.get("ANIO"),	(String) resultado.get("NUMERO"));

	nombre_letrado = (String) letrado.get("NOMBRE");
	nume_colegiado  = (String) letrado.get("NCOLEGIADO");
	datosColegiales = (String) letrado.get("DATOSCOLEGIALES");
	nume_colegiadoOrigen  = (String) letrado.get("NCOLEGIADOORIGEN");	
	idInstitucionOrigen  = (String)letrado.get("IDINSTITUCIONORIGEN");
	if(idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")) {
		institucionOrigen = CenVisibilidad.getAbreviaturaInstitucion(idInstitucionOrigen);
		if (nume_colegiadoOrigen==null || nume_colegiadoOrigen.equals("")) { // No colegiado 
			nume_colegiado=" ";
		} else {
			nume_colegiado=nume_colegiadoOrigen+ " ( "+institucionOrigen+" )    - ";
		}			
	} else {
		if (nume_colegiado==null || nume_colegiado.equals("")) // No colegiado 
		    nume_colegiado=" ";
		else
			nume_colegiado+="   - ";
	}
	
	if (resultado.get("ART27") != null) {
		if(resultado.get("ART27").equals("1")) { //Designacion Articulo 27-28
	   		ses.setAttribute("botonNuevo",false);		
		} else {// Designacion Normal
			ses.setAttribute("botonNuevo",true);
		}
	} else {
		ses.setAttribute("botonNuevo",true);
	}

	// Procurador seleccionado:
	ArrayList procuradorSel = new ArrayList();
	String idProcurador = null, idInstitucionProcurador = null;

	// Juzgado seleccionado:
	ArrayList juzgadoSel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	ArrayList pretensionesSel = new ArrayList();
	String idJuzgado = "-1", idInstitucionJuzgado = null, idProcedimiento = "-1";

	String idTurno = "", anio = "", numero = "", codigo = "", numeroProcedimiento = "",anioProcedimiento = "", sufijo="";
	boolean anulada = false;

	// Designa consultada:
	ScsDesignaBean beanDesigna = null;
	ScsAsistenciasBean asistenciaBean = null;
	String asistenciaAnio = "", asistenciaNumero = "", sFechaJuicio = "", auxJuicio = "", sHorasJuicio = "", sMinutosJuicio = "", calidad ="", nig = "";	
	
	String maxLenghtProc = "20", estilo = "box", readOnly="false", estiloCombo="boxCombo";
	String idPretension = "",pretension="", turno = "", paramsJuzgadoJSON = "", idProcedimientoParamsJSON = "", filtrarModulos = "N";
	String comboJuzgados ="", comboModulos="",comboPretensionesEjis="", comboModulosParentQueryIds="", comboPretensionesParentQueryIds = "";
	String art27 = "", fechaVigor = "";
	
	try {
		// Designa seleccionada:
		beanDesigna = (ScsDesignaBean) request.getAttribute("beanDesigna");
		asistenciaBean = (ScsAsistenciasBean) request.getAttribute("asistenciaBean");
		
		// FECHA DE APERTURA
		fechaApertura = GstDate.getFormatedDateShort("",beanDesigna.getFechaEntrada());		
		fechaOficioJuzgado = GstDate.getFormatedDateShort("",beanDesigna.getFechaOficioJuzgado());
		fechaRecepcionColegio = GstDate.getFormatedDateShort("",beanDesigna.getFechaRecepcionColegio());
		
		//Artículo 27
		art27 =(String)beanDesigna.getArt27();
		
		if (request.getAttribute("filtrarModulos") != null) {
			filtrarModulos = (String)request.getAttribute("filtrarModulos");
		}
		
		paramsJuzgadoJSON = "{\"idjuzgado\":\"-1\"}";
		//if(filtrarModulos.equals("S")){
	    	comboJuzgados = "getJuzgadosTurnosModulos";
	    	comboModulos = "getProcedimientosEnVigencia";
	    	comboModulosParentQueryIds = "idjuzgado,fechadesdevigor,fechahastavigor";
	    	comboPretensionesEjis= "getPretensionesEjisModulosFiltros";
	    	comboPretensionesParentQueryIds = "idpretension";
	    	
	    //}else{
			//comboJuzgados = "getJuzgadosTurnos";
	    	//comboModulos = "getProcedimientosEnVigencia";
	    	//comboModulosParentQueryIds = "idjuzgado,idprocedimiento,fechadesdevigor,fechahastavigor";
	    	//comboPretensionesEjis= "getPretensionesEjisModulos";
	    	//comboPretensionesParentQueryIds = "idpretension";
	   // }

		tipo = (String) resultado.get("IDTIPODESIGNACOLEGIO");
		turno = (String)resultado.get("TURNO");
		estado = (String) resultado.get("ESTADO");
		
		if (modo=="Ver"){
			 modo1="ver";	
			 modoAction="ver";
		}

	 	if ((modoAction.equals("editar"))&&(estado.equals("F"))){			
			modo1="editar";
			modo="ver";
	     }
	 
		
		if (resultado.get("FECHAFIN") != null && !((String) resultado.get("FECHAFIN")).equals(""))
			fecha = GstDate.getFormatedDateShort("", (String) resultado.get("FECHAFIN"));
		procurador = (String) resultado.get("PROCURADOR");
		asunto = (String) resultado.get("RESUMENASUNTO");
		calidad = (String) resultado.get("CALIDAD");
		try {
			fechaEstado = GstDate.getFormatedDateShort("", (String) resultado.get("FECHAESTADO"));
		} catch (Exception e) {
			fechaEstado = (String) resultado.get("FECHAESTADO");
		}

		if (resultado.get("OBSERVACIONES") != null)
			observaciones = (String) resultado.get("OBSERVACIONES");
		if (resultado.get("DELITOS") != null)
			delitos = (String) resultado.get("DELITOS");
		codigo = (String) resultado.get("CODIGO");
		sufijo = (String) resultado.get("SUFIJO");

		sFechaJuicio = com.atos.utils.GstDate.getFormatedDateShort("",beanDesigna.getFechaJuicio());
		auxJuicio = beanDesigna.getFechaJuicio();
		if (auxJuicio != null && !auxJuicio.equals("")) {
			sHorasJuicio = auxJuicio.substring(11, 13);
			sMinutosJuicio = auxJuicio.substring(14, 16);
		}

		// Datos del procurador seleccionado:
		if (beanDesigna.getIdProcurador() != null && beanDesigna.getIdInstitucionProcurador() != null) {
			idProcurador = beanDesigna.getIdProcurador().toString();
			idInstitucionProcurador = beanDesigna.getIdInstitucionProcurador().toString();
			procuradorSel.add(0, idProcurador + "," + idInstitucionProcurador);
		}
		
		//Otros datos:
		if (beanDesigna != null) {
			idTurno = beanDesigna.getIdTurno().toString();
			anio = beanDesigna.getAnio().toString();
			numero = beanDesigna.getNumero().toString();
			codigo = beanDesigna.getCodigo().toString();
			numeroProcedimiento = beanDesigna.getNumProcedimiento().toString();
			
			if (numeroProcedimiento == null)
				numeroProcedimiento = new String("");
			
			if(beanDesigna.getNIG() != null){
				nig = beanDesigna.getNIG();
			}
			
			if(beanDesigna.getProcedimiento() != null && !beanDesigna.getProcedimiento().equals(""))
				idProcedimiento = beanDesigna.getProcedimiento().toString();
			
			idProcedimientoParamsJSON = "{\"idprocedimiento\":\""+idProcedimiento+"\"";
			
			anioProcedimiento = new String("");
			if (beanDesigna.getAnioProcedimiento() != null)
				anioProcedimiento = beanDesigna.getAnioProcedimiento().toString();						
			
			procedimientoSel.add(0,"{\"idprocedimiento\":\""+idProcedimiento+"\",\"idinstitucion\":\""+usr.getLocation()+"\"}");
		}

		// obteniendo juzgados y juzgado seleccionado
		if (beanDesigna.getIdJuzgado() != null && !beanDesigna.getIdJuzgado().equals("") && 
			beanDesigna.getIdInstitucionJuzgado() != null &&!beanDesigna.getIdInstitucionJuzgado().equals("")) {
			idJuzgado = beanDesigna.getIdJuzgado().toString();
			idInstitucionJuzgado = beanDesigna.getIdInstitucionJuzgado().toString();
		} else if (request.getAttribute("idDesigna") != null) {
			Hashtable datosDesigna = (Hashtable) request.getAttribute("idDesigna");
			idJuzgado = (String) datosDesigna.get(ScsDesignaBean.C_IDJUZGADO);
			idInstitucionJuzgado = (String) datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
		}
		
		if(filtrarModulos.equals("S")) {
			fechaVigor = fechaApertura;
		} else {
			fechaVigor = GstDate.getHoyJsp();
		}
		
		juzgadoSel.add(0,"{\"idjuzgado\":\""+idJuzgado+"\",\"idinstitucion\":\""+idInstitucionJuzgado+"\",\"fechadesdevigor\":\""+fechaVigor+"\",\"fechahastavigor\":\""+fechaVigor+"\"}");
		paramsJuzgadoJSON = "{\"idjuzgado\":\""+idJuzgado+"\"";
		paramsJuzgadoJSON += ",\"fechadesdevigor\":\""+fechaVigor+"\"";
		paramsJuzgadoJSON += ",\"fechahastavigor\":\""+fechaVigor+"\"}";
		idProcedimientoParamsJSON += ",\"idjuzgado\":\""+idJuzgado+"\"";
		idProcedimientoParamsJSON += ",\"fechadesdevigor\":\""+fechaVigor+"\"";
		idProcedimientoParamsJSON += ",\"fechahastavigor\":\""+fechaVigor+"\"}";
		
		// obteniendo la pretension
		if ((beanDesigna != null) &&  (beanDesigna.getIdPretension()!= null)){
			idPretension = beanDesigna.getIdPretension().toString();
			pretensionesSel.add(0,idPretension);
		}		

		// TRATAMIENTO DE LA FECHA DE ANULACIÓN

		if (beanDesigna != null) {
			fechaAnulacion = beanDesigna.getFechaAnulacion().toString();
			if (fechaAnulacion.trim().equals("")) {
				Date hoy = new Date();
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
				fechaAnulacion = sd.format(hoy);
			} else {
				fechaAnulacion = GstDate.getFormatedDateShort("", fechaAnulacion);
				anulada = true;
			}
		}
		
		if (!anulada && estado != null && estado.equalsIgnoreCase("A"))
			anulada = true;
		
	} catch (Exception e) {
		idTurno = (String) resultado.get(ScsDesignaBean.C_IDTURNO);
		anio = (String) resultado.get(ScsDesignaBean.C_ANIO);
		numero = (String) resultado.get(ScsDesignaBean.C_NUMERO);
		codigo = (String) resultado.get(ScsDesignaBean.C_CODIGO);
		e.printStackTrace();
	}

	// RGG 17-03-2006

	String nombreTurnoAsistencia = (String) request.getAttribute("nombreTurnoAsistencia");
	String nombreGuardiaAsistencia = (String) request.getAttribute("nombreGuardiaAsistencia");
	
	String asterisco = "&nbsp(*)&nbsp";
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	
	int ejisActivo = 0;
	if (request.getAttribute("EJIS_ACTIVO")!=null){
		ejisActivo = Integer.parseInt(request.getAttribute("EJIS_ACTIVO").toString());
	}	
	
	boolean validarProcedimiento = false;
	boolean obligatorioProcedimiento = false;
	boolean obligatoriojuzgado=false;	
	boolean obligatorioModulo=false;
	boolean obligatorioTipoDesigna=false;
	
	if (pcajgActivo==2){
		maxLenghtProc = "15";
		obligatorioProcedimiento = true;
	}
	
	if(pcajgActivo==3){
		obligatorioProcedimiento = true;
	}	

	if (pcajgActivo==4){
		validarProcedimiento = true;
	    obligatorioProcedimiento = true;
		obligatorioModulo=true;
		obligatorioTipoDesigna=true;		
	}
	
	/*Se modifica para que sea obligatorio el juzgado para pcajg=5*/
	if (pcajgActivo==5){
		validarProcedimiento = true;
		obligatoriojuzgado = true;
		obligatorioProcedimiento = true;
	}

	if (modo1.equals("editar")){
		 validarProcedimiento = false;
		 obligatorioProcedimiento = false;
		 obligatoriojuzgado=false;	
		 obligatorioModulo=false;
		 obligatorioTipoDesigna=false;
	}
	
	String modoVerReadOnly = String.valueOf(modo.equalsIgnoreCase("ver"));
	
	//Combo Pretensiones
	String idPretensionParamsJSON = "";
	if(beanDesigna.getIdPretension()!=null){
		idPretensionParamsJSON = "{\"idpretension\":\""+beanDesigna.getIdPretension().toString()+"\"";
		comboPretensionesParentQueryIds = "idpretension,";
	} else {
		String aux = "-2";
		idPretensionParamsJSON = "{\"idpretension\":\""+aux+"\"";
	}
	
	String comboPretensiones = "getPretensiones";
	if (ejisActivo>0 || pcajgActivo == 4){
		comboPretensiones = comboPretensionesEjis;
		String sIdJuzgado = "";
		if (beanDesigna.getIdJuzgado() != null){
			sIdJuzgado = beanDesigna.getIdJuzgado().toString();
			comboPretensionesParentQueryIds = "idpretension,idjuzgado";
			idPretensionParamsJSON += ",\"idjuzgado\":\""+sIdJuzgado+"\"}";
		}
	} else {
		comboPretensionesParentQueryIds = "";
		idPretensionParamsJSON = "";
	}
%>	

<!-- HEAD -->
	<script type="text/javascript">
		var modo = "<%=modo%>";
	</script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.editarDesigna.literal.titulo" localizacion="gratuita.editarDesigna.literal.location"/>
	<bean:define id="ejgs" name="EJGS" scope="request" />
	<style>
		.literalEjg{display:inline-block;width:45px;padding:0px;padding-left:10px;}
		.literalTurno{display:inline-block;width:60px;padding:0px}
		.turnoEjg{display:inline-block;width:160px;padding:0px}
		.literalEstados{display:inline-block;width:70px;padding:0px}
		.tipoEjg{display:inline-block;width:160px;padding:0px}
		.numEjg{display:inline-block;width:90px;padding:0px}
		.estadoEjg{display:inline-block;width:160px;padding:0px}
		.botonera{display:inline-block;width:100px;padding:0px;text-align:right}
		.toggleButton{display:inline-block;width:25px;padding:0px}
		.botonDesplegar{cursor:pointer;width:16px;display:inline;padding:0;margin:0}
	</style>	 
	<script language="JavaScript">
	
		<% if (ejisActivo>0) { %>
			// Valida el numero de procedimiento (n/aaaa)
			function validaProcedimiento (strValue) {
				var objRegExp  = /^([0-9]{7})?$/;
				return objRegExp.test(strValue);
			}
			
			function validarAnioProcedimiento (strValue) {
				var objRegExp  = /^([0-9]{4})?$/;
				return objRegExp.test(strValue);
			}	
			
				
			
		<% } else { %>
			// Valida el numero de procedimiento (n/aaaa)
			function validaProcedimiento (strValue)	{
				var objRegExp  = /^([0-9]+\/[0-9]{4})?$/;
				return objRegExp.test(strValue);
			}		
			
		<%}%>
				
		// Asociada al boton Volver
		function accionVolver() {		
			document.BuscarDesignasForm.action="JGR_Designas.do";
			document.BuscarDesignasForm.modo.value="volverBusqueda";

			document.BuscarDesignasForm.submit();
		}

		
		// Asociada al boton Restablecer
		function accionRestablecer() {	
			jQuery("#mainWorkArea", window.top.document).contents().find("#pestana\\.justiciagratuitadesigna\\.datosgenerales").click();
			/*
			document.forms[0].reset();			
			if (jQuery("#juzgado").find("option:selected").exists()){
				var selectedKey = "";
				if (jQuery("#juzgado").find("option:selected").attr("data-searchKey"))
					selectedKey = jQuery("#juzgado").find("option:selected").attr("data-searchKey");
				document.getElementById("juzgado_searchBox").value = selectedKey;
			}
			*/
		}
		
		// Asociada al boton Guardar
		function accionGuardar() {	
			<%if (pcajgActivo>0) { %>
				var error = "";
				
				if (<%=obligatorioTipoDesigna%> && document.getElementById("tipo").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.editarDesigna.literal.tipo'/>"+ '\n';

				if(<%=obligatoriojuzgado%> && document.getElementById("juzgado").value=="")										
					error += "<siga:Idioma key='gratuita.editarDesigna.juzgado'/>"+ '\n';
				
				if (<%=obligatorioModulo%> && document.getElementById("idProcedimiento").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.modulo'/>"+ '\n';
					
				if (<%=obligatorioProcedimiento%> && document.getElementById("idPretension").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.pretensiones'/>"+ '\n';
				
				<% if (ejisActivo==0) { %>
					if (<%=validarProcedimiento%>) {
						if(!validaProcedimiento(document.getElementById("numeroProcedimiento").value))
							error += "<siga:Idioma key='gratuita.procedimientos.numero.formato'/>"+ '\n';
					}
				<% } %>
				
				
				if(error!=""){
					alert(error);
					fin();
					return false;
				}
		 	<% } %>
		 	
		 		
		 	
		 	<% if (ejisActivo>0) { %>		 	
				if(document.getElementById("numeroProcedimiento").value != "" || document.getElementById("anioProcedimiento").value != "") {
					if(document.getElementById("numeroProcedimiento").value == "" || !validaProcedimiento(document.getElementById("numeroProcedimiento").value))
						error += "<siga:Idioma key='gratuita.procedimientos.numero.formato.ejis'/>"+ '\n';
						
					if(document.getElementById("anioProcedimiento").value == "" || !validarAnioProcedimiento(document.getElementById("anioProcedimiento").value))	
						error += "<siga:Idioma key='gratuita.procedimientos.anio.formato'/>"+ '\n';
						
					if(error!="" && document.forms[0].estadoOriginal.value != 'F'){
						alert(error);
						fin();
						return false;
					}	
				}
				
				
				
				
			
		 	<% } %>
		 	
			var estado = trim(document.forms[0].estado.value); // Cogemos el estado de la designa del formulario
			var estadoOriginal = trim(document.forms[0].estadoOriginal.value); // Cogemos el estado original 
			if ((estado == "A") && (estadoOriginal != "A")) { // Si es un cambio a anulacion (V,F -> A)...
				if (confirm("<siga:Idioma key='gratuita.compensacion.confirmacion'/>")) { // y desea compensar al letrado ...
					document.forms[0].compensar.value = "1";
				} else {
					document.forms[0].compensar.value = "0";
				}
			} else if((estadoOriginal == "A") && (estado != "A")) { // Desanulacion (A -> V,F)
				alert ("<siga:Idioma key='gratuita.compensacion.anular'/>");
			}
			
			var fechaJuicio = trim(document.forms[0].fechaJuicio.value);
			sub();
			if (trim(fechaJuicio)!="") {
				//Para la validacion no tengo en cuenta si empieza por 0 y tiene 2 digitos (tanto hora como minuto)
				var horas = trim(document.forms[0].horasJuicio.value);
				var minutos = trim(document.forms[0].minutosJuicio.value);
				
				var mensajeminutos="<siga:Idioma key="messages.programarFacturacionForm.mensajeMinutos"/>";
				var mensajehoras="<siga:Idioma key="messages.programarFacturacionForm.mensajeHoras"/>";
	
				if (horas.length==1) {
					document.forms[0].horasJuicio.value = "0" + horas;
				}
				
				if (minutos.length==1) {
					document.forms[0].minutosJuicio.value = "0" + minutos;
				}
				
				if (horas!="" && (horas>23 || horas<0)) {
					alert(mensajehoras);
					fin();
					return false;
				}
				
				if (minutos!="" && (minutos>59 || minutos<0)) {
					alert(mensajeminutos);
					fin();
					return false;
				}
			}	
			
			var valor="";
			if(document.forms[0].fecha.value!=""){					
				if (trim(fechaJuicio)!="") {
					valor = trim(document.forms[0].horasJuicio.value);
		            if (valor!="" && !isNumero(valor)) {
		            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.horasGeneracion'/>");
		            	fin();
		            	return false;
					}
		            
					valor = trim(document.forms[0].minutosJuicio.value);
		            if (valor!="" && !isNumero(valor)) {
		            	alert ("<siga:Idioma key='facturacion.seriesFacturacion.literal.minutosGeneracion'/>");
		            	fin();
		            	return false;
					}
				}
			
		 	} else {		 
		 		alert('<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.aviso.fechaRequerida"/>');
		 		fin();
		 		return false;
		 	}
			var nigAux = document.getElementById("nig").value;
			nigAux = formateaNig(nigAux);
			if(!validarNig(nigAux)&& document.forms[0].estadoOriginal.value != 'F'){	
				alert("<siga:Idioma key='gratuita.nig.formato'/>");
				fin();
				return false;
					
			}
			document.getElementById("nig").value = nigAux;
			document.forms[0].nig.value = nigAux; 
			
			
			document.forms[0].action="JGR_MantenimientoDesignas.do";
			document.forms[0].modo.value="modificar";
			document.forms[0].target="submitArea";
			document.forms[0].submit();
		}
		
		function accionAnular() {	
			if(document.forms[0].tipo.selectedIndex<1)
				alert('<siga:Idioma key="gratuita.editarDesigna.literal.tipo"/>');
			else{
				if (document.forms[0].juzgado.value!='') {
					if (confirm('<siga:Idioma key="messages.anular.confirmacion"/>')) {
						jQuery("#tdTextoAnulacion").show();
						jQuery("#tdFechaAnulacion").show();
						document.forms[0].action="JGR_MantenimientoDesignas.do";
						document.forms[0].modo.value="anularDesigna";
						document.forms[0].target="submitArea";
						document.forms[0].submit();
					}
				} else {
					alert('<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado" /> <siga:Idioma key="messages.campoObligatorio.error" />');
				}
			}
		}
		
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].action="JGR_MantenimientoDesignas.do";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}
		
		function muestraFecAnulacion() {
			var anulada=<%=anulada%>;
			if(anulada) {
				jQuery("#tdTextoAnulacion").show();
				jQuery("#tdFechaAnulacion").show();
			}
		}
		
		// Asociada al boton Consultar Designa
		function consultarAsistenciaFuncion() {
		   	document.forms[1].submit();
	 	}
		
	    function reloadPage() {
		    var type = '<siga:Idioma key="messages.gratuita.designaDuplicada"/>';
			if (confirm(type)) {
			    document.forms[0].modificarDesigna.value="1";
				document.forms[0].submit();
			}
		}
		
		function generarCarta() {
			//sub();
			
			//idInstitucion  = document.MaestroDesignasForm.idInstitucion;
			idInstitucion  = <%=idInstitucionLocation%>;
			
			anio  = <%=anio%>;
			idTurno  = <%=idTurno%>;
			numero = <%=numero%>;
 		   	datos = "idInstitucion=="+idInstitucion +"##anio=="+anio + "##idTurno==" +idTurno+"##numero==" +numero+"##idTipoInforme==OFICI%%%";
						
 		   document.InformesGenericosForm.datosInforme.value=datos;
 		   var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
 			if (arrayResultado==undefined||arrayResultado[0]==undefined) {
 			   		
 		   	} else {
 		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
 		   		if(confirmar){
 		   			var idEnvio = arrayResultado[0];
 				    var idTipoEnvio = arrayResultado[1];
 				    var nombreEnvio = arrayResultado[2];				    
 				    
 				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
 				   	document.DefinirEnviosForm.modo.value='editar';
 				   	document.DefinirEnviosForm.submit();
 		   		}
 		   	}	
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
		});	
		
		function accionCerrar() {
			
		}
		
		function mostrarEjgs(){
			jQuery('.contenedorEjgOtros').show();		
			jQuery("#botonToggleEjgs").html("<img src=\"<html:rewrite page='/html/imagenes/iconoOcultar.gif'/>\" onclick=\"ocultarEjgs();\" class=\"botonDesplegar\"/>");
		}
		
		function ocultarEjgs(){
			jQuery('.contenedorEjgOtros').hide();
			jQuery("#botonToggleEjgs").html("<img src=\"<html:rewrite page='/html/imagenes/iconoDesplegar.gif'/>\" onclick=\"mostrarEjgs();\" class=\"botonDesplegar\"/>");

		}
		
		//Asociada al boton Consultar Ejg
		function abrirEJG(modo,idtipoejg,idinstitucion,anioejg,numeroejg) {
			document.DefinirEJGForm.modo.value= modo;
			document.DefinirEJGForm.idTipoEJG.value= idtipoejg;
			document.DefinirEJGForm.anio.value= anioejg;
			document.DefinirEJGForm.numero.value= numeroejg;
			document.DefinirEJGForm.idInstitucion.value= idinstitucion;
			document.DefinirEJGForm.submit();
	 	}
		//Asociada al boton Boton Ejg
		function borrarRelacionEJG(idtipoejg,idinstitucion,anioejg,numeroejg) {
			if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>")) {
				
				document.EjgDesignasForm.anio.value=<%=anio%>;
				document.EjgDesignasForm.numero.value=<%=numero%>;
				document.EjgDesignasForm.ejgIdInstitucion.value = idinstitucion;
				document.EjgDesignasForm.ejgAnio.value = anioejg;
				document.EjgDesignasForm.ejgNumero.value = numeroejg;		
				document.EjgDesignasForm.ejgIdTipoEjg.value = idtipoejg;
				document.EjgDesignasForm.idTurno.value=<%=idTurno%>;
			   	document.EjgDesignasForm.target="submitArea";
			   	document.EjgDesignasForm.modo.value = "borrarRelacionConEJG";
			   	document.EjgDesignasForm.submit();

			} 
		}
	</script>
	 
</head>

<body>

<table class="tablaTitulo" cellspacing="0" heigth="38" width="100%" border="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<%
				String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_sufijo="";
				ScsDesignaAdm adm = new ScsDesignaAdm(usr);
				Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(),	anio, numero, idTurno);
	
				if (hTitulo != null) {
					t_nombre = (String) hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
					t_apellido1 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
					t_apellido2 = (String) hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
					t_anio = (String) hTitulo.get(ScsDesignaBean.C_ANIO);
					t_numero = (String) hTitulo.get(ScsDesignaBean.C_CODIGO);
					t_sufijo = (String) hTitulo.get(ScsDesignaBean.C_SUFIJO);
					if (t_sufijo!=null && !t_sufijo.equals("")){
						t_numero=t_numero+"-"+t_sufijo;
					}
	
				}
			%> 
			<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
			- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%>
			<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
		</td>
		<td>
			<input 	type="button" 
					alt="UtilidadesString.getMensajeIdioma(usrbean,general.boton.cartaInteresados)"  
			       	id="idButton"  
			       	onclick="return generarCarta();" 
			       	class="button" 
			       	value=<%=UtilidadesString.getMensajeIdioma(usr,"gratuita.EJG.botonComunicaciones")%> />
		</td>
	</tr>
</table>

<!-- Comienzo del formulario con los campos -->
<table class="tablaCentralCampos" height="420" align="center" >
	<html:form action="JGR_Designas.do" method="POST" target="mainWorkArea">
		<html:hidden name="MaestroDesignasForm" property="modo"  styleId="modo" />
		<html:hidden name="MaestroDesignasForm" property="idTurno" styleId="idTurno"  value="<%=idTurno%>" />
		<html:hidden name="BuscarDesignasForm" property="calidad" styleId="calidad" value="<%=calidad%>" />	
		<input type="hidden" name="modificarDesigna" id="modificarDesigna"  value="0">
		<html:hidden property="compensar" styleId="compensar" value="" />
		<tr>
			<td valign="top">
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.turno">
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
						<tr>
							<%
								String claveDesigna="";
								if (sufijo!=null && !sufijo.equals("")){
									claveDesigna=codigo+"-"+sufijo;
								} else {
									claveDesigna=codigo;
								}
							%>
							<td class="labelText">
							 	<siga:Idioma key="facturacion.ano" /> / <siga:Idioma key="gratuita.busquedaDesignas.literal.codigo" />
							</td>
							<td class="labelText">
								<html:text name="MaestroDesignasForm" property="anio" size="4" maxlength="10" styleClass="boxConsulta" value="<%=anio%>"  readonly="true" />
								/
								<html:text name="MaestroDesignasForm" property="codigo" size="10" maxlength="10" styleClass="boxConsulta" value="<%=claveDesigna%>"  readonly="true" />
							</td>
							
							<td style="display: none">
								<html:text name="MaestroDesignasForm" property="numero" size="8" maxlength="10" styleClass="boxConsulta" value="<%=numero%>" readonly="true" />
							</td>
							<td class="labelText">
								<siga:Idioma key='sjcs.designa.general.letrado' />
							</td>
							<td>
								<input type="text" class="boxConsulta" value="<%=nume_colegiado%>  <%=nombre_letrado%>" readOnly="true" style="width:500px">
							</td>
						</tr>
					</table>
					
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.busquedaSOJ.literal.turno" />
							</td>
							<td>
								<html:text name="MaestroDesignasForm" property="turno" styleClass="boxConsulta" value="<%=turno%>" readonly="true" />
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha" />
							</td>
							<!-- JBD 16/2/2009 INC-5682-SIGA -->
							<% if (!modo.equalsIgnoreCase("ver")) { %>
								<td>
									<siga:Fecha nombreCampo="fecha"   valorInicial="<%=fechaApertura%>"/>															
								</td>
							<%}else{%>
								<td>
									<siga:Fecha nombreCampo="fecha"   valorInicial="<%=fechaApertura%>" disabled="true" readOnly="true" />								
								</td>
							<%}%>
							<!-- JBD 16/2/2009 INC-5682-SIGA -->
							<%if(art27.equals("1")){ %>
								<td class="labelText">
									<siga:Idioma key="gratuita.editarDesigna.literal.art27Texto"/>
								</td>
							<%}%>
						</tr>
					</table>
				</siga:ConjCampos> 
				
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.designa">
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
						<tr>
							<td class="labelText" width="15%">
								<siga:Idioma key="gratuita.editarDesigna.literal.tipo" />
								<% if (obligatorioTipoDesigna) { %>
							 		<%= asterisco %>
								<% } %>
							</td>
							
							<%
								ArrayList vTipo = new ArrayList();
								String s1 = (String) resultado.get(ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO);
								vTipo.add(s1 == null || s1 == "-1" ? "0" : s1);
							%>
							
							<td colspan="7">
								<siga:Select id="tipo" queryId="getTiposDesignaDeColegio" selectedIds="<%=vTipo%>" readOnly="<%=modoVerReadOnly%>"/>
							</td>
						</tr>
	
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.estado" />
							</td>
							
							<td>
								<% if (!modo1.equalsIgnoreCase("ver")) { %> 
									<select name="estado" class="boxCombo">
										<option value='V'
											<%if((estado!=null)&&(estado.equalsIgnoreCase("V"))){%> 
												selected
											<%}%>
										>
											<siga:Idioma key="gratuita.designa.estado.abierto" />
										</option>		
																
										<option value='F'
											<%if((estado!=null)&&(estado.equalsIgnoreCase("F"))){%> 
												selected
											<%}%>
										>
											<siga:Idioma key="gratuita.designa.estado.finalizado" />
										</option>
										
										<option value='A'
											<%if((estado!=null)&&(estado.equalsIgnoreCase("A"))){%> 
												selected
											<%}%>
											><siga:Idioma key="gratuita.designa.estado.anulado" />
										</option>
									</Select> 
							
								<% } else { %> 
							 		<% String valorEstado = "";
										if ((estado != null) && (estado.equalsIgnoreCase("V")))
											valorEstado = "Activo";
										else {
											if ((estado != null) && (estado.equalsIgnoreCase("F")))
												valorEstado = "Finalizado";
											else {
												if ((estado != null) && (estado.equalsIgnoreCase("A")))
													valorEstado = "Anulado";
											}
										} %> 
	 								<html:text name="MaestroDesignasForm" property="estado" maxlength="10" styleClass="boxConsulta" value="<%=valorEstado%>" readonly="true" /> 
								<%}%> 
								<html:hidden name="MaestroDesignasForm" property="estadoOriginal" value="<%=estado%>" />
							</td>
	
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaEstado" />
							</td>
							
							<td>
								<html:text name="MaestroDesignasForm" property="fechaEstado" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fechaEstado%>" readonly="true" />
							</td>
							
							<td class="labelText" id="tdTextoAnulacion" style="text-align: rigth; display: none">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaAnulacion" />
							</td>
							
							<td id="tdFechaAnulacion" style="text-align: rigth; display: none">
								<html:text name="MaestroDesignasForm" property="fechaAnulacion" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fechaAnulacion%>" readonly="true" /> &nbsp;
							</td>
							
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaCierre" />
							</td>
							
							<td>
								<% if (!modo.equalsIgnoreCase("ver")) {%> 
									<siga:Fecha nombreCampo="fechaCierre"   valorInicial="<%=fecha%>" />							 
								<%} else { %>
									<siga:Fecha nombreCampo="fechaCierre"   valorInicial="<%=fecha%>" disabled="true" readOnly="true" />  							
	 							<%}%>
							</td>
						</tr>
						
						<tr>
							<td class="labelText" style="vertical-align: middle;">
								<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento" />
							</td>
							
							<td colspan="7">
								<table align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
									<tr>
										 <%if (ejisActivo>0){%>																				
												<td style="vertical-align: middle;">						
													<% if (!modo.equalsIgnoreCase("ver")) { %> 
														<html:text name="MaestroDesignasForm" property="numeroProcedimiento" size="7" maxlength="7" styleClass="box" value="<%=numeroProcedimiento%>" />
														/
														<html:text name="MaestroDesignasForm" property="anioProcedimiento" size="4" maxlength="4" styleClass="box" value="<%=anioProcedimiento%>" />
														 
													<% } else { %> 
														<html:text name="MaestroDesignasForm" property="numeroProcedimiento"  size="7" maxlength="7" styleClass="boxConsulta" value="<%=numeroProcedimiento%>" readonly="true" />
														/
														<html:text name="MaestroDesignasForm" property="anioProcedimiento" size="4" maxlength="4" styleClass="boxConsulta" value="<%=anioProcedimiento%>"  readonly="true" />
													<% } %>	
												</td>
												
										<% } else { %> 	
										
												<td style="vertical-align: middle;">
													<% if (!modo.equalsIgnoreCase("ver")) { %> 
														<html:text name="MaestroDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="box" value="<%=numeroProcedimiento%>" /> 
													<% } else { %> 
														<html:text name="MaestroDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="boxConsulta" value="<%=numeroProcedimiento%>" readonly="true" /> 
													<% } %>	
												</td>									
										<% } %>		
							
										<td colspan="6"><!-- Busqueda automatica de juzgados--> 						
											<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">
												<table>							
													<%if (ejisActivo>0 || pcajgActivo == 4){%>		 														
															<tr>
																<% if (!modo.equalsIgnoreCase("ver")) { %>
																	<td class="labelText">
																		<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext" />
																		<% if (obligatoriojuzgado){ %>
																			<%= asterisco %>
																		<%}%>
																	</td>
																
																<% } %>
																	<td colspan="2" nowrap="nowrap">
																		<siga:Select id="juzgado" queryId="<%=comboJuzgados%>" queryParamId="idjuzgado" params="<%=paramsJuzgadoJSON%>" selectedIds="<%=juzgadoSel%>" showSearchBox="true" searchkey="CODIGOEXT2" searchBoxMaxLength="10" searchBoxWidth="8" width="500" childrenIds="idProcedimiento,idPretension" readonly="<%=modoVerReadOnly%>"/>
																	</td> 
															</tr>
															
													<% } else { %>														
															<tr>
																<% if (!modo.equalsIgnoreCase("ver")) { %>
																	<td class="labelText">
																		<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext" />
																		<% if (obligatoriojuzgado){ %>
																			<%= asterisco %>
																		<%}%>
																	</td>												
																<% } %> 
																
																<td rowspan="2" nowrap="nowrap">
																	<siga:Select id="juzgado" queryId="<%=comboJuzgados%>" queryParamId="idjuzgado" params="<%=paramsJuzgadoJSON%>" selectedIds="<%=juzgadoSel%>" showSearchBox="true" searchkey="CODIGOEXT2" searchBoxMaxLength="10" searchBoxWidth="8" width="500" childrenIds="idProcedimiento" readonly="<%=modoVerReadOnly%>"/>
																</td> 
															</tr>									
													<% } %>		
													<!-- Juzgado -->		
												</table>
											</siga:ConjCampos> 																
										</td>
									</tr>
								</table>	
							</td>
						</tr>
						
						<tr>
							<td class="labelText">
								<siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
							</td>
							<td > 
								<% if (!modo.equalsIgnoreCase("ver")) { %>
								 	<input id="nig" name="nig"  type="text" value="<%=nig%>" class="<%=estilo%>" style="size:19;width:200px"/>
								<%}else{%>
									<input id="nig" name="nig"  type="text" value="<%=nig%>" class="boxConsulta" style="size:19;width:200px"/>
								<%}%>						
							</td>
										
							<td id="info" style="display:none"><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
							</td>
							<td colspan = "5"></td>
							
						</tr>					
						
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo" /> 
								<%if (obligatorioModulo){ %>
									<%= asterisco %>
								<%}%>
							</td>
							
							<td colspan="7">
								<%-- Procedimiento --%> 
								<siga:Select id="idProcedimiento" queryId="<%=comboModulos%>" parentQueryParamIds="<%=comboModulosParentQueryIds%>" params="<%=idProcedimientoParamsJSON%>" selectedIds="<%=procedimientoSel%>" disabled="<%=modoVerReadOnly%>" width="750"/>							
							</td>
						</tr>
						
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.asunto" />
							</td>
							
							<td colspan="7">
								<% if (!modo.equalsIgnoreCase("ver")) { %> 
									<html:text name="MaestroDesignasForm" property="asunto" maxlength="100" styleClass="box" value="<%=(String)resultado.get(ScsDesignaBean.C_RESUMENASUNTO)%>" style="width:740"></html:text> 
								<% } else { %> 
									<html:text name="MaestroDesignasForm" property="asunto" maxlength="100" styleClass="boxConsulta" value="<%=(String)resultado.get(ScsDesignaBean.C_RESUMENASUNTO)%>" readonly="true" style="width:740"></html:text> 
								<% } %>
							</td>
						</tr>
						
						<!-- JBD 16/2/2009 INC-5739-SIGA -->
						<tr>
							<td class="labelText">	
								<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
								<% if (obligatorioProcedimiento){ %>
									<%= asterisco %>
								<%} %>
							</td>				
					
							<td  colspan="7">
								<siga:Select id="idPretension" queryId="<%=comboPretensiones %>" parentQueryParamIds="<%=comboPretensionesParentQueryIds %>" params="<%=idPretensionParamsJSON%>" selectedIds="<%=pretensionesSel %>" width="380" readOnly="<%=modoVerReadOnly%>"/>							
							</td>
						</tr>
						
						<!-- JBD 16/2/2009 INC-5739-SIGA -->
					</table>
				</siga:ConjCampos> 
				
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.juicio">			
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%">
						<tr>
							<td class="labelText" style="width: 80px">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaJuicio" />
							</td>
							<td class="labelText" style="width: 150px">
								<% if (!modo.equalsIgnoreCase("ver")) { %> 
									<siga:Fecha nombreCampo="fechaJuicio"   valorInicial="<%=sFechaJuicio%>"/>														 
								<% } else { %> 
									<siga:Fecha nombreCampo="fechaJuicio"   valorInicial="<%=sFechaJuicio%>" disabled="true" readOnly="true" />							 
								<% } %>
							</td>
							<td class="labelText" style="width: 80px">
								<siga:Idioma key="gratuita.editarDesigna.literal.horaJuicio" />
							</td>
							<td class="labelText">
								<% if (!modo.equalsIgnoreCase("ver")) { %> 
									<html:text name="MaestroDesignasForm" property="horasJuicio" value="<%=sHorasJuicio%>" size="1" maxlength="2" styleClass="box" readonly="false"></html:text> : <html:text name="MaestroDesignasForm" property="minutosJuicio" value="<%=sMinutosJuicio%>" size="1" maxlength="2" styleClass="box" readonly="false" /> 
								<% } else { %> 
									<html:text name="MaestroDesignasForm" property="horasJuicio" value="<%=sHorasJuicio%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true"></html:text> : <html:text name="MaestroDesignasForm" property="minutosJuicio" value="<%=sMinutosJuicio%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true" /> 
								<% } %>
							</td>
						</tr>
					</table>
				</siga:ConjCampos> 
				<!-- relacion con ejg -->
				<logic:notEmpty name="EJGS" scope="request">
	        		<siga:ConjCampos leyenda="gratuita.operarEJG.literal.relacionado">
						<logic:iterate name="EJGS" id="ejg" scope="request" indexId="index"> 
						<logic:equal name="index" value="0">
						<div class="contenedorPrimerEjg">
							<span class="labelText literalEjg"><siga:Idioma key='gratuita.operarEJG.literal.EJG'/></span>
							<span id="botonToggleEjgs" class="toggleButton"><img src="<html:rewrite page='/html/imagenes/iconoDesplegar.gif'/>" onclick="mostrarEjgs();" class="botonDesplegar"/></span>
						</logic:equal>
						<logic:notEqual name="index" value="0">
						<div class="contenedorEjgOtros" style="display:none">
							<span class="labelText literalEjg">&nbsp;</span>
							<span class="toggleButton">&nbsp;</span>
							<script></script>
						</logic:notEqual>
							<span class="labelTextValue numEjg">${ejg.anioejg}/${ejg.codigoejg}</span>
							<span class="labelText literalTurno"><siga:Idioma key='gratuita.operarEJG.literal.turno'/></span>
							<span class="labelTextValue turnoEjg">${ejg.descripcionTurno}</span>
							<span class="labelText literalTurno"><siga:Idioma key='gratuita.operarEJG.literal.tipo'/></span>
							<span class="labelTextValue tipoEjg">${ejg.descripcionTipo}</span>
							<span class="labelText literalEstados"><siga:Idioma key='gratuita.EJG.estados'/></span>
							<span class="labelTextValue estadoEjg">${ejg.descripcionEstado}</span>
							<span class="labelTextValue botonera">
								<img src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>" style="cursor:pointer;" alt="<siga:Idioma key='gratuita.operarEJG.boton.ConsultarEJG'/>" name="consultarEJG" border="0" onclick="abrirEJG('ver','${ejg.idtipoejg}','${ejg.idinstitucion}','${ejg.anioejg}','${ejg.numeroejg}')"/>
							<% if (modo.equalsIgnoreCase("ver")) { %>
								<img src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>" style="cursor:pointer;display:none" alt="<siga:Idioma key='gratuita.boton.EditarEJG'/>" class="botonEditarEJG" border="0" onclick="abrirEJG('<%=modo%>','${ejg.idtipoejg}','${ejg.idinstitucion}','${ejg.anioejg}','${ejg.numeroejg}')"/>
								<img src="<html:rewrite page='/html/imagenes/bborrar_off.gif'/>" style="cursor:pointer;display:none" alt="<siga:Idioma key='gratuita.boton.BorrarEJG'/>" class="botonBorrarEJG" border="0" onclick="borrarRelacionEJG('${ejg.idtipoejg}','${ejg.idinstitucion}','${ejg.anioejg}','${ejg.numeroejg}')"/>
							<% } else { %>
								<img src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>" style="cursor:pointer;" alt="<siga:Idioma key='gratuita.boton.EditarEJG'/>" class="botonEditarEJG" border="0" onclick="abrirEJG('<%=modo%>','${ejg.idtipoejg}','${ejg.idinstitucion}','${ejg.anioejg}','${ejg.numeroejg}')"/>
								<img src="<html:rewrite page='/html/imagenes/bborrar_off.gif'/>" style="cursor:pointer;" alt="<siga:Idioma key='gratuita.boton.BorrarEJG'/>" class="botonBorrarEJG" border="0" onclick="borrarRelacionEJG('${ejg.idtipoejg}','${ejg.idinstitucion}','${ejg.anioejg}','${ejg.numeroejg}')"/>
							<% }%>
							</span>
						</div>
						</logic:iterate>
						<script type="text/javascript">
							if (jQuery('.contenedorEjgOtros')[0]) {
								jQuery("#botonToggleEjgs").show();
							}else{
								jQuery("#botonToggleEjgs").hide();
							}
						</script>
					</siga:ConjCampos>
				</logic:notEmpty>
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.observaciones">
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%">
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaOficioJuzgado"/>
							</td>
							<% if (modo.equalsIgnoreCase("ver")) { %>
								<td class="labelTextValor">
									<%=UtilidadesString.mostrarDatoJSP(fechaOficioJuzgado)%>
								</td>
							<% } else { %>										
								<td>
									<siga:Fecha nombreCampo="fechaOficioJuzgado" valorInicial="<%=fechaOficioJuzgado%>" />							
								</td>
							<%} %>
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.fechaRecepcionColegio"/>
							</td>
							<% if (modo.equalsIgnoreCase("ver")) { %>
								<td class="labelTextValor">
									<%=UtilidadesString.mostrarDatoJSP(fechaRecepcionColegio)%>
								</td>
							<% } else { %>								
								<td>
									<siga:Fecha nombreCampo="fechaRecepcionColegio" valorInicial="<%=fechaRecepcionColegio%>" />							
								</td>
							<%}%>
						</tr>
						
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.editarDesigna.literal.observaciones" />
							</td>
							<td>
								<% if (!modo.equalsIgnoreCase("ver")) { %> 
									<textarea scroll="none" name="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="7" style="width: 300" class="box"><%=observaciones%></textarea> 
								<% } else { %>
									<textarea scroll="none" name="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="7" style="width: 300" class="boxConsulta" readonly="true"><%=observaciones%></textarea>
								<% } %>
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.general.literal.comentariosDelitos" />
							</td>
							<td>
								<% if (!modo.equalsIgnoreCase("ver")) { %> 
									<textarea scroll="none" name="delitos" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="7" style="width: 300" class="box"><%=delitos%></textarea> 
								<% } else { %>
									<textarea scroll="none" name="delitos" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="7" style="width: 300" class="boxConsulta" readonly="true"><%=delitos%></textarea>
								<% } %>
							</td>
						</tr>
					</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</table>
</html:form>

<html:form action="/JGR_Asistencia.do" method="POST" target="mainWorkArea">
	<html:hidden property="modo" value="ver" />
	<html:hidden property="anio" value="<%=asistenciaAnio%>" />
	<html:hidden property="numero" value="<%=asistenciaNumero%>" />
	<html:hidden property="desdeDesigna" value="si" />
</html:form>

<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
	<input type="hidden" name="modo" value="buscarJuzgado">
	<html:hidden property="codigoExt2" value="" />
	<html:hidden property="nombreObjetoDestino" value="" />
</html:form>

<!-- SI NO EXISTE FECHA DE ANULACIÓN MOSTRAMOS EL BOTÓN ANULARPOR SI DESEA ANULAR LA DESIGNA //-->
<siga:ConjBotonesAccion botones="G,R,V" clase="botonesDetalle" modo="<%=modo1%>" />

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucionLocation%>"/>
	<html:hidden property="idTipoInforme" value="OFICI"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>

<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">
</form>

<html:form action="/JGR_EJG.do"  method="POST" target="mainWorkArea">
	<html:hidden property="modo" value="modo"/>
	<html:hidden property="idTipoEJG" value=""/>
	<html:hidden property="anio" value=""/>
	<html:hidden property="numero" value=""/>
	<html:hidden property="idInstitucion" value=""/>
</html:form>

<html:form action="/JGR_EJG_Designa.do"  method="POST" target="mainWorkArea">
	<html:hidden property="modo" value="modo"/>
	<html:hidden property = "idTurno" value=""/>
	<html:hidden property = "anio" value=""/>
	<html:hidden property = "numero" value=""/>
	<html:hidden property = "ejgIdInstitucion" value=""/>
	<html:hidden property = "ejgAnio" value=""/>
	<html:hidden property = "ejgNumero" value=""/>
	<html:hidden property = "ejgIdTipoEjg" value=""/>
</html:form>

<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

</body>
</html>