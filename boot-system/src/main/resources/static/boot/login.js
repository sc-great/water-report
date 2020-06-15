$(function() {
	validateKickout();
	$('.imgcode').click(function() {
		var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
		$(".imgcode").attr("src", url);
	});
	$("#btnSubmit").click(function () {
        login();
    });
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            login();
        }
    });
});
function login() {
	var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    var rememberMe = $("input[name='rememberme']").is(':checked');
    if(username==""){
        layer.tips('请填写登录帐号！', '#username',{tips: [1, '#3595CC']});
        $("input[name='username']").focus()
    }else if(password==""){
        layer.tips('请填写登录密码！', '#password',{tips: [1, '#3595CC']});
        $("input[name='password']").focus();
    }else if(validateCode==""){
        layer.tips('请填写验证码！', '#validateCode',{tips: [1, '#3595CC']});
        $("input[name='validateCode']").focus();
    }else{
        var index = layer.load(1, {shade: 0.4}); //0代表加载的风格，支持0-2
        $.ajax({
            type: "post",
            url: ctx + "admin/doLogin",
            data: {
                "username": username,
                "password": password,
                "validateCode" : validateCode,
                "rememberMe": rememberMe
            },
            success: function(r) {
                if (r.code == 0) {
                    setTimeout(function () {
                        location.href = ctx + 'admin/index';
                    },500);
                } else {
                    layer.close(index);
                    $('.imgcode').click();
                    $(".code").val("");
                    $("input[name='validateCode']").focus();
                    layer.alert(r.msg, {
                        icon: 5,
                        title:'温馨提示'
                    })
                }
            }
        });
    }
}

function validateKickout() {
	if (getParam("kickout") == 1) {
	    layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
	        icon: 0,
	        title: "系统提示"
	    },
	    function(index) {
	        //关闭弹窗
	        layer.close(index);
	        if (top != self) {
	            top.location = self.location;
	        } else {
	            var url  =  location.search;
	            if (url) {
	                var oldUrl  = window.location.href;
	                var newUrl  = oldUrl.substring(0,  oldUrl.indexOf('?'));
	                self.location  = newUrl;
	            }
	        }
	    });
	}
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}