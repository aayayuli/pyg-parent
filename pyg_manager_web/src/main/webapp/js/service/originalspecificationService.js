/*
//只和后台交互 不和 html交互
app.service("specificationService",function($http){
//		 
	   
	     this.search=function(pageNo,pageSize,searchEntity){
	    	 return $http.get("../data/specification.json"); // TODO测试数据
	     };
	
         this.findPage=function(pageNo,pageSize){
            return $http.get("../specification.json");
         };
        
		
		 this.add=function(entity){
			 return $http.post("../specification/add",entity);
		 };
		 
		 this.update=function(entity){
			 return $http.post("../specification/update",entity);
		 };
		 
// 		 修改时先根据id查询
		 this.findOne=function(id){
			return $http.get("../data/specification_one.json"); // TODO测试数据
		 };
	   
// 	       删除
	   this.dele=function(selectIds){
	 		return   $http.get("../specification/dele?ids="+selectIds);
	   };
	   
	   this.findSpecList=function(){
	    	 return $http.get("../data/specList.json"); // TODO测试数据
	    };
	
	   this.findBrandList=function(){
		   return $http.get("../data/brandList.json"); // TODO测试数据
	   }
	   
														
    });
*/
