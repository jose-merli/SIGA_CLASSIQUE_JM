Drop Sequence SEQ_ENV_ENVIOPROGRAMADO;

create sequence SEQ_ENV_ENVIOPROGRAMADO
minvalue 1
maxvalue 999999 --NO AUMENTAR ESTE MAXIMO. AL USARLO EN BD, SE ANYADE EL ANYO COMO PREFIJO Y EN CODIGO SE USA COMO INT
start with 1
increment by 1
nocache
cycle;
