/**
 * Created by Oleg_Burshinov on 23.01.14.
 */
var ITEMS_ON_PAGE = 5;
var ASC = "ASC";
var DESC = "DESC";
var BIDDING_ROW_NUMBER = 9;
var NO_ORDER = "";
var itm_table_template;
var count = 0;
var currentUser = {};

var nextPageRequest = {};
var nextPageData = [];

var currentData = [];
var pageRequest =
{
    pageNumber: 1,
    itemsCountOnPage: ITEMS_ON_PAGE,
    order: [],
    filter: {},
    myitems: false,
    search: {
        keywordInput: "",
        shipping: 0
    },
    advancedSearch: {
        itemUID: 0,
        titleOfItem: "",
        description: "",
        minPrice: 0.0,
        maxPrice: 0.0,
        buyItNow: false,
        startDate: 0,
        expireDate: 0,
        biddersCount: 0
    },
    category: 0

};


function showItems() {
    $.get('resources/template/itmtable.tmpl', {}, function (temp) {
        // переопределяем метод, чтобы следующий вызов использовал уже готовый шаблон
        showItems = function () {
            _showItems(temp);
        };
        // вызываем обновление представления по шаблону
        showItems();
    }, 'text');
// запрещаем повторный вызов метода до загрузки шаблона
    showItems = function () {
    };

}

function _clearCookies() {
    $.removeCookie("itemUID");
    $.removeCookie("titleOfItem");
    $.removeCookie("description");
    $.removeCookie("minPrice");
    $.removeCookie("maxPrice");
    $.removeCookie("buyItNow");
    $.removeCookie("startDate");
    $.removeCookie("expireDate");
    $.removeCookie("biddersCount");
}

function formateDate(cookieName) {
    var d = new Date(parseInt($.cookie(cookieName)));
    var str = d.toJSON();
    return str;
}

function _readCookies() {
    pageRequest.advancedSearch.itemUID = $.cookie("itemUID");
    pageRequest.advancedSearch.titleOfItem = $.cookie("titleOfItem");
    pageRequest.advancedSearch.description = $.cookie("description");
    pageRequest.advancedSearch.minPrice = $.cookie("minPrice");
    pageRequest.advancedSearch.maxPrice = $.cookie("maxPrice");
    pageRequest.advancedSearch.buyItNow = $.cookie("buyItNow");
    pageRequest.advancedSearch.startDate = formateDate("startDate");
    pageRequest.advancedSearch.expireDate = formateDate("expireDate");
    pageRequest.advancedSearch.biddersCount = $.cookie("biddersCount");
}

function _showItems(itm_table_template) {
    this.itm_table_template = itm_table_template;
    sendRequest();

}

function send(){
    $.ajax({
        type: "POST",
        url: "api/item/list",
        data: JSON.stringify(pageRequest),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            currentData = clone(data);
            for(var i = 0; i < data.length; i++){
                var d = new Date(data[i].stopDate);
                data[i].stopDate = d.toLocaleDateString() +  " " + d.toLocaleTimeString();;
            }
            $('#itmesTBody').html(Mustache.to_html(itm_table_template, {item: data}));
            initBidding();
        }
    });
}

function sendRequest() {
    _readCookies();
    send();
}
function clone(obj) {
    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        var copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        var copy = [];
        for (var i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
}
function checkOrderDirection(columnName) {
    if (!pageRequest.order[0]) {
        pageRequest.order.push(columnName);
    }
    if (pageRequest.order[0] === columnName || pageRequest.order[0] === (columnName + " " + ASC)) {
        pageRequest.order[0] = columnName + " " + DESC;
    } else {
        pageRequest.order[0] = columnName + " " + ASC;
    }
}


function bidding(id) {
    function bid() {

        var field = document.getElementById("bid" + id);
        $("#bid"+id).focus(function(){
            $('#err').css('display', 'none');
        });
        var data = {
            bidderId: -1,
            itemId: id,
            bid: field.value
        };
        field.value = "";
        $.ajax({
            type: "PUT",
            url: "api/bid/add/",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data) {

                    $('#err').html("Bid success").css("color", "green").css('display', '').css("float", "right");
                    showItems();

                } else {
                    $('#err').html("Wrong bid").css("color", "red").css('display', '').css("float", "right");
                }
            }
        });

    }

    return bid;
}

