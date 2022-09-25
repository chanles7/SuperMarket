import req from './ajax';

/**
 *
 * @param {购物车相关接口}
 * @returns
 */
//获取购物车列表
export const reqCartList = () => req.get('/cart/list ');

//新增或修改购物车商品数量
export const reqAddOrUpdateCart = (skuId, skuNum) =>
  req.post(`/cart/addOrUpdateCart/${skuId}/${skuNum}`);

//变更购物车商品勾选状态
export const reqUpdateCheckedById = (skuId, isChecked) =>
  req.post(`/cart/updateCheck/${skuId}/${isChecked}`);

//获取订单确认信息
export const reqConfirmOrder = () => req.get(`/cart/confirmOrder`);
