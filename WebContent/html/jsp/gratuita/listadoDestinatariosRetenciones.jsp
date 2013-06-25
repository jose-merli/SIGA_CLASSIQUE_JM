<!-- listadoDestinatariosRetenciones.jsp-->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector vDatos = (Vector)request.getAttribute("resultado");
	//Vector resultado = (Vector)vDatos.get(0);
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		//Refresco del iframe 	
	 	function refrescarLocal()
		{
			parent.buscar()
		}
	</script>
 	
</head>

<body>

	<html:form action="/JGR_MantenimientoDestinatariosRetenciones.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden styleId = "modo"  property = "modo" value = ""/>
	</html:form>	
		
	<!-- INICIO: RESULTADO -->
		<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="gratuita.maestros.destinatariosRetenciones.literal.orden,gratuita.maestros.destinatariosRetenciones.literal.descripcion,"
		   		  columnSizes="20,70,10"
		   		  fixedHeight="95%"
		   		  modal="P">

<%				
				if (vDatos==null || vDatos.size()==0) { %>	

				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}
				
				else
				{
			 		for (int i=1;i<=vDatos.size();i++)
			   		{
			 			Row fila = (Row)vDatos.elementAt(i-1);
			   			String  botones="C,E,B";
						Hashtable registro = (Hashtable) fila.getRow();
						String orden = (String)registro.get(FcsDestinatariosRetencionesBean.C_ORDEN);
						String nombre = (String)registro.get(FcsDestinatariosRetencionesBean.C_NOMBRE);
						String cuentaContable = (String)registro.get(FcsDestinatariosRetencionesBean.C_CUENTACONTABLE);
						String idDestinatario = (String)registro.get(FcsDestinatariosRetencionesBean.C_IDDESTINATARIO);
						String idInstitucion = (String)registro.get(FcsDestinatariosRetencionesBean.C_IDINSTITUCION);

%>
	  			<siga:FilaConIconos fila='<%=""+i%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" id="oculto<%=""+(i)%>_1" value="<%=idInstitucion%>">
						<input type="hidden" id="oculto<%=""+(i)%>_2" value="<%=idDestinatario%>">
						<%=UtilidadesString.mostrarDatoJSP(orden)%>
					</td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombre)%></td>
				</siga:FilaConIconos>
<%
					}
				}
				
%>
			</siga:Table>

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>