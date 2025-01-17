<!DOCTYPE html>
<html>
<head>
<!-- caractAsistencias.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>



<!-- IMPORTS -->
<%@ page import="com.siga.gratuita.form.CaracteristicasForm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>


<!-- JSP -->
<%
HttpSession ses=request.getSession(true);
UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");

String app = request.getContextPath();

ArrayList conectadoLetradoSel    = new ArrayList();

//Tipo PCAJG
Integer PCAJG_ACTIVADO =(Integer) (request.getAttribute("PCAJG_ACTIVO"));

String maxLenghtProc = "20";

if(PCAJG_ACTIVADO == 2){
	maxLenghtProc = "15";
}

CaracteristicasForm miform = (CaracteristicasForm) request.getAttribute("CaracteristicasForm");
//Variables
String		idInstitucion ="";
String		anio="";
String		numero="";
String		idInstitucionJuzgado="";		
String		idOrigenContacto="";
String		otroDescripcionOrigenContacto="";
String		violenciaGenero="";
String		violenciaDomestica="";
String		contraLibertadSexual="";
String		victimaMenorAbusoMaltrato=""; 
String		personaConDiscapacidad=""; 

String		violenciaIntrafamiliar="";
String		violenciaContraMujer="";
String		contraLaLibertadSexual="";
String		temaSinDefinir="";

String		judicial="";
String		civil="";
String		penal="";
String		interposicionDenuncia="";
String		solicitudMedidasCautelares="";
String		asistenciaDeclaracion="";
String		medidasProvisionales="";
String		ordenProteccion="";
String		otras="";
String		otrasDescripcion="";
String		asesoramiento="";	
String		derivaActuacionesJudiciales="";
String		interposicionMinistFiscal="";
String		intervencionMedicoForense="";
String		derechosJusticiaGratuita="";
String		obligadaDesalojoDomicilio="";
String		entrevistaLetradoDemandante="";
String		letradoDesignadoContiActuJudi="";
String		civilesPenales="";
String		victimaLetradoAnterioridad="";
String		idPersona="";
String		numeroProcedimiento="";
String		idJuzgado="";
String		nig="";
String		idPretension="";
 
String descripcionContacto="";
String descripcionJuzgado="";
String descripcionPretension="";
 

//Se cargan los campos con los datos que viene el el formulario miform
if(miform!=null)
{
	
	 descripcionContacto = miform.getDescripcionContacto();
	 descripcionJuzgado= miform.getDescripcionJuzgado();
	 descripcionPretension = miform.getDescripcionPretension();
	
	 idInstitucion = miform.getIdInstitucion();
	 anio =miform.getAnio();
	 numero = miform.getNumero();
	 idInstitucionJuzgado = miform.getIdInstitucionJuzgado();		
	 idOrigenContacto = miform.getIdOrigenContacto();

	 if (idOrigenContacto!=null && !(idOrigenContacto.equals("")))
		 conectadoLetradoSel.add(0,idOrigenContacto);
	 
	 otroDescripcionOrigenContacto = miform.getOtroDescripcionOrigenContacto();
	 violenciaDomestica = miform.getViolenciaDomestica();
	 violenciaGenero = miform.getViolenciaGenero();
	 contraLibertadSexual = miform.getContraLibertadSexual();
	 victimaMenorAbusoMaltrato = miform.getVictimaMenorAbusoMaltrato();
	 personaConDiscapacidad = miform.getPersonaConDiscapacidad();
	 judicial = miform.getJudicial();
	 civil = miform.getCivil();
	 penal = miform.getPenal();
	 interposicionDenuncia = miform.getInterposicionDenuncia();
	 solicitudMedidasCautelares = miform.getSolicitudMedidasCautelares();
	 asistenciaDeclaracion = miform.getAsistenciaDeclaracion();
	 medidasProvisionales = miform.getMedidasProvisionales();
	 ordenProteccion = miform.getOrdenProteccion();
	 otras = miform.getOtras();
	 otrasDescripcion = miform.getOtrasDescripcion();
	 asesoramiento = miform.getAsesoramiento();	
	 derivaActuacionesJudiciales = miform.getDerivaActuacionesJudiciales();
	 interposicionMinistFiscal = miform.getInterposicionMinistFiscal();
	 intervencionMedicoForense = miform.getIntervencionMedicoForense();
	 derechosJusticiaGratuita = miform.getDerechosJusticiaGratuita();
	 obligadaDesalojoDomicilio = miform.getObligadaDesalojoDomicilio();
	 entrevistaLetradoDemandante = miform.getEntrevistaLetradoDemandante();
	 letradoDesignadoContiActuJudi = miform.getLetradoDesignadoContiActuJudi();
	 civilesPenales = miform.getCivilesPenales();
	 victimaLetradoAnterioridad = miform.getVictimaLetradoAnterioridad();
	 idPersona = miform.getIdPersona();
	 numeroProcedimiento = miform.getNumeroProcedimiento();
	 idJuzgado = miform.getIdJuzgado();
	 nig = miform.getNig();
	 idPretension = miform.getIdPretension();	
	 temaSinDefinir=miform.getTemaSinDefinir();
	 violenciaContraMujer=miform.getViolenciaContraMujer();
	 contraLaLibertadSexual=miform.getContraLaLibertadSexual();
	 violenciaIntrafamiliar=miform.getViolenciaIntrafamiliar();
}

