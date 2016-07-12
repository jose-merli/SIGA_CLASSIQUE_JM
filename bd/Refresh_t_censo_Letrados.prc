CREATE OR REPLACE Procedure Refresh_t_censo_Letrados Is
  -- Rellena la tabla homonima

  Cursor c_Letrados_Condireccion Is
    Select Per.Idpersona As Id_Letrado,
           Upper(Per.Nombre) As Nombre,
           Upper(Per.Apellidos1) As Apellido1,
           Upper(Per.Apellidos2) As Apellido2,
           Per.Nifcif As Num_Doc,
           Per.Idtipoidentificacion As Idtipoidentificacion,
           Per.Sexo,
           Greatest(Per.Fechamodificacion, Dir.Fechamodificacion) As Fechamodificacion,
           
           Dir.Domicilio As Dir_Profesional,
           Nvl((Select Cen_Poblaciones.Nombre
                 From Cen_Poblaciones
                Where Dir.Idpoblacion = Cen_Poblaciones.Idpoblacion),
               Dir.Poblacionextranjera) || Decode(Dir.Idpais, Null, Null, 191, Null, '-') As Poblacion,
           Dir.Idpoblacion,
           Dir.Idprovincia,
           Nvl((Select Cen_Provincias.Nombre
                 From Cen_Provincias
                Where Dir.Idprovincia = Cen_Provincias.Idprovincia),
               '') As Provincia,
           Dir.Codigopostal As Cod_Postal,
           Dir.Telefono1 As Telefono,
           Dir.Fax1 As Fax,
           Dir.Movil As Movil,
           Dir.Correoelectronico As Mail,
           Dir.Idpais,
           Nvl((Select f_Siga_Getrecurso(Cen_Pais.Nombre, 1)
                 From Cen_Pais
                Where Dir.Idpais = Cen_Pais.Idpais),
               '') Pais
      From Cen_Cliente Cli, Cen_Persona Per, Cen_Direcciones Dir
     Where Per.Idpersona = Cli.Idpersona
       And Cli.Idpersona = Dir.Idpersona
       And Cli.Idinstitucion = Dir.Idinstitucion
       And Dir.Iddireccion = (Select Max(Dir3.Iddireccion)
                                From Cen_Direcciones Dir3, Cen_Direccion_Tipodireccion Tip3
                               Where Dir3.Idinstitucion = Tip3.Idinstitucion
                                 And Dir3.Iddireccion = Tip3.Iddireccion
                                 And Dir3.Idpersona = Tip3.Idpersona
                                 And Tip3.Idtipodireccion = 3
                                 And Dir3.Idinstitucion = Cli.Idinstitucion
                                 And Dir3.Idpersona = Cli.Idpersona
                                 And Dir3.Fechabaja Is Null)
          
          --Letrados...
       And Cli.Idinstitucion = 2000
       And Cli.Letrado = '1'
          
          --que no esten fallecidos
       And Per.Fallecido = '0'
          
          --que sean Ejercientes o No ejercientes al menos en un colegio
       And Cli.Idpersona In
           (Select Col.Idpersona
              From Cen_Colegiado Col, Cen_Datoscolegialesestado Est
             Where Col.Idpersona = Est.Idpersona
               And Col.Idinstitucion = Est.Idinstitucion
               And Est.Idestado In ('10', '20')
                  --y no sean Inscritos
               And Col.Comunitario = '0'
               And Est.Fechaestado =
                   (Select Max(Est2.Fechaestado)
                      From Cen_Datoscolegialesestado Est2
                     Where Est2.Idpersona = Est.Idpersona
                       And Est2.Idinstitucion = Est.Idinstitucion
                       And Trunc(Est2.Fechaestado) <= Trunc(Sysdate)));

  Cursor c_Letrados_Sindireccion Is
    Select Per.Idpersona As Id_Letrado,
           Upper(Per.Nombre) As Nombre,
           Upper(Per.Apellidos1) As Apellido1,
           Upper(Per.Apellidos2) As Apellido2,
           Per.Nifcif As Num_Doc,
           Per.Idtipoidentificacion As Idtipoidentificacion,
           Per.Sexo As Sexo,
           Per.Fechamodificacion As Fechamodificacion,
           
           Null As Dir_Profesional,
           Null As Poblacion,
           Null As Idpoblacion,
           Null As Idprovincia,
           Null As Provincia,
           Null As Cod_Postal,
           Null As Telefono,
           Null As Fax,
           Null As Movil,
           Null As Mail,
           Null As Idpais,
           Null As Pais
      From Cen_Cliente Cli, Cen_Persona Per
     Where Per.Idpersona = Cli.Idpersona
       And Not Exists (Select Max(Dir3.Iddireccion)
                                From Cen_Direcciones Dir3, Cen_Direccion_Tipodireccion Tip3
                               Where Dir3.Idinstitucion = Tip3.Idinstitucion
                                 And Dir3.Iddireccion = Tip3.Iddireccion
                                 And Dir3.Idpersona = Tip3.Idpersona
                                 And Tip3.Idtipodireccion = 3
                                 And Dir3.Idinstitucion = Cli.Idinstitucion
                                 And Dir3.Idpersona = Cli.Idpersona
                                 And Dir3.Fechabaja Is Null)
          
          --Letrados...
       And Cli.Idinstitucion = 2000
       And Cli.Letrado = '1'
          
          --que no esten fallecidos
       And Per.Fallecido = '0'
          
          --que sean Ejercientes o No ejercientes al menos en un colegio
       And Cli.Idpersona In
           (Select Col.Idpersona
              From Cen_Colegiado Col, Cen_Datoscolegialesestado Est
             Where Col.Idpersona = Est.Idpersona
               And Col.Idinstitucion = Est.Idinstitucion
               And Est.Idestado In ('10', '20')
                  --y no sean Inscritos
               And Col.Comunitario = '0'
               And Est.Fechaestado =
                   (Select Max(Est2.Fechaestado)
                      From Cen_Datoscolegialesestado Est2
                     Where Est2.Idpersona = Est.Idpersona
                       And Est2.Idinstitucion = Est.Idinstitucion
                       And Trunc(Est2.Fechaestado) <= Trunc(Sysdate)));

  v_Tienesancion Varchar2(1);
  v_Lopdconsejo  Varchar2(1);
  v_Lopdcolegio  Varchar2(1);

  e_Tienesancion Exception;
  
  p_datoserror Varchar2(4000);
  v_fecha      Varchar2(20);
  
  n_antiguos_borrados  Number;
  n_insertados         Number;
  n_insertados_conlopd Number;
  n_insertados_sinlopd Number;
  n_insertados_sindir  Number;

