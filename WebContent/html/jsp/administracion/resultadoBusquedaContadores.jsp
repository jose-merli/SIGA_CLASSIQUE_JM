<!-- resultadoBusquedaContadores.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import = "com.siga.beans.AdmContadorBean"%>


<!-- JSP -->
<% 	
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	Vector vDatos = (Vector)request.getAttribute("resultados");
	
%>

<html>
	<head>
		<link id="default" rel="STYLESHEET" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
		<siga:Titulo titulo="administracion.parametrosGenerales.contadores"  localizacion="menu.parametrosGenerales.localizacion"/>
		
		<script language="JavaScript">
	
			

			function refrescarLocal() {
			
				parent.buscar();
			}

		</script>
		
	</head>
	
	<body>
		<html:form action="/ADM_Contadores.do" method="POST" target="submitArea">
			<input type="hidden" name="modo" value="modificar">
			<input type="hidden" name="datosModificados" value="">
			

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			
		</html:form>	
		
		<siga:TablaCabecerasFijas 
  				nombre="cabecera"
  				borde="2"
  				estilo=""
	   			clase="tableTitle"
  				nombreCol="administracion.parametrosGenerales.literal.codigo,administracion.parametrosGenerales.literal.nombre,administracion.parametrosGenerales.literal.descripcion,administracion.parametrosGenerales.literal.prefijo,administracion.parametrosGenerales.literal.contadorActual,administracion.parametrosGenerales.literal.sufijo,"  
   				tamanoCol="15,25,27,5,15,5,8"
	   			ajusteBotonera="true"
				modal="G" 
		   		activarFilaSel="true" >
		   			
		   	<%	if ( (vDatos != null) && (vDatos.size() > 0) ) {
                   
					for (int i = 1; i <= vDatos.size(); i++) {
					 String descripcion=" ";
					 String prefijo=" ";
   					 String sufijo=" ";
						AdmContadorBean b = (AdmContadorBean)vDatos.get(i-1);
						
						String botones = "C,E";
						/*if (b.getModificableContador()!=null && b.getModificableContador().equals("1")){
						 botones+=",E";
						}*/
						if (b.getDescripcion()!=null && !b.getDescripcion().equals("") ){
						   descripcion=b.getDescripcion();
						}
						if (b.getPrefijo()!=null && !b.getPrefijo().equals("")){
						   prefijo=b.getPrefijo();
						}
						if (b.getSufijo()!=null && !b.getSufijo().equals("")){
						   sufijo=b.getSufijo();
						}
						
			 %>   		
			 
				<siga:FilaConIconos fila='<%=(""+i)%>' botones="<%=botones%>" visibleBorrado="no" pintarEspacio="no" clase="listaNonEdit" >
					<td>
					     <input type="hidden" name="oculto<%=i%>_1" value="<%=b.getIdContador() %>">
					     
						<%=UtilidadesString.mostrarDatoJSP(b.getIdContador())%>
					</td>				
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getNombre())%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getDescripcion())%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getPrefijo())%>
					</td>
					<td>
						<%=b.getContador()%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.getSufijo())%>
					</td>
					
				</siga:FilaConIconos>
				
				<% }  // for
			} // if
			else { %>
				<br><br>
		   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 		<br><br>	 		
				
			<% } %>
			
		</siga:TablaCabecerasFijas>		
  
		<siga:ConjBotonesAccion botones=""  clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
	
</html>