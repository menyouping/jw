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
                        <label>集合A</label>
                        <button id="btnUnique0" class="btn btn-danger">去重</button>
                        <button id="btnAsc0" class="btn btn-default">升序</button>
                        <button id="btnDesc0" class="btn btn-default">降序</button>
                    </div>
                    <div class="col-md-3 col-lg-3">
                        <label>集合B</label>
                        <button id="btnUnique1" class="btn btn-danger">去重</button>
                        <button id="btnAsc1" class="btn btn-default">升序</button>
                        <button id="btnDesc1" class="btn btn-default">降序</button>
                    </div>
                    <div class="col-md-1 col-lg-1">
                        <label>&nbsp;</label>
                    </div>
                    <div class="col-md-5 col-lg-5">
                        <label><span id="txtResultTitle"></span></label>
                        <button id="btnAsc2" class="btn btn-default" style="display: none;">升序</button>
                        <button id="btnDesc2" class="btn btn-default" style="display: none;">降序</button>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top:10px;">
                    <div class="col-md-3 col-lg-3" >
                        <textarea id="code0" name="code0" style="display: none;"></textarea>
                    </div>
                    <div class="col-md-3 col-lg-3" >
                        <textarea id="code1" name="code1" style="display: none;"></textarea>
                    </div>
                    <div class="col-md-1 col-lg-1" >
                        <div style="margin-top:120px;">
                            <button id="btnAll" class="btn btn-success btn-operation">A ∪ B</button><br>
                            <button id="btnOverlap" class="btn btn-primary btn-operation">A ∩ B</button><br>
                            <button id="btnSubtract" class="btn btn-warning btn-operation">A - B</button>
                            <button id="btnSubtract2" class="btn btn-warning btn-operation">B - A</button>
                        </div>
                    </div>
                    <div class="col-md-5 col-lg-5">
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
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
    var editor0, editor1, editor2;
    var storageKey0 = 'set.edit0',storageKey1 = 'set.edit1';
    $(function(){
        $("#menuSet").addClass("active");
        editor0 = CodeMirror.fromTextArea(document.getElementById("code0"), {
            mode: "text/plain",
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor0.setValue($jw.readStorage(storageKey0) ||'');
        editor1 = CodeMirror.fromTextArea(document.getElementById("code1"), {
            mode: "text/plain",
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor1.setValue($jw.readStorage(storageKey1) ||'');
        editor2 = CodeMirror.fromTextArea(document.getElementById("code2"), {
            mode: "text/plain",
            lineNumbers: true,
            styleActiveLine: true,
            indentUnit:4
          });
        editor0.on('change', function(instance, changeObj) {
            editorChanged();
        });
        editor1.on('change', function(instance, changeObj) {
            editorChanged();
        });
        $('#btnAll').click(function(e){
            $('#txtResultTitle').html('A ∪ B');
            var content0 = editor0.getValue();
            var content1 = editor1.getValue();
            var rs = handle(content0 + '\n' + content1);
            editor2.setValue(rs.list.join('\n'));
            $('#btnAsc2').show();
            $('#btnDesc2').show();
            $jw.saveStorage(storageKey0, content0);
            $jw.saveStorage(storageKey1, content1);
        });
        $('#btnOverlap').click(function(e){
            $('#txtResultTitle').html('A ∩ B');
            var content0 = editor0.getValue();
            var content1 = editor1.getValue();
            if(!content0 || !content1) {
                editor2.setValue('');
            } else {
                var rs0 = handle(content0);
                var list2 = [];
                var map = rs0.map;
                var list1 = content1.split('\n');
                var len = list1.length;
                var line;
                for(var i = 0;i < len; i++) {
                    line = list1[i];
                    if(line && map.hasOwnProperty(line)) {
                        list2.push(line);
                    }
                }
                editor2.setValue(list2.join('\n'));
            }
            $('#btnAsc2').show();
            $('#btnDesc2').show();
            $jw.saveStorage(storageKey0, content0);
            $jw.saveStorage(storageKey1, content1);
        });
        $('#btnSubtract').click(function(e){
            $('#txtResultTitle').html('A - B');
            var content0 = editor0.getValue();
            var content1 = editor1.getValue();
            if(!content0) {
                editor2.setValue('');
            } else if(!content1){
                editor2.setValue(content0);
            } else {
                var list2 = [];
                var map = toMap(content1);
                var list0 = content0.split('\n');
                var len = list0.length;
                var line;
                for(var i = 0;i < len; i++) {
                    line = list0[i];
                    if(line && !map.hasOwnProperty(line)) {
                        list2.push(line);
                    }
                }
                editor2.setValue(list2.join('\n'));
            }
            $('#btnAsc2').show();
            $('#btnDesc2').show();
            $jw.saveStorage(storageKey0, content0);
            $jw.saveStorage(storageKey1, content1);
        });
        $('#btnSubtract2').click(function(e){
            $('#txtResultTitle').html('B - A');
            var content0 = editor0.getValue();
            var content1 = editor1.getValue();
            if(!content0) {
                editor2.setValue('');
            } else if(!content1){
                editor2.setValue(content0);
            } else {
                var list2 = [];
                var map = toMap(content0);
                var list0 = content1.split('\n');
                var len = list0.length;
                var line;
                for(var i = 0;i < len; i++) {
                    line = list0[i];
                    if(line && !map.hasOwnProperty(line)) {
                        list2.push(line);
                    }
                }
                editor2.setValue(list2.join('\n'));
            }
            $('#btnAsc2').show();
            $('#btnDesc2').show();
            $jw.saveStorage(storageKey0, content0);
            $jw.saveStorage(storageKey1, content1);
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
        $('#btnAsc1').click(function(e){
            sort(editor1, true);
            $jw.saveStorage(storageKey1, editor1.getValue());
        });
        $('#btnDesc1').click(function(e){
            sort(editor1, false);
            $jw.saveStorage(storageKey1, editor1.getValue());
        });
        $('#btnAsc2').click(function(e){
            sort(editor2, true);
        });
        $('#btnDesc2').click(function(e){
            sort(editor2, false);
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
        $('#btnAsc2').hide();
        $('#btnDesc2').hide();
    }

    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
