<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

    <jsp:include page="./header.jsp"></jsp:include>

    <title>Jw Labs-字典</title>

    <!-- Bootstrap Core CSS -->
    <link href="${root}/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${root}/css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
    <link href="${root}/css/jw/dict.css" rel="stylesheet">

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
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10" style="margin-left:15px;margin-right:15px">
                         <i class="jw-search searchicon"></i>
                         <input type="text" id="txtKeyword" class="form-control" placeholder="单词或词组从这里开始" value="${keyword}">
                         <i id="btnClear" class="jw-times clearicon"></i>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top:60px;display:none;">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10 word" style="margin-left:15px;margin-right:15px">
                        <div id="meaning-content">
                        </div>
                    </div>
                </div>
                
                <!-- /.row -->
                <div class="row" style="display:none;margin-bottom:10px;">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10 sentence" style="margin-left:15px;margin-right:15px">
                        <div id="sentence-content">
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <div id="divPlayer" style="display:none;"></div>
    
    <script id="meaning-content-template" type="text/x-dot-template">
        {{? it}}
             <span style="font-weight:bold;font-size:24px;">${keyword}</span>
             {{? it.ps}}
             <span>
                 {{? it.ps.length == 1}}
                 <span>
                     <span>[{{=it.ps[0]}}]</span>
                     <i class="jw-volume-up voice" data-url="{{=it.pron[0]}}"></i>
                 </span>
                 {{?}}
                 {{? it.ps.length == 2}}
                 <span>
                     <span>英 [{{=it.ps[0]}}]</span>
                     <i class="jw-volume-up voice" data-url="{{=it.pron[0]}}"></i>
                 </span>
                 <span>
                     <span>美 [{{=it.ps[1]}}]</span>
                     <i class="jw-volume-up voice" data-url="{{=it.pron[1]}}"></i>
                 </span>
                 {{?}}
            </span>
            {{?}}
            {{? it.acceptation && it.acceptation.length}}
                {{ for(var i in it.acceptation) { }}
                    <div class="meaning">
                        <span class="pos">
                            {{? it.pos.length}}
                                {{=it.pos[i]}}
                            {{??}}
                                &nbsp;
                            {{?}}
                        </span>
                        <span>{{=it.acceptation[i]}}</span>
                    </div>
                {{ } }}
            {{??}}
                {{? it.fy}}
                    <div class="meaning">
                        <span>{{=it.fy}}</span>
                    </div>
                {{?}}
            {{?}}
        {{?}}
    </script>

    <script id="sentence-content-template" type="text/x-dot-template">
        {{? it}}
            {{? it.sent && it.sent.length}}
                <div class="example">
                    例句
                </div>
                {{ for(var i in it.sent) { }}
                    <div>
                        <span>{{=(parseInt(i)+1)}}.</span>
                        <span>{{=highlightWord('en',it.sent[i].orig)}}</span><br>
                        <span class="sentence-cn">{{=highlightWord('cn',it.sent[i].trans)}}</span>
                    </div>
                {{ } }}
            {{?}}
        {{?}}
    </script>
    
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="${root}/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/doT.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
    $(function(){
        var $txtKeyword = $('#txtKeyword');
        $("#liDict").addClass("active");
        var keyword = '${keyword}';
        $('#btnClear').click(function(){
            $txtKeyword.val('').focus();
        });
        $txtKeyword.keyup(function(e) {
            if (e.keyCode == 13) {//监听回车事件
                var keyword = $('#txtKeyword').val();
                if(!keyword) {
                    $('#txtKeyword').focus();
                } else {
                    location.href = "${root}/dict/" + keyword;
                }
            }
        });
        
        $.post("${root}/dict/translate/" + keyword,{},
            function(result){
               if(result && result.status == 200) {
                   var context = JSON.parse(result.result);
                   var meaningfn = doT.template($('#meaning-content-template').text());
                   $('#meaning-content').html(meaningfn(context));

                   var sentencefn = doT.template($('#sentence-content-template').text());
                   $('#sentence-content').html(sentencefn(context));

                   if(!context.sent || context.sent.length == 0) {
                       $('#sentence-content').parents('.row').hide();
                   }

                   $('.voice').click(function(e){
                       var txt= '<audio autoplay="autoplay"><source src="{mp3}" type="audio/mpeg"/></audio>'; 
                       $('#divPlayer').html(txt.replace('{mp3}', $(this).data('url')));
                   });
                   $('#meaning-content').parents('.row').show();
                   $('#sentence-content').parents('.row').show();
               } else {
                   $('#meaning-content').html('<div class="col-md-12 aui-text-center">Ooop! 没有找到!</div>').parents('.row').show();
                   $('#sentence-content').parents('.row').hide();
               }
            }, "json");
    });
    
    function highlightWord(lang, sent) {
        if(lang == 'cn') {
            if(/[\u4e00-\u9fa5]/.test('${keyword}')) {
                return $jw.highlight(sent, '${keyword}', false);
            }
        } else {
            if(!/[\u4e00-\u9fa5]/.test('${keyword}')) {
                return $jw.highlight(sent, '${keyword}', true);
            }
        }
        return sent;
    }
    
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
