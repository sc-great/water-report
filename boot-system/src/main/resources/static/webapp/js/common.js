$(function () {
    //禁用右键
    /*$(document).bind("contextmenu", function (e) {
        return false;
    });*/
    var bodyHeight = $('body').height();
    $(window).resize(function () {
        $('.main').height(bodyHeight);
    });
    $.extend({
        _tree: {},
        // 树插件封装处理
        tree: {
            _option: {},
            // 初始化树结构
            init: function (option) {
                var defaults = {
                    id: "tree",                    // 属性ID
                    expandLevel: 0,                // 展开等级节点
                    view: {
                        selectedMulti: false,      // 设置是否允许同时选中多个节点
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    check: {
                        enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                        nocheckInherit: true       // 设置子节点是否自动继承
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    }
                };
                var options = $.extend(defaults, option);
                $.tree._option = options;
                // 树结构初始化加载
                var setting = {
                    callback: {
                        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick                 // 用于捕获鼠标双击之后的事件回调函数
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                //请求数据并渲染
                req(options.url, {
                    selValues: option.selValues,
                    selPValues: option.selPValues,
                    checkType: options.checkType
                }, function (result) {
                    if (result.code != 0) {
                        return false;
                    }
                    if (result.length == 0) {
                        return false;
                    }
                    //初始化树形
                    tree = $.fn.zTree.init($("#" + options.id), setting, result);
                    $._tree = tree;
                    //展开等级
                    $.tree._expandLevel = options.expandLevel;
                    var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                    for (var i = 0; i < nodes.length; i++) {
                        tree.expandNode(nodes[i], true, false, false);
                    }
                    if (options.checkType == "radio") {  //单选
                        //全局根据id检索
                        var nodeParam = null;
                        if ($.common.isNotEmpty(options.selPValues)) {
                            nodeParam = tree.getNodesByParam("id", options.selPValues, null)[0];
                        }
                        //默认选中值
                        var treeId = option.selValues;
                        var node = tree.getNodesByParam("id", treeId, nodeParam)[0];
                        if (node != undefined) {
                            $.tree.selectByIdName(treeId, node);
                        }
                    }
                    else if (options.checkType == "checkbox") {  //多选
                        if ($.common.isNotEmpty(options.selPValues)) {
                            $.each(options.selPValues.split(","), function (index_p, item_p) {
                                //所有选中父节点并展开
                                var parentNode = tree.getNodesByParam("id", item_p, null)[0];
                                if (parentNode != undefined) {
                                    $._tree.expandNode(parentNode, true, true, true);
                                }
                            });
                        }
                    }
                }, "post");
            },
            //初始化非表单，仅显示树形
            initShow: function (option) {
                var defaults = {
                    id: "tree",                    // 属性ID
                    view: {
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    }
                };
                var options = $.extend(defaults, option);
                $.tree._option = options;
                // 树结构初始化加载
                var setting = {
                    view: options.view,
                    data: options.data
                };
                //请求数据并渲染
                $.ajax({
                    type: 'post',
                    url: options.url,
                    data: option.extraData,
                    dataType: "json",
                    success: function (result) {
                        if (result.length == 0) {
                            return false;
                        }
                        //初始化树形
                        var tree = $.fn.zTree.init($("#" + options.id), setting, result);
                        $._tree = tree;
                        $._tree.expandAll(true);
                    }
                });
            },
            // 展开/折叠
            toggleSearch: function () {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus();
            }
        }
    });
});

//把区域id和设备名称写入cookie中
function setCookie(key, value, dd, path) {
    path = path == undefined ? '/' : path;//路径
    if (dd == undefined) {
        dd = new Date();
        dd.setTime(dd.getTime() + (365 * 24 * 60 * 60 * 1000));
    }
    $.cookie(key, value, {
        expires: dd,
        path: path
    });
}

//加载iframe
function loadIframe(url) {
    window.parent.iframe(url);
}

//读取cookie值
function getCookie(key) {
    var value = $.cookie(key);
    return value;
}

//删除cookie
function removeCookie(key, path) {
    path = path == undefined ? '/' : path;//路径
    $.cookie(key, null, {  //删除cookie
        expires: -1,
        path: path
    });
}

//清除所有cookie
function clearAllCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;) {
            removeCookie(keys[i]);
        }
    }
}

