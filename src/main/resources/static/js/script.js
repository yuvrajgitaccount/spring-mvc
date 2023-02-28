console.log("it is scripting file")

function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
}

function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
};


function search()
{
	
      let query= $("#search-input").val();
   
      
      
      if(query=='')
    	  {
    	  
    	  $(".search-result").hide();
    	  
    	  }else
    		  {
    		  //sarch
    		   console.log(query);
    		   
    		   //sending the request to the server
    		   
    		   let url=`http://localhost:8282/search/${query}`;
    		   
    		   fetch (url).then((response)=>{
    			   return response.json();
    			   
    			   
    		   }).then((data)=>{
    			   
    			   //data.....
    			   console.log(data);
    			   
    			   let text=`<div class='list-group'>`;
    			   
    			   
    			   data.forEach((contact)=>{
    				   
    				   text +=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`
    			   });
    			   text +=`</div>`;
    			   
    			   $(".search-result").html(text);
    			   
    			   $(".search-result").show();
    		   });
    		 
    		  }

};


//first request server to create order id
const paymentStart =()=>
{
	console.log("payment started");

	let amount=$("#payment_id").val();
	console.log(amount);

	if(amount ==''|| amount==null)
	{
		alert("ammount is required");

		return;
	}


$.ajax(
	{
		url:'/user/create_order',
		data:JSON.stringify({amount:amount,info:'order_request'}),
		contentType:'application/json',
		type:'POST',
		dataType:'json',
		success:function(response)
		{
			//invoked when success
			console.log(response)
		},

		error:function(error)
		{
			//invoked when error

			console.log(error)
			alert("something went wrong ||")
		}
	}



)


};