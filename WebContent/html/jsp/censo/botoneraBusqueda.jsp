<!-- botoneraBusqueda.jsp -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<% String app=request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Botonera Gestión Censal</title>
<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
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
