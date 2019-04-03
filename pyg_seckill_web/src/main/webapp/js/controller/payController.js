app.controller("payController", function ($scope, payService, $location, $interval) {
    // http://pay.pinyougou.com/pay.html#?out_trade_no=1111938209006260224     订单Url

    //创建二维码的方法
    $scope.createNative = function () {
        $scope.flag = false;
        // 从url中获取订单号
        $scope.out_trade_no = $location.search()['out_trade_no'];
        payService.createNative($scope.out_trade_no).success(function (response) {
            $scope.resultMap = response;
            new QRious({
                element: document.getElementById('payImg'),
                size: 300,
                value: response.code_url
            });
//二维码生成后， 马上查询支付结果
            /*   $scope.num=10;
                    // 马上查询支付状态
                   var myIn = $interval(function () {
                        if($scope.num==0){
                            $interval.cancel(myIn);
                            return;
                        }
                        $scope.num--;
                    },1000);*/


            $scope.orderQuery()
        })
    };

    $scope.orderQuery = function () {
        // 每隔3秒查询一次，知道查询的结果的response.trade_state是SUCCESS为止，但是也不会无休止查询，规定时间：5分钟
        // angularJs定时器

        // var bianliang = $interval(函数,时间间隔单位是毫秒,[执行的次数]);
        //  $interval.cancel(bianliang); //取消定时器

        $scope.num = 10;//执行10次
        // 马上开启查询支付状态的任务
        var myInterval = $interval(function (response) {
            if ($scope.num == 0) {
                $scope.flag = true;
                $interval.cancel(myInterval);//意味着执行了10次查询，结束
                return;
            }
            payService.orderQuery($scope.out_trade_no).success(function (response) {
                if (response == null || response == undefined || response == 'undefined') {
                    $interval.cancel(myInterval);//意味着支付失败了
                    location.href = "/payfail.html";
                    return
                }
                if (response.trade_state == "SUCCESS") {//在100次查询中 支付成功了

                    // 支付成功后修改订单数据 tb_pay_log tb_order
                    payService.updateOrder($scope.out_trade_no, response.transaction_id)

                    $interval.cancel(myInterval);
                    location.href = "/paysuccess.html";
                    return;
                }
            })
            $scope.num--;
        }, 3000);

    }
});