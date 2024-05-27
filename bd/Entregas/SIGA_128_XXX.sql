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
ALTER TABLE USCGAE_DESA.JE_CERTIFICACION_CICAC MODIFY ASUNTOS_CANTIDAD_TOTAL NUMBER(6,0) NOT NULL;
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



ALTER TABLE USCGAE_DESA.ADM_TIPOINFORME ADD PROGRAMACION VARCHAR2(5) NULL;
update ADM_TIPOINFORME set programacion = 15;
update ADM_TIPOINFORME set programacion = 16:00 WHERE idtipoinforme = 'CPAGO';
update ADM_TIPOINFORME set programacion = 16:00 WHERE idtipoinforme = 'CFACT';


INSERT INTO ENV_ESTADOENVIO (NOMBRE,IDESTADO,FECHAMODIFICACION,USUMODIFICACION) VALUES	 ('46000',0,SYSDATE,0);
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS,IDLENGUAJE) VALUES
('46000','PROGRAMADO',SYSDATE,0,NULL,'ENV_ESTADOENVIO','NOMBRE','env_estadoenvio.nombre.0.0',1);
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS,IDLENGUAJE) VALUES
 ('46000','PROGRAMAT',SYSDATE,0,NULL,'ENV_ESTADOENVIO','NOMBRE','env_estadoenvio.nombre.0.0',2);
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS,IDLENGUAJE) VALUES
 ('46000','PROGRAMADO#EU',SYSDATE,0,NULL,'ENV_ESTADOENVIO','NOMBRE','env_estadoenvio.nombre.0.0',3);
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS,IDLENGUAJE) VALUES
 ('46000','PROGRAMADO#GL',SYSDATE,0,NULL,'ENV_ESTADOENVIO','NOMBRE','env_estadoenvio.nombre.0.0',4);
 
 
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('env.parametro.horacomunicacioneseditable', 'Parámetro que permite modificar la hora de los envios', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('env.parametro.horacomunicacioneseditable', 'Parámetro que permite modificar la hora de los envios', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('env.parametro.horacomunicacioneseditable', 'Parámetro que permite modificar la hora de los envios#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('env.parametro.horacomunicacioneseditable', 'Parámetro que permite modificar la hora de los envios#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('ENV', 'HORA_COMUNICACIONES_EDITABLE', '1', SYSDATE, 1, 0, 'env.parametro.horacomunicacioneseditable', null);

CREATE INDEX ECOM_INTERCAMBIO_IDECOMCOLA_IDX ON USCGAE_DESA.ECOM_INTERCAMBIO (IDECOMCOLA);

ALTER TABLE FAC_FACTURACIONPROGRAMADA ADD FECHAPREVISTAPDFYENVIO DATE NULL;
UPDATE FAC_FACTURACIONPROGRAMADA SET FECHAPREVISTAPDFYENVIO = FECHACONFIRMACION WHERE IDESTADOENVIO= 15;

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fac.parametro.horapdfyenvio', 'Parámetro que dice la hora a la que generar los pdf y envios de la facturacion', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fac.parametro.horapdfyenvio', 'Parámetro que dice la hora a la que generar los pdf y envios de la facturacion#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fac.parametro.horapdfyenvio', 'Parámetro que dice la hora a la que generar los pdf y envios de la facturacion#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fac.parametro.horapdfyenvio', 'Parámetro que dice la hora a la que generar los pdf y envios de la facturacion#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO, FECHA_BAJA)
values ('FAC', 'HORA_PDF_Y_ENVIO', '16:00', SYSDATE, 1, 0, 'fac.parametro.horapdfyenvio', null);

UPDATE FAC_FACTURACIONPROGRAMADA SET IDESTADOCONFIRMACION = 3,IDESTADOPDF =5,IDESTADOENVIO =11
--SELECT * FROM FAC_FACTURACIONPROGRAMADA
WHERE IDESTADOCONFIRMACION = 17 AND FECHACONFIRMACION IS NOT NULL;

ALTER TABLE USCGAE_INT.ECOM_INTERCAMBIO ADD EJG_ANIO NUMBER(4,0) NULL;
ALTER TABLE USCGAE_INT.ECOM_INTERCAMBIO ADD EJG_IDTIPO NUMBER(3,0) NULL;
ALTER TABLE USCGAE_INT.ECOM_INTERCAMBIO ADD EJG_NUMERO NUMBER(10,0) NULL;
ALTER TABLE USCGAE_INT.ECOM_INTERCAMBIO ADD CONSTRAINT ECOM_INTERCAMBIO_SCS_EJG_FK FOREIGN KEY (IDINSTITUCION,EJG_IDTIPO,EJG_ANIO,EJG_NUMERO) REFERENCES USCGAE_INT.SCS_EJG(IDINSTITUCION,IDTIPOEJG,ANIO,NUMERO);

UPDATE ecom_intercambio ei SET
ei.EJG_IDTIPO  = (SELECT max(cp.valor) FROM ecom_cola_parametros cp WHERE cp.idecomcola = ei.idecomcola AND cp.clave ='IDTIPOEJG'),
ei.EJG_ANIO  = (SELECT max(cp.valor) FROM ecom_cola_parametros cp WHERE cp.idecomcola = ei.idecomcola AND cp.clave = 'ANIO'),
ei.EJG_NUMERO  = (SELECT max(cp.valor) FROM ecom_cola_parametros cp WHERE cp.idecomcola = ei.idecomcola AND cp.clave = 'NUMERO');



------- optimizacion de consultas
--Modificar esta consulta: 
SELECT
  FJG.NOMBRE || ' ' || TO_CHAR(FJG.FECHADESDE, 'dd/mm/yyyy') || '-' || 
    TO_CHAR(FJG.FECHAHASTA, 'dd/mm/yyyy') NOMBREFACTURACION,
  CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.IDCALENDARIOGUARDIAS,
  CABECERAGUARDIAS.FECHAINICIO,
  CABECERAGUARDIAS.FECHA_FIN,
  TURNO.NOMBRE AS TURNO,
  GUARDIA.NOMBRE AS GUARDIA,
  GUARDIA.SELECCIONLABORABLES AS SELECCIONLABORABLES,
  GUARDIA.SELECCIONFESTIVOS AS SELECCIONFESTIVOS,
  F_SIGA_NUMEROPERMUTAGUARDIAS (CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO ) AS ESTADO ,
  PKG_SIGA_ACCIONES_GUARDIAS.FUNC_ACCIONES_GUARDIAS( CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO) AS FUNCIONPERMUTAS ,
  CABECERAGUARDIAS.FACTURADO ,
  CABECERAGUARDIAS.VALIDADO,
  F_SIGA_TIENE_ACTS_VALIDADAS(CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDCALENDARIOGUARDIAS,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO) AS ACT_VALIDADAS
FROM
  SCS_CABECERAGUARDIAS CABECERAGUARDIAS,
  SCS_GUARDIASTURNO GUARDIA,
  SCS_TURNO TURNO,
  FCS_FACTURACIONJG FJG
WHERE
  GUARDIA.IDINSTITUCION = CABECERAGUARDIAS.IDINSTITUCION
  AND GUARDIA.IDTURNO = CABECERAGUARDIAS.IDTURNO
  AND GUARDIA.IDGUARDIA = CABECERAGUARDIAS.IDGUARDIA
  AND GUARDIA.IDINSTITUCION = TURNO.IDINSTITUCION
  AND GUARDIA.IDTURNO = TURNO.IDTURNO
  AND CABECERAGUARDIAS.IDINSTITUCION = FJG.IDINSTITUCION(+)
  AND CABECERAGUARDIAS.IDFACTURACION = FJG.IDFACTURACION(+)
  AND CABECERAGUARDIAS.IDINSTITUCION = 2011
  AND CABECERAGUARDIAS.IDPERSONA = 2011005711
ORDER BY
  CABECERAGUARDIAS.FECHAINICIO DESC,
  TURNO,
  GUARDIA;
--....por esta:
SELECT
  (select FJG.NOMBRE || ' ' || TO_CHAR(FJG.FECHADESDE, 'dd/mm/yyyy') || '-' || 
    TO_CHAR(FJG.FECHAHASTA, 'dd/mm/yyyy')
  from FCS_FACTURACIONJG FJG
  where CABECERAGUARDIAS.IDINSTITUCION = FJG.IDINSTITUCION  AND CABECERAGUARDIAS.IDFACTURACION = FJG.IDFACTURACION) NOMBREFACTURACION,
  CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.IDCALENDARIOGUARDIAS,
  CABECERAGUARDIAS.FECHAINICIO,
  CABECERAGUARDIAS.FECHA_FIN,
  TURNO.NOMBRE AS TURNO,
  GUARDIA.NOMBRE AS GUARDIA,
  GUARDIA.SELECCIONLABORABLES AS SELECCIONLABORABLES,
  GUARDIA.SELECCIONFESTIVOS AS SELECCIONFESTIVOS,
  F_SIGA_NUMEROPERMUTAGUARDIAS (CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO ) AS ESTADO ,
  PKG_SIGA_ACCIONES_GUARDIAS.FUNC_ACCIONES_GUARDIAS( CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO) AS FUNCIONPERMUTAS ,
  CABECERAGUARDIAS.FACTURADO ,
  CABECERAGUARDIAS.VALIDADO,
  F_SIGA_TIENE_ACTS_VALIDADAS(CABECERAGUARDIAS.IDINSTITUCION,
  CABECERAGUARDIAS.IDTURNO,
  CABECERAGUARDIAS.IDGUARDIA,
  CABECERAGUARDIAS.IDCALENDARIOGUARDIAS,
  CABECERAGUARDIAS.IDPERSONA,
  CABECERAGUARDIAS.FECHAINICIO) AS ACT_VALIDADAS
FROM
  SCS_CABECERAGUARDIAS CABECERAGUARDIAS,
  SCS_GUARDIASTURNO GUARDIA,
  SCS_TURNO TURNO
WHERE
  GUARDIA.IDINSTITUCION = CABECERAGUARDIAS.IDINSTITUCION
  AND GUARDIA.IDTURNO = CABECERAGUARDIAS.IDTURNO
  AND GUARDIA.IDGUARDIA = CABECERAGUARDIAS.IDGUARDIA
  AND CABECERAGUARDIAS.IDINSTITUCION = TURNO.IDINSTITUCION
  AND CABECERAGUARDIAS.IDTURNO = TURNO.IDTURNO
  AND CABECERAGUARDIAS.IDINSTITUCION = 2011
  AND CABECERAGUARDIAS.IDPERSONA = 2011005711
ORDER BY
  CABECERAGUARDIAS.FECHAINICIO DESC,
  TURNO,
  GUARDIA

CREATE INDEX SCS_ASISTENCIA_FECHAHORA_IDPERSONA_IDGUARDIA_IDX ON USCGAE.SCS_ASISTENCIA (IDINSTITUCION,IDTURNO,IDGUARDIA,IDPERSONACOLEGIADO,FECHAHORA);
BEGIN
  DBMS_STATS.GATHER_TABLE_STATS(
    ownname=>'USCGAE' ,
    tabname=>'SCS_ASISTENCIA' ,
    cascade=>TRUE) ;
END;

DROP   INDEX SI_PERMUTAGUARDIA_CONFIRMADOR;
CREATE INDEX SI_SCS_PERMUTAGUARDIAS_CONFIRMADOR ON SCS_PERMUTAGUARDIAS
(IDINSTITUCION, IDTURNO_CONFIRMADOR, IDGUARDIA_CONFIRMADOR, IDPERSONA_CONFIRMADOR, 
FECHAINICIO_CONFIRMADOR)
NOLOGGING
TABLESPACE TS_SIGA_SCS_IDX
NOPARALLEL
;

DROP INDEX SI_SCS_PERMUTAGUARDIAS_SOLICIT;
CREATE INDEX SI_SCS_PERMUTAGUARDIAS_SOLICITANTE ON SCS_PERMUTAGUARDIAS
(IDINSTITUCION, IDTURNO_SOLICITANTE, IDGUARDIA_SOLICITANTE, IDPERSONA_SOLICITANTE, 
FECHAINICIO_SOLICITANTE)
NOLOGGING
TABLESPACE TS_SIGA_SCS_IDX
NOPARALLEL
;

BEGIN
  DBMS_STATS.GATHER_TABLE_STATS(
    ownname=>'USCGAE' ,
    tabname=>'SCS_PERMUTAGUARDIAS' ,
    cascade=>TRUE) ;
END
;

CREATE OR REPLACE PACKAGE BODY USCGAE.PKG_SIGA_ACCIONES_GUARDIAS IS

    /****************************************************************************************************
    Nombre: PROC_ACCIONES_GUARDIAS
    Descripcion: Procedimiento que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos - Valores)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - P_SUSTITUIR - OUT - Indica si es sustituible la guardia - VARCHAR2 - 'N': no sustituible; 'S': sustituible
    - P_ANULAR - OUT - Indica si es anulable la guardia - VARCHAR2 - 'N': no anulable; 'S': anulable
    - P_BORRAR - OUT - Indica si es borrable la guardia - VARCHAR2 - 'N': no borrable; 'S': borrable
    - P_PERMUTAR - OUT - Indica si es permutable la guardia - VARCHAR2 - 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    - P_ASISTENCIA - OUT - Indica si tiene asistencia asociada - VARCHAR2 - 'N': sin Asistencia; 'S': con asistencia
    - P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10)
        En caso de error devuelve el codigo de error Oracle correspondiente.
    - P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400)
        En caso de error devuelve el mensaje de error Oracle correspondiente.

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/    
    PROCEDURE PROC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE,
        P_SUSTITUIR OUT VARCHAR2, -- 'N': no sustituible; 'S': sustituible
        P_ANULAR OUT VARCHAR2, -- 'N': no anulable; 'S': anulable
        P_BORRAR OUT VARCHAR2, -- 'N': no borrable; 'S': borrable
        P_PERMUTAR OUT VARCHAR2, -- 'N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
        P_ASISTENCIA OUT VARCHAR2, -- 'N': sin Asistencia; 'S': con asistencia
        P_CODRETORNO OUT VARCHAR2,
        P_DATOSERROR OUT VARCHAR2) IS      
        
        V_FACTURADO NUMBER; -- 0:NoFacturado; 1:Facturado
        V_EXISTE_GUARDIA NUMBER; -- 0:NoExisteGuardia; 1:ExisteGuardia
        V_PERMUTACION NUMBER; -- 0:PermutaPendienteSolicitante; 1:Permutada; 2:PermutaPendienteConfirmador 
        V_FECHAFIN SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE;
        
        E_NOEXISTE_O_FACTURADA EXCEPTION;
          
    BEGIN
        P_SUSTITUIR := 'N'; -- No sustituible
        P_ANULAR := 'N'; -- No anulable
        P_BORRAR := 'N'; -- No borrable
        P_PERMUTAR := 'N'; -- No permutable
        P_ASISTENCIA := 'N'; -- Sin Asistencia
        
        -- Paso 1. Busca si esta facturado
        V_EXISTE_GUARDIA := 1;
        BEGIN             
            SELECT nvl(FACTURADO, 0), FECHA_FIN INTO V_FACTURADO, V_FECHAFIN
            FROM SCS_CABECERAGUARDIAS
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTURNO       = P_IDTURNO
                AND IDGUARDIA     = P_IDGUARDIA
                AND IDPERSONA     = P_IDPERSONA
                AND FECHAINICIO   = P_FECHA;
        
            EXCEPTION WHEN NO_DATA_FOUND THEN
                V_EXISTE_GUARDIA := 0; -- No esta facturado
        END;
                
        IF (V_EXISTE_GUARDIA = 0 or V_FACTURADO = 1) THEN -- 0:NoExisteGuardia; 1:ExisteGuardia
            -- Compruebo que existe la guardia y que no esta facturada
            raise E_NOEXISTE_O_FACTURADA;
        END IF;
        
        -- Si NO esta facturado, entonces se podra sustituir y anular
        P_SUSTITUIR := 'S';
        P_ANULAR := 'S';
        
        -- Paso 2. Busco si tiene asistencias asociadas
        BEGIN
            SELECT 'S' INTO P_ASISTENCIA
            FROM SCS_ASISTENCIA
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTURNO = P_IDTURNO
                AND IDGUARDIA = P_IDGUARDIA
                AND IDPERSONACOLEGIADO = P_IDPERSONA
                AND TRUNC(FECHAHORA) BETWEEN P_FECHA AND V_FECHAFIN
                AND ROWNUM = 1; -- Con una ya tiene asistencias
                
            EXCEPTION WHEN NO_DATA_FOUND THEN
                P_ASISTENCIA := 'N'; -- No tiene asistencias asociadas
        END;

        -- Paso 3. Compruebo que el dia de la guardia es posterior al dia actual 
        IF TRUNC(SYSDATE) < TRUNC(P_FECHA) THEN
        
            /* Se puede borrar si se cumple lo siguiente:
                - NO esta facturado
                - El dia de guardia es posterior al actual
                - Sin asistencias asociadas*/
            IF (P_ASISTENCIA = 'N') THEN -- 'N': sin Asistencia; 'S': con asistencia
                P_BORRAR := 'S';
            END IF;
            
            -- Paso 5. Compruebo si es permutable 
            BEGIN
                SELECT PERMUTACION INTO V_PERMUTACION -- 0:PermutaPendienteSolicitante; 1:Permutada; 2:PermutaPendienteConfirmador 
                FROM (             
                    SELECT FECHAMODIFICACION, NVL2(FECHACONFIRMACION, 1, DECODE(IDPERSONA_CONFIRMADOR, P_IDPERSONA, 2, 0)) AS PERMUTACION
                    FROM SCS_PERMUTAGUARDIAS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTURNO_CONFIRMADOR = P_IDTURNO 
                        AND IDGUARDIA_CONFIRMADOR = P_IDGUARDIA
                        AND IDPERSONA_CONFIRMADOR = P_IDPERSONA
                        AND FECHAINICIO_CONFIRMADOR = P_FECHA
                    UNION ALL
                    SELECT FECHAMODIFICACION, NVL2(FECHACONFIRMACION, 1, DECODE(IDPERSONA_CONFIRMADOR, P_IDPERSONA, 2, 0)) AS PERMUTACION
                    FROM SCS_PERMUTAGUARDIAS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTURNO_SOLICITANTE = P_IDTURNO 
                        AND IDGUARDIA_SOLICITANTE = P_IDGUARDIA
                        
                        AND IDPERSONA_SOLICITANTE = P_IDPERSONA
                        AND FECHAINICIO_SOLICITANTE = P_FECHA
                    ORDER BY FECHAMODIFICACION DESC                        
                ) TABLA_PERMUTAGUARDIAS
                WHERE ROWNUM = 1;                        
          
                EXCEPTION WHEN NO_DATA_FOUND THEN
                    V_PERMUTACION := 1; -- No tiene permutas
            END;
            
            -- Compruebo que NO tiene permutas pendientes
            IF (V_PERMUTACION = 1) THEN
            
                /* Se puede permutar si se cumple lo siguiente:
                    - NO esta facturado
                    - El dia de guardia es posterior al actual
                    - Permutada o sin permutar*/
                P_PERMUTAR := 'S';
                   
            ELSIF (V_PERMUTACION = 2) THEN -- Pendiente del confirmador
                P_PERMUTAR := 'P';
            END IF; -- Sin permutas Pendientes
        END IF; -- Fecha posterior a la actual

        P_DATOSERROR := 'PROC_ACCIONES_GUARDIAS: Finalizado correctamente';
        P_CODRETORNO := '0';

    EXCEPTION
        WHEN E_NOEXISTE_O_FACTURADA THEN
            P_DATOSERROR := 'PROC_ACCIONES_GUARDIAS: Finalizado correctamente';
            P_CODRETORNO := '0';
        WHEN OTHERS THEN
            P_CODRETORNO := TO_CHAR(SQLCODE);
            P_DATOSERROR := P_DATOSERROR || ' ' || SQLERRM;        
    END PROC_ACCIONES_GUARDIAS;
    
    /****************************************************************************************************
    Nombre: FUNC_ACCIONES_GUARDIAS
    Descripcion: Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
    1. Si NO esta facturado, entonces se podra sustituir y anular
    2. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene asistencias, entonces se podra borrar
    3. Si NO esta facturado, el dia de guardia es posterior al actual, existe la guardia y no tiene una permuta pendiente, entonces se podra permutar

    Parametros (IN/OUT - Descripcion -Tipo de Datos)
    - P_IDINSTITUCION - IN - Identificador de la institucion - NUMBER
    - P_IDTURNO - IN - Identificador del turno - NUMBER
    - P_IDGUARDIA - IN - Identificador de la guardia - NUMBER
    - P_IDPERSONA - IN - Identificador de la persona - NUMBER
    - P_FECHA - IN - Fecha de la guardia - DATE
    - RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
    -- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
    -- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
    -- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
    -- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
    -- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia        

    Versiones (Fecha - Autor - Datos):
    - 1.0 - 01/03/2017 - Jorge Paez Trivino - Version inicial
  ****************************************************************************************************/        
    FUNCTION FUNC_ACCIONES_GUARDIAS(
        P_IDINSTITUCION IN NUMBER,
        P_IDTURNO IN NUMBER,
        P_IDGUARDIA IN NUMBER,
        P_IDPERSONA IN NUMBER,
        P_FECHA IN DATE) RETURN VARCHAR2 IS
        
        V_SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
        V_ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
        V_BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
        V_PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
        V_ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia
        V_CODRETORNO VARCHAR2(10);
        V_DATOSERROR VARCHAR2(400);
        
    BEGIN   
    
        PROC_ACCIONES_GUARDIAS(
            P_IDINSTITUCION,
            P_IDTURNO,
            P_IDGUARDIA,
            P_IDPERSONA,
            P_FECHA,
            V_SUSTITUIR, -- 'N': no sustituible; 'S': sustituible
            V_ANULAR, -- 'N': no anulable; 'S': anulable
            V_BORRAR, -- 'N': no borrable; 'S': borrable
            V_PERMUTAR, -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
            V_ASISTENCIA, -- 'N': sin Asistencia; 'S': con asistencia
            V_CODRETORNO,
            V_DATOSERROR);
            
            -- Si hay error indico que no puedo hacer nada
            IF (V_CODRETORNO <> '0') THEN
                V_SUSTITUIR := 'N';
                V_ANULAR := 'N';
                V_BORRAR := 'N';
                V_PERMUTAR := 'N';
                V_ASISTENCIA := 'N';                
            END IF;      
            
        RETURN V_SUSTITUIR || V_ANULAR || V_BORRAR || V_PERMUTAR || V_ASISTENCIA;
    END FUNC_ACCIONES_GUARDIAS;

END PKG_SIGA_ACCIONES_GUARDIAS;

CREATE OR REPLACE FUNCTION USCGAE.F_SIGA_NUMEROPERMUTAGUARDIAS(
       P_IDINSTITUCION 		IN SCS_PERMUTAGUARDIAS.IDINSTITUCION%TYPE,
       P_IDTURNO          IN SCS_PERMUTAGUARDIAS.IDTURNO_SOLICITANTE%TYPE,
       P_IDGUARDIA 	      IN SCS_PERMUTAGUARDIAS.IDGUARDIA_SOLICITANTE%TYPE,
       P_IDPERSONA        IN SCS_PERMUTAGUARDIAS.IDPERSONA_SOLICITANTE%TYPE,
       P_FECHAINICIO      IN SCS_PERMUTAGUARDIAS.FECHAINICIO_SOLICITANTE%TYPE)
RETURN NUMBER IS

/****************************************************************************************************************/
/* Nombre:        F_SIGA_NUMEROPERMUTAGUARDIAS                                                                 */
/* Descripcion:   Funcion que obtiene el numero de permutas guardias 				                                    */
/*  				                                                                         		                        */
/* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
/* -------------------   ------   ------------------------------------------------------------   -------------  */
/* P_IDINSTITUCION       IN  	    Identificador de la Institucion                                NUMBER         */
/* P_IDTURNO	           IN	      Identificador del Turno		                                     NUMBER         */
/* P_IDGUARDIA           IN 	    Identificador de la Guardia	                                   NUMBER         */
/* P_IDPERSONA           IN 	    Identificador de la Persona	                                   NUMBER         */
/* P_FECHAINICIO         IN 	    Fecha de Inicio	                                               DATE           */
/*          												                                                                            */
/* Version:        1.1												                                                                  */
/* Fecha Creacion: 14/02/2005                                                                                   */
/* Autor:		       Yolanda Garcia Espino                                                                        */
/* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
/* ------------------   ---------------------------------   --------------------------------------------------- */
/****************************************************************************************************************/

  /* Declaracion de variables */
  V_NUMERO NUMBER;
  V_FECHACONFIRMACION SCS_PERMUTAGUARDIAS.FECHACONFIRMACION%TYPE;
  V_FACTURADO   SCS_GUARDIASCOLEGIADO.FACTURADO%TYPE;
  
BEGIN
    IF P_FECHAINICIO < SYSDATE THEN
        /* Guardia realizada. Hay que comprobar si esta facturada o no */
        BEGIN
            SELECT FACTURADO INTO V_FACTURADO
            FROM SCS_CABECERAGUARDIAS
            WHERE IDINSTITUCION = P_IDINSTITUCION
                AND IDTURNO       = P_IDTURNO
                AND IDGUARDIA     = P_IDGUARDIA
                AND IDPERSONA     = P_IDPERSONA
                AND FECHAINICIO   = P_FECHAINICIO
                AND ROWNUM = 1;             
            
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                V_NUMERO := 5; /* Pendiente de realizar */
        END;
     
        IF V_FACTURADO = 1 THEN
            V_NUMERO := 6;  /* Guardia realizada y FACTURADA */
        ELSE
            V_NUMERO := 1; /* Guardia realizada y NO facturada */
        END IF;
     
    ELSE
        BEGIN
            SELECT NUMERO INTO V_NUMERO
            FROM (             
                SELECT FECHAMODIFICACION, NVL2(FECHACONFIRMACION, 3, 4) AS NUMERO
                FROM SCS_PERMUTAGUARDIAS
                WHERE IDINSTITUCION = P_IDINSTITUCION
                    AND IDTURNO_CONFIRMADOR = P_IDTURNO 
                    AND IDGUARDIA_CONFIRMADOR = P_IDGUARDIA
                    AND IDPERSONA_CONFIRMADOR = P_IDPERSONA
                    AND FECHAINICIO_CONFIRMADOR = P_FECHAINICIO
                UNION ALL
                    SELECT FECHAMODIFICACION, NVL2(FECHACONFIRMACION, 3, 2) AS NUMERO
                    FROM SCS_PERMUTAGUARDIAS
                    WHERE IDINSTITUCION = P_IDINSTITUCION
                        AND IDTURNO_SOLICITANTE = P_IDTURNO 
                        AND IDGUARDIA_SOLICITANTE = P_IDGUARDIA
                        AND IDPERSONA_SOLICITANTE = P_IDPERSONA
                        AND FECHAINICIO_SOLICITANTE = P_FECHAINICIO
                ORDER BY FECHAMODIFICACION DESC                        
            ) TABLA_PERMUTAGUARDIAS
            WHERE ROWNUM = 1;                        
      
        EXCEPTION WHEN NO_DATA_FOUND THEN
                V_NUMERO := 5; /* Pendiente de realizar */
        END;
    END IF;

  RETURN V_NUMERO;
end F_SIGA_NUMEROPERMUTAGUARDIAS;
 
CREATE OR REPLACE function USCGAE.F_SIGA_TIENE_ACTS_VALIDADAS(P_IDINSTITUCION SCS_CABECERAGUARDIAS.IDINSTITUCION%TYPE,
                                                       P_IDTURNO  SCS_CABECERAGUARDIAS.IDTURNO%TYPE, 
                                                       P_IDGUARDIA  SCS_CABECERAGUARDIAS.IDGUARDIA%TYPE, 
                                                       P_IDCALENDARIOGUARDIAS  SCS_CABECERAGUARDIAS.IDCALENDARIOGUARDIAS%TYPE, 
                                                       P_IDPERSONA  SCS_CABECERAGUARDIAS.IDPERSONA%TYPE, 
                                                       P_FECHAINICIO  SCS_CABECERAGUARDIAS.FECHAINICIO%TYPE
                                                       ) return NUMBER is

  -- VARIABLES
  V_NUMERO    NUMBER;
  V_FECHAFIN  SCS_CABECERAGUARDIAS.FECHA_FIN%TYPE;

BEGIN
  SELECT
    FECHA_FIN INTO V_FECHAFIN
  FROM
    SCS_CABECERAGUARDIAS CG
  WHERE
    CG.IDINSTITUCION = P_IDINSTITUCION
    AND CG.IDTURNO = P_IDTURNO
    AND CG.IDGUARDIA = P_IDGUARDIA
    AND CG.IDPERSONA = P_IDPERSONA
    AND CG.FECHAINICIO = P_FECHAINICIO;
  
  SELECT
    COUNT (1) INTO V_NUMERO
  FROM
    SCS_ASISTENCIA ASI,
    SCS_ACTUACIONASISTENCIA ACT
  WHERE 1 = 1
    AND ASI.IDINSTITUCION = P_IDINSTITUCION
    AND ASI.IDTURNO = P_IDTURNO
    AND ASI.IDGUARDIA = P_IDGUARDIA
    AND ASI.IDPERSONACOLEGIADO = P_IDPERSONA
    AND TRUNC(ASI.FECHAHORA) BETWEEN P_FECHAINICIO AND V_FECHAFIN
    AND ASI.IDINSTITUCION = ACT.IDINSTITUCION
    AND ASI.ANIO = ACT.ANIO
    AND ASI.NUMERO = ACT.NUMERO
    -- filtro
    AND ACT.VALIDADA='1';
 
   RETURN V_NUMERO;
EXCEPTION
  WHEN OTHERS THEN
    V_NUMERO := 1;
    RETURN V_NUMERO;

end F_SIGA_TIENE_ACTS_VALIDADAS;
 

INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.insertarEstado.mensaje.envioPericles', 'A continuación, serán enviados el expediente, la documentación y los informes económicos solicitados a la CAJG. ¿Desea continuar?', 
0, 1, sysdate, 0, 15);
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.insertarEstado.mensaje.envioPericles', 'A continuación, serán enviados el expediente, la documentación y los informes económicos solicitados a la CAJG. ¿Desea continuar?#CA', 
0, 2, sysdate, 0, 15);
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.insertarEstado.mensaje.envioPericles', 'A continuación, serán enviados el expediente, la documentación y los informes económicos solicitados a la CAJG. ¿Desea continuar?#EU', 
0, 3, sysdate, 0, 15);
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD)
VALUES('gratuita.insertarEstado.mensaje.envioPericles', 'A continuación, serán enviados el expediente, la documentación y los informes económicos solicitados a la CAJG. ¿Desea continuar?#GL', 
0, 4, sysdate, 0, 15);

