var $jw = (function() {
    "use strict";

    var that = {};
    
    var storage = window.localStorage;

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
    /**
     * 写cookies 
     */
    that.setCookie = function(name,value) 
    { 
        var Days = 30; 
        var exp = new Date(); 
        exp.setTime(exp.getTime() + Days*24*60*60*1000); 
        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
    } 

    /**
     * 读取cookies
     */ 
    that.getCookie = function(name) 
    { 
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
     
        if(arr=document.cookie.match(reg))
     
            return unescape(arr[2]); 
        else 
            return null; 
    } 
    
    that.saveStorage = function(key, value) {
    	if (storage) { 
            storage.setItem(key, value);   
        } else { 
            that.setCookie(key, value);  
        } 
    }
    
    that.readStorage = function(key) {
        if(storage.getItem(key) != null){ 
            return storage.getItem(key); 
        } else if(that.getCookie(key) != null){ 
            return that.getCookie(key); 
        } 
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

String.prototype.removeLineEnd = function() {
    return this.replace(/(<.+?\s+?)(?:\n\s*?(.+?=".*?"))/g,'$1 $2')
}