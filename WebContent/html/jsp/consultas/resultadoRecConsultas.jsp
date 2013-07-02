<!-- resultadoRecConsultas.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 09-02-2005 Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.ConConsultaBean"%>
<%@ page import="com.siga.beans.ConConsultaAdm"%>
<%@ page import="com.siga.beans.ConModuloBean"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	
	String idInstitucion = userBean.getLocation();	
	Vector vDatos = (Vector)request.getAttribute("datos");
	String todos = (String)request.getAttribute("todos");
	String botones = "";
%>	

<html>

<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	</head>

	<body class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" target="mainWorkArea" style="display:none">			
		    <html:hidden styleId = "modo"  property = "modo"  value = ""/>
			<html:hidden styleId = "hiddenFrame"  property = "hiddenFrame" value = "1"/>			
			<html:hidden styleId = "idInstitucion" property = "idInstitucion"/>
			<html:hidden styleId = "idConsulta" property = "idConsulta"/>
			<html:hidden styleId = "tipoConsulta"  property = "tipoConsulta"/>
			<html:hidden styleId = "tipoEnvio"  property = "tipoEnvio"/>
			<html:hidden styleId = "accionAnterior"  property = "accionAnterior"/>
			<html:hidden styleId = "idModulo"  property = "idModulo"/>
			<input type="hidden" id="actionModal" name="actionModal" value="">
		</html:form>	
  
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="consultas.recuperarconsulta.literal.modulo,
		   		  consultas.recuperarconsulta.literal.descripcion,"
		   		  columnSizes="15,72,13"
		   		  fixedHeight="95%"
		   		  modalScroll = "true">
	   		     		    		  
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
			<% if (vDatos==null || vDatos.size()==0) { %>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
			<% } else {
			 		for (int i=0; i<vDatos.size(); i++) {
				  		Row fila = (Row)vDatos.elementAt(i);
				  		FilaExtElement[] elementos=new FilaExtElement[1];
				  		if (todos!=null){
							if (fila.getString("PERMISOEJECUCION").equalsIgnoreCase("S")){
	  			 				elementos[0]=new FilaExtElement("ejecutar","ejecutar",SIGAConstants.ACCESS_READ);
	  			 			}
	  			 		}else{
	  			 			elementos[0]=new FilaExtElement("ejecutar","ejecutar",SIGAConstants.ACCESS_READ);
	  			 		}
	  			 		
	  			 		if (fila.getString(ConConsultaBean.C_IDINSTITUCION).equals(idInstitucion)){	
				  			botones="C,E,B";
				  		} else{
				  			botones="C,E";
				  		} %>
	  			<siga:FilaConIconos 
	  				fila='<%=""+(i+1)%>' 
	  				botones="<%=botones%>" 
	  				elementos='<%=elementos%>' 
	  				clase="listaNonEdit">
										
					<td><input type="hidden" id="oculto<%=""+(i+1)%>_1" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString(ConConsultaBean.C_IDINSTITUCION)%>" />
						<input type="hidden" id="oculto<%=""+(i+1)%>_2" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString(ConConsultaBean.C_IDCONSULTA)%>" />
						<input type="hidden" id="oculto<%=""+(i+1)%>_3" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString(ConConsultaBean.C_TIPOCONSULTA)%>" />	
						<input type="hidden" id="oculto<%=""+(i+1)%>_4" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString(ConConsultaBean.C_ESEXPERTA)%>" /><%=fila.getString(ConModuloBean.C_NOMBRE)%></td>
					<td><%=fila.getString(ConConsultaBean.C_DESCRIPCION)%></td>
				</siga:FilaConIconos>
			<% 
					}
				}
			%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:Table>
			
		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">		
		function refrescarLocal() {			
			parent.buscar() ;			
		}
		
		function seleccionarFila(fila) {
		    var idinstitucion = 'oculto' + fila + '_' + 1;		    
		    var idconsulta = 'oculto' + fila + '_' + 2;
		    var tipoconsulta = 'oculto' + fila + '_' + 3;
		
			//Datos del elemento seleccionado:
			document.forms[0].idConsulta.value = document.getElementById(idconsulta).value;
			document.forms[0].idInstitucion.value = document.getElementById(idinstitucion).value;
			document.forms[0].tipoConsulta.value = document.getElementById(tipoconsulta).value;			
		}

		<!-- Funcion asociada al boton Ejecutar -->
		function ejecutar(fila) {
			//Datos del elemento seleccionado:
			seleccionarFila(fila);		
			
			//Submito
			if (document.forms[0].tipoConsulta.value=="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"){
				document.forms[0].modo.value = "tipoEnvio";
				var resultado = ventaModalGeneral(document.forms[0].name,"P");
				if (resultado!=undefined && resultado!="VACIO" && resultado!=""){				
					document.forms[0].tipoEnvio.value=resultado;
					document.forms[0].modo.value = "ejecutarConsulta";
					var ejecucion = ventaModalGeneral(document.forms[0].name,"G");
				}
			} else {
				document.forms[0].modo.value = "criteriosDinamicos";
				var valores = ventaModalGeneral(document.forms[0].name,"G");
			}
		}
	</script>
	
		
	</body>
</html>