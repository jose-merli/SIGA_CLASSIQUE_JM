<!-- altaRetencionIRPF.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>


<!-- JSP -->
<%  
	String botones = "y,X";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	// Obtengo los datos para validar las fechas.
	Vector obj = (Vector) request.getSession().getAttribute("fechas");
	request.getSession().removeAttribute("fechas");

%>	

<script>
	function validarFechas(fechaInicio, fechaFin) {
		var fi = fechaInicio.substring(6,10)+fechaInicio.substring(3,5)+fechaInicio.substring(0,2);
		var ff = fechaFin.substring(6,10)+fechaFin.substring(3,5)+fechaFin.substring(0,2);
		if(fi>ff) {
			alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert1'/>");
			return false;
		}
		<%
		String fInicio 	= "";
		String fFin		= "";
		Hashtable hash	= null;
		if(obj!=null) {
			for(int x=0;x<obj.size();x++) {
				hash 	= (Hashtable)obj.get(x);
				if(hash.get("FECHAINICIO") != null) {
					fInicio = (String)hash.get("FECHAINICIO");
					fFin	= (String)hash.get("FECHAFIN");
					fInicio = fInicio.substring(0,4)+fInicio.substring(5,7)+fInicio.substring(8,10);
					if(!fFin.equals(""))
						fFin 	= fFin.substring(0,4)+fFin.substring(5,7)+fFin.substring(8,10);
		%>
				if(((fi > "<%=fInicio%>") && (fi < "<%=fFin%>" && "<%=fFin%>" != "")) ||
 				  ((ff  	> "<%=fInicio%>") && (ff	< "<%=fFin%>" && "<%=fFin%>" != "")) ||
 				  (fi == "<%=fInicio%>")
 				  )	{
					alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert2'/>");
					return false;
				} 
		
		<%      }//if
				
		    }//for
		}
		%>
		return true;
	}
</script>
<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaRetencionIRPF.literal.aRetencionIRPF"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>

		
	
		
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script type="text/javascript">
		function mostrarCalendario(numfila){
			var resultado;			
			var tabla;
			tabla = document.getElementById('listadoCalendario');
			resultado = showCalendarGeneral(tabla.rows[numfila].cells[0].all[4]);
		}		
	</script>
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.altaRetencionIRPF.literal.aRetencionIRPF"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<fieldset>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="JGR_PestanaRetencionesIRPF.do" method="post" target="_self">
	<input type="hidden" name="modo" value="insertar">
	<tr>
	<td>
	 <table width="100%" border="0">
		<tr>
			<td width="35%" class="labelText" colspan="2">
			<siga:Idioma key="gratuita.altaRetencionIRPF.literal.fDesde"/>
			</td>
			<td>
				<siga:Fecha  nombreCampo= "fechaInicio" posicionX="10" posicionY="10"/>
			</td>
		</tr>
		<tr>
			<td width="35%" class="labelText" colspan="2">
			<siga:Idioma key="gratuita.altaRetencionIRPF.literal.retencion"/>
			</td>
			<td>
				<siga:ComboBD nombre="idRetencion" tipo="tiposirpf" estilo="true" clase="boxCombo" obligatorio="false"/>
			</td>
		</tr>
	</table>
   </td>
   </tr>
   	</html:form>
	</table>
	</fieldset>

	<siga:ConjBotonesAccion botones="<%=botones%>"  modal="P"/>	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionVolver() {
		}

		function accionCancelar() {		
			//window.top.cierraConParametros("NORMAL");
			window.top.close();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script>
		<!-- Asociada al boton GuardarCerrar -->
		/*function accionGuardarCerrar() 
		{
			if(document.forms[0].idRetencion.value == "")
			{
				alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert3'/>");
				return false;
			}
			if(validarFechas(document.forms[0].fechaInicio.value,document.forms[0].fechaFin.value))
			{
				document.forms[0].submit();
				window.top.returnValue="MODIFICADO";			
			}
		}
		*/
		function accionGuardarCerrar() {
			sub();
			if(document.forms[0].idRetencion.value == "") {
				alert("<siga:Idioma key='gratuita.altaRetencionesIRPF.literal.alert3'/>");
				fin();
				return false;
			} else if(document.forms[0].fechaInicio.value == "") {
				alert("Debe introducir una fecha de inicio");
				fin();
				return false;
			} else {
				/*document.forms[0].target = "submitArea";
				document.forms[0].submit();
				//window.top.returnValue="MODIFICADO"; */
				document.forms[0].setAttribute("action","<%=app%>/JGR_PestanaRetencionesIRPF.do");
				document.forms[0].submit();
				window.top.returnValue="MODIFICADO";
				window.top.close();
			}
		}
</script>
</body>
</html>
