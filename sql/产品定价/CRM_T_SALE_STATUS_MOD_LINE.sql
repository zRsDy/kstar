-- Create table
create table CRM_T_SALE_STATUS_MOD_LINE
(
  c_id                  VARCHAR2(32) not null,
  c_maintain_id         VARCHAR2(32),
  c_mater_code          VARCHAR2(32),
  c_current_sale_status VARCHAR2(32),
  c_sale_status         VARCHAR2(500),
  c_reason              VARCHAR2(500),
  c_remark              VARCHAR2(32),
  c_created_by_id       VARCHAR2(32),
  dt_created_at         DATE,
  c_created_pos_id      VARCHAR2(32),
  c_created_org_id      VARCHAR2(32),
  c_updated_by_id       VARCHAR2(32),
  dt_updated_at         DATE
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
comment on table CRM_T_SALE_STATUS_MOD_LINE
  is '��Ʒ����״̬ά��';
-- Add comments to the columns 
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_id
  is '����';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_maintain_id
  is 'ά����ID';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_mater_code
  is '���Ϻ�';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_current_sale_status
  is '��Ʒ��ǰ����״̬';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_sale_status
  is '���������״̬';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_reason
  is '���ԭ��';
comment on column CRM_T_SALE_STATUS_MOD_LINE.c_remark
  is '��ע';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_SALE_STATUS_MOD_LINE
  add constraint CRM_T_SALE_STATUS_MOD_PK primary key (C_ID)
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
