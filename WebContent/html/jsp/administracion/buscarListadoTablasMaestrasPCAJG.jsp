<!-- buscarListadoTablasMaestrasPCAJG.jsp -->
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
	
	PCAJGTablasMaestrasBean beanTablaMaestra = (PCAJGTablasMaestrasBean)request.getAttribute("beanTablaMaestraPCAJG");
	
	//request.removeAttribute("datos");
	request.removeAttribute("beanTablaMaestraPCAJG");
	
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
	
	String action=app+"/PCAJGGestionarTablasMaestras.do";
    /**************/
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<script>
			function refrescarLocal()
	 		{   // De momento no queremos que refresque la consulta porque al haber añadido la paginacion, si estabamos en la pagina 2, al refrescar volvemos a la 1
			    // y queremos que nos siga manteniendo donde estabamos. En un futuro se arreglara esto (que refresque la consulta y permanezca en la pagina de la que 
				// viniamos.
				parent.buscar();
			}
		</script>
		<script>
			parent.listadoTablasMaestrasPCAJGForm.nombreTablaMaestra.value="<%=beanTablaMaestra.getIdentificador()%>";
			parent.listadoTablasMaestrasPCAJGForm.aliasTabla.value="<%=beanTablaMaestra.getAliasTabla()%>";
			
		</script>

	</head>

	<body class="tablaCentralCampos">

		<html:form action="/PCAJGGestionarTablasMaestras.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" id="nombreTablaMaestra"  name="nombreTablaMaestra" value="<%=beanTablaMaestra.getIdentificador()%>">
			<input type="hidden" id="tablaRelacion"  name="tablaRelacion" value="<%=beanTablaMaestra.getTablaRelacion()%>">
			<input type="hidden" id="aliasTabla" name="aliasTabla" value="<%=beanTablaMaestra.getAliasTabla()%>">
		</html:form>	
		
			
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="general.codeSIGA,general.descripcionSIGA,general.code,general.description,general.abrev,&nbsp;"
		   		  columnSizes="6,25,6,25,6,10"
		   		  modal="P">

<%				
				if (resultado==null || resultado.size()==0) { %>	

				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%
				}
				
				else
				{
					for (int i=0;i<resultado.size();i++){
			 			
			   			String  botones="C,E,B";
				  		Row fila = (Row)resultado.elementAt(i);
						Hashtable registro = (Hashtable) fila.getRow();

						//Si es estan bloqueadas no se pueden ni modificar ni borrar, solo consultar
						if(((String)registro.get("BLOQUEADO"))!=null && ((String)registro.get("BLOQUEADO")).equalsIgnoreCase("S")){
							botones="C";
						}
						
						
							%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
	  				<td align="center"><%=registro.get("CODIGOSIGA")!=null?registro.get("CODIGOSIGA"):""%>&nbsp;</td>
					<td><%=registro.get("DESCRIPCIONSIGA")!=null?registro.get("DESCRIPCIONSIGA"):""%>&nbsp;</td>					
					<td align="center">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=registro.get("IDENTIFICADOR")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=registro.get("BLOQUEADO")%>">						
						<%=registro.get("CODIGO")%>
					</td>
					<td><%=registro.get("DESCRIPCION")%></td>
					<td><%=registro.get("ABREVIATURA")%>&nbsp;</td>
				</siga:FilaConIconos>
				
<%
													

							
					}
				}
				
%>
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