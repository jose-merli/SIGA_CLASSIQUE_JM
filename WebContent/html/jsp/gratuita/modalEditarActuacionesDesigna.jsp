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


<!-- JSP -->
<% 
	String app=request.getContextPath();  
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	boolean esLetrado=usr.isLetrado();
	boolean justificacionValidada;

	String anio="", numero="", turno="", idTurno="", fecha="", ncolegiado="", nombre="", apellido1="", apellido2="", codigo="", idJuzgadoDesigna="", juzgadoInstitucionDesigna="", idPretensionDesigna="";
	String nactuacion = "", fechaActuacion="", acuerdoExtrajudicial="";
	String anulacion = "", observaciones = "", fechaJustificacion ="", observacionesJustificacion ="", modo="";
	String idPersona=null;
	String numeroProcedimiento="";
	String nig="";
	String maxLenghtProc = "20";
	
	String estiloCombo=null, readOnlyCombo=null;
	
	Hashtable hashDesigna = null;
	Hashtable hashActuacion = null;
	ArrayList vTipoRatificacion= new ArrayList();
	
	// Arrays de los combos de juzgado, comisaria y prision:
	ArrayList prisionSel = new ArrayList();
	ArrayList juzgadoSel = new ArrayList();
	ArrayList procedimientoSel = new ArrayList();
	ArrayList acreditacionSel = new ArrayList();
	ArrayList pretensionSel = new ArrayList();
	ArrayList motCambioSel = new ArrayList();

	String idJuzgado=null, idInstitucionJuzgado=null, idPrision=null, idInstitucionPrision=null, idProcedimiento="-1", idInstitucionProcedimiento=null, idAcreditacion=null, idMotivoCambio=null;
	String idTipoRatificacion=null,fechaRatificacion=null,fechaNotificacion=null,anioEJG=null;
	String idPretension=null;
	String nombreJuzgado="", nombreProcedimiento="", nombreAcreditacion="";
	String deDonde=(String)request.getParameter("deDonde");
	String validarJustificaciones = "";
	String estadoActuacion = "";
	String actuacionValidada = "";
	String modoJustificacion = (String) request.getAttribute("modoJustificacion");
	String modoAnterior = (String) request.getAttribute("MODO_ANTERIOR");
	String facturada="";
	String validarActuacion = (String) request.getAttribute("validarActuacion");
	String filtrarModulos = "N";
	String comboJuzgados ="", comboModulos="", comboJuzgadosJustificacion="";
	String[] datoJuzg = null;
	if (request.getAttribute("filtrarModulos") != null) {
		filtrarModulos = (String)request.getAttribute("filtrarModulos");
	}

	// Estilo de los combos:
	if (modoAnterior!=null && modoAnterior.equalsIgnoreCase("VER")) {
		estiloCombo = "boxConsulta";
		readOnlyCombo = "true";
	} else {
		estiloCombo = "boxCombo";
		readOnlyCombo = "false";
	}

	//Hash con la designa y la actuacion:
	hashDesigna = (Hashtable) request.getAttribute("hashDesigna");
	
	
	
	anio = (String)hashDesigna.get("ANIO");
	numero = (String)hashDesigna.get("NUMERO");
	codigo = (String)hashDesigna.get("CODIGO");
	turno = (String)hashDesigna.get("TURNO");
	idTurno = (String)hashDesigna.get("IDTURNO");
	fecha = GstDate.getFormatedDateShort("",(String)hashDesigna.get("FECHA"));
