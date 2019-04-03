app.service("addressService",function ($http) {
    this.findAddressListByUserId=function () {
      return  $http.get("./address/findAddressListByUserId")
    }

});