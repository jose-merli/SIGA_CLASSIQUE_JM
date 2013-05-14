<!-- definirCriteriosCliente.jsp -->

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
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>

<!-- JSP -->
<%
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");		
	String resultadoInicial="", resultado="";
	try{
		resultadoInicial = (String)request.getParameter("resultado");
		resultado = resultadoInicial;
	}catch(Exception e){}
	
	String modo = (String)request.getSession().getAttribute("modoResultado");
	request.getSession().removeAttribute("modoResultado");
	if (modo==null)modo="modificar";
	String visible="no";
	String habilitarCheck="";
	if (modo.equals("consulta")){
		habilitarCheck="disabled";
	}
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	</head>
	
	<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="mainWorkArea" style="display:none">
			<!-- RGG: cambio a formularios ligeros -->		
			<input type="hidden" name="actionModal" value="">
		</html:form>			

		<%
			String tamanosCol="";
			String nombresCol="";
			tamanosCol="8,4,40,15,20,4,10";
			nombresCol+="pys.mantenimientoServicios.literal.conector," +
				"(,"+
				"pys.mantenimientoServicios.literal.campo,"+
				"pys.mantenimientoServicios.literal.operador,"+
				"pys.mantenimientoServicios.literal.valor," +
				"),";
		%>

		<siga:Table
		   	name="tablaDatos"
		   	border="1"
		   	columnNames="<%=nombresCol %>"
		   	columnSizes="<%=tamanosCol %>">

				<!-- INICIO: ZONA DE REGISTROS -->
				<!-- Aqui se iteran los diferentes registros de la lista 
				 Para pintar la tabla se necesita un String, formado por la
				 concatenacion de criterios con el siguiente formato:
				 
				 *conector_campo_operador_valor_idCampo__idOperador_idValor_parentesisAbrir_parentesisCerrar
				 
				 donde los siguientes campos tambien tienen un formato concreto:
				 
				 idCampo: 		idCampo,tipoCampo,idTabla
				 idOperador: 	idOperador,simbolo				
				-->
			
			<%		
				boolean seguir = (resultado.indexOf("*")>0);
				int cont = 0;
				if (!seguir) {
			%>
			
				<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
			
			<%
				}else{
					FilaExtElement[] elems=new FilaExtElement[1];
					if (!modo.equalsIgnoreCase("consulta"))
						elems[0]=new FilaExtElement("borrar","borrar",SIGAConstants.ACCESS_FULL);
					else 
						visible = "si";
					String conector="", campo="", operador="", valor="", idCampo="", idOperador="", idValor="", parentesisAbrir="", parentesisCerrar="";
					
					while (seguir) {
						resultado = resultado.substring(1,resultado.length());
						conector = resultado.substring(1,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						campo = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						operador = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						valor = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						idCampo = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						idOperador = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						idValor = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						parentesisAbrir = resultado.substring(0,resultado.indexOf("_"));
			
						resultado = resultado.substring(resultado.indexOf("_")+1,resultado.length());
						parentesisCerrar = resultado.substring(0,resultado.indexOf("_"));
	
						seguir = (resultado.indexOf("*")>0);
						if (valor.equalsIgnoreCase("")) 
							valor ="&nbsp;";
							
						if (idOperador.split(",")[0].equals(Integer.toString(ClsConstants.ESVACIO_ALFANUMERICO)) || idOperador.split(",")[0].equals(Integer.toString(ClsConstants.ESVACIO_NUMERICO)) ){
							if (valor.equals("0") || valor.equals("NO")||valor.equals("NO COLEGIADO")|| valor.equals("Desconocido")){
								valor="NO";
								
							} else {
								valor="SI";
							}
						}
						
						if (seguir){
							resultado = resultado.substring(resultado.indexOf("*"),resultado.length());
							resultado = "*"+resultado;
						}
						
						String contador =  ""+(cont+1);
				%>
					<!-- REGISTRO  -->
					<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
					 que la lista contiene realmente 3 columnas: Las de datos mas 
					 la de botones de acción sobre los registos  -->
				
	  				<siga:FilaConIconos fila="<%=contador %>" botones=""  elementos='<%=elems%>' pintarEspacio="no"  visibleEdicion="no" visibleConsulta="no" visibleBorrado="<%=visible%>" clase="listaNonEdit">
						<td>
							<input type="hidden" name="oculto<%=contador %>_1" value="<%=idCampo%>">
							<input type="hidden" name="oculto<%=contador %>_2" value="<%=idOperador%>">
							<input type="hidden" name="oculto<%=contador %>_3" value="<%=idValor%>"><%=conector %>
						</td>
						<td>
							<input type="checkbox" value="1" name="chkParentesisAbrir" <%=(parentesisAbrir.equals("1"))?"checked":""%> <%=habilitarCheck%> >
						</td>
						<td><%=campo%></td>
						<td><%=operador%></td>
						<td><%=valor%></td>
						<td>
							<input type="checkbox" value="1" name="chkParentesisCerrar" <%=(parentesisCerrar.equals("1"))?"checked":""%> <%=habilitarCheck%> >
						</td>
					</siga:FilaConIconos>
					
					<%cont++;%>
				<%} // WHILE%>
			<%} // ELSE%>
		</siga:Table>

		<script language="JavaScript">
			function borrar(fila){
				parent.borrarFila(fila);
			}	
		</script>
	</body>
</html>