<!-- operarTipo.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Hashtable miHash = (Hashtable) ses.getAttribute("elegido");
	String accion = (String)request.getAttribute("accionModo");	
	ses.removeAttribute("elegido");

	String descri=(String)miHash.get("DESCRIPCION");
	String abrev=(String)miHash.get("ABREVIATURA");
	String tipodocu=(String)miHash.get("IDTIPODOCUMENTOEJG");
	
	ses.setAttribute("idTipoDoc",tipodocu);
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.documentacionEJG.localizacion.mantenimiento" 
		localizacion="gratuita.documentacionEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado'); ">
	
	<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "tipoDocumento" value = "<%=tipodocu%>"/>
	<html:hidden property = "actionModal" value = ""/>

	
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="sjcs.ejg.documentacion.tipoDocumentacion">
			<table align="center" width="100%">
				<tr>				
					<td class="labelText" valign="top">
						<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;(*)
					</td>				
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="30" maxlength="60" styleClass="boxConsulta" value="<%=abrev%>" readonly="true"></html:text>
						<%} else {%>
							<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="30" maxlength="60" styleClass="box" value="<%=abrev%>"></html:text>
						<%}%>
					</td>	
				
					<td class="labelText" valign="top">
						<siga:Idioma key="gratuita.maestros.documentacionEJG.nombre"/>&nbsp;(*)
					</td>
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<textarea name="descripcion" rows="5" cols="60" onkeydown="cuenta(this,200);" class="boxConsulta" readonly="true"><%=descri%></textarea>
						<%} else {%>
							<textarea name="descripcion" rows="5" cols="60" onkeydown="cuenta(this,200)" onChange="cuenta(this,200)" class="box"><%=descri%></textarea>
						<%}%>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="" clase="botonesSeguido" titulo="sjcs.ejg.documentacion.documentacion"/>
		<%} else {%>
			<siga:ConjBotonesAccion botones="G,R" clase="botonesSeguido" titulo="sjcs.ejg.documentacion.documentacion"/>
		<%}%>

	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	   function cuenta(obj, len){ 
	  if(obj.value.length > len-1) obj.value = obj.value.substring(0,len-1);
  }
		//Refresco
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}
	
		function buscar() 
		{		
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.DefinirMantenimDocumentacionEJGForm.reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardar() 
		{
			var nombre = document.forms[0].descripcion.value;
			var contenido = document.forms[0].abreviatura.value;
			sub();
			if (nombre != "") {
				if (contenido!="") {
                    document.forms[0].modo.value="modificar";
					document.forms[0].submit();
				}
				else{ 
					alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoAbreviatura"/>');
					fin();
					return false;
				}
			}
			else {
				alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoDescripcion"/>');
				fin();
				return false;
			}
			

		}			

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

						
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/gratuita/listadoDocumentos.jsp"
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
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"/>
		<%} else {%>
			<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"/>
		<%}%>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		//Asociada al boton Nuevo -->		
		function accionNuevo() 
		{				
			document.forms[0].modo.value = "nuevo2";
			//document.forms[0].accion.value = "materia";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();	
		}

		//Asociada al boton Volver -->
		function accionVolver() 
		{		
			<%
			// indicamos que es boton volver
			ses.setAttribute("esVolver","1");
			%>
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea"; 
			document.forms[0].submit(); 
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
