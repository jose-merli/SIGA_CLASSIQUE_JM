<!-- seleccionSerieModal.jsp -->
<!-- 
	 Permite mostrar/editar datos sobre lols precios asociados a los servicios
	 VERSIONES:
	 miguel.villegas 7-2-2005 
-->

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
	
	Vector series = (Vector)request.getSession().getAttribute("seriesCandidatas");
	
%>	

<html>
<!-- HEAD -->
	<head>

		

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	
	</head>

	<body>

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" heigth="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="informes.seleccionSerie.titulo"/>
					</td>
				</tr>

<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="submitArea">
	<html:hidden property="idInstitucion"/>
</html:form>


			</table>

			<siga:Table
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="infomes.seleccionSerie.literal.sel,infomes.seleccionSerie.literal.descripcion"
		   		  columnSizes="10,90"
		   		  modal="G">
<%
				if (series==null || series.size()==0)
				{
%>
				<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
<%
				} else 
				{
			 		for (int i=0; i<series.size(); i++)
			   		{
				  		FacSerieFacturacionBean bean = (FacSerieFacturacionBean)series.elementAt(i);
%>
			  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">
							<td>
								<input type="radio" value="<%=bean.getIdSerieFacturacion()%>" name="chkPL">
							</td>
							<td>
								<%=UtilidadesString.mostrarDatoJSP(bean.getDescripcion()) %>
							</td>
			  			</siga:FilaConIconos>
<%
					}
				}
%>
			
			</siga:Table>
			
			<siga:ConjBotonesAccion botones='Y' modo=''  modal="P"/>

		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			
			function accionGuardarCerrar() 
			{	 
				var marcado = false;
				
				var aDatos = new Array();
				var oCheck = document.getElementsByName("chkPL");
				
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
						marcado = true;
					}
				}
				if (marcado){
					var auxi = "";
					for (i=0; i<aDatos.length; i++)
					{
						auxi += aDatos[i] + "##";
					}
					if (auxi.length>2) auxi=auxi.substring(0,auxi.length-2);
			    	window.top.returnValue=auxi;
					window.top.close();
				}else{
					// Debe haber un concepto marcado
					alert('<siga:Idioma key="infomes.seleccionSerie.debeSeleccionar"/>');
				}
			}
	
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
				
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>