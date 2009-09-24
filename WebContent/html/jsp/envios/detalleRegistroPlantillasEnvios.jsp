<!-- detalleRegistroPlantillasEnvios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Hashtable htDatos = (Hashtable)datos.elementAt(0);
	
	String sIdPlantilla=(String)htDatos.get("idPlantilla");
	String sDescripcion=(String)htDatos.get("descripcion");
	String sPorDefecto=(String)htDatos.get("porDefecto");

	String sIdInstitucion=(String)htDatos.get("idInstitucion");
	String sIdTipoEnvio=(String)htDatos.get("idTipoEnvio");
	String sIdPlantillaEnvios=(String)htDatos.get("idPlantillaEnvios");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
			    sub();
					
				if(PlantillasEnviosPlantillasForm.descripcion.value=="")
				{
					var mensaje = "<siga:Idioma key="envios.plantillas.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	
					alert (mensaje);
					fin();
					return false;
				}
				
				if (!TestFileType(document.PlantillasEnviosPlantillasForm.theFile.value, ['ZIP', 'FO'])){
					fin();
					return false;
				}
				
				if (<%=bNuevo%>)
				{
				
					if(document.PlantillasEnviosPlantillasForm.theFile.value=="")
					{
						var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.archivo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";

						alert (mensaje);
						fin();
						return false;
					}
				}

				PlantillasEnviosPlantillasForm.submit();
	
				window.returnValue="MODIFICADO";
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.consultaPlantillas.cabecera2"/>
			</td>
		</tr>
	</table>	
			
			<html:form action="/ENV_Plantillas_Salida.do" method="POST" target="submitArea" enctype="multipart/form-data">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoEnvio" value="<%=sIdTipoEnvio%>"/>
				<html:hidden property="idPlantillaEnvios" value="<%=sIdPlantillaEnvios%>"/>
				
				<html:hidden property="idPlantilla" value="<%=sIdPlantilla%>"/>

				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="envios.consultaPlantillas.cabecera2">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/>&nbsp;(*)
										</td>
										<td>
											<html:text property="descripcion" value="<%=sDescripcion%>" styleClass="boxCombo"/>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.archivo"/>&nbsp;(*)
										</td>				
										<td>
											<html:file name="PlantillasEnviosPlantillasForm" property="theFile" styleClass="boxCombo"/>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.pordefecto"/>
										</td>				
										<td>
											<input type="checkbox" name="porDefecto" value="1" styleClass="boxCombo" <%if (sPorDefecto.equals("S")) {%>checked<%}%>/>
										</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones = bEditable ? "Y,C" : "C";
%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="P"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>