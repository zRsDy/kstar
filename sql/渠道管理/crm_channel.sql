drop table crm_t_gift_manage;
create table crm_t_gift_manage
(
  c_pid                 VARCHAR2(32) not null,
  c_gift_code       	VARCHAR2(32),
  c_description     	VARCHAR2(300),
  c_type                VARCHAR2(32),
  c_inventory_quantity  NUMBER(16),
  c_unit        		VARCHAR2(32),
  c_currency            VARCHAR2(32),
  c_price         		NUMBER(16,4),
  c_start_date          DATE,
  c_end_date            DATE,
  c_manager           	VARCHAR2(32),
  CONSTRAINT "pk_crm_t_gift_manage" PRIMARY KEY ("C_PID")
);

comment on table crm_t_gift_manage is '资料与礼品库管理表';
comment on column crm_t_gift_manage.c_pid is 'ID';
comment on column crm_t_gift_manage.c_gift_code is '编号';
comment on column crm_t_gift_manage.c_description is '描述';
comment on column crm_t_gift_manage.c_type is '类型';
comment on column crm_t_gift_manage.c_inventory_quantity is '库存数量';
comment on column crm_t_gift_manage.c_unit is '单位';
comment on column crm_t_gift_manage.c_currency is '货币';
comment on column crm_t_gift_manage.c_price is '单价';
comment on column crm_t_gift_manage.c_start_date is '生效日期';
comment on column crm_t_gift_manage.c_end_date is '失效日期';
comment on column crm_t_gift_manage.c_manager is '管理人员';
  
drop table crm_t_gift_apply;
create table crm_t_gift_apply
(
  c_pid                 	VARCHAR2(32) not null,
  c_apply_code       		VARCHAR2(32),
  c_apply_unit     			VARCHAR2(300),
  c_custom                	VARCHAR2(300),
  c_dealer  				VARCHAR2(32),
  c_industry_type        	VARCHAR2(32),
  c_purpose            		VARCHAR2(300),
  c_status                	VARCHAR2(32),
  c_currency  				VARCHAR2(32),
  c_available_limit         NUMBER(16,4),
  c_apply_amount         	NUMBER(16,4),
  c_apply_date          	DATE,
  c_estimated_demand_date   DATE,
  c_organization  			VARCHAR2(300),
  c_applier        			VARCHAR2(32),
  c_applier_phone           VARCHAR2(300),
  c_explain                	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_gift_apply" PRIMARY KEY ("C_PID")
);

comment on table crm_t_gift_apply is '资料与礼品申请表';
comment on column crm_t_gift_apply.c_pid is 'ID';
comment on column crm_t_gift_apply.c_apply_code is '申请单号';
comment on column crm_t_gift_apply.c_apply_unit is '申请单位';
comment on column crm_t_gift_apply.c_custom is '客户';
comment on column crm_t_gift_apply.c_dealer is '是否经销商';
comment on column crm_t_gift_apply.c_industry_type is '行业类型';
comment on column crm_t_gift_apply.c_purpose is '用途';
comment on column crm_t_gift_apply.c_status is '状态';
comment on column crm_t_gift_apply.c_currency is '货币';
comment on column crm_t_gift_apply.c_available_limit is '可用额度';
comment on column crm_t_gift_apply.c_apply_amount is '申请金额';
comment on column crm_t_gift_apply.c_apply_date is '申请日期';
comment on column crm_t_gift_apply.c_estimated_demand_date is '预计需求日期';
comment on column crm_t_gift_apply.c_organization is '组织';
comment on column crm_t_gift_apply.c_applier is '申请人';
comment on column crm_t_gift_apply.c_applier_phone is '申请人电话';
comment on column crm_t_gift_apply.c_explain is '说明';

