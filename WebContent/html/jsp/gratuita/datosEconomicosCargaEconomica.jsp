<!DOCTYPE html>
<html>
<head>
<!-- datosEconomicosCargaEconomica.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>

<%@page import="java.util.List"%>
<%@page import="org.redabogacia.sigaservices.app.vo.ScsDeCargaEconomicaExtends"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Hashtable"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants.PARAMETRO"%>

<% 	
	HttpSession ses=request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	
	// para ver si tengo que buscar tras mostrar la pantalla
	List listaCargasEconomicas = (List) request.getAttribute("LISTA_CARGASECONOMICAS");
	String trNew = (String) request.getAttribute("TR_NEW");
	String[] tdsNew = (String[]) request.getAttribute("TDS_NEW");
	
	String accion = (String)request.getSession().getAttribute("accion");
	//aalg: INC_10624
	if(usr.getAccessType().equals(SIGAConstants.ACCESS_READ)) accion="ver";
	
	boolean editable = false;			
	String botones = "V";
	if (accion!=null && accion.equals("editar")) {
		editable=true;
		botones = "G,V";
	}
	
	String idtipoejg = (String) request.getParameter("idtipoejg");
	String anio = (String) request.getParameter("anio");
	String numero = (String) request.getParameter("numero");
%>	
 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js?ver=1.7'/>"></script>
	
	<title><siga:Idioma key="gratuita.datoseconomicos.cargaeconomica.titulo"/></title>		
	<siga:Titulo titulo="gratuita.datoseconomicos.cargaeconomica.cabecera" localizacion="gratuita.datoseconomicos.cargaeconomica.localizacion"/>
</head>

<body onload="calcularAltura();">
	<html:form action="/JGR_DatosEconomicosEJG.do" styleId="DatosEconomicosEJGForm" method="POST" target="mainWorkArea">
		<html:hidden property="modo" value=""/>
		<html:hidden property="id" value=""/>	
		<html:hidden property="idtipoejg" value="<%=idtipoejg%>"/>	
		<html:hidden property="anio" value="<%=anio%>"/>	
		<html:hidden property="numero" value="<%=numero%>"/>		
	
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
					ScsEJGAdm adm = new ScsEJGAdm (usr);
					
					Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero, idtipoejg,(String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString()));
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
		
		<table id='cabeceraTabla' name='cabeceraTabla' width='100%'
			cellspacing='0' cellpadding='0' class='fixedHeaderTable dataScroll'
			style='table-layout: fixed; border-spacing: 0px;'>
			<thead class='Cabeceras' style='text-align: center;'>
				<tr class='tableTitle'>
					<th style='text-align: center; width: 22%;'><siga:Idioma
							key="gratuita.datoseconomicos.tipocargaeconomica" />
					
					<th style='text-align: center; width: 22%;'><siga:Idioma
							key="gratuita.datoseconomicos.periodicidad" /></th>
					<th style='text-align: center; width: 35%;'><siga:Idioma
							key="gratuita.datoseconomicos.titular" /></th>
					<th style='text-align: center; width: 16%;'><siga:Idioma
							key="gratuita.datoseconomicos.importe" /></th>
					<%
						if (editable) {
					%>
					<th style='text-align: center; width: 5%;'>&nbsp;</th>
					<%
						}
					%>
				</tr>
			</thead>
		</table>
	
		<div style="overflow-y:auto; position:absolute;width:100%;" id="divDatosTabla">
			<table border="1" cellspacing="0" cellpadding="5" id='datosTabla' width="100%">
				<% 
					if (listaCargasEconomicas != null && listaCargasEconomicas.size()>0) {
						for (int i=0; i<listaCargasEconomicas.size(); i++) {
							ScsDeCargaEconomicaExtends dato = (ScsDeCargaEconomicaExtends) listaCargasEconomicas.get(i);
							
							String claseFila;
							if((i+2)%2==0)
		   	 	 				claseFila = "filaTablaPar";
		   	 				else
		   		 				claseFila = "filaTablaImpar";
				%>				
					<tr class="<%=claseFila%>">
						<td align="left" width="22%">
							<%=dato.getDescripciontipocargaeconomica()%>						
						</td>
						<td align="left" width="22%">
							<%=dato.getDescripcionperiodicidad()%>						
						</td>
						<td align="left" width="35%">
							<%=dato.getTitular()%>
						</td>
						<td align="right" width="16%">
							<%=dato.getImporteFormateado()%> &euro;
						</td>		
						
						<% if (editable) { %>		
							<td align="center" width="5%">
								<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" 
									onclick="borrarCargaEconomica(<%=dato.getIdcargaeconomica()%>)">
							</td>
						<% } %>
					</tr>
				<% }} %>
				
				<% if (editable) { %>	
					<%=trNew%>
				<% } %>
			</table>
		</div>
	</html:form>
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<siga:ConjBotonesAccion botones="<%=botones%>" />
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitAreaDatosEnocomicosCargaEconomica" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>
</html>

