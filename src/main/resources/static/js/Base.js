
//ajax 全局配置
$.ajaxSetup({
    dataType: "json",
    beforeSend: function(request) {
                request.setRequestHeader("Authorization", getToken());
    cache: false,
    xhrFields:{
        withCredentials: true
    },
    error: function(XMLHttpRequest,textStatus){
      if(XMLHttpRequest.status == "401" || XMLHttpRequest.status == "302"){
    	  //登出
      }
      }
    },
});

 jQuery.fn.bootstrapTable.defaults.onLoadError = function (status, jqXHR) {
        if(status =='401'){
       		 logout();
        }
  }


$.extend({
    "infoMsg": function (msg) { //提示信息框
        $.gritter.add({
            // title: '提示',
            text: '提示：' + msg,
            class_name: 'gritter-info',
            fade_in_speed: "medium",
            fade_out_speed: 1000,
            time: 3000
        });
    },
    "sucMsg": function (msg) { //成功信息框
        $.gritter.add({
            // title: '提示',
            text: '提示：' + msg,
            class_name: 'gritter-success',
            fade_in_speed: "medium",
            fade_out_speed: 1000,
            time: 2000
        });
    },
    "errMsg": function (msg) { //错误信息框
        $.gritter.add({
            // title: '提示',
            text: '提示：' + msg,
            class_name: 'gritter-error',
            fade_in_speed: "medium",
            fade_out_speed: 1000,
            time: 5000
        });
    },
    "warnMsg": function (msg) { //警告信息框
        $.gritter.add({
            // title: '提示',
            text: '提示：' + msg,
            class_name: 'gritter-warning',
            fade_in_speed: "medium",
            fade_out_speed: 1000,
            time: 3000
        });
    }
});

/**
 * 清空输入框
 */
function inputClear() {
    //查找要清空的输入框，属性为data-clear = true
    let inputLabel = $("input[data-clear='true']");
    for (let i = 0; i < inputLabel.length; i++) {
        $(inputLabel[i]).val("");
    }
}

/**
 * 启用输入框输入功能
 */
function inputEnable() {
    let inputLabel = $("input[data-enable='true']");
    for (let i = 0; i < inputLabel.length; i++) {
        //启用编辑功能
        $(inputLabel[i]).attr("disabled", false);
    }
}

/**
 * 禁用输入框输入功能
 */
function inputDisable() {
    let inputLabel = $("input[data-enable = true]");
    for (let i = 0; i < inputLabel.length; i++) {
        //启用编辑功能
        $(inputLabel[i]).attr("disabled", true);
    }
}

/**
 * 禁用指定button
 * @param id button的id
 */
function buttonDisableById(id) {
    $("#" + id).attr("disabled", true);
}

/**
 * 启用指定button
 * @param id button的id
 */
function buttonEnableById(id) {
    $("#" + id).attr("disabled", false)
}

/**
 * 清空表格
 * @param id 表格的id
 */
function clearTable(id) {
    let data = [];
    $("#" + id).bootstrapTable('load', data);
}

function clearBodyTr(id){
   $("#"+id +">tbody > tr").remove();
}

/**
 * 给指定table添加首行
 * @param id
 */
function addRowsFirst(id) {
    var _id = "#" + id;
    var flag = isEmpty(_id);
    if (flag) {
        $(_id).bootstrapTable('selectPage', 1); //Jump to the first page
        var data = {code: '', description: '', type: ''}; //define a new row data，certainly it's empty
        $(_id).bootstrapTable('prepend', data); //the method of prepend must defined all fields，but append needn't
    } else {
        $.warnMsg("请先填写未完成的行");
    }
}

/**
 * 给select下拉框赋值（单个）
 * @param data 数据
 * @param id 下拉框class名称
 * @param message 提示信息
 */
