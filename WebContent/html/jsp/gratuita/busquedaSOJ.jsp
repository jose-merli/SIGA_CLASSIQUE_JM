<!-- busquedaSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsSOJBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.censo.action.*"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	String anio="", numSOJ="", numero="", fechaAperturaDesde="", fechaAperturaHasta="", estadoSOJ="", busquedaRealizada="", nif="", nombre="", apellido1="", apellido2="", idPersona="";

	String nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
	String nombreColegiado =  request.getAttribute("nombreColegiado")==null?"":(String)request.getAttribute("nombreColegiado");

	Hashtable miHash = (Hashtable)ses.getAttribute("DATOSFORMULARIO");
	ses.removeAttribute("DATOSFORMULARIO");

	anio = UtilidadesBDAdm.getYearBD("");
	
	try {
		busquedaRealizada = miHash.get("BUSQUEDAREALIZADA").toString();
		anio = miHash.get(ScsSOJBean.C_ANIO).toString();
		numSOJ = miHash.get(ScsSOJBean.C_NUMSOJ).toString();
		numero = miHash.get(ScsSOJBean.C_NUMERO).toString();		
		fechaAperturaDesde = miHash.get("FECHAAPERTURADESDE").toString();
		fechaAperturaHasta = miHash.get("FECHAAPERTURAHASTA").toString();
		estadoSOJ = miHash.get(ScsSOJBean.C_ESTADO).toString();
		idPersona = miHash.get(ScsSOJBean.C_IDPERSONA).toString();	
		nif = miHash.get(ScsPersonaJGBean.C_NIF).toString();	
		nombre = miHash.get(ScsPersonaJGBean.C_NOMBRE).toString();	
		apellido1 = miHash.get(ScsPersonaJGBean.C_APELLIDO1).toString();	
		apellido2 = miHash.get(ScsPersonaJGBean.C_APELLIDO2).toString();	
	}catch (Exception e){};
	
	ArrayList idTurno = new ArrayList(), idGuardia = new ArrayList(), idTipoSOJ = new ArrayList(), idTipoSOJColegio = new ArrayList();
	try {
		idTurno.add(miHash.get(ScsSOJBean.C_IDTURNO).toString());
		idGuardia.add(miHash.get(ScsSOJBean.C_IDGUARDIA).toString());
		idTipoSOJ.add(miHash.get(ScsSOJBean.C_IDTIPOSOJ).toString());
		idTipoSOJColegio.add(miHash.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString());
	} catch (Exception e) {};
	
	String dato[] = {(String)usr.getLocation()};
	
%>

<html>

<!-- HEAD -->
<head>
	<html:javascript formName="DefinirSOJForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	
	<script type="text/javascript">	
		function refrescarLocal(){
			//parent.buscar();
			buscar();
		}		
/*		function buscarCliente ()
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			var colegiado = document.getElementById('ncolegiado');
			if (resultado != null && resultado[2]!=null)
			{
				document.forms[1].idPersona.value=resultado[2];
			}
		}
		
*/		
		function inicio(){
		<% 
		
		 if (busquedaRealizada.equals("1")) {%>
		      buscarPaginador();
		<%}%>
		}
	</script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt 
		titulo="gratuita.busquedaSOJ.literal.expedientesSOJ"
		localizacion="gratuita.busquedaSOJ.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	

</head>

