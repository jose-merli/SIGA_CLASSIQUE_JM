create materialized view USCGAE.V_CENSO_LETRADOS_OOJJ
refresh force on demand
as
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) As Nombre,
       Upper(Cen_Persona.Apellidos1) As Apellido1,
       Upper(Cen_Persona.Apellidos2) As Apellido2,
       Cen_Persona.Nifcif As Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       Cen_Direcciones.Domicilio As Dir_Profesional,
       Decode(Cen_Direcciones.Idpoblacion,
              Null,
              Decode(Cen_Direcciones.Idpais,
                     Null,
                     Cen_Direcciones.Poblacionextranjera,
                     191,
                     Cen_Direcciones.Poblacionextranjera,
                     Decode(Cen_Direcciones.Poblacionextranjera,
                            Null,
                            '',
                            Cen_Direcciones.Poblacionextranjera || '-' ||
                            (Select Pais.Nombre
                               From Cen_Pais Pais
                              Where Pais.Idpais = Cen_Direcciones.Idpais))),
              (Select Pob.Nombre
                 From Cen_Poblaciones Pob
                Where Pob.Idpoblacion = Cen_Direcciones.Idpoblacion)) As Poblacion,
       Cen_Direcciones.Idpoblacion,
       Cen_Direcciones.Idprovincia,
       Cen_Direcciones.Codigopostal As Cod_Postal,
       Cen_Direcciones.Telefono1 As Telefono,
       Cen_Direcciones.Fax1 As Fax,
       Cen_Direcciones.Movil As Movil,
       Cen_Direcciones.Correoelectronico As Mail,
       Cen_Persona.Sexo,
       Cen_Direcciones.Idpais
  From Cen_Persona, Cen_Direcciones, Cen_Cliente, Cen_Direccion_Tipodireccion
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Cen_Cliente.Idpersona = Cen_Direcciones.Idpersona
   And Cen_Cliente.Idinstitucion = Cen_Direcciones.Idinstitucion
   And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
   And Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
   And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
   And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
   And Cen_Direcciones.Iddireccion =
       (Select Max(Cen_Direcciones.Iddireccion)
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And Cen_Direcciones.Fechabaja Is Null
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion In (4, 7)
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Fechainicio is not null 
           And Fechafin is not Null
           And Trunc(Sysdate) Between Trunc(Fechainicio) And Trunc(Fechafin))
   And Cen_Cliente.Idpersona In --De momento solo enviamos Ejercientes
       (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado = 20)
Union All
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) Nombre,
       Upper(Cen_Persona.Apellidos1) Apellidos1,
       Upper(Cen_Persona.Apellidos2) Apellidos2,
       Cen_Persona.Nifcif Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       '' Dir_Profesional,
       '' Poblacion,
       '' Idpoblacion,
       '' Idprovincia,
       '' Cod_Postal,
       '' Telefono,
       '' Fax,
       '' Movil,
       '' Mail,
       '' Sexo,
       '' Idpais
  From Cen_Persona, Cen_Cliente
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Not Exists
 (Select 1
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion In (4, 7)
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Fechainicio is not null 
           And Fechafin is not Null
           And Trunc(Sysdate) Between Trunc(Fechainicio) And Trunc(Fechafin))
   And Cen_Persona.Idpersona In --De momento solo enviamos Ejercientes
       (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado = 20)
