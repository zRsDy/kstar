-- Create table
create table CRM_T_CUSTOM_SAT_INVEST
(
  c_pid                        VARCHAR2(32) primary key,
  c_invest_code                VARCHAR2(32) not null,
  c_principal                  VARCHAR2(32) not null,
  c_area_or_industry           VARCHAR2(32) not null,
  c_service_attitude           VARCHAR2(40),
  c_service_attitude_improve   VARCHAR2(300),
  c_business_ability           VARCHAR2(40),
  c_business_ability_improve   VARCHAR2(300),
  c_cooperate_attitude         VARCHAR2(40),
  c_cooperate_attitude_improve VARCHAR2(300),
  c_salesman_advice            VARCHAR2(300),
  c_create_employee            VARCHAR2(32) not null,
  c_create_date                VARCHAR2(32),
  c_creator_pos_id             VARCHAR2(32),
  c_creator_org_id             VARCHAR2(32),
  c_update_employee            VARCHAR2(32),
  c_update_date                VARCHAR2(32),
  c_status_cd                  VARCHAR2(32),
  c_proc_status_cd             VARCHAR2(32) not null,
  c_invest_quarter             VARCHAR2(32)
);
-- Add comments to the columns 
comment on column CRM_T_CUSTOM_SAT_INVEST.c_pid
  is '主键自增';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_invest_code
  is '调查编号';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_principal
  is '负责人';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_area_or_industry
  is '办事处区域/行业';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_service_attitude
  is '服务态度';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_service_attitude_improve
  is '服务态度改进';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_business_ability
  is '业务能力';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_business_ability_improve
  is '业务能力改进';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_cooperate_attitude
  is '工作的配合程度';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_cooperate_attitude_improve
  is '工作的配合程度改进';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_salesman_advice
  is '销售对客服的合理化建议';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_create_employee
  is '调查发起人';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_create_date
  is '调查发起时间';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_creator_pos_id
  is '发起人岗位';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_creator_org_id
  is '发起人组织';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_update_employee
  is '修改人';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_update_date
  is '修改时间';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_status_cd
  is '状态';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_proc_status_cd
  is '流程状态';
comment on column CRM_T_CUSTOM_SAT_INVEST.c_invest_quarter
  is '季度';

