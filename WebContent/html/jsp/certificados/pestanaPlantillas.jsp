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
	HttpSession ses=request.getSession();
	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoProducto = (String)request.getAttribute("idTipoProducto");
	String sIdProducto = (String)request.getAttribute("idProducto");
	String sIdProductoInstitucion = (String)request.getAttribute("idProductoInstitucion");
	
	String sCertificado=(String)request.getAttribute("certificado");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descCertificado = (String)request.getAttribute("descripcionCertificado");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<siga:Titulo titulo="pestana.certificados.plantillas" localizacion="certificados.mantenimientoCertificados.plantillas.localizacion"/>	
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Volver
			function accionVolver()
			{
				MantenimientoCertificadosPlantillasForm.action = "/SIGA/CER_MantenimientoCertificados.do";
				MantenimientoCertificadosPlantillasForm.modo.value="abrirConParametros";
				MantenimientoCertificadosPlantillasForm.target="mainWorkArea";
				MantenimientoCertificadosPlantillasForm.submit();
			}
	
			//Asociada al boton Nuevo
			function accionNuevo()
			{
				document.MantenimientoCertificadosPlantillasForm.modo.value = "nuevo";
				document.MantenimientoCertificadosPlantillasForm.submit();
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
				document.forms[0].target="_self";
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body class="detallePestanas">
		<html:form action="/CER_Plantillas.do" method="POST">
			<html:hidden styleId="modo" property="modo" value=""/>

			<html:hidden styleId="idInstitucion" property="idInstitucion"  value="<%=sIdInstitucion%>"/>
			<html:hidden styleId="idTipoProducto" property="idTipoProducto"  value="<%=sIdTipoProducto%>"/>
			<html:hidden styleId="idProducto"  property="idProducto" value="<%=sIdProducto%>"/>
			<html:hidden styleId="idProductoInstitucion"  property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
			<html:hidden styleId="certificado"  property="certificado" value="<%=sCertificado%>"/>
		</html:form>

	<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo"  cellspacing="0">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="certificados.mantenimiento.literal.certificado"/>:&nbsp;<%=descCertificado%>
		</tr>
	</table>		

			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="certificados.mantenimiento.literal.plantilla,certificados.mantenimiento.literal.pordefecto,"
		   		  columnSizes="72,18,10"
		   		>

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
				  		CerPlantillasBean bean = (CerPlantillasBean)vDatos.elementAt(i);
				  		
						FilaExtElement[] elems=new FilaExtElement[1];
						elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);

%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no" pintarEspacio="false">
					<td>
						<input type="hidden" id="oculto<%=""+(i+1)%>_1" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_2" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoProducto()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_3" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdProducto()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_4" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdProductoInstitucion()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_5" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getIdPlantilla()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_6" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getDescripcion()%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_7" name="oculto<%=""+(i+1)%>_7" value="<%=bean.getPorDefecto()%>">
						
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