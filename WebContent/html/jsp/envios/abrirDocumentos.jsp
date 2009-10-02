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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			
			<!-- Asociada al boton Nuevo -->
			function accionNuevo()
			{
				DocumentosForm.modo.value="nuevo";
				DocumentosForm.idEnvio.value="<%=idEnvio%>";
				DocumentosForm.idInstitucion.value="<%=idInstitucion%>";
				
				var resultado=ventaModalGeneral("DocumentosForm","P");
				
				if (resultado=="MODIFICADO")
				{
					document.location.reload();
				}
			}

			function refrescarLocal()
			{
				document.location.reload();
			}

			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var i, j;
				for (i = 0; i < 2; i++) 
				{
  						var tabla;
  						tabla = document.getElementById('tabladatos');
  						if (i == 0)
  						{
    						var flag = true;
    						j = 1;
    						while (flag) 
    						{
      							var aux = 'oculto' + fila + '_' + j;
      							var oculto = document.getElementById(aux);
      							if (oculto == null)  
      							{ 
      								flag = false; 
      							}
     							else 
     							{ 
     								datos.value = datos.value + oculto.value + ','; 
     							}
      							j++;
    						}
    						datos.value = datos.value + "%"
  						} 
  						else 
  						{ 
  							j = 2; 
  						}
  						if ((tabla.rows[fila].cells)[i].innerText == "") 
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
 						else
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
					
					document.forms[0].target = "submitArea";
					document.forms[0].modo.value = "download";
					document.forms[0].submit();
					document.forms[0].target = "mainWorkArea";
				}
			}
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

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>


			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%> 				    
					&nbsp;&nbsp;&nbsp;
					<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
				</td>
			</tr>
		</table>
	
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="envios.definir.literal.descripcion,"
		   		  tamanoCol="87,13"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  modal="P">

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
			</siga:TablaCabecerasFijas>


		<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"/>

		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>