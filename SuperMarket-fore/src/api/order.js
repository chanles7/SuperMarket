import req from './ajax';

/**
 *
 * @param {订单相关接口}
 * @returns
 */
//获取购物车列表
export const reqSubmitOrder = (data) => req.post('/order/order/submit', data);
