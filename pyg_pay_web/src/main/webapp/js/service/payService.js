app.service("payService",function ($http) {
    this.createNative=function (out_trade_no) {
        return $http.get("./pay/createNative?out_trade_no="+out_trade_no);
    }

    this.orderQuery=function (out_trade_no) {
        return $http.get("./pay/orderQuery?out_trade_no="+out_trade_no);
    }
});