<!-- parametrosGeneralesBusquedaResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld"	prefix="siga"%>

<!-- IMPORTS -->
<%@ page import = "java.util.*"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.siga.administracion.*"%>
<%@ page import = "com.siga.beans.GenParametrosBean"%>


<!-- JSP -->
<% 	
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	

	Vector vDatos = (Vector)request.getAttribute("resultados");
	String checkParametrosGenerales = (String) request.getAttribute("checkParametrosGenerales");
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<siga:Titulo titulo="administracion.parametrosGenerales.titulo" localizacion="menu.parametrosGenerales.localizacion"/>
		
		<script language="JavaScript">
			function accionGuardar() 
			{
				var datos = "";
				var ele = document.getElementsByName("checkParametrosGenerales");
				for (i = 0; i < ele.length; i++) {
					if (ele[i].checked) {
					
						if (document.getElementById("valor_" + ele[i].value).value.length < 1) {
							alert ("<siga:Idioma key="administracion.parametrosGenerales.error.valorParametro"/> " + document.getElementById("oculto" + ele[i].value + "_3").value);
							return;
						}
					
						if (datos.length > 0) datos = datos + "#;;#";
						datos = datos + document.getElementById("oculto" + ele[i].value + "_2").value + "#;#" + 	// idInstitucion
							            document.getElementById("oculto" + ele[i].value + "_1").value + "#;#" +		// idModulo
								        document.getElementById("oculto" + ele[i].value + "_3").value + "#=#" + 	// parametro
								        document.getElementById("valor_" + ele[i].value).value;
					}
				}

				if (datos.length < 1) {
					alert ("<siga:Idioma key="administracion.parametrosGenerales.alert.seleccionarElementos"/>");
					return;
				}
				parametrosGeneralesForm.datosModificados.value = datos;
				parametrosGeneralesForm.modo.value = "modificar";
				parametrosGeneralesForm.submit();
			}	
			
			function modificaParametro(o) 
			{
				valor = document.getElementById("valor_" + o.value);
				if (o.checked) {
					valor.disabled = false;
				}
				else {
					var mensaje = "<siga:Idioma key="administracion.parametrosGenerales.alert.restaurarValor"/>";
					if(confirm(mensaje)) {						
						valor.value = document.getElementById("valorOriginal_" + o.value).value;
						valor.disabled = true;
					}
					else {
						o.checked = true;
					}
				}
			}
			

			function refrescarLocal() {
				parent.buscar();
			}

		</script>
		
	</head>
	
	<body>
		<html:form action="/ADM_ParametrosGenerales.do" method="POST" target="submitArea">
			<input type="hidden" name="modo" value="modificar">
			<input type="hidden" name="datosModificados" value="">
			

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
			<input type="hidden" name="checkParametrosGenerales" value="<%=checkParametrosGenerales%>">
		</html:form>	
		
		<siga:Table 
  				name="cabecera"
  				border="2"
  				columnNames="administracion.parametrosGenerales.literal.editar,administracion.parametrosGenerales.literal.modulo,administracion.parametrosGenerales.literal.parametro,administracion.parametrosGenerales.literal.valor," 
   				columnSizes="6,6,45,38,5">
		   			
		   	<%	if ( (vDatos != null) && (vDatos.size() > 0) ) {

					for (int i = 1; i <= vDatos.size(); i++) {
						GenParametrosBean b = (GenParametrosBean)vDatos.get(i-1);
						
						String botones = "B";
						if (b.getIdInstitucion().intValue() == 0){
							botones = "";
						}
						String nombreImagen = app + "/html/imagenes/botonAyuda.gif"; 
			 %>   		
			 
				<siga:FilaConIconos fila='<%=(""+i)%>' botones="<%=botones%>" visibleConsulta="no" visibleEdicion="no" pintarEspacio="no" clase="listaNonEdit" >
					<td align="center">
						<input type=checkbox id="checkParametrosGenerales" value="<%=i%>" onclick="modificaParametro(this)"/>
						
						<input type="hidden" value="<%=UtilidadesString.mostrarDatoJSP(b.getModulo())%>"        id="oculto<%=i%>_1" >
						<input type="hidden" value="<%=UtilidadesString.mostrarDatoJSP(b.getIdInstitucion())%>" id="oculto<%=i%>_2" >
						<input type="hidden" value="<%=UtilidadesString.mostrarDatoJSP(b.getParametro())%>"     id="oculto<%=i%>_3" >
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getModulo())%>
					</td>				
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getParametro())%>
					</td>
					<td>
						<siga:ToolTip id='<%=(""+i)%>' imagen='<%=nombreImagen%>' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (userBean, b.getIdRecurso()))%>' />
						<input type="hidden" value="<%=UtilidadesString.mostrarDatoJSP(b.getValor())%>" id="valorOriginal_<%=i%>" >
						<input type="text"   value="<%=UtilidadesString.mostrarDatoJSP(b.getValor())%>" id="valor_<%=i%>" disabled size="50">
					</td>
				</siga:FilaConIconos>
				
				<% }  // for
			} // if
			else { %>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
				
			<% } %>
			
		</siga:Table>		

		<siga:ConjBotonesAccion botones="G"  clase="botonesDetalle"/>	

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
	
</html>