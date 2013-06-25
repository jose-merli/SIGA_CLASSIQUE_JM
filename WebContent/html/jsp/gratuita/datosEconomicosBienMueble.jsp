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
<%@page import="org.redabogacia.sigaservices.app.vo.ScsDeBienMuebleExtends"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAGestorInterfaz"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<% 	
	HttpSession ses=request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	
	if (src==null) {
	  SIGAGestorInterfaz interfazGestor=new SIGAGestorInterfaz("2000");
	  src=interfazGestor.getInterfaceOptions();	  
	}	
	
	Integer alturaDatosTabla = 0;
	String tipoLetra = "";
	if (((String)src.get("font.style")).indexOf("Times")!=-1) {
		alturaDatosTabla = 98;
		tipoLetra = "Times";
	} else if (((String)src.get("font.style")).indexOf("Arial")>=0) {
		alturaDatosTabla = 96;
		tipoLetra = "Arial";
	} else {
	    alturaDatosTabla = 92;
	    tipoLetra = "Helvetica";
	} 
	
	// para ver si tengo que buscar tras mostrar la pantalla
	List listaBienes = (List) request.getAttribute("LISTA_BIENES");
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

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js?ver=1.7'/>"></script>
	
	<title><siga:Idioma key="gratuita.datoseconomicos.bienesmuebles.titulo"/></title>		
	<siga:Titulo titulo="gratuita.datoseconomicos.bienesmuebles.cabecera" localizacion="gratuita.datoseconomicos.bienesmuebles.localizacion"/>
</head>

<body onload="calcularAltura();">
	<html:form action="/JGR_DatosEconomicosEJG.do" styleId="DatosEconomicosEJGForm" method="POST" target="mainWorkArea">
		<html:hidden property="modo" value=""/>
		<html:hidden property="id" value=""/>
		<html:hidden property="idtipoejg" value="<%=idtipoejg%>"/>	
		<html:hidden property="anio" value="<%=anio%>"/>	
		<html:hidden property="numero" value="<%=numero%>"/>		
	
		<table class="tablaTitulo" cellspacing="0" heigth="38">
			<tr>
				<%  String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "", t_tipoEJG="";;
					ScsEJGAdm adm = new ScsEJGAdm (usr);
					
					Hashtable hTitulo = adm.getTituloPantallaEJG(usr.getLocation(), anio, numero, idtipoejg);
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
		<br>		
		<table border="1" cellspacing="0" cellpadding="5" id='cabeceraTabla' width="100%">
			<tr class="tableTitle">
				<td align="center" width="22%">
					<b><siga:Idioma key="gratuita.datoseconomicos.origenvaloracion"/></b>
				</td>
				<td align="center" width="22%">
					<b><siga:Idioma key="gratuita.datoseconomicos.tipomueble"/></b>
				</td>
				<td align="center" width="35%">
					<b><siga:Idioma key="gratuita.datoseconomicos.titular"/></b>
				</td>
				<td align="center" width="16%">
					<b><siga:Idioma key="gratuita.datoseconomicos.valoracion"/></b>
				</td>
				<td align="center" width="5%">
					<b>&nbsp;</b>
				</td>					
			</tr>
		</table>
		
		<div style="overflow-y:auto; position:absolute;width:100%;" id="divDatosTabla">
			<table border="1" cellspacing="0" cellpadding="5" id='datosTabla' width="100%">
				<% 
					if (listaBienes != null && listaBienes.size()>0) {
						for (int i=0; i<listaBienes.size(); i++) {
							ScsDeBienMuebleExtends dato = (ScsDeBienMuebleExtends) listaBienes.get(i);
							
							String claseFila;
							if((i+2)%2==0)
		   	 	 				claseFila = "filaTablaPar";
		   	 				else
		   		 				claseFila = "filaTablaImpar";
				%>				
					<tr class="<%=claseFila%>">
						<td align="left" width="22%">
							<%=dato.getDescripcionorigenvaloracion()%>						
						</td>
						<td align="left" width="22%">
							<%=dato.getDescripciontipomueble()%>						
						</td>
						<td align="left" width="35%">
							<%=dato.getTitular()%>
						</td>
						<td align="right" width="16%">
							<%=dato.getValoracionFormateada()%> &#8364;
						</td>		
						
						<% if (editable) { %>		
							<td align="center" width="5%">
								<img src="/SIGA/html/imagenes/bborrar_off.gif" style="cursor:pointer;" title="<siga:Idioma key='general.boton.borrar'/>" alt="<siga:Idioma key='general.boton.borrar'/>" name="" border="0" 
									onclick="borrarBien(<%=dato.getIdbienmueble()%>)">
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
	<iframe name="submitAreaDatosEnocomicosBienesMuebles" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>
</html>

<script language="JavaScript">
	var numFilasNuevas = 1;
	var numMaxFilaNueva = 1;
	
	function calcularAltura() {		
		var altura = document.getElementById("divDatosTabla").offsetParent.offsetHeight;
		document.getElementById("divDatosTabla").style.height=altura-<%=alturaDatosTabla%>;
		
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
		tdNew = tdNew.replace("select_OrigenValoraciones_1", "select_OrigenValoraciones_"+numMaxFilaNueva);
		tdNew = tdNew.replace("cambiaFila(1)", "cambiaFila("+numMaxFilaNueva+")");
		td.innerHTML=tdNew;
		
		td = tr.insertCell(1);		
		td.setAttribute("width", "22%");
		td.setAttribute("align", "left");		
		tdNew ="<%=tdsNew[1]%>";
		tdNew = tdNew.replace("select_TiposMuebles_1", "select_TiposMuebles_"+numMaxFilaNueva);
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
		tdNew = tdNew.replace("input_Valoracion_1", "input_Valoracion_"+numMaxFilaNueva);
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
			var objeto = document.getElementById("select_OrigenValoraciones_"+idFila);
			objeto.value="";
			objeto = document.getElementById("select_TiposMuebles_"+idFila);
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
			document.DatosEconomicosEJGForm.modo.value="borrarMueble";
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
				var idOrigenValoracion = tabla.rows[i].cells[0].children[0].value;
				var idTipoMueble = tabla.rows[i].cells[1].children[0].value;
				var idTitular = tabla.rows[i].cells[2].children[0].value;
				var valoracion = tabla.rows[i].cells[3].children[0].value;
				
				if (idOrigenValoracion!="" && idTipoMueble!="" && idTitular!="" && valoracion!="") {
					if (!regImporte.test(valoracion)){
						alert("<siga:Idioma key='gratuita.datoseconomicos.valoracion.errorFormato'/>");
						return "";
					}			
					
					valoracion = valoracion.replace(",", ".");
					
					if (resultado=="") {
						resultado=""+idOrigenValoracion+"---"+idTipoMueble+"---"+idTitular+"---"+valoracion;
					} else {
						resultado+="%%%"+idOrigenValoracion+"---"+idTipoMueble+"---"+idTitular+"---"+valoracion;				
					}
				
				} else {
					if ((idOrigenValoracion=="" || idTipoMueble=="" || idTitular=="" || valoracion=="") && (idOrigenValoracion!="" || idTipoMueble!="" || idTitular!="" || valoracion!="")) {
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
			document.DatosEconomicosEJGForm.modo.value="guardarMueble";
			document.DatosEconomicosEJGForm.id.value=datos;
			document.DatosEconomicosEJGForm.submit();
		}
	}
</script>
