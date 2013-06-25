<!-- busquedaFacturacionResultados.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 07-03-2005 creacion
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
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacion.form.MantenimientoFacturacionForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	// locales
	MantenimientoFacturacionForm formulario = (MantenimientoFacturacionForm)request.getSession().getAttribute("mantenimientoFacturacionForm");
	
	Vector resultado = (Vector) request.getAttribute("SJCSResultadoBusquedaFacturacion");

%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_MantenientoFacturacion.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_MantenientoFacturacion.do?noReset=true" method="POST" target="mainWorkArea">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "" />

			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>


<%

		String tamanosCol="";
		String nombresCol="";
		tamanosCol="30,10,10,20,20,10";
		nombresCol+="factSJCS.datosFacturacion.literal.institucion," +
					"factSJCS.datosFacturacion.literal.fechaInicio," +
					"factSJCS.datosFacturacion.literal.fechaFin," +
					"factSJCS.datosFacturacion.literal.serie," +
					"factSJCS.datosFacturacion.literal.estado,";
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
			Hashtable registro = (Hashtable) resultado.get(i);
			String cont = new Integer(i+1).toString();

			// permisos de acceso
			String permisos = "C,E,B";
			String modo = "";
			String idInstitucion = UtilidadesString.mostrarDatoJSP(registro.get("nombreInstitucion"));
			if (usrbean.getLocation().equals(idInstitucion)) {
				modo = "edicion";
			} else {
				modo = "consulta";
			}

			String idFacturacion = UtilidadesString.mostrarDatoJSP(registro.get("idFacturacion"));
			String fechaIni = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get("fechaIni")));
			String fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get("fechaFin")));
			String serie = UtilidadesString.mostrarDatoJSP(registro.get("serie"));
			String estado = UtilidadesString.mostrarDatoJSP(registro.get("estado"));
									
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acci�n sobre los registos  -->

  			<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos %>" modo="<%=modo %>" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idFacturacion %>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=usrbean.getLocation() %>">

					<%=idInstitucion %>
				</td>
				<td>
					<%=fechaIni %>
				</td>
				<td>
					<%=fechaFin %>
				</td>
				<td>
					<%=serie %>
				</td>
				<td>
					<%=estado %>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>


		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
