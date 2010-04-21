<!-- seleccionPlantillasModalNew.jsp -->

<!-- CABECERA JSP -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	
	String idInstitucion = (String) request.getAttribute("IdInstitucion");
	String idInforme = (String) request.getAttribute("idInforme");
	String idTipoInforme = (String) request.getAttribute("idTipoInforme");
	String datosInforme = (String) request.getAttribute("datosInforme");
	String enviar = (String) request.getAttribute("enviar");
	String descargar = (String) request.getAttribute("descargar");
	String tipoPersonas = (String) request.getAttribute("tipoPersonas");
	String clavesIteracion = (String) request.getAttribute("clavesIteracion");
	String action = (String) request.getAttribute("action");
	String accion = (String) request.getAttribute("accion");
	String form = (String) request.getAttribute("form");
	Vector plantillas = (Vector)request.getAttribute("plantillas");
	
%>	

<html>

	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	</head>
	
	<body>
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="informes.seleccionPlantillas.titulo"/>
				</td>
			</tr>
		</table>
	
		<html:form action="<%=action%>" method="POST" target="submitArea">
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="accion" value="<%=accion%>">
			<input type="hidden" name="idInstitucion" value="<%=idInstitucion%>">
			<input type="hidden" name="idInforme" value="<%=idInforme%>">
			<input type="hidden" name="idTipoInforme" value="<%=idTipoInforme%>">
			<input type="hidden" name="datosInforme" value="<%=datosInforme%>">
			<input type="hidden" name="enviar" value="<%=enviar%>">
			<input type="hidden" name="descargar" value="<%=descargar%>">
			<input type="hidden" name="tipoPersonas" value="<%=tipoPersonas%>">
			<input type="hidden" name="clavesIteracion" value="<%=clavesIteracion%>">
			<input type="hidden" name="seleccionados" value="3">
		</html:form>

		<siga:TablaCabecerasFijas
	   	      nombre="tablaDatos"
	   		  borde="1"
	   		  clase="tableTitle"
	   		  nombreCol="infomes.seleccionPlantillas.literal.sel,infomes.seleccionPlantillas.literal.descripcion"
	   		  tamanoCol="10,90"
	   		  alto="100%"
	   		  modal="G"
   		  	  activarFilaSel="true" 
			  ajusteBotonera="true">
<%
			if (plantillas==null || plantillas.size()==0)
			{
%>
			<br><br>
	   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
			<br><br>
<%
			} else 
			{
		 		for (int i=0; i<plantillas.size(); i++)
		   		{
			  		AdmInformeBean bean = (AdmInformeBean)plantillas.elementAt(i);
%>
		  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">
						<td>
							<input type="checkbox" value="<%=bean.getIdPlantilla()%>" name="chkPL" <%=(bean.getPreseleccionado().equals("S"))?"checked":"" %> >
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(bean.getDescripcion()) %>
						</td>
		  			</siga:FilaConIconos>
<%
				}
			}
%>
		
		</siga:TablaCabecerasFijas>
			
		<siga:ConjBotonesAccion botones='YGC' modo=''  modal="P"/>

		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			
			function accionGenerarCerrar() 
			{
					 
				var aDatos = new Array();
				var oCheck = document.getElementsByName("chkPL");
				sub();	
				for(i=0; i<oCheck.length; i++)
				{
					if (oCheck[i].checked)
					{
						var indice=aDatos.length;
						for (j=0; j<aDatos.length; j++)
						{
							var dato1 = aDatos[j];
							var dato2 = oCheck[i].value;
						}
						aDatos[j] = oCheck[i].value;
					}
				}
				var auxi = "";
				for (i=0; i<aDatos.length; i++)
				{
					auxi += aDatos[i] + "##";
				}
				if (auxi.length>2) auxi=auxi.substring(0,auxi.length-2);
		    	window.returnValue=auxi;
				window.close();
			}
	
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
				
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>