<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 20-04-2005 creacion
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
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPrevisionesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	// locales
	MantenimientoPrevisionesForm formulario = (MantenimientoPrevisionesForm)request.getSession().getAttribute("mantenimientoPrevisionesForm");
	Vector resultado = (Vector) request.getAttribute("SJCSResultadoBusquedaPrevisiones");

%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoPrevisionesForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
 	<script>
 		function refrescarLocal(){
 			parent.buscar();
 		}
 	</script>
 	
</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FCS_MantenimientoPrevisiones.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo"  styleId = "modo"  value = "" />
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		</html:form>	

<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="20,20,50,10";
		nombresCol+="factSJCS.datosFacturacion.literal.fechaInicio," +
					"factSJCS.datosFacturacion.literal.fechaFin," +
					"factSJCS.datosFacturacion.literal.nombre,";
%>
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol %>"
		   tamanoCol="<%=tamanosCol %>"
		   alto="298" 
		   activarFilaSel="true" >

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
			Hashtable registro = (Hashtable) resultado.get(i);
			String cont = new Integer(i+1).toString();

			String modo = "";
			String idInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_IDINSTITUCION));
			if (usrbean.getLocation().equals(idInstitucion)) {
				modo = "edicion";
			} else {
				modo = "consulta";
			}

			String idFacturacion = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_IDFACTURACION));
			String fechaIni = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsFacturacionJGBean.C_FECHADESDE)));
			String fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsFacturacionJGBean.C_FECHAHASTA)));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_NOMBRE));
//			String estado = UtilidadesString.mostrarDatoJSP(registro.get("DESESTADO"));
//			String idestado = UtilidadesString.mostrarDatoJSP(registro.get("IDESTADO"));
//			String fechaEstado = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get("FECHAESTADO")));
//			String nomInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(CenInstitucionBean.C_ABREVIATURA));

			// permisos de acceso
			String permisos = "C,B,E";
									
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

  			<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos %>" modo="<%=modo %>" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idFacturacion %>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=usrbean.getLocation() %>">

					<%=fechaIni %>
				</td>
				<td>
					<%=fechaFin %>
				</td>
				<td>
					<%=nombre %>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:TablaCabecerasFijas>


		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