drop table crm_t_gift_apply_detail;
create table crm_t_gift_apply_detail
(
  c_pid                   	VARCHAR2(32) not null,
  c_apply_code          	VARCHAR2(32),
  c_gift_code           	VARCHAR2(32),
  c_apply_quantity          NUMBER(16),
  c_actual_give_quantity    NUMBER(16),
  c_actual_give_date        DATE,
  c_actual_get_quantity     NUMBER(16),
  c_actual_get_date         DATE,
  c_status              	VARCHAR2(32),
  c_explain               	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_gift_apply_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_gift_apply_detail is '资料与礼品申请明细表';
comment on column crm_t_gift_apply_detail.c_pid is 'ID';
comment on column crm_t_gift_apply_detail.c_apply_code is '申请单号';
comment on column crm_t_gift_apply_detail.c_gift_code is '礼品编号';
comment on column crm_t_gift_apply_detail.c_apply_quantity is '申请数量';
comment on column crm_t_gift_apply_detail.c_actual_give_quantity is '实发数量';
comment on column crm_t_gift_apply_detail.c_actual_give_date is '实发日期';
comment on column crm_t_gift_apply_detail.c_actual_get_quantity is '实到数量';
comment on column crm_t_gift_apply_detail.c_actual_get_date is '实到日期';
comment on column crm_t_gift_apply_detail.c_status is '状态';
comment on column crm_t_gift_apply_detail.c_explain is '说明';

drop table crm_t_activity_apply;  
create table crm_t_activity_apply
(
  c_pid                       	VARCHAR2(32) not null,
  c_apply_code              	VARCHAR2(32),
  c_business_type              	VARCHAR2(32),
  c_apply_unit              	VARCHAR2(300),
  c_dealer                		VARCHAR2(32),
  c_activity_type         		VARCHAR2(32),
  c_industry_type         		VARCHAR2(32),
  c_activity_purpose            VARCHAR2(300),
  c_status                		VARCHAR2(32),
  c_currency              		VARCHAR2(32),
  c_estimated_expense         	NUMBER(16),
  c_receivable_expense        	NUMBER(16),
  c_apply_date              	DATE,
  c_estimated_start_date        DATE,
  c_close_date        			DATE,
  c_organization                VARCHAR2(300),
  c_applier                   	VARCHAR2(32),
  c_applier_phone               VARCHAR2(32),
  c_explain                   	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_activity_apply" PRIMARY KEY ("C_PID")
);

comment on table crm_t_activity_apply is '市场活动申请表';
comment on column crm_t_activity_apply.c_pid is 'ID';
comment on column crm_t_activity_apply.c_apply_code is '申请单号';
comment on column crm_t_activity_apply.c_business_type is '业务类型';
comment on column crm_t_activity_apply.c_apply_unit is '申请单位';
comment on column crm_t_activity_apply.c_dealer is '是否经销商';
comment on column crm_t_activity_apply.c_activity_type is '活动类型';
comment on column crm_t_activity_apply.c_industry_type is '行业类型';
comment on column crm_t_activity_apply.c_activity_purpose is '活动目的';
comment on column crm_t_activity_apply.c_status is '状态';
comment on column crm_t_activity_apply.c_currency is '货币';
comment on column crm_t_activity_apply.c_estimated_expense is '预计费用';
comment on column crm_t_activity_apply.c_receivable_expense is '应收费用';
comment on column crm_t_activity_apply.c_apply_date is '申请日期';
comment on column crm_t_activity_apply.c_estimated_start_date is '预计开始日期';
comment on column crm_t_activity_apply.c_close_date is '关闭日期';
comment on column crm_t_activity_apply.c_organization is '组织';
comment on column crm_t_activity_apply.c_applier is '申请人';
comment on column crm_t_activity_apply.c_applier_phone is '申请人电话';
comment on column crm_t_activity_apply.c_explain is '说明';
  
drop table crm_t_activity_content; 
create table crm_t_activity_content
(
  c_pid                         	VARCHAR2(32) not null,
  c_apply_code                		VARCHAR2(32),
  c_train_project                	VARCHAR2(300),
  c_estimated_activity_date         DATE,
  c_actual_activity_date          	DATE,
  c_activity_place              	VARCHAR2(300),
  c_content_desc              		VARCHAR2(300),
  c_responsible_person          	VARCHAR2(32),
  c_maker                   		VARCHAR2(32),
  c_status                  		VARCHAR2(32),
  c_make_explain              		VARCHAR2(300),
  CONSTRAINT "pk_crm_t_activity_content" PRIMARY KEY ("C_PID")
);

comment on table crm_t_activity_content is '活动主要内容表';
comment on column crm_t_activity_content.c_pid is 'ID';
comment on column crm_t_activity_content.c_apply_code is '申请单号';
comment on column crm_t_activity_content.c_train_project is '培训项目';
comment on column crm_t_activity_content.c_estimated_activity_date is '预计活动时间';
comment on column crm_t_activity_content.c_actual_activity_date is '实际活动时间';
comment on column crm_t_activity_content.c_activity_place is '活动地点';
comment on column crm_t_activity_content.c_content_desc is '内容描述';
comment on column crm_t_activity_content.c_responsible_person is '负责人员 ';
comment on column crm_t_activity_content.c_maker is '执行人员';
comment on column crm_t_activity_content.c_status is '状态';
comment on column crm_t_activity_content.c_make_explain is '执行说明';
  
drop table crm_t_activity_person;   
create table crm_t_activity_person
(
  c_pid                       	VARCHAR2(32) not null,
  c_apply_code                	VARCHAR2(32),
  c_internal_person           	VARCHAR2(32),
  c_seller_name               	VARCHAR2(300),
  c_department                	VARCHAR2(300),
  c_person_name               	VARCHAR2(32),
  c_position              		VARCHAR2(32),
  c_phone                   	VARCHAR2(32),
  c_email                   	VARCHAR2(32),
  c_power               		VARCHAR2(32),
  c_start_date                	DATE,
  c_end_date                  	DATE,
  c_explain                 	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_activity_person" PRIMARY KEY ("C_PID")
);

comment on table crm_t_activity_person is '参加活动人员表';
comment on column crm_t_activity_person.c_pid is 'ID';
comment on column crm_t_activity_person.c_apply_code is '申请单号';
comment on column crm_t_activity_person.c_internal_person is '是否内部人员 ';
comment on column crm_t_activity_person.c_seller_name is '经销商名称';
comment on column crm_t_activity_person.c_department is '部门';
comment on column crm_t_activity_person.c_person_name is '人员名称';
comment on column crm_t_activity_person.c_position is '职务';
comment on column crm_t_activity_person.c_phone is '联系电话';
comment on column crm_t_activity_person.c_email is 'email';
comment on column crm_t_activity_person.c_power is '权限';
comment on column crm_t_activity_person.c_start_date is '生效日期 ';
comment on column crm_t_activity_person.c_end_date is '失效日期 ';
comment on column crm_t_activity_person.c_explain is '说明';

drop table crm_t_activity_expense;   
create table crm_t_activity_expense
(
  c_pid                         VARCHAR2(32) not null,
  c_apply_code                	VARCHAR2(32),
  c_expense_project             VARCHAR2(300),
  c_estimated_expense         	NUMBER(16),
  c_actual_expense          	NUMBER(16),
  c_organizer               	VARCHAR2(300),
  c_charge_expense              VARCHAR2(32),
  c_explain                 	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_activity_expense" PRIMARY KEY ("C_PID")
);

comment on table crm_t_activity_expense is '活动费用表';
comment on column crm_t_activity_expense.c_pid is 'ID';
comment on column crm_t_activity_expense.c_apply_code is '申请单号';
comment on column crm_t_activity_expense.c_expense_project is '费用项目';
comment on column crm_t_activity_expense.c_estimated_expense is '预算费用';
comment on column crm_t_activity_expense.c_actual_expense is '实际费用';
comment on column crm_t_activity_expense.c_organizer is '承办单位';
comment on column crm_t_activity_expense.c_charge_expense is '是否需收取';
comment on column crm_t_activity_expense.c_explain is '说明';

drop table crm_t_activity_summary;  
create table crm_t_activity_summary
(
  c_pid                   			VARCHAR2(32) not null,
  c_apply_code          			VARCHAR2(32),
  c_start_date                		DATE,
  c_end_date                  		DATE,
  c_result_assess         			VARCHAR2(32),
  c_result_explain         			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_activity_summary" PRIMARY KEY ("C_PID")
);

comment on table crm_t_activity_summary is '活动总结表';
comment on column crm_t_activity_summary.c_pid is 'ID';
comment on column crm_t_activity_summary.c_apply_code is '申请单号';
comment on column crm_t_activity_summary.c_start_date is '活动开始时间';
comment on column crm_t_activity_summary.c_end_date is '活动结束时间';
comment on column crm_t_activity_summary.c_result_assess is '活动效果评估';
comment on column crm_t_activity_summary.c_result_explain is '活动效果说明';

drop table crm_t_channel_attachment;   
create table crm_t_channel_attachment
(
  c_pid                         VARCHAR2(32) not null,
  c_file_name                 	VARCHAR2(300),
  c_file_path                   VARCHAR2(300),
  c_explain                     VARCHAR2(300),
  c_upload_date                 DATE,
  c_upload_person               VARCHAR2(32),
  c_business_code               VARCHAR2(32),
  c_business_type               VARCHAR2(32),
  CONSTRAINT "pk_crm_t_channel_attachment" PRIMARY KEY ("C_PID")
);

comment on table crm_t_channel_attachment is '渠道附件表';
comment on column crm_t_channel_attachment.c_pid is 'ID';
comment on column crm_t_channel_attachment.c_file_name is '文件名称';
comment on column crm_t_channel_attachment.c_file_path is '文件保存路径';
comment on column crm_t_channel_attachment.c_explain is '说明';
comment on column crm_t_channel_attachment.c_upload_date is '上传时间';
comment on column crm_t_channel_attachment.c_upload_person is '上传人';
comment on column crm_t_channel_attachment.c_business_code is '业务编号';
comment on column crm_t_channel_attachment.c_business_type is '业务类型';

drop table crm_t_order_forecast;   
create table crm_t_order_forecast
(
  c_pid                   			VARCHAR2(32) not null,
  c_forecast_code          			VARCHAR2(32),
  c_forecast_unit                	VARCHAR2(300),
  c_forecast_week                	VARCHAR2(32),
  c_status                			VARCHAR2(32),
  c_forecast_date                  	DATE,
  c_applier         				VARCHAR2(32),
  c_applier_phone         			VARCHAR2(32),
  c_explain         				VARCHAR2(300),
  CONSTRAINT "pk_crm_t_order_forecast" PRIMARY KEY ("C_PID")
);

comment on table crm_t_order_forecast is '下单预测表';
comment on column crm_t_order_forecast.c_pid is 'ID';
comment on column crm_t_order_forecast.c_forecast_code is '预测单号';
comment on column crm_t_order_forecast.c_forecast_unit is '预测单位 ';
comment on column crm_t_order_forecast.c_forecast_week is '预测起始周次';
comment on column crm_t_order_forecast.c_status is '状态';
comment on column crm_t_order_forecast.c_forecast_date is '预测日期';
comment on column crm_t_order_forecast.c_applier is '提交人';
comment on column crm_t_order_forecast.c_applier_phone is '提交人电话 ';
comment on column crm_t_order_forecast.c_explain is '说明';

drop table crm_t_order_forecast_detail;     
create table crm_t_order_forecast_detail
(
  c_pid                   			VARCHAR2(32) not null,
  c_forecast_code          			VARCHAR2(32),
  c_product_series                	VARCHAR2(300),
  c_product_kind                	VARCHAR2(300),
  c_materiel_code                	VARCHAR2(32),
  c_customer                  		VARCHAR2(300),
  c_first_week_quantity         	NUMBER(16),
  c_second_week_quantity         	NUMBER(16),
  c_third_week_quantity         	NUMBER(16),
  c_fourth_week_quantity         	NUMBER(16),
  CONSTRAINT "pk_crm_t_order_forecast_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_order_forecast_detail is '下单预测明细表';
comment on column crm_t_order_forecast_detail.c_pid is 'ID';
comment on column crm_t_order_forecast_detail.c_forecast_code is '预测单号';
comment on column crm_t_order_forecast_detail.c_product_series is '产品系列 ';
comment on column crm_t_order_forecast_detail.c_product_kind is '产品型号';
comment on column crm_t_order_forecast_detail.c_materiel_code is '物料号';
comment on column crm_t_order_forecast_detail.c_customer is '客户';
comment on column crm_t_order_forecast_detail.c_first_week_quantity is '第一周数量';
comment on column crm_t_order_forecast_detail.c_second_week_quantity is '第二周数量 ';
comment on column crm_t_order_forecast_detail.c_third_week_quantity is '第三周数量';
comment on column crm_t_order_forecast_detail.c_fourth_week_quantity is '第四周数量';
 
drop table crm_t_delivery_forecast_detail; 
create table crm_t_delivery_forecast_detail
(
  c_pid                   			VARCHAR2(32) not null,
  c_order_detail_id          		VARCHAR2(32),
  c_delivery_date                	DATE,
  c_delivery_quantity         		NUMBER(16),
  CONSTRAINT "pk_crm_t_delivery_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_delivery_forecast_detail is '交货预测明细表';
comment on column crm_t_delivery_forecast_detail.c_pid is 'ID';
comment on column crm_t_delivery_forecast_detail.c_order_detail_id is '下单预测明细id';
comment on column crm_t_delivery_forecast_detail.c_delivery_date is '交货日期 ';
comment on column crm_t_delivery_forecast_detail.c_delivery_quantity is '交货数量';

drop table crm_t_import_sale_apply;   
create table crm_t_import_sale_apply
(
  c_pid                   		VARCHAR2(32) not null,
  c_apply_code          		VARCHAR2(32),
  c_apply_unit                	VARCHAR2(300),
  c_apply_type                  VARCHAR2(32),
  c_import_unit          		VARCHAR2(32),
  c_import_ratio                NUMBER(16,2),
  c_export_unit                 VARCHAR2(32),
  c_export_ratio          		NUMBER(16,2),
  c_status                		VARCHAR2(32),
  c_currency         			VARCHAR2(32),
  c_apply_amount				NUMBER(16,2),
  c_apply_date					DATE,
  c_organization				VARCHAR2(300),
  c_applier						VARCHAR2(32),
  c_applier_phone				VARCHAR2(32),
  c_explain						VARCHAR2(300),
  CONSTRAINT "pk_crm_t_import_sale_apply" PRIMARY KEY ("C_PID")
);

comment on table crm_t_import_sale_apply is '引入销量申请表';
comment on column crm_t_import_sale_apply.c_pid is 'ID';
comment on column crm_t_import_sale_apply.c_apply_code is '申请单号';
comment on column crm_t_import_sale_apply.c_apply_unit is '申请单位';
comment on column crm_t_import_sale_apply.c_apply_type is '申请类型';
comment on column crm_t_import_sale_apply.c_import_unit is '销量转入单位';
comment on column crm_t_import_sale_apply.c_import_ratio is '销量转入比例';
comment on column crm_t_import_sale_apply.c_export_unit is '交货日期 ';
comment on column crm_t_import_sale_apply.c_export_ratio is '销量转出单位';
comment on column crm_t_import_sale_apply.c_status is '状态';
comment on column crm_t_import_sale_apply.c_currency is '货币';
comment on column crm_t_import_sale_apply.c_apply_amount is '申请金额';
comment on column crm_t_import_sale_apply.c_apply_date is '申请日期';
comment on column crm_t_import_sale_apply.c_organization is '组织';
comment on column crm_t_import_sale_apply.c_applier is '申请人';
comment on column crm_t_import_sale_apply.c_applier_phone is '申请人电话';
comment on column crm_t_import_sale_apply.c_explain is '说明';

drop table crm_t_import_sale_apply_detail;     
create table crm_t_import_sale_apply_detail
(
  c_pid                   		VARCHAR2(32) not null,
  c_apply_code          		VARCHAR2(32),
  c_purchase_code               VARCHAR2(32),
  c_product_series              VARCHAR2(300),
  c_product_kind          		VARCHAR2(300),
  c_materiel_code               VARCHAR2(32),
  c_import_date                 DATE,
  c_import_quantity          	NUMBER(16),
  c_import_price                NUMBER(16,2),
  c_import_amount               NUMBER(16,2),
  CONSTRAINT "pk_crm_t_import_apply_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_import_sale_apply_detail is '引入销量明细表';
comment on column crm_t_import_sale_apply_detail.c_pid is 'ID';
comment on column crm_t_import_sale_apply_detail.c_apply_code is '申请单号';
comment on column crm_t_import_sale_apply_detail.c_purchase_code is '采购单号';
comment on column crm_t_import_sale_apply_detail.c_product_series is '产品系列';
comment on column crm_t_import_sale_apply_detail.c_product_kind is '产品型号';
comment on column crm_t_import_sale_apply_detail.c_materiel_code is '物料号';
comment on column crm_t_import_sale_apply_detail.c_import_date is '引入日期 ';
comment on column crm_t_import_sale_apply_detail.c_import_quantity is '引入数量';
comment on column crm_t_import_sale_apply_detail.c_import_price is '单价';
comment on column crm_t_import_sale_apply_detail.c_import_amount is '金额';

drop table crm_t_service_apply;    
create table crm_t_service_apply
(
  c_pid                   		VARCHAR2(32) not null,
  c_apply_code          		VARCHAR2(32),
  c_apply_unit               	VARCHAR2(300),
  c_service_type              	VARCHAR2(32),
  c_dealer          			VARCHAR2(32),
  c_status               		VARCHAR2(32),
  c_currency             		VARCHAR2(32),
  c_service_expense          	NUMBER(16,2),
  c_apply_date                	DATE,
  c_demand_finish_date          DATE,
  c_organization               	VARCHAR2(300),
  c_applier              		VARCHAR2(32),
  c_applier_phone          		VARCHAR2(32),
  c_explain               		VARCHAR2(300),
  c_busi_chance_code            VARCHAR2(32),
  c_busi_chance_name          	VARCHAR2(300),
  c_custom_place               	VARCHAR2(300),
  c_contact_address             VARCHAR2(300),
  c_contact_code          		VARCHAR2(32),
  c_end_custom_name             VARCHAR2(300),
  c_custom_contact              VARCHAR2(32),
  c_contact_phone               VARCHAR2(32),
  c_service_content            	VARCHAR2(300),
  CONSTRAINT "pk_crm_t_service_apply" PRIMARY KEY ("C_PID")
);

comment on table crm_t_service_apply is '服务申请表';
comment on column crm_t_service_apply.c_pid is 'ID';
comment on column crm_t_service_apply.c_apply_code is '服务单号';
comment on column crm_t_service_apply.c_apply_unit is '申请单位';
comment on column crm_t_service_apply.c_service_type is '服务类型';
comment on column crm_t_service_apply.c_dealer is '是否经销商';
comment on column crm_t_service_apply.c_status is '状态';
comment on column crm_t_service_apply.c_currency is '货币 ';
comment on column crm_t_service_apply.c_service_expense is '服务费用';
comment on column crm_t_service_apply.c_apply_date is '申请日期';
comment on column crm_t_service_apply.c_demand_finish_date is '需求完成日期';
comment on column crm_t_service_apply.c_organization is '组织';
comment on column crm_t_service_apply.c_applier is '申请人';
comment on column crm_t_service_apply.c_applier_phone is '申请人电话';
comment on column crm_t_service_apply.c_explain is '说明';
comment on column crm_t_service_apply.c_busi_chance_code is '商机编号';
comment on column crm_t_service_apply.c_busi_chance_name is '商机名称 ';
comment on column crm_t_service_apply.c_custom_place is '客户所在地';
comment on column crm_t_service_apply.c_contact_address is '联系地址';
comment on column crm_t_service_apply.c_contact_code is '合同编号';
comment on column crm_t_service_apply.c_end_custom_name is '终端客户名称';
comment on column crm_t_service_apply.c_custom_contact is '客户联系人';
comment on column crm_t_service_apply.c_contact_phone is '客户联系人电话 ';
comment on column crm_t_service_apply.c_service_content is '服务内容';

drop table crm_t_apply_equipment;    
create table crm_t_apply_equipment
(
  c_pid                   		VARCHAR2(32) not null,
  c_apply_code          		VARCHAR2(32),
  c_equip_model               	VARCHAR2(32),
  c_product_line              	VARCHAR2(32),
  c_brand          				VARCHAR2(300),
  c_equip_series               	VARCHAR2(32),
  c_equip_materiel             	VARCHAR2(32),
  c_service_price          		NUMBER(16,2),
  c_service_quantity           	NUMBER(16),
  c_service_money           	NUMBER(16,2),
  c_remark               		VARCHAR2(300),
  CONSTRAINT "pk_crm_t_apply_equipment" PRIMARY KEY ("C_PID")
);

comment on table crm_t_apply_equipment is '服务申请设备表';
comment on column crm_t_apply_equipment.c_pid is 'ID';
comment on column crm_t_apply_equipment.c_apply_code is '服务单号';
comment on column crm_t_apply_equipment.c_equip_model is '设备型号';
comment on column crm_t_apply_equipment.c_product_line is '产品线';
comment on column crm_t_apply_equipment.c_brand is '品牌';
comment on column crm_t_apply_equipment.c_equip_series is '设备系列号';
comment on column crm_t_apply_equipment.c_equip_materiel is '配件物料号 ';
comment on column crm_t_apply_equipment.c_service_price is '单价';
comment on column crm_t_apply_equipment.c_service_quantity is '数量';
comment on column crm_t_apply_equipment.c_service_money is '金额';
comment on column crm_t_apply_equipment.c_remark is '备注';

drop table crm_t_rebate_policy; 
create table crm_t_rebate_policy
(
  c_pid                   		VARCHAR2(32) not null,
  c_policy_code          		VARCHAR2(32),
  c_policy_name               	VARCHAR2(32),
  c_custom_name              	VARCHAR2(300),
  c_contact          			VARCHAR2(300),
  c_contact_phone               VARCHAR2(32),
  c_rebate_type             	VARCHAR2(32),
  c_status          			VARCHAR2(32),
  c_currency           			VARCHAR2(32),
  c_start_date          		DATE,
  c_end_date               		DATE,
  c_organization              	VARCHAR2(300),
  c_agree_code          		VARCHAR2(32),
  c_contract_code               VARCHAR2(32),
  c_salesman             		VARCHAR2(32),
  c_authority_zone          	VARCHAR2(300),
  c_authority_product           VARCHAR2(300),
  c_agent_level          		VARCHAR2(32),
  c_annual_task               	NUMBER(16),
  c_reference_type              VARCHAR2(32),
  c_reference_Date          	VARCHAR2(32),
  c_rebate_mode               	VARCHAR2(32),
  c_account_mode             	VARCHAR2(32),
  c_eliminate_special          	VARCHAR2(32),
  c_eliminate_non_standard      VARCHAR2(32),
  c_explain               		VARCHAR2(300),
  CONSTRAINT "pk_crm_t_rebate_policy" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_policy is '返利政策表';
comment on column crm_t_rebate_policy.c_pid is 'ID';
comment on column crm_t_rebate_policy.c_policy_code is '政策编号';
comment on column crm_t_rebate_policy.c_policy_name is '政策名称';
comment on column crm_t_rebate_policy.c_custom_name is '客户名称';
comment on column crm_t_rebate_policy.c_contact is '联系人';
comment on column crm_t_rebate_policy.c_contact_phone is '联系电话';
comment on column crm_t_rebate_policy.c_rebate_type is '返利类型 ';
comment on column crm_t_rebate_policy.c_status is '状态';
comment on column crm_t_rebate_policy.c_currency is '货币';
comment on column crm_t_rebate_policy.c_start_date is '起始日期';
comment on column crm_t_rebate_policy.c_end_date is '结束日期';
comment on column crm_t_rebate_policy.c_organization is '组织';
comment on column crm_t_rebate_policy.c_agree_code is '协议编号';
comment on column crm_t_rebate_policy.c_contract_code is '合同编号';
comment on column crm_t_rebate_policy.c_salesman is '销售员';
comment on column crm_t_rebate_policy.c_authority_zone is '授权区域';
comment on column crm_t_rebate_policy.c_authority_product is '授权产品';
comment on column crm_t_rebate_policy.c_agent_level is '代理商级别';
comment on column crm_t_rebate_policy.c_annual_task is '年任务量';
comment on column crm_t_rebate_policy.c_reference_type is '基准类型';
comment on column crm_t_rebate_policy.c_reference_Date is '基准日期';
comment on column crm_t_rebate_policy.c_rebate_mode is '返利方式';
comment on column crm_t_rebate_policy.c_account_mode is '账期方式';
comment on column crm_t_rebate_policy.c_eliminate_special is '剔除特价销售';
comment on column crm_t_rebate_policy.c_eliminate_non_standard is '剔除非标品销售';
comment on column crm_t_rebate_policy.c_explain is '说明';

drop table crm_t_rebate_clause; 
create table crm_t_rebate_clause
(
  c_pid                   		VARCHAR2(32) not null,
  c_policy_code          		VARCHAR2(32),
  c_product_group               VARCHAR2(32),
  c_reference_quantity          NUMBER(16),
  c_least_return_rate          	NUMBER(16,2),
  c_finish_rate               	NUMBER(16,2),
  c_rebate_rate             	NUMBER(16,2),
  c_finish_rate2          		NUMBER(16,2),
  c_rebate_rate2           		NUMBER(16,2),
  c_rebate_retio          		NUMBER(16,2),
  c_year_no_growth              NUMBER(16,2),
  c_excess_rebate              	NUMBER(16,2),
  c_remark          			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_rebate_clause" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_clause is '返利条款表';
comment on column crm_t_rebate_clause.c_pid is 'ID';
comment on column crm_t_rebate_clause.c_policy_code is '政策编号';
comment on column crm_t_rebate_clause.c_product_group is '产品组';
comment on column crm_t_rebate_clause.c_reference_quantity is '基准量';
comment on column crm_t_rebate_clause.c_least_return_rate is '至少回款率';
comment on column crm_t_rebate_clause.c_finish_rate is '完成率';
comment on column crm_t_rebate_clause.c_rebate_rate is '返利比例（%） ';
comment on column crm_t_rebate_clause.c_finish_rate2 is '完成率2';
comment on column crm_t_rebate_clause.c_rebate_rate2 is '返利比例2（%）';
comment on column crm_t_rebate_clause.c_rebate_retio is '返利系数（%）';
comment on column crm_t_rebate_clause.c_year_no_growth is '同比增长率（%）';
comment on column crm_t_rebate_clause.c_excess_rebate is '超额返利 （%）';
comment on column crm_t_rebate_clause.c_remark is '备注';

drop table crm_t_rebate_product; 
create table crm_t_rebate_product
(
  c_pid                   		VARCHAR2(32) not null,
  c_group_name          		VARCHAR2(300),
  c_organization               	VARCHAR2(300),
  c_start_date          		DATE,
  c_end_date          			DATE,
  c_create_date               	DATE,
  c_creater             		VARCHAR2(32),
  c_explain          			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_rebate_product" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_product is '返利产品组表';
comment on column crm_t_rebate_product.c_pid is 'ID';
comment on column crm_t_rebate_product.c_group_name is '产品组名称';
comment on column crm_t_rebate_product.c_organization is '组织';
comment on column crm_t_rebate_product.c_start_date is '生效日期';
comment on column crm_t_rebate_product.c_end_date is '失效日期';
comment on column crm_t_rebate_product.c_create_date is '创建日期';
comment on column crm_t_rebate_product.c_creater is '创建人';
comment on column crm_t_rebate_product.c_explain is '说明';

drop table crm_t_rebate_product_detail;
create table crm_t_rebate_product_detail
(
  c_pid                   		VARCHAR2(32) not null,
  c_product_group_id          	VARCHAR2(32),
  c_product_series              VARCHAR2(300),
  c_endDate          			DATE,
  CONSTRAINT "pk_crm_t_rebate_product_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_product_detail is '返利产品组明细表';
comment on column crm_t_rebate_product_detail.c_pid is 'ID';
comment on column crm_t_rebate_product_detail.c_product_group_id is '产品组id';
comment on column crm_t_rebate_product_detail.c_product_series is '产品系列';
comment on column crm_t_rebate_product_detail.c_endDate is '失效日期';

drop table crm_t_rebate_detail;
create table crm_t_rebate_detail
(
  c_pid                   		VARCHAR2(32) not null,
  c_policy_code          		VARCHAR2(32),
  c_accrued_money             	NUMBER(16,2),
  c_check_money          		NUMBER(16,2),
  c_difference_reason           VARCHAR2(300),
  c_returned_money          	NUMBER(16,2),
  c_non_return_money            NUMBER(16,2),
  c_status          			VARCHAR2(32),
  c_create_date              	DATE,
  c_creater          			VARCHAR2(32),
  c_sales_order              	VARCHAR2(300),
  c_product_code          		VARCHAR2(32),
  c_send_quantity				NUMBER(16),
  c_send_date              		DATE,
  c_order_money          		NUMBER(16,2),
  CONSTRAINT "pk_crm_t_rebate_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_detail is '返利明细表';
comment on column crm_t_rebate_detail.c_pid is 'ID';
comment on column crm_t_rebate_detail.c_policy_code is '政策编号';
comment on column crm_t_rebate_detail.c_accrued_money is '应计金额';
comment on column crm_t_rebate_detail.c_check_money is '确认金额';
comment on column crm_t_rebate_detail.c_difference_reason is '金额差异原因';
comment on column crm_t_rebate_detail.c_returned_money is '已返金额';
comment on column crm_t_rebate_detail.c_non_return_money is '未返金额';
comment on column crm_t_rebate_detail.c_status is '状态';
comment on column crm_t_rebate_detail.c_create_date is '创建日期';
comment on column crm_t_rebate_detail.c_creater is '创建人';
comment on column crm_t_rebate_detail.c_sales_order is '销售订单';
comment on column crm_t_rebate_detail.c_product_code is '产品编号';
comment on column crm_t_rebate_detail.c_send_quantity is '发货数量';
comment on column crm_t_rebate_detail.c_send_date is '发货日期';
comment on column crm_t_rebate_detail.c_order_money is '订单金额';

drop table crm_t_deduction_detail;
create table crm_t_deduction_detail
(
  c_pid                   		VARCHAR2(32) not null,
  c_rebate_detail_id          	VARCHAR2(32),
  c_deduction_code             	NUMBER(16,2),
  c_deduction_date          	DATE,
  c_deduction_money           	NUMBER(16,2),
  c_bill_apply_code          	VARCHAR2(32),
  c_bill_apply_date            	DATE,
  CONSTRAINT "pk_crm_t_deduction_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_deduction_detail is '返利明细表';
comment on column crm_t_deduction_detail.c_pid is 'ID';
comment on column crm_t_deduction_detail.c_rebate_detail_id is '返利明细id';
comment on column crm_t_deduction_detail.c_deduction_code is '抵扣单号';
comment on column crm_t_deduction_detail.c_deduction_date is '抵扣日期';
comment on column crm_t_deduction_detail.c_deduction_money is '抵扣金额 ';
comment on column crm_t_deduction_detail.c_bill_apply_code is '开票申请单号';
comment on column crm_t_deduction_detail.c_bill_apply_date is '开票申请日期';

drop table crm_t_rebate_settle;
create table crm_t_rebate_settle
(
  c_pid                   		VARCHAR2(32) not null,
  c_deduction_code          	VARCHAR2(32),
  c_status             			VARCHAR2(32),
  c_create_date          		DATE,
  c_creater           			VARCHAR2(32),
  c_explain          			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_rebate_settle" PRIMARY KEY ("C_PID")
);

comment on table crm_t_rebate_settle is '返利结算表';
comment on column crm_t_rebate_settle.c_pid is 'ID';
comment on column crm_t_rebate_settle.c_deduction_code is '抵扣单号';
comment on column crm_t_rebate_settle.c_status is '状态';
comment on column crm_t_rebate_settle.c_create_date is '创建日期';
comment on column crm_t_rebate_settle.c_creater is '创建人 ';
comment on column crm_t_rebate_settle.c_explain is '说明';

drop table crm_t_settle_detail;
create table crm_t_settle_detail
(
  c_pid                   		VARCHAR2(32) not null,
  c_policy_code          		VARCHAR2(32),
  c_status             			VARCHAR2(32),
  c_create_date          		DATE,
  c_creater           			VARCHAR2(32),
  c_explain          			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_settle_detail" PRIMARY KEY ("C_PID")
);

comment on table crm_t_settle_detail is '返利结算明细表';
comment on column crm_t_settle_detail.c_pid is 'ID';
comment on column crm_t_settle_detail.c_policy_code is '政策编号';
comment on column crm_t_settle_detail.c_status is '状态';
comment on column crm_t_settle_detail.c_create_date is '创建日期';
comment on column crm_t_settle_detail.c_creater is '创建人 ';
comment on column crm_t_settle_detail.c_explain is '说明';

drop table crm_t_settle_detail_choose;
create table crm_t_settle_detail_choose
(
  c_pid                   		VARCHAR2(32) not null,
  c_policy_code          		VARCHAR2(32),
  c_status             			VARCHAR2(32),
  c_create_date          		DATE,
  c_creater           			VARCHAR2(32),
  c_explain          			VARCHAR2(300),
  CONSTRAINT "pk_crm_t_settle_detail_choose" PRIMARY KEY ("C_PID")
);

comment on table crm_t_settle_detail_choose is '返利结算明细挑选表';
comment on column crm_t_settle_detail_choose.c_pid is 'ID';
comment on column crm_t_settle_detail_choose.c_policy_code is '政策编号';
comment on column crm_t_settle_detail_choose.c_status is '状态';
comment on column crm_t_settle_detail_choose.c_create_date is '创建日期';
comment on column crm_t_settle_detail_choose.c_creater is '创建人 ';
comment on column crm_t_settle_detail_choose.c_explain is '说明';

drop table crm_t_serial_number;
create table crm_t_serial_number
(
  c_pid                   		VARCHAR2(32) not null,
  c_module          			VARCHAR2(32),
  c_date             			VARCHAR2(32),
  c_code          				VARCHAR2(32),
  c_serial           			NUMBER(16),
  CONSTRAINT "pk_crm_t_serial_number" PRIMARY KEY ("C_PID")
);

comment on table crm_t_serial_number is '流水记录表';
comment on column crm_t_serial_number.c_pid is 'ID';
comment on column crm_t_serial_number.c_module is '流水模块';
comment on column crm_t_serial_number.c_date is '组成日期';
comment on column crm_t_serial_number.c_code is '组成编码';
comment on column crm_t_serial_number.c_serial is '流水号';



