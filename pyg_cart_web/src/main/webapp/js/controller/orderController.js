app.controller("orderController", function ($scope, orderService, addressService, cartService) {
    $scope.selectedAddress = null;
    $scope.findAddressList = function () {
        $scope.findAddressListByUserId().success(function (response) {
            $scope.addressList = response;
            for (var i = 0; i < response.length; i++) {
                if (response[i].isDefault == '1') {
                    $scope.selectedAddress = response[i]
                    break;
                }
            }
            if ($scope.selectedAddress = null) {//没有默认给第一个
                if (response != null && response.length > 0) {
                    $scope.selectedAddress = response[0]
                }
            }
        })

    }
    $scope.updateSelectAddress = function (pojo) {
        $scope.selectedAddress = pojo;
    }
    $scope.isSelectedAddress = function (pojo) {
        return $scope.selectedAddress == pojo;
    }


    $scope.findCartList = function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList = response;
            $scope.totalNum = 0;
            $scope.totalMoney = 0.00;
            for (var i = 0; i < response.length; i++) {
                var cart = response[i];
                var orderItemList = cart.orderItemList;
                for (var j = 0; j < orderItemList.length; j++) {
                    var tbOrderItem = orderItemList[j];
                    $scope.totalNum += tbOrderItem.num;
                    $scope.totalMoney += tbOrderItem.totalFee;
                }

            }
        })
    }

    //保存订单,根据数据库表结构分析，保存数据可分为前端获取，和后台获取两部分


    $scope.entity={paymentType:'1',sourceType:'2'};
    //  `source_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
    // `payment_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、微信支付，2、货到付款',
    //保存订单 ,  前端可传入的数据 先行传入， 在后台继续添加
    $scope.saveOrder=function () {
        $scope.entity['receiverAreaName']=$scope.selectedAddress.address;
        $scope.entity['receiverMobile']=$scope.selectedAddress.mobile;
        $scope.entity['receiver']=$scope.selectedAddress.contact;
        //  `receiver_area_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地区名称(省，市，县)街道',
        //  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
        //  `receiver` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
        orderService.add($scope.entity).success(function (response) {
            // response:{success:true,message:""}
            if(response.success){
                location.href="http://pay.pinyougou.com/pay.html#?out_trade_no="+response.message;
            }else{
                alert(response.message);
            }
        })
    }

});