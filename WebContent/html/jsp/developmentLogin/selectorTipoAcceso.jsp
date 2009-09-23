<!-- selectorTipoAcceso.jsp -->
<%@ page import="com.siga.administracion.SIGAConstants"%>

<select class = "boxCombo" name="tipoAcceso">
	<option value="3"><%=SIGAConstants.ACCESS_FULL%></option>
	<option value="2"><%=SIGAConstants.ACCESS_READ%></option>
	<option value="1"><%=SIGAConstants.ACCESS_DENY%></option>
	<option value="0"><%=SIGAConstants.ACCESS_NONE%></option>
</select>