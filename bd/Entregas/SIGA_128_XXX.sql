--Tabla para almacenar los cambios de documento de los colegiados.
create table CEN_HISTORICO_DOCPERSONA
(
  IDCENHISTORICODOC NUMBER(10) not null,
  IDPERSONA        NUMBER(10) not null,
  IDINSTITUCION    NUMBER(4) not null,
  TIPOIDENTIFICACIONNEW    NUMBER(2) not null,
  NUMEROIDENTIFICACIONNEW  VARCHAR2(20) not null,	
  TIPOIDENTIFICACIONOLD    NUMBER(2) not null,
  NUMEROIDENTIFICACIONOLD  VARCHAR2(20) not null,	
  USUMODIFICACION          NUMBER(4) not null,
  FECHAMODIFICACION DATE not null
)tablespace TS_SIGA_CMN;

alter table CEN_HISTORICO_DOCPERSONA
  add constraint PK_CENHIST_DOC primary key (IDCENHISTORICODOC)
  using index 
  tablespace TS_SIGA_CMN_IDX;
  
  alter table cen_historico_docpersona
  add constraint FK_CEN_HISTORICO_PERSONA foreign key (IDPERSONA)
  references CEN_PERSONA ( IDPERSONA);
 
alter table cen_historico_docpersona
  add constraint FK_CEN_HISTORICO_INSTITUCION foreign key (IDINSTITUCION)
  references CEN_INSTITUCION (IDINSTITUCION);

create sequence SEQ_CEN_HISTORICO_DOCPERSONA minvalue 1 maxvalue 9999999999 start with 1 increment by 1 nocache;

GRANT SELECT ON CEN_HISTORICO_DOCPERSONA TO ROLE_SIGA_R;
GRANT SELECT, INSERT, UPDATE, DELETE ON CEN_HISTORICO_DOCPERSONA TO ROLE_SIGA;

-- Ya ejecutado en integración

alter table MJU_REPORT_CERTIFICADO modify CLAVE VARCHAR2(25);

--Ejecutado en integración por FMS el 27/06/2019

PKG_SIGA_FACTURACION_SJCS

--Ejecutado en integración por AAG el 02/07/2019 09:00

--promover el triger  SCS_GUARDIASCOLEGIADO_AID de uscgae_desa hasta pro

-- ya lo ejecuto Azucena de forma urgente en PRe y PRO. Dejo aqui el comentario como Hco.

-- Solo para PRO:
Update Gen_Properties Pro   Set Pro.Valor = '/FILERMSA1000/SIGA' Where Parametro = 'directorios.path.OrigenPlantillas'   And Valor = '/FILERMSA1000/SIGA/';

-- Ya ejecutado en PRE: SIGA_128_008

--nuevo concepto de intercambios con cataluña

crear v_siga_cat_certificacion
crear v_siga_cat_anexo

--llevar de nuevo  v_siga_cat_justificacion
v_siga_cat_justificacion
--llevar de nuevo  v_siga_cat_devolucion
v_siga_cat_devolucion


-- Create sequence 
create sequence SEQ_JE_DEV_VALERRONEO
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;

-- Add/modify columns 
alter table JE_DEVOLUCION modify IDCODIGOEXPEDIENTE null;
alter table JE_DEVOLUCION modify IDJEJUSTICIABLE null;
alter table JE_DEVOLUCION modify MODULO_CAMBIO null;
-- Add/modify columns 
alter table JE_DESIGNA modify NUM_DESIGNACION_ABOGADO null;

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.anexo', 'Anexo', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.anexo', 'Anexo#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.anexo', 'Anexo#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.anexo', 'Anexo#GL', 0, '4', sysdate, 0, '19');


-- Create sequence 
 
create sequence SEQ_FCS_JE_INTERCAMBIOS
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;

