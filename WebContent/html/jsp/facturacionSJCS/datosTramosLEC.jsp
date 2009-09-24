<!-- datosTramosLEC.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//recogemos los datos
	Hashtable resultado = (Hashtable)request.getAttribute("resultado");

	//variables quese van a mostrar en la jsp
	String retencion="", maximo="", minimo="", idTramo="";

	//inicializamos los valores
	try{
		retencion = (String)resultado.get(FcsTramosLecBean.C_RETENCION);
		minimo = (String)resultado.get(FcsTramosLecBean.C_MINIMOSMI);
		maximo = (String)resultado.get(FcsTramosLecBean.C_MAXIMOSMI);
		idTramo = (String)resultado.get(FcsTramosLecBean.C_IDTRAMOLEC);
	}catch(Exception e){}

	//recuperamos el modo de acceso
	String modo ="Modificar";
	if ((minimo==null)||(minimo.equals("")))modo="Insertar";
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<html:javascript formName="MantenimientoTramosLECForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="FactSJCS.mantTramosLEC.cabecera"/>
		</td>
	</tr>
	</table>


	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:javascript formName="DatosMantenimientoTramosLECForm" staticJavascript="false" />
	<html:form action="/FCS_MantenimientoLEC.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idTramo" value = "<%=idTramo%>"/>
	
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.mantTramosLEC.leyenda">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="FactSJCS.mantTramosLEC.literal.minimo"/>&nbsp;(*)
		</td>				
		<td >
			<html:text name="DatosMantenimientoTramosLECForm" property="minimo" size="6" maxlength="3" styleClass="boxNumber" readonly="false" value="<%=minimo%>"/>
		</td>
		</tr>				

		<tr>
		<td class="labelText">
			<siga:Idioma key="FactSJCS.mantTramosLEC.literal.maximo"/>&nbsp;(*)
		</td>
		<td >
			<html:text name="DatosMantenimientoTramosLECForm" property="maximo" size="6" maxlength="3" styleClass="boxNumber" readonly="false" value="<%=maximo%>"/>
		</td>
		</tr>

		<!-- FILA -->
		<tr>

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="FactSJCS.mantTramosLEC.literal.nombre"/>&nbsp;(*)
		</td>
		<td >
			<html:text name="DatosMantenimientoTramosLECForm" property="retencion" size="6" maxlength="3" styleClass="boxNumber" readonly="false" value="<%=retencion%>"/> &nbsp;%
		</td>
		</tr>
		
		</table>

	</siga:ConjCampos>


	</td>
	</tr>

	</html:form>
	
	</table>

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			if (validateMantenimientoTramosLECForm(document.MantenimientoTramosLECForm)){
				if (document.forms[0].minimo.value > document.forms[0].maximo.value){
					alert ('<siga:Idioma key="messages.factSJCS.error.valoresErroneos"/>');
				}else{
				    document.forms[0].modo.value="<%=modo%>";
					document.forms[0].submit();
					window.returnValue="MODIFICADO";
				}
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
