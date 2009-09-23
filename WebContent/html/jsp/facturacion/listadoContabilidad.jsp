<!-- listadoContabilidad.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri = "struts-bean.tld" prefix="bean"%> 
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.util.Colegio"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<% 	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) request.getAttribute("resultado");
%>
<script>


		function download(fila)
		{
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
  			var aux1 = 'oculto' + fila + '_1';
   			var oculto1 = document.getElementById(aux1);
  			var aux2 = 'oculto' + fila + '_2';
   			var oculto2 = document.getElementById(aux2);
			
			if (oculto2.value<=0) {
				alert('<siga:Idioma key="facturacion.contabilidad.literal.avisoVacio"/>');
			} else {
				document.forms[0].fichero.value = oculto1.value;
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "download";
				document.forms[0].submit();
			}
		}
 
		function ejecutarContab(fila) {
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
  			var aux1 = 'oculto' + fila + '_3';
   			var oculto1 = document.getElementById(aux1);
					
			document.forms[0].idContabilidad.value = oculto1.value;
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value = "ejecutar";
			document.forms[0].submit();
		}
		
		function descargaLog(fila)
		{
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var i, j;
			var tabla;
			tabla = document.getElementById('tablaDatos');
  			var aux1 = 'oculto' + fila + '_1';
   			var oculto1 = document.getElementById(aux1);
  			var aux2 = 'oculto' + fila + '_2';
   			var oculto2 = document.getElementById(aux2);
					
			document.forms[0].fichero.value = oculto1.value;
			document.forms[0].target = "submitArea";
			document.forms[0].modo.value = "download";
			document.forms[0].submit();

		}


</script>
<html>
	<head>
	<title><"listarAsistencias.title"></title>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	</head>
<body class="tablaCentralCampos">
	<% 	
		String nC="";
		String tC="";
		String botones="";
		String alto="340";
	  	nC="gratuita.listadoContabilidad.literal.identificador,gratuita.listadoContabilidad.literal.fcreacion,gratuita.listadoContabilidad.literal.fdesde,gratuita.listadoContabilidad.literal.fhasta,gratuita.listadoContabilidad.literal.nasientos,gratuita.listadoContabilidad.literal.nombreFichero,gratuita.listadoContabilidad.literal.estado,";
		tC="4,10,10,10,10,20,16,10";
		
		FacEstadoContabAdm admEstado = new FacEstadoContabAdm(usr);
	%>
	
	<html:form action="/FAC_Contabilidad.do" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" />
		<input type="hidden" name="fichero" />
		<input type="hidden" name="idContabilidad" />
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>
		
		
		<!-- campos a pasar -->
		<siga:TablaCabecerasFijas 
		   nombre="listarAsistencias"
		   borde="2"
		   clase="tableTitle"
		   nombreCol="<%=nC%>"
		   tamanoCol="<%=tC%>"
		   			alto="100%"
		  >
		<%if (obj != null && obj.size()>0){%>
				<%
				String fecha = "";
		    	int recordNumber=1;
		    	FacRegistroFichContaBean facRegistroFichContaBean  = null;
				while ((recordNumber) <= obj.size())
				{	 
					
					facRegistroFichContaBean = (FacRegistroFichContaBean)obj.get(recordNumber-1);

					// RGG 					
					FilaExtElement[] elems=new FilaExtElement[2];
					if (facRegistroFichContaBean.getEstado().intValue()==FacRegistroFichContaBean.ESTADO_ERROR) {
						elems[0]=new FilaExtElement("ejecutarContab","ejecutarContab", "facturacion.contabilidad.boton.programarEjecucion", SIGAConstants.ACCESS_READ);
						elems[1]=new FilaExtElement("descargaLog","descargaLog",SIGAConstants.ACCESS_READ); 				
					} else 
					if (facRegistroFichContaBean.getEstado().intValue()==FacRegistroFichContaBean.ESTADO_PROGRAMADO) {
						elems[0]=new FilaExtElement("ejecutarContab","ejecutarContab", "facturacion.contabilidad.boton.programarEjecucion", SIGAConstants.ACCESS_READ);
					} else 
					if (facRegistroFichContaBean.getEstado().intValue()==FacRegistroFichContaBean.ESTADO_TERMINADO) {
						elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ); 				
					}
					
					
				%>
					<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones='' visibleConsulta='false' visibleEdicion='false' visibleBorrado='false' elementos='<%=elems%>' pintarEspacio="no" clase="listaNonEdit">
						<td><%=facRegistroFichContaBean.getIdContabilidad().toString()%></td>
						<td><input type="hidden" name="oculto<%=recordNumber%>_1" value="<%=facRegistroFichContaBean.getNombreFichero()%>">
							<input type="hidden" name="oculto<%=recordNumber%>_2" value="<%=facRegistroFichContaBean.getNumeroAsientos()%>">
							<input type="hidden" name="oculto<%=recordNumber%>_3" value="<%=facRegistroFichContaBean.getIdContabilidad()%>">
							<%=GstDate.getFormatedDateShort("",facRegistroFichContaBean.getFechaCreacion())%></td>
						<td><%=GstDate.getFormatedDateShort("",facRegistroFichContaBean.getFechaDesde())%></td>
						<td><%=GstDate.getFormatedDateShort("",facRegistroFichContaBean.getFechaHasta())%></td>
						<td align="right"><%=facRegistroFichContaBean.getNumeroAsientos()%></td>
						<td><%=facRegistroFichContaBean.getNombreFichero()%></td>
						<td><%=admEstado.obtenerDescripcion(facRegistroFichContaBean.getEstado().toString(),usr.getLanguage())%></td>
					</siga:FilaConIconos> 
					<% recordNumber++;
				} %>
		<%}else{%>
		 					<br>
		   		 			<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 					<br>		
		<%}%>
		</siga:TablaCabecerasFijas>

	</body>
</html>
