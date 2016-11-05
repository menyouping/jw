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
    <link href="${root}/css/sb-admin.min.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
    <link href="${root}/css/jw/index.css" rel="stylesheet">

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

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-md-12 col-lg-12" style="text-align:center;padding:40px 0px 20px 0px;">
                        <h1>
                            英汉字典
                        </h1>
                    </div>
                </div>
                <!-- /.row -->

                <div class="row">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-8 col-lg-8">
                        <form role="form">
                            <div class="form-group input-group">
                                <input type="text" id="txtKeyword" class="form-control" placeholder="单词或词组从这里开始">
                                <span class="input-group-btn"><button id="btnQuery" class="btn btn-lg btn-success" type="button" style="width:100px;"><i class="jw-search" style="font-size:20px;"></i></button></span>
                            </div>

                        </form>
                    </div>
                </div>
                <!-- /.row -->

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

    <script type="text/javascript">
    $(function(){
        var $txtKeyword = $('#txtKeyword');
        $("#menuDict").addClass("active");
        $('#btnQuery').click(function(e){
            var keyword = $txtKeyword.val();
            if(!keyword) {
                $txtKeyword.focus();
            } else {
                location.href = '${root}/dict/' + keyword;
            }
        });
        $txtKeyword.keypress(function(e) {
            if (e.which == 13) {//监听回车事件
                $('#btnQuery').click();
	            return false;
            }
        });
    });
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
