<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- locationSelector.jsp -->
<html>
<% String app=request.getContextPath(); %>
<head>
<title>Seleccione una localización</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script language="JavaScript">
	function send(selector) {
		if(selector.value!='')
		{
			if(confirm('Ha solicitado conectarse como usuario del Ilustrísimo Colegio de '+selector.value)) {
				window.top.returnValue = selector.value;
			  	window.top.close();		
			}	
		}
		return;
	}
</script>

</head>

<body style="text-align:center"><br>
<p class="titulitos">Por favor, escoja un Colegio:</p>
<p class="labelText">
  <select class="labelText" onChange="send(this);">
    <option value="" selected></option>
    <option value="2032">Colegio que pertenece a la institucion 2032</option>
	<option value="2">Ilustr&iacute;simo Colegio de Gij&oacute;n</option>
	<option value="3">Ilustr&iacute;simo Colegio de M&aacute;laga</option>
	<option value="4">Ilustr&iacute;simo Colegio de Melilla</option>
	<option value="5">Ilustr&iacute;simo Colegio de Murcia</option>
	<option value="6">Ilustr&iacute;simo Colegio de Zaragoza</option>						
  </select></p>
</body>
</html>
