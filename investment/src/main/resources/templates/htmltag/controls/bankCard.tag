<script type="text/javascript">
// 打开选择银行卡弹出框
function _selectBankCard(callback, args) {
	if (callback) {
		eval("(" + callback + "(args))");
	} else {
		showBankCardDlg($(this)[0].id);
	}
}
// 确认选择事件
function confirmBankCardBtn(id) {
	var selected = $('#selectBankCardGrid').datagrid('getSelected');
	if (null == selected) {
		$.messager.alert('警告','请选中一条数据');
		return;
	}
	$('#' + id).textbox('initValue', selected.id);
	$('#' + id).textbox('setText', selected.cardNumber);
	var icon = $('#' + id).textbox('getIcon',0);
	icon.css('visibility','visible');
	$('#${dlgId}').dialog('close');
}
//关闭银行卡选择窗口
function closeBankCardSelectDlg(){
	$('#${dlgId}').dialog('close');
}
// 根据id打开银行卡选择
function showBankCardDlg(id) {
	$('#${dlgId}').dialog({
				title : '银行卡选择',
				width : 800,
				height : 400,
				queryParams : {
					textboxId : id
				},
				href : '${contextPath!}/selectDialog/bankCard.html',
				modal : true,
				shadow : true
			});
}

$(function() {
	$('#${controlId}').textbox('addClearBtn', 'icon-clear');
})
</script>
<div id="${dlgId}" style="display: none;"></div>