app.controller("goodsEditController", function ($scope, uploadService, goodsService, itemCatService, typeTemplateService) {

    $scope.entity = {tbGoods: {isEnableSpec:"1"}, tbGoodsDesc: {itemImages: [], specificationItems: []}}; //初始化itemImages 是个数组
//	图片上传
    $scope.uploadFile = function () {
        uploadService.upload().success(function (response) {
            if (response.success) {
                // alert("上传成功后返回图片地址："+response.message);
                $scope.image.url = response.message;
            } else {
                alert(response.message);
            }

        })
    }

//	向entity.tbGoods.itemImages中追加对象
    $scope.addItemImage = function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image);
    };

//	从entity.tbGoods.itemImages中移除对象
    $scope.deleItemImages = function (index) {
        $scope.entity.tbGoodsDesc.itemImages.splice(index, 1);

    };


//	查询一级分类数据
    $scope.findCategory1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.category1List = response;
        })
    }
//	检测数据的变化entity.tbGoods.category1Id 查询二级分类数据  相当于onChange事件
    $scope.$watch('entity.tbGoods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.category2List = response;
        })
    })

    //	检测数据的变化entity.tbGoods.category2Id 查询三级分类数据  相当于onChange事件
    $scope.$watch('entity.tbGoods.category2Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.category3List = response;
        })
    })
//	检测数据的变化entity.tbGoods.category3Id 查询模板ID  相当于onChange事件
    $scope.$watch('entity.tbGoods.category3Id', function (newValue, oldValue) {
        for (var i = 0; i < $scope.category3List.length; i++) {
            if ($scope.category3List[i].id == newValue) {
                $scope.entity.tbGoods.typeTemplateId = $scope.category3List[i].typeId;
            }

        }


    });

    //	检测数据的变化$scope.entity.tbGoods.typeTemplateId 查询品牌数据  相当于onChange事件
    $scope.$watch('entity.tbGoods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
//			从模板中获取品牌
            $scope.brandList = JSON.parse(response.brandIds);
//			从模板中获取扩展属性
            $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
        });
        //根据模板ID以下格式
        typeTemplateService.findSpecList(newValue).success(function (response) {
//			response = [{"id":27,"text":"网络",options:[{},{},{}]},{"id":32,"text":"机身内存"}]
            $scope.specList = response;
        })
    });


//	勾选规格小项更改即将保存的数据
    $scope.entity.tbGoodsDesc.specificationItems = [];
    $scope.updateSpecificationItems = function (event, key, value) {//key：网络  value：联通4G
        // [{"attributeName":"网络","attributeValue":["移动4G"]},{"attributeName":"机身内存","attributeValue":["32G"]}]
        var specification = findObjectByKey($scope.entity.tbGoodsDesc.specificationItems, key);
        if (event.target.checked) {
            if (specification == null) {
                $scope.entity.tbGoodsDesc.specificationItems.push({"attributeName": key, "attributeValue": [value]});
            }else {
                specification.attributeValue.push(value);
            }
        }else {
            // [{"attributeName":"网络","attributeValue":["移动4G","联通4G"]},{"attributeName":"机身内存","attributeValue":["32G"]}]
            //key：网络  value：联通4G
            var index = specification.attributeValue.indexOf(value);
            specification.attributeValue.splice(index,1)
            //key："机身内存"  value：32G
            if (specification.attributeValue.length==0){
                // 把大对象从大数组中移除
                var index1=$scope.entity.tbGoodsDesc.specificationItems.indexOf(specification)
                $scope.entity.tbGoodsDesc.specificationItems.splice(index1,1)
            }
        }
// entity.tbGoodsDesc.specificationItems变化后itemList的数据也会变化

        //	alert("点击规格项修改即将保存规格的数据格式");
        createItemList();
    };

    function findObjectByKey(specificationItems, key) {
        // specificationItems: [{"attributeName":"网络","attributeValue":["移动4G"]},{"attributeName":"机身内存","attributeValue":["32G"]}]
        // key:网络  手机屏幕尺寸
        for (var i = 0; i < specificationItems.length; i++) {
            if (key == specificationItems[i].attributeName) {
                return specificationItems[i]
            }
        }
        return null;

    }


//	根据$scope.entity.tbGoodsDesc.specificationItems动态生成sku列表
    function createItemList() {
//	       第一步：初始化itemList  spec格式:{"机身内存":"16G","网络":"联通4G"}
        $scope.entity.itemList = [{spec: {}, price: 0, num: 9999, status: '1', isDefault: '0'}];
        var specificationItems = $scope.entity.tbGoodsDesc.specificationItems;
//		第二步：循环items 为$scope.entity.itemList追加列
        for (var i = 0; i < specificationItems.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, specificationItems[i].attributeName, specificationItems[i].attributeValue);
        }

    }

    function addColumn(itemList, attributeName, attributeValue) {
        var newItemList = [];
        for (var i = 0; i < itemList.length; i++) {
            for (var j = 0; j < attributeValue.length; j++) {
                var row = JSON.parse(JSON.stringify(itemList[i]));//深克隆
                row.spec[attributeName] = attributeValue[j];
                newItemList.push(row);
            }
        }
        return newItemList;
    }


    $scope.save = function () {
        $scope.entity.tbGoodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(function (response) {
            if (response.success) {
                location.href = "goods.html";  //跳转到列表页面
            } else {
                alert(response.message);
            }
        });

    }


})