


-------------------------
-- 报价单基本信息表
-------------------------

create table CRM_T_QUOTATION_BASIC
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
    N_VERSION             number(32, 0)                        NOT NULL,    -- 报价单版本
    C_NAME                varchar2(300)                        NULL,        -- 报价单名称
    C_SALESREP            varchar2(300)                        NULL,        -- 销售代表
    C_SALESREP_DEP        varchar2(300)                        NULL,        -- 销售代表部门
    C_BO_CODE             varchar2(32)                         NULL,        -- 商机编码
    C_BO_NAME             varchar2(300)                        NULL,        -- 商机名称
    C_ORG                 varchar2(300)                        NULL,        -- 组织
    C_CUSTOMER_NAME       varchar2(300)                        NULL,        -- 客户名称
    C_CUSTOMER_CODE       varchar2(300)                        NULL,        -- 客户编号
    C_CONTACTS            varchar2(300)                        NULL,        -- 客户联系人
    C_STATUS              varchar2(300)                        NULL,        -- 状态
    C_CLOSURE_REASON      varchar2(300)                        NULL,        -- 关闭原因
    C_PRO_REVIEW_STATUS   varchar2(300)                        NULL,        -- 工程评审状态
    C_BID_AUDIT_STATUS    varchar2(300)                        NULL,        -- 标书审核状态
    DT_CREATE_DATE        DATE                       		   NULL,        -- 创建日期
    C_CREATOR             varchar2(300)                         NULL,        -- 创建人
    C_PRICE_LIST          varchar2(300)                        NULL,        -- 价格表
    C_CURRENCY            varchar2(32)                           NULL,        -- 货币
    N_AMOUNT              number(16, 2)                        NULL,        -- 总额
    C_BID_RESULTS         varchar2(32)                         NULL,        -- 投标结果
    C_BID_RESULTS_Reason   varchar2(300)                        NULL,        -- 丢标/赢标原因
    N_IS_ProReview        number(1, 0)                         NOT NULL,    -- 是否需工程评审
    N_is_BidPro           number(1, 0)                         NOT NULL,    -- 是否投标项目
    N_BID_NO              varchar2(300)                        NULL,        -- 有效标识
    N_is_valid            number(1, 0)                         NULL,        -- 有效标识
    C_PAY_TYPE            varchar2(300)                           NULL,        -- 付款方式
	C_PRC_ID              varchar2(300)                           NULL,        -- 流程编号
	C_SAL_DEP             varchar2(300)                           NULL,        -- 销售部门
	C_BIDDOC_NO           varchar2(300)                           NULL,        -- 关联标书号
	C_BID_PRJ             varchar2(300)                           NULL,        -- 投标项目
	C_MEMO             	  varchar2(300)                           NULL,        -- 备注
	C_PRICE_LISTID		  varchar2(300)                        NULL,           -- 价格表ID
	C_IF_SUBMITTED		  varchar2(300)                        NULL,           -- 提交报价标志
constraint PK_M_T_QUOTATION_BASIC primary key( C_PID )
)




-------------------------
-- 内部团队成员信息表
-------------------------

create table CRM_T_MEMBER_INF
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 编号
	C_MARK_DEPART 		  varchar2(300) 					   NULL, 		-- 销售部门
	C_MEM_NAME            varchar2(300)                        NULL,        -- 团队成员名称
	C_MARK_RESPON		  varchar2(300)                        NULL,        -- 责任人
	C_MARK_OP_UNT		  varchar2(300)                        NULL,        -- 业务实体
	C_ROLE	              varchar2(300)                        NULL,        -- 团队成员角色
	C_MARK_AUTH			  varchar2(300)                        NULL,        -- 权限
	DT_MARK_VLD_FRM		  DATE                      		   NULL,        -- 有效日期从
	DT_MARK_VLD_TO		  DATE                        		   NULL,        -- 有效日期至
	C_NOTES	              varchar2(300)                        NULL,        -- 备注
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
constraint PK_M_T_MEMBER_INF primary key( C_PID )
)



-------------------------
-- 界面完成信息表
-------------------------

create table CRM_T_PG_INF
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	C_PG_TYP              varchar2(300)                        NULL,        -- 界面类型
	C_REL_PRD	          varchar2(300)                        NULL,        -- 相关产品 集成需求
	C_COMPLETE	          varchar2(300)                        NULL,        -- 表单填写完成度
	C_REQ_NO	          varchar2(32)                         NULL,    -- 支持请求单号
	C_SUPPORTER	          varchar2(32)                         NULL,    -- 支持人
	C_STATUS	          varchar2(32)                         NULL,    -- 受理状态
constraint PK_M_T_PG_INF primary key( C_PID )
)



-------------------------
-- 工程界面的项目基本信息表
-------------------------

create table CRM_T_BASE_INF
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
    C_DRAFT               varchar2(300)                        NULL,        -- 机房图纸
    C_PROJ_NAME           varchar2(300)                        NULL,        -- 项目名称
    N_HEIGHT              number(16, 2)                        NULL,        -- 当地海拔
    C_CNST_LEVEL          varchar2(32)                         NULL,        -- 建设等级
    C_PROJ_ADDR           varchar2(300)                        NULL,        -- 项目地址
    C_PROJ_BUDGET         number(16, 2)                        NULL,        -- 项目预算
    C_RM_TYPE             varchar2(32)                         NULL,        -- 机房性质
    C_INT_PRD_TYPE        varchar2(32)                         NULL,        -- 集成产品
    C_REL_PRD             varchar2(32)                         NULL,        -- 相关产品
    N_RM_LENGTH           number(16, 2)                        NULL,        -- 机房面积的长度
    N_RM_WIDTH            number(16, 2)                        NULL,        -- 机房面积的宽度
    N_RM_LEVEL            number(32, 0)                        NULL,        -- 机房楼层
    C_RM_LEVEL_NOTES      varchar2(300)                        NULL,        -- 机房楼层的说明
    N_RM_LEVEL_HGHT       number(16, 2)                        NULL,        -- 机房梁下净高
    C_RM_LEVEL_HGHT_NTS   varchar2(300)                        NULL,        -- 机房梁下净高的说明
    N_LEVEL_WGHT          number(16, 2)                        NULL,        -- 楼层承重
    C_LEVEL_WGHT_NTS      varchar2(300)                        NULL,        -- 楼层承重的说明
    C_IF_HGH_FLR          varchar2(32)                         NULL,        -- 是否为高架地板
    N_HGH_FLR             number(16, 2)                        NULL,        -- 高架地板长度
    C_HGH_FLR_NTS         varchar2(300)                        NULL,        -- 高架地板的说明
    C_PRJ_OTHER_NTS       varchar2(300)                        NULL,        -- 项目其他情况说明
    C_MH_LAYOUT           varchar2(300)                        NULL,        -- 多通道或机柜功率不均提供设备布局图
    C_LOAD_TYPE           varchar2(32)                         NULL,        -- 负载类型
	C_LOAD_TYP_NTS        varchar2(300)                        NULL,        -- 负载类型说明
    N_SERVER_NUM          number(32, 0)                        NULL,        -- 服务器机柜（若有）
    N_SERVER_PWR          number(16, 2)                        NULL,        -- 单机柜功率
    C_SERVER_SIZE         varchar2(300)                        NULL,        -- 机柜尺寸要求(宽×高×长)
    C_EXTENSION           varchar2(32)                         NULL,        -- 后期扩容要求
    C_LD_OTHER_NTS        varchar2(300)                        NULL,        -- 负载其他情况说明
    C_TECH_REQ_PRHS       varchar2(32)                         NULL,        -- 外购件技术要求
    C_INT_REQ_PRHS        varchar2(32)                         NULL,        -- 外购件安装服务要求
    C_LGST_REQ_PRHS       varchar2(32)                         NULL,        -- 外购件物流要求
    C_TURN_ON             varchar2(32)                         NULL,        -- 只负责开机
    C_TURN_ON_PRD         varchar2(32)                         NULL,        -- 只负责开机的相关产品
    C_INS_MNTR            varchar2(32)                         NULL,        -- 安装督导
    C_INS_MNTR_PRD        varchar2(32)                         NULL,        -- 安装督导的相关产品
    C_INS                 varchar2(32)                         NULL,        -- 安装（不含施工材料）
    C_INS_PRD             varchar2(32)                         NULL,        -- 安装（不含施工材料）的相关产品
    C_KEY                 varchar2(32)                         NULL,        -- 交钥匙工程
    C_KEY_PRD             varchar2(32)                         NULL,        -- 交钥匙工程的相关产品
    DT_DEL_READY          date                                 NULL,        -- 要求到货时间
    C_DEL_ADDR            varchar2(300)                        NULL,        -- 要求详细的到货地点（具体到楼层）
    C_BATCH_DEL           varchar2(300)                        NULL,        -- 分批发货说明（若有）
    C_PCKG_REQ            varchar2(300)                        NULL,        -- 包装标签需求
    C_BSS_OTHER_NTS       varchar2(300)                        NULL,        -- 商务部分的其他特殊需求
    C_ORD_REQ             varchar2(300)                        NULL,        -- 要货申请
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	N_IF_UPS	          number	                           NULL,        -- 相关产品UPS
	N_IF_BATTRY	          number	                           NULL,        -- 相关产品电池
	N_IF_CLR	          number	                           NULL,        -- 相关产品精密空调
	N_IF_ELEC	          number	                           NULL,        -- 相关产品配电
	N_IF_RCK	          number	                           NULL,        -- 相关产品机柜
	N_IF_MNT	          number	                           NULL,        -- 相关产品监控
	N_IF_IDM	          number	                           NULL,        -- 集成产品IDM
	N_IF_IDU	          number	                           NULL,        -- 集成产品IDU
	N_IF_IDR	          number	                           NULL,        -- 集成产品IDR
constraint PK_M_T_BASE_INF primary key( C_PID )
)


-------------------------
-- 工程界面的售后部分
-------------------------

