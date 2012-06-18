<!--  alert.jsp  -->
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<!-- JSP -->
<% 
	String app=request.getContextPath();
%>
<html>
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
	
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
		window.top.returnValue = true;
		
		function fixHeight(){			
			window.innerHeight = $(document).height();
		};
	</script>
</head>
<body onload="fixHeight();">
	<table class="sigaPopupTable">
		<tbody>
			<tr>
				<td class="sigaPopupImageContainer">
					<img class="sigaPopupImage" src="<%=app%>/html/imagenes/alert.png"/>
				</td>
				<td class="sigaPopupTextContainer">
					<span id="message" class="labelTextValue"></span>
				</td>				
			</tr>
			<tr>
				<td class="sigaPopupInputContainer" colspan="2">
					<input type="button" id="acceptButton" class="button sigaPopupInput" onClick="window.top.close();" />
				</td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript">
		$("#message",document).append(window.dialogArguments[0]);
		$("head",document).append("<title>"+window.dialogArguments[1]+"</title>");
		$("#acceptButton",document).attr("value",window.dialogArguments[2]);
	</script>
</body>
</html>