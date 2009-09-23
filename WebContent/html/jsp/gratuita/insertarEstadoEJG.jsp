<!-- insertarEstadoEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	Hashtable miHash = (Hashtable)ses.getAttribute("EJG");
	ses.removeAttribute("EJG");
	String modo = (String) request.getAttribute("modo");
	String estilo="";
	String estiloCombo="";
	String fechaHoy=UtilidadesBDAdm.getFechaBD("");
	boolean bReadOnly=false;
	
	
	ArrayList vSel = new ArrayList(); // 
	String anio= "", numero="", idTipoEJG = "", idEstadoPorEJG="", observaciones="", automatico="" ;	
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
		idEstadoPorEJG=miHash.get("IDESTADOPOREJG").toString();
		
		
		resultado = (ScsEstadoEJGBean)v.get(0);
		vSel.add(resultado.getIdEstadoEJG().toString());	
		
		fechaInicio=(String)resultado.getFechaInicio();
		fechaInicio=GstDate.getFormatedDateShort("",fechaInicio);
		

		observaciones=(String)resultado.getObservaciones();
		automatico=(String)resultado.getAutomatico();
		
		  if (automatico!=null && automatico.equals("1")){
	      estilo="boxConsulta";
	      estiloCombo="boxConsulta";
	      bReadOnly=true;
		  }
		   
		  /*}
	}else{
	 estilo="box";
	 estiloCombo="boxCombo";
	 bReadOnly=false;
	 
	}*/
		
		
		
	}
	catch(Exception e){};
	String dato[] = {(String)usr.getLocation()};	
%>

<html>
<!-- HEAD -->
<head>
	<html:javascript formName="DefinirEstadosEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
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
	<fieldset>

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_EstadosEJG" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idTipoEJG" value ="<%=idTipoEJG%>"/>
	<html:hidden property = "anio" value ="<%=anio%>"/>
	<html:hidden property = "numero" value ="<%=numero%>"/>
	<html:hidden property = "idEstadoPorEJG" value ="<%=idEstadoPorEJG%>"/>
	<html:hidden property = "automatico" value ="<%=automatico%>"/>
	

	<tr>		
	<td>	
	<!-- SUBCONJUNTO DE DATOS -->
	<table class="tablaCampos" align="center">
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.operarEJG.literal.fecha"/>&nbsp;(*)
	</td>
	<td>
		<html:text name="DefinirEstadosEJGForm" property="fechaInicio" size="10" styleClass="<%=estilo%>" value="<%=fechaInicio%>" readonly="<%=bReadOnly%>"></html:text>&nbsp;&nbsp;
		<%if (automatico!=null && !automatico.equals("1")){%>
		<a onClick="return showCalendarGeneral(fechaInicio);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
		<%}%>
		
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="pestana.justiciagratuitaejg.estados"/>&nbsp;(*)
	</td>
	<td>
	
	<%if (automatico!=null && !automatico.equals("1")){%>
		<siga:ComboBD nombre="idEstadoEJG" tipo="estadosEJG" clase="<%=estiloCombo%>"  ancho="300" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vSel%>" readonly="false"/>
	<%}else{%>
	   <siga:ComboBD nombre="idEstadoEJG" tipo="estadosEJG" clase="<%=estiloCombo%>"  ancho="300" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" elementoSel="<%=vSel%>" readonly="true"/>
	<%}%>	
	
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="pestana.justiciagratuitaejg.observaciones"/>
	</td>
	<td colspan="3">
		<html:textarea cols="60" rows="8" property="observaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"  styleclass="box" value="<%=observaciones%>" ></html:textarea> 
	
	</td>
	</tr>		
	
	</table>
	</td>
	</tr>
	</html:form>
	</table>
	</fieldset>
	<table align="left">
	
	<%if (automatico!=null && automatico.equals("1")){%>
	<tr  >
	  <td class="labelText" colspan="6"  >
		<siga:Idioma key="gratuita.maestro.campoBloqueo.nota"/>
	  </td>
	</tr>
	<%}%>	
	</table>
	
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
	<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{
			sub();
			if (validateDefinirEstadosEJGForm(document.forms[0])){
			  <%if (modo!=null && modo.equals("editar")){%>
			    document.forms[0].modo.value="Modificar";
			  <%}%>
		       	document.forms[0].submit();
				window.returnValue="MODIFICADO";
			}else{
				fin();
				return false;
			
			}
		}		
		
		<!-- Asociada al boton Cerrar -->
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