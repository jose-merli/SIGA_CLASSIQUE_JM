<!DOCTYPE html>
<html>
<head>
<!--- busquedaEnvios.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 15-03-2005 Versi�n inicial
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.text.*"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="java.io.File" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();

	//Vector vDatos = (Vector)request.getAttribute("datos");	
	request.removeAttribute("datos");
	
	String  pathFTP = (String)request.getAttribute("pathFTP");
	if (pathFTP==null) pathFTP="";

	//par�metro para la vuelta al listado de solicitudes desde la edici�n de env�o
	request.getSession().setAttribute("EnvEdicionEnvio","");
	
	String pathDescargaOrdinarios = "";
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	
	
	HashMap hm=new HashMap();
	String idPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(idPaginador)!=null) {
		hm = (HashMap)ses.getAttribute(idPaginador);
	
	 if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  resultado = (Vector)hm.get("datos");
	  Paginador paginador = (Paginador)hm.get("paginador");
	
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
	
	String action=app+"/ENV_DefinirEnvios.do?noReset=true";
    /**************/
%>	

<%@page import="java.io.File"%>


<!-- HEAD -->
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="envios.definir.literal.titulo" 
		localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
				
		    <html:hidden styleId = "modo" property = "modo"/>
			<html:hidden styleId = "hiddenFrame"  property = "hiddenFrame"  value = "1"/>
			<html:hidden styleId = "idEnvio" property = "idEnvio"/>
			<html:hidden styleId = "idTipoEnvio"  property = "idTipoEnvio"/>
			<html:hidden styleId = "idImpresora"  property = "idImpresora"/>
			<html:hidden styleId = "idEstadoEnvio" property = "idEstado"/>
			<html:hidden styleId = "origen" property = "origen" value ="${path}"/>
			<html:hidden styleId = "tipoEnvio"  property = "tipoEnvio"/>
			<html:hidden styleId = "estado" property = "estado"/>
			
			<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>

			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.definir.literal.identificador, envios.definir.literal.nombre,
		   		  	envios.definir.literal.fechacreacion,
		   		  	envios.definir.literal.fechaprogramada,
		   		  	envios.definir.literal.estado,
		   		  	envios.definir.literal.tipoenvio,"
		   		  columnSizes="10,24,12,12,12,12,18">
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%				if (resultado==null || resultado.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%				}
				else
				{
				
					FilaExtElement[] elems=new FilaExtElement[3];

					GenParametrosAdm paramAdm = new GenParametrosAdm(userBean);
					String pathFTPFaxAux = 
						paramAdm.getValor(userBean.getLocation(),"ENV","PATH_DESCARGA_ENVIOS_FAX","")+
						File.separatorChar + userBean.getLocation();
					String pathFTPOrdAux = 
						paramAdm.getValor(userBean.getLocation(),"ENV","PATH_DESCARGA_ENVIOS_ORDINARIOS","")+
					 	File.separatorChar + userBean.getLocation();
					
			 		for (int i=0; i<resultado.size(); i++)
			   		{
			   			String pathSistema = "";
				  		//Row fila = (Row)vDatos.elementAt(i);	
						 Row fila = (Row)resultado.elementAt(i);
			           //Hashtable registro = (Hashtable) fila.getRow();
				  		String pathFechaCreacion="";
						String idEnvio = fila.getString("IDENVIO");
						if (idEnvio!=null) {
						    String fechaCreacion=fila.getString(EnvEnviosBean.C_FECHACREACION);
							SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
						    Calendar cal = Calendar.getInstance();
						    try {
							
							   
								Date d = sdf.parse(fechaCreacion);
						        cal.setTime(d);
							    String anio = String.valueOf(cal.get(Calendar.YEAR));
								String mes = String.valueOf(cal.get(Calendar.MONTH)+1);		
								String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));		
								String aux = "/" + anio + "/" + mes + "/" + dia + "/" + idEnvio + "/" ;						
							    pathSistema = File.separatorChar + anio + File.separatorChar + mes + File.separatorChar + dia + File.separatorChar + idEnvio + File.separatorChar;						
						      	pathFechaCreacion = aux;
							 
						    } catch (ParseException e1) {
						    }
						}
							
						String idEstado = fila.getString(EnvEnviosBean.C_IDESTADO);
						String idTipoEnvio = fila.getString("IDTIPOENVIOS");
						boolean existeFichero=true;
						String auxPath="";
						
						if (idTipoEnvio.equals(new Integer(EnvTipoEnviosAdm.K_CORREO_ORDINARIO).toString())) {
							auxPath = pathFTPOrdAux + pathSistema;
						} else {
							auxPath = pathFTPFaxAux + pathSistema;
						}
							
						// RGG esto es lo que hay que descomentar y comentar lo de abajo
						// PROBLEMA DE TIEMPO EN ENVIOS
						//File aux2 = new File(auxPath);
						//if (!aux2.exists()) {
						//	existeFichero = false;
						//}
						existeFichero = true;

						//Boton de descarga del envio:
						if (existeFichero && (idTipoEnvio.equals(new Integer(EnvTipoEnviosAdm.K_CORREO_ORDINARIO).toString()) && (idEstado.equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO).toString()) || idEstado.equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES).toString()))))
						{
							elems[2]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
						} else {
							elems[2] = null;
						}

						//Boton de descarga del fichero de log de errores:
						elems[1]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
						
						String fechaCreacionRegistro = fila.getString(EnvEnviosBean.C_FECHACREACION);
						String fechaProgramada = fila.getString(EnvEnviosBean.C_FECHAPROGRAMADA);
						
						String botones = "C";
						
						if(fila.getString("FECHABAJA") == null || fila.getString("FECHABAJA").equals("")){
							
							if(fila.getString("IDESTADO") != null && fila.getString("IDESTADO").equals(String.valueOf(EnvEnviosAdm.ESTADO_PROCESADO))){
								botones += ",B";
								elems[0]=new FilaExtElement("enviardenuevo","enviardenuevo",SIGAConstants.ACCESS_READ);
							}else if(fila.getString("IDESTADO") != null && fila.getString("IDESTADO").equals(String.valueOf(EnvEnviosAdm.ESTADO_PROCESADO_ERRORES))){
								botones += ",B";
								elems[0]=new FilaExtElement("reenviar","reenviar",SIGAConstants.ACCESS_READ);
							}else{
								botones += ",E,B";
								elems[0]=new FilaExtElement("enviar","enviar",SIGAConstants.ACCESS_READ);
							}
						} else {
							elems[0]=new FilaExtElement("enviardenuevo","enviardenuevo",SIGAConstants.ACCESS_READ);
						}
						if(idEstado.equals("0")){
							elems[0] = null;
							elems[1] = null;
							elems[2] = null;
							botones ="";
							
						}
						
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>"elementos="<%=elems%>" clase="listaNonEdit">
											
					<td><input type="hidden" name="oculto<%=""+(i+1)%>_1" id="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDENVIO")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" id="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDTIPOENVIOS")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" id="oculto<%=""+(i+1)%>_3" value="<%=pathFechaCreacion%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" id="oculto<%=""+(i+1)%>_4" value="<%=fila.getString("IDESTADO")%>"><%=UtilidadesString.mostrarDatoJSP(fila.getString(EnvEnviosBean.C_IDENVIO))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(EnvEnviosBean.C_DESCRIPCION))%></td>
					<td>
					<% if (fechaCreacionRegistro!=null && !fechaCreacionRegistro.equals("")) { %>
						<%=GstDate.getFormatedDateShort(userBean.getLanguage(),fechaCreacionRegistro)%>
					<% } else { %>
						&nbsp;
					<% } %>
					</td>
					<td>
					<% if (fechaProgramada!=null && !fechaProgramada.equals("")&& !fechaProgramada.equals("null")) { %>
						<%=GstDate.getFormatedDateMedium(userBean.getLanguage(),fechaProgramada)%>
					<% } else { %>
						&nbsp;
					<% } %>
					</td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(fila.getString("ESTADO"),userBean)%></td>
					<td><%=UtilidadesMultidioma.getDatoMaestroIdioma(fila.getString("TIPOENVIO"),userBean)%></td>
				</siga:FilaConIconos>
