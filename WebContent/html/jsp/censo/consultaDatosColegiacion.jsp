<!-- consultaDatosColegiacion.jsp -->
<!-- 
	 Muestra los datos de colegiación generales de un cliente
	 VERSIONES:
	 RGG 15/03/2007 
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
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String modo=(String)request.getAttribute("ACCION");
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona	
	Long idPersona=(Long)request.getAttribute("IDPERSONA");
	String idInstitucion=(String)request.getAttribute("IDINSTITUCIONPERSONA"); // Obtengo el identificador de la institucion	
	String motivo=(String)request.getAttribute("MOTIVO");
	String activar=(String)request.getAttribute("ACTIVAR");
	if (motivo==null){
	  motivo="";
	}
	String fecha=(String)request.getAttribute("FECHAESTADO");
	if (fecha == null){
	   fecha="";
	}else{
	  fecha = GstDate.getFormatedDateShort(user.getLanguage(),fecha);	
	}
	

	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	String botonesAccion="";
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	} else {
		botonesAccion="V,";
	}
	if (!user.isLetrado()) { 
	    botonesAccion+="BA,";
	}
	// le quito la coma final
	if (botonesAccion.length()>0) {
		botonesAccion=botonesAccion.substring(0,botonesAccion.length()-1);
	}
	String estilo="";
	if (modo.equals("ver")){
	  estilo="boxConsulta";
	}else{
	  estilo="box";
	}
%>	
<script>
  function darBaja(){
     
	   if (document.DatosColegiacionForm.fechaEstado.value==""){
	    mensaje = '<siga:Idioma key="censo.consultaDatosColegiacion.error.fechaBaja"/>'
		alert(mensaje);
		return;
	   }  
   
       document.getElementById("activo").style.display="none";
	   document.getElementById("baja").style.display="block";
	   
	   document.getElementById("botonBaja").style.display="none";
	   document.getElementById("botonAlta").style.display="block";
	   document.getElementById("botonFallecido").style.display="none";
	   
	   document.DatosColegiacionForm.modo.value = "darBaja";	
	   document.DatosColegiacionForm.target="submitArea";
	     document.DatosColegiacionForm.submit();
		 document.DatosColegiacionForm.fechaEstado.disabled=true;
		 document.getElementById("calendario").style.visibility = "hidden";
	   
	   document.DatosColegiacionForm.motivo.disabled=true;
	
	   
	
  }
  
  function bajaFallecido(){
     
	    if (document.DatosColegiacionForm.fechaEstado.value==""){
	    mensaje = '<siga:Idioma key="censo.consultaDatosColegiacion.error.fechaBaja"/>'
		alert(mensaje);
		return;
	   }  
   
   
       document.getElementById("activo").style.display="none";
	   document.getElementById("baja").style.display="block";
	   
	   document.getElementById("botonBaja").style.display="none";
	   document.getElementById("botonAlta").style.display="block";
	   document.getElementById("botonFallecido").style.display="none";
	   
	   document.DatosColegiacionForm.modo.value = "bajaFallecido";	
	    document.DatosColegiacionForm.motivo.value='<siga:Idioma key="censo.consultaDatosColegiacion.literal.motivoFallecido"/>';
	   document.DatosColegiacionForm.target="submitArea";
	     document.DatosColegiacionForm.submit();
		 document.DatosColegiacionForm.fechaEstado.disabled=true;
		
		 document.getElementById("calendario").style.visibility = "hidden";
	   
	   document.DatosColegiacionForm.motivo.disabled=true;
	
	   
	
  }
  
  function refrescarLocal() {
  	document.location=document.location;
  }
  
  function darAlta(){
    
   
       document.getElementById("activo").style.display="block";
	   document.getElementById("baja").style.display="none";
	   document.getElementById("botonBaja").style.display="block";
	   document.getElementById("botonAlta").style.display="none";
	   document.getElementById("botonFallecido").style.display="block";
	  
	   document.DatosColegiacionForm.fechaEstado.disabled=false;
	   document.DatosColegiacionForm.motivo.disabled=false;
	    document.DatosColegiacionForm.fechaEstado.value="";
		document.getElementById("calendario").style.visibility = "visible";
		document.DatosColegiacionForm.motivo.value="";
			   document.DatosColegiacionForm.target="submitArea";
	   document.DatosColegiacionForm.modo.value = "activar";	
	  					
       document.DatosColegiacionForm.submit();
	  
	   
	
  }
  function situacionLetrado(){
  

  
     if (document.DatosColegiacionForm.activar.value=="1"){
	  
		
		 document.getElementById("activo").style.display="none";
	     document.getElementById("baja").style.display="block";
		
	   <%if (modo.equals("ver")){%>
	       document.getElementById("botonBaja").style.display="none";
	       document.getElementById("botonAlta").style.display="none";
		   document.getElementById("botonFallecido").style.display="none";
		  
	   <%}else{%>	   
	      <%
           if (!user.getAccessType().equalsIgnoreCase(SIGAConstants.ACCESS_FULL)){%>	
	   		document.getElementById("botonBaja").style.display="none";
		    document.getElementById("botonAlta").style.display="none";
			document.getElementById("botonFallecido").style.display="none";
			document.DatosColegiacionForm.fechaEstado.disabled=true;
	        document.DatosColegiacionForm.motivo.disabled=true;
			document.getElementById("calendario").style.visibility = "hidden";
		 <%}else{%>	
		    document.getElementById("botonBaja").style.display="none";
		    document.getElementById("botonAlta").style.display="block";
			document.getElementById("botonFallecido").style.display="none";
	        document.DatosColegiacionForm.fechaEstado.disabled=true;
	        document.DatosColegiacionForm.motivo.disabled=true;
			document.getElementById("calendario").style.visibility = "hidden";
			
		<%}
		}%>	
	   
	 }else{
	     document.getElementById("activo").style.display="block";
	     document.getElementById("baja").style.display="none";
	     
		 <%if (modo.equals("ver")){%>
	       document.getElementById("botonBaja").style.display="none";
	       document.getElementById("botonAlta").style.display="none";
		   document.getElementById("botonFallecido").style.display="none";
		   
	   <%}else{
	       
           if (!user.getAccessType().equalsIgnoreCase(SIGAConstants.ACCESS_FULL)){%>	
		    document.getElementById("botonBaja").style.display="none";
		    document.getElementById("botonAlta").style.display="none";
			document.getElementById("botonFallecido").style.display="none";
			
	        document.DatosColegiacionForm.fechaEstado.disabled=true;
	        document.DatosColegiacionForm.motivo.disabled=true;
			document.getElementById("calendario").style.visibility = "hidden";
		 <%}else{%>
	   	   document.getElementById("botonBaja").style.display="block";
     	   document.getElementById("botonAlta").style.display="none";
		   document.getElementById("botonFallecido").style.display="block";
	       document.DatosColegiacionForm.fechaEstado.disabled=false;
		  
		   document.getElementById("calendario").style.visibility = "visible";
		  
	       document.DatosColegiacionForm.motivo.disabled=false;
	       document.DatosColegiacionForm.fechaEstado.value="";
		   document.DatosColegiacionForm.motivo.value="";
	   <%}
	   }%>
	  
	 }
	
	 document.DatosColegiacionForm.modo.value="buscarDatosColegiacion";
	 document.DatosColegiacionForm.target="resultadoDatosColegiacion";
	 document.DatosColegiacionForm.submit();

  }
   

