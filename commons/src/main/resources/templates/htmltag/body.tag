<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>地利生鲜管理平台</title>
    <meta name="description" content="overview &amp; stats" />
    <link rel="shortcut icon" href="${contextPath}/resources/images/icon/fresh_icon.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <#css/>
    <#js/>
    <style>
        /** sweetAlert2 弹窗设置 */
        .swal2-container {
            z-index: 9999999
        }
    </style>
    <%
    if(has(pageCss) && pageCss=="true"){
    %>
    <#pageCss/>
    <%
    }
    %>
    
    <% if(has(pageJs) && pageJs=="true"){%>
    <#pageJs/>
    <%}%>
    <script type="text/javascript">
        var contextPath = '${contextPath}';
    </script>

</head>
<body>
<#loadingProgress/>
${tag.body}
</body>
</html>
