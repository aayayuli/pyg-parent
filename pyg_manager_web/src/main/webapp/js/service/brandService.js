//只和后台交互 不和 html交互
app.service("brandService",function($http){
	/**
	 * 此方法是添加模板时需要的
	 * @returns {*}
	 */
	this.findBrandList=function(){
		return $http.get("../brand/findBrandList");
	}


	
	     this.search=function(pageNo,pageSize,searchEntity){
	    	 return $http.post("../brand/search?pageNo="+pageNo+"&pageSize="+pageSize,searchEntity);
	     }
	
         this.findPage=function(pageNo,pageSize){
            return $http.get("../brand/findPage?pageNo="+pageNo+"&pageSize="+pageSize);
         }
        
		 this.findAll=function(){
			return $http.get("../brand/findAll");
   		  }
		 
		 this.add=function(entity){
			 return $http.post("../brand/add",entity);
		 }
		 
		 this.update=function(entity){
			 return $http.post("../brand/update",entity);
		 }
		 
// 		 修改时先根据id查询
		 this.findOne=function(id){
			return $http.get("../brand/findOne?id="+id);
		 }
	   
// 	       删除
	   this.dele=function(selectIds){
	 		return   $http.get("../brand/dele?ids="+selectIds);
	   }
														
    })
