<!-- listadoDelitosDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();

	String modopestanha = request.getSession().getAttribute("Modo")==null?"":(String)request.getSession().getAttribute("Modo");
	
	//Datos propios del jsp:	
	Vector vDelitosDesigna = (Vector) request.getAttribute("vDelitosDesigna");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
	<!-- INICIO: RESULTADO -->
		<siga:TablaCabecerasFijas 		   
			   nombre="listadoInicial"
			   borde="2"
			   clase="tableTitle"		   
			   nombreCol="gratuita.mantenimientoTablasMaestra.literal.delito,"
			   tamanoCol="90,10"
		   			alto="100%"
			   modal="P"
		>
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
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	   		 <br>
	<% } %>
	</siga:TablaCabecerasFijas>	


	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas -->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>