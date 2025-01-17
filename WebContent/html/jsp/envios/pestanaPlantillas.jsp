<!DOCTYPE html>
<html>
<head>
<!-- pestanaPlantillas.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<% 
	String app=request.getContextPath();
	
	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoEnvio = (String)request.getAttribute("idTipoEnvio");
	String sIdPlantillaEnvios = (String)request.getAttribute("idPlantillaEnvios");
	
	String plantilla=(String)request.getAttribute("plantilla");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String idTipoEnvios = (String)request.getAttribute("idTipoEnvios");

	
%>	

<%@page import="java.util.Vector"%>

	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Volver
			function accionVolver()
			{
				PlantillasEnviosPlantillasForm.action = "/SIGA/ENV_DefinirPlantillas.do?noreset=true";
				PlantillasEnviosPlantillasForm.modo.value="abrirConParametros";
				PlantillasEnviosPlantillasForm.submit();
			}
	
			//Asociada al boton Nuevo
			function accionNuevo()
			{
				PlantillasEnviosPlantillasForm.action = "/SIGA/ENV_Plantillas_Salida.do";
				PlantillasEnviosPlantillasForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("PlantillasEnviosPlantillasForm","P");
				
				if (resultado=="MODIFICADO")
				{
					parent.buscar();
				}
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

			function download(fila)
			{
		
				var idInstitucion = document.getElementById("oculto"+fila+"_1").value;
				var idTipoEnvios = document.getElementById("oculto"+fila+"_2").value;
				var idPlantillaEnvios = document.getElementById("oculto"+fila+"_3").value;
				var idPlantilla = document.getElementById("oculto"+fila+"_4").value;
				
				document.forms[0].idInstitucion.value = idInstitucion;
				document.forms[0].idTipoEnvios.value = idTipoEnvios;
				document.forms[0].idPlantillaEnvios.value = idPlantillaEnvios;
				document.forms[0].idPlantilla.value =	idPlantilla	;
				
				document.forms[0].target="submitArea";
				document.forms[0].modo.value = "download";
				document.forms[0].submit();
				document.forms[0].target="mainWorkArea";
			}
			function descargar(fila) 
			{
				var idInstitucion = document.getElementById("oculto"+fila+"_1").value;
				var idTipoEnvios = document.getElementById("oculto"+fila+"_2").value;
				var idPlantillaEnvios = document.getElementById("oculto"+fila+"_3").value;
				var idPlantilla = document.getElementById("oculto"+fila+"_4").value;
				
				document.forms[0].idInstitucion.value = idInstitucion;
				document.forms[0].idTipoEnvios.value = idTipoEnvios;
				document.forms[0].idPlantillaEnvios.value = idPlantillaEnvios;
				document.forms[0].idPlantilla.value =	idPlantilla	;
				
				document.forms[0].target="submitArea";
				document.forms[0].modo.value = "descargar";
				document.forms[0].submit();
				document.forms[0].target="mainWorkArea";
			   	//ProgramacionForm.submit();
			   	//ProgramacionForm.modo.value = "modificar";
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirTiposPlantillas.plantillas.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
		/>
	</head>

	<body>
	
		<html:form action="/ENV_Plantillas_Salida.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value=""/>

			<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
			<html:hidden property="idTipoEnvio" value="<%=sIdTipoEnvio%>"/>
			<html:hidden property="idPlantillaEnvios" value="<%=sIdPlantillaEnvios%>"/>
			<html:hidden property="idPlantilla" />			
			<html:hidden property="descripcionPlantilla" value="<%=descripcionPlantilla%>"/>
			<html:hidden property="idTipoEnvios" value="<%=idTipoEnvios%>"/>
			
		</html:form>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.plantillas.literal.plantilla"/>:&nbsp;<%=plantilla%>
			</td>
		</tr>
	</table>				

			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="certificados.mantenimiento.literal.plantilla,certificados.mantenimiento.literal.pordefecto,certificados.mantenimiento.literal.tipoArchivo,"
		   		  columnSizes="50,15,10,19"
		   		  modal="P">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}
				
				else
				{
					String sBotones = bEditable ? "E,B" : "";
					
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		//CerPlantillasBean bean = (CerPlantillasBean)vDatos.elementAt(i);
				  		EnvPlantillaGeneracionBean bean = (EnvPlantillaGeneracionBean)vDatos.elementAt(i);
				  		
						FilaExtElement[] elems=new FilaExtElement[2];
						elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);
						elems[1]=new FilaExtElement("enviar","descargar",SIGAConstants.ACCESS_READ);

%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta='no'>
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoEnvios()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdPlantillaEnvios()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdPlantilla()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getDescripcion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getPorDefecto()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=bean.getTipoArchivo()%>">
						
						<%=bean.getDescripcion()%>
					</td>
					<td>
<%
					String sPorDefecto = bean.getPorDefecto();
					
					if (sPorDefecto.equals("S"))
					{
%>
						<siga:Idioma key="general.yes"/>
<%
					}
					
					else
					{
%>
						<siga:Idioma key="general.no"/>
<%
					}
%>
					</td>
					<td><%=bean.getTipoArchivo()%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>


<%
		String sBotones2 = bEditable ? "V,N" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>