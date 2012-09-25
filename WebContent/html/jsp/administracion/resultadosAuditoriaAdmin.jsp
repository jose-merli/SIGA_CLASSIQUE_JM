<!-- resultadosAuditoriaAdmin.jsp -->
<%@page import="java.util.List"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAAuditoriaAdmin"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	String modoaux=(String)request.getAttribute("modo");
	String messageName = (String)request.getAttribute("descOperation");
	List listaResultado = new ArrayList();
%>
<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
</head>
<body class="tablaCentralCampos">
<%
	if (modoaux.equals("borrar"))
	{
	%>
		<script>
			window.parent.auditoriaAdminForm.reset();
			var type = '<siga:Idioma key="<%=messageName%>"/>';
			alert(type);
		</script>
	<%
	}else{
		if (request.getAttribute("Resultado") == null || ((List)request.getAttribute("Resultado")).size() <1 )
	   		{
	 %>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 <%		}
	 	else
	 		{
	 %>
			<siga:TablaCabecerasFijas
  				nombre="cabecera"
  				borde="2"
  				estilo=""
		   		clase="tableTitle"
  				nombreCol="administracion.auditoria.institucion,administracion.auditoria.diripservidor,administracion.auditoria.diriplocal,administracion.auditoria.usuario,administracion.auditoria.nif,administracion.auditoria.rol,administracion.auditoria.fechayhora,administracion.auditoria.accion"
   				tamanoCol="14,9,9,13,8,10,11,19"
		   			alto="100%"

  				>
	<%
				List en = (List)request.getAttribute("Resultado");
				for (int i=0; i<en.size(); i++)
					{
	    			String linea = (String) en.get(i);
	    			String[] cadena = SIGAAuditoriaAdmin.obtenerCadenas(linea, ";");
	%>
	   					<tr class="listaNonEdit">
  						<td style="text-align:center">
  							<%=	cadena[0]%>
						</td>
  						<td style="text-align:center">
  							<%=	cadena[1]%>
						</td>
  						<td style="text-align:center">
  							<%=	cadena[2]%>
						</td>
  						<td style="text-align:center">
  							<%=	cadena[3]%>
						</td>
  						<td style="text-align:center">
  							<%=	cadena[4]%>
						</td>
  						<td style="text-align:center">
  							<%=	cadena[5]%>
						</td>
  						<td style="text-align:center">
  							<%=com.atos.utils.GstDate.getFechaLenguaje("",cadena[6])%>
						</td>
						<td style="text-align:left">
  	<%						if (cadena[7]==null || cadena[7].trim().equals(""))
  	%>
  								&nbsp;
  	<%
							else
	%>
  								<%=	cadena[7]%>
						</td>
					</tr>
	<%				}
	%>
				  </siga:TablaCabecerasFijas>

	<%	}
	}%>
</body>
</html>