<%					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
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
		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">
		
		function refrescarLocal()
		{
			parent.buscar() ;
		}
		
		function enviar(fila) {
			sub();
			subicono('iconoboton_enviar'+fila);
			var auxEnv = 'oculto' + fila + '_1';
			var idEnv = document.getElementById(auxEnv);			          		
			
			var auxTipoEnv = 'oculto' + fila + '_2';
			var idTipoEnv = document.getElementById(auxTipoEnv);			          		
			var auxEstadoEnv = 'oculto' + fila + '_4';
			var idEstadoEnv = document.getElementById(auxEstadoEnv);
			
			document.forms[0].idEnvio.value=idEnv.value;
			document.forms[0].idTipoEnvio.value=idTipoEnv.value;
			document.forms[0].idEstado.value=idEstadoEnv.value;
			
			document.forms[0].target="submitArea";	   	
			document.forms[0].modo.value='procesarEnvio';
			document.forms[0].submit();
			document.forms[0].target="mainWorkArea";
		}
		function reenviar(fila) {
			if(!confirm('<siga:Idioma key="envios.confirmar.reenviar"/>')){
				return false;
			}
			
			sub();
			
			
			subicono('iconoboton_reeenviar'+fila);
			var auxEnv = 'oculto' + fila + '_1';
			var idEnv = document.getElementById(auxEnv);			          		
			var auxTipoEnv = 'oculto' + fila + '_2';
			var idTipoEnv = document.getElementById(auxTipoEnv);			          		
			var auxEstadoEnv = 'oculto' + fila + '_4';
			var idEstadoEnv = document.getElementById(auxEstadoEnv);
			document.forms[0].target="mainWorkArea";
			document.forms[0].idEnvio.value=idEnv.value;
			document.forms[0].tipoEnvio.value=idTipoEnv.value;
			document.forms[0].estado.value=idEstadoEnv.value;
			document.forms[0].modo.value='reenviar';
			document.forms[0].submit();
			alert('<siga:Idioma key="envios.aviso.reenviar.ok"/>');
			
		}
		function enviardenuevo(fila) {
			
			if(!confirm('<siga:Idioma key="envios.confirmar.duplicarcomonuevo"/>')){
				return false;
			}		
			sub();
			subicono('iconoboton_enviardenuevo'+fila);
			var auxEnv = 'oculto' + fila + '_1';
			var idEnv = document.getElementById(auxEnv);			          		
			
			var auxTipoEnv = 'oculto' + fila + '_2';
			var idTipoEnv = document.getElementById(auxTipoEnv);			          		
			var auxEstadoEnv = 'oculto' + fila + '_4';
			var idEstadoEnv = document.getElementById(auxEstadoEnv);
			
			document.forms[0].idEnvio.value=idEnv.value;
			document.forms[0].idTipoEnvio.value=idTipoEnv.value;
			document.forms[0].idEstado.value=idEstadoEnv.value;
			
			document.forms[0].target="submitArea";	   	
			document.forms[0].modo.value='enviardenuevo';
			document.forms[0].submit();
			document.forms[0].target="mainWorkArea";
		}

			function download(fila)
			{

				var auxEnv = 'oculto' + fila + '_3';
				var pathEnv = document.getElementById(auxEnv);			          		
			   	
			   	var urlFTP='<%=pathFTP %>' + pathEnv.value;
			   	window.open(urlFTP,'FTPdownload','height=600,width=800,status=yes,toolbar=yes,menubar=no,location=no');
			}


			function descargaLog(fila) {
				preparaDatos(fila,'tablaDatos');
				
				var aux = document.forms[0].target;
				document.forms[0].target="submitArea";
				document.forms[0].modo.value="descargarLogErrores";
				document.forms[0].submit();
				document.forms[0].target = aux;
			}

	</script>
	
		
	</body>
</html>