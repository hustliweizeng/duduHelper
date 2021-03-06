package com.dudu.duduhelper.http;

/**
 * 手机端参数常量 Created by Administrator on 2015/7/24.
 */
public class ConstantParamPhone 
{
	private ConstantParamPhone() 
	{
		
	}
	//版本号，通过判断版本号兼容老版本
    public static String VERSION="3.1";
	public static String IP="http://www.dolife.me";
    
    
	public static final String GET_SELL_CARD="/coupon";//get/post /coupon 查询优惠卡信息
	public static final String GET_MEMBER_CARD="/membercard";//GET /membercard 查询会员卡信息
    public static final String GET_GIFT_CARD="/gift";//GET /gift 查询礼品卡信息
    public static final String GET_HONGBAO_LIST="/rpList";//post 获取红包列表
    
    public static final String GET_COUPON_INFO="/coupon/info";//GET /goods/info 获取商品信息
    public static final String EDIT_COUPON_INFO="/coupon/update";//POST /goods/update 修改商品信息
    public static final String GET_FULL_LIST="/order/fulllist";//GET /order/list 获取所有商品订单
    public static final String GET_ALLORDER_LIST="/order/list";//GET /order/list 获取所有商品订单
    public static final String GET_NEWORDER_LIST="/order/newlist";//GET /order/newlist 获取新商品订单-当天
    public static final String GET_CASH_LIST="/cashier";//获取提现列表
    public static final String GET_HONGBAOHIST_LIST="/getRpl";//获取红包使用记录列表
    public static final String GET_CASH_DETAIL= "/cashier/detailnew";// 收款详情
    
    public static final String GET_SHOPE_CODE="/shop/qrcode"; //获取店铺首页二维码
    public static final String GET_CASHIER_CODE="/cashier/qrcode"; //获取店铺收银台二维码// 收款详情
    
    public static final String GET_COUPON_RECORD_LIST="/coupon/record/list";//coupon/record/list 获取优惠券领取记录
    public static final String GET_COUPON_RECORD_INFO="/coupon/record/info";
    public static final String GET_SCAN_ORDER="/getOrder";//POST /getOrder 查询刷卡支付订单

	/**
	 * 新接口地址
     */
    public static final String BASE_URL = "http://dev.dolife.me";//"http://dev.dolife.me";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String SWITCH_SHOP = "/api/app/user/store/";
    
    public static final String FORGET_PWD ="/api/app/tool/getpwdsms";//忘记密码发送验证码
    public static final String FIX_PWD = "/api/app/user/getpassword";//重置密码

    //红包
    public static final String USER_LOGIN = "/api/app/user/login";//用户登录接口
    public static final String GET_REDBAG_LIST = "/api/app/red_packet";//获取红包列表
    public static final String GET_REDBAG_DETAIL ="/api/app/red_packet/";//获取红包详情
    public static final String EDIT_REDBAG ="/api/app/red_packet/";//编辑红包
    public static final String ADD_REDBAG = "/api/app/red_packet";//添加红包
    public static final String DEL_REDBAG ="/api/app/red_packet/";//删除红包
    public static final String REDBAG_HISTORY = "/api/app/red_packet/";//红包领取记录
 
 
 
    public static final String UPLOAD_PIC = "/api/app/tool/uploadpic";//上传图片
    public static final String GET_USER_INFO = "/api/app/user/info";//获取用户信息
    public static final String  GET_SMS_CONFIRM = "/api/app/tool/sms"; //短信验证
    public static final String  CHANGE_PWD_BYSMS ="/api/app/user/changepwdbycode";//通过验证码修改密码
    public static final String  CHANGE_PWD_BYPWD = "/api/app/user/changepwdbypwd";//通过旧密码修改
    public static final String  LOG_OUT = "/api/app/user/logout";//退出登录
    public static final String  CHECK_SHOP = "/api/app/user/store/";//d选择店铺


    //银行卡相关接口
    public static final String  GET_BANKCARD_LIST = "/api/app/bank";//获取银行卡信息列表
    public static final String  ADD_BANKCARD = "/api/app/bank";//添加银行卡信息
    public static final String  CHANGE_BANKCARD_INFO = "/api/app/bank/";//修改银行卡信息
    public static final String  DEL_BANKCARD = "/api/app/bank/";//删除银行卡信息

   //收银相关
   public static final String  CREATE_PAY_PIC = "/api/app/cashier";//创建收银二维码
    public static final String GET_CASH_HISTORY ="/api/app/cashier";//获取账单流水
    public static final String PAY_BY_CAMERA = "/api/app/cashier/card";//扫码支付
    public static final String CREATE_PAYMENT_ORDER = "/api/app/cashier/order";//生成扫码支付订单
    public static final String GET_PAYMENT_RESULT = "/api/app/cashier/query";//查询付款结果
    

    //提现
    public static final String  CREATE_GET_MONEY ="/api/app/outmoney";//创建提现
    public static final String  GET_OUT_MONEY_LIST ="/api/app/outmoney";//获取提现列表

