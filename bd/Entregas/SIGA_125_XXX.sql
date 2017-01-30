Update Adm_Gestorinterfaz ges Set ges.Color = '0' Where ges.Color <> '0';
Update Gen_Procesos Set Descripcion = 'HIDDEN_' || Descripcion Where Idproceso = '80' and Descripcion not like 'HIDDEN%';
Delete Adm_Tiposacceso acc Where acc.Idproceso = '80' And acc.Idperfil <> 'ADG';

-- Ejecutado en Integracion por AAG el 17/01 a las 10:30

-- 125_002

Declare
  Cursor c_Aux Is
    Select Distinct Idinstitucion, Idperfil
      From Adm_Tiposacceso sub
     Where Idproceso In (Select Men.Idproceso
                           From Gen_Menu Men
                          Where Idmenu In ('7', '17', '19', '15E', '15F', '99E'))
       And Derechoacceso > 1
       And Not Exists (Select 1
              From Adm_Tiposacceso super
             Where sub.Idperfil = super.Idperfil
               And sub.Idinstitucion = super.Idinstitucion
               And super.Idproceso = '180');
  n_permisosanyadidos Number;

Begin
  n_permisosanyadidos := 0;
  For Rec In c_Aux Loop
    Begin
      Insert Into Adm_Tiposacceso
        (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
      Values
        ('180', Rec.Idperfil, Sysdate, 0, 3, Rec.Idinstitucion);
      n_permisosanyadidos := n_permisosanyadidos + sql%rowcount;
      Dbms_Output.Put_Line('Insertado: ' || Rec.Idperfil || ', ' || Rec.Idinstitucion);
    
    Exception
      When Others Then
        Dbms_Output.Put_Line('Error=' || Sqlerrm);
        Rollback;
    End;
  End Loop;
  dbms_output.put_line('Total: ' || n_permisosanyadidos);
End;
/

-- Ejecutado en Integracion por AAG el 18/01 a las 13:30

-- 125_003

create index SI_MJUCOLEGIADOERRORIDPERSONA on MJU_COLEGIADO_ERROR (IDPERSONA);
create index SI_MUTUAL_ENVIO_CERT_IDPERSONA on ECOM_MUTUALIDAD_CERTIFICADOS (IDPERSONA);
create index SI_CER_SOLCER_IDPERSONADIR on CER_SOLICITUDCERTIFICADOS (Idpersona_Dir);
create index SI_CEN_SOLICITUDINC_IDPERSONA on CEN_SOLICITUDINCORPORACION (IDPERSONA);
create index SI_FAC_ABONO_IDPERSONADEUDOR on FAC_ABONO (IDPERSONADEUDOR);
create index SI_CEN_MEDIADOR_CSV_IDPERSONA on CEN_MEDIADOR_CSV (IDPERSONA);
create index SI_CEN_MEDIADOR_XML on CEN_MEDIADOR_XML (IDPERSONA);
create index SI_CEN_SOLICITUDALT_IDPERSONA on CEN_SOLICITUDALTER (IDPERSONA);
create index SI_ECOM_COMURESOLAJG_IDPERSONA on ECOM_COMUNICACIONRESOLUCIONAJG (IDPERSONA);
create index SI_ECOM_SOLIMPUGRESOLAJG_IDPER on ECOM_SOLIMPUGRESOLUCIONAJG (IDPERSONA);
create index SI_SCS_CONTRARDESIGN_IDABOGADO on SCS_CONTRARIOSDESIGNA (Idabogadocontrario);
create index SI_SCS_CARACTASIST_IDPERSONA on SCS_CARACTASISTENCIA (IDPERSONA);
create index SI_SCS_CONTRARIOSEJG_IDABOG on SCS_CONTRARIOSEJG (IDABOGADOCONTRARIOEJG);
create index SI_SCS_CV_DATCONTCOL_IDPERSONA on SCS_CV_DATOSCONTACTOCOLEGIADO (IDPERSONA);
create index SI_SCS_CV_GUARDIACOL_IDPERSONA on SCS_CV_GUARDIACOLEGIADO (IDPERSONA);

-- Ejecutado en Integracion por AAG el 19/01 a las 15:00

Cambios en Pkg_Siga_Fusion_Personas

Update Scs_Maestroretenciones Set clavem190 = Null Where clavem190 Is Not Null;--quita la G04 que ha sido suprimida

Cambios en Pkg_Siga_Fusion_Personas

-- Ejecutado en Integracion por AAG el 27/01 a las 12:30

Cambios en Pkg_Siga_Fusion_Personas

