<!-- datosEconomicosIngresos.jsp -->

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
<%@page import="org.redabogacia.sigaservices.app.vo.ScsDeIngresosExtends"%>

<% 	
	// para ver si tengo que buscar tras mostrar la pantalla
	List listaIngresos = (List) request.getAttribute("LISTA_INGRESOS");
	String trNew = (String) request.getAttribute("TR_NEW");
	String[] tdsNew = (String[]) request.getAttribute("TDS_NEW");
	
	String accion = (String)request.getSession().getAttribute("accion");
	
	boolean editable = false;			
	String anchoTabla = "817px";	
	String botones = "V";
	if (accion!=null && accion.equals("editar")) {
		editable=true;
		anchoTabla="879px";
		botones = "G,V";
	}
	
	//UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");
	//String idioma = usuario.getLanguage();
	
%>	

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>		
</head>

<body>
	<html:form action="/JGR_DatosEconomicosEJG.do" styleId="DatosEconomicosEJGForm" method="POST" target="mainWorkArea">
		<html:hidden property="modo" value=""/>
		<html:hidden property="id" value=""/>	
	
		<br>		
		<table border="1" cellspacing="0" cellpadding="5">
			<tr class="tableTitle">
				<td align="center" width="200px">
					<b><siga:Idioma key="gratuita.datoseconomicos.tipoingreso"/></b>
				</td>
				<td align="center" width="150px">
					<b><siga:Idioma key="gratuita.datoseconomicos.periodicidad"/></b>
				</td>
				<td align="center" width="250px">
					<b><siga:Idioma key="gratuita.datoseconomicos.perceptor"/></b>
				</td>
				<td align="center" width="150px">
					<b><siga:Idioma key="gratuita.datoseconomicos.importe"/></b>
				</td>				
			</tr>
		</table>
		
		<div style="height:590px; overflow-y:auto; position:absolute; width:<%=anchoTabla%>">
			<table border="1" cellspacing="0" cellpadding="5" id='ingresostabla'>
				<% 
					if (listaIngresos != null && listaIngresos.size()>0) {
						for (int i=0; i<listaIngresos.size(); i++) {
							ScsDeIngresosExtends datoIngreso = (ScsDeIngresosExtends) listaIngresos.get(i);
							
							String claseFila;
							if((i+2)%2==0)
		   	 	 				claseFila = "filaTablaPar";
		   	 				else
		   		 				claseFila = "filaTablaImpar";
				%>				
					<tr class="<%=claseFila%>">
						<td align="left" width="200px">
							<%=datoIngreso.getDescripcionTipoIngreso()%>						
						</td>
						<td align="left" width="150px">
							<%=datoIngreso.getDescripcionPeriodicidad()%>						
						</td>
						<td align="left" width="250px">
							<%=datoIngreso.getPerceptor()%>
						</td>
						<td align="right" width="150px">
							<%=datoIngreso.getImporteFormateado()%> &#8364;
						</td>		
						
						<% if (editable) { %>		
							<td align="center" width="50px">
								<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" 
									onclick="borrarIngreso(<%=datoIngreso.getIddeingresos()%>)">
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
	<iframe name="submitAreaDatosEnocomicosIngresos" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>
</html>

<script language="JavaScript">
	var numFilasNuevas = 1;
	var numMaxFilaNueva = 1;
	
	function crearFila() {  		
		var tabla = document.getElementById("ingresostabla");
		
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
		td.setAttribute("width", "200px");
		td.setAttribute("align", "left");		
		var tdNew ="<%=tdsNew[0]%>";
		tdNew = tdNew.replace("select_TiposIngresos_1", "select_TiposIngresos_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(1);		
		td.setAttribute("width", "150px");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[1]%>";
		tdNew = tdNew.replace("select_Periodicidades_1", "select_Periodicidades_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(2);		
		td.setAttribute("width", "250px");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[2]%>";
		tdNew = tdNew.replace("select_Perceptores_1", "select_Perceptores_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(3);		
		td.setAttribute("width", "150px");
		td.setAttribute("align", "right");		
		tdNew ="<%=tdsNew[3]%>";
		tdNew = tdNew.replace("input_Importe_1", "input_Importe_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(4);		
		td.setAttribute("width", "50px");
		td.setAttribute("align", "center");		
		tdNew ="<%=tdsNew[4]%>";
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
			var tabla = document.getElementById("ingresostabla");
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
			var objeto = document.getElementById("select_TiposIngresos_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_Periodicidades_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_Perceptores_"+idFila);
			objeto.value="";
			objeto = document.getElementById("input_Importe_"+idFila);
			objeto.value="";
		}
	}

	function borrarIngreso(idIngreso){		
		if (confirm("<siga:Idioma key='messages.deleteConfirmation'/>")) {
			document.DatosEconomicosEJGForm.target="mainPestanasDatosEconomicos";
			document.DatosEconomicosEJGForm.id.value=idIngreso;
			document.DatosEconomicosEJGForm.modo.value="borrarIngreso";
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
		var tabla = document.getElementById("ingresostabla");
		var resultado = "";		
		var regImporte = /^[0-9]{1,10}([,.][0-9]{0,2})?$/;

		for (var i = 0; i<tabla.rows.length; i++) {
			var idFila = tabla.rows[i].id;

			if (idFila.indexOf('fila_') == 0) {
				var idTipoIngreso = tabla.rows[i].cells[0].children[0].value;
				var idPeriodicidad = tabla.rows[i].cells[1].children[0].value;
				var idPerceptores = tabla.rows[i].cells[2].children[0].value;
				var importe = tabla.rows[i].cells[3].children[0].value;
				
				if (idTipoIngreso!="" && idPeriodicidad!="" && idPerceptores!="" && importe!="") {
					if (!regImporte.test(importe)){
						alert("<siga:Idioma key='gratuita.datoseconomicos.importe.errorFormato'/>");
						return "";
					}			
					
					importe = importe.replace(",", ".");
					
					if (resultado=="") {
						resultado=""+idTipoIngreso+"---"+idPeriodicidad+"---"+idPerceptores+"---"+importe;
					} else {
						resultado+="%%%"+idTipoIngreso+"---"+idPeriodicidad+"---"+idPerceptores+"---"+importe;				
					}
				
				} else {
					if ((idTipoIngreso=="" || idPeriodicidad=="" || idPerceptores=="" || importe=="") && (idTipoIngreso!="" || idPeriodicidad!="" || idPerceptores!="" || importe!="")) {
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
			document.DatosEconomicosEJGForm.modo.value="guardarIngresos";
			document.DatosEconomicosEJGForm.id.value=datos;
			document.DatosEconomicosEJGForm.submit();
		}
	}		
</script>