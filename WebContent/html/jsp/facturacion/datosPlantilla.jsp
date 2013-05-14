<!-- datosPlantilla.jsp -->
<!-- 
	 Muestra el formulario de edicion de plantillas
	 VERSIONES:
		 miguel.villegas 25/04/2005
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.FacLineaAbonoBean"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.FacPlantillaFacturacionBean"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	FacPlantillaFacturacionBean registro= new FacPlantillaFacturacionBean();

	String nextModo = request.getAttribute("modelo").toString(); // Obtengo la operacion (modificar o insertar)a realizar
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el id de la institucion a la que pertenece

	Vector modelosPosibles=(Vector)request.getAttribute("plantillas"); // Obtengo las diferentes plantillas a elegir

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if (nextModo.equalsIgnoreCase("modificar")){
		Vector entradas=(Vector)request.getAttribute("container"); // Obtengo el vector
		registro=(FacPlantillaFacturacionBean)entradas.firstElement();
	}			

	String	botones="C,Y,R";
%>

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="PlantillasForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			  titulo="facturacion.busquedaAbonos.literal.cabecera" 
			  localizacion="facturacion.busquedaAbonos.ruta"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>


	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="facturacion.datosPlantillas.cabecera"/>
				</td>
			</tr>
		</table>
	
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalPeque" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposPeque"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.datosPlantillas.literal.datos">
						<table class="tablaCampos" align="center">
							<html:form action="/FAC_Plantillas.do" method="POST" target="submitArea">
								<html:hidden property = "modo" value = "<%=nextModo%>"/>
								<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosPlantillas.literal.descripcion"/>&nbsp;(*)
									</td>
									<td class="nonEdit">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<html:textarea cols="50" rows="2" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" property="descripcion" styleClass="box"></html:textarea> 
										<% } else { %>
											<html:textarea cols="50" rows="2" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" property="descripcion" styleClass="box" value="<%=registro.getDescripcion()%>"></html:textarea> 
							  	    	<% } %>
									</td>
								</tr>
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.datosPlantillas.literal.plantilla"/>&nbsp;(*)
									</td>	
									<td class="nonEdit">
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
											<html:select name="PlantillasForm" property="plantillaPDF" style = "null" styleClass = "box" value="">
												<html:option value="" ></html:option>
												<% for (int j=0; j<modelosPosibles.size();j++){ %>
													<html:option value="<%=(String)modelosPosibles.get(j)%>" ><%=(String)modelosPosibles.get(j)%></html:option>
												<% } %>
											</html:select>
										<% } else { %>
											<html:select name="PlantillasForm" property="plantillaPDF" style = "null" styleClass = "box" value="<%=registro.getPlantillaPDF()%>">
												<% for (int j=0; j<modelosPosibles.size();j++){ %>
													<html:option value="<%=(String)modelosPosibles.get(j)%>" ><%=(String)modelosPosibles.get(j)%></html:option>
												<% } %>
											</html:select>
							  	    	<% } %>
									</td>
								</tr>
							</html:form>	
						</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modal="P"/>
			
			<!-- FIN: BOTONES REGISTRO -->
	
		
			<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
			<script language="JavaScript">
	
				<!-- Asociada al boton GuardarCerrar -->
				function accionGuardarCerrar() 
				{
					sub();	
					if (validatePlantillasForm(document.PlantillasForm)){
						document.forms[0].submit();							
					}else{
					
						fin();
						return false;
					
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
		</div>
		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>