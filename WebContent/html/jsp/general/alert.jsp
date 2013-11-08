<!DOCTYPE html>
<html>
<head>
<!--  alert.jsp  -->
<%@ page pageEncoding="ISO-8859-1"%>

<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
%>


	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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