function select2SetSingleData(data, id, message) {
    //数据去重
    $.unique(data.sort());
    data.unshift('');
    //设置值
    $('.' + id).select2({
        data: data,
        width: '100%',
        theme: "bootstrap",
        language: "zh-CN",
        placeholder: message,
        allowClear: true
    });
}

/**
 * 给select下拉框赋值（单个）
 * @param data 数据
 * @param id 下拉框class名称
 * @param message 提示信息
 */
function select2SetCustomAttr(data, id, message,attr) {
    //数据去重
    $.unique(data.sort());
    data.unshift('');
    data.forEach(function (item) {
       if(item == "" ){
       		$('.' + id).append("<option value=''> </option>");
       }else{
       		$('.' + id).append("<option value="+item.id+" data-uid="+item.uid+">"+item.id+"</option>");
       }
    });
    
    //设置值
    $('.' + id).select2({
        //data: data,
        width: '100%',
        theme: "bootstrap",
        language: "zh-CN",
        placeholder: message,
        allowClear: true,
//        templateSelection: function (data, container) {
//        	$(data.element).attr('data-'+[attr], data[attr]);
//             return data.text;
//	   }
    });
}
/**
 * ajax get请求
 * @param param 参数，数组格式
 * @param url 请求地址
 */
function ajaxGet(param, url) {
    var data;
    var path = contextPath + url;
    for (let i = 0; i < param.length; i++) {
        path = path + "/" + param[i];
    }
    $.ajax({
        async: false,
        type: "get",
        url: path,
        cache: false,
        data: {},
        dataType: "json",
        success: function (result) {
            data = result;
        },
        error: function () {
            $.errMsg("操作失败！");
        }
    });
    return data;
}

/**
 * ajax post请求
 * @param param 参数，数组格式
 * @param url 请求地址
 */
function ajaxPost(param, url) {
    var data;
    $.ajax({
        async: false,
        type: "post",
        cache: false,
        url: url,
        data: param,
        dataType: "json",
        success: function (result) {
            data = result;
            if (result.code == 0) {
                // result.data;
            } else {
                $.errMsg(result.message);
            }
        },

    });
    return data;
}

/**
 * 根据codeSystem的property属性，动态生成表头
 * @param data
 */
function getColumnsByProperty(data) {
    var columns = [];
    //判断data是否为空,如果为空，默认之后checkbox，主键，编码和描述这三列。隐藏主键列
    // if(data.length == 0){
    columns.push({"checkbox": true, "width": "20"});
    columns.push({
        "field":"id",
        "title":"id",
        "visible":false
    }),
    columns.push({
        "field": "code",
        "title": "编码",
        "width": "150",
        "align": "center"
    });
    columns.push({
        "field": "display",
        "title": "描述",
        "align": "center"
    });
    for (var i = 0; i < data.length; i++) {
        if (data[i].type.toLowerCase() == 'boolean') {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "boolean",
            });
        } else if (data[i].type.toLowerCase() == 'integer') {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "integer",
            });
        } else if (data[i].type.toLowerCase() == 'coding'){
            columns.push({
                "field":"_p_"+data[i].code,
                "title":data[i].description,
                "align":"center",
                "type":"Coding"
            });
            columns.push({
                "field":"_p_"+data[i].code+"_system",
                "title":"system",
                "align":"center",
                "type":"codingParam",
                "visible":false
            });
            columns.push({
                "field":"_p_"+data[i].code+"_version",
                "title":"version",
                "align":"center",
                "type":"codingParam",
                "visible":false
            });
            columns.push({
                "field":"_p_"+data[i].code+"_display",
                "title":"display",
                "align":"center",
                "type":"codingParam",
                "visible":false
            });
        }else {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "string",
            });
        }
    }
    return columns;
}

/**
 * 根据codeSystem的property属性，动态生成表头(附class)
 * @param data
 */
