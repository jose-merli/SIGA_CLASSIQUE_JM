<!DOCTYPE html>
<html>
<head>
<!-- listadoActuacionesDesignas.jsp -->
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoDesignaForm"%>
<%@ page import = "org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION"%>
<%@ page import = "com.siga.tlds.FilaExtElement"%>
<%@ page import = "com.siga.beans.AdmInformeAdm"%>
<%@ page import = "com.siga.envios.EnvioInformesGenericos"%>



<!-- JSP -->

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
		
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	request.getSession().removeAttribute("resultado");
	String modo = (String) ses.getAttribute("Modo");
	String deDonde=(String)request.getParameter("deDonde");
	String idPersona = (String) request.getAttribute("idPersonaActuacion");
	String alto ="415";
	if (deDonde!=null && deDonde.equals("ficha")) {
		alto ="315";
	}
	String idTurno=(String)request.getSession().getAttribute("DESIGNA_IDTURNO");
	String anio=(String)request.getSession().getAttribute("DESIGNA_ANIO");
	String numero=(String)request.getSession().getAttribute("DESIGNA_NUMERO");
	String idInstitucion=(String)request.getSession().getAttribute("DESIGNA_IDINSTITUCION");
	
	boolean botonNuevo = (Boolean)request.getSession().getAttribute("botonNuevo");
	
	ScsDesignaAdm designaAdm = new ScsDesignaAdm(usr);
	Hashtable designaActual = new Hashtable();
	UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO, 			anio);
	UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO, 			numero);
	UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,	idInstitucion);
	UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,			idTurno);				
	UtilidadesHash.set(designaActual,ScsDesignasLetradoBean.C_IDPERSONA, idPersona);
	
	Vector resultadoDesigna = designaAdm.selectByPK(designaActual);
	ScsDesignaBean bean = new ScsDesignaBean();
	bean = (ScsDesignaBean)resultadoDesigna.get(0); 
	String codigoDesigna=anio+bean.getCodigo();
	
	//Comprobamos si el informe comunicacionesAcreditacionDeOficio está configurado para la institución y si es visible.
		AdmInformeAdm admInformeAdm = new AdmInformeAdm(usr);	
		Vector informeBeansAcreditacionOficio=admInformeAdm.obtenerInformesTipo(usr.getLocation(),EnvioInformesGenericos.comunicacionesAcreditacionDeOficio,null, null);
		
		boolean isActivarCartaAcreditacionOficio = Boolean.FALSE;
		boolean informeUnicoCartaAcreditacion =Boolean.TRUE;
		if(informeBeansAcreditacionOficio != null && informeBeansAcreditacionOficio.size() >0 ){
			for(int i=0; i<informeBeansAcreditacionOficio.size();i++){
				AdmInformeBean datoInformeAcreditacionOficio = (AdmInformeBean)informeBeansAcreditacionOficio.get(i);
				if(String.valueOf(datoInformeAcreditacionOficio.getIdInstitucion()).equalsIgnoreCase(usr.getLocation())){
					if(datoInformeAcreditacionOficio.getVisible() != null && datoInformeAcreditacionOficio.getVisible().equalsIgnoreCase("S")){
						isActivarCartaAcreditacionOficio = Boolean.TRUE;
					}else{//No es visible
					    isActivarCartaAcreditacionOficio = Boolean.FALSE;
					}
					
				}
				
				
			}
			//Comprobamos si tiene uno o varios informes
			
			if(informeBeansAcreditacionOficio.size() >1){
				 informeUnicoCartaAcreditacion = Boolean.FALSE;
			}
			
		}
		
