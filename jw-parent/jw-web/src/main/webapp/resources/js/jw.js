var $jw = (function() {
    "use strict";

    var that = {};

    that.highlight = function(text, target, checkSpace) {
        var result = text;
        var reg = new RegExp(target,'gi');
        var list = [];
        var arr, prev, next;
        var hasSpace = false;
        while ((arr = reg.exec(text)) != null) {
            if(checkSpace) {
                hasSpace = false;
                prev = arr.index - 1;
                if(prev >= 0 && /\w/.test(text.substr(prev, 1))) {
                    hasSpace = true;
                }
                next = arr.index + arr[0].length;
                if(next < text.length && /\w/.test(text.substr(next, 1))) {
                    hasSpace = true;
                }
                if(hasSpace) {
                    continue;
                }
            }
            list.push({'index':arr.index,'word':arr[0]});
        }
        if(list.length>0) {
            var item;
            for(var i = list.length - 1;i >= 0; i--) {
                item = list[i];
                result = result.insert('</span>', item.index + item.word.length);
                result = result.insert('<span class="hign-light">', item.index);
            }
        }
        
        return result;;
    }

    return that;
}());


String.prototype.insert = function(text,at) {
    if(at == null || at > this.length)
        at = this.length;
    else if(at < 0)
        at = 0;
    
    return this.substring(0,at)+text+this.substring(at);
}