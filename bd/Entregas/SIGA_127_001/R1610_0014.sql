create table CER_MOTIVOSOLICITUD
(
  IDINSTITUCION     NUMBER(4) not null,
  IDMOTIVOSOLICITUD NUMBER(3) not null,
  DESCRIPCION       VARCHAR2(100) not null,
  ORDEN             NUMBER(4),
  FECHAMODIFICACION DATE not null,
  USUMODIFICACION   NUMBER(5) not null,
  CODIGOEXT         VARCHAR2(10),
  BLOQUEADO         VARCHAR2(1) default 'N' not null,
  FECHABAJA         DATE
)
tablespace TS_SIGA_MGN;

comment on column CER_MOTIVOSOLICITUD.ORDEN
  is 'Establece la ordenacion del desplegable de motivos';

alter table CER_MOTIVOSOLICITUD
  add constraint PK_CER_MOTIVOSOLICITUD primary key (IDINSTITUCION, IDMOTIVOSOLICITUD)
  using index 
  tablespace TS_SIGA_MGN_IDX;

Insert Into gen_catalogos_multiidioma
  (codigo, nombretabla, campotabla, fechamodificacion, usumodificacion, Local, codigotabla, migrado)
Values
  (620, 'CER_MOTIVOSOLICITUD', 'DESCRIPCION', Sysdate, 0, 'S', 'IDMOTIVOSOLICITUD', 'N');
Insert Into gen_tablas_maestras
  (idtablamaestra, idcampocodigo, idcampodescripcion, aliastabla, 
  flagborradologico, flagusalenguaje, longitudcodigo, longituddescripcion, fechamodificacion, usumodificacion, tipocodigo, Local,
  pathaccion, idrecurso, idlenguaje, idcampocodigoext, longitudcodigoext, tipocodigoext, aceptabaja)
Values
  ('CER_MOTIVOSOLICITUD', 'IDMOTIVOSOLICITUD', 'DESCRIPCION', 'Motivos Solicitud Certificado', 
  0, 0, 3, 100, Sysdate, 0, 'A', 'S',
  '/listadoTablaMaestra.do', 'general.no', 1, 'CODIGOEXT', 20, 'A', '1');

Insert Into cer_motivosolicitud
  (idinstitucion, idmotivosolicitud, descripcion, fechamodificacion, usumodificacion)
Values
  (2000, 1, 'Solicitado explícitamente por el colegio', Sysdate, 0);

Declare
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  Update Gen_Catalogos_Multiidioma
     Set Migrado = 'N'
   Where Nombretabla = 'CER_MOTIVOSOLICITUD'; --Marcar la tabla como No traducida
  Proc_Act_Recursos(p_Codretorno, p_Datoserror);
  Dbms_Output.Put_Line(p_Codretorno || ': ' || p_Datoserror);
End;
/

alter table CER_SOLICITUDCERTIFICADOS add IDMOTIVOSOLICITUD NUMBER(3);
alter table CER_SOLICITUDCERTIFICADOS
  add constraint FK_CER_SOLCER_CER_MOTIVOSOL foreign key (IDINSTITUCION, IDMOTIVOSOLICITUD)
  references cer_motivosolicitud (IDINSTITUCION, IDMOTIVOSOLICITUD)
  deferrable;
  
create table CER_INCOMPATIBILIDADES
(
  IDINSTITUCION           NUMBER(4) not null,
  IDTIPOPRODUCTO          NUMBER(4) not null,
  IDPRODUCTO              NUMBER(10) not null,
  IDPRODUCTOINSTITUCION   NUMBER(10) not null,
  FECHAMODIFICACION       DATE not null,
  USUMODIFICACION         NUMBER(5) not null,
  IDTIPOPROD_INCOMPATIBLE NUMBER(4) not null,
  IDPROD_INCOMPATIBLE     NUMBER(10) not null,
  IDPRODINST_INCOMPATIBLE NUMBER(10) not null,
  MOTIVO                  VARCHAR2(4000)
)
tablespace TS_SIGA;
 
alter table CER_INCOMPATIBILIDADES
  add constraint PK_CER_INCOMPATIBILIDADES primary key (IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION, IDTIPOPROD_INCOMPATIBLE, IDPROD_INCOMPATIBLE, IDPRODINST_INCOMPATIBLE)
  using index 
  tablespace TS_SIGA;
alter table CER_INCOMPATIBILIDADES
  add constraint FK_CER_TIPOSCERT_PRODUCTO foreign key (IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION)
  references PYS_PRODUCTOSINSTITUCION (IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION)
  deferrable;