create table CRM_T_AFT_SALE
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
    C_DVC_DRAFT           varchar2(300)                        NULL,        -- 设备布局图
    N_DXPP_LENGTH         number(16, 2)                        NULL,        -- 等效联机管长
    C_IF_EXT_PRT          varchar2(32)                         NULL,        -- 是否需配延长组件
    N_HEIGHT_DIFF         number(16, 2)                        NULL,        -- 室外机与室内机高度落差
    C_IF_LWMCH            varchar2(32)                         NULL,        -- 低温型室外机需求
    N_XLPP_LENGTH         number(16, 2)                        NULL,        -- 水冷机组水管接口管长度
    C_WND_TYPE            varchar2(32)                         NULL,        -- 送风型式
    C_WNPP_LENGTH         varchar2(300)                        NULL,        -- 风管尺寸(宽×高×长)
    N_ESD_FLHGHT          number(16, 2)                        NULL,        -- 静电地板高度
    N_WND_RATE            number(16, 2)                        NULL,        -- 地板通风率
    N_JPPP_LENGTH         number(16, 2)                        NULL,        -- 加湿排水管长
    N_JJPP_LENGTH         number(16, 2)                        NULL,        -- 加湿进水管长
    N_LPPP_LENGTH         number(16, 2)                        NULL,        -- 冷凝排水管长
    N_MCH_LNLEN           number(16, 2)                        NULL,        -- 室内机线缆长
    C_INS_WNDREQ          varchar2(300)                        NULL,        -- 安装送风组件条件
    C_IF_DIP              varchar2(32)                         NULL,        -- 是否需要对空调解体搬运
    C_HNG_NTS             varchar2(300)                        NULL,        -- 如需外请吊装，情况另外说明
    C_UMB_DRAFT           varchar2(300)                        NULL,        -- UPS,机柜，配电，电池的设备布局图
    C_TMPWND_NTS          varchar2(300)                        NULL,        -- 现场温湿度与通风情况
    C_DUST_EVAL           varchar2(300)                        NULL,        -- 现场金属粉尘评估
    C_IF_FRTMCH           varchar2(32)                         NULL,        -- 前端是否使用油机
    C_EXT_ELEC            varchar2(32)                         NULL,        -- 配电系统
	C_FRT_ELEC            varchar2(32)                         NULL,        -- 前端配电大小
    C_INS_HGHT            number(16, 2)                        NULL,        -- 安装空间与梁下净高
    C_FIX_ENV             varchar2(300)                        NULL,        -- 维修环境
    C_ENT_DRSZE           varchar2(300)                        NULL,        -- 入场方式与电梯门大小
    C_INS_TOOL            varchar2(300)                        NULL,        -- 安装工具
    C_ENTR_DT             date                                 NULL,        -- 入场时间
    C_INOUT_TYPE          varchar2(300)                        NULL,        -- 进出线方式
    C_LN_INSTYP           varchar2(300)                        NULL,        -- 线缆安装方式
    C_UPS_LDTYP           varchar2(300)                        NULL,        -- UPS负载类型与容量
    C_DFT_ST              varchar2(300)                        NULL,        -- 防护情况
    C_IF_DVCDST           varchar2(32)                         NULL,        -- 设备间距是否符合维修条件
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
constraint PK_M_T_PRE_SALE primary key( C_PID )
)


-------------------------
-- 工程界面的IDM
-------------------------

create table CRM_T_IDM
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	C_PRJTCH_RQ           varchar2(300)                        NULL,        -- 项目技术要求
    C_RCK_NUM             number(16, 2)                        NULL,        -- 每个模块机柜数量
    C_RCK_SIZE            varchar2(300)                        NULL,        -- 机柜尺寸要求
    C_ENEXT_RQ            varchar2(300)                        NULL,        -- 后期扩容要求
    C_ELEC_INT            varchar2(32)                         NULL,        -- 市电输入
    C_BTTR_BKTM           number(16, 2)                        NULL,        -- 电池后备时间
    C_ELCRCK_RQ           varchar2(300)                        NULL,        -- 配电柜需求
    C_RCK_CRC             varchar2(32)                         NULL,        -- 机柜供电回路
    C_RM_LNDP             varchar2(32)                         NULL,        -- 机房布线
    C_BTR_INSPS           varchar2(300)                        NULL,        -- 电池安装位置
    C_PDU_OUT             varchar2(300)                        NULL,        -- PDU输出插孔
    C_PDU_OTNM            number(32, 0)                        NULL,        -- PDU输出路数
    C_PDU_EXFN            varchar2(300)                        NULL,        -- PDU选配功能
    C_CLR_CLDTYP          varchar2(32)                         NULL,        -- 空调制冷方式
	C_CLR_CLDNTS          varchar2(300)                        NULL,        -- 空调制冷方式其它
    C_CLR_WNDTYP          varchar2(300)                        NULL,        -- 空调送风方式
	C_CLR_WNDNTS          varchar2(300)                        NULL,        -- 空调送风方式其它
    C_MCH_TYP             varchar2(300)                        NULL,        -- 机型选择
    C_MCH_PST             varchar2(300)                        NULL,        -- 室外机位置
    C_RDM_REQ             varchar2(32)                         NULL,        -- 冗余需求
    C_MCH_HGT             number(16, 2)                        NULL,        -- 室外机与室内机之间的高度差
    C_MCHPP_LNG           number(16, 2)                        NULL,        -- 室内机与室外机的管程长度
    C_IF_WTR              varchar2(32)                         NULL,        -- 是否有给排水
    C_IF_CLSPP            varchar2(32)                         NULL,        -- 是否封闭通道
    C_CLSPP_TYP           varchar2(32)                         NULL,        -- 封闭通道形式
    C_RCK_BRD             varchar2(32)                         NULL,        -- 机柜品牌
    C_CLS_PP              varchar2(32)                         NULL,        -- 封闭通道门
    C_IF_MNTR             varchar2(300)                        NULL,        -- 是否需要监控
    C_ALM_RQ              varchar2(32)                         NULL,        -- 告警需求
    N_SMK_NM              number(16, 2)                        NULL,        -- 烟感数量
    N_TMPWT_NM            number(32, 0)                        NULL,        -- 温湿度数量
    N_INF_NM              number(32, 0)                        NULL,        -- 红外探测数量
    N_DRP_NM              number(32, 0)                        NULL,        -- 漏水监测数量
    N_DR_NM               number(32, 0)                        NULL,        -- 门禁门的数量
    C_DRP_TYP             varchar2(32)                         NULL,        -- 漏水检测方式
    C_DR_TYP              varchar2(32)                         NULL,        -- 开门方式
    C_DRLCK_TYP           varchar2(32)                         NULL,        -- 门锁方式
    N_CCTV_NM             number(32, 0)                        NULL,        -- 视频监控数量
    C_VD_RQ               varchar2(32)                         NULL,        -- 视频像素要求
    C_VD_TM               number(16, 2)                        NULL,        -- 视频存储时间
    C_INS_DBG             varchar2(32)                         NULL,        -- 原厂安装调试
    C_BTT_MNT             varchar2(300)                        NULL,        -- 电池监控
    C_TRD_DVC             varchar2(300)                        NULL,        -- 第三方设备情况
constraint PK_M_T_IDM primary key( C_PID )
)


-------------------------
-- 工程界面的IDU
-------------------------

create table CRM_T_IDU
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	C_PRJTCH_RQ           varchar2(300)                        NULL,        -- 项目技术要求
    N_RCKMDL_NM           number(32, 0)                        NULL,        -- 每个模块机柜数量
    N_RCK_NM              number(32, 0)                        NULL,        -- 机柜数量
	C_EDEXT_RQ            varchar2(300)                        NULL,        -- 后期扩容要求
    C_ELEC_IN             varchar2(300)                        NULL,        -- 市电输入
    N_UPS_CPC             number(16, 2)                        NULL,        -- UPS额定容量
    C_IF_UPSPRRL          varchar2(32)                         NULL,        -- UPS是否需要并机冗余
    N_BCKBTT_TM           number(16, 2)                        NULL,        -- 电池后备时间
    C_IF_ELECIN           varchar2(32)                         NULL,        -- 输入配电单元是否需要预留
	C_ELECIN_NM           varchar2(32)                         NULL,        -- 输入配电单元预留路
    C_IF_ELECOUT          varchar2(32)                         NULL,        -- 输出配电单元是否需要预留
	C_ELECOUT_NM          varchar2(32)                         NULL,        -- 输出配电单元预留路
    C_RCKELEC_CLC         varchar2(32)                         NULL,        -- 机柜供电回路
    C_RM_DSLLN            varchar2(300)                        NULL,        -- 机房布线
    C_BTTINS_TYP          varchar2(32)                         NULL,        -- 电池安装形式
    C_PDUOT_TYP           varchar2(300)                        NULL,        -- PDU输出插孔
    N_PDUOT_NM            number(32, 0)                        NULL,        -- PDU输出路数
    C_PDU_OPT             varchar2(300)                        NULL,        -- PDU选配功能
    C_IF_CLR              varchar2(32)                         NULL,        -- 是否需要空调制冷
    C_CLR_TYP             varchar2(300)                        NULL,        -- 空调制冷方式
    C_MCH_TYP             varchar2(300)                        NULL,        -- 机型选择
    C_MCH_PST             varchar2(300)                        NULL,        -- 室外机位置
    C_RDN_RQ              varchar2(32)                         NULL,        -- 冗余需求
    N_MCH_HGH             number(16, 2)                        NULL,        -- 室外机与室内机之间的高度差
    N_MCH_LNG             number(16, 2)                        NULL,        -- 室内机与室外机的管程长度
    C_IF_WTR              varchar2(32)                         NULL,        -- 是否有给排水
    C_IF_FAN              varchar2(32)                         NULL,        -- 是否需要应急风扇
    C_IF_FP               varchar2(32)                         NULL,        -- 是否需要消防
    C_SPC_RQ              varchar2(300)                        NULL,        -- 监控为默认配置有无特殊要求
constraint PK_M_T_IDU primary key( C_PID )
)

-------------------------
-- 报价申请合同表
-------------------------

create table CRM_T_GEN_CNT
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
    C_CNT_ID	          varchar2(300)                        NOT NULL,    -- 合同编号
    C_CNT_NM              varchar2(300)                        NULL,        -- 合同名称
    C_CNT_TYP          	  varchar2(300)                        NULL,        -- 合同类型
    DT_CRT_DT             DATE			                       NULL,        -- 创建日期
