<!DOCTYPE html>
<html>
<head>
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
<%@page import="com.siga.tlds.*"%>
<%@page import="com.siga.administracion.SIGAConstants"%>
<%@page import="com.siga.censo.form.MantenimientoDuplicadosForm"%>
<%@page import="com.siga.Utilidades.PaginadorBind"%>

<bean:define id="registrosSeleccionados" name="MantenimientoDuplicadosForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="MantenimientoDuplicadosForm" property="datosPaginador" type="java.util.HashMap"/>

<%
	/**************/
	try {
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idInstitucionLocation = usrbean.getLocation();
	String idioma = usrbean.getLanguage().toUpperCase();
	String valorCheckPersona = "";
	
	String action = app + "/CEN_MantenimientoDuplicados.do?noReset=true";
	
	
	/** PAGINADOR ***/
	String paginaSeleccionada = "";

	String totalRegistros = "";

	String registrosPorPagina = "";

	Vector resultado = null;
	
	if (datosPaginador != null) {

		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");

				PaginadorBind paginador = (PaginadorBind) datosPaginador.get("paginador");
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
	
	//.....
	
	//Vector resultado = (Vector)request.getAttribute("resultado");
	int nRegistros = 0;
	if(resultado!=null)nRegistros=Integer.parseInt(totalRegistros);
	
	MantenimientoDuplicadosForm form = (MantenimientoDuplicadosForm)request.getAttribute("MantenimientoDuplicadosForm");
	
	String nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2,censo.resultadoDuplicados.institucion,censo.resultadoDuplicados.numeroColegiado,";
	String tamanoCol="3,10,10,18,18,18,10,5,3";
	boolean ocultarColegiaciones=true;
	if(form!=null){
		ocultarColegiaciones = form.getAgruparColegiaciones()!=null&&form.getAgruparColegiaciones().equalsIgnoreCase("s");
	}
	if(ocultarColegiaciones){
		nombreCol="&nbsp;,censo.resultadoDuplicados.identificador,censo.resultadoDuplicados.nif,censo.resultadoDuplicados.nombre,censo.resultadoDuplicados.apellido1,censo.resultadoDuplicados.apellido2, Número de colegiaciones,";
		tamanoCol="3,10,10,17,19,18,9,10";
	}
%>

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>



<!-- HEAD -->


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
			
			ObjArray.splice($.inArray(obj.value, ObjArray),1);
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
		if(document.getElementById('registrosSeleccionadosPaginador')) 
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
	}

	function cargarChecks(){
		<%if (registrosSeleccionados != null) {
					for (int p = 0; p < registrosSeleccionados.size(); p++) {
						Hashtable clavesEJG = (Hashtable) registrosSeleccionados.get(p);
						valorCheckPersona = (String) clavesEJG.get("CLAVE");%>
						ObjArray.push('<%=valorCheckPersona%>');
						<%}
		}%>
		ObjArray.toString();
		seleccionados1=ObjArray;
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		document.getElementById('seleccionados').value =ObjArray.length;
		if(document.getElementById('registrosSeleccionadosPaginador')) {
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		}		
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
		if(document.getElementById('registrosSeleccionadosPaginador')) 
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
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
		document.forms[0].target="resultado";
		document.forms[0].submit();
	}

	function accionExportar(){
		
		if(<%=nRegistros<100%>||confirm("Hay muchos registros. El proceso de exportación puede tardar varios minutos.")){
			//sub();
			//seleccionarTodo();
			document.getElementById('seleccionados').value=<%=totalRegistros%>;
			document.forms[0].modo.value="export";
			document.forms[0].target="new";
			document.forms[0].submit();
			document.getElementById('seleccionados').value=0;
			//desmarcar();
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
	
	function informacionLetrado(fila) {
		document.forms[0].filaSelD.value = fila;					
	    var idInst = <%=idInstitucionLocation%>;			   				   	
	   	var auxPers = 'oculto' + fila + '_1';
	    var idPers = document.getElementById(auxPers);
		document.forms[0].tablaDatosDinamicosD.value=idPers.value + ',' + idInst + ',LETRADO' + '%';		
		document.forms[0].modo.value="editar";
		var verLetradoAux = 'oculto' + fila + '_4';
	    var verLetrado = document.getElementById(verLetradoAux);			    
		document.forms[0].verFichaLetrado.value=verLetrado.value;
	   	document.forms[0].submit();			   	
	}
	
	</script>

</head>

<body class="tablaCentralCampos"  onload="cargarChecks()" >

<html:form action="/CEN_MantenimientoDuplicados.do?noReset=true" method="POST" target="mainWorkArea">
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
	<input type="hidden" id="verFichaLetrado"  name="verFichaLetrado" value="">
</html:form>
	
	<table class="tablaTitulo" align="center">
		<tr>
		<td class="titulitos">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.resultados" />
		</td>
		</tr>
	</table>
	
	<siga:Table 
	   name="tablaDatos"
	   border="1"
	   columnNames="<%=nombreCol %>"
	   columnSizes="<%=tamanoCol %>">
		   
		<%if (resultado == null || resultado.size() == 0) {%>
			<tr class="notFound">
				<td class="titulitos">
					<siga:Idioma key="messages.noRecordFound" />
				</td>
			</tr>
		<%}else{%>
			<%String identificador="", nif="", nombre="", apellido1="",apellido2="", institucion="",nColegiado="", inst="", valorCheck="", abrev="", colegiaciones="", nocolegiadoCGAE="";%>
			<%Row fila; 
			  Hashtable registro;
			  String numFila;
			  String botones;%>
			<% for(int i=0;i<resultado.size();i++){ 
				numFila = String.valueOf(i);
				//fila = (Row) resultado.elementAt(i);
				//registro = fila.getRow();
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
				nocolegiadoCGAE=""+(String)registro.get("NOCOLEGIADOCGAE");
				
				FilaExtElement[] elems = null;
				
				if (colegiaciones.equals("0")) //si no es colegiado
					if (nocolegiadoCGAE.equals("0")) //si no es NoColegiado en CGAE
						botones = "";
					else //si es NoColegiado en CGAE
						botones = "C,E";
				else{ //si es colegiado
					botones = ""; 
					elems = new FilaExtElement[1];
					elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
				}
				
				
				String cont = new Integer(i + 1).toString();
				%>
			
			<siga:FilaConIconos fila="<%=cont %>" botones="<%=botones %>" visibleBorrado="no"
				clase="listaNonEdit" elementos="<%=elems%>"  pintarEspacio="no"
				modo="edicion">
				<td><%
 					boolean isChecked = false;
					for (int z = 0; z < registrosSeleccionados.size(); z++) {
							Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
						String clave = (String) clavesRegistro.get("CLAVE");
						if (valorCheck.toUpperCase().equals(clave.toUpperCase())) {
							isChecked = true;
							break;
						}
					}
 					
					if (isChecked) {
					 %> <input type="checkbox" value="<%=valorCheck%>" name="chkPersona" checked onclick="pulsarCheck(this);" > <%
					 	} else {
					 %> <input type="checkbox" value="<%=valorCheck%>" name="chkPersona" onclick="pulsarCheck(this);" > <%
							 	}
					 %></td>
				<td><!-- campos hidden --> 
					<input type="hidden" name="oculto<%=cont%>_1" id="oculto<%=cont%>_1" value="<%=identificador%>"/>
					<input type="hidden" name="oculto<%=cont%>_4" id="oculto<%=cont%>_4" value="1"/>
				<%=identificador%>&nbsp;</td>
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
				</siga:FilaConIconos>
			
			<%}%>
		<%}%>
		</siga:Table>
		
		<!-- aalg:para añadir la paginación -->
		<!-- FIN: LISTA DE VALORES -->
		<%
			if (datosPaginador != null && datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
				String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));
		%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>"
			registrosPorPagina="<%=registrosPorPagina%>"
			paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
			registrosSeleccionados="<%=regSeleccionados%>"
			modo="buscarPor" clase="paginator"
			divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
			
			distanciaPaginas="" action="<%=action%>" 
			
			/>
		<%
			} 
		%>

		
	<%if (resultado == null || resultado.size() == 0) {%>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
			<td class="tdBotones">
				
			</td>
		</tr>
	</table>
	<%} else {%>
	<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
		<tr>
		
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

	 
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"/>
</body>
</html>

<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
