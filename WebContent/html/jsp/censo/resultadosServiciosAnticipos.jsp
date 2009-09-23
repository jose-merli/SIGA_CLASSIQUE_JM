<!-- resultadosServiciosAnticipos.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
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
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("documentacionSolicitudForm");
	
	Vector resultado = (Vector) request.getAttribute("resultado");
	//mirar luego
	Vector seleccionados = (Vector) request.getAttribute("CenDocumentacionSolicitudResultados");

%>

<%@page import="java.util.Properties"%>
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_AnticiposCliente.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="censo.documentacionSolicitud.cabecera" 
		localizacion="censo.documentacionSolicitud.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

<script>

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
		<html:form action="/CEN_AnticiposCliente.do" method="POST" target="submitArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "" />
			<input type="hidden" name="documentosSolicitados">
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="10,30,30,30";
		nombresCol="infomes.seleccionSerie.literal.sel,pys.busquedaServicios.literal.tipo,pys.busquedaServicios.literal.categoria,pys.busquedaServicios.literal.servicio";
					
%>

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol %>"
		   tamanoCol="<%=tamanosCol %>"
		   alto="275"
		   ajusteBotonera="true"
		  >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
<%	
	} else { 

		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
						
			boolean selec = false;
			Row fila = (Row)resultado.elementAt(i);

			//if (seleccionados.contains(fila)) {
				// esta seleccionado
				//selec = true;
			//}
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
					<input type="hidden" name="solicita_<%=cont%>_1" value="<%=fila.getString("IDINST")%>">
					<input type="hidden" name="solicita_<%=cont%>_2" value="<%=fila.getString("IDTIPOSERV")%>">
					<input type="hidden" name="solicita_<%=cont%>_3" value="<%=fila.getString("IDSERV")%>">
					<input type="hidden" name="solicita_<%=cont%>_4" value="<%=fila.getString("IDESERVINST")%>">
																													
				</td>
				<td>
					<!-- columna categorias -->
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRECATSERVICIO")) %>
				</td>
				<td>
					<!-- columna tiposervicio -->
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRETIPOSERVICIO")) %>
				</td>
				<td>
					<!-- columna nombreservicio -->
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRESERVICIO")) %>
				</td>
		   </tr>


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:TablaCabecerasFijas>


	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
