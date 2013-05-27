<!-- datosSufijos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 	prefix = "html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.FacSufijoBean"%>
<%@ page import = "com.atos.utils.ClsConstants"%>
<%@ page import = "com.siga.facturacion.form.SufijosForm"%>

<!-- JSP -->
<% 
		//Datos  del request:
		String app=request.getContextPath(); 
		String modo = (String)request.getAttribute("modo");
		SufijosForm miForm = (SufijosForm)request.getAttribute("sufijosForm");
		
		//Datos usados en el JSP:
		String clase="box", claseSufijo="box";
		String accion = "";
		boolean lectura=false, lecturaSufijo=false;
		String sufijo="", concepto="", varios="";

		//MODO NUEVO:
		if (modo.equalsIgnoreCase("nuevo")) {
			accion = "insertar";
		//MODO EDITAR / CONSULTAR:
		} else {
			claseSufijo="boxconsulta";
			lecturaSufijo = true;
			//MODO CONSULTAR O VER
			if (modo.equalsIgnoreCase("ver")) {
				clase = "boxConsulta";
				lectura = true;
			}

			accion = "modificar";
			FacSufijoBean sufijoBean = (FacSufijoBean)request.getAttribute("SUFIJO");
			if (sufijo != null) {
				sufijo = sufijoBean.getSufijo();
				concepto = sufijoBean.getConcepto();
				varios = sufijoBean.getVarios();
				//Tratamiento del CheckBox:
				if (varios.equals(ClsConstants.DB_TRUE))
					miForm.setVarios(varios);
			}
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
	<html:javascript formName="sufijosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
		}	

		<!-- Asociada al boton Reset -->
		function accionRestablecer(){ 
			sufijosForm.reset();
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			
			sub();
			if (validateSufijosForm(document.sufijosForm)) {
				while(sufijosForm.sufijo.value.length <3) {
					sufijosForm.sufijo.value = "0"+sufijosForm.sufijo.value;
				}
				sufijosForm.modo.value = "<%=accion%>";
				sufijosForm.submit();
			}else{
			
				fin();
				return false;
			}
		}		
	</script>	

</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma key="menu.facturacion.mantenimientoSufijos"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_Sufijos.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<table class="tablaCentralCamposPeque" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.sufijos.leyenda">	
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.sufijos.literal.sufijo"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<html:text name="sufijosForm" property="sufijo" size="3" maxlength="3" styleClass="<%=claseSufijo%>" readonly="<%=lecturaSufijo%>" value="<%=sufijo%>"/>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.descripcion"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<html:text name="sufijosForm" property="concepto" size="50" maxlength="100" styleClass="<%=clase%>" readonly="<%=lectura%>" value="<%=concepto%>"/>
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.sufijos.literal.varios"/>
								</td>
								<td class="labelText">
									<html:checkbox name="sufijosForm" property="varios" value="<%=ClsConstants.DB_TRUE%>" disabled="<%=lectura%>" />
								</td>
							</tr>
					</table>					
				</siga:ConjCampos>	
				</td>
			</tr>
		</html:form>
		</table>
	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES ACCION -->
	<siga:ConjBotonesAccion botones='C,Y,R' modo='<%=modo%>' modal="P" />
	<!-- FIN: BOTONES ACCION -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
</div>



			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>