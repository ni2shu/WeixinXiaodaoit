!(function($) {
	var ctxurl = "";
	function parseUrlParams(string) {
		if (!string || !string.length) {
			return "";
		}
		string = string.replace(/&amp;/g, "&");
		string = string.replace(/\n/g, "\\n");
		return string.replace(/&([^=&]+)=([^=&]+)/g, function(match) {
			return match.replace(
					/[^=&\u4E00-\u9FA5\u3000-\u303F\uFF00-\uFFEF\s]+/g,
					function(match) {
						return encodeURIComponent(decodeURIComponent(match));
					});
		});
	}

	function buildAsyncUrl(page, listener, params, partids) {
		if (!page) {
			return null;
		}
		if (!listener) {
			return null;
		}
		var url = ctxurl + "?service=ajaxDirect/1/" + page + "/" + page
				+ "/javascript/" + partids;
		url += "&pagename=" + page;
		url += "&eventname=" + listener;
		if (params != null && params != "") {
			url += "&" + parseUrlParams(params);
		}
		return url = url + "&partids=" + partids;
	}

	function handleResponse(data, partId, afterFn) {
		// console.log(">>>start parse response");
		var json;
		$(data).find("parts").each(function(i) {
			var text = $(this).children("DATASETDATA").text();
			if (text) {
				json = eval(text);
			}
			var part = $(this).children("part").text();
			if (part) {
				partElement = document.getElementById(partId);
				if (partElement) {
					partElement.innerHTML = part;
				}
			}
		});
		// console.log(">>>result json:" + json);
		// console.log(">>>end parse response");
		if (typeof (afterFn) == "function") {
			// console.log(">>>start excute after ajax function");
			afterFn.call(this, json);
			// console.log(">>>end excute after ajax function");
		}
	}

	$.extend({
		ajaxDirect : function(params) {
			var requrl = buildAsyncUrl(params.page, params.listener,
					params.param, params.partId);
			requrl = requrl + "&ajaxSubmitType=get";
			requrl += "&ajax_randomcode=" + Math.random();
			// console.log(">>>request url:"+requrl);
			params.url = requrl;
			if (!params.dataType) {
				params.dataType = "xml";
			}
			params.success = function(data, textStatus, jqXHR) {
				// console.log(">>>recived response:" + data);
				handleResponse(data, params.partId, params.afterFn);
			};
			$.ajax(params);
			return true;
		},
		ajaxSubmit : function(params) {
			var requrl = buildAsyncUrl(params.page, params.listener,
					params.param, params.partId);
			requrl = requrl + "&ajaxSubmitType=post";
			requrl += "&ajax_randomcode=" + Math.random();
			params.url = requrl;
			// console.log(">>>request url:"+requrl);
			var formParam = $("#" + params.formId).serialize();
			// console.log(">>>formParam"+formParam);
			params.data = formParam;
			if (!params.dataType) {
				params.dataType = "xml";
			}
			params.success = function(data, textStatus, jqXHR) {
				// console.log(">>>recived response:" + data);
				handleResponse(data, params.partId, params.afterFn);
			};
			params.type = "POST";
			$.ajax(params);
			return true;
		},
		ajaxGet : function(page, listener, params, partids, afterFn) {
			var requrl = buildAsyncUrl(page, listener, params, partids);
			requrl = requrl + "&ajaxSubmitType=get";
			requrl += "&ajax_randomcode=" + Math.random();
			// console.log(">>>request url:"+requrl);
			$.ajax({
				url : requrl,
				dataType : "xml",
				success : function(data, textStatus, jqXHR) {
					// console.log(">>>recived response:" + data);
					handleResponse(data, partids, afterFn);
				}
			});
			return true;
		},

		ajaxPost : function(page, listener, params, partids, formId, afterFn) {
			var requrl = buildAsyncUrl(page, listener, params, partids);
			requrl = requrl + "&ajaxSubmitType=post";
			requrl += "&ajax_randomcode=" + Math.random();
			// console.log(">>>request url:"+requrl);
			var formParam = $("#" + formId).serialize();
			// console.log(">>>formParam"+formParam);
			$.ajax({
				type : "POST",
				url : requrl,
				data : formParam,
				dataType : "xml",
				success : function(data, textStatus, jqXHR) {
					// console.log(">>>recived response:" + data);
					handleResponse(data, partids, afterFn);
				}
			});
			return true;
		}
	});
})(jQuery);