--https://redabogacia.atlassian.net/browse/SIGA-844
DROP INDEX IND_FK_SCS_ACTUACIONASISTENCIA;
create unique index SCS_DOCUMENTACIONASI_SI on SCS_DOCUMENTACIONASI(IDINSTITUCION, IDDOCUMENTACIONASI) TABLESPACE TS_SIGA_SCS_IDX;
BEGIN
  DBMS_STATS.GATHER_TABLE_STATS(
    ownname=>'USCGAE' ,
    tabname=>'SCS_DOCUMENTACIONASI' ,
    cascade=>TRUE) ;
END;

--https://redabogacia.atlassian.net/browse/SIGA-845
create index SI_SCS_DESIGNA_UPPER_NUMPROC on SCS_DESIGNA(upper(NUMPROCEDIMIENTO), IDINSTITUCION_JUZG, IDJUZGADO) TABLESPACE TS_SIGA_SCS_IDX;
BEGIN
  DBMS_STATS.GATHER_TABLE_STATS(
    ownname=>'USCGAE' ,
    tabname=>'SCS_DESIGNA' ,
    cascade=>TRUE) ;
END;

--









--https://redabogacia.atlassian.net/browse/SIGA-893
CREATE OR REPLACE TRIGGER USCGAE_INT.ENV_ENVIOS_AIU_IDESTADO
after insert or update OF IDESTADO on env_envios
for each row
when (new.ENVIO is null and new.idestado is not null and (old.idestado is null or (old.idestado is not null and old.idestado <> new.idestado)))
/*ENVIO is null : solo para Envios creados desde Classique*/
begin
  insert into env_historicoestadoenvio (
    idinstitucion,
    idenvio,
    idestado,
    fechaestado,
    fechamodificacion,
    usumodificacion,
    idhistorico
  ) values (
    :new.idinstitucion,
    :new.idenvio,
    :new.idestado,
    sysdate,
    sysdate,
    :new.usumodificacion,
    nvl((select max(idhistorico) from env_historicoestadoenvio where
      idinstitucion = :new.idinstitucion and idenvio = :new.idenvio), 0)+1
  );
