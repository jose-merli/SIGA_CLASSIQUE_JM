<!-- buscarListadoCertificados.jsp -->
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
<%@ page import="com.siga.Utilidades.*"%>

<% 

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	String sCertificado = (String)request.getAttribute("certificado");
	
	if (sCertificado==null)
	{
		sCertificado="";
	}
	
	request.removeAttribute("datos");
	request.removeAttribute("certificado");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/CER_MantenimientoCertificados.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "certificado" value = "<%=sCertificado%>"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

		
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="certificados.mantenimiento.literal.certificado,certificados.mantenimiento.literal.tipoCertificado,"
		   		  tamanoCol="70,20,10"
		   		  alto="100%" 
		   		  activarFilaSel="true">
		   		  

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
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		PysProductosInstitucionBean bean = (PysProductosInstitucionBean)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E" clase="listaNonEdit" visibleBorrado="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoProducto()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdProducto()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdProductoInstitucion()%>">
						
						<%=bean.getDescripcion()%>
					</td>
					<td>
<%
						String sTipoCertificado = bean.getTipoCertificado();
						
						if (sTipoCertificado.equals(PysProductosInstitucionBean.PI_COMUNICACION_CODIGO))
						{
%>
							<%=UtilidadesString.getMensajeIdioma(userBean,PysProductosInstitucionBean.PI_COMUNICACION_DESCRIPCION)%>
<%
						}
						
						else if (sTipoCertificado.equals(PysProductosInstitucionBean.PI_DILIGENCIA_CODIGO))
						{
%>
							<%=UtilidadesString.getMensajeIdioma(userBean,PysProductosInstitucionBean.PI_DILIGENCIA_DESCRIPCION)%>
<%
						}
						
						else if (sTipoCertificado.equals(PysProductosInstitucionBean.PI_CERTIFICADO_CODIGO))
						{
%>
							<%=UtilidadesString.getMensajeIdioma(userBean,PysProductosInstitucionBean.PI_CERTIFICADO_DESCRIPCION)%>
<%
						}
						
						else
						{
%>
							&nbsp;
<%
						}
%>
						
					</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>

	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>