constraint PK_M_T_GEN_CNT primary key( C_PID )
)


-------------------------
-- 工程清单表
-------------------------

create table CRM_T_PRJ_LST
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	C_PRD_CD	          varchar2(300)                        NULL,        -- 产品编码
    C_PRD_PKG             varchar2(300)                        NULL,    	-- 产品包
    C_PRD_NAME            varchar2(300)                        NULL,        -- 产品名称
    C_PRD_CTG             varchar2(300)                        NULL,        -- 产品分类
	C_PRD_CTGID           varchar2(300)                        NULL,        -- 产品分类ID
    C_PRD_TYP	          varchar2(300)                        NULL,        -- 产品型号
    C_PRD_UNT             varchar2(32)                         NULL,        -- 单位
    N_PRD_SPRC            number(16, 2)                        NULL,        -- 定价
    N_PRD_PRC             number(20, 2)                        NULL,        -- 报价
    N_AMOUNT              number(16, 2)                        NULL,        -- 数量
    N_TTL_UNT             number(20, 2)                        NULL,       	-- 单品总金额
    N_HH_TTL              number(20, 2)                        NULL,        -- 行汇总单价
    N_AVG_TTL             number(20, 2)                        NULL,        -- 平均总单价
    C_NSTD_RQ		      varchar2(300)                        NULL,        -- 非标需求
    C_NOTES   			  varchar2(300)                        NULL,	    -- 备注
	C_LVID                varchar2(300)                        NULL,    	-- lovId
	C_MATER_CODE 		VARCHAR2(12 BYTE)					   NULL, 		-- 物料号
	C_PRO_DESC 			VARCHAR2(300 BYTE)					   NULL, 		--产品描述
	C_CUS_PRO_DESC 		VARCHAR2(300 BYTE)					   NULL, 		--客户产品描述
	N_ORD_NUM 			NUMBER(16,2)						   NULL, 		--已订购数量
	C_ORD_NO 			VARCHAR2(32 BYTE)					   NULL, 		--订单编号
	C_PRO_ID 			VARCHAR2(32 BYTE)				       NULL, 		--产品ID
	N_VERIED_NUM 		NUMBER(16,2)						   NULL, 		--已核销数量
	N_NOT_VERI_NUM 		NUMBER(16,2)						   NULL, 		--未核销数量
	C_VERI_STATUS 		VARCHAR2(32 BYTE)					   NULL, 		--核销状态
	C_BUSINEES_CODE 	VARCHAR2(50 BYTE)					   NULL, 		--业务号
	N_NEW_PRD_PRC 		NUMBER(20,2)     					   NULL, 		--新报价
	N_NEW_AMOUNT 		NUMBER(16,2)						   NULL, 		--新数量
	N_NEW_HH_TTL       	NUMBER(20,2)                           NULL,        --新行汇总单价
	N_NEW_AVG_TTL 		NUMBER(20,2)						   NULL, 		--新平均总单价
	C_UPDT_FLAG 		VARCHAR2(32 BYTE)					   NULL, 		--更新标志
	C_UPDT_TYPE 		VARCHAR2(32 BYTE)					   NULL, 		--更新类型
	N_NEW_TTL_UNT 		NUMBER(20,2)						   NULL,		--新单品总金额
	C_SPRV_MRK		      varchar2(300)                        NULL,        --特价审批标志
	C_SPRV_LVL		      varchar2(300)                        NULL,        --需要特价审批的层次
	C_CUR_LVL		  	  varchar2(300)                        NULL,        --当前层级
	C_LINE_NUM			  varchar2(300)                        NULL,        --产品行号
	
constraint PK_M_T_PRJ_LST primary key( C_PID )
)


-------------------------
-- 工程评审表
-------------------------

create table CRM_T_PRJ_EVL
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_SEQ_NO              number(32, 0)                        NULL,    	-- 序号
    C_EVL_TYP             varchar2(300)                        NULL,        -- 评审类别
    C_EVL_MM              varchar2(300)                        NULL,        -- 评审人
    C_EVL_ST	          varchar2(300)                        NULL,        -- 评审状态
    DT_SBM_DT              DATE		                           NULL,        -- 提交时间
    C_EVL_RS              varchar2(300)                        NULL,        -- 评审结论
    C_EVL_SG              varchar2(300)                        NULL,        -- 评审人意见
    DT_FIN_DT              DATE		                           NULL,        -- 评审完成时间
constraint PK_M_T_PRJ_EVL primary key( C_PID )
)



-------------------------
-- 附件表
-------------------------

create table CRM_T_ATT
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_DOC_NM              varchar2(300)                        NULL,    	-- 文档名称
	C_DOC_CMT             varchar2(300)                        NULL,    	-- 附件说明
    C_UPDR	              varchar2(300)                        NULL,        -- 上传人
    DT_UPD_DT              DATE      		                   NULL,        -- 上传时间
    C_NOTES		          varchar2(300)                        NULL,        -- 备注
constraint PK_M_T_ATT primary key( C_PID )
)



-------------------------
-- 单UPS
-------------------------

create table CRM_T_SNG_UPS
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_ELEC_DRAFT          varchar2(300)                        NULL,        -- 配电系统图纸
    C_PRJTCH_RQ           varchar2(300)                        NULL,        -- 项目技术要求
    C_INOTLN_TYP          varchar2(300)                        NULL,        -- 进出线方式
    C_UPS_CNST            varchar2(300)                        NULL,        -- UPS系统组成
    N_BCKUP_TM            number(16, 2)                        NULL,        -- 后备时间
    C_MNT_RQ              varchar2(300)                        NULL,        -- 监控要求协议要求
    C_IP_LVL              varchar2(300)                        NULL,        -- IP等级
	C_IP_LVL_RQ           varchar2(300)                        NULL,        -- IP等级其它
    C_OTHERS_RQ           varchar2(300)                        NULL,        -- 其他特殊需求
constraint PK_M_T_SNG_UPS primary key( C_PID )
)


-------------------------
-- 单电池
-------------------------

create table CRM_T_SNG_BATTERY
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_PRJTCH_RQ           varchar2(300)                        NULL,        -- 项目技术要求
    C_ENVLYT_DRFT         varchar2(300)                        NULL,        -- 现场布放图纸
    C_BTT_CP              varchar2(300)                        NULL,        -- 电池容量
	C_BTT_TNM             varchar2(300)                        NULL,        -- 电池总数量
	C_BTT_CNM             varchar2(300)                        NULL,        -- 电池组数
    C_IF_BTTMNT           varchar2(32)                         NULL,        -- 是否需要电池单体监控
    C_BTTMNT_TYP          varchar2(300)                        NULL,        -- 电池监控方式
    C_BTTINS_TYP          varchar2(300)                        NULL,        -- 电池安装形式
    C_BTTPS_TYP           varchar2(300)                        NULL,        -- 电池摆放方式
    C_BTTRCKLN_TYP        varchar2(300)                        NULL,        -- 电池柜架进出线方式
    N_BTTINS_HGH          number(16, 2)                        NULL,        -- 电池安装空间梁下净高
    C_BTTSWT_INS          varchar2(300)                        NULL,        -- 电池开关安装
    C_OTHERS_RQ           varchar2(300)                        NULL,        -- 其他特殊要求
    C_BTTSWT_RMT          varchar2(32)                         NULL,        -- 电池开关是否要远程控制
constraint PK_M_T_SNG_BATTERY primary key( C_PID )
)


-------------------------
-- 单空调
-------------------------

create table CRM_T_SNG_COOLER
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_PRJTCH_RQ           varchar2(300)                        NULL,        -- 项目技术要求
    C_CLR_TYP             varchar2(32)                         NULL,        -- 空调制冷方式
    C_WND_TYP             varchar2(300)                        NULL,        -- 空调送风方式
    C_MCH_TYP             varchar2(300)                        NULL,        -- 机型选择
    C_MCH_PST             varchar2(300)                        NULL,        -- 室外机位置
    C_MCH_HGH             number(16, 2)                        NULL,        -- 室外机与室内机之间的高度差
    C_MCH_LNG             number(16, 2)                        NULL,        -- 室内机与室外机的管程长度
    C_OTHERS_NTS          varchar2(300)                        NULL,        -- 其他情况说明
    C_IF_MNT              varchar2(32)                         NULL,        -- 是否需要监控
    C_MNT_TYP             varchar2(300)                        NULL,        -- 监控方式
    C_IF_DRP              varchar2(32)                         NULL,        -- 是否需要漏水检测
    C_DRP_TYP             varchar2(300)                        NULL,        -- 漏水检测方式
    C_CLR_IN_TMP          varchar2(300)                        NULL,        -- 水冷空调进水温度
	C_CLR_OT_TMP          varchar2(300)                        NULL,        -- 水冷空调出水温度
constraint PK_M_T_SNG_COOLER primary key( C_PID )
)


-------------------------
-- 单配电
-------------------------

