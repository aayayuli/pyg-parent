//控制层
app.controller('typeTemplateController', function ($scope, $controller, brandService, specificationService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //$scope.brandList={data:[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]};
    //使用brandService中的findAll方法查询的数据与上一行需求的不同 text和name
    //{["firstChar":"L","id":"1","name":"联想"]}
    $scope.findBrandList = function () {
        brandService.findBrandList().success(function (response) {
            $scope.brandList = {data: response}
        })
    };

    $scope.findSpecList = function () {
        specificationService.findSpecList().success(function (response) {
            $scope.specList = {data: response}
        })
    };
    //动态添加扩展属性， 向$scope.entity.customAttributeItems  中添加一个属性
    $scope.addCustomAttributeItems = function () {
        $scope.entity.customAttributeItems.push({});
    }
    $scope.deleCustomAttributeItems = function (index) {
        $scope.entity.customAttributeItems.splice(index, 1);
    }
    //页面显示优化，去掉数组格式， 只留下 其中text的值
    $scope.arrayToString=function(array){
        array= JSON.parse(array);
        var str = "";
        for (var i = 0; i < array.length; i++) {
            if (i==array.length-1){
                str+=array[i].text;
            } else {
                str+=array[i].text+","
            }
        }
        return str;
    }




    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (pageNum, pageSize) {
        typeTemplateService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
//把JSON字符串转换成Json数组
                response.brandIds=JSON.parse(response.brandIds);
                response.specIds=JSON.parse(response.specIds);
                response.customAttributeItems=JSON.parse(response.customAttributeItems);
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        typeTemplateService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (pageNum, pageSize) {
        typeTemplateService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

});	
