-- Create table
create table CRM_T_CONTRACT_RECEIPT_DETAIL
(
  c_pid             VARCHAR2(32) not null,
  c_contract_code   VARCHAR2(300),
  c_receipts_plan   VARCHAR2(300),
  c_receipts_stage  VARCHAR2(300),
  c_contract_borrow VARCHAR2(300),
  n_veri_amount     NUMBER(16,4),
  c_receipts_type   VARCHAR2(32),
  c_biz_dept        VARCHAR2(32),
  c_salesman_id     VARCHAR2(300),
  c_remarks         VARCHAR2(300),
  c_status          VARCHAR2(32),
  c_delivery_id     VARCHAR2(32),
  c_delivery_code   VARCHAR2(300),
  c_cust_id         VARCHAR2(32),
  c_cust_code       VARCHAR2(300),
  c_salesman_name   VARCHAR2(300),
  n_plan_amount     NUMBER(16,4),
  c_contract_id     VARCHAR2(32),
  c_updated_by_id   VARCHAR2(32),
  dt_updated_at     DATE,
  c_created_by_id   VARCHAR2(100),
  dt_created_at     DATE,
  c_created_pos_id  VARCHAR2(100),
  c_created_org_id  VARCHAR2(100),
  c_cust_name       VARCHAR2(300),
  c_salesman_code   VARCHAR2(300),
  dt_payment_date   DATE,
  c_contrpay_id     VARCHAR2(100)
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
-- Add comments to the columns 
comment on column CRM_T_CONTRACT_RECEIPT_DETAIL.dt_payment_date
  is '回款日期';
comment on column CRM_T_CONTRACT_RECEIPT_DETAIL.c_contrpay_id
  is '回款规划行ID';
