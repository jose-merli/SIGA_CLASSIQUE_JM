<!-- datosCartaEJG.jsp -->
<!-- 
	 Permite recoger datos para la generacion de cartas de EJG
	 VERSIONES:
	 miguel.villegas 24-05-2005 
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
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	
	
%>	

<html>
<!-- HEAD -->

 <script language="JavaScript">
   function init(){
    MantenimientoServiciosForm.radioAlta.value=0;
    jQuery("#chkSolicitudBaja").attr("disabled","disabled");
	
   }
   function habilitarCheck(check){
 
    if (check.value=="1"){
     jQuery("#chkSolicitudBaja").removeAttr("disabled");
	 MantenimientoServiciosForm.fechaAlta.value="";
	 jQuery("#fechaAlta").attr("disabled","disabled");
 
	}else{
     MantenimientoServiciosForm.chkSolicitudBaja.checked=false;
     jQuery("#chkSolicitudBaja").attr("disabled","disabled");
	 jQuery("#fechaAlta").removeAttr("disabled");
	}
   }
   function accionGuardar(){
   
    if (document.forms[0].chkSolicitudBaja.checked==true){
	 document.forms[0].chkSolicitudBaja.value="1";
	}else{
	 document.forms[0].chkSolicitudBaja.value="0";
	}
   
    document.forms[0].modo.value = "eliminarSuscripcion";
   	var f = document.forms[0].name;	
	
   	window.frames.submitArea.location = '/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName=' + f + '&msg=messages.wait';
	document.forms[0].target='submitArea';

   }
   
   function accionCerrar(){
     window.top.close();
   }
   
			
</script>
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body onload="init();">

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="pys.mantenimientoServicios.EliminarSuscripcionAutomatica.configurar"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			
<div id="camposRegistro"  class="posicionModalPeque" align="center">

           <table  class="tablaCentralCamposPeque" align="center">
				<html:form action="/PYS_MantenimientoServicios" method="POST" >
					<html:hidden property = "modo" value = ""/>
					<html:hidden property="idInstitucion" />
				    <html:hidden property="idTipoServicios" />
					<html:hidden property="idServicio" />
					<html:hidden property="idServiciosInstitucion" />	
					<tr>				
						<td>
					
							<siga:ConjCampos leyenda="pys.mantenimientoServicios.EliminarSuscripcionAutomatica.condicion">						
				              <table  class="tablaCampos" align="center" border="0" >	
							  
									<!-- FILA -->
									<tr>				
										<td  class="labelText"  width="150px">
										    <html:radio name="MantenimientoServiciosForm"  property="radioAlta" value="0" onclick="habilitarCheck(this);" ></html:radio>
											<siga:Idioma key="pys.busquedaServicios.AltasBajas"/>
										</td>	
										
										<td class="labelText"  width="200px" align="right">
											<siga:Idioma key="pys.busquedaServicios.Fecha"/>
										</td>	
										<td align="left" width="140px">
											 <html:text  name="MantenimientoServiciosForm" property="fechaAlta" styleId="fechaAlta" styleClass="box" size="10" readonly="true" />
											 <a href='javascript://'onClick="return showCalendarGeneral(fechaAlta);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
										</td>	
										
									</tr>				
									<tr>
									
										
										<td  class="labelText"  >
										<html:radio name="MantenimientoServiciosForm"  property="radioAlta" value="1" onclick="habilitarCheck(this);"></html:radio>
										<siga:Idioma key="pys.busquedaServicios.Bajas"/>
										</td>
													
										<td class="labelText" valign="middle" align="right">
											<siga:Idioma key="pys.busquedaServicios.Manuales"/>
										</td>	
										<td align="left">
											 <html:checkbox  name="MantenimientoServiciosForm" property="chkSolicitudBaja"  styleId="chkSolicitudBaja"/>
										</td>	
									</tr>	
									
								</table>	
							</siga:ConjCampos>		
								</td>
					</tr>
				</html:form>	
				
			</table>
				
				<siga:ConjBotonesAccion botones="G,C" modo='' modal="P"/>
				
				
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			
		
			
			
	
			

		
		
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		</div>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>

