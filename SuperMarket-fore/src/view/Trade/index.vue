<template>
  <div class="trade-container">
    <h3 class="title">填写并核对订单信息</h3>
    <div class="content">
      <h5 class="receive">收件人信息</h5>
      <div class="address clearFix" v-for="(address, index) in addressList" :key="address.id">
        <span class="username " :class="{ selected: address.defaultStatus == 1 }">{{
          address.name
        }}</span>
        <p>
          <span class="s1">{{ fullAddress(address) }}</span>
          <span class="s2">{{ address.phone }}</span>
          <span class="s3" v-show="address.defaultStatus == 1">默认地址</span>
          <span class="s4" v-show="address.defaultStatus != 1" @click="changeDefault(address)">设为默认地址</span>
        </p>
      </div>
      <div class="line"></div>
      <h5 class="pay">支付方式</h5>
      <div class="address clearFix">
        <span class="username selected">在线支付</span>
        <span class="username" style="margin-left:5px;">货到付款</span>
      </div>
      <div class="line"></div>
      <h5 class="pay">送货清单</h5>
      <div class="way">
        <h5>配送方式</h5>
        <div class="info clearFix">
          <span class="s1">天天快递</span>
          <p>配送时间：预计8月10日（周三）09:00-15:00送达</p>
        </div>
      </div>
      <div class="detail">
        <h5>商品清单</h5>
        <ul class="list clearFix" v-for="(product, index) in cart.items" :key="product.skuId">
          <li>
            <img :src="product.image" alt="" style="width:100px;height:100px" />
          </li>
          <li>
            <p>{{ product.title }}</p>
            <h4>7天无理由退货</h4>
          </li>
          <li>
            <h3>￥{{ product.totalPrice }}.00</h3>
          </li>
          <li>X{{ product.count }}</li>
          <li v-if="product.hasStock">有货</li>
          <li v-if="!product.hasStock">无货</li>
        </ul>
      </div>
      <div class="bbs">
        <h5>买家留言：</h5>
        <textarea placeholder="建议留言前先与商家沟通确认" class="remarks-cont" v-model="submit.remark"></textarea>
      </div>
      <div class="line"></div>
      <div class="bill">
        <h5>发票信息：</h5>
        <div>普通发票（电子） 个人 明细</div>
        <h5>使用优惠/抵用</h5>
      </div>
    </div>
    <div class="money clearFix">
      <ul>
        <li>
          <b><i>{{ cart.countType }}</i>类共<i>{{ cart.countNum }}</i>件商品，总商品金额</b>
          <span>¥{{ cart.resAmount }}.00</span>
        </li>
        <li>
          <b>返现：</b>
          <span>0.00</span>
        </li>
        <li>
          <b>运费：</b>
          <span>{{transport.fee}}.00</span>
        </li>
      </ul>
    </div>
    <div class="trade">
      <div class="price">
        应付金额: <span>¥{{ order.price }}.00</span>
      </div>
      <div class="receiveInfo">
        寄送至:
        <span>{{ fullAddress(selectedAddress) }}</span>&nbsp;
        收货人：<span>{{ selectedAddress.name }}</span>&nbsp;
        联系方式：<span>{{ selectedAddress.phone }}</span>
      </div>
    </div>
    <div class="sub clearFix">
      <a class="subBtn" @click="submitOrder">提交订单</a>
    </div>
  </div>
</template>

<script>
import { reqConfirmOrder } from "@/api/cart";
import { reqUpdateAddressState } from "@/api/user";
import { reqSubmitOrder } from "@/api/order";
import index from "@/mixin/index";
export default {
  name: "Trade",
  data() {
    return {
      //收集买家的留言信息
      msg: "",
      //订单号
      orderId: "",

      cart: {},
      addressList: [],
      transport: {},
      order: {},

      submit: {
        addressId: "",
        token: "",
        payType: 1,
        payablePrice: "",
        remark: "",
      },
    };
  },
  mixins: [index],
  //生命周期函数:挂载完毕
  mounted() {
    this.getConfirmOrderInfo();
  },
  computed: {
    //将来提交订单最终选中地址
    selectedAddress() {
      return this.transport.address || {};
    },
    fullAddress() {
      return function (address) {
        let detailAddress = "";
        if (address.province) {
          detailAddress += address.province + " ";
        }
        if (address.city) {
          detailAddress += address.city + " ";
        }
        if (address.region) {
          detailAddress += address.region + " ";
        }
        if (address.detailAddress) {
          detailAddress += address.detailAddress + " ";
        }
        return detailAddress;
      };
    },
  },
  methods: {
    async getConfirmOrderInfo() {
      const res = await reqConfirmOrder();
      if (res.code === 200) {
        this.addressList = res.data.addressList;
        this.cart = res.data.cart;
        this.price = res.data.price;
        this.transport = res.data.transport;
        this.order = res.data;
      } else {
        this.$message.error({
          title: "失败",
          message: res.msg,
        });
      }
    },
    //修改默认地址
    async changeDefault(address) {
      const res = await reqUpdateAddressState(address.id);
      this.showMessage(res);
      if (res.code === 200) {
        this.getConfirmOrderInfo();
      }
    },
    //提交订单
    async submitOrder() {
      let submit = this.submit;
      submit.addressId = this.selectedAddress.id;
      submit.token = this.order.orderToken;
      submit.payablePrice = this.order.price;
      const res = await reqSubmitOrder(submit);
      //提交订单成功
      if (res.code == 200) {
        const orderId = result.data;
        if (orderId) {
          //路由跳转 + 路由传递参数
          this.$router.push("/pay?orderId=" + orderId);
        }

        //提交的订单失败
      } else {
        this.$message.error({
          title: "失败",
          message: res.msg,
        });
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
