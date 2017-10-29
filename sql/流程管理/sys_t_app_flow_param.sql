create table sys_t_app_flow_power
(
  c_pid                   				VARCHAR2(32) not null,
  c_application_id                   	VARCHAR2(32),
  c_area_name          					VARCHAR2(300),
  c_power          						VARCHAR2(32),
  c_remark         						VARCHAR2(500),
  CONSTRAINT "pk_sys_t_app_flow_power" PRIMARY KEY ("C_PID")
);

comment on table sys_t_app_flow_power is '应用流程权限表';
comment on column sys_t_app_flow_power.c_pid is 'ID';
comment on column sys_t_app_flow_power.c_application_id is '应用id';
comment on column sys_t_app_flow_power.c_area_name is '域名称';
comment on column sys_t_app_flow_power.c_power is '权限';
comment on column sys_t_app_flow_power.c_remark is '备注';