create table CRM_T_SNG_ELEC
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
    C_LN_INOT             varchar2(300)                        NULL,    -- 进出线方式
    C_MNT_TYP             varchar2(300)                        NULL,    -- 维护方式
	C_MNT_NTS             varchar2(300)                        NULL,    -- 维护方式其它
    C_FRDR_TYP            varchar2(300)                        NULL,    -- 开门方式（前门）
	C_FRDR_NTS            varchar2(300)                        NULL,    -- 开门方式其它（前门）
    C_RRDR_TYP            varchar2(300)                        NULL,    -- 开门方式（后门）
	C_RRDR_NTS            varchar2(300)                        NULL,    -- 开门方式其它（后门）
    C_FRDR                varchar2(300)                        NULL,    -- 前门型式
    N_INOT_LN             number(16, 2)                        NULL,    -- 进出线电缆线径
    C_RCK_SZ              varchar2(300)                        NULL,    -- 柜体尺寸
	C_RCK_SZ_NTS          varchar2(300)                        NULL,    -- 柜体尺寸其它
    C_RCKPR_LVL           varchar2(32)                         NULL,    -- 柜体防护等级
	C_RCKPR_NTS           varchar2(32)                         NULL,    -- 柜体防护等级其它
    C_SWT_BRD             varchar2(300)                        NULL,    -- 开关品牌
	C_SWT_NTS             varchar2(300)                        NULL,    -- 开关品牌其它
    C_FP_TK               varchar2(32)                         NULL,    -- 消防分励脱扣
    C_CLC_PLGIN           varchar2(32)                         NULL,    -- 微型断路器热插拔功能
    C_ELEC_DRF            varchar2(32)                         NULL,    -- 提供配电系统图
    C_TTLIN_SWT_CRT       varchar2(300)                        NULL,    -- 单电源总输入开关电流
	C_TTLIN_SWT_PLE       varchar2(300)                        NULL,    -- 单电源总输入开关极数
	C_TTLIN_SWT_NM        varchar2(300)                        NULL,    -- 单电源总输入开关数量
    C_ATS_SWT_CRT         varchar2(300)                        NULL,    -- 双电源总输入ATS开关电流
	C_ATS_SWT_PLE         varchar2(300)                        NULL,    -- 双电源总输入ATS开关极数
	C_ATS_SWT_NM          varchar2(300)                        NULL,    -- 双电源总输入ATS开关数量
    C_ELECOT_SWT_CRT      varchar2(300)                        NULL,    -- 市电输出开关电流
	C_ELECOT_SWT_PLE      varchar2(300)                        NULL,    -- 市电输出开关极数
	C_ELECOT_SWT_NM       varchar2(300)                        NULL,    -- 市电输出开关数量	
    C_UPSIN_SWT_CRT       varchar2(300)                        NULL,    -- UPS输入开关电流
	C_UPSIN_SWT_PLE       varchar2(300)                        NULL,    -- UPS输入开关极数
	C_UPSIN_SWT_NM        varchar2(300)                        NULL,    -- UPS输入开关数量
    C_UPSOT_SWT_CRT       varchar2(300)                        NULL,    -- UPS输出开关电流
	C_UPSOT_SWT_PLE       varchar2(300)                        NULL,    -- UPS输出开关极数
	C_UPSOT_SWT_NM        varchar2(300)                        NULL,    -- UPS输出开关数量
    C_UPSBP_SWT_CRT       varchar2(300)                        NULL,    -- UPS旁路开关电流
	C_UPSBP_SWT_PLE       varchar2(300)                        NULL,    -- UPS旁路开关极数
	C_UPSBP_SWT_NM        varchar2(300)                        NULL,    -- UPS旁路开关数量
    C_FRT_UPS_CRT         varchar2(300)                        NULL,    -- 1UPS输出开关电流
	C_FRT_UPS_PLE         varchar2(300)                        NULL,    -- 1UPS输出开关极数
	C_FRT_UPS_NM          varchar2(300)                        NULL,    -- 1UPS输出开关数量
    C_SCD_UPS_CRT         varchar2(300)                        NULL,    -- 2UPS输出开关电流
	C_SCD_UPS_PLE         varchar2(300)                        NULL,    -- 2UPS输出开关极数
	C_SCD_UPS_NM          varchar2(300)                        NULL,    -- 2UPS输出开关数量
    C_OTHERS_RQ           varchar2(300)                        NULL,    -- 其他特殊需求
constraint PK_M_T_SNG_ELEC primary key( C_PID )
)


-------------------------
-- 单机柜
-------------------------

create table CRM_T_SNG_RACK
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,-- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,-- 类型
	C_PRJTCH_RQ           varchar2(300)                        NULL,    -- 项目技术要求
    C_LN_INOT             varchar2(300)                        NULL,    -- 进出线方式
    C_RCK_SZ              varchar2(300)                        NULL,    -- 柜体尺寸
	C_RCK_SZ_NTS          varchar2(300)                        NULL,    -- 柜体尺寸其他
    C_FRDR_TYP            varchar2(300)                        NULL,    -- 开门方式（前门）
	C_FRDR_NTS            varchar2(300)                        NULL,    -- 开门方式（前门）其他
    C_RRDR_TYP            varchar2(300)                        NULL,    -- 开门方式（后门）
	C_RRDR_NTS            varchar2(300)                        NULL,    -- 开门方式（后门）其他
    C_FRRR_DR             varchar2(300)                        NULL,    -- 前后门型式
	C_FRRR_NTS            varchar2(300)                        NULL,    -- 前后门型式其他
    C_SD_DR               varchar2(300)                        NULL,    -- 侧门
	C_SD_NTS              varchar2(300)                        NULL,    -- 侧门其他
    C_RCKPR_LVL           varchar2(300)                        NULL,    -- 柜体防护等级
	C_RCKPR_NTS           varchar2(300)                        NULL,    -- 柜体防护等级其他
    C_LN_SLOT             varchar2(300)                        NULL,    -- 理线槽
	C_LN_SLOT_NM          varchar2(300)                        NULL,    -- 理线槽规格数量
	C_LN_SLOT_NTS         varchar2(300)                        NULL,    -- 理线槽规格其他
    C_BTTM                varchar2(300)                        NULL,    -- 底板
	C_BTTM_NM             varchar2(300)                        NULL,    -- 底板数量
	C_BTTM_NTS            varchar2(300)                        NULL,    -- 底板其他
    C_LVL                 varchar2(300)                        NULL,    -- 层板
	C_LVL_NM              varchar2(300)                        NULL,    -- 层板数量
	C_LVL_NTS             varchar2(300)                        NULL,    -- 层板其他
    C_BLD                 varchar2(300)                        NULL,    -- 盲板
	C_BLD_NM              varchar2(300)                        NULL,    -- 盲板数量
	C_BLD_NTS             varchar2(300)                        NULL,    -- 盲板其他
    C_CLR_UT              varchar2(300)                        NULL,    -- 散热单元
	C_CLR_UT_NM           varchar2(300)                        NULL,    -- 散热单元数量
	C_CLR_UT_NTS          varchar2(300)                        NULL,    -- 散热单元其他
    C_RCK_CMT             varchar2(300)                        NULL,    -- 机柜密闭组件
	C_RCK_CMT_NM          varchar2(300)                        NULL,    -- 机柜密闭组件数量
	C_RCK_CMT_NTS         varchar2(300)                        NULL,    -- 机柜密闭组件其他
    C_OTHERS_RQ           varchar2(300)                        NULL，   -- 其他特殊需求
constraint PK_M_T_SNG_RACK primary key( C_PID )
)


-------------------------
-- 单监控
-------------------------

create table CRM_T_SNG_MNT
(
    C_PID                 varchar2(300)                        NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,-- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,-- 类型
	C_PRJTCH_RQ           varchar2(300)                        NULL,    -- 项目技术要求
    C_ALM_RQ              varchar2(300)                        NULL,    -- 告警需求
    C_PWR_MNT             varchar2(300)                        NULL,    -- 动力监控设备
    C_UPS                 varchar2(300)                        NULL,    -- UPS
	C_UPS_NM              varchar2(300)                        NULL,    -- UPS规格数量
    C_CLR                 varchar2(300)                        NULL,    -- 精密空调
	C_CLR_NM              varchar2(300)                        NULL,    -- 精密空调规格数量
    C_WND                 varchar2(300)                        NULL,    -- 新风机
	C_WND_NM              varchar2(300)                        NULL,    -- 新风机规格数量
    C_JM_ELEC             varchar2(300)                        NULL,    -- 精密配电
	C_JM_ELEC_NM          varchar2(300)                        NULL,    -- 精密配电规格数量
    C_INT_ELEC            varchar2(300)                        NULL,    -- 智能配电
	C_INT_ELEC_NM         varchar2(300)                        NULL,    -- 智能配电规格数量
    C_DRP                 varchar2(300)                        NULL,    -- 定位漏水
	C_DRP_NM              varchar2(300)                        NULL,    -- 定位漏水规格数量
    C_ELEC_GNR            varchar2(300)                        NULL,    -- 智能发电机
	C_ELEC_GNR_NM         varchar2(300)                        NULL,    -- 智能发电机规格数量
    C_PWR_OTR             varchar2(300)                        NULL,    -- 其他(动力监控设备)
	C_PWR_OTR_NM          varchar2(300)                        NULL,    -- 其他规格数量(动力监控设备)
    C_ENV_MNT             varchar2(300)                        NULL,    -- 环境监控设备
    C_TMP                 varchar2(300)                        NULL,    -- 温湿度
	C_TMP_NM              varchar2(300)                        NULL,    -- 温湿度规格数量
    C_SMK                 varchar2(300)                        NULL,    -- 烟感
	C_SMK_NM              varchar2(300)                        NULL,    -- 烟感规格数量
    C_IFR                 varchar2(300)                        NULL,    -- 红外
	C_IFR_NM              varchar2(300)                        NULL,    -- 红外规格数量
    C_DR_LCK              varchar2(300)                        NULL,    -- 门磁
	C_DR_LCK_NM           varchar2(300)                        NULL,    -- 门磁规格数量
	C_NWWND               varchar2(300)                        NULL,    -- 新风机(环境监控设备)
	C_NWWND_NM            varchar2(300)                        NULL,    -- 新风机规格数量(环境监控设备)
    C_NP_DRP              varchar2(300)                        NULL,    -- 不定位漏水
	C_NP_DRP_NM           varchar2(300)                        NULL,    -- 不定位漏水规格数量
    C_MNT_OTR             varchar2(300)                        NULL,    -- 其他(环境监控设备)
	C_MNT_OTR_NM          varchar2(300)                        NULL,    -- 其他规格数量(环境监控设备)
    C_SEC_SYS             varchar2(300)                        NULL,    -- 安防系统
	C_SEC_SYS_NTS         varchar2(300)                        NULL,    -- 安防系统描述
    C_DR_CNR              varchar2(300)                        NULL,    -- 门禁系统
	C_DR_CNR_NTS          varchar2(300)                        NULL,    -- 门禁系统描述
    C_OTHERS_RQ           varchar2(300)                        NULL,     -- 其他特殊需求
constraint PK_M_T_SNG_MNT primary key( C_PID )
)







-------------------------
-- 标书审核
-------------------------

