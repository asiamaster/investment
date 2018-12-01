
/**
 * 手机号码格式化
 * 隐藏手机号码(11位)第4-7位用*表示，例如：136****1212
 * 非11位参数座机号码规则，例如：0288****212
 */
function mobilePhoneFmt(value,row,index) {
    if (value == null || value.length == 0) {
        return null;
    }
    if (value.length == 11) {
        var reg = /^(\d{3})\d{4}(\d{4})$/;
        return value.replace(reg, "$1****$2");
    }
    if (value.length > 3 && value.length < 7 ){
        return plusXing(value, 0, 3);
    }
    if(value.length >=7){
        return plusXing(value, value.length-7, 3);
    }
    return value;
}

/**
 * 号码加星隐藏
 * @param str
 * @param frontLen 前面保留位数
 * @param endLen 后面保留位数
 * @return {string}
 */

function plusXing(str,frontLen,endLen) {
    if(str == undefined || str == null){
        return null;
    }
    if(typeof str == 'number'){
        str = "" + str;
    }
    var len = str.length-frontLen-endLen;
    if(len <= 0){
        return str;
    }
    var xing = '';
    for (var i=0;i<len;i++) {
        xing+='*';
    }
    return str.substring(0,frontLen)+xing+str.substring(str.length-endLen);
}
