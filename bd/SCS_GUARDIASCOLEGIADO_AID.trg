CREATE OR REPLACE TRIGGER "SCS_GUARDIASCOLEGIADO_AID"  
  AFTER Insert Or Delete ON SCS_GUARDIASCOLEGIADO
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW
Declare
  v_Enviocentralita  Scs_Guardiasturno.Enviocentralita%Type;
  v_Ecomidguardiacol Ecom_Guardiacolegiado.Idecomguardiacolegiado%Type;
  v_Scsidguardiacol  Scs_Cv_Guardiacolegiado.Idscsguardiacolegiado%Type;

  r_Guardiacolegiado Scs_Guardiascolegiado%Rowtype;
  v_Accion           Ecom_Guardiacolegiado.Accion%Type;
  activocolegio           GEN_PARAMETROS.Valor%Type;
  e exception;

Begin

  
  If (:Old.Idinstitucion Is Null) Then
    -- insert
    r_Guardiacolegiado.Idinstitucion := :New.Idinstitucion;
    r_Guardiacolegiado.Idturno       := :New.Idturno;
    r_Guardiacolegiado.Idguardia     := :New.Idguardia;
    r_Guardiacolegiado.Idpersona     := :New.Idpersona;
    r_Guardiacolegiado.Fechainicio   := :New.Fechainicio;
    r_Guardiacolegiado.Fechafin   := :New.Fechafin;
    v_Accion                         := 1;
  Else
    -- delete
    r_Guardiacolegiado.Idinstitucion := :Old.Idinstitucion;
    r_Guardiacolegiado.Idturno       := :Old.Idturno;
    r_Guardiacolegiado.Idguardia     := :Old.Idguardia;
    r_Guardiacolegiado.Idpersona     := :Old.Idpersona;
    r_Guardiacolegiado.Fechainicio   := :Old.Fechainicio;
    r_Guardiacolegiado.Fechafin   := :Old.Fechafin;
    v_Accion                         := 2;
  End If;
  --Comprobamos que es servicio centralita virtual este activo para 0 y la institucion.
--si no esta activo no hacemos nada
  
  Begin
    select VALOR
      into activocolegio
      from GEN_PARAMETROS
     where IDINSTITUCION = r_Guardiacolegiado.Idinstitucion
       and MODULO = 'ECOM'
       and PARAMETRO = 'CENTRALITAVIRTUAL_ACTIVO';
  exception
    when others then
      activocolegio := 0;
  end;
  If (activocolegio = 0) Then
    raise e;
  end if;

  -- obteniendo si la guardia es para envio a la centralita
  Select Gt.Enviocentralita
    Into v_Enviocentralita
    From Scs_Guardiasturno Gt
   Where Gt.Idinstitucion = r_Guardiacolegiado.Idinstitucion
     And Gt.Idturno = r_Guardiacolegiado.Idturno
     And Gt.Idguardia = r_Guardiacolegiado.Idguardia;

  If (v_Enviocentralita = '1') Then
  
    -- insertando en las tablas necesarias para comunicar a la centralita
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
      (Seq_Ecom_Cola.Nextval, r_Guardiacolegiado.Idinstitucion, 1, 35, 0, Sysdate, Sysdate, 0);
  
    Select Nvl(Max(Idecomguardiacolegiado), 0) + 1
      Into v_Ecomidguardiacol
      From Ecom_Guardiacolegiado;
    Insert Into Ecom_Guardiacolegiado
      (Idecomguardiacolegiado,
       Idinstitucion,
       Idguardia,
       Fechaguardia,
       Nombreguardia,
       Ordenguardia,
       Numerocolegiado,
       Nombrecolegiado,
       Telefono1,
       Telefono2,
       Telefonomovil,
       Email,
       Accion,
       Idecomcola)
      (Select v_Ecomidguardiacol,
              r_Guardiacolegiado.Idinstitucion,
              r_Guardiacolegiado.Idguardia,
              r_Guardiacolegiado.Fechafin,
              Gt.Nombre,
              Cg.Posicion,
              f_Siga_Calculoncolegiado(r_Guardiacolegiado.Idinstitucion,
                                       r_Guardiacolegiado.Idpersona),
              f_Siga_Getnombre_Colegiado(r_Guardiacolegiado.Idinstitucion,
                                         r_Guardiacolegiado.Idpersona,
                                         0),
              Nvl(f_Siga_Getdireccioncliente(r_Guardiacolegiado.Idinstitucion,
                                             r_Guardiacolegiado.Idpersona,
                                             6,
                                             11),
                  '000000000'),
              -- En el caso de que el telefono1 este vacio, lo rellenamos para que no de error al insertar en Ecom_Guardiacolegiado
              -- Esto valdria para el caso del delete. Si pasa en el insert, la incidencia ya estaria controlada en CV
              f_Siga_Getdireccioncliente(r_Guardiacolegiado.Idinstitucion,
                                         r_Guardiacolegiado.Idpersona,
                                         6,
                                         12),
              f_Siga_Getdireccioncliente(r_Guardiacolegiado.Idinstitucion,
                                         r_Guardiacolegiado.Idpersona,
                                         6,
                                         13),
              f_Siga_Getdireccioncliente(r_Guardiacolegiado.Idinstitucion,
                                         r_Guardiacolegiado.Idpersona,
                                         6,
                                         16),
              v_Accion,
              Seq_Ecom_Cola.Currval
         From Scs_Guardiasturno Gt, Scs_Cabeceraguardias Cg
        Where Cg.Idinstitucion = Gt.Idinstitucion
          And Cg.Idturno = Gt.Idturno
          And Cg.Idguardia = Gt.Idguardia
          And Cg.Idinstitucion = r_Guardiacolegiado.Idinstitucion
          And Cg.Idturno = r_Guardiacolegiado.Idturno
          And Cg.Idguardia = r_Guardiacolegiado.Idguardia
          And Cg.Idpersona = r_Guardiacolegiado.Idpersona
          And Cg.Fechainicio = r_Guardiacolegiado.Fechainicio);
  
    Select Nvl(Max(Idscsguardiacolegiado) + 1, 0)
      Into v_Scsidguardiacol
      From Scs_Cv_Guardiacolegiado;
    Insert Into Scs_Cv_Guardiacolegiado
      (Idscsguardiacolegiado,
       Idinstitucion,
       Idguardia,
       Fechaguardia,
       Nombreguardia,
       Ordenguardia,
       Fecharecepcion,
       Numerocolegiado,
       Nombrecolegiado,
       Telefono1,
       Telefono2,
       Telefonomovil,
       Email,
       Accion,
       Idturno,
       Idpersona)
    
      (Select v_Scsidguardiacol,
              Egc.Idinstitucion,
              Egc.Idguardia,
              Egc.Fechaguardia,
              Egc.Nombreguardia,
              Egc.Ordenguardia,
              Sysdate,
              Egc.Numerocolegiado,
              Egc.Nombrecolegiado,
              Egc.Telefono1,
              Egc.Telefono2,
              Egc.Telefonomovil,
              Egc.Email,
              v_Accion,
              r_Guardiacolegiado.Idturno,
              r_Guardiacolegiado.Idpersona
         From Ecom_Guardiacolegiado Egc
        Where Egc.Idecomguardiacolegiado = v_Ecomidguardiacol);
  
  End If;
  exception when e then null;
End Scs_Guardiascolegiado_Aid;