function getColumnsByPropertyWithClass(data) {
    var columns = [];
    //判断data是否为空,如果为空，默认之后checkbox，主键，编码和描述这三列。隐藏主键列
    // if(data.length == 0){
    columns.push({"checkbox": true, "width": "20"});
    columns.push({
        "field":"id",
        "title":"id",
        "visible":false
    }),
    columns.push({
        "field": "code",
        "title": "编码",
        "width": "150",
        "align": "center",
        "class": "code"
    });
    columns.push({
        "field": "display",
        "title": "描述",
        "align": "center",
         "class": "display"
    });
    for (var i = 0; i < data.length; i++) {
        if (data[i].type.toLowerCase() == 'boolean') {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "boolean",
                "class": data[i].code
            });
        } else if (data[i].type.toLowerCase() == 'integer') {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "integer",
                "class": data[i].code
            });
        } else if (data[i].type.toLowerCase() == 'coding'){
            columns.push({
                "field":"_p_"+data[i].code,
                "title":data[i].description,
                "align":"center",
                "type":"Coding",
                "class": data[i].code
            });
            columns.push({
                "field":"_p_"+data[i].code+"_system",
                "title":"system",
                "align":"center",
                "type":"codingParam",
                "visible":false,
                "class": data[i].code
            });
            columns.push({
                "field":"_p_"+data[i].code+"_version",
                "title":"version",
                "align":"center",
                "type":"codingParam",
                "visible":false,
                "class": data[i].code
            });
            columns.push({
                "field":"_p_"+data[i].code+"_display",
                "title":"display",
                "align":"center",
                "type":"codingParam",
                "visible":false,
                "class": data[i].code
            });
        }else {
            columns.push({
                "field": "_p_" + data[i].code,
                "title": data[i].description,
                "align": "center",
                "type": "string",
                "class": data[i].code
            });
        }
    }
    return columns;
}

function statusModalControl(status){
    if(status == 'draft'){
        $("#startTime").show();
        $("#endTime").show();
        $("#changeStatus").empty();
        var option1 = "<option value = 'suspend'>预使用</option>";
        // var option2 = "<option value = 'actived'>正在使用</option>";
        var option3 = "<option value = 'cancel'>作废</option>";
        // var option4 = "<option value = 'retired'>退休</option>";
        var option5 = "<option value = 'start'>立即启用</option>";
        $("#changeStatus").append(option1).append(option3).append(option5);
    }else if(status == 'suspend'){
        $("#changeStatus").empty();
        var option1 = "<option value = 'cancel'>作废</option>";
        var option2 = "<option value = 'start'>立即启用</option>";
        $("#changeStatus").append(option1).append(option2);
    }else if(status == 'actived'){
        $("#startTime").hide();
        $("#endTime").hide();
        $("#changeStatus").empty();
        var option2 = "<option value = 'retired'>退休</option>";
        $("#changeStatus").append(option1).append(option2);
    }

    $("#effectiveDate").val('9999/12/12 12:00');

}

function statusModalTimeControl(status){
    if(status == 'suspend'){
        $("#startTime").show();
        $("#endTime").show();
    }else if(status == 'actived'){
        $("#startTime").hide();
        $("#endTime").hide();
    }else if(status == 'cancel'){
        $("#startTime").hide();
        $("#endTime").hide();
        $("#expirationDate").val('9999/12/12 12:00');
    }else if(status == 'retired'){
        $("#startTime").hide();
        $("#endTime").hide();
        $("#expirationDate").val('9999/12/12 12:00');
    }else if(status == 'start'){
        $("#startTime").hide();
        $("#endTime").hide();
        $("#expirationDate").val('9999/12/12 12:00');
    }
    $("#effectiveDate").val('9999/12/12 12:00');
}

function getToken(){
    var token = $.cookie('access_token');
    return 'bearer '+ token;
}

function getScope(){
	return $.cookie('scope');
}

function getRole(){
	return $.cookie('role');
}

function getOrganization(){
	return $.cookie('organization');
}

function updateMenu(){
   $("#treeTableAll__layui +.ew-tree-table").remove();
   initTableLayui();
}

