<!-- busquedaGruposFijosResultados.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@taglib uri = "struts-tiles.tld" 		prefix="tiles"  %>
<%@taglib uri = "struts-bean.tld" 		prefix="bean"	 %>
<%@taglib uri = "struts-html.tld" 		prefix="html"  %>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"	 %>

<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenGruposClienteBean"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.atos.utils.Row"%>

<%	String app = request.getContextPath();
    HttpSession ses=request.getSession();
    UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idioma=userBean.getLanguage().toUpperCase();
	
	String idInstitucion = userBean.getLocation();
	//Vector vGruposFijos = (Vector) request.getAttribute("vGruposFijos");
	/** PAGINADOR ***/
	Vector resultado=null;
	
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	if (ses.getAttribute("DATAPAGINADOR")!=null){

	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  resultado = (Vector)hm.get("datos");
	  Paginador paginador = (Paginador)hm.get("paginador");
	 	paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }
	 else{
	  resultado =new Vector();
	  paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
	 }
	}else{
      resultado =new Vector();
	  paginaSeleccionada = "0";
	 	totalRegistros = "0";
	 	registrosPorPagina = "0";
    }	
	
	String action=app+"/CEN_MantenimientoGruposFijos.do";
    /**************/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><siga:Idioma key="pys.gestionSolicitudes.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<script>
		<!-- Refrescar -->
		function refrescarLocal(){ 		
			parent.buscar();
		}	
	</script>	
	
</head>

<body>
		<html:form action="/CEN_MantenimientoGruposFijos.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
			
			<siga:TablaCabecerasFijas 
			   nombre = "tablaResultados"
			   borde  = "1"
			   clase  = "tableTitle"
			   nombreCol="administracion.auditoria.institucion,
			   			  gratuita.mantenimientoTablasMaestra.literal.nombre,"
			   tamanoCol = "40,50,10"
			   alto  = "100%"
			   modal = "m" 
			   activarFilaSel="true"
			   ajustePaginador="true" >

		<%  if ((resultado != null) && (resultado.size() > 0)){ %>
	
				<%	 for (int i = 1; i <= resultado.size(); i++) { 
							
							// Hashtable hashGrupos = (Hashtable) vGruposFijos.get(i-1);
							Row fila = (Row)resultado.elementAt(i-1);
						    Hashtable hashGrupos = (Hashtable) fila.getRow();
							 if (hashGrupos != null){ 
									String idInstitucionGrupo = (String)hashGrupos.get("IDINSTITUCION");
									String nombreInstitucion = (String)hashGrupos.get("INSTITUCION");
									String idGrupo = (String)hashGrupos.get("IDGRUPO");
									String nombre = (String)hashGrupos.get(CenGruposClienteBean.C_NOMBRE);
									
									String botones=(idInstitucion.equals(idInstitucionGrupo)?"E,C,B":"C");
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones='<%=botones%>' visibleConsulta="false" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->
											<input type="hidden" id="oculto<%=i%>_1" value="<%=idInstitucionGrupo%>">
											<input type="hidden" id="oculto<%=i%>_2" value="<%=idGrupo%>">
											<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
									</td>
									<td>
										<%=UtilidadesString.mostrarDatoJSP(nombre)%>
									</td>
									
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  
			%>
	
	<% } else { %>
		<td colspan="8">
			<br><br>
			<table td align='center'>
					<tr><td class="labelText"><siga:Idioma key="messages.noRecordFound"/></td></tr>
			</table>
			<br><br>
		</td>
	<% } %>
			</siga:TablaCabecerasFijas>

			
			<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	     
		        <siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      <%  }%>

<!-- INICIO: SUBMIT AREA -->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>