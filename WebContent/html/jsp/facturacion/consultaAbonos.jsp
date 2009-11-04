<!-- consultaAbonos.jsp -->
<!-- 
	 Muestra los resultados de la busqueda de abonos
	 VERSIONES:
	 miguel.villegas 07-03-2005 Creacion
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>


<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idioma=usr.getLanguage().toUpperCase();
	
	ConsPLFacturacion cpl = new ConsPLFacturacion(usr);
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
	
	String botonesFila = "";
	//String maximo=(String)request.getAttribute("maximo");

	request.getSession().setAttribute("EnvEdicionEnvio","GAF");
	
	Vector resultado=null;
	/** PAGINADOR ***/
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
    if (ses.getAttribute("DATAPAGINADOR")!=null){
	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");
	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  resultado = (Vector)hm.get("datos");
	  PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)hm.get("paginador");
	
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
	String action=app+"/FAC_GenerarAbonos.do?noReset=true";

%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<style type="text/css">

			.especif
			{
				background-color : #<%=src.get("color.titleBar.BG")%>;
				position:absolute; width:964; height:35; z-index:2; top: 285px; left: 0px
			}

		</style>
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}			
		
			function enviar(fila)
			{
			   	
			   	var auxSol = 'oculto' + fila + '_1';
			    var idSolic = document.getElementById(auxSol);			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_3';
			    var idPers = document.getElementById(auxPers);	
			    	    
			    var auxDesc = 'oculto' + fila + '_4';
			    var desc = document.getElementById(auxDesc);			    
			    
			    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
			   	document.DefinirEnviosForm.idPersona.value=idPers.value;
			   	document.DefinirEnviosForm.descEnvio.value="Abono ";
			   	
			   	document.DefinirEnviosForm.modo.value='envioModal';		   	
			   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
			   	if (resultado==undefined||resultado[0]==undefined){			   		
			   	} else {
			   		var idEnvio = resultado[0];
				    var idTipoEnvio = resultado[1];
				    var nombreEnvio = resultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
			   	}
			}
		
		</script>

	</head>

	<body class="tablaCentralCampos">
		<html:form action="/FAC_GenerarAbonos.do?noReset=true" method="post" target="mainWorkArea" style="display:none">
			<html:hidden property="modo" value=""/>				

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
			<siga:TablaCabecerasFijas 
			   nombre="tablaDatos"
			   borde="1"
			   clase="tableTitle"
			   nombreCol="facturacion.busquedaAbonos.literal.numeroAbono,facturacion.busquedaAbonos.literal.fecha,
			   			  facturacion.busquedaAbonos.literal.cliente,gratuita.modalRegistro_DefinirCalendarioGuardia.literal.observaciones,facturacion.busquedaAbonos.literal.estado,
			   			  facturacion.busquedaAbonos.literal.totalAbono,facturacion.busquedaAbonos.literal.numeroFactura,"
			   tamanoCol="8,8,20,20,15,7,8,15"
			   alto="245"
			   ajustePaginador="true" 
			   activarFilaSel="true" >
		       
			<%
	    	if (resultado == null || resultado.size() < 1 )
		    {
			%>
				<br><br><br>
				<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
			<%
	    	}	    
		    else
		    { %>
	    		<%for (int i = 0; i < resultado.size(); i++) { 
							
					Row row = (Row)resultado.elementAt(i);
			        
	            	String total=row.getString("TOTAL");
	            	Boolean isPermitidoBorrar =(Boolean) row.getRow().get("ESPERMITIDOBORRAR");
					total=new Double(UtilidadesNumero.redondea(new Double(row.getString("TOTAL")).doubleValue(),2)).toString();
	            	botonesFila="C";
	            	// Modificada MAV 12/7/05 a instancias JG para no permitir editable cuando el abono esté contabilizado y pagado
	            	// if (row.getString(FacAbonoBean.C_CONTABILIZADA).equalsIgnoreCase(ClsConstants.FACTURA_ABONO_NO_CONTABILIZADA)){
	            	Object estado=cpl.obtenerEstadoFacAbo(new Integer(row.getString(FacAbonoBean.C_IDINSTITUCION)).intValue(),new Long(row.getString(FacAbonoBean.C_IDABONO)).longValue(),ConsPLFacturacion.ABONO);
	            	if (!(row.getString(FacAbonoBean.C_CONTABILIZADA).equalsIgnoreCase(ClsConstants.FACTURA_ABONO_CONTABILIZADA)&&
	            	      estado.toString().equalsIgnoreCase("Pagado"))){
		            	botonesFila+=",E";
	            	}
					//if (row.getString(FacAbonoBean.C_IDABONO).equalsIgnoreCase(maximo)){
					if (isPermitidoBorrar.booleanValue()){
		            	botonesFila+=",B";
		            	
	            	}
	            	

					// boton de envios
					FilaExtElement[] elems = new FilaExtElement[1];
					elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
	            	
	            	%>
					<siga:FilaConIconos
						  fila='<%=""+(i+1)%>'
						  botones="<%=botonesFila%>"
						  modo="edicion"
 						  elementos="<%=elems%>" 
						  pintarEspacio="no" 							  					  							  
						  clase="listaNonEdit">
						  
						<td>
							<input type="hidden" name="oculto<%=(i+1)%>_1" value="<%=row.getString(FacAbonoBean.C_IDABONO)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_2" value="<%=row.getString(FacAbonoBean.C_IDINSTITUCION)%>">

							<!-- ENVIOS 1 idAbono, 3 idPersona, 4 descripcion -->
							<input type="hidden" name="oculto<%=(i+1)%>_3" value="<%=row.getString(FacAbonoBean.C_IDPERSONA)%>">
							<input type="hidden" name="oculto<%=(i+1)%>_4" value=" ">

							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_NUMEROABONO))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",row.getString(FacAbonoBean.C_FECHA)))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(personaAdm.obtenerNombreApellidos(row.getString(FacAbonoBean.C_IDPERSONA)))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacAbonoBean.C_OBSERVACIONES))%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(estado)%>
  						</td>
						<td align="right">
							<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(total))%>&nbsp;&euro;
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(row.getString(FacFacturaBean.C_NUMEROFACTURA))%>
						</td>
					</siga:FilaConIconos>
					<% 
				} 
			} %>
			</siga:TablaCabecerasFijas>
			
			<!-- Metemos la paginación-->		
	 <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
    <%}%>	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>


	<!-- Formulario para la creacion de envio -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "tablaDatosDinamicosD" value = ""/>
		
		<html:hidden property = "idSolicitud" value = ""/>
		<html:hidden property = "idPersona" value = ""/>
		<html:hidden property = "descEnvio" value = ""/>
		
	</html:form>

	</body>
</html>