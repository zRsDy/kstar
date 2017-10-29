function getContentWindow(windowId) {
	return windowId == undefined ? $("#mainContent") : $("#" + windowId);
}
// open the url result to the window with the specified id.
// if no id specified, the use the max window
function openContentWindow(url, windowId) {
	var cnt = getContentWindow(windowId);
	var purl = url;
	if (purl.indexOf("?") < 0) {
		purl += "?a=";
	} else {
		purl += "&a=";
	}
	purl += (new Date()).getTime();
	if (cnt.length > 0) {
		$.ajax({
			url : purl,
			success : function(data) {
				cnt.html(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				var responseText = jqXHR.responseText;
				responseText = responseText.substring(responseText.indexOf('<body>') + 6, responseText.indexOf('</body>'));
				cnt.html("<span>请求数据出错：</span>" + responseText);
			}
		});
		return false;
	}
	return true;
}
// submit the form in the specified id window, then return the result to the
// window.
// if no id specified, the use the max window
function submitFormInWindow(theForm, windowId) {
	var cnt = getContentWindow(windowId);
	if (cnt.length > 0) {
		$.ajax({
			type : "POST",
			url : theForm.action,
			data : $(theForm).serialize(),
			success : function(data1) {
				cnt.html(data1);
			},
			error : function() {
				alert(errorMessage);
			}
		});
		return false;
	}
	return true;
}
function deletePortlet(thePortlet) {
	thePortlet.remove();
//	var url = contextPath + "/base/removePortlet/" + thePortlet.attr("id") + ".htm";
//	$.ajax({
//		url : url,
//		type : "DELETE",
//		success : function(data) {
//			thePortlet.remove();
//		},
//		error : function() {
//			alert('对不起，移除小窗口出错了！');
//		}
//	});

}
// save user portlet layout
function saveLayout(event, ui, newId, colId) {
	var list = "{";
	// var theCol = 1;
	$.each($(".column"), function(ci, col) {
		list += "{";
		$.each($(this).children(".portlet"), function(pi, plet) {
			list += plet.id + ",";
		});
		if (colId == col.id) {
			list += newId;
		} else if (list.charAt(list.length - 1) == ',') {
			list = list.substring(0, list.length - 1);
		}
		list += "},";
	});
	list = list.substring(0, list.length - 1);
	list += "}";
	$.ajax({
		url : contextPath + "/base/savePortletLayout.htm",
		type : "POST",
		data : ({
			"layout" : list
		}),
		success : function(data) {
			if (newId != undefined) {
				window.location.reload();
			}
		},
		error : function() {
			alert('对不起，保存小窗口布局出错了！');
		}
	});
}

function openlookup(lookupId) {
	$("#lookup_" + lookupId).dialog('open');
}
function closelookup(lookupId) {
	$("#lookup_" + lookupId).dialog('close');
}

function onNav(appUrl, funcUrl, isLtpa, isHomepage) {
	if (funcUrl == undefined || funcUrl == "") {
		return;
	}
	var frm = $("#navForm");

	frm.attr("target", "_blank");
	if (isHomepage != "T") {
		$("#WASReqURL").attr("value", funcUrl);
	}
	// if not SSO, then login first.
	if (isLtpa != "T") {
		frm.attr("action", appUrl);
		$.get("../appnav/AppNav_getUser.action", function(data) {
			var returnArr = trim(data);
			returnArr = returnArr.split(",");
			if (returnArr.length == 2) {
				var userid = returnArr[0];
				var password = returnArr[1];
				$("#j_username").attr("value", userid);
				$("#j_password").attr("value", password);
				frm.submit();
			} else {
				alert("或者用户认证信息错误,改错误可能是由于超时引起的,请重新登录后操作,如果问题依旧,请联系管员.");
				return;
			}

		});
	} else {
		frm.attr("action", funcUrl);
		frm.submit();
	}
}
function navToFunc(appUrl, funcUrl, isLtpa, isHomepage) {

	if (funcUrl != undefined && funcUrl != "") {
		if (isLtpa != 'T') {
			// 登录
			$.get("../appnav/AppNav_getUser.action", function(data) {
				var userid = data.substring(0, data.indexOf(","));
				var password = data.substring(data.indexOf(",") + 1);

				// toolId = encodeURI(toolId);
				$.ajax({
					url : appUrl,
					type : "POST",
					data : ({
						j_username : userid,
						j_password : password
					}),
					async : false,
					success : function(msg) {
						window.open(funcUrl);
					},
					error : function(msg) {
						alert('Login fail' + msg);
					}
				});

			});
		} else {
			window.open(funcUrl);
		}
	}
}
function openCustomerFinance(customerCode) {
	window.open("getEncryptMessage.action?custCode=" + customerCode);
}
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function submitDisplaytagFormInWindow(formname, fields, windowId) {
	var objfrm = document.forms[formname];
	for (j = fields.length - 1; j >= 0; j--) {
		if (fields[j].v != ''){
			$(objfrm).append("<input type='hidden' name='" + fields[j].f + "' value='" + fields[j].v + "'/>");
		}
	}
	submitFormInWindow(objfrm, windowId);
}
function displaytagform(formname, fields, windowId) {
	var objfrm = document.forms[formname];
	var fld = new Array();
	var fldIdx = 0;
	for (j = fields.length - 1; j >= 0; j--) {
		if (fields[j].v != '') {
			var ipt = document.createElement("INPUT");
			ipt.setAttribute("type", "hidden");
			ipt.setAttribute("name", fields[j].f);
			ipt.setAttribute("value", fields[j].v);
			objfrm.appendChild(ipt);
			fld[fldIdx++] = ipt;
		}
	}

	// for user lookup function(boc/common/lookup/lookup.jsp)
	if (objfrm.id == "lookupform" && !windowId) {
		windowId = "lookupArea";
	}
	if (windowId) {
		submitFormInWindow(objfrm, windowId);
		for (j = fld.length - 1; j >= 0; j--){
			$(fld[j]).remove();
		}
	} else {
		objfrm.submit();
	}
	for (j = fields.length - 1; j >= 0; j--) {
		$("input[name="+fields[j].f+"]").remove();
	}
}