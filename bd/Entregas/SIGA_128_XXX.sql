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

--Ejecutado en integración por FMS el 27/07/2019