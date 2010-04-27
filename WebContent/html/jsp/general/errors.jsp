<!-- errors.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.atos.utils.ClsExcBase"%>
<%@ page import="com.atos.utils.ExceptionManager"%>
<% String app=request.getContextPath(); %>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/javascript/SIGA.js" type="text/javascript"></script>
	</head>

	<body>

  	<%
  	try {
	ClsExcBase exc = null;
   	
   	if (session.getAttribute("exception") != null)
   	{
    	exc = (ClsExcBase) session.getAttribute("exception");
   	} 

	if (session.getAttribute("printStackTrace") != null)
	{
		ClsExcBase exc2 = session.getAttribute("exc2")!=null ? (ClsExcBase)session.getAttribute("exc2") : null;
        
        if (exc2!=null)
        {
	%>
		<center><p class="Title">Ha ocurrido el siguiente ERROR: </p></center>
  		
  		<pre><%=exc2.getMessage()%></pre>

		<center><p> al tratar la siguiente Excepción: </p></center>

		<div align="left">
	<%
		}
     }
     
     else
     {
	 	ExceptionManager mgr = (ExceptionManager) session.getAttribute("manager");
        if (mgr!=null)
        {
	%>
		</div>

		<p align="center" class="nonEditSelected"> ERROR en tiempo de ejecución</p>

		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
<!--
  			<tr>
    			<td width="112" class="tableTitle">MÓDULO</td>
				<td width="106" class="nonEdit"><%=mgr.getStream()%></td>
     		</tr>
     		<tr>
    			<td class="tableTitle">PROCESO</td>
    			<td class="nonEdit"><%=mgr.getProcessId() + " - " + mgr.getProcessDesc()%></td>
     		</tr>
   		
     		<tr>
    			<td class="tableTitle">CATEGORÍA ERROR</td>
    			<td class="nonEdit"><%=mgr.getErrorCategory() + " - " + mgr.getDescErrorCategory()%></td>
     		</tr>
     		<tr>
    			<td class="tableTitle">TIPO ERROR</td>
    			<td class="nonEdit"><%=mgr.getErrorType() + " - " + mgr.getDescErrorType()%></td>
     		</tr>
     		<tr>
    			<td width="130"  class="tableTitle">Usuario:</td>
    			<td class="nonEdit"><%=mgr.getUserId()%></td>
     		</tr>
     		
     		<tr>
    			<td width="130"  class="tableTitle">Institucion:</td>
    			<td class="nonEdit"><%=mgr.getInstitucion()%></td>
     		</tr>     		
-->

     		<tr>
    			<td width="130"  class="tableTitle">Descripción Error:</td>
    			<td class="nonEdit"><%=mgr.getSigaErrMsg()%></td>
     		</tr>
<!--
     		<tr>
    			<td class="tableTitle">CÓDIGO USUARIO</td>
    			<td class="nonEdit"><%=mgr.getUserId()%></td>
     		</tr>
-->     		
		</table>
  		
  		<br>
	<%
    		Exception e=mgr.getNextException();
    		
    		if (e!=null)
    		{
	%>
		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
    			<td width="112" class="tableTitle" >Nested</td>
	<%
				Exception nested=e;
      			
      			while (nested!=null)
      			{
	%>
        		<td class="nonEdit" ><%=nested.getMessage()%></td>
	<%
					if (nested instanceof ClsExcBase)
					{
          				nested=((ClsExcBase)nested).getNextException();
        			}
        			
        			else
        			{
          				nested=null;
        			}
      			}
	%>
		</table>
	<%
    		}
	%>
		<center>
  			<font color="red">
     			<p> Pila Completa (Stack Trace): </p>
  			</font>
		</center>
	<%
		}
	}
	%>
    	<pre>
	<%
    if (exc != null)
    {
    	out.println(exc.getMessage());
    }
    } catch (Exception e) {
    	//System.out.println(e.toString());
    }
	%>
		</pre>
	</body>
</html>