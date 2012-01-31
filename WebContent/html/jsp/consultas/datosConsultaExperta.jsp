<!-- datosConsultaExperta.jsp -->
<!-- 
	 VERSIONES:
	 Pilar.Duran 01/05/2007
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	String app_imagen = app+"/html/imagenes/";
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");	
	String estiloCaja = "";
	
	String errorDescripcion = UtilidadesString.getMensajeIdioma(user,"messages.consultas.error.descripcion");

	ArrayList moduloSel = new ArrayList();
	String idModulo = (String)request.getAttribute("idModulo");
	String sentenciaSelect = (String)request.getAttribute("sentenciaSelect");
	if (idModulo!=null && !idModulo.equals("")){
		moduloSel.add(idModulo);
	}
	
	String editable = (String)request.getParameter("editable");		
	String idInstitucion = (String)request.getParameter("idInstitucion");
	editable = idInstitucion.equals(user.getLocation())?editable:"0";	
	boolean bEditable = editable.equals("1")?true:false;
	String botones = bEditable?"V,G,GE":"V";
	String boxStyle = bEditable?"box":"boxConsulta";
	String accion = (String)request.getParameter("accion");
	boolean breadonly = false;
	
	String cgae = (String)request.getAttribute("esCGAE");
	boolean esCGAE = (cgae!=null && cgae.equals("true"))?true:false;	
	
	String tipoConsultaGeneral = ConConsultaAdm.TIPO_CONSULTA_GEN;
	String tipoConsultaEnvios = ConConsultaAdm.TIPO_CONSULTA_ENV;
	String tipoConsultaFact = ConConsultaAdm.TIPO_CONSULTA_FAC;
	
	String parametros = "";
	
	String noReset = !accion.equals("nuevo")?"noReset=true":"";
	parametros = noReset.equals("")?"":"?"+noReset;

	String tipoConsulta = (String)request.getAttribute("tipoConsulta");
	String sTipoConsultaCombo = tipoConsulta;
	tipoConsulta=!tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_GEN)?"tipoConsulta=listas":"";
	if (!tipoConsulta.equals("")){
		if (parametros.equals("")){
			parametros="?"+tipoConsulta;
		}else{
			parametros+="&"+tipoConsulta;
		}
	}
	
	String buscar = (String)request.getParameter("buscar");
	buscar = buscar!=null?"buscar=true":"";
	if (!buscar.equals("")){
		if (parametros.equals("")){
			parametros="?"+buscar;
		}else{
			parametros+="&"+buscar;
		}
	}
	
	
	
	
	//Calculo los values d elos simbolos esta vacio SQL:
	ConOperacionConsultaAdm opConsulta = new ConOperacionConsultaAdm(user);
	String valuesEstaVacio = opConsulta.getvaluesSimboloEstaIgual();

	String tipoConsultaParaElTitulo = (String)request.getAttribute("tipoConsulta");
	String help = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsultaexperta.literal.help");

	//2009-CGAE-119-INC-CAT-035
	//Como hay que comprobar varios valores se usa una expresión regular para poder emplear el tag "notMatch" y
	//no tener que recurrir a un if comprobando los valores
	String tipoConsultaPattern = "["+ConConsultaAdm.TIPO_CONSULTA_PYS+ConConsultaAdm.TIPO_CONSULTA_ENV+ConConsultaAdm.TIPO_CONSULTA_FAC+"]";
%>	


<html>

