/*==============================================================*/
/* Database name:  Database_1                                   */
/* DBMS name:      ORACLE Version 10gR2                         */
/* Created on:     12/29/2016 5:40:38 PM                        */
/*==============================================================*/


drop table CRM_T_CONTR_ADDR cascade constraints;

drop table CRM_T_CONTR_BASIC cascade constraints;

drop table CRM_T_CONTR_CHANGE cascade constraints;

drop table CRM_T_CONTR_CLAUSE cascade constraints;

drop table CRM_T_CONTR_PAY cascade constraints;

/*==============================================================*/
/* Database: "Database_1"                                       */
/*==============================================================*/
create database "Database_1";

/*==============================================================*/
/* Table: CRM_T_CONTR_ADDR                                      */
/*==============================================================*/
create table CRM_T_CONTR_ADDR  (
   N_PID                NUMBER(32),
   C_CONTR_NO           VARCHAR2(32),
   C_CONTR_NAME         VARCHAR2(100),
   C_CONTR_TYPE         VARCHAR2(10),
   C_DELIV_ADDR         VARCHAR2(200),
   C_RECEIVER           VARCHAR2(32),
   C_PHONE              VARCHAR2(32),
   C_REMARK             VARCHAR2(100)
);

comment on column CRM_T_CONTR_ADDR.N_PID is
'��������';

comment on column CRM_T_CONTR_ADDR.C_CONTR_NO is
'��ͬ���';

comment on column CRM_T_CONTR_ADDR.C_CONTR_NAME is
'��ͬ����';

comment on column CRM_T_CONTR_ADDR.C_CONTR_TYPE is
'��ͬ����';

comment on column CRM_T_CONTR_ADDR.C_DELIV_ADDR is
'�ͻ���ַ';

comment on column CRM_T_CONTR_ADDR.C_RECEIVER is
'�ջ���';

comment on column CRM_T_CONTR_ADDR.C_PHONE is
'��ϵ�绰';

comment on column CRM_T_CONTR_ADDR.C_REMARK is
'��ע';

/*==============================================================*/
/* Table: CRM_T_CONTR_BASIC                                     */
/*==============================================================*/
create table CRM_T_CONTR_BASIC  (
   N_PID                NUMBER(32),
   N_CONTR_VER          VARCHAR2(32),
   C_CONTR_NO           VARCHAR2(32),
   C_CONTR_NAME         VARCHAR2(100),
   C_CONTR_TYPE         VARCHAR2(10),
   C_CONTR_STAT         VARCHAR2(2),
   C_PROJ_NAME          VARCHAR2(100),
   C_PROJ_NO            VARCHAR2(32),
   C_CUST_CODE          VARCHAR2(32),
   C_CUST_NAME          VARCHAR2(100),
   C_CUST_CONTR_NO      VARCHAR2(32),
   C_CUST_LINK          VARCHAR2(100),
   C_PRIC_NO            VARCHAR2(32),
   C_PRIC_TABLE         VARCHAR2(100),
   C_REVIEW_STAT        VARCHAR2(2),
   C_TRIAL_STAT         VARCHAR2(2),
   C_CANCEL_STAT        VARCHAR2(2),
   C_FRAME_NO           VARCHAR2(32),
   N_TOTAL_AMT          NUMBER(12,4),
   N_NCAN_NUM           NUMBER(12,4),
   N_NCAN_AMT           NUMBER(12,4),
   C_BUSS_ENITY         VARCHAR2(100),
   C_ORG                VARCHAR2(100),
   C_PAY_WAY            VARCHAR2(10),
   C_PAY_COND           VARCHAR2(10),
   C_IS_VALID           VARCHAR2(2),
   C_IS_CHANGE          VARCHAR2(2),
   C_IS_SEAL_FIRST      CHAR(1),
   C_IS_WHOLE_SET       VARCHAR2(2),
   C_IS_CONF_LIST       VARCHAR2(2),
   C_IS_DELIV_HOME      VARCHAR2(2),
   C_IS_UNLOAD          VARCHAR2(2),
   C_IS_HOME_INSTALL    VARCHAR2(2),
   C_IS_AUX             VARCHAR2(2),
   C_IS_TRANF_SALE      VARCHAR2(2),
   C_POST_NO            VARCHAR2(100),
   DT_POST_DATE         DATE,
   DT_DELIV_DATE        DATE,
   DT_SYS_SIGN_DATE     DATE,
   DT_ACT_SIGN_DATE     DATE,
   C_CREATOR            VARCHAR2(32),
   DT_CREATE_TIME       TIMESTAMP,
   C_CHANGER            VARCHAR2(32),
   DT_CHANGE_TIME       TIMESTAMP,
   C_SUBMITER           VARCHAR2(32),
   DT_SUBMIT_TIME       TIMESTAMP,
   C_PROC_INST_ID       VARCHAR2(20),
   C_REMARK             VARCHAR2(100)
);

