<!-- listadoSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsSOJBean"%>
<%@ page import="com.siga.beans.ScsTipoSOJ"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.beans.ScsDefinirSOJAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>


<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%> 

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
	String idioma=usr.getLanguage().toUpperCase();
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
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
	  
	    PaginadorBind paginador = (PaginadorBind)hm.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	  
	
	 	
	 }else{
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
		String action=app+"/JGR_ExpedientesSOJ.do?noReset=true";
    /**************/

	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.busquedaSOJ.literal.expedientesSOJ"/></title>
	
	<script type="text/javascript">
		function refrescarLocal() 
		{
			parent.buscar();
		}
	</script>
</head>

<body>
	<html:form action="/JGR_ExpedientesSOJ.do" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">

		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoSOJ"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaSOJ.literal.apertura, gratuita.busquedaSOJ.literal.turno, gratuita.busquedaSOJ.literal.guardia, gratuita.busquedaSOJ.literal.anyo, gratuita.busquedaSOJ.literal.codigo, gratuita.busquedaSOJ.literal.tipoSOJ, gratuita.busquedaSOJ.literal.SOJColegio, gratuita.busquedaSOJ.literal.estadoSOJ, "
		   tamanoCol="10,17,17,4,7,11,12,10,11"
		   alto="258" 
		   activarFilaSel="true" 
		   ajustePaginador="true">

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String botones = "";
	    	String select = "";
	    	Vector v = null;
	    	ScsDefinirSOJAdm scsDefinirSOJAdm = new ScsDefinirSOJAdm(usr);
			while (recordNumber-1 < resultado.size())
			{	
				 Row fila = (Row)resultado.elementAt(recordNumber-1);
				Hashtable registro = (Hashtable) fila.getRow();		
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				// Comprobamos el estado del idfacturacion
				String idFacturacion =  (String)registro.get("IDFACTURACION");;
				
				if(idFacturacion==null||idFacturacion.equals("")) botones = "C,E,B";
				else botones = "C,B";
				
				String IDPERSONAJG=null;
				if(registro.get(ScsSOJBean.C_IDPERSONAJG)==null||registro.get(ScsSOJBean.C_IDPERSONAJG).equals(""))
					IDPERSONAJG=" ";
				else
					IDPERSONAJG=(String)registro.get(ScsSOJBean.C_IDPERSONAJG);
					
				String CODIGO=null;
				if(registro.get(ScsSOJBean.C_NUMSOJ)==null||registro.get(ScsSOJBean.C_NUMSOJ).equals("")){
					CODIGO="&nbsp;";
				}else{
					if(registro.get(ScsSOJBean.C_SUFIJO)!=null && !registro.get(ScsSOJBean.C_SUFIJO).equals("")){
					  CODIGO=(String)registro.get(ScsSOJBean.C_NUMSOJ)+'-'+(String)registro.get(ScsSOJBean.C_SUFIJO);
					}else{
					  CODIGO=(String)registro.get(ScsSOJBean.C_NUMSOJ);	
					}
				}	
					
				
				String nombreTurno=ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),registro.get(ScsSOJBean.C_IDTURNO).toString());
				
				
				String nombreGuardia=ScsGuardiasColegiadoAdm.getNombreGuardia(usr.getLocation(),registro.get(ScsSOJBean.C_IDTURNO).toString(),registro.get(ScsSOJBean.C_IDGUARDIA).toString());
				
				
				String tipoSoj=ScsDefinirSOJAdm.getNombreTipoSOJ(registro.get(ScsSOJBean.C_IDTIPOSOJ).toString());
				String descTipoSoj="";
				String descTipoSojColegio="";
				if (tipoSoj!=null &&	!tipoSoj.equals("")){
				   descTipoSoj=UtilidadesMultidioma.getDatoMaestroIdioma(tipoSoj.toString(),usr);
				}
				   
				
				String tipoSojColegio=ScsDefinirSOJAdm.getNombreTipoSOJColegio(usr.getLocation().toString(),registro.get(ScsSOJBean.C_IDTIPOSOJCOLEGIO).toString());
				if (tipoSojColegio!=null &&	!tipoSojColegio.equals("")){
				   descTipoSojColegio=UtilidadesMultidioma.getDatoMaestroIdioma(tipoSojColegio.toString(),usr);
				}
				
				
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsSOJBean.C_IDTIPOSOJ)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=IDPERSONAJG%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsSOJBean.C_NUMERO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=registro.get(ScsSOJBean.C_ANIO)%>">
					<%=GstDate.getFormatedDateShort("",registro.get(ScsSOJBean.C_FECHAAPERTURA).toString())%>&nbsp;</td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombreTurno)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(nombreGuardia)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(registro.get(ScsSOJBean.C_ANIO))%></td>
					<td><%=CODIGO%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(descTipoSoj)%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(descTipoSojColegio)%></td>
					<td><%if (registro.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("A")) {%><siga:Idioma key="gratuita.SOJ.estado.abierto"/><%} else if (registro.get(ScsSOJBean.C_ESTADO).toString().equalsIgnoreCase("P")){%><siga:Idioma key="gratuita.SOJ.estado.pendiente"/><%} else {%><siga:Idioma key="gratuita.SOJ.estado.cerrado"/><%}%></td>
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
			
	<%
	}else {
	%>
	<br>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>
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
															
	
	 <%}%>	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	