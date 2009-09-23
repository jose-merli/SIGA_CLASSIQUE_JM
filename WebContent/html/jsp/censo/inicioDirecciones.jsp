<!-- inicioDirecciones.jsp -->
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>	
	
<html>
<head>
	<title>"<prueba>"</title>	
	
<script language="JavaScript">
	function buscar() 
	{								
		document.all.busqueda.submit();		
	}
	</script>
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr><td class="rayita"></td></tr>
	</table>
	<p class="titulos" style="text-align:right">"<prueba>"&nbsp</p>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr><td class="rayita"></td></tr>
	</table>
		<!--form name="busqueda" method="POST" action="/SIGA/CEN_Historico.do" method="POST"-->
		<form name="busqueda" method="POST" action="/SIGA/CEN_DatosColegiales.do" method="POST">

		<input type="hidden" name="modo" value="">
		<!--input type="hidden" name="modo" value="ver"-->
		<table width="95%" border="0">
			<tr>
			<td>
				</td>				
				<td>
							ID persona
				</td>				
				<td>
					<input type="text" name="idPersona" value="1000">
				</td>
				<td>
							ID institucion
				</td>				
				<td>
					<input type="text" name="idInstitucion" value="2032">
				</td>
				<td>
							Accion: consulta, edicion, nuevo
				</td>				
				<td>
					<input type="text" name="accion" value="nuevo">
				</td>
			</tr>
		</table>
		
			<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr> 
					<td colspan="2">&nbsp;</td>
					<td colspan="2" align="center">
					<html:button property="searchButton"  onclick="return buscar();" styleClass="button">
						<siga:Idioma key="general.search"/>
					</html:button>
					<td colspan="2">&nbsp;</td>	
				</tr>	   
			</table>
		<br>	
			
</form>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!--<iframe name="submitArea"></iframe>-->
</body>
</html>
