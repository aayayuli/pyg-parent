//只和后台交互 不和 html交互
app.service("specificationService", function ($http) {

    this.findSpecList = function () {
        return $http.get("../specification/findSpecList");
    }

    this.search = function (pageNo, pageSize, searchEntity) {
        return $http.post("../specification/search?pageNo=" + pageNo + "&pageSize=" + pageSize, searchEntity);
    }

    this.findPage = function (pageNo, pageSize) {
        return $http.get("../specification/findPage?pageNo=" + pageNo + "&pageSize=" + pageSize);
    }

    this.findAll = function () {
        return $http.get("../specification/findAll");
    }

    this.add = function (entity) {
        return $http.post("../specification/add", entity);
    }

    this.update = function (entity) {
        return $http.post("../specification/update", entity);
    }

    // 根据id查询对象
    this.findOne = function (id) {
        return $http.get("../specification/findOne?id=" + id);
    }

// 	       删除
    this.dele = function (selectIds) {
        return $http.get("../specification/dele?ids=" + selectIds);
    }

})