comment on column CRM_T_CONTR_BASIC.N_PID is
'��������';

comment on column CRM_T_CONTR_BASIC.N_CONTR_VER is
'��ͬ�汾';

comment on column CRM_T_CONTR_BASIC.C_CONTR_NO is
'��ͬ���';

comment on column CRM_T_CONTR_BASIC.C_CONTR_NAME is
'��ͬ����';

comment on column CRM_T_CONTR_BASIC.C_CONTR_TYPE is
'��ͬ����';

comment on column CRM_T_CONTR_BASIC.C_CONTR_STAT is
'��ͬ״̬';

comment on column CRM_T_CONTR_BASIC.C_PROJ_NAME is
'��Ŀ����';

comment on column CRM_T_CONTR_BASIC.C_PROJ_NO is
'��Ŀ���';

comment on column CRM_T_CONTR_BASIC.C_CUST_CODE is
'�ͻ����';

comment on column CRM_T_CONTR_BASIC.C_CUST_NAME is
'�ͻ�����';

comment on column CRM_T_CONTR_BASIC.C_CUST_CONTR_NO is
'�ͻ���ͬ��';

comment on column CRM_T_CONTR_BASIC.C_CUST_LINK is
'�ͻ���ϵ��';

comment on column CRM_T_CONTR_BASIC.C_PRIC_NO is
'���۵����';

comment on column CRM_T_CONTR_BASIC.C_PRIC_TABLE is
'���۱�';

comment on column CRM_T_CONTR_BASIC.C_REVIEW_STAT is
'����״̬';

comment on column CRM_T_CONTR_BASIC.C_TRIAL_STAT is
'����״̬';

comment on column CRM_T_CONTR_BASIC.C_CANCEL_STAT is
'����״̬';

comment on column CRM_T_CONTR_BASIC.C_FRAME_NO is
'���Э����';

comment on column CRM_T_CONTR_BASIC.N_TOTAL_AMT is
'�ܽ��';

comment on column CRM_T_CONTR_BASIC.N_NCAN_NUM is
'δ��������';

comment on column CRM_T_CONTR_BASIC.N_NCAN_AMT is
'δ�������';

comment on column CRM_T_CONTR_BASIC.C_BUSS_ENITY is
'ҵ��ʵ��';

comment on column CRM_T_CONTR_BASIC.C_ORG is
'��֯';

comment on column CRM_T_CONTR_BASIC.C_PAY_WAY is
'���ʽ';

comment on column CRM_T_CONTR_BASIC.C_PAY_COND is
'��������';

comment on column CRM_T_CONTR_BASIC.C_IS_VALID is
'��Ч��ʶ';

comment on column CRM_T_CONTR_BASIC.C_IS_CHANGE is
'�����ʶ';

comment on column CRM_T_CONTR_BASIC.C_IS_SEAL_FIRST is
'�Ƿ��ȸ���';

comment on column CRM_T_CONTR_BASIC.C_IS_WHOLE_SET is
'�Ƿ����';

comment on column CRM_T_CONTR_BASIC.C_IS_CONF_LIST is
'�Ƿ��������嵥';

comment on column CRM_T_CONTR_BASIC.C_IS_DELIV_HOME is
'�Ƿ��ͻ�����';

comment on column CRM_T_CONTR_BASIC.C_IS_UNLOAD is
'�Ƿ���ж��';

