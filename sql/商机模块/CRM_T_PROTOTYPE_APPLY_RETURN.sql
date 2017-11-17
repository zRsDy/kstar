
  CREATE TABLE "CRM_T_PROTOTYPE_APPLY_RETURN" 
   ("C_PID" VARCHAR2(32) NOT NULL ENABLE,
	"C_APPLICANT" VARCHAR2(30 BYTE), 
	"C_DEPARTMENT" VARCHAR2(30 BYTE), 
	"C_NUMBER" VARCHAR2(30 BYTE), 
	"DT_PLAN_RECIEVE_TIME" DATE, 
	"DT_PLAN_RETURN_TIME" DATE, 
	"DT_CREATE_DATE" DATE, 
	"C_CLIENT_ID" VARCHAR2(32 BYTE), 
	"C_CLIENT_NAME" VARCHAR2(200 BYTE), 
	"C_BIZOPP_ID" VARCHAR2(32 BYTE), 
	"C_BIZOPP_NAME" VARCHAR2(200 BYTE), 
	"C_STATUS" VARCHAR2(10 BYTE), 
	"C_PROTOTYPE_TYPE_DISC" VARCHAR2(200 BYTE), 
	"C_IS_FOR_FREE" VARCHAR2(32 BYTE),
	"C_FREE_REASON" VARCHAR2(1000 BYTE), 
	"C_REMARK" VARCHAR2(1000 BYTE), 
	"C_PROTOTYPE_PRICE" VARCHAR2(32 BYTE), 
	"C_SALE_METHOD" VARCHAR2(32 BYTE), 
	"C_ORDER_NUMBER" VARCHAR2(30 BYTE), 
	"C_GET_CONFIRM" VARCHAR2(32 BYTE),
	"C_GET_REMARK" VARCHAR2(1000 BYTE), 
	"C_RECIEVER_ADDRESS" VARCHAR2(80 BYTE), 
	"C_RECIEVER" VARCHAR2(10 BYTE), 
	"C_RECIEVER_PHONE" VARCHAR2(20 BYTE), 
	"C_APPLICANT_R_CONFIRM" VARCHAR2(32 BYTE), 
	"C_PROTOTYPE_CONDITION" VARCHAR2(200 BYTE), 
	"C_RECIEVER_CONFIRM" VARCHAR2(32 BYTE),  
	"C_PROTOTYPE_STATUS" VARCHAR2(10 BYTE), 
	"C_RECIEVED_CONDITION" VARCHAR2(200 BYTE), 
	"C_APPLY_UNIT" VARCHAR2(30), 
	 CONSTRAINT "CRM_T_PROTOTYPE_A_R_ID" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ;
 

   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_PID" IS 'primary key';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_APPLICANT" IS '申请人';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_DEPARTMENT" IS '部门';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_NUMBER" IS '编号';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."DT_PLAN_RECIEVE_TIME" IS '期望到货时间';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."DT_PLAN_RETURN_TIME" IS '预计归还时间';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."DT_CREATE_DATE" IS '创建日期';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_CLIENT_ID" IS '客户ID';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_CLIENT_NAME" IS '客户名称';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_BIZOPP_ID" IS '商机名称';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_BIZOPP_NAME" IS '客户ID';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_STATUS" IS '状态';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_PROTOTYPE_TYPE_DISC" IS '样机型号及描述';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_IS_FOR_FREE" IS '是否免费赠送';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_FREE_REASON" IS '免费赠送原因';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_REMARK" IS '备注';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_PROTOTYPE_PRICE" IS '样机销售报价';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_SALE_METHOD" IS '销售方式';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_ORDER_NUMBER" IS '订单编号';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_GET_CONFIRM" IS '确认已领取';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_GET_REMARK" IS '样机领取备注';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_RECIEVER_ADDRESS" IS '收货人地址';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_RECIEVER" IS '收货人';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_RECIEVER_PHONE" IS '收货电话';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_APPLICANT_R_CONFIRM" IS '申请人确认退还接收';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_PROTOTYPE_CONDITION" IS '样机状况备注';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_RECIEVER_CONFIRM" IS '接收人确认退还接收';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_PROTOTYPE_STATUS" IS '样机状态';
 
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_RECIEVED_CONDITION" IS '样机接收备注';
 
   COMMENT ON TABLE "CRM_T_PROTOTYPE_APPLY_RETURN"  IS '样机申请与退还表';
   
   COMMENT ON COLUMN "CRM_T_PROTOTYPE_APPLY_RETURN"."C_APPLY_UNIT" IS '申请单位';
   
   
 
