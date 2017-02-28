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

-- Ejecutado en Integracion por AAG el 30/01 a las 12:55

-- 125_004

Modificado PKG_SIGA_FACTURACION_SJCS;

Cambios en Pkg_Siga_Abonos


Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.txt', 'Generando fichero de transferencias TXT', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.txt', 'Generando fichero de transferencias TXT#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.txt', 'Generando fichero de transferencias TXT#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.txt', 'Generando fichero de transferencias TXT#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.versionCuaderno', ': no se ha encontrado versión del cuaderno. Contacte con el Administrador', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.versionCuaderno', ': no se ha encontrado versión del cuaderno. Contacte con el Administrador#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.versionCuaderno', ': no se ha encontrado versión del cuaderno. Contacte con el Administrador#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.versionCuaderno', ': no se ha encontrado versión del cuaderno. Contacte con el Administrador#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', ': falta CIF del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', ': falta CIF del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', ': falta CIF del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', ': falta CIF del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.sufijo', ': falta Sufijo', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.sufijo', ': falta Sufijo#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.sufijo', ': falta Sufijo#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.sufijo', ': falta Sufijo#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', ': caracteres no válidos en CIF y/o Sufijo del ordenante', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', ': caracteres no válidos en CIF y/o Sufijo del ordenante#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', ': caracteres no válidos en CIF y/o Sufijo del ordenante#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', ': caracteres no válidos en CIF y/o Sufijo del ordenante#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', ': falta la fecha de creación del fichero bancario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', ': falta la fecha de creación del fichero bancario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', ': falta la fecha de creación del fichero bancario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', ': falta la fecha de creación del fichero bancario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', ': falta la fecha de ejecución del fichero bancario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', ': falta la fecha de ejecución del fichero bancario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', ': falta la fecha de ejecución del fichero bancario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', ': falta la fecha de ejecución del fichero bancario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', ': falta cuenta del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', ': falta cuenta del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', ': falta cuenta del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', ': falta cuenta del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio.caracteresNoValidos', ': caracteres no válidos en la cuenta del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio.caracteresNoValidos', ': caracteres no válidos en la cuenta del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio.caracteresNoValidos', ': caracteres no válidos en la cuenta del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio.caracteresNoValidos', ': caracteres no válidos en la cuenta del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio', ': falta BIC de la cuenta del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio', ': falta BIC de la cuenta del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio', ': falta BIC de la cuenta del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio', ': falta BIC de la cuenta del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.bicColegio.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', ': falta la cuenta del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', ': falta la cuenta del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', ': falta la cuenta del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', ': falta la cuenta del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', ': caracteres no válidos en la cuenta del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', ': caracteres no válidos en la cuenta del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', ': caracteres no válidos en la cuenta del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', ': caracteres no válidos en la cuenta del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', ': falta importe del abono para', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', ': falta importe del abono para#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', ': falta importe del abono para#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', ': falta importe del abono para#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', ': caracteres no válidos en importe del abono para', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', ': caracteres no válidos en importe del abono para#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', ': caracteres no válidos en importe del abono para#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', ': caracteres no válidos en importe del abono para#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', ': falta BIC de la cuenta del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', ': falta BIC de la cuenta del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', ': falta BIC de la cuenta del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', ': falta BIC de la cuenta del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', ': caracteres no válidos en BIC de la cuenta del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', ': faltan datos de la dirección del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', ': faltan datos de la dirección del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', ': faltan datos de la dirección del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', ': faltan datos de la dirección del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del beneficiario', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del beneficiario#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del beneficiario#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del beneficiario#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', 'Generando fichero de transferencias: CabOrdenanteXML', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', 'Generando fichero de transferencias: CabOrdenanteXML#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', 'Generando fichero de transferencias: CabOrdenanteXML#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', 'Generando fichero de transferencias: CabOrdenanteXML#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', 'Generando fichero de transferencias: BloqueOrdenanteXML', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', 'Generando fichero de transferencias: BloqueOrdenanteXML#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', 'Generando fichero de transferencias: BloqueOrdenanteXML#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', 'Generando fichero de transferencias: BloqueOrdenanteXML#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion', ': faltan datos de la dirección del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion', ': faltan datos de la dirección del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion', ': faltan datos de la dirección del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion', ': faltan datos de la dirección del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del colegio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del colegio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del colegio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', ': caracteres no válidos en datos de la dirección del colegio#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', 'Generando fichero de transferencias: BloqueBeneficiarioXML', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', 'Generando fichero de transferencias: BloqueBeneficiarioXML#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', 'Generando fichero de transferencias: BloqueBeneficiarioXML#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', 'Generando fichero de transferencias: BloqueBeneficiarioXML#GL', '0', 4, Sysdate, 0, '19');


-- Ejecutado en Integracion por AAG el 03/02 a las 10:15

-- SIGA_125_005

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('fcs.forma_facturar_actuaciones_designa_defin.ayuda', 'Cómo se facturan las actuaciones con acreditación de FIN. Valores posibles: 0 - (Natural/Porcentual) Aplicar el porcentaje de la acreditación sobre el importe del módulo; 1 - (Resto) Facturar el 100% del importe del módulo restando lo que se haya facturado ya en la actuación de Inicio correspondiente (misma designación y módulo)', 0, 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('fcs.forma_facturar_actuaciones_designa_defin.ayuda', 'Cómo se facturan las actuaciones con acreditación de FIN. Valores posibles: 0 - (Natural/Porcentual) Aplicar el porcentaje de la acreditación sobre el importe del módulo; 1 - (Resto) Facturar el 100% del importe del módulo restando lo que se haya facturado ya en la actuación de Inicio correspondiente (misma designación y módulo)#CA', 0, 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('fcs.forma_facturar_actuaciones_designa_defin.ayuda', 'Cómo se facturan las actuaciones con acreditación de FIN. Valores posibles: 0 - (Natural/Porcentual) Aplicar el porcentaje de la acreditación sobre el importe del módulo; 1 - (Resto) Facturar el 100% del importe del módulo restando lo que se haya facturado ya en la actuación de Inicio correspondiente (misma designación y módulo)#EU', 0, 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('fcs.forma_facturar_actuaciones_designa_defin.ayuda', 'Cómo se facturan las actuaciones con acreditación de FIN. Valores posibles: 0 - (Natural/Porcentual) Aplicar el porcentaje de la acreditación sobre el importe del módulo; 1 - (Resto) Facturar el 100% del importe del módulo restando lo que se haya facturado ya en la actuación de Inicio correspondiente (misma designación y módulo)#GL', 0, 4, Sysdate, 0, '19');

Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('FCS', 'FORMA_FACTURAR_ACTUACIONES_DESIGNA_DEFIN', '0', Sysdate, 0, 0, 'fcs.forma_facturar_actuaciones_designa_defin.ayuda');
Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('FCS', 'FORMA_FACTURAR_ACTUACIONES_DESIGNA_DEFIN', '1', Sysdate, 0, 2003, 'fcs.forma_facturar_actuaciones_designa_defin.ayuda');

PKG_SIGA_FACTURACION_SJCS


-- Ejecutado en Integracion por AAG el 20/02 a las 09:15

  
MODIFI8CACION DEL ORDEN DE F_SIGA_GET_IDULTIMOESTADOEJG SE PONE EL TRUNC(E.FECHAINICIO) AL ORDER BY
MODIFI8CACION DEL ORDEN DE F_SIGA_GET_ULTIMOESTADOPOREJG SE PONE EL TRUNC(E.FECHAINICIO) AL ORDER BY




  