alter table CER_INCOMPATIBILIDADES
  add constraint FK_CER_TIPOSCERT_PRODUCTO_INC foreign key (IDINSTITUCION, IDTIPOPROD_INCOMPATIBLE, IDPROD_INCOMPATIBLE, IDPRODINST_INCOMPATIBLE)
  references PYS_PRODUCTOSINSTITUCION (IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION)
  deferrable;
alter table PYS_PRODUCTOSINSTITUCION add ORDEN NUMBER(10);

Update Pys_Productosinstitucion Pro
   Set Pro.Orden = Case Substr(Pro.Codigoext, 1, 2) When 'NI' Then 100 When 'CA' Then 200 When 'NE' Then 300 When 'CO' Then 400 When 'CE' Then 410 End
 Where Pro.Idinstitucion = 2000
   And Pro.Tipocertificado = 'C'
   And Pro.Idtipoproducto = 18
   And Pro.Idproducto = 0;


Insert Into Gen_Procesos  (Idproceso, Idmodulo, Traza, Target, Fechamodificacion, Usumodificacion, Descripcion, Transaccion, Idparent, Nivel)
Values  ('62d', 'CER', 0, 'Y', Sysdate, 0, 'Incompatibilidad con otros certificados', 'CER_Incompatibilidades', '62', 10);
Insert Into Adm_Tiposacceso  (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
(Select '62d', 'ADG', Sysdate, 0, 3, Ins.Idinstitucion From Cen_Institucion Ins);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('pestana.certificados.incompatibilidades', 'Incompatibilidades', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('pestana.certificados.incompatibilidades', 'Incompatibilitats', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('pestana.certificados.incompatibilidades', 'Incompatibilidades#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('pestana.certificados.incompatibilidades', 'Incompatibilidades#GL', 0, 4, Sysdate, 0, 22);

Insert Into Gen_Pestanas  (Idproceso, Idlenguaje, Idrecurso, Posicion, Idgrupo)
Values  ('62d', 1, 'pestana.certificados.incompatibilidades', 3, 'CERTIFIC');

Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 4, Sysdate, 0, 18, 0, 1, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 4, Sysdate, 0, 18, 0, 10, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 4, Sysdate, 0, 18, 0, 4, 'No se pueden emitir dos certificados de Nueva Incorporación');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 4, Sysdate, 0, 18, 0, 8, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 4, Sysdate, 0, 18, 0, 9, 'No se pueden emitir dos certificados de Nueva Incorporación');

Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 9, Sysdate, 0, 18, 0, 1, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación (aunque sea de No ejerciente)');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 9, Sysdate, 0, 18, 0, 10, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación (aunque sea de No ejerciente)');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 9, Sysdate, 0, 18, 0, 4, 'No se pueden emitir dos certificados de Nueva Incorporación');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 9, Sysdate, 0, 18, 0, 8, 'Si ya se ha emitido un certificado cualquiera, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Nueva Incorporación (aunque sea de No ejerciente)');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 9, Sysdate, 0, 18, 0, 9, 'No se pueden emitir dos certificados de Nueva Incorporación');

Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 8, Sysdate, 0, 18, 0, 1, 'Si ya se ha emitido un certificado ordinario, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Cambio a Abogado');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 8, Sysdate, 0, 18, 0, 10, 'Si ya se ha emitido un certificado ordinario, quiere decir que el colegiado ya estaba incorporado y por tanto no necesita uno de Cambio a Abogado');
Insert Into CER_INCOMPATIBILIDADES  (idinstitucion, idtipoproducto, idproducto, idproductoinstitucion, fechamodificacion, usumodificacion, idtipoprod_incompatible, idprod_incompatible, idprodinst_incompatible, motivo)
Values  (2000, 18, 0, 8, Sysdate, 0, 18, 0, 8, 'No se pueden emitir dos certificados de Cambio a Abogado');


Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.incompatible', 'Tipo Certificado Incompatible', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.incompatible', 'Tipo Certificado Incompatible#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.incompatible', 'Tipo Certificado Incompatible#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.incompatible', 'Tipo Certificado Incompatible#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.motivo', 'Motivo', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.motivo', 'Motiu', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.motivo', 'Motivo#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.motivo', 'Motivo#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.nuevaIncompatibilidad', 'Nueva Incompatibilidad', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.nuevaIncompatibilidad', 'Nueva Incompatibilidad#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.nuevaIncompatibilidad', 'Nueva Incompatibilidad#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.nuevaIncompatibilidad', 'Nueva Incompatibilidad#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.edicionIncompatibilidad', 'Edición de Incompatibilidad', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.edicionIncompatibilidad', 'Edición de Incompatibilidad#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.edicionIncompatibilidad', 'Edición de Incompatibilidad#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.mantenimiento.literal.edicionIncompatibilidad', 'Edición de Incompatibilidad#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo', 'Motivo especial', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo', 'Motiu especial', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo', 'Motivo especial#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo', 'Motivo especial#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.certificadoIncompatible', 'El Certificado actual no debería ser emitido por resultar INCOMPATIBLE con los existentes para el mismo solicitante: revise los certificados no anulados para el mismo, junto con las incompatibilidades configuradas para el tipo de certificado actual. En caso de ser ABSOLUTAMENTE necesaria la emisión del certificado actual, previamente tendrá que SELECCIONAR un Motivo de solicitud.', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.certificadoIncompatible', 'El Certificado actual no debería ser emitido por resultar INCOMPATIBLE con los existentes para el mismo solicitante: revise los certificados no anulados para el mismo, junto con las incompatibilidades configuradas para el tipo de certificado actual. En caso de ser ABSOLUTAMENTE necesaria la emisión del certificado actual, previamente tendrá que SELECCIONAR un Motivo de solicitud.#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.certificadoIncompatible', 'El Certificado actual no debería ser emitido por resultar INCOMPATIBLE con los existentes para el mismo solicitante: revise los certificados no anulados para el mismo, junto con las incompatibilidades configuradas para el tipo de certificado actual. En caso de ser ABSOLUTAMENTE necesaria la emisión del certificado actual, previamente tendrá que SELECCIONAR un Motivo de solicitud.#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.certificadoIncompatible', 'El Certificado actual no debería ser emitido por resultar INCOMPATIBLE con los existentes para el mismo solicitante: revise los certificados no anulados para el mismo, junto con las incompatibilidades configuradas para el tipo de certificado actual. En caso de ser ABSOLUTAMENTE necesaria la emisión del certificado actual, previamente tendrá que SELECCIONAR un Motivo de solicitud.#GL', 0, 4, Sysdate, 0, 22);


