<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page isErrorPage = "true" %>

<html>
<head>
<title>Tratamiento de errores</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
<% if (exception != null) { %>
<blockquote>
  <p><strong>La excepci&oacute;n causante del error ha sido:</strong></p>
  <p><% exception.printStackTrace(new java.io.PrintWriter(out)); %></p>
</blockquote>
<% } // if (exception != null) %>
<ul>
  <li><strong>Atributos del objeto Request:</strong> </li>
    <blockquote> 
      <p>
	  	<%
        java.util.Enumeration enumer;
        String atributo;
		enumer = request.getAttributeNames(); // Leemos todos los atributos del request
        while (enumer.hasMoreElements()) {
	        atributo = (String) enumer.nextElement();
		%>
	        <%=atributo%> = <%=request.getAttribute(atributo)%><br>
		<%
        } // del while
		%>
	  </p>
    </blockquote>
  </li>
  <li><strong>Par&aacute;metros recibidos:</strong> </li>
    <blockquote> 
      <p>
	  	<%
        String parametro;
		enumer = request.getParameterNames(); // Leemos todos los atributos del request
        while (enumer.hasMoreElements()) {
	        parametro = (String) enumer.nextElement();
		%>
	        <%=parametro%> = <%=request.getParameter(parametro)%><br>
		<%
        } // del while
		%>
	  </p>
    </blockquote>
  </li>
  <li><strong>Sesi&oacute;n:</strong> </li>
    <blockquote> 
      <p>
	  	<%
		session = request.getSession(true); // Creamos la sesion, si no existe
        String elemento;
		%>
        Sesion activa desde: <%=new java.util.Date(session.getCreationTime())%><br>
        Ultimo acceso: <%=new java.util.Date(session.getLastAccessedTime())%><br>
		<br>
		Contenido:<br><br>
		<%
		enumer = session.getAttributeNames();
        while (enumer.hasMoreElements()) {
	        elemento = (String) enumer.nextElement();
		%>
	        <%=elemento%> = <%=session.getAttribute(elemento)%><br>
		<%
        } // del while
		%>
	  </p>
    </blockquote>
  </li>
 </ul>
</body>
</html>