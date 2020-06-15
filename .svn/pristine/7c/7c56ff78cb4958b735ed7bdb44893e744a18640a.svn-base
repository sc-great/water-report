/**
 * 通用js方法封装处理
 * Copyright (c) 2019 epl
 */
(function ($) {
    $.extend({
        _tree: {},
        btTable: {},
        bttTable: {},
        // 表格封装处理
        table: {
            _option: {},
            // 初始化表格参数
            init: function (options) {
                var defaults = {
                    id: "bootstrap-table",
                    type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: undefined,
                    sidePagination: "server",
                    sortName: "",
                    sortOrder: "asc",
                    pagination: true,
                    pageSize: 10,
                    pageList: [10, 25, 50],
                    toolbar: "toolbar",
                    striped: false,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    clickToSelect: false,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    rightFixedColumns: false,
                    rightFixedNumber: 0,
                    queryParams: $.table.queryParams,
                    rowStyle: {}
                };
                var options = $.extend(defaults, options);
                $.table._option = options;
                $.btTable = $('#' + options.id);
                $.table.initEvent();
                var params = {
                    url: options.url,                                   // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",   // 编码类型
                    method: 'post',                                     // 请求方式（*）
                    cache: false,                                       // 是否使用缓存
                    height: options.height,                             // 表格的高度
                    striped: options.striped,                           // 是否显示行间隔色
                    sortable: true,                                     // 是否启用排序
                    sortStable: true,                                   // 设置为 true 将获得稳定的排序
                    sortName: options.sortName,                         // 排序列名称
                    sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
                    pagination: options.pagination,                     // 是否显示分页（*）
                    pageNumber: 1,                                      // 初始化加载第一页，默认第一页
                    pageSize: options.pageSize,                         // 每页的记录行数（*）
                    pageList: options.pageList,                         // 可供选择的每页的行数（*）
                    firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
                    escape: options.escape,                             // 转义HTML字符串
                    showFooter: options.showFooter,                     // 是否显示表尾
                    iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
                    search: options.search,                             // 是否显示搜索框功能
                    searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showPageGo: options.showPageGo,               		// 是否显示跳转页
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
                    showExport: options.showExport,                     // 是否支持导出文件
                    uniqueId: options.uniqueId,                         // 唯 一的标识符
                    clickToSelect: options.clickToSelect,				// 是否启用点击选中行
                    detailView: options.detailView,                     // 是否启用显示细节视图
                    onClickRow: options.onClickRow,                     // 点击某行触发的事件
                    onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
                    onClickCell: options.onClickCell,                   // 单击某格触发的事件
                    onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
                    rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
                    fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
                    fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
                    rightFixedColumns: options.rightFixedColumns,       // 是否启用冻结列（右侧）
                    rightFixedNumber: options.rightFixedNumber,         // 列冻结的个数（右侧）
                    onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
                    queryParams: options.queryParams,                   // 传递参数（*）
                    rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
                    onToggle: $.table.onToggle,               // 切换视图事件
                    onPreBody: options.onPreBody,                       //在对表格体进行渲染前触发
                    exportOptions: options.exportOptions,               // 前端导出忽略列索引
                    detailFormatter: options.detailFormatter            // 在行下面展示其他数据列表

                };
                if (typeof options.onExpandRow == "function") {
                    params["onExpandRow"] = options.onExpandRow;
                }
                $('#' + options.id).bootstrapTable(params);
            },
            // 查询条件
            queryParams: function (params) {
                var curParams = {
                    // 传递参数查询参数
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    searchValue: params.search,
                    orderByColumn: params.sort,
                    isAsc: params.order
                };
                var currentId = $.common.isEmpty($.table._option.formId) ? $('form').attr('id') : $.table._option.formId;
                return $.extend(curParams, $.common.formToJSON(currentId));
            },
            // 请求获取数据后处理回调函数
            responseHandler: function (res) {
                if (typeof $.table._option.responseHandler == "function") {
                    $.table._option.responseHandler(res);
                }
                if (res.code == 0) {
                    if ($.common.isNotEmpty($.table._option.sidePagination) && $.table._option.sidePagination == 'client') {
                        return res.rows;
                    } else {
                        if ($.common.isNotEmpty($.table._option.rememberSelected) && $.table._option.rememberSelected) {
                            var column = $.common.isEmpty($.table._option.uniqueId) ? $.table._option.columns[1].field : $.table._option.uniqueId;
                            $.each(res.rows, function (i, row) {
                                row.state = $.inArray(row[column], selectionIds) !== -1;
                            })
                        }
                        return {rows: res.rows, total: res.total};
                    }
                } else {
                    $.modal.alertWarning(res.msg);
                    return {rows: [], total: 0};
                }
            },
            // 初始化事件
            initEvent: function (data) {
                // 触发行点击事件 加载成功事件
                $.btTable.on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
                    // 工具栏按钮控制
                    var rows = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
                    // 非多个禁用
                    $('#' + $.table._option.toolbar + ' .multiple').toggleClass('disabled', !rows.length);
                    // 非单个禁用
                    $('#' + $.table._option.toolbar + ' .single').toggleClass('disabled', rows.length != 1);
                });
                // 绑定选中事件、取消事件、全部选中、全部取消
                $.btTable.on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rows) {
                    // 复选框分页保留保存选中数组
                    var rowIds = $.table.affectedRowIds(rows);
                    if ($.common.isNotEmpty($.table._option.rememberSelected) && $.table._option.rememberSelected) {
                        func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
                        selectionIds = _[func](selectionIds, rowIds);
                    }
                });
                // 图片预览事件
                $.btTable.on('click', '.img-circle', function () {
                    var src = $(this).attr('src');
                    var target = $(this).data('target');
                    var height = $(this).data('height');
                    var width = $(this).data('width');
                    if ($.common.equals("self", target)) {
                        layer.open({
                            title: false,
                            type: 1,
                            closeBtn: true,
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
                        });
                    } else if ($.common.equals("blank", target)) {
                        window.open(src);
                    }
                });
                // 单击tooltip复制文本
                $.btTable.on('click', '.tooltip-show', function () {
                    var input = $(this).prev();
                    input.select();
                    document.execCommand("copy");
                });
            },
            //切换视图事件
            onToggle: function (cardView) {
                $.table.toggleToolsBar();
            },
            // 当所有数据被加载时触发
            onLoadSuccess: function (data) {
                $.table.toggleToolsBar();
                if (typeof $.table._option.onLoadSuccess == "function") {
                    $.table._option.onLoadSuccess(data);
                }
                // 浮动提示框特效
                $("[data-toggle='tooltip']").tooltip();
            },
            // 表格销毁
            destroy: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? $.table._option.id : tableId;
                $("#" + currentId).bootstrapTable('destroy');
            },
            // 序列号生成
            serialNumber: function (index) {
                var table = $.btTable.bootstrapTable('getOptions');
                var pageSize = table.pageSize;
                var pageNumber = table.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            },
            // 列超出指定长度浮动提示（单击文本复制）
            tooltip: function (value, length) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g, "’");
                    var actions = [];
                    actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf("<a href='###' class='tooltip-show' data-toggle='tooltip' title='%s'>%s</a>", _value, _text));
                    return actions.join('');
                } else {
                    _text = _value;
                    return _text;
                }
            },
            // 下拉按钮切换
            dropdownToggle: function (value) {
                var actions = [];
                actions.push('<div class="btn-group">');
                actions.push('<button type="button" class="btn btn-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false">');
                actions.push('<i class="fa fa-cog"></i>&nbsp;<span class="fa fa-chevron-down"></span></button>');
                actions.push('<ul class="dropdown-menu">');
                actions.push(value.replace(/<a/g, "<li><a").replace(/<\/a>/g, "</a></li>"));
                actions.push('</ul>');
                actions.push('</div>');
                return actions.join('');
            },
            // 图片预览
            imageView: function (value, height, width, target) {
                if ($.common.isEmpty(width)) {
                    width = 'auto';
                }
                if ($.common.isEmpty(height)) {
                    height = 'auto';
                }
                // blank or self
                var _target = $.common.isEmpty(target) ? 'self' : target;
                if ($.common.isNotEmpty(value)) {
                    return $.common.sprintf("<img class='img-circle img-xs' data-height='%s' data-width='%s' data-target='%s' src='%s'/>", width, height, _target, value);
                } else {
                    return $.common.nullToStr(value);
                }
            },
            // 搜索-默认第一个form
            search: function (formId, data) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.btTable.bootstrapTable('getOptions');
                params.queryParams = function (params) {
                    var search = $.common.formToJSON(currentId);
                    if ($.common.isNotEmpty(data)) {
                        $.each(data, function (key) {
                            search[key] = data[key];
                        });
                    }
                    search.pageSize = params.limit;
                    search.pageNum = params.offset / params.limit + 1;
                    search.searchValue = (search.searchValue != undefined && search.searchValue != '') ? search.searchValue : params.search;
                    search.orderByColumn = params.sort;
                    search.isAsc = params.order;
                    return search;
                };
                $.btTable.bootstrapTable('refresh', params);
            },
            // 导出数据
            exportExcel: function (formId) {
                $.modal.confirm("确定导出今日" + $.table._option.modalName + "吗？", function () {
                    var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                    $.modal.loading("正在导出数据，请稍后...");
                    $.post($.table._option.exportUrl, $("#" + currentId).serializeArray(), function (result) {
                        if (result.code == web_status.SUCCESS) {
                            window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    });
                });
            },
            // 下载模板
            importTemplate: function () {
                $.get($.table._option.importTemplateUrl, function (result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                    } else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
                        $.modal.alertError(result.msg);
                    }
                });
            },
            importTemplate: function (importTemplateUrl) {
                $.get(importTemplateUrl, function (result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                    } else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
                        $.modal.alertError(result.msg);
                    }
                });
            },
            // 导入数据
            importExcel: function (formId) {
                var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
                layer.open({
                    type: 1,
                    area: ['400px', '230px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: '导入' + $.table._option.modalName + '数据',
                    content: $('#' + currentId).html(),
                    btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    btn1: function (index, layero) {
                        var file = layero.find('#file').val();
                        if (file == '' || (!$.common.endWith(file, '.xls') && !$.common.endWith(file, '.xlsx'))) {
                            $.modal.msgWarning("请选择后缀为 “xls”或“xlsx”的文件。");
                            return false;
                        }
                        var index = layer.load(2, {shade: false});
                        $.modal.disable();
                        var formData = new FormData();
                        formData.append("file", $('#file')[0].files[0]);
                        formData.append("updateSupport", $("input[name='updateSupport']").is(':checked'));
                        $.ajax({
                            url: $.table._option.importUrl,
                            data: formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            success: function (result) {
                                if (result.code == web_status.SUCCESS) {
                                    $.modal.closeAll();
                                    $.modal.alertSuccess(result.msg);
                                    $.table.refresh();
                                } else if (result.code == web_status.WARNING) {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertWarning(result.msg)
                                } else {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertError(result.msg);
                                }
                            }
                        });
                    }
                });
            },
            // 刷新表格
            refresh: function () {
                $.btTable.bootstrapTable('refresh', {
                    silent: true
                });
            },
            // 查询表格指定列值
            selectColumns: function (column) {
                var rows = $.map($.btTable.bootstrapTable('getSelections'), function (row) {
                    return row[column];
                });
                if ($.common.isNotEmpty($.table._option.rememberSelected) && $.table._option.rememberSelected) {
                    rows = rows.concat(selectionIds);
                }
                return $.common.uniqueFn(rows);
            },
            // 获取当前页选中或者取消的行ID
            affectedRowIds: function (rows) {
                var column = $.common.isEmpty($.table._option.uniqueId) ? $.table._option.columns[1].field : $.table._option.uniqueId;
                var rowIds;
                if ($.isArray(rows)) {
                    rowIds = $.map(rows, function (row) {
                        return row[column];
                    });
                } else {
                    rowIds = [rows[column]];
                }
                return rowIds;
            },
            // 查询表格首列值
            selectFirstColumns: function () {
                var rows = $.map($.btTable.bootstrapTable('getSelections'), function (row) {
                    return row[$.table._option.columns[1].field];
                });
                if ($.common.isNotEmpty($.table._option.rememberSelected) && $.table._option.rememberSelected) {
                    rows = rows.concat(selectionIds);
                }
                return $.common.uniqueFn(rows);
            },
            // 回显数据字典
            selectDictLabel: function (datas, value) {
                var actions = [];
                $.each(datas, function (index, dict) {
                    if (dict.dictValue == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
            // 显示表格指定列
            showColumn: function (column) {
                $.btTable.bootstrapTable('showColumn', column);
            },
            // 隐藏表格指定列
            hideColumn: function (column) {
                $.btTable.bootstrapTable('hideColumn', column);
            },
            //更多操作按钮
            toggleToolsBar: function () {
                var tableId = $.table._option.id;
                $("#" + tableId).find("tbody tr").each(function (index, item) {
                    var $_row = $(item);
                    if ($_row.children("td").length > 2) {
                        var $_last_td = $_row.find("td:last");
                        if ($_last_td.find(".btn").length > 2) {
                            var toolsBarWidth = 0;
                            $_last_td.find(".btn").each(function (index, btn) {
                                toolsBarWidth = toolsBarWidth + $(btn).outerWidth();
                            });
                            var toolsBar = $_last_td.html();
                            var html = '<div class="btn-option-more">' +
                                '<a class="btn btn-primary btn-xs btn-option-show" href="javascript:void(0)"><i class="fa fa-angle-left"></i>&nbsp;&nbsp;操作</a>' +
                                '<div class="btn-group" style="width: ' + toolsBarWidth + 'px;">' + toolsBar + '</div>' +
                                '</div>';
                            var objHTML = $(html);
                            objHTML.hover(function () {
                                $(this).find(".btn-option-show").hide();
                                $(this).find(".btn-group").show();
                            }, function () {
                                $(this).find(".btn-option-show").show();
                                $(this).find(".btn-group").hide();
                            });
                            $_last_td.html(objHTML);
                        }
                    }
                });
            }
        },
        // 表格树封装处理
        treeTable: {
            // 初始化表格
            init: function (options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 0,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                $.table._option = options;
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,                                 // 用于设置父子关系
                    parentCode: options.parentCode,                     // 用于设置父子关系
                    type: 'post',                                       // 请求方式（*）
                    url: options.url,                                   // 请求后台的URL（*）
                    data: options.data,                                 // 无url时用于渲染的数据
                    ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
                    rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
                    height: options.height,                             // 表格树的高度
                    expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
                    striped: options.striped,                           // 是否显示行间隔色
                    bordered: true,                                     // 是否显示边框
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    expandAll: options.expandAll,                       // 是否全部展开
                    expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.treeTable.responseHandler        // 当所有数据被加载时触发处理函数
                });
            },
            // 条件查询
            search: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params);
            },
            // 刷新
            refresh: function () {
                $.bttTable.bootstrapTreeTable('refresh');
            },
            // 查询表格树指定列值
            selectColumns: function (column) {
                var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function (row) {
                    return row[column];
                });
                return $.common.uniqueFn(rows);
            },
            // 请求获取数据后处理回调函数，校验异常状态提醒
            responseHandler: function (data) {
                if (data.code != undefined && data.code != 0) {
                    $.modal.alertWarning(data.msg);
                    return [];
                } else {
                    return data;
                }
            },
        },
        // 表单封装处理
        form: {
            // 表单重置
            reset: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
                if ($.table._option.type == table_type.bootstrapTable) {
                    $.btTable.bootstrapTable('refresh');
                }
            },
            // 获取选中复选框项
            selectCheckeds: function (name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    } else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            // 获取选中下拉框项
            selectSelects: function (name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    } else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }
        },
        // 弹出层封装处理
        modal: {
            // 显示图标
            icon: function (type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0;
                } else if (type == modal_status.SUCCESS) {
                    icon = 1;
                } else if (type == modal_status.FAIL) {
                    icon = 2;
                } else {
                    icon = 3;
                }
                return icon;
            },
            // 消息提示
            msg: function (content, type) {
                if (type != undefined) {
                    layer.msg(content, {icon: $.modal.icon(type), time: 3000, shift: 5});
                } else {
                    layer.msg(content);
                }
            },
            // 错误消息
            msgError: function (content) {
                $.modal.msg(content, modal_status.FAIL);
            },
            // 成功消息
            msgSuccess: function (content) {
                $.modal.msg(content, modal_status.SUCCESS);
            },
            // 警告消息
            msgWarning: function (content) {
                $.modal.msg(content, modal_status.WARNING);
            },
            // 弹出提示
            alert: function (content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "系统提示",
                    btn: ['确认'],
                    btnclass: ['btn btn-primary']
                });
            },
            // 消息提示并刷新父窗体
            msgReload: function (msg, type) {
                layer.msg(msg, {
                        icon: $.modal.icon(type),
                        time: 500,
                        shade: [0.1, '#8F8F8F']
                    },
                    function () {
                        $.modal.reload();
                    });
            },
            // 错误提示
            alertError: function (content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            // 成功提示
            alertSuccess: function (content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            // 警告提示
            alertWarning: function (content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            // 关闭窗体
            close: function () {
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            },
            // 关闭全部窗体
            closeAll: function () {
                layer.closeAll();
            },
            // 确认窗体
            confirm: function (content, callBack) {
                layer.confirm(content, {
                    icon: 3,
                    title: "系统提示",
                    btn: ['确认', '取消']
                }, function (index) {
                    layer.close(index);
                    callBack(true);
                });
            },
            //弹出消息框
            openMsg: function (title, msg, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                layer.open({
                    type: 1,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: msg,
                    // 弹层外区域关闭
                    shadeClose: true
                });
            },
            // 弹出层指定宽度
            open: function (title, url, width, height, callback) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function (index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: callback,
                    cancel: function (index) {
                        return true;
                    }
                });
            },
            // 弹出层指定参数选项
            openOptions: function (options) {
                var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
                var _width = $.common.isEmpty(options.width) ? "800" : options.width;
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
                if ($.common.isEmpty(options.yes)) {
                    options.yes = function (index, layero) {
                        options.callBack(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    maxmin: true,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width + 'px', _height + 'px'],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : (options.btn == 'none' ? '' : options.btn),
                    yes: options.yes,
                    cancel: function () {
                        return true;
                    }
                });
            },
            // 弹出层全屏
            openFull: function (title, url, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                var index = layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: function (index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    },
                    cancel: function (index) {
                        return true;
                    }
                });
                layer.full(index);
            },
            // 选卡页方式打开
            openTab: function (title, url) {
                createMenuItem(url, title);
            },
            // 选卡页同一页签打开
            parentTab: function (title, url) {
                var dataId = window.frameElement.getAttribute('data-id');
                createMenuItem(url, title);
                closeItem(dataId);
            },
            // 关闭选项卡
            closeTab: function (dataId) {
                closeItem(dataId);
            },
            // 禁用按钮
            disable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            // 启用按钮
            enable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            // 打开遮罩层
            loading: function (message) {
                $.blockUI({message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>'});
            },
            // 关闭遮罩层
            closeLoading: function () {
                setTimeout(function () {
                    $.unblockUI();
                }, 50);
            },
            // 重新加载
            reload: function () {
                parent.location.reload();
            },
            //树形选择页面（数据少）
            abandonSelTree: function (params) {
                var tools = {
                    title: "请选择",//窗口标题
                    url: "",//连接地址
                    type: "radio",//单选
                    width: 380,//宽度
                    valueId: "",//返回编号控件id
                    nameId: "",//返回文本控件id
                    isCheckParent: false,//是否返回上级编号
                    parentId: "",//返回上级编号控件id
                    callBack: function (index, layero) {
                        if (tools.type == "checkbox") {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getCheckedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            //筛选出选中的节点
                            var idsArr = [], namesArr = [], parentIdsArr = [];
                            $.each(nodes, function (index, item) {
                                if (!item.isParent) {
                                    idsArr.push(item.id);
                                    namesArr.push(item.name);
                                    parentIdsArr.push(item.pId);
                                }
                            });
                            $("#" + tools.valueId).val(idsArr.join(","));
                            $("#" + tools.nameId).val(namesArr.join(","));
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val($.common.uniqueFn(parentIdsArr).join(","));
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(idsArr.join(","));
                            }
                        } else {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getSelectedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            var selNode = nodes[0];
                            //不能选中父节点
                            if (!tools.isCheckParent) {
                                if (selNode.isParent) {
                                    $.modal.msgError("不能选择父节点（" + selNode.name + "）");
                                    return false;
                                }
                            }
                            $("#" + tools.valueId).val(selNode.id);
                            $("#" + tools.nameId).val(selNode.name);
                            //获取父节点ID
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val(selNode.pId);
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(selNode.id);
                            }
                        }
                        layer.close(index);
                    },//回调函数
                    callBacked: ""//全部执行完成后调用的函数
                };
                $.extend(tools, params);
                var url;
                if ($.common.isNotEmpty(tools.valueId)) {
                    var checked = $("#" + tools.valueId).val();
                    url = tools.url + "?type=" + tools.type + "&checkValue=" + checked;
                    if ($.common.isNotEmpty(tools.parentId)) {
                        var parentValue = $("#" + tools.parentId).val();
                        url = tools.url + "?type=" + tools.type + "&checkValue=" + checked + "&parentValue=" + parentValue;
                        if ($.common.isNotEmpty(tools.parentPreId)) {
                            var parentPreValue = $("#" + tools.parentPreId).val();
                            url = tools.url + "?type=" + tools.type + "&checkValue=" + checked + "&parentValue=" + parentValue + "&parentPreValue=" + parentPreValue;
                        }
                    }
                } else {
                    url = tools.url;
                }
                var options = {
                    title: tools.title,
                    width: tools.width,
                    url: url,
                    callBack: tools.callBack
                };
                $.modal.openOptions(options);
            },
            //树形选择页面（数据大，sessionStorage存储）
            sessionTree: function (params) {
                var tools = {
                    title: "请选择",//窗口标题
                    url: "",//连接地址
                    type: "radio",//单选
                    width: 380,//宽度
                    valueId: "",//返回编号控件id
                    nameId: "",//返回文本控件id
                    isCheckParent: false,//是否返回上级编号
                    parentId: "",//返回上级编号控件id
                    callBack: function (index, layero) {
                        sessionStorage.clear();
                        if (tools.type == "checkbox") {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getCheckedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            //筛选出选中的节点
                            var idsArr = [], namesArr = [], parentIdsArr = [];
                            $.each(nodes, function (index, item) {
                                if (!item.isParent) {
                                    idsArr.push(item.id);
                                    namesArr.push(item.name);
                                    parentIdsArr.push(item.pId);
                                }
                            });
                            $("#" + tools.valueId).val(idsArr.join(","));
                            $("#" + tools.nameId).val(namesArr.join(","));
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val($.common.uniqueFn(parentIdsArr).join(","));
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(idsArr.join(","));
                            }
                        } else {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getSelectedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            var selNode = nodes[0];
                            //不能选中父节点
                            if (!tools.isCheckParent) {
                                if (selNode.isParent) {
                                    $.modal.msgError("不能选择父节点（" + selNode.name + "）");
                                    return false;
                                }
                            }
                            $("#" + tools.valueId).val(selNode.id);
                            $("#" + tools.nameId).val(selNode.name);
                            //获取父节点ID
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val(selNode.pId);
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(selNode.id);
                            }
                        }
                        layer.close(index);
                    },//回调函数
                    callBacked: ""//全部执行完成后调用的函数
                };
                $.extend(tools, params);
                var options = {
                    title: tools.title,
                    width: tools.width,
                    url: tools.url,
                    callBack: tools.callBack
                };
                //将回显数据存储到sessionStorage
                sessionStorage.setItem("userIds", $("#" + tools.valueId).val());
                sessionStorage.setItem("orgIds", $("#" + tools.parentId).val());
                $.modal.openOptions(options);
            },
            //树形选择页面（数据大，父页面获取）
            selTree: function (params) {
                var tools = {
                    title: "请选择",//窗口标题
                    url: "",//连接地址
                    type: "radio",//单选
                    width: 380,//宽度
                    valueId: "",//返回编号控件id
                    nameId: "",//返回文本控件id
                    isCheckParent: false,//是否返回上级编号
                    parentId: "",//返回上级编号控件id
                    callBack: function (index, layero) {
                        if (tools.type == "checkbox") {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getCheckedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            //筛选出选中的节点
                            var idsArr = [], namesArr = [], parentIdsArr = [];
                            $.each(nodes, function (index, item) {
                                if (!item.isParent) {
                                    idsArr.push(item.id);
                                    namesArr.push(item.name);
                                    parentIdsArr.push(item.pId);
                                }
                            });
                            $("#" + tools.valueId).val(idsArr.join(","));
                            $("#" + tools.nameId).val(namesArr.join(","));
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val($.common.uniqueFn(parentIdsArr).join(","));
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(idsArr.join(","));
                            }
                        } else {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = layero.find("iframe")[0].contentWindow.$._tree.getSelectedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgError("请选择节点后提交");
                                return false;
                            }
                            var selNode = nodes[0];
                            //不能选中父节点
                            if (!tools.isCheckParent) {
                                if (selNode.isParent) {
                                    $.modal.msgError("不能选择父节点（" + selNode.name + "）");
                                    return false;
                                }
                            }
                            $("#" + tools.valueId).val(selNode.id);
                            $("#" + tools.nameId).val(selNode.name);
                            //获取父节点ID
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val(selNode.pId);
                            }
                            if (tools.callBacked != "") {
                                tools.callBacked(selNode.id);
                            }
                        }
                        layer.close(index);
                    },//回调函数
                    callBacked: ""//全部执行完成后调用的函数
                };
                $.extend(tools, params);
                var url;
                if ($.common.isNotEmpty(tools.valueId)) {
                    var checkedDiv = tools.valueId;
                    url = tools.url + "?type=" + tools.type + "&checkDiv=" + checkedDiv;
                    if ($.common.isNotEmpty(tools.parentId)) {
                        var parentDiv = tools.parentId;
                        url = tools.url + "?type=" + tools.type + "&checkDiv=" + checkedDiv + "&parentDiv=" + parentDiv;
                        if ($.common.isNotEmpty(tools.parentPreId)) {
                            var parentPreDiv = tools.parentPreId;
                            url = tools.url + "?type=" + tools.type + "&checkDiv=" + checkedDiv + "&parentDiv=" + parentDiv + "&parentPreDiv=" + parentPreDiv;
                        }
                    }
                } else {
                    url = tools.url;
                }
                var options = {
                    title: tools.title,
                    width: tools.width,
                    url: url,
                    callBack: tools.callBack
                };
                $.modal.openOptions(options);
            },
            //树形选择页面（post请求）
            postTree: function (params) {
                var tools = {
                    title: "请选择",  //窗口标题
                    url: "",          //连接地址
                    type: "radio",    //单选
                    width: "380px",   //宽度
                    height: "90%",    //高度
                    valueId: "",      //返回编号控件id
                    nameId: "",       //返回文本控件id
                    isCheckParent: false,//是否返回上级编号
                    parentId: "",        //返回上级编号控件id
                    callBack: function (index, layero) {
                        if (tools.type == "checkbox") {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = $._tree.getCheckedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgWarning("请选择节点后提交");
                                return false;
                            }
                            //筛选出选中的节点
                            var idsArr = [], namesArr = [], parentIdsArr = [];
                            $.each(nodes, function (index, item) {
                                if (!item.isParent) {
                                    idsArr.push(item.id);
                                    namesArr.push(item.name);
                                    parentIdsArr.push(item.pId);
                                }
                            });
                            $("#" + tools.valueId).val(idsArr.join(","));
                            $("#" + tools.nameId).val(namesArr.join(","));
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val($.common.uniqueFn(parentIdsArr).join(","));
                            }
                        } else {
                            //点击确定的回掉函数，获取选中分类节点
                            var nodes = $._tree.getSelectedNodes();
                            if (nodes.length == 0) {
                                $.modal.msgWarning("请选择节点后提交");
                                return false;
                            }
                            var selNode = nodes[0];
                            //不能选中父节点
                            if (!tools.isCheckParent) {
                                if (selNode.isParent) {
                                    $.modal.msgError("不能选择父节点（" + selNode.name + "）");
                                    return false;
                                }
                            }
                            $("#" + tools.valueId).val(selNode.id);
                            $("#" + tools.nameId).val(selNode.name);
                            //获取父节点ID
                            if (tools.parentId != "") {
                                $("#" + tools.parentId).val(selNode.pId);
                            }
                        }
                        layer.close(index);
                    }
                };
                $.extend(tools, params);
                $.ajax({
                    type: 'post',
                    url: tools.url,
                    data: {
                        type: tools.type,
                        checkValue: $("#" + tools.valueId).val(),
                        parentValue: $("#" + tools.parentId).val()
                    },
                    dataType: "html",
                    success: function (result) {
                        layer.open({
                            type: 1,          //弹出框类型(页面层)
                            title: tools.title,
                            shadeClose: false, //点击遮罩关闭层
                            area: [tools.width, tools.height],
                            maxmin: true,
                            shade: 0.3,
                            fix: false,
                            btn: ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'],
                            content: result,   //将结果页面放入layer弹出层中
                            yes: function (index, layero) {
                                tools.callBack(index, layero);
                            }
                        });
                    }
                });
            }
        },
        // 操作封装处理
        operate: {
            // 提交数据
            submit: function (url, type, dataType, data, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    }
                };
                $.ajax(config)
            },
            // post请求传输
            post: function (url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            // get请求传输
            get: function (url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            },
            // 详细信息-弹窗
            detail: function (id, width, height) {
                var _url = $.operate.detailUrl(id);
                var options = {
                    title: $.table._option.modalName + "详细",
                    width: width,
                    height: height,
                    url: $.operate.detailUrl(id),
                    skin: 'layui-layer-gray',
                    btn: ['关闭'],
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                };
                $.modal.openOptions(options);
            },
            //详细信息，以tab页展现
            detailTab: function (id) {
                $.modal.openTab($.table._option.modalName + "详细", $.operate.detailUrl(id));
            },
            // 详细访问地址
            detailUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = $.table._option.detailUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = $.table._option.detailUrl.replace("{id}", id);
                }
                return url;
            },
            // 删除信息
            remove: function (id) {
                $.modal.confirm("确定删除该条" + $.table._option.modalName + "信息吗？", function () {
                    var url = $.common.isEmpty(id) ? $.table._option.removeUrl : $.table._option.removeUrl.replace("{id}", id);
                    if ($.table._option.type == table_type.bootstrapTreeTable) {
                        $.operate.get(url);
                    } else {
                        var data = {"ids": id};
                        $.operate.submit(url, "post", "json", data);
                    }
                });
            },
            // 批量删除信息
            removeAll: function () {
                var rows = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function () {
                    var url = $.table._option.removeUrl;
                    var data = {"ids": rows.join()};
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 清空信息
            clean: function () {
                $.modal.confirm("确定清空所有" + $.table._option.modalName + "吗？", function () {
                    var url = $.table._option.cleanUrl;
                    $.operate.submit(url, "post", "json", "");
                });
            },
            // 添加信息
            add: function (id) {
                $.modal.open("添加" + $.table._option.modalName, $.operate.addUrl(id));
            },
            // 添加信息，以tab页展现
            addTab: function (id) {
                $.modal.openTab("添加" + $.table._option.modalName, $.operate.addUrl(id));
            },
            // 添加信息 全屏
            addFull: function (id) {
                var url = $.common.isEmpty(id) ? $.table._option.createUrl : $.table._option.createUrl.replace("{id}", id);
                $.modal.openFull("添加" + $.table._option.modalName, url);
            },
            // 添加访问地址
            addUrl: function (id) {
                return $.common.isEmpty(id) ? $.table._option.createUrl.replace("{id}", "") : $.table._option.createUrl.replace("{id}", id);
            },
            // 修改信息
            edit: function (id) {
                if ($.common.isEmpty(id) && $.table._option.type == table_type.bootstrapTreeTable) {
                    var row = $.bttTable.bootstrapTreeTable('getSelections')[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    var url = $.table._option.updateUrl.replace("{id}", row[$.table._option.uniqueId]);
                    $.modal.open("修改" + $.table._option.modalName, url);
                } else {
                    $.modal.open("修改" + $.table._option.modalName, $.operate.editUrl(id));
                }
            },
            // 修改信息，以tab页展现
            editTab: function (id) {
                $.modal.openTab("修改" + $.table._option.modalName, $.operate.editUrl(id));
            },
            // 修改信息 全屏
            editFull: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = $.table._option.updateUrl.replace("{id}", id);
                } else {
                    var row = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
                    url = $.table._option.updateUrl.replace("{id}", row);
                }
                $.modal.openFull("修改" + $.table._option.modalName, url);
            },
            // 修改访问地址
            editUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = $.table._option.updateUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty($.table._option.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = $.table._option.updateUrl.replace("{id}", id);
                }
                return url;
            },
            // 保存信息 刷新表格
            save: function (url, data, callback, type) {
                if (type == undefined) {
                    type = "post";
                }
                var config = {
                    url: url,
                    type: type,
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                        $.modal.disable();
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                $.ajax(config)
            },
            // 保存信息 弹出提示框
            saveModal: function (url, data, callback, type) {
                if (type == undefined) {
                    type = "post";
                }
                var config = {
                    url: url,
                    type: type,
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        } else {
                            if (result.code == web_status.SUCCESS) {
                                $.modal.alertSuccess(result.msg);
                            } else if (result.code == web_status.WARNING) {
                                $.modal.alertWarning(result.msg);
                            } else {
                                $.modal.alertError(result.msg);
                            }
                        }
                        $.modal.closeLoading();
                    }
                };
                $.ajax(config)
            },
            // 保存选项卡信息
            saveTab: function (url, data, callback, type) {
                if (type == undefined) {
                    type = "post";
                }
                var config = {
                    url: url,
                    type: type,
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successTabCallback(result);
                    }
                };
                $.ajax(config);
            },
            // 保存选项卡信息(包含文件对象)
            saveFileTab: function (url, data, callback, type) {
                if (type == undefined) {
                    type = "post";
                }
                var config = {
                    url: url,
                    type: type,
                    dataType: "json",
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successTabCallback(result);
                    }
                };
                $.ajax(config);
            },
            // 保存结果弹出msg刷新table表格
            ajaxSuccess: function (result) {
                if (result.code == web_status.SUCCESS && $.table._option.type == table_type.bootstrapTable) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                } else if (result.code == web_status.SUCCESS && $.table._option.type == table_type.bootstrapTreeTable) {
                    $.modal.msgSuccess(result.msg);
                    $.treeTable.refresh();
                } else if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.msg);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功结果提示msg（父窗体全局更新）
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功回调执行事件（父窗体静默更新）
            successCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var parent = window.parent;
                    if (parent.$.table._option.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.table.refresh();
                    } else if (parent.$.table._option.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.treeTable.refresh();
                    } else {
                        $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
                $.modal.enable();
            },
            // 选项卡成功回调执行事件（父窗体静默更新）
            successTabCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var topWindow = $(window.parent.document);
                    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
                    var $contentWindow = $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
                    $.modal.close();
                    $contentWindow.$.modal.msgSuccess(result.msg);
                    $contentWindow.$(".layui-layer-padding").removeAttr("style");
                    if ($contentWindow.$.table._option.type == table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh();
                    } else if ($contentWindow.$.table._option.type == table_type.bootstrapTreeTable) {
                        $contentWindow.$.treeTable.refresh();
                    }
                    $.modal.closeTab();
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            }
        },
        // 校验封装处理
        validate: {
            // 判断返回标识是否唯一 false 不存在 true 存在
            unique: function (value) {
                if (value == "0") {
                    return true;
                }
                return false;
            },
            // 表单验证
            form: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().form();
            },
            //表单必填项验证(input,select,textarea)
            formRequired: function (formId) {
                var isValidated = true;
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId + " input[required],select[required],textarea[required]").each(function (index, item) {
                    var this_value = $.common.trim($(item).prop("value"));
                    if ($(item).is(":visible")) {
                        if ($.common.isEmpty(this_value)) {
                            if (!$(item).hasClass("error")) {
                                $(item).addClass("error");
                            }
                            var tipsMsg = $(this).data("tips");
                            if ($.common.isEmpty(tipsMsg)) {
                                tipsMsg = "此为必填项";
                            }
                            $.modal.alertWarning(tipsMsg);
                            var windowHeight = $(window).height();
                            var scrollTopHeight = $(document).scrollTop();
                            var this_offsetTop = $(item).offset().top;
                            var this_height = $(item).outerHeight(true);
                            if (scrollTopHeight > 0) {
                                if ((windowHeight - this_height)/2 >= this_offsetTop) {
                                    $(document).scrollTop(0);
                                }else {
                                    $(document).scrollTop(this_offsetTop - ((windowHeight - this_height)/2));
                                }
                            }
                            isValidated = false;
                            return false;
                        }
                    }
                });
                return isValidated;
            }
        },
        // 树插件封装处理
        tree: {
            _option: {},
            _lastValue: {},
            _expandLevel: {},
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
                $.ajax({
                    type: 'post',
                    url: options.url,
                    data: {selValues: option.selValues, selPValues: option.selPValues, checkType: options.checkType},
                    dataType: "json",
                    beforeSend: function () {
                        $.modal.loading("正在加载中...");
                    },
                    success: function (result) {
                        $.modal.closeLoading();
                        if (result.code == web_status.FAIL || result.code == web_status.WARNING) {
                            $.modal.msgWarning(result.msg);
                            return false;
                        }
                        if (result.length == 0) {
                            $.modal.msgWarning("无数据！");
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
                        } else if (options.checkType == "checkbox") {  //多选
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
                    }
                });
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
                        if (result.code == web_status.FAIL || result.code == web_status.WARNING) {
                            $.modal.msgWarning(result.msg);
                            return false;
                        }
                        if (result.length == 0) {
                            $.modal.msgWarning("无数据！");
                            return false;
                        }
                        //初始化树形
                        tree = $.fn.zTree.init($("#" + options.id), setting, result);
                        $._tree = tree;
                        $._tree.expandAll(true);
                    }
                });
            },
            // 搜索节点
            searchNode: function () {
                // 取得输入的关键字的值
                var value = $.common.trim($("#keyword").val());
                if ($.tree._lastValue == value) {
                    return;
                }
                // 保存最后一次搜索名称
                $.tree._lastValue = value;
                var nodes = $._tree.getNodes();
                // 如果要查空字串，就退出不查了。
                if (value == "") {
                    // $.tree.showAllNode(nodes);
                    var nodes = $._tree.getNodesByParam("isHidden", true);
                    $._tree.showNodes(nodes);
                    // $.tree.updateNodes(nodes);
                    return;
                }
                $.tree.hideAllNode(nodes);
                // 根据搜索值模糊匹配
                $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
            },
            // 根据Id和Name选中指定节点
            selectByIdName: function (treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.selectNode(node, true);
                }
            },
            // 根据Id和Name勾选指定节点
            checkByIdName: function (treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.checkNode(node, true, true);
                }
            },
            // 显示所有节点
            showAllNode: function (nodes) {
                nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._tree.expandNode(nodes[i], true, false, false, false);
                    } else {
                        $._tree.expandNode(nodes[i], true, true, false, false);
                    }
                    $._tree.showNode(nodes[i]);
                    $.tree.showAllNode(nodes[i].children);
                }
            },
            // 隐藏所有节点
            hideAllNode: function (nodes) {
                var tree = $.fn.zTree.getZTreeObj("tree");
                var nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._tree.hideNode(nodes[i]);
                }
            },
            // 显示所有父节点
            showParent: function (treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._tree.showNode(parentNode);
                    $._tree.expandNode(parentNode, true, false, false);
                    treeNode = parentNode;
                }
            },
            // 显示所有孩子节点
            showChildren: function (treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._tree.showNode(node);
                        $.tree.showChildren(node);
                    }
                }
            },
            // 更新节点状态
            updateNodes: function (nodeList) {
                $._tree.showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.tree.showChildren(treeNode);
                    $.tree.showParent(treeNode)
                }
            },
            // 获取当前被勾选集合
            getCheckedNodes: function (column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 不允许根父节点选择
            notAllowParents: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                if (nodes.length == 0) {
                    $.modal.msgError("请选择节点后提交");
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                        return false;
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 不允许最后层级节点选择
            notAllowLastLevel: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 隐藏/显示搜索栏
            toggleSearch: function () {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus();
            },
            // 折叠
            collapse: function () {
                $._tree.expandAll(false);
            },
            // 展开
            expand: function () {
                $._tree.expandAll(true);
            }
        },
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "" || this.trim(value) == "null") {
                    return true;
                }
                return false;
            },
            // 判断一个字符串是否为非空串
            isNotEmpty: function (value) {
                return !$.common.isEmpty(value);
            },
            // 空对象转字符串
            nullToStr: function (value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 比较两个字符串（大小写敏感）
            equals: function (str, that) {
                return str == that;
            },
            // 比较两个字符串（大小写不敏感）
            equalsIgnoreCase: function (str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase();
            },
            // 将字符串按指定字符分割
            split: function (str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null;
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg;
                });
                return flag ? str : '';
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            // 判断字符串是否是以start开头
            startWith: function (value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            // 判断字符串是否是以end结尾
            endWith: function (value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            // 数组去重
            uniqueFn: function (array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            // 数组中的所有元素放入一个字符串
            join: function (array, separator) {
                if ($.common.isEmpty(array)) {
                    return null;
                }
                return array.join(separator);
            },
            // 获取form下所有的字段并转换为json对象
            formToJSON: function (formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function (i, field) {
                    json[field.name] = field.value;
                });
                return json;
            },
            //预览iframe(页面或PDF)
            previewIframe: function (name, path) {
                var windowW = $(window.top).width(); //获取当前窗口宽度
                var windowH = $(window.top).height();//获取当前窗口高度
                var area = [windowW * 0.9 + "px", windowH * 0.9 + "px"];
                window.top.layer.open({
                    type: 2,
                    title: name,
                    area: area,
                    content: path,
                    maxmin: true,
                    isOutAnim: true,
                    scrollbar: false,
                    skin: 'layui-layer-gray'
                });
            },
            //放大显示图片
            imgShow: function (imgPath) {
                //获取当前窗口宽度
                var windowW = $(window).width();
                //获取当前窗口高度
                var windowH = $(window).height();
                //缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放
                var scale = 0.9;
                //图片显示宽度
                var imgWidth;
                //图片显示高度
                var imgHeight;
                $("<img/>").attr("src", imgPath).load(function () {
                    //图片的真实的宽度
                    var realWidth = this.width;
                    //图片的真实的高度
                    var realHeight = this.height;
                    if (realHeight > windowH * scale) {                  //判断图片高度
                        imgHeight = windowH * scale;                    //如大于窗口高度，图片高度进行缩放
                        imgWidth = imgHeight / realHeight * realWidth;  //等比例缩放宽度
                        if (imgWidth > windowW * scale) {                //如宽度扔大于窗口宽度
                            imgWidth = windowW * scale;                 //再对宽度进行缩放
                        }
                    } else if (realWidth > windowW * scale) {            //如图片高度合适，判断图片宽度
                        imgWidth = windowW * scale;                     //如大于窗口宽度，图片宽度进行缩放
                        imgHeight = imgWidth / realWidth * realHeight;  //等比例缩放高度
                    } else {                                            //如果图片真实高度和宽度都符合要求，高宽不变
                        imgWidth = realWidth;
                        imgHeight = realHeight;
                    }
                    //弹出图片
                    var area = [imgWidth + "px", imgHeight + "px"];
                    var img = '<img src="' + imgPath + '" alt="" width="' + imgWidth + '" height="' + imgHeight + '">';
                    layer.open({
                        type: 1,
                        area: area,
                        title: false, //不现实标题
                        shade: 0.6,  //遮罩透明度
                        anim: 0,     //0-6的动画形式，-1不开启
                        closeBtn: 2,
                        shadeClose: true, //点击遮罩层关闭
                        resize: false,    //不允许拉伸
                        scrollbar: false, //不出现滚动条
                        content: img
                    });
                });
            }
        },
        //文件操作封装处理
        file: {
            //文件下载
            downloadFile: function (id) {
                if (id == 'false') {
                    $.modal.msgError("本地文件不支持下载！");
                } else {
                    window.location.href = ctx + "common/downloadAnnex?id=" + encodeURI(id);
                }
            },
            //多文件上传初始化控件
            initFileInput: function (params) {
                var fiels = [];
                var obj = params.obj;
                var tools = params.tools;
                //下载按钮
                var download_btn = '<button type="button" class="kv-file-download btn btn-sm btn-kv btn-default btn-outline-secondary" onclick="$.file.downloadFile(\'{key}\');" title="下载文件" data-key="{key}">' +
                    '<i class="glyphicon glyphicon-download"></i>' +
                    '</button>';
                var param = {
                    language: 'zh',
                    uploadUrl: '/file/doUpload',
                    deleteUrl: '/file/doDelete',
                    allowedFileExtensions: ['jpg', 'jpeg', 'gif', 'png', "doc", "docx", "xls", "xlsx", "pdf"],
                    autoUpload: true,//自定义参数，是否自动上传
                    readFileTableId: '',//读取文件显示table容器id
                    readFileContent: false,//获取上传文件的内容（只对excel文件有用）
                    readFileSuccess: '',//文件内容读取成功回调函数
                    isCheckRepeat: false,//是否需要检查重复数据
                    checkRepeatUrl: '',//检查重复数据地址
                    readFileUrl: '',//读取文件内容地址
                    readFileColumns: [],//读取文件内容列
                    showFooter: false,
                    showCaption: true,//是否显示被选文件的简介
                    showBrowse: true,//是否显示浏览按钮
                    showPreview: true,//是否显示预览区域
                    showRemove: false,//是否显示移除按钮
                    showUpload: false,//是否显示上传按钮
                    showCancel: true,//是否显示取消按钮
                    showClose: false,//是否显示关闭按钮
                    showUploadedThumbs: true,//这个属性是基于这样一个使用方法的：选择若干个文件后点击右下角上传按钮批量上传，待全部完成后再选择一批文件，此时之前上传成功的文件是否要保存。就是这个属性控制的。注意，点击文件缩略图下面的上传按钮不会导致之前的成功上传的文件消失，即使这里设置了true
                    autoReplace: false,//是否自动替换当前图片，设置为true时，再次选择文件， 会将当前的文件替换掉。
                    dropZoneEnabled: true,//是否显示拖拽区域,
                    overwriteInitial: false,//是否要覆盖初始预览内容和标题设置
                    otherActionButtons: download_btn,
                    maxFileSize: 500000//文件大小限制
                };
                if (tools != undefined) {
                    $.extend(param, tools);
                }
                if (param.defaultPreview != undefined && param.defaultPreview != "") {
                    var initialPreview = [];
                    var initialPreviewConfig = [];
                    var defaultPreview = param.defaultPreview;
                    if ($.isArray(defaultPreview)) {
                        $.each(defaultPreview, function (index, item) {
                            fiels.push(item.id);
                            if (item.type == "image") {
                                initialPreview.push('<img src="' + item.path + '" class="kv-preview-data file-preview-image">');
                            } else {
                                initialPreview.push('<div class="file-preview-other"><span class="file-other-icon"><i class="glyphicon glyphicon-file"></i></span></div>');
                            }
                            initialPreviewConfig.push({
                                type: item.type,
                                caption: item.name,
                                size: item.size,
                                key: item.id,
                                extra: {id: item.id}
                            });
                        });
                    } else {
                        fiels.push(defaultPreview.id);
                        if (defaultPreview.type == "image") {
                            initialPreview.push('<img src="' + defaultPreview.path + '" class="kv-preview-data file-preview-image">');
                        } else {
                            initialPreview.push('<div class="file-preview-other"><span class="file-other-icon"><i class="glyphicon glyphicon-file"></i></span></div>');
                        }
                        initialPreviewConfig.push({
                            type: defaultPreview.type,
                            caption: defaultPreview.name,
                            size: defaultPreview.size,
                            key: defaultPreview.id,
                            extra: {id: defaultPreview.id}
                        });
                    }
                    param["initialPreview"] = initialPreview;
                    param["initialPreviewConfig"] = initialPreviewConfig;
                }
                var accept = obj.attr("accept");
                if (accept != undefined && accept != "") {
                    var accepts = accept.split(",");
                    var allowedFileExtensions = [];
                    $.each(accepts, function (index, item) {
                        allowedFileExtensions.push(item.replace(".", ""));
                    });
                    param["allowedFileExtensions"] = allowedFileExtensions;
                }
                var multiple = obj.attr("multiple");
                //单文件
                if (multiple == undefined) {
                    //布尔值，是否在预览窗口中持续显示已经上传的文件缩略图（用于ajax上传），直到按下删除/清除按钮。默认值为true。当设置为false时，下一批选择上传的文件将从预览中清除这些已经上传的文件缩略图。
                    param["showUploadedThumbs"] = false;
                    if (param.readFileContent && param.initialPreviewConfig != undefined && param.initialPreviewConfig.length == 1) {
                        initFileTable(param.initialPreviewConfig[0].extra.id);
                    }
                }
                obj.fileinput(param).on('fileloaded', function (event, file, previewId, index, reader) {
                    //预览文件加载完成后给删除按钮绑定事件
                    $(".kv-preview-thumb[id='" + previewId + "'] .kv-file-remove").bind("click", function () {
                        var id = $("#" + previewId).data("fileid");
                        fiels.splice($.inArray(id, fiels), 1);
                    });
                }).on('filedeleted', function (event, key, data) {
                    //删除初始化文件
                    var result = data.responseJSON;
                    if (result.code == 0) {
                        var id = result.data;
                        fiels.splice($.inArray(id, fiels), 1);
                    }
                }).on("fileuploaded", function (e, data, previewId, index) {
                    //文件上传成功的回调函数，还有其他的一些回调函数，比如上传之前...
                    var result = data.response;
                    if (result.code == 0) {
                        var id = result.data;
                        $(".file-preview-success[id='" + previewId + "']").data("fileid", id);
                        if (multiple == undefined) {
                            fiels.splice(0);
                        }
                        fiels.push(id);
                        //读取上传文件内容
                        if (param.readFileContent) {
                            if (param.isCheckRepeat && param.checkRepeatUrl != '') {
                                var getData = {id: id};
                                if (param.extraData != "") {
                                    $.extend(getData, param.extraData);
                                }
                                $.ajax({
                                    url: param.checkRepeatUrl,
                                    type: "get",
                                    data: getData,
                                    datatype: "json",
                                    beforeSend: function () {
                                        $.modal.loading("正在检查数据中，请稍后...");
                                    },
                                    success: function (result) {
                                        if (result.code == 0) {
                                            initFileTable(id);
                                            if (param.checkCallBack != undefined) {
                                                param.checkCallBack(result);
                                            }
                                        } else {
                                            $.modal.alertError(result.msg);
                                            $.modal.closeLoading();
                                        }
                                    }
                                });
                            } else {
                                initFileTable(id);
                            }
                        }
                    } else {
                        console.log(result);
                    }
                })/*.on('filesuccessremove', function(event, data) {
					//删除上传文件
					var id= $(".file-preview-success[id='"+data+"']").data("fileid");
					fiels.splice($.inArray(id,fiels),1);
					/!*$.ajax({
						url:param.deleteUrl,
						type:"post",
						data:{id:id},
						success:function(result){
							if(result.code==0){
								var id=result.data;
								fiels.splice($.inArray(id,fiels),1);
							}
						}
					});*!/
				})*/;
                if (param.autoUpload) {
                    obj.on("filebatchselected", function (event, files) {
                        $(this).fileinput("upload");
                    });
                }

                function initFileTable(id) {
                    var options = {
                        id: param.readFileTableId,
                        url: param.readFileUrl,
                        showSearch: false,
                        showRefresh: false,
                        showToggle: false,
                        showColumns: false,
                        pagination: false,
                        showFooter: param.showFooter,
                        queryParams: function (params) {
                            return {
                                id: id
                            };
                        },
                        columns: param.readFileColumns,
                        onPreBody: function (data) {
                            if (data.length == 0) {
                                $.modal.loading("正在加载数据中，请稍后...");
                            }
                        },
                        onLoadSuccess: function (data) {
                            if (typeof param.readFileSuccess == 'function') {
                                param.readFileSuccess(data);
                            }
                            if (param.fileOnLoadSuccess != undefined) {
                                param.fileOnLoadSuccess(data);
                            } else {
                                //分页加载
                                $.file.goPage(1, param.readFileTableId);
                                $(window).resize(function () {
                                    $.file.goPage(1, param.readFileTableId);
                                });
                            }
                            $.modal.closeLoading();
                        }
                    };
                    //销毁原来的table列表
                    $("#" + param.readFileTableId).bootstrapTable('destroy');
                    $.table.init(options);
                }

                return fiels;
            },
            //单文件上传初始化控件
            initUpload: function (params) {
                var tools = {
                    container: "",
                    type: "input",
                    file_name: "input_file",
                    accept: ".jpg,.jpeg,.gif,.png,.doc,.docx,.xls,.xlsx,.pdf",
                    width: 120,
                    height: 120,
                    value: "",
                    callback: ""
                };
                $.extend(tools, params);
                var inputType = tools.type;
                var html = '';
                var name = "";
                var id = "";
                var className = "";
                if (tools.value != null && tools.value != "") {
                    name = tools.value.name;
                    id = tools.value.id;
                    className = "fileinput-exists";
                }
                if (inputType == "input") {
                    html = '<div class="fileinput input-group ' + (className == "" ? "fileinput-new" : className) + '" data-provides="fileinput">\n' +
                        '       <div class="form-control">\n' +
                        '           <i class="glyphicon glyphicon-file fileinput-exists"></i>\n' +
                        '           <span class="fileinput-filename">' + (name != "" ? '<a href="javascript:void(0);" title="下载文件" onclick="$.file.downloadFile(\'' + id + '\')">' + name + '</a>' : '') + '</span>\n' +
                        '       </div>\n' +
                        '       <span class="input-group-addon btn btn-white btn-file">\n' +
                        '           <span class="fileinput-new">选择文件</span>\n' +
                        '           <span class="fileinput-exists">更改</span>\n' +
                        '           <input type="hidden" name="' + tools.file_name + '" value="' + id + '">' +
                        '           <input type="file" id="' + tools.file_name + '" accept="' + tools.accept + '">\n' +
                        '       </span>\n' +
                        '       <a href="#" class="input-group-addon btn btn-white fileinput-exists clearfile" data-dismiss="fileinput">清除</a>\n' +
                        '</div>';
                } else if (inputType == "button") {
                    html = '<div class="fileinput ' + (className == "" ? "fileinput-new" : className) + '" data-provides="fileinput">\n' +
                        '    <span class="btn btn-white btn-file">\n' +
                        '        <span class="fileinput-new">选择文件</span>\n' +
                        '        <span class="fileinput-exists">更改</span>\n' +
                        '        <input type="hidden" name="' + tools.file_name + '" value="' + id + '">' +
                        '        <input type="file" id="' + tools.file_name + '" accept="' + tools.accept + '">\n' +
                        '    </span>\n' +
                        '    <span class="fileinput-filename">' + (name != "" ? '<a href="javascript:void(0);" title="下载文件" onclick="$.file.downloadFile(\'' + id + '\')">' + name + '</a>' : '') + '</span>\n' +
                        '    <a href="#" class="close fileinput-exists clearfile" data-dismiss="fileinput" style="float: none">&times;</a>\n' +
                        '</div>';
                } else if (inputType == "preview") {
                    var img = "";
                    if (tools.value != null && tools.value != "") {
                        img = '<img src="' + tools.value.path + '">';
                    }
                    html = '<div class="fileinput ' + (className == "" ? "fileinput-new" : className) + '" data-provides="fileinput">\n' +
                        '    <div class="fileinput-preview thumbnail" data-trigger="fileinput" style="width: ' + tools.width + 'px; height: ' + tools.height + 'px;">' + img + '</div>\n' +
                        '    <div>\n' +
                        '        <span class="btn btn-white btn-file">\n' +
                        '            <span class="fileinput-new">选择图片</span>\n' +
                        '            <span class="fileinput-exists">更改</span>\n' +
                        '            <input type="hidden" name="' + tools.file_name + '" value="' + id + '">' +
                        '            <input type="file" id="' + tools.file_name + '" accept=".jpg,.jpeg,.gif,.png">\n' +
                        '        </span>\n' +
                        '        <a href="#" class="btn btn-white fileinput-exists clearfile" data-dismiss="fileinput">清除</a>\n' +
                        '    </div>\n' +
                        '</div>';
                }
                var objHTML = $(html);
                objHTML.find("#" + tools.file_name).bind("change", function () {
                    var file = $(this)[0].files[0];
                    var formData = new FormData();
                    formData.append('uploadfile', file);
                    var id = $("input[type=hidden][name=" + tools.file_name + "]").val();
                    if (id != "") {
                        $.ajax({
                            async: false,
                            url: "/file/doDelete",
                            type: "post",
                            data: {id: id},
                            success: function (result) {
                                if (result.code == 0) {
                                    var id = result.data;
                                    $("input[type=hidden][name=" + tools.file_name + "]").val("");
                                }
                            }
                        });
                    }
                    $.ajax({
                        type: "post",
                        url: "/file/doUpload",
                        data: formData,
                        dataType: "json",
                        mimeType: "multipart/form-data",
                        async: false,
                        cache: false, //上传文件不需要缓存
                        contentType: false, //需设置为false。因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
                        processData: false, //需设置为false。因为data值是FormData对象，不需要对数据做处理
                        success: function (result) {
                            if (result.code == 0) {
                                var id = result.data;
                                $("input[type=hidden][name=" + tools.file_name + "]").val(id);
                                if (typeof tools.callback == "function") {
                                    tools.callback(id);
                                }
                            }
                        }
                    });
                });
                objHTML.find(".clearfile").bind("click", function () {
                    var id = $("input[type=hidden][name=" + tools.file_name + "]").val();
                    if (id != "") {
                        $.ajax({
                            url: "/file/doDelete",
                            type: "post",
                            data: {id: id},
                            success: function (result) {
                                if (result.code == 0) {
                                    var id = result.data;
                                    $("input[type=hidden][name=" + tools.file_name + "]").val("");
                                }
                            }
                        });
                    }
                });
                $(tools.container).append(objHTML);
            },
            //summernote图片上传方法
            summerNoteImgSendFile: function (file, obj) {
                var data = new FormData();
                data.append("file", file);
                $.ajax({
                    type: "POST",
                    url: ctx + "common/upload",
                    data: data,
                    cache: false,
                    contentType: false,
                    processData: false,
                    dataType: 'json',
                    success: function (result) {
                        if (result.code == web_status.SUCCESS) {
                            $(obj).summernote('editor.insertImage', result.url, result.fileName);
                        } else {
                            $.modal.alertError(result.msg);
                        }
                    },
                    error: function (error) {
                        $.modal.alertWarning("图片上传失败。");
                    }
                });
            },
            //js分页
            goPage: function (pno, tableId) {
                var iTable = document.getElementById(tableId);
                var num = iTable.rows.length - 1;//表格所有行数(所有记录数)
                var totalPage = 0;//总页数
                var pageSize = 10;//每页显示行数
                //总共分几页
                if (num / pageSize > parseInt(num / pageSize)) {
                    totalPage = parseInt(num / pageSize) + 1;
                } else {
                    totalPage = parseInt(num / pageSize);
                }
                var currentPage = pno;//当前页数
                var startRow = (currentPage - 1) * pageSize + 1;  //开始显示的行
                var endRow = currentPage * pageSize;  //结束显示的行
                endRow = (endRow > num) ? num : endRow;
                //遍历显示数据实现分页
                for (var i = 1; i < (num + 1); i++) {
                    var irow = iTable.rows[i];
                    if (i >= startRow && i <= endRow) {
                        irow.style.display = "table-row";
                    } else {
                        irow.style.display = "none";
                    }
                }
                var tempStr = "";
                if (totalPage > 1) {
                    tempStr = "<span class='info'>共" + totalPage + "页</span>";

                    if (currentPage > 1) {
                        tempStr += "<a href=\"###\" onClick=\"$.file.goPage(" + (1) + ",\'" + tableId + "'\)\"><span>首页</span></a>";
                        tempStr += "<a href=\"###\" onClick=\"$.file.goPage(" + (currentPage - 1) + ",\'" + tableId + "'\)\"><span>上一页</span></a>"
                    } else {
                        tempStr += "<a><span>首页</span></a>";
                        tempStr += "<a><span>上一页</span></a>";
                    }

                    //判断当前页码
                    if (totalPage > 5) {
                        if (currentPage < 4) {
                            tempStr += "<a href=\"###\" onclick=\"$.file.goPage(" + 1 + ",\'" + tableId + "'\)\"><span>" + 1 + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + 2 + ",\'" + tableId + "'\)\"><span>" + 2 + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + 3 + ",\'" + tableId + "'\)\"><span>" + 3 + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + 4 + ",\'" + tableId + "'\)\"><span>" + 4 + "</span></a>" +
                                "<a><span>...</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + totalPage + ",\'" + tableId + "'\)\"><span>" + totalPage + "</span></a>";
                        } else if (currentPage > totalPage - 3) {
                            tempStr += "<a href=\"###\" onclick=\"$.file.goPage(" + (1) + ",\'" + tableId + "'\)\"><span>" + 1 + "</span></a>" +
                                "<a><span>...</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + (totalPage - 3) + ",\'" + tableId + "'\)\"><span>" + (totalPage - 3) + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + (totalPage - 2) + ",\'" + tableId + "'\)\"><span>" + (totalPage - 2) + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + (totalPage - 1) + ",\'" + tableId + "'\)\"><span>" + (totalPage - 1) + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + totalPage + ",\'" + tableId + "'\)\"><span>" + totalPage + "</span></a>";
                        } else {
                            tempStr += "<a href=\"###\" onclick=\"$.file.goPage(" + 1 + ",\'" + tableId + "'\)\"><span>" + 1 + "</span></a>" +
                                "<a><span>...</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + (currentPage - 1) + ",\'" + tableId + "'\)\"><span>" + (currentPage - 1) + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + currentPage + ",\'" + tableId + "'\)\"><span>" + currentPage + "</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + (currentPage + 1) + ",\'" + tableId + "'\)\"><span>" + (currentPage + 1) + "</span></a>" +
                                "<a><span>...</span></a>" +
                                "<a href=\"###\" onclick=\"$.file.goPage(" + totalPage + ",\'" + tableId + "'\)\"><span>" + totalPage + "</span></a>";
                        }
                    } else {
                        for (var pageIndex = 1; pageIndex < totalPage + 1; pageIndex++) {
                            tempStr += "<a href=\"###\" onclick=\"$.file.goPage(" + pageIndex + ",\'" + tableId + "'\)\"><span>" + pageIndex + "</span></a>";
                        }
                    }

                    if (currentPage < totalPage) {
                        tempStr += "<a href=\"###\" onClick=\"$.file.goPage(" + (currentPage + 1) + ",\'" + tableId + "'\)\"><span>下一页</span></a>";
                        tempStr += "<a href=\"###\" onClick=\"$.file.goPage(" + (totalPage) + ",\'" + tableId + "'\)\"><span>尾页</span></a>";
                    } else {
                        tempStr += "<a><span>下一页</span></a>";
                        tempStr += "<a><span>尾页</span></a>";
                    }
                }
                var $html = $(tempStr);
                $.each($html.find("span"), function (index, item) {
                    if ($(item).text() == currentPage) {
                        $(item).parent("a").addClass("active").siblings().removeClass("active");
                    }
                });
                $("#barCon").html($html);
            }
        },
        //地区省市区选择-三级联动
        district: {
            getCities: function (obj, selName) {
                var districtPrefix = ctx + "district";
                if ($(obj).val() == '') {//如果为空 则清空后面的数据
                    if ('city_code' == selName) {
                        $("#select_" + selName).html('');
                        $("#select_" + selName).append("<option value=''>--选择城市--</option>");
                        $("#select_" + selName).val('');
                    }
                    $("#select_area_code").html('');
                    $("#select_area_code").append("<option value=''>--选择区县--</option>");
                    $("#select_area_code").val('');
                } else {//否则调后台查询二级 三级 市区数据
                    $.get(districtPrefix + "/get", {pid: $(obj).val()}, function (data) {
                        if (data.code == 0) {
                            $("#select_" + selName).html('');
                            $("#select_" + selName).append("<option value=''>--选择城市--</option>");
                            $("#select_" + selName).val('');
                            $.each(data.data, function (i, v) {
                                $("#select_" + selName).append("<option value='" + v.id + "'>" + v.name + "</option>");
                            });
                            if (selName == 'city_code') {
                                $("#select_area_code").html('');
                                $("#select_area_code").append("<option value=''>--选择区县--</option>");
                                $("#select_area_code").val('');
                            }
                        }
                    }, 'json');
                }
            }
        },
        //日期时间处理
        dateUtil: {
            //当前时间
            getCurDateTime: function (fmt) {
                var myDate = new Date();//获取系统当前时间
                var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
                var month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
                month = month < 10 ? "0" + month : month;
                var day = myDate.getDate(); //获取当前日(1-31)
                day = day < 10 ? "0" + day : day;
                var hours = myDate.getHours(); //获取当前小时数(0-23)
                hours = hours < 10 ? "0" + hours : hours;
                var minutes = myDate.getMinutes(); //获取当前分钟数(0-59)
                minutes = minutes < 10 ? "0" + minutes : minutes;
                var seconds = myDate.getSeconds(); //获取当前秒数(0-59)
                seconds = seconds < 10 ? "0" + seconds : seconds;
                var dateStr = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
                var date = $.dateUtil.convertDate(dateStr);
                return date.pattern(fmt);
            },
            convertDate: function (dateStr) {
                return new Date(Date.parse(dateStr.replace(/-/g, "/")));
            },
            /**
             * @return {string}
             */
            MillisecondToDayHourMinute: function (datetime) {
                var time = "";
                var days = datetime / 1000 / 60 / 60 / 24;
                var daysRound = Math.floor(days);
                var hour_count = daysRound * 24;
                var hours = datetime / 1000 / 60 / 60 - (24 * daysRound);
                var hoursRound = Math.floor(hours);
                hour_count += hoursRound;
                if (hour_count >= 1) {
                    time += hour_count >= 10 ? hour_count + '时' : '0' + hour_count + '时';
                } else {
                    time += '00时';
                }
                var minutes = datetime / 1000 / 60 - (24 * 60 * daysRound) - (60 * hoursRound);
                var minutesRound = Math.floor(minutes);
                if (minutesRound >= 1) {
                    time += minutesRound >= 10 ? minutesRound + '分' : '0' + minutesRound + '分';
                } else {
                    time += '00分';
                }
                var seconds = datetime / 1000 - (24 * 60 * 60 * daysRound) - (60 * 60 * hoursRound) - (60 * minutesRound);
                var secondsRound = Math.floor(seconds);
                if (secondsRound >= 1) {
                    time += secondsRound >= 10 ? secondsRound + '秒' : '0' + secondsRound + '秒';
                } else {
                    time += '00秒';
                }
                return time;
            },
            minusSecond: function (datetime1, datetime2) {
                var time1 = new Date(datetime1).getTime();
                var time2 = new Date(datetime2).getTime();
                return time1 - time2;
            }
        },
        //运算
        math: {
            //除法运算，避免数据相除小数点后产生多位数和计算精度损失。
            numDiv: function (num1, num2) {
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
        }
    });
})(jQuery);

/** 表格类型 */
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};

/** 消息状态码 */
web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301
};

/** 弹窗状态码 */
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};
Date.prototype.pattern = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};