create table CER_MOTIVOANULACION
(
  IDINSTITUCION     NUMBER(4) not null,
  IDMOTIVOANULACION NUMBER(3) not null,
  DESCRIPCION       VARCHAR2(100) not null,
  ORDEN             NUMBER(4),
  FECHAMODIFICACION date not null,
  USUMODIFICACION   NUMBER(5) not null,
  CODIGOEXT         VARCHAR2(10),
  BLOQUEADO         VARCHAR2(1) default 'N' not null,
  FECHABAJA         date
)
tablespace TS_SIGA_MGN;

alter table CER_MOTIVOANULACION
  add constraint PK_CER_MOTIVOANULACION primary key (IDINSTITUCION, IDMOTIVOANULACION) 
  using index 
  tablespace TS_SIGA_MGN_IDX;

comment on column CER_MOTIVOANULACION.ORDEN
  is 'Establece la ordenacion del desplegable de motivos';

Insert Into gen_catalogos_multiidioma
  (codigo, nombretabla, campotabla, fechamodificacion, usumodificacion, Local, codigotabla, migrado)
Values
  (630, 'CER_MOTIVOANULACION', 'DESCRIPCION', Sysdate, 0, 'S', 'IDMOTIVOANULACION', 'N');
Insert Into gen_tablas_maestras
  (idtablamaestra, idcampocodigo, idcampodescripcion, aliastabla, 
  flagborradologico, flagusalenguaje, longitudcodigo, longituddescripcion, fechamodificacion, usumodificacion, tipocodigo, Local,
  pathaccion, idrecurso, idlenguaje, idcampocodigoext, longitudcodigoext, tipocodigoext, aceptabaja)
Values
  ('CER_MOTIVOANULACION', 'IDMOTIVOANULACION', 'DESCRIPCION', 'Motivos Anulación Certificado', 
  0, 0, 3, 100, Sysdate, 0, 'A', 'S',
  '/listadoTablaMaestra.do', 'general.no', 1, 'CODIGOEXT', 20, 'A', '1');

Insert Into cer_motivoanulacion (idinstitucion, idmotivoanulacion, descripcion, fechamodificacion, usumodificacion)
(Select idinstitucion, 1, 'Error manual detectado', Sysdate, 0
   From Cen_Institucion);
Insert Into cer_motivoanulacion (idinstitucion, idmotivoanulacion, descripcion, fechamodificacion, usumodificacion)
Values (2000, 2, 'Colegiado solicita anulación por no colegiarse finalmente', Sysdate, 0);
Insert Into cer_motivoanulacion (idinstitucion, idmotivoanulacion, descripcion, fechamodificacion, usumodificacion)
Values (2000, 3, 'Duplicado por distinta identificación', Sysdate, 0);
Insert Into cer_motivoanulacion (idinstitucion, idmotivoanulacion, descripcion, fechamodificacion, usumodificacion)
Values (2000, 4, 'Solicitado por el colegio: tipo de certificado erróneo', Sysdate, 0);
Insert Into cer_motivoanulacion (idinstitucion, idmotivoanulacion, descripcion, fechamodificacion, usumodificacion)
Values (2000, 5, 'Solicitado por el colegiado: error en datos', Sysdate, 0);

