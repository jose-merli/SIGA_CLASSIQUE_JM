<!-- insertarDictamen.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsAreaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	String[] dato={usr.getLocation()};
	//ArrayList fundamentoSel = new ArrayList();
	//ArrayList tipoDictamenSel = new ArrayList();
	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DefinirMantenimDictamenesEJGForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.dictamenesEJG.localizacion.nuevo" 
		localizacion="gratuita.dictamenesEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script>

		//Refresco
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}	

	</script>
	
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<html:form action="/JGR_MantenimientoDictamenesEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "InsertarDictamen"/>
	<html:hidden property = "actionModal" value = ""/>


		<!-- SUBCONJUNTO DE DATOS -->
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="gratuita.busquedaEJG.dictamen">
			<table align="center" width="100%">
				<tr>				
					<td class="labelText" >
						<siga:Idioma key="general.codeext"/>&nbsp;(*)
					</td>				
					<td >
							<html:text name="DefinirMantenimDictamenesEJGForm" property="codigoExt" size="30" styleClass="box" value=""></html:text>
					</td>	
				</tr>
				<tr>	
					<td class="labelText" valign="top">
						<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;(*)
					</td>				
					<td >
						
							<html:text name="DefinirMantenimDictamenesEJGForm" property="abreviatura" size="30" styleClass="box" value=""></html:text>
					</td>				
					<td class="labelText" valign="top">
						<siga:Idioma key="general.description"/> &nbsp;(*)
					</td>
					<td >
							<textarea name="descripcion" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" rows="5" cols="60" class="box"> </textarea>
					</td>
				</tr>
				<tr>				
					<td class="labelText">
						<siga:Idioma key="gratuita.operarDictamen.literal.fundamentoclf"/>&nbsp;(*)
					</td>				
					<td >
						<siga:ComboBD nombre="idFundamento" tipo="cmbFundamentoCalif" ancho="200" obligatorio="false" parametro="<%=dato%>" clase="boxCombo" readonly="false"/>
					</td>	
					<td class="labelText" >
						<siga:Idioma key="gratuita.operarDictamen.literal.tipoDictamen"/>&nbsp;(*)
					</td>				
					<td>
						<siga:ComboBD nombre="idTipoDictamen" tipo="cmbTipoDictamen" ancho="200" obligatorio="false" parametro="<%=dato%>" clase="boxCombo" readonly="false"/>
					</td>	
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>
		
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->

	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function buscar() 
		{		
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();		
		}
		
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{		
		  	sub();
		  	if (validateDefinirMantenimDictamenesEJGForm(document.DefinirMantenimDictamenesEJGForm)) {
				document.forms[0].submit();
			}else{
				fin();
				return false;
			} 
		}		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"		
				id="resultado"
				name="resultado" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0";
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<siga:ConjBotonesAccion botones="V,G,R" clase="botonesDetalle"/>

	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Nuevo -->		
		function accionNuevo() 
		{
			if(document.forms[0].tipoDocumento.value==null){
				alert('<siga:Idioma key="gratuita.insertarArea.message.insetarAreaPrimero"/>');
			}else{
				document.forms[0].modo.value = "nuevo2";
				var resultado=ventaModalGeneral(document.forms[0].name,"P");
				if(resultado=='MODIFICADO') buscar();
			}
		}

		//Asociada al boton Volver -->
		function accionVolver() 
		{
			document.DefinirMantenimDictamenesEJGForm.modo.value="abrir";
			document.DefinirMantenimDictamenesEJGForm.target="mainWorkArea"; 
			document.DefinirMantenimDictamenesEJGForm.submit(); 
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
	</body>
</html>
