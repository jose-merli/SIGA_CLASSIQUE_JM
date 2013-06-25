<!-- pestanaCorreoElectronico.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%
	String nombrePlantilla   = (String)request.getAttribute("nombrePlantilla");
%>

<html>
	<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/tiny_mce/tiny_mce.js'/>"></script>
	<script type="text/javascript">
		tinyMCE.init({
			mode : "textareas",
			theme : "advanced",
			plugins : "pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,inlinepopups",
	
			// Theme options
			theme_advanced_buttons1 : "newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,formatselect,fontselect,fontsizeselect,|,hr",
			theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,image,cleanup,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
			theme_advanced_buttons3 :"",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom"
		
		
		});
	</script>
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton Volver -->
			function accionVolver()
			{
				PlantillasEnviosCorreoElectronicoForm.target="mainWorkArea";
				PlantillasEnviosCorreoElectronicoForm.action = "/SIGA/ENV_DefinirPlantillas.do?noreset=true";
				PlantillasEnviosCorreoElectronicoForm.modo.value="abrirConParametros";
				PlantillasEnviosCorreoElectronicoForm.submit();
			}
	
			function accionGuardar()
			{
				sub();
				PlantillasEnviosCorreoElectronicoForm.target="submitArea";
				PlantillasEnviosCorreoElectronicoForm.action = "/SIGA/ENV_Campos_Correoe.do";
				PlantillasEnviosCorreoElectronicoForm.modo.value="Modificar";
				PlantillasEnviosCorreoElectronicoForm.submit();
			}

			function refrescarLocal()
			{
				parent.buscar();
			}
			function abrirAyuda() {
				
		
				var alto = 580, ancho = 800;
					  
		 		var posX = (screen.width - ancho) / 2;
     		var posY = (screen.height - alto) / 2;
     		var medidasWin = "height=" + alto + ", width=" + ancho + ", top=" + posY + ", left=" + posX;
 			
				w = window.open ("/SIGA/html/jsp/envios/paginaAyuda.jsp",
						   				   "", 
										     "status=0, toolbar=0, location=0, menubar=0, resizable=1," + medidasWin);
		}	
	
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirTiposPlantillas.correoElectronico.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
		/>
	</head>

	<body>

				<table class="tablaTitulo" align="center" cellspacing="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
							<siga:Idioma key="envios.plantillas.literal.plantilla"/>:&nbsp;<%=nombrePlantilla%>
						</td>
					</tr>
				</table>
	
		<html:form action="/ENV_Campos_Correoe" method="POST">
			<html:hidden property="modo" value=""/>

			<html:hidden property="idInstitucion"/>
			<html:hidden property="idTipoEnvio"/>
			<html:hidden property="idPlantillaEnvios"/>
			<bean:define id="botones" name="PlantillasEnviosCorreoElectronicoForm" property="botones" type="java.lang.String"/>
				<table class="tablaCampos" align="center" width="100%">
					<tr>
						<td class="labelText" width="15%">
							<siga:Idioma key="envios.plantillas.literal.asunto"/>
						</td>
						<td width="70%">
							<html:text property="asunto"  styleClass="boxCombo" style="width:640" readonly="${!PlantillasEnviosCorreoElectronicoForm.editable}"/>
						</td>
						<td width="15%" align="left">
			 			 <a href="javascript:abrirAyuda();"><img border=0 src="<html:rewrite page='/html/imagenes/help.gif'/>"  alt="<siga:Idioma key="general.ayuda.normativa"/>"></a>
			 			 
						</td>
						
						
					</tr>
					<tr>				
						<td class="labelText">
							<siga:Idioma key="envios.plantillas.literal.cuerpo"/>
						</td>
						<td colspan="2">
							<html:textarea property="cuerpo" rows="35" style="width:640" readonly="${!PlantillasEnviosCorreoElectronicoForm.editable}"/>
						</td>
						
						
					</tr>
				</table>
				
				
		


		<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/>
		</html:form>

		<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>
	</body>
</html>