function htmldecode(str) {
    if (str != undefined) {
        str = str.replace(/&amp;/gi, '&');
        str = str.replace(/&nbsp;/gi, ' ');
        str = str.replace(/&lt;/gi, '<');
        str = str.replace(/&gt;/gi, '>');
    }
    return str;
}

//字符串剪切
function cutstr(str, len) {
    if (str.length > len) {
        str = str.substring(0, len) + '...';
    }
    return str;
}

//时间格式化短时间（月-日）
function shortdateformat(str) {
    str = str.substring(5, 10).replace('/', '-');
    return str;
}

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    var param = "";
    if (r != null) {
        param = decode(r[2]);
    }
    return param; //返回参数值
}

//随机数
function RndNum(n) {
    var rnd = "";
    for (var i = 0; i < n; i++)
        rnd += Math.floor(Math.random() * 10);
    return rnd;
}

//框架最外层url跳转
function JumpTopURL(url) {
    window.top.location.href = url;
}

//框架跳转
function JumpURL(url) {
    window.location.href = url;
}

//返回
function goBack() {
    history.go(-1);
}

//编码
function encode(text) {
    var code = "";
    if (text != "") {
        code = encodeURIComponent(text);
    }
    return code;
}

//解码
function decode(code) {
    return decodeURIComponent(code);
}

//请求方法
function req(url, params, callback, method, async) {
    var layer_loading;
    if (async == undefined) {
        async = true;
    }
    $.ajax({
        async: async,
        url: url,
        data: params,
        type: method,
        dataType: 'json',
        //contentType: "application/json",
        beforeSend: function () {
            layer_loading = layer.open({
                type: 2,
                content: '加载中'
            });
        },
        success: function (result) {
            if (typeof callback == "function") {
                callback(result);
            }
            layer.close(layer_loading);
        },
        error: function (err) {
            layer.close(layer_loading);
            layer.open({
                content: '请求错误！'
                , btn: '我知道了'
            });
        }
    });
}

// 验证手机号
function isPhoneNo(phone) {
    var pattern = /^1[34578]\d{9}$/;
    return pattern.test(phone);
}

//判断身份证号格式是否正确
function isIdCardNo(idno) {
    var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    var re = new RegExp(reg);
    return re.test(idno);
}

//获取身份证中的信息
function getIdCardInfo(idno) {
    var info;
    if (isIdCardNo(idno)) {
        info = {};
        var birth = idno.substring(6, 10) + "-" + idno.substring(10, 12) + "-" + idno.substring(12, 14);
        info["birth"] = birth;
        var sex = "";
        if (parseInt(idno.substr(16, 1)) % 2 == 1) {
            sex = "1";
        } else {
            sex = "2";
        }
        info["sex"] = sex;
        //获取年龄
        var myDate = new Date();
        var month = myDate.getMonth() + 1;
        var day = myDate.getDate();
        var age = myDate.getFullYear() - idno.substring(6, 10) - 1;
        if (idno.substring(10, 12) < month || idno.substring(10, 12) == month && idno.substring(12, 14) <= day) {
            age++;
        }
        info["age"] = age;
    }
    return info;
}

//tab标签切换
function tabInit() {
    $(".tab .title li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var index = $(this).index();
        $(".tab .content .item").eq(index).addClass("active").siblings().removeClass("active");
    });
}

//页面内部tab标签切换
function tabInInit() {
    $(".tab_in .tab_title .tab_title_item").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var index = $(this).index();
        $(".tab_in .tab_content .tab_item").eq(index).addClass("active").siblings().removeClass("active");
    });
}

/*输入框空格去除*/
function removeSpace($this) {
    $this.value = $this.value.replace(/\s+/g, '');
}

//不可输入空格，且最大长度不超过16位
function checkMaxLength(obj) {
    obj.value = obj.value.replace(/\s+/g, '');
    var maxChars = 16;//最多字符数
    if (obj.value.length > maxChars) {
        obj.value = obj.value.substring(0, maxChars);
    }
    var curr = maxChars - obj.value.length;
}

