<!DOCTYPE html>
<html>
<head>
<!-- criteriosDinamicos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.beans.ConCampoConsultaBean"%>
<%@ page import="com.siga.beans.ConCriteriosDinamicosBean"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

	Vector criterios = (Vector)request.getAttribute("criterios");
	Vector valores = (Vector)request.getAttribute("valores");
	Vector operaciones = (Vector)request.getAttribute("operaciones");
	
	
%>



	
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	</head>

	<body onload="comprobar();ajusteAlto('resultado');">
	
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="consultas.recuperarconsulta.literal.criteriosdinamicos"/>
			</td>
		</tr>
	</table>	
	
	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="submitArea">			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>		
			<html:hidden property = "actionModal" value = ""/>


	<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.criteriosdinamicos">
	<table  class="tablaCampos"  align="center">
	<%if (criterios!=null){
		for (int i=0; i<criterios.size(); i++){
			Row fila = (Row)criterios.elementAt(i);
			String tipocampo = fila.getString(ConCampoConsultaBean.C_TIPOCAMPO);
			boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE))?true:false;
			boolean numerico = (tipocampo!=null && (tipocampo.equals(SIGAConstants.TYPE_NUMERIC)|| tipocampo.equals(SIGAConstants.TYPE_LONG)))?true:false;
			if (tipocampo!=null && !tipocampo.equals("")){
				if (tipocampo.equals(SIGAConstants.TYPE_NUMERIC) || tipocampo.equals(SIGAConstants.TYPE_LONG)){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valornumerico");
				}else if (tipocampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
				}else if (tipocampo.equals(SIGAConstants.TYPE_DATE)){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valorfecha");
				}
			}else{
				tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
			}
			
			int max = 10;
			String longitud = fila.getString(ConCampoConsultaBean.C_LONGITUD);
			if (longitud!=null && !longitud.equals("")){
				// +1 para dejar que introduzcan signo
				max = Integer.valueOf(longitud).intValue()+1;		
			}
			String escala = fila.getString(ConCampoConsultaBean.C_ESCALA);
			if (escala!=null && !escala.equals("")){
				if (!escala.equals("0")){
					// +2 para dejar que introduzcan signo y punto decimal
					max = Integer.valueOf(longitud).intValue()+Integer.valueOf(escala).intValue()+2;
				}
			}
			
%>		
		<tr>
			<td class="labelText">
				<input type="hidden" name="criteriosDinamicos[<%=i%>].idC" value="<%=fila.getString(ConCriteriosDinamicosBean.C_IDCAMPO)%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].tc" value="<%=fila.getString(ConCampoConsultaBean.C_TIPOCAMPO)%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].lg" value="<%=fila.getString(ConCampoConsultaBean.C_LONGITUD)%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].dc" value="<%=fila.getString(ConCampoConsultaBean.C_ESCALA)%>"></input>
				<%=UtilidadesString.mostrarDatoJSP(fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA))%>
			</td>
			<td>			
				<select id="operacion<%=i%>"  name="criteriosDinamicos[<%=i%>].op" class = "boxCombo">
					<% 	Vector o = (Vector)operaciones.elementAt(i);
							for (int j=0; j<o.size(); j++){
							Row fila1 = (Row)o.elementAt(j);
							String id = fila1.getString("ID");
							String desc = fila1.getString("DESCRIPCION");				
						%>
						<option value="<%=id%>"><%=desc%></option>
					<%	}%>
				</select>			
			</td>
			<td class="labelText">
				<%=tipocampo%>
			</td>		
			<td>
			<% if (valores.get(i)!=null){%>
				<select id="valor<%=i%>" name="criteriosDinamicos[<%=i%>].val" class = "boxCombo">
					<% Vector v = (Vector)valores.elementAt(i);
						for (int k=0; k<v.size(); k++){
							Row fila2 = (Row)v.elementAt(k);
							String id2 = fila2.getString("ID");
							String desc2 = fila2.getString("DESCRIPCION");				
						%>
						<option value="<%=id2%>"><%=desc2%></option>
					<%	}%>					
				</select>	
			<%}else{%>			
				<%if (fecha){
					String styleId = "valor" + i;
					String nombreCampo = "criteriosDinamicos[" + i + "].val";
				%>				
					<siga:Fecha nombreCampo="<%=nombreCampo%>" styleId="<%=styleId%>"></siga:Fecha>				
				<%}else if (numerico){%>
					<input type="text" id="valor<%=i%>" name="criteriosDinamicos[<%=i%>].val" class="box" maxlength="<%=max%>"></input>
				<%}else{%>
					<input type="text" id="valor<%=i%>" name="criteriosDinamicos[<%=i%>].val" class="box" maxlength="<%=max-1%>"></input>	
				<%}%>
			<%}%>
			</td>		
		</tr>
<%}
}%>			
	</table>
	</siga:ConjCampos>	
	
	
	<div>
		<table id="tablaBotonesDetalle" class="botonesSeguido" align="center">
			<tr>
				<td style="width: 100%;">&nbsp;</td>
				<td class="tdBotones">
					<input type="button" alt='<siga:Idioma key="global.boton.aceptar"/>' name='idButton' id="idButton" onclick="return accionAceptar();" class="button" value='<siga:Idioma key="global.boton.aceptar"/>'>
				</td>
			</tr>
		</table>
	</div>	
	
	<div id='frameResultado'>
		<iframe name="resultado" id="resultado" src="<%=app%>/html/jsp/general/blank.jsp" style="width:100%; border:0" frameborder="0"></iframe>
	</div>
	
	<!-- INICIO: BOTONES REGISTRO -->	
		

	<!-- FIN: BOTONES REGISTRO -->

	<script language="JavaScript">

		function comprobar() 
		{		
			<%if (criterios==null){%>			
				top.cierraConParametros("VACIO");
			<%}%>
		}
		
		<!-- Asociada al boton Aceptar -->
		function accionAceptar() 
		{			
			document.forms[0].target = "resultado";	
			document.forms[0].modo.value = "ejecutarConsulta";
			document.forms[0].submit();
			
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	</html:form>	
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
		
	</body>
</html>