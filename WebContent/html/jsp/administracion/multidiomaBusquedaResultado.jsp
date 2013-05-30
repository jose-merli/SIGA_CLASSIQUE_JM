
<!-- multidiomaBusquedaResultados.jsp -->

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
<%@ page import = "com.siga.beans.GenRecursosBean"%>

<!-- JSP -->
<% 	
	String app = request.getContextPath(); 
	HttpSession ses = request.getSession();
    UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	Vector vDatos    = (Vector)request.getAttribute("resultados");
	boolean bCatalogos = UtilidadesString.stringToBoolean((String)request.getAttribute("CATALOGOS_MAESTROS"));
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<siga:Titulo titulo="administracion.multidioma.etiquetas.titulo" localizacion="administracion.multidioma.etiquetas.localizacion"/>
		
		<script>
			
			function accionGuardar() 
			{
				var datos = "";
				var ele = document.getElementsByName("checkParametrosGenerales");
				
				for (i = 0; i < ele.length; i++) {
					if (ele[i].checked) {
					
						if (document.getElementById("valor_" + ele[i].value).value.length < 1) {
							alert ("<siga:Idioma key="administracion.multidioma.etiquetas.error.valorParametro"/>: \n'" + document.getElementById("oculto" + ele[i].value + "_3").value + "...'");
							return;
						}
					
						if (datos.length > 0) datos = datos + "#;;#";
						datos = datos + document.getElementById("oculto" + ele[i].value + "_2").value + "#;#" +		// idLenguaje
								        document.getElementById("oculto" + ele[i].value + "_1").value + "#=#" + 	// idRecurso
								        document.getElementById("valor_" + ele[i].value).value;
					}
				}

				if (datos.length < 1) {
					alert ("<siga:Idioma key="administracion.multidioma.etiquetas.alert.seleccionarElementos"/>");
					return;
				}
				multidiomasForm.datosModificados.value = datos;
				multidiomasForm.modo.value = "modificar";
				multidiomasForm.submit();
			}	
			
			function modificaParametro(o, bMostrarAlert)
			{
				valor = document.getElementById("valor_" + o.value);
				
				if (o.checked) {
					jQuery("#valor_" + o.value).removeAttr("disabled");
					
				}
				else {
					var mensaje = "<siga:Idioma key="administracion.multidioma.etiquetas.alert.restaurarValor"/>";
					if(!bMostrarAlert || confirm(mensaje)) {						
						valor.value = document.getElementById("valorOriginal_" + o.value).value;
						jQuery("#valor_" + o.value).attr("disabled","disabled");
					}
					else {
						o.checked = true;
					}
				}
			}
			
			function marcarDesmarcarTodos(o) 
			{

				var ele = document.getElementsByName("checkParametrosGenerales");
				
				for (i = 0; i < ele.length; i++) {
					ele[i].checked = o.checked;
					modificaParametro(ele[i], (i==0?true:false));
				}
			}

			function refrescarLocal() 
			{
				parent.buscar();
			}
			
		</script>
		
	</head>
	
	<body>	
	<% String sFormName = "/ADM_GestionarMultiidiomaCatalogos.do";
				if (bCatalogos) { // Catalogos 
				sFormName = "/ADM_GestionarMultiidiomaCatalogos.do";
			 } else { // Etiquetas 
				sFormName = "/ADM_GestionarMultiidiomaEtiquetas.do";
			 } %>
	<html:form action="<%=sFormName%>" method="POST" target="submitArea">
		
			<input type="hidden" name="datosModificados" value=""/>
			<input type="hidden" name="esCatalogo" value="<%=bCatalogos%>"/>
		
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value=""/>
			<input type="hidden" name="modo" value=""/>
		</html:form>	
		
		<siga:TablaCabecerasFijas 
  				nombre="cabecera"
  				borde="2"
  				estilo=""
	   			clase="tableTitle"
  				nombreCol="<input type='checkbox' name='checkParamGenerales' onclick='marcarDesmarcarTodos(this);'/>,
  				           administracion.multidioma.etiquetas.literal.descripcionInstitucion,
  				           administracion.multidioma.etiquetas.literal.descripcionIdiomaSeleccionado" 
   				tamanoCol="5,45,50"
	   			ajusteBotonera="true" >

		   	<%	if ( (vDatos != null) && (vDatos.size() > 0) ) {

					String botones = "C,E";
					
					String nombreImagen = app + "/html/imagenes/botonAyuda.gif"; 
	
					for (int i = 1; i <= vDatos.size(); i++) {
					
						Hashtable recurso = (Hashtable) vDatos.get(i-1);
						String idRecurso           = (String) recurso.get(GenRecursosBean.C_IDRECURSO);
						String descripcion         = (String) recurso.get(GenRecursosBean.C_DESCRIPCION);
						String descripcionTraducir = (String) recurso.get("DESCRIPCION_TRADUCIR");
//						String idioma              = (String) recurso.get("IDIOMA");
						String idiomaTraducir      = (String) recurso.get("IDIOMA_TRADUCIR");
						String ayuda           	   = (String) recurso.get("AYUDA");
			 %>   		
			 
				<tr class="listaNonEdit" >
					<td align="center">
						<input type=checkbox id="checkParametrosGenerales" name="checkParametrosGenerales" value="<%=i%>" onclick="modificaParametro(this, true)"/>
						
						<input type="hidden" value="<%=idRecurso%>"  id="oculto<%=i%>_1" />
						<input type="hidden" value="<%=idiomaTraducir%>" id="oculto<%=i%>_2" />
						<input type="hidden" value="<%=descripcion.substring(0, (descripcion.length()<50?descripcion.length():50))%>" id="oculto<%=i%>_3" />
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(descripcion)%>
					</td>
					<td align="center">
						<siga:ToolTip id='<%=(""+i)%>' imagen='<%=nombreImagen%>' texto='<%=ayuda%>' />
						<input type="hidden" value="<%=UtilidadesString.mostrarDatoJSP(descripcionTraducir)%>" id="valorOriginal_<%=i%>" />
						<input type="text"   value="<%=UtilidadesString.mostrarDatoJSP(descripcionTraducir)%>" id="valor_<%=i%>" disabled size="70"/>
					</td>
				</tr>
				
				<% }  // for
			} // if
			else { %>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
			<% } %>
		   			
			
		</siga:TablaCabecerasFijas>		

		<siga:ConjBotonesAccion botones="G"  clase="botonesDetalle"/>	

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
</html>