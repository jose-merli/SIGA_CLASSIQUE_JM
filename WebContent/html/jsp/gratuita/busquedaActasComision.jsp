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
	
	//mhg - INC_10639_SIGA
	ArrayList presidenteA = new ArrayList();
   	ArrayList secretarioA = new ArrayList();
	HashMap datosFormulario = new HashMap();
	String anioActa="", numeroActa="", fechaResolucion="", fechaReunion="", idPresidente="", idSecretario="";
	if (request.getSession().getAttribute("DATOSFORMULARIO")!=null) {
		datosFormulario = (HashMap)request.getSession().getAttribute("DATOSFORMULARIO");
		anioActa = datosFormulario.get("ANIOACTA")==null?"":(String)datosFormulario.get("ANIOACTA");
		numeroActa = datosFormulario.get("NUMEROACTA")==null?"":(String)datosFormulario.get("NUMEROACTA");
		fechaResolucion = datosFormulario.get("FECHARESOLUCION")==null?"":(String)datosFormulario.get("FECHARESOLUCION");
		fechaReunion = datosFormulario.get("FECHAREUNION")==null?"":(String)datosFormulario.get("FECHAREUNION");
		idPresidente = datosFormulario.get("IDPRESIDENTE")==null?"":(String)datosFormulario.get("IDPRESIDENTE");
		idSecretario = datosFormulario.get("IDSECRETARIO")==null?"":(String)datosFormulario.get("IDSECRETARIO");
		
		presidenteA.add(idPresidente);
		secretarioA.add(idSecretario);
	}
	String buscar = "0";
	if(request.getAttribute("buscarLista")!= null){
		buscar = (String)request.getAttribute("buscarLista");
	}	
%>
<html>

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>


<body onLoad="ajusteAlto('resultado'); <%if (buscar.equals("1")) {%> buscar() <% } %>">


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
								<html:text name="ActaComisionForm" property="anioActa" size="4" maxlength="4" styleClass="box"  onkeypress="return soloDigitos(event)" value="<%=anioActa%>"></html:text>
								&nbsp;/&nbsp;
								<html:text name="ActaComisionForm" property="numeroActa" size="8" maxlength="8" styleClass="boxNumber" value="<%=numeroActa%>"></html:text>
							</td>
							<td class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /></td>
							<td>
								<siga:Fecha nombreCampo="fechaResolucion" valorInicial="<%=fechaResolucion%>" /> 
							</td>
							<td class="labelText"><siga:Idioma key="sjcs.actas.fechaReunion" /></td>
							<td>
								<siga:Fecha nombreCampo="fechaReunion" valorInicial="<%=fechaReunion%>" />
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
							<td class="labelText" colspan="8"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo" elementoSel="<%=presidenteA%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="800"/></td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
							<td class="labelText" colspan="8"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo" elementoSel="<%=secretarioA%>" filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="800"/></td>
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
			var resultado = ventaModalGeneral(document.ActaComisionForm.name,"M");
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