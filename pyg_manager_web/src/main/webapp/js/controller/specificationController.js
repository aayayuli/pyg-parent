//表现层， 和页面数据交互
app.controller("specificationController", function ($scope, specificationService, $controller) {
    $controller("baseController", {$scope: $scope});

//添加规格小项  本质 是向 $scope.entity.tbSpecificationOptionList 中添加一个对象
    $scope.addTbSpecificationOptions=function(){
        $scope.entity.tbSpecificationOptionList.push({});
    }
    // 删除规格小项：从数据的本质上就是从 $scope.entity.tbSpecificationOptionList中移除一个对象
    $scope.deleTbSpecificationOptions=function(index) {
        $scope.entity.tbSpecificationOptionList.splice(index,1);
    }



    $scope.findAll = function () {
        specificationService.findAll().success(function (response) {
            $scope.list = response;
        })
    };
    //保存方法
    $scope.save = function () {
        var obj = null;
        if ($scope.entity.tbSpecification.id != null) {
            obj = specificationService.update($scope.entity);
        } else {
            obj = specificationService.add($scope.entity);
        }
        obj.success(function (response) {
            if (response.success) {
                $scope.reloadList();
            } else {
                alert(response.message);
            }
        });
    };
    // 根据id查询对象
    $scope.findOne=function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity=response;
        })
    }


    //删除
    $scope.dele = function () {
        //判断数组中是否有值
        if ($scope.selectIds.length === 0) {
            alert("请选择您要删除的数据！");
            return;
        }
        var flag = window.confirm("确定要删除您选的数据么？");
        if (flag) {
            specificationService.dele($scope.selectIds).success(function (response) {
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
        specificationService.search(pageNo, pageSize, $scope.searchEntity).success(function (response) {
            //response 分页后的结果  当前页数数据 list 总条数{total：100 ，rows:{}{}}
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }

});