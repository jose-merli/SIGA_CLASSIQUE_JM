CREATE OR REPLACE Package Pkg_Siga_Fusion_Personas Is

  -- Author   : ADRIANAG
  -- Created  : 17/06/2008
  -- Purpose  : Contiene procedimientos basicos para facilitar la fusion de personas
  -- Modified : ADRIANAG - 24/06/2008

  Procedure Mueveclienteaotrapersona(p_Idpersona_Origen  In Cen_Cliente.Idpersona%Type,
                                     p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                     p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                     p_Codretorno        Out Varchar2,
                                     p_Datoserror        Out Clob);

  Procedure Fusiona_Personas(p_Idpersona_Origen  In Cen_Persona.Idpersona%Type,
                             p_Idpersona_Destino In Cen_Persona.Idpersona%Type,
                             p_Codretorno        Out Varchar2,
                             p_Datoserror        Out Clob);

  Procedure Pasaanocolegiado(p_Idpersona  In Cen_Persona.Idpersona%Type,
                             p_Codretorno Out Varchar2,
                             p_Datoserror Out Clob);

End Pkg_Siga_Fusion_Personas;
/
CREATE OR REPLACE Package Body Pkg_Siga_Fusion_Personas Is

  Tabla Varchar2(100);

  --
  -- Muevecosasclienteaotrapersona
  -- Dados los idpersona de origen y destino y un idinstitucion, Mueve todos los datos posibles de un cliente a otro
  -- * Se suele usar antes de llamar a Borracliente_Simple()
  -- * Esta preparado para aceptar un idinstitucion null y entonces realiza la operacion para todos
  --
  Procedure Muevecosaspersonaaotrapersona(p_Idpersona_Origen  In Cen_Persona.Idpersona%Type,
                                          p_Idpersona_Destino In Cen_Persona.Idpersona%Type,
                                          p_Codretorno        Out Varchar2,
                                          p_Datoserror        Out Clob) Is
    n_registros_enorigen Number;
  Begin
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Muevecosaspersonaaotrapersona (' || 
                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): INI';
  
    Tabla := 'ECOM_MUTUALIDAD_CERTIFICADOS';
    Select Count(1) Into n_registros_enorigen
      From ECOM_MUTUALIDAD_CERTIFICADOS
     Where Idpersona = p_Idpersona_Origen; --¡OJO, SIN INSTITUCION!
    If n_registros_enorigen > 0 Then
    Update ECOM_MUTUALIDAD_CERTIFICADOS
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen; --¡OJO, SIN INSTITUCION!
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'MJU_COLEGIADO_ERROR';
    Select Count(1) Into n_registros_enorigen
      From MJU_COLEGIADO_ERROR
     Where Idpersona = p_Idpersona_Origen; --¡OJO, SIN INSTITUCION!
    If n_registros_enorigen > 0 Then
    Update MJU_COLEGIADO_ERROR
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen; --¡OJO, SIN INSTITUCION!
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Muevecosaspersonaaotrapersona (' ||
                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al mover en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End Muevecosaspersonaaotrapersona;
  
  --
  -- Muevecosasclienteaotrapersona
  -- Dados los idpersona de origen y destino y un idinstitucion, Mueve todos los datos posibles de un cliente a otro
  -- * Se suele usar antes de llamar a Borracliente_Simple()
  -- * Esta preparado para aceptar un idinstitucion null y entonces realiza la operacion para todos
  --
  Procedure Muevecosasclienteaotrapersona(p_Idpersona_Origen  In Cen_Persona.Idpersona%Type,
                                          p_Idpersona_Destino In Cen_Persona.Idpersona%Type,
                                          p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                          p_Codretorno        Out Varchar2,
                                          p_Datoserror        Out Clob) Is
    n_registros_enorigen Number;
    
    n_estadoscolegialesiguales Number;
    v_Iddireccionnueva Cen_Direcciones.Iddireccion%Type;
    Cursor c_Direcciones Is
      Select Idinstitucion, Iddireccion
        From Cen_Direcciones
       Where Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
         And Idpersona = p_Idpersona_Origen;
    n_direcciones Number;
    n_tiposdirecciones Number;
    n_solicitudesincorporacion Number;
    n_solicitudesmodifdirecciones Number;
    n_expedientes Number;
    n_denunciantes Number;
    n_denunciados Number;
    n_partes Number;
    n_solicitudescertificados Number;
    n_productossolicitados Number;
    
    v_Idcuentanueva Cen_Cuentasbancarias.idcuenta%Type;
    Cursor c_Cuentas Is
      Select Idinstitucion, Idcuenta
        From Cen_Cuentasbancarias Cue
       Where Cue.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
         And Cue.Idpersona = p_Idpersona_Origen;
    
    v_Idcvnuevo Cen_Datoscv.idcv%Type;
    Cursor c_DatosCV Is
      Select Idinstitucion, Idcv
        From Cen_Datoscv
       Where Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
         And Idpersona = p_Idpersona_Origen;
    
    v_Idanticiponuevo Cen_Datoscv.idcv%Type;
    Cursor c_Anticipos Is
      Select Idinstitucion, Idanticipo
        From Pys_Anticipoletrado
       Where Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
         And Idpersona = p_Idpersona_Origen;
      
  Begin
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Muevecosasclienteaotrapersona (' || p_Idinstitucion || ', ' ||
                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): INI';
  
    
    Tabla := 'CEN_CLIENTE fecha modificacion';
    Select Count(1) Into n_registros_enorigen
      From Cen_Cliente Destino
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Cliente Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Cliente Destino
       Set (Fechamodificacion, Fechaalta, Fechacarga, Fechaactualizacion, Fechaexportcenso) = 
                                 (Select Greatest(Nvl(Origen.Fechamodificacion, Destino.Fechamodificacion),
                                                  Nvl(Destino.Fechamodificacion, Origen.Fechamodificacion)),
                                         Least(Nvl(Origen.Fechaalta, Destino.Fechaalta),
                                               Nvl(Destino.Fechaalta, Origen.Fechaalta)),
                                         Greatest(Nvl(Origen.Fechacarga, Destino.Fechacarga),
                                                  Nvl(Destino.Fechacarga, Origen.Fechacarga)),
                                         Greatest(Nvl(Origen.Fechaactualizacion,
                                                      Destino.Fechaactualizacion),
                                                  Nvl(Destino.Fechaactualizacion,
                                                      Origen.Fechaactualizacion)),
                                         Greatest(Nvl(Origen.Fechaexportcenso, Destino.Fechaexportcenso),
                                                  Nvl(Destino.Fechaexportcenso, Origen.Fechaexportcenso))
                                    From Cen_Cliente Origen
                                   Where Origen.Idpersona = p_Idpersona_Origen
                                     And Destino.Idinstitucion = Origen.Idinstitucion)
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Cliente Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_COLEGIADO fecha modificacion';
    Select Count(1) Into n_registros_enorigen
      From Cen_Colegiado Destino
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Colegiado Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Colegiado Destino
       Set (Fechamodificacion) = (Select Greatest(Nvl(Origen.Fechamodificacion, Destino.Fechamodificacion),
                                                  Nvl(Destino.Fechamodificacion, Origen.Fechamodificacion))
                                    From Cen_Colegiado Origen
                                   Where Origen.Idpersona = p_Idpersona_Origen
                                     And Destino.Idinstitucion = Origen.Idinstitucion)
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Colegiado Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_NOCOLEGIADO fecha modificacion';
    Select Count(1) Into n_registros_enorigen
      From Cen_Nocolegiado Destino
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Nocolegiado Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Nocolegiado Destino
       Set (Fechamodificacion) = (Select Greatest(Nvl(Origen.Fechamodificacion, Destino.Fechamodificacion),
                                                  Nvl(Destino.Fechamodificacion, Origen.Fechamodificacion))
                                    From Cen_Nocolegiado Origen
                                   Where Origen.Idpersona = p_Idpersona_Origen
                                     And Destino.Idinstitucion = Origen.Idinstitucion)
     Where Destino.Idpersona = p_Idpersona_Destino
       And Destino.Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Exists (Select 1
              From Cen_Nocolegiado Origen
             Where Origen.Idpersona = p_Idpersona_Origen
               And Destino.Idinstitucion = Origen.Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_NOCOLEGIADO Notario';
    Select Count(1) Into n_registros_enorigen
      From CEN_NOCOLEGIADO
     Where idpersonanotario = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_NOCOLEGIADO
       Set idpersonanotario = p_Idpersona_Destino
     Where idpersonanotario = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_DATOSCOLEGIALESESTADO borrado duplicados';
    Select Count(1) Into n_registros_enorigen
      From Cen_Datoscolegialesestado Ori
     Where Exists (Select 1
              From Cen_Datoscolegialesestado Des
             Where Des.Idpersona = p_Idpersona_Destino
               And Des.Idinstitucion = Ori.Idinstitucion
               And trunc(Des.Fechaestado) = trunc(Ori.Fechaestado)
               And Des.Idestado = Ori.Idestado)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete From Cen_Datoscolegialesestado Ori
     Where Exists (Select 1
              From Cen_Datoscolegialesestado Des
             Where Des.Idpersona = p_Idpersona_Destino
               And Des.Idinstitucion = Ori.Idinstitucion
               And trunc(Des.Fechaestado) = trunc(Ori.Fechaestado)
               And Des.Idestado = Ori.Idestado)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_DATOSCOLEGIALESESTADO borrado duplicados de baja automaticos';
    Select Count(1) Into n_registros_enorigen
      From Cen_Datoscolegialesestado Ori
     Where Idestado = 30
       And Exists (Select *
              From Cen_Datoscolegialesestado Des
             Where Des.Idpersona In (p_Idpersona_Origen, p_Idpersona_Destino)
               And Des.Idinstitucion = Ori.Idinstitucion
               And trunc(Des.Fechaestado) = trunc(Ori.Fechaestado)
               And Des.Idestado < 30)
       And Idpersona In (p_Idpersona_Origen, p_Idpersona_Destino)
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete From Cen_Datoscolegialesestado Ori
     Where Idestado = 30
       And Exists (Select *
              From Cen_Datoscolegialesestado Des
             Where Des.Idpersona In (p_Idpersona_Origen, p_Idpersona_Destino)
               And Des.Idinstitucion = Ori.Idinstitucion
               And trunc(Des.Fechaestado) = trunc(Ori.Fechaestado)
               And Des.Idestado < 30)
       And Idpersona In (p_Idpersona_Origen, p_Idpersona_Destino)
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_DATOSCOLEGIALESESTADO';
-- LOS ESTADOS SON DATOS SENSIBLES: SE TIENEN QUE ARREGLAR MANUALMENTE SI COINCIDEN EN FECHAS
    Select Count(1)
      Into n_estadoscolegialesiguales
      From Cen_Datoscolegialesestado Ori
     Where Exists (Select *
              From Cen_Datoscolegialesestado Des
             Where Des.Idpersona = p_Idpersona_Destino
               And Des.Idinstitucion = Ori.Idinstitucion
               And trunc(Des.Fechaestado) = trunc(Ori.Fechaestado))
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_estadoscolegialesiguales > 0 Then
      p_Datoserror := 'Ambas personas tienen estados colegiales diferentes en la misma fecha. Por favor, borre los estados que no correspondan antes de continuar con la Fusión';
      p_Codretorno := -1;
      Return;
    End If;
    
    Select Count(1) Into n_registros_enorigen
      From Cen_Datoscolegialesestado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Datoscolegialesestado
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'PYS_PRODUCTOSSOLICITADOS despues de cambiar direcciones y cuentas';
    Select Count(1) Into n_registros_enorigen
      From Pys_Productossolicitados
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      -- PYS_PRODUCTOSSOLICITADOS tiene doble FK. Solo se puede activar de nuevo despues de cambiar ambos
      Execute Immediate 'set constraint FK_PYS_PSOLICITADOS_CEN_DIRECC deferred';
      Execute Immediate 'set constraint PK_PRODUCTOSSOLICITADOS_CUENTA deferred';
    End If;
  
    Begin
      Select Nvl(Max(Iddireccion), 0)
        Into v_Iddireccionnueva
        From Cen_Direcciones
       Where Idpersona = p_Idpersona_Destino
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      
      For Reg In c_Direcciones Loop
        v_Iddireccionnueva := v_Iddireccionnueva + 1;
        Execute Immediate 'set constraint FK_CEN_DIRECCION_DIRECCION deferred';
        Execute Immediate 'set constraint FK_DIRECCIONES deferred';
        Execute Immediate 'set constraint FK_DIRECCIONES_SOLICITUDMODIFI deferred';
        Execute Immediate 'set constraint FK_EXP_EXPEDIENTE_DIRECCION deferred';
        Execute Immediate 'set constraint FK_EXP_DENUNCIANTE_DIRECCION deferred';
        Execute Immediate 'set constraint FK_EXP_DENUNCIADO_DIRECCION deferred';
        Execute Immediate 'set constraint FK_EXP_PARTE_DIRECCION deferred';
        
        Tabla := 'CEN_DIRECCIONES';
        Select Count(1) Into n_registros_enorigen
          From Cen_Direcciones
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Cen_Direcciones
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_direcciones := n_direcciones + Sql%Rowcount;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_DIRECCION_TIPODIRECCION';
        Select Count(1) Into n_registros_enorigen
          From Cen_Direccion_Tipodireccion
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Cen_Direccion_Tipodireccion
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_tiposdirecciones := n_tiposdirecciones + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_CEN_DIRECCION_DIRECCION immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
        Tabla := 'CEN_SOLICITUDINCORPORACION';
        Select Count(1) Into n_registros_enorigen
          From CEN_SOLICITUDINCORPORACION
         Where Idpersonatemp = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddirecciontemp = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update CEN_SOLICITUDINCORPORACION
           Set Idpersonatemp = p_Idpersona_Destino, Iddirecciontemp = v_Iddireccionnueva
         Where Idpersonatemp = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddirecciontemp = Reg.Iddireccion;
        n_solicitudesincorporacion := n_solicitudesincorporacion + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_DIRECCIONES immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
        Tabla := 'CEN_SOLIMODIDIRECCIONES';
        Select Count(1) Into n_registros_enorigen
          From CEN_SOLIMODIDIRECCIONES
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update CEN_SOLIMODIDIRECCIONES
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_solicitudesmodifdirecciones := n_solicitudesmodifdirecciones + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_DIRECCIONES_SOLICITUDMODIFI immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
        Tabla := 'EXP_EXPEDIENTE';
        Select Count(1) Into n_registros_enorigen
          From Exp_Expediente
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Exp_Expediente
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_expedientes := n_expedientes + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_EXP_EXPEDIENTE_DIRECCION immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'EXP_DENUNCIANTE';
        Select Count(1) Into n_registros_enorigen
          From Exp_Denunciante
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Exp_Denunciante
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_denunciantes := n_denunciantes + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_EXP_DENUNCIANTE_DIRECCION immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'EXP_DENUNCIADO';
        Select Count(1) Into n_registros_enorigen
          From Exp_Denunciado
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Exp_Denunciado
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_denunciados := n_denunciados + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_EXP_DENUNCIADO_DIRECCION immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'EXP_PARTE';
        Select Count(1) Into n_registros_enorigen
          From EXP_PARTE
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update EXP_PARTE
           Set Idpersona = p_Idpersona_Destino, Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_partes := n_partes + Sql%Rowcount;
        End If;
        Execute Immediate 'set constraint FK_EXP_PARTE_DIRECCION immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CER_SOLICITUDCERTIFICADOS';
        Select Count(1) Into n_registros_enorigen
          From Cer_Solicitudcertificados
         Where Idpersona_Dir = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion_Dir = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Cer_Solicitudcertificados
           Set Idpersona_Dir = p_Idpersona_Destino, Iddireccion_Dir = v_Iddireccionnueva
         Where Idpersona_Dir = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion_Dir = Reg.Iddireccion;
        n_solicitudescertificados := n_solicitudescertificados + Sql%Rowcount;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'PYS_PRODUCTOSSOLICITADOS';
        Select Count(1) Into n_registros_enorigen
          From Pys_Productossolicitados
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        If n_registros_enorigen > 0 Then
        Update Pys_Productossolicitados
           Set Iddireccion = v_Iddireccionnueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Iddireccion = Reg.Iddireccion;
        n_productossolicitados := n_productossolicitados + Sql%Rowcount;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
      End Loop;
    End;
  
    Begin
      Select Nvl(Max(Idcuenta), 0)
        Into v_Idcuentanueva
        From Cen_cuentasbancarias
       Where Idpersona = p_Idpersona_Destino
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      
      For Reg In c_Cuentas Loop
        v_Idcuentanueva := v_Idcuentanueva + 1;
        Execute Immediate 'set constraint FK_CUENTASBANCARIASSOLICITUDMO deferred';
        Execute Immediate 'set constraint FK_MANDATOS_CUENTASBANCARIAS deferred';
        Execute Immediate 'set constraint FK_MANDATOS_ANEXOS deferred';
        Execute Immediate 'set constraint FK_COMPONENTES_CUENTASBANCARIA deferred';
        Execute Immediate 'set constraint FK_FAC_FACTURA_CUENTASBANCARIA deferred';
        Execute Immediate 'set constraint FK_FACTURA_MANDATO deferred';
        Execute Immediate 'set constraint FK_FACTURA_MANDATO_DEUDOR deferred';
        Execute Immediate 'set constraint FK_FAC_ABONO_CUENTASBANCARIAS deferred';
        Execute Immediate 'set constraint FK_FCS_PER_DEST_CUENTASBANCA deferred';
        Execute Immediate 'set constraint FK_FACINCLUIDADISQUETE_CUENTAS deferred';
        Execute Immediate 'set constraint FK_FAC_RENEGOCIACION_CUENTASBA deferred';
        Execute Immediate 'set constraint FK_COMPRA_CUENTASBANCARIAS deferred';
        Execute Immediate 'set constraint FK_SUSCRIPCION_CUENTASBANCARIA deferred';
        Execute Immediate 'set constraint FK_SERVICIOSSOLICITADOS_CUENTA deferred';
        
        Tabla := 'CEN_CUENTASBANCARIAS';
        Select Count(1) Into n_registros_enorigen
          From Cen_cuentasbancarias
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Cen_cuentasbancarias
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_SOLICMODICUENTAS';
        Select Count(1) Into n_registros_enorigen
          From CEN_SOLICMODICUENTAS
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update CEN_SOLICMODICUENTAS
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_CUENTASBANCARIASSOLICITUDMO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_MANDATOS_CUENTASBANCARIAS';
        Select Count(1) Into n_registros_enorigen
          From CEN_MANDATOS_CUENTASBANCARIAS
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update CEN_MANDATOS_CUENTASBANCARIAS
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_MANDATOS_CUENTASBANCARIAS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_ANEXOS_CUENTASBANCARIAS';
        Select Count(1) Into n_registros_enorigen
          From CEN_ANEXOS_CUENTASBANCARIAS
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update CEN_ANEXOS_CUENTASBANCARIAS
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_MANDATOS_ANEXOS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_COMPONENTES';
        Select Count(1) Into n_registros_enorigen
          From Cen_Componentes Ori
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Cen_Componentes Ori
           Set Idpersona    = p_Idpersona_Destino,
               Idcuenta     = v_Idcuentanueva,
               Idcomponente = Idcomponente +
                              (Select Nvl(Max(Des.Idcomponente), 0)
                                 From Cen_Componentes Des
                                Where Des.Idinstitucion = Ori.Idinstitucion
                                  And Des.Idpersona = p_Idpersona_Destino)
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_COMPONENTES_CUENTASBANCARIA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
     
        Tabla := 'FAC_FACTURA';
        Select Count(1) Into n_registros_enorigen
          From FAC_FACTURA
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update FAC_FACTURA
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FACTURA_MANDATO immediate';
        Execute Immediate 'set constraint FK_FAC_FACTURA_CUENTASBANCARIA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'FAC_FACTURA deudor';
        Select Count(1) Into n_registros_enorigen
          From fac_factura
         Where idpersonadeudor = p_idpersona_origen
           And idinstitucion = reg.idinstitucion
           And idcuentadeudor = reg.idcuenta;
        If n_registros_enorigen > 0 Then
        Update fac_factura
           Set idpersonadeudor = p_idpersona_destino, idcuentadeudor = v_idcuentanueva
         Where idpersonadeudor = p_idpersona_origen
           And idinstitucion = reg.idinstitucion
           And idcuentadeudor = reg.idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FACTURA_MANDATO_DEUDOR immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'FAC_ABONO';
        Select Count(1) Into n_registros_enorigen
          From FAC_ABONO
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update FAC_ABONO
           Set Idpersona = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FAC_ABONO_CUENTASBANCARIAS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'FAC_ABONO deudor';
        Select Count(1) Into n_registros_enorigen
          From FAC_ABONO
         Where Idpersonadeudor = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuentadeudor = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update FAC_ABONO
           Set Idpersonadeudor = p_Idpersona_Destino, Idcuentadeudor = v_Idcuentanueva
         Where Idpersonadeudor = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuentadeudor = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'FCS_PAGO_COLEGIADO destino';
        Select Count(1) Into n_registros_enorigen
          From FCS_PAGO_COLEGIADO
         Where Idperdestino = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update FCS_PAGO_COLEGIADO
           Set Idperdestino = p_Idpersona_Destino, Idcuenta = v_Idcuentanueva
         Where Idperdestino = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FCS_PER_DEST_CUENTASBANCA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'FAC_FACTURAINCLUIDAENDISQUETE';
        Select Count(1) Into n_registros_enorigen
          From Fac_Facturaincluidaendisquete
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Fac_Facturaincluidaendisquete
           Set Idpersona = p_Idpersona_Destino, idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FACINCLUIDADISQUETE_CUENTAS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
     
        Tabla := 'FAC_RENEGOCIACION';
        Select Count(1) Into n_registros_enorigen
          From Fac_Renegociacion
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Fac_Renegociacion
           Set Idpersona = p_Idpersona_Destino, idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_FAC_RENEGOCIACION_CUENTASBA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'PYS_COMPRA';
        Select Count(1) Into n_registros_enorigen
          From Pys_Compra
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Pys_Compra
           Set Idpersona = p_Idpersona_Destino, idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_COMPRA_CUENTASBANCARIAS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'PYS_SUSCRIPCION';
        Select Count(1) Into n_registros_enorigen
          From Pys_Suscripcion
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Pys_Suscripcion
           Set Idpersona = p_Idpersona_Destino, idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_SUSCRIPCION_CUENTASBANCARIA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
        Tabla := 'PYS_SERVICIOSSOLICITADOS';
        Select Count(1) Into n_registros_enorigen
          From Pys_Serviciossolicitados
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update Pys_Serviciossolicitados
           Set Idpersona = p_Idpersona_Destino, idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_SERVICIOSSOLICITADOS_CUENTA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
        
        Tabla := 'PYS_PRODUCTOSSOLICITADOS';
        Select Count(1) Into n_registros_enorigen
          From PYS_PRODUCTOSSOLICITADOS
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If n_registros_enorigen > 0 Then
        Update PYS_PRODUCTOSSOLICITADOS
           Set idcuenta = v_Idcuentanueva
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = Reg.Idinstitucion
           And Idcuenta = Reg.Idcuenta;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      End Loop;
    
    End;
    
    Tabla := 'PYS_PRODUCTOSSOLICITADOS despues de cambiar direcciones y cuentas';
    Select Count(1) Into n_registros_enorigen
      From Pys_Productossolicitados
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      Update Pys_Productossolicitados
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      -- PYS_PRODUCTOSSOLICITADOS tiene doble FK. Solo se puede activar de nuevo despues de cambiar ambos
      Execute Immediate 'set constraint FK_PYS_PSOLICITADOS_CEN_DIRECC immediate';
      Execute Immediate 'set constraint PK_PRODUCTOSSOLICITADOS_CUENTA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    End If;
  
    Tabla := 'CEN_SOLICITUDINCORPORACION sin direccion';
    Select Count(1) Into n_registros_enorigen
      From CEN_SOLICITUDINCORPORACION
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_SOLICITUDINCORPORACION
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'CEN_SOLICITUDINCORPORACION sin direccion temp';
    Select Count(1) Into n_registros_enorigen
      From CEN_SOLICITUDINCORPORACION
     Where Idpersonatemp = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddirecciontemp Is Null;
    If n_registros_enorigen > 0 Then
    Update CEN_SOLICITUDINCORPORACION
       Set Idpersonatemp = p_Idpersona_Destino
     Where Idpersonatemp = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddirecciontemp Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_EXPEDIENTE sin direccion';
    Select Count(1) Into n_registros_enorigen
      From Exp_Expediente
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If n_registros_enorigen > 0 Then
    Update Exp_Expediente
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_DENUNCIANTE sin direccion';
    Select Count(1) Into n_registros_enorigen
      From Exp_Denunciante
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If n_registros_enorigen > 0 Then
    Update Exp_Denunciante
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_DENUNCIADO sin direccion';
    Select Count(1) Into n_registros_enorigen
      From Exp_Denunciado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If n_registros_enorigen > 0 Then
    Update Exp_Denunciado
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_PARTE sin direccion';
    Select Count(1) Into n_registros_enorigen
      From EXP_PARTE
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If n_registros_enorigen > 0 Then
    Update EXP_PARTE
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CER_SOLICITUDCERTIFICADOS sin direccion';
    Select Count(1) Into n_registros_enorigen
      From Cer_Solicitudcertificados
     Where Idpersona_Des = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cer_Solicitudcertificados
       Set Idpersona_Des = p_Idpersona_Destino
     Where Idpersona_Des = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'CER_SOLICITUDCERTIFICADOS sin direccion dir';
    Select Count(1) Into n_registros_enorigen
      From Cer_Solicitudcertificados
     Where Idpersona_Dir = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion_Dir Is Null;
    If n_registros_enorigen > 0 Then
    Update Cer_Solicitudcertificados
       Set Idpersona_Dir = p_Idpersona_Destino
     Where Idpersona_Dir = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Iddireccion_Dir Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_COMPONENTES sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From Cen_Componentes Ori
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update Cen_Componentes Ori
       Set Idpersona    = p_Idpersona_Destino,
           Idcomponente = Idcomponente + (Select Nvl(Max(Idcomponente), 0)
                                            From Cen_Componentes Des
                                           Where Des.Idinstitucion = Ori.Idinstitucion
                                             And Des.Idpersona = p_Idpersona_Destino
                                             And Des.Idcuenta Is Null)
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_COMPONENTES socios';
    Select Count(1) Into n_registros_enorigen
      From Cen_Componentes Ori
     Where Cen_Cliente_Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Componentes Ori
       Set Cen_Cliente_Idpersona = p_Idpersona_Destino
     Where Cen_Cliente_Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_FACTURA sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From FAC_FACTURA
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update FAC_FACTURA
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_FACTURA deudor sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From fac_factura
     Where idpersonadeudor = p_idpersona_origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And idcuentadeudor Is Null;
    If n_registros_enorigen > 0 Then
    Update fac_factura
       Set idpersonadeudor = p_idpersona_destino
     Where idpersonadeudor = p_idpersona_origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And idcuentadeudor Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_ABONO sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From FAC_ABONO
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update FAC_ABONO
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_ABONO deudor sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From FAC_ABONO
     Where Idpersonadeudor = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuentadeudor Is Null;
    If n_registros_enorigen > 0 Then
    Update FAC_ABONO
       Set Idpersonadeudor = p_Idpersona_Destino
     Where Idpersonadeudor = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuentadeudor Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_PAGO_COLEGIADO destino sin cuenta';
    -- Esto no genera error porque la clave esta en idperorigen
    Select Count(1) Into n_registros_enorigen
      From FCS_PAGO_COLEGIADO
     Where Idperdestino = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update FCS_PAGO_COLEGIADO
       Set Idperdestino = p_Idpersona_Destino
     Where Idperdestino = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_RENEGOCIACION sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From Fac_Renegociacion
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update Fac_Renegociacion
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'PYS_COMPRA sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From Pys_Compra
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update Pys_Compra
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'PYS_SUSCRIPCION sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From Pys_Suscripcion
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update Pys_Suscripcion
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'PYS_SERVICIOSSOLICITADOS sin cuenta';
    Select Count(1) Into n_registros_enorigen
      From Pys_Serviciossolicitados
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If n_registros_enorigen > 0 Then
    Update Pys_Serviciossolicitados
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
       And Idcuenta Is Null;
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'PYS_PETICIONCOMPRASUSCRIPCION';
    Select Count(1) Into n_registros_enorigen
      From Pys_Peticioncomprasuscripcion
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Pys_Peticioncomprasuscripcion
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
    Select Count(1) Into n_registros_enorigen
      From FCS_PAGO_COLEGIADO
     Where Idperorigen = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    
      Execute Immediate 'set constraint FK_COBROS_RET_PAGOCOLEGIADO deferred';
      Execute Immediate 'set constraint FK_APLICA_MOVIMIENTOS_PAGOS deferred';
      Execute Immediate 'set constraint FK_FAC_ABONO_PAGOSCOLEGIADO deferred';
      
      Tabla := 'FCS_PAGO_COLEGIADO origen';
      Update FCS_PAGO_COLEGIADO
         Set Idperorigen = p_Idpersona_Destino
       Where Idperorigen = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
-- DARA ERROR CUANDO EXISTA TANTO EN EL ORIGEN COMO EN EL DESTINO
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      Tabla := 'FCS_COBROS_RETENCIONJUDICIAL';
      Select Count(1) Into n_registros_enorigen
        From FCS_COBROS_RETENCIONJUDICIAL
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update FCS_COBROS_RETENCIONJUDICIAL
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_COBROS_RET_PAGOCOLEGIADO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      Tabla := 'FCS_APLICA_MOVIMIENTOSVARIOS';
      Select Count(1) Into n_registros_enorigen
        From FCS_APLICA_MOVIMIENTOSVARIOS
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update FCS_APLICA_MOVIMIENTOSVARIOS
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_APLICA_MOVIMIENTOS_PAGOS immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      Tabla := 'FAC_ABONO origen';
      Select Count(1) Into n_registros_enorigen
        From FAC_ABONO
       Where Idperorigen = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update FAC_ABONO
         Set Idperorigen = p_Idpersona_Destino
       Where Idperorigen = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_FAC_ABONO_PAGOSCOLEGIADO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);

    End If;
  
    Begin
      Select Nvl(Max(Idcv), 0)
        Into v_Idcvnuevo
        From Cen_Datoscv
       Where Idpersona = p_Idpersona_Destino
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      
      For Reg In c_Datoscv Loop
        v_Idcvnuevo := v_Idcvnuevo + 1;
        Execute Immediate 'set constraint FK_DATOSCV_SOLICITUDMODIFICACI deferred';
          
        Tabla := 'CEN_DATOSCV';
        Select Count(1) Into n_registros_enorigen
          From Cen_Datoscv
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idcv = reg.idcv;
        If n_registros_enorigen > 0 Then
        Update Cen_Datoscv
           Set Idpersona = p_Idpersona_Destino, Idcv = v_Idcvnuevo
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idcv = reg.idcv;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'CEN_SOLICITUDMODIFICACIONCV';
        Select Count(1) Into n_registros_enorigen
          From Cen_Solicitudmodificacioncv
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idcv = reg.idcv;
        If n_registros_enorigen > 0 Then
        Update Cen_Solicitudmodificacioncv
           Set Idpersona = p_Idpersona_Destino, Idcv = v_Idcvnuevo
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idcv = reg.idcv;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_DATOSCV_SOLICITUDMODIFICACI immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      End Loop;
    
    End;
  
    Begin
      Select Nvl(Max(Idanticipo), 0)
        Into v_Idanticiponuevo
        From Pys_Anticipoletrado
       Where Idpersona = p_Idpersona_Destino
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      
      For Reg In c_Anticipos Loop
        v_Idanticiponuevo := v_Idanticiponuevo + 1;
        Execute Immediate 'set constraint FK_PYS_LINEAANT_CEN_ANTICIPO deferred';
        Execute Immediate 'set constraint FK_PYS_SERVANT_PYS_ANTICIPO deferred';
          
        Tabla := 'PYS_ANTICIPOLETRADO';
        Select Count(1) Into n_registros_enorigen
          From PYS_ANTICIPOLETRADO
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If n_registros_enorigen > 0 Then
        Update PYS_ANTICIPOLETRADO
           Set Idpersona = p_Idpersona_Destino, idanticipo = v_Idanticiponuevo
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'PYS_LINEAANTICIPO';
        Select Count(1) Into n_registros_enorigen
          From PYS_LINEAANTICIPO
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If n_registros_enorigen > 0 Then
        Update PYS_LINEAANTICIPO
           Set Idpersona = p_Idpersona_Destino, idanticipo = v_Idanticiponuevo
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_PYS_LINEAANT_CEN_ANTICIPO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
        Tabla := 'PYS_SERVICIOANTICIPO';
        Select Count(1) Into n_registros_enorigen
          From PYS_SERVICIOANTICIPO
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If n_registros_enorigen > 0 Then
        Update PYS_SERVICIOANTICIPO
           Set Idpersona = p_Idpersona_Destino, idanticipo = v_Idanticiponuevo
         Where Idpersona = p_Idpersona_Origen
           And Idinstitucion = reg.Idinstitucion
           And idanticipo = reg.idanticipo;
        If Sql%Rowcount > 0 Then
          p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
        End If;
        End If;
        Execute Immediate 'set constraint FK_PYS_SERVANT_PYS_ANTICIPO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      End Loop;
    
    End;
  
    Tabla := 'CEN_GRUPOSCLIENTE_CLIENTE borrado duplicados';
    Select Count(1) Into n_registros_enorigen
      From Cen_Gruposcliente_Cliente Ori
     Where Exists (Select 1
              From Cen_Gruposcliente_Cliente Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idinstitucion_Grupo = Ori.Idinstitucion_Grupo
               And Des.Idgrupo = Ori.Idgrupo
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Cen_Gruposcliente_Cliente Ori
     Where Exists (Select 1
              From Cen_Gruposcliente_Cliente Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idinstitucion_Grupo = Ori.Idinstitucion_Grupo
               And Des.Idgrupo = Ori.Idgrupo
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'CEN_GRUPOSCLIENTE_CLIENTE';
    Select Count(1) Into n_registros_enorigen
      From Cen_Gruposcliente_Cliente
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Gruposcliente_Cliente
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_HISTORICO';
    Select Count(1) Into n_registros_enorigen
      From Cen_Historico Ori
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Historico Ori
       Set Idpersona   = p_Idpersona_Destino,
           Idhistorico = Idhistorico + (Select Nvl(Max(Des.Idhistorico), 0)
                                          From Cen_Historico Des
                                         Where Des.Idinstitucion = Ori.Idinstitucion
                                           And Des.Idpersona = p_Idpersona_Destino)
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_COLACAMBIOLETRADO borrado';
    -- No necesitamos estos datos: igualmente se actualizara al final de este proceso
    Select Count(1) Into n_registros_enorigen
      From Cen_Colacambioletrado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Cen_Colacambioletrado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Tabla := 'CEN_NOCOLEGIADO_ACTIVIDAD';
    Select Count(1) Into n_registros_enorigen
      From Cen_Nocolegiado_Actividad Ori
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Nocolegiado_Actividad Ori
       Set Idpersona              = p_Idpersona_Destino,
           Idactividadprofesional = Idactividadprofesional +
                                    (Select Nvl(Max(Idactividadprofesional), 0)
                                       From Cen_Nocolegiado_Actividad Des
                                      Where Des.Idpersona = p_Idpersona_Destino
                                        And Des.Idinstitucion = Ori.Idinstitucion)
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ECOM_CEN_COLEGIADO';
    Select Count(1) Into n_registros_enorigen
      From Ecom_Cen_Colegiado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Ecom_Cen_Colegiado
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FAC_CLIENINCLUIDOENSERIEFACTUR borrado duplicados';
    Select Count(1) Into n_registros_enorigen
      From Fac_Clienincluidoenseriefactur Ori
     Where Exists (Select 1
              From Fac_Clienincluidoenseriefactur Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idseriefacturacion = Ori.Idseriefacturacion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Fac_Clienincluidoenseriefactur Ori
     Where Exists (Select 1
              From Fac_Clienincluidoenseriefactur Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idseriefacturacion = Ori.Idseriefacturacion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'FAC_CLIENINCLUIDOENSERIEFACTUR';
    Select Count(1) Into n_registros_enorigen
      From Fac_Clienincluidoenseriefactur
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fac_Clienincluidoenseriefactur
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_BAJASTEMPORALES borrado duplicados';
    -- en este caso se pierden datos, como la descripcion de la baja. Pero no es algo importante
    Select Count(1) Into n_registros_enorigen
      From Cen_Bajastemporales Ori
     Where Exists (Select 1
              From Cen_Bajastemporales Des
             Where Trunc(Des.Fechabt) = Trunc(Ori.Fechabt)
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Cen_Bajastemporales Ori
     Where Exists (Select 1
              From Cen_Bajastemporales Des
             Where Trunc(Des.Fechabt) = Trunc(Ori.Fechabt)
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'CEN_BAJASTEMPORALES';
    Select Count(1) Into n_registros_enorigen
      From Cen_Bajastemporales
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Bajastemporales
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_SOLICMODIFEXPORTARFOTO';
    Select Count(1) Into n_registros_enorigen
      From Cen_Solicmodifexportarfoto
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Cen_Solicmodifexportarfoto
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_SANCION';
    Select Count(1) Into n_registros_enorigen
      From Cen_Sancion
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      Execute Immediate 'set constraint FK_CEN_SANCION_SANCION_CGAE deferred';
      Update Cen_Sancion
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      Execute Immediate 'set constraint FK_CEN_SANCION_SANCION_CGAE immediate';
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_MEDIADOR_CSV';
    Select Count(1) Into n_registros_enorigen
      From CEN_MEDIADOR_CSV
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_MEDIADOR_CSV
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_MEDIADOR_XML';
    Select Count(1) Into n_registros_enorigen
      From CEN_MEDIADOR_XML
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_MEDIADOR_XML
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_SOLICITMODIFDATOSBASICOS';
    Select Count(1) Into n_registros_enorigen
      From CEN_SOLICITMODIFDATOSBASICOS
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_SOLICITMODIFDATOSBASICOS
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_SOLICITUDALTER';
    Select Count(1) Into n_registros_enorigen
      From CEN_SOLICITUDALTER
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_SOLICITUDALTER
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'CEN_SOLICITUDESMODIFICACION';
    Select Count(1) Into n_registros_enorigen
      From CEN_SOLICITUDESMODIFICACION
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update CEN_SOLICITUDESMODIFICACION
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ECOM_COMUNICACIONRESOLUCIONAJG';
    Select Count(1) Into n_registros_enorigen
      From ECOM_COMUNICACIONRESOLUCIONAJG
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update ECOM_COMUNICACIONRESOLUCIONAJG
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ECOM_DESIGNAPROVISIONAL';
    Select Count(1) Into n_registros_enorigen
      From ECOM_DESIGNAPROVISIONAL
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update ECOM_DESIGNAPROVISIONAL
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ECOM_SOLIMPUGRESOLUCIONAJG';
    Select Count(1) Into n_registros_enorigen
      From ECOM_SOLIMPUGRESOLUCIONAJG
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update ECOM_SOLIMPUGRESOLUCIONAJG
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion_letrado = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_DESTINATARIOSAVISOS borrado duplicados';
    -- en este caso se pierden datos, como la direccion adjunta. Pero al menos no son datos visibles
    Select Count(1) Into n_registros_enorigen
      From EXP_DESTINATARIOSAVISOS ori
     Where Exists (Select 1
              From EXP_DESTINATARIOSAVISOS Des
             Where Des.Idtipoexpediente = Ori.Idtipoexpediente
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete EXP_DESTINATARIOSAVISOS ori
     Where Exists (Select 1
              From EXP_DESTINATARIOSAVISOS Des
             Where Des.Idtipoexpediente = Ori.Idtipoexpediente
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'EXP_DESTINATARIOSAVISOS';
    Select Count(1) Into n_registros_enorigen
      From EXP_DESTINATARIOSAVISOS
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update EXP_DESTINATARIOSAVISOS
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'EXP_SOLICITUDBORRADO';
    Select Count(1) Into n_registros_enorigen
      From EXP_SOLICITUDBORRADO
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update EXP_SOLICITUDBORRADO
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Select Count(1) Into n_registros_enorigen
      From Scs_Inscripcionturno
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      Execute Immediate 'set constraint FK_SCS_INSCRIPCION_ULTIMO_T deferred';
      
      Tabla := 'SCS_INSCRIPCIONTURNO borrado duplicados';
      -- en este caso se pierden datos, como baja y motivos. Pero al menos no son datos visibles
      Delete Scs_Inscripcionturno Ori
       Where Exists (Select 1
                From Scs_Inscripcionturno Des
               Where Des.Idinstitucion = Ori.Idinstitucion
                 And Des.Idturno = Ori.Idturno
                 And Trunc(Des.Fechasolicitud) = Trunc(Ori.Fechasolicitud)
                 And Des.Idpersona = p_Idpersona_Destino)
         And Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      Tabla := 'SCS_INSCRIPCIONTURNO';
      Update Scs_Inscripcionturno
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      Tabla := 'SCS_TURNO';
      Select Count(1) Into n_registros_enorigen
        From SCS_TURNO
       Where IDPERSONA_ULTIMO = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update SCS_TURNO
         Set IDPERSONA_ULTIMO = p_Idpersona_Destino
       Where IDPERSONA_ULTIMO = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      
      Execute Immediate 'set constraint FK_SCS_INSCRIPCION_ULTIMO_T immediate';
    End If;
  
    Select Count(1) Into n_registros_enorigen
      From SCS_INSCRIPCIONGUARDIA
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      Execute Immediate 'set constraint FK_SCS_INSCRIPCION_ULTIMO deferred';
      Execute Immediate 'set constraint FK_SCS_GRUPOGUA_INSCRIPCIONGUA deferred';
      Execute Immediate 'set constraint FK_CALENDARIO_COLEGIADO_ULTIMO deferred';
      
      Tabla := 'SCS_INSCRIPCIONGUARDIA borrado duplicados';
      -- en este caso se pierden datos, como baja y motivos. Pero al menos no son datos visibles
      Delete Scs_Inscripcionguardia Ori
       Where Exists (Select 1
                From Scs_Inscripcionguardia Des
               Where Des.Idinstitucion = Ori.Idinstitucion
                 And Des.Idturno = Ori.Idturno
                 And Des.Idguardia = Ori.Idguardia
                 And Trunc(Des.Fechasuscripcion) = Trunc(Ori.Fechasuscripcion)
                 And Des.Idpersona = p_Idpersona_Destino)
         And Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      Tabla := 'SCS_INSCRIPCIONGUARDIA';
      Update SCS_INSCRIPCIONGUARDIA
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'SCS_GUARDIASTURNO';
      Select Count(1) Into n_registros_enorigen
        From Scs_Guardiasturno
       Where Idpersona_Ultimo = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Scs_Guardiasturno
         Set Idpersona_Ultimo = p_Idpersona_Destino
       Where Idpersona_Ultimo = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_SCS_INSCRIPCION_ULTIMO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'SCS_GRUPOGUARDIACOLEGIADO';
      Select Count(1) Into n_registros_enorigen
        From Scs_Grupoguardiacolegiado
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Scs_Grupoguardiacolegiado
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_SCS_GRUPOGUA_INSCRIPCIONGUA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'SCS_CALENDARIOGUARDIAS';
      Select Count(1) Into n_registros_enorigen
        From Scs_Calendarioguardias
       Where Idpersona_Ultimoanterior = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Scs_Calendarioguardias c
         Set Idpersona_Ultimoanterior = p_Idpersona_Destino
       Where Idpersona_Ultimoanterior = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_CALENDARIO_COLEGIADO_ULTIMO immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    End If;
  
    Tabla := 'SCS_SALTOSCOMPENSACIONES';
    Select Count(1) Into n_registros_enorigen
      From Scs_Saltoscompensaciones
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Saltoscompensaciones
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Select Count(1) Into n_registros_enorigen
      From SCS_CABECERAGUARDIAS
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
      Execute Immediate 'set constraint FK_SCS_PERMUTA_CABECERA deferred';
      Execute Immediate 'set constraint FK_SCS_GUARDIASCOLEGIADO_CABE deferred';
      Execute Immediate 'set constraint FK_FACT_GUARDIASCOLEGIADO_GUAR deferred';
      Execute Immediate 'set constraint FK_FCS_FACT_APUNTE_CABECERA deferred';
      
      Tabla := 'SCS_CABECERAGUARDIAS';
      Update SCS_CABECERAGUARDIAS
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
-- DARA ERROR CUANDO EXISTA TANTO EN EL ORIGEN COMO EN EL DESTINO
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'SCS_PERMUTA_CABECERA';
      Select Count(1) Into n_registros_enorigen
        From Scs_Permuta_Cabecera
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Scs_Permuta_Cabecera
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_SCS_PERMUTA_CABECERA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'SCS_GUARDIASCOLEGIADO';
      Select Count(1) Into n_registros_enorigen
        From SCS_GUARDIASCOLEGIADO
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update SCS_GUARDIASCOLEGIADO
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
-- DARA ERROR CUANDO EXISTA TANTO EN EL ORIGEN COMO EN EL DESTINO
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_SCS_GUARDIASCOLEGIADO_CABE immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'FCS_FACT_GUARDIASCOLEGIADO';
      Select Count(1) Into n_registros_enorigen
        From Fcs_Fact_Guardiascolegiado
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Fcs_Fact_Guardiascolegiado
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_FACT_GUARDIASCOLEGIADO_GUAR immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'FCS_FACT_APUNTE';
      Select Count(1) Into n_registros_enorigen
        From Fcs_Fact_Apunte
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Fcs_Fact_Apunte
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_FCS_FACT_APUNTE_CABECERA immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    End If;
    
    Tabla := 'SCS_ASISTENCIA';
    Select Count(1) Into n_registros_enorigen
      From Scs_Asistencia
     Where Idpersonacolegiado = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Asistencia
       Set Idpersonacolegiado = p_Idpersona_Destino
     Where Idpersonacolegiado = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_FACT_ASISTENCIA';
    Select Count(1) Into n_registros_enorigen
      From Fcs_Fact_Asistencia
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fcs_Fact_Asistencia
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_FACT_ACTUACIONASISTENCIA';
    Select Count(1) Into n_registros_enorigen
      From Fcs_Fact_Actuacionasistencia
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fcs_Fact_Actuacionasistencia
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_FACT_ACTUACIONDESIGNA';
    Select Count(1) Into n_registros_enorigen
      From Fcs_Fact_Actuaciondesigna
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fcs_Fact_Actuaciondesigna
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_MOVIMIENTOSVARIOS';
    Select Count(1) Into n_registros_enorigen
      From Fcs_Movimientosvarios
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fcs_Movimientosvarios
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_FACT_EJG';
    Select Count(1) Into n_registros_enorigen
      From FCS_FACT_EJG
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update FCS_FACT_EJG
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_FACT_SOJ';
    Select Count(1) Into n_registros_enorigen
      From FCS_FACT_SOJ
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update FCS_FACT_SOJ
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'FCS_RETENCIONES_JUDICIALES';
    Select Count(1) Into n_registros_enorigen
      From Fcs_Retenciones_Judiciales
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Fcs_Retenciones_Judiciales
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_ACTUACIONDESIGNA';
    Select Count(1) Into n_registros_enorigen
      From Scs_Actuaciondesigna
     Where Idpersonacolegiado = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Actuaciondesigna
       Set Idpersonacolegiado = p_Idpersona_Destino
     Where Idpersonacolegiado = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_PERMUTAGUARDIAS solicitante';
    Select Count(1) Into n_registros_enorigen
      From Scs_Permutaguardias
     Where Idpersona_Solicitante = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Permutaguardias
       Set Idpersona_Solicitante = p_Idpersona_Destino
     Where Idpersona_Solicitante = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_PERMUTAGUARDIAS confirmador';
    Select Count(1) Into n_registros_enorigen
      From Scs_Permutaguardias
     Where Idpersona_Confirmador = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Permutaguardias
       Set Idpersona_Confirmador = p_Idpersona_Destino
     Where Idpersona_Confirmador = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_CONTRARIOSDESIGNA';
    Select Count(1) Into n_registros_enorigen
      From Scs_Contrariosdesigna
     Where Idabogadocontrario = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Contrariosdesigna
       Set Idabogadocontrario = p_Idpersona_Destino
     Where Idabogadocontrario = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
-- DARA ERROR CUANDO EXISTA TANTO EN EL ORIGEN COMO EN EL DESTINO
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_DESIGNASLETRADO borrado duplicados';
    -- en este caso se pierden datos, como el motivo de renuncia. Pero al menos no son datos visibles
    Select Count(1) Into n_registros_enorigen
      From Scs_Designasletrado Ori
     Where Exists (Select 1
              From Scs_Designasletrado Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idturno = Ori.Idturno
               And Des.Anio = Ori.Anio
               And Des.Numero = Ori.Numero
               And Trunc(Des.Fechadesigna) = Trunc(Ori.Fechadesigna)
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Scs_Designasletrado Ori
     Where Exists (Select 1
              From Scs_Designasletrado Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idturno = Ori.Idturno
               And Des.Anio = Ori.Anio
               And Des.Numero = Ori.Numero
               And Trunc(Des.Fechadesigna) = Trunc(Ori.Fechadesigna)
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'SCS_DESIGNASLETRADO';
    Select Count(1) Into n_registros_enorigen
      From Scs_Designasletrado
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Designasletrado
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_RETENCIONESIRPF borrado duplicados';
    Select Count(1) Into n_registros_enorigen
      From Scs_Retencionesirpf Ori
     Where Exists (Select 1
              From Scs_Retencionesirpf Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idretencion = Ori.Idretencion
               And Trunc(Des.Fechainicio) = Trunc(Ori.Fechainicio)
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Scs_Retencionesirpf Ori
     Where Exists (Select 1
              From Scs_Retencionesirpf Des
             Where Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idretencion = Ori.Idretencion
               And Trunc(Des.Fechainicio) = Trunc(Ori.Fechainicio)
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'SCS_RETENCIONESIRPF';
    Select Count(1) Into n_registros_enorigen
      From Scs_Retencionesirpf
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Retencionesirpf
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_EJG';
    Select Count(1) Into n_registros_enorigen
      From Scs_Ejg
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Ejg
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_SOJ';
    Select Count(1) Into n_registros_enorigen
      From Scs_Soj
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Scs_Soj
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_CARACTASISTENCIA';
    Select Count(1) Into n_registros_enorigen
      From SCS_CARACTASISTENCIA
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update SCS_CARACTASISTENCIA
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_CONTRARIOSEJG';
    Select Count(1) Into n_registros_enorigen
      From SCS_CONTRARIOSEJG
     Where Idabogadocontrarioejg = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update SCS_CONTRARIOSEJG
       Set Idabogadocontrarioejg = p_Idpersona_Destino
     Where Idabogadocontrarioejg = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
-- DARA ERROR CUANDO EXISTA TANTO EN EL ORIGEN COMO EN EL DESTINO
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_CV_DATOSCONTACTOCOLEGIADO';
    Select Count(1) Into n_registros_enorigen
      From SCS_CV_DATOSCONTACTOCOLEGIADO
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update SCS_CV_DATOSCONTACTOCOLEGIADO
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'SCS_CV_GUARDIACOLEGIADO';
    Select Count(1) Into n_registros_enorigen
      From SCS_CV_GUARDIACOLEGIADO
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update SCS_CV_GUARDIACOLEGIADO
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_ESTAT_ENVIO';
    Select Count(1) Into n_registros_enorigen
      From Env_Estat_Envio
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Estat_Envio
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_COMUNICACIONMOROSOS';
    -- aunque contiene idpersona, no se envia la misma factura a dos personas
    Select Count(1) Into n_registros_enorigen
      From Env_Comunicacionmorosos
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Comunicacionmorosos
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_COMPONENTESLISTACORREO borrado duplicados';
    Select Count(1) Into n_registros_enorigen
      From Env_Componenteslistacorreo ori
     Where Exists (Select 1
              From Env_Componenteslistacorreo Des
             Where Des.Idlistacorreo = Ori.Idlistacorreo
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Env_Componenteslistacorreo ori
     Where Exists (Select 1
              From Env_Componenteslistacorreo Des
             Where Des.Idlistacorreo = Ori.Idlistacorreo
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'ENV_COMPONENTESLISTACORREO';
    Select Count(1) Into n_registros_enorigen
      From Env_Componenteslistacorreo
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Componenteslistacorreo
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_PROGRAMIRPF';
    Select Count(1) Into n_registros_enorigen
      From Env_Programirpf
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Programirpf
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_PROGRAMPAGOS';
    Select Count(1) Into n_registros_enorigen
      From Env_Programpagos
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Programpagos
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_PLANTILLAREMITENTES borrado duplicados';
    -- en este caso se pierden datos, como la direccion adjunta. Pero al menos no son datos visibles
    Select Count(1) Into n_registros_enorigen
      From ENV_PLANTILLAREMITENTES ori
     Where Exists (Select 1
              From ENV_PLANTILLAREMITENTES Des
             Where Des.Idtipoenvios = Ori.Idtipoenvios
               And Des.Idplantillaenvios = Ori.Idplantillaenvios
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete ENV_PLANTILLAREMITENTES ori
     Where Exists (Select 1
              From ENV_PLANTILLAREMITENTES Des
             Where Des.Idtipoenvios = Ori.Idtipoenvios
               And Des.Idplantillaenvios = Ori.Idplantillaenvios
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'ENV_PLANTILLAREMITENTES';
    Select Count(1) Into n_registros_enorigen
      From ENV_PLANTILLAREMITENTES
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update ENV_PLANTILLAREMITENTES
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_REMITENTES borrado duplicados';
    -- en este caso se pierden datos, como la direccion adjunta. Pero al menos no son datos visibles
    Select Count(1) Into n_registros_enorigen
      From Env_Remitentes Ori
     Where Exists (Select 1
              From Env_Remitentes Des
             Where Des.Idenvio = Ori.Idenvio
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Env_Remitentes Ori
     Where Exists (Select 1
              From Env_Remitentes Des
             Where Des.Idenvio = Ori.Idenvio
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'ENV_REMITENTES';
    Select Count(1) Into n_registros_enorigen
      From Env_Remitentes
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Remitentes
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
  
    Tabla := 'ENV_TEMP_DESTINATARIOS borrado duplicados';
    -- en este caso se pierden datos, como la direccion adjunta. Pero al menos no son datos visibles
    Select Count(1) Into n_registros_enorigen
      From Env_Temp_Destinatarios Ori
     Where Exists (Select 1
              From Env_Temp_Destinatarios Des
             Where Des.Idenvio = Ori.Idenvio
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Delete Env_Temp_Destinatarios Ori
     Where Exists (Select 1
              From Env_Temp_Destinatarios Des
             Where Des.Idenvio = Ori.Idenvio
               And Des.Idinstitucion = Ori.Idinstitucion
               And Des.Idpersona = p_Idpersona_Destino)
       And Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    Tabla := 'ENV_TEMP_DESTINATARIOS';
    Select Count(1) Into n_registros_enorigen
      From Env_Temp_Destinatarios
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    Update Env_Temp_Destinatarios
       Set Idpersona = p_Idpersona_Destino
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If Sql%Rowcount > 0 Then
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End If;
    End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    Select Count(1) Into n_registros_enorigen
      From Env_Destinatarios
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    
      Execute Immediate 'set constraint FK_ENV_DOCUMENTOSENVIOS_DESTIN deferred';
      Tabla := 'ENV_DESTINATARIOS borrado duplicados';
      -- en este caso se pierden datos, como la direccion adjunta. Pero al menos no son datos visibles
      Delete ENV_DESTINATARIOS ori
       Where Exists (Select 1
                From Env_Destinatarios Des
               Where Des.Idenvio = Ori.Idenvio
                 And Des.Idinstitucion = Ori.Idinstitucion
                 And Des.Idpersona = p_Idpersona_Destino)
         And Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      Tabla := 'ENV_DESTINATARIOS';
      Update Env_Destinatarios
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'ENV_DOCUMENTOSDESTINATARIOS';
      Select Count(1) Into n_registros_enorigen
        From Env_Documentosdestinatarios Ori
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update Env_Documentosdestinatarios Ori
         Set Idpersona   = p_Idpersona_Destino,
             Iddocumento = Iddocumento + Nvl((Select Max(Des.Iddocumento)
                                               From Env_Documentosdestinatarios Des
                                              Where Des.Idpersona = p_Idpersona_Destino
                                                And Des.Idinstitucion = Ori.Idinstitucion
                                                And Des.Idenvio = Ori.Idenvio),
                                             0)
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_ENV_DOCUMENTOSENVIOS_DESTIN immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);

    End If;
    
    Select Count(1) Into n_registros_enorigen
      From Env_Destprograminformes
     Where Idpersona = p_Idpersona_Origen
       And Idinstitucion_Persona = Nvl(p_Idinstitucion, Idinstitucion);
    If n_registros_enorigen > 0 Then
    
      Execute Immediate 'set constraint FK_VALOR_DESTPROGINF deferred';
      Tabla := 'ENV_DESTPROGRAMINFORMES borrado duplicado';
      Delete From Env_Destprograminformes ori
       Where Exists (Select 1
                From Env_Destprograminformes Des
               Where Des.Idprogram = Ori.Idprogram
                 And Des.Idenvio = Ori.Idenvio
                 And Des.Idinstitucion = Ori.Idinstitucion
                 And Des.Idinstitucion_Persona = Ori.Idinstitucion_Persona
                 And Des.Idpersona = p_Idpersona_Destino)
         And Idpersona = p_Idpersona_Origen
         And Idinstitucion_Persona = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
      Tabla := 'ENV_DESTPROGRAMINFORMES';
      Update Env_Destprograminformes
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion_Persona = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
      Tabla := 'ENV_VALORCAMPOCLAVE';
      Select Count(1) Into n_registros_enorigen
        From ENV_VALORCAMPOCLAVE
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion_persona = Nvl(p_Idinstitucion, Idinstitucion);
      If n_registros_enorigen > 0 Then
      Update ENV_VALORCAMPOCLAVE
         Set Idpersona = p_Idpersona_Destino
       Where Idpersona = p_Idpersona_Origen
         And Idinstitucion_persona = Nvl(p_Idinstitucion, Idinstitucion);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      End If;
      Execute Immediate 'set constraint FK_VALOR_DESTPROGINF immediate';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de ' || Tabla);
    
    End If;
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Muevecosasclienteaotrapersona (' || p_Idinstitucion || ', ' ||
                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al mover en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End; --Muevecosasclienteaotrapersona

  --
  -- Copiacliente_Simple
  -- Dados los idpersona de origen y destino y un idinstitucion, Copia los registros en CEN_CLIENTE, CEN_COLEGIADO, CEN_NOCOLEGIADO
  -- * Se suele usar antes de llamar a Muevecosasclienteaotrapersona() y Copiacliente_Simple()
  -- * Esta preparado para aceptar un idinstitucion null y entonces realiza la operacion para todos
  --
  Procedure Copiacliente_Simple(p_Idpersona_Origen  In Cen_Cliente.Idpersona%Type,
                                p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                p_Codretorno        Out Varchar2,
                                p_Datoserror        Out Clob) Is
  Begin
--    p_Datoserror := p_Datoserror || Chr(10) || 'Copiacliente_simple (' || p_Idinstitucion || ', ' ||
--                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): INI';
  
    Tabla := 'CEN_CLIENTE';
    Insert Into Cen_Cliente
      (Idpersona, Idinstitucion, Fechaalta, Caracter, Publicidad, Guiajudicial, Abonosbanco, Cargosbanco, Comisiones, Idtratamiento, Fechamodificacion, Usumodificacion, Idlenguaje, Fotografia, Asientocontable, Letrado, Fechacarga, Fechaactualizacion, Fechaexportcenso, Noenviarrevista, Noaparecerredabogacia)
      (Select p_Idpersona_Destino,
              Idinstitucion,
              Fechaalta,
              Caracter,
              Publicidad,
              Guiajudicial,
              Abonosbanco,
              Cargosbanco,
              Comisiones,
              Idtratamiento,
              Fechamodificacion,
              Usumodificacion,
              Idlenguaje,
              Fotografia,
              Asientocontable,
              Letrado,
              Fechacarga,
              Fechaactualizacion,
              Fechaexportcenso,
              Noenviarrevista,
              Noaparecerredabogacia
         From Cen_Cliente
        Where Idpersona = p_Idpersona_Origen
          And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
          And Not Exists (Select 1
                 From Cen_Cliente
                Where Idpersona = p_Idpersona_Destino
                  And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)));
--    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    Tabla := 'CEN_COLEGIADO';
    Insert Into Cen_Colegiado
      (Idpersona, Idinstitucion, Fechapresentacion, Fechaincorporacion, Indtitulacion, Jubilacioncuota, Situacionejercicio, Situacionresidente, Situacionempresa, Fechamodificacion, Usumodificacion, Comunitario, Ncolegiado, Fechajura, Ncomunitario, Fechatitulacion, Otroscolegios, Fechadeontologia, Fechamovimiento, Idtiposseguro, Cuentacontablesjcs)
      (Select p_Idpersona_Destino,
              Idinstitucion,
              Fechapresentacion,
              Fechaincorporacion,
              Indtitulacion,
              Jubilacioncuota,
              Situacionejercicio,
              Situacionresidente,
              Situacionempresa,
              Fechamodificacion,
              Usumodificacion,
              Comunitario,
              Ncolegiado,
              Fechajura,
              Ncomunitario,
              Fechatitulacion,
              Otroscolegios,
              Fechadeontologia,
              Fechamovimiento,
              Idtiposseguro,
              Cuentacontablesjcs
         From Cen_Colegiado
        Where Idpersona = p_Idpersona_Origen
          And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
          And Not Exists (Select 1
                 From Cen_Colegiado
                Where Idpersona = p_Idpersona_Destino
                  And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)));
--    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    Tabla := 'CEN_NOCOLEGIADO';
    Insert Into Cen_Nocolegiado
      (Idpersona, Idinstitucion, Fechamodificacion, Usumodificacion, Serie, Numeroref, Sociedadsj, Tipo, Anotaciones, Prefijo_Numreg, Contador_Numreg, Sufijo_Numreg, Fechafin, Idpersonanotario, Resena, Objetosocial, Sociedadprofesional, Prefijo_Numsspp, Contador_Numsspp, Sufijo_Numsspp, Nopoliza, Companiaseg)
      (Select p_Idpersona_Destino,
              Idinstitucion,
              Fechamodificacion,
              Usumodificacion,
              Serie,
              Numeroref,
              Sociedadsj,
              Tipo,
              Anotaciones,
              Prefijo_Numreg,
              Contador_Numreg,
              Sufijo_Numreg,
              Fechafin,
              Idpersonanotario,
              Resena,
              Objetosocial,
              Sociedadprofesional,
              Prefijo_Numsspp,
              Contador_Numsspp,
              Sufijo_Numsspp,
              Nopoliza,
              Companiaseg
         From Cen_Nocolegiado
        Where Idpersona = p_Idpersona_Origen
          And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)
          And Not Exists (Select 1
                 From Cen_Nocolegiado
                Where Idpersona = p_Idpersona_Destino
                  And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion)));
--    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
--    p_Datoserror := p_Datoserror || Chr(10) || 'Copiacliente_simple (' || p_Idinstitucion || ', ' ||
--                    p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al insertar en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End; --Copiacliente_simple

  --
  -- Borracliente_Simple
  -- Dados un idpersona y un idinstitucion, Borra los registros en CEN_CLIENTE, CEN_COLEGIADO, CEN_NOCOLEGIADO
  -- * Se suele usar despues de llamar a Copiacliente_Simple() y Muevecosasclienteaotrapersona()
  -- * Esta preparado para aceptar un idinstitucion null y entonces realiza la operacion para todos
  -- * Para que no se pierdan datos, se requiere un idpersona_destino para registrar historico de lo que se borra
  --
  Procedure Borracliente_Simple(p_Idpersona         In Cen_Cliente.Idpersona%Type,
                                p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                p_Codretorno        Out Varchar2,
                                p_Datoserror        Out Clob) Is
  Begin
    p_Datoserror := p_Datoserror || Chr(10) || 'Borracliente_Simple (' || p_Idinstitucion || ', ' ||
                    p_Idpersona || '): INI';
  
    Tabla := 'CEN_NOCOLEGIADO (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                            ' || Chr(10) ||
                'Idpersona                    = ' || Idpersona || Chr(10) ||
                'Idinstitucion                = ' || Idinstitucion || Chr(10) ||
                'Fechamodificacion            = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion              = ' || Usumodificacion || Chr(10) ||
                'Serie                        = ' || Serie || Chr(10) ||
                'Numeroref                    = ' || Numeroref || Chr(10) ||
                'Sociedadsj                   = ' || Sociedadsj || Chr(10) ||
                'Tipo                         = ' || Tipo || Chr(10) ||
                'Anotaciones                  = ' || Anotaciones || Chr(10) ||
                'Prefijo_Numreg               = ' || Prefijo_Numreg || Chr(10) ||
                'Contador_Numreg              = ' || Contador_Numreg || Chr(10) ||
                'Sufijo_Numreg                = ' || Sufijo_Numreg || Chr(10) ||
                'Fechafin                     = ' || Fechafin || Chr(10) ||
                'Idpersonanotario             = ' || Idpersonanotario || Chr(10) ||
                'Resena                       = ' || Resena || Chr(10) ||
                'Objetosocial                 = ' || Objetosocial || Chr(10) ||
                'Sociedadprofesional          = ' || Sociedadprofesional || Chr(10) ||
                'Prefijo_Numsspp              = ' || Prefijo_Numsspp || Chr(10) ||
                'Contador_Numsspp             = ' || Contador_Numsspp || Chr(10) ||
                'Sufijo_Numsspp               = ' || Sufijo_Numsspp || Chr(10) ||
                'Nopoliza                     = ' || Nopoliza || Chr(10) ||
                'Companiaseg                  = ' || Companiaseg,
                '20',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Nocolegiado
          Where Idpersona = p_Idpersona
            And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion));
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_COLEGIADO (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                            ' || Chr(10) ||
                'Idpersona                 = ' || Idpersona || Chr(10) ||
                'Idinstitucion             = ' || Idinstitucion || Chr(10) ||
                'Fechapresentacion         = ' || Fechapresentacion || Chr(10) ||
                'Fechaincorporacion        = ' || Fechaincorporacion || Chr(10) ||
                'Indtitulacion             = ' || Indtitulacion || Chr(10) ||
                'Jubilacioncuota           = ' || Jubilacioncuota || Chr(10) ||
                'Situacionejercicio        = ' || Situacionejercicio || Chr(10) ||
                'Situacionresidente        = ' || Situacionresidente || Chr(10) ||
                'Situacionempresa          = ' || Situacionempresa || Chr(10) ||
                'Fechamodificacion         = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion           = ' || Usumodificacion || Chr(10) ||
                'Comunitario               = ' || Comunitario || Chr(10) ||
                'Ncolegiado                = ' || Ncolegiado || Chr(10) ||
                'Fechajura                 = ' || Fechajura || Chr(10) ||
                'Ncomunitario              = ' || Ncomunitario || Chr(10) ||
                'Fechatitulacion           = ' || Fechatitulacion || Chr(10) ||
                'Otroscolegios             = ' || Otroscolegios || Chr(10) ||
                'Fechadeontologia          = ' || Fechadeontologia || Chr(10) ||
                'Fechamovimiento           = ' || Fechamovimiento || Chr(10) ||
                'Idtiposseguro             = ' || Idtiposseguro || Chr(10) ||
                'Cuentacontablesjcs        = ' || Cuentacontablesjcs || Chr(10) ||
                'Identificadords           = ' || Identificadords || Chr(10) ||
                'Nmutualista               = ' || Nmutualista || Chr(10) ||
                'Numsolicitudcolegiacion   = ' || Numsolicitudcolegiacion,
                '20',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Colegiado
          Where Idpersona = p_Idpersona
            And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion));
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_CLIENTE (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                            ' || Chr(10) ||
                'Idpersona                     = ' || Idpersona || Chr(10) ||
                'Idinstitucion                 = ' || Idinstitucion || Chr(10) ||
                'Fechaalta                     = ' || Fechaalta || Chr(10) ||
                'Caracter                      = ' || Caracter || Chr(10) ||
                'Publicidad                    = ' || Publicidad || Chr(10) ||
                'Guiajudicial                  = ' || Guiajudicial || Chr(10) ||
                'Abonosbanco                   = ' || Abonosbanco || Chr(10) ||
                'Cargosbanco                   = ' || Cargosbanco || Chr(10) ||
                'Comisiones                    = ' || Comisiones || Chr(10) ||
                'Idtratamiento                 = ' || Idtratamiento || Chr(10) ||
                'Fechamodificacion             = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion               = ' || Usumodificacion || Chr(10) ||
                'Idlenguaje                    = ' || Idlenguaje || Chr(10) ||
                'Fotografia                    = ' || Fotografia || Chr(10) ||
                'Asientocontable               = ' || Asientocontable || Chr(10) ||
                'Letrado                       = ' || Letrado || Chr(10) ||
                'Fechacarga                    = ' || Fechacarga || Chr(10) ||
                'Fechaactualizacion            = ' || Fechaactualizacion || Chr(10) ||
                'Fechaexportcenso              = ' || Fechaexportcenso || Chr(10) ||
                'Noenviarrevista               = ' || Noenviarrevista || Chr(10) ||
                'Noaparecerredabogacia         = ' || Noaparecerredabogacia || Chr(10) ||
                'Exportarfoto                  = ' || Exportarfoto,
                '20',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Cliente
          Where Idpersona = p_Idpersona
            And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion));
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_NOCOLEGIADO';
    Delete From Cen_Nocolegiado
     Where Idpersona = p_Idpersona
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    Tabla := 'CEN_COLEGIADO';
    Delete From Cen_Colegiado
     Where Idpersona = p_Idpersona
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    Tabla := 'CEN_CLIENTE';
    Delete From Cen_Cliente
     Where Idpersona = p_Idpersona
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Borracliente_Simple (' || p_Idinstitucion || ', ' ||
                    p_Idpersona || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al borrar en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End; --Borracliente_Simple

  --
  -- Borracolegiado_Simple
  -- Dados un idpersona y un idinstitucion, Borra los registros en CEN_COLEGIADO, CEN_DATOSCOLEGIALESESTADO
  -- * Se usa para pasar a no colegiado
  -- * Esta preparado para aceptar un idinstitucion null y entonces realiza la operacion para todos
  -- * Para que no se pierdan datos, se requiere un idpersona_destino para registrar historico de lo que se borra
  --
  Procedure Borracolegiado_Simple(p_Idpersona         In Cen_Cliente.Idpersona%Type,
                                  p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                  p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                  p_Codretorno        Out Varchar2,
                                  p_Datoserror        Out Clob) Is
  Begin
    p_Datoserror := p_Datoserror || Chr(10) || 'Borracolegiado_Simple (' || p_Idinstitucion || ', ' ||
                    p_Idpersona || '): INI';
  
    Tabla := 'CEN_DATOSCOLEGIALESESTADO (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                            ' || Chr(10) ||
                'Idpersona                 = ' || Idpersona || Chr(10) ||
                'Idinstitucion             = ' || Idinstitucion || Chr(10) ||
                'Fechaestado               = ' || Fechaestado || Chr(10) ||
                'Idestado                  = ' || Idestado || Chr(10) ||
                'Fechamodificacion         = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion           = ' || Usumodificacion || Chr(10) ||
                'Observaciones             = ' || Observaciones,
                '20',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Datoscolegialesestado
          Where Idpersona = p_Idpersona
            And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion));
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_COLEGIADO (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                             ' || Chr(10) ||
                'Idpersona                 = ' || Idpersona || Chr(10) ||
                'Idinstitucion             = ' || Idinstitucion || Chr(10) ||
                'Fechapresentacion         = ' || Fechapresentacion || Chr(10) ||
                'Fechaincorporacion        = ' || Fechaincorporacion || Chr(10) ||
                'Indtitulacion             = ' || Indtitulacion || Chr(10) ||
                'Jubilacioncuota           = ' || Jubilacioncuota || Chr(10) ||
                'Situacionejercicio        = ' || Situacionejercicio || Chr(10) ||
                'Situacionresidente        = ' || Situacionresidente || Chr(10) ||
                'Situacionempresa          = ' || Situacionempresa || Chr(10) ||
                'Fechamodificacion         = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion           = ' || Usumodificacion || Chr(10) ||
                'Comunitario               = ' || Comunitario || Chr(10) ||
                'Ncolegiado                = ' || Ncolegiado || Chr(10) ||
                'Fechajura                 = ' || Fechajura || Chr(10) ||
                'Ncomunitario              = ' || Ncomunitario || Chr(10) ||
                'Fechatitulacion           = ' || Fechatitulacion || Chr(10) ||
                'Otroscolegios             = ' || Otroscolegios || Chr(10) ||
                'Fechadeontologia          = ' || Fechadeontologia || Chr(10) ||
                'Fechamovimiento           = ' || Fechamovimiento || Chr(10) ||
                'Idtiposseguro             = ' || Idtiposseguro || Chr(10) ||
                'Cuentacontablesjcs        = ' || Cuentacontablesjcs || Chr(10) ||
                'Identificadords           = ' || Identificadords || Chr(10) ||
                'Nmutualista               = ' || Nmutualista || Chr(10) ||
                'Numsolicitudcolegiacion   = ' || Numsolicitudcolegiacion,
                '20',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Colegiado
          Where Idpersona = p_Idpersona
            And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion));
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_DATOSCOLEGIALESESTADO';
    Delete From Cen_Datoscolegialesestado
     Where Idpersona = p_Idpersona
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    Tabla := 'CEN_COLEGIADO';
    Delete From Cen_Colegiado
     Where Idpersona = p_Idpersona
       And Idinstitucion = Nvl(p_Idinstitucion, Idinstitucion);
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Borracolegiado_Simple (' || p_Idinstitucion || ', ' ||
                    p_Idpersona || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al borrar en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End; --Borracolegiado_Simple

  --
  -- Borrapersona_Simple
  -- Dado un idpersona, Borra simplemente el registro en CEN_PERSONA
  -- * Para que no se pierdan datos, se requiere un idpersona_destino para registrar historico de lo que se borra
  --
  Procedure Borrapersona_Simple(p_Idpersona         In Cen_Persona.Idpersona%Type,
                                p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                p_Codretorno        Out Varchar2,
                                p_Datoserror        Out Clob) Is
  Begin
    p_Datoserror := p_Datoserror || Chr(10) || 'Borrapersona_simple (' || p_Idpersona || '): INI';
  
    Tabla := 'CEN_PERSONA (auditoria borrado)';
    Begin
      Insert Into Cen_Historico
        (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        (Select Tabla || '                           ' || Chr(10) ||
                'Idpersona                  = ' || Idpersona || Chr(10) ||
                'Nombre                     = ' || Nombre || Chr(10) ||
                'Apellidos1                 = ' || Apellidos1 || Chr(10) ||
                'Apellidos2                 = ' || Apellidos2 || Chr(10) ||
                'Nifcif                     = ' || Nifcif || Chr(10) ||
                'Fechamodificacion          = ' || Fechamodificacion || Chr(10) ||
                'Usumodificacion            = ' || Usumodificacion || Chr(10) ||
                'Idtipoidentificacion       = ' || Idtipoidentificacion || Chr(10) ||
                'Fechanacimiento            = ' || Fechanacimiento || Chr(10) ||
                'Idestadocivil              = ' || Idestadocivil || Chr(10) ||
                'Naturalde                  = ' || Naturalde || Chr(10) ||
                'Fallecido                  = ' || Fallecido || Chr(10) ||
                'Sexo                       = ' || Sexo,
                '10',
                p_Idpersona_Destino,
                2000,
                (Select Nvl(Max(Idhistorico), 0)
                   From Cen_Historico His
                  Where His.Idinstitucion = 2000
                    And His.Idpersona = p_Idpersona_Destino) + Rownum,
                Sysdate,
                Sysdate,
                'Se ha realizado un mantenimiento de duplicados sobre esta persona: ' || Tabla,
                Sysdate,
                0
           From Cen_Persona
          Where Idpersona = p_Idpersona);
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
    Tabla := 'CEN_PERSONA';
    Delete From Cen_Persona Where Idpersona = p_Idpersona;
    p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Borrapersona_simple (' || p_Idpersona || '): FIN';
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: Al borrar en ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Codretorno := -1;
  End; --Borrapersona_simple

  --
  -- Fusiona_personas
  -- Dados los idpersona de origen y destino, Mueve todos los datos del origen al destino.
  -- * Si un cliente de la persona origen existe en el destino, no copia CEN_CLIENTE, CEN_COLEGIADO ni CEN_NOCOLEGIADO
  -- * Todos los demas datos los copia, si es necesario con nuevos ids (como las direcciones)
  -- * Los registros en CEN_ESTADOACTIVIDADPERSONA tambien se copian
  -- * Copia los datos de CEN_CLIENTE, CEN_COLEGIADO, CEN_NOCOLEGIADO y CEN_PERSONA si no existen en el destino.
  -- * Los datos que no se pueden copiar se registran en historico
  -- * Todo el proceso se registra en historico
  --
  Procedure Fusiona_Personas(p_Idpersona_Origen  In Cen_Persona.Idpersona%Type,
                             p_Idpersona_Destino In Cen_Persona.Idpersona%Type,
                             p_Codretorno        Out Varchar2,
                             p_Datoserror        Out Clob) Is
    v_Fechanacimiento Cen_Persona.Fechanacimiento%Type;
    v_Idestadocivil   Cen_Persona.Idestadocivil%Type;
    v_Naturalde       Cen_Persona.Naturalde%Type;
    v_Fallecido       Cen_Persona.Fallecido%Type;
    v_Sexo            Cen_Persona.Sexo%Type;
    
    v_colegiomovido Cen_Cliente.Idinstitucion%Type;
  
    c_CLIENTENOCOLEGIADO Constant Int := 1;
    c_CLIENTESOCIEDAD Constant Int := 2;
    c_CLIENTECOLEGIADO Constant Int := 3;
    v_Tipoclientecgae_Origen Int;
    v_Tipoclientecgae_Destino Int;
    n_Colegiados_Origen Int;
    n_Colegiados_Destino Int;
    Cursor c_Clientes (p_Idorigen Cen_Persona.Idpersona%Type, p_Iddestino Cen_Persona.Idpersona%Type) Is
      Select Ori.Idinstitucion,
             Decode((Select Count(1)
                      From Cen_Colegiado Col
                     Where Col.Idpersona = Ori.Idpersona
                       And Col.Idinstitucion = Ori.Idinstitucion),
                    1,
                    c_CLIENTECOLEGIADO,
                    Decode((Select Count(1)
                             From Cen_Nocolegiado Nocol
                            Where Nocol.Idpersona = Ori.Idpersona
                              And Nocol.Idinstitucion = Ori.Idinstitucion
                              And Nocol.Tipo <> '1'),
                           1,
                           c_CLIENTESOCIEDAD,
                           c_CLIENTENOCOLEGIADO)) As Tipoclienteorigen,
             Decode((Select Count(1)
                      From Cen_Colegiado Col
                     Where Col.Idpersona = p_Iddestino
                       And Col.Idinstitucion = Ori.Idinstitucion),
                    1,
                    c_CLIENTECOLEGIADO,
                    Decode((Select Count(1)
                             From Cen_Nocolegiado Nocol
                            Where Nocol.Idpersona = p_Iddestino
                              And Nocol.Idinstitucion = Ori.Idinstitucion
                              And Nocol.Tipo <> '1'),
                           1,
                           c_CLIENTESOCIEDAD,
                           Decode((Select Count(1)
                                    From Cen_Cliente Nocol
                                   Where Nocol.Idpersona = p_Iddestino
                                     And Nocol.Idinstitucion = Ori.Idinstitucion),
                                  1,
                                  c_CLIENTENOCOLEGIADO,
                                  0))) As Tipoclientedestino
        From Cen_Cliente Ori
       Where Ori.Idpersona = p_Idorigen
       Order By Ori.Idinstitucion;
  
    v_Codretorno Varchar2(10);
    v_Datoserror Clob;
    l_datoserror Number;
    n_datoserror Int;
  
  Begin
    p_Datoserror := p_Datoserror || Chr(10) || 'Fusiona_Personas (' || p_Idpersona_Origen || ' > ' ||
                    p_Idpersona_Destino || '): INI';
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Al principio');
  
    p_Datoserror := p_Datoserror || Chr(10) || 'Copiacliente_simple (';
    p_Datoserror := p_Datoserror || p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || ')';
    -- Para todos los clientes en origen
    For Reg_Cliente In c_Clientes(p_Idpersona_Origen, p_Idpersona_Destino) Loop
      -- Se copian los registros de cliente si no existen ya en destino
      Copiacliente_Simple(p_Idpersona_Origen,
                          p_Idpersona_Destino,
                          Reg_Cliente.Idinstitucion,
                          v_Codretorno,
                          v_Datoserror);
      p_Datoserror := p_Datoserror || Reg_Cliente.Idinstitucion || '(' || v_Datoserror || ')'  || ', ';
      If v_Codretorno <> 0 Then
        p_Codretorno := v_Codretorno;
        Return;
      End If;
    
      -- Se marca si existe colegiado
      If Reg_Cliente.Tipoclienteorigen = c_CLIENTECOLEGIADO Then
        v_colegiomovido := Reg_Cliente.Idinstitucion;
      End If;
    
      -- Comprobando que no se mezcla sociedad con persona fisica en cada colegio
      If Reg_Cliente.Tipoclientedestino > 0 Then
        If (Reg_Cliente.Tipoclienteorigen = c_CLIENTESOCIEDAD And Reg_Cliente.Tipoclientedestino <> c_CLIENTESOCIEDAD) Then
          p_Codretorno := -1;
          p_Datoserror := 'No se pueden mover datos de una Sociedad hacia una persona fisica';
          Return;
        End If;
        If (Reg_Cliente.Tipoclienteorigen = c_CLIENTECOLEGIADO And Reg_Cliente.Tipoclientedestino = c_CLIENTESOCIEDAD) Then
          p_Codretorno := -1;
          p_Datoserror := 'No se puede mezclar un Colegiado con una Sociedad';
          Return;
        End If;
      End If;
    End Loop;
    
    -- Comprobando que no se mezcla sociedad con persona fisica en CGAE
    Begin
      Select Decode((Select Count(1)
                      From Cen_Nocolegiado Nocol
                     Where Nocol.Idpersona = p_Idpersona_Origen
                       And Nocol.Idinstitucion = 2000
                       And Nocol.Tipo <> '1'),
                    1,
                    c_Clientesociedad,
                    Decode((Select Count(1)
                             From Cen_Cliente Cli
                            Where Cli.Idpersona = p_Idpersona_Origen
                              And Cli.Idinstitucion = 2000
                              And Cli.Letrado = '1'),
                           1,
                           c_Clientecolegiado,
                           Decode((Select Count(1)
                                    From Cen_Cliente Nocol
                                   Where Nocol.Idpersona = p_Idpersona_Origen
                                     And Nocol.Idinstitucion = 2000),
                                  1,
                                  c_Clientenocolegiado,
                                  0))),
             Decode((Select Count(1)
                      From Cen_Nocolegiado Nocol
                     Where Nocol.Idpersona = p_Idpersona_Destino
                       And Nocol.Idinstitucion = 2000
                       And Nocol.Tipo <> '1'),
                    1,
                    c_Clientesociedad,
                    Decode((Select Count(1)
                             From Cen_Cliente Cli
                            Where Cli.Idpersona = p_Idpersona_Destino
                              And Cli.Idinstitucion = 2000
                              And Cli.Letrado = '1'),
                           1,
                           c_Clientecolegiado,
                           Decode((Select Count(1)
                                    From Cen_Cliente Nocol
                                   Where Nocol.Idpersona = p_Idpersona_Destino
                                     And Nocol.Idinstitucion = 2000),
                                  1,
                                  c_Clientenocolegiado,
                                  0))),
             (Select Count(1) From Cen_Colegiado Col Where Col.Idpersona = p_Idpersona_Origen),
             (Select Count(1) From Cen_Colegiado Col Where Col.Idpersona = p_Idpersona_Destino)
        Into v_Tipoclientecgae_Origen,
             v_Tipoclientecgae_Destino,
             n_Colegiados_Origen,
             n_Colegiados_Destino
        From Dual;
      
      If v_Tipoclientecgae_Destino = c_Clientesociedad Then
        If n_Colegiados_Origen > 0 Or n_Colegiados_Destino > 0 Then
          p_Codretorno := -1;
          p_Datoserror := 'No se puede mezclar un Colegiado con una Sociedad';
          Return;
        End If;
      Elsif v_Tipoclientecgae_Origen = c_Clientesociedad Then
        p_Codretorno := -1;
        p_Datoserror := 'No se pueden mover datos de una Sociedad hacia una persona fisica';
        Return;
      End If;
    End;
    
    Begin
      -- Aseguramos que exista el cliente en el CGAE
      Tabla := 'CEN_CLIENTE insercion en CGAE';
      Insert Into Cen_Cliente
        (Idpersona, Idinstitucion, Fechaalta, Caracter, Publicidad, Guiajudicial, Abonosbanco, Cargosbanco, Comisiones, Idtratamiento, Fechamodificacion, Usumodificacion, Idlenguaje)
        (Select Idpersona, 2000, Sysdate, 'U', '0', '0', '0', '0', '0', '1', Sysdate, 0, '1'
           From Cen_Cliente Cli
          Where Cli.Idpersona = p_Idpersona_Destino
            And Not Exists (Select *
                   From Cen_Cliente Cli2
                  Where Cli2.Idpersona = Cli.Idpersona
                    And Cli2.Idinstitucion = 2000)
            And Rownum = 1);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
      
      Tabla := 'CEN_NOCOLEGIADO insercion en CGAE';
      Insert Into Cen_Nocolegiado
        (Idpersona, Idinstitucion, Fechamodificacion, Usumodificacion, Sociedadsj, Tipo)
        (Select Idpersona, 2000, Sysdate, 0, Sociedadsj, Tipo
           From Cen_Nocolegiado Cli
          Where Cli.Idpersona = p_Idpersona_Destino
            And Not Exists (Select *
                   From Cen_Nocolegiado Nocol
                  Where Nocol.Idpersona = Cli.Idpersona
                    And Nocol.Idinstitucion = 2000)
            And Rownum = 1);
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
    End;
  
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Antes de copiar cosas');
    -- Se mueve lo maximo posible de los registros de tablas a la nueva persona
    Muevecosaspersonaaotrapersona(p_Idpersona_Origen,
                                  p_Idpersona_Destino,
                                  v_Codretorno,
                                  v_Datoserror);
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
      Return;
    End If;
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
    Muevecosasclienteaotrapersona(p_Idpersona_Origen,
                                  p_Idpersona_Destino,
                                  Null, --todos a la vez
                                  v_Codretorno,
                                  v_Datoserror);
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
      Return;
    End If;
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Despues de copiar cosas');
      
    -- Se borran los clientes del origen
    Borracliente_Simple(p_Idpersona_Origen,
                        p_Idpersona_Destino,
                        Null, --todos a la vez
                        v_Codretorno,
                        v_Datoserror);
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      Return;
    End If;
  
    -- Si se movio alguna colegiacion, habra que actualizar los datos en los Consejos
    If v_colegiomovido Is Not Null Then
      Pkg_Siga_Censo.Actualizardatosletrado(p_Idpersona_Destino,
                                            v_colegiomovido,
                                            '30',
                                            1,
                                            '-7',
                                            v_Codretorno,
                                            v_Datoserror);
      p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
      If v_Codretorno Not In (0, -2) Then -- -2: cuando es no colegiado
        p_Codretorno := v_Codretorno;
        Return;
      End If;
    End If;
  
    --TODO: copiar datos de cliente, colegiado, no colegiado
  
    -- Se copian todos los datos que sea posible, generales de la persona
    Begin
      p_Datoserror := p_Datoserror || Chr(10);
      p_Datoserror := p_Datoserror || Chr(10) || 'Actualizando datos de Persona (' ||
                      p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): INI';
    
      Tabla := 'CEN_ESTADOACTIVIDADPERSONA';
      Update Cen_Estadoactividadpersona
         Set Idpersona = p_Idpersona_Destino,
             Idcodigo  = (Select Nvl(Max(Idcodigo), 0)
                            From Cen_Estadoactividadpersona
                           Where Idpersona = p_Idpersona_Destino) + Rownum
       Where Idpersona = p_Idpersona_Origen;
      If Sql%Rowcount > 0 Then
        p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
      End If;
    
      Tabla := 'CEN_PERSONA';
      Select Nvl(p_Destino.Fechanacimiento, p_Origen.Fechanacimiento),
             Nvl(p_Destino.Idestadocivil, p_Origen.Idestadocivil),
             Nvl(p_Destino.Naturalde, p_Origen.Naturalde),
             Greatest(p_Destino.Fallecido, p_Origen.Fallecido),
             Nvl(p_Destino.Sexo, p_Origen.Sexo)
        Into v_Fechanacimiento, v_Idestadocivil, v_Naturalde, v_Fallecido, v_Sexo
        From Cen_Persona p_Origen, Cen_Persona p_Destino
       Where p_Origen.Idpersona = p_Idpersona_Origen
         And p_Destino.Idpersona = p_Idpersona_Destino;
    
      Update Cen_Persona
         Set Fechanacimiento = v_Fechanacimiento,
             Idestadocivil   = v_Idestadocivil,
             Naturalde       = v_Naturalde,
             Fallecido       = v_Fallecido,
             Sexo            = v_Sexo
       Where Idpersona = p_Idpersona_Destino;
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    
      p_Datoserror := p_Datoserror || Chr(10) || 'Actualizando datos de Persona (' ||
                      p_Idpersona_Origen || ' > ' || p_Idpersona_Destino || '): FIN';
    
      Borrapersona_Simple(p_Idpersona_Origen, p_Idpersona_Destino, v_Codretorno, v_Datoserror);
      p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
      If v_Codretorno <> 0 Then
        p_Codretorno := v_Codretorno;
        Return;
      End If;
    End;
  
    p_Datoserror := p_Datoserror || Chr(10);
    p_Datoserror := p_Datoserror || Chr(10) || 'Fusiona_Personas (' || p_Idpersona_Origen || ' > ' ||
                    p_Idpersona_Destino || '): FIN';
  
    -- Se registra finalmente el log completo
    Begin
      l_datoserror := length(p_datoserror);
      n_datoserror := trunc(l_datoserror / 4001);
      For i In 0..n_datoserror Loop
        Insert Into Cen_Historico
          (Descripcion, Idtipocambio, Idpersona, Idinstitucion, Idhistorico, Fechaentrada, Fechaefectiva, Motivo, Fechamodificacion, Usumodificacion)
        Values
          (substr(p_Datoserror, 1+i*4000, 4000), '10', p_Idpersona_Destino, 2000, (Select Nvl(Max(Idhistorico), 0)
               From Cen_Historico His
              Where His.Idinstitucion = 2000
                And His.Idpersona = p_Idpersona_Destino) + 1, Sysdate, Sysdate, 'Se ha realizado un mantenimiento de duplicados sobre esta persona', Sysdate, 0);
      End Loop;
    Exception
      When No_Data_Found Then
        Null;
      When Others Then
        p_Datoserror := p_Datoserror || Chr(10) || Sqlerrm;
        p_Codretorno := Sqlcode;
    End;
  
--dbms_output.put_line (to_char(Sysdate, 'mi:ss') || ': Al final');
    p_Codretorno := 0;
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || Sqlcode || ': ' || Sqlerrm;
      p_Codretorno := -1;
    
  End; --Fusiona_personas

  --
  -- Mueveclienteaotrapersona
  -- Dados los idpersona de origen y destino y un idinstitucion, Mueve todos los datos de un cliente del origen al destino.
  -- No es recomendable usarlo salvo en casos excepcionales. Es mejor usar Fusiona_Personas para mover todo.
  -- * Si un cliente de la persona origen existe en el destino, no copia CEN_CLIENTE, CEN_COLEGIADO ni CEN_NOCOLEGIADO
  -- * Todos los demas datos los copia, si es necesario con nuevos ids (como las direcciones)
  -- * Copia los datos de CEN_CLIENTE, CEN_COLEGIADO, CEN_NOCOLEGIADO si no existen en el destino.
  -- * Los datos que no se pueden copiar se registran en historico
  -- * Todo el proceso se registra en historico
  --
  Procedure Mueveclienteaotrapersona(p_Idpersona_Origen  In Cen_Cliente.Idpersona%Type,
                                     p_Idpersona_Destino In Cen_Cliente.Idpersona%Type,
                                     p_Idinstitucion     In Cen_Cliente.Idinstitucion%Type,
                                     p_Codretorno        Out Varchar2,
                                     p_Datoserror        Out Clob) Is
    v_Codretorno      Varchar2(10);
    v_Datoserror      Clob;
  Begin
  
    Begin
      Copiacliente_Simple(p_Idpersona_Origen,
                          p_Idpersona_Destino,
                          p_Idinstitucion,
                          v_Codretorno,
                          v_Datoserror);
      p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
      If v_Codretorno <> 0 Then
        p_Codretorno := v_Codretorno;
        Return;
      End If;
    End;
  
    Muevecosasclienteaotrapersona(p_Idpersona_Origen,
                                  p_Idpersona_Destino,
                                  p_Idinstitucion,
                                  v_Codretorno,
                                  v_Datoserror);
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      Return;
    End If;
  
    Borracliente_Simple(p_Idpersona_Origen,
                        p_Idpersona_Destino,
                        p_Idinstitucion,
                        v_Codretorno,
                        v_Datoserror);
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      Return;
    End If;
  
  End; --Mueveclienteaotrapersona

  --
  --PasaAnoColegiado
  --  Elimina todos los colegiados y clientes correspondientes una persona,
  --a excepcion del cliente en CGAE e IT.
  --Luego, a estos clientes le cambia el check de Letrado y, finalmente
  --anyade el registro a CEN_NOCOLEGIADO
  --
  Procedure Pasaanocolegiado(p_Idpersona  Cen_Persona.Idpersona%Type,
                             p_Codretorno Out Varchar2,
                             p_Datoserror Out Clob) Is
  
    Cursor c_Clientes_Ambiguos Is
      Select Idinstitucion
        From Cen_Cliente Cli
       Where Cli.Idpersona = p_Idpersona
         And Not Exists (Select *
                From Cen_Colegiado Col
               Where Col.Idpersona = Cli.Idpersona
                 And Col.Idinstitucion = Cli.Idinstitucion)
         And Not Exists (Select *
                From Cen_Nocolegiado Col
               Where Col.Idpersona = Cli.Idpersona
                 And Col.Idinstitucion = Cli.Idinstitucion);
  
    v_Codretorno Varchar2(100);
    v_Datoserror Clob;
  
  Begin
  
    p_Datoserror := p_Datoserror || Chr(10) || 'PasaAnoColegiado (' || p_Idpersona || '): INI';
  
    Borracolegiado_Simple(p_Idpersona, p_Idpersona, Null, v_Codretorno, v_Datoserror);
    p_Datoserror := p_Datoserror || Chr(10) || v_Datoserror;
    If v_Codretorno <> 0 Then
      p_Codretorno := v_Codretorno;
      Return;
    End If;
  
    Tabla := 'CEN_CLIENTE';
    Update Cen_Cliente Set Letrado = '0' Where Idpersona = p_Idpersona;
    p_Datoserror := p_Datoserror || Chr(10) || 'INF: Actualizados "LETRADO" en ' || Tabla || ': ' ||
                    Sql%Rowcount;
  
    For Reg In c_Clientes_Ambiguos Loop
      Tabla := 'CEN_NOCOLEGIADO';
      Insert Into Cen_Nocolegiado
        (Idpersona, Idinstitucion, Fechamodificacion, Usumodificacion, Sociedadsj, Tipo)
      Values
        (p_Idpersona, Reg.Idinstitucion, Sysdate, 0, '0', '1');
      p_Datoserror := p_Datoserror || Chr(10) || Tabla || ': ' || Sql%Rowcount;
    End Loop;
  
    p_Datoserror := p_Datoserror || Chr(10) || 'PasaAnoColegiado (' || p_Idpersona || '): FIN';
    p_Datoserror := p_Datoserror || Chr(10) || ' ';
  
  Exception
    When Others Then
      p_Datoserror := p_Datoserror || Chr(10) || 'ERR: ' || Tabla || ': ' || Sqlcode || ': ' ||
                      Sqlerrm;
      p_Datoserror := p_Datoserror || Chr(10) || 'PasaAnoColegiado (' || p_Idpersona || '): FIN';
      p_Datoserror := p_Datoserror || Chr(10) || ' ';
  End; --PasaAnoColegiado

End Pkg_Siga_Fusion_Personas;
/