comment on column CRM_T_CONTR_BASIC.C_IS_HOME_INSTALL is
'�Ƿ����Ű�װ';

comment on column CRM_T_CONTR_BASIC.C_IS_AUX is
'�Ƿ��ṩ��װ����';

comment on column CRM_T_CONTR_BASIC.C_IS_TRANF_SALE is
'ת����ʶ';

comment on column CRM_T_CONTR_BASIC.C_POST_NO is
'�ʼĵ���';

comment on column CRM_T_CONTR_BASIC.DT_POST_DATE is
'�ʼ�����';

comment on column CRM_T_CONTR_BASIC.DT_DELIV_DATE is
'��������';

comment on column CRM_T_CONTR_BASIC.DT_SYS_SIGN_DATE is
'ϵͳǩ������';

comment on column CRM_T_CONTR_BASIC.DT_ACT_SIGN_DATE is
'ʵ��ǩ������';

comment on column CRM_T_CONTR_BASIC.C_CREATOR is
'������';

comment on column CRM_T_CONTR_BASIC.DT_CREATE_TIME is
'��������';

comment on column CRM_T_CONTR_BASIC.C_CHANGER is
'�����';

comment on column CRM_T_CONTR_BASIC.DT_CHANGE_TIME is
'�������';

comment on column CRM_T_CONTR_BASIC.C_SUBMITER is
'�����ύ��';

comment on column CRM_T_CONTR_BASIC.DT_SUBMIT_TIME is
'�ύ����ʱ��';

comment on column CRM_T_CONTR_BASIC.C_PROC_INST_ID is
'����ʵ��ID';

comment on column CRM_T_CONTR_BASIC.C_REMARK is
'��ע';

/*==============================================================*/
/* Table: CRM_T_CONTR_CHANGE                                    */
/*==============================================================*/
create table CRM_T_CONTR_CHANGE  (
   N_PID                NUMBER(32),
   N_CONTR_VER          VARCHAR2(32),
   C_CHANGE_NO          VARCHAR2(32),
   C_CHANGE_TYPE        VARCHAR2(32),
   C_CHANGE_REASON      VARCHAR2(100),
   C_CHANGE_CONT        VARCHAR2(100),
   C_CONTR_NO           VARCHAR2(32),
   C_CONTR_NAME         VARCHAR2(100),
   C_CONTR_TYPE         VARCHAR2(10),
   C_CONTR_STAT         VARCHAR2(2),
   C_PROJ_NAME          VARCHAR2(100),
   C_PROJ_NO            VARCHAR2(32),
   C_CUST_CODE          VARCHAR2(32),
   C_CUST_NAME          VARCHAR2(100),
   C_CUST_CONTR_NO      VARCHAR2(32),
   C_CUST_LINK          VARCHAR2(100),
   C_PRIC_NO            VARCHAR2(32),
   C_PRIC_TABLE         VARCHAR2(100),
   C_REVIEW_STAT        VARCHAR2(2),
   C_TRIAL_STAT         VARCHAR2(2),
   C_CANCEL_STAT        VARCHAR2(2),
   C_FRAME_NO           VARCHAR2(32),
   N_TOTAL_AMT          NUMBER(12,4),
   N_CANCEL_NUM         NUMBER(12,4),
   N_CANCEL_AMT         NUMBER(12,4),
   C_BUSS_ENITY         VARCHAR2(100),
   C_ORG                VARCHAR2(100),
   C_PAY_WAY            VARCHAR2(10),
   C_PAY_COND           VARCHAR2(10),
   C_IS_VALID           VARCHAR2(2),
   C_IS_CHANGE          VARCHAR2(2),
   C_IS_SEAL_FIRST      CHAR(1),
   C_IS_WHOLE_SET       VARCHAR2(2),
   C_IS_CONF_LIST       VARCHAR2(2),
   C_IS_DELIV_HOME      VARCHAR2(2),
   C_IS_UNLOAD          VARCHAR2(2),
   C_IS_HOME_INSTALL    VARCHAR2(2),
   C_IS_AUX             VARCHAR2(2),
   C_IS_TRANF_SALE      VARCHAR2(2),
   C_POST_NO            VARCHAR2(100),
   DT_POST_DATE         DATE,
   DT_DELIV_DATE        DATE,
   DT_SYS_SIGN_DATE     DATE,
   DT_ACT_SIGN_DATE     DATE,
   C_CREATOR            VARCHAR2(32),
   DT_CREATE_TIME       TIMESTAMP,
   C_CHANGER            VARCHAR2(32),
   DT_CHANGE_TIME       TIMESTAMP,
   C_SUBMITER           VARCHAR2(32),
   DT_SUBMIT_TIME       TIMESTAMP,
   C_PROC_INST_ID       VARCHAR2(20),
   C_REMARK             VARCHAR2(100)
);

