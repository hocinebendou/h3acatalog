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
    
    $('#case-summary').DataTable( {
        columnDefs:[
        	{"type": "num"}
        ],
    	'paging': false,
        'ordering': false,
        'info': false,
        'bFilter': false,
    	"bLengthChange": false
    } );
    
    $('#ctl-summary').DataTable( {
        columnDefs:[
        	{"type": "num"}
        ],
    	'paging': false,
        'ordering': false,
        'info': false,
        'bFilter': false,
    	"bLengthChange": false
    } );
    
    $("div.alert-card .close").click(function(){
        $(this).closest("div.alert-card").fadeOut("slow")
    });

    $("button[id^='process-csv']").click(function () {
        $("input[name='delete']").remove();
    });

    $("button[id^='delete-csv']").click(function () {
        $("input[name='process']").remove();
    });
});
