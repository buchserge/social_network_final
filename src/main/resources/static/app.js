var stompClient = null;


function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        getUnreadMessages();
        stompClient.subscribe('/user/queue/hello',function (messageOutput) {
        var body= JSON.parse(messageOutput.body);

        if(body.chatroomId == usrName+"_"+recipient || body.chatroomId == recipient+"_"+usrName ){
            showMessageOutput(body);
            };
})
//        stompClient.subscribe('/topic/hello', function (messageOutput) {
//            showMessageOutput(body);
//            });
  });

}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function fetchUnreadMessages(){
$.ajax({
    type: "GET",
    url: '/api/getUnreadMessages',
    datatype : "application/json",
    data: {'id':recipientId},
    cache: false,
    success: function(result){

    }
});}

function getUnreadMessages(){
setTimeout(fetchUnreadMessages(), '11000');
}



function sendMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({"text":  $( "#moi" ).val(),"type":"CHAT",'receiptId':recipientId,'senderId':senderId,
    'senderName':usrName,'recipientName':recipient}));
}


function      sendName(){
 stompClient.send("/app/name", {}, JSON.stringify({'text': $( "#moi" ).val(),"type":"NAME"}));
}


 function showMessageOutput(messageOutput) {
 var text=messageOutput.text;
 var response=document.createTextNode(text);
 addImage(response,messageOutput);
 }




function addImage(response,messageOutput) {

var base= document.getElementById('base');
var divBase= document.createElement('div');
if(messageOutput.senderName==usrName){
divBase.setAttribute("class","d-inline-flex  m-2");

}
else{
divBase.setAttribute("class","d-inline-flex flex-row-reverse  m-2");
}


var divImg= document.createElement('div');
var img= document.createElement('img');


img.setAttribute("src",'http://localhost:8080/api/image/getImageChat/' +  messageOutput.senderId);
img.setAttribute("height",'60');
img.setAttribute("width",'60');
img.setAttribute("class",'rounded-circle');
divImg.appendChild(img);

var divAlert= document.createElement('div');
divAlert.setAttribute("class"," z-3 bg-alert ");

var divFlexColumn= document.createElement('div');
divFlexColumn.setAttribute("class","d-flex flex-column position-relative ");

var span= document.createElement('span');
if(messageOutput.isRead==false){
span.setAttribute("class","text-bg-primary rounded-4  mt-2 mx-3 px-3 p-2 ");
}
else{span.setAttribute("class","text-bg-muted rounded-4 border mt-2 mx-3 px-3 p-2 ");
}
span.appendChild(response);
divFlexColumn.appendChild(span);
divAlert.appendChild(divFlexColumn);
divBase.appendChild(divImg);
divBase.appendChild(divAlert);
base.appendChild(divBase);
}



$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();});
    $( "#send" ).click(function() { sendMessage(); });
    connect();
});