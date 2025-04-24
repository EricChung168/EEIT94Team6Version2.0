$(function () {
	$("#datepicker").datepicker({ dateFormat: 'yy-mm-dd'});
	        $("#from-datepicker").on("change", function () {
	            var fromdate = $(this).val();
	            alert(fromdate);
	        });
	        //點選日期改時間
	 $("#datepicker").on("change",function () {
        	let selectdate=$("#datepicker").val();
        	console.log(selectdate)
        	$("#time").empty();
            $.ajax({
                url: '/booktick/time/'+selectdate,
                
                dataType: 'json',
                type: 'post',
                contentType: 'application/json', 
                success: function (respones) {
                	$('#time').append(`<option value="">場次</option>`);
					$.each(respones,function(i,e){
						
						$('#time').append(`<option value="${e.showtimeid}">${e.starttime}</option>`)
					})
                }
    
            })
    });
    //ready畫面加載預設時間
    
        ///
        //廳
		function name(){
		$('#moviename').empty();
        	let hall=$("#halls").val();
        	$.ajax({
                url: '/booktick/name',
                data:{hallid:hall},
                dataType: 'json',
                type: 'GET',
                success: function (respones) {
                	
                		
					$('#moviename').append(`<option value="${respones.movieid}">${respones.moviename}</option>`)
                	
					
                }
            })
	}
        
	$("#halls").on("change",function(){
        name();
		$('#moviename').empty();
        })


		$(document).ready(function () {
			
			$.ajax({
                    url: '/booktick/hall',
                    dataType: 'json',
                    type: 'GET',
                    success: function (respones) {
						$.each(respones,function(i,e){
		                    //console.log(e.hallid);
							$('#halls').append(`<option value="${e.hallid}" >${e.hallid}</option>`)
							
        	
        	
        	//console.log(hall)
        	
					});
					let hall=$("#halls").val();
					/////
	 	
					/////
					
					
            /////////////////////////////////////////////
           
           
				////////////////////////////////////////	
                }
            })
    });

    //////

        
        
        
        
        
		let money=[];

function moneyse(){
	let tp=	$('#type').val();
        let time=$('#time').text();
        //console.log(time)
        let mf=0
        	if(tp==1){
        		
        		mf=money[tp-1];
        		$("#money").val(mf)
        	console.log()      
        	}
        	else if(tp==2){
        		
        		mf=money[tp-1];
        		$("#money").val(mf)
            	}	
			else if(tp==3){
        		
				mf=money[tp-1];
				$("#money").val(mf)
            	}else if(tp==4){
            		
            		mf=money[tp-1];
            		$("#money").val(mf)
                	}
}


		$(document).ready(function () {
			
                $.ajax({
                    url: '/booktick/type',
                    dataType: 'json',
                    type: 'GET',
                    success: function (respones) {
						$.each(respones,function(i,e){
							
							
							//console.log(ticktyp)
							let se=e.tickettypeid
							
							//console.log(ticktyp)
							//console.log(se)
							//console.log(selectty)
							$('#type').append(`<option value="${e.tickettypeid}">${e.tickettype}</option>`)
							money.push(e.moneytype)
							moneyse();
					})
                }
            })
    });
   
	$('#type').on("change",function(){
        moneyse();
        })

	
/////
    });