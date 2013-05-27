<!-- operarCalendario.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ScsCalendarioLaboralBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	Hashtable miHash = (Hashtable) ses.getAttribute("elegido");	
	String accion = (String)ses.getAttribute("accion");
	String dato[] = {(String)usr.getLocation()};
	ses.removeAttribute("elegido");
	ses.removeAttribute("accion");
	
    String fechaFormateada = GstDate.getFormatedDateShort(usr.getLanguage(),(String)miHash.get(ScsCalendarioLaboralBean.C_FECHA));
    
    ArrayList vInt = new ArrayList();
	try {
		
		if (miHash.get(ScsCalendarioLaboralBean.C_IDPARTIDO) == "") {			
			vInt.add("0");
		}
		else {
			vInt.add((String)miHash.get(ScsCalendarioLaboralBean.C_IDPARTIDO));			
		}
	} catch (Exception e) {
		vInt.add("0");
	}
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	

	<html:javascript formName="CalendarioLaboralForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
	<tr>
	<td class="titulitosDatos">	
		<siga:Idioma key="gratuita.operarCalendario.literal.modificarCalendario"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->


	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/CalendarioLaboralAction.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "Modificar"/>
	<html:hidden property = "identificativo" value = "<%=(String)miHash.get(ScsCalendarioLaboralBean.C_IDENTIFICATIVO)%>"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaMod" value = "sysdate"/>

	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<!-- Ejemplo de conjunto de campos recuadrado y con titulo -->
	<siga:ConjCampos leyenda="gratuita.calendario.literal.calendario">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->
	<tr>
	
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.fecha"/>&nbsp;(*)
	</td>				
	<td>
		<siga:Fecha nombreCampo="fecha" posicionX="50" posicionY="50" valorInicial="<%=fechaFormateada%>"></siga:Fecha>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.fiestaLocalPartidoJudicial"/>
	</td>				
	<td>
		<siga:ComboBD nombre="idPartido" tipo="partidoJudicial" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" elementoSel="<%=vInt%>" obligatorio="false" parametro="<%=dato%>"/>		
	</td>		
	</tr>
	<tr>
	<td class="labelText">
		&nbsp;
	</td>				
	<td class="labelTextValue">
		<siga:Idioma key="gratuita.listadoCalendario.literal.avisoPartido"/>		
	</td>		
	</tr>
	
	<tr>	
	<td class="labelText">
		<siga:Idioma key="gratuita.listadoCalendario.literal.nombre"/>&nbsp;
	</td>				
	<td>
		<html:text name="CalendarioLaboralForm" property="nombreFiesta" size="30" styleClass="box" value="<%=(String)miHash.get(ScsCalendarioLaboralBean.C_NOMBREFIESTA)%>"></html:text>
	</td>
	</tr>

	</table>

	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
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
			if (validateCalendarioLaboralForm(document.CalendarioLaboralForm)){
	        	document.forms[0].submit();
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
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>