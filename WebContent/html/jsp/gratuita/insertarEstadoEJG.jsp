<!DOCTYPE html>
<html>
<head>
<!-- insertarEstadoEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsEstadoEJGBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import = "com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	boolean esComision = usr.isComision();
	Hashtable miHash = (Hashtable)ses.getAttribute("EJG");
	ses.removeAttribute("EJG");
	String modo = (String) request.getAttribute("modo");
	String envioPericles = (String) request.getAttribute("envioPericles");
	
	String estilo="";
	String estiloCombo="";
	String fechaHoy=UtilidadesBDAdm.getFechaBD("");
	boolean bReadOnly=false;
	
	
	ArrayList vSel = new ArrayList(); // 
	String anio= "", numero="", idTipoEJG = "", idEstadoPorEJG="", observaciones="", automatico="", idInstitucion="" ;	
	ScsEstadoEJGBean resultado = new ScsEstadoEJGBean();
	Vector v=(Vector)request.getAttribute("resultado");
	String fechaInicio=fechaHoy;
	estilo="box";
	estiloCombo="boxCombo";
	bReadOnly=false;
	try {
	
		anio = miHash.get("ANIO").toString();
		numero = miHash.get("NUMERO").toString();
		idTipoEJG = miHash.get("IDTIPOEJG").toString();
		idInstitucion =miHash.get("IDINSTITUCION").toString();
		idEstadoPorEJG=miHash.get("IDESTADOPOREJG").toString();
		
		
		resultado = (ScsEstadoEJGBean)v.get(0);
		vSel.add(resultado.getIdEstadoEJG().toString());	
		
		fechaInicio=(String)resultado.getFechaInicio();
		fechaInicio=GstDate.getFormatedDateShort("",fechaInicio);

		observaciones=(String)resultado.getObservaciones();
		automatico=(String)resultado.getAutomatico();
		
		  if (modo.equalsIgnoreCase("consulta")){
	      estilo="boxConsulta";
	      estiloCombo="boxComboConsulta";
	      bReadOnly=true;
		  }
	}
	catch(Exception e){
		//System.out.println("Error"+e.toString());
		
	};
	String dato[] = {idInstitucion};	
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="DefinirEstadosEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>
<input type="hidden" id ="envioPericles" value = "${envioPericles}"/>
	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
	<td class="titulitosDatos">	
	 <%if (modo!=null && modo.equals("editar")){%>
		<siga:Idioma key="gratuita.insertarEstado.literal.modificarEstado"/>
	<%}else{%>	
	    <siga:Idioma key="gratuita.insertarEstado.literal.insertarEstado"/>
	<%}%>	
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->


	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
		
	
	<html:form action="/JGR_EstadosEJG" method="POST" target="submitArea">
		<html:hidden property = "modo" value = "Insertar"/>
		<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
		<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
		<html:hidden property = "anio" value ="<%=anio%>"/>
		<html:hidden property = "numero" value ="<%=numero%>"/>
		<html:hidden property = "idEstadoPorEJG" value ="<%=idEstadoPorEJG%>"/>
		<html:hidden property = "automatico" value ="<%=automatico%>"/>
	<table class="tablaCentralCamposPeque" align="center">
		
		<tr>
			<td width="27%"></td>
			<td width="70%"></td>
			<td width="3%"></td>	
		</tr>
		
		<tr>		
			
		<!-- SUBCONJUNTO DE DATOS -->
		
			<td class="labelText">
				<siga:Idioma key="gratuita.operarEJG.literal.fecha"/>&nbsp;(*)
			</td>
			<td>		
				<siga:Fecha nombreCampo="fechaInicio" valorInicial="<%=fechaInicio%>" disabled="<%=String.valueOf(bReadOnly)%>"></siga:Fecha>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="pestana.justiciagratuitaejg.estados"/>&nbsp;(*)
			</td>
			<td>
			<%String readOnly = "true";
			  if (automatico!=null && !automatico.equals("1"))
				readOnly = "false"; 
			%>
			<%if(bReadOnly){ %>
				<siga:ComboBD nombre="idEstadoEJG" tipo="estadosEJG" clase="<%=estiloCombo%>"  ancho="300" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vSel%>" readonly="true"/>
			<%}else{ %>
				<% if(esComision){%>
						<siga:ComboBD nombre="idEstadoEJG" tipo="estadosEJGComision" clase="<%=estiloCombo%>"  ancho="300" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vSel%>" readonly="<%=readOnly%>"/>
				<% }else{ %>
						<siga:ComboBD nombre="idEstadoEJG" tipo="estadosEJG" clase="<%=estiloCombo%>"  ancho="300" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vSel%>" readonly="<%=readOnly%>"/>
				<% } %>
			<%} %>
			
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="pestana.justiciagratuitaejg.observaciones"/>
			</td>
			<td> 
				<html:textarea cols="60" rows="8" property="observaciones" onkeydown="cuenta(this,4000)" onchange="cuenta(this,4000)"  styleClass="<%=estilo%>" value="<%=observaciones%>" ></html:textarea> 
			
			</td>
			<td>&nbsp;</td>
		</tr>
		
		<%if (!modo.equalsIgnoreCase("consulta")){%>
			<%if (automatico!=null && automatico.equals("1")){%>
			<tr>
		  		<td class="labelText" colspan="3"  >
				<siga:Idioma key="gratuita.maestro.campoBloqueo.nota"/>
		  		</td>
			</tr>
		<%}%>
	<%}%>		
				
	</table>
	</html:form>
	
	
	
	
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
	<%if (bReadOnly){ %>
		<siga:ConjBotonesAccion botones="C" modal="P"  />
	<%}else{%>
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<%}%>
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		function accionGuardarCerrar() 
		{
			
			
			sub();
			if(document.getElementById('envioPericles') && document.getElementById('envioPericles').value=='true' && document.forms[0].idEstadoEJG.value =='7'){
				if(!confirm('Se va a enviar el expediente, la documentación y los expedientes económicos solicitados a la CAJG. ¿Deséa continuar?')){
					fin();
					return;
				}
			}
			if (validateDefinirEstadosEJGForm(document.forms[0])){
			  <%if (modo!=null && modo.equals("editar")){%>
			    document.forms[0].modo.value="Modificar";
			  <%}else{%>
			  document.forms[0].modo.value="Insertar";
			  <%}%>
		       	document.forms[0].submit();
				window.top.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			
			}
		}		
		
	
		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}	
		</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>