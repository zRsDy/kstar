-- Create table
create table CRM_T_PRODUCT_DOC
(
  C_PID           VARCHAR2(32) not null,
  C_DOC_NAME      VARCHAR2(30) not null,
  C_DOC_DESC      VARCHAR2(100) not null,
  C_APPLY_PERSON  VARCHAR2(32),
  DT_APPLY_DATE   DATE,
  C_APPLY_STATUS  VARCHAR2(32) not null,
  C_UPLOAD_PERSON VARCHAR2(32),
  DT_UPLOAD_DATE  DATE,
  C_ACCESS_GROUP  VARCHAR2(32),
  C_PRO_ID        VARCHAR2(32),
  C_DOC_URL       VARCHAR2(200),
  C_DOC_FROM      VARCHAR2(32),
  C_REATED_BY_ID  VARCHAR2(32),
  DT_CREATED_AT   DATE default sysdate
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
comment on table CRM_T_PRODUCT_DOC
  is '产品文档';
-- Add comments to the columns 
comment on column CRM_T_PRODUCT_DOC.C_PID
  is '文档ID';
comment on column CRM_T_PRODUCT_DOC.C_DOC_NAME
  is '文档名称';
comment on column CRM_T_PRODUCT_DOC.C_DOC_DESC
  is '文档说明';
comment on column CRM_T_PRODUCT_DOC.C_APPLY_PERSON
  is '申请人';
comment on column CRM_T_PRODUCT_DOC.DT_APPLY_DATE
  is '申请时间';
comment on column CRM_T_PRODUCT_DOC.C_APPLY_STATUS
  is '申请状态';
comment on column CRM_T_PRODUCT_DOC.C_UPLOAD_PERSON
  is '上传人';
comment on column CRM_T_PRODUCT_DOC.DT_UPLOAD_DATE
  is '上传时间';
comment on column CRM_T_PRODUCT_DOC.C_ACCESS_GROUP
  is '访问组';
comment on column CRM_T_PRODUCT_DOC.C_PRO_ID
  is '产品ID';
comment on column CRM_T_PRODUCT_DOC.C_DOC_URL
  is '文档地址';
comment on column CRM_T_PRODUCT_DOC.C_DOC_FROM
  is '文档来源：PDM，CRM';
comment on column CRM_T_PRODUCT_DOC.C_REATED_BY_ID
  is '记录创建者用户ID
';
comment on column CRM_T_PRODUCT_DOC.DT_CREATED_AT
  is '记录创建时间
';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_PRODUCT_DOC
  add constraint PK_DOC_ID primary key (C_PID)
  using index 
  tablespace TS_CRM
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
