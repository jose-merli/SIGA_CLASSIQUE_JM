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

Ejecutar SIGA_125_006/SIGA_125_XXX_ALCALA.sql


-- Ejecutado en Integracion por AAG el 01/03 a las 10:45

 PROC_RESPUESTAEJG_2003
f_comunicaciones_ejg_2003_CAB                             

UPDATE PCAJG_TIPO_IDENTIFICACION_CENT SET IDTIPOIDENTIFICACION = 10 WHERE IDINSTITUCION =2003 AND IDENTIFICADOR = 2 ;                                
UPDATE PCAJG_TIPO_IDENTIFICACION_CENT SET IDTIPOIDENTIFICACION = 20 WHERE IDINSTITUCION =2003 AND IDENTIFICADOR = 1 ;


-- Ejecutado en Integracion por AAG el 02/03 a las 15:00




crear tabla pcajg_alc_act_tipo_incidencia

insert into pcajg_alc_act_tipo_incidencia  (identificador, nombre, descripcion) values  (0, 'NO_IMPRIMIR', 'La columna con este valor no se enviará al fichero que se genera');
insert into pcajg_alc_act_tipo_incidencia  (identificador, nombre, descripcion) values  (1, 'OBLIGATORIO', 'El valor de la columna de este tipo tendrá que tener un valor');
insert into pcajg_alc_act_tipo_incidencia  (identificador, nombre, descripcion) values  (2, 'BOOLEANO', 'El valor de la columna si vale 1 mostrará el mensaje de la incidencia');
---


crear tabla pcajg_alc_act_incidencia

insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (1, 'IDINSTITUCION', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (2, 'IDFACTURACION', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (3, 'IS_ENVIADO_ENREMESA', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (4, 'IS_CAMBIO_JUZGADO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (5, 'IS_CAMBIO_JUZGADO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (6, 'IS_CAMBIO_PROCEDIMIENTO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (7, 'EJG_ANIO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (8, 'EJG_NUMERO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (9, 'DESIGNA_ANIO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (10, 'DESIGNA_CODIGO', 0, null, null);
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (11, 'NUMERO_ACTUACION', 0, null, null);
--
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (12, 'AXP1_TIPO_ACTUALIZACION', 1, 'ERROR', 'Debe rellenar el tipo de actualización');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (13, 'AXP2_NUM_EXPEDIENTE', 1, 'ERROR', 'La designación debe estar asociada a un expediente enviado a la comisión');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (14, 'IS_ENVIADO_ENREMESA', 1, 'ERROR', 'El expediente no ha sido enviado a la comisión en una remesa');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (15, 'PRC1_ORGANO_JUDICIAL', 1, 'ERROR', 'Debe rellenar el código del órgano judicial de la actuación o designación');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (16, 'PRC2_TIPO_PROCED_JUDICIAL', 1, 'ERROR', 'Debe rellenar el código del procedimiento (módulo) de la actuación');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (17, 'PRC3_NUM_PROCEDIMIENTO', 1, 'ERROR', 'Debe rellenar correctamente el número de procedimiento');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (18, 'PRC4_ANIO_PROCEDIMIENTO', 1, 'ERROR', 'Debe rellenar correctamente el año del procedimiento');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (19, 'PRC6_ESTADO_PROCEDIMIENTO', 1, 'ERROR', 'Debe rellenar el campo en calidad de');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (20, 'DPA1_ABOGADO_PROCURADOR', 1, 'ERROR', 'Debe indicar si se trata de un abogado o un procurador');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (21, 'DPA2_COLEGIO_PROFESIONAL', 1, 'ERROR', 'Debe rellenar el código del colegio de abogados');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (22, 'DPA4_FECHA_DESIGNA', 1, 'ERROR', 'Debe rellenar la fecha de la designación del abogado');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (23, 'DPA5_NUMERO_DESIGNA', 1, 'ERROR', 'Debe rellenar el número de la designación');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (24, 'DPA6_ANIO_DESIGNA', 1, 'ERROR', 'Debe rellenar el año de la designación');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (25, 'DAC1_TIPO_ACTUACION_SUPLEM', 1, 'ERROR', 'Debe indicar el tipo de actuación o suplemento');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (26, 'DAC2_FECHA_ACTUACION_SUPLEM', 1, 'ERROR', 'Debe indicar la fecha de actuación o suplemento');
--
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (27, 'IS_CAMBIO_JUZGADO', 2, 'ERROR', 'El juzgado de la actuación es diferente al juzgado de la designación. Debe indicar el motivo del cambio en la actuación');
insert into pcajg_alc_act_incidencia (identificador, campo, IDTIPOINC, nivel, mensaje)values (28, 'IS_CAMBIO_PROCEDIMIENTO', 2, 'ERROR', 'El código del módulo de la actuación es diferente al código del procedimiento de la designación. Debe indicar el motivo del cambio en la actuación');


update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Los expedientes seleccionados se marcarán como Recibida respuesta incorrecta. ¿Desea continuar?' where idrecurso='e_comunicaciones.confirmar.marcarRespuestaIncorrecta' and idlenguaje='1';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Los expedientes seleccionados se marcarán como Recibida respuesta incorrecta. ¿Desea continuar?#CA' where idrecurso='e_comunicaciones.confirmar.marcarRespuestaIncorrecta' and idlenguaje='2';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Los expedientes seleccionados se marcarán como Recibida respuesta incorrecta. ¿Desea continuar?#EU' where idrecurso='e_comunicaciones.confirmar.marcarRespuestaIncorrecta' and idlenguaje='3';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Los expedientes seleccionados se marcarán como Recibida respuesta incorrecta. ¿Desea continuar?#GL' where idrecurso='e_comunicaciones.confirmar.marcarRespuestaIncorrecta' and idlenguaje='4';


-- Ejecutado en Integracion por AAG el 06/03 a las 11:30

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.anular.tieneAsistencias', 'Tiene asistencias asociadas, ¿deseas cotinuar con el proceso de anulación?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.anular.tieneAsistencias', 'Tiene asistencias asociadas, ¿deseas cotinuar con el proceso de anulación?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.anular.tieneAsistencias', 'Tiene asistencias asociadas, ¿deseas cotinuar con el proceso de anulación?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.anular.tieneAsistencias', 'Tiene asistencias asociadas, ¿deseas cotinuar con el proceso de anulación?#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al permutado, ¿deseas cotinuar con el proceso de permutación?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al permutado, ¿deseas cotinuar con el proceso de permutación?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al permutado, ¿deseas cotinuar con el proceso de permutación?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al permutado, ¿deseas cotinuar con el proceso de permutación?#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al sustituto, ¿deseas cotinuar con el proceso de sustitución?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al sustituto, ¿deseas cotinuar con el proceso de sustitución?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al sustituto, ¿deseas cotinuar con el proceso de sustitución?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.listadoModal_DefinirCalendarioGuardia.sustituir.tieneAsistencias', 'Tiene asistencias asociadas, este proceso asignará las asistencias al sustituto, ¿deseas cotinuar con el proceso de sustitución?#EU', 0, '3', sysdate, 0, '19');
 
 Nuevo paquete PKG_SIGA_ACCIONES_GUARDIAS;
 
 
UPDATE PCAJG_ALC_ACT_INCIDENCIA I SET I.NIVEL = 'ERROR' WHERE I.NIVEL IS NOT NULL;
UPDATE PCAJG_ALC_ACT_INCIDENCIA I SET I.NIVEL = 'WARNING' WHERE I.IDENTIFICADOR = 30;

 -- Add/modify columns 
alter table SCS_ACREDITACIONPROCEDIMIENTO add CODSUBTARIFA varchar2(10);
-- Add comments to the columns 
comment on column SCS_ACREDITACIONPROCEDIMIENTO.CODSUBTARIFA
  is 'Requisito de Alcala para subtarifas';
  
UPDATE scs_acreditacionprocedimiento AC
SET AC.CODSUBTARIFA = (select DECODE(INSTR(codigoext,'##'),0,null,SUBSTR(codigoext,INSTR(codigoext,'##')+2)) from scs_acreditacionprocedimiento where idinstitucion = AC.IDINSTITUCION AND IDPROCEDIMIENTO = AC.IDPROCEDIMIENTO AND IDACREDITACION = AC.IDACREDITACION),
 AC.CODIGOEXT = (select DECODE(INSTR(codigoext,'##'),0,codigoext,SUBSTR(codigoext,0,INSTR(codigoext,'##')-1)) from scs_acreditacionprocedimiento where idinstitucion = AC.IDINSTITUCION AND IDPROCEDIMIENTO = AC.IDPROCEDIMIENTO AND IDACREDITACION = AC.IDACREDITACION)
WHERE AC.IDINSTITUCION = 2003;
  

-- Ejecutado en Integracion por AAG el 13/03 a las 10:00

 alter table cen_direcciones add OTRAPROVINCIA number(1) default 0;
comment on column cen_direcciones.OTRAPROVINCIA
  is '0: La provincia coincide con el código postal, 1: La provincia puede o no coincidir con el código postal';
  
  
   alter table CEN_SOLIMODIDIRECCIONES add OTRAPROVINCIA number(1) default 0;
comment on column CEN_SOLIMODIDIRECCIONES.OTRAPROVINCIA
  is '0: La provincia coincide con el código postal, 1: La provincia puede o no coincidir con el código postal';

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('general.boton.cambiarEstado', 'Cambiar estado', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('general.boton.cambiarEstado', 'Cambiar estado#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('general.boton.cambiarEstado', 'Cambiar estado#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('general.boton.cambiarEstado', 'Cambiar estado#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('gratuita.busquedaEJG.seleccioneEstado', 'Seleccione el nuevo estado', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('gratuita.busquedaEJG.seleccioneEstado', 'Seleccione el nuevo estado#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('gratuita.busquedaEJG.seleccioneEstado', 'Seleccione el nuevo estado#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('gratuita.busquedaEJG.seleccioneEstado', 'Seleccione el nuevo estado#GL', '0', 4, Sysdate, 0, '19');

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('messages.cajg.confirmarCambioEstado', 'Se va a cambiar añadir el nuevo estado a los expedientes seleccionados.', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('messages.cajg.confirmarCambioEstado', 'Se va a cambiar añadir el nuevo estado a los expedientes seleccionados.#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('messages.cajg.confirmarCambioEstado', 'Se va a cambiar añadir el nuevo estado a los expedientes seleccionados.#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('messages.cajg.confirmarCambioEstado', 'Se va a cambiar añadir el nuevo estado a los expedientes seleccionados.#GL', '0', 4, Sysdate, 0, '19');

insert into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.datosDireccion.literal.otraProvincia', 'Otra provincia', '0', 1, Sysdate, 0, '19');
insert into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.datosDireccion.literal.otraProvincia', 'Otra provincia#CA', '0', 2, Sysdate, 0, '19');
insert into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.datosDireccion.literal.otraProvincia', 'Otra provincia#EU', '0', 3, Sysdate, 0, '19');
insert into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.datosDireccion.literal.otraProvincia', 'Otra provincia#GL', '0', 4, Sysdate, 0, '19');






create table PCAJG_ALC_ACT_ERROR_CAM

insert into ecom_operacion (idoperacion, idservicio, nombre, maxreintentos, activo, fechamodificacion, usumodificacion)
values (56, 5, 'Carga de fichero de errores de actuaciones de la CAM', 5, 1, sysdate, 0);

-- Create sequence 
create sequence SEQ_PCAJG_ALC_ACT_ERROR_CAM
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache;


CREATE OR REPLACE VIEW V_PCAJG_ALC_ACT_ERROR_CAM AS
SELECT
E.IDINSTITUCION, E.IDFACTURACION, E.CODIGO_ERROR, TE.ERROR_DESCRIPCION, E.CODIGO_CAMPO_ERROR, TC.CAMPO_DESCRIPCION, COUNT(1) CUENTA
FROM PCAJG_ALC_ACT_ERROR_CAM E, PCAJG_ALC_TIPOERRORINTERCAMBIO TE, PCAJG_ALC_TIPOCAMPOCARGA TC
WHERE E.CODIGO_ERROR = TE.ERROR_CODIGO
AND E.CODIGO_CAMPO_ERROR = TC.CAMPO_CODIGO(+)
AND E.BORRADO = 0
GROUP BY E.IDINSTITUCION, E.IDFACTURACION, E.CODIGO_ERROR, TE.ERROR_DESCRIPCION, E.CODIGO_CAMPO_ERROR, TC.CAMPO_DESCRIPCION
ORDER BY E.CODIGO_ERROR;



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.inserted.success.ficheroErrorActuacionesCAM', 'El fichero se ha subido correctamente y será procesado en breve. Vuelva a generar los informes pasados unos instantes para ver el resumen de errores enviados por la CAM.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.inserted.success.ficheroErrorActuacionesCAM', 'El fichero se ha subido correctamente y será procesado en breve. Vuelva a generar los informes pasados unos instantes para ver el resumen de errores enviados por la CAM.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.inserted.success.ficheroErrorActuacionesCAM', 'El fichero se ha subido correctamente y será procesado en breve. Vuelva a generar los informes pasados unos instantes para ver el resumen de errores enviados por la CAM.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.inserted.success.ficheroErrorActuacionesCAM', 'El fichero se ha subido correctamente y será procesado en breve. Vuelva a generar los informes pasados unos instantes para ver el resumen de errores enviados por la CAM.#GL', 0, '4', sysdate, 0, '19');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.fichero', 'Fichero', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.fichero', 'Fichero#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.fichero', 'Fichero#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.fichero', 'Fichero#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroErrorCAM', 'Suba el fichero de errores generado por la CAM. Pasados unos instantes el fichero será procesado y se generará un informe de errores tras pulsar el botón generar informe.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroErrorCAM', 'Suba el fichero de errores generado por la CAM. Pasados unos instantes el fichero será procesado y se generará un informe de errores tras pulsar el botón generar informe.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroErrorCAM', 'Suba el fichero de errores generado por la CAM. Pasados unos instantes el fichero será procesado y se generará un informe de errores tras pulsar el botón generar informe.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroErrorCAM', 'Suba el fichero de errores generado por la CAM. Pasados unos instantes el fichero será procesado y se generará un informe de errores tras pulsar el botón generar informe.#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroErrorCAM', 'Fichero errores CAM', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroErrorCAM', 'Fichero errores CAM#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroErrorCAM', 'Fichero errores CAM#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroErrorCAM', 'Fichero errores CAM#GL', 0, '4', sysdate, 0, '19');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.boton.subirFicheroErrorCAM', 'Errores CAM', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.boton.subirFicheroErrorCAM', 'Errores CAM#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.boton.subirFicheroErrorCAM', 'Errores CAM#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.boton.subirFicheroErrorCAM', 'Errores CAM#GL', 0, '4', sysdate, 0, '19');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroSeleccionErrorCAM', 'Generar fichero CAM', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroSeleccionErrorCAM', 'Generar fichero CAM#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroSeleccionErrorCAM', 'Generar fichero CAM#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.dialogo.ficheroSeleccionErrorCAM', 'Generar fichero CAM#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroSeleccionErrorCAM', 'Por favor, seleccione un tipo de error que haya solucionado o seleccione opción todos para generar el fichero completo de nuevo. Si no desea generar el fichero para subir a la CAM seleccione la opción.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroSeleccionErrorCAM', 'Por favor, seleccione un tipo de error que haya solucionado o seleccione opción todos para generar el fichero completo de nuevo. Si no desea generar el fichero para subir a la CAM seleccione la opción.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroSeleccionErrorCAM', 'Por favor, seleccione un tipo de error que haya solucionado o seleccione opción todos para generar el fichero completo de nuevo. Si no desea generar el fichero para subir a la CAM seleccione la opción.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.aviso.ficheroSeleccionErrorCAM', 'Por favor, seleccione un tipo de error que haya solucionado o seleccione opción todos para generar el fichero completo de nuevo. Si no desea generar el fichero para subir a la CAM seleccione la opción.#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.noGenerarFichero', 'No generar fichero para subir a la CAM', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.noGenerarFichero', 'No generar fichero para subir a la CAM#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.noGenerarFichero', 'No generar fichero para subir a la CAM#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.noGenerarFichero', 'No generar fichero para subir a la CAM#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.generarFicheroCompleto', 'Generar fichero completo con todos los errores', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.generarFicheroCompleto', 'Generar fichero completo con todos los errores#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.generarFicheroCompleto', 'Generar fichero completo con todos los errores#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacionjg.literal.generarFicheroCompleto', 'Generar fichero completo con todos los errores#GL', 0, '4', sysdate, 0, '19');


--eliminamos de las plantillas los tipos de envio fax
delete from adm_envioinforme ei where ei.idtipoenvios =3 ;

-- Create table
create table SCS_PRETENSIONESPROCED
(
  IDINSTITUCION     NUMBER(4) not null,
  IDPRETENSION      NUMBER(3) not null,
  IDPROCEDIMIENTO   VARCHAR2(5) not null,
  FECHAMODIFICACION DATE not null,
  USUMODIFICACION   NUMBER(5) not null
)
tablespace TS_SIGA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCS_PRETENSIONESMODULO
  add constraint PK_PRETENSIONESPROCEDIMIENTO primary key (IDINSTITUCION, IDPRETENSION, IDPROCEDIMIENTO)
  using index 
  tablespace TS_SIGA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table SCS_PRETENSIONESMODULO
  add constraint FK_PRETENSION foreign key (IDINSTITUCION, IDPRETENSION)
  references SCS_PRETENSION (IDINSTITUCION, IDPRETENSION);
alter table SCS_PRETENSIONESMODULO
  add constraint FK_PROCEDIMIENTO foreign key (IDINSTITUCION, IDPROCEDIMIENTO)
  references SCS_PROCEDIMIENTOS (IDINSTITUCION, IDPROCEDIMIENTO);

insert into scs_pretensionesproced
     (idinstitucion, idpretension, idprocedimiento, fechamodificacion, usumodificacion)

     (
     Select PRE.IDINSTITUCION,pre.idpretension, pro.idprocedimiento,SYSDATE,1
  from scs_actuaciondesigna ad, scs_pretension pre, scs_procedimientos pro
 where ad.idinstitucion = pre.idinstitucion
   and ad.idpretension = pre.idpretension
   and ad.idinstitucion = pro.idinstitucion
   and ad.idprocedimiento = pro.idprocedimiento
   and ad.idinstitucion = 2003
                     
GROUP BY PRE.IDINSTITUCION,pre.idpretension,pro.idprocedimiento
     );
	 
	 
-- Add/modify columns 
alter table CAJG_REMESARESOLUCION add IDREMESA NUMBER(10);
-- Create/Recreate primary, unique and foreign key constraints 
alter table CAJG_REMESARESOLUCION
  add constraint FK_CAJG_REMESA foreign key (IDINSTITUCION, IDREMESA)
  references cajg_remesa (IDINSTITUCION, IDREMESA);
  
  -- Add/modify columns 
alter table PCAJG_ALC_TIPOERRORINTERCAMBIO add ERROR_SOLUCION VARCHAR2(500);

UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'No es necesario realizar ninguna acción.' WHERE ERROR_CODIGO =001;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'El expediente ya se trasladó anteriormente. Hay que incluirlo en otra remesa para que se envíe como actualización. ' WHERE ERROR_CODIGO =002;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'El expediente no se habia enviado anteriormente. Hay que incluirlo en otra remesa para que se envíe como traslado de expediente. ' WHERE ERROR_CODIGO =003;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que hay maestros que no se han migrado correctamente. Incluirlo en otra remesa cuando soporte haya solucionado la incidencia. ' WHERE ERROR_CODIGO =004;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Modificar los datos obligatorios e incluirlo en una nueva remesa. Informar a soporte para que se modifique el desarrollo por si este error se puede detectar antes del envío. ' WHERE ERROR_CODIGO =005;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que hay error de desarrollo. Incluirlo en otra remesa cuando se haya solucionado la incidencia. ' WHERE ERROR_CODIGO =006;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =007;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Después de que encuentren la solución hay incluirlo en una nueva remesa. ' WHERE ERROR_CODIGO =008;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Avisar a Soporte. Después de que encuentren la solución hay que incluirlo en una nueva remesa. ' WHERE ERROR_CODIGO =009;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que hay error de desarrollo. Incluirlo en otra remesa cuando se haya solucionado la incidencia. ' WHERE ERROR_CODIGO =010;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que existe una incoherencia con los datos enviados. Incluirlo en otra remesa cuando se haya solucionado la incidencia. ' WHERE ERROR_CODIGO =011;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que existe una incoherencia con los datos enviados. Incluirlo en otra remesa cuando se haya solucionado la incidencia. ' WHERE ERROR_CODIGO =012;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte de que existe una incoherencia con los datos enviados. Es posible que sea una designación relacionada con dos expedientes. No tiene solución pero nos quedamos el error como justificante del envío. ' WHERE ERROR_CODIGO =013;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'El procedimiento judicial ya se ha informado en otro expediente (Número procedimiento/año procedimiento) . Modificarlo e incluir el expediente en otra remesa. ' WHERE ERROR_CODIGO =014;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =015;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =016;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =017;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte. Es posible que la CAM no haya aceptado envíos anteriores. ' WHERE ERROR_CODIGO =018;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =019;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =020;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a Soporte. Error incompatible con la información enviada' WHERE ERROR_CODIGO =021;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'No tiene solución pero nos quedamos el error como justificante del envío' WHERE ERROR_CODIGO =022;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'Informar a soporte. Es posible que la CAM no haya aceptado envíos anteriores. ' WHERE ERROR_CODIGO =024;
UPDATE PCAJG_ALC_TIPOERRORINTERCAMBIO SET ERROR_SOLUCION = 'No tiene solución pero nos quedamos el error como justificante del envío' WHERE ERROR_CODIGO =023;

-- Add/modify columns 
f_comunicaciones_ejg_2003_CAB
PROC_RESPUESTAEJG_2003


--Creamos el proceso que utilizara SIGA
insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL) 
values ('12W', 'JGR', 1, 'Y', sysdate, 0, 'EJG pendientes envio actualización', 'JGR_E-Comunicaciones_EJGPendientes', '007', 10);

--Damos permiso al administrador general de Alcalá a ese proceso

insert into adm_tiposacceso
   (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion) 
 values
   ('12W','ADG',sysdate,0,3,2003);
--Configuramos la opción de menú SJCS > e - Comunicaciones > EJGs: Remesa de resultados

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.EJGPteEnvioActualizacion', 'EJGs: Envio actualización', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.facturacionSJCS.EJGPteEnvioActualizacion', 'EJGs: Envio actualización#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.facturacionSJCS.EJGPteEnvioActualizacion', 'EJGs: Envio actualización#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.facturacionSJCS.EJGPteEnvioActualizacion', 'EJGs: Envio actualización#EU', 0, '3', sysdate, 0, '19');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('12W', 22230, 160, '606', sysdate, 0, null, 'menu.sjcs.ecomunicaciones.EJGPteEnvioActualizacion', null, '12W', '1');

-- Ejecutado en Integracion por AAG el 27/03 a las 16:40

-- Add/modify columns 
alter table PCAJG_ALC_ACT_ERROR_CAM modify REGISTRO_ERROR_CAM VARCHAR2(1000);

-- Ejecutado en Integracion por ACP el 28/03 a las 11:17

Modificada vista V_WS_JE_2003_DESIGNA

INSERT INTO CAJG_ERRORESREMESARESOL VALUES(18,2003,18,'La carga de ficheros de error sólo se permite para intercambios de envío o actualización. Los de actuaciones profesionales se carga desde facturación SJCS');


 
insert into ADM_TIPOINFORME (IDTIPOINFORME, DESCRIPCION, IDTIPOINFORMEPADRE, TIPOFORMATO, FECHAMODIFICACION, USUMODIFICACION, CLASE, DIRECTORIO)
values ('CADO', 'Carta de Acreditación de Oficio', null, 'W', sysdate, 0, 'G', 'actuaciones_designacion');

--Creamos el proceso que utilizara SIGA
insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL) 
values ('12X', 'JGR', 1, 'Y', sysdate, 0, 'Carga masiva de procuradores', 'JGR_CargaMasivaProcuradores', '007', 10);

--Damos permiso al administrador general de Alcalá a ese proceso

insert into adm_tiposacceso
   (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion) 
 values
   ('12X','ADG',sysdate,0,3,2005);
--Configuramos la opción de menú SJCS > e - Comunicaciones > EJGs: Remesa de resultados

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.cargaMasivaProcuradores', 'Carga masiva de procuradores', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.cargaMasivaProcuradores', 'Carga masiva de procuradores#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.cargaMasivaProcuradores', 'Carga masiva de procuradores#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.cargaMasivaProcuradores', 'Carga masiva de procuradores#EU', 0, '3', sysdate, 0, '19');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('12X', 22240, 160, '606', sysdate, 0, null, 'menu.sjcs.ecomunicaciones.cargaMasivaProcuradores', null, '12X', '1');



-- Create sequence 
create sequence SEQ_SCSDATOSPROCURADORES
minvalue 0
maxvalue 9999939
start with 1
increment by 1
nocache
cycle;	

-- Create table
create table SCS_DATOSPROCURADORES
(
  IDDATOSPROCURADORES  NUMBER(7) not null,
  IDINSTITUCION        NUMBER(4) not null,
  CODIGODESIGNAABOGADO VARCHAR2(14) not null,
  NUMEJG               VARCHAR2(14),
  NUMCOLPROCURADOR     VARCHAR2(20) not null,
  FECHADESIGPROCURADOR DATE,
  NUMDESIGNAPROCURADOR VARCHAR2(14),
  OBSERVACIONES        VARCHAR2(500),
  FECHAMODIFICACION    DATE not null,
  USUMODIFICACION      NUMBER(5) not null
)
tablespace TS_SIGA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCS_DATOSPROCURADORES
  add constraint PK_SCS_DATOSPROCURADORES primary key (IDDATOSPROCURADORES)
  using index 
  tablespace TS_SIGA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );

  
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ejg.literal', 'Ejg', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ejg.literal', 'Ejg#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ejg.literal', 'Ejg#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ejg.literal', 'Ejg#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaAbogado.literal', 'DesignaciÃ³n Abogado', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaAbogado.literal', 'DesignaciÃ³n Abogado#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaAbogado.literal', 'DesignaciÃ³n Abogado#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaAbogado.literal', 'DesignaciÃ³n Abogado#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaProcurador.literal', 'DesignaciÃ³n procurador', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaProcurador.literal', 'DesignaciÃ³n procurador#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaProcurador.literal', 'DesignaciÃ³n procurador#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.designaProcurador.literal', 'DesignaciÃ³n procurador#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.observaciones.literal', 'Observaciones', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.observaciones.literal', 'Observaciones#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.observaciones.literal', 'Observaciones#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.observaciones.literal', 'Observaciones#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.procurador.literal', 'Procurador', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.procurador.literal', 'Procurador#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.procurador.literal', 'Procurador#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.procurador.literal', 'Procurador#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.fechaDesignaProc.literal', 'Fecha DesignaciÃ³n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.fechaDesignaProc.literal', 'Fecha DesignaciÃ³n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.fechaDesignaProc.literal', 'Fecha DesignaciÃ³n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.fechaDesignaProc.literal', 'Fecha DesignaciÃ³n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ncolProcurador.literal', 'N.Col Procurador', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ncolProcurador.literal', 'N.Col Procurador#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ncolProcurador.literal', 'N.Col Procurador#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cargaMasivaDatosProcuradores.ncolProcurador.literal', 'N.Col Procurador#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.localizacion', 'SJCS > e - Comunicaciones' , 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.localizacion', 'SJCS > e - Comunicaciones#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.localizacion', 'SJCS > e - Comunicaciones#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.localizacion', 'SJCS > e - Comunicaciones#EU', 0, '3', sysdate, 0, '19');

  
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('fac.control_emision_facturas_sii.ayuda', 'Controla que no se generen facturas más que por facturación masiva. De momento sólo se aplica en el módulo de Certificados (leer especificación para más info). Valores aceptados: 0 - Desactivado, funcionamiento normal; 1 - Activado, varias restricciones en diferentes pantallas', 0, 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('fac.control_emision_facturas_sii.ayuda', 'Controla que no se generen facturas más que por facturación masiva. De momento sólo se aplica en el módulo de Certificados (leer especificación para más info). Valores aceptados: 0 - Desactivado, funcionamiento normal; 1 - Activado, varias restricciones en diferentes pantallas#CA', 0, 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('fac.control_emision_facturas_sii.ayuda', 'Controla que no se generen facturas más que por facturación masiva. De momento sólo se aplica en el módulo de Certificados (leer especificación para más info). Valores aceptados: 0 - Desactivado, funcionamiento normal; 1 - Activado, varias restricciones en diferentes pantallas#EU', 0, 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values  ('fac.control_emision_facturas_sii.ayuda', 'Controla que no se generen facturas más que por facturación masiva. De momento sólo se aplica en el módulo de Certificados (leer especificación para más info). Valores aceptados: 0 - Desactivado, funcionamiento normal; 1 - Activado, varias restricciones en diferentes pantallas#GL', 0, 4, Sysdate, 0, '19');

Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('FAC', 'CONTROL_EMISION_FACTURAS_SII', '0', Sysdate, 0, 0, 'fac.control_emision_facturas_sii.ayuda');
Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('FAC', 'CONTROL_EMISION_FACTURAS_SII', '1', Sysdate, 0, 2000, 'fac.control_emision_facturas_sii.ayuda');


-- Ejecutado en Integracion por AAG el 03/04 a las 18:30

--f_comunicaciones_ejg_2003_CAB" 
 --PROC_RESPUESTAEJG_2003"
insert into adm_informe
  (descripcion,
   alias,
   nombrefisico,
   directorio,
   idtipoinforme,
   visible,
   idinstitucion,
   fechamodificacion,
   usumodificacion,
   nombresalida,
   preseleccionado,
   asolicitantes,
   destinatarios,
   tipoformato,
   codigo,
   orden,
   clasejava,
   idplantilla,
   idtipoenvio,
   idplantillaenvio,
   idplantillageneracion,
   idtipointercambiotelematico,
   plantilla,
   acontrarios,
   generarinformesindireccion)
values
  ('Carta de Acreditación de Oficio',
   'Acreditación de Oficio',
   'acreditacionOficio',
   'actuaciones_designacion',
   'CADO',
   'S',
   '0',
   SYSDATE,
   0,
   'AcreditacionOficio',
   'N',
   'N',
   'C',
   'P',
   null,
   '1',
   null,
 
   (select to_number(max(idplantilla)+1) from adm_informe),
   NULL,
   null,
   null,
   null,
   null,
   'N',
   'S');

-- Ejecutado en Integracion por AAG el 04/04 a las 10:10
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('envios.plantillas.literal.asuntoAcreditacionOficio', 'Acreditación de Oficio', '0', 1, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('envios.plantillas.literal.asuntoAcreditacionOficio', 'Acreditación de Oficio#CA', '0', 2, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('envios.plantillas.literal.asuntoAcreditacionOficio', 'Acreditación de Oficio#EU', '0', 3, Sysdate, 0, '19');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('envios.plantillas.literal.asuntoAcreditacionOficio', 'Acreditación de Oficio#GL', '0', 4, Sysdate, 0, '19');


Pkg_Siga_Censo

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.designacionAdicional', 'Ya existe una designación relacionada con este EJG, ¿desea continuar?' , 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.designacionAdicional', 'Ya existe una designación relacionada con este EJG, ¿desea continuar?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.designacionAdicional', 'Ya existe una designación relacionada con este EJG, ¿desea continuar?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.designacionAdicional', 'Ya existe una designación relacionada con este EJG, ¿desea continuar?#EU', 0, '3', sysdate, 0, '19');

F_SIGA_GETEJG_DESIGNA
 
-- Ejecutado en Integracion por AAG el 21/04 a las 13:20

