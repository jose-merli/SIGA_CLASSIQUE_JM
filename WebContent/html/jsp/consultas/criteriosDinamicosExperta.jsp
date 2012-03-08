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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

	Vector criterios = (Vector)request.getAttribute("criterios");
	Vector valores = (Vector)request.getAttribute("valores");
	Vector valoresDefecto = (Vector)request.getAttribute("valoresDefecto");
	Vector valoresNulo = (Vector)request.getAttribute("valoresNulo");
	Vector operaciones = (Vector)request.getAttribute("operaciones");
	Vector tipo = (Vector)request.getAttribute("tipo");
	
	Vector valias = (Vector)request.getAttribute("alias");
	Vector ayuda = (Vector)request.getAttribute("ayuda");
	
	
%>


<html>
	<head>
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	    
	    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	    
	   	<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		
	</head>

	<body onload="comprobar();">
	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="submitArea">			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>		
			<html:hidden property = "actionModal" value = ""/>

	<div id="camposRegistro" style="position:absolute; width:964px; height:450px; left:16px; top:20px; overflow-y:auto">

	<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.criteriosdinamicos">
	<table  class="tablaCampos"  align="center">
	<%if (criterios!=null){
		for (int i=0; i<criterios.size(); i++){
			String campo = (String)criterios.get(i);
			String tipocampo = (String)tipo.get(i);
			String alias = (String)valias.get(i);
			String selectAyuda = (String)ayuda.get(i);
			String valorDefecto = (String)valoresDefecto.get(i);
			Boolean valorNulo = (Boolean)valoresNulo.get(i);
			
			alias=alias.replaceAll("\"",ClsConstants.CONSTANTESUSTITUIRCOMILLAS);
			selectAyuda=selectAyuda.replaceAll("\"",ClsConstants.CONSTANTESUSTITUIRCOMILLAS);
			
			
			String tp=tipocampo;
			
			
			boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE))?true:false;
			boolean numerico = (tipocampo!=null && (tipocampo.equals(SIGAConstants.TYPE_NUMERIC)|| tipocampo.equals(SIGAConstants.TYPE_LONG)))?true:false;
			if (tipocampo!=null && !tipocampo.equals("")){
			
				if (tipocampo.equals(SIGAConstants.TYPE_NUMERIC) || tipocampo.equals(SIGAConstants.TYPE_LONG)||tipocampo.equals(SIGAConstants.TYPE_MULTIVALOR) ){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valornumerico");
				}else if (tipocampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
				}else if (tipocampo.equals(SIGAConstants.TYPE_DATE)){
					tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valorfecha");
				}else{
					tipocampo = "";
				}
			}else{
				tipocampo = UtilidadesString.getMensajeIdioma(user,"consultas.recuperarconsulta.literal.valoralfanumerico");
			}
			
			int max = 10;
			String longitud="";
			if (tipocampo.equals("N")){
			  longitud="30";
			}else if (tipocampo.equals("A")){
			  longitud="255";
			}else if (tipocampo.equals("D")){
			  longitud="7";
			}else{
			  longitud="50";
			}
			
			if (longitud!=null && !longitud.equals("")){
				// +1 para dejar que introduzcan signo
				max = Integer.valueOf(longitud).intValue()+1;		
			}
			String escala = "";// En principio no ponemos escala
			if (escala!=null && !escala.equals("")){
				if (!escala.equals("0")){
					// +2 para dejar que introduzcan signo y punto decimal
					max = Integer.valueOf(longitud).intValue()+Integer.valueOf(escala).intValue()+2;
				}
			}
			
