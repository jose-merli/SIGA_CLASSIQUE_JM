<!-- resultadoBusquedaContadores.jsp -->
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
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<siga:Titulo titulo="administracion.parametrosGenerales.contadores"  localizacion="menu.parametrosGenerales.localizacion"/>
		
		<script language="JavaScript">
	
			

			function refrescarLocal() {
			
				parent.buscar();
			}

		</script>
		
	</head>
	
	<body>
		<html:form action="/ADM_Contadores.do" method="POST" target="submitArea">
			<input type="hidden" id="modo"  name="modo" value="modificar">
			<input type="hidden" id="datosModificados" name="datosModificados"  value="">
		
		</html:form>	
		
		<siga:Table 
  				name="cabecera"
  				border="2"
  				columnNames="administracion.parametrosGenerales.literal.codigo,administracion.parametrosGenerales.literal.nombre,administracion.parametrosGenerales.literal.descripcion,administracion.parametrosGenerales.literal.prefijo,administracion.parametrosGenerales.literal.contadorActual,administracion.parametrosGenerales.literal.sufijo,"  
   				columnSizes="15,25,27,5,15,5,8"
				modal="G">
		   			
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
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
				
			<% } %>
			
		</siga:Table>		
  
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
	
</html>