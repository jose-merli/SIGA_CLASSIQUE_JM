<!-- listarDefendidosDesignas.jsp -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas -->
	 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getAttribute("resultado");
	String modo = (String) ses.getAttribute("Modo");
	String tipo = (String) request.getAttribute("tipo");
	String nombreCol="";
	String tamano="";
	 if (tipo.equals("asistencia")){/*si es asistencia solo se muestra dos campos en la tabla*/
		    nombreCol="fcs.criteriosFacturacion.asistencia.tipoAsistencia,fcs.criteriosFacturacion.asistencia.importe";
		    tamano="60,40";
		
     }else{/* si es actuacion, la tabla presenta 3 campos*/
		    nombreCol="fcs.criteriosFacturacion.asistencia.tipoAsistencia,fcs.criteriosFacturacion.asistencia.tipoActuacion,fcs.criteriosFacturacion.asistencia.importe";
		    tamano="40,40,20";
	 }
%>	

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="gratuita.defendidosDesigna.literal.titulo" 
		localizacion="gratuita.defendidosDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.close();
		}	
	
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_DefinirHitosFacturables.do" method="POST" target="mainPestanas" style="display:none">
		<html:hidden property = "modo" value = ""/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
          <table class="tablaTitulo" cellspacing="0" heigth="32">
          <tr>
	        <td id="titulo" class="titulosPeq">
		     <% if (tipo.equals("asistencia")) { %>
			   <siga:Idioma key="fcs.criteriosFacturacion.asistencia.tituloAsistencia"/>
		     <% } else { %>
			   <siga:Idioma key="fcs.criteriosFacturacion.asistencia.tituloActuacion"/>
		     <% } %>
			</td>
    	</tr>
  		</table>
			
			
		
    

		<% if (obj==null || obj.size()==0){%>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	 		
		<%}else{%>
		  <!-- Campo obligatorio -->
		 
			<siga:TablaCabecerasFijas 
			   nombre="tablaDatos"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="<%=nombreCol%>"
			   tamanoCol="<%=tamano%>"
		   			alto="100%"
		   			ajusteBotonera="true"		

			   modal="P"
			  >
			  
		    <% if (tipo.equals("asistencia")){
			  
		    	int recordNumber=1;
				while ((recordNumber) <= obj.size()){	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			 	%>	
				  	<tr>
						<td class="labelTextValue" >&nbsp;&nbsp;&nbsp;<%=hash.get("TIPOASISTENCIA")%></td>
						<td class="labelTextValue" >&nbsp;&nbsp;&nbsp;<%=hash.get("IMPORTE")%></td>
					</tr>
				<%recordNumber++;%>
				<%}
			}else{
			  int recordNumber=1;
				while ((recordNumber) <= obj.size()){	 
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
			 	%>	
				  	<tr>
						<td class="labelTextValue" >&nbsp;&nbsp;&nbsp;<%=hash.get("TIPOASISTENCIA")%></td>
						<td class="labelTextValue" >&nbsp;&nbsp;&nbsp;<%=hash.get("TIPOACTUACION")%></td>
						<td class="labelTextValue" >&nbsp;&nbsp;&nbsp;<%=hash.get("IMPORTE")%></td>
					</tr>
				<%recordNumber++;%>
				<%}
			}%>	
			
			</siga:TablaCabecerasFijas>
		<%}%>

			
		      <siga:ConjBotonesAccion botones="C" modal="P" clase="botonesDetalle"/>


<!-- FIN: LISTA DE VALORES -->
		
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
		  
		
