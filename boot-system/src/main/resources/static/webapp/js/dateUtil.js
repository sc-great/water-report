//获取系统当前时间(fmt指定时间格式)
function getCurDate(fmt) {
    var myDate = new Date();//获取系统当前时间
    myDate.getYear(); //获取当前年份(2位)
    var year = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
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
    var date = convertDate(dateStr);
    return date.pattern(fmt);
}
//格式化时间显示(格林尼治时间-->北京时间)
function parseDateTimeTostring(datetime) {
    if (datetime != undefined){
        var now = new Date(datetime);
        var year = now.getFullYear();
        var mon = now.getMonth() + 1;
        var date = now.getDate();
        var hours = now.getHours();
        var minutes = now.getMinutes();
        var seconds = now.getSeconds();
        if (mon < 10) {
            mon = '0' + mon;
        }
        if (date < 10) {
            date = '0' + date;
        }
        if (hours < 10) {
            hours = '0' + hours;
        }
        if (minutes < 10) {
            minutes = '0' + minutes;
        }
        if (seconds < 10) {
            seconds = '0' + seconds;
        }
        var postDate = year + '-' + mon + '-' + date + " " + hours + ":" + minutes + ":" + seconds;
        return postDate;
    }else {
        return "-";
    }
}
//格式化时间显示(格林尼治时间-->北京时间)
function parseDateTimeTostringFMT(datetime, fmt) {
    var now = new Date(datetime);
    var year = now.getFullYear();
    var mon = now.getMonth() + 1;
    var date = now.getDate();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds();
    if (mon < 10) {
        mon = '0' + mon;
    }
    if (date < 10) {
        date = '0' + date;
    }
    if (hours < 10) {
        hours = '0' + hours;
    }
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    if (seconds < 10) {
        seconds = '0' + seconds;
    }
    var postDate = year + '-' + mon + '-' + date + " " + hours + ":" + minutes + ":" + seconds;
    var dateTime = convertDate(postDate);
    return dateTime.pattern(fmt);
}
//字符串转换成时间类型
function convertDate(dateStr) {
    return new Date(Date.parse(dateStr.replace(/-/g, "/")));
}
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
}


function formatSecond(s,fmt) {
    var timeStr="";
    var hours=0;
    var minuts=0;
    var seconds=0;
    switch (fmt) {
        case "h时m分s秒":
            hours=parseInt(s/3600);
            minuts=parseInt((s%3600)/60);
            seconds=((s%3600)%60);
            break;
        case "m分s秒":
            minuts=parseInt(s/60);
            seconds=s%60;
            break;
    }
    if(hours>0){
        timeStr+=hours+"时";
    }
    if(minuts>0){
        timeStr+=minuts+"分";
    }
    if(seconds>0){
        timeStr+=seconds+"秒";
    }
    return timeStr;
}

