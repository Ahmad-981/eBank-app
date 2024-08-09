(function() {

    'use strict';

    var self = {};

    self.init = function() {
        $("#execute").click(self.execute);
    }

    self.execute = function() {
        var request = { url: $("#url").val() };
        request.method = $("#method").val();
        if ("GET" != request.method) {
            request.processData = false;
            request.headers = { "Content-Type": $("#contentType").val(), "X-XSRF-TOKEN": $.cookie("XSRF-TOKEN") };
            request.data = $("#requestBody").val();
        }
        $.ajax(request).done(function(data, textStatus, xhr) {
            $("#response").text(JSON.stringify(data));
        }).fail(function(xhr, textStatus, errorThrown) {

        });
    }

    $(document).ready(self.init);

}());