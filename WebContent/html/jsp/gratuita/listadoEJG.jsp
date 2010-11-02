<!-- listadoEJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.beans.ScsTurnoAdm"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoAdm"%>
<%@ page import="com.siga.beans.ScsTipoEJGBean"%>
<%@ page import="com.siga.beans.ScsTurnoBean"%>
<%@ page import="com.siga.beans.ScsGuardiasTurnoBean"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.beans.ScsMaestroEstadosEJGBean"%>
<%@ page import="com.siga.beans.ScsEJGAdm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.PaginadorBind"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.atos.utils.*"%>

<%@ page import="java.util.*"%>
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
	boolean esComision = usr.isComision();
	
	String idioma=usr.getLanguage().toUpperCase();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
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
		String action=app+"/JGR_EJG.do?noReset=true";
    /**************/

	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
		function refrescarLocal()
		{
			parent.buscar();
		}
	</script>
</head>

<body>

	<html:form action="/JGR_EJG.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo" value="">
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoEJG"
		   borde="1"
		   clase="tableTitle"		   
		   nombreCol="gratuita.busquedaEJG.literal.turnoGuardiaEJG, gratuita.busquedaEJG.literal.turnoDesignacion, gratuita.busquedaEJG.literal.anyo, gratuita.busquedaEJG.literal.codigo, gratuita.busquedaEJG.literal.letradoDesignacion, gratuita.listadoActuacionesAsistencia.literal.fecha, gratuita.busquedaEJG.literal.estadoEJG, gratuita.busquedaEJG.literal.solicitante,"
		   tamanoCol="15,15,5,6,15,9,10,15,10"
		   alto="100%" 
		   ajustePaginador="true"
		   activarFilaSel="true" >

	<%if (resultado.size()>0){%>
  			<%
	    	int recordNumber=1;
	    	String select = "";
	    	Vector v = null;
	    	String botones = "";
	    	String fRatificacion = "";
			while (recordNumber-1 < resultado.size())
			{			
			  
		    Row fila = (Row)resultado.elementAt(recordNumber-1);
			Hashtable registro = (Hashtable) fila.getRow();
			
				//Hashtable fila = (Hashtable)obj.get(recordNumber-1);
				
				// Comprobamos el estado del idfacturacion
	    	ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usr);
				
			// Creamos el Turno/Guardia EJG
			String turno = ScsTurnoAdm.getNombreTurnoJSP(usr.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO"));
			String guardia = ScsGuardiasTurnoAdm.getNombreGuardiaJSP(usr.getLocation(),(String)registro.get("GUARDIATURNO_IDTURNO"),(String)registro.get("GUARDIATURNO_IDGUARDIA")) ;
			String turnoGuardia = " ";
			if ((turno!="")||(guardia!="")){
				turnoGuardia = turno + "/ " + guardia ;
			}
			

	    	fRatificacion = (String)registro.get("FECHARATIFICACION");

	    	
	   		String idFacturacion =  (String)registro.get("IDFACTURACION");
			
	    	boolean isModificable = ((idFacturacion==null||idFacturacion.equals("")) ||(idFacturacion!=null &&(fRatificacion==null||fRatificacion.equals(""))));
	    	if(esComision){
	    		if(isModificable ) botones = "C,E";
				else botones = "C";
	    	}else{
				if(isModificable ) botones = "C,E,B";
				else botones = "C,B";
	    	}
			String CODIGO=null;
			if(registro.get(ScsEJGBean.C_NUMEJG)==null||registro.get(ScsEJGBean.C_NUMEJG).equals(""))
				CODIGO="&nbsp;";
			else
				CODIGO=(String)registro.get(ScsEJGBean.C_NUMEJG);

			%>
			
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td><%=turnoGuardia%>&nbsp;</td>
					<td><%=registro.get("TURNODESIGNA")%></td>
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=usr.getLocation()%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=registro.get(ScsEJGBean.C_ANIO)%>">
					<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=registro.get(ScsEJGBean.C_NUMERO)%>">
					<input type='hidden' name='datosCarta' value='idinstitucion==<%=usr.getLocation()%>##idtipo==<%=registro.get(ScsEJGBean.C_IDTIPOEJG)%>##anio==<%=registro.get(ScsEJGBean.C_ANIO)%>##numero==<%=registro.get(ScsEJGBean.C_NUMERO)%>'>
					<td><%=registro.get(ScsEJGBean.C_ANIO)%></td>
					 <% if (registro.get("SUFIJO")!=null && !registro.get("SUFIJO").equals("")){ %>
						<td><%=CODIGO%>-<%=(String)registro.get(ScsEJGBean.C_SUFIJO)%></td>
						<%}else{%>
						<td><%=CODIGO%></td>
					<% }%>
					
					<td><%=registro.get("LETRADODESIGNA")%></td>
					<td><%=GstDate.getFormatedDateShort("",registro.get(ScsEJGBean.C_FECHAAPERTURA))%>&nbsp;</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("DESC_ESTADO"), usr) %>&nbsp;</td>
					<td><%=ScsEJGAdm.getUnidadEJG(usr.getLocation(),(String)registro.get(ScsEJGBean.C_IDTIPOEJG),(String)registro.get(ScsEJGBean.C_ANIO),(String)registro.get(ScsEJGBean.C_NUMERO)) %>&nbsp;</td>
				</siga:FilaConIconos>		
			<% 	recordNumber++;		   
			} %>
	<%
	}else {
	%>
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
	