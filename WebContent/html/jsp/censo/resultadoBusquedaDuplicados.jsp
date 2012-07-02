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
	
	String nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2,censo.resultadoDuplicados.institucion,censo.resultadoDuplicados.numeroColegiado";
	String tamanoCol="3,10,10,18,18,18,10,8";
	boolean ocultarColegiaciones=true;
	if(form!=null){
		ocultarColegiaciones = form.getAgruparColegiaciones()!=null&&form.getAgruparColegiaciones().equalsIgnoreCase("s");
	}
	if(ocultarColegiaciones){
		nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2, Número de colegiaciones";
		tamanoCol="3,10,10,20,20,20,12";
	}
%>

<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script><script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>

	<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>


<html>

<!-- HEAD -->
<head>

	<script type="text/javascript">
	var contador=0;
	ObjArray = new Array();

	function refrescarLocal () 
	{
		parent.buscar();
	}
	
	function comparar(){
		if (document.getElementById('seleccionados') &&
			document.getElementById('seleccionados').value==2){
			document.MantenimientoDuplicadosForm.modo.value = "gestionar";
			sub(); 
			var resultado = ventaModalGeneral("MantenimientoDuplicadosForm","G");
			if(resultado&&resultado!="NOMODIFICADO"){
				buscar();
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
		fin();
	}

	function accionVolver() 
	{	
		desmarcar();
		document.forms[0].modo.value="volver";
		document.forms[0].target="mainWorkArea";
		document.forms[0].submit();
	}

	function buscar() 
	{	
		desmarcar();
		document.forms[0].modo.value="buscar";
		document.forms[0].target="mainWorkArea";
		document.forms[0].submit();
	}

	function accionExportar(){
		
		if(<%=nRegistros<100%>||confirm("Hay muchos registros. El proceso de exportación puede tardar varios minutos.")){
			//sub();
			seleccionarTodo();
			document.forms[0].modo.value="export";
			document.forms[0].target="new";
			document.forms[0].submit();
			desmarcar();
		}
	}

	function seleccionarTodo(){
		var ele = document.getElementsByName("chkPersona");
		selArray = new Array();
	  	for (i = 0; i < ele.length; i++) {
		  	obj=ele[i];
			selArray.push(obj.value);
		   	seleccionados1=selArray;
   		}
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		document.getElementById('seleccionados').value =selArray.length;
		document.forms[0].seleccion.value=seleccionados1;
	}
	
	</script>

</head>

<body class="tablaCentralCampos" >

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" id="actionModal"  value="">
	<html:hidden styleId="modo" property="modo"/>
	<html:hidden styleId="registrosSeleccionados" property="registrosSeleccionados" />
	<html:hidden styleId="seleccionados" property="seleccionados" />
	<html:hidden styleId="seleccion" property="seleccion" />
	<html:hidden styleId="datosPaginador" property="datosPaginador" />
	<html:hidden styleId="chkApellidos" property="chkApellidos" />
	<html:hidden styleId="chkNombreApellidos" property="chkNombreApellidos" />
	<html:hidden styleId="chkNumColegiado" property="chkNumColegiado" />
	<html:hidden styleId="chkIdentificador" property="chkIdentificador" />
	<html:hidden styleId="nifcif" property="nifcif" />
	<html:hidden styleId="nombre" property="nombre" />
	<html:hidden styleId="numeroColegiado" property="numeroColegiado" />
	<html:hidden styleId="apellido1" property="apellido1" />
	<html:hidden styleId="apellido2" property="apellido2" />
	<html:hidden styleId="agruparColegiaciones" property="agruparColegiaciones" />
	<html:hidden styleId="tipoConexion" property="tipoConexion" />
	<html:hidden styleId="sentidoOrdenacion" property="sentidoOrdenacion" />
	<html:hidden styleId="campoOrdenacion" property="campoOrdenacion" />
	
	<table class="tablaTitulo" align="center">
		<tr>
		<td class="titulitos">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.resultados" />
		</td>
		</tr>
	</table>
	
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
		
	<%if (resultado == null || resultado.size() == 0) {%>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="Volver" onclick="accionVolver();">
			</td>
		</tr>
	</table>
	<%} else {%>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="Volver" onclick="accionVolver();">
			</td>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="censo.resultadoDuplicados.botonLimpiar"/>" onclick="desmarcar();">
			</td>
			<td style="width:550px;text-align:center" class="labelText"><%=nRegistros%> registros</td>
			<td class="tdBotones">
				<input type="button" id="idButton" name="idButton" class="button" value="<siga:Idioma key="general.boton.exportarExcel"/>" onclick="accionExportar();">
			</td>
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