   //订单列表
   public static final String  GET_ORDER_LIST = "/api/app/order";//获取订单列表
    public static final String SEARCH_ORDER = "/api/app/order/search";//搜索订单
    public static final String GET_ORDER_DETAIL = "/api/app/order/";//订单详情
    public static final String CHANGE_ORDER_STATUS ="/api/app/order/changestatus";//改变状态


    //获取省份
    public static final String  GET_PROVINCE_LIST = "/api/app/tool/geo/province";//获取省份列表
    public static final String  GET_CITY_LIST = "/api/app/tool/geo/city";//获取城市列表
    public static final String  GET_COUNTRY_LISY= "/api/app/tool/geo/county";//获取县区列表

    //获取店铺
    public static final String  GET_SHOP_INFO = "/api/app/shop/info";//获取店铺信息
    public static final String  GET_CATEGPRY_INFO = "/api/app/shop/category";//获取行业分类信息
    public static final String  GET_SHOPCIRCLE_INFO = "/api/app/shop/area";//获取商圈信息
    public static final String  SAVE_SHOP_INFO = "/api/app/shop/info";//保存店铺信息
    public static final String  GET_SHOP_WXPIC ="/api/app/shop/cashier";//获取店铺二维码

    //大牌抢购
    public static final String GET_BIG_BAND_LIST = "/api/app/panic/listinfo";//大牌抢购列表
    public static final String GET_BIG_BAND_DETAIL = "/api/app/panic/panicinfo";//大牌抢购详情，也是大牌抢购编辑的接口
    public static final String DEL_BIG_BAND ="/api/app/panic/delpanic";//删除大牌抢购条目
    public static final String SWITCH_STATUS ="/api/app/panic/checkStatus";//切换商品上下架状态
    

   //优惠券列表
   public static final String GET_DISCOUNT_LIST = "/api/app/coupon/listinfo";//获取优惠券信息列表
   public static final String  GET_DISCOUT_DETAIL = "/api/app/coupon/couponinfo";//优惠券详情/api/app/coupon/couponinfo
   public static final String  DEL_DISECOUNT = "/api/app/coupon/delcoupon";//删除优惠券
    public static final String SWITCH_DIS_STATUS ="/api/app/coupon/checkCoupon";//切换商品上下架状态
    
    //产品信息描述
    public static final String GET_APP_INFO = "/api/app/tool/geo/boot";//获取产品信息

   //解绑手机
    public static final String UNBIND_PHONE ="/api/app/user/unbindphone";//解绑手机
    public static final String BIND_PHONE = "/api/app/user/bindphone";//绑定手机
   //核销
   public static final String  GET_CHECK_TICKET= "/api/app/ticket/";//查询券码
   public static final String  VERTIFY_TICKET="/api/app/ticket/";//核销券码
   public static final String  GET_VERTTIFY_HISTROY = "/api/app/ticket";//核销记录
    
    //获取门店信息
    public static final String GET_SHOP_LIST ="/api/app/branch_shop";//店铺列表
    public static final String ADD_SHOP ="/api/app/branch_shop";//添加店铺
    public static final String GET_SHOP_DETAIL ="/api/app/branch_shop/";//获取门店详情
    public static final String EDIT_SHOP_DETAIL ="/api/app/branch_shop/";//修改店铺详情
    
    //店员列表
    public static final String GET_SHOP_USER ="/api/app/shop_user";//店员列表
    public static final String GET_USER_DETAIL = "/api/app/shop_user/";//店员详情
    public static final String ADD_USER ="/api/app/shop_user";//添加店员
    public static final String EDIT_USER ="/api/app/shop_user/";//编辑店员
    public static final String DEL_USER ="/api/app/shop_user/";//删除店员
    public static final String GET_SHOPABLE ="/api/app/shop_user/shops";//获取可选店铺
 
  //五折卡验证
    public static final String  CHECK_CARD = "/api/app/half/index";//是否开通
   public static final String VERTIFY_CARD ="/api/app/half/valCode";//验证五折卡
   public static final String USE_CARD ="/api/app/half/useCard";//使用五折卡
  //统计
   public static final String GET_SUM = "/api/app/stat/date";//获取统计数据
 
 //客户管理
   public static final String  GET_GUEST_ISOPEN ="/api/app/custom/status";//获取客户管理是否开启
   public static final String  GET_GUEST_LIST = "/api/app/custom/members";//获取用户列表
   public static final String  GET_SHOP_STATUS ="/api/app/custom/statistics";//获取商家状态
   public static final String  GET_SEND_RECORD ="/api/app/custom/send_logs";//获取发送记录
   public static final String  GET_REDBAG_DETAILS ="/api/app/custom/red_packet/{id}";//获取红包详情
   public static final String  SEND_MESSAGE ="/api/app/custom/message";//发送消息
    public static final String  GET_USER_LIST ="/api/app/custom/members";//获取用户列表
    public static final String  GET_PAYORDER = "/api/app/custom/buy";//获取预支付订单
   //会员管理
    public static final String GET_VIP_ISOPEN = "/api/app/vip/status";//获取会员功能是否开启
    public static final String GET_VIP_HISTORY = "/api/app/vip/logs";//获取验证记录
    public static final String GET_VIP_INFO = "/api/app/vip/";//获取会员记录
    public static final String USE_VIP ="/api/app/vip/";//使用会员卡





}
