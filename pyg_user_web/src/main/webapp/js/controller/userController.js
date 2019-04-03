app.controller("userController", function ($scope, userService) {
    var reg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
    $scope.sendCode = function () {
        if (!reg.test($scope.entity.phone)) {
            alert("手机号码格式错误！");
            return;
        }
        userService.sendCode($scope.entity.phone).success(function (response) {
            if (response.success) {
                alert("倒计时跳转")
            } else {
                alert(response.message);
            }
        })
    };

    $scope.register = function () {
        if( ! reg.test( $scope.entity.phone)){
            alert("手机号码格式错误！");
            return;
        }

        if($scope.entity.password!=$scope.password2){
            alert("两次密码输入不一致！");
            return;
        }
        userService.register( $scope.entity, $scope.code).success(function (response) {
            if(response.success){
                // 注册成功后跳转到登录页面
                // location.href="http://passport.pinyougou.com/cas/login";
                location.href="/home-index.html";

                // 登录成功后 跳转到home-index.html
            }else{
                alert(response.message);
            }
        })
    };

   $scope.showName=function () {
        userService.showName().success(function (response) {
            $scope.username =JSON.parse(response)
        })

   }
});