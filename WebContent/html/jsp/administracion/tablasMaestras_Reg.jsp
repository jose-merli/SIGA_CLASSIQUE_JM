<!-- tablasMaestras_Reg.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.*"%>

<% String app=request.getContextPath(); %>
<% String acceso=((UsrBean)request.getSession().getAttribute("USRBEAN")).getAccessType();%>
<html>
	<head>
		<title><siga:Idioma key="administracion.catalogos.titulo"/></title>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<script language="JavaScript">
			var bHayDatos;
			//Change the color of the selected row to delete or modify
			function selectRow(rowIndex)
			{
				tablaDatos.rows[rowIndex].className = 'nonEditSelected';
	 		}

 			//Put all rows style as non selected
 			function returnClass()
 			{
 				if(bHayDatos)
 				{
	 				for (var i=0; i<tablaDatos.rows.length; i++)
	 				{
						//tablaDatos.rows[i].className = 'nonEdit';
						tablaDatos.rows[i].className = 'ListanonEdit';
					}
				}
 			}

			function enviar(rowIdx, accion)
			{
				var codVal='';
				var lengVal='';
				
				if(rowIdx!=-1)
				{
					returnClass();
					selectRow(rowIdx);

					codVal=tablaDatos.rows[rowIdx].cells[0].all[0].value;
				}
				
				else
				{
					returnClass();
					codVal='';
					lengVal='';				
				}  
				
				if(accion=='delete')
				{
					if(!confirm('<siga:Idioma key="messages.deleteConfirmation"/>'))
					{
						return false;
					}
					
					document.all.tablaMaestraForm.target='submitArea';
					document.all.tablaMaestraForm.hiddenFrame.value='1';
				}
				
				else
				{
					document.all.tablaMaestraForm.target='editar';
					document.all.tablaMaestraForm.hiddenFrame.value='0';
				}
				
				document.all.tablaMaestraForm.code.value=codVal;
				document.all.tablaMaestraForm.mode.value=accion;
				document.all.tablaMaestraForm.submit();
				
				return false;
			}

			function inicio()
			{
				MM_preloadImages('<%=app%>/html/imagenes/consulta_ilum.gif',
								 '<%=app%>/html/imagenes/actualizar_ilum.gif',
								 '<%=app%>/html/imagenes/actualizar_prohibido.gif',
								 '<%=app%>/html/imagenes/borrar_ilum.gif',
								 '<%=app%>/html/imagenes/borrar_prohibido.gif');
			}
		</script>
	</head>

	<body onLoad="inicio();" class ='tablaCentralCampos'>
		<table width="100%" border="0" cellspacing="2" cellpadding="2">
  			<tr>
  				<td align=center class="titulitos">&nbsp;&nbsp;&nbsp;<siga:Idioma key="administracion.catalogos.datosDe"/> <bean:write name="tablaMaestraForm" property="tableDescription"/></label></td>
  			</tr>

			<html:form action="listadoTablaMaestra.do" method="post" target="editar">
				<input type="hidden" name="hiddenFrame" value="0">
				<input type="hidden" name="lockFlag" value="1">
				<input type="hidden" name="mode" value="">
				<html:hidden name="tablaMaestraForm" property="tableName"></html:hidden>
				<html:hidden name="tablaMaestraForm" property="code" value=""></html:hidden>
				<html:hidden name="tablaMaestraForm" property="languageCode" value=""></html:hidden>
			</html:form>
		</table>

		<%
	    if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 )
	    {
		%>
			<script>bHayDatos=false;</script>
			<br><br><br>
			<p class="Title" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
		<%
	    }
	    
	    else
	    { %>
		<script>bHayDatos=true;</script>
		<table id="idTabla" width="100%" border="1"  cellspacing="0" cellpadding="0">
			<tr>
				<td width="10%" class="tableTitle" nowrap><siga:Idioma key="general.code"/></td>
				<td width="80%" class="tableTitle" nowrap><siga:Idioma key="general.description"/></td>
				<td width="10%" class="tableTitle">&nbsp;</td>
			</tr>
		</table>

		<div id="tabla" style="position:absolute; width:100%; height:135px; overflow-y:auto">
			<table id="tablaDatos" width="100%" border="1" align="center" cellspacing="0" cellpadding="0">
	    	<%Enumeration en = ((Vector)request.getAttribute("container")).elements();
			int recordNumber=0;
			while (en.hasMoreElements())
			{
	            Row row = (Row) en.nextElement(); %>
			<tr class="listaNonEdit">
				<td width="10%" style="text-align:center" nowrap><input type="hidden" name="code" value="<%=row.getString(SIGAMasterTable.ALIAS_CODE_FIELD)%>"><%=row.getString(SIGAMasterTable.ALIAS_CODE_FIELD)%></td>
				<td width="80%" style="text-align:center"><%=row.getString(SIGAMasterTable.ALIAS_DESC_FIELD)%></td>
				<td width="10%" style="text-align:center" nowrap>
					<a href="javascript://" onClick="enviar(<%=recordNumber%>, 'query');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('consulta_<%=recordNumber%>','','<%=app%>/html/imagenes/consulta_ilum.gif',1)"><img src="<%=app%>/html/imagenes/consulta.gif" alt="Ver Datos" name="consulta_<%=recordNumber%>" width="26" height="27" border="0"></a>
					<% if (acceso.equals(SIGAPTConstants.ACCESS_FULL)) { %>
					<a href="javascript://" onClick="enviar(<%=recordNumber%>, 'edit');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('edit_<%=recordNumber%>','','<%=app%>/html/imagenes/actualizar_ilum.gif',1)"><img src="<%=app%>/html/imagenes/actualizar.gif" alt="Editar" name="edit_<%=recordNumber%>" width="26" height="27" border="0"></a>
					<a href="javascript://" onClick="enviar(<%=recordNumber%>, 'delete');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('borrar_<%=recordNumber%>','','<%=app%>/html/imagenes/borrar_ilum.gif',1)"><img src="<%=app%>/html/imagenes/borrar.gif" alt="Borrar" name="borrar_<%=recordNumber%>" width="26" height="27" border="0"></a>
					<%} else {%>
					<img src="<%=app%>/html/imagenes/actualizar_prohibido.gif" alt="Editar" name="edit_<%=recordNumber%>" width="26" height="27" border="0">
					<img src="<%=app%>/html/imagenes/borrar_prohibido.gif" alt="Borrar" name="borrar_<%=recordNumber%>" width="26" height="27" border="0">
					<%}%>
				</td>		
			</tr>
			<% recordNumber++; %>
			<% } %>
			</table>
		</div>

		<script>
			if (document.all.tabla.clientHeight < document.all.tablaDatos.clientHeight)
			{
				idTabla.width="98.43%";
			}
		</script>

		<div id="buttons" style="position:absolute; top:90%; left:0%; height:6%; width=100%; text-align:center">
			<%if (acceso.equals(SIGAPTConstants.ACCESS_FULL))
			{%>
			<html:button property="newButton" onclick="enviar(-1, 'new');" styleClass="button">
				<siga:Idioma key="general.new"/>
			</html:button>&nbsp;
			<%}%>
			<html:button property="searchButton" onclick="enviar(-1, 'search');" styleClass="button">
				<siga:Idioma key="general.search"/>
			</html:button>
		</div>
	<%	} %>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>