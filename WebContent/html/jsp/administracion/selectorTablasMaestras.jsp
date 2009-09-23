<!-- selectorTablasMaestras.jsp -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<select name="tableName" class="boxCombo">
<%
	if (request.getAttribute("container") != null)
	{
    	Enumeration tables = ((Vector) request.getAttribute("container")).elements();
        while(tables.hasMoreElements())
        {
        	Row table = (Row)tables.nextElement();
%>
	<option value="<%=table.getString(SIGAMasterTable.FN_ID_MASTER_TABLE)%>" action="<%=table.getString(SIGAMasterTable.FN_DESC_ACTION_PATH)%>"><%=table.getString(SIGAMasterTable.FN_DESC_TABLE_ALIAS)%></option>
<%      }
	}
%>
</select> 