%>	
											
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Properties"%>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="gratuita.actuacionesDesigna.literal.titulo" 
		localizacion="gratuita.actuacionesDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
		function refrescarLocal (){
			parent.buscar();
		}
		
		function comunicar(fila, id) {
			if (typeof id == 'undefined')
				id='tablaDatos';
			preparaDatos(fila,id);
			var datosDesigna = "idInstitucion=="+<%=idInstitucion%> +"##idPersona=="+jQuery("#ocultoHidden"+fila+"_2").val()+ "##idTurno==" +<%=idTurno%>+"##anio=="+<%=anio%> +"##numero==" +<%=numero%>+
			"##numeroAsunto==" +jQuery("#ocultoHidden"+fila+"_1").val()  +"##codigoDesigna=="+<%=codigoDesigna%>+"##idPersonaActuacion=="+jQuery("#ocultoHidden"+fila+"_3").val()+"%%%";
			document.Informe.datosInforme.value=datosDesigna;
			
			
		//Si no es letrado
			if(!<%=usr.isLetrado()%>){
				document.Informe.destinatarios.value='C';
				document.Informe.enviar.value='1';
				
				
				//Esto permite la descarga de varios informes
				var arrayResultado = ventaModalGeneral("Informe","M");
			   	if (arrayResultado==undefined||arrayResultado[0]==undefined|| typeof(arrayResultado) == "string"){
			   				   		
			   	} 
			   	else {
			   		
			   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
			   		if(confirmar){
			   			var idEnvio = arrayResultado[0];
					    var idTipoEnvio = arrayResultado[1];
					    var nombreEnvio = arrayResultado[2];				    
					    
					   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
					   	document.DefinirEnviosForm.modo.value='editar';
					   	document.DefinirEnviosForm.submit();
			   		}
			   	}
			
			}else{//Es letrado, luego no puede enviar
				<%if(informeUnicoCartaAcreditacion){%> //Sólo un informe configurado
					document.Informe.enviar.value='0';
					document.Informe.submit();
				<%}else{%> //Más de un informe configurado pero no se envía
					var arrayResultado = ventaModalGeneral("Informe","M");
					if (arrayResultado==undefined||arrayResultado[0]==undefined|| typeof(arrayResultado) == "string"){
					   		
				   	} 
				   	else {
				   		
				   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
				   		if(confirmar){
				   			var idEnvio = arrayResultado[0];
						    var idTipoEnvio = arrayResultado[1];
						    var nombreEnvio = arrayResultado[2];				    
						    
						   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
						   	document.DefinirEnviosForm.modo.value='editar';
						   	document.DefinirEnviosForm.submit();
				   		}
				   	}
				<%}%>
			}
		
		 }
		
		//Funcion asociada a boton nuevo
		function anticiparImporte(fila, id) 
		{	
			document.movimientosVarios.modo.value="nuevo";
			document.movimientosVarios.target="submitArea";
			document.movimientosVarios.nactuacion.value=jQuery("#ocultoHidden"+fila+"_1").val(); 
			var resultado=ventaModalGeneral(document.movimientosVarios.name,"M");
			//if (resultado=="MODIFICADO")buscar2();
		}
		 
	</script>

</head>

