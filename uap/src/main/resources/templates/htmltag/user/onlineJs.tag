<script type="application/javascript">



    /**
     * 初始化用户列表组件
     */
    function initOnlineGrid() {
        var pager = $('#userGrid').datagrid('getPager');
        pager.pagination({
            <#controls_paginationOpts/>,
            buttons:[
                <#resource code="forcedOffline">
                {
                    iconCls:'icon-undo',
                    text:'强制下线',
                    handler:function(){
                        forcedOffline();
                    }
                }
                </#resource>
            ]
        });
        //表格仅显示下边框
        $('#userGrid').datagrid('getPanel').removeClass('lines-both lines-no lines-right lines-bottom').addClass("lines-bottom");
    }

    /**
     * 绑定页面回车事件，以及初始化页面时的光标定位
     * @formId
     *          表单ID
     * @elementName
     *          光标定位在指点表单元素的name属性的值
     * @submitFun
     *          表单提交需执行的任务
     */
    $(function () {
        /**
         * 加载部门信息
         */
        <% if (has(isGroup) && isGroup){ %>
            var obj={code:null,name:'-- 请选择 --'};
            //为了不改变原值，所以复制一遍数组
            var firmData = firms.slice();
            //动态添加'请选择'
            firmData.unshift(obj);
            $("#firmCode").combobox("loadData", firmData);
        <%}else{%>
            loadDepartments(firmCode,'departmentId');
            loadRoles(firmCode);
        <%}%>
        
        bindFormEvent("form", "userName", queryGrid);
        initOnlineGrid();
        queryGrid();
    });

    //表格查询
    function queryGrid() {
        var opts = $('#userGrid').datagrid("options");
        if (null == opts.url || "" == opts.url) {
            opts.url = "${contextPath}/user/listOnlinePage.action";
        }
        if(!$('#form').form("validate")){
            return;
        }
        $('#userGrid').datagrid("load", bindGridMeta2Form("userGrid", "form"));
    }

    /**
     * 强制下线
     */
    function forcedOffline() {
        var selected = $('#userGrid').datagrid("getSelected");
        if (null == selected) {
            $.messager.alert('警告','请选中一条数据');
            return;
        }
        $.messager.confirm('确认','您确认想要强制下线['+selected.userName+']吗？',function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/user/forcedOffline.action",
                    data: {id:selected.id},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success: function (ret) {
                        if(ret.success){
                            $('#userGrid').datagrid("reload");
                        }else{
                            $.messager.alert('错误',ret.result);
                        }
                    },
                    error: function(){
                        $.messager.alert('错误','远程访问失败');
                    }
                });
            }
        });
    }
    
</script>