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
    public static String VERSION="2.3.1";
//	public static String IP="https://business.duduapp.net/api";
	public static String IP="http://business.dev.duduapp.net/api";
    
//    public static String IP="http://dev.dolife.me/api/app";
//    public static String SUCCESS = "SUCCESS";
    
	// 相关方法名 methodname
	//public static final String USER_INFO = "/user/info";//登录,获取用户信息
	//public static final String SALER_INFO ="/saler/info";//登录，获取店员信息
	public static final String ACCOUNT_DATA="/stat/query";//GET /stat/query 根据时间查询店铺统计信息
	public static final String GET_SELL_CARD="/coupon";//get/post /coupon 查询优惠卡信息
	public static final String GET_MEMBER_CARD="/membercard";//GET /membercard 查询会员卡信息
    public static final String GET_GIFT_CARD="/gift";//GET /gift 查询礼品卡信息
    public static final String GET_USER_BANK="/user/bank";//GET /user/bank 获取支持的银行列表
    public static final String GET_USER_BANK_PROVINCE="/geo/province";//GET /geo/province 获取省份列表
    public static final String GET_USER_BANK_CITY="/geo/city";//GET /geo/city 获取城市列表
    public static final String EDIT_USER_BANK="/user/bank";//POST /user/bank 修改用户的银行卡信息
    public static final String GET_CASH_HISTORY="/user/outmoney";//GET /user/outmoney 获取提现记录
    public static final String SEND_SMS="/sms/send";//POST /sms/send 短信发送接口
    public static final String BIND_PHONE="/user/bindmobile";//POST /user/bindmobile 手机绑定操作
    public static final String UNBIND_PHONE="/user/unbindmobile";//POST /user/unbindmobile 手机解绑操作
    public static final String CHECK_OLD_PHONE="/user/checkoldmobile";//GET /user/checkoldmobile 验证老手机解绑验证码
    public static final String CHANGE_PHONE="/user/changemobile";//POST /user/changemobile 更改手机号
   // public static final String GET_USER_INFO="/user/token";//GET /user/token 根据token刷新用户信息
    public static final String GET_SALER_INFO="/saler/token";//GET /user/token 根据token刷新用户信息
    public static final String OUT_USER_MONEY="/user/outmoney";//POST /user/outmoney 用户提现操作
    public static final String GET_PRODUCT_LIST="/goods/list";//GET /goods/list 获取商品列表
    public static final String GET_COUPON_LIST="/coupon/list";//GET /goods/list 获取商品列表
    public static final String GET_HONGBAO_LIST="/rpList";//post 获取红包列表
    public static final String DELECT_PRODUCT="/goods/delete";//GET /goods/delete 删除商品信息
    public static final String GET_PRODUCT_INFO="/goods/info";//GET /goods/info 获取商品信息
    public static final String EDIT_PRODUCT_INFO="/goods/update";//POST /goods/update 修改商品信息
    public static final String GOODS_MULIT="/goods/mulit";//POST /goods/mulit 批量操作
    
    public static final String EDIT_HONGBAO="/editRp";//POST /editRp 编辑红包
    public static final String ADD_HONGBAO="/addRp";//POST /POST /addRp 编辑红包
    public static final String DELECT_HONGBAO="/delRp";//POST /goods/delete 删除商品信息
    
    public static final String DELECT_COUPON="/coupon/delete";//GET /goods/delete 删除商品信息
    public static final String GET_COUPON_INFO="/coupon/info";//GET /goods/info 获取商品信息
    public static final String EDIT_COUPON_INFO="/coupon/update";//POST /goods/update 修改商品信息
    public static final String COUPON_MULIT="/coupon/mulit";//POST /goods/mulit 批量操作
    public static final String GET_FULL_LIST="/order/fulllist";//GET /order/list 获取所有商品订单
    public static final String GET_ALLORDER_LIST="/order/list";//GET /order/list 获取所有商品订单
    public static final String GET_NEWORDER_LIST="/order/newlist";//GET /order/newlist 获取新商品订单-当天
    public static final String GET_ORDER_DETAIL="/order/info";//GET /order/info 获取商品信息
    public static final String EDIT_ORDER_STATUS="/order/status";//POST /order/status 修改状态
    public static final String GET_VERSION="/version/android";//GET /version/android 安卓系统版本
    public static final String ORDER_SEARCH="/order/search";//POST /goods/mulit 批量操作
    public static final String GET_URL="/system/boot";//获取网页
    public static final String GET_CASH_LIST="/cashier";//获取提现列表
    public static final String GET_HONGBAOHIST_LIST="/getRpl";//获取红包使用记录列表
    public static final String GET_CASH_DETAIL= "/cashier/detailnew";// 收款详情
    
    public static final String GET_SHOPE_CODE="/shop/qrcode"; //获取店铺首页二维码
    public static final String GET_CASHIER_CODE="/cashier/qrcode"; //获取店铺收银台二维码// 收款详情
    
    public static final String GET_COUPON_RECORD_LIST="/coupon/record/list";//coupon/record/list 获取优惠券领取记录
    public static final String GET_COUPON_RECORD_INFO="/coupon/record/info";
    public static final String GET_MEMBER_LIST="/saler/list";
    public static final String ADD_MEMBER="/saler/add";
    public static final String EDIT_MEMBER="/saler/edit";
    public static final String COUPON_RECORD_SEARCH="/coupon/record/search";
    public static final String MEMBER_DELECT="/saler/delete";
    public static final String MEMBER_DETAIL="/saler/detail";
    
    public static final String GET_SELL_DISCOUNT="/valCode";//POST /valCode 验证五折卡
    public static final String USE_SELL_DISCOUNT="/useCard";//POST /useCard 使用五折卡
    public static final String GET_FIRST_CARD="/getCardPre";//POST /getCardPre 获取五折卡前缀
    
    public static final String SCAN_CASH_ORDER="/order";//POST /order 创建刷卡支付订单
    public static final String GET_SCAN_ORDER="/getOrder";//POST /getOrder 查询刷卡支付订单

	/**
	 * 新接口地址
     */
    //public static final String USER_LOGIN = "/user/login";
    public static final String BASE_URL = "http://dev.dolife.me";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";





    public static final String USER_LOGIN = "/api/app/user/login";//用户登录接口
    public static final String CREATE_PAYMENT = "/cashier";//收款列表和创建收款
    public static final String GET_REDBAG_LIST = "/rpList";//获取红包列表
    public static final String ADD_REDBAG = "/addRp";//添加红包
    public static final String UPLOAD_PIC = "/api/app/tool/uploadpic";//上传图片
    public static final String GET_ORDER_LIST = "/order/fulllist";//添加红包
    public static final String GET_USER_INFO = "/api/app/user/info";//获取用户信息
    public static final String SAVE_SHOP_INFO = "/api/app/shop/info";//保存店铺信息
    public static final String  GET_SMS_CONFIRM = "/api/app/tool/sms"; //短信验证
    public static final String  CHANGE_PWD_BYSMS ="/api/app/user/changepwdbycode";//通过验证码修改密码
    public static final String  CHANGE_PWD_BYPWD = "/api/app/user/changepwdbypwd";//通过旧密码修改
    public static final String  LOG_OUT = "/api/app/user/logout";//退出登录
    public static final String  GET_CATEGPRY_INFO = "/api/app/shop/category";//获取行业分类信息
    public static final String  GET_SHOPCIRCLE_INFO = "/api/app/shop/area";//获取商圈信息

    //银行卡相关接口
    public static final String  GET_BANKCARD_LIST = "/api/app/bank";//获取银行卡信息列表
    public static final String  ADD_BANKCARD = "/api/app/bank";//添加银行卡信息
    public static final String  CHANGE_BANKCARD_INFO = "/api/app/bank/1";//修改银行卡信息
    public static final String  DEL_BANKCARD = "/api/app/bank/2";//删除银行卡信息










}
