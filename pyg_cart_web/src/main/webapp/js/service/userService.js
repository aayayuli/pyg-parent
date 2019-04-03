app.service("userService",function ($http) {
    this.sendCode=function (phone) {
      return  $http.get("./user/sendCode/"+phone)
    };
 this.register=function (user,code) {
      return  $http.post("./user/register/"+code,user)
    }

    this.showName=function () {
     return $http.get("./user/showName")
    }

});