<!-- generarImpreso190.jsp -->
<% 
//	 VERSIONES:
//	 raul.ggonzalez 06-04-2005 creacion
%>

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
<%@ page import="com.siga.facturacionSJCS.form.GenerarImpreso190Form"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	
	
<%  
	// locales
	GenerarImpreso190Form formulario = (GenerarImpreso190Form)request.getAttribute("generarImpreso190Form");
		
	// datos seleccionados Combo
	ArrayList provinciaSel = new ArrayList();
	/*if (formulario.getCodigoProvincia()!=null) {
		provinciaSel.add(formulario.getCodigoProvincia());
	}
*/
	String anioSel = "",nombreFichero="",provincia="",telefono="",nombre="",apellido1="",apellido2="";
	String anioActual = (String)request.getAttribute("FcsAnioActual");

	String soporteSel = formulario.getSoporte();
	if (soporteSel==null) {
		soporteSel = "";
	}
		
	// anios
	ArrayList aniosFacturacion = (ArrayList) request.getAttribute("FcsAniosFacturacion");
	if (aniosFacturacion==null) aniosFacturacion = new ArrayList();
	
	Vector obj = (Vector) request.getAttribute("vCabeceraInforme");	
	if (obj!=null && obj.size()>0){
	    GenerarImpreso190Bean impresoBean=(GenerarImpreso190Bean)obj.get(0);
		anioSel=""+impresoBean.getAnio();
		nombreFichero=impresoBean.getNombreFichero();
		provincia=impresoBean.getIdprovincia();
		provinciaSel.add(provincia);
		telefono=impresoBean.getTelefono();
		nombre=impresoBean.getNombre();
		apellido1=impresoBean.getApellido1();
		apellido2=impresoBean.getApellido2();
	}	
    
    if (anioSel==null ||anioSel.equals("")) 
	{
		anioSel=anioActual;
	}	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	


	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.impreso190.cabecera" 
		localizacion="factSJCS.impreso190.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="generarImpreso190Form" staticJavascript="false" />  
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

<script>
	function accionGenerarImpreso() {
	   validarNombreFichero();
	  	sub();
		if (validateGenerarImpreso190Form(document.generarImpreso190Form)){
			document.forms[0].target="submitArea";
			document.forms[0].modo.value="generar";
			var f = document.forms[0].name;
			// con pantalla de espera
//			window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=messages.factSJCS.generandoImpreso190';
			 window.frames.resultado.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=messages.factSJCS.generandoImpreso190';
		}else{
		
			fin();
			return false;
		}
	}
    function validarNombreFichero(){
	   /*Si el usuario no ha metido la extension del fichero (.190) se le añade*/
	    if (document.forms[0].nombreFicheroOriginal.value!="" && document.forms[0].nombreFicheroOriginal.value.indexOf(".190")<0){
         document.forms[0].nombreFicheroOriginal.value=document.forms[0].nombreFicheroOriginal.value+".190";
		}
		document.forms[0].nombreFichero.value=document.forms[0].nombreFicheroOriginal.value;
	  /**/	
	}
	function mostrarBotonDownload(mens, fichero, logError) {
		document.forms[0].modo.value="mostrarBoton";
		if (mens) {
			document.forms[0].mensaje.value=mens;
		}else{
			document.forms[0].mensaje.value="";
		}
		
		document.forms[0].nombreFichero.value = fichero;
		document.forms[0].logError.value = logError;
		
		var anterior = document.forms[0].target;
		document.forms[0].target = "resultado";
		document.forms[0].submit();
		document.forms[0].target = anterior;
	}
	
	function accionGuardar(){
	  document.forms[0].target="resultado";
	  validarNombreFichero();
	  sub();
	  if (validateGenerarImpreso190Form(document.generarImpreso190Form)){
	    document.generarImpreso190Form.modo.value="modificarDatos";
	    document.generarImpreso190Form.submit();
	  }else{
	  
	  	fin();
	  	return false;
	  }
	  
	}
	
	function refrescarLocal(){ 
	  document.generarImpreso190Form.modo.value="";
	  document.forms[0].target="submitArea";
	  document.generarImpreso190Form.submit();
	}
	
