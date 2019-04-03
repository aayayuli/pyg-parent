app.controller("seckillController", function ($scope, seckillService, $location, $interval) {
    $scope.findSeckillGoods = function () {
        seckillService.findSeckillGoods().success(function (response) {
            $scope.list = response;
        })
    }
    $scope.findOne = function () {
        //从URL中获取id
        var id = $location.search()['id'];
        seckillService.findOne(id).success(function (response) {
            $scope.entity = response;//获取了某商品
            // response.endTime-当前时间
            // 时间.getTime()//获取时间的毫秒
            //     Math.floor(数据);向下取整
            //     123345675  秒
            $scope.timeStr = "";

            var totalSeconds = (new Date(response.endTime).getTime() - new Date().getTime()) / 1000;
            $interval(function () {
                var days = Math.floor(totalSeconds / 60 / 60 / 24);//  3.34545
                var hours = Math.floor((totalSeconds - days * 24 * 60 * 60) / 60 / 60);  //1.345
                var minutes = Math.floor((totalSeconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
                var seconds = totalSeconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60;

                if (days < 10) {
                    days = "0" + days;
                }
                if (hours < 10) {
                    hours = "0" + hours;
                }
                if (minutes < 10) {
                    minutes = "0" + minutes;
                }
                if (seconds < 10) {
                    seconds = "0" + seconds;
                }
                if (days == '00') {
                    $scope.timeStr = hours + ":" + minutes + ":" + seconds;
                } else {
                    $scope.timeStr = days + "天 " + hours + ":" + minutes + ":" + seconds;
                }
                totalSeconds--;
                //3天 01:56:58
            }, 1000, totalSeconds)

        })
    }

    $scope.saveOrder = function () {
        seckillService.saveOrder($scope.entity.id).success(function (response) {
            if(response.success){
                location.href="/pay.html"
            }else{
                alert(response.message);
            }
        })
    }


});