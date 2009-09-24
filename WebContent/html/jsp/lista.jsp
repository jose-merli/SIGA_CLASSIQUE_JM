<!-- lista.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri= "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*,com.atos.utils.*"%>
<html>

<body>

<% if(request.getParameter("submit")==null) {%> 

<html:form action="/lista" method="get" >

Funcion 1<input type="checkbox" name="lista[0].automatico"/>
<input type="hidden" name="lista[0].funcion" value="Funcion 1"/>
<br/>
Funcion 2<input type="checkbox" name="lista[1].automatico"/>
<input type="hidden" name="lista[1].funcion" value="Funcion 2"/>
<br/>
Funcion 3<input type="checkbox" name="lista[2].automatico"/>
<input type="hidden" name="lista[2].funcion" value="Funcion 3"/>
<br/>
Funcion 4<input type="checkbox" name="lista[3].automatico"/>
<input type="hidden" name="lista[3].funcion" value="Funcion 4"/>
<br/>
Funcion 5<input type="checkbox" name="lista[4].automatico"/>
<input type="hidden" name="lista[4].funcion" value="Funcion 5"/>
<br/>
<br/>
<input type="hidden" name="submit" value="yes"/>

<html:submit/>
</html:form>

<%} else{%>

<logic:iterate id="elemento" name="formulariochecks" property="todos" indexId="index">
<bean:write name="elemento" property="funcion"/>--->
<bean:write name="elemento" property="automatico"/> 
<br>

</logic:iterate>
<%};%>


</body>
</html>