end ENV_ENVIOS_AIU_IDESTADO;

drop trigger ENV_ENVIOS_INS;
drop trigger ENV_ENVIOS_UPD;

update gen_recursos set descripcion='Módulo anulado o sin acreditaciones' where idRecurso='gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones' and idLenguaje=1;
update gen_recursos set descripcion='Módulo anulado o sin acreditaciones#CA' where idRecurso='gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones' and idLenguaje=2;
update gen_recursos set descripcion='Módulo anulado o sin acreditaciones#EU' where idRecurso='gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones' and idLenguaje=3;
update gen_recursos set descripcion='Módulo anulado o sin acreditaciones#GL' where idRecurso='gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones' and idLenguaje=4;


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.actualizacion.actuacion.validada', 'Permiso para impedir mdificacion de la documentacion de las actuaciones de las designas una vez se ha validado la actuacion', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.actualizacion.actuacion.validada', 'Permiso para impedir mdificacion de la documentacion de las actuaciones de las designas una vez se ha validado la actuacion#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.actualizacion.actuacion.validada', 'Permiso para impedir mdificacion de la documentacion de las actuaciones de las designas una vez se ha validado la actuacion#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.actualizacion.actuacion.validada', 'Permiso para impedir mdificacion de la documentacion de las actuaciones de las designas una vez se ha validado la actuacion#GL', 0, '4', sysdate, 0, '19');


insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
SELECT 'SCS', 'SCS_PERMISOS_ACTUACIONES_VALIDADAS', '1', sysdate, 0, I.IDINSTITUCION, 'scs.parametro.actualizacion.actuacion.validada'
FROM CEN_INSTITUCION I WHERE I.CEN_INST_IDINSTITUCION = 3004;