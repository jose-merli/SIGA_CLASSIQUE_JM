<!DOCTYPE html>
<html>
<head>

<!-- HEAD -->

<!-- configperfilcolegio.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>


<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.ws.form.BusquedaRemesasForm"%>
<%@ page import="com.siga.censo.ws.form.NuevaRemesaForm"%>
<%@ page import="com.siga.censo.ws.form.NuevaColumnaPerfilForm"%>
<%@ page import="org.redabogacia.sigaservices.app.vo.ColumnaPerfilVO"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ConModuloBean"%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Insert title here</title>

</head>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validation.js'/>"></script>   
	
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	
	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
	

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.perfil.colegio.titulo" localizacion="censo.ws.gestionremesas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="ConfigPerfilColegioForm" staticJavascript="false" />
	

	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
			
</head>


<body>


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<div id="camposRegistro" class="posicionBusquedaSolo" align="center">

		<!-- INICIO: CAMPOS DE BUSQUEDA-->
		<!-- Zona de campos de busqueda o filtro -->
		
		<table  class="tablaCentralCampos"  align="center">
			<tr>	
				<td>
					<siga:ConjCampos leyenda="censo.perfil.colegio.titulo">
						<table class="tablaCampos" align="center">
							<html:form action="/CEN_ConfigPerfilColegio.do?noReset=true" method="POST" target="resultado">
								<html:hidden name="ConfigPerfilColegioForm" property = "modo"/>
													
								<!-- FILA -->
								<tr>	
									<td class="labelText">
										<siga:Idioma key="censo.ws.literal.colegio"/>
									</td>
									<td>
										<html:select property="idColegio" name="ConfigPerfilColegioForm" styleClass="boxCombo" styleId="idColegio">
											<html:option value="">&nbsp;</html:option>
											<html:optionsCollection name="ConfigPerfilColegioForm" property="instituciones" value="id" label="nombre"></html:optionsCollection>
										</html:select>
									</td>
								</tr>
							</html:form>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
		
		<html:form action="/CEN_ConfigPerfilColegio" method="POST" target="mainWorkArea">
			<html:hidden name="ConfigPerfilColegioForm" property = "modo"/>
			<html:hidden property="idModulo" value="<%=ConModuloBean.IDMODULO_CENSO%>"/>
			<html:hidden property="modo" value="ver"/>
			<html:hidden property="accionAnterior" value="${path}"/>
		</html:form>

		<!-- FIN: CAMPOS DE BUSQUEDA-->
		<%  
			String botones = "N,B";
						 
		%>

		<siga:ConjBotonesBusqueda botones="<%=botones %>"  titulo="menu.censo.gestionCensoWS.gestionRemesas" />
		<!-- FIN: BOTONES BUSQUEDA -->

		<!-- INICIO: IFRAME LISTA RESULTADOS -->
	
	   		<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0" 
				marginwidth="0"					 
				class="frameGeneral">
	  		</iframe>
	  	
		<!-- FIN: IFRAME LISTA RESULTADOS -->
		<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
		
	</div>	
	
	
	<!-- <div id="dialogoNuevoFilaPerfil"  title='<bean:message key="censo.dialogo.nuevoExcel"/>' style="display:none"> -->
	<div id="dialogoNuevoFilaPerfil" title='<bean:message key="censo.perfil.literal.titulo.dialog"/>'	style="display: none">
		<div class="labelTextArea"><bean:message key="censo.perfil.literal.titulo.dialog"/></div>
		</br>
		<siga:ConjCampos>

			<html:form action="/CEN_NuevaColumnaPerfil.do?noReset=true" styleId="idNuevaColumPerfilForm" method="POST" target="submitArea" enctype="multipart/form-data">
				<html:hidden name="NuevaColumnaPerfilForm" property="modo" />
				<div class="labelText">
					<label for="nombreColumna"
						style="width: 140px; float: left; color: black"><bean:message key="censo.perfil.literal.nombrecolumna"/></label>
					<html:text name="NuevaColumnaPerfilForm" property="nombreColumna"
						title="nombreColumna"></html:text>
				</div>
				</br>
				<div class="labelText">
					<label for="file" style="width: 140px; float: left; color: black"><bean:message key="censo.perfil.literal.tipocolumna"/></label>
					<html:select property="tipoColumna" name="NuevaColumnaPerfilForm">
						<html:option value="">
							<siga:Idioma key="general.combo.seleccionar" />
						</html:option>
						<html:optionsCollection name="ConfigPerfilColegioForm"
							property="tiposColumna" value="idCol" label="nombre"></html:optionsCollection>
					</html:select>
				</div>
				</br>
				<div class="labelText">
					<label for="file" style="width: 140px; float: left; color: black"><bean:message key="censo.ws.literal.colegio"/></label>
					<label id="idColegioInsertar" style="width: 140px; float: left; color: black"></label> 
					<input type="hidden" name="idColegioInsertar" id="idColegioInsertarhid" value=""/>
				</div>

			</html:form>
		</siga:ConjCampos>
	</div>



	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
			jQuery.noConflict();
			
			function refrescarLocal(){
				
				if (jQuery("#dialogoNuevoFilaPerfil").is(':visible')){
					closeDialog('dialogoNuevoFilaPerfil');
				} 
				
			}
			
			function buscar() {
				
				var sel= jQuery("#idColegio").find(":selected").val();
				
				error = '';
				
				if(sel==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.colegio'/>"+ '\n';
				}
				if (error!=''){
					alert(error);
				
					return false;
				} else {
					
					ajusteAlto('resultado');
					sub();
					document.forms[0].modo.value="buscar";
					document.forms[0].target="resultado";	
					document.forms[0].submit();		
				}
							
			}
			
			
			function nuevo() {
				
				jQuery("#idNuevaColumPerfilForm")[0].reset();	
				var sel= jQuery("#idColegio").find(":selected").val();
				
				error = '';
				
				if(sel==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.colegio'/>"+ '\n';
				}
				if (error!=''){
					alert(error);
					return false;
				} else {
					
					jQuery("#dialogoNuevoFilaPerfil").dialog(
							{
							      height: 300,
							      width: 600,
							      modal: true,
							      resizable: false,						      
							      buttons: {
							    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionInsercion(); }},
							          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog("dialogoNuevoFilaPerfil");}}
							      }
							}
						);
						
					jQuery(".ui-widget-overlay").css("opacity","0");
					
				}
			}
			
		
			jQuery("#idColegio").on('change', function() {
				cambiaColegio( jQuery(this).find(":selected"));
			});
			
			
			function cambiaColegio(sel){
				
				var textval =sel.text();
				jQuery("#idColegioInsertar").text(textval);
			
			}
			
		
			function accionInsercion(){
				//sub();
				document.forms['NuevaColumnaPerfilForm'].modo.value = "insertar";
				
				error = '';
				if(document.forms['NuevaColumnaPerfilForm'].nombreColumna.value==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.colegio'/>"+ '\n';
					
				}
				if(document.forms['NuevaColumnaPerfilForm'].tipoColumna.value==''){
					error += "<siga:Idioma key='errors.required' arg0='censo.ws.literal.fechaExportacion'/>"+ '\n';
					
				}
			
				
				if (error!=''){
					alert(error);
					//fin();
					return false;
				} else {
					var sel= jQuery("#idColegio").find(":selected").val();
					jQuery("#idColegioInsertarhid").val(sel);
					jQuery("#idNuevaColumPerfilForm").submit();
					refrescarLocal();
				}
	
			}
			
			
			function closeDialog(dialogo){
				 jQuery("#"+dialogo).dialog("close"); 
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

</body>
</html>