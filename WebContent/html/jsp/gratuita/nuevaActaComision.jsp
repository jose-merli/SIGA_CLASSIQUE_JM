<!DOCTYPE html>
<html>
<head>
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
	String 	dato[] = {(String)usr.getLocation(),"-1"};
%>



	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/js/validation.js"></script>	
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>
	
	<siga:Titulo titulo="sjcs.actas.titulo" localizacion="sjcs.actas.localizacion"  />
</head>

<body>

	<html:form action="/JGR_ActasComisionEd" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idInstitucion" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "pendientes" value = ""/>
		<html:hidden property = "sufijoNumActa"/>
		

		<siga:ConjCampos leyenda="general.criterios">	
			<table class="tablaCampos" border="0" align="left">
			<tr>
				<td  width="19%"></td>
				<td  width="20%"></td>
				<td  width="8%"></td>
				<td  width="10%"></td>
				<td  width="14%"></td>
				<td  width="15%"></td>
				<td  width="13%"></td>
				<td  width="1%"></td>
				
			</tr>
			
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.anio" />/<siga:Idioma key="sjcs.actas.numeroActa" /> (*)</td>
				<td>
					<html:text name="ActaComisionForm" property="anioActa" style="width:40px" maxlength="4" styleClass="box" onkeypress="return soloDigitos(event)"></html:text>
					/
					<html:text name="ActaComisionForm" property="numActa" style="width:40px" maxlength="7" styleClass="box"></html:text>
					
					
				</td>
				<c:choose>
					<c:when test="${not empty sufijos}">
						<td>
							<select id="sufijo" onchange="onchangeSufijo();" style="width:60px;" class="boxCombo">
								<c:forEach items="${sufijos}" var="sufijo" varStatus="status">
									<option value="${sufijo}" ><c:out value="${sufijo}"/> </option>
								</c:forEach>
							</select>
						</td>
					</c:when>
					<c:otherwise>
						<td></td>
					
					</c:otherwise>
				</c:choose>
				
				
				<td colspan="2" class="labelText"><siga:Idioma key="sjcs.actas.fechaResolucion" /></td>
				<td >
					<siga:Fecha nombreCampo="fechaResolucion" valorInicial="${ActaComisionForm.fechaResolucion}"/> 
					
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.fechaReunion" /></td>
				<td>
					<siga:Fecha nombreCampo="fechaReunion" valorInicial="${ActaComisionForm.fechaReunion}"/> 
					
				</td>
				<td class="labelText" colspan ="2"><siga:Idioma key="sjcs.actas.horaInicio" /></td>
				<td><html:text name="ActaComisionForm" property="horaIni" maxlength="2" styleClass="box" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuIni" maxlength="2" styleClass="box" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
				<td class="labelText"><siga:Idioma key="sjcs.actas.horaFin" /></td>
				<td><html:text name="ActaComisionForm" property="horaFin" maxlength="2" styleClass="box" style="text-align:right; width:25px" onkeypress="return soloDigitos(event)"></html:text> : <html:text name="ActaComisionForm" property="minuFin" maxlength="2" styleClass="box" style="text-align:left; width:25px"  onkeypress="return soloDigitos(event)"></html:text></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.presidente"/></td>
				<td colspan="6"><siga:ComboBD nombre="idPresidente"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="500"/></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.secretario"/></td>
				<td colspan="6"><siga:ComboBD nombre="idSecretario"  tipo="tipoPonente" parametro="<%=dato%>" clase="boxCombo"  filasMostrar="1" seleccionMultiple="false" obligatorio="false" ancho="500"/></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.miembrosComision"/></td>
				<td colspan="6"><html:textarea styleClass="labelTextArea" property="miembros"  rows="3" cols="80"/></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td class="labelText"><siga:Idioma key="sjcs.actas.observaciones"/></td>
				<td colspan="6"><html:textarea styleClass="labelTextArea" property="observaciones" rows="3" cols="80"/></td>
				<td>&nbsp;</td>
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
		function onchangeSufijo() {
			
			var sufijo = document.getElementById('sufijo');
			var anioActa = document.getElementById('anioActa');
			
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
		           type: "POST",
		           url: "/SIGA/JGR_ActasComision.do?modo=geJQuerytNumActaComision",
		           data: "sufijo="+sufijo.value+"&anioActa="+anioActa.value,
		           dataType: "json",
		           success:  function(json) {
		       			var numActa = json.numActa;
		       			document.getElementById('numActa').value = numActa;
		       			//Si tiene opciones el valor es el que iene que estar seleccioando
		           },
		           error: function(xml,msg){
		        	   alert("Error: "+msg);
		           }
		        });
			
				
		}
		
		function accionGuardarCerrar(){
			sub();
			var errores = "";
			var error = false;
			
			var sufijo = document.getElementById('sufijo');
			if(sufijo)
				document.ActaComisionForm.sufijoNumActa.value = sufijo.value;
			
			if(document.ActaComisionForm.numActa.value=="" ||	document.ActaComisionForm.anioActa.value==""){
				error = true;
				errores += "<siga:Idioma key='errors.required' arg0='sjcs.actas.numeroActa'/>"+ '\n';
			
			}else{
					var numeroActa = document.ActaComisionForm.numActa.value;
					if(isNumero(numeroActa)==false){
						error = true;
						errores += "<siga:Idioma key='errors.integer' arg0='sjcs.actas.numeroActa'/>"+ '\n';
					}
				
			}
			
			if(document.ActaComisionForm.horaIni.value!=""){
				if(document.ActaComisionForm.horaIni.value>23){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaInicioError01'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.minuIni.value==""){
						document.ActaComisionForm.minuIni.value=0;
					}
				}			
			}
			if(document.ActaComisionForm.minuIni.value!=""){
				if(document.ActaComisionForm.minuIni.value>59){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaInicioError02'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.horaIni.value==""){
						document.ActaComisionForm.horaIni.value=0;
					}
				}	
			}
			
			if(document.ActaComisionForm.horaFin.value!=""){
				if(document.ActaComisionForm.horaFin.value>23){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaFinError01'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.minuFin.value==""){
						document.ActaComisionForm.minuFin.value=0;
					}
				}			
			}
			if(document.ActaComisionForm.minuFin.value!=""){
				if(document.ActaComisionForm.minuFin.value>59){
					error = true;
					errores += "<siga:Idioma key='sjcs.actas.horaFinError02'/>"+ '\n';
				} else {
					if(document.ActaComisionForm.horaFin.value==""){
						document.ActaComisionForm.horaFin.value=0;
					}
				}	
			}					
			if(error){
				alert(errores);
				fin();
			}else{
				document.ActaComisionForm.modo.value="insertar";
				document.ActaComisionForm.submit();
	
			}
		}
	
	</script>
</body>
</html>