comment on column CRM_T_CONTR_CHANGE.N_PID is
'��������';

comment on column CRM_T_CONTR_CHANGE.N_CONTR_VER is
'��ͬ�汾';

comment on column CRM_T_CONTR_CHANGE.C_CHANGE_NO is
'������';

comment on column CRM_T_CONTR_CHANGE.C_CHANGE_TYPE is
'���״̬';

comment on column CRM_T_CONTR_CHANGE.C_CHANGE_REASON is
'���ԭ��';

comment on column CRM_T_CONTR_CHANGE.C_CHANGE_CONT is
'�������';

comment on column CRM_T_CONTR_CHANGE.C_CONTR_NO is
'��ͬ���';

comment on column CRM_T_CONTR_CHANGE.C_CONTR_NAME is
'��ͬ����';

comment on column CRM_T_CONTR_CHANGE.C_CONTR_TYPE is
'��ͬ����';

comment on column CRM_T_CONTR_CHANGE.C_CONTR_STAT is
'��ͬ״̬';

comment on column CRM_T_CONTR_CHANGE.C_PROJ_NAME is
'��Ŀ����';

comment on column CRM_T_CONTR_CHANGE.C_PROJ_NO is
'��Ŀ���';

comment on column CRM_T_CONTR_CHANGE.C_CUST_CODE is
'�ͻ����';

comment on column CRM_T_CONTR_CHANGE.C_CUST_NAME is
'�ͻ�����';

comment on column CRM_T_CONTR_CHANGE.C_CUST_CONTR_NO is
'�ͻ���ͬ��';

comment on column CRM_T_CONTR_CHANGE.C_CUST_LINK is
'�ͻ���ϵ��';

comment on column CRM_T_CONTR_CHANGE.C_PRIC_NO is
'���۵����';

comment on column CRM_T_CONTR_CHANGE.C_PRIC_TABLE is
'���۱�';

comment on column CRM_T_CONTR_CHANGE.C_REVIEW_STAT is
'����״̬';

comment on column CRM_T_CONTR_CHANGE.C_TRIAL_STAT is
'����״̬';

comment on column CRM_T_CONTR_CHANGE.C_CANCEL_STAT is
'����״̬';

comment on column CRM_T_CONTR_CHANGE.C_FRAME_NO is
'���Э����';

comment on column CRM_T_CONTR_CHANGE.N_TOTAL_AMT is
'�ܽ��';

comment on column CRM_T_CONTR_CHANGE.N_CANCEL_NUM is
'δ��������';

comment on column CRM_T_CONTR_CHANGE.N_CANCEL_AMT is
'δ�������';

comment on column CRM_T_CONTR_CHANGE.C_BUSS_ENITY is
'ҵ��ʵ��';

comment on column CRM_T_CONTR_CHANGE.C_ORG is
'��֯';

comment on column CRM_T_CONTR_CHANGE.C_PAY_WAY is
'���ʽ';

comment on column CRM_T_CONTR_CHANGE.C_PAY_COND is
'��������';

comment on column CRM_T_CONTR_CHANGE.C_IS_VALID is
'��Ч��ʶ';

comment on column CRM_T_CONTR_CHANGE.C_IS_CHANGE is
'�����ʶ';

comment on column CRM_T_CONTR_CHANGE.C_IS_SEAL_FIRST is
'�Ƿ��ȸ���';

comment on column CRM_T_CONTR_CHANGE.C_IS_WHOLE_SET is
'�Ƿ����';

