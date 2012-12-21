<!-- datosEconomicosIrpf20.jsp -->

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
<%@page import="org.redabogacia.sigaservices.app.vo.ScsDeBienInmuebleExtends"%>

<% 	
	// para ver si tengo que buscar tras mostrar la pantalla
	List listaBienes = (List) request.getAttribute("LISTA_BIENES");
	String trNew = (String) request.getAttribute("TR_NEW");
	String[] tdsNew = (String[]) request.getAttribute("TDS_NEW");
	
	String accion = (String)request.getSession().getAttribute("accion");
	
	boolean editable = false;			
	String anchoTabla = "1817px";		
	String botones = "V";
	if (accion!=null && accion.equals("editar")) {
		editable=true;
		anchoTabla="1879px";
		botones = "G,V";
	}
	
	String idtipoejg = (String) request.getParameter("idtipoejg");
	String anio = (String) request.getParameter("anio");
	String numero = (String) request.getParameter("numero");	
%>	

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js?ver=1.7'/>"></script>
	
	<title><siga:Idioma key="gratuita.datoseconomicos.bienesinmuebles.titulo"/></title>		
	<siga:Titulo titulo="gratuita.datoseconomicos.bienesinmuebles.cabecera" localizacion="gratuita.datoseconomicos.bienesinmuebles.localizacion"/>
</head>