Declare
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  Update Gen_Catalogos_Multiidioma
     Set Migrado = 'N'
   Where Nombretabla = 'CER_MOTIVOANULACION'; --Marcar la tabla como No traducida
  Proc_Act_Recursos(p_Codretorno, p_Datoserror);
  Dbms_Output.Put_Line(p_Codretorno || ': ' || p_Datoserror);
End;
/

alter table CER_SOLICITUDCERTIFICADOS add IDMOTIVOANULACION NUMBER(3);
alter table CER_SOLICITUDCERTIFICADOS
  add constraint FK_CER_SOLCER_CER_MOTIVOANU foreign key (IDINSTITUCION, IDMOTIVOANULACION)
  references cer_motivoanulacion (IDINSTITUCION, IDMOTIVOANULACION)
  deferrable;


Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo.anulacion', 'Motivo anulación', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo.anulacion', 'Motiu anul·lació', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo.anulacion', 'Motivo anulación#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.motivo.anulacion', 'Motivo anulación#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('administracion.parametro.maximo_dias_antelacion_solicitud', 'La fecha de solicitud de un certificado tiene que ser igual o anterior al día actual, pero hasta este máximo de días naturales', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('administracion.parametro.maximo_dias_antelacion_solicitud', 'La fecha de solicitud de un certificado tiene que ser igual o anterior al día actual, pero hasta este máximo de días naturales#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('administracion.parametro.maximo_dias_antelacion_solicitud', 'La fecha de solicitud de un certificado tiene que ser igual o anterior al día actual, pero hasta este máximo de días naturales#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('administracion.parametro.maximo_dias_antelacion_solicitud', 'La fecha de solicitud de un certificado tiene que ser igual o anterior al día actual, pero hasta este máximo de días naturales#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('CER', 'MAXIMO_DIAS_ANTELACION_SOLICITUD', 365, Sysdate, 0, 0, 'administracion.parametro.maximo_dias_antelacion_solicitud');
Insert Into gen_parametros  (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
Values  ('CER', 'MAXIMO_DIAS_ANTELACION_SOLICITUD', 31, Sysdate, 0, 2000, 'administracion.parametro.maximo_dias_antelacion_solicitud');


Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFutura', 'La fecha de solicitud no puede ser posterior al día de hoy', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFutura', 'La fecha de solicitud no puede ser posterior al día de hoy#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFutura', 'La fecha de solicitud no puede ser posterior al día de hoy#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFutura', 'La fecha de solicitud no puede ser posterior al día de hoy#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFueraDePlazo', 'Para la fecha de solicitud, existe un máximo de {0} días de antelación respecto al día de hoy', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFueraDePlazo', 'Para la fecha de solicitud, existe un máximo de {0} días de antelación respecto al día de hoy#CA', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFueraDePlazo', 'Para la fecha de solicitud, existe un máximo de {0} días de antelación respecto al día de hoy#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.mensaje.fechaSolicitudFueraDePlazo', 'Para la fecha de solicitud, existe un máximo de {0} días de antelación respecto al día de hoy#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.automatico', '> En proceso', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.automatico', '> En procés', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.automatico', '> En proceso#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.automatico', '> En proceso#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.activo', '> Estados activos', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.activo', '> Estats actius', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.activo', '> Estados activos#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.estado.activo', '> Estados activos#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.estadosolicitud', 'Estado y Solicitud', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.estadosolicitud', 'Estat i Sol·licitud', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.estadosolicitud', 'Estado y Solicitud#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.estadosolicitud', 'Estado y Solicitud#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.certificadoacciones', 'Certificado y Acciones', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.certificadoacciones', 'Certificat i Accions', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.certificadoacciones', 'Certificado y Acciones#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.certificadoacciones', 'Certificado y Acciones#GL', 0, 4, Sysdate, 0, 22);

Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.personacolegio', 'Datos personales y de colegio', 0, 1, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.personacolegio', 'Dades personals i de col·legi', 0, 2, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.personacolegio', 'Datos personales y de colegio#EU', 0, 3, Sysdate, 0, 22);
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('certificados.solicitudes.literal.titulo.personacolegio', 'Datos personales y de colegio#GL', 0, 4, Sysdate, 0, 22);
