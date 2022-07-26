/* 
所有路由配置的数组
*/
// import Home from '@/view/Home'
// import Search from '@/view/Search'
const Search = () => import('@/view/Search');

import Pay from '@/view/Pay';
import PaySuccess from '@/view/PaySuccess';
import Center from '@/view/Center';
import MyOrder from '@/view/Center/myOrder';
import GroupBuy from '@/view/Center/groupOrder';

import Register from '@/view/Register';
import Login from '@/view/Login';
import store from '@/store';
import router from '@/router';

/* 
component: () => import('@/view/Search')
1. import(modulePath): 动态import引入模块, 被引入的模块会被单独打包
2. 组件配置的是一个函数, 函数中通过import动态加载模块并返回, 
    初始时函数不会执行, 第一次访问对应的路由才会执行, 也就是说只有一次请求对应的路由路径才会请求加载单独打包的js
作用: 用于提高首屏的加载速度
*/

export default [
  {
    path: '/',
    redirect: '/home',
  },

  {
    path: '/home',
    component: () => import('@/view/Home'),
  },
  {
    name: 'search', // 是当前路由的标识名称
    path: '/search/:keyword?',
    component: Search,
    // 将params参数和query参数映射成属性传入路由组件
    props: (route) => ({
      keyword3: route.params.keyword,
      keyword4: route.query.keyword2,
    }),
  },
  {
    name: 'detail', // 是当前路由的标识名称
    path: '/detail',
    component: () => import('@/view/Detail'),
  },
  {
    path: '/addcartsuccess',
    component: () => import('@/view/AddCartSuccess/index.vue'),

    beforeEnter(to, from, next) {
      const skuNum = to.query.skuNum;
      // 读取保存的数据
      const skuInfo = JSON.parse(window.sessionStorage.getItem('SKUINFO'));
      console.log('---', skuNum, skuInfo);
      // 只有都存在, 才放行
      if (skuNum && skuInfo) {
        next();
      } else {
        // 在组件对象创建前强制跳转到首页
        next('/');
      }
    },
  },
  {
    path: '/shopcart',
    component: () => import('@/view/ShopCart/index.vue'),
  },

  {
    path: '/trade',
    component: () => import('@/view/Trade/index.vue'),
    /* 只能从购物车界面, 才能跳转到交易界面 */
    beforeEnter(to, from, next) {
      // if (from.path === '/shopcart') {
      next();
      // }
    },
  },
  {
    path: '/pay',
    component: Pay,

    // 将query参数映射成props传递给路由组件
    props: (route) => ({ orderId: route.query.orderId }),

    /* 只能从交易界面, 才能跳转到支付界面 */
    beforeEnter(to, from, next) {
      // if (from.path === '/trade') {
      next();
      // } else {
      // next('/trade');
      // }
    },
  },

  {
    path: '/paysuccess',
    component: PaySuccess,
    /* 只有从支付界面, 才能跳转到支付成功的界面 */
    beforeEnter(to, from, next) {
      if (from.path === '/pay') {
        next();
      } else {
        next('/pay');
      }
    },
  },
  {
    path: '/center',
    component: Center,
    children: [
      {
        // path: '/center/myorder',
        path: 'myorder',
        component: MyOrder,
      },
      {
        path: 'groupbuy',
        component: GroupBuy,
      },

      {
        path: '',
        redirect: 'myorder',
      },
    ],
  },

  {
    path: '/register',
    component: Register,
    meta: {
      isHideFooter: true,
    },
  },
  {
    path: '/login',
    component: Login,
    meta: {
      isHideFooter: true,
    },
    /* 
    beforeEnter: (to, from, next) => { // 路由前置守卫
      // 如果还没有登陆, 放行
      if (!store.state.user.userInfo.token) {
        next()
      } else {
        // 如果已经登陆, 跳转到首页
        next('/')
      }
    } */
  },

  {
    path: '/communication',
    component: () => import('@/view/Communication/Communication'),
    children: [
      {
        path: 'event',
        component: () => import('@/view/Communication/EventTest/EventTest'),
        meta: {
          isHideFooter: true,
        },
      },
      {
        path: 'model',
        component: () => import('@/view/Communication/ModelTest/ModelTest'),
        meta: {
          isHideFooter: true,
        },
      },
      {
        path: 'sync',
        component: () => import('@/view/Communication/SyncTest/SyncTest'),
        meta: {
          isHideFooter: true,
        },
      },
      {
        path: 'attrs-listeners',
        component: () =>
          import('@/view/Communication/AttrsListenersTest/AttrsListenersTest'),
        meta: {
          isHideFooter: true,
        },
      },
      {
        path: 'children-parent',
        component: () =>
          import('@/view/Communication/ChildrenParentTest/ChildrenParentTest'),
        meta: {
          isHideFooter: true,
        },
      },
      {
        path: 'scope-slot',
        component: () =>
          import('@/view/Communication/ScopeSlotTest/ScopeSlotTest'),
        meta: {
          isHideFooter: true,
        },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: '404',
    component: () => import('@/view/404.vue'),
    meta: {
      isShowHeader: true,
      isHideFooter: true,
    },
  },
];
