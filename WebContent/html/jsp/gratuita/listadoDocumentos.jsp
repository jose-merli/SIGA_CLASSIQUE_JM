<!-- listadoDocumentos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>


<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String accion = (String)ses.getAttribute("accionModo");
	ses.removeAttribute("accion");
	
	
	FilaExtElement[] elems = new FilaExtElement[3];
	
	String botones="";
	
	if (accion == null || accion.equalsIgnoreCase("ver")){
		botones = "C";
		elems[0]=new FilaExtElement("consultar", "consultarDocumento", SIGAConstants.ACCESS_READ);
	}
	else {
		botones = "C,E,B";
		elems[0]=new FilaExtElement("consultar", "consultarDocumento", SIGAConstants.ACCESS_READ);
		elems[1]=new FilaExtElement("editar", "editarDocumento", SIGAConstants.ACCESS_READ);	
		elems[2]=new FilaExtElement("borrar", "borrarDocumento", SIGAConstants.ACCESS_READ);	
	}
	if (obj != null && obj.size()>0){
		Hashtable miHash = (Hashtable) obj.get(0);
		String tipodocu=(String)miHash.get("IDTIPODOCUMENTOEJG");
		ses.setAttribute("idTipoDoc",tipodocu);
	}
	

	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<title><siga:Idioma key="gratuita.retenciones.listadoRetenciones"/></title>

	<script type="text/javascript">
		function refrescarLocal()
		{
			parent.buscar();
		}
		function consultarDocumento(fila) {
		   var datos;
		   datos = document.getElementById('tablaDatosDinamicosD');
		   datos.value = ""; 
		   var i, j;
		   for (i = 0; i < 2; i++) {
		      var tabla;
		      tabla = document.getElementById('listadoMaterias');
		      if (i == 0) {
		        var flag = true;
		        j = 1;
		        while (flag) {
		          var aux = 'oculto' + fila + '_' + j;
		          var oculto = document.getElementById(aux);
		          if (oculto == null)  { flag = false; }
		          else { datos.value = datos.value + oculto.value + ','; }
		          j++;
		        }
		        datos.value = datos.value + "%"
		      } else { j = 2; }
		      if ((tabla.rows[fila].cells)[i].innerHTML == "")
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      else
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
		   }
		   document.forms[0].modo.value = "verDocu";
		   ventaModalGeneral(document.forms[0].name,"P");
		 }

		 function editarDocumento(fila) {
		   var datos;
		   datos = document.getElementById('tablaDatosDinamicosD');
		   datos.value = ""; 
		   var i, j;
		   for (i = 0; i < 2; i++) {
		      var tabla;
		      tabla = document.getElementById('listadoMaterias');
		      if (i == 0) {
		        var flag = true;
		        j = 1;
		        while (flag) {
		          var aux = 'oculto' + fila + '_' + j;
		          var oculto = document.getElementById(aux);
		          if (oculto == null)  { flag = false; }
		          else { datos.value = datos.value + oculto.value + ','; }
		          j++;
		        }
		        datos.value = datos.value + "%"
		      } else { j = 2; }
		      if ((tabla.rows[fila].cells)[i].innerHTML == "") 
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      else
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
		   }
		   document.forms[0].modo.value = "editarDocu";
		   var resultado = ventaModalGeneral(document.forms[0].name,"P");
		   if (resultado) {
		  	 	if (resultado[0]) {
		   		refrescarLocalArray(resultado);
		   	} else 
		   	if (resultado=="MODIFICADO")
		   	{
		      		refrescarLocal();
		   	}
		   }
		 }
		
		 function borrarDocumento(fila) {
		   var datos;
		   if (confirm('¿Está seguro de que desea eliminar el registro?')){
		   	datos = document.getElementById('tablaDatosDinamicosD');
		       datos.value = ""; 
		   	var i, j;
		   	for (i = 0; i < 2; i++) {
		      		var tabla;
		      		tabla = document.getElementById('listadoMaterias');
		      		if (i == 0) {
		        		var flag = true;
		        		j = 1;
		        		while (flag) {
		          			var aux = 'oculto' + fila + '_' + j;
		          			var oculto = document.getElementById(aux);
		          			if (oculto == null)  { flag = false; }
		          			else { datos.value = datos.value + oculto.value + ','; }
		          			j++;
		        		}
		        		datos.value = datos.value + "%"
		      		} else { j = 2; }
		      		if ((tabla.rows[fila].cells)[i].innerHTML == "")
		        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      		else
		        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
		   	}
		   	var auxTarget = document.forms[0].target;
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "BorrarDocu";
		   	document.forms[0].submit();
		   	document.forms[0].target=auxTarget;
		 	}
		 }
	</script>
</head>

<body>
	<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
	</html:form>
		
	<% if (obj != null && obj.size()>0) { %>
	
		<siga:Table
		   name="listadoMaterias"
		   border="2"
		   columnNames="censo.fichaCliente.literal.abreviatura,gratuita.maestros.documentacionEJG.nombre,"
		   columnSizes="25,65,10"
		   modal="M">
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				Hashtable fila = (Hashtable)obj.get(recordNumber-1);
			%>				
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" elementos="<%=elems%>" pintarEspacio="no" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDTIPODOCUMENTOEJG")%>">
					    <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.get("IDDOCUMENTOEJG")%>">
					    <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.get("IDINSTITUCION")%>">
					    <%=fila.get("ABREVIATURA")%>
					 </td>
					<td><%=fila.get("DESCRIPCION")%></td>
				</siga:FilaConIconos>		
			<% recordNumber++;		   
			} %>
		</siga:Table>

	<%
	}else {
	%>
	<siga:Table
		   name="listadoMaterias"
		   border="2"
		   columnNames="censo.fichaCliente.literal.abreviatura,gratuita.maestros.documentacionEJG.nombre,"
		   columnSizes="25,65,10"
		   modal="P">
		   <tr class="notFound">
	   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
  </siga:Table>
	<%
	}
	%>
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>