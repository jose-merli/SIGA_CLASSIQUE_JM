<!DOCTYPE html>
<html>
<head>
<!-- botoneraBusqueda.jsp -->
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<% String app=request.getContextPath(); %>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Botonera Gestión Censal</title>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>


<!-- Incluido jquery en siga.js -->

<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script language="javascript">
var tipo='0';
function iniciar() {
	initStyles();
	//ejecutar accion por defecto:
	var accion = '<%=app%>/html/jsp/censo/busquedaClientes.jsp';
	tipo=getParameterValue('tipo');
	if(tipo!='') {
		accion+='?tipo='+tipo;
	} else {
		tipo='0';
		accion+='?tipo=0';
		
	}
	parent.frames['CuerpoCentralGestion'].location=accion;	
}
</script>
</head>
<body onLoad="iniciar();">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
	<tr align="right" valign="bottom">
		<td width="113" height="21" nowrap>&nbsp;</td>
	</tr>
    <tr> 
      <td colspan="2" width="100%" height="10" class="rayita"></td>      
    </tr>
  </table>
</body>
</html>