<body class="tablaCentralCampos" >
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
					    String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
						ScsDesignaAdm adm = new ScsDesignaAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaDesigna(idInstitucion, anio, numero,idTurno);

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
							
						}
					
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>
	
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_ActuacionesDesigna.do" method="post" target="submitArea"  style="display:none">
			<html:hidden property = "modo"  styleId = "modo"  value = ""/>
			<html:hidden property = "idTurno" value= "<%=idTurno%>"/>
			<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden property = "anio" value="<%=anio%>" />	
			<html:hidden property = "numero" value="<%=numero%>" />
			<html:hidden property = "nactuacion"  />
		</html:form>
		
		<html:form action="/INF_InformesGenericos" name="Informe" type="com.siga.informes.form.InformesGenericosForm"  method="post"	target="submitArea">
			<html:hidden property="modo" value = "preSeleccionInformes"/>
			<html:hidden property="idTipoInforme" value='CADO'/>
			<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden property="enviar"  value="0"/>
			<html:hidden property="descargar" value="1"/>
			<html:hidden property="datosInforme"/>
			<html:hidden property="destinatarios"/>
			<input type='hidden' name='actionModal'>
	   </html:form>
		<!-- Formulario para la edicion del envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			<html:hidden property="idTipoInforme" value="CENSO#OSEPA#ASEPA"/>
		
		</html:form>
		
		 <html:form action="/JGR_MovimientosVariosLetrado?noReset=true" method="POST" target="submitArea" styleId="movimientosVarios">
			<html:hidden name="MantenimientoMovimientosForm" property = "modo" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property = "actionModal" value = ""/>
			<html:hidden name="MantenimientoMovimientosForm" property="checkHistorico" value=""/>
			<html:hidden name="MantenimientoMovimientosForm" property="idPersona" value=""/>
			<input type="hidden" name="limpiarFilaSeleccionada" value="">
			<input type="hidden" name="botonBuscarPulsado" value="">
			<input type="hidden" name="mostrarMovimientos" value="">
			
			<html:hidden property = "idTurno" value= "<%=idTurno%>"/>
			<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden property = "anio" value="<%=anio%>" />	
			<html:hidden property = "numero" value="<%=numero%>" />
			<html:hidden property = "nactuacion"  />
			<html:hidden property = "origen" value="ACTUACIONESDESIGNAS"  />
			
		</html:form>
			<!-- Campo obligatorio -->
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="gratuita.actuacionesAsistencia.literal.fechaActuacion,gratuita.busquedaAsistencias.literal.numero,gratuita.actuacionesDesigna.literal.modulo,gratuita.procedimientos.literal.acreditacion,gratuita.actuacionesDesigna.literal.justificacion,gratuita.actuacionesDesigna.literal.validada,gratuita.procedimientos.literal.anulada,gratuita.actuacionesDesigna.literal.facturacion,"
			   columnSizes="8,4,26,10,8,6,6,20,12"
			   modal="G"
			   fixedHeight="auto">

		<% if (obj==null || obj.size()==0) { %>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		<% } else { %>
		
			  <%
		    	int recordNumber=1;
		    	String select = "";
		    	String botones = "";
		    	Vector v = null;
		    	ScsActuacionDesignaAdm scsActuacionDesignaAdm = new ScsActuacionDesignaAdm(usr);
		    	FcsFactEstadosFacturacionAdm fcsFactEstadosFacturacionAdm = new FcsFactEstadosFacturacionAdm(usr);
		    	
				while ((recordNumber) <= obj.size()){	 
				    modo = (String) ses.getAttribute("Modo");
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					String idFacturacion = (String)hash.get("IDFACTURACION");
					String facturacionCerrada = (String)hash.get("FACTURACIONCERRADA");
										
					String estadoActualFacturacion = fcsFactEstadosFacturacionAdm.getIdEstadoFacturacion(idInstitucion, idFacturacion);
										
					boolean modificable = (idFacturacion==null || idFacturacion.equals("") 
							|| (estadoActualFacturacion != null && Integer.parseInt(estadoActualFacturacion) == ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo() ));

// RGG 					String nombreProc = ((String) hash.get("NOMBREPROCEDIMIENTO")) + ((hash.get("NOMBREACREDITACION")==null)?"":(" - " + (String)hash.get("NOMBREACREDITACION")));
					String nombreProc = (String) hash.get("NOMBREPROCEDIMIENTO");
					boolean  validada = ((String) hash.get("VALIDADA")).equals("1")?true:false;
					

// RGG 14-03-2006 CAMBIOS para controlar los permisos de los botones de edicion y borrado
                  
				    if (deDonde!=null && deDonde.equals("ficha")){
						// cuando esta en la ficha solamente se puede consultar, sea quien sea
						botones = "C";
						
						if (usr.isLetrado() && !validada && modificable) {
							botones = "C,E,B";
						}
					} 
					else {
						if (usr.isLetrado()) {
							// caso de menu SJCS
							// si es letrado solamente en consulta
							botones = "C";
						} else {
							if(modificable) {
								// modificable
								botones = "C,E,B";
							} else {
							    botones = "C";						
							}
						}
					}

				 String anulacion = (String)hash.get("ANULACION");
				 if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1")){
				 	modo="VER";	
				 }
				
			 	%>
			 	<%
			 	FilaExtElement[] elems = null;
			 	elems = new FilaExtElement[2];
				 	if(isActivarCartaAcreditacionOficio){
						elems[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
				 	}
				 	if(idFacturacion != null && !"".equalsIgnoreCase(idFacturacion) && facturacionCerrada != null && !"".equalsIgnoreCase(facturacionCerrada)){
				 		elems[1]=new FilaExtElement("anticiparImporte", "anticiparImporte", "movimientosVarios.icono.alt", SIGAConstants.ACCESS_FULL);
				 	}
				 	
			 	%>
			 
				  	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" modo="<%=modo%>" elementos="<%=elems%>" >
						<td>
							<input type="hidden" name="ocultoHidden<%=String.valueOf(recordNumber)%>_1" id="ocultoHidden<%=String.valueOf(recordNumber)%>_1" value="<%=hash.get("NUMEROASUNTO")%>">
							<input type="hidden" name="ocultoHidden<%=String.valueOf(recordNumber)%>_2" id="ocultoHidden<%=String.valueOf(recordNumber)%>_2" value="<%=hash.get("IDPERSONA")%>">
							<input type="hidden" name="ocultoHidden<%=String.valueOf(recordNumber)%>_3" id="ocultoHidden<%=String.valueOf(recordNumber)%>_3" value="<%=hash.get("IDPERSONAACTUACION")%>">
							<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",(String)hash.get("FECHA")))%>
						</td>
						<td><%=hash.get("NUMEROASUNTO")%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(nombreProc)%></td>
						<td>
							<%=hash.get("NOMBREACREDITACION")%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",(String)hash.get("FECHAJUSTIFICACION")))%></td>
						
						<td><%if (validada) { %><siga:Idioma key="general.yes"/>
							<% } else { %><siga:Idioma key="general.no"/>
							<% } %>&nbsp;
						</td>
						<td>
							<%if(anulacion.equalsIgnoreCase("1")){%>
								<siga:Idioma key="gratuita.operarEJG.literal.si"/>
							<%}else if(anulacion.equalsIgnoreCase("0")){%>
								<siga:Idioma key="general.boton.no"/>
							<%}%>
						</td>
						<td><%=((String) hash.get("NOMBREFACTURACION")==null?"":(String) hash.get("NOMBREFACTURACION"))%>&nbsp;
						</td>
						
					</siga:FilaConIconos>	 
				<%recordNumber++;%>
				<%}%>	 
		<%}%>

		</siga:Table>
	<div style="position:absolute; left:150px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<%if(!botonNuevo){%>
							<siga:Idioma key="gratuita.actuacionDesigna.art27.texto1"/>
						<%}%>
					</td>
				</tr>
			</table>
		</div>		

	</body>
</html>
		  
		
