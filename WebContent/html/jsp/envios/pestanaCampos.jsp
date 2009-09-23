<!-- pestanaCampos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoEnvio = (String)request.getAttribute("idTipoEnvio");
	String sIdPlantillaEnvios = (String)request.getAttribute("idPlantillaEnvios");
	
	String plantilla = (String)request.getAttribute("plantilla");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descripcionPlantilla = plantilla;
//	String descripcionPlantilla = (String)request.getAttribute("descripcionPlantilla");
	String idTipoEnvios = (String)request.getAttribute("idTipoEnvios");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton Volver -->
			function accionVolver()
			{
				PlantillasEnviosCamposForm.action = "/SIGA/ENV_DefinirPlantillas.do?noreset=true";
				PlantillasEnviosCamposForm.modo.value="abrirConParametros";
				PlantillasEnviosCamposForm.submit();
			}
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo()
			{
				PlantillasEnviosCamposForm.action = "/SIGA/ENV_Campos_Plantillas.do";
				PlantillasEnviosCamposForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("PlantillasEnviosCamposForm","M");
				
				if (resultado=="MODIFICADO")
				{
					parent.buscar();
				}
			}
			
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{	sub();	
				PlantillasEnviosCamposForm.plantilla.value =  document.getElementById('DescripcionPlantilla').value;
				document.forms[0].target="submitArea";
				document.forms[0].submit();
			}
			
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				var elemento=parent.document.getElementById('pestana.envios.campos');
				parent.pulsar(elemento,'mainPestanas')				
			}

			function refrescarLocal()
			{
				/*var elemento=parent.document.getElementById('pestana.envios.campos');
				parent.pulsar(elemento,'mainPestanas')*/
				document.location.reload();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<siga:Titulo
			titulo="envios.definirTiposPlantillas.campos.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
		/>

	</head>

	<body>
	
		<html:form action="/ENV_Campos_Plantillas.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value="buscar"/>

			<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
			<html:hidden property="idTipoEnvio" value="<%=sIdTipoEnvio%>"/>
			<html:hidden property="idPlantillaEnvios" value="<%=sIdPlantillaEnvios%>"/>
			
			<html:hidden property="plantilla" value="<%=plantilla%>"/>
			<html:hidden property="idTipoEnvios" value="<%=idTipoEnvios%>"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.plantillas.literal.plantilla"/>:&nbsp;
				<% if (bEditable){ %>
								<input type="text" id = "descripcionPlantilla" name="descripcionPlantilla" size="60" maxlength="100" class="box" value="<%=plantilla%>"></input>
								
				<%} else {%>					
								<%=plantilla%>
								
				<%}%>
			</td>
		</tr>
	</table>				
			
			
			
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="certificados.mantenimiento.literal.campo,certificados.mantenimiento.literal.formato,"
		   		  tamanoCol="45,45,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  modal="M">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
					String sBotones = bEditable ? "C,E,B" : "C";
					
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Hashtable htDatos = (Hashtable)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value=" <%=htDatos.get(EnvCamposBean.C_IDCAMPO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value=" <%=htDatos.get(CerFormatosBean.C_IDFORMATO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value=" <%=htDatos.get(EnvCamposBean.C_TIPOCAMPO)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value=" <%=htDatos.get(EnvCamposBean.C_CAPTURARDATOS)%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value=" <%=htDatos.get(EnvCamposPlantillaBean.C_VALOR)%>">
						
						<%=htDatos.get(CerCamposCertificadosBean.C_NOMBRE)%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(htDatos, CerFormatosBean.C_DESCRIPCION))%>
					</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>


<%
		String sBotones2 = bEditable ? "V,G,R,N" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>