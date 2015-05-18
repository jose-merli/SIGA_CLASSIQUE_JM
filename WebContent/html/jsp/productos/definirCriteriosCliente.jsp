<!DOCTYPE html>
<html>
<head>
<!-- definirCriteriosCliente.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%
	String resultado = (String)request.getParameter("resultado");
	String modo = (String)request.getSession().getAttribute("modoResultado");
	request.getSession().removeAttribute("modoResultado");
	if (modo == null)
		modo = "modificar";	
	String habilitarCheck = (modo.equals("consulta") ? "disabled" : "");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
</head>
	
<body class="tablaCentralCampos">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="mainWorkArea" style="display:none">
		<input type="hidden" name="actionModal" value="">
	</html:form>		
		
<%
	String tamanosCol = "8,4,40,15,20,4,10";
	String nombresCol = "pys.mantenimientoServicios.literal.conector,(,pys.mantenimientoServicios.literal.campo,pys.mantenimientoServicios.literal.operador,pys.mantenimientoServicios.literal.valor,),";
%>
	<siga:Table
	   	name="tablaDatos"
	   	border="1"
	   	columnNames="<%=nombresCol%>"
	   	columnSizes="<%=tamanosCol%>">

		<!-- Aqui se iteran los diferentes registros de la lista 
		 Para pintar la tabla se necesita un String, formado por la
		 concatenacion de criterios con el siguiente formato:
		 
		 *conector_campo_operador_valor_idCampo__idOperador_idValor_parentesisAbrir_parentesisCerrar_ERROR
		 
		 donde los siguientes campos tambien tienen un formato concreto:
		 
		 idCampo: 		idCampo,tipoCampo,idTabla
		 idOperador: 	idOperador,simbolo				
		-->
			
<%		
		String[] arrayCriterios = resultado.split("\\*");
		if (arrayCriterios.length == 0) {
%>
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	 
<%
		} else {
			FilaExtElement[] elems = new FilaExtElement[1];
			String visible = "no";
			if (!modo.equalsIgnoreCase("consulta"))
				elems[0] = new FilaExtElement("borrar","borrar",SIGAConstants.ACCESS_FULL);
			else 
				visible = "si";
			
			for (int i=1; i<arrayCriterios.length; i++) {
				String sCriterios = arrayCriterios[i];
				String[] arrayCampos = sCriterios.split("_");

				String conector = arrayCampos[0];
				String campo = arrayCampos[1];
				String operador = arrayCampos[2];
				String valor = arrayCampos[3];
				String idCampo = arrayCampos[4];
				String idOperador = arrayCampos[5];
				String idValor = arrayCampos[6];
				String parentesisAbrir = arrayCampos[7];
				String parentesisCerrar = arrayCampos[8];
				boolean tieneError = (arrayCampos.length>9 && arrayCampos[9].equalsIgnoreCase("ERROR"));
	
				if (valor.equalsIgnoreCase("")) 
					valor = "&nbsp;";
							
				if (idOperador.split(",")[0].equals(Integer.toString(ClsConstants.ESVACIO_ALFANUMERICO)) || 
					idOperador.split(",")[0].equals(Integer.toString(ClsConstants.ESVACIO_NUMERICO)) || 
					idOperador.split(",")[0].equals(Integer.toString(ClsConstants.ESVACIO_FECHA)) ){

					valor = (valor.equals("0") || valor.equals("NO") ? "NO" : "SI");								
				}
%>
  				<siga:FilaConIconos fila='<%=""+i%>' botones="" elementos='<%=elems%>' pintarEspacio="no" visibleEdicion="no" visibleConsulta="no" visibleBorrado="<%=visible%>" clase="listaNonEdit">
						<td>
							<input type="hidden" name="oculto<%=i%>_1" value="<%=idCampo%>">
							<input type="hidden" name="oculto<%=i%>_2" value="<%=idOperador%>">
							<input type="hidden" name="oculto<%=i%>_3" value="<%=idValor%>">
							<input type="hidden" name="oculto<%=i%>_4" value="<%=(tieneError?"ERROR":"")%>">
							<%=conector%>
						</td>
						<td><input type="checkbox" value="1" name="chkParentesisAbrir" <%=(parentesisAbrir.equals("1"))?"checked":""%> <%=habilitarCheck%>></td>
						<td><%=campo%></td>
						<td><%=operador%></td>
						<td><%=valor%></td>
						<td><input type="checkbox" value="1" name="chkParentesisCerrar" <%=(parentesisCerrar.equals("1"))?"checked":""%> <%=habilitarCheck%>></td>
					</siga:FilaConIconos>
<%
			} // for
		} // else
%>
	</siga:Table>

	<script language="JavaScript">
		function borrar(fila){
			parent.borrarFila(fila);
		}	
	</script>
</body>
</html>