/*浏览大图配合页面上的html一起使用*/
function imgShow(outerdiv, innerdiv, bigimg, _this) {
    var src = _this.attr("src");//获取当前点击的pimg元素中的src属性
    $(bigimg).attr("src", src);//设置#bigimg元素的src属性

    /*获取当前点击图片的真实大小，并显示弹出层及大图*/
    $("<img/>").attr("src", src).load(function () {
        var windowW = $(window).width();//获取当前窗口宽度
        var windowH = $(window).height();//获取当前窗口高度
        var realWidth = this.width;//获取图片真实宽度
        var realHeight = this.height;//获取图片真实高度
        var imgWidth, imgHeight;
        var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

        if (realHeight > windowH * scale) {//判断图片高度
            imgHeight = windowH * scale;//如大于窗口高度，图片高度进行缩放
            imgWidth = imgHeight / realHeight * realWidth;//等比例缩放宽度
            if (imgWidth > windowW * scale) {//如宽度扔大于窗口宽度
                imgWidth = windowW * scale;//再对宽度进行缩放
            }
        } else if (realWidth > windowW * scale) {//如图片高度合适，判断图片宽度
            imgWidth = windowW * scale;//如大于窗口宽度，图片宽度进行缩放
            imgHeight = imgWidth / realWidth * realHeight;//等比例缩放高度
        } else {//如果图片真实高度和宽度都符合要求，高宽不变
            imgWidth = realWidth;
            imgHeight = realHeight;
        }
        $(bigimg).css("width", imgWidth);//以最终的宽度对图片缩放

        var w = (windowW - imgWidth) / 2;//计算图片与窗口左边距
        var h = (windowH - imgHeight) / 2;//计算图片与窗口上边距
        $(innerdiv).css({"top": h, "left": w});//设置#innerdiv的top和left属性
        $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
    });

    $(outerdiv).click(function () {//再次点击淡出消失弹出层
        $(this).fadeOut("fast");
    });
}

/*判断输入是否为空或空串*/
function isNull(str) {
    if (str == "") {
        return true;
    }
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}

//获取身份证中的信息
function getCardNoInfo(idno) {
    var info;
    if (isCardNo(idno)) {
        info = {};
        var birth = idno.substring(6, 10) + "-" + idno.substring(10, 12) + "-" + idno.substring(12, 14);
        info["birth"] = birth;
        var sex = "";
        if (parseInt(idno.substr(16, 1)) % 2 == 1) {
            sex = "1";
        } else {
            sex = "2";
        }
        info["sex"] = sex;
        //获取年龄
        var myDate = new Date();
        var month = myDate.getMonth() + 1;
        var day = myDate.getDate();
        var age = myDate.getFullYear() - idno.substring(6, 10) - 1;
        if (idno.substring(10, 12) < month || idno.substring(10, 12) == month && idno.substring(12, 14) <= day) {
            age++;
        }
        info["age"] = age;
    }
    return info;
}

//判断身份证号格式是否正确
function isCardNo(idno) {
    var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    var re = new RegExp(reg);
    return re.test(idno);
}

function openFile(id) {
    var html = '<div class="open-file">\n' +
        '    <i class="close fa fa-close"></i>\n' +
        '    <div class="file-iframe">\n' +
        '        <iframe src="/file/openFile/' + id + '"></iframe>\n' +
        '    </div>\n' +
        '</div>';
    var $html = $(html);
    $html.find(".close").bind("click", function () {
        $(".open-file").remove();
    });
    $("body").append($html);
}

//预览图片
function previewImg(imgPath) {
    var objE = document.createElement("div");
    objE.innerHTML = '<div class="bgM" >' +
        '<img src="' + imgPath + '"  id="img_scan" class="img-custom-img2"/>' +
        '</div>';
    document.body.appendChild(objE.children[0]);
    //退出图片预览事件
    var $bg = document.querySelector(".bgM");
    $bg.onclick = function () {
        var dm = document.querySelector(".bgM");
        document.body.removeChild(dm);
    };
    //阻止事件冒泡
    var $img = document.querySelector(".img-custom-img2");
    $img.onclick = function (event) {
        event.stopPropagation();
    }
}

/**
 * 除法运算，避免数据相除小数点后产生多位数和计算精度损失。
 *
 * @param num1
 * @param num2
 */
function numDiv(num1, num2) {
    var baseNum1 = 0, baseNum2 = 0;
    var baseNum3, baseNum4;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    with (Math) {
        baseNum3 = Number(num1.toString().replace(".", ""));
        baseNum4 = Number(num2.toString().replace(".", ""));
        return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
    }
}
