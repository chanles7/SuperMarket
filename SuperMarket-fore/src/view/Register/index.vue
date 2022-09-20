<template>
  <div class="register-container">
    <!-- 注册内容 -->
    <div class="register">
      <h3>
        注册新用户
        <span class="go">我有账号，去 <a href="login.html" target="_blank">登陆</a>
        </span>
      </h3>
      <div class="content">
        <label>手机号:</label>
        <input placeholder="请输入你的手机号" v-model="phone" name="phone" v-validate="{ required: true, regex: /^1\d{10}$/ }" :class="{ invalid: errors.has('phone') }" />
        <span class="error-msg">{{ errors.first("phone") }}</span>
      </div>
      <div class="content">
        <label>验证码:</label>
        <input placeholder="请输入你的验证码" v-model="code" name="code" v-validate="{ required: true, regex: /^\d{6}$/ }" :class="{ invalid: errors.has('code') }" />
        <button v-if="isShow" style="width:100px;height:38px" @click="getCode">
          获取验证码
        </button>
        <button v-if="!isShow" style="width:100px;height:38px">
          <span>{{count}}</span> 秒
        </button>
        <span class="error-msg">{{ errors.first("code") }}</span>
      </div>
      <div class="content">
        <label>登录密码:</label>
        <input placeholder="请输入你的密码" v-model="password" name="password" v-validate="{ required: true, regex: /^[0-9A-Za-z]{8,20}$/ }" :class="{ invalid: errors.has('password') }" />
        <span class="error-msg">{{ errors.first("password") }}</span>
      </div>
      <div class="content">
        <label>确认密码:</label>
        <input placeholder="请输入确认密码" v-model="password1" name="password1" v-validate="{ required: true, is: password }" :class="{ invalid: errors.has('password1') }" />
        <span class="error-msg">{{ errors.first("password1") }}</span>
      </div>
      <div class="controls">
        <input type="checkbox" v-model="agree" name="agree" v-validate="{ required: true, tongyi: true }" :class="{ invalid: errors.has('agree') }" />
        <span>同意协议并注册《尚品汇用户协议》</span>
        <span class="error-msg">{{ errors.first("agree") }}</span>
      </div>
      <div class="btn">
        <button @click="userRegister">完成注册</button>
      </div>
    </div>

    <!-- 底部 -->
    <div class="copyright">
      <ul>
        <li>关于我们</li>
        <li>联系我们</li>
        <li>联系客服</li>
        <li>商家入驻</li>
        <li>营销中心</li>
        <li>手机尚品汇</li>
        <li>销售联盟</li>
        <li>尚品汇社区</li>
      </ul>
      <div class="address">地址：北京市昌平区宏福科技园综合楼6层</div>
      <div class="beian">京ICP备19006430号</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      // 收集表单数据--手机号
      phone: "",
      //验证码
      code: "",
      //密码
      password: "",
      //确认密码
      password1: "",
      //是否同意
      agree: true,

      isShow: true, //通过 v-show 控制显示'获取按钮'还是'倒计时'
      count: 0, //倒计时 计数器
    };
  },
  methods: {
    //获取验证码
    async getCode() {
      //简单判断一下---至少用数据
      this.isShow = false; //倒计时
      this.count = 30; //赋值3秒
      var times = setInterval(() => {
        this.count--; //递减
        if (this.count <= 0) {
          // <=0 变成获取按钮
          this.isShow = true;
          clearInterval(times);
        }
      }, 1000); //1000毫秒后执行

      try {
        //如果获取到验证码
        // const { phone } = this;
        // phone && (await this.$store.dispatch("getCode", phone));
        // //将组件的code属性值变为仓库中验证码[验证码直接自己填写了]
        // this.code = this.$store.state.user.code;
      } catch (error) {}
    },
    //用户注册
    async userRegister() {
      const success = await this.$validator.validateAll();
      //全部表单验证成功，在向服务器发请求，进行祖册
      //只要有一个表单没有成功，不会发请求
      if (success) {
        try {
          const { phone, code, password, password1 } = this;
          await this.$store.dispatch("userRegister", {
            phone,
            code,
            password,
          });
          //注册成功进行路由的跳转
          this.$router.push("/login");
        } catch (error) {
          alert(error.message);
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import "./index.less";
</style>
