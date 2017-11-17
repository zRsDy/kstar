-- Create table
create table CRM_T_PROD_INFO_MAINTAIN
(
  c_id                 VARCHAR2(40) not null,
  c_applicant_id       VARCHAR2(40),
  c_applicant_dept     VARCHAR2(40),
  c_maintain_code      VARCHAR2(100),
  dt_req_complete_date DATE,
  c_remark             VARCHAR2(500),
  c_process_status     VARCHAR2(40),
  c_status             VARCHAR2(40),
  c_created_by_id      VARCHAR2(40),
  dt_created_at        DATE,
  c_created_pos_id     VARCHAR2(40),
  c_created_org_id     VARCHAR2(40),
  c_updated_by_id      VARCHAR2(40),
  dt_updated_at        DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table CRM_T_PROD_INFO_MAINTAIN
  is '��Ʒ��Ϣά������';
-- Add comments to the columns 
comment on column CRM_T_PROD_INFO_MAINTAIN.c_id
  is '����';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_applicant_id
  is '������';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_applicant_dept
  is '���벿��';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_maintain_code
  is '���뵥��';
comment on column CRM_T_PROD_INFO_MAINTAIN.dt_req_complete_date
  is '�������ʱ��';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_remark
  is '��ע';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_process_status
  is '����״̬';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_status
  is '״̬';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_created_by_id
  is '������';
comment on column CRM_T_PROD_INFO_MAINTAIN.dt_created_at
  is '����ʱ��';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_created_pos_id
  is '�����˸�λ';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_created_org_id
  is '��������֯';
comment on column CRM_T_PROD_INFO_MAINTAIN.c_updated_by_id
  is '�޸���';
comment on column CRM_T_PROD_INFO_MAINTAIN.dt_updated_at
  is '�޸�ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_PROD_INFO_MAINTAIN
  add constraint CRM_T_PROD_INFO_MAINTAIN_PK primary key (C_ID)
  using index 
  tablespace USERS
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
