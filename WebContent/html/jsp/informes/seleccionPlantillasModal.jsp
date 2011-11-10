<!-- seleccionPlantillasModal.jsp -->
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
	
	Vector plantillas = (Vector)request.getAttribute("plantillas");
	
%>	

<html>
<!-- HEAD -->
	<head>

		

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	
	</head>

	<body onload="inicioCheckInforme();">

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo" cellspacing="0" heigth="32">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="informes.seleccionPlantillas.titulo"/>
					</td>
				</tr>

<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idInforme"/> 				
	<html:hidden property="idTipoInforme"/>	
	<html:hidden property="datosInforme"/>	
	<html:hidden property="descargar"/>
	<html:hidden property="clavesIteracion"/>
	<html:hidden property="tipoPersonas"/>
	<html:hidden property="seleccionados" value="3"/>	
</html:form>


			</table>

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
								<input type="checkbox" value="<%=bean.getIdTipoEnvio()%>" id="<%=bean.getIdPlantilla()%>" name="chkPL" <%=(bean.getPreseleccionado().equals("S"))?"checked":"" %> onclick="onClickCheckInforme(this);">
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
		function onClickCheckInforme(checkSeleccionado){
			checkIdTipoEnvioInforme(checkSeleccionado,checkSeleccionado.value);

			
		}
		
		function checkIdTipoEnvioInforme(checkSeleccionado,valor){
			if(checkSeleccionado.value==valor){
				if(checkSeleccionado.checked){
					checks = document.getElementsByName("chkPL");
					
					for(var i = 0 ; i <checks.length ; i++) {
						check = checks[i];
						if(check.value==valor){
							check.disabled =  "";
						}else{
							check.disabled =  "disabled";
							
						}
						
					}
				}else{
					checks = document.getElementsByName("chkPL");
					var atributoDisabled = "";
					for(var i = 0 ; i <checks.length ; i++) {
						check = checks[i];
						if(check.checked && (check.value==valor )){
							atributoDisabled = "disabled";
							break;
						}
						
					}
					
					
					
					
					
					for(var j = 0 ; j <checks.length ; j++) {
						check = checks[j];
						if(check.value!=valor ){
							check.disabled =  atributoDisabled;
						}
						
					}
					
				}
			}else{
				if(checkSeleccionado.checked){
					checks = document.getElementsByName("chkPL");
					for(var i = 0 ; i <checks.length ; i++) {
						check = checks[i];
						if(check.value==valor){
							check.disabled =  "disabled";
						}else{
							check.disabled =  "";
							
						}
						
					}
				}else{
					checks = document.getElementsByName("chkPL");
					
					var atributoDisabled2 = "";
					for(var i = 0 ; i <checks.length ; i++) {
						check = checks[i];
						
						if(check.value!=valor && check.checked){
							atributoDisabled2 = "disabled";
							break;
						}
						
					}
					
					
					
					
					for(var j = 0 ; j <checks.length ; j++) {
						check = checks[j];
						if(check.value==valor ){
							check.disabled =  atributoDisabled2;
						}
						
					}
					
				}
				
				
				
			}
			
			
			
		}	
		function inicioCheckInforme() {
			checks = document.getElementsByName("chkPL");
			var isAlgunoChecked = false;
			for(var i = 0 ; i <checks.length ; i++) {
				check = checks[i];
				if(check.checked){
					isAlgunoChecked = true;
					break;
				}
				
			}
			if(isAlgunoChecked){
				for(var j = 0 ; j <checks.length ; j++) {
					check = checks[j];
					if(check.value=="4" || check.value=="5"){
						check.disabled =  "disabled";
					}
					
				}
				
			}
		}
		
		
			function accionGenerarCerrar() 
			{
					 
				var aDatos = new Array();
				var oCheck = document.getElementsByName("chkPL");
				sub();
				var idTipoEnvio = "";
				for(i=0; i<oCheck.length; i++)
				{
					
					if (oCheck[i].checked)
					{
						if(idTipoEnvio=="" && (oCheck[i].value=="4"||oCheck[i].value=="5")){
							idTipoEnvio= oCheck[i].value;
						}
						var indice=aDatos.length;
						for (j=0; j<aDatos.length; j++)
						{
							var dato1 = aDatos[j];
							var dato2 = oCheck[i].id;
						}
						aDatos[j] = oCheck[i].id;
					}
				}
				var auxi = "";
				for (i=0; i<aDatos.length; i++)
				{
					
					auxi += aDatos[i] + "##";
				}
				
				if (auxi.length>2){
					auxi=auxi.substring(0,auxi.length-2);
					var array =new Array(2);
					array[0]=auxi;
					
					array[1]=idTipoEnvio;
					
					window.returnValue=array;
				}else{
					window.returnValue=new Array(0);
				}	
		    	
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