<!-- buscarListadoCertificados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.atos.utils.Row"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//Vector vDatos = (Vector)request.getAttribute("datos");
	
	request.removeAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();
	
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
	
	String action=app+"/ADM_GestionarCertificados.do";
    /**************/
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<script>
			function refrescarLocal()
			{
				parent.buscar();
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<html:form action="/ADM_GestionarCertificados.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="administracion.certificados.literal.nombre,administracion.certificados.literal.numeroSerie,administracion.certificados.literal.fechaCaducidad,administracion.certificados.literal.pendienteRevocar,administracion.certificados.literal.roles,"
		   		  tamanoCol="20,35,10,10,15,10"
		   		  alto="100%"
		   		  modal="M"
				  ajustePaginador="true"
		   		  activarFilaSel="true" >

<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
				  		//AdmCertificadosBean bean = (AdmCertificadosBean)vDatos.elementAt(i);
				  		//Hashtable registro = (Hashtable)vDatos.elementAt(i);
						Row fila = (Row)resultado.elementAt(i);
					    Hashtable registro = (Hashtable) fila.getRow();
				  		//AdmCertificadosBean bean = (AdmCertificadosBean)registro.get("datos");
				  		String nombreUsuario = (String)registro.get("DESCRIPCION");
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=registro.get("IDUSUARIO")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=registro.get("IDINSTITUCION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=registro.get("NUMSERIE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=registro.get("REVOCACION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=registro.get("FECHACAD")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=nombreUsuario%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=registro.get("NIF")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_8" value="<%=registro.get("ROL")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_9" value=" <%=registro.get("IDSROLES")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_10" value="<%=registro.get("USUMODIFICACION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_11" value="<%=registro.get("FECHAMODIFICACION")%>">
						
						<%=nombreUsuario%>
					</td>
					<td><%=registro.get("NUMSERIE")%></td>
					<td><%=GstDate.getFormatedDateShort(userBean.getLanguage(), registro.get("FECHACAD"))%></td>
					<td><%if (registro.get("REVOCACION").equals("S")) {%><siga:Idioma key="general.boton.yes"/><%} else {%><siga:Idioma key="general.boton.no"/><%}%></td>
					<td>&nbsp;<%=registro.get("IDSROLES")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>

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