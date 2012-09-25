<!-- buscarListadoTablasMaestras.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	GenTablasMaestrasBean beanTablaMaestra = (GenTablasMaestrasBean)request.getAttribute("beanTablaMaestra");
	
	//request.removeAttribute("datos");
	request.removeAttribute("beanTablaMaestra");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();
	int aceptabaja = beanTablaMaestra.getAceptabaja();
	String ncols = "";
	String nombrecols = "";
	
	
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
	
	String action=app+"/ADM_GestionarTablasMaestras.do";
    /**************/
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<script>
			function refrescarLocal() {   
				// De momento no queremos que refresque la consulta porque al haber añadido la paginacion, si estabamos en la pagina 2, al refrescar volvemos a la 1
			    // y queremos que nos siga manteniendo donde estabamos. En un futuro se arreglara esto (que refresque la consulta y permanezca en la pagina de la que 
				// viniamos.
				parent.buscar();
			}
		</script>
		<script>
			parent.listadoTablasMaestrasForm.nombreTablaMaestra.value="<%=beanTablaMaestra.getIdTablaMaestra()%>";
			parent.listadoTablasMaestrasForm.nombreCampoCodigo.value="<%=beanTablaMaestra.getIdCampoCodigo()%>";
			parent.listadoTablasMaestrasForm.nombreCampoCodigoExt.value="<%=beanTablaMaestra.getIdCampoCodigoExt()%>";
			parent.listadoTablasMaestrasForm.nombreCampoDescripcion.value="<%=beanTablaMaestra.getIdCampoDescripcion()%>";
			parent.listadoTablasMaestrasForm.local.value="<%=beanTablaMaestra.getLocal()%>";
			parent.listadoTablasMaestrasForm.aliasTabla.value="<%=beanTablaMaestra.getAliasTabla()%>";
			parent.listadoTablasMaestrasForm.longitudCodigo.value="<%=beanTablaMaestra.getLongitudCodigo()%>";
			parent.listadoTablasMaestrasForm.longitudCodigoExt.value="<%=beanTablaMaestra.getLongitudCodigoExt()%>";
			parent.listadoTablasMaestrasForm.longitudDescripcion.value="<%=beanTablaMaestra.getLongitudDescripcion()%>";
			parent.listadoTablasMaestrasForm.tipoCodigo.value="<%=beanTablaMaestra.getTipoCodigo()%>";
			parent.listadoTablasMaestrasForm.tipoCodigoExt.value="<%=beanTablaMaestra.getTipoCodigoExt()%>";
		</script>

	</head>

	<body class="tablaCentralCampos">

		<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" id="nombreTablaMaestra"  name="nombreTablaMaestra" value="<%=beanTablaMaestra.getIdTablaMaestra()%>">
			<input type="hidden" id="nombreCampoCodigo" name="nombreCampoCodigo" value="<%=beanTablaMaestra.getIdCampoCodigo()%>">
			<input type="hidden" id="nombreCampoCodigoExt" name="nombreCampoCodigoExt" value="<%=beanTablaMaestra.getIdCampoCodigoExt()%>">
			<input type="hidden" id="nombreCampoDescripcion" name="nombreCampoDescripcion" value="<%=beanTablaMaestra.getIdCampoDescripcion()%>">
			<input type="hidden" id="local" name="local" value="<%=beanTablaMaestra.getLocal()%>">
			<input type="hidden" id="aliasTabla" name="aliasTabla" value="<%=beanTablaMaestra.getAliasTabla()%>">
			<input type="hidden" id="longitudCodigo"  name="longitudCodigo" value="<%=beanTablaMaestra.getLongitudCodigo()%>">
			<input type="hidden" id="longitudCodigoExt"  name="longitudCodigoExt" value="<%=beanTablaMaestra.getLongitudCodigoExt()%>">
			<input type="hidden" id="longitudDescripcion"  name="longitudDescripcion" value="<%=beanTablaMaestra.getLongitudDescripcion()%>">
			<input type="hidden" id="tipoCodigo"  name="tipoCodigo" value="<%=beanTablaMaestra.getTipoCodigo()%>">
			<input type="hidden" id="tipoCodigoExt"  name="tipoCodigoExt" value="<%=beanTablaMaestra.getTipoCodigoExt()%>">

		</html:form>	
		
		<% if(aceptabaja == 1){
				ncols = "20,55,15,10";
				nombrecols = "general.codeext,general.description,general.estado,";
			}else{
				ncols = "20,70,10";
				nombrecols = "general.codeext,general.description,";
			}
		%>
			
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="<%=nombrecols%>"
		   		  tamanoCol="<%=ncols%>"
		   		  alto="100%"
		   		  ajustePaginador="true"
		   		  modal="P"
		   		  activarFilaSel="true" >

<%				
				if (resultado==null || resultado.size()==0) { %>	

				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0;i<resultado.size();i++)
			   		{
			   			String  botones="C,E,B";
				  		Row fila = (Row)resultado.elementAt(i);
						Hashtable registro = (Hashtable) fila.getRow();
						String estado = "Alta";

						//Si es estan bloqueadas no se pueden ni modificar ni borrar, solo consultar
						if(((String)registro.get("BLOQUEADO"))!=null && ((String)registro.get("BLOQUEADO")).equalsIgnoreCase("S")){
							botones="C";
						}
						
						if(aceptabaja == 1){
							if(((String)registro.get("FECHABAJA"))!=null && !((String)registro.get("FECHABAJA")).equalsIgnoreCase("")){
								estado="Baja desde: "+GstDate.getFormatedDateShort("", (String)registro.get("FECHABAJA"));
							}
						}
						

%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=registro.get("CODIGO")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=registro.get("BLOQUEADO")%>">
						
						<%=registro.get("CODIGOEXTERNO")%>
					</td>
					<td><%=registro.get("DESCRIPCION")%></td>
					
					<% if(aceptabaja == 1){%>
						<td><%=estado%></td>
					<% } %>
					
				</siga:FilaConIconos>
<%
					}
				}
				
%>
			</siga:TablaCabecerasFijas>

		
		<%if (hm.get("datos")!=null && !hm.get("datos").equals("")){%>
	     
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