<!-- detalleImagenPlantilla.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<%@page import="com.atos.utils.ClsConstants"%>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
		<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
		<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
		
		
		
			//Asociada al boton GuardarCerrar
			function accionGuardarCerrar() 
			{
			    sub();
				if(document.ImagenPlantillaForm.modo.value=='insertar'){
					
					if (!TestFileType(document.ImagenPlantillaForm.theFile.value, ['JPG', 'GIF','PNG','BMP'])){
						fin();
						return false;
					}
					if(document.ImagenPlantillaForm.nombre.value=="")
					{
						var mensaje = "<siga:Idioma key="envios.imagenes.literal.nombre"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
		
						alert (mensaje);
						fin();
						return false;
					}
					if(document.ImagenPlantillaForm.theFile.value=="")
						{
						var mensaje = "<siga:Idioma key="envios.imagenes.literal.archivo"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
						alert (mensaje);
						fin();
						return false;
					}
					if(document.ImagenPlantillaForm.theFile.size><%=ClsConstants.MAX_SIZE_IMAGE_EMBEBED%>){
						var mensaje = "<siga:Idioma key="envios.imagenes.aviso.bytesExcedidos"/>" + "<%=ClsConstants.MAX_SIZE_IMAGE_EMBEBED%>";
						alert (mensaje);
						fin();
						return false;
					
					
					}
					
				}else{
					if(document.ImagenPlantillaForm.nombre.value=="")
					{
						var mensaje = "<siga:Idioma key="envios.imagenes.literal.nombre"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
						alert (mensaje);
						fin();
						return false;
					}
				}
				document.ImagenPlantillaForm.submit();
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

	<body >
	
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.consultaPlantillas.cabecera2"/>
			</td>
		</tr>
	</table>	
			
			<html:form action="/ENV_ImagenesPlantilla" method="POST" target="submitArea" enctype="multipart/form-data">
				 <bean:define id="botonesEdicion" name="ImagenPlantillaForm" property="botonesEdicion" type="java.lang.String"/>
				
				<c:choose>
					<c:when test="${ImagenPlantillaForm.idImagen==null}">
						<html:hidden property = "modo" value="insertar"/>
					</c:when>
					<c:otherwise>
					  <html:hidden property = "modo" value="modificar"/>
					</c:otherwise>
				</c:choose>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property="idInstitucion"/>
				<html:hidden property="idTipoEnvios"/>
				<html:hidden property="idPlantillaEnvios"/>
				<html:hidden property="idImagen"/>

				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="envios.imagenes.cabecera">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="envios.imagenes.literal.nombre"/>&nbsp;(*)
										</td>
										<td>
											<html:text property="nombre"  styleClass="boxCombo"/>
										</td>
									</tr>
									<c:if test="${ImagenPlantillaForm.idImagen==null}">
						
					
									<tr>				
										<td class="labelText">
											<siga:Idioma key="envios.imagenes.literal.archivo"/>&nbsp;(*)
										</td>				
										<td>
											<html:file property="theFile" styleClass="boxCombo"/>
										</td>
									</tr>
									
									</c:if>			
									<tr>				
										<td class="labelText">
											<siga:Idioma key="envios.imagenes.literal.incrustado"/>&nbsp;(*)
											
										</td>
										<td>
										<html:checkbox property="embebed" styleClass="boxCombo"/>
																				
											
										</td>
									</tr>	
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>


			<siga:ConjBotonesAccion botones="<%=botonesEdicion%>" modal="P"/>
			</html:form>		
		<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>
	</body>
	
</html>