CREATE OR REPLACE TRIGGER CEN_DIRECCION_TIPODIR_AIR
  AFTER Insert ON CEN_DIRECCION_TIPODIRECCION
  REFERENCING NEW AS New
  FOR EACH Row
  
when (NEW.idtipodireccion = 6)
Declare
  v_Numerocolegiado Cen_Colegiado.Ncolegiado%Type;
  v_Nombrecolegiado Varchar2(300);

  v_Telefono1         Cen_Direcciones.Telefono1%Type;
  v_Telefono2         Cen_Direcciones.Telefono2%Type;
  v_Movil             Cen_Direcciones.Movil%Type;
  v_Correoelectronico Cen_Direcciones.Correoelectronico%Type;
activogeneral           GEN_PARAMETROS.Valor%Type;
  activocolegio           GEN_PARAMETROS.Valor%Type;
  e exception;

Begin
--Comprobamos que es servicio centralita virtual este activo para 0 y la institucion.
--si no esta activo no hacemos nada
  Begin
    select VALOR
      into activogeneral
      from GEN_PARAMETROS
     where IDINSTITUCION = 0
       and MODULO = 'ECOM'
       and PARAMETRO = 'CENTRALITAVIRTUAL_ACTIVO';
  exception
    when others then
      activogeneral := 0;
  end;
  Begin
    select VALOR
      into activocolegio
      from GEN_PARAMETROS
     where IDINSTITUCION = :New.Idinstitucion
       and MODULO = 'ECOM'
       and PARAMETRO = 'CENTRALITAVIRTUAL_ACTIVO';
  exception
    when others then
      activocolegio := 0;
  end;
  If (activogeneral = 0 or activocolegio = 0) Then
    raise e;
  end if;

  begin
  -- buscando si...
  Select Substr(f_Siga_Getnombre_Colegiado(:New.Idinstitucion, :New.Idpersona, 0), 1, 300),
         Decode(c.Comunitario, 1, c.Ncomunitario, c.Ncolegiado),
         Dir.Telefono1,
         Dir.Telefono2,
         Dir.Movil,
         Dir.Correoelectronico
    Into v_Nombrecolegiado,
         v_Numerocolegiado,
         v_Telefono1,
         v_Telefono2,
         v_Movil,
         v_Correoelectronico
    From Cen_Colegiado c, Cen_Direcciones Dir
  -- esta persona es colegiado...
   Where c.Idinstitucion = Dir.Idinstitucion
     And c.Idpersona = Dir.Idpersona
        
     And Dir.Idinstitucion = :New.Idinstitucion
     And Dir.Idpersona = :New.Idpersona
     And Dir.Iddireccion = :New.Iddireccion
        
     And Exists (Select *
            From Scs_Guardiascolegiado Gc, Scs_Guardiasturno Gt
           Where Gc.Idinstitucion = c.Idinstitucion
             And Gc.Idpersona = c.Idpersona
             And Gt.Idinstitucion = Gc.Idinstitucion
             And Gt.Idturno = Gc.Idturno
             And Gt.Idguardia = Gc.Idguardia
                -- y tiene una guardia futura...
             And Gc.Fechainicio >= Trunc(Sysdate)
                -- que pertenece a centralita...
             And Gt.Enviocentralita = 1);
  exception
  when others then
    v_Telefono1 := null;
  end;

  If (v_Telefono1 Is Not Null) Then
    Insert Into Ecom_Cola
      (Idecomcola,
       Idinstitucion,
       Idestadocola,
       Idoperacion,
       Reintento,
       Fechacreacion,
       Fechamodificacion,
       Usumodificacion)
    Values
      (Seq_Ecom_Cola.Nextval, :New.Idinstitucion, 1, 34, 0, Sysdate, Sysdate, 0);
  
    Insert Into Ecom_Datoscontactocolegiado
      (Iddatoscontactocolegiado,
       Idinstitucion,
       Numerocolegiado,
       Nombrecolegiado,
       Telefono1,
       Telefono2,
       Telefonomovil,
       Email,
       Idecomcola)
    Values
      ((Select Nvl(Max(Iddatoscontactocolegiado), 0) + 1 From Ecom_Datoscontactocolegiado),
       :New.Idinstitucion,
       v_Numerocolegiado,
       v_Nombrecolegiado,
       v_Telefono1,
       v_Telefono2,
       v_Movil,
       v_Correoelectronico,
       Seq_Ecom_Cola.Currval);
  
    Insert Into Scs_Cv_Datoscontactocolegiado
      (Idscsdatoscontactocolegiado,
       Idpersona,
       Idinstitucion,
       Fecharecepcion,
       Numerocolegiado,
       Nombrecolegiado,
       Telefono1,
       Telefono2,
       Telefonomovil,
       Email)
    Values
      ((Select Nvl(Max(Idscsdatoscontactocolegiado), 0) + 1 From Scs_Cv_Datoscontactocolegiado),
       :New.Idpersona,
       :New.Idinstitucion,
       Sysdate,
       v_Numerocolegiado,
       v_Nombrecolegiado,
       v_Telefono1,
       v_Telefono2,
       v_Movil,
       v_Correoelectronico);
  End If;
  exception when e then null;
End Cen_Direcciones_Aur;
/