<script language="JavaScript">
	var numFilasNuevas = 1;
	var numMaxFilaNueva = 1;
	
	function calcularAltura() {		
		if(document.getElementById("idBotonesAccion")) {
			var tablaBotones = jQuery('#idBotonesAccion')[0];						
			var tablaDatos = jQuery('#divDatosTabla')[0];
			
			var posTablaBotones = tablaBotones.offsetTop;
			var posTablaDatos = tablaDatos.offsetTop;
			
			jQuery('#divDatosTabla').height(posTablaBotones - posTablaDatos);
		}
		
		validarAnchoTabla();
	}	
	
	function validarAnchoTabla() {
		if (document.getElementById("datosTabla").clientHeight < document.getElementById("divDatosTabla").clientHeight) {
			document.getElementById("cabeceraTabla").width='100%';
		}
		else {
			document.getElementById("cabeceraTabla").width='98.30%';
		}
	}	
	
	function crearFila() {  		
		var tabla = document.getElementById("datosTabla");
		
		var claseFila ="filaTablaPar";
	   	if((tabla.rows.length+2)%2==0)
	   		claseFila = "filaTablaPar";
	   	else
	   		claseFila = "filaTablaImpar";

	   	var tr = tabla.insertRow(-1);	   
	   	numMaxFilaNueva++;
		numFilasNuevas++;	   		   	
		tr.className=claseFila;	
		tr.id = "fila_" + numMaxFilaNueva;
		
		td = tr.insertCell(0);		
		td.setAttribute("width", "22%");
		td.setAttribute("align", "left");		
		var tdNew ="<%=tdsNew[0]%>";
		tdNew = tdNew.replace("select_TiposCargasEconomicas_1", "select_TiposCargasEconomicas_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(1);		
		td.setAttribute("width", "22%");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[1]%>";
		tdNew = tdNew.replace("select_Periodicidades_1", "select_Periodicidades_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(2);		
		td.setAttribute("width", "35%");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[2]%>";
		tdNew = tdNew.replace("select_Titulares_1", "select_Titulares_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(3);		
		td.setAttribute("width", "16%");
		td.setAttribute("align", "right");		
		tdNew ="<%=tdsNew[3]%>";
		tdNew = tdNew.replace("input_Importe_1", "input_Importe_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(4);		
		td.setAttribute("width", "5%");
		td.setAttribute("align", "center");		
		tdNew ="<%=tdsNew[4]%>";
		tdNew = tdNew.replace("borrarFila(1)", "borrarFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		validarAnchoTabla();
	}	
	
	function cambiaFila(idFila){
		if (idFila==numMaxFilaNueva) {
			crearFila();
		}
	}	

	function borrarFila(idFila){			
		if (numFilasNuevas>1) {
			var tabla = document.getElementById("datosTabla");
			var encontrado = false;
			var numEncontrado = 0;
			var sFila = "fila_"+idFila;			

			for (var i = 0; i<tabla.rows.length; i++) {
				if (encontrado) {
					if (tabla.rows[i].className == 'filaTablaPar')
						tabla.rows[i].className='filaTablaImpar';
					else 
						tabla.rows[i].className='filaTablaPar';
							
				} else if (tabla.rows[i].id == sFila) {					
					numEncontrado=i;
					encontrado=true;					
				}				
			}
			
			if (encontrado) {
				tabla.deleteRow(numEncontrado);
				numFilasNuevas--;
				
				var idMaximoFila = tabla.rows[tabla.rows.length-1].id;
				numMaxFilaNueva = parseInt(idMaximoFila.split("_")[1],10);
			}
			
			validarAnchoTabla();
		
		} else {
			var objeto = document.getElementById("select_TiposCargasEconomicas_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_Periodicidades_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_Titulares_"+idFila);
			objeto.value="";
			objeto = document.getElementById("input_Importe_"+idFila);
			objeto.value="";
		}
	}

	function borrarCargaEconomica(idCargaEconomica){		
		if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>")) {
			document.DatosEconomicosEJGForm.target="mainPestanasDatosEconomicos";
			document.DatosEconomicosEJGForm.id.value=idCargaEconomica;
			document.DatosEconomicosEJGForm.modo.value="borrarCargaEconomica";
			document.DatosEconomicosEJGForm.submit();
		}
	}
	
	function accionVolver(){		
		document.forms[0].action="./JGR_EJG.do";	
		document.forms[0].modo.value="buscar";
		document.forms[0].target="mainWorkArea"; 
		document.forms[0].submit(); 
	}	
	
	function obtenerDatos () {
		var tabla = document.getElementById("datosTabla");
		var resultado = "";		
		var regImporte = /^[0-9]{1,10}([,.][0-9]{0,2})?$/;

		for (var i = 0; i<tabla.rows.length; i++) {
			var idFila = tabla.rows[i].id;

			if (idFila.indexOf('fila_') == 0) {
				var idTipoCargaEconomica = tabla.rows[i].cells[0].children[0].value;
				var idPeriodicidad = tabla.rows[i].cells[1].children[0].value;
				var idTitular = tabla.rows[i].cells[2].children[0].value;
				var importe = tabla.rows[i].cells[3].children[0].value;
				
				if (idTipoCargaEconomica!="" && idPeriodicidad!="" && idTitular!="" && importe!="") {
					if (!regImporte.test(importe)){
						alert("<siga:Idioma key='gratuita.datoseconomicos.importe.errorFormato'/>");
						return "";
					}			
					
					importe = importe.replace(",", ".");
					
					if (resultado=="") {
						resultado=""+idTipoCargaEconomica+"---"+idPeriodicidad+"---"+idTitular+"---"+importe;
					} else {
						resultado+="%%%"+idTipoCargaEconomica+"---"+idPeriodicidad+"---"+idTitular+"---"+importe;				
					}
				
				} else {
					if ((idTipoCargaEconomica=="" || idEjercicio=="" || idTitular=="" || importe=="") && (idTipoCargaEconomica!="" || idPeriodicidad!="" || idTitular!="" || importe!="")) {
						alert("<siga:Idioma key='gratuita.datoseconomicos.lineasIncompletas'/>");
						return "";
					}
				}				
			}
		}
		
		if (resultado=="") {
			alert("<siga:Idioma key='gratuita.datoseconomicos.lineasIncompletas'/>");
		}
		
		return resultado;
	}
	
	function accionGuardar(){
		var datos = obtenerDatos();
		if (datos != "") {
			document.DatosEconomicosEJGForm.target="mainPestanasDatosEconomicos";
			document.DatosEconomicosEJGForm.modo.value="guardarCargaEconomica";
			document.DatosEconomicosEJGForm.id.value=datos;
			document.DatosEconomicosEJGForm.submit();
		}
	}		
</script>