comment on column CRM_T_CONTR_CHANGE.C_IS_CONF_LIST is
'�Ƿ��������嵥';

comment on column CRM_T_CONTR_CHANGE.C_IS_DELIV_HOME is
'�Ƿ��ͻ�����';

comment on column CRM_T_CONTR_CHANGE.C_IS_UNLOAD is
'�Ƿ���ж��';

comment on column CRM_T_CONTR_CHANGE.C_IS_HOME_INSTALL is
'�Ƿ����Ű�װ';

comment on column CRM_T_CONTR_CHANGE.C_IS_AUX is
'�Ƿ��ṩ��װ����';

comment on column CRM_T_CONTR_CHANGE.C_IS_TRANF_SALE is
'ת����ʶ';

comment on column CRM_T_CONTR_CHANGE.C_POST_NO is
'�ʼĵ���';

comment on column CRM_T_CONTR_CHANGE.DT_POST_DATE is
'�ʼ�����';

comment on column CRM_T_CONTR_CHANGE.DT_DELIV_DATE is
'��������';

comment on column CRM_T_CONTR_CHANGE.DT_SYS_SIGN_DATE is
'ϵͳǩ������';

comment on column CRM_T_CONTR_CHANGE.DT_ACT_SIGN_DATE is
'ʵ��ǩ������';

comment on column CRM_T_CONTR_CHANGE.C_CREATOR is
'������';

comment on column CRM_T_CONTR_CHANGE.DT_CREATE_TIME is
'��������';

comment on column CRM_T_CONTR_CHANGE.C_CHANGER is
'�����';

comment on column CRM_T_CONTR_CHANGE.DT_CHANGE_TIME is
'�������';

comment on column CRM_T_CONTR_CHANGE.C_SUBMITER is
'�����ύ��';

comment on column CRM_T_CONTR_CHANGE.DT_SUBMIT_TIME is
'�ύ����ʱ��';

comment on column CRM_T_CONTR_CHANGE.C_PROC_INST_ID is
'����ʵ��ID';

comment on column CRM_T_CONTR_CHANGE.C_REMARK is
'��ע';

/*==============================================================*/
/* Table: CRM_T_CONTR_CLAUSE                                    */
/*==============================================================*/
/*
create table CRM_T_CONTR_CLAUSE  (
   N_PID                NUMBER(32),
   C_CONTR_NO           VARCHAR2(32),
   C_CONTR_NAME         VARCHAR2(100),
   C_CONTR_TYPE         VARCHAR2(10),
   C_CLAUSE_TYPE        VARCHAR2(10),
   C_CLAUSE_DESC        VARCHAR2(100),
   C_REMARK             VARCHAR2(100)
);

comment on column CRM_T_CONTR_CLAUSE.N_PID is
'��������';

comment on column CRM_T_CONTR_CLAUSE.C_CONTR_NO is
'��ͬ���';

comment on column CRM_T_CONTR_CLAUSE.C_CONTR_NAME is
'��ͬ����';

comment on column CRM_T_CONTR_CLAUSE.C_CONTR_TYPE is
'��ͬ����';

comment on column CRM_T_CONTR_CLAUSE.C_CLAUSE_TYPE is
'��������';

comment on column CRM_T_CONTR_CLAUSE.C_CLAUSE_DESC is
'��������';

comment on column CRM_T_CONTR_CLAUSE.C_REMARK is
'��ע';

/*==============================================================*/
/* Table: CRM_T_CONTR_PAY                                       */
/*==============================================================*/
create table CRM_T_CONTR_PAY  (
   N_PID                NUMBER(32),
   C_CONTR_NO           VARCHAR2(32),
   C_CONTR_NAME         VARCHAR2(100),
   C_CONTR_TYPE         VARCHAR2(10),
   C_PAY_SEQ            NUMBER(2),
   C_PAY_PLAN           VARCHAR2(100),
   C_PLAN_RATIO         NUMBER(12,4),
   C_PLAN_AMT           NUMBER(12,4),
   C_ORIG_CLAUSE        VARCHAR2(100),
   C_SALER              VARCHAR2(32),
   DT_MEET_TIME         TIMESTAMP,
   N_TOTAL_AMT          NUMBER(12,4),
   N_CANCEL_AMT         NUMBER(12,4),
   N_NCAN_AMT           NUMBER(12,4),
   C_REMARK             VARCHAR2(100)
);

