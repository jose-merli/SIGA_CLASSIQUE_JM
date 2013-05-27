<!-- envios_Edit.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String idInstitucion[] = {user.getLocation()};
	String envioSms = (String)request.getAttribute("smsHabilitado");
	String consultaPlantillas="";
	if (envioSms!=null && envioSms.equalsIgnoreCase("S")){
		consultaPlantillas = "cmbTipoEnviosInstSms";
	}else{
		consultaPlantillas = "cmbTipoEnviosInst";
	}
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="DefinirEnviosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

	
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="envios.definir.literal.titulo"/>
			</td>
		</tr>
	</table>	


	<html:form action="/ENV_DefinirEnvios" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "acuseRecibo" value = "Insertar"/>
	


		<fieldset>
		<table class="tablaCampos" align="center">
			<html:hidden name="DefinirEnviosForm" property="idEnvio"/>
			<html:hidden name="DefinirEnviosForm" property="idEnvioBuscar" value=""/>
			<tr>	
				<td class="labelText">
					<siga:Idioma key="envios.definir.literal.nombre"/>&nbsp;(*)
				</td>				
				<td>
					<html:text name="DefinirEnviosForm" property="nombre" size="50" maxlength="100" styleClass="box"></html:text>							
				</td>		
			</tr>	
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.definir.literal.tipoenvio"/>&nbsp;(*)
				</td>
				<td>	
					<siga:ComboBD nombre = "comboTipoEnvio" tipo="<%=consultaPlantillas%>" clase="boxCombo" obligatorio="true" ancho="330" parametro="<%=idInstitucion%>" accion="Hijo:comboPlantillaEnvio"/>						
				</td>
				
			</tr>
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.plantillas.literal.plantilla"/>&nbsp;(*)
				</td>
				<td>	
					<siga:ComboBD nombre = "comboPlantillaEnvio" tipo="cmbPlantillaEnvios2" clase="boxCombo" obligatorio="true" hijo="t" ancho="330" accion="Hijo:idPlantillaGeneracion"/>
				</td>
				
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
				</td>
				<td>
					<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" clase="boxCombo" ancho="330" obligatorio="false" hijo="t" pestana="true"/>
				</td>
			</tr>

		</table>
		</fieldset>


	</html:form>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />


	<!-- INICIO: SCRIPTS BOTONES -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			sub();
			if (validateDefinirEnviosForm(document.DefinirEnviosForm)){
				//JTA Esto comentado funciona si se desea evitar que existan envios ordinarios sin plantilla definida
				//var insTipoEnvio = document.forms[0].comboTipoEnvio.value;
				//var opcion_array=insTipoEnvio.split(",");
				//if (opcion_array[1]=='2') {
					//if (document.forms[0].idPlantillaGeneracion.value==""){
						//var campo = '<siga:Idioma key="envios.definir.literal.plantillageneracion"/>';
  						//var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
						//alert (msg);
						//fin();
						//return false;					
					//} 
				//}
				var cmbPlantillaEnvio = document.getElementsByName("comboPlantillaEnvio")[0];
				var opcionArray=cmbPlantillaEnvio.value.split(",");
				var idTipoEnvio = opcionArray[2]; 
				if(idTipoEnvio=='1'){
					DefinirEnviosForm.acuseRecibo.value = opcionArray[3] 
				}
				DefinirEnviosForm.submit();
			}else{
				fin();
			}
		}

		//<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
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
