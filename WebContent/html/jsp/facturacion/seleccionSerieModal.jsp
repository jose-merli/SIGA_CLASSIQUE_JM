<!-- ventanaFechaEfectiva.jsp -->
<!-- 
	 Permite mostrar/editar datos sobre lols precios asociados a los servicios
	 VERSIONES:
	 miguel.villegas 7-2-2005 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

		

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	
	</head>

	<body>

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" heigth="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="facturacion.seleccionSerie.titulo"/>
					</td>
				</tr>

			</table>

			<siga:TablaCabecerasFijas
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="facturacion.seleccionSerie.literal.sel,facturacion.seleccionSerie.literal.descripcion"
		   		   tamanoCol="10,90"
		   		  alto="100%"
		   		  modal="P"
	   		  	  activarFilaSel="true"  
	   		  	  >
<%

				if (series==null || series.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				} else 
				{
			 		for (int i=0; i<series.size(); i++)
			   		{
				  		FacSerieFacturacionBean bean = (FacSerieFacturacionBean)series.elementAt(i);
%>
			  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">
							<td>
								<input type="radio" name="seleccionSerie" value="<%=bean.getIdSerieFacturacion()%>"><%=bean.getIdSerieFacturacion()%></input>
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
			
			<siga:ConjBotonesAccion botones='Y' modo=''  modal="P"/>

		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			
			function accionGuardarCerrar() 
			{	 
				var seleccion = document.getElementsByName("seleccionSerie");
				var dato = "";
				for(i=0; i<seleccion.length; i++)
				{
					if (seleccion[i].checked)
					{
						dato = seleccion[i].value;
					}
				}
				if (dato=='') {
					alert('<siga:Idioma key="messages.facturacion.seleccionSerie.noSeleccion"/>');
					return false;
				}
				window.returnValue=dato;
				window.close();
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