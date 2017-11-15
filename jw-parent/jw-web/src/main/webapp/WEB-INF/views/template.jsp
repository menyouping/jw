<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

    <jsp:include page="./header.jsp"></jsp:include>

    <title>Jw Labs-集合</title>

    <!-- Bootstrap Core CSS -->
    <link href="${root}/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${root}/css/sb-admin.min.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${root}/plugin/codemirror/lib/codemirror.min.css">
    
    <style type="text/css">
    .btn-operation {
       margin: 10px 10px;
       width: 80px;
    }
    @media screen and (max-height:1080px) {
        .CodeMirror {
            height: 835px;
        }
    }
    
    @media screen and (max-height:800px) {
        .CodeMirror {
            height: 450px;
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
                <div class="row" style="padding-top:10px;">
                    <div class="col-md-3 col-lg-3">
                        <label>源数据A</label>
                        <button id="btnUnique0" class="btn btn-danger">去重</button>
                        <button id="btnAsc0" class="btn btn-default">升序</button>
                        <button id="btnDesc0" class="btn btn-default">降序</button>
                    </div>
                    <div class="col-md-5 col-lg-5">
                        <label>处理器B</label>
                        <input type="radio" name="method" value="TEMPLATE" checked>模板&nbsp;&nbsp;
                        <input type="radio" name="method" value="FUNCTION">函数&nbsp;&nbsp;
                        <button id="btnTrim" class="btn btn-default">去空格</button>
                        <button id="btnRemoveLine" class="btn btn-default">去行号</button>
                    </div>
                    <div class="col-md-1 col-lg-1">
                        <label>&nbsp;</label>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <label><span id="txtResultTitle"></span></label>
                        <button id="btnCopy" class="btn btn-danger" style="display:none;">复制到A</button>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top:10px;">
                    <div class="col-md-3 col-lg-3">
                        <textarea id="code0" name="code0" style="display: none;"></textarea>
                    </div>
                    <div class="col-md-5 col-lg-5" >
                        <textarea id="code1" name="code1" style="display: none;"></textarea>
                    </div>
                    <div class="col-md-1 col-lg-1" >
                        <div style="margin-top:120px;">
                            <button id="btnTemplate" title='A为数据源，B为模板或函数, 以模板方法处理A中的每行数据' class="btn btn-success btn-operation">美化</button>
                        </div>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <textarea id="code2" name="code2" style="display: none;"></textarea>
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

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/bootbox.js"></script>
    <script src="${root}/js/jsl.format.min.js"></script>
    <script src="${root}/js/jsl.parser.min.js"></script>
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/closebrackets.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchbrackets.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/javascript/javascript.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
    var editor0, editor1, editor2;
    var storageKey0 = 'tpl.edit0',storageKey1 = 'tpl.edit1', tplMethod = 'tpl.method';
    $(function(){
        $("#menuTemplate").addClass("active");
        
        if($jw.readStorage(tplMethod)) {
            $("input[name=method][value='" + $jw.readStorage(tplMethod) + "']").prop("checked", "checked");
        }
        
        editor0 = CodeMirror.fromTextArea(document.getElementById("code0"), {
            mode: "text/plain",
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor0.setValue($jw.readStorage(storageKey0) ||'');
        editor1 = CodeMirror.fromTextArea(document.getElementById("code1"), {
            mode: "application/json",
            matchBrackets: true,
            autoCloseBrackets: true,
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor1.setValue($jw.readStorage(storageKey1) ||'AAA{{line}}BBB');
        editor2 = CodeMirror.fromTextArea(document.getElementById("code2"), {
            mode: "text/plain",
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor0.on('change', function(instance, changeObj) {
            editorChanged();
            $('#btnCopy').hide();
        });
        editor1.on('change', function(instance, changeObj) {
            editorChanged();
        });
        
        $('input[name=method]').click(function(e){
            var $that = $(this);
            if($that.val() == "TEMPLATE") {
                editor1.setValue("{{i}}.{{line}}");
            } else{
                editor1.setValue("function beautify(line, i) {\n    return line;\n}");
            }
        });
        $('#btnTrim').click(function(e){
            $("input[name=method][value='FUNCTION']").prop("checked", "checked");
            editor1.setValue("function beautify(line, i) {\n    return line.trim();\n}");
        });
        $('#btnRemoveLine').click(function(e){
            $("input[name=method][value='FUNCTION']").prop("checked", "checked");
            editor1.setValue("function beautify(line, i) {\n    return line.replace(/^\\d+\\./,'');\n}");
        });
        $('#btnTemplate').click(function(e){
            $('#txtResultTitle').html('结果');
            $('#btnCopy').show();
            var content0 = editor0.getValue();
            var content1 = editor1.getValue();
            if(!content0) {
                editor2.setValue('');
            } else if(!content1){
                bootbox.alert('请设置模板！例如"AAA{{line}}BBB"');
            } else {
                var list2 = [];
                var list0 = content0.split('\n');
                var len = list0.length;
                var line;
                var tpl = content1.replaceAll('\s*\n+\s*','');
                
                var method = $('input[name=method]:checked').val();
                    if(method == "TEMPLATE") {
                        for(var i = 0;i < len; i++) {
                            line = list0[i];
                            list2.push(tpl.replaceAll("{{line}}",line).replaceAll("{{i}}",(i+1)));
                        }
                    } else{
                        try {
                            var func = content1;
                            eval(func);
                        } catch (ex) {
                            bootbox.alert("函数编译失败!");
                            return false;
                        }
                        for(var i = 0;i < len; i++) {
                            line = list0[i];
                            list2.push(beautify(line, i + 1));
                        }
                    }
                
                editor2.setValue(list2.join('\n'));
            }
            $jw.saveStorage(storageKey0, content0);
            $jw.saveStorage(storageKey1, content1);
            $jw.saveStorage(tplMethod, $("input[name=method]:checked").val());
        });
        $('#btnUnique0').click(function(e){
            var content0 = editor0.getValue();
            var rs = unique(content0);
            editor0.setValue(rs);
            $jw.saveStorage(storageKey0, rs);
        });
        $('#btnUnique1').click(function(e){
            var content1 = editor1.getValue();
            var rs = unique(content1);
            editor1.setValue(rs);
            $jw.saveStorage(storageKey1, rs);
        });
        $('#btnAsc0').click(function(e){
            sort(editor0, true);
            $jw.saveStorage(storageKey0, editor0.getValue());
        });
        $('#btnDesc0').click(function(e){
            sort(editor0, false);
            $jw.saveStorage(storageKey0, editor0.getValue());
        });
        $('#btnCopy').click(function(e){
            editor0.setValue(editor2.getValue());
        });
    });
    
    function stringAsc(a, b) {
        return a == b ? 0 : (a > b ? 1 : -1);
    }
    
    function stringDesc(a, b) {
        return -stringAsc(a, b);
    }
    
    function numberAsc(a, b) {
        return parseFloat(a) - parseFloat(b);
    }
    
    function numberDesc(a, b) {
        return -numberAsc(a, b);
    }
    
    function sort(editCtrl, isAsc) {
        var content = editCtrl.getValue();
        if(!content) {
            return;
        }
        var list = content.split('\n');
        var isNumber = true;
        if(/[a-zA-Z]/.test(content)) {
            isNumber = false;
        } else {
            var len = list.length;
            var n;
            for(var i = 0;i<len;i++) {
                n = list[i];
                if(isNaN(parseFloat(n)) || !isFinite(n)) {
                    isNumber = false;
                    break;
                }
            }
        }
        
        if(isNumber) {
            editCtrl.setValue(list.sort(isAsc ? numberAsc : numberDesc).join('\n'));
        } else {
            editCtrl.setValue(list.sort(isAsc ? stringAsc : stringDesc).join('\n'));
        }
    }
    
    function unique(content) {
        if(!content) {
            return content;
        }
        var rs = handle(content);
        return rs.list.join('\n');
    }
    
    function handle(content) {
        if(!content) {
            return {'list':[],'map':{}};
        }
        var arr = content.split('\n');
        var map = {};
        var len = arr.length;
        var line;
        var rs = [];
        for(var i = 0;i < len; i++) {
            line = arr[i];
            if(line && !map.hasOwnProperty(line)) {
                rs.push(line);
                map[line] = 1;
            }
        }
        return {'list':rs,'map':map};
    }
    
    function toMap(content) {
        if(!content) {
            return {};
        }
        var arr = content.split('\n');
        var map = {};
        var len = arr.length;
        var line;
        for(var i = 0;i < len; i++) {
            line = arr[i];
            if(line && !map.hasOwnProperty(line)) {
                map[line] = 1;
            }
        }
        return map;
    }
    
    function editorChanged() {
        $('#txtResultTitle').html('');
        editor2.setValue('');
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
