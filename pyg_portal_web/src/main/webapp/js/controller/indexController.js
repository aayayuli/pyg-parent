app.controller("indexController",function ($scope,contentService) {

    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.bannerList=response; //轮播图数据
        })

    };
    $scope.keyword=""
    $scope.search=function () {
        if ($scope.keyword==""){
            $scope.keyword="小米"
        }
        location.href="http://localhost:8084/search.html#?keyword="+$scope.keyword;
        // location.href="search.pinyougou.com/search.html#?keyword="+$scope.keyword;  加入nginx后使用

    }
});