<!-- pestanaPlantillas.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoProducto = (String)request.getAttribute("idTipoProducto");
	String sIdProducto = (String)request.getAttribute("idProducto");
	String sIdProductoInstitucion = (String)request.getAttribute("idProductoInstitucion");
	
	String sCertificado=(String)request.getAttribute("certificado");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descCertificado = (String)request.getAttribute("descripcionCertificado");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			<!-- Asociada al boton Volver -->
			function accionVolver()
			{
				MantenimientoCertificadosPlantillasForm.action = "/SIGA/CER_MantenimientoCertificados.do";
				MantenimientoCertificadosPlantillasForm.modo.value="abrirConParametros";
				MantenimientoCertificadosPlantillasForm.submit();
			}
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo()
			{
				MantenimientoCertificadosPlantillasForm.action = "/SIGA/CER_Plantillas.do";
				MantenimientoCertificadosPlantillasForm.modo.value="nuevo";
				var resultado=ventaModalGeneral("MantenimientoCertificadosPlantillasForm","P");
				
				if (resultado=="MODIFICADO")
				{
					parent.buscar();
				}
			}

			function refrescarLocal()
			{
				parent.buscar();
			}

			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var i, j;
				for (i = 0; i < 2; i++) 
				{
  						var tabla;
  						tabla = document.getElementById('tabladatos');
  						if (i == 0)
  						{
    						var flag = true;
    						j = 1;
    						while (flag) 
    						{
      							var aux = 'oculto' + fila + '_' + j;
      							var oculto = document.getElementById(aux);
      							if (oculto == null)  
      							{ 
      								flag = false; 
      							}
     							else 
     							{ 
     								datos.value = datos.value + oculto.value + ','; 
     							}
      							j++;
    						}
    						datos.value = datos.value + "%"
  						} 
  						else 
  						{ 
  							j = 2; 
  						}
  						if ((tabla.rows[fila].cells)[i].innerText == "") 
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
 						else
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
					
					document.forms[0].target="submitArea";
					document.forms[0].modo.value = "download";
					document.forms[0].submit();
					document.forms[0].target="mainWorkArea";
				}
			}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

	</head>

	<body class="detallePestanas">
		<html:form action="/CER_Plantillas.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value=""/>

			<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
			<html:hidden property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
			<html:hidden property="idProducto" value="<%=sIdProducto%>"/>
			<html:hidden property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
			
			<html:hidden property="certificado" value="<%=sCertificado%>"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

	<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo"  cellspacing="0">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="certificados.mantenimiento.literal.certificado"/>:&nbsp;<%=descCertificado%>
		</tr>
	</table>		

			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="certificados.mantenimiento.literal.plantilla,certificados.mantenimiento.literal.pordefecto,"
		   		  tamanoCol="72,15,13"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  modal="M">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
					String sBotones = bEditable ? "E,B" : "";
					
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		CerPlantillasBean bean = (CerPlantillasBean)vDatos.elementAt(i);
				  		
						FilaExtElement[] elems=new FilaExtElement[1];
						elems[0]=new FilaExtElement("download","download",SIGAConstants.ACCESS_READ);

%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" elementos='<%=elems%>' clase="listaNonEdit" visibleConsulta="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdTipoProducto()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getIdProducto()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getIdProductoInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=bean.getIdPlantilla()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=bean.getDescripcion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=bean.getPorDefecto()%>">
						
						<%=bean.getDescripcion()%>
					</td>
					<td>
<%
					String sPorDefecto = bean.getPorDefecto();
					
					if (sPorDefecto.equals("S"))
					{
%>
						<siga:Idioma key="general.yes"/>
<%
					}
					
					else
					{
%>
						<siga:Idioma key="general.no"/>
<%
					}
%>
					</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:TablaCabecerasFijas>

<%
		String sBotones2 = bEditable ? "V,N" : "V";
%>
		<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>