boolean scheck = false;

String[] datosOrigen = {usr.getLocation(),idInstitucion};

String modo = request.getParameter("MODO")!=null?request.getParameter("MODO"):"";

//Combo procedimientos (pretensiones)
ArrayList juzgadoSel = new ArrayList();
String[] datosPretension={usr.getLocation(),usr.getLanguage(),"-1"};
ArrayList pretensionesSel = new ArrayList();
if(idPretension!=null && !idPretension.equals(""))
{
	datosPretension[1]= idPretension;	
	pretensionesSel.add(0,idPretension);
}

String estilo = "box", comboSize="400";
boolean readonly;
String sreadonly;
if(modo.equals("ver"))
{
	estilo = "boxConsulta";
	readonly = true;
	comboSize="550";
	sreadonly ="true";
}
else
{	
	readonly = false;
	sreadonly ="false";
	//estilo = "box";
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<html:javascript formName="CaracteristicasForm" staticJavascript="false" />  

<!-- INICIO: TITULO Y LOCALIZACION PARA LA MIGA-->
	<siga:Titulo localizacion="gratuita.contAnulDeliJuriAsistencia.literal.localizacion" titulo="gratuita.caracteristicas.literal.tituloCaract"/>
<!-- FIN: TITULO Y LOCALIZACION -->

<script language="JavaScript">

function cambiarJuzgado() {
	if(document.getElementById("idJuzgado")){
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

function preAccionColegiado()
{	

}

function postAccionColegiado()
{
			
}

//función que muestra el mensaje de Datos Modificados correctamente una vez vuelve del action 
function refrescarLocal() 
{
	parent.buscar();
}

function limpiarColegiado()
{
	document.CaracteristicasForm.idPersona.value = '';
	document.CaracteristicasForm.colegiadoNumero.value = '';
	document.CaracteristicasForm.colegiadoNombre.value = '';
}

function buscarColegiado()
{	
	var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
	if (resultado!=undefined && resultado[0]!=undefined ){
		
		document.CaracteristicasForm.idPersona.value       = resultado[0];
		document.CaracteristicasForm.colegiadoNumero.value    = resultado[2];
		document.CaracteristicasForm.colegiadoNombre.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
	}
}

function fViolenciaGenero(o) 
{
	if (o.checked)
	{		
		document.CaracteristicasForm.violenciaGenero.value = "1";
	}	 
	else
	{			 	
		document.CaracteristicasForm.violenciaGenero.value = "0";
	}	
}

function fViolenciaDomestica(o) 
{
	if (o.checked)
	{		
		document.CaracteristicasForm.violenciaDomestica.value = "1";
	}	 
	else
	{			 	
		document.CaracteristicasForm.violenciaDomestica.value = "0";
	}	
}

function fContraLibertadSexual(o) 
{
	if (o.checked)
	{ 			
		document.forms[0].contraLibertadSexual.value = "1";
	}	 
	else
	{		 	
		document.forms[0].contraLibertadSexual.value = "0";
	}		
}

function fVictimaMenorAbusoMaltrato(o) 
{
	if (o.checked) { 			
		document.forms[0].victimaMenorAbusoMaltrato.value = "1";
	} else {		 	
		document.forms[0].victimaMenorAbusoMaltrato.value = "0";
	}		
}

function fPersonaConDiscapacidad(o) 
{
	if (o.checked) { 			
		document.forms[0].personaConDiscapacidad.value = "1";
	} else {		 	
		document.forms[0].personaConDiscapacidad.value = "0";
	}		
}

function fViolenciaIntrafamiliar(o){
	if (o.checked) { 			
		document.forms[0].violenciaIntrafamiliar.value = "1";
	} else {		 	
		document.forms[0].violenciaIntrafamiliar.value = "0";
	}		
}

function fViolenciaContraMujer(o){
	if (o.checked) { 			
		document.forms[0].violenciaContraMujer.value = "1";
	} else {		 	
		document.forms[0].violenciaContraMujer.value = "0";
	}		
}

function fTemaSinDefinir(o){
	if (o.checked) { 			
		document.forms[0].temaSinDefinir.value = "1";
	} else {		 	
		document.forms[0].temaSinDefinir.value = "0";
	}		
}

function fContraLaLibertadSexual(o){
	if (o.checked) { 			
		document.forms[0].contraLaLibertadSexual.value = "1";
	} else {		 	
		document.forms[0].contraLaLibertadSexual.value = "0";
	}		
}

function fJudicial(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.judicial.value = "1";	 
	else 	
		document.CaracteristicasForm.judicial.value = "0";		
}

function fAsesoramiento(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.asesoramiento.value = "1";	 
	else 	
		document.CaracteristicasForm.asesoramiento.value = "0";	
}

function fCivil(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.civil.value = "1";	 
	else 	
		document.CaracteristicasForm.civil.value = "0";		
}

function fPenal(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.penal.value = "1";	 
	else 	
		document.CaracteristicasForm.penal.value = "0";		
}

function fInterposicionDenuncia(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.interposicionDenuncia.value = "1";	 
	else 	
		document.CaracteristicasForm.interposicionDenuncia.value = "0";				
}

function fSolicitudMedidasCautelares(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.solicitudMedidasCautelares.value = "1";	 
	else 	
		document.CaracteristicasForm.solicitudMedidasCautelares.value = "0";			
}

function fAsistenciaDeclaracion(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.asistenciaDeclaracion.value = "1";	 
	else 	
		document.CaracteristicasForm.asistenciaDeclaracion.value = "0";		
}

function fMedidasProvisionales(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.medidasProvisionales.value = "1";	 
	else 	
		document.CaracteristicasForm.medidasProvisionales.value = "0";		
}

function fOrdenProteccion(o) 
{
	if (o.checked) 	
		document.CaracteristicasForm.ordenProteccion.value = "1";	 
	else 	
		document.CaracteristicasForm.ordenProteccion.value = "0";	
}

function fOtras(o) 
{
	if (o.checked) 
	{	
		document.CaracteristicasForm.otras.value = "1";		
		jQuery("#otrasDescripcion").removeAttr("disabled");
	}
	else 	
	{	
		document.CaracteristicasForm.otras.value = "0";
	   	jQuery("#otrasDescripcion").attr("disabled","disabled");
	}			
}

function fDerivaActuacionesJudiciales(o)
{
	document.CaracteristicasForm.derivaActuacionesJudiciales.value = o.value;	
}

function fInterposicionMinistFiscal(o)
{
	document.CaracteristicasForm.interposicionMinistFiscal.value = o.value;	
}

function fIntervencionMedicoForense(o)
{
	document.CaracteristicasForm.intervencionMedicoForense.value = o.value;	
}

function fDerechosJusticiaGratuita(o)
{
	document.CaracteristicasForm.derechosJusticiaGratuita.value = o.value;	
}

function fObligadaDesalojoDomicilio(o)
{
	document.CaracteristicasForm.obligadaDesalojoDomicilio.value = o.value;	
}

function fEntrevistaLetradoDemandante(o)
{
	document.CaracteristicasForm.entrevistaLetradoDemandante.value = o.value;	
}

function fLetradoDesignadoContiActuJudi(o)
{
	document.CaracteristicasForm.letradoDesignadoContiActuJudi.value = o.value;	
}

function fVictimaLetradoAnterioridad(o)
{
	document.CaracteristicasForm.victimaLetradoAnterioridad.value = o.value;	
}


// FUNCIONES DE BOTONERA

//Asociada al boton Restablecer -->
function accionRestablecer() 
{		
	document.forms[0].reset();
	// inc7269 // Refrescamos la pagina para que cargue tambien los telefonos
	window.location.reload();
}

//Asociada al boton Volver -->
function accionVolver()   
{		
	if(document.forms[0].jsonVolver && document.forms[0].jsonVolver.value!=''){
		jSonVolverValue = document.forms[0].jsonVolver.value;
		jSonVolverValue = replaceAll(jSonVolverValue,"'", "\"");
		var jSonVolverObject =  jQuery.parseJSON(jSonVolverValue);
		nombreFormulario = jSonVolverObject.nombreformulario; 
		if(nombreFormulario == 'SolicitudAceptadaCentralitaForm'){
			document.forms['SolicitudAceptadaCentralitaForm'].idSolicitudAceptada.value =  jSonVolverObject.idsolicitudaceptada;
			document.forms['SolicitudAceptadaCentralitaForm'].idInstitucion.value = jSonVolverObject.idinstitucion;
			document.forms['SolicitudAceptadaCentralitaForm'].modo.value="consultarSolicitudAceptada";
			document.forms['SolicitudAceptadaCentralitaForm'].target = "mainWorkArea";
			document.forms['SolicitudAceptadaCentralitaForm'].submit();
		}
	}else{
		document.forms[0].action="/SIGA/JGR_Asistencia.do";	
		document.forms[0].modo.value="abrir";
		document.forms[0].target="mainWorkArea"; 
		document.forms[0].submit();
	}
}

//Asociada al boton Guardar -->
function accionGuardar()
{		
	var indice = document.CaracteristicasForm.idOrigenContacto.selectedIndex;			

	if(indice!="" && indice!=0)
	{
		document.CaracteristicasForm.descripcionContactoHidden.value=document.CaracteristicasForm.idOrigenContacto.options[indice].text;		
	}

	indice = document.CaracteristicasForm.idJuzgado.selectedIndex;
	if(document.CaracteristicasForm.idJuzgado.selectedIndex!="" && document.CaracteristicasForm.idJuzgado.selectedIndex!="-1")
		document.CaracteristicasForm.descripcionJuzgadoHidden.value=document.CaracteristicasForm.idJuzgado.options[indice].text;
	
	indice = document.CaracteristicasForm.idPretension.selectedIndex;
	if(document.CaracteristicasForm.idPretension.selectedIndex!="" && document.CaracteristicasForm.idJuzgado.selectedIndex!="-1")
		document.CaracteristicasForm.descripcionPretensionHidden.value=document.CaracteristicasForm.idPretension.options[indice].text;
	
	var nigAux = document.getElementById("nig").value;
	nigAux = ready2ApplyMask(nigAux);
	nigAux = nigAux.toUpperCase();
	var idConsejo = '';
	if(document.getElementById("idConsejo"))
		idConsejo = document.getElementById("idConsejo").value;
	if(!validarNig(nigAux,idConsejo)){	
		formato = '<siga:Idioma key="gratuita.nig.formato.general"/>';
		if(idConsejo==IDINSTITUCION_CONSEJO_ANDALUZ){
			formato = '<siga:Idioma key="gratuita.nig.formato.cadeca"/>';
		}
		fin();
		alert("<siga:Idioma key='gratuita.nig.formato' arg0='"+formato+"' />");
		return false;
			
 	}
	document.forms[0].nig.value = nigAux; 
	
	document.forms[0].modo.value = "guardar";
	document.forms[0].target = "submitArea";
	document.forms[0].submit();
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

//se ejecuta cuando se carga la pagina
function inicializar() 
{		
	cambiarJuzgado();
	document.CaracteristicasForm.colegiadoNumero.value="";
	<%if(idOrigenContacto.equals("9")){%>
		document.CaracteristicasForm.otroDescripcionOrigenContacto.readOnly = false;
	<%}else{%>
		document.CaracteristicasForm.otroDescripcionOrigenContacto.readOnly = true;
	<%}%>	 
}

function actualizarTdNumeroProcedimiento()
{
	if (document.getElementById("tipoPcajg").value=="2")
	{
		var idJuzgado = document.getElementsByName('juzgado')[0];
		if(idJuzgado.value!="")
			document.getElementById("tdNumeroProcedimiento").innerHTML = '<siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/>(*)';
		else
			document.getElementById("tdNumeroProcedimiento").innerHTML = '<siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/>';
	}	
}

function bloquearDesbloquear(o)
{
	var indice = o.selectedIndex;
	if(indice=="9")
	{	
		document.CaracteristicasForm.otroDescripcionOrigenContacto.readOnly = false;
		jQuery("#otroDescripcionOrigenContacto").removeAttr("disabled");
	}
	else
	{	
		jQuery("#otroDescripcionOrigenContacto").attr("disabled","disabled");		
		document.CaracteristicasForm.otroDescripcionOrigenContacto.value = "";
	}	
}

</script>
	
<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body class="detallePestanas" onload="inicializar()">

<!--   Tabla para mostrar la barra superior de la tabla-->

 <table class="tablaTitulo" align="center" cellspacing=0 >
		<tr>
			<td class="titulitosDatos">
			
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
					ScsAsistenciasAdm adm = new ScsAsistenciasAdm (usr);
					Hashtable hTitulo = adm.getTituloPantallaAsistencia(usr.getLocation(), anio, numero);
					if (hTitulo != null) {
						t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
						t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
						t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
						t_anio      = (String)hTitulo.get(ScsAsistenciasBean.C_ANIO);
						t_numero    = (String)hTitulo.get(ScsAsistenciasBean.C_NUMERO);
					}
				%>
				<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%> 
				- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="userBean" name="USRBEAN"  scope="session" />
<input type="hidden" id ="idConsejo" value = "${userBean.idConsejo}"/>

<table align="center"  border="0" width="100%" class="tablaCampos" >
<html:form action="/JGR_CaracteristicasAsistenciaLetrado" method="POST" target="mainWorkArea">
<html:hidden styleId="jsonVolver" property = "jsonVolver"  />
<!-- VARIABLES HIDDEN -->
<input type="hidden" name="violenciaDomestica" value="<%=violenciaDomestica%>">
<input type="hidden" name="personaConDiscapacidad" value="<%=personaConDiscapacidad%>">
<input type="hidden" name="violenciaGenero" value="<%=violenciaGenero%>">

<input type="hidden" name="temaSinDefinir" value="<%=temaSinDefinir%>">
<input type="hidden" name="violenciaContraMujer" value="<%=violenciaContraMujer%>">
<input type="hidden" name="contraLaLibertadSexual" value="<%=contraLaLibertadSexual%>">
<input type="hidden" name="violenciaIntrafamiliar" value="<%=violenciaIntrafamiliar%>">

<input type="hidden" name="contraLibertadSexual" value="<%=contraLibertadSexual%>">
<input type="hidden" name="victimaMenorAbusoMaltrato" value="<%=victimaMenorAbusoMaltrato%>">
<input type="hidden" name="judicial" value="<%=judicial%>">
<input type="hidden" name="asesoramiento" value="<%=asesoramiento%>">
<input type="hidden" name="civil" value="<%=civil%>">
<input type="hidden" name="penal" value="<%=penal%>">
<input type="hidden" name="interposicionDenuncia" value="<%=interposicionDenuncia%>">
<input type="hidden" name="solicitudMedidasCautelares" value="<%=solicitudMedidasCautelares%>">
<input type="hidden" name="asistenciaDeclaracion" value="<%=asistenciaDeclaracion%>">
<input type="hidden" name="medidasProvisionales" value="<%=medidasProvisionales%>">
<input type="hidden" name="ordenProteccion" value="<%=ordenProteccion%>">
<input type="hidden" name="otras" value="<%=otras%>">

  
<input type="hidden" name="derivaActuacionesJudiciales" value="<%=derivaActuacionesJudiciales%>">
<input type="hidden" name="interposicionMinistFiscal" value="<%=interposicionMinistFiscal%>">
<input type="hidden" name="intervencionMedicoForense" value="<%=intervencionMedicoForense%>">
<input type="hidden" name="derechosJusticiaGratuita" value="<%=derechosJusticiaGratuita%>">
<input type="hidden" name="obligadaDesalojoDomicilio" value="<%=obligadaDesalojoDomicilio%>">
<input type="hidden" name="entrevistaLetradoDemandante" value="<%=entrevistaLetradoDemandante%>">
<input type="hidden" name="letradoDesignadoContiActuJudi" value="<%=letradoDesignadoContiActuJudi%>">
<input type="hidden" name="victimaLetradoAnterioridad" value="<%=victimaLetradoAnterioridad%>">

<input type="hidden" name="descripcionContactoHidden" value="<%=descripcionContacto%>">
<input type="hidden" name="descripcionJuzgadoHidden" value="<%=descripcionJuzgado%>">
<input type="hidden" name="descripcionPretensionHidden" value="<%=descripcionPretension%>">

<html:hidden name="CaracteristicasForm" property="colegiado" value=""/>
<html:hidden property="idPersona"/>
<input type="hidden" id="tipoPcajg" value="<%=PCAJG_ACTIVADO%>">
<html:hidden property = "modo" value = ""/>	

	<tr>
		<td colspan ="4" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.origenContacto"/>&nbsp;
			<%if(modo.equals("ver")){%>
					<%=descripcionContacto%>
				<%}else{%>
			<siga:ComboBD  nombre = "idOrigenContacto" tipo="cmbOrigenContacto" clase="boxCombo" obligatorio="false" accion="bloquearDesbloquear(this);" elementoSel="<%=conectadoLetradoSel%>"  parametro="<%=datosOrigen%>"/>
			<%}%>			
			<html:text name="CaracteristicasForm" styleId="otroDescripcionOrigenContacto" property="otroDescripcionOrigenContacto" size="50" maxlength="100" styleClass="box" value="<%=otroDescripcionOrigenContacto%>" readOnly="<%=readonly%>"></html:text>&nbsp;&nbsp;			
		</td>		
	</tr>

	<tr>
		<td colspan ="1" class="labelText"><siga:Idioma key="gratuita.caracteristicas.literal.temasActuaciones"/>:&nbsp;</td>
		<td colspan ="3" class="labelText">
			
			<label><input type="checkbox" name="cContraLaLibertadSexual" onclick="fContraLaLibertadSexual(this);" <%=(contraLaLibertadSexual.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.contraLibertadSexual"/>&nbsp;</label>
			
			<label><input type="checkbox" name="cViolenciaIntrafamiliar" onclick="fViolenciaIntrafamiliar(this);" <%=(violenciaIntrafamiliar.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.personaJG.literal.violenciaIntrafamiliar"/>&nbsp;</label>
			
			<label><input type="checkbox" name="cViolenciaContraMujer" onclick="fViolenciaContraMujer(this);" <%=(violenciaContraMujer.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.personaJG.literal.violenciaContraMujer"/>&nbsp;</label>
						
			<label><input type="checkbox" name="cTemaSinDefinir" onclick="fTemaSinDefinir(this);" <%=(temaSinDefinir.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.personaJG.literal.temaSinDefinir"/>&nbsp;</label>
		</td>
	</tr>
	
	<tr style="display:none">	
		<td>&nbsp;</td>
		<td colspan ="3" class="labelText">
			
			<input type="checkbox" name="cContraLibertadSexual" onclick="fContraLibertadSexual(this);" <%=(contraLibertadSexual.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.contraLibertadSexual"/>&nbsp;
			
			<input type="checkbox" name="cViolenciaDomestica" onclick="fViolenciaDomestica(this);" <%=(violenciaDomestica.equals("1"))?"checked":""%> disabled>															
			<siga:Idioma key="gratuita.caracteristicas.literal.violenciadomestica"/>&nbsp;
			
			<input type="checkbox" name="cViolenciaGenero" onclick="fViolenciaGenero(this);" <%=(violenciaGenero.equals("1"))?"checked":""%> disabled>															
			<siga:Idioma key="gratuita.caracteristicas.literal.violenciagenero"/>&nbsp;			

			<input type="checkbox" name="cVictimaMenorAbusoMaltrato" onclick="fVictimaMenorAbusoMaltrato(this);" <%=(victimaMenorAbusoMaltrato.equals("1"))?"checked":""%> disabled>
			<siga:Idioma key="gratuita.caracteristicas.literal.victimaMenorAbusoMaltrato"/>&nbsp;
						 	
			<input type="checkbox" name="cPersonaConDiscapacidad" onclick="fPersonaConDiscapacidad(this);" <%=(personaConDiscapacidad.equals("1"))?"checked":""%> disabled>
			<siga:Idioma key="gratuita.personaJG.literal.personaConDiscapacidad"/>&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="4" class="labelText">
			<input type="checkbox" name="cJudicial" onclick="fJudicial(this);" <%=(judicial.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.judicial"/>&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
		<td class="labelText">
			<input type="checkbox" name="cPenal" onclick="fPenal(this);" <%=(penal.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.penal"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
		<td class="labelText">
			<input type="checkbox" name="cCivil" onclick="fCivil(this);" <%=(civil.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.civil"/>&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2">
			&nbsp;
		</td>
		<td colspan ="1" class="labelText">
			<input type="checkbox" name="cInterposicionDenuncia" onclick="fInterposicionDenuncia(this);" <%=(interposicionDenuncia.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.interposicionDenuncia"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2">
			&nbsp;
		</td>
		<td colspan ="1" class="labelText">
			<input type="checkbox" name="cSolicitudMedidasCautelares" onclick="fSolicitudMedidasCautelares(this);" <%=(solicitudMedidasCautelares.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.solicitudMedidasCautelares"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2">
			&nbsp;
		</td>
		<td colspan ="1" class="labelText">
			<input type="checkbox" name="cAsistenciaDeclaracion" onclick="fAsistenciaDeclaracion(this);" <%=(asistenciaDeclaracion.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.asistenciaDeclaracion"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2">
			&nbsp;
		</td>
		<td colspan ="1" class="labelText">
			<input type="checkbox" name="cMedidasProvisionales" onclick="fMedidasProvisionales(this);" <%=(medidasProvisionales.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.mediasProvisionalisimas"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2">
			&nbsp;
		</td>
		<td colspan ="1" class="labelText">
			<input type="checkbox" name="cOrdenProteccion" onclick="fOrdenProteccion(this);" <%=(ordenProteccion.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.ordenProteccion"/>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="2" class="labelText">
			&nbsp;
		</td>
		<td colspan ="2" class="labelText">
			<input type="checkbox" name="cOtras" onclick="fOtras(this);" <%=(otras.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.otrasEspecificar"/>&nbsp;				
			<html:text name="CaracteristicasForm" styleId="otrasDescripcion" property="otrasDescripcion" size="50" maxlength="100"  value="<%=otrasDescripcion%>" styleClass="box"  readOnly="<%=readonly%>"></html:text>&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="4" class="labelText">
			<input type="checkbox" name="cAsesoramiento" onclick="fAsesoramiento(this);" <%=(asesoramiento.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.asesoramiento"/>&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">			
			<siga:Idioma key="gratuita.caracteristicas.literal.derivarActuacionesJudiciales"/>&nbsp;
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rDerivaActuacionesJudiciales" type=radio value="1"  onclick="fDerivaActuacionesJudiciales(this);"   <%=(derivaActuacionesJudiciales.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rDerivaActuacionesJudiciales" type=radio value="0"  onclick="fDerivaActuacionesJudiciales(this);"  <%=(derivaActuacionesJudiciales.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>> 			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.intervencionMinisterioFiscal"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rInterposicionMinistFiscal" type=radio value="1" onclick="fInterposicionMinistFiscal(this);"  <%=(interposicionMinistFiscal.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rInterposicionMinistFiscal" type=radio value="0" onclick="fInterposicionMinistFiscal(this);"   <%=(interposicionMinistFiscal.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.estabaPresente"/>
			<input name="rInterposicionMinistFiscal" type=radio value="2" onclick="fInterposicionMinistFiscal(this);"  <%=(interposicionMinistFiscal.equals("2"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.intervencionMedicoForense"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rIntervencionMedicoForense" type=radio value="1"  onclick="fIntervencionMedicoForense(this);" <%=(intervencionMedicoForense.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rIntervencionMedicoForense" type=radio value="0"  onclick="fIntervencionMedicoForense(this);" <%=(intervencionMedicoForense.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.informadoJusticiaGratuita"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rDerechosJusticiaGratuita" type=radio value="1" onclick="fDerechosJusticiaGratuita(this);"  <%=(derechosJusticiaGratuita.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rDerechosJusticiaGratuita" type=radio value="0" onclick="fDerechosJusticiaGratuita(this);"  <%=(derechosJusticiaGratuita.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.victimaDesalojarDomiciloHabitual"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rObligadaDesalojoDomicilio" type=radio value="1"  onclick="fObligadaDesalojoDomicilio(this);" <%=(obligadaDesalojoDomicilio.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rObligadaDesalojoDomicilio" type=radio value="0"  onclick="fObligadaDesalojoDomicilio(this);" <%=(obligadaDesalojoDomicilio.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.entrevistaLetradoDemandante"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rEntrevistaLetradoDemandante" type=radio value="1" onclick="fEntrevistaLetradoDemandante(this);" <%=(entrevistaLetradoDemandante.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rEntrevistaLetradoDemandante" type=radio value="0" onclick="fEntrevistaLetradoDemandante(this);"  <%=(entrevistaLetradoDemandante.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.letradoDesignadoActuJudiciales"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.siCiviles"/>
			<input name="rLetradoDesignadoContiActuJudi" type=radio value="1" onclick="fLetradoDesignadoContiActuJudi(this);"  <%=(letradoDesignadoContiActuJudi.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.siPenales"/>
			<input name="rLetradoDesignadoContiActuJudi" type=radio value="2" onclick="fLetradoDesignadoContiActuJudi(this);"  <%=(letradoDesignadoContiActuJudi.equals("2"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.ambos"/>
			<input name="rLetradoDesignadoContiActuJudi" type=radio value="3" onclick="fLetradoDesignadoContiActuJudi(this);"  <%=(letradoDesignadoContiActuJudi.equals("3"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rLetradoDesignadoContiActuJudi" type=radio value="0" onclick="fLetradoDesignadoContiActuJudi(this);"  <%=(letradoDesignadoContiActuJudi.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>											
		</td>
	</tr>
	<tr>
		<td colspan ="3" class="labelText">
			<siga:Idioma key="gratuita.caracteristicas.literal.victimaAsignadaLetrado"/>
		</td>
		<td colspan ="1" class="labelText">			 
			<siga:Idioma key="gratuita.caracteristicas.literal.si"/>
			<input name="rVictimaLetradoAnterioridad" type=radio value="1"  onclick="fVictimaLetradoAnterioridad(this);" <%=(victimaLetradoAnterioridad.equals("1"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>
			<siga:Idioma key="gratuita.caracteristicas.literal.no"/>
			<input name="rVictimaLetradoAnterioridad" type=radio value="0"  onclick="fVictimaLetradoAnterioridad(this);" <%=(victimaLetradoAnterioridad.equals("0"))?"checked":""%> <%=(readonly==true)?"disabled":""%>>  			
		</td>
	</tr>	
	
	<tr>
		<td colspan="6">
			<siga:ConjCampos leyenda="censo.bajastemporales.colegiado.leyenda">
			<table width="100%">
				<tr>
					<td>
						<table align="left">
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.bajastemporales.colegiado" /> 
								</td>
								<td>
									<html:text styleId="colegiadoNumero" property="colegiadoNumero" size="4" maxlength="9" styleClass="box" readOnly="<%=readonly%>"></html:text>
								</td>
								<td>
									<html:text styleId="colegiadoNombre" property="colegiadoNombre" size="50" maxlength="50" styleClass="box" readOnly="<%=readonly%>"></html:text>
								</td>
								<%if(!modo.equals("ver")){%>
								<td><!-- Boton buscar --> 
									<input type="button" class="button" id="idButton"  name="Buscar" value="<siga:Idioma key="general.boton.search" />" onClick="buscarColegiado();"> 
								<!-- Boton limpiar -->
									&nbsp;
									<input type="button" class="button" id="idButton"  name="Limpiar" value="<siga:Idioma key="general.boton.clear" />" onClick="limpiarColegiado();">
								</td>
								<%}%>
								<td class="labelText" width="75px" style="text-align: right;">						
								</td>
								<td>
								</td>
								<td> 
								</td>
						</tr>
				</table>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>
	</td>	
	</tr>
	
	<tr>
		<!-- Busqueda automatica de juzgados-->
		<td colspan="4">
			<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado"> 

		   	<table width="100%" border ="0">
		   		<tr>
			   		<td class="labelText"  id="tdNumeroProcedimiento" >
			   			<siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/>
			   		</td>			   	
			   		<td>&nbsp;
			   			<input type="text" name="numeroProcedimiento" maxLength="<%=maxLenghtProc%>"  value="<%=numeroProcedimiento%>" class="<%=estilo%>"/>
					</td>
				
					<td class="labelText">&nbsp;&nbsp;
						<input type="text" name="codigoExtJuzgado" styleId="codigoExtJuzgado" class="<%=estilo%>" size="8" maxlength="10" onBlur="obtenerJuzgado();"/>
					</td>
					<td class="labelTextValor">
						<%if(!modo.equals("ver")){%>
							<html:select styleId="juzgados" styleClass="boxCombo" style="width:500px;" property="idJuzgado" onchange="cambiarJuzgado();">
								<bean:define id="juzgados" name="CaracteristicasForm" property="juzgados" type="java.util.Collection" />
								<html:optionsCollection name="juzgados" value="idJuzgado" label="nombre" />
							</html:select>
						<%}else{%>
								<%=descripcionJuzgado%>           	   
						<%}%>
					</td> 					
				</tr>
			</table>
			<table width="100%" border ="0">
				<tr>
					<td class="labelText" >
						<siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					
					<td> 
						<% if (!modo.equalsIgnoreCase("ver")) { %>
						 	<input name="nig" id="nig"  type="text" value="<%=nig%>" class="<%=estilo%>" style="size:19;width:200px"/>
						<%}else{%>
							<input name="nig" id="nig" type="text" value="<%=nig%>" class="boxConsulta" style="size:19;width:200px"/>
						<%}%>						
					</td>
					<td id="info" style="display:none">
						<img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
					</td>
					<td class="labelText" >	
						<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
					</td>
					<td class="labelTextValor"> 
						<%if(!modo.equals("ver")){%>
							<siga:ComboBD nombre="idPretension" tipo="comboPretensiones" ancho="420" clase="<%=estilo%>" filasMostrar="1" pestana="t" seleccionMultiple="false" obligatorio="false" parametro="<%=datosPretension%>" elementoSel="<%=pretensionesSel%>"  readonly="false"/>           	   
						<%}else{%>
							<%=descripcionPretension%>           	   
						<%}%>
					</td>									
				</tr>	
		  </table>
		</siga:ConjCampos> 
		</td>
<!------------------>				
	</tr>
			

<!-- LLAMADAS AJAX PARA LA CARGA DE COLEGIADO Y PARA LA CARGA DE JUZGADOS -->					
<ajax:updateFieldFromField 
	baseUrl="/SIGA/JGR_CaracteristicasAsistenciaLetrado.do?modo=getAjaxColegiado"
    source="colegiadoNumero" target="idPersona,colegiadoNumero,colegiadoNombre"
	parameters="colegiadoNumero={colegiadoNumero}" 
	preFunction="preAccionColegiado"
	postFunction="postAccionColegiado"
/>
		
</html:form>
</table>

<!-- BOTONERA CON GUARDAR, RESTABLECER Y VOLVER -->
<siga:ConjBotonesAccion botones="V,G,R" modo="<%=modo%>" clase="botonesDetalle"/>

<!-- para la busqueda de ajax de los colegiados -->  
<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
</html:form>
<html:form action="/JGR_GestionSolicitudesAceptadasCentralita.do"  method="POST" target="mainWorkArea">
	<html:hidden property="modo"/>
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idSolicitudAceptada"/>
</html:form>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>