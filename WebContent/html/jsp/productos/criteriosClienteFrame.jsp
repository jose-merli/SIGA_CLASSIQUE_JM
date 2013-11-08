<!DOCTYPE html>
<html>
<head>
<!-- criteriosClienteFrame.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<%
	HttpSession ses=request.getSession();
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		

	String tipocampo = (String)request.getAttribute("tipoCampo");
	boolean fecha = (tipocampo!=null && tipocampo.equals(SIGAConstants.TYPE_DATE));

	Vector datosValor = (Vector)request.getAttribute("datosValor");
	Vector datosOperador = (Vector)request.getAttribute("datosOperador");

	String modo = (String)request.getSession().getAttribute("modo");
	request.getSession().removeAttribute("modo");
	if (modo==null)
		modo="modificar";
%>


	<!-- HEAD -->
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	
		<script language="JavaScript">
			function cambiar(){		
				var oper_aux= operador.value.split(",");			
				
				if ((oper_aux[0]==<%=ClsConstants.ESVACIO_ALFANUMERICO%>) || (oper_aux[0]==<%=ClsConstants.ESVACIO_NUMERICO%>) || (oper_aux[0]==<%=ClsConstants.ESVACIO_FECHA%>)) {
						if (document.getElementById('caja')){
							document.getElementById('comb').style.visibility='visible';
							document.getElementById('caja').style.visibility='hidden';
							document.getElementById('caja').style.width='0px';
							pasarvalor();
						}
	
						if (document.getElementById('cmbvalor') ){				
							document.getElementById('comb').style.visibility='visible';
							document.getElementById('cmbvalor').style.visibility='hidden';
							document.getElementById('cmbvalor').style.width='0px';
							pasarvalorcombo();					
						}
							
						if (document.getElementById('valor') ){						 								
						 	document.getElementById('comb').style.visibility='visible';
					 	 	if (document.getElementById('caja')){
						 	 	document.getElementById('caja').style.visibility='hidden';
						 	 	document.getElementById('caja').style.width='0px';
					 	 	}
					 	 	if (document.getElementById('caja-datepicker-trigger')){ 		
					 	 		jQuery("#caja-datepicker-trigger").hide();
					 	 	}		
					 	 	pasarvalor();	
					   }	
	
				} else {		
					if (document.getElementById('caja')){
						document.getElementById('valor').value="";
						document.getElementById('comb').style.visibility='hidden';
						document.getElementById('caja').style.visibility='visible';
						if (document.getElementById('caja-datepicker-trigger')){ 	
							jQuery("#caja-datepicker-trigger").show();
						}
						document.getElementById('caja').style.width='120px';
					}
					
					if (document.getElementById('cmbvalor') ){					
						quitarvalorcombo();
						document.getElementById('comb').style.visibility='hidden';
						document.getElementById('cmbvalor').style.visibility='visible';
						document.getElementById('cmbvalor').style.width='220px';
					}			
				}
			}
		
			function ocultar(){
				if (document.getElementById('comb')){
					document.getElementById('comb').value=1;
					document.getElementById('comb').style.visibility='hidden';
				}
			}
			
			function pasarvalor(){	
				document.getElementById('valor').value=document.getElementById('valor1').value
			}
		
			function pasarvalorcombo(){			
			 	var tiene_0=false;
			 	var tiene_1=false;
			 	var valor = document.getElementById('valor');
			 	for (i=0;i<valor.options.length;i++){
			 		if (valor[i].value==0){
			 			tiene_0=true;
			 			break;
			 		}
			 	}
			 	
			 	for (i=0;i<valor.options.length;i++){
			 		if (valor[i].value==1){
			 			tiene_1=true;
			 			break;
			 		}	
			 	}
			 	
			 	if (!tiene_0){
			 		valor.options[valor.options.length]=new Option("0","0");
			 	}
			 	
				if (!tiene_1){
					valor.options[valor.options.length]=new Option("1","1");
				}
				valor.value=valor1.value;			
			}
		
			function quitarvalorcombo(){			
			 	var tiene_0=false;
			 	var tiene_1=false;
				var posicion_0=0;
				var posicion_1=0;
				var valor = document.getElementById('valor');
			 	for (i=0;i<valor.options.length;i++){
			 		if (valor[i].innerHTML==0){
			 			tiene_0=true;
						posicion_0=i;
			 			break;					
			 		}
			 	}
			 	
				if (tiene_0){				
			 		valor.options[posicion_0]=null;				
			 	}
				
			 	for (i=0;i<valor.options.length;i++){
			 		if (valor[i].innerHTML==1){
			 			tiene_1=true;
						posicion_1=i;
						break;
			 		}	
			 	}
			 	
				if (tiene_1){
					valor.options[posicion_1]=null;
				}			
			}		
		</script>
	</head>

<body class="BodyIframe" onload="ocultar();">
	<table align="center" cellspacing="0" border="0" width="100%">
		<tr>
			<td class="labelText" >
				<siga:Idioma key="pys.mantenimientoServicios.literal.operador"/>
			</td>
			<td>
				<%if (!modo.equalsIgnoreCase("consulta")){%>
					<select name = "operador" id="operador"  class = "boxCombo" onchange="cambiar();">
						<% if (datosOperador!=null){
								for (int i=0; i<datosOperador.size(); i++){
									ConOperacionConsultaBean fila = (ConOperacionConsultaBean)datosOperador.get(i);
									String id = (String.valueOf(((Integer)fila.getIdOperacion()).intValue())+","+((String)fila.getSimbolo()));
									String desc = (String)fila.getDescripcion();							
						%>
							<option value="<%=id%>"><%=desc%></option>
							<% } // FOR %>
						<% } // IF %>
					</select>
								
				<% } else { %>
					<html:text property="operador" styleClass="boxConsulta" size="10" value="" readOnly="true" />
				<%}%>
			</td>
			
			<td class="labelText" width="25%">
				<siga:Idioma key="pys.mantenimientoServicios.literal.valor"/>
			</td>
			<td>
				<%if (!modo.equalsIgnoreCase("consulta")){%>
					<% if ((datosValor!=null)&&(datosValor.size()>0)){ %>				
						<select name = "valor" id="cmbvalor" style="width:220px" class = "boxCombo">
							<%
								for (int i=0; i<datosValor.size(); i++){
									Hashtable fila = (Hashtable)datosValor.get(i);
									String id2 = (String)fila.get("ID");
									String desc2 = (String)fila.get("DESCRIPCION");
							%>
								<option value="<%=id2%>"><%=desc2%></option>
							<% } %>					
						</select>
						
						<select name = "valor1" id="comb" class = "boxCombo" onchange="pasarvalorcombo();">
							<option value="1">SI</option>
							<option value="0">NO</option>
						</select>
							
					<% } else { %>						
						<% if (fecha){ %>	
							<siga:Fecha styleId="caja" nombreCampo="valor" anchoTextField="16" readOnly="true"/>
							<select name = "valor1" id="comb"  class = "boxCombo"  onchange="pasarvalor();">
								<option value="SI">SI</option>
								<option value="NO">NO</option>
							</select>
							
						<% } else { %>					
							<input type="text" name="valor" id="caja" class="box" width="300px"></input>
							<select name = "valor1" id="comb"  class = "boxCombo"  onchange="pasarvalor();">
								<option value="1">SI</option>
								<option value="0">NO</option>
							</select>									
						<%}%>
					<%}%>
					
				<%}else{%>
					<html:text property="valor" style="width:600px" styleClass="boxConsulta" value="" readOnly="true" />
				<%}%>
			</td>
		</tr>
	</table>
</body>
</html>