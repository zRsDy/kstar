create table crm_t_batch_product_price
(
  c_pid                   				VARCHAR2(32) not null,
  c_price_head_id                   	VARCHAR2(32),
  c_price_table_name          			VARCHAR2(300),
  c_organization             			VARCHAR2(300),
  c_product_type          				VARCHAR2(32),
  c_product_sort_id          			VARCHAR2(32),
  c_adjust_type             			VARCHAR2(32),
  c_adjust_value          				NUMBER(16,2),
  c_adjust_status        				VARCHAR2(32),
  c_adjust_date             			DATE
);

comment on table crm_t_batch_product_price is '产品批量调价表';
comment on column crm_t_batch_product_price.c_pid is 'ID';
comment on column crm_t_batch_product_price.c_price_head_id is '价格头表ID';
comment on column crm_t_batch_product_price.c_price_table_name is '价格表名称';
comment on column crm_t_batch_product_price.c_organization is '组织';
comment on column crm_t_batch_product_price.c_product_type is '产品类型';
comment on column crm_t_batch_product_price.c_product_sort_id is '产品分类id';
comment on column crm_t_batch_product_price.c_adjust_type is '调整类型';
comment on column crm_t_batch_product_price.c_adjust_value is '调整值';
comment on column crm_t_batch_product_price.c_adjust_status is '调整状态';
comment on column crm_t_batch_product_price.c_adjust_date is '调整日期';