comment on column CRM_T_CONTR_PAY.N_PID is
'��������';

comment on column CRM_T_CONTR_PAY.C_CONTR_NO is
'��ͬ���';

comment on column CRM_T_CONTR_PAY.C_CONTR_NAME is
'��ͬ����';

comment on column CRM_T_CONTR_PAY.C_CONTR_TYPE is
'��ͬ����';

comment on column CRM_T_CONTR_PAY.C_PAY_SEQ is
'����';

comment on column CRM_T_CONTR_PAY.C_PAY_PLAN is
'�տ�ƻ�';

comment on column CRM_T_CONTR_PAY.C_PLAN_RATIO is
'�ƻ��տ����';

comment on column CRM_T_CONTR_PAY.C_PLAN_AMT is
'�ƻ��տ���';

comment on column CRM_T_CONTR_PAY.C_ORIG_CLAUSE is
'��ͬԭʼ����';

comment on column CRM_T_CONTR_PAY.C_SALER is
'������Ա';

comment on column CRM_T_CONTR_PAY.DT_MEET_TIME is
'����ʱ��';

comment on column CRM_T_CONTR_PAY.N_TOTAL_AMT is
'��ͬ�ܽ��';

comment on column CRM_T_CONTR_PAY.N_CANCEL_AMT is
'�Ѻ������';

comment on column CRM_T_CONTR_PAY.N_NCAN_AMT is
'δ�������';

comment on column CRM_T_CONTR_PAY.C_REMARK is
'��ע';

*/
-- Create table
create table CRM_T_CONTR_PAY
(
  C_ID          VARCHAR2(32),
  C_PAY_NO      VARCHAR2(32),
  C_CONTR_ID    VARCHAR2(32),
  C_PAY_SEQ     NUMBER(2),
  C_PAY_SEQ_DESC     VARCHAR2(32),
  C_PAY_PLAN    VARCHAR2(32),
  C_PLAN_RATIO  NUMBER(12,4),
  C_PLAN_AMT    NUMBER(12,4),
  C_ORIG_CLAUSE VARCHAR2(100),
  C_SALER       VARCHAR2(32),
  DT_MEET_TIME  DATE,
  N_TOTAL_AMT   NUMBER(12,4),
  N_CANCEL_AMT  NUMBER(12,4),
  N_NCAN_AMT    NUMBER(12,4),
  C_REMARK      VARCHAR2(100)
)
tablespace TS_CRM
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column CRM_T_CONTR_PAY.C_ID
  is '主键自增';
comment on column CRM_T_CONTR_PAY.C_PAY_NO
  is '回款计划编号';
comment on column CRM_T_CONTR_PAY.C_CONTR_ID
  is '合同ID';
comment on column CRM_T_CONTR_PAY.C_PAY_SEQ
  is '笔数';
comment on column CRM_T_CONTR_PAY.C_PAY_SEQ_DESC
  is '笔数描述';
comment on column CRM_T_CONTR_PAY.C_PAY_PLAN
  is '收款计划';
comment on column CRM_T_CONTR_PAY.C_PLAN_RATIO
  is '计划收款比例';
comment on column CRM_T_CONTR_PAY.C_PLAN_AMT
  is '计划收款金额';
comment on column CRM_T_CONTR_PAY.C_ORIG_CLAUSE
  is '合同原始条款';
comment on column CRM_T_CONTR_PAY.C_SALER
  is '销售人员';
comment on column CRM_T_CONTR_PAY.DT_MEET_TIME
  is '满足时间';
comment on column CRM_T_CONTR_PAY.N_TOTAL_AMT
  is '合同总金额';
comment on column CRM_T_CONTR_PAY.N_CANCEL_AMT
  is '已核销金额';
comment on column CRM_T_CONTR_PAY.N_NCAN_AMT
  is '未核销金额';
comment on column CRM_T_CONTR_PAY.C_REMARK
  is '备注';