function initBidding() {
    $(".Bidding").each(function () {
            var row = $(this);

            var allItems = currentData;
            var itemsTable = document.getElementById("itemTable");
            var errors = document.getElementById("err");
            var j = count;
                var notForSale = false;
                var stop = new Date(allItems[j].stopDate);
                if (stop.getTime() <= new Date().getTime()) {
                    notForSale = true;
                }
                if(allItems[j].buyItNow){
                    notForSale = false;
                }
                var biddingData = null;


                if (currentUser.login !== null && currentUser.login !== undefined) {
                    if (notForSale) {
                        biddingData = document.createTextNode("Not for sale");
                        $(this).append(biddingData);
                    } else if (allItems[j].buyItNow && !notForSale) {

                        if ((allItems[j].bidder == null) && allItems[j].seller != currentUser.login) {
                            var uid = allItems[j].uid;
                            biddingData = document.createElement("input");
                            biddingData.type = "button";
                            biddingData.className = "buttons";
                            biddingData.value = "Buy";
                            biddingData.onclick = function () {
                                var data = {
                                    bidderId: -1,
                                    itemId: uid,
                                    bid: 0
                                };
                                $.ajax({
                                    type: "PUT",
                                    url: "api/bid/add/",
                                    data: JSON.stringify(data),
                                    contentType: "application/json; charset=utf-8",
                                    dataType: "json",
                                    success: function (data) {
                                        console.log("bid!!");
                                    }
                                });
                            };

                            $(this).append(biddingData);
                        } else {
                            biddingData = document.createTextNode("Not for sale");
                            $(this).append(biddingData);
                        }

                    } else {
                        if (allItems[j].seller != currentUser.login) {
                            var div = document.createElement("div");
                            div.className = "outerBid";
                            $(this).append(div);

                            var innerDiv = document.createElement("div");
                            innerDiv.className = "fieldBid";
                            div.appendChild(innerDiv);
                            biddingData = document.createElement("input");
                            biddingData.type = "text";
                            biddingData.name = "bid";
                            biddingData.id = "bid" + allItems[j].uid;
                            biddingData.className = "bidField";
                            innerDiv.appendChild(biddingData);
                            var scdInnerDiv = document.createElement("div");
                            scdInnerDiv.className = "fieldBid";
                            div.appendChild(scdInnerDiv);
                            biddingData = document.createElement("input");
                            biddingData.type = "button";
                            biddingData.value = "Bid";

                            var id = allItems[j].uid;

                            biddingData.onclick = bidding(id);
                            scdInnerDiv.appendChild(biddingData);
                            var test = "bid" + allItems[j].uid;
                            var field = document.getElementById(test);
                            field.onfocus = function () {
                                errors.style.display = "none";
                            };
                        } else {
                            biddingData = document.createTextNode("Not for sale");
                            $(this).append(biddingData);
                        }


                    }
                } else {
                    biddingCell = row;
                    biddingData = document.createElement("a");
                    biddingData.innerHTML = "registration";
                    biddingData.onclick = function () {
                        window.location.href= "registration.htm";
                    };
                    $(this).append(biddingData);
                }

            count++;
        }

    );
    count = 0;
}
;
function nextPageCheck() {
    nextPageRequest = pageRequest;
    nextPageRequest.pageNumber++;
    $.ajax({
        type: "POST",
        url: "api/item/list",
        data: JSON.stringify(nextPageRequest),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            nextPageData = data;
        }
    });
}

$(document).ready(function () {
    showItems();
//    nextPageCheck();
    $.getJSON("currentuser",function (json) {
        currentUser = json;
    }).done(function () {
            console.log("success");
        });


    $("#pageNumber").html(pageRequest.pageNumber);
    $("#backArrow").click(function () {
        if (pageRequest.pageNumber > 1) {
            pageRequest.pageNumber--;
        }
        sendRequest();
        $('#err').css('display', 'none');
        $("#pageNumber").html(pageRequest.pageNumber);
    });

//    if(nextPageData.length == 0){
//        $("#forwardArrow").css('display', 'none');
//    } else {
//        $("#forwardArrow").css('display', '');
//    }

    $("#simpleSearch").click(function() {
        var selected = $("#shipping option:selected").val();
        switch(selected)
        {
            case "1":
                pageRequest.advancedSearch.itemUID = parseInt($("#keywordInput").val());
                send();
                break;
            case "2":
                pageRequest.advancedSearch.titleOfItem = $("#keywordInput").val();
                send();
                break;
            case "3":
                pageRequest.advancedSearch.description = $("#keywordInput").val();
                send();
                break;
        }
    });

    $("#forwardArrow").click(function () {
        pageRequest.pageNumber++;
        sendRequest();
        $('#err').css('display', 'none');
        $("#pageNumber").html(pageRequest.pageNumber);
    });

    $("#UID").click(function () {
        checkOrderDirection("UID");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Title").click(function () {
        checkOrderDirection("Title");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Description").click(function () {
        checkOrderDirection("Description");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Seller").click(function () {
        checkOrderDirection("Seller");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Start_price").click(function () {
        checkOrderDirection("Start_price");
        sendRequest();
    });
    $("#BidInc").click(function () {
        checkOrderDirection("BidInc");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Best_offer").click(function () {
        checkOrderDirection("Best_offer");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Bidder").click(function () {
        checkOrderDirection("Title");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Stop_date").click(function () {
        checkOrderDirection("Stop_date");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#Category").click(function () {
        checkOrderDirection("Category");
        sendRequest();
        $('#err').css('display', 'none');
    });
    $("#clearLink").click(function () {
        _clearCookies();
        sendRequest();
        $('#err').css('display', 'none');
    });

});