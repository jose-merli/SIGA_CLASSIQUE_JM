CREATE OR REPLACE procedure ECENSO.P_SYNC_ABOGADOS_REFRESH AUTHID CURRENT_USER is
begin
  --ejecutar las vistas en SIGA
  dbms_mview.refresh@dbl_siga('V_CENSO_COLEGIOS');
  dbms_mview.refresh@dbl_siga('V_CENSO_COLEGIACIONES');
  dbms_mview.refresh@dbl_siga('V_CENSO_SOCIEDADES');
  dbms_mview.refresh@dbl_siga('V_CENSO_SOCIOS');
  Uscgae.Refresh_t_Censo_Letrados@dbl_siga();
end P_SYNC_ABOGADOS_REFRESH;
/
