$(function() {

    "use strict";

    $('.search-icon').on('click', function() {
        $('#search-form').fadeToggle( "50", "linear" );
    });


    $('#example').DataTable( {
        columnDefs: [
            {
                targets: [ 0, 1, 2 ],
                className: 'mdl-data-table__cell--non-numeric'
            }
        ],
    	"bLengthChange": false
    } );

    $("div.alert-card .close").click(function(){
        $(this).closest("div.alert-card").fadeOut("slow")
    });

    $("#process-csv").click(function () {
        $("input[name='delete']").remove();
    });

    $("#delete-csv").click(function () {
        $("input[name='process']").remove();
    })
});
