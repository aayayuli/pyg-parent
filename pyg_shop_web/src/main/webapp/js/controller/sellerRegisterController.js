app.controller("sellerRegisterController",function($scope,sellerService,$window){
	
	$scope.save=function(){
		sellerService.add($scope.entity).success(function(response){
			if(response.success){
				alert("24小时之内会完成审核！！！");
				// $window.location.reload();
				location.href="/register.html"
			}else{
				alert(response.message);
			}
			
			
		})
	}
	
	
})