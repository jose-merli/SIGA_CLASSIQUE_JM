<!DOCTYPE html>
<html>
<head>
<!-- tiposServicios.jsp -->
<!-- VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
     Utilizando tags pinta una lista con cabeceras fijas 
     VERSIONES:
	yolanda.garcia 22-12-2004 Creación
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
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
		
	Vector vDatosTServ = (Vector)request.getAttribute("datosTServ");
	request.removeAttribute("datosTServ");	

	String editable = (String)ses.getAttribute("editable");
	boolean bEditable = true;
	
	if (editable!=null && !editable.equals(""))
	{
		bEditable = editable.equals("1");
	}
%>


	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="TiposServiciosForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionConceptos.tipoServicio.cabecera" 
			localizacion="facturacion.tiposServicios.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<script>
			function refrescarLocal()
			{
				document.forms[0].target="_self";
			 	document.forms[0].modo.value="abrir";
			 	document.forms[0].submit();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_TiposServicios.do" method="POST"  target="submitArea" style="display:none">
			<html:hidden styleId="modo" property="modo" value=""/>
			
			<!-- RGG: cambio a formularios ligeros -->

			<input type="hidden" name="actionModal" id="actionModal" value="">
		</html:form>			
			<%
			if (vDatosTServ==null || vDatosTServ.size()==0)
			{
			%>
				<siga:Table 
   					name="tabladatos"
   					border="1"
   					columnNames="facturacion.tiposServicios.literal.tiposDeServicios"
   					columnSizes="100">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				</siga:Table>
	 		
			<%
			}
			else
			{
				if (!bEditable)
				{
				%>
					<siga:Table 
	   					name="tabladatos"
	   					border="1"
	   					columnNames="facturacion.tiposServicios.literal.tiposDeServicios"
	   					columnSizes="100">
	   						
	   					<!-- INICIO: ZONA DE REGISTROS -->
						<!-- Aqui se iteran los diferentes registros de la lista -->
				
						<%
	   					for (int i=0; i<vDatosTServ.size(); i++)
			   			{
							Hashtable miHash = (Hashtable)vDatosTServ.elementAt(i);
							%>
					
							<tr class="listaNonEdit">
								<td>
									<input type="hidden" name="oculto<%=""+(i+1)%>_1"  id="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDINSTITUCION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_2"  id="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("IDSERIEFACTURACION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_3"  id="oculto<%=""+(i+1)%>_3" value="<%=miHash.get("IDTIPOSERVICIOS")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_4"  id="oculto<%=""+(i+1)%>_4" value="<%=miHash.get("IDSERVICIO")%>">
									<%=miHash.get("DESCRIPCIONSERVICIO")%>
								</td>
							</tr>
						<%}%>
						
						<!-- FIN REGISTRO -->
						<!-- FIN: ZONA DE REGISTROS -->	
					</siga:Table>
				<%} 
				else 
				{
				%>
					<siga:Table 
		   				name="tabladatos"
	   					border="1"
	   					columnNames="facturacion.tiposServicios.literal.tiposDeServicios,"
	   					columnSizes="90,10">
	   					
	   					<!-- INICIO: ZONA DE REGISTROS -->
						<!-- Aqui se iteran los diferentes registros de la lista -->
				
						<%
	   					for (int i=0; i<vDatosTServ.size(); i++)
			   			{
							Hashtable miHash = (Hashtable)vDatosTServ.elementAt(i);
							%>
						
							<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="B" clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" >
								<td>
									<input type="hidden" name="oculto<%=""+(i+1)%>_1" id="oculto<%=""+(i+1)%>_1" value="<%=miHash.get("IDINSTITUCION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_2" id="oculto<%=""+(i+1)%>_2" value="<%=miHash.get("IDSERIEFACTURACION")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_3" id="oculto<%=""+(i+1)%>_3" value="<%=miHash.get("IDTIPOSERVICIOS")%>">
									<input type="hidden" name="oculto<%=""+(i+1)%>_4" id="oculto<%=""+(i+1)%>_4" value="<%=miHash.get("IDSERVICIO")%>">
									<%=miHash.get("DESCRIPCIONSERVICIO")%>
								</td>
							</siga:FilaConIconos>
						<%}%>
						
						<!-- FIN REGISTRO -->
						<!-- FIN: ZONA DE REGISTROS -->	
					</siga:Table>
				<%}
			}%>

		<!-- FIN: LISTA DE VALORES -->
		
		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->
	
		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->

			<%if (!bEditable){%>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
			<%} else {%>
				<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" />
			<%}%>

		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
			//Asociada al boton Nuevo
			function accionNuevo() 
			{		
				document.forms[0].modo.value="nuevo";
				document.forms[0].target='';						
				var salida = ventaModalGeneral(document.forms[0].name,"P");
				if (salida == "MODIFICADO") refrescarLocal();
			}

			//Asociada al boton Volver
			function accionVolver() 
			{	
				document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();
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
