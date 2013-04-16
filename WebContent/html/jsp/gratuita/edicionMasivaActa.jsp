<!-- edicionMasivaActa.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.atos.utils.UsrBean"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	String seleccionados = (String)request.getParameter("seleccionados");
	
%>

<html:html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	</script>
</head>

<body>
	<html:form action="/JGR_ActasComision.do" method="POST" target="submitArea">
		<input type="hidden" name="modo" value="updateMasivo">
		<input type="hidden" name="seleccionados" 	value="<%=seleccionados%>">
		<input type="hidden" name="idInstitucion" 	value="">
		<input type="hidden" name="idActa" 			value="">
		<input type="hidden" name="anioActa" 		value="">
	
		<siga:ConjCampos leyenda="general.criterios">	
			<table class="tablaCampos" border="0" align="left">
				<tr>
					<td style="vertical-align:middle" width="20px">
						<html:checkbox property="guardaActa" />
					</td>
					<td class="labelText" style="vertical-align:middle" width="110px">
						<siga:Idioma key="sjcs.actas.numeroActa" />/<siga:Idioma key="sjcs.actas.anio" /> -<br> <siga:Idioma key="sjcs.actas.fechaResolucion" />
					</td>
					<td style="vertical-align:middle" width="90px">
						<siga:ComboBD nombre="idActaComp"  tipo="cmbActaComision" clase="boxCombo" ancho="160" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" />
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle" width="20px">
						<html:checkbox property="guardaPonente"/>
					</td>
					<td class="labelText" style="vertical-align:middle" width="110px">
						<siga:Idioma key="gratuita.operarRatificacion.literal.ponente"/>
					</td>
					<td style="vertical-align:middle" width="90px">
						<siga:ComboBD nombre="idPonente"  tipo="tipoPonente" clase="boxCombo" ancho="500" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>"/>
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle" width="20px">
						<html:checkbox property="guardaRatificacion"/>
					</td>
					<td class="labelText" style="vertical-align:middle" width="110px">
					  <siga:Idioma key="gratuita.operarRatificacion.literal.tipoRatificacion"/>
					</td>
					<td style="vertical-align:middle" width="90px">
						<siga:ComboBD nombre="idTipoRatificacionEJG" tipo="tipoResolucion" clase="boxCombo" ancho="500" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>"  accion="Hijo:idFundamentoJuridico"/>
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle" width="20px">
						<html:checkbox property="guardaFundamento" />
					</td>
					<td class="labelText" style="vertical-align:middle" width="110px">
						<siga:Idioma key="gratuita.operarRatificacion.literal.fundamentoJuridico"/>
					</td>
					<td style="vertical-align:middle" width="90px">
						<siga:ComboBD nombre="idFundamentoJuridico" tipo="tipoFundamentos" clase="boxCombo" ancho="500" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=dato%>" hijo="t"/>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
		<siga:ConjBotonesAccion botones="Y,C"/>	
	</html:form>
	
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	<script language="JavaScript">
		function accionCerrar(){
			window.top.close();
		}

		function accionGuardarCerrar(){
			sub();
			if (document.ActaComisionForm.guardaActa.checked && jQuery("#idActaComp option:selected").html().length>10 &&
				( (!document.ActaComisionForm.guardaRatificacion.checked || jQuery("#idTipoRatificacionEJG option:selected").val()==0)
				|| (!document.ActaComisionForm.guardaFundamento.checked || jQuery("#idFundamentoJuridico option:selected").val()==0))){
				alert("<siga:Idioma key='sjcs.actas.checkRatificacionResolucion'/>");
				fin();
			}else if (document.ActaComisionForm.guardaRatificacion.checked && !document.ActaComisionForm.guardaFundamento.checked) {
				alert("<siga:Idioma key='sjcs.actas.checkresolucion'/>");
				fin();
			} else {
			
				if (document.ActaComisionForm.guardaFundamento.checked && !document.ActaComisionForm.guardaRatificacion.checked) {
					alert("<siga:Idioma key='sjcs.actas.checkfundamentojuridico'/>");
					fin();
				} else {
					if(document.ActaComisionForm.guardaActa.checked ||
						document.ActaComisionForm.guardaPonente.checked ||
						document.ActaComisionForm.guardaRatificacion.checked ||
						document.ActaComisionForm.guardaFundamento.checked){
		
						if(document.getElementById("idActaComp").value!=""){
							var actaComp= document.getElementById("idActaComp").value.split(',');
							document.ActaComisionForm.idInstitucion.value=actaComp[0];
							document.ActaComisionForm.anioActa.value=actaComp[1];
							document.ActaComisionForm.idActa.value=actaComp[2];
						}
						document.ActaComisionForm.submit();
					}else{
						alert("<siga:Idioma key='sjcs.actas.seleccioneCampos'/>");
						fin();
					}
				}
			}
		}
	</script>
</body>
</html:html>