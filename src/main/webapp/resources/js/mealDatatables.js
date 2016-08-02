var ajaxUrl = 'ajax/profile/meals/';
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + 'filter',
        data: $('#filter').serialize(),
        success: updateTableByData
    });
    return false;
}

$(function () {
    datatableApi = $('#datatable').DataTable(
        {
            "ajax": {
                "url": ajaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],

    "createdRow": function (row, data, dataIndex) {
        if (data.exceed) {
            $(row).css('color','red');
        }else {
            $(row).css('color','green');
        }
    },
    "initComplete": function () {
        $('#filter').submit(function () {
            updateTable();
            return false;
        });

        $('#startDate').datetimepicker({
            timepicker: false,
            format:'d.m.Y',
            inline:true,
            lang:'ru',
            closeOnDateSelect:true
        });

        $('#endDate').datetimepicker({
            timepicker: false,
            format:'d.m.Y',
            inline:true,
            lang:'ru',
            closeOnDateSelect:true
        });

        $('#startTime').datetimepicker({
            datepicker: false,
            format:'H:i',
            inline:true,
            lang:'ru',
            closeOnTimeSelect:true
        });

        $('#endTime').datetimepicker({
            datepicker: false,
            format:'H:i',
            inline:true,
            lang:'ru',
            closeOnTimeSelect:true

        });

        makeEditable();
    }
        });



});