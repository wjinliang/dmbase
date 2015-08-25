var DmCheck = function(data){
	var validate = true;
	if(data.status=="timeout"){
		validate = false;
		alert("session已失效,请重新登录");
		window.location.href=dm_root+"login";
	}
	if(data.status=="0"){
		validate = false;
		alert("服务器内部错误");
	}
	return validate;
};