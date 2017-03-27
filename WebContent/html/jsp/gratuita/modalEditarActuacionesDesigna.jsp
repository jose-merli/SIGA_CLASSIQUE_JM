<!DOCTYPE html>
<html>
<head>
<!-- modalEditarActuacionesDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.List"%>
<%@ page import="com.siga.gratuita.form.ActuacionesDesignasForm"%>


<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	String profile[] = usr.getProfile();

	boolean esLetrado = usr.isLetrado();
	boolean justificacionValidada;

	String anio = "", numero = "", turno = "", idTurno = "", fecha = "", ncolegiado = "", nombre = "", apellido1 = "", apellido2 = "", codigo = "", idJuzgadoDesigna = "", juzgadoInstitucionDesigna = "", idPretensionDesigna = "";
	String nactuacion = "", fechaActuacion = "", acuerdoExtrajudicial = "";
	String anulacion = "", observaciones = "", fechaJustificacion = "", observacionesJustificacion = "", modo = "", fechaComboFiltros = "";
	String idPersona = null;
	String numeroProcedimiento = "";
	String anioProcedimiento = "";
	String nig = "";
	String maxLenghtProc = "20";

	String estiloCombo = null, readOnlyCombo = null;

	Hashtable hashDesigna = null;
	Hashtable hashActuacion = null;
	ArrayList vTipoRatificacion = new ArrayList();

	// Arrays de los combos de juzgado, comisaria y prision:
	ArrayList prisionSel = new ArrayList();
	ArrayList juzgadoSel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	ArrayList acreditacionSel = new ArrayList();
	ArrayList pretensionSel = new ArrayList();
	ArrayList motCambioSel = new ArrayList();

	String idJuzgado = null, idInstitucionJuzgado = null, idPrision = null, idInstitucionPrision = null, idProcedimiento = "-1", idInstitucionProcedimiento = null, idAcreditacion = null, idMotivoCambio = null;
	String idTipoRatificacion = null, fechaRatificacion = null, fechaNotificacion = null, anioEJG = null, numEJG = null;
	String idPretension = null;
	String nombreJuzgado = "", nombreProcedimiento = "", nombreAcreditacion = "";
	String deDonde = (String) request.getParameter("deDonde");
	String validarJustificaciones = "";
	String estadoActuacion = "";
	String actuacionValidada = "";
	String modoJustificacion = (String) request.getAttribute("modoJustificacion");
	String modoAnterior = (String) request.getAttribute("MODO_ANTERIOR");
	String facturada = "";
	String validarActuacion = (String) request.getAttribute("validarActuacion");
	String filtrarModulos = ClsConstants.FILTRAR_MODULOS_FECHAACTUAL;
	String comboJuzgados = "", comboModulos = "", comboJuzgadosJustificacion = "";
	String[] datoJuzg = null;
	String dato[] = { (String) usr.getLocation() };
	if (request.getAttribute("filtrarModulos") != null) {
		filtrarModulos = (String) request.getAttribute("filtrarModulos");
	}

	// Estilo de los combos:
	if (modoAnterior != null && modoAnterior.equalsIgnoreCase("VER") || (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha"))) {
		estiloCombo = "boxConsulta";
		readOnlyCombo = "true";
	} else {
		estiloCombo = "boxCombo";
		readOnlyCombo = "false";
	}

	//Hash con la designa y la actuacion:
	hashDesigna = (Hashtable) request.getAttribute("hashDesigna");

	anio = (String) hashDesigna.get("ANIO");
	numero = (String) hashDesigna.get("NUMERO");
	codigo = (String) hashDesigna.get("CODIGO");
	turno = (String) hashDesigna.get("TURNO");
	idTurno = (String) hashDesigna.get("IDTURNO");
	fecha = GstDate.getFormatedDateShort("", (String) hashDesigna.get("FECHA"));
	//	ncolegiado =(String)hashDesigna.get("NCOLEGIADO");
	//	nombre = (String)hashDesigna.get("NOMBRE");
	//	apellido1 = (String)hashDesigna.get("APELLIDO1");
	//	apellido2 = (String)hashDesigna.get("APELLIDO2");
	idTipoRatificacion = (String) hashDesigna.get("IDTIPORATIFICACIONEJG");

	idJuzgadoDesigna = (String) hashDesigna.get(ScsDesignaBean.C_IDJUZGADO);
	juzgadoInstitucionDesigna = (String) hashDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);

	idPretensionDesigna = (String) hashDesigna.get(ScsDesignaBean.C_IDPRETENSION);

	String fechaAnulacion = hashDesigna.get("FECHAANULACION") == null ? "" : (String) hashDesigna.get("FECHAANULACION");
	String param[] = { usr.getLocation(), idTurno };

	// Caso de estar en Edicion o Consulta:

	idInstitucionProcedimiento = usr.getLocation();
	String nombreFacturacion = "";
	if (!modoAnterior.equalsIgnoreCase("NUEVO")) {
		hashActuacion = (Hashtable) request.getAttribute("hashActuacionActual");

		if (hashActuacion != null && hashActuacion.size() > 0) {
			nombreFacturacion = (String) hashActuacion.get("NOMBREFACTURACION");
			//Nuevo numero de actuacion:
			nactuacion = (String) hashActuacion.get("NUMEROASUNTO");
			//INC_3094_SIGA el colegiado se toma de la actuación, no de la designación
			ncolegiado = (String) hashActuacion.get("NCOLEGIADO");
			nombre = (String) hashActuacion.get("NOMBRE");
			apellido1 = (String) hashActuacion.get("APELLIDO1");
			apellido2 = (String) hashActuacion.get("APELLIDO2");

			// Datos de la actuacion modificables:
			fechaActuacion = GstDate.getFormatedDateShort("", (String) hashActuacion.get("FECHAACTUACION"));
			acuerdoExtrajudicial = (String) hashActuacion.get("ACUERDOEXTRAJUDICIAL");
			anulacion = (String) hashActuacion.get("ANULACION");
			if (hashActuacion.get("OBSERVACIONES") != null) {
				observaciones = (String) hashActuacion.get("OBSERVACIONES");
			}
			fechaJustificacion = GstDate.getFormatedDateShort("", (String) hashActuacion.get("FECHAJUSTIFICACION"));

			if (hashActuacion.get("OBSERVACIONESJUSTIFICACION") != null) {
				observacionesJustificacion = (String) hashActuacion.get("OBSERVACIONESJUSTIFICACION");
			}

			nombreAcreditacion = (String) hashActuacion.get("NOMBREACREDITACION");
			nombreJuzgado = (String) hashActuacion.get("NOMBREJUZGADO");
			if (hashActuacion.get("NOMBREPROCEDIMIENTO") != null) {
				nombreProcedimiento = (String) hashActuacion.get("NOMBREPROCEDIMIENTO");
			}
			// Datos de la Prision seleccionada:
			idPrision = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDPRISION);
			facturada = (String) hashActuacion.get("FACTURADO");
			idInstitucionPrision = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION);
			if (idPrision != null && idInstitucionPrision != null)
				prisionSel.add(0, idPrision + "," + idInstitucionPrision);

			// Datos de la Pretension seleccionada:
			idPretension = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDPRETENSION);
			if (idPretension != null) {
				pretensionSel.add(0, idPretension);
			}

			// Datos del motivo del cambio seleccionado
			idMotivoCambio = (String) hashActuacion.get(ScsActuacionDesignaBean.C_ID_MOTIVO_CAMBIO);
			if (idMotivoCambio != null) {
				motCambioSel.add(0, idMotivoCambio);
			}

			// Datos del Procedimiento seleccionado:
			if (hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO) != null && !((String) hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO)).equals("")) {
				idProcedimiento = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO);
			}

			// Datos de la Acreditacion seleccionada:
			idAcreditacion = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
			if (idAcreditacion != null)
				acreditacionSel.add(0, idAcreditacion);

			// Datos del Juzgado seleccionado:
			idJuzgado = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDJUZGADO);

			idInstitucionJuzgado = (String) hashActuacion.get(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO);

			actuacionValidada = (String) hashActuacion.get("ACTUACIONVALIDADA");
			numeroProcedimiento = (String) hashActuacion.get("NUMEROPROCEDIMIENTO");
			anioProcedimiento = (String) hashActuacion.get("ANIOPROCEDIMIENTO");

			nig = (String) hashActuacion.get("NIG");
		}

	} else { //Para el caso de estar en NUEVO:

		//Cuando es el caso de NUEVO el letrado se coge de la designa.
		ncolegiado = (String) hashDesigna.get("NCOLEGIADO");
		nombre = (String) hashDesigna.get("NOMBRE");
		apellido1 = (String) hashDesigna.get("APELLIDO1");
		apellido2 = (String) hashDesigna.get("APELLIDO2");

		// Datos del Juzgado seleccionado:
		idJuzgado = (String) hashDesigna.get(ScsDesignaBean.C_IDJUZGADO);
		idInstitucionJuzgado = (String) hashDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
		// Datos del Procedimiento seleccionado:
		if (hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO) != null && !((String) hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO)).equals("")) {
			idProcedimiento = (String) hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO);
		}
		idPretension = (String) hashDesigna.get(ScsDesignaBean.C_IDPRETENSION);
		if (idPretension != null) {
			pretensionSel.add(0, idPretension);
		}
		String aux = (String) request.getAttribute("fechaJustificacion");
		fechaJustificacion = aux != null ? aux : "";
		//Datos de la designa:
		nactuacion = (String) hashDesigna.get("NUMEROASUNTO");
		idPersona = (String) hashDesigna.get("IDPERSONA");
		nombreJuzgado = (String) hashDesigna.get("NOMBREJUZGADO");
		if (hashDesigna.get("NOMBREPROCEDIMIENTO") != null) {
			nombreProcedimiento = (String) hashDesigna.get("NOMBREPROCEDIMIENTO");
		}
		numeroProcedimiento = (String) hashDesigna.get("NUMPROCEDIMIENTO");
		anioProcedimiento = (String) hashDesigna.get("ANIOPROCEDIMIENTO");
		nig = (String) hashDesigna.get("NIG");
		if (validarActuacion != null)
			actuacionValidada = validarActuacion != null && validarActuacion.equals("S") ? "0" : "1";
		fechaComboFiltros = GstDate.getHoyJsp();
	}
	//  Datos de la designa comunes a todos los modos de visualizacion:

	String[] paramPretension = { usr.getLocation(), "-1" };
	if (idPretension != null && (!idPretension.equals("")))
		paramPretension[1] = idPretension;

	String[] paramMotivoCambio = new String[] { usr.getLocation() };
	validarJustificaciones = (String) hashDesigna.get("VALIDARJUSTIFICACIONES");
	actuacionValidada = actuacionValidada == null ? "0" : actuacionValidada;

	/////////  COMBOS POR FILTRO ///////////////////
	if (filtrarModulos.equals(ClsConstants.FILTRAR_MODULOS_FECHAACTUAL)) {
		datoJuzg = new String[3];
		datoJuzg[0] = usr.getLocation();
		datoJuzg[1] = idTurno;
		datoJuzg[2] = "-1";
		comboJuzgados = "comboJuzgadosJurisdiccion";
		comboModulos = "comboProcedimientos";
		comboJuzgadosJustificacion = "comboProcedimientosJustificacion";

		//Actualizo el combo de juzgados:
		if (idJuzgado != null && idInstitucionJuzgado != null)
			juzgadoSel.add(0, idJuzgado + "," + idInstitucionJuzgado);

		if (idJuzgado != null && !idJuzgado.equals(""))
			datoJuzg[2] = idJuzgado;
		
	} else {
		if (filtrarModulos.equals(ClsConstants.FILTRAR_MODULOS_FECHADESIGNACION)) {
			fechaComboFiltros = fecha;
		} else if (fechaComboFiltros.equals("") && filtrarModulos.equals(ClsConstants.FILTRAR_MODULOS_FECHAACTUACION)) {
			fechaComboFiltros = fechaActuacion;
		}		

		datoJuzg = new String[6];
		datoJuzg[0] = "-1";
		datoJuzg[1] = fechaComboFiltros;
		datoJuzg[2] = fechaComboFiltros;
		datoJuzg[3] = usr.getLocation();
		datoJuzg[4] = idTurno;
		datoJuzg[5] = "-1";
		comboJuzgados = "comboJuzgadosJurisdiccionModulos";
		comboModulos = "comboProcedimientosVigencia";
		comboJuzgadosJustificacion = "comboProcedimientosJustificacionVigencia";

		//Actualizo el combo de juzgados:
		if ((idJuzgado != null && idInstitucionJuzgado != null) && (idProcedimiento != null && idInstitucionProcedimiento != null))
			juzgadoSel.add(0, idJuzgado + "," + idInstitucionJuzgado + "," + idProcedimiento + "," + fechaComboFiltros + "," + fechaComboFiltros);

		if (idJuzgado != null && !idJuzgado.equals(""))
			datoJuzg[5] = idJuzgado;

		if (idProcedimiento != null && !idProcedimiento.equals(""))
			datoJuzg[0] = idProcedimiento;
	}

	if (idProcedimiento != null && idInstitucionProcedimiento != null)
		procedimientoSel.add(0, idProcedimiento + "," + idInstitucionProcedimiento);

	String paramAcreditacion[] = { idProcedimiento, usr.getLocation() };

	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}

	if (pcajgActivo == 2)
		maxLenghtProc = "15";

	String asterisco = "&nbsp;(*)";

	//si es el colegio de Alcala validaremos el procedimiento y el numero de procedimiento
	boolean isColegioAlcala = pcajgActivo == CajgConfiguracion.TIPO_CAJG_TXT_ALCALA;

	ArrayList vTipoResolAuto = new ArrayList();
	if (hashDesigna.containsKey(ScsEJGBean.C_IDTIPORESOLAUTO) && hashDesigna.get(ScsEJGBean.C_IDTIPORESOLAUTO) != "")
		vTipoResolAuto.add(hashDesigna.get(ScsEJGBean.C_IDTIPORESOLAUTO).toString());

	String fechaAuto = "";
	if (hashDesigna.containsKey("FECHAAUTO"))
		fechaAuto = GstDate.getFormatedDateShort("", hashDesigna.get(ScsEJGBean.C_FECHAAUTO).toString()).toString();
