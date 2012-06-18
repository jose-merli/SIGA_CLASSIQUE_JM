<!-- abrirRoles.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
		
	//Recupero el nombre del expediente
	String nombreExp = (String)request.getAttribute("nombreExp");
	
	request.removeAttribute("datos");	
%>	


<%@page import="com.siga.Utilidades.UtilidadesMultidioma"%><html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.tipoExpedientes.configuracion.cometidos.cabecera" 
			localizacion="expedientes.tipoExpedientes.configuracion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			// Asociada al boton Nuevo 
			function accionNuevo() 
			{		
				  document.forms[0].modo.value = "nuevo";
   				  var resultado=ventaModalGeneral(document.forms[0].name,"P");
   				  if(resultado=='MODIFICADO') parent.buscar();
			}
	
			// Asociada al boton Volver
			function accionVolver() 
			{		
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
		
		</script>
		<!-- FIN: SCRIPTS BOTONES -->		
		
	</head>
	
	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
			<html:form  action="/EXP_TiposExpedientes_Roles.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property="idTipoExpediente"/>
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="expedientes.literal.tiposexpedientes"/> :&nbsp;<%=nombreExp%> 				    
			</td>
		</tr>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
	</table>
	
		
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.auditoria.literal.rol,"
		   		  tamanoCol="90,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  modal="p"
		   		  >

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		ExpRolesBean bean = (ExpRolesBean)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="E,B" clase="listaNonEdit" visibleConsulta="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdRol()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoExpediente()%>">						
						
						<%=UtilidadesMultidioma.getDatoMaestroIdioma(bean.getNombre(),user)%>
					</td>					
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>

			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<%	if (tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)){ %>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } else{ %>
				<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"  />
	<% } %>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		