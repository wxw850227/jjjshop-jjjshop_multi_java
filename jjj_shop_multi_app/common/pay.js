/*
 * 支付
 */
export const pay = (result, self, success, fail) => {
	if (result.code === -10) {
		self.showError(result.msg);
		return false;
	}

	// 发起微信支付
	if (result.data.payType == 20) {
		//小程序支付
		// #ifdef  MP-WEIXIN	
		uni.requestPayment({
			provider: 'wxpay',
			timeStamp: result.data.payment.timeStamp,
			nonceStr: result.data.payment.nonceStr,
			package: result.data.payment.packageValue,
			signType: result.data.payment.signType,
			paySign: result.data.payment.paySign,
			success: res => {
				paySuccess(result, self, success);
			},
			fail: res => {
				self.showError('订单未支付成功', () => {
					payError(result, fail, self);
				});
			},
		});
		// #endif
		//公众号支付
		// #ifdef  H5
		if (self.isWeixin()) {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
					timeStamp: result.data.payment.timeStamp,
					nonceStr: result.data.payment.nonceStr,
					package: result.data.payment.packageValue,
					signType: result.data.payment.signType,
					paySign: result.data.payment.paySign,
					appId: result.data.payment.appId
			},
				function(res) {
					if (res.err_msg == "get_brand_wcpay_request:ok") {
						paySuccess(result, self, success);
					} else if (res.err_msg == "get_brand_wcpay_request:cancel") {
						self.showSuccess('支付取消', () => {
							payError(result, fail, self);
						});
					} else {
						self.showError('订单未支付成功', () => {
							payError(result, fail, self);
						});
					}
				}
			);
		} else {
			if(result.data.wxPayVersion == 2){
				window.location.href = result.data.payment.mwebUrl + '&redirect_url=' + result.data.returnUrl;
			}else{
				window.location.href = result.data.payment + '&redirect_url=' + result.data.returnUrl;
			}
			return;
		}
		// #endif
		// #ifdef  APP-PLUS
		//微信支付
		wxAppPay(result, self, success, fail);
		// #endif
	}
	// 余额支付
	if (result.data.payType == 10) {
		paySuccess(result, self, success);
	}
}

/*跳到支付成功页*/
function paySuccess(result, self, success) {
	if (success) {
		success(result);
		return;
	}
	gotoSuccess(result, self);
}
/*跳到支付成功页*/
function gotoSuccess(result, self) {
	self.gotoPage('/pages/order/pay-success/pay-success?order_id=' + result.data.order_id, 'reLaunch');
}

/*支付失败跳订单详情*/
function payError(result, fail, self) {
	if (fail) {
		fail(result);
		return;
	}
	self.gotoPage('/pages/order/order-detail?order_id=' + result.data.order_id, 'redirect');
}

function wxAppPay(result, self, success, fail) {
	let payment = result.data.payment;
	uni.requestPayment({
		provider: 'wxpay',
		orderInfo: {
			"appid": result.data.wxPayVersion == 2?payment.appId:payment.appid,
			"noncestr": result.data.wxPayVersion == 2?payment.nonceStr:payment.noncestr,
			"package": payment.packageValue,
			"partnerid": payment.partnerId,
			"prepayid": payment.prepayId,
			"timestamp": result.data.wxPayVersion == 2?payment.timeStamp:payment.timestamp,
			"sign": payment.sign
		},
		success(res) {
			paySuccess(result, self, success);
		},
		fail(error) {
			console.log(error);
			self.showError('订单未支付成功', () => {
				payError(result, fail);
			});
		},
	});
}