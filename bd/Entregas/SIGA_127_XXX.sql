

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
VALUES (9,'Factura devuelta: anulada para integrar la comisión bancaria',SYSDATE,0);
INSERT INTO FAC_TIPOSACCIONFACTURA (IDTIPOACCION, NOMBRE,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (10,'COMPENSACIÓN',SYSDATE,0);