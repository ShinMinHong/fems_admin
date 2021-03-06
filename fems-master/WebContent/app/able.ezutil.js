/**
 * Ablecoms ezutil js library
 *
 * Helper 및 공통 초기화 담당
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */


(function(w) {

	// 20190408 정경식 추가
	// IE에서 URLSearchParams을 사용하기 위해 미리 생성함.
    w.URLSearchParams = w.URLSearchParams || function (searchString) {
        var self = this;
        self.searchString = searchString;
        self.get = function (name) {
            var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(self.searchString);
            if (results == null) {
                return null;
            }
            else {
                return decodeURI(results[1]) || 0;
            }
        };
    }
})(window);


(function() {
	/** console unsupported browser fallback */
	if(_.isUndefined(window['console'])) {
		console = {
			error: function() {},
			warn: function() {},
			info: function() {},
			log: function() {},
			debug: function() {},
			assert: function() {},
			table: function() {}
		};
	}
	if(!_.has(console, "table")) { console.table = console.log; }

	/** create global message function */
	if(_.isUndefined(window['ablemessage'])) {
		messagemap = window["messagemap"] || {};
		ablemessage = function(code) {
			var lowerCode = (code || "").toLowerCase();
			if(_.has(messagemap, lowerCode)) {
				return messagemap[lowerCode];
			}
			return code;
		};
	}

	/*****************************************************************************
	 * ezUtil - 일반 Helpher Utility
	 *****************************************************************************/
	var EzUtil = function () {
		var self = this;
		self.clazz = "EzUtil";

		var _default_msg = {
			comm_loadindicator: "처리중입니다.<br/>잠시만 기다려 주세요...",
			comm_ajaxerror: "서버와의 통신도중 오류가 발생했습니다.\n잠시후 다시 시도해주세요.\n오류가 지속되면 관리자에게 문의 부탁드립니다.",
			comm_ajaxerrordetail: "[오류정보]\n -주소: %s\n -응답코드: %s (%s)",
			comm_loginrequired: "로그인이 필요합니다.",
			comm_dberror: "데이터베이스 오류.\n오류가 지속되면 관리자에게 문의 부탁드립니다."
		};

		var _msg;
		try{
			_msg = msg;
			if(_.size(_msg) == 0) {
				_msg = _default_msg;
			}
		} catch (e) {
			_msg = _default_msg;
		}

		//메시지 지역화: msg배열에서 메시지코드 치환
		self.lang = function(code) {
			if(_.has(_msg, code)) {
				return _msg[code];
			} else {
				return 'unknown message: ' + code;
			}
		};

		var _MESSAGES = {
				//"처리중입니다. 잠시만 기다려 주세요...",
				loadingIndicatorMessage : self.lang("comm_loadindicator").replace(/\\n/gi, "\n"),
				//"서버와의 통신도중 오류가 발생했습니다.\n잠시후 다시 시도해주세요.\n오류가 지속되면 관리자에게 문의 부탁드립니다.",
				ajaxErrorMessage: self.lang("comm_ajaxerror").replace(/\\n/gi, "\n"),
				//"[오류정보]\n -주소: %s\n -응답코드: %s (%s)"
				ajaxErrorDetailMessage: self.lang("comm_ajaxerrordetail").replace(/\\n/gi, "\n")
			};

		/*************************************************************************
		 * 전역 초기화
		 */

		self.ezInitialized = false;

		/** 기본 초기화 작업 수행. 모든페이지에서 사용 */
		self.ezInitialize = function(blockUsingPageAjaxIndicator) {
			if ( self.ezInitialized ){
				return false;
			}
			self.ezInitialized = true;
			//Moment init
			self.ezInitializeMoment();

			// 모바일에서 접속시 사이드바(sidebar-xs) 접기
			self.initSibebar();

			//Underscore String init
			self.ezInitializeUnderscoreString();

			//jQuery validate init
			self.ezInitializeJQueryValidate();

			//jQuery numeric init
			self.ezInitializeNumericInput();

			//turn off Autofill
			self.suppressAutofill();

			//Ajax 전역 설정
			self.ezInitializeAjaxHelpher();

			//Ajax 전역 Indicator
			if(blockUsingPageAjaxIndicator !== true) {
				self.ezRegisterPageAjaxIndicator();
			}
		};

		/** Moment 초기화 */
		self.ezInitializeMoment = function() {
			if(moment === undefined) {
				console.log('moment.js are not included. ezUtil skip ezInitializeMoment()');
				return false;
			}
			moment.locale("ko");
		}

		// 모바일에서 접속시 사이드바(sidebar-xs) 접기
		self.initSibebar = function(){
			var UserAgent = navigator.userAgent;
			if (UserAgent.match(/iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i) != null || UserAgent.match(/LG|SAMSUNG|Samsung/) != null) {
				$('body').toggleClass('sidebar-xs');
			}
		}

		/** Unserscore.string 초기화 */
		self.ezInitializeUnderscoreString = function() {
			if(_.string === undefined && s === undefined) {
				console.log('Underscore.string are not included. ezUtil skip ezInitializeUnderscoreString()');
				return false;
			}
			//Unserscore String을 Unserscore에 mixin
			if(!_.isUndefined(_.str)) {
				_.mixin(_.str.exports()); //Unserscore String Old Version
			} else {
				_.string = s;
				_.mixin(s.exports()); //Unserscore String Newer Version
			}
		};

		/** jQuery validate 초기화 */
		self.ezInitializeJQueryValidate = function() {
			if($.validator === undefined) {
				console.log('Underscore.string are not included. ezUtil skip ezInitializeUnderscoreString()');
				return false;
			}

			/** Global Setting */
			$.validator.setDefaults({
				ignore: [],
				onsubmit: false, onfocusout: false, onkeyup: false, onclick: false, focusInvalid: true, focusCleanup: false,
				//errorClass: "error", validClass: "valid", errorElement: "em", wrapper: "li", errorElement: "label"
				showErrors: vmUtil.createShowErrorFunctionWithLabelMap(),
				errorPlacement: function(error, element) {
					//error.insertAfter(element);
					//alert(error.html());
				}
			});
		};

		/** jQuery Numeric 연관 Input 초기화 */
		self.ezInitializeNumericInput = function() {
			if($.fn.numeric === undefined) {
				console.log('jquery.numeric are not included. ezUtil skip ezInitializeNumericInput()');
				return false;
			}
			$(".numeric").numeric();
			$(".integer").numeric(false, function() { this.value = ""; this.focus(); });
			$(".positive-numeric").numeric({ negative: false }, function() { this.value = ""; this.focus(); });
			$(".positive-integer").numeric({ decimal: false, negative: false }, function() { this.value = ""; this.focus(); });
		};

		/** 페이지 자동 Ajax Indicator 등록 */
		self.ezRegisterPageAjaxIndicator = function() {
			if($.blockUI === undefined) {
				console.log('jquery.blockUI are not included. ezUtil skip ezRegisterPageAjaxIndicator()');
				return false;
			}
			//jQuery Event 발생 순서 - http://api.jquery.com/Ajax_Events/
			//ajaxStart()
			//ajaxSend(event, jqXHR, ajaxOptions)
			//ajaxSuccess(event, XMLHttpRequest, ajaxOptions) 또는 .ajaxError(event, jqXHR, ajaxSettings, thrownError)
			//ajaxComplete(event, XMLHttpRequest, ajaxOptions)
			//ajaxStop()
			$(document).ajaxStart(function() { self.block(); });
			$(document).ajaxError(function() {
				self.unblock();
			});
			$(document).ajaxSuccess(function() { self.unblock(); });
			$(document).ajaxComplete(function(event, XMLHttpRequest, ajaxOptions) { self.unblock(); /*self.initLoginDurationTimer();*/ });
			$(document).ajaxStop(function() { self.unblock(); });
		};

		/** Ajax관련 전역 초기화 */
		self.ezInitializeAjaxHelpher = function() {
			$.ajaxSetup({
				type: "POST",
				dataType: "json",
				cache: false,
				//contentType: "application/x-www-form-urlencoded",
				contentType: "application/json",
				traditional: true,
				processData: false, //true이면 application/x-www-form-urlencoded을 위해 data를 key=value로 인코딩
				beforeSend: function(jqXHR,options) {
			        if (options.contentType == "application/json" && typeof options.data != "string") {
			            options.data = JSON.stringify(options.data);
			        }
			    }
				// Data converters: Keys separate source (or catchall "*") and destination types with a single space
				/* converters: {
					// Convert anything to text
					"* text": String,
					// Text to html (true = no transformation)
					"text html": true,
					// Evaluate text as a json expression
					"text json": function(data) {
						data = data.replace(/^(\ufeff)+/gi, ""); //Remove BOM at Response
						return jQuery.parseJSON(data);
					},
					// Parse text as xml
					"text xml": jQuery.parseXML
				} */
			});

			$(document).ajaxError(self._onAjaxErrorGlobalHandler);
		};

		/** onAjaxError 전역 핸들러 */
		self._onAjaxErrorGlobalHandler = function(event, jqXHR, ajaxSettings, thrownError) {
			//If either of these are true, then it's not a true error and we don't care
		    if (jqXHR.status === 0 || jqXHR.readyState === 0) {
		    	console.log("_onAjaxErrorGlobalHandler ==> ignored - jqXHR.status === 0 || jqXHR.readyState === 0");
		        return;
		    }

		    console.log("_onAjaxErrorGlobalHandler ==> ", event, jqXHR, ajaxSettings, thrownError);
			var defaultMessage = _MESSAGES.ajaxErrorMessage;
			try {
				var url = ajaxSettings.url;
				var errorMessage = _.sprintf(_MESSAGES.ajaxErrorDetailMessage, url, jqXHR.status, jqXHR.statusText);
				//alert(defaultMessage + "\n\n" + errorMessage);
			} catch (e) {
				alert(defaultMessage);
				console.log("_onAjaxErrorGlobalHandler ERROR ==> ", e);
			};
		};


		/** 자동 완성 방지 */
		self.suppressAutofill = function() {
			//chrome autocomplete 방지
			_.defer(function() {
				setTimeout(function(){
					$("input[data-id]").each(function(idx, element) {
						$(this).attr("id", $(this).attr("data-id"));
					});
					$("input[data-name]").each(function(idx, element) {
						$(this).attr("name", $(this).attr("data-name"));
					});
					$("input[name]:not([id])").each(function(idx, element) {
						$(this).attr("id", $(this).attr("name"));
					});
				}, 10);
			});
		};

		/*************************************************************************
		 * Loading Indicator Helpher - jQuery BlockUI Plugin 사용
		 *
		 * [사용방법]
		 *
		 * 		ezUtil.block(): 페이지 Block
		 * 	 	ezUtil.unblock(): 페이지 UnBlock
		 * 		ezUtil.block("#id"): 특정 Element Block
		 * 		ezUtil.unblock("#id"): 특정 Element Block
		 * 		ezUtil.unblockAll(): Block 했던 모든것 UnBlock
		 */
		/** Indicator용 메지지 포맷팅 */
		self.formatIndicatorMessage = function(message) {
			//고객사에서 박스없이 이미지만 표시 요청
			var src = (contextPath||"") + "/lib/blockui/busy5.gif";
			return _.sprintf("<div><img src='%s' style='width:48px; height:48px'/><div>", src);
		};

		/** true이면 화면의 Block을 UnBlock할수 없게 함 */
		self.isLockBlock = false;
		self.lockBlock = function() {
			self.isLockBlock=true;
		};
		self.releaseBlock = function() {
			self.isLockBlock=false;
		};

		/** 특정 Element 또는 Page전체를 Block */
		self.block = function(jQuerySelector, message) {
			if(self.isLockBlock) return;
			var config = {
				message : self.formatIndicatorMessage(message),
				css: {
	                width: '100%', left: 0, border: 'none', padding: '5px',
	                backgroundColor: 'transparent', opacity: .4
	            }
			};
			if (_.isEmpty(jQuerySelector)) {
				$.blockUI(config);
			} else {
				$(jQuerySelector).block(config);
			}
			self._blockedList.push(jQuerySelector);
		};

		/** 특정 Element 또는 Page전체를 UnBlock */
		self.unblock = function(jQuerySelector) {
			if(self.isLockBlock) return;
			if (_.isEmpty(jQuerySelector)) {
				$.unblockUI();
			} else {
				$(jQuerySelector).unblock();
			}
			self._blockedList = _.filter(self._blockedList, function(item){ return item != jQuerySelector; });
		};

		/** 내부적인 blockList관리 - unblockAll로 모두 해제시 사용 */
		self._blockedList = [];

		/** Block 했던것을 모두 Unblock */
		self.unblockAll = function() {
			if(!_.isEmpty(self._blockedList)){
				_.each(self._blockedList, self.unblock);
				self.unblock(null);
				self._blockedList = [];
			}
		};

		/*************************************************************************
		 * Common Util
		 */

		/**
		 * 객체배열의 특정 프로퍼티를 키로 맵을 구성
		 * listToMap( [{key:"a1", b:1}, {key:"a2", b:2}], "a" ) => { a1: {key:"a1", b:1}, a2: {key:"a2", b:2} }
		 */
		self.listToMap = function(list, keyField) {
			return _.reduce(list, function(result, item) { result[item[keyField]] = item; return result; }, {});
		};

		/** 첨부 이미지를 팝업 */
		self.popUpAttachedImage = function(url, target/*default: _blank*/) {
			window.open(url, target || "_blank", "width=400, height=400, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no" );
		};
		/** 첨부 파일을 팝업 */
		self.popUpAttachedFile = function(url, target/*default: _blank*/) {
			window.open(url, target || "_blank", "width=400, height=400, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no" );
		};
		/** 첨부 파일은 다운로드 (GET) */
		self.downloadAttachedFile = function(url, queryParamObject) {
			queryParamObject = _.defaults(queryParamObject||{}, { forceDownload: true });
			var urlWithParam = $.param.querystring(url, queryParamObject);
			$.fileDownload(urlWithParam);
		};
		/** excel파일 다운로드 */
		self.downloadExcelFile = function(url, data) {
			var options = {
				httpMethod: "POST",
				data: data
			};
			$.fileDownload(url, options);
		};

		/** 현재의 QueryString을 Json으로 획득 */
		self.deparamQueryString = function() {
			return _.omit($.deparam.querystring(), ["lang"]);
		};


		/*************************************************************************
		 * Communication Util
		 */

		/**
		 *  message의 formmat처리
		 *   __FIELD_NAME__: 필드명으로 변환. fieldNameMap이용, 메시지코드로 변환시도
		 */
		self.formatFieldErrorMessage = function(message, field, fieldNameMap/* { field : "fieldName Or msgCodeID", ... } */, fieldMsgCodePrefix) {
			var fieldName = self.resolveFieldName(field, fieldNameMap, fieldMsgCodePrefix);
			return message.replace("__FIELD_NAME__", fieldName);
		};
		/** 필드명 획득. 메시지 변환처리 */
		self.resolveFieldName = function(field, fieldNameMap/* { field : "fieldName Or msgCodeID", ... } */, fieldMsgCodePrefix) {
			fieldMsgCodePrefix = fieldMsgCodePrefix || "com.col."; //convention
			field = field || "";

			var fieldName = field;

			//1. fieldNameMapper를 이용
			fieldNameMap = fieldNameMap || {};
			if(!_.isEmpty(fieldNameMap) && _.has(fieldNameMap, field)) {
				fieldName = fieldNameMap[fieldName];
				return ablemessage(fieldName);
			}

			//2. field를 이용
			if (!_.isEmpty(fieldMsgCodePrefix)) {
				fieldName = fieldMsgCodePrefix + fieldName;
			}

			if (!_.isBlank($('label[for='+field+']').text())){
				return $('label[for='+field+']').text();
			}

			return ablemessage(fieldName);
		};

		/** REST API 공통응답 처리 - return: response.isSuccess 성공인지 실패인지 반환 */
		self.checkRESTResponseMsg = function(response,
				fieldCtrlMap /* { field : "inputCtrlName", ... } */,
				fieldNameMap /* { field : "fieldName Or msgCodeID", ... } */) {
			console.log("checkRESTResponseMsg ==> ", response);
			if(_.isString(response)) {
				response = JSON.parse(response);
			}

			if(_.isUndefined(response.status)) {
				return false;
			}

			// 1. 공통 메시지 처리 -> falldown
			if (!_.isEmpty(response.msg)) {
				alert(response.msg);
			}

			// 2. 공통 콜백 명령 처리 -> falldown
			if (!_.isEmpty(response.cbCmd)) {
				switch (response.cbCmd.toLowerCase()) {
				case 'loginrequired':
					//로그인 시도 검토
					document.location.href = contextPath + "/login";
					break;
				case 'redirect':
					document.location.href = response.cbParam;
					break;
				default:
					break;
				}
			}

			// 3. Validation Error 메시지 처리
			if (!_.isEmpty(response.fieldError)) {
				//show multiple message
				var errorMessage =
					_.chain(response.fieldError)
					.map(function(error) {
						return self.formatFieldErrorMessage(error.message, error.field, fieldNameMap);
					})
					.value().join("\r\n");
				if(!_.isEmpty(errorMessage)) {
					alert(errorMessage);
					response.message = null;
				}
				//focus 처리
				var fieldToFocus = _.chain(response.fieldError).map(function(error) { return error.field; }).first().value();
				if(!_.isEmpty(fieldToFocus)) {
					fieldCtrlMap = fieldCtrlMap || {};
					if(!_.isEmpty(fieldCtrlMap) && _.has(fieldCtrlMap, fieldToFocus)) {
						fieldToFocus = fieldCtrlMap[fieldToFocus];
					}
					var targetCtrl = $("#"+fieldToFocus);
					targetCtrl.focus().select();
				}
			}

			_.defer(function() {
				self.unblock();
			});

			return response.status == "SUCCESS";
		};

		//Admin 페이지 GNB,LNB 현재 페이지 활성화
		self.setAdminGnbLnbActivate = function(gnbIndexOrSelector,lnbIndexOrSelector) {
			if( _.isNumber(gnbIndexOrSelector)) {
				$(".gnb_box .gnb li:eq(" + gnbIndexOrSelector + ")").addClass("on");
			} else if(_.isString(gnbIndexOrSelector)) {
				$(".gnb_box .gnb").find(gnbIndexOrSelector).addClass("on");
			}

			if( _.isNumber(lnbIndexOrSelector)) {
				$("#lnb ul li:eq("+ lnbIndexOrSelector +")").addClass("on");
			} else if( _.isString(lnbIndexOrSelector)) {
				$("#lnb ul").find(lnbIndexOrSelector).addClass("on");
			}
		};

		/** selectOption text값으로 정렬 */
		self.orderBySelectOptionsText= function(SelectOptions, desc){
			if ( desc === true ){
				return _.sortBy(SelectOptions, 'text').reverse();
			}
			return _.sortBy(SelectOptions, 'text');
		};

		/** selectOption value값으로 정렬 */
		self.orderBySelectOptionsValue= function(SelectOptions, desc){
			if ( desc === true ){
				return _.sortBy(SelectOptions, 'value').reverse();
			}
			return _.sortBy(SelectOptions, 'value');
		};

		self.orderByYnSelectOptions = function(SelectOptions){
			 return _.sortBy(SelectOptions, function(dataRow){
					var gubunRank = {
							 "" : 1,
							 "Y" : 2,
							 "N" : 3
					}
					return gubunRank[dataRow.value];
				})
		};

		self.orderByTfSelectOptions = function(SelectOptions){
			 return _.sortBy(SelectOptions, function(dataRow){
					var gubunRank = {
							 "" : 0,
							 true : 1,
							 false : 2
					}
					return gubunRank[dataRow.value];
				})
		};

		self.orderByAuthorGroupSelectOptions = function(SelectOptions){
			 return _.sortBy(SelectOptions, function(dataRow){
					var gubunRank = {
							"" : 0, "HQ_ADMIN": 1, "AREA_ADMIN": 2, "MARKET_ADMIN": 3
					}
					return gubunRank[dataRow.value];
				})
		};

		/**
		 * selectOption value값으로 text값 추출
		 * @param selectOptions [{value:'E1', text:'처리중오류로기존카드롤백'}, {value:'R2', text:'기존카드삭제(카드등록)'}, ..... ]
		 * @param value 'E1'
		 * @return text '처리중오류로기존카드롤백'
		 * @param notMatchBaseValue 찾으려는 object가 존재하지 않을 경우 return 할 기본값
		 * */
		self.getSelectOptionsText = function(selectOptions, value, notMatchBaseValue) {
			if (_.contains(_.pluck(selectOptions, "value"), value)){
				return _.find(selectOptions, function(dataRows) { return dataRows.value == value;  })["text"];
			} else if ( notMatchBaseValue != null ){
				return notMatchBaseValue;
			}
			return "";
		};

		/**
		 * Object 에서 SelectOptions 추출
		 * @param obj { C1: "카드변경", ... }
		 * @param useAllOptions ALL을 추가할지 여부 (true, false)
		 * @return [ {text:'카드변경', value:'C1'}, {...}, ..... ]
		 * */
		self.convertObjectToSelectOptions = function(obj, useAllOptions) {
			var options = _.map(obj, function(value, key) {
				return {text:value, value:key}; });
			if( useAllOptions ) {
				//전체 Options 추가
				options = self.addAllOptions(options);
			}
			return options;
		};


		self.convertBooleanObjectToSelectOptions = function(obj, useAllOptions) {
			var options = _.map(obj, function(value, key) {
			 	if((key==='true'||'false') &&  (key!= ('Y') && key !=('N')) ){
			 	  	key = (key == 'true')
			 	  }
				return {text:value, value:key}; });
			if( useAllOptions ) {
				//전체 Options 추가
				options = self.addAllOptions(options);
			}
			return options;
		};

		/**
		 * SelectOptions 에서 Object  추출
		 * */
		self.convertSelectOptionsToObject = function(selectOptions, useAllOptions) {
//			var options = _.map(obj, function(value, key) {
//				return {text:value, value:key}; });
//			if( useAllOptions ) {
//				//전체 Options 추가
//				options = self.addAllOptions(options);
//			}
//			return options;
			return _.object(_.pluck(selectOptions, 'value'), _.pluck(selectOptions, 'text'));
		};


		/**
		 * 전체 Options를 추가
		 * @param obj [{text:'', value:''}, ....]
		 * @return [{text:'ALL', value:''}, {value:'',text:''}, ..... ]
		 * */
		self.addAllOptions = function(obj){
			obj.unshift({'text': '전체', 'value': ''});
			return obj;
		};

		/**
		 * Moment Formatter. (기본형식: 'YYYY-MM-DD HH:mm:ss')
		 */
		self.momentFormatter = function(value, momentFormatString, momentParseFormatString) {
			var momentFormatString = momentFormatString || 'YYYY-MM-DD HH:mm:ss';
			var momentParseFormatString = momentParseFormatString || momentFormatString;

			if(_.isBlank(value)) {
				return value;
			}
			return moment(value, momentParseFormatString).format(momentFormatString);
		};

		// 사용안함 - 2019.04.09 rodem4
		// URLSearchParams은 모든 브라우저에서 사용가능하지만 IE에서는 사용안됨.
		// IE에서 사용하기 위해 최상단에 URLSearchParams를 정의해 줌.
		self.getParamValues = function(key){
			return new URLSearchParams(location.search).get(key);
		};

		self.detectIEAndEdg = function () {
			var ua = window.navigator.userAgent;

            var msie = ua.indexOf('MSIE ');
            if (msie > 0) {
                // IE 10 or older => return version number
                return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
            }

            var trident = ua.indexOf('Trident/');
            if (trident > 0) {
                // IE 11 => return version number
                var rv = ua.indexOf('rv:');
                return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
            }

            var edge = ua.indexOf('Edge/');
            if (edge > 0) {
                // Edge => return version number
                return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
            }

            // other browser
            return false;
		}

		self.detectMobile = function() {
			if( navigator.userAgent.match(/Android/i) || navigator.userAgent.match(/webOS/i)
				 || navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPad/i) || (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1)
				 || navigator.userAgent.match(/iPod/i) || navigator.userAgent.match(/BlackBerry/i)
				 || navigator.userAgent.match(/Windows Phone/i) ) {
				return true;
			} else {
				return false;
			}
		}

		/* 비밀번호 유효성 체크 영문+숫자+특수문자 최소 하나씩 포함 8~20자 */
		self.isPasswordCheck = function (pwd){
			var regexp0 = /[0-9a-zA-Z\\/?,.<>;:'"\[\]{}|+=_`~!@#$%^&*()-]{8,20}$/;
			var regexp1 = /[\\/?,.<>;:'"\[\]{}|+=_`~!@#$%^&*()-]/;
		    var regexp2 = /[0-9]/;
		    var regexp3 = /[a-zA-Z]/;
		    var regexp5 = /(\w)\1\1\1/; //같은문자 3번까지

		    if(!regexp0.test(pwd)){
		    	return false;
		    }
		    if(!regexp1.test(pwd)){
		    	return false;
		    }
		    if(!regexp2.test(pwd)){
		    	return false;
		    }
		    if(!regexp3.test(pwd)){
		    	return false;
		    }

		    /*if(regexp5.test(pwd)){
		        alert('비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.');
		        return false;
		    }*/

		    return true;
		};

		/** 휴대폰번호 하이픈 자동 입력 - input 객체를 입력 */
		self.inputPhoneNumber = function(obj) {
			 var number = obj.value.replace(/[^0-9]/g, "");

			 var tel = "";

			    // 서울 지역번호(02)가 들어오는 경우
			    if(number.substring(0, 2).indexOf('02') == 0) {
			        if(number.length < 3) {
			            return number;
			        } else if(number.length < 6) {
			            tel += number.substr(0, 2);
			            tel += "-";
			            tel += number.substr(2);
			        } else if(number.length < 10) {
			            tel += number.substr(0, 2);
			            tel += "-";
			            tel += number.substr(2, 3);
			            tel += "-";
			            tel += number.substr(5);
			        } else {
			            tel += number.substr(0, 2);
			            tel += "-";
			            tel += number.substr(2, 4);
			            tel += "-";
			            tel += number.substr(6);
			        }

			    // 서울 지역번호(02)가 아닌경우
			    } else {
			        if(number.length < 4) {
			            return number;
			        } else if(number.length < 7) {
			            tel += number.substr(0, 3);
			            tel += "-";
			            tel += number.substr(3);
			        } else if(number.length < 11) {
			            tel += number.substr(0, 3);
			            tel += "-";
			            tel += number.substr(3, 3);
			            tel += "-";
			            tel += number.substr(6);
			        } else {
			            tel += number.substr(0, 3);
			            tel += "-";
			            tel += number.substr(3, 4);
			            tel += "-";
			            tel += number.substr(7);
			        }
			    }
			    obj.value = tel;
		};


		/** 휴대폰번호 하이픈 자동 입력 - Value값을 입력 */
		self.inputPhoneNumberByValue = function(str) {
			if (_.isBlank(str)){
				return str;
			}

		    var number = str.replace(/[^0-9]/g, "");
		    var phone = "";

		    if(number.length < 4) {
		        return number;
		    } else if(number.length < 7) {
		        phone += number.substr(0, 3);
		        phone += "-";
		        phone += number.substr(3);
		    } else if(number.length < 11) {
		        phone += number.substr(0, 3);
		        phone += "-";
		        phone += number.substr(3, 3);
		        phone += "-";
		        phone += number.substr(6);
		    } else {
		        phone += number.substr(0, 3);
		        phone += "-";
		        phone += number.substr(3, 4);
		        phone += "-";
		        phone += number.substr(7);
		    }

		    return phone;
		};

		/* 휴대폰번호 유효성 체크 */
		self.isMobilePhoneCheck = function (num){
			var regexp = /^(010|011|016|017|018|019)-([0-9]{3,4})-([0-9]{4})$/.test(num);
		    return regexp;
		};


		/* 일반전화 유효성 체크 */
		self.isPhoneCheck = function (num){
			var regexp = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|505))-(\d{3,4})-(\d{4})$/.test(num);
		    return regexp;
		};

		self.mobilePhoneSelectOptions = [{text:'010', value:'010'}, {text:'011', value:'011'}, {text:'016', value:'016'}, {text:'017', value:'017'}, {text:'018', value:'018'}, {text:'019', value:'019'}];

		/** 일반전화번호 하이픈 자동 입력 */
		self.inputTelNumber = function(obj) {

		    var number = obj.value.replace(/[^0-9]/g, "");
		    var tel = "";

		    // 서울 지역번호(02)가 들어오는 경우
		    if(number.substring(0, 2).indexOf('02') == 0) {
		        if(number.length < 3) {
		            return number;
		        } else if(number.length < 6) {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2);
		        } else if(number.length < 10) {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2, 3);
		            tel += "-";
		            tel += number.substr(5);
		        } else {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2, 4);
		            tel += "-";
		            tel += number.substr(6, 4);
		        }
		    // 서울 지역번호(02)가 아닌경우
		    } else {
		        if(number.length < 4) {
		            return number;
		        } else if(number.length < 7) {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3);
		        } else if(number.length < 11) {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3, 3);
		            tel += "-";
		            tel += number.substr(6);
		        } else {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3, 4);
		            tel += "-";
		            tel += number.substr(7, 4);
		        }
		    }
		    obj.value = tel;
		};

		/** 휴대폰번호 하이픈 자동 입력 - Value값을 입력 */
		self.inputTelNumberByValue = function(str) {
			if (_.isBlank(str)){
				return str;
			}
		    var number = str.replace(/[^0-9]/g, "");
		    var tel = "";

		    // 서울 지역번호(02)가 들어오는 경우
		    if(number.substring(0, 2).indexOf('02') == 0) {
		        if(number.length < 3) {
		            return number;
		        } else if(number.length < 6) {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2);
		        } else if(number.length < 10) {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2, 3);
		            tel += "-";
		            tel += number.substr(5);
		        } else {
		            tel += number.substr(0, 2);
		            tel += "-";
		            tel += number.substr(2, 4);
		            tel += "-";
		            tel += number.substr(6, 4);
		        }
		    // 서울 지역번호(02)가 아닌경우
		    } else {
		        if(number.length < 4) {
		            return number;
		        } else if(number.length < 7) {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3);
		        } else if(number.length < 11) {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3, 3);
		            tel += "-";
		            tel += number.substr(6);
		        } else {
		            tel += number.substr(0, 3);
		            tel += "-";
		            tel += number.substr(3, 4);
		            tel += "-";
		            tel += number.substr(7, 4);
		        }
		    }
		    return tel;
		};

		// SelectOption value의  해당 Text 값 반환
		// ex) var MARKET= [{text:"백련시장", value: "3"},{text: "인왕시장", value: "2"},{text: "포방터시장", value: "1"}] ,  self.findTextByValue(MARKET,'3') , 결과값:백련시장
		self.findByValue = function(selectOption, value){
		  var result = {text:""};
		  _.map(selectOption, function(row){if(row.value == value){result.text = row.text}})
			return result['text'];
		}

		// Object 의 value 타입이 boolean 혹은 number일떄 string type으로 타입 변환
		// ex . {a:"1",b:true} -> 	{a:"1",b:"true"}
		self.dataTypeToString = function(obj){
			function replacer(key, value) {
				  if (typeof value === "boolean"||typeof value === "number") {
				    return String(value);
				  }
				  return value;
				}
			var dataCloned = _.clone(obj)
			return JSON.parse(JSON.stringify(dataCloned, replacer));
		}

		/* true/false 매핑 */
		self.trueColorChange = {true:  '<font color="red">' + "true" + '</font>', false: 'false'};
		self.ynCodeMap = {true: '사용', false: '미사용'};
		self.smsYnCodeMap = {true: '수신', false: '미수신'};
		self.deleteCodeMap = {true: '삭제', false: '정상'};
		self.fnctngAtCodeMap =  {true: '발생', false: '해제'};
		self.checkSwitchOnCodeMap = {true: 'ON', false: ''};

		self.ynSelectOptions = [{text:'사용', value:'true'}, {text:'미사용', value:'false'}];
		self.smsYnSelectOptions = [{text:'수신', value:'true'}, {text:'미수신', value:'false'}];
		self.deleteYnSelectOptions = [{text:'삭제', value:'true'}, {text:'정상', value:'false'}];
		self.loginYnSelectOptions = [{text:'가능', value:true}, {text:'차단', value:false}];
		self.pblinsttAtYnSelectOptions = [{text:'공공', value:true}, {text:'일반', value:false}];
		self.nrmltAtSelectOptions = [{text:'정상', value:true}, {text:'이상', value:false}];
		self.searchDeleteYnSelectOptions = self.addAllOptions([{text:'삭제', value:'true'}, {text:'정상', value:'false'}]);
		self.searchYnSelectOptions = self.addAllOptions([{text:'사용', value: 'Y'}, {text:'미사용', value:'N'}]);
		self.searchInstalledSelectOptions = self.addAllOptions([{text:'등록', value: 'true'}, {text:'미등록', value:'false'}]);
		self.searchFnctngAtSelectOptions = self.addAllOptions([{text:'발생', value: 'true'}, {text:'해제', value:'false'}]);
		self.searchTureFalseSelectOptions = self.addAllOptions([{text:'true', value:'true'}, {text:'false', value:'false'}]);

		self.getHttpUrlForReportPopup = function(){
			if(location.protocol === "http:"){
				return location.origin;
			} else if(location.protocol === "https:"){
				var httpPort = "";
				if(location.port === "8443") {
					httpPort = ":8080";
				} else if(location.port === "443") {
					httpPort = ":80";
				}
				return "http://" + location.hostname + httpPort;
			}
		}


	};

	ezUtil = new EzUtil();

}(window));

