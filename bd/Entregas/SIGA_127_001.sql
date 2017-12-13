
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
  
  
 ---------------------------TRIGGER---------------------------
 
 CREATE OR REPLACE TRIGGER FAC_HISTORICOFACTURA_BF
  BEFORE Insert ON USCGAE_DESA.FAC_HISTORICOFACTURA
  REFERENCING NEW AS NEW
  For Each Row
declare
  --Particularizamos los mensajes de error
  E_ERROR EXCEPTION;
  v_idTipoAccion Number; -- Registro para los ficheros de salida
  v_idEstado     Number;
  v_resultados   BOOLEAN := false;
Begin
  --Revisa si se han introducido correctamente los datos en la tabla
  v_idTipoAccion := :new.idtipoaccion;
  v_idEstado     := :new.estado;

  IF (v_idTipoAccion = 1) THEN
    --Emisión      
    if (:new.estado != 7) then
      RAISE E_ERROR;
    end if;
  ELSIF (v_idTipoAccion = 2) THEN
    --Confirmación
    if (:New.estado != 9 and :New.estado != 1 and :New.estado != 5 and
       :New.estado != 2) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 3 THEN
    --Anticipo
    if (:new.imptotalanticipado <= 0) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 4 THEN
    --Pago por caja
    if (:new.idformapago != 30 or :new.idcuenta is not null or
       :new.idcuentadeudor is not null or :new.imptotalpagadoporcaja <= 0 or
       :new.imptotalpagadosolocaja <= 0 or :new.imptotalpagado <= 0 or
       (:new.estado != 1 and :new.estado != 7 and :new.estado != 2) or
       :new.idpagoporcaja is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 5 THEN
    --Pago por banco
    if (:new.idformapago != 20 or
       (:new.idcuenta is null and :new.idcuentadeudor is null) or
       ((:new.idcuentadeudor is not null and :new.idpersonadeudor is null) or
       (:new.idcuentadeudor is null and :new.idpersonadeudor is not null)) or
       :new.imptotalpagadoporbanco <= 0 or :new.imptotalpagado <= 0 or
       (:new.estado != 1 and :new.estado != 5) or
       :new.iddisquetecargos is null or
       :new.idfacturaincluidaendisquete is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 6 THEN
    --Devolución
    if (:new.imptotalporpagar <= 0 or :new.estado != 4 or
       :new.iddisquetedevoluciones is null or :new.idrecibo is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 7 THEN
    --renegociación
    if (:new.idrenegociacion is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 8 THEN
    --Anulación
    if (:new.imptotalporpagar <> 0 or :new.estado != 8 or
       :new.idabono is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 9 THEN
    --Anulación comisión
    --imptotalporpagar <>0 no sé pone debido a que cuando se llega a este estado no se pone a cero el importetotalporpagar, es un fallo que se debe de solucionar para poder incluirlo en el if
    if (:new.estado != 8 or :new.comisionidfactura is null) then
      RAISE E_ERROR;
    end if;
  ELSIF v_idTipoAccion = 10 THEN
    --Compensación
    if (:new.imptotalcompensado <= 0 or :new.idpagoporcaja is null or
       :new.idabono is null) then
      RAISE E_ERROR;
    end if;
  END IF;
EXCEPTION
  WHEN E_ERROR THEN
    -- Particularizamos la excepción
    IF (v_idTipoAccion = 1) then
      RAISE_APPLICATION_ERROR(-20001,
                              'Los datos de la emisión no son correctos.');
    ELSIF (v_idTipoAccion = 2) then
      RAISE_APPLICATION_ERROR(-20002,
                              'Los datos de la confirmación no son correctos.');
    ELSIF (v_idTipoAccion = 3) then
      RAISE_APPLICATION_ERROR(-20003,
                              'El importe total anticipado debe ser mayor que cero');
    ELSIF (v_idTipoAccion = 4) then
      RAISE_APPLICATION_ERROR(-20004,
                              'Los datos del pago por caja no son correctos');
    ELSIF (v_idTipoAccion = 5) then
      RAISE_APPLICATION_ERROR(-20005,
                              'Los datos del pago por banco no son correctos');
    ELSIF (v_idTipoAccion = 6) then
      RAISE_APPLICATION_ERROR(-20006,
                              'Los datos de la devolución no son correctos');
    ELSIF (v_idTipoAccion = 7) then
      RAISE_APPLICATION_ERROR(-20007,
                              'Los datos de la renegociación no son correctos');
    ELSIF (v_idTipoAccion = 8) then
      RAISE_APPLICATION_ERROR(-20008,
                              'Los datos de la anulación no son correctos');
    ELSIF (v_idTipoAccion = 9) then
      RAISE_APPLICATION_ERROR(-20009,
                              'Los datos de la anulación comisión no son correctos');
    ELSIF (v_idTipoAccion = 10) then
      RAISE_APPLICATION_ERROR(-20010,
                              'Los datos de compensación no son correctos');
    
    END IF;
End FAC_HISTORICOFACTURA_BF;
------------------------------------------------------------------------------------------------------------------
  
