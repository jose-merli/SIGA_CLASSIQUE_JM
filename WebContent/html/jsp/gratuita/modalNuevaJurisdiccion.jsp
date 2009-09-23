<!-- modalNuevaJurisdiccion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.ScsJurisdiccionBean"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vJurisdicciones = (Vector)request.getAttribute("JURISDICCIONES");
	String institucion = request.getAttribute("IDINSTITUCION").toString();
	
	String area = (String)request.getAttribute("IDAREA").toString();
	
	String materia = (String)request.getAttribute("IDMATERIA").toString();
	
%>	

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<html:javascript formName="DefinirAreasMateriasForm" staticJavascript="false" />
 	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.actuacionesDesigna.literal.jurisdiccion"/>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<html:javascript formName="DefinirAreasMateriasForm" staticJavascript="false" />
	<html:form action="/DefinirAreasMateriasAction.do" method="POST" target="submitArea">
		<html:hidden name="DefinirAreasMateriasForm" property="modo" value="insertarJurisdiccionModal"/>
		<html:hidden name="DefinirAreasMateriasForm" property="idJurisdiccion" />
		<html:hidden name="DefinirAreasMateriasForm" property = "idArea" value = "<%=area%>"/>
	    <html:hidden name="DefinirAreasMateriasForm" property = "idInstitucion" value ="<%=institucion%>"/>
	    <html:hidden name="DefinirAreasMateriasForm" property = "idMateria" value ="<%=materia%>"/>

		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="jurisdiccion">
	</html:form>

	<BR>

	<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="&nbsp;,gratuita.procedimientos.literal.nombre"
		   tamanoCol="20,80" >
			   
			<%	if (vJurisdicciones==null || vJurisdicciones.size() < 1) { %>			
			 		<br><br>
			   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>	 		
			<%	
				} 
				else { 
					for (int i = 0; i < vJurisdicciones.size(); i++) {
			
						Hashtable h = (Hashtable) vJurisdicciones.get(i);
						String nombre          = UtilidadesHash.getString  (h, ScsJurisdiccionBean.C_DESCRIPCION);
						String idJurisdiccion = UtilidadesHash.getString  (h, ScsJurisdiccionBean.C_IDJURISDICCION);
						
			%>

		   <tr class="listaNonEdit">
				<td align="center">
					<input type="checkbox" name="validado" value="1">
					<input type="hidden" name="solicita_<%=(i+1)%>_1" value="<%=idJurisdiccion%>">
				
					
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(nombre) %>
				</td>
		   </tr>

<%		} // del for %>			

<%	} // else del if %>			

	</siga:TablaCabecerasFijas>
	

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			var datos = "";
			var validados = document.getElementsByName("validado");
			
			for (i = 0; i < validados.length; i++){
			 
				if (validados[i].checked == 1){
				  var idJuris = document.getElementById("solicita_" + (i+1) + "_1");
                   if (datos.length > 0){ 
					  datos = datos + "%";
					 
				    }	  
					datos = datos + idJuris.value;
					
					
				}	
			}
			
			document.forms[0].jurisdiccion.value = datos;
			
			if (document.forms[0].jurisdiccion.value!='') {
				document.forms[0].submit();
				window.returnValue="MODIFICADO";			
			} 
			else
				alert('<siga:Idioma key="gratuita.procedimientos.error.seleccionarJurisdiccion"/>');

		}
		
		
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}	

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>