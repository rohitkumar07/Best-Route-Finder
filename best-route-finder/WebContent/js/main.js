$(document).ready(function() {    	
        $('.bus-stop').keyup(function() {
        	var data =$(this).val();
        	var id = this.id;   
        	if(data != ""){
	            $.get('AjaxHandler',{ name: data} ) 
	            	.done(function(responseText) {
		                //$('#resultdiv').text(responseText);
		                var places = responseText.split(',');
		                var text = "";
		                if(responseText != ""){
			                text += '<div class="list-group">'
			                for	(index = 0; index < places.length; index++) {
			                    text += '<a href="#"  class="list-group-item place">' + places[index] + '</a>';
			                } 
			                text += '</div>';
			                text += '<script>' + "$('.place').click(function(event){$('#" +  id + "').val($(this).html());});" ;
			                text += "<\/script>";
			                $('#'+id)
				                .attr('data-content', text)
				                .popover('fixTitle').popover("show");
		                } else {
		                	$('#'+id).popover("hide");
		                }
		                
		                //alert(responseText);
		            });  
        	}
        });        
        $('#bus-search-button').click(function() {
        	var src_stop = $('#src').val();
        	var dstn_stop = $('#dstn').val();
        	var bus_type = $('input[name=type]').val();
        	var time_input = $('#time').val();
        	var am_pm = $('#am_pm').val();
        	var time = "";
        	var res = time_input.split(":");
        	//alert(res);
        	if(res.length == 2){
        		
        		var hh = res[0];
        		var mm = res[1];
        		if(hh.length == 2 && mm.length == 2){
        			var hh_int = parseInt(hh);
        			var mm_int = parseInt(mm);
        			if(hh_int <= 12 && mm_int < 60){
        				time = time_input + am_pm;
        				//alert(time_input + am_pm);
        			}
        		}
        	}
        	if(src_stop != "" && dstn_stop != "" && bus_type != "" && time!= ""){
	            $.post('mainServlet',{src: src_stop, dstn: dstn_stop, type: bus_type,time:time, pageid: "search_bus"} ) 
	            	.done(function(responseText) {            		
	            		//alert(responseText.length);
	            		var divHTML = '<table class="table  table-hover"> <caption class="large center">Bus info for <span id="src1">' + src_stop +'</span> to <span id="dstn1">' + dstn_stop + '</span> <a href="#"><span id="bookmark" class="glyphicon glyphicon-star-empty"></span></a> </caption>' +
	            		   '<thead><tr><th>Bus ID</th><th>Cost</th><th>Arrival Time</th><th>Journey Time</th></tr></thead><tbody>';
	            		   
	            		for	(index = 0; index < responseText.length; index++) {            			
	            			var response = responseText[index];
	            			var busID = response['busId'];
	            			var cost = response['totalCost'];
	            			var discount = response['discountPercentage'];
	            			var jtime = response['journeyTime'];            			
	            			var atime = response['arrivalTime']; 
	            			divHTML +='<tr><td>'+ busID +'</td><td>' + cost + '</td><td>'+ atime + '</td><td>'+ jtime + '</td><td><button onclick="pay('+ busID +','+ cost +')" class="btn btn-primary">Book</button></td></tr>';
	            		}
	            		divHTML += '</tbody></table>';
	            		divHTML += '<script>'+'var pay = function(busID,cost){'
	            						+ '$("#PayModalBody").html("Bus with bus ID = " + busID +" has been booked." );'            		
	            						+'$("#PayModal").modal();};' + '</script>';  
	            		divHTML += '<script>'
					            			+ '$("#bookmark").click(function(){'
					            	        //	+'alert("rakesh");'
					            	        	+'var src_stop = $("#src1").html();'
					            	        	+'var dstn_stop = $("#dstn1").html();'
					            	        	+'$.get("AjaxHandler",{src: src_stop, dst: dstn_stop})' 
					            	        	+'.done(function(responseText) {' 
					            	        	//+	'alert(responseText);'
					            	        	+	 'if(responseText == "success"){'
					            	            +    	'$("#bookmark").removeClass("glyphicon-star-empty");'
					            	            +	 	'$("#bookmark").addClass("glyphicon-star");'
					            	            +	 '}'
					            	            +'});'     
					            	        +'});'
	            					+'</script>';
	            		sorry = '<div class="alert alert-warning">'
							+ '<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' 				
					        + '<a href="#" class="close" data-dismiss="alert">&times;</a>'	
					        + '<strong> No bus found</strong> ' 			
					        + '</div>';
	            		$('#hide-this').hide();
	            		if(responseText.length != 0){            			
	            			$('#search-results').html(divHTML);
	            		} else {
	            			$('#search-results').html(sorry);
	            		}
		                //alert(responseText);            		
		            }); 
        	}
        });
        $('#next_bus_stop').click(function() {
        	var bus_stop = $('#last-stop').val();
        	//alert(bus_stop);
        	if(bus_stop != ""){
	            $.post('mainServlet',{src: bus_stop, pageid: "next_stop"} ) 
	            	.done(function(responseText) {            		
	            		//alert(responseText.length);
	            		if(responseText.length > 0){
		            		var newInput = '<select id="last-stop" name = "last_bus_stop" class="form-bus">';
		            		for	(index = 0; index < responseText.length; index++) {            			
		            			var response = responseText[index];
		            			newInput += '<option>'+response+'</option>';
		            			
		            		}
		            		newInput += '</select>';
		            		$('#last-last-stop').removeAttr('id');
		            		$('#last-stop').prop('disabled',true);
		            		$('#last-stop').removeAttr('name');
		            		$('#last-stop').attr('id','last-last-stop');
		            		//alert(newInput);
		        			$(newInput).insertAfter('#last-last-stop');
			                //alert(responseText);  
	            		}
		            });
        	}
        });
        
    });