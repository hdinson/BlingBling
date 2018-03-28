package dinson.customview.utils;

import android.content.Context;
import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * 格式化
 */
public class NlinksParkUtils {

    public static String formatPlateState(int state) {
        switch (state) {
            case -1:
                return "认证失败";
            case 0:
                return "去认证";
            case 1:
                return "已完成";
            case 2:
                return "审核中";
            case 3:
                return "申诉中";
            case 4:
                return "未认证解绑";
            case 5:
                return "认证后解绑";
            default:
                return "";
        }
    }

    public static String formatSex(int state) {
        switch (state) {
            case 0:
                return "未设置";
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "";
        }
    }

    /**
     * 格式化钱包明细字段
     * 1钱包支付、2微信支付，3支付宝、4线下支付
     */
    public static String formatPayChannel(int state) {
        switch (state) {
            case 1:
                return "钱包支付";
            case 2:
                return "微信支付";
            case 3:
                return "支付宝支付";
            case 4:
                return "线下支付";
            default:
                return "";
        }
    }


   /* *//**
     * recordStatus:1待缴费2.预缴3.已完成4.欠费5.等待离场
     *//*
    public static RecordStateBean formatParkRecordState(ParkRecord parkRecord) {
        switch (parkRecord.getRecordStatus()) {
            case 1:
                return new RecordStateBean("待缴费", R.color.colorPrimary, R.mipmap.ic_parking);
            case 2:
                return new RecordStateBean("预缴", R.color.colorPrimary, R.mipmap.ic_parking);
            case 3:
                return new RecordStateBean("已完成", R.color.text_secondary, R.mipmap.ic_wait_leave);
            case 4:
                return new RecordStateBean("欠费", R.color.state_red, R.mipmap.ic_no_pay);
            case 5:
                return new RecordStateBean("等待离场", R.color.state_red, R.mipmap.ic_wait_leave);
            default:
                return new RecordStateBean("", R.color.white, R.mipmap.ic_parking);
        }
    }


    *//**
     * 格式化共享车位状态
     *//*
    public static ShareParkStateBean formatShareParkState(int auditStatus, int sharingStatus) {
        switch (auditStatus) {
            case 1:
                return new ShareParkStateBean("审核中", R.color.share_grey, R.mipmap.ic_no_check, "车位正在审核中");
            case 2:
                return new ShareParkStateBean("审核失败", R.color.share_red, R.mipmap.ic_share_error, "车位审核失败");
            case 3:
                if (sharingStatus == 1)
                    return new ShareParkStateBean("待发布", R.color.share_blue, R.mipmap.ic_release,
                        "车位正处于待发布状态");
                if (sharingStatus == 2)
                    return new ShareParkStateBean("发布中", R.color.share_grey, R.mipmap.ic_no_check,
                        "车位正处于发布状态中");
                if (sharingStatus == 3)
                    return new ShareParkStateBean("已出租", R.color.share_green, R.mipmap.ic_sent_out, "车位已出租");
                return new ShareParkStateBean("审核失败", R.color.share_red, R.mipmap.ic_share_error, "车位审核失败");
            default:
                return new ShareParkStateBean("审核失败", R.color.share_red, R.mipmap.ic_share_error, "车位审核失败");
        }
    }

    *//**
     * 格式化预约记录状态
     * 1未驶入 2未驶出 3已超时 4已完成
     *//*
    public static String formatAppointRecordState(AppointmentRecord appoint) {
        switch (appoint.getStatus()) {
            case 1:
                return "未驶入";
            case 2:
                return "未驶出";
            case 3:
                return "已超时";
            case 4:
                return "已完成";
            default:
                return "未知状态";
        }
    }*/

   /* public static void resetImageButton(int state, ImageButton ib) {
        switch (state) {
            case 1://落杆状态
                LogUtils.i("当前地锁的状态：落杆状态");
                if (ib.getVisibility() != View.VISIBLE) ib.setVisibility(View.VISIBLE);
                ib.setImageResource(R.drawable.selector_locker_up);
                break;
            case 2://起杆状态
                LogUtils.i("当前地锁的状态：起杆状态");
                if (ib.getVisibility() != View.VISIBLE) ib.setVisibility(View.VISIBLE);
                ib.setImageResource(R.drawable.selector_locker_down);
                break;
            case 3://异常状态
                LogUtils.i("当前地锁的状态：异常状态");
                if (ib.getVisibility() != View.GONE) ib.setVisibility(View.GONE);
                break;
        }
    }

    *//**
     * 加密
     *
     * @param str String
     * @return byte[]
     *//*
    public static String encrypt(String str) {
        try {
            byte[] datasource = str.getBytes();
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(AppConst.DESKEY.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return ByteUtils.byteTobyteString(cipher.doFinal(datasource), false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    *//**
     * 解密
     *//*
    public static String decrypt(String str) {
        try {
            byte[] src = ByteUtils.byteStringTobyte(str);
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(AppConst.DESKEY.getBytes());

            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            byte[] bytes = cipher.doFinal(src);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


   /**
     * 获取优惠金额
     *
     * @param coupon 优惠券类型
     * @param totle  总金额
     * @return
     */
   /* public static double getCouponMoney(ParkingCoupon coupon, double totle) {
        if (coupon == null) return 0;
        if (coupon.getCouponType() == 1) {
            return coupon.getCouponAmount();
        } else if (coupon.getCouponType() == 2) {
            return totle * (1 - coupon.getCouponDiscount());
        } else {
            return 0;
        }
    }*/

    public static String plateAddDot(String plate) {
        String last = plate.substring(2);
        String front = plate.substring(0, 2);
        return front + " • " + last;
    }