function diableAuthBtn(isDisable){
	//禁用
	console.log("authBtn diabled ==" + isDisable);
	$(".authBtn").prop('disabled', isDisable);
}

function checkAuthBtn(jur){
	//沒有編輯權限鎖button
	if($.cookie('scope')!='write'){
		diableAuthBtn(true);
	}else{
		//有編輯權限
		if($.cookie('role')!='admin'){
			if(jur=='default'||jur=='系統'|| jur=='系统'){
				diableAuthBtn(true);
			}else{
				diableAuthBtn(false);
			}
		}
	}
}

function checkAuthAction(jur){
	//沒有編輯權限鎖button
	if($.cookie('scope')!='write'){
		return false;
	}else{
		//有編輯權限
		if($.cookie('role')!='admin'){
			if(jur=='default'||jur=='系統'|| jur=='系统'){
				return false;
			}
		}
	}
	return true;
}

//select 新增屬性過濾條件
function selectAppendConceptStructure(selectClass,resKind,name,version){
    var data = {};
    data.resKind = resKind ,
    data.name= name;
    data.version = version;
    var url = contextPath + "/conceptMap/getConceptStructure";
    $.ajax({
        type: "get",
        url: url,
        data: data,
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (result) {
        	//console.log(result);
        	dataResource = result.data;
        	
        	let itemProps = result["data"]["property"];
        	
        	// 编码, 描述 is preset
        	console.log(itemProps);
        	
        	$("."+selectClass).children().remove();
        	$("."+selectClass).append("<option selected>选择 resource property</option>");
        	
        	for (let elem of itemProps.entries()) {
        		$("."+selectClass).append(`<option data-type="${elem[1]["type"]}" value="${elem[1]["code"]}">
				${elem[1]["description"]}</option>`);
        	}
        	
        }// end success
    })
}

function checkVersion(e){
   console.log(e);
   $('#historyVerSelect').find('option').remove();
   let name = '';
   let terminology = e.dataset.term;
   if(terminology=='codesystem'){
        name = $("#cs_propertyName").val();
   		cs_versionSelectSource(name,"historyVerSelect");
   }
   if(terminology=='valueset'){
        name = $("#vs_propertyName").val();
   		cs_versionSelectValueSet(name,"historyVerSelect");
   }
   if(terminology=='conceptMap'){
        name = $("#cm_propertyName").val();
        cs_versionSelectConcetMap(name,"historyVerSelect");
   }
   $("#verModal").modal();
   $("#loadVerBtn").attr("data-term",terminology);
   $("#loadVerBtn").attr("data-name",name);
   
}

//查看歷史版本
function loadVerData(e){
	let version = $("#historyVerSelect option:selected").val();
	let terminology = e.dataset.term;
	let name = e.dataset.name;
	let map = {};
	map["name"] = name;
    map["version"] = version;
	if(terminology=='codesystem'){
   		let result = ajaxPost(map, contextPath + '/codeSystemData/list');
   		let data = result.data[0];
   		setCsData(data,null);
   		result = ajaxGet({}, '/codeSystemData/basicData/' + name + "/" + version);
   		propertyTableByCodeSystemStructure(result);
   		var opt = {
           url: contextPath + "/codeSystemDetails/details/table/listCodeSystemDetailsByCodeSystemId",
        }
        $("#propertyTableCS").bootstrapTable("refresh",opt);
   		return;
   }
   if(terminology=='valueset'){
   		let result = ajaxPost(map, contextPath + '/valueSet/listBySelective');
   		let data = result.data[0]; 
   		let obj = {};
   		obj.id = data._id;
   		setVsData(data);
   		propertyTableVS(obj);
   		return;
   }
   if(terminology=='conceptMap'){
        let result = ajaxPost(map, contextPath + '/conceptMap/list/table');
   		setCmData(result.data[0],false);
   		return;
   }
}