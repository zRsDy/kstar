var defaultPopUpAttrs = {
    height : 160,
    width : 300,
    show : "clip",
    hide : "clip",
    resizable : false,
    closeOnEscape : true,
    modal : true,
    close : function() {
        $(this).html("");
    },
    autoOpen : false
};

$(document).ready(function() {

});

// 
var winStack = new Array();

function openNewWin(url, attributes, containerId, iframeAttr) {
    var id = "newWin_" + new Date().getTime();
    if (containerId && containerId != null) {
        $("#" + containerId).append("<div id=\"" + id + "\"></div>");
    } else {
        $("#maxWindowBody").append("<div id=\"" + id + "\"></div>");
    }
    $("#" + id).dialog(defaultPopUpAttrs);
    if (attributes) {
        $("#" + id).dialog(attributes);
    }
    $("#" + id).dialog({
                open : function() {
                    winStack.push(id);
                    if (iframeAttr != null) {
                        $("#" + id).append("<iframe id=\"" + id + "_iframe\" width=\"" + iframeAttr.width + "\" height=\"" + iframeAttr.height + "\" frameborder=\"" + iframeAttr.border + "\"></ifrme>");
                    } else {
                        $("#" + id).append("<iframe id=\"" + id + "_iframe\" width=\"100%\" height=\"100%\" frameborder=\"0\"></ifrme>");
                    }
                },
                close : function() {
                    $("#" + id).remove();
                    for ( var i = 0; i < winStack.length; i++) {
                        if (winStack[i] == id) {
                            for ( var ii = i; ii < winStack.length - 1; ii++) {
                                winStack[i] = winStack[i + 1];
                            }
                            winStack.length = winStack.length - 1;
                            break;
                        }
                    }
                },
                beforeClose : function() {
                    $("#" + id + "_iframe").removeAttr("src");
                }
            });
    $("#" + id).dialog("open");
    setTimeout(function() {
        $("#" + id + "_iframe").attr("src", url);
    }, 400);
}

function closeNewWin() {
    for ( var i = winStack.length - 1; i >= 0; i--) {
        if ($(winStack[i]).dialog("isOpen")) {
            $("#" + winStack.pop()).dialog("close");
            break;
        }
    }
}

function changeNewWinTitle(newTitle) {
    $("#" + winStack[winStack.length - 1]).dialog({
        title : newTitle
    });
}

function popupAlert(message, attributes, containerId, callbackMethod) {
    if (containerId && containerId != null) {
        $("#" + containerId).append("<div id=\"alertMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    } else {
        $("#maxWindowBody").append("<div id=\"alertMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    }
    $("#alertMessage").dialog(defaultPopUpAttrs);
    if (attributes && attributes != null && attributes != {}) {
        $("#alertMessage").dialog(attributes);
    }
    $("#alertMessage").dialog({
        title : "警告",
        open : function() {
            $("#alertMessage :first-child").append("<h5 class=\"error\"><p>" + message + "</p></h5>");
        },
        buttons : {
            "确定" : function() {
                $("#alertMessage").dialog("close");
            }
        },
        close : function() {
            $("#alertMessage").remove();
            if (callbackMethod != null) {
                callbackMethod();
            }
        }
    });
    $("#alertMessage").dialog("open");
}

function popupInfo(message, attributes, containerId, callbackMethod) {
    if (containerId && containerId != null) {
        $("#" + containerId).append("<div id=\"infoMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    } else {
        $("#maxWindowBody").append("<div id=\"infoMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    }
    $("#infoMessage").dialog(defaultPopUpAttrs);
    if (attributes && attributes != null && attributes != {}) {
        $("#infoMessage").dialog(attributes);
    }
    $("#infoMessage").dialog({
        title : "信息",
        open : function() {
            $("#infoMessage :first-child").append("<h5 class=\"success\"><p>" + message + "</p></h5>");
        },
        buttons : {
            "确定" : function() {
                $("#infoMessage").dialog("close");
            }
        },
        close : function() {
            $("#infoMessage").remove();
            if (callbackMethod != null) {
                callbackMethod();
            }
        }
    });
    $("#infoMessage").dialog("open");
}

function popupConfirm(message, callbackMethod, attributes, containerId) {
    if (containerId && containerId != null) {
        $("#" + containerId).append("<div id=\"confirmMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    } else {
        $("#maxWindowBody").append("<div id=\"confirmMessage\" style=\"border:1px solid #ccc; margin-top:5px; margin-left:5px; margin-right:4px; width:100%; height:100%; text-align:center;\"><div class=\"tip_box\"></div></div>");
    }
    $("#confirmMessage").dialog(defaultPopUpAttrs);
    if (attributes && attributes != null && attributes != {}) {
        $("#confirmMessage").dialog(attributes);
    }
    $("#confirmMessage").dialog({
        title : "确认",
        open : function() {
            $("#confirmMessage :first-child").append("<h5 class=\"confirmation\">" + message + "</h5>");
        },
        buttons : {
            "确定" : function() {
                $("#confirmMessage").dialog("close");
                callbackMethod();
            },
            "取消" : function() {
                $("#confirmMessage").dialog("close").addclass("btn_cancel");
            }
        },
        close : function() {
            $("#confirmMessage").remove();
        }
    });
    $("#confirmMessage").dialog("open");
}

function getMessage(key, array) {
    if (key.indexOf(",") > 0) {
        var newAarray = key.split(",");
        var argus = new Array();
        for (var i = 1; i < newAarray.length; i++) {
            argus[i - 1] = newAarray[i];
        }
        return getMessage(newAarray[0], argus);
    }
    var message = allPICCMessage[key];
    if (message == null) {
        message = allPICCMessage["NOMSGFOUND"];
    }
    if (message != null && array != null) {
        for (var i = 0; i < array.length; i++) {
            message = message.replace("{" + i + "}", array[i]);
        }
    }

    return message;
}

(function($) {

	var zIndex = 100;

// 消息框
var $message;
var messageTimer;
$.message = function() {
	var message = {};
	if ($.isPlainObject(arguments[0])) {
		message = arguments[0];
	} else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
		message.type = arguments[0];
		message.content = arguments[1];
	} else {
		return false;
	}
	
	if (message.type == null || message.content == null) {
		return false;
	}
	
	if ($message == null) {
		$message = $('<div class="xxMessage"><div class="messageContent message' + message.type + 'Icon"><\/div><\/div>');
		if (!window.XMLHttpRequest) {
			$message.append('<iframe class="messageIframe"><\/iframe>');
		}
		$message.appendTo("body");
	}
	
	$message.children("div").removeClass("messagewarnIcon messageerrorIcon messagesuccessIcon").addClass("message" + message.type + "Icon").html(message.content);
	$message.css({ "z-index": zIndex ++}).show();
	
	clearTimeout(messageTimer);
	messageTimer = setTimeout(function() {
		$message.hide();
	}, 4000);
	return $message;
};})(jQuery);
