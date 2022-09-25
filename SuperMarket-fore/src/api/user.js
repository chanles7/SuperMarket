import req from './ajax';

/**
 *
 * @param {用户相关接口}
 * @returns
 */
//获取购物车列表
export const reqUpdateAddressState = (addressId) =>
  req.post(`user/memberreceiveaddress/update/state/${addressId}`);
