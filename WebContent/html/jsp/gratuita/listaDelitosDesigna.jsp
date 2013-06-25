<!-- listadoDelitosDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsDelitoBean"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();

	String modopestanha = request.getSession().getAttribute("Modo")==null?"":(String)request.getSession().getAttribute("Modo");
	
	//Datos propios del jsp:	
	Vector vDelitosDesigna = (Vector) request.getAttribute("vDelitosDesigna");
%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		function refrescarLocal(){
				parent.refrescarLocal();
		}
	</script>
</head>

<body>

	<html:form action="/JGR_DelitosDesignas.do" method="post" target="resultado" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<!-- Datos de la pestanha -->
		<html:hidden name="pestanaDelitoDesignaForm" property="anio" />
		<html:hidden name="pestanaDelitoDesignaForm" property="numero" />
		<html:hidden name="pestanaDelitoDesignaForm" property="idTurno" />
	</html:form>	
		
		
	<!-- INICIO: RESULTADO -->
		<siga:Table 		   
			   name="listadoInicial"
			   border="2"
			   columnNames="gratuita.mantenimientoTablasMaestra.literal.delito,"
			   columnSizes="90,10"
			   modal="P">
	<% if (vDelitosDesigna!= null && !vDelitosDesigna.isEmpty()) { %>
			<%
				String descripcion="", idDelito="";
				int recordNumber=1;
				while ((recordNumber) <= vDelitosDesigna.size())
				{
					Hashtable hashDelitosDesigna = (Hashtable)vDelitosDesigna.get(recordNumber-1);
					descripcion = (String)hashDelitosDesigna.get(ScsDelitoBean.C_DESCRIPCION);
					idDelito = (String)hashDelitosDesigna.get(ScsDelitoBean.C_IDDELITO);
			%>
				<!-- Campos ocultos por cada fila:      
					1- IDDELITO
				-->
				<!-- Campos visibles por cada fila:
					1- DESCRIPCION
				-->
		       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="B" visibleConsulta="no" visibleEdicion="no" clase="listaNonEdit" modo="<%=modopestanha%>" >
					<td>
							<input type="hidden" name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idDelito%>' >
							<%=descripcion%>
					</td>
				</siga:FilaConIconos>
				<% 		recordNumber++; %>
				<% } %>
	<% } else { %>
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<% } %>
	</siga:Table>	


	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>