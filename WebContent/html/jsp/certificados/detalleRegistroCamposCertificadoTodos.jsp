<!-- detalleRegistroCamposCertificadoTodos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
	request.removeAttribute("nuevo");
   
	Hashtable htDatos = new Hashtable();
	Hashtable hfmts = new Hashtable();
	Hashtable hcampo = new Hashtable();
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
	Enumeration e=datos.elements();
      if(e.hasMoreElements()){
    	  htDatos = (Hashtable) e.nextElement();
    	}
    	//Mensajes
  			String mensaje = (String)request.getAttribute("mensaje");
      //tablas de campos
      if(e.hasMoreElements()){
    	  hcampo = (Hashtable) e.nextElement();
      }
      if(e.hasMoreElements()){
    	  hfmts = (Hashtable) e.nextElement();
    	}
      String botones = bEditable ? "Y,C" : "C";

      /* int numTablas = hcampo.size();

      Enumeration ek = hcampo.keys();  
      Object obj;  
     while (ek.hasMoreElements()) { //tablas  
            obj = ek.nextElement();  
            Vector formato= (Vector)hfmts.get(obj);  
	        Enumeration enumera=formato.elements();
	        while(enumera.hasMoreElements()){
	        	 Hashtable hsfmto = (Hashtable) enumera.nextElement();
	        	 String titulos = (String)hsfmto.get("DESCRIPCION");
	        }
	        Vector vCampos = (Vector)hcampo.get(obj);  
	        Enumeration enumeration=vCampos.elements();
	        while(enumeration.hasMoreElements()){
	        	 Hashtable hscertificado = (Hashtable) enumeration.nextElement();
	        	 String nombre = (String)hscertificado.get("DESCRIPCION");
	        }  
      }	*/
       	 	
    	 	
    	 	
    	 	
      
      
      
//	Hashtable htDatos = (Hashtable)datos.elementAt(0);
//	Vector vfmts = (Vector)datos.get(1);
//	Vector vcampos = (Vector)datos.get(2);
	String sIdCampo=(String)htDatos.get("idCampo");
	String sDescCampo=(String)htDatos.get("descCampo");
	String sIdFormato=(String)htDatos.get("idFormato");
	String sDescFormato=(String)htDatos.get("descFormato");

	String sTipoCampo=(String)htDatos.get("tipoCampo");
	String sCapturarDatos=(String)htDatos.get("capturarDatos");
	String sValor=(String)htDatos.get("valor");
	
	String sIdInstitucion=(String)htDatos.get("idInstitucion");
	String sIdTipoProducto=(String)htDatos.get("idTipoProducto");
	String sIdProducto=(String)htDatos.get("idProducto");
	String sIdProductoInstitucion=(String)htDatos.get("idProductoInstitucion");

%>	

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"type="text/javascript"></script>	
	<script src="<%=app%>/html/js/jquery-1.2.1.min.js" type="text/javascript"></script>
<script language='JavaScript'>

function marca(i,tipo, todos ) {
	var x =1;
	var ele;
	var primero = document.getElementsByName(tipo+"1")[i];
	if(primero.checked==false)
		todos.checked= true;
	if(primero.checked==true)
		todos.checked= false;
	do {
			 ele = document.getElementsByName(tipo+x)[i];
			 if(ele== undefined)
				 break;
			  
			if(todos.checked==false)
	  			ele.checked= false;
			else
	  			ele.checked= true;
			 x ++;
	} while (ele!= undefined);


}	


</script>



<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//-- Asociada al boton GuardarCerrar -->
			var values = new Array();

			function accionGuardarCerrar() 
			{
				sub();
				var values = new Array();
				$.each($("input[@type=radio][@checked]"), function() {
				  values.push($(this).val());
				  // or you can do something to the actual checked checkboxes by working directly with  'this'
				  // something like $(this).hide() (only something useful, probably) :P
				});
						
				MantenimientoCertificadosCamposForm.filasSelect.value=values;
				MantenimientoCertificadosCamposForm.submit();
				window.returnValue="MODIFICADO";
			}
			//-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.close();
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		
		<script>
	   			   
		</script>
	</head>

	<body class="tablaCentralCampos">

	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="certificados.campos.editar.literal"/>
				</td>
			</tr>
		</table>
				
<html:form action="/CER_Campos.do" method="POST" target="submitArea">

<siga:ConjCampos leyenda="certificados.mantenimiento.titulo">
<%
				String miModo = bNuevo ? "Insertar" : "Modificar";
%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				
				<html:hidden property="idInstitucion" value="<%=sIdInstitucion%>"/>
				<html:hidden property="idTipoProducto" value="<%=sIdTipoProducto%>"/>
				<html:hidden property="idProducto" value="<%=sIdProducto%>"/>
				<html:hidden property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
				
				<html:hidden property="idCampoCertificado" value="<%=sIdCampo%>"/>
				<html:hidden property="idFormato" value="<%=sIdFormato%>"/>
				<html:hidden property="tipoCampo" value="<%=sTipoCampo%>"/>
				<html:hidden property="filasSelect"/>



