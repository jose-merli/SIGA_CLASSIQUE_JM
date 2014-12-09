<!DOCTYPE html>
<html>
<head>
<!-- seleccionModalCarta.jsp -->
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
		
	
	//Vector plantillas = null;
	Vector plantillas = (Vector)request.getAttribute("plantillas");
%>	


<!-- HEAD -->
	

		

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	
	</head>

	<body>

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" height="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="facturacion.morosos.titulo.seleccionCarta"/>
					</td>
				</tr>


			</table>

			<siga:Table
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="infomes.seleccionPlantillas.literal.sel,infomes.seleccionPlantillas.literal.descripcion"
		   		  columnSizes="10,90">
<%
				if (plantillas==null || plantillas.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				} else 
				{
			 		for (int i=0; i<plantillas.size(); i++)
			   		{
%>
						<tr>
<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" pintarEspacio="no" clase="listaNonEdit">						
							<td align="center">
								<input type="hidden"   name="modelo<%=i%>" value="<%=plantillas.get(i)%>">
								<input type="CheckBox" name="fila<%=i%>"   onclick="marcarUnoSolo(this);" >
							</td>
							<td class="">
								<%=plantillas.get(i)%>
							</td>
</siga:FilaConIconos>							
						</tr>
<%
					}
				}
%>
			
			</siga:Table>
			
			<siga:ConjBotonesAccion botones='Y,C' modo=''  modal="P"/>

		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			

			function accionCerrar() 
			{
				var aDatos = new Array();
				aDatos[0] = "0";
		    	window.top.returnValue=aDatos;
				window.top.close();
			}

			function accionGuardarCerrar() 
			{	 
				if(confirm('<siga:Idioma key="facturacion.morosos.confirmacion.envio"/>')){
					var aDatos = new Array();
					aDatos[0] = "1";
					
					for(i=0;i<<%=plantillas.size()%>;i++){
						if(document.getElementById("fila"+i).checked){
							aDatos[1]=document.getElementById("modelo"+i).value;
							break;
						}
					}
				}
				
		    	window.top.returnValue=aDatos;
				window.top.close();
			}
			
			function marcarUnoSolo(o){
				for(i=0;i<<%=plantillas.size()%>;i++){
					document.getElementById("fila"+i).checked=false;
				}
				o.checked=true;
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