create table CRM_T_BIDDC_EVL
(
    C_PID                 varchar2(300)                        NOT NULL,    -- 主键自增
    C_QID                 varchar2(32)                         NOT NULL,    -- 报价单编号
	C_TYPE	              varchar2(32)                         NOT NULL,    -- 类型
	C_CUR_PRSR	          varchar2(32)                         NULL,    -- 当前处理人
	C_CUR_STTS	          varchar2(32)                         NULL,    -- 当前状态
	C_EVL_SGT	          varchar2(32)                         NULL,    -- 审核意见
	C_SN_PNT	          varchar2(32)                         NULL,    -- 下级结点
	C_DOC_URL	          varchar2(32)                         NULL,    -- 标书URL
	constraint PK_M_T_BIDDC_EVL primary key( C_PID )
)








-------------------------
-- 年度目标维护表
-------------------------

create table CRM_T_ANL_SALTRT
(
    C_PID                 varchar2(300)                        NULL,    	-- 主键自增
    C_TYP                 varchar2(300)                        NOT NULL,    -- 类型
    C_DEP	          	  varchar2(300)                        NULL,   		-- 部门
	C_DEP_ID	          varchar2(300)                        NULL,   		-- 部门ID
    C_EMP	              varchar2(300)                        NULL,        -- 销售员
    C_EMP_NM         	  varchar2(300)                        NULL,        -- 工号
    C_CURRENCY         	  varchar2(300)                        NOT NULL,    -- 币种
	C_ANNUAL         	  varchar2(300)                        NOT NULL,    -- 年度
	N_ANL_TRG			  number(32, 2)                        NOT NULL,    -- 年度目标
	N_JAN_TRG			  number(32, 2)                        NULL,    	-- 1月
	N_FEB_TRG			  number(32, 2)                        NULL,    	-- 2月
	N_MRC_TRG			  number(32, 2)                        NULL,    	-- 3月
	N_APR_TRG			  number(32, 2)                        NULL,    	-- 4月
	N_MAY_TRG			  number(32, 2)                        NULL,    	-- 5月
	N_JUN_TRG			  number(32, 2)                        NULL,    	-- 6月
	N_JUL_TRG			  number(32, 2)                        NULL,    	-- 7月
	N_AGT_TRG			  number(32, 2)                        NULL,    	-- 8月
	N_SEP_TRG			  number(32, 2)                        NULL,    	-- 9月
	N_OCT_TRG			  number(32, 2)                        NULL,    	-- 10月
	N_NOV_TRG			  number(32, 2)                        NULL,    	-- 11月
	N_DEC_TRG			  number(32, 2)                        NULL,    	-- 12月
constraint PK_M_T_ANL_SALTRT primary key( C_PID )
)











comment on table  CRM_T_QUOTATION_BASIC                                  is '报价单基本信息表';
comment on column CRM_T_QUOTATION_BASIC.C_PID                             is '主键自增';
comment on column CRM_T_QUOTATION_BASIC.C_QID                             is '报价单编号';
comment on column CRM_T_QUOTATION_BASIC.N_VERSION                             is '报价单版本';
comment on column CRM_T_QUOTATION_BASIC.C_NAME                             is '报价单名称';
comment on column CRM_T_QUOTATION_BASIC.C_SALESREP                             is '销售代表';
comment on column CRM_T_QUOTATION_BASIC.C_SALESREP_DEP                             is '销售代表部门';
comment on column CRM_T_QUOTATION_BASIC.C_BO_CODE                             is '商机编码';
comment on column CRM_T_QUOTATION_BASIC.C_BO_NAME                             is '商机名称';
comment on column CRM_T_QUOTATION_BASIC.C_ORG                             is '组织';
comment on column CRM_T_QUOTATION_BASIC.C_CUSTOMER_NAME                             is '客户名称';
comment on column CRM_T_QUOTATION_BASIC.C_CUSTOMER_CODE                             is '客户编号';
comment on column CRM_T_QUOTATION_BASIC.C_CONTACTS                             is '联系人';
comment on column CRM_T_QUOTATION_BASIC.C_STATUS                             is '状态';
comment on column CRM_T_QUOTATION_BASIC.C_CLOSURE_REASON                             is '关闭原因';
comment on column CRM_T_QUOTATION_BASIC.C_PRO_REVIEW_STATUS                             is '工程评审状态';
comment on column CRM_T_QUOTATION_BASIC.C_BID_AUDIT_STATUS                             is '标书审核状态';
comment on column CRM_T_QUOTATION_BASIC.C_CREATE_TIME                             is '创建日期';
comment on column CRM_T_QUOTATION_BASIC.C_CREATOR                             is '创建人';
comment on column CRM_T_QUOTATION_BASIC.C_PRICE_LIST                             is '价格表';
comment on column CRM_T_QUOTATION_BASIC.C_CURRENCY                             is '货币';
comment on column CRM_T_QUOTATION_BASIC.N_AMOUNT                             is '总额';
comment on column CRM_T_QUOTATION_BASIC.C_BID_RESULTS                             is '投标结果';
comment on column CRM_T_QUOTATION_BASIC.C_BID_RESULTS_Reason                             is '丢标/赢标原因';
comment on column CRM_T_QUOTATION_BASIC.N_IS_ProReview                             is '是否需工程评审';
comment on column CRM_T_QUOTATION_BASIC.N_is_BidPro                             is '是否投标项目';
comment on column CRM_T_QUOTATION_BASIC.N_BID_NO                             is '有效标识';
comment on column CRM_T_QUOTATION_BASIC.N_is_valid                             is '有效标识';
comment on column CRM_T_QUOTATION_BASIC.C_PAYTYPE                             is '付款方式';
comment on column CRM_T_QUOTATION_BASIC.C_PRC_ID                             is '流程编号';


comment on table  CRM_T_PG_INF                                  is '界面完成信息表';
comment on column CRM_T_PG_INF.C_PID                             is '主键自增';
comment on column CRM_T_PG_INF.C_QID                             is '报价单编号';
comment on column CRM_T_PG_INF.C_TYPE                          is '类型';
comment on column CRM_T_PG_INF.C_PG_TYP                          is '界面类型';
comment on column CRM_T_PG_INF.C_REL_PRD                          is '相关产品 集成需求';
comment on column CRM_T_PG_INF.C_COMPLETE                          is '表单填写完成度';
comment on column CRM_T_PG_INF.C_REQ_NO                          is '支持请求单号';
comment on column CRM_T_PG_INF.C_SUPPORTER                          is '支持人';
comment on column CRM_T_PG_INF.C_STATUS                          is '受理状态';



comment on table  CRM_T_SNG_UPS                                  is '单UPS';
comment on column CRM_T_SNG_UPS.C_PID                             is '主键自增';
comment on column CRM_T_SNG_UPS.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_UPS.C_ELEC_DRAFT                             is '配电系统图纸';
comment on column CRM_T_SNG_UPS.C_PRJTCH_RQ                             is '项目技术要求';
comment on column CRM_T_SNG_UPS.C_INOTLN_TYP                             is '进出线方式';
comment on column CRM_T_SNG_UPS.C_UPS_CNST                             is 'UPS系统组成';
comment on column CRM_T_SNG_UPS.N_BCKUP_TM                             is '后备时间';
comment on column CRM_T_SNG_UPS.C_MNT_RQ                             is '监控要求协议要求';
comment on column CRM_T_SNG_UPS.C_IP_LVL                             is 'IP等级';
comment on column CRM_T_SNG_UPS.C_OTHERS_RQ                             is '其他特殊需求';





comment on table  CRM_T_BASE_INF                                  is '工程界面的基本信息表';
comment on column CRM_T_BASE_INF.C_PID                             is '主键自增';
comment on column CRM_T_BASE_INF.C_QID                             is '报价单编号';
comment on column CRM_T_BASE_INF.C_DRAFT                             is '机房图纸';
comment on column CRM_T_BASE_INF.C_PROJ_NAME                             is '项目名称';
comment on column CRM_T_BASE_INF.N_HEIGHT                             is '当地海拔';
comment on column CRM_T_BASE_INF.C_CNST_LEVEL                             is '建设等级';
comment on column CRM_T_BASE_INF.C_PROJ_ADDR                             is '项目地址';
comment on column CRM_T_BASE_INF.C_PROJ_BUDGET                             is '项目预算';
comment on column CRM_T_BASE_INF.C_RM_TYPE                             is '机房性质';
comment on column CRM_T_BASE_INF.C_INT_PRD_TYPE                             is '集成产品';
comment on column CRM_T_BASE_INF.C_REL_PRD                             is '相关产品';
comment on column CRM_T_BASE_INF.N_RM_LENGTH                             is '机房面积的长度';
comment on column CRM_T_BASE_INF.N_RM_WIDTH                             is '机房面积的宽度';
comment on column CRM_T_BASE_INF.N_RM_LEVEL                             is '机房楼层';
comment on column CRM_T_BASE_INF.C_RM_LEVEL_NOTES                             is '机房楼层的说明';
comment on column CRM_T_BASE_INF.N_RM_LEVEL_HGHT                             is '机房梁下净高';
comment on column CRM_T_BASE_INF.C_RM_LEVEL_HGHT_NTS                             is '机房梁下净高的说明';
comment on column CRM_T_BASE_INF.N_LEVEL_WGHT                             is '楼层承重';
comment on column CRM_T_BASE_INF.C_LEVEL_WGHT_NTS                             is '楼层承重的说明';
comment on column CRM_T_BASE_INF.C_IF_HGH_FLR                             is '是否为高架地板';
comment on column CRM_T_BASE_INF.N_HGH_FLR                             is '高架地板长度';
comment on column CRM_T_BASE_INF.C_HGH_FLR_NTS                             is '高架地板的说明';
comment on column CRM_T_BASE_INF.C_PRJ_OTHER_NTS                             is '项目其他情况说明';
comment on column CRM_T_BASE_INF.C_MH_LAYOUT                             is '多通道或机柜功率不均提供设备布局图';
comment on column CRM_T_BASE_INF.C_LOAD_TYPE                             is '负载类型';
comment on column CRM_T_BASE_INF.N_SERVER_NUM                             is '服务器机柜（若有）';
comment on column CRM_T_BASE_INF.N_SERVER_PWR                             is '单机柜功率';
comment on column CRM_T_BASE_INF.C_SERVER_SIZE                             is '机柜尺寸要求(宽×高×长)';
comment on column CRM_T_BASE_INF.C_EXTENSION                             is '后期扩容要求';
comment on column CRM_T_BASE_INF.C_LD_OTHER_NTS                             is '负载其他情况说明';
comment on column CRM_T_BASE_INF.C_TECH_REQ_PRHS                             is '外购件技术要求';
comment on column CRM_T_BASE_INF.C_INT_REQ_PRHS                             is '外购件安装服务要求';
comment on column CRM_T_BASE_INF.C_LGST_REQ_PRHS                             is '外购件物流要求';
comment on column CRM_T_BASE_INF.C_TURN_ON                             is '只负责开机';
comment on column CRM_T_BASE_INF.C_TURN_ON_PRD                             is '只负责开机的相关产品';
comment on column CRM_T_BASE_INF.C_INS_MNTR                             is '安装督导';
comment on column CRM_T_BASE_INF.C_INS_MNTR_PRD                             is '安装督导的相关产品';
comment on column CRM_T_BASE_INF.C_INS                             is '安装（不含施工材料）';
comment on column CRM_T_BASE_INF.C_INS_PRD                             is '安装（不含施工材料）的相关产品';
comment on column CRM_T_BASE_INF.C_KEY                             is '交钥匙工程';
comment on column CRM_T_BASE_INF.C_KEY_PRD                             is '交钥匙工程的相关产品';
comment on column CRM_T_BASE_INF.DT_DEL_READY                             is '要求到货时间';
comment on column CRM_T_BASE_INF.C_DEL_ADDR                             is '要求详细的到货地点（具体到楼层）';
comment on column CRM_T_BASE_INF.C_BATCH_DEL                             is '分批发货说明（若有）';
comment on column CRM_T_BASE_INF.C_PCKG_REQ                             is '包装标签需求';
comment on column CRM_T_BASE_INF.C_BSS_OTHER_NTS                             is '商务部分的其他特殊需求';
comment on column CRM_T_BASE_INF.C_ORD_REQ                             is '要货申请';
comment on column CRM_T_BASE_INF.C_TYPE                          is '类型';

