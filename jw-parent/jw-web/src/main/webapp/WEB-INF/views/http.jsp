<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Jay Zhang">

<title>Jw Labs-HTTP</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${root}/plugin/codemirror/lib/codemirror.min.css">
<style type="text/css">
#main {
    color: #666
}

@media screen and (max-height:1080px) {
    .CodeMirror {
        height: 750px;
    }
}

@media screen and (max-height:800px) {
    .CodeMirror {
        height: 400px;
    }
}

</style>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

    <jsp:include page="./nav.jsp"></jsp:include>
    <div id="wrapper">
        <div id="page-wrapper">

            <div class="container-fluid">
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12">
                        <label><span>网址:&nbsp;&nbsp;</span></label>
                        <input id="txtUrl" type="text" name="url" class="form-control" style="display:inline-block;width:550px;"></input>
                        <label><span>方法:&nbsp;&nbsp;</span></label>
                        <input type="radio" name="method" value="POST" checked>POST&nbsp;&nbsp;<input type="radio" name="method" value="PUT">PUT&nbsp;&nbsp;
                        <label><span>格式:&nbsp;&nbsp;</span></label>
                        <input type="radio" name="contentType" value="XML" checked>XML&nbsp;&nbsp;<input type="radio" name="contentType" value="JSON">JSON&nbsp;&nbsp;
                        <button id="btnSend" class="btn btn-success">发&nbsp;&nbsp;送</button>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12">
                        <label><span>头部:&nbsp;&nbsp;</span></label>
                        <input id="txtHeader" type="text" name="header" class="form-control" style="display:inline-block;width:550px;" placeholder="hearder1=headerValue1;hearder2=headerValue2;"></input>
                        <label title="如果分割报文，例如2行，那么每２行数据作为一个报文发送，直到全部发送完"><span>分割请求报文:&nbsp;&nbsp;</span></label>
                        <input type="radio" name="splitRequest" value="-1" checked>不分割&nbsp;&nbsp;
                        <input type="radio" name="splitRequest" value="1">1行&nbsp;&nbsp;
                        <input type="radio" name="splitRequest" value="2">2行&nbsp;&nbsp;
                        <input type="radio" name="splitRequest" value="3">3行&nbsp;&nbsp;
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-6 col-lg-6">
                        <label><span>请求</span></label>
                        <textarea title="请求" id="request" name="request" style="display: none;"></textarea>
                    </div>
                    <div class="col-md-6 col-lg-6">
                        <label><span>响应</span></label>&nbsp;&nbsp;
                        <input type="text" id="txtSuccess" style="width:550px" placeholder="请输入正确的响应结果应包含的关键字，多个关键字以;分割。留空则显示原始响应报文">
                        <textarea title="响应" id="response" name="response" style="display: none;"></textarea>
                    </div>
                </div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>

    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="${root}/js/jquery.min.js"></script>
    <script src="${root}/js/bootbox.js"></script>
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript" >
       var httpUrl = 'http.url';
       var httpMethod = 'http.method';
       var httpContentType = 'http.contentType';
       var httpRequest = 'http.request';
       var httpResponse = 'http.response';
       var httpHeader = 'http.header';
       var httpSplitRequest = 'http.splitRequest';
       var httpSuccess= 'http.success';
       
       var response = "";
       
        var editor0 = null;
        var editor1 = null;
        $(function() {
            $("#menuHttp").addClass("active");
            editor0 = CodeMirror.fromTextArea(document.getElementById("request"), {
                mode: "text/plain",
                lineNumbers: true,
                styleActiveLine: true,
                indentUnit:4
              });
            editor1 = CodeMirror.fromTextArea(document.getElementById("response"), {
                mode: "text/plain",
                lineNumbers: true,
                styleActiveLine: true,
                indentUnit:4
              });
            $('#txtUrl').val($jw.readStorage(httpUrl) || '');
            if($jw.readStorage(httpMethod)) {
                $("input[name=method][value='" + $jw.readStorage(httpMethod) + "']").prop("checked", "checked");
            }
            if($jw.readStorage(httpContentType)) {
                $("input[name=contentType][value='" + $jw.readStorage(httpContentType) + "']").prop("checked", "checked");
            }
            if($jw.readStorage(httpSplitRequest)) {
                $("input[name=splitRequest][value='" + $jw.readStorage(httpSplitRequest) + "']").prop("checked", "checked");
            }
            editor0.setValue($jw.readStorage(httpRequest) || '');
            editor1.setValue($jw.readStorage(httpResponse) || '');
            $('#txtHeader').val($jw.readStorage(httpHeader) || '');
            $('#txtSuccess').val($jw.readStorage(httpSuccess) || '');
            $('#btnSend').click(function(e) {
                $jw.saveStorage(httpUrl, $('#txtUrl').val());
                $jw.saveStorage(httpMethod, $("input[name=method]:checked").val());
                $jw.saveStorage(httpContentType, $("input[name=contentType]:checked").val());
                $jw.saveStorage(httpRequest, editor0.getValue());
                $jw.saveStorage(httpHeader, $('#txtHeader').val());
                $jw.saveStorage(httpSplitRequest, $("input[name=splitRequest]:checked").val());
                $jw.saveStorage(httpSuccess, $('#txtSuccess').val());
                
                var content = editor0.getValue();
                editor1.setValue('');
                var param = {'url':$('#txtUrl').val(),
                    'method':$("input[name=method]:checked").val(),
                    'contentType':$("input[name=contentType]:checked").val(),
                    'header':$('#txtHeader').val(),
                    'body':content.trim()};
                if(!param.url) {
                    bootbox.alert('请输入网址！');
                    return false;
                }
                if(!param.body) {
                    bootbox.alert('请设置报文！');
                    return false;
                }
                
                var splitRequest = Number($("input[name=splitRequest]:checked").val());
                if(splitRequest < 0) {
                    sendRequest(param);
                } else {
                    var rows = [];
                    var returns = [];
                    if(splitRequest == 1) {
                        rows = content.split('\n');
                        var len = rows.length;
                        for(var i=0;i<len;i++) {
                            returns.push('\n');
                        }
                    } else {
                        var list = content.split('\n');
                        var len = list.length;
                        for(var i=0;i<len;i+=splitRequest) {
                            var row = "";
                            var splitChars = "";
                            for(var j=0; j < splitRequest;j++) {
                                splitChars+="\n";
                                if(i+j < len) {
                                    row += list[i+j] + "\n";
                                }
                            }
                            rows.push(row);
                            returns.push(splitChars);
                        }
                    }
                    param.body = "";
                    var params = $.extend({},param);
                    response = "";
                    sendRequest(params, rows, returns, 0);
                }
                return false;
            });
        });
        
        function sendRequest(param, rows, returns, lineIndex) {
            var argAmount = arguments.length;
            if(argAmount == 4) {
                if(lineIndex >= rows.length) {
                    if(response && response.endsWith('\n')) {
                        response = response.substring(0, response.length-1);
                        editor1.setValue(response);
                    }
                    $jw.saveStorage(httpResponse, response);
                    bootbox.alert("任务结束！");
                    return;
                }
                param.body = rows[lineIndex];
            }
            $.ajax({  
                url : "${root}/http/send",  
                type : 'POST',  
                data : JSON.stringify(param),  
                contentType : 'application/json;charset=utf-8',  
                success : function(data, status, xhr) {  
                    if(data && data.data) {
                        if(argAmount == 4) {
                            if($('#txtSuccess').val()) {
                                response += meetKeywords(data.data) ? 'Y' : 'N';
                                response += returns[lineIndex];
                            } else {
                                response += data.data.replaceAll('\n','');
                                response += returns[lineIndex];
                            }
                            editor1.setValue(response);
                        } else {//argAmount != 4
                            if($('#txtSuccess').val()) {
                                response += meetKeywords(data.data) ? 'Y' : 'N';
                                response += '\n';
                            } else {
                                response += data.data.replaceAll('\n','');
                                response += returns[lineIndex];
                            }
                            $jw.saveStorage(httpResponse, response);
                        }
                    } else {// no data.data
                        if(argAmount == 4) {
                            if($('#txtSuccess').val()) {
                                response += 'N' + returns[lineIndex];
                            } else {
                                response += (data.message? data.message.replaceAll('\n','') : '') + returns[lineIndex];
                            }
                            editor1.setValue(response);
                        } else {
                            editor1.setValue(data.message + '\n');
                        }
                        $jw.saveStorage(httpResponse, response);
                    }
                    if(argAmount == 4) {
                        sendRequest(param, rows, returns, lineIndex + 1);
                    } else {
                        if(data.status == 0) {
                            bootbox.alert('请求失败！');
                        }
                    }
                },  
                error : function(xhr, error, exception) {
                    if(argAmount == 4) {
                        if($('#txtSuccess').val()) {
                            response += 'N' + returns[lineIndex];
                        } else {
                            response += (exception.toString()? exception.toString().replaceAll('\n','') : '') + returns[lineIndex];
                        }
                        editor1.setValue(response);
                        sendRequest(param, rows, returns, lineIndex + 1);
                    } else {
                        bootbox.alert(xhr.status + ":" + exception.toString());  
                    }
                }  
            });
        }
        
        function meetKeywords(content) {
            var meet = false;
            if(content) {
                var keywords = $('#txtSuccess').val().split(";");
                for(var i=0;i<keywords.length;i++){
                    if(!keywords[i]) {
                        continue;
                    }
                    if(content.indexOf(keywords[i]) > -1) {
                        meet = true;
                        break;
                    }
                }
            }
            return meet;
        }
        
        String.prototype.endWith = function(s) {
            if (s == null || s == "" || this.length == 0 || s.length > this.length)
                return false;
            if (this.substring(this.length - s.length) == s)
                return true;
            else
                return false;
            return true;
        }

        String.prototype.startWith = function(s) {
            if (s == null || s == "" || this.length == 0 || s.length > this.length)
                return false;
            if (this.substr(0, s.length) == s)
                return true;
            else
                return false;
            return true;
        }

        String.prototype.replaceAll = function(s1,s2){
            return this.replace(new RegExp(s1,"gm"),s2);
        }

        String.prototype.trim = function(){   
             return   this.replace(/(^\s*)|(\s*$)/g,"");   
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