-- Create table
create table FCS_JE_INTERCAMBIOS
(
  IDINTERCAMBIO   NUMBER(10) not null,
  IDINSTITUCION     NUMBER(4) not null,
  DESCRIPCION       VARCHAR2(100) not null,
  IDPERIODO         NUMBER(3) not null,
  ANIO              NUMBER(4) not null,
  IDESTADO number(2) not null
 

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
alter table FCS_JE_INTERCAMBIOS
  add constraint PK_FCS_JE_INTERCAMBIO primary key (IDINTERCAMBIO)
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
alter table FCS_JE_INTERCAMBIOS
  add constraint FK_FCS_JE_INT_GEN_PERIODO foreign key (IDPERIODO)
  references GEN_PERIODO (IDPERIODO);
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_JE_INTERCAMBIOS
  add constraint FK_FCS_JE_INT_ESTADO foreign key (IDESTADO)
  references fcs_je_maestroestados (IDESTADO);
  
 
-- Create/Recreate indexes 
create unique index UK_FCS_JE_INTERCAMBIO on FCS_JE_INTERCAMBIOS (IDINSTITUCION, IDINTERCAMBIO)
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



alter table FCS_JE_JUSTIFICACION add IDINTERCAMBIO NUMBER(10) not null;
alter table FCS_JE_JUSTIFICACION
add constraint FK_FCS_JE_JUST_INTERCAMBIO foreign key (IDINTERCAMBIO)
  references FCS_JE_INTERCAMBIOS (IDINTERCAMBIO);

 -- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_JE_JUSTIFICACION
  drop constraint FK_FCS_JE_JUSTI_GEN_PERIODO;
-- Drop columns 
alter table FCS_JE_JUSTIFICACION drop column DESCRIPCION;
alter table FCS_JE_JUSTIFICACION drop column IDPERIODO;
alter table FCS_JE_JUSTIFICACION drop column ANIO;
alter table FCS_JE_JUSTIFICACION drop column IDINSTITUCION;

-- Create/Recreate primary, unique and foreign key constraints 


-- Add/modify columns 
alter table FCS_JE_DEVOLUCION rename column IDJUSTIFICACION to IDINTERCAMBIO;
-- Drop primary, unique and foreign key constraints 
alter table FCS_JE_DEVOLUCION
  drop constraint FK_FCSJEDEV_FCSJEJUST;
-- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_JE_DEVOLUCION
add constraint FK_FCS_JE_DEV_INTERCAMBIO foreign key (IDINTERCAMBIO)
  references FCS_JE_INTERCAMBIOS (IDINTERCAMBIO);

insert into fcs_je_tipocertificacion
  (idtipocertificacion, nombre)
values
  (0, 'Certificación ICA');
  insert into fcs_je_tipocertificacion
  (idtipocertificacion, nombre)
values
  (1, 'Certificación CICAC');


-- Drop columns 
alter table FCS_CERTIFICACION drop column DESCRIPCION;
alter table FCS_CERTIFICACION rename column IDJUSTIFICACION to IDINTERCAMBIO;
-- Drop primary, unique and foreign key constraints 
alter table FCS_CERTIFICACION
  drop constraint FK_FCSCERT_FCSJEJUST;
-- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_CERTIFICACION
  add constraint FK_FCSCERT_FCSJEINT foreign key (IDINTERCAMBIO)
  references fcs_je_intercambios (IDINTERCAMBIO);
  
DROP sequence Seq_FCS_JE_CER_ESTADO;
  create sequence Seq_FCS_JE_CERT_ESTADO
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;
  
    
  DROP TABLE FCS_JE_ANEXO_ESTADO;
  DROP TABLE FCS_JE_CERTANEXO_ESTADO;
  DROP TABLE FCS_JE_ANEXO;
  DROP TABLE FCS_JE_CERTIFICACIONANEXO;
  -- Create table
create table FCS_JE_CERTIFICACIONANEXO
(
  IDCERTIFICACIONANEXO     NUMBER(10) not null,
  IDINTERCAMBIO       NUMBER(10) not null,
  FECHAMODIFICACION   DATE not null,
  USUMODIFICACION     NUMBER(5) not null
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
alter table FCS_JE_CERTIFICACIONANEXO
  add constraint FCS_ANEXO_PK primary key (IDCERTIFICACIONANEXO)
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
alter table FCS_JE_CERTIFICACIONANEXO
  add constraint FK_FCSANEXO_FCSJEINT foreign key (IDINTERCAMBIO)
  references FCS_JE_INTERCAMBIOS (IDINTERCAMBIO);


  
  -- Create table
create table FCS_JE_CERTANEXO_ESTADO
(
  IDCERTIFICACIONANEXOESTADO    NUMBER(10) not null,
  IDCERTIFICACIONANEXO NUMBER(10) not null,
  IDESTADO        NUMBER(2) not null,
  IDJEINTERCAMBIO NUMBER(10)
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
alter table FCS_JE_CERTANEXO_ESTADO
  add constraint FCS_JE_ANEXO_ESTADO_PK primary key (IDCERTIFICACIONANEXOESTADO)
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
alter table FCS_JE_CERTANEXO_ESTADO
  add constraint FK_FCSJEANEXOEST_FCSANEXO foreign key (IDCERTIFICACIONANEXOESTADO)
  references FCS_JE_CERTANEXO_ESTADO (IDCERTIFICACIONANEXOESTADO);
alter table FCS_JE_CERTANEXO_ESTADO
  add constraint FK_FCSJEANEXOEST_EST foreign key (IDESTADO)
  references FCS_JE_MAESTROESTADOS (IDESTADO);
alter table FCS_JE_CERTANEXO_ESTADO
  add constraint FK_FCSJEANEXOEST_JEINTERC foreign key (IDJEINTERCAMBIO)
  references JE_INTERCAMBIO (IDJEINTERCAMBIO);

 -- Create sequence 
 
create sequence Seq_FCS_JE_CERTANEXO_ESTADO
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;
 
 
 
 
 
DROP TABLE JE_CERTIFICACION_ICA;
  	-- Create table

  -- Create table
create table JE_CERTIFICACION_ICA
(
  IDJECERTIFICACIONICA    NUMBER(10) not null,
  IDJEINTERCAMBIO         NUMBER(10) not null,
  IDJETRIMESTRE           NUMBER(10) not null,
  COD_ICA                 VARCHAR2(50) not null,
  IMPORTE_DEVOLUCIONES    NUMBER(15,3) not null,
  ASUNTOS_IMPORTE         NUMBER(15,3) not null,
  ASUNTOS_CANTIDAD        NUMBER(5) not null,
  ASUNTOS_IMP_OFI         NUMBER(15,3) not null,
  ASUNTOS_CANT_OFI        NUMBER(5) not null,
  ASUNTOS_IMP_T_ASI        NUMBER(15,3) not null,
  ASUNTOS_CANT_T_ASI       NUMBER(5) not null,
  ASUNTOS_IMP_ASI_P        NUMBER(15,3) not null,
  ASUNTOS_CANT_ASI_P     NUMBER(5) not null,
  
    
  ASUNTOS_CANT_ASI_PI      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_PS      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_PD      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_PDI      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_PC      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_PDD      NUMBER(5) not null,
  
  ASUNTOS_IMP_ASI_V        NUMBER(15,3) not null,
  ASUNTOS_CANT_ASI_V     NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VI      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VS      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VD      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VDI      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VC      NUMBER(5) not null,
  ASUNTOS_CANT_ASI_VDD      NUMBER(5) not null,
  
  NOMBRE_PDF              VARCHAR2(200) not null
    
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
alter table JE_CERTIFICACION_ICA
  add constraint JE_CERTIFICACION_ICA_PK primary key (IDJECERTIFICACIONICA)
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


alter table JE_CERTIFICACION_ICA
  add constraint FK_JECERTICA_TTIM foreign key (IDJETRIMESTRE)
  references JE_TRIMESTRE (IDJETRIMESTRE);
alter table JE_CERTIFICACION_ICA
  add constraint FK_JECERTICA_INTERC foreign key (IDJEINTERCAMBIO)
  references JE_INTERCAMBIO (IDJEINTERCAMBIO);
  
  DROP TABLE 	JE_CERTIFICACION_ANEXO;
	-- Create table
create table JE_CERTIFICACION_ANEXO
(
  IDJECERTIFICACIONANEXO NUMBER(10) not null,
  IDJEINTERCAMBIO        NUMBER(10) not null,
  IDJETRIMESTRE          NUMBER(10) not null,
  ICA                    VARCHAR2(50) not null,
  ORIGEN_DATOS           VARCHAR2(5)not null,
  IMPORTE_DEVOLUCIONES   NUMBER(15,3)not null,
  
  MODULO                    VARCHAR2(50)not null,
  IMPORTEMODULO         NUMBER(15,3) not null,
  CANTIDAD        NUMBER(5) not null,
  IMPORTE         NUMBER(15,3) not null
  
 
	
)
tablespace TS_SIGA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 8K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table JE_CERTIFICACION_ANEXO
  add constraint JE_CERTIFICACION_CICACV1_PK primary key (IDJECERTIFICACIONANEXO)
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
alter table JE_CERTIFICACION_ANEXO
  add constraint FK_JECERTCICAC_TRIME foreign key (IDJETRIMESTRE)
  references JE_TRIMESTRE (IDJETRIMESTRE);
alter table JE_CERTIFICACION_ANEXO
  add constraint FK_JECERTIFCICAC_INT foreign key (IDJEINTERCAMBIO)
  references JE_INTERCAMBIO (IDJEINTERCAMBIO);

     delete from GEN_MENU where idproceso = '711';
delete from GEN_MENU where idproceso = '712';
delete from adm_tiposacceso where idproceso = '711';
delete from adm_tiposacceso where idproceso = '712';
delete from gen_procesos where idproceso = '711';
delete from gen_procesos where idproceso = '712';

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.intercambios', 'Enviament de información económica', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.intercambios', 'Enviament d''informació econòmica', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.intercambios', 'Devolución#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecom.intercambios', 'Devolución#GL', 0, '4', sysdate, 0, '19');

update GEN_PROCESOS set DESCRIPCION = 'Intercambios económicos',TRANSACCION='JGR_E-IntercambiosCatalunya' where idproceso = '710';
update GEN_MENU set IDRECURSO = 'menu.sjcs.ecom.intercambios' where idproceso = '710';
  
-- Create table
create table JE_CERT_VALERRONEO
(
  IDJECERTVALERRONEO NUMBER(10) not null,
  IDCERTESTADO       NUMBER(10) not null,
  DESCERRORGENERAL  CLOB
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
alter table JE_CERT_VALERRONEO
  add constraint PK_JE_CERT_VALERRONEO primary key (IDJECERTVALERRONEO)
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
alter table JE_CERT_VALERRONEO
  add constraint FK_JE_CERT_VALERRONEO foreign key (IDCERTESTADO)
  references FCS_JE_CERT_ESTADO (IDCERTESTADO);

  
  -- Create table
create table JE_CERTANEXO_VALERRONEO
(
  IDJECERTANEXOVALERRONEO NUMBER(10) not null,
  IDCERTANEXOESTADO       NUMBER(10) not null,
  DESCERRORGENERAL  CLOB
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
alter table JE_CERTANEXO_VALERRONEO
  add constraint PK_JE_CERTANEXO_VALERRONEO primary key (IDJECERTANEXOVALERRONEO)
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
alter table JE_CERTANEXO_VALERRONEO
  add constraint FK_JE_CERTANEXO_VALERRONEO foreign key (IDCERTANEXOESTADO)
  references FCS_JE_CERTANEXO_ESTADO (IDCERTIFICACIONANEXOESTADO);
  
   
 create sequence Seq_JE_CERT_VALERRONEO
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;
create sequence Seq_JE_CERTANEXO_VALERRONEO
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
order;

update fcs_je_maestroestados set descripcion = 'Inicial' where idestado = 	10	;
update fcs_je_maestroestados set descripcion = 'Validant ...' where idestado = 	12	;
update fcs_je_maestroestados set descripcion = 'Validat correctament' where idestado = 	14	;
update fcs_je_maestroestados set descripcion = 'Validat erroni' where idestado = 	16	;
update fcs_je_maestroestados set descripcion = 'Enviant Consell ...' where idestado = 	20	;
update fcs_je_maestroestados set descripcion = 'Enviat Consell',PROPIETARIO= 2 where idestado = 	30	;
update fcs_je_maestroestados set descripcion = 'Error Consell' where idestado = 	40	;
update fcs_je_maestroestados set descripcion = 'Finalitzat Consell' where idestado = 	50	;
update fcs_je_maestroestados set descripcion = 'Intercanvi erroni. Fi de procés',PROPIETARIO= 2 where idestado = 	60	;
update fcs_je_maestroestados set descripcion = 'Enviant Generalitat. Generant XML ....' where idestado = 	62	;
update fcs_je_maestroestados set descripcion = 'Enviant Generalitat. Movent XML ...' where idestado = 	65	;
update fcs_je_maestroestados set descripcion = 'Enviat Generalitat.' where idestado = 	70	;
update fcs_je_maestroestados set descripcion = 'En procés XML de Generalitat ...' where idestado = 	75	;
update fcs_je_maestroestados set descripcion = 'Error Generalitat' where idestado = 	85	;
update fcs_je_maestroestados set descripcion = 'Retornat error Generalitat a Consell.' where idestado = 	87	;
update fcs_je_maestroestados set descripcion = 'Verificat correctament Generalitat' where idestado = 	90	;
update fcs_je_maestroestados set descripcion = 'Responent ICA ...' where idestado = 	92	;
update fcs_je_maestroestados set descripcion = 'Intercanvi lliurat. Fi de procés',PROPIETARIO= 2 where idestado = 	95	;



drop table fcs_je_justifica_facturacionjg ;
drop table FCS_JE_DEVOL_MOV_VARIO ;
drop table je_cert_ica_guardia;
drop SEQUENCE seq_fcs_je_devol_mov_vario;

drop SEQUENCE seq_fcs_je_justifica_facturaci;
drop SEQUENCE seq_je_cert_ica_guardia;

-- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_JE_CERTANEXO_ESTADO
  drop constraint FK_FCSJEANEXOEST_FCSANEXO;
-- Create/Recreate primary, unique and foreign key constraints 
alter table FCS_JE_CERTANEXO_ESTADO
  add constraint FK_FCSJEANEXOEST_FCSANEXO foreign key (IDCERTIFICACIONANEXO)
  references fcs_je_certificacionanexo (IDCERTIFICACIONANEXO);

--Habria que ñadir esto en el script o 
--quitar la linea de arriba update GEN_PROCESOS set DESCRIPCION = 'Intercambios económicos',TRANSACCION='JGR_E-IntercambiosCatalunya' where idproceso = '710';
--o qyuitar update GEN_MENU set IDRECURSO = 'menu.sjcs.ecom.intercambios' where idproceso = '710';
  update GEN_PROCESOS set DESCRIPCION = 'Intercambios económicos',TRANSACCION='JGR_E-Comunicaciones_Justificacion' where idproceso = '710';
  update GEN_MENU set IDRECURSO = 'menu.sjcs.ecom.justificacion' where idproceso = '710';
  update gen_Recursos set descripcion = 'Envio de información económica' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 1;
update gen_Recursos set descripcion = 'Enviament de información económica' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 2;
update gen_Recursos set descripcion = 'Envio de información económica#GL' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 3;
update gen_Recursos set descripcion = 'Enviament de información económica#EU' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 4;

drop table FCS_JE_CERTIFICACION;

-- Create table
create table FCS_JE_CERTIFICACION
(
  IDCERTIFICACION     NUMBER(10) not null,
  IDTIPOCERTIFICACION NUMBER(1) not null,
  IDINTERCAMBIO       NUMBER(10) not null,
  FECHAMODIFICACION   DATE not null,
  USUMODIFICACION     NUMBER(5) not null
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
alter table FCS_JE_CERTIFICACION
  add constraint FCS_CERTIFICACION_PK primary key (IDCERTIFICACION)
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
alter table FCS_JE_CERTIFICACION
  add constraint FK_FCSCERT_FCSJEINT foreign key (IDINTERCAMBIO)
  references FCS_JE_INTERCAMBIOS (IDINTERCAMBIO);
alter table FCS_JE_CERTIFICACION
  add constraint FK_FCSCERT_FCSJETIPOCERT foreign key (IDTIPOCERTIFICACION)
  references FCS_JE_TIPOCERTIFICACION (IDTIPOCERTIFICACION);

  
  alter table FCS_JE_CERT_ESTADO
  add constraint FK_FCSJECERTEST_FCSCERT foreign key (IDCERTIFICACION)
  references FCS_JE_CERTIFICACION (IDCERTIFICACION);

-- 2019-12-12 - Ejecutado en Integracion por AAG
  
  
  
  -----
  
  insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.comision', 'Enviar Intercambio GEN', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.comision', 'Enviar Intercambio GEN#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.comision', 'Enviar Intercambio GEN#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.comision', 'Enviar Intercambio GEN#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.ica', 'Enviar Respuesta ICA', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.ica', 'Enviar Respuesta ICA#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.ica', 'Enviar Respuesta ICA#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.ica', 'Enviar Respuesta ICA#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.consejo', 'Enviar Intercambio CICAC', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.consejo', 'Enviar Intercambio CICAC#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.consejo', 'Enviar Intercambio CICAC#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('intercambios.boton.enviar.consejo', 'Enviar Intercambio CICAC#GL', 0, '4', sysdate, 0, '19');

 update gen_diccionario set descripcion = 'Envio de información económica' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 1;
update gen_diccionario set descripcion = 'Enviament de información económica' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 2;
update gen_diccionario set descripcion = 'Envio de información económica#GL' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 3;
update gen_diccionario set descripcion = 'Enviament de información económica#EU' where IDRECURSO = 'menu.sjcs.ecom.justificacion' and idlenguaje = 4;

-- 2019-12-13 - Ejecutado en Integracion por AAG

-- SIGA_128_010
Cambio en CEN_COLEGIADO_AI

-- 2020-02-04 - Ejecutado en Integracion por AAG

Cambio en PKG_SIGA_REGULARIZACION_SJCS

-- Se entrega como adjunto a la ultima entrega para que quede registrado

-- SIGA_128_013

prompt 01_quitando_logs.log
Select * From Gen_Properties pro Where pro.Parametro Like '%log4j%' And pro.parametro In ('log4j.category.com.pra', 'log4j.category.org.apache');
Select * From Gen_Properties pro Where pro.Parametro Like '%log4j%' And valor Like '%DEBUG%';
Delete From Gen_Properties pro Where pro.Parametro In ('log4j.category.com.pra');
Update Gen_Properties pro Set pro.Valor = 'DEBUG' Where pro.Parametro In ('log4j.category.org.apache');
Update Gen_Properties pro Set pro.Valor = Replace (pro.valor, 'DEBUG', 'ERROR') Where pro.Parametro Like '%log4j%' And valor Like '%DEBUG%';


F_SIGA_GETRECURSO_ETIQUETA


prompt 21_desactivacion_logs_struts_nanc.log
Select * From Gen_Properties pro Where pro.Parametro Like '%PRA%';
Select * From Gen_Properties pro Where pro.Parametro Like '%STRUTS%';
Delete From Gen_Properties pro Where pro.Parametro Like '%PRA%';
Delete From Gen_Properties pro Where pro.Parametro Like '%STRUTS%';


prompt 22_desactivacion_logs_struts_nanc.log
Select * from gen_properties where parametro = 'log4j.category.com.pra';
Select * from gen_properties where parametro = 'log4j.category.org.apache';
delete from gen_properties where parametro = 'log4j.category.com.pra';
delete from gen_properties where parametro = 'log4j.category.org.apache';

-- Ejecutado ya todo en Integracion y PREproduccion
modificar v_siga_cat_justificacion

f_comunicaciones_ejg_2003_CAB

-- Add/modify columns 
alter table SCS_PERSONAJG add NOTIFICACIONTELEMATICA VARCHAR2(1);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.notificacionTelematica', 'Solicita notificaciones telemáticas', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.notificacionTelematica', 'Sol·licita notificacions telemàtiques', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.notificacionTelematica', 'Solicita notificaciones telemáticas#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.notificacionTelematica', 'Solicita notificaciones telemáticas#GL', 0, '4', sysdate, 0, '19');

-- 2020-05-25 - Ejecutado en Integracion por AAG

alter table PCAJG_ALC_INT_SOL add SOL21_NOTIFICA_TELEM CHAR(1);
alter table PCAJG_ALC_INT_SOL add SOL22_TIPOINTERVENCION VARCHAR2(3);

-- 2020-05-26 - Ejecutado en Integracion por AAG

--VALENCIA
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.numeroMaximoExpRemesas', 'Número máximo de expedientes que puede contener una remesa(0: No hay limite)', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.numeroMaximoExpRemesas', 'Nombre màxim d''expedients que pot contenir una remesa (0: No hi ha límit)', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.numeroMaximoExpRemesas', 'Número máximo de expedientes que puede contener una remesa(0: No hay limite)#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.numeroMaximoExpRemesas', 'Número máximo de expedientes que puede contener una remesa(0: No hay limite)#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.aviso.numeroMaximoExpRemesas', 'Se ha superado el número máximo de expedientes que puede contener una remesa', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.aviso.numeroMaximoExpRemesas', 'S''ha superat el nombre màxim d''expedients que pot contenir una remesa', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.aviso.numeroMaximoExpRemesas', 'Se ha superado el número máximo de expedientes que puede contener una remesa#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.aviso.numeroMaximoExpRemesas', 'Se ha superado el número máximo de expedientes que puede contener una remesa#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('SCS', 'REMESAS_NUM_MAXIMO_EXP', '0', SYSDATE, 1, 0, 'scs.parametro.numeroMaximoExpRemesas', null);

-- SIGARNV-1783

Insert Into Adm_Contador
  (Idinstitucion, Idcontador, Nombre, Descripcion, 
   Modificablecontador, Modo, Contador, Prefijo,
   Sufijo, Longitudcontador, Fechareconfiguracion, Reconfiguracioncontador, Reconfiguracionprefijo,
   Reconfiguracionsufijo, Idtabla, Idcampocontador, Idcampoprefijo, Idcamposufijo, General,
   Fechamodificacion, Usumodificacion, 
   Idmodulo, Fechacreacion, Usucreacion)
  (Select Idinstitucion,
          Idcontador || '_FCS',
          Nombre || ' de pagos SJCS',
          Descripcion || ' de pagos SJCS',
          Modificablecontador, Modo, Contador, 'TO' || prefijo,
          Sufijo, Longitudcontador, Fechareconfiguracion, Reconfiguracioncontador, Reconfiguracionprefijo,
          Reconfiguracionsufijo, Idtabla, Idcampocontador, Idcampoprefijo, Idcamposufijo, General,
          Fechamodificacion, Usumodificacion,
          7,
          Sysdate,
          -1
     From Adm_Contador Con
    Where Con.Idcontador = 'FAC_ABONOS');
Update Adm_Contador Con
   Set Idcontador    = Idcontador || '_GENERAL',
       Nombre        = Nombre || ' de facturas',
       Descripcion   = Descripcion || ' de pagos SJCS',
       Fechacreacion = Sysdate,
       Usucreacion   = -1
 Where Con.Idcontador = 'FAC_ABONOS';

Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select replace(idrecurso, 'serios', 'series') || '.facturas', Case idlenguaje When '1' Then 'Contador de facturas' When '2' Then 'Comptador de factures' When '3' Then 'Contador de facturas#EU' When '4' Then 'Contador de facturas#GL' End, error, idlenguaje, Sysdate, 0, idpropiedad
     From Gen_Recursos rec Where rec.Idrecurso = 'facturacion.serios.literal.contador');
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select replace(idrecurso, 'serios', 'series') || '.abonos', Case idlenguaje When '1' Then 'Contador de abonos' When '2' Then 'Comptador d''abonaments' When '3' Then 'Contador de abonos#EU' When '4' Then 'Contador de abonos#GL' End, error, idlenguaje, Sysdate, 0, idpropiedad
     From Gen_Recursos rec Where rec.Idrecurso = 'facturacion.serios.literal.contador');
Delete From Gen_Recursos rec Where rec.Idrecurso = 'facturacion.serios.literal.contador';

alter table FAC_SERIEFACTURACION add IDCONTADOR_ABONOS VARCHAR2(20);
Update FAC_SERIEFACTURACION Set IDCONTADOR_ABONOS = 'FAC_ABONOS_GENERAL';
alter table FAC_SERIEFACTURACION Modify IDCONTADOR_ABONOS Not Null;

Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorFacturas.longitud.prefijo', Case idlenguaje When '2' Then 'Error en el nou comptador de factures: ' Else 'Error en el nuevo contador de facturas: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.longitud.prefijo');
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorAbonos.longitud.prefijo', Case idlenguaje When '2' Then 'Error en el nou comptador d''abonaments: ' Else 'Error en el nuevo contador de abonos: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.longitud.prefijo');

Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorFacturas.longitud.sufijo', Case idlenguaje When '2' Then 'Error en el nou comptador de factures: ' Else 'Error en el nuevo contador de facturas: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.longitud.sufijo');
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorAbonos.longitud.sufijo', Case idlenguaje When '2' Then 'Error en el nou comptador d''abonaments: ' Else 'Error en el nuevo contador de abonos: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.longitud.sufijo');

Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorFacturas.noNumerico.contador', Case idlenguaje When '2' Then 'Error en el nou comptador de factures: ' Else 'Error en el nuevo contador de facturas: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.noNumerico.contador');
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorAbonos.noNumerico.contador', Case idlenguaje When '2' Then 'Error en el nou comptador d''abonaments: ' Else 'Error en el nuevo contador de abonos: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.noNumerico.contador');

Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorFacturas.obligatorio.contador', Case idlenguaje When '2' Then 'Error en el nou comptador de factures: ' Else 'Error en el nuevo contador de facturas: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.obligatorio.contador');
Insert Into gen_recursos
  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  (Select 'Facturacion.mensajes.contadorAbonos.obligatorio.contador', Case idlenguaje When '2' Then 'Error en el nou comptador d''abonaments: ' Else 'Error en el nuevo contador de abonos: ' End || descripcion, error, idlenguaje, Sysdate, 0, idpropiedad
  From Gen_Recursos rec Where rec.Idrecurso = 'Facturacion.mensajes.obligatorio.contador');

alter table FAC_SERIEFACTURACION
  add constraint FK_ADM_CONTADOR_ABONOS foreign key (IDINSTITUCION, IDCONTADOR_ABONOS)
  references adm_contador (IDINSTITUCION, IDCONTADOR)
  deferrable;
create index IDX_FK_ADM_CONTADOR_ABONOS on FAC_SERIEFACTURACION (IDINSTITUCION, IDCONTADOR_ABONOS);

-- 2020-06-03 - Ejecutado en Integracion por AAG

alter table SCS_CARACTASISTENCIA add VIOLENCIACONTRAMUJER varchar2(1);
alter table SCS_CARACTASISTENCIA add TEMASINDEFINIR varchar2(1);
alter table SCS_CARACTASISTENCIA add VIOLENCIAINTRAFAMILIAR varchar2(1);
alter table SCS_CARACTASISTENCIA add CONTRALALIBERTADSEXUAL varchar2(1);
comment on column SCS_CARACTASISTENCIA.CONTRALALIBERTADSEXUAL
  is 'Nuevo en 2020';


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaIntrafamiliar', 'Violencia intrafamiliar', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaIntrafamiliar', 'Violencia intrafamiliar#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaIntrafamiliar', 'Violencia intrafamiliar#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaIntrafamiliar', 'Violencia intrafamiliar#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaContraMujer', 'Violencia contra la mujer', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaContraMujer', 'Violencia contra la mujer#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaContraMujer', 'Violencia contra la mujer#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.violenciaContraMujer', 'Violencia contra la mujer#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.temaSinDefinir', 'Sin definir', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.temaSinDefinir', 'Sin definir#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.temaSinDefinir', 'Sin definir#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.temaSinDefinir', 'Sin definir#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.caracteristicas.literal.ambos', 'Si, ambos', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.caracteristicas.literal.ambos', 'Si, ambos#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.caracteristicas.literal.ambos', 'Si, ambos#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.caracteristicas.literal.ambos', 'Si, ambos#GL', 0, '4', sysdate, 0, '19');


insert into gen_catalogos_multiidioma
  (codigo, nombretabla, campotabla, fechamodificacion, usumodificacion, local, codigotabla, migrado)
values
  (835, 'SCS_ORIGENCONTACTO', 'DESCRIPCION', SYSDATE, 0, 'S', 'IDORIGENCONTACTO', 'S');
  
  
insert into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
(select '9M0', idmodulo, traza, target, sysdate, 0, descripcion, 'JGR_DocumentacionAsistenciaLetrado', '9S0', nivel from gen_procesos where idproceso='9Z7');

insert into gen_pestanas
  (idproceso, idlenguaje, idrecurso, posicion, idgrupo, tipoacceso)
(select '9M0', idlenguaje, idrecurso, 9, 'LETASIST', tipoacceso from gen_pestanas where idproceso='9Z7')  ;


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('sjcs.remesas.error.faltaDocumentacion', 'El expediente no tiene la documentación mínima requerida. Además de añadir los ficheros necesarios compruebe que ha introducido la fecha de presentación.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('sjcs.remesas.error.faltaDocumentacion', 'L''expedient no té la documentació mínima requerida. A més d''afegir els fitxers necessaris comprovi que ha introduït la data de presentació.', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('sjcs.remesas.error.faltaDocumentacion', 'El expediente no tiene la documentación mínima requerida. Además de añadir los ficheros necesarios compruebe que ha introducido la fecha de presentaciÃ³n.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('sjcs.remesas.error.faltaDocumentacion', 'El expediente no tiene la documentación mínima requerida. Además de añadir los ficheros necesarios compruebe que ha introducido la fecha de presentaciÃ³n.#GL', 0, '4', sysdate, 0, '19');


--coger PARA LO DE RSOLUCIONES DE BURGOS
UPDATE CAJG_TIPOREMESA SET JAVACLASS = 'com.siga.gratuita.pcajg.resoluciones.ResolucionesFicheroAlcala',fechamodificacion = sysdate WHERE IDINSTITUCION = 2013 AND IDTIPOREMESA = 1;
insert into CAJG_ERRORESREMESARESOL (IDERRORESREMESARESOL, IDINSTITUCION, CODIGO, DESCRIPCION) values (26, 2013, '26', 'Fichero de resolución no encontrado {0}');
PROC_SIGA_RESOL_2013


--SIGA-470
CEN_DATOSCOLEGIALESESTADO_AF.trg
CEN_DATOSCOLEGIALESESTADO_BF.trg
PROC_CALC_SITUACIONEJERCICIO.prc

-- Ejecutado en Integracion 09/09/2020 11:00

--https://redabogacia.atlassian.net/browse/SIGA-317
Insert Into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
Values
  ('9T3', 'JGR', 1, 'Y', Sysdate, 0, 'HIDDEN_AsistenciaActuaciones', 'JGR_ActuacionesAsistencia', '95O', '10');
Insert Into Gen_Recursos
  (Idrecurso, Descripcion, Error, Idlenguaje, Fechamodificacion, Usumodificacion, Idpropiedad)
  (Select 'pestana.justiciagratuitaasistencia.actuaciones.actuacion',
          Case To_Number(Idlenguaje)
            When 1 Then
             'Información de la actuación'
            When 2 Then
             'Informació de l''actuació'
            When 3 Then
             'Información de la actuación#EU'
            When 4 Then
             'Información de la actuación#GL'
          End,
          Error,
          Idlenguaje,
          Sysdate,
          0,
          Idpropiedad
     From Gen_Recursos Rec
    Where Rec.Idrecurso = 'pestana.justiciagratuitaasistencia.actuaciones');

Insert Into Adm_Tiposacceso
  (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
  (Select '9T3', Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion
     From Adm_Tiposacceso
    Where Idproceso = '95O');

Insert Into gen_pestanas
  (idproceso, idlenguaje, idrecurso, posicion, idgrupo)
Values
  ('9T3', 1, 'pestana.justiciagratuitaasistencia.actuaciones.actuacion', 1, 'ACTASIST');

INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.volantesExpres.literal.registroVolante', 'Registro de volante', 0, '1', sysdate, 0, '19');
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.volantesExpres.literal.registroVolante', 'Registre de Butlleta', 0, '2', sysdate, 0, '19');
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.volantesExpres.literal.registroVolante', 'Registro de volante#EU', 0, '3', sysdate, 0, '19');
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.volantesExpres.literal.registroVolante', 'Registro de volante#GL', 0, '4', sysdate, 0, '19');

-- Ejecutado en Integracion 10/09/2020 10:00

-- SIGA-490
UPDATE GEN_PROCESOS SET descripcion = 'HIDDEN_InfoActuacionAsistencia'
WHERE idproceso = '9T3';
Insert Into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
Values
  ('9T4', 'JGR', 1, 'Y', Sysdate, 0, 'HIDDEN_InfoActuacionAsistenciaLetrado', 'JGR_ActuacionAsistenciaLetrado', '9T0', '10');
Insert Into Adm_Tiposacceso
  (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
  (Select '9T4', Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion
     From Adm_Tiposacceso
    Where Idproceso = '9T0');
Insert Into gen_pestanas
  (idproceso, idlenguaje, idrecurso, posicion, idgrupo)
Values
  ('9T4', 1, 'pestana.justiciagratuitaasistencia.actuaciones.actuacion', 1, 'LETACTASI');
  
  
  
  
  
 update GEN_RECURSOS set descripcion = 'Debería ser una cadena de 19 caracteres con el formato [nnnnn nn A yyyy nnnnnnn] donde [n] es un número, [y] corresponde a un año y [a] es alfanumérico de valores S,C,P,O,I,V,M,0,1,2,3,4,6,8' WHERE idrecurso =  'gratuita.nig.formato.cadeca' and idlenguaje = 1 ;=======
-- Ejecutado en Integracion 10/09/2020 10:00

-- SIGA-490
UPDATE GEN_PROCESOS SET descripcion = 'HIDDEN_InfoActuacionAsistencia'
WHERE idproceso = '9T3';
Insert Into gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
Values
  ('9T4', 'JGR', 1, 'Y', Sysdate, 0, 'HIDDEN_InfoActuacionAsistenciaLetrado', 'JGR_ActuacionAsistenciaLetrado', '9T0', '10');
Insert Into Adm_Tiposacceso
  (Idproceso, Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion)
  (Select '9T4', Idperfil, Fechamodificacion, Usumodificacion, Derechoacceso, Idinstitucion
     From Adm_Tiposacceso
    Where Idproceso = '9T0');
Insert Into gen_pestanas
  (idproceso, idlenguaje, idrecurso, posicion, idgrupo)
Values
  ('9T4', 1, 'pestana.justiciagratuitaasistencia.actuaciones.actuacion', 1, 'LETACTASI');
  
  
  update GEN_RECURSOS set descripcion = 'Debería ser una cadena de 19 caracteres con el formato [nnnnn nn A yyyy nnnnnnn] donde [n] es un número, [y] corresponde a un año y [a] es alfanumérico de valores S,C,P,O,I,V,M,0,1,2,3,4,6,8' WHERE idrecurso =  'gratuita.nig.formato.cadeca' and idlenguaje = 1 ;
  
  
  insert into SCS_MAESTROESTADOSEJG (IDESTADOEJG, DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION, CODIGOEXT, BLOQUEADO, ORDEN, VISIBLECOMISION, CODIGOEJIS, FECHA_BAJA)
values (25, '150025', sysdate, 0, '25', 'S', 80, '0', null, null);

insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('150025', 'Generado envío a comisión', '1', sysdate, 0, null, 'SCS_MAESTROESTADOSEJG', 'DESCRIPCION', 'scs_maestroestadosejg.descripcion.0.25');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('150025', 'Generado envío a comisión#CA', '2', sysdate, 0, null, 'SCS_MAESTROESTADOSEJG', 'DESCRIPCION', 'scs_maestroestadosejg.descripcion.0.25');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('150025', 'Generado envío a comisión#EU', '3', sysdate, 0, null, 'SCS_MAESTROESTADOSEJG', 'DESCRIPCION', 'scs_maestroestadosejg.descripcion.0.25');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('150025', 'Generado envío a comisión#GL', '4', sysdate, 0, null, 'SCS_MAESTROESTADOSEJG', 'DESCRIPCION', 'scs_maestroestadosejg.descripcion.0.25');


-- Create table
create table ECOM_INTERCAMBIO
(
  IDECOMINTERCAMBIO NUMBER(10) not null,
  IDECOMCOLA        NUMBER(10) not null,
  DESCRIPCION       VARCHAR2(500) not null,
  FECHAENVIO        TIMESTAMP(6) not null,
  FECHARESPUESTA    TIMESTAMP(6),
  IDESTADORESPUESTA NUMBER(2),
  RESPUESTA         CLOB,
  IDINSTITUCION     NUMBER(4) not null
)
tablespace TS_SIGA_CMN
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 10M
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table ECOM_INTERCAMBIO
  add constraint PK_ECOM_INTERCAMBIO primary key (IDECOMINTERCAMBIO)
  using index 
  tablespace TS_SIGA_CMN_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 5M
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table ECOM_INTERCAMBIO
  add constraint FK_ECOM_INTERCAMBIO_COLA foreign key (IDECOMCOLA)
  references ECOM_COLA (IDECOMCOLA)
  deferrable;
alter table ECOM_INTERCAMBIO
  add constraint FK_ECOM_INTERCAMBIO_ESTADOS foreign key (IDESTADORESPUESTA)
  references ECOM_ESTADOSCOLA (IDESTADOCOLA)
  deferrable;
alter table ECOM_INTERCAMBIO
  add constraint FK_ECOM_INTERCAMBIO_INST foreign key (IDINSTITUCION)
  references CEN_INSTITUCION (IDINSTITUCION);



  create sequence SEQ_ECOM_INTERCAMBIO minvalue 1 maxvalue 9999999999 start with 1 increment by 1 nocache;
  
  insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.justiciagratuitaejg.intercambiosJG', 'Intercambios JG', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.justiciagratuitaejg.intercambiosJG', 'Intercambios JG#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.justiciagratuitaejg.intercambiosJG', 'Intercambios JG#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.justiciagratuitaejg.intercambiosJG', 'Intercambios JG#GL', 0, '4', sysdate, 0, '19');


insert into 
GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL) 
values ('940','JGR', 1, 'Y', SYSDATE,0 ,'EJGIntercambios Pericles', 'JGR_IntercambiosJG', 946,10);


insert into 
GEN_PESTANAS (idproceso, Idlenguaje, Idrecurso, Posicion, Idgrupo) 
values(940,1, 'pestana.justiciagratuitaejg.intercambiosJG', 6, 'EJG');

-- Add/modify columns 
alter table CEN_COLEGIOPROCURADOR modify CODIGOEJIS VARCHAR2(12);

UPDATE gen_pestanas set posicion = 10 where IDPROCESO = '940';


-- Add/modify columns 
alter table SCS_JURISDICCION add JUSTIFICAR_SIN_RESOL CHAR(1) default 0;
-- Add comments to the columns 
comment on column SCS_JURISDICCION.JUSTIFICAR_SIN_RESOL
  is 'Se permite la justificacion de actuaciones de designacion aunque no exista resolucion inclusives las pendientes';
  UPDATE scs_jurisdiccion SET JUSTIFICAR_SIN_RESOL = 1 WHERE IDJURISDICCION IN (1,9);
  
  CREATE TABLE CEN_GRUPOSINSTITUCION (
    IDGRUPO NUMBER(4,0) NOT NULL,
    FECHAMODIFICACION DATE NOT NULL,
    USUMODIFICACION NUMBER(5,0) NOT NULL,
    NOMBRE VARCHAR2(255) NOT NULL,
    CODIGO VARCHAR2(10) NOT NULL,
    BLOQUEADO VARCHAR2(1) NOT NULL,
    CONSTRAINT PK_CEN_GRUPOSINSTITUCION PRIMARY KEY (IDGRUPO)
);

CREATE UNIQUE INDEX UK_GRUPOSINSTITUCION ON CEN_GRUPOSINSTITUCION (NOMBRE); 
CREATE TABLE CEN_GRUPOSINSTITUCION_INSTITU (
    IDGRUPO NUMBER(4,0) NOT NULL,
    IDINSTITUCION NUMBER(4,0) NOT NULL,
    FECHAMODIFICACION DATE NOT NULL,
    USUMODIFICACION NUMBER(5,0) NOT NULL,
    FECHA_BAJA DATE NULL,
    FECHA_INICIO DATE NOT NULL,
    CONSTRAINT PK_CEN_GRUPOSINSTITU_INSTITU PRIMARY KEY (IDGRUPO,IDINSTITUCION),
    CONSTRAINT FK_GRUPOSINSTITU_INSTITU_GRUPO FOREIGN KEY (IDGRUPO) REFERENCES CEN_GRUPOSINSTITUCION(IDGRUPO),
    CONSTRAINT FK_CEN_INSTITU_GRUPOSINSTITU FOREIGN KEY (IDINSTITUCION) REFERENCES CEN_INSTITUCION(IDINSTITUCION)
);

CREATE INDEX IND_FK_GRUPOSINSTITUCION_GRUPO ON CEN_GRUPOSINSTITUCION_INSTITU (IDGRUPO);
CREATE INDEX IND_FK_GRUPOSINSTITUCION_INST ON CEN_GRUPOSINSTITUCION_INSTITU (IDINSTITUCION);
INSERT into CEN_GRUPOSINSTITUCION(idgrupo, fechamodificacion, usumodificacion, nombre, codigo, bloqueado)
values(1, sysdate, 0, 'Zona comun Ministerio Justicia', 'COMUN_MINI', 'S');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2002	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2060	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2065	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2009	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2010	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2011	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2013	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2014	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2020	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2022	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2054	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2070	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2074	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2078	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2082	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2031	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2050	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2019	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2040	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2042	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2049	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2067	, sysdate, 0, '01/01/2000');
INSERT into CEN_GRUPOSINSTITUCION_INSTITU(idgrupo, idinstitucion, fechamodificacion, usumodificacion, fecha_inicio) values(1,	2017	, sysdate, 0, '01/01/2000');

--Ya ejecutado en Produccion con caracter de urgencia: 
ALTER TABLE SCS_DOCUMENTACIONDESIGNA MODIFY IDDOCUMENTACIONDES NUMBER(10,0);

--https://redabogacia.atlassian.net/browse/SIGA-541
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('censo.consultaDatosColegiacion.literal.comunitario','Inscrito',0,'1',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('censo.consultaDatosColegiacion.literal.comunitario','Inscrit',0,'2',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('censo.consultaDatosColegiacion.literal.comunitario','Inscrito#EU',0,'3',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('censo.consultaDatosColegiacion.literal.comunitario','Inscrito#GL',0,'4',sysdate,0,'19');


-- Cargar V_PCAJG_FAMILIARES y V_PCAJG_EJG


-- https://redabogacia.atlassian.net/browse/SIGA-561 
V_SIGA_SOLICITANTES_EJG, V_SIGA_DEFENDIDOS_DESIGNA 

-- https://redabogacia.atlassian.net/browse/SIGA-579
ALTER SEQUENCE SEQ_ENV_ENVIOS INCREMENT BY 1 MINVALUE 1 MAXVALUE 9999999 CYCLE NOCACHE NOORDER ;




update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Se va a proceder a tramitar la petición a los organismos competentes: Ag. Tributaria, TGSS, INSS, etc. Para ello es necesario el consentimiento expreso del solicitante. Si continúa se marcará la casilla del solicitante ''Autoriza recabar información de la administración'' ¿Desea continuar?' where idrecurso='gratuita.eejg.message.confirmaSolicitud' and idlenguaje='1';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Es procedirà a tramitar la petició als organismes competents: Ag. Tributària, TGSS, INSS, etc. Per això és necessari el consentiment exprés del sol·licitant. Si continua es marcarà la casella del sol·licitant ''Autoritza demanar informació de l''administració''. Voleu continuar?' where idrecurso='gratuita.eejg.message.confirmaSolicitud' and idlenguaje='2';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Se va a proceder a tramitar la petición a los organismos competentes: Ag. Tributaria, TGSS, INSS, etc. Para ello es necesario el consentimiento expreso del solicitante. Si continúa se marcará la casilla del solicitante ''Autoriza recabar información de la administración'' ¿Desea continuar?' where idrecurso='gratuita.eejg.message.confirmaSolicitud' and idlenguaje='4';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Se va a proceder a tramitar la petición a los organismos competentes: Ag. Tributaria, TGSS, INSS, etc. Para ello es necesario el consentimiento expreso del solicitante. Si continúa se marcará la casilla del solicitante ''Autoriza recabar información de la administración'' ¿Desea continuar?' where idrecurso='gratuita.eejg.message.confirmaSolicitud' and idlenguaje='3';


INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('gratuita.eejg.error.solicitantenoautoriza','El usuario no ha autorizado el recabar información de las administraciones. No tiene permiso para continuar.',0,'1',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('gratuita.eejg.error.solicitantenoautoriza','L''usuari no ha autoritzat demanar informació de les administracions. No teniu permís per continuar.',0,'2',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('gratuita.eejg.error.solicitantenoautoriza','El usuario no ha autorizado el recabar información de las administraciones. No tiene permiso para continuar.#EU',0,'3',sysdate,0,'19');
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD) VALUES ('gratuita.eejg.error.solicitantenoautoriza','El usuario no ha autorizado el recabar información de las administraciones. No tiene permiso para continuar.#GL',0,'4',sysdate,0,'19');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderok', 'Enviar respuesta correcta', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderok', 'Enviar resposta correcta', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderok', 'Enviar respuesta correcta#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderok', 'Enviar respuesta correcta#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderko', 'Enviar respuesta incorrecta', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderko', 'Enviar resposta incorrecta', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderko', 'Enviar respuesta incorrecta#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.responderko', 'Enviar respuesta incorrecta#GL', 0, '4', sysdate, 0, '19');

   
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.file.extension', 'El tipo de archivo no está permitido. Los tipos de archivos permitidos son  {0}', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.file.extension', 'El tipus de fitxer no està permès. Els tipus de fitxers permesos són {0}', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.file.extension', 'El tipo de archivo no está permitido. Los tipos de archivos permitidos son  {0}#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.file.extension', 'El tipo de archivo no está permitido. Los tipos de archivos permitidos son  {0}#GL', 0, '4', sysdate, 0, '19');


--https://redabogacia.atlassian.net/browse/SIGA-605
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD,IDLENGUAJE) 
VALUES ('env.parametro.defaultemailfrom','Cuenta de correo electrónico desde donde se envían las comunicaciones y envíos manuales de SIGA classique. IMPORTANTE: Si se cambia, no llegarán los correos a los destinatarios. Antes de cambiarla en el colegio, es necesario comunicarlo a Soporte para dar de alta la dirección en el servidor de correo.',0,sysdate,0,'19',1);
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD,IDLENGUAJE) 
VALUES ('env.parametro.defaultemavlfrom','Cuenta de correo electrónico desde donde se envían las comunicaciones y envíos manuales de SIGA classique. IMPORTANTE: Si se cambia, no llegarán los correos a los destinatarios. Antes de cambiarla en el colegio, es necesario comunicarlo a Soporte para dar de alta la dirección en el servidor de correo.#CA',0,sysdate,0,'19',2);
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD,IDLENGUAJE) 
VALUES ('env.parametro.defaultemailfrom','Cuenta de correo electrónico desde donde se envían las comunicaciones y envíos manuales de SIGA classique. IMPORTANTE: Si se cambia, no llegarán los correos a los destinatarios. Antes de cambiarla en el colegio, es necesario comunicarlo a Soporte para dar de alta la dirección en el servidor de correo.#EU',0,sysdate,0,'19',3);
INSERT INTO GEN_RECURSOS (IDRECURSO,DESCRIPCION,ERROR,FECHAMODIFICACION,USUMODIFICACION,IDPROPIEDAD,IDLENGUAJE) 
VALUES ('env.parametro.defaultemailfrom','Cuenta de correo electrónico desde donde se envían las comunicaciones y envíos manuales de SIGA classique. IMPORTANTE: Si se cambia, no llegarán los correos a los destinatarios. Antes de cambiarla en el colegio, es necesario comunicarlo a Soporte para dar de alta la dirección en el servidor de correo.#GL',0,sysdate,0,'19',4);
INSERT INTO GEN_PARAMETROS (MODULO,PARAMETRO,VALOR,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,IDRECURSO) 
VALUES ('ENV','DEFAULT_EMAIL_FROM','comunicaciones.siga@redabogacia.org', sysdate,0,0,'env.parametro.defaultemailfrom');

--https://redabogacia.atlassian.net/browse/SIGA-605
delete from GEN_PARAMETROS where PARAMETRO = 'DEFAULT_EMAIL_FROM' and IDINSTITUCION <> 0;
update GEN_PARAMETROS set valor = '-' where PARAMETRO = 'DEFAULT_EMAIL_FROM' and IDINSTITUCION = 0;

CREATE OR REPLACE VIEW V_SIGA_CAT_ANEXOCICAC AS

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.intercambio.cicac', 'Antes de continuar, debe finalizar los intercambios de los siguientes colegios:', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.intercambio.cicac', 'Antes de continuar, debe finalizar los intercambios de los siguientes colegios:#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.intercambio.cicac', 'Antes de continuar, debe finalizar los intercambios de los siguientes colegios:#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.intercambio.cicac', 'Antes de continuar, debe finalizar los intercambios de los siguientes colegios:#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('success.intercambio.cicac', 'Todos los intercambios de todos los colegios han finalizado satisfactoriamente', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('success.intercambio.cicac', 'Todos los intercambios de todos los colegios han finalizado satisfactoriamente#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('success.intercambio.cicac', 'Todos los intercambios de todos los colegios han finalizado satisfactoriamente#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('success.intercambio.cicac', 'Todos los intercambios de todos los colegios han finalizado satisfactoriamente#GL', 0, '4', sysdate, 0, '19');

alter table FCS_JE_INTERCAMBIOS add FECHACREACION date;
UPDATE FCS_JE_INTERCAMBIOS SET FECHACREACION = FECHAMODIFICACION;
alter table FCS_JE_INTERCAMBIOS modify FECHACREACION not null;

--

--cambiamos la modal de busqueda
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.busquedapor', 'Búsqueda por', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.busquedapor', 'Búsqueda por#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.busquedapor', 'Recerca per', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.literal.busquedapor', 'Búsqueda por#EU', 0, '3', sysdate, 0, '19');
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.busqueda.personas.titulo', 'Búsqueda general de personas', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.busqueda.personas.titulo', 'Búsqueda general de personas#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.busqueda.personas.titulo', 'Recerca general de persones''', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.busqueda.personas.titulo', 'Búsqueda general de personas#EU', 0, '3', sysdate, 0, '19');
 
 insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR) values ('SIGA', 'mail.smtp.actualizacioncenso.sesion', 'CorreoSIGA_SIB');

--SIGA-581
 Pkg_Siga_Retenciones_Sjcs

--SIGA-715
create or replace directory FCS_EXPORTACION_DETALLE_dir as '/FILER_WEBLOGIC_PRO/SIGA/ficheros/previsiones_sjcs';
update GEN_PARAMETROS set valor = 'FCS_EXPORTACION_DETALLE_dir' WHERE PARAMETRO = 'PATH_PREVISIONES'  AND MODULO = 'FCS';


ALTER TABLE USCGAE_DESA.JE_CERTIFICACION_CICAC MODIFY ASUNTOS_CANTIDAD_TOTAL NUMBER(5,0) NULL;
update JE_CERTIFICACION_CICAC set ASUNTOS_CANTIDAD_TOTAL = ASUNTOS_CANTIDAD;
ALTER TABLE USCGAE_DESA.JE_CERTIFICACION_CICAC MODIFY ASUNTOS_CANTIDAD_TOTAL NUMBER(5,0) NOT NULL;
--Ejecutado en int
ALTER TABLE USCGAE_DESA.SCS_PERSONAJG ADD DIRECCIONEXTRANJERA VARCHAR2(255) NULL;
UPDATE SCS_PERSONAJG SET IDPAISDIR1 = 191 ;

  
insert into gen_recursos  (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.info.direccionExtranjera', 'Escriba la dirección postal completa, incluido el pais', 0, 1, sysdate, 0, 19);
insert into gen_recursos  (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.info.direccionExtranjera', 'Escriviu l''adreça postal completa, inclòs el país#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursoS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.info.direccionExtranjera', 'Escriba la dirección postal completa, incluido el pais#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursoS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.info.direccionExtranjera', 'Escriba la dirección postal completa, incluido el pais#GL', 0, 4, sysdate, 0, 19);

insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','0',sysdate,'0','0');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2003');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2006');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2035');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2036');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2021');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2008');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)  values('SCS','FILTRAR_JUZGADO_MODULO_ESPECIAL','1',sysdate,'0','2043');




