<!DOCTYPE html>
<html>
<head>
<!-- abrirDocumentos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";
	
	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	//Recupero el nombre y tipo del envio
	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),user);
	String idEnvio = (String)request.getParameter("idEnvio");	
	
	String idInstitucion = (String)request.getAttribute("idInstitucion");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String mensaje       = (String)request.getAttribute("mensaje");
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			
			// Asociada al boton Nuevo
			function accionNuevo()
			{
				DocumentosForm.modo.value="nuevo";
				DocumentosForm.idEnvio.value="<%=idEnvio%>";
				DocumentosForm.idInstitucion.value="<%=idInstitucion%>";
				
				var resultado=ventaModalGeneral("DocumentosForm","P");
				
				if (resultado=="MODIFICADO")
				{				
					alert('<siga:Idioma key="messages.updated.success"/>', 'success');
					refrescarLocal();
				}
			}

			function refrescarLocal()
			{
				<%if (mensaje!=null){%>
						var type = '<siga:Idioma key="<%=mensaje%>"/>';
						alert(type);
				<%
				}
				%>
				document.location.reload();
			}

			function accionDownload() 
			{		

				DocumentosForm.idEnvio.value="<%=idEnvio%>";
				DocumentosForm.idInstitucion.value="<%=idInstitucion%>";
  			    document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "descargarEnv";
				document.forms[0].submit();
				document.forms[0].target = "mainWorkArea";
			
			
		}
					
			
			
			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				preparaDatos(fila,'tablaDatos',datos);
  						
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "download";
				document.forms[0].submit();
				document.forms[0].target = "mainWorkArea";
			}
			<%if (mensaje != null && !mensaje.equals("")){%>
			$(document).ready(function(){alert("<%=mensaje%>");});
			<%}%>
		</script>
		
		
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirEnvios.documentos.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>

	</head>

	<body>
	
		<table class="tablaTitulo" align="center" cellspacing="0">
		<html:form action="/ENV_Documentos.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo"/>
			<html:hidden property="idInstitucion"/>
			<html:hidden property="idEnvio"/>			

		</html:form>


			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%> 				    
					&nbsp;&nbsp;&nbsp;
					<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
				</td>
			</tr>
		</table>
	
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.definir.literal.descripcion,"
		   		  columnSizes="87,13"
		   		  modal="P">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}
				
				else
				{
					for (int i=0; i<vDatos.size(); i++)
			   		{
				  		EnvDocumentosBean bean = (EnvDocumentosBean)vDatos.elementAt(i);
				  		
						FilaExtElement[] elems=new FilaExtElement[1];
						elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);

%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="E,B" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdEnvio()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdDocumento()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getPathDocumento()%>">
						
						<%=bean.getDescripcion()%>
					</td>

				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>

<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>							
				  									
											
		<siga:ConjBotonesAccion botones="V,N,D" clase="botonesDetalle"/>

		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>