<body onLoad="ajusteAlto('resultado');inicio();" >


	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type=""  style="display:none">	
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<html:form action="/JGR_ExpedientesSOJ.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
	<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "idPersona" value = ""/>
	<html:hidden property = "numero" value = ""/>
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
	<html:hidden property="seleccionarTodos" />

	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.expedientesSOJ">
	<table  align="center" width="100%" border="0">  		
	<tr>	
	<td class="labelText"  width="100" >
		<siga:Idioma key="gratuita.busquedaSOJ.literal.anyo"/> / <siga:Idioma key="gratuita.busquedaSOJ.literal.codigo"/>	
	</td>
	<td colspan="3">		
		<html:text name="DefinirSOJForm" property="anio" size="4" maxlength="4" styleClass="box"  value="<%=anio%>"></html:text> / <html:text name="DefinirSOJForm" property="numSOJ" size="5" maxlength="10" styleClass="box"  value="<%=numSOJ%>"></html:text>
	</td>
	<td class="labelText" width="160">		
		<siga:Idioma key="gratuita.busquedaSOJ.literal.fechaApertura"/>&nbsp;<siga:Idioma key="gratuita.busquedaSOJ.literal.desde"/>
	</td>	
		
	<td class="labelText" colspan="3" style="text-align: left">
		<siga:Fecha nombreCampo="fechaAperturaSOJDesde" valorInicial="<%=fechaAperturaDesde%>"></siga:Fecha>
		<siga:Idioma key="gratuita.busquedaSOJ.literal.hasta"/>
		<siga:Fecha nombreCampo="fechaAperturaSOJHasta" valorInicial="<%=fechaAperturaHasta%>"></siga:Fecha>
	</td>
	</tr>
	<tr>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.estadoSOJ"/>
	</td>
	<td>		
		<select name="estadoSOJ" class="box">
			<option value=""></option>
			<option value="A" <%if (estadoSOJ.startsWith("A")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.abierto"/></option>
			<option value="P" <%if (estadoSOJ.startsWith("P")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.pendiente"/></option>
			<option value="C" <%if (estadoSOJ.startsWith("C")) {%>selected<%}%>><siga:Idioma key="gratuita.SOJ.estado.cerrado"/></option>
		</select>
	</td>
	<td class="labelText" width="80">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.tipoSOJ"/>
	</td>	
	<td>	
		<siga:ComboBD nombre="idTipoSOJ" tipo="tipoSOJ" clase="boxCombo" obligatorio="false" elementoSel="<%=idTipoSOJ%>"/>
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.SOJColegio"/>
	</td>
	<td class="labelText" colspan="3">
		<siga:ComboBD nombre="idTipoSOJColegio" tipo="tipoSOJColegio" clase="boxCombo" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=idTipoSOJColegio%>"/>
	</td>	
	</tr>
	
	<tr>
		<td class="labelText">
			<siga:Idioma key="gratuita.busquedaSOJ.literal.turno"/>
		</td>
		<td colspan="3">
			<siga:ComboBD nombre = "idTurno" tipo="turnos"  ancho="350"  clase="boxCombo" obligatorio="false" accion="Hijo:idGuardia" parametro="<%=dato%>" elementoSel="<%=idTurno%>"/>
		</td>
		<td class="labelText">	
			<siga:Idioma key="gratuita.busquedaSOJ.literal.guardia"/>
		</td>
		<td class="labelText" colspan="3">
			<siga:ComboBD nombre = "idGuardia" tipo="guardias"  ancho="250" clase="boxCombo" obligatorio="false" hijo="t" elementoSel="<%=idGuardia%>"/>
		</td>	
	
	
	</tr>
	</table>

	<siga:BusquedaPersona tipo="colegiado" titulo="gratuita.seleccionColegiadoJG.literal.titulo" idPersona="idPersona">
	</siga:BusquedaPersona>

	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="gratuita.busquedaSOJ.literal.solicitante">
	<table  align="center" width="100%">
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.nif"/>		
	</td>
	<td class="labelText">
		<html:text name="DefinirSOJForm" property="nif" size="10" maxlength="20" styleClass="box"  value="<%=nif%>"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.nombre"/>
	</td>
	<td class="labelText">
		<html:text name="DefinirSOJForm" property="nombre" size="15" maxlength="100" styleClass="box"  value="<%=nombre%>"></html:text>
	</td>
	<td class="labelText">	
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido1"/>
	</td>		
	<td class="labelText">
		<html:text name="DefinirSOJForm" property="apellido1" size="15" maxlength="100" styleClass="box"  value="<%=apellido1%>"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="gratuita.busquedaSOJ.literal.apellido2"/>
	</td>
	<td class="labelText"  colspan="2">
		<html:text name="DefinirSOJForm" property="apellido2" size="15" maxlength="100" styleClass="box"  value="<%=apellido2%>"></html:text>
	</td>	
	</tr>	
	</table>
	</siga:ConjCampos>
	</html:form>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="C,N,B" titulo="gratuita.busquedaSOJ.literal.expedientesSOJ" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscarPaginador() 
		{
//			if (validateDefinirSOJForm(document.forms[1])){
		    	document.forms[1].modo.value = "buscarPor";
				document.forms[1].submit();
//			}		
		}
		function buscar() 
		{
//			if (validateDefinirSOJForm(document.forms[1])){
			sub();
			if (isNaN(document.forms[1].anio.value)) {
				
				alert('<siga:Idioma key="gratuita.busquedaEJG.literal.errorAnio"/>');
				fin();
				return false;
			}

			if ( !validarObjetoAnio(document.getElementById("anio")) ){
				alert("<siga:Idioma key="fecha.error.anio"/>");
				return false;
			}
			
		    	document.forms[1].modo.value = "buscarInit";
				document.forms[1].submit();
				
//			}		
		}			
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[1].reset();
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{
			document.forms[1].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[1].name,"M");
//			if(resultado=='MODIFICADO') buscar();
			if(resultado && resultado[0]=="MODIFICADO"){
				with(document.DefinirSOJForm){
					numero.value        = resultado[1];
					idTipoSOJ.value     = resultado[2];
					idInstitucion.value = resultado[3];
					anio.value          = resultado[4];
					modo.value          = "Editar";
					target				= "mainWorkArea";
			   		submit();
				}
			}
		}
		function seleccionarTodos(pagina) 
		{
				document.forms[1].seleccionarTodos.value = pagina;
				buscar('buscarPor');
				
		}		
		function generarCarta() {
   			if(window.frames.resultado.ObjArray){
 				window.frames.resultado.accionGenerarCarta();
			}
			else {
				alert("<siga:Idioma key='general.message.seleccionar'/>");
				fin();
			}	
		} 	
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>