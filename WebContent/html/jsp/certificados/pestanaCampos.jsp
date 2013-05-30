<!-- pestanaCampos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			// Asociada al boton Volver -->
			function accionVolver()
			{
				MantenimientoCertificadosCamposForm.action = "/SIGA/CER_MantenimientoCertificados.do";
				MantenimientoCertificadosCamposForm.modo.value="abrirConParametros";
				MantenimientoCertificadosCamposForm.submit();
			}
	
			//Asociada al boton Nuevo -->
			function accionNuevo()
			{
				MantenimientoCertificadosCamposForm.action = "/SIGA/CER_Campos.do";
				MantenimientoCertificadosCamposForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("MantenimientoCertificadosCamposForm","G");
				
				if (resultado=="MODIFICADO")
				{
					parent.buscar();
				}
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body class="detallePestanas">
		<html:form action="/CER_Campos.do" method="POST" target="mainWorkArea">
			<html:hidden styleId="modo" property="modo" value=""/>

			<html:hidden styleId="idInstitucion" property="idInstitucion" value="<%=sIdInstitucion%>"/>
			<html:hidden styleId="idTipoProducto" property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
			<html:hidden styleId="idProducto" property="idProducto" value="<%=sIdProducto%>"/>
			<html:hidden styleId="idProductoInstitucion" property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
			
			<html:hidden styleId="certificado" property="certificado" value="<%=sCertificado%>"/>
		</html:form>

	<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo" cellspacing="0">		
		<tr>		
			<td class="titulitosDatos">
			<siga:Idioma key="certificados.mantenimiento.literal.certificado"/>:&nbsp;<%=descCertificado%>			</td>				
		</tr>
	</table>		
				
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="certificados.mantenimiento.literal.campo,certificados.mantenimiento.literal.formato,"
		   		  columnSizes="45,45,10"
		   		  modal="M">

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
					String sBotones = bEditable ? "C,E,B" : "C";
					
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Hashtable htDatos = (Hashtable)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" id="oculto<%=""+(i+1)%>_1"  name="oculto<%=""+(i+1)%>_1" value=" <%=htDatos.get(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO)%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_2"  name="oculto<%=""+(i+1)%>_2" value=" <%=htDatos.get(CerFormatosBean.C_IDFORMATO)%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_3"  name="oculto<%=""+(i+1)%>_3" value=" <%=htDatos.get(CerCamposCertificadosBean.C_TIPOCAMPO)%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_4"  name="oculto<%=""+(i+1)%>_4" value=" <%=htDatos.get(CerCamposCertificadosBean.C_CAPTURARDATOS)%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_5"  name="oculto<%=""+(i+1)%>_5" value=" <%=((String)htDatos.get(CerProducInstiCampCertifBean.C_VALOR)).replace(" ","&nbsp")%>">
						
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
			</siga:Table>

<%
		String sBotones2 = bEditable ? "V,N" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>