</script>
<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DatosColegiacionForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="censo.fichaCliente.situacion.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

	
	</head>

	<body class="tablaCentralCampos" onLoad="situacionLetrado()";>

		<!-- ******* INFORMACION GENERAL CLIENTE ****** -->
		<table class="tablaTitulo" align="center" cellspacing=0>
			<tr><td class="titulitosDatos">
					<siga:Idioma key="censo.consultaDatosColegiacion.literal.titulo1"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;&nbsp;
					<% if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<% }else{ %>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>					
			</td></tr>
		</table>

			<!-- CAMPOS DEL REGISTRO -->
		<table class="tablaCentralCampos" align="center" height="10%">		

			<html:form action="/CEN_DatosColegiacion.do" method="POST" target="submitArea33">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property="idPersona" value="<%=idPersona.toString()%>"/> 				
			<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/> 
			<input type="hidden" name="activar" value="<%=activar%>">
			<input type="hidden" name="nombre" value="<%=nombre%>">
			<input type="hidden" name="numero" value="<%=numero%>">
			<input type="hidden" name="accion" value="<%=(String)request.getAttribute("ACCION")%>">
			<input type="hidden" name="idInstitucionPersona" value="<%=idInstitucion%>">
			
														

			<tr>				
				<td width="100%" align="center" >

					<!-- SUBCONJUNTO DE DATOS -->
	
					<siga:ConjCampos leyenda="censo.consultaDatosColegiacion.literal.situacionLetrado">

						<table align="center" width="100%" border="0" cellpadding="0"  cellspacing="0" height="20%">
						  <tr  >
							 <td width="6%">
							  <table>
							   <tr>
								<td id="activo"  valign="middle" style="display:block" align="center" class="txGrandeActivo" width="30%" >
									<siga:Idioma key="censo.consultaDatosColegiacion.literal.activo"/></td>
								<td id="baja"  valign="middle"  style="display:none" align="center"  class="txGrandeBaja" width="30%" >
									<siga:Idioma key="censo.consultaDatosColegiacion.literal.baja"/>
								</td>
							   </tr>	
							  </table>	
							 </td> 	
							<td width="64%">
 							  <table align="center" width="100%" border="0" >
							   <tr>
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiacion.literal.fechaBaja"/>&nbsp;(*)
								</td>				
								
								<td align="left" width="120" >
							           <html:text name="DatosColegiacionForm" property="fechaEstado" size="9" maxlength="10" styleClass="<%=estilo%>" readonly="true" value="<%=fecha%>"></html:text>
								  <%if (!modo.equals("ver")){%>
								           <a  name="calendario" href='javascript://'onClick="return showCalendarGeneral(fechaEstado);"><img  src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
								  <%}%>		   
								</td>	
						        
								
							  
							
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosColegiacion.literal.motivoBaja"/>
								</td>
								<td >
									<html:text name="DatosColegiacionForm" property="motivo" size="40" maxlength="255" styleClass="<%=estilo%>" value="<%=motivo%>"></html:text>
								</td>
							  </tr>
							 </table>
							</td>  
						
							<td width="30%">
							
							  <table cellpadding="0" cellspacing="0" align="center"  width="100%">
							   <tr>
								<td id="botonBaja"align="center" valign="middle" style="display:none">
								  <html:button property="botonBaja" onclick="return darBaja();" styleClass="button"> 
								    <siga:Idioma key="censo.consultaDatosColegiacion.literal.darBaja"/>
   								  </html:button> 
								</td>  
								<td id="botonAlta"   align="center" valign="middle" style="display:none">
								  <html:button property="botonAlta" onclick="return darAlta();" styleClass="button"> 
								    <siga:Idioma key="censo.consultaDatosColegiacion.literal.darAlta"/>
   								  </html:button> 
								</td>  
								<td id="botonFallecido"   align="center" valign="middle" style="display:none">
								  <html:button property="botonFallecido" onclick="return bajaFallecido();" styleClass="button"> 
								    <siga:Idioma key="censo.consultaDatosColegiacion.literal.bajaFallecido"/>
   								  </html:button> 
								</td>  
							   </tr>	
							 </table>	
						  			
							</td>	
						 
						 </tr>				
										
						</table>								

					</siga:ConjCampos>

				</td>
			</tr>
		</table>		
<table width="100%" align="center"  >		
	<tr>				
		<td width="100%" align="center" >
		<siga:ConjCampos leyenda="censo.consultaDatosColegiacion.literal.sancionesLetrado">
			<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado"
							name="resultado" 
							scrolling="no"
							frameborder="0"
							marginheight="5"
							marginwidth="5"					 
							style="width:100%; height:200px;">
			</iframe>
		</siga:ConjCampos>	
		</td>
	</tr>
</table>									

<table width="100%" align="center">		
	<tr>				
		<td width="100%" align="center">
		<siga:ConjCampos leyenda="censo.consultaDatosColegiacion.literal.colegiaciones">
			<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultadoDatosColegiacion"
							name="resultadoDatosColegiacion" 
							scrolling="no"
							frameborder="0"
							marginheight="5"
							marginwidth="5"
							style="width:100%;height:150px; ">
			</iframe>
		</siga:ConjCampos>	
		</td>
	</tr>
</table>									
		
		</html:form>

	<siga:ConjBotonesAccion botones="<%=botonesAccion %>"  modo="<%=modo%>" idBoton="4#5"  idPersonaBA="<%=idPersona.toString()%>" idInstitucionBA="<%=idInstitucion%>" clase="botonesDetalle" />

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

<!-- RGG para buscar las sanciones -->
<html:form action="/CEN_SancionesLetrado.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscar" />
		<input type="hidden" name= "accionModal" value = "" >
		<input type="hidden" name="accion" value="<%=(String)request.getAttribute("ACCION")%>">
		<html:hidden property = "idPersona" value = "<%=idPersona.toString() %>" />
		<html:hidden property = "idInstitucionAlta" value = "<%=user.getLocation() %>" />
</html:form>

		
<script>
	function buscar() {
		document.SancionesLetradoForm.submit();
	}
	document.body.onLoad=buscar();
</script>

<!-- FIN para buscar las sanciones -->


<!-- FIN para buscar las colegiaciones -->

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<iframe name="submitArea33" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