</script>

</head>

<body onLoad="ajusteAlto('resultado');">



	<html:form action="/FCS_GenerarImpreso190.do" method="POST" target="resultado">
	<html:hidden name="generarImpreso190Form" property = "modo" value = ""/>
	<html:hidden property = "mensaje" value = ""/>
	<html:hidden property = "soporte" value = "<%=ClsConstants.TIPO_SOPORTE_TELEMATICO %>"/>
	<html:hidden property = "nombreFichero"/>
	<input type="hidden" name="logError" value="NO"/>


	<siga:ConjCampos leyenda="factSJCS.impreso190.leyenda">

	<table class="tablaCampos" align="center" border=0>

	
	<!-- FILA -->
	
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.impreso190.literal.anio"/>
	</td>				
	<td>
		<html:select name="generarImpreso190Form" property="anio" styleClass="boxCombo" value="<%=anioSel %>">
<%
	for (int i=0; i<aniosFacturacion.size(); i++) {
%>
			<html:option value="<%=(String)aniosFacturacion.get(i) %>" key="<%=(String)aniosFacturacion.get(i) %>"/>
<%
	}
%>
		</html:select>
	</td>

	<td class="labelText">
		<siga:Idioma key="factSJCS.impreso190.literal.nombreFichero"/>&nbsp;(*)
	</td>				
	<td>
		<html:text name="generarImpreso190Form" property="nombreFicheroOriginal" value="<%=nombreFichero%>" size="30" styleClass="box"></html:text>
	</td>

	</tr>
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="factSJCS.impreso190.literal.provincia"/>&nbsp;(*)
	</td>
	<td align="left">
		<siga:ComboBD nombre = "codigoProvincia" tipo="provincia"   clase="boxCombo" obligatorio="false"  elementoSel="<%=provinciaSel %>" />						
	</td>


	</tr>
	</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="factSJCS.impreso190.literal.datosPersonaContacto">

	<table class="tablaCampos" align="center" width="100%">

	<!-- FILA -->
	<tr>				

		<td class="labelText">
			<siga:Idioma key="factSJCS.impreso190.literal.telefonoContacto"/>&nbsp;(*)
		</td>
		<td>
			<html:text name="generarImpreso190Form" property="telefonoContacto"   value="<%=telefono%>" size="10" styleClass="box" ></html:text>
		</td>
	
	
		<td class="labelText">
			<siga:Idioma key="factSJCS.impreso190.literal.nombreContacto"/>&nbsp;(*)
		</td>
		<td>
			<html:text name="generarImpreso190Form" property="nombreContacto"  value="<%=nombre%>" size="30" styleClass="box"></html:text>
		</td>
	
		</tr>
	
		<!-- FILA -->
		<tr>				
	
		<td class="labelText">
			<siga:Idioma key="factSJCS.impreso190.literal.apellido1Contacto"/>&nbsp;(*)
		</td>
		<td>
			<html:text name="generarImpreso190Form" property="apellido1Contacto"  value="<%=apellido1%>" size="30" styleClass="box"></html:text>
		</td>
	
	
		<td class="labelText">
			<siga:Idioma key="factSJCS.impreso190.literal.apellido2Contacto"/>&nbsp;(*)
		</td>
		<td>
			<html:text name="generarImpreso190Form" property="apellido2Contacto" value="<%=apellido2%>" size="30" styleClass="box"></html:text>
		</td>
	
	</tr>

	</table>

	</siga:ConjCampos>

	</html:form>


	<siga:ConjBotonesAccion clase="botonesSeguido" botones="gi,G"/>


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
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
