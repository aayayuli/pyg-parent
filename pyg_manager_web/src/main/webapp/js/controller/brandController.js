//表现层， 和页面数据交互
app.controller("brandController", function ($scope, brandService, $controller) {
    $controller("baseController", {$scope: $scope});

    $scope.findAll = function () {
        brandService.findAll().success(function (response) {
            $scope.list = response;
        })
    };
    //保存方法
    $scope.save = function () {
        var obj = null;
        if ($scope.entity.id != null) {
            obj = brandService.update($scope.entity);
        } else {
            obj = brandService.add($scope.entity);
        }
        obj.success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };
    //根据ID查询用户
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });

    };


    //删除
    $scope.dele = function () {
        //判断数组中是否有值
        if ($scope.selectIds.length === 0) {
            alert("请选择您要删除的数据！");
            return;
        }
        var flag = window.confirm("确定要删除您选的数据么？");
        if (flag) {
            brandService.dele($scope.selectIds).success(function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = {};//清空数组
                } else {
                    alert(response.message)
                }
            })
        }
    };


    $scope.searchEntity = {};
    $scope.search = function (pageNo, pageSize) {
        //$scope searchEntity   页码  条数
        brandService.search(pageNo, pageSize, $scope.searchEntity).success(function (response) {
            //response 分页后的结果  当前页数数据 list 总条数{total：100 ，rows:{}{}}
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }

});