comment on table  CRM_T_AFT_SALE                                  is '工程界面的售后部分';
comment on column CRM_T_AFT_SALE.C_PID                             is '主键自增';
comment on column CRM_T_AFT_SALE.C_QID                             is '报价单编号';
comment on column CRM_T_AFT_SALE.C_DVC_DRAFT                             is '设备布局图';
comment on column CRM_T_AFT_SALE.N_DXPP_LENGTH                             is '等效联机管长';
comment on column CRM_T_AFT_SALE.C_IF_EXT_PRT                             is '是否需配延长组件';
comment on column CRM_T_AFT_SALE.N_HEIGHT_DIFF                             is '室外机与室内机高度落差';
comment on column CRM_T_AFT_SALE.C_IF_LWMCH                             is '低温型室外机需求';
comment on column CRM_T_AFT_SALE.N_XLPP_LENGTH                             is '水冷机组水管接口管长度';
comment on column CRM_T_AFT_SALE.C_WND_TYPE                             is '送风型式';
comment on column CRM_T_AFT_SALE.C_WNPP_LENGTH                             is '风管尺寸(宽×高×长)';
comment on column CRM_T_AFT_SALE.N_ESD_FLHGHT                             is '静电地板高度';
comment on column CRM_T_AFT_SALE.N_WND_RATE                             is '地板通风率';
comment on column CRM_T_AFT_SALE.N_JPPP_LENGTH                             is '加湿排水管长';
comment on column CRM_T_AFT_SALE.N_JJPP_LENGTH                             is '加湿进水管长';
comment on column CRM_T_AFT_SALE.N_LPPP_LENGTH                             is '冷凝排水管长';
comment on column CRM_T_AFT_SALE.N_MCH_LNLEN                             is '室内机线缆长';
comment on column CRM_T_AFT_SALE.C_INS_WNDREQ                             is '安装送风组件条件';
comment on column CRM_T_AFT_SALE.C_IF_DIP                             is '是否需要对空调解体搬运';
comment on column CRM_T_AFT_SALE.C_HNG_NTS                             is '如需外请吊装，情况另外说明';
comment on column CRM_T_AFT_SALE.C_UMB_DRAFT                             is 'UPS,机柜，配电，电池的设备布局图';
comment on column CRM_T_AFT_SALE.C_TMPWND_NTS                             is '现场温湿度与通风情况';
comment on column CRM_T_AFT_SALE.C_DUST_EVAL                             is '现场金属粉尘评估';
comment on column CRM_T_AFT_SALE.C_IF_FRTMCH                             is '前端是否使用油机';
comment on column CRM_T_AFT_SALE.C_EXT_ELEC                             is '配电系统';
comment on column CRM_T_AFT_SALE.C_INS_HGHT                             is '安装空间与梁下净高';
comment on column CRM_T_AFT_SALE.C_FIX_ENV                             is '维修环境';
comment on column CRM_T_AFT_SALE.C_ENT_DRSZE                             is '入场方式与电梯门大小';
comment on column CRM_T_AFT_SALE.C_INS_TOOL                             is '安装工具';
comment on column CRM_T_AFT_SALE.C_ENTR_DT                             is '入场时间';
comment on column CRM_T_AFT_SALE.C_INOUT_TYPE                             is '进出线方式';
comment on column CRM_T_AFT_SALE.C_LN_INSTYP                             is '线缆安装方式';
comment on column CRM_T_AFT_SALE.C_UPS_LDTYP                             is 'UPS负载类型与容量';
comment on column CRM_T_AFT_SALE.C_DFT_ST                             is '防护情况';
comment on column CRM_T_AFT_SALE.C_IF_DVCDST                             is '设备间距是否符合维修条件';
comment on column CRM_T_AFT_SALE.C_TYPE                          is '类型';
comment on column CRM_T_AFT_SALE.C_FRT_ELEC                             is '前端配电大小';

comment on table  CRM_T_IDM                                  is 'IDM';
comment on column CRM_T_IDM.C_PID                             is '主键自增';
comment on column CRM_T_IDM.C_QID                             is '报价单编号';
comment on column CRM_T_IDM.C_RCK_NUM                             is '每个模块机柜数量';
comment on column CRM_T_IDM.C_RCK_SIZE                             is '机柜尺寸要求';
comment on column CRM_T_IDM.C_ENEXT_RQ                             is '后期扩容要求';
comment on column CRM_T_IDM.C_ELEC_INT                             is '市电输入';
comment on column CRM_T_IDM.C_BTTR_BKTM                             is '电池后备时间';
comment on column CRM_T_IDM.C_ELCRCK_RQ                             is '配电柜需求';
comment on column CRM_T_IDM.C_RCK_CRC                             is '机柜供电回路';
comment on column CRM_T_IDM.C_RM_LNDP                             is '机房布线';
comment on column CRM_T_IDM.C_BTR_INSPS                             is '电池安装位置';
comment on column CRM_T_IDM.C_PDU_OUT                             is 'PDU输出插孔';
comment on column CRM_T_IDM.C_PDU_OTNM                             is 'PDU输出路数';
comment on column CRM_T_IDM.C_PDU_EXFN                             is 'PDU选配功能';
comment on column CRM_T_IDM.C_CLR_CLDTYP                             is '空调制冷方式';
comment on column CRM_T_IDM.C_CLR_WNDTYP                             is '空调送风方式';
comment on column CRM_T_IDM.C_MCH_TYP                             is '机型选择';
comment on column CRM_T_IDM.C_MCH_PST                             is '室外机位置';
comment on column CRM_T_IDM.C_RDM_REQ                             is '冗余需求';
comment on column CRM_T_IDM.C_MCH_HGT                             is '室外机与室内机之间的高度差';
comment on column CRM_T_IDM.C_MCHPP_LNG                             is '室内机与室外机的管程长度';
comment on column CRM_T_IDM.C_IF_WTR                             is '是否有给排水';
comment on column CRM_T_IDM.C_IF_CLSPP                             is '是否封闭通道';
comment on column CRM_T_IDM.C_CLSPP_TYP                             is '封闭通道形式';
comment on column CRM_T_IDM.C_RCK_BRD                             is '机柜品牌';
comment on column CRM_T_IDM.C_CLS_PP                             is '封闭通道门';
comment on column CRM_T_IDM.C_IF_MNTR                             is '是否需要监控';
comment on column CRM_T_IDM.C_ALM_RQ                             is '告警需求';
comment on column CRM_T_IDM.N_SMK_NM                             is '烟感数量';
comment on column CRM_T_IDM.N_TMPWT_NM                             is '温湿度数量';
comment on column CRM_T_IDM.N_INF_NM                             is '红外探测数量';
comment on column CRM_T_IDM.N_DRP_NM                             is '漏水监测数量';
comment on column CRM_T_IDM.N_DR_NM                             is '门禁门的数量';
comment on column CRM_T_IDM.C_DRP_TYP                             is '漏水检测方式';
comment on column CRM_T_IDM.C_DR_TYP                             is '开门方式';
comment on column CRM_T_IDM.C_DRLCK_TYP                             is '门锁方式';
comment on column CRM_T_IDM.N_CCTV_NM                             is '视频监控数量';
comment on column CRM_T_IDM.C_VD_RQ                             is '视频像素要求';
comment on column CRM_T_IDM.C_VD_TM                             is '视频存储时间';
comment on column CRM_T_IDM.C_INS_DBG                             is '原厂安装调试';
comment on column CRM_T_IDM.C_BTT_MNT                             is '电池监控';
comment on column CRM_T_IDM.C_TRD_DVC                             is '第三方设备情况';
comment on column CRM_T_IDM.C_TYPE                          is '类型';

