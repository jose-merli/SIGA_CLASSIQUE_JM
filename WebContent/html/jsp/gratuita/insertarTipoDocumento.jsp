<!-- insertarTipoDocumento.jsp -->
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
	
	String tipo="";
	tipo=(String)request.getSession().getAttribute("idTipoDoc");	
	ses.removeAttribute("idTipoDoc");	
	int pcajgActivo = 0;	
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute(
				"pcajgActivo").toString());
	}
	String asterisco = "&nbsp(*)&nbsp";
	boolean codigoExtObligatorio = false;	
	if (pcajgActivo == 5){
		codigoExtObligatorio=true;
	}	
	
%>

<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.documentacionEJG.localizacion.nuevoTipo" 
		localizacion="gratuita.documentacionEJG.localizacion"/>
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

	<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "InsertarTipo"/>
	<html:hidden property = "actionModal" value = ""/>	
	<html:hidden property = "tipoDocumento" value = "<%=tipo%>"/>

		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="sjcs.ejg.documentacion.tipoDocumentacion">
			<table align="center" width="100%">
			
				<!-- FILA -->	
				<tr>	
					<td class="labelText" valign="top">
						<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>(*)
					</td>
					<td valign="top">
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="30" maxlength="60" styleClass="box" value=""></html:text>
					</td>
					<td class="labelText" valign="top">
						<siga:Idioma key="general.codeext"/>
						<%
							if (codigoExtObligatorio) {
						%>
							<%=asterisco%> 
						<%
 							}
 						%>		
					</td>
					<td >
						<html:text name="DefinirMantenimDocumentacionEJGForm" property="codigoExt" size="9" maxlength="10" styleClass="box" value=""></html:text>
					</td>
					
				</tr>
				
				<tr>
				</tr>
				<tr>				
					<td class="labelText" valign="top">
						<siga:Idioma key="gratuita.maestros.documentacionEJG.nombre"/>(*)
					</td>
					<td valign="top">
						<textarea name="descripcion" rows="5" cols="60" onkeydown="cuenta(this,200)" onChange="cuenta(this,200)" class="box"></textarea>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>
		
	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="G,R" clase="botonesSeguido" titulo="sjcs.ejg.documentacion.documentacion"/>		
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
		
		//Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar
		function accionGuardar() 
		{	sub();
			var nombre = document.forms[0].descripcion.value;
			var contenido = document.forms[0].abreviatura.value;			
			var codigoExt = document.forms[0].codigoExt.value;
			if (nombre != "") {
				if (contenido!="") {
					var error = "";
					if (<%=codigoExtObligatorio%> && document.forms[0].codigoExt.value==""){
						error += "<siga:Idioma key='errors.required' arg0='general.codeext'/>"+ '\n';
						 if(error!=""){
							  alert(error);
							  fin();
							  return false;
							 }	
					}else {
						document.forms[0].submit();
						}					  
				}
				else{
					alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoAbreviatura"/>');
					fin();
					return false;
				}
			}
			else{
				 alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoDescripcion"/>');
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
	<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"/>

	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Nuevo		
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

		//Asociada al boton Volver
		function accionVolver() 
		{
			document.DefinirMantenimDocumentacionEJGForm.modo.value="abrir";
			document.DefinirMantenimDocumentacionEJGForm.target="mainWorkArea"; 
			document.DefinirMantenimDocumentacionEJGForm.submit(); 
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
	</body>
</html>
