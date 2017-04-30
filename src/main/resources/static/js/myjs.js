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
    
    //charts
    $("div.summary-charts > a").click(function() {
    	var containerId = $(this).closest("div").next().attr('id');
    	$('#' + containerId).fadeToggle();
    	var id = $(this).attr('id');
    	console.log(containerId);
    	$.get("/api/study/" + id, function (data) {
    		if (!data ) return;
    		var countries = [];
    		var series = [];
    		$.each(data, function(k, v){
    			countries.push(k);
    			series.push(v);
    		});
    		Highcharts.chart(containerId, {
    			chart: {
    		        type: 'column'
    		    },
    		    title: {
    		        text: 'Samples by countries'
    		    },
    		    xAxis: {
    		        categories: countries,
    		        title: {
    		            text: null
    		        }
    		    },
    		    yAxis: {
    		        min: 0,
    		        title: {
    		            text: 'Number of samples'
    		        }
    		    },
    		    legend: {
    		        shadow:false
    		    },
    		    tooltip: {
    		        shared: true
    		    },
    		    plotOptions: {
                    column: {
                        grouping: false,
                        shadow: false,
                        borderWidth: 0
                    }
                },
    		    series: [{
    		    	name : 'Number samples by country',
    		    	color: '#1E6FA8',
    		    	data: series,
    		    	pointPadding: 0.3,
                    pointPlacement: 0
    		    }]
    		});
    	});
    });
});