<!-- HEAD -->
	<head>
	
		<link ="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- Validaciones en Cliente -->
		<html:javascript formName="EditarConsultaForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		  //Funcion que dice si valor es uno de los simbolos EstaVacio:	
		 		  
		  function trim(s) {
			  while (s.substring(0,1) == ' ') {
			    s = s.substring(1,s.length);
			  }
			  while (s.substring(s.length-1,s.length) == ' ') {
			    s = s.substring(0,s.length-1);
			  }
			  return s;
		  }
		  
		 	
		function refrescarLocal()
		{			
			document.location.reload();			
		}


		<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			if(parent.document.getElementById("accionAnterior")&&parent.document.getElementById("accionAnterior").value!=""){

				document.forms[1].accionAnterior.value=parent.document.getElementById("accionAnterior").value;
				document.forms[1].idModulo.value=parent.document.getElementById("idModulo").value;
				document.forms[1].modo.value="inicio";
			}else{
				document.forms[1].modo.value="abrir";
			}
			document.forms[1].action=document.forms[1].action+"<%=parametros%>";
			document.forms[1].submit();				
		}
		
		
		
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() 
		{		//Pongo la inhabilitacion de los botones dentro de este metodo
				guardarListas();
			
		}
		
		function guardarListas() 
		{	
			sub();
			if (document.forms[0].descripcion==null || document.forms[0].descripcion.value==""){
				alert('<%=errorDescripcion%>');
				fin();
				return false;
			}else{
		
				if (validateEditarConsultaForm(document.EditarConsultaForm)){	
					<%if (accion.equals("nuevo")){%>
						document.forms[0].modo.value="insertarSolo";
					<%}else{%>
						document.forms[0].modo.value="modificarSolo";					
					<%}%>
					document.forms[0].submit();
				}else{
					fin();
					return false;
				}
			}
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardarEjecutar() 
		{	
			//Pongo la inhabilitacion de los botones dentro de este metodo
				guardarEjecutarListas();
			
		}
		
		function comprobarIgualdadDeListas (lista1, lista2) 
		{		
			if (lista2.length == 0) 			return true;
			if (lista1.length != lista2.length) return false;
			
			v1 = lista1.options;
			v2 = lista2.options;
						
			for (i = 0; i < v1.length; i++) {
			
				o1  = v1[i].value;
				id1 = (o1.split("#"))[1];

				j = 0;
				for (j = 0; j < v2.length; j++) {
					o2 = v2[j].value;
					id2 = (o2.split("#"))[1];
					
					if (id1 == id2)
						break;
				}
				if (j >= v2.length) return false;
			}
			return true;
		}
		
		function guardarEjecutarListas() 
		{	
			sub();
			if (document.forms[0].descripcion==null || document.forms[0].descripcion.value==""){
				alert('<%=errorDescripcion%>');
				fin();
				return false;
			}else{
			  if (validateEditarConsultaForm(document.EditarConsultaForm)){	
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertarEjecutar";
				<%}else{%>
					document.forms[0].modo.value="modificarEjecutar";					
				<%}%>
				document.forms[0].submit();
			  }else{
			 	 fin();
				return false;
			  }
			}
		}
					
		
		
		
		
		
		function editarCriterio(ref){
			if (ref.selectedIndex>-1){
				criterio = ref.options[ref.selectedIndex];
				document.forms[0].criterioModif.value=criterio.value;
				document.forms[0].modo.value="cambiarCriterio";
				var resultado=ventaModalGeneral(document.forms[0].name,"M");
				if (resultado!=undefined){
					ref.options[ref.selectedIndex].text=resultado[0];
					ref.options[ref.selectedIndex].value=resultado[1];
				}
			}
			
		}
		function abrirAyuda() {
				
		
				var alto = 580, ancho = 800;
					  
		 		var posX = (screen.width - ancho) / 2;
     		var posY = (screen.height - alto) / 2;
     		var medidasWin = "height=" + alto + ", width=" + ancho + ", top=" + posY + ", left=" + posX;
 			
				w = window.open ("<%=app%>/html/jsp/consultas/paginaAyuda.jsp",
						   				   "", 
										     "status=0, toolbar=0, location=0, menubar=0, resizable=1," + medidasWin);
		}	
		
		/*
		 * Comprueba que existen los campos requeridos para los tipos de consulta
		 * de envío y facturación. Si no existen los crea
		 */
		function parsearSelect() 
		{	
			var lista = "";
			var tipoConsulta = document.getElementById("tipoConsulta");
			if (tipoConsulta.options[tipoConsulta.selectedIndex].value == 'E'){
				lista = 
			    '\n   SELECT CEN_CLIENTE.IDINSTITUCION AS "IDINSTITUCION",\n' +
		        '          CEN_CLIENTE.IDPERSONA AS "IDPERSONA",\n'  +
		        '          CEN_DIRECCIONES.CODIGOPOSTAL AS "CODIGOPOSTAL",\n' +
		        '          CEN_DIRECCIONES.CORREOELECTRONICO AS "CORREOELECTRONICO",\n' +
		        '          CEN_DIRECCIONES.DOMICILIO AS "DOMICILIO",\n' +
		        '          CEN_DIRECCIONES.MOVIL AS "MOVIL",\n' +
		        '          CEN_DIRECCIONES.FAX1 AS "FAX1",\n' +
		        '          CEN_DIRECCIONES.FAX2 AS "FAX2",\n' +
		        '          CEN_DIRECCIONES.IDPAIS AS "IDPAIS",\n' +
		        '          CEN_DIRECCIONES.IDPROVINCIA AS "IDPROVINCIA",\n' +
		        '          CEN_DIRECCIONES.IDPOBLACION AS "IDPOBLACION"\n';
			}
			else if (tipoConsulta.options[tipoConsulta.selectedIndex].value == 'F'){
				lista =  
				'\n   SELECT CEN_CLIENTE.IDINSTITUCION AS "IDINSTITUCION",\n' +
			    '          CEN_CLIENTE.IDPERSONA AS "IDPERSONA"\n';	
			}

			if (lista != ""){
				var textArea = document.getElementsByName("selectExperta")[0];
				var texto = textArea.value;
				//Obtiene el contenido del tag <SELECT>
				var ini = texto.indexOf("<SELECT>") + 8;
				var fin = texto.indexOf("</SELECT>");

				//compone de nuevo el texto
				texto = texto.substring(fin,texto.length);
				texto = "<SELECT>" + lista + texto;
				textArea.value = texto;
			}
		}

				
	</script>
	<!-- FIN: SCRIPTS BOTONES -->	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	