Begin

  Select to_char(Sysdate, 'dd/mm/yyyy hh24:mi:ss') Into v_fecha From dual;
  Dbms_Output.Put_Line('INI: ' || v_fecha);
  
  n_insertados_conlopd := 0;
  n_insertados_sinlopd := 0;
  n_insertados_sindir  := 0;

  --por cada registro del cursor
  For Reg In c_Letrados_Condireccion Loop
    Begin
    
      --obteniendo Sanciones activas
      Begin
        Select 1
          Into v_Tienesancion
          From Cen_Sancion
         Where Idpersona = Reg.Id_letrado
           And Idtiposancion = 4
           And Nvl(Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And (Fechainicio Is Not Null Or Fechafin Is Not Null)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999'))
           And Rownum = 1;
      Exception
        When Others Then
          v_Tienesancion := 0;
      End;
    
      If v_Tienesancion = 1 Then
        Raise e_Tienesancion;
      End If;
    
      --obteniendo LOPD en Consejos
      Begin
        Select 1
          Into v_Lopdconsejo
          From Cen_Cliente Consejo
         Where Consejo.Idpersona = Reg.Id_letrado
           And Consejo.Letrado = '1'
           And Nvl(Consejo.Noaparecerredabogacia, '0') = '1'
           And Rownum = 1;
      Exception
        When Others Then
          v_Lopdconsejo := 0;
      End;
    
      --obteniendo LOPD en Colegios
      Begin
        Select 1
          Into v_Lopdcolegio
          From Cen_Colegiado Col, Cen_Cliente Cli, Cen_Datoscolegialesestado Est
         Where Col.Idpersona = Reg.Id_letrado
           And Col.Idpersona = Cli.Idpersona
           And Col.Idinstitucion = Cli.Idinstitucion
           And Col.Idpersona = Est.Idpersona
           And Col.Idinstitucion = Est.Idinstitucion
           And Est.Fechaestado =
               (Select Max(Est2.Fechaestado)
                  From Cen_Datoscolegialesestado Est2
                 Where Col.Idpersona = Est2.Idpersona
                   And Col.Idinstitucion = Est2.Idinstitucion
                   And Trunc(Est2.Fechaestado) <= Trunc(Sysdate))
           And Est.Idestado < 30
           And Col.Comunitario = '0'
           And Nvl(Cli.Noaparecerredabogacia, '0') = '1'
           And Rownum = 1;
      Exception
        When Others Then
          v_Lopdcolegio := 0;
      End;
    
      --insertando el registro de exportacion
      Begin
        If v_Lopdconsejo = 1 Or v_Lopdcolegio = 1 Then
          Insert Into t_censo_Letrados
            (Id_Letrado, Nombre, Apellido1, Apellido2, Num_Doc, Idtipoidentificacion, Sexo, Fechamodificacion)
          Values
            (Reg.Id_Letrado, Reg.Nombre, Reg.Apellido1, Reg.Apellido2, Reg.Num_Doc, Reg.Idtipoidentificacion, Reg.Sexo, Reg.Fechamodificacion);
          n_insertados_conlopd := n_insertados_conlopd + Sql%Rowcount;
        Else
          Insert Into t_censo_Letrados
            (Id_Letrado, Nombre, Apellido1, Apellido2, Num_Doc, Idtipoidentificacion, Sexo, Fechamodificacion, Dir_Profesional, Poblacion, Idpoblacion, Idprovincia, Provincia, Cod_Postal, Telefono, Fax, Movil, Mail, Idpais, Pais)
          Values
            (Reg.Id_Letrado, Reg.Nombre, Reg.Apellido1, Reg.Apellido2, Reg.Num_Doc, Reg.Idtipoidentificacion, Reg.Sexo, Reg.Fechamodificacion, Reg.Dir_Profesional, Reg.Poblacion, Reg.Idpoblacion, Reg.Idprovincia, Reg.Provincia, Reg.Cod_Postal, Reg.Telefono, Reg.Fax, Reg.Movil, Reg.Mail, Reg.Idpais, Reg.Pais);
          n_insertados_sinlopd := n_insertados_sinlopd + Sql%Rowcount;
        End If;
        
      Exception
        When Others Then
          p_datoserror := Sqlcode || ': ' || Sqlerrm || ' - ' || 'ERR> Al insertar el letrado ' || Reg.Id_Letrado;
          Raise;
      End;
    
      Commit;
    
    Exception
      When e_Tienesancion Then
        Null;
    End;
  End Loop;

  --por cada registro del cursor
  For Reg In c_Letrados_Sindireccion Loop
    Begin
    
      --obteniendo Sanciones activas
      Begin
        Select 1
          Into v_Tienesancion
          From Cen_Sancion
         Where Idpersona = Reg.Id_letrado
           And Idtiposancion = 4
           And Nvl(Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And (Fechainicio Is Not Null Or Fechafin Is Not Null)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999'))
           And Rownum = 1;
      Exception
        When Others Then
          v_Tienesancion := 0;
      End;
    
      If v_Tienesancion = 1 Then
        Raise e_Tienesancion;
      End If;
    
      --insertando el registro de exportacion
      Begin
        Insert Into t_censo_Letrados
          (Id_Letrado, Nombre, Apellido1, Apellido2, Num_Doc, Idtipoidentificacion, Sexo, Fechamodificacion)
        Values
          (Reg.Id_Letrado, Reg.Nombre, Reg.Apellido1, Reg.Apellido2, Reg.Num_Doc, Reg.Idtipoidentificacion, Reg.Sexo, Reg.Fechamodificacion);
        n_insertados_sindir := n_insertados_sindir + Sql%Rowcount;
        
      Exception
        When Others Then
          p_datoserror := Sqlcode || ': ' || Sqlerrm || ' - ' || 'ERR> Al insertar el letrado ' || Reg.Id_Letrado;
          Raise;
      End;
    
      Commit;
    
    Exception
      When e_Tienesancion Then
        Null;
    End;
  End Loop;

  Select to_char(Sysdate, 'dd/mm/yyyy hh24:mi:ss') Into v_fecha From dual;
  Dbms_Output.Put_Line('Antes de bloquear: ' || v_fecha);
  Lock Table t_censo_Letrados
    IN EXCLUSIVE MODE;
  --borrando carga antigua
  Delete t_censo_Letrados Where RegistroTemporal = 0;
  n_antiguos_borrados := Sql%Rowcount;
  
  --volcando carga antigua a nueva
  Update t_censo_Letrados Set RegistroTemporal = 0;
  n_insertados          := Sql%Rowcount;
  
  Commit;

  Dbms_Output.Put_Line('Todo correcto');
  Dbms_Output.Put_Line('n_antiguos_borrados :' || n_antiguos_borrados);
  Dbms_Output.Put_Line('n_insertados        :' || n_insertados);
  Dbms_Output.Put_Line('n_insertados_conlopd:' || n_insertados_conlopd);
  Dbms_Output.Put_Line('n_insertados_sinlopd:' || n_insertados_sinlopd);
  Dbms_Output.Put_Line('n_insertados_sindir :' || n_insertados_sindir);
  Select to_char(Sysdate, 'dd/mm/yyyy hh24:mi:ss') Into v_fecha From dual;
  Dbms_Output.Put_Line('FIN: ' || v_fecha);

End Refresh_t_censo_Letrados;
/