//	ncolegiado =(String)hashDesigna.get("NCOLEGIADO");
//	nombre = (String)hashDesigna.get("NOMBRE");
//	apellido1 = (String)hashDesigna.get("APELLIDO1");
//	apellido2 = (String)hashDesigna.get("APELLIDO2");
	idTipoRatificacion=(String)hashDesigna.get("IDTIPORATIFICACIONEJG");
	
	idJuzgadoDesigna=(String)hashDesigna.get(ScsDesignaBean.C_IDJUZGADO);
	juzgadoInstitucionDesigna=(String)hashDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
	
	idPretensionDesigna=(String)hashDesigna.get(ScsDesignaBean.C_IDPRETENSION);
	
	if (idTipoRatificacion!=null && !idTipoRatificacion.equals("")) {
			vTipoRatificacion.add(idTipoRatificacion);
		}
    anioEJG=(String)hashDesigna.get("ANIOEJG");
	if (anioEJG!=null && !anioEJG.equals("")){
	 fechaRatificacion=GstDate.getFormatedDateShort("",(String)hashDesigna.get("FECHARATIFICACION"));
	 fechaNotificacion=GstDate.getFormatedDateShort("",(String)hashDesigna.get("FECHANOTIFICACION"));
	}
	String fechaAnulacion = hashDesigna.get("FECHAANULACION") == null?"":(String)hashDesigna.get("FECHAANULACION");	
    String param[] = {usr.getLocation(),idTurno};
    
    if(filtrarModulos.equals("S")){
    	datoJuzg = new String[5];
    	datoJuzg [0] = "-1";
    	datoJuzg [1] = fecha;
    	datoJuzg [2] = fecha;
    	datoJuzg [3] = usr.getLocation();
    	datoJuzg [4] = "-1";
    	comboJuzgados = "comboJuzgadosTurnosModulos";
    	comboModulos = "comboProcedimientosVigencia";
    	comboJuzgadosJustificacion = "comboProcedimientosJustificacionVigencia";
    }else{
    	datoJuzg = new String[2];
    	datoJuzg [0] = usr.getLocation();
		datoJuzg [1] = "-1";
		comboJuzgados = "comboJuzgadosTurno";
    	comboModulos = "comboProcedimientos";
    	comboJuzgadosJustificacion = "comboProcedimientosJustificacion";
    }
	// Caso de estar en Edicion o Consulta:
	
	idInstitucionProcedimiento =  usr.getLocation();
	String nombreFacturacion = "";
	if (!modoAnterior.equalsIgnoreCase("NUEVO")) {
		hashActuacion = (Hashtable) request.getAttribute("hashActuacionActual");
		
		if(hashActuacion!=null && hashActuacion.size() > 0){
			nombreFacturacion =	(String)hashActuacion.get("NOMBREFACTURACION");
			//Nuevo numero de actuacion:
			nactuacion = (String)hashActuacion.get("NUMEROASUNTO");		
			//INC_3094_SIGA el colegiado se toma de la actuación, no de la designación
			ncolegiado =(String)hashActuacion.get("NCOLEGIADO");
			nombre = (String)hashActuacion.get("NOMBRE");
			apellido1 = (String)hashActuacion.get("APELLIDO1");
			apellido2 = (String)hashActuacion.get("APELLIDO2");
	
			// Datos de la actuacion modificables:
			fechaActuacion=GstDate.getFormatedDateShort("",(String)hashActuacion.get("FECHAACTUACION"));
			acuerdoExtrajudicial= (String)hashActuacion.get("ACUERDOEXTRAJUDICIAL");
			anulacion = (String)hashActuacion.get("ANULACION");
			if(hashActuacion.get("OBSERVACIONES")!=null){
				observaciones = (String)hashActuacion.get("OBSERVACIONES");
			}
			fechaJustificacion = GstDate.getFormatedDateShort("",(String)hashActuacion.get("FECHAJUSTIFICACION"));
			
			if(hashActuacion.get("OBSERVACIONESJUSTIFICACION")!=null){
				observacionesJustificacion = (String)hashActuacion.get("OBSERVACIONESJUSTIFICACION");
			}
			
			nombreAcreditacion = (String)hashActuacion.get("NOMBREACREDITACION");
	        nombreJuzgado = (String)hashActuacion.get("NOMBREJUZGADO");
	        if(hashActuacion.get("NOMBREPROCEDIMIENTO")!=null){
		    	nombreProcedimiento = (String)hashActuacion.get("NOMBREPROCEDIMIENTO");
	        }
		 	// Datos de la Prision seleccionada:
		 	idPrision =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDPRISION);
		 	facturada =  (String)hashActuacion.get("FACTURADO");
		 	idInstitucionPrision =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDINSTITUCIONPRISION);
			if (idPrision!=null && idInstitucionPrision!=null)
				prisionSel.add(0,idPrision+","+idInstitucionPrision);
				
		    // Datos de la Pretension seleccionada:
		 	idPretension =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDPRETENSION);
		 	if (idPretension!=null){
				pretensionSel.add(0,idPretension);
			}	
		 	
		 	 // Datos del motivo del cambio seleccionado
		 	idMotivoCambio =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_ID_MOTIVO_CAMBIO);
		 	if (idMotivoCambio!=null){
				motCambioSel.add(0,idMotivoCambio);
			}
			
		 	// Datos del Procedimiento seleccionado:
		 	if(hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO)!=null && !((String)hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO)).equals("")){
		 		idProcedimiento =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDPROCEDIMIENTO);
		 	}
		 	
		 	// Datos de la Acreditacion seleccionada:
		 	idAcreditacion =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDACREDITACION);
			if (idAcreditacion!=null)
				acreditacionSel.add(0,idAcreditacion);
				
		 	// Datos del Juzgado seleccionado:
		 	idJuzgado =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDJUZGADO);
		
			idInstitucionJuzgado =  (String)hashActuacion.get(ScsActuacionDesignaBean.C_IDINSTITUCIONJUZGADO);
			
			
	
			actuacionValidada = (String) hashActuacion.get("ACTUACIONVALIDADA");
			numeroProcedimiento = (String) hashActuacion.get("NUMEROPROCEDIMIENTO");
			nig = (String) hashActuacion.get("NIG");
		}
		
		
	} else { //Para el caso de estar en NUEVO:
	 	// Datos del Juzgado seleccionado:
	 	idJuzgado =  (String)hashDesigna.get(ScsDesignaBean.C_IDJUZGADO);
	 	idInstitucionJuzgado =  (String)hashDesigna.get(ScsDesignaBean.C_IDINSTITUCIONJUZGADO);
	 	// Datos del Procedimiento seleccionado:
	 	if(hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO)!=null && !((String)hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO)).equals("")){
	 		idProcedimiento =  (String)hashDesigna.get(ScsDesignaBean.C_IDPROCEDIMIENTO);
	 	}	 	
		idPretension = (String)hashDesigna.get(ScsDesignaBean.C_IDPRETENSION);
		if (idPretension!=null){
			pretensionSel.add(0,idPretension);
		}
		String aux = (String)request.getAttribute("fechaJustificacion");
		fechaJustificacion = aux!=null?aux:"";
	 	//Datos de la designa:
		nactuacion = (String)hashDesigna.get("NUMEROASUNTO");
		idPersona = (String)hashDesigna.get("IDPERSONA");
		 nombreJuzgado = (String)hashDesigna.get("NOMBREJUZGADO");
		if(hashDesigna.get("NOMBREPROCEDIMIENTO")!=null){
	    	nombreProcedimiento = (String)hashDesigna.get("NOMBREPROCEDIMIENTO");
        }
	    numeroProcedimiento = (String) hashDesigna.get("NUMPROCEDIMIENTO");
	    nig = (String) hashDesigna.get("NIG");
	    if(validarActuacion!=null)
	    	actuacionValidada = validarActuacion!=null&&validarActuacion.equals("S")?"0":"1";
	}
	//  Datos de la designa comunes a todos los modos de visualizacion:
	
	String[] paramPretension = {usr.getLocation(), "-1"};
	if(idPretension!=null && (!idPretension.equals("")))
		paramPretension[1]= idPretension;
	
	String[] paramMotivoCambio = new String[]{usr.getLocation()};
	validarJustificaciones = (String) hashDesigna.get("VALIDARJUSTIFICACIONES");
	actuacionValidada = actuacionValidada ==null ? "0":actuacionValidada;
	//Actualizo el combo de juzgados:
	if(filtrarModulos.equals("S")){
		if ((idJuzgado!=null && idInstitucionJuzgado!=null) && (idProcedimiento!=null && idInstitucionProcedimiento!=null))
			juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado+","+idProcedimiento+","+fecha+","+fecha);
		
		if(idJuzgado!=null && !idJuzgado.equals(""))
			datoJuzg[4]=idJuzgado;
		
		if(idProcedimiento!=null && !idProcedimiento.equals(""))
			datoJuzg[0]=idProcedimiento;
	}else{
		
		//Actualizo el combo de juzgados:
		if (idJuzgado!=null && idInstitucionJuzgado!=null)
			juzgadoSel.add(0,idJuzgado+","+idInstitucionJuzgado);
				
		if(idJuzgado!=null && !idJuzgado.equals(""))
			datoJuzg[1]=idJuzgado;
	}

	if (idProcedimiento!=null && idInstitucionProcedimiento!=null)
		procedimientoSel.add(0,idProcedimiento+","+idInstitucionProcedimiento);	
	
	String paramAcreditacion[] = {idProcedimiento,usr.getLocation()};	

	
	int pcajgActivo = 0;
	if (request.getAttribute("PCAJG_ACTIVO")!=null){
		pcajgActivo = Integer.parseInt(request.getAttribute("PCAJG_ACTIVO").toString());
	}
	
	if(pcajgActivo==2)
		maxLenghtProc = "15";
	
	
	String asterisco = "&nbsp(*)";
	
	boolean obligatorioNumeroProcedimiento = false;
	boolean obligatorioProcedimiento = false;
	boolean validaNumeroProcedimiento = false;
	
	if (pcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA) {
		obligatorioNumeroProcedimiento = true;
		obligatorioProcedimiento = true;
		validaNumeroProcedimiento = true;
	}
	
	
	%>

