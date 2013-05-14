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
<%@ page import="com.siga.beans.CenSancionBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.censo.form.SancionesLetradoForm"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import = "com.siga.Utilidades.*"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Hashtable miHash = (Hashtable)ses.getAttribute("EJG");
	ses.removeAttribute("EJG");
	String modo = (String) request.getAttribute("modo");

   SancionesLetradoForm miform = (SancionesLetradoForm)request.getAttribute("miform");
	
	String estilo="";
	String estiloCombo="";
	String fechaHoy=UtilidadesBDAdm.getFechaBD("");
	boolean bReadOnly=false;
	
	
	ArrayList vSel = new ArrayList(); // 
	String anio= "", numero="", idTipoEJG = "", idEstadoPorEJG="", observaciones="", automatico="" ;	
	//ScsEstadoEJGBean resultado = new ScsEstadoEJGBean();
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
		
		
		//resultado = (ScsEstadoEJGBean)v.get(0);
		//vSel.add(resultado.getIdEstadoEJG().toString());	
		
		//fechaInicio=(String)resultado.getFechaInicio();
		fechaInicio=GstDate.getFormatedDateShort("",fechaInicio);
		

		//observaciones=(String)resultado.getObservaciones();
		//automatico=(String)resultado.getAutomatico();
		
		  if (automatico!=null && automatico.equals("1")){
		      estilo="boxConsulta";
		      estiloCombo="boxConsulta";
		      bReadOnly=true;
		  }
	}
	catch(Exception e){};
	String dato[] = {(String)usr.getLocation()};	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<html:javascript formName="SancionesLetradoForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>		
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
			<siga:Idioma key="gratuita.insertarArchivo.literal.insertarArchivo"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<fieldset>

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/CEN_SancionesLetrado" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Archivar"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property="nombreInstitucionBuscar" />
	<html:hidden property="tipoSancionBuscar" />
	<html:hidden property="refCGAE" />
	<html:hidden property="colegiadoBuscar" />
	<html:hidden property="chkRehabilitado" />
	<html:hidden property="mostrarTiposFechas" />			
	<html:hidden property="fechaInicioBuscar" />
	<html:hidden property="fechaFinBuscar" />
	<html:hidden property="mostrarSanciones" />					
	<html:hidden property="fechaInicioArchivada"/>
	<html:hidden property="fechaFinArchivada"/>
			
	
	

	<tr>		
	<td>	
	<!-- SUBCONJUNTO DE DATOS -->
	<table class="tablaCampos" align="center">
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.insertarArchivo.literal.fechaarchivo"/>&nbsp;(*)
	</td>
	<td>
		
		<%if (automatico!=null && !automatico.equals("1")){%>
		 	<siga:Datepicker nombreCampo="fechaArchivada" posicionX="30" posicionY="30"></siga:Datepicker>
		<%}else{%>
		<html:textarea property="fechaArchivada" styleclass="box" style="width:100;overflow:hidden" rows="1" value="<%=fechaInicio%>" readOnly="true"/>
		<%}%>
		
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
		<siga:ConjBotonesAccion botones="Y,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		
	//<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{
			sub();
			  document.forms[0].modo.value="Archivar";
		       	document.forms[0].submit();
		       	//fin();
				//return false;				
				//window.top.returnValue="buscarPor";
		       	window.top.returnValue="MODIFICADO";
		     
		       	//top.cierraConParametros("NORMAL");	
		
		}		
		
		//<!-- Asociada al boton Cerrar -->
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