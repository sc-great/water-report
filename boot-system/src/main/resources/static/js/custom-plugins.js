/*
自定义插件*/
// 单选
function selectTree(params) {
    var tools = {
        url: '',
        data: {},
        objId: '',
        valueId: '',
        textId: '',
        parent_filed: 'parentId',
        value_filed: 'id',
        text_filed: 'name',
        openLevel: 0,
        curLevel: 1,
        isShow: false, // 点击以后是否继续显示下拉框
        checkBox: false,
        linkChecked: false, // 是否联动选择（选中父自动选中子）
        lastNodeShowCheckBox: false, // 最后节点显示checkbox
        placeholder: '请选择',
        class: 'form-control',
        required: '',
        defaultValue: '',
        defaultText: '',
        callback: '' // 回调函数
    };
    if (params != undefined) {
        $.extend(tools, params);
    }
    var html = '<input type="text" name="' + tools.textId + '" id="' + tools.textId + '" class="' + tools.class + '" ' + tools.required + ' placeholder="' + tools.placeholder + '" autocomplete="off" readonly="readonly" onclick="toggleTree(\'' + tools.objId + '\');" value="' + tools.defaultText + '">\n' +
        '                        <span class="input-icon fa fa-caret-down"></span>\n' +
        '                        <input name="' + tools.valueId + '" id="' + tools.valueId + '" type="hidden" value="' + tools.defaultValue + '">\n' +
        '                        <div class="tree" style="position: absolute;border-top: none;max-height: 300px;display:none;">\n';
    if (!tools.checkBox) {
        html += '                            <div class="node default">\n' +
        '                                <div class="node-text">\n' +
        '                                    <label>' + tools.placeholder + '</label>\n' +
        '                                </div>\n' +
        '                            </div>\n';
    }
    html += '                        </div>';
    var objHTML = $(html);
    if (!tools.checkBox) {
        objHTML.find(".node-text").click(function () {
            $("#" + tools.valueId).val("");
            $("#" + tools.textId).val("");
            $(this).closest(".tree").slideUp();
            if ('function' == typeof tools.callback) {
                tools.callback("");
            }
        });
    }
    $("#" + tools.objId).html(objHTML);

    loadData(tools.url, tools.data, $("#" + tools.objId + " .tree"), tools.checkBox, tools.linkChecked, tools.lastNodeShowCheckBox, tools.openLevel, 1, tools.valueId, tools.textId, tools.value_filed, tools.text_filed, tools.parent_filed, tools.callback, tools.isShow);
    $("body").bind("click", function (e) {
        if ($(e.target).closest("#" + tools.objId).length == 0 && !$("#" + tools.objId + " .tree").is(':hidden')) {
            toggleTree(tools.objId);
        }
    });
}
function loadData(url, params, obj, checkBox, linkChecked, lastNodeShowCheckBox, openLevel, curLevel, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow) {
    obj.children(".node:not(.default)").remove();
    $.ajax({
        url: url,
        type: "post",
        async: false,
        data: params,
        success: function (data) {
            loadHTML(url, params, data, obj, checkBox, linkChecked, lastNodeShowCheckBox, openLevel, curLevel, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow);
        }
    });
}
function loadHTML(url, params, sources, obj, checkBox, linkChecked, lastNodeShowCheckBox, openLevel, curLevel, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow) {
    $.each(sources, function (index, item) {
        var childCount = item.childCount;
        var value = eval("item." + value_filed);
        var text = eval("item." + text_filed);
        var parent = eval("item." + parent_filed);
        var html = '<div class="node">\n' +
            '                        <div class="node-text">\n' + (childCount > 0 ? '<span class="node-icon fa ' + ((openLevel == -1 || openLevel >= curLevel) ? 'fa-caret-down' : 'fa-caret-right') + '"></span>' : '') +
            '                            <label data-value="' + value + '" data-text="' + text + '" data-parent="' + parent + '">';
        if (checkBox) {
            var checkValue = obj.closest(".tree-select").children("input[type='hidden']").val();
            var checkValues = checkValue == "" ? "" : checkValue.split(",");
            var checked = $.inArray(value, checkValues) != -1 ? true : false;
            /*
            if (linkChecked) {
			    checked = $.inArray(parent, checkValues) != -1 ? true : false;
			}
             */
            if (lastNodeShowCheckBox) {
                if (childCount == 0) {
                    html += '<input type="checkbox" value="' + value + '" data-text="' + text + '" data-parent="' + parent + '" ' + (checked ? 'checked="checked"' : '') + '>';
                }
            } else {
                html += '<input type="checkbox" value="' + value + '" data-text="' + text + '" data-parent="' + parent + '" ' + (checked ? 'checked="checked"' : '') + '>';
            }
        }
        html += text + '</label>\n' +
        '</div>\n' +
        '</div>\n';
        var objHTML = $(html);
        if (lastNodeShowCheckBox) {
            objHTML.find(".node-text").click(function () {
                if ($(this).find(".node-icon").hasClass("fa-caret-right")) {
                    $(this).find(".node-icon").addClass("fa-caret-down").removeClass("fa-caret-right");
                    params[parent_filed] = value;
                    loadData(url, params, $(this).closest(".node"), checkBox, linkChecked, lastNodeShowCheckBox, 0, 1, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow);
                } else {
                    $(this).find(".node-icon").addClass("fa-caret-right").removeClass("fa-caret-down");
                    $(this).closest(".node").find(".node").slideUp("slow");
                }
            });
        } else {
            objHTML.find(".node-icon").click(function () {
                if ($(this).hasClass("fa-caret-right")) {
                    $(this).addClass("fa-caret-down").removeClass("fa-caret-right");
                    params[parent_filed] = value;
                    loadData(url, params, $(this).closest(".node"), checkBox, linkChecked, lastNodeShowCheckBox, 0, 1, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow);
                } else {
                    $(this).addClass("fa-caret-right").removeClass("fa-caret-down");
                    $(this).closest(".node").find(".node").slideUp("slow");
                }
            });
        }
        // 这里不判断checkBox，让他每次值返回一个
        objHTML.find(".node-text label").click(function () {
            var value = $(this).data("value");
            var text = $(this).data("text");
            $("#" + value_id).val(value);
            $("#" + text_id).val(text);
            if (!isShow)
            	$(this).closest(".tree").slideUp();
            if ('function' == typeof callback) {
                callback(value);
            }
        });
/*        
        if (!checkBox) {
            objHTML.find(".node-text label").click(function () {
                var value = $(this).data("value");
                var text = $(this).data("text");
                $("#" + value_id).val(value);
                $("#" + text_id).val(text);
                $(this).closest(".tree").slideUp();
                if ('function' == typeof callback) {
                    callback(value);
                }
            });
        } else {
            objHTML.find("input[type='checkbox']").bind("change", function () {
                var values = "",
                texts = "";
                objHTML.closest(".tree").find("input[type='checkbox']:checked").each(function (index, item) {
                    var value = $(item).val();
                    var text = $(item).data("text");
                    var parent = $(item).data("parent");
                    if (values == "") {
                        values = value;
                    } else {
                        values += "," + value;
                    }
                    if (texts == "") {
                        texts = text;
                    } else {
                        texts += "," + text;
                    }
                });
                var valueObj = objHTML.closest(".tree-select").children("input[type='hidden']");
                var textObj = objHTML.closest(".tree-select").children("input[type='text']");
                valueObj.val(values);
                textObj.val(texts);
*/
                /*
                if (linkChecked) {
				    if ($(this).is(":checked")) {
				        $(this).closest(".node-text").find(".node-icon").addClass("fa-caret-down").removeClass("fa-caret-right");
				    } else {
				        $(this).closest(".node-text").find(".node-icon").addClass("fa-caret-right").removeClass("fa-caret-down");
				        $(this).closest(".node-text").find(".node-icon").closest(".node").find(".node").slideUp("slow", function () {
				            $(this).remove();
				        });
				    }
				    if ($(this).closest(".node").children(".node").length > 0) {}
				    else {
				        var value = $(this).val();
				        params[parent_filed] = value;
				        loadData(url, params, $(this).closest(".node"), checkBox, linkChecked, lastNodeShowCheckBox, 0, 1, value_id, text_id, value_filed, text_filed, parent_filed, isShow);
				    }
				}
                 */
/*                if ('function' == typeof callback) {
                    callback(values);
                }
            });
        }*/
        obj.append(objHTML);
        if (childCount > 0 && (openLevel == -1 || openLevel >= curLevel)) {
            params[parent_filed] = value;
            loadData(url, params, objHTML, checkBox, linkChecked, lastNodeShowCheckBox, openLevel, curLevel + 1, value_id, text_id, value_filed, text_filed, parent_filed, callback, isShow);
        }
    });
}
// 单选
// 显示树形下拉控件
function toggleTree(objId) {
    if ($("#" + objId).children(".input-icon").hasClass("fa-caret-up")) {
        $("#" + objId).children(".input-icon").addClass("fa-caret-down").removeClass("fa-caret-up");
    } else {
        $("#" + objId).children(".input-icon").addClass("fa-caret-up").removeClass("fa-caret-down");
    }
    $("#" + objId).children(".tree").slideToggle();
}