<body>
	<html:form action="/JGR_DatosEconomicosEJG.do" styleId="DatosEconomicosEJGForm" method="POST" target="mainWorkArea">
		<html:hidden property="modo" value=""/>
		<html:hidden property="id" value=""/>
		<html:hidden property="idtipoejg" value="<%=idtipoejg%>"/>	
		<html:hidden property="anio" value="<%=anio%>"/>	
		<html:hidden property="numero" value="<%=numero%>"/>		
	
		<br>		
		<table border="1" cellspacing="0" cellpadding="5">
			<tr class="tableTitle">
				<td align="center" width="120px">
					<b><siga:Idioma key="gratuita.datoseconomicos.origenvaloracion"/></b>
				</td>
				<td align="center" width="200px">
					<b><siga:Idioma key="gratuita.datoseconomicos.tipovivienda"/></b>
				</td>
				<td align="center" width="150px">
					<b><siga:Idioma key="gratuita.datoseconomicos.tipoinmueble"/></b>
				</td>				
				<td align="center" width="200px">
					<b><siga:Idioma key="gratuita.datoseconomicos.titular"/></b>
				</td>
				<td align="center" width="150px">
					<b><siga:Idioma key="gratuita.datoseconomicos.valoracion"/></b>
				</td>				
			</tr>
		</table>
		
		<div style="height:590px; overflow-y:auto; position:absolute; width:<%=anchoTabla%>">
			<table border="1" cellspacing="0" cellpadding="5" id='tablaPrincipal'>
				<% 
					if (listaBienes != null && listaBienes.size()>0) {
						for (int i=0; i<listaBienes.size(); i++) {
							ScsDeBienInmuebleExtends dato = (ScsDeBienInmuebleExtends) listaBienes.get(i);
							
							String claseFila;
							if((i+2)%2==0)
		   	 	 				claseFila = "filaTablaPar";
		   	 				else
		   		 				claseFila = "filaTablaImpar";
				%>				
					<tr class="<%=claseFila%>">
						<td align="left" width="120px">
							<%=dato.getDescripcionorigenvaloracion()%>						
						</td>
						<td align="left" width="200px">
							<%=dato.getDescripciontipovivienda()%>						
						</td>
						<td align="left" width="150px">
							<%=dato.getDescripciontipoinmueble()%>						
						</td>
						<td align="left" width="200px">
							<%=dato.getTitular()%>
						</td>
						<td align="right" width="150px">
							<%=dato.getValoracionFormateada()%> &#8364;
						</td>		
						
						<% if (editable) { %>		
							<td align="center" width="50px">
								<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" 
									onclick="borrarBien(<%=dato.getIdbieninmueble()%>)">
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
	<iframe name="submitAreaDatosEnocomicosBienesInmuebles" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>
</html>

<script language="JavaScript">
	var numFilasNuevas = 1;
	var numMaxFilaNueva = 1;
	
	function crearFila() {  		
		var tabla = document.getElementById("tablaPrincipal");
		
		var claseFila ="filaTablaPar";
	   	if((tabla.rows.length+2)%2==0)
	   		claseFila = "filaTablaPar";
	   	else
	   		claseFila = "filaTablaImpar";

	   	var tr = tabla.insertRow();	   
	   	numMaxFilaNueva++;
		numFilasNuevas++;	   		   	
		tr.className=claseFila;	
		tr.id = "fila_" + numMaxFilaNueva;
		
		td = tr.insertCell(0);		
		td.setAttribute("width", "120px");
		td.setAttribute("align", "left");		
		var tdNew ="<%=tdsNew[0]%>";
		tdNew = tdNew.replace("select_OrigenValoraciones_1", "select_OrigenValoraciones_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(1);		
		td.setAttribute("width", "200px");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[1]%>";
		tdNew = tdNew.replace("select_TiposViviendas_1", "select_TiposViviendas_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(2);		
		td.setAttribute("width", "150px");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[2]%>";
		tdNew = tdNew.replace("select_TiposInmuebles_1", "select_TiposInmuebles_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;		
		
		td = tr.insertCell(3);		
		td.setAttribute("width", "200px");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[3]%>";
		tdNew = tdNew.replace("select_Titulares_1", "select_Titulares_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(4);		
		td.setAttribute("width", "150px");
		td.setAttribute("align", "right");		
		tdNew ="<%=tdsNew[4]%>";
		tdNew = tdNew.replace("input_Valoracion_1", "input_Valoracion_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(5);		
		td.setAttribute("width", "50px");
		td.setAttribute("align", "center");		
		tdNew ="<%=tdsNew[5]%>";
		tdNew = tdNew.replace("borrarFila(1)", "borrarFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
	}	
	
	function cambiaFila(idFila){
		if (idFila==numMaxFilaNueva) {
			crearFila();
		}
	}	

	function borrarFila(idFila){			
		if (numFilasNuevas>1) {
			var tabla = document.getElementById("tablaPrincipal");
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
		
		} else {
			var objeto = document.getElementById("select_OrigenValoraciones_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_TiposViviendas_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_TiposInmuebles_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_Titulares_"+idFila);
			objeto.value="";
			objeto = document.getElementById("input_Valoracion_"+idFila);
			objeto.value="";
		}
	}

	function borrarBien(idBien){		
		if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>")) {
			document.DatosEconomicosEJGForm.target="mainPestanasDatosEconomicos";
			document.DatosEconomicosEJGForm.id.value=idBien;
			document.DatosEconomicosEJGForm.modo.value="borrarInmueble";
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
		var tabla = document.getElementById("tablaPrincipal");
		var resultado = "";		
		var regImporte = /^[0-9]{1,10}([,.][0-9]{0,2})?$/;

		for (var i = 0; i<tabla.rows.length; i++) {
			var idFila = tabla.rows[i].id;

			if (idFila.indexOf('fila_') == 0) {
				var idOrigenValoracion = tabla.rows[i].cells[0].children[0].value;
				var idTipoVivienda = tabla.rows[i].cells[1].children[0].value;
				var idTipoInmueble = tabla.rows[i].cells[2].children[0].value;
				var idTitular = tabla.rows[i].cells[3].children[0].value;
				var valoracion = tabla.rows[i].cells[4].children[0].value;
				
				if (idOrigenValoracion!="" && idTipoVivienda!="" && idTitular!="" && valoracion!="") {
					if (!regImporte.test(valoracion)){
						alert("<siga:Idioma key='gratuita.datoseconomicos.valoracion.errorFormato'/>");
						return "";
					}			
					
					valoracion = valoracion.replace(",", ".");
					
					if (resultado=="") {
						resultado=""+idOrigenValoracion+"---"+idTipoVivienda+"---"+idTipoInmueble+"---"+idTitular+"---"+valoracion;
					} else {
						resultado+="%%%"+idOrigenValoracion+"---"+idTipoVivienda+"---"+idTipoInmueble+"---"+idTitular+"---"+valoracion;				
					}
				
				} else {
					if ((idOrigenValoracion=="" || idTipoVivienda=="" || idTipoInmueble=="" || idTitular=="" || valoracion=="") && (idOrigenValoracion!="" || idTipoVivienda!="" || idTipoInmueble!="" || idTitular!="" || valoracion!="")) {
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
			document.DatosEconomicosEJGForm.modo.value="guardarInmueble";
			document.DatosEconomicosEJGForm.id.value=datos;
			document.DatosEconomicosEJGForm.submit();
		}
	}
</script>
