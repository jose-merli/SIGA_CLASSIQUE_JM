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
