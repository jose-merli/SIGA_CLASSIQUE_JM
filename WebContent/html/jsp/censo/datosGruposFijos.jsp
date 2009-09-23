<!-- datosProcurador.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	    prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@page import="com.siga.censo.form.MantenimientoGruposFijosForm"%>

<!-- JSP -->
<% 
		String app=request.getContextPath(); 
		
		String estilo="box";
		boolean desactivado = true;
		String accion = "";
		String modo = (String)request.getAttribute("modo");	
		String parametro[] = new String[1];

	// Formulario
	MantenimientoGruposFijosForm formulario = (MantenimientoGruposFijosForm) request.getAttribute("MantenimientoGruposFijosForm");
		if (modo.equalsIgnoreCase("EDITAR")) {
			desactivado  = false;
			estilo = "box";
			accion = "modificar";
			
		} else {
				if (modo.equalsIgnoreCase("NUEVO")) {
						desactivado = false;
						accion = "insertar";
			
				} else { //MODO=VER
						desactivado  = true;
						estilo = "boxConsulta";
						accion = "ver";
			
				}
		}
%>

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoGruposFijosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.close();
		}	

		<!-- Asociada al boton Reset -->
		function accionRestablecer(){ 
			MantenimientoGruposFijosForm.reset();
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub();
			if (validateMantenimientoGruposFijosForm(document.MantenimientoGruposFijosForm)){
				MantenimientoGruposFijosForm.modo.value = "<%=accion%>";
				MantenimientoGruposFijosForm.submit();
			}else{
				fin();
				return false;
			}
		}		
	</script>	
</head>

<body class="tablaCentralCampos">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma key="censo.datosGruposFijos.literal.GruposFijos"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/CEN_MantenimientoGruposFijos.do" method="POST" target="submitArea">
		<html:hidden property = "modo" />
		<html:hidden name="MantenimientoGruposFijosForm" property="idGrupo" />
		
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="censo.datosGruposFijos.literal.GruposFijos">
						<table class="tablaCampos" border="0" width="100%">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>&nbsp;(*)
								</td>
								<td>
									<html:text name="MantenimientoGruposFijosForm" property="nombre" size="80" maxlength="100" readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
					</table>
				</siga:ConjCampos>	
				</td>
			</tr>
		</html:form>
		</table>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones="C,Y,R" modo="<%=accion%>" clase="botonesDetalle" modal="M" />



<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>