<!-- resultadoBusquedaDuplicados.jsp -->

<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.siga.Utilidades.paginadores.PaginadorCaseSensitive"%>
<%@page import="com.siga.Utilidades.paginadores.Paginador"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.Vector"%>
<%@page import="com.atos.utils.ClsConstants"%>
<%@page import="com.atos.utils.UsrBean"%>
<%@page import="com.atos.utils.Row"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.administracion.SIGAConstants"%>
<%@page import="com.siga.censo.form.MantenimientoDuplicadosForm"%>
<%@page import="com.siga.Utilidades.paginadores.PaginadorBind"%>

<bean:define id="registrosSeleccionados" name="MantenimientoDuplicadosForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="MantenimientoDuplicadosForm" property="datosPaginador" type="java.util.HashMap"/>

<%
	/**************/
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
	String valorCheckPersona = "";
	
	/** PAGINADOR ***/
	Vector resultado = null;
	String paginaSeleccionada = "";
	String totalRegistros = "";
	String registrosPorPagina = "";
	/*HashMap datosPaginador = new HashMap();
	if (ses.getAttribute("DATOSPAGINADOR")!=null){
	 datosPaginador = (HashMap)ses.getAttribute("DATOSPAGINADOR");
	}*/
	if (datosPaginador!=null) {
		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");
			
			Paginador paginador = (Paginador) datosPaginador.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
			
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";
			totalRegistros = "0";
			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";
		totalRegistros = "0";
		registrosPorPagina = "0";
	}
	
	
			
	String action = app + "/CEN_MantenimientoDuplicados.do?noReset=true";
	int cont=0;
%>

<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
<link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

<html>

<!-- HEAD -->
<head>

	<script type="text/javascript">

	var contador=0;
	ObjArray = new Array();

	function refrescarLocal () 
	{
		alert("Vuelta");
		parent.buscar();
	}
	
	function comparar(){
		if (document.getElementById('registrosSeleccionadosPaginador') &&
			document.getElementById('registrosSeleccionadosPaginador').value==2){
			document.MantenimientoDuplicadosForm.modo.value = "gestionar";
			sub(); 
			var resultado = ventaModalGeneral("MantenimientoDuplicadosForm","G");
			if(resultado!="NOMODIFICADO"){
				parent.buscar();
			}else{
				fin();
			}
		}else{
			alert("<siga:Idioma key='censo.resultadoDuplicados.error.seleccioneLetrados'/>");
		}
	}

	function cargarChecks(){
		<%
		if (registrosSeleccionados!=null){
	   		for (int p=0;p<registrosSeleccionados.size();p++){
		   		Hashtable clavesEJG= (Hashtable) registrosSeleccionados.get(p);
				valorCheckPersona=(String)clavesEJG.get("CLAVE");%>
				ObjArray.push('<%=valorCheckPersona%>');
				<%
			} 
	   	}%>
		ObjArray.toString();
		seleccionados1=ObjArray;
		document.MantenimientoDuplicadosForm.registrosSeleccionados.value=seleccionados1;
		if(document.getElementById('registrosSeleccionadosPaginador'))
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			
	}

	function pulsarCheck(obj){
		if (!obj.checked ){
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			if (document.getElementById('registrosSeleccionadosPaginador').value<2){
				ObjArray.push(obj.value);
			   	seleccionados1=ObjArray;
			}else{
				obj.checked=false;
				alert("<siga:Idioma key='censo.resultadoDuplicados.error.demasiadosLetrados'/>");
			}
		}
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	}

	function desmarcar(){
		ObjArray1= new Array();
	 	ObjArray=ObjArray1;
	 	
	 	seleccionados1=ObjArray;
	 	
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		var ele = document.getElementsByName("chkPersona");
			
		for (i = 0; i < ele.length; i++) {
			if(!ele[i].disabled)	
				ele[i].checked = false; 
				
		}
		if(document.getElementById('registrosSeleccionadosPaginador'))  		 
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	}
	
	</script>

