CREATE OR REPLACE TRIGGER "CEN_DIRECCIONES_AUR"  
  AFTER UPDATE ON CEN_DIRECCIONES
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW
Declare
  v_Cont            Number;
  v_Numerocolegiado Cen_Colegiado.Ncolegiado%Type;
  v_Nombrecolegiado Varchar2(300);

  activocolegio           GEN_PARAMETROS.Valor%Type;
  e exception;

Begin
--Comprobamos que es servicio centralita virtual este activo para 0 y la institucion.
--si no esta activo no hacemos nada

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
  If (activocolegio = 0) Then
    raise e;
  end if;

  If (:New.Telefono1 Is Not Null And :Old.Telefono1 Is Null) Or
     (:New.Telefono1 Is Null And :Old.Telefono1 Is Not Null) Or (:New.Telefono1 <> :Old.Telefono1) Or
    
     (:New.Telefono2 Is Not Null And :Old.Telefono2 Is Null) Or
     (:New.Telefono2 Is Null And :Old.Telefono2 Is Not Null) Or (:New.Telefono2 <> :Old.Telefono2) Or
    
     (:New.Movil Is Not Null And :Old.Movil Is Null) Or
     (:New.Movil Is Null And :Old.Movil Is Not Null) Or (:New.Movil <> :Old.Movil) Or
    
     (:New.Correoelectronico Is Not Null And :Old.Correoelectronico Is Null) Or
     (:New.Correoelectronico Is Null And :Old.Correoelectronico Is Not Null) Or
     (:New.Correoelectronico <> :Old.Correoelectronico) Then
  
    -- buscando si...
    Select Count(*)
      Into v_Cont
      From Scs_Guardiascolegiado       Gc,
           Scs_Guardiasturno           Gt,
           Cen_Colegiado               c,
           Cen_Direccion_Tipodireccion Dt
    -- esta persona es colegiado...
     Where c.Idinstitucion = Dt.Idinstitucion
       And c.Idpersona = Dt.Idpersona
       And Gc.Idinstitucion = c.Idinstitucion
       And Gc.Idpersona = c.Idpersona
       And Gt.Idinstitucion = Gc.Idinstitucion
       And Gt.Idturno = Gc.Idturno
       And Gt.Idguardia = Gc.Idguardia
          
       And Dt.Idpersona = :New.Idpersona
       And Dt.Iddireccion = :New.Iddireccion
       And Dt.Idinstitucion = :New.Idinstitucion
          
          -- y tiene una guardia futura...
       And Gc.Fechainicio >= trunc(Sysdate)
          -- que pertenece a centralita...
       And Gt.Enviocentralita = 1
          -- y el tipo de esta direccion es de guardia
       And Dt.Idtipodireccion = 6;
  
    If (v_Cont > 0) Then
      Select f_Siga_Calculoncolegiado(:New.Idinstitucion, :New.Idpersona)
        Into v_Numerocolegiado
        From Dual;
      Select Substr(f_Siga_Getnombre_Colegiado(:New.Idinstitucion, :New.Idpersona, 0), 1, 300)
        Into v_Nombrecolegiado
        From Dual;
    
      Insert Into Ecom_Cola
        (Idecomcola, Idinstitucion, Idestadocola, Idoperacion, Reintento, Fechacreacion,
         Fechamodificacion, Usumodificacion)
      Values
        (seq_ecom_cola.Nextval, :New.Idinstitucion, 1, 34, 0, Sysdate,
         Sysdate, 0);
    
      Insert Into Ecom_Datoscontactocolegiado
        (Iddatoscontactocolegiado, Idinstitucion, Numerocolegiado, Nombrecolegiado, Telefono1,
         Telefono2, Telefonomovil, Email, Idecomcola)
      Values
        ((Select Nvl(Max(Iddatoscontactocolegiado), 0) + 1 From Ecom_Datoscontactocolegiado),
         :New.Idinstitucion, v_Numerocolegiado, v_Nombrecolegiado, :New.Telefono1, :New.Telefono2,
         :New.Movil, :New.Correoelectronico, seq_ecom_cola.Currval);
    
      Insert Into Scs_Cv_Datoscontactocolegiado
        (Idscsdatoscontactocolegiado, Idpersona, Idinstitucion, Fecharecepcion, Numerocolegiado,
         Nombrecolegiado, Telefono1, Telefono2, Telefonomovil, Email)
      Values
        ((Select Nvl(Max(Idscsdatoscontactocolegiado), 0) + 1 From Scs_Cv_Datoscontactocolegiado),
         :New.Idpersona, :New.Idinstitucion, Sysdate, v_Numerocolegiado, v_Nombrecolegiado,
         :New.Telefono1, :New.Telefono2, :New.Movil, :New.Correoelectronico);
    
    End If;
  End If;
 exception when e then null;
End Cen_Direcciones_Aur;
/
