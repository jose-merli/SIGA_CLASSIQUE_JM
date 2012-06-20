<!--- busquedaEnvios.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 15-03-2005 Versión inicial
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String idioma=userBean.getLanguage().toUpperCase();

	//Vector vDatos = (Vector)request.getAttribute("datos");	
	request.removeAttribute("datos");
	
	String  pathFTP = (String)request.getAttribute("pathFTP");
	if (pathFTP==null) pathFTP="";

	//parámetro para la vuelta al listado de solicitudes desde la edición de envío
	request.getSession().setAttribute("EnvEdicionEnvio","");
	
	String pathDescargaOrdinarios = "";
	
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
<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.definir.literal.titulo" 
		localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/ENV_DefinirEnvios.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
				
		    <html:hidden property = "modo"/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "idEnvio"/>
			<html:hidden property = "idTipoEnvio"/>
			<html:hidden property = "idImpresora"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD" id="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="envios.definir.literal.identificador, envios.definir.literal.nombre,
		   		  	envios.definir.literal.fechacreacion,
		   		  	envios.definir.literal.fechaprogramada,
		   		  	envios.definir.literal.estado,
		   		  	envios.definir.literal.tipoenvio,"
		   		  tamanoCol="10,24,12,12,12,12,18"
		   		  alto="333" 
				  ajustePaginador="true"
		   		  activarFilaSel="true" >
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%				}
				else
				{
				
					FilaExtElement[] elems=new FilaExtElement[3];
					elems[0]=new FilaExtElement("enviar","enviar",SIGAConstants.ACCESS_READ);

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
						
						if (idTipoEnvio.equals(new Integer(EnvEnviosAdm.TIPO_CORREO_ORDINARIO).toString())) {
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
						if (existeFichero && (idTipoEnvio.equals(new Integer(EnvEnviosAdm.TIPO_CORREO_ORDINARIO).toString()) && (idEstado.equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO).toString()) || idEstado.equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES).toString()))))
						{
							elems[1]=new FilaExtElement("download", "download", SIGAConstants.ACCESS_READ);
						} else {
							elems[1] = null;
						}

						//Boton de descarga del fichero de log de errores:
						elems[2]=new FilaExtElement("descargaLog", "descargaLog", SIGAConstants.ACCESS_READ);
						
						String fechaCreacionRegistro = fila.getString(EnvEnviosBean.C_FECHACREACION);
						String fechaProgramada = fila.getString(EnvEnviosBean.C_FECHAPROGRAMADA);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B" elementos="<%=elems%>" clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" id="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDENVIO")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" id="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDTIPOENVIOS")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" id="oculto<%=""+(i+1)%>_3" value="<%=pathFechaCreacion%>">
											
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(EnvEnviosBean.C_IDENVIO))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString(EnvEnviosBean.C_DESCRIPCION))%></td>
					<td>
					<% if (fechaCreacionRegistro!=null && !fechaCreacionRegistro.equals("")) { %>
						<%=GstDate.getFormatedDateShort(userBean.getLanguage(),fechaCreacionRegistro)%>
					<% } else { %>
						&nbsp;
					<% } %>
					</td>
					<td>
					<% if (fechaProgramada!=null && !fechaProgramada.equals("")) { %>
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
			
			document.forms[0].idEnvio.value=idEnv.value;
			document.forms[0].idTipoEnvio.value=idTipoEnv.value;
			
			document.forms[0].target="submitArea";	   	
			document.forms[0].modo.value='procesarEnvio';
				
			/*if (idTipoEnv.value=="< %=EnvEnviosAdm.TIPO_CORREO_ELECTRONICO%>") {
				document.forms[0].submit();
				document.forms[0].target="mainWorkArea";	   	
			} else if (idTipoEnv.value=="< %=EnvEnviosAdm.TIPO_CORREO_ORDINARIO%>"){
				var resultado = ventaModalGeneral("DefinirEnviosForm", "P");
			
				if (resultado!=undefined)
				{					
					DefinirEnviosForm.idImpresora.value=resultado;
					DefinirEnviosForm.modo.value="procesarCorreoOrdinario";
					DefinirEnviosForm.target="submitArea";
					DefinirEnviosForm.submit();
				}
			}*/
			
			document.forms[0].submit();
			document.forms[0].target="mainWorkArea";
			//finsubicono('iconoboton_enviar'+fila);
		}

			function download(fila)
			{

				var auxEnv = 'oculto' + fila + '_3';
				var pathEnv = document.getElementById(auxEnv);			          		
			   	
			   	var urlFTP='<%=pathFTP %>' + pathEnv.value;
			   	window.open(urlFTP,'FTPdownload','height=600,width=800,status=yes,toolbar=yes,menubar=no,location=no');
			}


			function descargaLog(fila)
			{
				   var datos;
				   datos = document.getElementById('tablaDatosDinamicosD');
				   datos.value = ""; 
				   var i, j;
				   for (i = 0; i < 5; i++) {
				      var tabla;
				      tabla = document.getElementById('tablaDatos');
				      if (i == 0) {
				        var flag = true;
				        j = 1;
				        while (flag) {
				          var aux = 'oculto' + fila + '_' + j;
				          var oculto = document.getElementById(aux);
				          if (oculto == null)  { flag = false; }
				          else { datos.value = datos.value + oculto.value + ','; }
				          j++;
				        }
				        datos.value = datos.value + "%"
				      } else { j = 2; }
				      if ((tabla.rows[fila].cells)[i].innerHTML == "") {
				        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
				      } else {
				        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
				      }
				   }
				   
				var aux = document.forms[0].target;
				document.forms[0].target="submitArea";
				document.forms[0].modo.value="descargarLogErrores";
				document.forms[0].submit();
				document.forms[0].target = aux;
			}

	</script>
	
		
	</body>
</html>