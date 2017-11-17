-- Create table
create table CRM_T_PROD_CATALOG_MOD_LINE
(
  c_id             VARCHAR2(40) not null,
  c_maintain_id    VARCHAR2(40),
  c_op_type        VARCHAR2(40),
  c_mater_code     VARCHAR2(40),
  c_catalog_id     VARCHAR2(40),
  c_created_by_id  VARCHAR2(40),
  dt_created_at    DATE,
  c_created_pos_id VARCHAR2(40),
  c_created_org_id VARCHAR2(40),
  c_updated_by_id  VARCHAR2(40),
  dt_updated_at    DATE
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
comment on table CRM_T_PROD_CATALOG_MOD_LINE
  is '��Ʒ����Ŀ¼�����ϸ';
-- Add comments to the columns 
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_id
  is '����';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_maintain_id
  is '���뵥ID';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_op_type
  is '��������';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_mater_code
  is '���Ϻ�';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_catalog_id
  is 'Ŀ¼ID';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_created_by_id
  is '������';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.dt_created_at
  is '����ʱ��';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_created_pos_id
  is '�����˸�λ';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_created_org_id
  is '��������֯';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.c_updated_by_id
  is '�޸���';
comment on column CRM_T_PROD_CATALOG_MOD_LINE.dt_updated_at
  is '�޸�ʱ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_PROD_CATALOG_MOD_LINE
  add constraint CRM_T_PROD_CATALOG_MOD_PK primary key (C_ID)
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
