<!-- busquedaActasComision.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.atos.utils.UsrBean"%>

<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<%
	HttpSession ses = request.getSession();
	String app = request.getContextPath();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	String 	dato[] = {(String)usr.getLocation()};
%>
<html>

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
	<link rel="stylesheet" href="<html:rewrite page="/html/js/themes/base/jquery.ui.all.css"/>" />
		
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" type="text/javascript" ></script>
	<script src="<html:rewrite page="/html/js/jquery.custom.js"/>" type="text/javascript" ></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>


<body onLoad="ajusteAlto('resultado');">


	<html:form action="/JGR_ActasComision?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscar"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property="datosPaginador" />
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
					<siga:ConjCampos leyenda="general.criterios">	
						<table class="tablaCampos" border="0" align="left">
						<tr>
							<td class="labelText"><siga:Idioma key="sjcs.actas.anio" />/<siga:Idioma key="sjcs.actas.numeroActa" /></td>
							<td class="labelText">
								<html:text name="ActaComisionForm" property="anioActa" size="4" maxlength="4" styleClass="box"  onkeypress="return soloDigitos(event)"></html:text>
								&nbsp;/&nbsp;
								<html:text name="ActaComisionForm" property="numeroActa" size="8" maxlength="8" styleClass="boxNumber"></html:text>
							</td>
							<td class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /></td>
							<td>
								<siga:Fecha nombreCampo="fechaResolucion" /> 
							</td>
							<td class="labelText"><siga:Idioma key="sjcs.actas.fechaReunion" /></td>
							<td>
								<siga:Fecha nombreCampo="fechaReunion" /> 
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
							<td class="labelText" colspan="5"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="500"/></td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
							<td class="labelText" colspan="5"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="500"/></td>
						</tr>
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
	
	</html:form>
		
	<siga:ConjBotonesBusqueda botones="B,N" />

	<script language="JavaScript">
		function refrescarLocal(){
			buscar();
		}
		
		function buscar(){
			sub();		
			document.ActaComisionForm.modo.value = "buscar";
			document.ActaComisionForm.submit();
		}
		
		function buscarPor(){
			sub();		
			document.ActaComisionForm.modo.value = "buscarPor";
			document.ActaComisionForm.submit();
		}
		
		function nuevo(){		
			document.ActaComisionForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.ActaComisionForm.name,"G");
			if(resultado=='MODIFICADO'){
				buscar();
			}

		}
	</script>
	
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

			
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

</body>
</html>