%>

<%@page import="com.siga.ws.CajgConfiguracion"%>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- validaciones struct -->
	<html:javascript formName="ActuacionesDesignasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	

	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
	
	
	<!-- fin validaciones struct -->

	<script type="text/javascript" >
	
	
	function cambiarAcreditacion() {
		<%if (!isColegioAlcala) {%>
			acreditacionString = document.getElementById("acreditacion").value
			acreditacionStrings = acreditacionString.split(',');

			if(acreditacionStrings.length>1 && acreditacionStrings[1]=='1'){
				jQuery("#labelNumProc").attr('style','display: block');
				jQuery("#labelNig").attr('style','display: block');
			 }else{
				 jQuery("#labelNumProc").attr('style','display: none');
				 jQuery("#labelNig").attr('style','display: none');
			 } 
		<%} else {%>
			acreditacionString = document.getElementById("acreditacion").value
			acreditacionStrings = acreditacionString.split(',');
	
			if(acreditacionStrings.length>1 && acreditacionStrings[1]=='1'){
				jQuery("#labelNig").attr('style','display: block');
			 }else{
				 jQuery("#labelNig").attr('style','display: none');
			 }
		<%}%>
		
	}	
		
		
		
		var bJuzgado=false;
		// Funcion que obtiene el juzgado buscando por codigo externo	
		function obtenerJuzgado() { 
			if (document.forms[0].codigoExtJuzgado.value!=""){
				document.MantenimientoJuzgadoForm.codigoExt2.value=document.forms[0].codigoExtJuzgado.value;
				document.MantenimientoJuzgadoForm.submit();		
			 }
			else {
				document.getElementById("juzgado").value=-1;			
				bJuzgado=true;
				document.getElementById("juzgado").onchange();
			}
		}
		
		function obtenerProcedimiento() { 
			
			if (document.getElementById("codigoBusquedaAux").value!= ""){
				document.MantenimientoProcedimientosForm.codigoBusqueda.value = document.getElementById("codigoBusquedaAux").value;
				document.MantenimientoProcedimientosForm.submit();		
			 }
		}		
		
		function traspasoDatos(resultado){			
			if (resultado[0]==undefined) {
				document.getElementById("juzgado").value=-1;
				document.getElementById("codigoExtJuzgado").value = "";
			} else {
				document.getElementById("juzgado").value=resultado[0];
			}
			
			bJuzgado=true;
			document.getElementById("juzgado").onchange();
		}
		
		function traspasoProcDatos(resultado){		
			if (resultado[0]==undefined) {
				//document.forms[0].procedimiento.value=-1; 
				document.getElementById("codigoBusquedaAux").value = "";
			} else {
				var procSelect = jQuery("#procedimientoFrame").contents().find("select");
				procSelect.val(resultado[0]);
				procSelect.change();
			}
		}
		
		function cambiarJuzgado(comboJuzgado) {
			if (bJuzgado)
				bJuzgado=false;
			else {
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
				
				bJuzgado=true;
				document.getElementById("juzgado").onchange();
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

		jQuery(document).ready(function(){
			jQuery('#mainDiv').height(jQuery(window).height()-62);
		});
		
		
		function tab(pestana,panel)
		{
			pst 	= document.getElementById(pestana);
			pnl 	= document.getElementById(panel);
			psts	= document.getElementById('tabs').getElementsByTagName('li');
			pnls	= document.getElementById('paneles').getElementsByTagName('div');
			
			// eliminamos las clases de las pestañas
			for(i=0; i< psts.length; i++)
			{
				psts[i].className = '';
				psts[i].style.width="100px";
			}
			
			// Añadimos la clase "actual" a la pestaña activa
			pst.className = 'actual';
			
			// eliminamos las clases de las pestañas
			for(i=0; i< pnls.length; i++)
			{
				pnls[i].style.display = 'none';
			}
			
			// Añadimos la clase "actual" a la pestaña activa
			pnl.style.display = 'block';
		}
		
	</script>		
</head>

<body onload="cambiarAcreditacion();">


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.actuacionesDesigna.literal.titulo"/>
		</td>
	</tr>
</table>

	<!-- Comienzo del formulario con los campos -->	
	<%
			String aDonde = "";
			if (deDonde != null && ((deDonde.equals("ficha") && usr.isLetrado()) || deDonde.equals("/JGR_PestanaDesignas"))) {
				aDonde = "/JGR_ActuacionDesignaLetrado.do";
			} else {
				deDonde = "";
				aDonde = "/JGR_ActuacionesDesigna.do";
			}
		%>	
		
<html:form action="<%=aDonde%>" method="post" target="submitArea">
	<html:hidden property = "modo" value= ""/>
	<html:hidden property = "deDonde" value="<%=deDonde%>"/>
	<html:hidden property = "actuacionValidada" value="<%=actuacionValidada%>"/>

	<html:hidden property = "idPersona" value="<%=idPersona%>" />
	<html:hidden property = "idTurno" value= "<%=idTurno%>"/>
	<html:hidden property = "idInstitucion" value="<%=usr.getLocation()%>"/>
	<html:hidden property = "anio" value="<%=anio%>" />	
	<html:hidden property = "numero" value="<%=numero%>" />
	<html:hidden property = "nactuacion" value="<%=nactuacion%>" />

	
		
		
<div id="mainDiv" style="overflow-y:auto;overflow-x:hidden;padding-left:5px; position: relative;">

			<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.Designacion">
				<table width="100%" >
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
						</td>
						<td>
							<html:text name="ActuacionesDesignasForm" property="turno" size="60" styleClass="boxConsulta" value="<%=turno%>" readonly="true" />
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.busquedaEJG.literal.anyo"/>
						</td>
						<td>
							<html:text name="ActuacionesDesignasForm" property="anio" size="5" styleClass="boxConsulta" value="<%=anio%>" readonly="true" />
						</td>	
										
						<td class="labelText" style="display:none" >
							<siga:Idioma key="gratuita.listadoAsistencias.literal.numero"/>
						</td>					
						<td style="display:none" >
					    	<html:text name="ActuacionesDesignasForm" property="numero" size="5" styleClass="boxConsulta" value="<%=numero%>" readonly="true" />
						</td>		
									
						<td class="labelText">
							<siga:Idioma key="gratuita.listadoAsistencias.literal.numero"/>
						</td>
						<td>
							<html:text name="ActuacionesDesignasForm" property="codigo" size="5" styleClass="boxConsulta" value="<%=codigo%>" readonly="true" />
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fechaDesigna"/>
						</td>
						<td>
							<html:text name="ActuacionesDesignasForm" property="fecha" size="10" styleClass="boxConsulta" value="<%=fecha%>" readonly="true" />
						</td>
					</tr>
				</table>
			</siga:ConjCampos>

			<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.letrado" desplegable="true">
				<table width="100%">
					<tr>
						<td class="labelText" width="15%">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.ncolegiado"/>
						</td>
						<td width="5%">
							<html:text name="ActuacionesDesignasForm" property="ncolegiado" size="5" styleClass="boxConsulta" value="<%=ncolegiado%>" readonly="true" />
						</td>
						
						<td class="labelText" width="15%">
							<siga:Idioma key="gratuita.busquedaAsistencias.literal.nombre"/>
						</td>
						<td class="labelTextValor" colspan="5">
							<%=nombre%> <%=apellido1%> <%=apellido2%>
						</td>
					</tr>
				</table>
			</siga:ConjCampos>

			<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.titulo">
				<table width="100%" border="0">
					<tr>				
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.actuacionesAsistencia.literal.fechaActuacion"/>&nbsp;(*)
						</td>
						
						<td>
						<%
							if (modoAnterior == null || modoAnterior.equalsIgnoreCase("ver")) {
						%> 
							<siga:Fecha nombreCampo="fechaActuacion"  disabled="true" valorInicial="<%=fechaActuacion%>" ></siga:Fecha> 
						<%
 							} else {
 						%>
							<siga:Fecha nombreCampo="fechaActuacion"   valorInicial="<%=fechaActuacion%>" ></siga:Fecha> 
						<%
 							}
 						%>							
							
						</td>
							
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.actuacionesAsistencia.literal.nactuacion"/>
						</td>
						<td>
							<html:text name="ActuacionesDesignasForm" property="nactuacion" size="10" value="<%=nactuacion%>" styleClass="boxConsulta" readonly="true"/>
						</td>
							
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalActuacionesDesigna.literal.anulacion"/>
							&nbsp;
							
							<%
															if (!modoAnterior.equalsIgnoreCase("VER") && (modoJustificacion == null || !modoJustificacion.equals("editarJustificacionFicha"))) {
																		if (!fechaAnulacion.equals("")) {
														%>
										<INPUT NAME="anulacion" TYPE=CHECKBOX <%if ((anulacion != null) && (anulacion).equalsIgnoreCase("1")) {%>checked<%}%> disabled>
							<%
								} else {
							%>	
										<INPUT NAME="anulacion" TYPE=CHECKBOX <%if ((anulacion != null) && (anulacion).equalsIgnoreCase("1")) {%>checked<%}%>>
							
							<%
															}
														%>
							
							<%
															} else {
														%>
								<INPUT NAME="anulacion" TYPE=CHECKBOX <%if ((anulacion != null) && (anulacion).equalsIgnoreCase("1")) {%>checked<%}%> disabled>
							<%
								}
							%>
						</td>
					</tr>
					
					<tr>			
						<td class="labelText" nowrap>
							<table>
							<tr>
							<td><siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento" /></td><%=(isColegioAlcala ? "<td>" + asterisco + "</td>" : "<td id='labelNumProc' style='display:block'>(*)</td>")%>	
							</tr> 
							</table>
							
						</td>
						<td>
							<%
								if (!modoAnterior.equalsIgnoreCase("VER")) {
							%> 
							
								<c:choose>
									<c:when	test="${EJIS_ACTIVO=='1'}">
									
										<html:text
											name="ActuacionesDesignasForm" property="numeroProcedimiento"
											size="7" maxlength="7" styleClass="box" value="<%=numeroProcedimiento%>" />/<html:text
											name="ActuacionesDesignasForm" property="anioProcedimiento" 
											size="4" maxlength="4" styleClass="box" value="<%=anioProcedimiento%>" />
	
									</c:when>
									<c:otherwise>
										<html:text name="ActuacionesDesignasForm" property="numeroProcedimiento" style="width:100px" maxlength="<%=maxLenghtProc%>" styleClass="box" value="<%=numeroProcedimiento%>"/>
									</c:otherwise>
								</c:choose>
							
							
								 
							<%
																						 								} else {
																						 							%> 
								<c:choose>
									<c:when	test="${EJIS_ACTIVO=='1'}">
											<c:out value="<%=numeroProcedimiento%>"/>/<c:out value="<%=anioProcedimiento%>"/>
									</c:when>
									<c:otherwise>
									<html:text name="ActuacionesDesignasForm" property="numeroProcedimiento" style="width:100px" maxlength="<%=maxLenghtProc%>" styleClass="boxConsulta" value="<%=numeroProcedimiento%>" readonly="true"/>
									</c:otherwise>
								</c:choose>
								
								 
							<%
																 								}
																 							%>
						</td>
									
			            <td class="labelText" nowrap>	
						 	<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>&nbsp;(*)
					  	</td>
					  	<td colspan="2">	 
							<%
	 								if (modoAnterior.equalsIgnoreCase("VER") || (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha"))) {
	 							%>							
									<siga:ComboBD nombre="juzgado" ancho="500" tipo="<%=comboJuzgados%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=datoJuzg%>"  elementoSel="<%=juzgadoSel%>" accion="Hijo:procedimiento"/>
							
							<%
															} else {
														%>
								<input type="text" name="codigoExtJuzgado" class="box" size="8"  style="margin-top:0px;" maxlength="10" onBlur="obtenerJuzgado();" />
								<siga:ComboBD nombre="juzgado" ancho="400" tipo="<%=comboJuzgados%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>" parametro="<%=datoJuzg%>"  elementoSel="<%=juzgadoSel%>"  accion="Hijo:procedimiento; cambiarJuzgado(this);"/>
							<%
								}
							%>
						</td>														
					</tr>	
					
					<tr>
						<td class="labelText" nowrap>
							<table>
								<tr>
								<td><siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/></td><td id='labelNig' style='display:block'>(*)</td>
								</tr>
							</table>
						</td>				
						<td > 
							<%
 								if (!modoAnterior.equalsIgnoreCase("VER")) {
 							%> 
								<html:text name="ActuacionesDesignasForm" property="nig" styleId="nig" value="<%=nig%>" styleClass="box" style="size:19;width:200px"/>
							<%
								} else {
							%>
								<html:text name="ActuacionesDesignasForm" property="nig" styleId="nig" value="<%=nig%>" styleClass="boxConsulta" readonly="true" style="size:19;width:200px"/>
							<%
								}
							%>						
						</td>
									
							<td id="info" style="display:none" ><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
						</td>
						<td colspan="2"></td>
					</tr>	
							
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo"/>&nbsp;(*)			
						</td>						
						<td colspan="4">											
							<%
																			if (modoJustificacion != null && modoJustificacion.equals("nuevoJustificacion")) {
																		%>
			                	<siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboJuzgadosJustificacion%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>" accion="Hijo:acreditacion" />
								
							<%
																} else if ((esLetrado || modoAnterior.equalsIgnoreCase("VER")) || (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha"))) {
															%>
								<html:text name="ActuacionesDesignasForm" style="width:600px" property="procedimiento1" styleClass="boxConsulta" readonly="true" value="<%=nombreProcedimiento%>"/>
								
							<%
																} else {
															%>				
								<input type="text" id="codigoBusquedaAux" name="codigoBusquedaAux" class="box" size="8"  style="margin-top:0px;" maxlength="10" onBlur="obtenerProcedimiento();" />
			                	<siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboModulos%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>" accion="Hijo:acreditacion" />
							<%
								}
							%>
						</td>
					</tr>		
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.procedimientos.literal.acreditacion"/>&nbsp;(*)
						</td>			
						<td  colspan="4">
							<%
								if (modoJustificacion != null && modoJustificacion.equals("nuevoJustificacion")) {
							%>
								<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>"  hijo="t" elementoSel="<%=acreditacionSel%>" accion="parent.cambiarAcreditacion();"/>
							<%
								} else if (modoAnterior.equalsIgnoreCase("VER") || (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha"))) {
							%>
								<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="true"   parametro="<%=paramAcreditacion%>" elementoSel="<%=acreditacionSel%>" />	
							<%
									} else {
												if (esLetrado) {
								%>
									<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>"   parametro="<%=paramAcreditacion%>" elementoSel="<%=acreditacionSel%>"  accion="parent.cambiarAcreditacion();" />
								<%
									} else {
								%>	
									<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readonly="<%=readOnlyCombo%>"  hijo="t" elementoSel="<%=acreditacionSel%>"  accion="parent.cambiarAcreditacion();"/>
								<%
									}
											}
								%>
						</td>
					</tr>
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prision"/>
						</td>
						<td colspan="4">
							<%
								if (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha")) {
							%>
								<siga:ComboBD  ancho="300" nombre="prision" tipo="comboPrisiones"  estilo="true" clase="boxConsulta" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=param%>" elementoSel="<%=prisionSel%>" />
							<%
								} else {
							%>
								<siga:ComboBD  ancho="300" nombre="prision" tipo="comboPrisiones"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=param%>" elementoSel="<%=prisionSel%>" />
							 
								<font class="labelTextValor" style="vertical-align: middle;">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prisionCompletar"/>
								</font>
							<%
								}
							%>
							
						</td>
					</tr>
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.actuacionesDesigna.literal.pretensiones"/><%=(isColegioAlcala ? asterisco : "")%>
						</td>
						<td colspan="4">
						<%
							if (modoJustificacion != null && modoJustificacion.equals("editarJustificacionFicha")) {
						%>
								<siga:ComboBD  ancho="300" nombre="pretension" tipo="comboPretensiones"  estilo="true" clase="boxConsulta" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=paramPretension%>" elementoSel="<%=pretensionSel%>" />
							<%
								} else {
							%>
								<siga:ComboBD  ancho="300" nombre="pretension" tipo="comboPretensiones"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=paramPretension%>" elementoSel="<%=pretensionSel%>" />										 
								<%
										 									if (pcajgActivo == CajgConfiguracion.TIPO_CAJG_TXT_ALCALA) {
										 								%>
									<font class="labelText">
										<siga:Idioma key="gratuita.altaGuardia.literal.motivoCambio"/>
									</font>
									<siga:ComboBD  ancho="300" nombre="idMotivoCambio" tipo="cmbActuacionDesignaMotivoCambio"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readonly="<%=readOnlyCombo%>" parametro="<%=paramMotivoCambio%>" elementoSel="<%=motCambioSel%>" />
								<%
									} else {
								%>
									<html:hidden property = "idMotivoCambio" value="<%=idMotivoCambio%>"/>
								<%
									}
								%>
								
							<%
																}
															%>
							
						</td>					
					</tr>
								
					<tr>
						<td class="labelText" nowrap style="vertical-align: middle;">
							<siga:Idioma key="gratuita.altaGuardia.literal.observaciones"/>
						</td>
						<td  colspan="4">
							<%
								if (!modoAnterior.equalsIgnoreCase("VER") && (modoJustificacion == null || !modoJustificacion.equals("editarJustificacionFicha"))) {
							%>
								<textarea class="box" name="observaciones"
									onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" style="overflow:auto;width:600px;height:45px" 
								><%=observaciones%></textarea>
							<%
								} else {
							%>
								<textarea class="boxConsulta" name="observaciones" readonly
									style="overflow:auto;width:600px;height:40px"
								><%=observaciones%></textarea>
							<%
								}
							%>							
						</td>
					</tr>
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.actuacionesDesigna.literal.talonario"/>&nbsp;/&nbsp;
							<siga:Idioma key="gratuita.actuacionesDesigna.literal.talon"/>					     
						</td>
						<td  colspan="4">	
							 <%
								 	if (!modoAnterior.equalsIgnoreCase("VER") && (modoJustificacion == null || !modoJustificacion.equals("editarJustificacionFicha"))) {
								 %>
								<html:text name="ActuacionesDesignasForm" property="talonario" size="20" maxlength="20" styleClass="box" />
								&nbsp;/&nbsp;
					     		<html:text name="ActuacionesDesignasForm" property="talon" size="20"  maxlength="20" styleClass="box" />
						     <%
						     	} else {
						     %>	
					     		<html:text name="ActuacionesDesignasForm" property="talonario" size="20" styleClass="boxConsulta" readonly="true"/>
					     		&nbsp;/&nbsp;
					     		<html:text name="ActuacionesDesignasForm" property="talon" size="20" styleClass="boxConsulta" readonly="true"/>
						     <%
						     	}
						     %>				     
						</td>			
					</tr>
				</table>
			</siga:ConjCampos>			

			<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.justificacion"  desplegable="true">
				<table width="100%" border="0">
					<tr>
						<td class="labelText">	
							<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha"/>
						</td>
						<td>	
						<% if (modoAnterior.equalsIgnoreCase("ver") || actuacionValidada.equals("1") || deDonde.equals("/JGR_PestanaDesignas")|| (modoJustificacion!=null && modoJustificacion.equals("editarJustificacionFicha"))) { %>
						<script type="text/javascript">
						jQuery(function(){
							jQuery("#fechaJustificacion").removeClass("box");
							jQuery("#fechaJustificacion").addClass("boxConsulta");
							jQuery("#fechaJustificacion").attr("readonly", true);
							jQuery("#fechaJustificacion-datepicker-trigger").hide();
						});
						</script>
						 <%
						 	}
						 %>
						<siga:Fecha nombreCampo="fechaJustificacion"   valorInicial="<%=fechaJustificacion%>" ></siga:Fecha> 
						
												
						</td>						

						<td>
							<%
								if (!modoAnterior.equals("VER") && !usr.isLetrado() && !deDonde.equals("/JGR_PestanaDesignas") && (facturada != null) && (!facturada.equals("1")) && (modoJustificacion == null || !modoJustificacion.equals("editarJustificacionFicha"))) {
							%>
								<input type="button" id="idButton" alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>" id="bValidarActuacion" onclick="validarJustificacion();" class="button" value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
							<%
								}
							%>
						</td>
						
						<td class="labelText">
							<%
								if (actuacionValidada.equals("1")) {
							%>	
								<input name="estadoActuacion" type="text" class="boxConsulta" value='<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>' readonly style="width:400px; border:0px solid">
							<%
								} else {
							%>
								<input name="estadoActuacion" type="text" class="boxConsulta" value="<siga:Idioma key='gratuita.mantActuacion.literal.actuacionPteValidar'/>" readonly style="width:400px; border:px solid">
							<%
								}
							%>
						</td>
					</tr>
					<tr>
						<td class="labelText" width="130px" style="vertical-align: middle;">
							<siga:Idioma key="gratuita.altaGuardia.literal.observaciones"/>
						</td>
						<td colspan="3">
							<%
								if (!modoAnterior.equalsIgnoreCase("VER") && !usr.isLetrado() && (modoJustificacion == null || !modoJustificacion.equals("editarJustificacionFicha"))) {
							%>
								<textarea class="box" name="observacionesJustificacion"
									onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" style="overflow:auto;width:600px;height:45px"
								><%=observacionesJustificacion%></textarea>
							<%
								} else {
							%>
								<textarea class="boxConsulta" name="observacionesJustificacion" readonly 
									style="overflow:auto;width:600px;height:40px" 
								><%=observacionesJustificacion%></textarea>
							<%
								}
							%>
						</td>
					</tr>
				</table>
		</siga:ConjCampos>
		
		
		
		<c:if test="${not empty ActuacionesDesignasForm.ejgs}">
		
		<%
			ActuacionesDesignasForm form = (ActuacionesDesignasForm) request.getSession().getAttribute("ActuacionesDesignasForm");
					int i = 0;
					if (form != null) {
						List<ScsEJGBean> listadoEjgs = form.getEjgs();
		%>
				<siga:ConjCampos leyenda="gratuita.operarEJG.literal.expedienteEJG" >
				<div id="panelEJGs" style="display: inline;overflow-x: hidden;">
					<div style="position:relative; left:0px; width:100%; height:30px; top:0px; " id="divid">
					<logic:notEmpty name="ActuacionesDesignasForm" property="ejgs">
						<table  class="tablaLineaPestanasArriba"  >
							<tr>
							<td></td>
							</tr>
							</table>
							<table id="tabs" class="pest" style="width:100%;border-bottom: 2px;border-bottom-color: black;">
								<tr>
									<logic:iterate name="ActuacionesDesignasForm" property="ejgs" id="ejg1" indexId="index">
										
											<td class="pestanaTD"   name="pestanas" style="width:15px" >
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
					    		
					<div id="paneles" style="height:100px;overflow-y: auto; ">
					<logic:iterate name="ActuacionesDesignasForm" property="ejgs" id="ejg2" indexId="index2">
						<div id="panel_${index2}"  style="display: inline;overflow-x: hidden;">
						
								<table class="tablaCampos" align="center" cellpadding="0"
									cellpadding="0" style="width:100%;" border="0">
								<tr>
									<td class="labelText" style="width:200px;">
										<siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>
									</td>	
				 
									<td class="labelTextValue">
									<c:choose>
									<c:when test="${ejg2.idTipoRatificacionEJG != null && ejg2.idTipoRatificacionEJG !=''}">
								<%
									ArrayList selTipoRatificacion = new ArrayList();
																ScsEJGBean ejg = (ScsEJGBean) listadoEjgs.get(i++);
																selTipoRatificacion.add(ejg.getIdTipoRatificacionEJG() + "," + usr.getLocation());
								%>	
												<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucionTodos" ancho="300" clase="boxConsulta" parametro="<%=dato%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=selTipoRatificacion%>" readonly="true"/>				
										</c:when>
										<c:otherwise>
											-
											<%
											i++;
										%>
										</c:otherwise>
									</c:choose>
									</td>
									<td class="labelText" style="width:200px;">
										<siga:Idioma key="gratuita.operarRatificacion.literal.fechaResolucionCAJG"/>
									</td>	
										
									<td class="labelTextValue">
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
								
								<tr>
									<td class="labelText" style="width:200px;">
										<siga:Idioma key="gratuita.operarRatificacion.literal.fechaNotificacion"/>
									</td>
										
									<td class="labelTextValue">
									<c:choose>
										<c:when test="${ejg2.fechaNotificacion != null && ejg2.fechaNotificacion!=''}">
											<siga:Fecha nombreCampo="fechaNotificacion" valorInicial="${ejg2.fechaNotificacion}" disabled="true" readOnly="true"></siga:Fecha>	
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
									</td>	
									
									<td class="labelText" style="width:160px;" >
										<siga:Idioma key="gratuita.operarRatificacion.literal.fechaRatificacion"/>
									</td>
										
									<td class="labelTextValue">
									<c:choose>
										<c:when test="${ejg2.fechaRatificacion != null && ejg2.fechaRatificacion!=''}">
											<siga:Fecha nombreCampo="fechaRatificacion" valorInicial="${ejg2.fechaRatificacion}" disabled="true" readOnly="true"></siga:Fecha>	
										</c:when>
										<c:otherwise>
											-
										</c:otherwise>
									</c:choose>
									</td>					
								</tr>							
								<c:if test="${ejg2.fechaAuto!=null && ejg2.fechaAuto !=''}">
									<tr>
										<td class="labelText">
											<siga:Idioma key="gratuita.EJG.literal.autoResolutorio"/>
										</td>	
										<td class="labelTextValue">
											<c:choose>
												<c:when test="${ejg2.nombreTipoResolAuto != null && ejg2.nombreTipoResolAuto!=''}">
													<html:text name="DefinirEJGForm" property="nombreTipoResolAuto" size="30" styleClass="boxConsulta" value="${ejg2.nombreTipoResolAuto}" disabled="false" readonly="true"/>		
												</c:when>	
												<c:otherwise>
													-
												</c:otherwise>	
											</c:choose>
										</td>
										
										<td class="labelText">
											<siga:Idioma key="pestana.justiciagratuitaejg.impugnacion"/>
											<siga:Idioma key="gratuita.operarRatificacion.literal.fechaAuto"/>
										</td>
										<td class="labelTextValue">
											<siga:Fecha nombreCampo="fechaAuto" valorInicial="${ejg2.fechaAuto}" disabled="true" readOnly="true"></siga:Fecha>
										</td>
										
									</tr>
									</c:if>
								
								</table>
							
							</div>
						</logic:iterate>	
						</div>
						<script type="text/javascript">
							tab('tab_0','panel_0');
							ajusteAlto('panelEJGs');
						</script>
					</div>
			</siga:ConjCampos> 
			<%
 				}
 			%>	
 			
			</c:if>
	
	
	<%
				if (nombreFacturacion != null && !nombreFacturacion.equals("")) {
			%>

				<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.facturacion">
					<table width="100%">
						<tr>
							<td width="15%">&nbsp; </td>
							<td width="85%">&nbsp; </td>
						</tr>
						
						<tr>
							<td class="labelText" width="15%">
								<siga:Idioma key="gratuita.actuacionesDesigna.literal.facturacion"/>:
							</td>
							<td class="labelTextValor" style="text-align: left;">
								<%=nombreFacturacion%>
							</td>
						</tr>
						<tr>
							<td>&nbsp; </td>
							<td>&nbsp;
						</tr>
					</table>
				</siga:ConjCampos>

	<%
		}
	%>	

</div>

</html:form>		
	
   <html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
	</html:form>
	
	<html:form action = "/JGR_MantenimientoProcedimientos.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarProcedimiento">
		<html:hidden property = "codigoBusqueda" value=""/>
	</html:form>

<%
	if (modoAnterior.equalsIgnoreCase("VER")) {
%>
	<siga:ConjBotonesAccion botones="C" modal="G"/>
<%
	} else {
%>
	<siga:ConjBotonesAccion botones="Y,C" modal="G"/>
<%
	}
%>
	

<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">
	
		//Asociada al boton Cerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
			
			<%if (modoJustificacion != null && modoJustificacion.equalsIgnoreCase("editarJustificacionFicha")) {%>
				error = '';
				if( document.forms[0].fechaActuacion.value==''){
					error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesAsistencia.literal.fechaActuacion'/>"+ '\n';	
				}	
				if(compararFecha (document.forms[0].fechaActuacion.value, document.forms[0].fecha.value)==2){
					error += "<siga:Idioma key='messages.error.acreditacionFechaNoValida'/>"+ '\n';
				}
				if ((<%=isColegioAlcala%> ||  jQuery("#labelNumProc").css('display')=='block') && document.forms[0].numeroProcedimiento.value=='') {
					error += "<siga:Idioma key='errors.required' arg0='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento'/>"+ '\n';
				}
				if((<%=isColegioAlcala%> || jQuery("#labelNumProc").css('display')=='block') && document.forms[0].numeroProcedimiento.value!='' &&  document.forms[0].anioProcedimiento &&  document.forms[0].anioProcedimiento.value==""){
					error += "<siga:Idioma key='errors.required' arg0='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento'/>"+ '\n';
					
				}
						
				if(<%=isColegioAlcala%> && !validaProcedimiento(document.forms[0].numeroProcedimiento.value)) {
					error += "<siga:Idioma key='gratuita.procedimientos.numero.formato'/>"+ '\n';
				}
				var nigAux = document.getElementById("nig").value;
				nigAux = formateaNig(nigAux);
				
				if (jQuery("#labelNig").css('display')=='block' && nigAux=='') {
					error += "<siga:Idioma key='errors.required' arg0='gratuita.mantAsistencias.literal.NIG'/>"+ '\n';
				}
				if(!validarNig(nigAux)){	
					error += "<siga:Idioma key='gratuita.nig.formato'/>"+ '\n';
				}
				document.forms[0].nig.value = nigAux;
				if (error!=''){
					fin();
					alert(error);
					return false;
				}
				
			<%} else {%>
				error = '';
					fecha = document.getElementById("fechaJustificacion");
					//Comparar fecha
					if(document.forms[0].fechaActuacion.value==''){
						error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesAsistencia.literal.fechaActuacion'/>"+ '\n';		
					}
					if(compararFecha (document.forms[0].fechaActuacion.value, document.forms[0].fecha.value)==2){
						error += "<siga:Idioma key='messages.error.acreditacionFechaNoValida'/>"+ '\n';
					}
					if ((<%=isColegioAlcala%> || jQuery("#labelNumProc").css('display')=='block' ) && document.forms[0].numeroProcedimiento.value=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento'/>"+ '\n';
					}
					if((<%=isColegioAlcala%> || jQuery("#labelNumProc").css('display')=='block' )&& document.forms[0].numeroProcedimiento.value!='' && document.forms[0].anioProcedimiento &&  document.forms[0].anioProcedimiento.value==""){
						error += "<siga:Idioma key='errors.required' arg0='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento'/>"+ '\n';
					}
					
					if(<%=isColegioAlcala%>  && !validaProcedimiento(document.forms[0].numeroProcedimiento.value)) {
						error += "<siga:Idioma key='gratuita.procedimientos.numero.formato'/>"+ '\n';
					}
					if (document.forms[0].juzgado.value=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.mantenimientoTablasMaestra.literal.juzgado'/>"+ '\n';
					}
					if (document.forms[0].procedimiento.value=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.modulo'/>"+ '\n';
							
					}
					if (document.forms[0].acreditacion.value=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.procedimientos.literal.acreditacion'/>"+ '\n';
					}	
	
					if (<%=isColegioAlcala%> && document.forms[0].pretension.value=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.actuacionesDesigna.literal.pretensiones'/>"+ '\n';
					}
					<%if (isColegioAlcala) {%>					
						if (document.forms[0].idMotivoCambio.value=='') {
																			
							if ('<%=(idJuzgadoDesigna + "," + juzgadoInstitucionDesigna)%>' != document.forms[0].juzgado.value) {
								error += "<siga:Idioma key='messages.gratuita.actuacionesDesigna.distintoJuzgado' />"+ '\n';
							}
							if ('<%=(idPretensionDesigna != null ? idPretensionDesigna : "")%>' != document.forms[0].pretension.value) {
								error += "<siga:Idioma key='messages.gratuita.actuacionesDesigna.distintoProcedimiento' />"+ '\n';
							}
						}
					<%}%>
					var nigAux = document.getElementById("nig").value;
					nigAux = formateaNig(nigAux);
					if (jQuery("#labelNig").css('display')=='block' && nigAux=='') {
						error += "<siga:Idioma key='errors.required' arg0='gratuita.mantAsistencias.literal.NIG'/>"+ '\n';
					}
					
					if(!validarNig(nigAux)){	
						error += "<siga:Idioma key='gratuita.nig.formato'/>"+ '\n';
							
					}
					document.forms[0].nig.value = nigAux;
					if (error!=''){
						fin();
						alert(error);
						return false;
					}
				
			<%}%>
			
			 
			<%if (modoJustificacion != null && modoJustificacion.equalsIgnoreCase("editarJustificacionFicha")) {%>
				document.forms[0].modo.value="modificarJustificacionFicha";
			<%} else if (modoAnterior.equalsIgnoreCase("EDITAR")) {%>
				document.forms[0].modo.value="modificar";
			<%} else {%>
				document.forms[0].modo.value="insertar";
			<%}%>
			document.forms[0].submit();

			
		}		

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		
		
		function limpiarValor(combo){
			seleccionComboSiga (combo, "");
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

		function validarJustificacion () {
			if(document.forms[0].actuacionValidada.value=="1"){
				jQuery("#fechaJustificacion").removeClass("boxConsulta");
				jQuery("#fechaJustificacion").addClass("box");
				jQuery("#fechaJustificacion-datepicker-trigger").show();
				jQuery("#fechaJustificacion").attr("readonly", false);
				
				document.forms[0].actuacionValidada.value="0";
				
				document.forms[0].fechaJustificacion.value="";
				document.forms[0].estadoActuacion.value= "";
			}else{
				jQuery("#fechaJustificacion").removeClass("box");
				jQuery("#fechaJustificacion").addClass("boxConsulta");
				jQuery("#fechaJustificacion-datepicker-trigger").hide();
				jQuery("#fechaJustificacion").attr("readonly", true);
				document.forms[0].actuacionValidada.value="1";					
				document.forms[0].estadoActuacion.value='<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				if((document.forms[0].fechaJustificacion.value==null)||(document.forms[0].fechaJustificacion.value=="")){					
					document.forms[0].fechaJustificacion.value="<%=UtilidadesBDAdm.getFechaBD("")%>";
				}		
			}			
		}

		function validaProcedimiento( strValue ) {
			var objRegExp  = /^([0-9]+\/[0-9]{4})?$/;
			return objRegExp.test(strValue);
		}
		<%if (modoAnterior != null && !modoAnterior.equalsIgnoreCase("VER")) {%>
			if(document.getElementById("juzgado") && document.getElementById("juzgado").onchange)
				document.getElementById("juzgado").onchange();
		<%}%>
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>