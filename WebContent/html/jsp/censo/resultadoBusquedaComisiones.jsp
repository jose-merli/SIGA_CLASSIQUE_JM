<!resultadoBusquedaComisiones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.general.CenVisibilidad"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CenDatosCVBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.GstDate"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>



<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("COMISIONES");
	String idioma=usr.getLanguage().toUpperCase();
	Vector resultado = null;
	
		
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.listadoZonas.literal.listadoZonas"/></title>
	
	<script language="JavaScript">
	
 
 </script>
</head>

<body >
	<html:form action="/CEN_GestionarComisiones.do" method="POST" target="submitArea">
			<input type="hidden" name="modo" value="modificar">
			<input type="hidden" name="datosModificados" value="">
			

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">

			
			
			
			
		</html:form>	
		
		<siga:TablaCabecerasFijas 
  				nombre="tablaDatos"
  				borde="2"
  				estilo=""
	   			clase="tableTitle"
  				nombreCol="censo.busquedaClientes.literal.apellidos,censo.busquedaClientes.literal.nombre,censo.busquedaClientes.literal.nColegiado,censo.busquedaClientes.literal.institucion,censo.datosCV.literal.comision,censo.datosCV.literal.cargo,FactSJCS.mantRetencionesJ.literal.fechaInicio,gratuita.modalConfirmar_PestanaCalendarioGuardias.literal.fechafin,"  
   				tamanoCol="20,13,8,8,10,12,10,10,7"
	   			ajusteBotonera="true"
				modal="M" 
		   		activarFilaSel="true" >
		   			
		   	<%	if ( (vDatos != null) && (vDatos.size() > 0) ) {
                   
					
 			  									
			for (int i=0;i<vDatos.size();i++) {
			Hashtable b = (Hashtable) vDatos.get(i);
			String c_subTipo1= "";
			String c_subTipo2= "";		
			String idInstitucion = (b
					.get(CenDatosCVBean.C_IDINSTITUCION) == null || ((String) b
					.get(CenDatosCVBean.C_IDINSTITUCION))
					.equals("")) ? "&nbsp;" : (String) b
					.get(CenDatosCVBean.C_IDINSTITUCION);
				if (b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1)==null || b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1).equals("")){
				 c_subTipo1=" ";
				}else{
				 c_subTipo1=(String)b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1);
				}
				if (b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2)==null || b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2).equals("")){
				 c_subTipo2=" ";
				}else{
				 c_subTipo2=(String)b.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2);
				}
				String botones = "C,E";
				String institucion = CenVisibilidad.getAbreviaturaInstitucion(idInstitucion);
						
						
			 %>   		
			 
				<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' botones="<%=botones%>" visibleBorrado="no" visibleEdicion="no" pintarEspacio="no" clase="listaNonEdit" >
					<td>
					     <input type="hidden" name="oculto<%=i+1%>_1" value="<%=(b.get(CenDatosCVBean.C_IDINSTITUCION))%>">
					     <input type="hidden" name="oculto<%=i+1%>_2" value="<%=(b.get(CenDatosCVBean.C_IDPERSONA))%>"> 
						 <input type="hidden" name="oculto<%=i+1%>_3" value="<%=(b.get(CenDatosCVBean.C_IDCV))%>"> 
						 <input type="hidden" name="oculto<%=i+1%>_4" value="<%=(b.get(CenDatosCVBean.C_IDTIPOCV))%>"> 
						 <input type="hidden" name="oculto<%=i+1%>_5" value="<%=(c_subTipo1)%>"> 
						 <input type="hidden" name="oculto<%=i+1%>_6" value="<%=(c_subTipo2)%>"> 
						 <input type="hidden" name="oculto<%=i+1%>_7" value="<%=UtilidadesString.mostrarDatoJSP(b.get("NCOLEGIADO"))%>">
						 						 
						<%=UtilidadesString.mostrarDatoJSP(b.get("APELLIDOS"))%>
					</td>				
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.get("NOMBRE"))%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.get("NCOLEGIADO"))%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(institucion)%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.get("COMISION"))%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(b.get("CARGO"))%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,b.get(CenDatosCVBean.C_FECHAINICIO)))%>
					</td>
					<td>
						<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,b.get(CenDatosCVBean.C_FECHAFIN)))%>
					</td>
					
					
					
				</siga:FilaConIconos>
				
				<% }  // while
			} // if
			else { %>
				<br><br>
		   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
		 		<br><br>	 		
				
			<% } %>
			
		</siga:TablaCabecerasFijas>		

		

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	</body>
</html>