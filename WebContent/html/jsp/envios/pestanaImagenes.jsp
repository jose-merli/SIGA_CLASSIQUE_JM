<!-- pestanaImagenes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.envios.form.ImagenPlantillaForm"%>
<%@ page import="com.siga.beans.EnvPlantillasEnviosBean"%>

<%
	ImagenPlantillaForm formPlantilla = (ImagenPlantillaForm)request.getAttribute("ImagenPlantillaForm");
	EnvPlantillasEnviosBean envPlantilla = formPlantilla.getPlantillaEnvios();
	String plantilla = envPlantilla.getNombre();
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		

		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Volver
			function accionVolver()
			{
				document.ImagenPlantillaForm.action = document.ImagenPlantillaForm.volverAccion.value;
				document.ImagenPlantillaForm.modo.value=document.ImagenPlantillaForm.volverModo.value;
				document.ImagenPlantillaForm.submit();
			}
	
			//Asociada al boton Nuevo
			function accionNuevo() {
				document.ImagenPlantillaForm.action = "/SIGA/ENV_ImagenesPlantilla.do";
				document.ImagenPlantillaForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("ImagenPlantillaForm","P");
				if (resultado=="MODIFICADO") {
					parent.buscar();
				}
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				preparaDatos(fila, 'tablaDatos', datos);
				document.forms[0].target="submitArea";
				document.forms[0].modo.value = "download";
				document.forms[0].submit();
				document.forms[0].target="mainWorkArea";
				
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirTiposPlantillas.imagenes.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
		/>
	</head>

	<body>
	
		<html:form action="/ENV_ImagenesPlantilla" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value=""/>
			<html:hidden property="idInstitucion"/>
			<html:hidden property="idTipoEnvios"/>
			<html:hidden property="editable"/>
			<html:hidden property="volverAccion"/>
			<html:hidden property="volverModo"/>
			<html:hidden property="editable"/>
			<html:hidden property="idPlantillaEnvios"/>

		</html:form>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.plantillas.literal.plantilla"/>:&nbsp;<%=plantilla%>								
			</td>
		</tr>
	</table>				
<bean:define id="botones" name="ImagenPlantillaForm" property="botones" type="java.lang.String"/>
<bean:define id="botonesFila" name="ImagenPlantillaForm" property="botonesFila" type="java.lang.String"/>
<bean:define id="elementosFila" name="ImagenPlantillaForm" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.imagenes.literal.nombre,envios.imagenes.literal.tipoArchivo,envios.imagenes.literal.incrustado,"
		   		  columnSizes="50,15,10,19"
		   		  modal="P">
		   		  
		   		  <logic:empty name="ImagenPlantillaForm"	property="imagenes">
		   		  <tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		   		  </logic:empty>
		   		 <logic:notEmpty name="ImagenPlantillaForm"	property="imagenes">
					<logic:iterate name="ImagenPlantillaForm" property="imagenes" id="imagen" indexId="index">
										
				<%
					ImagenPlantillaForm formImagen = formPlantilla.getImagenes().get(index);
					index = index.intValue()+1;										 
				%>
				<input type="hidden" id="oculto<%=index%>_1" value="<bean:write name="imagen" property="idInstitucion" />">
				<input type="hidden" id="oculto<%=index%>_2" value="<bean:write name="imagen" property="idTipoEnvios" />">
				<input type="hidden" id="oculto<%=index%>_3" value="<bean:write name="imagen" property="idPlantillaEnvios" />">
				<input type="hidden" id="oculto<%=index%>_4" value="<bean:write name="imagen" property="idImagen" />">
	  			<siga:FilaConIconos fila='<%=String.valueOf(index.intValue())%>' botones='<%=botonesFila%>'elementos='<%=elementosFila%>' clase="listaNonEdit" visibleConsulta='no'>
					<td><%=formImagen.getNombre()%></td>
					<td><%=formImagen.getTipoArchivo()%></td>
					<td><%=formImagen.getEmbebedTxt()%></td>
				</siga:FilaConIconos>

					</logic:iterate>
				</logic:notEmpty>
			</siga:Table>

	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/>

		<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>
	</body>
</html>