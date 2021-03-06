CREATE TABLE "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK" 
   (	"C_PID" VARCHAR2(32) NOT NULL ENABLE, 
	"C_SUPPORT_STAFF" VARCHAR2(10), 
	"DT_ASSIGN_DATE" DATE, 
	"DT_SUPPORT_START_DATE" DATE, 
	"DT_SUPPORT_END_DATE" DATE, 
	"C_RESULT_DISC" VARCHAR2(80), 
	 CONSTRAINT "SUPPORT_FEEDBACK_PK" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM"  ENABLE
   ) SEGMENT CREATION DEFERRED 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE( INITIAL 65536 MINEXTENTS 1 MAXEXTENTS 2147483645)
  TABLESPACE "TS_CRM" ;
 
  CREATE UNIQUE INDEX "KCRM"."SUPPORT_FEEDBACK_PK" ON "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK" ("C_PID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM" ;
 
  ALTER TABLE "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK" ADD CONSTRAINT "SUPPORT_FEEDBACK_PK" PRIMARY KEY ("C_PID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS NOCOMPRESS LOGGING
  TABLESPACE "TS_CRM"  ENABLE;
 
  ALTER TABLE "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK" MODIFY ("C_PID" NOT NULL ENABLE);
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."C_PID" IS 'primary key';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."C_SUPPORT_STAFF" IS '支持人员';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."DT_ASSIGN_DATE" IS '分配日期';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."DT_SUPPORT_START_DATE" IS '支持日期从';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."DT_SUPPORT_END_DATE" IS '支持日期至';
 
   COMMENT ON COLUMN "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"."C_RESULT_DISC" IS '结果说明';
 
   COMMENT ON TABLE "KCRM"."CRM_T_BIZ_SUPPORT_FEEDBACK"  IS '商机技术支持反馈表';