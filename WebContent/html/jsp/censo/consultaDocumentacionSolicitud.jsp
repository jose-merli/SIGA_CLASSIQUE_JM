<!-- consultaDocumentacionSolicitud.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.DocumentacionSolicitudForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	// locales
	DocumentacionSolicitudForm formulario = (DocumentacionSolicitudForm)request.getAttribute("documentacionSolicitudForm");
	
	Vector resultado = (Vector) request.getAttribute("CenDocumentacionSolicitudTodosDocumentos");
	Vector seleccionados = (Vector) request.getAttribute("CenDocumentacionSolicitudResultados");

%>

<%@page import="java.util.Properties"%>
<html>
<!-- HEAD -->
<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_DocumentacionSolicitudes.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.documentacionSolicitud.cabecera" 
		localizacion="censo.documentacionSolicitud.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

<script>
	function accionGuardar() {
		sub();
		var datos = "";
		var validados = document.getElementsByName("validado");
		if (validados.type !="checkbox") {
		
			for (i = 0; i < validados.length; i++){
	
				if (validados[i].checked==1){
					var j=i+1;
					var aux="solicita_"+j;
					var solicitado=document.getElementById(aux);
					datos=datos + solicitado.value + "%";						
				}	
			}		
		} else {
		   if (validados.checked==1){
					var j=1;
					var aux="solicita_"+j;
					var solicitado=document.getElementById(aux);
					datos=datos + solicitado.value + "%";						
				}	
		}
			
		parent.document.forms[0].documentosSolicitados.value = datos;				
		parent.document.forms[0].modo.value = "insertar";
		parent.document.forms[0].target = "submitArea";
		parent.document.forms[0].submit();
	}
	
	function refrescarLocal() {
		parent.buscar();
	}
</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_DocumentacionSolicitudes.do" method="POST" target="submitArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "" />
			<input type="hidden" name="documentosSolicitados">
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>

<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="5,95";
		nombresCol="censo.SolicitudIncorporacionDatos.literal.estado,censo.documentacionSolicitud.literal.documentación";
%>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol %>"
		   columnSizes="<%=tamanosCol %>">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
<%	
	} else { 

		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {

			CenDocumentacionSolicitudBean docBean = (CenDocumentacionSolicitudBean) resultado.get(i);
			boolean selec = false;
			if (seleccionados.contains(docBean.getIdDocumentacion())) {
				// esta seleccionado
				selec = true;
			}
			
			String cont = new Integer(i+1).toString();

%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

		   <tr class="listaNonEdit">
				<td>

			<% if (selec) {  %>
					<input type="checkbox" name="validado" value="1" checked>
			<% } else {  %>
					<input type="checkbox" name="validado" value="1">
			<% }  %>

					<!-- campos hidden -->
					<input type="hidden" name="solicita_<%=cont%>" value="<%=docBean.getIdDocumentacion()%>">									
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(docBean.getDescripcion()) %>
				</td>
		   </tr>


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>


	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->


	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->

		<siga:ConjBotonesAccion botones="G" clase="botonesDetalle" />

	<!-- FIN: BOTONES REGISTRO -->


		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
