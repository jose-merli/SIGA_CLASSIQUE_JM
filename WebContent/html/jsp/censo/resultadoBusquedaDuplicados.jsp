<!-- resultadoBusquedaDuplicados.jsp -->

<!-- CABECERA JSP -->
<%@page import="org.apache.commons.validator.Form"%>
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

<%
	/**************/
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
	String valorCheckPersona = "";
	
	String action = app + "/CEN_MantenimientoDuplicados.do?noReset=true";
	int cont=0;
	
	Vector resultado = (Vector)request.getAttribute("resultado");
	int nRegistros = 0;
	if(resultado!=null)nRegistros=resultado.size();
	
	MantenimientoDuplicadosForm form = (MantenimientoDuplicadosForm)request.getAttribute("MantenimientoDuplicadosForm");
	
	boolean ocultarColegiaciones = form.getAgruparColegiaciones().equalsIgnoreCase("s");
	String nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2,censo.resultadoDuplicados.institucion,censo.resultadoDuplicados.numeroColegiado";
	String tamanoCol="3,10,10,18,18,18,10,8";
	if(ocultarColegiaciones){
		nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2, Número de colegiaciones";
		tamanoCol="3,10,10,20,20,20,12";
	}
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
		if (document.getElementById('seleccionados') &&
			document.getElementById('seleccionados').value==2){
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

	function pulsarCheck(obj){
		if (!obj.checked ){
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			if (document.getElementById('seleccionados').value<2){
				ObjArray.push(obj.value);
			   	seleccionados1=ObjArray;
			}else{
				obj.checked=false;
				alert("<siga:Idioma key='censo.resultadoDuplicados.error.demasiadosLetrados'/>");
			}
		}
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		document.getElementById('seleccionados').value =ObjArray.length;
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
		if(document.getElementById('seleccionados'))  		 
			document.getElementById('seleccionados').value =ObjArray.length;
	}
	
	</script>

</head>

<body class="tablaCentralCampos" >

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="modo"/>
	<html:hidden property="registrosSeleccionados" />
	<html:hidden property="seleccionados" />
	<html:hidden property="datosPaginador" />
	
	<siga:TablaCabecerasFijas 
	   nombre="tablaDatos"
	   borde="1"
	   clase="tableTitle"
	   nombreCol="<%=nombreCol %>"
	   tamanoCol="<%=tamanoCol %>"
	   alto="100%"
	   ajusteBotonera="true"
	   ajustePaginador="false">
		   
		<%if (resultado == null || resultado.size() == 0) {%>
			<br>
			<br>
				<p class="titulitos" style="text-align: center">
					<siga:Idioma key="messages.noRecordFound" />
				</p>
			<br>
			<br>
		<%}else{%>
			<%String identificador="", nif="", nombre="", apellido1="",apellido2="", institucion="",nColegiado="", inst="", valorCheck="", abrev="", colegiaciones="";%>
			<%Row fila; 
			  Hashtable registro;
			  String numFila;%>
			<% for(int i=0;i<resultado.size();i++){ 
				numFila = String.valueOf(i);
				//fila = (Row) resultado.elementAt(i);
				registro =(Hashtable)resultado.elementAt(i);
				identificador=""+(String)registro.get("IDPERSONA");
				nif=""+(String)registro.get("NIFCIF");
				nombre=""+(String)registro.get("NOMBRE");
				apellido1=""+(String)registro.get("APELLIDOS1");
				apellido2=""+(String)registro.get("APELLIDOS2");
				inst=""+(String)registro.get("IDINSTITUCION");
				abrev=""+(String)registro.get("ABREVIATURA");
				nColegiado=""+(String)registro.get("NCOLEGIADO");
				valorCheck = inst+"||"+identificador;
				colegiaciones=""+(String)registro.get("COLEGIACIONES");
				%>
			<tr class="<%=((i+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
				<td><input type="checkbox" value="<%=valorCheck%>" name="chkPersona" onclick="pulsarCheck(this);" ></td>
				<td><%=identificador%>&nbsp;</td>
				<td><%=nif%>&nbsp;</td>
				<td><%=nombre%>&nbsp;</td>
				<td><%=apellido1%>&nbsp;</td>
				<td><%=apellido2%>&nbsp;</td>
				<!-- Comprobamos si vamos a mostrar colegiaciones o personas -->
				<%if(ocultarColegiaciones){%>
					<td><%=colegiaciones%>&nbsp;</td>
				<%}else{%>
					<td><%=abrev%>&nbsp;</td>
					<td><%=nColegiado%>&nbsp;</td>
				<%}%>
			</tr>
			<%}%>
		<%}%>
		</siga:TablaCabecerasFijas>
		
	<%if (resultado != null) {%>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="censo.resultadoDuplicados.botonLimpiar"/>" onclick="desmarcar();">
			</td>
			<td style="width:900px;text-align:center" class="labelText"><%=nRegistros%> registros</td>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="censo.resultadoDuplicados.botonFusionar"/>" onclick="comparar();">
			</td>
		</tr>
	</table>
	<%}%>
</html:form>
	 
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
</body>
</html>
