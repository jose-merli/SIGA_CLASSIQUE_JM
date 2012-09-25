<!-- tablasMaestras_Edit.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.form.SIGAAdmGenericMTForm"%>
<%@ page import="com.atos.utils.ColumnConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<% String app=request.getContextPath(); %>

<html>
	<head>
		<%
		String targetName="submitArea";
		String hiddenFrame="1";
		String titulo="";
		String mensajeConfirmacion="";
		
		String mode=(String)request.getAttribute("mode");
	
		if(mode.equals("query"))
		{
			titulo="messages.queryData";
		}
		
		else if (mode.equals("searching"))
		{
			targetName="registros";
			hiddenFrame="0";
			titulo="messages.searchRecords";
		}
		
		else if(mode.equals("insert"))
		{
			titulo="messages.createRecord";
			mensajeConfirmacion="messages.confirm.createRecord";
		}
		
		else if(mode.equals("update"))
		{
			titulo="messages.updateData";
			mensajeConfirmacion="messages.confirm.updateData";
		}
		
		String sMaxLengthCodigo = (String)request.getAttribute("codeLength");
		String sMaxLengthDescripcion = (String)request.getAttribute("descLength");
		String sLengthCodigo = sMaxLengthCodigo;
		String sLengthDescripcion = Integer.parseInt(sMaxLengthDescripcion)>100 ? "100" : sMaxLengthDescripcion;
		String sTipoCodigo = (String)request.getAttribute("tipoCodigo");
		%>
		
		<title><siga:Idioma key="administracion.catalogos.titulo"/></title>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script language="JavaScript">
			function inicio()
			{
			<% if(mode.equals("update"))
			   { %>
				document.all.tablaMaestraForm.code.readOnly=true;
				document.all.tablaMaestraForm.code.className='boxDisabled';
			<% } %>
			
				return true;
			}
		
			function cancelar()
			{
				if(confirm('<siga:Idioma key="messages.confirm.cancel"/>'))
				{
					document.all.tablaMaestraForm.reset();
				}
				
				return false;
			}
			
			function aceptar()
			{
				<%if(mode.equals("insert") ||mode.equals("update")) {%>
					if(!confirm('<siga:Idioma key="<%=mensajeConfirmacion%>"/>'))
					{
						return false;
					}
					
					else
					{
						if ("<%=sTipoCodigo%>"=="N")
						{
							if(isNaN(document.all.tablaMaestraForm.code.value))
							{
								alert('<siga:Idioma key="errors.byte" arg0="Código"/>');
								return false;
							}
						}
					}
				<% } %>		
				document.all.tablaMaestraForm.submit();
			
				return false;
			}
		</script>
	</head>
	
	<body onLoad="inicio();" >
	
	
		<html:form action="/editarTablaMaestra.do" target="<%=targetName%>">
			<input type="hidden" name="mode" value="<%=mode%>">
			<input type="hidden" name="hiddenFrame" value="<%=hiddenFrame%>">
			<html:hidden name="tablaMaestraForm" property="tableName"></html:hidden>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr><td class="labelText" colspan="6">&nbsp;</td></tr>
				<tr>
					<td colspan="6" class="titulitos" style="text-align:center">
						<siga:Idioma key="<%=titulo%>"/>
					</td>
				</tr>
				<tr><td class="labelText" colspan="6">&nbsp;</td></tr>
				<tr>
					<td class="labelText" width="16%">&nbsp;</td>
					<td class="labelText" width="16%" style="text-align:right" nowrap>
						<siga:Idioma key="general.code"/>
					</td>
					<td class="nonEdit"  width="17%" nowrap>
						<% if (mode.equals("query")) { %>
							<bean:write name="tablaMaestraForm" property="code"/>
						<% } else { %>
							<html:text name="tablaMaestraForm" property="code" styleClass="boxCombo" maxlength="<%=sMaxLengthCodigo%>" size="<%=sLengthCodigo%>"/>
						<% } %>
					</td>
					<td class="labelText" width="17%">&nbsp;</td>
				</tr>
				<tr><td class="labelText" colspan="6">&nbsp;</td></tr>
				<tr>
					<td class="labelText">&nbsp;</td>
					<td class="labelText" style="text-align:right" nowrap><siga:Idioma key="general.description"/></td>
					<td class="nonEdit">
						<% if (mode.equals("query")) { %>
							<bean:write name="tablaMaestraForm" property="description"/>
						<% } else { %>
							<html:text name="tablaMaestraForm" property="description" styleClass="boxCombo" maxlength="<%=sMaxLengthDescripcion%>" size="<%=sLengthDescripcion%>"/>
						<% } %>
					</td>
					<td class="labelText">&nbsp;</td>
				</tr>
		<% if(!mode.equals("query"))
		   { %>
				<tr>
					<td property="submitButton" class="labelText" colspan="6" align="center" nowrap>
					</td>
				</tr>
		<% } %>
			</table>
								<div id="buttons" style="position:absolute; top:90%; left:0%; height:6%; width=100%; text-align:center">
					<html:button property="submitButton"  onclick="return aceptar();" styleClass="button">
						<siga:Idioma key="general.submit"/>
					</html:button>&nbsp;
					<html:button property="cancelButton" onclick="return cancelar();" styleClass="button">
						<siga:Idioma key="general.cancel"/>
					</html:button>
					</div>
			
		</html:form>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>