<% 	if (accion!=null && accion.equalsIgnoreCase("NUEVO")) { %>

		<siga:TituloExt titulo="consultas.consultasRecuperar.consulta.cabecera"  localizacion="consultas.nuevaConsultaExperta.localizacion"/>
		
	
	
	<!-- FIN: TITULO Y LOCALIZACION -->	
<% } else { %>
	<siga:TituloExt 
		titulo="consultas.consultasRecuperar.consulta.cabecera" 
		localizacion="consultas.consultasRecuperar.editar.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
<% } %>
	</head>

	<body>
	
		<html:form action="/CON_EditarConsulta.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>	
			<html:hidden property = "idConsulta" value=""/>	
			<html:hidden property = "tablas"/>
			<html:hidden property = "actionModal" value=""/>
			<html:hidden property = "criterioModif" value=""/>
			<html:hidden property = "esExperta" value="1"/>
			
			<input type="Hidden"  name="experta" value="1">

		
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos" width=120px>
					<siga:Idioma key="consultas.recuperarconsulta.literal.descripcion"/>&nbsp(*)
					&nbsp;
					<html:text name="EditarConsultaForm" property="descripcion" size="80" maxlength="100" styleClass="<%=boxStyle%>" readonly="<%=!bEditable%>"></html:text>
				</td>
			</tr>
		</table>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<table  class="tablaCentralCampos"  align="center">			 		
		
		<tr>				
		<td>	
		
		<table class="tablaCampos" align="center">
<logic:notMatch name="EditarConsultaForm" property="tipoConsulta" value="<%=tipoConsultaPattern%>">		
		<tr>
			<td class="labelText" width="100">
				<siga:Idioma key="consultas.recuperarconsulta.literal.modulo"/>&nbsp(*)
			</td>
			<td  width="350">
			<%if (bEditable){%>				
				<siga:ComboBD nombre = "modulo" tipo="cmbModuloConsulta" clase="boxCombo" obligatorio="false" pestana="t" ElementoSel="<%=moduloSel%>"/>
			<%}else{%>
				<siga:ComboBD nombre = "modulo" tipo="cmbModuloConsulta" clase="boxConsulta" readonly="true" obligatorio="false" pestana="t" ElementoSel="<%=moduloSel%>"/>
			<%}%>
			</td>
			<td class="labelText">
				<siga:Idioma key="consultas.recuperarconsulta.literal.tipoConsulta"/>
				&nbsp&nbsp
				<%if (bEditable && "nuevo".equals(accion)){%>				
					<select name="tipoConsulta" class="boxCombo" id="tipoConsulta" onChange=parsearSelect()>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>">			
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>"></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>" selected><siga:Idioma key="modulo.envios"/></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>"><siga:Idioma key="modulo.facturacion"/></option>
					</logic:equal>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>">			
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>"></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"><siga:Idioma key="modulo.envios"/></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>" selected><siga:Idioma key="modulo.facturacion"/></option>
					</logic:equal>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>" selected></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"><siga:Idioma key="modulo.envios"/></option>
						<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>"><siga:Idioma key="modulo.facturacion"/></option>
					</logic:equal>
					</select>
				<%}else{%>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>">			
						<span class="boxConsulta"><siga:Idioma key="modulo.envios"/></span>
						<html:hidden property = "tipoConsulta" value = "<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"/>
					</logic:equal>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>">
						<span class="boxConsulta"><siga:Idioma key="modulo.facturacion"/></span>
						<html:hidden property = "tipoConsulta" value = "<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>"/>
					</logic:equal>
					<logic:equal name="EditarConsultaForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">
						<html:hidden property = "tipoConsulta" value = "<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>"/>
					</logic:equal>
				<%}%>
			

			</td>
			<td class="labelText"  width="100">
				<siga:Idioma key="consultas.recuperarconsulta.literal.general"/>
			</td>
			<td  width="50">				
				<html:checkbox name="EditarConsultaForm" property="general" disabled="<%=(!bEditable||!esCGAE)%>"></html:checkbox>
			</td>													
			<td>				
				&nbsp;
			</td>													
		</tr>
</logic:notMatch>

		</table>

<%
String altura = "285px";
if (!bEditable){
	altura="345px";
}
%>


<logic:notMatch name="EditarConsultaForm" property="tipoConsulta" value="<%=tipoConsultaPattern%>">
		<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.consultaExperta">
		<table class="tablaCampos" align="center" >
	
		<tr>

			<td  align="center" width="93%">
		    	<html:textarea style="width:800px" rows="20" property="selectExperta"  styleclass="boxExpert" value="<%=sentenciaSelect%>"></html:textarea> 
		    </td>	
			<td align="left">
			   <a HREF="javascript:abrirAyuda();"><IMG border=0 src="<%=app_imagen%>help.gif"  alt="<%=help%>"></a>
			</td>	
		</tr>	
		</table>		
		</siga:ConjCampos>	
</logic:notMatch>

		
		</td>
		</tr>
		</table>
			
		</html:form>
		<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<html:form action="/CON_RecuperarConsultas.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "accionAnterior"/>
			<html:hidden property = "idModulo"/>
		</html:form>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>	
</html>
