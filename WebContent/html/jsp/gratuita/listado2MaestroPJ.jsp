<!-- listado2MaestroPJ.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP --> 
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String profile[]=usr.getProfile();
	
	//Datos propios del jsp:	
	Vector obj = (Vector) request.getAttribute("resultado");
	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");	
	String cambiar = request.getAttribute("cambiar")==null?"":(String)request.getAttribute("cambiar");	
	String accion = request.getAttribute("accion") == null?"":(String)request.getAttribute("accion");
	//String accion = request.getSession().getAttribute("accion") == null?"":(String)request.getSession().getAttribute("accion");
	String idpartido = request.getAttribute("idPartido") == null?"":((String) request.getAttribute("idPartido"));
	String idprovincia = request.getAttribute("idProvincia") == null?"":((String) request.getAttribute("idProvincia"));
	boolean desactivar = false;
	String clase = "box";
	String botones = "";
	if (accion.equalsIgnoreCase("ver")) { desactivar = true; clase="boxConsulta"; }
	
	// CREO EL BOTON ELIMINAR POBLACION
	
	FilaExtElement[] elems = null;
	if(!accion.equalsIgnoreCase("ver")){
	
		elems = new FilaExtElement[1];	
		elems[0]=new FilaExtElement("borrar","eliminarPoblacion",SIGAConstants.ACCESS_FULL);
	}

%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<script language="JavaScript">
		//Refresco del iframe 	
	 	function refrescarLocal()
		{
			parent.refrescarLocal();
		}
		//Guardo los campos seleccionados
		function seleccionarFila(fila){
		    var idPartido = 'oculto' + fila + '_' + 1;
		    var idPoblacion = 'oculto' + fila + '_' + 2;

		
			//Datos del elemento seleccionado:
			document.forms[0].idPartido.value = document.getElementById(idPartido).value;
			document.forms[0].idPoblacion.value = document.getElementById(idPoblacion).value;

		}

		//Funcion asociada al botón eliminar población -->
		function eliminarPoblacion(fila) 
		{		
			//Datos del elemento seleccionado:
			seleccionarFila(fila);			
			
			//Submito
			document.forms[0].modo.value = "eliminarPoblacion";
			document.forms[0].target="submitArea";
			document.forms[0].submit();

		}				
	</script>
 	
</head>

<body>

	<html:form action="/JGR_MantenimientoMaestroPJ.do" method="post" target="mainWorkArea" style="display:none">
		<html:hidden property = "modo" value = "<%=modo%>"/>
		<html:hidden property = "cambiar" value = "modal"/>
		<html:hidden property = "accion" value = "<%=accion%>"/>
		<html:hidden property = "idPartido" />
		<html:hidden property = "idPoblacion" />
	</html:form>	
		
	<!-- INICIO: RESULTADO -->

	<siga:TablaCabecerasFijas
			   nombre="consultarPJ"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="gratuita.consultarPJ.literal.poblacionesPartido,"
			   tamanoCol="90,10"
		   			alto="100%"
			   modal="P"
	>
	<%if ((obj != null) && (obj.size()>0)) { %>

			<%
					int recordNumber=1;
					while ((recordNumber) <= obj.size())
					{	 Hashtable miHash = (Hashtable)obj.get(recordNumber-1);
			%>
			<!-- Campos ocultos por cada fila:      
					1- POBLACION
					2- IDPOBLACION
					3- IDPROVINCIA
					4- IDPARTIDO
			-->
			<!-- Campos visibles por cada fila:
					1- PARTIDOJUDICIAL
					2- POBLACION
			-->
	       	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no" visibleBorrado="no" pintarEspacio="false" modo="<%=accion%>" >

				<td><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idpartido%>'/>
				<input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=miHash.get("IDPOBLACION")%>'/>
				<%=miHash.get("POBLACION")%></td>
			</siga:FilaConIconos>
			<% 			recordNumber++; %>
			<%  	} %>
	<% } else { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>	
	<% } %>	
		</siga:TablaCabecerasFijas>
	<!-- FIN: RESULTADO -->


	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
</body>	
</html>