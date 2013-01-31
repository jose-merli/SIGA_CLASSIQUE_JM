<!-- documentos_edit.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String archivo = UtilidadesString.getMensajeIdioma(userBean,"certificados.mantenimiento.literal.archivo");
	String obligatorio = UtilidadesString.getMensajeIdioma(userBean,"messages.campoObligatorio.error");
	
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!-- Validaciones en Cliente -->
		<html:javascript formName="DocumentosForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>			

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				//Compruebo que es un fichero con extension .PDF
				
				if (validateDocumentosForm(document.DocumentosForm)){
					var doc = document.forms[0].theFile.value;
					var extension = doc.substring(doc.lastIndexOf('.')+1,doc.legth);
					var testear = isFicheroPermitido(document.DocumentosForm.theFile.value, ['COM', 'EXE']);
					if (testear==false) {
						fin();
						alert("<siga:Idioma key="messages.envios.error.FormatoIncorrecto"/>");
						return false;
					}
					if (document.forms[0].modo.value=="insertar" && doc=="") {
						alert("<%=archivo%>"+" "+"<%=obligatorio%>");
						fin();
						return false;
					} else {
						var f=document.getElementById("DocumentosForm");
		    			var fname = document.getElementById("DocumentosForm").name;
		    			//Ventana de espera:
					    window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.wait';
					    window.top.returnValue="MODIFICADO";
					}
				}else{
					fin();
				}				
				
			}
			
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
	
		<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="pestana.envios.documentos"/>
			</td>
		</tr>
	</table>		

			<html:form action="/ENV_Documentos.do" method="POST" target="submitArea" enctype="multipart/form-data">

				<html:hidden property = "hiddenFrame" value = "1"/>				
				
				<html:hidden name="DocumentosForm" property="modo"/>				
				<html:hidden name="DocumentosForm" property="idInstitucion"/>
				<html:hidden name="DocumentosForm" property="idEnvio"/>
				<html:hidden name="DocumentosForm" property="idDocumento"/>
				
				
				<table class="tablaCentralCamposPeque" align="center">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="pestana.envios.documentos">
								<table class="tablaCampos" align="center">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="envios.definir.literal.descripcion"/>&nbsp(*)
										</td>
										<td>
											<html:text name="DocumentosForm" property="descripcion" size="30" maxlength="100" styleClass="boxCombo"/>
										</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="certificados.mantenimiento.literal.archivo"/>&nbsp(*)
										</td>				
										<td>
											<html:file name="DocumentosForm" property="theFile" size="30" styleClass="boxCombo"  />
										</td>
									</tr>									
									<tr>				
										<td class="labelTextValor" colspan="2" align="center">
											<siga:Idioma key="messages.envios.error.FormatoIncorrecto"/>
										</td>				
									</tr>									
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->

			<siga:ConjBotonesAccion botones="Y,C" modal="P"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>