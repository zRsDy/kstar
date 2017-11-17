-- Create table
create table CRM_T_VERIFICATION_DETAIL
(
  c_pid                  VARCHAR2(32) not null,
  c_contract_code        VARCHAR2(32),
  c_receipts_id          VARCHAR2(32),
  c_receipts_plan        VARCHAR2(300),
  c_receipts_stage       VARCHAR2(300),
  c_contract_borrow      VARCHAR2(32),
  n_veri_amount          NUMBER(16,4),
  c_receipts_type        VARCHAR2(32),
  dt_veri_date           DATE,
  c_biz_dept             VARCHAR2(32),
  c_salesman_id          VARCHAR2(300),
  c_remarks              VARCHAR2(300),
  c_status               VARCHAR2(32),
  c_delivery_id          VARCHAR2(32),
  c_delivery_code        VARCHAR2(32),
  c_cust_id              VARCHAR2(32),
  c_cust_code            VARCHAR2(32),
  c_salesman_name        VARCHAR2(300),
  n_plan_amount          NUMBER(16,4),
  c_receipts_code        VARCHAR2(32),
  c_contract_id          VARCHAR2(32),
  c_contr_rece_detail_id VARCHAR2(32)
)
tablespace TS_CRM
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table CRM_T_VERIFICATION_DETAIL
  is '核销明细表';
-- Add comments to the columns 
comment on column CRM_T_VERIFICATION_DETAIL.c_pid
  is '主键';
comment on column CRM_T_VERIFICATION_DETAIL.c_contract_code
  is '合同编单号';
comment on column CRM_T_VERIFICATION_DETAIL.c_receipts_code
  is '收款单编号';
comment on column CRM_T_VERIFICATION_DETAIL.c_receipts_plan
  is '合同收款计划行';
comment on column CRM_T_VERIFICATION_DETAIL.c_receipts_stage
  is '收款阶段';
comment on column CRM_T_VERIFICATION_DETAIL.c_contract_borrow
  is '借货合同';
comment on column CRM_T_VERIFICATION_DETAIL.c_order_code
  is '销售订单编号';
comment on column CRM_T_VERIFICATION_DETAIL.n_veri_amount
  is '核销金额';
comment on column CRM_T_VERIFICATION_DETAIL.c_receipts_type
  is '收款分类';
comment on column CRM_T_VERIFICATION_DETAIL.dt_veri_date
  is '核销时间';
comment on column CRM_T_VERIFICATION_DETAIL.c_biz_dept
  is '业务部门';
comment on column CRM_T_VERIFICATION_DETAIL.c_salesman
  is '销售人员';
comment on column CRM_T_VERIFICATION_DETAIL.c_remarks
  is '备注';
comment on column CRM_T_VERIFICATION_DETAIL.c_status
  is '状态';
-- Create/Recreate primary, unique and foreign key constraints 
alter table CRM_T_VERIFICATION_DETAIL
  add constraint PK_M_T_VERIFICATION_DETAIL primary key (C_PID)
  using index 
  tablespace TS_CRM
  pctfree 10
  initrans 2
  maxtrans 255;
