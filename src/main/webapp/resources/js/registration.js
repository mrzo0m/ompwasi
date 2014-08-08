/**
 * Created by Oleg_Burshinov on 22.01.14.
 */
var delay = 500;
var isLoading = false;
var isDirty = false;
$(document).ready(function () {
    checkLoginUnique();
});

function reloadSearch() {
    if (!isLoading) {
        var q = $('#login').val();
        if (q.length >= 3) {
            isLoading = true;
            // ajax fetch the data
            $.ajax({
                url: 'api/user/' + q,
                type: 'GET',
                context: this,
                success: function (data) {
                    if (data.fullName == null) {
                        $('#searchlogin').html("OK").css("color", "green");
                    } else {
                        $('#searchlogin').html("already exists").css("color", "red");
                    }

                }
            });

            // enforce the delay
            setTimeout(function () {
                isLoading = false;
                if (isDirty) {
                    isDirty = false;
                    reloadSearch();
                }
            }, delay);
        }
    }
}
function checkLoginUnique() {
    reloadSearch();

    $('#login').keyup(function () {
        isDirty = true;
        reloadSearch();
    });
    $('#login').blur(function () {
        isDirty = true;
        reloadSearch();
    });
    $('#login').focus(function () {
        $('#searchlogin').empty()
    });
}