    public static String formatParkType(int type) {
        return type == 1 ? "路内" : "路外";
    }

   /* public static MonthlyStateBean formatMonthlyState(int state) {
        switch (state) {
            case 0:
                return new MonthlyStateBean("未支付", R.color.colorPrimary);
            case 1:
                return new MonthlyStateBean("未生效", R.color.colorPrimary);
            case 2:
                return new MonthlyStateBean("生效中", R.color.coupon_yellow);
            case 3:
                return new MonthlyStateBean("已过期", R.color.share_grey);
            default:
                return new MonthlyStateBean("未知", R.color.share_grey);
        }
    }*/

    public static String formatRuleTime(String ruleTime) {
        if (StringUtils.isEmpty(ruleTime)) return "";
        if (ruleTime.equals("00:00:00-00:00:00")) ruleTime = "24小时";
        if (ruleTime.contains(",")) {
            String[] split = ruleTime.split(",");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("00:00:00-00:00:00")) split[i] = "24小时";

                if (i != split.length - 1) {
                    result.append(split[i]).append("\n");
                } else {
                    result.append(split[i]);
                }
            }
            return result.toString();
        } else {
            return ruleTime;
        }
    }

    /*public static void createTextView(Context context, LinearLayout container, String attentionStr) {
        if (StringUtils.isEmpty(attentionStr)) return;
        String result;
        if (attentionStr.contains("；") | attentionStr.contains(";")) {
            String[] split = attentionStr.trim().split("；|;");
            for (String s : split) {
                container.addView(createTextView(context, s));
            }
        } else {
            result = attentionStr;
            container.addView(createTextView(context, result));
        }
    }*/

    /*private static TextView createTextView(Context context, String str) {
        TextView tv = new TextView(context);
        tv.setText(str);
        Drawable drawable = context.getResources().getDrawable(R.drawable.grey_point_attention);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setGravity(Gravity.TOP);
        tv.setCompoundDrawablePadding(UIUtils.dip2px(5));
        tv.setTextColor(context.getResources().getColor(R.color.text_secondary));
        tv.setPadding(0, 8, 0, 8);
        return tv;
    }
*/

    /**
     * 支付类型
     */
    public enum PayType implements Serializable {
        WALLET_RECHARGE(1, "钱包充值"),
        PARKPAY(2, "停车缴费"),
        APPOINTPAY(3, "预约缴费"),
        APPOINTPAY_OVERTIME(4, "预约超时缴费"),
        PARKPAY_PREPAY(5, "停车预缴"),
        OFFROAD_PARKPAY(6, "路外停车缴费"),
        GOLD_MEMBER(7, "会员升级"),
        MONTHLY_PAY(8, "错峰包月"),
        SHARE_PARKING(10, "共享停车");

        private int value;
        private String desc;


        PayType(int v, String desc) {
            this.value = v;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static PayType formatPayType(int code) {
        for (PayType type : PayType.values()) {
            if (code == type.getValue()) {
                return type;
            }
        }
        return null;
    }

    /**
     * 是否包含支付类型
     *
     * @param payTypeValue 2,3（格式）
     */
    public static boolean isContainPayType(String payTypeValue, int type) {
        try {
            String[] split = payTypeValue.split(",");
            for (String s : split) {
                int i = Integer.parseInt(s);
                if (i == type) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 优惠券是否包含parkCode
     *
     * @param coupon 用逗号隔开
     *//*
    public static boolean isContainParkCode(ParkingCoupon coupon, String parkCode) {
        if (StringUtils.isEmpty(parkCode)) return true;//为空表示支持所有停车场
        String[] spl = coupon.getParkCode().split(",");
        for (String s : spl) {
            if (s.equals(parkCode)) return true;
        }
        return false;
    }
*/

    /**
     * 格式化钱包明细--交易描述 (与支付类型相同)
     */
    public static String formatTransactDesc(int state) {
        PayType payType = formatPayType(state);
        if (payType == null) return "未知支付方式";
        return payType.getDesc();
    }


   /* *//**
     * 选择最佳的优惠券
     *
     * @param extra 优惠券验证是否可用实体
     *//*
    public static void chooseGreatCoupon(Context context, final CouponValidateExtra extra,
                                         final OnChooseGreatListener listener) {
        String userId = SPUtils.getUserId(context);
        if (StringUtils.isEmpty(userId)) return;
        HttpHelper.getRetrofit().create(PayCouponAPI.class).getAllUsablePayCoupon(userId)
            .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<List<ParkingCoupon>>() {
            @NonNull
            @Override
            public void onHandleSuccess(List<ParkingCoupon> listHttpResult) {
                ParkingCoupon parkingCoupon = filterCoupon(listHttpResult, extra);
                if (parkingCoupon != null) {
                    listener.onGreat(parkingCoupon);
                }
            }
        });
    }

    *//**
     * 过滤出最佳的优惠券
     *//*
    public static ParkingCoupon filterCoupon(List<ParkingCoupon> list, CouponValidateExtra extra) {
        if (list == null || list.isEmpty()) return null;
        int pos = -1;
        double bestCouponMoney = 0;
        for (int i = 0; i < list.size(); i++) {
            ParkingCoupon coupon = list.get(i);
            if (extra.validate(coupon)) {
                //包含指定的支付类型和支持所在停车场
                double couponMoney = getCouponMoney(coupon, extra.getConsume());
                if (couponMoney > 0 && couponMoney >= bestCouponMoney) {
                    //找到最低金额
                    pos = i;
                    bestCouponMoney = couponMoney;
                }
            }
        }
        return pos == -1 ? null : list.get(pos);
    }*/
}
