app.controller("cartController", function ($scope, cartService) {

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
    $scope.addGoodsToCartList=function (itemId,num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if (response.success){
                $scope.findCartList()
            }else {
                alert(response.message)
            }

        })

    }


});