</head>

<body onload="cargarChecks();" class="tablaCentralCampos" >

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="modo"/>
	<html:hidden property="registrosSeleccionados" />
	<html:hidden property="datosPaginador" />
	
	<siga:TablaCabecerasFijas 
	   nombre="tablaDatos"
	   borde="1"
	   clase="tableTitle"
	   nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2,censo.resultadoDuplicados.institucion,censo.resultadoDuplicados.numeroColegiado"
	   tamanoCol="3,10,10,18,18,18,10,8"
	   alto="100%"
	   ajusteBotonera="true"
	   ajustePaginador="true">
		   
		<%if (resultado == null || resultado.size() == 0) {%>
			<br>
			<br>
				<p class="titulitos" style="text-align: center">
					<siga:Idioma key="messages.noRecordFound" />
				</p>
			<br>
			<br>
		<%}else{%>
			<%String identificador="", nif="", nombre="", apellido1="",apellido2="", institucion="",nColegiado="", inst="", valorCheck="", abrev=""; %>
			<%Row fila; 
			  Hashtable registro;
			  String numFila;%>
			<% for(int i=0;i<resultado.size();i++){ 
				numFila = String.valueOf(i);
				fila = (Row) resultado.elementAt(i);
				registro =(Hashtable) fila.getRow();
				identificador=""+(String)registro.get("IDPERSONA");
				nif=""+(String)registro.get("NIFCIF");
				nombre=""+(String)registro.get("NOMBRE");
				apellido1=""+(String)registro.get("APELLIDOS1");
				apellido2=""+(String)registro.get("APELLIDOS2");
				inst=""+(String)registro.get("IDINSTITUCION");
				abrev=""+(String)registro.get("ABREVIATURA");
				nColegiado=""+(String)registro.get("NCOLEGIADO");
				valorCheck = inst+"||"+identificador;
				%>
				<% boolean isChecked = false;
					for (int z = 0; z < registrosSeleccionados.size(); z++) {
						Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);

						String clave = (String)clavesRegistro.get("CLAVE");
						if (valorCheck.equals(clave)) {
							isChecked = true;
							break;
						}
				}%>
			<tr class="<%=((i+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
				<%if(isChecked){%>
				<td><input type="checkbox" checked value="<%=valorCheck%>" name="chkPersona" onclick="pulsarCheck(this);" ></td>
				<%}else{ %>
				<td><input type="checkbox" value="<%=valorCheck%>" name="chkPersona" onclick="pulsarCheck(this);" ></td>
				<%}%>
				<td><%=identificador%>&nbsp;</td>
				<td><%=nif%>&nbsp;</td>
				<td><%=nombre%>&nbsp;</td>
				<td><%=apellido1%>&nbsp;</td>
				<td><%=apellido2%>&nbsp;</td>
				<td><%=abrev%>&nbsp;</td>
				<td><%=nColegiado%>&nbsp;</td>
			</tr>
			<%}%>
		<%}%>
		</siga:TablaCabecerasFijas>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
	<tr>
		<td style="width: 900px;">&nbsp;</td>
		<td class="tdBotones">
			<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="censo.resultadoDuplicados.botonFusionar"/>" onclick="comparar();">
		</td>
		<td class="tdBotones">
			<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="censo.resultadoDuplicados.botonLimpiar"/>" onclick="desmarcar();">
		</td>
	</tr>
</table>
</html:form>
	
	<%if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){
		String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));
		//String regSeleccionados = "2";
		%>
		<siga:Paginador 
			totalRegistros="<%=totalRegistros%>" 
			registrosPorPagina="<%=registrosPorPagina%>" 
			paginaSeleccionada="<%=paginaSeleccionada%>"
			registrosSeleccionados="<%=regSeleccionados%>" 
			idioma="<%=idioma%>"
			modo="buscarPor"								
			clase="paginator" 
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
			distanciaPaginas=""
			action="<%=action%>" />
 	<%}%>
	 
	 
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
</body>
</html>