%>		
		<tr>
			<td class="labelText">
				<input type="hidden" name="criteriosDinamicos[<%=i%>].idC" value="<%=campo%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].tc" value="<%=tp%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].lg" value="<%=longitud%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].dc" value="<%=escala%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].st" value="<%=alias%>"></input>
				<input type="hidden" name="criteriosDinamicos[<%=i%>].hp" value="<%=selectAyuda%>"></input>
				
				<%=campo%>
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
			
			<% if(tipocampo.equals("")){%>
			<td></td>
			<%}else{%>
				<td>
				
				<% if (valores.get(i)!=null){%>
				<select id="valor<%=i%>" nulo="<%=valorNulo.booleanValue()%>" name="criteriosDinamicos[<%=i%>].val" class = "boxCombo">
				
				
					<% if(valorNulo.booleanValue())%>
					<option value="" ><siga:Idioma key="general.combo.seleccionar" /></option>
					
				<%
					Vector v = (Vector)valores.elementAt(i);
						for (int k=0; k<v.size(); k++){
							Row fila2 = (Row)v.elementAt(k);
							String id2 = fila2.getString("ID");
							String desc2 = fila2.getString("DESCRIPCION");
							if(id2.equals(valorDefecto)){%>
							<option value="<%=id2%>" selected><%=desc2%></option>
							<%}else{
						%>
							<option value="<%=id2%>" ><%=desc2%></option>
					<%	}}%>					
				</select>	
			<%}else{%>				
				<%if (fecha){%>
					<input type="text" id="valor<%=i%>" nulo="<%=valorNulo%>"  name="criteriosDinamicos[<%=i%>].val" class="box" value="<%=valorDefecto%>" readonly="true"></input>
					<a href='javascript://'onClick="return showCalendarGeneral(valor<%=i%>);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}else if (numerico){%>
					<input type="text" id="valor<%=i%>" nulo="<%=valorNulo%>" name="criteriosDinamicos[<%=i%>].val" class="box" value="<%=valorDefecto%>" maxlength="<%=max%>"></input>
				<%}else{%>
					<input type="text" id="valor<%=i%>" nulo="<%=valorNulo%>" name="criteriosDinamicos[<%=i%>].val" value="<%=valorDefecto%>" class="box" maxlength="<%=max-1%>"></input>	
				<%}%>
			<%}
			}%>
			</td>		
		</tr>
<%}
}%>			
	</table>
	</siga:ConjCampos>	
	</div>
	
	
		<table id="tablaBotonesDetalle" class="botonesDetalle" align="center">
			<tr>
				<td style="width: 100%;">&nbsp;</td>
				<td class="tdBotones">
					<input type="button" alt='<siga:Idioma key="global.boton.aceptar"/>' name='idButton' id="idButton" onclick="return accionAceptar();" class="button" value='<siga:Idioma key="global.boton.aceptar"/>'>
				</td>
				<td class="tdBotones">
					<input type="button" alt='<siga:Idioma key="general.boton.close"/>' name='idButton' id="idButton" onclick="return accionCerrar();" class="button" value='<siga:Idioma key="general.boton.close"/>'>
				</td>
			</tr>
		</table>

	
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
			
			
			inputs = document.getElementsByTagName("input");
			error = "";
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="text"){
					if(input.nulo=='false' && input.value==""){
						id = input.id.split("valor");
						var index = id[1];
						//si es nulo permitimos vacio
						operacion = document.getElementById("operacion"+index+"").value;
						
						if(operacion!="20"&&operacion!="21"&&operacion!="22"&&operacion!="23"){
						
							nombre = document.getElementById("criteriosDinamicos["+index+"].idC").value;
						
							error += "<siga:Idioma key='errors.required' arg0='"+nombre+"'/>"+ '\n';
						}
						
					}
					
				}
				
			}
			selects = document.getElementsByTagName("select");
			for(var i = 0 ; i <selects.length ; i++) {
				select = selects[i];
				if(select.nulo=='false' && select.value==""){
					id = select.id.split("valor");
					var index = id[1]; 
					operacion = document.getElementById("operacion"+index+"").value;
					if(operacion!="20"&&operacion!="21"&&operacion!="22"&&operacion!="23"){
						nombre = document.getElementById("criteriosDinamicos["+index+"].idC").value;
						error += "<siga:Idioma key='errors.required' arg0='"+nombre+"'/>"+ '\n';
					}
				}
			}
			if(error!=""){
				alert(error);	
				return false;
			}
			
			
			document.forms[0].modo.value = "abrirConParametros";	
			document.forms[0].target = "submitArea";	
			document.forms[0].submit();
			
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("VACIO");
		}		
		
        function sustituirComillas(cadena) 
		{		
			cadena=cadena.replace("\"","#@#");
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