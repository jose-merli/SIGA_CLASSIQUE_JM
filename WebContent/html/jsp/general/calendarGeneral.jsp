<!-- calendarGeneral.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@taglib uri =	"struts-bean.tld"   prefix="bean" %>
<%@taglib uri =	"struts-html.tld" 	prefix="html" %>
<%@taglib uri = "libreria_SIGA.tld" prefix="siga" %>

<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>

<%
	String valor = request.getParameter("valor");
	if (valor == null) valor = new String("");
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String sHoy = UtilidadesString.getMensajeIdiomaCombo (user, "calendario.literal.hoy");
%>

<html:html>

<head>
	<%String app = request.getContextPath();%>
	<title>Calendario</title>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<link rel="stylesheet" type="text/css" href="<%=app%>/html/css/calendar.css">	
	
	<script language="JavaScript" src="<%=app%>/html/js/calendarJs.jsp" ></script>
	<script language="JavaScript" src="<%=app%>/html/js/validation.js" type="text/jscript"></script>
</head>

<body onLoad="load('<%=valor%>');">
	<FORM NAME="calControl" onSubmit="return false;">
	  <TABLE align="center" CELLPADDING="0" CELLSPACING="1" BORDER="0">
	    <TR>
	      <TD width="277" COLSPAN="7">
	      	  <center>
		          <select name="month" onChange="setMonth()" class="boxCombo">
				            <option><siga:Idioma key="calendario.literal.mes.enero"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.febrero"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.marzo"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.abril"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.mayo"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.junio"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.julio"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.agosto"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.septiembre"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.octubre"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.noviembre"/></option>
				            <option><siga:Idioma key="calendario.literal.mes.diciembre"/></option>
		          </select>
		          <INPUT NAME="year" TYPE=TEXT class="boxCombo"  onBlur="setYear()" onKeypress="filterChars(this, false, false);" onchange="isFourDigitYear(this.value)" value="" SIZE=4 MAXLENGTH=4>
	         </center>
	        </TD>
	    </TR>
	    <tr>
	      <td style="height:2px"></td>
	    </tr>
	    <TR> 
	      <TD COLSPAN="7" nowrap>
		      	<div align="center"> 
			          <INPUT TYPE=BUTTON class="button" NAME="previousYear"  value="<<"        onClick="setPreviousYear()">
			          <INPUT TYPE=BUTTON class="button" NAME="previousMonth" value=" < "       onClick="setPreviousMonth()">
			          <INPUT TYPE=BUTTON class="button" NAME="today"         value="<%=sHoy%>" onClick="setToday()">
			          <INPUT TYPE=BUTTON class="button" NAME="reset"         value="Reset"     onClick="setReset()">
			          <INPUT TYPE=BUTTON class="button" NAME="nextMonth"     value=" > "       onClick="setNextMonth()">
			          <INPUT TYPE=BUTTON class="button" NAME="nextYear"      value=">>"        onClick="setNextYear()">
		        </div>
	       </TD>
	    </TR>
	  </TABLE>
	</FORM>

	<div id="detail">
	</div>

</body>

</html:html>