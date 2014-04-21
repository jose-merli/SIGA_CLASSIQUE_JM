<!DOCTYPE html>
<html>
<head>
<!-- ficherosMandatoCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenAnexosCuentasBancariasBean"%>
<%@ page import="com.siga.beans.CenMandatosCuentasBancariasBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	UsrBean usuario = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String nombrePersona = (String) request.getParameter("nombrePersona");
	String numero = (String) request.getParameter("numero");
	Vector<CenAnexosCuentasBancariasBean> vListadoAnexos = (Vector<CenAnexosCuentasBancariasBean>) request.getAttribute("vListadoAnexos");
	CenMandatosCuentasBancariasBean beanMandato = (CenMandatosCuentasBancariasBean) request.getAttribute("beanMandato");
	String modoMandato = (String) request.getParameter("modoMandato");
	
	// JPT: Gestion de Volver y botones
	String botones = "";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null || usuario.isLetrado()) {
		busquedaVolver = "volverNo";
		if (modoMandato.equals("editar")) {
			botones = "N";
		}
	} else {
		botones = "V";
		if (modoMandato.equals("editar")) {
			botones = ",N";
		}
	}	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<siga:TituloExt titulo="pestana.fichaCliente.datosBancarios.ficherosAnexos" localizacion="censo.fichaCliente.bancos.mandatos.ficheros.localizacion" />
</head>

<body>
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" height="32px">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;

<%
				if ((numero != null) && (!numero.equalsIgnoreCase(""))) {
%>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
<%
				} else {
%>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<%
				}
%>
			</td>
		</tr>
	</table>	
	
	<form id="MandatosCuentasBancariasForm" name="MandatosCuentasBancariasForm" method="post" action="/SIGA/CEN_MandatosCuentasBancarias.do">
		<html:hidden property="modo" styleId="modo"  value="" />
		<input type='hidden' name="idInstitucion" id="idInstitucion" value="<%=beanMandato.getIdInstitucion()%>" />
		<input type='hidden' name="idPersona" id="idPersona" value="<%=beanMandato.getIdPersona()%>" />
		<input type='hidden' name="idCuenta" id="idCuenta" value="<%=beanMandato.getIdCuenta()%>" />
		<input type='hidden' name="idMandato" id="idMandato" value="<%=beanMandato.getIdMandato()%>" />
		<html:hidden property="idFichero" value=""/>
				
		<html:hidden property="nombrePersona" styleId="nombrePersona" value="<%=nombrePersona%>" />
		<html:hidden property="numero" styleId="numero" value="<%=numero%>" />
		<html:hidden property="modoMandato" styleId="modoMandato" value="<%=modoMandato%>" />
		<html:hidden property="actionModal" value=""/>
	</form>
	
	<form id="AnexosCuentasBancariasForm" name="AnexosCuentasBancariasForm" method="post" action="/SIGA/CEN_AnexosCuentasBancarias.do">
		<html:hidden property="modo" styleId="modo"  value="" />
		<input type='hidden' name="idInstitucion" id="idInstitucion" value="<%=beanMandato.getIdInstitucion()%>" />
		<input type='hidden' name="idPersona" id="idPersona" value="<%=beanMandato.getIdPersona()%>" />
		<input type='hidden' name="idCuenta" id="idCuenta" value="<%=beanMandato.getIdCuenta()%>" />
		<input type='hidden' name="idMandato" id="idMandato" value="<%=beanMandato.getIdMandato()%>" />
		<input type='hidden' name="idAnexo" id="idAnexo" value="" />
		<html:hidden property="idFichero" value=""/>
		<html:hidden property="actionModal" value=""/>

	</form>	
	
	<html:form action="/INF_InformesGenericos" method="post"  target="submitArea">
		<html:hidden property="idInstitucion" styleId="idInstitucion" value = "<%=beanMandato.getIdInstitucion()%>"/>
		<html:hidden property="idTipoInforme"  styleId="idTipoInforme"/>
		<html:hidden property="enviar"   styleId="enviar" value="1"/> 
		<html:hidden property="descargar"  styleId="descargar" value="1"/>
		<html:hidden property="datosInforme"  styleId="datosInforme"/>
		<html:hidden property="modo"  styleId="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal' id='actionModal'>
	</html:form>

	<form name="DefinirEnviosForm" id="DefinirEnviosForm"  method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD" value="">
	</form>
	
	<siga:Table 
	   	name="listadoAnexos"
	   	border="2"
	   	columnNames="censo.fichaCliente.bancos.mandatos.anexos.tipo, 
	   		censo.fichaCliente.bancos.mandatos.anexos.fechaCreacion, 
	   		censo.fichaCliente.bancos.mandatos.anexos.origen, 
	   		censo.fichaCliente.bancos.mandatos.anexos.fechaFirma,"
	   	columnSizes="10,15,45,15,15">		
	   	
