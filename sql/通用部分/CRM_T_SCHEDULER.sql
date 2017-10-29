-- Create table
create table CRM_T_SCHEDULER
(
  c_pid             VARCHAR2(40) primary key,
  c_job_name        VARCHAR2(100) not null,
  c_group_name      VARCHAR2(100) not null,
  c_class_path      VARCHAR2(200) not null,
  c_start_time      DATE,
  c_end_time        DATE,
  c_repeat_count    NUMBER(10),
  c_repeat_interval NUMBER(10) not null,
  c_status_cd       VARCHAR2(40),
  c_job_params      VARCHAR2(100)
);
-- Add comments to the columns 
comment on column CRM_T_SCHEDULER.c_pid
  is '主键自增';
comment on column CRM_T_SCHEDULER.c_job_name
  is '任务名';
comment on column CRM_T_SCHEDULER.c_group_name
  is '任务组名';
comment on column CRM_T_SCHEDULER.c_class_path
  is '任务类';
comment on column CRM_T_SCHEDULER.c_start_time
  is '任务开始时间';
comment on column CRM_T_SCHEDULER.c_end_time
  is '任务结束时间';
comment on column CRM_T_SCHEDULER.c_repeat_count
  is '任务重复次数';
comment on column CRM_T_SCHEDULER.c_repeat_interval
  is '任务重复时间间隔';
comment on column CRM_T_SCHEDULER.c_status_cd
  is '任务状态';
comment on column CRM_T_SCHEDULER.c_job_params
  is '任务参数';

