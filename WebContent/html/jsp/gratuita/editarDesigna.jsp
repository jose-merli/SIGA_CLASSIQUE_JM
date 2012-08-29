<!-- editarDesigna.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.beans.ScsDesignaAdm"%>






<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	Hashtable resultado = (Hashtable) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");

	String modo = (String) ses.getAttribute("Modo");
	String modoAction=(String) ses.getAttribute("ModoAction");
	String idInstitucionLocation = usr.getLocation();
	String[] dato = { usr.getLocation() };

	String tipo = "", estado = "", fecha = "", procurador = "", asunto = "", observaciones = "", delitos = "", fechaAnulacion = "", fechaEstado = "";
	String fechaApertura = "";
	String fechaOficioJuzgado = "";
	String fechaRecepcionColegio = "";
	String modo1="";
	
	
	
	//Hastable letrado= new Hastable();
	ScsDesignaAdm clase = new ScsDesignaAdm(usr);

	String nombre_letrado;
	String nume_colegiado;
	String nume_colegiadoOrigen;
	String datosColegiales;
	String institucionOrigen;
	String idInstitucionOrigen;
	Hashtable letrado = clase.obtenerLetradoDesigna((String) resultado.get("IDINSTITUCION"), 
													(String) resultado.get("IDTURNO"),
													(String) resultado.get("ANIO"), 
													(String) resultado.get("NUMERO"));

	nombre_letrado = (String) letrado.get("NOMBRE");
	

	nume_colegiado  = (String) letrado.get("NCOLEGIADO");
	datosColegiales = (String) letrado.get("DATOSCOLEGIALES");
	nume_colegiadoOrigen  = (String) letrado.get("NCOLEGIADOORIGEN");
	
	idInstitucionOrigen  = (String)letrado.get("IDINSTITUCIONORIGEN");
	if(idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")){
		institucionOrigen = CenVisibilidad.getAbreviaturaInstitucion(idInstitucionOrigen);
		if (nume_colegiadoOrigen==null || nume_colegiadoOrigen.equals("")){ // No colegiado 
			nume_colegiado=" ";
		}else{
			nume_colegiado=nume_colegiadoOrigen+ " ( "+institucionOrigen+" )    - ";
		}	
		
	}else{
		if (nume_colegiado==null || nume_colegiado.equals("")) // No colegiado 
		    nume_colegiado=" ";
		else
			nume_colegiado+="   - ";
	}
	
	if (resultado.get("ART27") != null){
		if(resultado.get("ART27").equals("1")){//Designacion Articulo 27-28
	   		ses.setAttribute("botonNuevo",false);		
		}else{// Designacion Normal
			ses.setAttribute("botonNuevo",true);
		}
	}else{
		ses.setAttribute("botonNuevo",true);
	}

	// Procurador seleccionado:
	ArrayList procuradorSel = new ArrayList();
	String idProcurador = null, idInstitucionProcurador = null;

	// Juzgado seleccionado:
	ArrayList juzgadoSel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	ArrayList pretensionesSel = new ArrayList();
	String idJuzgado = null, idInstitucionJuzgado = null, idProcedimiento = "-1";

	String idTurno = "", anio = "", numero = "", codigo = "", numeroProcedimiento = "", sufijo="";
	boolean anulada = false;

	// Designa consultada:
	ScsDesignaBean beanDesigna = null;
	ScsAsistenciasBean asistenciaBean = null;
	String asistenciaAnio = "", asistenciaNumero = "";
	String sFechaJuicio = "";
	String auxJuicio = "";
	String sHorasJuicio = "";
	String sMinutosJuicio = "";
	String calidad ="";
	String nig = "";	
	
	String maxLenghtProc = "20";
	String estilo = "box", readOnly="false", estiloCombo="boxCombo";
	String idPretension = "",pretension="";
	String turno = "";
	String[] datoJuzgado 	= null;
	String filtrarModulos = "N";
	String comboJuzgados ="", comboModulos="";
	String art27 = "";
	 
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
		
		if(filtrarModulos.equals("S")){
	    	datoJuzgado = new String[5];
	    	datoJuzgado [0] = "-1";
			datoJuzgado [1] = fechaApertura;
			datoJuzgado [2] = fechaApertura;
	    	datoJuzgado [3] = usr.getLocation();
	    	datoJuzgado [4] = "-1";
	    	comboJuzgados = "comboJuzgadosTurnosModulos";
	    	comboModulos = "comboProcedimientosVigencia";
	    }else{
	    	datoJuzgado = new String[2];
	    	datoJuzgado [0] = usr.getLocation();
			datoJuzgado [1] = "-1";
			comboJuzgados = "comboJuzgadosTurno";
	    	comboModulos = "comboProcedimientos";
	    }

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

			fechaEstado = GstDate.getFormatedDateShort("",
					(String) resultado.get("FECHAESTADO"));
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
			
			if(beanDesigna.getProcedimiento() != null && !beanDesigna.getProcedimiento().toString().equals("")){
				idProcedimiento = beanDesigna.getProcedimiento().toString();
			}
			
			if(filtrarModulos.equals("S")){
				if(!idProcedimiento.equals(""))
					datoJuzgado[0]=idProcedimiento;
			}

			procedimientoSel.add(0, idProcedimiento + "," + usr.getLocation());
		}

		// Datos del juzgado seleccionado:
		if (beanDesigna.getIdJuzgado() != null && !beanDesigna.getIdJuzgado().equals("") && 
			beanDesigna.getIdInstitucionJuzgado() != null &&!beanDesigna.getIdInstitucionJuzgado().equals("")) {
				idJuzgado = beanDesigna.getIdJuzgado().toString();
				idInstitucionJuzgado = beanDesigna.getIdInstitucionJuzgado().toString();
				if(filtrarModulos.equals("S")){
					juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado+","+idProcedimiento+","+fechaApertura+","+fechaApertura);
					datoJuzgado[4]=beanDesigna.getIdJuzgado().toString();
				}else{
					juzgadoSel.add(0, idJuzgado + "," + idInstitucionJuzgado);
					datoJuzgado[1]=beanDesigna.getIdJuzgado().toString();
				}

		} else {
			if (request.getAttribute("idDesigna") != null) {
				Hashtable datosDesigna = (Hashtable) request.getAttribute("idDesigna");
				idJuzgado = (String) datosDesigna.get(ScsDesignaBean.C_IDJUZGADO);
				idInstitucionJuzgado = (String) datosDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
				numero = (String) datosDesigna.get(ScsDesignaBean.C_NUMERO);
				if(filtrarModulos.equals("S")){
					juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado+","+idProcedimiento+","+fechaApertura+","+fechaApertura);
				}else{
					juzgadoSel.add(0, idJuzgado + "," + idInstitucionJuzgado);
				}
			}
		}

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
	
	// datos2 es para idPresentacion
	String[] datos2={usr.getLocation(),usr.getLanguage(),"-1"};
	if(beanDesigna.getIdPretension()!=null && (!beanDesigna.getIdPretension().toString().equals("")))
		datos2[1]= beanDesigna.getIdPretension().toString();
	
	String asterisco = "&nbsp(*)&nbsp";
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
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
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script type="text/javascript" src="<%=app%>/html/js/SIGA.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery-ui.js'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/js/calendarJs.jsp"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="gratuita.editarDesigna.literal.titulo" localizacion="gratuita.editarDesigna.literal.location"/>
	
	<script language="JavaScript">

	
		//<!-- Valida el numero de procedimiento (n/aaaa) -->
		function validaProcedimiento( strValue ) 
		{
			var objRegExp  = /^([0-9]+\/[0-9]{4})?$/;
			return objRegExp.test(strValue);
		}
	
		//<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			document.BuscarDesignasForm.action="JGR_Designas.do";
			document.BuscarDesignasForm.modo.value="volverBusqueda";

			document.BuscarDesignasForm.submit();
		}

		
		//<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{	
			<%if (pcajgActivo>0){%>
				var error = "";

				
				if (<%=obligatorioTipoDesigna%> && document.getElementById("tipo").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.editarDesigna.literal.tipo'/>"+ '\n';

				if(<%=obligatoriojuzgado%> && document.getElementById("juzgado").value==""){										
					error += "<siga:Idioma key='gratuita.editarDesigna.juzgado'/>"+ '\n';
				}
				if (<%=obligatorioModulo%> && document.getElementById("idProcedimiento").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.modulo'/>"+ '\n';
					
				if (<%=obligatorioProcedimiento%> && document.getElementById("idPretension").value=="")
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.pretensiones'/>"+ '\n';
				if(<%=validarProcedimiento%>){
					if(!validaProcedimiento(document.getElementById("numeroProcedimiento").value))
						error += "<siga:Idioma key='gratuita.procedimientos.numero.formato'/>"+ '\n';
				}
				

				
				if(error!=""){
					alert(error);
					fin();
					return false;
				}
		 	<%}%>
			var estado = trim(document.forms[0].estado.value); // Cogemos el estado de la designa del formulario
			var estadoOriginal = trim(document.forms[0].estadoOriginal.value); // Cogemos el estado original 
			if ((estado == "A") && (estadoOriginal != "A")){// Si es un cambio a anulacion (V,F -> A)...
				if (confirm("<siga:Idioma key='gratuita.compensacion.confirmacion'/>"))
						 // y desea compensar al letrado ...
				{
					document.forms[0].compensar.value = "1";
				}else{
					document.forms[0].compensar.value = "0";
				}
			}else if((estadoOriginal == "A") && (estado != "A")){ // Desanulacion (A -> V,F)
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
		 }else{
		 
		 alert('<siga:Idioma key="gratuita.informeJustificacionMasiva.mensaje.aviso.fechaRequerida"/>');
		 fin();
		 return false;
		 }
			
			document.forms[0].action="JGR_MantenimientoDesignas.do";
			document.forms[0].modo.value="modificar";
			document.forms[0].target="submitArea";
			document.forms[0].submit();
		}
		
		function accionAnular() 
		{	
			if(document.forms[0].tipo.selectedIndex<1)
				alert('<siga:Idioma key="gratuita.editarDesigna.literal.tipo"/>');
			else{
					if (document.forms[0].juzgado.value!='') {

						if (confirm('<siga:Idioma key="messages.anular.confirmacion"/>')) 
						{
							document.getElementById("tdTextoAnulacion").style.visibility = "visible";
							document.getElementById("tdFechaAnulacion").style.visibility = "visible";
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
		function muestraFecAnulacion()
		{
			var anulada=<%=anulada%>;
			if(anulada)
			{
				document.getElementById("tdTextoAnulacion").style.visibility = "visible";
				document.getElementById("tdFechaAnulacion").style.visibility = "visible";
			}
		}
		
		//<!-- Asociada al boton Consultar Designa -->
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

 		function obtenerJuzgado() { 
			if (document.forms[0].codigoExtJuzgado.value!=""){
				document.MantenimientoJuzgadoForm.nombreObjetoDestino.value="juzgado";	
 			    document.MantenimientoJuzgadoForm.codigoExt2.value=document.forms[0].codigoExtJuzgado.value;
				document.MantenimientoJuzgadoForm.submit();
			}
		}
	
		function traspasoDatos(resultado){
		 	document.getElementById("juzgado").value=resultado[0];
		}
		
	function cambiarJuzgado(comboJuzgado) {
		if(comboJuzgado.value!=""){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
	   			type: "POST",
				url: "/SIGA/JGR_MantenimientoJuzgados.do?modo=getAjaxJuzgado2",
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
 			if (arrayResultado==undefined||arrayResultado[0]==undefined){
 			   		
 		   	} 
 		   	else {
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
		 	
	function accionCerrar() {
			
	}

	function cargarComboModulo() {
		<% if (!modo.equalsIgnoreCase("ver")) { %>
			document.getElementById("juzgado").onchange();	
		<% } %>	
	}
	</script>
	 
</head>

<body onload ="cargarComboModulo();">

<table class="tablaTitulo" cellspacing="0" heigth="38" width="100%"
	border="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
		<%
			String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_sufijo="";
			ScsDesignaAdm adm = new ScsDesignaAdm(usr);
			Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(),
					anio, numero, idTurno);

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
		%> <%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
		- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%>
		<%=UtilidadesString.mostrarDatoJSP(t_apellido2)%></td>
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
		<html:hidden name="MaestroDesignasForm" property="modo"  styleId="modo" value="" />
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
						}else{
							claveDesigna=codigo;
						}
						%>
						<td class="labelText">
						 <siga:Idioma key="facturacion.ano" /> / <siga:Idioma key="gratuita.busquedaDesignas.literal.codigo" />
						</td>
						<td class="labelText">
							<html:text name="MaestroDesignasForm" property="anio" size="4" maxlength="10" styleClass="boxConsulta" value="<%=anio%>"  readonly="true"></html:text>/
							<html:text name="MaestroDesignasForm" property="codigo" size="10" maxlength="10" styleClass="boxConsulta" value="<%=claveDesigna%>"  readonly="true"></html:text>
						</td>
						
						<td style="display: none">
							<html:text name="MaestroDesignasForm" property="numero" size="8" maxlength="10" styleClass="boxConsulta" value="<%=numero%>" readonly="true"></html:text>
						</td>
						<td class="labelText">
							<siga:Idioma key='sjcs.designa.general.letrado' />
						</td>
						<td>
							<input type="text" class="boxConsulta" value="<%=nume_colegiado%>  <%=nombre_letrado%>" readOnly="true" style="width: 600">
						</td>
						<td colspan="4"></td>
					</tr>
					</table>
					<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaSOJ.literal.turno" />
						</td>
						<td>
							<html:text name="MaestroDesignasForm" property="turno" styleClass="boxConsulta" value="<%=turno%>" readonly="true"></html:text>
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
								<siga:Idioma key="gratuita.editarDesigna.literal.art27Texto"/>&nbsp;
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
							<%if(obligatorioTipoDesigna){%>
						 		<%= asterisco %>
							<%}%>
						</td>
						<%
							ArrayList vTipo = new ArrayList();
							String s1 = (String) resultado.get(ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO);
							vTipo.add(s1 == null || s1 == "-1" ? "0" : s1);
						%>
						<td colspan="2">
						<% if (!modo.equalsIgnoreCase("ver")) { %> 
							<siga:ComboBD  pestana="true" nombre="tipo" tipo="tipoDesignaColegio" estilo="true" clase="boxCombo" elementoSel="<%=vTipo%>" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" />
						<% } else { %> 
							<siga:ComboBD pestana="true" nombre="tipo" tipo="tipoDesignaColegio" estilo="true" clase="boxConsulta" elementoSel="<%=vTipo%>" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="true" />
						<% } %>
						</td>
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.editarDesigna.literal.estado" />
						</td>
						<td>
						<% if (!modo1.equalsIgnoreCase("ver")) { %> 
						<Select name="estado"
							class="boxCombo">
							<option value='V'
								<%if((estado!=null)&&(estado.equalsIgnoreCase("V"))){%> 
									selected
								<%}%>
								><siga:Idioma key="gratuita.designa.estado.abierto" />
							</option>
							<option value='F'
								<%if((estado!=null)&&(estado.equalsIgnoreCase("F"))){%> 
									selected
								<%}%>
								><siga:Idioma key="gratuita.designa.estado.finalizado" />
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
									if ((estado != null)
											&& (estado.equalsIgnoreCase("F")))
										valorEstado = "Finalizado";
									else {
										if ((estado != null)
												&& (estado.equalsIgnoreCase("A")))
											valorEstado = "Anulado";
									}
								} %> 
 							<html:text name="MaestroDesignasForm" property="estado" maxlength="10" styleClass="boxConsulta" value="<%=valorEstado%>" readonly="true"></html:text> 
						<%}%> 
							<html:hidden name="MaestroDesignasForm" property="estadoOriginal" value="<%=estado%>"></html:hidden>
						</td>

						<td class="labelText">
							<siga:Idioma key="gratuita.editarDesigna.literal.fechaEstado" />
						</td>
						<td>
							<html:text name="MaestroDesignasForm" property="fechaEstado" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fechaEstado%>" readonly="true"></html:text>
						</td>
						<td class="labelText" id="tdTextoAnulacion" style="text-align: rigth; display: none">
							<siga:Idioma key="gratuita.editarDesigna.literal.fechaAnulacion" />
						</td>
						<td id="tdFechaAnulacion" style="text-align: rigth; display: none">
							<html:text name="MaestroDesignasForm" property="fechaAnulacion" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fechaAnulacion%>" readonly="true"></html:text> &nbsp;
						</td>
						<td class="labelText">
							<siga:Idioma key="gratuita.editarDesigna.literal.fechaCierre" />
						</td>
						<td>
						<% if (!modo.equalsIgnoreCase("ver")) {%> 
							<siga:Fecha nombreCampo="fechaCierre"   valorInicial="<%=fecha%>" />
							 
						<%} else { %>
							<siga:Fecha nombreCampo="fechaCierre"   valorInicial="<%=fecha%>" disabled="true" readOnly="true" />  							
 						<% } %>
						</td>
					</tr>
					<tr>
						<td class="labelText" style="vertical-align: middle;">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento" />
						</td>
						<td style="vertical-align: middle;">
						<% if (!modo.equalsIgnoreCase("ver")) { %> 
							<html:text name="MaestroDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="box" value="<%=numeroProcedimiento%>"></html:text> 
						<% } else { %> 
							<html:text name="MaestroDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="boxConsulta" value="<%=numeroProcedimiento%>" readonly="true"></html:text> 
						<% } %>
						</td>
						<td colspan="5"><!-- Busqueda automatica de juzgados--> 						
						<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">
							<table>
								<tr>
									<% if (!modo.equalsIgnoreCase("ver")) { %>
										<td class="labelText" width="16%"><siga:Idioma
											key="gratuita.mantenimientoTablasMaestra.literal.codigoext" /><% if (obligatoriojuzgado){ %>
											<%= asterisco %>
											<%}%>
										</td>
										<td class="labelText" width="10%">
											<input type="text" name="codigoExtJuzgado" class="box" size="8" maxlength="10" onChange="obtenerJuzgado();" />&nbsp;
										</td>
										<td>&nbsp;</td>
									<% } %>

									<% if (!modo.equalsIgnoreCase("ver")) { %> 
										<td width="80%">
											<siga:ComboBD nombre="juzgado" tipo="<%=comboJuzgados%>" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=datoJuzgado%>" elementoSel="<%=juzgadoSel%>" ancho="500" pestana="t" accion="Hijo:idProcedimiento; cambiarJuzgado(this);"/>
										</td> 
									<% } else { %> 
										<td width="100%">
									 		<siga:ComboBD nombre="juzgado" tipo="<%=comboJuzgados%>" estilo="" clase="boxComboConsulta" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=datoJuzgado%>" elementoSel="<%=juzgadoSel%>" ancho="700" pestana="t" accion="Hijo:idProcedimiento" readonly="true" />
										</td> 
									<% } %>
								</tr>
							</table>
						</siga:ConjCampos> 
<!------------------> <%-- Juzgado --%>
						</td>

					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
						</td>
						<td > 
							<% if (!modo.equalsIgnoreCase("ver")) { %>
							 	<input name="nig" size="15" type="text" value="<%=nig%>" class="<%=estilo%>" maxlength="50"/>
							<%}else{%>
								<input name="nig" size="15" type="text" value="<%=nig%>" class="boxConsulta"/>
							<%}%>						
						</td>
					</tr>					
					
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.actuacionesDesigna.literal.modulo" /> 
							<%if (obligatorioModulo){ %>
								<%= asterisco %>
							<%}%>
						</td>
						<td colspan="7">
						<%-- Procedimiento --%> 
						<% if (!modo.equalsIgnoreCase("ver")) { %>
							<siga:ComboBD nombre="idProcedimiento" tipo="<%=comboModulos%>" estilo="true" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" ancho="750" obligatorio="false" elementoSel="<%=procedimientoSel%>" hijo="t" pestana="t" /> 
						<% } else { %>
							<siga:ComboBD nombre="idProcedimiento" tipo="<%=comboModulos%>" estilo="true" clase="boxComboConsulta" filasMostrar="1" seleccionMultiple="false" ancho="750" obligatorio="false" readonly="true"  elementoSel="<%=procedimientoSel%>" hijo="t" pestana="t" /> 
						<% } %>
						</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.editarDesigna.literal.asunto" />
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
							<%if(modo.equals("editar")){%>
								<siga:ComboBD nombre="idPretension" tipo="comboPretensiones" ancho="380" clase="<%=estiloCombo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false" parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="false"/>           	   
							<%}else{%>
								<siga:ComboBD nombre="idPretension" tipo="comboPretensiones" ancho="380" clase="boxConsulta" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false"  parametro="<%=datos2%>" elementoSel="<%=pretensionesSel%>" hijo="t" readonly="true"/>           	   
							<%}%>
						</td>
					</tr>
					<!-- JBD 16/2/2009 INC-5739-SIGA -->
				</table>
			</siga:ConjCampos> <siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.juicio">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%">
					<tr>
						<td class="labelText" style="width: 80px"><siga:Idioma
							key="gratuita.editarDesigna.literal.fechaJuicio" /></td>
						<td class="labelText" style="width: 150px">
						<% if (!modo.equalsIgnoreCase("ver")) { %> 
							<siga:Fecha nombreCampo="fechaJuicio"   valorInicial="<%=sFechaJuicio%>"/>														 
						<% } else { %> 
							<siga:Fecha nombreCampo="fechaJuicio"   valorInicial="<%=sFechaJuicio%>" disabled="true" readOnly="true" />							 
						<% } %>
						</td>
						<td class="labelText" style="width: 80px">
							<siga:Idioma key="gratuita.editarDesigna.literal.horaJuicio" /></td>
						<td class="labelText">
						<% if (!modo.equalsIgnoreCase("ver")) { %> 
							<html:text name="MaestroDesignasForm" property="horasJuicio" value="<%=sHorasJuicio%>" size="1" maxlength="2" styleClass="box" readonly="false"></html:text> : <html:text name="MaestroDesignasForm" property="minutosJuicio" value="<%=sMinutosJuicio%>" size="1" maxlength="2" styleClass="box" readonly="false"></html:text> 
						<% } else { %> 
							<html:text name="MaestroDesignasForm" property="horasJuicio" value="<%=sHorasJuicio%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true"></html:text> : <html:text name="MaestroDesignasForm" property="minutosJuicio" value="<%=sMinutosJuicio%>" size="1" maxlength="2" styleClass="boxConsulta" readonly="true"></html:text> 
						<% } %>
						</td>
					</tr>
				</table>
			</siga:ConjCampos> <siga:ConjCampos
				leyenda="gratuita.busquedaDesignas.literal.observaciones">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%">
					<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.editarDesigna.literal.fechaOficioJuzgado"/></td>
					<%if (modo.equalsIgnoreCase("ver")){%>
						<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(fechaOficioJuzgado)%></td>
					<%}else{%>										
					<td>
						<siga:Fecha nombreCampo="fechaOficioJuzgado" valorInicial="<%=fechaOficioJuzgado%>"></siga:Fecha>
						
					</td>
					<%} %>
					<td class="labelText">
						<siga:Idioma key="gratuita.editarDesigna.literal.fechaRecepcionColegio"/>
					</td>
					<%if (modo.equalsIgnoreCase("ver")){%>
						<td class="labelTextValor"><%=UtilidadesString.mostrarDatoJSP(fechaRecepcionColegio)%></td>
					<%}else{%>								
					<td>
						<siga:Fecha nombreCampo="fechaRecepcionColegio" valorInicial="<%=fechaRecepcionColegio%>"></siga:Fecha>
						
					</td>
					<%}%>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.editarDesigna.literal.observaciones" />
						</td>
						<td>
						<% if (!modo.equalsIgnoreCase("ver")) { %> 
							<textarea scroll="none" name="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="7" style="width: 300" class="box"><%=observaciones%></textarea> 
						<% } else { %>
							<textarea scroll="none" name="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="7" style="width: 300" class="boxConsulta" readonly="true"><%=observaciones%></textarea>
						<% } %>
						</td>
						<td class="labelText"><siga:Idioma
							key="gratuita.general.literal.comentariosDelitos" />
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
		<%
 		//if(asistenciaBean != null){ 
 		//	asistenciaAnio   = "" + asistenciaBean.getAnio();
 		//	asistenciaNumero = "" + asistenciaBean.getNumero();
 		%> <%
 		// 	<siga:ConjCampos leyenda="gratuita.generalDesigna.literal.relacionado">
 		%>
			<!--	<table class="tablaCampos" align="center" cellpadding="0" cellpadding="0" width="100%">-->
			<!--				<tr>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.generalDesigna.literal.asistencia"/>-->
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.nuevaAsistencia.literal.turno"/>-->
			<!--				</td>--> <!--				<td class="labelTextValor">--> <%
 		//=UtilidadesString.mostrarDatoJSP(nombreTurnoAsistencia)
 		%>
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.nuevaAsistencia.literal.guardia"/>-->
			<!--				</td>--> <!--				<td class="labelTextValor">--> <%
 	//=UtilidadesString.mostrarDatoJSP(nombreGuardiaAsistencia)
 %>
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.generalDesigna.literal.anio"/>-->
			<!--				</td>--> <!--				<td class="labelTextValor">--> <%
 	//=UtilidadesString.mostrarDatoJSP(asistenciaBean.getAnio())
 %>
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.generalDesigna.literal.numero"/>:&nbsp;-->
			<!--				</td>--> <!--				<td class="labelTextValor">--> <%
 	//=UtilidadesString.mostrarDatoJSP(asistenciaBean.getNumero())
 %>
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<siga:Idioma key="gratuita.generalDesigna.literal.fecha"/>-->
			<!--				</td>--> <!--				<td class="labelTextValor">--> <%
 	//=GstDate.getFormatedDateShort("",asistenciaBean.getFechaHora())
 %>
			<!--				</td>--> <!--				<td class="labelText">--> <!--					<img src="<%=app%>/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="<siga:Idioma key='gratuita.generalDesigna.boton.ConsultarAsistencia'/>" name="consultarDesigna" border="0" onclick="consultarAsistenciaFuncion()">-->
			<!--				</td>--> <!--		    </tr>--> <!--		  </table>--> <%
 	//</siga:ConjCampos>
 %>
			<%
				//}
			%>
			</td>
		</tr>
</table>

</html:form>

<html:form action="/JGR_Asistencia.do" method="POST"
	target="mainWorkArea">
	<html:hidden property="modo" value="ver" />
	<html:hidden property="anio" value="<%=asistenciaAnio%>" />
	<html:hidden property="numero" value="<%=asistenciaNumero%>" />
	<html:hidden property="desdeDesigna" value="si" />
</html:form>
<html:form action="/JGR_MantenimientoJuzgados.do" method="POST"
	target="submitArea">
	<input type="hidden" name="modo" value="buscarJuzgado">
	<html:hidden property="codigoExt2" value="" />
	<html:hidden property="nombreObjetoDestino" value="" />
</html:form>
<!-- SI NO EXISTE FECHA DE ANULACIÓN MOSTRAMOS EL BOTÓN ANULARPOR SI DESEA ANULAR LA DESIGNA //-->
<siga:ConjBotonesAccion botones="G,R,V" clase="botonesDetalle"
	modo="<%=modo1%>" />

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

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>

</body>
</html>