<%
		if (vListadoAnexos==null || vListadoAnexos.size()==0) {
%>
	 		<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>			
	 		
<%
		} else {		
			for (int j=0; j < vListadoAnexos.size(); j++) {
				int i=j+1;
				CenAnexosCuentasBancariasBean anexoBean = (CenAnexosCuentasBancariasBean)vListadoAnexos.get(j);
				
				// JPT: 
				// - Si tiene fecha de uso, pero no tiene fecha de firma:
				// 		a. No se puede modificar la informacion del mandato
				//		b. Se pueden modificar los ficheros de la firma de mandatos y anexos
				// - Si tiene fecha de firma
				// 		a. No se puede modificar la informacion del mandato
				//		b. No se puede modificar los ficheros de la firma de mandatos y anexos que ya estan firmados y tienen un documento asignado
				//		c. Solo Se puede modificar el documento asignado (no se puede moficiar la fecha, lugar, origen y descripcion de la firma), para los ficheros firmados sin documento asignado
				//		d. Se pueden modificar todos los datos de los ficheros de mandatos y anexos que no estan firmados	
				String botonesAnexo = "C";				
				String sTipoAnexo = UtilidadesString.getMensajeIdioma(usuario, "censo.fichaCliente.bancos.mandatos.anexos.tipo.mandato");
				if (anexoBean.getIdAnexo()!=null && !anexoBean.getIdAnexo().equals("")) {
					sTipoAnexo = UtilidadesString.getMensajeIdioma(usuario, "censo.fichaCliente.bancos.mandatos.anexos.tipo.anexo");
					if (modoMandato.equals("editar")) {
						if (anexoBean.getFirmaFecha()!=null && !anexoBean.getFirmaFecha().equals("") && (anexoBean.getIdFicheroFirma()==null || anexoBean.getIdFicheroFirma().equals(""))) {
							botonesAnexo += ",E";
							
						} else if (anexoBean.getFirmaFecha()==null || anexoBean.getFirmaFecha().equals("")) {
							botonesAnexo += ",E,B";
						}
					}
					
				} else {
					if (modoMandato.equals("editar") && (anexoBean.getFirmaFecha()==null || anexoBean.getFirmaFecha().equals("") || anexoBean.getIdFicheroFirma()==null || anexoBean.getIdFicheroFirma().equals(""))) {
						botonesAnexo += ",E";
					}
				}		
				
				String sFirmaFecha = "";
				if (anexoBean.getFirmaFecha()!=null && !anexoBean.getFirmaFecha().equals("") &&
					anexoBean.getFirmaFechaHora()!=null && !anexoBean.getFirmaFechaHora().equals("") &&
					anexoBean.getFirmaFechaMinutos()!=null && !anexoBean.getFirmaFechaMinutos().equals("")) {
					sFirmaFecha = anexoBean.getFirmaFecha() + " " + anexoBean.getFirmaFechaHora() + ":" + anexoBean.getFirmaFechaMinutos();
				}
				
				FilaExtElement[] elementos = new FilaExtElement[1];
				if(anexoBean.getIdFicheroFirma()!=null && !anexoBean.getIdFicheroFirma().equals("")){
			 		elementos[0] = new FilaExtElement("download", "descargar", SIGAConstants.ACCESS_READ);
				}else{
					elementos[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
				}
%>
				<siga:FilaConIconos 
					fila="<%=String.valueOf(i)%>" 
					botones="<%=botonesAnexo%>"
					elementos='<%=elementos%>'
					pintarEspacio="no"
					clase="listaNonEdit">
					
					<td>
						<input type='hidden' id='ocultoIdAnexo_<%=String.valueOf(i)%>' name='ocultoIdAnexo_<%=String.valueOf(i)%>' value='<%=anexoBean.getIdAnexo()%>'/>
						<input type='hidden' id='ocultoIdFichero_<%=String.valueOf(i)%>' name='ocultoIdFichero_<%=String.valueOf(i)%>' value='<%=anexoBean.getIdFicheroFirma()%>'/>
						<%=sTipoAnexo%>						
					</td>		
					<td><%=UtilidadesString.mostrarDatoJSP(anexoBean.getFechaCreacion())%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(anexoBean.getOrigen())%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(sFirmaFecha)%></td>	
				</siga:FilaConIconos>
<%												
			}
		}
%>
	</siga:Table>	   	
	   	
	<!-- SCRIPTS LOCALES -->
	<script>
		function editar(fila) {
			var idAnexo = document.getElementById('ocultoIdAnexo_' + fila).value;
			
			if (idAnexo == null || idAnexo == "") {			
		   		document.MandatosCuentasBancariasForm.modo.value = "editarFicheroMandatoCuentaBancaria";
		   		
				var resultado = ventaModalGeneral(document.MandatosCuentasBancariasForm.name, "M");				
				if (resultado=='MODIFICADO') {
					alert(unescape("<siga:Idioma key='messages.updated.success'/>"),"success");
					refrescarLocal();
				}
		    	
			} else {
		   		document.AnexosCuentasBancariasForm.idAnexo.value = idAnexo;
		   		document.AnexosCuentasBancariasForm.modo.value = "editarFicheroAnexoCuentaBancaria";
		   		
				var resultado = ventaModalGeneral(document.AnexosCuentasBancariasForm.name, "M");				
				if (resultado=='MODIFICADO') {
					alert(unescape("<siga:Idioma key='messages.updated.success'/>"),"success");
					refrescarLocal();
				}
			}
		}
		
		function consultar(fila) {
			var idAnexo = document.getElementById('ocultoIdAnexo_' + fila).value;
			
			if (idAnexo == null || idAnexo == "") {			
		   		document.MandatosCuentasBancariasForm.modo.value = "verFicheroMandatoCuentaBancaria";
				
		   		ventaModalGeneral(document.MandatosCuentasBancariasForm.name, "M");					    	
		    	
			} else {
		   		document.AnexosCuentasBancariasForm.idAnexo.value = idAnexo;
		   		document.AnexosCuentasBancariasForm.modo.value = "verFicheroAnexoCuentaBancaria";
		   		
		   		ventaModalGeneral(document.AnexosCuentasBancariasForm.name, "M");
			}			
		}
		
		function borrar(fila) {
			var idAnexo = document.getElementById('ocultoIdAnexo_' + fila).value;
			
			if (idAnexo == null || idAnexo == "") {			
				// JPT: No se pueden borrar los ficheros del mandato
				var mensaje = "<siga:Idioma key="messages.deleted.error"/>";
				alert(mensaje);
				fin();
			    return false;
				    
			} else {
		   		document.AnexosCuentasBancariasForm.idAnexo.value = idAnexo;
		   		document.AnexosCuentasBancariasForm.modo.value = "borrarFirmaAnexoCuentaBancaria";
		   		document.AnexosCuentasBancariasForm.target = "submitArea";
		    	document.AnexosCuentasBancariasForm.submit();
			}			
		}	
		
		function accionNuevo() {
			document.AnexosCuentasBancariasForm.idAnexo.value = "";
		   	document.AnexosCuentasBancariasForm.modo.value = "nuevoFicheroAnexoCuentaBancaria";
		   	
			var resultado = ventaModalGeneral(document.AnexosCuentasBancariasForm.name, "M");				
			if (resultado=='MODIFICADO') {
				alert(unescape("<siga:Idioma key='messages.inserted.success'/>"),"success");			
				refrescarLocal();
			}
		}		
		
		function refrescarLocal() {
			document.MandatosCuentasBancariasForm.target = "_self";
			document.MandatosCuentasBancariasForm.modo.value = "ficherosMandatoCuentaBancaria";		
			document.MandatosCuentasBancariasForm.submit();	
		}	
		
		
		function comunicar(fila) {
			var idAnexo = document.getElementById('ocultoIdAnexo_' + fila).value;
			
			var idInstitucion = document.MandatosCuentasBancariasForm.idInstitucion.value;
			var idPersona = document.MandatosCuentasBancariasForm.idPersona.value;
			var idCuenta = document.MandatosCuentasBancariasForm.idCuenta.value;
			var idMandato = document.MandatosCuentasBancariasForm.idMandato.value;		
			
		   	datos = "idInstitucion=="+idInstitucion + "##idPersona==" +idPersona+ "##idCuenta==" +idCuenta+"##idMandato=="+idMandato; 
			if (idAnexo == null || idAnexo == "") {
				datos += "##idTipoInforme==OSEPA"
				document.InformesGenericosForm.idTipoInforme.value = 'OSEPA'; 
			} else {
				datos += "##idAnexo=="+idAnexo+"##idTipoInforme==ASEPA"
				
				document.InformesGenericosForm.idTipoInforme.value = 'ASEPA';
			}
		   	document.InformesGenericosForm.datosInforme.value=datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		   	if (arrayResultado==undefined||arrayResultado[0]==undefined){
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
			
			
		}
		
		
		function descargar(fila) {
			var idAnexo = document.getElementById('ocultoIdAnexo_' + fila).value;
			var idFichero = document.getElementById('ocultoIdFichero_' + fila).value;
			if (idAnexo == null || idAnexo == "") {
				document.forms['MandatosCuentasBancariasForm'].idFichero.value = idFichero;
				document.forms['MandatosCuentasBancariasForm'].modo.value = "downloadFichero";
				document.forms['MandatosCuentasBancariasForm'].submit();
			} else {
				document.forms['AnexosCuentasBancariasForm'].idFichero.value = idFichero;
				document.forms['AnexosCuentasBancariasForm'].idAnexo.value = idAnexo;
				document.forms['AnexosCuentasBancariasForm'].modo.value = "downloadFichero";
				document.forms['AnexosCuentasBancariasForm'].submit();
				
			}
		}
		
		
		
	</script>
	
	<siga:ConjBotonesAccion botones='<%=botones%>'/>
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>