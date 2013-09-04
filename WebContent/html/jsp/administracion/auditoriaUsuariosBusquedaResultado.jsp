<!DOCTYPE html>
<html>
<head>
<!-- auditoriaUsuariosBusquedaResultado.jsp -->
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
<%@ page import = "com.siga.beans.CenHistoricoBean"%>

<!-- JSP -->
<% 	
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
		
    UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();
	//Vector vDatos = (Vector)request.getAttribute("resultados");
	
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
	
	String action=app+"/ADM_AuditoriaUsuarios.do";
    /**************/
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<siga:Titulo titulo="administracion.auditoriaUsuarios.titulo" localizacion="menu.auditoriaUsuarios.localizacion"/>
		
		
		<script>
			function refrescarLocal() {
				parent.buscar();
			}
		</script>
		
	</head>
	
	<body>	
		<html:form action="/ADM_AuditoriaUsuarios.do" method="POST" target="submitArea">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="modo" value="">
		</html:form>	
		
		<siga:Table 
  				name="cabecera"
  				border="2"
  				columnNames="administracion.auditoriaUsuarios.literal.nombre, administracion.auditoriaUsuarios.literal.tipoAccion, administracion.auditoriaUsuarios.literal.fechaEfectiva, administracion.auditoriaUsuarios.literal.motivo,&nbsp;" 
   				columnSizes="22,30,10,30,8"	   			
		   		modal="M">
		   			
		   	<%	if ( (resultado != null) && (resultado.size() > 0) ) {

					String botones = "C,E";
					for (int i = 1; i <= resultado.size(); i++) {
						//Hashtable h = (Hashtable)vDatos.get(i-1);
						Row fila = (Row)resultado.elementAt(i-1);
						Hashtable h = (Hashtable) fila.getRow();
						
						String fecha = "";
						if (UtilidadesHash.getString(h, CenHistoricoBean.C_FECHAEFECTIVA) != null) {
						   fecha = GstDate.getFormatedDateShort("", UtilidadesHash.getString(h, CenHistoricoBean.C_FECHAEFECTIVA));
						}
						
				

			 %>   		
			 
				<siga:FilaConIconos fila='<%=(""+i)%>' botones="<%=botones%>" visibleBorrado="no" pintarEspacio="no" clase="listaNonEdit" >
					<td >
						<input type="hidden" name="oculto<%=(""+i)%>_1" value="<%=UtilidadesHash.getString(h, CenHistoricoBean.C_IDINSTITUCION)%>">
						<input type="hidden" name="oculto<%=(""+i)%>_2" value="<%=UtilidadesHash.getString(h, CenHistoricoBean.C_IDPERSONA)%>">
						<input type="hidden" name="oculto<%=(""+i)%>_3" value="<%=UtilidadesHash.getString(h, CenHistoricoBean.C_IDHISTORICO)%>">
						<% if (UtilidadesHash.getString(h, CenHistoricoBean.C_USUMODIFICACION).equalsIgnoreCase(Integer.toString(ClsConstants.USUMODIFICACION_AUTOMATICO))) { %>
							<siga:Idioma key="censo.tipoApunteHistorico.automatico"/>
						<% } else { %>
							<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(h, "NOMBRE_USUARIO"))%>						
						<% } %>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma(UtilidadesHash.getString(h, "DESCRIPCION_TIPO_ACCESO"), userBean))%>
					</td>				
					<td>
						<%=UtilidadesString.mostrarDatoJSP(fecha)%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(UtilidadesHash.getString(h, CenHistoricoBean.C_MOTIVO))%>
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

		<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	     
		        <siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscar"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
      <%  }%>
	    
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
</html>