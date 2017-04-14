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
        ]
    } );

});
