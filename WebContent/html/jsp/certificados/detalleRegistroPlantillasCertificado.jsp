<!-- detalleRegistroPlantillasCertificado.jsp -->
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
	String sIdTipoProducto=(String)htDatos.get("idTipoProducto");
	String sIdProducto=(String)htDatos.get("idProducto");
	String sIdProductoInstitucion=(String)htDatos.get("idProductoInstitucion");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton GuardarCerrar
			function accionGuardarCerrar() 
			{  
				//sub();
				if(MantenimientoCertificadosPlantillasForm.descripcion.value=="")
				{
					var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
	
					alert (mensaje);
					//fin();
					return false;
				}
				if (!TestFileType(document.MantenimientoCertificadosPlantillasForm.theFile.value, ['ZIP', 'FO'])){
					//fin();
					return false;
				}

				if (<%=bNuevo%>)
				{ 
					
					if(document.MantenimientoCertificadosPlantillasForm.theFile.value=="")
					{
						var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.archivo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";

						alert (mensaje);
						//fin();
						return false;
					}
				}
				
				//LMS 22/08/2006
				//Ventana de espera con las tuercas.
				//MantenimientoCertificadosPlantillasForm.submit();
				
				var fname = document.getElementById("MantenimientoCertificadosPlantillasForm").name;
				window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.wait';
				
				window.top.returnValue="MODIFICADO";
			}
			
			//Asociada al boton Cerrar
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="certificados.plantillas.editar.literal"/>
				</td>
			</tr>
		</table>
			
		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<html:form action="/CER_Plantillas.do" method="POST" target="submitArea" enctype="multipart/form-data">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
				<html:hidden property="idProducto" value="<%=sIdProducto%>"/>
				<html:hidden property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
				
				<html:hidden property="idPlantilla" value="<%=sIdPlantilla%>"/>

				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="certificados.mantenimiento.titulo">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/>&nbsp(*)
										</td>
										<td>
											<html:text property="descripcion" value="<%=sDescripcion%>" styleClass="boxCombo" maxlength="50"/>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.archivo"/>&nbsp(*)
										</td>				
										<td>
											<html:file name="MantenimientoCertificadosPlantillasForm" property="theFile" styleClass="boxCombo"/>
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
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>