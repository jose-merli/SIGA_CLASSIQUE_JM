<!-- paginaAyuda.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% String app=request.getContextPath(); %>

<html>
	<head>
	  
	    <title>Pagina Ayuda</title>
	    
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	    
	    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	</head>

	<body class ='tablaCentralCampos'>

	<BR>
	 
	 <table ALIGN="center" WIDTH="94%" border="0">
	 <tr>
		 <td WIDTH="10px"> &nbsp; </td>
		 <td>
			 <p class="labelText" style="FONT-SIZE: 14px">NORMATIVA DE USO</p>
		 </td>
	 </tr>
	 </table>

	 <hr size="1" align="center" width="97%" noshade="true" COLOR="black">
	 <div style="overflow:auto; height:525px;">
			<ol class="labelText">
			  <LI > La sentencia debe ir etiquetada con los siguientes etiquetas para indicar el comienzo y fin de las diferentes partes de la sentencia:
			   <ul class="labelText" type="square">
			       <LI ><B style="FONT-SIZE: 14px;">&LT;SELECT&GT; &LT;/SELECT&GT%%</B>, donde se indica la clausula SELECT.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">&LTFROM&GT; &LT;/FROM&GT;</B>, donde se indica la clausula FROM de la sentencia principal.</LI>			       
			       <LI ><B style="FONT-SIZE: 14px;">&LTWHERE&GT; &LT;/WHERE&GT;</B>, donde se indica la clausula WHERE de la sentencia principal.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">&LTUNION&GT; &LT;/UNION&GT;</B>, donde se indica la clausula UNION de la sentencia principal.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">&LTUNIONALL&GT; &LT;/UNIONALL&GT;</B>, donde se indica la clausula UNION ALL de la sentencia principal.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">&LTGROUPBY&GT; &LT;/GROUPBY&GT;</B>, donde se indica la clausula GROUP BY de la sentencia principal.</LI>   
       		       <LI ><B style="FONT-SIZE: 14px;">&LTORDERBY&GT; &LT;/ORDERBY&GT;</B>, donde se indica la clausula ORDER BY de la sentencia principal.</LI>   
			    </ul> 
			  </LI>
		  <BR>
			  <LI > Los campos de salida deben tener ALIAS y con el siguiente formato: <B style="FONT-SIZE: 14px;">CAMPO AS "ALIAS_CAMPO"</B>.</LI>
	      <BR>
		  <BR>
			  <LI > Los criterios din�micos deben ir delimitados por los delimitadores <B style="FONT-SIZE: 14px;">%%OPERADOR%% %%CRITERIO%%</B>, CRITERIO es el formato del campo.</LI>
		  <BR>
		  <BR>
			  <LI > Los formatos de criterios din�micos admitidos son: 
			    <ul class="labelText" type="square">
			       <LI ><B style="FONT-SIZE: 14px;">%%NUMERO%%</B>, para campos num�ricos.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">%%TEXTO%%</B>, para campos alfanum�ricos.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">%%FECHA%%</B>, para fechas.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">%%MULTIVALOR@CONSULTA%%</B>, para campos multivalor. CONSULTA es una consulta con campos de salida ID y DESCRIPCION que no debe ir encerrada entre par�ntesis.</LI>   
			    </ul> 
			  </LI>
		  <BR>
		   	<LI>Si se utiliza como criterio din�mico <B style="FONT-SIZE: 14px;">%%TEXTO%%</B>
		   	para los campos alfanum�ricos.Recuerde que puede usar los comodines <B style="FONT-SIZE: 14px;">%</B> Y <B style="FONT-SIZE: 14px;">_</B> tanto en la consulta como en los criterios din�micos con el operador <B style="FONT-SIZE: 14px;">COMO</B>.
		   	Si queremos hacer una b�squeda aproximada por nombre de colegiado ya que se desconoce su nombre exacto utilizaremos el comod�n  <B style="FONT-SIZE: 14px;">%NOMBRE COLEGIADO%</B> y la consulta nos devolver� los resultados de esa cadena por ejemplo
		   	<B style="FONT-SIZE: 14px;">%JOSE%</B> nos devolver� Maria Jose, Jose Maria.
 
		  <BR>
		  	<BR>
		  		<LI>Para que los criterios din�micos del campo de texto sean independientes de may�sculas y min�sculas puede
		  		 utilizar la funci�n <B style="FONT-SIZE: 14px;">UPPER</B> delante de los campos usados en la consulta.
		  		 Ejemplo: Si queremos buscar din�micamente por el nombre de un colegiado para que nos ignore las may�sculas y min�sculas haremos <B style="FONT-SIZE: 14px;">UPPER(Cen_persona.Nombre) </B>
		  		 <B style="FONT-SIZE: 14px;">%%OPERADOR%%UPPER(%%TEXTO%%).</B>

		  	<BR>
		  <BR>
			  <LI > Los campos de salida correspondientes a descripciones 'multi-idioma' deben tener el siguiente formato:<BR>
			        &nbsp;&nbsp;&nbsp;&nbsp;<B style="FONT-SIZE: 14px;">F_SIGA_GETRECURSO(</B>campo <B>, %%IDIOMA%%)</B> as "alias_campo"
			   </LI>
		   <BR> 
		   <BR>
			 <LI > La cl�usula ORDER BY no se comprueba al guardar, por lo que podr�a ser causa de error en la ejecuci�n.</LI>
		   <BR>   
		   <BR>
			 <LI > La consulta, una vez construida, no se debe finalizar con ";".</LI>
		   <BR> 
		   <BR>
			 <LI > Cuando se utiliza <B style="FONT-SIZE: 14px;">%%FECHA%%</B> como criterio din�micos, se puede utilizar la funci�n <B style="FONT-SIZE: 14px;">TRUNC</B> cuando se desee hacer las comparaciones sin horas/min/seg.</LI>
		   <BR> 
		    <BR>
			 <LI > Puede utilizar la funci�n F_SIGA_GETDIRECCION (IDINSTITUCION, IDPERSONA, TIPOENVIO) para obtener una direcci�n a donde se har�n los env�os. 
			       Esta direcci�n ser� siempre la preferente por tipo de env�o, si no hay ninguna configurada, devolver� la de tipo correo, si no la de despacho y si no cualquier direcci�n dada de alta.
                   <B style="FONT-SIZE: 14px;">TIPOENVIO</B> puede tomar los siguientes valores:
			       <ul class="labelText" type="square">
			       <LI ><B style="FONT-SIZE: 14px;">1</B>, si el tipo de env�o es por correo electr�nico.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">2</B>, si el tipo de env�o es por correo ordinario.</LI>
			       <LI ><B style="FONT-SIZE: 14px;">3</B>, si el tipo de env�o es por fax.</LI>
			          
			    </ul> 
			 </LI>
		   <br>
		   <BR> 
			 <LI > 
			 	En el caso en que las consultas sean de Env�os a Grupos, la condici�n del tipo de direcci�n se a�ade autom�ticamente y cuando se ejecuta la consulta se pide el tipo de env�o.
			 </LI>
		   <br>		   
		   <br>
			 <LI > Las consultas de tipo "Facturaci�n" o "Env�o a Grupos" tienen las siguientes restricciones:
			 	<ul class="labelText" type="square">
		 			<LI ><B style="FONT-SIZE: 14px;">No puede incorporar solicitudes de criterios din�micos por interfaz.</B></LI>   
			 		<LI ><B style="FONT-SIZE: 14px;">Para las consultas de tipo "Facturaci�n" los siguientes campos son obligatorios: IDINSTITUCION , IDPERSONA</B>.</LI>   
			 		<LI ><B style="FONT-SIZE: 14px;">Para las consultas de tipo "Env�o a Grupos": IDINSTITUCION, IDPERSONA, CODIGOPOSTAL, CORREOELECTRONICO,
			  DOMICILIO, MOVIL, FAX1, FAX1, IDPAIS, IDPROVINCIA, IDPOBLACION.
			  		</B></LI>   
			 	</ul> 
			 </LI>	   
		     <br>
			 <LI> Las consultas de tipo "Env�o a Grupos" tiene las siguientes restricciones:
			 	<ul class="labelText" type="square">
		 			<LI ><B style="FONT-SIZE: 14px;">Las consultas listas para env�os han de llevar la tabla CEN_CLIENTE y CEN_DIRECCIONES sin alias.</LI>   
			 		<LI ><B style="FONT-SIZE: 14px;">NO se debe a�adir condiciones para filtrar la direcci�n, ya que eso se hace autom�ticamente.</LI>   
			 	</ul> 
			 </LI>
			 <br>
			 <LI>Se han a�adido los nuevos operadores =,!=,IS NULL,LIKE al formato de los criterios TEXTO. A estos operadores se a�aden >,>=,<,<= cuando el formato sea NUMERO, FECHA o MULTIVALOR. Estos operadores funcionan del mismo modo que %%OPERADOR%%
			 </LI>
<br>
			 <LI>Se ha a�adido la cl�usula DEFECTO para los operadores. La notaci�n ser�  %%OPERADOR%% %%CRITERIO%% DEFECTO "VALOR". Estos valores aparecer�n por defecto en la pantalla de criterios din�micos. Si el criterio es FECHA el formato ser� dd/mm/yyyy y tambi�n acepta SYSDATE para la fecha actual.
			 </LI>
<br>
			 <LI>Se ha a�adido la cl�usula NULO para los operadores. La notaci�n ser�  %%OPERADOR%% %%CRITERIO%% NULO "VALOR". Donde valor es "SI" y "NO". En caso que no exista ser� "NO". En el caso de que exista la cl�usula DEFECTO debe ir siempre detr�s de ella. En caso de que admita nulos no ser� obligatorio dar ning�n valor al criterio din�mico en la pantalla de criterios din�micos y filtrar� como IS NULL
			 </LI>
			 
		</ol>
	</div>
  </BODY>
</html>