comment on table  CRM_T_IDU                                  is 'IDU';
comment on column CRM_T_IDU.C_PID                             is '主键自增';
comment on column CRM_T_IDU.C_QID                             is '报价单编号';
comment on column CRM_T_IDU.N_RCKMDL_NM                             is '每个模块机柜数量';
comment on column CRM_T_IDU.N_RCK_NM                             is '机柜数量';
comment on column CRM_T_IDU.C_ELEC_IN                             is '市电输入';
comment on column CRM_T_IDU.N_UPS_CPC                             is 'UPS额定容量';
comment on column CRM_T_IDU.C_IF_UPSPRRL                             is 'UPS是否需要并机冗余';
comment on column CRM_T_IDU.N_BCKBTT_TM                             is '电池后备时间';
comment on column CRM_T_IDU.C_IF_ELECIN                             is '输入配电单元是否需要预留';
comment on column CRM_T_IDU.C_IF_ELECOUT                             is '输出配电单元是否需要预留';
comment on column CRM_T_IDU.C_RCKELEC_CLC                             is '机柜供电回路';
comment on column CRM_T_IDU.C_RM_DSLLN                             is '机房布线';
comment on column CRM_T_IDU.C_BTTINS_TYP                             is '电池安装形式';
comment on column CRM_T_IDU.C_PDUOT_TYP                             is 'PDU输出插孔';
comment on column CRM_T_IDU.N_PDUOT_NM                             is 'PDU输出路数';
comment on column CRM_T_IDU.C_PDU_OPT                             is 'PDU选配功能';
comment on column CRM_T_IDU.C_IF_CLR                             is '是否需要空调制冷';
comment on column CRM_T_IDU.C_CLR_TYP                             is '空调制冷方式';
comment on column CRM_T_IDU.C_MCH_TYP                             is '机型选择';
comment on column CRM_T_IDU.C_MCH_PST                             is '室外机位置';
comment on column CRM_T_IDU.C_RDN_RQ                             is '冗余需求';
comment on column CRM_T_IDU.N_MCH_HGH                             is '室外机与室内机之间的高度差';
comment on column CRM_T_IDU.N_MCH_LNG                             is '室内机与室外机的管程长度';
comment on column CRM_T_IDU.C_IF_WTR                             is '是否有给排水';
comment on column CRM_T_IDU.C_IF_FAN                             is '是否需要应急风扇';
comment on column CRM_T_IDU.C_IF_FP                             is '是否需要消防';
comment on column CRM_T_IDU.C_SPC_RQ                             is '监控为默认配置有无特殊要求';
comment on column CRM_T_IDU.C_TYPE                          is '类型';


comment on table  CRM_T_GEN_CNT                                  is '报价申请合同表';
comment on column CRM_T_GEN_CNT.C_PID                             is '主键自增';
comment on column CRM_T_GEN_CNT.C_QID                             is '报价单编号';
comment on column CRM_T_GEN_CNT.C_CNT_ID                             is '合同编号';
comment on column CRM_T_GEN_CNT.C_CNT_NM                             is '合同名称';
comment on column CRM_T_GEN_CNT.C_CNT_TYP                             is '合同类型';
comment on column CRM_T_GEN_CNT.C_CRT_DT                             is '创建日期';


comment on table  CRM_T_PRJ_LST                                  is '工程清单表';
comment on column CRM_T_PRJ_LST.C_PID                             is '主键自增';
comment on column CRM_T_PRJ_LST.C_QID                             is '报价单编号';
comment on column CRM_T_PRJ_LST.C_TYPE                             is '类型';
comment on column CRM_T_PRJ_LST.C_PRD_PKG                             is '产品包';
comment on column CRM_T_PRJ_LST.C_PRD_NAME                             is '产品名称';
comment on column CRM_T_PRJ_LST.C_PRD_CTG                             is '产品分类';
comment on column CRM_T_PRJ_LST.C_PRD_TYP                             is '产品型号';
comment on column CRM_T_PRJ_LST.C_PRD_UNT                             is '单位';
comment on column CRM_T_PRJ_LST.N_PRD_SPRC                             is '定价';
comment on column CRM_T_PRJ_LST.N_PRD_PRC                             is '报价';
comment on column CRM_T_PRJ_LST.N_AMOUNT                             is '数量';
comment on column CRM_T_PRJ_LST.N_TTL_UNT                             is '单品总金额';
comment on column CRM_T_PRJ_LST.N_HH_TTL                             is '行汇总单价';
comment on column CRM_T_PRJ_LST.N_AVG_TTL                             is '平均总单价';
comment on column CRM_T_PRJ_LST.C_NSTD_RQ                             is '非标需求';
comment on column CRM_T_PRJ_LST.C_NOTES                             is '备注';


comment on table  CRM_T_PRJ_EVL                                  is '工程评审表';
comment on column CRM_T_PRJ_EVL.C_PID                             is '主键自增';
comment on column CRM_T_PRJ_EVL.C_QID                             is '报价单编号';
comment on column CRM_T_PRJ_EVL.C_TYPE                             is '类型';
comment on column CRM_T_PRJ_EVL.C_SEQ_NO                             is '序号';
comment on column CRM_T_PRJ_EVL.C_EVL_TYP                             is '评审类别';
comment on column CRM_T_PRJ_EVL.C_EVL_MM                             is '评审人';
comment on column CRM_T_PRJ_EVL.C_EVL_ST                             is '评审状态';
comment on column CRM_T_PRJ_EVL.DT_SBM_DT                             is '提交时间';
comment on column CRM_T_PRJ_EVL.C_EVL_RS                             is '评审结论';
comment on column CRM_T_PRJ_EVL.C_EVL_SG                             is '评审人意见';
comment on column CRM_T_PRJ_EVL.DT_FIN_DT                             is '评审完成时间';



comment on table  CRM_T_ATT                                  is '附件表';
comment on column CRM_T_ATT.C_PID                             is '主键自增';
comment on column CRM_T_ATT.C_QID                             is '编号';
comment on column CRM_T_ATT.C_TYPE                             is '类型';
comment on column CRM_T_ATT.C_DOC_NM                             is '文档名称';
comment on column CRM_T_ATT.C_DOC_CMT                             is '附件说明';
comment on column CRM_T_ATT.C_UPDR                             is '上传人';
comment on column CRM_T_ATT.DT_UPD_DT                             is '上传时间';
comment on column CRM_T_ATT.C_NOTES                             is '备注';


comment on table  CRM_T_SNG_UPS                                  is '单UPS';
comment on column CRM_T_SNG_UPS.C_PID                             is '主键自增';
comment on column CRM_T_SNG_UPS.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_UPS.C_TYPE                             is '类型';
comment on column CRM_T_SNG_UPS.C_ELEC_DRAFT                             is '配电系统图纸';
comment on column CRM_T_SNG_UPS.C_PRJTCH_RQ                             is '项目技术要求';
comment on column CRM_T_SNG_UPS.C_INOTLN_TYP                             is '进出线方式';
comment on column CRM_T_SNG_UPS.C_UPS_CNST                             is 'UPS系统组成';
comment on column CRM_T_SNG_UPS.N_BCKUP_TM                             is '后备时间';
comment on column CRM_T_SNG_UPS.C_MNT_RQ                             is '监控要求协议要求';
comment on column CRM_T_SNG_UPS.C_IP_LVL                             is 'IP等级';
comment on column CRM_T_SNG_UPS.C_OTHERS_RQ                             is '其他特殊需求';
comment on column CRM_T_SNG_UPS.C_IP_LVL_RQ                             is 'IP等级其它';


comment on table  CRM_T_SNG_BATTERY                                  is '单电池';
comment on column CRM_T_SNG_BATTERY.C_PID                             is '主键自增';
comment on column CRM_T_SNG_BATTERY.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_BATTERY.C_TYPE                             is '类型';
comment on column CRM_T_SNG_BATTERY.C_PRJTCH_RQ                             is '项目技术要求';
comment on column CRM_T_SNG_BATTERY.C_ENVLYT_DRFT                             is '现场布放图纸';
comment on column CRM_T_SNG_BATTERY.C_BTT_CP                             is '电池容量';
comment on column CRM_T_SNG_BATTERY.C_BTT_TNM                             is '电池总数量';
comment on column CRM_T_SNG_BATTERY.C_BTT_CNM                             is '电池组数';
comment on column CRM_T_SNG_BATTERY.C_IF_BTTMNT                             is '是否需要电池单体监控';
comment on column CRM_T_SNG_BATTERY.C_BTTMNT_TYP                             is '电池监控方式';
comment on column CRM_T_SNG_BATTERY.C_BTTINS_TYP                             is '电池安装形式';
comment on column CRM_T_SNG_BATTERY.C_BTTPS_TYP                             is '电池摆放方式';
comment on column CRM_T_SNG_BATTERY.C_BTTRCKLN_TYP                             is '电池柜架进出线方式';
comment on column CRM_T_SNG_BATTERY.N_BTTINS_HGH                             is '电池安装空间梁下净高';
comment on column CRM_T_SNG_BATTERY.C_BTTSWT_INS                             is '电池开关安装';
comment on column CRM_T_SNG_BATTERY.C_OTHERS_RQ                             is '其他特殊要求';
comment on column CRM_T_SNG_BATTERY.C_BTTSWT_RMT                             is '电池开关是否要远程控制';

comment on table  CRM_T_SNG_COOLER                                  is '单空调';
comment on column CRM_T_SNG_COOLER.C_PID                             is '主键自增';
comment on column CRM_T_SNG_COOLER.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_COOLER.C_PRJTCH_RQ                             is '项目技术要求';
comment on column CRM_T_SNG_COOLER.C_CLR_TYP                             is '空调制冷方式';
comment on column CRM_T_SNG_COOLER.C_WND_TYP                             is '空调送风方式';
comment on column CRM_T_SNG_COOLER.C_MCH_TYP                             is '机型选择';
comment on column CRM_T_SNG_COOLER.C_MCH_PST                             is '室外机位置';
comment on column CRM_T_SNG_COOLER.C_MCH_HGH                             is '室外机与室内机之间的高度差';
comment on column CRM_T_SNG_COOLER.C_MCH_LNG                             is '室内机与室外机的管程长度';
comment on column CRM_T_SNG_COOLER.C_OTHERS_NTS                             is '其他情况说明';
comment on column CRM_T_SNG_COOLER.C_IF_MNT                             is '是否需要监控';
comment on column CRM_T_SNG_COOLER.C_MNT_TYP                             is '监控方式';
comment on column CRM_T_SNG_COOLER.C_IF_DRP                             is '是否需要漏水检测';
comment on column CRM_T_SNG_COOLER.C_DRP_TYP                             is '漏水检测方式';
comment on column CRM_T_SNG_COOLER.C_CLR_IN_TMP                             is '水冷空调进水温度';
comment on column CRM_T_SNG_COOLER.C_CLR_OT_TMP                             is '水冷空调出水温度';

