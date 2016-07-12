
<!-- resultadoPerfilcolegiado.jsp -->


<!-- CABECERA JSP -->
<%@page import="com.siga.censo.ws.form.ConfiguracionPerfilColegioForm"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>


<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.censo.ws.action.ConfiguracionPerfilColegioAction"%>
<%@ page import="org.redabogacia.sigaservices.app.vo.ColegioPerfilColumnaVO"%>


<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorVector"%>
<%@page import="org.redabogacia.sigaservices.app.AppConstants"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	

	String idioma=usr.getLanguage().toUpperCase();

	
	String action=app+request.getAttribute("javax.servlet.forward.servlet_path")+"?noReset=true";
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	String botones = "";
	
	boolean accionVer = false;
	String accion = (String) request.getSession().getAttribute("accion");
	if (accion != null && accion.equals("ver")) {
		accionVer = true;
	}
	
	
%>	


<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	</head>
	
	<script type="text/javascript">

		function refrescarLocal() {
			parent.buscar();
		}
		function accionGuardar() 
		{
			var idperfil="";
			var datos = "";
			var ele = document.getElementsByName("checkParametrosGenerales");
		
			for (i = 0; i < ele.length; i++) {
				if (ele[i].checked) {
				
					if (document.getElementById("valor_" + ele[i].value).value.length < 1) {
						alert ("<siga:Idioma key="administracion.parametrosGenerales.error.valorParametro"/>");
						return;
					}
				
					if (datos.length > 0) datos = datos + "#;;#";
					datos = datos + document.getElementById("valor_" + ele[i].value).value;
					idperfil = document.getElementById("id_" + ele[i].value).value;
					
				}
			}

			if (datos.length < 1) {
				alert ("<siga:Idioma key="administracion.parametrosGenerales.alert.seleccionarElementos"/>");
				return;
			}
			
			
			
			document.forms['ConfigPerfilColegioForm'].modo.value="modificar";
			document.forms['ConfigPerfilColegioForm'].nombreColumna.value=datos;
			document.forms['ConfigPerfilColegioForm'].idPerfil.value=idperfil;
			document.forms['ConfigPerfilColegioForm'].submit();
			
		}	
		
		function modificaParametro(o) 
		{
			var ele = document.getElementsByName("checkParametrosGenerales");
			for (i = 0; i < ele.length; i++) {
				if (!ele[i].checked) {
					ele[i].disabled=true;
				}
			}
			
			valor = document.getElementById("valor_" + o.value);
			if (o.checked) {
				valor.disabled = false;
			}
			else {
				
				for (i = 0; i < ele.length; i++) {
					if (!ele[i].checked) {
						ele[i].disabled= false;
					}
				}
				
				var mensaje = "<siga:Idioma key="administracion.parametrosGenerales.alert.restaurarValor"/>";
				if(confirm(mensaje)) {						
					valor.value = document.getElementById("valorOriginal_" + o.value).value;
					valor.disabled = true;
				}
				else {
					o.checked = true;
				}
			}
		}
		
	</script>

	<body>	
	
	<html:form action="/CEN_ConfigPerfilColegio" method="POST" target="submitArea">
			<html:hidden name="ConfigPerfilColegioForm" property = "modo"/>
			<html:hidden name="ConfigPerfilColegioForm" property = "nombreColumna"/>
			<html:hidden name="ConfigPerfilColegioForm" property = "tipoColumna"/>
			<html:hidden name="ConfigPerfilColegioForm" property = "idPerfil"/>
			
			<html:hidden property="accionAnterior" value="${path}"/>
	</html:form>
	

	<siga:Table 		   
		   name="listadoPerfil"
		   border="2"
		   columnNames="administracion.parametrosGenerales.literal.editar,censo.perfil.literal.nombrecolumna,censo.perfil.literal.tipocolumna,"
		   columnSizes="6,42,41,5">
		 
		     	<%
		     		List<ColegioPerfilColumnaVO> listadoPerfil =null;
		     		if (ses.getAttribute("DATA") != null) {
		     			listadoPerfil = (List<ColegioPerfilColumnaVO>) ses.getAttribute("DATA");
		     		}
		     	
   					FilaExtElement[] elems = new FilaExtElement[0];	   		
		   		   	botones = "B";
		   			   		   		
		   		 	if(listadoPerfil != null && listadoPerfil.size() > 0){
		   		   		for (int i=0; i<listadoPerfil.size(); i++) {
		   		   			
		   		   		ColegioPerfilColumnaVO perfil = listadoPerfil.get(i);
		   		   		%>
		   	    		<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>'  visibleBorrado="false" visibleEdicion="false" visibleConsulta="false" pintarEspacio="no" botones="<%=botones%>" clase="listaNonEdit">
		   	    			<td align="center">
								<input type=checkbox name="checkParametrosGenerales" id="id_<%=perfil.getIdPerfil() %>" value="<%=perfil.getIdPerfil()%>" onclick="modificaParametro(this)"/>
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_" name="oculto<%=String.valueOf(i+1)%>_" value="<%=perfil.getIdPerfil()%>">
							</td>
							<input type="hidden" value="<%=perfil.getNombrePerfil()%>" id="valorOriginal_<%=perfil.getIdPerfil()%>" >
							<td style="text-align: center"><input type="text" value="<%=perfil.getNombrePerfil()%>" id="valor_<%=perfil.getIdPerfil()%>" disabled size="50"> </td>
							<td style="text-align: center"><%=perfil.getNombreColumna()%></td>
					
						</siga:FilaConIconos>	
							<% }
		   				   } else { %>
	 							<tr class="notFound">
	   					<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
						<% } %>
		   
		</siga:Table>
		<siga:ConjBotonesAccion botones="G"  clase="botonesDetalle"/>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>	
	</body>
</html>