<!-- nuevaActaComision.jsp -->

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
		
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/jquery.custom.js"/>" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validation.js" type="text/javascript"></script>	
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>

<body>

	<html:form action="/JGR_ActasComision" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idInstitucion" value = ""/>
		<html:hidden property = "actionModal" value = ""/>

		<siga:ConjCampos leyenda="general.criterios">	
			<table class="tablaCampos" border="0" align="left">
			<tr>
				<td class="labelText" width="18%"><siga:Idioma key="sjcs.actas.numeroActa" />/<siga:Idioma key="sjcs.actas.anio" /> (*)</td>
				<td>
					<html:text name="ActaComisionForm" property="numeroActa" size="4" maxlength="8" styleClass="box"></html:text>
					&nbsp;/&nbsp;
					<html:text name="ActaComisionForm" property="anioActa" size="2" maxlength="4" styleClass="box" onkeypress="return soloDigitos(event)"></html:text>
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /> (*)</td>
				<td>
					<siga:Fecha nombreCampo="fechaResolucion" valorInicial="${ActaComisionForm.fechaResolucion}"/> 
					
				</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaReunion" /></td>
				<td>
					<siga:Fecha nombreCampo="fechaReunion" valorInicial="${ActaComisionForm.fechaReunion}"/> 
					
				</td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaInicio" /></td>
				<td><html:text name="ActaComisionForm" property="horaIni" maxlength="2" styleClass="box" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuIni" maxlength="2" styleClass="box" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaFin" /></td>
				<td><html:text name="ActaComisionForm" property="horaFin" maxlength="2" styleClass="box" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuFin" maxlength="2" styleClass="box" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
				<td colspan="5"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="400"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
				<td colspan="5"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="400"/></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.miembrosComision"/></td>
				<td colspan="5"><html:textarea styleClass="labelTextArea" property="miembros" style="width:500; height:100"></html:textarea></td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.observaciones"/></td>
				<td colspan="5"><html:textarea styleClass="labelTextArea" property="observaciones" style="width:500; height:100"></html:textarea></td>
			</tr>
			</table>
		</siga:ConjCampos>	

	</html:form>
	
	<siga:ConjBotonesAccion botones="Y,C" modal="G"/>		

			
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

	<script language="JavaScript">
		function accionCerrar(){
			window.top.close();
		}
		
		function accionGuardarCerrar(){
			sub();
			var errores = "";
			var error = false;
			if(document.ActaComisionForm.numeroActa.value=="" || 
			   document.ActaComisionForm.anioActa.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.numeroActa'/>"+ '\n';
			}if (document.ActaComisionForm.fechaResolucion.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.fechaResolucion'/>"+ '\n';
			}if (document.ActaComisionForm.fechaResolucion.value!=""&&document.ActaComisionForm.fechaReunion.value!=""&&document.ActaComisionForm.fechaReunion.value>document.ActaComisionForm.fechaResolucion.value){
				error = true;
				errores += "<siga:Idioma key='sjcs.actas.fechasErroneas'/>"+ '\n';
			}
			if(error==false){
				document.ActaComisionForm.modo.value="insertar";
				document.ActaComisionForm.submit();
			}else{
				alert(errores);
				fin();
			}
		}
	</script>
</body>
</html>