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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		
		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/CER_MantenimientoCertificados.do" method="POST" target="mainWorkArea">
			<html:hidden  styleId = "modo"  property = "modo" value = ""/>
			<html:hidden  styleId = "hiddenFrame"  property = "hiddenFrame" value = "1"/>
			<html:hidden  styleId = "certificado"  property = "certificado" value = "<%=sCertificado%>"/>
			<input type="hidden" id="actionModal" name="actionModal" value="">
		</html:form>

		
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="certificados.mantenimiento.literal.certificado,certificados.mantenimiento.literal.tipoCertificado,&nbsp;"
		   		  columnSizes="70,20,10">

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
			</siga:Table>

	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>