// end 树形控件
// 判断身份证号格式是否正确
function isCardNo(idno) {
    var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    var re = new RegExp(reg);
    return re.test(idno);
}
// 获取身份证中的信息
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
        // 获取年龄
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

// 步骤控件初始化
function stepTabInit() {
    // 下一步
    $(".btn-next-step").click(function () {
        var $step = $(this).closest(".step");
        var isNext = true;
        if ($(this).data("click") != undefined) {
            var result = eval($(this).data("click"));
            if (!result) {
                isNext = false;
            }
        } else {
            $(this).closest(".tab-item").find("input[required],select[required]").each(function (index, item) {
                if ($(item).val().trim() == "") {
                    var msg = $(item).data("tips") == undefined ? "该项必填！" : $(item).data("tips");
                    layer.tips(msg, $(item), {
                        tips: [1, '#3595CC']
                    });
                    isNext = false;
                    return false;
                }
            });
        }
        if (isNext) {
            $(this).closest(".tab-item").removeClass("active").next().addClass("active");
            $step.find(".tab-title li").eq($step.find(".tab-content .tab-item.active").index()).addClass("active").siblings("li").removeClass("active");
        }
    });
    // 上一步
    $(".btn-prev-step").click(function () {
        var $step = $(this).closest(".step");
        $(this).closest(".tab-item").removeClass("active").prev().addClass("active");
        $step.find(".tab-title li").eq($step.find(".tab-content .tab-item.active").index()).addClass("active").siblings("li").removeClass("active");
    });
}
