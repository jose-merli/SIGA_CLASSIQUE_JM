<!-- consultaPlantillas.jsp -->
<!-- 
	 Muestra las diferentes plantillas asociadas a una institucion
	 VERSIONES:
	 miguel.villegas 25-04-2005 Creacion
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION");
	String botonesAccion= "N";

%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<style type="text/css">

			.especif
			{
				background-color : #<%=src.get("color.titleBar.BG")%>;
				position:absolute; width:964; height:35; z-index:2; top: 285px; left: 0px
			}

		</style>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			<!-- Funcion asociada a boton nuevo -->
			function accionNuevo() 
			{							
				document.forms[0].modo.value='nuevo';
				var resultado = ventaModalGeneral("PlantillasForm","P");
				if (resultado=="MODIFICADO")
				{
					refrescarLocal();
				}
				//document.forms[0].submit();
			}

			function refrescarLocal() 
			{
				document.location.reload();
			}
		
		</script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="facturacion.consultaPlantillas.cabecera" 
			  localizacion="facturacion.busquedaAbonos.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

	</head>

	<body>
		<div id="camposRegistro" class="posicionBusquedaSolo">	
			<html:form action="/FAC_Plantillas.do" method="post" target="submitArea">
				<html:hidden property="modo" value=""/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>

		</html:form>
		
				
				<siga:Table 
				   name="tablaDatos"
				   border="1"
				   columnNames="facturacion.consultaPlantillas.literal.nombrePlantilla,facturacion.consultaPlantillas.literal.plantilla,"
				   columnSizes="40,40,20"
				   modal="P">
				<%
		    	if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
			    {
				%>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				<%
		    	}	    
			    else
			    { %>
		    		<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
					int recordNumber=1;
					String botonesFila="E,B";	            	
					while (en.hasMoreElements())
					{
		            	FacPlantillaFacturacionBean registro= (FacPlantillaFacturacionBean) en.nextElement(); 
		            	%>
						<siga:FilaConIconos
							  fila='<%=String.valueOf(recordNumber)%>'
							  botones="<%=botonesFila%>"
							  visibleConsulta="no"
							  modo="edicion"
							  clase="listaNonEdit">
							  
							<td>
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idInstitucion%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=registro.getIdPlantilla()%>">
								<%=registro.getDescripcion()%>
							</td>
							<td>
								<%=registro.getPlantillaPDF()%>
							</td>
						</siga:FilaConIconos>
						<% recordNumber++;
					} 
				} %>
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
				
			<siga:ConjBotonesAccion botones="<%=botonesAccion%>" modo="" clase="botonesDetalle"/>
				
			<!-- FIN: BOTONES REGISTRO -->
			<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

		</div>
	</body>
</html>