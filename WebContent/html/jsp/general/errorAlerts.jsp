<!-- errorAlerts.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.ClsExcBase"%>
<%@ page import="com.atos.utils.ExceptionManager"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%  String app=request.getContextPath(); %>
<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<script>
	<%
		String msg="Error general en SIGA";
		try {
	   		ExceptionManager mgr = (ExceptionManager) session.getAttribute("manager");
			
   		
   			if (mgr!=null) {
	   			msg=UtilidadesString.escape(mgr.getSigaErrMsg());
		   	}
		  } catch (Exception e) {
		    //e.printStackTrace();
		  }
	%>

   			function doShowError()
   			{
     			var type =unescape('<%=msg%>');
     			alert(type);
     			return false;
   			}
   			
		</script>
		
	</head>
	
	<body onLoad="doShowError();">
<script>
	fin(window.parent.parent.document);
</script>
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
        
    }
    else
    {
	 	ExceptionManager mgr = (ExceptionManager) session.getAttribute("manager");
        if (mgr!=null)
        {
	%>
		<br>		
		<br>
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				ERROR en tiempo de ejecución
			</td>
		</tr>
	</table>		  		
		<br>
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

<!--  		
  		<br>
	<%
    		Exception e=mgr.getNextException();
    		
    		if (e!=null)
    		{
	%>
		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
    			<td width="130" class="tableTitle" >Nested</td>
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
    	//e.printStackTrace();
    }
	%>
		</pre>
-->		

<br>

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesAccion botones="V"  />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function accionVolver() 
		{		
			history.go(-1);
		}

		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
		
	</body>
</html>
