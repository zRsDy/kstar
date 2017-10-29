-- Create table
create table CRM_T_ACTIVITY_APPLY_WORKFLOW
(
  C_PID              VARCHAR2(32) not null,
  C_PRO_PROCESS_ID   VARCHAR2(32) not null,
  C_PRO_PROCESS_NAME VARCHAR2(20),
  C_PRO_ID           VARCHAR2(32) not null,
  C_SUBMIT_BY        VARCHAR2(32)
)
tablespace TS_CRM
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table CRM_T_ACTIVITY_APPLY_WORKFLOW
  is '市场活动相关工作流记录表';
-- Add comments to the columns 
comment on column CRM_T_ACTIVITY_APPLY_WORKFLOW.C_PID
  is '主键';
comment on column CRM_T_ACTIVITY_APPLY_WORKFLOW.C_PRO_PROCESS_ID
  is '流程ID';
comment on column CRM_T_ACTIVITY_APPLY_WORKFLOW.C_PRO_PROCESS_NAME
  is '流程名称';
comment on column CRM_T_ACTIVITY_APPLY_WORKFLOW.C_PRO_ID
  is '业务实体ID';
comment on column CRM_T_ACTIVITY_APPLY_WORKFLOW.C_SUBMIT_BY
  is '提交人';