<%@page import="com.siga.ws.CajgConfiguracion"%><html>

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
	<!-- validaciones struct -->
	<html:javascript formName="ActuacionesDesignasForm" staticJavascript="false" />  
	<script type="text/javascript" src="<%=app%>/html/js/validacionStruts.js"></script>
	<!-- fin validaciones struct -->

	<script language="JavaScript">
	
		//Selecciona el valor del combo -->
		function rellenarCombos() {
			<% if (modoAnterior!=null && !modoAnterior.equalsIgnoreCase("VER")) { %>
				document.getElementById("juzgado").onchange();
			<% } %>
			return;
			;
		}
		
		// Funcion que obtiene el juzgado buscando por codigo externo	
		function obtenerJuzgado() { 
			if (document.forms[0].codigoExtJuzgado.value!=""){
				document.MantenimientoJuzgadoForm.codigoExt2.value=document.forms[0].codigoExtJuzgado.value;
				document.MantenimientoJuzgadoForm.submit();		
			 }
		}

		//					
		function traspasoDatos(resultado){
			seleccionComboSiga("juzgado",resultado[0]);		 	
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
	</script>		
</head>

<body onload="rellenarCombos()">


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.actuacionesDesigna.literal.titulo"/>
	</td>
</tr>
</table>

	<!-- Comienzo del formulario con los campos -->	
	<% String aDonde="";
        if (deDonde!=null && ((deDonde.equals("ficha") && usr.isLetrado())||deDonde.equals("/JGR_PestanaDesignas"))) {
    		aDonde="/JGR_ActuacionDesignaLetrado.do";
		} else {
     		deDonde="";
		    aDonde="/JGR_ActuacionesDesigna.do";
		} %>	
		
	<html:form action="<%=aDonde%>" method="post" target="submitArea">
		<html:hidden property = "modo" value= ""/>
		<html:hidden property = "deDonde" value="<%=deDonde%>"/>
		<html:hidden property = "actuacionValidada" value="<%=actuacionValidada%>"/>

		<html:hidden property = "idPersona" value="<%=idPersona%>" />
		<html:hidden property = "idTurno" value= "<%=idTurno%>"/>
		<html:hidden property = "anio" value="<%=anio%>" />	
		<html:hidden property = "numero" value="<%=numero%>" />
	
		
		
		
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table class="tablaCentralCampos" align="center" >
		<!-- INICIO: CAMPOS DEL REGISTRO -->
		<tr>			
			<td>
				<!-- SUBCONJUNTO DE DATOS -->
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.Designacion">
				<table width="100%" >
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
					</td>
					<td>
						<html:text name="ActuacionesDesignasForm" property="turno" size="60" styleClass="boxConsulta" value="<%=turno%>" readonly="true"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.busquedaEJG.literal.anyo"/>
					</td>
					<td>
						<html:text name="ActuacionesDesignasForm" property="anio" size="5" styleClass="boxConsulta" value="<%=anio%>" readonly="true"></html:text>
					</td>					
					<td class="labelText" style="display:none" >
						<siga:Idioma key="gratuita.listadoAsistencias.literal.numero"/>
					</td>					
					<td style="display:none" >
				    	<html:text name="ActuacionesDesignasForm" property="numero" size="5" styleClass="boxConsulta" value="<%=numero%>" readonly="true"></html:text>
					</td>					
					<td class="labelText">
						<siga:Idioma key="gratuita.listadoAsistencias.literal.numero"/>
					</td>
					<td>
					<html:text name="ActuacionesDesignasForm" property="codigo" size="5" styleClass="boxConsulta" value="<%=codigo%>" readonly="true"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fechaDesigna"/>
					</td>
					<td>
						<html:text name="ActuacionesDesignasForm" property="fecha" size="10" styleClass="boxConsulta" value="<%=fecha%>" readonly="true"></html:text>
					</td>
				</tr>
				</table>
				</siga:ConjCampos>
			</td>
		</tr>
		<tr>
			<td>
				<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.letrado">
				<table width="100%">
				<tr>
					<td class="labelText" width="15%">
						<siga:Idioma key="gratuita.busquedaAsistencias.literal.ncolegiado"/>
					</td>
					<td width="5%">
						<html:text name="ActuacionesDesignasForm" property="ncolegiado" size="5" styleClass="boxConsulta" value="<%=ncolegiado%>" readonly="true"></html:text>
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
			</td>
		</tr>
		<tr>
			<td>
				<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.titulo">
				<table width="100%" border="0">
				<tr>
					
					<td class="labelText">
						<siga:Idioma key="gratuita.actuacionesAsistencia.literal.fechaActuacion"/>&nbsp;(*)
					</td>
					<td>
						<% if (!modoAnterior.equalsIgnoreCase("VER")) { %>
							<html:text name="ActuacionesDesignasForm" property="fechaActuacion" size="10" styleClass="box" value="<%=fechaActuacion%>" readonly="true"></html:text>
							&nbsp;
							<a onClick="return showCalendarGeneral(fechaActuacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
						<% } else { %>
							<html:text name="ActuacionesDesignasForm" property="fechaActuacion" size="10" styleClass="boxConsulta" value="<%=fechaActuacion%>" readonly="true"></html:text>
						<% } %>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.actuacionesAsistencia.literal.nactuacion"/>
					</td>
					<td>
						<html:text name="ActuacionesDesignasForm" property="nactuacion" size="10" value="<%=nactuacion%>" styleClass="boxConsulta" readonly="true"></html:text>
					</td>
					<td class="labelText">
						<siga:Idioma key="gratuita.modalActuacionesDesigna.literal.anulacion"/> 
					</td>
					<td>
						<% if (!modoAnterior.equalsIgnoreCase("VER")) { 
								if(!fechaAnulacion.equals(""))
								{
						%>
									<INPUT NAME="anulacion" TYPE=CHECKBOX <%if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1")){%>checked<%}%> disabled>
						<% 		} else{ %>	
									<INPUT NAME="anulacion" TYPE=CHECKBOX <%if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1")){%>checked<%}%>>
						
						<%		} %>
						
						<%}else{%>
							<INPUT NAME="anulacion" TYPE=CHECKBOX <%if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1")){%>checked<%}%> disabled>
						<%}%>
					</td>
				<tr>
				
				<td class="labelText" style="vertical-align: middle;">
							<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento" /><%=(obligatorioNumeroProcedimiento?asterisco:"")%>
						</td>
						<td style="vertical-align: middle;">
						<% if (!modoAnterior.equalsIgnoreCase("VER")) { %> 
							<html:text name="ActuacionesDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="box" value="<%=numeroProcedimiento%>"></html:text> 
						<% } else { %> 
							<html:text name="ActuacionesDesignasForm" property="numeroProcedimiento" style="width:100" maxlength="<%=maxLenghtProc%>" styleClass="boxConsulta" value="<%=numeroProcedimiento%>" readonly="true"></html:text> 
						<% } %>
						</td>
				
				
              <td class="labelText">	
				 <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
				 <%if(!modoAnterior.equalsIgnoreCase("VER")){%>
				    &nbsp;/&nbsp;<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
				 <%}%>
				 &nbsp;(*)
			  </td>	 
		
			<% if (esLetrado||modoAnterior.equalsIgnoreCase("VER")){%>
					<td colspan="6" >
							<siga:ComboBD nombre="juzgado" ancho="530" tipo="<%=comboJuzgados%>"estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=datoJuzg%>"  elementoSel="<%=juzgadoSel%>" accion="Hijo:procedimiento"/>
					</td>
					<%}else{%>
					  <td colspan="5" >
							<input type="text" name="codigoExtJuzgado" class="box" size="8"  style="margin-top:0px;" maxlength="10" onChange="obtenerJuzgado();" />
							<siga:ComboBD nombre="juzgado" ancho="430" tipo="<%=comboJuzgados%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" parametro="<%=datoJuzg%>"  elementoSel="<%=juzgadoSel%>"  accion="Hijo:procedimiento; cambiarJuzgado(this);"/>
					</td>
			<%}%>
				
			 </tr>	
			 <tr>
				<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
				</td>
				<td > 
					<% if (!modoAnterior.equalsIgnoreCase("VER")) { %> 
					 	<html:text name="ActuacionesDesignasForm" property="nig"  value="<%=nig%>" styleClass="<%=estiloCombo%>" maxlength="50"/>
					<%}else{%>
						<html:text name="ActuacionesDesignasForm" property="nig"  value="<%=nig%>" styleClass="boxConsulta"/>
					<%}%>						
				</td>
			 </tr>	
				<tr>
				
					<td class="labelText">
						<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo"/>&nbsp;(*)
					</td>
					<%if (modoJustificacion!=null && modoJustificacion.equals("editarJustificacion")){ %>
					<td colspan="7">
						
							<html:text name="ActuacionesDesignasForm"  style="width:600px" property="procedimiento1" styleClass="boxConsulta" readOnly="true" value="<%=nombreProcedimiento%>"/>
					</td>
					<td  style="display:none">
                            <siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboJuzgados%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>" accion="Hijo:acreditacion" />
					</td>
					<%} else if (modoJustificacion!=null && modoJustificacion.equals("nuevoJustificacion")){%>
						<td  colspan="7">
                            	<siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboJuzgadosJustificacion%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>" accion="Hijo:acreditacion" />
						</td>
					
					<%} else if ((esLetrado||modoAnterior.equalsIgnoreCase("VER"))){%>
					<td colspan="7">						
							<html:text name="ActuacionesDesignasForm"  style="width:600px" property="procedimiento1" styleClass="boxConsulta" readOnly="true" value="<%=nombreProcedimiento%>"/>
					</td>
					<td  style="display:none">
                            <siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboModulos%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>"  />
					</td>
					
					
					<%}else { %>
					<td  colspan="7">
                            <siga:ComboBD ancho="600" nombre="procedimiento" tipo="<%=comboModulos%>" estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>" hijo="t"  elementoSel="<%=procedimientoSel%>" accion="Hijo:acreditacion" />
					</td>
					<%}%>
					
				<tr>
				
					<td class="labelText">
						<siga:Idioma key="gratuita.procedimientos.literal.acreditacion"/>&nbsp;(*)
					</td>
					<td  colspan="7">
						<%if (modoJustificacion!=null && modoJustificacion.equals("nuevoJustificacion")){%>
							<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>"  hijo="t" elementoSel="<%=acreditacionSel%>" />
						<%}else if (modoAnterior.equalsIgnoreCase("VER")) { %>
							<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="true"   parametro="<%=paramAcreditacion%>" elementoSel="<%=acreditacionSel%>" />
						
							
						<% } else { 
						  
						%>
						<%     if (esLetrado){%>
							<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>"   parametro="<%=paramAcreditacion%>" elementoSel="<%=acreditacionSel%>" />
						<%    }else{%>	
						   	<siga:ComboBD ancho="600" nombre="acreditacion" tipo="comboAcreditaciones" estilo="true" clase="<%=estiloCombo%>"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" readOnly="<%=readOnlyCombo%>"  hijo="t" elementoSel="<%=acreditacionSel%>" />
						<%    }%>
						
						<% } %>
					</td>

				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prision"/>
					</td>
					<td colspan="7">
						<siga:ComboBD  ancho="300" nombre="prision" tipo="comboPrisiones"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=param%>" elementoSel="<%=prisionSel%>" />
					 
						<font class="labelTextValor">
							&nbsp;&nbsp;&nbsp;<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.prisionCompletar"/>
						</font>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.actuacionesDesigna.literal.pretensiones"/><%=(obligatorioProcedimiento?asterisco:"")%>
					</td>
					<td colspan="7">
						<siga:ComboBD  ancho="300" nombre="pretension" tipo="comboPretensiones"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=paramPretension%>" elementoSel="<%=pretensionSel%>" />										 
					<% if (pcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA) { %>
						<font class="labelText"><siga:Idioma key="gratuita.altaGuardia.literal.motivoCambio"/></font>
						<siga:ComboBD  ancho="300" nombre="idMotivoCambio" tipo="cmbActuacionDesignaMotivoCambio"  estilo="true" clase="<%=estiloCombo%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false"  readOnly="<%=readOnlyCombo%>" parametro="<%=paramMotivoCambio%>" elementoSel="<%=motCambioSel%>" />
					<% } else {%>
						<html:hidden property = "idMotivoCambio" value="<%=idMotivoCambio%>"/>
					<% } %>
					</td>					
				</tr>
					
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.altaGuardia.literal.observaciones"/>
					</td>
					<td  colspan="7">
						<% if (!modoAnterior.equalsIgnoreCase("VER")) { %>
							<textarea class="box" scroll="none" name="observaciones" rows="2" cols="150"><%=observaciones%></textarea>
						<% } else { %>
							<textarea class="boxConsulta" scroll="none" name="observaciones" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" rows="2" cols="150" readonly="true"><%=observaciones%></textarea>
						<%}%>
						<font class="labelText">
						&nbsp;&nbsp;&nbsp;<siga:Idioma key="gratuita.actuacionesDesigna.literal.talonario"/>&nbsp;/&nbsp;
					    <siga:Idioma key="gratuita.actuacionesDesigna.literal.talon"/>					     
						</font>
						
						 <%if(!modoAnterior.equalsIgnoreCase("VER")){%>
						 <font class="labelTextValor">	
							<html:text name="ActuacionesDesignasForm" property="talonario" size="20" maxlength="20" styleClass="box" ></html:text>
							&nbsp;/&nbsp;
					     	<html:text name="ActuacionesDesignasForm" property="talon" size="20"  maxlength="20" styleClass="box"></html:text>
					     </font>
					     <%}else{%>	
					    	 <font class="labelTextValor">
					     		<html:text name="ActuacionesDesignasForm" property="talonario" size="20" styleClass="boxConsulta" readOnly="true"/>
					     		&nbsp;/&nbsp;
					     		<html:text name="ActuacionesDesignasForm" property="talon" size="20" styleClass="boxConsulta" readOnly="true"/>
					    	 </font>	
					     <%}%>	
					     
					</td>
					
				</tr>
				</table>
				</siga:ConjCampos>			
			</td>
		</tr>
		
		<tr>
			<td>
							
				<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.justificacion">
				<table width="100%">
						<tr>
							<td class="labelText">	
								<siga:Idioma key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha"/>
							</td>
								<%if(!modoAnterior.equals("VER")&&(!actuacionValidada.equals("1"))) {%>	
									<td>	
										<html:text name="ActuacionesDesignasForm" property="fechaJustificacion" size="10" maxlength="10" styleClass="box" value="<%=fechaJustificacion%>"  readOnly="true"></html:text>
										<%if(!modoAnterior.equals("VER")&&!deDonde.equals("/JGR_PestanaDesignas")) {%>
											&nbsp;&nbsp;<a name="calendarioTd" style="visibility:visible" onClick="showCalendarGeneral(fechaJustificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
										<%}else if(!modoAnterior.equals("VER")&&deDonde.equals("/JGR_PestanaDesignas")){%>
											&nbsp;&nbsp;<a name="calendarioTd" style="visibility:hidden" onClick="showCalendarGeneral(fechaJustificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>	
										<%}%>
									</td>
								<%}else{%>
									<td class="labelTextValor">	
										<html:text name="ActuacionesDesignasForm" property="fechaJustificacion" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fechaJustificacion%>" readOnly="true" ></html:text>
										<%if(!modoAnterior.equals("VER")) {%>
											&nbsp;&nbsp;<a name="calendarioTd" style="visibility:hidden" onClick="showCalendarGeneral(fechaJustificacion);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
										<%}%>
									</td>
								<%}%>								

							<td class="labelText">

								&nbsp;&nbsp;


				<% if(!modoAnterior.equals("VER")) {%>
						<% if (!usr.isLetrado()&&!deDonde.equals("/JGR_PestanaDesignas")) { // Agente %>
							<% if ((facturada != null) && (!facturada.equals("1"))) {%>
									<input type="button" id="idButton" alt="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>" id="bValidarActuacion" onclick="validarJustificacion();" class="button" value="<siga:Idioma key='gratuita.altaTurnos.literal.validacion'/>">
							<%}%>
						<%} %>
				<% }     // if modo consulta %>
	
							</td>
							<td class="labelText">
								<%if(actuacionValidada.equals("1")) {%>	
								<input name="estadoActuacion" type="text" class="boxConsulta" value='<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>' readonly="true" >
								<% }else{ %>
								<input name="estadoActuacion" type="text" class="boxConsulta" value="<siga:Idioma key='gratuita.mantActuacion.literal.actuacionPteValidar'/>" readonly="true" >
								<% } %>
							</td>

							<td class="labelText">
								<siga:Idioma key="gratuita.altaGuardia.literal.observaciones"/>
							</td>
							<td>
									<% if (!modoAnterior.equalsIgnoreCase("VER") && !usr.isLetrado() && validarJustificaciones.equalsIgnoreCase("S")) { %>
										<textarea class="box" scroll="none" name="observacionesJustificacion" rows="3" cols="100"><%=observacionesJustificacion%></textarea>
									<%}else{%>
										<textarea class="boxConsulta" scroll="none" name="observacionesJustificacion" rows="3" cols="100" readonly="true"><%=observacionesJustificacion%></textarea>
									<%}%>
							</td>
						</tr>
		<%if(anioEJG!=null && !anioEJG.equals("")){ //si la designa tiene EJG asociad... %>
			
			<tr>
				<td class="labelText" colspan="2">
					<siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>:
				</td>	
			<%if (vTipoRatificacion!=null && vTipoRatificacion.size()>0){%>	
				<td>
					<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucion" ancho="200" clase="boxConsulta"  filasMostrar="1" seleccionMultiple="false" obligatorio="false"  elementoSel="<%=vTipoRatificacion%>" readOnly="true"/>
				
				</td>
			 <%}%>	
			</tr>
			
	
	<tr>
		<td class="labelText" colspan="2">
			<siga:Idioma key="gratuita.operarRatificacion.literal.fechaNotificacion"/>:
		</td>
	 <%if (fechaNotificacion!=null && !fechaNotificacion.equals("")){%>			
		<td>
			<html:text name="DefinirEJGForm" property="fechaNotificacion" size="10" styleClass="boxConsulta" value="<%=fechaNotificacion%>" disabled="false" readonly="true"></html:text>
		</td>
	 <%}%>	
	</tr>
	
	
	<tr>
	<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.operarRatificacion.literal.fechaRatificacion"/>:
	</td>	
	<%if (fechaRatificacion!=null && !fechaRatificacion.equals("")){%>
	<td>
		
			<html:text name="DefinirEJGForm" property="fechaRatificacion" size="10" styleClass="boxConsulta" value="<%=fechaRatificacion%>" disabled="false" readonly="true"></html:text>
		
	</td>
	<%}%>
	</tr>
	
<%}%>
					</table>
				</siga:ConjCampos>
				</table>
			</td>
		</tr>
		<%if (nombreFacturacion!=null && !nombreFacturacion.equals("")){%>
		<tr>
			<td>
				<siga:ConjCampos leyenda="gratuita.actuacionesDesigna.literal.facturacion">
					<table width="100%">
						<tr>
						<td width="15%">&nbsp; </td>
						<td width="85%">&nbsp;
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
			</td>
		</tr>
		<%} %>
	
		</table>

</html:form>
	
    <html:form action = "/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<input type="hidden" name="modo"        value="buscarJuzgado">
		<html:hidden property = "codigoExt2" value=""/>
	</html:form>	

<% if (modoAnterior.equalsIgnoreCase("VER")) { %>
		<siga:ConjBotonesAccion botones="C" modal="G"/>
<% } else { %>
		<siga:ConjBotonesAccion botones="Y,C" modal="G"/>
<% } %>
	

	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Asociada al boton Cerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
			if (validateActuacionesDesignasForm(document.ActuacionesDesignasForm)) {
			
				// document.forms[0].actuacionValidada.value = document.forms[0].checkActuacionValidacion.checked;
				fecha = document.getElementById("fechaJustificacion");
				/* if(!((fecha.value == null)||(fecha.value == ""))){
					document.forms[0].actuacionValidada.value="1";
					document.forms[0].estadoActuacion.value='<siga:Idioma key='gratuita.mantActuacion.literal.actuacionValidada'/>';
				}
				*/

				if (<%=obligatorioNumeroProcedimiento%> && document.forms[0].numeroProcedimiento.value=='') {
					alert('<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento"/> <siga:Idioma key="messages.campoObligatorio.error" />');
					fin();
					return false;
				}		
				if(<%=validaNumeroProcedimiento%> && !validaProcedimiento(document.forms[0].numeroProcedimiento.value)) {
					alert('<siga:Idioma key='gratuita.procedimientos.numero.formato'/>');
					fin();
					return false;
				}
					
				if (document.forms[0].juzgado.value=='') {
					alert('<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/> <siga:Idioma key="messages.campoObligatorio.error" />');
					fin();
					return false;	
				}
				if (document.forms[0].procedimiento.value=='') {
					alert('<siga:Idioma key="gratuita.actuacionesDesigna.literal.modulo"/> <siga:Idioma key="messages.campoObligatorio.error" />');
					fin();
					return false;	
				}
				if (document.forms[0].acreditacion.value=='') {
					alert('<siga:Idioma key="gratuita.procedimientos.literal.acreditacion"/> <siga:Idioma key="messages.campoObligatorio.error" />');
					fin();
					return false;
				}			

				if (<%=obligatorioProcedimiento%> && document.forms[0].pretension.value=='') {
					alert('<siga:Idioma key="gratuita.actuacionesDesigna.literal.pretensiones"/> <siga:Idioma key="messages.campoObligatorio.error" />');
					fin();
					return false;
				}
				
				<%if (pcajgActivo == CajgConfiguracion.TIPO_CAJG_TXT_ALCALA) {%>					
					if (document.forms[0].idMotivoCambio.value=='') {
																		
						if ('<%=(idJuzgadoDesigna + "," + juzgadoInstitucionDesigna)%>' != document.forms[0].juzgado.value) {							
							alert('<siga:Idioma key="messages.gratuita.actuacionesDesigna.distintoJuzgado"/>');
							fin();
							return false;
						}
						if ('<%=(idPretensionDesigna!=null?idPretensionDesigna:"")%>' != document.forms[0].pretension.value) {							
							alert('<siga:Idioma key="messages.gratuita.actuacionesDesigna.distintoProcedimiento"/>');
							fin();
							return false;
						}
					}
				<%}%>
									
				<% if (modoAnterior.equalsIgnoreCase("EDITAR")) { %>
				document.forms[0].modo.value="modificar";
				<% } else { %>
				document.forms[0].modo.value="insertar";
				<% } %>
				document.forms[0].submit();

			}else{
			
				fin();
				return false;
			
			}	
		}		

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		
		
		function limpiarValor(combo){
			  seleccionComboSiga (combo, "");
		}

		function validarJustificacion () {
				if(document.forms[0].actuacionValidada.value=="1"){
					document.forms[0].fechaJustificacion.className="box";
					document.forms[0].actuacionValidada.value="0";
					document.getElementById("calendarioTd").style.visibility="visible";
					document.forms[0].fechaJustificacion.value="";
					document.forms[0].estadoActuacion.value= "";
				}else{
					document.forms[0].fechaJustificacion.className="boxConsulta";					
					document.forms[0].actuacionValidada.value="1";
					document.getElementById("calendarioTd").style.visibility="hidden";
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
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>