<% 

Enumeration ek = hcampo.keys();  
Object obj; 
int numTablas=0;
String nTabla = "";
if(mensaje!=null && !mensaje.trim().equalsIgnoreCase("")){
 botones = bEditable ? "C" : "C";	
%>
			<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="<%= mensaje%>"/></p>
				<br><br>
<%
}else{	
while (ek.hasMoreElements()) { //tablas  
    obj = ek.nextElement();  
    Vector formato= (Vector)hfmts.get(obj); 
    int numCol = formato.size();
    int tam = (int)80/(numCol+1);
    nTabla= "A"+nTabla;
    numTablas++;
    
%>

<fieldset class="legend">
<legend style="cursor:hand;"> 
<a onclick="ocultarDIV('tabla_<%=nTabla %>');">
<img id="tabla_<%=nTabla %>ImMas" src="<%=app%>/html/imagenes/simboloMas.gif"style="display:none">
<img id="tabla_<%=nTabla %>ImMenos" src="<%=app%>/html/imagenes/simboloMenos.gif"style="display:inline">
</a>
</legend>
<div style="display: inline" id="tabla_<%=nTabla %>">

<table border='1' width='98.43%' cellspacing='0' cellpadding='0'>
	<tr class = 'tableTitle'>
<%
	
 
    Enumeration enumera=formato.elements();
    %>
     
    <td align='center' width='20%'><siga:Idioma key="certificados.mantenimiento.literal.campo"/></td>
    <%   
    int n=0;
    String formatos[] = new String[10];
    String tipoCampo="";
    while(enumera.hasMoreElements()){
    	 
    	 Hashtable hsfmto = (Hashtable) enumera.nextElement();
    	 String titulos = (String)hsfmto.get("DESCRIPCION");
    	 formatos[n] = (String)hsfmto.get("ID");
    	 tipoCampo = (String)hsfmto.get("TIPO");
    	 n++;
    	 
		%>
		<td align='center' width='<%=tam%>%'><%=titulos %></td>
		<%    	 
    }
    %>
    <td align='center' width='<%=tam%>%'><siga:Idioma key="certificados.mantenimiento.literal.sinFrmt"/></td>
    </tr>
   	<tr class = 'tableTitle'>
    	 <td align='center' width='20%'></td>
    <%
    for(int m=0; m<n;m++){
    	%>
		<td align='center' width='<%=tam%>%'><input type="radio"
									name="todos_+<%=numTablas%>" value=""
									onclick="marca('<%=m %>','<%=tipoCampo %>',this)"></td>
		<% 
    }
    %>
    <td align='center' width='<%=tam%>%'><input type="radio"
									name="todos_+<%=numTablas%>" value=""
									onclick="marca('<%=n %>','<%=tipoCampo %>',this)"></td>
  </tr>
  </table>


<div style="overflow:scroll; overflow-x:hidden; height:200px; width:100%; padding:0" align="center">

<table id='tablaDatos<%=numTablas %>' name='tablaDatos<%=numTablas %>' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
    <%     
    Vector vCampos = (Vector)hcampo.get(obj);  
    Enumeration enumeration=vCampos.elements();
    int i=0;
    while(enumeration.hasMoreElements()){
    	 Hashtable hscertificado = (Hashtable) enumeration.nextElement();
    	 String nombre = (String)hscertificado.get("DESCRIPCION");
    	 String id = (String)hscertificado.get("ID");
    	 String tipo = (String)hscertificado.get("TIPO");
    	 String fila ="filaTablaPar";
    	 if((i+1)%2==0)
    	 	 fila = "filaTablaPar";
    	 else
    		 fila = "filaTablaImpar";
    	 i++;
    	 %>
    	   <tr class="<%=fila %>" >
    	   <td width='20%'><%=nombre %></td>
    	   <% for(int j=0; j<numCol;j++ ){ %>
    	   <td align='center'  width='<%=tam%>%'> <input type="radio" id="<%=tipo+i %>" name="<%=tipo+i %>" value="<%= formatos[j]+"#"+id %>"> 
    	   </td>
    	    <%} %>
    	    <td align='center'  width='<%=tam%>%'> <input type="radio" id="<%=tipo+i %>" name="<%=tipo+i %>" value="<%= "# #"+id %>">
    	   </tr>
    	 <%    	 

    }
    %>

    </table>
    </div>
    	</div>
</fieldset>
    <%
}
}
%>
</siga:ConjCampos>
			</html:form>
				
			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->

			<siga:ConjBotonesAccion botones="<%=botones%>" modal="M"/>
		
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>