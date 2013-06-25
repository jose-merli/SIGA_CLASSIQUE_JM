<!-- detalleRegistroUsuario.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector datos = (Vector)request.getAttribute("datos");
	String modo=(String)request.getAttribute("modo");	
	String estilo="boxConsulta";
	String estiloCombo="boxConsulta";
	String soloLectura="true";
	if (modo!=null && modo.equals("editar")){
	  estilo="box";
	  estiloCombo="boxCombo";
	  soloLectura="false";
	  
	}	
	AdmContadorBean bean = (AdmContadorBean)datos.elementAt(0);

	String modificable=(bean.getModificableContador()!=null && bean.getModificableContador().equals("1"))?"Si":"No";
	ArrayList modoSel = new ArrayList();
	modoSel.add( bean.getModoContador().toString());
	String fechaReconf=GstDate.getFormatedDateShort("",bean.getFechaReconfiguracion());
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<!-- Validaciones en Cliente -->
	   <html:javascript formName="gestionContadoresForm" staticJavascript="false" />  
		
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
            function refrescarLocal() {
			  
				parent.refrescarLocal();
				window.top.close();
			}
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				gestionContadoresForm.reset();
			}
		
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();	
			   if (!validateGestionContadoresForm(document.gestionContadoresForm)){
			   	 fin();	 
			     return false;
			   }
				 var mensaje;
				if (gestionContadoresForm.contador.value.length>gestionContadoresForm.longitud.value){
				   mensaje='<siga:Idioma key="administracion.contador.error.longitudContador"/>';
     				 alert(mensaje);
     				 fin();
					 return false;
				}
				
				if( document.getElementById("contadorSiguiente").style.display=="block"){
				  if (gestionContadoresForm.reconfiguracionContador.value==""){
				     mensaje='<siga:Idioma key="administracion.contador.error.valorContadorSiguiente"/>';
     				 alert(mensaje);
     				 fin();
					 return false;
				  }
				
				  if (gestionContadoresForm.reconfiguracionContador.value.length>gestionContadoresForm.longitud.value){
				      mensaje='<siga:Idioma key="administracion.contador.error.longitudContadorSiguiente"/>';
     				 alert(mensaje);
     				 fin();
					 return false;
				  } 
				}
				if (gestionContadoresForm.modificable.checked){
				  gestionContadoresForm.modificable.value="<%=ClsConstants.DB_TRUE%>";
				}else{
				   gestionContadoresForm.modificable.value="<%=ClsConstants.DB_FALSE%>";

				}
				gestionContadoresForm.submit();
				
			}		
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() {		
				window.top.close();
			}
			
			function showCalendarGeneral(inputElement){
                var cont=document.getElementById("contadorSiguiente");
				
             	var resultado = showModalDialog("<%=app%>/html/jsp/general/calendarGeneral.jsp?valor="+inputElement.value,inputElement,"dialogHeight:295px;dialogWidth:360px;help:no;scroll:no;status:no;");	
             	window.top.focus();
             	if (resultado) {
            		inputElement.value = resultado;
        	    } 
				if (inputElement.value==""){
					gestionContadoresForm.reconfiguracionContador.value="0";
				   	gestionContadoresForm.reconfiguracionPrefijo.value="";
				   	gestionContadoresForm.reconfiguracionSufijo.value="";				  	  				  
				  	cont.style.display="none";
				} else {				
				  <% if (bean.getReconfiguracionContador()!=null && !bean.getReconfiguracionContador().equals("")){%>
				   gestionContadoresForm.reconfiguracionContador.value="<%=bean.getReconfiguracionContador()%>";
				  
				  <%}else{%>
				   gestionContadoresForm.reconfiguracionContador.value="0";
				  <%}%>
				  <% if (bean.getReconfiguracionPrefijo()!=null && !bean.getReconfiguracionPrefijo().equals("")){%>
				   gestionContadoresForm.reconfiguracionPrefijo.value="<%=bean.getReconfiguracionPrefijo()%>";
				  <%}else{%>
				   gestionContadoresForm.reconfiguracionPrefijo.value="";
				  <%}%>
				  <% if (bean.getReconfiguracionSufijo()!=null && !bean.getReconfiguracionSufijo().equals("")){%>
				   gestionContadoresForm.reconfiguracionSufijo.value="<%=bean.getReconfiguracionSufijo()%>";
				  <%}else{%>
				   gestionContadoresForm.reconfiguracionSufijo.value="";
				  <%}%>
				  cont.style.display="block";
				}
				return false;
			}	
	
	       function inicio(){
		     var cont=document.getElementById("contadorSiguiente");
		   
		     <% if (fechaReconf!=null && !fechaReconf.equals("")){%>
			   
			   cont.style.display="block";
			 
			 <%}%>
			 
			 <% if (bean.getModificableContador().equals(ClsConstants.DB_TRUE)){%>
			     gestionContadoresForm.modificable.checked="true";
			 
			 <%}else{%>
			  
			     gestionContadoresForm.modificable.checked="";
			     
			 <%}%>
			 
			
			 
		   }
		   function desactivar(){
		    var modo_aux="";
		    modo_aux=<%=modo%>;
		   	alert (modo_aux);
		   	  
		   }
		  
   		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body onload="inicio();">
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="administracion.contador.titulo"/>
			</td>
		</tr>
	</table>	
		
		<div id="camposRegistro" class="posicionModalGrande" align="center">
			<html:form action="/ADM_Contadores.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "Modificar"/>
				


				<table class="tablaCentralCamposGrande" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.contador.titulo">
							
								<table class="tablaCampos" align="center" border="0">
									<tr>				
										<td class="labelText" width="15%">
										  <siga:Idioma key="administracion.parametrosGenerales.literal.codigo"/>&nbsp;(*)
										</td>				
										<td class="labelTextValue" colspan="5">
											<html:text property="codigo" styleClass="boxConsulta" size="30" maxlength="20" value="<%=bean.getIdContador()%>" ></html:text>
										</td>
									</tr>
									<tr>				
										<td class="labelText" width="15%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.nombre"/>&nbsp;(*)
										</td>				
										<td class="labelTextValue" colspan="5">
											<html:text property="nombreContador" styleClass="<%=estilo%>" size="100" maxlength="100" value="<%=bean.getNombre()%>"></html:text>
										</td>
									</tr>
									<tr>				
										<td class="labelText" width="15%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.descripcion"/>
										</td>				
										<td class="labelTextValue" colspan="5">
										  <html:text property="descripcionContador" styleClass="<%=estilo%>" size="100"  maxlength="255" value="<%=bean.getDescripcion()%>"></html:text>
										</td>
									</tr>
									<tr>
										<td class="labelText" width="15%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.modificable"/>
										</td>				
										<td class="labelTextValue" colspan="5">
										<% if (modo!=null && modo.equals("editar")){ %>
											   <html:checkbox  property="modificable"  value="<%=bean.getModificableContador()%>"/>
										 	   
											<% }else{ %>
												<input type="checkbox" name="modificable" value="1" disabled>
											  <%}%>
											 
										</td>
									</tr>
									<tr>				
										<td class="labelText" width="15%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.modo"/>&nbsp;(*)
										</td>
										<td class="labelTextValue" colspan="5">
										  <% if (bean.getIdContador().equals("SOCIEDADSJ") || bean.getIdContador().equals("SSPP")){%>
										    <siga:ComboBD nombre="modoContador" tipo="cmbModo" obligatorio="true" obligatorioSinTextoSeleccionar="true" clase="<%=estiloCombo%>" elementoSel="<%=modoSel%>" readonly="<%=soloLectura%>" />
										  <%}else{%>
											<siga:ComboBD nombre="modoContador" tipo="cmbModo" obligatorio="true" obligatorioSinTextoSeleccionar="true" clase="boxConsulta" elementoSel="<%=modoSel%>" readonly="true" />
										  <%}%>	
										</td>
									</tr>
									<tr>	
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.prefijo"/>
										</td>
										<td class="labelTextValue">
											<html:text property="prefijo" styleClass="<%=estilo%>" size="10" maxlength="8" value="<%=bean.getPrefijo()%>"></html:text>
										</td>
									
												
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.contadorActual"/>&nbsp;(*)
										</td>
										<td class="labelTextValue">
											<html:text property="contador" styleClass="<%=estilo%>"  size="10" maxlength="10" value="<%=bean.getContador().toString()%>"></html:text>
										</td>
									
												
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.sufijo"/>
										</td>
										<td class="labelTextValue">
											<html:text property="sufijo" styleClass="<%=estilo%>"  size="10" maxlength="8" value="<%=bean.getSufijo()%>"></html:text>
										</td>
											
									</tr>
									<tr>				
										<td class="labelText" width="15%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.longitud"/>&nbsp;(*)
										</td>
										<td class="labelTextValue" colspan="5">
											<html:text property="longitud" styleClass="<%=estilo%>"  size="10"  maxlength="5" value="<%=bean.getLongitudContador().toString()%>"></html:text>
										</td>
									</tr>
									<tr>
									<td>&nbsp;
									</td>
									</tr>
									<tr>
									  <td class="labelText" colspan="6" style="font-size: 12px;">
									    <siga:Idioma key="administracion.parametrosGenerales.literal.nota"/>
									  </td>
									</tr>
									<tr>
									<td>&nbsp;
									</td>
									</tr>
									<tr>	
									<td colspan="6">
									<siga:ConjCampos leyenda="administracion.contador.reconfigurar">
									<table class="tablaCampos" align="center" border="0">
									 <tr>
										<td class="labelText" width="30%">
											<siga:Idioma key="administracion.parametrosGenerales.literal.fechaReconfiguracion"/>
										</td>
										<td class="labelTextValue" colspan="5">																																										
											<% if (modo!=null && modo.equals("editar")){%>
		                                     &nbsp;&nbsp;<siga:Fecha  nombreCampo="fechaReconfiguracion" valorInicial="<%=fechaReconf%>"/>
											<%}else{%>
												<siga:Fecha  nombreCampo="fechaReconfiguracion" valorInicial="<%=fechaReconf%>" disabled="true"/>
											<%}%>
											 
										</td>
									</tr>	
									<tr id="contadorSiguiente"  style= "display:none">	
									 
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.prefijoSiguiente"/>
										</td>
										<td class="labelTextValue" id="prefijoSiguiente">
											<html:text property="reconfiguracionPrefijo" styleClass="<%=estilo%>" size="10" maxlength="8" value="<%=bean.getReconfiguracionPrefijo()%>"></html:text>
										</td>
									
												
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.contadorSiguiente"/>&nbsp;(*)
										</td>
										<td class="labelTextValue" id="contadorSiguiente">
											<html:text property="reconfiguracionContador" styleClass="<%=estilo%>"  size="10" maxlength="10" value="<%=bean.getReconfiguracionContador().toString()%>"></html:text>
										</td>
									
												
										<td class="labelText" >
											<siga:Idioma key="administracion.parametrosGenerales.literal.sufijoSiguiente"/>
										</td>
										<td class="labelTextValue" id="sufijoSiguiente">
											<html:text property="reconfiguracionSufijo" styleClass="<%=estilo%>"  size="10" maxlength="8" value="<%=bean.getReconfiguracionSufijo()%>"></html:text>
										</td>
											
									</tr>
									 </table>	
									 </siga:ConjCampos>		
									 </td>	
									</tr>
									
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
<%
			String botones =  "R,Y,C";
			String cerrar = "C" ;
%>
			<% if (modo!=null && modo.equals("editar")){ %>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
			<%}else{%>
			<siga:ConjBotonesAccion botones="<%=cerrar%>" modal="M"/>
			<%}%>
		</div>	
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>