var calendarInit=function (params){
    var calendar=new Object();
    var tools={
        objId:"",//需要加载日历的容器id
        checkStr:[],
        defaultDateStr:getCurDate("yyyy-MM-dd"),//默认显示日历月份
        txtData:"",//需要在日历上标记的数据格式[{date:'2018-11-20',topTips:'日期上方文本',text:'日期文本',bottomTips:'日期下方文本'}]
        minDate:"",//日历最小日期
        maxDate:"",//日历最大日期
        disabled:[],//禁用日期格式：数组
        checkMore:true,//是否允许多选
        isCheckWeek:false,//工作日
        checkWeeks:[],//禁用星期：数组
        checkCallBack:"",//选择回调函数
    };
    if(params!=undefined){
        $.extend(tools,params);
    }
    removeCookie(tools.objId);
    var obj=$("#"+tools.objId);
    obj.empty();
    var year=tools.defaultDateStr.substring(0,4);
    var month=tools.defaultDateStr.substring(5,7);
    var curData=year+"-"+month;
    var preMonth=getPreMonth(curData+"-01");
    var nextMonth=getNextMonth(curData+"-01");
    var html='<div class="calendar">\n' +
        '                                <div class="calendar-header">\n' +
        '                                    <div class="fa fa-caret-left premonth" data-value="'+preMonth+'" title="上一个月"></div>\n' +
        '                                    <div class="datetext">'+curData+'</div>\n' +
        '                                    <div class="fa fa-caret-right nextmonth" data-value="'+nextMonth+'" title="下一个月"></div>\n' +
        '                                </div>\n' +
        '                                <div class="calendar-main">\n' +
        '                                    <div class="dateheader">\n' +
        '                                        <div class="week">日</div>\n' +
        '                                        <div class="week">一</div>\n' +
        '                                        <div class="week">二</div>\n' +
        '                                        <div class="week">三</div>\n' +
        '                                        <div class="week">四</div>\n' +
        '                                        <div class="week">五</div>\n' +
        '                                        <div class="week">六</div>\n' +
        '                                    </div>\n' +
        '                                    <div class="datelist">\n';
    html+=getDateHTML();
    html+='                                    </div>\n' +
        '                                </div>\n' +
        '                            </div>';
    var objHTML=$(html);
    objHTML.find(".premonth,.nextmonth").bind("click",function () {
        var curDate=$(this).attr("data-value");
        if(tools.minDate!=undefined && tools.minDate!=""){
            if(minusDateMonth(curDate,tools.minDate)<0){
                layer.open({
                    content: "最小日期："+tools.minDate
                    ,time: 2
                });
                return false;
            }
        }
        if(tools.maxDate!=undefined && tools.maxDate!=""){
            if(minusDateMonth(curDate,tools.maxDate)>0){
                layer.open({
                    content: "最大日期："+tools.maxDate
                    ,time: 2
                });
                return false;
            }
        }
        var params={objId:tools.objId,checkStr:tools.checkStr,defaultDateStr:curDate,txtData:tools.txtData,minDate:tools.minDate,maxDate:tools.maxDate,disabled:tools.disabled,checkMore:tools.checkMore,isCheckWeek:tools.isCheckWeek,checkWeeks:tools.checkWeeks,checkCallBack:tools.checkCallBack};
        var calendar=new calendarInit(params);
    });
    objHTML.find(".date:not(.nochecked)").bind("click",function () {
        var checkStr="";
        $(this).toggleClass("checked");
        var dateText=$(this).attr("data-value");
        if(tools.checkMore){
            var checkedDataStr=getCookie(tools.objId);
            var checkedData=[];
            if(checkedDataStr!=undefined && checkedDataStr!=""){
                checkedData=checkedDataStr.split(",");
            }
            if($(this).hasClass("checked")){
                checkedData.push(dateText);
            }else{
                var index = checkedData.indexOf(dateText);
                if (index > -1) {
                    checkedData.splice(index, 1);
                }
            }
            checkStr=checkedData.join(",");
        }else{
            $(this).siblings().removeClass("checked");
            if($(this).hasClass("checked")){
                checkStr=dateText;
            }
        }
        tools.checkStr=checkStr;
        setCookie(tools.objId, checkStr);
        if(tools.checkCallBack!=""){
            tools.checkCallBack(dateText);
        }
    });
    obj.html(objHTML);
    function getDateHTML(){
        var dateHTML='';
        var date= new Date(year,month,0);
        //当月天数
        var currMonthDayCount=parseInt(date.getDate());
        //当月第一天
        var monthFirstDay=new Date(year+"-"+month+"-01");
        //当月第一天星期
        var week=0;
        week=parseInt(monthFirstDay.getDay());
        var style=week==0?'':'style="margin-left:calc(100% / 7 * '+week+')"';
        //获取选中的日期
        var checkedData=tools.checkStr;

        for(var i=1;i<=currMonthDayCount;i++){
            var checked="";
            var dateValue=year+"-"+month+"-"+(i<10?"0"+i:i);
            if(tools.checkMore){
                if(checkedData.length>0){
                    $.each(checkedData,function (index,item) {
                        if(item==dateValue){
                            checked="checked";
                            return false;
                        }
                    });
                    setCookie(tools.objId,checkedData.join(","));
                }
            }else{
                if(checkedData!=""){
                    if(checkedData==dateValue){
                        checked="checked";
                    }
                    setCookie(tools.objId,checkedData);
                }
            }


            var noChecked="";
            if(tools.isCheckWeek){
                noChecked="nochecked";
                $.each(tools.checkWeeks,function(w_index,w_week){
                    if(parseInt(convertDate(dateValue).getDay())==w_week){
                        noChecked="";
                        return false;
                    }
                });
            }
            if(tools.minDate!=undefined && tools.minDate!=""){
                if(minusDateDay(dateValue,tools.minDate)<0){
                    noChecked="nochecked";
                }
            }
            if(tools.maxDate!=undefined && tools.maxDate!=""){
                if(minusDateDay(dateValue,tools.maxDate)>=0){
                    noChecked="nochecked";
                }
            }
            if(tools.disabled.length>0){
                $.each(tools.disabled,function(d_index,d_date){
                    if(d_date==dateValue){
                        noChecked="nochecked";
                        return false;
                    }
                });
            }
            var text=i;
            var topTips="";
            var bottomTips="";
            if(tools.txtData!=undefined && tools.txtData!=""){
                $.each(tools.txtData,function (index,item) {
                    if(item.date==dateValue){
                        if(item.topTips!=undefined && item.topTips!=""){
                            topTips='<span class="toptips">'+item.topTips+'</span>';
                        }
                        if(item.text!=undefined && item.text!=""){
                            text=item.text;
                        }
                        if(item.bottomTips!=undefined && item.bottomTips!=""){
                            bottomTips='<span class="bottomtips">'+item.bottomTips+'</span>';
                        }
                        return false;
                    }
                });
            }
            if(dateValue==getCurDate("yyyy-MM-dd")){
                text="今天";
            }
            dateHTML+='<div class="date '+checked+' '+noChecked+'" '+(i==1?style:'')+' data-value="'+dateValue+'">\n' +topTips+
                '<span class="text">'+text+'</span>\n' +bottomTips+
                '</div>\n';
        }
        return dateHTML;
    }
    calendar.getCheckDate=function(){
        var checkDate;
        if(tools.checkMore){
            checkDate=[];
        }else{
            checkDate="";
        }
        var checkedDataStr=getCookie(tools.objId);
        if(checkedDataStr!=undefined && checkedDataStr!=""){
            if(tools.checkMore){
                checkDate=checkedDataStr.split(",");
            }else{
                checkDate=checkedDataStr;
            }
        }
        return checkDate;
    }
    return calendar;
}
//上个月
function getPreMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中月的天数
    var year2 = year;
    var month2 = parseInt(month) - 1;
    if (month2 == 0) {
        year2 = parseInt(year2) - 1;
        month2 = 12;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;
    }
    var t2 = year2 + '-' + month2 + '-' + day2;
    return t2;
}
//下个月
function getNextMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中的月的天数
    var year2 = year;
    var month2 = parseInt(month) + 1;
    if (month2 == 13) {
        year2 = parseInt(year2) + 1;
        month2 = 1;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;
    }

    var t2 = year2 + '-' + month2 + '-' + day2;
    return t2;
}
//计算两个月份差的函数，通用
function minusDateMonth(date1, date2) {
    //sDate1和sDate2是yyyy-MM-dd格式
    //月份差
    date1 = date1.split("-");
    date2 = date2.split("-");
    //获取年,月数
    var year1 = parseInt(date1[0]) ,
        month1 = parseInt(date1[1]) ,
        year2 = parseInt(date2[0]) ,
        month2 = parseInt(date2[1]) ,
        //通过年,月差计算月份差
        months = (year1 - year2) * 12 + (month1-month2);
    return months;
}
//计算2个日期相差天数
function minusDateDay(datetime1,datetime2){
    var tempTime = Date.parse(datetime1.replace(/-/g, "/"))- Date.parse(datetime2.replace(/-/g, "/"));
    var days = Math.floor(tempTime/(24*3600*1000));
    return days;
}
//计算2个日期相差
function minusDateHour(datetime1,datetime2){
    var tempTime = Date.parse(datetime1.replace(/-/g, "/"))- Date.parse(datetime2.replace(/-/g, "/"));
    var hours = Math.floor(tempTime/(3600*1000));
    return hours;
}
//计算两个日期相差秒
function minusSeconds(datetime1,datetime2) {
    var tempTime = Date.parse(datetime1.replace(/-/g, "/"))- Date.parse(datetime2.replace(/-/g, "/"));
    /* var days = Math.floor(tempTime/(24*3600*1000))
         ,tempSec1 = tempTime%(24*3600*1000)
         ,hours = Math.floor(tempSec1/(3600*1000))
         ,tempSec2 = tempSec1%(3600*1000)
         ,minutes = Math.floor(tempSec2/(60*1000))
         ,tempSec3 = tempSec2%(60*1000)
         ,seconds = Math.round(tempSec3/1000);*/
    var seconds = Math.round(tempTime/1000);
    return seconds;
}
//日期加天数
function addDateDay(date, num) {
    //日期加天数
    var translateDate = "", dateString = "", monthString = "", dayString = "";
    translateDate = date.replace("-", "/").replace("-", "/");
    var newDate = new Date(translateDate);
    newDate = newDate.valueOf();
    newDate = newDate + num * 24 * 60 * 60 * 1000;  //备注 如果是往前计算日期则为减号 否则为加号
    newDate = new Date(newDate);
    //如果月份长度少于2，则前加 0 补位
    if ((newDate.getMonth() + 1).toString().length == 1) {
        monthString = 0 + "" + (newDate.getMonth() + 1).toString();
    } else {
        monthString = (newDate.getMonth() + 1).toString();
    }
    //如果天数长度少于2，则前加 0 补位
    if (newDate.getDate().toString().length == 1) {
        dayString = 0 + "" + newDate.getDate().toString();
    } else {
        dayString = newDate.getDate().toString();
    }
    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;
    return dateString;
}

//获取星期几
function getWeek(date) {
    var mydate = new Date(date);
    //当月第一天星期
    var week = 0;
    week = parseInt(mydate.getDay());
    return week;
}

