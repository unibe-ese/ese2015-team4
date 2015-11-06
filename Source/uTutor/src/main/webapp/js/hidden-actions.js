function hiddenAction(action, objectId){
	document.frmHiddenActions.action.value=action;
	document.frmHiddenActions.objectId.value=objectId;
	document.frmHiddenActions.submit();
}