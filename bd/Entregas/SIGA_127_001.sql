
-- Create table
create table FAC_TIPOSACCIONFACTURA
(
  IDTIPOACCION NUMBER(3) not null,
  NOMBRE                  VARCHAR2(100) not null,
  FECHAMODIFICACION            DATE not null,
  USUMODIFICACION              NUMBER(5) not null
)
tablespace TS_SIGA_FAC
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
  ); 
-- Create/Recreate primary, unique and foreign key constraints 
alter table FAC_TIPOSACCIONFACTURA
  add constraint PK_FAC_TIPOSACCIONFACTURA primary key (IDTIPOACCION)
  deferrable
  using index 
  tablespace TS_SIGA_FAC_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
  
  
  
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (1,'EMISIÓN FACTURA',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (2,'CONFIRMACIÓN FACTURA',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (3,'APLICAR ANTICIPO',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (4,'PAGOS POR CAJA',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (5,'PAGO POR BANCO',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (6,'DEVOLUCIÓN',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (7,'RENEGOCIACIÓN',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (8,'ANULACIÓN',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (9,'ANULACIÓN (Factura devuelta: anulada para integrar la comisión bancaria)',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (10,'COMPENSACIÓN',SYSDATE,0);


INSERT INTO GEN_CATALOGOS_MULTIIDIOMA (CODIGO, NOMBRETABLA,CAMPOTABLA,FECHAMODIFICACION,USUMODIFICACION,LOCAL,CODIGOTABLA,MIGRADO) 
VALUES (1344,'FAC_TIPOSACCIONFACTURA','NOMBRE',SYSDATE,0,'N','IDTIPOACCION','N');


 declare
      p_codretorno varchar2(4000);
      p_datoserror varchar2(4000);
    begin
      proc_act_recursos(p_codretorno, p_datoserror);
      dbms_output.put_line(p_codretorno || ': ' || p_datoserror);
    end;
    
    
    
  -- Create table
create table FAC_HISTORICOFACTURA
(
  IDINSTITUCION  NUMBER(4) not null,
  IDFACTURA      VARCHAR2(10) not null,
  IDHISTORICO    NUMBER(4) not null,
  FECHAMODIFICACION            DATE not null,
  USUMODIFICACION              NUMBER(5) not null,
  IDTIPOACCION NUMBER(3) not null,
  IDFORMAPAGO NUMBER(2) not null,
  IDPERSONA NUMBER(10) not null,
  IDCUENTA NUMBER(3) null,
  IDPERSONADEUDOR NUMBER(10) null,
  IDCUENTADEUDOR NUMBER(3) null,
  IMPTOTALANTICIPADO NUMBER(10,2) not null,
  IMPTOTALPAGADOPORCAJA NUMBER(10,2) not null,
  IMPTOTALPAGADOSOLOCAJA NUMBER(10,2) not null,
  IMPTOTALPAGADOSOLOTARJETA NUMBER(10,2) not null,
  IMPTOTALPAGADOPORBANCO NUMBER(10,2) not null,
  IMPTOTALPAGADO NUMBER(10,2) not null,
  IMPTOTALPORPAGAR NUMBER(10,2) not null,
  IMPTOTALCOMPENSADO  NUMBER(10,2) not null,
  ESTADO  NUMBER(1) not null
  
)
tablespace TS_SIGA_FAC
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
  ); 
-- Create/Recreate primary, unique and foreign key constraints 
alter table FAC_HISTORICOFACTURA
  add constraint PK_FAC_HISTORICOFACTURA primary key (IDINSTITUCION,IDFACTURA,IDHISTORICO)
  deferrable
  using index 
  tablespace TS_SIGA_FAC_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );    
 
    
  
  alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_IDINSTITUCION foreign key (IDINSTITUCION,IDFACTURA)
  references FAC_FACTURA (IDINSTITUCION,IDFACTURA)
  deferrable;

  alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_IDTIPOACCION foreign key (IDTIPOACCION)
  references FAC_TIPOSACCIONFACTURA (IDTIPOACCION)
  deferrable;
  
  alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_IDFORMAPAGO foreign key (IDFORMAPAGO)
  references PYS_FORMAPAGO (IDFORMAPAGO)
  deferrable;
  
  alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_IDPERSONA foreign key (IDINSTITUCION,IDPERSONA,IDCUENTA)
  references CEN_CUENTASBANCARIAS (IDINSTITUCION,IDPERSONA,IDCUENTA)
  deferrable;
  
alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_IDPERDEUDOR foreign key (IDINSTITUCION,IDPERSONADEUDOR,IDCUENTADEUDOR)
  references CEN_CUENTASBANCARIAS (IDINSTITUCION,IDPERSONA,IDCUENTA)
  deferrable;
  
 alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_HISFAC_ESTADO foreign key (ESTADO)
  references FAC_ESTADOFACTURA (IDESTADO)
  deferrable;
  
  
create sequence SEQ_FAC_HISTORIAL
minvalue 1
maxvalue 9999
increment by 1
nocache
cycle;
  



INSERT INTO Fac_Estadofactura (IDESTADO, DESCRIPCION,LENGUAJE) 
VALUES (9,'facturacion.pagosFactura.estado.pendienteCobro',1);

ALTER TABLE fac_historicofactura ADD IDPAGOPORCAJA number(3);
ALTER TABLE fac_historicofactura ADD IDDISQUETECARGOS number(10);
ALTER TABLE fac_historicofactura ADD IDFACTURAINCLUIDAENDISQUETE number(5);
ALTER TABLE fac_historicofactura ADD IDDISQUETEDEVOLUCIONES number(10);
ALTER TABLE fac_historicofactura ADD IDRECIBO VARCHAR2(12);
ALTER TABLE fac_historicofactura ADD IDRENEGOCIACION number(3);
ALTER TABLE fac_historicofactura ADD IDABONO number(10);
ALTER TABLE fac_historicofactura ADD COMISIONIDFACTURA VARCHAR2(10);


alter table FAC_HISTORICOFACTURA
  add constraint FK_FAC_PAGOSPORCAJA foreign key (IDINSTITUCION, IDFACTURA, IDPAGOPORCAJA)
  references FAC_PAGOSPORCAJA (IDINSTITUCION, IDFACTURA, IDPAGOPORCAJA)
  deferrable;
  
  
  alter table fac_historicofactura
  add constraint FK_FACTURAINCLUIDAENDISQUETE foreign key (IDINSTITUCION, IDDISQUETECARGOS, IDFACTURAINCLUIDAENDISQUETE)
  references FAC_FACTURAINCLUIDAENDISQUETE (IDINSTITUCION, IDDISQUETECARGOS, IDFACTURAINCLUIDAENDISQUETE)
  deferrable;
  
  alter table fac_historicofactura
  add constraint FK_FAC_LINEADEVOLUDISQBANCO foreign key (IDINSTITUCION, IDDISQUETEDEVOLUCIONES, IDRECIBO)
  references FAC_LINEADEVOLUDISQBANCO (IDINSTITUCION, IDDISQUETEDEVOLUCIONES, IDRECIBO)
  deferrable;
  
  
  alter table fac_historicofactura
  add constraint FK_FAC_RENEGOCIACION foreign key (IDINSTITUCION, IDFACTURA, IDRENEGOCIACION)
  references FAC_RENEGOCIACION (IDINSTITUCION, IDFACTURA, IDRENEGOCIACION)
  deferrable;
  
  
  alter table fac_historicofactura
  add constraint FK_FAC_ABONO foreign key (IDINSTITUCION, IDABONO)
  references FAC_ABONO (IDINSTITUCION, IDABONO)
  deferrable;
  
  alter table fac_historicofactura
  add constraint FK_HIST_FAC_FACTURA1 foreign key (IDINSTITUCION,COMISIONIDFACTURA)
  references FAC_FACTURA (IDINSTITUCION, IDFACTURA)
  deferrable;
  
  alter table fac_historicofactura
  add constraint FK_HIST_FAC_FACTURA2 foreign key (IDINSTITUCION,IDFACTURA)
  references FAC_FACTURA (IDINSTITUCION, IDFACTURA)
  deferrable;
  
  
  
  PKG_SIGA_CARGOS
  PKG_SIGA_FACTURACION
  TRIGGER FAC_HISTORICOFACTURA_BF