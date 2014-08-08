/**
 * Created by Oleg_Burshinov on 28.01.14.
 */
var my_itm_table_template;

var ITEMS_ON_PAGE = 5;
var ASC = "ASC";
var DESC = "DESC";
var NO_ORDER = "";
var itm_table_template;

var currentUser = {};
var count = 0;
var currentData = [];
var pageRequest =
{
    pageNumber: 1,
    itemsCountOnPage: ITEMS_ON_PAGE,
    order: [],
    filter: {},
    myitems: true,
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


function initAction() {
    $(".Action").each(function () {
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
            var timeIsUp = notForSale;
            if (timeIsUp && (allItems[j].bidder !== null)) {
                var div = document.createElement('div');
                div.className = "action";
                var span = document.createElement('span');
                span.style.fontWeight = "bold";
                span.style.color = "blue";
                biddingData = document.createTextNode("sold");
                span.appendChild(biddingData);
                div.appendChild(span);
                $(this).append(div);
            } else if (allItems[j].seller == currentUser.login && !timeIsUp) {
                var div = document.createElement('div');
                div.className = "action";

                var editSpan = document.createElement('span');
                editSpan.style.fontWeight = "bold";
                editSpan.style.color = "blue";
                biddingData = document.createTextNode("edit");
                editSpan.onclick = editItem(allItems[j].uid);
                editSpan.appendChild(biddingData);
                div.appendChild(editSpan);
                var separator = document.createTextNode("|");
                div.appendChild(separator);
                var deleteSpan = document.createElement('span');
                deleteSpan.style.fontWeight = "bold";
                deleteSpan.style.color = "blue";
                biddingData = document.createTextNode("delete");
                deleteSpan.onclick = deleteItem(allItems[j].uid);
                deleteSpan.appendChild(biddingData);
                div.appendChild(deleteSpan);
                $(this).append(div);
            } else if (timeIsUp) {
                var div = document.createElement('div');
                div.className = "action";
                var span = document.createElement('span');
                span.style.fontWeight = "bold";
                span.style.color = "black";
                biddingData = document.createTextNode("time is up");
                span.appendChild(biddingData);
                div.appendChild(span);
                $(this).append(div);
            } else {
                var div = document.createElement('div');
                div.className = "action";
                var span = document.createElement('span');
                span.style.fontWeight = "bold";
                span.style.color = "blue";
                biddingData = document.createTextNode("bid");
                span.appendChild(biddingData);
                div.appendChild(span);
                $(this).append(div);
            }
            count++;
        }

    );
    count = 0;
}

function showItems() {
    $.get('resources/template/myitmtable.tmpl', {}, function (temp) {
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
function _showItems(my_itm_table_template) {
    this.my_itm_table_template = my_itm_table_template;
    sendRequest();

}
function sendRequest() {
    $.ajax({
        type: "POST",
        url: "api/item/mylist",
        data: JSON.stringify(pageRequest),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            currentData = clone(data);
            for(var i = 0; i < data.length; i++){
                var d = new Date(data[i].stopDate);
                data[i].stopDate = d.toLocaleDateString() +  " " + d.toLocaleTimeString();;
            }
            $('#myItmesTBody').html(Mustache.to_html(my_itm_table_template, {myitem: data}));
            initAction();
        }
    });
}

function editItem(id) {
    function editById() {
        window.location.href= "edititem.htm" + "?id=" + id;
    }

    return editById;
}

function deleteItem(id) {
    function deleteById() {
        $.ajax({
            type: "DELETE",
            url: "api/item/delete" + "?id=" + id,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data) {
                    $('#err').html("success").css("color", "green").css('display', '').css("float", "right");
                    showItems();
                } else {
                    $('#err').html("error").css("color", "red").css('display', '').css("float", "right");
                }
            }
        });
    }

    return deleteById;
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

$(document).ready(function () {
    showItems();
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
    $("#forwardArrow").click(function () {
        pageRequest.pageNumber++;
        sendRequest();
        $('#err').css('display', 'none');
        $("#pageNumber").html(pageRequest.pageNumber);
    });
});