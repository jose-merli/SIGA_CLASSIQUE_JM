<!DOCTYPE html>
<html>
<head>
<!--  confirm.jsp  -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- JSP -->
<% 
	String app=request.getContextPath();
%>


	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		window.top.returnValue = false;
		
		function acceptAndClose(){
			window.top.returnValue = true;
			window.top.close();
		}
		
		function cancelAndClose(){
			window.top.returnValue = false;
			window.top.close();
		}
		
		function fixHeight(){			
			window.innerHeight = $(document).height();
		};
	</script>
</head>
<body onload="fixHeight();">
	<table class="sigaPopupTable labelText">
		<tbody>
			<tr>
				<td class="sigaPopupImageContainer">
					<img class="sigaPopupImage" src="<%=app%>/html/imagenes/question.png"/>
				</td>
				<td class="sigaPopupTextContainer">
					<span id="message"></span>
				</td>				
			</tr>
			<tr>
				<td class="sigaPopupInputContainer" colspan="2">
					<input type="button" id="acceptButton" class="button sigaPopupInput" onClick="acceptAndClose();" />
					<input type="button" id="cancelButton" class="button sigaPopupInput" onClick="cancelAndClose();" />
				</td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript">
		$("#message",document).append(window.dialogArguments[0]);
		$("head",document).append("<title>"+window.dialogArguments[1]+"</title>");
		$("#acceptButton",document).attr("value",window.dialogArguments[2]);
		$("#cancelButton",document).attr("value",window.dialogArguments[3]);
	</script>
</body>
</html>