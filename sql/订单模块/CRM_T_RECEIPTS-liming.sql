-- Create table
create table CRM_T_RECEIPTS
(
  c_pid                   VARCHAR2(32) not null,
  c_receipts_code         VARCHAR2(32),
  dt_receipts_date        DATE,
  c_business_code         VARCHAR2(300),
  c_receipts_bank         VARCHAR2(300),
  c_bank_account          VARCHAR2(300),
  c_currency              VARCHAR2(32),
  c_payment_customer_name VARCHAR2(300),
  c_erp_cust              VARCHAR2(300),
  c_biz_dept              VARCHAR2(300),
  c_salesman_id           VARCHAR2(32),
  c_remarks               VARCHAR2(300),
  c_pic                   VARCHAR2(300),
  c_resp_dept             VARCHAR2(300),
  c_status                VARCHAR2(32),
  dt_create_time          DATE,
  c_business_name         VARCHAR2(300),
  c_correct_cust_id       VARCHAR2(300),
  c_correct_cust_name     VARCHAR2(300),
  c_business_id           VARCHAR2(300),
  c_payment_customer_id   VARCHAR2(300),
  c_region                VARCHAR2(300),
  n_amount                NUMBER(16,4),
  c_pic_no                VARCHAR2(300),
  c_sales_center          VARCHAR2(32),
  c_salesman_name         VARCHAR2(300),
  n_veri_amount           NUMBER(16,4),
  c_created_by_id         VARCHAR2(100),
  dt_created_at           DATE,
  c_created_pos_id        VARCHAR2(100),
  c_created_org_id        VARCHAR2(100),
  c_updated_by_id         VARCHAR2(100),
  dt_updated_at           DATE,
  n_arrival_amount        NUMBER(16,4),
  n_poundage              NUMBER(16,4),
  n_freight               NUMBER(16,4),
  c_is_advances_received  VARCHAR2(10),
  c_contract_code         VARCHAR2(300),
  c_receipts_type         VARCHAR2(300),
  c_salesman_code         VARCHAR2(300),
  c_pic_salecenter        VARCHAR2(300),
  c_erp_imp               VARCHAR2(300) default 'No',
  c_error_message         VARCHAR2(3000)
)
tablespace TS_CRM
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
comment on table CRM_T_RECEIPTS
  is '收款单表';
-- Add comments to the columns 
comment on column CRM_T_RECEIPTS.c_pid
  is '主键';
comment on column CRM_T_RECEIPTS.c_receipts_code
  is '收款单号';
comment on column CRM_T_RECEIPTS.dt_receipts_date
  is '收款日期';
comment on column CRM_T_RECEIPTS.c_business_code
  is '业务实体';
comment on column CRM_T_RECEIPTS.c_receipts_bank
  is '收款银行';
comment on column CRM_T_RECEIPTS.c_bank_account
  is '银行账号';
comment on column CRM_T_RECEIPTS.c_currency
  is '币种';
comment on column CRM_T_RECEIPTS.c_payment_customer_name
  is '付款客户名称';
comment on column CRM_T_RECEIPTS.c_erp_cust
  is '是否ERP客户';
comment on column CRM_T_RECEIPTS.c_biz_dept
  is '业务部门';
comment on column CRM_T_RECEIPTS.c_salesman_id
  is '销售人员ID';
comment on column CRM_T_RECEIPTS.c_remarks
  is '备注';
comment on column CRM_T_RECEIPTS.c_pic
  is '负责人';
comment on column CRM_T_RECEIPTS.c_resp_dept
  is '负责部门';
comment on column CRM_T_RECEIPTS.c_status
  is '状态';
comment on column CRM_T_RECEIPTS.dt_create_time
  is '创建日期';
comment on column CRM_T_RECEIPTS.c_business_name
  is '业务实体名称';
comment on column CRM_T_RECEIPTS.c_correct_cust_id
  is '更正客户ID';
comment on column CRM_T_RECEIPTS.c_correct_cust_name
  is '更正客名称';
comment on column CRM_T_RECEIPTS.c_business_id
  is '业务实体ID';
comment on column CRM_T_RECEIPTS.c_payment_customer_id
  is '付款客户ID';
comment on column CRM_T_RECEIPTS.c_region
  is '地区（国家/省）';
comment on column CRM_T_RECEIPTS.n_amount
  is '合计';
comment on column CRM_T_RECEIPTS.c_pic_no
  is '负责人员工号';
comment on column CRM_T_RECEIPTS.c_sales_center
  is '所属销售中心';
comment on column CRM_T_RECEIPTS.c_salesman_name
  is '销售人员名称';
comment on column CRM_T_RECEIPTS.n_veri_amount
  is '已核销金额';
comment on column CRM_T_RECEIPTS.c_created_by_id
  is '创建人';
comment on column CRM_T_RECEIPTS.dt_created_at
  is '创建时间';
comment on column CRM_T_RECEIPTS.c_created_pos_id
  is '创建人岗位';
comment on column CRM_T_RECEIPTS.c_created_org_id
  is '创建人组织';
comment on column CRM_T_RECEIPTS.c_updated_by_id
  is '更新者';
comment on column CRM_T_RECEIPTS.dt_updated_at
  is '更新时间';
comment on column CRM_T_RECEIPTS.n_arrival_amount
  is '到账金额';
comment on column CRM_T_RECEIPTS.n_poundage
  is '手续费';
comment on column CRM_T_RECEIPTS.n_freight
  is '代垫运费';
comment on column CRM_T_RECEIPTS.c_is_advances_received
  is '是否是预收款';
comment on column CRM_T_RECEIPTS.c_contract_code
  is '合同编号';
comment on column CRM_T_RECEIPTS.c_receipts_type
  is '收款类型';
comment on column CRM_T_RECEIPTS.c_salesman_code
  is '销售人员编码';
comment on column CRM_T_RECEIPTS.c_pic_salecenter
  is '负责人销售中心';
comment on column CRM_T_RECEIPTS.c_erp_imp
  is 'ERP已导入';
-- Create/Recreate indexes 
create index CRM_T_RECEIPTS_N1 on CRM_T_RECEIPTS (C_RECEIPTS_CODE)
  tablespace TS_CRM
  pctfree 10
  initrans 4
  maxtrans 255
  storage
  (
    initial 16K
    next 128K
    minextents 1
    maxextents unlimited
  );
create index CRM_T_RECEIPTS_N2 on CRM_T_RECEIPTS (DT_UPDATED_AT)
  tablespace TS_CRM
  pctfree 10
  initrans 4
  maxtrans 255
  storage
  (
    initial 16K
    next 128K
    minextents 1
    maxextents unlimited
  );
create index CRM_T_RECEIPTS_N3 on CRM_T_RECEIPTS (C_ERP_IMP)
  tablespace TS_CRM
  pctfree 10
  initrans 4
  maxtrans 255
  storage
  (
    initial 16K
    next 128K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_RECEIPTS
  add constraint PK_M_T_RECEIPTS primary key (C_PID)
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
