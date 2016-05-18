create materialized view V_CENSO_LETRADOS
Tablespace TS_SIGA_CENSO
refresh force on demand
as
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) As Nombre,
       Upper(Cen_Persona.Apellidos1) As Apellido1,
       Upper(Cen_Persona.Apellidos2) As Apellido2,
       Cen_Persona.Nifcif As Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       Cen_Direcciones.Domicilio As Dir_Profesional,
       Nvl((Select Cen_Poblaciones.Nombre
             From Cen_Poblaciones
            Where Cen_Direcciones.Idpoblacion = Cen_Poblaciones.Idpoblacion),
           Cen_Direcciones.Poblacionextranjera) ||
       Decode(Cen_Direcciones.Idpais, Null, Null, 191, Null, '-') As Poblacion,
       Cen_Direcciones.Idpoblacion,
       Cen_Direcciones.Idprovincia,
       Nvl((Select Cen_Provincias.Nombre
             From Cen_Provincias
            Where Cen_Direcciones.Idprovincia = Cen_Provincias.Idprovincia),
           '') As Provincia,
       Cen_Direcciones.Codigopostal As Cod_Postal,
       Cen_Direcciones.Telefono1 As Telefono,
       Cen_Direcciones.Fax1 As Fax,
       Cen_Direcciones.Movil As Movil,
       Cen_Direcciones.Correoelectronico As Mail,
       Cen_Persona.Sexo,
       Cen_Direcciones.Idpais,
       Nvl((Select f_Siga_Getrecurso(Cen_Pais.Nombre, 1)
             From Cen_Pais
            Where Cen_Direcciones.Idpais = Cen_Pais.Idpais),
           '') Pais,
       Greatest(Cen_Persona.Fechamodificacion, Cen_Direcciones.Fechamodificacion) As Fechamodificacion
  From Cen_Persona, Cen_Direcciones, Cen_Cliente
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Cen_Cliente.Idpersona = Cen_Direcciones.Idpersona
   And Cen_Cliente.Idinstitucion = Cen_Direcciones.Idinstitucion
   And Cen_Direcciones.Iddireccion =
       (Select Max(Cen_Direcciones.Iddireccion)
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 3
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And '0' = (Select Max(Nvl(Cli.Noaparecerredabogacia, '0'))
                From Cen_Cliente Cli
               Where Cli.Idpersona = Cen_Cliente.Idpersona) -- Solo se pone direccion cuando no hay check LOPD activo
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion = 4
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And (Fechainicio Is Not Null Or Fechafin Is Not Null)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Exists
 (Select d.Idpersona
          From Cen_Datoscolegialesestado d, Cen_Colegiado Col
         Where Col.Idinstitucion = d.Idinstitucion
           And Col.Idpersona = d.Idpersona
           And d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Est.Fechaestado)
                                  From Cen_Datoscolegialesestado Est
                                 Where Est.Idinstitucion = d.Idinstitucion
                                   And Est.Idpersona = d.Idpersona
                                   And Trunc(Est.Fechaestado) <= Trunc(Sysdate))
           And d.Idestado In (10, 20)
           And Col.Comunitario = '0')
Union All
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) As Nombre,
       Upper(Cen_Persona.Apellidos1) As Apellido1,
       Upper(Cen_Persona.Apellidos2) As Apellido2,
       Cen_Persona.Nifcif Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       '' Dir_Profesional,
       '' Poblacion,
       '' Idpoblacion,
       '' Idprovincia,
       '' Provincia,
       '' Cod_Postal,
       '' Telefono,
       '' Fax,
       '' Movil,
       '' Mail,
       '' Sexo,
       '' Idpais,
       '' Pais,
       Cen_Persona.Fechamodificacion
  From Cen_Persona, Cen_Cliente
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And (Not Exists (Select 1
                      From Cen_Direcciones, Cen_Direccion_Tipodireccion
                     Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
                       And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
                       And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
                       And Cen_Direccion_Tipodireccion.Idtipodireccion = 3
                       And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
                       And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
                       And Cen_Direcciones.Fechabaja Is Null) Or
        '1' = (Select Max(Nvl(Cli.Noaparecerredabogacia, '0'))
                            From Cen_Cliente Cli
                           Where Cli.Idpersona = Cen_Cliente.Idpersona)) -- Solo se pone direccion cuando no hay check LOPD activo
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion = 4
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And (Fechainicio Is Not Null Or Fechafin Is Not Null)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Exists
 (Select d.Idpersona
          From Cen_Datoscolegialesestado d, Cen_Colegiado Col
         Where Col.Idinstitucion = d.Idinstitucion
           And Col.Idpersona = d.Idpersona
           And d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Est.Fechaestado)
                                  From Cen_Datoscolegialesestado Est
                                 Where Est.Idinstitucion = d.Idinstitucion
                                   And Est.Idpersona = d.Idpersona
                                   And Trunc(Est.Fechaestado) <= Trunc(Sysdate))
           And d.Idestado In (10, 20)
           And Col.Comunitario = '0')