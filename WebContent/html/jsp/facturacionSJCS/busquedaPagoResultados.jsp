<!DOCTYPE html>
<html>
<head>
<!-- busquedaPagoResultados.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 david.sanchez 17-03-2005 creacion
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
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
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
	MantenimientoPagoForm formulario = (MantenimientoPagoForm)request.getSession().getAttribute("mantenimientoPagoForm");
	
	Vector resultado = (Vector) request.getAttribute("SJCSResultadoBusquedaPago");

%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_MantenimientoPago.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
 	<script>
 		//Refresco al borrar:
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
		<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainPestanas" style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo"  styleId = "modo" value = "" />
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		</html:form>	
		

<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="20,10,10,15,10,25,10";
		nombresCol+="factSJCS.datosPagos.literal.institucion," +
					"factSJCS.datosPagos.literal.fechaInicio," +
					"factSJCS.datosPagos.literal.fechaFin,"+
					"factSJCS.datosPagos.literal.estado,"+
					"factSJCS.datosPagos.literal.fechaEstado,"+
					"factSJCS.datosPagos.literal.nombre,";
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

			String modo = "";
			String idInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(FcsPagosJGBean.C_IDINSTITUCION));
			if (usrbean.getLocation().equals(idInstitucion)) {
				modo = "edicion";
			} else {
				modo = "consulta";
			}

			String idPagosJG = UtilidadesString.mostrarDatoJSP(registro.get(FcsPagosJGBean.C_IDPAGOSJG));
			String fechaIni = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsPagosJGBean.C_FECHADESDE)));
			String fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsPagosJGBean.C_FECHAHASTA)));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.get(FcsPagosJGBean.C_NOMBRE));
			String estado = UtilidadesString.mostrarDatoJSP(registro.get("DESESTADO"));
			String idestado = UtilidadesString.mostrarDatoJSP(registro.get("IDESTADO"));
			String fechaEstado = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get("FECHAESTADO")));
			String nomInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(CenInstitucionBean.C_ABREVIATURA));

			// Permisos de acceso a los botones Consultar C, Borrar B y Editar E:
			String permisos = "C";
			if ((idInstitucion.equals(usrbean.getLocation())) && 
				( (idestado.equals(ClsConstants.ESTADO_PAGO_ABIERTO)) || (idestado.equals(ClsConstants.ESTADO_PAGO_EJECUTADO)) )) {
				permisos += ",B";
			}
			if ((idInstitucion.equals(usrbean.getLocation())) && 
				( (idestado.equals(ClsConstants.ESTADO_PAGO_ABIERTO)) || (idestado.equals(ClsConstants.ESTADO_PAGO_EJECUTADO)) )) {
				permisos += ",E";
			}
									
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

  			<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos %>" modo="<%=modo %>" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<!-- 1. IDPAGOSJG
						 2. IDINSTITUCION REGISTRO
						 3. IDINSTITUCION USUARIO
						 4. IDESTADOPAGOSJG
					-->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idPagosJG%>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=usrbean.getLocation()%>">
					<input type="hidden" name="oculto<%=cont %>_4" value="<%=idestado%>">					

					<%=nomInstitucion %>
				</td>
				<td>
					<%=fechaIni %>
				</td>
				<td>
					<%=fechaFin %>
				</td>
				<td>
					<%=estado %>
				</td>
				<td>
					<%=fechaEstado %>
				</td>
				<td>
					<%=nombre %>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>


		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