comment on table  CRM_T_SNG_ELEC                                  is '单配电';
comment on column CRM_T_SNG_ELEC.C_PID                             is '主键自增';
comment on column CRM_T_SNG_ELEC.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_ELEC.C_TYPE                             is '类型';
comment on column CRM_T_SNG_ELEC.C_LN_INOT                             is '进出线方式';
comment on column CRM_T_SNG_ELEC.C_MNT_TYP                             is '维护方式';
comment on column CRM_T_SNG_ELEC.C_FRDR_TYP                             is '开门方式（前门）';
comment on column CRM_T_SNG_ELEC.C_RRDR_TYP                             is '开门方式（后门）';
comment on column CRM_T_SNG_ELEC.C_FRDR                             is '前门型式';
comment on column CRM_T_SNG_ELEC.N_INOT_LN                             is '进出线电缆线径';
comment on column CRM_T_SNG_ELEC.C_RCK_SZ                             is '柜体尺寸';
comment on column CRM_T_SNG_ELEC.C_RCKPR_LVL                             is '柜体防护等级';
comment on column CRM_T_SNG_ELEC.C_SWT_BRD                             is '开关品牌';
comment on column CRM_T_SNG_ELEC.C_FP_TK                             is '消防分励脱扣';
comment on column CRM_T_SNG_ELEC.C_CLC_PLGIN                             is '微型断路器热插拔功能';
comment on column CRM_T_SNG_ELEC.C_ELEC_DRF                             is '提供配电系统图';
comment on column CRM_T_SNG_ELEC.C_TTLIN_SWT_CRT                             is '单电源总输入开关';
comment on column CRM_T_SNG_ELEC.C_ATS_SWT_CRT                             is '双电源总输入ATS开关';
comment on column CRM_T_SNG_ELEC.C_ELECOT_SWT_CRT                             is '市电输出开关';
comment on column CRM_T_SNG_ELEC.C_UPSIN_SWT_CRT                             is 'UPS输入开关';
comment on column CRM_T_SNG_ELEC.C_UPSOT_SWT_CRT                             is 'UPS输出开关';
comment on column CRM_T_SNG_ELEC.C_UPSBP_SWT_CRT                             is 'UPS旁路开关';
comment on column CRM_T_SNG_ELEC.C_FRT_UPS_CRT                             is '1UPS输出开关';
comment on column CRM_T_SNG_ELEC.C_SCD_UPS_CRT                             is '2UPS输出开关';
comment on column CRM_T_SNG_ELEC.C_OTHERS_RQ                             is '其他特殊需求';

comment on table  CRM_T_SNG_RACK                                  is '单机柜';
comment on column CRM_T_SNG_RACK.C_PID                             is '主键自增';
comment on column CRM_T_SNG_RACK.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_RACK.C_TYPE                             is '类型';
comment on column CRM_T_SNG_RACK.C_LN_INOT                             is '进出线方式';
comment on column CRM_T_SNG_RACK.C_RCK_SZ                             is '柜体尺寸';
comment on column CRM_T_SNG_RACK.C_FRDR_TYP                             is '开门方式（前门）';
comment on column CRM_T_SNG_RACK.C_RRDR_TYP                             is '开门方式（后门）';
comment on column CRM_T_SNG_RACK.C_FRRR_DR                             is '前后门型式';
comment on column CRM_T_SNG_RACK.C_SD_DR                             is '侧门';
comment on column CRM_T_SNG_RACK.C_RCKPR_LVL                             is '柜体防护等级';
comment on column CRM_T_SNG_RACK.C_SDDR_ATT                             is '侧门';
comment on column CRM_T_SNG_RACK.C_LN_SLOT                             is '理线槽';
comment on column CRM_T_SNG_RACK.C_BTTM                             is '底板';
comment on column CRM_T_SNG_RACK.C_LVL                             is '层板';
comment on column CRM_T_SNG_RACK.C_BLD                             is '盲板';
comment on column CRM_T_SNG_RACK.C_CLR_UT                             is '散热单元';
comment on column CRM_T_SNG_RACK.C_RCK_CMT                             is '机柜密闭组件';
comment on column CRM_T_SNG_RACK.C_OTHERS_RQ                             is '其他特殊需求';

comment on table  CRM_T_SNG_MNT                                  is '单监控';
comment on column CRM_T_SNG_MNT.C_PID                             is '主键自增';
comment on column CRM_T_SNG_MNT.C_QID                             is '报价单编号';
comment on column CRM_T_SNG_MNT.C_TYPE                             is '类型';
comment on column CRM_T_SNG_MNT.C_ALM_RQ                             is '告警需求';
comment on column CRM_T_SNG_MNT.C_PWR_MNT                             is '动力监控设备';
comment on column CRM_T_SNG_MNT.C_UPS                             is 'UPS';
comment on column CRM_T_SNG_MNT.C_CLR                             is '精密空调';
comment on column CRM_T_SNG_MNT.C_WND                             is '新风机';
comment on column CRM_T_SNG_MNT.C_JM_ELEC                             is '精密配电';
comment on column CRM_T_SNG_MNT.C_INT_ELEC                             is '智能配电';
comment on column CRM_T_SNG_MNT.C_DRP                             is '定位漏水';
comment on column CRM_T_SNG_MNT.C_ELEC_GNR                             is '智能发电机';
comment on column CRM_T_SNG_MNT.C_PWR_OTR                             is '其他(动力监控设备)';
comment on column CRM_T_SNG_MNT.C_ENV_MNT                             is '环境监控设备';
comment on column CRM_T_SNG_MNT.C_TMP                             is '温湿度';
comment on column CRM_T_SNG_MNT.C_SMK                             is '烟感';
comment on column CRM_T_SNG_MNT.C_IFR                             is '红外';
comment on column CRM_T_SNG_MNT.C_DR_LCK                             is '门磁';
comment on column CRM_T_SNG_MNT.C_NP_DRP                             is '不定位漏水';
comment on column CRM_T_SNG_MNT.C_MNT_OTR                             is '其他(环境监控设备)';
comment on column CRM_T_SNG_MNT.C_SEC_SYS                             is '安防系统';
comment on column CRM_T_SNG_MNT.C_DR_CNR                             is '门禁系统';
comment on column CRM_T_SNG_MNT.C_OTHERS_RQ                             is '其他特殊需求';






comment on table  CRM_T_BIDDC_EVL                                  is '标书审核表';
comment on column CRM_T_BIDDC_EVL.C_PID                             is '主键自增';
comment on column CRM_T_BIDDC_EVL.C_QID                             is '报价单编号';
comment on column CRM_T_BIDDC_EVL.C_TYPE                             is '类型';
comment on column CRM_T_BIDDC_EVL.C_CUR_PRSR                             is '当前处理人';
comment on column CRM_T_BIDDC_EVL.C_CUR_STTS                             is '当前状态';
comment on column CRM_T_BIDDC_EVL.C_EVL_SGT                             is '审核意见';
comment on column CRM_T_BIDDC_EVL.C_SN_PNT                             is '下级结点';



comment on table  CRM_T_MEMBER_INF                                  is '内部团队成员信息表';
comment on column CRM_T_MEMBER_INF.C_PID                             is '主键自增';
comment on column CRM_T_MEMBER_INF.C_QID                             is '编号';
comment on column CRM_T_MEMBER_INF.C_TYPE                             is '类型';
comment on column CRM_T_MEMBER_INF.C_MARK_DEPART                             is '销售部门';
comment on column CRM_T_MEMBER_INF.C_MEM_NAME                             is '团队成员名称';
comment on column CRM_T_MEMBER_INF.C_MARK_RESPON                             is '责任人';
comment on column CRM_T_MEMBER_INF.C_MARK_OP_UNT                             is '业务实体';
comment on column CRM_T_MEMBER_INF.C_ROLE                             is '团队成员角色';
comment on column CRM_T_MEMBER_INF.C_MARK_AUTH                             is '权限';
comment on column CRM_T_MEMBER_INF.C_MARK_VLD_FRM                             is '有效日期从';
comment on column CRM_T_MEMBER_INF.C_MARK_VLD_TO                             is '有效日期至';
comment on column CRM_T_MEMBER_INF.C_NOTES                             is '备注';



comment on table  CRM_T_ANL_SALTRT                                  is '年度目标维护表';
comment on column CRM_T_ANL_SALTRT.C_PID                             is '主键自增';
comment on column CRM_T_ANL_SALTRT.C_TYP                             is '类型';
comment on column CRM_T_ANL_SALTRT.C_DEP                             is '部门';
comment on column CRM_T_ANL_SALTRT.C_EMP                             is '销售员';
comment on column CRM_T_ANL_SALTRT.C_EMP_NM                          is '工号';
comment on column CRM_T_ANL_SALTRT.C_CURRENCY                        is '币种';
comment on column CRM_T_ANL_SALTRT.C_ANNUAL                          is '年度';
comment on column CRM_T_ANL_SALTRT.N_ANL_TRG                         is '年度目标';
comment on column CRM_T_ANL_SALTRT.N_JAN_TRG                         is '1月';
comment on column CRM_T_ANL_SALTRT.N_FEB_TRG                         is '2月';
comment on column CRM_T_ANL_SALTRT.N_MRC_TRG                         is '3月';
comment on column CRM_T_ANL_SALTRT.N_APR_TRG                         is '4月';
comment on column CRM_T_ANL_SALTRT.N_MAY_TRG                         is '5月';
comment on column CRM_T_ANL_SALTRT.N_JUN_TRG                         is '6月';
comment on column CRM_T_ANL_SALTRT.N_JUL_TRG                         is '7月';
comment on column CRM_T_ANL_SALTRT.N_AGT_TRG                         is '8月';
comment on column CRM_T_ANL_SALTRT.N_SEP_TRG                         is '9月';
comment on column CRM_T_ANL_SALTRT.N_OCT_TRG                         is '10月';
comment on column CRM_T_ANL_SALTRT.N_NOV_TRG                         is '11月';
comment on column CRM_T_ANL_SALTRT.N_DEC_TRG                         is '12月';




















